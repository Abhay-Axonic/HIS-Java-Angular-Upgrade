package com.param.global.billing.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IVendorServiceRateConfigDao;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.MultiSelectDto;
import com.param.global.dto.VendorServiceRateConfigDto;


@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class VendorServiceRateConfigServiceImpl implements IVendorServiceRateConfigService,ICommonConstants{

	final static Logger logger = Logger.getLogger(VendorServiceRateConfigServiceImpl.class);

	@Autowired
	IVendorServiceRateConfigDao iVendorServiceRateConfigDao;
	
	
	@Override
	@Transactional
	public Response saveVendorServiceRateConfig(VendorServiceRateConfigDto vendorServiceRateConfigDto) {
		try {

			Response res2 = iVendorServiceRateConfigDao.saveVendorServiceRateConfig(vendorServiceRateConfigDto.getVendorServiceRateConfigDtosList());
			
			if(res2.getStatus().equals(SUCCESS) && vendorServiceRateConfigDto.getDuplicateIdsList()!=null && vendorServiceRateConfigDto.getDuplicateIdsList().size()>0)
				iVendorServiceRateConfigDao.inactiveDuplicateIds(vendorServiceRateConfigDto.getDuplicateIdsList());
			
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);

		} catch (Exception e) {
			logger.error("saveVendorServiceRateConfig failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response uniqueCheck(VendorServiceRateConfigDto vendorServiceRateConfigDto) {
		try {

			if (vendorServiceRateConfigDto.getVendorsList().size() > 0) {
				List<Integer> vendorIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getVendorsList()) {
					vendorIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setVenderIdList(vendorIds);
			}

			if (vendorServiceRateConfigDto.getVisitTypeList().size() > 0) {
				List<Integer> visitTypeIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getVisitTypeList()) {
					visitTypeIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setVisitTypeIdList(visitTypeIds);
			}

			if (vendorServiceRateConfigDto.getPatientTypeList().size() > 0) {
				List<Integer> patientTypeIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getPatientTypeList()) {
					patientTypeIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setPatientTypeIdList(patientTypeIds);
			}

			if (vendorServiceRateConfigDto.getPaymentEntitlementList().size() > 0) {
				List<Integer> paymentEntitlementIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getPaymentEntitlementList()) {
					paymentEntitlementIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setPaymentEntitlementIdList(paymentEntitlementIds);
			}

			if (vendorServiceRateConfigDto.getBedCategoryList().size() > 0) {
				List<Integer> bedCategoryIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getBedCategoryList()) {
					bedCategoryIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setBedCategoryIdList(bedCategoryIds);
			}
			
			if (vendorServiceRateConfigDto.getServiceList().size() > 0) {
				List<Integer> serviceIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getServiceList()) {
					serviceIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setListServiceId(serviceIds);
			}
			
			if (vendorServiceRateConfigDto.getPackageList()!=null && vendorServiceRateConfigDto.getPackageList().size() > 0) {
				List<Integer> packageIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getPackageList()) {
					packageIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setPackageIdList(packageIds);
			}
			
			Response res = iVendorServiceRateConfigDao.getUniqueEffectiveDate(vendorServiceRateConfigDto);
			Integer count = (Integer)res.getObject();
			
			if(count!=null && count.intValue()>0 ){
				return new Response(ERROR, null, ALREADY_EXIST, vendorServiceRateConfigDto.getVendorServiceRateConfigDtosList(), count,res.getListObject());	
			}
			else{
				return new Response(SUCCESS, null, null, vendorServiceRateConfigDto.getVendorServiceRateConfigDtosList(), null);
			}
		} catch (Exception e) {
			logger.error("uniqueCheck failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response getVendorServiceRateConfigList(VendorServiceRateConfigDto vendorServiceRateConfigDto) {
		try {

			if (vendorServiceRateConfigDto.getVendorsList().size() > 0) {
				List<Integer> vendorIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getVendorsList()) {
					vendorIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setVenderIdList(vendorIds);
			}

			if (vendorServiceRateConfigDto.getVisitTypeList().size() > 0) {
				List<Integer> visitTypeIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getVisitTypeList()) {
					visitTypeIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setVisitTypeIdList(visitTypeIds);
			}

			if (vendorServiceRateConfigDto.getPatientTypeList().size() > 0) {
				List<Integer> patientTypeIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getPatientTypeList()) {
					patientTypeIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setPatientTypeIdList(patientTypeIds);
			}

			if (vendorServiceRateConfigDto.getPaymentEntitlementList().size() > 0) {
				List<Integer> paymentEntitlementIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getPaymentEntitlementList()) {
					paymentEntitlementIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setPaymentEntitlementIdList(paymentEntitlementIds);
			}

			if (vendorServiceRateConfigDto.getBedCategoryList().size() > 0) {
				List<Integer> bedCategoryIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getBedCategoryList()) {
					bedCategoryIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setBedCategoryIdList(bedCategoryIds);
			}
			
			if (vendorServiceRateConfigDto.getServiceList().size() > 0) {
				List<Integer> serviceIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getServiceList()) {
					serviceIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setListServiceId(serviceIds);
			}

			if (vendorServiceRateConfigDto.getPackageList()!=null && vendorServiceRateConfigDto.getPackageList().size() > 0) {
				List<Integer> packageIds = new LinkedList<>();
				for (MultiSelectDto item : vendorServiceRateConfigDto.getPackageList()) {
					packageIds.add(item.getId());
				}
				vendorServiceRateConfigDto.setPackageIdList(packageIds);
			}
			
			return iVendorServiceRateConfigDao.getVendorServiceRateConfigList(vendorServiceRateConfigDto);
		} catch (Exception e) {
			logger.error("getVendorServiceRateConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response generateVendorServiceRateConfigList(VendorServiceRateConfigDto vendorServiceRateConfigDto) {
		try {

			if(vendorServiceRateConfigDto.getPackageList()!=null && vendorServiceRateConfigDto.getPackageList().size()>0)
			{
				if (vendorServiceRateConfigDto.getVisitTypeList().size() > 0) {
					List<Integer> visitTypeIds = new LinkedList<>();
					for (MultiSelectDto item : vendorServiceRateConfigDto.getVisitTypeList()) {
						visitTypeIds.add(item.getId());
					}
					vendorServiceRateConfigDto.setVisitTypeIdList(visitTypeIds);
				}

				if (vendorServiceRateConfigDto.getPatientTypeList().size() > 0) {
					List<Integer> patientTypeIds = new LinkedList<>();
					for (MultiSelectDto item : vendorServiceRateConfigDto.getPatientTypeList()) {
						patientTypeIds.add(item.getId());
					}
					vendorServiceRateConfigDto.setPatientTypeIdList(patientTypeIds);
				}

				if (vendorServiceRateConfigDto.getPaymentEntitlementList().size() > 0) {
					List<Integer> paymentEntitlementIds = new LinkedList<>();
					for (MultiSelectDto item : vendorServiceRateConfigDto.getPaymentEntitlementList()) {
						paymentEntitlementIds.add(item.getId());
					}
					vendorServiceRateConfigDto.setPaymentEntitlementIdList(paymentEntitlementIds);
				}

				if (vendorServiceRateConfigDto.getBedCategoryList().size() > 0) {
					List<Integer> bedCategoryIds = new LinkedList<>();
					for (MultiSelectDto item : vendorServiceRateConfigDto.getBedCategoryList()) {
						bedCategoryIds.add(item.getId());
					}
					vendorServiceRateConfigDto.setBedCategoryIdList(bedCategoryIds);
				}
				
				if (vendorServiceRateConfigDto.getServiceList().size() > 0) {
					List<Integer> serviceIds = new LinkedList<>();
					for (MultiSelectDto item : vendorServiceRateConfigDto.getServiceList()) {
						serviceIds.add(item.getId());
					}
					vendorServiceRateConfigDto.setListServiceId(serviceIds);
				}

				if (vendorServiceRateConfigDto.getPackageList().size() > 0) {
					List<Integer> packageIds = new LinkedList<>();
					for (MultiSelectDto item : vendorServiceRateConfigDto.getPackageList()) {
						packageIds.add(item.getId());
					}
					vendorServiceRateConfigDto.setPackageIdList(packageIds);
				}
				
				List<VendorServiceRateConfigDto> finalConfigDtosList = new ArrayList<>();
				
				List<VendorServiceRateConfigDto> configDtosList = iVendorServiceRateConfigDao.getEHCPackageServicesForGeneratingList(vendorServiceRateConfigDto);

				if(configDtosList.size() > 0 && vendorServiceRateConfigDto.getVendorsList().size()>0)
				{
					ListIterator<MultiSelectDto> vendorItr = vendorServiceRateConfigDto.getVendorsList().listIterator();
					while (vendorItr.hasNext()) 
					{
						MultiSelectDto serviceDto = vendorItr.next();
						int vendorId = serviceDto.getId();
						String vendorDesc = serviceDto.getLable();
					
						for(VendorServiceRateConfigDto configDto : configDtosList)
						{	
							VendorServiceRateConfigDto vendorServiceConfig = new VendorServiceRateConfigDto();
								vendorServiceConfig.setVendorId(vendorId);
								vendorServiceConfig.setVendorName(vendorDesc);
								vendorServiceConfig.setPaymentEntitlementId(configDto.getPaymentEntitlementId());
								vendorServiceConfig.setPaymentEntitlementDesc(configDto.getPaymentEntitlementDesc());
								vendorServiceConfig.setPatientTypeId(configDto.getPatientTypeId());
								vendorServiceConfig.setPatientTypeDesc(configDto.getPatientTypeDesc());
								vendorServiceConfig.setVisitTypeId(configDto.getVisitTypeId());
								vendorServiceConfig.setVisitTypeDesc(configDto.getVisitTypeDesc());
								vendorServiceConfig.setBillingBedCatId(configDto.getBillingBedCatId());
								vendorServiceConfig.setBillingBedCatDesc(configDto.getBillingBedCatDesc());
								vendorServiceConfig.setServiceId(configDto.getServiceId());
								vendorServiceConfig.setServiceDesc(configDto.getServiceDesc());
								vendorServiceConfig.setPackageId(configDto.getPackageId());
								vendorServiceConfig.setPackageDesc(configDto.getPackageDesc());
								vendorServiceConfig.setRate(0d);
							finalConfigDtosList.add(vendorServiceConfig);
						}
					}
				}else
					return new Response(ERROR, null,"Select atleast one Services OR vendor", null, null);

				return new Response(SUCCESS, null, null, finalConfigDtosList, null);
			}else{
				
				Integer venderId = null;
				String venderDesc = null;
				Integer paymentEntitlementId = null;
				String paymentEntitlementDesc = null;
				Integer patientTypeId = null;
				String patientTypeDesc = null;
				Integer visitTypeId = null;
				String visitTypeDesc = null;
				Integer billingBedCatId = null;
				String billingBedCatDesc = null;
				Integer serviceId = null;
				String serviceDesc = null;

				List<VendorServiceRateConfigDto> list = new ArrayList<>();

				ListIterator<MultiSelectDto> vendorItr = vendorServiceRateConfigDto.getVendorsList().listIterator();
				while (vendorItr.hasNext()) {
					MultiSelectDto tariffItrDto = vendorItr.next();
					venderId = tariffItrDto.getId();
					venderDesc = tariffItrDto.getLable();

					ListIterator<MultiSelectDto> visitTypeItr = vendorServiceRateConfigDto.getVisitTypeList()
							.listIterator();
					while (visitTypeItr.hasNext()) {
						MultiSelectDto visitTypeItrDto = visitTypeItr.next();
						visitTypeId = visitTypeItrDto.getId();
						visitTypeDesc = visitTypeItrDto.getLable();

						ListIterator<MultiSelectDto> paymentEntitlementItr = vendorServiceRateConfigDto.getPaymentEntitlementList().listIterator();
						while (paymentEntitlementItr.hasNext()) {
							MultiSelectDto paymentEntitlementItrDto = paymentEntitlementItr.next();
							paymentEntitlementId = paymentEntitlementItrDto.getId();
							paymentEntitlementDesc = paymentEntitlementItrDto.getLable();

							ListIterator<MultiSelectDto> patientTypeItr = vendorServiceRateConfigDto.getPatientTypeList()
									.listIterator();
							while (patientTypeItr.hasNext()) {
								MultiSelectDto patientTypeItrDto = patientTypeItr.next();
								patientTypeId = patientTypeItrDto.getId();
								patientTypeDesc = patientTypeItrDto.getLable();

								ListIterator<MultiSelectDto> billingBedCatItr = vendorServiceRateConfigDto.getBedCategoryList().listIterator();
								while (billingBedCatItr.hasNext()) {
									MultiSelectDto billingBedCatItrDto = billingBedCatItr.next();
									billingBedCatId = billingBedCatItrDto.getId();
									billingBedCatDesc = billingBedCatItrDto.getLable();

									if (vendorServiceRateConfigDto.getServiceList().size() > 0)
									{
										ListIterator<MultiSelectDto> serviceItr = vendorServiceRateConfigDto.getServiceList().listIterator();
										while (serviceItr.hasNext()) {
											MultiSelectDto serviceDto = serviceItr.next();
											serviceId = serviceDto.getId();
											serviceDesc = serviceDto.getLable();

											VendorServiceRateConfigDto serviceTariffMasterDto = new VendorServiceRateConfigDto();
												serviceTariffMasterDto.setVendorId(venderId);
												serviceTariffMasterDto.setVendorName(venderDesc);
												serviceTariffMasterDto.setPaymentEntitlementId(paymentEntitlementId);
												serviceTariffMasterDto.setPaymentEntitlementDesc(paymentEntitlementDesc);
												serviceTariffMasterDto.setPatientTypeId(patientTypeId);
												serviceTariffMasterDto.setPatientTypeDesc(patientTypeDesc);
												serviceTariffMasterDto.setVisitTypeId(visitTypeId);
												serviceTariffMasterDto.setVisitTypeDesc(visitTypeDesc);
												serviceTariffMasterDto.setBillingBedCatId(billingBedCatId);
												serviceTariffMasterDto.setBillingBedCatDesc(billingBedCatDesc);
												serviceTariffMasterDto.setServiceId(serviceId);
												serviceTariffMasterDto.setServiceDesc(serviceDesc);
												serviceTariffMasterDto.setPackageId(0);
												serviceTariffMasterDto.setPackageDesc("-");
												serviceTariffMasterDto.setRate(0d);
											list.add(serviceTariffMasterDto);

										}
									}else
										return new Response(ERROR, null,"Select atleast Speciality OR SubSpeciality OR Services", null, null);

								}
							}
						}

					}
				}
				return new Response(SUCCESS, null, null, list, null);
			}
		} catch (Exception e) {
			logger.error("generateVendorServiceRateConfigList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	@Transactional
	public Response getEHCPackageList(VendorServiceRateConfigDto vendorServiceRateConfigDto) {
		
		if (vendorServiceRateConfigDto.getVisitTypeList().size() > 0) {
			List<Integer> visitTypeIds = new LinkedList<>();
			for (MultiSelectDto item : vendorServiceRateConfigDto.getVisitTypeList()) {
				visitTypeIds.add(item.getId());
			}
			vendorServiceRateConfigDto.setVisitTypeIdList(visitTypeIds);
		}

		if (vendorServiceRateConfigDto.getPatientTypeList().size() > 0) {
			List<Integer> patientTypeIds = new LinkedList<>();
			for (MultiSelectDto item : vendorServiceRateConfigDto.getPatientTypeList()) {
				patientTypeIds.add(item.getId());
			}
			vendorServiceRateConfigDto.setPatientTypeIdList(patientTypeIds);
		}

		if (vendorServiceRateConfigDto.getPaymentEntitlementList().size() > 0) {
			List<Integer> paymentEntitlementIds = new LinkedList<>();
			for (MultiSelectDto item : vendorServiceRateConfigDto.getPaymentEntitlementList()) {
				paymentEntitlementIds.add(item.getId());
			}
			vendorServiceRateConfigDto.setPaymentEntitlementIdList(paymentEntitlementIds);
		}

		if (vendorServiceRateConfigDto.getBedCategoryList().size() > 0) {
			List<Integer> bedCategoryIds = new LinkedList<>();
			for (MultiSelectDto item : vendorServiceRateConfigDto.getBedCategoryList()) {
				bedCategoryIds.add(item.getId());
			}
			vendorServiceRateConfigDto.setBedCategoryIdList(bedCategoryIds);
		}
		
		return iVendorServiceRateConfigDao.getEHCPackageList(vendorServiceRateConfigDto);
	}


	@Override
	@Transactional
	public Response getEHCPackageServices(VendorServiceRateConfigDto vendorServiceRateConfigDto) {
		
		if (vendorServiceRateConfigDto.getPackageList().size() > 0) {
			List<Integer> packageIds = new LinkedList<>();
			for (MultiSelectDto item : vendorServiceRateConfigDto.getPackageList()) {
				packageIds.add(item.getId());
			}
			vendorServiceRateConfigDto.setPackageIdList(packageIds);
		}
		
		return iVendorServiceRateConfigDao.getEHCPackageServices(vendorServiceRateConfigDto);
	}
}
