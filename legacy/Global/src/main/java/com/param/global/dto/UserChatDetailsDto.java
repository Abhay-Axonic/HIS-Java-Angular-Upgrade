package com.param.global.dto;

import java.time.LocalTime;
import java.util.List;

import com.param.opd.common.CommonDateUtils;

public class UserChatDetailsDto {

	
	private int userChatDetailsId;
	private Integer fromUserId;
	private Integer toUserId;
	private String message;
	private String dateTime;
	private Integer fromRoleId;
	private Integer toRoleId;
	private Integer chatStatusId;
	private String fromUserName;
	private String toUserName;
	private Integer startPoint;
	private Integer appointmentIdMyLife;
	private Integer appointmentStatusId;
	private String appointmentStatusTime;
	private String appointmentStatus;
	private Integer applicationId;
	private String uuid;
	private Integer globalUhid;
	private Integer unitId;
	private Integer doctorId;
	private String timeSlot;
	
	public Integer getAppointmentIdMyLife() {
		return appointmentIdMyLife;
	}
	public void setAppointmentIdMyLife(Integer appointmentIdMyLife) {
		this.appointmentIdMyLife = appointmentIdMyLife;
	}
	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}
	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}
	public String getAppointmentStatusTime() {
		return appointmentStatusTime;
	}
	public void setAppointmentStatusTime(String appointmentStatusTime) {
		this.appointmentStatusTime = appointmentStatusTime;
	}
	public String getAppointmentStatus() {
		return appointmentStatus;
	}
	public void setAppointmentStatus(String appointmentStatus) {
		this.appointmentStatus = appointmentStatus;
	}
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public Integer getGlobalUhid() {
		return globalUhid;
	}
	public void setGlobalUhid(Integer globalUhid) {
		this.globalUhid = globalUhid;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}
	public int getUserChatDetailsId() {
		return userChatDetailsId;
	}
	public void setUserChatDetailsId(int userChatDetailsId) {
		this.userChatDetailsId = userChatDetailsId;
	}
	public Integer getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}
	public Integer getToUserId() {
		return toUserId;
	}
	public void setToUserId(Integer toUserId) {
		this.toUserId = toUserId;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getDateTime() {
		return dateTime;
	}
	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	public Integer getFromRoleId() {
		return fromRoleId;
	}
	public void setFromRoleId(Integer fromRoleId) {
		this.fromRoleId = fromRoleId;
	}
	public Integer getToRoleId() {
		return toRoleId;
	}
	public void setToRoleId(Integer toRoleId) {
		this.toRoleId = toRoleId;
	}
	public Integer getChatStatusId() {
		return chatStatusId;
	}
	public void setChatStatusId(Integer chatStatusId) {
		this.chatStatusId = chatStatusId;
	}
	public String getFromUserName() {
		return fromUserName;
	}
	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}
	public String getToUserName() {
		return toUserName;
	}
	public void setToUserName(String toUserName) {
		this.toUserName = toUserName;
	}
	public Integer getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(Integer startPoint) {
		this.startPoint = startPoint;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public synchronized String getTimeSlot() {
		return timeSlot;
	}
	public synchronized void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	
	
	
}
