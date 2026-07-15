package com.param.adt.master.unit.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.admission.dto.BedManagementDto;
import com.param.adt.admission.dto.BedMgtDto;
import com.param.adt.master.global.dto.BedMasterDto;
import com.param.adt.master.unit.dao.IBedMasterDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BedMasterServiceImpl implements IBedMasterService,ICommonConstants{

	@Autowired
	IBedMasterDao iBedMasterDao; 
	
	@Override
	@Transactional
	public Response addBed(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			
			
			Response resNameAndDesc=iBedMasterDao.getBedByNameAndDesc(bedMasterDto);
			if(resNameAndDesc.getStatus().equalsIgnoreCase(SUCCESS) && resNameAndDesc.getListObject()!=null && resNameAndDesc.getListObject().size()>0){
				return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null,null);
			}else{
				Integer getConfiguredBedLimit=iBedMasterDao.getConfiguredBedLimit(bedMasterDto);
				Integer getBedCount=iBedMasterDao.getBedCount(bedMasterDto);
				if(getBedCount!=null && getConfiguredBedLimit!=null && getBedCount>=getConfiguredBedLimit)
				{
					return new Response(ERROR, ALREADY_EXIST_CODE, "You have exceeded bed limit...!", null,null);
				}
				else
				{
				
				// check bed number is exist or not
				Boolean statusFlag=iBedMasterDao.checkBedNumberExistOrNot(bedMasterDto);
				  if(statusFlag) {
					  return new Response(ERROR, ALREADY_EXIST_CODE, "bed number already exist...!", null,null);
				  }
					  
					bedMasterDto.setBedNumber(bedMasterDto.getBedDesc());
				Response res = iBedMasterDao.addBed(bedMasterDto);
				if(res.getStatus().equals(SUCCESS))
				{
					Response res2 = iBedMasterDao.saveBedLog(bedMasterDto);
					if(res2.getStatus().equals(SUCCESS))
					{
						return new Response(SUCCESS, null, COMMON_SUCCESS, null,null);
					}
				}
			}
			}
			
			
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		return new Response(ERROR, null, null, null,null);
	}
	
	@Override
	@Transactional
	public Response getBedMasterList(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			return iBedMasterDao.getBedMasterList(bedMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateBedMaster(BedMasterDto bedMasterDto) throws ApplicationException {
		try {
			Response deptResponse = iBedMasterDao.getBedByNameNotId(bedMasterDto);
			if (deptResponse.getStatus().equals(SUCCESS) && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				Boolean statusFlag=iBedMasterDao.checkBedNumberExistOrNot(bedMasterDto);
				  if(statusFlag) {
					  return new Response(ERROR, ALREADY_EXIST_CODE, "bed number already exist...!", null,null);
				  }
				bedMasterDto.setBedNumber(bedMasterDto.getBedDesc());
//				bedMasterDto.setBedNumber(generateBedNumber(bedMasterDto));
				iBedMasterDao.updateBedMaster(bedMasterDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getbedById(Integer bedId) throws ApplicationException {
		try{
			BedMasterDto bedMasterDto = new BedMasterDto();
			bedMasterDto.setBedId(bedId);
			Response deptResponse = iBedMasterDao.getBedByStatusId(bedMasterDto);
			if (deptResponse.getStatus().equals(SUCCESS) && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else {
			return iBedMasterDao.getbedById(bedId);
			}
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updateStatus(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			
			Response deptResponse = iBedMasterDao.getBedByStatusId(bedMasterDto);
			if (deptResponse.getStatus().equals(SUCCESS) && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else {
				return iBedMasterDao.updateStatus(bedMasterDto);
			}
			
			
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response bedListCount(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			return iBedMasterDao.bedListCount(bedMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getBedCategoryAvailabilityCount(int organizationId, int unitId) throws ApplicationException {
		try{
			return iBedMasterDao.getBedCategoryAvailabilityCount(organizationId,unitId);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	@Override
	@Transactional
	public Response getDataForBedManagementData(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			
			Response res = null;
					if(bedMasterDto.getIsIp()=='Y')
						res = iBedMasterDao.getDataForBedManagementData(bedMasterDto,false);
					else
						res = iBedMasterDao.getDataForBedManagementDataForER(bedMasterDto,false);
			List<BedManagementDto> bedManagementList = res.getListObject();
			Map<Integer, String> ward= new TreeMap<Integer, String>();
			for(BedManagementDto list : bedManagementList)
			{
				ward.put(list.getWardId(),list.getWardName());
			}
			
			List<BedMgtDto> finalList = new LinkedList<>();
			for (Map.Entry<Integer, String> entry : ward.entrySet()) 
			{
				//System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
				BedMgtDto mgt = new BedMgtDto();
					mgt.setWardId(entry.getKey());
					mgt.setWardName(entry.getValue());
					
					List<BedManagementDto> bedManagementDtoList = new LinkedList<>();
					for(BedManagementDto list : bedManagementList)
					{
						if(list.getWardId().equals(entry.getKey()))
							bedManagementDtoList.add(list);
					}	
					mgt.setBedList(bedManagementDtoList);
					finalList.add(mgt);
			}
			return new Response(SUCCESS, null, null, finalList,null);
		}catch (Exception e) {
			throw e;
		}
//		return new Response(ERROR, null, null, null,null);
	}

	@Override
	@Transactional
	public Response getDataForBedManagementDataSearch(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			Response res = null;
			if(bedMasterDto.getIsIp()=='Y')
				res = iBedMasterDao.getDataForBedManagementData(bedMasterDto,true);
			else
				res = iBedMasterDao.getDataForBedManagementDataForER(bedMasterDto,true);
//			Response res = iBedMasterDao.getDataForBedManagementData(bedMasterDto,true);
			List<BedManagementDto> bedManagementList = res.getListObject();

			List<BedMgtDto> finalList = new LinkedList<>();
			Map<Integer, String> ward= new TreeMap<Integer, String>();

			if(bedMasterDto.getIsIp()=='Y') {
				for (BedManagementDto list : bedManagementList) {
					ward.put(list.getWardId(), list.getWardName());
				}


				for (Map.Entry<Integer, String> entry : ward.entrySet()) {
					//System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
					BedMgtDto mgt = new BedMgtDto();
					mgt.setWardId(entry.getKey());
					mgt.setWardName(entry.getValue());

					List<BedManagementDto> bedManagementDtoList = new LinkedList<>();
					for (BedManagementDto list : bedManagementList) {
						if (list.getWardId().equals(entry.getKey()))
							bedManagementDtoList.add(list);
					}
					mgt.setBedList(bedManagementDtoList);
					finalList.add(mgt);
				}
			}
			else{
				Map<Integer, String> room= new TreeMap<Integer, String>();
				for (BedManagementDto list : bedManagementList) {
					if(list.getRoomId() != null) {
						room.put(list.getRoomId(), list.getRoomDesc());
					}
					else{
						ward.put(list.getWardId(), list.getWardName());
					}
				}
				for (Map.Entry<Integer, String> entry : room.entrySet()) {
					//System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
					BedMgtDto mgt = new BedMgtDto();
					mgt.setRoomId(entry.getKey());
					mgt.setRoomDesc(entry.getValue());

					List<BedManagementDto> bedManagementDtoList = new LinkedList<>();
					for (BedManagementDto list : bedManagementList) {
						if (list.getRoomId() != null && list.getRoomId().equals(entry.getKey())) {
							bedManagementDtoList.add(list);
						}
					}
					mgt.setRoomList(bedManagementDtoList);
					finalList.add(mgt);
				}

				for (Map.Entry<Integer, String> entry : ward.entrySet()) {
					//System.out.println("Item : " + entry.getKey() + " Count : " + entry.getValue());
					BedMgtDto mgt = new BedMgtDto();
					mgt.setWardId(entry.getKey());
					mgt.setWardName(entry.getValue());

					List<BedManagementDto> bedManagementDtoList = new LinkedList<>();
					for (BedManagementDto list : bedManagementList) {
						if (list.getWardId().equals(entry.getKey()))
							bedManagementDtoList.add(list);
					}
					mgt.setBedList(bedManagementDtoList);
					finalList.add(mgt);
				}


			}
			return new Response(SUCCESS, null, null, finalList,null);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
	
	
	@Override
	@Transactional
	public Response getBedMovingBedList(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			return iBedMasterDao.getBedMovingBedList(bedMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response movingBedListCount(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			return iBedMasterDao.movingBedListCount(bedMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response activeMovingBedList(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			return iBedMasterDao.activeMovingBedList(bedMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response postBedMagementCount(Integer unitId) throws ApplicationException {
		try{
			return iBedMasterDao.postBedMagementCount(unitId);
		}catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null,null);
	}

	@Override
	@Transactional
	public Response getDialysisChair(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			return iBedMasterDao.getDialysisChair(bedMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response bedMasterUniversalSearch(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			return iBedMasterDao.bedMasterUniversalSearch(bedMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getPaymentEntitlmentbyAdmissionId(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			return iBedMasterDao.getPaymentEntitlmentbyAdmissionId(bedMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getIcuMonitorData(BedMasterDto bedMasterDto) throws ApplicationException {
		try{
			return iBedMasterDao.getIcuMonitorData(bedMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
