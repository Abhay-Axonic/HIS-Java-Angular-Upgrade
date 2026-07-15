package com.param.opd.coversheet.dao;

import java.util.List;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.ComplaintAppointmentDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.model.ComplaintAppointmentMapper;
import com.param.opd.coversheet.model.DiagnosisPatientAppointmentMapper;
import com.param.opd.coversheet.model.PatientFamilyHistory;
import com.param.opd.coversheet.model.PatientPersonalHistoryDetails;
import com.param.opd.coversheet.service.IPatientPersonalHistoryDetailsService;
import com.param.utility.TimeZoneComponent;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked", "rawtypes" })
public class PatientPersonalHistoryDetailsDaoImpl extends GenericDao<PatientPersonalHistoryDetails, Integer> implements IPatientPersonalHistoryDetailsDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(PatientPersonalHistoryDetailsDaoImpl.class);


	public PatientPersonalHistoryDetailsDaoImpl() {
		super(PatientPersonalHistoryDetails.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private IPatientPersonalHistoryDetailsService iPatientPersonalHistoryDetailsService;
	
	@SuppressWarnings("rawtypes")
	@Override
	public Response savePatientPersonalHistoryDetails(PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto)throws ApplicationException {
		try
		{
			for(int i=0;i<patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().size();i++)
			{
				if(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getPersonalHistoryDetailsId() != null && patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getPersonalHistoryDetailsId()>0)
				{
					PatientPersonalHistoryDetails patientPersonalHistoryDetails = findById(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getPersonalHistoryDetailsId());
					patientPersonalHistoryDetails.setIsReviewedFlag(YES);
					iPatientPersonalHistoryDetailsService.updateSaveNurseReviewedFlag(patientPersonalHistoryDetails);
				}
				
				PatientPersonalHistoryDetails patientPersonalHistoryDetails = new PatientPersonalHistoryDetails();
				patientPersonalHistoryDetails.setDescription(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getDescription());
				patientPersonalHistoryDetails.setIsNoSignificantHistoryStatus(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getIsNoSignificantHistoryStatus());
				patientPersonalHistoryDetails.setRemark(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getRemark());
				
				patientPersonalHistoryDetails.setRoleId(patientPersonalHistoryDetailsDto.getRoleId());
				
				patientPersonalHistoryDetails.setIsReviewedFlag(
						patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getIsReviewedFlag() == '\0'? NO :
						patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getIsReviewedFlag());
				
				patientPersonalHistoryDetails.setIsEnterInErrorStatus(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getIsEnterInErrorStatus());
				patientPersonalHistoryDetails.setCreatedDate(CommonDateUtils.getDate(patientPersonalHistoryDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
				if(patientPersonalHistoryDetailsDto.getEncounterId()!=null){
					patientPersonalHistoryDetails.setEncounterId(patientPersonalHistoryDetailsDto.getEncounterId());
				}else{
					patientPersonalHistoryDetails.setAdmissionId(patientPersonalHistoryDetailsDto.getAdmissionId());
				}
				
				patientPersonalHistoryDetails.setCreatedBy(patientPersonalHistoryDetailsDto.getCreatedBy());
				patientPersonalHistoryDetails.setUpdatedBy(patientPersonalHistoryDetailsDto.getUpdatedBy());
				patientPersonalHistoryDetails.setUpdatedDate(CommonDateUtils.getDate(patientPersonalHistoryDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
				patientPersonalHistoryDetails.setStatus(patientPersonalHistoryDetailsDto.getStatus());
				patientPersonalHistoryDetails.setOrganizationId(patientPersonalHistoryDetailsDto.getOrganizationId());
				patientPersonalHistoryDetails.setUnitId(patientPersonalHistoryDetailsDto.getUnitId());
				patientPersonalHistoryDetails.setPatientId(patientPersonalHistoryDetailsDto.getPatientId());
				
				PatientPersonalHistoryDetails historyId=save(patientPersonalHistoryDetails);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("savePatientPersonalHistoryDetails failed", he);
		} catch (Exception e) {
		logger.error("savePatientPersonalHistoryDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getListOfOldPatientPersonalHistoryDetails(PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto)throws ApplicationException {
		try
		{
			List<PatientPersonalHistoryDetailsDto> listPatientPersonalHistoryDetailsDto;
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_PERSONAL_HISTORY_DEATILS").getQueryString();
			StringBuffer subQuery=new StringBuffer();
			query = query.concat(subQuery.toString());	
			listPatientPersonalHistoryDetailsDto=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", patientPersonalHistoryDetailsDto.getPatientId())
					.setParameter("organizationId", patientPersonalHistoryDetailsDto.getOrganizationId())
					.setParameter("created_date", patientPersonalHistoryDetailsDto.getCreatedDate())
					.setResultTransformer(Transformers.aliasToBean(PatientPersonalHistoryDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientPersonalHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfOldPatientPersonalHistoryDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getListOfCurrentPatientPersonalHistoryDetails(
			PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto)
			throws ApplicationException {
		try
		{
			List<PatientPersonalHistoryDetailsDto> listPatientPersonalHistoryDetailsDto;
			if(patientPersonalHistoryDetailsDto.getRoleId() == 3)
			{

				String baseQuery = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_CURRENT_PATIENT_PERSONAL_HISTORY_DEATILS_BY_ROLE_ID")
						.getQueryString();
				StringBuilder queryBuilder = new StringBuilder(baseQuery);
				if (patientPersonalHistoryDetailsDto.getEncounterId() != null) {
					queryBuilder.append(" AND patientPersonalHist.encounterId = :encounterId")
							.append(" ORDER BY patientPersonalHist.personalHistoryDetailsId ASC");
				}
				if (patientPersonalHistoryDetailsDto.getAdmissionId() != null) {
					queryBuilder.append(" AND patientPersonalHist.admissionId = :admissionId")
							.append(" ORDER BY patientPersonalHist.personalHistoryDetailsId ASC");
				}
				String finalQuery = queryBuilder.toString();
				Query query = sessionFactory.getCurrentSession().createQuery(finalQuery)
						.setParameter("roleId", patientPersonalHistoryDetailsDto.getRoleId())
						.setParameter("patientId", patientPersonalHistoryDetailsDto.getPatientId())
						.setParameter("unitId", patientPersonalHistoryDetailsDto.getUnitId())
						.setParameter("organizationId", patientPersonalHistoryDetailsDto.getOrganizationId());
				if (patientPersonalHistoryDetailsDto.getEncounterId() != null) {
					query.setParameter("encounterId", patientPersonalHistoryDetailsDto.getEncounterId());
				}
				if (patientPersonalHistoryDetailsDto.getAdmissionId() != null) {
					query.setParameter("admissionId", patientPersonalHistoryDetailsDto.getAdmissionId());
				}
				listPatientPersonalHistoryDetailsDto = query
						.setResultTransformer(Transformers.aliasToBean(PatientPersonalHistoryDetailsDto.class))
						.list();

			}else
			{
				String baseQuery = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_CURRENT_PATIENT_PERSONAL_HISTORY_DEATILS")
						.getQueryString();
				StringBuilder queryBuilder = new StringBuilder(baseQuery);
				if (patientPersonalHistoryDetailsDto.getEncounterId() != null) {
					queryBuilder.append(" AND patientPersonalHist.encounterId = :encounterId")
							.append(" ORDER BY patientPersonalHist.personalHistoryDetailsId ASC");
				}
				if (patientPersonalHistoryDetailsDto.getAdmissionId() != null) {
					queryBuilder.append(" AND patientPersonalHist.admissionId = :admissionId")
							.append(" ORDER BY patientPersonalHist.personalHistoryDetailsId ASC");
				}
				String finalQuery = queryBuilder.toString();
				Query query = sessionFactory.getCurrentSession().createQuery(finalQuery)
						.setParameter("patientId", patientPersonalHistoryDetailsDto.getPatientId())
						.setParameter("unitId", patientPersonalHistoryDetailsDto.getUnitId())
						.setParameter("organizationId", patientPersonalHistoryDetailsDto.getOrganizationId());
				if (patientPersonalHistoryDetailsDto.getEncounterId() != null) {
					query.setParameter("encounterId", patientPersonalHistoryDetailsDto.getEncounterId());
				}
				if (patientPersonalHistoryDetailsDto.getAdmissionId() != null) {
					query.setParameter("admissionId", patientPersonalHistoryDetailsDto.getAdmissionId());
				}
				listPatientPersonalHistoryDetailsDto = query
						.setResultTransformer(Transformers.aliasToBean(PatientPersonalHistoryDetailsDto.class))
						.list();

			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientPersonalHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfCurrentPatientPersonalHistoryDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updatePatientPersonalHistoryDetails(PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().size();i++)
			{
				
				Session statlessSession = sessionFactory.getCurrentSession();
				PatientPersonalHistoryDetails patientPersonalHistoryDetails = findById(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getPersonalHistoryDetailsId());
				
				if(patientPersonalHistoryDetailsDto.getRoleId() != null 
						&& patientPersonalHistoryDetailsDto.getRoleId() == 2 
						&& patientPersonalHistoryDetails.getRoleId() != 2 
						&& patientPersonalHistoryDetails.getIsReviewedFlag() == 'N')
				{
					iPatientPersonalHistoryDetailsService.savePatientPersonalHistoryDetails(patientPersonalHistoryDetailsDto);
				}
				
				patientPersonalHistoryDetails.setDescription(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getDescription());
				patientPersonalHistoryDetails.setIsNoSignificantHistoryStatus(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getIsNoSignificantHistoryStatus());
				patientPersonalHistoryDetails.setRemark(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getRemark());
				patientPersonalHistoryDetails.setRoleId(patientPersonalHistoryDetails.getRoleId());
				patientPersonalHistoryDetails.setIsReviewedFlag(patientPersonalHistoryDetailsDto.getRoleId() == 2
						&& patientPersonalHistoryDetails.getRoleId() == 3 ? YES : patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getIsReviewedFlag());
				patientPersonalHistoryDetails.setIsEnterInErrorStatus(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getIsEnterInErrorStatus());
				
				patientPersonalHistoryDetails.setCreatedDate(CommonDateUtils.getDate(patientPersonalHistoryDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
				if(patientPersonalHistoryDetails.getEncounterId()!=null){
					patientPersonalHistoryDetails.setEncounterId(patientPersonalHistoryDetailsDto.getEncounterId());
				}else{
					patientPersonalHistoryDetails.setAdmissionId(patientPersonalHistoryDetailsDto.getAdmissionId());
				}
				patientPersonalHistoryDetails.setCreatedBy(patientPersonalHistoryDetailsDto.getCreatedBy());
				patientPersonalHistoryDetails.setUpdatedBy(patientPersonalHistoryDetailsDto.getUpdatedBy());
				patientPersonalHistoryDetails.setUpdatedDate(CommonDateUtils.getDate(patientPersonalHistoryDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
				patientPersonalHistoryDetails.setStatus(patientPersonalHistoryDetailsDto.getStatus());
				patientPersonalHistoryDetails.setOrganizationId(patientPersonalHistoryDetailsDto.getOrganizationId());
				patientPersonalHistoryDetails.setUnitId(patientPersonalHistoryDetailsDto.getUnitId());
				patientPersonalHistoryDetails.setPatientId(patientPersonalHistoryDetailsDto.getPatientId());
				save(patientPersonalHistoryDetails);
				
					
			}	
		
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch (HibernateException he) {
		logger.error("updatePatientPersonalHistoryDetails failed", he);
		} catch (Exception e) {
		logger.error("updatePatientPersonalHistoryDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateSaveNurseReviewedFlag(
			PatientPersonalHistoryDetails patientPersonalHistoryDetails)
			throws ApplicationException {
		try
		{
			save(patientPersonalHistoryDetails);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("updateSaveNurseReviewedFlag failed", he);
		} catch (Exception e) {
		logger.error("updateSaveNurseReviewedFlag failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateStatusEnterInError(PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		try
		{
			for(int i=0;i<patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().size();i++)
			{
				PatientPersonalHistoryDetails patientPersonalHistory = findById(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getPersonalHistoryDetailsId());
				patientPersonalHistory.setIsEnterInErrorStatus(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getIsEnterInErrorStatus());
				patientPersonalHistory.setIsReviewedFlag(patientPersonalHistory.getIsReviewedFlag() == 'N' && 
														 patientPersonalHistory.getRoleId() == 3 &&
														 patientPersonalHistoryDetailsDto.getRoleId() == 2
														 ? YES : patientPersonalHistory.getIsReviewedFlag());
				iPatientPersonalHistoryDetailsService.updateSaveNurseReviewedFlag(patientPersonalHistory);
				
				if(patientPersonalHistoryDetailsDto.getRoleId() != null && patientPersonalHistoryDetailsDto.getRoleId() == 2 && patientPersonalHistory.getRoleId() == 3)
				{
					PatientPersonalHistoryDetails patientPersonalHistoryDetails = new PatientPersonalHistoryDetails();
					patientPersonalHistoryDetails.setDescription(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getDescription());
					patientPersonalHistoryDetails.setIsNoSignificantHistoryStatus(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getIsNoSignificantHistoryStatus());
					patientPersonalHistoryDetails.setRemark(patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getRemark());
					
					patientPersonalHistoryDetails.setRoleId(patientPersonalHistoryDetailsDto.getRoleId());
					
					patientPersonalHistoryDetails.setIsReviewedFlag(
							patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getIsReviewedFlag() == '\0'? NO :
							patientPersonalHistoryDetailsDto.getListOfPatientPersonalHistoryDtos().get(i).getIsReviewedFlag());
					
					//patientPersonalHistoryDetails.setIsEnterInErrorStatus(patientPersonalHistoryDetailsDto.getRoleId() == 3? NO : YES);
					patientPersonalHistoryDetails.setIsEnterInErrorStatus(NO);
					patientPersonalHistoryDetails.setCreatedDate(CommonDateUtils.getDate(patientPersonalHistoryDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
					if(patientPersonalHistoryDetails.getEncounterId()!=null){
						patientPersonalHistoryDetails.setEncounterId(patientPersonalHistoryDetailsDto.getEncounterId());
					}else{
						patientPersonalHistoryDetails.setAdmissionId(patientPersonalHistoryDetailsDto.getAdmissionId());
					}
					patientPersonalHistoryDetails.setCreatedBy(patientPersonalHistoryDetailsDto.getCreatedBy());
					patientPersonalHistoryDetails.setUpdatedBy(patientPersonalHistoryDetailsDto.getUpdatedBy());
					patientPersonalHistoryDetails.setUpdatedDate(CommonDateUtils.getDate(patientPersonalHistoryDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
					patientPersonalHistoryDetails.setStatus(patientPersonalHistoryDetailsDto.getStatus());
					patientPersonalHistoryDetails.setOrganizationId(patientPersonalHistoryDetailsDto.getOrganizationId());
					patientPersonalHistoryDetails.setUnitId(patientPersonalHistoryDetailsDto.getUnitId());
					patientPersonalHistoryDetails.setPatientId(patientPersonalHistoryDetailsDto.getPatientId());
					
					save(patientPersonalHistoryDetails);
				}
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("updateStatusEnterInError failed", he);
		} catch (Exception e) {
		logger.error("updateStatusEnterInError failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCurrentPatientPersonalHistoryDetailsForCoversheet(
			PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto) throws ApplicationException {
		try
		{
			
			List<PatientPersonalHistoryDetailsDto> listPatientPersonalHistoryDetailsDto;
				String query = sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_PERSONAL_HISTORY_DEATILS_FOR_COVERSHEET").getQueryString();
				StringBuffer subQuery = new StringBuffer();		
						query = query.concat(subQuery.toString());
						listPatientPersonalHistoryDetailsDto=sessionFactory.getCurrentSession().createSQLQuery(query)
								.setParameter("patientId", patientPersonalHistoryDetailsDto.getPatientId())
								.setParameter("organizationId", patientPersonalHistoryDetailsDto.getOrganizationId())
								.setParameter("created_date", patientPersonalHistoryDetailsDto.getCreatedDate())
								.setResultTransformer(Transformers.aliasToBean(PatientPersonalHistoryDetailsDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientPersonalHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentPatientPersonalHistoryDetailsForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientPersonalHistoryForBookView(PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto) throws ApplicationException {
		try {
				List<PatientPersonalHistoryDetailsDto> listPatientPersonalHistoryDetailsDto;
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_PERSONAL_HISTORY_FOR_BOOK_VIEW").getQueryString();
				
				StringBuffer subQuery=new StringBuffer();
				
				listPatientPersonalHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("patientId", patientPersonalHistoryDetailsDto.getPatientId())
					.setParameter("organizationId", patientPersonalHistoryDetailsDto.getOrganizationId())
					.setParameter("fromDate", patientPersonalHistoryDetailsDto.getFromDate())
					.setParameter("toDate", patientPersonalHistoryDetailsDto.getToDate())
				.setResultTransformer(Transformers.aliasToBean(PatientPersonalHistoryDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientPersonalHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientPersonalHistoryForBookView failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveResolvedPersonalHistoryDate(PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto)
			throws ApplicationException {
		try {
			
			PatientPersonalHistoryDetails patientPersonalHistoryDetails = findById(patientPersonalHistoryDetailsDto.getPersonalHistoryDetailsId());
			patientPersonalHistoryDetails.setResolvedDate(GlobalCommonDateUtils.getDate(patientPersonalHistoryDetailsDto.getResolvedDate(), "dd-M-yyyy HH:mm:ss"));
			patientPersonalHistoryDetails.setResolvedRemark(patientPersonalHistoryDetailsDto.getResolvedRemark());
			update(patientPersonalHistoryDetails);
			return new Response(SUCCESS, SUCCESS_CODE, "Personal history marked as Resolved.", null, null);
		} catch (Exception e) {
		logger.error("saveResolvedPersonalHistoryDate failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getResolvedPatientPersonalHistoryDetailsForCoversheet(
			PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto) throws ApplicationException {
		try
		{
			
			List<PatientPersonalHistoryDetailsDto> listPatientPersonalHistoryDetailsDto;
				String query = sessionFactory.getCurrentSession().getNamedQuery("GET_RESOLVED_PATIENT_PERSONAL_HISTORY_DEATILS_FOR_COVERSHEET").getQueryString();
				StringBuffer subQuery = new StringBuffer();		
			
						query = query.concat(subQuery.toString());
						listPatientPersonalHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
								.setParameter("patientId", patientPersonalHistoryDetailsDto.getPatientId())
								.setParameter("organizationId", patientPersonalHistoryDetailsDto.getOrganizationId())
								.setResultTransformer(Transformers.aliasToBean(PatientPersonalHistoryDetailsDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientPersonalHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getResolvedPatientPersonalHistoryDetailsForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPersonalHistory(PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto)
			throws ApplicationException {
		try
		{
			List<PatientPersonalHistoryDetailsDto> listPatientPersonalHistoryDetailsDto;

			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_PERSONAL_HISTORY_FOR_DOCTOR_APP").getQueryString();
			StringBuffer subQuery = new StringBuffer();

			query = query.concat(subQuery.toString());
			listPatientPersonalHistoryDetailsDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", patientPersonalHistoryDetailsDto.getPatientId())
					.setParameter("organizationId", patientPersonalHistoryDetailsDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientPersonalHistoryDetailsDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientPersonalHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPersonalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response saveCurrentPatientPersonalHistoryDetails(List<PatientPersonalHistoryDetailsDto> list,
			CoversheetTemplateMasterDto resDto) throws ApplicationException {
		try{
			
			for (PatientPersonalHistoryDetailsDto dto : list){
				if(dto.getCovTemplateId() == null){
					PatientPersonalHistoryDetails patientPersonalHistoryDetails = new PatientPersonalHistoryDetails();
					patientPersonalHistoryDetails.setDescription(dto.getDescription());
					patientPersonalHistoryDetails.setIsNoSignificantHistoryStatus(dto.getIsNoSignificantHistoryStatus());
					patientPersonalHistoryDetails.setRemark(dto.getRemark());
					
					patientPersonalHistoryDetails.setRoleId(dto.getRoleId());
					
					patientPersonalHistoryDetails.setIsReviewedFlag(
							dto.getIsReviewedFlag() == '\0'? NO :
							dto.getIsReviewedFlag());
					
					patientPersonalHistoryDetails.setIsEnterInErrorStatus(dto.getIsEnterInErrorStatus());
					patientPersonalHistoryDetails.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientPersonalHistoryDetails.setCreatedBy(resDto.getCreatedBy());
					patientPersonalHistoryDetails.setUpdatedBy(resDto.getUpdatedBy());
					patientPersonalHistoryDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientPersonalHistoryDetails.setStatus(resDto.getStatus());
					patientPersonalHistoryDetails.setOrganizationId(resDto.getOrganizationId());
					patientPersonalHistoryDetails.setUnitId(resDto.getUnitId());
					patientPersonalHistoryDetails.setCovTemplateId(resDto.getCovTemplateId());
					save(patientPersonalHistoryDetails);
				}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCovTempCurrentPatientPersonalHistoryDetails(List<PatientPersonalHistoryDetailsDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
	try{
			
			for (PatientPersonalHistoryDetailsDto dto : list){
				if(dto.getCovTemplateId() != null){
				
					PatientPersonalHistoryDetails patientPersonalHistoryDetails = findById(coversheetTemplateMasterDto.getCovTemplateId());
					patientPersonalHistoryDetails.setDescription(dto.getDescription());
					patientPersonalHistoryDetails.setIsNoSignificantHistoryStatus(dto.getIsNoSignificantHistoryStatus());
					patientPersonalHistoryDetails.setRemark(dto.getRemark());
					
					patientPersonalHistoryDetails.setRoleId(dto.getRoleId());
					
					patientPersonalHistoryDetails.setIsReviewedFlag(
							dto.getIsReviewedFlag() == '\0'? NO :
							dto.getIsReviewedFlag());
					
					patientPersonalHistoryDetails.setIsEnterInErrorStatus(dto.getIsEnterInErrorStatus());
					patientPersonalHistoryDetails.setCreatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					patientPersonalHistoryDetails.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
					patientPersonalHistoryDetails.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
					patientPersonalHistoryDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					patientPersonalHistoryDetails.setStatus(coversheetTemplateMasterDto.getStatus());
					patientPersonalHistoryDetails.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					patientPersonalHistoryDetails.setUnitId(coversheetTemplateMasterDto.getUnitId());
					PatientPersonalHistoryDetails historyId=update(patientPersonalHistoryDetails);
				}
				}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}
	
	
}
