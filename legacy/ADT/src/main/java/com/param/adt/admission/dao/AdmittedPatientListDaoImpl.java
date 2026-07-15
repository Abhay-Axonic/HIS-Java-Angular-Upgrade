package com.param.adt.admission.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.master.unit.dao.IBedMasterDao;
import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.admission.model.Admission;
import com.param.adt.admission.model.AdmissionNote;
import com.param.adt.common.ADTCommonDateUtils;
import com.param.adt.common.ICommonConstants;
import com.param.adt.common.Response;
import com.param.adt.master.global.model.BedLogStatus;
import com.param.adt.master.unit.dto.WardMasterDto;
import com.param.global.billing.dto.BillPayeeDetailsResDto;
import com.param.global.dao.IKinDetailsDao;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.AdmissionNoteDto;
import com.param.global.dto.AdmittedPatientListDto;
import com.param.global.dto.KinDetailsDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.VitalUploaderAdmissionDto;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.opd.coversheet.dao.IAllergyPatientMapperDao;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class AdmittedPatientListDaoImpl extends GenericDao<Admission, Integer>
implements IAdmittedPatientListDao, ICommonConstants {

	public AdmittedPatientListDaoImpl() {
		super(Admission.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;
	
	@Autowired
	private IAllergyPatientMapperDao iAllergyPatientMapperDao;
	
	@Autowired
	private IKinDetailsDao iKinDetailsDao;

	@Autowired
	IBedMasterDao iBedMasterDao;


	@Override
	public Response getAdmittedPatientList(AdmissionDto admissionDto) throws ApplicationException {
		try {
			List<AdmissionDto> admissionDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveWardListByFloorId(WardMasterDto wardMasterDto) throws ApplicationException {
		try {
			List<AdmissionDto> admissionDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_WARD_BY_FLOOR_ID")
					.setParameter("organizationId", wardMasterDto.getOrganizationId())
					.setParameter("unitId", wardMasterDto.getUnitId()).setParameter("floorId", wardMasterDto.getFloorId())
					.setResultTransformer(Transformers.aliasToBean(WardMasterDto.class)).list();
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getKinDetailsByAdmissionId(AdmissionDto admissionDto) throws ApplicationException {
		try {
			Session session=sessionFactory.getCurrentSession();
			SQLQuery query=session.createSQLQuery("SELECT kin.kin_id as \"kinDetailsId\", "
					+ "kin.visit_type_id as \"visitTypeId\", "
					+ "kin.prefix_id as \"prefixId\", "
					+ "prefix.prefix_code as \"prefixCode\", "
					+ "kin.kin_name as \"kinName\", "
					+ "kin.relation_id as \"relationId\", "
					+ "relation.relation_name as \"relationDesc\", "
					+ "kin.mobile_no as \"mobileNo\", "
					+ "kin.phone_no as \"phoneNo\", "
					+ "kin.identification_id as \"identificationId\", "
					+ "id.identification_name as \"identificationName\", "
					+ "kin.identification_no as \"identificationNo\", "
					+ "kin.address as \"address\" "
					+ "FROM public.m_kin_details kin "
					+ "INNER JOIN  public.m_prefix_master prefix ON  prefix.prefix_id = kin.prefix_id "
					+ "INNER JOIN  public.m_relation_master relation ON  relation.relation_id = kin.relation_id "
					+ "INNER JOIN  adt.m_identification_master id ON id.identification_id = kin.identification_id "
					+ "WHERE kin.organization_id=:organizationId "
					+ "AND kin.unit_id=:unitId "
					+ "AND kin.status='A' "
					+ "AND kin.admission_id=:admissionId ");
			List<AdmissionDto> admissionDtosList = query
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("admissionId", admissionDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(KinDetailsDto.class)).list();
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmittedPatientByFloorWard(AdmissionDto admissionDto) throws ApplicationException {
		try {

//			Session session = sessionFactory.getCurrentSession();
//			Query queryString = sessionFactory.getCurrentSession().getNamedQuery("GET_ADMITTED_PATIENT_LIST");
			Query queryString = sessionFactory.getCurrentSession().getNamedQuery("GET_ADMITTED_PATIENT_LIST_FOR_PAGINATION");
			
			String arr[] = queryString.getQueryString().split(" order by ");
//			String arr1[] = arr[1].split(" order by ");
			
			
			StringBuilder stringQuery = new StringBuilder(arr[0]);
			if (admissionDto.getFloorId() > 0 && admissionDto.getWardId() == 0) 
			{
				stringQuery.append(" AND bm.floor_id =:floorId ");
				//System.out.println(stringQuery);
			} else if (admissionDto.getFloorId() != 0 && admissionDto.getWardId() != 0) {
				stringQuery.append(
						" AND bm.floor_id=:floorId AND bm.ward_id=:wardId ");
			} 
			
//			stringQuery.append(" UNION");
//			stringQuery.append(arr1[0]);
			if (admissionDto.getFloorId() > 0 && admissionDto.getWardId() == 0) {
				stringQuery.append(" AND bm.floor_id=:floorId ");
				//System.out.println(stringQuery);
			} else if (admissionDto.getFloorId() != 0 && admissionDto.getWardId() != 0) {
				stringQuery.append(
						" AND bm.floor_id=:floorId AND bm.ward_id=:wardId" );
			} 
			
			//admissionType 
			if (admissionDto.getAdmissionTypeId() != null ) {
				stringQuery.append(" AND adl.admission_type_id=:admissionTypeId");
			}
			
			//doctor Id 
			
			if ( admissionDto.getDoctorId() != null ) {
				stringQuery.append(" AND admission.doctor_id=:doctorId" );
			}
			
			//admission Date 
			if ( admissionDto.getDoa() != null ) {
				stringQuery.append(" AND to_char(adl.doa, 'YYYY-MM-DD')=:doa");
			}
			if ( admissionDto.getPaymentEntitlementId() != null ) {
				stringQuery.append(" AND pe.payment_entitlement_id=:paymentEntitlementId ");
			}
			
			stringQuery.append(" order by \"admissionId\" desc");
			Query newQuery = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("organizationId", admissionDto.getOrganizationId());

			if (admissionDto.getFloorId() > 0 && admissionDto.getWardId() == 0)
			{
				newQuery.setParameter("floorId", admissionDto.getFloorId());

			} else if (admissionDto.getFloorId() != 0 && admissionDto.getWardId() != 0) {
				newQuery.setParameter("floorId", admissionDto.getFloorId());
				newQuery.setParameter("wardId", admissionDto.getWardId());
			}
			if (admissionDto.getFloorId() > 0 && admissionDto.getWardId() == 0) {

				newQuery.setParameter("floorId", admissionDto.getFloorId());

			} else if (admissionDto.getFloorId() != 0 && admissionDto.getWardId() != 0) {
				newQuery.setParameter("floorId", admissionDto.getFloorId());
				newQuery.setParameter("wardId", admissionDto.getWardId());

			}
			if (admissionDto.getAdmissionTypeId() != null ) {
				newQuery.setParameter("admissionTypeId", admissionDto.getAdmissionTypeId());
			}
			if ( admissionDto.getDoctorId() != null ) {
				newQuery.setParameter("doctorId", admissionDto.getDoctorId());
			}
			if ( admissionDto.getDoa() != null ) {
				newQuery.setParameter("doa", admissionDto.getDoa());
			}
			if ( admissionDto.getPaymentEntitlementId() != null ) {
				newQuery.setParameter("paymentEntitlementId", admissionDto.getPaymentEntitlementId());
			}
			List<AdmittedPatientListDto> admissionNoteDtosList = newQuery
					.setResultTransformer(Transformers.aliasToBean(AdmittedPatientListDto.class)).list();

			return new Response(SUCCESS, null, null, admissionNoteDtosList, admissionNoteDtosList.size());

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response serachPatientForMapOfBed(AdmissionDto admissionDto) throws ApplicationException {
		try {

			Session session = sessionFactory.getCurrentSession();
			SQLQuery finalQuery =session.createSQLQuery(patientSearch(admissionDto).toString());

			if(admissionDto.getFloorId()>0)
			{
				finalQuery.setParameter("floorId", admissionDto.getFloorId());
			}
			if(admissionDto.getWardId()>0)
			{
				finalQuery.setParameter("wardId", admissionDto.getWardId());
			}
			if(admissionDto.getNursingStationId()>0)
			{
				finalQuery.setParameter("nursingStationId", admissionDto.getNursingStationId());
			}
			if(admissionDto.getBedCategoryId()>0)
			{
				finalQuery.setParameter("bedCategoryId", admissionDto.getBedCategoryId());
			}
			if(admissionDto.getPatientId()>0)
			{
				finalQuery.setParameter("patientId", admissionDto.getPatientId());
			}
			List<AdmissionNoteDto> admissionNoteDtosList = finalQuery.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

			return new Response(SUCCESS, null, null, admissionNoteDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	private StringBuilder patientSearch(AdmissionDto admissionDto)
	{
		
		StringBuilder condition = new  StringBuilder();
		StringBuilder sb = new  StringBuilder();
		
		if(admissionDto.getFloorId()>0)
		{
			condition.append(" AND bm.floor_id=:floorId ");
		}
		if(admissionDto.getWardId()>0)
		{
			condition.append(" AND bm.ward_id=:wardId ");
		}
		if(admissionDto.getNursingStationId()>0)
		{
			condition.append(" AND nurWard.nursing_station_id=:nursingStationId ");
		}
		if(admissionDto.getBedCategoryId()>0)
		{
			condition.append(" AND adl.bed_category_id=:bedCategoryId ");
		}
		if(admissionDto.getPatientId()>0)
		{
			condition.append(" AND admission.patient_id=:patientId ");
		}
		
		
		String query1 = "SELECT "
				+ "  admission.admission_id as \"admissionId\", "
				+ "  admission.patient_id as \"patientId\", "
				+ "  admission.t_patient_id as \"tPatientId\", "
				+ "  admission.admission_number as \"admissionNumber\", "
				+ "  admission.visit_type_id as \"visitTypeId\", "
				+ "  admission.doctor_id as \"doctorId\", "
				+ "  admission.speciality_id as \"specialityId\", "
				+ "  sp.speciality_name as \"specialityName\", "
				+ "  pr.uhid_number as \"UHIDNumber\", "
				+ "  concat(coalesce(pre.prefix_code,''),' ',coalesce(pr.first_name),' ',coalesce(pr.middle_name),' ',coalesce(pr.last_name)) as \"pFirstName\", "
				+ "  pr.gender_id as \"genderId\", "
				+ "  gm.gender_code as \"genderCode\", "
				+ "  to_char(pr.dob, 'MM/dd/yyyy') as \"dob\", "
				+ "  concat(coalesce(doc.first_name),' ',coalesce(doc.middle_name),' ',coalesce(doc.last_name)) as \"dFirstName\", "
				+ "  to_char(adl.doa, 'dd/MM/yyyy') as \"doa\", "
				+ "  to_char(adl.pdd, 'dd/MM/yyyy') as \"pdd\", "
				+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
				+ "  spd.contract_id as \"contractId\", "
				+ "  spd.payee_id as \"payeeId\", "
				+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
				+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
				+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
				+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
				+ "  bm.floor_id as \"floorId\", "
				+ "  bm.bed_number as \"bedNumber\", "
				+ "  bm.bed_id as \"bedId\", "
				+ "  bm.ward_id as \"wardId\", "
				+ "  bm.bed_status_id as \"bedStatusId\", "
				+ "  ward.ward_name as \"wardName\","
				//+ "  mm.total_score as \"totalScore\", "
				+ "  adl.is_short_leave as \"isShortLeave\", "
				+ "  pr.patient_category_id as \"patientCategoryId\", "
				+ "  ( "
				+ "			with labct as (select "
				+ "	count(ord_details.order_details_id) as ct "
				+ "from "
				+ "	public.t_order_master ord_mst "
				+ "    inner join public.t_order_details ord_details on ord_details.order_id = ord_mst.order_id "
				+ " inner join service.t_unit_service_master_details service_dtls on "
				+ "	  ord_details.service_id = service_dtls.service_master_id and service_dtls.status = 'A' "
				+ "	  and service_dtls.is_panel = 'Y' "
				+ "inner join lab.t_panel_details pd on "
				+ "	ord_details.service_id = pd.service_id "
				+ "	and pd.is_deleted = 'N' "
				+ "	and pd.test_id not in (select coalesce(test_id,0) from lab.t_lab_sample_dtls lsd where lsd.profile_id =pd.panel_id and lsd.order_details_id = ord_details.order_details_id) "
				+ "left join lab.t_lab_sample_dtls smpdtls on "
				+ "	 ord_details.order_details_id = smpdtls.order_details_id "
				+ "	 and smpdtls.sample_barcode is null "
				+ "where "
				+ "	 ord_mst.org_unit_id =:unitId "
				+ "	 and ord_mst.patient_id =  admission.patient_id "
				+ "     and ord_mst.visit_type_id = admission.visit_type_id "
				+ "     and ord_mst.admission_id =  admission.admission_id "
				+ "     and ord_details.ord_cancelled = 'N' "
				+"      and ord_details.is_from_addservice = 'N' "
				+"      and ord_details.debit_note_service = false  "
				+ " union "
				+ " select "
				+ "      count(*) as ct "
				+ "    from "
				+ "      public.t_order_master ord_mst "
				+ "      inner join public.t_order_details ord_details on ord_details.order_id = ord_mst.order_id "
				+ "      inner join service.t_unit_service_master_details service_dtls on "
				+ "	  ord_details.service_id = service_dtls.service_master_id and service_dtls.status = 'A' "
				+ "      inner join public.t_lab_configuration lab_config on lab_config.unit_id  = :unitId "
				+ "    where "
				+ "      ord_mst.patient_id =  admission.patient_id "
				+ "      and ord_mst.visit_type_id = admission.visit_type_id "
				+ "      and ord_mst.admission_id =  admission.admission_id "
				+ "      and ord_details.service_rendered = 0 "
				+ "      and ord_details.ord_cancelled = 'N' "
				+"       and ord_details.is_from_addservice = 'N' "
				+"       and ord_details.debit_note_service = false  "
				+ "      and ord_details.service_rendering_deptid in (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.serology_id,lab_config.clinical_pathology_id) "
				+ "  ) select cast(sum(a.ct) as text) as \"labOrder\" from labct a "
				+ "  ),"
				+ "( select "
				+ " cast(count(*) as text ) as \"imaging\" " 
				+ " FROM public.t_order_details orderDetails " 
				+ " INNER JOIN public.t_order_master orderM on orderM.order_id=orderDetails.order_id "
				+ " INNER JOIN service.m_service_master service on service.service_master_id=orderDetails.service_id "
				+ " INNER JOIN public.t_unit_application_billing_configuration unitapplication on  unitapplication.unit_id= :unitId "
				+ " and unitapplication.radiology_speciality_id=10 "
				+ " where "
				+ " orderM.patient_id = admission.patient_id "
				+ " and orderM.visit_type_id = admission.visit_type_id "
				+ " and orderM.admission_id = admission.admission_id "
				+ " and orderDetails.service_rendered = 0 "
				+ " and orderDetails.ord_cancelled = 'N' ),"
				+ " (select  cast(count(*) as text ) as \"nbm\" "
				+ " FROM adt.t_ipd_diet_order dietOrd "
				+ " INNER JOIN public.t_unit_application_billing_configuration unitapplication on  unitapplication.unit_id= :unitId "
				+ " where "
				+ " dietOrd.patient_id = admission.patient_id "
				+ " and dietOrd.admission_id = admission.admission_id "
				+ " and dietOrd.is_nbm = 'Y' ), "
				+ "( select "
				+ "cast(count(*) as text ) as \"procedureOrder\" "
				+ " from "
				+ " otims.t_procedure_req_mst prm "
				+  " left join otims.t_procedure_order_master pom on pom.pro_req_id = prm.pro_req_id "
				+  " left join service.m_service_master sm on sm.service_master_id = pom.service_id "
				+  " left join otims.t_procedure_configuration proc_config on "
				+   " proc_config.org_id = :organizationId and proc_config.unit_id = :unitId "
				+  " where "
				+  " prm.patient_id = admission.patient_id "
				+  " and prm.visit_type_id = admission.visit_type_id "
				+  " and prm.admission_id = admission.admission_id"
				+ "  and prm.procedure_status_id=1 ),  "
				+ " ( select "
				+" cast(count(*) as text ) as \"allergy\" " 
	      		+" from "
	    		+" patient.t_allergy_patient_mapper alrgyMap  "
	      		+" LEFT JOIN 	emr.m_allergy_master alrgyMst on alrgyMst.allergy_id=alrgyMap.allergy_id  "
				+" LEFT JOIN 	public.m_generic genericMst on genericMst.id=alrgyMap.generic_id  "
				+" LEFT JOIN 	emr.m_allergy_type_master alrgyTypeMst on alrgyTypeMst.allergy_type_id=alrgyMap.allergy_type_id  "
				+" LEFT JOIN 	emr.m_severity_master servtyMst on servtyMst.severity_id=alrgyMap.severity_id  "
				+" where "
				+" alrgyMap.patient_id = admission.patient_id "
	      		+" and alrgyMap.admission_id = admission.admission_id ), "
				+ " (select mm.mews_mapper_id as \"mewsMapperId\" " 
				+ " from adt.t_mews_mapper mm "  
				+ " where mm.mews_mapper_id=(select max(ms.mews_mapper_id) from adt.t_mews_mapper ms "  
				+ " where ms.organization_id=:organizationId "  
				+ " and ms.unit_id=:unitId "  
				+ " and ms.patient_id=admission.patient_id "  
				+ " )), "  
				+ " (select mm.total_score as \"totalScore\" "  
				+ " from adt.t_mews_mapper mm "  
				+ " where mm.mews_mapper_id=(select max(ms.mews_mapper_id) from adt.t_mews_mapper ms "  
				+ " where ms.organization_id=:organizationId "  
				+ " and ms.unit_id=:unitId "  
				+ " and ms.patient_id=admission.patient_id "  
				+ " )) "
				
				+ "FROM adt.m_bed_master bm "
				+ "LEFT join adt.t_admission_details adl on bm.bed_id = adl.bed_id and adl.status = 'A' and adl.nursing_acceptance='Y' "
				+ "left join adt.t_admission admission on adl.admission_id = admission.admission_id "
						+ "	and admission.status = 'A' "
						+ "	and admission.organization_id = :organizationId "
						+ "	and admission.unit_id = :unitId "
				+ "  LEFT JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
				+ "  LEFT JOIN m_prefix_master pre on pre.prefix_id = pr.prefix_id "
				+ "  LEFT JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
				+ "  LEFT JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
				+ "  LEFT JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
				+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
				+ "  LEFT JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 "
				+ "  INNER JOIN adt.m_bed_category_master bedCatMst on bm.bed_category_id = bedCatMst.bed_category_id "
				+ "  LEFT JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
				+ " WHERE "
				+ "  bm.status='A' "
				+ "  AND bm.organization_id = :organizationId "
				+ "  AND bm.unit_id = :unitId ";
				
		
		
		
		sb.append(query1).append(condition);
		/*
		 * StringBuilder query= new StringBuilder( "select"
		 * +"  	admission.admission_id as \"admissionId\","
		 * +"  	admission.patient_id as \"patientId\","
		 * +"  	admission.t_patient_id as \"tPatientId\","
		 * +"  	admission.admission_number as \"admissionNumber\","
		 * +"  	admission.visit_type_id as \"visitTypeId\","
		 * +"  	admission.doctor_id as \"doctorId\","
		 * +"  	admission.speciality_id as \"specialityId\","
		 * +"  	sp.speciality_name as \"specialityName\","
		 * +"  	pr.uhid_number as \"UHIDNumber\","
		 * +"  	concat( pr.first_name, ' ', pr.middle_name, ' ', pr.last_name ) as \"pFirstName\" ,"
		 * +"  	pr.gender_id as \"genderId\"," +"  	gm.gender_code as \"genderCode\","
		 * +"  	to_char( pr.dob," +"  	'MM/dd/yyyy' ) as \"dob\","
		 * +"  	concat( doc.first_name, ' ', doc.middle_name, ' ', doc.last_name ) as \"dFirstName\","
		 * +"  	to_char( adl.doa," +"  	'dd/MM/yyyy' ) as \"doa\","
		 * +"  	to_char( adl.pdd," +"  	'dd/MM/yyyy' ) as \"pdd\","
		 * +"  	patsp.payment_entitlement_id as \"paymentEntitlementId\","
		 * +"  	pe.payment_entitlement_desc as \"paymentEntitlementDesc\","
		 * +"  	bedCatMst.bed_category_desc as \"bedCategoryDesc\","
		 * +"  	bm.floor_id as \"floorId\"," +"  	bm.bed_number as \"bedNumber\""
		 * +"  from" +"  	adt.t_admission admission"
		 * +"  inner join patient.t_patient_registration pr on"
		 * +"  	admission.patient_id = pr.patient_id"
		 * +"  inner join public.m_gender_master gm on"
		 * +"  	pr.gender_id = gm.gender_id"
		 * +"  inner join doctor.m_doctor_master doc on"
		 * +"  	admission.doctor_id = doc.doctor_id"
		 * +"  inner join doctor.m_speciality_master sp on"
		 * +"  	admission.speciality_id = sp.speciality_id"
		 * +"  inner join adt.t_admission_details adl on"
		 * +"  	adl.admission_id = admission.admission_id"
		 * +"  inner join adt.m_bed_master bm on" +"  	adl.bed_id = bm.bed_id"
		 * +"  inner join opd.t_patient_sponsor_details patsp on "
		 * +"  	patsp.admission_id= admission.admission_id and patsp.priority_id=1"
		 * +"  inner join adt.m_bed_category_master bedCatMst on"
		 * +"  	adl.bed_category_id = bedCatMst.bed_category_id"
		 * +"  inner join public.m_payment_entitlement_master pe on"
		 * +"  	patsp.payment_entitlement_id = pe.payment_entitlement_id"
		 * +"  inner join adt.m_ward_master ward on" +"  	bm.ward_id = ward.ward_id"
		 * +"  left join adt.t_nursing_station_ward_mapper nurWard on"
		 * +"  	ward.ward_id = nurWard.ward_id"
		 * +"  left join adt.m_nursing_station_master nur on"
		 * +"  	nurWard.nursing_station_id = nur.nursing_station_id" +"  where"
		 * +"  	admission.status = 'A'" +
		 * "AND admission.organization_id=:organizationId " +
		 * "AND admission.unit_id=:unitId " + condition );
		 * 
		 * 
		 * if(admissionDto.gettPatientId()>0) {
		 * condition.append(" AND admission.t_patient_id="+admissionDto.gettPatientId()
		 * +" ");
		 * 
		 * query.append( "UNION " + " select"
		 * +"  	admission.admission_id as \"admissionId\","
		 * +"  	admission.patient_id as \"patientId\","
		 * +"  	admission.t_patient_id as \"tPatientId\","
		 * +"  	admission.admission_number as \"admissionNumber\","
		 * +"  	admission.visit_type_id as \"visitTypeId\","
		 * +"  	admission.doctor_id as \"doctorId\","
		 * +"  	admission.speciality_id as \"specialityId\","
		 * +"  	sp.speciality_name as \"specialityName\","
		 * +"  	pr.uhid_number as \"UHIDNumber\","
		 * +"  	concat( pr.first_name, ' ', pr.middle_name, ' ', pr.last_name ) as \"pFirstName\" ,"
		 * +"  	pr.gender_id as \"genderId\"," +"  	gm.gender_code as \"genderCode\","
		 * +"  	to_char( pr.dob," +"  	'MM/dd/yyyy' ) as \"dob\","
		 * +"  	concat( doc.first_name, ' ', doc.middle_name, ' ', doc.last_name ) as \"dFirstName\","
		 * +"  	to_char( adl.doa," +"  	'dd/MM/yyyy' ) as \"doa\","
		 * +"  	to_char( adl.pdd," +"  	'dd/MM/yyyy' ) as \"pdd\","
		 * +"  	patsp.payment_entitlement_id as \"paymentEntitlementId\","
		 * +"  	pe.payment_entitlement_desc as \"paymentEntitlementDesc\","
		 * +"  	bedCatMst.bed_category_desc as \"bedCategoryDesc\","
		 * +"  	bm.floor_id as \"floorId\"," +"  	bm.bed_number as \"bedNumber\""
		 * +"  from" +"  	adt.t_admission admission"
		 * +"  inner join patient.m_unknown_patient_registration pr on admission.t_patient_id=pr.unknown_patient_id"
		 * +"  inner join public.m_gender_master gm on"
		 * +"  	pr.gender_id = gm.gender_id"
		 * +"  inner join doctor.m_doctor_master doc on"
		 * +"  	admission.doctor_id = doc.doctor_id"
		 * +"  inner join doctor.m_speciality_master sp on"
		 * +"  	admission.speciality_id = sp.speciality_id"
		 * +"  inner join adt.t_admission_details adl on"
		 * +"  	adl.admission_id = admission.admission_id"
		 * +"  inner join adt.m_bed_master bm on" +"  	adl.bed_id = bm.bed_id"
		 * +"  inner join opd.t_patient_sponsor_details patsp on "
		 * +"  	patsp.admission_id= admission.admission_id and patsp.priority_id=1"
		 * +"  inner join adt.m_bed_category_master bedCatMst on"
		 * +"  	adl.bed_category_id = bedCatMst.bed_category_id"
		 * +"  inner join public.m_payment_entitlement_master pe on"
		 * +"  	patsp.payment_entitlement_id = pe.payment_entitlement_id"
		 * +"  inner join adt.m_ward_master ward on" +"  	bm.ward_id = ward.ward_id"
		 * +"  left join adt.t_nursing_station_ward_mapper nurWard on"
		 * +"  	ward.ward_id = nurWard.ward_id"
		 * +"  left join adt.m_nursing_station_master nur on"
		 * +"  	nurWard.nursing_station_id = nur.nursing_station_id" +"  where"
		 * +"  	admission.status = 'A'" +
		 * " AND admission.organization_id=:organizationId " +
		 * " AND admission.unit_id=:unitId" + condition);
		 * 
		 * }
		 */
		
		return sb;
	}

	@Override
	public Response getIpPatientListByDocId(AdmissionDto admissionDto) throws ApplicationException {
		try {
		
			Integer count=0;
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_DOCTOR_WISE").getQueryString();

	        String finalQuery;
	        String finalQuery1;
			List<AdmissionDto> admissionDtosList;
			List<AdmissionDto> admissionDtosList1;
			if (admissionDto.getIsRMO()==null) {
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("docId", admissionDto.getDoctorId())
					.setParameter("visitTypeId", admissionDto.getVisitTypeId())
					.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset()  : 0)
					.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
				admissionDtosList1 = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("docId", admissionDto.getDoctorId())
					.setParameter("visitTypeId", admissionDto.getVisitTypeId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
			}else{
			finalQuery = query.replace("admission.doctor_id=:docId ", "adl.ward_id in (:wardList) ");
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameterList("wardList", admissionDto.getRmoMappedWardList())
						.setParameter("visitTypeId", admissionDto.getVisitTypeId())
						.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset()  : 0)
						.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage()  : 10)
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
				finalQuery1 = query.replace("admission.doctor_id=:docId ", "adl.ward_id in (:wardList) ");
				admissionDtosList1 = sessionFactory.getCurrentSession().createSQLQuery(finalQuery1.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameterList("wardList", admissionDto.getRmoMappedWardList())
						.setParameter("visitTypeId", admissionDto.getVisitTypeId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}

			
			 if(admissionDtosList1 != null && admissionDtosList1.size()>0) {
		    	  count = admissionDtosList1.size();
		      }
			return new Response(SUCCESS, null, null, admissionDtosList, count);
			
		

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmittedPatientListForEMR(AdmissionDto admissionDto) throws ApplicationException {
		try {
			List<AdmissionDto> admissionDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_DOCTOR_WISE_FOR_EMR")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("docId", admissionDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmittedPatientListForIPDNursingWorklist(AdmissionDto admissionDto) throws ApplicationException {
		try {
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_FOR_IPD_NURSING_WORKLIST").getQueryString();

			String finalQuery;
			List<AdmissionDto> admissionDtosList;
			if (admissionDto.getIsShowPatientByBed().equals('N')) {
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameterList("wardList", admissionDto.getWardList())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			} else {
				finalQuery = query.replace("bm.ward_id IN (:wardList)", "bm.bed_id in (:bedList)");
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameterList("bedList", admissionDto.getBedList())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}

			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfERPatients(AdmissionDto admissionDto) throws ApplicationException {
		try {
			  List<AdmissionDto> admissionDtosList1 = sessionFactory.getCurrentSession()
					  .getNamedQuery("GET_LIST_OF_ER_PATIENTS")
					  .setParameter("organizationId", admissionDto.getOrganizationId())
					  .setParameter("unitId", admissionDto.getUnitId())
					  .setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

			  List<AdmissionDto> admissionDtosList = new ArrayList<>(
					admissionDtosList1.stream()
					.collect(Collectors.toMap(
							AdmissionDto::getPatientId,   // key
							Function.identity(),         // value
							(existing, duplicate) -> existing // keep first duplicate
					))
					.values()
			  );



			if(admissionDtosList != null && !admissionDtosList.isEmpty()){
				List<Integer> patientIds = admissionDtosList.stream().map(x -> x.getPatientId()).collect(Collectors.toList());
				if(patientIds != null && !patientIds.isEmpty()){
					com.param.global.common.Response interventionRes = iBedMasterDao.getInterventionDetails(patientIds, admissionDto.getUnitId(), admissionDto.getOrganizationId());
					if(interventionRes.getStatus().equals(SUCCESS) && interventionRes.getListObject() != null && !interventionRes.getListObject().isEmpty()){
						List<BedMasterDto> interventionList = interventionRes.getListObject();

						Map<Integer, BedMasterDto> interventionMap =
								interventionList.stream()
										.collect(Collectors.toMap(
												BedMasterDto::getPatientId,
												Function.identity(),
												(existing, duplicate) -> duplicate
										));

						admissionDtosList.forEach(bedRecord -> {
							BedMasterDto intervention =
									interventionMap.get(bedRecord.getPatientId());

							if (intervention != null) {
								if(intervention.getInterventionProcessedDoctorId() != null && intervention.getInterventionProcessedDoctorId() > 0){
									bedRecord.setInterventionCompleted(1);
								}
								bedRecord.setInterventionRequired(1);
							}
						});
					}
				}
			}
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {
			System.out.println(he);
		} catch (Exception e) {
			System.out.println(e);
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfDayCarePatientsForNursing(AdmissionDto admissionDto) throws ApplicationException {
		try {
			List<AdmissionDto> admissionDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_DAY_CARE_PATIENTS_FOR_NURSING")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameterList("wardList", admissionDto.getWardList())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getListOfDayCareNursingAccepted(AdmissionDto admissionDto) throws ApplicationException {
		try {
			List<AdmissionDto> admissionDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_LIST_OF_DAY_CARE_NURSING_ACCEPTED")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameterList("wardList", admissionDto.getWardList())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response ipPatientListByDocIdForCancelDischarge(AdmissionDto admissionDto) throws ApplicationException {
		try {
			
			String query  ="SELECT "
					+ "  admission.admission_id as \"admissionId\", "
					+ " (select coalesce(bm.bill_status_id,0) from billing.m_billing_master bm where bm.is_interim_or_final_bill = 2 and  bm.admission_id =admission.admission_id and bm.bill_cancelled = 'N' order by bm.billing_master_id limit 1  ) as \"billStatusId\" ,"
					+ "  admission.patient_id as \"patientId\", "
					+ "  admission.t_patient_id as \"tPatientId\", "
					+ "  admission.admission_number as \"admissionNumber\", "
					+ "  admission.visit_type_id as \"visitTypeId\", "
					+ "  admission.doctor_id as \"doctorId\", "
					+ "  admission.speciality_id as \"specialityId\", "
					+ "  sp.speciality_name as \"specialityName\", "
					+ "	 pr.prefix_id as \"prefixId\", "
					+ "  pr.uhid_number as \"UHIDNumber\", "
					+ "  concat(COALESCE(pr.first_name,''),' ',COALESCE(pr.middle_name,''),' ',COALESCE(pr.last_name,'')) as \"pFirstName\", "
					+ "  pr.gender_id as \"genderId\", "
					+ "  pr.image_path as \"imagePath\","
					+ "  pm.prefix_code as \"prefixCode\", "
					+"   concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ' , EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)),'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'D') as \"ageStr\", "
					+ "  pr.mpshah_patient_id as \"mpshahPatientId\", "
					+ "  gm.gender_code as \"genderCode\", "
					+ "  to_char(pr.dob, 'MM/dd/yyyy') as \"dob\", "
					+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
					+ "  to_char(adl.doa, 'dd/MM/yyyy') as \"doa\", "
					+ "  to_char(adl.pdd, 'dd/MM/yyyy') as \"pdd\", "
					+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
					+ "  spd.contract_id as \"contractId\", "
					+ "  spd.payee_id as \"payeeId\", "
					+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
					+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
					+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
					+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
					+ "  bm.floor_id as \"floorId\", "
					+ "  bm.bed_number as \"bedNumber\", "
					+ "  bm.bed_id as \"bedId\", "
					+ "  bm.ward_id as \"wardId\", "
					+ "  ward.ward_name as \"wardName\", "
					+ "  adl.is_short_leave as \"isShortLeave\", "
					+ "  pr.patient_category_id as \"patientCategoryId\", "
					+ "  admission.concent as \"concent\", "
					+ "  disc.discharge_id as \"dischargeId\", "
					+ "  disc.discharge_status_id as \"dischargeStatusId\", "
					+ "  to_char(admission.created_date, 'dd/mm/yyyy') as \"createdDate\", "
					+ "  to_char(admission.created_date, 'HH:mm') as \"createdTime\", "
					+ " medleg.medico_legal_id as \"medicoLegalId\", " 
				/*	+ "  nurWard.nursing_station_id as \"nursingStationId\", "
					+ "  nur.nursing_station_desc as \"nursingStationDesc\", "*/
					+ "  ( "
					+ "			with labct as (select "
					+ "	count(ord_details.order_details_id) as ct "
					+ " from "
					+ "	public.t_order_master ord_mst "
					+ "    inner join public.t_order_details ord_details on ord_details.order_id = ord_mst.order_id "
					+ " inner join service.t_unit_service_master_details service_dtls on "
					+ "	  ord_details.service_id = service_dtls.service_master_id and service_dtls.status = 'A' "
					+ "	  and service_dtls.is_panel = 'Y' "
					+ " inner join lab.t_panel_details pd on "
					+ "	ord_details.service_id = pd.service_id "
					+ "	and pd.is_deleted = 'N' "
					+ "	and pd.test_id not in (select coalesce(test_id,0) from lab.t_lab_sample_dtls lsd where lsd.profile_id =pd.panel_id and lsd.order_details_id = ord_details.order_details_id) "
					+ " left join lab.t_lab_sample_dtls smpdtls on "
					+ "	 ord_details.order_details_id = smpdtls.order_details_id "
					+ "	 and smpdtls.sample_barcode is null "
					+ " where "
					+ "	 ord_mst.org_unit_id =:unitId "
					+ "	 and ord_mst.patient_id =  admission.patient_id "
					+ "     and ord_mst.visit_type_id = admission.visit_type_id "
					+ "     and ord_mst.admission_id =  admission.admission_id "
					+ "     and ord_details.ord_cancelled = 'N' "
					+"      and ord_details.is_from_addservice = 'N' "
					+"      and ord_details.debit_note_service = false  "
					+ " union "
					+ " select "
					+ "      count(*) as ct "
					+ "    from "
					+ "      public.t_order_master ord_mst "
					+ "      inner join public.t_order_details ord_details on ord_details.order_id = ord_mst.order_id "
					+ "      inner join service.t_unit_service_master_details service_dtls on "
					+ "	  ord_details.service_id = service_dtls.service_master_id and service_dtls.status = 'A' "
					+ "      inner join public.t_lab_configuration lab_config on lab_config.unit_id  = :unitId "
					+ "    where "
					+ "      ord_mst.patient_id =  admission.patient_id "
					+ "      and ord_mst.visit_type_id = admission.visit_type_id "
					+ "      and ord_mst.admission_id =  admission.admission_id "
					+ "      and ord_details.service_rendered = 0 "
					+ "      and ord_details.ord_cancelled = 'N' "
					+"       and ord_details.is_from_addservice = 'N' "
					+"       and ord_details.debit_note_service = false  "
					+ "      and ord_details.service_rendering_deptid in (lab_config.biochemistry_id,lab_config.hematology_id,lab_config.microbiology_id,lab_config.serology_id,lab_config.clinical_pathology_id) "
					+ "  ) select cast(sum(a.ct) as text) as \"labOrder\" from labct a"
					+ "  ) "
					+ " FROM "
					+ "  adt.t_admission admission "
					+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id "
					+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
					+ "  INNER JOIN public.m_prefix_master pm on pm.prefix_id = pr.prefix_id "
					+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
					+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
					+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
					+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
					+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
					+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A'  "
					+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id and disc.status = 'A' "
					+ "   left join adt.t_medico_legal medleg on admission.admission_id = medleg.admission_id "

					/*	+ "  LEFT JOIN adt.t_nursing_station_ward_mapper nurWard on ward.ward_id = nurWard.ward_id "
					+ "  LEFT JOIN adt.m_nursing_station_master nur on nurWard.nursing_station_id = nur.nursing_station_id "*/
					//+ "  INNER JOIN adt.t_admission_note anl on admission.admission_id = anl.admission_id "
					//+ "  INNER JOIN adt.t_admission_patient_mapping apml on anl.admission_note_id = apml.admission_note_id "
					+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
					+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
					+ " WHERE "
					+ "  admission.status = 'A' "
					+ " AND adl.status='A' "
					+ " AND adl.nursing_acceptance = 'Y' "
					//+ "  AND apml.status = 'A' "
					//+ "  AND anl.status = 'A' "
					+ "  AND admission.organization_id = :organizationId "
					+ "  AND admission.unit_id = :unitId "
					//+ "  AND admission.doctor_id=:docId "
					 + " AND disc.discharge_status_id not in (5,6) " ;
			
			
			if(admissionDto.getAdmissionId()!=null) {
				query=query.concat(" AND disc.admission_id=:admissionId ");
			}
			List<AdmissionDto> admissionDtosList=null;
			if(admissionDto.getIsRMO()!=null && admissionDto.getIsRMO().equals('Y')) {
				query=query.concat(" AND adl.ward_id in (:wardList) order by \"admissionId\" desc ");
				 Query sql = sessionFactory.getCurrentSession()
							.createSQLQuery(query.toString())
							.setParameter("organizationId", admissionDto.getOrganizationId())
							.setParameter("unitId", admissionDto.getUnitId())
							.setParameterList("wardList", admissionDto.getRmoMappedWardList());
				if(admissionDto.getAdmissionId()!=null) {
					sql.setParameter("admissionId", admissionDto.getAdmissionId());
				}
				admissionDtosList = sql.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}else {
				 query=query.concat(" AND admission.doctor_id=:docId order by \"admissionId\" desc ");
				Query sql2 = sessionFactory.getCurrentSession()
							.createSQLQuery(query.toString())
							.setParameter("organizationId", admissionDto.getOrganizationId())
							.setParameter("unitId", admissionDto.getUnitId())
							.setParameter("docId", admissionDto.getDoctorId());
				if(admissionDto.getAdmissionId()!=null) {
					sql2.setParameter("admissionId", admissionDto.getAdmissionId());
				}
				admissionDtosList =	sql2.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				
			}
			
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getHemodialysisByDoctorList(AdmissionDto admissionDto) throws ApplicationException {
		try {
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_NEPHROLOGY_DOCTOR_WISE").getQueryString();
			
			List<AdmissionDto> admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("docId", admissionDto.getDoctorId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

			return new Response(SUCCESS, null, null, admissionDtosList, null);
			
		

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmittedPatientListForIPDNursingWorklistForNursingPage(AdmissionDto admissionDto)
			throws ApplicationException {
		try {
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_FOR_IPD_NURSING_WORKLIST_FOR_NURSING_PAGE").getQueryString();

			String finalQuery;
			List<AdmissionDto> admissionDtosList;
			if (admissionDto.getIsShowPatientByBed()!=null && admissionDto.getIsShowPatientByBed().equals('N')) {
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameter("visitTypeId", admissionDto.getVisitTypeId())
						.setParameterList("wardList", admissionDto.getWardList())
						.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset() : 0)
						.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			} else {
				finalQuery = query.replace("bm.ward_id IN (:wardList)", "bm.bed_id in (:bedList)");
				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameter("visitTypeId", admissionDto.getVisitTypeId())
						.setParameterList("bedList", admissionDto.getBedList())
						.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset() : 0)
						.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage() : 10)
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}

			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmittedPatientListForIPDNursingWorklistForNursingPageCount(AdmissionDto admissionDto)
			throws ApplicationException {
		try {
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_FOR_IPD_NURSING_WORKLIST_FOR_NURSING_PAGE").getQueryString();

			String finalQuery;
			List<AdmissionDto> admissionDtosList=null;
			if(admissionDto.getWardList().size()>0)
			{
				if (admissionDto.getIsShowPatientByBed()!=null && admissionDto.getIsShowPatientByBed().equals('N')) {
					admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
							.setParameter("organizationId", admissionDto.getOrganizationId())
							.setParameter("unitId", admissionDto.getUnitId())
	                        .setParameter("visitTypeId", admissionDto.getVisitTypeId())
							.setParameterList("wardList", admissionDto.getWardList())
							.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
				} else {
					if(admissionDto.getBedList().size()>0)
					{
						finalQuery = query.replace("bm.ward_id IN (:wardList)", "bm.bed_id in (:bedList)");
						admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
								.setParameter("organizationId", admissionDto.getOrganizationId())
								.setParameter("unitId", admissionDto.getUnitId())
		                        .setParameter("visitTypeId", admissionDto.getVisitTypeId())
								.setParameterList("bedList", admissionDto.getBedList())
								.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
					}
					
				}
				Integer count =0;
				if(admissionDtosList!=null && admissionDtosList.size()>0) {
					count=admissionDtosList.size();
				}
				return new Response(SUCCESS, null, null, null, count);
			}

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getLoginDoctorSpecialityId(AdmissionDto admissionDto) throws ApplicationException {
		try {
			AdmissionDto admissionDtosList = (AdmissionDto) sessionFactory.getCurrentSession().createSQLQuery("select  "
					+ "dsm.speciality_id as\"specialityId\" "
					+ "from doctor.t_doctor_speciality_mapper dsm "
					+ " where dsm.doctor_id =:doctId "
					+ " AND dsm.unit_id =:unitId "
					+ " AND status='A' ")
					.setParameter("doctId", admissionDto.getDoctorId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).uniqueResult();

			return new Response(SUCCESS, null, null, null, admissionDtosList);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		}
		finally {
			;
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmittedPatientListPagination(AdmissionDto admissionDto) throws ApplicationException {
		try {
			List<AdmittedPatientListDto> admissionDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_FOR_PAGINATION")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset() : 0)
					.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AdmittedPatientListDto.class)).list();
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {
			System.out.println(he);
		} catch (Exception e) {
			System.out.println(e);
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmittedPatientListPaginationCount(AdmissionDto admissionDto) throws ApplicationException {
		try {
			BigInteger  count= (BigInteger)sessionFactory.getCurrentSession()
					.createSQLQuery(" SELECT   count(*) "
							+ " from "
							+ "	adt.t_admission admission "
							+ " inner join patient.t_patient_registration pr on  admission.patient_id = pr.patient_id "
							+ " inner join doctor.m_doctor_master doc 		 on admission.doctor_id = doc.doctor_id "
							+ " inner join doctor.m_speciality_master sp 	 on  admission.speciality_id = sp.speciality_id "
							+ " inner join adt.t_admission_details adl 		 on adl.admission_id = admission.admission_id "
							+ " inner join adt.m_bed_master bm 				 on adl.bed_id = bm.bed_id "
							+ " inner join adt.m_ward_master ward 			 on bm.ward_id = ward.ward_id "
							+ " inner join opd.t_patient_sponsor_details spd on spd.admission_id = admission.admission_id and spd.priority_id = 1 and spd.status = 'A'"
							+"  WHERE " 
							+"  admission.status = 'A' "  
							+"  AND adl.status='A' " 
							+"  AND adl.nursing_acceptance = 'Y' "  
							+"  AND admission.organization_id = :organizationId " 
							+"  AND admission.unit_id = :unitId ")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.uniqueResult();
			 
			return new Response(SUCCESS, null, null, null, count);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response admittedPatientListUniversalSearch(AdmissionDto admissionDto) throws ApplicationException {
		try {
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_ADMITTED_PATIENT_DETAILS_LIST_SEARCH").getQueryString();

            if(admissionDto.getVisitTypeId()!=null) {
                query=query.concat(" AND admission.visit_type_id=:visitTypeId ");
            }
            query=query.concat(" order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
			
			String finalQuery;
			List<AdmissionDto> admissionDtosList;
			if (admissionDto.getIsShowPatientByBed()!=null && admissionDto.getIsShowPatientByBed().equals('N')) {
				Query sql = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameterList("wardList", admissionDto.getWardList())
						.setParameter("patientName", "%"+admissionDto.getPatientName().trim().toLowerCase()+"%")
						.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset() : 0)
						.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage() : 10);
				if(admissionDto.getVisitTypeId()!=null) {
					sql.setParameter("visitTypeId", admissionDto.getVisitTypeId());
				}
				admissionDtosList =	sql.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			} else {
				finalQuery = query.replace("bm.ward_id IN (:wardList)", "bm.bed_id in (:bedList)");
				Query sql2 = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameterList("bedList", admissionDto.getBedList())
						.setParameter("patientName", "%"+admissionDto.getPatientName().trim().toLowerCase()+"%")
						.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset() : 0)
						.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage() : 10);
				if(admissionDto.getVisitTypeId()!=null) {
					sql2.setParameter("visitTypeId", admissionDto.getVisitTypeId());
				}
				admissionDtosList= sql2.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response admittedPatientListUniversalSearchForDietOrder(AdmissionDto admissionDto) throws ApplicationException {
		try {
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_PAGINATED_ADMITTED_PATIENT_DETAILS_LIST_SEARCH_FOR_DIET_ORDER").getQueryString();

            if(admissionDto.getVisitTypeId()!=null) {
                query=query.concat(" AND admission.visit_type_id=visitTypeId ");
            }
            query=query.concat(" order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
			
			String finalQuery;
			List<AdmissionDto> admissionDtosList;
			if (admissionDto.getIsShowPatientByBed()!=null && admissionDto.getIsShowPatientByBed().equals('N')) {
				Query sql = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameterList("wardList", admissionDto.getWardList())
						.setParameter("patientName", "%"+admissionDto.getPatientName().trim().toLowerCase()+"%")
						.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset() : 0)
						.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage() : 10);
				if(admissionDto.getVisitTypeId()!=null) {
					sql.setParameter("visitTypeId", admissionDto.getVisitTypeId());
				}
				admissionDtosList= sql.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			} else {
				finalQuery = query.replace("bm.ward_id IN (:wardList)", "bm.bed_id in (:bedList)");
				Query sql2 = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameterList("bedList", admissionDto.getBedList())
						.setParameter("patientName", "%"+admissionDto.getPatientName().trim().toLowerCase()+"%")
						.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset() : 0)
						.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage() : 10);
				if(admissionDto.getVisitTypeId()!=null) {
					sql2.setParameter("visitTypeId", admissionDto.getVisitTypeId());
				}
				admissionDtosList=	sql2.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}
			return new Response(SUCCESS, null, null, admissionDtosList, null);

		} catch (HibernateException he) {
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response ipPatientListByDocIdBySearch(AdmissionDto admissionDto) throws ApplicationException {
		try {

			Integer count=0;
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_DOCTOR_WISE_SERACH").getQueryString());

			String finalQuery;
			String finalQuery1;
			List<AdmissionDto> admissionDtosList;
			List<AdmissionDto> admissionDtosList1;
			if (admissionDto.getIsRMO()==null) {

				if (admissionDto.getSearchKeyword() != null) {
					query.append(" and (LOWER(pr.first_name) LIKE :searchKeyword  "
							+" OR LOWER(pr.last_name) LIKE :searchKeyword "
							+" OR LOWER(pr.uhid_number) LIKE :searchKeyword "
							+" OR LOWER(pr.mobile_number) LIKE :searchKeyword )");
				}


			Query SQL=	sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameter("docId", admissionDto.getDoctorId())
						.setParameter("visitTypeId", admissionDto.getVisitTypeId())
						.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset()  : 0)
						.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage()  : 10);

				if (admissionDto.getSearchKeyword() != null) {
					SQL.setParameter("searchKeyword", admissionDto.getSearchKeyword().toLowerCase());
				}

				admissionDtosList = SQL.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

				Query SQL1= sessionFactory.getCurrentSession().createSQLQuery(query.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameter("docId", admissionDto.getDoctorId())
						.setParameter("visitTypeId", admissionDto.getVisitTypeId());
				if (admissionDto.getSearchKeyword() != null) {
					SQL1.setParameter("searchKeyword", admissionDto.getSearchKeyword().toLowerCase());
				}
				admissionDtosList1 = SQL1.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

			}else{
				finalQuery = String.valueOf(new StringBuilder(query.toString()
						.replace("admission.doctor_id=:docId", "adl.ward_id in (:wardList)")));

				admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameterList("wardList", admissionDto.getRmoMappedWardList())
						.setParameter("visitTypeId", admissionDto.getVisitTypeId())
						.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset()  : 0)
						.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage()  : 10)
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

				finalQuery1 = String.valueOf(new StringBuilder(query.toString()
						.replace("admission.doctor_id=:docId", "adl.ward_id in (:wardList)")));

				admissionDtosList1 = sessionFactory.getCurrentSession().createSQLQuery(finalQuery1.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameterList("wardList", admissionDto.getRmoMappedWardList())
						.setParameter("visitTypeId", admissionDto.getVisitTypeId())
						.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			}


			if(admissionDtosList1 != null && admissionDtosList1.size()>0) {
				count = admissionDtosList1.size();
			}
			return new Response(SUCCESS, null, null, admissionDtosList, count);
		} catch (HibernateException he) {

		} catch (Exception e) {
e.printStackTrace();
		}
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getAdmittedPatientListForVitalUploader(AdmissionDto admissionDto) throws ApplicationException {
try {
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_FOR_VITAL_UPLOADER").getQueryString();

			  String finalQuery;
				List<AdmissionDto> admissionDtosList;
				if (admissionDto.getDoctorId()!=null) {
					finalQuery = query.concat(	" and (admission.doctor_id =:docId "  
							+" OR (((case when (select specMa.speciality_id from doctor.t_doctor_speciality_mapper specMa where specMa.doctor_id=:docId " 
							+" and  specMa.speciality_id=( " 
							+" select app.er_speciality_id from t_unit_application_billing_configuration app "
							+ " where app.organization_id=:organizationId and app.unit_id=:unitId and status='A' ))>0 "  
							+" then true else false end) and admission.visit_type_id=4))) "
							+ " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
					admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameter("docId", admissionDto.getDoctorId())
						.setResultTransformer(Transformers.aliasToBean(VitalUploaderAdmissionDto.class)).list();
				}else{
					 
				   finalQuery = query.concat(" and adl.ward_id in (:wardList) "
						+ " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
				//finalQuery = finalQuery.concat("AND admission.visit_type_id=2");
					admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(finalQuery.toString())
							.setParameter("organizationId", admissionDto.getOrganizationId())
							.setParameter("unitId", admissionDto.getUnitId())
							.setParameterList("wardList", admissionDto.getWardList())
							.setResultTransformer(Transformers.aliasToBean(VitalUploaderAdmissionDto.class)).list();
				}

				return new Response(SUCCESS, null, null, admissionDtosList, null);
				

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIpPatientListForDoctorApp(AdmissionDto admissionDto) throws ApplicationException {
		try {
			List<AdmissionDto> admissionDtosList=new ArrayList<AdmissionDto>();
			StringBuilder query = new StringBuilder(sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_DOCTOR_WISE_FOR_DOCTOR_APP").getQueryString());

			if (admissionDto.getSearchKeyword() != null) {
				query.append(" and (lower(pr.first_name) like :searchKeyword "
						+ "or lower(pr.last_name) like :searchKeyword ");
			}
			query.append(" order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
			Query sql = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("docId", admissionDto.getDoctorId())
					.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset() : 0)
					.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage() : 10);
			if (admissionDto.getSearchKeyword() != null) {
				sql.setParameter("searchKeyword", admissionDto.getSearchKeyword().toLowerCase());
			}
			admissionDtosList=	sql.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
		    
		    Query sql2= sessionFactory.getCurrentSession().createSQLQuery(query.toString())
					.setParameter("organizationId", "%"+admissionDto.getOrganizationId()+"%")
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("docId", admissionDto.getDoctorId());
			if (admissionDto.getSearchKeyword() != null) {
				sql2.setParameter("searchKeyword", admissionDto.getSearchKeyword().toLowerCase());
			}
			List<AdmissionDto> admissionDtosListCount =sql2.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			return new Response(SUCCESS, null, null, admissionDtosList, admissionDtosListCount.size());

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	
	}

	@Override
	public Response getAdmittedPatientListForNewBorn(AdmissionDto admissionDto) throws ApplicationException {
		try {
			Integer count=0;
			List<AdmissionDto> admissionDtosList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_NEW_BORN")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset()  : 0)
					.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage()  : 10)
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			
			List<AdmissionDto> admissionDtosCount = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ADMITTED_PATIENT_LIST_NEW_BORN")
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
			
			if(admissionDtosCount != null && admissionDtosCount.size()>0) {
				count= admissionDtosCount.size();
				}
			
			return new Response(SUCCESS, null, null, admissionDtosList, count);
			
		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response editAdmissionDetailsGetById(AdmissionDto admissionDto) throws ApplicationException {
		try {

			Query queryString = sessionFactory.getCurrentSession().getNamedQuery("GET_ADMITTED_DETAILS_PATIENT_GET_BY_ID");
			String arr[] = queryString.getQueryString().split(" order by ");
			StringBuilder stringQuery = new StringBuilder(arr[0]);
			stringQuery.append(" order by \"admissionId\" desc");
			Query newQuery = sessionFactory.getCurrentSession().createSQLQuery(stringQuery.toString());
			List<AdmittedPatientListDto> admissionNoteDtosList = 
					newQuery.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("admissionId", admissionDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(AdmittedPatientListDto.class)).list();

			return new Response(SUCCESS, null, null, admissionNoteDtosList, null);

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response admissionCancellation(AdmissionDto admissionDto) throws ApplicationException {
		try {

			//check  bill is generated
			List<AdmittedPatientListDto> billList = sessionFactory.getCurrentSession()
					.createSQLQuery("select admission_id as \"admissionId\" from billing.m_billing_master "
					              + " where admission_id =:admId and status='A' ")
					.setParameter("admId", admissionDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(AdmittedPatientListDto.class)).list();
			
			//check order is active
			List<AdmittedPatientListDto> orderList = sessionFactory.getCurrentSession()
					.createSQLQuery("select tom.admission_id as \"admissionId\" from public.t_order_master tom"
							+ " inner join public.t_order_details tod on tod.order_id=tom.order_id "
							      + " where tom.admission_id =:admId and tod.status='A' ")
					.setParameter("admId", admissionDto.getAdmissionId())
					.setResultTransformer(Transformers.aliasToBean(AdmittedPatientListDto.class)).list();
			
			//check for pharmacy medication order
//			List<MedicationDTO> prescriptionList = sessionFactory.getCurrentSession()
//					.createSQLQuery("select " +
//					        " pres.id as \"prescriptionId\", " +
//					        " issue.status_id as \"statusIdIssue\", " +
//					        " presDtl.id as \"prescriptionDetailsid\" " +
//					        " from " +
//					        " pharmacy.t_prescription pres " +
//					        " left join pharmacy.t_prescription_detail presDtl on " +
//					        " presDtl.prescription_id = pres.id " +
//					        " left join pharmacy.t_issue_detail issue on " +
//					        " issue.prescription_detail_id = presDtl.id " +
//					        " where " +
//					        " presDtl.is_medication_stop = 'N' " +
//					        " and presDtl.status_id != 9 " +
//					        " and pres.admission_id =:admId " +
//					        " and issue.status_id!= 2")
//					.setParameter("admId", admissionDto.getAdmissionId())
//					.setResultTransformer(Transformers.aliasToBean(MedicationDTO.class)).list();
			
			if((billList!=null && billList.size()>0) || (orderList!=null && orderList.size()>0) ) {//|| (prescriptionList!=null && prescriptionList.size()>0)
				return new Response(ERROR, null, " Can not cancel the admission. Please check pending services in Patient's bill.", null, null);
			}else {
				sessionFactory.getCurrentSession()
				.createSQLQuery("update  adt.t_admission set status='I',is_admission_cancelled='Y',admission_cancelled_by=:userId, "
				+" admission_cancelled_date_time=:cancelledDate where admission_id=:admId ")
				.setParameter("admId", admissionDto.getAdmissionId())
				.setParameter("userId", admissionDto.getUserId())
				.setTimestamp("cancelledDate", TimeZoneComponent.getDateByzone(admissionDto.getUnitId()))
				.executeUpdate();
				
				sessionFactory.getCurrentSession()
				.createSQLQuery("update adt.t_admission_details set status='I' where admission_id =:admissionId ").setParameter("admissionId", admissionDto.getAdmissionId()).executeUpdate();
				
				// <-----Changing status of bed to Booked (5 is the ID of
				// 'Booked'status)
				Query qry2 = sessionFactory.getCurrentSession().createQuery("update BedMaster bed set "
						+ " bed.bedStatusId=3, "
						+ " bed.updatedBy=:updatedBy, "
						+ " bed.updatedDate=:updatedDate "
						+ " where bed.bedId=:bedId "
						+ " AND bed.organizationId=:organizationId "
						+ " AND bed.unitId=:unitId "
						+ " AND bed.status='A' ")
				.setParameter("updatedBy", admissionDto.getUpdatedBy())
				.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-M-yyyy HH:mm:ss"))
				.setParameter("bedId", admissionDto.getBedId())
				.setParameter("organizationId", admissionDto.getOrganizationId())
				.setParameter("unitId", admissionDto.getUnitId());
				qry2.executeUpdate();
				// ----->

				// <-----Updating status of current bed to I in BedLogStatus
				Query qry3 = sessionFactory.getCurrentSession().createQuery("update BedLogStatus bedStatus set bedStatus.status='I', "
						+ " bedStatus.updatedBy=:updatedBy, "
						+ " bedStatus.updatedDate=:updatedDate"
						+ " WHERE bedStatus.status='A' " 
						+ " AND bedStatus.bedId=:bedId "
						+ " AND bedStatus.organizationId=:organizationId "
						+ " AND bedStatus.unitId=:unitId ")
						.setParameter("updatedBy", admissionDto.getUpdatedBy())
						.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-M-yyyy HH:mm:ss"))
						.setParameter("bedId", admissionDto.getBedId())
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId());
				qry3.executeUpdate();
				// ----->

				// <-----Adding new entry in log as the status of bed is changing
				BedLogStatus bedLogStatus = new BedLogStatus();
					bedLogStatus.setCreatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					bedLogStatus.setUpdatedDate(ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
					bedLogStatus.setCreatedBy(admissionDto.getUpdatedBy());
					bedLogStatus.setUpdatedBy(admissionDto.getUpdatedBy());
					bedLogStatus.setStatus('A');
					bedLogStatus.setBedId(admissionDto.getBedId());
					bedLogStatus.setBedStatusId(3);
					bedLogStatus.setOrganizationId(admissionDto.getOrganizationId());
					bedLogStatus.setUnitId(admissionDto.getUnitId());
				sessionFactory.getCurrentSession().save(bedLogStatus);
				// ----->
				
				Criteria c=sessionFactory.getCurrentSession().createCriteria(AdmissionNote.class); 
					c.add(Restrictions.eq("admissionId", admissionDto.getAdmissionId()));
					c.add(Restrictions.eq("admissionStatus", 'B'));
					c.add(Restrictions.eq("status", 'A'));
				AdmissionNote admissionNote = (AdmissionNote)c.uniqueResult();
				
				if(admissionNote!=null && admissionNote.getAdmissionNoteId()>0)
				{
					// <-----Updating admission note's admission status to B(Booked)
					// from P(Pending)
					admissionNote.setAdmissionStatus('P');
					sessionFactory.getCurrentSession().update(admissionNote);
					// ----->
		
					// <-----Updating status of last admission patient mapper record to
					// I(inactive)
					Query qry1 = sessionFactory.getCurrentSession().createQuery(  "update AdmissionPatientMapper m set m.activeStatus = :activeStatus, " +
							"m.updatedBy = :updatedBy, " +
							"m.updatedDate = :updatedDate " +
							"where m.admissionNoteId = :admissionNoteId and m.status = :status");

					qry1.setParameter("activeStatus", 'P');
					qry1.setParameter("updatedBy", admissionDto.getUpdatedBy());
					qry1.setParameter("updatedDate", ADTCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(admissionDto.getUnitId()), "dd-MM-yyyy HH:mm:ss"));
					qry1.setParameter("admissionNoteId", admissionNote.getAdmissionNoteId());
					qry1.setParameter("status", 'A');

					qry1.executeUpdate();
					// ----->
		
				}
				
				return new Response(SUCCESS, null, null, null, null);
			}

		} catch (HibernateException he) {

		} catch (Exception e) {

		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	
	}

	@Override
	public Response getPatientDetailsForWorklistForWebPage(AdmissionDto admissionDto) throws ApplicationException {
		try {
			if(admissionDto.getVisitTypeId() == OP) {
				String actualQuery= " select " 
						 + " 	enMst.encounter_id as \"encounterId\", " 
						 + " 	(case " 
						 + " 		when enMst.encounter_type_id = 6 then enMst.review_against_enc_id " 
						 + " 		when enMst.against_enc_id is not null then enMst.against_enc_id " 
						 + " 		else enMst.encounter_id " 
						 + " 	end) as \"openCoversheetWithEncounterId\", " 
						 + " 	enMst.against_enc_id as \"againstEncId\", " 
						 + " 	enMst.encounter_number as \"encounterNumber\", " 
						 + " 	enMst.created_by as \"createdBy\", " 
						 + " 	enMst.encounter_date as \"encounterDate\", " 
						 + " 	(case " 
						 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'YYYY-MM-DD HH24:MI') " 
						 + " 		else to_char(enMst.encounter_date, 'YYYY-MM-DD HH24:MI') " 
						 + " 	end) as \"encounterDateWithTime\", " 
						 + " 	(case " 
						 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'DD/MM/YYYY HH24:MI:SS') " 
						 + " 		else to_char(enMst.encounter_date, 'DD/MM/YYYY HH24:MI:SS') " 
						 + " 	end) as \"encounterDateWithTimeString\", " 
						 + " 	(case " 
						 + " 		when am.is_paid_appointment = 'Y' then to_char(am.arrival_time, 'HH24:MI:SS') " 
						 + " 		else to_char(enMst.encounter_date, 'HH24:MI:SS') " 
						 + " 	end) as \"createdTime\", " 
						 + " 	enMst.user_defined_visit_type_id as \"userDefinedVisitTypeId\" , " 
						 + " 	enMst.status as \"status\", " 
						 + " 	enMst.status_id as \"statusId\", " 
						 + " 	enMst.is_called as \"isCalled\", " 
						 + " 	enMst.call_count as \"callCount\", " 
						 + " 	enMst.cabin_id as \"cabinId\", " 
						 + " 	enMst.doctor_desk_arrival as \"doctorDeskArrival\", " 
						 + " 	patient.is_vip as \"isVip\", " 
						 + " 	patient.first_name as \"firstName\", " 
						 + " 	patient.middle_name as \"middleName\", " 
						 + " 	patient.uuid as \"uuid\", " 
						 + " 	patient.last_name as \"lastName\", " 
						 + " 	patient.patient_remark as \"patientRemark\", " 
						 + " 	(case " 
						 + " 		when patient.is_vip = 'Y' then coalesce(patient.alise_name, patient.first_name) " 
						 + " 		else concat(coalesce(patient.first_name) , ' ', coalesce(patient.middle_name) , ' ', coalesce(patient.last_name)) " 
						 + " 	end ) as \"pFirstName\", " 
						 + " 	to_char(sm.appointment_date, 'dd/MM/yyyy') as \"appointmentDate\", " 
						 + " 	am.appointment_id as \"appointmentId\", " 
						 + " 	sm.slot_id as \"slotId\", " 
						 + " 	concat(to_char(sm.from_time, 'HH24:MI')) as \"fromTimeString\", " 
						 + " 	concat(to_char(sm.to_time, 'HH24:MI')) as \"toTimeString\", " 
						 + " 	am.remark as \"remark\", " 
						 + " 	enMst.doctor_id as \"doctorId\", " 
						 + " 	enMst.payment_entitlement_type_id as \"paymentEntitlementTypeId\", " 
						 + " 	am.appointment_type_id as \"appointmentTypeId\", " 
						 + " 	enMst.encounter_type_id as \"encounterTypeId\", " 
						 + " 	am.appointment_status_id as \"appointmentStatusId\", " 
						 + " 	asm.appointment_status_desc as \"appointmentStatusDesc\", " 
						 + " 	concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name)) as \"dFirstName\", " 
						 + " 	enMst.preferred_doc_id as \"preferredDocId\", " 
						 + " 	concat('Dr. ', coalesce(prefDoc.first_name), ' ', coalesce(prefDoc.middle_name), ' ', coalesce(prefDoc.last_name)) as \"preferedDocName\", " 
						 + " 	concat('Dr. ', coalesce(doc.first_name), ' ' , coalesce(doc.last_name)) as \"docNameTemp\", " 
						 + " 	enMst.dept_id as \"specialityId\", " 
						 + " 	speciaityMaster.speciality_name as \"specialityName\", " 
						 + " 	concat(extract(year from age(current_timestamp, patient.dob)) , 'Y ', extract(month from age(current_timestamp, patient.dob)) , 'M ' , extract(day from age(current_timestamp, patient.dob)), 'D ') as \"age\", " 
						 + " 	concat(extract(year from age(current_timestamp, patient.dob))) as \"ageYear\", " 
						 + " 	patient.mobile_number as \"mobileNumber\", " 
						 + " 	patient.email as \"email\", "
						 + "    patient.video_call_url as \"videoCallUrl\", " 
						 + " 	doc.email as \"emailDoctor\", " 
						 + " 	patient.patient_id as \"patientId\", " 
						 + " 	patient.uhid_number as \"uhidNumber\", " 
						 + " 	patient.prefix_id as \"prefixId\", " 
						 + " 	prefix.prefix_code as \"prefixCode\", " 
						 + "    prefix.prefix_desc as \"prefixDesc\", "
						 + " 	patient.gender_id as \"genderId\", " 
						 + " 	patient.image_path as \"imagePath\", " 
						 + " 	patient.patient_category_id as \"patientCategoryId\", " 
						 + " 	patient.mpshah_patient_id as \"mpshahPatientId\", " 
						 + " 	gender.gender_code as \"genderCode\", " 
						 + " 	gender.gender_name as \"genderDec\", " 
						 + " 	to_char(patient.dob, 'DD/MM/YYYY') as \"dob\", " 
						 + " 	to_char(patient.dob, 'MM/DD/YYYY') as \"dobForDatePicker\", " 
						 + " 	sm.modality_id as \"modalityId\", " 
						 + " 	modality.modality_desc as \"modalityDesc\", " 
						 + " 	enDet.is_assignment_comp_status as \"isAssignmentCompStatus\", " 
						 + " 	enDet.is_consultation_status as \"isConsultationStatus\", " 
						 + " 	to_char(enDet.consultation_start_time, 'HH24:MI:SS') as \"consultationStartTime\", " 
						 + " 	to_char(enDet.consultation_start_time, 'YYYY-MM-DD HH24:MI') as \"consultationStartTimeWithTime\", " 
						 + " 	to_char(enDet.consultation_stop_time, 'HH24:MI:SS') as \"consultationStopTime\", " 
						 + " 	to_char(enDet.consultation_start_time, 'dd-MM-yyyy HH:MM:SS') as \"consultationStartTimeDate\", " 
						 + " 	to_char(enDet.consultation_stop_time, 'dd-MM-yyyy HH:MM:SS') as \"consultationStopTimeDate\", " 
						 + " 	refrlTypeMst.referral_type_name as \"referralTypeName\", " 
						 + " 	reflMst.referral_name as \"referralName\", "
						 + "	reflMst.referral_id as \"referralId\", " 
						 + " 	refrDtls.referral_remark as \"referralRemark\", " 
						 + " 	adm.admission_id as \"admissionId\", " 
						 + " 	to_char(enDet.assignment_start_time, 'HH:MM:SS') as \"assignmentStartTime\", " 
						 + " 	to_char(enDet.assignment_start_time, 'dd-MM-yyyy HH:MM:SS') as \"assignmentStartTimeDate\", " 
						 + " 	to_char(enDet.assignment_stop_time, 'HH:MM:SS') as \"assignmentStopTime\", " 
						 + " 	to_char(enDet.assignment_stop_time, 'dd-MM-yyyy HH:MM:SS') as \"assignmentStopTimeDate\", " 
						 + " 	enDet.assignment_start_by as \"assignmentStartBy\", " 
						 + " 	enDet.assignment_stop_by as \"assignmentStopBy\", " 
						 + " 	enDet.assignment_start_role_id as \"assignmentStartRoleId\", " 
						 + " 	enDet.assignment_stop_role_id as \"assignmentStopRoleId\", " 
						 + " 	enDet.consultation_start_by as \"consultationStartBy\", " 
						 + " 	enDet.consultation_start_role_id as \"consultationStartRoleId\", " 
						 + " 	enDet.encounter_details_mapper_id as \"encounterDetailsMapperId\", " 
						 + " 	spd.contract_id as \"contractId\", " 
						 + " 	spd.payee_id as \"payeeId\", " 
						 + " 	enMst.doc_queue_number as \"queueNumber\", " 
						 + " 	enMst.unit_token_number as \"tokenNumber\", " 
						 + " 	enDet.is_priority as \"isPriority\", " 
						 + " 	enMst.is_critical as \"isCritical\", " 
						 + " 	doc.allowed_over_booking_slots as \"allowedOverBookingSlots\", " 
						 + " 	enMst.speciality_queue_number as \"specialityQueueNumber\", " 
						 + " 	speciaitymapper.dept_wise_appointment as \"deptWiseAppointment\", " 
						 + " cast(mbm.billing_master_id as integer) as \"billId\", "
						 + " 	enMst.old_doc_id as \"oldDocId\", " 
						 + " 	(case " 
						 + " 		when (enMst.is_critical = 'Y' " 
						 + " 		or enDet.is_priority = 'Y') then 1 " 
						 + " 		when (enMst.is_critical = 'N' " 
						 + " 		or enDet.is_priority is null) then 2 " 
						 + " 	end) as \"isCriticalOrder\", " 
						 + " 	(case " 
						 + " 		when enDet.is_priority = 'Y' then 1 " 
						 + " 		when enDet.is_priority is null then 2 " 
						 + " 	end) as \"isPriorityOrder\", " 
						 + " 	(case " 
						 + " 		when am.is_cyclic_appointment = 'N' then coalesce((select nursing_acceptance from adt.t_admission_details where admission_id =( select admission_id from adt.t_admission where patient_id = enMst.patient_id and status = 'A' order by 1 desc limit 1 ) order by 1 desc limit 1 ), 'N') " 
						 + " 		else 'N' " 
						 + " 	end) as \"nursingAccepatance\", " 
						 + " 	tod.service_rendered as \"serviceRenderId\", " 
						 + " 	tod.order_details_id as \"orderDetailsId\", " 
						 + " 	patient.whatsapp_no as \"whatsAppCallingNo\", " 
						 + " 	patient.whatsapp_calling_code as \"whatsAppCallingCode\", " 
						 + " 	patient.skype_id as \"skypeId\", " 
						 + " 	am.is_teleconsultation_apppointment as \"isTeleconsultationAppointment\", " 
						 + " 	mbm.bill_status_id as \"billStatusId\", "
						 + " 	(select array_to_string(array(select concat('Dr. ', coalesce(doc.first_name), ' ', coalesce(doc.last_name)) from opd.t_encounter_doctor_consultants_mapper mpr "
						 + " 		inner join doctor.m_doctor_master doc  "
						 + " 				on doc.doctor_id = mpr.doctor_id  "
						 + " 				where mpr.encounter_id =enMst.encounter_id and mpr.doctor_id!=enMst.doctor_id), ', ')) as \"otherConsultants\"  "
						 + " from " 
						 + " 	opd.t_encounter_master enMst " 
						 + " left join opd.t_encounter_details enDet on " 
						 + " 	enDet.encounter_id = enMst.encounter_id " 
						 + " left join opd.m_appointment_master am on " 
						 + " 	am.appointment_id = enMst.appointment_id " 
						 + " left join opd.m_slot_master sm on " 
						 + " 	am.slot_id = sm.slot_id " 
						 + " left join public.t_referal_details refrDtls on " 
						 + " 	(case " 
						 + " 		when enMst.against_enc_id is not null then refrDtls.encounter_id = enMst.against_enc_id " 
						 + " 		else refrDtls.encounter_id = enMst.encounter_id " 
						 + " 	end) " 
						 + " 	and refrDtls.status = 'A' " 
						 + " left join adt.m_referral_type_master refrlTypeMst on " 
						 + " 	refrlTypeMst.referral_type_id = refrDtls.referral_type_id " 
						 + " left join adt.m_referral_master reflMst on " 
						 + "  reflMst.referral_id = refrDtls.referral_id " 
						 + " 	and reflMst.status = 'A' " 
						 + " left join opd.t_patient_sponsor_details spd on " 
						 + " 	spd.encounter_id = enMst.encounter_id " 
						 + " 	and spd.priority_id = 1 " 
						 + " 	and spd.status = 'A' " 
						 + " left join public.m_clinic_master clinMst on " 
						 + " 	clinMst.clinic_master_id = enMst.clinic_id " 
						 + " left join opd.t_nursing_station_clinic_mapper nurStatClinMapp on " 
						 + " 	nurStatClinMapp.clinic_id = clinMst.clinic_master_id " 
						 + " left join doctor.m_speciality_master speciaityMaster on " 
						 + " 	enMst.dept_id = speciaityMaster.speciality_id " 
						 + " left join t_unit_speciality_mapper speciaityMapper on " 
						 + " 	enMst.dept_id = speciaityMapper.speciality_id " 
						 + " 	and speciaityMapper.unit_id = enMst.unit_id " 
						 + " left  join doctor.m_doctor_master doc on " 
						 + " 	enMst.doctor_id = doc.doctor_id " 
						 + " left  join doctor.m_doctor_master prefDoc on " 
						 + " 	enMst.preferred_doc_id = prefDoc.doctor_id " 
						 + " left  join public.m_modality_master modality on " 
						 + " 	sm.modality_id = modality.modality_id " 
						 + " left join patient.t_patient_registration patient on " 
						 + " 	enMst.patient_id = patient.patient_id " 
						 + " left join public.m_gender_master gender on " 
						 + " 	patient.gender_id = gender.gender_id " 
						 + " left join public.m_prefix_master prefix on " 
						 + " 	patient.prefix_id = prefix.prefix_id " 
						 + " left join opd.m_appointment_status_master asm on " 
						 + " 	asm.appointment_status_id = enMst.status_id " 
						 + " left  join adt.t_admission adm on " 
						 + " 	adm.admission_id = am.admission_id " 
						 + " left join t_order_master tom on "
						 + " 	tom.encounter_id = enMst.encounter_id "
						 + " left outer join t_order_details tod on "
						 + " 	tod.order_id = tom.order_id "
						 + " 	and tod.is_consultation_service = 'Y' "
						 + "	and tod.status = 'A' "
						 + "	and ((select tcnd.credit_note_detail_id from billing.t_credit_note_detail tcnd where tcnd.order_detail_id = tod.order_details_id and tcnd.status = 'A' and tcnd.cancel_or_discount = 1) is null) "
						 + " left  join billing.t_billing_details bdtls on " 
						 + " 	bdtls.order_details_id = tod.order_details_id " 
						 + " 	and bdtls.status = 'A' " 
						 + " left  join billing.m_billing_master mbm on " 
						 + " 	mbm.billing_master_id = bdtls.billing_master_id " 
						 + " 	and mbm.status = 'A' " 
						 //New Added start
						 + " inner join ( select patient_id , to_char(encounter_date, 'dd-MM-yyyy') as ddate, min(encounter_id) FirstEntID "
						 + "  from  opd.t_encounter_master tem  where status_id in (5, 6, 9) "
						 + "  group by  patient_id ,  to_char(encounter_date, 'dd-MM-yyyy') ) FirstEncountfoday on "
						 + "  FirstEncountfoday.patient_id = enMst.patient_id and FirstEncountfoday.ddate = to_char(enMst.encounter_date, 'dd-MM-yyyy') "
			             + "  inner join opd.t_encounter_master Firstentmst on Firstentmst.encounter_id = FirstEncountfoday.FirstEntID "
						 //New Added End
						 + " where " 
						 + " 	enMst.organization_id =:orgId " 
						 + " 	and enMst.unit_id =:unitId " 
						 + " 	and enMst.patient_id =:patientId " 
						 + " 	and enMst.doctor_id =:doctorId " 
						 + " 	and (enMst.status_id in (5,6,9) " 
						 + " 	and mbm.bill_status_id in (2,3) " 
						 + " 	and mbm.status = 'A' " 
						 + " 	or (am.is_teleconsultation_apppointment = 1 " 
						 + " 	and mbm.bill_status_id = 3  and mbm.status = 'A'  and enMst.status_id in (5,6,9))) " 
						 + " 	and patient.registration_type_id in (1,3,4,6) " 
						 + " 	and am.appointment_type_id in (1,5,6,9) " ;
				StringBuffer subQuery = new StringBuffer();	
				Integer encounterId = admissionDto.getEncounterId();
					subQuery.append(" and enMst.encounter_id="+encounterId);
				actualQuery = actualQuery.concat(subQuery.toString());			
				List<SlotMasterDto> patientRegistrationsList = sessionFactory.getCurrentSession().createSQLQuery(actualQuery)
						.setParameter("patientId", admissionDto.getPatientId())
						.setParameter("doctorId", admissionDto.getDoctorId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameter("orgId", admissionDto.getOrganizationId())
						.setResultTransformer(Transformers.aliasToBean(SlotMasterDto.class)).list();
				
				String actualQuery1= "select\r\n" + 
						"			distinct allergypat0_.allergy_id as \"allergyId\", " + 
						"			allergypat0_.patient_id as \"patientId\", " + 
						"			allergypat0_.allergy_type_id as \"allergyTypeId\", " + 
						"			allergymas1_.allergy_name as \"allergyName\", " + 
						"			allergymas1_.allergy_desc as \"allergyDesc\", " + 
						"			allergytyp3_.allergy_type_desc as \"allergyTypeDesc\", " + 
						"			allergypat0_.is_no_known_allergies as \"isNoKnownAllergies\", " + 
						"			generic2_.id as \"genericId\", " + 
						"			generic2_.name as \"genericName\"  " + 
						"		from\r\n" + 
						"			patient.t_allergy_patient_mapper allergypat0_  " + 
						"		left outer join emr.m_allergy_master allergymas1_ on " + 
						"			allergypat0_.allergy_id = allergymas1_.allergy_id " + 
						"		left outer join public.m_generic generic2_ on " + 
						"			allergypat0_.generic_id = generic2_.id " + 
						"		left outer join emr.m_allergy_type_master allergytyp3_ on " + 
						"			allergypat0_.allergy_type_id = allergytyp3_.allergy_type_id " + 
						"		left outer join emr.m_severity_master severityma4_ on " + 
						"			allergypat0_.severity_id = severityma4_.severity_id " + 
						"		where " + 
						"			allergypat0_.patient_id =:patientId" + 
						"			and allergypat0_.unit_id =:unitId " + 
						"			and allergypat0_.organization_id =:organizationId " + 
						"			and( " + 
						"				allergypat0_.resolved_remark is null " + 
						"			) " + 
						"			and allergypat0_.status = 'A' ";
				
				
				List<AllergyPatientMapperDto> allergyPatientMapperDtoList = sessionFactory.getCurrentSession().createSQLQuery(actualQuery1)
						.setParameter("organizationId", admissionDto.getOrganizationId())
						.setParameter("unitId", admissionDto.getUnitId())
						.setParameter("patientId", admissionDto.getPatientId())
						.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
				
				String allergyTypeDesc = "";
	            Integer i = 0;
	            
	            //For min time
	            for (i = 0; i < allergyPatientMapperDtoList.size(); i++) {
	                if(i == 0) {
	                	allergyTypeDesc = allergyTypeDesc.concat(allergyPatientMapperDtoList.get(i).getAllergyTypeDesc()); 
	                }else {
	                	allergyTypeDesc = allergyTypeDesc.concat(",").concat(allergyPatientMapperDtoList.get(i).getAllergyTypeDesc());  
	                }
	            }
	            
	            com.param.global.common.Response res2 =iKinDetailsDao.getKinDetailsByPatientId(admissionDto.getPatientId(),admissionDto.getUnitId(),admissionDto.getOrganizationId());
	            Integer i1 = 0;
				 String emergencyNumber = "";
				if(res2.getListObject().size()>0){
					List<KinDetailsDto> list = res2.getListObject();
					KinDetailsDto kin = list.get(0);
					emergencyNumber = kin.getMobileNo();
				}
				
				for(SlotMasterDto dto : patientRegistrationsList)
				{
					if(allergyTypeDesc != "") {
						dto.setAllergyTypeDesc(allergyTypeDesc);
					}
					if(emergencyNumber != "") {
						dto.setEmergencyNumber(emergencyNumber);
					}
				}
				return new Response(SUCCESS, null, null, patientRegistrationsList, null);

			}
			else {
				    
				    Integer admissionId = admissionDto.getEncounterId();
					String query = sessionFactory.getCurrentSession().getNamedQuery("GET_PATIENT_DETAILS_FOR_WORKLIST_FOR_WEB_PAGE_ADMISSION").getQueryString();
					String finalQuery;
					List<AdmissionDto> admissionDtosList;
						admissionDtosList = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
								.setParameter("organizationId", admissionDto.getOrganizationId())
								.setParameter("unitId", admissionDto.getUnitId())
								.setParameter("visitTypeId", admissionDto.getVisitTypeId())
								.setParameter("patientId", admissionDto.getPatientId())
								.setParameter("admissionId",admissionId)
								.setParameter("doctorId", admissionDto.getDoctorId())
								.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();
	
						
						String actualQuery1= "select\r\n" + 
								"			distinct allergypat0_.allergy_id as \"allergyId\", " + 
								"			allergypat0_.patient_id as \"patientId\", " + 
								"			allergypat0_.allergy_type_id as \"allergyTypeId\", " + 
								"			allergymas1_.allergy_name as \"allergyName\", " + 
								"			allergymas1_.allergy_desc as \"allergyDesc\", " + 
								"			allergytyp3_.allergy_type_desc as \"allergyTypeDesc\", " + 
								"			allergypat0_.is_no_known_allergies as \"isNoKnownAllergies\", " + 
								"			generic2_.id as \"genericId\", " + 
								"			generic2_.name as \"genericName\"  " + 
								"		from\r\n" + 
								"			patient.t_allergy_patient_mapper allergypat0_  " + 
								"		left outer join emr.m_allergy_master allergymas1_ on " + 
								"			allergypat0_.allergy_id = allergymas1_.allergy_id " + 
								"		left outer join public.m_generic generic2_ on " + 
								"			allergypat0_.generic_id = generic2_.id " + 
								"		left outer join emr.m_allergy_type_master allergytyp3_ on " + 
								"			allergypat0_.allergy_type_id = allergytyp3_.allergy_type_id " + 
								"		left outer join emr.m_severity_master severityma4_ on " + 
								"			allergypat0_.severity_id = severityma4_.severity_id " + 
								"		where " + 
								"			allergypat0_.patient_id =:patientId" + 
								"			and allergypat0_.unit_id =:unitId " + 
								"			and allergypat0_.organization_id =:organizationId " + 
								"			and( " + 
								"				allergypat0_.resolved_remark is null " + 
								"			) " + 
								"			and allergypat0_.status = 'A' ";
						
						
						StringBuffer subQuery = new StringBuffer();	
						List<AllergyPatientMapperDto> allergyPatientMapperDtoList = sessionFactory.getCurrentSession().createSQLQuery(actualQuery1)
								.setParameter("organizationId", admissionDto.getOrganizationId())
								.setParameter("unitId", admissionDto.getUnitId())
								.setParameter("patientId", admissionDto.getPatientId())
								.setResultTransformer(Transformers.aliasToBean(AllergyPatientMapperDto.class)).list();
						
						String allergyTypeDesc = "";
			            Integer i = 0;
			            
			            //For min time
			            for (i = 0; i < allergyPatientMapperDtoList.size(); i++) {
			                if(i == 0) {
			                	allergyTypeDesc = allergyTypeDesc.concat(allergyPatientMapperDtoList.get(i).getAllergyTypeDesc()); 
			                }else {
			                	allergyTypeDesc = allergyTypeDesc.concat(",").concat(allergyPatientMapperDtoList.get(i).getAllergyTypeDesc());  
			                }
			            }
			            
			            com.param.global.common.Response res2 =iKinDetailsDao.getKinDetailsByPatientId(admissionDto.getPatientId(),admissionDto.getUnitId(),admissionDto.getOrganizationId());
			            Integer i1 = 0;
			            String emergencyNumber = "";
						if(res2.getListObject().size()>0){
							List<KinDetailsDto> list = res2.getListObject();
							KinDetailsDto kin = list.get(0);
							emergencyNumber = kin.getMobileNo();
						}
						
						
						for(AdmissionDto dto : admissionDtosList)
						{
							if(allergyTypeDesc != "") {
								dto.setAllergyTypeDesc(allergyTypeDesc);
							}
							if(emergencyNumber != "") {
								dto.setEmergencyNumber(emergencyNumber);
							}
						}
					return new Response(SUCCESS, null, null, admissionDtosList, null);
			}
			
			

			

		} catch (HibernateException he) {

		} catch (Exception e) {
			
		} 
		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAdmittedPatientListForDietOrder(AdmissionDto admissionDto) throws ApplicationException {
		try {
			/*String query = "SELECT "
					+ "  admission.admission_id as \"admissionId\", "
					+ "  admission.patient_id as \"patientId\", "
					+ "  admission.t_patient_id as \"tPatientId\", "
					+ "  admission.admission_number as \"admissionNumber\", "
					+ "  admission.visit_type_id as \"visitTypeId\", "
		            + "  admission.is_final_bill as \"isFinalBill\", "
					+ "  admission.doctor_id as \"doctorId\", "
					+ "  admission.speciality_id as \"specialityId\", "
					+ "  sp.speciality_name as \"specialityName\", "
					+ "  pr.uhid_number as \"UHIDNumber\", "
					+ "  pr.gender_id as \"genderId\", "
					+ "  gm.gender_code as \"genderCode\", "
					+ "  to_char(pr.dob, 'MM/DD/YYYY') as \"dob\", "
					+ "  concat(COALESCE(doc.first_name,''),' ',COALESCE(doc.middle_name,''),' ',COALESCE(doc.last_name,'')) as \"dFirstName\", "
					+ "  to_char(adl.doa, 'DD/MM/YYYY') as \"doa\", "
					+ "  to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\", "
					+ "  spd.payment_entitlement_id as \"paymentEntitlementId\", "
					+ "  spd.contract_id as \"contractId\", "
					+ "  spd.payee_id as \"payeeId\", "
					+ "  pe.payment_entitlement_desc as \"paymentEntitlementDesc\", "
					+ "  bedCatMst.bed_category_desc as \"bedCategoryDesc\", "
					+ "  bedCatMst.billing_bed_category_id as \"billingBedCategoryId\", "
					+ "  bedCatMst.bed_category_id as \"bedCategoryId\", "
					+ "  bm.floor_id as \"floorId\", "
					+ "  bm.bed_number as \"bedNumber\", "
					+ "  to_char(adl.doa, 'HH:MI AM') as \"createdTime\","
					+ "  adl.bed_id as \"bedId\", "
					+ "  adl.ward_id as \"wardId\", "
					+ "  adl.room_id as \"roomId\", "
					+ "  ward.ward_name as \"wardName\", "
	                + "  floor.floor_name as \"floorName\", "
					+ "  adl.is_short_leave as \"isShortLeave\", "
					+ "  pr.patient_category_id as \"patientCategoryId\", "
					+ "  admission.concent as \"concent\", "
					+ "  medic.medico_legal_id as \"medicoLegalId\","
					+ "  disc.discharge_status_id as \"dischargeStatusId\", "
					+ "  disc.discharge_id as \"dischargeId\", "
					+ "  pr.is_vip as \"isVip\", "
					+ "  pr.uuid as \"uuid\", "
					+ "  prefxmstr.prefix_id as \"prefixId\" ,"
					 + " concat(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob)) ,'Y ', EXTRACT(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , EXTRACT(day from AGE(CURRENT_TIMESTAMP, pr.dob)),'D ') as \"ageStr\", "
					+ "  prefxmstr.prefix_desc as \"prefixDesc\",pr.image_path as \"imagePath\" , "
					+ "  (case when pr.is_vip='Y' then "
					+ "  coalesce(pr.alise_name,pr.first_name) "
					+ "  else "
					+ "  concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name)) "
					+ "  end) as \"pFirstName\", "
					+ "  (case when (select total_score from adt.t_mews_mapper where admission_id = admission.admission_id and status='A') > 6 then "
					+ "  	1 "
					+ "   else "
					+ "     0 end) as \"isCriticalOrder\","
					+ " admission.reference_number as \"referenceNumber\","
					+ " bedCatMst.is_icu as \"isIcuu\" "
					+ "FROM "
					+ "  adt.t_admission admission "
					+ "  INNER JOIN patient.t_patient_registration pr on admission.patient_id = pr.patient_id"
					+ "  INNER JOIN public.m_prefix_master prefxmstr on prefxmstr.prefix_id = pr.prefix_id "
					+ "  INNER JOIN public.m_gender_master gm on pr.gender_id = gm.gender_id "
					+ "  INNER JOIN doctor.m_doctor_master doc on admission.doctor_id = doc.doctor_id "
					+ "  INNER JOIN doctor.m_speciality_master sp on admission.speciality_id = sp.speciality_id "
					+ "  INNER JOIN adt.t_admission_details adl on adl.admission_id = admission.admission_id "
					+ "  INNER JOIN adt.m_bed_master bm on adl.bed_id = bm.bed_id "
					+ "  INNER JOIN adt.m_ward_master ward on bm.ward_id = ward.ward_id "
		            + "  INNER JOIN adt.m_floor_master floor on ward.floor_id = floor.floor_id "
					+ "  INNER JOIN opd.t_patient_sponsor_details spd on spd.admission_id=admission.admission_id AND spd.priority_id=1 and spd.status='A' "
					+ "  INNER JOIN adt.m_bed_category_master bedCatMst on adl.bed_category_id = bedCatMst.bed_category_id "
					+ "  INNER JOIN public.m_payment_entitlement_master pe on spd.payment_entitlement_id = pe.payment_entitlement_id "
					+ "  LEFT JOIN adt.t_discharge disc on admission.admission_id = disc.admission_id and disc.status='A' "
					+ "  LEFT JOIN adt.t_medico_legal medic on admission.admission_id = medic.admission_id "
					+ "WHERE "
					+ "  admission.status = 'A' "
					+ " AND adl.status='A' "
					+ " AND adl.nursing_acceptance = 'Y' "
					+ "  AND admission.organization_id = :organizationId "
					+ "  AND admission.unit_id = :unitId "
					+ " AND admission.visit_type_id=:visitTypeId "
//					+ " AND bm.ward_id =:wardId " 
//					+ " order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc "
					;*/
			 String query1 = "select  " + 
			 		"	admission.admission_id as \"admissionId\",  " + 
			 		"	admission.patient_id as \"patientId\",  " + 
			 		"	admission.t_patient_id as \"tPatientId\",  " + 
			 		"	admission.admission_number as \"admissionNumber\",  " + 
			 		"	admission.visit_type_id as \"visitTypeId\",  " + 
			 		"	admission.is_final_bill as \"isFinalBill\",  " + 
			 		"	admission.doctor_id as \"doctorId\",  " + 
			 		"	admission.speciality_id as \"specialityId\",  " + 
			 		"	sp.speciality_name as \"specialityName\",  " + 
			 		"	pr.uhid_number as \"UHIDNumber\",  " + 
			 		"	pr.gender_id as \"genderId\",  " + 
			 		"	gm.gender_code as \"genderCode\",  " + 
			 		"	to_char(pr.dob, 'MM/DD/YYYY') as \"dob\",  " + 
			 		"	concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"dFirstName\",  " + 
			 		"	to_char(adl.doa, 'DD/MM/YYYY') as \"doa\",  " + 
			 		"	to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\",  " + 
			 		"	spd.payment_entitlement_id as \"paymentEntitlementId\",  " + 
			 		"	spd.contract_id as \"contractId\",  " + 
			 		"	spd.payee_id as \"payeeId\",  " + 
			 		"	pe.payment_entitlement_desc as \"paymentEntitlementDesc\",  " + 
			 		"	bedCatMst.bed_category_desc as \"bedCategoryDesc\",  " + 
			 		"	bedCatMst.billing_bed_category_id as \"billingBedCategoryId\",  " + 
			 		"	bedCatMst.bed_category_id as \"bedCategoryId\",  " + 
			 		"	bm.floor_id as \"floorId\",  " + 
			 		"	bm.bed_number as \"bedNumber\",  " + 
			 		"	to_char(adl.doa, 'HH:MI AM') as \"createdTime\",  " + 
			 		"	adl.bed_id as \"bedId\",  " + 
			 		"	adl.ward_id as \"wardId\",  " + 
			 		"	adl.room_id as \"roomId\",  " + 
			 		"	ward.ward_name as \"wardName\",  " + 
			 		"	floor.floor_name as \"floorName\",  " + 
			 		"	adl.is_short_leave as \"isShortLeave\",  " + 
			 		"	pr.patient_category_id as \"patientCategoryId\",  " + 
			 		"	admission.concent as \"concent\",  " + 
			 		"	medic.medico_legal_id as \"medicoLegalId\",  " + 
			 		"	disc.discharge_status_id as \"dischargeStatusId\",  " + 
			 		"	disc.discharge_id as \"dischargeId\",  " + 
			 		"	pr.is_vip as \"isVip\",  " + 
			 		"	pr.uuid as \"uuid\",  " + 
			 		"	prefxmstr.prefix_id as \"prefixId\" ,  " + 
			 		"	concat(extract(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ', extract(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , extract(day from AGE(CURRENT_TIMESTAMP, pr.dob)), 'D ') as \"ageStr\",  " + 
			 		"	prefxmstr.prefix_desc as \"prefixDesc\",  " + 
			 		"	pr.image_path as \"imagePath\" ,  " + 
			 		"	(case  " + 
			 		"		when pr.is_vip = 'Y' then coalesce(pr.alise_name, pr.first_name)  " + 
			 		"		else concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name))  " + 
			 		"	end) as \"pFirstName\",  " + 
			 		"	(case  " + 
			 		"		when (  " + 
			 		"		select  " + 
			 		"			total_score  " + 
			 		"		from  " + 
			 		"			adt.t_mews_mapper  " + 
			 		"		where  " + 
			 		"			admission_id = admission.admission_id  " + 
			 		"			and status = 'A') > 6 then 1  " + 
			 		"		else 0  " + 
			 		"	end) as \"isCriticalOrder\",  " + 
			 		"	admission.reference_number as \"referenceNumber\",  " + 
			 		"	bedCatMst.is_icu as \"isIcuu\"  " + 
			 		"from  " + 
			 		"	adt.t_admission admission  " + 
			 		"inner join patient.t_patient_registration pr on  " + 
			 		"	admission.patient_id = pr.patient_id  " + 
			 		"inner join public.m_prefix_master prefxmstr on  " + 
			 		"	prefxmstr.prefix_id = pr.prefix_id  " + 
			 		"inner join public.m_gender_master gm on  " + 
			 		"	pr.gender_id = gm.gender_id  " + 
			 		"inner join doctor.m_doctor_master doc on  " + 
			 		"	admission.doctor_id = doc.doctor_id  " + 
			 		"inner join doctor.m_speciality_master sp on  " + 
			 		"	admission.speciality_id = sp.speciality_id  " + 
			 		"inner join adt.t_admission_details adl on  " + 
			 		"	adl.admission_id = admission.admission_id  " + 
			 		" inner join adt.m_bed_master bm on  " + 
			 		"	adl.bed_id = bm.bed_id  " + 
			 		" inner join adt.m_ward_master ward on  " + 
			 		"	bm.ward_id = ward.ward_id  " + 
			 		" inner join adt.m_floor_master floor on  " + 
			 		"	ward.floor_id = floor.floor_id  " + 
			 		" inner join opd.t_patient_sponsor_details spd on  " + 
			 		"	spd.admission_id = admission.admission_id  " + 
			 		"	and spd.priority_id = 1  " + 
			 		"	and spd.status = 'A'  " + 
			 		" inner join adt.m_bed_category_master bedCatMst on  " + 
			 		"	adl.bed_category_id = bedCatMst.bed_category_id  " + 
			 		" inner join public.m_payment_entitlement_master pe on  " + 
			 		"	spd.payment_entitlement_id = pe.payment_entitlement_id  " + 
			 		" left join adt.t_discharge disc on  " + 
			 		"	admission.admission_id = disc.admission_id  " + 
			 		"	and disc.status = 'A'  " + 
			 		" left join adt.t_medico_legal medic on  " + 
			 		"	admission.admission_id = medic.admission_id" + 
			 		" where  " 
			 		+ "  admission.status = 'A' "
					+ " AND adl.status='A' "
					+ " AND adl.nursing_acceptance = 'Y' "
					+ "  AND admission.organization_id = :organizationId "
					+ "  AND admission.unit_id = :unitId "
					+ " AND admission.visit_type_id=:visitTypeId "+
			 		"	and bm.bed_number is not null  " + 
			 		"	and admission.t_patient_id is null  "
//			 		+ " AND bm.ward_id =:wardId" 																						
			 		; 
			 		 
			 		String query2 = " union   " + 
			 		" select  " + 
			 		"	admission.admission_id as \"admissionId\",  " + 
			 		"	admission.patient_id as \"patientId\",  " + 
			 		"	admission.t_patient_id as \"tPatientId\",  " + 
			 		"	admission.admission_number as \"admissionNumber\",  " + 
			 		"	admission.visit_type_id as \"visitTypeId\",  " + 
			 		"	admission.is_final_bill as \"isFinalBill\",  " + 
			 		"	admission.doctor_id as \"doctorId\",  " + 
			 		"	admission.speciality_id as \"specialityId\",  " + 
			 		"	sp.speciality_name as \"specialityName\",  " + 
			 		"	pr.uhid_number as \"UHIDNumber\",  " + 
			 		"	pr.gender_id as \"genderId\",  " + 
			 		"	gm.gender_code as \"genderCode\",  " + 
			 		"	to_char(pr.dob, 'MM/DD/YYYY') as \"dob\",  " + 
			 		"	concat(coalesce(doc.first_name, ''), ' ', coalesce(doc.middle_name, ''), ' ', coalesce(doc.last_name, '')) as \"dFirstName\",  " + 
			 		"	to_char(adl.doa, 'DD/MM/YYYY') as \"doa\",  " + 
			 		"	to_char(adl.pdd, 'DD/MM/YYYY') as \"pdd\",  " + 
			 		"	spd.payment_entitlement_id as \"paymentEntitlementId\",  " + 
			 		"	spd.contract_id as \"contractId\",  " + 
			 		"	spd.payee_id as \"payeeId\",  " + 
			 		"	pe.payment_entitlement_desc as \"paymentEntitlementDesc\",  " + 
			 		"	bedCatMst.bed_category_desc as \"bedCategoryDesc\",  " + 
			 		"	bedCatMst.billing_bed_category_id as \"billingBedCategoryId\",  " + 
			 		"	bedCatMst.bed_category_id as \"bedCategoryId\",  " + 
			 		"	bm.floor_id as \"floorId\",  " + 
			 		"	bm.bed_number as \"bedNumber\",  " + 
			 		"	to_char(adl.doa, 'HH:MI AM') as \"createdTime\",  " + 
			 		"	adl.bed_id as \"bedId\",  " + 
			 		"	adl.ward_id as \"wardId\",  " + 
			 		"	adl.room_id as \"roomId\",  " + 
			 		"	ward.ward_name as \"wardName\",  " + 
			 		"	floor.floor_name as \"floorName\",  " + 
			 		"	adl.is_short_leave as \"isShortLeave\",  " + 
			 		"	pr.patient_category_id as \"patientCategoryId\",  " + 
			 		"	admission.concent as \"concent\",  " + 
			 		"	medic.medico_legal_id as \"medicoLegalId\",  " + 
			 		"	disc.discharge_status_id as \"dischargeStatusId\",  " + 
			 		"	disc.discharge_id as \"dischargeId\",  " + 
			 		"	pr.is_vip as \"isVip\",  " + 
			 		"	pr.uuid as \"uuid\",  " + 
			 		"	prefxmstr.prefix_id as \"prefixId\" ,  " + 
			 		"	concat(extract(year from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'Y ', extract(month from AGE(CURRENT_TIMESTAMP, pr.dob)) , 'M ' , extract(day from AGE(CURRENT_TIMESTAMP, pr.dob)), 'D ') as \"ageStr\",  " + 
			 		"	prefxmstr.prefix_desc as \"prefixDesc\",  " + 
			 		"	pr.image_path as \"imagePath\" ,  " + 
			 		"	(case  " + 
			 		"		when pr.is_vip = 'Y' then coalesce(pr.alise_name, pr.first_name)  " + 
			 		"		else concat(coalesce(pr.first_name) , ' ', coalesce(pr.middle_name) , ' ', coalesce(pr.last_name))  " + 
			 		"	end) as \"pFirstName\",  " + 
			 		"	(case  " + 
			 		"		when (  " + 
			 		"		select  " + 
			 		"			total_score  " + 
			 		"		from  " + 
			 		"			adt.t_mews_mapper  " + 
			 		"		where  " + 
			 		"			admission_id = admission.admission_id  " + 
			 		"			and status = 'A') > 6 then 1  " + 
			 		"		else 0  " + 
			 		"	end) as \"isCriticalOrder\",  " + 
			 		"	admission.reference_number as \"referenceNumber\",  " + 
			 		"	bedCatMst.is_icu as \"isIcuu\"  " + 
			 		" from  " + 
			 		"	adt.t_admission admission  " + 
			 		" inner join patient.t_patient_registration pr on  " + 
			 		"	admission.patient_id = pr.patient_id  " + 
			 		" inner join public.m_prefix_master prefxmstr on  " + 
			 		"	prefxmstr.prefix_id = pr.prefix_id  " + 
			 		" inner join public.m_gender_master gm on  " + 
			 		"	pr.gender_id = gm.gender_id  " + 
			 		" left join doctor.m_doctor_master doc on  " + 
			 		"	admission.doctor_id = doc.doctor_id  " + 
			 		" left join doctor.m_speciality_master sp on  " + 
			 		"	admission.speciality_id = sp.speciality_id  " + 
			 		" inner join adt.t_admission_details adl on  " + 
			 		"	adl.admission_id = admission.admission_id  " + 
			 		" inner join adt.m_bed_master bm on  " + 
			 		"	adl.bed_id = bm.bed_id  " + 
			 		" inner join adt.m_ward_master ward on  " + 
			 		"	bm.ward_id = ward.ward_id  " + 
			 		" inner join adt.m_floor_master floor on  " + 
			 		"	ward.floor_id = floor.floor_id  " + 
			 		" inner join opd.t_patient_sponsor_details spd on  " + 
			 		"	spd.admission_id = admission.admission_id  " + 
			 		"	and spd.priority_id = 1  " + 
			 		"	and spd.status = 'A'  " + 
			 		" inner join adt.m_bed_category_master bedCatMst on  " + 
			 		"	adl.bed_category_id = bedCatMst.bed_category_id  " + 
			 		"inner join public.m_payment_entitlement_master pe on  " + 
			 		"	spd.payment_entitlement_id = pe.payment_entitlement_id  " + 
			 		"left join adt.t_discharge disc on  " + 
			 		"	admission.admission_id = disc.admission_id  " + 
			 		"	and disc.status = 'A'  " + 
			 		"left join adt.t_medico_legal medic on  " + 
			 		"	admission.admission_id = medic.admission_id" + 
			 		" where  " 
			 		+ "  admission.status = 'A' "
					+ " AND adl.status='A' "
					+ " AND adl.nursing_acceptance = 'Y' "
					+ "  AND admission.organization_id = :organizationId "
					+ "  AND admission.unit_id = :unitId "
					+ " AND admission.visit_type_id=:visitTypeId "+
			 		"	and bm.bed_number is not null  " + 
			 		"	and admission.t_patient_id is null  "
//			 		+ " AND bm.ward_id =:wardId"
			 		;
					StringBuffer s1 = new StringBuffer();
			if(admissionDto.getWardIds()!=null && !admissionDto.getWardIds().isEmpty()) {
				s1.append(" AND bm.ward_id IN (:wardIds)");

			}
//			s1.append(" order by \"isCriticalOrder\" desc ,EXTRACT(year from AGE(CURRENT_TIMESTAMP, pr.dob))>60 desc, \"admissionId\" desc ");
			String queryN = query1.concat(s1.toString())+" "+query2.concat(s1.toString());
			List<Integer> wardIds = admissionDto.getWardIds();


			Query queryList = sessionFactory.getCurrentSession().createSQLQuery(queryN)
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("visitTypeId", admissionDto.getVisitTypeId());

			// Set the wardIds parameter only if it's not null
			if (wardIds != null) {
				queryList.setParameterList("wardIds", wardIds);
			}

			List<AdmissionDto> admissionDtosList = queryList
					.setFirstResult(admissionDto.getOffset() != null ? admissionDto.getOffset() : 0)
					.setMaxResults(admissionDto.getNoOfRecordsPerPage() != null ? admissionDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(AdmissionDto.class))
					.list();

			Query queryLists = sessionFactory.getCurrentSession().createSQLQuery(queryN)
					.setParameter("organizationId", admissionDto.getOrganizationId())
					.setParameter("unitId", admissionDto.getUnitId())
					.setParameter("visitTypeId", admissionDto.getVisitTypeId());

			// Set the wardIds parameter only if it's not null
			if (wardIds != null) {
				queryLists.setParameterList("wardIds", wardIds);
			}

			List<AdmissionDto> admissionDtosLists = queryLists.
					setResultTransformer(Transformers.aliasToBean(AdmissionDto.class)).list();

			Integer count =0;
			if(admissionDtosLists!=null && admissionDtosLists.size()>0) {
				count=admissionDtosLists.size();
			}
			
			return new Response(SUCCESS, null, null, admissionDtosList, count);

		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		} 
//		return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
	}

}
