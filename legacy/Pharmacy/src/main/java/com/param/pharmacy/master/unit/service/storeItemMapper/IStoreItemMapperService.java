package com.param.pharmacy.master.unit.service.storeItemMapper;

import java.math.BigInteger;
import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dto.MultipleStoreRequest;
import com.param.pharmacy.master.unit.dto.storeItemMapper.GetStoreItemRequest;
import com.param.pharmacy.master.unit.dto.storeItemMapper.SIResponse;
import com.param.pharmacy.master.unit.dto.storeItemMapper.StoreItemMapperDetailsResponse;
import com.param.pharmacy.master.unit.dto.storeItemMapper.StoreItemMapperListResponse;

public interface IStoreItemMapperService {

	StoreItemMapperDetailsResponse getById(BigInteger id);

	CommonResponse update(List<SIResponse> response, Integer userId);

	CommonResponse create(List<GetStoreItemRequest> getStoreItemRequest);

	List<StoreItemMapperListResponse> getAll(Integer storeId);

	CommonResponse GetMultipleStore(StoreItemMapperListResponse request);

	CommonResponse removeItemMapping(SIResponse request) throws CustomeException;

	List<StoreItemMapperListResponse> GetTransactionCount(List<MultipleStoreRequest> request, BigInteger itemId);


}
