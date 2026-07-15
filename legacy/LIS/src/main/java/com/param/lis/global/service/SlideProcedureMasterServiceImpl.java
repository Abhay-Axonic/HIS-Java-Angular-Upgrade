package com.param.lis.global.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.lis.global.SlideProcedureMaster;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dao.ISliderProcedureMasterDao;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.lis.global.dto.SlideProcedureMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class SlideProcedureMasterServiceImpl implements ISlideProcedureMasterService,ICommonConstants, IError{

	@Autowired
	private ISliderProcedureMasterDao iSliderProcedureMasterDao;
	
	@Override
	@Transactional
	public Response addSlideProcedureMaster(SlideProcedureMasterDto slideProcedureMasterDto)throws ApplicationException {
		try{
			Response res = iSliderProcedureMasterDao.checkSlideProcedureMaster(slideProcedureMasterDto);
			
			Response response = iSliderProcedureMasterDao.checkSlideProcedureMasterDesc(slideProcedureMasterDto);
			
			if(res.getListObject() != null && res.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,SLIDEPROCEDURE_CODE_EXISIS, null, null);
			}else if(response.getListObject() != null && response.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,SLIDEPROCEDURE_NAME_EXISIS, null, null);
			}else{
				long time = TimeZoneComponent.getDateByzone(0).getTime();
				slideProcedureMasterDto.setCreatedDate(time);
				slideProcedureMasterDto.setUpdatedDate(time);
				iSliderProcedureMasterDao.addSlideProcedureMaster(slideProcedureMasterDto);
				return new Response<>(SUCCESS, SUCCESS_200,SLIDEPROCEDURE_ADD_SUCC, null, null);
			}
		}catch(Exception e){ 
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getSlideProcedureMasterById(Integer orgId, Integer reagentId)
			throws ApplicationException {
		try
		{
			return iSliderProcedureMasterDao.getSlideProcedureMasterById(orgId, reagentId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response ActivateInactivateSlideProcedureMaster(Integer orgId,
			Integer reagentId, Character reagentStatus)
			throws ApplicationException {
		try
		{
			Response resp= iSliderProcedureMasterDao.ActivateInactivateSlideProcedureMaster(orgId, reagentId, reagentStatus);
			SlideProcedureMaster slideProcedureMst = resp.getObject() != null ? (SlideProcedureMaster)resp.getObject() : null;
			if(slideProcedureMst != null){
				if (slideProcedureMst.getStatus() == ACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200,SLIDEPROCEDURE_ACTIVATE_SUCC, null, null);
				} else if (slideProcedureMst.getStatus()== INACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200,SLIDEPROCEDURE_INACTIVATE_SUCC, null, null);
				} else
				{
					if (reagentStatus == ACTIVE)
					{
						return new Response(SUCCESS, SUCCESS_200,SLIDEPROCEDURE_ACTIVATE_FAIL, null, null);
					} else
					{
						return new Response(SUCCESS, SUCCESS_200,SLIDEPROCEDURE_INACTIVATE_FAIL, null, null);
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
	public Response listSlideProcedureMaster(Integer orgId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		try
		{
			Response res =  iSliderProcedureMasterDao.listSlideProcedureMaster(orgId, offset, recordPerPage);
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
	public Response getToTalSlideProcedureMaster(Integer orgId)
			throws ApplicationException {
		try
		{
			return iSliderProcedureMasterDao.getToTalSlideProcedureMaster(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateSlideProcedureMaster(SlideProcedureMasterDto slideProcedureMasterDto)
			throws ApplicationException {
		try{
			Response res = iSliderProcedureMasterDao.updateCheckSlideProcedureMaster(slideProcedureMasterDto);
			
			Response response = iSliderProcedureMasterDao.updateCheckSlideProcedureMasterDesc(slideProcedureMasterDto);
			
			if(res.getListObject() != null && res.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,SLIDEPROCEDURE_CODE_EXISIS, null, null);
			}else if(response.getListObject() != null && response.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,SLIDEPROCEDURE_NAME_EXISIS, null, null);
			}else{
				slideProcedureMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(0).getTime());
				Response ress =  iSliderProcedureMasterDao.updateSlideProcedureMaster(slideProcedureMasterDto);
				if (ress.getStatus().equalsIgnoreCase(SUCCESS))
				{
					return new Response(SUCCESS, SUCCESS_200,SLIDEPROCEDURE_UPDATE_SUCC, null, null);
				} else
				{
					return new Response(ERROR, ERR_500,SLIDEPROCEDURE_UPDATE_FAIL, null, null);
				}
			}
		}catch(Exception e){
			throw e;
		}
	
	}


}
