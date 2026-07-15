package com.param.opd.coversheet.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UserMaster;
import com.param.entity.model.master.Generic;
import com.param.global.model.AllergyMaster;
import com.param.global.model.AllergyTypeMaster;
import com.param.global.model.PatientRegistration;
import com.param.global.model.SeverityMaster;

@NamedQueries({
		
	@NamedQuery(name="GET_LIST_ALLERGY_PATIENT_MAPPER",
			query=" SELECT 		alrgyMap.allergyId as allergyId, alrgyMap.allergyPatientMapperId as allergyPatientMapperId, "
				+ "				alrgyMap.patientId as patientId, alrgyMst.allergyDesc as allergyDesc ,"
				+ "				alrgyMap.allergyTypeId as allergyTypeId,"
				+ "				alrgyMap.type as type, alrgyMap.natureOfReaction as natureOfReaction, alrgyMap.comments as comments,"
				+ " 			alrgyMap.severityId as severityId, servtyMst.severityDesc as severityDesc,"
				+ " 			alrgyMap.remark as remark, alrgyMap.month as month, alrgyMap.year as year, alrgyMap.isNoKnownAllergies as isNoKnownAllergies,"
				+ " 			alrgyMst.allergyName as allergyName, "
				+ "             alrgyMst.cimsCode as cimsCode,alrgyMst.cimsType as cimsType, "
				+ "				alrgyTypeMst.allergyTypeDesc as allergyTypeName,"
				+ "  (case " 
				+ "  when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
				+ "  when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
				+ "  else 'Admin' end) as userName,"
				+ "				alrgyMap.encounterId as encounterId, "
				+ "				alrgyMap.roleId as roleId,"
				+ "				alrgyMap.isReviewedFlag as isReviewedFlag, alrgyMap.isEnterInErrorStatus as isEnterInErrorStatus "
				+ " FROM 		AllergyPatientMapper alrgyMap"
				+ " left JOIN 	alrgyMap.allergyMaster alrgyMst "
				+ " left JOIN 	alrgyMap.allergyTypeMaster alrgyTypeMst "
				+ " left JOIN 	alrgyMap.severityMaster servtyMst "
				+ " LEFT JOIN   alrgyMap.userMaster um " 
				+ " LEFT JOIN   um.employeeMastersList empmst " 
				+ " LEFT JOIN   um.doctorMastersList  docmst "
				+ " WHERE 		alrgyMap.patientId=:patientId"
//				+ " AND 		alrgyMap.unitId=:unitId"
				+ " AND 		alrgyMap.organizationId=:organizationId"
				+ " AND 		alrgyMap.resolvedRemark is null "
				+ " AND			to_char(alrgyMap.createdDate, 'DD-MM-YYYY') != :created_date"
				//+ " AND 		alrgyMap.encounterId!=:encounterId"
				+ " AND 		alrgyMap.status = 'A'"
				+ " ORDER BY alrgyMap.type ASC"
		),
				
				@NamedQuery(name="GET_LIST_ALLERGY_PATIENT_MAPPER_BY_ROLE_ID",
				query=" SELECT 		alrgyMap.allergyId as allergyId, alrgyMap.allergyPatientMapperId as allergyPatientMapperId, "
					+ "				alrgyMap.patientId as patientId, alrgyMst.allergyDesc as allergyDesc ,"
					+ "				alrgyMap.allergyTypeId as allergyTypeId,"
					+ "				alrgyMap.type as type, alrgyMap.natureOfReaction as natureOfReaction, alrgyMap.comments as comments,"
					+ " 			alrgyMap.severityId as severityId, servtyMst.severityDesc as severityDesc,"
					+ " 			alrgyMap.remark as remark, alrgyMap.month as month, alrgyMap.year as year, alrgyMap.isNoKnownAllergies as isNoKnownAllergies,"
					+ "				alrgyMap.status as status, alrgyMap.createdBy as createdBy, alrgyMap.updatedBy as updatedBy, "
					+ " 			to_char(alrgyMap.createdDate,'DD-MM-YYYY') as createdDate, to_char(alrgyMap.updatedDate,'DD-MM-YYYY') as updatedDate,"
					+ "				alrgyMap.unitId as unitId, alrgyMap.organizationId as organizationId, "
					+ " 			alrgyMst.allergyName as allergyName, alrgyTypeMst.allergyTypeName as allergyTypeName,"
					+ "				alrgyMap.encounterId as encounterId, alrgyMap.roleId as roleId,"
					+ "				alrgyMap.isReviewedFlag as isReviewedFlag, alrgyMap.isEnterInErrorStatus as isEnterInErrorStatus "
					+ " FROM 		AllergyPatientMapper alrgyMap"
					+ " INNER JOIN 	alrgyMap.allergyMaster alrgyMst "
					+ " INNER JOIN 	alrgyMap.allergyTypeMaster alrgyTypeMst "
					+ " INNER JOIN 	alrgyMap.severityMaster servtyMst "
					+ " WHERE 		alrgyMap.patientId=:patientId"
					+ " AND 		alrgyMap.unitId=:unitId"
					+ " AND 		alrgyMap.organizationId=:organizationId"
					+ " AND 		alrgyMap.encounterId!=:encounterId"
					+ " AND 		alrgyMap.roleId=:roleId"
					+ " AND 		alrgyMap.status = 'A'"
			),
			
			@NamedQuery(name="GET_CURRENT_LIST_ALLERGY_PATIENT_MAPPER",
			query=" SELECT 		alrgyMap.allergyId as allergyId, alrgyMap.allergyPatientMapperId as allergyPatientMapperId, "
				+ "				alrgyMap.patientId as patientId, alrgyMst.allergyDesc as allergyDesc ,"
				+ "				alrgyMap.genericId as genericId, "
				+ "				alrgyMap.allergyTypeId as allergyTypeId,"
				+ "				alrgyMap.type as type, alrgyMap.natureOfReaction as natureOfReaction, alrgyMap.comments as comments,"
				+ " 			alrgyMap.severityId as severityId, servtyMst.severityDesc as severityDesc,"
				+ " 			alrgyMap.remark as remark, alrgyMap.month as month, alrgyMap.year as year, alrgyMap.isNoKnownAllergies as isNoKnownAllergies,"
				+ "				alrgyMap.status as status, alrgyMap.createdBy as createdBy, alrgyMap.updatedBy as updatedBy, "
				+ " 			to_char(alrgyMap.createdDate,'DD/MM/YYYY') as createdDate, to_char(alrgyMap.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "				alrgyMap.unitId as unitId, alrgyMap.organizationId as organizationId, "
				+ " 			alrgyMst.allergyName as allergyName, alrgyTypeMst.allergyTypeName as allergyTypeName,"
				+ "				alrgyMap.encounterId as encounterId, alrgyMap.roleId as roleId,"
				+ "				alrgyMap.isReviewedFlag as isReviewedFlag,genericMst.id as genericId,genericMst.name as genericName,alrgyMap.isEnterInErrorStatus as isEnterInErrorStatus "
				+ " FROM 		AllergyPatientMapper alrgyMap"
				+ " LEFT JOIN 	alrgyMap.allergyMaster alrgyMst "
				+ " LEFT JOIN 	alrgyMap.generic genericMst "
				+ " LEFT JOIN 	alrgyMap.allergyTypeMaster alrgyTypeMst "
				+ " LEFT JOIN 	alrgyMap.severityMaster servtyMst "
				+ " WHERE 		alrgyMap.patientId=:patientId"
				+ " AND 		alrgyMap.unitId=:unitId"
				+ " AND 		alrgyMap.organizationId=:organizationId"
//				+ " AND 		alrgyMap.encounterId=:encounterId"
				+ " AND 		alrgyMap.status = 'A'"
		),
		
		@NamedQuery(name="GET_CURRENT_LIST_ALLERGY_PATIENT_MAPPER_BY_ROLE_ID",
		query=" SELECT 		alrgyMap.allergyId as allergyId, alrgyMap.allergyPatientMapperId as allergyPatientMapperId, "
				+ "				alrgyMap.patientId as patientId,alrgyMst.allergyDesc as allergyDesc , "
				+ "				alrgyMap.genericId as genericId, "
				+ "				alrgyMap.allergyTypeId as allergyTypeId,"
				+ "				alrgyMap.type as type, alrgyMap.natureOfReaction as natureOfReaction, alrgyMap.comments as comments,"
				+ " 			alrgyMap.severityId as severityId, servtyMst.severityDesc as severityDesc,"
				+ " 			alrgyMap.remark as remark, alrgyMap.month as month, alrgyMap.year as year, alrgyMap.isNoKnownAllergies as isNoKnownAllergies,"
				+ "				alrgyMap.status as status, alrgyMap.createdBy as createdBy, alrgyMap.updatedBy as updatedBy, "
				+ " 			to_char(alrgyMap.createdDate,'DD/MM/YYYY') as createdDate, to_char(alrgyMap.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "				alrgyMap.unitId as unitId, alrgyMap.organizationId as organizationId, "
				+ " 			alrgyMst.allergyName as allergyName, alrgyTypeMst.allergyTypeName as allergyTypeName,"
				+ "				alrgyMap.encounterId as encounterId, alrgyMap.roleId as roleId,"
				+ "				alrgyMap.isReviewedFlag as isReviewedFlag,genericMst.id as genericId,genericMst.name as genericName,alrgyMap.isEnterInErrorStatus as isEnterInErrorStatus "
				+ " FROM 		AllergyPatientMapper alrgyMap"
				+ " LEFT JOIN 	alrgyMap.allergyMaster alrgyMst "
				+ " LEFT JOIN 	alrgyMap.generic genericMst "
				+ " LEFT JOIN 	alrgyMap.allergyTypeMaster alrgyTypeMst "
				+ " LEFT JOIN 	alrgyMap.severityMaster servtyMst "
				+ " WHERE 		alrgyMap.patientId=:patientId"
				+ " AND 		alrgyMap.unitId=:unitId"
				+ " AND 		alrgyMap.organizationId=:organizationId"
	//			+ " AND 		alrgyMap.encounterId=:encounterId"
				+ " AND 		alrgyMap.roleId=:roleId"
				+ " AND 		alrgyMap.status = 'A'"
	),
				
				
				@NamedQuery(name="CANCEL_ALLERGY_PATIENT_MAPPER",
				query="UPDATE AllergyPatientMapper alrgyMap SET alrgyMap.status= 'I' "
					+ " WHERE 		alrgyMap.allergyPatientMapperId.patientId=:patientId"
					+ " AND 		alrgyMap.allergyPatientMapperId.allergyId=:allergyId"
					+ " AND 		alrgyMap.allergyPatientMapperId.allergyTypeId=:allergyTypeId"
					
			),
			@NamedQuery(name = "GENERIC_AUTO_FILL_SEARCH", query = " SELECT 	generic.id as genericId, generic.code as genericCode, generic.name as genericName "
						+ " FROM 	Generic generic "
						+ " WHERE 	LOWER(generic.name) LIKE :genericName "),
						//+ " AND		generic.unitId=:unitId " + " AND		generic.organizationId=:organizationId "
						//+ " AND 	generic.status = 'A' "),

			
			@NamedQuery(name="GET_CURRENT_LIST_ALLERGY_PATIENT_MAPPER_FOR_COVERSHEET",
			query=" SELECT 		alrgyMap.allergyId as allergyId, alrgyMap.allergyPatientMapperId as allergyPatientMapperId, "
				+ "				alrgyMap.patientId as patientId, alrgyMap.monthYear as monthYear,"
				+ "				alrgyMap.patientId as patientId, alrgyMap.covTemplateId as covTemplateId, "
				+ "				alrgyMap.genericId as genericId, "
				+ "				alrgyMap.allergyTypeId as allergyTypeId,"
				+ "				alrgyMap.type as type, alrgyMap.natureOfReaction as natureOfReaction, alrgyMap.comments as comments,"
				+ " 			alrgyMap.severityId as severityId, servtyMst.severityDesc as severityDesc,"
				+ " 			alrgyMap.remark as remark, alrgyMap.month as month, alrgyMap.year as year, alrgyMap.isNoKnownAllergies as isNoKnownAllergies,"
				+ "				alrgyMap.status as status, alrgyMap.createdBy as createdBy, alrgyMap.updatedBy as updatedBy, "
				+ " 			to_char(alrgyMap.createdDate,'DD/MM/YYYY') as createdDate, to_char(alrgyMap.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "				alrgyMap.unitId as unitId, alrgyMap.organizationId as organizationId, alrgyMst.allergyDesc as allergyDesc ,"
				+ " 			alrgyMst.allergyName as allergyName, alrgyMst.cimsCode as cimsCode,alrgyMst.cimsType as cimsType, alrgyTypeMst.allergyTypeDesc as allergyTypeName,"
				+ "  (case " 
				+ "  when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
				+ "  when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
				+ "  else 'Admin' end) as userName,"
				+ "				alrgyMap.encounterId as encounterId, alrgyMap.roleId as roleId,"
				+ "				alrgyMap.isReviewedFlag as isReviewedFlag,genericMst.id as genericId,genericMst.name as genericName,alrgyMap.isEnterInErrorStatus as isEnterInErrorStatus "
				+ " FROM 		AllergyPatientMapper alrgyMap"
				+ " LEFT JOIN 	alrgyMap.allergyMaster alrgyMst "
				+ " LEFT JOIN 	alrgyMap.generic genericMst "
				+ " LEFT JOIN 	alrgyMap.allergyTypeMaster alrgyTypeMst "
				+ " LEFT JOIN 	alrgyMap.severityMaster servtyMst "
				+ " LEFT JOIN alrgyMap.userMaster um " 
				+ " LEFT JOIN um.employeeMastersList empmst " 
				+ " LEFT JOIN um.doctorMastersList  docmst "
				+ " WHERE 		alrgyMap.patientId=:patientId"
//				+ " AND 		alrgyMap.unitId=:unitId"
				+ " AND 		alrgyMap.organizationId=:organizationId"
				+ " AND 		alrgyMap.resolvedRemark is null "
				+ " AND			to_char(alrgyMap.createdDate, 'DD-MM-YYYY') <= :created_date"
				+ " AND 		alrgyMap.status = 'A'"
		),
			
			@NamedQuery(name="GET_CURRENT_LIST_ALLERGY_PATIENT_MAPPER_FOR_COV_TEMPLATE",
			query=" SELECT 		alrgyMap.allergyId as allergyId, alrgyMap.allergyPatientMapperId as allergyPatientMapperId, "
				+ "				alrgyMap.patientId as patientId, alrgyMap.covTemplateId as covTemplateId, "
				+ "				alrgyMap.genericId as genericId, "
				+ "				alrgyMap.allergyTypeId as allergyTypeId,"
				+ "				alrgyMap.type as type, alrgyMap.natureOfReaction as natureOfReaction, alrgyMap.comments as comments,"
				+ " 			alrgyMap.severityId as severityId, servtyMst.severityDesc as severityDesc,"
				+ " 			alrgyMap.remark as remark, alrgyMap.month as month, alrgyMap.year as year, alrgyMap.isNoKnownAllergies as isNoKnownAllergies,"
				+ "				alrgyMap.status as status, alrgyMap.createdBy as createdBy, alrgyMap.updatedBy as updatedBy, "
				+ " 			to_char(alrgyMap.createdDate,'DD-MM-YYYY') as createdDate, to_char(alrgyMap.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "				alrgyMap.unitId as unitId, alrgyMap.organizationId as organizationId, alrgyMst.allergyDesc as allergyDesc ,"
				+ " 			alrgyMst.allergyName as allergyName, alrgyTypeMst.allergyTypeDesc as allergyTypeName,"
				+ "  (case " 
				+ "  when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
				+ "  when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
				+ "  else 'Admin' end) as userName,"
				+ "				alrgyMap.encounterId as encounterId, alrgyMap.roleId as roleId,"
				+ "				alrgyMap.isReviewedFlag as isReviewedFlag,genericMst.id as genericId,genericMst.name as genericName,alrgyMap.isEnterInErrorStatus as isEnterInErrorStatus "
				+ " FROM 		AllergyPatientMapper alrgyMap"
				+ " LEFT JOIN 	alrgyMap.allergyMaster alrgyMst "
				+ " LEFT JOIN 	alrgyMap.generic genericMst "
				+ " LEFT JOIN 	alrgyMap.allergyTypeMaster alrgyTypeMst "
				+ " LEFT JOIN 	alrgyMap.severityMaster servtyMst "
				+ " LEFT JOIN alrgyMap.userMaster um " 
				+ " LEFT JOIN um.employeeMastersList empmst " 
				+ " LEFT JOIN um.doctorMastersList  docmst "
				+ " WHERE 		alrgyMap.covTemplateId=:covTemplateId"
				+ " AND 		alrgyMap.unitId=:unitId"
				+ " AND 		alrgyMap.organizationId=:organizationId"
				+ " AND 		alrgyMap.resolvedRemark is null "
				+ " AND 		alrgyMap.status = 'A'"
		),
			@NamedQuery(name="GET_ALLERGY_PATIENT_MAPPER_FOR_BOOK_VIEW",
			query=" SELECT 		alrgyMap.allergyId as allergyId, alrgyMap.allergyPatientMapperId as allergyPatientMapperId, "
				+ "				alrgyMap.patientId as patientId, alrgyMst.allergyDesc as allergyDesc ,"
				+ "				alrgyMap.genericId as genericId, "
				+ "				alrgyMap.allergyTypeId as allergyTypeId,"
				+ "				alrgyMap.type as type, alrgyMap.natureOfReaction as natureOfReaction, alrgyMap.comments as comments,"
				+ " 			alrgyMap.severityId as severityId, servtyMst.severityDesc as severityDesc,"
				+ " 			alrgyMap.remark as remark, alrgyMap.month as month, alrgyMap.year as year, alrgyMap.isNoKnownAllergies as isNoKnownAllergies,"
				+ "				alrgyMap.status as status, alrgyMap.createdBy as createdBy, alrgyMap.updatedBy as updatedBy, "
				+ " 			to_char(alrgyMap.createdDate,'DD/MM/YYYY') as createdDate, to_char(alrgyMap.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "				alrgyMap.unitId as unitId, alrgyMap.organizationId as organizationId, "
				+ " 			alrgyMst.allergyName as allergyName, alrgyTypeMst.allergyTypeDesc as allergyTypeName,"
				+ "				alrgyMap.encounterId as encounterId, alrgyMap.roleId as roleId,"
				+ "				alrgyMap.isReviewedFlag as isReviewedFlag,genericMst.id as genericId,genericMst.name as genericName,alrgyMap.isEnterInErrorStatus as isEnterInErrorStatus "
				+ " FROM 		AllergyPatientMapper alrgyMap"
				+ " LEFT JOIN 	alrgyMap.allergyMaster alrgyMst "
				+ " LEFT JOIN 	alrgyMap.generic genericMst "
				+ " LEFT JOIN 	alrgyMap.allergyTypeMaster alrgyTypeMst "
				+ " LEFT JOIN 	alrgyMap.severityMaster servtyMst "
				+ " WHERE 		alrgyMap.patientId=:patientId"
				+ " AND 		alrgyMap.organizationId=:organizationId"
				+ " AND 		alrgyMap.resolvedRemark is null "
				+ " AND to_char(alrgyMap.createdDate , 'YYYY-MM-DD')  BETWEEN :fromDate " 
				+ " AND :toDate "
				+ " AND 		alrgyMap.status = 'A'"
		),
			
			@NamedQuery(name="GET_PATIENT_ALLERGY_LIST_FOR_ICON",
			query=" SELECT 		distinct(alrgyMap.allergyId) as allergyId, "
//					+ " alrgyMap.allergyPatientMapperId as allergyPatientMapperId, "
				+ "				alrgyMap.patientId as patientId, "
				+ "				alrgyMap.genericId as genericId, "
				+ "				alrgyMap.allergyTypeId as allergyTypeId,"
				+ " 			alrgyMst.allergyName as allergyName,"
				+ " 			alrgyMst.allergyDesc as allergyDesc, "
				+ "				alrgyTypeMst.allergyTypeDesc as allergyTypeName, "
//				+ " 			alrgyMap.remark as remark, alrgyMap.month as month, alrgyMap.year as year, "
				+ "              alrgyMap.isNoKnownAllergies as isNoKnownAllergies,"
//				+ "				alrgyMap.encounterId as encounterId, alrgyMap.roleId as roleId,"
//				+ "				alrgyMap.isReviewedFlag as isReviewedFlag,"
				+ " genericMst.id as genericId,genericMst.name as genericName "
				+ " FROM 		AllergyPatientMapper alrgyMap"
				+ " LEFT JOIN 	alrgyMap.allergyMaster alrgyMst "
				+ " LEFT JOIN 	alrgyMap.generic genericMst "
				+ " LEFT JOIN 	alrgyMap.allergyTypeMaster alrgyTypeMst "
				+ " LEFT JOIN 	alrgyMap.severityMaster servtyMst "
				+ " WHERE 		alrgyMap.patientId=:patientId"
				+ " AND 		alrgyMap.unitId=:unitId"
				+ " AND 		alrgyMap.organizationId=:organizationId"
				+ " AND 		alrgyMap.resolvedRemark is null "
				+ " AND 		alrgyMap.status = 'A'"
		),
			
			
			
			@NamedQuery(name="GET_PATIENT_ALLERGY_LIST_FOR_ICON_ABDM",
			query=" SELECT 		distinct(alrgyMap.allergyId) as allergyId, "
				+ "				alrgyMap.patientId as patientId, "
				+ "				alrgyMap.genericId as genericId, "
				+ "				alrgyMap.allergyTypeId as allergyTypeId,"
				+ " 			alrgyMst.allergyName as allergyName,"
				+ " 			alrgyMst.allergyDesc as allergyDesc, "
				+ "				alrgyTypeMst.allergyTypeDesc as allergyTypeName, "
				+ "              alrgyMap.isNoKnownAllergies as isNoKnownAllergies,"
				+ " genericMst.id as genericId,genericMst.name as genericName "
				+ " FROM 		AllergyPatientMapper alrgyMap"
				+ " LEFT JOIN 	alrgyMap.allergyMaster alrgyMst "
				+ " LEFT JOIN 	alrgyMap.generic genericMst "
				+ " LEFT JOIN 	alrgyMap.allergyTypeMaster alrgyTypeMst "
				+ " LEFT JOIN 	alrgyMap.severityMaster servtyMst "
                + " LEFT JOIN 	alrgyMap.encounterMaster encounterMst "
				+ " WHERE 		alrgyMap.patientId=:patientId"
				//+ " AND 		alrgyMap.unitId=:unitId"
				+ " AND 		alrgyMap.organizationId=:organizationId"
				+ " AND 		encounterMst.encounterId=:encounterId"
				+ " AND 		alrgyMap.resolvedRemark is null "
				+ " AND 		alrgyMap.status = 'A'"
		),
			
			
			
			
			
			@NamedQuery(name="GET_RESOLVED_LIST_ALLERGY_PATIENT_MAPPER_FOR_COVERSHEET",
			query=" SELECT 		alrgyMap.allergyId as allergyId, alrgyMap.allergyPatientMapperId as allergyPatientMapperId, "
				+ "				alrgyMap.patientId as patientId, alrgyMap.monthYear as monthYear, "
				+ "				alrgyMap.genericId as genericId, "
				+ "				alrgyMap.allergyTypeId as allergyTypeId,"
				+ "				alrgyMap.type as type, alrgyMap.natureOfReaction as natureOfReaction, alrgyMap.comments as comments,"
				+ " 			alrgyMap.severityId as severityId, servtyMst.severityDesc as severityDesc,"
				+ " 			alrgyMap.remark as remark, alrgyMap.resolvedRemark as resolvedRemark, alrgyMap.month as month, alrgyMap.year as year, alrgyMap.isNoKnownAllergies as isNoKnownAllergies,"
				+ "				alrgyMap.status as status, alrgyMap.createdBy as createdBy, alrgyMap.updatedBy as updatedBy, "
				+ " 			to_char(alrgyMap.createdDate,'DD-MM-YYYY') as createdDate, to_char(alrgyMap.resolvedDate,'DD-MM-YYYY') as resolvedDate, to_char(alrgyMap.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "				alrgyMap.unitId as unitId, alrgyMap.organizationId as organizationId, alrgyMst.allergyDesc as allergyDesc ,"
				+ " 			alrgyMst.allergyName as allergyName, alrgyTypeMst.allergyTypeDesc as allergyTypeName,"
				+ "				alrgyMap.encounterId as encounterId, alrgyMap.roleId as roleId,"
				+ "				alrgyMap.isReviewedFlag as isReviewedFlag,genericMst.id as genericId,genericMst.name as genericName,alrgyMap.isEnterInErrorStatus as isEnterInErrorStatus "
				+ " FROM 		AllergyPatientMapper alrgyMap"
				+ " LEFT JOIN 	alrgyMap.allergyMaster alrgyMst "
				+ " LEFT JOIN 	alrgyMap.generic genericMst "
				+ " LEFT JOIN 	alrgyMap.allergyTypeMaster alrgyTypeMst "
				+ " LEFT JOIN 	alrgyMap.severityMaster servtyMst "
				+ " WHERE 		alrgyMap.patientId=:patientId"
//				+ " AND 		alrgyMap.unitId=:unitId"
				+ " AND 		alrgyMap.organizationId=:organizationId"
				+ " AND 		alrgyMap.resolvedRemark is not null "
				+ " AND 		alrgyMap.status = 'A'"
		),
			
			
			@NamedQuery(name="GET_ALLERGY_PATIENT_MAPPER_FOR_DOCTOR_APP",
			query=" SELECT 		alrgyMap.allergyId as allergyId, alrgyMap.allergyPatientMapperId as allergyPatientMapperId, "
				+ "				alrgyMap.patientId as patientId, "
				+ "				alrgyMap.genericId as genericId, "
				+ "				alrgyMap.allergyTypeId as allergyTypeId,"
				+ "				alrgyMap.type as type, alrgyMap.natureOfReaction as natureOfReaction, alrgyMap.comments as comments,"
				+ " 			alrgyMap.severityId as severityId, servtyMst.severityDesc as severityDesc,"
				+ " 			alrgyMap.remark as remark, alrgyMap.month as month, alrgyMap.year as year, alrgyMap.isNoKnownAllergies as isNoKnownAllergies,"
				+ "				alrgyMap.status as status, alrgyMap.createdBy as createdBy, alrgyMap.updatedBy as updatedBy, "
				+ " 			to_char(alrgyMap.createdDate,'DD-MM-YYYY') as createdDate, to_char(alrgyMap.updatedDate,'DD-MM-YYYY') as updatedDate,"
				+ "				alrgyMap.unitId as unitId, alrgyMap.organizationId as organizationId, alrgyMst.allergyDesc as allergyDesc ,"
				+ " 			alrgyMst.allergyName as allergyName, alrgyTypeMst.allergyTypeDesc as allergyTypeName,"
				+ "  (case " 
				+ "  when empmst.employeeId is not null then concat(empmst.firstName, ' ', empmst.lastName) " 
				+ "  when docmst.doctorId is not null then concat('Dr. ', docmst.firstName, ' ', docmst.lastName) " 
				+ "  else 'Admin' end) as userName,"
				+ "				alrgyMap.encounterId as encounterId, alrgyMap.roleId as roleId,"
				+ "				alrgyMap.isReviewedFlag as isReviewedFlag,genericMst.id as genericId,genericMst.name as genericName,alrgyMap.isEnterInErrorStatus as isEnterInErrorStatus "
				+ " FROM 		AllergyPatientMapper alrgyMap"
				+ " LEFT JOIN 	alrgyMap.allergyMaster alrgyMst "
				+ " LEFT JOIN 	alrgyMap.generic genericMst "
				+ " LEFT JOIN 	alrgyMap.allergyTypeMaster alrgyTypeMst "
				+ " LEFT JOIN 	alrgyMap.severityMaster servtyMst "
				+ " LEFT JOIN alrgyMap.userMaster um " 
				+ " LEFT JOIN um.employeeMastersList empmst " 
				+ " LEFT JOIN um.doctorMastersList  docmst "
				+ " WHERE 		alrgyMap.patientId=:patientId"
				+ " AND 		alrgyMap.organizationId=:organizationId"
				+ " AND 		alrgyMap.resolvedRemark is null "
				+ " AND 		alrgyMap.status = 'A'"
		),	
		
})



@Entity
@Table(name="t_allergy_patient_mapper",schema="patient")
@SequenceGenerator(name="allergy_patient_mapp_seq" , sequenceName="patient.allergy_patient_mapp_seq", allocationSize=1)
public class AllergyPatientMapper {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "allergy_patient_mapp_seq")
	@Column(name="allergy_patient_mapp_id")
	private Integer allergyPatientMapperId;
	
	@Column(name="type")
	private String type;
	
	@Column(name="nature_of_reaction")
	private String natureOfReaction;
	
	@Column(name="comments")
	private String comments;
	
	@Column(name="severity_id")
	private Integer severityId;
	
	@Column(name="remark")
	private String remark;
	
	@Column(name="month")
	private Integer month;
	
	@Column(name="year")
	private Integer year;
	
	@Column(name="is_no_known_allergies")
	private char isNoKnownAllergies;
	
	@Column(name="status")
	private char status;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="unit_id")
	private Integer unitId;
	
	@Column(name="organization_id")
	private Integer organizationId;
	
	@Column(name="encounter_id")
	private Integer encounterId;
	
	@Column(name="admission_id")
	private Integer admissionId;
	
	@Column(name="role_id")
	private Integer roleId;
	
	@Column(name="is_reviewed_flag")
	private char isReviewedFlag;
	
	@Column(name="is_enter_in_error")
	private char isEnterInErrorStatus;
	
	@Column(name = "allergy_id")
	private Integer allergyId;
	
	@Column(name = "patient_id")
	private Integer patientId;
	
	@Column(name = "allergy_type_id")
	private Integer allergyTypeId;
	
	@Column(name = "generic_id")
	private Integer genericId;
	
	@Column(name = "month_year")
	private String monthYear;
	
	@Column(name = "resolved_Date")
	private Date resolvedDate;

	@Column(name="cov_template_id")
	private Integer covTemplateId;
	
	@Column(name="resolved_Remark")
	private String resolvedRemark;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "allergy_id", insertable = false, nullable = false, updatable = false)
	private AllergyMaster allergyMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "allergy_type_id", insertable = false, nullable = false, updatable = false)
	private AllergyTypeMaster allergyTypeMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "severity_id", insertable = false, nullable = false, updatable = false)
	private SeverityMaster severityMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "generic_id", insertable = false, nullable = false, updatable = false)
	private Generic generic;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="created_by", insertable=false, updatable=false)
	private UserMaster userMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, updatable = false)
	private EncounterMaster encounterMaster;
	
	
	public Generic getGeneric() {
		return generic;
	}

	public void setGeneric(Generic generic) {
		this.generic = generic;
	}

	public Integer getGenericId() {
		return genericId;
	}

	public void setGenericId(Integer genericId) {
		this.genericId = genericId;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Integer getAllergyId() {
		return allergyId;
	}

	public void setAllergyId(Integer allergyId) {
		this.allergyId = allergyId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public Integer getAllergyTypeId() {
		return allergyTypeId;
	}

	public void setAllergyTypeId(Integer allergyTypeId) {
		this.allergyTypeId = allergyTypeId;
	}

	public Integer getAllergyPatientMapperId() {
		return allergyPatientMapperId;
	}

	public void setAllergyPatientMapperId(Integer allergyPatientMapperId) {
		this.allergyPatientMapperId = allergyPatientMapperId;
	}

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

	public char getIsEnterInErrorStatus() {
		return isEnterInErrorStatus;
	}

	public void setIsEnterInErrorStatus(char isEnterInErrorStatus) {
		this.isEnterInErrorStatus = isEnterInErrorStatus;
	}

	public SeverityMaster getSeverityMaster() {
		return severityMaster;
	}

	public void setSeverityMaster(SeverityMaster severityMaster) {
		this.severityMaster = severityMaster;
	}

	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNatureOfReaction() {
		return natureOfReaction;
	}

	public void setNatureOfReaction(String natureOfReaction) {
		this.natureOfReaction = natureOfReaction;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Integer getSeverityId() {
		return severityId;
	}

	public void setSeverityId(Integer severityId) {
		this.severityId = severityId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getMonth() {
		return month;
	}

	public void setMonth(Integer month) {
		this.month = month;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public char getIsNoKnownAllergies() {
		return isNoKnownAllergies;
	}

	public void setIsNoKnownAllergies(char isNoKnownAllergies) {
		this.isNoKnownAllergies = isNoKnownAllergies;
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

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
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

	public AllergyMaster getAllergyMaster() {
		return allergyMaster;
	}

	public void setAllergyMaster(AllergyMaster allergyMaster) {
		this.allergyMaster = allergyMaster;
	}

	public PatientRegistration getPatientRegistration() {
		return patientRegistration;
	}

	public void setPatientRegistration(PatientRegistration patientRegistration) {
		this.patientRegistration = patientRegistration;
	}

	public AllergyTypeMaster getAllergyTypeMaster() {
		return allergyTypeMaster;
	}

	public void setAllergyTypeMaster(AllergyTypeMaster allergyTypeMaster) {
		this.allergyTypeMaster = allergyTypeMaster;
	}

	public Date getResolvedDate() {
		return resolvedDate;
	}

	public void setResolvedDate(Date resolvedDate) {
		this.resolvedDate = resolvedDate;
	}

	public String getResolvedRemark() {
		return resolvedRemark;
	}

	public void setResolvedRemark(String resolvedRemark) {
		this.resolvedRemark = resolvedRemark;
	}

	public String getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(String monthYear) {
		this.monthYear = monthYear;
	}
	
	public Integer getCovTemplateId() {
		return covTemplateId;
	}

	public void setCovTemplateId(Integer covTemplateId) {
		this.covTemplateId = covTemplateId;

	}  
	
}
