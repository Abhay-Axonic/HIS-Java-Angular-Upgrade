package com.param.entity.model.procurement;

import java.io.Serial;
import java.io.Serializable;
import javax.persistence.*;

import org.springframework.beans.factory.annotation.Value;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Vendor;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the t_work_order_quotation_request_detail database table.
 * 
 */
@Entity
@Table(name="t_work_order_quotation_request_detail",schema="procurement")
public class WorkOrderQuotationRequestDetail extends BaseEntity implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "current_vendor_id")
	private Vendor currentVendor;

	private String specification;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "work_order_detail_id")
	private WorkOrderRequestDetail workOrderRequestDetail;

	//bi-directional many-to-one association to TWorkOrderQuotationRequest
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="work_order_quotation_request_id")
	private WorkOrderQuotationRequest WorkOrderQuotationRequest;
	
	private String remark;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "workOrderQRDetail", cascade = CascadeType.ALL)
	private List<WoQuotationVendorDetail> woQuotationVendorDetail;
	
	@Value("${stuff.value:#{null}}")
	private Integer quantity;
	
	@Column(name="expected_inclusion")
	private String expectedInclusion;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id")
	private Batch batch;
	
	

	public List<WoQuotationVendorDetail> getWoQuotationVendorDetail() {
		return woQuotationVendorDetail;
	}


	public void setWoQuotationVendorDetail(List<WoQuotationVendorDetail> woQuotationVendorDetail) {
		this.woQuotationVendorDetail = woQuotationVendorDetail;
	}


	public WorkOrderQuotationRequestDetail() {
		super();
	}
	

	public WorkOrderQuotationRequestDetail(Vendor currentVendor, String specification, Status status,
			WorkOrderRequestDetail workOrderRequestDetail, String remark, Integer quantity, String expectedInclusion,
			Item item,Batch batch) {
		super();
		this.currentVendor = currentVendor;
		this.specification = specification;
		this.status = status;
		this.workOrderRequestDetail = workOrderRequestDetail;
		this.remark = remark;
		this.quantity = quantity;
		this.expectedInclusion = expectedInclusion;
		this.item = item;
		this.batch = batch;
	}

	public WorkOrderQuotationRequestDetail(Integer id) {
		this.id = id;
	}

	public void updateWorkOrderQuotationRequestDetail(String specification, String remark, Integer quantity,
			String expectedInclusion,Batch batch,Vendor currentVendor) {
		this.specification = specification;
		this.remark = remark;
		this.quantity = quantity;
		this.expectedInclusion = expectedInclusion;
		this.batch = batch;
		this.currentVendor = currentVendor;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Vendor getCurrentVendor() {
		return currentVendor;
	}

	public void setCurrentVendor(Vendor currentVendor) {
		this.currentVendor = currentVendor;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public WorkOrderRequestDetail getWorkOrderRequestDetail() {
		return workOrderRequestDetail;
	}

	public void setWorkOrderRequestDetail(WorkOrderRequestDetail workOrderRequestDetail) {
		this.workOrderRequestDetail = workOrderRequestDetail;
	}

	public WorkOrderQuotationRequest getWorkOrderQuotationRequest() {
		return WorkOrderQuotationRequest;
	}

	public void setWorkOrderQuotationRequest(WorkOrderQuotationRequest workOrderQuotationRequest) {
		WorkOrderQuotationRequest = workOrderQuotationRequest;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getQuantity() {
		if(quantity==null)
			quantity=0;
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getExpectedInclusion() {
		return expectedInclusion;
	}

	public void setExpectedInclusion(String expectedInclusion) {
		this.expectedInclusion = expectedInclusion;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}
}