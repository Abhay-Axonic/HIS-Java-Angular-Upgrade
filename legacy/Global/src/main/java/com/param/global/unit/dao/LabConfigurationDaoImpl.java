package com.param.global.unit.dao;

import java.math.BigInteger;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.global.LabConfigurationMaster;
import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.UserMasterDto;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class LabConfigurationDaoImpl  extends GenericDao<LabConfigurationMaster, Integer> implements ILabConfigurationDao,ICommonConstants{

	final static Logger logger = Logger.getLogger(LabConfigurationDaoImpl.class);

	public LabConfigurationDaoImpl() {
		super(LabConfigurationMaster.class);
	}

	@Override
	public Response saveLabConfiguration(LabConfigurationDto dto) throws ApplicationException {
		try{
				LabConfigurationMaster master = new LabConfigurationMaster();
					master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
					master.setOrganisationId(dto.getOrganisationId());
					master.setLabSpecialityId(dto.getLabSpecialityId());
					master.setBiochemistryId(dto.getBiochemistryId());			
					master.setHematologyId(dto.getHematologyId());
					master.setMicrobiologyId(dto.getMicrobiologyId());
					master.setHistopathologyId(dto.getHistopathologyId());
					master.setCystopathologyId(dto.getCystopathologyId());
					master.setIsPinApplicable(dto.getIsPinApplicable());
					master.setSerologyId(dto.getSerologyId());
					master.setIsPrebarcodeGenerantion(dto.getIsPrebarcodeGenerantion() !=null ? dto.getIsPrebarcodeGenerantion() :'N');
					master.setClinicalPathologyId(dto.getClinicalPathologyId());
					master.setStatus(ACTIVE);
					master.setUnitId(dto.getUnitId());
					master.setUpdatedBy(dto.getUpdatedBy());
					master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
					master.settLabConfigId(dto.gettLabConfigId());
					master.setIsSendToCrApplicable(dto.getIsSendToCrApplicable()!=null ? dto.getIsSendToCrApplicable() : 'N');
					master.setIsAcceptanceInDepartment(dto.getIsAcceptanceInDepartment()!=null ? dto.getIsAcceptanceInDepartment() : 'N');
					master.setIsSampleAcceptanceApplicable(dto.getIsSampleAcceptanceApplicable()!=null ? dto.getIsSampleAcceptanceApplicable() : 'N');
					master.setIsWorklistDepartment(dto.getIsWorklistDepartment()!=null ? dto.getIsWorklistDepartment() : 'N');
					master.setIsReportRecall(dto.getIsReportRecall()!=null ? dto.getIsReportRecall() : 'N');
					master.setIsValidationAndRelease(dto.getIsValidationAndRelease()!=null ? dto.getIsValidationAndRelease() : 'N');
					master.setReportReleasePin(dto.getReportReleasePin()!=null ? dto.getReportReleasePin() : 'N');
					master.setPhotoRequired(dto.getPhotoRequired() != null ?dto.getPhotoRequired():  'N');
					master.setConsolidatedResultEntry(dto.getConsolidatedResultEntry() != null ? dto.getConsolidatedResultEntry() : 'N');
					master.setGroupRelease(dto.getGroupRelease() != null ? dto.getGroupRelease() : 'N');
					master.setTriggerReleaseMail(dto.getTriggerReleaseMail() != null ? dto.getTriggerReleaseMail() : 'N');
					master.setIsPRNPrint(dto.getIsPRNPrint()!=null ? dto.getIsPRNPrint() : 'N');
					master.setCollectSampleOnOPBilling(dto.getCollectSampleOnOPBilling() != null ? dto.getCollectSampleOnOPBilling() : 'N');
					master.setLabSortingId(dto.getLabSortingId() != null ? dto.getLabSortingId() : null);
					master.setSkipValidation(dto.getSkipValidation() != null ? dto.getSkipValidation()  : 'N');
					master.setByContainer(dto.getByContainer()!= null ? dto.getByContainer() : 'N');
					master.setByDepartment(dto.getByDepartment()!= null ? dto.getByDepartment() : 'N');
					master.setBySampleType(dto.getBySampleType()!= null ? dto.getBySampleType() : 'N');
					master.setSendMailOnOrderCompletion(dto.getSendMailOnOrderCompletion()!= null  ? dto.getSendMailOnOrderCompletion() : 'N');
					master.setManualSampleCollectionTime(dto.getManualSampleCollectionTime() != null ? dto.getManualSampleCollectionTime() : 'N');
					master.setIncubationMasterApplicable(dto.getIncubationMasterApplicable() != null ? dto.getIncubationMasterApplicable() :'N');
					master.setMacroscopicExaminationApplicable(dto.getMacroscopicExaminationApplicable() != null ? dto.getMacroscopicExaminationApplicable() :'N');
					master.setMicroscopicExaminationApplicable(dto.getMicroscopicExaminationApplicable() != null ? dto.getMicroscopicExaminationApplicable() : 'N');
					master.setBiochemicalTestingApplicable(dto.getBiochemicalTestingApplicable() != null ? dto.getBiochemicalTestingApplicable() : 'N');
					master.setQrCodeRequired(dto.getQrCodeRequired() != null ? dto.getQrCodeRequired() : 'N');
					master.setOutsourceLabId(dto.getOutsourceLabId() != null ? dto.getOutsourceLabId() : null);
					master.setPrintLabReportBeforRelease(dto.getPrintLabReportBeforRelease()!= null ? dto.getPrintLabReportBeforRelease() : 'Y');
				sessionFactory.getCurrentSession().saveOrUpdate(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response updateLabConfiguration(LabConfigurationDto dto) throws ApplicationException {
		try{
			LabConfigurationMaster master = new LabConfigurationMaster();
			master.settLabConfigId(dto.gettLabConfigId());
			master.setCreatedDate(dto.getCreatedDate() != null && !dto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setOrganisationId(dto.getOrganisationId());
			master.setLabSpecialityId(dto.getLabSpecialityId());
			master.setBiochemistryId(dto.getBiochemistryId());			
			master.setHematologyId(dto.getHematologyId());
			master.setMicrobiologyId(dto.getMicrobiologyId());
			master.setHistopathologyId(dto.getHistopathologyId());
			master.setCystopathologyId(dto.getCystopathologyId());
			master.setIsPinApplicable(dto.getIsPinApplicable());
			master.setSerologyId(dto.getSerologyId());
			master.setClinicalPathologyId(dto.getClinicalPathologyId());
			master.setIsPrebarcodeGenerantion(dto.getIsPrebarcodeGenerantion() !=null ? dto.getIsPrebarcodeGenerantion() :'N');
			master.setStatus(ACTIVE);
			master.setUnitId(dto.getUnitId());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUpdatedDate(dto.getUpdatedDate() != null && !dto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(dto.getUnitId()));
			master.setIsSendToCrApplicable(dto.getIsSendToCrApplicable());
			master = update(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}
	}

	@Override
	public Response getLabConfigurationById(int tLabConfigId) throws ApplicationException {
		try {
			LabConfigurationDto dto=(LabConfigurationDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LAB_CONFIG_BY_ID")
					.setParameter("tLabConfigId", tLabConfigId)
					.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).uniqueResult();
		return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);

		} catch (HibernateException he) {
			logger.error("getLabConfigurationById failed", he);
		} catch (Exception e) {
			logger.error("getLabConfigurationById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateConfigStatus(int tLabConfigId, char status) throws ApplicationException {
		try{
			sessionFactory.getCurrentSession().getNamedQuery("UPDATE_LAB_CONFIG_STATUS")
			    .setParameter("tLabConfigId", tLabConfigId)
				.setParameter("status", status)
			.executeUpdate();
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_STATUS_SUCCESS, null, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getLabConfigurationList(int orgId, int unitId) throws ApplicationException {
		try {
			List<LabConfigurationDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LAB_CONFIG_LIST")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error("getLabConfigurationList failed", he);
		} catch (Exception e) {
			logger.error("getLabConfigurationList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkConfigurationExistOrNot(LabConfigurationDto dto) throws ApplicationException {
		try {
			BigInteger count=(BigInteger) sessionFactory.getCurrentSession()
					.getNamedQuery("CHECK_LAB_CONFIG_EXIST_OR_NOT")
					.setParameter("orgId", dto.getOrganisationId())
					.setParameter("unitId", dto.getUnitId())
					.uniqueResult();
		return new Response(SUCCESS, SUCCESS_CODE, null, null, count);

		} catch (HibernateException he) {
			logger.error("checkConfigurationExistOrNot failed", he);
		} catch (Exception e) {
			logger.error("checkConfigurationExistOrNot failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getLabConfigurationByOrgUnit(int orgId, int unitId) throws ApplicationException {
		try {
			LabConfigurationDto dto=(LabConfigurationDto) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LAB_CONFIG_BY_BY_ORG_UNIT")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(LabConfigurationDto.class)).uniqueResult();
		return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);

		} catch (HibernateException he) {
			logger.error("getLabConfigurationByOrgUnit failed", he);
		} catch (Exception e) {
			logger.error("getLabConfigurationByOrgUnit failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkPinExistOrNot(int orgId, int unitId, String pin,int userId) throws ApplicationException {
		try {
			BigInteger count= BigInteger.ZERO;
			if(userId>0){
				 StringBuilder query= new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("CHECK_PIN_EXIST_OR_NOT").getQueryString());
				 query.append(" and um.lab_pin <> '"+pin+"'");
						count=(BigInteger) sessionFactory.getCurrentSession().createSQLQuery(query.toString())
								.setParameter("orgId", orgId)
								.setParameter("unitId",unitId)
								.uniqueResult();
						
				if(count!=null && count.intValue()==0)
				{
					return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
				}
				else {
					return new Response<>(ERROR, ERROR_CODE, "Pin is already in use.", null, null);
				}
						
			}else{
				StringBuilder query= new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("CHECK_PIN_EXIST_OR_NOT_GET_USERID").getQueryString());
				 query.append(" and um.lab_pin = '"+pin+"'");
				 count=(BigInteger) sessionFactory.getCurrentSession().createSQLQuery(query.toString())
								.setParameter("orgId", orgId)
								.setParameter("unitId",unitId)
								.uniqueResult();
					if(count!=null && count.intValue()==0)
					{
						return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
					}
					else
					{
						return new Response<>(ERROR, ERROR_CODE, "Pin is already in use. Please try another pin.", null, null);
					}
			}


		} catch (HibernateException he) {
			logger.error("checkPinExistOrNot failed", he);
		} catch (Exception e) {
			logger.error("checkPinExistOrNot failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserByPin(int orgId, int unitId, String pin) throws ApplicationException {
		try {
			List<UserMasterDto> list=(List<UserMasterDto>) sessionFactory.getCurrentSession()
					.getNamedQuery("GET_USER_BY_PIN")
					.setParameter("orgId", orgId)
					//.setParameter("unitId", unitId)
					.setParameter("pin",pin)
					.setResultTransformer(Transformers.aliasToBean(UserMasterDto.class)).list();
			int cnt=0;
			if(list!=null && list.size()==1)
			{
			 cnt= list.getFirst().getUserId() ;	
			 return new Response(SUCCESS, SUCCESS_CODE, null, list, cnt);
			}
			else if(list!=null && list.size()>1){
				return new Response(ERROR, ERROR_CODE, "Duplicate Pin is Present..", null, cnt);
			}
			else {
				return new Response(ERROR, ERROR_CODE, "Pin is Not Present.", null, cnt);
			}

		} catch (HibernateException he) {
			logger.error("getUserByPin failed", he);
		} catch (Exception e) {
			logger.error("getUserByPin failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
