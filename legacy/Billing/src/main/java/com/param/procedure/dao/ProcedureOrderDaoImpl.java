package com.param.procedure.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

import com.google.gson.Gson;
import com.param.global.dao.IScheduleLogsDao;
import com.param.global.dto.MyLifeConnectionLinkDto;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dto.CommonDto;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.otims.dao.IImplantDetailsDao;
import com.param.global.otims.dao.IMedicationDetailsDao;
import com.param.global.otims.dto.OTCompletionNoteDto;
import com.param.global.otims.dto.OTManagerDashboardDto;
import com.param.global.otims.dto.OtcCompletionInvImplantsDto;
import com.param.global.otims.dto.OtcCompletionInvMedicationDto;
import com.param.global.otims.dto.ProcedureOrderChecklistDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.otims.dto.ProcedureSchedulingDto;
import com.param.otims.model.ProcedureOrderMaster;
import com.param.procedure.dto.CssdKitVO;
import com.param.procedure.dto.OtCompletionEquipmentDto;
import com.param.procedure.dto.ProcedureOrderListReqDto;
import com.param.procedure.dto.ProcedureOrderListResDto;
import com.param.procedure.dto.ProcedureOrderServSrchReqDto;
import com.param.procedure.dto.ProcedureOrderServSrchResDto;
import com.param.procedure.dto.ProcedureRequestDto;
import com.param.procedure.dto.ProcedureRes;
import com.param.procedure.dto.VOProcedureOrderMasterDto;
import com.param.utility.TimeZoneComponent;
import java.net.URI;
import java.util.List;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.springframework.web.client.RestTemplate;

@Repository
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class ProcedureOrderDaoImpl extends GenericDao<ProcedureOrderMaster, Integer> implements ICommonConstants, IProcedureOrderDao{

	public ProcedureOrderDaoImpl() {
		super(ProcedureOrderMaster.class);
	}

	@Autowired
	IMedicationDetailsDao iMedicationDetailsDao;
	
	@Autowired
	private IImplantDetailsDao iImplantDetailsDao;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private IOrderDetailsMasterDao iOrderDetailsMasterDao;

	@Autowired
	private Gson gson;

	@Autowired
	RestTemplate restTemplate;

	@Autowired
	IScheduleLogsDao iScheduleLogsDao;
	
	
	@Override
	public Response getProcedureOrderById(int proReqId) throws ServiceException {
		try{
			List<ProcedureOrderMasterDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_ORDER_DETAILS_BY_ID").setParameter("orderId", proReqId).setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response searchProcedureServiceForOrder(ProcedureOrderServSrchReqDto reqDto) throws ApplicationException {
		try{
			Date effectiveDate = GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "dd-M-yyyy");
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("SEARCH_SERVICES_FOR_PROCEDURE_ORDER").getQueryString());
			query.append(reqDto.getProcedureTypeId() != null ? " and proconf.procedure_type_id =:procedureTypeId " : " ");
			query.append(reqDto.getSpecialityId() != null ? " and srvmst.speciality_id =:specialityId " : " ");
			query.append(" and srvmppr.is_procedure =:isProcedure ");
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameter("serviceName", "%" + reqDto.getServiceName().trim().toLowerCase() + "%")
					.setParameter("isProcedure", reqDto.getIsProcedure());
					//.setParameter("tariffId", reqDto.getTariffId())
					//.setParameter("paymentEntitlementId", reqDto.getPaymentEntitlementId())
					//.setParameter("visitTypeId", reqDto.getVisitTypeId())
					//.setParameter("patientTypeId", reqDto.getPatientTypeId())
					//.setParameter("billingBedCategoryId", reqDto.getBillingBedCategoryId())
					//.setParameter("effectiveDate", effectiveDate)

			if(reqDto.getProcedureTypeId() != null)
				sqlQuery.setParameter("procedureTypeId", reqDto.getProcedureTypeId());

			if(reqDto.getSpecialityId() != null)
				sqlQuery.setParameter("specialityId", reqDto.getSpecialityId());

			List<ProcedureOrderServSrchResDto> list = sqlQuery.setResultTransformer(Transformers.aliasToBean(ProcedureOrderServSrchResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getProcedureOrderListByStatus(ProcedureOrderListReqDto procedureOrderListReqDto)throws ApplicationException {
		try{
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PROCEDURE_ORDER_LIST")
					.getQueryString();
			StringBuffer subQuery = new StringBuffer();
			if(procedureOrderListReqDto.getFromDate()!=null && procedureOrderListReqDto.getToDate()!=null){
				subQuery.append(" WHERE proreq.prefered_date_time between :fromDate and :toDate ");
			}
			if(procedureOrderListReqDto.getVisitTypeId()!=0){
				subQuery.append(subQuery.length()!=0?" AND ":" Where ");
				subQuery.append(" proreq.visit_type_id =:visitTypeId ");
			}
			if(procedureOrderListReqDto.getOrderStatusId()!=0){
				subQuery.append(subQuery.length()!=0?" AND ":" Where ");
				subQuery.append(" proreq.procedure_status_id =:orderStatusId ");
			}
			subQuery.append(subQuery.length()!=0?" AND ":" Where ");
			subQuery.append(" proreq.org_id =:orgId and proreq.unit_id =:unitId ");
			
			query = query.concat(subQuery.toString());
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", procedureOrderListReqDto.getOrgId())
					.setParameter("unitId", procedureOrderListReqDto.getUnitId());

			if(procedureOrderListReqDto.getFromDate()!=null && procedureOrderListReqDto.getToDate()!=null){
				sqlQuery.setParameter("fromDate", procedureOrderListReqDto.getFromDate());
				sqlQuery.setParameter("toDate", procedureOrderListReqDto.getToDate());
			}

			if(procedureOrderListReqDto.getVisitTypeId()!=0)
				if(!subQuery.isEmpty())
					sqlQuery.setParameter("visitTypeId", procedureOrderListReqDto.getVisitTypeId());

			if(procedureOrderListReqDto.getOrderStatusId()!=0)
				if(!subQuery.isEmpty())
					sqlQuery.setParameter("orderStatusId", procedureOrderListReqDto.getOrderStatusId());

			List<ProcedureOrderListResDto> listProcedureOrderListResDto = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderListResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listProcedureOrderListResDto, null);
		}catch(HibernateException he){
		}catch(Exception e){
			
			throw e;
		}
		return null;
	}

	public Response saveProcedureOrder(ProcedureOrderMasterDto procedureOrderMasterDto)
			throws ServiceException {
		try{

			String Global_Scheduling_URI = null;
			String MY_LIFE_URI = null;
			String uuid = null;
			URI urlMyLife = null;
			Response responseMyLife = null;
			Response commonResponseMyLife = null;
			Boolean isOPDPatient = false;
			Boolean isSyncSchedulingData = false;
			Integer channelId = null;
			String token = null;
			List<ProcedureOrderMasterDto> serviceIdAndNameList = new ArrayList<>();


			if(procedureOrderMasterDto.getEncounterId() != null && procedureOrderMasterDto.getEncounterId()>0) {
				Boolean isSyncScheduling = (Boolean) sessionFactory.getCurrentSession()
						.createSQLQuery("  select om.is_sync_scheduling as \"isSyncScheduling\" from public.m_organization_master om where  om.organization_id=:organizationId and om.status='A' ")
						.setParameter("organizationId", procedureOrderMasterDto.getOrgId())
						.uniqueResult();

				isSyncSchedulingData = isSyncScheduling;

				channelId = (Integer) sessionFactory.getCurrentSession()
						.createSQLQuery("  select org.channel_id as \"channelId\" from public.m_organization_master org where  org.organization_id=:organizationId and org.status='A' ")
						.setParameter("organizationId", procedureOrderMasterDto.getOrgId())
						.uniqueResult();

				uuid = (String) sessionFactory.getCurrentSession()
						.createSQLQuery("select patReg.uuid from patient.t_patient_registration patReg "
								+ "	where patReg.patient_id=:patientId and patReg.organization_id=:orgId ")
						.setParameter("patientId", procedureOrderMasterDto.getPatientId())
						.setParameter("orgId", procedureOrderMasterDto.getOrgId())
						.uniqueResult();

				if (isSyncScheduling) {
					Response res = iScheduleLogsDao.getConnectionStringForMyLife();
					MyLifeConnectionLinkDto connectionLinkDto = (MyLifeConnectionLinkDto) res.getObject();
					if (connectionLinkDto != null && connectionLinkDto.getMyLifeLink() != null && connectionLinkDto.getMyLifeLink() != "" && connectionLinkDto.getGlobalSchedulingLink() != null && connectionLinkDto.getGlobalSchedulingLink() != "") {
						Global_Scheduling_URI = connectionLinkDto.getGlobalSchedulingLink();
						MY_LIFE_URI = connectionLinkDto.getMyLifeLink();
					}

					String sqlQuery = "  select mst.service_standard_name as \"serviceName\", mst.service_master_id as \"serviceId\" from  service.m_service_master mst "
							+	  " where  mst.organization_id=:organizationId "
							+	  "	and mst.status='A' and mst.service_master_id=:serviceId";

					StringBuffer sb = new StringBuffer(sqlQuery);
					serviceIdAndNameList = getSession().createSQLQuery(sb.toString())
							.setParameter("organizationId", procedureOrderMasterDto.getOrgId())
							.setParameter("serviceId", procedureOrderMasterDto.getServiceId())
							.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();

				}
			}

			if(procedureOrderMasterDto.getProcedureOrderId() !=null) {
			
				Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_master set status='I'  where procedure_order_id =:procedureOrderId ")
						.setParameter("procedureOrderId", procedureOrderMasterDto.getProcedureOrderId())
						.executeUpdate();
			}

			ProcedureOrderMaster master = new ProcedureOrderMaster();
			master.setServiceId(procedureOrderMasterDto.getServiceId());
			master.setPreferedDateTime(procedureOrderMasterDto.getPreferedDateTime() != null && !procedureOrderMasterDto.getPreferedDateTime().isEmpty() ? GlobalCommonDateUtils.getDate(procedureOrderMasterDto.getPreferedDateTime(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureOrderMasterDto.getUnitId()));
			master.setIsPrimarySecondary(procedureOrderMasterDto.getIsPrimarySecondary());
			master.setAmount(procedureOrderMasterDto.getAmount()==null?(procedureOrderMasterDto.getRate() == null?0:procedureOrderMasterDto.getRate()):procedureOrderMasterDto.getAmount());
			master.setUrgency(procedureOrderMasterDto.getUrgency());
			master.setRequestReason(procedureOrderMasterDto.getRequestReason());
			master.setStatus('A');
			master.setIsIcuRequired(procedureOrderMasterDto.getIsIcuRequired());
			master.setIsBloodComponentRequired(procedureOrderMasterDto.getIsBloodComponentRequired());
			master.setIsPacRequired(procedureOrderMasterDto.getIsPacRequired());
			master.setOrgId(procedureOrderMasterDto.getOrgId());
			master.setUnitId(procedureOrderMasterDto.getUnitId());
			master.setCreatedBy(procedureOrderMasterDto.getCreatedBy());
			master.setCreatedDate(procedureOrderMasterDto.getCreatedDate() != null && !procedureOrderMasterDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureOrderMasterDto.getCreatedDate(), "dd-MM-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureOrderMasterDto.getUnitId()));
			master.setUpdatedBy(procedureOrderMasterDto.getUpdatedBy());
			master.setUpdatedDate(procedureOrderMasterDto.getUpdatedDate() != null && !procedureOrderMasterDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureOrderMasterDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureOrderMasterDto.getUnitId()));
			master.setProReqId(procedureOrderMasterDto.getProReqId());
			master.setExportedToOtims(false);
			master.setProcedureSequence(procedureOrderMasterDto.getProcedureSequence()!=null ? procedureOrderMasterDto.getProcedureSequence(): 1);;
			master.setEmergency(procedureOrderMasterDto.getEmergancy()!=null?procedureOrderMasterDto.getEmergancy():false);
			if(procedureOrderMasterDto.getPreferedTime()!=null) {

				LocalTime preferedTime1 = LocalTime.parse(procedureOrderMasterDto.getPreferedTime());
				LocalDate today = LocalDate.now();
				Instant preferedTime = preferedTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				master.setPreferedTime(preferedTime);
			}else {
				master.setPreferedTime(null);
			}
			
			sessionFactory.getCurrentSession().save(master);
			if(isSyncSchedulingData && (procedureOrderMasterDto.getEncounterId() != null && procedureOrderMasterDto.getEncounterId()>0)){

				procedureOrderMasterDto.setUuid(uuid != null ? uuid: "0");
				procedureOrderMasterDto.setHospitalId(procedureOrderMasterDto.getOrganizationId() != null ? procedureOrderMasterDto.getOrganizationId() : procedureOrderMasterDto.getOrgId());

				if(serviceIdAndNameList != null && !serviceIdAndNameList.isEmpty()) {
					ProcedureOrderMasterDto objectDto = serviceIdAndNameList.stream()
							.filter(order -> procedureOrderMasterDto.getServiceId().equals(order.getServiceId()))
							.findAny()
							.orElse(null);
					if (objectDto != null) {
						procedureOrderMasterDto.setServiceName(objectDto.getServiceName());
					} else {
						procedureOrderMasterDto.setServiceName(null);
					}
				}

				try {
					if(MY_LIFE_URI!= null) {
						urlMyLife = URI.create(MY_LIFE_URI + "/saveProcedureOrder");
						HttpHeaders headers = new HttpHeaders();

						headers.set("channelId", (channelId != null && channelId != 0) ? channelId.toString() : "1");
						HttpEntity<ProcedureOrderMasterDto> request = new HttpEntity<>(procedureOrderMasterDto, headers);
						String stringResponse = restTemplate.postForObject(urlMyLife, request, String.class);
						commonResponseMyLife = gson.fromJson(stringResponse, Response.class);
					}


				}catch(Exception e){
					return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
				}
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}

	@Override
	public Response searchServicesForProcedureOrder(ProcedureOrderServSrchReqDto reqDto) throws ApplicationException {
		try{
			Date effectiveDate = GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(reqDto.getUnitId()), "dd-M-yyyy");
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("SEARCH_SERVICES_FOR_PROCEDURE_SERVICE_ORDERING").getQueryString());
			query.append(reqDto.getSpecialityId() != null ? " and srvmst.speciality_id =:specialityId " : " ");
			query.append(" and srvmppr.is_procedure =:isProcedure ");
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("orgId", reqDto.getOrgId())
					.setParameter("unitId", reqDto.getUnitId())
					.setParameter("serviceName", "%" + reqDto.getServiceName().trim().toLowerCase() + "%")
					.setParameter("tariffId", reqDto.getTariffId())
					.setParameter("paymentEntitlementId", reqDto.getPaymentEntitlementId())
					.setParameter("visitTypeId", reqDto.getVisitTypeId())
					.setParameter("patientTypeId", reqDto.getPatientTypeId())
					.setParameter("billingBedCategoryId", reqDto.getBillingBedCategoryId())
					.setParameter("effectiveDate", effectiveDate)
					.setParameter("isProcedure", reqDto.getIsProcedure());

			if(reqDto.getSpecialityId() != null)
				sqlQuery.setParameter("specialityId", reqDto.getSpecialityId());

			List<ProcedureOrderServSrchResDto> list  = sqlQuery.setResultTransformer(Transformers.aliasToBean(ProcedureOrderServSrchResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getProcReqDetailsById(int id) throws ApplicationException {
		try{
			List<ProcedureOrderMasterDto> listdto = sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_REQ_DETAILS_BY_ID")
					.setParameter("id", id).setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listdto, null);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	public Response searchProcedureOrder(ProcedureRequestDto procedureRequestDto) throws ApplicationException {
		try {
	/*		MedicationDetailsDto  medicationDetailsDto = new MedicationDetailsDto();
			ImplantDetailsDto implantDetailsDto = new ImplantDetailsDto();*/
			
			String query = sessionFactory.getCurrentSession()
					.createSQLQuery(" select  distinct ON(prm.pro_req_id)prm.pro_req_id as \"proReqId\", " + 
							"pr.uhid_number as \"uhid\"," + 
							"pom.org_id as \"orgId\"," + 
							"pom.unit_id as \"unitId\"," + 
							"concat(coalesce(prefix_mst.prefix_code),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"patientName\"," + 
							" concat(coalesce(doc.first_name,''),' ',coalesce(doc.middle_name,''),' ',coalesce(doc.last_name,'')) as \"doctorName\", " +
							"pr.patient_id as \"patientId\"," + 
							"pr.image_path as \"imagePath\", "+
							"pr.gender_id as \"genderId\", "+
							"gender_mst.gender_code as \"genderCode\", " +
							"(extract(year from age(current_timestamp,	pr.dob))|| 'Y ' || extract(month from age(current_timestamp,pr.dob))|| 'M ') as \"age\", "+
							"prm.admission_id as \"admissionId\"," + 
							"  to_char(adtDtls.doa, 'dd/MM/yyyy') as \"doa\", "+
							"  to_char(adtDtls.pdd, 'dd/MM/yyyy') as \"pdd\", "+
							"  to_char(pr.dob, 'dd/MM/yyyy') as \"dob\", "+
							" bm.bed_number as \"bedNumber\", "+
							"prm.encounter_id as \"encounterId\"," + 
						//	"to_char(prm.prefered_date_time,'MM-DD-YYYY HH:MI:SS') as \"preferedDateTime\"," + 
							"to_char(prm.request_date,'MM-DD-YYYY HH:MI:SS') as \"requestDate\"," + 
							"prm.request_by as \"requestBy\"," + 
							"scpemstr.speciality_id as \"specialityId\" ," + 
							"scpemstr.speciality_name as \"specialityName\", "
							+" (case when prm.admission_id is not null then pdmin.visit_type_id " + 
							"			else 1 " + 
							"		end " + 
							"	) as \"visitTypeId\","+
							//"to_char(prm.prefered_time,'HH:MI:SS') as \"preferedTime\", " + 
							"to_char(pdmin.created_date,'MM-DD-YYYY HH:MI:SS') as \"admintionDate\" ," +
							" ward.ward_name as \"wardName\" "+
							"from otims.t_procedure_order_master pom " + 
							"inner join service.m_service_master sm on pom.service_id = sm.service_master_id " + 
							"inner join otims.t_procedure_req_mst prm on pom.pro_req_id = prm.pro_req_id " + 
							"left join adt.t_admission pdmin on pdmin.admission_id = prm.admission_id " +
							"left join adt.t_admission_details adtDtls on pdmin.admission_id = adtDtls.admission_id and adtDtls.status='A' "+
							" left JOIN adt.m_bed_master bm on adtDtls.bed_id = bm.bed_id "  +
							"left join doctor.m_doctor_master doc on doc.user_id = prm.created_by " +
							" left join adt.m_ward_master ward on ward.ward_id = bm.ward_id "+
							"inner join patient.t_patient_registration pr on prm.patient_id = pr.patient_id " + 
							"inner join otims.t_procedure_configuration pc on sm.service_master_id = pc.service_id " + 
							"inner join public.m_gender_master gender_mst on	pr.gender_id = gender_mst.gender_id " + 
							"inner join public.m_prefix_master prefix_mst on pr.prefix_id = prefix_mst.prefix_id " + 
							"inner join doctor.m_speciality_master scpemstr on pc.speciality_id = scpemstr.speciality_id " + 
                            "where pom.exported_to_otims=false and prm.procedure_status_id = 1  and pom.status='A' and (prm.admission_id is not null or prm.encounter_id is not null)")

					.getQueryString();
			
			StringBuffer subQuery = new StringBuffer();
			if(procedureRequestDto.getFromDate()!=null && procedureRequestDto.getToDate()!=null){
				subQuery.append(" and to_char(prm.prefered_date_time,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			if(procedureRequestDto.getVisitTypeId()!=0){
				subQuery.append(" and  prm.visit_type_id =:visitTypeId ");
			}
			subQuery.append(" and  pom.org_id =:orgId and pom.unit_id =:unitId ");
			
			query = query.concat(subQuery.toString());
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", procedureRequestDto.getOrgId())
					.setParameter("unitId", procedureRequestDto.getUnitId());

			if(procedureRequestDto.getFromDate()!=null && procedureRequestDto.getToDate()!=null) {
				sqlQuery.setParameter("fromDate", procedureRequestDto.getFromDate());
				sqlQuery.setParameter("toDate", procedureRequestDto.getToDate());
			}

			if(procedureRequestDto.getVisitTypeId()!=0)
				sqlQuery.setParameter("visitTypeId", procedureRequestDto.getVisitTypeId());

			List<ProcedureRequestDto> procedureReqlist = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(ProcedureRequestDto.class)).list();
			
					
			for (ProcedureRequestDto reqList : procedureReqlist) {

				
				List<VOProcedureOrderMasterDto> procedurelist = sessionFactory.getCurrentSession()
						.createSQLQuery("select pom.procedure_order_id as \"procedureOrderId\","
								+ "pom.is_primary_secondary as \"isPrimarySecondary\","
								+ "pom.service_id as \"serviceId\","
								+ "pom.org_id as \"orgId\", pom.unit_id as \"unitId\", "
								+ "pom.is_blood_component_required as \"isBloodComponentRequired\","
								+ "sm.service_standard_name as \"procedureName\","
								+ "pc.operation_theatre_type_id as \"operationTheatreTypeId\" , "
								+ "ptm.procedure_type_name as \"procedureType\","
								+ "pc.duration as \"duration\","
								+ "to_char(pom.prefered_date_time,'YYYY-MM-DD') as \"preferedDateTime\"," 
								+ "to_char(pom.prefered_time,'hh24:mi:ss') as \"preferedTime\", " 
							/*	+ "staff.role_id as \"roleId\","
								+ " rolemstr.role_name as \"roleName\","*/
								+ " ot.theatre_type_name as \"theatreTypeName\" "
								+ "from otims.t_procedure_order_master pom "
								+ "inner join service.m_service_master sm on pom.service_id = sm.service_master_id "
								+ "inner join otims.t_procedure_req_mst prm on pom.pro_req_id = prm.pro_req_id "
								+ "inner join patient.t_patient_registration pr on prm.patient_id = pr.patient_id "
								+ "inner join otims.t_procedure_configuration pc on sm.service_master_id = pc.service_id "
								+ "inner join otims.m_procedure_type_master ptm on ptm.procedure_type_id = pc.procedure_type_id "
							/*	+ "left join otims.t_procedure_order_staff staff on  pom.procedure_order_id = staff.procedure_order_id " 
								+ "left join public.m_otims_role_master rolemstr on staff.role_id = rolemstr.role_id "*/
								+ "left join otims.m_operation_theatre_type ot  on ot.operation_theatre_type_id = pc.operation_theatre_type_id "  
								+ "where pom.pro_req_id =:proReqId "
								+" and pom.status='A' order by pom.procedure_order_id  asc")
						.setParameter("proReqId", reqList.getProReqId())
						.setResultTransformer(Transformers.aliasToBean(VOProcedureOrderMasterDto.class)).list();

				reqList.setProcedureOrderMaster(procedurelist);
			}

			return new Response<>(SUCCESS, SUCCESS_CODE, null, procedureReqlist, null);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getRoleListInOTBilling(int orgId, int orgUnitId) throws ApplicationException {
		try
		{
			List<CommonDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_ROL_LIST_PROCEDURE")
					.setParameter("orgId", orgId)
					.setParameter("unitId", orgUnitId)
					.setResultTransformer(Transformers.aliasToBean(CommonDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
		}
	}

	@Override
	public Response getProcedureOrderList(int encounterId) throws ApplicationException {
		try
		{
			List<ProcedureOrderMasterDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_LIST_OF_PROCEDURE_ORDER")
					.setParameter("encounterId", encounterId)
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response searchProcedureAndPatientDetailsList(OTManagerDashboardDto otManagerDashboardDto)
			throws ApplicationException {
		try{
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("SEARCH_PATIENT_AND_PROCEDURE_DETAILS")
					.getQueryString();
			StringBuffer subQuery = new StringBuffer();
			if(otManagerDashboardDto.getFromDate()!=null && otManagerDashboardDto.getToDate()!=null){
				subQuery.append(" WHERE to_char(prm.request_date,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			if(otManagerDashboardDto.getSpecialityId()!=null){
				subQuery.append(subQuery.length()!=0?" AND ":" Where ");
				subQuery.append(" spl.speciality_id =:specialityId ");
			}
			if(otManagerDashboardDto.getDoctorId()!=null){
				subQuery.append(subQuery.length()!=0?" AND ":" Where ");
				subQuery.append(" dm.doctor_id =:doctorId ");
			}
			if(otManagerDashboardDto.getProcedureName()!=null){
				subQuery.append(subQuery.length()!=0?" AND ":" Where ");
				subQuery.append(" service_standard_name =:procedureName ");
			}
			if(otManagerDashboardDto.getPriority()!=null){
				if(otManagerDashboardDto.getPriority().equalsIgnoreCase("Normal"))
				{
				subQuery.append(subQuery.length()!=0?" AND ":" Where ");
				subQuery.append("pom.urgency = 0");
				}
				else
				{
					subQuery.append(subQuery.length()!=0?" AND ":" Where ");
					subQuery.append("pom.urgency = 1");
				}
			}
			subQuery.append(subQuery.length()!=0?" AND ":" Where ");
			subQuery.append(" pom.org_id =:orgId and pom.unit_id =:unitId and pom.status='A' ");
			subQuery.append(" AND (implant.is_exported = false OR medication.is_exported = false ) ");
		
			query = query.concat(subQuery.toString());
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("orgId", otManagerDashboardDto.getOrganizationId())
					.setParameter("unitId", otManagerDashboardDto.getUnitId());

			if(otManagerDashboardDto.getFromDate()!=null && otManagerDashboardDto.getToDate()!=null) {
				sqlQuery.setParameter("fromDate", otManagerDashboardDto.getFromDate());
				sqlQuery.setParameter("toDate", otManagerDashboardDto.getToDate());
			}

			if(otManagerDashboardDto.getSpecialityId()!=null)
				if(!subQuery.isEmpty())
					sqlQuery.setParameter("specialityId", otManagerDashboardDto.getSpecialityId());

			if(otManagerDashboardDto.getDoctorId()!=null)
				if(!subQuery.isEmpty())
					sqlQuery.setParameter("doctorId", otManagerDashboardDto.getDoctorId());

			if(otManagerDashboardDto.getProcedureName()!=null)
				if(!subQuery.isEmpty())
					sqlQuery.setParameter("procedureName", otManagerDashboardDto.getProcedureName());

			List<OTManagerDashboardDto> listPatientAndProcedureDetailsList = sqlQuery
					.setFirstResult(otManagerDashboardDto.getOffset() != null ? otManagerDashboardDto.getOffset() : 0)
					.setMaxResults(otManagerDashboardDto.getNoOfRecordsPerPage() != null ? otManagerDashboardDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(OTManagerDashboardDto.class)).list();
			for(OTManagerDashboardDto ls : listPatientAndProcedureDetailsList)
			{
				ls.setIsImplant(0);
				ls.setIsKit(0);
				ls.setImplantDetailsId(0);
				if(ls.getUrgency()==0)
				{
					ls.setPriority("Normal");
				}
				else
				{
					ls.setPriority("Emergency");
				}
			}
			int count = 0 ;
			if(listPatientAndProcedureDetailsList!=null && listPatientAndProcedureDetailsList.size()>0) {
				String queryForCount = sessionFactory.getCurrentSession()
						.getNamedQuery("SEARCH_PATIENT_AND_PROCEDURE_DETAILS").getQueryString();
				StringBuffer subQueryForCount = new StringBuffer();
				if(otManagerDashboardDto.getFromDate()!=null && otManagerDashboardDto.getToDate()!=null){
					subQueryForCount.append(" WHERE to_char(prm.request_date,'YYYY-MM-DD') between :fromDate and :toDate ");
				}
				if(otManagerDashboardDto.getSpecialityId()!=null){
					subQueryForCount.append(subQueryForCount.length()!=0?" AND ":" Where ");
					subQueryForCount.append(" spl.speciality_id =:specialityId ");
				}
				if(otManagerDashboardDto.getDoctorId()!=null){
					subQueryForCount.append(subQueryForCount.length()!=0?" AND ":" Where ");
					subQueryForCount.append(" dm.doctor_id =:doctorId ");
				}
				if(otManagerDashboardDto.getProcedureName()!=null){
					subQueryForCount.append(subQueryForCount.length()!=0?" AND ":" Where ");
					subQueryForCount.append(" service_standard_name =:procedureName ");
				}
				if(otManagerDashboardDto.getPriority()!=null){
					if(otManagerDashboardDto.getPriority().equalsIgnoreCase("Normal"))
					{
						subQueryForCount.append(subQueryForCount.length()!=0?" AND ":" Where ");
						subQueryForCount.append("pom.urgency = 0");
					}
					else
					{
						subQueryForCount.append(subQueryForCount.length()!=0?" AND ":" Where ");
						subQueryForCount.append("pom.urgency = 1");
					}
				}
				subQueryForCount.append(subQueryForCount.length()!=0?" AND ":" Where ");
				subQueryForCount.append(" pom.org_id =:orgId and pom.unit_id =:unitId and pom.status='A' ");
				subQueryForCount.append(" AND (implant.is_exported = false OR medication.is_exported = false ) ");
			
				queryForCount = queryForCount.concat(subQueryForCount.toString());

				Query sqlQueryCount = sessionFactory.getCurrentSession().createSQLQuery(queryForCount)
						.setParameter("orgId", otManagerDashboardDto.getOrganizationId())
						.setParameter("unitId", otManagerDashboardDto.getUnitId());

				if(otManagerDashboardDto.getFromDate()!=null && otManagerDashboardDto.getToDate()!=null) {
					sqlQueryCount.setParameter("fromDate", otManagerDashboardDto.getFromDate());
					sqlQueryCount.setParameter("toDate", otManagerDashboardDto.getToDate());
				}

				if(otManagerDashboardDto.getSpecialityId()!=null)
					if(!subQuery.isEmpty())
						sqlQueryCount.setParameter("specialityId", otManagerDashboardDto.getSpecialityId());

				if(otManagerDashboardDto.getDoctorId()!=null)
					if(!subQuery.isEmpty())
						sqlQueryCount.setParameter("doctorId", otManagerDashboardDto.getDoctorId());

				if(otManagerDashboardDto.getProcedureName()!=null)
					if(!subQuery.isEmpty())
						sqlQueryCount.setParameter("procedureName", otManagerDashboardDto.getProcedureName());

				List<OTManagerDashboardDto> listForCount = sqlQueryCount
						.setResultTransformer(Transformers.aliasToBean(OTManagerDashboardDto.class)).list();
				
				count = listForCount.size();
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPatientAndProcedureDetailsList, count);
		}catch(Exception e){
			
			throw e;
		}
	}
		
	@Override
	public Response procedureOrderUpdate(ProcedureOrderMasterDto procedureOrderMasterDto) {
		try
		{	
			int proc_id=procedureOrderMasterDto.getProcedureOrderId();
			
			//System.out.println("proc_id    "+proc_id);
			List<ProcedureOrderMasterDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_ORDER_ID")
					.setParameter("procedureOrderId", proc_id)
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
		  
			for(ProcedureOrderMasterDto procedureOrderMasterDto1:list) {
			ProcedureOrderMaster master = new ProcedureOrderMaster();
			master.setProcedureOrderId(procedureOrderMasterDto1.getProcedureOrderId());
			master.setServiceId(procedureOrderMasterDto1.getServiceId());
			master.setPreferedDateTime(GlobalCommonDateUtils.getDate(procedureOrderMasterDto1.getPreferedDateTime(), "dd-M-yyyy HH:mm:ss"));
			//master.setPreferedTime(GlobalCommonDateUtils.getLocalTime(procedureOrderMasterDto.getPreferedTime()));
			master.setIsPrimarySecondary(procedureOrderMasterDto1.getIsPrimarySecondary());
			master.setAmount(procedureOrderMasterDto1.getAmount()==null?0:procedureOrderMasterDto1.getAmount());
			master.setUrgency(procedureOrderMasterDto1.getUrgency());
			master.setRequestReason(procedureOrderMasterDto1.getRequestReason());
			master.setIsIcuRequired(procedureOrderMasterDto1.getIsIcuRequired());
			master.setIsBloodComponentRequired(procedureOrderMasterDto1.getIsBloodComponentRequired());
			master.setIsPacRequired(procedureOrderMasterDto1.getIsPacRequired());
			master.setOrgId(procedureOrderMasterDto1.getOrgId());
			master.setUnitId(procedureOrderMasterDto1.getUnitId());
			master.setCreatedBy(procedureOrderMasterDto1.getCreatedBy());
			master.setCreatedDate(GlobalCommonDateUtils.getDate(procedureOrderMasterDto1.getCreatedDate(), "dd-MM-yyyy HH:mm:ss"));
			master.setUpdatedBy(procedureOrderMasterDto1.getUpdatedBy());
			master.setUpdatedDate(procedureOrderMasterDto1.getUpdatedDate() != null && !procedureOrderMasterDto1.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureOrderMasterDto1.getUpdatedDate(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureOrderMasterDto1.getUnitId()));
			master.setProReqId(procedureOrderMasterDto1.getProReqId());
			master.setExportedToOtims(true);
			update(master);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);

		} catch (Exception e)
		{
			
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getProcedureRequestId(int orderId) throws ApplicationException {
		try{
			ProcedureOrderMasterDto dto = (ProcedureOrderMasterDto)sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_REQUEST_ID_BY_ORDER_ID")
					.setParameter("procedureOrderId", orderId).setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, dto);
		}catch(Exception e){
			
			throw e;
		}
	}
	@Override
	public Response getprocedureOrderbyVisitandDate(ProcedureOrderMasterDto procedureOrderMasterDto) {
		try{
			List<ProcedureSchedulingDto> list = sessionFactory.getCurrentSession().createSQLQuery(
			"select " 
					+" distinct pr.uhid_number as \"uhidNumber\", " 
					+" concat(coalesce(prefix_mst.prefix_code),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"patientName\", " 
				//	+" concat(coalesce(doc.first_name,''),' ',coalesce(doc.middle_name,''),' ',coalesce(doc.last_name,'')) as \"doctorName\", " 
					+" pr.patient_id as \"patientId\", "
					+ " (case when prm.admission_id is not null then admission.doctor_id " + 
					"			else encounter.doctor_id " + 
					"		end " + 
					"	) as \"doctorId\","
					+ " (case when "
					+ "    encounterDoc.doctor_id is not null then concat( coalesce( encounterDoc.first_name, '' ), ' ', coalesce( encounterDoc.middle_name, '' ), ' ', coalesce( encounterDoc.last_name, '' )) " + 
					"	 else concat( coalesce( adtDoc.first_name, '' ), ' ', coalesce( adtDoc.middle_name, '' ), ' ', coalesce( adtDoc.last_name, '' )) " + 
					"		end " + 
					"	) as \"doctorName\"," 
					+"	pr.gender_id as \"genderId\", "
					+"	pr.image_path as \"imagePath\", "
					+ "  to_char(adtDtls.doa, 'dd/MM/yyyy') as \"doa\", "
					+ "  to_char(adtDtls.pdd, 'dd/MM/yyyy') as \"pdd\", "
					+ "to_char(pr.dob,'DD/MM/YYYY') as \"birthDate\", "
					+ "to_char(prm.created_date,'DD-MM-YYYY') as \"createdDate\", "
					+" schedule.total_duration as \"totalDuration\", " 
					+" prm.admission_id as \"admissionId\", " 
					+" prm.encounter_id as \"encounterId\", " 
					+" prm.pro_req_id as \"proReqId\", " 
					+" ot.theatre_name as \"theatreName\", "
					+" schedule.ot_type as \"otType\", " 
					+" schedule.procedure_schedule_id as \"procedureScheduleId\", " 
					+" tbl.table_name as \"tableName\", "
					+" encounter.encounter_number as \"encounterNumber\", "
					+" admission.admission_number as \"admissionNumber\", "
					+" bm.bed_number as \"bedNumber\", "
					+" ward.ward_name as \"wardName\", "
					+ " prm.visit_type_id as \"visitTypeId\", "
					+" to_char(schedule.prefer_time,'hh24:mi:ss') as \"preferTime\", "
					+" gender.gender_code as \"genderCode\", "
					+" concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ') as \"age\", "
					+" to_char(schedule.schedule_date, 'DD-MM-YYYY')  as \"scheduleDate\" , " 
					+" schedule.procedure_type as \"procedureType\", "
					+" prm.procedure_status_id as \"proceduerStatusId\", "
				//	+" schedule.proceduer_status_id as \"proceduerStatusId\", "
					+" prost.status as \"procedureStatusName\","
					+" proConfig.anesthesia_type_id as \"anesthesiaTypeId\","
					+" prm.emergency as \"emergency\","
					+" schedule.procedure_name as \"procedureName\" " 
					+" from otims.t_procedure_req_mst prm " 
					+" inner join otims.t_procedure_schedule schedule on prm.pro_req_id = schedule.pro_req_id " 
					+"inner join otims.m_procedure_order_status prost on prm.procedure_status_id = prost.id "
					+" inner join otims.t_procedure_configuration proConfig on prm.primary_procedure_id = proConfig.service_id " 
					+" inner join otims.t_procedure_ot_schedule otDetails on schedule.procedure_schedule_id = otDetails.procedure_schedule_id " 
					+" inner join otims.m_operation_theatre ot on otDetails.theatre_id = ot.operation_theatre_id " 
					+" inner join otims.m_operation_table tbl on otDetails.table_id = tbl.operation_table_id "
					//+" left join doctor.m_doctor_master doc on doc.user_id = prm.created_by " 
					+" inner join patient.t_patient_registration pr on prm.patient_id = pr.patient_id " 
					+" inner join public.m_prefix_master prefix_mst on pr.prefix_id = prefix_mst.prefix_id "
					+" left join adt.t_admission admission on prm.admission_id = admission.admission_id "
					+" left join adt.t_admission_details adtDtls on admission.admission_id = adtDtls.admission_id and adtDtls.status='A' "
					+" left JOIN adt.m_bed_master bm on adtDtls.bed_id = bm.bed_id "  
					+" left JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
					+" left join opd.t_encounter_master encounter on prm.encounter_id = encounter.encounter_id " 
					+" inner join public.m_gender_master gender on pr.gender_id = gender.gender_id "
					+ " left join doctor.m_doctor_master encounterDoc on encounter.doctor_id = encounterDoc.doctor_id "
					+ " left join doctor.m_doctor_master adtDoc on admission.doctor_id = adtDoc.doctor_id "
					+" where  prm.procedure_status_id in (2,3,4)  " 
					+" and schedule.organization_id =:orgId "
					+" and schedule.unit_id =:unitId "
					+" and prm.visit_type_id =:visitTypeId "
					+" and schedule.status = 'A' "
					+" and to_char(schedule.schedule_date,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate "
					+" and otDetails.status='A' "
					+" and prm.emergency = false ")
					.setParameter("orgId", procedureOrderMasterDto.getOrganizationId())
					.setParameter("unitId", procedureOrderMasterDto.getUnitId())
					.setParameter("visitTypeId", procedureOrderMasterDto.getVisitTypeId())
					.setParameter("fromDate", procedureOrderMasterDto.getFromDate())
					.setParameter("toDate", procedureOrderMasterDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(ProcedureSchedulingDto.class)).list();
			/*List<ProcedureOrderMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(
					"select pr.uhid_number as \"uhidNumber\","
					+"concat(coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"patientName\","
					+"sm.service_standard_name as \"procedureName\","
					+"ptm.procedure_type_name as \"procedureType\","
					+"prm.pro_req_id as \"proReqId\","
					+"pom.exported_to_otims as \"exportedToOtims\","
					+"prost.status as \"status\","
					+"prost.id as \"id\" "
					+"from otims.t_procedure_order_master pom " 
					+"inner join service.m_service_master sm on pom.service_id = sm.service_master_id "
					+"inner join otims.t_procedure_req_mst prm on pom.pro_req_id = prm.pro_req_id "
					+"inner join patient.t_patient_registration pr on prm.patient_id = pr.patient_id "
					+"inner join otims.t_procedure_configuration pc on sm.service_master_id = pc.service_id "
					+"inner join otims.m_procedure_type_master ptm on ptm.procedure_type_id = pc.procedure_type_id "
					+"inner join otims.m_procedure_order_status prost on prm.procedure_status_id = prost.id "
					+"where pom.is_primary_secondary='Y' and  pom.exported_to_otims=false and prm.visit_type_id="+procedureOrderMasterDto.getVisitTypeId()+" and "
					+"to_char(prm.prefered_date_time,'YYYY-MM-DD') BETWEEN '"+procedureOrderMasterDto.getFromDate()+"' AND '"+procedureOrderMasterDto.getToDate()+"' ")
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
			*/return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getProcedureOrderListByAdmissionId(int admissionId,int encounterId ,int proReqId) throws ApplicationException {
		try
		{
			///String query=sessionFactory.getCurrentSession().getNamedQuery("GET_LIST_OF_PROCEDURE_ORDER_BY_ADMISSION_ID").getQueryString();
			String q1 = " select spl.speciality_name as \"specialityName\" , " + 
					"	spl.speciality_id as \"specialityId\"," + 
					"	pom.procedure_order_id as \"procedureOrderId\"," + 
					"	pom.service_id as \"serviceId\"," + 
					"	pom.urgency as \"urgency\"," + 
					"	service_standard_name \"procedureName\"," + 
					"	to_char(pom.prefered_date_time, 'dd-MM-yyyy') as \"preferedDateTime\"," + 
					"	to_char(pom.prefered_time, 'hh24:mi') as \"preferedTime\"," + 
					"	prm.pro_req_id as \"proReqId\"," + 
					"	prm.procedure_status_id as \"procedureStatusId\"," + 
					"	pom.request_reason as \"requestReason\"," + 
					"	to_char(pom.prefered_date_time, 'yyyy') as \"year\"," + 
					"	to_char(pom.prefered_date_time, 'month') as \"month\"," + 
					"	to_char(pom.prefered_date_time, 'dd/MM/yyyy') as \"orderDateTime\"," + 
					"	pom.created_by as \"createdBy\"," + 
					"	to_char(pom.created_date, 'dd-MM-yyyy HH:MI:SS')as \"createdDate\"," +
					"	to_char(pom.created_date, 'DD/MM/YYYY')as \"createdDate1\"," +
					"	to_char(pom.updated_date, 'dd-MM-yyyy HH:MI:SS')as \"updatedDate\"," + 
					"	pom.updated_by as \"updatedBy\"," + 
					"	pom.amount as \"amount\"," + 
					"	cast(pom.is_icu_required as char(1))  as \"isIcuRequired\"," + 
					"	cast(pom.is_primary_secondary as char(1)) as \"isPrimarySecondary\"," + 
					"	cast(pom.is_blood_component_required as char(1)) as \"isBloodComponentRequired\"," + 
					"	prm.org_id as \"orgId\"," + 
					"	cast(rtrim(pom.status) as varchar(10)) as \"status\"," + 
					"	prm.unit_id as \"unitId\"," + 
					"	cast(pom.is_pac_required as char(1)) as \"isPacRequired\"," + 
					" (case " + 
					" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
					" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
					" else 'Admin' end) as \"userName\", "+
					"	dtls.procedure_type_id as \"procedureTypeId\","
					+" config.operation_theatre_type_id as \"operationTheatreTypeId\","
					+ " pom.procedure_sequence AS \"procedureSequence\"  " + 
					" from otims.t_procedure_req_mst prm " + 
					" inner join otims.t_procedure_order_master pom on " + 
					"	prm.pro_req_id = pom.pro_req_id" + 
					" inner join service.m_service_master ser on " + 
					"	ser.service_master_id = pom.service_id " + 
					" inner join doctor.m_speciality_master spl on" + 
					"	spl.speciality_id = ser.speciality_id " + 
					" inner join service.t_unit_service_master_details dtls on" + 
					"	dtls.service_master_id = pom.service_id	" + 
					" left join public.m_user_master um on prm.created_by = um.user_id "+ 
					" left join public.m_employee_master empmst on um.user_id = empmst.user_id " + 
					" left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id "+
					" inner join otims.t_procedure_configuration config on config.service_id = pom.service_id "+
					" where pom.status = 'A'  "
					+ " and dtls.procedure_type_id  is not null ";
			
			String q2 =" select distinct spl.speciality_name as \"specialityName\" ," + 
					"	spl.speciality_id as \"specialityId\"," + 
					"	0 as \"procedureOrderId\"," + 
					"	tod.service_id as \"serviceId\"," + 
					"	0 as \"urgency\"," + 
					"	service_standard_name \"procedureName\"," + 
					"	to_char(tod.order_date, 'dd-MM-yyyy') as \"preferedDateTime\"," + 
					"	to_char(tod.order_date, 'hh24:mi') as \"preferedTime\"," + 
					"	0 as \"proReqId\"," + 
					"	0 as \"procedureStatusId\"," + 
					"	'-' as \"requestReason\"," + 
					"	to_char(tod.order_date, 'yyyy') as \"year\"," + 
					"	to_char(tod.order_date, 'month') as \"month\"," + 
					"	to_char(tod.order_date, 'dd/MM/yyyy') as \"orderDateTime\"," + 
					"	tod.created_by as \"createdBy\"," + 
					"	to_char(tod.created_date, 'dd-MM-yyyy HH:MI:SS')as \"createdDate\"," +
					"	to_char(tod.created_date, 'DD/MM/YYYY')as \"createdDate1\"," +
					"	to_char(tod.updated_date, 'dd-MM-yyyy HH:MI:SS')as \"updatedDate\"," + 
					"	tod.updated_by as \"updatedBy\"," + 
					"	0 as \"amount\"," + 
					"	cast('N' as char(1)) as \"isIcuRequired\"," + 
					"	cast('N' as char(1)) as \"isPrimarySecondary\"," + 
					"	cast('N' as char(1)) as \"isBloodComponentRequired\"," + 
					"	tod.org_id as \"orgId\"," + 
					"	cast(rtrim(tod.status) as varchar(10)) as \"status\"," + 
					"	tod.org_unit_id as \"unitId\"," + 
					"	cast('N' as char(1)) as \"isPacRequired\"," + 
					" (case " + 
					" when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " + 
					" when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " + 
					" else 'Admin' end) as \"userName\", "+
					"	dtls.procedure_type_id as \"procedureTypeId\", " +
					" config.operation_theatre_type_id as \"operationTheatreTypeId\","+
					" 0 AS \"procedureSequence\"  " + 
					" from  " + 
					"	t_order_details tod " + 
					" inner join t_order_master om on" + 
					"	om.order_id = tod.order_id " + 
					" inner join service.m_service_master ser on " + 
					"	ser.service_master_id = tod.service_id " + 
					" inner join doctor.m_speciality_master spl on " + 
					"	spl.speciality_id = ser.speciality_id "
					+ "	inner join public.t_unit_speciality_mapper unitspc on " + 
					"	unitspc.speciality_id = spl.speciality_id " + 
					" inner join service.t_unit_service_master_details dtls on " + 
					"	dtls.service_master_id = tod.service_id " + 
					" left join public.m_user_master um on tod.created_by = um.user_id "+ 
					" left join public.m_employee_master empmst on um.user_id = empmst.user_id " + 
					" left join doctor.m_doctor_master docmst on um.user_id = docmst.user_id "+
					" inner join otims.t_procedure_configuration config on config.service_id = tod.service_id "+
					" where " + 
					"	tod.status = 'A' "
					+ " and dtls.procedure_type_id  is not null " + 
					"	and dtls.procedure_type_id in(1,2) "
					+ " and unitspc.is_surgical_code = 'Y'  " + 
					"	and dtls.unit_id = tod.org_unit_id ";
			
			
			StringBuffer subQuery = new StringBuffer();
			StringBuffer subQuery1 = new StringBuffer();
			int count = 0;
			if(admissionId!=0){
				subQuery.append(" and prm.admission_id =:admissionId ");
				subQuery1.append(" and om.admission_id =:admissionId ");
				count = 1 ; 
			}
			if(encounterId!=0){
				subQuery.append(" and prm.encounter_id =:encounterId ");
				subQuery1.append(" and om.encounter_id =:encounterId ");
				count = 1 ; 
			}
			if(proReqId!=0){
				subQuery.append(" and prm.pro_req_id =:proReqId ");
			}
			
			q1 = q1.concat(subQuery.toString());
			q2 = q2.concat(subQuery1.toString());
			String query = "";
			if(count == 1) {
				query = q1+ " union "+q2;	 
			}else {
				query = q1;
			}
			Query sqlQuery =sessionFactory.getCurrentSession().createSQLQuery(query);

			if(admissionId!=0)
				sqlQuery.setParameter("admissionId", admissionId);

			if(encounterId!=0)
				sqlQuery.setParameter("encounterId", encounterId);

			if(proReqId!=0)
				sqlQuery.setParameter("proReqId", proReqId);

			List<ProcedureOrderMasterDto> list = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			

		} catch (Exception e)
		{
			
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
    @Override
    public Response getMedicationList(int proReqId) throws ApplicationException {
        try
        {
            List<OtcCompletionInvMedicationDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_LIST_OF_MEDICATION_BY_PROCEDURE_REQUEST_ID")
                    .setParameter("procedureRequestId", proReqId)
                    .setResultTransformer(Transformers.aliasToBean(OtcCompletionInvMedicationDto.class)).list();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
        } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
    
    }

    @Override
    public Response getImplantsList(int proReqId) throws ApplicationException {
        try
        {
            List<OtcCompletionInvImplantsDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_LIST_OF_IMPLANT_BY_PROCEDURE_REQUEST_ID")
                    .setParameter("procedureRequestId", proReqId)
                    .setResultTransformer(Transformers.aliasToBean(OtcCompletionInvImplantsDto.class)).list();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
        } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
    }

	@Override
	public Response getProcedureByAdmisssion(int admissionId) throws ApplicationException {
		try
        {
            List<ProcedureRes> list = sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_DETAILS_BY_ADMISSION")
                    .setParameter("admissionId", admissionId)
                    .setResultTransformer(Transformers.aliasToBean(ProcedureRes.class)).list();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
        } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}


	@Override
	public Response toCheckByAdmitionId(int proReqId) throws ApplicationException {
		try
        {
			ProcedureOrderMasterDto checkAdmitionId = (ProcedureOrderMasterDto)sessionFactory.getCurrentSession().createSQLQuery(
				"select admission_id as \"admissionId\" "
				+" from otims.t_procedure_req_mst where pro_req_id =:proReqId ")
				.setParameter("proReqId", proReqId)
				.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).uniqueResult();
			if(checkAdmitionId.getAdmissionId()!=null) { 
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			}
        } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getProcedureOrderListByDoctorForEMR(int doctorId, int orgId, int orgUnitId)
			throws ApplicationException {
		try
        {
            List<ProcedureOrderMasterDto> list = sessionFactory.getCurrentSession()
            		.getNamedQuery("GET_PROCEDURE_ORDER_LIST_BY_DOCTOR_FOR_EMR")
                    .setParameter("doctorId", doctorId)
                    .setParameter("orgId", orgId)
                    .setParameter("orgUnitId", orgUnitId)
                    .setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
            return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
        } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	@SuppressWarnings("unused")
	@Override
	public Response updateAllProcedureOrderDetailsStatus(ProcedureOrderMasterDto procedureOrderMasterDto)
			throws ApplicationException {
		try{	
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_master set status='I' , reason_id =:reasonId , reason_note =:reasonNote "
					+" where procedure_order_id =:procedureOrderId ")
					.setParameter("procedureOrderId", procedureOrderMasterDto.getProcedureOrderId())
					.setParameter("reasonId", procedureOrderMasterDto.getReasonId())
					.setParameter("reasonNote", procedureOrderMasterDto.getReasonNote())
					.executeUpdate();
			if(count>0) {
				 return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			}else {
				 return new Response(SUCCESS, SUCCESS_CODE, null, null, 0);
			}
			  
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveProcedureListFromOPD(ProcedureOrderMasterDto procedureOrderMasterDto)
			throws ApplicationException {
		try{
			ProcedureOrderMaster master = new ProcedureOrderMaster();
			master.setServiceId(procedureOrderMasterDto.getServiceId());
			master.setPreferedDateTime(procedureOrderMasterDto.getPreferedDateTime() != null && !procedureOrderMasterDto.getPreferedDateTime().isEmpty() ? GlobalCommonDateUtils.getDate(procedureOrderMasterDto.getPreferedDateTime(), "dd-M-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureOrderMasterDto.getUnitId()));
			//master.setPreferedTime(GlobalCommonDateUtils.getLocalTime(procedureOrderMasterDto.getPreferedTime()));
			master.setIsPrimarySecondary(procedureOrderMasterDto.getIsPrimarySecondary());
			master.setAmount(procedureOrderMasterDto.getAmount()==null?0:procedureOrderMasterDto.getAmount());
			master.setUrgency(procedureOrderMasterDto.getUrgency());
			master.setStatus('A');
			master.setRequestReason(procedureOrderMasterDto.getRequestReason());
			master.setIsIcuRequired(procedureOrderMasterDto.getIsIcuRequired());
			master.setIsBloodComponentRequired(procedureOrderMasterDto.getIsBloodComponentRequired());
			master.setIsPacRequired(procedureOrderMasterDto.getIsPacRequired());
			master.setOrgId(procedureOrderMasterDto.getOrgId());
			master.setUnitId(procedureOrderMasterDto.getUnitId());
			master.setCreatedBy(procedureOrderMasterDto.getCreatedBy());
			master.setCreatedDate(procedureOrderMasterDto.getCreatedDate() != null && !procedureOrderMasterDto.getCreatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureOrderMasterDto.getCreatedDate(), "dd-MM-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureOrderMasterDto.getUnitId()));
			master.setUpdatedBy(procedureOrderMasterDto.getUpdatedBy());
			master.setUpdatedDate(procedureOrderMasterDto.getUpdatedDate() != null && !procedureOrderMasterDto.getUpdatedDate().isEmpty() ? GlobalCommonDateUtils.getDate(procedureOrderMasterDto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss") : TimeZoneComponent.getDateByzone(procedureOrderMasterDto.getUnitId()));
			master.setProReqId(procedureOrderMasterDto.getProReqId());
			master.setExportedToOtims(false);
			master = save(master);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, master);
		}catch(HibernateException he){
			throw new BillingException(he.getMessage());
		}catch(Exception e){
			
			throw new BillingException(e.getMessage());
		}	
	}

	@Override
	public Response getProcedureOrderListByProReqID(Integer proReqId) throws ApplicationException {
		try{	
			Session session=sessionFactory.getCurrentSession();
			Integer count=session.createSQLQuery(" update otims.t_procedure_order_master set is_primary_secondary='N'  where pro_req_id	=:proReqId ")
					.setParameter("proReqId", proReqId)
					.executeUpdate();
			
			   return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateProcedureOrderSeconderyToPrimary(Integer procedureOrderId) throws ApplicationException {
		try{	
			
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_order_master set is_primary_secondary='Y' "
					+ " where procedure_order_id =:procedureOrderId ")
					.setParameter("procedureOrderId", procedureOrderId)
					.executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		
	}

	@Override
	public Response getProcedureOrderListForBookView(ProcedureOrderMasterDto procedureOrderMasterDto)
			throws ApplicationException {
		try
		{
			List<ProcedureOrderMasterDto> list;

			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_ORDER_LIST_FOR_BOOK_VIEW").getQueryString();
			list=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("patientId", procedureOrderMasterDto.getPatientId())
					.setParameter("organizationId", procedureOrderMasterDto.getOrganizationId())
					.setParameter("fromDate", procedureOrderMasterDto.getFromDate())
					.setParameter("toDate", procedureOrderMasterDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
//			}
		
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response searchPlannedProcedure(ProcedureRequestDto procedureRequestDto) throws ApplicationException {
		try {
			
			String query=sessionFactory.getCurrentSession().getNamedQuery("SEARCH_PLANNED_PROCEDURE_BY_VISIT_TYPE_ID").getQueryString();
			
					StringBuffer subQuery = new StringBuffer();
					if(procedureRequestDto.getFromDate()!=null && procedureRequestDto.getToDate()!=null){
						subQuery.append(" and to_char(prm.prefered_date_time,'YYYY-MM-DD') between :fromDate and :toDate ");
					}
					if(procedureRequestDto.getVisitTypeId()!=0){
						subQuery.append(" and  prm.visit_type_id =:visitTypeId ");
					}
					query = query.concat(subQuery.toString());
					Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
							.setInteger("unitId", procedureRequestDto.getUnitId())
							.setInteger("organizationId", procedureRequestDto.getOrgId());

			if(procedureRequestDto.getFromDate()!=null && procedureRequestDto.getToDate()!=null) {
				sqlQuery.setParameter("fromDate", procedureRequestDto.getFromDate());
				sqlQuery.setParameter("toDate", procedureRequestDto.getToDate());
			}

			if(procedureRequestDto.getVisitTypeId()!=0)
				sqlQuery.setParameter("visitTypeId", procedureRequestDto.getVisitTypeId());

			List<ProcedureSchedulingDto> list =	sqlQuery
							.setResultTransformer(Transformers.aliasToBean(ProcedureSchedulingDto.class)).list();

					return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
				} catch (Exception e) {
					
					throw e;
				}

	}

	@Override
	public Response getProcedureCheckListByProreqId(Integer proReqId/*, List<Integer> roleIds*/) throws ApplicationException {
		try{
			List<ProcedureOrderChecklistDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_ORDER_LIST_BY_PROREQ_ID")
					.setParameter("orderId", proReqId)
					//.setParameterList("roleIds", roleIds)
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderChecklistDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getChecklistItemListByMasterId(ProcedureOrderChecklistDto dto/*, List<Integer> roleIds*/) throws ApplicationException {
		try {
			List<ProcedureOrderChecklistDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_CHECKLIST_MASTER_DETAILS_LIST_BY_CHECK_LIST_MASTER_ID")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("checklistMasterId", dto.getChecklistId())
					//.setParameterList("roleIds", roleIds)
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderChecklistDto.class)).list();
			
			for (ProcedureOrderChecklistDto checklistMasterDetailsDto : list) {
				checklistMasterDetailsDto.setCheckStatus(false);
				checklistMasterDetailsDto.setChecklistStatusId(1);
				checklistMasterDetailsDto.setRemark("");
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, list, null);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUserOTIMSRoleID(Integer userId) throws ApplicationException {
		try
        {
			List<ProcedureOrderMasterDto> checkAdmitionId = sessionFactory.getCurrentSession().createSQLQuery(
				" select otm.role_id as \"roleId\" "
				+ "from public.t_otims_user_role_mapper orm  " + 
				" inner join  public.m_otims_role_master otm on otm.role_id = orm.role_id  " + 
				" where orm.user_id =:userId "
				+ " and  orm.status='A' ")
				.setParameter("userId", userId)
				.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, checkAdmitionId, null);
		
        } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getSaveChecklistItemList(ProcedureOrderChecklistDto procedureChecklistDto/*, List<Integer> roleIds*/,
			Integer proReqId,Integer stageId) throws ApplicationException {
		try
        {
			List<ProcedureOrderChecklistDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SAVED_CHECKLIST_ITEM_LIST")
					.setParameter("proReqId", proReqId)
					.setParameter("checklistMasterId", procedureChecklistDto.getChecklistId())
					.setParameter("stageId", stageId)
					//.setParameterList("roleIds", roleIds)
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderChecklistDto.class)).list();
			
			Integer count =0;
			Integer status = 3;
			if(list.size()>0) {
				
			
			for (ProcedureOrderChecklistDto checklistMasterDetailsDto : list) {
				checklistMasterDetailsDto.setCheckStatus(true);
				
				if(checklistMasterDetailsDto.getChecklistStatusId().equals(status)) {
					count++;
				}
			}
			if(count==0) {
				 status = 2;
			
			}
			
			}
				return new Response<>(SUCCESS, SUCCESS_CODE, null, list, status);
		
        } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getScheduleEquipment(Integer proReqId) {
		try{
			List<OtCompletionEquipmentDto> list = sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_EQUIPMENT_SCHEDULE_BY_PRO_REQ_ID")
					.setParameter("proReqId", proReqId)
					.setResultTransformer(Transformers.aliasToBean(OtCompletionEquipmentDto.class)).list();

			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getNoteByProcedureConfigId(ProcedureOrderMasterDto dto) throws ApplicationException {
		try {
            String query = sessionFactory.getCurrentSession()
                    .getNamedQuery("GET_NOTE_BY_PROCEDURE_ORDER_ID")
                    .getQueryString();
            
            StringBuffer subQuery = new StringBuffer();
           
            if(dto.getServiceId()!=null ){
                subQuery.append(" AND procedureConfiguration.serviceId =:serviceId ");
            }
            
            query = query.concat(subQuery.toString());
            Query sqlQuery = sessionFactory.getCurrentSession().createQuery(query);

			if(dto.getServiceId()!=null )
				sqlQuery.setParameter("serviceId", dto.getServiceId());

			List<OTCompletionNoteDto> list = sqlQuery
                    .setResultTransformer(Transformers.aliasToBean(OTCompletionNoteDto.class)).list();
            
            return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getSterilizedKitBatchList(CssdKitVO d) throws ApplicationException {
		try {
			List<CssdKitVO> kitBatchList=sessionFactory.getCurrentSession().createSQLQuery("select pre.id as \"kitTransitionId\",kitMaster.code as \"kitCode\","
					+ "kitMaster.kit_id as \"kitId\","  
					+"  kitMaster.name as \"kitName\",to_char(pre.expiry_date,'dd-mm-yyyy') as \"kitExpiryDate\",concat('B-',batch.load_batch_id)as \"batchNumber\" "  
					+"  from cssd.t_pre_sterilize_process pre "  
					+"  inner join cssd.t_load_batch_kit_mapper batchKitMapp  on batchKitMapp.transition_number=pre.id"  
					+"  inner join cssd.t_load_batch batch on batch.load_batch_id=batchKitMapp.load_batch_id"  
					+"  inner join cssd.m_kit_master kitMaster on kitMaster.kit_id = pre.kit_id"  
					+"  inner join cssd.t_issued_kit_transition_mapper kitTranMapp on kitTranMapp.transition_number=pre.id"  
					+"  inner join cssd.t_kit_issue kitIssue on kitIssue.id=kitTranMapp.issue_no"  
					+"  where pre.is_sterilized=true and pre.is_issued=true and pre.is_accepted=true"  
					+"  and kitIssue.to_store_id=:storeId and kitMaster.kit_id=:kitId"
					+"	and pre.organization_id=:orgId and pre.unit_id=:unitId")
					.setParameter("orgId", d.getOrganizationId())
					.setParameter("unitId", d.getUnitId())
					.setParameter("storeId", d.getStoreId())
					.setParameter("kitId", d.getKitId())
					.setResultTransformer(Transformers.aliasToBean(CssdKitVO.class))
					.list();
		 return new Response(SUCCESS, SUCCESS_CODE, null, kitBatchList, null);	
	} catch (HibernateException he) {
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateProcedureOrder(ProcedureOrderMasterDto order) throws ApplicationException {
		try{	
			
			ProcedureOrderMaster  mstr = findById(order.getProcedureOrderId());
            if(order.getStatus()!=null) {
                if(order.getStatus().equals("I")) {
                    mstr.setStatus('I');    
                }
            }
			mstr.setAmount(order.getRate());
			mstr = update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
			
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getEmergencyProcedure(ProcedureOrderMasterDto procedureOrderMasterDto) throws ApplicationException {
		try{
			List<ProcedureSchedulingDto> list = sessionFactory.getCurrentSession().createSQLQuery(
			"select " 
					+" distinct pr.uhid_number as \"uhidNumber\", " 
					+" concat(coalesce(prefix_mst.prefix_code),' ',coalesce(pr.first_name),' ',coalesce(pr.last_name)) as \"patientName\", " 
					+" pr.patient_id as \"patientId\", " 
					+ " (case when prm.admission_id is not null then admission.doctor_id " + 
					"			else encounter.doctor_id " + 
					"		end " + 
					"	) as \"doctorId\","
					+ " (case when "
					+ "    encounterDoc.doctor_id is not null then concat( coalesce( encounterDoc.first_name, '' ), ' ', coalesce( encounterDoc.middle_name, '' ), ' ', coalesce( encounterDoc.last_name, '' )) " + 
					"	 else concat( coalesce( adtDoc.first_name, '' ), ' ', coalesce( adtDoc.middle_name, '' ), ' ', coalesce( adtDoc.last_name, '' )) " + 
					"		end " + 
					"	) as \"doctorName\"," 
					+ "  to_char(adtDtls.doa, 'dd/MM/yyyy') as \"doa\", "
					+ "  to_char(adtDtls.pdd, 'dd/MM/yyyy') as \"pdd\", "
					+ "to_char(pr.dob,'DD/MM/YYYY') as \"birthDate\", "
					+" prm.admission_id as \"admissionId\", " 
					+" prm.encounter_id as \"encounterId\", " 
					+" prm.pro_req_id as \"proReqId\", " 
					+" bm.bed_number as \"bedNumber\", "
					+" ward.ward_name as \"wardName\", "
					+ " prm.visit_type_id as \"visitTypeId\", "
					+" to_char(prm.prefered_time,'hh24:mi:ss') as \"preferTime\", "
					+"	pr.gender_id as \"genderId\", "
					+"	pr.image_path as \"imagePath\", "
					+" gender.gender_code as \"genderCode\", "
					+" concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ') as \"age\", "
					+" to_char(prm.prefered_date_time, 'DD-MM-YYYY')  as \"scheduleDate\" , " 
					+" prm.procedure_status_id as \"proceduerStatusId\", "
					+" prost.status as \"procedureStatusName\","
					+" prm.emergency as \"emergency\","
					+" otdetails.total_hrs as \"totalDuration\" " 
					+" from otims.t_procedure_req_mst prm " 
					+" inner join otims.m_procedure_order_status prost on prm.procedure_status_id = prost.id "
					+" inner join otims.t_ot_completion_details otdetails on otdetails.pro_req_id = prm.pro_req_id"
					+" inner join patient.t_patient_registration pr on prm.patient_id = pr.patient_id " 
					+" inner join public.m_prefix_master prefix_mst on pr.prefix_id = prefix_mst.prefix_id "
					+" left join adt.t_admission admission on prm.admission_id = admission.admission_id "
					+" left join adt.t_admission_details adtDtls on admission.admission_id = adtDtls.admission_id and adtDtls.status='A' "
					+" left JOIN adt.m_bed_master bm on adtDtls.bed_id = bm.bed_id "  
					+" left JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
					+" left join opd.t_encounter_master encounter on prm.encounter_id = encounter.encounter_id " 
					+" inner join public.m_gender_master gender on pr.gender_id = gender.gender_id "
					+ " left join doctor.m_doctor_master encounterDoc on encounter.doctor_id = encounterDoc.doctor_id "
					+ " left join doctor.m_doctor_master adtDoc on admission.doctor_id = adtDoc.doctor_id "
					+" where  prm.procedure_status_id in (2,3,4)  " 
					+" and prm.org_id =:orgId "
					+" and prm.unit_id =:unitId  "
					+" and prm.visit_type_id =:visitTypeId "
					+" and to_char(prm.prefered_date_time,'YYYY-MM-DD') BETWEEN :fromDate AND :toDate "
					+" and prm.emergency = true ")
					.setParameter("orgId", procedureOrderMasterDto.getOrganizationId())
					.setParameter("unitId", procedureOrderMasterDto.getUnitId())
					.setParameter("visitTypeId", procedureOrderMasterDto.getVisitTypeId())
					.setParameter("fromDate", procedureOrderMasterDto.getFromDate())
					.setParameter("toDate", procedureOrderMasterDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(ProcedureSchedulingDto.class)).list();
			
			if(list != null && list.size()>0) {
				
				for (ProcedureSchedulingDto procedureSchedulingDto : list) {
					
					List<ProcedureOrderMasterDto> procedure = sessionFactory.getCurrentSession().createSQLQuery(
							"select ser.service_standard_name as \"procedureName\", "
							+ " prtype.procedure_type_name as \"procedureType\"  " 
									+" from otims.t_procedure_order_master procOrder " 
									+"  inner join service.m_service_master ser on procOrder.service_id = ser.service_master_id "
									+ "	  inner join otims.t_procedure_configuration prconf on procOrder.service_id = prconf.service_id  " + 
									"	   inner join otims.m_procedure_type_master prtype on prtype.procedure_type_id = prconf.procedure_type_id "
									+" where   pro_req_id =:procReqId ")
									.setParameter("procReqId", procedureOrderMasterDto.getProReqId())
									.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
					
					if(procedure!= null && procedure.size()>0 ) {
						int count = 0;
						String procedureName = "";
						String procedureType = "";
						for (ProcedureOrderMasterDto dto : procedure) {
							
							if(count == 0) {
								procedureName = dto.getProcedureName();
								procedureType = dto.getProcedureType();
							}else {
								procedureName += ","+dto.getProcedureName();
								procedureType += ","+dto.getProcedureType();
							}
						}
						procedureSchedulingDto.setProcedureName(procedureName);
						procedureSchedulingDto.setProcedureType(procedureType);
					}
				}  
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getUserOTIMSRoleByUserID(Integer userId) throws ApplicationException {
		try
        {
			List<ProcedureOrderMasterDto> checkAdmitionId = sessionFactory.getCurrentSession().createSQLQuery(
				" select usmstr.role_id as \"roleId\" "
				+ " from public.t_user_role_mapper usmppr  " + 
				" inner join  public.m_role_master usmstr on usmppr.role_id = usmstr.role_id   " + 
				" where usmppr.user_id =:userId  "
				+ " and usmstr.role_type_id in(8,9,10,11,12) and usmppr.status = 'A'  ")
				.setParameter("userId", userId)
				.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, checkAdmitionId, null);
		
        } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
	@Override
	public Response updateProcedureSchedulingDetails(ProcedureOrderMasterDto procDto) throws ApplicationException {
		try
        {
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update otims.t_procedure_schedule set procedure_name =:procedureName , ot_type=:theatreTypeName ,procedure_type =:procedureType where procedure_schedule_id =:procedureScheduleId ")
					.setParameter("procedureScheduleId", procDto.getProcedureScheduleId())
					.setParameter("procedureName", procDto.getProcedureName())
					.setParameter("theatreTypeName", procDto.getTheatreTypeName())
					.setParameter("procedureType", procDto.getProcedureType())
					.executeUpdate();
			
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
		
        } catch (Exception e)
        {
            
        }
        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getOTTThetreByServiceId(ProcedureOrderMasterDto order) throws ApplicationException {
		 try{
			 ProcedureOrderMasterDto OTTDetailsObj = (ProcedureOrderMasterDto)sessionFactory.getCurrentSession().createSQLQuery(
					" select config.operation_theatre_type_id as \"operationTheatreTypeId\","
				    +" theatre.theatre_type_name  as \"theatreTypeName\"  "
					+" from otims.t_procedure_configuration config "
					+ " inner join otims.m_operation_theatre_type theatre on config.operation_theatre_type_id = theatre.operation_theatre_type_id "
					+ " where config.service_id =:serviceId and config.status = 'A' and config.org_id =:orgId and config.unit_id =:unitId ")
					.setParameter("orgId", order.getOrgId())
					.setParameter("unitId", order.getUnitId())
					.setParameter("serviceId", order.getServiceId())
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).uniqueResult();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, OTTDetailsObj);
	        } catch (Exception e)
	        {
	            
	        }
	        return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response getProcedureOrderListByAdmissionIdAndEncounterId(int admissionId, int encounterId, int i)
			throws ApplicationException {
		try
		{
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_LIST_OF_PROCEDURE_ORDER_BY_ADMISSION_ID_ENCOUNTER_ID").getQueryString();
			
			StringBuffer subQuery = new StringBuffer();
			if(admissionId!=0){
				subQuery.append(" and prm.admission_id =:admissionId ");
			}
			if(encounterId!=0){
				subQuery.append(" and prm.encounter_id =:encounterId ");
			}
			
			query = query.concat(subQuery.toString());
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query);

			if(admissionId!=0)
				sqlQuery.setParameter("admissionId", admissionId);

			if(encounterId!=0)
				sqlQuery.setParameter("encounterId", encounterId);

			List<ProcedureOrderMasterDto> list = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(ProcedureOrderMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (Exception e)
		{
			
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response searchProcedureByNameOrCode(ProcedureOrderMasterDto procedureOrderDto) throws ApplicationException {
		try
		{
			String query=sessionFactory.getCurrentSession().getNamedQuery("GET_PROCEDURE_SERVICE_LIST_BY_SERVICENAME_OR_CODE").getQueryString();
			
			StringBuffer subQuery = new StringBuffer();
			if(procedureOrderDto.getSpecialityId()!=null){
				subQuery.append(" AND service.speciality_id =:specialityId ");
			}
			
			query = query.concat(subQuery.toString());
			Query sqlQuery = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("unitId", procedureOrderDto.getUnitId())
					.setParameter("orgId", procedureOrderDto.getOrganizationId())
					.setParameter("searchKeyword", "%"+ procedureOrderDto.getServiceName().trim().toLowerCase()+"%");

			if(procedureOrderDto.getSpecialityId()!=null)
				sqlQuery.setParameter("specialityId", procedureOrderDto.getSpecialityId());

			List<ServiceSearchResDto> list = sqlQuery
					.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
			

		} catch (Exception e)
		{
			
		}
		return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

}