package com.param.billing.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.global.dao.IHospitalAccountMasterDao;
import com.param.billing.global.dto.HospitalAccountMasterDto;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class HospitalAccountMasterServiceImpl implements IHospitalAccountMasterService,ICommonConstants{

	@Autowired
	private IHospitalAccountMasterDao iHospitalAccountMasterDao;

	@Autowired
	private IBankMasterService iBankMasterService;

	@Override
	public Response getActiveHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			return iHospitalAccountMasterDao.getActiveHospitalAccountMaster(hospitalAccountMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			Response response=iHospitalAccountMasterDao.getHospitalAccountMasterByName(hospitalAccountMasterDto);
			
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				if(hospitalAccountMasterDto.getIsDefault() == 'Y')
				{
					iHospitalAccountMasterDao.updateAllHospitalAccount(hospitalAccountMasterDto);
				}
				Response saveRes = iHospitalAccountMasterDao.saveHospitalAccountMaster(hospitalAccountMasterDto);

				if(saveRes.getStatus().equals(SUCCESS)) {
					iBankMasterService.invalidateHospitalBankMasterCache(hospitalAccountMasterDto.getUnitId());
				}
				return saveRes;
			}
					
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getHospitalAccountMasterById(Integer hospitalAccountId, Integer orgId, Integer unitId) {
		try {
			return iHospitalAccountMasterDao.getHospitalAccountMasterById(hospitalAccountId, orgId,unitId);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getHospitalAccountMasterList(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			return iHospitalAccountMasterDao.getHospitalAccountMasterList(hospitalAccountMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			Response response=iHospitalAccountMasterDao.getHospitalAccountMasterByNameNotId(hospitalAccountMasterDto);
			if(response.getListObject().size()>0 && response.getListObject()!=null){
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			}else{
				iHospitalAccountMasterDao.updateAllHospitalAccount(hospitalAccountMasterDto);
				Response updateRes = iHospitalAccountMasterDao.updateHospitalAccountMaster(hospitalAccountMasterDto);
				if(updateRes.getStatus().equals(SUCCESS)) {
					iBankMasterService.invalidateHospitalBankMasterCache(hospitalAccountMasterDto.getUnitId());
				}
				return updateRes;
			}
			
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateHospitalAccountMasterStatus(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			Response saveRes = iHospitalAccountMasterDao.updateHospitalAccountMasterStatus(hospitalAccountMasterDto);
			if(saveRes.getStatus().equals(SUCCESS) && saveRes.getObject() != null) {
				Integer filterId = (Integer) saveRes.getObject();
				iBankMasterService.invalidateHospitalBankMasterCache(hospitalAccountMasterDto.getUnitId() !=null ? hospitalAccountMasterDto.getUnitId():filterId);
			}
			return saveRes;
		} catch (Exception e) {
			
			throw e;
		}
	//	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getHospitalAccountMasterCount(HospitalAccountMasterDto hospitalAccountMasterDto) {
		try {
			return iHospitalAccountMasterDao.getHospitalAccountMasterCount(hospitalAccountMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
		//return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	}

