package com.param.bloodBank.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.bloodBank.dto.BloodBagCollectionDto;
import com.param.bloodBank.dto.BloodBankStockDto;
import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.bloodBank.service.IBloodBagCollectionService;
import com.param.bloodBank.service.IBloodBankStockService;
import com.param.bloodBank.service.ITransfusionTransmittedDiseasesService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping(value="/api/bloodbank")
@SuppressWarnings({"rawtypes","unchecked"})
public class BloodBagCollectionController implements ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagCollectionController.class);


	@Autowired
	private IBloodBagCollectionService iBloodBagCollectionService;

	@Autowired
	ITransfusionTransmittedDiseasesService iTransfusionTransmittedDiseasesService;
	
	@Autowired
	private IBloodBankStockService iBloodBankStockService;
	
	@RequestMapping(value = "/saveBloodBagCollection", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response saveBloodBagCollection(@RequestBody BloodBagCollectionDto bloodBagCollectionDto) {
		try {
			return iBloodBagCollectionService.saveBloodBagCollection(bloodBagCollectionDto);

		} catch (Exception e) {
			logger.error("saveBloodBagCollection failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/searchBloodBag", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response searchBloodBag(@RequestBody BloodBagCollectionDto bloodBagCollectionDto) {
		try {
			Response res1 =  iBloodBagCollectionService.searchBloodBag(bloodBagCollectionDto); //collection

			TransfusionTransmittedDiseasesDto dto = new TransfusionTransmittedDiseasesDto(); 
			BloodBankStockDto bloodBankStockDto = new BloodBankStockDto();
			
			if(bloodBagCollectionDto.getFromDate()!="" && bloodBagCollectionDto.getFromDate()!=null && bloodBagCollectionDto.getToDate()!="" && bloodBagCollectionDto.getToDate()!=null )
			{
				dto.setFromDate(bloodBagCollectionDto.getFromDate());
				dto.setToDate(bloodBagCollectionDto.getToDate());
				bloodBankStockDto.setFromDate(bloodBagCollectionDto.getFromDate());
				bloodBankStockDto.setToDate(bloodBagCollectionDto.getToDate());
			}
			else if(bloodBagCollectionDto.getBloodBagNo()!="" && bloodBagCollectionDto.getBloodBagNo()!=null)
			{
				dto.setBloodBagNo(bloodBagCollectionDto.getBloodBagNo());
				bloodBankStockDto.setBloodBagNo(bloodBagCollectionDto.getBloodBagNo());
			}
			dto.setOrganizationId(bloodBagCollectionDto.getOrganizationId());
			dto.setUnitId(bloodBagCollectionDto.getUnitId());
			bloodBankStockDto.setOrganizationId(bloodBagCollectionDto.getOrganizationId());
			bloodBankStockDto.setUnitId(bloodBagCollectionDto.getUnitId());
			Response res2 = iTransfusionTransmittedDiseasesService.searchTTD(dto); //ttd
			Response res3 = iBloodBankStockService.getBloodBankStockListByTTDStatus(bloodBankStockDto); //bloodBank Stock
			List<BloodBagCollectionDto> finalList = new ArrayList<>();
			
			if(res1.getListObject().size()>0 && res1.getListObject()!=null)
			{
				finalList.addAll(res1.getListObject());
			}
			if(res2.getListObject().size()>0 && res2.getListObject()!=null)
			{
				finalList.addAll(res2.getListObject());
			}
			
			if(res3.getListObject().size()>0 && res3.getListObject()!=null)
			{
				finalList.addAll(res3.getListObject());
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);
		} catch (Exception e) {
			logger.error("searchBloodBag failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getDonorAndScreeningAndBagCollectionDetail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDonorAndScreeningAndBagCollectionDetail(@RequestBody BloodBagCollectionDto bloodBagCollectionDto) {
		try {
			return iBloodBagCollectionService.getDonorAndScreeningAndBagCollectionDetail(bloodBagCollectionDto);

		} catch (Exception e) {
			logger.error("getDonorAndScreeningAndBagCollectionDetail failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getBagCollectionDetailById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBagCollectionDetailById(@RequestBody BloodBagCollectionDto bloodBagCollectionDto) {
		try {
			return iBloodBagCollectionService.getBagCollectionDetailById(bloodBagCollectionDto);

		} catch (Exception e) {
			logger.error("getBagCollectionDetailById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@RequestMapping(value = "/getExaminationListByCollectionId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getExaminationListByCollectionId(@RequestBody BloodBagCollectionDto bloodBagCollectionDto)
			throws ApplicationException {
		try {
			return iBloodBagCollectionService.getExaminationListByCollectionId(bloodBagCollectionDto);
		} catch (Exception e) {
			logger.error("getExaminationListByCollectionId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
