package com.param.billing.global.transaction.model;

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
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.admission.model.Admission;
import com.param.global.common.DateConverter;
import com.param.global.model.PatientRegistration;
import com.param.opd.coversheet.model.EncounterMaster;

/*@NamedNativeQueries({
	@NamedNativeQuery(name="GET_BILL_DETAILS_FOR_PAYMENT",
			query=" select billing.billing_master_id as \"billingMasterId\",coalesce(sum(paymentDtls.amount),0) as \"paidAmount\","
				+ "  billing.concession_amount as \"concessionAmount\",billing.net_amount AS \"netAmount\" "
				 + " from billing.m_billing_master billing "  
				 + "	left join billing.t_billing_payment_details paymentDtls" 
				 + "		on paymentDtls.billing_master_id=billing.billing_master_id" 
				 + "	inner join opd.t_encounter_master encounter"
				 + "		on encounter.encounter_id = billing.encounter_id"
			     + " where billing.billing_master_id=:billMasterId"
				 + " group by billing.billing_master_id "),
	
})*/

@NamedQueries({
		

		@NamedQuery(name = "GET_BILL_DETAILS_BY_BILL_ID", 
		query = "SELECT bill.billingMasterId as billingMasterId, "
				+ "billDtls.billingDetailsId as billingDetailsId, " 
				+ "ordMast.orderNo as orderNo, "
				+ "billDtls.serviceId as serviceId, "
				+ "ordMast.saleId as saleId, "
				+ "ordMast.orderId as orderId, "
				+ "serviceMstr.serviceStandardName as serviceStandardName, "
				+ "serviceMstr.serviceStandardCode as serviceStandardCode, "
				+ "billDtls.ordDocId as doctorId, "
				+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
				+ "billDtls.quantity as quantity, " 
				+ "billDtls.rate as unitPrice, "
				+ "billDtls.grossAmount as grossAmount, " 
				+ "billDtls.selfPayable as selfPayable, " 
				+ "billDtls.creditPayable as creditPayable, " 
				+ "ordDtls.packageConsumptionAmt as packageConsumptionAmt, "
				+ "billDtls.payeeId as payeeId, " 
				+ "billDtls.concession as concession, "
				+ "billDtls.discount as discount, " 
				+ "unitService.isDiscount as isDiscountApplicable, "
				+ "billDtls.taxId as taxId, "
				+ "billDtls.coPayPercentage as coPayPer, "
				+ "tax.name as taxName, "
				+ "billDtls.taxPer as taxPercentage, " 
				+ "billDtls.taxAmount as taxAmount, "
				+ "billDtls.netAmt as netAmt, " 
                + "specialityMast.specialityId as specialityId ,  "
				+ "specialityMast.specialityName as specialityName ,  "
                + " bill.roundOff as roundOff, "
				+ "to_char(ordDtls.orderDate,'dd/mm/yyyy') as orderDateString, "
				+ " ordDtls.orderDetailsId as orderDetailsId, "
				+ " ordDtls.serviceRendered as serviceRendered ,"
				+ "ordDtls.isPackageService as isPackageService  "
				+ "FROM BillingMaster bill " 
				+ "INNER JOIN bill.billingDetailsList billDtls "
				+ "INNER JOIN billDtls.serviceMaster serviceMstr "
				+ "INNER JOIN serviceMstr.unitServiceMasterDetailsList unitService "
				+ "INNER JOIN serviceMstr.specialityMaster specialityMast "
				+ "INNER JOIN billDtls.orderDetailsMaster ordDtls "
				+ "INNER JOIN ordDtls.orderMaster ordMast " 
				+ "LEFT JOIN billDtls.doctorMaster doc "
				+ "LEFT JOIN ordDtls.tax tax " 
				+ "WHERE bill.billingMasterId=:billId and bill.unitId = unitService.unitId order by billDtls.billingDetailsId "),

		@NamedQuery(name = "GET_BILL_DETAILS_BY_BILL_ID_CN", query = "SELECT bill.billingMasterId as billingMasterId, "
				+ "billDtls.billingDetailsId as billingDetailsId, " 
				+ "bill.visitTypeId as visitTypeId, "
				+ "ordMast.orderNo as orderNo, "
				+ "ordDtls.orderId as orderId, " 
				+ "ordDtls.orderDetailsId as orderDetailsId, "
				+ "serviceMstr.serviceStandardName as serviceStandardName, " 
				+ "serviceMstr.serviceStandardCode as serviceStandardCode, "
				+ "billDtls.ordDocId as doctorId, "
				+ "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
				+ "billDtls.quantity as quantity, " 
				+ "billDtls.rate as unitPrice, "
				+ "billDtls.grossAmount as grossAmount, " 
				+ "coalesce(billDtls.concession,0) as concession, "
				+ "billDtls.discount as previousDiscount, "
				+ "billDtls.discount as billDiscount, "
				+ " (SELECT sum(coalesce(crDtls.crTaxAmount,0)) FROM CreditNoteDetails crDtls inner join crDtls.creditNoteMaster credtmaster  WHERE crDtls.billingDetailsId = billDtls.billingDetailsId and credtmaster.isApproved not in('R') and credtmaster.status = 'A') as crTaxToadd ,"
				+ " (SELECT sum(coalesce(crDtls.crAdditionalDiscount,0)) FROM CreditNoteDetails crDtls inner join crDtls.creditNoteMaster credtmaster  WHERE crDtls.billingDetailsId = billDtls.billingDetailsId and credtmaster.isApproved not in('R') and credtmaster.status = 'A') as previousDiscountToadd ,"
				+ "bill.outstanding as outstanding , " 
				+ "bill.totalBillAmount as totalBillAmount, " 
				+" billPayeeDtls.dueAmount as payeeDueAmount , "
				+" billPayeeDtls.billAmount as billAmount , "
				+ "billDtls.taxId as taxId, " 
				+ "tax.name as taxName, "
				+ "coalesce(billDtls.taxPer,0) as taxPercentage, " 
				+ "billDtls.taxAmount as taxAmount, "
				+ "billDtls.netAmt as netAmt, " 
				+ "(ordDtls.selfPayable - coalesce((SELECT sum(coalesce(crDtls.crNetAmount,0)) FROM CreditNoteDetails crDtls inner join crDtls.creditNoteMaster credtmaster  WHERE crDtls.billingDetailsId = billDtls.billingDetailsId and credtmaster.isApproved not in('R') and credtmaster.status = 'A'),0)) as remainingRefundAmt, "
				+ "ordDtls.serviceAmt as basePrice, "
				+ "ordDtls.isPackageService as isPackageService, "
				+ " ordDtls.isPackage as isPackage," 
				+ " ordDtls.packageId as packageId," 
				+ "ordDtls.serviceId as serviceMasterId, "
				+ "billPayeeDtls.associateCompanyId as associateCompanyId,"
				+ "coalesce(ordDtls.ordCancelled,'N') as ordCancelled, "
				+ "to_char(ordDtls.orderDate,'dd/mm/yyyy') as orderDateString ,"
				+ "unitService.rateEditbleMinValue as minRateEditable, "
				+ "unitService.rateEditableMaxValue as maxRateEditable, "
				+ "unitService.isTaxApplicable as isTaxApplicable, "
				+ "unitService.isRefDoctorShare as isRefDoctorShare, "
				+ "unitService.isDiscount as isDiscountApplicable, " 
				+ "unitService.discountValue as discountValue, "
				+ "unitService.gstTypeId as gstTypeId, " 
				+ "unitService.otcGstTypeId as otcGstTypeId, "
				+ "unitService.isQuantityEditable as isQuantityEditable, "
				+ "unitService.isAutoRender as isAutorendered , "
				+ "ordDtls.serviceRendered as serviceRendered ,  "
				+ "specialityMast.specialityId as specialityId ,  "
				+ "specialityMast.specialityName as specialityName ,  "
				+ "subPecilityMst.subSpecialityMasterId as subSpecialityMasterId ,  "
				+ "subPecilityMst.subSpecialityMasterName as subSpecialityMasterName ,  "
				+ "(SELECT distinct coalesce(crDtls.cancelOrDiscount,0) FROM CreditNoteDetails crDtls  WHERE crDtls.billingDetailsId = billDtls.billingDetailsId and crDtls.status = 'A' ) as cancelOrDiscount,   "
				+ "(SELECT sum(coalesce(crDtls.crNetAmount,0)) FROM CreditNoteDetails crDtls inner join crDtls.creditNoteMaster credtmaster  WHERE crDtls.billingDetailsId = billDtls.billingDetailsId and credtmaster.isApproved not in('R') and credtmaster.status = 'A' ) as refundAmount,   "
				+ "ordDtls.selfPayable as selfPayable, "
				+ "ordDtls.creditPayable as creditPayable, "
                + "(SELECT coalesce(mpack.packageTypeId,0) from MPackageMaster mpack where mpack.packageMasterId = ordDtls.packageId) as packageTypeId, "
				+ "ordDtls.packageConsumptionAmt as packageConsumptionAmt ,"
				+ " ordDtls.docSharePer as docSharePer ,"
				+ " ordDtls.hospitalShare as hospitalShare ,"
				+ " ordDtls.refDocSharePer as refDocSharePer ,"
				+ " ordDtls.ordDocSplId as doctorSpecialityId ,"
				+ " ordDtls.tariffId as tariffId ,"
				+ " unitService.isDocReq as isDoctorRequired, "
				+ " ordDtls.orderDate as orderDate ,"
				+ " ordDtls.discountOwnership as discountOwnership, "
				+ " ordDtls.isDummyService as isDummyService ,"
				+ " bill.billStatusId as billStatusId "
				+ "FROM BillingMaster bill "
				+ "INNER JOIN bill.billingDetailsList billDtls " 
				+ "INNER JOIN billDtls.serviceMaster serviceMstr "
				+ "INNER JOIN serviceMstr.unitServiceMasterDetailsList unitService "
				+ "INNER JOIN serviceMstr.specialityMaster specialityMast "
				+ "INNER JOIN serviceMstr.subSpecialityMaster subPecilityMst "
				+ "INNER JOIN billDtls.orderDetailsMaster ordDtls "
				+ "INNER JOIN bill.billPayeeDetailsList billPayeeDtls "
				+ "INNER JOIN ordDtls.orderMaster ordMast "
				+ "INNER JOIN ordDtls.listOrderDetailsPackageMapper pckMppr WITH  pckMppr.status = 'A' "
				+ "LEFT JOIN billDtls.doctorMaster doc " 
				+ "LEFT JOIN ordDtls.tax tax "
				+ " WHERE bill.billingMasterId=:billId " 
				//+ " AND ordDtls.ordCancelled='N' "
				//+ " AND ordDtls.status='A' "
				+ " AND unitService.unitId = bill.unitId "
				+ " AND billPayeeDtls.payeeId = (SELECT uabc.selfPayeeId FROM  UnitApplicationBillingConfiguration uabc WHERE uabc.status = 'A' AND  uabc.unitId = bill.unitId ) "
				+ " AND unitService.unitId = bill.unitId "
				+ " AND pckMppr.isPackageService='N' "
				//+ " AND ordDtls.status='A' "
				//+ " AND billPayeeDtls.dueAmount=0 "
				+ " AND ordDtls.isPackage in (false,true) "
                + " order by billDtls.billingDetailsId "
				),

		@NamedQuery(name = "GET_INTERIM_BILL_DETAILS_BY_ADMISSION_ID", query = " SELECT bill.billingMasterId as billingMasterId, "
				+ " bill.billNumber as billNumber, "
				+ " bill.billDateTime as billDateTime, "
				+ " bill.totalBillAmount as totalBillAmount, "
				+ " bill.roundOff as roundOff, "
				+ " bill.netAmount as netAmount, "
				+ " bill.billCancelled as billCancelled "
				+ " FROM BillingMaster bill "
				+ " WHERE bill.admissionId =:admissionId "
				+ " and bill.isIntermediateBill = 'Y' "
				+ " ORDER BY bill.billingMasterId DESC "),


        @NamedQuery(name = "GET_BILL_DETAILS_BY_LIST_OF_BILL_IDS",
                query = "SELECT bill.billingMasterId as billingMasterId, "
                        + "billDtls.billingDetailsId as billingDetailsId, "
                        + "ordMast.orderNo as orderNo, "
                        + "billDtls.serviceId as serviceId, "
                        + "ordMast.saleId as saleId, "
                        + "ordMast.orderId as orderId, "
                        + "serviceMstr.serviceStandardName as serviceStandardName, "
                        + "serviceMstr.serviceStandardCode as serviceStandardCode, "
                        + "billDtls.ordDocId as doctorId, "
                        + "concat('Dr. ',coalesce(doc.firstName),' ',coalesce(doc.middleName),' ',coalesce(doc.lastName)) as doctorName, "
                        + "billDtls.quantity as quantity, "
                        + "billDtls.rate as unitPrice, "
                        + "billDtls.grossAmount as grossAmount, "
                        + "billDtls.selfPayable as selfPayable, "
                        + "billDtls.creditPayable as creditPayable, "
                        + "ordDtls.packageConsumptionAmt as packageConsumptionAmt, "
                        + "billDtls.payeeId as payeeId, "
                        + "billDtls.concession as concession, "
                        + "billDtls.discount as discount, "
                        + "unitService.isDiscount as isDiscountApplicable, "
                        + "billDtls.taxId as taxId, "
                        + "billDtls.coPayPercentage as coPayPer, "
                        + "tax.name as taxName, "
                        + "billDtls.taxPer as taxPercentage, "
                        + "billDtls.taxAmount as taxAmount, "
                        + "billDtls.netAmt as netAmt, "
                        + "specialityMast.specialityId as specialityId ,  "
                        + "specialityMast.specialityName as specialityName ,  "
                        + " bill.roundOff as roundOff, "
                        + "to_char(ordDtls.orderDate,'dd/mm/yyyy') as orderDateString, "
                        + " ordDtls.orderDetailsId as orderDetailsId, "
                        + " ordDtls.serviceRendered as serviceRendered ,"
                        + "ordDtls.isPackageService as isPackageService, "
                        + " bill.billNumber as billNumber "
                        + "FROM BillingMaster bill "
                        + "INNER JOIN bill.billingDetailsList billDtls "
                        + "INNER JOIN billDtls.serviceMaster serviceMstr "
                        + "INNER JOIN serviceMstr.unitServiceMasterDetailsList unitService "
                        + "INNER JOIN serviceMstr.specialityMaster specialityMast "
                        + "INNER JOIN billDtls.orderDetailsMaster ordDtls "
                        + "INNER JOIN ordDtls.orderMaster ordMast "
                        + "LEFT JOIN billDtls.doctorMaster doc "
                        + "LEFT JOIN ordDtls.tax tax "
                        + "WHERE bill.billingMasterId in (:billIds) and bill.unitId = unitService.unitId order by billDtls.billingDetailsId ")
})

@NamedNativeQueries({ @NamedNativeQuery(name = "GET_BILL_DETAILS_FOR_BILL_LIST_BY_FILTER", query = " select " 
		 + " 	bill.billing_master_id as \"billingMasterId\", " 
		 + " 	to_char(bill.bill_date_time, 'DD-MM-YYYY HH24:MI:SS') as \"billDate\", " 
		 + " 	bill.bill_number as \"billNumber\", " 
		 + " 	pat.uhid_number as \"uhidNumber\", " 
		 + " 	concat(coalesce(pre.prefix_code, ''), ' ', pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", " 
		 + " 	to_char(pat.dob, 'DD/MM/YYYY') as \"birthDate\", " 
		 + " 	trim(to_char(extract(year from age(current_timestamp, pat.dob)), '999')) as \"ageString\", " 
		 + " 	pat.image_path as \"imagePath\", " 
		 + " 	pat.gender_id as \"genderId\", " 
		 + " 	bill.visit_type_id as \"visitTypeId\", " 
		 + " 	(case " 
		 + " 		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " 
		 + " 		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " 
		 + " 		else 'System' end) as \"userName\", " 
		 + " 	pat.is_otc_reg as \"isOtcReg\", " 
		 + " 	bill.organisation_id as \"orgId\", " 
		 + " 	bill.unit_id as \"unitId\", " 
		 + " 	bill.patient_id as \"patientId\", " 
		 + " 	bill.is_dr_note as \"isDrNote\", " 
		 + " 	billagainst.bill_number as \"againstbillNumber\", " 
		 + " 	spl.is_nursing_assessment_required as \"isNursingAssessmentRequired\", " 
		 + " 	bill.disallowed_amount as \"disallowedAmount\", " 
		 + " 	bill.disallowed_against_invoice_dtl_id as \"disallowedAgainstInvoiceDtlsId\", " 
		/* + " 	case " 
		 + " 		when bill.is_interim_or_final_bill = 2 then ( cast(coalesce((select sum(due_amount) from billing.t_bill_payee_details where payee_id = ( select self_payee_id from t_unit_application_billing_configuration where unit_id = :unitId ) and bill_id in ( select billing_master_id from billing.m_billing_master where status ='A' and admission_id = bill.admission_id and is_interim_or_final_bill = 1)), 0) as double precision)) end as \"intermediateSelfDue\", " 
		*/ 
		 + " cast(coalesce(temp.intermediateSelfDue,0)as double precision)  as \"intermediateSelfDue\", "
		 + " 		coalesce( case when bill.visit_type_id = 1 then ( select referral_id from t_referal_details where encounter_id = bill.encounter_id and referral_type_id = 1 ) end, case when bill.visit_type_id != 1 then ( select referral_id from t_referal_details where admission_id = bill.admission_id and referral_type_id = 1 ) end ) as \"referalDocId\", " 
		 + " 		coalesce( case when bill.visit_type_id = 1 then enc.doctor_id when bill.visit_type_id != 1 then adm.doctor_id end ) as \"doctorId\", " 
		 + " 		coalesce( case when bill.visit_type_id = 1 then enc.encounter_number when bill.visit_type_id != 1 then adm.admission_number end ) as \"visitNo\", " 
		 + " 		coalesce( case when bill.visit_type_id = 1 then to_char(enc.encounter_date, 'DD/MM/YYYY') when bill.visit_type_id != 1 then to_char(adm.created_date, 'DD/MM/YYYY') end ) as \"visitDate\", " 
		 + " 		coalesce( case when bill.visit_type_id = 1 then enc.encounter_id when bill.visit_type_id != 1 then adm.admission_id end ) as \"visitId\", " 
		 + " 		coalesce( case when bill.visit_type_id = 1 then (select enc.appointment_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id and enc.status = 'A') end ) as \"appointmentId\", " 
		 + " 		coalesce( case when bill.visit_type_id = 1 then (select mam.is_paid_appointment from opd.m_appointment_master mam where mam.appointment_id = (select enc.appointment_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id and enc.status = 'A')) end ) as \"isPaidAppointment\", " 
		 + " 		bill.total_bill_amount as \"totalBillAmount\", " 
		 + " 		bill.net_amount as \"netAmount\", " 
		 + " 		coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\", " 
		 + " 		cast(coalesce(round(cast(bill.outstanding as numeric), 2), 0) as double precision) as \"outstanding\", " 
		 + " 		bill.bill_status_id as \"billStatusId\", " 
		 + " 		bill.is_interim_or_final_bill as \"isInterimOrFinalBill\", " 
		 + " 		bill.total_self_payable as \"totalSelfPayable\", " 
		 + " 		bill.total_credit_payable as \"totalCreditPayable\", " 
		 + " 		billPayee.due_amount as \"selfDueAmount\", "
		 + "		bill.is_print as \"isPrint\", "
		 + "		(case when bill.encounter_id > 0 and tuabc.is_consolidated_bill='Y' then "
		 + "			(select count(tod.order_details_id) from t_order_details tod inner join t_order_master tom on tom.order_id = tod.order_id where tom.encounter_id = bill.encounter_id and tod.status ='A' and tod.service_is_billed=0 and service_id is not null) "
		 + "		else 	 "
		 + "			(select count(tod.order_details_id) from t_order_details tod inner join t_order_master tom on tom.order_id = tod.order_id where tom.admission_id = bill.admission_id and tod.status ='A' and tod.service_is_billed=0 and service_id is not null) "
		 + "		end) as \"pendingCount\", "
		 + "		(case "
		 + "			when bill.visit_type_id = 1 and bill.bill_status_id=1 then coalesce(enc.is_financial_clearance,'N') "
		 + "			when bill.visit_type_id = 1 and bill.bill_status_id!=1 then 'Y' "
		 + "			else adm.is_financial_creance  "
		 + "		end ) as \"isFinancialClearance\"  "
		 + " 	from " 
		 + " 		billing.m_billing_master bill " 
		 + " 	left join billing.m_billing_master billagainst on " 
		 + " 		bill.dr_note_against_billid = billagainst.billing_master_id " 
		 + " 	inner join 	t_unit_application_billing_configuration tuabc on "
		 + " 		tuabc.unit_id = bill.unit_id "
		 + " 	left join billing.t_bill_payee_details billPayee on " 
		 + " 		bill.billing_master_id = billPayee.bill_id " 
		 + " 		and payee_id = tuabc.self_payee_id " 
		 + " 	inner join patient.t_patient_registration pat on " 
		 + " 		pat.patient_id = bill.patient_id " 
		 + " 	left join m_prefix_master pre on " 
		 + " 		pre.prefix_id = pat.prefix_id " 
		 + " 	left join PUBLIC.m_employee_master empmst on " 
		 + " 		bill.created_by = empmst.user_id " 
		 + " 	left join doctor.m_doctor_master docmst on " 
		 + " 		bill.created_by = docmst.user_id " 
		 + " 	left join opd.t_encounter_master enc on " 
		 + " 		enc.encounter_id = bill.encounter_id " 
		 + " 	left join adt.t_admission adm on " 
		 + " 		adm.admission_id = bill.admission_id " 
		 + " 	left join t_unit_speciality_mapper spl on " 
		 + " 		spl.speciality_id = enc.dept_id " 
		 + " 		and spl.unit_id = enc.unit_id " 
		 + " left join ("
		 + "			select sum(due_amount) as intermediateSelfDue ,tbpd.payee_id,mbm.is_interim_or_final_bill,mbm.admission_id,mbm.billing_master_id  "  
		 + " 		from billing.t_bill_payee_details tbpd "  
		 + " 		inner join t_unit_application_billing_configuration tubc on tubc.self_payee_id = tbpd.payee_id "  
		 + " 		inner join billing.m_billing_master mbm on mbm.billing_master_id = tbpd.bill_id AND mbm.is_interim_or_final_bill = 1 and mbm.status='A' " 
		 + " 		group by "
		 + " 		tbpd.payee_id,tbpd.bill_id ,mbm.is_interim_or_final_bill ,	mbm.billing_master_id ,"  
		 + " 		mbm.admission_id"
		 + ") temp on temp.billing_master_id = bill.billing_master_id "  
		 + " and temp.admission_id = bill.admission_id  and temp.is_interim_or_final_bill = bill.is_interim_or_final_bill"
		 + " AND  bill.is_interim_or_final_bill = 2  "
		 + " 	where " 
		 + " 		bill.bill_cancelled = 'N' " 
		 + " 		and bill.organisation_id =:orgId " 
		 + " 		and bill.unit_id =:unitId "  ),
	
	@NamedNativeQuery(name = "GET_BILL_DETAILS_FOR_BILL_LIST_BY_FILTER_FOR_CANCELLED_BILL", query = " select " 
			 + " 	bill.billing_master_id as \"billingMasterId\","
			 + " bill.is_bill_reopened as \"billReopened\" ,  " 
			 + " 	to_char(bill.bill_date_time, 'DD-MM-YYYY HH24:MI:SS') as \"billDate\","
			 + "    (select to_char(tad.pdd, 'YYYY-MM-DD')  from adt.t_admission_details tad  where tad.admission_id  = adm.admission_id  order by tad.admission_details_id desc  limit 1) as \"pdd\", " 
			 + " 	bill.bill_number as \"billNumber\", " 
			 + " 	pat.uhid_number as \"uhidNumber\", " 
			 + " 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", " 
			 + "	to_char(pat.dob,'DD/MM/YYYY') as \"birthDate\", "
			 + "	trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pat.dob)),'999')) as \"ageString\", "
			 + " 	pat.image_path as \"imagePath\", "
			 + " 	pat.gender_id as \"genderId\", "
			 + " 	pat.country_calling_code as \"countryCallingCode\", "
			 + " 	bill.visit_type_id as \"visitTypeId\", " 
			 + " 	(case " 
			 + " 		when empmst.employee_id is not null then concat(empmst.first_name, ' ', empmst.last_name) " 
			 + " 		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " 
			 + " 		else 'Admin' " 
			 + " 	end) as \"userName\", " 
			 + " 	pat.is_otc_reg as \"isOtcReg\", " 
			 + " 	bill.organisation_id as \"orgId\", " 
			 + " 	bill.unit_id as \"unitId\", " 
			 + " 	bill.patient_id as \"patientId\", " 
			 + " 	bill.is_dr_note as \"isDrNote\", " 
			 + " 	billagainst.bill_number as \"againstbillNumber\", " 
			 + " 	spl.is_nursing_assessment_required as \"isNursingAssessmentRequired\", " 
			 + " 	bill.disallowed_amount as \"disallowedAmount\", " 
			 + " 	bill.disallowed_against_invoice_dtl_id as \"disallowedAgainstInvoiceDtlsId\", " 
			/* + " 	case " 
			 + " 		when bill.is_interim_or_final_bill = 2 then ( cast(coalesce((select sum(due_amount) from billing.t_bill_payee_details where payee_id = ( select self_payee_id from t_unit_application_billing_configuration where unit_id = :unitId ) and bill_id in ( select billing_master_id from billing.m_billing_master where admission_id = bill.admission_id and is_interim_or_final_bill = 1)), 0) as double precision)) " 
			 + " end as \"intermediateSelfDue\", " */
			 + " cast(coalesce(temp.intermediateSelfDue,0)as double precision)  as \"intermediateSelfDue\", "
			 + " 	coalesce( case when bill.visit_type_id = 1 then ( select referral_id from t_referal_details where encounter_id = bill.encounter_id and referral_type_id = 1 ) end, case when bill.visit_type_id!=1 then ( select referral_id from t_referal_details where admission_id = bill.admission_id and referral_type_id = 1 ) end ) as \"referalDocId\", " 
			 + " 	coalesce( case when bill.visit_type_id = 1 then enc.doctor_id when bill.visit_type_id != 1 then adm.doctor_id end ) as \"doctorId\", " 
			 + " 	coalesce( case when bill.visit_type_id = 1 then enc.encounter_number when bill.visit_type_id != 1 then adm.admission_number end ) as \"visitNo\", " 
			 + " 	coalesce( case when bill.visit_type_id = 1 then to_char(enc.encounter_date, 'YYYY-MM-DD') when bill.visit_type_id != 1 then to_char(adm.created_date, 'YYYY-MM-DD') end ) as \"visitDate\", " 
			 + " 	coalesce( case when bill.visit_type_id = 1 then enc.encounter_id when bill.visit_type_id != 1 then adm.admission_id end ) as \"visitId\", " 
			 + " 	coalesce( case when bill.visit_type_id = 1 then (select enc.appointment_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id and enc.status = 'A') end ) as \"appointmentId\", " 
			 + " 	bill.total_bill_amount as \"totalBillAmount\", " 
			 + " 	bill.net_amount as \"netAmount\", " 
			 + " 	coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\", " 
			 + " 	cast(coalesce(ROUND(CAST(bill.outstanding AS NUMERIC),2),0) as double precision) as \"outstanding\", " 
			 + " 	bill.bill_status_id as \"billStatusId\", " 
			 + " 	bill.is_interim_or_final_bill as \"isInterimOrFinalBill\", " 
			 + " 	bill.total_self_payable as \"totalSelfPayable\", "
			 + " 	bill.total_credit_payable as \"totalCreditPayable\", " 
			 + " 	pat.uhid_number as \"uhIdNumber\", "
			 + " 	gender.gender_code as \"genderName\", "
			 + " 	pat.mobile_number as \"mobileNo\", "
			 + " 	pat.identification_number as \"identificationNo\", "
			 + " 	identi.identification_name as \"identificationName\", "
			 + "	trim(to_char(EXTRACT(year from AGE(CURRENT_TIMESTAMP, pat.dob)),'999')) as \"age\", "
			 + "	reason.reason_desc as \"reasonDesc\", "
			 + "	bill.bill_cancel_remark as \"remark\" ,"
			 + "    bill.encounter_id as \"encounterId\" , "
			 + "    bill.admission_id as \"admissionId\" ,"
			 //+ "    disc.discharge_status_id as \"dischargeStatusId\" ,"
			 + " (select disc.discharge_status_id from adt.t_discharge disc where disc.admission_id = adm.admission_id limit 1) as \"dischargeStatusId\" , "
			 + "    (case when tad.bed_id is not null then tad.bed_id else (select bed_id from adt.t_admission_details tad2 where tad2.admission_id = adm.admission_id order by tad2.admission_id limit 1 ) end)  as \"bedId\" ,"
			 + "    (case when tad.ward_id is not null then tad.ward_id else (select ward_id from adt.t_admission_details tad2 where tad2.admission_id = adm.admission_id order by tad2.admission_id limit 1 ) end)  as \"wardId\" ,"
			 + "    (case when tad.is_discharge is not null then tad.is_discharge else (select is_discharge from adt.t_admission_details tad2 where tad2.admission_id = adm.admission_id order by tad2.admission_id limit 1 ) end)  as \"isDischarge\" ,"
			 + " 	coalesce( case when bill.visit_type_id = 1 then enc.payment_entitlement_type_id when bill.visit_type_id != 1 then adm.payment_entitlement_id end ) as \"paymentEntitlementId\" "
			 + " from " 
			 + " 	billing.m_billing_master bill " 
			 + " left join billing.m_billing_master billagainst on " 
			 + " 	bill.dr_note_against_billid = billagainst.billing_master_id " 
			 + " inner join patient.t_patient_registration pat on " 
			 + " 	pat.patient_id = bill.patient_id " 
			 + " left join m_prefix_master pre on "
			 + " 	pre.prefix_id = pat.prefix_id "
			 + " left join public.m_gender_master gender on " 
			 + " 	gender.gender_id = pat.gender_id " 
			 + " left join adt.m_identification_master identi on " 
			 + " 	identi.identification_id = pat.identification_type_id " 
			 + " left join PUBLIC.m_employee_master empmst on " 
			 + " 	bill.bill_cancel_by = empmst.user_id " 
			 + " left join doctor.m_doctor_master docmst on " 
			 + " 	bill.bill_cancel_by = docmst.user_id " 
			 + " left join opd.t_encounter_master enc on " 
			 + " 	enc.encounter_id = bill.encounter_id " 
			 + " left join adt.t_admission adm on " 
			 + " 	adm.admission_id = bill.admission_id "
			 + " left join adt.t_admission_details tad on tad.admission_id = adm.admission_id and tad.status = 'A' " 
			 + " left join t_unit_speciality_mapper spl on "
			 + " 	spl.speciality_id = enc.dept_id "
			 + " 	and spl.unit_id =enc.unit_id  " 
			 + " left join adt.m_reason_master reason on "
			 + " 	reason.reason_master_id = bill.bill_cancel_reason_id "
			// + " left join adt.t_discharge disc on "
			// + " 	disc.admission_id  = adm.admission_id "
			 + " left join ("
			 + "			select sum(due_amount) as intermediateSelfDue ,tbpd.payee_id,mbm.is_interim_or_final_bill,mbm.admission_id,mbm.billing_master_id  "  
			 + " 		from billing.t_bill_payee_details tbpd "  
			 + " 		inner join t_unit_application_billing_configuration tubc on tubc.self_payee_id = tbpd.payee_id "  
			 + " 		inner join billing.m_billing_master mbm on mbm.billing_master_id = tbpd.bill_id AND mbm.is_interim_or_final_bill = 1 and mbm.status='A' " 
			 + " 		group by "
			 + " 		tbpd.payee_id,tbpd.bill_id ,mbm.is_interim_or_final_bill ,	mbm.billing_master_id ,"  
			 + " 		mbm.admission_id"
			 + ") temp on temp.billing_master_id = bill.billing_master_id "  
			 + " and temp.admission_id = bill.admission_id  and temp.is_interim_or_final_bill = bill.is_interim_or_final_bill"
			 + " AND  bill.is_interim_or_final_bill = 2  "
			 + " where " 
			 + " 	bill.bill_cancelled = 'Y' " 
			 + " 	and bill.organisation_id =:orgId " 
			 + " 	and bill.unit_id =:unitId " ),
	
		@NamedNativeQuery(name = "GET_BILL_MASTER_BY_ID", query = "select "
			+ " 	billingmas0_.billing_master_id as \"billingMasterId\", "
			+ " 	billingmas0_.encounter_id as \"encounterId\", "
			+ " 	billingmas0_.visit_type_id as \"visitTypeId\", "
			+ " 	billingmas0_.patient_id as \"patientId\", "
			+ " 	billingmas0_.bill_number as \"billNumber\", "
			+ " 	coalesce(billingmas0_.bill_cancelled,'N') as \"billCancelled\", "
			+ " 	cast(coalesce(ROUND(CAST(billingmas0_.net_amount AS NUMERIC),2),0) as double precision) as \"netAmount\", "
			+ " 	cast(coalesce(ROUND(CAST(billingmas0_.discount_amount AS NUMERIC),2),0) as double precision) as \"discountAmount\", "
			+ " 	cast(coalesce(ROUND(CAST(billingmas0_.concession_amount AS NUMERIC),2),0) as double precision) as \"concessionAmount\", "
			+ " 	cast(coalesce(ROUND(CAST(billingmas0_.total_bill_amount AS NUMERIC),2),0) as double precision) as \"totalBillAmount\", "
			+ " 	cast(coalesce(ROUND(CAST(billingmas0_.outstanding AS NUMERIC),2),0) as double precision) as \"outstanding\" "
			+ " from "
			+ " 	billing.m_billing_master billingmas0_ "
			+ " where "
			+ " 	billingmas0_.billing_master_id =:billMasterId "),

		@NamedNativeQuery(name = "GET_BILL_DETAILS_FOR_BILL_LIST_PATIENT", query =
				"    select "
						 + "  	bill.billing_master_id as \"billingMasterId\", "
						 + "  	to_char(bill.bill_date_time, 'DD-MM-YYYY HH24:MI:SS') as \"billDate\", "
						 + "  	bill.bill_number as \"billNumber\", "
						 + "  	pat.uhid_number as \"uhidNumber\", "
						 + "  	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", "
						 + "  	bill.visit_type_id as \"visitTypeId\", "
						 + "  	bill.encounter_id as \"encounterId\", "
						 + "  	bill.admission_id as \"admissionId\", "
						 + "  	pat.is_otc_reg as \"isOtcReg\", "
						 + "  	bill.organisation_id as \"orgId\", "
						 + "  	bill.unit_id as \"unitId\", "
						 + "  	bill.patient_id as \"patientId\", "
						 + "  	(case "
						 + "  		when adm.status is null then 'A' "
						 + "  		else adm.status end) as \"status\", "
						// + "  	cast(coalesce((select sum(due_amount) from billing.t_bill_payee_details where payee_id = ( select self_payee_id from t_unit_application_billing_configuration where unit_id = :unitId ) and bill_id in ( select billing_master_id from billing.m_billing_master where admission_id = bill.admission_id and is_interim_or_final_bill = 1)), 0) as double precision) as \"intermediateSelfDue\", "
						 + " cast(coalesce(temp.intermediateSelfDue,0)as double precision)  as \"intermediateSelfDue\", "
						+ " 	(case " 
						 + " 		when emp_mst.employee_id is not null then concat(emp_mst.first_name, ' ', emp_mst.last_name) " 
						 + " 		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " 
						 + " 		else 'Admin' " 
						 + " 	end) as \"userName\", "
						 + "  	coalesce( case when bill.visit_type_id = 1 then ( select enc.encounter_number from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id ) end, case when bill.visit_type_id <> 1 then ( select adm.admission_number from adt.t_admission adm where adm.admission_id = bill.admission_id ) end ) as \"visitNo\", "
						 + "  	coalesce( case when bill.visit_type_id = 1 then ( select to_char(enc.encounter_date, 'DD/MM/YYYY') from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id ) end, case when bill.visit_type_id <> 1 then ( select to_char(adm.doa, 'DD/MM/YYYY') from adt.t_admission_details adm where adm.admission_id = bill.admission_id order by 1 desc limit 1 ) end ) as \"visitDate\", "
						 + "  	coalesce( case when bill.visit_type_id = 1 then ( select enc.encounter_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id ) end, case when bill.visit_type_id <> 1 then ( select adm.admission_id from adt.t_admission adm where adm.admission_id = bill.admission_id ) end ) as \"visitId\", "
						 + "  	coalesce( case when bill.visit_type_id = 1 then ( select enc.appointment_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id ) end ) as \"appointmentId\", "
						 + "  	coalesce( case when bill.visit_type_id = 1 then ( select enc.status from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id ) end ) as \"encounterStatus\", "
						 + "  	bill.total_bill_amount as \"totalBillAmount\", "
						 + "  	coalesce( case when bill.visit_type_id = 1 then ( select referral_id from t_referal_details where encounter_id = bill.encounter_id and referral_type_id = 1 ) end, case when bill.visit_type_id = 2 or bill.visit_type_id = 3 or bill.visit_type_id = 4 then ( select referral_id from t_referal_details where admission_id = bill.admission_id and referral_type_id = 1 ) end ) as \"referalDocId\", "
						 + "  	bill.net_amount as \"netAmount\", "
						 + "  	coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\", "
						 + "  	bill.outstanding as \"outstanding\", "
						 + "  	adm.is_final_bill as \"isFinalBill\", "
						 + "  	( "
						 + "  	select "
						 + "  		max(bill_status_id) "
						 + "  	from "
						 + "  		billing.t_billing_status_mapper "
						 + "  	where "
						 + "  		bill_id = bill.billing_master_id) as \"billStatusId\" , "
						 + "  	bill.is_interim_or_final_bill as \"isInterimOrFinalBill\", "
						 + "  	bill.total_self_payable as \"totalSelfPayable\", "
						 + "  	bill.total_credit_payable as \"totalCreditPayable\" "
						 + "  from "
						 + "  	billing.m_billing_master bill "
						 + "  inner join patient.t_patient_registration pat on "
						 + "  	pat.patient_id = bill.patient_id "
						 + "  left join m_prefix_master pre on "
						 + "  	pre.prefix_id = pat.prefix_id "
						 + "  left join adt.t_admission adm on "
						 + "  	bill.admission_id = adm.admission_id "
						 + "  left join public.m_employee_master emp_mst on "
						 + "  	emp_mst.user_id = bill.created_by "
						 + " left join doctor.m_doctor_master docmst on " 
						 + " 	bill.created_by = docmst.user_id " 
						 + " left join ("
						 + "			select sum(due_amount) as intermediateSelfDue ,tbpd.payee_id,mbm.is_interim_or_final_bill,mbm.admission_id,mbm.billing_master_id  "  
						 + " 		from billing.t_bill_payee_details tbpd "  
						 + " 		inner join t_unit_application_billing_configuration tubc on tubc.self_payee_id = tbpd.payee_id "  
						 + " 		inner join billing.m_billing_master mbm on mbm.billing_master_id = tbpd.bill_id AND mbm.is_interim_or_final_bill = 1 and mbm.status='A' " 
						 + " 		group by "
						 + " 		tbpd.payee_id,tbpd.bill_id ,mbm.is_interim_or_final_bill ,	mbm.billing_master_id ,"  
						 + " 		mbm.admission_id"
						 + ") temp on temp.billing_master_id = bill.billing_master_id "  
						 + " and temp.admission_id = bill.admission_id  and temp.is_interim_or_final_bill = bill.is_interim_or_final_bill "
						 + "  where "
						 + "  	bill.bill_status_id in (:billStatusIds) "
						 + "  	and bill.is_dr_note in (:isDrCr) "
						 + "  	and bill.bill_cancelled = 'N' "
						 + "  	and bill.is_interim_or_final_bill in (:intremediateFinal) "
						 + "  	and bill.organisation_id = :orgId "
						 + "  	and bill.unit_id = :unitId "
						 + "  	and bill.patient_id = :patientId "
						 + "	order by bill.bill_date_time desc "),
		
		
		
		@NamedNativeQuery(name = "GET_BILL_DETAILS_FOR_BILL_LIST_PATIENT_CR", query =
//		        "with billdtls as ( "
				 "select "
				+ "	bill.billing_master_id as \"billingMasterId\", "
				+ "	to_char(bill.bill_date_time, "
				+ "	'DD-MM-YYYY HH24:MI:SS') as \"billDate\", "
				+ "	bill.bill_number as \"billNumber\", "
				+ "	pat.uhid_number as \"uhidNumber\", "
				+ "	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", "
				+ "	bill.visit_type_id as \"visitTypeId\", "
				+ "	bill.encounter_id as \"encounterId\", "
				+ "	bill.admission_id as \"admissionId\", "
				+ "	pat.is_otc_reg as \"isOtcReg\", "
				+ "	bill.organisation_id as \"orgId\", "
				+ "	bill.unit_id as \"unitId\", "
				+ "	bill.patient_id as \"patientId\", "
				 + " 	(case " 
				 + " 		when emp_mst.employee_id is not null then concat(emp_mst.first_name, ' ', emp_mst.last_name) " 
				 + " 		when docmst.doctor_id is not null then concat('Dr. ', docmst.first_name, ' ', docmst.last_name) " 
				 + " 		else 'Admin' " 
				 + " 	end) as \"userName\", "
				+ "	coalesce( "
				+ "	case "
				+ "		when bill.visit_type_id = 1 then ( "
				+ "		select "
				+ "			enc.encounter_number "
				+ "		from "
				+ "			opd.t_encounter_master enc "
				+ "		where "
				+ "			enc.encounter_id = bill.encounter_id ) "
				+ "	end, "
				+ "	case "
				+ "		when bill.visit_type_id <> 1 then ( "
				+ "		select "
				+ "			adm.admission_number "
				+ "		from "
				+ "			adt.t_admission adm "
				+ "		where "
				+ "			adm.admission_id = bill.admission_id ) "
				+ "	end ) as \"visitNo\", "
				+ "	coalesce( "
				+ "	case "
				+ "		when bill.visit_type_id = 1 then ( "
				+ "		select "
				+ "			to_char(enc.encounter_date, "
				+ "			'DD/MM/YYYY') "
				+ "		from "
				+ "			opd.t_encounter_master enc "
				+ "		where "
				+ "			enc.encounter_id = bill.encounter_id ) "
				+ "	end, "
				+ "	case "
				+ "		when bill.visit_type_id <> 1 then ( "
				+ "		select "
				+ "			to_char(adm.doa, "
				+ "			'DD/MM/YYYY') "
				+ "		from "
				+ "			adt.t_admission_details adm "
				+ "		where "
				+ "			adm.admission_id = bill.admission_id order by 1 desc limit 1 ) "
				+ "	end ) as \"visitDate\", "
				+ "	coalesce( "
				+ "	case "
				+ "		when bill.visit_type_id = 1 then ( "
				+ "		select "
				+ "			enc.encounter_id "
				+ "		from "
				+ "			opd.t_encounter_master enc "
				+ "		where "
				+ "			enc.encounter_id = bill.encounter_id ) "
				+ "	end, "
				+ "	case "
				+ "		when bill.visit_type_id <> 1 then ( "
				+ "		select "
				+ "			adm.admission_id "
				+ "		from "
				+ "			adt.t_admission adm "
				+ "		where "
				+ "			adm.admission_id = bill.admission_id ) "
				+ "	end ) as \"visitId\", "
				+ "	coalesce( "
				+ "	case "
				+ "		when bill.visit_type_id = 1 then ( "
				+ "		select "
				+ "			enc.appointment_id "
				+ "		from "
				+ "			opd.t_encounter_master enc "
				+ "		where "
				+ "			enc.encounter_id = bill.encounter_id ) "
				+ "	end ) as \"appointmentId\", "
				+ "	bill.total_bill_amount as \"totalBillAmount\", "
				+ "	bill.net_amount as \"netAmount\", "
				+ "	coalesce((bill.net_amount - bill.outstanding), "
				+ "	0) as \"amountPaid\", "
				+ "	bill.outstanding as \"outstanding\", "
				+ "	( "
				+ "	select "
				+ "		max(bill_status_id) "
				+ "	from "
				+ "		billing.t_billing_status_mapper "
				+ "	where "
				+ "		bill_id = bill.billing_master_id) as \"billStatusId\" "
				+ "from "
				+ "	billing.m_billing_master bill "
				+ "inner join billing.t_bill_payee_details payeedtls on "
				+ "	bill.billing_master_id = payeedtls.bill_id "
				+ "	inner join public.t_unit_application_billing_configuration config on "
				+ "	config.self_payee_id = payeedtls.payee_id "
				+ "	and bill.unit_id = config.unit_id "
				+ "	and config.status = 'A' "
				+ "inner join patient.t_patient_registration pat on "
				+ "	pat.patient_id = bill.patient_id "
				+ "  left join m_prefix_master pre on "
				 + "  	pre.prefix_id = pat.prefix_id "
				+ "left join billing.t_billing_payment_details billpymt on "
				+ "	billpymt.billing_master_id = bill.billing_master_id "
				+ "left join public.m_employee_master emp_mst on "
				+ "	emp_mst.user_id = bill.created_by "
				 + " left join doctor.m_doctor_master docmst on " 
				 + " 	bill.created_by = docmst.user_id " 
				+ "where "
				+ "    payeedtls.due_amount = 0 "
				+ "	and bill.bill_status_id in (:billStatusIds) "
				+ "	and bill.is_dr_note in (:isDrCr) and bill.bill_cancelled = 'N' "
				+ "	and bill.organisation_id = :orgId "
				+ "	and bill.unit_id = :unitId "
				+ "	and bill.patient_id = :patientId "
				+ "group by "
				+ "	bill.billing_master_id, "
				+ "	pat.uhid_number, "
				+ "	\"patientName\", "
				+ "	pat.is_otc_reg, "
				+ "	\"userName\" "
				+ "order by "
				+ "	bill.bill_date_time desc"
//				+ " ) select "
//				+ "	* "
//				+ "from "
//				+ "	billdtls bill "
//				+ "where "
//				+ "	bill.\"orgId\" = :orgId "
//				+ "	and bill.\"unitId\" = :unitId "
//				+ "	and bill.\"patientId\" = :patientId"
				),


		@NamedNativeQuery(name = "GET_BILLS_FOR_CREDIT_NOTE", query ="SELECT "
		+ "	bill.billing_master_id AS \"billingMasterId\", "
		+ "	To_char(bill.bill_date_time, 'DD-MM-YYYY HH24:MI:SS') AS \"billDate\", "
		+ "	bill.bill_number AS \"billNumber\", "
		+ "	pat.uhid_number AS \"uhidNumber\", "
		+ "	Concat(Coalesce(pre.prefix_code, ''), ' ', pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) AS \"patientName\", "
		+ "	bill.visit_type_id AS \"visitTypeId\", "
		+ "	bill.encounter_id AS \"encounterId\", "
		+ "	bill.admission_id AS \"admissionId\", "
		+ "	pat.is_otc_reg AS \"isOtcReg\", "
		+ "	bill.organisation_id AS \"orgId\", "
		+ "	bill.unit_id AS \"unitId\", "
		+ "	bill.patient_id AS \"patientId\", "
		+ "	(CASE "
		+ "		WHEN emp_mst.employee_id IS NOT NULL THEN Concat(emp_mst.first_name, ' ', emp_mst.last_name) "
		+ "		WHEN docmst.doctor_id IS NOT NULL THEN Concat('Dr. ', docmst.first_name, ' ', docmst.last_name) "
		+ "		ELSE 'Admin' "
		+ "	end) AS \"userName\", "
		+ "	Coalesce( CASE WHEN bill.visit_type_id = 1 THEN ( SELECT enc.encounter_number FROM opd.t_encounter_master enc WHERE enc.encounter_id = bill.encounter_id ) end, CASE WHEN bill.visit_type_id <> 1 THEN ( SELECT adm.admission_number FROM adt.t_admission adm WHERE adm.admission_id = bill.admission_id ) end ) AS \"visitNo\", "
		+ "	Coalesce( CASE WHEN bill.visit_type_id = 1 THEN ( SELECT To_char(enc.encounter_date, 'DD/MM/YYYY') FROM opd.t_encounter_master enc WHERE enc.encounter_id = bill.encounter_id ) end, CASE WHEN bill.visit_type_id <> 1 THEN ( SELECT To_char(adm.doa, 'DD/MM/YYYY') FROM adt.t_admission_details adm WHERE adm.admission_id = bill.admission_id ORDER BY 1 DESC LIMIT 1 ) end ) AS \"visitDate\", "
		+ "	Coalesce( CASE WHEN bill.visit_type_id = 1 THEN ( SELECT enc.encounter_id FROM opd.t_encounter_master enc WHERE enc.encounter_id = bill.encounter_id ) end, CASE WHEN bill.visit_type_id <> 1 THEN ( SELECT adm.admission_id FROM adt.t_admission adm WHERE adm.admission_id = bill.admission_id ) end ) AS \"visitId\", "
		+ "	Coalesce( CASE WHEN bill.visit_type_id = 1 THEN ( SELECT enc.appointment_id FROM opd.t_encounter_master enc WHERE enc.encounter_id = bill.encounter_id ) end ) AS \"appointmentId\", "
		+ "	bill.total_bill_amount AS \"totalBillAmount\", "
		+ "	bill.net_amount AS \"netAmount\", "
		+ "	Coalesce((bill.net_amount - bill.outstanding), 0) AS \"amountPaid\", "
		+ "	bill.outstanding AS \"outstanding\", "
		+ "	( "
		+ "	SELECT "
		+ "		Max(bill_status_id) "
		+ "	FROM "
		+ "		billing.t_billing_status_mapper "
		+ "	WHERE "
		+ "		bill_id = bill.billing_master_id) AS \"billStatusId\" "
		+ "FROM "
		+ "	billing.m_billing_master bill "
		+ "INNER JOIN billing.t_bill_payee_details payeedtls ON "
		+ "	bill.billing_master_id = payeedtls.bill_id "
		+ "INNER JOIN public.t_unit_application_billing_configuration config ON "
		+ "	config.self_payee_id = payeedtls.payee_id "
		+ "	AND bill.unit_id = config.unit_id "
		+ "	AND config.status = 'A' "
		+ "INNER JOIN patient.t_patient_registration pat ON "
		+ "	pat.patient_id = bill.patient_id "
		+ "LEFT JOIN m_prefix_master pre ON "
		+ "	pre.prefix_id = pat.prefix_id "
		+ "LEFT JOIN billing.t_billing_payment_details billpymt ON "
		+ "	billpymt.billing_master_id = bill.billing_master_id "
		+ "LEFT JOIN public.m_employee_master emp_mst ON "
		+ "	emp_mst.user_id = bill.created_by "
		+ "LEFT JOIN doctor.m_doctor_master docmst ON "
		+ "	bill.created_by = docmst.user_id "
		+ " WHERE "
		+ "	bill.bill_status_id IN (:billStatusIds) "
		+ "	AND bill.is_dr_note IN (:isDrCr) "
		+ "	AND bill.bill_cancelled = 'N' "
		+ "	AND bill.organisation_id = :orgId "
		+ "	AND bill.unit_id = :unitId "
		+ "	AND bill.patient_id = :patientId "
		+ " ORDER BY "
		+ "	bill.bill_date_time DESC"),
		

		@NamedNativeQuery(name = "UPDATE_OUTSTANDING_AMOUNT_BY_BILL_ID_FOR_COMPANY_INVOICE_SETTLEMENT", query = "update billing.m_billing_master set "
				+ "outstanding = :amount, "
				+ "updated_by =:updatedBy, "
				+ "updated_date=now() "
				+ "where billing_master_id =:billId "),
		
		
		//Ganesh
		@NamedNativeQuery(name = "UPDATE_PHARAMCY_OUTSTANDING_AMOUNT_BY_BILL_ID_FOR_COMPANY_INVOICE_SETTLEMENT", query = "update " + 
				"	pharmacy.t_sale " + 
				" set " + 
				"	outstanding_amount = :amount," + 
				"	updated_by =:updatedBy," + 
				"	updated_date = now() " + 
				" where " + 
				"	id  =:saleId"),
		

        @NamedNativeQuery(name = "CHECK_OUTSTANDING_IS_ZERO_FOR_SETTLEMENT_OF_BILL1", query = "select exists(select bill.billing_master_id from billing.m_billing_master bill where bill.outstanding <=0 and bill.billing_master_id = :billId)"),

		@NamedNativeQuery(name = "CHECK_OUTSTANDING_IS_ZERO_FOR_COMP_SETTLEMENT_OF_BILL", query = " select exists(select bill.bill_payee_details_id from billing.t_bill_payee_details bill where  bill.payment_entitlement_id <> 1 and bill.due_amount =:reciveAmount  and bill.bill_id = :billId)"),

		@NamedNativeQuery(name = "CHECK_OUTSTANDING_IS_ZERO_FOR_SETTLEMENT_OF_BILL", query = " select exists(select bill.bill_payee_details_id from billing.t_bill_payee_details bill where bill.due_amount <=0 and bill.payment_entitlement_id  = 1 and bill.bill_id = :billId)"),

		
		@NamedNativeQuery(name = "UPDATE_BILL_STATUS_ID_IN_BILL_MASTER_BY_BILL_ID", 
		query = "update billing.m_billing_master set bill_status_id = :statusId"
				+ " where billing_master_id =:billId  "),
		
		 @NamedNativeQuery(name = "GET_BILL_DETAILS_FOR_BOOK_VIEW",
		 query =   " select " 
				 + " 	bill.billing_master_id as \"billingMasterId\", " 
				 + " 	to_char(bill.bill_date_time, 'DD-MM-YYYY HH24:MI:SS') as \"billDate\", " 
				 + " 	bill.bill_number as \"billNumber\", " 
				 + " 	pat.uhid_number as \"uhidNumber\", " 
				 + " 	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", " 
				 + " 	bill.visit_type_id as \"visitTypeId\", " 
				 + " 	concat(empmst.first_name, ' ', empmst.last_name) as \"userName\", " 
				 + " 	pat.is_otc_reg as \"isOtcReg\", " 
				 + " 	bill.organisation_id as \"orgId\", " 
				 + " 	bill.unit_id as \"unitId\", " 
				 + " 	bill.patient_id as \"patientId\", " 
				 + " 	bill.is_dr_note as \"isDrNote\", " 
				 + " 	billagainst.bill_number as \"againstbillNumber\", " 
				 + " 	coalesce( case when bill.visit_type_id = 1 then ( select enc.encounter_number from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id ) end, case when bill.visit_type_id = 2 or bill.visit_type_id = 3 or bill.visit_type_id = 4 then ( select adm.admission_number from adt.t_admission adm where adm.admission_id = bill.admission_id ) end ) as \"visitNo\", " 
				 + " 	coalesce( case when bill.visit_type_id = 1 then ( select to_char(enc.encounter_date, 'DD/MM/YYYY') from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id ) end, case when bill.visit_type_id = 2 or bill.visit_type_id = 3 or bill.visit_type_id = 4 then ( select to_char(adm.doa, 'DD/MM/YYYY') from adt.t_admission_details adm where adm.admission_id = bill.admission_id and adm.status = 'A' ) end ) as \"visitDate\", " 
				 + " 	coalesce( case when bill.visit_type_id = 1 then ( select enc.encounter_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id ) end, case when bill.visit_type_id = 2 or bill.visit_type_id = 3 or bill.visit_type_id = 4 then ( select adm.admission_id from adt.t_admission adm where adm.admission_id = bill.admission_id ) end ) as \"visitId\", " 
				 + " 	coalesce( case when bill.visit_type_id = 1 then ( select enc.appointment_id from opd.t_encounter_master enc where enc.encounter_id = bill.encounter_id ) end ) as \"appointmentId\", " 
				 + " 	bill.total_bill_amount as \"totalBillAmount\", " 
				 + " 	bill.net_amount as \"netAmount\", " 
				 + " 	coalesce((bill.net_amount - bill.outstanding), 0) as \"amountPaid\", " 
				 + " 	bill.outstanding as \"outstanding\", " 
				 + " 	to_char(bill.created_date, 'YYYY-MM-DD') as \"createdDate\" , " 
				 + " 	bill.bill_status_id as \"billStatusId\" " 
				 + " from " 
				 + " 	billing.m_billing_master bill " 
				 + " left join billing.m_billing_master billagainst on " 
				 + " 	bill.dr_note_against_billid = billagainst.billing_master_id " 
				 + " inner join patient.t_patient_registration pat on " 
				 + " 	pat.patient_id = bill.patient_id " 
				 + " left join m_prefix_master pre on " 
				 + " 	pre.prefix_id = pat.prefix_id " 
				 + " left join billing.t_billing_payment_details billpymt on " 
				 + " 	billpymt.billing_master_id = bill.billing_master_id " 
				 + " left join PUBLIC.m_employee_master empmst on " 
				 + " 	bill.created_by = empmst.user_id " 
				 + " where " 
				 + " 	bill.bill_cancelled = 'N' " 
				 + " 	and bill.organisation_id = :orgId " 
				 + " 	and bill.unit_id = :unitId " 
				 + " 	and bill.patient_id = :patientId " 
				 + " 	and to_char(bill.bill_date_time, 'YYYY-MM-DD') >= :fromDate " 
				 + " 	and to_char(bill.bill_date_time, 'YYYY-MM-DD') <= :toDate "),

		@NamedNativeQuery(name = "GET_BILL_DETAILS_FOR_BILL_LIST_PATIENT_MYLIFE",
				query =   "     select "
						+ "  	bill.billing_master_id as \"billingMasterId\", "
						+ "  	tem.appointment_id as \"appointmentId\", "
						+ "  	pat.uhid_number as \"uhid\", "
						+ "  	bill.bill_number as \"billNumber\", "
						+ "  	to_char(bill.bill_date_time, 'DD-MM-YYYY') as \"billDate\", "
						+ "  	bill.net_amount as \"netAmount\", "
						+ "  	bill.patient_id as \"patientId\", "
						+ "  	to_char(tem.encounter_date, 'DD-MM-YYYY') as \"appointmentDate\", "
						+ "  	( select max(bill_status_id) from "
						+ "  		billing.t_billing_status_mapper where "
						+ "  		bill_id = bill.billing_master_id) as \"billStatusId\" , "
						+ "  	coalesce((bill.net_amount - bill.outstanding), 0) as \"paidAmount\", "
						+ "  	billstatus.bill_status_desc as \"billStatusDesc\", "
						+ "  	bill.unit_id as \"unitId\", "
						+ "  	bill.organisation_id as \"hospitalId\", "
						+ "  	to_char(tem.encounter_time, 'HH24:MI:SS') as \"encounterTime\", "
						+ "  	(case when tem.doctor_id is not null then concat('Dr.',' ',docmst.first_name, ' ', docmst.middle_name, ' ', docmst.last_name) "
						+ "		 else 'Hospital Visit' end) as \"doctorName\", "
						+ "  	currancy.currency_name as \"currency\", "
						+ "  	docmst.doctor_id as \"doctorId\", "
						+ "  	bill.outstanding as \"outstanding\", "
						+ "  	concat(coalesce(pre.prefix_code,''),' ',pat.first_name, ' ', pat.middle_name, ' ', pat.last_name) as \"patientName\", "
						+ "  	unitmst.unit_name as \"unitName\", "
						+ "  	orgmst.organization_name as \"hospitalName\" "
						+ "  from "
						+ "  	billing.m_billing_master bill "
						+ "  inner join patient.t_patient_registration pat on "
						+ "  	pat.patient_id = bill.patient_id "
						+ "  left join m_prefix_master pre on "
						+ "  	pre.prefix_id = pat.prefix_id "
						+ "  left join adt.t_admission adm on "
						+ "  	bill.admission_id = adm.admission_id "
						+ "  left join public.m_employee_master emp_mst on "
						+ "  	emp_mst.user_id = bill.created_by "
						+ " left join opd.t_encounter_master tem on "
						+ " 	bill.encounter_id = tem.encounter_id "
						+ " left join doctor.m_doctor_master docmst on "
						+ " 	tem.doctor_id = docmst.doctor_id "
						+ " left join public.m_unit_master unitmst on "
						+ " 	bill.unit_id = unitmst.unit_id "
						+ " left join public.m_organization_master orgmst on "
						+ " 	bill.organisation_id = orgmst.organization_id "
						+ " left join public.m_currency_master currancy on  "
						+ " 	bill.organisation_id = currancy.orgnisation_id and currancy.status='A' "
						+ " left join billing.m_billing_status_master billstatus on  "
						+ " 	bill.bill_status_id = billstatus.bill_status_id "
						+ "  where "
						+ "  	bill.bill_cancelled = 'N' "
						+ "  	and bill.visit_type_id = 1 "
						+ "  	and bill.patient_id = :patientId ")

})

@Entity
@Table(name = "m_billing_master", schema = "billing")
@SequenceGenerator(name = "billing_master_seq", sequenceName = "billing.billing_master_seq", allocationSize = 1)
public class BillingMaster {
	@Id
	@Column(name = "billing_master_id")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "billing_master_seq")
	private Integer billingMasterId;

	@Column(name = "encounter_id")
	private Integer encounterId;

	@Column(name = "visit_type_id")
	private Integer visitTypeId;

	@Column(name = "patient_id")
	private Integer patientId;

	@Column(name = "bill_number")
	private String billNumber;

	@Column(name = "bill_date_time")
	@Convert(converter = DateConverter.class)
	private Long billDateTime;

	@Column(name = "net_amount")
	private Double netAmount;

	@Column(name = "discount_amount")
	private Double discountAmount;

	@Column(name = "concession_amount")
	private Double concessionAmount;

	@Column(name = "gst")
	private Double gst;

	@Column(name = "total_bill_amount")
	private Double totalBillAmount;

	@Column(name = "outstanding")
	private Double outstanding;

	@Column(name = "round_off")
	private Double roundOff;

	@Column(name = "is_credit_bill")
	private Character isCreditBill;

	@Column(name = "is_invoice_creation")
	private Character isInvoiceCreation;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organisation_id")
	private Integer organisationId;

	@Column(name = "status")
	private Character status;

	@Column(name = "created_date")
	@Convert(converter = DateConverter.class)
	private Long createdDate;

	@Column(name = "created_by")
	private Integer createdBy;

	@Column(name = "updated_date")
	@Convert(converter = DateConverter.class)
	private Long updatedDate;

	@Column(name = "updated_by")
	private Integer updatedBy;

	@Column(name = "bill_status_id")
	private Integer billStatusId;

	@Column(name = "cr_amount")
	private Double crAmount;

	@Column(name = "settlement_date")
	@Convert(converter = DateConverter.class)
	private Long settlementDate;

	@Column(name = "bill_cancelled")
	private Character billCancelled;
	
	@Column(name = "is_addendum_bill")
	private Character isAddendumBill;
	
	@Column(name = "old_bill_id")
	private Integer oldBillId;
	
	@Column(name = "is_doctor_referral_posted")
	private Character isDoctorReferralPosted;

	@Column(name = "bill_cancel_reason_id")
	private Integer billCancelReasonId;

	@Column(name = "bill_cancel_datetime")
	@Convert(converter = DateConverter.class)
	private Long billCancelDatetime;

	@Column(name = "bill_cancel_by")
	private Integer billCancelBy;

	@Column(name = "bill_cancel_remark")
	private String billCancelRemark;

	@Column(name = "admission_id")
	private Integer admissionId;

	@Column(name = "is_interim_or_final_bill")
	private Integer isInterimOrFinalBill;

	@Column(name = "is_dr_note")
	private char isDrNote;

	@Column(name = "dr_note_against_billid")
	private Integer drNoteAgainstBillId;
	
	@Column(name="counter_id")
	private Integer counterId;
	
	@Column(name="is_intermediate_bill")
	private Character isIntermediateBill;
	
	@Column(name = "total_self_payable")
	private Double selfPayable;
	
	@Column(name = "total_credit_payable")
	private Double creditPayable;
	
	@Column(name = "disallowed_amount")
	private Double disallowedAmount;
	
	@Column(name = "other_deduction")
	private Double otherDeduction;
	
	@Column(name="disallowed_against_invoice_dtl_id")
	private Integer disallowedAgainstInvoiceDtlsId;
	
	@Column(name = "against_bill_id")
	private Integer againstBillId;
	
	@Column(name = "is_bill_reopened")
	private Character isBillReopened;
	
	@Column(name = "bill_remark")
	private String billRemark;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "encounter_id", insertable = false, nullable = false, updatable = false)
	private EncounterMaster encounter;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "admission_id", insertable = false, nullable = false, updatable = false)
	private Admission admission;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "patient_id", insertable = false, nullable = false, updatable = false)
	private PatientRegistration patientRegistration;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "billingMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BillingPaymentDetails> billingPaymentDetailsList;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "billingMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BillingDetails> billingDetailsList;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "billingMaster", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<BillPayeeDetails> billPayeeDetailsList;

	public String getBillRemark() {
		return billRemark;
	}

	public void setBillRemark(String billRemark) {
		this.billRemark = billRemark;
	}
	
	

	public Double getOtherDeduction() {
		return otherDeduction;
	}

	public void setOtherDeduction(Double otherDeduction) {
		this.otherDeduction = otherDeduction;
	}

	public Integer getAgainstBillId() {
		return againstBillId;
	}

	public void setAgainstBillId(Integer againstBillId) {
		this.againstBillId = againstBillId;
	}

	public Character getIsBillReopened() {
		return isBillReopened;
	}

	public void setIsBillReopened(Character isBillReopened) {
		this.isBillReopened = isBillReopened;
	}

	public List<BillPayeeDetails> getBillPayeeDetailsList() {
		return billPayeeDetailsList;
	}

	public void setBillPayeeDetailsList(List<BillPayeeDetails> billPayeeDetailsList) {
		this.billPayeeDetailsList = billPayeeDetailsList;
	}

	public Double getDisallowedAmount() {
		return disallowedAmount;
	}

	public void setDisallowedAmount(Double disallowedAmount) {
		this.disallowedAmount = disallowedAmount;
	}

	public Integer getDisallowedAgainstInvoiceDtlsId() {
		return disallowedAgainstInvoiceDtlsId;
	}

	public void setDisallowedAgainstInvoiceDtlsId(Integer disallowedAgainstInvoiceDtlsId) {
		this.disallowedAgainstInvoiceDtlsId = disallowedAgainstInvoiceDtlsId;
	}

	public Character getIsIntermediateBill() {
		return isIntermediateBill;
	}

	public void setIsIntermediateBill(Character isIntermediateBill) {
		this.isIntermediateBill = isIntermediateBill;
	}

	public Integer getIsInterimOrFinalBill() {
		return isInterimOrFinalBill;
	}

	public void setIsInterimOrFinalBill(Integer isInterimOrFinalBill) {
		this.isInterimOrFinalBill = isInterimOrFinalBill;
	}

	public Integer getAdmissionId() {
		return admissionId;
	}

	public void setAdmissionId(Integer admissionId) {
		this.admissionId = admissionId;
	}

	public Character getBillCancelled() {
		return billCancelled;
	}

	public void setBillCancelled(Character billCancelled) {
		this.billCancelled = billCancelled;
	}

	public Integer getBillingMasterId() {
		return billingMasterId;
	}

	public void setBillingMasterId(Integer billingMasterId) {
		this.billingMasterId = billingMasterId;
	}

	public Integer getEncounterId() {
		return encounterId;
	}

	public void setEncounterId(Integer encounterId) {
		this.encounterId = encounterId;
	}

	public Integer getVisitTypeId() {
		return visitTypeId;
	}

	public void setVisitTypeId(Integer visitTypeId) {
		this.visitTypeId = visitTypeId;
	}

	public Integer getPatientId() {
		return patientId;
	}

	public void setPatientId(Integer patientId) {
		this.patientId = patientId;
	}

	public String getBillNumber() {
		return billNumber;
	}

	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
	}

	public Long getBillDateTime() {
		return billDateTime;
	}

	public void setBillDateTime(Long billDateTime) {
		this.billDateTime = billDateTime;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public Double getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(Double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public Double getConcessionAmount() {
		return concessionAmount;
	}

	public void setConcessionAmount(Double concessionAmount) {
		this.concessionAmount = concessionAmount;
	}

	public Double getGst() {
		return gst;
	}

	public void setGst(Double gst) {
		this.gst = gst;
	}

	public Double getTotalBillAmount() {
		return totalBillAmount;
	}

	public void setTotalBillAmount(Double totalBillAmount) {
		this.totalBillAmount = totalBillAmount;
	}

	public Double getOutstanding() {
		return outstanding;
	}

	public void setOutstanding(Double outstanding) {
		this.outstanding = outstanding;
	}

	public Double getRoundOff() {
		return roundOff;
	}

	public void setRoundOff(Double roundOff) {
		this.roundOff = roundOff;
	}

	public Character getIsCreditBill() {
		return isCreditBill;
	}

	public void setIsCreditBill(Character isCreditBill) {
		this.isCreditBill = isCreditBill;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = (status != null && status != '\u0000') ? status : 'A';
	}

	public Long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Long updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getBillStatusId() {
		return billStatusId;
	}

	public void setBillStatusId(Integer billStatusId) {
		this.billStatusId = billStatusId;
	}

	public Double getCrAmount() {
		return crAmount;
	}

	public void setCrAmount(Double crAmount) {
		this.crAmount = crAmount;
	}

	public Long getSettlementDate() {
		return settlementDate;
	}

	public void setSettlementDate(Long settlementDate) {
		this.settlementDate = settlementDate;
	}

	public Integer getBillCancelReasonId() {
		return billCancelReasonId;
	}

	public void setBillCancelReasonId(Integer billCancelReasonId) {
		this.billCancelReasonId = billCancelReasonId;
	}

	public Long getBillCancelDatetime() {
		return billCancelDatetime;
	}

	public void setBillCancelDatetime(Long billCancelDatetime) {
		this.billCancelDatetime = billCancelDatetime;
	}

	public Integer getBillCancelBy() {
		return billCancelBy;
	}

	public void setBillCancelBy(Integer billCancelBy) {
		this.billCancelBy = billCancelBy;
	}

	public String getBillCancelRemark() {
		return billCancelRemark;
	}

	public void setBillCancelRemark(String billCancelRemark) {
		this.billCancelRemark = billCancelRemark;
	}

	public Character getIsDoctorReferralPosted() {
		return isDoctorReferralPosted;
	}

	public void setIsDoctorReferralPosted(Character isDoctorReferralPosted) {
		this.isDoctorReferralPosted = (isDoctorReferralPosted == null || isDoctorReferralPosted == '\u0000') ? 'N'
				: isDoctorReferralPosted;
	}

	public Character getIsInvoiceCreation() {
		return isInvoiceCreation;
	}

	public void setIsInvoiceCreation(Character isInvoiceCreation) {
		this.isInvoiceCreation = (isInvoiceCreation != null && isInvoiceCreation != '\u0000') ? isInvoiceCreation : 'N' ;
	}

	public char getIsDrNote() {
		return isDrNote;
	}

	public void setIsDrNote(char isDrNote) {
		this.isDrNote = (isDrNote != '\u0000' ? isDrNote : 'N');
	}

	public Integer getDrNoteAgainstBillId() {
		return drNoteAgainstBillId;
	}

	public void setDrNoteAgainstBillId(Integer drNoteAgainstBillId) {
		this.drNoteAgainstBillId = drNoteAgainstBillId;
	}
	public Integer getCounterId() {
		return counterId;
	}

	public void setCounterId(Integer counterId) {
		this.counterId = counterId;
	}

	public Character getIsAddendumBill() {
		return isAddendumBill;
	}

	public void setIsAddendumBill(Character isAddendumBill) {
		this.isAddendumBill = isAddendumBill;
	}

	public Integer getOldBillId() {
		return oldBillId;
	}

	public void setOldBillId(Integer oldBillId) {
		this.oldBillId = oldBillId;
	}

	public Double getSelfPayable() {
		return selfPayable;
	}

	public void setSelfPayable(Double selfPayable) {
		this.selfPayable = selfPayable;
	}

	public Double getCreditPayable() {
		return creditPayable;
	}

	public void setCreditPayable(Double creditPayable) {
		this.creditPayable = creditPayable;
	}
	
}
