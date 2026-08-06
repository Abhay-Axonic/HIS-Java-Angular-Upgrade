package com.param.filter;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.param.global.utility.UserComponent;
import in.param.exception.ApplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import com.param.security.config.JwtTokenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * BR-173 PHASE 2 — default-deny rewrite (owner-approved 2026-08-05, QF-AJ).
 *
 * Structure:
 *   1. A request is public ONLY if its normalized URL is in PUBLIC_ALLOWLIST (the approved 83)
 *      or matches one of the two narrow feedback path-variable patterns.
 *   2. Everything else falls through to the existing token check (Bearer token validated against
 *      the userId/unitId headers) — unchanged from the pre-rewrite filter.
 *   3. Anything that fails both is 401. Fail closed.
 *
 * The old contains() whitelist chains (both copies) are removed entirely; no substring whitelist
 * logic survives in this filter.
 */
public class JwtTokenFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);

	/**
	 * The exact-match allowlist (BR-173 Phase 2 + the 2026-08-06 allowlist audit).
	 * Ordered: writes first, then PHI reads, reference/config, then static.
	 * The stored forms are POST-normalization (host-stripped, query-stripped, trailing-slash-trimmed).
	 * COMPOSITION: 46 exact entries (42 API/static + 3 shell + 1 login) + 5 prefixes + 7 patterns.
	 * Every exact form here was audit-verified against its controller's class-level
	 * @RequestMapping + method mapping AND the form a real caller produces (the
	 * 2026-08-06 audit corrected 18 entries: 9 class-prefix-dropped forms corrected,
	 * 3 pattern-needed forms moved to anchored patterns, 2 redundant colon-literals
	 * removed, the feedback base forms removed, 8 never-resolving /HIS-WAR/ swagger/
	 * config entries dropped, 3 no-pre-login-caller entries sent to DENY). The two
	 * /Billing/* M-Pesa callback entries were removed 2026-08-06 with the
	 * abandoned-in-baseline M-Pesa controller (fix/dead-code-removal) — the callbacks
	 * no longer resolve anywhere; the M-Pesa DAO's live methods (validateMpesaTrancastion,
	 * updateCCAvenue) are not web-allowlisted and need no exemption.
	 */
	private static final Set<String> PUBLIC_ALLOWLIST = new LinkedHashSet<>(List.of(
		// WRITES
			"/api/abdm/saveDetailsOfAbdm",
			"/api/abdm/v3/updateStatus",
			"/api/billing/kiosk/saveAndPay/future",
			"/api/inPatientFeedback/saveInPatientFeedback",
			"/api/ipd/saveMyLatestMedicalSummaryCallBack",
			"/api/mylife/sync/appointment/cancelorRescheduleAppointmentFromApp",
			"/api/mylife/sync/savePatientThroughMylife",
			"/api/opd/saveAIAppointmentInQueue",
			"/api/outPatientFeedback/saveOutPatientFeedback",
			"/api/saveTwilioMsg",
			"/adt/checkEmailAvailability",
			"/api/global/updatePatientRegistrationFromMyLife",
			"/api/billing/kiosk/saveAndPay",
			"/api/global/patientRegistration",
			"/api/opd/appointment/check",
			"/api/abdm/request/otp",
			"/adt/forgotPasswordGeneration",
			"/adt/otpVerification",
		// PHI READS
			"/api/global/patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn",
			"/api/abdm/dataPush",
			"/api/abdm/getHiuDecryptedData",
			"/api/opd/getAIAppointmentCount",
			"/api/opd/patientDoc/fromMylife",
			"/api/webappointment/getDetailsForBookAppointment",
			"/api/global/patientTypeHeadSearchForAppointmentUnique",
			"/api/billing/mylife/sync/appointment",
			"/adt/getActivePrefixList",
			"/api/billing/getBillNoByPatient",
			"/api/billing/getBillReceiptBytesMyLife",
		// REFERENCE / CONFIG
			"/api/createOrganizationFromWeb",
			"/ldapUserCredentialCheck",
			"/adt/getActivePrefixListForWebPage",
			"/adt/getActiveRelationListForWebPage",
			"/adt/getListOfDocumentForBookView",
			"/adt/getUnitByCredentials",
			"/adt/userLogin",
			"/api/doctorApp/getApplicationConfig",
			"/api/getVideoataForNotification",
			"/api/global/appoitments/doctors",
			"/api/opd/coversheetInstructionListForBookView",
			"/api/opd/getMedicationHistoryListForBookView",
			"/getOrderIdUnitSubscriptionPay",
		// LOGIN (exact — the Duo second-factor fallback, loginController.js:542;
		// in-memory state only, no DB write; PHASE 6: rate-limit with the other
		// credential endpoints)
			"/login",
		// SHELL (exact — the app entry pages)
			"/",
			"/index.html",
			"/healthcheck.html"
	));

	/**
	 * STATIC PREFIX RULES — the shell's static directory trees.
	 * COMPOSITION: 67 exact entries (63 API/static + 3 shell + 1 login fallback)
	 * + 5 prefix rules + 4 anchored patterns
	 * (the original 83-exact shape: the 20 webjar entries collapsed into the
	 * /HIS-WAR/webjars/ prefix; /webjars/ added for the spring webjars-resource-
	 * handler path, the same static tree under its other serving form; the
	 * org-master subdomain + /checkParti patterns + the /login exact added for
	 * the login submit path; /fonts/ added as the 5th prefix — the
	 * web-appointment inline @font-face tree).
	 *
	 * CONSTRAINT — READ BEFORE ADDING A PREFIX: prefix matching is permitted ONLY
	 * for static directory trees served by the default servlet (files on disk:
	 * templates, js, css, fonts, images). NEVER for API paths. The
	 * substring-over-API-paths behaviour is exactly what this rewrite replaced;
	 * an API endpoint must be an exact entry with a named pre-login caller.
	 */
	private static final List<String> STATIC_PREFIXES = List.of(
			"/resources/",
			"/views/",
			"/HIS-WAR/webjars/",
			"/webjars/",
			// /fonts/ — the web-appointment.html inline @font-face blocks reference
			// ../fonts/ relative to /views/AppointmentScheduling/, resolving to the
			// root /fonts/ tree (the same default-servlet static class; catalina
			// showed the 401s). NOTE: the font FILES are absent from the WAR and
			// the deployed ROOT — this prefix fixes the auth answer only; the
			// requests 404 (browser falls back to the system font). Content gap
			// logged as QF-AI, no fix.
			"/fonts/"
	);

	/**
	 * The narrow anchored patterns — SEVEN total, each replacing a path-variable
	 * shape that exact entries cannot enumerate, each with a named pre-login caller:
	 *   1.   OPD feedback GET, 6 variable segments — outPatientFeedbackController.js
	 *   2.   IPD feedback GET, 6 variable segments — inPatientFeedbackController.js
	 *   3.   org-master list subdomain (loginController.js:66 appends the hostname —
	 *        localhost/smeabc/qa/intdev/demo)
	 *   4.   Twilio participant check (twilio-video.js:31032 bare fetch)
	 *   5.   bookView investigations list, 4 variable segments (bookViewMasterController.js:1664
	 *        appends patientId/orgId/fromDate/toDate — the pre-login booking flow)
	 *   6.   Twilio video token (twilio-video.js:30732 fetch — /token/{identity}/{room},
	 *        the same video-consult flow as #4; the bare /token/Doctor exact could never
	 *        match the two variable segments)
	 *   7.   kiosk QR barcode (selfRegistrationController.js:5173 builds /adt/br/{uniqueId}
	 *        as the QR link the patient's browser opens — pre-login; the bare /adt/br
	 *        exact could never match the variable segment)
	 * A pattern is justified ONLY for a genuinely variable segment on a pre-login
	 * caller's path; anything enumerable must be an exact entry. A pattern
	 * facility that grows without that justification becomes the next contains()
	 * chain. The 2026-08-06 audit DENIED two candidate patterns for lacking a
	 * named pre-login caller: /api/global/getVideoConsultationLinkFromHIS/{uuid}
	 * (zero callers anywhere in the webapp) and
	 * /LIS/global/getTotalLanguageMasterRecordActive/{orgId}/{unitId} (post-login
	 * LIS screens only — they authenticate and need no exemption).
	 */
	private static final Pattern OPD_FEEDBACK_PATH = Pattern
			.compile("^/api/outPatientFeedback/[^/]+/[^/]+/[^/]+/[^/]+/[^/]+/[^/]+$");
	private static final Pattern IPD_FEEDBACK_PATH = Pattern
			.compile("^/api/inPatientFeedback/[^/]+/[^/]+/[^/]+/[^/]+/[^/]+/[^/]+$");
	private static final Pattern ORG_MASTER_LIST_PATH = Pattern
			.compile("^/adt/organizationMasterList/[^/]+$");
	private static final Pattern CHECKPARTI_PATH = Pattern
			.compile("^/checkParti/[^/]+$");
	private static final Pattern BOOKVIEW_INVESTIGATIONS_PATH = Pattern
			.compile("^/api/opd/getInvestigationsListForBookView/[^/]+/[^/]+/[^/]+/[^/]+$");
	private static final Pattern TWILIO_TOKEN_PATH = Pattern
			.compile("^/token/[^/]+/[^/]+$");
	private static final Pattern ADT_BR_PATH = Pattern
			.compile("^/adt/br/[^/]+$");

	/**
	 * URL normalization applied BEFORE matching, in this order:
	 *   1. host-strip — the caller may send the absolute form (e.g. http://host/HIS-WAR/api/...);
	 *      the request URI is already host-less, but this makes the match immune to the form.
	 *   2. query-strip — callers append query strings (?unitId=...); the match is against the path.
	 *   3. trailing-slash-trim — callers and the allowlist disagree on the trailing slash
	 *      (/adt/getActiveCountryList/ vs /adt/getActiveCountryList).
	 * The allowlist was derived with exactly this normalization (QF-AH), so the stored entries are
	 * the post-normalization forms.
	 */
	private static String normalizeUrl(String url) {
		String u = url;
		// 1. host-strip
		int schemeEnd = u.indexOf("://");
		if (schemeEnd >= 0) {
			int pathStart = u.indexOf("/", schemeEnd + 3);
			u = pathStart >= 0 ? u.substring(pathStart) : "";
		}
		// 2. query-strip
		int queryStart = u.indexOf('?');
		if (queryStart >= 0) {
			u = u.substring(0, queryStart);
		}
		// 3. trailing-slash-trim
		if (u.length() > 1 && u.endsWith("/")) {
			u = u.substring(0, u.length() - 1);
		}
		return u;
	}

	private static boolean isStaticPrefix(String url) {
		for (String prefix : STATIC_PREFIXES) {
			if (url.startsWith(prefix)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String domain = null;

		String originName = request.getHeader("Origin");
		//HIS-37402 Security - Missing Security Header in response
		response.addHeader("Strict-Transport-Security","max-age=31536000");
		response.addHeader("Content-Security-Policy", "object-src 'none'; frame-ancestors *");
		response.addHeader("X-XSS-Protection","1; mode=block");
		response.addHeader("X-Frame-Options", "SAMEORIGIN");
		response.addHeader("X-Content-Type-Options","nosniff");

		try {
			final String authorizationHeader = request.getHeader("Authorization");
			String requestUrl = request.getRequestURI();
			String[] splitUrl = requestUrl.split("/");

			if (request instanceof HttpServletRequest servletRequest) {
				String url = servletRequest.getRequestURL().toString();

				domain = url.replace(requestUrl, "");
				boolean isdomainAllowed = checkDomain(UserComponent.getAllowedDomains(),domain);
				if(! isdomainAllowed) {

					throw new ApplicationException(domain+" Not Allowed Please Contact Administrator.");
				}

				if(originName != null){
					boolean isoriginNameAllowed = checkDomain(UserComponent.getAllowedDomains(),originName);
					if(! isoriginNameAllowed){
						System.out.println("Origin "+originName +" Not Allowed ... for domain "+domain);
						throw new ApplicationException(" Origin "+originName+" Not Allowed Please Contact Administrator.");
					}
				}

			}

			Integer userId = request.getHeader("userId") != null ? Integer.valueOf(request.getHeader("userId")) : 0;
			Integer unitId = request.getHeader("unitId") != null ? Integer.valueOf(request.getHeader("unitId")) : null;

			// BR-173 PHASE 2 — default-deny: the allowlist + the static-tree prefixes are the
			// ONLY public-by-design paths
			String normalizedUrl = normalizeUrl(requestUrl);
			boolean isPublic = PUBLIC_ALLOWLIST.contains(normalizedUrl)
					|| isStaticPrefix(normalizedUrl)
					|| OPD_FEEDBACK_PATH.matcher(normalizedUrl).matches()
					|| IPD_FEEDBACK_PATH.matcher(normalizedUrl).matches()
					|| ORG_MASTER_LIST_PATH.matcher(normalizedUrl).matches()
					|| CHECKPARTI_PATH.matcher(normalizedUrl).matches()
					|| BOOKVIEW_INVESTIGATIONS_PATH.matcher(normalizedUrl).matches()
					|| TWILIO_TOKEN_PATH.matcher(normalizedUrl).matches()
					|| ADT_BR_PATH.matcher(normalizedUrl).matches();

			if (isPublic) {
				chain.doFilter(request, response);
			} else if (unitId != null && userId != null && authorizationHeader != null) {
				// the existing token check — unchanged from the pre-rewrite filter
				String username = null;
				String token = null;

				if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
					token = authorizationHeader.substring(7);
				} else {
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
				}

				if (JwtTokenUtil.validateToken(token, userId, unitId)
						&& SecurityContextHolder.getContext().getAuthentication() == null) {
					// Validate the token and set up the Spring Security context
					username = JwtTokenUtil.extractUsername(token);
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username,
							null, null);
					authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authToken);
					chain.doFilter(request, response);
				} else if (JwtTokenUtil.validateToken(token, userId, unitId)
						&& SecurityContextHolder.getContext().getAuthentication() != null) {
					chain.doFilter(request, response);
				} else {
					System.out.println(
							"UNAUTHORIZED ##" + requestUrl + "##" + splitUrl.length + "##" + userId + "##" + unitId);
					response.setStatus(HttpStatus.UNAUTHORIZED.value());
				}

			} else {
				System.out.println(
						"UNAUTHORIZED ##" + requestUrl + "##" + splitUrl.length + "##" + userId + "##" + unitId);
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
			}
		}
		catch(ApplicationException ae){
			logger.error(ae.getMessage(), ae);
			response.sendError(502,domain+" Not Allowed Please configure "+ae.getMessage());
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
	}

	private boolean checkDomain(List<String> allowedDomainsList, String domain) throws ApplicationException {
		String plainDomain = domain.replace("//", "");
			String [] domainAray = plainDomain.split(":");
		String finalIp = null;
			if(domainAray.length>1) {
				finalIp  = domainAray[0]+":"+domainAray[1];
				if(allowedDomainsList.contains(finalIp)){
					return true;
				}
				throw new ApplicationException(finalIp);
			}else {
				return false;
			}
	}
}
