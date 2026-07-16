package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the t_wo_quotation_negotiation_detail database table.
 * 
 */
@Entity
@Table(name="t_wo_quotation_negotiation_detail", schema = "procurement")
public class WoQuotationNegotiationDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="vendor_quotation_detail_id")
	private WoQuotationVendorDetail vendorQuotationDetail;

	//bi-directional many-to-one association to TWoQuotationNegotiation
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="negotiation_id")
	private WoQuotationNegotiation woQuotationNegotiation;	
	
	//bi-directional many-to-one association to TWoQuotationNegotiationMapper
	@OneToMany(fetch = FetchType.LAZY, mappedBy="woQuotationNegotiationDetail", cascade = CascadeType.ALL)
	private List<WoQuotationNegotiationMapper> woQuotationNegotiationMapperList;

	public WoQuotationNegotiationDetail() {
		super();
	}	

	public WoQuotationNegotiationDetail(WoQuotationVendorDetail vendorQuotationDetail) {
		super();
		this.vendorQuotationDetail = vendorQuotationDetail;
		
	}
	
	/*public void updateWoQuotationNegotiationDetail(Integer quantity, String remark) {
		this.quantity = quantity;
		this.remark = remark;
	}*/

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public WoQuotationVendorDetail getVendorQuotationDetail() {
		return vendorQuotationDetail;
	}

	public void setVendorQuotationDetail(WoQuotationVendorDetail vendorQuotationDetail) {
		this.vendorQuotationDetail = vendorQuotationDetail;
	}

	public WoQuotationNegotiation getWoQuotationNegotiation() {
		return woQuotationNegotiation;
	}

	public void setWoQuotationNegotiation(WoQuotationNegotiation woQuotationNegotiation) {
		this.woQuotationNegotiation = woQuotationNegotiation;
	}

	public List<WoQuotationNegotiationMapper> getWoQuotationNegotiationMapperList() {
		return woQuotationNegotiationMapperList;
	}

	public void setWoQuotationNegotiationMapperList(List<WoQuotationNegotiationMapper> woQuotationNegotiationMapperList) {
		this.woQuotationNegotiationMapperList = woQuotationNegotiationMapperList;
	}
	
	public WoQuotationNegotiationMapper addWoQuotationNegotiationMapperList(WoQuotationNegotiationMapper woQuotationNegotiationMapper) {
		if (getWoQuotationNegotiationMapperList() == null)
			this.woQuotationNegotiationMapperList = new ArrayList<WoQuotationNegotiationMapper>();

		getWoQuotationNegotiationMapperList().add(woQuotationNegotiationMapper);
		woQuotationNegotiationMapper.setWoQuotationNegotiationDetail(this);

		return woQuotationNegotiationMapper;
	}

	public WoQuotationNegotiationMapper removeWoQuotationNegotiationMapper(WoQuotationNegotiationMapper woQuotationNegotiationMapper) {
		getWoQuotationNegotiationMapperList().remove(woQuotationNegotiationMapper);
		woQuotationNegotiationMapper.setWoQuotationNegotiationDetail(this);

		return woQuotationNegotiationMapper;
	}
}