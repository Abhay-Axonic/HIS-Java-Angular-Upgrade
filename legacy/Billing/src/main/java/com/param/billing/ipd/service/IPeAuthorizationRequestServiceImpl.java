package com.param.billing.ipd.service;

import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.param.billing.global.transaction.dto.MPreauthRequestMasterDto;
import com.param.billing.global.transaction.model.MPreauthRequestMaster;
import com.param.billing.ipd.dao.IPreAuthorizationRequest;
import com.param.billing.ipd.dto.PreAuthorizationRequestDto;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.opd.encounter.dto.PatientDocReqDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class IPeAuthorizationRequestServiceImpl implements IPreAuthorizationRequestService, ICommonConstants{

	@Autowired
	private IPreAuthorizationRequest iPreAuthorizationRequestDao;

	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired
    DocUploadUtility docUploadUtility;

	@Override
	@Transactional
	public Response getPreAuthorizationRequestList(PreAuthorizationRequestDto preAuthorizationRequestDto)
			throws BillingException {
		try{
			return iPreAuthorizationRequestDao.getPreAuthorizationRequestList(preAuthorizationRequestDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}

//getPreAuthorizationRequestListCount
	
	@Override
	@Transactional
	public Response getPreAuthorizationRequestListCount(PreAuthorizationRequestDto preAuthorizationRequestDto)
			throws BillingException {
		try{
			return iPreAuthorizationRequestDao.getPreAuthorizationRequestListCount(preAuthorizationRequestDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}
	
	@Override
	@Transactional
	public Response saveNewPreAuthorizationRequest(MPreauthRequestMasterDto reqDto) throws BillingException {
		try{
			return iPreAuthorizationRequestDao.saveNewPreAuthorizationRequest(reqDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}


	@Override
	@Transactional
	public Response updateAuthorizationRequest(MPreauthRequestMasterDto reqDto) throws BillingException {
		try{
			return iPreAuthorizationRequestDao.updateAuthorizationRequest(reqDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}


	@Override
	@Transactional
	public Response getPreAuthorizationRequestListByPayeeId(MPreauthRequestMasterDto reqDto) throws BillingException {
		try{
			return iPreAuthorizationRequestDao.getPreAuthorizationRequestListByPayeeId(reqDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}


	@Override
	@Transactional
	public Response getPreAuthorizationRequestListByPayeeIdIPDCharges(MPreauthRequestMasterDto reqDto)
			throws BillingException {
		try{
			return iPreAuthorizationRequestDao.getPreAuthorizationRequestListByPayeeIdIPDCharges(reqDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	
	@Override
	@Transactional
	public Response cancelNewPreAuthorizationRequestByPreAuthorizationId(MPreauthRequestMasterDto reqDto)
			throws BillingException {
		try{
			return iPreAuthorizationRequestDao.cancelNewPreAuthorizationRequestByPreAuthorizationId(reqDto);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}


	@Override
	@Transactional
	public Response saveInsuranceAuthorizationDoc(int organizationId, MultipartFile inputFile,
			MPreauthRequestMaster reqDto) throws ApplicationException {
		try{
		      if(organizationId>0&&!inputFile.isEmpty())
		      {
		        Response organizationRes =  iGlobalOrganisationMasterDao.getOrganiazationFolderPath(organizationId);
		        OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) organizationRes.getObject();
		       // String insuranceAuthorizationDocPath =  docUploadUtility.uploadInsuranceAuthorizationsDoc(inputFile, organizationMasterDto, reqDto);
		    	return null;
		      }
		    return null;
		  }catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
		  
		  }


	@Override
	@Transactional
	public Response updateIsSplitStatusByPreAuthorizationId(MPreauthRequestMasterDto reqDto) throws BillingException {
		try{
			for (Iterator iterator = reqDto.getPreAuthReqIds().iterator(); iterator.hasNext();) {
				Integer preauthRequestId = (Integer) iterator.next();
				reqDto.setPreauthRequestId(preauthRequestId);
				 iPreAuthorizationRequestDao.updateIsSplitStatusByPreAuthorizationId(reqDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
	}


	@Override
	public Response getPatientDocumentNameForPreAuthorization(MPreauthRequestMasterDto patientDocReqDto)throws ApplicationException{
	try{
		return iPreAuthorizationRequestDao.getPatientDocumentNameForPreAuthorization(patientDocReqDto);
	}catch(Exception e){
		
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}



}
