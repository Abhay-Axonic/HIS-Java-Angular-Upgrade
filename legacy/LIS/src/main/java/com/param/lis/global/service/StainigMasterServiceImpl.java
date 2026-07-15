package com.param.lis.global.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.lis.global.StainigMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dao.IStainigMasterDao;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.StainigMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class StainigMasterServiceImpl implements IStainigMasterService,ICommonConstants, IError{

	@Autowired
	private IStainigMasterDao  iIStainigMasterDao;
	
	@Override
	@Transactional
	public Response addStainigMaster(StainigMasterDto stainigMasterDto)throws ApplicationException {
		try{
			Response res = iIStainigMasterDao.checkStainigMaster(stainigMasterDto);
			
			Response res1 = iIStainigMasterDao.checkStainigMasterDesc(stainigMasterDto);
			
			if(res.getListObject() != null && res.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,STAINIG_CODE_EXISIS, null, null);
			}else if(res1.getListObject() != null && res1.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,STAINIG_NAME_EXISIS, null, null);
			}else{
				long time = TimeZoneComponent.getDateByzone(0).getTime();
				stainigMasterDto.setCreatedDate(time);
				stainigMasterDto.setUpdatedDate(time);
				iIStainigMasterDao.addStainigMaster(stainigMasterDto);
				return new Response<>(SUCCESS, SUCCESS_200,STAINIG_ADD_SUCC, null, null);
			}
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getStainigMasterMasterById(Integer orgId, Integer reagentId)
			throws ApplicationException {
		try
		{
			return iIStainigMasterDao.getStainigMasterById(orgId, reagentId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response ActivateInactivateStainigMaster(Integer orgId,
			Integer stainigId, Character stainigStatus)
			throws ApplicationException {
		try
		{
			Response resp= iIStainigMasterDao.ActivateInactivateStainigMaster(orgId, stainigId, stainigStatus);
			StainigMaster stainigMst = resp.getObject() != null ? (StainigMaster)resp.getObject() : null;
			if(stainigMst != null){
				if (stainigMst.getStatus() == ACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200,STAINIG_ACTIVATE_SUCC, null, null);
				} else if (stainigMst.getStatus()== INACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200,STAINIG_INACTIVATE_SUCC, null, null);
				} else
				{
					if (stainigId == ACTIVE)
					{
						return new Response(SUCCESS, SUCCESS_200,STAINIG_ACTIVATE_FAIL, null, null);
					} else
					{
						return new Response(SUCCESS, SUCCESS_200,STAINIG_INACTIVATE_FAIL, null, null);
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
	public Response listStainigMaster(Integer orgId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		try
		{
			Response res =  iIStainigMasterDao.listStainigMaster(orgId, offset, recordPerPage);
			if(res.getListObject() != null && res.getListObject().size() > 0){
				List<GlobalCommonDto> orgMst = res.getListObject() != null ? (List<GlobalCommonDto>)res.getListObject() : null;
				if(orgMst.isEmpty())
				{
					return new Response(ERROR, ERR_500, STAINIG_NOT_FOUND, null, null);
				}
				else{
					return new Response(SUCCESS, SUCCESS_200, null, orgMst, null);
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
	public Response getToTalStainigMaster(Integer orgId)
			throws ApplicationException {
		try
		{
			return iIStainigMasterDao.getToTalStainigMaster(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateStainigMaster(StainigMasterDto stainigMasterDto)
			throws ApplicationException {
		
		try{
			Response res = iIStainigMasterDao.updateCheckStainigMasterCodeAvaiable(stainigMasterDto);
			
			Response res1 = iIStainigMasterDao.updateCheckStainigMasterDescAvaiable(stainigMasterDto);
			
			if(res.getListObject() != null && res.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,STAINIG_CODE_EXISIS, null, null);
			}else if(res1.getListObject() != null && res1.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,STAINIG_NAME_EXISIS, null, null);
			}else{
				stainigMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(0).getTime());
				Response resdata =  iIStainigMasterDao.updateStainigMaster(stainigMasterDto);
				if (resdata.getStatus().equalsIgnoreCase(SUCCESS))
				{
					return new Response(SUCCESS, SUCCESS_200,STAINIG_UPDATE_SUCC, null, null);
				} else
				{
					return new Response(ERROR, ERR_500,STAINIG_UPDATE_FAIL, null, null);
				}
			}
		}catch(Exception e){
			throw e;
		}
	
	}



}
