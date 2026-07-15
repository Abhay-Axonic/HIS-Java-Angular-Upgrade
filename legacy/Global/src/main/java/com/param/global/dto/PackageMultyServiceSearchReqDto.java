package com.param.global.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PackageMultyServiceSearchReqDto {

	Integer packageTypeId;
	Integer orgId;
	Integer unitId;
	Integer packageServiceId;
	private Integer sexId;
	Set<Integer> serviceIds;
	Set<Integer> itemIds;
	List<ServiceForPackageReqDto> serviceForPackageReqList;
	
	public Set<Integer> getServiceIds() {
		return serviceIds;
	}
	public void setServiceIds(Set<Integer> serviceIds) {
		this.serviceIds = serviceIds;
	}
	
	public Set<Integer> getItemIds() {
		return itemIds;
	}
	public void setItemIds(Set<Integer> itemIds) {
		this.itemIds = itemIds;
	}
	public List<ServiceForPackageReqDto> getServiceForPackageReqList() {
		return serviceForPackageReqList;
	}
	public void setServiceForPackageReqList(List<ServiceForPackageReqDto> serviceForPackageReqList) {
		this.serviceForPackageReqList = serviceForPackageReqList;
	}
	public Integer getPackageTypeId() {
		return packageTypeId;
	}
	public void setPackageTypeId(Integer packageTypeId) {
		this.packageTypeId = packageTypeId;
	}
	public Integer getPackageServiceId() {
		return packageServiceId;
	}
	public void setPackageServiceId(Integer packageServiceId) {
		this.packageServiceId = packageServiceId;
	}
	public Integer getOrgId() {
		return orgId;
	}
	public void setOrgId(Integer orgId) {
		this.orgId = orgId;
	}
	public Integer getUnitId() {
		return unitId;
	}
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	public Integer getSexId() {
		return sexId;
	}

	public void setSexId(Integer sexId) {
		this.sexId = sexId;
	}
}
