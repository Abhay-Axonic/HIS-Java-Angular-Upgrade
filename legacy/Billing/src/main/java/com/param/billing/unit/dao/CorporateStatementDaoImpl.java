package com.param.billing.unit.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.common.IError;
import com.param.billing.common.Response;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.dto.CorporateStatementDto;
import com.param.global.model.CabinMaster;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.CoversheetAccessMasterDto;
import com.param.service.global.model.CorporateStatmentMaster;
import com.param.service.global.model.MCompanyContractMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class CorporateStatementDaoImpl  extends GenericDao<CorporateStatmentMaster, Integer> implements ICorporateStatementDao,ICommonConstants, IError{

	public CorporateStatementDaoImpl() {
		super(CorporateStatmentMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response saveCorporateStatementMasterDetails(CorporateStatementDto corporateStatementDto)
			throws ApplicationException {

		try {
			CorporateStatmentMaster corporateStatmentMaster=new CorporateStatmentMaster();
			corporateStatmentMaster.setVisitTypeId(corporateStatementDto.getVisitTypeId());
			corporateStatmentMaster.setBillId(corporateStatementDto.getBillId());
			corporateStatmentMaster.setAddress1(corporateStatementDto.getAddress1());
			corporateStatmentMaster.setAddress2(corporateStatementDto.getAddress2());
			corporateStatmentMaster.setConsAmount(corporateStatementDto.getConsAmount());
			corporateStatmentMaster.setConsultant(corporateStatementDto.getConsultant());
			corporateStatmentMaster.setConsDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getConsDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setDisease(corporateStatementDto.getDisease());
			corporateStatmentMaster.setDoneOnDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getDoneOnDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setEmployeeName(corporateStatementDto.getEmployeeName());
			corporateStatmentMaster.setFirstName(corporateStatementDto.getFirstName());
			corporateStatmentMaster.setFatherName(corporateStatementDto.getFatherName());
			corporateStatmentMaster.setHospitalTreatment(corporateStatementDto.getHospitalTreatment());
			corporateStatmentMaster.setInvestigation(corporateStatementDto.getInvestigation());
			corporateStatmentMaster.setRelationId(corporateStatementDto.getRelationId());
			corporateStatmentMaster.setRelation(corporateStatementDto.getRelation());
			corporateStatmentMaster.setInvoiceNumber(corporateStatementDto.getInvoiceNumber());
			corporateStatmentMaster.setHospitalName(corporateStatementDto.getHospitalName());
			corporateStatmentMaster.setLabAmount(corporateStatementDto.getLabAmount());
			corporateStatmentMaster.setOrgName(corporateStatementDto.getOrgName());
			corporateStatmentMaster.setLetterType(corporateStatementDto.getLetterType());
			corporateStatmentMaster.setVisitfromDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getVisitfromDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setVisittoDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getVisittoDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setTreatfromDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getTreatfromDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setTreattoDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getTreattoDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setPharmacyAmount(corporateStatementDto.getPharmacyAmount());
			corporateStatmentMaster.setReferenceDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getReferenceDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setOrganizationId(corporateStatementDto.getOrganizationId());
			corporateStatmentMaster.setUnitId(corporateStatementDto.getUnitId());
			corporateStatmentMaster.setStatus(corporateStatementDto.getStatus());
			corporateStatmentMaster.setCreatedBy(corporateStatementDto.getCreatedBy());
			corporateStatmentMaster.setCreatedDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setUpdatedBy(corporateStatementDto.getUpdatedBy());
			corporateStatmentMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setMedicalCardNo(corporateStatementDto.getMedicalCardNo());
			corporateStatmentMaster.setAuthorizationLetterNo(corporateStatementDto.getAuthorizationLetterNo());
			save(corporateStatmentMaster);
			
			corporateStatementDto.setCorporateStatementId(corporateStatmentMaster.getCorporateStatementId());
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,corporateStatementDto.getCorporateStatementId());
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	}

	@Override
	public Response updateCorporateStatementMasterDetails(CorporateStatementDto corporateStatementDto)
			throws ApplicationException {

		try {
			CorporateStatmentMaster corporateStatmentMaster=findById(corporateStatementDto.getCorporateStatementId());
			corporateStatmentMaster.setVisitTypeId(corporateStatementDto.getVisitTypeId());
			corporateStatmentMaster.setBillId(corporateStatementDto.getBillId());
			corporateStatmentMaster.setAddress1(corporateStatementDto.getAddress1());
			corporateStatmentMaster.setAddress2(corporateStatementDto.getAddress2());
			corporateStatmentMaster.setConsAmount(corporateStatementDto.getConsAmount());
			corporateStatmentMaster.setConsultant(corporateStatementDto.getConsultant());
			corporateStatmentMaster.setConsDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getConsDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setDisease(corporateStatementDto.getDisease());
			corporateStatmentMaster.setDoneOnDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getDoneOnDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setEmployeeName(corporateStatementDto.getEmployeeName());
			corporateStatmentMaster.setFirstName(corporateStatementDto.getFirstName());
			corporateStatmentMaster.setFatherName(corporateStatementDto.getFatherName());
			corporateStatmentMaster.setHospitalTreatment(corporateStatementDto.getHospitalTreatment());
			corporateStatmentMaster.setInvestigation(corporateStatementDto.getInvestigation());
			corporateStatmentMaster.setRelationId(corporateStatementDto.getRelationId());
			corporateStatmentMaster.setRelation(corporateStatementDto.getRelation());
			corporateStatmentMaster.setInvoiceNumber(corporateStatementDto.getInvoiceNumber());
			corporateStatmentMaster.setHospitalName(corporateStatementDto.getHospitalName());
			corporateStatmentMaster.setLabAmount(corporateStatementDto.getLabAmount());
			corporateStatmentMaster.setOrgName(corporateStatementDto.getOrgName());
			corporateStatmentMaster.setLetterType(corporateStatementDto.getLetterType());
			corporateStatmentMaster.setVisitfromDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getVisitfromDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setVisittoDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getVisittoDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setTreatfromDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getTreatfromDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setTreattoDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getTreattoDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setPharmacyAmount(corporateStatementDto.getPharmacyAmount());
			corporateStatmentMaster.setReferenceDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getReferenceDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setOrganizationId(corporateStatementDto.getOrganizationId());
			corporateStatmentMaster.setUnitId(corporateStatementDto.getUnitId());
			corporateStatmentMaster.setStatus(corporateStatementDto.getStatus());
			corporateStatmentMaster.setUpdatedBy(corporateStatementDto.getUpdatedBy());
			corporateStatmentMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(corporateStatementDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			corporateStatmentMaster.setMedicalCardNo(corporateStatementDto.getMedicalCardNo());
			corporateStatmentMaster.setAuthorizationLetterNo(corporateStatementDto.getAuthorizationLetterNo());
			update(corporateStatmentMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, corporateStatementDto.getCorporateStatementId());
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	
	}

	@Override
	public Response getCorporateStatementMasterById(Integer billId, Integer organizationId,
			Integer unitId) throws ApplicationException {
		try {
			List<CabinMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CORPORATE_STATEMENT_MASTER_BY_ID")
					.setParameter("billId", billId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(CorporateStatementDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkAlreadyExit(CorporateStatementDto corporateStatementDto) throws ApplicationException {
		try {
			List<CorporateStatementDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECK")
					.setParameter("billId", corporateStatementDto.getBillId())
					.setParameter("invoiceNumber", corporateStatementDto.getInvoiceNumber())
					.setResultTransformer(Transformers.aliasToBean(CorporateStatementDto.class)).list();
			 
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	

}
