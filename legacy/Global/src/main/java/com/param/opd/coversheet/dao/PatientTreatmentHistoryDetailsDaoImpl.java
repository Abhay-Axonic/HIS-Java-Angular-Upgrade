package com.param.opd.coversheet.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.dto.PatientTreatmentHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientTreatmentHistoryDetailsDto;
import com.param.opd.coversheet.model.PatientHabitDetails;
import com.param.opd.coversheet.model.PatientTreatmentHistoryDetails;
import com.param.opd.coversheet.model.PatientTreatmentHistoryDetails;
import com.param.opd.coversheet.service.IPatientTreatmentHistoryDetailsService;
import com.param.utility.TimeZoneComponent;
import com.param.opd.coversheet.model.PatientTreatmentHistoryDetails;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked","rawtypes" })
public class PatientTreatmentHistoryDetailsDaoImpl extends GenericDao<PatientTreatmentHistoryDetails, Integer> implements IPatientTreatmentHistoryDetailsDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(PatientTreatmentHistoryDetailsDaoImpl.class);


	public PatientTreatmentHistoryDetailsDaoImpl() {
		super(PatientTreatmentHistoryDetails.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private IPatientTreatmentHistoryDetailsService iPatientTreatmentHistoryDetailsService;

	@Override
	public Response updateEnterErrorStatusPatientTreatmentHistoryDetails(
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response updatePatientTreatmentHistoryDetails(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
		try
		{
			for(int i=0;i<patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().size();i++)
			{
				Session statlessSession = sessionFactory.getCurrentSession();
				PatientTreatmentHistoryDetails patientTreatmentHistoryDetails = findById(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getTreatmentHistoryId());
				

				if(patientTreatmentHistoryDetailsDto.getRoleId() != null && 
				   patientTreatmentHistoryDetailsDto.getRoleId() == 2 && 
				   patientTreatmentHistoryDetails.getRoleId() != 2 && 
				   patientTreatmentHistoryDetails.getIsReviewedFlag() == 'N')
				{
					iPatientTreatmentHistoryDetailsService.savePatientTreatmentHistoryDetails(patientTreatmentHistoryDetailsDto);
				}
				
				patientTreatmentHistoryDetails.setMonth(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getMonth());
				patientTreatmentHistoryDetails.setIsNoSignificantStatus(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getIsNoSignificantStatus());
				patientTreatmentHistoryDetails.setYear(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getYear());
				patientTreatmentHistoryDetails.setRemark(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getRemark());
				patientTreatmentHistoryDetails.setSurgenName(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getSurgenName());
				patientTreatmentHistoryDetails.setTreatementName(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getTreatementName());
				patientTreatmentHistoryDetails.setTreatDate(CommonDateUtils.getDate(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getTreatDate(),"dd-MM-yyyy HH:mm:ss"));

				if(patientTreatmentHistoryDetailsDto.getEncounterId()!=null){
					patientTreatmentHistoryDetails.setEncounterId(patientTreatmentHistoryDetailsDto.getEncounterId());
				}
				if(patientTreatmentHistoryDetailsDto.getAdmissionId()!=null){
					patientTreatmentHistoryDetails.setAdmissionId(patientTreatmentHistoryDetailsDto.getAdmissionId());
				}
				patientTreatmentHistoryDetails.setIsEnterInErrorStatus(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getIsEnterInErrorStatus());
				patientTreatmentHistoryDetails.setRoleId(patientTreatmentHistoryDetails.getRoleId());
				patientTreatmentHistoryDetails.setIsReviewedFlag(patientTreatmentHistoryDetailsDto.getRoleId() == 2 && patientTreatmentHistoryDetails.getRoleId() == 3 ? YES :
					patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getIsReviewedFlag()
					);
//				patientTreatmentHistoryDetails.setTreatDate(CommonDateUtils.getDate(patientTreatmentHistoryDetailsDto.getTreatDate(), "dd-MM-yyyy"));
				patientTreatmentHistoryDetails.setCreatedDate(CommonDateUtils.getDate(patientTreatmentHistoryDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
				patientTreatmentHistoryDetails.setCreatedBy(patientTreatmentHistoryDetailsDto.getCreatedBy());
				patientTreatmentHistoryDetails.setUpdatedBy(patientTreatmentHistoryDetailsDto.getUpdatedBy());
				patientTreatmentHistoryDetails.setUpdatedDate(CommonDateUtils.getDate(patientTreatmentHistoryDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
				patientTreatmentHistoryDetails.setStatus(patientTreatmentHistoryDetailsDto.getStatus());
				patientTreatmentHistoryDetails.setOrganizationId(patientTreatmentHistoryDetailsDto.getOrganizationId());
				patientTreatmentHistoryDetails.setUnitId(patientTreatmentHistoryDetailsDto.getUnitId());
				patientTreatmentHistoryDetails.setPatientId(patientTreatmentHistoryDetailsDto.getPatientId());
				
				save(patientTreatmentHistoryDetails);
				
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updatePatientTreatmentHistoryDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response savePatientTreatmentHistoryDetails(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
		try
		{
			for(int i=0;i<patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().size();i++)
			{
				if(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getTreatmentHistoryId()!= null &&
						patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getTreatmentHistoryId() >0
						)
				{
					PatientTreatmentHistoryDetails patientTreatmentHistoryDetails = findById(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getTreatmentHistoryId());
					patientTreatmentHistoryDetails.setIsReviewedFlag('N');
					patientTreatmentHistoryDetails.setMonth(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getMonth());
					patientTreatmentHistoryDetails.setIsNoSignificantStatus(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getIsNoSignificantStatus());
					patientTreatmentHistoryDetails.setYear(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getYear());
					patientTreatmentHistoryDetails.setRemark(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getRemark());
					patientTreatmentHistoryDetails.setSurgenName(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getSurgenName());
					patientTreatmentHistoryDetails.setTreatementName(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getTreatementName());
					patientTreatmentHistoryDetails.setTreatDate(CommonDateUtils.getDate(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getTreatDate(),"dd-MM-yyyy HH:mm:ss"));
					if(patientTreatmentHistoryDetailsDto.getEncounterId()!=null){
						patientTreatmentHistoryDetails.setEncounterId(patientTreatmentHistoryDetailsDto.getEncounterId());
					}
					if(patientTreatmentHistoryDetailsDto.getAdmissionId()!=null){
						patientTreatmentHistoryDetails.setAdmissionId(patientTreatmentHistoryDetailsDto.getAdmissionId());
					}
					patientTreatmentHistoryDetails.setRoleId(patientTreatmentHistoryDetailsDto.getRoleId());
					patientTreatmentHistoryDetails.setIsEnterInErrorStatus(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getIsEnterInErrorStatus());
					
					patientTreatmentHistoryDetails.setIsReviewedFlag(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getIsReviewedFlag() == '\0' ? NO:
								patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getIsReviewedFlag()
							);
					
					patientTreatmentHistoryDetails.setCreatedDate(CommonDateUtils.getDate(patientTreatmentHistoryDetailsDto.getCreatedDate(), "dd-MM-yyyy HH:mm:ss"));
					patientTreatmentHistoryDetails.setCreatedBy(patientTreatmentHistoryDetailsDto.getCreatedBy());
					patientTreatmentHistoryDetails.setUpdatedBy(patientTreatmentHistoryDetailsDto.getUpdatedBy());
					patientTreatmentHistoryDetails.setUpdatedDate(CommonDateUtils.getDate(patientTreatmentHistoryDetailsDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
					patientTreatmentHistoryDetails.setStatus('I');
					patientTreatmentHistoryDetails.setOrganizationId(patientTreatmentHistoryDetailsDto.getOrganizationId());
					patientTreatmentHistoryDetails.setUnitId(patientTreatmentHistoryDetailsDto.getUnitId());
					patientTreatmentHistoryDetails.setPatientId(patientTreatmentHistoryDetailsDto.getPatientId());
					iPatientTreatmentHistoryDetailsService.updateSaveNurseReviewedFlag(patientTreatmentHistoryDetails);
				}else {
				
				PatientTreatmentHistoryDetails patientTreatmentHistoryDetails = new PatientTreatmentHistoryDetails();
				patientTreatmentHistoryDetails.setMonth(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getMonth());
				patientTreatmentHistoryDetails.setIsNoSignificantStatus(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getIsNoSignificantStatus());
				patientTreatmentHistoryDetails.setYear(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getYear());
				patientTreatmentHistoryDetails.setRemark(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getRemark());
				patientTreatmentHistoryDetails.setSurgenName(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getSurgenName());
				patientTreatmentHistoryDetails.setTreatementName(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getTreatementName());
				patientTreatmentHistoryDetails.setTreatDate(CommonDateUtils.getDate(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getTreatDate(),"dd-MM-yyyy HH:mm:ss"));
				if(patientTreatmentHistoryDetailsDto.getEncounterId()!=null){
					patientTreatmentHistoryDetails.setEncounterId(patientTreatmentHistoryDetailsDto.getEncounterId());
				}
				if(patientTreatmentHistoryDetailsDto.getAdmissionId()!=null){
					patientTreatmentHistoryDetails.setAdmissionId(patientTreatmentHistoryDetailsDto.getAdmissionId());
				}
				patientTreatmentHistoryDetails.setRoleId(patientTreatmentHistoryDetailsDto.getRoleId());
				patientTreatmentHistoryDetails.setIsEnterInErrorStatus(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getIsEnterInErrorStatus());
				
				patientTreatmentHistoryDetails.setIsReviewedFlag(patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getIsReviewedFlag() == '\0' ? NO:
							patientTreatmentHistoryDetailsDto.getListOfPatientTreatmentHistoryDto().get(i).getIsReviewedFlag()
						);
				
				patientTreatmentHistoryDetails.setCreatedDate(CommonDateUtils.getDate(patientTreatmentHistoryDetailsDto.getCreatedDate(), "dd-MM-yyyy HH:mm:ss"));
//				patientTreatmentHistoryDetails.setTreatDate(CommonDateUtils.getDate(patientTreatmentHistoryDetailsDto.getTreatDate(),"dd-M-yyyy HH:mm:ss"));
				//patientTreatmentHistoryDetails.setAppointmentId(patientTreatmentHistoryDetailsDto.getAppointmentId());
				patientTreatmentHistoryDetails.setCreatedBy(patientTreatmentHistoryDetailsDto.getCreatedBy());
				patientTreatmentHistoryDetails.setUpdatedBy(patientTreatmentHistoryDetailsDto.getUpdatedBy());
				patientTreatmentHistoryDetails.setUpdatedDate(CommonDateUtils.getDate(patientTreatmentHistoryDetailsDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
				patientTreatmentHistoryDetails.setStatus(patientTreatmentHistoryDetailsDto.getStatus());
				patientTreatmentHistoryDetails.setOrganizationId(patientTreatmentHistoryDetailsDto.getOrganizationId());
				patientTreatmentHistoryDetails.setUnitId(patientTreatmentHistoryDetailsDto.getUnitId());
				patientTreatmentHistoryDetails.setPatientId(patientTreatmentHistoryDetailsDto.getPatientId());
				
				PatientTreatmentHistoryDetails historyId=save(patientTreatmentHistoryDetails);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("savePatientTreatmentHistoryDetails failed", he);
		} catch (Exception e) {
		logger.error("savePatientTreatmentHistoryDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getOldListOfPatientTreatmentHistoryDetails(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
		try
		{
			List<PatientTreatmentHistoryDetailsDto> listPatientTreatmentHistoryDetailsDto;
		
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_TREATMENT_HISTORY").getQueryString();
			StringBuffer subQuery=new StringBuffer();
			query = query.concat(subQuery.toString());	
			listPatientTreatmentHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("patientId", patientTreatmentHistoryDetailsDto.getPatientId())
					.setParameter("organizationId", patientTreatmentHistoryDetailsDto.getOrganizationId())
					.setParameter("created_date", patientTreatmentHistoryDetailsDto.getCreatedDate())
					.setResultTransformer(Transformers.aliasToBean(PatientTreatmentHistoryDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTreatmentHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getOldListOfPatientTreatmentHistoryDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCurrentListOfPatientTreatmentHistoryDetails(
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
		try
		{
			List<PatientTreatmentHistoryDetailsDto> listPatientTreatmentHistoryDetailsDto;
			

				

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_TREATMENT_HISTORY").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				query = query.concat(subQuery.toString());
				listPatientTreatmentHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientTreatmentHistoryDetailsDto.getPatientId())
						.setParameter("unitId", patientTreatmentHistoryDetailsDto.getUnitId())
						.setParameter("organizationId", patientTreatmentHistoryDetailsDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientTreatmentHistoryDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTreatmentHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentListOfPatientTreatmentHistoryDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	@Override
	public Response updateSaveNurseReviewedFlag(
			PatientTreatmentHistoryDetails patientTreatmentHistoryDetails)
			throws ApplicationException {
		try
		{
			save(patientTreatmentHistoryDetails);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateSaveNurseReviewedFlag failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCurrentPatientTreatmentHistoryDetailsForCoversheet(
			PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
		try
		{
			List<PatientTreatmentHistoryDetailsDto> listPatientTreatmentHistoryDetailsDto;
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_TREATMENT_HISTORY_FOR_COVERSHEET").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				query = query.concat(subQuery.toString());
				listPatientTreatmentHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientTreatmentHistoryDetailsDto.getPatientId())
						.setParameter("organizationId", patientTreatmentHistoryDetailsDto.getOrganizationId())
						.setParameter("created_date", patientTreatmentHistoryDetailsDto.getCreatedDate())
						.setResultTransformer(Transformers.aliasToBean(PatientTreatmentHistoryDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTreatmentHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentPatientTreatmentHistoryDetailsForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientTreatmentHistoryForBookView(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
		try
		{
			List<PatientTreatmentHistoryDetailsDto> listPatientTreatmentHistoryDetailsDto;
			
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_TREATMENT_HISTORY_FOR_BOOK_VIEW").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				
				query = query.concat(subQuery.toString());	
				listPatientTreatmentHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientTreatmentHistoryDetailsDto.getPatientId())
						.setParameter("organizationId", patientTreatmentHistoryDetailsDto.getOrganizationId())
						.setParameter("fromDate", patientTreatmentHistoryDetailsDto.getFromDate())
						.setParameter("toDate", patientTreatmentHistoryDetailsDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(PatientTreatmentHistoryDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTreatmentHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientTreatmentHistoryForBookView failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCompletedDiagnosticsProcedures(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
		try
		{
			List<PatientTreatmentHistoryDetailsDto> listPatientTreatmentHistoryDetailsDto;
			
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_COMPLETED_DIAGNOSTICS_PROCEDURES").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				
				query = query.concat(subQuery.toString());	
				listPatientTreatmentHistoryDetailsDto=sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("patientId", patientTreatmentHistoryDetailsDto.getPatientId())
						.setParameter("organizationId", patientTreatmentHistoryDetailsDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientTreatmentHistoryDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTreatmentHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCompletedDiagnosticsProcedures failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCompletedMedicalProcedures(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto)
			throws ApplicationException {
		try
		{
			List<PatientTreatmentHistoryDetailsDto> listPatientTreatmentHistoryDetailsDto;
			
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_COMPLETED_MEDICAL_PROCEDURES").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				
				query = query.concat(subQuery.toString());	
				listPatientTreatmentHistoryDetailsDto=sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("patientId", patientTreatmentHistoryDetailsDto.getPatientId())
						.setParameter("organizationId", patientTreatmentHistoryDetailsDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientTreatmentHistoryDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTreatmentHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCompletedMedicalProcedures failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateStatusRemoveMedicalHistory( PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {try {
		       
		        PatientTreatmentHistoryDetails patientTreatmentHistoryDetails = findById(patientTreatmentHistoryDetailsDto.getTreatmentHistoryId());
				patientTreatmentHistoryDetails.setStatus('I');
				patientTreatmentHistoryDetails.setUpdatedBy(patientTreatmentHistoryDetailsDto.getUpdatedBy());
				patientTreatmentHistoryDetails.setUpdatedDate(CommonDateUtils.getDate(patientTreatmentHistoryDetailsDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
				update(patientTreatmentHistoryDetails);
		
		return new Response(SUCCESS, SUCCESS_CODE, "Cancel successfully", null, null);
		
		} catch (HibernateException he) {
		logger.error("updateStatusRemoveMedicalHistory failed", he);
		} catch (Exception e) {
		logger.error("updateStatusRemoveMedicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientTreatmentHistoryForDischargeSummary(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto) throws ApplicationException {
		try
		{
			List<PatientTreatmentHistoryDetailsDto> listPatientTreatmentHistoryDetailsDto;
			
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_TREATMENT_HISTORY_FOR_DISCHARGE_SUMMARY").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				query = query.concat(subQuery.toString());	
				listPatientTreatmentHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientTreatmentHistoryDetailsDto.getPatientId())
						.setParameter("organizationId", patientTreatmentHistoryDetailsDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientTreatmentHistoryDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTreatmentHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientTreatmentHistoryForDischargeSummary failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response getInvestigationHistory(PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto)
			throws ApplicationException {
		try
		{
			List<PatientTreatmentHistoryDetailsDto> finalTreatmentHistoryList = new ArrayList<PatientTreatmentHistoryDetailsDto>();
			List<PatientTreatmentHistoryDetailsDto> listPatientTreatmentHistoryDetailsDto = new ArrayList<PatientTreatmentHistoryDetailsDto>();
			List<PatientTreatmentHistoryDetailsDto> listPatientTreatmentHistoryDetailsDto1 = new ArrayList<PatientTreatmentHistoryDetailsDto>();
			List<PatientTreatmentHistoryDetailsDto> listPatientTreatmentHistoryDetailsDto2 = new ArrayList<PatientTreatmentHistoryDetailsDto>();

			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_TREATMENT_HISTORY_FOR_DOCTOR_APP").getQueryString();
			StringBuffer subQuery = new StringBuffer();

			query = query.concat(subQuery.toString());
			listPatientTreatmentHistoryDetailsDto = sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("patientId", patientTreatmentHistoryDetailsDto.getPatientId())
					.setParameter("organizationId", patientTreatmentHistoryDetailsDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientTreatmentHistoryDetailsDto.class)).list();

			if (!listPatientTreatmentHistoryDetailsDto.isEmpty()) {
				finalTreatmentHistoryList.addAll(listPatientTreatmentHistoryDetailsDto);
			}

			String query1 = sessionFactory.getCurrentSession().getNamedQuery("GET_COMPLETED_DIAGNOSTICS_PROCEDURES")
					.getQueryString();
			StringBuffer subQuery1 = new StringBuffer();

			query = query.concat(subQuery1.toString());
			listPatientTreatmentHistoryDetailsDto1= sessionFactory.getCurrentSession().createSQLQuery(query1)
					.setParameter("patientId", patientTreatmentHistoryDetailsDto.getPatientId())
					.setParameter("organizationId", patientTreatmentHistoryDetailsDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientTreatmentHistoryDetailsDto.class)).list();

			if (!listPatientTreatmentHistoryDetailsDto1.isEmpty()) {
				finalTreatmentHistoryList.addAll(listPatientTreatmentHistoryDetailsDto1);
			}

			String query2 = sessionFactory.getCurrentSession().getNamedQuery("GET_COMPLETED_MEDICAL_PROCEDURES")
					.getQueryString();
			StringBuffer subQuery2 = new StringBuffer();

			query = query.concat(subQuery2.toString());
			listPatientTreatmentHistoryDetailsDto2 = sessionFactory.getCurrentSession().createSQLQuery(query2)
					.setParameter("patientId", patientTreatmentHistoryDetailsDto.getPatientId())
					.setParameter("organizationId", patientTreatmentHistoryDetailsDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientTreatmentHistoryDetailsDto.class)).list();
			if (!listPatientTreatmentHistoryDetailsDto2.isEmpty()) {
				finalTreatmentHistoryList.addAll(listPatientTreatmentHistoryDetailsDto2);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, finalTreatmentHistoryList, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getInvestigationHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response savePatientTreatmentHistoryDetails(List<PatientTreatmentHistoryDetailsDto> list,
			CoversheetTemplateMasterDto resDto) throws ApplicationException {
		try{
			
			for (PatientTreatmentHistoryDetailsDto dto : list){
				if(dto.getCovTemplateId() == null){
					PatientTreatmentHistoryDetails patientTreatmentHistoryDetails = new PatientTreatmentHistoryDetails();
					patientTreatmentHistoryDetails.setMonth(dto.getMonth());
					patientTreatmentHistoryDetails.setIsNoSignificantStatus(dto.getIsNoSignificantStatus());
					patientTreatmentHistoryDetails.setYear(dto.getYear());
					patientTreatmentHistoryDetails.setRemark(dto.getRemark());
					patientTreatmentHistoryDetails.setSurgenName(dto.getSurgenName());
					patientTreatmentHistoryDetails.setTreatementName(dto.getTreatementName());
					patientTreatmentHistoryDetails.setTreatDate(CommonDateUtils.getDate(dto.getTreatDate(),"dd-MM-yyyy HH:mm:ss"));
					patientTreatmentHistoryDetails.setRoleId(dto.getRoleId());
					patientTreatmentHistoryDetails.setIsEnterInErrorStatus(dto.getIsEnterInErrorStatus());
					
					patientTreatmentHistoryDetails.setIsReviewedFlag(dto.getIsReviewedFlag() == '\0' ? NO:
								dto.getIsReviewedFlag()
							);
					
					patientTreatmentHistoryDetails.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientTreatmentHistoryDetails.setCreatedBy(resDto.getCreatedBy());
					patientTreatmentHistoryDetails.setUpdatedBy(resDto.getUpdatedBy());
					patientTreatmentHistoryDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientTreatmentHistoryDetails.setStatus(resDto.getStatus());
					patientTreatmentHistoryDetails.setOrganizationId(resDto.getOrganizationId());
					patientTreatmentHistoryDetails.setUnitId(resDto.getUnitId());
					patientTreatmentHistoryDetails.setCovTemplateId(resDto.getCovTemplateId());
					
					save(patientTreatmentHistoryDetails);
					
				}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCovTempPatientTreatmentHistoryDetails(List<PatientTreatmentHistoryDetailsDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
	try{
			
			for (PatientTreatmentHistoryDetailsDto dto : list){
				if(dto.getCovTemplateId() != null){
					
					PatientTreatmentHistoryDetails patientTreatmentHistoryDetails =findById(coversheetTemplateMasterDto.getCovTemplateId());
					patientTreatmentHistoryDetails.setMonth(dto.getMonth());
					patientTreatmentHistoryDetails.setIsNoSignificantStatus(dto.getIsNoSignificantStatus());
					patientTreatmentHistoryDetails.setYear(dto.getYear());
					patientTreatmentHistoryDetails.setRemark(dto.getRemark());
					patientTreatmentHistoryDetails.setSurgenName(dto.getSurgenName());
					patientTreatmentHistoryDetails.setTreatementName(dto.getTreatementName());
					patientTreatmentHistoryDetails.setTreatDate(CommonDateUtils.getDate(dto.getTreatDate(),"dd-MM-yyyy HH:mm:ss"));
					patientTreatmentHistoryDetails.setRoleId(dto.getRoleId());
					patientTreatmentHistoryDetails.setIsEnterInErrorStatus(dto.getIsEnterInErrorStatus());
					patientTreatmentHistoryDetails.setIsReviewedFlag(dto.getIsReviewedFlag() == '\0' ? NO:
								dto.getIsReviewedFlag()
							);
					patientTreatmentHistoryDetails.setCreatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					patientTreatmentHistoryDetails.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
					patientTreatmentHistoryDetails.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
					patientTreatmentHistoryDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					patientTreatmentHistoryDetails.setStatus(coversheetTemplateMasterDto.getStatus());
					patientTreatmentHistoryDetails.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					patientTreatmentHistoryDetails.setUnitId(coversheetTemplateMasterDto.getUnitId());

					PatientTreatmentHistoryDetails historyId=update(patientTreatmentHistoryDetails);
					
				}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}
}
