package com.param.entity.model.procurement;

import java.io.Serializable;
import javax.persistence.*;

import com.param.entity.model.base.BaseEntity;
import com.param.entity.model.inventory.StoreIndentDetail;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;

import io.swagger.annotations.ApiModelProperty;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the t_quotation_request_detail database table.
 * 
 */
@Entity(name = "QuotationRequestDetail")
@Table(name="t_quotation_request_detail",schema="procurement")
public class QuotationRequestDetail extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;
	
	private Integer quantity;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "status_id")
	private Status status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="uom_type_id")
	private UomType uomType;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="uom_unit_id")
	private UomUnit uomUnit;

	@Column(name="warranty_period")
	private String warrantyPeriod;
	
	@Column(name="expected_payment")
	private String expectedPayment;

	@Column(name="expected_support_term")
	private String expectedSupportTerm;

	@Column(name="expected_technical_specification")
	private String expectedTechnicalSpecification;
	
	@Column(name="lease_quantity")
	private Integer leaseQuantity;
	
	@Column(name="pending_quantity")
	private Integer pendingQuantity;

	@Column(name="qr_pending_quantity")
	private Integer qrPendingQuantity;
	
	//bi-directional many-to-one association to QuotationRequest
	@ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name="qr_id")
	private QuotationRequest quotationRequest;

	//bi-directional many-to-one association to PurchaseRequestDetail
	@OneToMany(fetch = FetchType.LAZY, mappedBy="quotationRequestDetail", cascade = CascadeType.ALL)
	private List<PurchaseRequestDetail> purchaseRequestDetailsList;
	
	//bi-directional many-to-one association to PurchaseRequestDetail
	@OneToMany(fetch = FetchType.LAZY,mappedBy="quotationRequestDetail", cascade = CascadeType.ALL)
	private List<QuotationRequestAccessoriesMapper> quotationRequestAccessoriesMapperList;
	
	//bi-directional many-to-one association to PurchaseRequestDetail
	@OneToMany(fetch = FetchType.LAZY,mappedBy="qrDetail", cascade = CascadeType.ALL)
	private List<QuotationVendorDetail> quotationVendorDetail;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "quotationRequestDetail", cascade = CascadeType.ALL)
	private List<PurchaseOrderDetail> purchaseOrderDetails;
	
	@Column(name = "is_quotation_fill_cancel")
	@ApiModelProperty(value = "isQuotationFillCancel")
	protected Boolean isQuotationFillCancel = false;
	
	public Integer getQrPendingQuantity() {
		return qrPendingQuantity;
	}

	public void setQrPendingQuantity(Integer qrPendingQuantity) {
		this.qrPendingQuantity = qrPendingQuantity;
	}

	public Boolean getIsQuotationFillCancel() {
		return isQuotationFillCancel;
	}

	public void setIsQuotationFillCancel(Boolean isQuotationFillCancel) {
		this.isQuotationFillCancel = isQuotationFillCancel;
	}

	public QuotationRequestDetail() {
		super();
	}
	
	public QuotationRequestDetail(Integer id)
	{
		this.id = id;
	}
	
	
	public QuotationRequestDetail(Item item, Integer quantity, Status status, UomType uomType, UomUnit uomUnit, String warrantyPeriod,
			String expectedPayment, String expectedSupportTerm, String expectedTechnicalSpecification, Integer leaseQuantity) {
		super();
		this.item = item;
		this.quantity = quantity;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.status = status;
		this.warrantyPeriod = warrantyPeriod;
		this.expectedPayment = expectedPayment;
		this.expectedSupportTerm = expectedSupportTerm;
		this.expectedTechnicalSpecification = expectedTechnicalSpecification;
		this.leaseQuantity = leaseQuantity;
	}
	
	public QuotationRequestDetail(Item item, Integer quantity, Status status, UomType uomType, UomUnit uomUnit, String warrantyPeriod,
			String expectedPayment, String expectedSupportTerm, String expectedTechnicalSpecification, Integer leaseQuantity, Integer pendingQuantity) {
		super();
		this.item = item;
		this.quantity = quantity;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.status = status;
		this.warrantyPeriod = warrantyPeriod;
		this.expectedPayment = expectedPayment;
		this.expectedSupportTerm = expectedSupportTerm;
		this.expectedTechnicalSpecification = expectedTechnicalSpecification;
		this.leaseQuantity = leaseQuantity;
		this.pendingQuantity = pendingQuantity;
	}
	
	public QuotationRequestDetail(Item item, Integer quantity, Status status, String warrantyPeriod,
			String expectedPayment, String expectedSupportTerm, String expectedTechnicalSpecification,Integer leaseQuantity) {
		super();
		this.item = item;
		this.quantity = quantity;
		this.status = status;
		this.warrantyPeriod = warrantyPeriod;
		this.expectedPayment = expectedPayment;
		this.expectedSupportTerm = expectedSupportTerm;
		this.expectedTechnicalSpecification = expectedTechnicalSpecification;
		this.leaseQuantity = leaseQuantity;
		this.pendingQuantity=leaseQuantity;
	}


	public void updateQuotationRequestDetail( Integer quantity, UomType uomType, UomUnit uomUnit,
			String warrantyPeriod, String expectedPayment, String expectedSupportTerm,
			String expectedTechnicalSpecification,Integer leaseQuantity) {
		this.quantity = quantity;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.warrantyPeriod = warrantyPeriod;
		this.expectedPayment = expectedPayment;
		this.expectedSupportTerm = expectedSupportTerm;
		this.expectedTechnicalSpecification = expectedTechnicalSpecification;
		this.leaseQuantity = leaseQuantity;
	}
	
	public void updateQuotationRequestDetail( Integer quantity, UomType uomType, UomUnit uomUnit,
			String warrantyPeriod, String expectedPayment, String expectedSupportTerm,
			String expectedTechnicalSpecification,Integer leaseQuantity, Integer pendingQuantity) {
		this.quantity = quantity;
		this.uomType = uomType;
		this.uomUnit = uomUnit;
		this.warrantyPeriod = warrantyPeriod;
		this.expectedPayment = expectedPayment;
		this.expectedSupportTerm = expectedSupportTerm;
		this.expectedTechnicalSpecification = expectedTechnicalSpecification;
		this.leaseQuantity = leaseQuantity;
		this.pendingQuantity = pendingQuantity;
	}
	
	public void updateQuotationRequestDetail( Integer quantity,
			String warrantyPeriod, String expectedPayment, String expectedSupportTerm,
			String expectedTechnicalSpecification,Integer leaseQuantity) {
		this.quantity = quantity;
		this.warrantyPeriod = warrantyPeriod;
		this.expectedPayment = expectedPayment;
		this.expectedSupportTerm = expectedSupportTerm;
		this.expectedTechnicalSpecification = expectedTechnicalSpecification;
		this.leaseQuantity = leaseQuantity;
	}
	
	public Integer getId() {
		return id;
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

	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}

	public String getExpectedPayment() {
		return expectedPayment;
	}

	public void setExpectedPayment(String expectedPayment) {
		this.expectedPayment = expectedPayment;
	}

	public String getExpectedSupportTerm() {
		return expectedSupportTerm;
	}

	public void setExpectedSupportTerm(String expectedSupportTerm) {
		this.expectedSupportTerm = expectedSupportTerm;
	}

	public String getExpectedTechnicalSpecification() {
		return expectedTechnicalSpecification;
	}

	public void setExpectedTechnicalSpecification(String expectedTechnicalSpecification) {
		this.expectedTechnicalSpecification = expectedTechnicalSpecification;
	}
	
	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	public UomType getUomType() {
		return uomType;
	}

	public void setUomType(UomType uomType) {
		this.uomType = uomType;
	}

	public UomUnit getUomUnit() {
		return uomUnit;
	}

	public void setUomUnit(UomUnit uomUnit) {
		this.uomUnit = uomUnit;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public QuotationRequest getQuotationRequest() {
		return quotationRequest;
	}

	public void setQuotationRequest(QuotationRequest quotationRequest) {
		this.quotationRequest = quotationRequest;
	}

	public Integer getLeaseQuantity() {
		return leaseQuantity;
	}

	public void setLeaseQuantity(Integer leaseQuantity) {
		this.leaseQuantity = leaseQuantity;
	}

	public List<PurchaseRequestDetail> getPurchaseRequestDetailsList() {
		return purchaseRequestDetailsList;
	}

	public void setPurchaseRequestDetailsList(List<PurchaseRequestDetail> purchaseRequestDetailsList) {
		this.purchaseRequestDetailsList = purchaseRequestDetailsList;
	}

	public List<QuotationRequestAccessoriesMapper> getQuotationRequestAccessoriesMapperList() {
		return quotationRequestAccessoriesMapperList;
	}

	public void setQuotationRequestAccessoriesMapperList(
			List<QuotationRequestAccessoriesMapper> quotationRequestAccessoriesMapperList) {
		this.quotationRequestAccessoriesMapperList = quotationRequestAccessoriesMapperList;
	}
	
	public List<QuotationVendorDetail> getQuotationVendorDetail() {
		return quotationVendorDetail;
	}

	public void setQuotationVendorDetail(List<QuotationVendorDetail> quotationVendorDetail) {
		this.quotationVendorDetail = quotationVendorDetail;
	}
	
	

	public List<PurchaseOrderDetail> getPurchaseOrderDetails() {
		return purchaseOrderDetails;
	}

	public void setPurchaseOrderDetails(List<PurchaseOrderDetail> purchaseOrderDetails) {
		this.purchaseOrderDetails = purchaseOrderDetails;
	}

	
	public Integer getPendingQuantity() {
		return pendingQuantity;
	}

	public void setPendingQuantity(Integer pendingQuantity) {
		this.pendingQuantity = pendingQuantity;
	}

	public QuotationRequestAccessoriesMapper addQuotationRequestAccessoriesMapperList(QuotationRequestAccessoriesMapper qRAccessoriesMapper) {
		if (getQuotationRequestAccessoriesMapperList() == null)
			this.quotationRequestAccessoriesMapperList = new ArrayList<QuotationRequestAccessoriesMapper>();
		
		getQuotationRequestAccessoriesMapperList().add(qRAccessoriesMapper);
		qRAccessoriesMapper.setQuotationRequestDetail(this);
		
		return qRAccessoriesMapper;
	}
	
	public PurchaseRequestDetail addPurchaseRequestDetailList(PurchaseRequestDetail purchaseRequestDetail) {
		if (getPurchaseRequestDetailsList() == null)
			this.purchaseRequestDetailsList = new ArrayList<PurchaseRequestDetail>();

		getPurchaseRequestDetailsList().add(purchaseRequestDetail);
		purchaseRequestDetail.setQuotationRequestDetail(this);

		return purchaseRequestDetail;
	}
	
	public QuotationVendorDetail addQuotationVendorDetailList(QuotationVendorDetail quotationVendorDetail) {
		if (getQuotationVendorDetail() == null)
			this.quotationVendorDetail = new ArrayList<QuotationVendorDetail>();

		getQuotationVendorDetail().add(quotationVendorDetail);
		quotationVendorDetail.setQrDetail(this);

		return quotationVendorDetail;
	}
	
	/*public StoreIndentDetail addStoreIndentDetailList(StoreIndentDetail storeIndentDetail) {
		if (getStoreIndentDetailList() == null)
			this.storeIndentDetailList = new ArrayList<StoreIndentDetail>();

		getStoreIndentDetailList().add(storeIndentDetail);
		storeIndentDetail.setPurchaseRequestDetail(this);

		return storeIndentDetail;
	}*/
}