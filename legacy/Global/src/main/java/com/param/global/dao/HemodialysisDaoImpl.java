package com.param.global.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IAppointmentAndSlotStatusMasterConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.PatientDialysisRecordDto;
import com.param.global.dto.PatientDialysisVitalDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.SlotMasterDto;
import com.param.opd.appointment.dto.HemodialysisAppointmentMapperDto;
import com.param.opd.appointment.model.HemodialysisAppointmentMapper;
import com.param.opd.coversheet.model.PatientDialysisRecord;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class HemodialysisDaoImpl extends GenericDao<HemodialysisAppointmentMapper, Integer> implements IHemodialysisDao,ICommonConstants,IAppointmentAndSlotStatusMasterConstants {
	final static Logger logger = Logger.getLogger(HemodialysisDaoImpl.class);


	public HemodialysisDaoImpl() {
		super(HemodialysisAppointmentMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response assignDialysisChair(SlotMasterDto slotMasterDto) {
		try {
			String appDateStr[]=(slotMasterDto.getAppointmentDate()).split("-");
			Date appDate=(GlobalCommonDateUtils.getDate(appDateStr[2]+"-"+appDateStr[1]+"-"+appDateStr[0], "dd-M-yyyy"));
			sessionFactory.getCurrentSession()
					.createQuery("update AppointmentMaster set " + "appointmentStatusId=:appointmentStatusId, "
							+ " updatedBy=:updatedBy, updatedDate=:updatedDate,slotId=:slotId,appointmentTypeId=8,"
							+ " appointmentDate=:appointmentDate "
							+ " WHERE appointmentId=:appointmentId")
					.setParameter("appointmentStatusId", 5)//for check in
					.setParameter("updatedBy", slotMasterDto.getUpdatedBy())
					.setParameter("slotId", slotMasterDto.getSlotId())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"))
					.setParameter("appointmentDate", appDate)
					.setParameter("appointmentId", slotMasterDto.getAppointmentId()).executeUpdate();

			sessionFactory.getCurrentSession().createQuery("update SlotMaster sm set " + "sm.slotStatusId=" + Booked + ", " // slot booked
					+ "sm.updatedBy=:updatedBy, " + "sm.updatedDate=:updatedDate " + "where sm.slotId=:slotId ")
					.setParameter("updatedBy", slotMasterDto.getUpdatedBy())
					.setParameter("updatedDate", GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(slotMasterDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"))
					.setParameter("slotId", slotMasterDto.getSlotId()).executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("assignDialysisChair failed", he);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveDialysisRequest(HemodialysisAppointmentMapperDto hemodialysisDto) {
		try {
			if(hemodialysisDto.getDialysisAppMapperId()==null) {
			HemodialysisAppointmentMapper hemodialysisAppointmentMapper=new HemodialysisAppointmentMapper();
			hemodialysisAppointmentMapper.setAppointmentId(hemodialysisDto.getAppointmentId());
			hemodialysisAppointmentMapper.setOrderDetailsId(hemodialysisDto.getOrderDetailsId());
			hemodialysisAppointmentMapper.setChairId(hemodialysisDto.getChairId());
			if(hemodialysisDto.getHemodialysisWorkbenchAcceptance()!=null) {
				hemodialysisAppointmentMapper.setDoctorId(hemodialysisDto.getDialysisDoctorId());
				hemodialysisAppointmentMapper.setWorkbenchAcceptance(hemodialysisDto.getHemodialysisWorkbenchAcceptance());
			}
			hemodialysisAppointmentMapper.setOrganizationId(hemodialysisDto.getOrganizationId());
			hemodialysisAppointmentMapper.setUnitId(hemodialysisDto.getUnitId());
			hemodialysisAppointmentMapper.setCreatedBy(hemodialysisDto.getUpdatedBy());
			hemodialysisAppointmentMapper.setUpdatedBy(hemodialysisDto.getUpdatedBy());
			hemodialysisAppointmentMapper.setCreatedDate(GlobalCommonDateUtils.getDate(hemodialysisDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			hemodialysisAppointmentMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(hemodialysisDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			hemodialysisAppointmentMapper.setStatus('A');
			hemodialysisAppointmentMapper.setDialysisStatusId(1);//1-for pending 2-for completed 3-Terminated 4-Final completion 5-In progress 6-Rejected
			sessionFactory.getCurrentSession().save(hemodialysisAppointmentMapper);
			}else {
				HemodialysisAppointmentMapper hemodialysisAppointmentMapper=findById(hemodialysisDto.getDialysisAppMapperId());
				if(hemodialysisDto.getHemodialysisWorkbenchAcceptance()!=null) {
				hemodialysisAppointmentMapper.setChairId(hemodialysisDto.getChairId());
				hemodialysisAppointmentMapper.setDoctorId(hemodialysisDto.getDialysisDoctorId());
				hemodialysisAppointmentMapper.setWorkbenchAcceptance(hemodialysisDto.getHemodialysisWorkbenchAcceptance());
				}
				
				if(hemodialysisDto.getTechnicianAcceptance()!=null && hemodialysisDto.getTechnicianAcceptance().equals('R')) {
					hemodialysisAppointmentMapper.setTechnicianAcceptance(hemodialysisDto.getTechnicianAcceptance());
					hemodialysisAppointmentMapper.setDialysisStatusId(6);
				}
				
				if(hemodialysisDto.getTechnicianId()!=null) {
				hemodialysisAppointmentMapper.setTechnicianAcceptance(hemodialysisDto.getTechnicianAcceptance());
				if(hemodialysisDto.getTechnicianAcceptance().equals('A')){
					hemodialysisAppointmentMapper.setDialysisStatusId(5);//5 for in progress
				}
				hemodialysisAppointmentMapper.setTechnicianId(hemodialysisDto.getTechnicianId());
				hemodialysisAppointmentMapper.setMachineId(hemodialysisDto.getMachineId());
				}
				
				if(hemodialysisDto.getDialysisStatusId()!=null) {
					hemodialysisAppointmentMapper.setDialysisStatusId(hemodialysisDto.getDialysisStatusId());
				}
				sessionFactory.getCurrentSession().update(hemodialysisAppointmentMapper);
			}
			
			if(hemodialysisDto.getAppointmentDate()!=null) {
				Date appDate=(GlobalCommonDateUtils.getDate(hemodialysisDto.getAppointmentDate(), "dd-M-yyyy"));
				sessionFactory.getCurrentSession()
				.createQuery("update OrderDetailsMaster ord set ord.orderDate=:orderDate,"
						+ " ord.orderSchDate=:orderDate where ord.orderDetailsId=:orderDetailId ")
				.setParameter("orderDetailId", hemodialysisDto.getOrderDetailsId()).setParameter("orderDate", appDate)
				.executeUpdate();
			}
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("saveDialysisRequest failed", he);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientListForDialysisWorkload(
			HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {

		try {
				
				StringBuilder query = new StringBuilder(" select " 
						 + " 	distinct (orderDetail.order_details_id) as \"orderDetailsId\", " 
						 + " 	sm.slot_id as \"slotId\", " 
						 + " 	admission.admission_id as \"admissionId\", " 
						 + " 	appMstr.appointment_id as \"appointmentId\", " 
						 + " 	orderDetail.ord_doc_id as \"ordDoctorId\", " 
						 + " 	orderDetail.status as \"status\", " 
						 + " 	orderDetail.service_rendered as \"serviceRendered\", " 
						 + " 	patientReg.patient_id as \"patientId\", " 
						 + " 	patientReg.uhid_number as \"uhidNumber\", " 
						 + " 	patientReg.gender_id as \"genderId\", " 
						 + " 	genderMaster.gender_code as \"genderCode\", " 
						 + " 	to_char(patientReg.dob, 'DD-MM-YYYY HH:mm:ss') as \"dob\", " 
						 + " 	concat(extract(year from AGE(CURRENT_TIMESTAMP, patientReg.dob)) , 'Y ', extract(month from AGE(CURRENT_TIMESTAMP, patientReg.dob)) , 'M ' , extract(day from AGE(CURRENT_TIMESTAMP, patientReg.dob)), 'D ') as \"ageString\", " 
						 + " 	encounterMaster.encounter_id as \"encounterId\", " 
						 + " 	encounterMaster.doctor_id as \"doctorId\", " 
						 + " 	('Dr. ' || coalesce(doctorMaster.first_name)|| ' ' || coalesce(doctorMaster.middle_name)|| ' ' || coalesce(doctorMaster.last_name)) as \"doctorName\", " 
						 + " 	(coalesce(doctorMasterIp.first_name)|| ' ' || coalesce(doctorMasterIp.middle_name)|| ' ' || coalesce(doctorMasterIp.last_name)) as \"admittingDoctorName\", " 
						 + " 	encounterMaster.dept_id as \"specialityId\", " 
						 + " 	specMaster.speciality_name as \"specialityName\", " 
						 + " 	serviceMaster.service_standard_name as \"serviceName\", " 
						 + " 	serviceMaster.service_master_id as \"serviceId\", " 
						 + " 	serviceMaster.service_standard_code as \"serviceCode\", " 
						 + " 	orderDetail.priority_id as \"priorityId\", " 
						 + " 	orderMaster.order_no as \"orderNo\", " 
						 + " 	to_char(dtl.doa, 'DD/MM/YYYY') as \"doa\", " 
						 + " 	to_char(dtl.pdd, 'DD/MM/YYYY') as \"pdd\", " 
						 + " 	to_char(orderDetail.order_date, 'dd/mm/yyyy') as \"orderDate\", " 
						 + " 	(prefixMaster.prefix_code || ' ' || coalesce(patientReg.first_name)|| ' ' || coalesce(patientReg.middle_name)|| ' ' || coalesce(patientReg.last_name)) as \"patientName\", " 
						 + " 	bedMstr.bed_number as \"bedNumber\" , " 
						 + " 	0 as \"fromBedId\", " 
						 + " 	orderDetail.accession_number as \"accessionNumber\" , " 
						 + " 	orderMaster.visit_type_id as \"visitTypeId\", " 
						 + " 	patientReg.identification_number as \"identificationNumber\", " 
						 + " 	vm.visit_type_name as \"visitName\", " 
						 + " 	pcm.patient_category as \"patientCategory\" , " 
						 + " 	encounterMaster.encounter_number as \"visitNumber\", " 
						 + " 	cm.country_code as \"countryCode\" , " 
						 + " 	sm.bed_id as \"toBedId\", " 
						 + " 	bm.bed_number as \"toBedNumber\", " 
						 + " 	to_char(sm.from_time, 'HH:MI') as \"appointmentTime\", " 
						 + " 	bm.ward_id as \"toWardId\", " 
						 + " 	bm.room_id as \"toRoomId\", " 
						 + " 	bedMstr.ward_id as \"fromWardId\", " 
						 + " 	bedMstr.room_id as \"fromRoomId\", " 
						 + " 	to_char(orderDetail.order_date, 'DD/MM/YYYY') as \"appointmentDate\", " 
						 + " 	wm.ward_name as \"wardName\", " 
						 + " 	hemoDialysisMapper.dialysis_app_mapper_id as \"dialysisAppMapperId\", " 
						 + " 	hemoDialysisMapper.chair_id as \"chairId\", " 
						 + " 	hemoDialysisMapper.doctor_id as \"dialysisDoctorId\", " 
						 + " 	hemoDialysisMapper.workbench_acceptance as \"hemodialysisWorkbenchAcceptance\", " 
						 + " 	bm.bed_number as \"chairName\", " 
						 + " 	hemoDialysisMapper.technician_acceptance as \"technicianAcceptance\", " 
						 + " 	hemoDialysisMapper.technician_id as \"technicianId\", " 
						 + " 	CONCAT (employeeMaster.first_name, " 
						 + " 	' ', " 
						 + " 	employeeMaster.middle_name, " 
						 + " 	' ', " 
						 + " 	employeeMaster.last_name) as \"technicianName\", " 
						 + " 	hemoDialysisMapper.machine_id as \"machineId\", " 
						 + " 	itemMaster.name as \"machineName\", " 
						 + " 	hemoDialysisMapper.dialysis_status_id as \"dialysisStatusId\", " 
						 + " 	( " 
						 + " 	select " 
						 + " 		to_char(pdr.dialysis_start_date, 'DD/MM/YYYY') " 
						 + " 	from " 
						 + " 		patient.t_patient_dialysis_record pdr " 
						 + " 	where " 
						 + " 		pdr.appointment_id = hemoDialysisMapper.appointment_id " 
						 + " 	order by " 
						 + " 		pdr.patient_dialysis_rec_id asc " 
						 + " 	limit 1) as \"dialysisStartDate\", " 
						 + " 	( " 
						 + " 	select " 
						 + " 		to_char(pdr.dialysis_start_time, 'HH24:MI') " 
						 + " 	from " 
						 + " 		patient.t_patient_dialysis_record pdr " 
						 + " 	where " 
						 + " 		pdr.appointment_id = hemoDialysisMapper.appointment_id " 
						 + " 	order by " 
						 + " 		pdr.patient_dialysis_rec_id asc " 
						 + " 	limit 1) as \"dialysisStartTime\", " 
						 + " 	( " 
						 + " 	select " 
						 + " 		to_char(pdr.created_date, 'DD/MM/YYYY') " 
						 + " 	from " 
						 + " 		patient.t_patient_dialysis_record pdr " 
						 + " 	where " 
						 + " 		pdr.appointment_id = hemoDialysisMapper.appointment_id " 
						 + " 	order by " 
						 + " 		pdr.patient_dialysis_rec_id desc " 
						 + " 	limit 1) as \"dialysisEndDate\", " 
						 + " 	( " 
						 + " 	select " 
						 + " 		to_char(pdr.dialysis_end_time, 'HH24:MI') " 
						 + " 	from " 
						 + " 		patient.t_patient_dialysis_record pdr " 
						 + " 	where " 
						 + " 		pdr.appointment_id = hemoDialysisMapper.appointment_id " 
						 + " 	order by " 
						 + " 		pdr.patient_dialysis_rec_id desc " 
						 + " 	limit 1) as \"dialysisEndTime\" " 
						 + " from " 
						 + " 	opd.t_hemodialysis_appointment_mapper hemoDialysisMapper " 
						 + " inner join public.t_order_details orderDetail on " 
						 + " 	orderDetail.order_details_id = hemoDialysisMapper.order_details_id " 
						 + " inner join public.t_order_master orderMaster on " 
						 + " 	orderDetail.order_id = orderMaster.order_id " 
						 + " inner join patient.t_patient_registration patientReg on " 
						 + " 	orderMaster.patient_id = patientReg.patient_id " 
						 + " inner join public.m_prefix_master prefixMaster on " 
						 + " 	patientReg.prefix_id = prefixMaster.prefix_id " 
						 + " inner join public.m_gender_master genderMaster on " 
						 + " 	patientReg.gender_id = genderMaster.gender_id " 
						 + " inner join patient.t_patient_details patdtl on " 
						 + " 	patdtl.patient_id = patientReg.patient_id " 
						 + " inner join public.m_visit_type_master vm on " 
						 + " 	vm.visit_type_id = orderMaster.visit_type_id " 
						 + " 	and vm.status = 'A' " 
						 + " left join public.m_patient_category_master pcm on " 
						 + " 	pcm.patient_category_id = patientReg.patient_category_id " 
						 + " 	and pcm.status = 'A' " 
						 + " left join public.m_country_master cm on " 
						 + " 	cm.country_id = patdtl.country_id " 
						 + " left join opd.t_encounter_master encounterMaster on " 
						 + " 	orderMaster.encounter_id = encounterMaster.encounter_id " 
						 + " left outer join adt.t_admission admission on " 
						 + " 	orderMaster.admission_id = admission.admission_id " 
						 + " left join billing.t_billing_details billingDet on " 
						 + " 	orderDetail.order_details_id = billingDet.order_details_id " 
						 + " left join billing.m_billing_master billMaster on " 
						 + " 	orderDetail.service_bill_id = billMaster.billing_master_id " 
						 + " 	and billMaster.bill_cancelled = 'N' " 
						 + " left outer join doctor.m_doctor_master doctorMaster on " 
						 + " 	hemoDialysisMapper.doctor_id = doctorMaster.doctor_id " 
						 + " left join doctor.m_speciality_master specMaster on " 
						 + " 	orderDetail.ord_doc_spl_id = specMaster.speciality_id " 
						 + " inner join opd.m_appointment_master appMstr on " 
						 + " 	appMstr.appointment_id = hemoDialysisMapper.appointment_id " 
						 + " left join opd.m_slot_master sm on " 
						 + " 	sm.slot_id = appMstr.slot_id " 
						 + " inner join service.m_service_master serviceMaster on " 
						 + " 	orderDetail.service_id = serviceMaster.service_master_id " 
						 + " left join adt.m_priority_master pm on " 
						 + " 	pm.priority_id = orderDetail.priority_id " 
						 + " left join adt.m_bed_master bm on " 
						 + " 	bm.bed_id = sm.bed_id " 
						 + " left join adt.t_admission_details dtl on " 
						 + " 	dtl.admission_id = admission.admission_id " 
						 + " 	and admission.status = 'A' " 
						 + " 	and dtl.status = 'A' " 
						 + " left join doctor.m_doctor_master doctorMasterIp on " 
						 + " 	doctorMasterIp.doctor_id = admission.doctor_id " 
						 + " left join adt.m_bed_master bedMstr on " 
						 + " 	dtl.bed_id = bedMstr.bed_id " 
						 + " left join adt.m_ward_master wm on " 
						 + " 	wm.ward_id = dtl.ward_id " 
						 + " left join public.m_employee_master employeeMaster on " 
						 + " 	employeeMaster.user_id = hemoDialysisMapper.technician_id " 
						 + " left join public.m_item itemMaster on " 
						 + " 	itemMaster.id = hemoDialysisMapper.machine_id " 
						 + " left join billing.t_credit_note_detail tcnd on " 
						 + " 	tcnd.order_detail_id = orderDetail.order_details_id " 
						 + " 	and tcnd.status = 'A' " 
						 + " 	and tcnd.cancel_or_discount = 1 " 
						 + " where " 
						 + " 	serviceMaster.speciality_id =:hemodialysisSpecialityId " 
						 + " 	and orderMaster.org_id =:orgId " 
						 + " 	and orderMaster.org_unit_id =:unitId " 
						 + " 	and (orderDetail.status = 'A' and tcnd.credit_note_detail_id is null) " 
						 + " 	and hemoDialysisMapper.workbench_acceptance = 'A' ");//and orderDetail.is_order_visible_dept='A'
				
				
				if (hemodialysisAppointmentMapperDto.getFromDate() != null){
					query.append(" AND to_char(orderDetail.order_date,'YYYY-MM-DD') between :fromDate  and  :toDate ");
				}

				if(hemodialysisAppointmentMapperDto.getPatientId() > 0){
					query.append(" and orderMaster.patient_id=:patientId");
				}
				
				if(hemodialysisAppointmentMapperDto.getVisitList()!=null && hemodialysisAppointmentMapperDto.getVisitList().size() > 0){
					query.append(" and orderMaster.visit_type_id IN :visitTypeList ");
				}
				//this condition to get patient list in dialysis record screen
				if(hemodialysisAppointmentMapperDto.getTechnicianAcceptance()!=null) {
					query.append(" and hemoDialysisMapper.technician_acceptance='A' ");//A for accepted R for Rejected
				}
				
				if(hemodialysisAppointmentMapperDto.getSearchKeyword()!=null) {
					query.append(" and (lower(concat(coalesce(patientReg.first_name, ''), ' ', coalesce(patientReg.middle_name, ''), ' ', coalesce(patientReg.last_name, ''),' ', coalesce(patientReg.uhid_number, ''))) like :searchKeyword) ");
				}
				
				if(hemodialysisAppointmentMapperDto.getDialysisStatusId()!=null && hemodialysisAppointmentMapperDto.getDialysisStatusId()>0 ) {
					query.append(" and hemoDialysisMapper.dialysis_status_id=:dialysisStatusId ");
				}
				
				if(hemodialysisAppointmentMapperDto.getTechnicianId()!=null && hemodialysisAppointmentMapperDto.getTechnicianId()>0) {
					query.append(" and hemoDialysisMapper.technician_id=:technicianId ");
				}
				
				Query query2 = sessionFactory.getCurrentSession().createSQLQuery(query.toString());

				if (hemodialysisAppointmentMapperDto.getFromDate() != null){
					query2.setParameter("fromDate", hemodialysisAppointmentMapperDto.getFromDate().substring(0, 10));
					query2.setParameter("toDate", hemodialysisAppointmentMapperDto.getToDate().substring(0, 10));
				}

				if(hemodialysisAppointmentMapperDto.getPatientId() > 0){
					query2.setParameter("patientId", hemodialysisAppointmentMapperDto.getPatientId());
				}

				if(hemodialysisAppointmentMapperDto.getVisitList()!=null && hemodialysisAppointmentMapperDto.getVisitList().size()>0) {
					query2.setParameterList("visitTypeList", hemodialysisAppointmentMapperDto.getVisitList());
				}
				if(hemodialysisAppointmentMapperDto.getSearchKeyword()!=null) {
					query2.setParameter("searchKeyword",'%'+hemodialysisAppointmentMapperDto.getSearchKeyword().toLowerCase()+'%');
				}

				if(hemodialysisAppointmentMapperDto.getDialysisStatusId()!=null && hemodialysisAppointmentMapperDto.getDialysisStatusId()>0 ) {
					query2.setParameter("dialysisStatusId", hemodialysisAppointmentMapperDto.getDialysisStatusId());
				}

				if(hemodialysisAppointmentMapperDto.getTechnicianId()!=null && hemodialysisAppointmentMapperDto.getTechnicianId()>0) {
					query2.setParameter("technicianId", hemodialysisAppointmentMapperDto.getTechnicianId());
				}
				List<HemodialysisAppointmentMapperDto> listForTotalCount = query2
						.setParameter("unitId", hemodialysisAppointmentMapperDto.getUnitId())
						.setParameter("orgId", hemodialysisAppointmentMapperDto.getOrganizationId())
						.setParameter("hemodialysisSpecialityId", hemodialysisAppointmentMapperDto.getHemodialysisSpecialityId())
						.setResultTransformer(Transformers.aliasToBean(HemodialysisAppointmentMapperDto.class)).list();
				
				List<HemodialysisAppointmentMapperDto> listServiceMasterDto = query2
						.setParameter("unitId", hemodialysisAppointmentMapperDto.getUnitId())
						.setParameter("orgId", hemodialysisAppointmentMapperDto.getOrganizationId())
						.setParameter("hemodialysisSpecialityId", hemodialysisAppointmentMapperDto.getHemodialysisSpecialityId())
						.setFirstResult(hemodialysisAppointmentMapperDto.getOffset() != null ? hemodialysisAppointmentMapperDto.getOffset()  : 0)
						.setMaxResults(hemodialysisAppointmentMapperDto.getNoOfRecordsPerPage() != null ? hemodialysisAppointmentMapperDto.getNoOfRecordsPerPage()  : 10)
						.setResultTransformer(Transformers.aliasToBean(HemodialysisAppointmentMapperDto.class)).list();
				return new Response<>(SUCCESS, SUCCESS_CODE, null, listServiceMasterDto, listForTotalCount.size());
			}catch (Exception e) {
			logger.error("getPatientListForDialysisWorkload failed", e);
			}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getDialysisMachines(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
	try {
		List<HemodialysisAppointmentMapperDto> dialysisMachineList=sessionFactory.getCurrentSession()
				.createSQLQuery("select itemMaster.name as \"machineName\",itemMaster.id as \"machineId\" from public.m_item itemMaster " 
				+"where itemMaster.is_dialyser=true " 
				+"and itemMaster.organization_id=:orgId " 
				+"and itemMaster.unit_id=:unitId ")
				.setParameter("orgId", hemodialysisAppointmentMapperDto.getOrganizationId())
				.setParameter("unitId", hemodialysisAppointmentMapperDto.getUnitId())
				.setResultTransformer(Transformers.aliasToBean(HemodialysisAppointmentMapperDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, dialysisMachineList, null);
	} catch (HibernateException he) {
	logger.error("getDialysisMachines failed", he);
	}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response savePatientDialysisRecord(PatientDialysisRecordDto patientDialysisRecordDto) {
		try {
			PatientDialysisRecord patientDialysisRecord=new PatientDialysisRecord();
			patientDialysisRecord.setAppointmentId(patientDialysisRecordDto.getAppointmentId());
            patientDialysisRecord.setPatientId(patientDialysisRecordDto.getPatientId());	
            if(patientDialysisRecordDto.getTemplateTransactionId()!=null)
            patientDialysisRecord.setTemplateTransactionId(patientDialysisRecordDto.getTemplateTransactionId());
            patientDialysisRecord.setDialysisEventId(patientDialysisRecordDto.getDialysisEventId());
            if(patientDialysisRecordDto.getNetUltrafication()!=null)
            patientDialysisRecord.setDialysisNetUltrafication(patientDialysisRecordDto.getNetUltrafication());
            if(patientDialysisRecordDto.getVascularAccessType()!=null)
            patientDialysisRecord.setDialysisVascularAccessType(patientDialysisRecordDto.getVascularAccessType());
            if(patientDialysisRecordDto.getKtvResult()!=null)
            patientDialysisRecord.setDialysisKtvResult(patientDialysisRecordDto.getKtvResult());
            if(patientDialysisRecordDto.getDialysisDuration()!=null)
                patientDialysisRecord.setDialysisDuration(patientDialysisRecordDto.getDialysisDuration());
            if(patientDialysisRecordDto.getDialysisNote()!=null)
                patientDialysisRecord.setDialysisNote(patientDialysisRecordDto.getDialysisNote());	
            if(patientDialysisRecordDto.getDialysisStartTime()!=null) {
				LocalTime startTime = LocalTime.parse(patientDialysisRecordDto.getDialysisStartTime());
				LocalDate today = LocalDate.now();

				patientDialysisRecord.setDialysisStartTime(startTime.atDate(today).atZone(ZoneId.of("UTC")).toInstant());
			}
            if(patientDialysisRecordDto.getDialysisEndTime()!=null) {
				LocalTime endTime = LocalTime.parse(patientDialysisRecordDto.getDialysisEndTime());
				LocalDate today = LocalDate.now();

				patientDialysisRecord.setDialysisEndTime(endTime.atDate(today).atZone(ZoneId.of("UTC")).toInstant());
			}
            if(patientDialysisRecordDto.getDialysisStartDate()!=null)
            	patientDialysisRecord.setDialysisStartDate(GlobalCommonDateUtils.getDate(patientDialysisRecordDto.getDialysisStartDate(), "dd-M-yyyy HH:mm:ss"));
			patientDialysisRecord.setStatus('A');
			patientDialysisRecord.setOrganizationId(patientDialysisRecordDto.getOrganizationId());
			patientDialysisRecord.setUnitId(patientDialysisRecordDto.getUnitId());
			patientDialysisRecord.setCreatedBy(patientDialysisRecordDto.getCreatedBy());
			patientDialysisRecord.setCreatedDate(GlobalCommonDateUtils.getDate(patientDialysisRecordDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			patientDialysisRecord.setUpdatedBy(patientDialysisRecordDto.getUpdatedBy());
			patientDialysisRecord.setUpdatedDate(GlobalCommonDateUtils.getDate(patientDialysisRecordDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 sessionFactory.getCurrentSession().save(patientDialysisRecord);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, patientDialysisRecord);
		} catch (HibernateException he) {
		logger.error("savePatientDialysisRecord failed", he);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response uploadDialysisDocument(PatientDialysisRecordDto patientDialysisRecordDto) {
		try {
			sessionFactory.getCurrentSession().createQuery("UPDATE  PatientDialysisRecord patDialRec SET patDialRec.documentPath=:docPath "
					+ "  where patDialRec.patientDialysisRecId=:patDialRecId ")
			.setParameter("patDialRecId", patientDialysisRecordDto.getPatientDialysisRecId())
			.setString("docPath", patientDialysisRecordDto.getDocumentPath()).executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (HibernateException he) {
		logger.error("uploadDialysisDocument failed", he);
		}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientDialysisRecordDetails(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
		try {
			List<PatientDialysisRecordDto> dialysisPatientList=sessionFactory.getCurrentSession()
								.getNamedQuery("GET_PATIENT_DIALYSIS_RECORD_DETAILS")
								.setParameter("appointmentId", hemodialysisAppointmentMapperDto.getAppointmentId())
								.setResultTransformer(Transformers.aliasToBean(PatientDialysisRecordDto.class)).list();
			
			
			PatientDialysisRecordDto patientDialysisDetails=(PatientDialysisRecordDto)sessionFactory.getCurrentSession().createSQLQuery(" select hemoDialysisMapper.dialysis_status_id as \"dialysisStatusId\", "
					    +"(select pdr.dialysis_event_id from patient.t_patient_dialysis_record pdr where pdr.appointment_id=hemoDialysisMapper.appointment_id  "
					    + " order by pdr.patient_dialysis_rec_id desc limit 1) as \"dialysisEventId\", " 
					    +"(select to_char(pdr.dialysis_start_date,'DD/MM/YYYY') from patient.t_patient_dialysis_record pdr where pdr.appointment_id=hemoDialysisMapper.appointment_id  "
						+ " order by pdr.patient_dialysis_rec_id asc limit 1) as \"dialysisStartDate\", " 
						+"(select to_char(pdr.dialysis_start_time,'HH24:MI AM') from patient.t_patient_dialysis_record pdr where pdr.appointment_id=hemoDialysisMapper.appointment_id  " 
						+" order by pdr.patient_dialysis_rec_id asc limit 1) as \"dialysisStartTime\", 	"
						+"(select to_char(pdr.created_date,'DD/MM/YYYY') from patient.t_patient_dialysis_record pdr where pdr.appointment_id=hemoDialysisMapper.appointment_id " 
						+" order by pdr.patient_dialysis_rec_id desc limit 1) as \"dialysisEndDate\","
						+" (select to_char(pdr.dialysis_end_time,'HH24:MI AM') from patient.t_patient_dialysis_record pdr where pdr.appointment_id=hemoDialysisMapper.appointment_id  " 
						+" order by pdr.patient_dialysis_rec_id desc limit 1) as \"dialysisEndTime\", "
						
						+"(select pdr.dia_vascular_access_type from patient.t_patient_dialysis_record pdr where pdr.appointment_id=hemoDialysisMapper.appointment_id " 
						+" order by pdr.patient_dialysis_rec_id desc limit 1) as \"vascularAccessType\","
						+" (select pdr.dia_net_ultrafication from patient.t_patient_dialysis_record pdr where pdr.appointment_id=hemoDialysisMapper.appointment_id  " 
						+" order by pdr.patient_dialysis_rec_id desc limit 1) as \"netUltrafication\", "
						+" (select pdr.dia_ktv_result from patient.t_patient_dialysis_record pdr where pdr.appointment_id=hemoDialysisMapper.appointment_id  " 
						+" order by pdr.patient_dialysis_rec_id desc limit 1) as \"ktvResult\" "
						
						+" from opd.t_hemodialysis_appointment_mapper hemoDialysisMapper"
						+ " where hemoDialysisMapper.appointment_id=:appointmentId")
					.setParameter("appointmentId", hemodialysisAppointmentMapperDto.getAppointmentId())
					.setResultTransformer(Transformers.aliasToBean(PatientDialysisRecordDto.class)).uniqueResult();
			
			List<PatientDialysisVitalDto> dialysisVitalList =  sessionFactory.getCurrentSession().createQuery(
					" SELECT 		appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, appVitalMapp.vitalId as vitalId,"
							+ "				appVitalMapp.encounterId as encounterId,appVitalMapp.admissionId as admissionId, appVitalMapp.patientId as patientId, "
							+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
							+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy, "
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
//							+ " appVitalMapp.unitId=:unitId"
//							+ " AND			"
							+ " appVitalMapp.organizationId=:organizationId"
//							+ " AND 		appVitalMapp.appointmentId=:appointmentId"
							+ " AND 		appVitalMapp.patientId=:patientId"
							+ " AND 		appVitalMapp.status= 'A' "		
							+ " AND 		appVitalMapp.value !='' "		
							+ " AND 		appVitalMapp.isEnteredInError ='N' "
							+ "  order by	appVitalMapp.createdDate asc, vitalMst.vitalOrderOpd desc ")
					.setParameter("organizationId", hemodialysisAppointmentMapperDto.getOrganizationId())
					.setParameter("patientId", hemodialysisAppointmentMapperDto.getPatientId())
					.setResultTransformer(Transformers.aliasToBean(PatientDialysisVitalDto.class)).list();
		
			PatientDialysisRecordDto finalDto=new PatientDialysisRecordDto();
			if(patientDialysisDetails!=null) {
				finalDto.setPatientDialysisDetails(patientDialysisDetails);
			}
			if(dialysisPatientList.size()>0) {
				finalDto.setDialysisPatientList(dialysisPatientList);
			}
			
			if(dialysisVitalList.size()>0) {
				finalDto.setDialysisVitalList(dialysisVitalList);
			}
			  return new Response(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, finalDto);
			} catch (HibernateException he) {
			logger.error("getPatientDialysisRecordDetails failed", he);
			}
	return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response searchPatientForDialysis(HemodialysisAppointmentMapperDto hemodialysisAppointmentMapperDto) {
		try {

			List<PatientRegistrationDto> patientRegistrationDtosList = sessionFactory.getCurrentSession()
					.createSQLQuery("	SELECT"
							+" 	pr.patient_id as \"patientId\","
							+" 	concat(coalesce(pre.prefix_code,''),' ',coalesce( pr.first_name,''),' ',coalesce( pr.middle_name,'' ),' ',coalesce( pr.last_name,'' )) as \"patientName\","
							+" 	pr.uhid_number as \"uhidNumber\","
							+" 	pr.mobile_number as \"mobileNumber\" "
							+" 	FROM patient.t_patient_registration pr	"
							+ " left join m_prefix_master pre on pre.prefix_id = pr.prefix_id"
							+" 	INNER JOIN opd.m_appointment_master mast on pr.patient_id=mast.patient_id and  mast.status='A'"
							+ " INNER JOIN opd.t_hemodialysis_appointment_mapper hdam on hdam.appointment_id=mast.appointment_id and hdam.dialysis_status_id!=1 "
							//+" 	LEFT JOIN adt.t_admission adm on pr.patient_id=adm.patient_id and   adm.status='A'"
							+" 	WHERE	(LOWER(pr.first_name) LIKE :patientName  "
							+" 	OR LOWER(pr.last_name) LIKE :patientName "
							+" 	OR LOWER(pr.uhid_number) LIKE :patientName "
							+" 	OR LOWER(pr.mobile_number) LIKE :patientName )"
							+" 	AND pr.status = 'A'	"
							+" 	AND pr.organization_id =:organizationId")
					.setParameter("patientName", hemodialysisAppointmentMapperDto.getPatientName().toLowerCase() + "%")
					.setParameter("organizationId", hemodialysisAppointmentMapperDto.getOrganizationId())
					//.setParameter("unitId", slotMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(PatientRegistrationDto.class)).list();
			return new Response(SUCCESS, null, null, patientRegistrationDtosList, null);
		} catch (HibernateException he) {
		logger.error("searchPatientForDialysis failed", he);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

	@Override
	public Response updatePatientDialysisRecord(PatientDialysisRecordDto dto) {
		try {
			Date endTime =GlobalCommonDateUtils.getDate(dto.getDialysisEndTime(), "dd-M-yyyy HH:mm:ss");
           sessionFactory.getCurrentSession().createSQLQuery("update  patient.t_patient_dialysis_record  "
           		+ "set dialysis_end_time=:endTime,updated_date=:updatedDate where patient_dialysis_rec_id=("
   				+ "select pdr.patient_dialysis_rec_id from patient.t_patient_dialysis_record pdr where pdr.appointment_id=:appId "
           	    + " order by pdr.patient_dialysis_rec_id desc limit 1)")
           .setParameter("updatedDate", TimeZoneComponent.getDateByzone(dto.getUnitId()))
           .setParameter("endTime", endTime)
           .setParameter("appId",dto.getAppointmentId())
           .executeUpdate();
			return new Response(SUCCESS, null, null, null, null);
		} catch (HibernateException he) {
		logger.error("updatePatientDialysisRecord failed", he);
		} catch (Exception e) {
		logger.error("updatePatientDialysisRecord failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}
	
}
