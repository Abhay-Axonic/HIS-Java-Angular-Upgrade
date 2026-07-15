package com.param.adt.master.unit.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;

import com.param.adt.common.Response;
import com.param.adt.master.global.dto.VisitTypeMasterDto;
import com.param.adt.master.unit.dao.IBillingBedCategoryMasterDao;
import com.param.adt.master.unit.dao.ICorporateStatementChecklistMasterDao;
import com.param.adt.master.unit.dto.BillingBedCategoryMasterDto;
import com.param.adt.master.unit.dto.CorporateStatementChecklistMasterDto;
import com.param.adt.master.unit.model.BillingBedVisitTypeMapper;
import com.param.adt.master.unit.model.CorporateStatementChecklistVisitTypeMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;


@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class CorporateStatementChecklistMasterServiceImpl implements ICorporateStatementChecklistMasterService, ICommonConstants {

	@Autowired
	ICorporateStatementChecklistMasterDao iCorporateStatementChecklistMasterDao;
	
	
	@Override
	public Response getActiveCorporateStatementChecklistMaster(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		try {
			return iCorporateStatementChecklistMasterDao.getActiveCorporateStatementChecklistMaster(corporateStatementChecklistMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response saveCorporateStatementChecklistMaster( CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		Integer savCount=0;
		Response response = iCorporateStatementChecklistMasterDao.getCorporateStatementChecklistByName(corporateStatementChecklistMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				Response res = iCorporateStatementChecklistMasterDao.saveCorporateStatementChecklistMaster(corporateStatementChecklistMasterDto);
				Integer corporateStatementChecklistId=(Integer)res.getObject();
				ListIterator<VisitTypeMasterDto> iterator = corporateStatementChecklistMasterDto.getVisitTypeMasterDtosList().listIterator();
				while(iterator.hasNext())
				{
					VisitTypeMasterDto masterDto = iterator.next();
					CorporateStatementChecklistVisitTypeMapper corporateStatementChecklistVisitTypeMapper = new CorporateStatementChecklistVisitTypeMapper();
					corporateStatementChecklistVisitTypeMapper.setCorporateStatementChecklistId(corporateStatementChecklistId);
					corporateStatementChecklistVisitTypeMapper.setVisitTypeId(masterDto.getId());
//					corporateStatementChecklistVisitTypeMapper.setCreatedBy(corporateStatementChecklistMasterDto.getCreatedBy());
					corporateStatementChecklistVisitTypeMapper.setCreatedDate(GlobalCommonDateUtils.getDate(corporateStatementChecklistMasterDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
//					corporateStatementChecklistVisitTypeMapper.setUpdatedBy(corporateStatementChecklistMasterDto.getUpdatedBy());
					corporateStatementChecklistVisitTypeMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(corporateStatementChecklistMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
					corporateStatementChecklistVisitTypeMapper.setStatus('A');
					corporateStatementChecklistVisitTypeMapper.setOrganizationId(corporateStatementChecklistMasterDto.getOrganizationId());
					corporateStatementChecklistVisitTypeMapper.setUnitId(corporateStatementChecklistMasterDto.getUnitId());
					Integer i = iCorporateStatementChecklistMasterDao.saveCorporateStatementChecklistMasterVisitTypeMapper(corporateStatementChecklistVisitTypeMapper);
					if(i>0)
						savCount++;
				}
				if(savCount.equals(corporateStatementChecklistMasterDto.getVisitTypeMasterDtosList().size()))
				{
					corporateStatementChecklistMasterDto.setCorporateStatementChecklistId(corporateStatementChecklistId);
					Request request = new Request();
					Payload payload = new Payload();
					payload.setActionType("create");
					payload.setData(corporateStatementChecklistMasterDto);
					request.setKey(MasterKeyConstants.Type_Of_Stay.toString());
					request.setPayload(payload);
					//iOtimsService.publish(request);
					
					return new Response (SUCCESS,SUCCESS_CODE,COMMON_SUCCESS,null,null);
				}
			}
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	@Transactional
	public Response getCorporateStatementChecklistMasterList(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
			
			List<CorporateStatementChecklistMasterDto> finalList = new LinkedList<>();
			Response res = iCorporateStatementChecklistMasterDao.getCorporateStatementChecklistMasterList(corporateStatementChecklistMasterDto);
			ListIterator<CorporateStatementChecklistMasterDto> iterator = (ListIterator<CorporateStatementChecklistMasterDto>) res.getListObject().listIterator();
			while(iterator.hasNext())
			{
				CorporateStatementChecklistMasterDto corporateStatementMasterDto = iterator.next();
				corporateStatementMasterDto.setOrganizationId(corporateStatementChecklistMasterDto.getOrganizationId());
				corporateStatementMasterDto.setUnitId(corporateStatementChecklistMasterDto.getUnitId());
				corporateStatementMasterDto.setVisitTypeMasterDtosList(iCorporateStatementChecklistMasterDao.getCorporateStatementChecklistVistList(corporateStatementMasterDto));
				
				finalList.add(corporateStatementMasterDto);
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);

	}

	@Override
	public Response getCorporateStatementChecklistMasterCount(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		try {
			return iCorporateStatementChecklistMasterDao.getCorporateStatementChecklistMasterCount(corporateStatementChecklistMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCorporateStatementChecklistMasterById(int corporateStatementChecklistId, int orgId, int unitId)
			throws ApplicationException {
		try {
			return iCorporateStatementChecklistMasterDao.getCorporateStatementChecklistMasterById(corporateStatementChecklistId, orgId, unitId);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCorporateStatementChecklistMaster(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		Response response = iCorporateStatementChecklistMasterDao.getCorporateStatementChecklistByNameNotById(corporateStatementChecklistMasterDto);

		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				iCorporateStatementChecklistMasterDao.updateCorporateStatementChecklistMaster(corporateStatementChecklistMasterDto);
				if(corporateStatementChecklistMasterDto.getRemovedVisitTypeList().size()>0)
				{
					ListIterator<VisitTypeMasterDto> iterator = corporateStatementChecklistMasterDto.getRemovedVisitTypeList().listIterator();
					while(iterator.hasNext())
					{
						VisitTypeMasterDto masterDto = iterator.next();
						CorporateStatementChecklistVisitTypeMapper corporateStatementChecklistVisitTypeMapper = new CorporateStatementChecklistVisitTypeMapper();
						corporateStatementChecklistVisitTypeMapper.setVisitTypeId(masterDto.getId());
						corporateStatementChecklistVisitTypeMapper.setCorporateStatementChecklistId(corporateStatementChecklistMasterDto.getCorporateStatementChecklistId());
//						corporateStatementChecklistVisitTypeMapper.setUpdatedBy(corporateStatementChecklistMasterDto.getUpdatedBy());
						corporateStatementChecklistVisitTypeMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(corporateStatementChecklistMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
						corporateStatementChecklistVisitTypeMapper.setStatus('I');
						
						iCorporateStatementChecklistMasterDao.updatePreviousVisitTypeList(corporateStatementChecklistVisitTypeMapper);
					}
				}
				if(corporateStatementChecklistMasterDto.getNewlyAddedVisitTypeList().size()>0)
				{
					ListIterator<VisitTypeMasterDto> iterator = corporateStatementChecklistMasterDto.getNewlyAddedVisitTypeList().listIterator();
					while(iterator.hasNext())
					{
						VisitTypeMasterDto masterDto = iterator.next();
						CorporateStatementChecklistVisitTypeMapper corporateStatementChecklistVisitTypeMapper = new CorporateStatementChecklistVisitTypeMapper();
						corporateStatementChecklistVisitTypeMapper.setCorporateStatementChecklistId(corporateStatementChecklistMasterDto.getCorporateStatementChecklistId());
						corporateStatementChecklistVisitTypeMapper.setVisitTypeId(masterDto.getId());
//						corporateStatementChecklistVisitTypeMapper.setCreatedBy(corporateStatementChecklistMasterDto.getUpdatedBy());
						corporateStatementChecklistVisitTypeMapper.setCreatedDate(GlobalCommonDateUtils.getDate(corporateStatementChecklistMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
//						corporateStatementChecklistVisitTypeMapper.setUpdatedBy(corporateStatementChecklistMasterDto.getUpdatedBy());
						corporateStatementChecklistVisitTypeMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(corporateStatementChecklistMasterDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
						corporateStatementChecklistVisitTypeMapper.setStatus('A');
						corporateStatementChecklistVisitTypeMapper.setOrganizationId(corporateStatementChecklistMasterDto.getOrganizationId());
						iCorporateStatementChecklistMasterDao.saveCorporateStatementListBedVisitTypeMapper(corporateStatementChecklistVisitTypeMapper);
					}
					
				}
				
				Request request = new Request();
				Payload payload = new Payload();
				payload.setActionType("update");
				payload.setData(corporateStatementChecklistMasterDto);
				request.setKey(MasterKeyConstants.Type_Of_Stay.toString());
				request.setPayload(payload);
				//iOtimsService.publish(request);
				
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCorporateStatementChecklistMasterStatus(
			CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto) throws ApplicationException {
		try {
			return iCorporateStatementChecklistMasterDao.updateCorporateStatementChecklistMasterStatus(corporateStatementChecklistMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getCorporateStatementChecklistMasterListAll(CorporateStatementChecklistMasterDto corporateStatementChecklistMasterDto)
			throws ApplicationException {
		try {
			
			List<CorporateStatementChecklistMasterDto> finalList = new LinkedList<>();
			Response res = iCorporateStatementChecklistMasterDao.getCorporateStatementChecklistMasterList(corporateStatementChecklistMasterDto);
			ListIterator<CorporateStatementChecklistMasterDto> iterator = (ListIterator<CorporateStatementChecklistMasterDto>) res.getListObject().listIterator();
			while(iterator.hasNext())
			{
				CorporateStatementChecklistMasterDto corporateStatementMasterDto = iterator.next();
				corporateStatementMasterDto.setOrganizationId(corporateStatementChecklistMasterDto.getOrganizationId());
				corporateStatementMasterDto.setUnitId(corporateStatementChecklistMasterDto.getUnitId());
				corporateStatementMasterDto.setVisitTypeMasterDtosList(iCorporateStatementChecklistMasterDao.getCorporateStatementChecklistVistList(corporateStatementMasterDto));
				
				finalList.add(corporateStatementMasterDto);
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
 }

	
	
}
