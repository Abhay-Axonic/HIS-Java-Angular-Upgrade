package com.param.global.common;

import org.springframework.context.ApplicationEvent;

public class CustomSynchEvent extends ApplicationEvent {

	private static final long serialVersionUID = -3448633405178813251L;
	
	private String message;
	
	public CustomSynchEvent(Object source, String message) {
		super(source);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	
}
