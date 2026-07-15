package com.param.opd.coversheet.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.model.ComplaintAppointmentMapper;
import com.param.opd.coversheet.model.PatientFamilyHistory;
import com.param.opd.coversheet.model.PatientPersonalHistoryDetails;
import com.param.opd.coversheet.model.PatientTravelHistoryDetails;
import com.param.opd.coversheet.service.IPatientSurgicalHistoryService;
import com.param.opd.coversheet.service.IPatientTravelHistoryDetailsService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;


import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked" })
public class PatientTravelHistoryDetailsDaoImpl extends GenericDao<PatientTravelHistoryDetails, Integer> implements IPatientTravelHistoryDetailsDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(PatientTravelHistoryDetailsDaoImpl.class);


	public PatientTravelHistoryDetailsDaoImpl() {
		super(PatientTravelHistoryDetails.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private IPatientTravelHistoryDetailsService iPatientTravelHistoryDetailsService;
	@Override
	public Response getListOfPatientTravelHistoryDetails(
			PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto) throws ApplicationException {
			
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<PatientTravelHistoryDetailsDto> listPatientTravelHistoryDetailsDto = statlessSession.getNamedQuery("GET_PATIENT_TRAVEL_HISTORY_DEATILS")
					.setParameter("patientId", patientTravelHistoryDetailsDto.getPatientId())
					.setParameter("unitId", patientTravelHistoryDetailsDto.getUnitId())
					.setParameter("organizationId", patientTravelHistoryDetailsDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(PatientTravelHistoryDetailsDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTravelHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfPatientTravelHistoryDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response savePatientTravelHistoryDetails(PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto) throws ApplicationException {
		try
		{
			for(int i=0;i<patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().size();i++)
			{
				if(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getTravelHistoryDetailsId() != null && 
						patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getTravelHistoryDetailsId() >0)
				{
					PatientTravelHistoryDetails patientTravelHistoryDetails = findById(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getTravelHistoryDetailsId());
					patientTravelHistoryDetails.setIsReviewedFlag(YES);
					patientTravelHistoryDetails.setStatus('I');
					patientTravelHistoryDetails.setDescription(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getDescription());
					if(patientTravelHistoryDetailsDto.getWhenTravel() == "Invalid date" && patientTravelHistoryDetailsDto.getWhenTravel() == ""){
					patientTravelHistoryDetails.setWhenTravel(null);
					}else{
						patientTravelHistoryDetails.setWhenTravel(CommonDateUtils.getDate(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getWhenTravel(), "dd-MM-yyyy"));
					}
					patientTravelHistoryDetails.setRoleId(patientTravelHistoryDetailsDto.getRoleId());
					patientTravelHistoryDetails.setIsReviewedFlag(
							patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsReviewedFlag() == '\0' ? NO :
								patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsReviewedFlag()
							);
					
					patientTravelHistoryDetails.setIsEnterInErrorStatus( patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsEnterInErrorStatus() );
					
					patientTravelHistoryDetails.setCreatedDate(CommonDateUtils.getDate(patientTravelHistoryDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
					if(patientTravelHistoryDetailsDto.getEncounterId()!=null){
						patientTravelHistoryDetails.setEncounterId(patientTravelHistoryDetailsDto.getEncounterId());
					}
					else{
						patientTravelHistoryDetails.setAdmissionId(patientTravelHistoryDetailsDto.getAdmissionId());
					}
					patientTravelHistoryDetails.setIsNoSignificantTravel(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsNoSignificantTravel());
					patientTravelHistoryDetails.setCreatedBy(patientTravelHistoryDetailsDto.getCreatedBy());
					patientTravelHistoryDetails.setUpdatedBy(patientTravelHistoryDetailsDto.getUpdatedBy());
					patientTravelHistoryDetails.setUpdatedDate(CommonDateUtils.getDate(patientTravelHistoryDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
					patientTravelHistoryDetails.setOrganizationId(patientTravelHistoryDetailsDto.getOrganizationId());
					patientTravelHistoryDetails.setUnitId(patientTravelHistoryDetailsDto.getUnitId());
					patientTravelHistoryDetails.setPatientId(patientTravelHistoryDetailsDto.getPatientId());
					patientTravelHistoryDetails.setIsDevHistory(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsDevHistory());

					iPatientTravelHistoryDetailsService.updateSaveNurseReviewedFlag(patientTravelHistoryDetails);
				}
				else {
				PatientTravelHistoryDetails patientTravelHistoryDetails = new PatientTravelHistoryDetails();
				patientTravelHistoryDetails.setDescription(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getDescription());
				if(patientTravelHistoryDetailsDto.getWhenTravel() == "Invalid date" && patientTravelHistoryDetailsDto.getWhenTravel() == ""){
				patientTravelHistoryDetails.setWhenTravel(null);
				}else{
					patientTravelHistoryDetails.setWhenTravel(CommonDateUtils.getDate(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getWhenTravel(), "dd-MM-yyyy"));
				}
				patientTravelHistoryDetails.setRoleId(patientTravelHistoryDetailsDto.getRoleId());
				patientTravelHistoryDetails.setIsReviewedFlag(
						patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsReviewedFlag() == '\0' ? NO :
							patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsReviewedFlag()
						);
				
				patientTravelHistoryDetails.setIsEnterInErrorStatus( patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsEnterInErrorStatus() );
				
				patientTravelHistoryDetails.setCreatedDate(CommonDateUtils.getDate(patientTravelHistoryDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
				if(patientTravelHistoryDetailsDto.getEncounterId()!=null){
					patientTravelHistoryDetails.setEncounterId(patientTravelHistoryDetailsDto.getEncounterId());
				}
				else{
					patientTravelHistoryDetails.setAdmissionId(patientTravelHistoryDetailsDto.getAdmissionId());
				}
				patientTravelHistoryDetails.setIsNoSignificantTravel(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsNoSignificantTravel());
				patientTravelHistoryDetails.setCreatedBy(patientTravelHistoryDetailsDto.getCreatedBy());
				patientTravelHistoryDetails.setUpdatedBy(patientTravelHistoryDetailsDto.getUpdatedBy());
				patientTravelHistoryDetails.setUpdatedDate(CommonDateUtils.getDate(patientTravelHistoryDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
				patientTravelHistoryDetails.setStatus(patientTravelHistoryDetailsDto.getStatus());
				patientTravelHistoryDetails.setOrganizationId(patientTravelHistoryDetailsDto.getOrganizationId());
				patientTravelHistoryDetails.setUnitId(patientTravelHistoryDetailsDto.getUnitId());
				patientTravelHistoryDetails.setPatientId(patientTravelHistoryDetailsDto.getPatientId());
				patientTravelHistoryDetails.setIsDevHistory(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsDevHistory());
				
				PatientTravelHistoryDetails historyId=save(patientTravelHistoryDetails);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("savePatientTravelHistoryDetails failed", he);
		} catch (Exception e) {
		logger.error("savePatientTravelHistoryDetails failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getOldPatientTravelHistory(
			PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto)
			throws ApplicationException {
		try
		{
			List<PatientTravelHistoryDetailsDto> listPatientTravelHistoryDetailsDto;
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_TRAVEL_HISTORY_DEATILS").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				query=query.concat(subQuery.toString());
				listPatientTravelHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientTravelHistoryDetailsDto.getPatientId())
						.setParameter("organizationId", patientTravelHistoryDetailsDto.getOrganizationId())
						.setParameter("created_date", patientTravelHistoryDetailsDto.getCreatedDate())
						.setResultTransformer(Transformers.aliasToBean(PatientTravelHistoryDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTravelHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getOldPatientTravelHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCurrentPatientTravelHistory(
			PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto)
			throws ApplicationException {
		try
		{
			List<PatientTravelHistoryDetailsDto> listPatientTravelHistoryDetailsDto;
			if(patientTravelHistoryDetailsDto.getRoleId() == 3)
			{

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_TRAVEL_HISTORY_DEATILS_BY_ROLE_ID").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(patientTravelHistoryDetailsDto.getEncounterId()!=null){
					subQuery.append(" AND  patientTravelHist.encounterId ="+patientTravelHistoryDetailsDto.getEncounterId());
					subQuery.append("  ORDER BY 	patientTravelHist.travelHistoryDetailsId asc");
				}
				if(patientTravelHistoryDetailsDto.getAdmissionId()!=null){
					subQuery.append(" AND  patientTravelHist.admissionId ="+patientTravelHistoryDetailsDto.getAdmissionId());
					subQuery.append("  ORDER BY 	patientTravelHist.travelHistoryDetailsId asc");
				}
				query=query.concat(subQuery.toString());
				listPatientTravelHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientTravelHistoryDetailsDto.getPatientId())
						.setParameter("unitId", patientTravelHistoryDetailsDto.getUnitId())
						.setParameter("organizationId", patientTravelHistoryDetailsDto.getOrganizationId())
						.setParameter("roleId", patientTravelHistoryDetailsDto.getRoleId())
						.setResultTransformer(Transformers.aliasToBean(PatientTravelHistoryDetailsDto.class)).list();
			}
			else
			{

				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_TRAVEL_HISTORY_DEATILS").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				if(patientTravelHistoryDetailsDto.getEncounterId()!=null){
					subQuery.append(" AND  patientTravelHist.encounterId ="+patientTravelHistoryDetailsDto.getEncounterId());
					subQuery.append("  ORDER BY 	patientTravelHist.travelHistoryDetailsId asc");
				}
				if(patientTravelHistoryDetailsDto.getAdmissionId()!=null){
					subQuery.append(" AND  patientTravelHist.admissionId ="+patientTravelHistoryDetailsDto.getAdmissionId());
					subQuery.append("  ORDER BY 	patientTravelHist.travelHistoryDetailsId asc");
				}
				query=query.concat(subQuery.toString());
				listPatientTravelHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientTravelHistoryDetailsDto.getPatientId())
						.setParameter("unitId", patientTravelHistoryDetailsDto.getUnitId())
						.setParameter("organizationId", patientTravelHistoryDetailsDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientTravelHistoryDetailsDto.class)).list();
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTravelHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentPatientTravelHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updatePatientTravelHistory(
			PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().size();i++)
			{
				
				PatientTravelHistoryDetails patientTravelHistoryDetails = findById(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getTravelHistoryDetailsId());
				patientTravelHistoryDetails.setDescription(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getDescription());
				if(patientTravelHistoryDetailsDto.getWhenTravel() == "Invalid date"){
					patientTravelHistoryDetails.setWhenTravel(null);
					}else{
						patientTravelHistoryDetails.setWhenTravel(CommonDateUtils.getDate(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getWhenTravel(), "dd-MM-yyyy"));
					}				
				patientTravelHistoryDetails.setRoleId(patientTravelHistoryDetailsDto.getRoleId());
				patientTravelHistoryDetails.setIsReviewedFlag(
						patientTravelHistoryDetailsDto.getRoleId() == 2 
						&& patientTravelHistoryDetails.getRoleId() == 3
						? YES : patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsReviewedFlag()
						);
				
				patientTravelHistoryDetails.setIsEnterInErrorStatus( patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsEnterInErrorStatus() );
				
				patientTravelHistoryDetails.setCreatedDate(CommonDateUtils.getDate(patientTravelHistoryDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
				if(patientTravelHistoryDetailsDto.getEncounterId()!=null){
					patientTravelHistoryDetails.setEncounterId(patientTravelHistoryDetailsDto.getEncounterId());
				}
				else{
					patientTravelHistoryDetails.setAdmissionId(patientTravelHistoryDetailsDto.getAdmissionId());
				}
				patientTravelHistoryDetails.setCreatedBy(patientTravelHistoryDetailsDto.getCreatedBy());
				patientTravelHistoryDetails.setUpdatedBy(patientTravelHistoryDetailsDto.getUpdatedBy());
				patientTravelHistoryDetails.setUpdatedDate(CommonDateUtils.getDate(patientTravelHistoryDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
				patientTravelHistoryDetails.setStatus(patientTravelHistoryDetailsDto.getStatus());
				patientTravelHistoryDetails.setOrganizationId(patientTravelHistoryDetailsDto.getOrganizationId());
				patientTravelHistoryDetails.setUnitId(patientTravelHistoryDetailsDto.getUnitId());
				patientTravelHistoryDetails.setPatientId(patientTravelHistoryDetailsDto.getPatientId());
				
				PatientTravelHistoryDetails historyId=save(patientTravelHistoryDetails);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch (HibernateException he) {
		logger.error("updatePatientTravelHistory failed", he);
		} catch (Exception e) {
		logger.error("updatePatientTravelHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateSaveNurseReviewedFlag(
			PatientTravelHistoryDetails patientTravelHistoryDetails)
			throws ApplicationException {
		try
		{
			save(patientTravelHistoryDetails);
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
			PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().size();i++)
			{
				
					PatientTravelHistoryDetails patientTravelHistory = findById(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getTravelHistoryDetailsId());
					patientTravelHistory.setIsEnterInErrorStatus(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsEnterInErrorStatus());
					patientTravelHistory.setIsReviewedFlag(patientTravelHistory.getIsReviewedFlag() == 'N' && 
							patientTravelHistory.getRoleId() == 3 &&
							patientTravelHistoryDetailsDto.getRoleId() == 2
							? YES : patientTravelHistory.getIsReviewedFlag());
					iPatientTravelHistoryDetailsService.updateSaveNurseReviewedFlag(patientTravelHistory);
					if(patientTravelHistoryDetailsDto.getRoleId() != null && patientTravelHistoryDetailsDto.getRoleId() == 2 && patientTravelHistory.getRoleId() == 3)
					{
					
						PatientTravelHistoryDetails patientTravelHistoryDetails = new PatientTravelHistoryDetails();
						patientTravelHistoryDetails.setDescription(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getDescription());
						patientTravelHistoryDetails.setWhenTravel(CommonDateUtils.getDate(patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getWhenTravel(), "dd-MM-yyyy"));
						
						patientTravelHistoryDetails.setRoleId(patientTravelHistoryDetailsDto.getRoleId());
						patientTravelHistoryDetails.setIsReviewedFlag(
								patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsReviewedFlag() == '\0' ? NO :
									patientTravelHistoryDetailsDto.getListOfPatientTravelHistoryDtos().get(i).getIsReviewedFlag()
								);
						
						patientTravelHistoryDetails.setIsEnterInErrorStatus( NO );
						
						patientTravelHistoryDetails.setCreatedDate(CommonDateUtils.getDate(patientTravelHistoryDetailsDto.getCreatedDate(), "dd-MM-yyyy"));
						if(patientTravelHistoryDetailsDto.getEncounterId()!=null){
							patientTravelHistoryDetails.setEncounterId(patientTravelHistoryDetailsDto.getEncounterId());
						}
						else{
							patientTravelHistoryDetails.setAdmissionId(patientTravelHistoryDetailsDto.getAdmissionId());
						}
						patientTravelHistoryDetails.setCreatedBy(patientTravelHistoryDetailsDto.getCreatedBy());
						patientTravelHistoryDetails.setUpdatedBy(patientTravelHistoryDetailsDto.getUpdatedBy());
						patientTravelHistoryDetails.setUpdatedDate(CommonDateUtils.getDate(patientTravelHistoryDetailsDto.getUpdatedDate(), "dd-MM-yyyy"));
						patientTravelHistoryDetails.setStatus(patientTravelHistoryDetailsDto.getStatus());
						patientTravelHistoryDetails.setOrganizationId(patientTravelHistoryDetailsDto.getOrganizationId());
						patientTravelHistoryDetails.setUnitId(patientTravelHistoryDetailsDto.getUnitId());
						patientTravelHistoryDetails.setPatientId(patientTravelHistoryDetailsDto.getPatientId());
						
						PatientTravelHistoryDetails historyId=save(patientTravelHistoryDetails);
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
	public Response getCurrentPatientTravelHistoryDetailsForCoversheet(
			PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto) throws ApplicationException {
		try
		{
			List<PatientTravelHistoryDetailsDto> listPatientTravelHistoryDetailsDto;
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_TRAVEL_HISTORY_DEATILS_FOR_COVERSHEET").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				query=query.concat(subQuery.toString());
				listPatientTravelHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientTravelHistoryDetailsDto.getPatientId())
						.setParameter("organizationId", patientTravelHistoryDetailsDto.getOrganizationId())
						.setParameter("created_date", patientTravelHistoryDetailsDto.getCreatedDate())
						.setResultTransformer(Transformers.aliasToBean(PatientTravelHistoryDetailsDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTravelHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentPatientTravelHistoryDetailsForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientTravelHistoryForBookView(PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto)
			throws ApplicationException {
		try
		{
			List<PatientTravelHistoryDetailsDto> listPatientTravelHistoryDetailsDto;
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_TRAVEL_HISTORY_FOR_BOOK_VIEW").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				query=query.concat(subQuery.toString());
				listPatientTravelHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientTravelHistoryDetailsDto.getPatientId())
						.setParameter("organizationId", patientTravelHistoryDetailsDto.getOrganizationId())
						.setParameter("fromDate", patientTravelHistoryDetailsDto.getFromDate())
						.setParameter("toDate", patientTravelHistoryDetailsDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(PatientTravelHistoryDetailsDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTravelHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientTravelHistoryForBookView failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateStatusRemoveTravelHistory(PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto)
			throws ApplicationException {
		try {
			PatientTravelHistoryDetails patientTravelHistoryDetails = findById(patientTravelHistoryDetailsDto.getTravelHistoryDetailsId());
			patientTravelHistoryDetails.setStatus('I');
			patientTravelHistoryDetails.setUpdatedBy(patientTravelHistoryDetailsDto.getUpdatedBy());
			patientTravelHistoryDetails.setUpdatedDate(GlobalCommonDateUtils.getDate(patientTravelHistoryDetailsDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(patientTravelHistoryDetails);
	
	return new Response(SUCCESS, SUCCESS_CODE, "Cancel successfully", null, null);
	
	} catch (HibernateException he) {
	logger.error("updateStatusRemoveTravelHistory failed", he);
	} catch (Exception e) {
	logger.error("updateStatusRemoveTravelHistory failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientTravelHistory(PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto)
			throws ApplicationException {
		try
		{
			List<PatientTravelHistoryDetailsDto> listPatientTravelHistoryDetailsDto;
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_TRAVEL_HISTORY_FOR_DOCTOR_APP").getQueryString();
				StringBuffer subQuery=new StringBuffer();
				query=query.concat(subQuery.toString());
				listPatientTravelHistoryDetailsDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientTravelHistoryDetailsDto.getPatientId())
						.setParameter("organizationId", patientTravelHistoryDetailsDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientTravelHistoryDetailsDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientTravelHistoryDetailsDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientTravelHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}

	@Override
	public Response saveCurrentPatientTravelHistoryDetails(List<PatientTravelHistoryDetailsDto> list,
			CoversheetTemplateMasterDto resDto) throws ApplicationException {
		try{
			
			for (PatientTravelHistoryDetailsDto dto : list){
				if(dto.getCovTemplateId() == null){
					PatientTravelHistoryDetails patientTravelHistoryDetails = new PatientTravelHistoryDetails();
					patientTravelHistoryDetails.setDescription(dto.getDescription());
					if(dto.getWhenTravel() == "Invalid date" && dto.getWhenTravel() == ""){
					patientTravelHistoryDetails.setWhenTravel(null);
					}else{
						patientTravelHistoryDetails.setWhenTravel(CommonDateUtils.getDate(dto.getWhenTravel(), "dd-MM-yyyy"));
					}
					patientTravelHistoryDetails.setRoleId(dto.getRoleId());
					patientTravelHistoryDetails.setIsReviewedFlag(
							dto.getIsReviewedFlag() == '\0' ? NO :
								dto.getIsReviewedFlag()
							);
					
					patientTravelHistoryDetails.setIsEnterInErrorStatus( dto.getIsEnterInErrorStatus() );
					
					patientTravelHistoryDetails.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientTravelHistoryDetails.setIsNoSignificantTravel(dto.getIsNoSignificantTravel());
					patientTravelHistoryDetails.setCreatedBy(resDto.getCreatedBy());
					patientTravelHistoryDetails.setUpdatedBy(resDto.getUpdatedBy());
					patientTravelHistoryDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientTravelHistoryDetails.setStatus(resDto.getStatus());
					patientTravelHistoryDetails.setOrganizationId(resDto.getOrganizationId());
					patientTravelHistoryDetails.setUnitId(resDto.getUnitId());
					patientTravelHistoryDetails.setCovTemplateId(resDto.getCovTemplateId());
					save(patientTravelHistoryDetails);
					}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCovTempCurrentPatientTravelHistoryDetails(List<PatientTravelHistoryDetailsDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
	try{
			
			for (PatientTravelHistoryDetailsDto dto : list){
				if(dto.getCovTemplateId() == null){
					
						PatientTravelHistoryDetails patientTravelHistoryDetails = findById(coversheetTemplateMasterDto.getCovTemplateId());
						patientTravelHistoryDetails.setDescription(dto.getDescription());
						if(dto.getWhenTravel() == "Invalid date" && dto.getWhenTravel() == ""){
						patientTravelHistoryDetails.setWhenTravel(null);
						}else{
							patientTravelHistoryDetails.setWhenTravel(CommonDateUtils.getDate(dto.getWhenTravel(), "dd-MM-yyyy"));
						}
						patientTravelHistoryDetails.setRoleId(dto.getRoleId());
						patientTravelHistoryDetails.setIsReviewedFlag(
								dto.getIsReviewedFlag() == '\0' ? NO :
									dto.getIsReviewedFlag()
								);
						
						patientTravelHistoryDetails.setIsEnterInErrorStatus( dto.getIsEnterInErrorStatus() );
						
						patientTravelHistoryDetails.setCreatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
						patientTravelHistoryDetails.setIsNoSignificantTravel(dto.getIsNoSignificantTravel());
						patientTravelHistoryDetails.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						patientTravelHistoryDetails.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						patientTravelHistoryDetails.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
						patientTravelHistoryDetails.setStatus(coversheetTemplateMasterDto.getStatus());
						patientTravelHistoryDetails.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						patientTravelHistoryDetails.setUnitId(coversheetTemplateMasterDto.getUnitId());
						PatientTravelHistoryDetails historyId=update(patientTravelHistoryDetails);
						}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}



	
}
