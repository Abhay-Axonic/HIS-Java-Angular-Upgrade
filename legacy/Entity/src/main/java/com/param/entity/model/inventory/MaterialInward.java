package com.param.entity.model.inventory;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

/**
 * The persistent class for the t_material_inwards database table.
 * 
 */
@Entity
@Table(name = "t_material_inwards", schema = "inventory")
public class MaterialInward extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approval_status_id", nullable = true)
	private Status approvalStatus;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approve_by")
	protected User approvedBy;

	@Column(name = "approved_on")
	private String approvedOn;
	
	public Double getCreditTaxAmount() {
		return creditTaxAmount;
	}

	public void setCreditTaxAmount(Double creditTaxAmount) {
		this.creditTaxAmount = creditTaxAmount;
	}

	public Double getCreditNetAmount() {
		return creditNetAmount;
	}

	public void setCreditNetAmount(Double creditNetAmount) {
		this.creditNetAmount = creditNetAmount;
	}

	@Column(name = "replacement_number")
	private String replacementNumber;

	public String getReplacementNumber() {
		return replacementNumber;
	}

	public void setReplacementNumber(String replacementNumber) {
		this.replacementNumber = replacementNumber;
	}

	@Column(name = "document_no")
	private String documentNo;

	@Column(name = "document_date")
	protected Date documentDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_type_id", nullable = false)
	private AssetType assetType;

	@Column(name = "cancel_note")
	private String cancelNote;
	
	@Column(name = "replacement_id")
	private int replacementId;

	public int getReplacementId() {
		return replacementId;
	}

	public void setReplacementId(int replacementId) {
		this.replacementId = replacementId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@Column(name = "inword_no")
	private String inwordNo;

	@Column(name = "net_amount")
	private Double netAmount;

	@Column(name = "return_amount")
	private Double returnAmount;

	@Column(name = "vendor_payable")
	private Double vendorPayable;

	//@Column(name = "credit_note")
	@Column(name = "credit_note_amount")
	private Double creditNote;
	
	@Column(name = "credit_tax_amount")
	private Double creditTaxAmount;
	
	@Column(name = "credit_net_amount")
	private Double creditNetAmount;
	
	@Column(name = "available_amount")
	private Double availableAmount;

	public Double getAvailableAmount() {
		return availableAmount;
	}

	public void setAvailableAmount(Double availableAmount) {
		this.availableAmount = availableAmount;
	}

	@Column(name = "remark")
	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_type_id", nullable = false)
	private RequestType requestType;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "returnable_gatepass_id", nullable = false)
	private ReturnableGatepass returnableGatepass;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	
	private Store store;
	
	
	//@ManyToOne(fetch = FetchType.LAZY)
	//@JoinColumn(name = "requesting_store_id", nullable = false)
	//private Store requestingStore;

	/*public Store getRequestingStore() {
		return requestingStore;
	}

	public void setRequestingStore(Store requestingStore) {
		this.requestingStore = requestingStore;
	}*/

	@Column(name = "total_amount")
	private Double totalAmount;

	@Column(name = "total_tax_amount")
	private Double totalTaxAmount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id", nullable = false)
	private Vendor vendor;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "materialInward", cascade = CascadeType.ALL)
	private List<MaterialInwardDetail> materialInwardsDetailList;

	public MaterialInward() {
		super();
	}

	public MaterialInward(Store store, Vendor vendor, AssetType assetType, RequestType requestType,
			ReturnableGatepass returnableGatepass, Double totalAmount, Double totalTaxAmount, Double totalNetAmount,
			String inwordNo, String remark, Status status, Status approvalStatus, String documentNo,
			Date documentDate, int replacementId ) {
		this.store = store;
		this.vendor = vendor;
		this.assetType = assetType;
		this.requestType = requestType;
		this.returnableGatepass = returnableGatepass;
		this.totalAmount = totalAmount;
		this.totalTaxAmount = totalTaxAmount;
		this.netAmount = totalNetAmount;
		this.remark = remark;
		this.inwordNo = inwordNo;
		this.status = status;
		this.approvalStatus = approvalStatus;
		this.documentNo = documentNo;
		this.documentDate = documentDate;
		this.replacementId=replacementId;
		//this.replacementNumber= replacementNumber;
		//this.requestingStore = requestingStore;
	}

	
	public MaterialInward(Store store, Vendor vendor, AssetType assetType, RequestType requestType, 
			String inwordNo, String remark, Status status, Status approvalStatus, String documentNo,
			Date documentDate, int replacementId,  String replacementNumber ) {
		this.store = store;
		this.vendor = vendor;
		this.assetType = assetType;
		this.requestType = requestType;
		//this.returnableGatepass = returnableGatepass;
		
		this.remark = remark;
		this.inwordNo = inwordNo;
		this.status = status;
		this.approvalStatus = approvalStatus;
		this.documentNo = documentNo;
		this.documentDate = documentDate;
		this.replacementId=replacementId;
		this.replacementNumber = replacementNumber;
		//this.requestingStore = requestingStore;
	}
	
	
	public void updateMaterialInward(Vendor vendor, RequestType requestType, ReturnableGatepass returnableGatepass,
			Double totalTaxAmount, Double totalAmount, Double totalNetAmount) {
		this.vendor = vendor;
		this.requestType = requestType;
		this.returnableGatepass = returnableGatepass;
		this.totalTaxAmount = totalTaxAmount;
		this.totalAmount = totalAmount;
		this.netAmount = totalNetAmount;
	}
	
	public void updateMaterialInward(String documentNo, Date documentDate, Double totalTaxAmount,
			Double totalAmount, Double totalNetAmount, String remark) {
		this.documentNo = documentNo;
		this.documentDate = documentDate;
		this.totalTaxAmount = totalTaxAmount;
		this.totalAmount = totalAmount;
		this.netAmount = totalNetAmount;
		this.remark = remark;
	}

	public MaterialInward(Integer id) {
		super();
		this.id = id;
	}

	public User getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(User updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Status getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(Status approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public String getApprovedOn() {
		return approvedOn;
	}

	public void setApprovedOn(String approvedOn) {
		this.approvedOn = approvedOn;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public String getCancelNote() {
		return cancelNote;
	}

	public void setCancelNote(String cancelNote) {
		this.cancelNote = cancelNote;
	}

	public CancelReason getCancelReason() {
		return cancelReason;
	}

	public void setCancelReason(CancelReason cancelReason) {
		this.cancelReason = cancelReason;
	}

	public String getInwordNo() {
		return inwordNo;
	}

	public void setInwordNo(String inwordNo) {
		this.inwordNo = inwordNo;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public Double getReturnAmount() {
		return returnAmount;
	}

	public void setReturnAmount(Double returnAmount) {
		this.returnAmount = returnAmount;
	}

	public Double getVendorPayable() {
		return vendorPayable;
	}

	public void setVendorPayable(Double vendorPayable) {
		this.vendorPayable = vendorPayable;
	}

	public Double getCreditNote() {
		return creditNote;
	}

	public void setCreditNote(Double creditNote) {
		this.creditNote = creditNote;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public ReturnableGatepass getReturnableGatepass() {
		return returnableGatepass;
	}

	public void setReturnableGatepass(ReturnableGatepass returnableGatepass) {
		this.returnableGatepass = returnableGatepass;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalTaxAmount() {
		return totalTaxAmount;
	}

	public void setTotalTaxAmount(Double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}

	public String getDocumentNo() {
		return documentNo;
	}

	public void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public List<MaterialInwardDetail> getMaterialInwardsDetailList() {
		return materialInwardsDetailList;
	}

	public void setMaterialInwardsDetailList(List<MaterialInwardDetail> materialInwardsDetailList) {
		this.materialInwardsDetailList = materialInwardsDetailList;
	}

	public MaterialInwardDetail addMaterialInwardDetailList(MaterialInwardDetail materialInwardDetail) {
		if (getMaterialInwardsDetailList() == null)
			this.materialInwardsDetailList = new ArrayList<MaterialInwardDetail>();

		getMaterialInwardsDetailList().add(materialInwardDetail);
		materialInwardDetail.setMaterialInward(this);

		return materialInwardDetail;
	}

	public void approveMaterialInward(Status statusId, Status approvalStatusId) {
		this.status = statusId;
		this.approvalStatus = approvalStatusId;
	}

}