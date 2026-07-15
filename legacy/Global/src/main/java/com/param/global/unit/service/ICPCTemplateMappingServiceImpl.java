package com.param.global.unit.service;

import java.util.Iterator;
import java.util.ListIterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.IICPCTemplateMappingDao;
import com.param.global.unit.dto.ICPCTemplateMappingDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes", "unchecked"})
public class ICPCTemplateMappingServiceImpl implements IICPCTemplateMappingService, ICommonConstants, IError{

	final static Logger logger = Logger.getLogger(ICPCTemplateMappingServiceImpl.class);

 @Autowired
 private IICPCTemplateMappingDao iICPCTemplateMappingDao;
	@Override
	public Response saveICPCTemplateMapping(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException {
		try {
				Response response=iICPCTemplateMappingDao.checkICPCTemplateMappingAvailable(iCPCTemplateMappingDto);
				if(response.getListObject().size()>0 && response.getListObject()!=null){
					
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}else{
					ICPCTemplateMappingDto dto=new ICPCTemplateMappingDto();
					dto.setTemplateId(iCPCTemplateMappingDto.getTemplateId());
					dto.setIsDeleted(iCPCTemplateMappingDto.getIsDeleted());
					dto.setOrgUnitId(iCPCTemplateMappingDto.getOrgUnitId());
					dto.setOrgId(iCPCTemplateMappingDto.getOrgId());
					dto.setCreatedBy(iCPCTemplateMappingDto.getCreatedBy());
					dto.setUpdatedBy(iCPCTemplateMappingDto.getUpdatedBy());
					dto.setCreatedDate(iCPCTemplateMappingDto.getCreatedDate());
					dto.setUpdatedDate(iCPCTemplateMappingDto.getUpdatedDate());
					dto.setStatus(iCPCTemplateMappingDto.getStatus());
				
					ListIterator<ICPCTemplateMappingDto> listICPC=iCPCTemplateMappingDto.getSelectedICPCList().listIterator();
					while(listICPC.hasNext()){
						ICPCTemplateMappingDto dtoICPC=listICPC.next();
						dto.setmIcpcId(dtoICPC.getId());
						dto.setmIcpcSystemId(dtoICPC.getmIcpcSystemId());
					
						iICPCTemplateMappingDao.saveICPCTemplateMapping(dto);
					
				}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	} catch (Exception e) {
		logger.error("saveICPCTemplateMapping failed", e);
	}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response updateICPCTemplateMapping(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try
		{
			if(!iCPCTemplateMappingDto.getTemplateId().equals(iCPCTemplateMappingDto.getPrevTemplateId())) {
			Response response=iICPCTemplateMappingDao.checkICPCTemplateMappingAvailable(iCPCTemplateMappingDto);
				if(response.getListObject().size()>0 && response.getListObject()!=null){
					return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				}
			}
			ICPCTemplateMappingDto dto=new ICPCTemplateMappingDto();
			//dto.setTemplateId(iCPCTemplateMappingDto.getTemplateId());
			dto.setTemplateId(iCPCTemplateMappingDto.getPrevTemplateId());
			dto.setIsDeleted('N');
			dto.setOrgUnitId(iCPCTemplateMappingDto.getOrgUnitId());
			dto.setOrgId(iCPCTemplateMappingDto.getOrgId());
			dto.setCreatedBy(iCPCTemplateMappingDto.getCreatedBy());
			dto.setUpdatedBy(iCPCTemplateMappingDto.getUpdatedBy());
			dto.setCreatedDate(iCPCTemplateMappingDto.getCreatedDate());
			dto.setUpdatedDate(iCPCTemplateMappingDto.getUpdatedDate());
			dto.setStatus(iCPCTemplateMappingDto.getStatus());
			iICPCTemplateMappingDao.updateICPCTemplateMapping(dto);
			dto.setTemplateId(iCPCTemplateMappingDto.getTemplateId());
			ListIterator<ICPCTemplateMappingDto> listICPC=iCPCTemplateMappingDto.getSelectedICPCList().listIterator();
			while(listICPC.hasNext()){
				ICPCTemplateMappingDto dtoICPC=listICPC.next();
				dto.setmIcpcId(dtoICPC.getId());
				dto.setmIcpcSystemId(dtoICPC.getmIcpcSystemId());
			
				iICPCTemplateMappingDao.saveICPCTemplateMapping(dto);
			
		}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (Exception e)
		{
			logger.error("updateICPCTemplateMapping failed", e);

			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	
	@Override
	public Response getICPCTemplateMappingById(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try
		{
			return iICPCTemplateMappingDao.getICPCTemplateMappingById(iCPCTemplateMappingDto);
		} catch (Exception e)
		{
			logger.error("getICPCTemplateMappingById failed", e);

			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	@Override
	public Response updateICPCTemplateMappingStatus(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try
		{
			return iICPCTemplateMappingDao.updateICPCTemplateMappingStatus(iCPCTemplateMappingDto);
		} catch (Exception e)
		{
			logger.error("updateICPCTemplateMappingStatus failed", e);

			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	

	@Override
	public Response getICPCTemplateMappingList(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try
		{
			return iICPCTemplateMappingDao.getICPCTemplateMappingList(iCPCTemplateMappingDto);
		} catch (Exception e)
		{
			logger.error("getICPCTemplateMappingList failed", e);

			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	

	@Override
	public Response getListOfICPCSystems() throws ApplicationException {
		try
		{
			return iICPCTemplateMappingDao.getListOfICPCSystems();
		} catch (Exception e)
		{
			logger.error("getListOfICPCSystems failed", e);

			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}
	
	@Override
	public Response getDetailedListOfICPCSystems(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try
		{
			return iICPCTemplateMappingDao.getDetailedListOfICPCSystems(iCPCTemplateMappingDto);
		} catch (Exception e)
		{
			logger.error("getDetailedListOfICPCSystems failed", e);

			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getDetailedListOfICPCSystemsDescription(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try
		{
			return iICPCTemplateMappingDao.getDetailedListOfICPCSystemsDescription(iCPCTemplateMappingDto);
		} catch (Exception e)
		{
			logger.error("getDetailedListOfICPCSystemsDescription failed", e);

			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getICPCTemplateMappingCount(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try
		{
			return iICPCTemplateMappingDao.getICPCTemplateMappingCount(iCPCTemplateMappingDto);
		} catch (Exception e)
		{
			logger.error("getICPCTemplateMappingCount failed", e);

			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getICPCSystemsCodesBySystemId(ICPCTemplateMappingDto iCPCTemplateMappingDto)
			throws ApplicationException {
		try
		{
			return iICPCTemplateMappingDao.getICPCSystemsCodesBySystemId(iCPCTemplateMappingDto);
		} catch (Exception e)
		{
			logger.error("getICPCSystemsCodesBySystemId failed", e);

			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getTemplateListForICPC(ICPCTemplateMappingDto iCPCTemplateMappingDto) throws ApplicationException {
		try
		{
			return iICPCTemplateMappingDao.getTemplateListForICPC(iCPCTemplateMappingDto);
		} catch (Exception e)
		{
			logger.error("getTemplateListForICPC failed", e);

			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	
	
	

}
