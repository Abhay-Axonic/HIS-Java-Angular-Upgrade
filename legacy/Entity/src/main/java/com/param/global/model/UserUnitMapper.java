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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.adt.master.global.model.UnitMaster;
import com.param.adt.master.global.model.UserMaster;


@Entity
@Table(name="t_user_unit_mapper",schema="public")
@SequenceGenerator(name="t_user_unit_mapper_seq",sequenceName="public.t_user_unit_mapper_seq",allocationSize=1)
public class UserUnitMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_user_unit_mapper_seq")
	@Column(name = "user_unit_mppr_id")
	private Integer userUnitMpprId;
	
	@Column(name = "user_id")
	private Integer userId;
	
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
	
	@Column(name = "mis_report_ids")
	private String misReportIds;
	
	@Column(name = "is_rendered_service_cancelled")
	private boolean isRenderedServiceCancelled;
	
	@Column(name = "is_update_patient_reg")
	private boolean isUpdatePatientReg;
	
	@Column(name = "tab_access_ids")
	private String tabAccessIds;
	
	@Column(name = "tab_access_ids_IPD")
	private String tabAccessIdsIPD;
	
	@Column(name = "update_discharge_summary")
	private boolean updateDischargeSummary;
	
	@Column(name = "is_update_ipd_charges_remark")
	private boolean isUpdateIpdChargesRemark;
	
	@Column(name = "opd_bill_cancellation")
	private boolean opdBillCancellation;
	
	@Column(name = "ipd_bill_cancellation")
	private boolean ipdBillCancellation;
	
	@Column(name = "bill_settlement")
	private boolean billSettlement;
	
	@Column(name = "ip_bill_settlement")
	private boolean ipBillSettlement;
	
	@Column(name = "service_rate_editable")
	private boolean serviceRateEditable;
	
	@Column(name = "is_force_update_service_package")
	private boolean isForceUpdateServicePackage;
	
	@Column(name = "nursing_op_scan")
	private boolean nursingOpScan;
	
	@Column(name = "nursing_ip_scan")
	private boolean nursingIpScan;
	
	@Column(name = "doctor_op_scan")
	private boolean doctorOpScan;
	
	@Column(name = "doctor_ip_scan")
	private boolean doctorIpScan;
	
	@Column(name = "change_payee_rights")
	private boolean changePayeeRights;
	
	@Column(name = "extend_pdd")
	private boolean extendPdd;
	
	@Column(name="is_bill_manipulation_allowed")
	private boolean isBillManipulationAllowed;

	@Column(name = "deposit_receipt_cancellation")
	private boolean depositReceiptCancellation;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", insertable = false, nullable = false, updatable = false)
	private UnitMaster unitMaster;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, nullable = false, updatable = false)
	private UserMaster userMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", insertable = false, nullable = false, updatable = false)
	private DoctorMaster doctorMaster;

	
	public boolean isBillManipulationAllowed() {
		return isBillManipulationAllowed;
	}

	public void setBillManipulationAllowed(boolean isBillManipulationAllowed) {
		this.isBillManipulationAllowed = isBillManipulationAllowed;
	}

	public String getMisReportIds() {
		return misReportIds;
	}

	public void setMisReportIds(String misReportIds) {
		this.misReportIds = misReportIds;
	}

	public Integer getUserUnitMpprId() {
		return userUnitMpprId;
	}

	public void setUserUnitMpprId(Integer userUnitMpprId) {
		this.userUnitMpprId = userUnitMpprId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public boolean getIsRenderedServiceCancelled() {
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
	}

	public String getTabAccessIds() {
		return tabAccessIds;
	}

	public void setTabAccessIds(String tabAccessIds) {
		this.tabAccessIds = tabAccessIds;
	}

	public String getTabAccessIdsIPD() {
		return tabAccessIdsIPD;
	}

	public void setTabAccessIdsIPD(String tabAccessIdsIPD) {
		this.tabAccessIdsIPD = tabAccessIdsIPD;
	}

	public boolean isUpdateIpdChargesRemark() {
		return isUpdateIpdChargesRemark;
	}

	public void setIsUpdateIpdChargesRemark(boolean isUpdateIpdChargesRemark) {
		this.isUpdateIpdChargesRemark = isUpdateIpdChargesRemark;
	}

	public boolean isUpdateDischargeSummary() {
		return updateDischargeSummary;
	}

	public void setUpdateDischargeSummary(boolean updateDischargeSummary) {
		this.updateDischargeSummary = updateDischargeSummary;
	}

	public boolean getOpdBillCancellation() {
		return opdBillCancellation;
	}

	public void setOpdBillCancellation(boolean opdBillCancellation) {
		this.opdBillCancellation = opdBillCancellation;
	}

	public boolean getIpdBillCancellation() {
		return ipdBillCancellation;
	}

	public void setIpdBillCancellation(boolean ipdBillCancellation) {
		this.ipdBillCancellation = ipdBillCancellation;
	}

	public boolean getBillSettlement() {
		return billSettlement;
	}

	public void setBillSettlement(boolean billSettlement) {
		this.billSettlement = billSettlement;
	}

	public boolean getIpBillSettlement() {
		return ipBillSettlement;
	}

	public void setIpBillSettlement(boolean ipBillSettlement) {
		this.ipBillSettlement = ipBillSettlement;
	}

	public boolean getServiceRateEditable() {
		return serviceRateEditable;
	}

	public void setServiceRateEditable(boolean serviceRateEditable) {
		this.serviceRateEditable = serviceRateEditable;
	}

	public boolean getIsForceUpdateServicePackage() {
		return isForceUpdateServicePackage;
	}

	public void setIsForceUpdateServicePackage(boolean isForceUpdateServicePackage) {
		this.isForceUpdateServicePackage = isForceUpdateServicePackage;
	}

	public boolean isChangePayeeRights() {
		return changePayeeRights;
	}

	public void setChangePayeeRights(boolean changePayeeRights) {
		this.changePayeeRights = changePayeeRights;
	}

	public boolean isExtendPdd() {
		return extendPdd;
	}

	public void setExtendPdd(boolean extendPdd) {
		this.extendPdd = extendPdd;
	}
	
}