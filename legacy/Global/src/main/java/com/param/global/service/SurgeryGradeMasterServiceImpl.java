package com.param.global.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ISurgeryGradeMasterDao;
import com.param.global.dto.SurgeryGradeMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
public class SurgeryGradeMasterServiceImpl  implements ISurgeryGradeMasterService,ICommonConstants {
	final static Logger logger = Logger.getLogger(SurgeryGradeMasterServiceImpl.class);


	
	@Autowired
	ISurgeryGradeMasterDao iSurgeryGradeMasterDao;

	@Transactional
	@Override
	public Response addSurgeryGradeMaster(SurgeryGradeMasterDto surgeryGradeMasterDto) throws ApplicationException {

		try {

			Response deptResponse = iSurgeryGradeMasterDao.getSurgeryGradeByName(surgeryGradeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
					return new Response(ERROR, null, ALREADY_EXIST, null, null);
				
			} else {
				iSurgeryGradeMasterDao.addSurgeryGradeMaster(surgeryGradeMasterDto);
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response getSurgeryGradeMasterList(SurgeryGradeMasterDto surgeryGradeMasterDto) throws ApplicationException {
		try {
			return iSurgeryGradeMasterDao.getSurgeryGradeMasterList(surgeryGradeMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response updateSurgeryGradeMaster(SurgeryGradeMasterDto surgeryGradeMasterDto) throws ApplicationException {
			
		try {
			Response deptResponse = iSurgeryGradeMasterDao.getSurgeryGradeByNameNotId(surgeryGradeMasterDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				/*nationalityMasterDto
						.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss"););*/
				iSurgeryGradeMasterDao.updateSurgeryGradeMaster(surgeryGradeMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		

	}

	@Transactional
	@Override
	public Response getSurgeryGradeById(SurgeryGradeMasterDto surgeryGradeMasterDto) throws ApplicationException {
		
		try {
			return iSurgeryGradeMasterDao.getSurgeryGradeByID(surgeryGradeMasterDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Transactional
	@Override
	public Response updateStatusForSurgeryGrade(SurgeryGradeMasterDto surgeryGradeMasterDto)
			throws ApplicationException {
			
		try {
				iSurgeryGradeMasterDao.updateSurgeryGradeStatus(surgeryGradeMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		

	}

	@Transactional
	@Override
	public Response getActiveSurgeryGradeList(Integer organizationId) throws ApplicationException {
			
		try {
			return iSurgeryGradeMasterDao.getActiveSurgeryGradeMasterList(organizationId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
