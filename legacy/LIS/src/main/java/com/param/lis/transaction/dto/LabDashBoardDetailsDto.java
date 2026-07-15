package com.param.lis.transaction.dto;

import java.util.Date;

public class LabDashBoardDetailsDto {
  
  private String action;
  
  private String userName;

  private Long actionDateTime;
  
  private String actionDateTimeString;
  
  private Date actionDateTimeActual;
  
  private String actionDateTimeStringNew;

  public String getAction() {
    return action;
  }

  public void setAction(String action) {
    this.action = action;
  }

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public Long getActionDateTime() {
    return actionDateTime;
  }

  public void setActionDateTime(Long actionDateTime) {
    this.actionDateTime = actionDateTime;
  }

public String getActionDateTimeString() {
	return actionDateTimeString;
}

public void setActionDateTimeString(String actionDateTimeString) {
	this.actionDateTimeString = actionDateTimeString;
}

public Date getActionDateTimeActual() {
	return actionDateTimeActual;
}

public void setActionDateTimeActual(Date actionDateTimeActual) {
	this.actionDateTimeString=actionDateTimeActual.toString();
	this.actionDateTimeActual = actionDateTimeActual;
}

public String getActionDateTimeStringNew() {
	return actionDateTimeStringNew;
}

public void setActionDateTimeStringNew(String actionDateTimeStringNew) {
	this.actionDateTimeStringNew = actionDateTimeStringNew;
}
  
  
  
  

}
