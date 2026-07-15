package com.param.adt.transfer.dao;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.param.global.common.GlobalCommonDateUtils;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.transfer.dto.TransferModalityRequestDto;
import com.param.adt.transfer.model.TransferModalityRequest;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.PatientSearchDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class ModalityTransferDaoImpl extends GenericDao<TransferModalityRequest, Integer>
		implements ICommonConstants, IModalityTransferDao {

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	public ModalityTransferDaoImpl() {
		super(TransferModalityRequest.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response searchAdmittedPatientByNameAndUhid(AdmissionDto admissionDto) throws ApplicationException {
		try {
			List<PatientRegistrationDto> patientRegistrationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_LIKE")
					.setParameter("firstName", "%" + admissionDto.getPatientName().toLowerCase() + "%")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientServiceOrderListByAdmissionId(AdmissionDto admissionDto) throws ApplicationException {
		try {
			List<PatientRegistrationDto> patientRegistrationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_SERVICE_ORDER_LIST_BY_ADMISSION_ID")
					.setParameter("orgId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("admissionId", admissionDto.getAdmissionId())
					.setParameter("transferTypeId", admissionDto.getTransferTypeId())
					.setParameter("visitTypeId", admissionDto.getVisitTypeId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveModalityTransferRequest(TransferModalityRequestDto transferModalityRequestDto)
			throws ApplicationException {
		try {
			// <----- Saving new modality transfer request
			TransferModalityRequest transferModalityRequest = new TransferModalityRequest();
			transferModalityRequest.setOrganizationId(transferModalityRequestDto.getOrganizationId());
			transferModalityRequest.setUnitId(transferModalityRequestDto.getUnitId());
			transferModalityRequest.setAdmissionId(transferModalityRequestDto.getAdmissionId());
			transferModalityRequest.setModalityId(transferModalityRequestDto.getModalityId());
			transferModalityRequest.setSchedularId(transferModalityRequestDto.getSchedularId());
			transferModalityRequest.setModalityTime(transferModalityRequestDto.getModalityTime());
			transferModalityRequest.setTransferTime(transferModalityRequestDto.getTransferTime());
			transferModalityRequest.setIsRetained('N');
			transferModalityRequest.setDestinationId(transferModalityRequestDto.getDestinationId());
			transferModalityRequest.setStatus('A');
			transferModalityRequest.setTransferStatusId(transferModalityRequestDto.getTransferStatusId());
			transferModalityRequest.setNote(transferModalityRequestDto.getNote());
			transferModalityRequest.setPatientServiceOrderId(transferModalityRequestDto.getPatientServiceOrderId());
			transferModalityRequest.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferModalityRequestDto.getUnitId() != null ? transferModalityRequestDto.getUnitId():0), "dd-M-yyyy HH:mm:ss"));
			transferModalityRequest.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferModalityRequestDto.getUnitId() != null ? transferModalityRequestDto.getUnitId():0), "dd-M-yyyy HH:mm:ss"));
			transferModalityRequest.setCreatedBy(transferModalityRequestDto.getCreatedBy());
			transferModalityRequest.setUpdatedBy(transferModalityRequestDto.getUpdatedBy());
			save(transferModalityRequest);
			// ----->

			Session session = sessionFactory.getCurrentSession();

			// <----- Updating patient service order's is_mortality_transfer to
			// 'Y'
			Query query = session.createQuery("update PatientServicesOrder pso set pso.updatedBy=:updatedBy, "
					+ ",pso.updatedDate=:updatedDate, "
					+ "', pso.isModalityTransfer='Y'"
					+"where pso.patientSevicesOrderId=:patientServiceOrderId ");
			query.setParameter("updatedBy", transferModalityRequestDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(transferModalityRequestDto.getUnitId() != null ? transferModalityRequestDto.getUnitId() : 0), "dd-M-yyyy HH:mm:ss"));
			query.setParameter("patientServiceOrderId", transferModalityRequestDto.getPatientServiceOrderId());
			query.executeUpdate();
			// ----->

			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getModalityTransferRequestList(TransferModalityRequestDto transferModalityRequestDto)
			throws ApplicationException {
		try {
			List<TransferModalityRequestDto> patientRegistrationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_MODALITY_TRANSFER_REQUEST_LIST")
					.setParameter("orgId", transferModalityRequestDto.getOrganizationId())
					.setParameter("unitId", transferModalityRequestDto.getUnitId())
					.setParameter("admissionId", transferModalityRequestDto.getAdmissionId())
					.setParameter("transferTypeId", transferModalityRequestDto.getTransferTypeId())
					.setResultTransformer(Transformers.aliasToBean(TransferModalityRequestDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getPatientServiceOrderList(AdmissionDto admissionDto) throws ApplicationException {
		try {
			List<PatientRegistrationDto> patientRegistrationDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_PATIENT_SERVICE_ORDER_LIST")
					.setParameter("orgId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("transferTypeId", admissionDto.getTransferTypeId())
					.setParameter("visitTypeId", admissionDto.getVisitTypeId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchPatientServiceOrdersByMulCriteria(PatientSearchDto patientSearchDto)
			throws ApplicationException {
		try {

			Date date = TimeZoneComponent.getDateByzone(patientSearchDto.getUnitId());
			String currentDate= GlobalCommonDateUtils.getStringDate(date, "yyyy-MM-dd");

			Session session = sessionFactory.getCurrentSession();
			StringBuilder resQuery = this.searchPatient(patientSearchDto);
			SQLQuery actualQuery = session.createSQLQuery(resQuery.toString());
			Query sql = actualQuery
					.setParameter("orgId", patientSearchDto.getOrganizationId())
					.setParameter("unitId", patientSearchDto.getUnitId())
					.setParameter("transferTypeId", patientSearchDto.getTransferTypeId())
					.setParameter("visitTypeId", patientSearchDto.getVisitTypeId());
			if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty()) {
				sql.setParameter("patientName", patientSearchDto.getPatientName());
			}

			if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty()) {
				sql.setParameter("uhidNumber", patientSearchDto.getUhIdNumber());
			}

			if (patientSearchDto.getWardName() != null && !patientSearchDto.getWardName().isEmpty()) {
				sql.setParameter("wardName", patientSearchDto.getWardName());
			}

			if (patientSearchDto.getBedNumber() != null && !patientSearchDto.getBedNumber().isEmpty()) {
				sql.setParameter("bedNumber", patientSearchDto.getBedNumber());
			}

			List<PatientSearchDto> bedMasterDtosList = sql.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

			if(bedMasterDtosList != null && !bedMasterDtosList.isEmpty()){

				List<Integer> patientList = bedMasterDtosList.stream().map(PatientSearchDto :: getPatientId).collect(Collectors.toList());
				String q = "select " +
						" max(enc.encounter_id) as \"encounterId\" ,  " +
						" enc.patient_id as \"patientId\" " +
						" from  " +
						" opd.t_encounter_master enc " +
						" inner join opd.m_appointment_master app on  " +
						" enc.appointment_id = app.appointment_id  " +
						" where  " +
						" enc.status_id != 6  " +
						" and to_char(app.appointment_date, 'yyyy-MM-dd')= '"+currentDate+"'   and  enc.patient_id in (:patientsList) " +
						" group by enc.patient_id  ";
				SQLQuery query = session.createSQLQuery(q);
				Query sqlsub = query.setParameterList("patientsList",patientList);
				List<AdmissionDto> encounterList = sqlsub.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

				for(PatientSearchDto dto :bedMasterDtosList){
					AdmissionDto dummyservic= encounterList.stream()
							.filter(order -> dto.getPatientId().equals(order.getPatientId()))
							.findAny()
							.orElse(null);
					if(dummyservic!=null){
						dto.setEncounterId(dummyservic.getEncounterId());
					}
				}

			}

			return new Response(SUCCESS, null, null, bedMasterDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	private StringBuilder searchPatient(PatientSearchDto patientSearchDto) {

		StringBuilder pCondition = new StringBuilder();
		StringBuilder tCondition = new StringBuilder();
		StringBuilder commonCondition = new StringBuilder();


		StringBuilder ANDtPatientName = new StringBuilder(
				"AND lower(concat(coalesce(pr.patient_name))) LIKE concat('%',lower(:patientName),'%') ");

		StringBuilder ANDtUhid = new StringBuilder(
				"AND lower(pr.t_uhid) LIKE concat('%',LOWER(:uhidNumber),'%') ");

		StringBuilder ANDPatientName = new StringBuilder(
				"AND lower(concat(coalesce(pr.first_name,''),coalesce(pr.last_name,''))) LIKE concat('%',lower(:patientName),'%') ");

		StringBuilder ANDUhid = new StringBuilder(
				"AND lower(pr.uhid_number) LIKE concat('%',LOWER(:uhidNumber),'%') ");

		StringBuilder ANDWardName = new StringBuilder(
				"AND lower(ward.ward_name) LIKE concat('%',LOWER(:wardName),'%') ");

		StringBuilder ANDBedNumber = new StringBuilder(
				"AND lower(bm.bed_number) LIKE concat('%',LOWER(:bedNumber) ");

		if (patientSearchDto.getUhIdNumber() != null && !patientSearchDto.getUhIdNumber().isEmpty())
		{
			pCondition.append(ANDUhid);
			tCondition.append(ANDtUhid);
		}
		
		if (patientSearchDto.getPatientName() != null && !patientSearchDto.getPatientName().isEmpty())
		{
			pCondition.append(ANDPatientName);
			tCondition.append(ANDtPatientName);
		}
		
		if (patientSearchDto.getWardName() != null && !patientSearchDto.getWardName().isEmpty())
			commonCondition.append(ANDWardName);
		
		if (patientSearchDto.getBedNumber() != null && !patientSearchDto.getBedNumber().isEmpty())
			commonCondition.append(ANDBedNumber);

		StringBuilder query = new StringBuilder("SELECT pso.patient_sevices_order_id as \"patientSevicesOrderId\", "
				+ "pso.service_id as \"serviceId\", " + "pso.admission_id as \"admissionId\", "
				+ "adm.patient_id as \"patientId\", " + "adm.t_patient_id as \"tPatientId\", "
				+ "adm.visit_type_id as \"visitTypeId\", " + "pr.uhid_number as \"UHIDNumber\", "
				+ "concat(coalesce(pre.prefix_code,''),' ',pr.first_name,' ',pr.middle_name,' ',pr.last_name) as \"pFirstName\" , "
				+ "to_char(pr.dob,'MM/dd/yyyy') as \"dob\", " + "pr.gender_id as \"genderId\", "
				+ "gm.gender_code as \"genderCode\", " + "bm.bed_number as \"bedNumber\", " + "bm.bed_id as \"bedId\", "
				+ "bm.ward_id as \"wardId\"," + "ward.ward_name as \"wardName\", "
				+ "ser.service_standard_name as \"serviceStandardName\", "
				+ "to_char(ss.schedule_date,'dd/MM/yyyy') as \"scheduleDate\", "
				+ "to_char(ss.from_time,'HH:MI') as \"fromTime2\"," + "ss.schedule_id as \"scheduleId\" "
				+ "FROM public.t_patient_sevices_order pso "
				+ "INNER join adt.t_admission adm on pso.admission_id=adm.admission_id "
				+ "INNER join adt.t_admission_details adl on adm.admission_id=adl.admission_id "
				+ "INNER JOIN patient.t_patient_registration pr on adm.patient_id=pr.patient_id "
				+ "INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id "
				+ " left join m_prefix_master pre on pr.prefix_id = pat.prefix_id "
				+ "INNER JOIN adt.m_bed_master bm on adl.bed_id=bm.bed_id "
				+ "INNER JOIN adt.m_ward_master ward on bm.ward_id=ward.ward_id "
				+ "INNER join service.m_service_master ser on ser.service_master_id=pso.service_id AND ser.is_required_schedule = 'Y'"
				+ "LEFT join service.t_service_schedule ss on pso.patient_sevices_order_id = ss.patient_service_order_id AND ss.transfer_type_id=:transferTypeId AND ss.visit_type_id = :visitTypeId "
				+ "WHERE pso.organization_id=:orgId " 
				+ "AND pso.unit_id=:unitId " 
				+ "AND pso.order_status_id=1 "
				//+ "AND pso.is_modality_transfer = 'N' " 
				+ pCondition
				+ commonCondition
				+ "UNION "
				+ "SELECT pso.patient_sevices_order_id as \"patientSevicesOrderId\", "
				+ "pso.service_id as \"serviceId\", " 
				+ "pso.admission_id as \"admissionId\", "
				+ "adm.patient_id as \"patientId\", " 
				+ "adm.t_patient_id as \"tPatientId\", "
				+ "adm.visit_type_id as \"visitTypeId\", " 
				+ " pr.t_uhid as \"UHIDNumber\","
				+ "  pr.patient_name as \"pFirstName\" , "
				+ "  concat(to_char(pr.age,'999'),' ', pr.age_format) as \"dob\" ," 
				+ "pr.gender_id as \"genderId\", "
				+ "gm.gender_code as \"genderCode\", " + "bm.bed_number as \"bedNumber\", " + "bm.bed_id as \"bedId\", "
				+ "bm.ward_id as \"wardId\"," + "ward.ward_name as \"wardName\", "
				+ "ser.service_standard_name as \"serviceStandardName\", "
				+ "to_char(ss.schedule_date,'dd/MM/yyyy') as \"scheduleDate\", "
				+ "to_char(ss.from_time,'HH:MI') as \"fromTime2\"," + "ss.schedule_id as \"scheduleId\" "
				+ "FROM public.t_patient_sevices_order pso "
				+ "INNER join adt.t_admission adm on pso.admission_id=adm.admission_id "
				+ "INNER join adt.t_admission_details adl on adm.admission_id=adl.admission_id "
				+ " INNER JOIN patient.m_unknown_patient_registration pr on adm.t_patient_id=pr.unknown_patient_id "
				+ "INNER JOIN public.m_gender_master gm on pr.gender_id=gm.gender_id "
				+ "INNER JOIN adt.m_bed_master bm on adl.bed_id=bm.bed_id "
				+ "INNER JOIN adt.m_ward_master ward on bm.ward_id=ward.ward_id "
				+ "INNER join service.m_service_master ser on ser.service_master_id=pso.service_id AND ser.is_required_schedule = 'Y'"
				+ "LEFT join service.t_service_schedule ss on pso.patient_sevices_order_id = ss.patient_service_order_id AND ss.transfer_type_id=:transferTypeId AND ss.visit_type_id = :visitTypeId "
				+ "WHERE pso.organization_id=:orgId " + "AND pso.unit_id=:unitId " + "AND pso.order_status_id=1 "
				//+ "AND pso.is_modality_transfer = 'N' " 
				+ tCondition
				+ commonCondition
				);

		// //System.out.println(query);
		return query;
	}

}
