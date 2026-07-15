package com.param.CSSD.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.param.entity.model.master.Store;


@NamedQueries({
	
	

		@NamedQuery(name = "GET_KIT_ISSUE_LIST", query = "SELECT kitIssue.issueNo as issueNo, "
				+ "to_char(kitIssue.createdDate,'dd-MM-yyyy') as createdDate,kitIssue.indentNo as indentNo, "
				+ "to_char(kitIndent.indentDate,'dd-MM-yyyy') as indentDate,kitIssue.status as status,kitIssue.remark as remark,kitIssue.carrier as carrier,"
				+ "fromStore.storeName as fromStore,toStore.storeName as toStore FROM KitIssue kitIssue INNER JOIN kitIssue.fromStore as fromStore "
				+ "INNER JOIN kitIssue.toStore as toStore INNER JOIN kitIssue.kitIndent as kitIndent "),
		
		@NamedQuery(name = "GET_KIT_ISSUE_BY_ISSUE_NO", query = "SELECT kitIssue.issueNo as issueNo, "
				+ "to_char(kitIssue.createdDate,'dd-MM-yyyy') as createdDate,kitIssue.indentNo as indentNo, "
				+ "to_char(kitIndent.indentDate,'dd-MM-yyyy') as indentDate,kitIssue.status as status,kitIssue.remark as remark,kitIssue.carrier as carrier,"
				+ "fromStore.storeName as fromStore,toStore.storeName as toStore FROM KitIssue kitIssue INNER JOIN kitIssue.fromStore as fromStore "
				+ "INNER JOIN kitIssue.toStore as toStore INNER JOIN kitIssue.kitIndent as kitIndent WHERE kitIssue.issueNo=:issueNo "),

		@NamedQuery(name = "GET_KIT_ISSUE_BY_MULTIPLE_FIELDS", query = "SELECT kitIssue.issueNo as issueNo, "
				+ "to_char(kitIssue.createdDate,'dd-MM-yyyy') as createdDate,kitIssue.withIndent as withIndent, kitIssue.indentNo as indentNo, "
				+ "to_char(kitIndent.indentDate,'dd-MM-yyyy') as indentDate,kitIssue.status as status,kitIssue.remark as remark,kitIssue.carrier as carrier, fromStore.storeName as fromStore,"
				+ "toStore.storeName as toStore FROM KitIssue kitIssue INNER JOIN kitIssue.fromStore as fromStore "
				+ "INNER JOIN kitIssue.toStore as toStore INNER JOIN kitIssue.kitIndent as kitIndent WHERE kitIssue.createdDate BETWEEN :fromDate AND :toDate AND kitIssue.status=:status "
				+ "AND fromStore.storeId=:fromStore AND toStore.storeId=:toStore AND kitIssue.indentNo=:indentNo ") })



@Entity
@Table(name = "t_kit_issue", schema = "cssd")
@SequenceGenerator(name = "t_kit_issue_id_seq", sequenceName = "cssd.t_kit_issue_id_seq", allocationSize = 1)
public class KitIssue {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "t_kit_issue_id_seq")
	@Column(name = "id")
	private Integer issueNo;

	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "updated_date")
	private Date updatedDate;
	
	@Column(name = "created_by")
	private Integer createdBy;
	
	@Column(name = "updated_by")
	private Integer updatedBy;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name = "unit_id")
	private Integer unitId;
	
	@Column(name = "with_indent")
	private Integer withIndent;
	
	@Column(name = "indent_no")
	private Integer indentNo;
	
	@Column(name = "request_no")
	private Integer requestNo;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "request_no", insertable = false, updatable = false)
	private SterilizationRequest  sterilizationRequest;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "indent_no", insertable = false, updatable = false)
	private KitIndent  kitIndent;
	
	@Column(name = "from_store_id")
	private Integer fromStoreId;
	
	@Column(name = "to_store_id")
	private Integer toStoreId;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "from_store_id", insertable = false, updatable = false)
	private Store fromStore;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "to_store_id", insertable = false, updatable = false)
	private Store toStore;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "id", insertable = false, updatable = false)
	private List<KitIssueKitMapper> kitList;
	
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "cancel_reason_id")
	private Integer cancelReasonId;

	@Column(name = "cancel_reason_note")
	private String cancelReasonNote;
	
	@Column(name = "remark")
	private String remark;
	
	@Column(name = "carrier")
	private String carrier;
	
	@Column(name = "reason")
	private String reason;

	public Integer getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(Integer issueNo) {
		this.issueNo = issueNo;
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

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public Integer getWithIndent() {
		return withIndent;
	}

	public void setWithIndent(Integer withIndent) {
		this.withIndent = withIndent;
	}

	public Integer getIndentNo() {
		return indentNo;
	}

	public void setIndentNo(Integer indentNo) {
		this.indentNo = indentNo;
	}

	public KitIndent getKitIndent() {
		return kitIndent;
	}

	public void setKitIndent(KitIndent kitIndent) {
		this.kitIndent = kitIndent;
	}

	public Integer getFromStoreId() {
		return fromStoreId;
	}

	public void setFromStoreId(Integer fromStoreId) {
		this.fromStoreId = fromStoreId;
	}

	public Integer getToStoreId() {
		return toStoreId;
	}

	public void setToStoreId(Integer toStoreId) {
		this.toStoreId = toStoreId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCarrier() {
		return carrier;
	}

	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	public Store getFromStore() {
		return fromStore;
	}

	public void setFromStore(Store fromStore) {
		this.fromStore = fromStore;
	}

	public Store getToStore() {
		return toStore;
	}

	public void setToStore(Store toStore) {
		this.toStore = toStore;
	}

	public List<KitIssueKitMapper> getKitList() {
		return kitList;
	}

	public void setKitList(List<KitIssueKitMapper> kitList) {
		this.kitList = kitList;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	public Integer getCancelReasonId() {
		return cancelReasonId;
	}

	public void setCancelReasonId(Integer cancelReasonId) {
		this.cancelReasonId = cancelReasonId;
	}

	public String getCancelReasonNote() {
		return cancelReasonNote;
	}

	public void setCancelReasonNote(String cancelReasonNote) {
		this.cancelReasonNote = cancelReasonNote;
	}

	public Integer getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(Integer requestNo) {
		this.requestNo = requestNo;
	}

	public SterilizationRequest getSterilizationRequest() {
		return sterilizationRequest;
	}

	public void setSterilizationRequest(SterilizationRequest sterilizationRequest) {
		this.sterilizationRequest = sterilizationRequest;
	}

	
}
