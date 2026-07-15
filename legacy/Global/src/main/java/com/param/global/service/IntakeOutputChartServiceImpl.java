package com.param.global.service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IIntakeOutputChartDao;
import com.param.global.dto.IntakeOutputChartDetailsDto;
import com.param.global.dto.IntakeOutputChartMasterDto;
import com.param.global.dto.OutputChartDetailsDto;
import com.param.global.model.IntakeOutputChartMaster;
import com.param.global.otims.dto.OtcCompletionInvImplantsDto;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
@Service
@SuppressWarnings({"rawtypes","unused"})
public class IntakeOutputChartServiceImpl implements IIntakeOutputChartService, ICommonConstants{
	private static final Logger logger = Logger.getLogger(IntakeOutputChartServiceImpl.class);
	@Autowired
	private IIntakeOutputChartDao iIntakeOutputChartDao;
	
	@Override
	@Transactional
	public Response saveIntakeOutputChart(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			intakeOutputChartMasterDto.setStatus('A'); 
			intakeOutputChartMasterDto.setUpdatedBy(intakeOutputChartMasterDto.getCreatedBy());
			intakeOutputChartMasterDto.setUpdatedDate(intakeOutputChartMasterDto.getCreatedDate());
			
		
		if(intakeOutputChartMasterDto.getIntakeOutputChartMasterDtoList().size()>0) {
			Response presRes = iIntakeOutputChartDao.saveIntakeOutputChart(intakeOutputChartMasterDto);
			IntakeOutputChartMasterDto intakeOutputChartMasterDto1= (IntakeOutputChartMasterDto) presRes.getObject();
			ListIterator<IntakeOutputChartDetailsDto> iterator = intakeOutputChartMasterDto.getIntakeOutputChartMasterDtoList().listIterator();
			while(iterator.hasNext())
			{
				IntakeOutputChartDetailsDto intakeOutputChartDetailsDto = iterator.next();
					intakeOutputChartDetailsDto.setOrganizationId(intakeOutputChartMasterDto.getOrganizationId());
					intakeOutputChartDetailsDto.setUnitId(intakeOutputChartMasterDto.getUnitId());
					intakeOutputChartDetailsDto.setIntakeOutputChartId(intakeOutputChartMasterDto1.getIntakeOutputChartId());
					intakeOutputChartDetailsDto.setTotalIntake(intakeOutputChartMasterDto1.getTotalIntake());
					intakeOutputChartDetailsDto.setCreatedBy(intakeOutputChartMasterDto.getCreatedBy());
					intakeOutputChartDetailsDto.setCreatedDate(intakeOutputChartMasterDto.getCreatedDate());
					intakeOutputChartDetailsDto.setIntakeFluidId(intakeOutputChartDetailsDto.getIntakeFluidId());
					intakeOutputChartDetailsDto.setIntakeTypeFluidId(intakeOutputChartDetailsDto.getIntakeTypeFluidId());
					intakeOutputChartDetailsDto.setStartIntakeDate(intakeOutputChartDetailsDto.getStartIntakeDate());
					intakeOutputChartDetailsDto.setStartIntakeTime( intakeOutputChartDetailsDto.getStartIntakeTime());
					intakeOutputChartDetailsDto.setStatus(intakeOutputChartDetailsDto.getStatus());
					intakeOutputChartDetailsDto.setCreatedDate(intakeOutputChartDetailsDto.getCreatedDate());
					intakeOutputChartDetailsDto.setCreatedDate(intakeOutputChartDetailsDto.getCreatedDate());
					intakeOutputChartDetailsDto.setUpdatedBy(intakeOutputChartDetailsDto.getUpdatedBy());
					intakeOutputChartDetailsDto.setUpdatedDate(intakeOutputChartDetailsDto.getUpdatedDate());
				Response resPreDtls = iIntakeOutputChartDao.saveIntakeOutputChartDetails(intakeOutputChartDetailsDto);	
			}
		}
		if(intakeOutputChartMasterDto.getOutputChartMasterDtoList() != null && intakeOutputChartMasterDto.getOutputChartMasterDtoList().size()>0) {
			Response presRes1 = iIntakeOutputChartDao.saveIntakeOutputChart(intakeOutputChartMasterDto);
			IntakeOutputChartMasterDto intakeOutputChartMasterDto2= (IntakeOutputChartMasterDto) presRes1.getObject();
			ListIterator<OutputChartDetailsDto> iterator1 = intakeOutputChartMasterDto.getOutputChartMasterDtoList().listIterator();
			while(iterator1.hasNext())
			{
				OutputChartDetailsDto outputChartDetailsDto = iterator1.next();
				outputChartDetailsDto.setOrganizationId(intakeOutputChartMasterDto.getOrganizationId());
				outputChartDetailsDto.setUnitId(intakeOutputChartMasterDto.getUnitId());
				outputChartDetailsDto.setIntakeOutputChartId(intakeOutputChartMasterDto2.getIntakeOutputChartId());
				outputChartDetailsDto.setTotalOutput(intakeOutputChartMasterDto2.getTotalOutput());
				outputChartDetailsDto.setCreatedBy(intakeOutputChartMasterDto.getCreatedBy());
				outputChartDetailsDto.setCreatedDate(intakeOutputChartMasterDto.getCreatedDate());
				outputChartDetailsDto.setOutputTypeId(outputChartDetailsDto.getOutputTypeId());
				outputChartDetailsDto.setStartOutputDate(outputChartDetailsDto.getStartOutputDate());
				outputChartDetailsDto.setStartOutputTime(outputChartDetailsDto.getStartOutputTime());
				outputChartDetailsDto.setStatus(outputChartDetailsDto.getStatus());
				outputChartDetailsDto.setCreatedDate(outputChartDetailsDto.getCreatedDate());
				outputChartDetailsDto.setCreatedDate(outputChartDetailsDto.getCreatedDate());
				outputChartDetailsDto.setUpdatedBy(outputChartDetailsDto.getUpdatedBy());
				outputChartDetailsDto.setUpdatedDate(outputChartDetailsDto.getUpdatedDate());
				Response resPreDtls1 = iIntakeOutputChartDao.saveOutputChartDetails(outputChartDetailsDto);	
			}
		}
		
		return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
	} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	
	}

	@Override
	public Response getIntakeOutputChartListByPatientId(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			
			return iIntakeOutputChartDao.getIntakeOutputChartListByPatientId(intakeOutputChartMasterDto);

		} catch (Exception e) {
			logger.error("getIntakeOutputChartListByPatientId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIntakeOutputChartHistoryListByDate(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			Response res=iIntakeOutputChartDao.getIntakeOutputChartHistoryListByDate(intakeOutputChartMasterDto);
			Map<String,List<IntakeOutputChartMasterDto>> map = new HashMap<String,List<IntakeOutputChartMasterDto>>();
			if(res.getListObject()!=null) {
				List<IntakeOutputChartMasterDto> list = res.getListObject();
			
				
				for (IntakeOutputChartMasterDto dto : list) {
					if(dto.getStartIntakeDate() != null) {
					
					if( !map.containsKey(dto.getStartIntakeDate())) {
						IntakeOutputChartMasterDto dto1 = new IntakeOutputChartMasterDto();
						dto1.setAdmissionId(dto.getAdmissionId());
						dto1.setBalance(dto.getBalance());
						dto1.setIntakeDate(dto.getIntakeDate());
						dto1.setFromDate(dto.getFromDate());
						dto1.setIntakeOutputChartDetailsId(dto.getIntakeOutputChartDetailsId());
						dto1.setIntakeOutputChartId(dto.getIntakeOutputChartId());
						dto1.setIntakeQuantity(dto.getIntakeQuantity());
						dto1.setTotalIntake(dto.getTotalIntake());
						dto1.setTotalOutput(dto.getTotalOutput());
						dto1.setStartIntakeTime(dto.getStartIntakeTime());
						dto1.setStartOutputTime(dto.getStartOutputTime());
						dto1.setIntakeShiftId(dto.getIntakeShiftId());
						dto1.setStartIntakeDate(dto.getStartIntakeDate());
						dto1.setStartOutputDate(dto.getStartOutputDate());
						dto1.setStartIntakeQuantity(dto.getStartIntakeQuantity());
						dto1.setStartOutputQuantity(dto.getStartOutputQuantity());
						dto1.setIntakeShiftName(dto.getIntakeShiftName());
						dto1.setIntakeTime(dto.getIntakeTime());
						dto1.setIntakeTypeFluidDesc(dto.getIntakeTypeFluidDesc());
						dto1.setOrganizationId(dto.getOrganizationId());
						dto1.setOutputChartDetailsId(dto.getOutputChartDetailsId());
						dto1.setOutputDate(dto.getOutputDate());
						dto1.setOutputQuantity(dto.getOutputQuantity());
						dto1.setOutputShiftId(dto.getOutputShiftId());
						dto1.setOutputShiftName(dto.getOutputShiftName());
						dto1.setOutputTime(dto.getOutputTime());
						dto1.setOutputTypeDesc(dto.getOutputTypeDesc());
						List<IntakeOutputChartMasterDto> intakeDetlList = new ArrayList() ; 
						 intakeDetlList.add(dto1);
						 map.put(dto.getStartIntakeDate(), intakeDetlList);
					}else {
						IntakeOutputChartMasterDto dto1 = new IntakeOutputChartMasterDto();
						dto1.setAdmissionId(dto.getAdmissionId());
						dto1.setIntakeDate(dto.getIntakeDate());
						dto1.setBalance(dto.getBalance());
						dto1.setFromDate(dto.getFromDate());
						dto1.setIntakeOutputChartDetailsId(dto.getIntakeOutputChartDetailsId());
						dto1.setIntakeOutputChartId(dto.getIntakeOutputChartId());
						dto1.setIntakeQuantity(dto.getIntakeQuantity());
						dto1.setTotalIntake(dto.getTotalIntake());
						dto1.setTotalOutput(dto.getTotalOutput());
						dto1.setStartIntakeTime(dto.getStartIntakeTime());
						dto1.setStartOutputTime(dto.getStartOutputTime());
						dto1.setIntakeShiftId(dto.getIntakeShiftId());
						dto1.setStartIntakeDate(dto.getStartIntakeDate());
						dto1.setStartOutputDate(dto.getStartOutputDate());
						dto1.setStartIntakeQuantity(dto.getStartIntakeQuantity());
						dto1.setStartOutputQuantity(dto.getStartOutputQuantity());
						dto1.setIntakeShiftName(dto.getIntakeShiftName());
						dto1.setIntakeTime(dto.getIntakeTime());
						dto1.setIntakeTypeFluidDesc(dto.getIntakeTypeFluidDesc());
						dto1.setOrganizationId(dto.getOrganizationId());
						dto1.setOutputChartDetailsId(dto.getOutputChartDetailsId());
						dto1.setOutputDate(dto.getOutputDate());
						dto1.setOutputQuantity(dto.getOutputQuantity());
						dto1.setOutputShiftId(dto.getOutputShiftId());
						dto1.setOutputShiftName(dto.getOutputShiftName());
						dto1.setOutputTime(dto.getOutputTime());
						dto1.setOutputTypeDesc(dto.getOutputTypeDesc());
					//	intakeDetlList.add(dto1);
						map.get(dto.getStartIntakeDate()).add(dto1);
					}
					
					}
					else {
						
						if( !map.containsKey(dto.getStartOutputDate())) {
							IntakeOutputChartMasterDto dto1 = new IntakeOutputChartMasterDto();
							dto1.setAdmissionId(dto.getAdmissionId());
							dto1.setBalance(dto.getBalance());
							dto1.setIntakeDate(dto.getIntakeDate());
							dto1.setFromDate(dto.getFromDate());
							dto1.setIntakeOutputChartDetailsId(dto.getIntakeOutputChartDetailsId());
							dto1.setIntakeOutputChartId(dto.getIntakeOutputChartId());
							dto1.setIntakeQuantity(dto.getIntakeQuantity());
							dto1.setTotalIntake(dto.getTotalIntake());
							dto1.setTotalOutput(dto.getTotalOutput());
							dto1.setStartIntakeTime(dto.getStartIntakeTime());
							dto1.setStartOutputTime(dto.getStartOutputTime());
							dto1.setIntakeShiftId(dto.getIntakeShiftId());
							dto1.setStartIntakeDate(dto.getStartIntakeDate());
							dto1.setStartOutputDate(dto.getStartOutputDate());
							dto1.setStartIntakeQuantity(dto.getStartIntakeQuantity());
							dto1.setStartOutputQuantity(dto.getStartOutputQuantity());
							dto1.setIntakeShiftName(dto.getIntakeShiftName());
							dto1.setIntakeTime(dto.getIntakeTime());
							dto1.setIntakeTypeFluidDesc(dto.getIntakeTypeFluidDesc());
							dto1.setOrganizationId(dto.getOrganizationId());
							dto1.setOutputChartDetailsId(dto.getOutputChartDetailsId());
							dto1.setOutputDate(dto.getOutputDate());
							dto1.setOutputQuantity(dto.getOutputQuantity());
							dto1.setOutputShiftId(dto.getOutputShiftId());
							dto1.setOutputShiftName(dto.getOutputShiftName());
							dto1.setOutputTime(dto.getOutputTime());
							dto1.setOutputTypeDesc(dto.getOutputTypeDesc());
							List<IntakeOutputChartMasterDto> intakeDetlList = new ArrayList() ; 
							 intakeDetlList.add(dto1);
							 map.put(dto.getStartOutputDate(), intakeDetlList);
						}else {
							IntakeOutputChartMasterDto dto1 = new IntakeOutputChartMasterDto();
							dto1.setAdmissionId(dto.getAdmissionId());
							dto1.setIntakeDate(dto.getIntakeDate());
							dto1.setBalance(dto.getBalance());
							dto1.setFromDate(dto.getFromDate());
							dto1.setIntakeOutputChartDetailsId(dto.getIntakeOutputChartDetailsId());
							dto1.setIntakeOutputChartId(dto.getIntakeOutputChartId());
							dto1.setIntakeQuantity(dto.getIntakeQuantity());
							dto1.setTotalIntake(dto.getTotalIntake());
							dto1.setTotalOutput(dto.getTotalOutput());
							dto1.setStartIntakeTime(dto.getStartIntakeTime());
							dto1.setStartOutputTime(dto.getStartOutputTime());
							dto1.setIntakeShiftId(dto.getIntakeShiftId());
							dto1.setStartIntakeDate(dto.getStartIntakeDate());
							dto1.setStartOutputDate(dto.getStartOutputDate());
							dto1.setStartIntakeQuantity(dto.getStartIntakeQuantity());
							dto1.setStartOutputQuantity(dto.getStartOutputQuantity());
							dto1.setIntakeShiftName(dto.getIntakeShiftName());
							dto1.setIntakeTime(dto.getIntakeTime());
							dto1.setIntakeTypeFluidDesc(dto.getIntakeTypeFluidDesc());
							dto1.setOrganizationId(dto.getOrganizationId());
							dto1.setOutputChartDetailsId(dto.getOutputChartDetailsId());
							dto1.setOutputDate(dto.getOutputDate());
							dto1.setOutputQuantity(dto.getOutputQuantity());
							dto1.setOutputShiftId(dto.getOutputShiftId());
							dto1.setOutputShiftName(dto.getOutputShiftName());
							dto1.setOutputTime(dto.getOutputTime());
							dto1.setOutputTypeDesc(dto.getOutputTypeDesc());
						//	intakeDetlList.add(dto1);
							map.get(dto.getStartOutputDate()).add(dto1);
						}
						
					}
				
			
			}
			}
			
			List<IntakeOutputChartMasterDto> ls = new ArrayList() ;
			  for (Entry<String, List<IntakeOutputChartMasterDto>> a : map.entrySet()) {
				  IntakeOutputChartMasterDto d = new IntakeOutputChartMasterDto();
				  d.setIntakeDate(a.getKey());
				  d.setIntakeOutputChartMasterDetailsList(a.getValue());
				  ls.add(d);
				}	
			
			
		//	ls.add(m)
		//	ls.add(map);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, ls);

		} catch (Exception e) {
			logger.error("getIntakeOutputChartHistoryListByDate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getIntakeHistoryList(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			
			return iIntakeOutputChartDao.getIntakeHistoryList(intakeOutputChartMasterDto);

		} catch (Exception e) {
			logger.error("getIntakeHistoryList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getOutputHistoryList(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			
			return iIntakeOutputChartDao.getOutputHistoryList(intakeOutputChartMasterDto);

		} catch (Exception e) {
			logger.error("getOutputHistoryList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getInputOutputChartHistoryDetailsList(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			
			return iIntakeOutputChartDao.getInputOutputChartHistoryDetailsList(intakeOutputChartMasterDto);

		} catch (Exception e) {
			logger.error("getInputOutputChartHistoryDetailsList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getInputOutputChartHistoryByDate(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			
				return iIntakeOutputChartDao.getInputOutputChartHistoryByDate(intakeOutputChartMasterDto);

		} catch (Exception e) {
			logger.error("getInputOutputChartHistoryByDate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response updateInputOutputStartStatus(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
	try {
		
		return iIntakeOutputChartDao.updateInputOutputStartStatus(intakeOutputChartMasterDto);

		} catch (Exception e) {
			logger.error("updateInputOutputStartStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateStatusForStartIOChart(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			
			return iIntakeOutputChartDao.updateStatusForStartIOChart(intakeOutputChartMasterDto);

			} catch (Exception e) {
				logger.error("updateStatusForStartIOChart failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getIntakeAndOutputDateByPatientId(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			
				return iIntakeOutputChartDao.getIntakeAndOutputDateByPatientId(intakeOutputChartMasterDto);

			} catch (Exception e) {
				logger.error("getIntakeAndOutputDateByPatientId failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response updateIntakeOutputChart(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			intakeOutputChartMasterDto.setStatus('A'); 
			intakeOutputChartMasterDto.setUpdatedBy(intakeOutputChartMasterDto.getCreatedBy());
			intakeOutputChartMasterDto.setUpdatedDate(intakeOutputChartMasterDto.getCreatedDate());
			
		
		if(intakeOutputChartMasterDto.getIntakeOutputChartMasterDtoList().size()>0) {
			Response presRes = iIntakeOutputChartDao.updateIntakeOutputChart(intakeOutputChartMasterDto);
			IntakeOutputChartMasterDto intakeOutputChartMasterDto1= (IntakeOutputChartMasterDto) presRes.getObject();
			ListIterator<IntakeOutputChartDetailsDto> iterator = intakeOutputChartMasterDto.getIntakeOutputChartMasterDtoList().listIterator();
			while(iterator.hasNext())
			{
				IntakeOutputChartDetailsDto intakeOutputChartDetailsDto = iterator.next();
					intakeOutputChartDetailsDto.setOrganizationId(intakeOutputChartMasterDto.getOrganizationId());
					intakeOutputChartDetailsDto.setUnitId(intakeOutputChartMasterDto.getUnitId());
					intakeOutputChartDetailsDto.setIntakeOutputChartId(intakeOutputChartMasterDto1.getIntakeOutputChartId());
					intakeOutputChartDetailsDto.setTotalIntake(intakeOutputChartMasterDto1.getTotalIntake());
					intakeOutputChartDetailsDto.setCreatedBy(intakeOutputChartMasterDto.getCreatedBy());
					intakeOutputChartDetailsDto.setCreatedDate(intakeOutputChartMasterDto.getCreatedDate());
					intakeOutputChartDetailsDto.setIntakeFluidId(intakeOutputChartDetailsDto.getIntakeFluidId());
					intakeOutputChartDetailsDto.setIntakeTypeFluidId(intakeOutputChartDetailsDto.getIntakeTypeFluidId());
					intakeOutputChartDetailsDto.setStartIntakeDate(intakeOutputChartDetailsDto.getStartIntakeDate());
					intakeOutputChartDetailsDto.setStartIntakeTime( intakeOutputChartDetailsDto.getStartIntakeTime());
					intakeOutputChartDetailsDto.setStatus(intakeOutputChartDetailsDto.getStatus());
					intakeOutputChartDetailsDto.setCreatedDate(intakeOutputChartDetailsDto.getCreatedDate());
					intakeOutputChartDetailsDto.setCreatedDate(intakeOutputChartDetailsDto.getCreatedDate());
					intakeOutputChartDetailsDto.setUpdatedBy(intakeOutputChartDetailsDto.getUpdatedBy());
					intakeOutputChartDetailsDto.setUpdatedDate(intakeOutputChartDetailsDto.getUpdatedDate());
					intakeOutputChartDetailsDto.setIntakeOutputChartDetailsId(intakeOutputChartDetailsDto.getIntakeOutputChartDetailsId());
				Response resPreDtls = iIntakeOutputChartDao.updateIntakeOutputChartDetails(intakeOutputChartDetailsDto);	
			}
		}
		if(intakeOutputChartMasterDto.getOutputChartMasterDtoList() != null && intakeOutputChartMasterDto.getOutputChartMasterDtoList().size()>0) {
			Response presRes1 = iIntakeOutputChartDao.updateIntakeOutputChart(intakeOutputChartMasterDto);
			IntakeOutputChartMasterDto intakeOutputChartMasterDto2= (IntakeOutputChartMasterDto) presRes1.getObject();
			ListIterator<OutputChartDetailsDto> iterator1 = intakeOutputChartMasterDto.getOutputChartMasterDtoList().listIterator();
			while(iterator1.hasNext())
			{
				OutputChartDetailsDto outputChartDetailsDto = iterator1.next();
				outputChartDetailsDto.setOrganizationId(intakeOutputChartMasterDto.getOrganizationId());
				outputChartDetailsDto.setUnitId(intakeOutputChartMasterDto.getUnitId());
				outputChartDetailsDto.setIntakeOutputChartId(intakeOutputChartMasterDto2.getIntakeOutputChartId());
				outputChartDetailsDto.setTotalOutput(intakeOutputChartMasterDto2.getTotalOutput());
				outputChartDetailsDto.setCreatedBy(intakeOutputChartMasterDto.getCreatedBy());
				outputChartDetailsDto.setCreatedDate(intakeOutputChartMasterDto.getCreatedDate());
				outputChartDetailsDto.setOutputTypeId(outputChartDetailsDto.getOutputTypeId());
				outputChartDetailsDto.setStartOutputDate(outputChartDetailsDto.getStartOutputDate());
				outputChartDetailsDto.setStartOutputTime(outputChartDetailsDto.getStartOutputTime());
				outputChartDetailsDto.setStatus(outputChartDetailsDto.getStatus());
				outputChartDetailsDto.setCreatedDate(outputChartDetailsDto.getCreatedDate());
				outputChartDetailsDto.setCreatedDate(outputChartDetailsDto.getCreatedDate());
				outputChartDetailsDto.setUpdatedBy(outputChartDetailsDto.getUpdatedBy());
				outputChartDetailsDto.setUpdatedDate(outputChartDetailsDto.getUpdatedDate());
				outputChartDetailsDto.setOutputChartDetailsId(outputChartDetailsDto.getOutputChartDetailsId());
				Response resPreDtls1 = iIntakeOutputChartDao.updateOutputChartDetails(outputChartDetailsDto);	
			}
		}
		
		return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
	} catch (Exception e) {
		logger.error("updateIntakeOutputChart failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response deleteStatusForStartIOChart(IntakeOutputChartMasterDto intakeOutputChartMasterDto)
			throws ApplicationException {
		try {
			
			return iIntakeOutputChartDao.deleteStatusForStartIOChart(intakeOutputChartMasterDto);

			} catch (Exception e) {
				logger.error("deleteStatusForStartIOChart failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}


	@Override
	public Response deleteStatusForOutputChart(OutputChartDetailsDto outputChartDetailsDto)
			throws ApplicationException {
		// TODO Auto-generated method stub

		try {
			
			return iIntakeOutputChartDao.deleteStatusForOutputChart(outputChartDetailsDto);

			} catch (Exception e) {
				logger.error("deleteStatusForOutputChart failed", e);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	}
