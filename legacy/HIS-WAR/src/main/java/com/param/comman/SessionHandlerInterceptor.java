package com.param.comman;

import com.google.gson.Gson;
import com.param.service.global.model.CookieObjectAppConfig;
import com.param.service.global.model.CookieObjectDto;
import com.param.service.global.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.param.global.common.ServiceException;

public class SessionHandlerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(SessionHandlerInterceptor.class);

    @Autowired
    private SessionService sessionService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
        	if(request.getCookies()!=null)
        	{
        		 List<Cookie> cookies = Arrays.stream(request.getCookies())
                         .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
        		 
        		 List<Cookie> appConfig = Arrays.stream(request.getCookies())
                         .filter(cookie -> cookie.getName().equals("cookieObjectAppConfig")).collect(Collectors.toList());
        		 if (cookies != null && cookies.size() > 0) {
                     sessionService.setCookieObjectDto(new Gson().fromJson(java.net.URLDecoder.decode(cookies.get(0).getValue(), StandardCharsets.UTF_8.name()), CookieObjectDto.class));
                 }
        		 if (appConfig != null && appConfig.size() > 0) {
        			 sessionService.setCookieObjectAppConfig(new Gson().fromJson(java.net.URLDecoder.decode(appConfig.get(0).getValue(), StandardCharsets.UTF_8.name()), CookieObjectAppConfig.class));
                 }
        	}

            return true;
        } catch (UnsupportedEncodingException ex) {
            logger.error(ex.getMessage(), ex);
            throw new ServiceException(ex.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}