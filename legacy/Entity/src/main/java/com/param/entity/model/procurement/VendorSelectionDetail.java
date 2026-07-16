package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Vendor;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the t_vendor_selection_detail database table.
 * 
 */
@Entity
@Table(name="t_vendor_selection_detail", schema = "procurement")
public class VendorSelectionDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qr_detail_id")
	private QuotationRequestDetail quotationRequestDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	//bi-directional many-to-one association to TVendorSelection
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="vendor_selection_id")
	private VendorSelection vendorSelection;
	
	// bi-directional many-to-one association to TVendorSelectionDetail
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vendorSelectionDetail", cascade = CascadeType.ALL)
	private List<VendorSelectionMapper> vendorSelectionMapperList;
	
	

	public VendorSelectionDetail(Integer id) {
		super();
		this.id = id;
	}

	public VendorSelectionDetail() {
		super();
	}

	public VendorSelectionDetail(QuotationRequestDetail quotationRequestDetail, Status status) {
		super();
		this.quotationRequestDetail = quotationRequestDetail;
		this.status = status;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public QuotationRequestDetail getQuotationRequestDetail() {
		return quotationRequestDetail;
	}

	public void setQuotationRequestDetail(QuotationRequestDetail quotationRequestDetail) {
		this.quotationRequestDetail = quotationRequestDetail;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public VendorSelection getVendorSelection() {
		return vendorSelection;
	}

	public void setVendorSelection(VendorSelection vendorSelection) {
		this.vendorSelection = vendorSelection;
	}

	public List<VendorSelectionMapper> getVendorSelectionMapperList() {
		return vendorSelectionMapperList;
	}

	public void setVendorSelectionMapperList(List<VendorSelectionMapper> vendorSelectionMapperList) {
		this.vendorSelectionMapperList = vendorSelectionMapperList;
	}
	
	public VendorSelectionMapper addVendorSelectionMapper(VendorSelectionMapper vendorSelectionMapper) {
		if (getVendorSelectionMapperList() == null)
			this.vendorSelectionMapperList = new ArrayList<VendorSelectionMapper>();
		getVendorSelectionMapperList().add(vendorSelectionMapper);
		vendorSelectionMapper.setVendorSelectionDetail(this);
		return vendorSelectionMapper;
	}
}