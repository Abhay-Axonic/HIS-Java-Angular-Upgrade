package com.param.service.services;

import java.util.Date;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.IError;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.Response;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.opd.encounter.dto.FundSanctionRequestDocumentDto;
import com.param.service.dao.IFundSanctionRequestDao;
import com.param.service.dto.BillFundSanctionDetailsDto;
import com.param.service.dto.FundSanctionAcceptanceRequestDto;
import com.param.service.dto.FundSanctionRequestDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unused","unchecked"})
public class FundSanctionRequestServiceImpl implements IFundSanctionRequestService, ICommonConstants, IError{
	
	@Autowired
	private IFundSanctionRequestDao iFundSanctionRequestDao;
	
	@Autowired
	private IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private DocUploadUtility docUploadUtility;
	
	@Override
	@Transactional
	public Response saveFundSanctionRequest(FundSanctionRequestDto fundSanctionRequestDto) throws ApplicationException {
		try {
			return iFundSanctionRequestDao.saveFundSanctionRequest(fundSanctionRequestDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response fundSanctionRequestByPatientId(FundSanctionRequestDto fundSanctionRequestDto) throws ApplicationException{
	try {
		return iFundSanctionRequestDao.fundSanctionRequestByPatientId(fundSanctionRequestDto);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response fundSanctionRequestDocument(FundSanctionRequestDocumentDto fundSanctionRequestDocumentDto)
			throws ApplicationException {
		try {
			Date date = TimeZoneComponent.getDateByzone(fundSanctionRequestDocumentDto.getUnitId());
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");

			if (fundSanctionRequestDocumentDto.getUnitId() > 0) {
				Response orgRes = iGlobalOrganisationMasterDao
						.getOrganiazationFolderPath(fundSanctionRequestDocumentDto.getOrgId());
				OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				String docPath = "";
					docPath = docUploadUtility.uploadFundRequestDocuments(fundSanctionRequestDocumentDto.getFile(),
							organizationMasterDto.getOrganizationFolder(), fundSanctionRequestDocumentDto);
					
					fundSanctionRequestDocumentDto.setDocumentPath(docPath);
					fundSanctionRequestDocumentDto.setCreatedDate(dateTime);
					fundSanctionRequestDocumentDto.setUpdatedDate(dateTime);
				return iFundSanctionRequestDao.fundSanctionRequestDocument(fundSanctionRequestDocumentDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getDocumentsByFundSanctionRequestId(FundSanctionRequestDocumentDto fundSanctionRequestDocumentDto)
			throws ApplicationException {
		try {
			return iFundSanctionRequestDao.getDocumentsByFundSanctionRequestId(fundSanctionRequestDocumentDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response saveApprovalRequest(FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto)
			throws ApplicationException {
		try {
			if(fundSanctionAcceptanceRequestDto.getFundrequestStatusId()!=null)
				if(fundSanctionAcceptanceRequestDto.getFundrequestStatusId()==2) {
					//accepted
					if(fundSanctionAcceptanceRequestDto.getFundAcceptacepaymentTypeId()!=null) {
						if(fundSanctionAcceptanceRequestDto.getFundAcceptacepaymentTypeId()== 1) {
						//debited from Trust fundinflow
							return iFundSanctionRequestDao.saveRequestApprovalFromFundinflow(fundSanctionAcceptanceRequestDto);
						}else if(fundSanctionAcceptanceRequestDto.getFundAcceptacepaymentTypeId()== 2) {
							//debited from Trust virtual
							return iFundSanctionRequestDao.saveRequestApprovalFromFundVirtual(fundSanctionAcceptanceRequestDto);
						}
					}
					
				}else if(fundSanctionAcceptanceRequestDto.getFundrequestStatusId()==3) {
					//rejected
					return iFundSanctionRequestDao.saveRequestReject(fundSanctionAcceptanceRequestDto);
				}
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getAllPendingVirtualAcceptance(FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto)
			throws ApplicationException {
		try {
			return iFundSanctionRequestDao.getAllPendingVirtualAcceptance(fundSanctionAcceptanceRequestDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getInflowAcceptedRequest(FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto)
			throws ApplicationException {
		try {
			return iFundSanctionRequestDao.getInflowAcceptedRequest(fundSanctionAcceptanceRequestDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getVirtualRequestPayments(FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto)
			throws ApplicationException {
		try {
			return iFundSanctionRequestDao.getVirtualRequestPayments(fundSanctionAcceptanceRequestDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getAllFundApprovalRequestByPatient(
			FundSanctionAcceptanceRequestDto fundSanctionAcceptanceRequestDto) throws ApplicationException {
		try {
			return iFundSanctionRequestDao.getAllFundApprovalRequestByPatient(fundSanctionAcceptanceRequestDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response saveBillFundDetails(BillFundSanctionDetailsDto billFundSanctionDetailsDto)
			throws ApplicationException {
		try {
			return iFundSanctionRequestDao.saveBillFundDetails(billFundSanctionDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getBillFundDetailsByBillMasterId(BillFundSanctionDetailsDto billFundSanctionDetailsDto)
			throws ApplicationException {
		try {
			return iFundSanctionRequestDao.getBillFundDetailsByBillMasterId(billFundSanctionDetailsDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}
	
	

}
