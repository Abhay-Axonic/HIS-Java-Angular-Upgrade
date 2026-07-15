package com.param.global.dto;

public class MyLifeConnectionLinkDto {

	private String myLifeLink;
	
	private String globalSchedulingLink;
	private String pacsHost;
	private String pacsPort;
	private Integer scheduleLogsId;
	
	private Character status;

	public String getMyLifeLink() {
		return myLifeLink;
	}

	public void setMyLifeLink(String myLifeLink) {
		this.myLifeLink = myLifeLink;
	}

	public String getGlobalSchedulingLink() {
		return globalSchedulingLink;
	}

	public void setGlobalSchedulingLink(String globalSchedulingLink) {
		this.globalSchedulingLink = globalSchedulingLink;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public String getPacsHost() {
		return pacsHost;
	}

	public void setPacsHost(String pacsHost) {
		this.pacsHost = pacsHost;
	}

	public String getPacsPort() {
		return pacsPort;
	}

	public void setPacsPort(String pacsPort) {
		this.pacsPort = pacsPort;
	}

	public Integer getScheduleLogsId() {
		return scheduleLogsId;
	}

	public void setScheduleLogsId(Integer scheduleLogsId) {
		this.scheduleLogsId = scheduleLogsId;
	}
	
	
}
