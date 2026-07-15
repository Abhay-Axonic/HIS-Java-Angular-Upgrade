package com.param.global.model;

import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.global.common.LocalTimeConverter;

@NamedNativeQueries({

@NamedNativeQuery(name = "GET_CHATS_BY_DETAILS_TWILIO",
query = "select usrChatDtls.user_chat_details_id as \"userChatDetailsId\"  from public.user_chat_details usrChatDtls   "
		 + " where	usrChatDtls.doctor_id = :doctorId "
		 + " and  usrChatDtls.appointment_id_my_life =:appointmentId "
		 + " and  usrChatDtls.unit_id =:unitId "
		 + " and usrChatDtls.uuid =:uuid "),

@NamedNativeQuery(name = "GET_CHATS_BY_CHAT_ID_TWILIO_NOTIFICATION",
query = "select usrChatDtls.user_chat_details_id as \"userChatDetailsId\" ,"
		+ " usrChatDtls.appointment_id_my_life as \"appointmentIdMyLife\" ,"
		 + " 	to_char(usrChatDtls.date_time, 'dd/MM/yyyy') as \"appointmentDate\", " 
		 + " 	usrChatDtls.time_slot as \"timeSlot\", " 
		+ " usrChatDtls.uuid as \"uuid\" ,"
		+ " usrChatDtls.unit_id as \"unitId\" ,"
		+ " usrChatDtls.chat_status_id as \"chatStatusId\", "
		 + " 	(case " 
		 + " 		when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " 
		 + " 		else concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) " 
		 + " 	end ) as \"patientName\", " 
		 + " patient.gender_id as \"genderId\" "
		+ "  from public.user_chat_details usrChatDtls "
		+ " inner join  patient.t_patient_registration patient on"
		+ " patient.uuid =usrChatDtls.uuid "
		 + " where	usrChatDtls.doctor_id = :doctorId "
		 + " and  usrChatDtls.unit_id =:unitId "
		 + " and  usrChatDtls.chat_status_id NOT IN (2)"),

		@NamedNativeQuery(
				name = "GET_CHATS_BY_CHAT_ID_TWILIO",
				query = "SELECT usrChatDtls.user_chat_details_id AS \"userChatDetailsId\" " +
						"FROM public.user_chat_details usrChatDtls " +
						"WHERE usrChatDtls.doctor_id = :doctorId " +
						"AND "
		),

@NamedNativeQuery(name = "UPDATE_CHATS_BY_CHAT_ID_TWILIO",
query = " Update public.user_chat_details   set chat_status_id=2 where user_chat_details_id=:chatId"),

})

@Entity
@Table(name = "user_chat_details", schema = "public")
@SequenceGenerator(name = "user_chat_details_seq", sequenceName = "public.user_chat_details_seq", allocationSize = 1)
public class UserChatDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE , generator = "user_chat_details_seq")
	@Column(name = "user_chat_details_id")
	private int userChatDetailsId;
	
	@Column(name = "appointment_id_my_life")
	private Integer appointmentIdMyLife;
	
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	
	@Column(name = "status")
	private Integer status;
	
	@Column(name = "chat_status_id")
	private Integer chatStatusId;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "doctor_id")
	private Integer doctorId;
	
	@Column(name = "date_time")
	private Date dateTime;
	
	
	@Column(name = "time_slot")
	private String timeSlot;
	

	public int getUserChatDetailsId() {
		return userChatDetailsId;
	}

	public void setUserChatDetailsId(int userChatDetailsId) {
		this.userChatDetailsId = userChatDetailsId;
	}
	

	public Integer getAppointmentIdMyLife() {
		return appointmentIdMyLife;
	}

	public void setAppointmentIdMyLife(Integer appointmentIdMyLife) {
		this.appointmentIdMyLife = appointmentIdMyLife;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getChatStatusId() {
		return chatStatusId;
	}

	public void setChatStatusId(Integer chatStatusId) {
		this.chatStatusId = chatStatusId;
	}

	public synchronized Date getDateTime() {
		return dateTime;
	}

	public synchronized void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public  String getTimeSlot() {
		return timeSlot;
	}

	public  void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	
	

}
