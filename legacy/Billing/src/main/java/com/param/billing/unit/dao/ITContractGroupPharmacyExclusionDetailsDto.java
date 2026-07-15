package com.param.billing.unit.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.service.dto.TContractGroupPharmacyDetailsDto;
import com.param.service.global.model.ContractItemDetails;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface ITContractGroupPharmacyExclusionDetailsDto extends IGenericDao<ContractItemDetails,Integer> {
	Response saveContractGroupPharmacyInclusionDetails(List<TContractGroupPharmacyDetailsDto> masterDtoList)throws ApplicationException;

	Response saveContractGroupPharmacyExclusionDetails(List<TContractGroupPharmacyDetailsDto> groupPharmacyExclusionDetailsDtoList)throws ApplicationException;

}
