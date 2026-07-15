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
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Vendor;

@Entity(name = "ConsignmentStockPOInvoice")
@Table(name = "t_consignment_purchase_order_invoice", schema = "procurement")
public class ConsignmentStockPOInvoice extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "grn_id")
	private GoodReceiptNote goodReceiptNote;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "po_id")
	private PurchaseOrder purchaseOrder;
	
	@Column(name = "invoice_number")
	private String invoiceNumber;
	
	@Column(name = "invoice_date")
	private Date invoiceDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "requesting_store_id")
	private Store requestingStore;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "issuing_store_id")
	private Store issuingStore;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_type_id")
	private AssetType assetType;
	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	@Column(name="cancel_note")
	private String cancelNote;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "consignmentStockPOInvoice", cascade = CascadeType.ALL)
	private List<ConsignmentStockPOInvoiceDetail> consignmentStockPOInvoiceDetailList;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public GoodReceiptNote getGoodReceiptNote() {
		return goodReceiptNote;
	}

	public void setGoodReceiptNote(GoodReceiptNote goodReceiptNote) {
		this.goodReceiptNote = goodReceiptNote;
	}

	public PurchaseOrder getPurchaseOrder() {
		return purchaseOrder;
	}

	public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
		this.purchaseOrder = purchaseOrder;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	
	public Date getInvoiceDate() {
		return invoiceDate;
	}
	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}
	public Store getRequestingStore() {
		return requestingStore;
	}
	public void setRequestingStore(Store requestingStore) {
		this.requestingStore = requestingStore;
	}
	public Store getIssuingStore() {
		return issuingStore;
	}

	public void setIssuingStore(Store issuingStore) {
		this.issuingStore = issuingStore;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
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
	
	public List<ConsignmentStockPOInvoiceDetail> getConsignmentStockPOInvoiceDetailList() {
		return consignmentStockPOInvoiceDetailList;
	}

	public void setConsignmentStockPOInvoiceDetailList(
			List<ConsignmentStockPOInvoiceDetail> consignmentStockPOInvoiceDetailList) {
		this.consignmentStockPOInvoiceDetailList = consignmentStockPOInvoiceDetailList;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public ConsignmentStockPOInvoice() {
		super();
	}
	
	public ConsignmentStockPOInvoice(Integer id) {
		super();
		this.id = id;
	}
	public ConsignmentStockPOInvoice(Status status, AssetType assetType, Store requestStore, Store issuingStore, Vendor vendor,
			PurchaseOrder purchaseOrder,GoodReceiptNote goodReceiptNote ,String invoiceNumber, Date invoiceDate) {
		super();
		this.status=status;
		this.assetType=assetType;
		this.requestingStore=requestStore;
		this.issuingStore=issuingStore;
		this.vendor=vendor;
		this.purchaseOrder=purchaseOrder;
		this.goodReceiptNote=goodReceiptNote;
		this.invoiceNumber=invoiceNumber;
		this.invoiceDate=invoiceDate;
	}
	
	public ConsignmentStockPOInvoiceDetail addConsignmentPOInvoiceDetailList(ConsignmentStockPOInvoiceDetail consignmentStockPOInvoiceDetail) {
		if (getConsignmentStockPOInvoiceDetailList() == null)
			this.consignmentStockPOInvoiceDetailList = new ArrayList<ConsignmentStockPOInvoiceDetail>();

		getConsignmentStockPOInvoiceDetailList().add(consignmentStockPOInvoiceDetail);
		consignmentStockPOInvoiceDetail.setConsignmentStockPOInvoice(this);

		return consignmentStockPOInvoiceDetail;
	}

	
}