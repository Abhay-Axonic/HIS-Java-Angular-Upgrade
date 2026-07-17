package com.param.pharmacy.nursing.crashCart.service.crashCartApproval;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.AdmissionDetail;
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
import com.param.entity.model.pharmacy.CrashCartBatchMapper;
import com.param.entity.model.pharmacy.CrashCartRequestDetail;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.SaleDetailPayeeMapper;
import com.param.entity.model.pharmacy.SaleType;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.common.Response;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.PatientConsession;
import com.param.global.dto.items.BatchResponse;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.global.IGlobalService;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.pharmacy.nursing.crashCart.dao.ICrashCartConsumptionDao;
import com.param.pharmacy.nursing.crashCart.dao.crashCartApproval.ICrashCartApprovalDao;
import com.param.pharmacy.nursing.crashCart.dto.CrashCartRequest;
import com.param.pharmacy.nursing.crashCart.dto.CrashCartResponse;
import com.param.pharmacy.nursing.crashCart.dto.ItemResponse;
import com.param.service.global.SessionService;
import com.param.service.global.model.SaleDetailPackageMapper;
import com.param.utility.TimeZoneComponent;

@Service
public class CrashCartApprovalService implements ICrashCartApprovalService {

	private Integer screenId = Screen.CrashCartRequestApproval.getScreen();
	private Integer screenId1 = Screen.CrashCartRequestApproval.getScreen();

	@Autowired
	private SessionService sessionService;

	@Autowired
	ICrashCartApprovalDao CrashCartApprovalDao;

	@Autowired
	ICrashCartConsumptionDao CrashCartConsumptionDao;

	@Autowired
	IGlobalDao GlobalDao;

	@Autowired
	IWalkInSalesService iWalkInSalesService;

	@Autowired
	IMPackageMasterService iMPackageMasterService;

	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao;

	@Autowired
	IGlobalDao iglobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Override
	@Transactional
	public List<CrashCartResponse> getCrashCartApprovalList(CrashCartRequest request) {

		List<CrashCartResponse> crashCartResponsesList = new ArrayList<CrashCartResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		for (com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest : CrashCartApprovalDao
				.crashCartApprovalSearch(request, screenId)) {

			Integer age = Util
					.getAgeByBirthDate(crashCartRequest.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = crashCartRequest.getPatient().getPrefix().getPrefixCode() + " "
					+ crashCartRequest.getPatient().getFirstName() + " " + crashCartRequest.getPatient().getMiddleName()
					+ " " + crashCartRequest.getPatient().getLastName() + " | " + age + " | "
					+ crashCartRequest.getPatient().getGender().getGenderCode();

			String doctorDetail = "";

			if (crashCartRequest.getDoctor() != null && crashCartRequest.getSpecialityMaster() != null) {
				/*
				 * List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
				 * .doctorSearchByIdValue(crashCartRequest.getDoctor().getDoctorId());
				 */
				doctorDetail = crashCartRequest.getDoctor().getFirstName() + " "
						+ crashCartRequest.getDoctor().getMiddleName() + " "
						+ crashCartRequest.getDoctor().getLastName() + " | "
						+ crashCartRequest.getSpecialityMaster().getSpecialityName();
			} else {
//				doctorDetail = "- | "
//						+ GlobalDao.doctorSpeciality(request.getOrganizationId(), request.getUnitId());
                if (crashCartRequest.getSpecialityMaster() != null) {
                	   doctorDetail = doctorDetail + " | " + crashCartRequest.getSpecialityMaster().getSpecialityName();
                   }
			}

			CrashCartResponse crashCartResponse = new CrashCartResponse(crashCartRequest.getId(),
					crashCartRequest.getCrashCartNo(), crashCartRequest.getAddedDate(),
					crashCartRequest.getCodeBlueDateTime(), crashCartRequest.getVisitType().getVisitTypeCode(),
					crashCartRequest.getStore().getStoreName(), crashCartRequest.getPatient().getUhidNumber(),
					patientDetail, doctorDetail,
					(crashCartRequest.getWard() != null) ? crashCartRequest.getWard().getWardName() : null,
					(crashCartRequest.getBed() != null) ? crashCartRequest.getBed().getBedNumber() : null,
					crashCartRequest.getAddedBy().getUserName(), crashCartRequest.getStatus().getId(),
					crashCartRequest.getStatus().getStatus());

			if (crashCartRequest.getAdmission() != null) {
				crashCartResponse.setVisitNo(crashCartRequest.getAdmission().getAdmissionNumber());
			}
			if (crashCartRequest.getEncounter() != null) {
				crashCartResponse.setVisitNo(crashCartRequest.getEncounter().getEncounterNumber());
			}

			crashCartResponsesList.add(crashCartResponse);
		}
		return crashCartResponsesList;
	}

	@Override
	@Transactional
	public Object saveCrashCartApproval(CrashCartRequest request) throws Exception {

		com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest = CrashCartConsumptionDao
				.getCrashCartRequest(request.getId(),request.getUnitId());

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
				saleType = new SaleType(5);
			else
				throw new RuntimeException("Invalid visit type");
		}

		// Check Contract
		request.setOrganizationId(request.getOrganizationId());
		request.setUnitId(request.getUnitId());
		Double outStandingAmount = request.getNetAmount();
		Double paidAmount = 0.0;

		List itemList = request.getItems();
		request.setItemList(itemList);
		Response billableConsumptionRes = iMPackageMasterService.consumeItemFromPackageContract(request,0);
		CrashCartRequest billableConsumptionContract = (CrashCartRequest) billableConsumptionRes.getObject();

		Double totalSelfPayable = billableConsumptionContract.getItemList().stream()
				.filter(i -> i.getSelfPayable() != null).mapToDouble(i -> i.getSelfPayable()).sum();

		Double totalCreditPayable = billableConsumptionContract.getItemList().stream()
				.filter(i -> i.getCreditPayable() != null).mapToDouble(i -> i.getCreditPayable()).sum();

		Double totalAmount = billableConsumptionContract.getItemList().stream().filter(i -> i.getTotalAmount() != null)
				.mapToDouble(i -> i.getTotalAmount()).sum();

		Double tatalNetAmount = billableConsumptionContract.getItemList().stream()
				.filter(i -> i.getNetAmount() != null).mapToDouble(i -> i.getNetAmount()).sum();

		Double totalTax = billableConsumptionContract.getItemList().stream().filter(i -> i.getTaxAmount() != null)
				.mapToDouble(i -> i.getTaxAmount()).sum();

		Sale sale = new Sale(request.getAdmissionId() != null ? new Admission(request.getAdmissionId()) : null, null,
				tatalNetAmount, 0.00,
				new com.param.entity.model.master.Status(com.param.common.constants.Status.Pending.getStatus()),
				new Store(request.getStoreId()), totalTax, totalAmount, tatalNetAmount, null, totalSelfPayable,
				totalCreditPayable,com.param.common.constants.Screen.CrashCartRequestApproval.getScreen());

		/*
		 * String billNo = GlobalService.getBillNoByGeneratedCode(request.getStoreId(),
		 * sessionService.getUnitId(), screenId);
		 * 
		 * if (billNo == null) { throw new
		 * CustomException(" Please configure prefix number for crash cart bill "); }
		 */
		sale.setFromCrashCart(true);
		sale.setBillNo(crashCartRequest.getCrashCartNo()); // if change use above code and add screen
		sale.setSaleType(saleType);
		sale.setVisitType(visitType);
		if (request.getAdmissionId() != null)
			sale.setAdmission(new Admission(request.getAdmissionId()));
		else if (request.getEncounterId() != null)
			sale.setEncounter(new Encounter(request.getEncounterId()));

		sale.setPatient(new PatientRegistration(request.getPatientId()));
		sale.setFromBillableConsumption(false);
		sale.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
		//sessionService.setEntityDefaults(sale);
		sale.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		for (CrashCartRequestDetail detail : CrashCartConsumptionDao.getCrashCartRequestDetail(request.getId())) {

			for (CommonBillingItemsDto items : request.getItemList()) {
				
				if(detail.getId().equals(items.getId())) //HIS-11408
				{
					CrashCartBatchMapper crashCartBatchMapper = new CrashCartBatchMapper(new Batch(items.getBatchId()),
						items.getConsumedQuantity(), new CrashCartRequestDetail(items.getId()));
					crashCartBatchMapper.setUomType(items.getUomTypeId() != null ? new UomType(items.getUomTypeId()) : null);
					crashCartBatchMapper.setUomUnit(items.getUomUnitId() != null ? new UomUnit(items.getUomUnitId()) : null);
				detail.setApprovedQty(items.getConsumedQuantity());
				detail.addCrashCartBatchMapper(crashCartBatchMapper);
				//sessionService.setEntityDefaults(crashCartBatchMapper);
				crashCartBatchMapper.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

				Batch batch = GlobalDao.getBatch(request.getStoreId(), items.getBatchId(),request.getUnitId());

				// added by minesh for consignment and consumption logic.
				Integer leaseQuantity = Util.GetLeaseQuantity(items.getConsumedQuantity(), batch.getBatchUomList(),
						items.getUomTypeId());

				// List<StoreStock> StoreStockList =
				// GlobalDao.getStoreStockListByBatchIdAndStoreId(items.getBatchId(),request.getStoreId());
				List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(items.getBatchId(),
						request.getStoreId(), batch.getLotNo(),request.getUnitId());

				Integer totalBatchCurrentQuantity = StoreStockList.stream().mapToInt(x -> x.getAvailableQty()).sum();
				if (totalBatchCurrentQuantity >= leaseQuantity) {
					for (StoreStock storeStockObj : StoreStockList) {
						if (leaseQuantity != 0) {
							Integer storeAvaliableQuantity = storeStockObj.getAvailableQty();
							Long batchIdFromStore = storeStockObj.getBatch().getId();
							if (leaseQuantity <= storeAvaliableQuantity) {
								updateStoreStockAndBatch(storeStockObj, leaseQuantity, batchIdFromStore,
										request.getStoreId(), request,detail);
								leaseQuantity = 0;
								break;
							} else {
								int avalibleLeaseQuantity = storeStockObj.getAvailableQty();
								updateStoreStockAndBatch(storeStockObj, avalibleLeaseQuantity, batchIdFromStore,
										request.getStoreId(),request,detail);
								leaseQuantity = leaseQuantity - avalibleLeaseQuantity;
							}
						}
					}
				} else {
					throw new CustomException(
							"Return quantity must be less than current quantity for " + batch.getBatchNo());
				}
				// code end by minesh for consignment and consumption logic.

				SaleDetail saleDetail = new SaleDetail(batch, /* items.getDiscountAmount() */ Double.valueOf(0),
						items.getLeaseQuantity(), items.getNetAmount(), /* items.getOrderDetailId() */ null,
						/*
						 * (items.getPrescriptionDetailId() != null) ? new
						 * PrescriptionDetail(items.getPrescriptionDetailId()) :
						 */ null, items.getQuantity(), items.getRate(), items.getTaxAmount(), new Tax(items.getTaxId()),
						items.getTaxPercentage(), items.getTotalAmount(),
						/* items.getUnitDiscountAmount() */Double.valueOf(0),
						items.getUomTypeId() != null ? new UomType(items.getUomTypeId()) : null,
						items.getUomUnitId() != null ? new UomUnit(items.getUomUnitId()) : null,
						new Status(com.param.common.constants.Status.Pending.getStatus()), items.getSelfPayable(),
						items.getCreditPayable(), items.getPayeeId(), items.getProductCategoryId(),
						items.getPackageConsumptionAmt());

				saleDetail.setConsessionAmount(items.getConsessionAmount());
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

				//sessionService.setEntityDefaults(saleDetail);
				saleDetail.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

				sale.addSaleDetailsList(saleDetail);
			}
			}
		}
		iWalkInSalesService.saveGlobalSale(sale);
		crashCartRequest.setSale(sale);
		crashCartRequest.setStatus(new Status(request.getStatusId()));
		CrashCartApprovalDao.saveCrashCartRequestApproval(crashCartRequest);

		CommonResponse response = new CommonResponse();
		response.setId(crashCartRequest.getId().longValue());
		response.setMessage("Crash Cart Request Approval saved successfully");

		return response;
	}

	@Override
	@Transactional
	public Object crashCartApprovalDetails(Integer id,Integer unitId,Integer orgId) throws Exception {



		List<PatientSponsorDetails> patientSponsorDetails = null;
		CrashCartResponse response = new CrashCartResponse();
		com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest = CrashCartConsumptionDao
				.getCrashCartRequest(id,unitId);

		Integer age = Util.getAgeByBirthDate(crashCartRequest.getPatient().getDob().toLocalDateTime().toLocalDate());

		String patientDetail = crashCartRequest.getPatient().getPrefix().getPrefixCode() + " "
				+ crashCartRequest.getPatient().getFirstName() + " " + crashCartRequest.getPatient().getMiddleName()
				+ " " + crashCartRequest.getPatient().getLastName() + " | " + age + " | "
				+ crashCartRequest.getPatient().getGender().getGenderCode();

		String doctorDetail = "";

		if (crashCartRequest.getDoctor() != null && crashCartRequest.getSpecialityMaster() != null) {
			/*
			 * List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
			 * .doctorSearchByIdValue(crashCartRequest.getDoctor().getDoctorId());
			 */
			doctorDetail = crashCartRequest.getDoctor().getFirstName() + " "
					+ crashCartRequest.getDoctor().getMiddleName() + " " + crashCartRequest.getDoctor().getLastName()
					+ " | " + crashCartRequest.getSpecialityMaster().getSpecialityName();
		} else {
			doctorDetail = "- | "
					+ GlobalDao.doctorSpeciality(orgId, unitId);
		}

		if (crashCartRequest.getAdmission() != null) {

			patientSponsorDetails = iPatientSponsorDetailsDao
					.findByAdmissionId(crashCartRequest.getAdmission().getAdmissionId());

			Date doa = null;
			Date pdd = null;
			AdmissionDetail admissionDetail = GlobalDao
					.getPatientWardBedDetails(crashCartRequest.getAdmission().getAdmissionId());
			if (admissionDetail != null) {
				doa = admissionDetail.getDoa();
				pdd = admissionDetail.getPdd();
			}
			response = new CrashCartResponse(crashCartRequest.getId(), crashCartRequest.getCrashCartNo(),
					crashCartRequest.getAddedDate(), crashCartRequest.getVisitType().getVisitTypeCode(),
					crashCartRequest.getStore().getStoreName(), crashCartRequest.getPatient().getUhidNumber(),
					patientDetail, doctorDetail, crashCartRequest.getAdmission().getAdmissionNumber(),
					(crashCartRequest.getWard() != null) ? crashCartRequest.getWard().getWardName() : null,
					(crashCartRequest.getBed() != null) ? crashCartRequest.getBed().getBedNumber() : null, pdd, doa,
					crashCartRequest.getAdmission().getPatient().getDob(), crashCartRequest.getRemark(),
					crashCartRequest.getStatus().getId(), crashCartRequest.getStatus().getStatus(),
					crashCartRequest.getAdmission().getPatient().getGender().getGenderId(),
					crashCartRequest.getAdmission().getPatient().getGender().getGenderCode(),
					crashCartRequest.getAdmission().getPatient().getImagePath());
			response.setVisitTypeId(crashCartRequest.getVisitType().getVisitTypeId());
			response.setPatientId(crashCartRequest.getAdmission().getPatient().getPatientId());
			response.setAdmissionId(crashCartRequest.getAdmission().getAdmissionId());
			/*
			 * start added storeId code for resolved HIS-10354 Crash Cart Request Approval -
			 */
			response.setStoreId(crashCartRequest.getStore().getStoreId());
			/*
			 * end added storeId code for resolved HIS-10354 Crash Cart Request Approval -
			 */
		}
		if (crashCartRequest.getEncounter() != null) {

			patientSponsorDetails = iPatientSponsorDetailsDao
					.findByEncounterId(crashCartRequest.getEncounter().getEncounterId());

			response = new CrashCartResponse(crashCartRequest.getId(), crashCartRequest.getCrashCartNo(),
					crashCartRequest.getAddedDate(), crashCartRequest.getVisitType().getVisitTypeCode(),
					crashCartRequest.getStore().getStoreName(), crashCartRequest.getPatient().getUhidNumber(),
					patientDetail, doctorDetail, crashCartRequest.getEncounter().getEncounterNumber(),
					crashCartRequest.getEncounter().getPatient().getDob(), crashCartRequest.getRemark(),
					crashCartRequest.getEncounter().getPatient().getGender().getGenderId(),
					crashCartRequest.getEncounter().getPatient().getGender().getGenderCode(),
					crashCartRequest.getEncounter().getPatient().getImagePath());
			response.setVisitTypeId(crashCartRequest.getVisitType().getVisitTypeId());
			response.setPatientId(crashCartRequest.getEncounter().getPatient().getPatientId());
			response.setEncounterId(crashCartRequest.getEncounter().getEncounterId());
			response.setStatusId(crashCartRequest.getStatus().getId());
			response.setStatus(crashCartRequest.getStatus().getStatus());

			/*
			 * start added storeId code for resolved HIS-10354 Crash Cart Request Approval -
			 */
			response.setStoreId(crashCartRequest.getStore().getStoreId());
			/*
			 * end added storeId code for resolved HIS-10354 Crash Cart Request Approval -
			 */
		}
		if (!crashCartRequest.getStatus().getId().equals(5)) {
			for (CrashCartRequestDetail detail : CrashCartConsumptionDao.getCrashCartRequestDetail(id)) {
				List<CrashCartBatchMapper> crashCartBatchMappers = detail.getCrashCartBatchMappers();
				List<BatchResponse> batches = new ArrayList<BatchResponse>();
				List<Batch> batchList = GlobalDao.getBatchDetailsPharmacy(detail.getItem().getId(),
						crashCartRequest.getStore().getStoreId(),unitId);
				if (batchList.isEmpty()) {
					try {
						throw new Exception("Selected item is out of stock");
					} catch (Exception e) {
						
						throw e;
					}
				} else {
					for (Batch batch : batchList) {

						Optional<CrashCartBatchMapper> batchMapper = crashCartBatchMappers.stream()
								.filter(x -> x.getBatch().getId().equals(batch.getId())).findFirst();
						if (batchMapper.isPresent()) {
							BatchResponse res = Util.PopulateBatchWithStockWithFlag(batch, true);
							res.setUomTypeId(detail.getUomType().getId());
							res.setUomUnitId(detail.getUomUnit().getId());
							batches.add(res);
						} else
							batches.add(Util.PopulateBatchWithStockWithFlag(batch, false));
					}
				}

				ItemResponse item = new ItemResponse(detail.getId(), detail.getItem().getId(),
						detail.getItem().getCode(), detail.getItem().getName(), detail.getItem().getGeneric().getId(),
						detail.getItem().getGeneric().getName(), detail.getRequiredQty(), detail.getApprovedQty(),
						batches, detail.getItem().getIsThreeUOMApplicable());
				item.setUomTypeId(detail.getUomType().getId());
				for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
					List<Integer> prodCatIds = new ArrayList<Integer>();
					Integer productCategoryId = detail.getItem().getGeneric() != null
							? detail.getItem().getGeneric().getProductCategory().getId()
							: detail.getItem().getProductCategory().getId();
					prodCatIds.add(productCategoryId);
					item.setProductCategoryId(productCategoryId);
					item.setProdCatId(productCategoryId);
					List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
							sponsorDetail.getPaymentEntitlementId());
					if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
						if (pharmacyTariffs.size() > 1) {
							// throw exception?
							// Don't throw - get first object and set data.
						}

						PharmacyTariff pharmacyTariff = pharmacyTariffs.getFirst();
						item.setMarkUpDown(pharmacyTariff.getMarkUpDown());
						item.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());

						break;
					}
				}

				Integer assetCatId = 0;
				Integer prodCatId = 0;
				PatientConsession patientConsession = null;
				if (detail.getItem().getProductCategory() != null) {
					prodCatId = detail.getItem().getProductCategory().getId();
					assetCatId = detail.getItem().getProductCategory().getAssetCategory().getId();
				} else {
					prodCatId = detail.getItem().getGeneric().getProductCategory().getId();
					assetCatId = detail.getItem().getGeneric().getProductCategory().getAssetCategory().getId();
				}

				if (crashCartRequest.getAdmission() != null) {
					patientConsession = iglobalDao.getPatientConsessionAdmission(detail.getItem().getId(),
							crashCartRequest.getAdmission().getAdmissionId(),
							crashCartRequest.getAdmission().getPatient().getPatientCategoryId(), assetCatId, prodCatId);
				} else {
					patientConsession = iglobalDao.getPatientConsessionEncounter(detail.getItem().getId(),
							crashCartRequest.getEncounter().getEncounterId(),
							crashCartRequest.getEncounter().getPatient().getPatientCategoryId(), assetCatId, prodCatId,null,null);
				}

				if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
					item.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
					item.setMarkupStatus(patientConsession.isMarkupStatus());
				} else {
					item.setPatientClassPercentage(0.00);
					item.setMarkupStatus(false);
				}

				item.setProdCatId(prodCatId);
				item.setAssetCatId(assetCatId);
				response.addItems(item);
			}

		}

		else {

			for (CrashCartRequestDetail detail : CrashCartConsumptionDao.getCrashCartRequestDetail(id)) {
				List<CrashCartBatchMapper> crashCartBatchMappers = detail.getCrashCartBatchMappers();
				List<BatchResponse> batches = new ArrayList<BatchResponse>();
				List<Batch> batchList = GlobalDao.getBatchDetailsPharmacy(detail.getItem().getId(),
						crashCartRequest.getStore().getStoreId(),unitId);
				
				
				for(CrashCartBatchMapper bm : crashCartBatchMappers)
				{
					
					if(detail.getId().equals(bm.getCrashCartRequestDetail().getId())) {
					
					List<BatchResponse> batches1 = new ArrayList<BatchResponse>();
					BatchResponse res = Util.PopulateBatchWithStockWithFlag(bm.getBatch(), true);
					res.setUomTypeId(bm.getUomType().getId());
					res.setUomUnitId(bm.getUomUnit().getId());
					batches1.add(res);
				ItemResponse item = new ItemResponse(detail.getId(), detail.getItem().getId(),
						detail.getItem().getCode(), detail.getItem().getName(), detail.getItem().getGeneric().getId(),
						detail.getItem().getGeneric().getName(), detail.getRequiredQty(), bm.getConsumedQty(),
						batches1, detail.getItem().getIsThreeUOMApplicable());
				item.setUomTypeId(detail.getUomType().getId());
				for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
					List<Integer> prodCatIds = new ArrayList<Integer>();
					Integer productCategoryId = detail.getItem().getGeneric() != null
							? detail.getItem().getGeneric().getProductCategory().getId()
							: detail.getItem().getProductCategory().getId();
					prodCatIds.add(productCategoryId);
					item.setProductCategoryId(productCategoryId);
					item.setProdCatId(productCategoryId);
					List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
							sponsorDetail.getPaymentEntitlementId());
					if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
						if (pharmacyTariffs.size() > 1) {
							// throw exception?
							// Don't throw - get first object and set data.
						}

						PharmacyTariff pharmacyTariff = pharmacyTariffs.getFirst();
						item.setMarkUpDown(pharmacyTariff.getMarkUpDown());
						item.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());

						break;
					}
				}

				Integer assetCatId = 0;
				Integer prodCatId = 0;
				PatientConsession patientConsession = null;
				if (detail.getItem().getProductCategory() != null) {
					prodCatId = detail.getItem().getProductCategory().getId();
					assetCatId = detail.getItem().getProductCategory().getAssetCategory().getId();
				} else {
					prodCatId = detail.getItem().getGeneric().getProductCategory().getId();
					assetCatId = detail.getItem().getGeneric().getProductCategory().getAssetCategory().getId();
				}

				if (crashCartRequest.getAdmission() != null) {
					patientConsession = iglobalDao.getPatientConsessionAdmission(detail.getItem().getId(),
							crashCartRequest.getAdmission().getAdmissionId(),
							crashCartRequest.getAdmission().getPatient().getPatientCategoryId(), assetCatId, prodCatId);
				} else {
					patientConsession = iglobalDao.getPatientConsessionEncounter(detail.getItem().getId(),
							crashCartRequest.getEncounter().getEncounterId(),
							crashCartRequest.getEncounter().getPatient().getPatientCategoryId(), assetCatId, prodCatId,null,null);
				}

				if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
					item.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
					item.setMarkupStatus(patientConsession.isMarkupStatus());
				} else {
					item.setPatientClassPercentage(0.00);
					item.setMarkupStatus(false);
				}

				item.setProdCatId(prodCatId);
				item.setAssetCatId(assetCatId);
				response.addItems(item);
				}
			}
			}
		
		}

		return response;

	}


	private void updateStoreStockAndBatch(StoreStock storeStockObj, int leaseQuantity, Long batchIdFromStore,
			int storeId, CrashCartRequest request, CrashCartRequestDetail d) {

		Batch batch = storeStockObj.getBatch();

		if (batch.getIsConsignment()) {
			
			batch.setCurrentQuantity(batch.getCurrentQuantity() - leaseQuantity);

			storeStockObj.setAvailableQty(storeStockObj.getAvailableQty() - leaseQuantity);
		} else {

			batch.setCurrentQuantity(batch.getCurrentQuantity() - leaseQuantity);
			storeStockObj.setAvailableQty(storeStockObj.getAvailableQty() - leaseQuantity);
		}

		saveStockTransaction(leaseQuantity, storeStockObj , request, d);
	}

	
	private Batch getConsignmentConvertedBatch(Batch batch, Integer leaseQuantity,  CrashCartRequest request) {
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
		newBatch.setAddedDate(TimeZoneComponent.getDateByzone(batch.getUnit().getUnitId()));
		newBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(batch.getUnit().getUnitId()));
		newBatch.setAddedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		newBatch.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
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
			newBatch.addBatchUom(batchUomMapper);
		}

		newBatch = GlobalDao.saveBatch(newBatch);
		return newBatch;
	}

	private Batch getConsignmentConvertedBatchWithConsumption(Batch batch, Integer leaseQuantity, CrashCartRequest request) {
		Batch newBatch = new Batch();
		newBatch.setOrganization(batch.getOrganization());
		newBatch.setUnit(batch.getUnit());
		newBatch.setUomUnit(batch.getUomUnit());
		newBatch.setItem(batch.getItem());
		newBatch.setBatchNo(batch.getBatchNo());
		newBatch.setLotNo(batch.getLotNo());
		newBatch.setQuantity(leaseQuantity);// leaseQuantity
		newBatch.setCurrentQuantity(0);// leaseQuantity
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
		newBatch.setAddedDate(TimeZoneComponent.getDateByzone(0));
		newBatch.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
		newBatch.setAddedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		newBatch.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		newBatch.setOrganizationId(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId());
		newBatch.setUnitId(request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
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
		newBatch.setIsReturn(false);
		newBatch.setRejectedQuantity(0);
		newBatch.setReturnedQuantity(0);
		newBatch.setIsConsignment(false);

		for (BatchUomMapper batchUomMapperObj : batch.getBatchUomList()) {
			BatchUomMapper batchUomMapper = new BatchUomMapper(batchUomMapperObj.getConversions(),
					batchUomMapperObj.getIpUom(), batchUomMapperObj.getOpUom(), batchUomMapperObj.getStoreUom(),
					new UomType(batchUomMapperObj.getUomType().getId()),
					new UomUnit(batchUomMapperObj.getUomUnit().getId()));
			
			batchUomMapper.setAddedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			batchUomMapper.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			batchUomMapper.setOrganizationId(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId());
			batchUomMapper.setUnitId(request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
			newBatch.addBatchUom(batchUomMapper);
		}
		newBatch = GlobalDao.saveBatch(newBatch);
		return newBatch;
	}

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock, CrashCartRequest request, CrashCartRequestDetail d) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), new Store(request.getStoreId()!=null ? request.getStoreId() : sessionService.getStoreId()),
				new Batch(storeStock.getBatch().getId()), d.getId());
		stockTransaction.setTransactionId(request.getId());
		stockTransaction.setOutQty(leaseQuantity);
		stockTransaction.setIsTransit(false);
		stockTransaction.setItem(storeStock.getBatch().getItem());
		//sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);
	}

	@Override
	@Transactional
	public Object saveNewCrashCartApproval(CrashCartRequest request) throws Exception {

		// new method (changes required)
		com.param.entity.model.pharmacy.CrashCartRequest crashCartRequest = CrashCartConsumptionDao
				.getCrashCartRequest(request.getId(),request.getUnitId());

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
				saleType = new SaleType(5);
			else
				throw new RuntimeException("Invalid visit type");
		}

		// Check Contract
		request.setOrganizationId(request.getOrganizationId());
		request.setUnitId(request.getUnitId());
		Double outStandingAmount = request.getNetAmount();
		Double paidAmount = 0.0;

		List itemList = request.getItems();
		request.setItemList(itemList);
		Response billableConsumptionRes = iMPackageMasterService.consumeItemFromPackageContract(request,0);
		CrashCartRequest billableConsumptionContract = (CrashCartRequest) billableConsumptionRes.getObject();

		Double totalSelfPayable = billableConsumptionContract.getItemList().stream()
				.filter(i -> i.getSelfPayable() != null).mapToDouble(i -> i.getSelfPayable()).sum();

		Double totalCreditPayable = billableConsumptionContract.getItemList().stream()
				.filter(i -> i.getCreditPayable() != null).mapToDouble(i -> i.getCreditPayable()).sum();

		Double totalAmount = billableConsumptionContract.getItemList().stream().filter(i -> i.getTotalAmount() != null)
				.mapToDouble(i -> i.getTotalAmount()).sum();

		Double tatalNetAmount = billableConsumptionContract.getItemList().stream()
				.filter(i -> i.getTotalAmount() != null).mapToDouble(i -> i.getTotalAmount()).sum();

		Double totalTax = billableConsumptionContract.getItemList().stream().filter(i -> i.getTotalAmount() != null)
				.mapToDouble(i -> i.getTotalAmount()).sum();

		Sale sale = new Sale(request.getAdmissionId() != null ? new Admission(request.getAdmissionId()) : null, null,
				tatalNetAmount, 0.00,
				new com.param.entity.model.master.Status(com.param.common.constants.Status.Pending.getStatus()),
				new Store(request.getStoreId()), totalTax, totalAmount, tatalNetAmount, null, totalSelfPayable,
				totalCreditPayable, Screen.CrashCartRequestApproval.getScreen());

		sale.setSaleType(saleType);
		sale.setVisitType(visitType);
		if (request.getAdmissionId() != null)
			sale.setAdmission(new Admission(request.getAdmissionId()));
		else if (request.getEncounterId() != null)
			sale.setEncounter(new Encounter(request.getEncounterId()));

		sale.setPatient(new PatientRegistration(request.getPatientId()));

		sale.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
		//sessionService.setEntityDefaults(sale);
		sale.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		for (CrashCartRequestDetail detail : CrashCartConsumptionDao.getCrashCartRequestDetail(request.getId())) {

			for (CommonBillingItemsDto items : request.getItemList()) {
				CrashCartBatchMapper crashCartBatchMapper = new CrashCartBatchMapper(new Batch(items.getBatchId()),
						items.getConsumedQuantity(), new CrashCartRequestDetail(items.getId()));
				detail.setApprovedQty(items.getConsumedQuantity());
				detail.addCrashCartBatchMapper(crashCartBatchMapper);
				//sessionService.setEntityDefaults(crashCartBatchMapper);
				crashCartBatchMapper.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

				Batch batch = GlobalDao.getBatch(request.getStoreId(), items.getBatchId(),null);

				// added by minesh for consignment and consumption logic.
				Integer leaseQuantity = Util.GetLeaseQuantity(items.getConsumedQuantity(), batch.getBatchUomList(),
						items.getUomTypeId());

				// List<StoreStock> StoreStockList =
				// GlobalDao.getStoreStockListByBatchIdAndStoreId(items.getBatchId(),request.getStoreId());
				List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(items.getBatchId(),
						request.getStoreId(), batch.getLotNo(),request.getUnitId());

				Integer totalBatchCurrentQuantity = StoreStockList.stream().mapToInt(x -> x.getAvailableQty()).sum();
				if (totalBatchCurrentQuantity >= leaseQuantity) {
					for (StoreStock storeStockObj : StoreStockList) {
						if (leaseQuantity != 0) {
							Integer storeAvaliableQuantity = storeStockObj.getAvailableQty();
							Long batchIdFromStore = storeStockObj.getBatch().getId();
							if (leaseQuantity <= storeAvaliableQuantity) {
								updateStoreStockAndBatch(storeStockObj, leaseQuantity, batchIdFromStore,
										request.getStoreId(), request, detail);
								leaseQuantity = 0;
								break;
							} else {
								int avalibleLeaseQuantity = storeStockObj.getAvailableQty();
								updateStoreStockAndBatch(storeStockObj, avalibleLeaseQuantity, batchIdFromStore,
										request.getStoreId(),request, detail);
								leaseQuantity = leaseQuantity - avalibleLeaseQuantity;
							}
						}
					}
				} else {
					throw new CustomException("Return quantity must be less than current quantity");
				}
				// code end by minesh for consignment and consumption logic.

				SaleDetail saleDetail = new SaleDetail(batch, /* items.getDiscountAmount() */ Double.valueOf(0),
						items.getLeaseQuantity(), items.getNetAmount(), /* items.getOrderDetailId() */ null,
						/*
						 * (items.getPrescriptionDetailId() != null) ? new
						 * PrescriptionDetail(items.getPrescriptionDetailId()) :
						 */ null, items.getQuantity(), items.getRate(), items.getTaxAmount(), new Tax(items.getTaxId()),
						items.getTaxPercentage(), items.getTotalAmount(),
						/* items.getUnitDiscountAmount() */Double.valueOf(0),
						items.getUomTypeId() != null ? new UomType(items.getUomTypeId()) : null,
						items.getUomUnitId() != null ? new UomUnit(items.getUomUnitId()) : null,
						new Status(com.param.common.constants.Status.Pending.getStatus()), items.getSelfPayable(),
						items.getCreditPayable(), items.getPayeeId(), items.getProductCategoryId(),
						items.getPackageConsumptionAmt());

				saleDetail.setConsessionAmount(items.getConsessionAmount());
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

				//sessionService.setEntityDefaults(saleDetail);
				saleDetail.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

				sale.addSaleDetailsList(saleDetail);

			}
		}
		sale.setFromBillableConsumption(false);
		iWalkInSalesService.saveGlobalSale(sale);
		crashCartRequest.setSale(sale);
		crashCartRequest.setStatus(new Status(request.getStatusId()));
		CrashCartApprovalDao.saveCrashCartRequestApproval(crashCartRequest);

		CommonResponse response = new CommonResponse();
		response.setId(crashCartRequest.getId().longValue());
		response.setMessage("Crash Cart Request Approval saved successfully");

		return response;

	}

}
