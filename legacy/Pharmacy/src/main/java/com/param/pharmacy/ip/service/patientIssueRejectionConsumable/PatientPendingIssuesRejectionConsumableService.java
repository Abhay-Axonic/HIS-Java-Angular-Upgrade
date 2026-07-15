package com.param.pharmacy.ip.service.patientIssueRejectionConsumable;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.RejectDetail;
import com.param.entity.model.pharmacy.RejectedRejection;
import com.param.entity.model.pharmacy.RejectedRejectionDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.patientIssueRejectionConsumable.IPatientIssuesRejectionConsumableDao;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionItem;
import com.param.pharmacy.ip.dto.patientIssueRejection.PatientIssueRejectionSearchResult;
import com.param.pharmacy.ip.dto.patientIssueRejectionConsumable.PatientIssueRejectionConsumableSearch;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSaveItems;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("patientIssuesRejectionConsumableService")
@Transactional
public class PatientPendingIssuesRejectionConsumableService implements IPatientPendingIssuesRejectionConsumableService {
	@Autowired
	private IPatientIssuesRejectionConsumableDao patientIssuesRejectionConsumableDao;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	private IGlobalService GlobalService;

	private Integer screenId = Screen.IssueRejection.getScreen();

	private Integer rejectScreenId = Screen.RejectedRejection.getScreen();

	@Override
	@Transactional
	public CommonDropdown getDropdownList(Integer unitId) {
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (WardMasters wardMaster : GlobalDao.getWardList(unitId)) {
			response.addWard(new com.param.global.dto.global.Ward(wardMaster.getWardId(), wardMaster.getWardName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<PatientIssueRejectionSearchResult> patientIssuesRejectionConsumableSearch(
			PatientIssueRejectionConsumableSearch patientIssueRejectionSearch) {
		patientIssueRejectionSearch
				.setToDate((new DateTime(patientIssueRejectionSearch.getToDate())).plusDays(1).toDate());
		List<PatientIssueRejectionSearchResult> patientIssueRejectionSearchResult = new ArrayList<PatientIssueRejectionSearchResult>();
		for (Reject reject : patientIssuesRejectionConsumableDao
				.patientIssuesRejectionConsumableSearch(patientIssueRejectionSearch, screenId)) 
		{
			String doctorDetail ="";
			if(reject.getIssue().getDoctor()!=null)
			{
				doctorDetail = reject.getIssue().getDoctor().getFirstName() + " "
							+ reject.getIssue().getDoctor().getMiddleName() + " " + reject.getIssue().getDoctor().getLastName();
						//	+ " || " + reject.getIssue().getSale().getSpecialityMaster().getSpecialityName();
			}
			if(reject.getIssue().getSale().getSpecialityMaster()!=null)
			{
				doctorDetail = doctorDetail+ " || " + reject.getIssue().getSale().getSpecialityMaster().getSpecialityName();
			}	
			if (reject.getIssue().getAdmission() != null) {
				String wardName = "";
				String bedNumber = "";
				AdmissionDetail admissionDetail = GlobalDao
						.getPatientWardBedDetails(reject.getIssue().getAdmission().getAdmissionId());
				if (admissionDetail != null) {
					WardMasters wardMaster = admissionDetail.getWardMaster();
					if (wardMaster != null) {
						wardName = wardMaster.getWardName();
					}
					BedMaster bedMaster = admissionDetail.getBedMaster();
					if (bedMaster != null) {
						bedNumber = bedMaster.getBedNumber();
					}
				}

				Integer age = Util.getAgeByBirthDate(
						reject.getIssue().getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
				Integer ageString = Util.getAgeByBirthDate(
						reject.getIssue().getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = reject.getIssue().getAdmission().getPatient().getPrefix().getPrefixCode() + " "
						+ reject.getIssue().getAdmission().getPatient().getFirstName() + " "
						+ reject.getIssue().getAdmission().getPatient().getMiddleName() + " "
						+ reject.getIssue().getAdmission().getPatient().getLastName() + " | " + age + " | "
						+ reject.getIssue().getAdmission().getPatient().getGender().getGenderCode()+" | "+reject.getIssue().getAdmission().getPatient().getUhidNumber();
//				reject.getIssue().getAdmission().getPatient().getGender().getGenderId();
//				reject.getIssue().getAdmission().getPatient().getImagePath();
				String patientName = reject.getIssue().getAdmission().getPatient().getPrefix().getPrefixCode() + " "
						+ reject.getIssue().getAdmission().getPatient().getFirstName() + " "
						+ reject.getIssue().getAdmission().getPatient().getMiddleName() + " "
						+ reject.getIssue().getAdmission().getPatient().getLastName();
				reject.getIssue().getAdmission().getPatient().getGender().getGenderCode();
				PatientIssueRejectionSearchResult patientIssueRejection = new PatientIssueRejectionSearchResult(
						reject.getId(), reject.getRejectNo(), reject.getRejectedDate(),
						reject.getIssue().getPatientIndent().getIndentNo(),
						reject.getIssue().getAdmission().getPatient().getUhidNumber(), patientDetail, doctorDetail,
						reject.getIssue().getAdmission().getAdmissionNumber(),
						reject.getIssue().getAdmission().getVisitType().getVisitTypeCode(),
						reject.getIssue().getStore().getStoreName(), patientName, ageString);
				patientIssueRejection.setWardName(!wardName.equals("") ? wardName : null);
				patientIssueRejection.setBedNo(!bedNumber.equals("") ? bedNumber : null);
				patientIssueRejection.setStatus(reject.getStatus().getStatus());
				patientIssueRejection.setStatusId(reject.getStatus().getId());
				patientIssueRejection.setImagePath(reject.getIssue().getAdmission().getPatient().getImagePath());
				patientIssueRejection.setGenderCode(reject.getIssue().getAdmission().getPatient().getGender().getGenderCode());
				patientIssueRejection.setGenderId(reject.getIssue().getAdmission().getPatient().getGender().getGenderId());
				patientIssueRejection.setToStore(reject.getIssue().getToStore().getStoreName());

				patientIssueRejectionSearchResult.add(patientIssueRejection);
			} else if (reject.getIssue().getEncounter() != null) {
				Integer age = Util.getAgeByBirthDate(
						reject.getIssue().getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
				Integer ageString = Util.getAgeByBirthDate(
						reject.getIssue().getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
				
				String patientDetail = reject.getIssue().getEncounter().getPatient().getPrefix().getPrefixCode() + " "
						+ reject.getIssue().getEncounter().getPatient().getFirstName() + " "
						+ reject.getIssue().getEncounter().getPatient().getMiddleName() + " "
						+ reject.getIssue().getEncounter().getPatient().getLastName() + " | " + age + " | "
						+ reject.getIssue().getEncounter().getPatient().getGender().getGenderCode() + " | " +reject.getIssue().getEncounter().getPatient().getUhidNumber();
				String patientName = reject.getIssue().getEncounter().getPatient().getPrefix().getPrefixCode() + " "
						+ reject.getIssue().getEncounter().getPatient().getFirstName() + " "
						+ reject.getIssue().getEncounter().getPatient().getMiddleName() + " "
						+ reject.getIssue().getEncounter().getPatient().getLastName();
//				reject.getIssue().getEncounter().getPatient().getGender().getGenderId();
//				reject.getIssue().getEncounter().getPatient().getGender().getGenderCode();
//				reject.getIssue().getEncounter().getPatient().getImagePath();
				PatientIssueRejectionSearchResult patientIssueRejection = new PatientIssueRejectionSearchResult(
						reject.getId(), reject.getRejectNo(), reject.getRejectedDate(),
						reject.getIssue().getPatientIndent().getIndentNo(),
						reject.getIssue().getEncounter().getPatient().getUhidNumber(), patientDetail, doctorDetail,
						reject.getIssue().getEncounter().getEncounterNumber(),
						reject.getIssue().getVisitType().getVisitTypeCode(),
						reject.getIssue().getStore().getStoreName(), patientName, ageString);
				patientIssueRejection.setStatus(reject.getStatus().getStatus());
				patientIssueRejection.setStatusId(reject.getStatus().getId());
				patientIssueRejection.setImagePath(reject.getIssue().getEncounter().getPatient().getImagePath());
				patientIssueRejection.setGenderCode(reject.getIssue().getEncounter().getPatient().getGender().getGenderCode());
				patientIssueRejection.setGenderId(reject.getIssue().getEncounter().getPatient().getGender().getGenderId());
				patientIssueRejection.setToStore(reject.getIssue().getToStore().getStoreName());
				patientIssueRejectionSearchResult.add(patientIssueRejection);
			}
		}
		return patientIssueRejectionSearchResult;
	}

	@Override
	@Transactional
	public PatientIssueRejectionSearchResult patientIssuesRejectionConsumableDetails(Integer id) {
		Reject reject = patientIssuesRejectionConsumableDao.patientIssuesRejectionMedicationById(id);
		String doctorDetail = "";
		if(reject.getIssue().getDoctor()!=null && reject.getIssue().getSpecialityMaster()!=null)
		{
			doctorDetail = reject.getIssue().getDoctor().getFirstName() + " "
					+ reject.getIssue().getDoctor().getMiddleName() + " " + reject.getIssue().getDoctor().getLastName()
					+ " " + reject.getIssue().getSpecialityMaster().getSpecialityName();
		}
		
		PatientIssueRejectionSearchResult patientIssueRejection = new PatientIssueRejectionSearchResult();

		if (reject.getIssue().getAdmission() != null) {
			String wardName = "";
			String bedNumber = "";
			Integer wardId = 0;
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
					reject.getRejectNo(), reject.getRejectedDate(), reject.getIssue().getPatientIndent().getIndentNo(),
					reject.getIssue().getIssueNo(), reject.getIssue().getAdmission().getPatient().getUhidNumber(),
					patientDetail, doctorDetail, reject.getIssue().getAdmission().getAdmissionNumber(),
					reject.getIssue().getAdmission().getVisitType().getVisitTypeCode());
			patientIssueRejection.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			patientIssueRejection.setWardId(wardId != 0 ? wardId : null);
			patientIssueRejection.setWardName(!wardName.equals("") ? wardName : null);
			patientIssueRejection.setStatus(reject.getStatus().getStatus());
			patientIssueRejection.setStatusId(reject.getStatus().getId());

		} else if (reject.getIssue().getEncounter() != null) {
			Integer age = Util.getAgeByBirthDate(
					reject.getIssue().getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = reject.getIssue().getEncounter().getPatient().getPrefix().getPrefixCode() + " "
					+ reject.getIssue().getEncounter().getPatient().getFirstName() + " "
					+ reject.getIssue().getEncounter().getPatient().getMiddleName() + " "
					+ reject.getIssue().getEncounter().getPatient().getLastName() + " | " + age + " | "
					+ reject.getIssue().getEncounter().getPatient().getGender().getGenderCode();
			patientIssueRejection = new PatientIssueRejectionSearchResult(reject.getId(), reject.getIssue().getId(),
					reject.getRejectNo(), reject.getRejectedDate(), reject.getIssue().getPatientIndent().getIndentNo(),
					reject.getIssue().getIssueNo(), reject.getIssue().getEncounter().getPatient().getUhidNumber(),
					patientDetail, doctorDetail, reject.getIssue().getEncounter().getEncounterNumber(),
					reject.getIssue().getEncounter().getVisitType().getVisitTypeCode());
			patientIssueRejection.setStatus(reject.getStatus().getStatus());
			patientIssueRejection.setStatusId(reject.getStatus().getId());

		}
		List<PatientIssueRejectionItem> items = new ArrayList<PatientIssueRejectionItem>();
		for (RejectDetail rejectDetail : patientIssuesRejectionConsumableDao
				.patientIssuesRejectionMedicationDetailById(id)) {
			IssueDetailBatchMapper batchMapper = rejectDetail.getIssueDetailBatchMapper();
			PatientIssueRejectionItem item = new PatientIssueRejectionItem(rejectDetail.getId(),
					batchMapper.getBatch().getItem().getName(), batchMapper.getBatch().getItem().getGeneric().getName(),
					batchMapper.getBatch().getItem().getIsOtc(), batchMapper.getUomType().getType(),
					batchMapper.getUomUnit().getUnits(), batchMapper.getBatch().getBatchNo(),
					batchMapper.getBatch().getExpiryDate(), batchMapper.getBatch().getSellingPrice(),
					rejectDetail.getIssueRejectQuantity(),
					(rejectDetail.getRejectReason() != null) ? rejectDetail.getRejectReason().getReason() : null,
					rejectDetail.getIssueDetailBatchMapper().getId(), batchMapper.getBatch().getItem().getIsThreeUOMApplicable(), 
					GlobalDao.getUnitOfMeasurementListByItem(batchMapper.getBatch().getItem().getId(), reject.getOrganizationId()));
			item.setStatus(rejectDetail.getStatus().getStatus());
			item.setStatusId(rejectDetail.getStatus().getId());
			// todo
			item.setAcceptQuantity(rejectDetail.getAcceptedQuantity() != null ? rejectDetail.getAcceptedQuantity() : null);
			item.setRejectQuantity(rejectDetail.getRejectedQuantity() != null ? rejectDetail.getRejectedQuantity() : null);
			item.setRejectNote(rejectDetail.getRejectedNote() != null ? rejectDetail.getRejectedNote() : null);
			item.setRejectReasonId((rejectDetail.getRejectReason() != null) ? rejectDetail.getRejectReason().getId() : null);
//			item.setQty(rejectDetail.getRejectedQuantity());
			items.add(item);
			patientIssueRejection.setItems(items);
		}
		return patientIssueRejection;
	}

	@Override
	@Transactional
	public Object patientIssuesRejectionMedicationSave(PendingIssuesAcceptanceSave patientIssueRejectionSave) {
		Reject reject = patientIssuesRejectionConsumableDao.patientIssuesRejectionMedicationById(patientIssueRejectionSave.getId());
		for (PendingIssuesAcceptanceSaveItems rejectDetailItem : patientIssueRejectionSave.getItems())
		{
			RejectDetail rejectDetail = patientIssuesRejectionConsumableDao.patientIssuesRejectionMedicationDetailByIdValue(rejectDetailItem.getId());
			rejectDetail.setAcceptedQuantity(
					rejectDetailItem.getAcceptQuantity() != null ? rejectDetailItem.getAcceptQuantity() : null);
			rejectDetail.setRejectedQuantity(
					(rejectDetailItem.getRejectQuantity() != null) ? rejectDetailItem.getRejectQuantity() : null);
			rejectDetail.setRejectReason(
					(rejectDetailItem.getReasonId() != null) ? new RejectReason(rejectDetailItem.getReasonId()) : null);
			rejectDetail.setRejectedNote((rejectDetailItem.getNote() != null) ? rejectDetailItem.getNote() : null);
			rejectDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(reject.getUnitId()));
			rejectDetail.setUpdatedByUser(patientIssueRejectionSave.getUserId()!=null ? patientIssueRejectionSave.getUserId() : sessionService.getUserId());
			if (rejectDetailItem.getStatusId() != null) {
				rejectDetail.setStatus(new Status(rejectDetailItem.getStatusId()));
			}
			reject.addRejectDetail(rejectDetail);
		}
		if (patientIssueRejectionSave.getStatusId() != null) {
			reject.setStatus(new Status(patientIssueRejectionSave.getStatusId()));
		}
		reject.setUpdatedDate(TimeZoneComponent.getDateByzone(reject.getUnitId()));
		reject.setUpdatedByUser(patientIssueRejectionSave.getUserId()!=null ? patientIssueRejectionSave.getUserId() : sessionService.getUserId());
		patientIssuesRejectionConsumableDao.patientIssuesRejectionMedicationSave(reject);
		saveRejectedRejectionDetails(patientIssueRejectionSave);
		saveStoreStockDetails(patientIssueRejectionSave);

		CommonResponse response = new CommonResponse();
		response.setId(reject.getId().longValue());
		response.setMessage("Issue Rejection Medication saved successfully");

		return response;
	}

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock, IssueDetailBatchMapper bm) {

		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		Batch batch = GlobalDao.getBatch(storeStock.getBatch().getId());
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), new Store(sessionService.getStoreId()), batch,bm.getId());
		GlobalDao.saveStockTransaction(stockTransaction);
	}

	private void saveStoreStockDetails(PendingIssuesAcceptanceSave patientIssueRejectionSave) {
		for (PendingIssuesAcceptanceSaveItems rejectDetailItem : patientIssueRejectionSave.getItems()) {
			if (rejectDetailItem.getAcceptQuantity() > 0) {
				IssueDetailBatchMapper batchMapper = GlobalDao
						.getIssueDetailBatchMapperById(rejectDetailItem.getBatchMapperId());

				batchMapper.updateIssueDetailBatchMapper(rejectDetailItem.getBatchMapperId(),
						rejectDetailItem.getAcceptQuantity(),
						(rejectDetailItem.getNote() != null) ? rejectDetailItem.getNote() : null,
						rejectDetailItem.getRejectQuantity(),
						(rejectDetailItem.getReasonId() != null) ? new RejectReason(rejectDetailItem.getReasonId())
								: null);

				Integer leaseQuantity = Util.GetLeaseQuantity(rejectDetailItem.getAcceptQuantity(),
						batchMapper.getBatch().getBatchUomList(), batchMapper.getUomType());
				Integer tQuantity = 0;
				Integer availableQuantity = 0;
				Integer currentQuantity = 0;
				StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(batchMapper.getBatch().getId(),
						patientIssueRejectionSave.getStoreId(),patientIssueRejectionSave.getUnitId());
				StoreStock storeStockDetail = null;
				if (storeStockList == null) {
					StoreStock storeStock = new StoreStock(leaseQuantity, new Store(patientIssueRejectionSave.getStoreId()),
							new Batch(batchMapper.getBatch().getId()));
					storeStockDetail = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
				} else {
					tQuantity = leaseQuantity - storeStockList.gettQty();
					availableQuantity = leaseQuantity + storeStockList.getAvailableQty();
					storeStockList.setAvailableQty(availableQuantity);
					storeStockList.settQty(tQuantity);
					storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
				}
				Batch batchObject = GlobalDao.getBatch(patientIssueRejectionSave.getStoreId(), storeStockDetail.getBatch().getId(),patientIssueRejectionSave.getUnitId());
				currentQuantity = batchObject.getCurrentQuantity() + leaseQuantity;
				batchObject.setCurrentQuantity(currentQuantity);
				GlobalDao.updateBatchLeaseQuantity(batchObject);
				saveStockTransaction(leaseQuantity, storeStockDetail,batchMapper);

			}
		}

	}

	private void saveRejectedRejectionDetails(PendingIssuesAcceptanceSave patientIssueRejectionSave) {
		boolean flag = false;

		/*Integer storeId = sessionService.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String rejectRejectionNumber = GlobalService.getBillNoByGeneratedCode(sessionService.getStoreId(), sessionService.getUnitId(), rejectScreenId);

		RejectedRejection rejectedRejection = new RejectedRejection(rejectRejectionNumber,
				new Reject(patientIssueRejectionSave.getId()), new Store(sessionService.getStoreId()),
				new Issue(patientIssueRejectionSave.getIssueId()),
				new WardMasters(patientIssueRejectionSave.getWardId()),
				new Status(com.param.common.constants.Status.Pending.getStatus()));
		//sessionService.setEntityDefaults(rejectedRejection);
		rejectedRejection.setCreateDefaults(patientIssueRejectionSave.getOrganizationId()!=null ? patientIssueRejectionSave.getOrganizationId() : sessionService.getOrganizationId(),patientIssueRejectionSave.getUnitId() !=null ? patientIssueRejectionSave.getUnitId() :sessionService.getUnitId() ,patientIssueRejectionSave.getUserId()!=null ? patientIssueRejectionSave.getUserId() : sessionService.getUserId());
		for (PendingIssuesAcceptanceSaveItems rejectDetailItem : patientIssueRejectionSave.getItems())
		{
			if (rejectDetailItem.getRejectQuantity() > 0) 
			{
				flag = true;
				RejectedRejectionDetail rejectedRejectionDetail=new RejectedRejectionDetail(
						new RejectDetail(rejectDetailItem.getId()),
						new IssueDetailBatchMapper(rejectDetailItem.getBatchMapperId()),
						(rejectDetailItem.getAcceptQuantity() != null) ? rejectDetailItem.getAcceptQuantity() : null,
						(rejectDetailItem.getRejectQuantity() != null) ? rejectDetailItem.getRejectQuantity() : null,
						(rejectDetailItem.getReasonId() != null) ? new RejectReason(rejectDetailItem.getReasonId()) : null,
						(rejectDetailItem.getNote() != null) ? rejectDetailItem.getNote() : null,
						new Status(com.param.common.constants.Status.Pending.getStatus()));
				rejectedRejection.addRejectedRejectionDetail(rejectedRejectionDetail);
				//sessionService.setEntityDefaults(rejectedRejectionDetail);
				rejectedRejectionDetail.setCreateDefaults(patientIssueRejectionSave.getOrganizationId()!=null ? patientIssueRejectionSave.getOrganizationId() : sessionService.getOrganizationId(),patientIssueRejectionSave.getUnitId() !=null ? patientIssueRejectionSave.getUnitId() :sessionService.getUnitId() ,patientIssueRejectionSave.getUserId()!=null ? patientIssueRejectionSave.getUserId() : sessionService.getUserId());
				
			}
		}
		if (flag == true) {
			patientIssuesRejectionConsumableDao.patientIssuesRejectedRejectionMedicationSave(rejectedRejection);
		}

	}

}