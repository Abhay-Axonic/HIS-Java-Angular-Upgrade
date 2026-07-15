package com.param.global.mail;

import java.io.ByteArrayOutputStream;
import java.util.List;

import com.param.data.dto.MailReqDto;
import com.param.global.common.RegistrationType;
import com.param.global.common.Response;
import com.param.global.dto.MailDto;
import com.param.global.dto.MessageQueueDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.org.common.dto.PatientLoyaltyCardMapperDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.PreOperativeDetailsDto;
import com.param.global.otims.dto.ProcedureOrderPatientEducationDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.global.otims.dto.ProcedureStaffScheduleDto;
import com.param.global.unit.dto.BulkEmailDto;
import com.sendgrid.Attachments;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IMailingService {
	public Response forgotPassword(UserMasterDto userMasterDto)throws ApplicationException;
	public Response sendEmailToUserWithNewPassword(UserMasterDto userMasterDto)throws ApplicationException;
	public Response sendUserName(UserMasterDto userMasterDto,RegistrationType registration)throws ApplicationException;
	public Response sendUserPassword(UserMasterDto userMasterDto,RegistrationType registration)throws ApplicationException;
	public Response resendUserName(String emailID, String userName) throws ApplicationException;
	public Response resendUserNameAndPassword(String emailID, String userName, String password) throws ApplicationException;
	public Response sendOTP(PatientLoyaltyCardMapperDto dto) throws ApplicationException;
	public Response sentPatinetEducationTemplate(ProcedureOrderPatientEducationDto procedureOrderPatientEducationDto)throws ApplicationException;
	public Response consumeLoyaltyAmount(PatientLoyaltyCardMapperDto dto) throws ApplicationException;
	public Response addLoyaltyAmount(PatientLoyaltyCardMapperDto patientLoyaltyCardMapperDto) throws ApplicationException;
	public Response sendSchedulingNotification(ProcedureStaffScheduleDto procedureStaffScheduleDto, ProcedureSchedulingDto procedureSchedulingDto)throws ApplicationException;
	public Response sendSchedulingConcelledNotification(ProcedureStaffScheduleDto procedureStaffScheduleDto,
			OTReservationDto dto)throws ApplicationException;
	public Response sentRegistrationMailToPatient(PatientRegistrationDto dto) throws ApplicationException;
	public Response sendMailInvite(String url, String mails, String date, String time, String remark) throws ApplicationException;
	public Response sendNotificationToDoctor(PreOperativeDetailsDto preOperativeDetailsDto)throws ApplicationException;
	public Response triggerEmail(String message, String toEmailId , String mailSubject ,String fileName,ByteArrayOutputStream report, MailDto mailDto)throws ApplicationException;
	public Response sendOutSourceMail(String patientEmail, String mailData) throws ApplicationException;
	
	
	/**@author Ganesh Chaudhari*/
	public Response sendTemplateMailtoPatient(MailReqDto dto) throws ApplicationException;
	public Response sendFeedBackTemplateMailtoPatient(MailReqDto dto) throws ApplicationException;
	public Response sendTemplateFormToDoctor(MailReqDto mailDto)throws ApplicationException;
	public Response triggerBulkEmail(String message, String toEmailId, String mailSubject,
			List<BulkEmailDto> bulkmailList, MailDto mailDto)throws ApplicationException;
	
	public Response sendAutoMail(MessageQueueDto dto, List<Attachments> attachments, String mailProvider)throws ApplicationException;
	public Response sendOTPMail(MessageQueueDto dto, String mailCredential, String string)throws ApplicationException;
	public Response sendMailForpostRoomRentAutoJobFail(String key, List<Integer> value)throws ApplicationException, Exception;
}
