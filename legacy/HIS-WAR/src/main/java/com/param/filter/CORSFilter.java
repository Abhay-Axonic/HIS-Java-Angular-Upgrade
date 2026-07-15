package com.param.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CORSFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Calcutta"));
	}
	
	  private static final List<String> ALLOWED_ORIGINS = Arrays.asList(
		        "http://localhost:8081",
		        "https://ociqa.his.axonichealth.com",
		        "http://localhost:8090",
		        "http://localhost:8080"
		    );
	
	

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletResponse response = (HttpServletResponse) servletResponse;
		HttpServletRequest request = (HttpServletRequest) servletRequest;

		
		  String origin = request.getHeader("Origin");

//	        if (ALLOWED_ORIGINS.contains(origin)) {
	            response.setHeader("Access-Control-Allow-Origin", origin);
	            response.setHeader("Access-Control-Allow-Credentials", "true");
//	        }
		/*response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
		response.setHeader("Access-Control-Allow-Credentials", "true");*/
		response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers", "Origin, Accept,x-access-token,unit_id, organization_id, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers, Authorization");


		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
	}
}
