package com.param.global.common;

import java.io.File;

import javax.servlet.http.HttpServletRequest;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class ServletUtils {

	public static String getRelativeUrl(HttpServletRequest request) {

		String baseUrl = null;

		if ((request.getServerPort() == 80) || (request.getServerPort() == 443))
			baseUrl = request.getHeader("X-Forwarded-Proto") + "://" + request.getServerName() + request.getContextPath();
		else
			baseUrl = request.getHeader("X-Forwarded-Proto") + "://" + request.getServerName() + ":" + request.getServerPort()
					+ request.getContextPath();

		StringBuffer buf = request.getRequestURL();

		if (request.getQueryString() != null) {
			buf.append("?");
			buf.append(request.getQueryString());
		}

		return buf.substring(baseUrl.length());
	}

	public static String getBaseUrl(HttpServletRequest request) {

		if (request.getHeader("X-Forwarded-Proto") != null
				&& request.getHeader("X-Forwarded-Proto").equalsIgnoreCase("https")) {
			/*System.out.println(
					"request.getHeader(X-Forwarded-Proto) + \"://\" + request.getServerName() +request.getContextPath()   "
							+ request.getHeader("X-Forwarded-Proto") + "://" + request.getServerName() + request.getContextPath());*/
			return request.getHeader("X-Forwarded-Proto") + "://" + request.getServerName() + request.getContextPath();
		} else {
			if (request.getServerPort() != 0) {
				/*System.out.println(
						"request.getScheme() + \"://\" + request.getServerName() +request.getContextPath()   " + request.getScheme()
								+ "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());*/
				return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
						+ request.getContextPath();
			} else {
				/*System.out.println(
						"request.getScheme() + \"://\" + request.getServerName() +request.getContextPath()   " + request.getScheme()
								+ "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath());*/
				return request.getScheme() + "://" + request.getServerName() + request.getServerPort()
						+ request.getContextPath();

			}
		}
	}

	public static HttpServletRequest getCurrentRequest() {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		Assert.state(requestAttributes != null, "Could not find current request via RequestContextHolder");
		Assert.isInstanceOf(ServletRequestAttributes.class, requestAttributes);
		HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
		Assert.state(servletRequest != null, "Could not find current HttpServletRequest");
		return servletRequest;
	}

}