package com.param.opd.coversheet.dao;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dto.ComplaintAppointmentDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientSurgicalHistoryDto;
import com.param.opd.coversheet.model.PatientSurgicalHistory;
import com.param.opd.coversheet.model.PatientPersonalHistoryDetails;
import com.param.opd.coversheet.model.PatientSurgicalHistory;
import com.param.opd.coversheet.service.IPatientSurgicalHistoryService;
import com.param.utility.TimeZoneComponent;
import org.springframework.web.client.RestTemplate;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unused", "unchecked","rawtypes" })
public class PatientSurgicalHistoryDaoImpl extends GenericDao<PatientSurgicalHistory, Integer> implements IPatientSurgicalHistoryDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(PatientSurgicalHistoryDaoImpl.class);


	public PatientSurgicalHistoryDaoImpl() {
		super(PatientSurgicalHistory.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private IPatientSurgicalHistoryService iPatientSurgicalHistoryService;

	@Autowired
	private Gson gson;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	IScheduleLogsDao iScheduleLogsDao;
	
	@Override
	public Response savePatientSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)throws ApplicationException {
		try
		{
			String Global_Scheduling_URI = null;
			String uuid = null;
			String MY_LIFE_URI = null;
			URI urlMyLife = null;
			Response responseMyLife = null;
			Response commonResponseMyLife = null;
			Boolean isSyncScheduling = false;
			Integer channelId = null;
			List<PatientSurgicalHistoryDto> historyDtoList = new ArrayList<>();

			if(patientSurgicalHistoryDto.getEncounterId() != null && patientSurgicalHistoryDto.getEncounterId()>0) {
				isSyncScheduling = (Boolean) sessionFactory.getCurrentSession()
						.createSQLQuery("  select om.is_sync_scheduling as \"isSyncScheduling\" from public.m_organization_master om where  om.organization_id=:organizationId and om.status='A' ")
						.setParameter("organizationId", patientSurgicalHistoryDto.getOrganizationId())
						.uniqueResult();

				channelId = (Integer) sessionFactory.getCurrentSession()
						.createSQLQuery("  select org.channel_id as \"channelId\" from public.m_organization_master org where  org.organization_id=:organizationId and org.status='A' ")
						.setParameter("organizationId", patientSurgicalHistoryDto.getOrganizationId())
						.uniqueResult();

				uuid = (String) sessionFactory.getCurrentSession()
						.createSQLQuery("select patReg.uuid from patient.t_patient_registration patReg "
								+ "	where patReg.patient_id=:patientId and patReg.organization_id=:orgId ")
						.setParameter("patientId", patientSurgicalHistoryDto.getPatientId())
						.setParameter("orgId", patientSurgicalHistoryDto.getOrganizationId())
						.uniqueResult();

				if (isSyncScheduling) {
					Response res = iScheduleLogsDao.getConnectionStringForMyLife();
					MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) res.getObject();
					if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null && connectionLinkDto.getMyLifeLink() != "" && connectionLinkDto.getGlobalSchedulingLink() != null && connectionLinkDto.getGlobalSchedulingLink() != "") {
						Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();
						MY_LIFE_URI = connectionLinkDto.getMyLifeLink();
					}
				}
			}

			for(int i=0;i<patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().size();i++)
			{
				if(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgicalHistoryDetailsId() != null &&
						patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgicalHistoryDetailsId() >0
						)
				{
					PatientSurgicalHistory patientSurgicalHistory = findById(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgicalHistoryDetailsId());
					patientSurgicalHistory.setIsReviewedFlag('N');
					patientSurgicalHistory.setMonth(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getMonth());
					patientSurgicalHistory.setIsNoSignificantStatus(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsNoSignificantStatus());
					patientSurgicalHistory.setYear(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getYear());
					patientSurgicalHistory.setRemark(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getRemark());
					patientSurgicalHistory.setSurgeryId(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryId());
					patientSurgicalHistory.setSurgenName(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgenName());
					if(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryId()== 0){
						patientSurgicalHistory.setSurgeryName(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryName());
					}
					if(patientSurgicalHistoryDto.getEncounterId()!=null){
						patientSurgicalHistory.setEncounterId(patientSurgicalHistoryDto.getEncounterId());
					}
					if(patientSurgicalHistoryDto.getAdmissionId()!=null){
						patientSurgicalHistory.setAdmissionId(patientSurgicalHistoryDto.getAdmissionId());
					}
					patientSurgicalHistory.setRoleId(patientSurgicalHistoryDto.getRoleId());
					patientSurgicalHistory.setIsEnterInErrorStatus(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsEnterInErrorStatus());
					
					patientSurgicalHistory.setIsReviewedFlag(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsReviewedFlag() == '\0' ? NO:
								patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsReviewedFlag()
							);
					
					patientSurgicalHistory.setCreatedDate(CommonDateUtils.getDate(patientSurgicalHistoryDto.getCreatedDate(), "dd-MM-yyyy"));
					patientSurgicalHistory.setCreatedBy(patientSurgicalHistoryDto.getCreatedBy());
					patientSurgicalHistory.setUpdatedBy(patientSurgicalHistoryDto.getUpdatedBy());
					patientSurgicalHistory.setUpdatedDate(CommonDateUtils.getDate(patientSurgicalHistoryDto.getUpdatedDate(), "dd-MM-yyyy"));
					patientSurgicalHistory.setStatus('I');
					patientSurgicalHistory.setOrganizationId(patientSurgicalHistoryDto.getOrganizationId());
					patientSurgicalHistory.setUnitId(patientSurgicalHistoryDto.getUnitId());
					patientSurgicalHistory.setPatientId(patientSurgicalHistoryDto.getPatientId());
					iPatientSurgicalHistoryService.updateSaveNurseReviewedFlag(patientSurgicalHistory);
				}
				else {
				PatientSurgicalHistory patientSurgicalHistory = new PatientSurgicalHistory();
				patientSurgicalHistory.setMonth(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getMonth());
				patientSurgicalHistory.setIsNoSignificantStatus(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsNoSignificantStatus());
				patientSurgicalHistory.setYear(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getYear());
				patientSurgicalHistory.setRemark(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getRemark());
				patientSurgicalHistory.setSurgeryId(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryId());
				patientSurgicalHistory.setSurgenName(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgenName());
				if(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryId()== 0){
					patientSurgicalHistory.setSurgeryName(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryName());
				}
				if(patientSurgicalHistoryDto.getEncounterId()!=null){
					patientSurgicalHistory.setEncounterId(patientSurgicalHistoryDto.getEncounterId());
				}
				if(patientSurgicalHistoryDto.getAdmissionId()!=null){
					patientSurgicalHistory.setAdmissionId(patientSurgicalHistoryDto.getAdmissionId());
				}
				patientSurgicalHistory.setRoleId(patientSurgicalHistoryDto.getRoleId());
				patientSurgicalHistory.setIsEnterInErrorStatus(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsEnterInErrorStatus());
				
				patientSurgicalHistory.setIsReviewedFlag(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsReviewedFlag() == '\0' ? NO:
							patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsReviewedFlag()
						);
				
				patientSurgicalHistory.setCreatedDate(CommonDateUtils.getDate(patientSurgicalHistoryDto.getCreatedDate(), "dd-MM-yyyy"));
				//patientSurgicalHistory.setAppointmentId(patientSurgicalHistoryDto.getAppointmentId());
				patientSurgicalHistory.setCreatedBy(patientSurgicalHistoryDto.getCreatedBy());
				patientSurgicalHistory.setUpdatedBy(patientSurgicalHistoryDto.getUpdatedBy());
				patientSurgicalHistory.setUpdatedDate(CommonDateUtils.getDate(patientSurgicalHistoryDto.getUpdatedDate(), "dd-MM-yyyy"));
				patientSurgicalHistory.setStatus(patientSurgicalHistoryDto.getStatus());
				patientSurgicalHistory.setOrganizationId(patientSurgicalHistoryDto.getOrganizationId());
				patientSurgicalHistory.setUnitId(patientSurgicalHistoryDto.getUnitId());
				patientSurgicalHistory.setPatientId(patientSurgicalHistoryDto.getPatientId());
				
				PatientSurgicalHistory historyId=save(patientSurgicalHistory);

					if(patientSurgicalHistory.getSurgicalHistoryDetailsId() != null && isSyncScheduling){
						PatientSurgicalHistoryDto tempDto = new PatientSurgicalHistoryDto();

						tempDto.setHospitalId(patientSurgicalHistoryDto.getOrganizationId());

						tempDto.setMonth(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getMonth());
						tempDto.setIsNoSignificantStatus(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsNoSignificantStatus());
						tempDto.setYear(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getYear());
						tempDto.setRemark(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getRemark());
						tempDto.setSurgeryId(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryId());
						tempDto.setSurgenName(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgenName());
						tempDto.setSurgeryName(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryName() != null ?
								patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryName() : patientSurgicalHistoryDto.getSurgeryName());
						tempDto.setIsEnterInErrorStatus(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsEnterInErrorStatus());
						tempDto.setEncounterId(patientSurgicalHistoryDto.getEncounterId());
						tempDto.setIsReviewedFlag(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsReviewedFlag() == '\0' ? NO:
								patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsReviewedFlag()
						);
						tempDto.setUuid(uuid != null ? uuid : "0");
						tempDto.setCreatedDate(patientSurgicalHistoryDto.getCreatedDate());
						tempDto.setCreatedBy(patientSurgicalHistoryDto.getCreatedBy());
						tempDto.setUpdatedBy(patientSurgicalHistoryDto.getUpdatedBy());
						tempDto.setUpdatedDate(patientSurgicalHistoryDto.getUpdatedDate());
						tempDto.setStatus(patientSurgicalHistoryDto.getStatus());
						tempDto.setRoleId(patientSurgicalHistoryDto.getRoleId());
						tempDto.setApplicationId(2);

						historyDtoList.add(tempDto);
					}
				}
			}

			if(historyDtoList != null && !historyDtoList.isEmpty()) {
				List<PatientSurgicalHistoryDto> surgeryIdAndNameList = new ArrayList<>();
				List<Integer> surgeryIds = historyDtoList.stream().map(PatientSurgicalHistoryDto::getSurgeryId).collect(Collectors.toList());

				if( surgeryIds != null && !surgeryIds.isEmpty()) {
					String sqlQuery = "  select mst.surgery_id as \"surgeryId\", mst.surgery_name as \"surgeryName\" from  public.m_surgery_master mst "
							+ "	where mst.status='A' and mst.organization_id=:orgId and mst.surgery_id in (:surgeryIds)";

					StringBuffer sb = new StringBuffer(sqlQuery);
					surgeryIdAndNameList = getSession().createSQLQuery(sb.toString())
							.setParameter("orgId", patientSurgicalHistoryDto.getOrganizationId())
							.setParameterList("surgeryIds", surgeryIds)
							.setResultTransformer(Transformers.aliasToBean(PatientSurgicalHistoryDto.class)).list();
				}

				if(surgeryIdAndNameList != null && !surgeryIdAndNameList.isEmpty() && surgeryIdAndNameList.size() == surgeryIds.size()){
					for (PatientSurgicalHistoryDto dto : historyDtoList) {
						PatientSurgicalHistoryDto objectDto = surgeryIdAndNameList.stream()
								.filter(order -> dto.getSurgeryId().equals(order.getSurgeryId()))
								.findAny()
								.orElse(null);
						if(objectDto!=null){
							dto.setSurgeryName(objectDto.getSurgeryName());
						}
					}
				}

				PatientSurgicalHistoryDto reqHistoryDto = new PatientSurgicalHistoryDto();
				reqHistoryDto.setPatientSurgicalHistoryDtoList(historyDtoList);

				try {
					urlMyLife = URI.create(MY_LIFE_URI + "/saveSurgicalHistoryFromHis");
					HttpHeaders headers = new HttpHeaders();
					headers.set("channelId", (channelId != null && channelId != 0) ? channelId.toString() : "1");
					HttpEntity<PatientSurgicalHistoryDto> request = new HttpEntity<>(reqHistoryDto, headers);
					String stringResponse = restTemplate.postForObject(urlMyLife, request, String.class);
					commonResponseMyLife = gson.fromJson(stringResponse, Response.class);
				} catch (Exception e) {
					logger.error("savePatientSurgicalHistory MyLife sync failed", e);
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
				}
			}

			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("savePatientSurgicalHistory failed", he);
		} catch (Exception e) {
		logger.error("savePatientSurgicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getOldListOfPatientSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientSurgicalHistoryDto> listPatientSurgicalHistoryDto;
			
			
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_SURGICAL_HISTORY").getQueryString();
			StringBuffer subQuery=new StringBuffer();
			query = query.concat(subQuery.toString());	
			listPatientSurgicalHistoryDto=sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("patientId", patientSurgicalHistoryDto.getPatientId())
					.setParameter("organizationId", patientSurgicalHistoryDto.getOrganizationId())
					.setParameter("created_date", patientSurgicalHistoryDto.getCreatedDate())
					.setResultTransformer(Transformers.aliasToBean(PatientSurgicalHistoryDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientSurgicalHistoryDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getOldListOfPatientSurgicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCurrentListOfPatientSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)throws ApplicationException {
		try
		{
			List<PatientSurgicalHistoryDto> listPatientSurgicalHistoryDto;
			
			if(patientSurgicalHistoryDto.getRoleId() == 3)
			{
				String baseQuery = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_CURRENT_PATIENT_SURGICAL_HISTORY_BY_ROLE_ID")
						.getQueryString();
				StringBuilder queryBuilder = new StringBuilder(baseQuery);
				if (patientSurgicalHistoryDto.getEncounterId() != null) {
					queryBuilder.append(" AND patientSurgicalHist.encounterId = :encounterId")
							.append(" ORDER BY patientSurgicalHist.surgicalHistoryDetailsId ASC");
				}
				if (patientSurgicalHistoryDto.getAdmissionId() != null) {
					queryBuilder.append(" AND patientSurgicalHist.admissionId = :admissionId")
							.append(" ORDER BY patientSurgicalHist.surgicalHistoryDetailsId ASC");
				}
				String finalQuery = queryBuilder.toString();
				Query query = sessionFactory.getCurrentSession().createQuery(finalQuery)
						.setParameter("roleId", patientSurgicalHistoryDto.getRoleId())
						.setParameter("patientId", patientSurgicalHistoryDto.getPatientId())
						.setParameter("unitId", patientSurgicalHistoryDto.getUnitId())
						.setParameter("organizationId", patientSurgicalHistoryDto.getOrganizationId());
				if (patientSurgicalHistoryDto.getEncounterId() != null) {
					query.setParameter("encounterId", patientSurgicalHistoryDto.getEncounterId());
				}
				if (patientSurgicalHistoryDto.getAdmissionId() != null) {
					query.setParameter("admissionId", patientSurgicalHistoryDto.getAdmissionId());
				}
				 listPatientSurgicalHistoryDto = query
						.setResultTransformer(Transformers.aliasToBean(PatientSurgicalHistoryDto.class))
						.list();

			}else
			{
				String baseQuery = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_CURRENT_PATIENT_SURGICAL_HISTORY")
						.getQueryString();
				StringBuilder queryBuilder = new StringBuilder(baseQuery);
				if (patientSurgicalHistoryDto.getEncounterId() != null) {
					queryBuilder.append(" AND patientSurgicalHist.encounterId = :encounterId")
							.append(" ORDER BY patientSurgicalHist.surgicalHistoryDetailsId ASC");
				}
				if (patientSurgicalHistoryDto.getAdmissionId() != null) {
					queryBuilder.append(" AND patientSurgicalHist.admissionId = :admissionId")
							.append(" ORDER BY patientSurgicalHist.surgicalHistoryDetailsId ASC");
				}
				String finalQuery = queryBuilder.toString();
				Query query = sessionFactory.getCurrentSession().createQuery(finalQuery)
						.setParameter("patientId", patientSurgicalHistoryDto.getPatientId())
						.setParameter("unitId", patientSurgicalHistoryDto.getUnitId())
						.setParameter("organizationId", patientSurgicalHistoryDto.getOrganizationId());
				if (patientSurgicalHistoryDto.getEncounterId() != null) {
					query.setParameter("encounterId", patientSurgicalHistoryDto.getEncounterId());
				}
				if (patientSurgicalHistoryDto.getAdmissionId() != null) {
					query.setParameter("admissionId", patientSurgicalHistoryDto.getAdmissionId());
				}
                  listPatientSurgicalHistoryDto = query
						.setResultTransformer(Transformers.aliasToBean(PatientSurgicalHistoryDto.class))
						.list();

			}
		
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientSurgicalHistoryDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentListOfPatientSurgicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updatePatientSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)throws ApplicationException {
		try
		{
			for(int i=0;i<patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().size();i++)
			{
				Session statlessSession = sessionFactory.getCurrentSession();
				PatientSurgicalHistory patientSurgicalHistory = findById(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgicalHistoryDetailsId());
				

				if(patientSurgicalHistoryDto.getRoleId() != null && 
				   patientSurgicalHistoryDto.getRoleId() == 2 && 
				   patientSurgicalHistory.getRoleId() != 2 && 
				   patientSurgicalHistory.getIsReviewedFlag() == 'N')
				{
					iPatientSurgicalHistoryService.savePatientSurgicalHistory(patientSurgicalHistoryDto);
				}
				
				patientSurgicalHistory.setMonth(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getMonth());
				patientSurgicalHistory.setIsNoSignificantStatus(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsNoSignificantStatus());
				patientSurgicalHistory.setYear(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getYear());
				patientSurgicalHistory.setRemark(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getRemark());
				patientSurgicalHistory.setSurgeryId(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryId());
				patientSurgicalHistory.setSurgenName(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgenName());
				if(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryId()== 0){
					patientSurgicalHistory.setSurgeryName(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryName());
				}
				if(patientSurgicalHistoryDto.getEncounterId()!=null){
					patientSurgicalHistory.setEncounterId(patientSurgicalHistoryDto.getEncounterId());
				}
				if(patientSurgicalHistoryDto.getAdmissionId()!=null){
					patientSurgicalHistory.setAdmissionId(patientSurgicalHistoryDto.getAdmissionId());
				}
				patientSurgicalHistory.setIsEnterInErrorStatus(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsEnterInErrorStatus());
				patientSurgicalHistory.setRoleId(patientSurgicalHistory.getRoleId());
				patientSurgicalHistory.setIsReviewedFlag(patientSurgicalHistoryDto.getRoleId() == 2 && patientSurgicalHistory.getRoleId() == 3 ? YES :
					patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsReviewedFlag()
					);
				
				patientSurgicalHistory.setCreatedDate(CommonDateUtils.getDate(patientSurgicalHistoryDto.getCreatedDate(), "dd-MM-yyyy"));
				patientSurgicalHistory.setCreatedBy(patientSurgicalHistoryDto.getCreatedBy());
				patientSurgicalHistory.setUpdatedBy(patientSurgicalHistoryDto.getUpdatedBy());
				patientSurgicalHistory.setUpdatedDate(CommonDateUtils.getDate(patientSurgicalHistoryDto.getUpdatedDate(), "dd-MM-yyyy"));
				patientSurgicalHistory.setStatus(patientSurgicalHistoryDto.getStatus());
				patientSurgicalHistory.setOrganizationId(patientSurgicalHistoryDto.getOrganizationId());
				patientSurgicalHistory.setUnitId(patientSurgicalHistoryDto.getUnitId());
				patientSurgicalHistory.setPatientId(patientSurgicalHistoryDto.getPatientId());
				save(patientSurgicalHistory);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updatePatientSurgicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateSaveNurseReviewedFlag(
			PatientSurgicalHistory patientSurgicalHistory)
			throws ApplicationException {
		try
		{
			save(patientSurgicalHistory);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("updateSaveNurseReviewedFlag failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateStatusEnterInError(
			PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().size();i++)
			{
				Session statlessSession = sessionFactory.getCurrentSession();
				PatientSurgicalHistory patientSurgicalHistoryDetails = findById(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgicalHistoryDetailsId());
				patientSurgicalHistoryDetails.setIsEnterInErrorStatus(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsEnterInErrorStatus());
				patientSurgicalHistoryDetails.setIsReviewedFlag(patientSurgicalHistoryDetails.getIsReviewedFlag() == 'N' && 
						patientSurgicalHistoryDetails.getRoleId() == 3 &&
						patientSurgicalHistoryDto.getRoleId() == 2
						? YES : patientSurgicalHistoryDetails.getIsReviewedFlag());
				iPatientSurgicalHistoryService.updateSaveNurseReviewedFlag(patientSurgicalHistoryDetails);
				
				if(patientSurgicalHistoryDto.getRoleId() != null &&
				   patientSurgicalHistoryDto.getRoleId() == 2 &&
				   patientSurgicalHistoryDetails.getRoleId() == 3
				   )
				{
					PatientSurgicalHistory patientSurgicalHistory = new PatientSurgicalHistory();
					patientSurgicalHistory.setMonth(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getMonth());
					patientSurgicalHistory.setIsNoSignificantStatus(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsNoSignificantStatus());
					patientSurgicalHistory.setYear(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getYear());
					patientSurgicalHistory.setRemark(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getRemark());
					patientSurgicalHistory.setSurgeryId(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgeryId());
					patientSurgicalHistory.setSurgenName(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getSurgenName());
					
					if(patientSurgicalHistoryDto.getEncounterId()!=null){
						patientSurgicalHistory.setEncounterId(patientSurgicalHistoryDto.getEncounterId());
					}
					if(patientSurgicalHistoryDto.getAdmissionId()!=null){
						patientSurgicalHistory.setAdmissionId(patientSurgicalHistoryDto.getAdmissionId());
					}
					patientSurgicalHistory.setRoleId(patientSurgicalHistoryDto.getRoleId());
					patientSurgicalHistory.setIsEnterInErrorStatus(NO);
					
					patientSurgicalHistory.setIsReviewedFlag(patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsReviewedFlag() == '\0' ? NO:
								patientSurgicalHistoryDto.getListOfPatientSurgicalHistoryDto().get(i).getIsReviewedFlag()
							);
					
					patientSurgicalHistory.setCreatedDate(CommonDateUtils.getDate(patientSurgicalHistoryDto.getCreatedDate(), "dd-MM-yyyy"));
					patientSurgicalHistory.setCreatedBy(patientSurgicalHistoryDto.getCreatedBy());
					patientSurgicalHistory.setUpdatedBy(patientSurgicalHistoryDto.getUpdatedBy());
					patientSurgicalHistory.setUpdatedDate(CommonDateUtils.getDate(patientSurgicalHistoryDto.getUpdatedDate(), "dd-MM-yyyy"));
					patientSurgicalHistory.setStatus(patientSurgicalHistoryDto.getStatus());
					patientSurgicalHistory.setOrganizationId(patientSurgicalHistoryDto.getOrganizationId());
					patientSurgicalHistory.setUnitId(patientSurgicalHistoryDto.getUnitId());
					patientSurgicalHistory.setPatientId(patientSurgicalHistoryDto.getPatientId());
					
					PatientSurgicalHistory historyId=save(patientSurgicalHistory);
				}
				
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			}
		}catch(HibernateException he) { 
				throw new HibernateException(he.getMessage());
			}catch(Exception e) {
			logger.error("updateStatusEnterInError failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);		
	}

	@Override
	public Response getCurrentPatientSurgicalHistoryForCoversheet(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientSurgicalHistoryDto> listPatientSurgicalHistoryDto;
			
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_CURRENT_PATIENT_SURGICAL_HISTORY_FOR_COVERSHEET").getQueryString();

				listPatientSurgicalHistoryDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientSurgicalHistoryDto.getPatientId())
						.setParameter("organizationId", patientSurgicalHistoryDto.getOrganizationId())
						.setParameter("created_date", patientSurgicalHistoryDto.getCreatedDate())
						.setResultTransformer(Transformers.aliasToBean(PatientSurgicalHistoryDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientSurgicalHistoryDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentPatientSurgicalHistoryForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getPatientSurgicalHistoryForBookView(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientSurgicalHistoryDto> listPatientSurgicalHistoryDto;
			
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_SURGICAL_HISTORY_FOR_BOOK_VIEW").getQueryString();

				listPatientSurgicalHistoryDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientSurgicalHistoryDto.getPatientId())
						.setParameter("organizationId", patientSurgicalHistoryDto.getOrganizationId())
						.setParameter("fromDate", patientSurgicalHistoryDto.getFromDate())
						.setParameter("toDate", patientSurgicalHistoryDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(PatientSurgicalHistoryDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientSurgicalHistoryDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientSurgicalHistoryForBookView failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getCompletedProcedureListForSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientSurgicalHistoryDto> listPatientSurgicalHistoryDto;
			
				String query=sessionFactory.getCurrentSession().getNamedQuery("GET_COMPLETED_PROCEDURE_LIST_FOR_SURGICAL_HISTORY").getQueryString();

				listPatientSurgicalHistoryDto=sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("patientId", patientSurgicalHistoryDto.getPatientId())
						.setParameter("organizationId", patientSurgicalHistoryDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientSurgicalHistoryDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientSurgicalHistoryDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCompletedProcedureListForSurgicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateStatusRemoveSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {try {
				PatientSurgicalHistory patientSurgicalHistoryDetails = findById(patientSurgicalHistoryDto.getSurgicalHistoryDetailsId());
				patientSurgicalHistoryDetails.setStatus('I');
				patientSurgicalHistoryDetails.setUpdatedBy(patientSurgicalHistoryDto.getUpdatedBy());
				patientSurgicalHistoryDetails.setUpdatedDate(CommonDateUtils.getDate(patientSurgicalHistoryDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
				update(patientSurgicalHistoryDetails);
		return new Response(SUCCESS, SUCCESS_CODE, "Cancel successfully", null, null);
		
		} catch (HibernateException he) {
		logger.error("updateStatusRemoveSurgicalHistory failed", he);
		} catch (Exception e) {
		logger.error("updateStatusRemoveSurgicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}

	@Override
	public Response getPatientSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientSurgicalHistoryDto> listPatientSurgicalHistoryDto;
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_SURGICAL_HISTORY_FOR_DOCTOR_APP").getQueryString();
			listPatientSurgicalHistoryDto=sessionFactory.getCurrentSession().createQuery(query)
						.setParameter("patientId", patientSurgicalHistoryDto.getPatientId())
						.setParameter("organizationId", patientSurgicalHistoryDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientSurgicalHistoryDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientSurgicalHistoryDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getPatientSurgicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getMostUsedSurgicalHistory(PatientSurgicalHistoryDto patientSurgicalHistoryDto)
			throws ApplicationException {
		try
		{
			List<PatientSurgicalHistoryDto> listPatientSurgicalHistoryDto;
			
			String query = "select " 
						+ "	surMppr.surgery_id as \"surgeryId\", "
						+ "	surgMst.surgery_name as \"surgeryName\", " 
						+ "	surgMst.surgery_code as \"surgeryCode\", "
						+ "	surMppr.status as \"status\", " 
						+ "	surMppr.unit_id as \"unitId\", "
						+ "	surMppr.organization_id as \"organizationId\", " 
						+ "	count(*) as \"countSurg\" " 
						+ " from "
						+ "	patient.t_surgical_history_details surMppr "
						+ " inner join public.m_surgery_master surgMst on surgMst.surgery_id = surMppr.surgery_id  "
						+ " where " 
						+ "	surMppr.unit_id =:unitId  " 
						+ "	and surMppr.organization_id =:organizationId "
						+ "	and surMppr.status = 'A' " 
						+ " group by " 
						+ "	surMppr.surgery_id , "
						+ "	surgMst.surgery_name, " 
						+ "	surgMst.surgery_code, " 
						+ "	surMppr.status, "
						+ "	surMppr.unit_id, " 
						+ "	surMppr.organization_id " 
						+ " order by "
						+ "	count(*) desc " 
						+ "limit 10;";
				
			listPatientSurgicalHistoryDto=sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("unitId", patientSurgicalHistoryDto.getUnitId())
						.setParameter("organizationId", patientSurgicalHistoryDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(PatientSurgicalHistoryDto.class)).list();
		
			return new Response(SUCCESS, SUCCESS_CODE, null, listPatientSurgicalHistoryDto, null);
		}catch(HibernateException he) {
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getMostUsedSurgicalHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveSurgHistory(List<PatientSurgicalHistoryDto> list, CoversheetTemplateMasterDto resDto)
			throws ApplicationException {

		try{
		
			for (PatientSurgicalHistoryDto dto : list) {
				if(dto.getCovTemplateId() == null) {
					PatientSurgicalHistory patientSurgicalHistory = new PatientSurgicalHistory();
					patientSurgicalHistory.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientSurgicalHistory.setIsReviewedFlag(dto.getIsReviewedFlag());
					patientSurgicalHistory.setCreatedBy(resDto.getCreatedBy());
					patientSurgicalHistory.setUpdatedBy(resDto.getUpdatedBy());
					patientSurgicalHistory.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					patientSurgicalHistory.setStatus(resDto.getStatus());
					patientSurgicalHistory.setOrganizationId(resDto.getOrganizationId());
					patientSurgicalHistory.setUnitId(resDto.getUnitId());
					patientSurgicalHistory.setSurgeryId(dto.getSurgeryId());
					patientSurgicalHistory.setMonth(dto.getMonth());
					patientSurgicalHistory.setYear(dto.getYear());
					patientSurgicalHistory.setIsEnterInErrorStatus('N');
					patientSurgicalHistory.setRemark(dto.getRemark());
					patientSurgicalHistory.setCovTemplateId(resDto.getCovTemplateId());
					patientSurgicalHistory.setSurgenName(dto.getSurgenName());
					patientSurgicalHistory.setSurgeryName(dto.getSurgeryName());
					save(patientSurgicalHistory);
				}
				
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCovTempSurgHistory(List<PatientSurgicalHistoryDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
		try{
			
			for (PatientSurgicalHistoryDto dto : list) {
				if(dto.getCovTemplateId() != null) {
					
					PatientSurgicalHistory patientSurgicalHistory = findById(dto.getCovTemplateId());
					patientSurgicalHistory.setCreatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					patientSurgicalHistory.setIsReviewedFlag(dto.getIsReviewedFlag());
					patientSurgicalHistory.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
					patientSurgicalHistory.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
					patientSurgicalHistory.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					patientSurgicalHistory.setStatus(coversheetTemplateMasterDto.getStatus());
					patientSurgicalHistory.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					patientSurgicalHistory.setUnitId(coversheetTemplateMasterDto.getUnitId());
					patientSurgicalHistory.setSurgeryId(dto.getSurgeryId());
					patientSurgicalHistory.setMonth(dto.getMonth());
					patientSurgicalHistory.setYear(dto.getYear());
					patientSurgicalHistory.setIsEnterInErrorStatus('N');
					patientSurgicalHistory.setRemark(dto.getRemark());
					patientSurgicalHistory.setSurgenName(dto.getSurgenName());
					patientSurgicalHistory.setSurgeryName(dto.getSurgeryName());
					sessionFactory.getCurrentSession().update(patientSurgicalHistory);
				}
				
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}



}
