package com.param.entity.model.procurement;

import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the t_vendor_negociation_detail database table.
 * 
 */
@Entity
@Table(name="t_vendor_negociation_detail", schema = "procurement")
public class VendorNegotiationDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "qf_detail_id")
	private QuotationFillDetail quotationFillDetail;

	//bi-directional many-to-one association to TVendorNegociation
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="vendor_negociation_id")
	private VendorNegotiation vendorNegociation;

	//bi-directional many-to-one association to TVendorNegociationMapper
	@OneToMany(fetch = FetchType.LAZY, mappedBy="vendorNegociationDetail", cascade = CascadeType.ALL)
	private List<VendorNegotiationMapper> VendorNegociationMappersList;

	public VendorNegotiationDetail() {
		super();
		// TODO Auto-generated constructor stub
	}

	public VendorNegotiationDetail(QuotationFillDetail quotationFillDetail) {
		super();
		this.quotationFillDetail = quotationFillDetail;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public VendorNegotiation getVendorNegociation() {
		return vendorNegociation;
	}

	public void setVendorNegociation(VendorNegotiation vendorNegociation) {
		this.vendorNegociation = vendorNegociation;
	}

	public List<VendorNegotiationMapper> getVendorNegociationMappersList() {
		return VendorNegociationMappersList;
	}

	public void setVendorNegociationMappersList(List<VendorNegotiationMapper> vendorNegociationMappersList) {
		VendorNegociationMappersList = vendorNegociationMappersList;
	}
	
	public QuotationFillDetail getQuotationFillDetail() {
		return quotationFillDetail;
	}

	public void setQuotationFillDetail(QuotationFillDetail quotationFillDetail) {
		this.quotationFillDetail = quotationFillDetail;
	}

	public VendorNegotiationMapper addVendorNegociationMapperList(VendorNegotiationMapper vendorNegociationMapper) {
		if (getVendorNegociationMappersList() == null)
			this.VendorNegociationMappersList = new ArrayList<VendorNegotiationMapper>();

		getVendorNegociationMappersList().add(vendorNegociationMapper);
		vendorNegociationMapper.setVendorNegociationDetail(this);
		return vendorNegociationMapper;
	}
	
}