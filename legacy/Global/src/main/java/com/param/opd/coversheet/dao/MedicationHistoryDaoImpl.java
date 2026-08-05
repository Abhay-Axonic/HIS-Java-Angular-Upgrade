package com.param.opd.coversheet.dao;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.opd.coversheet.dto.*;
import com.param.opd.coversheet.model.PatientSurgicalHistory;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Repository;

import com.param.entity.model.opd.MedicationHistory;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.model.PatientTreatmentHistoryDetails;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.springframework.web.client.RestTemplate;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "unchecked","rawtypes" })
public class MedicationHistoryDaoImpl extends GenericDao<MedicationHistory, Integer>
implements IMedicationHistoryDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(MedicationHistoryDaoImpl.class);

	public MedicationHistoryDaoImpl() {
		super(MedicationHistory.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	DozerBeanMapper mapper;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private Gson gson;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	IScheduleLogsDao iScheduleLogsDao;


	@Override
	public Response saveMedicationHistory(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
	try {

		String Global_Scheduling_URI = null;
		String MY_LIFE_URI = null;
		String uuid = null;
		URI urlMyLife = null;
		Response responseMyLife = null;
		Response commonResponseMyLife = null;
		Boolean isSyncScheduling = false;
		Integer channelId = null;
		String token = null;

		if(medicationHistoryDto.getEncounterId() != null && medicationHistoryDto.getEncounterId()>0) {
			isSyncScheduling = (Boolean) sessionFactory.getCurrentSession()
					.createSQLQuery("  select om.is_sync_scheduling as \"isSyncScheduling\" from public.m_organization_master om where  om.organization_id=:organizationId and om.status='A' ")
					.setParameter("organizationId", medicationHistoryDto.getOrganizationId())
					.uniqueResult();

			channelId = (Integer) sessionFactory.getCurrentSession()
					.createSQLQuery("  select org.channel_id as \"channelId\" from public.m_organization_master org where  org.organization_id=:organizationId and org.status='A' ")
					.setParameter("organizationId", medicationHistoryDto.getOrganizationId())
					.uniqueResult();

			uuid = (String) sessionFactory.getCurrentSession()
					.createSQLQuery("select patReg.uuid from patient.t_patient_registration patReg "
							+ "	where patReg.patient_id=:patientId and patReg.organization_id=:orgId ")
					.setParameter("patientId", medicationHistoryDto.getPatientId())
					.setParameter("orgId", medicationHistoryDto.getOrganizationId())
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

		/*MedicationHistory medicationHistory = mapper.map(medicationHistoryDto, MedicationHistory.class, "MedicationHistoryDto_to_MedicationHistory");*/
	 MedicationHistory medicationHistory=new MedicationHistory();
	 medicationHistory.setDoctorId(medicationHistoryDto.getDoctorId());
	 medicationHistory.setDoseUnitId(medicationHistoryDto.getDoseUnitId());
	 medicationHistory.setStoreId(medicationHistoryDto.getStoreId() );
	 medicationHistory.setStatus(medicationHistoryDto.getStatus());
	 medicationHistory.setPatientId(medicationHistoryDto.getPatientId());
	 medicationHistory.setEncounterId(medicationHistoryDto.getEncounterId());
	 medicationHistory.setAdmissionId(medicationHistoryDto.getAdmissionId());
	 medicationHistory.setDoseId(medicationHistoryDto.getDoseId());
	 medicationHistory.setDuration(medicationHistoryDto.getDuration());
	 medicationHistory.setDurationUnit(medicationHistoryDto.getDurationUnit());
	 medicationHistory.setDoseUnitId(medicationHistoryDto.getDoseUnitId());
	 medicationHistory.setFrequencyId(medicationHistoryDto.getFrequencyId());
	 medicationHistory.setUnitId(medicationHistoryDto.getUnitId());
	 medicationHistory.setOrganizationId(medicationHistoryDto.getOrganizationId());
	 medicationHistory.setCreatedBy(medicationHistoryDto.getCreatedBy());
	 medicationHistory.setGenericId(medicationHistoryDto.getGenericId());
	 medicationHistory.setInstructionId(medicationHistoryDto.getInstructionId());
	 medicationHistory.setItemId(medicationHistoryDto.getItemId());
	 medicationHistory.setGenericName(medicationHistoryDto.getGenericName());
	 medicationHistory.setItemName(medicationHistoryDto.getItemName());
	 medicationHistory.setIsNonFormulary(medicationHistoryDto.getIsNonFormulary());
	 medicationHistory.setOrderQty(medicationHistoryDto.getOrderQty());
	 medicationHistory.setRoaId(medicationHistoryDto.getRoaId());
	 medicationHistory.setIsMedicationContinue(medicationHistoryDto.getIsMedicationContinue());
	 medicationHistory.setIsMedicationStopHis(medicationHistoryDto.getIsMedicationStopHis());
	 medicationHistory.setJustificationStopHis(medicationHistoryDto.getJustificationStopHis());
	 medicationHistory.setIsNoSignificantMedication(medicationHistoryDto.getIsNoSignificantMedication());
	 medicationHistory.setUpdatedBy(medicationHistoryDto.getUpdatedBy());
	 medicationHistory.setCreatedDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
	 medicationHistory.setUpdatedDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
	 if(medicationHistoryDto.getAdmissionId()!=null){
		 medicationHistory.setStartDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getStartDate(), "dd-M-yyyy HH:mm:ss"));
		 if(medicationHistoryDto.getEndDate()!="Invalid date") {
			 medicationHistory.setEndDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getEndDate(), "dd-M-yyyy HH:mm:ss"));

		 }else {
			 medicationHistory.setEndDate(null);
		 }
	 }else{
	 medicationHistory.setStartDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getStartDate(), "dd-M-yyyy HH:mm:ss"));
	 if(medicationHistoryDto.getEndDate() != "Invalid date") {
		 medicationHistory.setEndDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getEndDate(), "dd-M-yyyy HH:mm:ss"));
	 }
	 
	 }
	 medicationHistory.setAuthorisedDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getAuthorisedDate(), "yyyy-M-dd HH:mm:ss"));
	 MedicationHistory responseClass = save(medicationHistory);

		if(responseClass.getMedicationHistoryId() != null && isSyncScheduling){

			List<MedicationHistoryDto> frequencyIdAndNameList = new ArrayList<>();

			if (medicationHistoryDto.getFrequencyId() != null && medicationHistoryDto.getFrequencyId()>0) {
				String sqlQuery = "  select mst.id as \"frequencyId\", mst.description as \"frequencyDesc\" from  public.m_frequency mst "
						+ "	where mst.is_active=true and mst.organization_id=:orgId and mst.id=:frequencyId";

				StringBuffer sb = new StringBuffer(sqlQuery);
				frequencyIdAndNameList = getSession().createSQLQuery(sb.toString())
						.setParameter("frequencyId", medicationHistoryDto.getFrequencyId())
						.setParameter("orgId", medicationHistoryDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();

				if(frequencyIdAndNameList != null && !frequencyIdAndNameList.isEmpty()){
					MedicationHistoryDto objectDto = frequencyIdAndNameList.stream()
							.filter(order -> medicationHistoryDto.getFrequencyId().equals(order.getFrequencyId()))
							.findAny()
							.orElse(null);
					if(objectDto!=null){
						medicationHistoryDto.setFrequencyDesc(objectDto.getFrequencyDesc());
					}else{
						medicationHistoryDto.setFrequencyDesc(null);
					}
				}
			}

			medicationHistoryDto.setHospitalId(medicationHistoryDto.getOrganizationId());
			medicationHistoryDto.setUuid(uuid != null ? uuid:"0");
			medicationHistoryDto.setApplicationId(2);

			try {
				urlMyLife = URI.create(MY_LIFE_URI + "/saveMedicationAppoMapper");
				HttpHeaders headers = new HttpHeaders();
				headers.set("Authorization", "Bearer " + token);
				headers.set("userName", "ClinivantageAdminUser");
				headers.set("channelId", (channelId != null && channelId != 0) ? channelId.toString() : "1");
				HttpEntity<MedicationHistoryDto> request = new HttpEntity<>(medicationHistoryDto, headers);
				String stringResponse = restTemplate.postForObject(urlMyLife, request, String.class);
				commonResponseMyLife = gson.fromJson(stringResponse, Response.class);
			}catch(Exception e){
				logger.error("saveMedicationHistory MyLife sync failed", e);
				return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
			}
		}

		return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
	} catch (HibernateException he) {
	logger.error("saveMedicationHistory failed", he);
	} catch (Exception e) {
	logger.error("saveMedicationHistory failed", e);
	}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}

	@Override
	public Response getMedicationHistoryList(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
			try {
				List<MedicationHistoryDto> list=sessionFactory.getCurrentSession().getNamedQuery("GET_MEDICATION_HISTORY_LIST")
					.setParameter("organizationId", medicationHistoryDto.getOrganizationId())
					.setParameter("unitId", medicationHistoryDto.getUnitId())
					.setParameter("patientId", medicationHistoryDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
				
				
		} catch (HibernateException he) {
		logger.error("getMedicationHistoryList failed", he);
		} catch (Exception e) {
		logger.error("getMedicationHistoryList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdministrationWorkListList(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			Integer count=0;
			List<MedicationHistoryDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMINISTRATION_WORKLIST_LIST")
					.setParameter("organizationId",  medicationHistoryDto.getOrganizationId())
					.setParameter("unitId",  medicationHistoryDto.getUnitId())
					.setParameter("fromDate", medicationHistoryDto.getFromDate())
					.setParameter("toDate", medicationHistoryDto.getToDate())
					.setParameter("admissionId", medicationHistoryDto.getAdmissionId())
					.setFirstResult(medicationHistoryDto.getOffset() != null ? medicationHistoryDto.getOffset()  : 0)
					.setMaxResults(medicationHistoryDto.getNoOfRecordsPerPage() != null ? medicationHistoryDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
			
			List<MedicationHistoryDto> listCount=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMINISTRATION_WORKLIST_LIST")
					.setParameter("organizationId",  medicationHistoryDto.getOrganizationId())
					.setParameter("unitId",  medicationHistoryDto.getUnitId())
					.setParameter("fromDate", medicationHistoryDto.getFromDate())
					.setParameter("toDate", medicationHistoryDto.getToDate())
					.setParameter("admissionId", medicationHistoryDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
			
			 if(listCount != null && listCount.size()>0) {
				 count= listCount.size();
	            }

		return new Response(SUCCESS, SUCCESS_CODE, null, list, count);
		
		} catch (HibernateException he) {
		logger.error("getAdministrationWorkListList failed", he);
		} catch (Exception e) {
		logger.error("getAdministrationWorkListList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDetailedListOfMedication(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try{
			List<MedicationHistoryDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_MEDICATIONS")
					.setParameter("organizationId", medicationHistoryDto.getOrganizationId())
					.setParameter("unitId", medicationHistoryDto.getUnitId())
					.setParameter("patientId", medicationHistoryDto.getPatientId())
					.setParameter("storeId", medicationHistoryDto.getStoreId())
					.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getDetailedListOfMedication failed", he);
		} catch (Exception e) {
		logger.error("getDetailedListOfMedication failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMedicationEncountersList(Integer patientId, Integer encounterId, Integer unitId,
			Integer organizationId) throws ApplicationException {
		try{
			String query =" SELECT ent.encounter_id as \"encounterId\", "
					+ " to_char(ent.encounter_date,'dd/mm/yyyy') as \"encounterDateString\", "
					+ " ent.doctor_id as \"doctorId\", "
					+ " concat('Dr. ', coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "
					+ " docSpec.speciality_id as \"specialityId\", "
					+ " speciality.speciality_name as \"specialityName\" "
					+ " FROM opd.t_encounter_master ent "
					+ " INNER JOIN doctor.m_doctor_master doc on ent.doctor_id=doc.doctor_id "
					//+ " INNER JOIN doctor.t_doctor_speciality_mapper docSpec on docSpec.doctor_id=doc.doctor_id "
					+ " INNER JOIN doctor.m_speciality_master speciality on speciality.speciality_id=ent.dept_id "
					+ " WHERE ent.organization_id=:organizationId "
					//+ " AND ent.unit_id=:unitId "
					+ " AND ent.encounter_id=:encounterId "
					+ " AND ent.patient_id=:patientId ";

					
					List<MedicationHistoryDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
							//.setParameter("unitId",unitId)
							.setParameter("organizationId",organizationId)
							.setParameter("patientId", patientId)
							.setParameter("encounterId", encounterId)
							.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
					return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getMedicationEncountersList failed", he);
		} catch (Exception e) {
		logger.error("getMedicationEncountersList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveJustificationForMedicationStopForHistory(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			MedicationHistory medicationHistory=findById(medicationHistoryDto.getMedicationHistoryId());
			medicationHistory.setIsMedicationStopHis(medicationHistoryDto.getIsMedicationStopHis());
			medicationHistory.setIsMedicationContinue('N');
			medicationHistory.setJustificationStopHis(medicationHistoryDto.getJustificationStopHis());
			medicationHistory.setUpdatedBy(medicationHistoryDto.getUpdatedBy());
			medicationHistory.setUpdatedDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 
		update(medicationHistory);
	
        return new Response(SUCCESS, SUCCESS_CODE, "Medication discontinued successfully.", null, null);
		
		} catch (HibernateException he) {
		logger.error("saveJustificationForMedicationStopForHistory failed", he);
		} catch (Exception e) {
		logger.error("saveJustificationForMedicationStopForHistory failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getContinuedMedicationList(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			String pattern = "yyyy-MM-dd";
		    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		    String toDayDate = simpleDateFormat.format(TimeZoneComponent.getDateByzone(medicationHistoryDto.getUnitId()));
		    
			StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_CONTINUED_MEDICATION_LIST").getQueryString());
			
		List<MedicationHistoryDto> list=sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
				.setParameter("organizationId", medicationHistoryDto.getOrganizationId())
				.setParameter("unitId", medicationHistoryDto.getUnitId())
				.setParameter("patientId", medicationHistoryDto.getPatientId())
				.setParameter("expDate", toDayDate)
				.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
			
			
	} catch (HibernateException he) {
	logger.error("getContinuedMedicationList failed", he);
	} catch (Exception e) {
	logger.error("getContinuedMedicationList failed", e);
	}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getMedicationHistoryListForBookView(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try{
			List<MedicationHistoryDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MEDICATION_HISTORY_LIST_FOR_BOOKVIEW")
					.setParameter("organizationId", medicationHistoryDto.getOrganizationId())
					.setParameter("patientId", medicationHistoryDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		} catch (HibernateException he) {
		logger.error("getMedicationHistoryListForBookView failed", he);
		} catch (Exception e) {
		logger.error("getMedicationHistoryListForBookView failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getMasterListOfICPCSystemForCoversheet(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			List<ICPCSystemDTO> icpcMasterDtos = null;
			
			if(medicationHistoryDto.getIcpcSyatemId()!=null && medicationHistoryDto.getIcpcSyatemId()>0) {
				String query = "SELECT icpc.icpc_id  as \"icpcId\", "
						 + "icpc.icpc_code  as \"icpcCode\", "
						 + "icpc.icpc_desc  as \"icpcDesc\", "
						 + "icpc.icpc_system_id  as \"icpcSystemId\"  "
						 + "FROM emr.m_icpc icpc "
						 + "WHERE icpc.status='A' "
						 + "AND icpc.icpc_system_id=:icpcSyatemId "
						 + "AND lower(icpc.icpc_desc) LIKE '%"+medicationHistoryDto.getSearchICPC().toString().toLowerCase().trim()+"%' ";
			
			icpcMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("icpcSyatemId",medicationHistoryDto.getIcpcSyatemId())
					.setResultTransformer(Transformers.aliasToBean(ICPCSystemDTO.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, icpcMasterDtos, null);
			}else{
				String query = "SELECT icpc.icpc_id  as \"icpcId\", "
						 + "icpc.icpc_code  as \"icpcCode\", "
						 + "icpc.icpc_desc  as \"icpcDesc\", "
						 + "icpc.icpc_system_id  as \"icpcSystemId\"  "
						 + "FROM emr.m_icpc icpc "
						 + "WHERE icpc.status='A'"
//						 + "AND	icpc.icpc_id = :icpcId "
						 + "AND lower(icpc.icpc_desc) LIKE '%"+medicationHistoryDto.getSearchICPC().toString().toLowerCase().trim()+"%' ";
			
			icpcMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setResultTransformer(Transformers.aliasToBean(ICPCSystemDTO.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, icpcMasterDtos, null);
			}
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getMasterListOfICPCSystemForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getMasterListOfICPCSystemForCoversheetForCode(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			List<ICPCSystemDTO> icpcMasterDtos = null;
			
		
				String query = "SELECT icpc.icpc_id  as \"icpcId\", "
						 + "icpc.icpc_code  as \"icpcCode\", "
						 + "icpc.icpc_desc  as \"icpcDesc\", "
						 + "sys.icpc_system_desc  as \"systemName\", "
						 + "icpc.icpc_system_id  as \"icpcSystemId\"  "
						 + "FROM emr.m_icpc icpc "
						 + "inner join emr.m_icpc_system sys on sys.icpc_system_id=icpc.icpc_system_id "
						 + "WHERE icpc.status='A'"
						 + "AND	icpc.icpc_id = :icpcId ";
//						 + "AND lower(icpc.icpc_desc) LIKE '%"+medicationHistoryDto.getSearchICPC().toString()+"%'";
			
			icpcMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("icpcId",medicationHistoryDto.getIcpcId())
					.setResultTransformer(Transformers.aliasToBean(ICPCSystemDTO.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, icpcMasterDtos, null);
			
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getMasterListOfICPCSystemForCoversheetForCode failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getDetailedListForAdministrationWorklist(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getBatchListForAdministrationWorklist(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			List<MedicationHistoryDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery("select bth.batch_no as \"serialNumber\" ," + 
							" bth.id as \"batchId\", " + 
							" to_char(bth.expiry_date, 'YYYY-MM-DD') as \"expiryDate\", " + 
							" itmMppr.store_id as \"storeId\", bth.uom_unit_id as \"uomUnitId\" " + 
							" from inventory.t_batch  bth" + 
							" inner join public.m_store_item_mapper itmMppr on itmMppr.item_id = bth.item_id" + 
							" where bth.item_id =:itemId and bth.unit_id=:unitId and  bth.organization_id =:organizationId " + 
							" and bth.is_deleted=false " + 
							" and itmMppr.store_id=:storeId")
					.setParameter("unitId", medicationHistoryDto.getUnitId())
					.setParameter("organizationId", medicationHistoryDto.getOrganizationId())
					.setParameter("itemId", medicationHistoryDto.getItemId())
					.setParameter("storeId", medicationHistoryDto.getStoreId())
					.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
		logger.error("getBatchListForAdministrationWorklist failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUomListByBatchIdForAdministrationWorklist(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			List<MedicationHistoryDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery("select distinct(bthMppr.uom_type_id) as \"uomTypeId\", bthMppr.uom_unit_id as \"uomUnitId\" ,"
									+ " utyp.code as \"unitTypeCode\" , uUnit.unit as \"unitDesc\", "
									+ " stomap.available_qty as \"availableQty\" " + 
									"   from inventory.t_batch_uom_mapper bthMppr "
									+ " inner join inventory.t_batch  bth on bth.id=bthMppr.batch_id " + 
									"	inner join public.m_uom_type utyp on utyp.id=bthMppr.uom_type_id" + 
									"	inner join  public.m_uom_unit	uUnit on uUnit.id=bthMppr.uom_unit_id "
									+ " inner join inventory.t_store_stock stomap on stomap.batch_id = bthMppr.batch_id" + 
									"	where bthMppr.organization_id =:organizationId"
									+ " AND bthMppr.unit_id=:unitId"
									+ " AND bthMppr.batch_id=:batchId "
									+ " and bth.is_deleted = false" + 
									"	and bth.is_active = true" + 
									"	and bth.is_quarantine = false" + 
									"	and bth.is_pharmacy = false" + 
									"	and stomap.store_id =:storeId"
//									+ " order by utyp.id desc"
									)
					.setParameter("batchId", medicationHistoryDto.getBatchId())
					.setParameter("unitId", medicationHistoryDto.getUnitId())
					.setParameter("organizationId", medicationHistoryDto.getOrganizationId())
					.setParameter("storeId", medicationHistoryDto.getStoreId())
					.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
			}catch (Exception e) {
			logger.error("getUomListByBatchIdForAdministrationWorklist failed", e);
			}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR,null, null);
		}

	@Override
	public Response getSubstituteListForActiveMedication(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
			List<MedicationHistoryDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery("SELECT " + 
							" pres.id as \"prescriptionId\", " + 
							"  presDtl.is_medication_stop as \"isMedicationStop\", " + 
							" presDtl.justification_stop as \"justificationStop\", " + 
							" presDtl.item_id as \"itemId\", " + 
							" itm.name as \"itemName\", " + 
							" preFreDtl.dose as \"doseDesc\", " + 
							" su.unit as \"strengthDesc\", " + 
							" su.id as \"doseUnitId\", " + 
							" preFreDtl.frequency_id as \"frequencyId\", " + 
							" freq.description as \"frequencyDesc\", " + 
							" preFreDtl.route_id as \"roaId\", " + 
							" presDtl.statflag as \"statflag\", " + 
							" roa.description as \"roaDesc\", " + 
							" presDtl.order_qty as \"orderQty\", " + 
							" to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," + 
							" to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", " + 
							" presDtl.instruction_id as \"instructionId\" ," + 
							" presDtl.instruction_desc as \"instructionText\" ," + 
							" instr.instruction_desc as \"instructionDesc\", " + 
							" presDtl.id as \"prescriptionDetailsid\"," + 
							" concat('Dr. ', coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"doctorName\", "+
							" preFreDtl.id as \"prescriptionFrequencyId\" , " + 
							" gen.name as \"genericName\" " + 
							" FROM pharmacy.t_prescription pres " + 
							" left JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id " + 
							" left JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id " + 
							" left JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " + 
							" left JOIN public.m_item itm on itm.id=presDtl.item_id " + 
							" left JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id " + 
							" left JOIN public.m_generic gen on itm.generic_id = gen.id " + 
							" left JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id " + 
							" LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id " + 
							" left JOIN doctor.m_doctor_master doc ON doc.doctor_id=pres.doctor_id " + 
							" WHERE presDtl.parent_substitute_id=:parentSubstituteId " + 
							" AND presDtl.is_medication_stop='N'"
							+ " AND presDtl.organization_id=:organizationId"
									)
					.setParameter("parentSubstituteId", medicationHistoryDto.getParentSubstituteId())
					.setParameter("organizationId", medicationHistoryDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
			}catch (Exception e) {
			logger.error("getSubstituteListForActiveMedication failed", e);
			}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR,null, null);
		}

	@Override
	public Response getBrandListByGenericIdForVaccinationChart(MedicationHistoryDto medicationHistoryDto)
			throws ApplicationException {
		try {
		String query = "select itm.id as \"itemId\", itm.name as \"name\" ," + 
				" gen.name as \"genericName\"" + 
				" from public.m_item itm" + 
				" inner join public.m_generic gen on gen.id=itm.generic_id" + 
				" where itm.generic_id=:genericId";
		
		
		List<MedicationDTO> listMedicationDto = sessionFactory.getCurrentSession().createSQLQuery(query)
//				.setParameter("itemId", medicationHistoryDto.getItemId())
				.setParameter("genericId", medicationHistoryDto.getGenericId())
				.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
		return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);

	} catch (HibernateException he) {
		throw new HibernateException(he.getMessage());
	} catch (Exception e) {
	logger.error("getBrandListByGenericIdForVaccinationChart failed", e);
	}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}


	@Override
	public Response getPrescriptionDetailsList(Integer prescriptionId, Integer storeId) {
			
			try {
				String query =  "select  " + 
						"	distinct itm.id as \"itemId\" ,  " + 
						"	itm.generic_id as \"genericId\",  " + 
						"	saleDtl.quantity as \"consumableQuantity\",  " + 
						"	cast(saleDtl.id as int4) as \"saleDetailId\",  " + 
						"	tb.batch_no as \"batchNo\",  " + 
						"	cast(tb.id as int4) as \"batchId\",  " + 
						"	to_char(tb.expiry_date, 'DD/MM/YYYY') as \"expiryDate\" ,  " + 
						"	to_char(bdl.administration_date, 'DD/MM/YYYY') as \"administrationDate\",  " + 
						"	bdl.administration_time as \"administrationTime\",  " + 
						"	bdl.administration_remark as \"administrationRemark\",  " + 
						"	bdl.administration_worklist_time_details_id as \"administrationWorklistTimeDetailsId\" ,  " + 
						"	itm.name as \"itemName\",  " + 
						"	gen.name as \"genericName\" ,  " + 
						"	preFreDtl.dose as \"doseDesc\",  " + 
						"	pdl.order_qty as \"quantity\",  " + 
						"	cast(pdl.id as int) as \"prescriptionDetailId\" ,  " + 
						"	cast(pres.id as int) as \"prescriptionId\" ,  " + 
						"	pdl.uom_unit_id as \"uomUnitId\",  " + 
						"	pdl.uom_type_id as \"uomTypeId\",  " + 
						"	gen.product_category_id as \"productCategoryId\",  " + 
						"	su.unit as \"strengthDesc\" ,  " + 
						"	roa.description as \"roaDesc\" ,  " + 
						"	itmMppr.store_id as \"storeId\",  " + 
						"	pdl.status_id as \"statusId\",  " + 
						"	x.unit as \"unitName\" ,  " + 
						"	itm.is_high_risk_product as \"isHighRiskProduct\",  " + 
						"	bdl.administrator_qty as \"administratorQty\",  " + 
						"	presDose.id as \"prescribeDoseUnitId\",  " + 
						"	presDose.unit as \"prescribeDoseUnit\",  " + 
						"	pdl.prescribe_dose as \"prescribeDose\",  " + 
						"	fdm.dosage_form as \"dosageForm\",  " + 
						"	pdl.factor_id as \"factorId\",  " + 
						"	pdl.formulation_dose_mapp_id as \"formulationDosageMappId\" ,"
						+ " pdl.duration as \"duration\", " + 
						"	pdl.duration_unit as \"durationUnit\" ,"
						+ " mpc.antibiotic_test  as \"antibioticTest\","
						+ " pdl.is_medication_stop as \"isMedicationStop\" " + 
						"from  " + 
						"	public.m_item itm  " + 
						"inner join public.m_generic gen on  " + 
						"	itm.generic_id = gen.id  "
						+ " inner join m_pharmacological_classification mpc  on mpc.id = itm.pharmacological_classification_id  " + 
						"inner join pharmacy.t_prescription_detail pdl on  " + 
						"	itm.id = pdl.item_id  " + 
						"inner join pharmacy.t_prescription pres on  " + 
						"	pres.id = pdl.prescription_id  " + 
						"inner join pharmacy.t_prescription_frequency_detail preFreDtl on  " + 
						"	preFreDtl.prescription_detail_id = pdl.id  " + 
						"inner join public.m_store_item_mapper itmMppr on  " + 
						"	itmMppr.item_id = itm.id  " + 
						"left join pharmacy.t_sale_detail saleDtl on  " + 
						"	pdl.id = saleDtl.prescription_detail_id  " + 
						"left join inventory.t_batch tb on  " + 
						"	tb.id = saleDtl.batch_id  " + 
						"left join public.m_uom_unit x on  " + 
						"	x.id = saleDtl.uom_unit_id  " + 
						"left join pharmacy.t_administration_worklist_time_details bdl on  " + 
						"		bdl.prescription_detail_id = pdl.id " + 
						"left join public.m_strength_unit su on  " + 
						"	preFreDtl.dose_unit_id = su.id  " + 
						"left join public.m_route_of_administration roa on  " + 
						"	roa.id = preFreDtl.route_id  " + 
						"left join public.m_strength_unit presDose on  " + 
						"	pdl.prescribe_dose_unit_id = presDose.id  " + 
						"left join public.m_fomulation_dosage_mapper fdm on  " + 
						"	fdm.formulation_dosage_mapp_id = pdl.formulation_dose_mapp_id"
						+" where  pres.id= :prescriptionId" + 
						" and itmMppr.store_id=:storeId and pdl.statflag = true ";
				
				
				List<AdministrationWorklistDto> listMedicationDto = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("prescriptionId", prescriptionId)
						.setParameter("storeId",storeId)
						.setResultTransformer(Transformers.aliasToBean(AdministrationWorklistDto.class)).list();
			
				return new Response(SUCCESS, SUCCESS_CODE, null, listMedicationDto, null);
			
			
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getPrescriptionDetailsList failed", e);
		}
		return null;
	}

	@Override
	public Response getAvailableStokeByItemId(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			List<MedicationHistoryDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery("select " + 
							"sum(S.available_qty) " + 
							"from " + 
							"inventory.t_batch b " + 
							"inner join inventory.t_store_stock S on " + 
							" B.id = S.batch_id " + 
							" where b.item_id =:itemId " + 
							" and s.store_id in (" + 
							" select " + 
							" store_id" + 
							" from " + 
							" m_store_item_mapper msim " + 
							" where " + 
							" msim.item_id =:itemId " + 
							" and unit_id =:unitId " + 
							" and store_id in (" + 
							" select " + 
							" ms.store_id " + 
							" from " + 
							" m_store ms " + 
							" where ms.unit_id =:unitId and ms.is_pharmacy=true)) " + 
							" and b.unit_id =:unitId " + 
							" and s.unit_id =:unitId " + 
							" and b.is_quarantine=false "
									)
					.setParameter("itemId", medicationHistoryDto.getItemId())
					.setParameter("unitId", medicationHistoryDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
			}catch (Exception e) {
			logger.error("getAvailableStokeByItemId failed", e);
			}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR,null, null);
		}

	@Override
	public Response getPatientMedicationHistoryForDoctorApp(MedicationHistoryDto medicationHistoryDto) {
		try {
			String query = "SELECT "
					+ "pres.id as \"prescriptionId\", "
					+ "  presDtl.is_medication_stop as \"isMedicationStop\", " + 
					" presDtl.justification_stop as \"justificationStop\", "
					+ " pres.encounter_id as \"encounterId\", "
					+ " pres.admission_id as \"admissionId\", "
					+ " presDtl.item_id as \"itemId\", "
					+ " itm.name as \"itemName\", "
					+ " preFreDtl.dose as \"doseDesc\", "
					+ " su.unit as \"strengthDesc\", " 
					+ " preFreDtl.frequency_id as \"frequencyId\", "
					+ " freq.description as \"frequencyDesc\", "
					+ " preFreDtl.route_id as \"roaId\", "
					+ " presDtl.statflag as \"statflag\", "
					+ " roa.description as \"roaDesc\", "
					+ " presDtl.order_qty as \"orderQty\", "
					+ " to_char(preFreDtl.added_date,'dd/mm/yyyy') as \"createdDate\"," 
					+ " to_char(preFreDtl.start_date,'dd/mm/yyyy') as \"startDate\"," 
					+ " to_char(preFreDtl.stop_date,'dd/mm/yyyy') as \"endDate\", "
					+ " to_char(pres.updated_date,'dd/mm/yyyy') as \"updatedDate\", "
					+ " presDtl.instruction_id as \"instructionId\" ,"
					+ " instr.instruction_desc as \"instructionDesc\", "
					+ " presDtl.instruction_desc \"instructionText\", "
					+ " presDtl.id as \"prescriptionDetailsid\","
					+ " preFreDtl.id as \"prescriptionFrequencyId\" , "
					+ " gen.name as \"genericName\" "
					+ " FROM pharmacy.t_prescription pres "
					+ " INNER JOIN pharmacy.t_prescription_detail presDtl ON presDtl.prescription_id=pres.id "
					+ " INNER JOIN pharmacy.t_prescription_frequency_detail preFreDtl ON preFreDtl.prescription_detail_id=presDtl.id "
					+ " LEFT JOIN public.m_strength_unit su ON preFreDtl.dose_unit_id = su.id " 
					+ " INNER JOIN public.m_item itm on itm.id=presDtl.item_id "
					+ " LEFT JOIN public.m_route_of_administration roa on roa.id=preFreDtl.route_id "
					+ " INNER JOIN public.m_generic gen on itm.generic_id = gen.id "
					+ " INNER JOIN public.m_frequency freq on freq.id=preFreDtl.frequency_id "
					+ " LEFT JOIN public.m_instruction instr on instr.instruction_id=presDtl.instruction_id "
					+ " WHERE pres.patient_id=:patientId "
					+ " AND (presDtl.is_medication_stop='Y' "
					+ " or to_char(preFreDtl.stop_date,'YYYY-MM-DD')<:prescriptionDate) ";

			
			List<MedicationDTO> orderedMedicationHistory= getSession().createSQLQuery(query)
			.setParameter("patientId", medicationHistoryDto.getPatientId())
			.setParameter("prescriptionDate", CommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(medicationHistoryDto.getUnitId()), "yyyy-MM-dd"))
			.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
			
			StringBuilder stringQuery=new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_MEDICATION_HISTORY_LIST_FOR_DOCTOR_APP").getQueryString());
			List<MedicationHistoryDto> medicationHistoryList=sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
				.setParameter("organizationId", medicationHistoryDto.getOrganizationId())
				.setParameter("unitId", medicationHistoryDto.getUnitId())
				.setParameter("patientId", medicationHistoryDto.getPatientId())
				.setResultTransformer(Transformers.aliasToBean(MedicationHistoryDto.class)).list();
			
			MedicationHistoryAppDto dto=new MedicationHistoryAppDto();
			if(medicationHistoryList!=null && !medicationHistoryList.isEmpty()) {
				dto.setMedicationHistoryList(medicationHistoryList);
			}
			
            if(orderedMedicationHistory!=null && !orderedMedicationHistory.isEmpty()) {
        	  dto.setOrderedMedicationHistory(orderedMedicationHistory);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, null, dto);
			
			
	} catch (HibernateException he) {
	logger.error("getPatientMedicationHistoryForDoctorApp failed", he);
	} catch (Exception e) {
	logger.error("getPatientMedicationHistoryForDoctorApp failed", e);
	}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response deleteMedicationHistory(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			
			String query ="update opd.t_medication_history set status='I' "
					+" where medication_history_id="+medicationHistoryDto.getMedicationHistoryId()
					+" and organization_id="+medicationHistoryDto.getOrganizationId()
					+" and unit_id ="+medicationHistoryDto.getUnitId();
			sessionFactory.getCurrentSession()
				.createSQLQuery(query)
				.executeUpdate();
		
			return new Response(SUCCESS, SUCCESS_CODE, "Deleted Successfully", null, null);
	} catch (Exception e) {
	logger.error("deleteMedicationHistory failed", e);
	}
		return new Response(ERROR, COMMON_ERROR, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateMedicationHistory(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			
			/*MedicationHistory medicationHistory = mapper.map(medicationHistoryDto, MedicationHistory.class, "MedicationHistoryDto_to_MedicationHistory");*/
		 MedicationHistory medicationHistory=findById(medicationHistoryDto.getMedicationHistoryId());
		 medicationHistory.setDoctorId(medicationHistoryDto.getDoctorId());
		 medicationHistory.setDoseUnitId(medicationHistoryDto.getDoseUnitId());
		 medicationHistory.setStoreId(medicationHistoryDto.getStoreId() );
		 medicationHistory.setStatus(medicationHistoryDto.getStatus());
		 medicationHistory.setPatientId(medicationHistoryDto.getPatientId());
		 medicationHistory.setEncounterId(medicationHistoryDto.getEncounterId());
		 medicationHistory.setAdmissionId(medicationHistoryDto.getAdmissionId());
		 medicationHistory.setDoseId(medicationHistoryDto.getDoseId());
		 medicationHistory.setDuration(medicationHistoryDto.getDuration());
		 medicationHistory.setDurationUnit(medicationHistoryDto.getDurationUnit());
		 medicationHistory.setDoseUnitId(medicationHistoryDto.getDoseUnitId());
		 medicationHistory.setFrequencyId(medicationHistoryDto.getFrequencyId());
		 medicationHistory.setUnitId(medicationHistoryDto.getUnitId());
		 medicationHistory.setOrganizationId(medicationHistoryDto.getOrganizationId());
		 medicationHistory.setCreatedBy(medicationHistoryDto.getCreatedBy());
		 medicationHistory.setGenericId(medicationHistoryDto.getGenericId());
		 medicationHistory.setInstructionId(medicationHistoryDto.getInstructionId());
		 medicationHistory.setItemId(medicationHistoryDto.getItemId());
		 medicationHistory.setGenericName(medicationHistoryDto.getGenericName());
		 medicationHistory.setItemName(medicationHistoryDto.getItemName());
		 medicationHistory.setIsNonFormulary(medicationHistoryDto.getIsNonFormulary());
		 medicationHistory.setOrderQty(medicationHistoryDto.getOrderQty());
		 medicationHistory.setRoaId(medicationHistoryDto.getRoaId());
		 medicationHistory.setIsMedicationContinue(medicationHistoryDto.getIsMedicationContinue());
		 medicationHistory.setIsMedicationStopHis(medicationHistoryDto.getIsMedicationStopHis());
		 medicationHistory.setJustificationStopHis(medicationHistoryDto.getJustificationStopHis());
		 medicationHistory.setIsNoSignificantMedication(medicationHistoryDto.getIsNoSignificantMedication());
		 medicationHistory.setUpdatedBy(medicationHistoryDto.getUpdatedBy());
		 medicationHistory.setCreatedDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
		 medicationHistory.setUpdatedDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
		 if(medicationHistoryDto.getAdmissionId()!=null){
			 medicationHistory.setStartDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getStartDate(), "yyyy-M-dd HH:mm:ss"));
			 if(medicationHistoryDto.getEndDate()!="Invalid date") {
				 medicationHistory.setEndDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getEndDate(), "yyyy-M-dd HH:mm:ss"));
			 }else {
				 medicationHistory.setEndDate(null);
			 }
		 }else{
		 medicationHistory.setStartDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getStartDate(), "dd-M-yyyy HH:mm:ss"));
		 if(medicationHistoryDto.getEndDate() != "Invalid date") {
			 medicationHistory.setEndDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getEndDate(), "dd-M-yyyy HH:mm:ss"));
		 }
		 
		 }
		 medicationHistory.setAuthorisedDate(GlobalCommonDateUtils.getDate(medicationHistoryDto.getAuthorisedDate(), "yyyy-M-dd HH:mm:ss"));
		 update(medicationHistory);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateMedicationHistory failed", he);
		} catch (Exception e) {
		logger.error("updateMedicationHistory failed", e);
		} 
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
}

	@Override
	public Response saveCurrentMedicationHistory(List<MedicationHistoryDto> list, CoversheetTemplateMasterDto resDto)
			throws ApplicationException {
		try{
			
			for (MedicationHistoryDto dto : list){
				if(dto.getCovTemplateId() == null){
				 MedicationHistory medicationHistory=new MedicationHistory();
				 medicationHistory.setDoctorId(resDto.getDoctorId());
				 medicationHistory.setDoseUnitId(dto.getDoseUnitId());
				 medicationHistory.setStoreId(dto.getStoreId() );
				 medicationHistory.setStatus(resDto.getStatus());
//				 medicationHistory.setPatientId(resDto.getPatientId());
//				 medicationHistory.setEncounterId(resDto.getEncounterId());
//				 medicationHistory.setAdmissionId(resDto.getAdmissionId());
				 medicationHistory.setDoseId(dto.getDoseId());
				 medicationHistory.setDuration(dto.getDuration());
				 medicationHistory.setDurationUnit(dto.getDurationUnit());
				 medicationHistory.setDoseUnitId(dto.getDoseUnitId());
				 medicationHistory.setFrequencyId(dto.getFrequencyId());
				 medicationHistory.setUnitId(resDto.getUnitId());
				 medicationHistory.setOrganizationId(resDto.getOrganizationId());
				 medicationHistory.setCreatedBy(resDto.getCreatedBy());
				 medicationHistory.setGenericId(dto.getGenericId());
				 medicationHistory.setInstructionId(dto.getInstructionId());
				 medicationHistory.setItemId(dto.getItemId());
				 medicationHistory.setGenericName(dto.getGenericName());
				 medicationHistory.setItemName(dto.getItemName());
				 medicationHistory.setIsNonFormulary(dto.getIsNonFormulary());
				 medicationHistory.setOrderQty(dto.getOrderQty());
				 medicationHistory.setRoaId(dto.getRoaId());
				 medicationHistory.setIsMedicationContinue(dto.getIsMedicationContinue());
				 medicationHistory.setIsMedicationStopHis(dto.getIsMedicationStopHis());
				 medicationHistory.setJustificationStopHis(dto.getJustificationStopHis());
				 medicationHistory.setIsNoSignificantMedication(dto.getIsNoSignificantMedication());
				 medicationHistory.setUpdatedBy(dto.getUpdatedBy());
				 medicationHistory.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
				 medicationHistory.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
				 if(dto.getAdmissionId()!=null){
					 medicationHistory.setStartDate(GlobalCommonDateUtils.getDate(dto.getStartDate(), "yyyy-M-dd HH:mm:ss"));
					 if(dto.getEndDate()!="Invalid date") {
						 medicationHistory.setEndDate(GlobalCommonDateUtils.getDate(dto.getEndDate(), "yyyy-M-dd HH:mm:ss"));
					 }else {
						 medicationHistory.setEndDate(null);
					 }
				 }else{
				 medicationHistory.setStartDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				 if(dto.getEndDate() != "Invalid date") {
					 medicationHistory.setEndDate(GlobalCommonDateUtils.getDate(new Date(), "dd-M-yyyy HH:mm:ss"));
				 }

				 }
				 medicationHistory.setAuthorisedDate(GlobalCommonDateUtils.getDate(new Date(), "yyyy-M-dd HH:mm:ss"));
				 save(medicationHistory);
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
				}/*else {
					 MedicationHistory medicationHistory=findById(resDto.getCovTemplateId());
					 medicationHistory.setDoctorId(resDto.getDoctorId());
					 medicationHistory.setDoseUnitId(dto.getDoseUnitId());
					 medicationHistory.setStoreId(dto.getStoreId() );
					 medicationHistory.setStatus(resDto.getStatus());
//					 medicationHistory.setPatientId(resDto.getPatientId());
//					 medicationHistory.setEncounterId(resDto.getEncounterId());
//					 medicationHistory.setAdmissionId(resDto.getAdmissionId());
					 medicationHistory.setDoseId(dto.getDoseId());
					 medicationHistory.setDuration(dto.getDuration());
					 medicationHistory.setDurationUnit(dto.getDurationUnit());
					 medicationHistory.setDoseUnitId(dto.getDoseUnitId());
					 medicationHistory.setFrequencyId(dto.getFrequencyId());
					 medicationHistory.setUnitId(resDto.getUnitId());
					 medicationHistory.setOrganizationId(resDto.getOrganizationId());
					 medicationHistory.setCreatedBy(resDto.getCreatedBy());
					 medicationHistory.setGenericId(dto.getGenericId());
					 medicationHistory.setInstructionId(dto.getInstructionId());
					 medicationHistory.setItemId(dto.getItemId());
					 medicationHistory.setGenericName(dto.getGenericName());
					 medicationHistory.setItemName(dto.getItemName());
					 medicationHistory.setIsNonFormulary(dto.getIsNonFormulary());
					 medicationHistory.setOrderQty(dto.getOrderQty());
					 medicationHistory.setRoaId(dto.getRoaId());
					 medicationHistory.setIsMedicationContinue(dto.getIsMedicationContinue());
					 medicationHistory.setIsMedicationStopHis(dto.getIsMedicationStopHis());
					 medicationHistory.setJustificationStopHis(dto.getJustificationStopHis());
					 medicationHistory.setIsNoSignificantMedication(dto.getIsNoSignificantMedication());
					 medicationHistory.setUpdatedBy(resDto.getUpdatedBy());
					 medicationHistory.setCreatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					 medicationHistory.setUpdatedDate(TimeZoneComponent.getDateByzone(resDto.getUnitId()));
					 if(dto.getAdmissionId()!=null){
						 medicationHistory.setStartDate(GlobalCommonDateUtils.getDate(dto.getStartDate(), "yyyy-M-dd HH:mm:ss"));
						 if(dto.getEndDate()!="Invalid date") {
							 medicationHistory.setEndDate(GlobalCommonDateUtils.getDate(dto.getEndDate(), "yyyy-M-dd HH:mm:ss"));
						 }else {
							 medicationHistory.setEndDate(null);
						 }
					 }else{
					 medicationHistory.setStartDate(GlobalCommonDateUtils.getDate(dto.getStartDate(), "dd-M-yyyy HH:mm:ss"));
					 if(dto.getEndDate() != "Invalid date") {
						 medicationHistory.setEndDate(GlobalCommonDateUtils.getDate(dto.getEndDate(), "dd-M-yyyy HH:mm:ss"));
					 }
					 
					 }
					 medicationHistory.setAuthorisedDate(GlobalCommonDateUtils.getDate(dto.getAuthorisedDate(), "yyyy-M-dd HH:mm:ss"));
					 save(medicationHistory);
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
					}*/
				}
				
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateCovTempCurrentMedicationHistory(List<MedicationHistoryDto> list,
			CoversheetTemplateMasterDto coversheetTemplateMasterDto) {
		try{
			
			for (MedicationHistoryDto dto : list){
				if(dto.getCovTemplateId() != null){
					 MedicationHistory medicationHistory=findById(coversheetTemplateMasterDto.getCovTemplateId());
					 medicationHistory.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
					 medicationHistory.setDoseUnitId(dto.getDoseUnitId());
					 medicationHistory.setStoreId(dto.getStoreId() );
					 medicationHistory.setStatus(coversheetTemplateMasterDto.getStatus());
//					 medicationHistory.setPatientId(resDto.getPatientId());
//					 medicationHistory.setEncounterId(resDto.getEncounterId());
//					 medicationHistory.setAdmissionId(resDto.getAdmissionId());
					 medicationHistory.setDoseId(dto.getDoseId());
					 medicationHistory.setDuration(dto.getDuration());
					 medicationHistory.setDurationUnit(dto.getDurationUnit());
					 medicationHistory.setDoseUnitId(dto.getDoseUnitId());
					 medicationHistory.setFrequencyId(dto.getFrequencyId());
					 medicationHistory.setUnitId(coversheetTemplateMasterDto.getUnitId());
					 medicationHistory.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					 medicationHistory.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
					 medicationHistory.setGenericId(dto.getGenericId());
					 medicationHistory.setInstructionId(dto.getInstructionId());
					 medicationHistory.setItemId(dto.getItemId());
					 medicationHistory.setGenericName(dto.getGenericName());
					 medicationHistory.setItemName(dto.getItemName());
					 medicationHistory.setIsNonFormulary(dto.getIsNonFormulary());
					 medicationHistory.setOrderQty(dto.getOrderQty());
					 medicationHistory.setRoaId(dto.getRoaId());
					 medicationHistory.setIsMedicationContinue(dto.getIsMedicationContinue());
					 medicationHistory.setIsMedicationStopHis(dto.getIsMedicationStopHis());
					 medicationHistory.setJustificationStopHis(dto.getJustificationStopHis());
					 medicationHistory.setIsNoSignificantMedication(dto.getIsNoSignificantMedication());
					 medicationHistory.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
					 medicationHistory.setCreatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					 medicationHistory.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()));
					 if(dto.getAdmissionId()!=null){
						 medicationHistory.setStartDate(GlobalCommonDateUtils.getDate(dto.getStartDate(), "yyyy-M-dd HH:mm:ss"));
						 if(dto.getEndDate()!="Invalid date") {
							 medicationHistory.setEndDate(GlobalCommonDateUtils.getDate(dto.getEndDate(), "yyyy-M-dd HH:mm:ss"));
						 }else {
							 medicationHistory.setEndDate(null);
						 }
					 }else{
					 medicationHistory.setStartDate(GlobalCommonDateUtils.getDate(dto.getStartDate(), "dd-M-yyyy HH:mm:ss"));
					 if(dto.getEndDate() != "Invalid date") {
						 medicationHistory.setEndDate(GlobalCommonDateUtils.getDate(dto.getEndDate(), "dd-M-yyyy HH:mm:ss"));
					 }
					 
					 }
					 medicationHistory.setAuthorisedDate(GlobalCommonDateUtils.getDate(dto.getAuthorisedDate(), "yyyy-M-dd HH:mm:ss"));
					 save(medicationHistory);
				
						return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
					}
				}
		
		
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
		}catch(Exception e){
			
			throw e;
		}

	
	
}
	@Override
	public Response 	getFavriteListOfICPCSystemForCoversheet(MedicationHistoryDto medicationHistoryDto) throws ApplicationException {
		try {
			List<ICPCSystemDTO> icpcMasterDtos = null;
				String query = "SELECT icpc.icpc_id  as \"icpcId\", "
						+ "icpc.icpc_code  as \"icpcCode\", "
						+ "icpc.icpc_desc  as \"icpcDesc\", "
						+ "icpc.icpc_system_id  as \"icpcSystemId\" "
						+ "FROM emr.m_icpc icpc "
						+ "INNER JOIN emr.m_fav_icpc ficpc ON ficpc.icpc_id = icpc.icpc_id "
						+ "WHERE icpc.status = 'A' "
						+ "AND ficpc.organization_id = :organizationId "
						+ "AND ficpc.status = 'A' "
						+ "AND ficpc.unit_id = :unitId ";
				icpcMasterDtos = sessionFactory.getCurrentSession().createSQLQuery(query)
						.setParameter("organizationId",medicationHistoryDto.getOrganizationId())
						.setParameter("unitId",medicationHistoryDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(ICPCSystemDTO.class)).list();
				return new Response(SUCCESS, SUCCESS_CODE, null, icpcMasterDtos, null);
		} catch (HibernateException he) {
			throw new HibernateException(he.getMessage());
		} catch (Exception e) {
		logger.error("getFavriteListOfICPCSystemForCoversheet failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


}
