package com.param.global.controller;

import com.param.global.common.Response;
import com.param.global.dto.UserChatDetailsDto;
import com.param.opd.appointment.dto.AppointmentMasterDto;
import com.param.opd.appointment.dto.AppointmentStatusSyncDto;

import in.param.exception.ApplicationException;

public interface IUserChatDetailsService {

	public Response saveTwilioMsg(AppointmentStatusSyncDto dto)throws ApplicationException;

	public Response getVideoataForNotification(AppointmentStatusSyncDto dto);

	public Response changeStatusofuserChatDetailsId(AppointmentStatusSyncDto dto);

	public Response getChatsByToUserIdChatIdTwilio(UserChatDetailsDto input)throws ApplicationException;


}
