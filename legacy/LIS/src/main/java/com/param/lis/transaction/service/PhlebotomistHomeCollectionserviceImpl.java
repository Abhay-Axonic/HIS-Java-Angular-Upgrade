package com.param.lis.transaction.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.transaction.dao.IPhlebotomistHomeCollectionDao;
import com.param.lis.transaction.dto.PhlebotomistHomeCollectionDto;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class PhlebotomistHomeCollectionserviceImpl implements IPhlebotomistHomeCollectionservice, ICommonConstants, IError,ITransactionConstants
{

	@Autowired
	IPhlebotomistHomeCollectionDao iPhlebotomistHomeCollectionDao;
	
	@Override
	public Response savePhlebotomistHomeCollection(List<PhlebotomistHomeCollectionDto> dto) {
		try
		{
			for (PhlebotomistHomeCollectionDto phlebotomistHomeCollectionDto : dto) {
				 iPhlebotomistHomeCollectionDao.savePhlebotomistHomeCollection(phlebotomistHomeCollectionDto);
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	public Response validateUserAlocation(PhlebotomistHomeCollectionDto dto) {
		try
		{
			return iPhlebotomistHomeCollectionDao.validateUserAlocation(dto);
		} catch (Exception e)
		{
			throw e;
		}
	}

}
