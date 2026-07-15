package com.param.lis.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.lis.global.common.Response;
import com.param.lis.unit.dao.IMicrobiologyTestMasterDao;
import com.param.lis.unit.dto.MicrobiologyTestMasterDto;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes"})
public class MicrobiologyTestMasterServiceImpl implements IMicrobiologyTestMasterService, ICommonConstants{

	@Autowired
	IMicrobiologyTestMasterDao iMicrobiologyTestMasterDao;
	@Override
	public Response saveMicrobiologyTestMaster(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try {
			Response res =iMicrobiologyTestMasterDao.getMicrobiologyTestMasterByCode(microbiologyTestMasterDto);
			
			Response response=iMicrobiologyTestMasterDao.getMicrobiologyTestMasterByName(microbiologyTestMasterDto);
			if(res.getListObject().size()>0 && res.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIT_BIOCHEMICAL_TEST_CODE, null, null);
			}else if(response.getListObject().size()>0 && response.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIT_BIOCHEMICAL_TEST_DESC, null, null);
			}else{
				return iMicrobiologyTestMasterDao.saveMicrobiologyTestMaster(microbiologyTestMasterDto);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response updateMicrobiologyTestMaster(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try {
			Response res = iMicrobiologyTestMasterDao.checkMicrobiologyTestMasterByCode(microbiologyTestMasterDto);
			
			Response response=iMicrobiologyTestMasterDao.getMicrobiologyTestMasterByNameNotId(microbiologyTestMasterDto);
			
			if(res.getListObject().size()>0 && res.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIT_BIOCHEMICAL_TEST_CODE, null, null);
			}else if(response.getListObject().size()>0 && response.getListObject()!=null){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIT_BIOCHEMICAL_TEST_DESC, null, null);
			}else{
				return iMicrobiologyTestMasterDao.updateMicrobiologyTestMaster(microbiologyTestMasterDto);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response updateMicrobiologyTestMasterStatus(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try {
			return iMicrobiologyTestMasterDao.updateMicrobiologyTestMasterStatus(microbiologyTestMasterDto);
		}catch (Exception e) {
			throw e;
		}
	}
		

	@Override
	public Response getMicrobiologyTestMasterList(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try {
			return iMicrobiologyTestMasterDao.getMicrobiologyTestMasterList(microbiologyTestMasterDto);
		}catch (Exception e) {
			throw e;
		}
	}
		

	@Override
	public Response getMicrobiologyTestMasterById(Integer microTestId, Integer orgId, Integer orgUnitId)
			throws ApplicationException {
		try {
			return iMicrobiologyTestMasterDao.getMicrobiologyTestMasterById(microTestId, orgId, orgUnitId);
		}catch (Exception e) {
			throw e;
		}
	}
		
	@Override
	public Response getMicrobiologyTestMasterCount(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try {
			return iMicrobiologyTestMasterDao.getMicrobiologyTestMasterCount(microbiologyTestMasterDto);
		}catch (Exception e) {
			throw e;
		}
	}
		

	@Override
	public Response getActiveMicrobiologyTestMasterList(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try {
			return iMicrobiologyTestMasterDao.getActiveMicrobiologyTestMasterList(microbiologyTestMasterDto);
		}catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getMicrobiologyTestMasterByName(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try {
			return iMicrobiologyTestMasterDao.getMicrobiologyTestMasterByName(microbiologyTestMasterDto);
		}catch (Exception e) {
			throw e;
		}
	}
	@Override
	public Response getMicrobiologyTestMasterByNameNotId(MicrobiologyTestMasterDto microbiologyTestMasterDto)
			throws ApplicationException {
		try {
			return iMicrobiologyTestMasterDao.getMicrobiologyTestMasterByNameNotId(microbiologyTestMasterDto);
		}catch (Exception e) {
			throw e;
		}
	}
		

}
