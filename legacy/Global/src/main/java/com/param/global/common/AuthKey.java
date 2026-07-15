package com.param.global.common;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
public class AuthKey {
	final static Logger logger = Logger.getLogger(AuthKey.class);

	public static AuthTokenDto getAuthProperties()
	{
		Properties prop = new Properties();
		InputStreamReader input = null;
		try {
			Resource resource = new ClassPathResource("jdbc.properties");
		    input = new InputStreamReader(resource.getInputStream());
			prop.load(input);
			AuthTokenDto dto  = new AuthTokenDto();
			dto.setUserName(prop.getProperty("client.username"));
			dto.setPassword(prop.getProperty("client.password"));
			dto.setSecretKey(prop.getProperty("client.secretkey"));
			dto.setSecretPassword(prop.getProperty("client.secretpassword"));
			return dto;

		} catch (IOException io) {
		logger.error("getAuthProperties failed", io);
			return null;
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				logger.error("getAuthProperties failed", e);
				}
			}
		}
	}
}
