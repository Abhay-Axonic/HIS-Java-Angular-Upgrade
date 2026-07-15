package com.param.entity.lis.histo;

import java.math.BigInteger;
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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.entity.lis.common.LocalTimeConverter;




@NamedQueries({

/*	@NamedQuery(name = "GET_SPCIMEN_LIST", query = "SELECT tSpecimanMaster.tSpecimanId AS tSpecimanId,"
			+ " tSpecimanMaster.orgId AS orgId,"
			+ " tSpecimanMaster.orgUnitId AS orgUnitId,"
			+ " tSpecimanMaster.specimanExaminination AS specimanExaminination,"
			+ " tSpecimanMaster.labSampleDtlsId AS labSampleDtlsId,"
			+ " tSpecimanMaster.histopathlogyNumber AS histopathlogyNumber,"
			+ " tSpecimanMaster.barcodeNo AS barcodeNo,"
			+ " tSpecimanMaster.specimanTypeId AS specimanTypeId,"
			+ " tSpecimanMaster.specimanId AS specimanId,"
			+ " tSpecimanMaster.specimanName AS specimanName "
			+ " FROM TSpecimanMaster tSpecimanMaster "
			+ " WHERE tSpecimanMaster.orgId = :orgId "
			+ " AND tSpecimanMaster.orgUnitId = :orgUnitId"
			+ " AND tSpecimanMaster.isDeleted ='N'"),*/
  
  
  @NamedQuery(name = "GET_SUB_SPCIMEN_LIST", query = " SELECT "
         /* +" tSubSpecimanMaster.tSubSpecimanId AS tSubSpecimanId, "*/
          +" to_char(ordermst.orderDate,'dd-MM-yyyy') AS orderDate, "
          +" visitMst.visitTypeCode AS visitType, "
          +" speciMst.desc AS specimanType, "
          +" subspemst.subSpecialityMasterName AS deptName, "
          +" labSampleMst.visitTypeId AS visitTypeId, "
          +" testMst.testDesc AS testDesc, "
          +" speciMst.specimanId AS specimanId, "
          +" tSpecimanMaster.orgId AS orgId, "
          +" tSpecimanMaster.orgUnitId AS orgUnitId, "
          +" tSpecimanMaster.labSampleDtlsId AS labSampleDtlsId, "
          +" tSpecimanMaster.tSpecimanId AS tSpecimanId, "
       /*   +" tSubSpecimanMaster.subSpecimanExaminination AS subSpecimanExaminination, "
          +" tSubSpecimanMaster.labSampleDtlsId AS labSampleDtlsId, "*/
          +" tSpecimanMaster.histopathlogyNumber AS histopathlogyNumber, "
        /*  +" tSubSpecimanMaster.subSpecimanBarcode AS subSpecimanBarcode, "*/
          +" tSpecimanMaster.specimanTypeId AS specimanTypeId, "
          +" tSpecimanMaster.specimanId AS specimanId, "
          +" tSpecimanMaster.specimanName AS specimanName, "
          +" patientReg.uhidNumber AS uhid, "
          +" genderMst.desc AS genderName, "
          +" genderMst.code AS genderCode, "
          +" genderMst.id AS genderId, "
          +" patientReg.patientId AS patientId, "
          +" doctorMst.doctorId AS doctorId, "
          +" patientReg.dob AS dob, "
          +"  billMst.billNumber as billNumber, "
          +" patientReg.imagePath AS imagePath, "
          + "to_char(addetails.doa,'dd/MM/yyyy') as doa, " 
          + "to_char(addetails.pdd,'dd/MM/yyyy') as pdd, "
          +" prefixMst.prefixCode || ' ' || patientReg.firstName ||' ' || coalesce(patientReg.middleName) ||' ' || patientReg.lastName AS patientDetails, "
          +" 'Dr.' || ' ' || doctorMst.firstName || ' ' || doctorMst.lastName AS doctorDetails, "
          +" bedMst.bedNumber AS bedNumber, "  
          +" wardmst.wardCode AS wardCode, "
          + "lstTTemplateRes.templateResId AS templateResId, "
          + "testMst.isHistoCyto as isHistoCyto ,"
          + "labSampleDetailsMst.orderDetailsId as orderDetailsId, "
          + " (CASE WHEN encPaymentEntitlementMaster.paymentEntitlementDesc is not null  THEN "
          + " encPaymentEntitlementMaster.paymentEntitlementDesc "
          + " ELSE admiPaymentEntitlementMaster.paymentEntitlementDesc  END)  AS entitlementDesc ,"
          + "ordermst.encounterId  as encounterId ,"
          + "ordermst.admissionId  as admissionId "
          +" FROM "
          +"  TSpecimanMaster tSpecimanMaster "
         /* +" INNER JOIN tSpecimanMaster.listTSubSpecimanMaster tSubSpecimanMaster "*/
          +" INNER JOIN tSpecimanMaster.labSampleDetailsMaster labSampleDetailsMst "
          +" INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
          +" INNER JOIN labSampleMst.orderMaster ordermst "
          +" INNER JOIN labSampleDetailsMst.testMaster testMst "
          +" INNER JOIN testMst.specimanMaster speciMst "
          +" INNER JOIN testMst.subSpecialityMaster subspemst   "
          +" INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
          +" INNER JOIN labSampleMst.visitTypeMaster visitMst "
          +" INNER JOIN labSampleDetailsMst.patientRegistration patientReg "
          +" INNER JOIN patientReg.prefixMaster prefixMst "
          +" INNER JOIN labSampleDetailsMst.genderMaster genderMst "
          +" INNER JOIN labSampleDetailsMst.orderDetailsMaster ordDtl "
          + " left join ordDtl.billingDetailsList billDtl "
          + " left join billDtl.billingMaster billMst "
          +" LEFT JOIN labSampleDetailsMst.doctorMaster doctorMst "
          +" LEFT JOIN ordermst.admission admsion "
          +" LEFT JOIN admsion.admissionDetailsList addetails WITH addetails.nursingAcceptance = 'Y' AND addetails.status = 'A' "
          +" LEFT JOIN addetails.bedMaster bedMst "
          +" LEFT JOIN addetails.wardMaster wardmst "
          +" LEFT JOIN labSampleDetailsMst.listTTemplateResult lstTTemplateRes "
          +" LEFT JOIN ordermst.encounterMaster encounterMaster "
          +" LEFT JOIN encounterMaster.patientSponsorDetailsList encounterPatientSponsorDetailsList  with encounterPatientSponsorDetailsList.priorityId = 1  and encounterPatientSponsorDetailsList.status = 'A' "
          +" LEFT JOIN encounterPatientSponsorDetailsList.paymentEntitlementMaster encPaymentEntitlementMaster "
          +" LEFT JOIN admsion.patientSponsorDetailsList adtPatientSponsorDetailsList  with adtPatientSponsorDetailsList.priorityId = 1  and adtPatientSponsorDetailsList.status = 'A' "
          +" LEFT JOIN adtPatientSponsorDetailsList.paymentEntitlementMaster admiPaymentEntitlementMaster "
          +" WHERE "
          +"tSpecimanMaster.orgId = :orgId "
		  +" AND tSpecimanMaster.orgUnitId = :orgUnitId "
		  +" AND tSpecimanMaster.isDeleted = 'N' "
          +" AND labSampleDetailsMst.sampleStatusId in (:sampleStatusIds) AND  testMst.isHistoCyto IN ('H','C')"
		   ),
  
  @NamedQuery(name = "GETMICROSCOPICEXAMINITON", query = " SELECT "
          +" tSubSpecimanMaster.tSubSpecimanId AS tSubSpecimanId, "
          +" to_char(ordermst.orderDate,'dd-MM-yyyy') AS orderDate, "
          +" visitMst.visitTypeCode AS visitType, "
          +" speciMst.desc AS specimanType, "
          +" subspemst.subSpecialityMasterName AS deptName, "
          +" labSampleMst.visitTypeId AS visitTypeId, "
          +" testMst.testDesc AS testDesc, "
          +" speciMst.specimanId AS specimanId, "
          +" tSpecimanMaster.orgId AS orgId, "
          +" tSpecimanMaster.orgUnitId AS orgUnitId, "
          +" tSubSpecimanMaster.subSpecimanExaminination AS subSpecimanExaminination, "
          +" tSubSpecimanMaster.labSampleDtlsId AS labSampleDtlsId, "
          +" tSpecimanMaster.histopathlogyNumber AS histopathlogyNumber, "
          +" tSubSpecimanMaster.subSpecimanBarcode AS subSpecimanBarcode, "
          +" tSpecimanMaster.specimanTypeId AS specimanTypeId, "
          +" tSpecimanMaster.specimanId AS specimanId, "
          +" tSpecimanMaster.specimanName AS specimanName, "
          +" patientReg.uhidNumber AS uhid, "
          +" genderMst.desc AS genderName, "
          +" genderMst.code AS genderCode, "
          +" patientReg.dob AS dob, "
          + "to_char(addetails.doa,'dd/MM/yyyy') as doa, " 
          + "to_char(addetails.pdd,'dd/MM/yyyy') as pdd, "
          +" prefixMst.prefixCode || ' ' || patientReg.firstName || ' ' || patientReg.lastName AS patientDetails, "
          +" 'Dr.' || ' ' || doctorMst.firstName || ' ' || doctorMst.lastName AS doctorDetails, "
          +" bedMst.bedNumber AS bedNumber, "  
          +" wardmst.wardCode AS wardCode, "
          + "lstTTemplateRes.templateResId AS templateResId, "
          + "testMst.isHistoCyto as isHistoCyto ,"
          + "labSampleDetailsMst.orderDetailsId as orderDetailsId "
          +" FROM "
          +"  TSpecimanMaster tSpecimanMaster "
          +" INNER JOIN tSpecimanMaster.listTSubSpecimanMaster tSubSpecimanMaster "
          +" INNER JOIN tSubSpecimanMaster.labSampleDetailsMaster labSampleDetailsMst "
          +" INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
          +" INNER JOIN labSampleMst.orderMaster ordermst "
          +" INNER JOIN labSampleDetailsMst.testMaster testMst "
          +" INNER JOIN testMst.specimanMaster speciMst "
          +" INNER JOIN testMst.subSpecialityMaster subspemst   "
          +" INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
          +" INNER JOIN labSampleMst.visitTypeMaster visitMst "
          +" INNER JOIN labSampleDetailsMst.patientRegistration patientReg "
          +" INNER JOIN patientReg.prefixMaster prefixMst "
          +" INNER JOIN labSampleDetailsMst.genderMaster genderMst "
          +" LEFT JOIN labSampleDetailsMst.doctorMaster doctorMst "
          +" LEFT JOIN ordermst.admission admsion "
          +" LEFT JOIN admsion.admissionDetailsList addetails WITH addetails.nursingAcceptance = 'Y' AND addetails.status = 'A' "
          +" LEFT JOIN addetails.bedMaster bedMst "
          +" LEFT JOIN addetails.wardMaster wardmst "
          +" LEFT JOIN labSampleDetailsMst.listTTemplateResult lstTTemplateRes"
          +" WHERE "
          +"  tSubSpecimanMaster.isDeleted = 'N' "
          +" AND labSampleDetailsMst.labSampleDtlsId = :labSampleDetailsId AND  testMst.isHistoCyto IN ('H','C')"
		   ),

@NamedQuery(name = "GET_SUB_SPCIMEN_LIST_COUNT", query = 
       " SELECT COUNT(*)"
		+" from TSpecimanMaster tSpecimanMaster "
		+" INNER JOIN tSpecimanMaster.listTSubSpecimanMaster tSubSpecimanMaster "
	    +" INNER JOIN tSubSpecimanMaster.labSampleDetailsMaster labSampleDetailsMst "
	    +" INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
	    +" INNER JOIN labSampleMst.orderMaster ordermst "
	    +" INNER JOIN labSampleDetailsMst.testMaster testMst "
	    +" INNER JOIN testMst.specimanMaster speciMst "
	    +" INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
	    +" INNER JOIN labSampleMst.visitTypeMaster visitMst "
	    +" INNER JOIN labSampleDetailsMst.patientRegistration patientReg "
	    +" INNER JOIN patientReg.prefixMaster prefixMst "
	    +" INNER JOIN labSampleDetailsMst.genderMaster genderMst "
        +" WHERE tSubSpecimanMaster.orgId = :orgId "
        +" AND tSubSpecimanMaster.orgUnitId = :orgUnitId"
        +" AND tSubSpecimanMaster.isDeleted ='N'"
        +" AND labSampleDetailsMst.sampleStatusId in (:sampleStatusIds) AND  testMst.isHistoCyto IN ('H','C')"),
  
  
	
	@NamedQuery(name = "GET_GROSS_SPCIMEN_LIST", query = " SELECT "
			/* +"	tSubSpecimanMaster.tSubSpecimanId AS tSubSpecimanId, "*/
             +"  to_char(ordermst.orderDate,'dd-MM-yyyy') AS orderDate, "
             + "  to_char(ordermst.orderDate, 'DD/MM/YYYY HH24:MI:SS') AS orderDateAndTimeString,  "
			 +" ordermst.orderDate AS orderDateAndTime, "
             +"  billMst.billNumber as billNumber, "
			 +"	visitMst.visitTypeCode AS visitType, "
			 +" testMst.testDesc AS testDesc, "
			 +"	tSpecimanMaster.histopathlogyNumber AS histopathlogyNumber, "
		     +"	speciMst.desc AS specimanType, "
			 +"	tSpecimanMaster.orgId AS orgId, "
			 +"	tSpecimanMaster.tSpecimanId AS tSpecimanId, "
			 +"	tSpecimanMaster.labSampleDtlsId AS labSampleDtlsId, "
		/*	 +"	tSubSpecimanMaster.subSpecimanExaminination AS subSpecimanExaminination, "
			 +"	tSubSpecimanMaster.subSpecimanName AS subSpecimanName, "
			 +"	tSubSpecimanMaster.labSampleDtlsId AS labSampleDtlsId, "
			 +"	tSubSpecimanMaster.subSpcimanNo AS subSpcimanNo, "
			 +"	tSubSpecimanMaster.subSpecimanBarcode AS subSpecimanBarcode, "*/
			/* +"	tSubSpecimanMaster.captureNote AS captureNote, "*/
			 +"	tSpecimanMaster.specimanTypeId AS specimanTypeId, "
			 +"	tSpecimanMaster.specimanId AS specimanId, "
			 +"	tSpecimanMaster.specimanName AS specimanName, "
			 +"	patientReg.uhidNumber AS uhid, "
			 +"	genderMst.desc AS genderName, "
			 +"	genderMst.id AS genderId, "
			 +"	patientReg.imagePath AS imagePath, "
			 +"	patientReg.dob AS dob, "
			 +"	prefixMst.prefixCode || ' ' || patientReg.firstName ||' '|| coalesce(patientReg.middleName) || ' ' || patientReg.lastName AS patientDetails, "
			 +"	'Dr.' || ' ' || doctorMst.firstName || ' ' || doctorMst.lastName AS doctorDetails, "
			 +" bedMst.bedNumber AS bedNumber, "  
	         +" wardmst.wardCode AS wardCode ,"
	         + "testMst.isHistoCyto as isHistoCyto, "
             + " (CASE WHEN encPaymentEntitlementMaster.paymentEntitlementDesc is not null  THEN "
             + " encPaymentEntitlementMaster.paymentEntitlementDesc "
             + " ELSE admiPaymentEntitlementMaster.paymentEntitlementDesc  END)  AS entitlementDesc "
			 +" FROM   TSpecimanMaster tSpecimanMaster "
			 //+" INNER JOIN tSpecimanMaster.listTSubSpecimanMaster tSubSpecimanMaster "
			 +" INNER JOIN tSpecimanMaster.labSampleDetailsMaster labSampleDetailsMst "
			 +" INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
			 +" INNER JOIN labSampleMst.orderMaster ordermst "
			 +" INNER JOIN labSampleDetailsMst.testMaster testMst   "
			 +" INNER JOIN testMst.specimanMaster speciMst "
			 +" INNER JOIN labSampleMst.visitTypeMaster visitMst "
			 +" INNER JOIN labSampleDetailsMst.patientRegistration patientReg "
			 +" INNER JOIN patientReg.prefixMaster prefixMst "
			 +" INNER JOIN labSampleDetailsMst.genderMaster genderMst "
             +"INNER JOIN labSampleDetailsMst.orderDetailsMaster ordDtl "

			 +" LEFT JOIN labSampleDetailsMst.doctorMaster doctorMst "
	         +" LEFT JOIN ordermst.admission admsion "
	         +" LEFT JOIN admsion.admissionDetailsList addetails WITH addetails.nursingAcceptance = 'Y' AND addetails.status = 'A' "
	         +" LEFT JOIN addetails.bedMaster bedMst "
	         +" LEFT JOIN addetails.wardMaster wardmst "
             + " left join ordDtl.billingDetailsList billDtl "
             + " left join billDtl.billingMaster billMst "
             +"LEFT JOIN ordermst.encounterMaster encounterMaster "
             +"LEFT JOIN encounterMaster.patientSponsorDetailsList encounterPatientSponsorDetailsList  with encounterPatientSponsorDetailsList.priorityId = 1  and encounterPatientSponsorDetailsList.status = 'A' "
             +"LEFT JOIN encounterPatientSponsorDetailsList.paymentEntitlementMaster encPaymentEntitlementMaster "
             +"LEFT JOIN admsion.patientSponsorDetailsList adtPatientSponsorDetailsList  with adtPatientSponsorDetailsList.priorityId = 1  and adtPatientSponsorDetailsList.status = 'A' "
             +"LEFT JOIN adtPatientSponsorDetailsList.paymentEntitlementMaster admiPaymentEntitlementMaster "
			 +" WHERE "
			 +"	tSpecimanMaster.orgId = :orgId "
			 +"	AND tSpecimanMaster.orgUnitId = :orgUnitId "
			 +"	AND tSpecimanMaster.isDeleted = 'N' "
			 +"	AND (labSampleDetailsMst.sampleStatusId = :histoSampleStatus AND  testMst.isHistoCyto = 'H')"),
	
		/*
		 * OR" + " (labSampleDetailsMst.sampleStatusId = :cytoSampleStatus AND
		 * testMst.isHistoCyto = 'C') OR " + 
			 "  (labSampleDetailsMst.sampleStatusId = :cytoSampleStatus AND  testMst.isHistoCyto = 'C') "
		 */	
	@NamedQuery(name = "GET_GROSS_SPCIMEN_LIST_COUNT", query = "SELECT COUNT(*)"
			 + " FROM TSpecimanMaster tSpecimanMaster "
			 +" INNER JOIN tSpecimanMaster.listTSubSpecimanMaster tSubSpecimanMaster "
			 +" INNER JOIN tSpecimanMaster.labSampleDetailsMaster labSampleDetailsMst "
			 +" INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
			 +" INNER JOIN labSampleMst.orderMaster ordermst "
			 +" INNER JOIN labSampleDetailsMst.testMaster testMst   "
			 +" INNER JOIN testMst.specimanMaster speciMst "
			 +" INNER JOIN labSampleMst.visitTypeMaster visitMst "
			 +" INNER JOIN labSampleDetailsMst.patientRegistration patientReg "
			 +" INNER JOIN patientReg.prefixMaster prefixMst "
			 +" INNER JOIN labSampleDetailsMst.genderMaster genderMst "
			 +" WHERE tSpecimanMaster.orgId = :orgId "
			 +" AND tSpecimanMaster.orgUnitId = :orgUnitId"
			 +" AND tSpecimanMaster.isDeleted ='N' "
			 +" AND (labSampleDetailsMst.sampleStatusId = :histoSampleStatus AND  testMst.isHistoCyto = 'H')"),
	
	
	
	
	@NamedQuery(name = "GET_SLIDE_CREATION_LIST", query = " SELECT "
			/* +"	tSubSpecimanMaster.tSubSpecimanId AS tSubSpecimanId, "*/
             +"  billMst.billNumber as billNumber, "
			 +"	to_char(ordermst.orderDate,'dd-MM-yyyy') AS orderDate, "
			 + "  to_char(ordermst.orderDate, 'DD/MM/YYYY HH24:MI:SS') AS orderDateAndTimeString,  "
			 +" ordermst.orderDate AS orderDateAndTime, "
			 +"	visitMst.visitTypeCode AS visitType, "
			 +" testMst.testDesc AS testDesc, "
			 +" subspemst.subSpecialityMasterName AS deptName, "
			 +"	tSpecimanMaster.histopathlogyNumber AS histopathlogyNumber, "
		     +"	speciMst.desc AS specimanType, "
			 +"	tSpecimanMaster.orgId AS orgId, "
			 +"	tSpecimanMaster.tSpecimanId AS tSpecimanId, "
			 +"	tSpecimanMaster.labSampleDtlsId AS labSampleDtlsId, "
			 /*+"	tSubSpecimanMaster.subSpecimanExaminination AS subSpecimanExaminination, "
			 +"	tSubSpecimanMaster.subSpecimanName AS subSpecimanName, "
			 +"	tSubSpecimanMaster.labSampleDtlsId AS labSampleDtlsId, "
			 +"	tSubSpecimanMaster.subSpcimanNo AS subSpcimanNo, "
			 +"	tSubSpecimanMaster.subSpecimanBarcode AS subSpecimanBarcode, "
			 +"	tSubSpecimanMaster.captureNote AS captureNote, "*/
			 +"	tSpecimanMaster.specimanTypeId AS specimanTypeId, "
			 +"	tSpecimanMaster.specimanId AS specimanId, "
			 +"	tSpecimanMaster.specimanName AS specimanName, "
			 +"	patientReg.uhidNumber AS uhid, "
			 +"	genderMst.desc AS genderName, "
			 +"	genderMst.id AS genderId, "
			 +"	patientReg.imagePath AS imagePath, "
			 +"	patientReg.dob AS dob, "
			 +"	prefixMst.prefixCode || ' ' || patientReg.firstName ||' '|| coalesce(patientReg.middleName) ||' ' || patientReg.lastName AS patientDetails, "
			 +"	'Dr.' || ' ' || doctorMst.firstName || ' ' || doctorMst.lastName AS doctorDetails, "
			 +" bedMst.bedNumber AS bedNumber, "  
	         +" wardmst.wardCode AS wardCode ,"
	         + "testMst.isHistoCyto as isHistoCyto,"
             + " (CASE WHEN encPaymentEntitlementMaster.paymentEntitlementDesc is not null  THEN "
             + " encPaymentEntitlementMaster.paymentEntitlementDesc "
             + " ELSE admiPaymentEntitlementMaster.paymentEntitlementDesc  END)  AS entitlementDesc "
			 +" FROM   TSpecimanMaster tSpecimanMaster "
			/* +" INNER JOIN tSpecimanMaster.listTSubSpecimanMaster tSubSpecimanMaster "*/
			 +" INNER JOIN tSpecimanMaster.labSampleDetailsMaster labSampleDetailsMst "
			 +" INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
			 +" INNER JOIN labSampleMst.orderMaster ordermst "
			 +" INNER JOIN labSampleDetailsMst.testMaster testMst   "
			 +" INNER JOIN testMst.subSpecialityMaster subspemst   "
			 +" INNER JOIN testMst.specimanMaster speciMst "
			 +" INNER JOIN labSampleMst.visitTypeMaster visitMst "
			 +" INNER JOIN labSampleDetailsMst.patientRegistration patientReg "
			 +" INNER JOIN patientReg.prefixMaster prefixMst "
			 +" INNER JOIN labSampleDetailsMst.genderMaster genderMst "
             +"INNER JOIN labSampleDetailsMst.orderDetailsMaster ordDtl "
			 +" LEFT JOIN labSampleDetailsMst.doctorMaster doctorMst "
	         +" LEFT JOIN ordermst.admission admsion "
	         + " left join ordDtl.billingDetailsList billDtl "
             + " left join billDtl.billingMaster billMst "
	         +" LEFT JOIN admsion.admissionDetailsList addetails WITH addetails.nursingAcceptance = 'Y' AND addetails.status = 'A' "
	         +" LEFT JOIN addetails.bedMaster bedMst "
	         +" LEFT JOIN addetails.wardMaster wardmst "
             +"LEFT JOIN ordermst.encounterMaster encounterMaster "
             +"LEFT JOIN encounterMaster.patientSponsorDetailsList encounterPatientSponsorDetailsList  with encounterPatientSponsorDetailsList.priorityId = 1  and encounterPatientSponsorDetailsList.status = 'A' "
             +"LEFT JOIN encounterPatientSponsorDetailsList.paymentEntitlementMaster encPaymentEntitlementMaster "
             +"LEFT JOIN admsion.patientSponsorDetailsList adtPatientSponsorDetailsList  with adtPatientSponsorDetailsList.priorityId = 1   and adtPatientSponsorDetailsList.status = 'A'  "
             +"LEFT JOIN adtPatientSponsorDetailsList.paymentEntitlementMaster admiPaymentEntitlementMaster "
			 +" WHERE "
			 +"	tSpecimanMaster.orgId = :orgId "
			 +"	AND tSpecimanMaster.orgUnitId = :orgUnitId "
			 +"	AND tSpecimanMaster.isDeleted = 'N' "
			 ),
	
		/*
		 * OR" + " (labSampleDetailsMst.sampleStatusId = :cytoSampleStatus AND
		 * testMst.isHistoCyto = 'C') OR " + 
			 "  (labSampleDetailsMst.sampleStatusId = :cytoSampleStatus AND  testMst.isHistoCyto = 'C') "
		 */	
	@NamedQuery(name = "GET_SLIDE_CREATION_LIST_COUNT", query = "SELECT COUNT(*)"
			 + " FROM TSpecimanMaster tSpecimanMaster "
			 +" INNER JOIN tSpecimanMaster.listTSubSpecimanMaster tSubSpecimanMaster "
			 +" INNER JOIN tSpecimanMaster.labSampleDetailsMaster labSampleDetailsMst "
			 +" INNER JOIN labSampleDetailsMst.labSampleMaster labSampleMst "
			 +" INNER JOIN labSampleMst.orderMaster ordermst "
			 +" INNER JOIN labSampleDetailsMst.testMaster testMst   "
			 +" INNER JOIN testMst.specimanMaster speciMst "
			 +" INNER JOIN labSampleMst.visitTypeMaster visitMst "
			 +" INNER JOIN labSampleDetailsMst.patientRegistration patientReg "
			 +" INNER JOIN patientReg.prefixMaster prefixMst "
			 +" INNER JOIN labSampleDetailsMst.genderMaster genderMst "
			 +" WHERE tSpecimanMaster.orgId = :orgId "
			 +" AND tSpecimanMaster.orgUnitId = :orgUnitId"
			 +" AND tSpecimanMaster.isDeleted ='N' "
			 +" "),
	
	
	@NamedQuery(name = "GET_MICROSCOPIC_EXAM_DATA_BY_SUB_SPCIMEN_ID", query = "SELECT tSubSpecimanMaster "
	        + " FROM TSubSpecimanMaster tSubSpecimanMaster "
	        + " WHERE tSubSpecimanMaster.orgId =:orgId "
	        + " AND tSubSpecimanMaster.orgUnitId =:orgUnitId"
	        + " AND tSubSpecimanMaster.tSubSpecimanId =:tSubSpecimanId"
	        ),
	
	

	@NamedQuery(name = "GET_SUB_SPECIMAN_BY_LAB_SAMPLEDTLS_ID", query = "SELECT tSubSpecimanMaster.tSubSpecimanId AS tSubSpecimanId "
	        + " FROM TSubSpecimanMaster tSubSpecimanMaster "
	        + " WHERE tSubSpecimanMaster.orgId =:orgId "
	        + " AND tSubSpecimanMaster.orgUnitId =:orgUnitId"
	        + " AND tSubSpecimanMaster.labSampleDtlsId =:labSampleDtlsId"
	        ),
	
	
	/*@NamedQuery(name = "GET_PATIENT_SEARCH_BY_SPCIMAN", query = ""),*/
	
	@NamedQuery(name = "GET_SUB_SPECIMAN_DETAIL", 
	query = "SELECT tSubSpecimanMaster.tSubSpecimanId AS tSubSpecimanId, "
    + "tSubSpecimanMaster.subSpecimanName AS subSpecimanName, "
    + "tSubSpecimanMaster.labSampleDtlsId AS labSampleDtlsId, "
    + "tSubSpecimanMaster.subSpcimanNo AS subSpcimanNo, "
    + "tSpecimanMst.desc AS specimanType "
    + "FROM TSpecimanMaster tSpecimanMaster "
    + "INNER JOIN tSpecimanMaster.listTSubSpecimanMaster tSubSpecimanMaster "
    + "INNER JOIN tSpecimanMaster.labSampleDetailsMaster labSampleDetailsMst "
    + "INNER JOIN tSpecimanMaster.specimanMaster tSpecimanMst "
    + "WHERE tSpecimanMaster.tSpecimanId = :tSpecimanId"),

	
	@NamedQuery(name = "GET_SUB_SPECIMAN_BY_LAB_SAMPLE_DETAILS_ID", 
	query = "SELECT tSubSpecimanMaster.labSampleDtlsId AS labSampleDtlsId,"
			+ "tSubSpecimanMaster.tSubSpecimanId AS tSubSpecimanId, "
			+ "tSubSpecimanMaster.subSpecimanName AS subSpecimanName "
	        + " FROM TSubSpecimanMaster tSubSpecimanMaster "
	        + " WHERE tSubSpecimanMaster.orgId =:orgId "
	        + " AND tSubSpecimanMaster.orgUnitId =:orgUnitId"
	        + " AND tSubSpecimanMaster.tSubSpecimanId =:tSubSpecimanId"),
	
})


@Entity
@Table(name = "t_sub_speciman_master", schema = "lab")
@SequenceGenerator(name = "t_seq_sub_speciaman_master", sequenceName = "lab.t_seq_sub_speciaman_master", allocationSize = 1)
public class TSubSpecimanMaster {
	

	@Id
	@Column(name = "t_sub_speciman_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_seq_sub_speciaman_master")
	private int tSubSpecimanId;
	
	@Column(name = "org_id")
	private Integer orgId;

	@Column(name = "org_unit_id")
	private Integer orgUnitId;
	
	@Column(name = "t_speciman_id")
	private Integer tSpecimanId;
	
	@Column(name = "sub_speciman_name")
	private String subSpecimanName;
	
	@Column(name = "sub_speciman_examination")
	private String subSpecimanExaminination;

	@Column(name = "lab_sample_dtls_id")
	private Integer labSampleDtlsId;
	
	@Column(name = "sub_spciman_no")
	private String subSpcimanNo;
	
	@Column(name = "sub_speciman_barcode")
	private String subSpecimanBarcode;
	
	@Column(name = "capture_note")
	private String captureNote;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "sub_speciman_num")
	private BigInteger subSpecimanNum;
	
	@Column(name = "created_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long createdDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "updated_date")
	@Convert(converter = LocalTimeConverter.class)
	private Long updatedDate;
	
	@Column(name = "is_deleted")
	private Character isDeleted;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lab_sample_dtls_id", insertable = false, nullable = false, updatable = false)
	private LabSampleDetailsMaster labSampleDetailsMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "t_speciman_id", insertable = false, nullable = false, updatable = false)
	private TSpecimanMaster tSpecimanMaster;  
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "tSubSpecimanMaster")
	private List<TGrossMaster> listTGrossMaster;

	public int gettSubSpecimanId() {
		return tSubSpecimanId;
	}

	public void settSubSpecimanId(int tSubSpecimanId) {
		this.tSubSpecimanId = tSubSpecimanId;
	}

	public Integer getOrgId() {
		return orgId;
	}

	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}

	public Integer getOrgUnitId() {
		return orgUnitId;
	}

	public void setOrgUnitId(Integer orgUnitId) {
		this.orgUnitId = orgUnitId;
	}

	public Integer gettSpecimanId() {
		return tSpecimanId;
	}

	public void settSpecimanId(Integer tSpecimanId) {
		this.tSpecimanId = tSpecimanId;
	}

	public String getSubSpecimanExaminination() {
		return subSpecimanExaminination;
	}

	public void setSubSpecimanExaminination(String subSpecimanExaminination) {
		this.subSpecimanExaminination = subSpecimanExaminination;
	}

	public Integer getLabSampleDtlsId() {
		return labSampleDtlsId;
	}

	public void setLabSampleDtlsId(Integer labSampleDtlsId) {
		this.labSampleDtlsId = labSampleDtlsId;
	}

	public String getSubSpcimanNo() {
		return subSpcimanNo;
	}

	public void setSubSpcimanNo(String subSpcimanNo) {
		this.subSpcimanNo = subSpcimanNo;
	}

	public String getSubSpecimanBarcode() {
		return subSpecimanBarcode;
	}

	public void setSubSpecimanBarcode(String subSpecimanBarcode) {
		this.subSpecimanBarcode = subSpecimanBarcode;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Character getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Character isDeleted) {
		this.isDeleted = isDeleted;
	}

	public LabSampleDetailsMaster getLabSampleDetailsMaster() {
		return labSampleDetailsMaster;
	}

	public void setLabSampleDetailsMaster(LabSampleDetailsMaster labSampleDetailsMaster) {
		this.labSampleDetailsMaster = labSampleDetailsMaster;
	}

	public TSpecimanMaster gettSpecimanMaster() {
		return tSpecimanMaster;
	}

	public void settSpecimanMaster(TSpecimanMaster tSpecimanMaster) {
		this.tSpecimanMaster = tSpecimanMaster;
	}

	public List<TGrossMaster> getListTGrossMaster() {
		return listTGrossMaster;
	}

	public void setListTGrossMaster(List<TGrossMaster> listTGrossMaster) {
		this.listTGrossMaster = listTGrossMaster;
	}

	public String getCaptureNote() {
		return captureNote;
	}

	public void setCaptureNote(String captureNote) {
		this.captureNote = captureNote;
	}

	public String getSubSpecimanName() {
		return subSpecimanName;
	}

	public void setSubSpecimanName(String subSpecimanName) {
		this.subSpecimanName = subSpecimanName;
	}

	public BigInteger getSubSpecimanNum() {
		return subSpecimanNum;
	}

	public void setSubSpecimanNum(BigInteger subSpecimanNum) {
		this.subSpecimanNum = subSpecimanNum;
	}

}
