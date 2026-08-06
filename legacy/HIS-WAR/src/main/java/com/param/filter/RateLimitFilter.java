package com.param.filter;

import com.param.exception.ContentCachingRequestWrapper;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rate limiting for the pre-login credential endpoints (PHASE 6 / 6A).
 *
 * TARGETS — the explicit table of the real request forms confirmed by the 2026-08-06
 * allowlist audit (no substring matching; a path is rate-limited ONLY if it is in this
 * table, and the forms are the live controller mappings):
 *   LOGIN group  /adt/userLogin, /adt/getUnitByCredentials, /ldapUserCredentialCheck, /login
 *                per-IP: 30 burst, refill 10/min; per-account: 5 burst, refill 5/15 min
 *   ORACLE group /adt/checkEmailAvailability (the account-existence oracle)
 *                per-IP: 10 burst, refill 2/min; no per-account dimension (the payload is
 *                email-only and the oracle is cheap to rate at the IP level)
 *   OTP group    /adt/forgotPasswordGeneration, /adt/otpVerification
 *                per-IP: 10 burst, refill 2/min; per-account: 5 burst, refill 5/15 min
 *
 * COUNTING is attempts-based: every request to a target consumes one token per applicable
 * bucket, regardless of outcome (401/404/200/500). This is deterministic and needs no
 * response wrapping; a failure-aware counter would add response inspection for marginal
 * value — an attacker's failed guesses and a legitimate user's typos both consume budget,
 * which is the point.
 *
 * PER-ACCOUNT KEY: organizationId + ":" + unitId + ":" + account-id read from the POST
 * body (the request is wrapped with the existing ContentCachingRequestWrapper so the body
 * is read once here and still served downstream). For the LOGIN group the account-id is
 * the encrypted userName — the frontend encrypts with JSEncrypt (RSA PKCS#1 v1.5, fixed
 * public key), which is DETERMINISTIC (no IV/randomness), so the same user produces the
 * same ciphertext on every attempt and the key is stable (verified 2026-08-06 before
 * building). For the OTP group the account-id is the emailId (the payloads carry
 * emailId, not userName). If the body is unparseable or the fields are absent, only the
 * per-IP bucket applies (fail-open on the account dimension, never fail-closed on a
 * malformed body).
 *
 * NAT HAZARD (why the per-IP numbers are what they are): an office behind one NAT shares
 * one public IP, so a tight per-IP login bucket would lock everyone out after a few
 * attempts. The raised per-IP login burst (30) clears NAT bursts, and the per-account
 * bucket is NAT-immune — one account = one bucket regardless of the source IP — so a
 * shared NAT cannot trip it (one account, one bucket). Residual: a user with repeated
 * typos waits 15 minutes for the account bucket to refill (self-releasing; no admin
 * intervention). A known-large NAT (e.g. a hospital wing) is one config constant if it
 * ever needs raising.
 *
 * IN-MEMORY rationale: this stack has no shared cache/Redis in the deployment; buckets
 * live in ConcurrentHashMaps keyed per process. A restart resets all counters (a
 * documented, accepted limitation — an attacker cannot persist a lockout across a
 * restart, and a legitimate user is never locked out by a restart). Single-node
 * deployment today; if the app ever runs multi-node, the per-account cache must move to
 * a shared store.
 */
public class RateLimitFilter implements Filter {

    private static final class TargetConfig {
        final int ipBurst;
        final int ipRefillPerMin;
        final boolean accountDim;
        final int accountBurst;
        final int accountRefillPerWindow;
        final Duration accountWindow;

        TargetConfig(int ipBurst, int ipRefillPerMin, boolean accountDim,
                     int accountBurst, int accountRefillPerWindow, Duration accountWindow) {
            this.ipBurst = ipBurst;
            this.ipRefillPerMin = ipRefillPerMin;
            this.accountDim = accountDim;
            this.accountBurst = accountBurst;
            this.accountRefillPerWindow = accountRefillPerWindow;
            this.accountWindow = accountWindow;
        }
    }

    /** The explicit target table — the 7 audited real forms. */
    private static final Map<String, TargetConfig> TARGETS = new HashMap<>();

    static {
        TargetConfig login = new TargetConfig(30, 10, true, 5, 5, Duration.ofMinutes(15));
        TARGETS.put("/adt/userLogin", login);
        TARGETS.put("/adt/getUnitByCredentials", login);
        TARGETS.put("/ldapUserCredentialCheck", login);
        TARGETS.put("/login", login);

        TargetConfig oracle = new TargetConfig(10, 2, false, 0, 0, null);
        TARGETS.put("/adt/checkEmailAvailability", oracle);

        TargetConfig otp = new TargetConfig(10, 2, true, 5, 5, Duration.ofMinutes(15));
        TARGETS.put("/adt/forgotPasswordGeneration", otp);
        TARGETS.put("/adt/otpVerification", otp);
    }

    private static final Pattern ORG_ID = Pattern.compile("\"organizationId\"\\s*:\\s*(\\d+)");
    private static final Pattern UNIT_ID = Pattern.compile("\"unitId\"\\s*:\\s*(\\d+)");
    private static final Pattern USER_NAME = Pattern.compile("\"userName\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern EMAIL_ID = Pattern.compile("\"emailId\"\\s*:\\s*\"([^\"]+)\"");

    /** per-IP buckets: key = ip + "|" + path. */
    private final Map<String, Bucket> ipCache = new ConcurrentHashMap<>();
    /** per-account buckets: key = orgId:unitId:accountId + "|" + path. */
    private final Map<String, Bucket> accountCache = new ConcurrentHashMap<>();

    private static Bucket createBucket(int burst, int refillPerWindow, Duration window) {
        Bandwidth limit = Bandwidth.classic(burst, Refill.greedy(refillPerWindow, window));
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String path = httpRequest.getRequestURI();
        if (path.length() > 1 && path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }

        TargetConfig config = TARGETS.get(path);
        if (config == null) {
            chain.doFilter(request, response);
            return;
        }

        // Wrap so the body can be read once here and still served to the downstream filter/controller.
        ContentCachingRequestWrapper wrapped = new ContentCachingRequestWrapper(httpRequest);

        String ip = httpRequest.getRemoteAddr();
        Bucket ipBucket = ipCache.computeIfAbsent(ip + "|" + path, k ->
                createBucket(config.ipBurst, config.ipRefillPerMin, Duration.ofMinutes(1)));
        if (!ipBucket.tryConsume(1)) {
            httpResponse.setStatus(429);
            httpResponse.getWriter().write("Too many requests");
            return;
        }

        if (config.accountDim) {
            String accountKey = readAccountKey(wrapped, path);
            if (accountKey != null) {
                Bucket accountBucket = accountCache.computeIfAbsent(accountKey + "|" + path, k ->
                        createBucket(config.accountBurst, config.accountRefillPerWindow, config.accountWindow));
                if (!accountBucket.tryConsume(1)) {
                    httpResponse.setStatus(429);
                    httpResponse.getWriter().write("Too many requests");
                    return;
                }
            }
        }

        chain.doFilter(wrapped, response);
    }

    /**
     * Reads the POST body once (the wrapper caches it for the downstream) and derives the
     * account key: orgId:unitId:userName for the LOGIN group, orgId:unitId:emailId for the
     * OTP group. Returns null when the body is unreadable or the account field is absent —
     * the per-IP bucket then applies alone.
     */
    private String readAccountKey(ContentCachingRequestWrapper wrapped, String path) throws IOException {
        String body;
        try {
            byte[] content = wrapped.getContentAsByteArray();
            if (content == null || content.length == 0) {
                byte[] buf = new byte[4096];
                int n = wrapped.getInputStream().read(buf);
                body = n > 0 ? new String(buf, 0, n, StandardCharsets.UTF_8) : "";
            } else {
                body = new String(content, StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            return null;
        }
        if (body == null || body.isEmpty()) {
            return null;
        }
        String orgId = first(ORG_ID, body);
        String unitId = first(UNIT_ID, body);
        String accountId = (path.startsWith("/adt/forgotPasswordGeneration") || path.startsWith("/adt/otpVerification"))
                ? first(EMAIL_ID, body)
                : first(USER_NAME, body);
        if (accountId == null) {
            return null;
        }
        return (orgId != null ? orgId : "") + ":" + (unitId != null ? unitId : "") + ":" + accountId;
    }

    private static String first(Pattern p, String body) {
        Matcher m = p.matcher(body);
        return m.find() ? m.group(1) : null;
    }

    @Override
    public void destroy() {
    }
}
