package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.billing.global.transaction.model.InsuranceMaster;

@NamedQueries({
	@NamedQuery(name="GET_DOCTOR_CONSULTATION_SERVICE_MAPPER_LIST",query="SELECT dcsm.doctorConsultationServiceMapperId as doctorConsultationServiceMapperId, "
			+ "dcsm.doctorId as doctorId, "
			+ "dcsm.specialityId as specialityId, "
			+ "spl.specialityName as specialityName, "
			+ "concat(coalesce(doc.firstName),' ', coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,  "
			+ "dcsm.newVisitServiceId as newVisitServiceId, "
			+ "nvs.serviceStandardName as newVisitServiceName, "
			+ "dcsm.newVisitRate as newVisitRate, "
			+ "dcsm.followupVisitServiceId as followupVisitServiceId, "
			+ "dcsm.followupVisitRate as followupVisitRate, "
			+ "fvs.serviceStandardName as followupVisitServiceName, "
			+ "dcsm.secondaryVisitServiceId as secondaryVisitServiceId, "
			+ "dcsm.secondaryVisitRate as secondaryVisitRate, "
			+ "svs.serviceStandardName as secondaryVisitServiceName, "
			+ " payEntMstr.paymentEntitlementDesc as paymentEntitlement ,"
			+ " dcsm.paymentEntitlementId as paymentEntitlementId, "
			+ " dcsm.status as status,	 "
			+ " comMst.companyDesc as companyName ,"
			+ " dcsm.companyId as companyId, "
			+ " insuMst.insuranceDesc as insuranceCompanyName,"
			+ " dcsm.assoCompanyId as assoCompanyId  "
			+ "FROM DoctorConsultationServiceMapper dcsm "
			+ "LEFT JOIN dcsm.doctorMaster doc "
			+ "INNER JOIN dcsm.specialityMaster spl "
			+ "INNER JOIN dcsm.newVisitService nvs "
			+ "INNER JOIN dcsm.followupVisitService fvs "
			+ "INNER JOIN dcsm.secondaryVisitService svs "
			+ "LEFT JOIN dcsm.paymentEntitlementMaster payEntMstr "
			+ "LEFT JOIN dcsm.companyMaster comMst "
			+ "LEFT JOIN dcsm.insuranceMaster insuMst "
			+ "WHERE dcsm.organizationId=:orgId "
			+ "AND dcsm.unitId=:unitId "
			+ " AND dcsm.status='A'"),
			
	
	@NamedQuery(name="GET_DOCTOR_CONSULTATION_SERVICE_MAPPER_LIST_COUNT"
			,query="SELECT count(*) "
			+ " FROM DoctorConsultationServiceMapper dcsm "
			+ " WHERE dcsm.organizationId=:orgId "
			+ " AND dcsm.unitId=:unitId "
			+ " AND dcsm.status='A' "),
	 
	@NamedQuery(name="GET_DOCTOR_CONSULTATION_SERVICE_BY_ID",query="SELECT dcsm.doctorConsultationServiceMapperId as doctorConsultationServiceMapperId, "
			+ "dcsm.doctorId as doctorId, "
			+ "spl.specialityName as specialityName, "
			+ "concat(coalesce(doc.firstName),' ', coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,  "
			+ "dcsm.specialityId as specialityId, "
			+ "dcsm.newVisitServiceId as newVisitServiceId, "
			+ "dcsm.newVisitRate as newVisitRate, "
			+ "dcsm.followupVisitServiceId as followupVisitServiceId, "
			+ "dcsm.followupVisitRate as followupVisitRate, "
			+ "dcsm.secondaryVisitServiceId as secondaryVisitServiceId ,"
			+ "dcsm.secondaryVisitRate as secondaryVisitRate ,"
			+ "nvs.serviceStandardName as newVisitServiceName, "
			+ "fvs.serviceStandardName as followupVisitServiceName, "
			+ "svs.serviceStandardName as secondaryVisitServiceName, "
			+ " payEntMstr.paymentEntitlementDesc as paymentEntitlement ,"
			+ " dcsm.paymentEntitlementId as paymentEntitlementId,"
			+ " dcsm.status as status,	 "
			+ " comMst.companyDesc as companyName ,"
			+ " dcsm.companyId as companyId, "
			+ " insuMst.insuranceDesc as insuranceCompanyName,"
			+ " dcsm.assoCompanyId as assoCompanyId  "
			+ "FROM DoctorConsultationServiceMapper dcsm "
			+ "LEFT JOIN dcsm.doctorMaster doc "
			+ "INNER JOIN dcsm.specialityMaster spl "
			+ "INNER JOIN dcsm.newVisitService nvs "
			+ "INNER JOIN dcsm.followupVisitService fvs "
			+ "INNER JOIN dcsm.secondaryVisitService svs "
			+ "LEFT JOIN dcsm.paymentEntitlementMaster payEntMstr "
			+ "LEFT JOIN dcsm.companyMaster comMst "
			+ "LEFT JOIN dcsm.insuranceMaster insuMst "
			+ "WHERE dcsm.doctorConsultationServiceMapperId=:doctorConsultationServiceMapperId "),
	
	@NamedQuery(name="CHECK_DOCTOR_CONSULTATION_EXIST_OR_NOT",query="SELECT dcsm.doctorConsultationServiceMapperId as doctorConsultationServiceMapperId "
			+ "FROM DoctorConsultationServiceMapper dcsm "
			+ "WHERE dcsm.organizationId=:orgId "
			+ "AND dcsm.unitId=:unitId "
			/*+ "AND dcsm.newVisitServiceId=:newVisitServiceId "
			+ "AND dcsm.followupVisitServiceId=:followupVisitServiceId "
			+ "AND dcsm.secondaryVisitServiceId=:secondaryVisitServiceId "*/
			+ "AND dcsm.paymentEntitlementId=:paymentEntitlementId "
			+ "AND dcsm.status='A'"),
	
	/*@NamedQuery(name="GET_DOCTOR_CONSULTATION_SERVICE_BY_SPECIALITY_AND_DOCTOR_ID",
	    query="SELECT dcsm.doctorConsultationServiceMapperId as doctorConsultationServiceMapperId, "
			+ "dcsm.newVisitServiceId as newVisitServiceId, "
			+ "dcsm.newVisitRate as newVisitRate, "
			+ "dcsm.followupVisitServiceId as followupVisitServiceId, "
			+ "dcsm.followupVisitRate as followupVisitRate, "
			+ "dcsm.secondaryVisitServiceId as secondaryVisitServiceId, "
			+ "dcsm.secondaryVisitRate as secondaryVisitRate ,"
			+ "newVisitService.specialityName as newVisitSpecialityName, "
			+ "followVisitService.specialityName as followupVisitSpecialityName, "
			+ "secondaryVisitService.specialityName as secondarySpecialityName, "
			+ "nvs.serviceStandardName as newVisitServiceName, "
			+ "fvs.serviceStandardName as followupVisitServiceName, "
			+ "svs.serviceStandardName as secondaryVisitServiceName, "
			+ "(SELECT coalesce(isRefDoctorShare,'N') FROM UnitServiceMasterDetails where serviceMasterId=dcsm.newVisitServiceId and unitId=dcsm.unitId and status='A') as newVisitReferralShare, "
			+ "(SELECT coalesce(isRefDoctorShare,'N') FROM UnitServiceMasterDetails where serviceMasterId=dcsm.followupVisitServiceId and unitId=dcsm.unitId and status='A') as followUpVisitReferralShare, "
			+ "(SELECT coalesce(isRefDoctorShare,'N') FROM UnitServiceMasterDetails where serviceMasterId=dcsm.secondaryVisitServiceId and unitId=dcsm.unitId and status='A') as secondaryVisitReferralShare, "
			+ "(SELECT coalesce(isDocReq,'N') FROM UnitServiceMasterDetails where serviceMasterId=dcsm.newVisitServiceId and unitId=dcsm.unitId and status='A') as newVisitDoctorShare, "
			+ "(SELECT coalesce(isDocReq,'N') FROM UnitServiceMasterDetails where serviceMasterId=dcsm.followupVisitServiceId and unitId=dcsm.unitId and status='A') as followUpVisitDoctorShare, "
			+ "(SELECT coalesce(isDocReq,'N') FROM UnitServiceMasterDetails where serviceMasterId=dcsm.secondaryVisitServiceId and unitId=dcsm.unitId and status='A') as secondaryVisitDoctorShare "
			+ "FROM DoctorConsultationServiceMapper dcsm "
			+ "INNER JOIN dcsm.newVisitService nvs "
			+ "INNER JOIN dcsm.followupVisitService fvs "
			+ "INNER JOIN dcsm.secondaryVisitService svs "
			+ "INNER JOIN nvs.specialityMaster newVisitService "
			+ "INNER JOIN fvs.specialityMaster followVisitService "
			+ "INNER JOIN svs.specialityMaster secondaryVisitService "
			+ "WHERE dcsm.organizationId=:orgId "
			+ " AND dcsm.unitId=:unitId "
			+ " AND dcsm.paymentEntitlementId=:paymentEntitlementId "
			+ " AND dcsm.status='A' "
			+ " AND dcsm.specialityId=:specialityId ")*/
			}	
		)

@NamedNativeQueries({
	@NamedNativeQuery(name="GET_DOCTOR_CONSULTATION_SERVICE_BY_SPECIALITY_AND_DOCTOR_ID",query = " select " 
			 + " 	doctorcons.doctor_consultation_service_mapper_id as \"doctorConsultationServiceMapperId\", " 
			 + " 	doctorcons.new_visit_service_id as \"newVisitServiceId\", " 
			 + " 	doctorcons.new_visit_rate as \"newVisitRate\", " 
			 + " 	doctorcons.followup_visit_service_id as \"followupVisitServiceId\", " 
			 + " 	doctorcons.follow_up_visit_rate as \"followupVisitRate\", " 
			 + " 	doctorcons.secondary_visit_service_id as \"secondaryVisitServiceId\", " 
			 + " 	doctorcons.secondary_visit_rate as \"secondaryVisitRate\", " 
			 + " 	speciality4.speciality_name as \"newVisitSpecialityName\", " 
			 + " 	speciality5.speciality_name as \"followupVisitSpecialityName\", " 
			 + " 	speciality.speciality_name as \"secondarySpecialityName\", " 
			 + " 	servicemas1.service_standard_name as \"newVisitServiceName\", " 
			 + " 	servicemas2.service_standard_name as \"followupVisitServiceName\", " 
			 + " 	servicemas3.service_standard_name as \"secondaryVisitServiceName\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic7_.is_ref_doctor_share, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic7_ " 
			 + " 	where " 
			 + " 		unitservic7_.service_master_id = doctorcons.new_visit_service_id " 
			 + " 		and unitservic7_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic7_.status = 'A') as \"newVisitReferralShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic8_.is_ref_doctor_share, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic8_ " 
			 + " 	where " 
			 + " 		unitservic8_.service_master_id = doctorcons.followup_visit_service_id " 
			 + " 		and unitservic8_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic8_.status = 'A') as \"followUpVisitReferralShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic9_.is_ref_doctor_share, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic9_ " 
			 + " 	where " 
			 + " 		unitservic9_.service_master_id = doctorcons.secondary_visit_service_id " 
			 + " 		and unitservic9_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic9_.status = 'A') as \"secondaryVisitReferralShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic10_.is_doc_req, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic10_ " 
			 + " 	where " 
			 + " 		unitservic10_.service_master_id = doctorcons.new_visit_service_id " 
			 + " 		and unitservic10_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic10_.status = 'A') as \"newVisitDoctorShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic11_.is_doc_req, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic11_ " 
			 + " 	where " 
			 + " 		unitservic11_.service_master_id = doctorcons.followup_visit_service_id " 
			 + " 		and unitservic11_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic11_.status = 'A') as \"followUpVisitDoctorShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic12_.is_doc_req, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic12_ " 
			 + " 	where " 
			 + " 		unitservic12_.service_master_id = doctorcons.secondary_visit_service_id " 
			 + " 		and unitservic12_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic12_.status = 'A') as \"secondaryVisitDoctorShare\", " 
			 + " 	1 as \"type\"  " 
			 + " from " 
			 + " 	doctor.m_doctor_consultation_service_mapper doctorcons " 
			 + " inner join service.m_service_master servicemas1 on " 
			 + " 	doctorcons.new_visit_service_id = servicemas1.service_master_id " 
			 + " inner join doctor.m_speciality_master speciality4 on " 
			 + " 	servicemas1.speciality_id = speciality4.speciality_id " 
			 + " inner join service.m_service_master servicemas2 on " 
			 + " 	doctorcons.followup_visit_service_id = servicemas2.service_master_id " 
			 + " inner join doctor.m_speciality_master speciality5 on " 
			 + " 	servicemas2.speciality_id = speciality5.speciality_id " 
			 + " inner join service.m_service_master servicemas3 on " 
			 + " 	doctorcons.secondary_visit_service_id = servicemas3.service_master_id " 
			 + " inner join doctor.m_speciality_master speciality on " 
			 + " 	servicemas3.speciality_id = speciality.speciality_id " 
			 + " where " 
			 + " 	doctorcons.organization_id = :orgId " 
			 + " 	and doctorcons.unit_id = :unitId " 
			 + " 	and doctorcons.payment_entitlement_id =:paymentEntitlementId " 
			 + " 	and doctorcons.status = 'A' " 
			 + " 	and doctorcons.speciality_id =:specialityId " 
			 + " 	and doctorcons.doctor_id =:doctorId " 
			 + " 	and doctorcons.company_id = 0 " 
			 + " 	and doctorcons.asso_company_id = 0 " 
			 + " union " 
			 + " select " 
			 + " 	doctorcons.doctor_consultation_service_mapper_id as \"doctorConsultationServiceMapperId\", " 
			 + " 	doctorcons.new_visit_service_id as \"newVisitServiceId\", " 
			 + " 	doctorcons.new_visit_rate as \"newVisitRate\", " 
			 + " 	doctorcons.followup_visit_service_id as \"followupVisitServiceId\", " 
			 + " 	doctorcons.follow_up_visit_rate as \"followupVisitRate\", " 
			 + " 	doctorcons.secondary_visit_service_id as \"secondaryVisitServiceId\", " 
			 + " 	doctorcons.secondary_visit_rate as \"secondaryVisitRate\", " 
			 + " 	speciality4.speciality_name as \"newVisitSpecialityName\", " 
			 + " 	speciality5.speciality_name as \"followupVisitSpecialityName\", " 
			 + " 	speciality.speciality_name as \"secondarySpecialityName\", " 
			 + " 	servicemas1.service_standard_name as \"newVisitServiceName\", " 
			 + " 	servicemas2.service_standard_name as \"followupVisitServiceName\", " 
			 + " 	servicemas3.service_standard_name as \"secondaryVisitServiceName\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic7_.is_ref_doctor_share, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic7_ " 
			 + " 	where " 
			 + " 		unitservic7_.service_master_id = doctorcons.new_visit_service_id " 
			 + " 		and unitservic7_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic7_.status = 'A') as \"newVisitReferralShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic8_.is_ref_doctor_share, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic8_ " 
			 + " 	where " 
			 + " 		unitservic8_.service_master_id = doctorcons.followup_visit_service_id " 
			 + " 		and unitservic8_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic8_.status = 'A') as \"followUpVisitReferralShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic9_.is_ref_doctor_share, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic9_ " 
			 + " 	where " 
			 + " 		unitservic9_.service_master_id = doctorcons.secondary_visit_service_id " 
			 + " 		and unitservic9_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic9_.status = 'A') as \"secondaryVisitReferralShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic10_.is_doc_req, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic10_ " 
			 + " 	where " 
			 + " 		unitservic10_.service_master_id = doctorcons.new_visit_service_id " 
			 + " 		and unitservic10_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic10_.status = 'A') as \"newVisitDoctorShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic11_.is_doc_req, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic11_ " 
			 + " 	where " 
			 + " 		unitservic11_.service_master_id = doctorcons.followup_visit_service_id " 
			 + " 		and unitservic11_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic11_.status = 'A') as \"followUpVisitDoctorShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic12_.is_doc_req, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic12_ " 
			 + " 	where " 
			 + " 		unitservic12_.service_master_id = doctorcons.secondary_visit_service_id " 
			 + " 		and unitservic12_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic12_.status = 'A') as \"secondaryVisitDoctorShare\", " 
			 + " 	2 as \"type\" 	 " 
			 + " from " 
			 + " 	doctor.m_doctor_consultation_service_mapper doctorcons " 
			 + " inner join service.m_service_master servicemas1 on " 
			 + " 	doctorcons.new_visit_service_id = servicemas1.service_master_id " 
			 + " inner join doctor.m_speciality_master speciality4 on " 
			 + " 	servicemas1.speciality_id = speciality4.speciality_id " 
			 + " inner join service.m_service_master servicemas2 on " 
			 + " 	doctorcons.followup_visit_service_id = servicemas2.service_master_id " 
			 + " inner join doctor.m_speciality_master speciality5 on " 
			 + " 	servicemas2.speciality_id = speciality5.speciality_id " 
			 + " inner join service.m_service_master servicemas3 on " 
			 + " 	doctorcons.secondary_visit_service_id = servicemas3.service_master_id " 
			 + " inner join doctor.m_speciality_master speciality on " 
			 + " 	servicemas3.speciality_id = speciality.speciality_id " 
			 + " where " 
			 + " 	doctorcons.organization_id = :orgId " 
			 + " 	and doctorcons.unit_id = :unitId " 
			 + " 	and doctorcons.payment_entitlement_id = 2 " 
			 + " 	and doctorcons.status = 'A' " 
			 + " 	and doctorcons.speciality_id =:specialityId " 
			 + " 	and doctorcons.doctor_id = :doctorId " 
			 + " 	and doctorcons.company_id =:companyId " 
			 + " 	and doctorcons.asso_company_id =:assoCompanyId " 
			 + " union " 
			 + " select " 
			 + " 	doctorcons.doctor_consultation_service_mapper_id as \"doctorConsultationServiceMapperId\", " 
			 + " 	doctorcons.new_visit_service_id as \"newVisitServiceId\", " 
			 + " 	doctorcons.new_visit_rate as \"newVisitRate\", " 
			 + " 	doctorcons.followup_visit_service_id as \"followupVisitServiceId\", " 
			 + " 	doctorcons.follow_up_visit_rate as \"followupVisitRate\", " 
			 + " 	doctorcons.secondary_visit_service_id as \"secondaryVisitServiceId\", " 
			 + " 	doctorcons.secondary_visit_rate as \"secondaryVisitRate\", " 
			 + " 	speciality4.speciality_name as \"newVisitSpecialityName\", " 
			 + " 	speciality5.speciality_name as \"followupVisitSpecialityName\", " 
			 + " 	speciality.speciality_name as \"secondarySpecialityName\", " 
			 + " 	servicemas1.service_standard_name as \"newVisitServiceName\", " 
			 + " 	servicemas2.service_standard_name as \"followupVisitServiceName\", " 
			 + " 	servicemas3.service_standard_name as \"secondaryVisitServiceName\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic7_.is_ref_doctor_share, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic7_ " 
			 + " 	where " 
			 + " 		unitservic7_.service_master_id = doctorcons.new_visit_service_id " 
			 + " 		and unitservic7_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic7_.status = 'A') as \"newVisitReferralShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic8_.is_ref_doctor_share, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic8_ " 
			 + " 	where " 
			 + " 		unitservic8_.service_master_id = doctorcons.followup_visit_service_id " 
			 + " 		and unitservic8_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic8_.status = 'A') as \"followUpVisitReferralShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic9_.is_ref_doctor_share, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic9_ " 
			 + " 	where " 
			 + " 		unitservic9_.service_master_id = doctorcons.secondary_visit_service_id " 
			 + " 		and unitservic9_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic9_.status = 'A') as \"secondaryVisitReferralShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic10_.is_doc_req, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic10_ " 
			 + " 	where " 
			 + " 		unitservic10_.service_master_id = doctorcons.new_visit_service_id " 
			 + " 		and unitservic10_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic10_.status = 'A') as \"newVisitDoctorShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic11_.is_doc_req, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic11_ " 
			 + " 	where " 
			 + " 		unitservic11_.service_master_id = doctorcons.followup_visit_service_id " 
			 + " 		and unitservic11_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic11_.status = 'A') as \"followUpVisitDoctorShare\", " 
			 + " 	( " 
			 + " 	select " 
			 + " 		coalesce(unitservic12_.is_doc_req, 'N') " 
			 + " 	from " 
			 + " 		service.t_unit_service_master_details unitservic12_ " 
			 + " 	where " 
			 + " 		unitservic12_.service_master_id = doctorcons.secondary_visit_service_id " 
			 + " 		and unitservic12_.unit_id = doctorcons.unit_id " 
			 + " 		and unitservic12_.status = 'A') as \"secondaryVisitDoctorShare\", " 
			 + " 	3 as \"type\"	 " 
			 + " from " 
			 + " 	doctor.m_doctor_consultation_service_mapper doctorcons " 
			 + " inner join service.m_service_master servicemas1 on " 
			 + " 	doctorcons.new_visit_service_id = servicemas1.service_master_id " 
			 + " inner join doctor.m_speciality_master speciality4 on " 
			 + " 	servicemas1.speciality_id = speciality4.speciality_id " 
			 + " inner join service.m_service_master servicemas2 on " 
			 + " 	doctorcons.followup_visit_service_id = servicemas2.service_master_id " 
			 + " inner join doctor.m_speciality_master speciality5 on " 
			 + " 	servicemas2.speciality_id = speciality5.speciality_id " 
			 + " inner join service.m_service_master servicemas3 on " 
			 + " 	doctorcons.secondary_visit_service_id = servicemas3.service_master_id " 
			 + " inner join doctor.m_speciality_master speciality on " 
			 + " 	servicemas3.speciality_id = speciality.speciality_id " 
			 + " where " 
			 + " 	doctorcons.organization_id = :orgId " 
			 + " 	and doctorcons.unit_id = :unitId " 
			 + " 	and doctorcons.payment_entitlement_id = 3 " 
			 + " 	and doctorcons.status = 'A' " 
			 + " 	and doctorcons.speciality_id =:specialityId " 
			 + " 	and doctorcons.doctor_id = :doctorId " 
			 + " 	and doctorcons.company_id = :companyId " 
			 + " 	and doctorcons.asso_company_id = 0 " 
			 + " order by \"type\"  " 
			 + " 	desc limit 1 ")
			}	
)


@Entity
@Table(name="m_doctor_consultation_service_mapper",schema="doctor")
@SequenceGenerator(name="doctor_consultation_service_mapper_seq",sequenceName="doctor.doctor_consultation_service_mapper_seq")
public class DoctorConsultationServiceMapper {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="doctor_consultation_service_mapper_seq")
	@Column(name="doctor_consultation_service_mapper_id")
	private Integer doctorConsultationServiceMapperId;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="speciality_id")
	private Integer specialityId;
	
	@Column(name="doctor_id")
	private Integer doctorId;
	
	@Column(name="new_visit_service_id")
	private Integer newVisitServiceId;
	
	@Column(name="followup_visit_service_id")
	private Integer followupVisitServiceId;
	
	@Column(name="secondary_visit_service_id")
	private Integer secondaryVisitServiceId;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="payment_entitlement_id")
	private Integer paymentEntitlementId;
	
	@Column(name="new_visit_rate")
	private Double newVisitRate;
	
	@Column(name="follow_up_visit_rate")
	private Double followupVisitRate;
	
	@Column(name="secondary_visit_rate")
	private Double secondaryVisitRate;
	
	@Column(name="company_id")
	private Integer companyId;
	
	@Column(name="asso_company_id")
	private Integer assoCompanyId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "doctor_id", insertable = false, updatable = false)
	private DoctorMaster doctorMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "new_visit_service_id", insertable = false, updatable = false)
	private ServiceMaster newVisitService;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "followup_visit_service_id", insertable = false, updatable = false)
	private ServiceMaster followupVisitService;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "secondary_visit_service_id", insertable = false, updatable = false)
	private ServiceMaster secondaryVisitService;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "speciality_id", insertable = false, updatable = false)
	private SpecialityMaster specialityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "payment_entitlement_id", insertable = false, nullable = false, updatable = false)
	private PaymentEntitlementMaster paymentEntitlementMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id", insertable = false, nullable = false, updatable = false)
	private CompanyMaster companyMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asso_company_id", insertable = false, nullable = false, updatable = false)
	private InsuranceMaster insuranceMaster;
	
	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getAssoCompanyId() {
		return assoCompanyId;
	}

	public void setAssoCompanyId(Integer assoCompanyId) {
		this.assoCompanyId = assoCompanyId;
	}

	public Integer getSpecialityId() {
		return specialityId;
	}

	public void setSpecialityId(Integer specialityId) {
		this.specialityId = specialityId;
	}

	public Integer getDoctorConsultationServiceMapperId() {
		return doctorConsultationServiceMapperId;
	}

	public void setDoctorConsultationServiceMapperId(Integer doctorConsultationServiceMapperId) {
		this.doctorConsultationServiceMapperId = doctorConsultationServiceMapperId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Integer doctorId) {
		this.doctorId = doctorId;
	}

	public Integer getNewVisitServiceId() {
		return newVisitServiceId;
	}

	public void setNewVisitServiceId(Integer newVisitServiceId) {
		this.newVisitServiceId = newVisitServiceId;
	}

	public Integer getFollowupVisitServiceId() {
		return followupVisitServiceId;
	}

	public void setFollowupVisitServiceId(Integer followupVisitServiceId) {
		this.followupVisitServiceId = followupVisitServiceId;
	}

	public Integer getSecondaryVisitServiceId() {
		return secondaryVisitServiceId;
	}

	public void setSecondaryVisitServiceId(Integer secondaryVisitServiceId) {
		this.secondaryVisitServiceId = secondaryVisitServiceId;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getPaymentEntitlementId() {
		return paymentEntitlementId;
	}

	public void setPaymentEntitlementId(Integer paymentEntitlementId) {
		this.paymentEntitlementId = paymentEntitlementId;
	}

	public Double getNewVisitRate() {
		return newVisitRate;
	}

	public void setNewVisitRate(Double newVisitRate) {
		this.newVisitRate = newVisitRate;
	}

	public Double getFollowupVisitRate() {
		return followupVisitRate;
	}

	public void setFollowupVisitRate(Double followupVisitRate) {
		this.followupVisitRate = followupVisitRate;
	}

	public Double getSecondaryVisitRate() {
		return secondaryVisitRate;
	}

	public void setSecondaryVisitRate(Double secondaryVisitRate) {
		this.secondaryVisitRate = secondaryVisitRate;
	}
	
	
}


/*CREATE TABLE doctor.m_doctor_consultation_service_mapper
(
  doctor_consultation_service_mapper_id integer NOT NULL DEFAULT nextval('doctor.doctor_consultation_service_mapper_seq'::regclass),
  unit_id integer,
  organisation_id integer,
  doctor_id integer,
  new_visit_service_id integer,
  followup_visit_service_id integer,
  secondary_visit_service_id integer,
  status character(1) DEFAULT 'A'::"char",
  created_date timestamp without time zone,
  created_by integer,
  updated_date timestamp without time zone DEFAULT now(),
  updated_by integer,
  CONSTRAINT pk_doctor_consultation_service_mapper_id PRIMARY KEY (doctor_consultation_service_mapper_id),
  CONSTRAINT m_doctor_consultation_service_m_secondary_visit_service_id_fkey FOREIGN KEY (secondary_visit_service_id)
      REFERENCES service.m_service_master (service_master_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT m_doctor_consultation_service_ma_followup_visit_service_id_fkey FOREIGN KEY (followup_visit_service_id)
      REFERENCES service.m_service_master (service_master_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT m_doctor_consultation_service_mapper_doctor_id_fkey FOREIGN KEY (doctor_id)
      REFERENCES doctor.m_doctor_master (doctor_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT m_doctor_consultation_service_mapper_new_visit_service_id_fkey FOREIGN KEY (new_visit_service_id)
      REFERENCES service.m_service_master (service_master_id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)*/