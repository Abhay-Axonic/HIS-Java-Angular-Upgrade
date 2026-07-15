package com.param.procurement.consignment.dto.consignment;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ConsignmentDto {
	
	protected Integer id;	
	
	private String outwardNumber;
	
	private Date addedDate;
	
	private Integer storeId;

	private String storeName;
	
	private String vendorName;
	
	private Integer vendorId;

	private String remark;
	
	private String outWardBy;
	
	private Integer outWardById;

	private String status;
	
	private Integer statusId;

	
	
	public ConsignmentDto()
	{
		super();
	}
	
		
	public ConsignmentDto(Integer id,String outwardNumber,Date addedDate,Integer storeId,String storeName,String vendorName,Integer vendorId,
			String remark,String outWardBy,Integer outWardById,String status,Integer statusId)
	{
		super();
		this.id=id;
		this.outwardNumber=outwardNumber;
		this.addedDate=addedDate;
		this.storeId=storeId;
		this.storeName=storeName;
		this.vendorName=vendorName;
		this.vendorId=vendorId;
		this.remark=remark;
		this.outWardBy=outWardBy;
		this.outWardById=outWardById;
		this.status=status;
		this.statusId=statusId;
		
	
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public Integer getVendorId() {
		return vendorId;
	}
	public void setVendorId(Integer vendorId) {
		this.vendorId = vendorId;
	}
	public String getVendorName() {
		return vendorName;
	}
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}
	
	public String getOutWardBy() {
		return outWardBy;
	}
	public void setOutWardBy(String outWardBy) {
		this.outWardBy = outWardBy;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
		public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}


	public String getOutwardNumber() {
		return outwardNumber;
	}


	public void setOutwardNumber(String outwardNumber) {
		this.outwardNumber = outwardNumber;
	}


	public Date getAddedDate() {
		return addedDate;
	}


	public void setAddedDate(Date addedDate) {
		this.addedDate = addedDate;
	}


	public String getStoreName() {
		return storeName;
	}


	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}


	public Integer getOutWardById() {
		return outWardById;
	}


	public void setOutWardById(Integer outWardById) {
		this.outWardById = outWardById;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}
	
	
}
