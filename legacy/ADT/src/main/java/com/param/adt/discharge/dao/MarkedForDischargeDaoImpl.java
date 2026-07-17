package com.param.adt.discharge.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.discharge.model.Discharge;
import com.param.adt.housekeeping.model.Housekeeping;
import com.param.adt.housekeeping.model.HousekeepingLog;
import com.param.adt.master.global.model.BedLogStatus;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.DischargeDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings({ "rawtypes", "unchecked" })
@Repository
public class MarkedForDischargeDaoImpl extends GenericDao<Discharge, Integer>
		implements IMarkedForDischargeDao, ICommonConstants {

	public MarkedForDischargeDaoImpl() {
		super(Discharge.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	@Override
	public Response getDischagePatientListForNursing(DischargeDto dischargeDto) throws ApplicationException {
		try {

			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISCHARGE_PATIENT_LIST_FOR_NURSING").getQueryString();

			String finalQuery = null;
			List<AdmissionDto> admissionDtosList;
			if (dischargeDto.getBedList()!=null) {
				finalQuery = query.replace("bm.ward_id IN (:wardList)", "bm.bed_id IN (:bedList)");
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameterList("bedList", dischargeDto.getBedList())
						//.setDate("date", ADTCommonDateUtils.getDate(dischargeDto.getDischargeDate(), "dd-MM-yyyy"))
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}else if(dischargeDto.getAdmissionId()!=null) {
				finalQuery = query.replace("bm.ward_id IN (:wardList)", ("dis.admission_id = "+dischargeDto.getAdmissionId()));
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}
			else if(dischargeDto.getWardList()!=null && dischargeDto.getWardList().size()>0){
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameterList("wardList", dischargeDto.getWardList())
						//.setDate("date", ADTCommonDateUtils.getDate(dischargeDto.getDischargeDate(), "dd-MM-yyyy"))
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}else{
//				finalQuery=query.replace("AND (bm.ward_id IN (:wardList) OR bm.is_moving_bed='Y' )", "  OR bm.is_moving_bed='Y' ");
				finalQuery=query.replace("AND (bm.ward_id IN (:wardList) OR bm.is_moving_bed='Y' )", " ");
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						//.setDate("date", ADTCommonDateUtils.getDate(dischargeDto.getDischargeDate(), "dd-MM-yyyy"))
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}
		
			return new Response(SUCCESS, null, null, admissionDtosList, null);


		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response updatePatientServiceOrder(AdmissionDto admissionDto) throws ApplicationException {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					"update PatientServicesOrder pso set pso.orderStatusId = :orderStatusId, " +
							"pso.cancelReasonId = :reasonId, " +
							"pso.updatedBy = :updatedBy, " +
							"pso.updatedDate = :updatedDate " +
							"WHERE pso.organizationId = :organizationId " +
							"AND pso.unitId = :unitId " +
							"AND pso.patientSevicesOrderId = :patientSevicesOrderId"
			);
			query.setParameter("orderStatusId", admissionDto.getOrderStatusId())
					.setParameter("reasonId", admissionDto.getReasonId())
					.setParameter("updatedBy", admissionDto.getUpdatedBy())
					.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"))
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("patientSevicesOrderId", admissionDto.getPatientSevicesOrderId());
			query.executeUpdate();
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientReadyForBillingList(DischargeDto dischargeDto) throws ApplicationException {
		try {
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_READY_FOR_BILLING").getQueryString();

			String finalQuery;
			List<AdmissionDto> admissionDtosList;
			if (dischargeDto.getBedList()!=null) {
				finalQuery = query.replace("bm.ward_id IN (:wardList)", "bm.bed_id IN (:bedList)");
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameter("labDeptId", dischargeDto.getLabDeptId()!=null ? dischargeDto.getLabDeptId() : 0)
						.setParameter("radDeptId", dischargeDto.getRadDeptId()!=null ? dischargeDto.getRadDeptId() : 0)
						.setParameterList("bedList", dischargeDto.getBedList())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}else if(dischargeDto.getAdmissionId()!=null){ 
				finalQuery = query.replace("bm.ward_id IN (:wardList)", "discharge.admission_id=:admissionId");
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameter("labDeptId", dischargeDto.getLabDeptId()!=null ? dischargeDto.getLabDeptId() : 0)
						.setParameter("radDeptId", dischargeDto.getRadDeptId()!=null ? dischargeDto.getRadDeptId() : 0)
						.setParameter("admissionId", dischargeDto.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}else {
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameter("labDeptId", dischargeDto.getLabDeptId()!=null ? dischargeDto.getLabDeptId() : 0)
						.setParameter("radDeptId", dischargeDto.getRadDeptId()!=null ? dischargeDto.getRadDeptId() : 0)
						.setParameterList("wardList", dischargeDto.getWardList())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}
		
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updatePatientReadyForBillingStatus(DischargeDto dischargeDto) throws ApplicationException {
		try {
			Query query = sessionFactory.getCurrentSession().createQuery(
					"update Discharge dis set dis.updatedBy = :updatedBy, " +
							"dis.updatedDate = :updatedDate, " +
							"dis.dischargeStatusId = :dischargeStatusId " +
							"WHERE dis.dischargeId = :dischargeId " +
							"AND dis.organizationId = :organizationId " +
							"AND dis.unitId = :unitId " +
							"AND dis.status = 'A'");

			query.setParameter("updatedBy", dischargeDto.getUpdatedBy());
			query.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			query.setParameter("dischargeStatusId", dischargeDto.getDischargeStatusId());
			query.setParameter("dischargeId", dischargeDto.getDischargeId());
			query.setParameter("organizationId", dischargeDto.getOrganizationId());
			query.setParameter("unitId", dischargeDto.getUnitId());
			query.executeUpdate();

			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getPatientFinalDischargeList(DischargeDto dischargeDto) throws ApplicationException {
		try {
			String query = sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_FINAL_DISCHARGE_LIST").getQueryString();

			String finalQuery;
			List<AdmissionDto> admissionDtosList;
			if (dischargeDto.getBedList()!=null) {
				finalQuery = query.replace("bm.ward_id IN (:wardList)", "bm.bed_id IN (:bedList)");
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameter("unitId", dischargeDto.getUnitId())
//						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setParameterList("bedList", dischargeDto.getBedList())
						//.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			} else if(dischargeDto.getAdmissionId()!=null){
				finalQuery = query.replace("bm.ward_id IN (:wardList)", "discharge.admission_id=:admissionId");
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameter("unitId", dischargeDto.getUnitId())
//						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setParameter("admissionId", dischargeDto.getAdmissionId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}else {
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
//						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setParameterList("wardList", dischargeDto.getWardList())
						//.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}
		
			return new Response(SUCCESS, null, null, admissionDtosList, null);


		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response vacateBed(DischargeDto dischargeDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			// <-----Updating status of Admission to I
			Query query1 = session.createQuery(
					"update Admission adm set adm.updatedBy = :updatedBy, " +
							"adm.updatedDate = :updatedDate, " +
							"adm.status = 'I' " +
							"WHERE adm.admissionId = :admissionId " +
							"AND adm.organizationId = :organizationId " +
							"AND adm.unitId = :unitId");

			query1.setParameter("updatedBy", dischargeDto.getUpdatedBy());
			query1.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			query1.setParameter("admissionId", dischargeDto.getAdmissionId());
			query1.setParameter("organizationId", dischargeDto.getOrganizationId());
			query1.setParameter("unitId", dischargeDto.getUnitId());
			query1.executeUpdate();

			// ----->

			// <-----Updating status of AdmissionDetails to I AND isDischarge to D
			Query query2 = session.createQuery(
					"update AdmissionDetails adm set adm.updatedBy = :updatedBy, " +
							"adm.updatedDate = :updatedDate, " +
							"adm.status = 'I', " +
							"adm.isDischarge = 'D' " +
							"WHERE adm.status = 'A' " +
							"AND adm.admissionId = :admissionId " +
							"AND adm.organizationId = :organizationId " +
							"AND adm.unitId = :unitId");

			query2.setParameter("updatedBy", dischargeDto.getUpdatedBy());
			query2.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			query2.setParameter("admissionId", dischargeDto.getAdmissionId());
			query2.setParameter("organizationId", dischargeDto.getOrganizationId());
			query2.setParameter("unitId", dischargeDto.getUnitId());
			query2.executeUpdate();

			// ----->

			// <-----Updating status of Discharge to 5(DISCHARGED)
			Query query5 = session.createQuery(
					"update Discharge dis set dis.updatedBy = :updatedBy, " +
							"dis.updatedDate = :updatedDate, " +
							"dis.dischargeStatusId = 5 " +
							"WHERE dis.dischargeId = :dischargeId " +
							"AND dis.organizationId = :organizationId " +
							"AND dis.unitId = :unitId");
			query5.setParameter("updatedBy", dischargeDto.getUpdatedBy());
			query5.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
			query5.setParameter("dischargeId", dischargeDto.getDischargeId());
			query5.setParameter("organizationId", dischargeDto.getOrganizationId());
			query5.setParameter("unitId", dischargeDto.getUnitId());
			query5.executeUpdate();

			// ----->

			if (dischargeDto.getBedId() != null) 
			{
				// <-----Updating status of AdmissionDetails to I
				Query query3 = session.createQuery(
						"update BedMaster bm set bm.updatedBy = :updatedBy, " +
								"bm.updatedDate = :updatedDate, " +
								"bm.bedStatusId = :bedStatusId " +
								"WHERE bm.bedId = :bedId " +
								"AND bm.organizationId = :organizationId " +
								"AND bm.unitId = :unitId");
				query3.setParameter("updatedBy", dischargeDto.getUpdatedBy());
				query3.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				query3.setParameter("bedStatusId", dischargeDto.getBedStatusId());
				query3.setParameter("bedId", dischargeDto.getBedId());
				query3.setParameter("organizationId", dischargeDto.getOrganizationId());
				query3.setParameter("unitId", dischargeDto.getUnitId());
				query3.executeUpdate();

				// ----->

				// <-----Updating status of AdmissionDetails to I
				Query query4 = session.createQuery("update BedLogStatus bm set bm.updatedBy=:updatedBy, " +
						"bm.updatedDate=:updatedDate, "+
						"bm.status='I' WHERE bm.bedId=:bedId " +
						"AND bm.organizationId=:organizationId "+
						" AND bm.unitId=:unitId ");
				query4.setParameter("updatedBy", dischargeDto.getUpdatedBy());
				query4.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
				query4.setParameter("bedId", dischargeDto.getBedId());
				query4.setParameter("organizationId", dischargeDto.getOrganizationId());
				query4.setParameter("unitId", dischargeDto.getUnitId());
				query4.executeUpdate();
				// ----->

				// <-----Adding new entry in BedLogStatus
				BedLogStatus bedLogStatus = mapper.map(dischargeDto, BedLogStatus.class,
						"DischargeDto_to_BedLogStatus");
				bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"));
				bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"));
				session.save(bedLogStatus);
				// ----->
				
				
				if(dischargeDto.getDischargeTypeId()!=null && dischargeDto.getDischargeTypeId()==3){
					Query abscondQuery = session.createQuery(
							"update PatientRegistration  set status='I' "					
							+ "where patientId=:patientId");						
					abscondQuery.setParameter("patientId",dischargeDto.getPatientId());
					abscondQuery.executeUpdate();
				}
				
				// <-----Adding new entry in Housekeeping
				Housekeeping housekeeping = new Housekeeping();
					housekeeping.setOrganizationId(dischargeDto.getOrganizationId());
					housekeeping.setUnitId(dischargeDto.getUnitId());
					housekeeping.setNursingStationId(dischargeDto.getNursingStationId());
					housekeeping.setFloorId(dischargeDto.getFloorId());
					housekeeping.setWardId(dischargeDto.getWardId());
					housekeeping.setRoomId(dischargeDto.getRoomId());
					housekeeping.setBedId(dischargeDto.getBedId());
					housekeeping.setBedCategoryId(dischargeDto.getBedCategoryId());
					
					if(dischargeDto.getIsIcu()=='Y')
						housekeeping.setPriorityId(1);
					else
						housekeeping.setPriorityId(2);
					
					housekeeping.setHousekeepingStatusId(1);
					housekeeping.setStatus('A');
					housekeeping.setCreatedBy(dischargeDto.getUpdatedBy());
					housekeeping.setUpdatedBy(dischargeDto.getUpdatedBy());
					housekeeping.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"));
					housekeeping.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId()),"dd-MM-yyyy HH:mm:ss"));
				Integer id = (Integer)session.save(housekeeping);
				//System.out.println(id);
				dischargeDto.setHousekeepingId(id);
				// ----->
			}
			return new Response(SUCCESS, null, null, null, dischargeDto);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveHousekeepingLog(DischargeDto dischargeDto2) throws ApplicationException {
		try {	
			Session session=sessionFactory.getCurrentSession();
			HousekeepingLog housekeepingLog=new HousekeepingLog();
				housekeepingLog.setHousekeepingId(dischargeDto2.getHousekeepingId());
				housekeepingLog.setOrganizationId(dischargeDto2.getOrganizationId());
				housekeepingLog.setUnitId(dischargeDto2.getUnitId());
				housekeepingLog.setPriorityId(dischargeDto2.getPriorityId());
				housekeepingLog.setHousekeepingStatusId(1);
				housekeepingLog.setCreatedBy(dischargeDto2.getUpdatedBy());
				housekeepingLog.setUpdatedBy(dischargeDto2.getUpdatedBy());
				housekeepingLog.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto2.getUnitId() != null ? dischargeDto2.getUnitId() :0),"dd-MM-yyyy HH:mm:ss"));
				housekeepingLog.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dischargeDto2.getUnitId() != null ? dischargeDto2.getUnitId() :0),"dd-MM-yyyy HH:mm:ss"));
				housekeepingLog.setStatus('A');
			session.save(housekeepingLog);
			return new Response(SUCCESS, null, null, null, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getAdmissionDetailsByAdmissionId(DischargeDto dischargeDto) throws ApplicationException {
    try{
			String sqlQuery="select " 
							+ " 	admission.admission_id as \"admissionId\", " 
							+ " 	admission.patient_id as \"patientId\", " 
							+ " 	admission.visit_type_id as \"visitTypeId\", " 
							+ " 	admission.doctor_id as \"doctorId\", " 
							+ " 	admission.speciality_id as \"specialityId\", " 
							+ " 	spd.contract_id as \"contractId\", " 
							+ " 	spd.payee_id as \"payeeId\", " 
							+ " 	adl.billing_bed_category_id as \"billingBedCategoryId\", " 
							+ " 	bedCatMst.bed_category_id as \"bedCategoryId\", " 
							+ " 	patReg.patient_category_id as \"patientTypeId\", "
							+ "		admission.organization_id as \"organizationId\", "
							+ "		admission.unit_id as \"unitId\", "
			                 + "    adl.ward_id as \"wardId\" ," 
			                 + "    adl.room_id as \"roomId\" ,"
			                 + "    adl.bed_id as \"bedId\" ,"
							+ "     to_char(admission.created_date ,'dd/MM/yyyy HH24:MI:ss') as \"admissionDateTime\", "
							+ "		coalesce(bedCatMst.service_id,0) as \"serviceIdForBedCharges\" "
							+ " from " 
							+ " 	adt.t_admission admission " 
							+ " inner join adt.t_admission_details adl on " 
							+ " 	adl.admission_id = admission.admission_id  " 
							+ " inner join opd.t_patient_sponsor_details spd on " 
							+ " 	spd.admission_id = admission.admission_id and spd.priority_id = 1 and spd.status='A' " 
							+ " inner join adt.m_bed_category_master bedCatMst on " 
							+ " 	adl.bed_category_id = bedCatMst.bed_category_id " 
							+ " inner join patient.t_patient_registration patReg on " 
							+ " 	patReg.patient_id = admission.patient_id "
							+ " where " 
							+ " 	admission.status = 'A' " 
							+ " 	and adl.status = 'A' "
							+ " 	and adl.nursing_acceptance = 'Y' "
							+ "     and admission.admission_id=:admissionId " ;
			
			AdmissionsForBatchProcessDto admissionForBatchDto = (AdmissionsForBatchProcessDto)sessionFactory.getCurrentSession().createSQLQuery(sqlQuery)
					.setParameter("admissionId", dischargeDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionsForBatchProcessDto.class)).uniqueResult();
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, admissionForBatchDto);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	public Response getDischagePatientListByDoctorId(DischargeDto dischargeDto) throws ApplicationException {
		try {

			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PATIENT_LIST_FOR_DISCHARGE_SUMMARY").getQueryString());
			
			List<AdmissionDto> admissionDtosList=new ArrayList<AdmissionDto>();
			 if(dischargeDto.getRmoMappedDoctorList()!=null) {
				 query.append(" and admission.doctor_id in(:doctorIdList)) ");
				 query.append( " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
			     admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("orgId", dischargeDto.getOrganizationId())
							.setParameter("unitId", dischargeDto.getUnitId())
							.setParameterList("doctorIdList", dischargeDto.getRmoMappedDoctorList())
							.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			 }else if(dischargeDto.getRmoMappedWardList()!=null) {
				query.append(" and adl.ward_id in (:wardList) ");
				  if(dischargeDto.getVisitTypeId()!=OP) {
				    	 query.append(" and admission.visit_type_id="+dischargeDto.getVisitTypeId());
				    }
				query.append( " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");//\"dischargeId\" desc limit 1
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("orgId", dischargeDto.getOrganizationId())
							.setParameter("unitId", dischargeDto.getUnitId())
							.setParameterList("wardList", dischargeDto.getRmoMappedWardList())
							.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset()  : 0)
							.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage()  : 10)
							.setParameter("doctorId", dischargeDto.getDoctorId())
							.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			     
			     List<AdmissionDto> admissionDto = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("orgId", dischargeDto.getOrganizationId())
							.setParameter("unitId", dischargeDto.getUnitId())
							.setParameterList("wardList", dischargeDto.getRmoMappedWardList())
							.setParameter("doctorId", dischargeDto.getDoctorId())
							.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
					     if(admissionDto.size()>0) {
					        	dischargeDto.setPatientCount(admissionDto.size());
					        }
			 }else {
				  query.append(" and (admission.doctor_id =:doctorId "
				 		+ " OR (((case when (select specMa.speciality_id from doctor.t_doctor_speciality_mapper specMa where specMa.doctor_id=:doctorId and specMa.status ='A' "
				 		+ " and  specMa.speciality_id=("  
				 		+" select app.er_speciality_id from t_unit_application_billing_configuration app where app.organization_id=:orgId and app.unit_id=:unitId ) limit 1 )>0 then true else false end))))");
				
				  if(dischargeDto.getVisitTypeId()!=OP) {
				    	 query.append(" and admission.visit_type_id="+dischargeDto.getVisitTypeId());
				    }
				  
				  query.append( " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc,\"admissionId\" desc ");//\"dischargeId\" desc limit 1,
				 admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("orgId", dischargeDto.getOrganizationId())
							.setParameter("unitId", dischargeDto.getUnitId())
							.setParameter("doctorId", dischargeDto.getDoctorId())
							.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset()  : 0)
							.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage()  : 10)
							.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			       
			        List<AdmissionDto> admissionDto = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("orgId", dischargeDto.getOrganizationId())
							.setParameter("unitId", dischargeDto.getUnitId())
							.setParameter("doctorId", dischargeDto.getDoctorId())
							.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			        if(admissionDto.size()>0) {
			        	dischargeDto.setPatientCount(admissionDto.size());
			        }
			 }
	
				if(admissionDtosList.size()>0){
				   return new Response(SUCCESS, null, null, admissionDtosList, dischargeDto);
				}else{
					 return new Response(ERROR, null, "NO patient marked for discharge", null, null);
				}
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getPatientDischargedList(DischargeDto dischargeDto) throws ApplicationException {
		try {
			Date fromDate = GlobalCommonDateUtils.getDate(dischargeDto.getFromDate()!=null && dischargeDto.getFromDate()!="" ? dischargeDto.getFromDate() : GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId() != null ? dischargeDto.getUnitId() :0), "yyyy-MM-dd") , "yyyy-MM-dd");
			Date toDate = GlobalCommonDateUtils.getDate(dischargeDto.getToDate() !=null  && dischargeDto.getToDate()!="" ? dischargeDto.getToDate() :  GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId() != null ? dischargeDto.getUnitId() :0), "yyyy-MM-dd") , "yyyy-MM-dd");
			StringBuilder query =new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_DISCHARGED_LIST").getQueryString());
			if(dischargeDto.getPatientName()!=null && dischargeDto.getPatientName()!="") {
				query.append("AND (LOWER(pr.first_name) LIKE :patientName OR LOWER(pr.last_name) LIKE :patientName) ");
			}
			if(dischargeDto.getBedNumber()!=null && dischargeDto.getBedNumber()!="") {
				query.append("AND bm.bed_number LIKE :bedNumber ");
			}
			  query.append( " order by \"admissionId\" ");
			
			Query sql = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate);
			if(dischargeDto.getPatientName()!=null && dischargeDto.getPatientName()!="") {
				sql.setParameter("patientName", "%" + dischargeDto.getPatientName().toLowerCase() + "%");
			}
			if(dischargeDto.getBedNumber()!=null && dischargeDto.getBedNumber()!="") {
				 sql.setParameter("bedNumber", "%" + dischargeDto.getBedNumber() + "%");
			}
			List<AdmissionDto> admissionDtosList =	sql.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			
		
			return new Response(SUCCESS, null, null, admissionDtosList, null);


		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getPatientDischargedListForPrint(DischargeDto dischargeDto) throws ApplicationException {
		try {
			Date fromDate = GlobalCommonDateUtils.getDate(dischargeDto.getFromDate()!=null && dischargeDto.getFromDate()!="" ? dischargeDto.getFromDate() : GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId() != null ? dischargeDto.getUnitId():0), "yyyy-MM-dd") , "yyyy-MM-dd");
			Date toDate = GlobalCommonDateUtils.getDate(dischargeDto.getToDate() !=null  && dischargeDto.getToDate()!="" ? dischargeDto.getToDate() :  GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dischargeDto.getUnitId() != null ? dischargeDto.getUnitId():0), "yyyy-MM-dd") , "yyyy-MM-dd");
			
			StringBuilder query =new StringBuilder(sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_DISCHARGED_LIST_FOR_PRINT").getQueryString());
			
			if(dischargeDto.getDoctorId()!=null && dischargeDto.getDoctorId() > 0) {
				query.append(" and admission.doctor_id =:doctorId ");
			}

			if(dischargeDto.getPatientId()!=null && dischargeDto.getPatientId() > 0) {
				query.append(" and admission.patient_id =:patientId ");
			}
			
			if(dischargeDto.getFloorId()!=null && dischargeDto.getFloorId() > 0) {
				query.append(" and admissionDetails.floor_id =:floorId ");
			}
			if(dischargeDto.getWardId()!=null && dischargeDto.getWardId() > 0) {
				query.append(" and admissionDetails.ward_id =:wardId ");
			}
			
			  //query.append( " order by \"admissionId\" ");
			
			Query sql = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameter("fromDate", fromDate)
						.setParameter("toDate", toDate)
						.setParameterList("visitTypeIds", dischargeDto.getVisitTypeIds())
					.setParameter("orgId", dischargeDto.getOrganizationId());
			if(dischargeDto.getDoctorId()!=null && dischargeDto.getDoctorId() > 0)
			{
				sql.setParameter("doctorId", dischargeDto.getDoctorId());
			}
			if(dischargeDto.getPatientId()!=null && dischargeDto.getPatientId() > 0){
				sql.setParameter("patientId", dischargeDto.getPatientId());
			}
			if(dischargeDto.getFloorId()!=null && dischargeDto.getFloorId() > 0){
				sql.setParameter("floorId", dischargeDto.getFloorId());
			}
			if(dischargeDto.getWardId()!=null && dischargeDto.getWardId() > 0){
				sql.setParameter("wardId", dischargeDto.getWardId());
			}
			List<AdmissionDto> admissionDtosList = 	sql.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			
		
			return new Response(SUCCESS, null, null, admissionDtosList, null);


		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getPatientFinalDischargeListNewForListAndCount(DischargeDto dischargeDto)
	throws ApplicationException {
	try {
	String query = sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_FINAL_DISCHARGE_LIST_NEW_FOR_LIST_AND_COUNT").getQueryString().toString();
	StringBuilder sb = new StringBuilder(query);
	StringBuilder sb1 = sb;
	Integer count=0;
	String finalQuery;
	Query createdQuery = sessionFactory.getCurrentSession()
	.createSQLQuery(sb.toString());
	List<AdmissionDto> admissionDtosList;
	List<AdmissionDto> listcount;
	if (dischargeDto.getBedList()!=null) {
	finalQuery = query.replace("bm.ward_id IN (:wardList)", "bm.bed_id IN (:bedList)");
	admissionDtosList = createdQuery
	.setParameter("orgId", dischargeDto.getOrganizationId())
	.setParameter("unitId", dischargeDto.getUnitId())
	.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
	.setParameterList("bedList", dischargeDto.getBedList())
	.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset() : 0)
	.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage() : 10)
	//.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
	.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
	} else if(dischargeDto.getAdmissionId()!=null){
	finalQuery = query.replace("bm.ward_id IN (:wardList)", "discharge.admission_id=:admissionId");
	admissionDtosList = createdQuery
	.setParameter("orgId", dischargeDto.getOrganizationId())
	.setParameter("unitId", dischargeDto.getUnitId())
	.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
	.setParameter("admissionId", dischargeDto.getAdmissionId())
	.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset() : 0)
	.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage() : 10)
	.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
	}else {
	admissionDtosList = createdQuery
	.setParameter("orgId", dischargeDto.getOrganizationId())
	.setParameter("unitId", dischargeDto.getUnitId())
	// .setParameter("visitTypeId", dischargeDto.getVisitTypeId())
	.setParameterList("wardList", dischargeDto.getWardList())
	.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
	.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset() : 0)
	.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage() : 10)
	.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
	}
	Query createdQuery1 = sessionFactory.getCurrentSession()
	.createSQLQuery(sb1.toString());

	if (dischargeDto.getBedList()!=null) {
	finalQuery = query.replace("bm.ward_id IN (:wardList)", "bm.bed_id IN (:bedList)");
	listcount = createdQuery1
	.setParameter("orgId", dischargeDto.getOrganizationId())
	.setParameter("unitId", dischargeDto.getUnitId())
	.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
	.setParameterList("bedList", dischargeDto.getBedList())
	//.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
	.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
	} else if(dischargeDto.getAdmissionId()!=null){
	finalQuery = query.replace("bm.ward_id IN (:wardList)", "discharge.admission_id=:admissionId");
	listcount = createdQuery1
	.setParameter("orgId", dischargeDto.getOrganizationId())
	.setParameter("unitId", dischargeDto.getUnitId())
	.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
	.setParameter("admissionId", dischargeDto.getAdmissionId())
	.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
	}else {
	listcount = createdQuery1
	.setParameter("orgId", dischargeDto.getOrganizationId())
	.setParameter("unitId", dischargeDto.getUnitId())
	// .setParameter("visitTypeId", dischargeDto.getVisitTypeId())
	.setParameterList("wardList", dischargeDto.getWardList())
	.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
	.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
	}
	if(listcount != null && listcount.size()>0) {
	count= listcount.size();
	}
	return new Response(SUCCESS, null, null, admissionDtosList, count);


	} catch (HibernateException he) {

	} catch (Exception e) {
	
	}
	return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getDischargePatientListForNursingByVisitTypeId(DischargeDto dischargeDto)
			throws ApplicationException {
		try {

			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISCHARGE_PATIENT_LIST_FOR_NURSING_VISIT_TYPE_ID").getQueryString();

			String finalQuery = null;
			List<AdmissionDto> admissionDtosList;
			String finalQuery1 = null;
			List<AdmissionDto> admissionDtosList1;
			Integer count = 0;
			
			
			if (dischargeDto.getBedList()!=null) {
				finalQuery = query.replace("bm.ward_id IN (:wardList)", "bm.bed_id IN (:bedList)");
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameterList("bedList", dischargeDto.getBedList())
						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset() : 0)
						.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
				finalQuery1 = query.replace("bm.ward_id IN (:wardList)", "bm.bed_id IN (:bedList)");
				admissionDtosList1 = sessionFactory.getCurrentSession().createSQLQuery(finalQuery1.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameterList("bedList", dischargeDto.getBedList())
						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
				
			}else if(dischargeDto.getAdmissionId()!=null) {
				finalQuery = query.replace("bm.ward_id IN (:wardList)", ("dis.admission_id = "+dischargeDto.getAdmissionId()));
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset() : 0)
						.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
				finalQuery1 = query.replace("bm.ward_id IN (:wardList)", ("dis.admission_id = "+dischargeDto.getAdmissionId()));
				admissionDtosList1 = sessionFactory.getCurrentSession().createSQLQuery(finalQuery1.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
			}
			else if(dischargeDto.getWardList()!=null && dischargeDto.getWardList().size()>0){
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameterList("wardList", dischargeDto.getWardList())
						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset() : 0)
						.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
				admissionDtosList1 = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameterList("wardList", dischargeDto.getWardList())
						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
			}else{
				
				finalQuery=query.replace("AND (bm.ward_id IN (:wardList) OR bm.is_moving_bed='Y' )", " ");
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset() : 0)
						.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
				finalQuery1=query.replace("AND (bm.ward_id IN (:wardList) OR bm.is_moving_bed='Y' )", " ");
				admissionDtosList1 = sessionFactory.getCurrentSession().createSQLQuery(finalQuery1.toString())
						.setParameter("orgId", dischargeDto.getOrganizationId())
						.setParameter("unitId", dischargeDto.getUnitId())
						.setParameter("visitTypeId", dischargeDto.getVisitTypeId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}
			
			if(admissionDtosList1 != null && admissionDtosList1.size() > 0) {
				count = admissionDtosList1.size();
			}
		
			return new Response(SUCCESS, null, null, admissionDtosList, count);


		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	public Response getDischargedPatientsList(DischargeDto dischargeDto) throws ApplicationException {

		try {
		String query = sessionFactory.getCurrentSession().getNamedQuery("GET_DISCHARGED_PATIENT_LIST_AND_COUNT").getQueryString().toString();
		StringBuilder sb = new StringBuilder(query);
		Integer count=0;
		List<AdmissionDto> admissionDtosList=null;
		List<AdmissionDto> listcount=null;
		
		
		 if (dischargeDto.getFromDate() != null ) 
	        {
	            sb.append( " AND to_char(discharge.discharge_date,'YYYY-MM-DD') >=:fromDate ");
	        }

	        if (dischargeDto.getToDate() != null ) {
	            sb.append(" AND to_char(discharge.discharge_date,'YYYY-MM-DD') <=:toDate ");
	        }
		
		if(dischargeDto.getDoctorId()!=null) {
			sb.append(" and admission.doctor_id=:doctorId ");
		}
		
		if(dischargeDto.getDischargeTypeId()!=null) {
			sb.append(" and discharge.discharge_type_id=:dischargeTypeId ");
		}
		
		if(dischargeDto.getAdmissionTypeId()!=null) {
			sb.append(" and admissionDetails.admission_type_id=:admissionTypeId ");
		}
		if(dischargeDto.getVisitTypeId()>0) {
			sb.append(" and admission.visit_type_id=:visitTypeId ");
		}
		
		if(dischargeDto.getSearchKey()!=null) {
			String searchKey = "%" + dischargeDto.getSearchKey().toLowerCase() + "%";
			sb.append(" AND (LOWER(pr.first_name) LIKE :searchKey OR LOWER(pr.last_name) LIKE :searchKey OR LOWER(pr.uhid_number) LIKE :searchKey) ");
		}
		
		
		Query createdQuery = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		createdQuery
		.setParameter("orgId", dischargeDto.getOrganizationId())
		.setParameter("unitId", dischargeDto.getUnitId());
			if (dischargeDto.getFromDate() != null) {
				createdQuery.setParameter("fromDate", dischargeDto.getFromDate());
			}

			if (dischargeDto.getToDate() != null) {
				createdQuery.setParameter("toDate", dischargeDto.getToDate());
			}

			if (dischargeDto.getDoctorId() != null) {
				createdQuery.setParameter("doctorId", dischargeDto.getDoctorId());
			}

			if (dischargeDto.getDischargeTypeId() != null) {
				createdQuery.setParameter("dischargeTypeId", dischargeDto.getDischargeTypeId());
			}

			if (dischargeDto.getAdmissionTypeId() != null) {
				createdQuery.setParameter("admissionTypeId", dischargeDto.getAdmissionTypeId());
			}

			if (dischargeDto.getVisitTypeId() > 0) {
				createdQuery.setParameter("visitTypeId", dischargeDto.getVisitTypeId());
			}

			if (dischargeDto.getSearchKey() != null) {
				createdQuery.setParameter("searchKey", "%" + dischargeDto.getSearchKey().toLowerCase() + "%");
			}
			createdQuery.setFirstResult(dischargeDto.getOffset() != null ? dischargeDto.getOffset() : 0)
			.setMaxResults(dischargeDto.getNoOfRecordsPerPage() != null ? dischargeDto.getNoOfRecordsPerPage() : 10);
			admissionDtosList=	createdQuery.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
		
		Query createdQuery1 = sessionFactory.getCurrentSession().createSQLQuery(sb.toString());
		createdQuery1.setParameter("orgId", dischargeDto.getOrganizationId())
		.setParameter("unitId", dischargeDto.getUnitId());
			if (dischargeDto.getFromDate() != null) {
				createdQuery1.setParameter("fromDate", dischargeDto.getFromDate());
			}

			if (dischargeDto.getToDate() != null) {
				createdQuery1.setParameter("toDate", dischargeDto.getToDate());
			}

			if (dischargeDto.getDoctorId() != null) {
				createdQuery1.setParameter("doctorId", dischargeDto.getDoctorId());
			}

			if (dischargeDto.getDischargeTypeId() != null) {
				createdQuery1.setParameter("dischargeTypeId", dischargeDto.getDischargeTypeId());
			}

			if (dischargeDto.getAdmissionTypeId() != null) {
				createdQuery1.setParameter("admissionTypeId", dischargeDto.getAdmissionTypeId());
			}

			if (dischargeDto.getVisitTypeId() > 0) {
				createdQuery1.setParameter("visitTypeId", dischargeDto.getVisitTypeId());
			}

			if (dischargeDto.getSearchKey() != null) {
				createdQuery1.setParameter("searchKey", "%" + dischargeDto.getSearchKey().toLowerCase() + "%");
			}
			listcount = createdQuery1.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
		if(listcount!=null && listcount.size()>0) {
			count=listcount.size();
		}
		
		return new Response(SUCCESS, null, null, admissionDtosList, count);


		} catch (HibernateException he) {

		} catch (Exception e) {
		
		}
		return new Response(ERROR, null, null, null, null);
		
	}




}
