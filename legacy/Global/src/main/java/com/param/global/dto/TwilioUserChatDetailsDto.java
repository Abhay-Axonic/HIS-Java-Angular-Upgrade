package com.param.global.dto;
import java.time.LocalTime;
public class TwilioUserChatDetailsDto {
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
	private String patientName;
	private String patientPhoto;
	private Integer genderId;
	public String timeSlot;
	private Integer patientId;
	private Integer appointmentId;
	private Integer appointmentTypeId;
	private Integer appointmentStatusId;
	private char status;
	public Integer getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(Integer startPoint) {
		this.startPoint = startPoint;
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
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public String getTimeSlot() {
		return timeSlot;
	}
	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}
	public String getPatientPhoto() {
		return patientPhoto;
	}
	public void setPatientPhoto(String patientPhoto) {
		this.patientPhoto = patientPhoto;
	}
	public Integer getGenderId() {
		return genderId;
	}
	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
	}
	public Integer getPatientId() {
		return patientId;
	}
	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
	}
	public Integer getAppointmentTypeId() {
		return appointmentTypeId;
	}
	public void setAppointmentTypeId(Integer appointmentTypeId) {
		this.appointmentTypeId = appointmentTypeId;
	}
	public Integer getAppointmentStatusId() {
		return appointmentStatusId;
	}
	public void setAppointmentStatusId(Integer appointmentStatusId) {
		this.appointmentStatusId = appointmentStatusId;
	}
	public char getStatus() {
		return status;
	}
	public void setStatus(char status) {
		this.status = status;
	}
	 
}
