package com.param.global.dto;

public class PackageBillingBedCategory {

	private Integer billingBedCategoryId;
	private String billingBedCategory;
	private MPackageMasterDto mPackageMasterDto;

	public Integer getBillingBedCategoryId() {
		return billingBedCategoryId;
	}

	public void setBillingBedCategoryId(Integer billingBedCategoryId) {
		this.billingBedCategoryId = billingBedCategoryId;
	}

	public String getBillingBedCategory() {
		return billingBedCategory;
	}

	public void setBillingBedCategory(String billingBedCategory) {
		this.billingBedCategory = billingBedCategory;
	}

	public MPackageMasterDto getmPackageMasterDto() {
		return mPackageMasterDto;
	}

	public void setmPackageMasterDto(MPackageMasterDto mPackageMasterDto) {
		this.mPackageMasterDto = mPackageMasterDto;
	}

}
