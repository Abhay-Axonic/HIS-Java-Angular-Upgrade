    package com.param.global.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.param.global.common.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.billing.packages.model.TPackageConsumptionMaster;
import com.param.entity.model.master.Tax;
import com.param.global.billing.controller.BillingException;
import com.param.global.billing.dao.IContractConsumptionDao;
import com.param.global.billing.dto.AdmissionListIpdChargesResDto;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.billing.dto.PharmacyBillCommonDto;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.dao.IMPackageMasterDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IPackageConsumptionDao;
import com.param.global.dao.ITPackageBedCategoryDetailDao;
import com.param.global.dao.ITPackageCapDetailsDao;
import com.param.global.dao.ITPackageCategoryWiseConsumableDetailsDao;
import com.param.global.dao.ITPackageConsumptionCapDetailsDao;
import com.param.global.dao.ITPackageConsumptionEoDetailsDao;
import com.param.global.dao.ITPackageConsumptionMasterDao;
import com.param.global.dao.ITPackageConsumptionServiceDetailDao;
import com.param.global.dao.ITPackageEitherOrGroupDetailsDao;
import com.param.global.dao.ITPackageIncExcDetailsDao;
import com.param.global.dao.ITPackageServicesDetailsDao;
import com.param.global.dao.IUnitServiceMapperDao;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.MPackageMasterDto;
import com.param.global.dto.MultiSelectObj;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.PackaageResDto;
import com.param.global.dto.PackageBillingBedCategory;
import com.param.global.dto.PackageConsumptionEODetailDto;
import com.param.global.dto.PackageConsumptionMasterResDto;
import com.param.global.dto.PackageConsumptionServiceDetailDto;
import com.param.global.dto.PackageListForOpBillResDto;
import com.param.global.dto.PackageListReqDto;
import com.param.global.dto.PackageMultyServiceSearchReqDto;
import com.param.global.dto.PackagePatientCategory;
import com.param.global.dto.PackagePaymentEntitlement;
import com.param.global.dto.PackageSearchRequestDto;
import com.param.global.dto.PackageServiceCheckReqDto;
import com.param.global.dto.ParentPackageMasterDto;
import com.param.global.dto.ServiceForPackageReqDto;
import com.param.global.dto.ServiceForPackageResDto;
import com.param.global.dto.ServiceSearchReqDto;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.TPackageBedCategoryDetailDto;
import com.param.global.dto.TPackageCapDetailsDto;
import com.param.global.dto.TPackageCategoryWiseConsumableDetailsDto;
import com.param.global.dto.TPackageConsumptionCapDetailsDto;
import com.param.global.dto.TPackageConsumptionEoDetailsDto;
import com.param.global.dto.TPackageConsumptionMasterDto;
import com.param.global.dto.TPackageConsumptionServiceDetailDto;
import com.param.global.dto.TPackageEitherorGroupDetailsDto;
import com.param.global.dto.TPackageIncExcDetailsDto;
import com.param.global.dto.TPackageServicesDetailsDto;
import com.param.global.dto.UnitServiceTariffMasterDto;
import com.param.global.model.OrderDetailsMaster;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.pharmacy.dao.IWalkInSalesDao;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.opd.encounter.dto.EncounterEhcConsultationMasterDto;
import com.param.opd.encounter.dto.EncounterEhcConsultationMpprDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.service.global.SessionService;
import com.param.service.global.model.MPackageMaster;
import com.param.service.global.model.ParentPackageMaster;
import com.param.service.global.model.TPackageEitherorGroupDetails;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class MPackageMasterServiceImpl implements IMPackageMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(MPackageMasterServiceImpl.class);


	@Autowired
	private IMPackageMasterDao iMPackageMasterDao;
	@Autowired
	private ITPackageServicesDetailsDao iTPackageServicesDetailsDao;
	@Autowired
	private ITPackageEitherOrGroupDetailsDao iTPackageEitherOrGroupDetailsDao;
	@Autowired
	private ITPackageCapDetailsDao iTPackageCapDetailsDao;
	@Autowired
	private ITPackageIncExcDetailsDao iTPackageIncExcDetailsDao;
	@Autowired
	private ITPackageBedCategoryDetailDao iTPackageBedCategoryDetailDao;
	@Autowired
	private ITPackageCategoryWiseConsumableDetailsDao iTPackageCategoryWiseConsumableDetailsDao;
	@Autowired
	private ITPackageConsumptionMasterDao iTPackageConsumptionMasterDao;
	@Autowired
	private IPackageConsumptionDao iPackageConsumptionDao;
	@Autowired
	private IUnitServiceMapperDao iUnitServiceMapperDao;
	@Autowired
	private ITPackageConsumptionEoDetailsDao iTPackageConsumptionEoDetailsDao;
	@Autowired
	private ITPackageConsumptionServiceDetailDao iTPackageConsumptionServiceDetailDao;
	@Autowired
	private IUnitServiceTariffMasterService iUnitServiceTariffMasterDao;
	@Autowired
	IContractConsumptionDao iContractConsumptionDao;
	@Autowired
	ITPackageConsumptionCapDetailsDao iTPackageConsumptionCapDetailsDao;

	@Autowired
	IOrderMasterService iOrderMasterService;

	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;

	@Autowired
	IOrderDetailsMasterService iOrderDetailsMasterService;

	@Autowired
	IContractConsumptionService iContractConsumptionService;

	@Autowired
	IEncounterMasterDao iEncounterMasterDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private IWalkInSalesService iWalkInSalesService;

	@Autowired
	private IWalkInSalesDao iWalkInSalesDao;

	@Autowired
	private BillingCommonMethods billingCommonMethods;



	@Override
	@Transactional
	public Response createEhcPackage(ParentPackageMasterDto parentPackage) throws ServiceException {
		try {

			if (parentPackage.getParentPackageId() != null && parentPackage.getParentPackageId() > 0) {
				iMPackageMasterDao.inactivePacakge(parentPackage.getParentPackageId(),
						parentPackage.getPackageTypeId(),false,1);
			}
			Response parentPackageRes = iMPackageMasterDao.saveParentPackageMaster(parentPackage);
			if (parentPackageRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && parentPackageRes.getObject() != null) {
				ParentPackageMaster pPackageMst = (ParentPackageMaster) parentPackageRes.getObject();
				Integer parentPackageId  = pPackageMst.getParentPackageId();
				Integer versionId =  pPackageMst.getParentPackageId();

				List<MPackageMasterDto> packageMasterDtoList = parentPackage.getPackageMasterDtoList();
				if (packageMasterDtoList != null && packageMasterDtoList.size() > 0) {

					for (MPackageMasterDto mPackageMasterDto : packageMasterDtoList) {
						mPackageMasterDto.setParentPackageId(parentPackageId);
						mPackageMasterDto.setPackageTypeId(parentPackage.getPackageTypeId());
						mPackageMasterDto.setCreatedBy(parentPackage.getCreatedBy());
						mPackageMasterDto.setUpdatedBy(parentPackage.getCreatedBy());
						mPackageMasterDto.setServiceId(parentPackage.getServiceId());
						mPackageMasterDto.setMinAge(parentPackage.getMinAge());
						mPackageMasterDto.setMaxAge(parentPackage.getMaxAge());
						mPackageMasterDto.setVersionId(versionId);
						Response packageRes = iMPackageMasterDao.savePackageMaster(mPackageMasterDto);
						if (packageRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && packageRes.getObject() != null) {
							MPackageMaster packageMaster = (MPackageMaster) packageRes.getObject();
							Integer packageId = packageMaster.getPackageMasterId();
							ListIterator<TPackageServicesDetailsDto> servicDtlsItr = mPackageMasterDto
									.getListTPackageServicesDetailsDto().listIterator();
							TPackageServicesDetailsDto servicesDetailsDto = null;
							while (servicDtlsItr.hasNext()) {
								servicesDetailsDto = servicDtlsItr.next();
								servicesDetailsDto.setPackageId(packageId);
								servicesDetailsDto.setOrgnisationId(parentPackage.getOrgId());
								servicesDetailsDto.setUnitId(parentPackage.getUnitId());
								servicesDetailsDto.setParentPackageId(parentPackageId);
								servicesDetailsDto.setIsCap(1);
								iTPackageServicesDetailsDao.saveTPackageServicesDetails(servicesDetailsDto);
							}

						}
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, EHC_PACKAGE_CREATED_MSG, null, null);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response SaveAllInclusivePackage(ParentPackageMasterDto parentPackage) throws ServiceException {
		try {

			if (parentPackage.getParentPackageId() != null && parentPackage.getParentPackageId() > 0) {
				iMPackageMasterDao.inactivePacakge(parentPackage.getParentPackageId(),
						parentPackage.getPackageTypeId(),false,1);
			}

			Response parentPackageRes = iMPackageMasterDao.saveParentPackageMaster(parentPackage);
			if (parentPackageRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && parentPackageRes.getObject() != null) {
				ParentPackageMaster pPackageMst = (ParentPackageMaster) parentPackageRes.getObject();
				Integer parentPackageId  = pPackageMst.getParentPackageId();
				Integer versionId =  pPackageMst.getParentPackageId();

				List<MPackageMasterDto> packageMasterDtoList = parentPackage.getPackageMasterDtoList();
				if (packageMasterDtoList != null && packageMasterDtoList.size() > 0) {
					for (MPackageMasterDto mPackageMasterDto : packageMasterDtoList) {
						mPackageMasterDto.setParentPackageId(parentPackageId);
						mPackageMasterDto.setPackageTypeId(parentPackage.getPackageTypeId());
						mPackageMasterDto.setCreatedBy(parentPackage.getCreatedBy());
						mPackageMasterDto.setUpdatedBy(parentPackage.getCreatedBy());
						mPackageMasterDto.setServiceId(parentPackage.getServiceId());
						mPackageMasterDto.setVersionId(versionId);
						Response packageRes = iMPackageMasterDao.savePackageMaster(mPackageMasterDto);
						if (packageRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && packageRes.getObject() != null) {
							MPackageMaster packageMaster = (MPackageMaster) packageRes.getObject();
							Integer packageId = packageMaster.getPackageMasterId();

							ListIterator<TPackageIncExcDetailsDto> servicDtlsItr = mPackageMasterDto
									.getListTPackageIncExcServiceDetailsDto().listIterator();
							TPackageIncExcDetailsDto servicesDetailsDto = null;
							while (servicDtlsItr.hasNext()) {
								servicesDetailsDto = servicDtlsItr.next();
								servicesDetailsDto.setCreatedBy(parentPackage.getCreatedBy());
								servicesDetailsDto.setUpdatedBy(parentPackage.getCreatedBy());
								servicesDetailsDto.setPackageId(packageId);
								servicesDetailsDto.setOrgId(parentPackage.getOrgId());
								servicesDetailsDto.setOrgUnitId(parentPackage.getUnitId());
								servicesDetailsDto.setParentPackageId(parentPackageId);
								iTPackageIncExcDetailsDao.saveTPackageIncExcDetails(servicesDetailsDto);
							}

							ListIterator<TPackageIncExcDetailsDto> itemDtlsItr = mPackageMasterDto
									.getListTPackageIncExcItemDetailsDto().listIterator();
							TPackageIncExcDetailsDto itemDetailsDto = null;
							while (itemDtlsItr.hasNext()) {
								itemDetailsDto = itemDtlsItr.next();
								itemDetailsDto.setCreatedBy(parentPackage.getCreatedBy());
								itemDetailsDto.setUpdatedBy(parentPackage.getCreatedBy());
								itemDetailsDto.setPackageId(packageId);
								itemDetailsDto.setOrgId(parentPackage.getOrgId());
								itemDetailsDto.setOrgUnitId(parentPackage.getUnitId());
								itemDetailsDto.setParentPackageId(parentPackageId);
								iTPackageIncExcDetailsDao.saveTPackageIncExcDetails(itemDetailsDto);
							}

						}
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, ALL_INCLUSIVE_PACKAGE_CREATED, null, null);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response createMultiencouterPackage(ParentPackageMasterDto parentPackage) throws ServiceException {
		try {

			if (parentPackage.getParentPackageId() != null && parentPackage.getParentPackageId() > 0) {
				iMPackageMasterDao.inactivePacakge(parentPackage.getParentPackageId(),
						parentPackage.getPackageTypeId(),false,1);
			}
			Response parentPackageRes = iMPackageMasterDao.saveParentPackageMaster(parentPackage);
			if (parentPackageRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && parentPackageRes.getObject() != null) {

				ParentPackageMaster pPackageMst = (ParentPackageMaster) parentPackageRes.getObject();
				Integer parentPackageId  = pPackageMst.getParentPackageId();
				Integer versionId =  pPackageMst.getParentPackageId();

				List<MPackageMasterDto> packageMasterDtoList = parentPackage.getPackageMasterDtoList();
				if (packageMasterDtoList != null && packageMasterDtoList.size() > 0) {
					for (MPackageMasterDto mPackageMasterDto : packageMasterDtoList) {
						mPackageMasterDto.setParentPackageId(parentPackageId);
						mPackageMasterDto.setPackageTypeId(parentPackage.getPackageTypeId());
						mPackageMasterDto.setCreatedBy(parentPackage.getCreatedBy());
						mPackageMasterDto.setUpdatedBy(parentPackage.getCreatedBy());
						mPackageMasterDto.setServiceId(parentPackage.getServiceId());
						mPackageMasterDto.setVersionId(versionId);
						//mPackageMasterDto.setNoOfEncounters(parentPackage.getNoOfEncounters());
						Response packageRes = iMPackageMasterDao.savePackageMaster(mPackageMasterDto);
						if (packageRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && packageRes.getObject() != null) {
							MPackageMaster packageMaster = (MPackageMaster) packageRes.getObject();
							Integer packageId = packageMaster.getPackageMasterId();

							// save service wise details
							if (mPackageMasterDto.getListTPackageServicesDetailsDto() != null
									&& mPackageMasterDto.getListTPackageServicesDetailsDto().size() > 0) {
								ListIterator<TPackageServicesDetailsDto> serviceWiseItr = mPackageMasterDto
										.getListTPackageServicesDetailsDto().listIterator();
								TPackageServicesDetailsDto serviceDetails = null;
								while (serviceWiseItr.hasNext()) {
									serviceDetails = serviceWiseItr.next();
									serviceDetails.setPackageId(packageId);
									serviceDetails.setOrgnisationId(parentPackage.getOrgId());
									serviceDetails.setUnitId(parentPackage.getUnitId());
									serviceDetails.setParentPackageId(parentPackageId);
									serviceDetails.setIsCap(1);
									iTPackageServicesDetailsDao.saveTPackageServicesDetails(serviceDetails);
								}
							}
							// save item wise details
							if (mPackageMasterDto.getListTPackageItemDetailsDto() != null
									&& mPackageMasterDto.getListTPackageItemDetailsDto().size() > 0) {
								ListIterator<TPackageServicesDetailsDto> itemWiseItr = mPackageMasterDto
										.getListTPackageItemDetailsDto().listIterator();
								TPackageServicesDetailsDto itemDetails = null;
								while (itemWiseItr.hasNext()) {
									itemDetails = itemWiseItr.next();
									itemDetails.setPackageId(packageId);
									itemDetails.setOrgnisationId(parentPackage.getOrgId());
									itemDetails.setUnitId(parentPackage.getUnitId());
									itemDetails.setParentPackageId(parentPackageId);
									itemDetails.setIsCap(1);
									iTPackageServicesDetailsDao.saveTPackageServicesDetails(itemDetails);
								}
							}
						}
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, MULTIENCOUNTER_PACKAGE_CREATED, null, null);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response createEitherOrPackage(ParentPackageMasterDto parentPackage) throws ServiceException {
		try {

			if (parentPackage.getParentPackageId() != null && parentPackage.getParentPackageId() > 0) {
				iMPackageMasterDao.inactivePacakge(parentPackage.getParentPackageId(),
						parentPackage.getPackageTypeId(),false,1);
			}
			Response parentPackageRes = iMPackageMasterDao.saveParentPackageMaster(parentPackage);
			if (parentPackageRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && parentPackageRes.getObject() != null) {

				ParentPackageMaster pPackageMst = (ParentPackageMaster) parentPackageRes.getObject();
				Integer parentPackageId  = pPackageMst.getParentPackageId();
				Integer versionId =  pPackageMst.getParentPackageId();

				List<MPackageMasterDto> packageMasterDtoList = parentPackage.getPackageMasterDtoList();
				if (packageMasterDtoList != null && packageMasterDtoList.size() > 0) {
					for (MPackageMasterDto mPackageMasterDto : packageMasterDtoList) {
						mPackageMasterDto.setParentPackageId(parentPackageId);
						mPackageMasterDto.setPackageTypeId(parentPackage.getPackageTypeId());
						mPackageMasterDto.setCreatedBy(parentPackage.getCreatedBy());
						mPackageMasterDto.setUpdatedBy(parentPackage.getCreatedBy());
						mPackageMasterDto.setServiceId(parentPackage.getServiceId());
						mPackageMasterDto.setIsManualRoundingIsAllow('Y');
						mPackageMasterDto.setVersionId(versionId);
						Response packageRes = iMPackageMasterDao.savePackageMaster(mPackageMasterDto);
						if (packageRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && packageRes.getObject() != null) {
							MPackageMaster packageMaster = (MPackageMaster) packageRes.getObject();
							Integer packageId = packageMaster.getPackageMasterId();
							if(mPackageMasterDto.getListTPackageEitherorGroupDetailsDto()!=null && mPackageMasterDto.getListTPackageEitherorGroupDetailsDto().size()>0) {

							ListIterator<TPackageEitherorGroupDetailsDto> eitherOrGrpItr = mPackageMasterDto.getListTPackageEitherorGroupDetailsDto().listIterator();
							ListIterator<TPackageServicesDetailsDto> groupPackServItr = null;
								while (eitherOrGrpItr.hasNext()) {
									TPackageEitherorGroupDetailsDto groupDetailsDto = eitherOrGrpItr.next();
									groupDetailsDto.setOrgId(parentPackage.getOrgId());
									groupDetailsDto.setOrgUnitId(parentPackage.getUnitId());
									groupDetailsDto.setPackageId(packageId);
									groupDetailsDto.setParentPackageId(parentPackageId);
									Response eitherOrGrpRes = iTPackageEitherOrGroupDetailsDao.savePackageEitherOrGroupDetails(groupDetailsDto);
									if (eitherOrGrpRes.getCode().equalsIgnoreCase(SUCCESS_CODE)&& eitherOrGrpRes.getObject() != null) {
										TPackageEitherorGroupDetails eihterOrGrpDtls = (TPackageEitherorGroupDetails) eitherOrGrpRes.getObject();
										Integer packageGrpDtlsId = eihterOrGrpDtls.getPackageGroupDetailsId();
										groupPackServItr = groupDetailsDto.getListTPackageServicesDetailsDto().listIterator();
										// save group wise services
										TPackageServicesDetailsDto groupServiceDetailsDto = null;
										while (groupPackServItr.hasNext()) {
											groupServiceDetailsDto = groupPackServItr.next();
											groupServiceDetailsDto.setPackageEitherOrGroupId(packageGrpDtlsId);
											groupServiceDetailsDto.setOrgnisationId(parentPackage.getOrgId());
											groupServiceDetailsDto.setUnitId(parentPackage.getUnitId());
											groupServiceDetailsDto.setPackageId(packageId);
											groupServiceDetailsDto.setParentPackageId(parentPackageId);
											groupServiceDetailsDto.setIsCap(1);
											iTPackageServicesDetailsDao.saveTPackageServicesDetails(groupServiceDetailsDto);
										}
									}
								}

							}
							// save package wise services
							if(mPackageMasterDto.getListTPackageServicesDetailsDto()!=null && mPackageMasterDto.getListTPackageServicesDetailsDto().size()>0) {
								ListIterator<TPackageServicesDetailsDto> servicDtlsItr = mPackageMasterDto
										.getListTPackageServicesDetailsDto().listIterator();
								TPackageServicesDetailsDto servicesDetailsDto = null;
								while (servicDtlsItr.hasNext()) {
									servicesDetailsDto = servicDtlsItr.next();
									servicesDetailsDto.setPackageId(packageId);
									servicesDetailsDto.setOrgnisationId(parentPackage.getOrgId());
									servicesDetailsDto.setUnitId(parentPackage.getUnitId());
									servicesDetailsDto.setParentPackageId(parentPackageId);
									servicesDetailsDto.setIsCap(1);
									iTPackageServicesDetailsDao.saveTPackageServicesDetails(servicesDetailsDto);
								}
							}
						}
					}

				}
				return new Response<>(SUCCESS, SUCCESS_CODE, EITHER_OR_PACKAGE_CREATED, null, null);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response searchServiceForOrderSet(ServiceForPackageReqDto reqDto) throws ServiceException {
		try {
			return iMPackageMasterDao.searchServiceForOrderSet(reqDto);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response searchServiceForPackage(ServiceForPackageReqDto reqDto) throws ServiceException {
		try {
			Response dtoRes = iMPackageMasterDao.searchServiceForPackage(reqDto);
			if (dtoRes.getStatus() == SUCCESS) {
				int serviceId = 0;
				ServiceForPackageResDto masterDto = new ServiceForPackageResDto();
				List<ServiceForPackageResDto> finalList = new ArrayList<>();
				ListIterator<ServiceForPackageResDto> iterator = dtoRes.getListObject().listIterator();
				while (iterator.hasNext()) {
					masterDto = iterator.next();

					if (serviceId != masterDto.getServiceMasterId()) {
						finalList.add(masterDto);
					}

					serviceId = masterDto.getServiceMasterId();
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, finalList, null);
			}
			return dtoRes;
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	@Transactional
	public Response getEHCPackagesList(ServiceForPackageReqDto reqDto) throws ServiceException {
		try {
			return iMPackageMasterDao.getEHCPackagesList(reqDto);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response createPackageWithCap(ParentPackageMasterDto parentPackage) throws ServiceException {
		try {
			if (parentPackage.getParentPackageId() != null && parentPackage.getParentPackageId() > 0) {
				iMPackageMasterDao.inactivePacakge(parentPackage.getParentPackageId(),
						parentPackage.getPackageTypeId(),false,1);
			}
			Response parentPackageRes = iMPackageMasterDao.saveParentPackageMaster(parentPackage);
			if (parentPackageRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && parentPackageRes.getObject() != null) {
				ParentPackageMaster pPackageMst = (ParentPackageMaster) parentPackageRes.getObject();
				Integer parentPackageId  = pPackageMst.getParentPackageId();
				Integer versionId =  pPackageMst.getParentPackageId();
				List<MPackageMasterDto> packageMasterDtoList = parentPackage.getPackageMasterDtoList();
				if (packageMasterDtoList != null && packageMasterDtoList.size() > 0) {
					for (MPackageMasterDto mPackageMasterDto : packageMasterDtoList) {
						mPackageMasterDto.setParentPackageId(parentPackageId);
						mPackageMasterDto.setPackageTypeId(parentPackage.getPackageTypeId());
						mPackageMasterDto.setCreatedBy(parentPackage.getCreatedBy());
						mPackageMasterDto.setUpdatedBy(parentPackage.getCreatedBy());
						mPackageMasterDto.setServiceId(parentPackage.getServiceId());
						mPackageMasterDto.setIsManualRoundingIsAllow('Y');
						mPackageMasterDto.setVersionId(versionId);
						Response packageRes = iMPackageMasterDao.savePackageMaster(mPackageMasterDto);

						// save package master
						if (packageRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && packageRes.getObject() != null) {
							MPackageMaster packageMaster = (MPackageMaster) packageRes.getObject();
							Integer packageId = packageMaster.getPackageMasterId();

							// save service details
							if (mPackageMasterDto.getListTPackageServicesDetailsDto() != null
									&& mPackageMasterDto.getListTPackageServicesDetailsDto().size() > 0) {
								ListIterator<TPackageServicesDetailsDto> servicDtlsItr = mPackageMasterDto
										.getListTPackageServicesDetailsDto().listIterator();
								TPackageServicesDetailsDto servicesDetailsDto = null;
								while (servicDtlsItr.hasNext()) {
									servicesDetailsDto = servicDtlsItr.next();
									servicesDetailsDto.setPackageId(packageId);
									servicesDetailsDto.setOrgnisationId(parentPackage.getOrgId());
									servicesDetailsDto.setUnitId(parentPackage.getUnitId());
									servicesDetailsDto.setCreatedBy(parentPackage.getCreatedBy());
									servicesDetailsDto.setUpdatedBy(parentPackage.getCreatedBy());
									servicesDetailsDto.setParentPackageId(parentPackageId);
									servicesDetailsDto.setServiceCap(servicesDetailsDto.getServicePrice());
									iTPackageServicesDetailsDao.saveTPackageServicesDetails(servicesDetailsDto);
								}
							}

							if (mPackageMasterDto.getListTPackageItemDetailsDto() != null
									&& mPackageMasterDto.getListTPackageItemDetailsDto().size() > 0) {
								ListIterator<TPackageServicesDetailsDto> servicDtlsItr = mPackageMasterDto
										.getListTPackageItemDetailsDto().listIterator();
								TPackageServicesDetailsDto servicesDetailsDto = null;
								while (servicDtlsItr.hasNext()) {
									servicesDetailsDto = servicDtlsItr.next();
									servicesDetailsDto.setIsServiceItem(2);
									servicesDetailsDto.setPackageId(packageId);
									servicesDetailsDto.setOrgnisationId(parentPackage.getOrgId());
									servicesDetailsDto.setUnitId(parentPackage.getUnitId());
									servicesDetailsDto.setCreatedBy(parentPackage.getCreatedBy());
									servicesDetailsDto.setUpdatedBy(parentPackage.getCreatedBy());
									servicesDetailsDto.setParentPackageId(parentPackageId);
									servicesDetailsDto.setServiceCap(servicesDetailsDto.getApportionedPrice());
									iTPackageServicesDetailsDao.saveTPackageServicesDetails(servicesDetailsDto);
								}
							}

							// save group wise cap
							if (mPackageMasterDto.getListTPackageGroupWiseCapDetailsDto() != null
									&& mPackageMasterDto.getListTPackageGroupWiseCapDetailsDto().size() > 0) {
								ListIterator<TPackageCapDetailsDto> packCapItr = mPackageMasterDto
										.getListTPackageGroupWiseCapDetailsDto().listIterator();
								TPackageCapDetailsDto packageCapDetailsDto = null;
								while (packCapItr.hasNext()) {
									packageCapDetailsDto = packCapItr.next();
									packageCapDetailsDto.setPackageId(packageId);
									packageCapDetailsDto.setOrganisationId(parentPackage.getOrgId());
									packageCapDetailsDto.setUnitId(parentPackage.getUnitId());
									packageCapDetailsDto.setCreatedBy(parentPackage.getCreatedBy());
									packageCapDetailsDto.setParentPackageId(parentPackageId);
									iTPackageCapDetailsDao.saveTPackageCapDetails(packageCapDetailsDto);
								}
							}

							// save group wise inclusion and exclusion
							if (mPackageMasterDto.getListGroupWiseIncExc() != null
									&& mPackageMasterDto.getListGroupWiseIncExc().size() > 0) {
								ListIterator<TPackageIncExcDetailsDto> groupWiseIncExcItr = mPackageMasterDto
										.getListGroupWiseIncExc().listIterator();
								TPackageIncExcDetailsDto groupWiseIncExc = null;
								while (groupWiseIncExcItr.hasNext()) {
									groupWiseIncExc = groupWiseIncExcItr.next();
									groupWiseIncExc.setPackageId(packageId);
									groupWiseIncExc.setOrgId(parentPackage.getOrgId());
									groupWiseIncExc.setOrgUnitId(parentPackage.getUnitId());
									groupWiseIncExc.setCreatedBy(parentPackage.getCreatedBy());
									groupWiseIncExc.setParentPackageId(parentPackageId);
									iTPackageIncExcDetailsDao.saveTPackageIncExcDetails(groupWiseIncExc);
								}
							}

							// save package bed category details
							if (mPackageMasterDto.getListTPackageBedCategoryDetailDto() != null
									&& mPackageMasterDto.getListTPackageBedCategoryDetailDto().size() > 0) {
								ListIterator<TPackageBedCategoryDetailDto> packageBedCatItr = mPackageMasterDto
										.getListTPackageBedCategoryDetailDto().listIterator();
								TPackageBedCategoryDetailDto bedCategoryDetailsDto = null;
								while (packageBedCatItr.hasNext()) {
									bedCategoryDetailsDto = packageBedCatItr.next();
									bedCategoryDetailsDto.setPackageId(packageId);
									bedCategoryDetailsDto.setOrgId(parentPackage.getOrgId());
									bedCategoryDetailsDto.setOrgUnitId(parentPackage.getUnitId());
									bedCategoryDetailsDto.setCreatedBy(parentPackage.getCreatedBy());
									bedCategoryDetailsDto.setBillingBedCategoryId(packageMaster.getBillingBedCategoryId());
									bedCategoryDetailsDto.setParentPackageId(parentPackageId);
									iTPackageBedCategoryDetailDao.saveTPackageBedCategoryDetail(bedCategoryDetailsDto);
								}
							}

							// save package consumable details
							if (mPackageMasterDto.getListTPackageConsumableDetailsDto() != null
									&& mPackageMasterDto.getListTPackageConsumableDetailsDto().size() > 0) {
								ListIterator<TPackageServicesDetailsDto> consumDtlsItr = mPackageMasterDto
										.getListTPackageConsumableDetailsDto().listIterator();
								TPackageServicesDetailsDto servicesDetailsDto = null;
								while (consumDtlsItr.hasNext()) {
									servicesDetailsDto = consumDtlsItr.next();
									servicesDetailsDto.setPackageId(packageId);
									servicesDetailsDto.setOrgnisationId(parentPackage.getOrgId());
									servicesDetailsDto.setUnitId(parentPackage.getUnitId());
									servicesDetailsDto.setCreatedBy(parentPackage.getCreatedBy());
									servicesDetailsDto.setParentPackageId(parentPackageId);
									iTPackageServicesDetailsDao.saveTPackageServicesDetails(servicesDetailsDto);
								}
							}

							// save category wise consumables
							if (mPackageMasterDto.getListTPackageCategoryWiseConsumableDetailsDto() != null
									&& mPackageMasterDto.getListTPackageCategoryWiseConsumableDetailsDto().size() > 0) {
								ListIterator<TPackageCategoryWiseConsumableDetailsDto> catWiseConsumDtlsItr = mPackageMasterDto
										.getListTPackageCategoryWiseConsumableDetailsDto().listIterator();
								TPackageCategoryWiseConsumableDetailsDto catWiseConsumDto = null;
								while (catWiseConsumDtlsItr.hasNext()) {
									catWiseConsumDto = catWiseConsumDtlsItr.next();
									catWiseConsumDto.setPackageId(packageId);
									catWiseConsumDto.setOrgId(parentPackage.getOrgId());
									catWiseConsumDto.setOrgUnitId(parentPackage.getUnitId());
									catWiseConsumDto.setCreatedBy(parentPackage.getCreatedBy());
									catWiseConsumDto.setParentPackageId(parentPackageId);
									iTPackageCategoryWiseConsumableDetailsDao
											.saveTPackageCategoryWiseConsumableDetails(catWiseConsumDto);
								}
							}

							// save consumable wise amount
							if (mPackageMasterDto.getListTPackageConsumableWiseCapDetailsDto() != null
									&& mPackageMasterDto.getListTPackageConsumableWiseCapDetailsDto().size() > 0) {
								ListIterator<TPackageCapDetailsDto> packConsumCapItr = mPackageMasterDto
										.getListTPackageConsumableWiseCapDetailsDto().listIterator();
								TPackageCapDetailsDto packConsumCap = null;
								while (packConsumCapItr.hasNext()) {
									packConsumCap = packConsumCapItr.next();
									packConsumCap.setPackageId(packageId);
									packConsumCap.setOrganisationId(parentPackage.getOrgId());
									packConsumCap.setUnitId(parentPackage.getUnitId());
									packConsumCap.setCreatedBy(parentPackage.getCreatedBy());
									packConsumCap.setParentPackageId(parentPackageId);
									iTPackageCapDetailsDao.saveTPackageCapDetails(packConsumCap);
								}
							}

							// save consumable wise inc exc
							if (mPackageMasterDto.getListConsumableWiseIncExc() != null
									&& mPackageMasterDto.getListConsumableWiseIncExc().size() > 0) {
								ListIterator<TPackageIncExcDetailsDto> consumWiseIncExcItr = mPackageMasterDto
										.getListConsumableWiseIncExc().listIterator();
								TPackageIncExcDetailsDto consumWiseIncExc = null;
								while (consumWiseIncExcItr.hasNext()) {
									consumWiseIncExc = consumWiseIncExcItr.next();
									consumWiseIncExc.setPackageId(packageId);
									consumWiseIncExc.setOrgId(parentPackage.getOrgId());
									consumWiseIncExc.setOrgUnitId(parentPackage.getUnitId());
									consumWiseIncExc.setCreatedBy(parentPackage.getCreatedBy());
									consumWiseIncExc.setParentPackageId(parentPackageId);
									iTPackageIncExcDetailsDao.saveTPackageIncExcDetails(consumWiseIncExc);
								}
							}
						}
					}
				}
				return new Response<>(SUCCESS, SUCCESS_CODE, PACKAGE_WITH_CAP_CREATED, null, null);
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		} catch (

		ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response searchServicesForBilling(ServiceSearchReqDto reqDto) throws ServiceException {
		try {
			return iMPackageMasterDao.searchServicesForBilling(reqDto);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	@Transactional
	public Response searchServicesForPackage(ServiceForPackageReqDto reqDto) throws ServiceException {
		try {
			return iMPackageMasterDao.searchServicesForPackage(reqDto);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getPackageDetailsByIdForBilling(List<Integer> packageIds) throws ServiceException {
		try {
			// get package details
			List<PackageListForOpBillResDto> listPackageListForOpBillResDto = new LinkedList<PackageListForOpBillResDto>();
			PackageListForOpBillResDto mPackage = null;
			Response packageServRes = null;
			Response packageCapRes = null;
			Response packageEoRes = null;
			Response packageIncExcRes = null;
			for (int packageId : packageIds) {
				Response packRes = iMPackageMasterDao.getPackageById(packageId);
				if (packRes.getCode().equals(SUCCESS_CODE) && packRes.getObject() != null) {
					MPackageMasterDto mPackageDto = (MPackageMasterDto) packRes.getObject();
					switch (mPackageDto.getPackageTypeId()) {
					case 1: // EHC package
						mPackage = new PackageListForOpBillResDto();
						mPackage.setmPackageMasterDto(mPackageDto);

						// get service details
						packageServRes = iTPackageServicesDetailsDao.getPackageServicesByPackageId(packageId);
						mPackage.setListTPackageServicesDetailsDto(packageServRes.getListObject());
						listPackageListForOpBillResDto.add(mPackage);
						break;
					case 2: // Either or Package
						mPackage = new PackageListForOpBillResDto();
						mPackage.setmPackageMasterDto(mPackageDto);

						// get eo details
						packageEoRes = iTPackageEitherOrGroupDetailsDao
								.getPackageEitherOrGroupDetailsByPackageId(packageId);
						mPackage.setListTPackageEitherorGroupDetailsDto(packageEoRes.getListObject());

						// get service details
						packageServRes = iTPackageServicesDetailsDao.getPackageServicesByPackageId(packageId);
						mPackage.setListTPackageServicesDetailsDto(packageServRes.getListObject());
						listPackageListForOpBillResDto.add(mPackage);
						break;
					case 3: // package with cap
						mPackage = new PackageListForOpBillResDto();
						mPackage.setmPackageMasterDto(mPackageDto);

						// get service details
						packageServRes = iTPackageServicesDetailsDao.getPackageServicesByPackageId(packageId);
						mPackage.setListTPackageServicesDetailsDto(packageServRes.getListObject());
						listPackageListForOpBillResDto.add(mPackage);

						// get cap details
						packageCapRes = iTPackageCapDetailsDao.getPackageCapDetailsByPackageIdForBilling(packageId);
						mPackage.setListTPackageGroupWiseCapDetailsDto(packageCapRes.getListObject());

						// get inclusion exclusion details
						packageIncExcRes = iTPackageIncExcDetailsDao
								.getTPackageIncExcDetailsForBillingByPackage(packageId);
						mPackage.setListTPackageIncExcDetailsDto(packageIncExcRes.getListObject());

						break;
					case 4: // All exclusive package
						mPackage = new PackageListForOpBillResDto();
						mPackage.setmPackageMasterDto(mPackageDto);

						// get service details
						packageServRes = iTPackageServicesDetailsDao.getPackageServicesByPackageId(packageId);
						mPackage.setListTPackageServicesDetailsDto(packageServRes.getListObject());

						// get inclusion exclusion details
						Response incExcRes = iTPackageIncExcDetailsDao
								.getTPackageIncExcDetailsForBillingByPackage(packageId);
						mPackage.setListTPackageIncExcDetailsDto(incExcRes.getListObject());

						listPackageListForOpBillResDto.add(mPackage);
						break;
					case 5: // multi encounter package
						mPackage = new PackageListForOpBillResDto();
						mPackage.setmPackageMasterDto(mPackageDto);

						// get service details
						packageServRes = iTPackageServicesDetailsDao.getPackageServicesByPackageId(packageId);
						mPackage.setListTPackageServicesDetailsDto(packageServRes.getListObject());
						listPackageListForOpBillResDto.add(mPackage);
						break;
					}

				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPackageListForOpBillResDto, null);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getExistingActivePackageByPatientEncounter(Integer patientId, Integer encounterId,
			Integer visitTypeId, Integer organisationId, Integer unitId, Integer packageId) throws ServiceException
	{
		try {
			List<PackageConsumptionMasterResDto> finalPackageConMasterDto = new ArrayList<>();
			Response packageResponse = iTPackageConsumptionMasterDao.getExistingActivePackageByPatientEncounter(
					patientId, encounterId, visitTypeId, organisationId, unitId,false,0);
			if (packageResponse.getCode().equals(SUCCESS_CODE) && packageResponse.getListObject() != null) {
				List<PackageConsumptionMasterResDto> listResDto = packageResponse.getListObject();
				// finalPackageConMasterDto = listResDto;
				for (PackageConsumptionMasterResDto resDto : listResDto) {
					Response resPackServDetails = null;
					switch (resDto.getPackageTypeId()) {
					case 1: // EHC package
						// get service details
						resPackServDetails = iTPackageConsumptionMasterDao
								.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageId(),
										patientId);
						resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());

						break;
					case 2: // Either or Package
						// get service details
						resPackServDetails = iTPackageConsumptionMasterDao
								.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageId(),
										patientId);
						resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());
						break;
					case 3:
						// package with cap
						resPackServDetails = iTPackageConsumptionMasterDao
								.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageId(),
										patientId);
						resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());

						// get cap details
						Response packageCapRes = iPackageConsumptionDao
								.getPackageConsumptionCapDetails(resDto.getPackageConsumptionMasterId());
						resDto.setListTPackageConsumptionCapDetailsDto(packageCapRes.getListObject());
						break;
					case 4: // All exclusive package
						resPackServDetails = iTPackageConsumptionMasterDao
								.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageId(),
										patientId);
						resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());
						Response inclusionExclusionService = iTPackageConsumptionMasterDao
								.getOrderedExclusionServices(resDto.getPackageConsumptionMasterId());
						resDto.setListOrderedInclusionExclusionServices(inclusionExclusionService.getListObject());
						break;
					case 5: // multi encounter package
						resPackServDetails = iTPackageConsumptionMasterDao
								.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageId(),
										patientId);
						resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());
						break;
					}
					finalPackageConMasterDto.add(resDto);

				}
				return new Response(SUCCESS, SUCCESS_CODE, null, finalPackageConMasterDto, finalPackageConMasterDto);

			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return null;
	}

	@Override
	@Transactional
	public Response checkIfServiceFromPackage(PackageServiceCheckReqDto packageServiceCheckReqDto)
			throws ServiceException {
		try {
			Integer patientId = packageServiceCheckReqDto.getPatientId();
			Integer encounterId = packageServiceCheckReqDto.getEncounterId();
			Integer visitTypeId = packageServiceCheckReqDto.getVisitType();
			Integer organisationId = packageServiceCheckReqDto.getOrganisationId();
			Integer unitId = packageServiceCheckReqDto.getUnitId();
			Integer tariffId = packageServiceCheckReqDto.getTariffId();
			Integer paymentEntitlementId = packageServiceCheckReqDto.getPaymentEntitlementId();
			Integer patientTypeId = packageServiceCheckReqDto.getPatientTypeId();
			Integer billingBedCategoryId = packageServiceCheckReqDto.getBillingBedCategoryId();
			Integer serviceId = packageServiceCheckReqDto.getServiceId();
			Response incExcRes = null;

			List<PackageConsumptionMasterResDto> finalPackageConMasterDto = new ArrayList<>();
			Response packageResponse = iTPackageConsumptionMasterDao.getExistingActivePackageByPatientEncounter(
					patientId, encounterId, visitTypeId, organisationId, unitId,false,billingBedCategoryId);
			if (packageResponse.getCode().equals(SUCCESS_CODE) && packageResponse.getListObject() != null) {
				List<PackageConsumptionMasterResDto> listResDto = packageResponse.getListObject();
				for (PackageConsumptionMasterResDto resDto : listResDto)
				{
					Response resPackServDetails = null;
					switch (resDto.getPackageTypeId())
					{
					//case 1: // EHC package
						// get service details
						// resPackServDetails =
						// iTPackageConsumptionMasterDao.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageId(),
						// patientId);
						// resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());

						//break;
					case 2: // Either or Package
						// get service details
						resPackServDetails = iTPackageConsumptionMasterDao
								.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageId(),
										patientId);

						ListIterator<PackageConsumptionServiceDetailDto> servicesItr = resPackServDetails
								.getListObject().listIterator();
						while (servicesItr.hasNext()) {
							PackageConsumptionServiceDetailDto serv = servicesItr.next();
							if (serv.getPackageEODetailId() != null && serv.getServiceId() == serviceId) {
								// get cap details
								Response eoRes = iTPackageConsumptionEoDetailsDao
										.getTPackageConsumptionEoDetailsByGrpIdConsumptionId(
												serv.getPackageEODetailId(), resDto.getPackageConsumptionMasterId());
								if (eoRes.getCode().equals(SUCCESS_CODE) && eoRes.getListObject() != null
										&& eoRes.getListObject().size() > 0) {
									ListIterator<PackageConsumptionEODetailDto> eoItr = eoRes.getListObject()
											.listIterator();
									while (eoItr.hasNext()) {
										PackageConsumptionEODetailDto eoDetails = eoItr.next();
										Integer availableQty = eoDetails.getBalanceQty();
										if (availableQty > 0 && eoDetails.getPackageConsumptionEODetailId() == serv
												.getPackageEODetailId()) {
											Response tempRes = iTPackageConsumptionServiceDetailDao
													.getPackageServiceByPackageConsumServiceId(
															resDto.getPackageConsumptionMasterId(),
															serv.getServiceId());
											return new Response<>(SUCCESS, SUCCESS_CODE,
													SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, null,
													(tempRes.getListObject() != null
															&& tempRes.getListObject().size() > 0)
																	? tempRes.getListObject().get(0)
																	: null);
										}
									}
								}
							} else if (serv.getPackageEODetailId() == null && serv.getServiceId() == serviceId) {
								// check if available in services
								if (serv.getBalanceQuantity() > 0) {
									Response tempRes = iTPackageConsumptionServiceDetailDao
											.getPackageServiceByPackageConsumServiceId(
													resDto.getPackageConsumptionMasterId(), serv.getServiceId());
									return new Response<>(SUCCESS, SUCCESS_CODE, SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE,
											null,
											(tempRes.getListObject() != null && tempRes.getListObject().size() > 0)
													? tempRes.getListObject().get(0)
													: null);
								}
							}
						}

						break;
					case 3:
						// package with cap
						resPackServDetails = iTPackageConsumptionMasterDao
								.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageId(),
										patientId);
						resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());


						// check inclusion exclusion service
						incExcRes = iTPackageIncExcDetailsDao
								.getTPackageIncExcDetailsForBillingByPackage(resDto.getPackageId());
						if (incExcRes.getCode().equals(SUCCESS_CODE) && incExcRes.getListObject() != null
								&& incExcRes.getListObject().size() > 0) {
							ListIterator<TPackageIncExcDetailsDto> incexcItr = incExcRes.getListObject().listIterator();
							while (incexcItr.hasNext()) {
								TPackageIncExcDetailsDto incExcServ = incexcItr.next();
								if (incExcServ.getServiceId() == packageServiceCheckReqDto.getServiceId()
										&& incExcServ.getIsInclusiveService() == 1) {
									// available as inclusive service in package
									return new Response<>(SUCCESS, SUCCESS_CODE,
											SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE_AS_INC, null, incExcServ);
								} else if (incExcServ.getServiceId() == packageServiceCheckReqDto.getServiceId()
										&& incExcServ.getIsInclusiveService() == 2) {
									// available as exclusion service and hence cannot be considered
									return new Response<>(ERROR, NOT_FOUND_CODE,
											SERVICE_NOT_AVAILABLE_IN_ACTIVE_PACKAGE_AS_EXC, null, null);
								}
							}
						}

						// get cap details
						Response packageCapRes = iPackageConsumptionDao
								.getPackageConsumptionCapDetails(resDto.getPackageConsumptionMasterId());
						resDto.setListTPackageConsumptionCapDetailsDto(packageCapRes.getListObject());
						if (packageCapRes.getListObject() != null && packageCapRes.getListObject().size() > 0) {
							ListIterator<TPackageConsumptionCapDetailsDto> capDtlsItr = packageCapRes.getListObject()
									.listIterator();
							TPackageConsumptionCapDetailsDto capDtls = null;
							while (capDtlsItr.hasNext()) {
								capDtls = capDtlsItr.next();
								// get service details by id
								Response servRes = iUnitServiceMapperDao
										.getServiceByIdForPackageValid(packageServiceCheckReqDto.getServiceId(),unitId);
								if (servRes.getListObject() != null && servRes.getListObject().size() > 0) {
									ServiceSearchResDto servDtls = (ServiceSearchResDto) servRes.getListObject().get(0);

									// get service price
									UnitServiceTariffMasterDto tariffDto = new UnitServiceTariffMasterDto();
									tariffDto.setServiceId(servDtls.getServiceMasterId());
									tariffDto.setTariffId(tariffId);
									tariffDto.setPaymentEntitlementId(paymentEntitlementId);
									tariffDto.setVisitTypeId(visitTypeId);
									tariffDto.setPatientTypeId(patientTypeId);
									tariffDto.setBillingBedCategoryId(billingBedCategoryId);
									tariffDto.setOrganizationId(organisationId);
									tariffDto.setUnitId(unitId);
									tariffDto
											.setOrderDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(unitId), "yyyy-M-dd"));
									UnitServiceTariffMasterDto serviceConfig = iUnitServiceTariffMasterDao
											.getBasePriceByServiceTariffMaster(tariffDto);

									servDtls.setBasePrice(BigDecimal.valueOf(serviceConfig.getRate()));

									if (packageServiceCheckReqDto.getGroupId() != null
											&& packageServiceCheckReqDto.getSubGroupId() != null) {
										if (servDtls.getSpecialityId() == packageServiceCheckReqDto.getGroupId()
												&& servDtls.getSubSpecialityId() == packageServiceCheckReqDto
														.getSubGroupId()
												&& (servDtls.getBasePrice().doubleValue() <= capDtls
														.getAvailableCap().doubleValue())) {
											TPackageConsumptionServiceDetailDto packServDtls = new TPackageConsumptionServiceDetailDto();
											packServDtls.setIsCapService('Y');
											packServDtls.setPackageId(resDto.getPackageId());
											packServDtls
													.setBalanceCapAmount(capDtls.getAvailableCap());
											return new Response<>(SUCCESS, SUCCESS_CODE,
													SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, null, packServDtls);
										} else if (capDtls.getAvailableCap() > 0) {
											TPackageConsumptionServiceDetailDto packServDtls = new TPackageConsumptionServiceDetailDto();
											packServDtls.setIsCapService('Y');
											packServDtls.setPackageId(resDto.getPackageId());
											packServDtls
													.setBalanceCapAmount(capDtls.getAvailableCap());
											return new Response<>(SUCCESS, SUCCESS_CODE,
													SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, null, packServDtls);
										}
									} else if (packageServiceCheckReqDto.getGroupId() != null
											&& packageServiceCheckReqDto.getSubGroupId() == null) {
										if (servDtls.getSpecialityId() == packageServiceCheckReqDto.getGroupId()
												&& (servDtls.getBasePrice().doubleValue() <= capDtls
														.getAvailableCap().doubleValue())) {
											TPackageConsumptionServiceDetailDto packServDtls = new TPackageConsumptionServiceDetailDto();
											packServDtls.setIsCapService('Y');
											packServDtls.setPackageId(resDto.getPackageId());
											packServDtls
													.setBalanceCapAmount(capDtls.getAvailableCap());
											return new Response<>(SUCCESS, SUCCESS_CODE,
													SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, null, packServDtls);
										} else if (capDtls.getAvailableCap() > 0) {
											TPackageConsumptionServiceDetailDto packServDtls = new TPackageConsumptionServiceDetailDto();
											packServDtls.setIsCapService('Y');
											packServDtls.setPackageId(resDto.getPackageId());
											packServDtls
													.setBalanceCapAmount(capDtls.getAvailableCap());
											return new Response<>(SUCCESS, SUCCESS_CODE,
													SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, null, packServDtls);
										}
									}
								}
							}
						}

						// check service
						if (resPackServDetails.getListObject() != null
								&& resPackServDetails.getListObject().size() > 0) {
							ListIterator<PackageConsumptionServiceDetailDto> resSrvItr = resPackServDetails
									.getListObject().listIterator();
							PackageConsumptionServiceDetailDto srv = null;
							while (resSrvItr.hasNext()) {
								srv = resSrvItr.next();
								if (srv.getServiceId() == packageServiceCheckReqDto.getServiceId()
										&& (srv.getBalanceQuantity() > 0)) {
									Response tempRes = iTPackageConsumptionServiceDetailDao
											.getPackageServiceByPackageConsumServiceId(
													resDto.getPackageConsumptionMasterId(), srv.getServiceId());
									return new Response<>(SUCCESS, SUCCESS_CODE, SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE,
											null,
											(tempRes.getListObject() != null && tempRes.getListObject().size() > 0)
													? tempRes.getListObject().get(0)
													: null);
								}
							}
						}
						break;
					case 4: // All exclusive package
						resPackServDetails = iTPackageConsumptionMasterDao
								.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageId(),
										patientId);
						resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());

						// check inclusion exclusion service
						incExcRes = iTPackageIncExcDetailsDao
								.getTPackageIncExcDetailsForBillingByPackage(resDto.getPackageId());
						if (incExcRes.getCode().equals(SUCCESS_CODE) && incExcRes.getListObject() != null
								&& incExcRes.getListObject().size() > 0) {
							ListIterator<TPackageIncExcDetailsDto> incexcItr = incExcRes.getListObject().listIterator();
							while (incexcItr.hasNext()) {
								TPackageIncExcDetailsDto incExcServ = incexcItr.next();
								if (incExcServ.getServiceId() == packageServiceCheckReqDto.getServiceId()
										&& incExcServ.getIsInclusiveService() == 1) {
									// available as inclusive service in package
									return new Response<>(SUCCESS, SUCCESS_CODE,
											SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE_AS_INC, null, incExcServ);
								} else if (incExcServ.getServiceId() == packageServiceCheckReqDto.getServiceId()
										&& incExcServ.getIsInclusiveService() == 2) {
									// available as exclusion service and hence cannot be considered
									return new Response<>(ERROR, NOT_FOUND_CODE,
											SERVICE_NOT_AVAILABLE_IN_ACTIVE_PACKAGE_AS_EXC, null, null);
								}
							}
						}

						if (resPackServDetails.getStatus().equalsIgnoreCase(SUCCESS)
								&& resPackServDetails.getListObject() != null
								&& resPackServDetails.getListObject().size() > 0) {
							ListIterator<PackageConsumptionServiceDetailDto> packSrvItr = resPackServDetails
									.getListObject().listIterator();
							PackageConsumptionServiceDetailDto srvDtls = null;
							while (packSrvItr.hasNext()) {
								srvDtls = packSrvItr.next();
								if (srvDtls.getServiceId() == packageServiceCheckReqDto.getServiceId()
										&& srvDtls.getIsInclusiveExclusive() == 1 && srvDtls.getIsServiceItem() == 1) {
									Response tempRes = iTPackageConsumptionServiceDetailDao
											.getPackageServiceByPackageConsumServiceId(
													resDto.getPackageConsumptionMasterId(), srvDtls.getServiceId());
									return new Response<>(SUCCESS, SUCCESS_CODE, SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE,
											null,
											(tempRes.getListObject() != null && tempRes.getListObject().size() > 0)
													? tempRes.getListObject().get(0)
													: null);
								}
							}
						}
						break;
					case 5: // multi encounter package
						resPackServDetails = iTPackageConsumptionMasterDao
								.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageId(),
										patientId);
						resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());

						Date today = GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy");
						Date validityFrom = GlobalCommonDateUtils.getDate(resDto.getValidityFrom(), "dd-M-yyyy");
						Date validityTo = GlobalCommonDateUtils.getDate(resDto.getValidityTo(), "dd-M-yyyy");

						if (today.compareTo(validityFrom) >= 0 && today.compareTo(validityTo) <= 0) {
							if (resPackServDetails.getListObject() != null
									&& resPackServDetails.getListObject().size() > 0) {
								ListIterator<PackageConsumptionServiceDetailDto> resSrvItr = resPackServDetails
										.getListObject().listIterator();
								PackageConsumptionServiceDetailDto srv = null;
								while (resSrvItr.hasNext()) {
									srv = resSrvItr.next();
									if (srv.getServiceId() == packageServiceCheckReqDto.getServiceId()
											&& (srv.getBalanceQuantity() > 0)) {
										Response tempRes = iTPackageConsumptionServiceDetailDao
												.getPackageServiceByPackageConsumServiceId(
														resDto.getPackageConsumptionMasterId(), srv.getServiceId());
										return new Response<>(SUCCESS, SUCCESS_CODE,
												SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, null,
												(tempRes.getListObject() != null && tempRes.getListObject().size() > 0)
														? tempRes.getListObject().get(0)
														: null);
									}
								}
							}
						} else
						{
							return new Response<>(ERROR, NOT_FOUND_CODE, PACKAGE_VALIDITY_EXPIRED, null, null);
						}
						break;
					}
					finalPackageConMasterDto.add(resDto);
				}
				return new Response<>(ERROR, NOT_FOUND_CODE, SERVICE_NOT_AVAILABLE_IN_ACTIVE_PACKAGE, null, null);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
		return null;
	}

	@Override
	@Transactional
	public Response getPackageListByFilter(PackageListReqDto reqDto) throws ServiceException {
		try {
			return iMPackageMasterDao.getPackageListByFilter(reqDto);
		} catch (Exception e) {

			throw e;
		}
	}

	// METHOD TO SAVE PACKGE CONSUMPTION
	@Override
	@Transactional
	public void savePackageConsumption(BillingMasterDto billingMasterDto, List<OrderDetailsMasterDto> listToBeAdded,
			Response orderRes) throws Exception {
		try {
			Map<Integer, Integer> packageOrders = (Map) orderRes.getObject();

			ListIterator<OrderDetailsMasterDto> packageItr = listToBeAdded.listIterator();
			if (packageOrders != null && packageOrders.size() > 0) {
				while (packageItr.hasNext()) {
					OrderDetailsMasterDto orderDtls = packageItr.next();
					if (orderDtls.isPackage() && orderDtls.getPackageId() != null
							&& orderDtls.getIsPackageService() != 'Y') {
						Integer createdBy = billingMasterDto.getCreatedBy();
						Integer updatedBy = billingMasterDto.getUpdatedBy();
						String createdDate = GlobalCommonDateUtils.getStringDate(
								new Date(billingMasterDto.getCreatedDate() != null ? billingMasterDto.getCreatedDate()
										: TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime()),
								"dd-M-yyyy HH:mm:ss");
						String updatedDate = GlobalCommonDateUtils.getStringDate(
								new Date(billingMasterDto.getUpdatedDate() != null ? billingMasterDto.getUpdatedDate()
										: TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()).getTime()),
								"dd-M-yyyy HH:mm:ss");

						// save package into package consumption
						Response packRes = iMPackageMasterDao.getPackageById(orderDtls.getPackageId());
						if (packRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && packRes.getObject() != null)
						{
							MPackageMasterDto mPackage = (MPackageMasterDto) packRes.getObject();
							TPackageConsumptionMasterDto consMstDto = new TPackageConsumptionMasterDto();
							consMstDto.setAdmissionId(billingMasterDto.getAdmissionId());
							consMstDto.setApplicableNoOfVisit(mPackage.getNoOfEncounters());
							consMstDto.setEncounterId(billingMasterDto.getEncounterId());
							consMstDto.setOrderDetailId(packageOrders.get(mPackage.getPackageMasterId()));
							consMstDto.setOrganisationId(billingMasterDto.getOrganisationId());
							consMstDto.setPackageId(mPackage.getPackageMasterId());
							consMstDto.setPackageTypeId(mPackage.getPackageTypeId());
							consMstDto.setPatientId(billingMasterDto.getPatientId());
							consMstDto.setPayeeId(orderDtls.getPayeeId());
							consMstDto.setRate(mPackage.getPackagePrice());
							consMstDto.setStatus(ACTIVE);
							consMstDto.setUnitId(billingMasterDto.getUnitId());
							consMstDto.setVisitType(billingMasterDto.getVisitTypeId());
							consMstDto.setCreatedBy(createdBy);
							consMstDto.setCreatedDate(createdDate);
							consMstDto.setUpdatedBy(updatedBy);
							consMstDto.setUpdatedDate(updatedDate);

							// if multi encounter package
							if (mPackage.getPackageTypeId() == PACKAGE5) {
								Integer validityInDays = mPackage.getValidityPeriodIndays();
								LocalDate ldate = LocalDate.now().plusDays(validityInDays);
								Date validityDate = GlobalCommonDateUtils.getDate(ldate);
								consMstDto
										.setValidityFrom(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(billingMasterDto.getUnitId()), "dd-M-yyyy"));
								consMstDto
										.setValidityTo(GlobalCommonDateUtils.getStringDate(validityDate, "dd-M-yyyy"));
							}

							Response packConsRes = iTPackageConsumptionMasterDao
									.saveTPackageConsumptionMaster(consMstDto);
							if (packConsRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
									&& packConsRes.getObject() != null) {
								TPackageConsumptionMaster consMaster = (TPackageConsumptionMaster) packConsRes
										.getObject();
								Integer packageConsumptionId = consMaster.getPackageConsumptionMasterId();

								Response packServRes = iTPackageServicesDetailsDao
										.getPackageServicesByPackageId(mPackage.getPackageMasterId());
								if (packServRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
										&& packServRes.getListObject() != null
										&& packServRes.getListObject().size() > 0) {

									// save package service wise details
									ListIterator<TPackageServicesDetailsDto> srvItr = packServRes.getListObject()
											.listIterator();
									TPackageConsumptionServiceDetailDto consumpServDtls = null;
									while (srvItr.hasNext()) {
										TPackageServicesDetailsDto servDts = srvItr.next();
										consumpServDtls = new TPackageConsumptionServiceDetailDto();
										consumpServDtls.setApportionedPrice(servDts.getApportionedPrice());
										consumpServDtls.setBalancePackageQuantity(
												mPackage.getPackageTypeId() == PACKAGE1 ? (servDts.getNumberToBeUse() - 1)
														: servDts.getNumberToBeUse());
										consumpServDtls.setIsServiceItem(servDts.getIsServiceItem());
										consumpServDtls.setItemId(servDts.getItemId());
										consumpServDtls.setOrganisationId(billingMasterDto.getOrganisationId());
										consumpServDtls.setPackageConsumptionMasterId(packageConsumptionId);
										consumpServDtls.setPackageEoDetailId(servDts.getPackageEitherOrGroupId());
										consumpServDtls.setPackageQuantity(servDts.getNumberToBeUse());
										consumpServDtls.setServiceId(servDts.getServiceId());
										consumpServDtls.setStatus(ACTIVE);
										consumpServDtls.setUnitId(billingMasterDto.getUnitId());
										consumpServDtls.setCreatedBy(createdBy);
										consumpServDtls.setCreatedDate(createdDate);
										consumpServDtls.setUpdatedBy(updatedBy);
										consumpServDtls.setUpdatedDate(updatedDate);
										consumpServDtls.setIsCap(servDts.getIsCap());
										iTPackageConsumptionServiceDetailDao
												.saveTPackageConsumptionServiceDetail(consumpServDtls);
									}

									// save package cap wise details
									if (consMstDto.getPackageTypeId() == PACKAGE3) {
										Response packCapRes = iTPackageCapDetailsDao
												.getPackageCapDetailsByPackageId(mPackage.getPackageMasterId());
										if (packCapRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
												&& packCapRes.getListObject() != null
												&& packCapRes.getListObject().size() > 0) {
											ListIterator<TPackageCapDetailsDto> capItr = packCapRes.getListObject()
													.listIterator();
											TPackageConsumptionCapDetailsDto consCapDtls = null;
											while (capItr.hasNext()) {
												TPackageCapDetailsDto capDtls = capItr.next();
												consCapDtls = new TPackageConsumptionCapDetailsDto();
												consCapDtls.setCreatedBy(createdBy);
												consCapDtls.setCreatedDate(createdDate);
												consCapDtls.setDepartmentCapAmount(capDtls.getDepartmentCapAmount());
												consCapDtls.setDepartmentId(capDtls.getDepartmentId());
												consCapDtls.setIsServiceItem(capDtls.getIsServiceItem());
												consCapDtls.setOrganisationId(billingMasterDto.getOrganisationId());
												consCapDtls.setPackageConsumptionMasterId(packageConsumptionId);
												consCapDtls.setProductCategoryId(capDtls.getProductCateroyId());
												consCapDtls.setStatus(ACTIVE);
												if(capDtls.getSubDepartmentCapAmount()!=null && capDtls.getSubDepartmentCapAmount()>0)
												{
													consCapDtls.setAvailableCap(capDtls.getSubDepartmentCapAmount());
													consCapDtls.setCapAmount(capDtls.getSubDepartmentCapAmount());
												}
												else {
													consCapDtls.setAvailableCap(capDtls.getDepartmentCapAmount());
													consCapDtls.setCapAmount(capDtls.getDepartmentCapAmount());
												}

												consCapDtls.setSubDepartmentId(capDtls.getSubDepartmentId());
												consCapDtls.setUnitId(billingMasterDto.getUnitId());
												consCapDtls.setUpdatedBy(updatedBy);
												consCapDtls.setUpdatedDate(updatedDate);
												iTPackageConsumptionCapDetailsDao
														.saveTPackageConsumptionCapDetails(consCapDtls);
											}
										}
									}

									// save package eo wise details
									if (consMstDto.getPackageTypeId() == PACKAGE2) {
										Response packEoRes = iTPackageEitherOrGroupDetailsDao
												.getPackageEitherOrGroupDetailsByPackageId(
														mPackage.getPackageMasterId());
										if (packEoRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
												&& packEoRes.getListObject() != null
												&& packEoRes.getListObject().size() > 0) {
											ListIterator<TPackageEitherorGroupDetailsDto> eoItr = packEoRes
													.getListObject().listIterator();
											TPackageConsumptionEoDetailsDto eoDetailsDto = null;
											while (eoItr.hasNext()) {
												TPackageEitherorGroupDetailsDto packEoDtls = eoItr.next();
												eoDetailsDto = new TPackageConsumptionEoDetailsDto();
												eoDetailsDto.setBalanceQuantity(mPackage.getPackageTypeId() == PACKAGE1
														? (packEoDtls.getNumberServiceUsable() - 1)
														: packEoDtls.getNumberServiceUsable());
												eoDetailsDto.setCreatedBy(createdBy);
												eoDetailsDto.setCreatedDate(createdDate);
												eoDetailsDto.setGroupPrice(packEoDtls.getGroupPrice());
												eoDetailsDto.setOrganisationId(billingMasterDto.getOrganisationId());
												eoDetailsDto.setPacakgeEighterorGroupId(
														packEoDtls.getPackageGroupDetailsId());
												eoDetailsDto.setPackageConsumptionMasterId(packageConsumptionId);
												eoDetailsDto.setTotalQuantity(packEoDtls.getNumberServiceUsable());
												eoDetailsDto.setUnitId(billingMasterDto.getUnitId());
												eoDetailsDto.setUpdatedBy(updatedBy);
												eoDetailsDto.setUpdatedDate(updatedDate);
												iTPackageConsumptionEoDetailsDao
														.saveTPackageConsumptionEoDetails(eoDetailsDto);
											}
										}
									}

									// save inc exclusive details
									if (consMstDto.getPackageTypeId() == PACKAGE3 || consMstDto.getPackageTypeId() == PACKAGE4) {
										Response packIncExcRes = iTPackageIncExcDetailsDao
												.getTPackageIncExcDetailsForBillingByPackage(
														mPackage.getPackageMasterId());
										if (packIncExcRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
												&& packIncExcRes.getListObject() != null
												&& packIncExcRes.getListObject().size() > 0) {
											ListIterator<TPackageIncExcDetailsDto> packIncExcItr = packIncExcRes
													.getListObject().listIterator();
											TPackageIncExcDetailsDto packIncExcDto = null;
											while (packIncExcItr.hasNext()) {
												packIncExcDto = packIncExcItr.next();
												consumpServDtls = new TPackageConsumptionServiceDetailDto();
												consumpServDtls
														.setApportionedPrice(packIncExcDto.getApportionedPrice());
												consumpServDtls.setIsServiceItem(packIncExcDto.getIsServiceItem());
												consumpServDtls.setItemId(packIncExcDto.getItemId());
												consumpServDtls
														.setIsInclusiveExclusive(packIncExcDto.getIsInclusiveService());
												consumpServDtls.setOrganisationId(billingMasterDto.getOrganisationId());
												consumpServDtls.setPackageConsumptionMasterId(packageConsumptionId);
												consumpServDtls.setServiceId(packIncExcDto.getServiceId());
												consumpServDtls.setStatus(ACTIVE);
												consumpServDtls.setUnitId(billingMasterDto.getUnitId());
												consumpServDtls.setCreatedBy(createdBy);
												consumpServDtls.setCreatedDate(createdDate);
												consumpServDtls.setUpdatedBy(updatedBy);
												consumpServDtls.setUpdatedDate(updatedDate);
												iTPackageConsumptionServiceDetailDao
														.saveTPackageConsumptionServiceDetail(consumpServDtls);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {

			throw e;
		}
	}



	@Override
	@Transactional
	public Response getPackageLog(Integer packageConsumptionServiceDetailId) throws ApplicationException {
		try {
			return iMPackageMasterDao.getPackageLog(packageConsumptionServiceDetailId);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	@Transactional
	public Response getPackageListForIPDPackageOrder(Integer patientId,Integer admissionId, Integer orgId, Integer unitId,Integer visitTypeId)
			throws ApplicationException {
		try {

			return iMPackageMasterDao.getPackageListForIPDPackageOrder(patientId,admissionId, orgId, unitId,visitTypeId);
			/*List<PackageConsumptionMasterResDto> resList=new ArrayList<>();
			if(res.getListObject()!=null && res.getListObject().size()>0) {
				Iterator<PackageConsumptionMasterResDto> itr=res.getListObject().iterator();
				while(itr.hasNext()) {
					PackageConsumptionMasterResDto dto=itr.next();
					Date validityTo = GlobalCommonDateUtils.getDate(dto.getValidityTo(), "dd-M-yyyy");
					Date currentDate=GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy");
					if(currentDate.compareTo(validityTo) <= 0) {
						resList.add(dto);
					}
				}
			}
			res.setListObject(resList);*/
			//return res;
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	public Response packageConsumtionOfItem(BillingMasterDto dto, OrderMasterDto orderDto) throws BillingException {
		try {
			   return iOrderMasterService.saveOrderMasterPharmacy(orderDto, null, true);
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				throw ex;
			} catch (Exception e) {
			logger.error("packageConsumtionOfItem failed", e);
			}
		}
		return new Response(ERROR, null, null, null, null);
	}


	@Override
	@Transactional
	public Response renderPackageService(OrderDetailsMaster dto,
			AdmissionsForBatchProcessDto vistDetailsObj,Integer packageId) throws ApplicationException {

		try {

			Response res = iMPackageMasterDao.renderPackageService(dto, vistDetailsObj,packageId);
			if (res.getObject() != null) {

				BigDecimal serviceAmt = BigDecimal.ZERO;
				BigDecimal taxAmt = BigDecimal.ZERO;
				BigDecimal netAmt = BigDecimal.ZERO;
				List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList = new LinkedList<>();
				List<OrderDetailsPackageMapperDto> orderDetailsPackageMapperDtoList = new LinkedList<>();

				OrderDetailsMaster orderDetails = new OrderDetailsMaster();
				orderDetails = (OrderDetailsMaster) res.getObject();
				ServiceSearchResDto searchResDto = new ServiceSearchResDto();
				searchResDto.setOrganizationId(dto.getOrgId());
				searchResDto.setUnitId(dto.getOrgUnitId());
				searchResDto.setVisitTypeId(vistDetailsObj.getVisitTypeId());
				searchResDto.setBillingBedCategoryId(vistDetailsObj.getBillingBedCategoryId());
				searchResDto.setDefaultSelfTariffId(dto.getTariffId());
				searchResDto.setBillingBedCategoryId(vistDetailsObj.getBillingBedCategoryId());
				searchResDto.setServiceMasterId(orderDetails.getServiceId());

				searchResDto.setContractId(vistDetailsObj.getContractId());
				if (vistDetailsObj.getAdmissionId() > 0)
					searchResDto.setAdmissionId(vistDetailsObj.getAdmissionId());
				if (vistDetailsObj.getEncounterId() > 0)
					searchResDto.setEncounterId(vistDetailsObj.getEncounterId());
				searchResDto.setDefaultSelfTariffId(dto.getTariffId());
				searchResDto.setSelfPayeeId(vistDetailsObj.getSelfPayeeId());
				searchResDto.setBasePrice(orderDetails.getServiceAmt());
				searchResDto.setPackageMasterId(packageId);

				ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
				consumptionDto.setOrganizationId(orderDetails.getOrgId());
				consumptionDto.setUnitId(orderDetails.getOrgUnitId());
				consumptionDto.setVisitTypeId(vistDetailsObj.getVisitTypeId());
				consumptionDto.setPatientTypeId(vistDetailsObj.getPatientTypeId());
				consumptionDto.setBillingBedCategoryId(vistDetailsObj.getBillingBedCategoryId());
				consumptionDto.setPaymentEntitlementId(vistDetailsObj.getPaymentEntitlementId());
				consumptionDto.setServiceId(orderDetails.getServiceId());
				consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(orderDetails.getOrgUnitId()).getTime(), "yyyy-MM-dd"));
				consumptionDto.setContractId(vistDetailsObj.getContractId());

				if (vistDetailsObj.getAdmissionId() > 0)
					consumptionDto.setAdmissionId(vistDetailsObj.getAdmissionId());
				if (vistDetailsObj.getEncounterId() > 0)
					consumptionDto.setEncounterId(vistDetailsObj.getEncounterId());
				consumptionDto.setDefaultSelfTariffId(orderDetails.getTariffId());
				consumptionDto.setSelfPayeeId(vistDetailsObj.getSelfPayeeId());

				Response searchRes = iContractConsumptionService.consumePackageFromContract(searchResDto);

				consumptionDto = (ContractConsumptionDto) searchRes.getObject();
				if (consumptionDto != null) {
					orderDetails.setContractId(consumptionDto.getContractId());
					orderDetails.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
					orderDetails.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(consumptionDto.getUnitId()).getTime());

					Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(orderDetails.getServiceId(),consumptionDto.getUnitId());
					if (tax != null) {
						orderDetails.setTaxId(tax.getId());
						orderDetails.setTaxPer(new BigDecimal(tax.getTaxPercentage()));
						serviceAmt = orderDetails.getServiceAmt();
						taxAmt = serviceAmt.multiply((orderDetails.getTaxPer().multiply(new BigDecimal(0.01))));
						netAmt = serviceAmt.add(taxAmt);
					} else {
						orderDetails.setTaxPer(new BigDecimal(0.00));
						serviceAmt = orderDetails.getServiceAmt();
						netAmt = serviceAmt;
					}
					orderDetails.setOrdTotalAmt(serviceAmt);
					orderDetails.setTaxAmount(taxAmt);
					orderDetails.setNetAmount(netAmt);
					orderDetails.setSelfPayable(consumptionDto.getPayeeId() == vistDetailsObj.getSelfPayeeId() ? netAmt
							: new BigDecimal(consumptionDto.getSelfPayable()));
					orderDetails.setPayeeId(consumptionDto.getPayeeId());
					orderDetails.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
					orderDetails.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(consumptionDto.getUnitId()).getTime());
					orderDetails.setOrdRenderDatetime(TimeZoneComponent.getDateByzone(consumptionDto.getUnitId()).getTime());
					orderDetails.setServiceRendered(1);
					if (consumptionDto.getPayeeId() == vistDetailsObj.getSelfPayeeId()
							&& consumptionDto.getContractId() == null)
					{
						orderDetailsPayeeMapperDtosList = new LinkedList<>();
						OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
						detailsPayeeMapperDto.setPayeeId(consumptionDto.getPayeeId());
						detailsPayeeMapperDto.setPayeeAmount(netAmt);
						detailsPayeeMapperDto.setServiceId(consumptionDto.getServiceId());
						orderDetailsPayeeMapperDtosList.add(detailsPayeeMapperDto);
					} else
						orderDetailsPayeeMapperDtosList = consumptionDto.getOrderDetailsPayeeMapperDtosList();

					if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
						orderDetailsPackageMapperDtoList = consumptionDto.getOrderDetailsPackageMapperDtosList();
				}
				orderDetails.setServiceRendered(1);
				iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetails, orderDetailsPayeeMapperDtosList,orderDetailsPackageMapperDtoList);
			}

		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
		return null;
	}

	@Transactional
	@Override
	public Response searchPackageCommonFilter(PackageSearchRequestDto searchDto) throws ApplicationException {
		try {
			return iMPackageMasterDao.searchPackageCommonFilter(searchDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

		
	}

	@Transactional
	@Override
	public Response getPackageServicesByPackageId(PackageSearchRequestDto searchDto) throws ApplicationException {
		try {
			return iMPackageMasterDao.getPackageServicesByPackageId(searchDto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

		
	}
	private static <T> List<T> mergeList(List<T> list1, List<T> list2) {

		Set<T> set1 = new LinkedHashSet<>();
		set1.addAll(list1);
		set1.addAll(list2);
		return new ArrayList<>(set1);
	}

	@Override
	public Map<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> mergeStrem(Map<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> map1, Map<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> map2) {
		for (Map.Entry<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> entry : map2.entrySet()) {
			map1.merge(entry.getKey(), entry.getValue(), (v1, v2) -> {
				for (Map.Entry<Integer, Map<Integer, List<MPackageMasterDto>>> entry1 : v2.entrySet()) {
					v1.merge(entry1.getKey(), entry1.getValue(), (c1, c2) -> {
						for (Map.Entry<Integer, List<MPackageMasterDto>> entry2 : c2.entrySet()) {
							c1.merge(entry2.getKey(), entry2.getValue(), (s1, s2) ->
							{

								List<TPackageServicesDetailsDto> serviceDetails1 = s1.stream().flatMap(c->c.getListTPackageServicesDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageServicesDetailsDto> itemDetails1 = s1.stream().filter(c->c.getListTPackageItemDetailsDto()!=null).flatMap(c->c.getListTPackageItemDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageIncExcDetailsDto> incExcServiceDetails1 = s1.stream().filter(c->c.getListTPackageIncExcServiceDetailsDto()!=null).flatMap(c->c.getListTPackageIncExcServiceDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageIncExcDetailsDto> incExcItemDetails1 = s1.stream().filter(c->c.getListTPackageIncExcItemDetailsDto()!=null).flatMap(c->c.getListTPackageIncExcItemDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageBedCategoryDetailDto> bedCategoryDetail1 = s1.stream().filter(c->c.getListTPackageBedCategoryDetailDto()!=null).flatMap(c->c.getListTPackageBedCategoryDetailDto().stream()).collect(Collectors.toList());
								List<TPackageCapDetailsDto> groupWiseCapDetails1 = s1.stream().filter(c->c.getListTPackageGroupWiseCapDetailsDto()!=null).flatMap(c->c.getListTPackageGroupWiseCapDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageCapDetailsDto> consumableWiseCapDetails1= s1.stream().filter(c->c.getListTPackageConsumableWiseCapDetailsDto()!=null).flatMap(c->c.getListTPackageConsumableWiseCapDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageCategoryWiseConsumableDetailsDto> categoryWiseConsumableDetails1= s1.stream().filter(c->c.getListTPackageCategoryWiseConsumableDetailsDto()!=null).flatMap(c->c.getListTPackageCategoryWiseConsumableDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageIncExcDetailsDto> groupWiseIncExc1= s1.stream().filter(c->c.getListGroupWiseIncExc()!=null).flatMap(c->c.getListGroupWiseIncExc().stream()).collect(Collectors.toList());
								List<TPackageIncExcDetailsDto> consumableWiseIncExc1 = s1.stream().filter(c->c.getListConsumableWiseIncExc()!=null).flatMap(c->c.getListConsumableWiseIncExc().stream()).collect(Collectors.toList());
								List<TPackageServicesDetailsDto> consumableDetails1 = s1.stream().filter(c->c.getListTPackageConsumableDetailsDto()!=null).flatMap(c->c.getListTPackageConsumableDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageEitherorGroupDetailsDto> eitherorGroupDetails1= s1.stream().filter(c->c.getListTPackageEitherorGroupDetailsDto()!=null).flatMap(c->c.getListTPackageEitherorGroupDetailsDto().stream()).collect(Collectors.toList());

								List<TPackageServicesDetailsDto> serviceDetails2 = s2.stream().flatMap(c->c.getListTPackageServicesDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageServicesDetailsDto> itemDetails2 = s2.stream().filter(c->c.getListTPackageItemDetailsDto()!=null).flatMap(c->c.getListTPackageItemDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageIncExcDetailsDto> incExcServiceDetails2 = s2.stream().filter(c->c.getListTPackageIncExcServiceDetailsDto()!=null).flatMap(c->c.getListTPackageIncExcServiceDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageIncExcDetailsDto> incExcItemDetails2 = s2.stream().filter(c->c.getListTPackageIncExcItemDetailsDto()!=null).flatMap(c->c.getListTPackageIncExcItemDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageBedCategoryDetailDto> bedCategoryDetail2 = s2.stream().filter(c->c.getListTPackageBedCategoryDetailDto()!=null).flatMap(c->c.getListTPackageBedCategoryDetailDto().stream()).collect(Collectors.toList());
								List<TPackageCapDetailsDto> groupWiseCapDetails2 = s2.stream().filter(c->c.getListTPackageGroupWiseCapDetailsDto()!=null).flatMap(c->c.getListTPackageGroupWiseCapDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageCapDetailsDto> consumableWiseCapDetails2= s2.stream().filter(c->c.getListTPackageConsumableWiseCapDetailsDto()!=null).flatMap(c->c.getListTPackageConsumableWiseCapDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageCategoryWiseConsumableDetailsDto> categoryWiseConsumableDetails2= s2.stream().filter(c->c.getListTPackageCategoryWiseConsumableDetailsDto()!=null).flatMap(c->c.getListTPackageCategoryWiseConsumableDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageIncExcDetailsDto> groupWiseIncExc2= s2.stream().filter(c->c.getListGroupWiseIncExc()!=null).flatMap(c->c.getListGroupWiseIncExc().stream()).collect(Collectors.toList());
								List<TPackageIncExcDetailsDto> consumableWiseIncExc2 = s2.stream().filter(c->c.getListConsumableWiseIncExc()!=null).flatMap(c->c.getListConsumableWiseIncExc().stream()).collect(Collectors.toList());
								List<TPackageServicesDetailsDto> consumableDetails2 = s2.stream().filter(c->c.getListTPackageConsumableDetailsDto()!=null).flatMap(c->c.getListTPackageConsumableDetailsDto().stream()).collect(Collectors.toList());
								List<TPackageEitherorGroupDetailsDto> eitherorGroupDetails2= s2.stream().filter(c->c.getListTPackageEitherorGroupDetailsDto()!=null).flatMap(c->c.getListTPackageEitherorGroupDetailsDto().stream()).collect(Collectors.toList());

								List<TPackageServicesDetailsDto> serviceDetails3 =   mergeList(serviceDetails1,serviceDetails2);
								List<TPackageServicesDetailsDto> itemDetails3 =   mergeList(itemDetails1,itemDetails2);
								List<TPackageIncExcDetailsDto> incExcServiceDetails3 =   mergeList(incExcServiceDetails1,incExcServiceDetails2);
								List<TPackageIncExcDetailsDto> incExcItemDetails3 =   mergeList(incExcItemDetails1,incExcItemDetails2);
								List<TPackageBedCategoryDetailDto> bedCategoryDetail3 =   mergeList(bedCategoryDetail1,bedCategoryDetail2);
								List<TPackageCapDetailsDto> groupWiseCapDetails3 =   mergeList(groupWiseCapDetails1,groupWiseCapDetails2);
								List<TPackageCapDetailsDto> consumableWiseCapDetails3=   mergeList(consumableWiseCapDetails1,consumableWiseCapDetails2);
								List<TPackageCategoryWiseConsumableDetailsDto> categoryWiseConsumableDetails3=   mergeList(categoryWiseConsumableDetails1,categoryWiseConsumableDetails2);
								List<TPackageIncExcDetailsDto> groupWiseIncExc3=   mergeList(groupWiseIncExc1,groupWiseIncExc2);
								List<TPackageIncExcDetailsDto> consumableWiseIncExc3 =   mergeList(consumableWiseIncExc1,consumableWiseIncExc1);
								List<TPackageServicesDetailsDto> consumableDetails3 =   mergeList(consumableDetails1,consumableDetails2);
								List<TPackageEitherorGroupDetailsDto> eitherorGroupDetails3 =   mergeList(eitherorGroupDetails1,eitherorGroupDetails2);

								List<MPackageMasterDto> packages =  mergeList(s1,s2);
								packages.forEach(
										pkg->{
											pkg.setListTPackageServicesDetailsDto(serviceDetails3);
											pkg.setListTPackageItemDetailsDto(itemDetails3);
											pkg.setListTPackageIncExcServiceDetailsDto(incExcServiceDetails3);
											pkg.setListTPackageIncExcItemDetailsDto(incExcItemDetails3);
											pkg.setListTPackageBedCategoryDetailDto(bedCategoryDetail3);
											pkg.setListTPackageGroupWiseCapDetailsDto(groupWiseCapDetails3);
											pkg.setListTPackageConsumableWiseCapDetailsDto(consumableWiseCapDetails3);
											pkg.setListTPackageCategoryWiseConsumableDetailsDto(categoryWiseConsumableDetails3);
											pkg.setListGroupWiseIncExc(groupWiseIncExc3);
											pkg.setListConsumableWiseIncExc(consumableWiseIncExc3);
											pkg.setListTPackageConsumableDetailsDto(consumableDetails3);
											pkg.setListTPackageEitherorGroupDetailsDto(eitherorGroupDetails3);
										});
								return packages;
							});
						}
						return c1;
					});
				}
				return v1;
			});
		}
		return map1;
	}



	@Override
	@Transactional
	public MPackageMasterDto searchMap(Map<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> map,MPackageMasterDto dto)
	{
		MPackageMasterDto mPackageMasterDto = null;
		for (Map.Entry<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> entry : map.entrySet()) //paymentEntitlement
		{
			for (Map.Entry<Integer, Map<Integer, List<MPackageMasterDto>>> entry1 :  entry.getValue().entrySet()) //patientType
			{
				for (Map.Entry<Integer, List<MPackageMasterDto>> entry2 :  entry1.getValue().entrySet()) //BillingBedCategory
				{
					List<MPackageMasterDto> packageList =  entry2.getValue(); //BillingBedCategory

					Optional<MPackageMasterDto> pkgObject =  packageList.stream().filter(
							pkg->pkg.getBillingBedCategoryId().equals(dto.getBillingBedCategoryId())
							    && pkg.getPaymentEntitlementTypeId().equals(dto.getPaymentEntitlementTypeId())
							    && pkg.getPatientTypeId().equals(dto.getPatientTypeId())
							    && pkg.getTariffId().equals(dto.getTariffId())).findFirst();
					if(pkgObject.isPresent()){
						return pkgObject.get();
					}
				}
			}
	   }
	  return mPackageMasterDto;
	}

	@Override
	@Transactional
	public Response editPackage(ParentPackageMasterDto parentPackageDto,Map<Integer,Integer> packageIds) throws ApplicationException {

		try {
			PackaageResDto resDto = new PackaageResDto();
			PackageMultyServiceSearchReqDto reqDto = new PackageMultyServiceSearchReqDto();
			Set<Integer> serviceIds = new HashSet<>();
			Set<Integer> itemIds = new HashSet<>();
			Set<Integer> serviceIdsExclusion = new HashSet<>();
			Set<Integer> itemIdsExclusion = new HashSet<>();
			List<TPackageIncExcDetailsDto> exclulist = null;
			List<TPackageEitherorGroupDetailsDto> eorlist = null;
			  //cap
			 List<TPackageBedCategoryDetailDto> bedCategoryListCap = null;
			 List<TPackageCapDetailsDto> groupWiseCap = null;
			 List<TPackageCapDetailsDto> consumableWiseCap = null;

			Set<MultiSelectObj> sBedCatList = new HashSet<>();
			Set<MultiSelectObj> sVisitTList = new HashSet<>();
			Set<MultiSelectObj> sPatientTypeList = new HashSet<>();
			Set<MultiSelectObj> sPayementEList = new HashSet<>();

			Map<Integer, Map<Integer, Map<Integer, List<ServiceForPackageReqDto>>>> map = parentPackageDto.getServiceForPackageReqList().stream()
					.collect(Collectors.groupingBy(ServiceForPackageReqDto::getPaymentEntitlementId,Collectors.groupingBy(ServiceForPackageReqDto::getPatientTypeId,Collectors.groupingBy(ServiceForPackageReqDto::getBillingBedCategoryId))));

			Map<Integer, Map<Integer, Map<Integer, List<MPackageMasterDto>>>> searchMap = parentPackageDto.getPackageMasterDtoList().stream()
					.collect(Collectors.groupingBy(MPackageMasterDto::getPaymentEntitlementTypeId,Collectors.groupingBy(MPackageMasterDto::getPatientTypeId,Collectors.groupingBy(MPackageMasterDto::getBillingBedCategoryId))));


			List<PackagePaymentEntitlement> packagePaymentEntitlementList = new ArrayList<PackagePaymentEntitlement>();
			for (Map.Entry<Integer, Map<Integer, Map<Integer, List<ServiceForPackageReqDto>>>> entry : map.entrySet()) //paymentEntitlement
			{
				PackagePaymentEntitlement packagePaymentEntitlement = new PackagePaymentEntitlement();
				List<PackagePatientCategory> packagePatientCategoryList = new ArrayList<PackagePatientCategory>();
				for (Map.Entry<Integer, Map<Integer, List<ServiceForPackageReqDto>>> entry1 :  entry.getValue().entrySet()) //patientType
				{
					PackagePatientCategory packagePatientCategory = new PackagePatientCategory();
					List<PackageBillingBedCategory> packageBillingBedCategoryList = new ArrayList<PackageBillingBedCategory>();
					for (Map.Entry<Integer, List<ServiceForPackageReqDto>> entry2 :  entry1.getValue().entrySet()) //BillingBedCategory
					{
						PackageBillingBedCategory packageBillingBedCategory = new PackageBillingBedCategory();
						for (ServiceForPackageReqDto dto: entry2.getValue()) //BillingBedCategory
						{
							Response packageRes = null;
							MPackageMasterDto  packageDto = null;
							dto.setServiceId(parentPackageDto.getServiceId());
							Integer bbId = dto.getBillingBedCategoryId();
							Integer pcId = dto.getPatientTypeId();
							Integer peId = dto.getPaymentEntitlementId();

							Optional<String> billingBedCategory = parentPackageDto.getBillingBedCategoryList()
									.stream()
									.filter(obj -> Objects.equals(obj.getBillingBedCategoryId(), bbId))
									.map(obj->obj.getBillingBedDesc()).findFirst();

							Optional<String> payementEntitlement = parentPackageDto.getPaymentEntitlement()
									.stream()
									.filter(obj -> Objects.equals(obj.getId(), peId))
									.map(obj->obj.getLabel()).findFirst();

							Optional<String> patientCategoryDesc = parentPackageDto.getPatientCategoryList()
									.stream()
									.filter(obj -> Objects.equals(obj.getId(), pcId))
									.map(obj->obj.getLabel()).findFirst();

							packagePaymentEntitlement.setPaymentEntitlementId(dto.getPaymentEntitlementId());
							packagePaymentEntitlement.setPaymentEntitlement(payementEntitlement.get());

							packagePatientCategory.setPatientType( patientCategoryDesc.orElse("UNKNOWN"));
							packagePatientCategory.setPatientTypeId(dto.getPatientTypeId());


							packageBillingBedCategory.setBillingBedCategory(billingBedCategory.orElse("UNKNOWN"));
							packageBillingBedCategory.setBillingBedCategoryId(dto.getBillingBedCategoryId());

							switch (parentPackageDto.getPackageTypeId()) {
								case 1: // EHC package
									     if(serviceIds.isEmpty()) {
											 serviceIds = parentPackageDto.getPackageMasterDtoList().get(0)
													 .getListTPackageServicesDetailsDto()
													 .stream()
													 .filter(pkg -> pkg.getServiceId() != null)
													 .map(pkg -> pkg.getServiceId())
													 .distinct().collect(Collectors.toSet());
											 reqDto.setServiceIds(serviceIds);
										 }
											 packageRes =   this.searchServiceForPackageByMultipleCategory(dto, reqDto);

											 packageDto =  (MPackageMasterDto)packageRes.getObject();
									break;
								case 2: // Either or Package
									// get service details
									if(serviceIds.isEmpty() && parentPackageDto.getPackageMasterDtoList().get(0)
											.getListTPackageServicesDetailsDto()!=null) {
										serviceIds = 	 parentPackageDto.getPackageMasterDtoList().get(0)
												.getListTPackageServicesDetailsDto()
												.stream()
												.filter(pkg -> pkg.getServiceId() != null)
												.map(pkg->pkg.getServiceId())
												.distinct().collect(Collectors.toSet());
										reqDto.setServiceIds(serviceIds);
									}
									eorlist = parentPackageDto.getPackageMasterDtoList().get(0).getListTPackageEitherorGroupDetailsDto();

									packageRes =   this.searchServiceForPackageByMultipleCategory(dto, reqDto);
									packageDto=  (MPackageMasterDto)packageRes.getObject();

									if(eorlist!=null && eorlist.size()>0){
										List<TPackageEitherorGroupDetailsDto> mergeList = new ArrayList<>();
										if(packageDto.getListTPackageEitherorGroupDetailsDto()!=null){
											mergeList.addAll(packageDto.getListTPackageEitherorGroupDetailsDto());
										}
										mergeList.addAll(eorlist);
										packageDto.setListTPackageEitherorGroupDetailsDto(mergeList);
									}
									break;
								case 3: // package with cap
									packageRes =   this.searchServiceForPackageByMultipleCategory(dto, reqDto);
									packageDto=  (MPackageMasterDto)packageRes.getObject();
									if(serviceIds.isEmpty() && parentPackageDto.getPackageMasterDtoList().get(0)
											.getListTPackageServicesDetailsDto()!=null) {
										serviceIds = parentPackageDto.getPackageMasterDtoList().get(0)
												.getListTPackageServicesDetailsDto()
												.stream()
												.filter(pkg -> pkg.getServiceId() != null)
												.map(pkg -> pkg.getServiceId())
												.distinct().collect(Collectors.toSet());
									}if(!serviceIds.isEmpty()){
										PackageMultyServiceSearchReqDto reqDtoCapService = new PackageMultyServiceSearchReqDto();
										reqDtoCapService.setServiceIds(serviceIds);
										dto.setIsServiceItem(1);
										dto.setIsInclusiveService(null);
										packageRes =   this.searchServiceForPackageByMultipleCategory(dto, reqDtoCapService);
										MPackageMasterDto packageDtoService  =  (MPackageMasterDto)packageRes.getObject();
										if(packageDtoService.getListTPackageServicesDetailsDto()!=null){
											packageDto.setListTPackageServicesDetailsDto(packageDtoService.getListTPackageServicesDetailsDto());
										}
									}
									if(itemIds.isEmpty() && parentPackageDto.getPackageMasterDtoList().get(0)
											.getListTPackageItemDetailsDto()!=null) {
										itemIds = parentPackageDto.getPackageMasterDtoList().get(0)
												.getListTPackageItemDetailsDto()
												.stream()
												.filter(pkg -> pkg.getItemId() != null)
												.map(pkg -> pkg.getItemId())
												.distinct().collect(Collectors.toSet());
									}
									if(!itemIds.isEmpty()){
										PackageMultyServiceSearchReqDto reqDtoCapItem = new PackageMultyServiceSearchReqDto();
										reqDtoCapItem.setItemIds(itemIds);
										dto.setIsServiceItem(2);
										dto.setIsInclusiveService(null);
										packageRes =   this.searchServiceForPackageByMultipleCategory(dto, reqDtoCapItem);
										MPackageMasterDto packageDtoItem =  (MPackageMasterDto)packageRes.getObject();
										if(packageDtoItem.getListTPackageItemDetailsDto()!=null)
										{
											packageDto.setListTPackageItemDetailsDto(packageDtoItem.getListTPackageItemDetailsDto());
										}
									 }
									if(serviceIdsExclusion.isEmpty() && parentPackageDto.getPackageMasterDtoList().get(0)
											.getListGroupWiseIncExc()!=null) {
										serviceIdsExclusion = parentPackageDto.getPackageMasterDtoList().get(0)
												.getListGroupWiseIncExc()
												.stream()
												.filter(pkg -> pkg.getServiceId() != null)
												.map(pkg -> pkg.getServiceId())
												.distinct().collect(Collectors.toSet());
									}if(!serviceIdsExclusion.isEmpty()){
										PackageMultyServiceSearchReqDto reqDtoCapEService = new PackageMultyServiceSearchReqDto();
										reqDtoCapEService.setServiceIds(serviceIdsExclusion);
										dto.setIsServiceItem(1);
										dto.setIsInclusiveService(2);
										packageRes =   this.searchServiceForPackageByMultipleCategory(dto, reqDtoCapEService);
										MPackageMasterDto packageDtoExclusion =  (MPackageMasterDto)packageRes.getObject();

										if(packageDtoExclusion.getListGroupWiseIncExc()!=null){
											packageDto.setListGroupWiseIncExc(packageDtoExclusion.getListGroupWiseIncExc());
										}
									}
									if(itemIdsExclusion.isEmpty() && parentPackageDto.getPackageMasterDtoList().get(0)
											.getListConsumableWiseIncExc()!=null) {
										itemIdsExclusion = parentPackageDto.getPackageMasterDtoList().get(0)
												.getListConsumableWiseIncExc()
												.stream()
												.filter(pkg -> pkg.getItemId() != null)
												.map(pkg -> pkg.getItemId())
												.distinct().collect(Collectors.toSet());
									}if(!itemIdsExclusion.isEmpty()){
										PackageMultyServiceSearchReqDto reqDtoCapEItem = new PackageMultyServiceSearchReqDto();
										reqDtoCapEItem.setItemIds(itemIdsExclusion);
										dto.setIsServiceItem(2);
										dto.setIsInclusiveService(2);
										packageRes =   this.searchServiceForPackageByMultipleCategory(dto, reqDtoCapEItem);
										MPackageMasterDto packageDtoItemExclusion =  (MPackageMasterDto)packageRes.getObject();

										if(packageDtoItemExclusion.getListConsumableWiseIncExc()!=null){
											packageDto.setListConsumableWiseIncExc(packageDtoItemExclusion.getListConsumableWiseIncExc());
										}
									}
									if(parentPackageDto.getPackageMasterDtoList().get(0).getListTPackageBedCategoryDetailDto()!=null									 ){
										 bedCategoryListCap = parentPackageDto.getPackageMasterDtoList().get(0)
												.getListTPackageBedCategoryDetailDto();
									}
									if(parentPackageDto.getPackageMasterDtoList().get(0).getListTPackageGroupWiseCapDetailsDto()!=null									 ){
										groupWiseCap = parentPackageDto.getPackageMasterDtoList().get(0)
												.getListTPackageGroupWiseCapDetailsDto();
									}
									if(parentPackageDto.getPackageMasterDtoList().get(0).getListTPackageConsumableWiseCapDetailsDto()!=null									 ){
										consumableWiseCap = parentPackageDto.getPackageMasterDtoList().get(0)
												.getListTPackageConsumableWiseCapDetailsDto();
									}


									if(bedCategoryListCap !=null){
										packageDto.setListTPackageBedCategoryDetailDto(bedCategoryListCap);
									}
									if(groupWiseCap !=null){
										packageDto.setListTPackageGroupWiseCapDetailsDto(groupWiseCap);
									}
									if(consumableWiseCap !=null){
										packageDto.setListTPackageConsumableWiseCapDetailsDto(consumableWiseCap);
									}
									break;
								case 4: // All exclusive package
									//List<TPackageIncExcDetailsDto> tPackageServiceIncExcDetailsDto = new ArrayList<TPackageIncExcDetailsDto>();
									if(serviceIds.isEmpty() && parentPackageDto.getPackageMasterDtoList().get(0)
											.getListTPackageIncExcServiceDetailsDto()!=null) {
										serviceIds = 	 parentPackageDto.getPackageMasterDtoList().get(0)
												.getListTPackageIncExcServiceDetailsDto()
												.stream()
												.filter(pkg -> pkg.getServiceId() != null)
												.map(pkg->pkg.getServiceId())
												.distinct().collect(Collectors.toSet());
										reqDto.setServiceIds(serviceIds);
									}
									if(itemIds.isEmpty() && parentPackageDto.getPackageMasterDtoList().get(0)
											.getListTPackageIncExcItemDetailsDto()!=null) {
										itemIds = 	 parentPackageDto.getPackageMasterDtoList().get(0)
												.getListTPackageIncExcItemDetailsDto()
												.stream()
												.filter(pkg -> pkg.getItemId() != null)
												.map(pkg->pkg.getItemId())
												.distinct().collect(Collectors.toSet());
										reqDto.setItemIds(itemIds);
									}
									 if(parentPackageDto.getPackageMasterDtoList().get(0)
											 .getListTPackageIncExcServiceDetailsDto()!=null){
										 exclulist  = parentPackageDto.getPackageMasterDtoList().get(0)
												 .getListTPackageIncExcServiceDetailsDto()
												 .stream()
												 .filter(pkg -> pkg.getIsGroupwiseExlusion() != null && pkg.getIsGroupwiseExlusion().equals(2))
												 .distinct().collect(Collectors.toList());
									 }
									packageRes =   this.searchServiceForPackageByMultipleCategory(dto, reqDto);
									packageDto =  (MPackageMasterDto)packageRes.getObject();
									if(exclulist!=null
											&& exclulist.size()>0){
										List<TPackageIncExcDetailsDto> mergeList = new ArrayList<>();
										if(packageDto.getListTPackageIncExcServiceDetailsDto()!=null) {
											mergeList.addAll(packageDto.getListTPackageIncExcServiceDetailsDto());
										}mergeList.addAll(exclulist);
										packageDto.setListTPackageIncExcServiceDetailsDto(mergeList);
									}
									break;
								case 5: // multi encounter package
									if(serviceIds.isEmpty() && parentPackageDto.getPackageMasterDtoList().get(0)
											.getListTPackageServicesDetailsDto()!=null) {
										serviceIds = 	 parentPackageDto.getPackageMasterDtoList().get(0)
												.getListTPackageServicesDetailsDto()
												.stream()
												.filter(pkg -> pkg.getServiceId() != null)
												.map(pkg->pkg.getServiceId())
												.distinct().collect(Collectors.toSet());
										reqDto.setServiceIds(serviceIds);
									}
									if(itemIds.isEmpty() && parentPackageDto.getPackageMasterDtoList().get(0)
											.getListTPackageItemDetailsDto()!=null) {
										itemIds = 	 parentPackageDto.getPackageMasterDtoList().get(0)
												.getListTPackageItemDetailsDto()
												.stream()
												.filter(pkg -> pkg.getItemId() != null)
												.map(pkg->pkg.getItemId())
												.distinct().collect(Collectors.toSet());
										reqDto.setItemIds(itemIds);
									}
									packageRes =   this.searchServiceForPackageByMultipleCategory(dto, reqDto);
									packageDto=  (MPackageMasterDto)packageRes.getObject();
									break;
							}

							packageDto.setTariffId(dto.getTariffId());
							 MPackageMasterDto pkgSearchDto = this.searchMap(searchMap,packageDto);
							 if(pkgSearchDto!=null){
								 if(packageDto.getListTPackageItemDetailsDto()!=null && packageDto.getListTPackageItemDetailsDto().size()>0
								    && pkgSearchDto.getListTPackageItemDetailsDto()!=null && pkgSearchDto.getListTPackageItemDetailsDto().size()>0){
									 for (TPackageServicesDetailsDto itm:packageDto.getListTPackageItemDetailsDto()) {

										 Optional<TPackageServicesDetailsDto> item = pkgSearchDto.getListTPackageItemDetailsDto().stream().filter(it->
												 it.getItemId()!=null &&
												 it.getItemId().equals(itm.getItemId())).findFirst();
										 if(item.isPresent()){
											 itm.setNumberToBeUse(item.get().getNumberToBeUse());
											 itm.setServicePrice(item.get().getServicePrice());
										 }
									 }
								 }
								 if(packageDto.getListTPackageServicesDetailsDto()!=null && packageDto.getListTPackageServicesDetailsDto().size()>0
										 && pkgSearchDto.getListTPackageServicesDetailsDto()!=null && pkgSearchDto.getListTPackageServicesDetailsDto().size()>0){
									 for (TPackageServicesDetailsDto srvitr:packageDto.getListTPackageServicesDetailsDto()) {

										 Optional<TPackageServicesDetailsDto> service = pkgSearchDto.getListTPackageServicesDetailsDto().stream().filter(srv->
												 srv.getServiceId()!=null && srv.getServiceId().equals(srvitr.getServiceId())
														 && srv.getServicePrice()!=null && srv.getServicePrice()>0).findFirst();
										 if(service.isPresent()){
											 srvitr.setServicePrice(service.get().getServicePrice());
											 srvitr.setNumberToBeUse(service.get().getNumberToBeUse());
										 }
									 }
								 }
							  }
							packageBillingBedCategory.setmPackageMasterDto(packageDto);
							packageBillingBedCategoryList.add(packageBillingBedCategory);

							sBedCatList.add(new MultiSelectObj(dto.getBillingBedCategoryId(),dto.getBillingBedCategory()));
							sPatientTypeList.add(new MultiSelectObj(dto.getPatientTypeId(),dto.getPatientType()));
							sVisitTList.add(new MultiSelectObj(dto.getVisitTypeId(),dto.getVisitTypeName()));
							sPayementEList.add(new MultiSelectObj(dto.getPaymentEntitlementId(),dto.getPaymentEntitlement()));
						}
					}
					packagePatientCategory.setBillingBedCategoryList(packageBillingBedCategoryList);
					packagePatientCategoryList.add(packagePatientCategory);
				}
				packagePaymentEntitlement.setPatientCategoryList(packagePatientCategoryList);
				packagePaymentEntitlementList.add(packagePaymentEntitlement);
			}
			resDto.setParentPackageMasterDto(parentPackageDto);
			resDto.setPaymentEntitlement(packagePaymentEntitlementList);
			resDto.setSelectedBedBillingCategoryList(sBedCatList);
			resDto.setSelectedPaymentEntitlementList(sPayementEList);
			resDto.setSelectedVisitTypeList(sVisitTList);
			resDto.setSelectedPatientTypeList(sPatientTypeList);
			return  new Response<>(SUCCESS, SUCCESS_CODE, null, null, resDto);
		} catch (ServiceException e) {

			throw e;
		}
	}



	@Override
	@Transactional
	public Response getEhcByPackageId(EncounterMasterDto encounterMasterDto) throws Exception {
		try {
			Date orderDate = TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId());
			/*BigDecimal serviceAmt = BigDecimal.ZERO;
			BigDecimal taxAmt = BigDecimal.ZERO;
			BigDecimal netAmt = BigDecimal.ZERO;*/

			List<OrderDetailsMasterDto> list = new ArrayList<>();
			List<OrderDetailsPayeeMapperDto> orderDetailsPayeeMapperDtosList = new LinkedList<>();

			OrderMasterDto orderMasterDto = new OrderMasterDto();
			orderMasterDto.setVisitTypeId(OP);// op==1
			orderMasterDto.setEncounterId(encounterMasterDto.getEncounterId());
			orderMasterDto.setPatientId(encounterMasterDto.getPatientId());
			orderMasterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
			orderMasterDto.setUpdatedBy(encounterMasterDto.getUpdatedBy());
			orderMasterDto.setCreatedDate(encounterMasterDto.getCreatedDate());
			orderMasterDto.setUpdatedDate(encounterMasterDto.getUpdatedDate());
			orderMasterDto.setOrderDate(encounterMasterDto.getCreatedDate());
			orderMasterDto.setOrderStatus('A');
			orderMasterDto.setOrgId(encounterMasterDto.getOrganizationId());
			orderMasterDto.setOrgUnitId(encounterMasterDto.getUnitId());
			orderMasterDto.setOrderStatusId(1);// order status==1
			orderMasterDto.setPatientTypeId(encounterMasterDto.getPatientCategoryId());
			orderMasterDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
			orderMasterDto.setPaymentEntitlementTypeId(encounterMasterDto.getPaymentEntitlementTypeId());

			ServiceSearchResDto searchResDto = new ServiceSearchResDto();
				searchResDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				searchResDto.setUnitId(encounterMasterDto.getUnitId());
				searchResDto.setVisitTypeId(OP);
				searchResDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
				searchResDto.setDefaultSelfTariffId(encounterMasterDto.getDefaultSelfTariffId());
				searchResDto.setBillingBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
				searchResDto.setServiceMasterId(encounterMasterDto.getServiceId());
				searchResDto.setContractId(encounterMasterDto.getContractId());
				searchResDto.setEncounterId(encounterMasterDto.getEncounterId());
				searchResDto.setDefaultSelfTariffId(encounterMasterDto.getDefaultSelfTariffId());
				searchResDto.setSelfPayeeId(encounterMasterDto.getPayeeId());
				searchResDto.setBasePrice(encounterMasterDto.getPackageBasePrice());
				searchResDto.setPackageMasterId(encounterMasterDto.getEhcPackageId());
				searchResDto.setPatientCategoryId(encounterMasterDto.getPatientCategoryId());
				searchResDto.setPackageMasterId(encounterMasterDto.getEhcPackageId());
				searchResDto.setPatientClassId(encounterMasterDto.getPatientClassId());
				searchResDto.setIsFromEncounter('Y');
			Response searchRes = iContractConsumptionService.consumePackageFromContract(searchResDto);

			ContractConsumptionDto consumptionDto = (ContractConsumptionDto) searchRes.getObject();
			OrderDetailsMasterDto orderDetails = new OrderDetailsMasterDto();

			if (consumptionDto != null) {
				orderDetails.setContractId(consumptionDto.getContractId());
				orderDetails.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
				orderDetails.setOrderDate(orderDate.getTime());
				orderDetails.setOrdRenderDatetime(orderDate.getTime());
				orderDetails.setOrderSchDate(orderDate.getTime());
				orderDetails.setOrdTotalAmt(new BigDecimal(consumptionDto.getServiceRate()));
				orderDetails.setTaxId(consumptionDto.getTaxId());
				orderDetails.setTaxPer(new BigDecimal(consumptionDto.getTaxPercentage()));
				orderDetails.setTaxAmount(new BigDecimal(consumptionDto.getTaxAmt()));
				orderDetails.setNetAmount(new BigDecimal(consumptionDto.getFinalPrice()));
				orderDetails.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
				orderDetails.setOrdConcession(new BigDecimal(consumptionDto.getConcession()));
				orderDetails.setPayeeId(consumptionDto.getPayeeId());
				orderDetails.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
				orderDetails.setContractId(consumptionDto.getContractId());
				orderDetails.setBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
				orderDetails.setOrgId(encounterMasterDto.getOrganizationId());
				orderDetails.setOrgUnitId(encounterMasterDto.getUnitId());
				orderDetails.setCreatedBy(encounterMasterDto.getCreatedBy());
				orderDetails.setCreatedDate(orderDate.getTime());
				orderDetails.setUpdatedBy(encounterMasterDto.getCreatedBy());
				orderDetails.setUpdatedDate(orderDate.getTime());
				orderDetails.setServiceIsBilled(0);
				orderDetails.setStatus('A');
				orderDetails.setCreatedBy(encounterMasterDto.getCreatedBy());
				orderDetails.setUpdatedBy(encounterMasterDto.getCreatedBy());
				orderDetails.setIsDrug('N');
				orderDetails.setIsPackageService('N');
				orderDetails.setIsPackage(true);
				orderDetails.setPackageId(encounterMasterDto.getEhcPackageId());
				orderDetails.setIsInvestigationOrder('N');
				orderDetails.setIsProcedure('N');
				orderDetails.setDebitNoteService(false);
				orderDetails.setOrderQty(1);
				orderDetails.setServiceId(encounterMasterDto.getServiceId());
				orderDetails.setPriorityId(1);
				orderDetails.setOrderEmergencyFlag('N');
				orderDetails.setIsDrug('N');
				orderDetails.setOrdCancelled('N');
				orderDetails.setCoPayPer(BigDecimal.ZERO);
				orderDetails.setDiscountValue(BigDecimal.ZERO);
				orderDetails.setPackageConsumptionAmt(BigDecimal.ZERO);

				orderDetailsPayeeMapperDtosList = consumptionDto.getOrderDetailsPayeeMapperDtosList();
			}
			orderDetails.setServiceRendered(0);
			orderDetails.setOrderDetailsPayeeMapperDtosList(orderDetailsPayeeMapperDtosList);


			//for package
			List<OrderDetailsPackageMapperDto> packageMpper = new LinkedList<>();
			OrderDetailsPackageMapperDto packgeMppr = new OrderDetailsPackageMapperDto();
			packgeMppr.setPackageId(encounterMasterDto.getEhcPackageId());
			packgeMppr.setIsPackageService('N');
			packgeMppr.setIsCapService('N');
			packgeMppr.setPackageAmount(new BigDecimal(consumptionDto.getFinalPrice()));
			packgeMppr.setServiceAmount(new BigDecimal(consumptionDto.getServiceRate()));
			packgeMppr.setServiceId(orderDetails.getServiceId());
			packgeMppr.setStatus('A');
			packageMpper.add(packgeMppr);
			orderDetails.setOrderDetailsPackageMapperDtosList(packageMpper);

			// list to be add
			list.add(orderDetails);

			Response prackageResponse = this
					.getPackageDetailsByIdForBilling(Arrays.asList(encounterMasterDto.getEhcPackageId()));
			if (prackageResponse.getListObject() != null && prackageResponse.getListObject().size() > 0) {
				List<PackageListForOpBillResDto> listPackageListForOpBillResDto = prackageResponse.getListObject();
				if (listPackageListForOpBillResDto != null && listPackageListForOpBillResDto.size() > 0) {
					for (PackageListForOpBillResDto packageListForOpBillResDto : listPackageListForOpBillResDto) {

						for (TPackageServicesDetailsDto tPackageServicesDetailsDto : packageListForOpBillResDto
								.getListTPackageServicesDetailsDto()) {

							List<OrderDetailsPayeeMapperDto> mpprList = new LinkedList<>();
							List<OrderDetailsPackageMapperDto> packageMpperList = new LinkedList<>();

							OrderDetailsMasterDto ordDtls = new OrderDetailsMasterDto();
							ordDtls.setOrgId(encounterMasterDto.getOrganizationId());
							ordDtls.setOrgUnitId(encounterMasterDto.getUnitId());
							ordDtls.setOrderQty(1);
							ordDtls.setServiceId(tPackageServicesDetailsDto.getServiceId());
							ordDtls.setPriorityId(1);
							ordDtls.setIsOutsourced('N');
							ordDtls.setOrderDate(orderDate.getTime());
							ordDtls.setOrderSchDate(orderDate.getTime());
							ordDtls.setOrdDocSplId(tPackageServicesDetailsDto.getConsultationSpecialityId());
							ordDtls.setServiceAmt(BigDecimal.valueOf(tPackageServicesDetailsDto.getServicePrice()));
							ordDtls.setOrdTotalAmt(BigDecimal.valueOf(tPackageServicesDetailsDto.getServicePrice()));
							ordDtls.setNetAmount(BigDecimal.ZERO);
							ordDtls.setPackageConsumptionAmt(BigDecimal.valueOf(tPackageServicesDetailsDto.getServicePrice()));
							ordDtls.setOrdConcession(BigDecimal.ZERO);
							ordDtls.setOrdDiscount(BigDecimal.ZERO);
							ordDtls.setTaxPer(BigDecimal.ZERO);
							ordDtls.setTaxAmount(BigDecimal.ZERO);
							ordDtls.setSelfPayable(BigDecimal.ZERO);
							ordDtls.setCreditPayable(BigDecimal.ZERO);
							ordDtls.setPayeeId(encounterMasterDto.getPayeeId());
							ordDtls.setContractId(consumptionDto.getContractId());
							ordDtls.setBedCategoryId(encounterMasterDto.getBillingBedCategoryId());
							ordDtls.setCoPayPer(BigDecimal.ZERO);
							ordDtls.setOrderEmergencyFlag('N');
							ordDtls.setPackageId(encounterMasterDto.getEhcPackageId());
							ordDtls.setServiceRendered(0);
							ordDtls.setServiceRenderingDeptId(tPackageServicesDetailsDto.getSubSpecialityId());
							ordDtls.setOrdRenderDatetime(orderDate.getTime());
							ordDtls.setServiceIsBilled(0);
							ordDtls.setCreatedDate(orderDate.getTime());
							ordDtls.setStatus('A');
							ordDtls.setCreatedBy(encounterMasterDto.getCreatedBy());
							ordDtls.setUpdatedBy(encounterMasterDto.getCreatedBy());
							ordDtls.setIsDrug('N');
							ordDtls.setIsPackageService('Y');
							ordDtls.setIsPackage(false);;
							ordDtls.setIsInvestigationOrder('N');
							ordDtls.setIsProcedure('N');
							ordDtls.setDebitNoteService(false);
							ordDtls.setIsFromAddservice('N');
							ordDtls.setOrdCancelled('N');
							//doctor & ref share
							ordDtls.setIsDocReq(tPackageServicesDetailsDto.getIsDocReq());
							ordDtls.setDocSharePer(BigDecimal.valueOf(tPackageServicesDetailsDto.getDocSharePer()));
							ordDtls.setRefDocSharePer(BigDecimal.valueOf(tPackageServicesDetailsDto.getRefSharePer()));
							ordDtls.setDiscountValue(BigDecimal.ZERO);

							//package for contract
							orderDetailsPayeeMapperDtosList = new LinkedList<>();
							OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
								detailsPayeeMapperDto.setPayeeId(encounterMasterDto.getPayeeId());
								detailsPayeeMapperDto.setPayeeAmount(BigDecimal.ZERO);
								detailsPayeeMapperDto.setServiceId(tPackageServicesDetailsDto.getServiceId());
							mpprList.add(detailsPayeeMapperDto);
							ordDtls.setOrderDetailsPayeeMapperDtosList(mpprList);
							list.add(ordDtls);

							//for package
							OrderDetailsPackageMapperDto packgeMpper = new OrderDetailsPackageMapperDto();
								packgeMpper.setPackageId(encounterMasterDto.getEhcPackageId());
								packgeMpper.setIsPackageService('Y');
								packgeMpper.setIsCapService('N');
								packgeMpper.setPackageAmount(BigDecimal.valueOf(tPackageServicesDetailsDto.getServicePrice()));
								packgeMpper.setServiceAmount(BigDecimal.valueOf(tPackageServicesDetailsDto.getServicePrice()));
								packgeMpper.setServiceId(tPackageServicesDetailsDto.getServiceId());
								packgeMpper.setOrganizationId(ordDtls.getOrgId());
								packgeMpper.setUnitId(ordDtls.getOrgUnitId());
								packgeMpper.setStatus('A');
							packageMpperList.add(packgeMpper);

							ordDtls.setOrderDetailsPackageMapperDtosList(packageMpperList);
						}
					}
				}
			}
			orderMasterDto.setListOrderDetailsMasterDto(list);
			return iOrderMasterService.saveOrderMaster(orderMasterDto);

		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}



	@Override
	@Transactional
	public Response saveEhcEncounterEhcConsultation(EncounterMasterDto encounterMasterDto) throws Exception {
		try {
			Response consultationRes = iMPackageMasterDao.getEhcConsultationServices(encounterMasterDto);
			if (consultationRes.getListObject() != null && !consultationRes.getListObject().isEmpty()) {
				EncounterEhcConsultationMasterDto masterDto = new EncounterEhcConsultationMasterDto();
				masterDto.setPackageId(encounterMasterDto.getEhcPackageId());
				masterDto.setOrderDtailsId(encounterMasterDto.getOrderDetailsIdEhc());
				masterDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				masterDto.setServiceId(encounterMasterDto.getServiceId());
				masterDto.setEncounterId(encounterMasterDto.getEncounterId());
				masterDto.setUnitId(encounterMasterDto.getUnitId());
				masterDto.setPackageId(encounterMasterDto.getEhcPackageId());
				masterDto.setOrganizationId(encounterMasterDto.getOrganizationId());
				masterDto.setCreatedBy(encounterMasterDto.getCreatedBy());
				masterDto.setUpdatedBy(encounterMasterDto.getCreatedBy());
				masterDto.setStatus('A');
				masterDto.setCreatedDate(
						GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime(), "dd-M-yyyy HH:mm:ss"));
				masterDto.setUpdatedDate(
						GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime(), "dd-M-yyyy HH:mm:ss"));
				Response masterRes = iMPackageMasterDao.saveEhcEncounterEhcConsultationMaster(masterDto);
				Integer consultationMasterId = (Integer) masterRes.getObject();

				List<EncounterEhcConsultationMpprDto> list = consultationRes.getListObject();
				for (EncounterEhcConsultationMpprDto dto : list)
				{
					dto.setEncounterId(encounterMasterDto.getEncounterId());
					dto.setAppointmentStatusId(APPOINTMENTSTATUSID1);
					dto.setOrganizationId(dto.getOrganizationId());
					dto.setUnitId(encounterMasterDto.getUnitId());
					dto.setPackageId(encounterMasterDto.getEhcPackageId());
					dto.setOrganizationId(encounterMasterDto.getOrganizationId());
					dto.setCreatedBy(encounterMasterDto.getCreatedBy());
					dto.setUpdatedBy(encounterMasterDto.getCreatedBy());
					dto.setStatus('A');
					dto.setConsultationMasterId(consultationMasterId);
					dto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime(),
							"dd-M-yyyy HH:mm:ss"));
					dto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(encounterMasterDto.getUnitId()).getTime(),
							"dd-M-yyyy HH:mm:ss"));
					iMPackageMasterDao.saveEhcEncounterEhcConsultation(dto);
				}
			}
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
	}

	@Override
	@Transactional
	public Response searchServiceForPackageByMultipleCategory(ServiceForPackageReqDto reqDto,
			PackageMultyServiceSearchReqDto searchDto) throws ServiceException {
		try {

			MPackageMasterDto packageDto = new MPackageMasterDto();
			packageDto.setBillingBedCategory(reqDto.getBillingBedCategory());
			packageDto.setVisitTypeName(reqDto.getVisitTypeName());
			packageDto.setPaymentEntitlement(reqDto.getPaymentEntitlement());
			packageDto.setPatientType(reqDto.getPatientType());
			packageDto.setPaymentEntitlementTypeId(reqDto.getPaymentEntitlementId());
			packageDto.setBillingBedCategoryId(reqDto.getBillingBedCategoryId());
			packageDto.setPatientTypeId(reqDto.getPatientTypeId());
			packageDto.setVisitTypeId(reqDto.getVisitTypeId());

			Response packageServiceRes = null;
			Response packageItemRes = null;
			List<ServiceForPackageResDto> serviceList = null;
			List<ServiceForPackageResDto> itemList = null;
			if (searchDto.getServiceIds() != null && searchDto.getServiceIds().size() > 0) {
				packageServiceRes = iMPackageMasterDao.searchServiceForPackageByMultipleCategory(reqDto,
						searchDto.getServiceIds());
				serviceList = packageServiceRes.getListObject();
			}
			if (searchDto.getItemIds() != null && searchDto.getItemIds().size() > 0) {
				packageItemRes = iMPackageMasterDao.searchItemsForPackageByMultipleCategory(reqDto,
						searchDto.getItemIds());
				itemList = packageItemRes.getListObject();
			}

			switch (reqDto.getPackagetypeId()) {
			case 1: // EHC package

				if (serviceList != null && serviceList.size() > 0) {
					List<TPackageServicesDetailsDto> ehcServiceList = new ArrayList<TPackageServicesDetailsDto>();
					for (ServiceForPackageResDto ehcSerivce : serviceList) {

						TPackageServicesDetailsDto ehcPackageDto = new TPackageServicesDetailsDto();
						ehcPackageDto.setIsServiceItem(1);// for Service
						ehcPackageDto.setServiceId(ehcSerivce.getServiceMasterId());
						ehcPackageDto.setSpecialityId(ehcSerivce.getSpecialityId());
						ehcPackageDto.setSubSpecialityId(ehcSerivce.getSubSpecialityMasterId());
						ehcPackageDto.setServiceName(ehcSerivce.getServiceDescription());
						ehcPackageDto.setServiceCode(ehcSerivce.getServiceCode());
						ehcPackageDto.setServicePrice(ehcSerivce.getRate() == null ? 0 : ehcSerivce.getRate());
						ehcPackageDto.setApportionedPrice(0.0);
						ehcPackageDto.setTotal(0.00);
						ehcPackageDto.setNumberToBeUse(1);
						ehcPackageDto.setStatus(ACTIVE);
						ehcServiceList.add(ehcPackageDto);
					}
					packageDto.setListTPackageServicesDetailsDto(ehcServiceList);
				}
				break;
			case 2: // Either or Package
				// get service details
				if (serviceList != null && serviceList.size() > 0) {
					List<TPackageServicesDetailsDto> eitherOrList = new ArrayList<TPackageServicesDetailsDto>();
					for (ServiceForPackageResDto service : serviceList) {
						TPackageServicesDetailsDto eitherOr = new TPackageServicesDetailsDto();
						eitherOr.setIsServiceItem(1);// for Service
						eitherOr.setServiceId(service.getServiceMasterId());
						eitherOr.setSpecialityId(service.getSpecialityId());
						eitherOr.setSubSpecialityId(service.getSubSpecialityMasterId());
						eitherOr.setServiceName(service.getServiceDescription());
						eitherOr.setServiceCode(service.getServiceCode());
						eitherOr.setSpecialityName(service.getGroupDesc());
						eitherOr.setSubSpecialityName(service.getSubGroup());
						eitherOr.setServicePrice(service.getRate() == null ? 0 : service.getRate());
						eitherOr.setApportionedPrice(0.0);
						eitherOr.setTotal(0.00);
						eitherOr.setNumberToBeUse(1);
						eitherOr.setStatus(ACTIVE);
						eitherOrList.add(eitherOr);
					}
					packageDto.setListTPackageServicesDetailsDto(eitherOrList);
				}
				break;
			case 3: // package with cap
				if (reqDto.getIsServiceItem() != null && reqDto.getIsServiceItem().intValue() == 1) {
					if (serviceList != null && serviceList.size() > 0) {
						if (reqDto.getIsInclusiveService() != null) {
							List<TPackageIncExcDetailsDto> tPackageServiceIncExcDetailsDto = new ArrayList<TPackageIncExcDetailsDto>();
							for (ServiceForPackageResDto dto : serviceList) {
								TPackageIncExcDetailsDto packIncExcDto = new TPackageIncExcDetailsDto();
								packIncExcDto.setIsServiceItem(1);// for Service
								packIncExcDto.setServiceId(dto.getServiceMasterId());
								packIncExcDto.setSpecialityId(dto.getSpecialityId());
								packIncExcDto.setSubSpecialityId(dto.getSubSpecialityMasterId());
								packIncExcDto.setServiceName(dto.getServiceDescription());
								packIncExcDto.setServiceCode(dto.getServiceCode());
								packIncExcDto.setIsInclusiveService(reqDto.getIsInclusiveService());
								packIncExcDto.setIsSelected(true);
								tPackageServiceIncExcDetailsDto.add(packIncExcDto);
							}
							packageDto.setListGroupWiseIncExc(tPackageServiceIncExcDetailsDto);
						} else {
							List<TPackageServicesDetailsDto> tpackageServiceDtls = new ArrayList<TPackageServicesDetailsDto>();
							for (ServiceForPackageResDto dto : serviceList) {
								TPackageServicesDetailsDto packSrvDtls = new TPackageServicesDetailsDto();
								packSrvDtls.setIsServiceItem(1);// for Service
								packSrvDtls.setServiceId(dto.getServiceMasterId());
								packSrvDtls.setSpecialityId(dto.getSpecialityId());
								packSrvDtls.setSubSpecialityId(dto.getSubSpecialityMasterId());
								packSrvDtls.setServiceName(dto.getServiceDescription());
								packSrvDtls.setServiceCode(dto.getServiceCode());
								packSrvDtls.setServicePrice(dto.getRate() == null ? 0 : dto.getRate());
								packSrvDtls.setNumberToBeUse(1);
								packSrvDtls.setTotal(0.00);
								packSrvDtls.setIsCap(1);
								packSrvDtls.setProductCategoryId(dto.getProductCategoryId());
								tpackageServiceDtls.add(packSrvDtls);
							}
							packageDto.setListTPackageServicesDetailsDto(tpackageServiceDtls);
						}
					}
				}
				if (reqDto.getIsServiceItem() != null && reqDto.getIsServiceItem().intValue() == 2) {
					if (itemList != null && itemList.size() > 0) {
						if (reqDto.getIsInclusiveService() != null) {
							List<TPackageIncExcDetailsDto> tPackageItemIncExcDetailsDto = new ArrayList<TPackageIncExcDetailsDto>();
							for (ServiceForPackageResDto dto : itemList) {
								TPackageIncExcDetailsDto packIncExcDto = new TPackageIncExcDetailsDto();
								packIncExcDto.setIsServiceItem(2);// for item
								packIncExcDto.setItemId(dto.getItemId());
								packIncExcDto.setItemCode(dto.getItemCode());
								packIncExcDto.setItemName(dto.getItemName());
								packIncExcDto.setSubSpecialityId(dto.getSubSpecialityMasterId());
								packIncExcDto.setServiceName(dto.getServiceDescription());
								packIncExcDto.setServiceCode(dto.getServiceCode());
								packIncExcDto.setIsInclusiveService(reqDto.getIsInclusiveService());
								packIncExcDto.setIsSelected(true);
								tPackageItemIncExcDetailsDto.add(packIncExcDto);
							}
							packageDto.setListConsumableWiseIncExc(tPackageItemIncExcDetailsDto);
						} else {
							List<TPackageServicesDetailsDto> tPackageItemsDtls = new ArrayList<TPackageServicesDetailsDto>();
							for (ServiceForPackageResDto dto : itemList) {
								TPackageServicesDetailsDto packItemDtls = new TPackageServicesDetailsDto();
								packItemDtls.setIsServiceItem(2);// for item
								packItemDtls.setItemId(dto.getItemId());
								packItemDtls.setItemCode(dto.getItemCode());
								packItemDtls.setItemName(dto.getItemName());
								packItemDtls.setSubSpecialityId(dto.getSubSpecialityMasterId());
								packItemDtls.setServiceName(dto.getServiceDescription());
								packItemDtls.setServiceCode(dto.getServiceCode());
								packItemDtls.setServicePrice(dto.getRate() == null ? 0 : dto.getRate());
								packItemDtls.setTotal(0.00);
								packItemDtls.setNumberToBeUse(1);
								tPackageItemsDtls.add(packItemDtls);
							}
							packageDto.setListTPackageItemDetailsDto(tPackageItemsDtls);
						}
					}
				}
				break;
			case 4: // All exclusive package

				if (serviceList != null && serviceList.size() > 0) {
					List<TPackageIncExcDetailsDto> tPackageServiceIncExcDetailsDto = new ArrayList<TPackageIncExcDetailsDto>();
					for (ServiceForPackageResDto dto : serviceList) {
						TPackageIncExcDetailsDto packIncExcDto = new TPackageIncExcDetailsDto();
						packIncExcDto.setIsServiceItem(1);// for Service
						packIncExcDto.setServiceId(dto.getServiceMasterId());
						packIncExcDto.setSpecialityId(dto.getSpecialityId());
						packIncExcDto.setSubSpecialityId(dto.getSubSpecialityMasterId());
						packIncExcDto.setServiceName(dto.getServiceDescription());
						packIncExcDto.setServiceCode(dto.getServiceCode());
						packIncExcDto.setServicePrice(dto.getRate() == null ? 0 : dto.getRate());
						packIncExcDto.setApportionedPrice(0.0);
						packIncExcDto.setIsSelected(true);
						tPackageServiceIncExcDetailsDto.add(packIncExcDto);
					}
					packageDto.setListTPackageIncExcServiceDetailsDto(tPackageServiceIncExcDetailsDto);
				}
				if (itemList != null && itemList.size() > 0) {
					List<TPackageIncExcDetailsDto> tPackageItemIncExcDetailsDto = new ArrayList<TPackageIncExcDetailsDto>();
					for (ServiceForPackageResDto dto : itemList) {
						TPackageIncExcDetailsDto packIncExcDto = new TPackageIncExcDetailsDto();
						packIncExcDto.setIsServiceItem(2);// for item
						packIncExcDto.setItemId(dto.getItemId());
						packIncExcDto.setItemCode(dto.getItemCode());
						packIncExcDto.setItemName(dto.getItemName());
						packIncExcDto.setSubSpecialityId(dto.getSubSpecialityMasterId());
						packIncExcDto.setServiceName(dto.getServiceDescription());
						packIncExcDto.setServiceCode(dto.getServiceCode());
						packIncExcDto.setServicePrice(dto.getRate() == null ? 0 : dto.getRate());
						packIncExcDto.setIsSelected(true);
						tPackageItemIncExcDetailsDto.add(packIncExcDto);
					}
					packageDto.setListTPackageIncExcItemDetailsDto(tPackageItemIncExcDetailsDto);
				}
				break;
			case 5: // multi encounter package
				if (serviceList != null && serviceList.size() > 0) {
					List<TPackageServicesDetailsDto> tpackageServiceDtls = new ArrayList<TPackageServicesDetailsDto>();
					for (ServiceForPackageResDto dto : serviceList) {
						TPackageServicesDetailsDto packSrvDtls = new TPackageServicesDetailsDto();
						packSrvDtls.setIsServiceItem(1);// for Service
						packSrvDtls.setServiceId(dto.getServiceMasterId());
						packSrvDtls.setSpecialityId(dto.getSpecialityId());
						packSrvDtls.setSubSpecialityId(dto.getSubSpecialityMasterId());
						packSrvDtls.setServiceName(dto.getServiceDescription());
						packSrvDtls.setServiceCode(dto.getServiceCode());
						packSrvDtls.setNumberToBeUse(1);
						packSrvDtls.setServicePrice(dto.getRate() == null ? 0 : dto.getRate());
						tpackageServiceDtls.add(packSrvDtls);
					}
					packageDto.setListTPackageServicesDetailsDto(tpackageServiceDtls);
				}

				if (itemList != null && itemList.size() > 0) {
					List<TPackageServicesDetailsDto> tPackageItemsDtls = new ArrayList<TPackageServicesDetailsDto>();
					for (ServiceForPackageResDto dto : itemList) {
						TPackageServicesDetailsDto packItemDtls = new TPackageServicesDetailsDto();
						packItemDtls.setIsServiceItem(2);// for item
						packItemDtls.setItemId(dto.getItemId());
						packItemDtls.setItemCode(dto.getItemCode());
						packItemDtls.setItemName(dto.getItemName());
						packItemDtls.setSubSpecialityId(dto.getSubSpecialityMasterId());
						packItemDtls.setServiceName(dto.getServiceDescription());
						packItemDtls.setServiceCode(dto.getServiceCode());
						packItemDtls.setServicePrice(dto.getRate() == null ? 0 : dto.getRate());
						tPackageItemsDtls.add(packItemDtls);
					}
					packageDto.setListTPackageItemDetailsDto(tPackageItemsDtls);
				}
				break;
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, packageDto);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getPackgeByIds(Map<Integer, Integer> packageIds) throws ServiceException {

		try {

			List<ParentPackageMasterDto> parentPackageList = new ArrayList<ParentPackageMasterDto>();
			if (packageIds.size() > 0) {
				for (Map.Entry<Integer, Integer> entry : packageIds.entrySet()) {

					Response parentRes = iMPackageMasterDao.getParentPacakgeById(entry.getKey());
					ParentPackageMasterDto parent = (ParentPackageMasterDto) parentRes.getObject();
					switch (entry.getValue()) {
					case 1: // EHC package
						Response packageResehc = iMPackageMasterDao.getPacakgeByParentId(entry.getKey());
						parent.setPackageMasterDtoList(packageResehc.getListObject());
						if (parent.getPackageMasterDtoList() != null && parent.getPackageMasterDtoList().size() > 0) {
							for (MPackageMasterDto pacakgeDto : parent.getPackageMasterDtoList()) {
								Response serResponse = iTPackageServicesDetailsDao
										.getPackageServicesByPackageId(pacakgeDto.getPackageMasterId());
								pacakgeDto.setListTPackageServicesDetailsDto(serResponse.getListObject());
							}
						}
						parentPackageList.add(parent);
						break;
					case 2: // Either or Package
						Response packageEitherOr = iMPackageMasterDao.getPacakgeByParentId(entry.getKey());
						parent.setPackageMasterDtoList(packageEitherOr.getListObject());
						if (parent.getPackageMasterDtoList() != null && parent.getPackageMasterDtoList().size() > 0) {
							for (MPackageMasterDto pacakgeDto : parent.getPackageMasterDtoList())
							{
								Response serResponse = iTPackageServicesDetailsDao
										.getPackageServicesByPackageId(pacakgeDto.getPackageMasterId());

								//newly added code for pkg edit
								List<TPackageServicesDetailsDto> notGrpServices = null;
								if(serResponse.getListObject()!=null && serResponse.getListObject().size()>0){
									List<TPackageServicesDetailsDto> allServices = serResponse.getListObject();
									 notGrpServices = allServices
											.stream()
											.filter(service->service.getPackageEitherOrGroupId()==null)
											.collect(Collectors.toList());

								}
								pacakgeDto.setListTPackageServicesDetailsDto(notGrpServices);//end

								Response eitherOrGrpRes = iTPackageEitherOrGroupDetailsDao
										.getPackageEitherOrGroupDetailsByPackageId(pacakgeDto.getPackageMasterId());
								if (eitherOrGrpRes.getCode().equalsIgnoreCase(SUCCESS_CODE)
										&& eitherOrGrpRes.getListObject() != null)
								 {
									List<TPackageEitherorGroupDetailsDto> eihterOrGrpDtlsList = eitherOrGrpRes.getListObject();
									for (TPackageEitherorGroupDetailsDto eihterOrGrpDtls : eihterOrGrpDtlsList)
									 {
										Response eitherOrServiceRes = iTPackageServicesDetailsDao
												.getPackageEitherOrServiceEditByGroupId(eihterOrGrpDtls.getPackageGroupDetailsId());
										eihterOrGrpDtls.setListTPackageServicesDetailsDto(eitherOrServiceRes.getListObject());
									 }

									pacakgeDto.setListTPackageEitherorGroupDetailsDto(eihterOrGrpDtlsList);
								}
							}
						}
						parentPackageList.add(parent);
						break;
					case 3: // package with cap
						Response packageWithCapRes = iMPackageMasterDao.getPacakgeByParentId(entry.getKey());
						parent.setPackageMasterDtoList(packageWithCapRes.getListObject());
						if (parent.getPackageMasterDtoList() != null && parent.getPackageMasterDtoList().size() > 0) {
							for (MPackageMasterDto pacakgeDto : parent.getPackageMasterDtoList()) {

								Response serResponse = iTPackageServicesDetailsDao
										.getPackageServiceEditByPackageId(pacakgeDto.getPackageMasterId());
								Response itemRes = iTPackageServicesDetailsDao
										.getPackageItemEditByPackageId(pacakgeDto.getPackageMasterId());

								pacakgeDto.setListTPackageServicesDetailsDto(serResponse.getListObject());
								pacakgeDto.setListTPackageItemDetailsDto(itemRes.getListObject());

								Response serIExResponse = iTPackageIncExcDetailsDao
										.getTPackageIncExcDetailsForServiceByPacakgeId(pacakgeDto.getPackageMasterId());

								Response itemIExRes = iTPackageIncExcDetailsDao
										.getTPackageIncExcDetailsForItemsByPacakgeId(pacakgeDto.getPackageMasterId());

								//newly added code for pkg edit
								List<TPackageIncExcDetailsDto> listGroupWiseIncExc = serIExResponse.getListObject();
								if(listGroupWiseIncExc!=null && listGroupWiseIncExc.size()>0) {
									List<TPackageIncExcDetailsDto> filterdEServices = listGroupWiseIncExc.stream().filter(service -> service.getIsServiceItem() == 1).collect(Collectors.toList());
									pacakgeDto.setListGroupWiseIncExc(filterdEServices);
								} //end
								pacakgeDto.setListConsumableWiseIncExc(itemIExRes.getListObject());

								Response capResGroup = iTPackageCapDetailsDao
										.getPackageGroupCapDetailsByPackageId(pacakgeDto.getPackageMasterId());
								pacakgeDto.setListTPackageGroupWiseCapDetailsDto(capResGroup.getListObject());

								Response capResConsumable = iTPackageCapDetailsDao
										.getPackageConsumbaleCapDetailsByPackageId(pacakgeDto.getPackageMasterId());
								pacakgeDto.setListTPackageConsumableWiseCapDetailsDto(capResConsumable.getListObject());

								Response categoryConsumableRes = iTPackageCategoryWiseConsumableDetailsDao
										.getTPackageCategoryWiseConsumableDetails(pacakgeDto.getPackageMasterId());
								pacakgeDto.setListTPackageCategoryWiseConsumableDetailsDto(
										categoryConsumableRes.getListObject());

								Response betCatogoryRes = iTPackageBedCategoryDetailDao.getTPackageBedCategoryDetail(pacakgeDto.getPackageMasterId());
								pacakgeDto.setListTPackageBedCategoryDetailDto(betCatogoryRes.getListObject());

							}
						 }
						parentPackageList.add(parent);
						break;
					case 4: // All exclusive package
						Response packageRes = iMPackageMasterDao.getPacakgeByParentId(entry.getKey());
						parent.setPackageMasterDtoList(packageRes.getListObject());
						if (parent.getPackageMasterDtoList() != null && parent.getPackageMasterDtoList().size() > 0) {
							for (MPackageMasterDto pacakgeDto : parent.getPackageMasterDtoList()) {
								Response serResponse = iTPackageIncExcDetailsDao
										.getTPackageIncExcDetailsForServiceByPacakgeId(pacakgeDto.getPackageMasterId());
								Response itemRes = iTPackageIncExcDetailsDao
										.getTPackageIncExcDetailsForItemsByPacakgeId(pacakgeDto.getPackageMasterId());
								pacakgeDto.setListTPackageIncExcServiceDetailsDto(serResponse.getListObject());
								pacakgeDto.setListTPackageIncExcItemDetailsDto(itemRes.getListObject());
							}
						}
						parentPackageList.add(parent);
						break;
					case 5: // multi encounter package
						Response packageResMe = iMPackageMasterDao.getPacakgeByParentId(entry.getKey());
						parent.setPackageMasterDtoList(packageResMe.getListObject());
						if (parent.getPackageMasterDtoList() != null && parent.getPackageMasterDtoList().size() > 0) {
							for (MPackageMasterDto pacakgeDto : parent.getPackageMasterDtoList()) {
								Response serResponse = iTPackageServicesDetailsDao
										.getPackageServiceEditByPackageId(pacakgeDto.getPackageMasterId());
								Response itemRes = iTPackageServicesDetailsDao
										.getPackageItemEditByPackageId(pacakgeDto.getPackageMasterId());
								pacakgeDto.setListTPackageServicesDetailsDto(serResponse.getListObject());
								pacakgeDto.setListTPackageItemDetailsDto(itemRes.getListObject());
							}
						}
						parentPackageList.add(parent);
						break;
					}
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, parentPackageList, null);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	@Transactional
	public Response packageSearchForBilling(ServiceSearchReqDto reqDto) throws ServiceException {

		try {
			List<ServiceSearchResDto>  list = new ArrayList<>();
			List<ServiceSearchResDto>  finalList = new ArrayList<>();
			Response res = iMPackageMasterDao.packageSearchForBilling(reqDto);

			if(res.getListObject() != null && res.getListObject().size()>0){
				list.addAll(res.getListObject());

				int count = 0;
				for (ServiceSearchResDto serviceSearchResDto : list) {

					if(count ==0){

						finalList.add(serviceSearchResDto);
						count =1;

					}else{

						Boolean checkServiceExits = true;
							for (ServiceSearchResDto dto : finalList) {

								if(dto.getServiceMasterId().equals(serviceSearchResDto.getServiceMasterId())){
									checkServiceExits = false;
								}

							}

							if(checkServiceExits){
								finalList.add(serviceSearchResDto);
							}

						}

					}
				}



			return new Response<>(SUCCESS, SUCCESS_CODE, null, finalList, null);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response parentPackage(ServiceSearchReqDto reqDto) throws ServiceException {
		try {
			return iMPackageMasterDao.parentPackage(reqDto);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response checkIfServiceFromPackage(ContractConsumptionDto consumptionDto) throws ServiceException {
		try {

			ContractConsumptionDto cloneDto = consumptionDto;
			consumptionDto.setTariffId(consumptionDto.getTariffId() == null ? consumptionDto.getDefaultSelfTariffId()
					: consumptionDto.getTariffId());
			Integer patientId = consumptionDto.getPatientId();
			Integer encounterId = consumptionDto.getEncounterId() != null ? consumptionDto.getEncounterId()
					: consumptionDto.getAdmissionId();
			Integer visitTypeId = consumptionDto.getVisitTypeId();
			Integer organisationId = consumptionDto.getOrganizationId();
			Integer unitId = consumptionDto.getUnitId();
			String orderDate= consumptionDto.getCurrentDate()!=null ? consumptionDto.getCurrentDate() : GlobalCommonDateUtils.getStringDate(new Date(),"yyyy-MM-dd HH:mm:ss");
			consumptionDto.setCurrentDate(orderDate);
			Integer billingBedCategoryId = consumptionDto.getBillingBedCategoryId();
			Integer serviceId = consumptionDto.getServiceId();
			Integer serviceQty = consumptionDto.getQuantity() == null ? 1 : consumptionDto.getQuantity().intValue();
			Double serviceRate = consumptionDto.getChangedPrice() == null ? consumptionDto.getChangedPrice()
					: consumptionDto.getServiceRate() != null ? consumptionDto.getServiceRate() : null;

			Response incExcRes = null;

			// for check service rate in seltContract

			Response packageResponse = iTPackageConsumptionMasterDao.getExistingActivePackageByPatientEncounter(
					patientId, encounterId, visitTypeId, organisationId, unitId, false, billingBedCategoryId);
			if (packageResponse.getListObject() != null && !packageResponse.getListObject().isEmpty()) {

				ContractConsumptionDto consumptionDtoClone = consumptionDto;
				List<ContractConsumptionDto> consumptionDtoList = new ArrayList<ContractConsumptionDto>();

				Response serviceRateRes = iContractConsumptionService.getSerivceRateGlobal(consumptionDto, false, false);
				if (serviceRateRes.getObject() != null) {
					consumptionDto = (ContractConsumptionDto) serviceRateRes.getObject();
					consumptionDtoList = serviceRateRes.getListObject();
				}
				consumptionDto.setServiceId(consumptionDto.getServiceId());
				consumptionDto.setSelfPayable((serviceRate == null ? consumptionDto.getServiceRate() : serviceRate) * serviceQty);
				consumptionDto.setConsumeService(consumptionDtoClone.getConsumeService());
				consumptionDto.setQuantity(serviceQty.doubleValue());
				consumptionDto.setContractId(consumptionDtoClone.getContractId());
				consumptionDto.setAdmissionId(consumptionDtoClone.getAdmissionId());
				consumptionDto.setPatientCategoryId(consumptionDtoClone.getPatientTypeId());
				consumptionDto.setTariffId(consumptionDtoClone.getTariffId());
				consumptionDto.setVisitTypeId(consumptionDtoClone.getVisitTypeId());
				consumptionDto.setOrderDetailsPackageMapperDtosList(null);
				consumptionDto.setDocSplId(consumptionDtoClone.getDocSplId() != null ? consumptionDtoClone.getDocSplId() : null);
				consumptionDto.setIsConsultationService(consumptionDtoClone.getIsConsultationService() != null ? consumptionDtoClone.getIsConsultationService() : null);
				consumptionDto.setPatientTypeId(consumptionDto.getPatientCategoryId());

				Integer specialityId = consumptionDto.getSpecialityId();
				Integer subSpecialityId = consumptionDto.getSubSpecialityId();

				List<PackageConsumptionMasterResDto> listResDto = packageResponse.getListObject();

				for (PackageConsumptionMasterResDto resDto : listResDto) {
					Date validityFrom = resDto.getValidityFrom() != null
							? GlobalCommonDateUtils.getDate(resDto.getValidityFrom(), "dd-MM-yyyy HH:mm:ss")
							: GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "yyyy-MM-dd HH:mm:ss");
					
					Date todaysDate = GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "yyyy-MM-dd HH:mm:ss");
					Date validityTo = resDto.getValidityTo() != null
							? GlobalCommonDateUtils.getDate(resDto.getValidityTo(), "dd-MM-yyyy HH:mm:ss")
							: GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "yyyy-MM-dd HH:mm:ss");
					Long packEffectiveDate = GlobalCommonDateUtils
							.getDate(resDto.getPackageApplicableDate(), "dd-MM-yyyy").getTime();
					
					Long orderDateLong = GlobalCommonDateUtils.getDate(orderDate, "yyyy-MM-dd HH:mm:ss").getTime();

					/*boolean isPackageEffective = (resDto.getPackageTypeId() == 5 || resDto.getPackageTypeId() == 4 || resDto.getPackageTypeId() == 2) ?
							(packEffectiveDate <= orderDateLong && todaysDate.compareTo(validityFrom) >= 0 && todaysDate.compareTo(validityTo) <= 0)
							: packEffectiveDate <= orderDateLong;*/
					boolean isPackageEffective = false;
					if (resDto.getPackageTypeId() == 5 || resDto.getPackageTypeId() == 4 || resDto.getPackageTypeId() == 2) {
						isPackageEffective = (packEffectiveDate <= orderDateLong
								&& todaysDate.compareTo(validityFrom) >= 0 && todaysDate.compareTo(validityTo) <= 0);
					}else if(resDto.getValidityTo()!=null){
						isPackageEffective = packEffectiveDate <= orderDateLong && todaysDate.compareTo(validityTo) <= 0;
					}else{
						isPackageEffective = packEffectiveDate <= orderDateLong;
					}

					if (isPackageEffective) {

						Response resPackServDetails = null;
						boolean isDateValid = todaysDate.compareTo(validityFrom) >= 0 && todaysDate.compareTo(validityTo) <= 0;
						switch (resDto.getPackageTypeId()) {
						case 1: // EHC package

							break;
						case 2: // Either or Package
							// get service details
							resPackServDetails = iTPackageConsumptionMasterDao
									.getPackageConsumptionServiceDetailsByPackageIdPatientId(
											resDto.getPackageConsumptionMasterId());

							ListIterator<PackageConsumptionServiceDetailDto> servicesItr = resPackServDetails
									.getListObject().listIterator();

							if (isDateValid) {
								while (servicesItr.hasNext()) {

									PackageConsumptionServiceDetailDto serv = servicesItr.next();
									if (serv.getPackageEODetailId() != null && serv.getServiceId() != null
											&& serv.getServiceId().equals(serviceId)) {
										// get cap details
										Response eoRes = iTPackageConsumptionEoDetailsDao
												.getTPackageConsumptionEoDetailsByGrpIdConsumptionId(
														serv.getPackageEODetailId(),
														resDto.getPackageConsumptionMasterId());
										if (eoRes.getCode().equals(SUCCESS_CODE) && eoRes.getListObject() != null
												&& !eoRes.getListObject().isEmpty()) {
											ListIterator<PackageConsumptionEODetailDto> eoItr = eoRes.getListObject()
													.listIterator();
											while (eoItr.hasNext()) {
												PackageConsumptionEODetailDto eoDetails = eoItr.next();
												Integer availableQty = eoDetails.getBalanceQty();
												if (availableQty > 0
														&& eoDetails.getPackageEOGroupId()
																.equals(serv.getPackageEODetailId())
														&& consumptionDto.getSelfPayable().doubleValue() > 0)

												{

													if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null
															&& !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
														OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
														oDPmpprDto.setPackageId(resDto.getPackageId());
														oDPmpprDto.setIsPackageService('Y');
														oDPmpprDto.setIsCapService('N');
														oDPmpprDto.setPackageName(resDto.getPackageName());
														oDPmpprDto.setPackageCode(resDto.getPackageCode());
														oDPmpprDto.setPackageConsumptionEODetailId(
																eoDetails.getPackageConsumptionEODetailId());
														oDPmpprDto.setPackageConsumptionMasterId(
																eoDetails.getPackageConsumptionMasterId());
														oDPmpprDto.setPackageAmount(
																BigDecimal.valueOf(consumptionDto.getSelfPayable()));
														oDPmpprDto.setServiceAmount(
																BigDecimal.valueOf(consumptionDto.getServiceRate()));
														consumptionDto.setPackageConsumptionAmt(
																consumptionDto.getPackageConsumptionAmt().add(BigDecimal
																		.valueOf(consumptionDto.getSelfPayable())));

														if (availableQty >= serviceQty) {
															oDPmpprDto.setPackageQty(serviceQty);
															iTPackageConsumptionCapDetailsDao.updateEitherOrQuantity(
																	eoDetails.getPackageConsumptionEODetailId(),
																	(serviceQty) * -1,false);
															eoDetails.setBalanceQty(availableQty - serviceQty);
														} else {
															serviceQty = serviceQty - availableQty;
															oDPmpprDto.setPackageQty(availableQty);
															iTPackageConsumptionCapDetailsDao.updateEitherOrQuantity(
																	eoDetails.getPackageConsumptionEODetailId(),
																	(1 * -1),false);
															eoDetails.setBalanceQty(0);
														}
														consumptionDto.getOrderDetailsPackageMapperDtosList()
																.add(oDPmpprDto);
														consumptionDto.setSelfPayable(0.00);
													} else {
														List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
														OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
														oDPmpprDto.setPackageId(resDto.getPackageId());
														oDPmpprDto.setIsPackageService('Y');
														oDPmpprDto.setIsCapService('N');
														oDPmpprDto.setPackageName(resDto.getPackageName());
														oDPmpprDto.setPackageCode(resDto.getPackageCode());
														oDPmpprDto.setPackageConsumptionEODetailId(
																eoDetails.getPackageConsumptionEODetailId());
														oDPmpprDto.setPackageConsumptionMasterId(
																eoDetails.getPackageConsumptionMasterId());
														oDPmpprDto.setPackageAmount(
																BigDecimal.valueOf(consumptionDto.getSelfPayable()));
														oDPmpprDto.setServiceAmount(
																BigDecimal.valueOf(consumptionDto.getServiceRate()));
														consumptionDto.setPackageConsumptionAmt(
																consumptionDto.getPackageConsumptionAmt().add(BigDecimal
																		.valueOf(consumptionDto.getSelfPayable())));

														if (availableQty >= serviceQty) {
															oDPmpprDto.setPackageQty(serviceQty);
															iTPackageConsumptionCapDetailsDao.updateEitherOrQuantity(
																	eoDetails.getPackageConsumptionEODetailId(),
																	(serviceQty) * -1,false);
															eoDetails.setBalanceQty(availableQty - serviceQty);
														} else {
															serviceQty = serviceQty - availableQty;
															oDPmpprDto.setPackageQty(1);
															iTPackageConsumptionCapDetailsDao.updateEitherOrQuantity(
																	eoDetails.getPackageConsumptionEODetailId(),
																	(1 * -1),false);
															eoDetails.setBalanceQty(0);
														}

														mppr.add(oDPmpprDto);
														consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
														consumptionDto.setSelfPayable(0.00);
													}

													return new Response<>(SUCCESS, SUCCESS_CODE,
															SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, consumptionDtoList,
															consumptionDto);
												}
											}
										}
									}
									if (serv.getPackageEODetailId() == null && serv.getServiceId() != null
											&& serv.getServiceId().equals(serviceId)
											&& consumptionDto.getSelfPayable().doubleValue() > 0) {
										// check if available in services

										if (serv.getBalanceQuantity() != null && serv.getBalanceQuantity() > 0) {
											Integer availableQty = serv.getBalanceQuantity();
											if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null
													&& consumptionDto.getOrderDetailsPackageMapperDtosList()
															.size() > 0) {
												OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
												oDPmpprDto.setPackageId(resDto.getPackageId());
												oDPmpprDto.setIsPackageService('Y');
												oDPmpprDto.setIsCapService('N');
												oDPmpprDto.setPackageName(resDto.getPackageName());
												oDPmpprDto.setPackageCode(resDto.getPackageCode());
												oDPmpprDto.setPacakgeConsumptionServiceDetailId(
														serv.getPackageConsumptionServiceDetailId());
												oDPmpprDto.setPackageConsumptionMasterId(
														serv.getPackageConsumptionMasterId());
												oDPmpprDto.setPackageAmount(
														BigDecimal.valueOf(consumptionDto.getSelfPayable()));
												oDPmpprDto.setServiceAmount(
														BigDecimal.valueOf(consumptionDto.getServiceRate()));
												consumptionDto.setPackageConsumptionAmt(
														consumptionDto.getPackageConsumptionAmt().add(
																BigDecimal.valueOf(consumptionDto.getSelfPayable())));
												if (availableQty >= serviceQty) {
													oDPmpprDto.setPackageQty(serviceQty);
													availableQty = availableQty - serviceQty;
													iTPackageConsumptionCapDetailsDao.updatePackageQty(
															serv.getPackageConsumptionServiceDetailId(),
															(serviceQty) * -1);
													serv.setBalanceQuantity(availableQty);
												} else {
													serviceQty = serviceQty - availableQty;
													oDPmpprDto.setPackageQty(1);
													iTPackageConsumptionCapDetailsDao.updatePackageQty(
															serv.getPackageConsumptionServiceDetailId(), (1 * -1));
													serv.setBalanceQuantity(0);
												}
												consumptionDto.getOrderDetailsPackageMapperDtosList().add(oDPmpprDto);
												consumptionDto.setSelfPayable(0.00);
											} else {
												List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
												OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
												oDPmpprDto.setPackageId(resDto.getPackageId());
												oDPmpprDto.setIsPackageService('Y');
												oDPmpprDto.setIsCapService('N');
												oDPmpprDto.setPackageName(resDto.getPackageName());
												oDPmpprDto.setPackageCode(resDto.getPackageCode());
												oDPmpprDto.setPacakgeConsumptionServiceDetailId(
														serv.getPackageConsumptionServiceDetailId());
												oDPmpprDto.setPackageConsumptionMasterId(
														serv.getPackageConsumptionMasterId());
												oDPmpprDto.setPackageAmount(
														BigDecimal.valueOf(consumptionDto.getSelfPayable()));
												oDPmpprDto.setServiceAmount(
														BigDecimal.valueOf(consumptionDto.getServiceRate()));
												consumptionDto.setPackageConsumptionAmt(
														consumptionDto.getPackageConsumptionAmt().add(
																BigDecimal.valueOf(consumptionDto.getSelfPayable())));
												if (availableQty >= serviceQty) {
													oDPmpprDto.setPackageQty(serviceQty);
													availableQty = availableQty - serviceQty;
													iTPackageConsumptionCapDetailsDao.updatePackageQty(
															serv.getPackageConsumptionServiceDetailId(),
															(serviceQty) * -1);
													serv.setBalanceQuantity(availableQty);
												} else {
													serviceQty = serviceQty - availableQty;
													oDPmpprDto.setPackageQty(1);
													iTPackageConsumptionCapDetailsDao.updatePackageQty(
															serv.getPackageConsumptionServiceDetailId(), (1 * -1));
													serv.setBalanceQuantity(0);
												}

												mppr.add(oDPmpprDto);
												consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
												consumptionDto.setSelfPayable(0.00);
											}
											return new Response<>(SUCCESS, SUCCESS_CODE,
													SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, consumptionDtoList,
													consumptionDto);

										}
									}
								}
							}
							break;
						case 3:
							// package with cap

							// check inclusion exclusion service
							if (resDto.isBillingBedExist()) {
								resPackServDetails = iTPackageConsumptionMasterDao
										.getPackageConsumptionServiceDetailsByPackageIdPatientId(
												resDto.getPackageConsumptionMasterId());
								resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());
								// check service
								if (resPackServDetails.getListObject() != null && !resPackServDetails.getListObject().isEmpty()) {
									ListIterator<PackageConsumptionServiceDetailDto> resSrvItr = resPackServDetails
											.getListObject().listIterator();
									PackageConsumptionServiceDetailDto srv = null;
									while (resSrvItr.hasNext()) {
										srv = resSrvItr.next();
										if (srv.getServiceId() != null
												&& srv.getServiceId().equals(consumptionDto.getServiceId())
												&& srv.getAvailableCap() != null
												&& consumptionDto.getSelfPayable().doubleValue() > 0
												&& srv.getAvailableCap() > 0) {

											if (consumptionDto.getSelfPayable() <= srv.getAvailableCap()) {
												double capAmount = srv.getAvailableCap() - consumptionDto.getSelfPayable();
												srv.setAvailableCap(capAmount);
												Double consumeAmt = consumptionDto.getSelfPayable();
												/** if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
													consumptionDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(resDto,srv,consumptionDto,consumeAmt,'Y',true));
                                                } else {
													List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
													mppr.add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(resDto,srv,consumptionDto,consumeAmt,'Y',true));
													consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
                                                }*/
												consumptionDto = billingCommonMethods.updatePackageServiceCap(resDto,srv,consumptionDto,consumeAmt,'Y',true);
                                                consumptionDto.setSelfPayable(0.00);
                                                return new Response<>(SUCCESS, SUCCESS_CODE, SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, consumptionDtoList, consumptionDto);
                                            } else {
												consumptionDto.setSelfPayable(consumptionDto.getSelfPayable() - srv.getAvailableCap());
												double capAmount = srv.getAvailableCap();
												srv.setAvailableCap(0.00);
/**												if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
													consumptionDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(resDto,srv,consumptionDto,capAmount,'Y',true));
												} else {
													List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
													mppr.add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(resDto,srv,consumptionDto,capAmount,'Y',true));
													consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
												}*/
												consumptionDto = billingCommonMethods.updatePackageServiceCap(resDto,srv,consumptionDto,capAmount,'Y',true);

											}
										}
									}
									// for bedCategory
									// for change bed category room Rent
									if (visitTypeId != 1) {
										Response bedCategroyRes = iTPackageBedCategoryDetailDao.getTPackageBedCategoryDetailBilling(resDto.getPackageConsumptionMasterId());
										if (bedCategroyRes.getListObject() != null && !bedCategroyRes.getListObject().isEmpty()) {
											Response bedTrasnferRes = iTPackageBedCategoryDetailDao.getPatientApplicableDaysByAdmissionId(encounterId);
											if (bedTrasnferRes.getListObject() != null && !bedTrasnferRes.getListObject().isEmpty()
													&& consumptionDto.getSelfPayable().doubleValue() > 0) {
												List<TPackageBedCategoryDetailDto> betTrasList = bedTrasnferRes.getListObject();
												List<TPackageBedCategoryDetailDto> bedCategoryRes = bedCategroyRes.getListObject();
												List<PackageConsumptionServiceDetailDto> srvItr = resPackServDetails.getListObject();
												for (TPackageBedCategoryDetailDto transferReq : betTrasList) {
													for (TPackageBedCategoryDetailDto bdCtrDto : bedCategoryRes) {
														if (transferReq.getBedCategoryId().equals(bdCtrDto.getBedCategoryId())
																&& transferReq.getApplicableDays() <= bdCtrDto.getApplicableDays()) {

                                                            for (PackageConsumptionServiceDetailDto pcServDto : srvItr) {
                                                                if (pcServDto.getServiceBedCategoryId() != null && pcServDto.getServiceBedCategoryId() > 0) {

                                                                    PackageConsumptionServiceDetailDto service = srvItr.stream()
                                                                            .filter(p -> p.getServiceId() != null)
                                                                            .filter(p -> (p.getServiceBedCategoryId() != null
                                                                                    && p.getServiceBedCategoryId().equals(pcServDto.getServiceBedCategoryId())
                                                                                    && p.getServiceId().equals(serviceId))).findFirst().orElse(null);
                                                                    if (service != null && consumptionDto.getSelfPayable() <= service.getAvailableCap()) {
																		assert srv != null;
																		srv.setAvailableCap(srv.getAvailableCap() - consumptionDto.getSelfPayable());
                                                                     /**   if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null
                                                                                && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
                                                                            consumptionDto.getOrderDetailsPackageMapperDtosList()
																					.add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(resDto,srv,consumptionDto,consumptionDto.getSelfPayable(),'Y',true));

                                                                        } else {
                                                                            List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
                                                                            mppr.add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(resDto,srv,consumptionDto,consumptionDto.getSelfPayable(),'Y',true));
                                                                            consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);

                                                                        }*/
																	 	consumptionDto = billingCommonMethods.updatePackageServiceCap(resDto,srv,consumptionDto,consumptionDto.getSelfPayable(),'Y',true);
                                                                        consumptionDto.setSelfPayable(0.00);
                                                                        return new Response<>(SUCCESS, SUCCESS_CODE, SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, consumptionDtoList, consumptionDto);
                                                                    } else {
                                                                        consumptionDto.setSelfPayable(consumptionDto.getSelfPayable() - srv.getAvailableCap());
                                                                        double capAmount = srv.getAvailableCap();
                                                                        srv.setAvailableCap(0.00);
                                                                       /* if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null
                                                                                && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
                                                                            consumptionDto.getOrderDetailsPackageMapperDtosList()
																					.add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(resDto,srv,consumptionDto,capAmount,'Y',true));
                                                                        } else {
                                                                            List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
                                                                            mppr.add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(resDto,srv,consumptionDto,capAmount,'Y',true));
                                                                            consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
                                                                            iTPackageConsumptionCapDetailsDao.updatePackageServiceCap(srv.getPackageConsumptionServiceDetailId(), capAmount * -1);
                                                                        }*/
																		consumptionDto = billingCommonMethods.updatePackageServiceCap(resDto,srv,consumptionDto,capAmount,'Y',true);

																	}
                                                                }
                                                            }
														}
													}
												}
											}

										}
									}
								}

								incExcRes = iTPackageIncExcDetailsDao.getTPackageIncExcDetailsForBillingByPackageConsumptionId(resDto.getPackageConsumptionMasterId());
								// get cap details
								Response packageCapRes = iPackageConsumptionDao.getPackageConsumptionCapDetails(resDto.getPackageConsumptionMasterId());
								resDto.setListTPackageConsumptionCapDetailsDto(packageCapRes.getListObject());
								if (packageCapRes.getListObject() != null && !packageCapRes.getListObject().isEmpty()) {
									ListIterator<TPackageConsumptionCapDetailsDto> capDtlsItr = packageCapRes.getListObject().listIterator();
									TPackageConsumptionCapDetailsDto capDtls = null;
									while (capDtlsItr.hasNext()) {
										capDtls = capDtlsItr.next();
										// get service details by id

										if (consumptionDto.getSpecialityId() != null && consumptionDto.getSubSpecialityId() != null
												&& capDtls.getDepartmentId() != null && capDtls.getSubDepartmentId() != null) {
											if (capDtls.getDepartmentId().equals(consumptionDto.getSpecialityId())
													&& capDtls.getSubDepartmentId().equals(consumptionDto.getSubSpecialityId())
													&& consumptionDto.getSelfPayable().doubleValue() > 0) {

												if (incExcRes.getListObject() != null && !incExcRes.getListObject().isEmpty()
														&& consumptionDto.getSelfPayable().doubleValue() > 0) {
													List<TPackageIncExcDetailsDto> incexcItr = incExcRes.getListObject();
													Optional<TPackageIncExcDetailsDto> result = incexcItr.stream()
															.parallel()
															.filter(service -> service.getServiceId() != null
																	&& service.getServiceId().equals(serviceId)
																	&& service.getIsInclusiveService() == 2)
															.findAny();

													if (result.isPresent()) {

														// Response ress =
														// iContractConsumptionService.consumeServiceFromContract(consumptionDtoClone);
														break;
														// return new
														// Response<>(ERROR,
														// NOT_FOUND_CODE,
														// SERVICE_NOT_AVAILABLE_IN_ACTIVE_PACKAGE_AS_EXC,
														// serviceRateRes.getListObject(),
														// ress.getObject());
													}
												}

												if (capDtls.getAvailableCap() > 0 && consumptionDto.getSelfPayable().doubleValue() > 0) {
													if (consumptionDto.getSelfPayable() <= capDtls.getAvailableCap().doubleValue()) {
														double capAmount = capDtls.getAvailableCap() - consumptionDto.getSelfPayable();
														capDtls.setAvailableCap(capAmount);
														/**if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
															consumptionDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageCap(resDto,capDtls,consumptionDto,consumptionDto.getSelfPayable()));
                                                        } else {
															List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
															mppr.add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageCap(resDto,capDtls,consumptionDto,capAmount));
															consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
                                                        }*/
														consumptionDto = billingCommonMethods.updatePackageCap(resDto,capDtls,consumptionDto,capAmount);
                                                        consumptionDto.setSelfPayable(0.00);
                                                        return new Response<>(SUCCESS, SUCCESS_CODE, SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, consumptionDtoList, consumptionDto);
                                                    } else {
														consumptionDto.setSelfPayable(consumptionDto.getSelfPayable()
																- capDtls.getAvailableCap());
														double capAmount = capDtls.getAvailableCap();
														capDtls.setAvailableCap(0.00);
														consumptionDto = billingCommonMethods.updatePackageCap(resDto,capDtls,consumptionDto,capAmount);
													}
												}
											}
										} else if (capDtls.getDepartmentId() != null
												&& capDtls.getSubDepartmentId() == null) {
											if (capDtls.getDepartmentId().equals(consumptionDto.getSpecialityId())
													&& consumptionDto.getSelfPayable().doubleValue() > 0) {

												if (incExcRes.getListObject() != null
														&& incExcRes.getListObject().size() > 0
														&& consumptionDto.getSelfPayable().doubleValue() > 0) {
													List<TPackageIncExcDetailsDto> incexcItr = incExcRes
															.getListObject();
													Optional<TPackageIncExcDetailsDto> result = incexcItr.stream()
															.parallel()
															.filter(service -> service.getServiceId() != null
																	&& service.getServiceId().equals(serviceId)
																	&& service.getIsInclusiveService() == 2)
															.findAny();

													if (result.isPresent()) {
														break;
														// return new
														// Response<>(ERROR,
														// NOT_FOUND_CODE,
														// SERVICE_NOT_AVAILABLE_IN_ACTIVE_PACKAGE_AS_EXC,
														// serviceRateRes.getListObject(),
														// consumptionDto);
													}
												}
												if (capDtls.getAvailableCap() > 0
														&& consumptionDto.getSelfPayable().doubleValue() > 0) {
													if (consumptionDto.getSelfPayable() <= capDtls.getAvailableCap().doubleValue()) {
														double capAmount = capDtls.getAvailableCap() - consumptionDto.getSelfPayable();
														capDtls.setAvailableCap(capAmount);
													/**	if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null
																&& !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
//															OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
//															oDPmpprDto.setPackageId(resDto.getPackageId());
//															oDPmpprDto.setIsPackageService('Y');
//															oDPmpprDto.setIsCapService('Y');
//															oDPmpprDto.setPackageName(resDto.getPackageName());
//															oDPmpprDto.setPackageCode(resDto.getPackageCode());
//															oDPmpprDto.setPackageConsumptionMasterId(
//																	capDtls.getPackageConsumptionMasterId());
//															oDPmpprDto.setPackageConsumptionCapDetailsId(
//																	capDtls.getPackageConsumptionCapDetailsId());
//															oDPmpprDto.setPackageAmount(BigDecimal
//																	.valueOf(consumptionDto.getSelfPayable()));
//															oDPmpprDto.setServiceAmount(BigDecimal
//																	.valueOf(consumptionDto.getServiceRate()));
//															consumptionDto.setPackageConsumptionAmt(consumptionDto
//																	.getPackageConsumptionAmt().add(BigDecimal
//																			.valueOf(consumptionDto.getSelfPayable())));
															consumptionDto.getOrderDetailsPackageMapperDtosList()
																	.add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageCap(resDto,capDtls,consumptionDto,consumptionDto.getSelfPayable()));
//															iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(),
//																	consumptionDto.getSelfPayable() * -1);
                                                        } else {
//															double capAmount = capDtls.getAvailableCap()
//																	- consumptionDto.getSelfPayable();
//															capDtls.setAvailableCap(capAmount);
															List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
															/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
															oDPmpprDto.setPackageId(resDto.getPackageId());
															oDPmpprDto.setIsPackageService('Y');
															oDPmpprDto.setIsCapService('Y');
															oDPmpprDto.setPackageName(resDto.getPackageName());
															oDPmpprDto.setPackageCode(resDto.getPackageCode());
															oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
															oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
															oDPmpprDto.setPackageAmount(BigDecimal.valueOf(consumptionDto.getSelfPayable()));
															oDPmpprDto.setServiceAmount(BigDecimal.valueOf(consumptionDto.getServiceRate()));
															consumptionDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt().add(BigDecimal
																			.valueOf(consumptionDto.getSelfPayable())));*/
														/**	mppr.add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageCap(resDto,capDtls,consumptionDto,consumptionDto.getSelfPayable()));
															consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
//															iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(),
//																	consumptionDto.getSelfPayable() * -1);
                                                        }*/
														consumptionDto = billingCommonMethods.updatePackageCap(resDto,capDtls,consumptionDto,consumptionDto.getSelfPayable());
                                                        consumptionDto.setSelfPayable(0.00);
                                                        return new Response<>(SUCCESS, SUCCESS_CODE, SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, consumptionDtoList, consumptionDto);
                                                    } else {
														consumptionDto.setSelfPayable(consumptionDto.getSelfPayable() - capDtls.getAvailableCap());
														double capAmount = capDtls.getAvailableCap();
														capDtls.setAvailableCap(0.00);
														/**		if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null
																&& !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
															OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
															oDPmpprDto.setPackageId(resDto.getPackageId());
															oDPmpprDto.setIsPackageService('Y');
															oDPmpprDto.setIsCapService('Y');
															oDPmpprDto.setPackageName(resDto.getPackageName());
															oDPmpprDto.setPackageCode(resDto.getPackageCode());
															oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
															oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
															oDPmpprDto.setPackageAmount(BigDecimal.valueOf(capAmount));
															oDPmpprDto.setServiceAmount(BigDecimal.valueOf(consumptionDto.getServiceRate()));
															consumptionDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt()
																			.add(BigDecimal.valueOf(capAmount)));
															consumptionDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageCap(resDto,capDtls,consumptionDto,capAmount));
//															iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(),
//																	capAmount * -1);
														} else {
															List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
															/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
															oDPmpprDto.setPackageId(resDto.getPackageId());
															oDPmpprDto.setIsPackageService('Y');
															oDPmpprDto.setIsCapService('Y');
															oDPmpprDto.setPackageName(resDto.getPackageName());
															oDPmpprDto.setPackageCode(resDto.getPackageCode());
															oDPmpprDto.setPackageConsumptionCapDetailsId(
																	capDtls.getPackageConsumptionCapDetailsId());
															oDPmpprDto.setPackageConsumptionMasterId(
																	capDtls.getPackageConsumptionMasterId());
															oDPmpprDto.setPackageAmount(BigDecimal.valueOf(capAmount));
															oDPmpprDto.setServiceAmount(BigDecimal
																	.valueOf(consumptionDto.getServiceRate()));
															consumptionDto.setPackageConsumptionAmt(
																	consumptionDto.getPackageConsumptionAmt()
																			.add(BigDecimal.valueOf(capAmount)));
															mppr.add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageCap(resDto,capDtls,consumptionDto,capAmount));
															consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
//															iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(), capAmount * -1);
														}*/

														consumptionDto = billingCommonMethods.updatePackageCap(resDto,capDtls,consumptionDto,capAmount);
													}
												}
											}
										}
									}
								}
							}
							break;
						case 4: // All exclusive package
							resPackServDetails = iTPackageConsumptionMasterDao
									.getPackageConsumptionServiceDetailsByPackageIdPatientId(
											resDto.getPackageConsumptionMasterId());
							resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());

							// check inclusion exclusion service
							incExcRes = iTPackageIncExcDetailsDao
									.getTPackageIncExcDetailsForBillingByPackageConsumptionId(
											resDto.getPackageConsumptionMasterId());

							if (isDateValid) {
								if (incExcRes.getListObject() != null && !incExcRes.getListObject().isEmpty()
										&& consumptionDto.getSelfPayable().doubleValue() > 0) {
									List<TPackageIncExcDetailsDto> incexcItr = incExcRes.getListObject();
									Optional<TPackageIncExcDetailsDto> result = incexcItr.stream().parallel().filter(
											service -> (service.getDeptId() != null && service.getSubDeptId() == null)
													&& service.getDeptId().equals(specialityId)
													&& service.getIsInclusiveService() == 2)
											.findAny();
									if (result.isPresent())
										break;

									result = incexcItr.stream().parallel().filter(
											service -> (service.getDeptId() != null && service.getSubDeptId() != null)
													&& service.getDeptId().equals(specialityId)
													&& service.getSubDeptId().equals(subSpecialityId)
													&& service.getIsInclusiveService() == 2)
											.findAny();
									if (result.isPresent())
										break;

									result = incexcItr.stream().parallel()
											.filter(service -> service.getServiceId() != null
													&& service.getServiceId().equals(serviceId)
													&& service.getIsInclusiveService() == 2)
											.findAny();
									if (result.isPresent())
										break;

									if (!result.isPresent()) {
									/**	if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null
												&& !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
											/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
											oDPmpprDto.setPackageId(resDto.getPackageId());
											oDPmpprDto.setIsPackageService('Y');
											oDPmpprDto.setIsCapService('N');
											oDPmpprDto.setPackageName(resDto.getPackageName());
											oDPmpprDto.setPackageCode(resDto.getPackageCode());
											oDPmpprDto.setPackageAmount(
													BigDecimal.valueOf(consumptionDto.getSelfPayable()));
											oDPmpprDto.setServiceAmount(
													BigDecimal.valueOf(consumptionDto.getServiceRate()));
											oDPmpprDto.setPackageConsumptionMasterId(
													resDto.getPackageConsumptionMasterId());
											consumptionDto
													.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt()
															.add(BigDecimal.valueOf(consumptionDto.getSelfPayable())));
											consumptionDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(resDto,null,consumptionDto,consumptionDto.getSelfPayable(),'N',false));
										} else {
											List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
									/**		OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
											oDPmpprDto.setPackageId(resDto.getPackageId());
											oDPmpprDto.setIsPackageService('Y');
											oDPmpprDto.setIsCapService('N');
											oDPmpprDto.setPackageName(resDto.getPackageName());
											oDPmpprDto.setPackageCode(resDto.getPackageCode());
											oDPmpprDto.setPackageAmount(
													BigDecimal.valueOf(consumptionDto.getSelfPayable()));
											oDPmpprDto.setServiceAmount(
													BigDecimal.valueOf(consumptionDto.getServiceRate()));
											oDPmpprDto.setPackageConsumptionMasterId(
													resDto.getPackageConsumptionMasterId());
											consumptionDto
													.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt()
															.add(BigDecimal.valueOf(consumptionDto.getSelfPayable())));
											mppr.add(billingCommonMethods.getOrderDetailsPackageMapperDtoAndUpdatePackageServiceCap(resDto,null,consumptionDto,consumptionDto.getSelfPayable(),'N',false));
											consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
										}*/
                                        consumptionDto = billingCommonMethods.updatePackageServiceCap(resDto,null,consumptionDto,consumptionDto.getSelfPayable(),'N',false);
										consumptionDto.setSelfPayable(0.00);

										// available as inclusive service in
										// package
										return new Response<>(SUCCESS, SUCCESS_CODE,
												SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE_AS_INC, consumptionDtoList,
												consumptionDto);
									}

								}
							}
							break;
						case 5: // multi encounter package
							resPackServDetails = iTPackageConsumptionMasterDao
									.getPackageConsumptionServiceDetailsByPackageIdPatientId(
											resDto.getPackageConsumptionMasterId());
							resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());

							if (isDateValid) {
								if (resPackServDetails.getListObject() != null && !resPackServDetails.getListObject().isEmpty()) {
									ListIterator<PackageConsumptionServiceDetailDto> resSrvItr = resPackServDetails
											.getListObject().listIterator();
									PackageConsumptionServiceDetailDto srv = null;
									while (resSrvItr.hasNext()) {
										srv = resSrvItr.next();
										if (srv.getServiceId() != null && srv.getServiceId().equals(consumptionDto.getServiceId())
												&& (srv.getBalanceQuantity() != null && srv.getBalanceQuantity() > 0)
												&& consumptionDto.getSelfPayable().doubleValue() > 0
												&& (srv.getConsultationSpecialityId() == null
														|| ((consumptionDto.getIsConsultationService() != null
																&& consumptionDto.getIsConsultationService() == 'Y')
																&& (consumptionDto.getDocSplId() != null
																		&& consumptionDto.getDocSplId().equals(
																				srv.getConsultationSpecialityId()))))) {

											Integer availableQty = srv.getBalanceQuantity();
										/**	if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null
													&& !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
											/*	OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
												oDPmpprDto.setPackageId(resDto.getPackageId());
												oDPmpprDto.setIsPackageService('Y');
												oDPmpprDto.setIsCapService('N');
												oDPmpprDto.setPackageName(resDto.getPackageName());
												oDPmpprDto.setPackageCode(resDto.getPackageCode());
												oDPmpprDto.setPacakgeConsumptionServiceDetailId(
														srv.getPackageConsumptionServiceDetailId());
												oDPmpprDto.setPackageConsumptionMasterId(
														srv.getPackageConsumptionMasterId());
												if (consumptionDto.getConsumeService() == 0) {
													if (availableQty >= serviceQty) {
														oDPmpprDto.setPackageQty(serviceQty);
														availableQty = availableQty - serviceQty;
														consumptionDto.setServiceRate(srv.getAvailableCap());
														consumptionDto.setOrdTotalAmount(consumptionDto.getServiceRate()
																* serviceQty.doubleValue());
														consumptionDto.setPatientClassPercentage(0.0);
														consumptionDto.setOrdDiscountPer(0.0);
														consumptionDto.setNetAmount(consumptionDto.getFinalPrice());
														consumptionDto.setTaxId(null);
														consumptionDto.setTaxAmt(0.0);
<<<<<<< HEAD
														consumptionDto.setNetAmount(consumptionDto.getOrdTotalAmount());
														consumptionDto.setTaxId(null);
														consumptionDto.setSelfPayable(0.0);
														consumptionDto.setPackageConsumptionAmt(BigDecimal.valueOf(srv.getAvailableCap()));
														oDPmpprDto.setPackageQty(serviceQty);
														oDPmpprDto.setPackageAmount(BigDecimal.valueOf(consumptionDto.getOrdTotalAmount()));
														oDPmpprDto.setServiceAmount(BigDecimal.valueOf(consumptionDto.getServiceRate()));
														iTPackageConsumptionCapDetailsDao.updatePackageQty(srv.getPackageConsumptionServiceDetailId(),
=======
														consumptionDto.setPackageConsumptionAmt(
																BigDecimal.valueOf(srv.getAvailableCap()));

														oDPmpprDto.setPackageQty(serviceQty);
														oDPmpprDto.setPackageAmount(
																new BigDecimal(consumptionDto.getFinalPrice()));
														oDPmpprDto.setServiceAmount(
																new BigDecimal(consumptionDto.getServiceRate()));
														iTPackageConsumptionCapDetailsDao.updatePackageQty(
																srv.getPackageConsumptionServiceDetailId(),
>>>>>>> 3.02
																(serviceQty) * -1);
														srv.setBalanceQuantity(availableQty);
													} else {
														Double ratePerQty = consumptionDto.getServiceRate()
																.doubleValue();
														serviceQty = serviceQty - availableQty;

														consumptionDto.setPackageConsumptionAmt(
																new BigDecimal(availableQty * srv.getAvailableCap()));
														consumptionDto.setServiceRate(ratePerQty);
														consumptionDto.setOrdTotalAmount(consumptionDto.getServiceRate()
																* serviceQty.doubleValue());
														consumptionDto.setPatientClassPercentage(0.0);
														consumptionDto.setOrdDiscountPer(0.0);
														consumptionDto.setNetAmount(
																consumptionDto.getOrdTotalAmount() - consumptionDto
																		.getPackageConsumptionAmt().doubleValue());
														consumptionDto.setSelfPayable(consumptionDto.getNetAmount());

														oDPmpprDto.setPackageQty(1);
														iTPackageConsumptionCapDetailsDao.updatePackageQty(
																srv.getPackageConsumptionServiceDetailId(), (1 * -1));
														srv.setBalanceQuantity(0);
													}
												}
//												consumptionDto.getOrderDetailsPackageMapperDtosList()
//														.add(billingCommonMethods.getOrderDetailsPackageMapperForMultiEncounter(resDto,srv,availableQty,consumptionDto,serviceQty,false));
//											} else {
//												List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
												/* OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
												oDPmpprDto.setPackageId(resDto.getPackageId());
												oDPmpprDto.setIsPackageService('Y');
												oDPmpprDto.setIsCapService('N');
												oDPmpprDto.setPackageName(resDto.getPackageName());
												oDPmpprDto.setPackageCode(resDto.getPackageCode());
												oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
												oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
												oDPmpprDto.setPackageAmount(BigDecimal.valueOf(consumptionDto.getSelfPayable()));
												oDPmpprDto.setServiceAmount(BigDecimal.valueOf(consumptionDto.getServiceRate()));
												consumptionDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt()
														.add(BigDecimal.valueOf(consumptionDto.getSelfPayable())));
												if (consumptionDto.getConsumeService() == 0) {
													if (availableQty >= serviceQty) {
														oDPmpprDto.setPackageQty(serviceQty);
														availableQty = availableQty - serviceQty;
														consumptionDto.setServiceRate(srv.getAvailableCap());
														consumptionDto.setOrdTotalAmount(consumptionDto.getServiceRate()
																* serviceQty.doubleValue());
														consumptionDto.setPatientClassPercentage(0.0);
														consumptionDto.setOrdDiscountPer(0.0);
														consumptionDto.setNetAmount(consumptionDto.getOrdTotalAmount());
														consumptionDto.setTaxId(null);
														consumptionDto.setTaxAmt(0.0);
														consumptionDto.setSelfPayable(0.0);
														consumptionDto.setPackageConsumptionAmt(BigDecimal.valueOf(srv.getAvailableCap()));
														oDPmpprDto.setPackageQty(serviceQty);
														oDPmpprDto.setPackageAmount(BigDecimal.valueOf(consumptionDto.getOrdTotalAmount()));
														oDPmpprDto.setServiceAmount(BigDecimal.valueOf(consumptionDto.getServiceRate()));
														iTPackageConsumptionCapDetailsDao.updatePackageQty(srv.getPackageConsumptionServiceDetailId(),
																(serviceQty) * -1);
														srv.setBalanceQuantity(availableQty);
													} else {
														Double ratePerQty = consumptionDto.getServiceRate().doubleValue();
														serviceQty = serviceQty - availableQty;
														consumptionDto.setPackageConsumptionAmt(new BigDecimal(availableQty * srv.getAvailableCap()));
														consumptionDto.setServiceRate(ratePerQty);
														consumptionDto.setOrdTotalAmount(consumptionDto.getServiceRate()
																* serviceQty.doubleValue());
														consumptionDto.setPatientClassPercentage(0.0);
														consumptionDto.setOrdDiscountPer(0.0);
														consumptionDto.setNetAmount(consumptionDto.getOrdTotalAmount());
														consumptionDto.setTaxId(null);
														consumptionDto.setNetAmount(
																consumptionDto.getOrdTotalAmount() - consumptionDto
																		.getPackageConsumptionAmt().doubleValue());
														consumptionDto.setSelfPayable(consumptionDto.getNetAmount());

														oDPmpprDto.setPackageQty(1);
														iTPackageConsumptionCapDetailsDao.updatePackageQty(srv.getPackageConsumptionServiceDetailId(), (1 * -1));
														srv.setBalanceQuantity(0);
													}
												}*/
//												mppr.add(billingCommonMethods.getOrderDetailsPackageMapperForMultiEncounter(resDto,srv,availableQty,consumptionDto,serviceQty,true));
//												consumptionDto.setOrderDetailsPackageMapperDtosList(mppr);
//												consumptionDto.setSelfPayable(0.00);
//											}
											consumptionDto = billingCommonMethods.updatePackageForMultiEncounter(resDto,srv,availableQty,consumptionDto,serviceQty);

											return new Response<>(SUCCESS, SUCCESS_CODE,
													SERVICE_AVAILABLE_IN_ACTIVE_PACKAGE, consumptionDtoList,
													consumptionDto);
										}
									}
								}
							}
							break;
						}
					}
				}
				if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null && !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty())
					return new Response<>(SUCCESS, SUCCESS_CODE, null, consumptionDtoList, consumptionDto);
				else {
					return iContractConsumptionService.consumeServiceFromContract(cloneDto);
				}
			} else {
				return iContractConsumptionService.consumeServiceFromContract(consumptionDto);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	@Override
	@Transactional
	public Response getPackageRate(ServiceForPackageReqDto reqDto) throws ServiceException {
		try {
			return iMPackageMasterDao.getPackageRate(reqDto);
		}catch(Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response updatePackageService(SlotMasterDto slotMasterDto) throws ServiceException {
		try {
			return iMPackageMasterDao.updatePackageService(slotMasterDto);
		}catch(Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}


	@Override
	@Transactional
	public Response checkServiceAvailableInPackageContract(ContractConsumptionDto contractConsumptionDto)
			throws ServiceException
	{
		try {

			Response packageRes = this.checkIfServiceFromPackage(contractConsumptionDto);
			ContractConsumptionDto consumptionDto = null;
			if (packageRes.getObject() != null) {

				consumptionDto = (ContractConsumptionDto) packageRes.getObject();
				consumptionDto.setCurrentDate(consumptionDto.getCurrentDate() != null ? consumptionDto.getCurrentDate()
						: contractConsumptionDto.getCurrentDate());
				consumptionDto.setVisitTypeId(contractConsumptionDto.getVisitTypeId());
				consumptionDto.setOrganizationId(contractConsumptionDto.getOrganizationId());
				consumptionDto.setUnitId(contractConsumptionDto.getUnitId());

				if (consumptionDto.getOrderDetailsPackageMapperDtosList() != null
						&& !consumptionDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
					if (consumptionDto.getSelfPayable() != null && consumptionDto.getSelfPayable().doubleValue() > 0) {

						consumptionDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.createOrderDetailsPackageMapper(consumptionDto,consumptionDto.getSelfPayable(), null, null));
					} else {
						consumptionDto.setOrderDetailsPayeeMapperDtosList(billingCommonMethods.createOrderDetailsPayeeMapperDtos(contractConsumptionDto, consumptionDto, null, null));
						consumptionDto.setNetAmount(0.00);
						consumptionDto.setFinalPrice(0.00);
                        consumptionDto.setCreaditPayable(0.00);
                        consumptionDto.setTaxAmt(0.00);
                        consumptionDto.setTaxPercentage(0.00);
					}
					if (consumptionDto.getSelfPayable() != null && consumptionDto.getSelfPayable() > 0.00)
						return iContractConsumptionService.consumeServiceFromContractPackage(consumptionDto,
								packageRes.getListObject());
					else
						return new Response<>(SUCCESS, null, null, null, consumptionDto);
				} else {

					if (consumptionDto.getSelfPayable() != null && consumptionDto.getSelfPayable().doubleValue() >= 0) {
						List<OrderDetailsPackageMapperDto> mpprList = new ArrayList<>();
					/**	OrderDetailsPackageMapperDto detailsPackageMapper = new OrderDetailsPackageMapperDto();
						detailsPackageMapper.setIsPackageService('N');
						detailsPackageMapper.setIsCapService('N');
						detailsPackageMapper.setPackageAmount(BigDecimal.ZERO);
						detailsPackageMapper.setServiceId(consumptionDto.getServiceId());
						detailsPackageMapper.setServiceAmount(BigDecimal.valueOf(consumptionDto.getServiceRate()));
						detailsPackageMapper.setStatus('A');*/
						mpprList.add(billingCommonMethods.createOrderDetailsPackageMapper(consumptionDto,consumptionDto.getServiceRate(), null, null));
						consumptionDto.setOrderDetailsPackageMapperDtosList(mpprList);
					}
					// return
					// iContractConsumptionService.consumeServiceFromContractPackage(consumptionDto,packageRes.getListObject());
					return new Response<>(SUCCESS, null, null, null, consumptionDto);
				}
			}
		} catch (Exception e) {

			throw e;
		}
		return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	@Transactional
	public Response updatePackage(ContractConsumptionDto consumptionDto) throws ServiceException {
		try {
			Integer patientId = consumptionDto.getPatientId();
			Integer encounterId = (consumptionDto.getEncounterId()!=null && consumptionDto.getEncounterId()>0)?consumptionDto.getEncounterId():consumptionDto.getAdmissionId();
			Integer visitTypeId = consumptionDto.getVisitTypeId();
//			Integer organisationId = consumptionDto.getOrganizationId();
//			Integer unitId = consumptionDto.getUnitId();
			Integer billingBedCategoryId =consumptionDto.getBillingBedCategoryId();
			Response incExcRes = null;


			Response packageResponse = iTPackageConsumptionMasterDao.getExistingActivePackageByPatientEncounter(patientId, encounterId, visitTypeId, consumptionDto.getOrganizationId(), consumptionDto.getUnitId(),false,billingBedCategoryId);
			if (packageResponse.getCode().equals(SUCCESS_CODE) && packageResponse.getListObject()!=null && !packageResponse.getListObject().isEmpty()) {
				//list of active packages
				List<PackageConsumptionMasterResDto> listResDto = packageResponse.getListObject();

				Date minEffectiveDate = listResDto.stream().map(pobj -> GlobalCommonDateUtils.getDate(pobj.getPackageApplicableDate(),"dd-MM-yyyy")).min(Date::compareTo).get();

				//HIS-16443 --> Kaustubh
				//initializing lists
				//1. doneWithConsumption : list with orderes which are COMPLETELY consumed from packages, initally null
				List<OrderDetailsMasterDto> doneWithConsumption = new ArrayList<OrderDetailsMasterDto>();
				//2. needConsumption : this is the list which needs to go through package consumption logic.
				List<OrderDetailsMasterDto> needConsumption = consumptionDto.getListOrderDetailsMasterDto().stream().filter(obj-> !obj.isPackage()
						&& minEffectiveDate.getTime()<=GlobalCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDateFromLongDate(obj.getOrderDate(), "dd-MM-yyyy"), "dd-MM-yyyy").getTime()).collect(Collectors.toList());
				//3. mergedList : at the end all the lists will get merge initializing this list with the package orders which are not requred for the consumption logic
				List<OrderDetailsMasterDto> mergedList = consumptionDto.getListOrderDetailsMasterDto().stream().filter(obj-> obj.isPackage()
						|| minEffectiveDate.getTime()>GlobalCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDateFromLongDate(obj.getOrderDate(), "dd-MM-yyyy"), "dd-MM-yyyy").getTime()).collect(Collectors.toList());

				//for package iteration
				for (PackageConsumptionMasterResDto resDto : listResDto) {
					/*Response pkgBbcRes = iTPackageConsumptionMasterDao.getBillingBedCategoryIdsByPackageConsumptionId(resDto.getPackageConsumptionMasterId());
					List<Integer> bbcList = pkgBbcRes.getListObject();*/
					//for service iteration


					//4. removing objects (orders which are done with complete consumtion) from the list which is getting used for iteration for consumption
					needConsumption.removeAll(doneWithConsumption);

					Long packEffectiveDate = GlobalCommonDateUtils.getDate(resDto.getPackageApplicableDate(), "dd-MM-yyyy").getTime();

					for (OrderDetailsMasterDto ordDtlsDto : needConsumption)
					{
						Date validityTo1 = resDto.getValidityTo()!=null ? GlobalCommonDateUtils.getDate(resDto.getValidityTo(), "dd-M-yyyy HH:mm:ss") : GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(consumptionDto.getUnitId()), "dd-M-yyyy HH:mm:ss");
						String orderDateStr=GlobalCommonDateUtils.getStringDate(ordDtlsDto.getOrderDate(), "dd-M-yyyy HH:mm:ss");
						Date orderDate=GlobalCommonDateUtils.getDate(orderDateStr, "dd-M-yyyy HH:mm:ss");
						Long orderDateLong = GlobalCommonDateUtils.getDate(orderDateStr, "dd-MM-yyyy").getTime();

						if(ordDtlsDto.getSelfPayable()!=null
								&& ordDtlsDto.getSelfPayable().doubleValue()>0
									&& (ordDtlsDto.getPackageTypeId()==null ||ordDtlsDto.getPackageTypeId()!=1)
										&& !ordDtlsDto.isPackage() && ordDtlsDto.getStatus()=='A' && orderDate.compareTo(validityTo1) <= 0
											&& ordDtlsDto.getForceAddRemoved().equals(0) && resDto.isBillingBedExist()
												&& packEffectiveDate<=orderDateLong)
						{
							Integer serviceId = ordDtlsDto.getServiceId();

							Integer serviceQty = 1;

							if(ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null)
							{
								for(OrderDetailsPackageMapperDto dto : ordDtlsDto.getOrderDetailsPackageMapperDtosList())
								{
									serviceQty = ordDtlsDto.getOrderQty() - (dto.getPackageQty()!=null ? dto.getPackageQty():0);
								}
							}else{
								serviceQty = ordDtlsDto.getOrderQty();
							}

						Integer specialityId = ordDtlsDto.getDepartmentId();
						Integer subSpecialityId = ordDtlsDto.getSubDepartmentId();

						Response resPackServDetails = null;
						switch (resDto.getPackageTypeId())
						{
						/*case 1: // EHC package

							break;*/
						case 2: // Either or Package
							// get service details
							resPackServDetails = iTPackageConsumptionMasterDao.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageConsumptionMasterId());

							ListIterator<PackageConsumptionServiceDetailDto> servicesItr = resPackServDetails.getListObject().listIterator();
							while (servicesItr.hasNext())
							{
								PackageConsumptionServiceDetailDto serv = servicesItr.next();

								Double ratePerQty = ordDtlsDto.getServiceAmt().doubleValue();

								if (serv.getPackageEODetailId() != null && serv.getServiceId()!=null && serv.getServiceId() .equals(ordDtlsDto.getServiceId()))
								{
									// get cap details
									Response eoRes = iTPackageConsumptionEoDetailsDao.getTPackageConsumptionEoDetailsByGrpIdConsumptionId(serv.getPackageEODetailId(), resDto.getPackageConsumptionMasterId());

									if (eoRes.getCode().equals(SUCCESS_CODE) && eoRes.getListObject() != null && !eoRes.getListObject().isEmpty())
									{
										ListIterator<PackageConsumptionEODetailDto> eoItr = eoRes.getListObject().listIterator();
										while (eoItr.hasNext())
										{
											PackageConsumptionEODetailDto eoDetails = eoItr.next();
											Integer availableQty = eoDetails.getBalanceQty();

											if (availableQty > 0 && eoDetails.getPackageEOGroupId().equals(serv.getPackageEODetailId()) && ordDtlsDto.getSelfPayable().doubleValue()>0)
											{
												if(ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty())
												{
													OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
														oDPmpprDto.setPackageId(resDto.getPackageId());
														oDPmpprDto.setIsPackageService('Y');
														oDPmpprDto.setIsCapService('N');
														oDPmpprDto.setPackageName(resDto.getPackageName());
														oDPmpprDto.setPackageCode(resDto.getPackageCode());
														oDPmpprDto.setPackageConsumptionEODetailId(eoDetails.getPackageConsumptionEODetailId());
														oDPmpprDto.setPackageConsumptionMasterId(eoDetails.getPackageConsumptionMasterId());
														//oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
														oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());
														//ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
														if(availableQty>=serviceQty)
														{
															oDPmpprDto.setPackageAmount(BigDecimal.valueOf(ratePerQty*serviceQty));
															oDPmpprDto.setPackageQty(serviceQty);
															iTPackageConsumptionCapDetailsDao.updateEitherOrQuantity(eoDetails.getPackageConsumptionEODetailId(), (serviceQty)*-1,false);
															eoDetails.setBalanceQty(availableQty-serviceQty);
															ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
														}else{

															serviceQty =serviceQty-availableQty;
															oDPmpprDto.setPackageAmount(BigDecimal.valueOf(ratePerQty*availableQty));
															oDPmpprDto.setPackageQty(availableQty);
															iTPackageConsumptionCapDetailsDao.updateEitherOrQuantity(eoDetails.getPackageConsumptionEODetailId(), (availableQty*-1),false);
															eoDetails.setBalanceQty(0);
															ordDtlsDto.setSelfPayable(BigDecimal.valueOf(ratePerQty*serviceQty));
														}
														ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(oDPmpprDto.getPackageAmount()));
														ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(oDPmpprDto);

												}else{

													List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
													OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
														oDPmpprDto.setPackageId(resDto.getPackageId());
														oDPmpprDto.setIsPackageService('Y');
														oDPmpprDto.setIsCapService('N');
														oDPmpprDto.setPackageName(resDto.getPackageName());
														oDPmpprDto.setPackageCode(resDto.getPackageCode());
														oDPmpprDto.setPackageConsumptionEODetailId(eoDetails.getPackageConsumptionEODetailId());
														oDPmpprDto.setPackageConsumptionMasterId(eoDetails.getPackageConsumptionMasterId());
														//oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
														oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());
														//ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
														if(availableQty>=serviceQty)
														{
															oDPmpprDto.setPackageAmount(BigDecimal.valueOf(ratePerQty*serviceQty));
															oDPmpprDto.setPackageQty(serviceQty);
															iTPackageConsumptionCapDetailsDao.updateEitherOrQuantity(eoDetails.getPackageConsumptionEODetailId(), (serviceQty)*-1,false);
															eoDetails.setBalanceQty(availableQty-serviceQty);
															ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
														}else{
															serviceQty =serviceQty-availableQty;
															oDPmpprDto.setPackageAmount(BigDecimal.valueOf(ratePerQty*availableQty));
															oDPmpprDto.setPackageQty(availableQty);
															iTPackageConsumptionCapDetailsDao.updateEitherOrQuantity(eoDetails.getPackageConsumptionEODetailId(), (availableQty*-1),false);
															eoDetails.setBalanceQty(0);
															ordDtlsDto.setSelfPayable(BigDecimal.valueOf(ratePerQty*serviceQty));
														}
													mppr.add(oDPmpprDto);
													ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(oDPmpprDto.getPackageAmount()));
													ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);

												}
											}
										}
									}
								}
								if (serv.getPackageEODetailId() == null && serv.getServiceId()!=null  && serv.getServiceId().equals(serviceId))
								{
									// check if available in services
									if (serv.getBalanceQuantity()!=null && serv.getBalanceQuantity() > 0 && ordDtlsDto.getSelfPayable().doubleValue()>0)
									{
										Integer availableQty = serv.getBalanceQuantity();
										if(ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null && ordDtlsDto.getOrderDetailsPackageMapperDtosList().size()>0)
										{
											OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
											oDPmpprDto.setPackageId(resDto.getPackageId());
											oDPmpprDto.setIsPackageService('Y');
											oDPmpprDto.setIsCapService('N');
											oDPmpprDto.setPackageName(resDto.getPackageName());
											oDPmpprDto.setPackageCode(resDto.getPackageCode());
											oDPmpprDto.setPacakgeConsumptionServiceDetailId(serv.getPackageConsumptionServiceDetailId());
											oDPmpprDto.setPackageConsumptionMasterId(serv.getPackageConsumptionMasterId());
											//oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
											oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());
											//ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
											if(availableQty>=serviceQty)
											{
												oDPmpprDto.setPackageAmount(BigDecimal.valueOf(ratePerQty*serviceQty));
												oDPmpprDto.setPackageQty(serviceQty);
												availableQty = availableQty-serviceQty;
												iTPackageConsumptionCapDetailsDao.updatePackageQty(serv.getPackageConsumptionServiceDetailId(), (serviceQty)*-1);
												serv.setBalanceQuantity(availableQty);
												ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
											}
											else {
												serviceQty =serviceQty-availableQty;
												oDPmpprDto.setPackageAmount(new BigDecimal(ratePerQty*availableQty));
												oDPmpprDto.setPackageQty(availableQty);
												iTPackageConsumptionCapDetailsDao.updatePackageQty(serv.getPackageConsumptionServiceDetailId(), (availableQty*-1));
												serv.setBalanceQuantity(0);
												ordDtlsDto.setSelfPayable(new BigDecimal(ratePerQty*serviceQty));
											  }

											ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(oDPmpprDto.getPackageAmount()));
											ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(oDPmpprDto);

										}
										else {

											List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
											OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
											oDPmpprDto.setPackageId(resDto.getPackageId());
											oDPmpprDto.setIsPackageService('Y');
											oDPmpprDto.setIsCapService('N');
											oDPmpprDto.setPackageName(resDto.getPackageName());
											oDPmpprDto.setPackageCode(resDto.getPackageCode());
											oDPmpprDto.setPacakgeConsumptionServiceDetailId(serv.getPackageConsumptionServiceDetailId());
											oDPmpprDto.setPackageConsumptionMasterId(serv.getPackageConsumptionMasterId());
											//oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
											oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());
											//ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
											if(availableQty>=serviceQty)
											{
												oDPmpprDto.setPackageQty(serviceQty);
												oDPmpprDto.setPackageAmount(new BigDecimal(ratePerQty*serviceQty));
												availableQty = availableQty-serviceQty;
												iTPackageConsumptionCapDetailsDao.updatePackageQty(serv.getPackageConsumptionServiceDetailId(), (serviceQty)*-1);
												serv.setBalanceQuantity(availableQty);
												ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
											}
											else {
												serviceQty =serviceQty-availableQty;
												oDPmpprDto.setPackageAmount(new BigDecimal(ratePerQty*availableQty));
												oDPmpprDto.setPackageQty(availableQty);
												iTPackageConsumptionCapDetailsDao.updatePackageQty(serv.getPackageConsumptionServiceDetailId(), (availableQty*-1));
												serv.setBalanceQuantity(0);
												ordDtlsDto.setSelfPayable(new BigDecimal(ratePerQty*serviceQty));
											  }
											ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(oDPmpprDto.getPackageAmount()));
											mppr.add(oDPmpprDto);
											ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);

										}
									}
								}
							}
							break;
						case 3:
							// package with cap
							if(resDto.isBillingBedExist()) {
							resPackServDetails = iTPackageConsumptionMasterDao.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageConsumptionMasterId());
							resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());
							// check service
							if (resPackServDetails.getListObject() != null && !resPackServDetails.getListObject().isEmpty()) {
								ListIterator<PackageConsumptionServiceDetailDto> resSrvItr = resPackServDetails.getListObject().listIterator();
								PackageConsumptionServiceDetailDto srv = null;
								while (resSrvItr.hasNext()) {
									srv = resSrvItr.next();
									if (srv.getServiceId()!=null  && srv.getServiceId().equals(ordDtlsDto.getServiceId())
											&& srv.getAvailableCap()>0  && ordDtlsDto.getSelfPayable().doubleValue()>0) {
										if(ordDtlsDto.getSelfPayable().doubleValue()<= srv.getAvailableCap()) {
											double capAmount = srv.getAvailableCap()-ordDtlsDto.getSelfPayable().doubleValue();
											srv.setAvailableCap(capAmount);
											if(ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
												/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
												oDPmpprDto.setPackageId(resDto.getPackageId());
												oDPmpprDto.setIsPackageService('Y');
												oDPmpprDto.setIsCapService('Y');
												oDPmpprDto.setPackageName(resDto.getPackageName());
												oDPmpprDto.setPackageCode(resDto.getPackageCode());
												oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
												oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
												oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
												oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/

												ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,srv,ordDtlsDto.getSelfPayable(),ordDtlsDto.getServiceAmt(),'Y',true));
												ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
												//iTPackageConsumptionCapDetailsDao.updatePackageServiceCap(srv.getPackageConsumptionServiceDetailId(), ordDtlsDto.getSelfPayable().doubleValue()*-1);
												ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
											}
											else
											{
//												double capAmount = srv.getAvailableCap()-ordDtlsDto.getSelfPayable().doubleValue();
//												srv.setAvailableCap(capAmount);
												List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
												/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
												oDPmpprDto.setPackageId(resDto.getPackageId());
												oDPmpprDto.setIsPackageService('Y');
												oDPmpprDto.setIsCapService('Y');
												oDPmpprDto.setPackageName(resDto.getPackageName());
												oDPmpprDto.setPackageCode(resDto.getPackageCode());
												oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
												oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
												oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
												oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
												ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
												mppr.add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,srv,ordDtlsDto.getSelfPayable(),ordDtlsDto.getServiceAmt(),'Y',true));
												ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
//												iTPackageConsumptionCapDetailsDao.updatePackageServiceCap(srv.getPackageConsumptionServiceDetailId(), ordDtlsDto.getSelfPayable().doubleValue()*-1);
												ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
											}
										}
										else
										{
											ordDtlsDto.setSelfPayable(ordDtlsDto.getSelfPayable().subtract(BigDecimal.valueOf(srv.getAvailableCap())));
											double capAmount = srv.getAvailableCap();
											srv.setAvailableCap(0.00);
											if(ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty())
											{
												/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
												oDPmpprDto.setPackageId(resDto.getPackageId());
												oDPmpprDto.setIsPackageService('Y');
												oDPmpprDto.setIsCapService('Y');
												oDPmpprDto.setPackageName(resDto.getPackageName());
												oDPmpprDto.setPackageCode(resDto.getPackageCode());
												oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
												oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
												oDPmpprDto.setPackageAmount(BigDecimal.valueOf(capAmount));
												oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
												ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(BigDecimal.valueOf(capAmount)));
												ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,srv,BigDecimal.valueOf(capAmount),ordDtlsDto.getServiceAmt(),'Y',true));
//												iTPackageConsumptionCapDetailsDao.updatePackageServiceCap(srv.getPackageConsumptionServiceDetailId(), capAmount*-1);
											}
											else
											{
												List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
												/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
												oDPmpprDto.setPackageId(resDto.getPackageId());
												oDPmpprDto.setIsPackageService('Y');
												oDPmpprDto.setIsCapService('Y');
												oDPmpprDto.setPackageName(resDto.getPackageName());
												oDPmpprDto.setPackageCode(resDto.getPackageCode());
												oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
												oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
												oDPmpprDto.setPackageAmount(BigDecimal.valueOf(capAmount));
												oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
												ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(BigDecimal.valueOf(capAmount)));
												mppr.add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,srv,BigDecimal.valueOf(capAmount),ordDtlsDto.getServiceAmt(),'Y',true));
												ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
//												iTPackageConsumptionCapDetailsDao.updatePackageServiceCap(srv.getPackageConsumptionServiceDetailId(), capAmount*-1);
											}
										}
									}
								}
								//for bedCategory
								//for change bed category room Rent
								if(visitTypeId!=1)
								{
									Response bedCategroyRes = iTPackageBedCategoryDetailDao.getTPackageBedCategoryDetailBilling(resDto.getPackageConsumptionMasterId());
									if(bedCategroyRes.getListObject()!=null && !bedCategroyRes.getListObject().isEmpty() && ordDtlsDto.getSelfPayable().doubleValue()>0)
									{
										Response bedTrasnferRes = iTPackageBedCategoryDetailDao.getPatientApplicableDaysByAdmissionId(consumptionDto.getAdmissionId());
										if(bedTrasnferRes.getListObject()!=null && !bedTrasnferRes.getListObject().isEmpty())
										{
											List<TPackageBedCategoryDetailDto> betTrasList  =bedTrasnferRes.getListObject();
											List<TPackageBedCategoryDetailDto> bedCategoryRes = bedCategroyRes.getListObject();
											List<PackageConsumptionServiceDetailDto> srvItr = resPackServDetails.getListObject();
											for (TPackageBedCategoryDetailDto transferReq : betTrasList) {
												for (TPackageBedCategoryDetailDto bdCtrDto : bedCategoryRes) {
												   	if(transferReq.getBedCategoryId().equals(bdCtrDto.getBedCategoryId()) && transferReq.getApplicableDays()<=bdCtrDto.getApplicableDays()) {
                                                        for (PackageConsumptionServiceDetailDto pcServDto : srvItr) {
                                                            if (pcServDto.getServiceBedCategoryId() != null && pcServDto.getServiceBedCategoryId() > 0) {
                                                                PackageConsumptionServiceDetailDto service = srvItr.stream()
                                                                        .filter(p -> p.getServiceId() != null)
                                                                        .filter(p -> (p.getServiceBedCategoryId() != null && p.getServiceBedCategoryId().equals(pcServDto.getServiceBedCategoryId()) && ordDtlsDto.getServiceId().equals(p.getServiceId())))
                                                                        .findFirst().orElse(null);

                                                                if (service != null) {
                                                                    if (ordDtlsDto.getSelfPayable().doubleValue() <= service.getAvailableCap()) {
																		double capAmount;
																		capAmount = srv.getAvailableCap() - consumptionDto.getSelfPayable();
																		srv.setAvailableCap(capAmount);
																		if (ordDtlsDto.getOrderDetailsPackageMapperDtosList() != null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
                                                                         /**   OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
                                                                            oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                            oDPmpprDto.setIsPackageService('Y');
                                                                            oDPmpprDto.setIsCapService('Y');
                                                                            oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                            oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                            oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                            oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                            oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
                                                                            oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
                                                                            ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
                                                                            ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,srv,ordDtlsDto.getSelfPayable(),ordDtlsDto.getServiceAmt(),'Y',true));
                                                                        } else {
//                                                                            capAmount = srv.getAvailableCap() - ordDtlsDto.getSelfPayable().doubleValue();
//                                                                            srv.setAvailableCap(capAmount);
                                                                            List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
                                                                            /**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
                                                                            oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                            oDPmpprDto.setIsPackageService('Y');
                                                                            oDPmpprDto.setIsCapService('Y');
                                                                            oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                            oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                            oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                            oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                            oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
                                                                            oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
                                                                            ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
                                                                            mppr.add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,srv,ordDtlsDto.getSelfPayable(),ordDtlsDto.getServiceAmt(),'Y',true));
                                                                            ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
                                                                        }
//                                                                        iTPackageConsumptionCapDetailsDao.updatePackageServiceCap(srv.getPackageConsumptionServiceDetailId(), ordDtlsDto.getSelfPayable().doubleValue() * -1);
                                                                        ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
                                                                    } else {
                                                                        ordDtlsDto.setSelfPayable(ordDtlsDto.getSelfPayable().subtract(BigDecimal.valueOf(srv.getAvailableCap())));
                                                                        double capAmount = srv.getAvailableCap();
                                                                        if (ordDtlsDto.getOrderDetailsPackageMapperDtosList() != null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
                                                                           /** OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
                                                                            oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                            oDPmpprDto.setIsPackageService('Y');
                                                                            oDPmpprDto.setIsCapService('Y');
                                                                            oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                            oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                            oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                            oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                            oDPmpprDto.setPackageAmount(BigDecimal.valueOf(capAmount));
                                                                            oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
                                                                            ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(BigDecimal.valueOf(capAmount)));
                                                                            ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,srv,BigDecimal.valueOf(capAmount),ordDtlsDto.getServiceAmt(),'Y',true));

                                                                        } else {
                                                                            List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
                                                                          /**  OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
                                                                            oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                            oDPmpprDto.setIsPackageService('Y');
                                                                            oDPmpprDto.setIsCapService('Y');
                                                                            oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                            oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                            oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                            oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                            oDPmpprDto.setPackageAmount(BigDecimal.valueOf(capAmount));
                                                                            oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
                                                                            ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(BigDecimal.valueOf(capAmount)));
                                                                            mppr.add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,srv,BigDecimal.valueOf(capAmount),ordDtlsDto.getServiceAmt(),'Y',true));
                                                                            ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
                                                                        }
                                                                        srv.setAvailableCap(0.00);
//                                                                        iTPackageConsumptionCapDetailsDao.updatePackageServiceCap(srv.getPackageConsumptionServiceDetailId(), capAmount * -1);
                                                                    }
                                                                }
                                                            }
                                                        }
												   	}
												}
											}
										}

									}
								}
							}

							// check inclusion exclusion service
							incExcRes = iTPackageIncExcDetailsDao
				                   	.getTPackageIncExcDetailsForBillingByPackageConsumptionId(resDto.getPackageConsumptionMasterId());
							// get cap details
							Response packageCapRes = iPackageConsumptionDao
									.getPackageConsumptionCapDetails(resDto.getPackageConsumptionMasterId());
							resDto.setListTPackageConsumptionCapDetailsDto(packageCapRes.getListObject());
							if (packageCapRes.getListObject() != null && !packageCapRes.getListObject().isEmpty())
							{
								ListIterator<TPackageConsumptionCapDetailsDto> capDtlsItr = packageCapRes.getListObject()
										.listIterator();
								TPackageConsumptionCapDetailsDto capDtls = null;

								while (capDtlsItr.hasNext()) {
									capDtls = capDtlsItr.next();
									// get service details by id
										if (capDtls.getDepartmentId() != null
												&& capDtls.getSubDepartmentId() != null) {
											if (capDtls.getDepartmentId().equals(specialityId)
													&& capDtls.getSubDepartmentId().equals(subSpecialityId) && ordDtlsDto.getSelfPayable().doubleValue()>0) {
												if (incExcRes.getListObject() != null
														&& !incExcRes.getListObject().isEmpty() && ordDtlsDto.getSelfPayable().doubleValue()>0) {
															 List<TPackageIncExcDetailsDto> incexcItr = incExcRes.getListObject();
														     Optional<TPackageIncExcDetailsDto> result = incexcItr
															      .stream().parallel()
															      .filter(service -> service.getServiceId()!=null && service.getServiceId().equals(serviceId) && service.getIsInclusiveService() == 2).findAny();

													   if(result.isPresent())
														   {
															 continue;
														   }
										                }

												if(capDtls.getAvailableCap()>0  && ordDtlsDto.getSelfPayable().doubleValue()>0) {
												if(ordDtlsDto.getSelfPayable().doubleValue() <= capDtls.getAvailableCap().doubleValue()) {
												if(ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty()) {
													double capAmount = capDtls.getAvailableCap()-ordDtlsDto.getSelfPayable().doubleValue();
													capDtls.setAvailableCap(capAmount);
													/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
													oDPmpprDto.setPackageId(resDto.getPackageId());
													oDPmpprDto.setIsPackageService('Y');
													oDPmpprDto.setIsCapService('Y');
													oDPmpprDto.setPackageName(resDto.getPackageName());
													oDPmpprDto.setPackageCode(resDto.getPackageCode());
													oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
													oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
													oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
													oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
													ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
													ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,capDtls,ordDtlsDto.getSelfPayable(),ordDtlsDto.getServiceAmt()));
//													iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(), ordDtlsDto.getSelfPayable().doubleValue()*-1);
													ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
												}
												else
												{
													double capAmount = capDtls.getAvailableCap()-ordDtlsDto.getSelfPayable().doubleValue();
													capDtls.setAvailableCap(capAmount);
													List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
													/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
													oDPmpprDto.setPackageId(resDto.getPackageId());
													oDPmpprDto.setIsPackageService('Y');
													oDPmpprDto.setIsCapService('Y');
													oDPmpprDto.setPackageName(resDto.getPackageName());
													oDPmpprDto.setPackageCode(resDto.getPackageCode());
													oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
													oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
													oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
													oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
													ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
													mppr.add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,capDtls,ordDtlsDto.getSelfPayable(),ordDtlsDto.getServiceAmt()));
													ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
//													iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(), ordDtlsDto.getSelfPayable().doubleValue()*-1);
													ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
												  }
												}
												else
												{
													ordDtlsDto.setSelfPayable(ordDtlsDto.getSelfPayable().subtract(BigDecimal.valueOf(capDtls.getAvailableCap())));
													double capAmount = capDtls.getAvailableCap();
													capDtls.setAvailableCap(0.00);
													if(ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty())
													{

														/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
														oDPmpprDto.setPackageId(resDto.getPackageId());
														oDPmpprDto.setIsPackageService('Y');
														oDPmpprDto.setIsCapService('Y');
														oDPmpprDto.setPackageName(resDto.getPackageName());
														oDPmpprDto.setPackageCode(resDto.getPackageCode());
														oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
														oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
														oDPmpprDto.setPackageAmount(BigDecimal.valueOf(capAmount));
														oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
														ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(BigDecimal.valueOf(capAmount)));
														ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,capDtls,
																BigDecimal.valueOf(capAmount),ordDtlsDto.getServiceAmt()));
//														iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(), capAmount*-1);
													}
													else
													{
														List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
														/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
														oDPmpprDto.setPackageId(resDto.getPackageId());
														oDPmpprDto.setIsPackageService('Y');
														oDPmpprDto.setIsCapService('Y');
														oDPmpprDto.setPackageName(resDto.getPackageName());
														oDPmpprDto.setPackageCode(resDto.getPackageCode());
														oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
														oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
														oDPmpprDto.setPackageAmount(BigDecimal.valueOf(capAmount));
														oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
														ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(BigDecimal.valueOf(capAmount)));
														mppr.add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,capDtls,BigDecimal.valueOf(capAmount),ordDtlsDto.getServiceAmt()));
														ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
//														iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(), capAmount*-1);
													}
												}
											  }
											}
										} else if (capDtls.getDepartmentId() != null
												&& capDtls.getSubDepartmentId() == null)
										{
											if (capDtls.getDepartmentId().equals(specialityId) && ordDtlsDto.getSelfPayable().doubleValue()>0)
											{

												if (incExcRes.getListObject() != null
														&& !incExcRes.getListObject().isEmpty() && ordDtlsDto.getSelfPayable().doubleValue()>0)
															{
															 List<TPackageIncExcDetailsDto> incexcItr = incExcRes.getListObject();
														     Optional<TPackageIncExcDetailsDto> result = incexcItr
															      .stream().parallel()
															      .filter(service -> service.getServiceId()!=null &&  service.getServiceId().equals(serviceId) && service.getIsInclusiveService() == 2).findAny();

													   if(result.isPresent())
														   {
															 continue;
														   }
										                }
												if(capDtls.getAvailableCap()>0 &&  ordDtlsDto.getSelfPayable().doubleValue()>0)
												{
												  if((ordDtlsDto.getSelfPayable().doubleValue() <= capDtls.getAvailableCap().doubleValue()) )
													{
												if(ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty())
												{
													double capAmount = capDtls.getAvailableCap()-ordDtlsDto.getSelfPayable().doubleValue();
													capDtls.setAvailableCap(capAmount);

												/**	OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
													oDPmpprDto.setPackageId(resDto.getPackageId());
													oDPmpprDto.setIsPackageService('Y');
													oDPmpprDto.setIsCapService('Y');
													oDPmpprDto.setPackageName(resDto.getPackageName());
													oDPmpprDto.setPackageCode(resDto.getPackageCode());
													oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
													oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
													oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
													oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
													ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
													ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,capDtls,ordDtlsDto.getSelfPayable(),ordDtlsDto.getServiceAmt()));
//													iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(), ordDtlsDto.getSelfPayable().doubleValue()*-1);
													ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
												}
												else
												{
													double capAmount = capDtls.getAvailableCap()-ordDtlsDto.getSelfPayable().doubleValue();
													capDtls.setAvailableCap(capAmount);
													List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
													/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
													oDPmpprDto.setPackageId(resDto.getPackageId());
													oDPmpprDto.setIsPackageService('Y');
													oDPmpprDto.setIsCapService('Y');
													oDPmpprDto.setPackageName(resDto.getPackageName());
													oDPmpprDto.setPackageCode(resDto.getPackageCode());
													oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
													oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
													oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
													oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
													ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
													mppr.add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,capDtls,ordDtlsDto.getSelfPayable(),ordDtlsDto.getServiceAmt()));
													ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
//													iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(), ordDtlsDto.getSelfPayable().doubleValue()*-1);
													ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
												}
											}
											else
											{
												ordDtlsDto.setSelfPayable(ordDtlsDto.getSelfPayable().subtract(BigDecimal.valueOf(capDtls.getAvailableCap())));
												double capAmount = capDtls.getAvailableCap();
												capDtls.setAvailableCap(0.00);
												if(ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty())
												{

													/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
													oDPmpprDto.setPackageId(resDto.getPackageId());
													oDPmpprDto.setIsPackageService('Y');
													oDPmpprDto.setIsCapService('Y');
													oDPmpprDto.setPackageName(resDto.getPackageName());
													oDPmpprDto.setPackageCode(resDto.getPackageCode());
													oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
													oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
													oDPmpprDto.setPackageAmount(BigDecimal.valueOf(capAmount));
													oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
													ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(BigDecimal.valueOf(capAmount)));
													ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,capDtls,BigDecimal.valueOf(capAmount),ordDtlsDto.getServiceAmt()));
													iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(), capAmount*-1);
												}
												else
												{
													List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
													/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
													oDPmpprDto.setPackageId(resDto.getPackageId());
													oDPmpprDto.setIsPackageService('Y');
													oDPmpprDto.setIsCapService('Y');
													oDPmpprDto.setPackageName(resDto.getPackageName());
													oDPmpprDto.setPackageCode(resDto.getPackageCode());
													oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
													oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
													oDPmpprDto.setPackageAmount(BigDecimal.valueOf(capAmount));
													oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());*/
													ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(BigDecimal.valueOf(capAmount)));
													mppr.add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,capDtls,BigDecimal.valueOf(capAmount),ordDtlsDto.getServiceAmt()));
													ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
//													iTPackageConsumptionCapDetailsDao.updatePackageCap(capDtls.getPackageConsumptionCapDetailsId(), capAmount*-1);
												}
											  }
											 }
										   }
										}
								}
							   }
							 }
							break;
						case 4: // All exclusive package
							resPackServDetails = iTPackageConsumptionMasterDao
									.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageConsumptionMasterId());
							resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());

							// check inclusion exclusion service
							incExcRes = iTPackageIncExcDetailsDao
									.getTPackageIncExcDetailsForBillingByPackageConsumptionId(resDto.getPackageConsumptionMasterId());
							if (incExcRes.getListObject() != null
									&& !incExcRes.getListObject().isEmpty() && ordDtlsDto.getSelfPayable().doubleValue()>0) {
								 List<TPackageIncExcDetailsDto> incexcItr = incExcRes.getListObject();
								 Optional<TPackageIncExcDetailsDto> result =null;

								   result =  incexcItr.stream().parallel()
								            .filter(service -> (service.getDeptId()!=null &&service.getSubDeptId()==null)
													&& service.getDeptId().equals(specialityId)
													&& service.getIsInclusiveService() == 2).findAny();
								   if(result.isPresent())
									   continue;

								   result =  incexcItr.stream().parallel()
										      .filter(service -> (service.getDeptId()!=null &&service.getSubDeptId()!=null)
													  && service.getDeptId().equals(specialityId)
													  && service.getSubDeptId().equals(subSpecialityId)
													  && service.getIsInclusiveService() == 2).findAny();
								   if(result.isPresent())
									   continue;

								   result = incexcItr
									      .stream().parallel()
									      .filter(service -> service.getServiceId()!=null && service.getServiceId().equals(serviceId) && service.getIsInclusiveService() == 2).findAny();
								   if(result.isPresent())
									   continue;

								  if (ordDtlsDto.getSelfPayable().doubleValue()>0 && !result.isPresent())
									{
										if(ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty())
										{
											/**OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
											oDPmpprDto.setPackageId(resDto.getPackageId());
											oDPmpprDto.setIsPackageService('Y');
											oDPmpprDto.setPackageName(resDto.getPackageName());
											oDPmpprDto.setIsCapService('N');
											oDPmpprDto.setPackageCode(resDto.getPackageCode());
											oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
											oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());
											oDPmpprDto.setPackageConsumptionMasterId(resDto.getPackageConsumptionMasterId());*/
											ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
											ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,null,ordDtlsDto.getSelfPayable(),ordDtlsDto.getServiceAmt(),'N',false));
											ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
										}
										else
										{
											List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
										/**	OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
											oDPmpprDto.setPackageId(resDto.getPackageId());
											oDPmpprDto.setIsPackageService('Y');
											oDPmpprDto.setIsCapService('N');
											oDPmpprDto.setPackageName(resDto.getPackageName());
											oDPmpprDto.setPackageCode(resDto.getPackageCode());
											oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
											oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());
											oDPmpprDto.setPackageConsumptionMasterId(resDto.getPackageConsumptionMasterId());*/
											ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
											mppr.add(billingCommonMethods.createOrderDetailsPackageMapper(resDto,null,ordDtlsDto.getSelfPayable(),ordDtlsDto.getServiceAmt(),'N',false));
											ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
											ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
										}
								}
							}
							break;
						case 5: // multi encounter package
								resPackServDetails = iTPackageConsumptionMasterDao
										.getPackageConsumptionServiceDetailsByPackageIdPatientId(resDto.getPackageConsumptionMasterId());
								resDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());

								//Double ratePerQty = ordDtlsDto.getServiceAmt().doubleValue();

								if (resPackServDetails.getListObject() != null
										&& !resPackServDetails.getListObject().isEmpty()) {
									ListIterator<PackageConsumptionServiceDetailDto> resSrvItr = resPackServDetails
											.getListObject().listIterator();
									PackageConsumptionServiceDetailDto srv = null;
									while (resSrvItr.hasNext())
									{
										srv = resSrvItr.next();
										if (srv.getServiceId()!=null && srv.getServiceId() .equals(ordDtlsDto.getServiceId())
												&& (srv.getBalanceQuantity()!=null && srv.getBalanceQuantity() > 0)
													&& ordDtlsDto.getSelfPayable().doubleValue()>0
													 && (srv.getConsultationSpecialityId() == null
													 		|| (ordDtlsDto.getIsConsultationService()!=null && ordDtlsDto.getIsConsultationService()=='Y'
													 				&& ordDtlsDto.getOrdDocSplId().equals(srv.getConsultationSpecialityId()))))
										{

											Integer availableQty = srv.getBalanceQuantity();
											if(ordDtlsDto.getOrderDetailsPackageMapperDtosList()!=null && !ordDtlsDto.getOrderDetailsPackageMapperDtosList().isEmpty())
											{
												OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
												oDPmpprDto.setPackageId(resDto.getPackageId());
												oDPmpprDto.setIsPackageService('Y');
												oDPmpprDto.setIsCapService('N');
												oDPmpprDto.setPackageName(resDto.getPackageName());
												oDPmpprDto.setPackageCode(resDto.getPackageCode());
												oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
												oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
												oDPmpprDto.setPackageQty(1);
												//ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(ordDtlsDto.getSelfPayable()));
												if(availableQty>=serviceQty)
												{
//													Double ratePerQty = Double.valueOf(srv.getAvailableCap());
													Double ratePerQty = srv.getAvailableCap();
													ordDtlsDto.setServiceAmt(BigDecimal.valueOf(ratePerQty));
													ordDtlsDto.setOrdTotalAmt(ordDtlsDto.getServiceAmt().multiply(BigDecimal.valueOf(serviceQty)));
													ordDtlsDto.setOrdConcession(BigDecimal.ZERO);
													ordDtlsDto.setOrdDiscountPer(BigDecimal.ZERO);
													ordDtlsDto.setTaxId(null);
													ordDtlsDto.setTaxAmount(BigDecimal.ZERO);
													ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
													oDPmpprDto.setPackageQty(serviceQty);
													oDPmpprDto.setPackageAmount(BigDecimal.valueOf(ratePerQty*serviceQty));
													oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());
													availableQty = availableQty-serviceQty;
													iTPackageConsumptionCapDetailsDao.updatePackageQty(srv.getPackageConsumptionServiceDetailId(), (serviceQty)*-1);
													srv.setBalanceQuantity(availableQty);


												}
												else {
													Double ratePerQty = ordDtlsDto.getServiceAmt().doubleValue();
													serviceQty =serviceQty-availableQty;

													ordDtlsDto.setServiceAmt(BigDecimal.valueOf(ratePerQty));
													ordDtlsDto.setOrdTotalAmt(ordDtlsDto.getServiceAmt().multiply(BigDecimal.valueOf(serviceQty)));
													ordDtlsDto.setOrdConcession(BigDecimal.ZERO);
													ordDtlsDto.setOrdDiscountPer(BigDecimal.ZERO);
													ordDtlsDto.setTaxId(null);
													ordDtlsDto.setTaxAmount(BigDecimal.ZERO);
													oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());
													oDPmpprDto.setPackageQty(availableQty);
													oDPmpprDto.setPackageAmount(BigDecimal.valueOf(availableQty*srv.getAvailableCap()));

													iTPackageConsumptionCapDetailsDao.updatePackageQty(srv.getPackageConsumptionServiceDetailId(), (availableQty*-1));
													srv.setBalanceQuantity(0);
													ordDtlsDto.setSelfPayable(ordDtlsDto.getOrdTotalAmt().subtract(oDPmpprDto.getPackageAmount()));
												  }
												ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(oDPmpprDto.getPackageAmount()));
												ordDtlsDto.getOrderDetailsPackageMapperDtosList().add(oDPmpprDto);
												//ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
											} else {

												List<OrderDetailsPackageMapperDto> mppr = new ArrayList<>();
												OrderDetailsPackageMapperDto oDPmpprDto = new OrderDetailsPackageMapperDto();
												oDPmpprDto.setPackageId(resDto.getPackageId());
												oDPmpprDto.setIsPackageService('Y');
												oDPmpprDto.setIsCapService('N');
												oDPmpprDto.setPackageName(resDto.getPackageName());
												oDPmpprDto.setPackageCode(resDto.getPackageCode());
												oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
												oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
												//oDPmpprDto.setPackageAmount(ordDtlsDto.getSelfPayable());
												//oDPmpprDto.setPackageQty(1);
												if(availableQty>=serviceQty)
												{
//													Double ratePerQty = Double.valueOf(srv.getAvailableCap());
													Double ratePerQty = srv.getAvailableCap();

													ordDtlsDto.setServiceAmt(BigDecimal.valueOf(ratePerQty));
													ordDtlsDto.setSelfPayable(BigDecimal.valueOf(ratePerQty));
													ordDtlsDto.setOrdTotalAmt(ordDtlsDto.getServiceAmt().multiply(BigDecimal.valueOf(serviceQty)));
													ordDtlsDto.setOrdConcession(BigDecimal.ZERO);
													ordDtlsDto.setOrdDiscountPer(BigDecimal.ZERO);
													ordDtlsDto.setTaxId(null);
													ordDtlsDto.setTaxAmount(BigDecimal.ZERO);
													ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
													oDPmpprDto.setPackageQty(serviceQty);
													oDPmpprDto.setPackageAmount(BigDecimal.valueOf(ratePerQty*serviceQty));
													oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());
													availableQty = availableQty-serviceQty;
													iTPackageConsumptionCapDetailsDao.updatePackageQty(srv.getPackageConsumptionServiceDetailId(), (serviceQty)*-1);
													srv.setBalanceQuantity(availableQty);


												} else {
													Double ratePerQty = ordDtlsDto.getServiceAmt().doubleValue();
													serviceQty =serviceQty-availableQty;

													ordDtlsDto.setServiceAmt(BigDecimal.valueOf(ratePerQty));
													ordDtlsDto.setOrdTotalAmt(ordDtlsDto.getServiceAmt().multiply(BigDecimal.valueOf(serviceQty)));
													ordDtlsDto.setOrdConcession(BigDecimal.ZERO);
													ordDtlsDto.setOrdDiscountPer(BigDecimal.ZERO);
													ordDtlsDto.setTaxId(null);
													ordDtlsDto.setTaxAmount(BigDecimal.ZERO);
													oDPmpprDto.setPackageQty(availableQty);
													oDPmpprDto.setPackageAmount(BigDecimal.valueOf(availableQty*srv.getAvailableCap()));
													oDPmpprDto.setServiceAmount(ordDtlsDto.getServiceAmt());

													iTPackageConsumptionCapDetailsDao.updatePackageQty(srv.getPackageConsumptionServiceDetailId(), (availableQty*-1));
													srv.setBalanceQuantity(0);
													ordDtlsDto.setSelfPayable(ordDtlsDto.getOrdTotalAmt().subtract(oDPmpprDto.getPackageAmount()));
												  }
												ordDtlsDto.setPackageConsumptionAmt(ordDtlsDto.getPackageConsumptionAmt().add(oDPmpprDto.getPackageAmount()));
												mppr.add(oDPmpprDto);
												ordDtlsDto.setOrderDetailsPackageMapperDtosList(mppr);
												//ordDtlsDto.setSelfPayable(BigDecimal.ZERO);
											}
										}
									}
								}
							//}
							break;
						}
					}
					//5. adding object to doneWithConsumption list (orders which are done with consumption
					if(ordDtlsDto.getServiceAmt().doubleValue()==ordDtlsDto.getPackageConsumptionAmt().doubleValue())
					{
						doneWithConsumption.add(ordDtlsDto);
					}
				}
			}
			//6. after iterations merging all the lists.
			if(packageResponse.getListObject() != null && packageResponse.getListObject().size()==1)
			{
				needConsumption.removeAll(doneWithConsumption);
			}
			mergedList.addAll(doneWithConsumption);
			mergedList.addAll(needConsumption);

			Optional<OrderDetailsMasterDto> result = mergedList.stream()
			        .filter(od -> od.getOrderDetailsId()==null)
			        .findFirst();

			if(!result.isPresent())
			{
				List<OrderDetailsMasterDto> distinctOrderDetailsList = mergedList.stream()
	                    .collect(Collectors.collectingAndThen(
	                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(OrderDetailsMasterDto::getOrderDetailsId))),
	                            ArrayList::new));

				consumptionDto.setListOrderDetailsMasterDto(distinctOrderDetailsList);
			}else{

				List<OrderDetailsMasterDto> notNullOrderDetailsList = mergedList.stream().filter(od -> od.getOrderDetailsId()!=null).collect(Collectors.toList());

				notNullOrderDetailsList = notNullOrderDetailsList.stream()
	                    .collect(Collectors.collectingAndThen(
	                            Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(OrderDetailsMasterDto::getOrderDetailsId))),
	                            ArrayList::new));

				List<OrderDetailsMasterDto> nullOrderDetailsList = mergedList.stream().filter(od -> od.getOrderDetailsId()==null).collect(Collectors.toList());

				mergedList = new ArrayList<OrderDetailsMasterDto>();
				mergedList.addAll(notNullOrderDetailsList);
				mergedList.addAll(nullOrderDetailsList);

				consumptionDto.setListOrderDetailsMasterDto(mergedList);
			}

			}
			return new Response<>(SUCCESS, SUCCESS_CODE,null,null, consumptionDto);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}
	public double getNetAmountByQuantity(CommonBillingItemsDto dto,boolean taxInclusive)
	{
		double rate = dto.getRate();
		Double netAmount = 0.00;
		double totalAmount = 0.00;
		double unitDiscount = dto.getUnitDiscountAmount()==null?0:dto.getUnitDiscountAmount();
		double unitConcession = dto.getUnitConsessionAmount();
		if(dto.getTaxPercentage()!=null && dto.getTaxPercentage()>0 && !taxInclusive)
		{
		    totalAmount = (rate * 1)-Math.abs(unitConcession-unitDiscount);
			netAmount = totalAmount +(totalAmount*(dto.getTaxPercentage()*0.01));
			dto.setUnitNetAmount(netAmount);
		}
		else {
			 totalAmount = (rate * 1)-Math.abs(unitConcession-unitDiscount);
			 netAmount = totalAmount;
		     dto.setUnitNetAmount(netAmount);
		}
		return GlobalCommonDateUtils.getAmount(netAmount);
	}
	public CommonBillingItemsDto getTotalNetAmountByQuantity(CommonBillingItemsDto dto,boolean taxInclusive)
	{
		//double rate = dto.getRate();
		Double netAmount = 0.00;
		double totalAmount = 0.00;
		double discount = dto.getDiscountAmount();
		double concession = dto.getConsessionAmount();
	/**	if(dto.getTaxPercentage()!=null && dto.getTaxPercentage()>0 && !taxInclusive)
		{
		    totalAmount = (rate * dto.getQuantity())-(concession+discount);
			netAmount = GlobalCommonDateUtils.getAmount(totalAmount +(totalAmount*(dto.getTaxPercentage()*0.01)));
			dto.setNetAmount(netAmount);
			dto.setSelfPayable(netAmount);
			dto.setUnitNetAmount(GlobalCommonDateUtils.getAmount(netAmount/dto.getQuantity()));
		}
		else {
			 totalAmount = (rate * dto.getQuantity())-(concession+discount);
			 netAmount = GlobalCommonDateUtils.getAmount(totalAmount);
			 dto.setNetAmount(netAmount);
		     dto.setSelfPayable(netAmount);
		     dto.setUnitNetAmount(GlobalCommonDateUtils.getAmount(netAmount/dto.getQuantity()));
		}*/
		totalAmount = (dto.getRate() * dto.getQuantity()) - (dto.getConsessionAmount() + dto.getDiscountAmount());
//		netAmount = GlobalCommonDateUtils.getAmount(totalAmount + (totalAmount * ((dto.getTaxPercentage() != null && !taxInclusive) ? dto.getTaxPercentage() : 0) * 0.01));
		netAmount = (dto.getTaxPercentage()!=null && dto.getTaxPercentage()>0 && !taxInclusive) ?  GlobalCommonDateUtils.getAmount(totalAmount +(totalAmount*(dto.getTaxPercentage()*0.01))) : GlobalCommonDateUtils.getAmount(totalAmount);
		dto.setNetAmount(netAmount);
		dto.setSelfPayable(netAmount);
		dto.setUnitNetAmount(GlobalCommonDateUtils.getAmount(netAmount / dto.getQuantity()));
		return dto;
	}



	//Pharmacy
	@Override
	public Response consumeItemFromPackage(PharmacyBillCommonDto dto) throws Exception {
		try {
			Integer patientId = dto.getPatientId();
			Integer visitTypeId = dto.getVisitTypeId();
			Integer encounterId = (dto.getEncounterId() == null || dto.getEncounterId()==0) && (dto.getAdmissionId() != null && dto.getAdmissionId()>0) ?dto.getAdmissionId():dto.getEncounterId();
			Integer organisationId = dto.getOrganizationId();
			Integer unitId = dto.getUnitId();
			Integer billingBedCategoryId = dto.getBillingBedCategoryId();
			boolean isPharamcy = visitTypeId!=null && visitTypeId==1 && sessionService.getIsPreOrPostBilling()=='Y' &&  sessionService.getCookieObjectAppConfig().getIsConsolidatedBill()=='N';
//			boolean isForceAddRemoveServiceExsist = dto.getItemList().stream().filter(obj->dto.getForceAddRemoved().intValue()==1).findAny().isPresent();
			boolean isForceAddRemoveServiceExist = dto.getItemList().stream().anyMatch(obj->dto.getForceAddRemoved().intValue()==1);
			if(billingBedCategoryId==null || billingBedCategoryId==0)
			{
				    if(encounterId!=null && encounterId >0 && visitTypeId==1) {
							billingBedCategoryId = sessionService.getCookieObjectAppConfig().getOpdBillingCatId();
				    }
				    else if(encounterId!=null && encounterId >0 && visitTypeId!=1)
				    {
				    	Response admissionDto = iWalkInSalesDao.getAdmissionsById(encounterId);
				    	AdmissionListIpdChargesResDto adminDto = (AdmissionListIpdChargesResDto) admissionDto.getObject();
				    	billingBedCategoryId = adminDto.getBedBillingCategoryId();
				    }
			}

			if(dto.getPharmacyReturnServiceRecal() == null) {
				dto.setPharmacyReturnServiceRecal(0);
			}
			Date orderDate  = dto.getOrderDate() == null ? GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "yyyy-MM-dd HH:mm:ss")  : GlobalCommonDateUtils.getDate(dto.getOrderDate(), "yyyy-MM-dd HH:mm:ss");
			Long orderDateLong = GlobalCommonDateUtils.getDate(GlobalCommonDateUtils.getStringDate(orderDate, "dd-MM-yyyy"),"dd-MM-yyyy").getTime();
			for (CommonBillingItemsDto item : dto.getItemList())
			{

				//for cap plus

				/*if(!dto.isRecalculation() && dto.getConsume() && dto.getForceAddRemoved()==0) 
				{
					item.setEncounterId(dto.getEncounterId());
					item.setAdmissionId(dto.getAdmissionId());
					item.setPatientId(patientId);
					item.setAddPreAuth(dto.isAddPreAuth());
					iWalkInSalesService.removeContractItems(item);
			    }*/
				item = getTotalNetAmountByQuantity(item, dto.isTaxInclusive());
				item.setSelfQuantity(item.getQuantity());
				item.setCreditPayable(0.00);
				item.setPackageConsumptionAmt(0.00);
				item.setSaleDetailPackageMapperDtoList(null);
				item.setSaleDetailPayeeMapperDtoList(null);
			}

			Response packageResponse = iTPackageConsumptionMasterDao.getExistingActivePackageByPatientEncounter(patientId, encounterId, visitTypeId, organisationId, unitId,isPharamcy,billingBedCategoryId);
			Date minEffectiveDate =null;
			List<PackageConsumptionMasterResDto> listResDto = packageResponse.getListObject();
			/**if(packageResponse.getListObject()!= null && !packageResponse.getListObject().isEmpty())
			{
				minEffectiveDate = listResDto.stream().map(pobj -> GlobalCommonDateUtils.getDate(pobj.getPackageApplicableDate(),"dd-MM-yyyy")).min(Date::compareTo).get();
			}else{
				minEffectiveDate = TimeZoneComponent.getDateByzone(unitId);
			}*/

			minEffectiveDate = (packageResponse.getListObject() != null && !packageResponse.getListObject().isEmpty())
					? listResDto.stream().map(pobj -> GlobalCommonDateUtils.getDate(pobj.getPackageApplicableDate(), "dd-MM-yyyy")).min(Date::compareTo).get()
					: TimeZoneComponent.getDateByzone(unitId);

			if (packageResponse.getCode().equals(SUCCESS_CODE) && packageResponse.getListObject()!= null && !packageResponse.getListObject().isEmpty()
					&& (minEffectiveDate.getTime()<=orderDateLong || isForceAddRemoveServiceExist))
			{
				for (PackageConsumptionMasterResDto mpkMstDto : listResDto) {
					billingBedCategoryId = (billingBedCategoryId!=null && billingBedCategoryId>0)?billingBedCategoryId:mpkMstDto.getBillingBedCategoryId();
					Response resPackServDetails = null;
					Response incExcRes = null;
					Response packageCapRes = null;

					/*Response pkgBbcRes = iTPackageConsumptionMasterDao.getBillingBedCategoryIdsByPackageConsumptionId(mpkMstDto.getPackageConsumptionMasterId());
					mpkMstDto.setBbcIds(pkgBbcRes.getListObject());*/

					//packageData
					 resPackServDetails = iTPackageConsumptionMasterDao.getPackageConsumptionServiceDetailsByPackageIdPatientId(mpkMstDto.getPackageConsumptionMasterId());
					 mpkMstDto.setListPackageConsumptionServiceDetailDto(resPackServDetails.getListObject());

		     	    // ListIterator<PackageConsumptionServiceDetailDto> resSrvItr = resPackServDetails.getListObject().listIterator();

		     	    // List<PackageConsumptionServiceDetailDto> serviceList = resPackServDetails.getListObject();

		     	    // check inclusion exclusion service
					 incExcRes = iTPackageIncExcDetailsDao
			                   	.getTPackageIncExcDetailsForBillingByPackageConsumptionId(mpkMstDto.getPackageConsumptionMasterId());
					 mpkMstDto.setListTPackageIncExcDetailsDto(incExcRes.getListObject());

					// get cap details
				    packageCapRes = iPackageConsumptionDao.getPackageConsumptionCapDetails(mpkMstDto.getPackageConsumptionMasterId());
				    mpkMstDto.setListTPackageConsumptionCapDetailsDto(packageCapRes.getListObject());
				}

						//for item iteration
						for (CommonBillingItemsDto itemDto : dto.getItemList()) {
							if(itemDto.getSelfPayable()!=null && itemDto.getSelfPayable().doubleValue()>0)
							{
								//Integer itemId = (itemDto.getItemId()!=null && itemDto.getItemId()>0)?itemDto.getItemId():itemDto.getConsumedItemId();
								//HIS-21658
								Integer itemId = itemDto.getItemId();
								Integer productCategoryId = (itemDto.getConsumedPcategroyId()!=null && itemDto.getConsumedPcategroyId()>0)?itemDto.getConsumedPcategroyId():itemDto.getProductCategoryId();
								Integer quantity = itemDto.getSelfQuantity();
								double totalNetAmount=0.00;
								Double itemNetAmount=itemDto.getNetAmount();
								for (int qty = 1 ; qty <=quantity;  qty++) {
									double unitNetAmount = getNetAmountByQuantity(itemDto,dto.isTaxInclusive());
									totalNetAmount=totalNetAmount+unitNetAmount;
									if(qty==quantity && !itemNetAmount.equals(totalNetAmount))
									{
										double diff = itemNetAmount-totalNetAmount;
										unitNetAmount=unitNetAmount+diff;
                                        unitNetAmount=GlobalCommonDateUtils.getAmount(unitNetAmount);

										
									}
									itemDto.setUnitNetAmount(unitNetAmount);
									for (PackageConsumptionMasterResDto resDto : listResDto)
									{
										Date validityFrom=resDto.getValidityFrom()!=null ?  GlobalCommonDateUtils.getDate(resDto.getValidityFrom(), "dd-M-yyyy") : GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy");
										Date validityTo = resDto.getValidityTo() !=null ? GlobalCommonDateUtils.getDate(resDto.getValidityTo(), "dd-M-yyyy HH:mm:ss") : GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(unitId), "dd-M-yyyy HH:mm:ss");

										Long packEffectiveDate = GlobalCommonDateUtils.getDate(resDto.getPackageApplicableDate(), "dd-MM-yyyy").getTime();

										
										boolean isPackageEffective = false;
										if (resDto.getPackageTypeId() == 5 
												|| resDto.getPackageTypeId() == 4
												|| resDto.getPackageTypeId() == 2) {
											isPackageEffective = (packEffectiveDate <= orderDateLong
													&& orderDate.compareTo(validityFrom) >= 0 && orderDate.compareTo(validityTo) <= 0);
										}else if(resDto.getValidityTo()!=null){
											isPackageEffective = packEffectiveDate <= orderDateLong && orderDate.compareTo(validityTo) <= 0;
										}else{
											isPackageEffective = packEffectiveDate <= orderDateLong;
										}
										
										if(isPackageEffective && dto.getForceAddRemoved().intValue()==0)
										{	
											ListIterator<PackageConsumptionServiceDetailDto> resSrvItr = null;
											List<PackageConsumptionServiceDetailDto> serviceList = resDto.getListPackageConsumptionServiceDetailDto();
											Boolean isDateValid = orderDate.compareTo(validityFrom) >= 0 && orderDate.compareTo(validityTo) <= 0;

										switch (resDto.getPackageTypeId()) {
										case 1: // EHC package
											break;
										case 2: // Either or Package
									        break;
								        case 3:
									// package with cap
									// check service

							       if(resDto.isBillingBedExist()) {
                                       if (resDto.getListPackageConsumptionServiceDetailDto() != null
                                               && !resDto.getListPackageConsumptionServiceDetailDto().isEmpty()) {
                                           resSrvItr = resDto.getListPackageConsumptionServiceDetailDto().listIterator();
                                           PackageConsumptionServiceDetailDto srv = null;
                                           while (resSrvItr.hasNext()) {
                                               srv = resSrvItr.next();
                                               if (unitNetAmount > 0) {
                                                   //1.. For Service Tab
                                                   if (srv.getServiceId() != null && srv.getAvailableCap() > 0 && srv.getProductCategoryId() != null && srv.getProductCategoryId().equals(productCategoryId) && srv.getIsServiceItem() == 1 && itemDto.getSelfPayable().doubleValue() > 0) {
                                                       if (unitNetAmount <= srv.getAvailableCap()) {
                                                           double capAmount = srv.getAvailableCap() - unitNetAmount;
                                                           if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
//														srv.setAvailableCap(capAmount);
                                                               /**SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                oDPmpprDto.setIsPackageItem('Y');
                                                                oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                oDPmpprDto.setPackageAmount(unitNetAmount);
                                                                oDPmpprDto.setPackageQty(1);
                                                                oDPmpprDto.setCapOrQty(1);
                                                                oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//														itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+unitNetAmount);
                                                               itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', unitNetAmount, itemDto.getUnitNetAmount(), dto.getConsume(), 1,dto.getPharmacyReturnServiceRecal()));
                                                               /**    if(dto.getConsume()) {
                                                                iTPackageConsumptionCapDetailsDao.updatePackageCapItem(srv.getPackageConsumptionServiceDetailId(), (unitNetAmount*-1),false);
                                                                }
                                                                itemDto.setSelfPayable(itemDto.getSelfPayable()-unitNetAmount);
                                                                itemDto.setSelfQuantity(itemDto.getSelfQuantity()-1);
                                                                itemDto.setNetAmount(itemDto.getNetAmount()-unitNetAmount);
                                                                unitNetAmount = 0;*/
                                                           } else {
//														double capAmount = srv.getAvailableCap()-unitNetAmount;
//														srv.setAvailableCap(capAmount);
                                                               List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
                                                               /**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                oDPmpprDto.setIsPackageItem('Y');
                                                                oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                oDPmpprDto.setPackageAmount(unitNetAmount);
                                                                oDPmpprDto.setPackageQty(1);
                                                                oDPmpprDto.setCapOrQty(1);
                                                                oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//														itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+unitNetAmount);
                                                               mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', unitNetAmount, itemDto.getUnitNetAmount(), dto.getConsume(), 1,dto.getPharmacyReturnServiceRecal()));
                                                               itemDto.setSaleDetailPackageMapperDtoList(mppr);
                                                               /**if(dto.getConsume()) {
                                                                iTPackageConsumptionCapDetailsDao.updatePackageCapItem(srv.getPackageConsumptionServiceDetailId(), (unitNetAmount*-1),false);
                                                                }
                                                                itemDto.setSelfPayable(itemDto.getSelfPayable()-unitNetAmount);
                                                                itemDto.setSelfQuantity(itemDto.getSelfQuantity()-1);
                                                                itemDto.setNetAmount(itemDto.getNetAmount()-unitNetAmount);
                                                                unitNetAmount = 0;*/
                                                           }
                                                           srv.setAvailableCap(capAmount);
                                                           itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
                                                           itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
                                                           itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
                                                           itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
                                                           unitNetAmount = 0;
                                                       } else {
                                                           double rate = unitNetAmount - srv.getAvailableCap();
                                                           if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
                                                               /** SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                oDPmpprDto.setIsPackageItem('Y');
                                                                oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                oDPmpprDto.setPackageAmount(srv.getAvailableCap());
                                                                oDPmpprDto.setPackageQty(1);
                                                                oDPmpprDto.setCapOrQty(1);
                                                                oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//															itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+srv.getAvailableCap());
                                                               itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', srv.getAvailableCap(), itemDto.getUnitNetAmount(), dto.getConsume(), 1,dto.getPharmacyReturnServiceRecal()));
                                                           } else {
                                                               List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
                                                               /**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                oDPmpprDto.setIsPackageItem('Y');
                                                                oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                oDPmpprDto.setPackageAmount(srv.getAvailableCap());
                                                                oDPmpprDto.setPackageQty(1);
                                                                oDPmpprDto.setCapOrQty(1);
                                                                oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//															itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+srv.getAvailableCap());
                                                               mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', srv.getAvailableCap(), itemDto.getUnitNetAmount(), dto.getConsume(), 1,dto.getPharmacyReturnServiceRecal()));
                                                               itemDto.setSaleDetailPackageMapperDtoList(mppr);
                                                           }
//                                                        if(dto.getConsume()) {
//                                                        iTPackageConsumptionCapDetailsDao.updatePackageCapItem(srv.getPackageConsumptionServiceDetailId(),(srv.getAvailableCap()*-1),false);
//                                                        }
                                                           itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + srv.getAvailableCap());
                                                           itemDto.setSelfPayable(itemDto.getSelfPayable() - srv.getAvailableCap());
                                                           itemDto.setNetAmount(itemDto.getNetAmount() - srv.getAvailableCap());
                                                           unitNetAmount = rate;
                                                           itemDto.setApportionedRate(rate);
                                                           srv.setAvailableCap(0.00);
                                                       }
                                                   } else {
                                                       if (serviceList != null) {
                                                           Optional<PackageConsumptionServiceDetailDto> serviceRes = serviceList
                                                                   .stream().parallel()
                                                                   .filter(item -> item.getProductCategoryId() != null && item.getIsServiceItem() == 1)
                                                                   .filter(item -> item.getProductCategoryId().equals(productCategoryId)).findAny();
                                                           if (serviceRes.isPresent())
                                                               continue;
                                                       }
                                                       if (srv.getItemId() != null && srv.getAvailableCap() > 0 && srv.getBalanceQuantity() == null && srv.getItemId().equals(itemId) && itemDto.getSelfPayable().doubleValue() > 0) {
                                                           //for (int qty =1 ; qty <=quantity ; qty++){
                                                           if (unitNetAmount <= srv.getAvailableCap()) {
                                                               double capAmount = srv.getAvailableCap() - unitNetAmount;
                                                               if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
//															double capAmount = srv.getAvailableCap()-unitNetAmount;
//															srv.setAvailableCap(capAmount);
                                                                   /**        SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                    oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                    oDPmpprDto.setIsPackageItem('Y');
                                                                    oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                    oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                    oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                    oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                    oDPmpprDto.setPackageAmount(unitNetAmount);
                                                                    oDPmpprDto.setPackageQty(1);
                                                                    oDPmpprDto.setCapOrQty(1);
                                                                    oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//															itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+unitNetAmount);
                                                                   itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', unitNetAmount, itemDto.getUnitNetAmount(), dto.getConsume(), 1,dto.getPharmacyReturnServiceRecal()));
//															if(dto.getConsume()) {
//																iTPackageConsumptionCapDetailsDao.updatePackageCapItem(srv.getPackageConsumptionServiceDetailId(), (unitNetAmount*-1),false);
//															}
//															itemDto.setSelfPayable(itemDto.getSelfPayable()-unitNetAmount);
//															itemDto.setSelfQuantity(itemDto.getSelfQuantity()-1);
//															itemDto.setNetAmount(itemDto.getNetAmount()-unitNetAmount);
//															unitNetAmount = 0;
                                                               } else {
//															double capAmount = srv.getAvailableCap()-unitNetAmount;
//															srv.setAvailableCap(capAmount);
                                                                   List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
                                                                   /**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                    oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                    oDPmpprDto.setIsPackageItem('Y');
                                                                    oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                    oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                    oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                    oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                    oDPmpprDto.setPackageAmount(unitNetAmount);
                                                                    oDPmpprDto.setPackageQty(1);
                                                                    oDPmpprDto.setCapOrQty(1);
                                                                    oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//															itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+unitNetAmount);
                                                                   mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', unitNetAmount, itemDto.getUnitNetAmount(), dto.getConsume(), 1,dto.getPharmacyReturnServiceRecal()));
                                                                   itemDto.setSaleDetailPackageMapperDtoList(mppr);
//															if(dto.getConsume()) {
//															iTPackageConsumptionCapDetailsDao.updatePackageCapItem(srv.getPackageConsumptionServiceDetailId(), (unitNetAmount*-1),false);
//															}
//															itemDto.setSelfPayable(itemDto.getSelfPayable()-unitNetAmount);
//															itemDto.setNetAmount(itemDto.getNetAmount()-unitNetAmount);
//															itemDto.setSelfQuantity(itemDto.getSelfQuantity()-1);
//															unitNetAmount = 0;
                                                               }
                                                               srv.setAvailableCap(capAmount);
                                                               itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
                                                               itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
                                                               itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
                                                               itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
                                                               unitNetAmount = 0;
                                                           } else {
                                                               double rate = unitNetAmount - srv.getAvailableCap();
                                                               if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
//																double rate = unitNetAmount-srv.getAvailableCap();
                                                                   /**SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                    oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                    oDPmpprDto.setIsPackageItem('Y');
                                                                    oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                    oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                    oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                    oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                    oDPmpprDto.setPackageAmount(srv.getAvailableCap());
                                                                    oDPmpprDto.setPackageQty(1);
                                                                    oDPmpprDto.setCapOrQty(1);
                                                                    oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+srv.getAvailableCap());
                                                                   itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', srv.getAvailableCap(), itemDto.getUnitNetAmount(), dto.getConsume(), 1,dto.getPharmacyReturnServiceRecal()));
                                                                   if (dto.getConsume()) {
                                                                       iTPackageConsumptionCapDetailsDao.updatePackageCapItem(srv.getPackageConsumptionServiceDetailId(), (srv.getAvailableCap() * -1), false);
                                                                   }
                                                                   /**    itemDto.setSelfPayable(itemDto.getSelfPayable()-srv.getAvailableCap());
                                                                    itemDto.setNetAmount(itemDto.getNetAmount()-srv.getAvailableCap());
                                                                    unitNetAmount = rate;
                                                                    itemDto.setApportionedRate(rate);
                                                                    srv.setAvailableCap(0.00);*/
                                                               } else {
//																double rate = unitNetAmount-srv.getAvailableCap();
                                                                   List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
                                                                   /**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                    oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                    oDPmpprDto.setIsPackageItem('Y');
                                                                    oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                    oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                    oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                    oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                    oDPmpprDto.setPackageAmount(srv.getAvailableCap());
                                                                    oDPmpprDto.setPackageQty(1);
                                                                    oDPmpprDto.setCapOrQty(1);
                                                                    oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+srv.getAvailableCap());
                                                                   mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', srv.getAvailableCap(), itemDto.getUnitNetAmount(), dto.getConsume(), 1,dto.getPharmacyReturnServiceRecal()));
                                                                   itemDto.setSaleDetailPackageMapperDtoList(mppr);
//																if(dto.getConsume()) {
//																	iTPackageConsumptionCapDetailsDao.updatePackageCapItem(srv.getPackageConsumptionServiceDetailId(),(srv.getAvailableCap()*-1),false);
//																}
                                                                   /**    itemDto.setSelfPayable(itemDto.getSelfPayable()-srv.getAvailableCap());
                                                                    itemDto.setNetAmount(itemDto.getNetAmount()-srv.getAvailableCap());
                                                                    unitNetAmount = rate;
                                                                    itemDto.setApportionedRate(rate);
                                                                    srv.setAvailableCap(0.00);*/
                                                               }
                                                               itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + srv.getAvailableCap());
                                                               itemDto.setSelfPayable(itemDto.getSelfPayable() - srv.getAvailableCap());
                                                               itemDto.setNetAmount(itemDto.getNetAmount() - srv.getAvailableCap());
                                                               unitNetAmount = rate;
                                                               itemDto.setApportionedRate(rate);
                                                               srv.setAvailableCap(0.00);
                                                           }
                                                           //for close   }
                                                       } else if (srv.getItemId() != null && srv.getAvailableCap() > 0 && srv.getBalanceQuantity() != null && srv.getBalanceQuantity() > 0 && srv.getItemId().equals(itemId) && itemDto.getSelfPayable().doubleValue() > 0) {
                                                           //for (int qty =1 ; qty <=quantity ; qty++){
                                                           if (srv.getBalanceQuantity() > 0 && srv.getAvailableCap() > 0) {
                                                               if (unitNetAmount <= srv.getAvailableCap()) {
                                                                   double capAmount = srv.getAvailableCap() - unitNetAmount;
                                                                   if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
                                                                       srv.setAvailableCap(capAmount);
                                                                       srv.setBalanceQuantity(srv.getBalanceQuantity() - 1);
                                                                       /**        SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                        oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                        oDPmpprDto.setIsPackageItem('Y');
                                                                        oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                        oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                        oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                        oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                        oDPmpprDto.setPackageAmount(unitNetAmount);
                                                                        oDPmpprDto.setPackageQty(1);
                                                                        oDPmpprDto.setCapOrQty(2);
                                                                        oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//															itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+unitNetAmount);
                                                                       itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', unitNetAmount, itemDto.getUnitNetAmount(), dto.getConsume(), 2,dto.getPharmacyReturnServiceRecal()));
                                                                       /**    if(dto.getConsume()) {
                                                                        iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapItem(srv.getPackageConsumptionServiceDetailId(), (1*-1), (unitNetAmount*-1),false);
                                                                        }
                                                                        itemDto.setSelfPayable(itemDto.getSelfPayable()-unitNetAmount);
                                                                        itemDto.setNetAmount(itemDto.getNetAmount()-unitNetAmount);
                                                                        itemDto.setSelfQuantity(itemDto.getSelfQuantity()-1);
                                                                        unitNetAmount = 0;*/
                                                                   } else {
//															double capAmount = srv.getAvailableCap()-unitNetAmount;
                                                                       srv.setAvailableCap(capAmount);
                                                                       srv.setBalanceQuantity(srv.getBalanceQuantity() - 1);
                                                                       List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
                                                                       /**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                        oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                        oDPmpprDto.setIsPackageItem('Y');
                                                                        oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                        oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                        oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                        oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                        oDPmpprDto.setPackageAmount(unitNetAmount);
                                                                        oDPmpprDto.setPackageQty(1);
                                                                        oDPmpprDto.setCapOrQty(2);
                                                                        oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//															itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+unitNetAmount);
                                                                       mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', unitNetAmount, itemDto.getUnitNetAmount(), dto.getConsume(), 2,dto.getPharmacyReturnServiceRecal()));
                                                                       itemDto.setSaleDetailPackageMapperDtoList(mppr);
                                                                       /**if(dto.getConsume()) {
                                                                        iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapItem(srv.getPackageConsumptionServiceDetailId(), (1*-1), (unitNetAmount*-1),false);
                                                                        }
                                                                        itemDto.setSelfPayable(itemDto.getSelfPayable()-unitNetAmount);
                                                                        itemDto.setNetAmount(itemDto.getNetAmount()-unitNetAmount);
                                                                        itemDto.setSelfQuantity(itemDto.getSelfQuantity()-1);
                                                                        unitNetAmount = 0;*/
                                                                   }
                                                                   itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
                                                                   itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
                                                                   itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
                                                                   itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
                                                                   unitNetAmount = 0;
                                                               } else {
                                                                   double rate = unitNetAmount - srv.getAvailableCap();
                                                                   if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
//																double rate = unitNetAmount-srv.getAvailableCap();
                                                                       /**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                        oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                        oDPmpprDto.setIsPackageItem('Y');
                                                                        oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                        oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                        oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                        oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                        oDPmpprDto.setPackageAmount(srv.getAvailableCap());
                                                                        oDPmpprDto.setPackageQty(1);
                                                                        oDPmpprDto.setCapOrQty(2);
                                                                        oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
                                                                     //  itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + srv.getAvailableCap());
                                                                       itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', srv.getAvailableCap(),
                                                                               itemDto.getUnitNetAmount(), dto.getConsume(), 2,dto.getPharmacyReturnServiceRecal()));
                                                                       /**    if(dto.getConsume()) {
                                                                        iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapItem(srv.getPackageConsumptionServiceDetailId(), (1*-1), (srv.getAvailableCap()*-1),false);
                                                                        }
                                                                        itemDto.setSelfPayable(itemDto.getSelfPayable()-srv.getAvailableCap());
                                                                        itemDto.setNetAmount(itemDto.getNetAmount()-srv.getAvailableCap());
                                                                        unitNetAmount = rate;
                                                                        itemDto.setApportionedRate(rate);
                                                                        srv.setAvailableCap(0.00);
                                                                        srv.setBalanceQuantity(srv.getBalanceQuantity()-1);*/
                                                                   } else {
//																double rate = unitNetAmount-srv.getAvailableCap();
                                                                       List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
                                                                       /**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                        oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                        oDPmpprDto.setIsPackageItem('Y');
                                                                        oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                        oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                        oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
                                                                        oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
                                                                        oDPmpprDto.setPackageAmount(srv.getAvailableCap());
                                                                        oDPmpprDto.setPackageQty(1);
                                                                        oDPmpprDto.setCapOrQty(2);
                                                                        oDPmpprDto.setRate(itemDto.getUnitNetAmount());
                                                                        itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+srv.getAvailableCap());*/
                                                                       mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', srv.getAvailableCap(), itemDto.getUnitNetAmount(), dto.getConsume(),
                                                                               2,dto.getPharmacyReturnServiceRecal()));
                                                                       itemDto.setSaleDetailPackageMapperDtoList(mppr);
                                                                       /**    if(dto.getConsume()) {
                                                                        iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapItem(srv.getPackageConsumptionServiceDetailId(), (1*-1), (srv.getAvailableCap()*-1),false);
                                                                        }
                                                                        itemDto.setSelfPayable(itemDto.getSelfPayable()-srv.getAvailableCap());
                                                                        itemDto.setNetAmount(itemDto.getNetAmount()-srv.getAvailableCap());
                                                                        unitNetAmount = rate;
                                                                        itemDto.setApportionedRate(rate);
                                                                        srv.setAvailableCap(0.00);
                                                                        srv.setBalanceQuantity(srv.getBalanceQuantity()-1);*/
                                                                   }
                                                                   itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + srv.getAvailableCap());
                                                                   itemDto.setSelfPayable(itemDto.getSelfPayable() - srv.getAvailableCap());
                                                                   itemDto.setNetAmount(itemDto.getNetAmount() - srv.getAvailableCap());
                                                                   unitNetAmount = rate;
                                                                   itemDto.setApportionedRate(rate);
                                                                   srv.setAvailableCap(0.00);
                                                                   srv.setBalanceQuantity(srv.getBalanceQuantity() - 1);
                                                               }
                                                           }
                                                           //for close   }
                                                       }
                                                   }
                                               }
                                           }
                                       }


                                       if (resDto.getListTPackageConsumptionCapDetailsDto() != null && !resDto.getListTPackageConsumptionCapDetailsDto().isEmpty()) {
                                           ListIterator<TPackageConsumptionCapDetailsDto> capDtlsItr = resDto.getListTPackageConsumptionCapDetailsDto()
                                                   .listIterator();
                                           TPackageConsumptionCapDetailsDto capDtls = null;
                                           while (capDtlsItr.hasNext()) {
                                               capDtls = capDtlsItr.next();
                                               // get service details by id

                                               if (capDtls.getProductCategoryId() != null && itemDto.getSelfPayable().doubleValue() > 0 && unitNetAmount >0) {
                                                   if (capDtls.getProductCategoryId().equals(productCategoryId) &&
                                                           itemDto.getSelfPayable().doubleValue() > 0) {
                                                       if (resDto.getListTPackageIncExcDetailsDto() != null
                                                               && !resDto.getListTPackageIncExcDetailsDto().isEmpty() && itemDto.getSelfPayable().doubleValue() > 0) {
                                                           Optional<TPackageIncExcDetailsDto> result = null;
                                                           List<TPackageIncExcDetailsDto> incexcItr = resDto.getListTPackageIncExcDetailsDto();

                                                           result = incexcItr
                                                                   .stream().parallel()
                                                                   .filter(item -> item.getItemId() != null && item.getItemId().equals(itemId) && item.getIsInclusiveService() == 2).findAny();

                                                           if (result.isPresent())
                                                               continue;


                                                           if (serviceList != null) {
                                                               Optional<PackageConsumptionServiceDetailDto> serviceRes = serviceList
                                                                       .stream().parallel()
                                                                       .filter(item -> item.getProductCategoryId() != null && item.getIsServiceItem() == 1)
                                                                       .filter(item -> item.getProductCategoryId().equals(productCategoryId)).findAny();
                                                               if (serviceRes.isPresent())
                                                                   continue;
                                                           }
                                                       }
                                                       if (capDtls.getAvailableCap() > 0 && capDtls.getQuantity() == null && itemDto.getSelfPayable().doubleValue() > 0) {
                                                           //for (int qty =1 ; qty <=quantity ; qty++){
                                                           if (unitNetAmount <= capDtls.getAvailableCap()) {
                                                               double capAmount = capDtls.getAvailableCap() - unitNetAmount;
                                                               if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
//																	double capAmount = capDtls.getAvailableCap()-unitNetAmount;
//																	capDtls.setAvailableCap(capAmount);
                                                                   /**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                    oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                    oDPmpprDto.setIsPackageItem('Y');
                                                                    oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                    oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                    oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
                                                                    oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
                                                                    oDPmpprDto.setPackageAmount(unitNetAmount);
                                                                    oDPmpprDto.setPackageQty(1);
                                                                    oDPmpprDto.setCapOrQty(1);
                                                                    oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
                                                                   //itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
                                                                   itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, capDtls, 'Y', unitNetAmount, itemDto.getUnitNetAmount(), dto.getConsume(), 1,dto.getPharmacyReturnServiceRecal()));
                                                                   /**if(dto.getConsume()) {
                                                                    iTPackageConsumptionCapDetailsDao.updatePackageCapProductCategory(capDtls.getPackageConsumptionCapDetailsId(), (unitNetAmount*-1),false);
                                                                    }*/
                                                                   /**    itemDto.setSelfPayable(itemDto.getSelfPayable()-unitNetAmount);
                                                                    itemDto.setNetAmount(itemDto.getNetAmount()-unitNetAmount);
                                                                    itemDto.setSelfQuantity(itemDto.getSelfQuantity()-1);
                                                                    unitNetAmount = 0;*/
                                                               } else {
//																	double capAmount = capDtls.getAvailableCap()-unitNetAmount;
//																	capDtls.setAvailableCap(capAmount);
                                                                   List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
                                                                   /**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                    oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                    oDPmpprDto.setIsPackageItem('Y');
                                                                    oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                    oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                    oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
                                                                    oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
                                                                    oDPmpprDto.setPackageAmount(unitNetAmount);
                                                                    oDPmpprDto.setPackageQty(1);
                                                                    oDPmpprDto.setCapOrQty(1);
                                                                    oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																	itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+unitNetAmount);
                                                                   mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, capDtls, 'Y', unitNetAmount, itemDto.getUnitNetAmount(), dto.getConsume(), 1,dto.getPharmacyReturnServiceRecal()));
                                                                   itemDto.setSaleDetailPackageMapperDtoList(mppr);
                                                                   /**    if(dto.getConsume()) {
                                                                    iTPackageConsumptionCapDetailsDao.updatePackageCapProductCategory(capDtls.getPackageConsumptionCapDetailsId(), (unitNetAmount*-1),false);
                                                                    }*/
                                                                   /**itemDto.setSelfPayable(itemDto.getSelfPayable()-unitNetAmount);
                                                                    itemDto.setNetAmount(itemDto.getNetAmount()-unitNetAmount);
                                                                    itemDto.setSelfQuantity(itemDto.getSelfQuantity()-1);
                                                                    unitNetAmount = 0;*/
                                                               }
                                                               capDtls.setAvailableCap(capAmount);
                                                               itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
                                                               itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
                                                               itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
                                                               itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
                                                               unitNetAmount = 0;

                                                           } else {
                                                               double rate = unitNetAmount - capDtls.getAvailableCap();
                                                               if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
//																		double rate = unitNetAmount-capDtls.getAvailableCap();
                                                                   /**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                    oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                    oDPmpprDto.setIsPackageItem('Y');
                                                                    oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                    oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                    oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
                                                                    oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
                                                                    oDPmpprDto.setPackageAmount(capDtls.getAvailableCap());
                                                                    oDPmpprDto.setPackageQty(1);
                                                                    oDPmpprDto.setCapOrQty(1);
                                                                    oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																		itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+capDtls.getAvailableCap());
                                                                   itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, capDtls, 'Y', capDtls.getAvailableCap(), itemDto.getUnitNetAmount(), dto.getConsume(), 1,dto.getPharmacyReturnServiceRecal()));
                                                                   /**        if(dto.getConsume()) {
                                                                    iTPackageConsumptionCapDetailsDao.updatePackageCapProductCategory(capDtls.getPackageConsumptionCapDetailsId(),(capDtls.getAvailableCap()*-1),false);
                                                                    }
                                                                    itemDto.setSelfPayable(itemDto.getSelfPayable()-capDtls.getAvailableCap());
                                                                    itemDto.setNetAmount(itemDto.getNetAmount()-capDtls.getAvailableCap());
                                                                    unitNetAmount = rate;
                                                                    itemDto.setApportionedRate(rate);
                                                                    capDtls.setAvailableCap(0.00);*/
                                                               } else {
//																		double rate = unitNetAmount-capDtls.getAvailableCap();
                                                                   List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
                                                                   /**        SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                    oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                    oDPmpprDto.setIsPackageItem('Y');
                                                                    oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                    oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                    oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
                                                                    oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
                                                                    oDPmpprDto.setPackageAmount(capDtls.getAvailableCap());
                                                                    oDPmpprDto.setPackageQty(1);
                                                                    oDPmpprDto.setCapOrQty(1);
                                                                    oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																		itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+capDtls.getAvailableCap());
                                                                   mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, capDtls, 'Y', capDtls.getAvailableCap(), itemDto.getUnitNetAmount(), dto.getConsume(), 1,dto.getPharmacyReturnServiceRecal()));
                                                                   itemDto.setSaleDetailPackageMapperDtoList(mppr);
                                                                   /**    if(dto.getConsume()) {
                                                                    iTPackageConsumptionCapDetailsDao.updatePackageCapProductCategory(capDtls.getPackageConsumptionCapDetailsId(),(capDtls.getAvailableCap()*-1),false);
                                                                    }
                                                                    itemDto.setSelfPayable(itemDto.getSelfPayable()-capDtls.getAvailableCap());
                                                                    itemDto.setNetAmount(itemDto.getNetAmount()-capDtls.getAvailableCap());
                                                                    unitNetAmount = rate;
                                                                    itemDto.setApportionedRate(rate);
                                                                    capDtls.setAvailableCap(0.00);*/
                                                               }
                                                               itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + capDtls.getAvailableCap());
                                                               itemDto.setSelfPayable(itemDto.getSelfPayable() - capDtls.getAvailableCap());
                                                               itemDto.setNetAmount(itemDto.getNetAmount() - capDtls.getAvailableCap());
                                                               unitNetAmount = rate;
                                                               itemDto.setApportionedRate(rate);
                                                               capDtls.setAvailableCap(0.00);
                                                           }
                                                           // qty close  }
                                                       } else if (capDtls.getAvailableCap() > 0 && capDtls.getQuantity() != null && capDtls.getQuantity() > 0 && itemDto.getSelfPayable().doubleValue() > 0) {
                                                           //	for (int qty =1 ; qty <=quantity ; qty++)   {
                                                           if (capDtls.getQuantity() > 0 && capDtls.getAvailableCap() > 0) {
                                                               double capAmount = capDtls.getAvailableCap() - unitNetAmount;
                                                               if (unitNetAmount <= capDtls.getAvailableCap()) {
                                                                   if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
//																	double capAmount = capDtls.getAvailableCap()-unitNetAmount;
//																		 capDtls.setAvailableCap(capAmount);
//																		 capDtls.setQuantity(capDtls.getQuantity() - 1);
                                                                       /**     SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                        oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                        oDPmpprDto.setIsPackageItem('Y');
                                                                        oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                        oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                        oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
                                                                        oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
                                                                        oDPmpprDto.setPackageAmount(unitNetAmount);
                                                                        oDPmpprDto.setPackageQty(1);
                                                                        oDPmpprDto.setCapOrQty(2);
                                                                        oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																		 itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
                                                                       itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, capDtls, 'Y', unitNetAmount, itemDto.getUnitNetAmount(), dto.getConsume(), 2,dto.getPharmacyReturnServiceRecal()));
                                                                       /**     if (dto.getConsume()) {
                                                                        iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapProductCategory(capDtls.getPackageConsumptionCapDetailsId(), (1 * -1), (unitNetAmount * -1), false);
                                                                        }
                                                                        itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
                                                                        itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
                                                                        itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
                                                                        unitNetAmount = 0;*/
                                                                   } else {
//																		 double capAmount = capDtls.getAvailableCap() - unitNetAmount;
//																		 capDtls.setAvailableCap(capAmount);
//																		 capDtls.setQuantity(capDtls.getQuantity() - 1);
                                                                       List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
                                                                       /**     SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                        oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                        oDPmpprDto.setIsPackageItem('Y');
                                                                        oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                        oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                        oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
                                                                        oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
                                                                        oDPmpprDto.setPackageAmount(unitNetAmount);
                                                                        oDPmpprDto.setPackageQty(1);
                                                                        oDPmpprDto.setCapOrQty(2);
                                                                        oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																		 itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
                                                                       mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, capDtls, 'Y', unitNetAmount, itemDto.getUnitNetAmount(), dto.getConsume(), 2,dto.getPharmacyReturnServiceRecal()));
                                                                       itemDto.setSaleDetailPackageMapperDtoList(mppr);
                                                                       /**     if (dto.getConsume()) {
                                                                        iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapProductCategory(capDtls.getPackageConsumptionCapDetailsId(), (1 * -1), (unitNetAmount * -1), false);
                                                                        }
                                                                        itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
                                                                        itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
                                                                        itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
                                                                        unitNetAmount = 0;*/
                                                                   }
                                                                   capDtls.setAvailableCap(capAmount);
                                                                   capDtls.setQuantity(capDtls.getQuantity() - 1);
                                                                   itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
                                                                   itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
                                                                   itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
                                                                   itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
                                                                   unitNetAmount = 0;
                                                               } else {
                                                                   double rate = unitNetAmount - capDtls.getAvailableCap();

                                                                   if (itemDto.getSaleDetailPackageMapperDtoList() != null && itemDto.getSaleDetailPackageMapperDtoList().size() > 0) {
//																		double rate = unitNetAmount-capDtls.getAvailableCap();
                                                                       /**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                        oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                        oDPmpprDto.setIsPackageItem('Y');
                                                                        oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                        oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                        oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
                                                                        oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
                                                                        oDPmpprDto.setPackageAmount(capDtls.getAvailableCap());
                                                                        oDPmpprDto.setPackageQty(1);
                                                                        oDPmpprDto.setCapOrQty(2);
                                                                        oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																		itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+capDtls.getAvailableCap());
                                                                       itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, capDtls, 'Y', capDtls.getAvailableCap(), itemDto.getUnitNetAmount(), dto.getConsume(), 2,dto.getPharmacyReturnServiceRecal()));
                                                                       /**        if(dto.getConsume()) {
                                                                        iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapProductCategory(capDtls.getPackageConsumptionCapDetailsId(), (1*-1), (capDtls.getAvailableCap()*-1),false);
                                                                        }
                                                                        itemDto.setSelfPayable(itemDto.getSelfPayable()-capDtls.getAvailableCap());
                                                                        itemDto.setNetAmount(itemDto.getNetAmount()-capDtls.getAvailableCap());
                                                                        unitNetAmount = rate;
                                                                        itemDto.setApportionedRate(rate);
                                                                        capDtls.setAvailableCap(0.00);
                                                                        capDtls.setQuantity(capDtls.getQuantity()-1);*/

                                                                   } else {
//																		double rate = unitNetAmount-capDtls.getAvailableCap();
                                                                       List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
                                                                       /**        SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
                                                                        oDPmpprDto.setPackageId(resDto.getPackageId());
                                                                        oDPmpprDto.setIsPackageItem('Y');
                                                                        oDPmpprDto.setPackageName(resDto.getPackageName());
                                                                        oDPmpprDto.setPackageCode(resDto.getPackageCode());
                                                                        oDPmpprDto.setPackageConsumptionMasterId(capDtls.getPackageConsumptionMasterId());
                                                                        oDPmpprDto.setPackageConsumptionCapDetailsId(capDtls.getPackageConsumptionCapDetailsId());
                                                                        oDPmpprDto.setPackageAmount(capDtls.getAvailableCap());
                                                                        oDPmpprDto.setPackageQty(1);
                                                                        oDPmpprDto.setCapOrQty(2);
                                                                        oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																		itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt()+capDtls.getAvailableCap());
                                                                       mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, capDtls, 'Y', capDtls.getAvailableCap(), itemDto.getUnitNetAmount(), dto.getConsume(), 2,dto.getPharmacyReturnServiceRecal()));
                                                                       itemDto.setSaleDetailPackageMapperDtoList(mppr);
                                                                       /**        if(dto.getConsume()) {
                                                                        iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapProductCategory(capDtls.getPackageConsumptionCapDetailsId(), (1*-1), (capDtls.getAvailableCap()*-1),false);
                                                                        }
                                                                        itemDto.setSelfPayable(itemDto.getSelfPayable()-capDtls.getAvailableCap());
                                                                        itemDto.setNetAmount(itemDto.getNetAmount()-capDtls.getAvailableCap());
                                                                        unitNetAmount = rate;
                                                                        itemDto.setApportionedRate(rate);
                                                                        capDtls.setAvailableCap(0.00);
                                                                        capDtls.setQuantity(capDtls.getQuantity()-1);*/
                                                                   }
                                                                   itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + capDtls.getAvailableCap());
                                                                   itemDto.setSelfPayable(itemDto.getSelfPayable() - capDtls.getAvailableCap());
                                                                   itemDto.setNetAmount(itemDto.getNetAmount() - capDtls.getAvailableCap());
                                                                   unitNetAmount = rate;
                                                                   itemDto.setApportionedRate(rate);
                                                                   capDtls.setAvailableCap(0.00);
                                                                   capDtls.setQuantity(capDtls.getQuantity() - 1);
                                                               }
                                                           }
                                                           // }
                                                       }
                                                   }
                                               }
                                           }
                                       }
                                   }
									break;
								case 4: // All exclusive package

									if(unitNetAmount>0) {
										// check inclusion exclusion service
										if (isDateValid) {
											if (resDto.getListTPackageIncExcDetailsDto() != null
													&& !resDto.getListTPackageIncExcDetailsDto().isEmpty() && itemDto.getSelfPayable().doubleValue() > 0) {
												List<TPackageIncExcDetailsDto> incexcItr = resDto.getListTPackageIncExcDetailsDto();
												Optional<TPackageIncExcDetailsDto> result = null;

												result = incexcItr.stream().parallel()
														.filter(item -> item.getProductCategoryId() != null && item.getProductCategoryId().equals(productCategoryId) && item.getIsInclusiveService() == 2).findAny();
												if (result.isPresent())
													continue;

												result = incexcItr
														.stream().parallel()
														.filter(item -> item.getItemId() != null && item.getItemId().equals(itemId) && item.getIsInclusiveService() == 2).findAny();


												if (result.isPresent())
													continue;

												//for (int qty =1 ; qty <=quantity ; qty++) {
												if (itemDto.getSelfPayable().doubleValue() > 0 && !result.isPresent() && unitNetAmount > 0) {
													if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
													/**	SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
														oDPmpprDto.setPackageId(resDto.getPackageId());
														oDPmpprDto.setIsPackageItem('Y');
														oDPmpprDto.setPackageName(resDto.getPackageName());
														oDPmpprDto.setPackageCode(resDto.getPackageCode());
														oDPmpprDto.setPackageQty(1);
														oDPmpprDto.setCapOrQty(1);
														oDPmpprDto.setPackageTypeId(resDto.getPackageTypeId());
														oDPmpprDto.setPackageAmount(unitNetAmount);
														oDPmpprDto.setRate(itemDto.getUnitNetAmount());
														oDPmpprDto.setPackageConsumptionMasterId(-1);*/
//														itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
														itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto,null,'Y',unitNetAmount,itemDto.getUnitNetAmount(),false,1,dto.getPharmacyReturnServiceRecal()));
												/**		itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
														itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
														itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
														unitNetAmount = 0;*/
													} else {
														List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
													/**	SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
														oDPmpprDto.setPackageId(resDto.getPackageId());
														oDPmpprDto.setIsPackageItem('Y');
														oDPmpprDto.setPackageName(resDto.getPackageName());
														oDPmpprDto.setPackageCode(resDto.getPackageCode());
														oDPmpprDto.setPackageQty(1);
														oDPmpprDto.setCapOrQty(1);
														oDPmpprDto.setPackageTypeId(resDto.getPackageTypeId());
														oDPmpprDto.setPackageAmount(unitNetAmount);
														oDPmpprDto.setRate(itemDto.getUnitNetAmount());
														oDPmpprDto.setPackageConsumptionMasterId(-1);*/
//														itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
														mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto,null,'Y',unitNetAmount,itemDto.getUnitNetAmount(),false,1,dto.getPharmacyReturnServiceRecal()));
														itemDto.setSaleDetailPackageMapperDtoList(mppr);
													/**	itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
														itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
														itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
														unitNetAmount = 0;*/
													}
													itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
													itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
													itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
													itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
													unitNetAmount = 0;
												}
												// }
											}
										}
									}
									break;
								case 5: // multi encounter package

									if(unitNetAmount>0)
									{
//										if (orderDate.compareTo(validityFrom) >= 0 && orderDate.compareTo(validityTo) <= 0)
										if (isDateValid) {
											if (resDto.getListPackageConsumptionServiceDetailDto() != null && !resDto.getListPackageConsumptionServiceDetailDto().isEmpty()) {
												ListIterator<PackageConsumptionServiceDetailDto> mresSrvItr = resDto.getListPackageConsumptionServiceDetailDto().listIterator();
												PackageConsumptionServiceDetailDto srv = null;
												while (mresSrvItr.hasNext()) {
													srv = mresSrvItr.next();
													if (unitNetAmount > 0) {
														if (srv.getItemId() != null && srv.getAvailableCap() > 0 && srv.getBalanceQuantity() == null && srv.getItemId().equals(itemId) && itemDto.getSelfPayable().doubleValue() > 0) {
															//for (int qty =1 ; qty <=quantity ; qty++){
															if (unitNetAmount <= srv.getAvailableCap()) {
																double capAmount = srv.getAvailableCap() - unitNetAmount;
																srv.setAvailableCap(capAmount);
																if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
//																	double capAmount = srv.getAvailableCap() - unitNetAmount;
//																	srv.setAvailableCap(capAmount);
																/**	SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
																	oDPmpprDto.setPackageId(resDto.getPackageId());
																	oDPmpprDto.setIsPackageItem('Y');
																	oDPmpprDto.setPackageName(resDto.getPackageName());
																	oDPmpprDto.setPackageCode(resDto.getPackageCode());
																	oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
																	oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
																	oDPmpprDto.setPackageAmount(unitNetAmount);
																	oDPmpprDto.setPackageQty(1);
																	oDPmpprDto.setCapOrQty(1);
																	oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																	itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
																	itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto,srv,'Y',unitNetAmount,itemDto.getUnitNetAmount(),dto.getConsume(),1,dto.getPharmacyReturnServiceRecal()));
															/**		if (dto.getConsume()) {
																		iTPackageConsumptionCapDetailsDao.updatePackageCapItem(srv.getPackageConsumptionServiceDetailId(), (unitNetAmount * -1), false);
																	}
																	itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
																	itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
																	itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
																	unitNetAmount = 0;*/
																} else {
//																	double capAmount = srv.getAvailableCap() - unitNetAmount;
//																	srv.setAvailableCap(capAmount);
																	List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
														/**			SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
																	oDPmpprDto.setPackageId(resDto.getPackageId());
																	oDPmpprDto.setIsPackageItem('Y');
																	oDPmpprDto.setPackageName(resDto.getPackageName());
																	oDPmpprDto.setPackageCode(resDto.getPackageCode());
																	oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
																	oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
																	oDPmpprDto.setPackageAmount(unitNetAmount);
																	oDPmpprDto.setPackageQty(1);
																	oDPmpprDto.setCapOrQty(1);
																	oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																	itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
																	mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto,srv,'Y',unitNetAmount,itemDto.getUnitNetAmount(),dto.getConsume(),1,dto.getPharmacyReturnServiceRecal()));
																	itemDto.setSaleDetailPackageMapperDtoList(mppr);
																/**	if (dto.getConsume()) {
																		iTPackageConsumptionCapDetailsDao.updatePackageCapItem(srv.getPackageConsumptionServiceDetailId(), (unitNetAmount * -1), false);
																	}
																	itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
																	itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
																	itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
																	unitNetAmount = 0;*/
																}
																itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
																itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
																itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
																itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
																unitNetAmount = 0;
															} else {
																double rate = unitNetAmount - srv.getAvailableCap();
																if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
//																	double rate = unitNetAmount - srv.getAvailableCap();
															/**		SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
																	oDPmpprDto.setPackageId(resDto.getPackageId());
																	oDPmpprDto.setIsPackageItem('Y');
																	oDPmpprDto.setPackageName(resDto.getPackageName());
																	oDPmpprDto.setPackageCode(resDto.getPackageCode());
																	oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
																	oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
																	oDPmpprDto.setPackageAmount(srv.getAvailableCap());
																	oDPmpprDto.setPackageQty(1);
																	oDPmpprDto.setCapOrQty(1);
																	oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
																	itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + srv.getAvailableCap());
																	itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto,srv,'Y',srv.getAvailableCap(),itemDto.getUnitNetAmount(),dto.getConsume(),1,dto.getPharmacyReturnServiceRecal()));
															/**		if (dto.getConsume()) {
																		iTPackageConsumptionCapDetailsDao.updatePackageCapItem(srv.getPackageConsumptionServiceDetailId(), (srv.getAvailableCap() * -1), false);
																	}
																	itemDto.setSelfPayable(itemDto.getSelfPayable() - srv.getAvailableCap());
																	itemDto.setNetAmount(itemDto.getNetAmount() - srv.getAvailableCap());
																	unitNetAmount = rate;
																	itemDto.setApportionedRate(rate);
																	srv.setAvailableCap(0.00);*/
																} else {
//																	double rate = unitNetAmount - srv.getAvailableCap();
																	List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
														/**		SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
																	oDPmpprDto.setPackageId(resDto.getPackageId());
																	oDPmpprDto.setIsPackageItem('Y');
																	oDPmpprDto.setPackageName(resDto.getPackageName());
																	oDPmpprDto.setPackageCode(resDto.getPackageCode());
																	oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
																	oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
																	oDPmpprDto.setPackageAmount(srv.getAvailableCap());
																	oDPmpprDto.setPackageQty(1);
																	oDPmpprDto.setCapOrQty(1);
																	oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																	itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + srv.getAvailableCap());
																	mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto,srv,'Y',srv.getAvailableCap(),itemDto.getUnitNetAmount(),dto.getConsume(),1,dto.getPharmacyReturnServiceRecal()));
																	itemDto.setSaleDetailPackageMapperDtoList(mppr);
																	/**			if (dto.getConsume()) {
																		iTPackageConsumptionCapDetailsDao.updatePackageCapItem(srv.getPackageConsumptionServiceDetailId(), (srv.getAvailableCap() * -1), false);
																	}
																	itemDto.setSelfPayable(itemDto.getSelfPayable() - srv.getAvailableCap());
																	itemDto.setNetAmount(itemDto.getNetAmount() - srv.getAvailableCap());
																	unitNetAmount = rate;
																	itemDto.setApportionedRate(rate);
																	srv.setAvailableCap(0.00);*/
																}
																itemDto.setSelfPayable(itemDto.getSelfPayable() - srv.getAvailableCap());
																itemDto.setNetAmount(itemDto.getNetAmount() - srv.getAvailableCap());
																unitNetAmount = rate;
																itemDto.setApportionedRate(rate);
																srv.setAvailableCap(0.00);
															}
															//qty close}
														}//
														else if (srv.getItemId() != null && srv.getAvailableCap() > 0 && srv.getBalanceQuantity() != null && srv.getBalanceQuantity() > 0 && srv.getItemId().equals(itemId) && itemDto.getSelfPayable().doubleValue() > 0) {
														//	if (srv.getBalanceQuantity() > 0 && srv.getAvailableCap() > 0) { // not required
																if (unitNetAmount <= srv.getAvailableCap()) {
																	double capAmount = srv.getAvailableCap() - unitNetAmount;
																	srv.setAvailableCap(capAmount);
																	srv.setBalanceQuantity(srv.getBalanceQuantity() - 1);
																	if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
//																		double capAmount = srv.getAvailableCap() - unitNetAmount;
//																		srv.setAvailableCap(capAmount);
//																		srv.setBalanceQuantity(srv.getBalanceQuantity() - 1);
																		/**        SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
																		 oDPmpprDto.setPackageId(resDto.getPackageId());
																		 oDPmpprDto.setIsPackageItem('Y');
																		 oDPmpprDto.setPackageName(resDto.getPackageName());
																		 oDPmpprDto.setPackageCode(resDto.getPackageCode());
																		 oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
																		 oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
																		 oDPmpprDto.setPackageAmount(unitNetAmount);
																		 oDPmpprDto.setPackageQty(1);
																		 oDPmpprDto.setCapOrQty(2);
																		 oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																		itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
																		itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', unitNetAmount, itemDto.getUnitNetAmount(), dto.getConsume(), 2,dto.getPharmacyReturnServiceRecal()));
																		/**            if (dto.getConsume()) {
																		 iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapItem(srv.getPackageConsumptionServiceDetailId(), (1 * -1), (unitNetAmount * -1), false);
																		 }
																		 itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
																		 itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
																		 itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
																		 unitNetAmount = 0;*/
																	} else {
//																		double capAmount = srv.getAvailableCap() - unitNetAmount;
//																		srv.setAvailableCap(capAmount);
//																		srv.setBalanceQuantity(srv.getBalanceQuantity() - 1);
																		List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
																		/**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
																		 oDPmpprDto.setPackageId(resDto.getPackageId());
																		 oDPmpprDto.setIsPackageItem('Y');
																		 oDPmpprDto.setPackageName(resDto.getPackageName());
																		 oDPmpprDto.setPackageCode(resDto.getPackageCode());
																		 oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
																		 oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
																		 oDPmpprDto.setPackageAmount(unitNetAmount);
																		 oDPmpprDto.setPackageQty(1);
																		 oDPmpprDto.setCapOrQty(2);
																		 oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
//																		itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
																		mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', unitNetAmount, itemDto.getUnitNetAmount(), dto.getConsume(), 2,dto.getPharmacyReturnServiceRecal()));
																		itemDto.setSaleDetailPackageMapperDtoList(mppr);
																		/**        if (dto.getConsume()) {
																		 iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapItem(srv.getPackageConsumptionServiceDetailId(), (1 * -1), (unitNetAmount * -1), false);
																		 }
																		 itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
																		 itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
																		 itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
																		 unitNetAmount = 0;*/
																	}
																	itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + unitNetAmount);
																	itemDto.setSelfPayable(itemDto.getSelfPayable() - unitNetAmount);
																	itemDto.setNetAmount(itemDto.getNetAmount() - unitNetAmount);
																	itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
																	unitNetAmount = 0;
																} else {
																	double rate = unitNetAmount - srv.getAvailableCap();
																	if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
//																		double rate = unitNetAmount - srv.getAvailableCap();
																		/**    SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
																		 oDPmpprDto.setPackageId(resDto.getPackageId());
																		 oDPmpprDto.setIsPackageItem('Y');
																		 oDPmpprDto.setPackageName(resDto.getPackageName());
																		 oDPmpprDto.setPackageCode(resDto.getPackageCode());
																		 oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
																		 oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
																		 oDPmpprDto.setPackageAmount(srv.getAvailableCap());
																		 oDPmpprDto.setPackageQty(1);
																		 oDPmpprDto.setCapOrQty(2);
																		 oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
																		itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + srv.getAvailableCap());
																		itemDto.getSaleDetailPackageMapperDtoList().add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', srv.getAvailableCap(), itemDto.getUnitNetAmount(), dto.getConsume(), 2,dto.getPharmacyReturnServiceRecal()));
																		/**    if (dto.getConsume()) {
																		 iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapItem(srv.getPackageConsumptionServiceDetailId(), (1 * -1), (srv.getAvailableCap() * -1), false);
																		 }
																		 itemDto.setSelfPayable(itemDto.getSelfPayable() - srv.getAvailableCap());
																		 itemDto.setNetAmount(itemDto.getNetAmount() - srv.getAvailableCap());
																		 unitNetAmount = rate;
																		 itemDto.setApportionedRate(rate);
																		 srv.setAvailableCap(0.00);
																		 srv.setBalanceQuantity(srv.getBalanceQuantity() - 1);*/
																	} else {
//																		double rate = unitNetAmount - srv.getAvailableCap();
																		List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
																		/**            SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
																		 oDPmpprDto.setPackageId(resDto.getPackageId());
																		 oDPmpprDto.setIsPackageItem('Y');
																		 oDPmpprDto.setPackageName(resDto.getPackageName());
																		 oDPmpprDto.setPackageCode(resDto.getPackageCode());
																		 oDPmpprDto.setPackageConsumptionMasterId(srv.getPackageConsumptionMasterId());
																		 oDPmpprDto.setPacakgeConsumptionServiceDetailId(srv.getPackageConsumptionServiceDetailId());
																		 oDPmpprDto.setPackageAmount(srv.getAvailableCap());
																		 oDPmpprDto.setPackageQty(1);
																		 oDPmpprDto.setCapOrQty(2);
																		 oDPmpprDto.setRate(itemDto.getUnitNetAmount());*/
																		itemDto.setPackageConsumptionAmt(itemDto.getPackageConsumptionAmt() + srv.getAvailableCap());
																		mppr.add(billingCommonMethods.createSaleDetailPackageMapperDto(resDto, srv, 'Y', srv.getAvailableCap(), itemDto.getUnitNetAmount(), dto.getConsume(), 2,dto.getPharmacyReturnServiceRecal()));
																		itemDto.setSaleDetailPackageMapperDtoList(mppr);
																		/**        if (dto.getConsume()) {
																		 iTPackageConsumptionCapDetailsDao.updatePackageQuantityCapItem(srv.getPackageConsumptionServiceDetailId(), (1 * -1), (srv.getAvailableCap() * -1), false);
																		 }
																		 itemDto.setSelfPayable(itemDto.getSelfPayable() - srv.getAvailableCap());
																		 itemDto.setNetAmount(itemDto.getNetAmount() - srv.getAvailableCap());
																		 unitNetAmount = rate;
																		 itemDto.setApportionedRate(rate);
																		 srv.setAvailableCap(0.00);
																		 srv.setBalanceQuantity(srv.getBalanceQuantity() - 1);*/
																	}
																	itemDto.setSelfPayable(itemDto.getSelfPayable() - srv.getAvailableCap());
																	itemDto.setNetAmount(itemDto.getNetAmount() - srv.getAvailableCap());
																	unitNetAmount = rate;
																	itemDto.setApportionedRate(rate);
																	srv.setAvailableCap(0.00);
																	srv.setBalanceQuantity(srv.getBalanceQuantity() - 1);
																}
														//	}
														}
													}
												}
											}
										}
									}
									break;
								  }
							    }//validity compare
							  }//quantity loop
						    }
					    }
			     }
			}
			return new Response<>(SUCCESS, SUCCESS_CODE,null,null, dto);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	@Override
	@Transactional
	public Response consumeItemFromPackageContract(PharmacyBillCommonDto dto, Integer pharmacyRec) throws Exception {
		try {

			if(pharmacyRec!= null) {
				dto.setPharmacyReturnServiceRecal(pharmacyRec);
			}
			Response packageItemRes = this.consumeItemFromPackage(dto);
			if(packageItemRes.getObject()!=null) {
				PharmacyBillCommonDto pkgContractDto = (PharmacyBillCommonDto) packageItemRes.getObject();
				for (CommonBillingItemsDto itemDto : pkgContractDto.getItemList()) {
					int selQty = itemDto.getSelfQuantity();
					for (int qty = 1; qty <= selQty; qty++) {
						if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty()) {
							SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
							oDPmpprDto.setIsPackageItem(dto.getForceAddRemoved().intValue() == 1 ? 'Y' : 'N');
							oDPmpprDto.setPackageAmount(dto.getForceAddRemoved().intValue() == 1 ? itemDto.getRate() : 0.00);
							if (itemDto.getApportionedRate() > 0) {
								oDPmpprDto.setRate(itemDto.getApportionedRate());
								itemDto.setContractRate(itemDto.getApportionedRate());
								itemDto.setApportionedRate(0);
							} else {
								oDPmpprDto.setRate(itemDto.getUnitNetAmount());
							}
							oDPmpprDto.setPackageQty(1);
							itemDto.getSaleDetailPackageMapperDtoList().add(oDPmpprDto);
						} else {
							List<SaleDetailPackageMapperDto> mppr = new ArrayList<>();
							SaleDetailPackageMapperDto oDPmpprDto = new SaleDetailPackageMapperDto();
							oDPmpprDto.setIsPackageItem(dto.getForceAddRemoved().intValue() == 1 ? 'Y' : 'N');
							oDPmpprDto.setPackageAmount(dto.getForceAddRemoved().intValue() == 1 ? itemDto.getUnitNetAmount() : 0.00);
							if (itemDto.getApportionedRate() > 0) {
								oDPmpprDto.setRate(itemDto.getApportionedRate());
								itemDto.setApportionedRate(0);
							} else {
								oDPmpprDto.setRate(itemDto.getUnitNetAmount());
							}
							oDPmpprDto.setPackageQty(1);
							if (dto.getForceAddRemoved().intValue() == 1) {
								itemDto.setSelfPayable(0.00);
								itemDto.setPackageConsumptionAmt(itemDto.getNetAmount());
								itemDto.setSelfQuantity(itemDto.getSelfQuantity() - 1);
								oDPmpprDto.setPackageId(dto.getForceAddPackageId());
							}
							mppr.add(oDPmpprDto);
							itemDto.setSaleDetailPackageMapperDtoList(mppr);
						}
					}
					if (itemDto.getSaleDetailPackageMapperDtoList() != null && !itemDto.getSaleDetailPackageMapperDtoList().isEmpty() && itemDto.getNetAmount() > 0) {
						Double itemNet = itemDto.getNetAmount();
						Double amt = 0.00;
						Double diff = 0.00;
						int i = 1;
						for (SaleDetailPackageMapperDto mpprDto : itemDto.getSaleDetailPackageMapperDtoList()) {
							amt = amt + mpprDto.getPackageAmount().doubleValue();
							if (i == itemDto.getSaleDetailPackageMapperDtoList().size() && mpprDto.getPackageAmount().doubleValue() > 0) {
								diff = amt - itemNet;
								mpprDto.setPackageAmount(mpprDto.getPackageAmount() - diff);
							}
							i++;
						}
					}
				}
				return iWalkInSalesService.consumeItemFromContract(pkgContractDto);
			}
			return new Response<>(ERROR, ERROR_CODE,null,null, dto);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	@Override
	@Transactional
	public Response renderPackageServiceFrPostBillig(OrderDetailsMaster dto,
			AdmissionsForBatchProcessDto vistDetailsObj, Integer packageId) throws ApplicationException {
		try {
			    iMPackageMasterDao.renderPackageServiceFrPostBillig(dto, vistDetailsObj,packageId);
			return new Response<>(ERROR, ERROR_CODE,null,null, null);
		}catch (Exception e) {

			throw e;
		}
	}

	@Override
	@Transactional
	public Response getPackageDetailsByIdForBillingTransaction(Map<Integer, Integer> consumtionIdPackageIds)
			throws Exception {
		try {
			// get package details
			List<PackageListForOpBillResDto> listPackageListForOpBillResDto = new LinkedList<PackageListForOpBillResDto>();
			PackageListForOpBillResDto mPackage = null;
			Response packageServRes = null;
			Response packageCapRes = null;
			for (Map.Entry<Integer, Integer> entry : consumtionIdPackageIds.entrySet()) {
				Response packRes = iMPackageMasterDao.getPackageById(entry.getValue());
				if (packRes.getCode().equals(SUCCESS_CODE) && packRes.getObject() != null) {
					MPackageMasterDto mPackageDto = (MPackageMasterDto) packRes.getObject();
					switch (mPackageDto.getPackageTypeId()) {
					case 1: // EHC package
						mPackage = new PackageListForOpBillResDto();
						mPackage.setmPackageMasterDto(mPackageDto);

						// get service details
						packageServRes = iTPackageServicesDetailsDao.getPackageServicesFrTransactionByPackageId(entry.getValue(),entry.getKey());
						mPackage.setListTPackageServicesDetailsDto(packageServRes.getListObject());
						listPackageListForOpBillResDto.add(mPackage);
						break;
					case 2: // Either or Package
						mPackage = new PackageListForOpBillResDto();
						mPackage.setmPackageMasterDto(mPackageDto);

						// get eo details
						/*packageEoRes = iTPackageEitherOrGroupDetailsDao
								.getPackageEitherOrGroupDetailsByPackageId(entry.getValue());
						mPackage.setListTPackageEitherorGroupDetailsDto(packageEoRes.getListObject());*/

						/*// get service details
						packageServRes = iTPackageServicesDetailsDao.getPackageServicesByPackageId(entry.getValue());
						mPackage.setListTPackageServicesDetailsDto(packageServRes.getListObject());
						listPackageListForOpBillResDto.add(mPackage);*/

						// get service details
						packageServRes = iTPackageServicesDetailsDao.getPackageServicesFrTransactionByPackageId(entry.getValue(),entry.getKey());
						mPackage.setListTPackageServicesDetailsDto(packageServRes.getListObject());
						listPackageListForOpBillResDto.add(mPackage);

					/*	Response serResponse = iTPackageServicesDetailsDao
								.getPackageServicesByPackageId(pacakgeDto.getPackageMasterId());
						pacakgeDto.setListTPackageServicesDetailsDto(serResponse.getListObject());*/

						Response eitherOrGrpRes = iTPackageEitherOrGroupDetailsDao.getPackageEitherOrGroupDetailsByConsumptionIdForTransaction(entry.getKey());
						if (eitherOrGrpRes.getCode().equalsIgnoreCase(SUCCESS_CODE)&& eitherOrGrpRes.getListObject() != null)
						 {
							List<TPackageEitherorGroupDetailsDto> eihterOrGrpDtlsList = eitherOrGrpRes.getListObject();
							for (TPackageEitherorGroupDetailsDto eihterOrGrpDtls : eihterOrGrpDtlsList)
							 {
								Response eitherOrServiceRes = iTPackageServicesDetailsDao
										.getPackageEitherOrServicesByGroupIdForTransaction(entry.getValue(),entry.getKey(),eihterOrGrpDtls.getPackageGroupDetailsId());
								eihterOrGrpDtls.setListTPackageServicesDetailsDto(eitherOrServiceRes.getListObject());
							 }

							mPackage.setListTPackageEitherorGroupDetailsDto(eihterOrGrpDtlsList);
						}

						break;
					case 3: // package with cap
						mPackage = new PackageListForOpBillResDto();
						mPackage.setmPackageMasterDto(mPackageDto);

						// get service details
						/*packageServRes = iTPackageServicesDetailsDao.getPackageServicesByPackageId(entry.getValue());
						mPackage.setListTPackageServicesDetailsDto(packageServRes.getListObject());
						listPackageListForOpBillResDto.add(mPackage);*/
						packageServRes = iTPackageServicesDetailsDao.getPackageServicesFrTransactionByPackageId(entry.getValue(),entry.getKey());
						mPackage.setListTPackageServicesDetailsDto(packageServRes.getListObject());
						listPackageListForOpBillResDto.add(mPackage);

						// get cap details
						/*packageCapRes = iTPackageCapDetailsDao.getPackageCapDetailsByPackageIdForBilling(entry.getValue());
						mPackage.setListTPackageGroupWiseCapDetailsDto(packageCapRes.getListObject());*/

						packageCapRes = iTPackageCapDetailsDao.getPackageCapDetailsByPackageIdForTransaction(entry.getKey());
						mPackage.setListTPackageGroupWiseCapDetailsDto(packageCapRes.getListObject());

						// get inclusion exclusion details
						/*packageIncExcRes = iTPackageIncExcDetailsDao
								.getTPackageIncExcDetailsForBillingByPackage(entry.getValue());
						mPackage.setListTPackageIncExcDetailsDto(packageIncExcRes.getListObject());*/


						Response betCatogoryRes = iTPackageBedCategoryDetailDao.getTPackageBedCategoryDetailBilling(entry.getKey());
						mPackage.setListTPackageBedCategoryDetailDto(betCatogoryRes.getListObject());

						break;
					case 4: // All exclusive package
						mPackage = new PackageListForOpBillResDto();
						mPackage.setmPackageMasterDto(mPackageDto);

						// get service details
						packageServRes = iTPackageServicesDetailsDao.getPackageServicesFrTransactionByPackageId(entry.getValue(),entry.getKey());
						mPackage.setListTPackageServicesDetailsDto(packageServRes.getListObject());

						/*// get inclusion exclusion details
						Response incExcRes = iTPackageIncExcDetailsDao
								.getTPackageIncExcDetailsForBillingByPackage(entry.getValue());
						mPackage.setListTPackageIncExcDetailsDto(incExcRes.getListObject());
*/
						listPackageListForOpBillResDto.add(mPackage);
						break;
					case 5: // multi encounter package
						mPackage = new PackageListForOpBillResDto();
						mPackage.setmPackageMasterDto(mPackageDto);

						// get service details
//						packageServRes = iTPackageServicesDetailsDao.getPackageServicesByPackageId(entry.getValue());
						packageServRes = iTPackageServicesDetailsDao.getPackageServicesFrTransactionByPackageId(entry.getValue(),entry.getKey());
						mPackage.setListTPackageServicesDetailsDto(packageServRes.getListObject());
						listPackageListForOpBillResDto.add(mPackage);
						break;
					}

				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, null, listPackageListForOpBillResDto, null);
		} catch (ServiceException se) {
			throw se;
		} catch (Exception e) {

			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response forceCancelPackage(PackageConsumptionMasterResDto reqDto) throws Exception {
			try {
			      return iMPackageMasterDao.forceCancelPackage(reqDto);
		}catch (Exception e) {

			throw e;
		}
	}

	@Override
	@Transactional
	public Response inactivePackage(Integer parentPkgId, Integer packageTypeId, boolean flag) throws Exception {
			try {
			      return iMPackageMasterDao.inactivePacakge(parentPkgId,packageTypeId,flag,0);
		}catch (Exception e) {

			throw e;
		}
	}

	@Override
	@Transactional
	public Response inactiveParentPackage(Integer parentPkgId, boolean flag) throws Exception {
		try {
		      return iMPackageMasterDao.inactiveParentPackage(parentPkgId,flag);
	}catch (Exception e) {

		throw e;
	}
	}

	@Override
	@Transactional
	public Response getPackageListForIPDPackageOrderForBookView(Integer patientId, String fromDate, String toDate)
			throws Exception {
		try {
			Response res= iMPackageMasterDao.getPackageListForIPDPackageOrderForBookView(patientId,fromDate,toDate);
			List<PackageConsumptionMasterResDto> resList=new ArrayList<>();
			if(res.getListObject()!=null && res.getListObject().size()>0) {
				Iterator<PackageConsumptionMasterResDto> itr=res.getListObject().iterator();
				while(itr.hasNext()) {
					PackageConsumptionMasterResDto dto=itr.next();
					Date validityTo = GlobalCommonDateUtils.getDate(dto.getValidityTo(), "dd-M-yyyy");
					Date currentDate=GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy");
					if(currentDate.compareTo(validityTo) <= 0) {
						resList.add(dto);
					}
				}
			}
			res.setListObject(resList);
			return res;
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}

	@Override
	@Transactional
	public Response getAllInclusivePackageConsumeAmount(BillingMasterDto bdto) throws Exception {
		try {
		      return iMPackageMasterDao.getAllInclusivePackageConsumeAmount(bdto);
	}catch (Exception e) {

		throw e;
	}
	}

	@Override
	@Transactional
	public Response getAllInclusivePackageAmount(BillingMasterDto bdto) throws Exception {
		try {
		      return iMPackageMasterDao.getAllInclusivePackageAmount(bdto);
	}catch (Exception e) {

		throw e;
	}
	}

	@Override
	public Response getMPackageForIPDCoversheetByDate(PackageConsumptionMasterResDto dto) throws ApplicationException {
		try{
			List<PackageConsumptionMasterResDto> resDtoList = new ArrayList<>();

			for(String date : dto.getDates()){
				List<PackageConsumptionMasterResDto> listPackageOrder = new ArrayList<>();
				PackageConsumptionMasterResDto resDto = new PackageConsumptionMasterResDto();

				resDto.setAddedDate(date);
				if(dto.getListPackageOrder() != null && dto.getListPackageOrder().size()>0){
					for (PackageConsumptionMasterResDto item : dto.getListPackageOrder()) {

						if (date.equals(item.getCreatedDate())) {
							item.setAddedDate(date);
							listPackageOrder.add(item);
						}
					}
					resDto.setListPackageOrder(listPackageOrder);
				}
				resDtoList.add(resDto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, resDtoList, null);
		}
		catch (Exception e){
		logger.error("getMPackageForIPDCoversheetByDate failed", e);
		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getUniqueDateForIPDCoversheet(PackageConsumptionMasterResDto dto) throws ApplicationException {
		try{
			List<String>distinctDates = new ArrayList<>();
			PackageConsumptionMasterResDto resDto = dto;

			if(dto.getListPackageOrder() != null && dto.getListPackageOrder().size()>0){
				for (PackageConsumptionMasterResDto complaint : dto.getListPackageOrder()) {

					if (!distinctDates.contains(complaint.getCreatedDate())) {
						distinctDates.add(complaint.getCreatedDate());
					}
				}

				if(distinctDates != null && distinctDates.size()>0){
					Collections.sort(distinctDates, Comparator.reverseOrder());
					resDto.setDates(distinctDates);
					return getMPackageForIPDCoversheetByDate(resDto);
				}

			}
			else{
				return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
			}
		}
		catch (Exception e){
		logger.error("getUniqueDateForIPDCoversheet failed", e);
		}

		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Transactional
	@Override
	public Response getPackageListOnCreditNote(Integer patientId, Integer admissionId, Integer orgId, Integer unitId,
			Integer visitTypeId) {

		try {
			return iMPackageMasterDao.getPackageListOnCreditNote(patientId,admissionId, orgId, unitId,visitTypeId);
			
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}


	}
	@Transactional
	@Override
	public Response packageListForWeb(PackageListReqDto reqDto) throws Exception {
		try {
			return iMPackageMasterDao.packageListForWeb(reqDto);

		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}

	}


}
