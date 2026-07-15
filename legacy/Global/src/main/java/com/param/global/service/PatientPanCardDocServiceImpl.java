package com.param.global.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IPatientPanCardDocDao;
import com.param.global.dto.PatientPanCardDocDto;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class PatientPanCardDocServiceImpl implements IPatientPanCardDocService,ICommonConstants{
	final static Logger logger = Logger.getLogger(PatientPanCardDocServiceImpl.class);


	@Autowired
	IPatientPanCardDocDao iPatientPanCardDocDao;

	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired
	DocUploadUtility docUploadUtility;
	
	@Override
	public Response savePanCardDocument(PatientPanCardDocDto dto) throws ApplicationException {
		try {
			Date date = TimeZoneComponent.getDateByzone(dto.getUnitId() != null ? dto.getUnitId() : 0);
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			
				if(dto.getFile().getSize()>0)
			      {
					com.param.global.common.Response orgRes =  iGlobalOrganisationMasterDao.getOrganiazationFolderPath(dto.getOrganizationId());
				    OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				 	 String docPath="";
				   
				    	 docPath =  docUploadUtility.UoploadPacCardForFile(dto.getFile(),organizationMasterDto.getOrganizationFolder(),dto.getOrganizationId(),dto.getBillId(),dto.getIsPharmacy(),dto.getUnitId());
			       
					dto.setDocUrl(docPath);
			        dto.setCreatedDate(dateTime);
			        dto.setUpdatedDate(dateTime);
			        iPatientPanCardDocDao.savePanCardDocument(dto);
			      }
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		logger.error("savePanCardDocument failed", e);
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPanCardDetailsById(int billId, int screenId) throws ApplicationException {
		try {
			
			return iPatientPanCardDocDao.getPanCardDetailsById(billId,screenId);
		} catch (Exception e) {
		logger.error("getPanCardDetailsById failed", e);
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response inwardDataupdate(Double consumenetamount, Integer mateinwardid,Integer id) throws ApplicationException {
		try {
			return iPatientPanCardDocDao.inwardDataupdate(consumenetamount,mateinwardid,id);
		} catch (Exception e) {
		logger.error("inwardDataupdate failed", e);
		}
		return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
}
