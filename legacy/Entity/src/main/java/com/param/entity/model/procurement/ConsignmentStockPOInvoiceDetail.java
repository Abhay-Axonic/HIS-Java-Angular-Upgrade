package com.param.entity.model.procurement;

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
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Manufacturer;
import com.param.entity.model.master.RequestType;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.Vendor;

@Entity(name = "ConsignmentStockPOInvoiceDetail")
@Table(name = "t_consignment_purchase_order_invoice_detail", schema = "procurement")
public class ConsignmentStockPOInvoiceDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cpo_invoice_id")
	private ConsignmentStockPOInvoice consignmentStockPOInvoice;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grn_detail_id")
	private GoodReceiptNoteDetail goodReceiptNoteDetail;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "po_detail_id")
	private PurchaseOrderDetail purchaseOrderDetail;
	
	/*@Column(name = "invoice_number")
	private String invoiceNumber;*/
	
	@Column(name = "specification")
	private String specification;

	@Column(name = "remark")
	private String remark;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	/* start code for resolved HIS-9322 Consignment PO invoice to have editable fields */	
	@Column(name = "cop")
	private Double cop;

	@Column(name = "mrp")
	private Double mrp;
	
	@Column(name = "discount")
	private Double discount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "tax_id", nullable = false)
	private Tax tax;

	@Column(name = "tax_percentage")
	private Double taxPercentage;

	@Column(name = "total_amount")
	private Double totalAmount;
	
	@Column(name = "total_discount")
	private Double totalDiscount;

	@Column(name = "tax_amount")
	private Double taxAmount;

	@Column(name = "total_other_amount")
	private Double totalOtherAmount;

	@Column(name = "net_amount")
	private Double netAmount;	

	/* end code for resolved HIS-9322 Consignment PO invoice to have editable fields */

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public ConsignmentStockPOInvoice getConsignmentStockPOInvoice() {
		return consignmentStockPOInvoice;
	}

	public void setConsignmentStockPOInvoice(ConsignmentStockPOInvoice consignmentStockPOInvoice) {
		this.consignmentStockPOInvoice = consignmentStockPOInvoice;
	}

	public GoodReceiptNoteDetail getGoodReceiptNoteDetail() {
		return goodReceiptNoteDetail;
	}

	public void setGoodReceiptNoteDetail(GoodReceiptNoteDetail goodReceiptNoteDetail) {
		this.goodReceiptNoteDetail = goodReceiptNoteDetail;
	}

	public PurchaseOrderDetail getPurchaseOrderDetail() {
		return purchaseOrderDetail;
	}

	public void setPurchaseOrderDetail(PurchaseOrderDetail purchaseOrderDetail) {
		this.purchaseOrderDetail = purchaseOrderDetail;
	}

	/*public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}*/

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ConsignmentStockPOInvoiceDetail() {
		super();
	}

	public ConsignmentStockPOInvoiceDetail(Integer id) {
		super();
		this.id = id;
	}
	
	public ConsignmentStockPOInvoiceDetail(String remark, String specification, Status status,
			GoodReceiptNoteDetail goodReceiptNoteDetail, PurchaseOrderDetail purchaseOrderDetail) {
		super();
		this.remark=remark;
		this.specification=specification;
		this.status=status;
		this.goodReceiptNoteDetail=goodReceiptNoteDetail;
		this.purchaseOrderDetail=purchaseOrderDetail;
	}
	
	/* start code for resolved HIS-9322 Consignment PO invoice to have editable fields */
	public Double getCop() {
		return cop;
	}

	public void setCop(Double cop) {
		this.cop = cop;
	}

	public Double getMrp() {
		return mrp;
	}

	public void setMrp(Double mrp) {
		this.mrp = mrp;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public Tax getTax() {
		return tax;
	}

	public void setTax(Tax tax) {
		this.tax = tax;
	}

	public Double getTaxPercentage() {
		return taxPercentage;
	}

	public void setTaxPercentage(Double taxPercentage) {
		this.taxPercentage = taxPercentage;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Double getTotalDiscount() {
		return totalDiscount;
	}

	public void setTotalDiscount(Double totalDiscount) {
		this.totalDiscount = totalDiscount;
	}

	public Double getTaxAmount() {
		return taxAmount;
	}

	public void setTaxAmount(Double taxAmount) {
		this.taxAmount = taxAmount;
	}

	public Double getTotalOtherAmount() {
		return totalOtherAmount;
	}

	public void setTotalOtherAmount(Double totalOtherAmount) {
		this.totalOtherAmount = totalOtherAmount;
	}

	public Double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}
	/* end code for resolved HIS-9322 Consignment PO invoice to have editable fields */
	
	
}