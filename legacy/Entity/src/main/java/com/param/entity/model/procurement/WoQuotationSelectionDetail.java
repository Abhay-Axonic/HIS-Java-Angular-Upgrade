package com.param.entity.model.procurement;

import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.master.Status;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


/**
 * The persistent class for the t_wo_quotation_selection_detail database table.
 * 
 */
@Entity
@Table(name="t_wo_quotation_selection_detail", schema = "procurement")
public class WoQuotationSelectionDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="quotation_request_detail_id")
	private WorkOrderQuotationRequestDetail quotationRequestDetail;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	//bi-directional many-to-one association to TWoQuotationSelectionDetail
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="quotation_selection_id")
	private WoQuotationSelection woQuotationSelection;

	//bi-directional many-to-one association to TWoQuotationSelectionVendorMapper
	@OneToMany(fetch = FetchType.LAZY,mappedBy="woQuotationSelectionDetail", cascade = CascadeType.ALL)
	private List<WoQuotationSelectionVendorMapper> woQuotationSelectionVendorMappers;

	public WoQuotationSelectionDetail() {
		super();
	}

	public WoQuotationSelectionDetail(WorkOrderQuotationRequestDetail quotationRequestDetail, Status status) {
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

	public WorkOrderQuotationRequestDetail getQuotationRequestDetail() {
		return quotationRequestDetail;
	}

	public void setQuotationRequestDetail(WorkOrderQuotationRequestDetail quotationRequestDetail) {
		this.quotationRequestDetail = quotationRequestDetail;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public WoQuotationSelection getWoQuotationSelection() {
		return woQuotationSelection;
	}

	public void setWoQuotationSelection(WoQuotationSelection woQuotationSelection) {
		this.woQuotationSelection = woQuotationSelection;
	}

	public List<WoQuotationSelectionVendorMapper> getWoQuotationSelectionVendorMappers() {
		return woQuotationSelectionVendorMappers;
	}

	public void setWoQuotationSelectionVendorMappers(
			List<WoQuotationSelectionVendorMapper> woQuotationSelectionVendorMappers) {
		this.woQuotationSelectionVendorMappers = woQuotationSelectionVendorMappers;
	}
	
	
	
	public WoQuotationSelectionVendorMapper addWoQuotationSelectionVendorMapperList(WoQuotationSelectionVendorMapper WoQuotationSelectionMapper) {
		if (getWoQuotationSelectionVendorMappers() == null)
			this.woQuotationSelectionVendorMappers = new ArrayList<WoQuotationSelectionVendorMapper>();

		getWoQuotationSelectionVendorMappers().add(WoQuotationSelectionMapper);
		WoQuotationSelectionMapper.setWoQuotationSelectionDetail(this);
		return WoQuotationSelectionMapper;
	}

	public WoQuotationSelectionVendorMapper removeWoQuotationSelectionVendorMapper(WoQuotationSelectionVendorMapper WoQuotationSelectionMapper) {
		getWoQuotationSelectionVendorMappers().remove(WoQuotationSelectionMapper);
		WoQuotationSelectionMapper.setWoQuotationSelectionDetail(this);

		return WoQuotationSelectionMapper;
	}
}