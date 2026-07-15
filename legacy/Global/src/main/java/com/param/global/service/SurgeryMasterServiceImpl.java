package com.param.global.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ISurgeryMasterDao;
import com.param.global.dto.SurgeryMasterDto;
import com.param.global.model.SurgeryMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SurgeryMasterServiceImpl implements ISurgeryMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(SurgeryMasterServiceImpl.class);


	@Autowired
	private ISurgeryMasterDao iSurgeryMasterDao;
	
	@Autowired
	IEmrService EmrService;   

	@Override
	@Transactional
	public Response getListOfSurgeryMaster(SurgeryMasterDto surgeryMasterDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return iSurgeryMasterDao.getListOfSurgeryMaster(surgeryMasterDto);
	}

	@Override
	@Transactional
	public Response getSurgeryMasterList(SurgeryMasterDto surgeryMasterDto) throws ApplicationException {
		try {
			return iSurgeryMasterDao.getSurgeryMasterList(surgeryMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		

	}

	@Override
	@Transactional
	public Response saveSurgeryMaster(SurgeryMasterDto surgeryMasterDto) throws ApplicationException {

		try {
			Response deptResponse = iSurgeryMasterDao.getSurgeryMasterByName(surgeryMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				Response res =  iSurgeryMasterDao.saveSurgeryMaster(surgeryMasterDto);
				
				if(res.getObject() != null) {
					SurgeryMaster dto = new SurgeryMaster();
					dto = (SurgeryMaster) res.getObject();
					surgeryMasterDto.setSurgery_id(dto.getSurgery_id());
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(surgeryMasterDto);
					request.setKey(MasterKeyConstants.SurgeryMaster.toString());
					request.setPayload(payload);
					//EmrService.publish(request);
				}
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getSurgeryMasterById(SurgeryMasterDto surgeryMasterDto) throws ApplicationException {
		try {
			return iSurgeryMasterDao.getSurgeryMasterById(surgeryMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateSurgeryMaster(SurgeryMasterDto surgeryMasterDto) throws ApplicationException {
		try {
			Response res =  iSurgeryMasterDao.updateSurgeryMaster(surgeryMasterDto);
			if(res.getObject() != null) {
				
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(surgeryMasterDto);
				request.setKey(MasterKeyConstants.SurgeryMaster.toString());
				request.setPayload(payload);
				//EmrService.publish(request);
			}
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateStatusForSurgeryMaster(SurgeryMasterDto surgeryMasterDto) throws ApplicationException {
		try {
			return iSurgeryMasterDao.updateStatusForSurgeryMaster(surgeryMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
