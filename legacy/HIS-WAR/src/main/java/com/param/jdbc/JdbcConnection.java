package com.param.jdbc;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.param.global.common.ServiceException;

public class JdbcConnection {

	private static final Logger logger = LoggerFactory.getLogger(JdbcConnection.class);

	void getJdbcConnectionProps() {
		
		Properties prop = new Properties();
		InputStreamReader input = null;
		try {
			//input = new FileInputStream("F://HIS-GIT-REPO//HIS-WAR//src//main//resources//jdbc.properties");
			Resource resource = new ClassPathResource("jdbc.properties");
		    input = new InputStreamReader(resource.getInputStream());
			prop.load(input);
			// set the properties value
			//System.out.println(prop.getProperty("jdbc.url"));
			//System.out.println(prop.getProperty("jdbc.username"));
			//System.out.println(prop.getProperty("jdbc.password"));


		} catch (IOException io) {
			logger.error(io.getMessage(), io);
			throw new ServiceException(io.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
					throw new ServiceException(e.getMessage());
				}
			}
		}
	  }
	

	
}
