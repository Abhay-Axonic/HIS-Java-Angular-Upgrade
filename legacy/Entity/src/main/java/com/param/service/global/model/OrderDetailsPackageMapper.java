package com.param.service.global.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.param.billing.packages.model.PackageMaster;
import com.param.global.model.OrderDetailsMaster;

@Entity
@Table(name = "t_order_details_package_mapper", schema = "public")
public class OrderDetailsPackageMapper {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	private Integer id;

	@Column(name = "order_details_id")
	private Integer orderDetailsId;

	@Column(name = "service_id")
	private Integer serviceId;

	@Column(name = "package_id")
	private Integer packageId;

	@Column(name = "is_package_service")
	private char isPackageService;

	@Column(name = "is_cap_service")
	private char isCapService;

	@Column(name = "payee_id")
	private Integer payeeId;

	@Column(name = "service_amount")
	private BigDecimal serviceAmount;

	@Column(name = "package_amount")
	private BigDecimal packageAmount;

	@Column(name = "t_sale_details_id")
	private Integer saleDetailsId;

	@Column(name = "item_id")
	private Integer itemId;

	@Column(name = "status")
	private Character status;

	@Column(name = "unit_id")
	private Integer unitId;

	@Column(name = "organization_id")
	private Integer organizationId;

	@Column(name = "package_consumption_cap_details_id")
	private Integer packageConsumptionCapDetailsId;

	@Column(name = "package_consumption_master_id")
	private Integer packageConsumptionMasterId;

	@Column(name = "pacakge_consumption_service_detail_id")
	private Integer pacakgeConsumptionServiceDetailId;

	@Column(name = "package_qty")
	private Integer packageQty;

	@Column(name = "package_consumtion_eo_detail_id")
	private Integer packageConsumptionEODetailId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "package_id", insertable = false, updatable = false, nullable = false)
	private MPackageMaster packageMaster;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_details_id", insertable = false, updatable = false, nullable = false)
	private OrderDetailsMaster orderDetailsMaster;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderDetailsId() {
		return orderDetailsId;
	}

	public void setOrderDetailsId(Integer orderDetailsId) {
		this.orderDetailsId = orderDetailsId;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getPackageId() {
		return packageId;
	}

	public void setPackageId(Integer packageId) {
		this.packageId = packageId;
	}

	public Integer getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Integer payeeId) {
		this.payeeId = payeeId;
	}

	public BigDecimal getServiceAmount() {
		return serviceAmount!=null?serviceAmount:BigDecimal.ZERO;
	}

	public void setServiceAmount(BigDecimal serviceAmount) {
		this.serviceAmount = serviceAmount!=null?serviceAmount:BigDecimal.ZERO;
	}

	public BigDecimal getPackageAmount() {
		return packageAmount;
	}

	public void setPackageAmount(BigDecimal packageAmount) {
		this.packageAmount = packageAmount;
	}

	public Integer getSaleDetailsId() {
		return saleDetailsId;
	}

	public void setSaleDetailsId(Integer saleDetailsId) {
		this.saleDetailsId = saleDetailsId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Character getStatus() {
		return status;
	}

	public void setStatus(Character status) {
		this.status = status;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public char getIsPackageService() {
		return isPackageService;
	}

	public void setIsPackageService(char isPackageService) {
		this.isPackageService = (isPackageService == '\u0000') ? 'N' : isPackageService;
	}

	public char getIsCapService() {
		return isCapService;
	}

	public void setIsCapService(char isCapService) {
		this.isCapService = (isCapService == '\u0000') ? 'N' : isCapService;
	}

	public Integer getPackageConsumptionCapDetailsId() {
		return packageConsumptionCapDetailsId;
	}

	public void setPackageConsumptionCapDetailsId(Integer packageConsumptionCapDetailsId) {
		this.packageConsumptionCapDetailsId = packageConsumptionCapDetailsId;
	}

	public Integer getPackageConsumptionMasterId() {
		return packageConsumptionMasterId;
	}

	public void setPackageConsumptionMasterId(Integer packageConsumptionMasterId) {
		this.packageConsumptionMasterId = packageConsumptionMasterId;
	}

	public Integer getPacakgeConsumptionServiceDetailId() {
		return pacakgeConsumptionServiceDetailId;
	}

	public void setPacakgeConsumptionServiceDetailId(Integer pacakgeConsumptionServiceDetailId) {
		this.pacakgeConsumptionServiceDetailId = pacakgeConsumptionServiceDetailId;
	}

	public Integer getPackageQty() {
		return packageQty;
	}

	public void setPackageQty(Integer packageQty) {
		this.packageQty = packageQty;
	}

	public Integer getPackageConsumptionEODetailId() {
		return packageConsumptionEODetailId;
	}

	public void setPackageConsumptionEODetailId(Integer packageConsumptionEODetailId) {
		this.packageConsumptionEODetailId = packageConsumptionEODetailId;
	}

	public MPackageMaster getPackageMaster() {
		return packageMaster;
	}

	public void setPackageMaster(MPackageMaster packageMaster) {
		this.packageMaster = packageMaster;
	}




}
