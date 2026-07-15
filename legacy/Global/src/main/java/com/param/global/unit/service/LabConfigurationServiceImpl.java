package com.param.global.unit.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.lis.global.LabConfigurationMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.ILabConfigurationDao;
import com.param.global.unit.dao.ILabMachineInterfaceDao;
import com.param.global.unit.dao.IMedallRequestResponseDao;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.global.unit.dto.MedallRequestResponseDto;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;


@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class LabConfigurationServiceImpl implements ILabConfigurationService,ICommonConstants{

	private static final Logger logger = Logger.getLogger(LabConfigurationServiceImpl.class);

	@Autowired
	ILabConfigurationDao iLabConfigurationDao;
	
	@Autowired
	IMedallRequestResponseDao iMedallRequestResponseDao; 
	
	@Autowired
	ILabMachineInterfaceDao iLabMachineInterfaceDao; 
	
	@Autowired
	IEmrService EmrService;   

	@Override
	@Transactional
	public Response saveLabConfiguration(LabConfigurationDto dto) throws ApplicationException {
		try {
			Date date = TimeZoneComponent.getDateByzone(dto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			dto.setCreatedDate(dateTime);
			dto.setUpdatedDate(dateTime);
			
			/*Response res = this.checkConfigurationExistOrNot(dto);
			BigInteger count = (BigInteger)res.getObject();
			if(count.intValue()==0)
			{
				iLabConfigurationDao.saveLabConfiguration(dto);
			}
			else{
				return new Response<>(ERROR, ERROR_CODE,ALREADY_EXIST, null, null);
			}*/
			Response res = iLabConfigurationDao.saveLabConfiguration(dto);
			
			if(res.getObject()!=null ) {
				LabConfigurationMaster master  = (LabConfigurationMaster) res.getObject();
				Request request = new Request();
				Payload payload = new Payload();
				if(dto.gettLabConfigId()!=null) {
					payload.setActionType("update");
				}else {
					payload.setActionType("create");
				}
				payload.setData(master);
				request.setKey(MasterKeyConstants.LabConfigurationMaster.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, dto);

		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateLabConfiguration(LabConfigurationDto dto) throws ApplicationException {
		try {
			Date date = TimeZoneComponent.getDateByzone(dto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			dto.setCreatedDate(dateTime);
			dto.setUpdatedDate(dateTime);
		    iLabConfigurationDao.updateLabConfiguration(dto);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, dto);

		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getLabConfigurationById(int tLabConfigId) throws ApplicationException {
		try {
			return iLabConfigurationDao.getLabConfigurationById(tLabConfigId);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateConfigStatus(int tLabConfigId, char status) throws ApplicationException {
		try {
			return iLabConfigurationDao.updateConfigStatus(tLabConfigId,status);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getLabConfigurationList(int orgId, int unitId) throws ApplicationException {
		try {
			return iLabConfigurationDao.getLabConfigurationList(orgId,unitId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response checkConfigurationExistOrNot(LabConfigurationDto dto) throws ApplicationException {
		try {
			return iLabConfigurationDao.checkConfigurationExistOrNot(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getLabConfigurationByOrgUnit(int orgId, int unitId) throws ApplicationException {
		try {
			return iLabConfigurationDao.getLabConfigurationByOrgUnit(orgId,unitId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response checkPinExistOrNot(int orgId, int unitId, String pin,int userId) throws ApplicationException {
		try{
			return iLabConfigurationDao.checkPinExistOrNot(orgId,unitId,pin,userId);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getUserByPin(int orgId, int unitId, String pin) throws ApplicationException {
		try{
			return iLabConfigurationDao.getUserByPin(orgId,unitId,pin);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response saveMedallRequestResponse(MedallRequestResponseDto dto) throws ApplicationException {
		try{
			return iMedallRequestResponseDao.saveMedallRequestResponse(dto);
		}catch(Exception e){
			logger.error("saveMedallRequestResponse failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMedallRequestResponse(MedallRequestResponseDto med) throws ApplicationException {
		try{
			Response res= iMedallRequestResponseDao.getMedallRequestResponse(med);
			if(res.getListObject() != null && res.getListObject().size() > 0) {
				List<MedallRequestResponseDto> list=res.getListObject();
				med.setOrderDetailsListById(list);
				Response res1= iLabMachineInterfaceDao.saveLabMachineInterface(med);
				if(res1.getObject() == null) {
					return new Response<>(ERROR, ERROR_CODE, "Medall id is not configured in HIS.", null, null);
				}else if(res1.getListObject() != null && res1.getListObject().size() > 0){
					List<MedallRequestResponseDto> reqDtoList=res1.getListObject();
					for (MedallRequestResponseDto reqDto : reqDtoList) {
						if(reqDto.getSampleNo() == null) {
							iMedallRequestResponseDao.updateMedallRequestResponse(reqDto);
						}
					}
					
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}else {
					return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
				}
			}
		}catch(Exception e){
			logger.error("getMedallRequestResponse failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}




}
