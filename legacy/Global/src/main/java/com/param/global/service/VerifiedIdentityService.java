package com.param.global.service;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * docs/DECISION_NEEDED_OrganizationIdNotCryptographicallyVerified.md --
 * `organizationId` is never part of the JWT this app issues (only `unitId`
 * is signed and verified, see `JwtTokenUtil`/`JwtTokenFilter`), so every
 * existing "trusted" organizationId source -- `SessionService.getOrganizationId()`
 * included -- actually comes from an unsigned, client-editable `cookieObject`
 * cookie (`SessionHandlerInterceptor`), not from anything the server has
 * verified.
 *
 * This service derives organizationId the only safe way available without
 * re-issuing tokens: from the `unitId` HTTP header, which `JwtTokenFilter`
 * has ALREADY cryptographically validated against the signed JWT before any
 * request reaches this far -- re-reading it here is safe precisely because
 * nothing between that filter and this call can have altered it (a plain,
 * per-request HTTP header, not a mutable shared field). `m_unit_master.unit_id`
 * is that table's primary key with a single `organization_id` FK column, so
 * this is an unambiguous 1:1 derivation, not a heuristic.
 *
 * Deliberately stateless -- no mutable fields, nothing cached across calls --
 * so this carries none of `SessionService`'s own risk (that bean is a plain
 * singleton with a mutable field repopulated per-request by
 * `SessionHandlerInterceptor`, which is a real, separate, pre-existing
 * concurrency concern under load, not something this class perpetuates).
 * `getVerifiedOrganizationId()` is `@Transactional` on its own -- there is
 * no `OpenSessionInView` configured in this app, and being invoked from a
 * Controller method is NOT enough on its own to have a Hibernate session
 * bound (confirmed live: without this annotation, the very first test call
 * threw "No Session found for current thread", the same class of bug
 * already found and fixed as BR-108 earlier this session). Callers don't
 * need to be transactional themselves for this reason, though this must
 * still never be called from a raw Filter/HandlerInterceptor `preHandle()`
 * (before Spring's request-processing/transaction machinery is available at
 * all).
 */
@Service("VerifiedIdentityService")
public class VerifiedIdentityService {

	private static final Logger logger = Logger.getLogger(VerifiedIdentityService.class);

	@Autowired
	private SessionFactory sessionFactory;

	/**
	 * Returns the real organizationId for the current request's
	 * JWT-verified unitId. Throws {@link IllegalStateException} if called
	 * outside a real HTTP request, if the request has no `unitId` header
	 * (i.e. it never passed through {@code JwtTokenFilter}'s authenticated
	 * path), or if that unitId doesn't resolve to a real, active unit --
	 * fails loudly rather than silently returning null, since a caller
	 * treating a null organizationId as "no filter" would reopen the exact
	 * IDOR this class exists to close.
	 */
	@Transactional
	public Integer getVerifiedOrganizationId() {
		Integer unitId = getVerifiedUnitId();

		Integer organizationId = (Integer) sessionFactory.getCurrentSession()
				.getNamedQuery("GET_ORGANIZATION_ID_BY_UNIT_ID")
				.setParameter("unitId", unitId)
				.uniqueResult();

		if (organizationId == null) {
			logger.error("VerifiedIdentityService: unitId " + unitId
					+ " (from a JWT-validated request) has no matching organization -- refusing to proceed");
			throw new IllegalStateException("Unable to resolve organization for the authenticated unit.");
		}

		return organizationId;
	}

	/**
	 * The `unitId` HTTP header, as already validated by {@code JwtTokenFilter}
	 * against the signed JWT for this request. Not a new trust decision --
	 * this class's entire premise is that this specific header, at this
	 * specific point in the request lifecycle, is the one value in this
	 * application that has actually been cryptographically checked.
	 */
	private Integer getVerifiedUnitId() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if (attrs == null) {
			throw new IllegalStateException(
					"VerifiedIdentityService.getVerifiedOrganizationId() called outside an HTTP request context.");
		}

		HttpServletRequest request = attrs.getRequest();
		String unitIdHeader = request.getHeader("unitId");
		if (unitIdHeader == null || unitIdHeader.trim().isEmpty()) {
			throw new IllegalStateException(
					"No verified unitId header on this request -- this endpoint may not be behind JwtTokenFilter's authenticated path.");
		}

		return Integer.valueOf(unitIdHeader.trim());
	}
}
