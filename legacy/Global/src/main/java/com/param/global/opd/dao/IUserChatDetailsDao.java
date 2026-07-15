package com.param.global.opd.dao;

import com.param.global.common.Response;
import com.param.global.dto.UserChatDetailsDto;
import com.param.opd.appointment.dto.AppointmentStatusSyncDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IUserChatDetailsDao {

	public Response saveUserChatDetails(UserChatDetailsDto userChatDetailsDto)throws ApplicationException;

	public Response getUserChatDetails(UserChatDetailsDto userChatDetailsDto)throws ApplicationException;

	public Response updateUserChatDetails(UserChatDetailsDto updatedto)throws ApplicationException;

	public Response getVideoataForNotification(AppointmentStatusSyncDto dto)throws ApplicationException;

	public Response changeStatusofuserChatDetailsId(AppointmentStatusSyncDto dto)throws ApplicationException;

	public Response getChatsByToUserIdChatIdTwilio(UserChatDetailsDto chatStatusLogDto)throws ApplicationException;

}
