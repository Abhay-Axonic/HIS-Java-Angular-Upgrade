package com.param.comman;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class OTIMSBean implements ServletContextListener{

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		//System.out.println("started otims..........");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		// TODO Auto-generated method stub
		
	}
}
