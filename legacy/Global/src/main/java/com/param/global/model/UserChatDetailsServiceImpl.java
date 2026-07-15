package com.param.global.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.controller.IUserChatDetailsService;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.UserChatDetailsDto;
import com.param.global.opd.dao.IAppointmentMasterDao;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.opd.dao.IUserChatDetailsDao;
import com.param.opd.appointment.dto.AppointmentMasterDto;
import com.param.opd.appointment.dto.AppointmentStatusSyncDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.encounter.dto.EncounterMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class UserChatDetailsServiceImpl implements IUserChatDetailsService, ICommonConstants{
	final static Logger logger = Logger.getLogger(UserChatDetailsServiceImpl.class);

	
	@Autowired
	private IUserChatDetailsDao iUserChatDetailsDao;
	
	@Autowired
	private IAppointmentMasterDao iAppointmentMasterDao;
	
	@Autowired
	private IEncounterMasterDao iEncounterMasterDao;
	
	@Override
	@Transactional
	public Response saveTwilioMsg(AppointmentStatusSyncDto dto) throws ApplicationException {
		try{
			
			//get doctoid for the toget appointment details
			dto.setCallToGetDocId(true);
			Response response = iEncounterMasterDao.getOpPatientListByDocIdForVideo(dto);
			SlotMasterDto dtoEnc = (SlotMasterDto) response.getResObject();
			
			UserChatDetailsDto	userChatDetailsDto = new UserChatDetailsDto();
			userChatDetailsDto.setAppointmentIdMyLife(dto.getAppointmentId());
			userChatDetailsDto.setUuid(dto.getUuid());
			userChatDetailsDto.setGlobalUhid(dto.getGlobalUhid());
			userChatDetailsDto.setUnitId(dto.getHisUnitId());
			userChatDetailsDto.setDateTime(dto.getAppointmentDate());
			userChatDetailsDto.setTimeSlot(dto.getTimeSlot());
			if(dtoEnc != null ){
				userChatDetailsDto.setDoctorId(dtoEnc.getDoctorId());
			}else{
				userChatDetailsDto.setDoctorId(dto.getDoctorId());
			}
			
			 Response exitRes= iUserChatDetailsDao.getUserChatDetails(userChatDetailsDto);
			if(exitRes!=null && exitRes.getListObject()!=null && exitRes.getListObject().size()>0){
				// Assuming exitRes.getListObject() returns List<UserChatDetails>
				List<UserChatDetails> userChatDetailsList = exitRes.getListObject();
				List<UserChatDetailsDto> userChatDtoList = new ArrayList<>();

				for (UserChatDetails userChatDetails : userChatDetailsList) {
				    UserChatDetailsDto dtos = new UserChatDetailsDto();
				    dtos.setUserChatDetailsId(userChatDetails.getUserChatDetailsId());
				    // Set other properties if needed
				    userChatDtoList.add(dtos);
				}

				// Now use the userChatDtoList which contains UserChatDetailsDto objects
				UserChatDetailsDto updatedto = new UserChatDetailsDto();
				for (UserChatDetailsDto userChatDetailsDto2 : userChatDtoList) {
				    updatedto.setUserChatDetailsId(userChatDetailsDto2.getUserChatDetailsId());
				    Response chatRes = iUserChatDetailsDao.updateUserChatDetails(updatedto);
				}

			}else{
				userChatDetailsDto.setChatStatusId(1);
				Response   chatRes = iUserChatDetailsDao.saveUserChatDetails(userChatDetailsDto);
			}
			
			
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response<>(ERROR, null, null, null,null);
	}

	@Override
	@Transactional
	public Response getVideoataForNotification(AppointmentStatusSyncDto dto) {
		try {
			
			return iUserChatDetailsDao.getVideoataForNotification(dto);
	
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
}

	@Override
	@Transactional
	public Response changeStatusofuserChatDetailsId(AppointmentStatusSyncDto dto) {
		try {
			
			return iUserChatDetailsDao.changeStatusofuserChatDetailsId(dto);
	
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
}

	@Override
	@Transactional
	public Response getChatsByToUserIdChatIdTwilio(UserChatDetailsDto chatStatusLogDto) throws ApplicationException {
		return iUserChatDetailsDao.getChatsByToUserIdChatIdTwilio(chatStatusLogDto);
	}

}
