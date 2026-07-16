package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.AssetType;
import com.param.entity.model.master.CancelReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Vendor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the t_vendor_negociation database table.
 * 
 */
@Entity
@Table(name="t_vendor_negociation", schema = "procurement")
public class VendorNegotiation extends BaseEntity implements Serializable  {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "asset_type_id")
	private AssetType assetType;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qf_id")
	private QuotationFill quotationFil;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "store_id")
	private Store store;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "vendor_id")
	private Vendor vendor;
	
	@Column(name="cancel_note")
	private String cancelNote;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "cancel_reason_id")
	private CancelReason cancelReason;

	
	//bi-directional many-to-one association to TVendorNegociationDetail
	@OneToMany(fetch = FetchType.LAZY, mappedBy="vendorNegociation", cascade = CascadeType.ALL)
	private List<VendorNegotiationDetail> vendorNegociationDetailsList;

	public VendorNegotiation() {
		super();
	}

	public VendorNegotiation(AssetType assetType,
			QuotationFill quotationFil, Status status, Store store, Vendor vendor) {
		super();
		this.assetType = assetType;
		this.quotationFil = quotationFil;
		this.status = status;
		this.store = store;
		this.vendor = vendor;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AssetType getAssetType() {
		return assetType;
	}

	public void setAssetType(AssetType assetType) {
		this.assetType = assetType;
	}

	public QuotationFill getQuotationFil() {
		return quotationFil;
	}

	public void setQuotationFil(QuotationFill quotationFil) {
		this.quotationFil = quotationFil;
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

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
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

	public List<VendorNegotiationDetail> getVendorNegociationDetailsList() {
		return vendorNegociationDetailsList;
	}

	public void setVendorNegociationDetailsList(List<VendorNegotiationDetail> vendorNegociationDetailsList) {
		this.vendorNegociationDetailsList = vendorNegociationDetailsList;
	}
	
	public VendorNegotiationDetail addVendorNegociationDetailList(VendorNegotiationDetail vendorNegociationDetail) {
		if (getVendorNegociationDetailsList() == null)
			this.vendorNegociationDetailsList = new ArrayList<VendorNegotiationDetail>();

		getVendorNegociationDetailsList().add(vendorNegociationDetail);
		vendorNegociationDetail.setVendorNegociation(this);
		return vendorNegociationDetail;
	}
	

	
}