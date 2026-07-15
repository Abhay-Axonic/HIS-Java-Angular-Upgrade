package com.param.lis.transaction.service;

import java.util.List;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.lis.transaction.dto.PhlebotomistHomeCollectionDto;

@Transactional
@SuppressWarnings("rawtypes")
public interface IPhlebotomistHomeCollectionservice {

	Response savePhlebotomistHomeCollection(List<PhlebotomistHomeCollectionDto> list);

	Response validateUserAlocation(PhlebotomistHomeCollectionDto dto);

}
