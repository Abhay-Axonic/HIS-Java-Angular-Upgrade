package com.param.lis.global.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.lis.global.SensitivityMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dao.ISensitivityTestResultMasterDao;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.SensitivityMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class SensitivityTestResultMasterServiceImpl implements ISensitivityTestResultMasterService,ICommonConstants, IError{

	@Autowired
	private ISensitivityTestResultMasterDao iSensitivityTestResultMasterDao;
	
	@Override
	@Transactional
	public Response addSensitivityTestResultMaster(SensitivityMasterDto sensitivityTestResultMasterDto)throws ApplicationException {
		try{
			Response res = iSensitivityTestResultMasterDao.checkSensitivityTestResultMaster(sensitivityTestResultMasterDto);
			
			Response response = iSensitivityTestResultMasterDao.checkSensitivityTestResultMasterDesc(sensitivityTestResultMasterDto);
			
			if(res.getListObject() != null && res.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,SENSITIVITY_CODE_EXISIS, null, null);
			}else if(response.getListObject() != null && response.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,SENSITIVITY_NAME_EXISIS, null, null);
			}else{
				long time = TimeZoneComponent.getDateByzone(0).getTime();
				sensitivityTestResultMasterDto.setCreatedDate(time);
				sensitivityTestResultMasterDto.setUpdatedDate(time);
				iSensitivityTestResultMasterDao.addSensitivityTestResultMaster(sensitivityTestResultMasterDto);
				return new Response<>(SUCCESS, SUCCESS_200,SENSITIVITY_ADD_SUCC, null, null);
			}
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSensitivityTestResultMasterById(Integer orgId, Integer reagentId)
			throws ApplicationException {
		try
		{
			return iSensitivityTestResultMasterDao.getSensitivityTestResultMasterById(orgId, reagentId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response ActivateInactivateSensitivityTestResultMaster(Integer orgId,
			Integer reagentId, Character reagentStatus)
			throws ApplicationException {
		try
		{
			Response resp= iSensitivityTestResultMasterDao.ActivateInactivateSensitivityTestResultMaster(orgId, reagentId, reagentStatus);
			SensitivityMaster sensitivityTestResultMst = resp.getObject() != null ? (SensitivityMaster)resp.getObject() : null;
			if(sensitivityTestResultMst != null){
				if (sensitivityTestResultMst.getStatus() == ACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200,SENSITIVITY_ACTIVATE_SUCC, null, null);
				} else if (sensitivityTestResultMst.getStatus()== INACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200,SENSITIVITY_INACTIVATE_SUCC, null, null);
				} else
				{
					if (reagentStatus == ACTIVE)
					{
						return new Response(SUCCESS, SUCCESS_200,SENSITIVITY_ACTIVATE_FAIL, null, null);
					} else
					{
						return new Response(SUCCESS, SUCCESS_200,SENSITIVITY_INACTIVATE_FAIL, null, null);
					}
				}
			}
		} catch (Exception e)
		{
			
			throw e;
		}
		return null;
	}

	@Override
	@Transactional
	public Response listSensitivityTestResultMaster(Integer orgId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		try
		{
			Response res =  iSensitivityTestResultMasterDao.listSensitivityTestResultMaster(orgId, offset, recordPerPage);
			if(res.getListObject() != null && res.getListObject().size() > 0){
				List<GlobalCommonDto> unitMst = res.getListObject() != null ? (List<GlobalCommonDto>)res.getListObject() : null;
				if(unitMst.isEmpty())
				{
					return new Response(ERROR, ERR_500, SAMPLE_RECORDS_NOT_FOUND, null, null);
				}
				else{
					return new Response(SUCCESS, SUCCESS_200, null, unitMst, null);
				}
			}
		} catch (Exception e)
		{
			
			throw e;
		}
		return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getToTalSensitivityTestResultMaster(Integer orgId)
			throws ApplicationException {
		try
		{
			return iSensitivityTestResultMasterDao.getToTalSensitivityTestResultMaster(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateSensitivityTestResultMaster(SensitivityMasterDto sensitivityTestResultMasterDto)
			throws ApplicationException {

		try{
			Response res = iSensitivityTestResultMasterDao.updateCheckSensitivityTestResultCodeAvaiable(sensitivityTestResultMasterDto);
			
			Response response = iSensitivityTestResultMasterDao.updateCheckSensitivityTestResultDescAvaiable(sensitivityTestResultMasterDto);
			
			if(res.getListObject() != null && res.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,SENSITIVITY_CODE_EXISIS, null, null);
			}else if(response.getListObject() != null && response.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,SENSITIVITY_NAME_EXISIS, null, null);
			}else{
				sensitivityTestResultMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(0).getTime());
				Response resdata =  iSensitivityTestResultMasterDao.updateSensitivityTestResultMaster(sensitivityTestResultMasterDto);
				if (resdata.getStatus().equalsIgnoreCase(SUCCESS))
				{
					return new Response(SUCCESS, SUCCESS_200,SENSITIVITY_UPDATE_SUCC, null, null);
				} else
				{
					return new Response(ERROR, ERR_500,SENSITIVITY_UPDATE_FAIL, null, null);
				}
			}
		}catch(Exception e){
			throw e;
		}
	}


}
