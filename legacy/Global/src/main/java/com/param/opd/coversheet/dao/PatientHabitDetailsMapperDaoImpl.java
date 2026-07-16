package com.param.opd.coversheet.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.ComplaintAppointmentDto;
import com.param.opd.coversheet.dto.ImmunizationHistoryMapperDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.model.ComplaintAppointmentMapper;
import com.param.opd.coversheet.model.PatientFamilyHistory;
import com.param.opd.coversheet.model.PatientHabitDetails;
import com.param.opd.coversheet.model.PatientHabitDetailsId;
import com.param.opd.coversheet.service.IPatientHabitDetailsMapperService;
import com.param.opd.model.NewCovDoctorNotes;
import com.param.utility.TimeZoneComponent;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked" })
public class PatientHabitDetailsMapperDaoImpl extends GenericDao<PatientHabitDetails, Integer> implements IPatientHabitDetailsMapperDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(PatientHabitDetailsMapperDaoImpl.class);


	
	public PatientHabitDetailsMapperDaoImpl() {
		super(PatientHabitDetails.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private IPatientHabitDetailsMapperService iPatientHabitDetailsMapperService;
	
	@Override
	@Transactional
	public Response savePatientHabitDetailsMapper(PatientHabitDetailsDto patientHabitDetailsDto)throws ApplicationException {
		try
		{
			for(int i=0;i<patientHabitDetailsDto.getListOfPatientHabitDetailsDto().size();i++)
			{
				if(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPatientHabitDetailsId()==null){
					if(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPatientHabitDetailsId() != null  && 	
							   patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPatientHabitDetailsId()>0 )
							{
									PatientHabitDetails patientHabitDetails = findById(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPatientHabitDetailsId());
									patientHabitDetails.setIsReviewedFlag(YES);
									iPatientHabitDetailsMapperService.updateSaveNurseReviewedFlag(patientHabitDetails);
								}
							
							
							PatientHabitDetails patientHabitDetails = new PatientHabitDetails();
							
							patientHabitDetails.setPatientId(patientHabitDetailsDto.getPatientId());
							patientHabitDetails.setHabitId(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getHabitId());
							patientHabitDetails.setStatus(patientHabitDetailsDto.getStatus());
							
							patientHabitDetails.setCigarettesPerDay(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getCigarettesPerDay());
							patientHabitDetails.setFrequency(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getFrequency());
							patientHabitDetails.setGmsPerDay(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getGmsPerDay());
							patientHabitDetails.setLeftWhen(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getLeftWhen());
							patientHabitDetails.setDurationOfLeftWhen(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getDurationOfLeftWhen());
							patientHabitDetails.setDrug(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getDrug());
							/*patientHabitDetails.setExerciseWhen(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getExerciseWhen());*/
							
							patientHabitDetails.setMlsPerDay(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getMlsPerDay());
							patientHabitDetails.setPackYear(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPackYear());
							patientHabitDetails.setRemark(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getRemark());
							patientHabitDetails.setTypeOfExercise(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getTypeOfExercise());
							patientHabitDetails.setYearsSmoked(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getYearsSmoked());
							patientHabitDetails.setYearsUsed(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getYearsUsed());
							patientHabitDetails.setHabitTypeId(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getHabitTypeId());
							patientHabitDetails.setCreatedDate(CommonDateUtils.getDate(patientHabitDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
							patientHabitDetails.setCreatedBy(patientHabitDetailsDto.getCreatedBy());
							patientHabitDetails.setUpdatedBy(patientHabitDetailsDto.getUpdatedBy());
							patientHabitDetails.setUpdatedDate(CommonDateUtils.getDate(patientHabitDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
							patientHabitDetails.setOrganizationId(patientHabitDetailsDto.getOrganizationId());
							patientHabitDetails.setUnitId(patientHabitDetailsDto.getUnitId());
							
							if(patientHabitDetailsDto.getEncounterId()!=null)
								patientHabitDetails.setEncounterId(patientHabitDetailsDto.getEncounterId());
							else
								patientHabitDetails.setAdmissionId(patientHabitDetailsDto.getAdmissionId());
								
							patientHabitDetails.setRoleId(patientHabitDetailsDto.getRoleId());
							patientHabitDetails.setIsReviewedFlag(
									patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getIsReviewedFlag() == '\0' ? NO :
									patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getIsReviewedFlag()
								);
						
								patientHabitDetails.setIsEnterInErrorStatus(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getIsEnterInErrorStatus());
							
							
							PatientHabitDetails historyId=save(patientHabitDetails);
				}else{
					if(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPatientHabitDetailsId() != null  && 	
							   patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPatientHabitDetailsId()>0 )
							{
									PatientHabitDetails patientHabitDetails = findById(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPatientHabitDetailsId());
									patientHabitDetails.setIsReviewedFlag(YES);
									iPatientHabitDetailsMapperService.updateSaveNurseReviewedFlag(patientHabitDetails);
								}
							
							
							PatientHabitDetails patientHabitDetails = findById(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPatientHabitDetailsId());
							
							patientHabitDetails.setPatientId(patientHabitDetailsDto.getPatientId());
							patientHabitDetails.setHabitId(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getHabitId());
							patientHabitDetails.setStatus(patientHabitDetailsDto.getStatus());
							
							patientHabitDetails.setCigarettesPerDay(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getCigarettesPerDay());
							patientHabitDetails.setFrequency(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getFrequency());
							patientHabitDetails.setGmsPerDay(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getGmsPerDay());
							patientHabitDetails.setLeftWhen(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getLeftWhen());
							patientHabitDetails.setDurationOfLeftWhen(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getDurationOfLeftWhen());
							patientHabitDetails.setDrug(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getDrug());
							/*patientHabitDetails.setExerciseWhen(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getExerciseWhen());*/
							
							patientHabitDetails.setMlsPerDay(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getMlsPerDay());
							patientHabitDetails.setPackYear(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPackYear());
							patientHabitDetails.setRemark(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getRemark());
							patientHabitDetails.setTypeOfExercise(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getTypeOfExercise());
							patientHabitDetails.setYearsSmoked(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getYearsSmoked());
							patientHabitDetails.setYearsUsed(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getYearsUsed());
							patientHabitDetails.setHabitTypeId(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getHabitTypeId());
							patientHabitDetails.setCreatedDate(CommonDateUtils.getDate(patientHabitDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
							patientHabitDetails.setCreatedBy(patientHabitDetailsDto.getCreatedBy());
							patientHabitDetails.setUpdatedBy(patientHabitDetailsDto.getUpdatedBy());
							patientHabitDetails.setUpdatedDate(CommonDateUtils.getDate(patientHabitDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
							patientHabitDetails.setOrganizationId(patientHabitDetailsDto.getOrganizationId());
							patientHabitDetails.setUnitId(patientHabitDetailsDto.getUnitId());
							
							if(patientHabitDetailsDto.getEncounterId()!=null)
								patientHabitDetails.setEncounterId(patientHabitDetailsDto.getEncounterId());
							else
								patientHabitDetails.setAdmissionId(patientHabitDetailsDto.getAdmissionId());
								
							patientHabitDetails.setRoleId(patientHabitDetailsDto.getRoleId());
							patientHabitDetails.setIsReviewedFlag(
									patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getIsReviewedFlag() == '\0' ? NO :
									patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getIsReviewedFlag()
								);
						
								patientHabitDetails.setIsEnterInErrorStatus(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getIsEnterInErrorStatus());
							
							
							PatientHabitDetails historyId=update(patientHabitDetails);
				}
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getListOfPatientHabitDetailsMapper(PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<PatientHabitDetailsDto> listPatientHabitDetailsDto = statlessSession.getNamedQuery("GET_PATIENT_HABIT_DETAILS_LIST")
					.setParameter("patientId", patientHabitDetailsDto.getPatientId())
					.setParameter("unitId", patientHabitDetailsDto.getUnitId())
					.setParameter("organizationId", patientHabitDetailsDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientHabitDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfPatientHabitDetailsMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	@Transactional
	public Response updatePatientHabitDetailsMapper(
			PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<patientHabitDetailsDto.getListOfPatientHabitDetailsDto().size();i++)
			{
				
				
//					StatelessSession statlessSession = sessionFactory.openStatelessSession();
//					int updateCount= statlessSession.getNamedQuery("UPDATE_OLD_HABITS_BYPATIENT")
//							.setParameter("patientId", patientHabitDetailsDto.getPatientId())
//							.setParameter("habitId", patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getHabitId())
//							.setParameter("encounterId", patientHabitDetailsDto.getEncounterId())
//							.setParameter("roleId", patientHabitDetailsDto.getRoleId())
//							.executeUpdate();
//					statlessSession.close();
				String query=sessionFactory.getCurrentSession().getNamedQuery("UPDATE_OLD_HABITS_BYPATIENT").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(patientHabitDetailsDto.getEncounterId()!=null){
					subQuery.append(" AND pHabitDetail.encounterId = :encounterId");
				}
				if(patientHabitDetailsDto.getAdmissionId()!=null){
					subQuery.append(" AND pHabitDetail.admissionId = :admissionId");
				}
				query=query.concat(subQuery.toString());
				Query createQuery=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientHabitDetailsDto.getPatientId())
						.setParameter("habitId", patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getHabitId())
						.setParameter("roleId", patientHabitDetailsDto.getRoleId());

				if (patientHabitDetailsDto.getEncounterId() != null) {
					createQuery.setParameter("encounterId", patientHabitDetailsDto.getEncounterId());
				}
				if (patientHabitDetailsDto.getAdmissionId() != null) {
					createQuery.setParameter("admissionId", patientHabitDetailsDto.getAdmissionId());
				}

				int updateCount=createQuery.executeUpdate();
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getOldPatientHabitDetails(
			PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		try
		{
			List<PatientHabitDetailsDto> listPatientHabitDetailsDto;
			
			/*if(patientHabitDetailsDto.getRoleId() == 3)
			{
				StatelessSession statlessSession = sessionFactory.openStatelessSession();
				listPatientHabitDetailsDto = statlessSession.getNamedQuery("GET_PATIENT_HABIT_DETAILS_LIST_BY_ROLE_ID")
						.setParameter("patientId", patientHabitDetailsDto.getPatientId())
						.setParameter("unitId", patientHabitDetailsDto.getUnitId())
						.setParameter("organizationId", patientHabitDetailsDto.getOrganizationId())
						.setParameter("encounterId", patientHabitDetailsDto.getEncounterId())
						.setParameter("roleId", patientHabitDetailsDto.getRoleId())
						.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
				statlessSession.close();
			}
			else
			{*/
//				StatelessSession statlessSession = sessionFactory.openStatelessSession();
//				listPatientHabitDetailsDto = statlessSession.getNamedQuery("GET_PATIENT_HABIT_DETAILS_LIST")
//						.setParameter("patientId", patientHabitDetailsDto.getPatientId())
//						.setParameter("unitId", patientHabitDetailsDto.getUnitId())
//						.setParameter("organizationId", patientHabitDetailsDto.getOrganizationId())
//						.setParameter("encounterId", patientHabitDetailsDto.getEncounterId())
//						.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
//				statlessSession.close();
		/*	}
			
			*/
					String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_HABIT_DETAILS_LIST").getQueryString();
					StringBuffer subQuery=new StringBuffer();
					if(patientHabitDetailsDto.getEncounterId()!=null){
//						subQuery.append(" AND (patHab.encounterId IS NULL OR patHab.encounterId!="+patientHabitDetailsDto.getEncounterId()+")");
						subQuery.append(" ORDER BY patHab.patientHabitDetailsId ASC ");
					}
					if(patientHabitDetailsDto.getAdmissionId()!=null){
//						subQuery.append(" AND (patHab.admissionId IS NULL OR patHab.admissionId!="+patientHabitDetailsDto.getAdmissionId()+")");
						subQuery.append(" ORDER BY patHab.patientHabitDetailsId ASC ");
					}
					query=query.concat(subQuery.toString());
					listPatientHabitDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
							.setParameter("patientId", patientHabitDetailsDto.getPatientId())
							.setParameter("unitId", patientHabitDetailsDto.getUnitId())
							.setParameter("organizationId", patientHabitDetailsDto.getOrganizationId())
							.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
							
					return new Response(SUCCESS, SUCCESS_CODE, null, listPatientHabitDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getOldPatientHabitDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCurrentPatientHabitDetails(
			PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		try
		{
			List<PatientHabitDetailsDto> listPatientHabitDetailsDto;
			
/*			if(patientHabitDetailsDto.getRoleId() == 3)
			{
//				StatelessSession statlessSession = sessionFactory.openStatelessSession();
//				listPatientHabitDetailsDto = statlessSession.getNamedQuery("GET_CURRENT_PATIENT_HABIT_DETAILS_LIST_BY_ROLE_ID")
//						.setParameter("patientId", patientHabitDetailsDto.getPatientId())
//						.setParameter("unitId", patientHabitDetailsDto.getUnitId())
//						.setParameter("organizationId", patientHabitDetailsDto.getOrganizationId())
//						.setParameter("encounterId", patientHabitDetailsDto.getEncounterId())
//						.setParameter("roleId", patientHabitDetailsDto.getRoleId())
//						.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
//				statlessSession.close();
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_HABIT_DETAILS_LIST_BY_ROLE_ID").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(patientHabitDetailsDto.getEncounterId()!=null){
					subQuery.append(" AND patHab.encounterId="+patientHabitDetailsDto.getEncounterId());
					subQuery.append("  group by patHab.patientId,patHab.patientHabitDetailsId ");
					subQuery.append(" ORDER BY patHab.patientHabitDetailsId DESC ");
					
				}
				if(patientHabitDetailsDto.getAdmissionId()!=null){
					subQuery.append(" AND patHab.admissionId="+patientHabitDetailsDto.getAdmissionId());
					subQuery.append("  group by patHab.patientId,patHab.patientHabitDetailsId ");
					subQuery.append(" ORDER BY patHab.patientHabitDetailsId DESC ");
				}
				
				query=query.concat(subQuery.toString());
				listPatientHabitDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientHabitDetailsDto.getPatientId())
						.setParameter("unitId", patientHabitDetailsDto.getUnitId())
						.setParameter("organizationId", patientHabitDetailsDto.getOrganizationId())
						.setParameter("roleId", patientHabitDetailsDto.getRoleId())
//						.setMaxResults(1)
						.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
						
				return new Response(SUCCESS, SUCCESS_CODE, null, listPatientHabitDetailsDto, null);
			}*/
			/*else
			{*/
//				StatelessSession statlessSession = sessionFactory.openStatelessSession();
//				listPatientHabitDetailsDto = statlessSession.getNamedQuery("GET_CURRENT_PATIENT_HABIT_DETAILS_LIST")
//						.setParameter("patientId", patientHabitDetailsDto.getPatientId())
//						.setParameter("unitId", patientHabitDetailsDto.getUnitId())
//						.setParameter("organizationId", patientHabitDetailsDto.getOrganizationId())
//						.setParameter("encounterId", patientHabitDetailsDto.getEncounterId())
//						.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
//				statlessSession.close();
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_HABIT_DETAILS_LIST").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(patientHabitDetailsDto.getEncounterId()!=null){
//					subQuery.append(" AND patHab.encounterId="+patientHabitDetailsDto.getEncounterId());
					subQuery.append(" ORDER BY patHab.patientHabitDetailsId ASC ");
				}
				if(patientHabitDetailsDto.getAdmissionId()!=null){
//					subQuery.append(" AND patHab.admissionId="+patientHabitDetailsDto.getAdmissionId());
					subQuery.append(" ORDER BY patHab.patientHabitDetailsId ASC ");
				}
				
				query=query.concat(subQuery.toString());
				listPatientHabitDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientHabitDetailsDto.getPatientId())
//						.setParameter("unitId", patientHabitDetailsDto.getUnitId())
						.setParameter("organizationId", patientHabitDetailsDto.getOrganizationId())
//						.setMaxResults(1)
						.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
//				}
			
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientHabitDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentPatientHabitDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updatePatientHabitDetails(
			PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<patientHabitDetailsDto.getListOfPatientHabitDetailsDto().size();i++)
			{
				Session s = sessionFactory.getCurrentSession();
				PatientHabitDetails patientHabitDetails =  findById(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPatientHabitDetailsId());
				
				if(patientHabitDetailsDto.getRoleId() != null 
						&& patientHabitDetailsDto.getRoleId() == 2 
						&& patientHabitDetails.getRoleId() != 2 
						&& patientHabitDetails.getIsReviewedFlag() == 'N'
						)
				{
					iPatientHabitDetailsMapperService.savePatientHabitDetailsMapper(patientHabitDetailsDto);
				}
				
				patientHabitDetails.setCigarettesPerDay(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getCigarettesPerDay());
				patientHabitDetails.setFrequency(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getFrequency());
				patientHabitDetails.setGmsPerDay(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getGmsPerDay());
				patientHabitDetails.setLeftWhen(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getLeftWhen());
				patientHabitDetails.setDurationOfLeftWhen(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getDurationOfLeftWhen());
				patientHabitDetails.setDrug(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getDrug());
				/*patientHabitDetails.setExerciseWhen(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getExerciseWhen());*/
				
				patientHabitDetails.setMlsPerDay(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getMlsPerDay());
				patientHabitDetails.setPackYear(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPackYear());
				patientHabitDetails.setRemark(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getRemark());
				patientHabitDetails.setTypeOfExercise(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getTypeOfExercise());
				patientHabitDetails.setYearsSmoked(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getYearsSmoked());
				patientHabitDetails.setYearsUsed(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getYearsUsed());
				patientHabitDetails.setHabitTypeId(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getHabitTypeId());
				patientHabitDetails.setCreatedDate(CommonDateUtils.getDate(patientHabitDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
				patientHabitDetails.setCreatedBy(patientHabitDetailsDto.getCreatedBy());
				patientHabitDetails.setUpdatedBy(patientHabitDetailsDto.getUpdatedBy());
				patientHabitDetails.setUpdatedDate(CommonDateUtils.getDate(patientHabitDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
				patientHabitDetails.setOrganizationId(patientHabitDetailsDto.getOrganizationId());
				patientHabitDetails.setUnitId(patientHabitDetailsDto.getUnitId());
				
				if(patientHabitDetailsDto.getEncounterId()!=null)
					patientHabitDetails.setEncounterId(patientHabitDetailsDto.getEncounterId());
				else
					patientHabitDetails.setAdmissionId(patientHabitDetailsDto.getAdmissionId());
				
				patientHabitDetails.setRoleId(patientHabitDetails.getRoleId());
				patientHabitDetails.setIsReviewedFlag(patientHabitDetailsDto.getRoleId() == 2 
						&& patientHabitDetails.getRoleId() == 3
						? YES : patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getIsReviewedFlag());
			
				patientHabitDetails.setIsEnterInErrorStatus(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getIsEnterInErrorStatus());
				
				
				PatientHabitDetails historyId=save(patientHabitDetails);
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updatePatientHabitDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateSaveNurseReviewedFlag(
			PatientHabitDetails patientHabitDetails)
			throws ApplicationException {
		try
		{
			save(patientHabitDetails);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("updateSaveNurseReviewedFlag failed", he);
		} catch (Exception e) {
		logger.error("updateSaveNurseReviewedFlag failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateStatusEnterInError(
			PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<patientHabitDetailsDto.getListOfPatientHabitDetailsDto().size();i++)
			{
				Session s = sessionFactory.getCurrentSession();
				PatientHabitDetails patientHabit =  findById(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPatientHabitDetailsId());
				
				patientHabit.setIsEnterInErrorStatus(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getIsEnterInErrorStatus());
				patientHabit.setIsReviewedFlag(patientHabit.getIsReviewedFlag() == 'N' && 
						patientHabit.getRoleId() == 3 && patientHabitDetailsDto.getRoleId() == 2 ? YES : patientHabit.getIsReviewedFlag());
				
				if(patientHabitDetailsDto.getRoleId() != null && patientHabitDetailsDto.getRoleId() == 2 && patientHabitDetailsDto.getRoleId() == 3)
				{
					
					
					
					PatientHabitDetails patientHabitDetails = new PatientHabitDetails();
					patientHabitDetails.setPatientId(patientHabitDetailsDto.getPatientId());
					patientHabitDetails.setHabitId(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getHabitId());
					patientHabitDetails.setStatus(patientHabitDetailsDto.getStatus());
					patientHabitDetails.setCigarettesPerDay(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getCigarettesPerDay());
					patientHabitDetails.setFrequency(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getFrequency());
					patientHabitDetails.setGmsPerDay(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getGmsPerDay());
					patientHabitDetails.setLeftWhen(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getLeftWhen());
					patientHabitDetails.setDurationOfLeftWhen(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getDurationOfLeftWhen());
					patientHabitDetails.setDrug(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getDrug());
					/*patientHabitDetails.setExerciseWhen(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getExerciseWhen());*/
					
					patientHabitDetails.setMlsPerDay(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getMlsPerDay());
					patientHabitDetails.setPackYear(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getPackYear());
					patientHabitDetails.setRemark(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getRemark());
					patientHabitDetails.setTypeOfExercise(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getTypeOfExercise());
					patientHabitDetails.setYearsSmoked(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getYearsSmoked());
					patientHabitDetails.setYearsUsed(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getYearsUsed());
					patientHabitDetails.setHabitTypeId(patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getHabitTypeId());
					patientHabitDetails.setCreatedDate(CommonDateUtils.getDate(patientHabitDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
					patientHabitDetails.setCreatedBy(patientHabitDetailsDto.getCreatedBy());
					patientHabitDetails.setUpdatedBy(patientHabitDetailsDto.getUpdatedBy());
					patientHabitDetails.setUpdatedDate(CommonDateUtils.getDate(patientHabitDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
					patientHabitDetails.setOrganizationId(patientHabitDetailsDto.getOrganizationId());
					patientHabitDetails.setUnitId(patientHabitDetailsDto.getUnitId());
					
					if(patientHabitDetailsDto.getEncounterId()!=null)
						patientHabitDetails.setEncounterId(patientHabitDetailsDto.getEncounterId());
					else
						patientHabitDetails.setAdmissionId(patientHabitDetailsDto.getAdmissionId());
					patientHabitDetails.setRoleId(patientHabitDetailsDto.getRoleId());
					patientHabitDetails.setIsReviewedFlag(
							patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getIsReviewedFlag() == '\0' ? NO :
							patientHabitDetailsDto.getListOfPatientHabitDetailsDto().get(i).getIsReviewedFlag()
						);
				
						patientHabitDetails.setIsEnterInErrorStatus(NO);
					
					
					PatientHabitDetails historyId=save(patientHabitDetails);
					
				}

				
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateStatusEnterInError failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientHabitsForBookView(PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		try
		{
			List<PatientHabitDetailsDto> listPatientHabitDetailsDto;

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_HABITS_FOR_BOOK_VIEW").getQueryString();
				listPatientHabitDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientHabitDetailsDto.getPatientId())
						.setParameter("organizationId", patientHabitDetailsDto.getOrganizationId())
						.setParameter("fromDate", patientHabitDetailsDto.getFromDate())
						.setParameter("toDate", patientHabitDetailsDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
//				}
			
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientHabitDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientHabitsForBookView failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveCurrentPatientHabitDetails(List<PatientHabitDetailsDto> list, CoversheetTemplateMasterDto resDto)
			throws ApplicationException {
		try{
			
			for (PatientHabitDetailsDto dto : list){
				if(dto.getCovTemplateId() == null){
								PatientHabitDetails patientHabitDetails = new PatientHabitDetails();
//								patientHabitDetails.setPatientId(resDto.getPatientId());
								patientHabitDetails.setHabitId(dto.getHabitId());
								patientHabitDetails.setStatus(resDto.getStatus());
								patientHabitDetails.setCigarettesPerDay(dto.getCigarettesPerDay());
								patientHabitDetails.setFrequency(dto.getFrequency());
								patientHabitDetails.setGmsPerDay(dto.getGmsPerDay());
								patientHabitDetails.setLeftWhen(dto.getLeftWhen());
								patientHabitDetails.setDurationOfLeftWhen(dto.getDurationOfLeftWhen());
								patientHabitDetails.setDrug(dto.getDrug());
								patientHabitDetails.setMlsPerDay(dto.getMlsPerDay());
								patientHabitDetails.setPackYear(dto.getPackYear());
								patientHabitDetails.setRemark(dto.getRemark());
								patientHabitDetails.setTypeOfExercise(dto.getTypeOfExercise());
								patientHabitDetails.setYearsSmoked(dto.getYearsSmoked());
								patientHabitDetails.setYearsUsed(dto.getYearsUsed());
								patientHabitDetails.setHabitTypeId(dto.getHabitTypeId());
								patientHabitDetails.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
								patientHabitDetails.setCreatedBy(resDto.getCreatedBy());
								patientHabitDetails.setUpdatedBy(resDto.getUpdatedBy());
								patientHabitDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
								patientHabitDetails.setOrganizationId(resDto.getOrganizationId());
								patientHabitDetails.setUnitId(resDto.getUnitId());
								patientHabitDetails.setCovTemplateId(resDto.getCovTemplateId());
//								if(resDto.getEncounterId()!=null)
//									patientHabitDetails.setEncounterId(resDto.getEncounterId());
//								else
//									patientHabitDetails.setAdmissionId(resDto.getAdmissionId());
//									
								patientHabitDetails.setRoleId(dto.getRoleId());
								patientHabitDetails.setIsReviewedFlag(dto.getIsReviewedFlag() == '\0' ? NO :dto.getIsReviewedFlag());
								patientHabitDetails.setIsEnterInErrorStatus(dto.getIsEnterInErrorStatus());
								save(patientHabitDetails);
					
					
				}/*else {
					PatientHabitDetails patientHabitDetails = findById(dto.getCovTemplateId());
//					patientHabitDetails.setPatientId(resDto.getPatientId());
					patientHabitDetails.setHabitId(dto.getHabitId());
					patientHabitDetails.setStatus(resDto.getStatus());
					patientHabitDetails.setCigarettesPerDay(dto.getCigarettesPerDay());
					patientHabitDetails.setFrequency(dto.getFrequency());
					patientHabitDetails.setGmsPerDay(dto.getGmsPerDay());
					patientHabitDetails.setLeftWhen(dto.getLeftWhen());
					patientHabitDetails.setDurationOfLeftWhen(dto.getDurationOfLeftWhen());
					patientHabitDetails.setDrug(dto.getDrug());
					patientHabitDetails.setMlsPerDay(dto.getMlsPerDay());
					patientHabitDetails.setPackYear(dto.getPackYear());
					patientHabitDetails.setRemark(dto.getRemark());
					patientHabitDetails.setTypeOfExercise(dto.getTypeOfExercise());
					patientHabitDetails.setYearsSmoked(dto.getYearsSmoked());
					patientHabitDetails.setYearsUsed(dto.getYearsUsed());
					patientHabitDetails.setHabitTypeId(dto.getHabitTypeId());;
					patientHabitDetails.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientHabitDetails.setCreatedBy(resDto.getCreatedBy());
					patientHabitDetails.setUpdatedBy(resDto.getUpdatedBy());
					patientHabitDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientHabitDetails.setOrganizationId(resDto.getOrganizationId());
					patientHabitDetails.setUnitId(resDto.getUnitId());
					patientHabitDetails.setCovTemplateId(resDto.getCovTemplateId());
//					if(resDto.getEncounterId()!=null)
//						patientHabitDetails.setEncounterId(resDto.getEncounterId());
//					else
//						patientHabitDetails.setAdmissionId(resDto.getAdmissionId());
//						
					patientHabitDetails.setRoleId(dto.getRoleId());
					patientHabitDetails.setIsReviewedFlag(dto.getIsReviewedFlag() == '\0' ? NO :dto.getIsReviewedFlag());
					patientHabitDetails.setIsEnterInErrorStatus(dto.getIsEnterInErrorStatus());
					PatientHabitDetails historyId=update(patientHabitDetails);
		
		
				}*/
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCovTempCurrentPatientHabitDetails(List<PatientHabitDetailsDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
try{
			
			for (PatientHabitDetailsDto dto : list){
				if(dto.getCovTemplateId() != null){
							
					PatientHabitDetails patientHabitDetails = findById(dto.getCovTemplateId());
//					patientHabitDetails.setPatientId(resDto.getPatientId());
					patientHabitDetails.setHabitId(dto.getHabitId());
					patientHabitDetails.setStatus(coversheetTemplateMasterDto.getStatus());
					patientHabitDetails.setCigarettesPerDay(dto.getCigarettesPerDay());
					patientHabitDetails.setFrequency(dto.getFrequency());
					patientHabitDetails.setGmsPerDay(dto.getGmsPerDay());
					patientHabitDetails.setLeftWhen(dto.getLeftWhen());
					patientHabitDetails.setDurationOfLeftWhen(dto.getDurationOfLeftWhen());
					patientHabitDetails.setDrug(dto.getDrug());
					patientHabitDetails.setMlsPerDay(dto.getMlsPerDay());
					patientHabitDetails.setPackYear(dto.getPackYear());
					patientHabitDetails.setRemark(dto.getRemark());
					patientHabitDetails.setTypeOfExercise(dto.getTypeOfExercise());
					patientHabitDetails.setYearsSmoked(dto.getYearsSmoked());
					patientHabitDetails.setYearsUsed(dto.getYearsUsed());
					patientHabitDetails.setHabitTypeId(dto.getHabitTypeId());
					patientHabitDetails.setCreatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					patientHabitDetails.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
					patientHabitDetails.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
					patientHabitDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					patientHabitDetails.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					patientHabitDetails.setUnitId(coversheetTemplateMasterDto.getUnitId());
					patientHabitDetails.setCovTemplateId(coversheetTemplateMasterDto.getCovTemplateId());
//					if(resDto.getEncounterId()!=null)
//						patientHabitDetails.setEncounterId(resDto.getEncounterId());
//					else
//						patientHabitDetails.setAdmissionId(resDto.getAdmissionId());
//						
					patientHabitDetails.setRoleId(dto.getRoleId());
					patientHabitDetails.setIsReviewedFlag(dto.getIsReviewedFlag() == '\0' ? NO :dto.getIsReviewedFlag());
					patientHabitDetails.setIsEnterInErrorStatus(dto.getIsEnterInErrorStatus());
					PatientHabitDetails historyId=update(patientHabitDetails);
		
		
				}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getPatientHabitsForNewCov(PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		try
		{
			List<PatientHabitDetailsDto> listPatientHabitDetailsDto;

			String query = "select " 
					+ "	tphd.habit_id as \"habitId\", "
					+ "	mhm.habit_desc as \"habitDesc\", "
					+ "	mhtm.habit_type_desc as \"habitTypeDesc\", " 
					+ "	tphd.admission_id as \"admissionId\", "
					+ "	tphd.patient_habit_details_id as \"patientHabitDetailsId\", "
					+ "	tphd.remark as \"remark\", " 
					+ "	tphd.habit_type_id as \"habitTypeId\", "
					+ "	tphd.patient_id as \"patientId\", " 
					+ "	tphd.cigarettes_per_day as \"cigarettesPerDay\", "
					+ "	tphd.years_smoked as \"yearsSmoked\", " 
					+ "	tphd.mls_per_day as \"mlsPerDay\", "
					+ "	tphd.gms_per_day as \"gmsPerDay\", " 
					+ "	tphd.years_used as \"yearsUsed\", "
					+ "	tphd.frequency as \"frequency\", " 
					+ "	tphd.pack_year as \"packYear\", "
					+ "	tphd.type_of_exercise as \"typeOfExercise\", " 
					+ "	tphd.status as \"status\", "
					+ "	tphd.unit_id as \"unitId\", " 
					+ "	tphd.organization_id as \"organizationId\", "
					+ "	tphd.left_when as \"leftWhen\", " 
					+ " 	tphd.drug as \"drug\", "
					+ "	tphd.is_enter_in_error as \"isEnterInErrorStatus\", " 
					+ "	tphd.encounter_id as \"encounterId\", "
					+ "	tphd.role_id as \"roleId\", " 
					+ " 	tphd.cov_template_id as \"covTemplateId\" " 
					+ " from "
					+ "	patient.t_patient_habit_details tphd " 
					+ " inner join emr.m_habit_master as mhm on "
					+ "	mhm.habit_id = tphd.habit_id " 
					+ " inner join emr.m_habit_type_master mhtm on "
					+ "	mhtm.habit_type_id = tphd.habit_type_id  " 
					+ " where " 
					+ "	tphd.patient_id = :patientId"
					+ "	and tphd.unit_id = :unitId" 
					+ "	and tphd.status = 'A'" 
					+ "	and tphd.organization_id = :organizationId";
			listPatientHabitDetailsDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", patientHabitDetailsDto.getPatientId())
					.setParameter("unitId", patientHabitDetailsDto.getUnitId())
					.setParameter("organizationId", patientHabitDetailsDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
//				}

			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientHabitDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientHabitsForNewCov failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getSearchedHabits(PatientHabitDetailsDto patientHabitDetailsDto) throws ApplicationException {

		try
		{
			List<PatientHabitDetailsDto> listPatientHabitDetailsDto;

			String query = "select " + 
					"	mst.habit_id as \"habitId\", " + 
					"	mst.habit_desc as \"habitDesc\"  " + 
					"from " + 
					"	emr.m_habit_master mst " + 
					"where lower(mst.habit_desc) like '%"+patientHabitDetailsDto.getSearchKeyword()+"%' ";
			listPatientHabitDetailsDto = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(PatientHabitDetailsDto.class)).list();
//				}

			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientHabitDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getSearchedHabits failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response removePatientHabits(PatientHabitDetailsDto patientHabitDetailsDto) throws ApplicationException {
		try {
			PatientHabitDetails patHabit= findById(patientHabitDetailsDto.getPatientHabitDetailsId());
			patHabit.setStatus('I');
			patHabit.setUpdatedBy(patientHabitDetailsDto.getUpdatedBy());
			patHabit.setUpdatedDate(GlobalCommonDateUtils.getDate(patientHabitDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(patHabit);
	
	return new Response(SUCCESS, SUCCESS_CODE, "Cancel successfully", null, null);
	
	} catch (HibernateException he) {
	logger.error("removePatientHabits failed", he);
	} catch (Exception e) {
	logger.error("removePatientHabits failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response savePatientHabitsDetails(PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		try {
			if(patientHabitDetailsDto.getPatientHabitDetailsId() == null) {
				PatientHabitDetails patientHabitDetails = new PatientHabitDetails();
				
				patientHabitDetails.setPatientId(patientHabitDetailsDto.getPatientId());
				patientHabitDetails.setHabitId(patientHabitDetailsDto.getHabitId());
				patientHabitDetails.setStatus(patientHabitDetailsDto.getStatus());
				
				patientHabitDetails.setCigarettesPerDay(patientHabitDetailsDto.getCigarettesPerDay());
				patientHabitDetails.setFrequency(patientHabitDetailsDto.getFrequency());
				patientHabitDetails.setGmsPerDay(patientHabitDetailsDto.getGmsPerDay());
				patientHabitDetails.setLeftWhen(patientHabitDetailsDto.getLeftWhen());
				patientHabitDetails.setDurationOfLeftWhen(patientHabitDetailsDto.getDurationOfLeftWhen());
				patientHabitDetails.setDrug(patientHabitDetailsDto.getDrug());
				patientHabitDetails.setMlsPerDay(patientHabitDetailsDto.getMlsPerDay());
				patientHabitDetails.setPackYear(patientHabitDetailsDto.getPackYear());
				patientHabitDetails.setRemark(patientHabitDetailsDto.getRemark());
				patientHabitDetails.setTypeOfExercise(patientHabitDetailsDto.getTypeOfExercise());
				patientHabitDetails.setYearsSmoked(patientHabitDetailsDto.getYearsSmoked());
				patientHabitDetails.setYearsUsed(patientHabitDetailsDto.getYearsUsed());
				patientHabitDetails.setHabitTypeId(patientHabitDetailsDto.getHabitTypeId());
				patientHabitDetails.setCreatedDate(CommonDateUtils.getDate(patientHabitDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
				patientHabitDetails.setCreatedBy(patientHabitDetailsDto.getCreatedBy());
				patientHabitDetails.setUpdatedBy(patientHabitDetailsDto.getUpdatedBy());
				patientHabitDetails.setUpdatedDate(CommonDateUtils.getDate(patientHabitDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
				patientHabitDetails.setOrganizationId(patientHabitDetailsDto.getOrganizationId());
				patientHabitDetails.setUnitId(patientHabitDetailsDto.getUnitId());
				
				if(patientHabitDetailsDto.getEncounterId()!=null)
					patientHabitDetails.setEncounterId(patientHabitDetailsDto.getEncounterId());
				else
					patientHabitDetails.setAdmissionId(patientHabitDetailsDto.getAdmissionId());
					
				patientHabitDetails.setRoleId(patientHabitDetailsDto.getRoleId());
				patientHabitDetails.setIsReviewedFlag(
						patientHabitDetailsDto.getIsReviewedFlag() == '\0' ? NO :
						patientHabitDetailsDto.getIsReviewedFlag()
					);
			
					patientHabitDetails.setIsEnterInErrorStatus(patientHabitDetailsDto.getIsEnterInErrorStatus());
				
				
				save(patientHabitDetails);
				return new Response(SUCCESS, SUCCESS_CODE,SUCCESS_MESSAGE, null, null);
			}else {
				PatientHabitDetails patientHabitDetails = findById(patientHabitDetailsDto.getPatientHabitDetailsId());
				
				patientHabitDetails.setPatientId(patientHabitDetailsDto.getPatientId());
				patientHabitDetails.setHabitId(patientHabitDetailsDto.getHabitId());
				patientHabitDetails.setStatus(patientHabitDetailsDto.getStatus());
				
				patientHabitDetails.setCigarettesPerDay(patientHabitDetailsDto.getCigarettesPerDay());
				patientHabitDetails.setFrequency(patientHabitDetailsDto.getFrequency());
				patientHabitDetails.setGmsPerDay(patientHabitDetailsDto.getGmsPerDay());
				patientHabitDetails.setLeftWhen(patientHabitDetailsDto.getLeftWhen());
				patientHabitDetails.setDurationOfLeftWhen(patientHabitDetailsDto.getDurationOfLeftWhen());
				patientHabitDetails.setDrug(patientHabitDetailsDto.getDrug());
				patientHabitDetails.setMlsPerDay(patientHabitDetailsDto.getMlsPerDay());
				patientHabitDetails.setPackYear(patientHabitDetailsDto.getPackYear());
				patientHabitDetails.setRemark(patientHabitDetailsDto.getRemark());
				patientHabitDetails.setTypeOfExercise(patientHabitDetailsDto.getTypeOfExercise());
				patientHabitDetails.setYearsSmoked(patientHabitDetailsDto.getYearsSmoked());
				patientHabitDetails.setYearsUsed(patientHabitDetailsDto.getYearsUsed());
				patientHabitDetails.setHabitTypeId(patientHabitDetailsDto.getHabitTypeId());
				patientHabitDetails.setCreatedDate(CommonDateUtils.getDate(patientHabitDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
				patientHabitDetails.setCreatedBy(patientHabitDetailsDto.getCreatedBy());
				patientHabitDetails.setUpdatedBy(patientHabitDetailsDto.getUpdatedBy());
				patientHabitDetails.setUpdatedDate(CommonDateUtils.getDate(patientHabitDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
				patientHabitDetails.setOrganizationId(patientHabitDetailsDto.getOrganizationId());
				patientHabitDetails.setUnitId(patientHabitDetailsDto.getUnitId());
				
				if(patientHabitDetailsDto.getEncounterId()!=null)
					patientHabitDetails.setEncounterId(patientHabitDetailsDto.getEncounterId());
				else
					patientHabitDetails.setAdmissionId(patientHabitDetailsDto.getAdmissionId());
					
				patientHabitDetails.setRoleId(patientHabitDetailsDto.getRoleId());
				patientHabitDetails.setIsReviewedFlag(
						patientHabitDetailsDto.getIsReviewedFlag() == '\0' ? NO :
						patientHabitDetailsDto.getIsReviewedFlag()
					);
			
					patientHabitDetails.setIsEnterInErrorStatus(patientHabitDetailsDto.getIsEnterInErrorStatus());
				
				
				update(patientHabitDetails);
				return new Response(SUCCESS, SUCCESS_CODE,COMMON_UPDATE, null, null);
			}
			
	
	
	
	} catch (HibernateException he) {
	logger.error("savePatientHabitsDetails failed", he);
	} catch (Exception e) {
	logger.error("savePatientHabitsDetails failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response removePatientHabitsDetails(PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		try {
			PatientHabitDetails patientHabitDetails=findById(patientHabitDetailsDto.getPatientHabitDetailsId());
			patientHabitDetails.setStatus(patientHabitDetailsDto.getStatus());
			patientHabitDetails.setUpdatedBy(patientHabitDetailsDto.getUpdatedBy());
			patientHabitDetails.setUpdatedDate(GlobalCommonDateUtils.getDate(patientHabitDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(patientHabitDetails);
		
		return new Response(SUCCESS, COMMON_UPDATE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("removePatientHabitsDetails failed", he);
		} catch (Exception e) {
		logger.error("removePatientHabitsDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updatePatientHabitRemarkForNewCov(PatientHabitDetailsDto patientHabitDetailsDto)
			throws ApplicationException {
		try {
			PatientHabitDetails patientHabitDetails=findById(patientHabitDetailsDto.getPatientHabitDetailsId());
			patientHabitDetails.setRemark(patientHabitDetailsDto.getRemark());
			patientHabitDetails.setUpdatedBy(patientHabitDetailsDto.getUpdatedBy());
			patientHabitDetails.setUpdatedDate(GlobalCommonDateUtils.getDate(patientHabitDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 update(patientHabitDetails);
		
		return new Response(SUCCESS, COMMON_UPDATE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updatePatientHabitRemarkForNewCov failed", he);
		} catch (Exception e) {
		logger.error("updatePatientHabitRemarkForNewCov failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
