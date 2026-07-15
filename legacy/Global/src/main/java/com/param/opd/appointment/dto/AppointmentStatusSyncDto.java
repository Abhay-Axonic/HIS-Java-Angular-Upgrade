package com.param.opd.appointment.dto;


public class AppointmentStatusSyncDto {
	private Integer appointmentId;
	private Integer appointmentStatusId;
	private String appointmentStatusTime;
	private String appointmentStatus;
	private Integer applicationId;
	private String countryCode;//this for doctor
	private String uuid;
	private Integer globalUhid;
	private Integer unitId;
	private String hisApiUrl;
	private Integer hisUnitId;
	private Integer doctorId;
	private int userChatDetailsId;
	private String patientName;
	private Integer genderId;
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
	private Integer patientId;
	private Boolean callToGetDocId = false;
	private String appointmentDate;
	private String timeSlot;
	public Integer getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(Integer applicationId) {
		this.applicationId = applicationId;
	}
	public Integer getAppointmentId() {
		return appointmentId;
	}
	public void setAppointmentId(Integer appointmentId) {
		this.appointmentId = appointmentId;
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
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
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
	public String getHisApiUrl() {
		return hisApiUrl;
	}
	public void setHisApiUrl(String hisApiUrl) {
		this.hisApiUrl = hisApiUrl;
	}
	public Integer getHisUnitId() {
		return hisUnitId;
	}
	public void setHisUnitId(Integer hisUnitId) {
		this.hisUnitId = hisUnitId;
	}
	public Integer getDoctorId() {
		return doctorId;
	}
	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}
	public int getUserChatDetailsId() {
		return userChatDetailsId;
	}
	public void setUserChatDetailsId(int userChatDetailsId) {
		this.userChatDetailsId = userChatDetailsId;
	}
	public String getPatientName() {
		return patientName;
	}
	public void setPatientName(String patientName) {
		this.patientName = patientName;
	}
	public Integer getGenderId() {
		return genderId;
	}
	public void setGenderId(Integer genderId) {
		this.genderId = genderId;
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
	public Integer getAppointmentIdMyLife() {
		return appointmentIdMyLife;
	}
	public void setAppointmentIdMyLife(Integer appointmentIdMyLife) {
		this.appointmentIdMyLife = appointmentIdMyLife;
	}
	public synchronized String getAppointmentDate() {
		return appointmentDate;
	}
	public synchronized void setAppointmentDate(String appointmentDate) {
		this.appointmentDate = appointmentDate;
	}
	public synchronized String getTimeSlot() {
		return timeSlot;
	}
	public synchronized void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Boolean getCallToGetDocId() {
		return callToGetDocId;
	}

	public void setCallToGetDocId(Boolean callToGetDocId) {
		this.callToGetDocId = callToGetDocId;
	}
}