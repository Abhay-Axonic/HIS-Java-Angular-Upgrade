package com.param.lis.global.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.lis.global.ReagentMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dao.IReagentMasterDao;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.ReagentMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class ReagentMasterServiceImpl implements IReagentMasterService,ICommonConstants, IError{

	@Autowired
	private IReagentMasterDao iReagentMasterDao;
	
	@Override
	@Transactional
	public Response addReagentMaster(ReagentMasterDto reagentMasterDto)throws ApplicationException {
		try{
			Response res = iReagentMasterDao.checkReagentCodeAvaiable(reagentMasterDto);
			
			Response res1 = iReagentMasterDao.checkReagentDescriptionAvaiable(reagentMasterDto);
			
			if(res.getListObject() != null && res.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,REAGENT_CODE_EXISIS, null, null);
			}else if(res1.getListObject() != null && res1.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,REAGENT_DESCRIPTION_EXISIS, null, null);
			} else{
				long time = TimeZoneComponent.getDateByzone(0).getTime();
				reagentMasterDto.setCreatedDate(time);
				reagentMasterDto.setUpdatedDate(time);
				iReagentMasterDao.addReagentMaster(reagentMasterDto);
				return new Response<>(SUCCESS, SUCCESS_200,REAGENT_ADD_SUCC, null, null);
			}
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getReagentMasterById(Integer orgId, Integer reagentId)
			throws ApplicationException {
		try
		{
			return iReagentMasterDao.getReagentMasterById(orgId, reagentId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response ActivateInactivateReagentMaster(Integer orgId,
			Integer reagentId, Character reagentStatus)
			throws ApplicationException {
		try
		{
			Response resp= iReagentMasterDao.ActivateInactivateReagentMaster(orgId, reagentId, reagentStatus);
			ReagentMaster reagentMst = resp.getObject() != null ? (ReagentMaster)resp.getObject() : null;
			if(reagentMst != null){
				if (reagentMst.getStatus() == ACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200,REAGENT_ACTIVATE_SUCC, null, null);
				} else if (reagentMst.getStatus()== INACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200,REAGENT_INACTIVATE_SUCC, null, null);
				} else
				{
					if (reagentStatus == ACTIVE)
					{
						return new Response(SUCCESS, SUCCESS_200,REAGENT_ACTIVATE_FAIL, null, null);
					} else
					{
						return new Response(SUCCESS, SUCCESS_200,REAGENT_INACTIVATE_FAIL, null, null);
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
	public Response listReagentMaster(Integer orgId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		try
		{
			Response res =  iReagentMasterDao.listReagentMaster(orgId, offset, recordPerPage);
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
	public Response getToTalReagentMasterRecord(Integer orgId)
			throws ApplicationException {
		try
		{
			return iReagentMasterDao.getToTalReagentMasterRecord(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateReagentMaster(ReagentMasterDto reagentMasterDto)
			throws ApplicationException {
		
		try{
			Response res = iReagentMasterDao.updateCheckReagentCodeAvaiable(reagentMasterDto);
			
			Response res1 = iReagentMasterDao.updateCheckReagentDescriptionAvaiable(reagentMasterDto);
			
			if(res.getListObject() != null && res.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,REAGENT_CODE_EXISIS, null, null);
			}else if(res1.getListObject() != null && res1.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,REAGENT_DESCRIPTION_EXISIS, null, null);
			}else{
				reagentMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(0).getTime());
				Response resdata =  iReagentMasterDao.updateReagentMaster(reagentMasterDto);
				if (resdata.getStatus().equalsIgnoreCase(SUCCESS))
				{
					return new Response(SUCCESS, SUCCESS_200,REAGENT_UPDATE_SUCC, null, null);
				} else
				{
					return new Response(ERROR, ERR_500,REAGENT_UPDATE_FAIL, null, null);
				}
			}
		}catch(Exception e){
			throw e;
		}
	}


}
