package com.param.lis.global.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.lis.global.MicrobiologyStainMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dao.IMicrobiologyStainigMasterDao;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.MicrobiologyStainMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class MicrobiologyStainigMasterServiceImpl implements IMicrobiologyStainigMasterService,ICommonConstants, IError{

	@Autowired
	private IMicrobiologyStainigMasterDao  iIStainigMasterDao;
	
	@Override
	@Transactional
	public Response addMicrobStainigMaster(MicrobiologyStainMasterDto microbstainigMasterDto)throws ApplicationException {
		try{
			Response res = iIStainigMasterDao.checkMicrobStainigMaster(microbstainigMasterDto);
			
			Response res1 = iIStainigMasterDao.checkMicrobStainigMasterDesc(microbstainigMasterDto);
			
			if(res.getListObject() != null && res.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,MICRO_STAINIG_CODE_EXISIS, null, null);
			}else if(res1.getListObject() != null && res1.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,MICRO_STAINIG_DESC_EXISIS, null, null);
			}else{
				long time = TimeZoneComponent.getDateByzone(0).getTime();
				microbstainigMasterDto.setCreatedDate(time);
				microbstainigMasterDto.setUpdatedDate(time);
				iIStainigMasterDao.addMicrobStainigMaster(microbstainigMasterDto);
				return new Response<>(SUCCESS, SUCCESS_200,MICRO_STAINIG_ADD_SUCC, null, null);
			}
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getMicrobStainigMasterMasterById(Integer orgId, Integer microStainId)
			throws ApplicationException {
		try
		{
			return iIStainigMasterDao.getMicrobStainigMasterById(orgId, microStainId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response ActivateInactivateMicrobStainigMaster(Integer orgId,
			Integer microStainId, Character stainigStatus)
			throws ApplicationException {
		try
		{
			Response resp= iIStainigMasterDao.ActivateInactivateMicrobStainigMaster(orgId, microStainId, stainigStatus);
			MicrobiologyStainMaster stainigMst = resp.getObject() != null ? (MicrobiologyStainMaster)resp.getObject() : null;
			if(stainigMst != null){
				if (stainigMst.getStatus() == ACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200,MICRO_STAINIG_ACTIVATE_SUCC, null, null);
				} else if (stainigMst.getStatus()== INACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200,MICRO_STAINIG_INACTIVATE_SUCC, null, null);
				} else
				{
					if (microStainId == ACTIVE)
					{
						return new Response(SUCCESS, SUCCESS_200,MICRO_STAINIG_ACTIVATE_FAIL, null, null);
					} else
					{
						return new Response(SUCCESS, SUCCESS_200,MICRO_STAINIG_INACTIVATE_FAIL, null, null);
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
	public Response listMicrobStainigMaster(Integer orgId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		try
		{
			Response res =  iIStainigMasterDao.listMicrobStainigMaster(orgId, offset, recordPerPage);
			if(res.getListObject() != null && res.getListObject().size() > 0){
				List<GlobalCommonDto> orgMst = res.getListObject() != null ? (List<GlobalCommonDto>)res.getListObject() : null;
				if(orgMst.isEmpty())
				{
					return new Response(ERROR, ERR_500, MICRO_STAINIG_NOT_FOUND, null, null);
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
	public Response getToTalMicrobStainigMaster(Integer orgId)
			throws ApplicationException {
		try
		{
			return iIStainigMasterDao.getToTalMicrobStainigMaster(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateMicrobStainigMaster(MicrobiologyStainMasterDto microbiologyStainMasterDto)
			throws ApplicationException {
		
		try{
			Response res = iIStainigMasterDao.updateCheckMicrobStainigMasterCodeAvaiable(microbiologyStainMasterDto);
			
			Response res1 = iIStainigMasterDao.updateCheckMicrobStainigMasterDescAvaiable(microbiologyStainMasterDto);
			
			if(res.getListObject() != null && res.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,MICRO_STAINIG_CODE_EXISIS, null, null);
			}else if(res1.getListObject() != null && res1.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,MICRO_STAINIG_DESC_EXISIS, null, null);
			}else{
				microbiologyStainMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(0).getTime());
				Response resdata =  iIStainigMasterDao.updateMicrobStainigMaster(microbiologyStainMasterDto);
				if (resdata.getStatus().equalsIgnoreCase(SUCCESS))
				{
					return new Response(SUCCESS, SUCCESS_200,MICRO_STAINIG_UPDATE_SUCC, null, null);
				} else
				{
					return new Response(ERROR, ERR_500,MICRO_STAINIG_UPDATE_FAIL, null, null);
				}
			}
		}catch(Exception e){
			throw e;
		}
	
	}



}
