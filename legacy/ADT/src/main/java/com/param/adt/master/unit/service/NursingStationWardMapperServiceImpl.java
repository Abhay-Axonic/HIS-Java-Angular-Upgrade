package com.param.adt.master.unit.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.unit.dao.INursingStationWardMapperDao;
import com.param.adt.master.unit.dto.NursingStationWardMapperDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.UserNursingStationMapperDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class NursingStationWardMapperServiceImpl implements INursingStationWardMapperService, ICommonConstants {

	@Autowired
	INursingStationWardMapperDao iNursingStationWardMapperDao;

	@Override
	public Response saveNursingStationWardMapper(NursingStationWardMapperDto nursingStationWardMapperDto)
			throws ApplicationException {
		try {
			// Station-level pre-check removed: it blocked the ENTIRE request whenever the
			// station had ANY existing mapping, regardless of which ward was being added
			// (docs/DECISION_NEEDED_NursingStationWardMapperBrokenPicker.md). The DAO's own
			// saveNursingStationWardMapper already does correct, per-ward duplicate detection
			// inside its loop (checkIsNursingStationAlreadyAssignedOrNot, called per mapperDto,
			// now ward-scoped) -- this outer gate was redundant with it and, unlike the loop,
			// had no way to be ward-specific since it only ever saw the whole-request DTO.
			return iNursingStationWardMapperDao.saveNursingStationWardMapper(nursingStationWardMapperDto);
		} catch (Exception e) {

		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getNursingStationWardMapperList(NursingStationWardMapperDto nursingStationWardMapperDto)
			throws ApplicationException {
		try {
			return iNursingStationWardMapperDao.getNursingStationWardMapperList(nursingStationWardMapperDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNursingStationWardMapperStatus(NursingStationWardMapperDto nursingStationWardMapperDto)
			throws ApplicationException {
		try {
			Response res =iNursingStationWardMapperDao.checkIsNursingStationAlreadyAssigned(nursingStationWardMapperDto);
			if((Integer)res.getObject()>0)
				return new Response<>(ERROR, ERROR_CODE, "Nursing Satation Is Already Assigned to Selected Ward..!!", null, null);
			else
				return iNursingStationWardMapperDao.updateNursingStationWardMapperStatus(nursingStationWardMapperDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNursingStationWardMapper(NursingStationWardMapperDto nursingStationWardMapperDto)
			throws ApplicationException {
	/*	try {
			return iNursingStationWardMapperDao.updateNursingStationWardMapper(nursingStationWardMapperDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);*/
		if(nursingStationWardMapperDto.getIsDeleted()!=null && nursingStationWardMapperDto.getIsDeleted()!='A') {
			return new Response<>(ERROR, ERROR_CODE, "Please activate nursing station", null, 1);
		}
		
		try
		{
			List<NursingStationWardMapperDto> listUserNursingStationMapperDto = new ArrayList();
			Iterator ite=nursingStationWardMapperDto.getNursingWardMapperList().iterator();
			while (ite.hasNext()) {
				NursingStationWardMapperDto dto=new NursingStationWardMapperDto();
				NursingStationWardMapperDto mappedDto= (NursingStationWardMapperDto)ite.next();
//				Integer nursingStationId = (Integer) ite.next();
				dto.setUpdatedDate(nursingStationWardMapperDto.getUpdatedDate());
//				dto.setUserId(nursingStationWardMapperDto.getUserId());
				dto.setWardId(mappedDto.getWardId());
				dto.setCreatedBy(nursingStationWardMapperDto.getCreatedBy());
				dto.setOrganizationId(nursingStationWardMapperDto.getOrganizationId());
				dto.setUnitId(nursingStationWardMapperDto.getUnitId());
				dto.setCreatedDate(nursingStationWardMapperDto.getCreatedDate());
				dto.setStatus(nursingStationWardMapperDto.getStatus());
				dto.setIsDeleted(nursingStationWardMapperDto.getIsDeleted());
				dto.setNursingStationId(nursingStationWardMapperDto.getNursingStationId());
//				dto.setOrganisationId(nursingStationWardMapperDto.getOrganisationId());
				dto.setUpdatedBy(nursingStationWardMapperDto.getUpdatedBy());
				listUserNursingStationMapperDto.add(dto);
			}
			 return iNursingStationWardMapperDao.updateNursingStationWardMapper(listUserNursingStationMapperDto,nursingStationWardMapperDto);
			/*
			 * if(!listUserNursingStationMapperDto.isEmpty()) { return
			 * iNursingStationWardMapperDao.updateNursingStationWardMapper(
			 * listUserNursingStationMapperDto); } else { return new Response<>(SUCCESS,
			 * SUCCESS_CODE, SUCCESS_MESSAGE, null, null); }
			 */
			} catch (Exception e)
				{
					
		//			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
				}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationWardMapperById(Integer id, Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
			return iNursingStationWardMapperDao.getNursingStationWardMapperById(id, orgId, unitId);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationWardMapperCount(NursingStationWardMapperDto nursingStationWardMapperDto)
			throws ApplicationException {
		try {
			return iNursingStationWardMapperDao.getNursingStationWardMapperCount(nursingStationWardMapperDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getWardListByUserId(Integer userId,Integer orgId,Integer unitId)
			throws ApplicationException {
		try {
			
			Response res = iNursingStationWardMapperDao.getWardListByUserId(userId,orgId,unitId);
			
			List<Integer> wardList = new LinkedList<>();
			ListIterator<NursingStationWardMapperDto> iterator = res.getListObject().listIterator();
			while(iterator.hasNext())
			{
				NursingStationWardMapperDto dto = iterator.next();
				wardList.add(dto.getWardId());
			}
			res.setListObject(wardList);
			return res;
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getWardListByNursingStationId(Integer nursingStationId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
			return iNursingStationWardMapperDao.getWardListByNursingStationId(nursingStationId, orgId, unitId);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationListByWardId(Integer wardId, Integer orgId, Integer unitId)
			throws ApplicationException {
		try {
			return iNursingStationWardMapperDao.getNursingStationListByWardId(wardId, orgId, unitId);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
