package com.param.pharmacy.ip.service.pendingIssues.acceptance.consumable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
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
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.RejectReason;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PatientIndent;
import com.param.entity.model.pharmacy.PatientIndentDetail;
import com.param.entity.model.pharmacy.Reject;
import com.param.entity.model.pharmacy.RejectDetail;
import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.items.UomResponse;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.pendingIssues.acceptance.consumable.IPendingIssuesAcceptanceConsumableDao;
import com.param.pharmacy.ip.dao.pendingIssues.acceptance.medication.IPendingIssuesAcceptanceMedicationDao;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.IssueAcceptanceDropdown;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceConsumableSearch;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceDetails;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceItem;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSaveItems;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearchResult;
import com.param.pharmacy.nursing.workstation.dao.patientIssueReturn.IPatientIssueReturnDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("pendingIssuesAcceptanceConsumableService")
@Transactional
public class PendingIssuesAcceptanceConsumableService implements ICommonConstants,IPendingIssuesAcceptanceConsumableService {
	@Autowired
	private SessionService sessionService;

	@Autowired
	private IPendingIssuesAcceptanceConsumableDao pendingIssuesAcceptanceConsumableDao;

	@Autowired
	private IPendingIssuesAcceptanceMedicationDao pendingIssuesAcceptanceMedicationDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	private IGlobalService GlobalService;

	private Integer screenId = Screen.IssueAcceptance.getScreen();

	private Integer rejectScreenId = Screen.PatientIssueRejection.getScreen();

	private com.param.entity.model.master.Screen screen;
	@Autowired
	private IPatientIssueReturnDao patientIssueReturnDao;
	@Autowired
	private IWalkInSalesService iWalkInSalesService;

	private static final Logger logger = Logger.getLogger(PendingIssuesAcceptanceConsumableService.class);

	@Override
	@Transactional
	public IssueAcceptanceDropdown getDropdownList(Integer unitId) {
		IssueAcceptanceDropdown response = new IssueAcceptanceDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (WardMasters ward : GlobalDao.getWardList(unitId)) {
			response.addWard(new com.param.global.dto.global.Ward(ward.getWardId(), ward.getWardName()));
		}

		for (FloorMasters floor : GlobalDao.getFloorList(unitId)) {
			response.addFloor(new com.param.global.dto.global.Floor(floor.getFloorId(), floor.getFloorName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<PendingIssuesAcceptanceSearchResult> pendingIssuesAcceptanceConsumableSearch(
			PendingIssuesAcceptanceConsumableSearch pendingIssuesAcceptanceSearch) {
		pendingIssuesAcceptanceSearch
				.setToDate((new DateTime(pendingIssuesAcceptanceSearch.getToDate())).plusDays(1).toDate());
		List<PendingIssuesAcceptanceSearchResult> pendingIssuesAcceptanceSearchResult = new ArrayList<PendingIssuesAcceptanceSearchResult>();
		List<Issue> dataList = pendingIssuesAcceptanceConsumableDao
				.pendingIssuesAcceptanceConsumableSearch(pendingIssuesAcceptanceSearch, screenId);
		for (Issue issue : dataList) {
			// List<DoctorSpecialityMapper> doctorSpeciality =
			// GlobalDao.doctorSearchByIdValue(issue.getDoctor().getDoctorId());

			/*
			 * String deSpeciality=""; if(doctorSpeciality.size() > 0) {
			 * deSpeciality=doctorSpeciality.get(0).getSpeciality().getSpecialityName(); }
			 */
			String doctorDeatil = "";
			if (issue.getVisitType().getVisitTypeId() != ER) {
                if (issue.getDoctor() != null && issue.getDoctor().getDoctorId() != null) {
    					doctorDeatil = issue.getDoctor().getFirstName() + " " + issue.getDoctor().getMiddleName() + " "
    							+ issue.getDoctor().getLastName();
    				}
			}
			if (issue.getSpecialityMaster() != null) {
				doctorDeatil = doctorDeatil + " | " + issue.getSpecialityMaster().getSpecialityName();
			}

			if (issue.getAdmission() != null) {
				String wardName = "";
				String bedNumber = "";
				AdmissionDetail admissionDetail = GlobalDao
						.getPatientWardBedDetails(issue.getAdmission().getAdmissionId());
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
				Integer age = Util
						.getAgeByBirthDate(issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
//				String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " " + issue.getAdmission().getPatient().getFirstName() + " " + issue.getAdmission().getPatient().getMiddleName() + " "+
//						issue.getAdmission().getPatient().getLastName() + " | " + age + " | " + issue.getAdmission().getPatient().getGender().getGenderCode();
				String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " "
						+ issue.getAdmission().getPatient().getFirstName() + " "
						+ issue.getAdmission().getPatient().getMiddleName() + " "
						+ issue.getAdmission().getPatient().getLastName();
				PendingIssuesAcceptanceSearchResult pendingIssuesAcceptance = new PendingIssuesAcceptanceSearchResult(
						issue.getId(), issue.getIssueNo(), issue.getAddedDate(),
						issue.getAdmission().getPatient().getUhidNumber(), patientDetail, age,
						issue.getAdmission().getPatient().getGender().getGenderCode(),
						issue.getAdmission().getPatient().getImagePath(), doctorDeatil,
						issue.getVisitType().getVisitTypeCode(), issue.getStore().getStoreName());
				pendingIssuesAcceptance.setWardName(!wardName.equals("") ? wardName : null);
				pendingIssuesAcceptance.setBedNo(!bedNumber.equals("") ? bedNumber : null);
				pendingIssuesAcceptance.setConsignment(issue.getPatientIndent().isConsignment());
				pendingIssuesAcceptance.setVisitNo(issue.getAdmission().getAdmissionNumber());
				pendingIssuesAcceptance.setIndentNo(issue.getPatientIndent().getIndentNo());
				pendingIssuesAcceptance.setStatus(issue.getStatus().getStatus());
				pendingIssuesAcceptance.setStatusId(issue.getStatus().getId());
				pendingIssuesAcceptance.setToStoreId(issue.getToStore().getStoreId());
				pendingIssuesAcceptance.setToStoreName(issue.getToStore().getStoreName());
				pendingIssuesAcceptance.setIndentId(issue.getPatientIndent().getId());
				pendingIssuesAcceptanceSearchResult.add(pendingIssuesAcceptance);
			} else if (issue.getEncounter() != null) {
				Integer age = Util
						.getAgeByBirthDate(issue.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
//				String patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " " + issue.getEncounter().getPatient().getFirstName() + " " + issue.getEncounter().getPatient().getMiddleName() + " "+
//						issue.getEncounter().getPatient().getLastName() + " | " + age + " | " + issue.getEncounter().getPatient().getGender().getGenderCode();
				String patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " "
						+ issue.getEncounter().getPatient().getFirstName() + " "
						+ issue.getEncounter().getPatient().getMiddleName() + " "
						+ issue.getEncounter().getPatient().getLastName();
				PendingIssuesAcceptanceSearchResult pendingIssuesAcceptance = new PendingIssuesAcceptanceSearchResult(
						issue.getId(), issue.getIssueNo(), issue.getAddedDate(),
						issue.getEncounter().getPatient().getUhidNumber(), patientDetail, age,
						issue.getEncounter().getPatient().getGender().getGenderCode(),
						issue.getEncounter().getPatient().getImagePath(), doctorDeatil,
						issue.getVisitType().getVisitTypeCode(), issue.getStore().getStoreName());
				pendingIssuesAcceptance.setConsignment(issue.getPatientIndent().isConsignment());
				pendingIssuesAcceptance.setVisitNo(issue.getEncounter().getEncounterNumber());
				pendingIssuesAcceptance.setIndentNo(issue.getPatientIndent().getIndentNo());
				pendingIssuesAcceptance.setStatus(issue.getStatus().getStatus());
				pendingIssuesAcceptance.setStatusId(issue.getStatus().getId());
				pendingIssuesAcceptance.setToStoreName(issue.getToStore().getStoreName());
				pendingIssuesAcceptanceSearchResult.add(pendingIssuesAcceptance);
				// pendingIssuesAcceptanceSearchResult.add(pendingIssuesAcceptance);
			}
		}

		return pendingIssuesAcceptanceSearchResult;
	}

	@Override
	@Transactional
	public PendingIssuesAcceptanceDetails pendingIssuesAcceptanceConsumableDetails(Integer id) {
		Issue issue = pendingIssuesAcceptanceMedicationDao.pendingIssuesAcceptanceMedicationById(id);
		PendingIssuesAcceptanceDetails pendingIssuesAcceptanceDetail = new PendingIssuesAcceptanceDetails();
		// List<DoctorSpecialityMapper> doctorSpeciality =
		// GlobalDao.doctorSearchByIdValue(issue.getDoctor().getDoctorId());

		/*
		 * String deSpeciality=""; if(doctorSpeciality.size() > 0) {
		 * deSpeciality=doctorSpeciality.get(0).getSpeciality().getSpecialityName(); }
		 */
		String doctorDeatil = issue.getDoctor().getFirstName() + " " + issue.getDoctor().getMiddleName() + " "
				+ issue.getDoctor().getLastName() + " | " + issue.getSpecialityMaster().getSpecialityName();

		if (issue.getAdmission() != null) {
			String wardName = "";
			String bedNumber = "";
			Date doa = null;
			Date pdd = null;
			AdmissionDetail admissionDetail = GlobalDao.getPatientWardBedDetails(issue.getAdmission().getAdmissionId());
			if (admissionDetail != null) {
				doa = admissionDetail.getDoa();
				pdd = admissionDetail.getPdd();
				WardMasters wardMaster = admissionDetail.getWardMaster();
				if (wardMaster != null) {
					wardName = wardMaster.getWardName();
				}
				BedMaster bedMaster = admissionDetail.getBedMaster();
				if (bedMaster != null) {
					bedNumber = bedMaster.getBedNumber();
				}
			}
			Integer age = Util
					.getAgeByBirthDate(issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " "
					+ issue.getAdmission().getPatient().getFirstName() + " "
					+ issue.getAdmission().getPatient().getMiddleName() + " "
					+ issue.getAdmission().getPatient().getLastName() + " | " + age + " | "
					+ issue.getAdmission().getPatient().getGender().getGenderCode();
			pendingIssuesAcceptanceDetail = new PendingIssuesAcceptanceDetails(issue.getId(),
					issue.getAdmission().getPatient().getPatientId(), patientDetail,
					issue.getAdmission().getPatient().getDob(), issue.getDoctor().getDoctorId(), doctorDeatil,
					issue.getAdmission().getPatient().getUhidNumber(), issue.getIssueNo(), issue.getAddedDate(),
					issue.getPatientIndent().getIndentNo(), issue.getStore().getStoreName());
			pendingIssuesAcceptanceDetail.setWardName(!wardName.equals("") ? wardName : null);
			pendingIssuesAcceptanceDetail.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			pendingIssuesAcceptanceDetail.setDoa(doa != null ? doa : null);
			pendingIssuesAcceptanceDetail.setPdd(pdd != null ? pdd : null);
			pendingIssuesAcceptanceDetail.setStatus(issue.getStatus().getStatus());
			pendingIssuesAcceptanceDetail.setStatusId(issue.getStatus().getId());
		} else if (issue.getEncounter() != null) {
			Integer age = Util
					.getAgeByBirthDate(issue.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " "
					+ issue.getEncounter().getPatient().getFirstName() + " "
					+ issue.getEncounter().getPatient().getMiddleName() + " "
					+ issue.getEncounter().getPatient().getLastName() + " | " + age + " | "
					+ issue.getEncounter().getPatient().getGender().getGenderCode();
			pendingIssuesAcceptanceDetail = new PendingIssuesAcceptanceDetails(issue.getId(),
					issue.getEncounter().getPatient().getPatientId(), patientDetail,
					issue.getEncounter().getPatient().getDob(), issue.getDoctor().getDoctorId(), doctorDeatil,
					issue.getEncounter().getPatient().getUhidNumber(), issue.getIssueNo(), issue.getAddedDate(), "",
					issue.getStore().getStoreName());
			pendingIssuesAcceptanceDetail.setStatus(issue.getStatus().getStatus());
			pendingIssuesAcceptanceDetail.setStatusId(issue.getStatus().getId());
		}

		List<PendingIssuesAcceptanceItem> list = pendingIssuesAcceptanceMedicationDao
				.pendingIssuesAcceptanceMedicationDetails(id);

		for (PendingIssuesAcceptanceItem i : list) {
			List<Integer> itemIds = new ArrayList<>();
			itemIds.add(i.getItemId());
			List<UomResponse> uoms = null;
			if (itemIds.size() > 0) {
				uoms = GlobalDao.getUnitOfMeasurementListByItemBettoerPerformance(itemIds, issue.getOrganizationId());
			}
			i.setUomResponse(uoms);
			// Integer issueQuantity =
			// Util.GetPendingIndentQuantityLeaseQuantityStoreIndent(i.getQuantityIssued(),unitOfMeasurementDao.getAllByItemId(i.getItemId(),issue.getOrganizationId()),
			// i.getUomTypeId());
			// i.setQuantityIssued(issueQuantity);
		}
		pendingIssuesAcceptanceDetail.setItems(list);
		/*
		 * pendingIssuesAcceptanceDetail .setItems(pendingIssuesAcceptanceMedicationDao.
		 * pendingIssuesAcceptanceMedicationDetails(id));
		 */
		return pendingIssuesAcceptanceDetail;
	}

	@Override
	public Object pendingIssuesAcceptanceConsumableSave(PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave) {
		screen = GlobalDao.getScreen(screenId);
		Issue issue = pendingIssuesAcceptanceMedicationDao
				.pendingIssuesAcceptanceMedicationById(pendingIssuesAcceptanceSave.getId());

		// for (IssueDetail issueDetail :
		// GlobalDao.getIssueDeatilByIssueId(issue.getId())) {
		for (IssueDetail issueDetail : GlobalDao.getIssueDetail(issue.getId())) {
			// for (IssueDetailBatchMapper batchMapper :
			// issueDetail.getIssueDetailBatchMapper()) {
			for (IssueDetailBatchMapper batchMapper : GlobalDao.getIssueDetailBatchMapper(issueDetail.getId())) {
				for (PendingIssuesAcceptanceSaveItems issueDetailBatchMapper : pendingIssuesAcceptanceSave.getItems()) {
					if (issueDetailBatchMapper.getAcceptQuantity() >= 0
							&& batchMapper.getId().equals(issueDetailBatchMapper.getBatchMapperId())) {
						if (issueDetailBatchMapper.getBatchMapperId() != null
								&& (batchMapper.getId().equals(issueDetailBatchMapper.getBatchMapperId()))) {
							// Batch b=GlobalDao.getBatch(batchMapper.getBatch().getId());
							// GlobalDao.getbatch
							/*
							 * BatchMapperStoreStock(pendingIssuesAcceptanceSave.getStoreId(),
							 * GlobalDao.getBatch(batchMapper.getBatch().getId()),
							 * issueDetailBatchMapper.getAcceptQuantity(), batchMapper.getUomType(),
							 * pendingIssuesAcceptanceSave, batchMapper,issueDetailBatchMapper, issue);
							 */

							batchMapper.updateIssueDetailBatchMapper(issueDetailBatchMapper.getAcceptQuantity(),
									(issueDetailBatchMapper.getNote() != null) ? issueDetailBatchMapper.getNote()
											: null,
									issueDetailBatchMapper.getRejectQuantity(),
									(issueDetailBatchMapper.getReasonId() != null)
											? new RejectReason(issueDetailBatchMapper.getReasonId())
											: null);
							batchMapper.setReturnAcceptedQuantity(0);
							batchMapper.setReturnRejectedQuantity(0);

						}
					}

				}
				GlobalDao.updateIssueDetailBatchMapper(batchMapper);

			}

		}
		issue.setUpdatedByUser(pendingIssuesAcceptanceSave.getUserId() != null ? pendingIssuesAcceptanceSave.getUserId()
				: sessionService.getUserId());
		issue.setUpdatedDate(TimeZoneComponent.getDateByzone(issue.getUnitId()));
		issue.setStatus(pendingIssuesAcceptanceSave.getStatusId() != null
				? new Status(pendingIssuesAcceptanceSave.getStatusId())
				: new Status(com.param.common.constants.Status.Closed.getStatus()));
		// sales return
		try {
			// iWalkInSalesService.pharamcyPackageContractReturn(dto, orderDetailsList);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

		if (issue.getPatientIndent() != null) {
			boolean statusChangeFlag = true;
			PatientIndent patientIndent = issue.getPatientIndent();
			for (PatientIndentDetail pid : patientIndent.getPatientIndentDetail()) {
				if (pid.getPendingQuantity() > 0) {
					statusChangeFlag = false;
				}
			}
			if (statusChangeFlag) {
				patientIndent.setStatus(new Status(com.param.common.constants.Status.Closed.getStatus()));
			}
		}
		/*
		 * end code for resolved HIS-10988 On patient Indent status not getting change
		 */

		pendingIssuesAcceptanceMedicationDao.pendingIssuesAcceptanceMedicationSave(issue);
		saveRejectDetails(pendingIssuesAcceptanceSave);

		CommonResponse response = new CommonResponse();
		response.setId(issue.getId().longValue());
		response.setMessage("Issue Acceptance Consumable saved successfully");

		return response;
	}

	private void BatchMapperStoreStock(Integer storeId, Batch batch, Integer issueQuantity, UomType uomType,
			PendingIssuesAcceptanceSave request, IssueDetailBatchMapper bm, PendingIssuesAcceptanceSaveItems issueDetailBatchMapper, Issue issue) {

		List<StoreStock> fromStoreStock1 = GlobalDao.getStoreStockByBatchAndStoreList(batch.getId(),
				request.getStoreId(), batch.getUnitId());
		boolean hasStock = false;
		/*
		 * List<StoreStock> fromStoreStock1 = fromStoreStock1New.stream() .filter(i ->
		 * i.getStore().getStoreId() ==
		 * request.getStoreId()).collect(Collectors.toList());
		 */
		if (fromStoreStock1 != null && fromStoreStock1.size() > 0) {
			// fromStoreStock1.get(0).settQty(tQty);
			Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(request.getStoreId(), batch.getId());
			Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity, batch.getBatchUomList(), uomType);
			Integer rejectleaseQty = Util.GetLeaseQuantity(issueDetailBatchMapper.getRejectQuantity()!=null ? issueDetailBatchMapper.getRejectQuantity() : 0, batch.getBatchUomList(), uomType);
			
			// Integer balanceQuantity = avaialableStoreStock - leaseQuantity;
			StoreStock storeStock = fromStoreStock1.get(0);
			// storeStock.setAvailableQty(balanceQuantity);
			Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(request.getStoreId(), batch.getId());
			Integer tQty = inTransitQuantity - leaseQuantity;
			storeStock.settQty(tQty < 0 ? leaseQuantity : tQty);
			StockTransaction stockTransaction = new StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					leaseQuantity, storeStock.getStore(),bm.getId());
			stockTransaction.setQuantity(rejectleaseQty);
			stockTransaction.setIsTransit(true);
			stockTransaction.setItem(batch.getItem());
			stockTransaction.setTransitNo(issue.getIssueNo());
			stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(), request.getUserId());
			batch.addStockTransaction(stockTransaction);
			GlobalDao.updateStoreStockLeaseQuantity(storeStock);
		}

		/*
		 * for (StoreStock storeStock : batch.getStoreStockList()) { if
		 * (storeStock.getStore().getStoreId().equals(request.getFromStoreId())) {
		 * 
		 * hasStock = true; Integer avaialableStoreStock =
		 * GlobalDao.getStoreStockByBatch(storeId, batch.getId());
		 * 
		 * Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity,
		 * batch.getBatchUomList(), uomType);
		 * 
		 * Integer balanceQuantity = avaialableStoreStock + leaseQuantity;
		 * 
		 * storeStock.setAvailableQty(balanceQuantity); Integer inTransitQuantity =
		 * GlobalDao.getTransitQuantityByBatch(storeId, batch.getId()); Integer tQty =
		 * inTransitQuantity - leaseQuantity; storeStock.settQty(tQty);
		 * 
		 * GlobalDao.updateStoreStockLeaseQuantity(storeStock); StockTransaction
		 * stockTransaction = new StockTransaction(balanceQuantity, screen,
		 * screen.getCode(), leaseQuantity,
		 * batch.getStoreStockList().get(0).getStore());
		 * stockTransaction.setCreateDefaults(request.getOrganizationId(),
		 * request.getUnitId(), request.getUserId());
		 * batch.addStockTransaction(stockTransaction); } else if
		 * (storeStock.getStore().getStoreId().equals(request.getFromStoreId())) {
		 * 
		 * 
		 * hasStock = true; Integer avaialableStoreStock =
		 * GlobalDao.getStoreStockByBatch(request.getFromStoreId(), batch.getId());
		 * 
		 * Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity,
		 * batch.getBatchUomList(), uomType);
		 * 
		 * Integer balanceQuantity = avaialableStoreStock + leaseQuantity;
		 * 
		 * storeStock.setAvailableQty(balanceQuantity);
		 * GlobalDao.updateStoreStockLeaseQuantity(storeStock); StockTransaction
		 * stockTransaction = new StockTransaction(balanceQuantity, screen,
		 * screen.getCode(), leaseQuantity,
		 * batch.getStoreStockList().get(0).getStore());
		 * stockTransaction.setCreateDefaults(request.getOrganizationId(),
		 * request.getUnitId(), request.getUserId());
		 * batch.addStockTransaction(stockTransaction);
		 * 
		 * } else {
		 * 
		 * 
		 * StoreStock st=new StoreStock(issueQuantity, new
		 * Store(request.getFromStoreId()), new Batch(batch.getId()));
		 * 
		 * Integer avaialableStoreStock =
		 * GlobalDao.getStoreStockByBatch(request.getFromStoreId(), batch.getId());
		 * 
		 * Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity,
		 * batch.getBatchUomList(), uomType); st.setTotalQty(leaseQuantity);
		 * st.setAvailableQty(leaseQuantity);
		 * st.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
		 * request.getUserId()); GlobalDao.saveStoreStockLeaseQuantity(st);
		 * StockTransaction stockTransaction = new
		 * StockTransaction(avaialableStoreStock, screen, screen.getCode(),
		 * leaseQuantity, batch.getStoreStockList().get(0).getStore());
		 * stockTransaction.setCreateDefaults(request.getOrganizationId(),
		 * request.getUnitId(), request.getUserId());
		 * batch.addStockTransaction(stockTransaction);
		 * 
		 * } }
		 */
		// As discussed with divyesh remove requesting store stock plus logic
		if (true) {/*
					 * 
					 * StoreStock st=new StoreStock(issueQuantity, new Store(storeId), new
					 * Batch(batch.getId()));
					 * 
					 * Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId,
					 * batch.getId());
					 * 
					 * Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity,
					 * batch.getBatchUomList(), uomType);
					 * 
					 * StockTransaction stockTransaction = new
					 * StockTransaction(avaialableStoreStock, screen, screen.getCode(),
					 * leaseQuantity, batch.getStoreStockList().get(0).getStore());
					 * stockTransaction.setCreateDefaults(request.getOrganizationId(),
					 * request.getUnitId(), request.getUserId());
					 * batch.addStockTransaction(stockTransaction);
					 *
					 * 
					 */

			/*
			 * if (request.getFromStoreId() != null) {
			 * 
			 * List<StoreStock> fromStoreStock =
			 * GlobalDao.getStoreStockByBatchAndStoreList(batch.getId(),
			 * request.getFromStoreId(), batch.getUnitId()); List<StoreStock> fromStoreStock
			 * = fromStoreStock1New1.stream() .filter(i -> i.getStore().getStoreId() ==
			 * request.getFromStoreId()) .collect(Collectors.toList());
			 * 
			 * if (fromStoreStock != null && fromStoreStock.size() > 0) { Integer
			 * avaialableStoreStock =
			 * GlobalDao.getStoreStockByBatch(request.getFromStoreId(), batch.getId());
			 * 
			 * Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity,
			 * batch.getBatchUomList(), uomType);
			 * 
			 * Integer balanceQuantity = avaialableStoreStock + leaseQuantity; StoreStock ss
			 * = fromStoreStock.get(0); ss.setAvailableQty(balanceQuantity < 0 ? 0 :
			 * balanceQuantity); ss.setTotalQty((ss.getTotalQty() != null ? ss.getTotalQty()
			 * : 0) + leaseQuantity); GlobalDao.updateStoreStockLeaseQuantity(ss);
			 * StockTransaction stockTransaction = new StockTransaction(balanceQuantity,
			 * screen, screen.getCode(), leaseQuantity,
			 * batch.getStoreStockList().get(0).getStore());
			 * stockTransaction.setCreateDefaults(request.getOrganizationId(),
			 * request.getUnitId(), request.getUserId());
			 * batch.addStockTransaction(stockTransaction); } else { StoreStock st = new
			 * StoreStock(issueQuantity, new Store(request.getFromStoreId()), new
			 * Batch(batch.getId()));
			 * 
			 * Integer avaialableStoreStock =
			 * GlobalDao.getStoreStockByBatch(request.getFromStoreId(), batch.getId());
			 * 
			 * Integer leaseQuantity = Util.GetLeaseQuantity(issueQuantity,
			 * batch.getBatchUomList(), uomType); st.setTotalQty(leaseQuantity);
			 * st.setAvailableQty(leaseQuantity < 0 ? 0 : leaseQuantity);
			 * st.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
			 * request.getUserId()); GlobalDao.saveStoreStockLeaseQuantity(st);
			 * StockTransaction stockTransaction = new
			 * StockTransaction(avaialableStoreStock, screen, screen.getCode(),
			 * leaseQuantity, batch.getStoreStockList().get(0).getStore());
			 * stockTransaction.setCreateDefaults(request.getOrganizationId(),
			 * request.getUnitId(), request.getUserId());
			 * batch.addStockTransaction(stockTransaction); }
			 * 
			 * }
			 */
		}
	}

	private void saveRejectDetails(PendingIssuesAcceptanceSave pendingIssuesAcceptanceSave) {
		Timestamp now = new Timestamp((new DateTime()).getMillis());
		boolean flag = false;

		/*
		 * Integer storeId = pendingIssuesAcceptanceSave.getStoreId();
		 * 
		 * Store storeNew = GlobalDao.getStore(storeId);
		 * 
		 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
		 */

		String rejectBillNumber = GlobalService.getBillNoByGeneratedCode(pendingIssuesAcceptanceSave.getStoreId(),
				sessionService.getUnitId(), rejectScreenId);

		Reject reject = new Reject(rejectBillNumber, new Issue(pendingIssuesAcceptanceSave.getId()),
				new User(sessionService.getUserId()), now,
				new Status(com.param.common.constants.Status.Pending.getStatus()));
		reject.setConsumable(true);
		// sessionService.setEntityDefaults(reject);
		reject.setCreateDefaults(
				pendingIssuesAcceptanceSave.getOrganizationId() != null
						? pendingIssuesAcceptanceSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				pendingIssuesAcceptanceSave.getUnitId() != null ? pendingIssuesAcceptanceSave.getUnitId()
						: sessionService.getUnitId(),
				pendingIssuesAcceptanceSave.getUserId() != null ? pendingIssuesAcceptanceSave.getUserId()
						: sessionService.getUserId());
		for (PendingIssuesAcceptanceSaveItems issueDetailItem : pendingIssuesAcceptanceSave.getItems()) {
			if (issueDetailItem.getRejectQuantity() > 0) {
				flag = true;
				RejectDetail rejectDetail = new RejectDetail(
						new IssueDetailBatchMapper(issueDetailItem.getBatchMapperId()),
						issueDetailItem.getRejectQuantity(),
						new Status(com.param.common.constants.Status.Pending.getStatus()),
						(issueDetailItem.getReasonId() != null) ? new RejectReason(issueDetailItem.getReasonId())
								: null,
						issueDetailItem.getNote());

				rejectDetail.setCreateDefaults(
						pendingIssuesAcceptanceSave.getOrganizationId() != null
								? pendingIssuesAcceptanceSave.getOrganizationId()
								: sessionService.getOrganizationId(),
						pendingIssuesAcceptanceSave.getUnitId() != null ? pendingIssuesAcceptanceSave.getUnitId()
								: sessionService.getUnitId(),
						pendingIssuesAcceptanceSave.getUserId() != null ? pendingIssuesAcceptanceSave.getUserId()
								: sessionService.getUserId());
				reject.addRejectDetail(rejectDetail);

			}
		}
		if (flag == true) {
			pendingIssuesAcceptanceMedicationDao.pendingIssuesAcceptanceMedicationRejectSave(reject);
		}
	}

}
