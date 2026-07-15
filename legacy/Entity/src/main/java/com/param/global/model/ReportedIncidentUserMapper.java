package com.param.global.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;



@NamedNativeQueries({ 
		@NamedNativeQuery(name = "GET_REPORTED_INCIDENT_MAPPER_LIST", query = "  select"
				+ "	distinct(inciRepMppr.incident_reporting_mapper_id) as \"incidentReportingMapperId\"," + "	to_char("
				+ "		inciRepMppr.incident_reporting_date," + "		'DD/MM/YYYY'"
				+ "	) as \"incidentReportingDate\"," + "	inciRepMppr.patient_id as \"patientId\","
				+ "	inciRepMppr.incident_reporting_number as \"incidentReportingNumber\","
				+ "	inciRepMppr.incident_remark as \"incidentRemark\","
				+ "	inciRepMppr.speciality_id as \"specialityId\","
				+ "	inciRepMppr.incident_type_id as \"incidentTypeId\","
				+ "	inciRepMppr.incident_category_id as \"incidentCategoryId\","
				+ "	inciRepMppr.incident_report_id as \"incidentReportId\","
				+ " inciRepMppr.conversation_id as \"conversationId\","
				+ " inciRepMppr.encounter_id as \"encounterId\","
				+ " inciRepMppr.admission_id as \"admissionId\","
//				+ "	inciRepMppr.reporting_template_id as \"reportingTemplateId\","
//				+ "	inciRepMppr.reporting_template_obj as \"reportingTemplateObj\","
//				+ "	reptUserInc.reported_template_obj as \"reportedTemplateObj\","
				+ "	incRepMst.incident_report_desc as \"incidentReportDesc\","
				+ "	templatema5_.template_format_id as \"templateFormatId\","
				+ " reptInc.reported_inc_mppr_id as \"reporteIncMpprId\", "
				+ " reptInc.is_submit as \"isSubmit\", "
				+ "	spl.speciality_name as \"specialityName\","
				+ "	incCatMst.incident_category_desc as \"incidentCategoryDesc\","
				+ "	patient.uhid_number as \"uhidNumber\"," + "	(" + "		coalesce(" + "			patient.first_name,"
				+ "			''" + "		)|| ' ' || coalesce(" + "			patient.middle_name," + "			''"
				+ "		)|| ' ' || coalesce(" + "			patient.last_name," + "			''" + "		)"
				+ "	) as \"patientName\"," + "	inciRepMppr.status as \"status\"" + " from"
				+ "	public.t_incident_reporting_mapper inciRepMppr"
				+ " left outer join patient.t_patient_registration patient on"
				+ "	inciRepMppr.patient_id = patient.patient_id" + " inner join doctor.m_speciality_master spl on"
				+ "	inciRepMppr.speciality_id = spl.speciality_id"
				+ " inner join public.m_incident_category_master incCatMst on"
				+ "	inciRepMppr.incident_category_id = incCatMst.incident_category_id"
				+ " inner join public.m_incident_report_master incRepMst on"
				+ "	inciRepMppr.incident_report_id = incRepMst.incident_report_id"
				+ " inner join public.t_template_master templatema5_ on"
				+ "	inciRepMppr.reporting_template_id = templatema5_.template_id" + " " + "	and("
				+ "		templatema5_.status = 'A'" + "	)" + " left join public.t_reported_incident_mapper reptInc on"
//				+ "	incRepMst.incident_report_id = reptInc.incident_report_id"
                + " inciRepMppr.incident_reporting_mapper_id = reptInc.incident_reporting_mapper_id "
				+ " LEFT join public.t_reported_incident_user_mapper reptUserInc on"
				+ "	reptInc.reported_inc_mppr_id = reptUserInc.reported_inc_mppr_id and( " + 
				"		reptUserInc.status = 'A' " + 
				"	)" 
				+ " where"
				+ "	inciRepMppr.organization_id =:organizationId " 
				+ "	and inciRepMppr.unit_id =:unitId "
				+ " and incRepMst.incident_report_id in "
				+ " ("
				+ " select  " + 
				"			distinct(irm.incident_report_id)  " + 
				"		from  " + 
				"			 public.t_incident_reporting_mapper irm   " + 
				"			  " + 
				"		left join public.t_reported_incident_mapper rip on rip.incident_report_id =irm.incident_report_id  " + 
				"		left join public.t_reported_incident_user_mapper umr on umr.reported_inc_mppr_id =rip.reported_inc_mppr_id  " + 
				"		where  " + 
				"			(umr.user_id =:userId or irm.user_id =:userId or irm.created_by=:userId) " + 
				"			and irm.status='A' "
//				+ " select " + 
//				"			distinct(im.incident_reporting_mapper_id) " + 
//				"		from " + 
//				"			public.t_reported_incident_user_mapper umr " + 
//				"		inner join public.t_reported_incident_mapper im on im.reported_inc_mppr_id=umr.reported_inc_mppr_id " + 
//				"		where " + 
//				"			umr.user_id =:userId " + 
//				"			and umr.status='A' "
				+ ")"
//				+ " (select incident_report_id from public.t_reported_incident_user_mapper where user_id=:userId)" 
				+ "	and("
				+ "		to_char(" 
				+ "			inciRepMppr.created_date," 
				+ "			'YYYY-MM-DD'"
				+ "		) between :fromDate and :toDate" 
				+ "	)" + "	and(" + "		:specialityId= 0"
				+ " or inciRepMppr.speciality_id =:specialityId" 
				+ " )" + " and(" + " :incidentTypeId= 0"
				+ " or inciRepMppr.incident_type_id =:incidentTypeId" + " )"),
	
	
	@NamedNativeQuery(name = "GET_REPORTED_INCIDENT_MAPPER_BY_ID", query = " select " + 
			"	distinct(inciRepMppr.incident_reporting_mapper_id) as \"incidentReportingMapperId\"," + 
			"	to_char(" + 
			"		inciRepMppr.incident_reporting_date," + 
			"		'DD/MM/YYYY'" + 
			"	) as \"incidentReportingDate\"," + 
			"	inciRepMppr.patient_id as \"patientId\"," + 
			"	inciRepMppr.incident_reporting_number as \"incidentReportingNumber\"," + 
			"	inciRepMppr.incident_remark as \"incidentRemark\"," + 
			"	inciRepMppr.speciality_id as \"specialityId\"," + 
			"	inciRepMppr.incident_type_id as \"incidentTypeId\"," + 
			"	inciRepMppr.incident_category_id as \"incidentCategoryId\"," + 
			"	inciRepMppr.incident_report_id as \"incidentReportId\"," + 
			"	inciRepMppr.reporting_template_id as \"reportingTemplateId\"," + 
			"	inciRepMppr.reporting_template_obj as \"reportingTemplateObj\"," + 
			"	reptUserInc.reported_template_obj as \"reportedTemplateObj\"," + 
			"	reptInc.resolution_details_type_id as \"resolutionDetailsTypeId\"," + 
			"	reptInc.is_submit as \"isSubmit\"," + "	reptInc.parent_user_id as \"parentUserId\"," + 
			"	reptUserInc.reported_template_id as \"reportedTemplateId\"," + 
			"	reptInc.reported_inc_mppr_id as \"reportedIncMpprId\"," + 
			"	incRepMst.incident_report_desc as \"incidentReportDesc\"," + 
			"	templatema5_.template_format_id as \"templateFormatId\"," + 
			"	spl.speciality_name as \"specialityName\"," + 
			"	incCatMst.incident_category_desc as \"incidentCategoryDesc\"," + 
			"	patient.uhid_number as \"uhidNumber\"," + 
			"	um.user_name as \"userName\"," + 
			"	(" + 
			"		coalesce(" + 
			"			patient.first_name," + 
			"			''" + 
			"		)|| ' ' || coalesce(" + 
			"			patient.middle_name," + 
			"			''" + 
			"		)|| ' ' || coalesce(" + 
			"			patient.last_name," + 
			"			''" + 
			"		)" + 
			"	) as \"patientName\"," + 
			"	inciRepMppr.status as \"status\"" + 
			" from" + 
			"	public.t_incident_reporting_mapper inciRepMppr" + 
			" left outer join patient.t_patient_registration patient on" + 
			"	inciRepMppr.patient_id = patient.patient_id" + 
			" inner join doctor.m_speciality_master spl on" + 
			"	inciRepMppr.speciality_id = spl.speciality_id" + 
			" inner join public.m_incident_category_master incCatMst on" + 
			"	inciRepMppr.incident_category_id = incCatMst.incident_category_id" + 
			" inner join public.m_incident_report_master incRepMst on" + 
			"	inciRepMppr.incident_report_id = incRepMst.incident_report_id" + 
			" inner join public.t_template_master templatema5_ on" + 
			"	inciRepMppr.reporting_template_id = templatema5_.template_id" + 
			"	and(" + 
			"		templatema5_.status = 'A'" + 
			"	)" + 
			" inner join public.t_reported_incident_mapper reptInc on" + 
			"	incRepMst.incident_report_id = reptInc.incident_report_id " + 
			" inner join public.t_reported_incident_user_mapper reptUserInc on" + 
			"	reptInc.reported_inc_mppr_id = reptUserInc.reported_inc_mppr_id  and( " + 
			"	reptUserInc.status = 'A'" + 
			"			) " + 
			" inner join public.m_user_master um on um.user_id = inciRepMppr.user_id " + 
			" where reptInc.reported_inc_mppr_id=:reportedIncMpprId" + 
			" and reptInc.organization_id =:organizationId" + 
			" and reptInc.unit_id=:unitId"
//			" and inciRepMppr.patient_id=:patientId"
			),
	
	
	@NamedNativeQuery(name = "GET_REPORTED_INCIDENT_MAPPER_FOR_ROLE_BY_ID", 
	query = "  select repRoleMppr.role_id as \"roleId\",  repRoleMppr.reported_inc_mppr_id as \"reportedIncMpprId\", "
			+ "repRoleMppr.parent_rep_inc_user_id as\"parentRepIncUserId\", "
			+ "repRoleMppr.is_submit as\"isSubmit\", "
			+ "repRoleMppr.rep_inc_role_mppr_id as\"repIncRoleMpprId\" "
			+ " from public.t_reported_incident_role_mapper repRoleMppr " + 
			" where repRoleMppr.reported_inc_mppr_id=:reportedIncMpprId" + 
			" and repRoleMppr.organization_id=:organizationId" + 
			" and repRoleMppr.unit_id=:unitId"+
			" and repRoleMppr.status='A'"),
	
	
	@NamedNativeQuery(name = "GET_REPORTED_NABH_MAPPER_FOR_ROLE_BY_ID", 
	query = "  select repRoleMppr.role_id as \"roleId\",  repRoleMppr.reported_nabh_mppr_id as \"reportedNabhMpprId\", "
			+ "repRoleMppr.parent_rep_inc_user_id as\"parentRepIncUserId\", "
			+ "repRoleMppr.is_submit as\"isSubmit\", "
			+ "repRoleMppr.rep_inc_role_mppr_id as\"repIncRoleMpprId\" "
			+ " from public.t_reported_incident_role_mapper repRoleMppr " + 
			" where repRoleMppr.reported_nabh_mppr_id=:reportedNabhMpprId" + 
			" and repRoleMppr.organization_id=:organizationId" + 
			" and repRoleMppr.unit_id=:unitId"+
			" and repRoleMppr.status='A'"),
	
	@NamedNativeQuery(name = "GET_REPORTED_NABH_MAPPER_FOR_USER_BY_ID",
	query = " select repUserMppr.role_id as \"roleId\" , repUserMppr.reported_nabh_mppr_id as \"reportedNabhMpprId\" , "
			+ "repUserMppr.rep_inc_user_mppr_id as \"repIncUserMpprId\" , "
			+ " repUserMppr.user_id as \"userId\" , "
			+ " repUserMppr.reported_template_id as  \"templateId\" , "
			+ " repUserMppr.conversation_id as \"conversationId\" , "
	+ " concat(coalesce(mem.first_name,'') ,' ', coalesce(mem.middle_name,'') ,' ', coalesce(mem.last_name,'')) as \"userName\" ," 
	+ "repUserMppr.parent_rep_inc_user_id as\"parentRepIncUserId\", "
	+ "repUserMppr.is_submit as\"isSubmit\", "
	+ "repUserMppr.resolution_details_type_id as\"resolutionDetailsTypeId\", "+
	" repUserMppr.reported_template_obj as \"reportedTemplateObj\" " +
	" from public.t_reported_incident_user_mapper repUserMppr "
	+ " inner join public.m_user_master um on um.user_id =repUserMppr.user_id "
	+ " inner join public.m_employee_master mem on mem.user_id =repUserMppr.user_id " +
	" where repUserMppr.reported_nabh_mppr_id=:reportedNabhMpprId " +
	" and repUserMppr.role_id=:roleId " +
	" and repUserMppr.organization_id=:organizationId " +
	" and repUserMppr.unit_id=:unitId "+
    " and repUserMppr.status='A'"+
    " union select repUserMppr.role_id as \"roleId\" , repUserMppr.reported_nabh_mppr_id as \"reportedNabhMpprId\" , "
                + "repUserMppr.rep_inc_user_mppr_id as \"repIncUserMpprId\" , "
                + " repUserMppr.user_id as \"userId\" , "
                + " repUserMppr.reported_template_id as  \"templateId\" , "
                + " repUserMppr.conversation_id as \"conversationId\" , "
        + " concat(coalesce(mem.first_name,'') ,' ', coalesce(mem.middle_name,'') ,' ', coalesce(mem.last_name,'')) as \"userName\" ," 
        + "repUserMppr.parent_rep_inc_user_id as\"parentRepIncUserId\", "
        + "repUserMppr.is_submit as\"isSubmit\", "
        + "repUserMppr.resolution_details_type_id as\"resolutionDetailsTypeId\", "+
        " repUserMppr.reported_template_obj as \"reportedTemplateObj\" " +
        " from public.t_reported_incident_user_mapper repUserMppr "
        + " inner join public.m_user_master um on um.user_id =repUserMppr.user_id "
        + " inner join doctor.m_doctor_master mem on mem.user_id =repUserMppr.user_id " +
        " where repUserMppr.reported_nabh_mppr_id=:reportedNabhMpprId " +
        " and repUserMppr.role_id=:roleId " +
        " and repUserMppr.organization_id=:organizationId " +
        " and repUserMppr.unit_id=:unitId "+
        " and repUserMppr.status='A'"),
	
	@NamedNativeQuery(name = "GET_REPORTED_INCIDENT_MAPPER_FOR_USER_BY_ID",
	query = " select repUserMppr.role_id as \"roleId\" , repUserMppr.reported_inc_mppr_id as \"reportedIncMpprId\" , "
			+ "repUserMppr.rep_inc_user_mppr_id as \"repIncUserMpprId\" , "
			+ " repUserMppr.user_id as \"userId\" , "
			+ " repUserMppr.reported_template_id as  \"templateId\" , "
			+ " repUserMppr.conversation_id as \"conversationId\" , "
	//+ " repUserMppr.user_id as \"userId\" ,um.user_name as \"userName\" ," 
	+ " concat(coalesce(mem.first_name,'') ,' ', coalesce(mem.middle_name,'') ,' ', coalesce(mem.last_name,'')) as \"userName\" ," 
	+ "repUserMppr.parent_rep_inc_user_id as\"parentRepIncUserId\", "
	+ "repUserMppr.is_submit as\"isSubmit\", "
	+ "repUserMppr.resolution_details_type_id as\"resolutionDetailsTypeId\", "+
	" repUserMppr.reported_template_obj as \"reportedTemplateObj\" " +
	" from public.t_reported_incident_user_mapper repUserMppr "
	+ " inner join public.m_user_master um on um.user_id =repUserMppr.user_id "
	+ " inner join public.m_employee_master mem on mem.user_id =repUserMppr.user_id " +
	" where repUserMppr.reported_inc_mppr_id=:reportedIncMpprId " +
	" and repUserMppr.role_id=:roleId " +
	" and repUserMppr.organization_id=:organizationId " +
	" and repUserMppr.unit_id=:unitId "+
			" and repUserMppr.status='A'"+
			" union select repUserMppr.role_id as \"roleId\" , repUserMppr.reported_inc_mppr_id as \"reportedIncMpprId\" , "
			+ "repUserMppr.rep_inc_user_mppr_id as \"repIncUserMpprId\" , "
			+ " repUserMppr.user_id as \"userId\" , "
			+ " repUserMppr.reported_template_id as  \"templateId\" , "
			+ " repUserMppr.conversation_id as \"conversationId\" , "
			+ " concat(coalesce(mem.first_name,'') ,' ', coalesce(mem.middle_name,'') ,' ', coalesce(mem.last_name,'')) as \"userName\" ,"
			+ "repUserMppr.parent_rep_inc_user_id as\"parentRepIncUserId\", "
			+ "repUserMppr.is_submit as\"isSubmit\", "
			+ "repUserMppr.resolution_details_type_id as\"resolutionDetailsTypeId\", "
			+ " repUserMppr.reported_template_obj as \"reportedTemplateObj\" "
			+ " from public.t_reported_incident_user_mapper repUserMppr "
			+ " inner join public.m_user_master um on um.user_id =repUserMppr.user_id "
			+ " inner join doctor.m_doctor_master mem on mem.user_id =repUserMppr.user_id "
			+ " where repUserMppr.reported_inc_mppr_id=:reportedIncMpprId "
			+ " and repUserMppr.role_id=:roleId " + " and repUserMppr.organization_id=:organizationId "
			+ " and repUserMppr.unit_id=:unitId " + " and repUserMppr.status='A'"),
	
	@NamedNativeQuery(name = "GET_TEMPLATE_BY_REPORTED_INCIDENT_ID",
	query = " select inciRepMppr.reporting_template_obj as \"reportingTemplateObj\" ,"
			+ " inciRepMppr.incident_remark as \"incidentRemark\" from " + 
			"	public.t_incident_reporting_mapper inciRepMppr " + 
			"	where " + 
			"	inciRepMppr.organization_id= :organizationId " + 
			"	and inciRepMppr.unit_id = :unitId " + 
			"	and inciRepMppr.incident_reporting_mapper_id = :incidentReportingMapperId"),
	

	
	@NamedNativeQuery(name = "GET_REPORTED_NABH_MAPPER_LIST", query = "  select"
			+ "	distinct(inciRepMppr.nabh_reporting_mapper_id) as \"nabhReportingMapperId\"," + "	to_char("
			+ "		inciRepMppr.nabh_reporting_date," + "		'DD/MM/YYYY'"
			+ "	) as \"nabhReportingDate\"," + "	inciRepMppr.patient_id as \"patientId\","
			+ "	inciRepMppr.nabh_reporting_number as \"nabhReportingNumber\","
			+ "	inciRepMppr.nabh_remark as \"nabhRemark\","
			+ "	inciRepMppr.speciality_id as \"specialityId\","
			+ "	inciRepMppr.nabh_type_id as \"nabhTypeId\","
			+ "	inciRepMppr.incident_category_id as \"incidentCategoryId\","
			+ "	inciRepMppr.nabh_process_id as \"nabhProcessId\","
			+ " inciRepMppr.conversation_id as \"conversationId\","
			+ " inciRepMppr.encounter_id as \"encounterId\","
			+ " inciRepMppr.admission_id as \"admissionId\","
//			+ "	inciRepMppr.reporting_template_id as \"reportingTemplateId\","
//			+ "	inciRepMppr.reporting_template_obj as \"reportingTemplateObj\","
//			+ "	reptUserInc.reported_template_obj as \"reportedTemplateObj\","
			+ "	incRepMst.nabh_process_desc as \"nabhProcessDesc\","  
			+ "	incRepMst.is_capa_off as \"isCapaOff\","
			+ "	templatema5_.template_format_id as \"templateFormatId\","
			+ " reptInc.reported_nabh_mppr_id as \"reportedNabhMpprId\", "
			+ " reptInc.is_submit as \"isSubmit\", "
			+ "	spl.speciality_name as \"specialityName\","
			+ "	incCatMst.incident_category_desc as \"incidentCategoryDesc\","
			+ "	patient.uhid_number as \"uhidNumber\"," + "	(" + "		coalesce(" + "			patient.first_name,"
			+ "			''" + "		)|| ' ' || coalesce(" + "			patient.middle_name," + "			''"
			+ "		)|| ' ' || coalesce(" + "			patient.last_name," + "			''" + "		)"
			+ "	) as \"patientName\"," + "	inciRepMppr.status as \"status\"" + " from"
			+ "	public.t_nabh_reporting_mapper inciRepMppr"
			+ " left outer join patient.t_patient_registration patient on"
			+ "	inciRepMppr.patient_id = patient.patient_id" + " inner join doctor.m_speciality_master spl on"
			+ "	inciRepMppr.speciality_id = spl.speciality_id"
			+ " inner join public.m_incident_category_master incCatMst on"
			+ "	inciRepMppr.incident_category_id = incCatMst.incident_category_id"
			+ " inner join public.m_nabh_process_master incRepMst on"
			+ "	inciRepMppr.nabh_process_id = incRepMst.nabh_process_id"
			+ " inner join public.t_template_master templatema5_ on"
			+ "	inciRepMppr.reporting_template_id = templatema5_.template_id" + " " + "	and("
			+ "		templatema5_.status = 'A'" + "	)" + " left join public.t_reported_nabh_mapper reptInc on"
//			+ "	incRepMst.incident_report_id = reptInc.incident_report_id"
            + " inciRepMppr.nabh_reporting_mapper_id = reptInc.nabh_reporting_mapper_id "
			+ " LEFT join public.t_reported_incident_user_mapper reptUserInc on"
			+ "	reptInc.reported_nabh_mppr_id = reptUserInc.reported_nabh_mppr_id and( " + 
			"		reptUserInc.status = 'A' " + 
			"	)" 
			+ " where"
			+ "	inciRepMppr.organization_id =:organizationId " 
			+ "	and inciRepMppr.unit_id =:unitId "
			+ " and incRepMst.is_capa_off ='Y'"
			+ " and incRepMst.nabh_process_id in "
			+ " ("
			+ " select  " + 
			"			distinct(irm.nabh_process_id)  " + 
			"		from  " + 
			"			 public.t_nabh_reporting_mapper irm   " + 
			"			  " + 
			"		left join public.t_reported_nabh_mapper rip on rip.nabh_process_id =irm.nabh_process_id  " + 
			"		left join public.t_reported_incident_user_mapper umr on umr.reported_nabh_mppr_id =rip.reported_nabh_mppr_id  " + 
			"		where  " + 
			"			(umr.user_id =:userId or irm.user_id =:userId or irm.created_by=:userId) " + 
			"			and irm.status='A' "
//			+ " select " + 
//			"			distinct(im.incident_reporting_mapper_id) " + 
//			"		from " + 
//			"			public.t_reported_incident_user_mapper umr " + 
//			"		inner join public.t_reported_nabh_mapper im on im.reported_nabh_mppr_id=umr.reported_inc_mppr_id " + 
//			"		where " + 
//			"			umr.user_id =:userId " + 
//			"			and umr.status='A' "
			+ ")"
//			+ " (select incident_report_id from public.t_reported_incident_user_mapper where user_id=:userId)" 
			+ "	and("
			+ "		to_char(" 
			+ "			inciRepMppr.created_date," 
			+ "			'YYYY-MM-DD'"
			+ "		) between :fromDate and :toDate" 
			+ "	)" + "	and(" + "		:specialityId= 0"
			+ " or inciRepMppr.speciality_id =:specialityId" 
			+ " )" + " and(" + " :nabhTypeId= 0"
			+ " or inciRepMppr.nabh_type_id =:nabhTypeId" + " )"),
	
	@NamedNativeQuery(name = "GET_TEMPLATE_BY_REPORTED_NABH_ID",
	query = " select inciRepMppr.reporting_template_obj as \"reportingTemplateObj\" ,"
			+ " inciRepMppr.nabh_remark as \"nabhRemark\" from " + 
			"	public.t_nabh_reporting_mapper inciRepMppr " + 
			"	where " + 
			"	inciRepMppr.organization_id= :organizationId " + 
			"	and inciRepMppr.unit_id = :unitId " + 
			"	and inciRepMppr.nabh_reporting_mapper_id = :nabhReportingMapperId"),
	

	
	@NamedNativeQuery(name = "GET_TEMPLATE_BY_REPORTED_NABH_PROCESS_ID", query = "  select"
			+ "	distinct(inciRepMppr.nabh_reporting_mapper_id) as \"nabhReportingMapperId\"," + "	to_char("
			+ "		inciRepMppr.nabh_reporting_date," + "		'DD/MM/YYYY'"
			+ "	) as \"nabhReportingDate\"," + "	inciRepMppr.patient_id as \"patientId\","
			+ "	inciRepMppr.nabh_reporting_number as \"nabhReportingNumber\","
			+ "	inciRepMppr.nabh_remark as \"nabhRemark\","
			+ "	inciRepMppr.speciality_id as \"specialityId\","
			+ "	inciRepMppr.nabh_type_id as \"nabhTypeId\","
			+ "	inciRepMppr.incident_category_id as \"incidentCategoryId\","
			+ "	inciRepMppr.nabh_process_id as \"nabhProcessId\","
			+ " inciRepMppr.conversation_id as \"conversationId\","
			+ " inciRepMppr.encounter_id as \"encounterId\","
			+ " inciRepMppr.admission_id as \"admissionId\","
			+ "	incRepMst.nabh_process_desc as \"nabhProcessDesc\","  
			+ "	templatema5_.template_format_id as \"templateFormatId\","
			+ " reptInc.reported_nabh_mppr_id as \"reportedNabhMpprId\", "
			+ "	reptInc.resolution_details_type_id as \"resolutionDetailsTypeId\"," 
			+ "	reptInc.parent_user_id as \"parentUserId\"," 
			+ " reptInc.is_submit as \"isSubmit\", "
			+ "	spl.speciality_name as \"specialityName\","
			+ "	incCatMst.incident_category_desc as \"incidentCategoryDesc\","
			+ "	patient.uhid_number as \"uhidNumber\"," + "	(" + "		coalesce(" + "			patient.first_name,"
			+ "			''" + "		)|| ' ' || coalesce(" + "			patient.middle_name," + "			''"
			+ "		)|| ' ' || coalesce(" + "			patient.last_name," + "			''" + "		)"
			+ "	) as \"patientName\"," + "	inciRepMppr.status as \"status\"" + " from"
			+ "	public.t_nabh_reporting_mapper inciRepMppr"
			+ " left outer join patient.t_patient_registration patient on"
			+ "	inciRepMppr.patient_id = patient.patient_id" + " inner join doctor.m_speciality_master spl on"
			+ "	inciRepMppr.speciality_id = spl.speciality_id"
			+ " inner join public.m_incident_category_master incCatMst on"
			+ "	inciRepMppr.incident_category_id = incCatMst.incident_category_id"
			+ " inner join public.m_nabh_process_master incRepMst on"
			+ "	inciRepMppr.nabh_process_id = incRepMst.nabh_process_id"
			+ " inner join public.t_template_master templatema5_ on"
			+ "	inciRepMppr.reporting_template_id = templatema5_.template_id" + " " + "	and("
			+ "		templatema5_.status = 'A'" + "	)" + " left join public.t_reported_nabh_mapper reptInc on"
            + " inciRepMppr.nabh_reporting_mapper_id = reptInc.nabh_reporting_mapper_id "
			+ " LEFT join public.t_reported_incident_user_mapper reptUserInc on"
			+ "	reptInc.reported_nabh_mppr_id = reptUserInc.reported_nabh_mppr_id and( " + 
			"		reptUserInc.status = 'A' " + 
			"	)" +
			" where reptInc.reported_nabh_mppr_id=:reportedNabhMpprId" + 
			" and reptInc.organization_id =:organizationId" + 
			" and reptInc.unit_id=:unitId"),
	
})


@NamedQueries({
	@NamedQuery(name = "UPDATE_REPORTED_INCIDENT_USER_MAPPER",
			query = " UPDATE ReportedIncidentUserMapper reportedIncidentUserMapper "
					+ " SET reportedIncidentUserMapper.status = :status"
					+ " WHERE reportedIncidentUserMapper.organizationId= :organizationId "
					+ " AND reportedIncidentUserMapper.unitId = :unitId"
					+ " AND reportedIncidentUserMapper.reportedIncMpprId = :reportedIncMpprId"
            ),
    @NamedQuery(name = "UPDATE_REPORTED_INCIDENT_USER_MAPPER_NABH",
    query = " UPDATE ReportedIncidentUserMapper reportedIncidentUserMapper "
            + " SET reportedIncidentUserMapper.status = :status"
            + " WHERE reportedIncidentUserMapper.organizationId= :organizationId "
            + " AND reportedIncidentUserMapper.unitId = :unitId"
            + " AND reportedIncidentUserMapper.reportedNabhMpprId = :repIncUserMpprId"
        )
})
	

@Entity
@Table(name = "t_reported_incident_user_mapper", schema = "public")
@SequenceGenerator(name = "t_reported_incident_user_mapper_seq", sequenceName = "public.t_reported_incident_user_mapper_seq", allocationSize = 1)

public class ReportedIncidentUserMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_reported_incident_user_mapper_seq")
	@Column(name = "rep_inc_user_mppr_id")
	private Integer repIncUserMpprId;

	@Column(name = "reported_inc_mppr_id")
	private Integer reportedIncMpprId;

	@Column(name = "reported_template_id")
	private Integer reportingTemplateId;

	@Column(name = "reported_template_obj")
	private String reportedTemplateObj;

	@Column(name = "parent_rep_inc_user_id")
	private Integer parentRepIncUserId;
	
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "role_id")
	private Integer roleId;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;

	@Column(name = "status")
	private Character status;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "resolution_details_type_id")
	private Integer resolutionDetailsTypeId;
	
	@Column(name = "is_submit")
	private Character isSubmit;
	
	@Column(name = "conversation_id")
	private String conversationId;
	
	
	@Column(name = "reported_nabh_mppr_id")
	private Integer reportedNabhMpprId;
 
	public Integer getReportedNabhMpprId() {
		return reportedNabhMpprId;
	}
 
	public void setReportedNabhMpprId(Integer reportedNabhMpprId) {
		this.reportedNabhMpprId = reportedNabhMpprId;
	}

	public Integer getRepIncUserMpprId() {
		return repIncUserMpprId;
	}

	public void setRepIncUserMpprId(Integer repIncUserMpprId) {
		this.repIncUserMpprId = repIncUserMpprId;
	}

	public Integer getReportedIncMpprId() {
		return reportedIncMpprId;
	}

	public void setReportedIncMpprId(Integer reportedIncMpprId) {
		this.reportedIncMpprId = reportedIncMpprId;
	}

	public String getReportedTemplateObj() {
		return reportedTemplateObj;
	}

	public void setReportedTemplateObj(String reportedTemplateObj) {
		this.reportedTemplateObj = reportedTemplateObj;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
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

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getReportedTemplateId() {
		return reportingTemplateId;
	}

	public void setReportedTemplateId(Integer reportingTemplateId) {
		this.reportingTemplateId = reportingTemplateId;
	}

	public Integer getReportingTemplateId() {
		return reportingTemplateId;
	}

	public void setReportingTemplateId(Integer reportingTemplateId) {
		this.reportingTemplateId = reportingTemplateId;
	}

	public Integer getParentRepIncUserId() {
		return parentRepIncUserId;
	}

	public void setParentRepIncUserId(Integer parentRepIncUserId) {
		this.parentRepIncUserId = parentRepIncUserId;
	}

	public Integer getResolutionDetailsTypeId() {
		return resolutionDetailsTypeId;
	}

	public void setResolutionDetailsTypeId(Integer resolutionDetailsTypeId) {
		this.resolutionDetailsTypeId = resolutionDetailsTypeId;
	}

	public Character getIsSubmit() {
		return isSubmit;
	}

	public void setIsSubmit(Character isSubmit) {
		this.isSubmit = isSubmit;
	}

	public String getConversationId() {
		return conversationId;
	}

	public void setConversationId(String conversationId) {
		this.conversationId = conversationId;
	}
	
	

}
