package com.param.service.dto;

import java.util.List;

import com.param.global.billing.dto.TContractServiceDetailsDto;


public class CompanyContractReqDto {

	private Integer orgId;
	
	private Integer unitId;
	
	private Integer contractId; 
	
	private MCompanyContractMasterDto companyContractMasterDto;
	
	private TContractGroupPharmacyDetailsDto contractGroupPharmacyIncusionDetailsDto;
	
	private List<TContractBedCategoryDetailDto> listTContractBedCategoryDetailDto;
	
	private List<TContractGroupDetailsDto> listContractGroupExclusion;
	
	private List<TContractGroupDetailsDto> listContractGroupWiseDetails;
	
	private List<TContractCapDetailsDto> listContractServiceWiseCapDetails;
	
	private List<TContractCapDetailsDto> listTContractCapDetailsDto;
	
	private List<TContractProcedureDetailsDto> procedureDetailsDtosList;
	
	private List<TContractServiceDetailsDto> listTContractServiceDetailsDto;
	
	private List<TContractServiceDetailsDto> listTContractPackageDetailsDto;
	
	private List<Integer> itemIdInclusionList;
	
	private List<TContractGroupPharmacyDetailsDto> itemList;
	
	private List<ContractProductCategoryDto> contractCapProductList;
	
	private List<TContractGroupPharmacyDetailsDto> groupPharmacyExclusionDetailsList;
	
	private Integer oldContractId; 
	
	private Integer versionNo; 
	
	private Integer parentContractId;


	public Integer getParentContractId() {
		return parentContractId;
	}

	public void setParentContractId(Integer parentContractId) {
		this.parentContractId = parentContractId;
	}

	public Integer getVersionNo() {
		return versionNo;
	}

	public void setVersionNo(Integer versionNo) {
		this.versionNo = versionNo;
	}

	public Integer getOldContractId() {
		return oldContractId;
	}

	public void setOldContractId(Integer oldContractId) {
		this.oldContractId = oldContractId;
	}

	public List<TContractServiceDetailsDto> getListTContractPackageDetailsDto() {
		return listTContractPackageDetailsDto;
	}

	public void setListTContractPackageDetailsDto(List<TContractServiceDetailsDto> listTContractPackageDetailsDto) {
		this.listTContractPackageDetailsDto = listTContractPackageDetailsDto;
	}

	public List<TContractCapDetailsDto> getListContractServiceWiseCapDetails() {
		return listContractServiceWiseCapDetails;
	}

	public void setListContractServiceWiseCapDetails(List<TContractCapDetailsDto> listContractServiceWiseCapDetails) {
		this.listContractServiceWiseCapDetails = listContractServiceWiseCapDetails;
	}

	public List<TContractGroupPharmacyDetailsDto> getItemList() {
		return itemList;
	}

	public void setItemList(List<TContractGroupPharmacyDetailsDto> itemList) {
		this.itemList = itemList;
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

	public Integer getContractId() {
		return contractId;
	}

	public void setContractId(Integer contractId) {
		this.contractId = contractId;
	}

	public MCompanyContractMasterDto getCompanyContractMasterDto() {
		return companyContractMasterDto;
	}

	public void setCompanyContractMasterDto(MCompanyContractMasterDto companyContractMasterDto) {
		this.companyContractMasterDto = companyContractMasterDto;
	}

	public TContractGroupPharmacyDetailsDto getContractGroupPharmacyIncusionDetailsDto() {
		return contractGroupPharmacyIncusionDetailsDto;
	}

	public void setContractGroupPharmacyIncusionDetailsDto(
			TContractGroupPharmacyDetailsDto contractGroupPharmacyIncusionDetailsDto) {
		this.contractGroupPharmacyIncusionDetailsDto = contractGroupPharmacyIncusionDetailsDto;
	}

	public List<TContractBedCategoryDetailDto> getListTContractBedCategoryDetailDto() {
		return listTContractBedCategoryDetailDto;
	}

	public void setListTContractBedCategoryDetailDto(
			List<TContractBedCategoryDetailDto> listTContractBedCategoryDetailDto) {
		this.listTContractBedCategoryDetailDto = listTContractBedCategoryDetailDto;
	}

	public List<TContractGroupDetailsDto> getListContractGroupExclusion() {
		return listContractGroupExclusion;
	}

	public void setListContractGroupExclusion(List<TContractGroupDetailsDto> listContractGroupExclusion) {
		this.listContractGroupExclusion = listContractGroupExclusion;
	}

	public List<TContractGroupDetailsDto> getListContractGroupWiseDetails() {
		return listContractGroupWiseDetails;
	}

	public void setListContractGroupWiseDetails(List<TContractGroupDetailsDto> listContractGroupWiseDetails) {
		this.listContractGroupWiseDetails = listContractGroupWiseDetails;
	}

	public List<TContractCapDetailsDto> getListTContractCapDetailsDto() {
		return listTContractCapDetailsDto;
	}

	public void setListTContractCapDetailsDto(List<TContractCapDetailsDto> listTContractCapDetailsDto) {
		this.listTContractCapDetailsDto = listTContractCapDetailsDto;
	}

	public List<TContractProcedureDetailsDto> getProcedureDetailsDtosList() {
		return procedureDetailsDtosList;
	}

	public void setProcedureDetailsDtosList(List<TContractProcedureDetailsDto> procedureDetailsDtosList) {
		this.procedureDetailsDtosList = procedureDetailsDtosList;
	}

	public List<TContractServiceDetailsDto> getListTContractServiceDetailsDto() {
		return listTContractServiceDetailsDto;
	}

	public void setListTContractServiceDetailsDto(List<TContractServiceDetailsDto> listTContractServiceDetailsDto) {
		this.listTContractServiceDetailsDto = listTContractServiceDetailsDto;
	}

	public List<Integer> getItemIdInclusionList() {
		return itemIdInclusionList;
	}

	public void setItemIdInclusionList(List<Integer> itemIdInclusionList) {
		this.itemIdInclusionList = itemIdInclusionList;
	}

	public List<ContractProductCategoryDto> getContractCapProductList() {
		return contractCapProductList;
	}

	public void setContractCapProductList(List<ContractProductCategoryDto> contractCapProductList) {
		this.contractCapProductList = contractCapProductList;
	}

	public List<TContractGroupPharmacyDetailsDto> getGroupPharmacyExclusionDetailsList() {
		return groupPharmacyExclusionDetailsList;
	}

	public void setGroupPharmacyExclusionDetailsList(
			List<TContractGroupPharmacyDetailsDto> groupPharmacyExclusionDetailsList) {
		this.groupPharmacyExclusionDetailsList = groupPharmacyExclusionDetailsList;
	}
	
}
