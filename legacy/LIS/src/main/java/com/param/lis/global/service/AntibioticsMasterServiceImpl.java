package com.param.lis.global.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.entity.lis.global.AntibioticClassMaster;
import com.param.global.common.ServiceException;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dao.IAntibioticsMasterDao;
import com.param.lis.global.dto.AntibioticClassMasterDto;
import com.param.lis.global.dto.GlobalCommonDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class AntibioticsMasterServiceImpl implements IAntibioticsMasterService,ICommonConstants, IError{

	private static final Logger logger = Logger.getLogger(AntibioticsMasterServiceImpl.class);

	@Autowired
	private IAntibioticsMasterDao iAntibioticsMasterDao;
	
	@Override
	@Transactional
	public Response addAntibioticsMaster(AntibioticClassMasterDto antibioticClassMasterDto)throws ApplicationException {
		try{
			Response res = iAntibioticsMasterDao.checkAntibioticsMaster(antibioticClassMasterDto);
			
			Response res1 = iAntibioticsMasterDao.checkForUniqueAntibioticsMasterDesc(antibioticClassMasterDto);
			
			if(res.getListObject() != null && res.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,ANTIBIOTICS_CODE_EXISIS, null, null);
			}else if(res1.getListObject() != null && res1.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,ANTIBIOTICS_DESCRIPTION_EXISIS, null, null);
			}else{
				long time = TimeZoneComponent.getDateByzone(0).getTime();
				antibioticClassMasterDto.setCreatedDate(time);
				antibioticClassMasterDto.setUpdatedDate(time);
				iAntibioticsMasterDao.addAntibioticsMaster(antibioticClassMasterDto);
				return new Response<>(SUCCESS, SUCCESS_200,ANTIBIOTICS_ADD_SUCC, null, null);
			}
		}catch(Exception e){
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getAntibioticsMasterById(Integer orgId, Integer reagentId)
			throws ApplicationException {
		try
		{
			return iAntibioticsMasterDao.getAntibioticsMasterById(orgId, reagentId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response ActivateInactivateAntibioticsMaster(Integer orgId,
			Integer reagentId, Character reagentStatus)
			throws ApplicationException {
		try
		{
			Response resp= iAntibioticsMasterDao.ActivateInactivateAntibioticsMaster(orgId, reagentId, reagentStatus);
			AntibioticClassMaster antibioticMst = resp.getObject() != null ? (AntibioticClassMaster)resp.getObject() : null;
			if(antibioticMst != null){
				if (antibioticMst.getStatus() == ACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200,ANTIBIOTICS_ACTIVATE_SUCC, null, null);
				} else if (antibioticMst.getStatus()== INACTIVE)
				{
					return new Response(SUCCESS, SUCCESS_200,ANTIBIOTICS_INACTIVATE_SUCC, null, null);
				} else
				{
					if (reagentStatus == ACTIVE)
					{
						return new Response(SUCCESS, SUCCESS_200,ANTIBIOTICS_ACTIVATE_FAIL, null, null);
					} else
					{
						return new Response(SUCCESS, SUCCESS_200,ANTIBIOTICS_INACTIVATE_FAIL, null, null);
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
	public Response listAntibioticsMaster(Integer orgId, Integer offset,
			Integer recordPerPage) throws ApplicationException {
		try
		{
			Response res =  iAntibioticsMasterDao.listAntibioticsMaster(orgId, offset, recordPerPage);
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
		return new Response(ERROR, ERR_500, SAMPLE_RECORDS_NOT_FOUND, null, null);
	}

	@Override
	@Transactional
	public Response getToTalAntibioticsMaster(Integer orgId)
			throws ApplicationException {
		try
		{
			return iAntibioticsMasterDao.getToTalAntibioticsMaster(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateAntibioticsMaster(AntibioticClassMasterDto antibioticClassMasterDto)
			throws ApplicationException {
		
		try{
			Response res = iAntibioticsMasterDao.updateCheckAntibioticsMasterCodeAvaiable(antibioticClassMasterDto);
			
			Response res1 = iAntibioticsMasterDao.updateCheckAntibioticsMasterDescriptionAvaiable(antibioticClassMasterDto);
			
			if(res.getListObject() != null && res.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,ANTIBIOTICS_CODE_EXISIS, null, null);
			}else if(res1.getListObject() != null && res1.getListObject().size() > 0){
				return new Response<>(ERROR, ERR_500,ANTIBIOTICS_DESCRIPTION_EXISIS, null, null);
			}else{
				antibioticClassMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(0).getTime());
				Response resdata =  iAntibioticsMasterDao.updateAntibioticsMaster(antibioticClassMasterDto);
				if (resdata.getStatus().equalsIgnoreCase(SUCCESS))
				{
					return new Response(SUCCESS, SUCCESS_200,ANTIBIOTICS_UPDATE_SUCC, null, null);
				} else
				{
					return new Response(ERROR, ERR_500,ANTIBIOTICS_UPDATE_FAIL, null, null);
				}
			}
		}catch(Exception e){
			logger.error("Exception in updateAntibioticsMaster", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	@Transactional
	public Response getListAntibioticsMaster(Integer orgId) throws ApplicationException {
		Response<AntibioticClassMasterDto, Integer> response = null;
		try
		{
			response = iAntibioticsMasterDao.getListAntibioticsMaster(orgId);
			return response;
		} catch (Exception e)
		{
			
			throw e;
		}
	}


}
