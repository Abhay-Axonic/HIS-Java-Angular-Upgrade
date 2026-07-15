package com.param.otims.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dao.IProcedureEquipmentScheduleDao;
import com.param.global.otims.dto.EquipmentSchedulingDto;
import com.param.global.otims.dto.EquipmentSchedulingTimeDetailsDto;
import com.param.global.otims.dto.OtSchedulingTimeDetailsDto;
import com.param.otims.dao.IEquipmentSchedulingDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class EquipmentSchedulingServiceImpl implements IEquipmentSchedulingService ,ICommonConstants {

	private static final Logger logger = Logger.getLogger(EquipmentSchedulingServiceImpl.class);

	@Autowired
	IEquipmentSchedulingDao iEquipmentSchedulingDao;
	
	@Autowired
	IProcedureEquipmentScheduleDao iProcedureEquipmentScheduleDao;
	

	@Override
	public Response saveEquipmentScheduling(EquipmentSchedulingDto equipmentSchedulingDto) throws ApplicationException {
		try {
				List<EquipmentSchedulingDto> list = new ArrayList<EquipmentSchedulingDto>();
				for (EquipmentSchedulingTimeDetailsDto EqBatchAndItmDto : equipmentSchedulingDto.getBatchAndItem()) {
					
					for(OtSchedulingTimeDetailsDto schedDetails : equipmentSchedulingDto.getTimeDetails()) {
						
						Map<String,Integer> DateList= GlobalCommonDateUtils.getMonthValue(schedDetails.getFromDate(),schedDetails.getToDate(),schedDetails.getDays());
						if(!DateList.isEmpty()) {
							for (Entry<String, Integer> a : DateList.entrySet()) {
								
								EquipmentSchedulingDto dto = new EquipmentSchedulingDto();
								dto.setCreatedBy(equipmentSchedulingDto.getCreatedBy());
								dto.setCreatedDate(equipmentSchedulingDto.getCreatedDate());
								dto.setDayId(a.getValue());
								dto.setEquipmentSchedulingDate(a.getKey());
								dto.setFromTime(schedDetails.getFromTime());
								dto.setAssetCategoryId(equipmentSchedulingDto.getAssetCategoryId());
								dto.setOrgId(equipmentSchedulingDto.getOrgId());
								dto.setStatus(equipmentSchedulingDto.getStatus());
								dto.setToTime(schedDetails.getToTime());
								dto.setUnitId(equipmentSchedulingDto.getUnitId());
								dto.setUpdatedBy(equipmentSchedulingDto.getUpdatedBy());
								dto.setUpdatedDate(equipmentSchedulingDto.getUpdatedDate());
								dto.setBatchId(EqBatchAndItmDto.getBatchId());
								dto.setItemId(EqBatchAndItmDto.getItemId());
								Response res = iEquipmentSchedulingDao.checkOtScheduling(dto);
								
								if(res.getListObject()!=null && res.getListObject().size()>0) {
									return new Response<>(ERROR, ERROR_CODE, "Slot Already Exist", null, null);
								}
								list.add(dto);
							}
						}
					}
				}
			
				return iEquipmentSchedulingDao.saveEquipmentScheduling(list);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getEquipmentScheduling(EquipmentSchedulingDto equipmentSchedulingDto) throws ApplicationException {
		try {
			
			return iEquipmentSchedulingDao.getEquipmentScheduling(equipmentSchedulingDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response deActivateEquipmentScheduling(EquipmentSchedulingDto equipmentSchedulingDto)
			throws ApplicationException {
		try {
			
			return iEquipmentSchedulingDao.deActivateEquipmentScheduling(equipmentSchedulingDto);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkEquipmentavailableOrNot(EquipmentSchedulingDto equipmentSchedulingDto)
			throws ApplicationException {
		try {
			
			Response res = iEquipmentSchedulingDao.checkEquipmentavailableOrNot(equipmentSchedulingDto);
			if(res.getListObject()!=null && res.getListObject().size()>0) {
				Response reolcvd = iProcedureEquipmentScheduleDao.checkEquipmentUsedSlot(equipmentSchedulingDto);
				
				if(reolcvd.getListObject()!=null && reolcvd.getListObject().size()>0) {
					return new Response(ERROR, ERROR_CODE, "red", null,null);
				}
				
				Response resMaoin =  iProcedureEquipmentScheduleDao.checkEquipmentMaintenamceStatus(equipmentSchedulingDto);
				if(resMaoin.getListObject()!=null && resMaoin.getListObject().size()>0) {
					return new Response(ERROR, ERROR_CODE, "gray", null,null);
				}	
				
				return new Response(SUCCESS, SUCCESS_CODE, "green",null,null);
			}else {
				return new Response(ERROR, ERROR_CODE, "red", null,null);
			}

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response checkEquipmentListAvailableOrNot(List<EquipmentSchedulingDto> equipmentSchedulingDto)
			throws ApplicationException {
		try {
			
			List<EquipmentSchedulingDto> finalList = new ArrayList<>();
			for (EquipmentSchedulingDto equipmentSchedulingDto2 : equipmentSchedulingDto) {
				EquipmentSchedulingDto dto = new EquipmentSchedulingDto();
				Response res = iEquipmentSchedulingDao.checkEquipmentavailableOrNot(equipmentSchedulingDto2);
				if(res.getListObject()!=null && res.getListObject().size()>0) {
					Response reolcvd = iProcedureEquipmentScheduleDao.checkEquipmentUsedSlot(equipmentSchedulingDto2);
					
					if(reolcvd.getListObject()!=null && reolcvd.getListObject().size()>0) {
						dto.setAssetCategoryId(equipmentSchedulingDto2.getAssetCategoryId());
						dto.setBatchId(equipmentSchedulingDto2.getBatchId());
						dto.setItemId(equipmentSchedulingDto2.getItemId());
						dto.setColor("red");
						finalList.add(dto);
					}
					
					Response resMaoin =  iProcedureEquipmentScheduleDao.checkEquipmentMaintenamceStatus(equipmentSchedulingDto2);
					if(resMaoin.getListObject()!=null && resMaoin.getListObject().size()>0) {
						dto.setAssetCategoryId(equipmentSchedulingDto2.getAssetCategoryId());
						dto.setBatchId(equipmentSchedulingDto2.getBatchId());
						dto.setItemId(equipmentSchedulingDto2.getItemId());
						dto.setColor("gray");
						finalList.add(dto);
					}	
					dto.setAssetCategoryId(equipmentSchedulingDto2.getAssetCategoryId());
					dto.setBatchId(equipmentSchedulingDto2.getBatchId());
					dto.setItemId(equipmentSchedulingDto2.getItemId());
					dto.setColor("green");
					finalList.add(dto);
					
				}else {
					dto.setAssetCategoryId(equipmentSchedulingDto2.getAssetCategoryId());
					dto.setBatchId(equipmentSchedulingDto2.getBatchId());
					dto.setItemId(equipmentSchedulingDto2.getItemId());
					dto.setColor("red");
					finalList.add(dto);
				}

			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, finalList, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


}
