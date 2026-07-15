package com.param.billing.global.model;

import java.math.BigDecimal;
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

import com.param.adt.master.global.model.RoleMaster;
import com.param.adt.master.global.model.UserMaster;
@NamedQueries({
	
	@NamedQuery(name="GET_DISCOUNT_RIGHTS_MASTER_LIST",
		query=" SELECT  dm.discountRightId AS discountRightId, "
				+ " dm.userId AS userId, "
				+ " dm.fromDiscountAmount AS fromDiscountAmount, "
				+ " dm.toDiscountAmount AS toDiscountAmount, "	
				+ " dm.status AS status, dm.organisationId AS organisationId, "
				+" (case when ( em.employeeId is not null )then "
				+ " (concat(coalesce(pre.prefixCode),' ',coalesce(em.firstName), ' ', coalesce(em.middleName), ' ', coalesce(em.lastName)))"
				+" else "
				+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) AS employeeName,"
				+ " (case "
				+ "		when em.employeeId is not null then (coalesce(em.empCode))"
				+"		else (coalesce( doc.docCode ))"
				+"	end )as employeeCode,"
//				+ " em.empCode AS employeeCode,"
				+ " dm.unitId AS unitId, "
				+ " rm.roleDesc AS roleName, "
				+ " dm.isFinancialClearance as isFinancialClearance, "
				+ " dm.isCreditNoteApproval as isCreditNoteApproval, "
				+ " dm.isDiscountRights as isDiscountRights, "
				+ " dm.discountType as discountType, "
				+ " dm.badDebitType as badDebitType, "
				+ " dm.financialClearanceType as financialClearanceType, "
				+ " dm.creditNoteType as creditNoteType, "
				+ " dm.expenseApprovalType as expenseApprovalType, "
				+ " dm.isBadDebit as isBadDebit, "
				+ " dm.fromBadDebitAmount as fromBadDebitAmount, "
				+ " dm.toBadDebitAmount as toBadDebitAmount, "
				+ " dm.fromFinancialClearanceAmount as fromFinancialClearanceAmount, "
				+ " dm.toFinancialClearanceAmount as toFinancialClearanceAmount, "
				+ " dm.fromCreditNoteAmount as fromCreditNoteAmount, "
				+ " dm.toCreditNoteAmount as toCreditNoteAmount, "
				+ " dm.fromExpenseApprovalAmount as fromExpenseApprovalAmount, "
				+ " dm.toExpenseApprovalAmount as toExpenseApprovalAmount, "
				+ " dm.isExpenseApproval as isExpenseApproval, "
				+ " dm.isUHIDMergeApproval as isUHIDMergeApproval, "
				+ " dm.isRefundApproval as isRefundApproval,"
				+ " dm.fromRefundAmount as fromRefundAmount,"
				+ " dm.toRefundAmount as toRefundAmount ,"
			    + " dm.refundType as refundType"
				+ " FROM DiscountRightsMaster dm"
				+ " INNER JOIN dm.userMaster um"
				+ " LEFT JOIN dm.roleMaster rm"
				+ " LEFT JOIN um.employeeMastersList em "
				+ " LEFT JOIN em.prefixMaster pre "
				+ " LEFT JOIN um.doctorMastersList doc "
				+ " WHERE dm.organisationId= :organisationId"
				+ " AND dm.unitId= :unitId "
				//+ " AND dm.status='A' "
			),
	
	/*@NamedQuery(name="GET_DISCOUNT_RIGHTS_MASTER_BY_ID",
	query=" SELECT  dm.discountRightId AS discountRightId, "
				+ " dm.userId AS userId, "
				+ " dm.fromDiscountAmount AS fromDiscountAmount, "
				+ " dm.toDiscountAmount AS toDiscountAmount, "	
				+ " dm.status AS status, dm.organisationId AS organisationId, concat(em.firstName, ' ', em.middleName, ' ', em.lastName ) AS employeeName, "
				+ " em.empCode AS employeeCode, "
				+ " dm.unitId AS unitId, "
				+ " dm.isFinancialClearance as isFinancialClearance, "
				+ " dm.isCreditNoteApproval as isCreditNoteApproval "
				+ " FROM DiscountRightsMaster dm "
				+ " INNER JOIN dm.userMaster um"
				+ " INNER JOIN um.employeeMastersList em"
				+ " WHERE dm.organisationId= :organisationId"
				+ " AND dm.unitId= :unitId"
				+ " AND dm.discountRightId= :discountRightId"
			),*/
	
	@NamedQuery(name="GET_DISCOUNT_RIGHTS_MASTER_BY_ID",
	query = " SELECT  dm.discountRightId AS discountRightId, "
			+ " dm.userId AS userId, "
			+ " dm.roleId AS roleId, "
			+ " dm.fromDiscountAmount AS fromDiscountAmount, "
			+ " dm.toDiscountAmount AS toDiscountAmount, "	
			+ " dm.status AS status, dm.organisationId AS organisationId, "
			+" (case when ( em.employeeId is not null )then "
			+ " (concat(coalesce(pre.prefixCode),' ',coalesce(em.firstName), ' ', coalesce(em.middleName), ' ', coalesce(em.lastName)))"
			+" else "
			+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) AS employeeName,"
			+ " (case "
			+ "		when em.employeeId is not null then (coalesce(em.empCode))"
			+"		else (coalesce( doc.docCode ))"
			+"	end )as employeeCode,"
			//+ " em.empCode AS employeeCode,"
			+ " dm.unitId AS unitId, "
			+ " dm.isFinancialClearance as isFinancialClearance, "
			+ " dm.isCreditNoteApproval as isCreditNoteApproval, "
			+ " dm.isBadDebit as isBadDebit, "
			+ " dm.fromBadDebitAmount as fromBadDebitAmount, "
			+ " dm.toBadDebitAmount as toBadDebitAmount, "
			+ " dm.fromFinancialClearanceAmount as fromFinancialClearanceAmount, "
			+ " dm.toFinancialClearanceAmount as toFinancialClearanceAmount, "
			+ " dm.fromCreditNoteAmount as fromCreditNoteAmount, "
			+ " dm.toCreditNoteAmount as toCreditNoteAmount, "
			+ " dm.isDiscountRights as isDiscountRights, "
			+ " dm.discountType as discountType, "
			+ " dm.badDebitType as badDebitType, "
			+ " dm.financialClearanceType as financialClearanceType, "
			+ " dm.creditNoteType as creditNoteType, "
			+ " dm.expenseApprovalType as expenseApprovalType, "
			+ " dm.fromExpenseApprovalAmount as fromExpenseApprovalAmount, "
			+ " dm.toExpenseApprovalAmount as toExpenseApprovalAmount, "
			+ " dm.isExpenseApproval as isExpenseApproval, "
			+ " dm.isUHIDMergeApproval as isUHIDMergeApproval ,"
			+ " dm.isRefundApproval as isRefundApproval,"
			+ " dm.fromRefundAmount as fromRefundAmount,"
			+ " dm.toRefundAmount as toRefundAmount ,"
			+ " dm.refundType as refundType"
			+ " FROM DiscountRightsMaster dm"
			+ " INNER JOIN dm.userMaster um"
			+ " LEFT JOIN um.employeeMastersList em "
			+ " LEFT JOIN em.prefixMaster pre "
			+ " LEFT JOIN um.doctorMastersList doc "
			+ " WHERE dm.organisationId= :organisationId"
			+ " AND dm.unitId= :unitId "
			+ " AND dm.discountRightId= :discountRightId"),
	
	@NamedQuery(name="GET_ACTIVE_DISCOUNT_RIGHTS_MASTER",
			query=" SELECT  dm.discountRightId AS discountRightId, dm.userId AS userId, "
				+ " dm.fromDiscountAmount AS fromDiscountAmount, dm.toDiscountAmount AS toDiscountAmount,"	
				+ " dm.status AS status, dm.organisationId AS organisationId,"
				+ " dm.unitId AS unitId"
				+ " FROM DiscountRightsMaster dm"
				+ " WHERE dm.status= 'A'"
			),
	
	@NamedQuery(name="CHECK_DISCOUNT_RIGHTS_MASTER_AVAILABLE",
	query=" SELECT dm.discountRightId as discountRightId "
				+ " FROM DiscountRightsMaster dm"
				+ " WHERE dm.userId= :userId  AND dm.unitId=:unitId AND dm.organisationId=:orgId"
				+ " AND dm.status='A' "
				
			),
	
	@NamedQuery(name="GET_USERS_DISC_RIGHTS",query=" SELECT  dm.discountRightId AS discountRightId, "
			+ " dm.userId AS userId, "
			+ " dm.isBadDebit as isBadDebit, "
			+ " dm.fromBadDebitAmount as fromBadDebitAmount, "
			+ " dm.toBadDebitAmount as toBadDebitAmount, "
			+ " dm.fromFinancialClearanceAmount as fromFinancialClearanceAmount, "
			+ " dm.toFinancialClearanceAmount as toFinancialClearanceAmount, "
			+ " dm.fromCreditNoteAmount as fromCreditNoteAmount, "
			+ " dm.toCreditNoteAmount as toCreditNoteAmount, "
			+ " dm.fromExpenseApprovalAmount as fromExpenseApprovalAmount, "
			+ " dm.toExpenseApprovalAmount as toExpenseApprovalAmount, "
			+ " dm.fromDiscountAmount AS fromDiscountAmount, "
			+ " dm.discountType AS discountType, "
			+ " dm.toDiscountAmount AS toDiscountAmount "	
			+ " FROM DiscountRightsMaster dm"
			+ " WHERE dm.organisationId= :orgId "
			+ " AND dm.unitId=:unitId "
			+ " AND dm.userId=:userId "
			+ " AND dm.status='A' "),
	
	@NamedQuery(name="GET_USERS_LIST_HAVING_CREDIT_RIGHTS",query=" SELECT  dm.discountRightId AS discountRightId, "
				+ " dm.userId AS userId, "
				+" (case when ( em.employeeId is not null )then "
				+ " (concat(coalesce(pre.prefixCode),' ',coalesce(em.firstName), ' ', coalesce(em.middleName), ' ', coalesce(em.lastName)))"
				+" else " 
				+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) AS employeeName "
				+ " FROM DiscountRightsMaster dm "
				+ " INNER JOIN dm.userMaster um "				
				+ " LEFT JOIN um.employeeMastersList em "
				+ " LEFT JOIN em.prefixMaster pre "
				+ " LEFT JOIN um.doctorMastersList doc "
				+ " WHERE dm.organisationId= :orgId"
				+ " AND dm.unitId= :unitId "
				+ " AND ((:amount BETWEEN dm.fromCreditNoteAmount AND dm.toCreditNoteAmount AND dm.creditNoteType = 2)"
				+ " OR (:percentage BETWEEN dm.fromCreditNoteAmount AND dm.toCreditNoteAmount AND dm.creditNoteType = 1))"
				+ " AND dm.status='A' "),
	
	@NamedQuery(name="GET_USERS_LIST_HAVING_DISC_RIGHTS",query=" SELECT  dm.discountRightId AS discountRightId, "
			+ " dm.userId AS userId, "
			+" (case when ( em.employeeId is not null )then "
			+ " (concat(coalesce(pre.prefixCode),' ',coalesce(em.firstName), ' ', coalesce(em.middleName), ' ', coalesce(em.lastName)))"
			+" else " 
			+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) AS employeeName "
			+ " FROM DiscountRightsMaster dm "
			+ " INNER JOIN dm.userMaster um "				
			+ " LEFT JOIN um.employeeMastersList em "
			+ " LEFT JOIN em.prefixMaster pre "
			+ " LEFT JOIN um.doctorMastersList doc "
			+ " WHERE dm.organisationId= :orgId"
			+ " AND dm.unitId= :unitId "
			+ " AND ((:amount BETWEEN dm.fromDiscountAmount AND dm.toDiscountAmount AND dm.discountType = 2)"
			+ " OR (:percentage BETWEEN dm.fromDiscountAmount AND dm.toDiscountAmount AND dm.discountType = 1))"
			+ " AND dm.status='A' "),
	
	@NamedQuery(name="GET_USERS_LIST_HAVING_DISC_RIGHTS_FOR_BAD_DEBIT",query=" SELECT  dm.discountRightId AS discountRightId, "
			+ " dm.userId AS userId, "
			+" (case when ( em.employeeId is not null )then "
			+ " (concat(coalesce(pre.prefixCode),' ',coalesce(em.firstName), ' ', coalesce(em.middleName), ' ', coalesce(em.lastName)))"
			+" else "
			+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) AS employeeName "
			+ " FROM DiscountRightsMaster dm "
			+ " INNER JOIN dm.userMaster um "				
			+ " LEFT JOIN um.employeeMastersList em "
			+ " LEFT JOIN em.prefixMaster pre "
			+ " LEFT JOIN um.doctorMastersList doc "
			+ " WHERE dm.organisationId= :orgId"
			+ " AND dm.unitId= :unitId "
			+ " AND :amount BETWEEN dm.fromBadDebitAmount AND dm.toBadDebitAmount "
			+ " AND dm.status='A' "),
	
	
	@NamedQuery(name="GET_USERS_LIST_HAVING_DISC_RIGHTS_FOR_CREDIT_NOTE",query=" SELECT  dm.discountRightId AS discountRightId, "
			+ " dm.userId AS userId, "
			+" (case when ( em.employeeId is not null )then "
			+ " (concat(coalesce(pre.prefixCode),' ',coalesce(em.firstName), ' ', coalesce(em.middleName), ' ', coalesce(em.lastName)))"
			+" else "
			+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) AS employeeName "
			+ " FROM DiscountRightsMaster dm "
			+ " INNER JOIN dm.userMaster um "				
			+ " LEFT JOIN um.employeeMastersList em "
			+ " LEFT JOIN em.prefixMaster pre "
			+ " LEFT JOIN um.doctorMastersList doc "
			+ " WHERE dm.organisationId= :orgId"
			+ " AND dm.unitId= :unitId "
			+ " AND :amount BETWEEN dm.fromCreditNoteAmount AND dm.toCreditNoteAmount "
			+ " AND dm.status='A' "),
	
	@NamedQuery(name="GET_USERS_LIST_CREDIT_NOTE_RIGHTS",query=" SELECT  dm.discountRightId AS discountRightId, "
			+ " dm.userId AS userId, "
			+" (case when ( em.employeeId is not null )then "
			+ " (concat(coalesce(pre.prefixCode),' ',coalesce(em.firstName), ' ', coalesce(em.middleName), ' ', coalesce(em.lastName)))"
			+" else "
			+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) AS employeeName "
			+ " FROM DiscountRightsMaster dm"
			+ " INNER JOIN dm.userMaster um"
			+ " LEFT JOIN um.employeeMastersList em"
			+ " LEFT JOIN em.prefixMaster pre "
			+ " LEFT JOIN um.doctorMastersList doc "
			+ " WHERE dm.organisationId= :orgId"
			+ " AND dm.unitId= :unitId "
			+ " AND dm.isCreditNoteApproval = 'Y' "
			+ " AND ((:amount BETWEEN dm.fromCreditNoteAmount AND dm.toCreditNoteAmount))"
			+ " AND dm.status='A' "),
	
	@NamedQuery(name="GET_USERS_LIST_REFUND_RIGHTS",query=" SELECT  dm.discountRightId AS discountRightId, "
			+ " dm.userId AS userId, "
			+" (case when ( em.employeeId is not null )then "
			+ " (concat(coalesce(pre.prefixCode),' ',coalesce(em.firstName), ' ', coalesce(em.middleName), ' ', coalesce(em.lastName)))"
			+" else "
			+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) AS employeeName "
			+ " FROM DiscountRightsMaster dm"
			+ " INNER JOIN dm.userMaster um"
			+ " INNER JOIN um.employeeMastersList em"
			+ " LEFT JOIN em.prefixMaster pre "
			+ " LEFT JOIN um.doctorMastersList doc "
			+ " WHERE dm.organisationId= :orgId"
			+ " AND dm.unitId= :unitId "
			+ " AND dm.isExpenseApproval = 'Y' "
			+ " AND dm.status='A' "),

		@NamedQuery(name="GET_USERS_LIST_DEPOSIT_REFUND_RIGHTS",query=" SELECT  dm.discountRightId AS discountRightId, "
				+ " dm.userId AS userId, "
				+" (case when ( em.employeeId is not null )then "
				+ " (concat(coalesce(pre.prefixCode),' ',coalesce(em.firstName), ' ', coalesce(em.middleName), ' ', coalesce(em.lastName)))"
				+" else "
				+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) AS employeeName "
				+ " FROM DiscountRightsMaster dm"
				+ " INNER JOIN dm.userMaster um"
				+ " INNER JOIN um.employeeMastersList em"
				+ " LEFT JOIN em.prefixMaster pre "
				+ " LEFT JOIN um.doctorMastersList doc "
				+ " WHERE dm.organisationId= :orgId"
				+ " AND dm.unitId= :unitId "
				+ " AND dm.isRefundApproval = 'Y' "
				+ " AND dm.status='A' "),
	
	@NamedQuery(name="GET_USERS_LIST_DEPOSIT_REFUND_RIGHTS_WITH_AMOUNT",query=" SELECT  dm.discountRightId AS discountRightId, "
			+ " dm.userId AS userId, "
			+" (case when ( em.employeeId is not null )then "
			+ " (concat(coalesce(pre.prefixCode),' ',coalesce(em.firstName), ' ', coalesce(em.middleName), ' ', coalesce(em.lastName)))"
			+" else "
			+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) AS employeeName "
			+ " FROM DiscountRightsMaster dm"
			+ " INNER JOIN dm.userMaster um"
			+ " LEFT JOIN um.employeeMastersList em"
			+ " LEFT JOIN em.prefixMaster pre "
			+ " LEFT JOIN um.doctorMastersList doc "
			+ " WHERE dm.organisationId= :orgId"
			+ " AND dm.unitId= :unitId "
			+ " AND dm.isRefundApproval = 'Y' "
			+ " AND :amount BETWEEN dm.fromRefundAmount AND dm.toRefundAmount "
			+ " AND dm.status='A' "),

	
	@NamedQuery(name="GET_REFUND_RIGHTS",query="SELECT dm.isExpenseApproval as isExpenseApproval "
			+ " FROM DiscountRightsMaster dm"
			+ " WHERE dm.unitId= :unitId "
			+ " AND dm.organisationId = :orgId "
			+ " AND dm.userId = :userId "
			+ " AND dm.status='A' "),

		@NamedQuery(name="GET_DEPOSIT_REFUND_RIGHTS",query="SELECT count(dm.isRefundApproval)"
				+ " FROM DiscountRightsMaster dm"
				+ " WHERE dm.unitId= :unitId "
				+ " AND dm.organisationId = :orgId "
				+ " AND dm.userId = :userId "
				+ " AND dm.status='A'  "
				+ " AND dm.isRefundApproval = 'Y' "
				+ " AND :amount BETWEEN dm.fromRefundAmount AND dm.toRefundAmount "),
	
	@NamedQuery(name="GET_USERS_HAVING_FINANCIAL_CLEARANCE_FLAG",query=" SELECT  dm.discountRightId AS discountRightId, "
			+ " dm.userId AS userId, "
			+" (case when ( em.employeeId is not null )then "
			+ " (concat(coalesce(pre.prefixCode),' ',coalesce(em.firstName), ' ', coalesce(em.middleName), ' ', coalesce(em.lastName)))"
			+" else "
			+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) AS employeeName "
			+ " FROM DiscountRightsMaster dm"
			+ " INNER JOIN dm.userMaster um"
			+ " LEFT JOIN um.employeeMastersList em "
			+ " LEFT JOIN em.prefixMaster pre "
			+ " LEFT JOIN um.doctorMastersList doc "
			+ " WHERE dm.organisationId= :orgId"
			+ " AND dm.unitId= :unitId "
			+ " AND dm.isFinancialClearance='Y' "
			+ " AND :amount BETWEEN dm.fromFinancialClearanceAmount AND dm.toFinancialClearanceAmount "
			+ " AND dm.status='A' "),
	
	@NamedQuery(name="GET_CREDIT_NOTE_RIGHTS",query="SELECT dm.isCreditNoteApproval as isCreditNoteApproval, "
			+ " dm.fromCreditNoteAmount as fromCreditNoteAmount, "
			+ " dm.toCreditNoteAmount as toCreditNoteAmount, "
			+ " dm.discountType as discountType, "
			+ " dm.badDebitType as badDebitType, "
			+ " dm.financialClearanceType as financialClearanceType, "
			+ " dm.creditNoteType as creditNoteType, "
			+ " dm.expenseApprovalType as expenseApprovalType ,"
			+ " dm.fromDiscountAmount AS fromDiscountAmount, "
			+ " dm.toDiscountAmount AS toDiscountAmount "	
			+ " FROM DiscountRightsMaster dm"
			+ " WHERE dm.unitId= :unitId "
			+ " AND dm.organisationId = :orgId "
			+ " AND dm.userId = :userId "
			+ " AND dm.status='A' and dm.isCreditNoteApproval = 'Y '"),
	
	@NamedQuery(name="GET_USERS_LIST_HAVING_RIGHTS_FOR_EXPENSE",query=" SELECT  dm.discountRightId AS discountRightId, "
			+ " dm.userId AS userId, "
			+" (case when ( em.employeeId is not null )then "
			+ " (concat(coalesce(pre.prefixCode),' ',coalesce(em.firstName), ' ', coalesce(em.middleName), ' ', coalesce(em.lastName)))"
			+" else "
			+ " (concat('Dr. ',coalesce(doc.firstName), ' ', coalesce(doc.middleName), ' ', coalesce(doc.lastName))) end ) AS employeeName "
			+ " FROM DiscountRightsMaster dm"
			+ " INNER JOIN dm.userMaster um"
			+ " INNER JOIN um.employeeMastersList em"
			+ " LEFT JOIN em.prefixMaster pre "
			+ " LEFT JOIN um.doctorMastersList doc "
			+ " WHERE dm.organisationId= :orgId"
			+ " AND dm.unitId= :unitId "
			+ " AND dm.isExpenseApproval = 'Y' "
			+ " AND :amount BETWEEN dm.fromExpenseApprovalAmount AND dm.toExpenseApprovalAmount "
			+ " AND dm.status='A' "),

	

//	@NamedQuery(name="GET_USER_LIST",query="select user_id as userId ,"
//	            +" is_uhid_merge_approval as UHID Approval ,"
//	            +"from billing.m_discount_rights"   
//	            + "where is_uhid_merge_approval ='Y' "),
	
	
	
	

	
})
		@NamedNativeQueries({
			
			
		@NamedNativeQuery(name="GET_USERS_LIST_BY_ROLE_ID",
		query="select "
		 +" 	uum.user_id as \"userId\", "
		 +" 	(concat(coalesce(prefix.prefix_desc), ' ', coalesce(em.first_name), ' ', coalesce(em.middle_name), ' ', coalesce(em.last_name))) as \"employeeName\", "
		 +" 	em.emp_code as \"employeeCode\" "
		 +" 	from public.t_user_role_mapper mpr "
		 /*+" inner join public.m_user_master um on "
		 +" 	mpr.user_id = um.user_id "*/
		 +" inner join public.m_employee_master em on "
		 +" 	em.user_id = mpr.user_id "
		 +" inner join public.m_prefix_master prefix on "
		 +" 	em.prefix_id = prefix.prefix_id "
		 +" inner join t_user_unit_mapper uum on "
		 +" 	uum.user_id = em.user_id "
		 +" where "
		 +" 	uum.organisation_id =:orgId "
		 +" 	and uum.unit_id =:unitId "
		 +" 	and mpr.role_id =:roleId "
		 +" 	and mpr.status = 'A' "
		 +" 	and uum.status = 'A' "
		 +" union  "
		 +" select "
		 +" 	uum.user_id as \"userId\", "
		 +" 	(concat('Dr. ',coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name))) as \"employeeName\", "
		 +" 	doc.doc_code as \"employeeCode\" "
		 +" 	from public.t_user_role_mapper mpr "
		/* +" inner join public.m_user_master um on "
		 +" 	mpr.user_id = um.user_id "*/
		 +" inner join doctor.m_doctor_master doc on "
		 +" 	doc.user_id = mpr.user_id "
		 +" inner join t_user_unit_mapper uum on "
		 +" 	uum.user_id = doc.user_id "
		 +" where "
		 +" 	uum.organisation_id =:orgId "
		 +" 	and uum.unit_id =:unitId "
		 +" 	and mpr.role_id =:roleId "
		 +" 	and mpr.status = 'A' "
		 +" 	and uum.status = 'A' "
		 + " ORDER BY \"employeeName\" ASC "

),
@NamedNativeQuery(name="GET_USERS_LIST",
		query="select "
		 +" 	uum.user_id as \"userId\", "
		 +" 	(concat(coalesce(prefix.prefix_code), ' ', coalesce(em.first_name), ' ', coalesce(em.middle_name), ' ', coalesce(em.last_name))) as \"employeeName\", "
		 +" 	em.emp_code as \"employeeCode\" "
		 +" 	from public.t_user_role_mapper mpr "
		 /*+" inner join public.m_user_master um on "
		 +" 	mpr.user_id = um.user_id "*/
		 +" inner join public.m_employee_master em on "
		 +" 	em.user_id = mpr.user_id "
		 +" inner join public.m_prefix_master prefix on "
		 +" 	em.prefix_id = prefix.prefix_id "
		 +" inner join t_user_unit_mapper uum on "
		 +" 	uum.user_id = em.user_id "
		 +" where "
		 +" 	uum.organisation_id =:orgId "
		 +" 	and uum.unit_id =:unitId "
		 +" 	and mpr.status = 'A' "
		 +" 	and uum.status = 'A' "
		 +" union  "
		 +" select "
		 +" 	uum.user_id as \"userId\", "
		 +" 	(concat('Dr. ',coalesce(doc.first_name), ' ', coalesce(doc.middle_name), ' ', coalesce(doc.last_name))) as \"employeeName\", "
		 +" 	doc.doc_code as \"employeeCode\" "
		 +" 	from public.t_user_role_mapper mpr "
		 /*+" inner join public.m_user_master um on "
		 +" 	mpr.user_id = um.user_id "*/
		 +" inner join doctor.m_doctor_master doc on "
		 +" 	doc.user_id = mpr.user_id "
		 +" inner join t_user_unit_mapper uum on "
		 +" 	uum.user_id = doc.user_id "
		 +" where "
		 +" 	uum.organisation_id =:orgId "
		 +" 	and uum.unit_id =:unitId "
		 +" 	and uum.status = 'A' "
		 +" 	and mpr.status = 'A' "),




@NamedNativeQuery(name="GET_UHID_REQUEST_USERS_LIST",
		query="select "
		+" mdr.user_id as \"userId\", "
	   + " mdr.discount_right_id as \"discountRightId\", "
	   + " ( "
	   + " case "
	   + " when mem.employee_id is not null then concat( mem.first_name, ' ', mem.last_name ) "
	   + " when mdm.doctor_id is not null then concat( 'Dr. ', mdm.first_name, ' ', mdm.last_name ) "
	   + " else 'Admin' "
	   + " end ) as userName "
       + " from"
	   + " billing.m_discount_rights mdr "
       + " left join public.m_employee_master mem on "
	   + " mdr.user_id = mem.user_id"
       + " left join doctor.m_doctor_master mdm on "
	   + " mdr.user_id = mdm.user_id"
       + " where"
	   + " mdr.organisation_id =:organizationId "
	   + " 	and mdr.unit_id =:unitId "
	   + " and mdr.is_uhid_merge_approval = 'Y' ")


})

@Entity
@Table(name = "m_discount_rights", schema = "billing")
@SequenceGenerator(name = "m_seq_discount_rights", sequenceName = "billing.m_seq_discount_rights", allocationSize = 1)
public class DiscountRightsMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_seq_discount_rights")
	@Column(name = "discount_right_id")
	private Integer discountRightId;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "from_discount_amount")
	private BigDecimal fromDiscountAmount;
	
	@Column(name = "to_discount_amount")
	private BigDecimal toDiscountAmount;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "organisation_id")
	private Integer organisationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name="is_financial_clearance")
	private Character isFinancialClearance;
	
	@Column(name="is_credit_note_approval")
	private Character isCreditNoteApproval;

	@Column(name="is_expense_approval")
	private Character isExpenseApproval;
	
	@Column(name="is_bad_debit")
	private Character isBadDebit;

	@Column(name="from_bad_debit_amount")
	private BigDecimal fromBadDebitAmount;
	
	@Column(name="to_bad_debit_amount")
	private BigDecimal toBadDebitAmount;
	
	@Column(name="from_financial_clearance_amount")
	private BigDecimal fromFinancialClearanceAmount;
	
	@Column(name="to_financial_clearance_amount")
	private BigDecimal toFinancialClearanceAmount;
	
	@Column(name="from_credit_note_amount")
	private BigDecimal fromCreditNoteAmount;
	
	@Column(name="to_credit_note_amount")
	private BigDecimal toCreditNoteAmount;
	
	@Column(name="from_expense_approval_amount")
	private BigDecimal fromExpenseApprovalAmount;
	
	@Column(name="to_expense_approval_amount")
	private BigDecimal toExpenseApprovalAmount;
	
	@Column(name="role_id")
	private Integer roleId;
	
	@Column(name="is_discount_rights")
	private Character isDiscountRights;
	
	
	@Column(name = "discount_type")
	private Integer discountType;
	
	@Column(name = "badDebit_type")
	private Integer badDebitType;
	
	@Column(name = "financial_clearance_type")
	private Integer financialClearanceType;
	
	@Column(name = "credit_note_type")
	private Integer creditNoteType;

	
	@Column(name = "expense_approval_type")
	private Integer expenseApprovalType;
	              
	@Column(name="is_uhid_merge_approval")
	private Character isUHIDMergeApproval;

	@Column(name = "refund_type")
	private Integer refundType;

	@Column(name="is_refund_approval")
	private Character isRefundApproval;

	@Column(name="from_refund_amount")
	private BigDecimal fromRefundAmount;

	@Column(name="to_refund_amount")
	private BigDecimal toRefundAmount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserMaster userMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id", insertable = false, updatable = false)
	private RoleMaster roleMaster;
	
	public Character getIsDiscountRights() {
		return isDiscountRights;
	}

	public void setIsDiscountRights(Character isDiscountRights) {
		this.isDiscountRights = isDiscountRights;
	}

	public Integer getDiscountType() {
		return discountType;
	}

	public void setDiscountType(Integer discountType) {
		this.discountType = discountType;
	}

	public Integer getBadDebitType() {
		return badDebitType;
	}

	public void setBadDebitType(Integer badDebitType) {
		this.badDebitType = badDebitType;
	}

	public Integer getFinancialClearanceType() {
		return financialClearanceType;
	}

	public void setFinancialClearanceType(Integer financialClearanceType) {
		this.financialClearanceType = financialClearanceType;
	}

	public Integer getCreditNoteType() {
		return creditNoteType;
	}

	public void setCreditNoteType(Integer creditNoteType) {
		this.creditNoteType = creditNoteType;
	}

	public Integer getExpenseApprovalType() {
		return expenseApprovalType;
	}

	public void setExpenseApprovalType(Integer expenseApprovalType) {
		this.expenseApprovalType = expenseApprovalType;
	}

	public BigDecimal getFromFinancialClearanceAmount() {
		return fromFinancialClearanceAmount;
	}

	public void setFromFinancialClearanceAmount(BigDecimal fromFinancialClearanceAmount) {
		this.fromFinancialClearanceAmount = fromFinancialClearanceAmount;
	}

	public BigDecimal getToFinancialClearanceAmount() {
		return toFinancialClearanceAmount;
	}

	public void setToFinancialClearanceAmount(BigDecimal toFinancialClearanceAmount) {
		this.toFinancialClearanceAmount = toFinancialClearanceAmount;
	}

	public BigDecimal getFromCreditNoteAmount() {
		return fromCreditNoteAmount;
	}

	public void setFromCreditNoteAmount(BigDecimal fromCreditNoteAmount) {
		this.fromCreditNoteAmount = fromCreditNoteAmount;
	}

	public BigDecimal getToCreditNoteAmount() {
		return toCreditNoteAmount;
	}

	public void setToCreditNoteAmount(BigDecimal toCreditNoteAmount) {
		this.toCreditNoteAmount = toCreditNoteAmount;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Character getIsBadDebit() {
		return isBadDebit;
	}

	public void setIsBadDebit(Character isBadDebit) {
		this.isBadDebit = isBadDebit;
	}

	public BigDecimal getFromBadDebitAmount() {
		return fromBadDebitAmount;
	}

	public void setFromBadDebitAmount(BigDecimal fromBadDebitAmount) {
		this.fromBadDebitAmount = fromBadDebitAmount;
	}

	public BigDecimal getToBadDebitAmount() {
		return toBadDebitAmount;
	}

	public void setToBadDebitAmount(BigDecimal toBadDebitAmount) {
		this.toBadDebitAmount = toBadDebitAmount;
	}

	public Character getIsFinancialClearance() {
		return isFinancialClearance;
	}

	public void setIsFinancialClearance(Character isFinancialClearance) {
		this.isFinancialClearance = isFinancialClearance;
	}

	public Integer getDiscountRightId() {
		return discountRightId;
	}

	public void setDiscountRightId(Integer discountRightId) {
		this.discountRightId = discountRightId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getFromDiscountAmount() {
		return fromDiscountAmount;
	}

	public void setFromDiscountAmount(BigDecimal fromDiscountAmount) {
		this.fromDiscountAmount = fromDiscountAmount;
	}

	public BigDecimal getToDiscountAmount() {
		return toDiscountAmount;
	}

	public void setToDiscountAmount(BigDecimal toDiscountAmount) {
		this.toDiscountAmount = toDiscountAmount;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
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

	public Integer getOrganisationId() {
		return organisationId;
	}

	public void setOrganisationId(Integer organisationId) {
		this.organisationId = organisationId;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}
	
	public Character getIsExpenseApproval() {
		return isExpenseApproval;
	}

	public void setIsExpenseApproval(Character isExpenseApproval) {
		this.isExpenseApproval = isExpenseApproval;
	}

	public BigDecimal getFromExpenseApprovalAmount() {
		return fromExpenseApprovalAmount;
	}

	public void setFromExpenseApprovalAmount(BigDecimal fromExpenseApprovalAmount) {
		this.fromExpenseApprovalAmount = fromExpenseApprovalAmount;
	}

	public BigDecimal getToExpenseApprovalAmount() {
		return toExpenseApprovalAmount;
	}

	public void setToExpenseApprovalAmount(BigDecimal toExpenseApprovalAmount) {
		this.toExpenseApprovalAmount = toExpenseApprovalAmount;
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

	public Character getIsCreditNoteApproval() {
		return isCreditNoteApproval;
	}

	public void setIsCreditNoteApproval(Character isCreditNoteApproval) {
		this.isCreditNoteApproval = isCreditNoteApproval;
	}
	
	public Character getIsUHIDMergeApproval() {
		return isUHIDMergeApproval;
	}

	public void setIsUHIDMergeApproval(Character isUHIDMergeApproval) {
		this.isUHIDMergeApproval = isUHIDMergeApproval;
	}

	public Character getIsRefundApproval() {
		return isRefundApproval;
	}

	public void setIsRefundApproval(Character isRefundApproval) {
		this.isRefundApproval = isRefundApproval;
	}

	public BigDecimal getFromRefundAmount() {
		return fromRefundAmount;
	}

	public void setFromRefundAmount(BigDecimal fromRefundAmount) {
		this.fromRefundAmount = fromRefundAmount;
	}

	public BigDecimal getToRefundAmount() {
		return toRefundAmount;
	}

	public void setToRefundAmount(BigDecimal toRefundAmount) {
		this.toRefundAmount = toRefundAmount;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}
}
