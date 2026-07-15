package com.param.pharmacy.master.unit.service.storewiseGenericMapper;

import java.util.List;

import com.param.common.dto.CommonResponse;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.StorewiseGenericMapperDetailsResponse;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.StorewiseGenericMapperListResponse;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.StorewiseGenericMapperRequest;
import com.param.pharmacy.master.unit.dto.storewiseGenericMapper.VedResponseList;
import io.swagger.models.auth.In;

public interface IStorewiseGenericMapperService {

	List<StorewiseGenericMapperListResponse> getAll(Integer unitId);

	StorewiseGenericMapperDetailsResponse getById(Integer id);

	CommonResponse create(StorewiseGenericMapperRequest storewiseGenericMapperRequest);

	CommonResponse update(StorewiseGenericMapperRequest storewiseGenericMapperRequest) throws CustomeException;

	List<VedResponseList> getDropdownList();

	Object getGenericList(String name, Integer unitId, Integer orgId);

}
