package com.param.global.billing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.global.model.CouponTemplateMaster;
import com.param.global.billing.dao.ICouponTemplateDetailsDao;
import com.param.global.billing.dao.ICouponTemplateMasterDao;
import com.param.global.billing.dto.CouponTemplateMasterDto;
import com.param.global.billing.dto.PatientCouponTemplateMapperDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class CouponTemplateMasterServiceImpl implements ICouponTemplateMasterService, ICommonConstants{

	@Autowired
	ICouponTemplateMasterDao iCouponTemplateMasterDao;
	
	@Autowired
	ICouponTemplateDetailsDao iCouponTemplateDetailsDao;
	
	@Override
	public Response saveCouponTemplateMaster(CouponTemplateMasterDto dto) throws ApplicationException {
		try {
			Response res =  iCouponTemplateMasterDao.saveCouponTemplateMaster(dto);
			
			if(res.getObject() != null) {
				CouponTemplateMaster master = (CouponTemplateMaster)res.getObject();
				return iCouponTemplateDetailsDao.saveCouponTemplateMasterDetails(dto.getCouponTemplateDetailsDtoList(),master);
			}
			
		} catch (Exception e) {
			
			throw e;
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCouponTemplateMaster(CouponTemplateMasterDto dto) throws ApplicationException {
		try {
			return iCouponTemplateMasterDao.getCouponTemplateMaster(dto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response updateStatus(CouponTemplateMasterDto dto) throws ApplicationException {
		try {
			return iCouponTemplateMasterDao.updateStatus(dto);
			
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getCouponTemplateDetails(CouponTemplateMasterDto dto) throws ApplicationException {
		try {
			Response res =  iCouponTemplateMasterDao.getCouponTemplateMasterById(dto);
			
			if(res.getObject() != null) {
				CouponTemplateMasterDto masterDto = (CouponTemplateMasterDto) res.getObject();
				Response res1 =  iCouponTemplateDetailsDao.getCouponTemplateDetailsById(masterDto.getCouponTemplateMasterId());
				if(res1.getListObject()!= null && res1.getListObject().size()>0) {
					masterDto.setCouponTemplateDetailsDtoList(res1.getListObject());
				}
				
				if(dto.getScreenId() != null && masterDto.getServiceId() != null) {
					Response rescheck = iCouponTemplateMasterDao.getCheckServiceBilledOrNot(dto,masterDto.getServiceId());
					
					if(rescheck.getListObject()!= null && rescheck.getListObject().size()>0) {
						PatientCouponTemplateMapperDto mapper = (PatientCouponTemplateMapperDto) rescheck.getListObject().get(0);
						masterDto.setOrderDetailsId(mapper.getOrderDetailsId());
						return new Response<>(SUCCESS, SUCCESS_CODE, null, null, masterDto);
					}else {
						return new Response(ERROR, COMMON_ERROR_CODE,"Bill is not created for "+masterDto.getCouponTemplateName(), null, null);
					}
				}
				
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, masterDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
			
		} catch (Exception e) {
			
			throw e;
		}
	}

}
