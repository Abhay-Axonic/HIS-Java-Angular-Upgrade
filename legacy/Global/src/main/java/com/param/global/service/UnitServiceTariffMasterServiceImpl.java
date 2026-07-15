package com.param.global.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IUnitServiceMapperDao;
import com.param.global.dao.IUnitServiceTariffMasterDao;
import com.param.global.dto.MultiSelectDto;
import com.param.global.dto.ServiceChargesReqDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.ServicePriceDetailsDto;
import com.param.global.dto.ServiceProfitMarginDto;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.dto.UnitServiceMapperDto;
import com.param.global.dto.UnitServiceTariffMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UnitServiceTariffMasterServiceImpl implements IUnitServiceTariffMasterService, ICommonConstants {

	final static Logger logger = Logger.getLogger(UnitServiceTariffMasterServiceImpl.class);

	@Autowired
	private IUnitServiceMapperService iUnitServiceMapperService;

	@Autowired
	IServiceTarrifMasterService iServiceTarrifMasterService;

	@Autowired
	IUnitServiceTariffMasterDao iUnitServiceTariffMasterDao;
	
	@Autowired
	IUnitServiceMapperDao iUnitServiceMapperDao;

	@Override
	public Response getMasterServiceListByMultipleParameters(UnitServiceTariffMasterDto unitServiceTariffMasterDto)
			throws ApplicationException {
		try {
			Integer tariffId = null;
			String tariffDesc = null;
			Integer paymentEntitlementId = null;
			String paymentEntitlementDesc = null;
			Integer patientTypeId = null;
			String patientTypeDesc = null;
			Integer visitTypeId = null;
			String visitTypeDesc = null;
			Integer billingBedCatId = null;
			String billingBedCatDesc = null;
			Integer specialityId = null;
			String specialityDesc = null;
			Integer subSpecialityId = null;
			String subSpecialityDesc = null;
			Integer serviceId = null;
			String serviceDesc = null;
			String effectiveDate = null;
			Double rate = null;
			String statPer = null;
			List serviceList = null;
			/*
			 * List<Integer> specialityIdsList = new ArrayList<>();
			 * List<Integer> subSpecialityIdsList = new ArrayList<>();
			 * List<Integer> serviceIdsList = new ArrayList<>();
			 */

			List<UnitServiceTariffMasterDto> list = new ArrayList<>();

			ListIterator<MultiSelectDto> tariffItr = unitServiceTariffMasterDto.getTariffMasterList().listIterator();
			while (tariffItr.hasNext()) {
				MultiSelectDto tariffItrDto = tariffItr.next();
				tariffId = tariffItrDto.getId();
				tariffDesc = tariffItrDto.getLable();

				ListIterator<MultiSelectDto> visitTypeItr = unitServiceTariffMasterDto.getVisitTypeList()
						.listIterator();
				while (visitTypeItr.hasNext()) {
					MultiSelectDto visitTypeItrDto = visitTypeItr.next();
					visitTypeId = visitTypeItrDto.getId();
					visitTypeDesc = visitTypeItrDto.getLable();

					ListIterator<MultiSelectDto> paymentEntitlementItr = unitServiceTariffMasterDto
							.getPaymentEntitlementList().listIterator();
					while (paymentEntitlementItr.hasNext()) {
						MultiSelectDto paymentEntitlementItrDto = paymentEntitlementItr.next();
						paymentEntitlementId = paymentEntitlementItrDto.getId();
						paymentEntitlementDesc = paymentEntitlementItrDto.getLable();

						ListIterator<MultiSelectDto> patientTypeItr = unitServiceTariffMasterDto.getPatientTypeList()
								.listIterator();
						while (patientTypeItr.hasNext()) {
							MultiSelectDto patientTypeItrDto = patientTypeItr.next();
							patientTypeId = patientTypeItrDto.getId();
							patientTypeDesc = patientTypeItrDto.getLable();

							ListIterator<MultiSelectDto> billingBedCatItr = unitServiceTariffMasterDto
									.getBedCategoryList().listIterator();
							while (billingBedCatItr.hasNext()) {
								MultiSelectDto billingBedCatItrDto = billingBedCatItr.next();
								billingBedCatId = billingBedCatItrDto.getId();
								billingBedCatDesc = billingBedCatItrDto.getLable();

								// <-----Logic for retrieving service list
								if (unitServiceTariffMasterDto.getServiceList().size() > 0)
									serviceList = getServiceListWithBasePrices(unitServiceTariffMasterDto,
											unitServiceTariffMasterDto.getServiceList(), visitTypeId, billingBedCatId,
											patientTypeId, paymentEntitlementId);
								else if (unitServiceTariffMasterDto.getSubSpecialityList().size() > 0)
									serviceList = getServiceListWithBasePrices(unitServiceTariffMasterDto,
											unitServiceTariffMasterDto.getSubSpecialityList(), visitTypeId,
											billingBedCatId, patientTypeId, paymentEntitlementId);
								else if (unitServiceTariffMasterDto.getSpecialityList().size() > 0)
									serviceList = getServiceListWithBasePrices(unitServiceTariffMasterDto,
											unitServiceTariffMasterDto.getSpecialityList(), visitTypeId,
											billingBedCatId, patientTypeId, paymentEntitlementId);
								else
									return new Response(ERROR, null,
											"Select atleast Speciality OR SubSpeciality OR Services", null, null);

								ListIterator<ServicePriceDetailsDto> serviceItr = serviceList.listIterator();
								while (serviceItr.hasNext()) 
								{
									ServicePriceDetailsDto servicePriceDetailsDto = serviceItr.next();
										serviceId = servicePriceDetailsDto.getServiceMasterId();
										serviceDesc = servicePriceDetailsDto.getServiceStandardName();
										specialityId = servicePriceDetailsDto.getSpecialityId();
										specialityDesc = servicePriceDetailsDto.getSpecialityName();
										subSpecialityId = servicePriceDetailsDto.getSubSpecialityId();
										subSpecialityDesc = servicePriceDetailsDto.getSubSpecialityName();
										rate = servicePriceDetailsDto.getBasePrice();

									UnitServiceTariffMasterDto serviceTariffMasterDto = new UnitServiceTariffMasterDto();
										serviceTariffMasterDto.setTariffId(tariffId);
										serviceTariffMasterDto.setTariffDesc(tariffDesc);
										serviceTariffMasterDto.setPaymentEntitlementId(paymentEntitlementId);
										serviceTariffMasterDto.setPaymentEntitlementDesc(paymentEntitlementDesc);
										serviceTariffMasterDto.setPatientTypeId(patientTypeId);
										serviceTariffMasterDto.setPatientTypeDesc(patientTypeDesc);
										serviceTariffMasterDto.setVisitTypeId(visitTypeId);
										serviceTariffMasterDto.setVisitTypeDesc(visitTypeDesc);
										serviceTariffMasterDto.setBillingBedCatId(billingBedCatId);
										serviceTariffMasterDto.setBillingBedCatDesc(billingBedCatDesc);
										serviceTariffMasterDto.setSpecialityId(specialityId);
										serviceTariffMasterDto.setSpecialityDesc(specialityDesc);
										serviceTariffMasterDto.setSubSpecialityId(subSpecialityId);
										serviceTariffMasterDto.setSubSpecialityDesc(subSpecialityDesc);
										serviceTariffMasterDto.setServiceId(serviceId);
										serviceTariffMasterDto.setServiceDesc(serviceDesc);
										serviceTariffMasterDto.setRate(rate);
										serviceTariffMasterDto.setServiceCode(servicePriceDetailsDto.getServiceStandardCode());
									list.add(serviceTariffMasterDto);

								}

							}
						}
					}

				}

			}

			return new Response(SUCCESS, null, null, list, null);
		} catch (Exception e) {
			logger.error("getMasterServiceListByMultipleParameters failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	private List getServiceListWithBasePrices(UnitServiceTariffMasterDto unitServiceTariffMasterDto,List<Integer> listToCarryForGettingServices,Integer visitTypeId,Integer billingBedCatId, Integer patientTypeId, Integer paymentEntitlementId)
	{
		try{
			
		Response unitServiceMapperDtoRes =	null;
		List<Integer> listServiceId=new LinkedList<>();
		
		UnitServiceMapperDto unitServiceMapperDto =new UnitServiceMapperDto();
		unitServiceMapperDto.setOrgnisationId(unitServiceTariffMasterDto.getOrganizationId());
		unitServiceMapperDto.setUnitId(unitServiceTariffMasterDto.getUnitId());
		
		if(unitServiceTariffMasterDto.getServiceList().size()>0)
		{
			unitServiceMapperDto.setServiceIdList(listToCarryForGettingServices);
			
		}else if(unitServiceTariffMasterDto.getSubSpecialityList().size()>0)
		{
			unitServiceMapperDto.setSubSpecialityIdList(listToCarryForGettingServices);
			
		}else if(unitServiceTariffMasterDto.getSpecialityList().size()>0)
		{
			unitServiceMapperDto.setSpecialityIdList(listToCarryForGettingServices);
			
		}
		unitServiceMapperDtoRes = iUnitServiceMapperService.searchUnitServiceByMultipleSpecialityAndSubSpeciality(unitServiceMapperDto);
		
		ListIterator<UnitServiceMapperDto> listItr = unitServiceMapperDtoRes.getListObject().listIterator();
		while(listItr.hasNext())
		{
			UnitServiceMapperDto serviceMapperDto = listItr.next();
			listServiceId.add(serviceMapperDto.getServiceId());
		}
		
		if(unitServiceMapperDtoRes.getListObject().size()>0)
		{
		ServiceChargesReqDto serviceChargesReqDto = new ServiceChargesReqDto();
			serviceChargesReqDto.setOrgId(unitServiceTariffMasterDto.getOrganizationId());
			serviceChargesReqDto.setUnitId(unitServiceTariffMasterDto.getUnitId());
			serviceChargesReqDto.setVisitTypeId(visitTypeId);
			serviceChargesReqDto.setBedTypeId(billingBedCatId);
			serviceChargesReqDto.setPatientTypeId(patientTypeId);
			serviceChargesReqDto.setPaymentTypeId(paymentEntitlementId);
			serviceChargesReqDto.setListServiceId(listServiceId);
		
		Response serviceCharges = iServiceTarrifMasterService.getServiceCharges(serviceChargesReqDto);	
		
		return (List) serviceCharges.getObject();
		}
		else
			return null;
				}catch (Exception e) {
					logger.error("getServiceListWithBasePrices failed", e);
					throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public UnitServiceTariffMasterDto getBasePriceByServiceTariffMaster(
			UnitServiceTariffMasterDto unitServiceTariffMasterDto) {
		try {
			UnitServiceTariffMasterDto masterDto = new UnitServiceTariffMasterDto();
			UnitServiceTariffMasterDto lastObject = new UnitServiceTariffMasterDto();
			List<Long> dayCountList = new LinkedList<>();
			Date effectiveDate = null;
			Date currentDate = GlobalCommonDateUtils.getDate(unitServiceTariffMasterDto.getOrderDate(), "yyyy-M-dd");
			List<UnitServiceTariffMasterDto> list = iUnitServiceTariffMasterDao
					.getBasePriceByServiceTariffMaster(unitServiceTariffMasterDto);
			ListIterator<UnitServiceTariffMasterDto> iterator = list.listIterator();
			while (iterator.hasNext()) {

				masterDto = iterator.next();

				effectiveDate = GlobalCommonDateUtils.getDate(masterDto.getEffectiveDate(), "yyyy-M-dd");
				// in milliseconds
				long diff = currentDate.getTime() - effectiveDate.getTime();
				Long dayDiffDay = diff / (24 * 60 * 60 * 1000);
				dayCountList.add(dayDiffDay);
			}
			if (dayCountList.size() > 0) {
				int minIndex = dayCountList.indexOf(Collections.min(dayCountList));
				lastObject = list.get(minIndex);
			}
			return lastObject;

		} catch (Exception e) {
			logger.error("getBasePriceByServiceTariffMaster failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getBasePriceOfServicesFromTariffMasterByServiceList(
			UnitServiceTariffMasterDto unitServiceTariffMasterDto) throws ApplicationException {
		try {
			UnitServiceTariffMasterDto masterDto = new UnitServiceTariffMasterDto();
			UnitServiceTariffMasterDto lastObject = new UnitServiceTariffMasterDto();
			List<Long> dayCountList = new LinkedList<>();
			Date effectiveDate = null;
			Date currentDate = GlobalCommonDateUtils.getDate(unitServiceTariffMasterDto.getOrderDate(), "yyyy-M-dd");
			List<UnitServiceTariffMasterDto> list = iUnitServiceTariffMasterDao
					.getBasePriceOfServicesFromTariffMasterByServiceList(unitServiceTariffMasterDto);
			ListIterator<UnitServiceTariffMasterDto> iterator = list.listIterator();
			while (iterator.hasNext()) {

				masterDto = iterator.next();

				effectiveDate = GlobalCommonDateUtils.getDate(masterDto.getEffectiveDate(), "yyyy-M-dd");
				// in milliseconds
				long diff = currentDate.getTime() - effectiveDate.getTime();
				Long dayDiffDay = diff / (24 * 60 * 60 * 1000);
				dayCountList.add(dayDiffDay);
			}
			if (dayCountList.size() > 0) {
				int minIndex = dayCountList.indexOf(Collections.min(dayCountList));
				lastObject = list.get(minIndex);
			}

			return new Response(SUCCESS, null, "Price is not set !!", null, lastObject);

		} catch (Exception e) {
			logger.error("getBasePriceOfServicesFromTariffMasterByServiceList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getUnitServiceTariffMasterList(UnitServiceTariffMasterDto unitServiceTariffMasterDto)
			throws ApplicationException {
		try {

			if (unitServiceTariffMasterDto.getTariffMasterList().size() > 0) {
				List<Integer> tariffMasterIds = new LinkedList<>();
				for (MultiSelectDto item : unitServiceTariffMasterDto.getTariffMasterList()) {
					tariffMasterIds.add(item.getId());
				}
				unitServiceTariffMasterDto.setTariffMasterIdList(tariffMasterIds);
			}

			if (unitServiceTariffMasterDto.getVisitTypeList().size() > 0) {
				List<Integer> visitTypeIds = new LinkedList<>();
				for (MultiSelectDto item : unitServiceTariffMasterDto.getVisitTypeList()) {
					visitTypeIds.add(item.getId());
				}
				unitServiceTariffMasterDto.setVisitTypeIdList(visitTypeIds);
			}

			if (unitServiceTariffMasterDto.getPatientTypeList().size() > 0) {
				List<Integer> patientTypeIds = new LinkedList<>();
				for (MultiSelectDto item : unitServiceTariffMasterDto.getPatientTypeList()) {
					patientTypeIds.add(item.getId());
				}
				unitServiceTariffMasterDto.setPatientTypeIdList(patientTypeIds);
			}

			if (unitServiceTariffMasterDto.getPaymentEntitlementList().size() > 0) {
				List<Integer> paymentEntitlementIds = new LinkedList<>();
				for (MultiSelectDto item : unitServiceTariffMasterDto.getPaymentEntitlementList()) {
					paymentEntitlementIds.add(item.getId());
				}
				unitServiceTariffMasterDto.setPaymentEntitlementIdList(paymentEntitlementIds);
			}

			if (unitServiceTariffMasterDto.getBedCategoryList().size() > 0) {
				List<Integer> bedCategoryIds = new LinkedList<>();
				for (MultiSelectDto item : unitServiceTariffMasterDto.getBedCategoryList()) {
					bedCategoryIds.add(item.getId());
				}
				unitServiceTariffMasterDto.setBedCategoryIdList(bedCategoryIds);
			}

			return iUnitServiceTariffMasterDao.getUnitServiceTariffMasterList(unitServiceTariffMasterDto);
		} catch (Exception e) {
			logger.error("getUnitServiceTariffMasterList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getProfitMarginList(UnitServiceTariffMasterDto unitServiceTariffMasterDto) {
		try {

			if (unitServiceTariffMasterDto.getTariffMasterList().size() > 0) {
				List<Integer> tariffMasterIds = new LinkedList<>();
				for (MultiSelectDto item : unitServiceTariffMasterDto.getTariffMasterList()) {
					tariffMasterIds.add(item.getId());
				}
				unitServiceTariffMasterDto.setTariffMasterIdList(tariffMasterIds);
			}

			if (unitServiceTariffMasterDto.getVisitTypeList().size() > 0) {
				List<Integer> visitTypeIds = new LinkedList<>();
				for (MultiSelectDto item : unitServiceTariffMasterDto.getVisitTypeList()) {
					visitTypeIds.add(item.getId());
				}
				unitServiceTariffMasterDto.setVisitTypeIdList(visitTypeIds);
			}

			if (unitServiceTariffMasterDto.getPatientTypeList().size() > 0) {
				List<Integer> patientTypeIds = new LinkedList<>();
				for (MultiSelectDto item : unitServiceTariffMasterDto.getPatientTypeList()) {
					patientTypeIds.add(item.getId());
				}
				unitServiceTariffMasterDto.setPatientTypeIdList(patientTypeIds);
			}

			if (unitServiceTariffMasterDto.getPaymentEntitlementList().size() > 0) {
				List<Integer> paymentEntitlementIds = new LinkedList<>();
				for (MultiSelectDto item : unitServiceTariffMasterDto.getPaymentEntitlementList()) {
					paymentEntitlementIds.add(item.getId());
				}
				unitServiceTariffMasterDto.setPaymentEntitlementIdList(paymentEntitlementIds);
			}

			if (unitServiceTariffMasterDto.getBedCategoryList().size() > 0) {
				List<Integer> bedCategoryIds = new LinkedList<>();
				for (MultiSelectDto item : unitServiceTariffMasterDto.getBedCategoryList()) {
					bedCategoryIds.add(item.getId());
				}
				unitServiceTariffMasterDto.setBedCategoryIdList(bedCategoryIds);
			}

			Response res = iUnitServiceTariffMasterDao.getProfitMarginList(unitServiceTariffMasterDto);
			if (res.getListObject() != null) {

				ListIterator<UnitServiceTariffMasterDto> iterator = res.getListObject().listIterator();
				while (iterator.hasNext()) {
					UnitServiceTariffMasterDto dto = iterator.next();
					dto.setProfit(
							dto.getServiceCost() != null ? (dto.getRate() - dto.getServiceCost()) : dto.getRate());
				}
				return res;
			}

		} catch (Exception e) {
			logger.error("getProfitMarginList failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response saveServiceProfitMargin(List<ServiceProfitMarginDto> serviceProfitMarginDto) {
		try {

			return iUnitServiceTariffMasterDao.saveServiceProfitMargin(serviceProfitMarginDto);
		} catch (Exception e) {
			logger.error("saveServiceProfitMargin failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response getServiceListBySubSpecialityId(UnitServiceTariffMasterDto unitServiceTariffMasterDto)
			throws ApplicationException {
		try {

			return iUnitServiceTariffMasterDao.getServiceListBySubSpecialityId(unitServiceTariffMasterDto);
		} catch (Exception e) {
			logger.error("getServiceListBySubSpecialityId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getServiceListBySpecialityIdSubSpecialityList(UnitServiceTariffMasterDto unitServiceTariffMasterDto)
			throws ApplicationException {
		try {

			return iUnitServiceTariffMasterDao.getServiceListBySpecialityIdSubSpecialityList(unitServiceTariffMasterDto);
		} catch (Exception e) {
			logger.error("getServiceListBySpecialityIdSubSpecialityList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response uniqueCheck(UnitServiceTariffMasterDto unitServiceTariffMasterDto) {
		try {
			int alreadyExistCount = 0;
			
			if (unitServiceTariffMasterDto.getPatientTypeList().size() > 0) 
			{
				List<Integer> patientTypeIds = new LinkedList<>();
				for (MultiSelectDto item : unitServiceTariffMasterDto.getPatientTypeList()) {
					patientTypeIds.add(item.getId());
				}
				unitServiceTariffMasterDto.setPatientTypeIdList(patientTypeIds);
			}
			if (unitServiceTariffMasterDto.getBedCategoryList().size() > 0) 
			{
				List<Integer> bedCategoryIds = new LinkedList<>();
				for (MultiSelectDto item : unitServiceTariffMasterDto.getBedCategoryList()) {
					bedCategoryIds.add(item.getId());
				}
				unitServiceTariffMasterDto.setBedCategoryIdList(bedCategoryIds);
			}

			
			Response res = iUnitServiceTariffMasterDao.getUniqueEffectiveDate(unitServiceTariffMasterDto);
			Integer count = (Integer)res.getObject();
			
			if(unitServiceTariffMasterDto.getAlreadyExist()==0){
				if(count!=null && count.intValue()>0 ){
					return new Response(ERROR, null, ALREADY_EXIST, unitServiceTariffMasterDto.getUnitServiceTariffMasterDtosList(), count,res.getListObject());	
				}
				else{
					return new Response(SUCCESS, null, null, unitServiceTariffMasterDto.getUnitServiceTariffMasterDtosList(), null);
				}
			}


		} catch (Exception e) {
			logger.error("uniqueCheck failed", e);
			throw new ServiceException(e.getMessage());
		}
		return new Response(ERROR, null, null, null, null);

	}

	@Override
	public Response saveUnitServiceTariffMaster(UnitServiceTariffMasterDto unitServiceTariffMasterDto) {
		try {

				Response res2 = iUnitServiceTariffMasterDao.saveUnitServiceTariffMaster(unitServiceTariffMasterDto.getUnitServiceTariffMasterDtosList());
				
				if(res2.getStatus().equals(SUCCESS) && unitServiceTariffMasterDto.getDuplicateIdsList()!=null && unitServiceTariffMasterDto.getDuplicateIdsList().size()>0)
					iUnitServiceTariffMasterDao.inactiveDuplicateIds(unitServiceTariffMasterDto.getDuplicateIdsList());
				
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);

			} catch (Exception e) {
				logger.error("saveUnitServiceTariffMaster failed", e);
				throw new ServiceException(e.getMessage());
			}

	}


	@Override
	public Response getServiceListBySubSpecialityIdNotByTariff(UnitServiceTariffMasterDto unitServiceTariffMasterDto) {
		try {
			return iUnitServiceTariffMasterDao.getServiceListBySubSpecialityIdNotByTariff(unitServiceTariffMasterDto);
		} catch (Exception e) {
			logger.error("getServiceListBySubSpecialityIdNotByTariff failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response searchServicesByNameAndCode(ServiceMasterDto serviceMasterDto) {
		try {
				Response res = iUnitServiceTariffMasterDao.searchServicesByNameAndCode(serviceMasterDto);
				List<ServiceSearchResDto> finalListServiceMasterDto =new LinkedList<>();
				List<ServiceSearchResDto> listServiceMasterDto = res.getListObject();
				Integer id=0;
				for(ServiceSearchResDto dto : listServiceMasterDto)
				{
					if(!(id.equals(dto.getServiceMasterId())))
					{
						finalListServiceMasterDto.add(dto);
						id=dto.getServiceMasterId();
					}
				}
				
				return new Response(SUCCESS, SUCCESS_CODE, null, finalListServiceMasterDto, null);
		} catch (Exception e) {
			logger.error("searchServicesByNameAndCode failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	
	@Override
	public Response checkUnitServiceMasterMapping(List<Integer> serviceIds, Integer unitId)
			throws ApplicationException {
		try {
			return iUnitServiceMapperDao.getServiceListBySubSpecialityIdNotByTariff(serviceIds,unitId);
		} catch (Exception e) {
			logger.error("checkUnitServiceMasterMapping failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getUnitServiceTariffMasterListForWeb(UnitServiceTariffMasterDto unitServiceTariffMasterDto) {
		{
			try {
				return iUnitServiceTariffMasterDao.getUnitServiceTariffMasterListForWeb(unitServiceTariffMasterDto);
			} catch (Exception e) {
				logger.error("getUnitServiceTariffMasterListForWeb failed", e);
				throw new ServiceException(e.getMessage());
			}
		}
	}
}
