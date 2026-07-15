package com.param.lis.global.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.lis.common.dao.ICommonListDao;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.dao.IAntibioticOrganismMpprMasterDao;
import com.param.lis.global.dto.AntibioticOrganismMpprMasterDto;
import com.param.lis.global.dto.SelectedAntibioticListDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class AntibioticOrganismMpprMasterServiceImpl implements IAntibioticOrganismMpprMasterService ,ICommonConstants, IError{
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private IAntibioticOrganismMpprMasterDao iAntibioticOrganismMpprMasterDao;
	
	@Autowired
	ICommonListDao iCommonListDao;

	@Transactional
	@Override
	public Response addAntibioticOrganismMpprMaster(
			AntibioticOrganismMpprMasterDto antibioticAdditionMasterDto)
			throws ApplicationException {
		try{	
			if(antibioticAdditionMasterDto.getIsUpdate().equals(false)) {
				
				Response res = iAntibioticOrganismMpprMasterDao.checkAntibioticOrganismMpprMasterAvaiable(antibioticAdditionMasterDto);
				if(res.getListObject() != null && res.getListObject().size() > 0){
					return new Response<>(ERROR, ERR_500, ANTIBIOTICS_ORGANISM_CODE_EXISIS, null, null);
				}
			}
			List<AntibioticOrganismMpprMasterDto> list = new ArrayList();
			for (AntibioticOrganismMpprMasterDto dto : antibioticAdditionMasterDto.getSelectAntiboiticOrganismMppList()) {
			AntibioticOrganismMpprMasterDto antibioticAdditionDto = new AntibioticOrganismMpprMasterDto();
			antibioticAdditionDto.setCreatedDate(System.currentTimeMillis());
			antibioticAdditionDto.setOrganismId(antibioticAdditionMasterDto.getOrganismId());
			antibioticAdditionDto.setAntiboiticId(dto.getAntiboiticId());
			antibioticAdditionDto.setAntiboiticGroupId(dto.getAntiboiticGroupId());
		    antibioticAdditionDto.setStatus(dto.getStatus()!=null?dto.getStatus():'A');
			antibioticAdditionDto.setOrgId(antibioticAdditionMasterDto.getOrgId());
			antibioticAdditionDto.setCreatedBy(antibioticAdditionMasterDto.getCreatedBy());
			antibioticAdditionDto.setUpdatedBy(antibioticAdditionMasterDto.getUpdatedBy());
			antibioticAdditionDto.setUpdatedDate(System.currentTimeMillis());
			antibioticAdditionDto.setAntiboiticOrganismMpprId(dto.getAntiboiticOrganismMpprId()!=null?dto.getAntiboiticOrganismMpprId():null);
			list.add(antibioticAdditionDto);
			}
			list.forEach(antibioticAdditionDto -> iAntibioticOrganismMpprMasterDao.addAntibioticOrganismMpprMaster(antibioticAdditionDto));
//			iAntibioticOrganismMpprMasterDao.addAntibioticOrganismMpprMaster(antibioticAdditionDto); /getAntibioticAddtionClassMasterById
			return new Response<>(SUCCESS, SUCCESS_200,ANTIBIOTICS_ORGANISM_ADD_SUCC, null, null);
			
		}catch(Exception e){
			throw e;
	
		}
	}


	@Override
	@Transactional
	public Response listAntibioticOrganismMpprMaster(Integer orgId,
			Integer offset, Integer recordPerPage) throws ApplicationException {
		try
		{
			Response res =  iAntibioticOrganismMpprMasterDao.listAntibioticOrganismMpprMaster(orgId, offset, recordPerPage);
			if(res.getListObject() != null && res.getListObject().size() > 0){
				List<AntibioticOrganismMpprMasterDto> antibioticAddtionClassMst = res.getListObject() != null ? (List<AntibioticOrganismMpprMasterDto>)res.getListObject() : null;
				if(antibioticAddtionClassMst.isEmpty())
				{
					return new Response(ERROR, ERR_500, ANTIBIOTICS_ORGANISM_CORDS_NOT_FOUND, null, null);
				}
				else{
					return new Response(SUCCESS, SUCCESS_200, null, antibioticAddtionClassMst, null);
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
	public Response getToTalAntibioticOrganismMpprMaster(Integer orgId)
			throws ApplicationException {
		try
		{
			return iAntibioticOrganismMpprMasterDao.getToTalAntibioticOrganismMpprMaster(orgId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response activateInactivateAntibioticOrganismMpprMaster(
			Integer orgId, Integer organismId, Character status)
			throws ApplicationException {
		try
		{ 
		    return iAntibioticOrganismMpprMasterDao.activateInactivateAntibioticOrganismMpprMaster(orgId, organismId, status);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response updateAntibioticOrganismMpprMaster(
			AntibioticOrganismMpprMasterDto antibioticAdditionMasterDto)
			throws ApplicationException {
		try{	
			
			List<AntibioticOrganismMpprMasterDto> listAntibioticOrganismMpprMasterDto = new ArrayList();
			Iterator ite=antibioticAdditionMasterDto.getSelectAntiboiticOrganismMppList().iterator(); 
			while (ite.hasNext()) {
				AntibioticOrganismMpprMasterDto antibioticOrganismMpprMasterDto = new AntibioticOrganismMpprMasterDto();
				Integer antiboiticId = (Integer) ite.next();
				long time = TimeZoneComponent.getDateByzone(0).getTime();
				antibioticOrganismMpprMasterDto.setUpdatedDate(time);
				antibioticOrganismMpprMasterDto.setOrganismId(antibioticAdditionMasterDto.getOrganismId());
				antibioticOrganismMpprMasterDto.setAntiboiticId(antiboiticId);
				antibioticOrganismMpprMasterDto.setStatus(antibioticAdditionMasterDto.getStatus());
				antibioticOrganismMpprMasterDto.setOrgId(antibioticAdditionMasterDto.getOrgId());
				antibioticOrganismMpprMasterDto.setUpdatedBy(antibioticAdditionMasterDto.getUpdatedBy());
				listAntibioticOrganismMpprMasterDto.add(antibioticOrganismMpprMasterDto);
			}
			if(!listAntibioticOrganismMpprMasterDto.isEmpty())
			{
				return iAntibioticOrganismMpprMasterDao
				.updateAntibioticOrganismMpprMaster(listAntibioticOrganismMpprMasterDto);
			}
			else {
				return new Response<>(ERROR, ERR_201,ANTIBIOTICS_ORGANISM_ADD_FAIL, null, null);
			}
			
		}catch(Exception e){
			
			throw e;
		}
		
		
	}


	@Override
	@Transactional
	public Response getAntibioticOrganismMpprMasterById(Integer orgId,
			Integer antiboiticClassId) throws ApplicationException {
		try
		{
			return iAntibioticOrganismMpprMasterDao.getAntibioticOrganismMpprMasterById(orgId, antiboiticClassId);
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response getAntibioticByOrganismId(Integer organismId)
			throws ApplicationException {
		try
		{
			return iAntibioticOrganismMpprMasterDao.getAntibioticByOrganismId(organismId);
			
		} catch (Exception e)
		{
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response<AntibioticOrganismMpprMasterDto, Integer> getAntibioticByOrganismIdUpdate(Integer antiboiticClassId)
			throws ApplicationException {
		try
		{
			HashSet<AntibioticOrganismMpprMasterDto> set = new HashSet<AntibioticOrganismMpprMasterDto>();  
			int orgId = 0;
			List<AntibioticOrganismMpprMasterDto> masterList = null;
			Response res =  iAntibioticOrganismMpprMasterDao.getAntibioticByOrganismId(antiboiticClassId);
			if(res.getListObject() != null && res.getListObject().size()>0) {
				List<AntibioticOrganismMpprMasterDto> ls = res.getListObject();
				List<Integer> groupIds = new ArrayList<>();
				for (AntibioticOrganismMpprMasterDto antibioticOrganismMpprMasterDto : ls) {
					groupIds.add(antibioticOrganismMpprMasterDto.getAntiboiticGroupId());
					orgId = antibioticOrganismMpprMasterDto.getOrgId();
				}
				
				Set<Integer> hashsetList = new HashSet<Integer>(groupIds);
				List<Integer> list = new ArrayList<Integer>(hashsetList);
					SelectedAntibioticListDto dto = new SelectedAntibioticListDto();
					dto.setOrgId(orgId);
					dto.setSelectAntibioticList(list);
				Response res1 =	iCommonListDao.getAntibioticGroupByGroup(dto);
				if(res1.getStatus().equals(SUCCESS)) {
					masterList = res1.getListObject();
					for (AntibioticOrganismMpprMasterDto antibioticOrganismMpprMasterDto : masterList) {
						for (AntibioticOrganismMpprMasterDto selecteddto : ls) {
							if(antibioticOrganismMpprMasterDto.getAntiboiticId()==selecteddto.getAntiboiticId() && selecteddto.getAntiboiticGroupId()== antibioticOrganismMpprMasterDto.getAntiboiticGroupId()) {
								antibioticOrganismMpprMasterDto.setStatus('Y');
								antibioticOrganismMpprMasterDto.setAntiboiticOrganismMpprId(selecteddto.getAntiboiticOrganismMpprId());
							}
						}
					}
				}
			}
			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, masterList, null);
			
		} catch (Exception e)
		{
			
			throw e;
		}
	}


}
