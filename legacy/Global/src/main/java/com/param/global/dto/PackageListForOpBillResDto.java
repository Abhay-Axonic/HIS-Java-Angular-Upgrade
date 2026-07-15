package com.param.global.dto;

import java.util.List;

public class PackageListForOpBillResDto {
	private MPackageMasterDto mPackageMasterDto;
	private List<TPackageServicesDetailsDto> listTPackageServicesDetailsDto;
	private List<TPackageCapDetailsDto> listTPackageGroupWiseCapDetailsDto;
	private List<TPackageEitherorGroupDetailsDto> listTPackageEitherorGroupDetailsDto;
	private List<TPackageIncExcDetailsDto> listTPackageIncExcDetailsDto;
	private List<TPackageBedCategoryDetailDto> listTPackageBedCategoryDetailDto;
	
	
	public List<TPackageIncExcDetailsDto> getListTPackageIncExcDetailsDto() {
		return listTPackageIncExcDetailsDto;
	}
	public void setListTPackageIncExcDetailsDto(List<TPackageIncExcDetailsDto> listTPackageIncExcDetailsDto) {
		this.listTPackageIncExcDetailsDto = listTPackageIncExcDetailsDto;
	}
	public List<TPackageEitherorGroupDetailsDto> getListTPackageEitherorGroupDetailsDto() {
		return listTPackageEitherorGroupDetailsDto;
	}
	public void setListTPackageEitherorGroupDetailsDto(
			List<TPackageEitherorGroupDetailsDto> listTPackageEitherorGroupDetailsDto) {
		this.listTPackageEitherorGroupDetailsDto = listTPackageEitherorGroupDetailsDto;
	}
	public MPackageMasterDto getmPackageMasterDto() {
		return mPackageMasterDto;
	}
	public void setmPackageMasterDto(MPackageMasterDto mPackageMasterDto) {
		this.mPackageMasterDto = mPackageMasterDto;
	}
	public List<TPackageServicesDetailsDto> getListTPackageServicesDetailsDto() {
		return listTPackageServicesDetailsDto;
	}
	public void setListTPackageServicesDetailsDto(List<TPackageServicesDetailsDto> listTPackageServicesDetailsDto) {
		this.listTPackageServicesDetailsDto = listTPackageServicesDetailsDto;
	}
	public List<TPackageCapDetailsDto> getListTPackageGroupWiseCapDetailsDto() {
		return listTPackageGroupWiseCapDetailsDto;
	}
	public void setListTPackageGroupWiseCapDetailsDto(List<TPackageCapDetailsDto> listTPackageGroupWiseCapDetailsDto) {
		this.listTPackageGroupWiseCapDetailsDto = listTPackageGroupWiseCapDetailsDto;
	}
	public List<TPackageBedCategoryDetailDto> getListTPackageBedCategoryDetailDto() {
		return listTPackageBedCategoryDetailDto;
	}
	public void setListTPackageBedCategoryDetailDto(List<TPackageBedCategoryDetailDto> listTPackageBedCategoryDetailDto) {
		this.listTPackageBedCategoryDetailDto = listTPackageBedCategoryDetailDto;
	}
	
}
