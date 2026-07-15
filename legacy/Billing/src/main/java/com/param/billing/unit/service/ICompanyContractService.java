package com.param.billing.unit.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.billing.global.transaction.config.dto.TPatientContractCapDetailsDto;
import com.param.billing.unit.dto.ChargeConversionDto;
import com.param.global.billing.dto.TContractServiceDetailsDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CompanyMasterDto;
import com.param.global.dto.ItemsForContractDto;
import com.param.service.dto.CompanyContractReqDto;
import com.param.service.dto.ContractServiceCheckReqDto;
import com.param.service.dto.ServiceForCompnayContarctReqDto;
import com.param.service.dto.TContractGroupDetailsDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")

public interface ICompanyContractService {
	
	@Transactional
	public  Response getCompanyMasterList(CompanyMasterDto companyMasterDto)throws ApplicationException;
	
	@Transactional
	public  Response getAssociateCompanyMaster(CompanyMasterDto companyMasterDto)throws ApplicationException;
	
	@Transactional
	public  Response getGradeListByAssociateCompanyId(CompanyMasterDto companyMasterDto)throws ApplicationException;
	
	@Transactional
	public  Response getVisitTypeList(CompanyMasterDto companyMasterDto)throws ApplicationException;
	
	@Transactional
	public  Response createCompanyContarct(CompanyContractReqDto companyContractReqDto)throws ApplicationException;
	
	@Transactional
	public Response searchCompanyContract(ServiceForCompnayContarctReqDto reqDto) throws ServiceException;

	@Transactional
	public Response getContractByCompanyId(CompanyMasterDto reqDto)throws ServiceException;


	@Transactional
	public Response getServiceRateByCompanyId(CompanyMasterDto reqDto)throws ServiceException;
	
	@Transactional
	public com.param.billing.common.Response savePatientContarctCapDetails(TPatientContractCapDetailsDto tPatientContractCapDetailsDto) throws ApplicationException;
	
	@Transactional
	public Response checkIfServiceFromContarct(ContractServiceCheckReqDto contractServiceCheckReqDto) throws ServiceException;
	@Transactional
	public Response getChargeConversionListByCurrentPayer(ChargeConversionDto reqDto)throws ServiceException;
	
	@Transactional
	public Response getChargeConversionListByConvertPayer(ChargeConversionDto reqDto)throws ServiceException;

	@Transactional
	public Response getItemListByAssetAndProductCategory(ItemsForContractDto itemsForContractDto)throws ServiceException;

	@Transactional
	public Response getItemListByProductCategoryForEquipment(ItemsForContractDto itemsForContractDto)throws ServiceException;
	
	@Transactional
	public Response getProductAssetInclusionList(ItemsForContractDto itemsForContractDto)throws ServiceException;

	@Transactional
	public Response getCompanyContractDetailByContractId(CompanyContractReqDto companyContractReqDto)throws ServiceException;

	@Transactional
	public Response updateStatus(Integer contractId, Character status)throws ServiceException;
	@Transactional
	public Response CompanyContractListSearch(ServiceForCompnayContarctReqDto reqDto)throws ServiceException;
	
	@Transactional
	public Response searchCompanyContractForVersion(ServiceForCompnayContarctReqDto reqDto)throws ServiceException;

	@Transactional
	public Response getContractDetailsBySubDept(TContractGroupDetailsDto reqDto)throws ServiceException;

	@Transactional
	public Response addServicesInContract(List<TContractServiceDetailsDto> masterDtoList);

	@Transactional
	public Response getAssociateCompanyMasterByMultipleCompanyIds(CompanyMasterDto companyMasterDto)throws ApplicationException;

}
