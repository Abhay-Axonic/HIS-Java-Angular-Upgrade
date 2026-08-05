package com.param.opd.coversheet.model;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.adt.master.global.model.UserMaster;
import com.param.global.common.LocalTimeConverter;
import com.param.global.model.DoctorMaster;
import com.param.global.model.PatientRegistration;
import com.param.global.model.UserRoleMapper;
import com.param.global.model.VitalMaster;
import com.param.global.common.InstantConverter;
import com.param.global.common.InstantConverterAppoVital;

@NamedQueries({
	
	
	
	@NamedQuery(name="GET_LIST_APPOINTMENT_VITAL_MAPPER_BY_PATIENT_ID",
	query=" SELECT 		appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, appVitalMapp.vitalId as vitalId,"
		+ "				appVitalMapp.encounterId as encounterId, appVitalMapp.patientId as patientId, "
		+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
		+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy, "
		+ "				to_char(appVitalMapp.createdDate,'DD-MM-YYYY') as createdDate, to_char(appVitalMapp.updatedDate,'DD-MM-YYYY') as updatedDate,"
		+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
		+ "				vitalMst.vitalName as vitalName,"
		+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag "
		+ " FROM 		AppointmentVitalMapper appVitalMapp"
		+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
		+ " WHERE 		appVitalMapp.unitId=:unitId"
		+ " AND			appVitalMapp.organizationId=:organizationId"
		+ " AND 		appVitalMapp.patientId=:patientId"
		+ " ORDER BY 	appVitalMapp.appointmentVitalMapperId desc"
	),
	
	@NamedQuery(name="GET_VITAL_MASTER_TO_SYNC_BY_ID",
	query=" SELECT 		appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, appVitalMapp.vitalId as vitalId,"
		+ "				appVitalMapp.encounterId as encounterId, appVitalMapp.patientId as patientId, "
		+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
		+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy, "
		+ "				to_char(appVitalMapp.createdDate,'DD-MM-YYYY') as createdDate, to_char(appVitalMapp.updatedDate,'DD-MM-YYYY') as updatedDate,"
		+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
		+ "				vitalMst.vitalName as vitalName,"
		+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag "
		+ " FROM 		AppointmentVitalMapper appVitalMapp"
		+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
		+ " WHERE 		appVitalMapp.unitId=:unitId"
		+ " AND			appVitalMapp.organizationId=:organizationId"
		+ " AND appVitalMapp.appointmentVitalMapperId=:id"
	),
	
	@NamedQuery(name="GET_LIST_APPOINTMENT_VITAL_MAPPER",
			query=" SELECT 		distinct(appVitalMapp.vitalId) as vitalId, appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, orgWiseVitalMapper.vitalOrderOpd as vitalOrderOpd, "
				+ "				appVitalMapp.encounterId as encounterId, appVitalMapp.patientId as patientId, "
				+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
				+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy, "
				+ "				to_char(appVitalMapp.createdDate,'DD/MM/YYYY') as createdDate, to_char(appVitalMapp.postFactoDate,'DD/MM/YYYY') as postFactoDate, "
				+ "             to_char(appVitalMapp.postFactoTime,'HH24:MI') as postFactoTime, appVitalMapp.isPostFacto as isPostFacto,"
				+ "             to_char(appVitalMapp.updatedDate,'DD/MM/YYYY') as updatedDate,"
				+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
				+ "				vitalMst.vitalName as vitalName,"
				+ "              vitalMst.unit as unitDesc, "
				+ "				to_char(appVitalMapp.createdDate,'HH24:MI') as createdTime,"
				+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag, "
				+ " 			concat(coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
				+ " 			concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,appVitalMapp.dialysisEventId as dialysisEventId "
				+ " FROM 		AppointmentVitalMapper appVitalMapp"
				+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
				+ " INNER JOIN 	vitalMst.listOrgWiseVitalMapper orgWiseVitalMapper with orgWiseVitalMapper.organizationId=:organizationId"
				+ " LEFT JOIN 	appVitalMapp.userMaster userMaster"
				+ " LEFT JOIN 	userMaster.employeeMastersList employee"
				+ " LEFT JOIN 	userMaster.doctorMastersList doc"
//				+ " WHERE 		appVitalMapp.unitId=:unitId"
				+ " WHERE			appVitalMapp.organizationId=:organizationId"
//				+ " AND 		appVitalMapp.vitalId=:vitalId"
                + "	 AND appVitalMapp.vitalId IN (:multipleVitalList) "
				+ " AND 		appVitalMapp.patientId=:patientId"
				+ " AND 		appVitalMapp.status= 'A' "
//				+ " AND 		appVitalMapp.encounterId!=:encounterId"
//				+ " AND 		to_char(appVitalMapp.createdDate, 'YYYY-MM-DD') BETWEEN :formDate AND :toDate "
				+ " AND 		appVitalMapp.value !='' "	
				+ " AND 		appVitalMapp.isEnteredInError ='N'"
//				+ " ORDER BY 	appVitalMapp.appointmentVitalMapperId desc"
			),
	
	@NamedQuery(name="UPDATE_OLD_VITAL",
			query=" UPDATE 		AppointmentVitalMapper appointVitalMapp "
				+ " SET 		appointVitalMapp.status = 'I' "
				+ " WHERE 		appointVitalMapp.patientId=:patientId "
//				+ " AND appointVitalMapp.encounterId=:encounterId "
				+ " AND 		appointVitalMapp.vitalId=:vitalId AND appointVitalMapp.roleId=:roleId "
			),
			
			@NamedQuery(name="GET_LIST_APPOINTMENT_VITAL",
			query=" SELECT 		appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, appVitalMapp.vitalId as vitalId,"
				+ "				appVitalMapp.encounterId as encounterId, appVitalMapp.patientId as patientId, "
				+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
				+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy, "
				+ "				to_char(appVitalMapp.createdDate,'DD-MM-YYYY') as createdDate, to_char(appVitalMapp.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
				+ "				vitalMst.vitalName as vitalName,"
				+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag "
				+ " FROM 		AppointmentVitalMapper appVitalMapp"
				+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
				+ " WHERE 		appVitalMapp.unitId=:unitId"
				+ " AND			appVitalMapp.organizationId=:organizationId"
				+ " AND 		appVitalMapp.encounterId!=:encounterId"
				+ " AND 		appVitalMapp.patientId=:patientId"
				+ " AND 		appVitalMapp.status= 'A' "		
				
			),
			
			@NamedQuery(name="GET_LIST_APPOINTMENT_VITAL_MAPPER_BY_ROLE_ID",
			query=" SELECT 		appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, appVitalMapp.vitalId as vitalId,"
				+ "				appVitalMapp.encounterId as encounterId, appVitalMapp.patientId as patientId, "
				+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
				+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy, "
				+ "				to_char(appVitalMapp.createdDate,'DD-MM-YYYY') as createdDate, to_char(appVitalMapp.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
				+ "				vitalMst.vitalName as vitalName,"
				+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag "
				+ " FROM 		AppointmentVitalMapper appVitalMapp"
				+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
				+ " WHERE 		appVitalMapp.unitId=:unitId"
				+ " AND			appVitalMapp.organizationId=:organizationId"
				+ " AND 		appVitalMapp.encounterId!=:encounterId"
				+ " AND 		appVitalMapp.roleId=:roleId"		
				+ " AND 		appVitalMapp.patientId=:patientId"
				+ " AND 		appVitalMapp.status= 'A' "		
				
			),
			
			@NamedQuery(name="GET_CURRENT_LIST_APPOINTMENT_VITAL",
			query=" SELECT 		appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, appVitalMapp.vitalId as vitalId,"
				+ "				appVitalMapp.encounterId as encounterId, appVitalMapp.patientId as patientId, "
				+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
				+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy, "
				+ "				to_char(appVitalMapp.createdDate,'DD/MM/YYYY') as createdDate,"
				+ "             to_char(appVitalMapp.createdDate,'HH24:MI') as createdTime, to_char(appVitalMapp.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
				+ "				vitalMst.vitalName as vitalName, vitalMst.unit as unitDesc, "
				+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag ,"
				+ " concat(coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
				+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
				+ " FROM 		AppointmentVitalMapper appVitalMapp"
				+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
				+ " LEFT JOIN 	appVitalMapp.userMaster userMaster"
				+ " LEFT JOIN 	userMaster.employeeMastersList employee"
				+ " LEFT JOIN 	userMaster.doctorMastersList doc"
				+ " WHERE 		appVitalMapp.unitId=:unitId"
				+ " AND			appVitalMapp.organizationId=:organizationId"
//				+ " AND 		appVitalMapp.encounterId=:encounterId"
				+ " AND 		appVitalMapp.patientId=:patientId"
				+ " AND 		appVitalMapp.status= 'A' "		
				+ " AND 		cast(appVitalMapp.value as float)<> 0 "		
				
			),
			
			@NamedQuery(name="GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COVERSHEET",
			query=" SELECT 		distinct(appVitalMapp.vitalId) as vitalId, "
					+ " appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, orgWiseVitalMapper.vitalOrderOpd as vitalOrderOpd,"
				+ "				appVitalMapp.encounterId as encounterId,appVitalMapp.admissionId as admissionId, appVitalMapp.patientId as patientId, "
				+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
				+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy,"
				+ "				to_char(appVitalMapp.createdDate,'YYYY/MM/DD') as creatDate,"
				+ "				to_char(appVitalMapp.createdDate,'DD/MM/YYYY') as createdDate,appVitalMapp.createdDate as createdDateNew,"
				+ " 			 to_char(appVitalMapp.postFactoDate,'DD/MM/YYYY') as postFactoDate, "
				+ "             to_char(appVitalMapp.postFactoTime,'HH24:MI') as postFactoTime, appVitalMapp.isPostFacto as isPostFacto,"
				+ "             to_char(appVitalMapp.createdDate,'HH24:MI') as createdTime,appVitalMapp.createdDate as createdTimeNew, to_char(appVitalMapp.updatedDate,'DD-MM-YYYY') as updatedDate,appVitalMapp.updatedDate as updatedDateNew,"
				+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
				+ "				vitalMst.vitalName as vitalName, vitalMst.unit as unitDesc, vitalMst.isIcu as isIcu, "
				+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag ,"
				+ " concat(coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
				+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,appVitalMapp.dialysisEventId as dialysisEventId "
				+ " FROM 		AppointmentVitalMapper appVitalMapp"
				+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
				+ " INNER JOIN 	vitalMst.listOrgWiseVitalMapper orgWiseVitalMapper with orgWiseVitalMapper.organizationId=:organizationId"
				+ " LEFT JOIN 	appVitalMapp.userMaster userMaster"
				+ " LEFT JOIN 	userMaster.employeeMastersList employee"
				+ " LEFT JOIN 	userMaster.doctorMastersList doc"
				+ " WHERE 		"
//				+ " appVitalMapp.unitId=:unitId"
//				+ " AND			"
				+ " appVitalMapp.organizationId=:organizationId"
//				+ " AND 		appVitalMapp.encounterId=:encounterId"
				+ " AND 		appVitalMapp.patientId=:patientId"
				+ " AND 		appVitalMapp.status= 'A' "		
				+ " AND 		appVitalMapp.value !='' "		
				+ " AND 		appVitalMapp.isEnteredInError ='N'"
				+ "  order by	creatDate desc, createdTime desc  , vitalOrderOpd desc "		
			),
			
			
			@NamedQuery(name="GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COVERSHEET_ABDM",
			query=" SELECT 		distinct(appVitalMapp.vitalId) as vitalId, "
					+ " appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, orgWiseVitalMapper.vitalOrderOpd as vitalOrderOpd,"
				+ "				appVitalMapp.encounterId as encounterId,appVitalMapp.admissionId as admissionId, appVitalMapp.patientId as patientId, "
				+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
				+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy,"
				+ "				to_char(appVitalMapp.createdDate,'YYYY/MM/DD') as creatDate,"
				+ "				to_char(appVitalMapp.createdDate,'DD/MM/YYYY') as createdDate,appVitalMapp.createdDate as createdDateNew,"
				+ " 			 to_char(appVitalMapp.postFactoDate,'DD/MM/YYYY') as postFactoDate, "
				+ "             to_char(appVitalMapp.postFactoTime,'HH24:MI') as postFactoTime, appVitalMapp.isPostFacto as isPostFacto,"
				+ "             to_char(appVitalMapp.createdDate,'HH24:MI') as createdTime,appVitalMapp.createdDate as createdTimeNew, to_char(appVitalMapp.updatedDate,'DD-MM-YYYY') as updatedDate,appVitalMapp.updatedDate as updatedDateNew,"
				+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
				+ "				vitalMst.vitalName as vitalName, vitalMst.unit as unitDesc, vitalMst.isIcu as isIcu, "
				+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag ,"
				+ " concat(coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
				+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,appVitalMapp.dialysisEventId as dialysisEventId "
				+ " FROM 		AppointmentVitalMapper appVitalMapp"
				+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
				+ " INNER JOIN 	vitalMst.listOrgWiseVitalMapper orgWiseVitalMapper with orgWiseVitalMapper.organizationId=:organizationId"
				+ " LEFT JOIN 	appVitalMapp.userMaster userMaster"
				+ " LEFT JOIN 	userMaster.employeeMastersList employee"
				+ " LEFT JOIN 	userMaster.doctorMastersList doc"
				+ " WHERE 		"
//				+ " appVitalMapp.unitId=:unitId"
//				+ " AND			"
				+ " appVitalMapp.organizationId=:organizationId"
				+ " AND 		appVitalMapp.encounterId=:encounterId"
				+ " AND 		appVitalMapp.patientId=:patientId"
				+ " AND 		appVitalMapp.status= 'A' "		
				+ " AND 		appVitalMapp.value !='' "		
				+ " AND 		appVitalMapp.isEnteredInError ='N'"
				+ "  order by	creatDate desc, createdTime desc  , vitalOrderOpd desc "		
			),
			
			@NamedQuery(name="GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COVERSHEET_ABDM_ADMISSION",
			query=" SELECT 		distinct(appVitalMapp.vitalId) as vitalId, "
					+ " appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, orgWiseVitalMapper.vitalOrderOpd as vitalOrderOpd,"
				+ "				appVitalMapp.encounterId as encounterId,appVitalMapp.admissionId as admissionId, appVitalMapp.patientId as patientId, "
				+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
				+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy,"
				+ "				to_char(appVitalMapp.createdDate,'YYYY/MM/DD') as creatDate,"
				+ "				to_char(appVitalMapp.createdDate,'DD/MM/YYYY') as createdDate,appVitalMapp.createdDate as createdDateNew,"
				+ " 			 to_char(appVitalMapp.postFactoDate,'DD/MM/YYYY') as postFactoDate, "
				+ "             to_char(appVitalMapp.postFactoTime,'HH24:MI') as postFactoTime, appVitalMapp.isPostFacto as isPostFacto,"
				+ "             to_char(appVitalMapp.createdDate,'HH24:MI') as createdTime,appVitalMapp.createdDate as createdTimeNew, to_char(appVitalMapp.updatedDate,'DD-MM-YYYY') as updatedDate,appVitalMapp.updatedDate as updatedDateNew,"
				+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
				+ "				vitalMst.vitalName as vitalName, vitalMst.unit as unitDesc, vitalMst.isIcu as isIcu, "
				+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag ,"
				+ " concat(coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
				+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,appVitalMapp.dialysisEventId as dialysisEventId "
				+ " FROM 		AppointmentVitalMapper appVitalMapp"
				+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
				+ " INNER JOIN 	vitalMst.listOrgWiseVitalMapper orgWiseVitalMapper with orgWiseVitalMapper.organizationId=:organizationId"
				+ " LEFT JOIN 	appVitalMapp.userMaster userMaster"
				+ " LEFT JOIN 	userMaster.employeeMastersList employee"
				+ " LEFT JOIN 	userMaster.doctorMastersList doc"
				+ " WHERE 		"
				+ " appVitalMapp.organizationId=:organizationId"
				+ " AND 		appVitalMapp.admissionId=:admissionId"
				+ " AND 		appVitalMapp.patientId=:patientId"
				+ " AND 		appVitalMapp.status= 'A' "		
				+ " AND 		appVitalMapp.value !='' "		
				+ " AND 		appVitalMapp.isEnteredInError ='N'"
				+ "  order by	creatDate desc, createdTime desc  , vitalOrderOpd desc "		
			),

			@NamedQuery(name="GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COV_TEMPLATE",
			query=" SELECT 		distinct(appVitalMapp.vitalId) as vitalId, "
					+ " appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, orgWiseVitalMapper.vitalOrderOpd as vitalOrderOpd,"
				+ "				appVitalMapp.encounterId as encounterId,appVitalMapp.admissionId as admissionId, appVitalMapp.patientId as patientId, "
				+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
				+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy,"
				+ "				to_char(appVitalMapp.createdDate,'YYYY/MM/DD') as creatDate,"
				+ "				to_char(appVitalMapp.createdDate,'DD/MM/YYYY') as createdDate,"
				+ " 			 to_char(appVitalMapp.postFactoDate,'DD/MM/YYYY') as postFactoDate, "
				+ "             to_char(appVitalMapp.postFactoTime,'HH24:MI') as postFactoTime, appVitalMapp.isPostFacto as isPostFacto,"
				+ "             to_char(appVitalMapp.createdDate,'HH24:MI') as createdTime, to_char(appVitalMapp.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
				+ "				vitalMst.vitalName as vitalName, vitalMst.unit as unitDesc, "
				+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag ,"
				+ " concat(coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
				+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName,appVitalMapp.dialysisEventId as dialysisEventId "
				+ " FROM 		AppointmentVitalMapper appVitalMapp"
				+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
				+ " INNER JOIN 	vitalMst.listOrgWiseVitalMapper orgWiseVitalMapper with orgWiseVitalMapper.organizationId=:organizationId"
				+ " LEFT JOIN 	appVitalMapp.userMaster userMaster"
				+ " LEFT JOIN 	userMaster.employeeMastersList employee"
				+ " LEFT JOIN 	userMaster.doctorMastersList doc"
				+ " WHERE 		"
				+ " appVitalMapp.unitId=:unitId"
				+ " AND			"
				+ " appVitalMapp.organizationId=:organizationId"
				+ " AND 		appVitalMapp.covTemplateId=:covTemplateId"
				+ " AND 		appVitalMapp.status= 'A' "		
				+ " AND 		appVitalMapp.value !='' "		
				+ " AND 		appVitalMapp.isEnteredInError ='N'"
				+ "  order by	creatDate desc, createdTime desc  , vitalOrderOpd desc "		
			),
			
			
			@NamedQuery(name="GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_DIALYSIS",
			query=" SELECT 		appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, appVitalMapp.vitalId as vitalId,"
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
				+ " INNER JOIN 	vitalMst.listOrgWiseVitalMapper orgWiseVitalMapper with orgWiseVitalMapper.organizationId=:organizationId"
				+ " LEFT JOIN 	appVitalMapp.userMaster userMaster"
				+ " LEFT JOIN 	userMaster.employeeMastersList employee"
				+ " LEFT JOIN 	userMaster.doctorMastersList doc"
				+ " WHERE 		"
//				+ " appVitalMapp.unitId=:unitId"
//				+ " AND			"
				+ " appVitalMapp.organizationId=:organizationId"
//				+ " AND 		appVitalMapp.encounterId=:encounterId"
				+ " AND 		appVitalMapp.patientId=:patientId"
				+ " AND 		appVitalMapp.status= 'A' "		
				+ " AND 		appVitalMapp.value !='' "		
				+ " AND 		appVitalMapp.isEnteredInError ='N'"
				+ " AND         appVitalMapp.dialysisEventId=:dialysisEventId "
				+ "  order by	appVitalMapp.createdDate asc, orgWiseVitalMapper.vitalOrderOpd desc "		
			),
			
			@NamedQuery(name="GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_BOOK_VIEW",
			query=" SELECT 		appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, appVitalMapp.vitalId as vitalId, orgWiseVitalMapper.vitalOrderOpd as vitalOrderOpd,"
				+ "				appVitalMapp.encounterId as encounterId, appVitalMapp.patientId as patientId, "
				+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
				+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy, "
				+ "				to_char(appVitalMapp.createdDate,'DD/MM/YYYY') as createdDate, to_char(appVitalMapp.postFactoDate,'DD/MM/YYYY') as postFactoDate, "
				+ "             to_char(appVitalMapp.postFactoTime,'HH24:MI') as postFactoTime, appVitalMapp.isPostFacto as isPostFacto,"				
				+ "             to_char(appVitalMapp.createdDate,'HH24:MI') as createdTime, to_char(appVitalMapp.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
				+ "				vitalMst.vitalName as vitalName, vitalMst.unit as unitDesc, "
				+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag ,"
				+ " concat(coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
				+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
				+ " FROM 		AppointmentVitalMapper appVitalMapp"
				+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
				+ " INNER JOIN 	vitalMst.listOrgWiseVitalMapper orgWiseVitalMapper with orgWiseVitalMapper.organizationId=:organizationId"
				+ " LEFT JOIN 	appVitalMapp.userMaster userMaster"
				+ " LEFT JOIN 	userMaster.employeeMastersList employee"
				+ " LEFT JOIN 	userMaster.doctorMastersList doc"
				+ " Where			appVitalMapp.organizationId=:organizationId"
				+ " AND 		appVitalMapp.patientId=:patientId"
				+ " AND 		appVitalMapp.status= 'A' "		
				+ " AND 		appVitalMapp.value !='' "		
				+ " AND 		appVitalMapp.isEnteredInError ='N'"	
				+ " AND to_char(appVitalMapp.createdDate , 'YYYY-MM-DD') BETWEEN :fromDate " 
				+ " AND :toDate "
				+ "  order by	createdDate desc, createdTime desc  , vitalOrderOpd desc "		
				),
			
			@NamedQuery(name="GET_LIST_OF_ENTERED_IN_ERROR_VITALS",
			query=" SELECT 		appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, appVitalMapp.vitalId as vitalId,"
				+ "				appVitalMapp.encounterId as encounterId, appVitalMapp.patientId as patientId, "
				+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
				+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy, appVitalMapp.reasonId as reasonId, "
				+ "             appVitalMapp.isEnteredInError as isEnteredInError, reasonMaster.reasonDesc as reasonDesc, "
				+ "				to_char(appVitalMapp.createdDate,'DD/MM/YYYY') as createdDate, to_char(appVitalMapp.postFactoDate,'DD/MM/YYYY') as postFactoDate, "
				+ "             to_char(appVitalMapp.postFactoTime,'HH24:MI') as postFactoTime, appVitalMapp.isPostFacto as isPostFacto,"
				+ "             to_char(appVitalMapp.createdDate,'HH24:MI') as createdTime, to_char(appVitalMapp.updatedDate,'DD/MM/YYYY') as updatedDate, to_char(appVitalMapp.updatedDate,'HH24:MI') as updatedTime,"
				+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
				+ "				vitalMst.vitalName as vitalName, vitalMst.unit as unitDesc, "
				+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag ,"
				+ " concat(coalesce(employee.firstName),' ',coalesce(employee.middleName),' ',coalesce(employee.lastName)) as employeeName, "
				+ " concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName "
				+ " FROM 		AppointmentVitalMapper appVitalMapp"
				+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
				+ " LEFT JOIN 	appVitalMapp.userMaster userMaster"
				+ " LEFT JOIN 	userMaster.employeeMastersList employee"
				+ " LEFT JOIN 	userMaster.doctorMastersList doc"
				+ " LEFT JOIN 	appVitalMapp.reasonMaster reasonMaster"
				+ " WHERE 		appVitalMapp.unitId=:unitId"
				+ " AND			appVitalMapp.organizationId=:organizationId"
//				+ " AND 		appVitalMapp.encounterId=:encounterId"
				+ " AND 		appVitalMapp.patientId=:patientId"
				+ " AND 		appVitalMapp.status= 'A' "		
				+ " AND 		appVitalMapp.value !='' "		
				+ " AND 		appVitalMapp.isEnteredInError ='Y'"		
				
			),
			
			@NamedQuery(name="GET_CURRENT_LIST_APPOINTMENT_VITAL_MAPPER_BY_ROLE_ID",
			query=" SELECT 		appVitalMapp.appointmentVitalMapperId as appointmentVitalMapperId, appVitalMapp.vitalId as vitalId,"
				+ "				appVitalMapp.encounterId as encounterId, appVitalMapp.patientId as patientId, "
				+ "				appVitalMapp.value as value, appVitalMapp.status as status, appVitalMapp.isSelfTriage as isSelfTriage,"
				+ "				appVitalMapp.createdBy as createdBy, appVitalMapp.updatedBy as updatedBy, vitalMst.unit as unitDesc, "
				+ "				to_char(appVitalMapp.createdDate,'dd/MM/yyyy') as createdDate, to_char(appVitalMapp.updatedDate,'dd/MM/yyyy') as updatedDate,"
				+ "				appVitalMapp.unitId as unitId, appVitalMapp.organizationId as organizationId, "
				+ "				vitalMst.vitalName as vitalName,"
				+ " 			appVitalMapp.roleId as roleId, appVitalMapp.isReviewedFlag as isReviewedFlag "
				+ " FROM 		AppointmentVitalMapper appVitalMapp"
				+ " INNER JOIN 	appVitalMapp.vitalMaster vitalMst"
				+ " WHERE 		appVitalMapp.unitId=:unitId"
				+ " AND			appVitalMapp.organizationId=:organizationId"
//				+ " AND 		appVitalMapp.encounterId=:encounterId"
				+ " AND 		appVitalMapp.roleId=:roleId"		
				+ " AND 		appVitalMapp.patientId=:patientId"
				+ " AND 		appVitalMapp.status= 'A' "		
				+ " AND 		appVitalMapp.value !='' "		
			),
	
})
@NamedNativeQuery(name="GET_QUALIFIERS_LIST_FOR_POPUP",
				query="select "
//						+ "distinct(appovtl.patient_id) as \"patientId\" ,"
						+ "vmst.vital_name as \"vitalName\", "
						+ "vqmst.vital_qualifire_desc as \"vitalQualifireDesc\"," + 
						"	vcmst.vital_category_desc as \"vitalCategoryDesc\","
//						+ " to_char(appovtl.created_date, 'HH24:MI') as \"createdTime\", " + 
//						"	to_char(appovtl.created_date, 'YYYY-MM-DD') as \"createdDate\","
						+ " vam.appointment_vital_mapper_id as \"appointmentVitalMapperId\" " + 
						"	from emr.t_vital_appo_qualifier_mapper vam  " + 
						"	inner join emr.m_vital_master vmst on  vmst.vital_id=vam.vital_id" + 
						"	inner join emr.m_vital_qualifire_master vqmst on  vqmst.vital_qualifire_id=vam.vital_qualifier_id" + 
						"	inner join emr.m_vital_category_master vcmst on  vcmst.vital_category_id=vqmst.vital_category_id" + 
//						"	inner join patient.t_appointment_vital_mapper appovtl on  appovtl.patient_id=vam.patient_id" + 
						"	where vam.vital_id=:vitalId" + 
						"	and vam.organization_id=:organizationId" +
						"	and vam.appointment_vital_mapper_id=:appointmentVitalMapperId")

@Entity
@Table(name = "t_appointment_vital_mapper", schema = "patient")
@SequenceGenerator(name = "appointment_vital_mapper_seq", sequenceName = "patient.appointment_vital_mapper_seq", allocationSize = 1)
public class AppointmentVitalMapper {

	@Id
	@Column(name = "appointment_vital_mapper_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "appointment_vital_mapper_seq")
	private Integer appointmentVitalMapperId;
	
	@Column(name = "vital_id")
	private Integer vitalId;
	
	/*@Column(name = "appointment_id")
	private Integer appointmentId;*/
	
	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "value")
	private String value;
	
	@Column(name = "status")
	private char status;
	
	@Column(name = "is_self_triage")
	private char isSelfTriage;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "createdDate")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="role_id")
	private Integer roleId;
	
	@Column(name="is_reviewed_flag")
	private char isReviewedFlag;
	
	@Column(name="is_post_facto")
	private char isPostFacto;
	
	@Column(name = "post_facto_time")
	@Convert(converter = InstantConverterAppoVital.class)
	private Instant postFactoTime;
	
	@Column(name = "post_facto_date")
	private Date postFactoDate;
	
	@Column(name="reason_id")
	private Integer reasonId;
	
	@Column(name="dialysis_event_id")
	private Integer dialysisEventId;
	
	@Column(name="patient_dialysis_rec_id")
	private Integer patientDialysisRecId;
	
	@Column(name="is_entered_in_error")
	private Character isEnteredInError;
	
	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	/*@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "appointment_id", insertable = false, nullable = false, updatable = false)
	private AppointmentMaster appointmentMaster;*/
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vital_id", insertable = false, nullable = false, updatable = false)
	private VitalMaster vitalMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by", insertable = false, nullable = false, updatable = false)
	private UserMaster userMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "reason_id", insertable = false, nullable = false, updatable = false)
	private ReasonMaster reasonMaster;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<UserRoleMapper> userMasterList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "userMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<DoctorMaster> doctorMastersList;
	
	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public char getIsReviewedFlag() {
		return isReviewedFlag;
	}

	public void setIsReviewedFlag(char isReviewedFlag) {
		this.isReviewedFlag = isReviewedFlag;
	}

	public Integer getAppointmentVitalMapperId() {
		return appointmentVitalMapperId;
	}

	public void setAppointmentVitalMapperId(Integer appointmentVitalMapperId) {
		this.appointmentVitalMapperId = appointmentVitalMapperId;
	}

	public Integer getVitalId() {
		return vitalId;
	}

	public void setVitalId(Integer vitalId) {
		this.vitalId = vitalId;
	}

	

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}

	public char getIsSelfTriage() {
		return isSelfTriage;
	}

	public void setIsSelfTriage(char isSelfTriage) {
		this.isSelfTriage = isSelfTriage;
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

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}


	public VitalMaster getVitalMaster() {
		return vitalMaster;
	}

	public void setVitalMaster(VitalMaster vitalMaster) {
		this.vitalMaster = vitalMaster;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getReasonId() {
		return reasonId;
	}

	public void setReasonId(Integer reasonId) {
		this.reasonId = reasonId;
	}

	public Character getIsEnteredInError() {
		return isEnteredInError;
	}

	public void setIsEnteredInError(Character isEnteredInError) {
		this.isEnteredInError = isEnteredInError;
	}

	public char getIsPostFacto() {
		return isPostFacto;
	}

	public void setIsPostFacto(char isPostFacto) {
		this.isPostFacto = isPostFacto;
	}

	public Instant getPostFactoTime() {
		return postFactoTime;
	}

	public void setPostFactoTime(Instant postFactoTime) {
		this.postFactoTime = postFactoTime;
	}

	public Date getPostFactoDate() {
		return postFactoDate;
	}

	public void setPostFactoDate(Date postFactoDate) {
		this.postFactoDate = postFactoDate;
	}

	public Integer getDialysisEventId() {
		return dialysisEventId;
	}

	public void setDialysisEventId(Integer dialysisEventId) {
		this.dialysisEventId = dialysisEventId;
	}

	public Integer getPatientDialysisRecId() {
		return patientDialysisRecId;
	}

	public void setPatientDialysisRecId(Integer patientDialysisRecId) {
		this.patientDialysisRecId = patientDialysisRecId;
	}

	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;
	}
	
	
	
}
