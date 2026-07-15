package com.param.global.otims.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.IssueAcceptanceDto;
import com.param.global.otims.dao.IImplantDetailsDao;
import com.param.global.otims.dto.ImplantDetailsDto;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked"})
@Service
public class ImplantDetailsServiceImpl implements IImplantDetailsService, ICommonConstants{
	final static Logger logger = Logger.getLogger(ImplantDetailsServiceImpl.class);


	@Autowired
	private IImplantDetailsDao iImplantDetailsDao;
	
	@Override
	public Response saveImplantdetails( List<ImplantDetailsDto> implantDetailsDto) throws ApplicationException {
		try{
			return iImplantDetailsDao.saveImplantdetails(implantDetailsDto,0,0);
			
		} catch(Exception e){
		logger.error("saveImplantdetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getImplantdetailsList(ImplantDetailsDto implantDetailsDto) {
		try{
			return iImplantDetailsDao.getImplantdetailsList(implantDetailsDto);
			
		} catch(Exception e){
		logger.error("getImplantdetailsList failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getImplantdetails(ImplantDetailsDto implantDetailsDto) throws ApplicationException {
		try{
			return iImplantDetailsDao.getImplantdetails(implantDetailsDto);
			
		} catch(Exception e){
		logger.error("getImplantdetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getKitdetails(ImplantDetailsDto implantDetailsDto) throws ApplicationException {
		try{
			return iImplantDetailsDao.getKitdetails(implantDetailsDto);
			
		} catch(Exception e){
		logger.error("getKitdetails failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchImplantItems(ImplantDetailsDto implantDetailsDto) throws ApplicationException {
		try{
			return iImplantDetailsDao.searchImplantItems(implantDetailsDto);
			
		} catch(Exception e){
		logger.error("searchImplantItems failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getImplantDetailsByProcedureId(ImplantDetailsDto implantDetailsDto) throws ApplicationException {
		try{
			return iImplantDetailsDao.getImplantDetailsByProcedureId(implantDetailsDto,0);
			
		} catch(Exception e){
		logger.error("getImplantDetailsByProcedureId failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response searchImplantKit(ImplantDetailsDto implantDetailsDto) throws ApplicationException {
		try{
			return iImplantDetailsDao.searchImplantKit(implantDetailsDto);
			
		} catch(Exception e){
		logger.error("searchImplantKit failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getImplantIndentAcceptanceData(IssueAcceptanceDto issueAcceptanceDto) throws ApplicationException {
		try {
			return iImplantDetailsDao.getImplantIndentAcceptanceData(issueAcceptanceDto);

		} catch (Exception e) {
		logger.error("getImplantIndentAcceptanceData failed", e);
		}
		return new Response<>("error", null, null, null, null);
	}

	@Override
	public Response updateImplantDetailsIsExported(List<ImplantDetailsDto> list) throws ApplicationException {
		try{
			return iImplantDetailsDao.updateImplantDetailsIsExported(list);
			
		} catch(Exception e){
		logger.error("updateImplantDetailsIsExported failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getImplantDetailsByProcedureIdOnOTIMS(ImplantDetailsDto implantDetailsDto)
			throws ApplicationException {
		try{
			return iImplantDetailsDao.getImplantDetailsByProcedureIdOnOTIMS(implantDetailsDto);
			
		} catch(Exception e){
		logger.error("getImplantDetailsByProcedureIdOnOTIMS failed", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
}
