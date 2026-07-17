package com.param.pharmacy.ip.service.patientIssueRejection;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.FloorMasters;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.RejectDetail;
import com.param.entity.model.pharmacy.RejectedRejection;
import com.param.entity.model.pharmacy.RejectedRejectionDetail;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.dto.global.DoctorSpecialityResp;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.patientIssueRejection.IPatientIssuesRejectionMedicationDao;
import com.param.pharmacy.ip.dao.rejectedRejection.IRejectedRejectionMedicationDao;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionItem;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionSearch;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionSearchResult;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSaveItems;
import com.param.pharmacy.ip.dto.returnAcceptance.PharmacySaleReturnDto;
import com.param.pharmacy.nursing.workstation.dao.patientIssueReturn.IPatientIssueReturnDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("patientIssuesRejectionMedicationService")
@Transactional
public class PatientPendingIssuesRejectionService implements IPatientPendingIssuesRejectionService {

	private static final Logger logger = Logger.getLogger(PatientPendingIssuesRejectionService.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	private IPatientIssuesRejectionMedicationDao patientIssuesRejectionMedicationDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	private Integer screenId = Screen.PatientIssueRejection.getScreen();
	@Autowired
	private IRejectedRejectionMedicationDao rejectedRejectionMedicationDao;
	
	@Autowired
	private IPatientIssueReturnDao patientIssueReturnDao;
	@Autowired
	private IWalkInSalesService iWalkInSalesService;

	@Override
	@Transactional
	public CommonDropdown getDropdownList(Integer unitId) {
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (FloorMasters floor : GlobalDao.getFloorList(unitId)) {
			response.addFloor(new com.param.global.dto.global.Floor(floor.getFloorId(), floor.getFloorName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<PatientIssueRejectionSearchResult> patientIssuesRejectionMedicationSearchPT(
			PatientIssueRejectionSearch patientIssueRejectionSearch) {
		patientIssueRejectionSearch
				.setToDate((new DateTime(patientIssueRejectionSearch.getToDate())).plusDays(1).toDate());
		List<PatientIssueRejectionSearchResult> patientIssueRejectionSearchResult = new ArrayList<PatientIssueRejectionSearchResult>();

		patientIssueRejectionSearchResult = patientIssuesRejectionMedicationDao
				.patientIssuesRejectionMedicationSearchPT(patientIssueRejectionSearch, screenId);
		if (patientIssueRejectionSearchResult != null && !patientIssueRejectionSearchResult.isEmpty()) {
			/*
			 * List<Integer> doctorIds = patientIssueRejectionSearchResult.stream().map(
			 * PatientIssueRejectionSearchResult::getDoctorId)
			 * .collect(Collectors.toList());
			 */
			// List<DoctorSpecialityResp> doctorSpecialityRespsAll =
			// GlobalDao.doctorSearchByIdValueBP(doctorIds);

			for (PatientIssueRejectionSearchResult searchRes : patientIssueRejectionSearchResult) {
				List<DoctorSpecialityResp> doctorSpeciality = new ArrayList<DoctorSpecialityResp>();
				/*
				 * if(!doctorSpecialityRespsAll.isEmpty()) {
				 */
				/*
				 * doctorSpeciality = doctorSpecialityRespsAll.stream().filter(x ->
				 * x.getDoctorId().equals(searchRes.getDoctorId())).collect(Collectors.toList())
				 * ; if(doctorSpeciality != null && !doctorSpeciality.isEmpty())
				 * searchRes.setDoctorDetail(searchRes.getDoctorDetail() + " | " +
				 * doctorSpeciality.get(0).getSpeciality());
				 */
				String doctorDetail = null;
				if(searchRes.getDoctorId()!=null) {
				Doctor dr = GlobalDao.GetDoctorById(searchRes.getDoctorId());
				if (searchRes.getDoctorDetail() == null) {
					doctorDetail = dr.getFirstName() + " " + dr.getMiddleName() + " " + dr.getLastName();
					DoctorSpecialityResp doctorSpecialityResp = GlobalDao
							.getDoctorSpecialityResp(searchRes.getDoctorId(),patientIssueRejectionSearch.getUnitId());
					if (doctorSpecialityResp != null) {
						doctorDetail = doctorDetail + " | " + doctorSpecialityResp.getSpeciality();
					}
					searchRes.setDoctorDetail(doctorDetail);
					}
				}
				if (searchRes.getAdmissionId() != null) {

					Integer age = Util
							.getAgeByBirthDate(searchRes.getAdmissionPatDob().toLocalDateTime().toLocalDate());

					searchRes.setPatientDetail(searchRes.getAdmittedPatientDetails() + " | " + age);
					searchRes.setMedicationType(searchRes.getMedicationType());
					searchRes.setUhid(searchRes.getAdmittedPatientUhid());
				} else if (searchRes.getEncounterId() != null) {

					Integer age = Util
							.getAgeByBirthDate(searchRes.getPatientEncounterDob().toLocalDateTime().toLocalDate());

					searchRes.setPatientDetail(searchRes.getEncounterPatientDetails() + " | " + age);
					searchRes.setMedicationType(searchRes.getMedicationType());
					searchRes.setPatientDetail(searchRes.getEncounterPatientDetails());
					searchRes.setUhid(searchRes.getEncounterPatientUhid());
				}
				// }
			}
		}

		return patientIssueRejectionSearchResult;
	}

	@Override
	@Transactional
	public PatientIssueRejectionSearchResult patientIssuesRejectionMedicationDetails(Integer id) {
		Reject reject = patientIssuesRejectionMedicationDao.patientIssuesRejectionMedicationById(id);

		String doctorDetail = "";
		Integer admissionId=0;
		Integer encounterId=0;
		if (reject.getIssue().getDoctor() != null) {
			doctorDetail = reject.getIssue().getDoctor().getFirstName() + " "
					+ reject.getIssue().getDoctor().getMiddleName() + " " + reject.getIssue().getDoctor().getLastName();
		}

		if (reject.getIssue().getSale().getSpecialityMaster() != null) {
			doctorDetail = doctorDetail + " | " + reject.getIssue().getSale().getSpecialityMaster().getSpecialityName();
		}

		PatientIssueRejectionSearchResult patientIssueRejection = new PatientIssueRejectionSearchResult();
		if (reject.getIssue().getAdmission() != null) {
			String wardName = "";
			String bedNumber = "";
			Integer wardId = 0;
			admissionId=reject.getIssue().getAdmission().getAdmissionId();
			AdmissionDetail admissionDetail = GlobalDao
					.getPatientWardBedDetails(reject.getIssue().getAdmission().getAdmissionId());
			if (admissionDetail != null) {
				WardMasters wardMaster = admissionDetail.getWardMaster();
				if (wardMaster != null) {
					wardName = wardMaster.getWardName();
					wardId = wardMaster.getWardId();
				}
				BedMaster bedMaster = admissionDetail.getBedMaster();
				if (bedMaster != null) {
					bedNumber = bedMaster.getBedNumber();
				}
			}
			Integer age = Util.getAgeByBirthDate(
					reject.getIssue().getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = reject.getIssue().getAdmission().getPatient().getPrefix().getPrefixCode() + " "
					+ reject.getIssue().getAdmission().getPatient().getFirstName() + " "
					+ reject.getIssue().getAdmission().getPatient().getMiddleName() + " "
					+ reject.getIssue().getAdmission().getPatient().getLastName() + " | " + age + " | "
					+ reject.getIssue().getAdmission().getPatient().getGender().getGenderCode();
			patientIssueRejection = new PatientIssueRejectionSearchResult(reject.getId(), reject.getIssue().getId(),
					reject.getRejectNo(), reject.getRejectedDate(), reject.getIssue().getIssueNo(),
					reject.getIssue().getAdmission().getPatient().getUhidNumber(), patientDetail, doctorDetail,
					reject.getIssue().getStore().getStoreName());
			patientIssueRejection.setStatus(reject.getStatus().getStatus());
			patientIssueRejection.setStatusId(reject.getStatus().getId());
			patientIssueRejection.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			patientIssueRejection.setWardId(wardId != 0 ? wardId : null);
			patientIssueRejection.setWardName(!wardName.equals("") ? wardName : null);
		} else if (reject.getIssue().getEncounter() != null) {
			encounterId=reject.getIssue().getEncounter().getEncounterId();
			Integer age = Util.getAgeByBirthDate(
					reject.getIssue().getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = reject.getIssue().getEncounter().getPatient().getPrefix().getPrefixCode() + " "
					+ reject.getIssue().getEncounter().getPatient().getFirstName() + " "
					+ reject.getIssue().getEncounter().getPatient().getMiddleName() + " "
					+ reject.getIssue().getEncounter().getPatient().getLastName() + " | " + age + " | "
					+ reject.getIssue().getEncounter().getPatient().getGender().getGenderCode();
			patientIssueRejection = new PatientIssueRejectionSearchResult(reject.getId(), reject.getIssue().getId(),
					reject.getRejectNo(), reject.getRejectedDate(), reject.getIssue().getIssueNo(),
					reject.getIssue().getEncounter().getPatient().getUhidNumber(), patientDetail, doctorDetail,
					reject.getIssue().getToStore()!=null ? reject.getIssue().getToStore().getStoreName()  :reject.getIssue().getStore().getStoreName());
			patientIssueRejection.setStatus(reject.getStatus().getStatus());
			patientIssueRejection.setStatusId(reject.getStatus().getId()); 
			patientIssueRejection.setStoreId(reject.getIssue().getToStore()!=null ? reject.getIssue().getToStore().getStoreId()  :reject.getIssue().getStore().getStoreId());
		}

		List<PatientIssueRejectionItem> items = new ArrayList<PatientIssueRejectionItem>();
		for (RejectDetail rejectDetail : patientIssuesRejectionMedicationDao
				.patientIssuesRejectionMedicationDetailById(id)) {
			boolean atCop= rejectDetail.getIssueDetailBatchMapper().getIssueDetail().getSaleDetail().getAtCop();
			IssueDetailBatchMapper batchMapper = rejectDetail.getIssueDetailBatchMapper();
			PatientIssueRejectionItem item = new PatientIssueRejectionItem(rejectDetail.getId(),
					batchMapper.getBatch().getItem().getName(), batchMapper.getBatch().getItem().getGeneric().getName(),
					batchMapper.getBatch().getItem().getIsOtc(), batchMapper.getUomType().getType(),
					batchMapper.getUomUnit().getUnits(), batchMapper.getBatch().getBatchNo(),
					batchMapper.getBatch().getExpiryDate(), (atCop==true ? batchMapper.getBatch().getActualLendingPrice():batchMapper.getBatch().getSellingPrice()),
					rejectDetail.getIssueRejectQuantity(),
					(rejectDetail.getRejectReason() != null) ? rejectDetail.getRejectReason().getReason() : null,
					rejectDetail.getIssueDetailBatchMapper().getId(), batchMapper.getBatch().getItem().getIsThreeUOMApplicable(), 
					GlobalDao.getUnitOfMeasurementListByItem(batchMapper.getBatch().getItem().getId(), reject.getOrganizationId()));
			item.setStatus(rejectDetail.getStatus().getStatus());
			item.setStatusId(rejectDetail.getStatus().getId());
			item.setPharmReasonId(
					rejectDetail.getPharmRejectReason() != null ? rejectDetail.getPharmRejectReason().getId() : null);
			item.setPharmReasonName(
					rejectDetail.getPharmRejectReason() != null ? rejectDetail.getPharmRejectReason().getReason()
							: null);
			item.setGenericId( batchMapper.getBatch().getItem().getGeneric().getId());
			item.setAcceptQuantity(
					rejectDetail.getAcceptedQuantity() != null ? rejectDetail.getAcceptedQuantity() : null);
			item.setRejectQuantity(
					rejectDetail.getRejectedQuantity() != null ? rejectDetail.getRejectedQuantity() : null);
			item.setRejectNote(rejectDetail.getRejectedNote() != null ? rejectDetail.getRejectedNote() : null);
			item.setRejectReasonId(
					(rejectDetail.getRejectReason() != null) ? rejectDetail.getRejectReason().getId() : null);
			// item.setQty(rejectDetail.getRejectedQuantity());
			// item.setPatientIssueRejection(rejectDetail.getIsPatientIssueRejection());
			// chanage code for resolved HIS-10707 After click on details issue rejection
			// details not showing on patient issue rejection screen.
			item.setPatientIssueRejection(
					(rejectDetail.getIsPatientIssueRejection() != null) ? rejectDetail.getIsPatientIssueRejection()
							: true);
			List<PatientSponsorDetails> patientSponsorDetails = null;
			if(encounterId>0)
			{
				patientSponsorDetails = patientIssuesRejectionMedicationDao.findByEncounterId(encounterId);
			}
			else
			{
				patientSponsorDetails = patientIssuesRejectionMedicationDao.findByAdmissionId(admissionId);
			}
			    Integer productCategoryId = null;
				productCategoryId = batchMapper.getBatch().getItem().getGeneric().getProductCategory().getId();
				if(patientSponsorDetails != null && productCategoryId != null) 
				{
					for(PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(productCategoryId);
						
						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds, sponsorDetail.getPaymentEntitlementId());
						if(pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							if(pharmacyTariffs.size() > 1){
								//throw exception?
								//Don't throw - get first object and set data.
							}
							
							PharmacyTariff pharmacyTariff = pharmacyTariffs.getFirst();
							item.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							item.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
							break;
						}
					}
					
				}
			// true referenced by patientIssuesRejectionMedicationSave method

			items.add(item);
		}
		patientIssueRejection.setItems(items);
		return patientIssueRejection;
	}

	@Override
	@Transactional
	public Object patientIssuesRejectionMedicationSave(PendingIssuesAcceptanceSave patientIssueRejectionSave) {
		Reject reject = patientIssuesRejectionMedicationDao
				.patientIssuesRejectionMedicationById(patientIssueRejectionSave.getId());
		//For return
				BillingMasterDto dto = new BillingMasterDto();
				dto.setOrganisationId(reject.getOrganization().getOrganizationId());
				dto.setUnitId(reject.getUnit().getUnitId());
				dto.setAdmissionId(reject.getIssue().getAdmission()!=null?reject.getIssue().getAdmission().getAdmissionId():null);
				dto.setEncounterId(reject.getIssue().getEncounter()!=null?reject.getIssue().getEncounter().getEncounterId():null);
				
				if(dto.getAdmissionId()!=null && dto.getAdmissionId()>0)
				{
					dto.setPatientId(reject.getIssue().getAdmission().getPatient()!=null?reject.getIssue().getAdmission().getPatient().getPatientId():null);
				    dto.setVisitTypeId(reject.getIssue().getAdmission().getVisitType().getVisitTypeId());
				}
				else if(dto.getEncounterId()!=null && dto.getEncounterId()>0) {
					dto.setPatientId(reject.getIssue().getEncounter().getPatient()!=null?reject.getIssue().getEncounter().getPatient().getPatientId():null);
				    dto.setVisitTypeId(reject.getIssue().getVisitType().getVisitTypeId());
				}
				dto.setCreatedBy(reject.getAddedByUser()!=null ? reject.getAddedByUser() : sessionService.getUserId());
				List<OrderDetailsMasterDto> orderDetailsList = new ArrayList<OrderDetailsMasterDto>();	
		for (PendingIssuesAcceptanceSaveItems rejectDetailItem : patientIssueRejectionSave.getItems()) 
		{
			RejectDetail rejectDetail = patientIssuesRejectionMedicationDao
					.patientIssuesRejectionMedicationDetailByIdValue(rejectDetailItem.getId());
			rejectDetail.setAcceptedQuantity(
					rejectDetailItem.getAcceptQuantity() != null ? rejectDetailItem.getAcceptQuantity() : null);
			rejectDetail.setRejectedQuantity(
					(rejectDetailItem.getRejectQuantity() != null) ? rejectDetailItem.getRejectQuantity() : null);
			rejectDetail.setRejectReason(
					(rejectDetailItem.getReasonId() != null) ? new RejectReason(rejectDetailItem.getReasonId()) : null);
			rejectDetail.setRejectedNote((rejectDetailItem.getNote() != null) ? rejectDetailItem.getNote() : null);
			rejectDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(reject.getUnitId()));
			rejectDetail.setUpdatedByUser(patientIssueRejectionSave.getUserId()!=null ? patientIssueRejectionSave.getUserId() :sessionService.getUserId());
			rejectDetail.setIsPatientIssueRejection(true);
			if (rejectDetailItem.getStatusId() != null) {
				rejectDetail.setStatus(new Status(rejectDetailItem.getStatusId()));
			}

			rejectDetail.setPharmRejectReason((rejectDetailItem.getPharmReasonId() != null)
					? new RejectReason(rejectDetailItem.getPharmReasonId())
					: null);

			reject.addRejectDetail(rejectDetail);
			
			
 
			PharmacySaleReturnDto phSRDto = patientIssueReturnDao
					.getSaleDetailIdByIssueDetailMapper(rejectDetail.getIssueDetailBatchMapper().getId());


			if(phSRDto!=null && rejectDetailItem.getAcceptQuantity()!=null && rejectDetailItem.getAcceptQuantity() > 0)
			{
				OrderDetailsMasterDto orderDtlsDto = new OrderDetailsMasterDto();
				orderDtlsDto.setSaleDetailId(phSRDto.getSaleDetailId().intValue());
				orderDtlsDto.setSaleId(phSRDto.getSaleId()!=null ? phSRDto.getSaleId().intValue():null);
				orderDtlsDto.setOrgId(dto.getOrganisationId());
				orderDtlsDto.setOrgUnitId(dto.getUnitId());
				orderDtlsDto.setVisitTypeId(dto.getVisitTypeId());
				orderDtlsDto.setOrderQty(rejectDetailItem.getAcceptQuantity());
				orderDtlsDto.setBatchId(phSRDto.getBatchId().intValue());
				orderDtlsDto.setDrugId(phSRDto.getItemId());
				orderDtlsDto.setProductCategoryId(phSRDto.getProductCategoryId());
				orderDetailsList.add(orderDtlsDto);	
			}
	     
		}
		if (patientIssueRejectionSave.getStatusId() != null) {
			reject.setStatus(new Status(patientIssueRejectionSave.getStatusId()));
		}
		reject.setUpdatedDate(TimeZoneComponent.getDateByzone(reject.getUnitId()));
		reject.setUpdatedByUser(patientIssueRejectionSave.getUserId()!=null ? patientIssueRejectionSave.getUserId() :sessionService.getUserId());
		patientIssuesRejectionMedicationDao.patientIssuesRejectionMedicationSave(reject);
		//sales return 
		try {
			if(!orderDetailsList.isEmpty())
			{
				iWalkInSalesService.pharamcyPackageContractReturn(dto, orderDetailsList);	
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
		saveRejectedRejectionDetails(patientIssueRejectionSave);
		saveStoreStockDetails(patientIssueRejectionSave,reject);

		CommonResponse response = new CommonResponse();
		response.setId(reject.getId().longValue());
		response.setMessage("Issue Rejection Medication saved successfully");

		return response;
	}

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock, PendingIssuesAcceptanceSave patientIssueRejectionSave) {

		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		Batch batch = GlobalDao.getBatch(storeStock.getBatch().getId());
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), new Store(sessionService.getStoreId()), batch,0);
	//	sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(patientIssueRejectionSave.getOrganizationId()!=null ? patientIssueRejectionSave.getOrganizationId() : sessionService.getOrganizationId(),patientIssueRejectionSave.getUnitId() !=null ? patientIssueRejectionSave.getUnitId() :sessionService.getUnitId() ,patientIssueRejectionSave.getUserId()!=null ? patientIssueRejectionSave.getUserId() : sessionService.getUserId());
		
		GlobalDao.saveStockTransaction(stockTransaction);
	}

	private void saveStoreStockDetails(PendingIssuesAcceptanceSave patientIssueRejectionSave , Reject reject) {
		for (PendingIssuesAcceptanceSaveItems rejectDetailItem : patientIssueRejectionSave.getItems()) {
			if (rejectDetailItem.getAcceptQuantity() > 0) {
				IssueDetailBatchMapper batchMapper = GlobalDao
						.getIssueDetailBatchMapperById(rejectDetailItem.getBatchMapperId());

				batchMapper.updateIssueDetailBatchMapper(rejectDetailItem.getBatchMapperId(),
						(rejectDetailItem.getNote() != null) ? rejectDetailItem.getNote() : null,
						(rejectDetailItem.getReasonId() != null) ? new RejectReason(rejectDetailItem.getReasonId())
								: null);
				batchMapper.setRejectionAcceptedQuantity(rejectDetailItem.getAcceptQuantity()!=null ? (batchMapper.getRejectionAcceptedQuantity()!=null ? batchMapper.getRejectionAcceptedQuantity() : 0)+rejectDetailItem.getAcceptQuantity() :null);
				batchMapper.setRejectionRejectedQuantity(rejectDetailItem.getRejectQuantity()!=null ? (batchMapper.getRejectionRejectedQuantity()!=null ? batchMapper.getRejectionRejectedQuantity() :0)+ rejectDetailItem.getRejectQuantity() :null);
				Integer leaseQuantity = Util.GetLeaseQuantity(rejectDetailItem.getAcceptQuantity(),
						batchMapper.getBatch().getBatchUomList(), batchMapper.getUomType());
				Integer leaseRejQuantity = Util.GetLeaseQuantity(rejectDetailItem.getRejectQuantity()!=null ?rejectDetailItem.getRejectQuantity():0,
						batchMapper.getBatch().getBatchUomList(), batchMapper.getUomType());
				
				List<StoreStock> storeStockList1=GlobalDao.getStoreStockByBatchAndStoreList(batchMapper.getBatch().getId(),
						patientIssueRejectionSave.getStoreId(),patientIssueRejectionSave.getUnitId());
				
				/*StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(batchMapper.getBatch().getId(),
						patientIssueRejectionSave.getStoreId(),patientIssueRejectionSave.getUnitId());*/
				 
				for(StoreStock s : storeStockList1)
				{
					if(s.getStore().getStoreId().equals(patientIssueRejectionSave.getStoreId()))
					{
						Integer stockInTransit=(s.gettQty()!=null ? s.gettQty():0)-leaseQuantity;
						s.settQty(stockInTransit < 0 ? 0 : stockInTransit);
						Integer availableStock=(s.getAvailableQty()!=null ? s.getAvailableQty():0)+leaseQuantity;
						s.setAvailableQty(availableStock < 0 ? 0 :  availableStock);
						s=GlobalDao.updateStoreStockLeaseQuantity(s);
						com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
						Batch batch = GlobalDao.getBatch(s.getBatch().getId());
						StockTransaction stockTransaction = new StockTransaction(s.getAvailableQty(),
								new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseRejQuantity,
								s.getBatch().getRate(), s.getStore(), batch,batchMapper.getId());
					 	stockTransaction.setIsTransit(false);
					     stockTransaction.setItem(batch.getItem());
						stockTransaction.setTransitNo(reject.getRejectNo());
						stockTransaction.setInQty(leaseQuantity);
						stockTransaction.setTransactionId(reject.getId());
					//	sessionService.setEntityDefaults(stockTransaction);
						stockTransaction.setCreateDefaults(patientIssueRejectionSave.getOrganizationId()!=null ? patientIssueRejectionSave.getOrganizationId() : sessionService.getOrganizationId(),patientIssueRejectionSave.getUnitId() !=null ? patientIssueRejectionSave.getUnitId() :sessionService.getUnitId() ,patientIssueRejectionSave.getUserId()!=null ? patientIssueRejectionSave.getUserId() : sessionService.getUserId());
						
						GlobalDao.saveStockTransaction(stockTransaction);
						//saveStockTransaction(leaseQuantity, s, patientIssueRejectionSave);
					}
					
					
				}
				// as discussed with divyesh rahul
			/*	if (storeStockList == null) {
					StoreStock storeStock = new StoreStock(leaseQuantity, new Store(patientIssueRejectionSave.getStoreId()),
							new Batch(batchMapper.getBatch().getId()));
					storeStock.settQty(rejectDetailItem.getAcceptQuantity());
					storeStock.setTotalQty(leaseQuantity);
					//sessionService.setEntityDefaults(storeStock);
					storeStock.setCreateDefaults(patientIssueRejectionSave.getOrganizationId()!=null ? patientIssueRejectionSave.getOrganizationId() : sessionService.getOrganizationId(),patientIssueRejectionSave.getUnitId() !=null ? patientIssueRejectionSave.getUnitId() :sessionService.getUnitId() ,patientIssueRejectionSave.getUserId()!=null ? patientIssueRejectionSave.getUserId() : sessionService.getUserId());
					storeStockDetail = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
				} else {
					tQuantity = leaseQuantity - storeStockList.gettQty();
					availableQuantity = leaseQuantity + storeStockList.getAvailableQty();
					storeStockList.setAvailableQty(availableQuantity);

					Integer qty = 0;
					if (storeStockList.gettQty() != null) {
						qty = storeStockList.gettQty();
					}
					if(qty.equals(0))
					{
						storeStockList.settQty(qty);	
					}
					else if(qty >=rejectDetailItem.getAcceptQuantity())
					{
						storeStockList.settQty(qty - rejectDetailItem.getAcceptQuantity());
					}
					
					// storeStockList.settQty(tQuantity);
					storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
				}*/
		/*		Batch batchObject = GlobalDao.getBatch(patientIssueRejectionSave.getStoreId(),
						storeStockDetail.getBatch().getId(),patientIssueRejectionSave.getUnitId());
				currentQuantity = batchObject.getCurrentQuantity() + leaseQuantity;
				batchObject.setCurrentQuantity(currentQuantity);
				GlobalDao.updateBatchLeaseQuantity(batchObject);
				saveStockTransaction(leaseQuantity, storeStockDetail, patientIssueRejectionSave);*/

			}
		}

	}

	private void saveRejectedRejectionDetails(PendingIssuesAcceptanceSave patientIssueRejectionSave) {
		boolean flag = false;

		/*
		 * Integer storeId = sessionService.getStoreId();
		 * 
		 * Store storeNew = GlobalDao.getStore(storeId);
		 * 
		 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		 */

		String rejectBillNumber = GlobalService.getBillNoByGeneratedCode(patientIssueRejectionSave.getStoreId(),
				patientIssueRejectionSave.getUnitId(), screenId);

		RejectedRejection rejectedRejection = new RejectedRejection(rejectBillNumber,
				new Reject(patientIssueRejectionSave.getId()), new Store(patientIssueRejectionSave.getStoreId()),
				new Issue(patientIssueRejectionSave.getIssueId()),
				new Status(com.param.common.constants.Status.Pending.getStatus()));
		if(patientIssueRejectionSave.getWardId()!=null)
		{
			rejectedRejection.setWard(new WardMasters(patientIssueRejectionSave.getWardId()));
				
		}
		//sessionService.setEntityDefaults(rejectedRejection);
		rejectedRejection.setCreateDefaults(patientIssueRejectionSave.getOrganizationId()!=null ? patientIssueRejectionSave.getOrganizationId() : sessionService.getOrganizationId(),patientIssueRejectionSave.getUnitId() !=null ? patientIssueRejectionSave.getUnitId() :sessionService.getUnitId() ,patientIssueRejectionSave.getUserId()!=null ? patientIssueRejectionSave.getUserId() : sessionService.getUserId());
		
		for (PendingIssuesAcceptanceSaveItems rejectDetailItem : patientIssueRejectionSave.getItems()) {
			if (rejectDetailItem.getRejectQuantity() > 0) {
				flag = true;
				RejectedRejectionDetail rejectedRejectionDetail = new RejectedRejectionDetail(
						new RejectDetail(rejectDetailItem.getId()),
						new IssueDetailBatchMapper(rejectDetailItem.getBatchMapperId()),  null,
						(rejectDetailItem.getRejectQuantity() != null) ? rejectDetailItem.getRejectQuantity() : null,
						(rejectDetailItem.getReasonId() != null) ? new RejectReason(rejectDetailItem.getReasonId())
								: null,
						(rejectDetailItem.getNote() != null) ? rejectDetailItem.getNote() : null,
						new Status(com.param.common.constants.Status.Pending.getStatus()));
				rejectedRejection.addRejectedRejectionDetail(rejectedRejectionDetail);
				//sessionService.setEntityDefaults(rejectedRejectionDetail);
				rejectedRejectionDetail.setCreateDefaults(patientIssueRejectionSave.getOrganizationId()!=null ? patientIssueRejectionSave.getOrganizationId() : sessionService.getOrganizationId(),patientIssueRejectionSave.getUnitId() !=null ? patientIssueRejectionSave.getUnitId() :sessionService.getUnitId() ,patientIssueRejectionSave.getUserId()!=null ? patientIssueRejectionSave.getUserId() : sessionService.getUserId());
				
				// status column need to be added in detail

			}
		}
		if (flag == true) {
			patientIssuesRejectionMedicationDao.patientIssuesRejectedRejectionMedicationSave(rejectedRejection);
		}

	}

}