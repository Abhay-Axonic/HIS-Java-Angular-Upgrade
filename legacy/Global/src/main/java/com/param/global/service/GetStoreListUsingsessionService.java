package com.param.global.service;

import com.google.gson.Gson;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGetStoreListUsingsessionDao;
import com.param.service.global.SessionService;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service("GetStoreListUsingsessionService")
public class GetStoreListUsingsessionService implements IGetStoreListUsingsessionService, ICommonConstants {
	final static Logger logger = Logger.getLogger(GetStoreListUsingsessionService.class);

    @Autowired
    IGetStoreListUsingsessionDao iGetStoreListUsingsessionDao;

    @Autowired
    SessionService sessionService;

    @Override
    public Response getStoreListUsingSession(Integer userId, Integer unitId) throws ApplicationException {
        try {
            return iGetStoreListUsingsessionDao.getStoreListUsingSessionByUserId(userId, unitId);
        } catch (Exception e) {
        logger.error("getStoreListUsingSession failed", e);
        }
        return new Response(ERROR, null);
    }

    @Override
    public Cookie setStoreIdToSession(HttpServletRequest request, Integer storeId) {
        try {
            this.sessionService.setStoreId(storeId);
            List<Cookie> cookies = Arrays.stream(request.getCookies())
                    .filter(cookie -> cookie.getName().equals("cookieObject")).collect(Collectors.toList());
            Cookie cookie = cookies.getFirst();
            cookie.setValue(java.net.URLEncoder.encode(new Gson().toJson(sessionService.getCookieObjectDto()), StandardCharsets.UTF_8.name()));
            cookie.setMaxAge(60 * 60 * 24 * 30);
            cookie.setPath(request.getContextPath());
            return cookie;
        } catch (UnsupportedEncodingException ex) {
        logger.error("setStoreIdToSession failed", ex);
        } catch (Exception ex) {
            throw ex;
        }

        return null;
    }
}
