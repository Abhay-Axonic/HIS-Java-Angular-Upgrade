package com.param.billing.unit.dao;

import java.util.List;
import java.util.Set;

import com.param.billing.unit.dto.ChargeConversionDto;
import com.param.global.billing.dto.TContractServiceDetailsDto;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.CompanyMasterDto;
import com.param.global.dto.ItemsForContractDto;
import com.param.service.dto.CompanyContractReqDto;
import com.param.service.dto.MCompanyContractMasterDto;
import com.param.service.dto.ServiceForCompnayContarctReqDto;
import com.param.service.dto.TContractGroupDetailsDto;
import com.param.service.dto.TContractGroupPharmacyDetailsDto;
import com.param.service.global.model.MCompanyContractMaster;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICompanyContractDao extends IGenericDao<MCompanyContractMaster,Integer>{

	
	public  Response getCompanyMasterList(CompanyMasterDto companyMasterDto)throws ApplicationException;
	
	public  Response getAssociateCompanyMaster(CompanyMasterDto companyMasterDto)throws ApplicationException;
	
	public  Response getGradeListByAssociateCompanyId(CompanyMasterDto companyMasterDto)throws ApplicationException;
	
	public  Response getVisitTypeList(CompanyMasterDto companyMasterDto)throws ApplicationException;
	
	public  Response createCompanyContarct(MCompanyContractMasterDto mCompanyContractMasterDto)throws ApplicationException;
	
	public Response searchCompanyContract(ServiceForCompnayContarctReqDto reqDto) throws ServiceException;

	public Response getContractByCompanyId(CompanyMasterDto reqDto)throws ServiceException;

	public Response getServiceRateByCompanyId(CompanyMasterDto reqDto)throws ServiceException;
	
	public Response getChargeConversionListByCurrentPayer(ChargeConversionDto reqDto)throws ServiceException;
	
	public Response getChargeConversionListByConvertPayer(ChargeConversionDto reqDto)throws ServiceException;

	public Response getItemListByAssetAndProductCategory(ItemsForContractDto itemsForContractDto)throws ServiceException;

	public Response getProductAssetInclusionList(ItemsForContractDto itemsForContractDto)throws ServiceException;

	public Response getItemListByProductCategoryForEquipment(ItemsForContractDto itemsForContractDto)throws ServiceException;

	public List<TContractServiceDetailsDto> getTariffRatesForContract(MCompanyContractMasterDto contractMasterDto)throws ServiceException;

	public Response getCompanyContractDetailByContractId(CompanyContractReqDto companyContractDto)throws ServiceException;

	Response getInclusionItemList(TContractGroupPharmacyDetailsDto contractGroupPharmacyExclusionDto, Set<Integer> excludeProdCatList)
			throws ServiceException;

	public Response updateStatus(Integer contractId, Character status, Character isVersioning, Integer parConId)throws ServiceException;

	public Response CompanyContractListSearch(ServiceForCompnayContarctReqDto reqDto)throws ServiceException;

	public Response searchCompanyContractForVersion(ServiceForCompnayContarctReqDto reqDto)throws ServiceException;

	public Response getContractDetailsBySubDept(TContractGroupDetailsDto reqDto)throws ServiceException;

	public Response getAssociateCompanyMasterByMultipleCompanyIds(CompanyMasterDto companyMasterDto)throws ApplicationException;

	public Response getCompanyContarctName(MCompanyContractMasterDto companyContractMasterDto)throws ApplicationException;
}
