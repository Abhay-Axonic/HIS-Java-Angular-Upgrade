package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;

import java.util.Date;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the t_po_staged database table.
 * 
 */
@Entity(name="ConsignmentStockPOStaged")
@Table(name="t_consignment_stock_po_staged", schema = "procurement")
	public class ConsignmentStockPOStaged extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Integer id;

	
	private Integer quantity;

	@Temporal(TemporalType.DATE)
	@Column(name="staged_date")
	private Date stagedDate;

	//bi-directional many-to-one association to TPurchaseOrderDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="po_detail_id")
	private ConsignmentStockPODetail consignmentPurchaseOrderDetail;
	
	@Column(name="approved_qunatity")
	private Integer approvedQunatity;

	/*@OneToMany(fetch = FetchType.LAZY, mappedBy = "purchaseOrderStaged", cascade = CascadeType.ALL)
	private List<GRNStagedQuantity> grnStagedQuantities;*/

	public ConsignmentStockPOStaged() {
		super();
	}
	
	public ConsignmentStockPOStaged(Integer id) {
		this.id = id ;
	}
	
	public ConsignmentStockPOStaged(Integer quantity, Date stagedDate) {
		super();
		this.quantity = quantity;
		this.stagedDate = stagedDate;
	}
	
	public void updatePurchaseOrderStaged(Integer quantity, Date stagedDate) {
		this.quantity = quantity;
		this.stagedDate = stagedDate;
	}
	
	public void approvePurchaseOrderStaged(Integer approvedQunatity) {
		this.approvedQunatity = approvedQunatity;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Date getStagedDate() {
		return stagedDate;
	}

	public void setStagedDate(Date stagedDate) {
		this.stagedDate = stagedDate;
	}

	public ConsignmentStockPODetail getPurchaseOrderDetail() {
		return consignmentPurchaseOrderDetail;
	}

	public void setPurchaseOrderDetail(ConsignmentStockPODetail purchaseOrderDetail) {
		this.consignmentPurchaseOrderDetail = purchaseOrderDetail;
	}

	public Integer getApprovedQunatity() {
		return approvedQunatity;
	}

	public void setApprovedQunatity(Integer approvedQunatity) {
		this.approvedQunatity = approvedQunatity;
	}

	/*public List<GRNStagedQuantity> getGrnStagedQuantities() {
		return grnStagedQuantities;
	}

	public void setGrnStagedQuantities(List<GRNStagedQuantity> grnStagedQuantities) {
		this.grnStagedQuantities = grnStagedQuantities;
	}*/
}