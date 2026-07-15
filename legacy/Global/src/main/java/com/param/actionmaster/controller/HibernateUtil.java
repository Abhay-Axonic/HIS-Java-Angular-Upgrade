package com.param.actionmaster.controller;

import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import com.param.global.common.ServiceException;

public class HibernateUtil{

	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(HibernateUtil.class);

	private static SessionFactory mySqlSessionFactory;
	
	
	public static SessionFactory getMySqlSessionFactory() {
		try {
			
		 	if(mySqlSessionFactory==null)
		 	{
		 		Configuration configuration = new Configuration();
		 		Properties properties = new Properties();
		 		properties.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
		 		properties.put(Environment.URL, "jdbc:mysql://41.215.3.102:8013/mpshah");
		 		properties.put(Environment.USER, "shiraz");
		 		properties.put(Environment.PASS, "mpshah");
		 		properties.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");
		 		properties.put(Environment.SHOW_SQL, "true");
		 		configuration.setProperties(properties);
		 		ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
		 		mySqlSessionFactory = configuration.buildSessionFactory(serviceRegistry);
		 	}
		
		}catch(Exception e)

		{
			logger.error("Error while building MySQL session factory", e);
		}
		return mySqlSessionFactory;
	}
	
}