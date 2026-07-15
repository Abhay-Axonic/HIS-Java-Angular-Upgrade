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


import com.param.adt.master.global.model.UserMaster;

@NamedQueries({ 
			
				@NamedQuery(name = "GET_ACTIVE_USER_ROLE_MAPPER_LIST",
						query = "SELECT  "
								+ " userRoleMapper.userRoleMpprId as userRoleMpprId, userRoleMapper.userId as userId, "
								+ " userRoleMapper.roleId as roleId, userRoleMapper.organisationId AS organisationId, "
								+ " userRoleMapper.status AS status, userRoleMapper.unitId AS unitId "
								+ " FROM UserRoleMapper userRoleMapper"
								+ " WHERE userRoleMapper.status= 'A' "
								+ " AND userRoleMapper.unitId= :unitId "
								+ " AND userRoleMapper.organisationId= :organisationId"
	
							
								),
	
				@NamedQuery(name = "GET_USER_ROLE_MAPPER_BY_NAME",
						query = "SELECT  "
								+ " userRoleMapper.userRoleMpprId as userRoleMpprId, "
								+ " userRoleMapper.userId as userId, "
								+ " userRoleMapper.roleId as roleId "
								+ " FROM UserRoleMapper userRoleMapper "
								+ " WHERE userRoleMapper.userId= :userId "
//								+ " AND userRoleMapper.roleId= :roleId"
								+ " AND userRoleMapper.unitId= :unitId "
								+ " AND userRoleMapper.organisationId= :organisationId"
								+ " AND (userRoleMapper.status='A' OR userRoleMapper.status='I') "
								),
				
			
				
				
			})



@NamedNativeQueries({
						@NamedNativeQuery(name="GET_USER_ROLE_MAPPER_LIST",
										query=" select "
												+ " distinct on "
												+ " (d.user_id) d.doc_code as \"empCode\", "
												+ " concat (d.first_name, "
												+ " ' ', "
												+ " d.middle_name, "
												+ " ' ', "
												+ " d.last_name) as \"empName\" , "
												+ " d.mobile_number as \"contactNo\", "
												+ " a.user_id as \"userId\", "
												+ " a.user_role_mppr_id as \"userRoleMpprId\", "
												+ " a.status as \"status\", "
												+ " d.doctor_id as \"doctorId\", "
												+ " c.dashboard_desc as \"dashboardDesc\", "
												+ " mppr.mis_report_ids as \"misReportIds\", "
												+ " mppr.tab_access_ids as \"tabAccessIds\", "
												+ " mppr.tab_access_ids_IPD as \"tabAccessIdsIPD\", "
												+ " (case "
												+ " when ( "
												+ " select "
												+ " count(rolemappr.role_id) "
												+ " from "
												+ " public.t_user_role_mapper rolemappr "
												+ " inner join public.m_role_master rolemstr on "
												+ " rolemappr.role_id = rolemstr.role_id "
												+ " where "
												+ " rolemstr.role_type_id in(8, "
												+ " 9, "
												+ " 10, "
												+ " 11, "
												+ " 12) "
												+ " and rolemappr.user_id = a.user_id "
												+ " and rolemappr.status = 'A') > 0 then true "
												+ " else false end) as \"isOtUser\" "
												+ " from "
												+ " public.t_user_role_mapper a "
												+ " inner join doctor.m_doctor_master d on "
												+ " d.user_id = a.user_id "
												+ " inner join public.t_user_unit_mapper mppr on "
												+ " mppr.user_id = a.user_id "
												+ " and mppr.unit_id = a.unit_id "
												+ " and mppr.status = 'A' "
												+ " left join public.m_dashboard_master c on "
												+ " c.dashboard_id = a.dashboard_id "
												+ " where "
												+ " a.organisation_id =:organisationId "
												+ " and a.unit_id =:unitId "
												+ " and (a.status = 'A' "
												+ " or a.status = 'I') "
												+ " union "
												+ " select "
												+ " distinct on "
												+ " (b.user_id) b.emp_code as \"empCode\", "
												+ " concat (b.first_name, "
												+ " ' ', "
												+ " b.middle_name, "
												+ " ' ', "
												+ " b.last_name) as \"empName\" , "
												+ " b.mobile_number as \"contactNo\", "
												+ " a.user_id as \"userId\", "
												+ " a.user_role_mppr_id as \"userRoleMpprId\", "
												+ " a.status as \"status\" , "
												+ " 0 as \"doctorId\" , "
												+ " c.dashboard_desc as \"dashboardDesc\", "
												+ " mppr.mis_report_ids as \"misReportIds\", "
												+ " mppr.tab_access_ids as \"tabAccessIds\", "
												+ " mppr.tab_access_ids_IPD as \"tabAccessIdsIPD\", "
												+ " (case "
												+ " when ( "
												+ " select "
												+ " count(rolemappr.role_id) "
												+ " from "
												+ " public.t_user_role_mapper rolemappr "
												+ " inner join public.m_role_master rolemstr on "
												+ " rolemappr.role_id = rolemstr.role_id "
												+ " where "
												+ " rolemstr.role_type_id in(8, "
												+ " 9, "
												+ " 10, "
												+ " 11, "
												+ " 12) "
												+ " and rolemappr.user_id = a.user_id "
												+ " and rolemappr.status = 'A') > 0 then true "
												+ " else false end) as \"isOtUser\" "
												+ " from "
												+ " public.t_user_role_mapper a "
												+ " inner join public.m_employee_master b on "
												+ " b.user_id = a.user_id "
												+ " inner join public.t_user_unit_mapper mppr on "
												+ " mppr.user_id = a.user_id "
												+ " and mppr.unit_id = a.unit_id "
												+ " and mppr.status = 'A' "
												+ " left join public.m_dashboard_master c on "
												+ " c.dashboard_id = a.dashboard_id "
												+ " where "
												+ " a.organisation_id =:organisationId "
												+ " and a.unit_id =:unitId "
												+ " and (a.status = 'A' "
												+ " or a.status = 'I') "
								),

						@NamedNativeQuery(name="GET_USER_ROLE_MAPPER_COUNT",
							query=" select count(*)"
									+ " from "
									+ " public.t_user_role_mapper a "
									+ " inner join doctor.m_doctor_master d on "
									+ " d.user_id = a.user_id "
									+ " inner join public.t_user_unit_mapper mppr on "
									+ " mppr.user_id = a.user_id "
									+ " and mppr.unit_id = a.unit_id "
									+ " and mppr.status = 'A' "
									+ " left join public.m_dashboard_master c on "
									+ " c.dashboard_id = a.dashboard_id "
									+ " where "
									+ " a.organisation_id =:organisationId "
									+ " and a.unit_id =:unitId "
									+ " and (a.status = 'A' "
									+ " or a.status = 'I') "
									+ " union "
									+ " select count(*)"
									+ " from "
									+ " public.t_user_role_mapper a "
									+ " inner join public.m_employee_master b on "
									+ " b.user_id = a.user_id "
									+ " inner join public.t_user_unit_mapper mppr on "
									+ " mppr.user_id = a.user_id "
									+ " and mppr.unit_id = a.unit_id "
									+ " and mppr.status = 'A' "
									+ " left join public.m_dashboard_master c on "
									+ " c.dashboard_id = a.dashboard_id "
									+ " where "
									+ " a.organisation_id =:organisationId "
									+ " and a.unit_id =:unitId "
									+ " and (a.status = 'A' "
									+ " or a.status = 'I') "
						),

						@NamedNativeQuery(name="GET_USER_ROLE_MAPPER_BY_ID",
							query="select userRoleMapper.user_role_mppr_id as \"userRoleMpprId\", CONCAT  (doctorMaster.first_name, ' ', doctorMaster.middle_name, ' ',doctorMaster.last_name) as \"empName\" ,"
								+ " userRoleMapper.user_id as \"userId\", userRoleMapper.status as \"status\" ,  userRoleMapper.role_id as \"roleId\" ,c.dashboard_desc as \"dashboardDesc\"  "
								+ " from  public.t_user_role_mapper userRoleMapper "
								+ " inner join  doctor.m_doctor_master doctorMaster on userRoleMapper.user_id=doctorMaster.user_id "
								+ " left join  public.m_dashboard_master c on c.dashboard_id=userRoleMapper.dashboard_id "													
								+ " where userRoleMapper.organisation_id =:organisationId "
								+ " and userRoleMapper.unit_id =:unitId"
								+ " and userRoleMapper.user_role_mppr_id =:userRoleMpprId" 
						
								+ " union "
								
								+ " select userRoleMapper.user_role_mppr_id as \"userRoleMpprId\", CONCAT  (employeeMaster.first_name, ' ', employeeMaster.middle_name, ' ',employeeMaster.last_name) as \"empName\" ,"
								+ " userRoleMapper.user_id as \"userId\", userRoleMapper.status as \"status\" ,  userRoleMapper.role_id as \"roleId\"  ,c.dashboard_desc as \"dashboardDesc\"  "
								+ " from  public.t_user_role_mapper userRoleMapper "
								+ " inner join  public.m_employee_master employeeMaster on userRoleMapper.user_id=employeeMaster.user_id "
								+ " left join  public.m_dashboard_master c on c.dashboard_id=userRoleMapper.dashboard_id "													
								+ " where userRoleMapper.organisation_id =:organisationId "
								+ " and userRoleMapper.unit_id =:unitId"
								+ " and userRoleMapper.user_role_mppr_id =:userRoleMpprId" 
								),
						@NamedNativeQuery(name="GET_USER_ROLE_BY_ID",
						query="select distinct ( userRoleMapper.user_id) as \"userId\",   " + 
								"CONCAT  (doctorMaster.first_name, ' ', doctorMaster.middle_name, ' ',doctorMaster.last_name) as \"empName\" ,  " + 
								//" userRoleMapper.user_id as \"userId\",   " + 
								" userRoleMapper.status as \"status\" ,    " + 
								" userRoleMapper.role_id as \"roleId\" ,  " + 
								" tuum.is_rendered_service_cancelled as \"isRenderedServiceCancelled\" ,  " + 
								" tuum.is_update_patient_reg as \"isUpdatePatientReg\" ,tuum.update_discharge_summary as \"updateDischargeSummary\" ,  " + 
								" tuum.is_update_ipd_charges_remark as \"isUpdateIpdChargesRemark\" ,  " +
								" tuum.opd_bill_cancellation as \"opdBillCancellation\" ,  " +
								" tuum.ipd_bill_cancellation as \"ipdBillCancellation\" ,  " +
								" tuum.bill_settlement as \"billSettlement\" ,  " +
								" tuum.ip_bill_settlement as \"ipBillSettlement\", " +
								" tuum.service_rate_editable as \"serviceRateEditable\", " +
								" tuum.is_force_update_service_package as \"isForceUpdateServicePackage\", " +
								" tuum.pharmacy_bill_cancellation as \"pharmacyBillCancellation\", " +
								" tuum.pharmacy_receipt_cancellation as \"pharmacyReceiptCancellation\", " +
								" tuum.sample_recollection as \"sampleRecollection\", " +
								" tuum.nursing_op_scan as \"nursingOpScan\", " +
								" tuum.nursing_ip_scan as \"nursingIpScan\", " +
								" tuum.doctor_op_scan as \"doctorOpScan\", " +
								" tuum.doctor_ip_scan as \"doctorIpScan\", " +
								" tuum.change_payee_rights as \"changePayeeRights\", " +
								" tuum.extend_pdd as \"extendPdd\", " +
								" userRoleMapper.is_recoll as \"isRecoll\",   " + 
								" userRoleMapper.is_ris_recall as \"isRisRecall\",   " + 
								" userRoleMapper.manual_mpesa as \"manualMpesa\" ,  " + 
								" userRoleMapper.enable_block_print as \"enableBlockPrint\" ,  " + 
								" tuum.ip_rendered_date_change as \"ipRenderedDateChange\",   " + 
								" tuum.is_bill_manipulation_allowed as \"isBillManipulationAllowed\",   " +
								" tuum.deposit_receipt_cancellation as \"depositReceiptCancellation\",   " +
								"c.dashboard_id AS \"dashboardId\", " +
								"c.dashboard_desc AS \"dashboardDesc\" " +
								" from  public.t_user_role_mapper userRoleMapper   " + 
								" inner join  doctor.m_doctor_master doctorMaster on userRoleMapper.user_id=doctorMaster.user_id   " + 
								" inner join  public.t_user_unit_mapper tuum on userRoleMapper.user_id=tuum.user_id   " +
								"LEFT JOIN public.m_dashboard_master c ON c.dashboard_id = userRoleMapper.dashboard_id " +
								" where userRoleMapper.organisation_id =:organisationId   " + 
								" and userRoleMapper.unit_id =:unitId  "
								+ " and tuum.unit_id = :unitId  " + 
								" and userRoleMapper.user_id =:userId  " + 
								" and (userRoleMapper.status='A' OR userRoleMapper.status='I')   " + 
								" and tuum.status ='A'" + 
								" union   " + 
								"  " + 
								" select distinct ( userRoleMapper.user_id) as \"userId\",   " + 
								" CONCAT  (employeeMaster.first_name, ' ', employeeMaster.middle_name, ' ',employeeMaster.last_name) as \"empName\" ,  " + 
								//" userRoleMapper.user_id as \"userId\",   " + 
								" userRoleMapper.status as \"status\" ,   " + 
								" userRoleMapper.role_id as \"roleId\",  " + 
								" tuum.is_rendered_service_cancelled as \"isRenderedServiceCancelled\" ,  " + 
								" tuum.is_update_patient_reg as \"isUpdatePatientReg\" ,tuum.update_discharge_summary as \"updateDischargeSummary\" ,  " + 
								" tuum.is_update_ipd_charges_remark as \"isUpdateIpdChargesRemark\" ,  " + 
								" tuum.opd_bill_cancellation as \"opdBillCancellation\" ,  " +
								" tuum.ipd_bill_cancellation as \"ipdBillCancellation\" ,  " +
								" tuum.bill_settlement as \"billSettlement\" ,  " +
								" tuum.ip_bill_settlement as \"ipBillSettlement\", " +
								" tuum.service_rate_editable as \"serviceRateEditable\", " +
								" tuum.is_force_update_service_package as \"isForceUpdateServicePackage\", " +
								" tuum.pharmacy_bill_cancellation as \"pharmacyBillCancellation\", " +
								" tuum.pharmacy_receipt_cancellation as \"pharmacyReceiptCancellation\", " +
								" tuum.sample_recollection as \"sampleRecollection\", " +
								" tuum.nursing_op_scan as \"nursingOpScan\", " +
								" tuum.nursing_ip_scan as \"nursingIpScan\", " +
								" tuum.doctor_op_scan as \"doctorOpScan\", " +
								" tuum.doctor_ip_scan as \"doctorIpScan\", " +
								" tuum.change_payee_rights as \"changePayeeRights\", " +
								" tuum.extend_pdd as \"extendPdd\", " +
								" userRoleMapper.is_recoll as \"isRecoll\" ,  " + 
								" userRoleMapper.is_ris_recall as \"isRisRecall\" ,  " + 
								" userRoleMapper.manual_mpesa as \"manualMpesa\"  , " + 
								" userRoleMapper.enable_block_print as \"enableBlockPrint\" ,  " + 
								" tuum.ip_rendered_date_change as \"ipRenderedDateChange\" ,  " +
								" tuum.is_bill_manipulation_allowed as \"isBillManipulationAllowed\",   " +
								" tuum.deposit_receipt_cancellation as \"depositReceiptCancellation\",   " +
								"c.dashboard_id AS \"dashboardId\", " +
								"c.dashboard_desc AS \"dashboardDesc\" " +
								" from  public.t_user_role_mapper userRoleMapper   " + 
								" inner join  public.m_employee_master employeeMaster on userRoleMapper.user_id=employeeMaster.user_id   " + 
								"  inner join  public.t_user_unit_mapper tuum on userRoleMapper.user_id=tuum.user_id   " +
								" LEFT JOIN public.m_dashboard_master c ON c.dashboard_id = userRoleMapper.dashboard_id " +
								" where userRoleMapper.organisation_id =:organisationId   " +
								" and userRoleMapper.unit_id =:unitId  " 
								+ " and tuum.unit_id = :unitId  " + 
								" and userRoleMapper.user_id =:userId  " + 
								" and (userRoleMapper.status='A' OR userRoleMapper.status='I') "+
								" and tuum.status ='A'"
							),
						/*@NamedNativeQuery(name="GET_USER_AUTHENTICATION_LIST",
						query=" select distinct on(usrMaster.user_id) doctorMaster.doc_code as \"empCode\", CONCAT  (doctorMaster.first_name, ' ', doctorMaster.middle_name, ' ',doctorMaster.last_name) as \"empName\" , doctorMaster.mobile_number as \"contactNo\","
						+ " usrRoleMppr.user_role_mppr_id as \"userRoleMpprId\",  roleMaster.role_id as \"roleId\", roleMaster.role_name as \"roleName\" ,"
						+" usrMaster.user_name as \"userName\",usrMaster.password as \"password\",usrMaster.status as \"status\",doctorMaster.email as \"emailId\",doctorMaster.first_name as \"firstName\" "
						+ " from  public.t_user_role_mapper usrRoleMppr "
						+ " inner join  doctor.m_doctor_master doctorMaster on doctorMaster.user_id=usrRoleMppr.user_id "
						+ " inner join public.m_role_master roleMaster on usrRoleMppr.role_id=roleMaster.role_id  "
						+ " inner join public.m_user_master usrMaster on usrMaster.user_id=doctorMaster.user_id "				
						+ " where usrRoleMppr.organisation_id =:organisationId "
						+ " and usrRoleMppr.unit_id =:unitId "
						+ " and (usrRoleMppr.status='A' OR usrRoleMppr.status='I') "						 
						+ " UNION "
						+ "  select distinct on(usrMaster.user_id) employeeMaster.emp_code as \"empCode\", CONCAT  (employeeMaster.first_name, ' ', employeeMaster.middle_name, ' ',employeeMaster.last_name) as \"empName\" , employeeMaster.mobile_number as \"contactNo\","
						+ "  usrRoleMppr.user_role_mppr_id as \"userRoleMpprId\",  roleMaster.role_id as \"roleId\", roleMaster.role_name as \"roleName\", "
						+"  usrMaster.user_name as \"userName\",usrMaster.password as \"password\",usrMaster.status as \"status\",employeeMaster.email as \"emailId\" ,employeeMaster.first_name as \"firstName\" "
						+ " from  public.t_user_role_mapper usrRoleMppr "
						+ " inner join  public.m_employee_master employeeMaster on employeeMaster.user_id=usrRoleMppr.user_id "
						+ " inner join public.m_role_master roleMaster on usrRoleMppr.role_id=roleMaster.role_id  "
						+ " inner join public.m_user_master usrMaster on usrMaster.user_id=employeeMaster.user_id "
						+ " where usrRoleMppr.organisation_id =:organisationId "
						+ " and usrRoleMppr.unit_id =:unitId "
						+ " and (usrRoleMppr.status='A' OR usrRoleMppr.status='I') " 
				),*/
						@NamedNativeQuery(name="GET_USER_LIST_BY_ROLE_ID",
						query="select userRoleMapper.user_role_mppr_id as \"userRoleMpprId\", usrMaster.user_name as \"userName\" ,"
							+ " userRoleMapper.user_id as \"userId\", userRoleMapper.status as \"status\" ,  userRoleMapper.role_id as \"roleId\","
							+ " CONCAT(doctorMaster.first_name, ' ', doctorMaster.middle_name, ' ',doctorMaster.last_name) as \"empName\" "
							+ " from  public.t_user_role_mapper userRoleMapper "
							+ " inner join  doctor.m_doctor_master doctorMaster on userRoleMapper.user_id=doctorMaster.user_id "
							+ " inner join public.m_user_master usrMaster on usrMaster.user_id=doctorMaster.user_id "
							+ " where userRoleMapper.organisation_id =:orgId "
							+ " and userRoleMapper.unit_id =:unitId"
							+ " and userRoleMapper.role_id =:roleId" 
							+ " and userRoleMapper.status='A' "
							
							+ " union "
							
							+ " select userRoleMapper.user_role_mppr_id as \"userRoleMpprId\", usrMaster.user_name as \"userName\", "
							+ " userRoleMapper.user_id as \"userId\", userRoleMapper.status as \"status\" ,  userRoleMapper.role_id as \"roleId\","
							+ " CONCAT  (employeeMaster.first_name, ' ', employeeMaster.middle_name, ' ',employeeMaster.last_name) as \"empName\" "
							+ " from  public.t_user_role_mapper userRoleMapper "
							+ " inner join  public.m_employee_master employeeMaster on userRoleMapper.user_id=employeeMaster.user_id "
							+ " inner join public.m_user_master usrMaster on usrMaster.user_id=employeeMaster.user_id "
							+ " where userRoleMapper.organisation_id =:orgId "
							+ " and userRoleMapper.unit_id =:unitId"
							+ " and userRoleMapper.role_id =:roleId" 
							+ " and userRoleMapper.status='A' "
							+ " ORDER By \"empName\" ASC "
							),
						@NamedNativeQuery(name="GET_USER_ROLE_NAME_BY_USER_ID",
						query="select userRoleMapper.user_role_mppr_id as \"userRoleMpprId\",roleMaster.role_name as \"roleName\", "
							+ " userRoleMapper.user_id as \"userId\", userRoleMapper.status as \"status\" ,  userRoleMapper.role_id as \"roleId\" "
							+ " from  public.t_user_role_mapper userRoleMapper "
							+ " inner join public.m_role_master roleMaster on userRoleMapper.role_id=roleMaster.role_id  "
							+ " where userRoleMapper.organisation_id =:orgId "
							+ " and userRoleMapper.unit_id =:unitId"
							+ " and userRoleMapper.user_id =:userId" 
							+ " and (userRoleMapper.status='A' OR userRoleMapper.status='I') "
							
							+ " union "
							
							+ " select userRoleMapper.user_role_mppr_id as \"userRoleMpprId\",roleMaster.role_name as \"roleName\", "
							+ " userRoleMapper.user_id as \"userId\", userRoleMapper.status as \"status\" ,  userRoleMapper.role_id as \"roleId\" "
							+ " from  public.t_user_role_mapper userRoleMapper "
							+ " inner join public.m_role_master roleMaster on userRoleMapper.role_id=roleMaster.role_id  "
							+ " where userRoleMapper.organisation_id =:orgId "
							+ " and userRoleMapper.unit_id =:unitId"
							+ " and userRoleMapper.user_id =:userId" 
							+ " and (userRoleMapper.status='A' OR userRoleMapper.status='I') "
							),
						
						@NamedNativeQuery(name="GET_MPESA_BY_ID",
						query="select userRoleMapper.user_role_mppr_id as \"userRoleMpprId\" from public.t_user_role_mapper userRoleMapper "
								+ " where  userRoleMapper.unit_id =:unitId"
								+ " and userRoleMapper.user_id =:userId"
								+ " and userRoleMapper.manual_mpesa = true" 
								+ " and (userRoleMapper.status='A' ) "
							),
						
						@NamedNativeQuery(name="GET_ENABLE_BLOCK_REPORT_BY_ID",
						query="select userRoleMapper.user_role_mppr_id as \"userRoleMpprId\" from public.t_user_role_mapper userRoleMapper "
								+ " where  userRoleMapper.unit_id =:unitId"
								+ " and userRoleMapper.user_id =:userId"
								+ " and userRoleMapper.enable_block_print = true" 
								+ " and (userRoleMapper.status='A' ) "
							),
						
						@NamedNativeQuery(name="GET_USER_ROLE_ACCESS_BY_ID",
						query="select userRoleMapper.pharmacy_bill_cancellation as \"pharmacyBillCancellation\","
								+ " userRoleMapper.pharmacy_receipt_cancellation as \"pharmacyReceiptCancellation\", "
								+ " userRoleMapper.sample_recollection as \"sampleRecollection\" "
								+ " from public.t_user_unit_mapper userRoleMapper "
								+ " where  userRoleMapper.unit_id =:unitId"
								+ " and userRoleMapper.user_id =:userId"
								+ " and userRoleMapper.status='A'  "
							)
})

@Entity
@Table(name="t_user_role_mapper",schema="public")
@SequenceGenerator(name="t_user_role_mapper_seq",sequenceName="public.t_user_role_mapper_seq",allocationSize=1)
public class UserRoleMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_user_role_mapper_seq")
	@Column(name = "user_role_mppr_id")
	private Integer userRoleMpprId;
	
	@Column(name = "status")
	private Character status;
	
	@Column(name="organisation_id")
	private Integer organisationId;
	
	@Column(name="unit_id")
	private Integer unitId;

	@Column(name = "created_by")
	private int createdBy;

	@Column(name = "created_date")
	private Date createdDate;

	@Column(name = "updated_by")
	private int updatedBy;

	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "user_id")
	private Integer userId;
	
	@Column(name = "role_id")
	private Integer roleId;
	
	@Column(name = "dashboard_id")
	private Integer dashboardId;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private UserMaster userMaster;
	
//	@ManyToOne(fetch = FetchType.LAZY)
//	@JoinColumn(name = "role_id", insertable = false, updatable = false)
//	private UserRoleMaster userRoleMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, updatable = false)
	private EmployeeMaster employeeMaster;
	
	@Column(name = "is_recoll")
	private Boolean isRecoll;
	
	@Column(name = "is_ris_recall")
	private Boolean isRisRecall;
	
	@Column(name = "manual_mpesa")
	private Boolean manualMpesa;
	
	@Column(name = "enable_block_print")
	private Boolean enableBlockPrint;
	
	/*@Column(name = "is_rendered_service_cancelled")
	private boolean isRenderedServiceCancelled;
	
	@Column(name = "is_update_patient_reg")
	private boolean isUpdatePatientReg;*/
	
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

	public Integer getUserRoleMpprId() {
		return userRoleMpprId;
	}

	public void setUserRoleMpprId(Integer userRoleMpprId) {
		this.userRoleMpprId = userRoleMpprId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
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

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public int getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public UserMaster getUserMaster() {
		return userMaster;
	}

	public void setUserMaster(UserMaster userMaster) {
		this.userMaster = userMaster;
	}

	public EmployeeMaster getEmployeeMaster() {
		return employeeMaster;
	}

	public void setEmployeeMaster(EmployeeMaster employeeMaster) {
		this.employeeMaster = employeeMaster;
	}

	public Integer getDashboardId() {
		return dashboardId;
	}

	public void setDashboardId(Integer dashboardId) {
		this.dashboardId = dashboardId;
	}

	public Boolean getIsRecoll() {
		return isRecoll;
	}

	public void setIsRecoll(Boolean isRecoll) {
		this.isRecoll = isRecoll;
	}

	public Boolean getIsRisRecall() {
		return isRisRecall;
	}

	public void setIsRisRecall(Boolean isRisRecall) {
		this.isRisRecall = isRisRecall;
	}

	public Boolean getManualMpesa() {
		return manualMpesa;
	}

	public void setManualMpesa(Boolean manualMpesa) {
		this.manualMpesa = manualMpesa;
	}

	public Boolean getEnableBlockPrint() {
		return enableBlockPrint;
	}

	public void setEnableBlockPrint(Boolean enableBlockPrint) {
		this.enableBlockPrint = enableBlockPrint;
	}

	/*public boolean getIsRenderedServiceCancelled() {
		return isRenderedServiceCancelled;
	}

	public void setIsRenderedServiceCancelled(boolean isRenderedServiceCancelled) {
		this.isRenderedServiceCancelled = isRenderedServiceCancelled;
	}

	public boolean getIsUpdatePatientReg() {
		return isUpdatePatientReg;
	}

	public void setIsUpdatePatientReg(boolean isUpdatePatientReg) {
		this.isUpdatePatientReg = isUpdatePatientReg;
	}*/

	


		
}
