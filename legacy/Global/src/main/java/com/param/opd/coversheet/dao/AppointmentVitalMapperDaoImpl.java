package com.param.opd.coversheet.dao;

import java.math.BigInteger;
import java.net.URI;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.google.gson.Gson;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.dto.TVitalDto;
import com.param.opd.coversheet.dto.*;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.model.AppointmentVitalMapper;
import com.param.opd.coversheet.model.PatientHabitDetails;
import com.param.opd.coversheet.service.IAppointmentVitalMapperService;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.springframework.web.client.RestTemplate;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AppointmentVitalMapperDaoImpl extends GenericDao<AppointmentVitalMapper, Integer> implements IAppointmentVitalMapperDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(AppointmentVitalMapperDaoImpl.class);


	public AppointmentVitalMapperDaoImpl() {
		super(AppointmentVitalMapper.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private IAppointmentVitalMapperService iAppointmentVitalMapperService;
	
	@Autowired
	DozerBeanMapper mapper;

	@Autowired
	private Gson gson;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	IScheduleLogsDao iScheduleLogsDao;

	@Autowired
	@Qualifier("applicationEventMulticaster")
	private ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	@Transactional
	public Response saveAppointmentVitalMapper(AppointmentVitalMapperDto appointmentVitalMapperDto)throws ApplicationException {
		try
		{

            String Global_Scheduling_URI = null;
            String MY_LIFE_URI = null;
            URI urlMyLife = null;
            Response responseMyLife = null;
            Response commonResponseMyLife = null;
            List<AppointmentVitalMapperDto> dtoListForMyLife = new ArrayList<>();
            Boolean isSyncScheduling = false;
            Integer channelId = null;
            String uuid = null;
            String token = null;

            if(appointmentVitalMapperDto.getEncounterId() != null && appointmentVitalMapperDto.getEncounterId() > 0) {
                isSyncScheduling = (Boolean) sessionFactory.getCurrentSession()
                        .createSQLQuery("  select om.is_sync_scheduling as \"isSyncScheduling\" from public.m_organization_master om where  om.organization_id=:organizationId and om.status='A' ")
                        .setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
                        .uniqueResult();

                channelId = (Integer) sessionFactory.getCurrentSession()
                        .createSQLQuery("  select org.channel_id as \"channelId\" from public.m_organization_master org where  org.organization_id=:organizationId and org.status='A' ")
                        .setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
                        .uniqueResult();

                uuid = (String) sessionFactory.getCurrentSession()
                        .createSQLQuery("select patReg.uuid from patient.t_patient_registration patReg "
                                + "	where patReg.patient_id=:patientId and patReg.organization_id=:orgId ")
                        .setParameter("patientId", appointmentVitalMapperDto.getPatientId())
                        .setParameter("orgId", appointmentVitalMapperDto.getOrganizationId())
                        .uniqueResult();

                if (isSyncScheduling) {
                    Response res = iScheduleLogsDao.getConnectionStringForMyLife();
                    MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) res.getObject();
                    if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null && connectionLinkDto.getMyLifeLink() != "" && connectionLinkDto.getGlobalSchedulingLink() != null && connectionLinkDto.getGlobalSchedulingLink() != "") {
                        Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();
                        MY_LIFE_URI = connectionLinkDto.getMyLifeLink();
                        RestTemplate restTemplate = new RestTemplate();
                        String urlWithParams = MY_LIFE_URI + "/generateToken" + "?userName=" + "ClinivantageAdminUser";

                        Map<String, String> mapResponse = restTemplate.getForObject(urlWithParams, Map.class);

                        if (mapResponse != null) {
                            token = mapResponse.get("token");
                        }
                    }
                }

              
            }
			List<ListAppointmentVitalMapperDto> appointmentVitalList = appointmentVitalMapperDto.getListAppointmentVitalMapperDtos();

//			for(int i=0;i<appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().size();i++)

			for(ListAppointmentVitalMapperDto appointmentVital : appointmentVitalList)
			{

				/*if(appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getAppointmentVitalMapperId() != null &&
						appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getAppointmentVitalMapperId()>0	
						)
				{
					AppointmentVitalMapper appointmentVitalMapper = findById(appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getAppointmentVitalMapperId());
					appointmentVitalMapper.setIsReviewedFlag(YES);
					iAppointmentVitalMapperService.updateSaveNurseReviewedFlag(appointmentVitalMapper);
				}*/
				AppointmentVitalMapper appointmentVitalMapper = new AppointmentVitalMapper();
				
				/*appointmentVitalMapper.setValue(appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getValue());*/
				
				appointmentVitalMapper.setValue(appointmentVital.getValue()!=""?
						appointmentVital.getValue():"0");
				
//				!=null? reportDetailsDto.getUnitId():0)
				
				appointmentVitalMapper.setVitalId(appointmentVital.getVitalId());
				
				appointmentVitalMapper.setIsEnteredInError(appointmentVitalMapperDto.getIsEnteredInError());
				appointmentVitalMapper.setRoleId(appointmentVitalMapperDto.getRoleId());
				appointmentVitalMapper.setIsReviewedFlag(
						appointmentVital.getIsReviewedFlag() == '\0' ? NO :
								appointmentVital.getIsReviewedFlag()
						);
				
				appointmentVitalMapper.setIsSelfTriage(INACTIVE);
				if(appointmentVitalMapperDto.getEncounterId()!=null)
					appointmentVitalMapper.setEncounterId(appointmentVitalMapperDto.getEncounterId());
				else
					appointmentVitalMapper.setAdmissionId(appointmentVitalMapperDto.getAdmissionId());
				
				appointmentVitalMapper.setIsPostFacto(appointmentVitalMapperDto.getIsPostFacto());

				if(appointmentVitalMapperDto.getPostFactoTime()!=null) {

					LocalTime postFactoTime1 = LocalTime.parse(appointmentVitalMapperDto.getPostFactoTime());
					LocalDate today = LocalDate.now();
					LocalDateTime localDateTime = LocalDateTime.of(today, postFactoTime1);
					Instant postFactoTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

					appointmentVitalMapper.setPostFactoTime(postFactoTime);
				}
				else {

					LocalTime currentTime1 = LocalTime.parse(Long.toString(TimeZoneComponent.getDateByzone(appointmentVitalMapperDto.getUnitId()).getTime()));
					LocalDate today = LocalDate.now();
					LocalDateTime localDateTime = LocalDateTime.of(today, currentTime1);
					Instant currentTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

					appointmentVitalMapper.setPostFactoTime(currentTime);
				}
				
				if(appointmentVitalMapperDto.getPostFactoDate()!=null) {
					appointmentVitalMapper.setPostFactoDate(GlobalCommonDateUtils.getDate(appointmentVitalMapperDto.getPostFactoDate(), "dd-M-yyyy HH:mm:ss"));
				}
				if(appointmentVitalMapperDto.getPatientDialysisRecId()!=null) {
					appointmentVitalMapper.setPatientDialysisRecId(appointmentVitalMapperDto.getPatientDialysisRecId());
					appointmentVitalMapper.setDialysisEventId(appointmentVitalMapperDto.getDialysisEventId());
				}
				appointmentVitalMapper.setCreatedBy(appointmentVitalMapperDto.getCreatedBy());
				appointmentVitalMapper.setCreatedDate(GlobalCommonDateUtils.getDate(appointmentVitalMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				appointmentVitalMapper.setOrganizationId(appointmentVitalMapperDto.getOrganizationId());
				appointmentVitalMapper.setPatientId(appointmentVitalMapperDto.getPatientId());
				appointmentVitalMapper.setStatus(appointmentVitalMapperDto.getStatus());
				appointmentVitalMapper.setUnitId(appointmentVitalMapperDto.getUnitId());
				appointmentVitalMapper.setUpdatedBy(appointmentVitalMapperDto.getUpdatedBy());
				appointmentVitalMapper.setUpdatedDate(CommonDateUtils.getDate(appointmentVitalMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				if(appointmentVital.getValue() != null ){

                    AppointmentVitalMapper vitalMappId = save(appointmentVitalMapper);
                    appointmentVitalMapperDto.setAppointmentVitalMapperId(appointmentVitalMapper.getAppointmentVitalMapperId());

					if(appointmentVitalMapperDto.getEncounterId() != null && appointmentVitalMapperDto.getEncounterId()>0) {

                    if (vitalMappId != null && isSyncScheduling) {
						AppointmentVitalMapperDto tempDto = new AppointmentVitalMapperDto();
						tempDto.setHospitalId(appointmentVitalMapperDto.getOrganizationId());
						tempDto.setCreatedDate(appointmentVitalMapperDto.getCreatedDate());
						tempDto.setUnitId(appointmentVitalMapperDto.getUnitId());
						tempDto.setEncounterId(appointmentVitalMapperDto.getEncounterId());
						tempDto.setCreatedBy(appointmentVitalMapperDto.getCreatedBy());
						tempDto.setPatientId(appointmentVitalMapperDto.getPatientId());
						tempDto.setStatus(appointmentVitalMapperDto.getStatus());
						tempDto.setUpdatedBy(appointmentVitalMapperDto.getUpdatedBy());
						tempDto.setUuid(uuid != null ? uuid:"0");
						tempDto.setValue(appointmentVital.getValue()!="" ? appointmentVital.getValue():"0");
						tempDto.setVitalId(appointmentVital.getVitalId());
						tempDto.setIsReviewedFlag(appointmentVital.getIsReviewedFlag() == '\0' ? NO : appointmentVital.getIsReviewedFlag());
						tempDto.setIsSelfTriage(INACTIVE);
						if(appointmentVitalMapperDto.getPatientDialysisRecId()!=null) {
							tempDto.setPatientDialysisRecId(appointmentVitalMapperDto.getPatientDialysisRecId());
							tempDto.setDialysisEventId(appointmentVitalMapperDto.getDialysisEventId());
						}

                        dtoListForMyLife.add(tempDto);
                    }
                }
					appointmentVital.setAppointmentVitalMapperId(appointmentVitalMapper.getAppointmentVitalMapperId());
			}
				
			}

			if (!dtoListForMyLife.isEmpty() && isSyncScheduling) {

				List<Integer> vitalIds = appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().stream().map(ListAppointmentVitalMapperDto::getVitalId).collect(Collectors.toList());
				String sqlQuery = "  select vitalmst.vital_name as \"vitalName\", vitalmst.vital_id as \"vitalId\" from  emr.m_vital_master vitalmst "
							+	  " where vitalmst.status='A' and vitalmst.vital_id in (:vitalIds)";

				StringBuffer sb = new StringBuffer(sqlQuery);
				List<AppointmentVitalMapperDto> vitalIdAndNameList = getSession().createSQLQuery(sb.toString())
						.setParameterList("vitalIds", vitalIds)
						.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();

				if(vitalIdAndNameList != null && !vitalIdAndNameList.isEmpty()){
					for (AppointmentVitalMapperDto dto : dtoListForMyLife) {
						AppointmentVitalMapperDto objectDto = vitalIdAndNameList.stream()
								.filter(order -> dto.getVitalId().equals(order.getVitalId()))
								.findAny()
								.orElse(null);
						if(objectDto!=null){
							dto.setVitalName(objectDto.getVitalName());
						}else{
							dto.setVitalName(null);
						}
					}
				}

				AppointmentVitalMapperDto reqDto = new AppointmentVitalMapperDto();
				reqDto.setListAppointmentVitalMapperDto(dtoListForMyLife);
				try {
					urlMyLife = URI.create(MY_LIFE_URI + "/patient/savespecific/vitals");
					HttpHeaders headers = new HttpHeaders();
					headers.set("Authorization", "Bearer " + token);
					headers.set("userName", "ClinivantageAdminUser");
					headers.set("channelId", (channelId != null && channelId != 0) ? channelId.toString() : "1");
					HttpEntity<AppointmentVitalMapperDto> request = new HttpEntity<>(reqDto, headers);
					String stringResponse = restTemplate.postForObject(urlMyLife, request, String.class);
					commonResponseMyLife = gson.fromJson(stringResponse, Response.class);

				} catch (Exception e) {
					logger.error("saveAppointmentVitalMapper MyLife sync failed", e);
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, appointmentVitalMapperDto.getListAppointmentVitalMapperDtos(), null);
		}catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	public Response getListOfAppointmentVitalMapper(AppointmentVitalMapperDto appointmentVitalMapperDto)throws ApplicationException {
		try
		{
			if(appointmentVitalMapperDto.getListOfSelectedVitals().size() > 0) {
			for(int i=0;i<appointmentVitalMapperDto.getListOfSelectedVitals().size();i++)
			{
			/*StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto = statlessSession.getNamedQuery("GET_LIST_APPOINTMENT_VITAL_MAPPER")
					.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
					.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
					.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
					.setDate("formDate", GlobalCommonDateUtils.getDate(appointmentVitalMapperDto.getFromDate(), "yyyy-MM-dd"))
					.setDate("toDate", GlobalCommonDateUtils.getDate(appointmentVitalMapperDto.getToDate(), "yyyy-MM-dd"))
					.setParameter("vitalId", appointmentVitalMapperDto.getVitalId())
					.setParameter("encounterId", appointmentVitalMapperDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
			
			//System.out.println(listAppointmentVitalMapperDto.size());
			statlessSession.close();*/
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			StringBuilder query=new StringBuilder(statlessSession.getNamedQuery("GET_LIST_APPOINTMENT_VITAL_MAPPER").getQueryString());
//			if(appointmentVitalMapperDto.getPostFactoDate()!=null){
				query.append(" AND 		(to_char(appVitalMapp.postFactoDate, 'YYYY-MM-DD') BETWEEN :formDate AND :toDate "
						+ " OR 		to_char(appVitalMapp.createdDate, 'YYYY-MM-DD') BETWEEN :formDate AND :toDate)");
               
				if(appointmentVitalMapperDto.getDialysisEventId()!=null) {
					query.append(" AND appVitalMapp.dialysisEventId=:dialysisEventId");
				}
				
			/*}else{*/
				query.append(" order by to_char(appVitalMapp.createdDate,'DD/MM/YYYY') desc, to_char(appVitalMapp.createdDate,'HH24:MI') desc  , orgWiseVitalMapper.vitalOrderOpd desc");
			/*}*/
				
			Query sql = statlessSession.createQuery(query.toString())
					.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
//					.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
					.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
					.setParameter("formDate", appointmentVitalMapperDto.getFromDate())
					.setParameter("toDate", appointmentVitalMapperDto.getToDate())
					.setParameterList("multipleVitalList", appointmentVitalMapperDto.getListOfSelectedVitals());
					if(appointmentVitalMapperDto.getDialysisEventId()!=null) {
						sql.setParameter("dialysisEventId", appointmentVitalMapperDto.getDialysisEventId());
					}
//					.setParameter("vitalId", appointmentVitalMapperDto.getVitalId())
				List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto = sql.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
					statlessSession.close();
					
					return new Response(SUCCESS, SUCCESS_CODE, null, listAppointmentVitalMapperDto, null);
			}
			}else if(appointmentVitalMapperDto.getDialysisEventId()!=null){ 
				List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto;
				
				Session statlessSession = sessionFactory.getCurrentSession();
				StringBuilder query=new StringBuilder(statlessSession.getNamedQuery("GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_DIALYSIS").getQueryString());
				
				listAppointmentVitalMapperDto = statlessSession.createQuery(query.toString())
//						.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
						.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
						.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
						.setParameter("dialysisEventId", appointmentVitalMapperDto.getDialysisEventId())
						.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, listAppointmentVitalMapperDto, null);
			}else{
				List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto;
					
					Session statlessSession = sessionFactory.getCurrentSession();
					StringBuilder query=new StringBuilder(statlessSession.getNamedQuery("GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COVERSHEET").getQueryString());
					/*if(appointmentVitalMapperDto.getIsAdmissionFlag()!='Y'){
						query.append(" AND appVitalMapp.encounterId="+appointmentVitalMapperDto.getEncounterId());
						query.append( " order by 	appVitalMapp.appointmentVitalMapperId, vitalMst.vitalOrderOpd desc");
					}else{
						query.append(" AND appVitalMapp.admissionId="+appointmentVitalMapperDto.getEncounterId());
						query.append( " order by 	appVitalMapp.appointmentVitalMapperId, vitalMst.vitalOrderOpd desc");
					}*/
					
					
					listAppointmentVitalMapperDto = statlessSession.createQuery(query.toString())
//							.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
							.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
							.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
							.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, listAppointmentVitalMapperDto, null);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfAppointmentVitalMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	@Transactional
	public Response updateAppointmentVitalMapper(
			AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().size();i++)
			{
				/*Session statlessSession = sessionFactory.getCurrentSession();
				int appointmentVitalMapperId= statlessSession.getNamedQuery("UPDATE_OLD_VITAL")
						.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
						.setParameter("encounterId", appointmentVitalMapperDto.getEncounterId())
						.setParameter("roleId", appointmentVitalMapperDto.getRoleId())
						.setParameter("vitalId", appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getVitalId())
						.executeUpdate();*/
				Session statlessSession = sessionFactory.getCurrentSession();
				StringBuilder query=new StringBuilder(statlessSession.getNamedQuery("UPDATE_OLD_VITAL").getQueryString());
				if(appointmentVitalMapperDto.getEncounterId()!=null){
					query.append(" AND appointVitalMapp.encounterId = :encounterId");
				}else{
					query.append(" AND appointVitalMapp.admissionId = :admissionId");
				}
				int appointmentVitalMapperId= statlessSession.createQuery(query.toString())
						.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
						.setParameter("roleId", appointmentVitalMapperDto.getRoleId())
						.setParameter("vitalId", appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getVitalId())
						.setParameter("encounterId", appointmentVitalMapperDto.getEncounterId())
						.setParameter("admissionId", appointmentVitalMapperDto.getEncounterId())
						.executeUpdate();
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getListOfAppointmentVitalMapperByPatientId(
			AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto = statlessSession.getNamedQuery("GET_LIST_APPOINTMENT_VITAL_MAPPER_BY_PATIENT_ID")
					.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
					.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
					.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
			
			//System.out.println(listAppointmentVitalMapperDto.size());
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listAppointmentVitalMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfAppointmentVitalMapperByPatientId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getOldAppointmentVitalMapper(
			AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		try
		{
			List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto;
			/*if(appointmentVitalMapperDto.getRoleId() == 3)
			{
				Session statlessSession = sessionFactory.getCurrentSession();
				listAppointmentVitalMapperDto = statlessSession.getNamedQuery("GET_LIST_APPOINTMENT_VITAL")
						.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
						.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
						.setParameter("encounterId", appointmentVitalMapperDto.getEncounterId())
						.setParameter("roleId", appointmentVitalMapperDto.getRoleId())
						.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
			}
			else
			{*/
				Session statlessSession = sessionFactory.getCurrentSession();
				listAppointmentVitalMapperDto = statlessSession.getNamedQuery("GET_LIST_APPOINTMENT_VITAL_MAPPER_BY_ROLE_ID")
						.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
						.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
						.setParameter("encounterId", appointmentVitalMapperDto.getEncounterId())
						.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
			/*}
			*/
			return new Response(SUCCESS, SUCCESS_CODE, null, listAppointmentVitalMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getOldAppointmentVitalMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	

	@Override
	public Response getCurrentAppointmentVitalMapper(
			AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		try
		{
			List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto;
			/*if(appointmentVitalMapperDto.getRoleId() == 3)
			{
				Session statlessSession = sessionFactory.getCurrentSession();
				listAppointmentVitalMapperDto = statlessSession.getNamedQuery("GET_CURRENT_LIST_APPOINTMENT_VITAL_MAPPER_BY_ROLE_ID")
						.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
						.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
						.setParameter("encounterId", appointmentVitalMapperDto.getEncounterId())
						.setParameter("roleId", appointmentVitalMapperDto.getRoleId())
						.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
				Session statlessSession = sessionFactory.getCurrentSession();
				StringBuilder query=new StringBuilder(statlessSession.getNamedQuery("GET_CURRENT_LIST_APPOINTMENT_VITAL_MAPPER_BY_ROLE_ID").getQueryString());
				if(appointmentVitalMapperDto.getIsAdmissionFlag()!='Y'){
					query.append(" AND appVitalMapp.encounterId="+appointmentVitalMapperDto.getEncounterId());
					query.append( " order by 	appVitalMapp.appointmentVitalMapperId, vitalMst.vitalOrderOpd desc");
				}else{
					query.append(" AND appVitalMapp.admissionId="+appointmentVitalMapperDto.getEncounterId());
					query.append( " order by 	appVitalMapp.appointmentVitalMapperId, vitalMst.vitalOrderOpd desc");
				}
				listAppointmentVitalMapperDto = statlessSession.createQuery(query.toString())
						.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
						.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
						.setParameter("roleId", appointmentVitalMapperDto.getRoleId())
						.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
			}
			else
			{*/
				/*Session statlessSession = sessionFactory.getCurrentSession();
				listAppointmentVitalMapperDto = statlessSession.getNamedQuery("GET_CURRENT_LIST_APPOINTMENT_VITAL")
						.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
						.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
						.setParameter("encounterId", appointmentVitalMapperDto.getEncounterId())
						.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();*/
				
				Session statlessSession = sessionFactory.getCurrentSession();
				StringBuilder query=new StringBuilder(statlessSession.getNamedQuery("GET_CURRENT_LIST_APPOINTMENT_VITAL").getQueryString());
				
				if(appointmentVitalMapperDto.getFromDate()!=null && appointmentVitalMapperDto.getToDate()!=null) {
					query.append(" AND to_char(appVitalMapp.createdDate, 'YYYY-MM-DD') BETWEEN :fromDate AND :toDate");
				}
				
				if(appointmentVitalMapperDto.getVitalId()>0) {
					query.append(" AND appVitalMapp.vitalId = :vitalId");
				}
				
				
				if(appointmentVitalMapperDto.getIsAdmissionFlag()!='Y'){
					query.append(" AND appVitalMapp.encounterId = :encounterId");
					query.append( " order by 	appVitalMapp.appointmentVitalMapperId, vitalMst.vitalOrderOpd desc");
				}else{
					query.append(" AND appVitalMapp.admissionId = :admissionId");
					query.append( " order by 	appVitalMapp.appointmentVitalMapperId, vitalMst.vitalOrderOpd desc");
				}
				listAppointmentVitalMapperDto = statlessSession.createQuery(query.toString())
						.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
						.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
						.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
						.setParameter("fromDate", appointmentVitalMapperDto.getFromDate()) // Added parameter for fromDate
						.setParameter("toDate", appointmentVitalMapperDto.getToDate())     // Added parameter for toDate
						.setParameter("vitalId", appointmentVitalMapperDto.getVitalId())   // Added parameter for vitalId
						.setParameter("encounterId", appointmentVitalMapperDto.getEncounterId()) // Added parameter for encounterId
						.setParameter("admissionId", appointmentVitalMapperDto.getEncounterId())
						.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
//			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listAppointmentVitalMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getCurrentAppointmentVitalMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateAppointmentVitalMapperDto(
			AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		try
		{
			for(int i=0;i<appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().size();i++)
			{
				
				AppointmentVitalMapper appointmentVitalMapper = findById(appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getAppointmentVitalMapperId());
				
				
				/*if(appointmentVitalMapperDto.getRoleId() != null 
						&& appointmentVitalMapperDto.getRoleId() == 2 
						&& appointmentVitalMapper.getRoleId() != 2 
						&& appointmentVitalMapper.getIsReviewedFlag() == 'N'
						)
				{
					iAppointmentVitalMapperService.saveAppointmentVitalMapper(appointmentVitalMapperDto);
				}*/
				
				appointmentVitalMapper.setValue(appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getValue());
				appointmentVitalMapper.setVitalId(appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getVitalId());
				
				appointmentVitalMapper.setRoleId(appointmentVitalMapperDto.getRoleId());
				appointmentVitalMapper.setIsReviewedFlag(
						appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getIsReviewedFlag() == '\0' ? NO :
							appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getIsReviewedFlag()
						);
				
				appointmentVitalMapper.setIsSelfTriage(INACTIVE);
				appointmentVitalMapper.setEncounterId(appointmentVitalMapperDto.getEncounterId());
				appointmentVitalMapper.setCreatedBy(appointmentVitalMapperDto.getCreatedBy());
				appointmentVitalMapper.setCreatedDate(CommonDateUtils.getDate(appointmentVitalMapperDto.getCreatedDate(), "dd-MM-yyyy"));
				appointmentVitalMapper.setOrganizationId(appointmentVitalMapperDto.getOrganizationId());
				appointmentVitalMapper.setPatientId(appointmentVitalMapperDto.getPatientId());
				appointmentVitalMapper.setStatus(appointmentVitalMapperDto.getStatus());
				appointmentVitalMapper.setUnitId(appointmentVitalMapperDto.getUnitId());
				appointmentVitalMapper.setUpdatedBy(appointmentVitalMapperDto.getUpdatedBy());
				appointmentVitalMapper.setUpdatedDate(CommonDateUtils.getDate(appointmentVitalMapperDto.getUpdatedDate(), "dd-MM-yyyy"));
				
				AppointmentVitalMapper vitalMappId= save(appointmentVitalMapper);
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("updateAppointmentVitalMapperDto failed", he);
		} catch (Exception e) {
		logger.error("updateAppointmentVitalMapperDto failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updateSaveNurseReviewedFlag(
			AppointmentVitalMapper appointmentVitalMapper)
			throws ApplicationException {
		try
		{
			save(appointmentVitalMapper);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		}catch (HibernateException he) {
		logger.error("updateSaveNurseReviewedFlag failed", he);
		} catch (Exception e) {
		logger.error("updateSaveNurseReviewedFlag failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response saveAppointmentVitalMapperFromAndroid(List<AppointmentVitalMapperDto> appointmentVitalMapperDto) {
		try {
			List<AppointmentVitalMapperDto> list=new ArrayList<AppointmentVitalMapperDto>();
			//System.out.println(appointmentVitalMapperDto.size());
			if(!appointmentVitalMapperDto.isEmpty()) {
			for(AppointmentVitalMapperDto l:appointmentVitalMapperDto)
			{
				
				/*	Integer result=(Integer)sessionFactory.getCurrentSession()
							.createSQLQuery(" update patient.t_appointment_vital_mapper set status='I',is_reviewed_flag='Y' "
									+ "where encounter_id="+l.getEncounterId()+" and vital_id="+l.getVitalId()+" ")
							.executeUpdate();*/
					
				Date date = TimeZoneComponent.getDateByzone(l.getUnitId());  
			    SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  
			    String strDate= formatter.format(date);  
			    if(l.getCreatedDate()==null) {
			    	l.setCreatedDate(strDate);
			    }
			    if(l.getUpdatedDate()==null) {
			    	l.setUpdatedDate(strDate);
			    }
			    //System.out.println(strDate);
						AppointmentVitalMapper appointmentVitalMapper=new AppointmentVitalMapper();
						appointmentVitalMapper.setPatientId(l.getPatientId());
						if(l.getEncounterId()!=null) {
						appointmentVitalMapper.setEncounterId(l.getEncounterId());
						}
						appointmentVitalMapper.setVitalId(l.getVitalId());
						appointmentVitalMapper.setValue(l.getValue());
						appointmentVitalMapper.setIsReviewedFlag(NO);
						appointmentVitalMapper.setIsSelfTriage(INACTIVE);
						if(l.getAdmissionId()!=null) {
							appointmentVitalMapper.setAdmissionId(l.getAdmissionId());
						}
						appointmentVitalMapper.setIsEnteredInError('N');
						appointmentVitalMapper.setIsPostFacto('N');
						appointmentVitalMapper.setRoleId(l.getRoleId());
						appointmentVitalMapper.setOrganizationId(l.getOrganizationId());
						appointmentVitalMapper.setUnitId(l.getUnitId());
						appointmentVitalMapper.setStatus('A');
						appointmentVitalMapper.setCreatedBy(l.getCreatedBy());
						appointmentVitalMapper.setCreatedDate(GlobalCommonDateUtils.getDate(l.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
						appointmentVitalMapper.setUpdatedBy(l.getCreatedBy());
						appointmentVitalMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(l.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
						save(appointmentVitalMapper);
				//}
		
			}
			return new Response(SUCCESS, SUCCESS_CODE, "Vital Added Successfully", null, null);
		}else {
			return new Response(SUCCESS, SUCCESS_CODE, "Vital not uploaded", null, null);
		}
		
		} catch (HibernateException he) {
		logger.error("saveAppointmentVitalMapperFromAndroid failed", he);
		} catch (Exception e) {
		logger.error("saveAppointmentVitalMapperFromAndroid failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getIsCriticalByPatientId(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		try
		{
			StatelessSession session=sessionFactory.openStatelessSession();
			EncounterMasterDto dto = (EncounterMasterDto)session
					.getNamedQuery("GET_IS_CRITICAL_BY_PATIENT_ID")
//					.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
					.setParameter("orgId", appointmentVitalMapperDto.getOrganizationId())
					.setParameter("encounterId", appointmentVitalMapperDto.getEncounterId())
					.setResultTransformer(Transformers.aliasToBean(EncounterMasterDto.class)).uniqueResult();
			session.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getIsCriticalByPatientId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response updateVitalForEnteredInError(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		AppointmentVitalMapper appointmentVitalMapper = findById(appointmentVitalMapperDto.getAppointmentVitalMapperId());
		
		appointmentVitalMapper.setIsEnteredInError(appointmentVitalMapperDto.getIsEnteredInError());
		appointmentVitalMapper.setReasonId(appointmentVitalMapperDto.getReasonId());
		appointmentVitalMapper.setUpdatedBy(appointmentVitalMapperDto.getUpdatedBy());
		appointmentVitalMapper.setUpdatedDate(CommonDateUtils.getDate(appointmentVitalMapperDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
		update(appointmentVitalMapper);
  
  return new Response(SUCCESS, SUCCESS_CODE, "Vital marked as entered in error.", null, null);
}


	@Override
	public Response getListOfVitalsByPatientIdForCoversheet(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		try
		{
			List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto;
				
				Session statlessSession = sessionFactory.getCurrentSession();
				StringBuilder query=new StringBuilder(statlessSession.getNamedQuery("GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COVERSHEET").getQueryString());
				listAppointmentVitalMapperDto = statlessSession.createQuery(query.toString())
//						.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
						.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
						.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
//			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listAppointmentVitalMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
			throw e;
		}
//		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getListOfEnteredInErrorVitals(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<AppointmentVitalMapperDto> list= statlessSession.getNamedQuery("GET_LIST_OF_ENTERED_IN_ERROR_VITALS")
					.setParameter("unitId", appointmentVitalMapperDto.getUnitId())
					.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
					.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfEnteredInErrorVitals failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getListOfVitalsByPatientIdForBookView(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		try
		{
			List<AppointmentVitalMapperDto> listAppointmentVitalMapperDto;
				
				Session statlessSession = sessionFactory.getCurrentSession();
				StringBuilder query=new StringBuilder(statlessSession.getNamedQuery("GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_BOOK_VIEW").getQueryString());
				listAppointmentVitalMapperDto = statlessSession.createQuery(query.toString())
						.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
						.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
						.setParameter("fromDate", appointmentVitalMapperDto.getFromDate())
						.setParameter("toDate", appointmentVitalMapperDto.getToDate())
						.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
//			}
			
			return new Response(SUCCESS, SUCCESS_CODE, null, listAppointmentVitalMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfVitalsByPatientIdForBookView failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getQualifiersListForPopUp(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<AppointmentVitalMapperDto> list= statlessSession.getNamedQuery("GET_QUALIFIERS_LIST_FOR_POPUP")
					.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
					.setParameter("appointmentVitalMapperId", appointmentVitalMapperDto.getAppointmentVitalMapperId())
					.setParameter("vitalId", appointmentVitalMapperDto.getVitalId())
					.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getQualifiersListForPopUp failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response getListOfVitalsByPatientIdForDoctorApp(AppointmentVitalMapperDto appointmentVitalMapperDto)
			throws ApplicationException {
		try
		{
			 List<AppointmentVitalMapperDto>	listAppointmentVitalMapperDto = sessionFactory.getCurrentSession().createQuery(" SELECT 		distinct(appVitalMapp.vitalId) as vitalId, appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, vitalMst.vitalOrderOpd as vitalOrderOpd,"
						+ "				appVitalMapp.encounterId as encounterId,appVitalMapp.admissionId as admissionId, appVitalMapp.patientId as patientId, "
						+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
						+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy,vitalMst.vitalOrderOpd as  vitalOrderOpd,"
						+ "				to_char(appVitalMapp.createdDate,'DD/MM/YYYY') as createdDate, to_char(appVitalMapp.postFactoDate,'DD/MM/YYYY') as postFactoDate, "
						+ "             to_char(appVitalMapp.postFactoTime,'HH24:MI') as postFactoTime, appVitalMapp.isPostFacto as isPostFacto,"
						+ "             to_char(appVitalMapp.createdDate,'HH24:MI') as createdTime, to_char(appVitalMapp.updatedDate,'DD-MM-YYYY') as updatedDate,"
						+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
						+ "				vitalMst.vitalName as vitalName, vitalMst.unit as unitDesc, "
						+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag ,"
						+ " concat(coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
						+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,appVitalMapp.dialysisEventId as dialysisEventId "
						+ " FROM 		AppointmentVitalMapper appVitalMapp"
						+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
						+ " LEFT JOIN 	appVitalMapp.userMaster userMaster"
						+ " LEFT JOIN 	userMaster.employeeMastersList employee"
						+ " LEFT JOIN 	userMaster.doctorMastersList doc"
						+ " WHERE 		"
						+ " appVitalMapp.organizationId=:organizationId"
						+ " AND 		appVitalMapp.patientId=:patientId"
						+ " AND 		appVitalMapp.status= 'A' "		
						+ " AND 		appVitalMapp.value !='' "		
						+ " AND 		appVitalMapp.isEnteredInError ='N'"
						+ " order by	appVitalMapp.appointmentVitalMapperId desc ")
						.setParameter("organizationId", appointmentVitalMapperDto.getOrganizationId())
						.setParameter("patientId", appointmentVitalMapperDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(AppointmentVitalMapperDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listAppointmentVitalMapperDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfVitalsByPatientIdForDoctorApp failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);

	}


	@Override
	public Response saveCurrentAppointmentVitals(List<AppointmentVitalMapperDto> list,
			CoversheetTemplateMasterDto resDto) throws ApplicationException {
try{
			
			for (AppointmentVitalMapperDto dto : list){
				if(dto.getCovTemplateId() == null){
			AppointmentVitalMapper appointmentVitalMapper = new AppointmentVitalMapper();
			appointmentVitalMapper.setValue(dto.getValue()!=""?
					dto.getValue():"0");
			appointmentVitalMapper.setVitalId(dto.getVitalId());
			appointmentVitalMapper.setIsEnteredInError(dto.getIsEnteredInError());
			appointmentVitalMapper.setRoleId(dto.getRoleId());
			appointmentVitalMapper.setIsReviewedFlag(
					dto.getIsReviewedFlag() == '\0' ? NO :
						dto.getIsReviewedFlag()
					);
			
			appointmentVitalMapper.setIsSelfTriage(INACTIVE);
//			if(dto.getEncounterId()!=null)
//				appointmentVitalMapper.setEncounterId(resDto.getEncounterId());
//			else
//				appointmentVitalMapper.setAdmissionId(resDto.getAdmissionId());
//			
			appointmentVitalMapper.setIsPostFacto(dto.getIsPostFacto());

			if(dto.getPostFactoTime()!=null) {

				LocalTime postFactoTime1 = LocalTime.parse(dto.getPostFactoTime());
				LocalDate today = LocalDate.now();
				LocalDateTime localDateTime = LocalDateTime.of(today, postFactoTime1);
				Instant postFactoTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

				appointmentVitalMapper.setPostFactoTime(postFactoTime);
			}
			else {
				LocalTime currentTime1 = LocalTime.parse(Long.toString(TimeZoneComponent.getDateByzone(resDto.getUnitId()).getTime()));
				LocalDate today = LocalDate.now();
				LocalDateTime localDateTime = LocalDateTime.of(today, currentTime1);
				Instant currentTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

				appointmentVitalMapper.setPostFactoTime(currentTime);
			}
			
			if(dto.getPostFactoDate()!=null) {
				appointmentVitalMapper.setPostFactoDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			}
			if(dto.getPatientDialysisRecId()!=null) {
				appointmentVitalMapper.setPatientDialysisRecId(dto.getPatientDialysisRecId());
				appointmentVitalMapper.setDialysisEventId(dto.getDialysisEventId());
			}
			appointmentVitalMapper.setCreatedBy(dto.getCreatedBy());
			appointmentVitalMapper.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
			appointmentVitalMapper.setOrganizationId(resDto.getOrganizationId());
//			appointmentVitalMapper.setPatientId(resDto.getPatientId());
			appointmentVitalMapper.setStatus(resDto.getStatus());
			appointmentVitalMapper.setUnitId(resDto.getUnitId());
			appointmentVitalMapper.setUpdatedBy(resDto.getUpdatedBy());
			appointmentVitalMapper.setCovTemplateId(resDto.getCovTemplateId());
			appointmentVitalMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
			if(dto.getValue() != null ){
			save(appointmentVitalMapper);
		}
			
		}/*else {
			AppointmentVitalMapper appointmentVitalMapper = findById(dto.getCovTemplateId());
			appointmentVitalMapper.setValue(dto.getValue()!=""?
					dto.getValue():"0");
			appointmentVitalMapper.setVitalId(dto.getVitalId());
			appointmentVitalMapper.setIsEnteredInError(dto.getIsEnteredInError());
			appointmentVitalMapper.setRoleId(dto.getRoleId());
			appointmentVitalMapper.setIsReviewedFlag(
					dto.getIsReviewedFlag() == '\0' ? NO :
						dto.getIsReviewedFlag()
					);
			
			appointmentVitalMapper.setIsSelfTriage(INACTIVE);
//			if(dto.getEncounterId()!=null)
//				appointmentVitalMapper.setEncounterId(resDto.getEncounterId());
//			else
//				appointmentVitalMapper.setAdmissionId(resDto.getAdmissionId());
			
			appointmentVitalMapper.setIsPostFacto(dto.getIsPostFacto());

			if(dto.getPostFactoTime()!=null) {
				LocalTime postFactoTime = LocalTime.parse(dto.getPostFactoTime());
				appointmentVitalMapper.setPostFactoTime(postFactoTime);
			}
			else {
				LocalTime currentTime = LocalTime.parse( Long.toString(TimeZoneComponent.getDateByzone(resDto.getUnitId()).getTime()));
				appointmentVitalMapper.setPostFactoTime(currentTime);
			}
			
			if(dto.getPostFactoDate()!=null) {
				appointmentVitalMapper.setPostFactoDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			}
			if(dto.getPatientDialysisRecId()!=null) {
				appointmentVitalMapper.setPatientDialysisRecId(dto.getPatientDialysisRecId());
				appointmentVitalMapper.setDialysisEventId(dto.getDialysisEventId());
			}
			appointmentVitalMapper.setCreatedBy(dto.getCreatedBy());
			appointmentVitalMapper.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
			appointmentVitalMapper.setOrganizationId(resDto.getOrganizationId());
//			appointmentVitalMapper.setPatientId(resDto.getPatientId());
			appointmentVitalMapper.setStatus(resDto.getStatus());
			appointmentVitalMapper.setUnitId(resDto.getUnitId());
			appointmentVitalMapper.setUpdatedBy(resDto.getUpdatedBy());
			appointmentVitalMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
			if(dto.getValue() != null ){
			AppointmentVitalMapper vitalMappId= update(appointmentVitalMapper);
		}
			
		}*/
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response updateCovTempCurrentAppointmentVitals(List<AppointmentVitalMapperDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) throws ApplicationException {
try{
			
			for (AppointmentVitalMapperDto dto : list){
				if(dto.getCovTemplateId() != null){
			
			AppointmentVitalMapper appointmentVitalMapper = findById(dto.getCovTemplateId());
			appointmentVitalMapper.setValue(dto.getValue()!=""?
					dto.getValue():"0");
			appointmentVitalMapper.setVitalId(dto.getVitalId());
			appointmentVitalMapper.setIsEnteredInError(dto.getIsEnteredInError());
			appointmentVitalMapper.setRoleId(dto.getRoleId());
			appointmentVitalMapper.setIsReviewedFlag(
					dto.getIsReviewedFlag() == '\0' ? NO :
						dto.getIsReviewedFlag()
					);
			
			appointmentVitalMapper.setIsSelfTriage(INACTIVE);
//			if(dto.getEncounterId()!=null)
//				appointmentVitalMapper.setEncounterId(resDto.getEncounterId());
//			else
//				appointmentVitalMapper.setAdmissionId(resDto.getAdmissionId());
			
			appointmentVitalMapper.setIsPostFacto(dto.getIsPostFacto());

			if(dto.getPostFactoTime()!=null) {

				LocalTime postFactoTime1 = LocalTime.parse(dto.getPostFactoTime());
				LocalDate today = LocalDate.now();
				LocalDateTime localDateTime = LocalDateTime.of(today, postFactoTime1);
				Instant postFactoTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

				appointmentVitalMapper.setPostFactoTime(postFactoTime);
			}
			else {
				LocalTime currentTime1 = LocalTime.parse(Long.toString(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime()));
				LocalDate today = LocalDate.now();
				LocalDateTime localDateTime = LocalDateTime.of(today, currentTime1);
				Instant currentTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

				appointmentVitalMapper.setPostFactoTime(currentTime);
			}
			
			if(dto.getPostFactoDate()!=null) {
				appointmentVitalMapper.setPostFactoDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
			}
			if(dto.getPatientDialysisRecId()!=null) {
				appointmentVitalMapper.setPatientDialysisRecId(dto.getPatientDialysisRecId());
				appointmentVitalMapper.setDialysisEventId(dto.getDialysisEventId());
			}
			appointmentVitalMapper.setCreatedBy(dto.getCreatedBy());
			appointmentVitalMapper.setCreatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
			appointmentVitalMapper.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
//			appointmentVitalMapper.setPatientId(resDto.getPatientId());
			appointmentVitalMapper.setStatus(coversheetTemplateMasterDto.getStatus());
			appointmentVitalMapper.setUnitId(coversheetTemplateMasterDto.getUnitId());
			appointmentVitalMapper.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
			appointmentVitalMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
			if(dto.getValue() != null ){
			AppointmentVitalMapper vitalMappId= update(appointmentVitalMapper);
		}
			
		}
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	
	@Override
	@Transactional
	public Response saveAppointmentVitalMapperEasyEmr(AppointmentVitalMapperDto appointmentVitalMapperDto)throws ApplicationException {
		try
		{
			
			for(int i=0;i<appointmentVitalMapperDto.getListAppointmentVitalMapperDto().size();i++)
			{
				/*if(appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getAppointmentVitalMapperId() != null &&
						appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getAppointmentVitalMapperId()>0	
						)
				{
					AppointmentVitalMapper appointmentVitalMapper = findById(appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getAppointmentVitalMapperId());
					appointmentVitalMapper.setIsReviewedFlag(YES);
					iAppointmentVitalMapperService.updateSaveNurseReviewedFlag(appointmentVitalMapper);
				}*/
				AppointmentVitalMapper appointmentVitalMapper = new AppointmentVitalMapper();
				
				/*appointmentVitalMapper.setValue(appointmentVitalMapperDto.getListAppointmentVitalMapperDtos().get(i).getValue());*/
				
				appointmentVitalMapper.setValue(appointmentVitalMapperDto.getListAppointmentVitalMapperDto().get(i).getValue()!=""?
						appointmentVitalMapperDto.getListAppointmentVitalMapperDto().get(i).getValue():"0");
				
//				!=null? reportDetailsDto.getUnitId():0)
				
				appointmentVitalMapper.setVitalId(appointmentVitalMapperDto.getListAppointmentVitalMapperDto().get(i).getVitalId());
				
				appointmentVitalMapper.setIsEnteredInError(appointmentVitalMapperDto.getIsEnteredInError());
				appointmentVitalMapper.setRoleId(appointmentVitalMapperDto.getRoleId());
				appointmentVitalMapper.setIsReviewedFlag(
						appointmentVitalMapperDto.getListAppointmentVitalMapperDto().get(i).getIsReviewedFlag() == '\0' ? NO :
							appointmentVitalMapperDto.getListAppointmentVitalMapperDto().get(i).getIsReviewedFlag()
						);
				
				appointmentVitalMapper.setIsSelfTriage(INACTIVE);
				if(appointmentVitalMapperDto.getEncounterId()!=null)
					appointmentVitalMapper.setEncounterId(appointmentVitalMapperDto.getEncounterId());
				else
					appointmentVitalMapper.setAdmissionId(appointmentVitalMapperDto.getAdmissionId());
				
				appointmentVitalMapper.setIsPostFacto(appointmentVitalMapperDto.getIsPostFacto());

				if(appointmentVitalMapperDto.getPostFactoTime()!=null) {
					LocalTime postFactoTime1 = LocalTime.parse(appointmentVitalMapperDto.getPostFactoTime());
					LocalDate today = LocalDate.now();
					LocalDateTime localDateTime = LocalDateTime.of(today, postFactoTime1);
					Instant postFactoTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

					appointmentVitalMapper.setPostFactoTime(postFactoTime);
				}
				else {

					LocalTime currentTime1 = LocalTime.parse(Long.toString(TimeZoneComponent.getDateByzone(appointmentVitalMapperDto.getUnitId()).getTime()));
					LocalDate today = LocalDate.now();
					LocalDateTime localDateTime = LocalDateTime.of(today, currentTime1);
					Instant currentTime = localDateTime.atZone(ZoneId.systemDefault()).toInstant();

					appointmentVitalMapper.setPostFactoTime(currentTime);
				}
				
				if(appointmentVitalMapperDto.getPostFactoDate()!=null) {
					appointmentVitalMapper.setPostFactoDate(GlobalCommonDateUtils.getDate(appointmentVitalMapperDto.getPostFactoDate(), "dd-M-yyyy HH:mm:ss"));
				}
				if(appointmentVitalMapperDto.getPatientDialysisRecId()!=null) {
					appointmentVitalMapper.setPatientDialysisRecId(appointmentVitalMapperDto.getPatientDialysisRecId());
					appointmentVitalMapper.setDialysisEventId(appointmentVitalMapperDto.getDialysisEventId());
				}
				appointmentVitalMapper.setCreatedBy(appointmentVitalMapperDto.getCreatedBy());
				appointmentVitalMapper.setCreatedDate(GlobalCommonDateUtils.getDate(appointmentVitalMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				appointmentVitalMapper.setOrganizationId(appointmentVitalMapperDto.getOrganizationId());
				appointmentVitalMapper.setPatientId(appointmentVitalMapperDto.getPatientId());
				appointmentVitalMapper.setStatus(appointmentVitalMapperDto.getStatus());
				appointmentVitalMapper.setUnitId(appointmentVitalMapperDto.getUnitId());
				appointmentVitalMapper.setUpdatedBy(appointmentVitalMapperDto.getUpdatedBy());
				appointmentVitalMapper.setUpdatedDate(CommonDateUtils.getDate(appointmentVitalMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				if(appointmentVitalMapperDto.getListAppointmentVitalMapperDto().get(i).getValue() != null ){
				AppointmentVitalMapper vitalMappId= save(appointmentVitalMapper);
				//appointmentVitalMapperDto.setAppointmentVitalMapperId(appointmentVitalMapper.getAppointmentVitalMapperId());
				appointmentVitalMapperDto.getListAppointmentVitalMapperDto().get(i).setAppointmentVitalMapperId(appointmentVitalMapper.getAppointmentVitalMapperId());
			}
				
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, appointmentVitalMapperDto.getListAppointmentVitalMapperDto(), null);
		}catch (HibernateException he) {
		throw new ApplicationException(he.getMessage(), he);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
