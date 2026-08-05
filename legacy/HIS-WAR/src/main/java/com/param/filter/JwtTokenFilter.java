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
	 * The approved 83-entry exact-match allowlist (QF-AJ owner review 2026-08-05).
	 * Ordered as the review list was: writes first, then PHI reads, reference/config, then static.
	 * The stored forms are POST-normalization (host-stripped, query-stripped, trailing-slash-trimmed).
	 */
	private static final Set<String> PUBLIC_ALLOWLIST = new LinkedHashSet<>(List.of(
		// WRITES
		"/api/abdm/saveDetailsOfAbdm",
		"/api/abdm/v3/updateStatus",
		"/api/billing/kiosk/saveAndPay/future",
		"/api/inPatientFeedback",
		"/api/inPatientFeedback/:organizationId/:unitId/:patientId/:visitType/:visitTypeId/:admissionId",
		"/api/inPatientFeedback/saveInPatientFeedback",
		"/api/ipd/saveMyLatestMedicalSummaryCallBack",
		"/api/mylife/sync/appointment/cancelorRescheduleAppointmentFromApp",
		"/api/mylife/sync/savePatientThroughMylife",
		"/api/opd/saveAIAppointmentInQueue",
		"/api/outPatientFeedback",
		"/api/outPatientFeedback/:organizationId/:unitId/:patientId/:visitType/:visitTypeId/:encounterId",
		"/api/outPatientFeedback/saveOutPatientFeedback",
		"/api/saveTwilioMsg",
		"/checkEmailAvailability",
		"/updatePatientRegistrationFromMyLife",
		"/api/billing/kiosk/saveAndPay",
		"/api/global/patientRegistration",
		"/api/opd/appointment/check",
		"/api/abdm/request/otp",
		"/forgotPasswordGeneration",
		"/otpVerification",
		// PHI READS
		"/api/global/patientTypeHeadSearchForAppointmentUniqueForNurseCheckIn",
		"/api/abdm/dataPush",
		"/api/abdm/getHiuDecryptedData",
		"/api/opd/getAIAppointmentCount",
		"/api/opd/patientDoc/fromMylife",
		"/api/webappointment/getDetailsForBookAppointment",
		"/api/global/patientTypeHeadSearchForAppointmentUnique",
		"/api/billing/mylife/sync/appointment",
		"/api/webappointment/getActivePrefixList",
		"/getBillNoByPatient",
		"/getBillReceiptBytesMyLife",
		// REFERENCE / CONFIG
		"/api/createOrganizationFromWeb",
		"/ldapUserCredentialCheck",
		"/api/global/getVideoConsultationLinkFromHIS",
		"/HIS-WAR/configuration/security",
		"/HIS-WAR/configuration/ui",
		"/adt/getActivePrefixListForWebPage",
		"/HIS-WAR/v2/api-docs",
		"/LIS/global/getTotalLanguageMasterRecordActive",
		"/adt/br",
		"/adt/getActiveRelationListForWebPage",
		"/adt/getListOfDocumentForBookView",
		"/adt/getUnitByCredentials",
		"/adt/organizationMasterList",
		"/adt/userLogin",
		"/api/doctorApp/getApplicationConfig",
		"/api/getVideoataForNotification",
		"/api/global/appoitments/doctors",
		"/api/opd/coversheetInstructionListForBookView",
		"/api/opd/getInvestigationsListForBookView",
		"/api/opd/getMedicationHistoryListForBookView",
		"/getOrderIdUnitSubscriptionPay",
		"/resultUrl",
		"/token/Doctor",
		"/validationResultResponse",
		// STATIC
		"/HIS-WAR/api-ui",
		"/HIS-WAR/images/favicon-16x16.png",
		"/HIS-WAR/images/favicon-32x32.png",
		"/HIS-WAR/swagger-resources",
		"/HIS-WAR/swagger-ui.html",
		"/HIS-WAR/webjars/springfox-swagger-ui/css/print.css",
		"/HIS-WAR/webjars/springfox-swagger-ui/css/reset.css",
		"/HIS-WAR/webjars/springfox-swagger-ui/css/screen.css",
		"/HIS-WAR/webjars/springfox-swagger-ui/css/typography.css",
		"/HIS-WAR/webjars/springfox-swagger-ui/fonts/DroidSans-Bold.ttf",
		"/HIS-WAR/webjars/springfox-swagger-ui/fonts/DroidSans.ttf",
		"/HIS-WAR/webjars/springfox-swagger-ui/images/logo_small.png",
		"/HIS-WAR/webjars/springfox-swagger-ui/lib/backbone-min.js",
		"/HIS-WAR/webjars/springfox-swagger-ui/lib/handlebars-2.0.0.js",
		"/HIS-WAR/webjars/springfox-swagger-ui/lib/highlight.7.3.pack.js",
		"/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery-1.8.0.min.js",
		"/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery.ba-bbq.min.js",
		"/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery.slideto.min.js",
		"/HIS-WAR/webjars/springfox-swagger-ui/lib/jquery.wiggle.min.js",
		"/HIS-WAR/webjars/springfox-swagger-ui/lib/jsoneditor.min.js",
		"/HIS-WAR/webjars/springfox-swagger-ui/lib/marked.js",
		"/HIS-WAR/webjars/springfox-swagger-ui/lib/swagger-oauth.js",
		"/HIS-WAR/webjars/springfox-swagger-ui/lib/underscore-min.js",
		"/HIS-WAR/webjars/springfox-swagger-ui/springfox.js",
		"/HIS-WAR/webjars/springfox-swagger-ui/swagger-ui.min.js",
		"/applicationConfiguration/screen-images"
	));

	/**
	 * The two narrow anchored patterns for the feedback path-variable GETs — ONLY these two,
	 * no general pattern facility. The exact 6-variable shapes from QF-AH/AI:
	 *   /api/outPatientFeedback/{organizationId}/{unitId}/{patientId}/{visitType}/{visitTypeId}/{encounterId}
	 *   /api/inPatientFeedback/{organizationId}/{unitId}/{patientId}/{visitType}/{visitTypeId}/{admissionId}
	 */
	private static final Pattern OPD_FEEDBACK_PATH = Pattern
			.compile("^/api/outPatientFeedback/[^/]+/[^/]+/[^/]+/[^/]+/[^/]+/[^/]+$");
	private static final Pattern IPD_FEEDBACK_PATH = Pattern
			.compile("^/api/inPatientFeedback/[^/]+/[^/]+/[^/]+/[^/]+/[^/]+/[^/]+$");

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

			// BR-173 PHASE 2 — default-deny: the allowlist is the ONLY public-by-design path
			String normalizedUrl = normalizeUrl(requestUrl);
			boolean isPublic = PUBLIC_ALLOWLIST.contains(normalizedUrl)
					|| OPD_FEEDBACK_PATH.matcher(normalizedUrl).matches()
					|| IPD_FEEDBACK_PATH.matcher(normalizedUrl).matches();

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
