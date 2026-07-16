package com.param.entity.model.inventory;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
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
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.User;

/**
 * The persistent class for the t_stop_transaction database table.
 * 
 */
@Entity
@Table(name = "t_stop_transaction", schema = "inventory")
public class StopTransaction extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "approved_by")
	protected User approvedBy;

	@Column(name = "approved_on")
	private Date approvedDate;

	@Column(name = "cancel_note")
	private String cancelNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	@Column(name = "is_a")
	private Boolean isA;

	@Column(name = "is_b")
	private Boolean isB;

	@Column(name = "is_c")
	private Boolean isC;

	@Column(name = "is_diseriable")
	private Boolean isDiseriable;

	@Column(name = "is_essential")
	private Boolean isEssential;

	@Column(name = "is_vital")
	private Boolean isVital;

	private String remark;

	@Column(name = "request_no")
	private String requestNo;

	@Column(name = "start_date_time")
	private Date startDateTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id", nullable = true)
	private Status status;

	@Column(name = "stop_date_time")
	private Date stopDateTime;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id", nullable = false)
	private Store store;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "StopTransaction", cascade = CascadeType.ALL)
	private List<StopTransactionItemExclusionDetail> StopTransactionItemExclusionDetails;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "StopTransaction", cascade = CascadeType.ALL)
	private List<StopTransactionItemDetail> StopTransactionItemDetails;

	@OneToMany(fetch = FetchType.LAZY,mappedBy = "StopTransaction", cascade = CascadeType.ALL)
	private List<StopTransactionProdCatExclDetail> StopTransactionProdCatExclDetails;

	public StopTransaction() {
		super();
	}

	public StopTransaction(Integer id) {
		super();
		this.id = id;
	}

	public StopTransaction(String requestNo, Store store, Date startDateTime, Date stopDateTime, Status status) {
		this.store = store;
		this.startDateTime = startDateTime;
		this.stopDateTime = stopDateTime;
		this.status = status;
		this.requestNo = requestNo;

	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(User approvedBy) {
		this.approvedBy = approvedBy;
	}

	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
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
	
	public Boolean getIsA() {
		return isA;
	}

	public void setIsA(Boolean isA) {
		this.isA = isA;
	}

	public Boolean getIsB() {
		return isB;
	}

	public void setIsB(Boolean isB) {
		this.isB = isB;
	}

	public Boolean getIsC() {
		return isC;
	}

	public void setIsC(Boolean isC) {
		this.isC = isC;
	}

	public Boolean getIsDiseriable() {
		return isDiseriable;
	}

	public void setIsDiseriable(Boolean isDiseriable) {
		this.isDiseriable = isDiseriable;
	}

	public Boolean getIsEssential() {
		return isEssential;
	}

	public void setIsEssential(Boolean isEssential) {
		this.isEssential = isEssential;
	}

	public Boolean getIsVital() {
		return isVital;
	}

	public void setIsVital(Boolean isVital) {
		this.isVital = isVital;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getRequestNo() {
		return requestNo;
	}

	public void setRequestNo(String requestNo) {
		this.requestNo = requestNo;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Date getStopDateTime() {
		return stopDateTime;
	}

	public void setStopDateTime(Date stopDateTime) {
		this.stopDateTime = stopDateTime;
	}

	public Store getStore() {
		return store;
	}

	public void setStore(Store store) {
		this.store = store;
	}

	public List<StopTransactionItemExclusionDetail> getStopTransactionItemExclusionDetails() {
		return this.StopTransactionItemExclusionDetails;
	}

	public void setStopTransactionItemExclusionDetails(
			List<StopTransactionItemExclusionDetail> stopTransactionItemExclusionDetails) {
		StopTransactionItemExclusionDetails = stopTransactionItemExclusionDetails;
	}

	public StopTransactionItemExclusionDetail addStopTransactionItemExclusionDetail(
			StopTransactionItemExclusionDetail StopTransactionItemExclusionDetail) {
		getStopTransactionItemExclusionDetails().add(StopTransactionItemExclusionDetail);
		StopTransactionItemExclusionDetail.setStopTransaction(this);

		return StopTransactionItemExclusionDetail;
	}

	public StopTransactionItemExclusionDetail removeStopTransactionItemExclusionDetail(
			StopTransactionItemExclusionDetail StopTransactionItemExclusionDetail) {
		getStopTransactionItemExclusionDetails().remove(StopTransactionItemExclusionDetail);
		StopTransactionItemExclusionDetail.setStopTransaction(null);

		return StopTransactionItemExclusionDetail;
	}

	public List<StopTransactionItemDetail> getStopTransactionItemDetails() {
		return this.StopTransactionItemDetails;
	}

	public void setStopTransactionItemDetails(List<StopTransactionItemDetail> StopTransactionItemDetails) {
		this.StopTransactionItemDetails = StopTransactionItemDetails;
	}

	public StopTransactionItemDetail addStopTransactionItemDetail(StopTransactionItemDetail StopTransactionItemDetail) {
		getStopTransactionItemDetails().add(StopTransactionItemDetail);
		StopTransactionItemDetail.setStopTransaction(this);

		return StopTransactionItemDetail;
	}

	public StopTransactionItemDetail removeStopTransactionItemDetail(
			StopTransactionItemDetail StopTransactionItemDetail) {
		getStopTransactionItemDetails().remove(StopTransactionItemDetail);
		StopTransactionItemDetail.setStopTransaction(null);

		return StopTransactionItemDetail;
	}

	public List<StopTransactionProdCatExclDetail> getStopTransactionProdCatExclDetails() {
		return this.StopTransactionProdCatExclDetails;
	}

	public void setStopTransactionProdCatExclDetails(
			List<StopTransactionProdCatExclDetail> StopTransactionProdCatExclDetail) {
		this.StopTransactionProdCatExclDetails = StopTransactionProdCatExclDetail;
	}

	public StopTransactionProdCatExclDetail addStopTransactionProdCatExclDetail(
			StopTransactionProdCatExclDetail StopTransactionProdCatExclDetail) {
		getStopTransactionProdCatExclDetails().add(StopTransactionProdCatExclDetail);
		StopTransactionProdCatExclDetail.setStopTransaction(this);

		return StopTransactionProdCatExclDetail;
	}

	public StopTransactionProdCatExclDetail removeStopTransactionProductCategoryExclusionDetail(
			StopTransactionProdCatExclDetail StopTransactionProdCatExclDetail) {
		getStopTransactionProdCatExclDetails().remove(StopTransactionProdCatExclDetail);
		StopTransactionProdCatExclDetail.setStopTransaction(null);

		return StopTransactionProdCatExclDetail;
	}

	public void saveStopTransaction(Boolean isVital, Boolean isEssential, Boolean isDiseriable, Boolean isA,
			Boolean isB, Boolean isC, String remark, Status status) {
		this.isVital = isVital;
		this.isEssential = isEssential;
		this.isDiseriable = isDiseriable;
		this.isA = isA;
		this.isB = isB;
		this.isC = isC;
		this.remark = remark;
		this.status = status;

	}

}