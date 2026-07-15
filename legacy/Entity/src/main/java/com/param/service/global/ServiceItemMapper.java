package com.param.service.global;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "m_service_item_mapper", schema = "service")
@SequenceGenerator(name = "m_service_item_mapper_seq", sequenceName = "service.m_service_item_mapper_seq", allocationSize = 1)
public class ServiceItemMapper {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "m_service_item_mapper_seq")
	@Column(name = "service_item_mapper_id")
	private Integer serviceItemMapperId;

	@Column(name = "item_id")
	private Integer itemId;
	
	@Column(name = "assign_qty")
	private Integer assignQty;
	
	@Column(name = "service_id")
	private Integer serviceId;
	
	@Column(name = "organization_id")
	private Integer organizationId;
	
	@Column(name="created_date")
	private Date createdDate;
	
	@Column(name="updated_date")
	private Date updatedDate;
	
	@Column(name="created_by")
	private Integer createdBy;
	
	@Column(name="updated_by")
	private Integer updatedBy;
	
	@Column(name="status")
	private char status;

	public Integer getServiceItemMapperId() {
		return serviceItemMapperId;
	}

	public void setServiceItemMapperId(Integer serviceItemMapperId) {
		this.serviceItemMapperId = serviceItemMapperId;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getAssignQty() {
		return assignQty;
	}

	public void setAssignQty(Integer assignQty) {
		this.assignQty = assignQty;
	}

	public Integer getServiceId() {
		return serviceId;
	}

	public void setServiceId(Integer serviceId) {
		this.serviceId = serviceId;
	}

	public Integer getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Integer getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		this.createdBy = createdBy;
	}

	public Integer getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Integer updatedBy) {
		this.updatedBy = updatedBy;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}
	
	
	
	
}
