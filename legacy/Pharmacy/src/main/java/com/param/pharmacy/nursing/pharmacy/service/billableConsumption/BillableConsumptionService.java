package com.param.pharmacy.nursing.pharmacy.service.billableConsumption;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.BatchUomMapper;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.BillableConsumption;
import com.param.entity.model.pharmacy.BillableConsumptionDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.SaleDetailPayeeMapper;
import com.param.entity.model.pharmacy.SaleType;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.common.Response;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dao.IVisitTypeMasterDao;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.pharmacy.dto.BillableConsumptionSave;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.global.IGlobalService;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.pharmacy.nursing.pharmacy.dao.billableConsumption.IBillableConsumptionDao;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionDetailItem;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionDetails;
//import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionItem;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionRequest;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionResponse;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
//import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionSave;
//import com.param.pharmacy.op.dto.walkInSales.SaleDetailPayeeMapperDto;
import com.param.service.global.SessionService;
import com.param.service.global.model.SaleDetailPackageMapper;
import com.param.utility.TimeZoneComponent;

@SuppressWarnings("unused")
@Service("billableConsumptionServiceService")
@Transactional
public class BillableConsumptionService implements IBillableConsumptionService {

	private static final Logger logger = Logger.getLogger(BillableConsumptionService.class);

	@Autowired
	private IBillableConsumptionDao billableConsumptionDao;

	@Autowired
	IOrderMasterService iOrderMasterService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IVisitTypeMasterDao visitTypeMasterDao;

	@Autowired
	IMPackageMasterService iMPackageMasterService;

	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao;

	@Autowired
	IWalkInSalesService iWalkInSalesService;

	@Autowired
	IGoodReceiptNoteDao grnDao;

	private Integer screenId = Screen.BillableConsumption.getScreen();

	
	private Integer lotNoScreenId = Screen.LotNoCreation.getScreen();

	@Override
	@Transactional
	public CommonDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId) {
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (WardMasters wardMaster : GlobalDao.getWardList(unitId)) {
			response.addWard(new com.param.global.dto.global.Ward(wardMaster.getWardId(), wardMaster.getWardName()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));

		}

		/*
		 * for(Store store : GlobalDao.getStoreListByUser()) { response.addStore(new
		 * com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		 * }
		 */

		return response;
	}

	@Override
	@Transactional
	public List<BillableConsumptionResponse> billableConsumptionSearch(BillableConsumptionRequest request) {
		List<BillableConsumptionResponse> billableConsumptionResponseList = new ArrayList<BillableConsumptionResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		for (BillableConsumption billableConsumption : billableConsumptionDao.billableConsumptionSearch(request,
				screenId)) {
			Integer age = Util
					.getAgeByBirthDate(billableConsumption.getPatient().getDob().toLocalDateTime().toLocalDate());

//			String patientDetail = billableConsumption.getPatient().getPrefix().getPrefixCode() + " "
//					+ billableConsumption.getPatient().getFirstName() + " "
//					+ billableConsumption.getPatient().getMiddleName() + " "
//					+ billableConsumption.getPatient().getLastName() + " | " + age + " | "
//					+ billableConsumption.getPatient().getGender().getGenderCode();
			String patientDetail = billableConsumption.getPatient().getPrefix().getPrefixCode() + " "
					+ billableConsumption.getPatient().getFirstName() + " "
					+ billableConsumption.getPatient().getMiddleName() + " "
					+ billableConsumption.getPatient().getLastName();

			BillableConsumptionResponse billableConsumptionResponse = new BillableConsumptionResponse(
					billableConsumption.getId(), billableConsumption.getConsumableNumber(),
					billableConsumption.getAddedDate(), billableConsumption.getVisitType().getVisitTypeCode(),
					billableConsumption.getStore().getStoreName(), billableConsumption.getPatient().getUhidNumber(),
					patientDetail, age, billableConsumption.getPatient().getGender().getGenderCode(),
					billableConsumption.getPatient().getImagePath(), billableConsumption.getAddedBy().getUserName(),
					(billableConsumption.getWard() != null) ? billableConsumption.getWard().getWardName() : null,
					(billableConsumption.getBed() != null) ? billableConsumption.getBed().getBedNumber() : null);
			billableConsumptionResponse.setStatusId(billableConsumption.getStatus().getId());
			billableConsumptionResponse.setStatus(billableConsumption.getStatus().getStatus());
			billableConsumptionResponse.setPatientId(billableConsumption.getPatient().getPatientId());
			billableConsumptionResponseList.add(billableConsumptionResponse);
		}
		return billableConsumptionResponseList;
	}

	@Override
	@Transactional
	public BillableConsumptionDetails billableConsumptionDetails(Integer id, Integer unitId) {
		BillableConsumptionDetails billableConsumptionDetails = new BillableConsumptionDetails();
		BillableConsumption billableConsumption = billableConsumptionDao.billableConsumptionById(id, unitId);
		Integer age = Util.getAgeByBirthDate(billableConsumption.getPatient().getDob().toLocalDateTime().toLocalDate());
		String patientDetail = billableConsumption.getPatient().getFirstName() + " "
				+ billableConsumption.getPatient().getMiddleName() + " "
				+ billableConsumption.getPatient().getLastName() + " | " + age + " | "
				+ billableConsumption.getPatient().getGender().getGenderCode();

		List<PatientSponsorDetails> patientSponsorDetails = null;
		if (billableConsumption.getAdmission() != null) {
			patientSponsorDetails = iPatientSponsorDetailsDao
					.findByAdmissionId(billableConsumption.getAdmission().getAdmissionId());

			billableConsumptionDetails = new BillableConsumptionDetails(billableConsumption.getId(),
					billableConsumption.getConsumableNumber(), billableConsumption.getAddedDate(),
					billableConsumption.getStore().getStoreName(),
					billableConsumption.getVisitType().getVisitTypeCode(),
					billableConsumption.getPatient().getUhidNumber(), patientDetail,
					billableConsumption.getAdmission().getAdmissionNumber());
			billableConsumptionDetails.setWardName(
					(billableConsumption.getWard() != null) ? billableConsumption.getWard().getWardName() : null);
			billableConsumptionDetails.setBedNo(
					(billableConsumption.getBed() != null) ? billableConsumption.getBed().getBedNumber() : null);
		} else if (billableConsumption.getEncounter() != null) {
			patientSponsorDetails = iPatientSponsorDetailsDao
					.findByEncounterId(billableConsumption.getEncounter().getEncounterId());
			billableConsumptionDetails = new BillableConsumptionDetails(billableConsumption.getId(),
					billableConsumption.getConsumableNumber(), billableConsumption.getAddedDate(),
					billableConsumption.getStore().getStoreName(),
					billableConsumption.getVisitType().getVisitTypeCode(),
					billableConsumption.getPatient().getUhidNumber(), patientDetail,
					billableConsumption.getEncounter().getEncounterNumber());
		}
		List<BillableConsumptionDetailItem> items = new ArrayList<BillableConsumptionDetailItem>();
		for (BillableConsumptionDetail billableConsumptionDetail : billableConsumptionDao
				.billableConsumptionDetailById(id, unitId)) {
			if (billableConsumptionDetail.getBatch() != null) {
				BillableConsumptionDetailItem item = new BillableConsumptionDetailItem(
						billableConsumptionDetail.getId(),
						billableConsumptionDetail.getBatch() != null
								? billableConsumptionDetail.getBatch().getItem().getId()
								: null,
						billableConsumptionDetail.getBatch() != null
								? billableConsumptionDetail.getBatch().getItem().getCode()
								: null,
						billableConsumptionDetail.getBatch() != null
								? billableConsumptionDetail.getBatch().getItem().getName()
								: null,
						billableConsumptionDetail.getBatch() != null
								? billableConsumptionDetail.getBatch().getItem().getGeneric().getId()
								: null,
						billableConsumptionDetail.getBatch() != null
								? billableConsumptionDetail.getBatch().getItem().getGeneric().getName()
								: null,
						billableConsumptionDetail.getBatch() != null ? billableConsumptionDetail.getBatch().getId()
								: null,
						billableConsumptionDetail.getBatch() != null ? billableConsumptionDetail.getBatch().getBatchNo()
								: null,
						billableConsumptionDetail.getBatch() != null
								? billableConsumptionDetail.getBatch().getExpiryDate()
								: null,
						billableConsumptionDetail.getUomType() != null
								? billableConsumptionDetail.getUomType().getType()
								: null,
						billableConsumptionDetail.getUomUnit() != null
								? billableConsumptionDetail.getUomUnit().getUnits()
								: null,
						billableConsumptionDetail.getConsumableQuantity(),
						billableConsumptionDetail.getBatch() != null
								? billableConsumptionDetail.getBatch().getItem().getIsBatchRequired()
								: null,
						billableConsumptionDetail.getBatch().getItem().getIsThreeUOMApplicable(), 
						GlobalDao.getUnitOfMeasurementListByItem(billableConsumptionDetail.getBatch().getItem().getId(),billableConsumption.getOrganizationId()));
				items.add(item);

				if (patientSponsorDetails != null
						&& billableConsumptionDetail.getItem().getGeneric().getProductCategory() != null) {
					for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(billableConsumptionDetail.getItem().getGeneric().getProductCategory().getId());

						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
								sponsorDetail.getPaymentEntitlementId());
						if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							if (pharmacyTariffs.size() > 1) {
								// throw exception?
								// Don't throw - get first object and set data.
							}

							PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
							item.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							item.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
							break;
						}
					}

				}

			} else {
				BillableConsumptionDetailItem item = new BillableConsumptionDetailItem(
						billableConsumptionDetail.getId(), billableConsumptionDetail.getItem().getId(),
						billableConsumptionDetail.getItem().getCode(), billableConsumptionDetail.getItem().getName(),
						billableConsumptionDetail.getItem().getGeneric().getId(),
						billableConsumptionDetail.getItem().getGeneric().getName(),
						billableConsumptionDetail.getBatch() != null ? billableConsumptionDetail.getBatch().getId()
								: null,
						billableConsumptionDetail.getBatch() != null ? billableConsumptionDetail.getBatch().getBatchNo()
								: null,
						billableConsumptionDetail.getBatch() != null
								? billableConsumptionDetail.getBatch().getExpiryDate()
								: null,
						billableConsumptionDetail.getUomType() != null
								? billableConsumptionDetail.getUomType().getType()
								: null,
						billableConsumptionDetail.getUomUnit() != null
								? billableConsumptionDetail.getUomUnit().getUnits()
								: null,
						billableConsumptionDetail.getConsumableQuantity(),
						billableConsumptionDetail.getItem().getIsBatchRequired(),
						billableConsumptionDetail.getItem().getIsThreeUOMApplicable(),
						GlobalDao.getUnitOfMeasurementListByItem(billableConsumptionDetail.getBatch().getItem().getId(),billableConsumption.getOrganizationId()));
				items.add(item);

				if (patientSponsorDetails != null
						&& billableConsumptionDetail.getItem().getGeneric().getProductCategory() != null) {
					for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(billableConsumptionDetail.getItem().getGeneric().getProductCategory().getId());

						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
								sponsorDetail.getPaymentEntitlementId());
						if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							if (pharmacyTariffs.size() > 1) {
								// throw exception?
								// Don't throw - get first object and set data.
							}

							PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
							item.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							item.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
							break;
						}
					}

				}
			}

		}
		billableConsumptionDetails.setItems(items);
		return billableConsumptionDetails;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@Transactional
	public CommonResponse billableConsumptionSave(BillableConsumptionSave request) throws Exception {
		try {

			

			Double paidAmount = 0.0;
			Date date = TimeZoneComponent.getDateByzone(request.getUnitId());

			VisitType visitType = null;
			SaleType saleType = null;
			if (request.getVisitTypeId() != null)
				visitType = GlobalDao.getVisitType(request.getVisitTypeId());
			if (visitType != null) {
				if (visitType.getVisitTypeCode().equals("OP"))
					saleType = new SaleType(2);
				else if (visitType.getVisitTypeCode().equals("IP"))
					saleType = new SaleType(1);
				else if (visitType.getVisitTypeCode().equals("DC"))
					saleType = new SaleType(4);
				else if (visitType.getVisitTypeCode().equals("ER"))
					saleType = new SaleType(1);
				else
					throw new RuntimeException("Invalid visit type");
			}

			// Check Contract
			request.setOrganizationId(request.getOrganizationId());
			request.setUnitId(request.getUnitId());
			UnitApplicationConfigurationDto appConfigDto = iOrderMasterService.getPharmacyServiceId(request.getOrganizationId(),request.getUnitId());
			boolean taxInclusive = appConfigDto.getTaxInclusive();

			request.setTaxInclusive(taxInclusive);
			List itemList = request.getItems();
			request.setItemList(itemList);
			Response billableConsumptionRes = iMPackageMasterService.consumeItemFromPackageContract(request,0);
			BillableConsumptionSave billableConsumptionContract = (BillableConsumptionSave) billableConsumptionRes
					.getObject();

			Double totalSelfPayable = billableConsumptionContract.getItemList().stream()
					.filter(i -> i.getSelfPayable() != null).mapToDouble(i -> i.getSelfPayable()).sum();

			Double totalCreditPayable = billableConsumptionContract.getItemList().stream()
					.filter(i -> i.getCreditPayable() != null).mapToDouble(i -> i.getCreditPayable()).sum();

			Double totalNetPayable = billableConsumptionContract.getItemList().stream()
					.filter(i -> i.getNetAmount() != null).mapToDouble(i -> i.getNetAmount()).sum();

			Sale sale = new Sale(null, request.getDiscount(), null, null, null,
					(totalNetPayable != null) ? totalNetPayable : null,
					(totalNetPayable != null) ? totalNetPayable : null, paidAmount,
					(request.getPatientId() != null) ? new PatientRegistration(request.getPatientId()) : null,
					request.getRoundOff(), new Store(request.getStoreId()),
					(request.getTaxAmount() != null) ? request.getTaxAmount() : null,
					(request.getTotalAmount() != null) ? request.getTotalAmount() : null,
					request.getTotalDiscountAmount(),
					(request.getVisitTypeId() != null) ? new VisitType(request.getVisitTypeId()) : null, saleType, null,
					totalSelfPayable, totalCreditPayable, Screen.BillableConsumption.getScreen());

			sale.setFromBillableConsumption(true); // HIS-10498

			if (request.getAdmissionId() != null)
				sale.setAdmission(new Admission(request.getAdmissionId()));
			else if (request.getEncounterId() != null)
				sale.setEncounter(new Encounter(request.getEncounterId()));

			sale.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
			// sessionService.setEntityDefaults(sale);
			sale.setCreateDefaults(
					request.getOrganizationId() != null ? request.getOrganizationId()
							: sessionService.getOrganizationId(),
					request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
			String billConsumNumber = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(),
					screenId);
			sale.setBillNo(billConsumNumber);
			BillableConsumption billableConsumption = new BillableConsumption(billConsumNumber,
					new Store(request.getStoreId()), new VisitType(request.getVisitTypeId()),
					new PatientRegistration(request.getPatientId()), new Status(request.getStatusId()),
					new Status(request.getApprovedStatusId()));
			billableConsumption.setTotalAmount(request.getTotalAmount());
			billableConsumption.setTaxAmount(request.getTaxAmount());
			billableConsumption.setNetAmount(totalNetPayable);

			// sessionService.setEntityDefaults(billableConsumption);
			billableConsumption.setCreateDefaults(
					request.getOrganizationId() != null ? request.getOrganizationId()
							: sessionService.getOrganizationId(),
					request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

			if (request.getAdmissionId() != null) {
				billableConsumption.setAdmission(new Admission(request.getAdmissionId()));
				if (request.getWardId() != null) {
					billableConsumption.setWard(new WardMasters(request.getWardId()));
				}
				if (request.getBedId() != null) {
					billableConsumption.setBed(new BedMaster(request.getBedId()));
				}
			} else if (request.getEncounterId() != null) {
				billableConsumption.setEncounter(new Encounter(request.getEncounterId()));
			}

			if (request.getPrescriptionId() != null && request.getPrescriptionId() > 0) {
				Prescription presObj = GlobalDao.getPrescriptionData(request.getPrescriptionId());
				presObj.updatePrescriptionStatus(new Status(request.getStatusId()));
			}
			List<Batch>batchListAll=GlobalDao.getBatchAll(request.getStoreId(), request.getUnitId());
			for (CommonBillingItemsDto items : billableConsumptionContract.getItemList()) {

				if (items.getPrescriptionDetailId() != null && items.getPrescriptionDetailId() > 0) {
					PrescriptionDetail details = GlobalDao.getListPrescriptionDetails(items.getPrescriptionDetailId());
					details.updatePrescriptionDetailsStatus(new Status(items.getStatusId()));

				}

				BillableConsumptionDetail billableConsumptionDetail = new BillableConsumptionDetail(
						items.getBatchId() != null ? new Batch(items.getBatchId()) : null,
						items.getUomTypeId() != null ? new UomType(items.getUomTypeId()) : null,
						items.getUomUnitId() != null ? new UomUnit(items.getUomUnitId()) : null,
						items.getConsumableQuantity(), new Status(items.getStatusId()));
				billableConsumptionDetail.setTotalAmount(request.getTotalAmount());
				billableConsumptionDetail.setTaxAmount(request.getTaxAmount());
				billableConsumptionDetail.setNetAmount(request.getNetAmount());
				billableConsumptionDetail.setTax(new Tax(items.getTaxId()));
				billableConsumptionDetail.setItem(new Item(items.getItemId()));

				if (items.getDateAdministrationDate() != null) {
					billableConsumptionDetail.setDateAdministrationDate(items.getDateAdministrationDate());
				}

				if (items.getAdministrationTime() != null) {
					billableConsumptionDetail.setAdministrationTime(items.getAdministrationTime());
				}

				if (items.getRemark() != null) {
					billableConsumptionDetail.setRemark(items.getRemark());
				}
				billableConsumptionDetail.setCreateDefaults(
						request.getOrganizationId() != null ? request.getOrganizationId()
								: sessionService.getOrganizationId(),
						request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
						request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
				billableConsumption.addReturnDetailList(billableConsumptionDetail);
				
                Stream<Batch> batchListAllStream = batchListAll.stream();
                List<Batch> batchResponses=batchListAllStream
                .filter(str -> str.getId()!=null && str.getId().equals(items.getBatchId()))
                .collect(Collectors.toList());
                
				Batch batch = batchResponses.get(0);
				batch.setCreateDefaults(
						request.getOrganizationId() != null ? request.getOrganizationId()
								: sessionService.getOrganizationId(),
						request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
						request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
				if (batch != null) {
					Integer leaseQuantity = Util.GetLeaseQuantity(items.getConsumableQuantity(),
							batch.getBatchUomList(), new UomType(items.getUomTypeId()));


					Double amount = (leaseQuantity * batch.getSellingPrice());
					IssueDetailBatchMapper issueDetailBatchMapper = new IssueDetailBatchMapper(batch,
							new UomType(items.getUomTypeId()), new UomUnit(items.getUomUnitId()), leaseQuantity,
							amount);
				}

				SaleDetail saleDetail = new SaleDetail(batch, /* items.getDiscountAmount() */ new Double(0),
						items.getLeaseQuantity(), items.getNetAmount(), /* items.getOrderDetailId() */ null,
						 null, items.getQuantity(), items.getRate(), items.getTaxAmount(), new Tax(items.getTaxId()),
						items.getTaxPercentage(), items.getTotalAmount(),
						/* items.getUnitDiscountAmount() */new Double(0),
						items.getUomTypeId() != null ? new UomType(items.getUomTypeId()) : null,
						items.getUomUnitId() != null ? new UomUnit(items.getUomUnitId()) : null,
						new Status(com.param.common.constants.Status.Completed.getStatus()), items.getSelfPayable(),
						items.getCreditPayable(), items.getPayeeId(), items.getProductCategoryId(),
						items.getPackageConsumptionAmt());

				saleDetail.setConsessionAmount(items.getConsessionAmount());
				// added by chetana
				if (items.getPrescriptionDetailId() != null && items.getPrescriptionDetailId() > 0) {
					PrescriptionDetail predtl = new PrescriptionDetail();
					predtl.setId(items.getPrescriptionDetailId().longValue());
					saleDetail.setPrescriptionDetail(predtl);
				}

				//
				saleDetail.setConsessionPercentage(items.getConsessionPercentage());
				saleDetail.setUnitConsessionAmount(items.getUnitConsessionAmount());
				saleDetail.setItemId(items.getItemId());

				// For package
				List<SaleDetailPackageMapperDto> packageMppr = items.getSaleDetailPackageMapperDtoList();
				if (packageMppr != null && packageMppr.size() > 0) {
					List<SaleDetailPackageMapper> pkgMpprList = new ArrayList<SaleDetailPackageMapper>();
					for (SaleDetailPackageMapperDto dto : packageMppr) {
						SaleDetailPackageMapper mpper = new SaleDetailPackageMapper();
						mpper.setOrganizationId(request.getOrganizationId());
						mpper.setUnitId(request.getUnitId());
						mpper.setItemId(items.getItemId());
						mpper.setStatus('A');
						mpper.setIsPackageItem(dto.getIsPackageItem());
						mpper.setPackageId(dto.getPackageId());
						mpper.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
						mpper.setPacakgeConsumptionServiceDetailId(dto.getPacakgeConsumptionServiceDetailId());
						mpper.setPackageConsumptionCapDetailsId(dto.getPackageConsumptionCapDetailsId());
						mpper.setPackageConsumptionEODetailId(dto.getPackageConsumptionEODetailId());
						mpper.setPackageAmount(BigDecimal.valueOf(dto.getPackageAmount()));
						mpper.setPackageQty(dto.getPackageQty());
						mpper.setCapOrQty(dto.getCapOrQty());
						mpper.setRate(BigDecimal.valueOf(dto.getRate()));
						mpper.setSaleDetail(saleDetail);
						pkgMpprList.add(mpper);
					}
					saleDetail.setSaleDetailPackageMapperList(pkgMpprList);
				}

				// For Contract
				List<SaleDetailPayeeMapperDto> payeeMapper = items.getSaleDetailPayeeMapperDtoList();
				if (payeeMapper != null && payeeMapper.size() > 0) {
					List<SaleDetailPayeeMapper> payeeList = new ArrayList<SaleDetailPayeeMapper>();
					for (SaleDetailPayeeMapperDto dto : payeeMapper) {
						SaleDetailPayeeMapper mpper = new SaleDetailPayeeMapper();
						mpper.setOrganizationId(request.getOrganizationId());
						mpper.setUnitId(request.getUnitId());
						mpper.setItemId(items.getItemId());
						mpper.setStatus('A');
						mpper.setContractId(dto.getContractId());
						mpper.setPayeeId(dto.getPayeeId());
						mpper.setAssociateCompanyId(dto.getAssociateCompanyId());
						mpper.setPayeeAmount(dto.getPayeeAmount());
						mpper.setQuantity(dto.getQuantity());
						saleDetail.setPayeeId(dto.getPayeeId());
						mpper.setSaleDetail(saleDetail);
						payeeList.add(mpper);
					}
					saleDetail.setSaleDetailPayeeMapperList(payeeList);
				}

				saleDetail.setCreateDefaults(
						request.getOrganizationId() != null ? request.getOrganizationId()
								: sessionService.getOrganizationId(),
						request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
						request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

				sale.addSaleDetailsList(saleDetail);

				billableConsumptionDetail.setSaleDetail(saleDetail);
				billableConsumptionDetail.setCreateDefaults(
						request.getOrganizationId() != null ? request.getOrganizationId()
								: sessionService.getOrganizationId(),
						request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
						request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

			}
			iWalkInSalesService.saveGlobalSale(sale);
			billableConsumption.setSale(sale);
			billableConsumption.setCreateDefaults(
					request.getOrganizationId() != null ? request.getOrganizationId()
							: sessionService.getOrganizationId(),
					request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

			billableConsumptionDao.saveBillableConsumption(billableConsumption);
			
			for(CommonBillingItemsDto item :request.getItemList()){
				updateStockStransactionsameTransaction(billableConsumption.getBillableConsumptionDetail(),item,billableConsumption.getId().longValue(),request);
			}
			
			
			CommonResponse response = new CommonResponse();
			response.setId(billableConsumption.getId().longValue());
			response.setMessage("billable consumption saved successfully");

			return response;

		} catch (Exception e) {
			
			throw e;
		}
		// return null;
	}

	private Long saveStoreStockDetails(Integer storeId, Long batchId, Integer quantity, UomType uomType, Integer unitId,
			BillableConsumptionSave request, BillableConsumptionDetail billableConsumptionDetail, String transitNo) {
		if (quantity > 0) {
			try {
				Batch batch = GlobalDao.getBatch(batchId);
				if (batch != null) {

					Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);

					// added by minesh for consume consignment first.
					List<Batch> batchList = GlobalDao.getSameBatchList(batch.getItem().getId(), batch.getBatchNo(),
							batch.getLotNo(), storeId, unitId);
					List<Batch> sortedBatchList = batchList.stream()
							.sorted((s1, s2) -> s1.getIsConsignment() ? -1 : s2.getIsConsignment() ? 1 : 0)
							.collect(Collectors.toList());
					// Integer totalBatchCurrentQuantity = sortedBatchList.stream().mapToInt(x ->
					// x.getCurrentQuantity()).sum();
					List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(batch.getId(),
							storeId, unitId);
					Integer totalBatchCurrentQuantity = StoreStockList.stream().mapToInt(x -> x.getAvailableQty())
							.sum();
					if (totalBatchCurrentQuantity >= leaseQuantity) {
						for (Batch batchObj : sortedBatchList) {
							if (leaseQuantity != 0) {
//                                if((batchObj.getCurrentQuantity() == null || batchObj.getCurrentQuantity() <= 0) && batch.getId().equals(batchObj.getId())){
//                                    Integer res = GlobalDao.updateBatchCurrentQuantityWithStoreStockQuantity(batch);
//                                    batchObj.setCurrentQuantity(batch.getCurrentQuantity());
//                                }
								if (batchObj.getCurrentQuantity() >= leaseQuantity) {
									batchId = callSetLeaseQuantity(batchObj, leaseQuantity, storeId, unitId, request,
											billableConsumptionDetail, transitNo);
									leaseQuantity = 0;
									return batchId;
								} else {
									int batchCurrentQuantity = batchObj.getCurrentQuantity();
									callSetLeaseQuantity(batchObj, batchCurrentQuantity, storeId, unitId, request,
											billableConsumptionDetail, transitNo);
									leaseQuantity = leaseQuantity - batchCurrentQuantity;
								}
							}
						}
					} else {
						throw new CustomException("Return quantity must be less than current quantity");
					}

					/*
					 * // added if condation by Minesh for check flag isConsignment // use for
					 * convert consignmentBatch in to normalBatch if (batch.getIsConsignment() !=
					 * null && batch.getIsConsignment()) { Batch normalBatch =
					 * getConsignmentConvertedBatch(batchId, batch, leaseQuantity);
					 * 
					 * // change by Minesh/Atmiya for we consume quantity in batch , so set
					 * 'quantity' batch.setQuantity(batch.getQuantity() - leaseQuantity);
					 * setLeaseQuantity(batch, leaseQuantity);
					 * 
					 * // change by Minesh/Atmiya for we consume quantity in normal batch , so set
					 * // 'quantity' setLeaseQuantity(normalBatch, leaseQuantity);
					 * 
					 * // change by Minesh/Atmiya for we consume quantity in normal batch , so set
					 * 'quantity' setLeaseQuantity(normalBatch, leaseQuantity);
					 * 
					 * // code for update conversion Status of grn
					 * updateGRNConversionStatus(batchId, storeId);
					 * 
					 * return normalBatch.getId(); } else { setLeaseQuantity(batch, leaseQuantity);
					 * return batch.getId(); }
					 */

				}

			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
		}
		return batchId;
	}

	private Long callSetLeaseQuantity(Batch batchObj, Integer leaseQuantity, Integer storeId, Integer unitId,
			BillableConsumptionSave request, BillableConsumptionDetail billableConsumptionDetail, String transitNo) {
		// added for manage hibernate session

		Batch batch = GlobalDao.getBatch(batchObj.getId());

		batch.setCreateDefaults(
				request.getOrganizationId() != null ? request.getOrganizationId() : sessionService.getOrganizationId(),
				request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
				request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
		// added if condition by Minesh for check flag isConsignment, use for convert
		// consignmentBatch in to normalBatch
		if (batch.getIsConsignment() != null && batch.getIsConsignment()) {
			Batch convertedConsumptionBatch = getConsignmentConvertedBatch(batch, leaseQuantity, request);

			// change by Minesh/Atmiya for we consume quantity from origin
			// batch(consignmentBatch) , so set 'quantity'
			batch.setQuantity(batch.getQuantity() - leaseQuantity);
			setLeaseQuantity(batch, leaseQuantity, storeId, unitId, request, billableConsumptionDetail, transitNo);

			// change by Minesh/Atmiya for we consume quantity in normal batch , so set
			// 'quantity'
			// convertedConsumptionBatch.setQuantity(convertedConsumptionBatch.getQuantity()
			// - leaseQuantity);
			setLeaseQuantity(convertedConsumptionBatch, leaseQuantity, storeId, unitId, request,
					billableConsumptionDetail, transitNo);

			return convertedConsumptionBatch.getId();
		} else {
			// batch.setQuantity(batch.getQuantity() - leaseQuantity);
			setLeaseQuantity(batch, leaseQuantity, storeId, unitId, request, billableConsumptionDetail, transitNo);
			return batch.getId();
		}
	}

	private void setLeaseQuantity(Batch batch, Integer leaseQuantity, Integer storeId, Integer unitId,
			BillableConsumptionSave request, BillableConsumptionDetail billableConsumptionDetail, String transitNo) {
		// Integer tQuantity;
		Integer availableQuantity;

		// change from batchId to batch.getId() in parameter
		StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(batch.getId(), storeId, unitId);
		StoreStock storeStockDetail = null;
		if (storeStockList == null) {
			// change from batchId to batch.getId() in parameter
			StoreStock storeStock = new StoreStock(leaseQuantity, new Store(storeId), new Batch(batch.getId()));
			// sessionService.setEntityDefaults(storeStock);
			storeStock.setCreateDefaults(
					request.getOrganizationId() != null ? request.getOrganizationId()
							: sessionService.getOrganizationId(),
					request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

			storeStockList = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
		}

		// code change: remove else condation by Atmiya. for insert new stock and update
		// it.
		// storeStockList = GlobalDao.getStoreStockByBatchAndStore(batch.getId(),
		// sessionService.getStoreId());

		// tQuantity = leaseQuantity + storeStockList.gettQty();
		availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
		if (availableQuantity < 0) {
			availableQuantity = 0;
		}
		storeStockList.setAvailableQty(availableQuantity);
		// storeStockList.settQty(tQuantity);
		storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);

		// comment code by atmiya to useless fetch (alredy featched). no need to fetch
		// again
		// Batch batchObject = GlobalDao.getBatch(sessionService.getStoreId(),
		// storeStockDetail.getBatch().getId());
		Integer currentQty = batch.getCurrentQuantity() - leaseQuantity;

		batch.setCurrentQuantity(currentQty > 0 ? currentQty : 0);
		GlobalDao.updateBatchLeaseQuantity(batch);

		saveStockTransaction(leaseQuantity, storeStockDetail, request, billableConsumptionDetail, transitNo);
	}

	/*
	 * created by Minesh for duplication entry with new id and consignmentflag false
	 * in batch table use for convert consignmentBatch in normal batch return new
	 * converted batch(normalBatch).
	 */
	private Batch getConsignmentConvertedBatch(Batch batch, Integer leaseQuantity, BillableConsumptionSave request) {
		Batch newBatch = new Batch();
		newBatch.setOrganization(batch.getOrganization());
		newBatch.setUnit(batch.getUnit());
		newBatch.setUomUnit(batch.getUomUnit());
		newBatch.setItem(batch.getItem());
		newBatch.setBatchNo(batch.getBatchNo());
		newBatch.setLotNo(batch.getLotNo());
		newBatch.setQuantity(leaseQuantity);// leaseQuantity
		newBatch.setCurrentQuantity(leaseQuantity);// leaseQuantity
		newBatch.setExpiryDate(batch.getExpiryDate());
		newBatch.setCop(batch.getCop());
		newBatch.setRate(batch.getRate());
		newBatch.setSellingPrice(batch.getSellingPrice());
		newBatch.setMarkup(batch.getMarkup());
		newBatch.setLendingPrice(batch.getLendingPrice());
		newBatch.setActualLendingPrice(batch.getActualLendingPrice());
		newBatch.setTax(batch.getTax());
		newBatch.setTaxPercentage(batch.getTaxPercentage());
		newBatch.setTaxAmount(batch.getTaxAmount());
		newBatch.setBarcode(batch.getBarcode());
		newBatch.setIsOpb(batch.getIsOpb());
		newBatch.setIsActive(batch.getIsActive());
		newBatch.setAddedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		newBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
		newBatch.setAddedByUser(request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
		newBatch.setUpdatedByUser(request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
		newBatch.setIsDeleted(batch.getIsDeleted());
		newBatch.setBarcodePrintCount(batch.getBarcodePrintCount());
		newBatch.setIsQuarantine(batch.getIsQuarantine());
		newBatch.setVendor(batch.getVendor());
		newBatch.setServiceProvider(batch.getServiceProvider());
		newBatch.setWarrantyFrom(batch.getWarrantyFrom());
		newBatch.setWarrantyTo(batch.getWarrantyTo());
		newBatch.setPmsFrequency(batch.getPmsFrequency());
		newBatch.setYearOfManufacture(batch.getYearOfManufacture());
		newBatch.setWorkingCondition(batch.getWorkingCondition());
		newBatch.setModelNumber(batch.getModelNumber());
		newBatch.setIsPharmacy(batch.getIsPharmacy());
		newBatch.setIsIndent(batch.getIsIndent());
		newBatch.setOrganizationId(request.getOrganizationId());
		newBatch.setUnitId(request.getUnitId());
		newBatch.setIsReturn(false);
		newBatch.setRejectedQuantity(0);
		newBatch.setReturnedQuantity(0);
		// newBatch.stop
		newBatch.setIsConsignment(false);

		for (BatchUomMapper batchUomMapperObj : batch.getBatchUomList()) {
			BatchUomMapper batchUomMapper = new BatchUomMapper(batchUomMapperObj.getConversions(),
					batchUomMapperObj.getIpUom(), batchUomMapperObj.getOpUom(), batchUomMapperObj.getStoreUom(),
					new UomType(batchUomMapperObj.getUomType().getId()),
					new UomUnit(batchUomMapperObj.getUomUnit().getId()));
			batchUomMapper.setCreateDefaults(
					request.getOrganizationId() != null ? request.getOrganizationId()
							: sessionService.getOrganizationId(),
					request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
					request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
			newBatch.addBatchUom(batchUomMapper);
		}

		newBatch = GlobalDao.saveBatch(newBatch);
		return newBatch;
	}

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock, BillableConsumptionSave request,
			BillableConsumptionDetail billableConsumptionDetail, String transitNo) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), new Store(request.getStoreId()!=null ? request.getStoreId() : sessionService.getStoreId()),
				new Batch(storeStock.getBatch().getId()), 0);
		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setTransitNo(transitNo);
		stockTransaction.setTransactionDetialsId(billableConsumptionDetail.getId());
		stockTransaction.setTransactionId(billableConsumptionDetail.getBillableConsumption().getId());
		stockTransaction.setItem(billableConsumptionDetail.getItem());
		stockTransaction.setIsTransit(false);
		stockTransaction.setOutQty(leaseQuantity);
		
		stockTransaction.setCreateDefaults(
				request.getOrganizationId() != null ? request.getOrganizationId() : sessionService.getOrganizationId(),
				request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
				request.getUserId() != null ? request.getUserId() : sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);
	}

	private Long saveStoreStockDetailsWithoutBatchRequired(Integer storeId, Long itemId, Integer quantity,
			Integer unitId, BillableConsumptionSave request, CommonBillingItemsDto items,
			BillableConsumptionDetail billableConsumptionDetail, String transitNo, Long batchId, UomType uomType){
		try{
			List<Batch> batchList = GlobalDao.getBatchListByItemIdForBillableConsumption(itemId, storeId);
			if (!batchList.isEmpty()) {
				List<Batch> sortedBatchList = batchList.stream()
						.sorted((s1, s2) -> s1.getIsConsignment() ? -1 : s2.getIsConsignment() ? 1 : 0)
						.collect(Collectors.toList());
				Batch batch = GlobalDao.getBatch(batchId);
				Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
				Integer totalBatchCurrentQuantity = sortedBatchList.stream().mapToInt(x -> x.getCurrentQuantity()).sum();
				if (totalBatchCurrentQuantity >= leaseQuantity) {
					for (Batch batchObj : sortedBatchList) {
						if (batchObj.getId().equals(items.getBatchId())) {
//                            if((batchObj.getCurrentQuantity() == null || batchObj.getCurrentQuantity() <= 0)){
//                                Integer res = GlobalDao.updateBatchCurrentQuantityWithStoreStockQuantity(batch);
//                                batchObj.setCurrentQuantity(batch.getCurrentQuantity());
//                            }
							if (batchObj.getCurrentQuantity() >= leaseQuantity) {
								callSetLeaseQuantity(batchObj, leaseQuantity, storeId, unitId, request,
										billableConsumptionDetail, transitNo);
								leaseQuantity = 0;
								return batchObj.getId().longValue();
							} else {
								int batchCurrentQuantity = batchObj.getCurrentQuantity();
								callSetLeaseQuantity(batchObj, batchCurrentQuantity, storeId, unitId, request,
										billableConsumptionDetail, transitNo);
								leaseQuantity = leaseQuantity - batchCurrentQuantity;
							}
						}

						batchObj.setCreateDefaults(
								request.getOrganizationId() != null ? request.getOrganizationId()
										: sessionService.getOrganizationId(),
								request.getUnitId() != null ? request.getUnitId() : sessionService.getUnitId(),
								request.getUserId() != null ? request.getUserId() : sessionService.getUserId());
					}
				} else {
					throw new CustomException("Return quantity must be less than current quantity");
				}
			} else {
				throw new CustomException("Batches not found for given item ID");
			}
			return batchId;
//			throw new CustomException("Error while updating store stcock");
		}
		catch(Exception e){
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	@Transactional
	public List<BillableConsumptionSave> getBillableConsumptionDetails(Long id) throws Exception {
		// TODO Auto-generated method stub
		return billableConsumptionDao.getBillableConsumptionDetails(id);
	}

	
	private  void updateStockStransactionsameTransaction(List<BillableConsumptionDetail> resList, CommonBillingItemsDto items,Long id, BillableConsumptionSave request)
			throws Exception {
		
		BillableConsumptionDetail billableConsumptionDetail = new BillableConsumptionDetail();
		
		for (BillableConsumptionDetail billableConsumptionDetail2 : resList) {
			if(billableConsumptionDetail2.getItem().getId().equals(items.getItemId()) && billableConsumptionDetail2.getConsumableQuantity().equals(items.getConsumableQuantity())) {
				billableConsumptionDetail.setConsumableQuantity(billableConsumptionDetail2.getConsumableQuantity());
				billableConsumptionDetail.setItem(new Item(items.getItemId()));
				billableConsumptionDetail.setBillableConsumption(new BillableConsumption(id.intValue()));
				billableConsumptionDetail.setId(billableConsumptionDetail2.getId());
			}
		}
		
		if (items.getIsBatchReq() != null) {
			if (items.getIsBatchReq()) {
				Long batchId = saveStoreStockDetails(request.getStoreId(), items.getBatchId(),
						items.getConsumableQuantity(), new UomType(items.getUomTypeId()), request.getUnitId(),
						request, billableConsumptionDetail,billableConsumptionDetail.getBillableConsumption().getConsumableNumber());
				items.setBatchId(batchId);
			} else {
				Long batchId = saveStoreStockDetailsWithoutBatchRequired(request.getStoreId(),
						items.getItemId().longValue(), items.getConsumableQuantity(), request.getUnitId(),
						request, items, billableConsumptionDetail,billableConsumptionDetail.getBillableConsumption().getConsumableNumber(),items.getBatchId(), new UomType(items.getUomTypeId()));
				items.setBatchId(items.getBatchId());
				billableConsumptionDetail.setBatch(new Batch(items.getBatchId()));
			}
		} else {
			Long batchId = saveStoreStockDetailsWithoutBatchRequired(request.getStoreId(),
					items.getItemId().longValue(), items.getConsumableQuantity(), request.getUnitId(), request,
					items,billableConsumptionDetail,billableConsumptionDetail.getBillableConsumption().getConsumableNumber(),items.getBatchId(), new UomType(items.getUomTypeId()));
			items.setBatchId(batchId);
		}

	}


}