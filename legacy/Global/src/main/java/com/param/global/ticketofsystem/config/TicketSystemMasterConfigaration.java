package com.param.global.ticketofsystem.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TicketSystemMasterConfigaration{

	@Bean
	  public ModelMapper modelMapper() {
	    return new ModelMapper();
	  }
	
	 
}
