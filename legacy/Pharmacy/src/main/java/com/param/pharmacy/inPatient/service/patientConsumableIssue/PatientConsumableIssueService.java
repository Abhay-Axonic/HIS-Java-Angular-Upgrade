package com.param.pharmacy.inPatient.service.patientConsumableIssue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.unit.model.WardMaster;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PatientIndent;
import com.param.entity.model.pharmacy.PatientIndentDetail;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.SaleDetailPayeeMapper;
import com.param.entity.model.pharmacy.SaleType;
import com.param.global.billing.dto.CommonBillingItemsDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.global.PatientConsession;
import com.param.global.dto.items.BatchResponse;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.pharmacy.dto.PatientConsumableIssueSave;
import com.param.global.pharmacy.dto.PatientConsumableIssueSaveItem;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.global.IGlobalService;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.pharmacy.inPatient.dao.patientConsumableIssue.IPatientConsumableIssueDao;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.Carrier;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueDetails;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueItem;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueRequest;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueResponse;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.SubstitutePatientConsumableDto;
import com.param.pharmacy.ip.dao.pendingIssues.IPendingIssuesDao;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSaveItems;
import com.param.pharmacy.ip.service.pendingIssues.acceptance.consumable.IPendingIssuesAcceptanceConsumableService;
import com.param.pharmacy.nursing.pharmacy.dao.patientIndent.IPatientIndentDao;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentDropdown;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
import com.param.service.global.SessionService;
import com.param.service.global.model.SaleDetailPackageMapper;
import com.param.utility.TimeZoneComponent;

@Service("patientConsumableService")
@Transactional
public class PatientConsumableIssueService implements ICommonConstants,IPatientConsumableIssueService {
	@Autowired
	private SessionService sessionService;

	@Autowired
	private IPatientConsumableIssueDao patientConsumableIssueDao;

	@Autowired
	private IPatientIndentDao patientIndentDao;

	@Autowired
	private IPendingIssuesDao pendingIssuesDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IOrderMasterService iOrderMasterService;

	@Autowired
	IMPackageMasterService iMPackageMasterService;

	@Autowired
	IWalkInSalesService iWalkInSalesService;

	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao;
	@Autowired
	private IPendingIssuesAcceptanceConsumableService pendingIssuesAcceptanceConsumableService;
	private Integer screenId = Screen.PatientPendingIssue.getScreen();
	private Integer patientIssueAcceptance = Screen.PatientIssueAcceptance.getScreen();
	private Integer screenIdWalkInSale = Screen.WalkinSale.getScreen();
	private Integer issueScreenId = Screen.PatientIssueConsumable.getScreen();

	private com.param.entity.model.master.Screen screen;
	@Autowired
	IGoodReceiptNoteDao grnDao;

	@Override
	@Transactional
	public PatientIndentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId) {
		PatientIndentDropdown response = new PatientIndentDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (com.param.global.dto.global.Store store : GlobalDao.getUserStoreListBP(userId, unitId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getId(), store.getStore()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId, orgId, userId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (WardMasters ward : GlobalDao.getWardList(unitId)) {
			response.addWard(new com.param.global.dto.global.Ward(ward.getWardId(), ward.getWardName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<PatientConsumableIssueResponse> patientConsumableIssueSearch(PatientConsumableIssueRequest request) {
		List<PatientConsumableIssueResponse> patientIndentList = new ArrayList<PatientConsumableIssueResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		for (Issue issue : patientConsumableIssueDao.patientConsumableIssueSearch(request, screenId)) {
			if (issue.getPatientIndent() != null) {

				Integer age = Util.getAgeByBirthDate(
						issue.getPatientIndent().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = issue.getPatientIndent().getPatient().getPrefix().getPrefixCode() + " "
						+ issue.getPatientIndent().getPatient().getFirstName() + " "
						+ issue.getPatientIndent().getPatient().getMiddleName() + " "
						+ issue.getPatientIndent().getPatient().getLastName() + " | " + age + " | "
						+ issue.getPatientIndent().getPatient().getGender().getGenderCode();

				String patientName = issue.getPatientIndent().getPatient().getPrefix().getPrefixCode() + " "
						+ issue.getPatientIndent().getPatient().getFirstName() + " "
						+ issue.getPatientIndent().getPatient().getMiddleName() + " "
						+ issue.getPatientIndent().getPatient().getLastName();

				String doctorDetail = "";
				// no doctor for emergency admission
				if (issue.getDoctor() != null) {
					if (issue.getVisitType().getVisitTypeId().equals(ER)) {
						// no doctor for emergency admission
					} else {
			            if (issue.getDoctor() != null) {
			                doctorDetail = issue.getDoctor().getFirstName() + " " + issue.getDoctor().getMiddleName() + " "
									+ issue.getDoctor().getLastName();
			            }
					}

				}
				if (issue.getSpecialityMaster() != null) {
					doctorDetail = doctorDetail + " | " + issue.getSpecialityMaster().getSpecialityName();
				}

				/*
				 * add getStatus().equals("A") condation for resolved HIS-10573 Unable to save
				 * patient consumable issue throwing an error in case of multiple narcotics
				 * drugs added from patient indent
				 */
				if (issue.getAdmission() != null && issue.getAdmission().getStatus().equals("A")) {
				
					PatientConsumableIssueResponse patientIndentObj = new PatientConsumableIssueResponse(issue.getId(),
							issue.getPatientIndent().getIndentNo(), issue.getAddedDate(),
							issue.getVisitType().getVisitTypeCode(), issue.getAdmission().getAdmissionNumber(),
							issue.getPatientIndent().getPatient().getUhidNumber(), patientDetail, doctorDetail,
							issue.getStore().getStoreName(), issue.getToStore().getStoreName(),
							issue.getStatus().getId(), issue.getStatus().getStatus(),
							issue.getPatientIndent().getPatient().getPatientId(), age, patientName);
					
					for(AdmissionDetail admissionDetail1: issue.getAdmission().getAdmissionDetail()){
						if(admissionDetail1.getStatus().equals("A")){
							patientIndentObj.setWardName(admissionDetail1.getWardMaster().getWardName());				
						}
					}
//					patientIndentObj.setWardName((issue.getPatientIndent().getWard() != null)
//							? issue.getPatientIndent().getWard().getWardName()
//							: null);
					patientIndentObj.setBedNo((issue.getPatientIndent().getBed() != null)
							? issue.getPatientIndent().getBed().getBedNumber()
							: null);
					patientIndentObj.setIssueNo(issue.getIssueNo());
					patientIndentObj.setIssueDate(issue.getAddedDate());
					patientIndentObj.setImagePath(issue.getAdmission().getPatient().getImagePath());
					patientIndentObj.setGenderCode(issue.getAdmission().getPatient().getGender().getGenderCode());
					patientIndentObj.setGenderId(issue.getAdmission().getPatient().getGender().getGenderId());
					Integer isFinalBill=GlobalDao.getIsFinalBill(null,issue.getAdmission().getAdmissionId());
					if(isFinalBill!=null && isFinalBill==2)
					{
						patientIndentObj.setIsFinalBill('Y');
					}
					else
					{
						patientIndentObj.setIsFinalBill('N');
					}
					patientIndentList.add(patientIndentObj);
				} else if (issue.getEncounter() != null && issue.getEncounter().getStatus().equals("A")) {
					/*
					 * add getStatus().equals("A") condation for resolved HIS-10573 Unable to save
					 * patient consumable issue throwing an error in case of multiple narcotics
					 * drugs added from patient indent
					 */
					PatientConsumableIssueResponse patientIndentObj = new PatientConsumableIssueResponse(issue.getId(),
							issue.getPatientIndent().getIndentNo(), issue.getAddedDate(),
							issue.getVisitType().getVisitTypeCode(), issue.getEncounter().getEncounterNumber(),
							issue.getPatientIndent().getPatient().getUhidNumber(), patientDetail, doctorDetail,
							issue.getStore().getStoreName(), issue.getToStore().getStoreName(),
							issue.getStatus().getId(), issue.getStatus().getStatus(),
							issue.getPatientIndent().getPatient().getPatientId(), age, patientName);
					patientIndentObj.setIssueNo(issue.getIssueNo());
					patientIndentObj.setIssueDate(issue.getAddedDate());
					patientIndentObj.setImagePath(issue.getEncounter().getPatient().getImagePath());
					patientIndentObj.setGenderCode(issue.getEncounter().getPatient().getGender().getGenderCode());
					patientIndentObj.setGenderId(issue.getEncounter().getPatient().getGender().getGenderId());
					Integer isFinalBill=GlobalDao.getIsFinalBill(issue.getEncounter().getEncounterId(),null);
					if(isFinalBill!=null && isFinalBill==2)
					{
						patientIndentObj.setIsFinalBill('Y');
					}
					else
					{
						patientIndentObj.setIsFinalBill('N');
					}
					patientIndentList.add(patientIndentObj);
				}
			}
		}
		return patientIndentList;
	}

	@Override
	@Transactional
	public List<PatientConsumableIssueResponse> patientConsumableIssueSearchForPatientIssueAcceptance(
			PatientConsumableIssueRequest request) {
		List<PatientConsumableIssueResponse> patientIndentList = new ArrayList<PatientConsumableIssueResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		for (Issue issue : patientConsumableIssueDao.patientConsumableIssueSearch(request, patientIssueAcceptance)) {
			if (issue.getPatientIndent() != null) {

				Integer age = Util.getAgeByBirthDate(
						issue.getPatientIndent().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = issue.getPatientIndent().getPatient().getPrefix().getPrefixCode() + " "
						+ issue.getPatientIndent().getPatient().getFirstName() + " "
						+ issue.getPatientIndent().getPatient().getMiddleName() + " "
						+ issue.getPatientIndent().getPatient().getLastName() + " | " + age + " | "
						+ issue.getPatientIndent().getPatient().getGender().getGenderCode();

				String doctorDetail = "";
				if (issue.getDoctor() != null && issue.getSpecialityMaster() != null) {
					doctorDetail = issue.getDoctor().getFirstName() + " | " + issue.getDoctor().getMiddleName() + " "
							+ issue.getDoctor().getLastName() + " | " + issue.getSpecialityMaster().getSpecialityName();
				}

				if (issue.getAdmission() != null) {
					PatientConsumableIssueResponse patientIndentObj = new PatientConsumableIssueResponse(issue.getId(),
							issue.getPatientIndent().getIndentNo(), issue.getAddedDate(),
							issue.getVisitType().getVisitTypeCode(), issue.getAdmission().getAdmissionNumber(),
							issue.getPatientIndent().getPatient().getUhidNumber(), patientDetail, doctorDetail,
							issue.getStore().getStoreName(), issue.getToStore().getStoreName(),
							issue.getStatus().getId(), issue.getStatus().getStatus(),
							issue.getPatientIndent().getPatient().getPatientId());
					patientIndentObj.setWardName((issue.getPatientIndent().getWard() != null)
							? issue.getPatientIndent().getWard().getWardName()
							: null);
					patientIndentObj.setBedNo((issue.getPatientIndent().getBed() != null)
							? issue.getPatientIndent().getBed().getBedNumber()
							: null);
					patientIndentObj.setIssueNo(issue.getIssueNo());
					patientIndentObj.setIssueDate(issue.getAddedDate());

					patientIndentList.add(patientIndentObj);
				} else if (issue.getEncounter() != null) {
					PatientConsumableIssueResponse patientIndentObj = new PatientConsumableIssueResponse(issue.getId(),
							issue.getPatientIndent().getIndentNo(), issue.getAddedDate(),
							issue.getVisitType().getVisitTypeCode(), issue.getEncounter().getEncounterNumber(),
							issue.getPatientIndent().getPatient().getUhidNumber(), patientDetail, doctorDetail,
							issue.getStore().getStoreName(), issue.getToStore().getStoreName(),
							issue.getStatus().getId(), issue.getStatus().getStatus(),
							issue.getPatientIndent().getPatient().getPatientId());
					patientIndentObj.setIssueNo(issue.getIssueNo());
					patientIndentObj.setIssueDate(issue.getAddedDate());

					patientIndentList.add(patientIndentObj);
				}
			}
		}
		return patientIndentList;
	}

	@SuppressWarnings("unused")
	@Override
	@Transactional
	public PatientConsumableIssueDetails patientConsumableIssueDetails(Integer id, Integer screenFlag, Integer storeId,
			Integer unitId) throws Exception {
		Issue issue = patientConsumableIssueDao.patientConsumableIssueById(id, unitId);

		if (issue.getStatus().getId().equals(2)) {
			String doctorDeatil = "";
			Integer drId = 0;
			if (issue.getVisitType().getVisitTypeId().equals(ER)) {
				// doctor not found for emergency admission

			} else {
				if(issue.getDoctor()!= null) {
					 String doctorDetail = issue.getDoctor().getFirstName() + " " + issue.getDoctor().getMiddleName() + " "
									+ issue.getDoctor().getLastName();
					 drId = issue.getDoctor().getDoctorId();
									}
					
			}

			List<PatientSponsorDetails> patientSponsorDetails = null;
			PatientConsumableIssueDetails patientConsumableIssueDetails = new PatientConsumableIssueDetails();
			if (issue.getPatientIndent() != null) {
				String patientName = issue.getPatientIndent().getPatient().getFirstName() + " "
						+ issue.getPatientIndent().getPatient().getMiddleName() + " "
						+ issue.getPatientIndent().getPatient().getLastName();

				if (issue.getAdmission() != null) {
					patientConsumableIssueDetails = new PatientConsumableIssueDetails(issue.getId(),
							issue.getStore().getStoreName(), issue.getStore().getStoreId(),
							issue.getToStore().getStoreName(), issue.getToStore().getStoreId(),
							issue.getVisitType().getVisitTypeId(), issue.getVisitType().getVisitTypeCode(), patientName,
							issue.getPatientIndent().getPatient().getUhidNumber(),
							issue.getAdmission().getAdmissionNumber(), issue.getPatientIndent().getIndentNo(), drId,
							issue.getPatientIndent().getId());
					patientConsumableIssueDetails.setAdmissionId(issue.getAdmission().getAdmissionId());
					patientConsumableIssueDetails.setDoctorDetail(doctorDeatil);

					Date doa = null;
					Date pdd = null;
					AdmissionDetail admissionDetail = GlobalDao
							.getPatientWardBedDetails(issue.getAdmission().getAdmissionId());
					if (admissionDetail != null) {
						doa = admissionDetail.getDoa();
						pdd = admissionDetail.getPdd();
					}
					patientConsumableIssueDetails.setDoa(doa != null ? doa : null);
					patientConsumableIssueDetails.setDob(issue.getPatientIndent().getPatient().getDob());
					patientConsumableIssueDetails.setPdd(pdd != null ? pdd : null);
					patientConsumableIssueDetails.setWardName((issue.getPatientIndent().getWard() != null)
							? issue.getPatientIndent().getWard().getWardName()
							: null);
					patientConsumableIssueDetails.setBedNo((issue.getPatientIndent().getBed() != null)
							? issue.getPatientIndent().getBed().getBedNumber()
							: null);
					patientConsumableIssueDetails.setStatusId(issue.getStatus().getId());
					patientConsumableIssueDetails.setStatus(issue.getStatus().getStatus());
					patientConsumableIssueDetails.setIssueNo(issue.getIssueNo());
					patientConsumableIssueDetails.setIssueDate(issue.getAddedDate());

					if (issue != null && issue.getAdmission() != null)
						patientSponsorDetails = iPatientSponsorDetailsDao
								.findByAdmissionId(issue.getAdmission().getAdmissionId());
				} else if (issue.getEncounter() != null) {
					patientConsumableIssueDetails = new PatientConsumableIssueDetails(issue.getId(),
							issue.getStore().getStoreName(), issue.getStore().getStoreId(),
							issue.getToStore().getStoreName(), issue.getToStore().getStoreId(),
							issue.getVisitType().getVisitTypeId(), issue.getVisitType().getVisitTypeCode(), patientName,
							issue.getPatientIndent().getPatient().getUhidNumber(),
							issue.getEncounter().getEncounterNumber(), issue.getPatientIndent().getIndentNo(), drId);
					patientConsumableIssueDetails.setEncounterId(issue.getEncounter().getEncounterId());
					patientConsumableIssueDetails.setStatusId(issue.getStatus().getId());
					patientConsumableIssueDetails.setStatus(issue.getStatus().getStatus());
					patientConsumableIssueDetails.setIssueNo(issue.getIssueNo());
					patientConsumableIssueDetails.setIssueDate(issue.getAddedDate());
					patientConsumableIssueDetails.setDoctorDetail(doctorDeatil);
					if (issue != null && issue.getEncounter() != null)
						patientSponsorDetails = iPatientSponsorDetailsDao
								.findByEncounterId(issue.getEncounter().getEncounterId());
				}
			}
			if (issue.getImplantIndent() != null && issue.getIsImplant().equals(false)) {
				String patientName = issue.getImplantIndent().getPatient().getFirstName() + " "
						+ issue.getImplantIndent().getPatient().getMiddleName() + " "
						+ issue.getImplantIndent().getPatient().getLastName();

				patientConsumableIssueDetails = new PatientConsumableIssueDetails(issue.getId(),
						issue.getImplantIndent().getId(), issue.getStore().getStoreName(),
						issue.getStore().getStoreId(), issue.getToStore().getStoreName(),
						issue.getToStore().getStoreId(), issue.getVisitType().getVisitTypeId(),
						issue.getVisitType().getVisitTypeCode(), patientName,
						issue.getImplantIndent().getPatient().getUhidNumber(),
						issue.getAdmission().getAdmissionNumber(), issue.getImplantIndent().getImplantIndentNo(), drId);
				patientConsumableIssueDetails.setAdmissionId(issue.getAdmission().getAdmissionId());
				patientConsumableIssueDetails.setStatusId(issue.getStatus().getId());
				patientConsumableIssueDetails.setStatus(issue.getStatus().getStatus());
				patientConsumableIssueDetails.setIssueNo(issue.getIssueNo());
				patientConsumableIssueDetails.setIssueDate(issue.getAddedDate());
				patientConsumableIssueDetails.setDoctorDetail(doctorDeatil);
			}

			List<PatientConsumableIssueItem> itemList = new ArrayList<PatientConsumableIssueItem>();
			List<Integer> ids = patientConsumableIssueDao.getIssueDetailids(id, unitId);
			List<IssueDetail> issueDetailData = patientConsumableIssueDao.getpatientConsumableIssueDetailByIds(ids,
					screenFlag, unitId);
			// for (IssueDetail issueDetail : issueDetailData) {
			for (IssueDetail issueDetail : issue.getIssueDetail()) {
				if (issueDetail.getPatientIndentDetail().getIsDeleted().equals(false)) {

					List<BatchResponse> medicineBatches = new ArrayList<BatchResponse>();
					List<Batch> batchList = new ArrayList<Batch>();
					// List<Integer>
					// batchMapperIds=patientConsumableIssueDao.getBatchMapperIds(issueDetail.getId());

					// List<IssueDetailBatchMapper> batchMapperList =
					// issueDetail.getIssueDetailBatchMapper();

					// List<IssueDetailBatchMapper> batchMapperList =
					// patientConsumableIssueDao.getBatchMapperList(batchMapperIds);

					if (issueDetail.getPatientIndentDetail() != null) {
						batchList = GlobalDao.getBatchDetailsPharmacyConsignment(
								issueDetail.getPatientIndentDetail().getItem().getId(), issue.getToStore().getStoreId(),
								unitId);
						for (Batch batch : batchList) {
							medicineBatches.add(Util.PopulateBatchWithStockWithFlag(batch, false));
						}
						Long stockAvailable = GlobalDao.getStoreStockByItemPharmacy(issue.getToStore().getStoreId(),
								issueDetail.getPatientIndentDetail().getItem().getId(), unitId);
						if (stockAvailable < 0) {
							stockAvailable = 0l;
						}
						Integer texId = issueDetail.getPatientIndentDetail().getItem().getSalesTax() != null ? issueDetail.getPatientIndentDetail().getItem().getSalesTax().getId():null;
						Double taxPercentage = issueDetail.getPatientIndentDetail().getItem().getSalesTax()!= null ? issueDetail.getPatientIndentDetail().getItem().getSalesTax().getTaxPercentage():null;
						Integer issueQuantity = (issueDetail.getPatientIndentDetail().getIndentQuantity()
								- issueDetail.getPatientIndentDetail().getPendingQuantity());
						PatientConsumableIssueItem item = new PatientConsumableIssueItem(issueDetail.getId(),
								issueDetail.getPatientIndentDetail().getItem().getId(),
								issueDetail.getPatientIndentDetail().getItem().getCode(),
								issueDetail.getPatientIndentDetail().getItem().getName(),
								issueDetail.getPatientIndentDetail().getItem().getGeneric() != null
										? issueDetail.getPatientIndentDetail().getItem().getGeneric().getName()
										: null,
								issueDetail.getPatientIndentDetail().getUomType() != null
										? issueDetail.getPatientIndentDetail().getUomType().getType()
										: null,
								issueDetail.getPatientIndentDetail().getUomUnit() != null
										? issueDetail.getPatientIndentDetail().getUomUnit().getUnits()
										: null,
								issueDetail.getPatientIndentDetail().getIndentQuantity(), issueQuantity, stockAvailable,
								medicineBatches, issueDetail.getPatientIndentDetail().getId(), issueDetail.getParentIssueDetailId(),
								issueDetail.getPatientIndentDetail().getUomType() != null
								? issueDetail.getPatientIndentDetail().getUomType().getId() 
								: null,
								issueDetail.getPatientIndentDetail().getUomUnit() != null
								? issueDetail.getPatientIndentDetail().getUomUnit().getId()
								: null,
								issueDetail.getPatientIndentDetail().getItem().getIsThreeUOMApplicable(),
								issueDetail.getPatientIndentDetail().getItem().getIsLasa() != null ? issueDetail.getPatientIndentDetail().getItem().getIsLasa() : false
						);
					
						if (issueDetail.getIssueDetailBatchMapper().size() > 0) {
							item.setBatchMapperId(issueDetail.getIssueDetailBatchMapper().getFirst().getId());
						}
						item.setTaxId(texId);
						item.setFormulationTypeId(issueDetail.getPatientIndentDetail().getItem().getFormulationType().getId());
						item.setTaxPercentage(taxPercentage);
						item.setStatus(issueDetail.getStatus().getStatus());
						item.setStatusId(issueDetail.getStatus().getId());
						item.setGenericId(issueDetail.getPatientIndentDetail().getItem().getGeneric() != null
								? issueDetail.getPatientIndentDetail().getItem().getGeneric().getId()
								: null);
						item.setBatchNo(batchList.size() > 0 ? batchList.getFirst().getBatchNo() : null);
						item.setExpiry(batchList.size() > 0 ? batchList.getFirst().getExpiryDate() : null);
						Integer productCategoryId = null;
						if (issueDetail.getPatientIndentDetail().getItem().getGeneric() != null) {
							productCategoryId = issueDetail.getPatientIndentDetail().getItem().getGeneric()
									.getProductCategory().getId();
						} else {
							productCategoryId = issueDetail.getPatientIndentDetail().getItem().getProductCategory()
									.getId();
						}
						item.setProductCategoryId(productCategoryId);
						item.setDose(issueDetail.getPatientIndentDetail().getItem().getStrength() != null
								? issueDetail.getPatientIndentDetail().getItem().getStrength()
								: null);
						item.setDoseUnit(issueDetail.getPatientIndentDetail().getItem().getStrengthUnit() != null
								? issueDetail.getPatientIndentDetail().getItem().getStrengthUnit().getUnits()
								: null);
						item.setStoreId(issue.getStore().getStoreId());
						if (patientSponsorDetails != null && patientSponsorDetails.size() > 0) {
							for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
								List<Integer> prodCatIds = new ArrayList<Integer>();
								prodCatIds.add(productCategoryId);

								List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
										sponsorDetail.getPaymentEntitlementId());
								if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
									if (pharmacyTariffs.size() > 1) {
										// throw exception?
										// Don't throw - get first object and set data.
									}

									PharmacyTariff pharmacyTariff = pharmacyTariffs.size() > 0 ? pharmacyTariffs.getFirst()
											: null;
									item.setMarkUpDown(pharmacyTariff.getMarkUpDown());
									item.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
									break;
								} else {
									item.setMarkUpDown(1); // default
									item.setMarkUpDownPer(0.00);
								}
							}
						}

						Integer assetCatId = 0;
						Integer prodCatId = 0;
						if (issueDetail.getPatientIndentDetail().getItem().getProductCategory() != null) {
							prodCatId = issueDetail.getPatientIndentDetail().getItem().getProductCategory().getId();
							assetCatId = issueDetail.getPatientIndentDetail().getItem().getProductCategory()
									.getAssetCategory().getId();
						} else {
							prodCatId = issueDetail.getPatientIndentDetail().getItem().getGeneric().getProductCategory()
									.getId();
							assetCatId = issueDetail.getPatientIndentDetail().getItem().getGeneric()
									.getProductCategory().getAssetCategory().getId();
						}
						PatientConsession patientConsession = null;

						if (issue.getEncounter() != null) {
							patientConsession = GlobalDao.getPatientConsessionEncounter(
									issueDetail.getPatientIndentDetail().getItem().getId(),
									issue.getEncounter().getEncounterId(),
									issue.getEncounter().getPatient().getPatientCategoryId(), assetCatId, prodCatId,
									null, null);
						} else if (issue.getAdmission() != null) {
							patientConsession = GlobalDao.getPatientConsessionAdmission(
									issueDetail.getPatientIndentDetail().getItem().getId(),
									issue.getAdmission().getAdmissionId(),
									issue.getAdmission().getPatient().getPatientCategoryId(), assetCatId, prodCatId);

						}

						if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
							item.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
							item.setMarkupStatus(patientConsession.isMarkupStatus());
						} else {
							item.setPatientClassPercentage(0.00);
							item.setMarkupStatus(false);
						}

						// itemList.add(item);
						/*
						 * start code for resolved HIS-10665 On patient issue acceptance indent issued
						 * qty shows wrong
						 */
						if (issueDetail.getIssueDetailBatchMapper().size() > 0) {
							for (IssueDetailBatchMapper batchMapper : issueDetail.getIssueDetailBatchMapper()) {
								itemList.add(fillPatientConsumableIssueItem(item, batchMapper, true)); // true for
																										// getPatientIndentDetail
							}
						} else {
							itemList.add(item);
						}
						/*
						 * end code for resolved HIS-10665 On patient issue acceptance indent issued qty
						 * shows wrong
						 */

					}

					if (issueDetail.getImplantIndentDetail() != null) {
						batchList = GlobalDao.getBatchDetailsPharmacyConsignment(
								issueDetail.getImplantIndentDetail().getItem().getId(), storeId, unitId);
						for (Batch batch : batchList) {
							medicineBatches.add(Util.PopulateBatchWithStockWithFlag(batch, false));
						}
						Long stockAvailable = GlobalDao.getStoreStockByItemPharmacy(storeId,
								issueDetail.getImplantIndentDetail().getItem().getId(), unitId);
						Integer issueQuantity = (issueDetail.getImplantIndentDetail().getIndentQuantity()
								- issueDetail.getImplantIndentDetail().getPendingQuantity());
						PatientConsumableIssueItem item = new PatientConsumableIssueItem(issueDetail.getId(),
								issueDetail.getImplantIndentDetail().getId(),
								issueDetail.getImplantIndentDetail().getItem().getId(),
								issueDetail.getImplantIndentDetail().getItem().getCode(),
								issueDetail.getImplantIndentDetail().getItem().getName(),
								issueDetail.getImplantIndentDetail().getItem().getGeneric() != null
										? issueDetail.getImplantIndentDetail().getItem().getGeneric().getName()
										: null,
								issueDetail.getImplantIndentDetail().getUomType() != null
										? issueDetail.getImplantIndentDetail().getUomType().getType()
										: null,
								issueDetail.getImplantIndentDetail().getUomUnit() != null
										? issueDetail.getImplantIndentDetail().getUomUnit().getUnits()
										: null,
								issueDetail.getImplantIndentDetail().getIndentQuantity(), issueQuantity, stockAvailable,
								medicineBatches);
						if (issueDetail.getIssueDetailBatchMapper().size() > 0) {
							item.setBatchMapperId(issueDetail.getIssueDetailBatchMapper().getFirst().getId());
						}
						item.setStatus(issueDetail.getStatus().getStatus());
						item.setStatusId(issueDetail.getStatus().getId());
						item.setBatchNo(batchList.size() > 0 ? batchList.getFirst().getBatchNo() : null);
						item.setExpiry(batchList.size() > 0 ? batchList.getFirst().getExpiryDate() : null);
						Integer productCategoryId = null;
						if (issueDetail.getImplantIndentDetail().getItem().getGeneric() != null) {
							productCategoryId = issueDetail.getImplantIndentDetail().getItem().getGeneric()
									.getProductCategory().getId();
						} else {
							productCategoryId = issueDetail.getImplantIndentDetail().getItem().getProductCategory()
									.getId();
						}
						item.setItemId(issueDetail.getImplantIndentDetail().getItem().getId());
						item.setProductCategoryId(productCategoryId);
						item.setStoreId(issue.getStore().getStoreId());
						item.setBatchNo(issueDetail.getBatch().getBatchNo());
						item.setExpiry(issueDetail.getBatch().getExpiryDate());
						if (patientSponsorDetails != null && patientSponsorDetails.size() > 0) {
							for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
								List<Integer> prodCatIds = new ArrayList<Integer>();
								prodCatIds.add(productCategoryId);

								List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
										sponsorDetail.getPaymentEntitlementId());
								if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
									if (pharmacyTariffs.size() > 1) {
										// throw exception?
										// Don't throw - get first object and set data.
									}

									PharmacyTariff pharmacyTariff = pharmacyTariffs.size() > 0 ? pharmacyTariffs.getFirst()
											: null;

									item.setMarkUpDown(pharmacyTariff.getMarkUpDown());
									item.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
									break;
								} else {
									item.setMarkUpDown(1); // default
									item.setMarkUpDownPer(0.00);
								}
							}
						}

						Integer assetCatId = 0;
						Integer prodCatId = 0;
						if (issueDetail.getPatientIndentDetail().getItem().getProductCategory() != null) {
							prodCatId = issueDetail.getPatientIndentDetail().getItem().getProductCategory().getId();
							assetCatId = issueDetail.getPatientIndentDetail().getItem().getProductCategory()
									.getAssetCategory().getId();
						} else {
							prodCatId = issueDetail.getPatientIndentDetail().getItem().getGeneric().getProductCategory()
									.getId();
							assetCatId = issueDetail.getPatientIndentDetail().getItem().getGeneric()
									.getProductCategory().getAssetCategory().getId();
						}

						PatientConsession patientConsession = GlobalDao.getPatientConsessionAdmission(
								issueDetail.getPatientIndentDetail().getItem().getId(),
								issue.getAdmission().getAdmissionId(),
								issue.getAdmission().getPatient().getPatientCategoryId(), assetCatId, prodCatId);

						if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
							item.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
							item.setMarkupStatus(patientConsession.isMarkupStatus());
						} else {
							item.setPatientClassPercentage(0.00);
							item.setMarkupStatus(false);
						}
						// itemList.add(item);

						/*
						 * start code for resolved HIS-10665 On patient issue acceptance indent issued
						 * qty shows wrong
						 */
						if (issueDetail.getIssueDetailBatchMapper().size() > 0) {
							for (IssueDetailBatchMapper batchMapper : issueDetail.getIssueDetailBatchMapper()) {
								itemList.add(fillPatientConsumableIssueItem(item, batchMapper, false)); // false for
																										// getImplantIndentDetail
							}
						} else {
							itemList.add(item);
						}
						/*
						 * end code for resolved HIS-10665 On patient issue acceptance indent issued qty
						 * shows wrong
						 */

					}

				}

			}

			patientConsumableIssueDetails.setItems(itemList);
			List<Carrier> carrierList = new ArrayList<Carrier>();
			for (User user : GlobalDao.getPickerUsers(unitId)) {
				Carrier carrier = new Carrier(user.getUserId(), user.getUserName());
				carrierList.add(carrier);
			}
			patientConsumableIssueDetails.setCarrier(carrierList);
			return patientConsumableIssueDetails;
		} else {

			String doctorDeatil = "";
			Integer drId = 0;
			if (issue.getVisitType().getVisitTypeId().equals(4)) {
				// doctor not found for emergency admission

			} else {
                if (issue.getDoctor() != null) {
    				doctorDeatil = issue.getDoctor().getFirstName() + " " + issue.getDoctor().getMiddleName() + " "
    						+ issue.getDoctor().getLastName();
    				drId = issue.getDoctor().getDoctorId();
                }
			}

			List<PatientSponsorDetails> patientSponsorDetails = null;
			PatientConsumableIssueDetails patientConsumableIssueDetails = new PatientConsumableIssueDetails();

			if (issue.getPatientIndent() != null) {
				String patientName = issue.getPatientIndent().getPatient().getFirstName() + " "
						+ issue.getPatientIndent().getPatient().getMiddleName() + " "
						+ issue.getPatientIndent().getPatient().getLastName();

				if (issue.getAdmission() != null) {
					patientConsumableIssueDetails = new PatientConsumableIssueDetails(issue.getId(),
							issue.getStore().getStoreName(), issue.getStore().getStoreId(),
							issue.getToStore().getStoreName(), issue.getToStore().getStoreId(),
							issue.getVisitType().getVisitTypeId(), issue.getVisitType().getVisitTypeCode(), patientName,
							issue.getPatientIndent().getPatient().getUhidNumber(),
							issue.getAdmission().getAdmissionNumber(), issue.getPatientIndent().getIndentNo(), drId,
							issue.getPatientIndent().getId());
					patientConsumableIssueDetails.setAdmissionId(issue.getAdmission().getAdmissionId());
					patientConsumableIssueDetails.setDoctorDetail(doctorDeatil);

					Date doa = null;
					Date pdd = null;
					AdmissionDetail admissionDetail = GlobalDao
							.getPatientWardBedDetails(issue.getAdmission().getAdmissionId());
					if (admissionDetail != null) {
						doa = admissionDetail.getDoa();
						pdd = admissionDetail.getPdd();
					}
					patientConsumableIssueDetails.setDoa(doa != null ? doa : null);
					patientConsumableIssueDetails.setDob(issue.getPatientIndent().getPatient().getDob());
					patientConsumableIssueDetails.setPdd(pdd != null ? pdd : null);
					patientConsumableIssueDetails.setWardName((issue.getPatientIndent().getWard() != null)
							? issue.getPatientIndent().getWard().getWardName()
							: null);
					patientConsumableIssueDetails.setBedNo((issue.getPatientIndent().getBed() != null)
							? issue.getPatientIndent().getBed().getBedNumber()
							: null);
					patientConsumableIssueDetails.setStatusId(issue.getStatus().getId());
					patientConsumableIssueDetails.setStatus(issue.getStatus().getStatus());
					patientConsumableIssueDetails.setIssueNo(issue.getIssueNo());
					patientConsumableIssueDetails.setIssueDate(issue.getAddedDate());

					if (issue != null && issue.getAdmission() != null)
						patientSponsorDetails = iPatientSponsorDetailsDao
								.findByAdmissionId(issue.getAdmission().getAdmissionId());
				} else if (issue.getEncounter() != null) {
					patientConsumableIssueDetails = new PatientConsumableIssueDetails(issue.getId(),
							issue.getStore().getStoreName(), issue.getStore().getStoreId(),
							issue.getToStore().getStoreName(), issue.getToStore().getStoreId(),
							issue.getVisitType().getVisitTypeId(), issue.getVisitType().getVisitTypeCode(), patientName,
							issue.getPatientIndent().getPatient().getUhidNumber(),
							issue.getEncounter().getEncounterNumber(), issue.getPatientIndent().getIndentNo(), drId);
					patientConsumableIssueDetails.setEncounterId(issue.getEncounter().getEncounterId());
					patientConsumableIssueDetails.setStatusId(issue.getStatus().getId());
					patientConsumableIssueDetails.setStatus(issue.getStatus().getStatus());
					patientConsumableIssueDetails.setIssueNo(issue.getIssueNo());
					patientConsumableIssueDetails.setIssueDate(issue.getAddedDate());
					patientConsumableIssueDetails.setDoctorDetail(doctorDeatil);
					if (issue != null && issue.getEncounter() != null)
						patientSponsorDetails = iPatientSponsorDetailsDao
								.findByEncounterId(issue.getEncounter().getEncounterId());
				}
			}
			if (issue.getImplantIndent() != null && issue.getIsImplant().equals(false)) {
				String patientName = issue.getImplantIndent().getPatient().getFirstName() + " "
						+ issue.getImplantIndent().getPatient().getMiddleName() + " "
						+ issue.getImplantIndent().getPatient().getLastName();

				patientConsumableIssueDetails = new PatientConsumableIssueDetails(issue.getId(),
						issue.getImplantIndent().getId(), issue.getStore().getStoreName(),
						issue.getStore().getStoreId(), issue.getToStore().getStoreName(),
						issue.getToStore().getStoreId(), issue.getVisitType().getVisitTypeId(),
						issue.getVisitType().getVisitTypeCode(), patientName,
						issue.getImplantIndent().getPatient().getUhidNumber(),
						issue.getAdmission().getAdmissionNumber(), issue.getImplantIndent().getImplantIndentNo(), drId);
				patientConsumableIssueDetails.setAdmissionId(issue.getAdmission().getAdmissionId());
				patientConsumableIssueDetails.setStatusId(issue.getStatus().getId());
				patientConsumableIssueDetails.setStatus(issue.getStatus().getStatus());
				patientConsumableIssueDetails.setIssueNo(issue.getIssueNo());
				patientConsumableIssueDetails.setIssueDate(issue.getAddedDate());
				patientConsumableIssueDetails.setDoctorDetail(doctorDeatil);
			}

			List<PatientConsumableIssueItem> itemList = new ArrayList<PatientConsumableIssueItem>();
			List<Integer> ids = patientConsumableIssueDao.getIssueDetailids(id, unitId);
			List<IssueDetail> issueDetailData = patientConsumableIssueDao.getpatientConsumableIssueDetailByIds(ids,
					screenFlag, unitId);
			// for (IssueDetail issueDetail : issueDetailData) {
			for (IssueDetail issueDetail : issue.getIssueDetail()) {
				List<SubstitutePatientConsumableDto> subItemsList=new ArrayList<SubstitutePatientConsumableDto>();
//				if(issueDetail.getParentIssueDetailId()!=null) {
	                List<SubstitutePatientConsumableDto> issueDetailSub=patientConsumableIssueDao.getSubstituteIssueDetails(issueDetail.getIssue().getId());
				
				for (SubstitutePatientConsumableDto substituteIssueDetail : issueDetailSub) {
					if(substituteIssueDetail.getParentIssueDetailId().equals(issueDetail.getId())) {
					List<BatchResponse> bth=new ArrayList<BatchResponse>();
					List<IssueDetailBatchMapper> bthMappr=patientConsumableIssueDao.getBatchMapperForSubstitute(substituteIssueDetail.getIssueDetailId());
					Long stockAvailable = GlobalDao.getStoreStockByItemPharmacy(issue.getToStore().getStoreId(),
							substituteIssueDetail.getItemId(), unitId);
					if (stockAvailable < 0) {
						stockAvailable = 0l;
					}
					for (IssueDetailBatchMapper batchMapper : bthMappr) {
						// itemList.add(fillPatientConsumzableIssueItem(item,batchMapper,true)); // true
						// for getPatientIndentDetail
								BatchResponse b=new BatchResponse();
								b.setIsSelected(true);
								b.setIssueQuantity(batchMapper.getIssueQuantity());
								b.setBatchNo(batchMapper.getBatch().getBatchNo());
								b.setExpiry(batchMapper.getBatch().getExpiryDate());
								b.setSellingPrice(batchMapper.getBatch().getSellingPrice());
								b.setAvailableStockQuantity(batchMapper.getBatch().getCurrentQuantity());
								if(batchMapper.getUomType()!=null)
								{
									b.setUomType(batchMapper.getUomType().getType());
								}
								if(batchMapper.getUomUnit()!=null)
								{
									b.setUomUnit(batchMapper.getUomUnit().getUnits());
								}
								b.setUom(GlobalDao.getUnitOfMeasurementListByItem(substituteIssueDetail.getItemId(),  sessionService.getOrganizationId()));
								bth.add(b);

					}
					substituteIssueDetail.setAvailableStockQuantity(stockAvailable.intValue());
					substituteIssueDetail.setNewBatch(bth);
					subItemsList.add(substituteIssueDetail);
				}
				}
//			}
				
				// where IssueDetail.parentId=issueDetail.getId()
				if (issueDetail.getPatientIndentDetail().getIsDeleted() == false) {

					List<BatchResponse> medicineBatches = new ArrayList<BatchResponse>();
					List<Batch> batchList = new ArrayList<Batch>();
					List<Batch> batchList1 = new ArrayList<Batch>();
					// List<Integer>
					// batchMapperIds=patientConsumableIssueDao.getBatchMapperIds(issueDetail.getId());

					// List<IssueDetailBatchMapper> batchMapperList =
					// issueDetail.getIssueDetailBatchMapper();

					// List<IssueDetailBatchMapper> batchMapperList =
					// patientConsumableIssueDao.getBatchMapperList(batchMapperIds);

					if (issueDetail.getPatientIndentDetail() != null) {
						batchList = GlobalDao.getBatchDetailsPharmacyConsignment(
								issueDetail.getPatientIndentDetail().getItem().getId(), issue.getToStore().getStoreId(),
								unitId);
						
						batchList1 = GlobalDao.getBatchDetailsPharmacyConsignment1(
								issueDetail.getPatientIndentDetail().getItem().getId(), issue.getToStore().getStoreId(),
								unitId);
						for (Batch batch : batchList1) {
							medicineBatches.add(Util.PopulateBatchWithStockWithFlag(batch, false));
						}
						Long stockAvailable = GlobalDao.getStoreStockByItemPharmacy(issue.getToStore().getStoreId(),
								issueDetail.getPatientIndentDetail().getItem().getId(), unitId);
						if (stockAvailable < 0) {
							stockAvailable = 0l;
						}

						Integer issueQuantity = (issueDetail.getPatientIndentDetail().getIndentQuantity()
								- issueDetail.getPatientIndentDetail().getPendingQuantity());
						PatientConsumableIssueItem item = new PatientConsumableIssueItem(issueDetail.getId(),
								issueDetail.getPatientIndentDetail().getItem().getId(),
								issueDetail.getPatientIndentDetail().getItem().getCode(),
								issueDetail.getPatientIndentDetail().getItem().getName(),
								issueDetail.getPatientIndentDetail().getItem().getGeneric() != null
										? issueDetail.getPatientIndentDetail().getItem().getGeneric().getName()
										: null,
								issueDetail.getPatientIndentDetail().getUomType() != null
										? issueDetail.getPatientIndentDetail().getUomType().getType()
										: null,
								issueDetail.getPatientIndentDetail().getUomUnit() != null
										? issueDetail.getPatientIndentDetail().getUomUnit().getUnits()
										: null,
								issueDetail.getPatientIndentDetail().getIndentQuantity(), issueQuantity, stockAvailable,
								medicineBatches, issueDetail.getPatientIndentDetail().getId(), issueDetail.getParentIssueDetailId(),
								issueDetail.getPatientIndentDetail().getUomType() != null
								? issueDetail.getPatientIndentDetail().getUomType().getId() 
								: null,
								issueDetail.getPatientIndentDetail().getUomUnit() != null
								? issueDetail.getPatientIndentDetail().getUomUnit().getId()
								: null,
								issueDetail.getPatientIndentDetail().getItem().getIsThreeUOMApplicable(),
								issueDetail.getPatientIndentDetail().getItem().getIsLasa() != null ? issueDetail.getPatientIndentDetail().getItem().getIsLasa() : false
						);
						if (issueDetail.getIssueDetailBatchMapper().size() > 0) {
							item.setBatchMapperId(issueDetail.getIssueDetailBatchMapper().getFirst().getId());
						}
						item.setSelectedItems(subItemsList);
						item.setFormulationTypeId(issueDetail.getPatientIndentDetail().getItem().getFormulationType().getId());

						item.setPatientConsumableIssueItemSub(null);
						item.setStatus(issueDetail.getStatus().getStatus());
						item.setStatusId(issueDetail.getStatus().getId());
						item.setGenericId(issueDetail.getPatientIndentDetail().getItem().getGeneric() != null
								? issueDetail.getPatientIndentDetail().getItem().getGeneric().getId()
								: null);
						item.setBatchNo(batchList.size() > 0 ? batchList.getFirst().getBatchNo() : null);
						item.setExpiry(batchList.size() > 0 ? batchList.getFirst().getExpiryDate() : null);
						Integer productCategoryId = null;
						if (issueDetail.getPatientIndentDetail().getItem().getGeneric() != null) {
							productCategoryId = issueDetail.getPatientIndentDetail().getItem().getGeneric()
									.getProductCategory().getId();
						} else {
							productCategoryId = issueDetail.getPatientIndentDetail().getItem().getProductCategory()
									.getId();
						}
						item.setProductCategoryId(productCategoryId);
						item.setStoreId(issue.getStore().getStoreId());
						item.setDose(issueDetail.getPatientIndentDetail().getItem().getStrength() != null
								? issueDetail.getPatientIndentDetail().getItem().getStrength()
								: null);
						item.setDoseUnit(issueDetail.getPatientIndentDetail().getItem().getStrengthUnit() != null
								? issueDetail.getPatientIndentDetail().getItem().getStrengthUnit().getUnits()
								: null);
						if (patientSponsorDetails != null && patientSponsorDetails.size() > 0) {
							for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
								List<Integer> prodCatIds = new ArrayList<Integer>();
								prodCatIds.add(productCategoryId);

								List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
										sponsorDetail.getPaymentEntitlementId());
								if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
									if (pharmacyTariffs.size() > 1) {
										// throw exception?
										// Don't throw - get first object and set data.
									}

									PharmacyTariff pharmacyTariff = pharmacyTariffs.size() > 0 ? pharmacyTariffs.getFirst()
											: null;
									item.setMarkUpDown(pharmacyTariff.getMarkUpDown());
									item.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
									break;
								} else {
									item.setMarkUpDown(1); // default
									item.setMarkUpDownPer(0.00);
								}
							}
						}

						Integer assetCatId = 0;
						Integer prodCatId = 0;
						if (issueDetail.getPatientIndentDetail().getItem().getProductCategory() != null) {
							prodCatId = issueDetail.getPatientIndentDetail().getItem().getProductCategory().getId();
							assetCatId = issueDetail.getPatientIndentDetail().getItem().getProductCategory()
									.getAssetCategory().getId();
						} else {
							prodCatId = issueDetail.getPatientIndentDetail().getItem().getGeneric().getProductCategory()
									.getId();
							assetCatId = issueDetail.getPatientIndentDetail().getItem().getGeneric()
									.getProductCategory().getAssetCategory().getId();
						}
						PatientConsession patientConsession = null;

						if (issue.getEncounter() != null) {
							patientConsession = GlobalDao.getPatientConsessionEncounter(
									issueDetail.getPatientIndentDetail().getItem().getId(),
									issue.getEncounter().getEncounterId(),
									issue.getEncounter().getPatient().getPatientCategoryId(), assetCatId, prodCatId,
									null, null);
						} else if (issue.getAdmission() != null) {
							patientConsession = GlobalDao.getPatientConsessionAdmission(
									issueDetail.getPatientIndentDetail().getItem().getId(),
									issue.getAdmission().getAdmissionId(),
									issue.getAdmission().getPatient().getPatientCategoryId(), assetCatId, prodCatId);

						}

						if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
							item.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
							item.setMarkupStatus(patientConsession.isMarkupStatus());
						} else {
							item.setPatientClassPercentage(0.00);
							item.setMarkupStatus(false);
						}

						// itemList.add(item);
						/*
						 * start code for resolved HIS-10665 On patient issue acceptance indent issued
						 * qty shows wrong
						 */
						if (issueDetail.getIssueDetailBatchMapper().size() > 0) {
							
							List<PatientConsumableIssueItem> b2=new ArrayList<PatientConsumableIssueItem>();
							List<IssueDetailBatchMapper>ab=new ArrayList<IssueDetailBatchMapper>();
							for (IssueDetailBatchMapper batchMapper : issueDetail.getIssueDetailBatchMapper()) {
								// itemList.add(fillPatientConsumableIssueItem(item,batchMapper,true)); // true
								// for getPatientIndentDetail
								PatientConsumableIssueItem ba=new PatientConsumableIssueItem();
                                ba.setBatchId(batchMapper.getBatch().getId());
                                ba.setIssueQuantity(batchMapper.getIssueQuantity());
                                b2.add(ba);
                                
								for (BatchResponse b : medicineBatches) {
									if (b.getBatchId().equals(batchMapper.getBatch().getId())) {
//										Integer getTotalQuantity=GlobalDao.getTotalQuantity(issueDetail.getPatientIndentDetail().getItem().getId(),issueDetail.getId(),
//												storeId,unitId,batchMapper.getBatch().getId());
											b.setIsSelected(true);
											b.setIssueQuantity(batchMapper.getIssueQuantity());
											if(batchMapper.getUomType()!=null)
											{
												b.setUomType(batchMapper.getUomType().getType());
											}
											if(batchMapper.getUomUnit()!=null)
											{
												b.setUomUnit(batchMapper.getUomUnit().getUnits());
											}
											item.setSelected(true);
									}

								}
							}
							item.setBatchIssued(b2);
						} else {
							// itemList.add(item);
						}
						itemList.add(item);
						/*
						 * end code for resolved HIS-10665 On patient issue acceptance indent issued qty
						 * shows wrong
						 */

					}

				}

			}

			patientConsumableIssueDetails.setItems(itemList);
			List<Carrier> carrierList = new ArrayList<Carrier>();
			for (User user : GlobalDao.getPickerUsers(unitId)) {
				Carrier carrier = new Carrier(user.getUserId(), user.getUserName());
				carrierList.add(carrier);
			}
			patientConsumableIssueDetails.setCarrier(carrierList);
			patientConsumableIssueDetails
					.setCarrierId(issue.getCarrier() != null ? issue.getCarrier().getUserId() : null);
			return patientConsumableIssueDetails;

		}

	}

	public Object patientConsumableIssueSave(PatientConsumableIssueSave patientConsumableIssueSave) throws Exception {
		Boolean isAcceptance = false;
		try {
			ApplicationConfiguration app = grnDao.getApplicationConfiguration(patientConsumableIssueSave.getUnitId());
			if (app != null && app.getIsAutoConsumableAcceptance() != null) {
				isAcceptance = app.getIsAutoConsumableAcceptance();
			}

		
			
			patientConsumableIssueSave.setForceAddRemoved(0);
			Issue issue = patientConsumableIssueDao.patientConsumableIssue(patientConsumableIssueSave.getId());

			PatientIndent patientIndent = issue.getPatientIndent();
			patientIndent.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
			/*
			 * Integer patientId = patientConsumableIssueSave.getPatientId(); Integer
			 * admissionId = issue.getAdmission().getAdmissionId(); Integer visitTypeId =
			 * issue.getAdmission().getVisitType().getVisitTypeId();
			 */
			// Integer patientId=null;

			VisitType visitType = null;
			SaleType saleType = null;
			if (patientConsumableIssueSave.getVisitTypeId() != null)
				visitType = GlobalDao.getVisitType(patientConsumableIssueSave.getVisitTypeId());
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

			// Save sale
			/*
			 * Store storeNew = GlobalDao.getStore(patientConsumableIssueSave.getStoreId());
			 * Unit unit = GlobalDao.getUnit(sessionService.getUnitId());
			 */
			String billNo = GlobalService.getBillNoByGeneratedCode(patientConsumableIssueSave.getStoreId(),
					patientConsumableIssueSave.getUnitId(), screenIdWalkInSale);

			String issuetNumber = GlobalService.getBillNoByGeneratedCode(patientConsumableIssueSave.getStoreId(),
					patientConsumableIssueSave.getUnitId(), issueScreenId);
			Issue newIssue = null;

			if (visitType.getVisitTypeId().equals(ER)) {
				newIssue = new Issue(issuetNumber, new VisitType(patientIndent.getVisitType().getVisitTypeId()),
						new Store(patientIndent.getStore().getStoreId()),
						new Store(patientIndent.getToStore().getStoreId()), new PatientIndent(patientIndent.getId()),
						new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
			} else {

                Integer doctorId = 0;
                if (patientIndent.getDoctor() != null) {
                    doctorId = patientIndent.getDoctor().getDoctorId();
                }
				newIssue = new Issue(issuetNumber, new VisitType(patientIndent.getVisitType().getVisitTypeId()),
						new Doctor(doctorId),
						new Store(patientIndent.getStore().getStoreId()),
						new Store(patientIndent.getToStore().getStoreId()), new PatientIndent(patientIndent.getId()),
						new Status(com.param.common.constants.Status.PartiallyCompleted.getStatus()));
			}

			// sessionService.setEntityDefaults(newIssue);
			newIssue.setCreateDefaults(
					patientConsumableIssueSave.getOrganizationId() != null
							? patientConsumableIssueSave.getOrganizationId()
							: sessionService.getOrganizationId(),
					patientConsumableIssueSave.getUnitId() != null ? patientConsumableIssueSave.getUnitId()
							: sessionService.getUnitId(),
					patientConsumableIssueSave.getUserId() != null ? patientConsumableIssueSave.getUserId()
							: sessionService.getUserId());

			if (patientIndent.getAdmission() != null) {
				newIssue.setAdmission(new Admission(patientIndent.getAdmission().getAdmissionId()));
			} else if (patientIndent.getEncounter() != null) {
				newIssue.setEncounter(new Encounter(patientIndent.getEncounter().getEncounterId()));
			}
			newIssue.setIsImplant(false);
			newIssue.setIsImplant(false);
			newIssue.setIsConsignment(false);

			Boolean saveIssue = false;
			
			for (PatientConsumableIssueSaveItem item : patientConsumableIssueSave.getItems()) {
				
				if(item.getId()!=null)
				{
					IssueDetail issueDetail = patientConsumableIssueDao.patientConsumableIssueDetail(item.getId());
					issueDetail.setTaxAmount(item.getTaxAmount());
					issueDetail.setTaxPercentage(item.getTaxPercentage());
					// added flag for resolved multiple issueDetail entry
					// found during resolved HIS-10988 On patient Indent status not getting change
					boolean issueDetailFlag = true;
					// end flag for resolved multiple issueDetail entry

					/* new code start for clubbatch and consignmentlogic by minesh */
					for (BatchResponse batch : item.getBatchList()) {
						List<StoreStock> StoreStockList = GlobalDao.getStoreStockByBatchAndStoreList(batch.getBatchId(), issue.getToStore().getStoreId(), patientConsumableIssueSave.getUnitId());
						/*List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(batch.getBatchId(),
								issue.getToStore().getStoreId(), patientConsumableIssueSave.getUnitId());*/
						int issueQtyFromRequest = batch.getIssueQuantity();
						for (StoreStock storeStockObj : StoreStockList) {
							if (issueQtyFromRequest != 0) {
								Long storeAvaliableQuantity = Util.ConvertQuantity(
										Long.valueOf(storeStockObj.getAvailableQty()),
										storeStockObj.getBatch().getBatchUomList(), new UomType(batch.getUomTypeId()));
								Long batchId = storeStockObj.getBatch().getId();
								if (issueQtyFromRequest <= storeAvaliableQuantity) {
									IssueDetailBatchMapper issueDetailBatchMapper = clubBatchStoreTransactionWithBatchMapper(
											issue, item, batch, issueQtyFromRequest, batchId,
											patientConsumableIssueSave.getUnitId(), patientConsumableIssueSave, issueDetail.getPatientIndentDetail().getItem(),issueDetail);
									issueDetail.addBatch(issueDetailBatchMapper);
									issueQtyFromRequest = 0;
								} else {
									int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();
									IssueDetailBatchMapper issueDetailBatchMapper = clubBatchStoreTransactionWithBatchMapper(
											issue, item, batch, storeAvaliableQuantityINT, batchId,
											patientConsumableIssueSave.getUnitId(), patientConsumableIssueSave , issueDetail.getPatientIndentDetail().getItem(),issueDetail);
									issueDetail.addBatch(issueDetailBatchMapper);
									issueQtyFromRequest = issueQtyFromRequest - storeAvaliableQuantityINT;
								}
							} // end if (issueQtyFromRequest != 0)
						} // end for (StoreStock storeStockObj : StoreStockList)
						/* new code end for clubbatch and consignmentlogic by minesh */

						PatientIndentDetail indDtls = issueDetail.getPatientIndentDetail();
						int indentQty = indDtls.getIndentQuantity();
						Integer qty = (indentQty - indDtls.getPendingQuantity()) + batch.getIssueQuantity();

						indDtls.setPendingQuantity(indDtls.getPendingQuantity() - batch.getIssueQuantity());

						if (qty.equals(indentQty)) {
							issueDetail.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
						} else if (batch.getIssueQuantity() > 0 && batch.getIssueQuantity() < indentQty) {
							// create new issue details
							issueDetail.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));

							if (issueDetailFlag) {// added flag for resolved multiple issueDetail entry(resolved HIS-10988)

								IssueDetail newIssueDetail = new IssueDetail(new PatientIndentDetail(indDtls.getId()),
										new Status(com.param.common.constants.Status.Pending.getStatus()));
								// sessionService.setEntityDefaults(newIssueDetail);
								newIssueDetail.setCreateDefaults(
										patientConsumableIssueSave.getOrganizationId() != null
												? patientConsumableIssueSave.getOrganizationId()
												: sessionService.getOrganizationId(),
										patientConsumableIssueSave.getUnitId() != null
												? patientConsumableIssueSave.getUnitId()
												: sessionService.getUnitId(),
										patientConsumableIssueSave.getUserId() != null
												? patientConsumableIssueSave.getUserId()
												: sessionService.getUserId());

								newIssue.addIssueDetail(newIssueDetail);

							}

							saveIssue = true;
						} else if (batch.getIssueQuantity() > indentQty) {
							throw new CustomException("Issued quantity can not be greater than indent quantity");
						} else {
							// create new issue details. delete current issue details
							issueDetail.setIsDeleted(true);
							if (issueDetailFlag) {// added flag for resolved multiple issueDetail entry(resolved HIS-10988)

								IssueDetail newIssueDetail = new IssueDetail(new PatientIndentDetail(indDtls.getId()),
										new Status(com.param.common.constants.Status.Pending.getStatus()));
								// sessionService.setEntityDefaults(newIssueDetail);
								newIssueDetail.setCreateDefaults(
										patientConsumableIssueSave.getOrganizationId() != null
												? patientConsumableIssueSave.getOrganizationId()
												: sessionService.getOrganizationId(),
										patientConsumableIssueSave.getUnitId() != null
												? patientConsumableIssueSave.getUnitId()
												: sessionService.getUnitId(),
										patientConsumableIssueSave.getUserId() != null
												? patientConsumableIssueSave.getUserId()
												: sessionService.getUserId());

								newIssue.addIssueDetail(newIssueDetail);

							}

							saveIssue = true;
						}
						// issueDetail.addBatch(issueDetailBatchMapper);
						issueDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(patientConsumableIssueSave.getUnitId()));
						issueDetail.setUpdatedByUser(
								patientConsumableIssueSave.getUserId() != null ? patientConsumableIssueSave.getUserId()
										: sessionService.getUserId());

						// added flag for resolved multiple issueDetail entry
						// found during resolved HIS-10988 On patient Indent status not getting change
						issueDetailFlag = false;
						// end flag for resolved multiple issueDetail entry
					}	
				}
				if(item.getId()==null && item.getParentId()!=null)
				{
					
					IssueDetail IssueDetailSub=new IssueDetail();
					IssueDetail issueDetail = patientConsumableIssueDao.patientConsumableIssueDetail(item.getParentId());
					PatientIndentDetail pd=issueDetail.getPatientIndentDetail();
					PatientIndentDetail pid=new PatientIndentDetail();
					pid.setPatientIndent(pd.getPatientIndent());
					pid.setIndentQuantity(item.getIndentQuantity());
					Item itm=new Item();
					itm.setId(item.getItemId());
					pid.setItem(itm);
					pid.setIsActive(true);
					pid.setIsDeleted(false);
					pid.setPendingQuantity((pd.getIndentQuantity()!=null ? pd.getIndentQuantity() : 0 )-(item.getQuantity()!=null ? item.getQuantity() : 0) );

					UomType uomType=new UomType();
					uomType.setId(item.getUomTypeId());
					pid.setUomType(uomType);
					UomUnit uomUnit=new UomUnit();
					uomUnit.setId(item.getUomUnitId());
					pid.setUomUnit(uomUnit);
					
					pid.setCreateDefaults(patientConsumableIssueSave.getOrganizationId(), patientConsumableIssueSave.getUnitId(), patientConsumableIssueSave.getUserId());
					List<PatientIndentDetail> listd=new ArrayList<>();
					listd.add(pid);
					patientIndent.setPatientIndentDetail(listd);
					IssueDetailSub.setPatientIndentDetail(pid);
					
					
					IssueDetailSub.setIssue(issueDetail.getIssue());
					//IssueDetailSub.setPatientIndentDetail(issueDetail.getPatientIndentDetail());
					IssueDetailSub.setParentIssueDetailId(issueDetail.getId());
					IssueDetailSub.setNetAmount(item.getNetAmount());
					IssueDetailSub.setConsessionAmount(item.getConsessionAmount());
					IssueDetailSub.setConsessionPercentage(item.getConsessionPercentage());
					IssueDetailSub.setRate(item.getRate());
					IssueDetailSub.setSelfPayable(item.getSelfPayable());
					Status sts=new Status();
					sts.setId(item.getStatusId());
					IssueDetailSub.setStatus(sts);
					IssueDetailSub.setTaxAmount(item.getTaxAmount());
					IssueDetailSub.setTaxPercentage(item.getTaxPercentage());
					IssueDetailSub.setTotalAmount(item.getTotalAmount());
					UomType uomTypeInd=new UomType();
					uomTypeInd.setId(item.getUomTypeId());
					IssueDetailSub.setUomType(uomType);
					UomUnit uomUnitInd=new UomUnit();
					uomUnitInd.setId(item.getUomTypeId());
					IssueDetailSub.setUomUnit(uomUnit);
					IssueDetailSub.setIsActive(true);
					IssueDetailSub.setIsDeleted(false);
					Batch bth=new Batch();
					bth.setId(item.getBatchId());
					IssueDetailSub.setBatch(bth);
					IssueDetailSub.setIssueQuantity((item.getQuantity()!=null ? item.getQuantity() : 0));
					IssueDetailSub.setIsSubstitute(true);
					IssueDetailSub.setCreateDefaults(patientConsumableIssueSave.getOrganizationId(), patientConsumableIssueSave.getUnitId(), patientConsumableIssueSave.getUserId());
					IssueDetail issDtl=GlobalDao.saveIssueDetailForSubstitute(IssueDetailSub);
					item.setId(issDtl.getId());
					
					issue.addIssueDetail(IssueDetailSub);
					// added flag for resolved multiple issueDetail entry
					// found during resolved HIS-10988 On patient Indent status not getting change
					boolean issueDetailFlag = true;
					// end flag for resolved multiple issueDetail entry

					/* new code start for clubbatch and consignmentlogic by minesh */
					for (BatchResponse batch : item.getBatchList()) {
						List<StoreStock> StoreStockList = GlobalDao.getStoreStockByBatchAndStoreList(batch.getBatchId(), issue.getToStore().getStoreId(), patientConsumableIssueSave.getUnitId());
						/*List<StoreStock> StoreStockList = GlobalDao.getStoreStockListByBatchIdAndStoreId(batch.getBatchId(),
								issue.getToStore().getStoreId(), patientConsumableIssueSave.getUnitId());*/
						int issueQtyFromRequest = batch.getIssueQuantity();
						for (StoreStock storeStockObj : StoreStockList) {
							if (issueQtyFromRequest != 0) {
								Long storeAvaliableQuantity = Util.ConvertQuantity(
										Long.valueOf(storeStockObj.getAvailableQty()),
										storeStockObj.getBatch().getBatchUomList(), new UomType(batch.getUomTypeId()));
								Long batchId = storeStockObj.getBatch().getId();
								if (issueQtyFromRequest <= storeAvaliableQuantity) {
									IssueDetailBatchMapper issueDetailBatchMapper = clubBatchStoreTransactionWithBatchMapper(
											issue, item, batch, issueQtyFromRequest, batchId,
											patientConsumableIssueSave.getUnitId(), patientConsumableIssueSave , issDtl.getPatientIndentDetail().getItem(),issDtl);
									IssueDetailSub.addBatch(issueDetailBatchMapper);
									issueQtyFromRequest = 0;
								} else {
									int storeAvaliableQuantityINT = storeAvaliableQuantity.intValue();
									IssueDetailBatchMapper issueDetailBatchMapper = clubBatchStoreTransactionWithBatchMapper(
											issue, item, batch, storeAvaliableQuantityINT, batchId,
											patientConsumableIssueSave.getUnitId(), patientConsumableIssueSave , issDtl.getPatientIndentDetail().getItem(),issDtl);
									IssueDetailSub.addBatch(issueDetailBatchMapper);
									issueQtyFromRequest = issueQtyFromRequest - storeAvaliableQuantityINT;
								}
							} // end if (issueQtyFromRequest != 0)
						} // end for (StoreStock storeStockObj : StoreStockList)
						/* new code end for clubbatch and consignmentlogic by minesh */

						  
						// issueDetail.addBatch(issueDetailBatchMapper);
						issueDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(patientConsumableIssueSave.getUnitId()));
						issueDetail.setUpdatedByUser(
								patientConsumableIssueSave.getUserId() != null ? patientConsumableIssueSave.getUserId()
										: sessionService.getUserId());

						// added flag for resolved multiple issueDetail entry
						// found during resolved HIS-10988 On patient Indent status not getting change
						issueDetailFlag = false;
						// end flag for resolved multiple issueDetail entry
					}
				}
				
				// end for (BatchResponse batch : item.getBatchList())
			}

			List<Integer> ids = new ArrayList<>();

			for (PatientConsumableIssueSaveItem item : patientConsumableIssueSave.getItems()) {
				if(item.getId()!=null) {
					ids.add(item.getId());
				}
				
			}

			/*
			 * List<IssueDetail> issueDetails =
			 * patientConsumableIssueDao.patientConsumableIssueDetail(
			 * patientConsumableIssueSave.getId(),ids);
			 * 
			 * if(issueDetails.size() >0 ) { for(IssueDetail issueD : issueDetails) {
			 * issueD.setIsDeleted(true);
			 * patientConsumableIssueDao.updateIssueDetail(issueD);
			 * 
			 * }
			 * 
			 * }
			 */

			// Check Contract
			patientConsumableIssueSave.setOrganizationId(patientConsumableIssueSave.getOrganizationId());
			patientConsumableIssueSave.setUnitId(patientConsumableIssueSave.getUnitId());

			List<PatientConsumableIssueSaveItem> finalItemList=new ArrayList<PatientConsumableIssueSaveItem>();
			for(PatientConsumableIssueSaveItem item:patientConsumableIssueSave.getItems()) {
				
				for(BatchResponse batch:item.getBatchList()) {
					List<BatchResponse> batchList=new ArrayList<BatchResponse>();
					item.setBatchId(batch.getBatchId());
					item.setRate(batch.getRate());
					item.setTotalAmount(batch.getRate()*batch.getIssueQuantity());
					item.setConsumableQuantity(batch.getIssueQuantity());
					item.setQuantity(batch.getIssueQuantity());
					batchList.add(batch);
					item.setBatchList(batchList);
					PatientConsumableIssueSaveItem newItem=new PatientConsumableIssueSaveItem();
					newItem=(PatientConsumableIssueSaveItem) item.clone();
					finalItemList.add(newItem);
				}
			}
			patientConsumableIssueSave.setItems(finalItemList);
			
			List itemList = patientConsumableIssueSave.getItems();
			patientConsumableIssueSave.setItemList(itemList);
//			boolean taxInclusive = sessionService.getCookieObjectAppConfig().getTaxInclusive()== null ? false : sessionService.getCookieObjectAppConfig().getTaxInclusive();
			UnitApplicationConfigurationDto appConfigDto = iOrderMasterService.getPharmacyServiceId(patientConsumableIssueSave.getOrganizationId(),patientConsumableIssueSave.getUnitId());
			boolean taxInclusive = appConfigDto.getTaxInclusive();


			patientConsumableIssueSave.setTaxInclusive(taxInclusive);
			Response patientIssueSaveRes = iMPackageMasterService
					.consumeItemFromPackageContract(patientConsumableIssueSave,0);

			PatientConsumableIssueSave patientConsumableIssueContract = (PatientConsumableIssueSave) patientIssueSaveRes
					.getObject();
			Double totalSelfPayable=0.00;
			Double totalCreditPayable=0.00;
			Double totalAmount=0.00;
			Double tatalNetAmount=0.00;
			Double totalTax=0.00;
					if(patientConsumableIssueContract!=null)
					{
			 totalSelfPayable = patientConsumableIssueContract.getItemList() .stream()
					.filter(i -> i.getSelfPayable() != null).mapToDouble(i -> i.getSelfPayable()).sum();

			 totalCreditPayable = patientConsumableIssueContract.getItemList().stream()
					.filter(i -> i.getCreditPayable() != null).mapToDouble(i -> i.getCreditPayable()).sum();

			 totalAmount = patientConsumableIssueContract.getItemList().stream()
					.filter(i -> i.getTotalAmount() != null).mapToDouble(i -> i.getTotalAmount()).sum();

			 tatalNetAmount = patientConsumableIssueContract.getItemList().stream()
					.filter(i -> i.getNetAmount() != null).mapToDouble(i -> i.getNetAmount()).sum();

			 totalTax = patientConsumableIssueContract.getItemList().stream()
					.filter(i -> i.getTaxAmount() != null).mapToDouble(i -> i.getTaxAmount()).sum();
					}
			Sale sale = new Sale(
					patientConsumableIssueSave.getAdmissionId() != null
							? new Admission(patientConsumableIssueSave.getAdmissionId())
							: null,
					billNo, tatalNetAmount, 0.00,
					new com.param.entity.model.master.Status(com.param.common.constants.Status.Pending.getStatus()),
					patientIndent.getToStore(), totalTax, totalAmount, tatalNetAmount, null,
					totalSelfPayable, totalCreditPayable, Screen.PatientIssueConsumable.getScreen());

			sale.setCop(patientConsumableIssueSave.getAtCop());
			sale.setPatient(new PatientRegistration(patientConsumableIssueSave.getPatientId()));
			sale.setSaleType(saleType);
			sale.setFromPatientIndent(true);
			sale.setVisitType(visitType);
			sale.setFromBillableConsumption(false);
			if (patientConsumableIssueSave.getAdmissionId() != null)
				sale.setAdmission(new Admission(patientConsumableIssueSave.getAdmissionId()));
			else if (patientConsumableIssueSave.getEncounterId() != null)
				sale.setEncounter(new Encounter(patientConsumableIssueSave.getEncounterId()));

			sale.setStatus(new Status(com.param.common.constants.Status.Pending.getStatus()));
			// sessionService.setEntityDefaults(sale);
			sale.setCreateDefaults(
					patientConsumableIssueSave.getOrganizationId() != null
							? patientConsumableIssueSave.getOrganizationId()
							: sessionService.getOrganizationId(),
					patientConsumableIssueSave.getUnitId() != null ? patientConsumableIssueSave.getUnitId()
							: sessionService.getUnitId(),
					patientConsumableIssueSave.getUserId() != null ? patientConsumableIssueSave.getUserId()
							: sessionService.getUserId());

			for (CommonBillingItemsDto item : patientConsumableIssueSave.getItemList()) {
				for (BatchResponse batch : item.getBatchList()) {
					// IssueDetail issueDetail =
					// patientConsumableIssueDao.patientConsumableIssueDetail(item.getId());
					SaleDetail saleDetails = new SaleDetail(new Batch(item.getBatchId().longValue()),
							item.getLeaseQuantity(), item.getNetAmount(), item.getItemId(),
							new com.param.entity.model.master.Status(
									com.param.common.constants.Status.Pending.getStatus()),
							batch.getIssueQuantity(), batch.getRate(), item.getTaxAmount(), new Tax(item.getTaxId()),
							item.getTaxPercentage(), item.getTotalAmount(), new Sale(sale.getId()),
							batch.getUomTypeId() != null ? new UomType(batch.getUomTypeId()) : null,
							batch.getUomUnitId() != null ? new UomUnit(batch.getUomUnitId()) : null,
							item.getSelfPayable(), item.getCreditPayable(), item.getPayeeId(),
							item.getProductCategoryId(), item.getPackageConsumptionAmt());
					saleDetails.setDiscountAmount(0.0);
					saleDetails.setBatch(new Batch(batch.getBatchId()));
					saleDetails.setItemId(item.getItemId());
					saleDetails.setConsessionAmount(item.getConsessionAmount());
					saleDetails.setConsessionPercentage(item.getConsessionPercentage());
					saleDetails.setUnitConsessionAmount(item.getUnitConsessionAmount());
					saleDetails.setItemId(item.getItemId());
					// For package
					List<SaleDetailPackageMapperDto> packageMppr = item.getSaleDetailPackageMapperDtoList();
					if (packageMppr != null && packageMppr.size() > 0) {
						List<SaleDetailPackageMapper> pkgMpprList = new ArrayList<SaleDetailPackageMapper>();
						for (SaleDetailPackageMapperDto dto : packageMppr) {
							SaleDetailPackageMapper mpper = new SaleDetailPackageMapper();
							mpper.setOrganizationId(patientConsumableIssueSave.getOrganizationId());
							mpper.setUnitId(patientConsumableIssueSave.getUnitId());
							mpper.setItemId(item.getItemId());
							mpper.setStatus('A');
							mpper.setIsPackageItem(dto.getIsPackageItem());
							mpper.setPackageId(dto.getPackageId());
							mpper.setPackageConsumptionMasterId(dto.getPackageConsumptionMasterId());
							mpper.setPacakgeConsumptionServiceDetailId(dto.getPacakgeConsumptionServiceDetailId());
							mpper.setPackageConsumptionCapDetailsId(dto.getPackageConsumptionCapDetailsId());
							mpper.setPackageConsumptionEODetailId(dto.getPackageConsumptionEODetailId());
							mpper.setPackageAmount(BigDecimal.valueOf(dto.getPackageAmount()));
							mpper.setPackageQty(dto.getPackageQty());
							mpper.setRate(dto.getRate()!=null ? BigDecimal.valueOf(dto.getRate()):BigDecimal.ZERO);
							mpper.setCapOrQty(dto.getCapOrQty());
							mpper.setSaleDetail(saleDetails);
							pkgMpprList.add(mpper);
						}
						saleDetails.setSaleDetailPackageMapperList(pkgMpprList);
					}

					// For Contract
					List<SaleDetailPayeeMapperDto> payeeMapper = item.getSaleDetailPayeeMapperDtoList();
					if (payeeMapper != null && payeeMapper.size() > 0) {
						List<SaleDetailPayeeMapper> payeeList = new ArrayList<SaleDetailPayeeMapper>();
						for (SaleDetailPayeeMapperDto dto : payeeMapper) {
							SaleDetailPayeeMapper mpper = new SaleDetailPayeeMapper();
							mpper.setOrganizationId(patientConsumableIssueSave.getOrganizationId());
							mpper.setUnitId(patientConsumableIssueSave.getUnitId());
							mpper.setItemId(item.getItemId());
							mpper.setStatus('A');
							mpper.setContractId(dto.getContractId());
							mpper.setPayeeId(dto.getPayeeId());
							mpper.setAssociateCompanyId(dto.getAssociateCompanyId());
							mpper.setPayeeAmount(dto.getPayeeAmount());
							mpper.setQuantity(dto.getQuantity());
							saleDetails.setPayeeId(dto.getPayeeId());
							mpper.setSaleDetail(saleDetails);
							payeeList.add(mpper);
						}
						saleDetails.setSaleDetailPayeeMapperList(payeeList);
					}

					// sessionService.setEntityDefaults(saleDetails);
					saleDetails.setCreateDefaults(
							patientConsumableIssueSave.getOrganizationId() != null
									? patientConsumableIssueSave.getOrganizationId()
									: sessionService.getOrganizationId(),
							patientConsumableIssueSave.getUnitId() != null ? patientConsumableIssueSave.getUnitId()
									: sessionService.getUnitId(),
							patientConsumableIssueSave.getUserId() != null ? patientConsumableIssueSave.getUserId()
									: sessionService.getUserId());

					sale.addSaleDetailsList(saleDetails);
					// issueDetail.setSaleDetail(saleDetails);

				}
			}

			// map issue with sale
			// patientConsumableIssueDao.saveSale(sale);

			iWalkInSalesService.saveGlobalSale(sale);
			issue.setSale(sale);

			for (CommonBillingItemsDto item : patientConsumableIssueSave.getItemList()) {
				IssueDetail issueDetail = patientConsumableIssueDao.patientConsumableIssueDetail(item.getId());
				for (SaleDetail sd : sale.getSaleDetailsList()) {
					if (issueDetail != null) {
						if (issueDetail.getPatientIndentDetail().getItem().getId().equals(sd.getItemId())) {
							issueDetail.setSaleDetail(sd);
							patientConsumableIssueDao.updateIssueDetail(issueDetail);

						}
					}

				}

			}

			// if (saveIssue)
			// pendingIssuesDao.pendingIssuesSave(newIssue);

			issue.setRemark(
					patientConsumableIssueSave.getRemark() != null ? patientConsumableIssueSave.getRemark() : null);
			issue.setCarrier(new User(patientConsumableIssueSave.getCarrierId()));
			issue.setStatus(new Status(patientConsumableIssueSave.getStatusId()));
			issue.setUpdatedDate(TimeZoneComponent.getDateByzone(patientConsumableIssueSave.getUnitId()));
			issue.setUpdatedByUser(
					patientConsumableIssueSave.getUserId() != null ? patientConsumableIssueSave.getUserId()
							: sessionService.getUserId());
			patientConsumableIssueDao.updatePatientIndent(patientIndent);
			// Issue issueResponse = pendingIssuesDao.pendingIssuesUpdate(issue);
			CommonResponse response = new CommonResponse();
			// response.setId(issueResponse.getId().longValue());
			response.setMessage("Patient Issue saved successfully");
			if (isAcceptance.equals(true)) {
			//	response.setObject(issue);
			//	response.setObject1(patientConsumableIssueSave);
				 this.savePatientIssueAcceptance(issue, patientConsumableIssueSave);
			}

			return response;
		} catch (Exception e) {
			
			throw e;
		}
		// return null;
	}

	public Object savePatientIssueAcceptance(Issue issue, PatientConsumableIssueSave patientConsumableIssueSave) {
		PendingIssuesAcceptanceSave PendingIssuesAcceptanceSave = new PendingIssuesAcceptanceSave();
		PendingIssuesAcceptanceSave.setId(issue.getId());
		PendingIssuesAcceptanceSave.setStoreId(issue.getToStore().getStoreId());
		PendingIssuesAcceptanceSave.setStatusId(issue.getStatus().getId().equals(4) ? 8 :issue.getStatus().getId());
		PendingIssuesAcceptanceSave.setOrganizationId(patientConsumableIssueSave.getOrganizationId());
		PendingIssuesAcceptanceSave.setUnitId(patientConsumableIssueSave.getUnitId());
		PendingIssuesAcceptanceSave.setUserId(patientConsumableIssueSave.getUserId());
		List<PendingIssuesAcceptanceSaveItems> items = new ArrayList<>();
		for (IssueDetail id : issue.getIssueDetail()) {

			for (IssueDetailBatchMapper bm : id.getIssueDetailBatchMapper()) {
				if (bm.getAcceptedQuantity() == null) {
					PendingIssuesAcceptanceSaveItems pendingIssuesAcceptanceSaveItems = new PendingIssuesAcceptanceSaveItems();
					pendingIssuesAcceptanceSaveItems.setBatchMapperId(bm.getId());
					pendingIssuesAcceptanceSaveItems.setAcceptQuantity(bm.getIssueQuantity());
					pendingIssuesAcceptanceSaveItems.setRejectQuantity(0);
					items.add(pendingIssuesAcceptanceSaveItems);
				}

			}
			PendingIssuesAcceptanceSave.setItems(items);
		}
		pendingIssuesAcceptanceConsumableService.pendingIssuesAcceptanceConsumableSave(PendingIssuesAcceptanceSave);
		CommonResponse response = new CommonResponse();
		// response.setId(issueResponse.getId().longValue());
		response.setMessage("Patient Issue saved successfully");
		return response;

	}

	// created by minesh for Consignment and club logic
	private IssueDetailBatchMapper clubBatchStoreTransactionWithBatchMapper(Issue issue,
			PatientConsumableIssueSaveItem item, BatchResponse batch, int issueQtyFromRequest, Long batchId,
			Integer unitId, PatientConsumableIssueSave patientConsumableIssueSave , Item item1, IssueDetail issueDetails) {

		Batch batchObj = BatchStoreTransaction(issue.getToStore().getStoreId(), batchId, issueQtyFromRequest,
				new UomType(batch.getUomTypeId()), unitId, patientConsumableIssueSave, issueDetails , item1);
		Integer leaseQuantity = Util.GetLeaseQuantity(issueQtyFromRequest, batchObj.getBatchUomList(),
				new UomType(batch.getUomTypeId()));

		UomType uType = null;
		UomUnit uUnit = null;
		if (batch.getUomTypeId() != null)
			uType = new UomType(batch.getUomTypeId());
		item.setUomTypeId(batch.getUomTypeId());

		if (batch.getUomUnitId() != null)
			uUnit = new UomUnit(batch.getUomUnitId());
		item.setUomUnitId(batch.getUomUnitId());

		Double amount = (leaseQuantity * batchObj.getSellingPrice());
		IssueDetailBatchMapper issueDetailBatchMapper = new IssueDetailBatchMapper(batchObj, uType, uUnit,
				issueQtyFromRequest, amount);
		// sessionService.setEntityDefaults(issueDetailBatchMapper);
		issueDetailBatchMapper.setCreateDefaults(
				patientConsumableIssueSave.getOrganizationId() != null ? patientConsumableIssueSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				patientConsumableIssueSave.getUnitId() != null ? patientConsumableIssueSave.getUnitId()
						: sessionService.getUnitId(),
				patientConsumableIssueSave.getUserId() != null ? patientConsumableIssueSave.getUserId()
						: sessionService.getUserId());

		return issueDetailBatchMapper;
	}
	// created by minesh for Consignment and club logic

	private Batch BatchStoreTransaction(Integer storeId, Long batchId, Integer quantity, UomType uomType,
			Integer unitId, PatientConsumableIssueSave patientConsumableIssueSave , IssueDetail issue , Item item) {

		screen = GlobalDao.getScreen(screenId);

		// get batch needs to be issued
		Batch batch = GlobalDao.getBatch(storeId, batchId, unitId);

		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId);

		// Get lease quantity
		Integer leaseQuantity = 0;
		if (uomType.getId() != null) {
			leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
		} else {
			leaseQuantity = quantity;
		}

		Integer balanceQuantity = avaialableStoreStock - leaseQuantity;

		// Deduct issued quantity from the batch store stock
		batch.getStoreStockList().getFirst().setAvailableQty(balanceQuantity);

		// added by minesh  HIS-21466	
		/*
		 * Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(storeId,
		 * batchId); Integer tQty = inTransitQuantity + leaseQuantity;
		 * batch.getStoreStockList().get(0).settQty(tQty);
		 */
		// added by minesh

		// Deduct issued quantity from the batch
		Integer currentQuantity = batch.getCurrentQuantity() - leaseQuantity;
		batch.setCurrentQuantity(currentQuantity);

		// Add stock transaction
		StockTransaction stockTransaction = new StockTransaction(balanceQuantity, screen, screen.getCode(),
				leaseQuantity, batch.getStoreStockList().getFirst().getStore(), issue.getId());
		stockTransaction.setIsTransit(false);
		stockTransaction.setTransitNo(issue.getIssue().getIssueNo());
		stockTransaction.setItem(item);
		stockTransaction.setOutQty(leaseQuantity);
		stockTransaction.setTransactionId(issue.getIssue().getId());
		stockTransaction.setIssue(issue);
		//stockTransaction.setPatientId(issue.getIssue().getPatientIndent().getPatient().getPatientId());
		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(
				patientConsumableIssueSave.getOrganizationId() != null ? patientConsumableIssueSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				patientConsumableIssueSave.getUnitId() != null ? patientConsumableIssueSave.getUnitId()
						: sessionService.getUnitId(),
				patientConsumableIssueSave.getUserId() != null ? patientConsumableIssueSave.getUserId()
						: sessionService.getUserId());

		batch.addStockTransaction(stockTransaction);

		return batch;
	}

	/*
	 * start code for resolved HIS-10665 On patient issue acceptance indent issued
	 * qty shows wrong
	 */
	public PatientConsumableIssueItem fillPatientConsumableIssueItem(PatientConsumableIssueItem item,
			IssueDetailBatchMapper batchMapper, boolean patientIndentFlag) {
		PatientConsumableIssueItem newItem = null;
		if (patientIndentFlag) {
			// true for getPatientIndentDetail
			newItem = new PatientConsumableIssueItem(item.getId(), item.getItemId(), item.getItemCode(),
					item.getItemName(), item.getGenericName(), item.getUomType(), item.getUomUnit(),
					item.getIndentQuantity(), batchMapper.getIssueQuantity(), item.getAvailableStockQuantity(),
					item.getBatch(), item.getPatientIndentDetailId(), item.getParentIssueDetailId(),
					item.getUomTypeId() != null
					? item.getUomTypeId()
					: null,
					item.getUomUnitId() != null
					? item.getUomUnitId()
					: null,
					item.getIsThreeUOMApplicable(),
					item.getIsLasa() != null ? item.getIsLasa() : false);
			newItem.setGenericId(item.getGenericId());
			newItem.setRate(item.getRate());
		} else {
			// false for getImplantIndentDetail
			newItem = new PatientConsumableIssueItem(item.getId(), item.getImplantIndentDetailId(), item.getItemId(),
					item.getItemCode(), item.getItemName(), item.getGenericName(), item.getUomType(), item.getUomUnit(),
					item.getIndentQuantity(), batchMapper.getIssueQuantity(), item.getAvailableStockQuantity(),
					item.getBatch());
			newItem.setRate(item.getRate());
		}

		newItem.setBatchMapperId(batchMapper.getId());
		newItem.setBatchId(batchMapper.getBatch().getId());
		newItem.setBatchNo(batchMapper.getBatch().getBatchNo());
		newItem.setStatus(item.getStatus());
		newItem.setStatusId(item.getStatusId());
		newItem.setExpiry(item.getExpiry());
		newItem.setProductCategoryId(item.getProductCategoryId());
		newItem.setStoreId(item.getStoreId());
		newItem.setMarkUpDown(item.getMarkUpDown());
		newItem.setMarkUpDownPer(item.getMarkUpDownPer());
		newItem.setPatientClassPercentage(item.getPatientClassPercentage());
		newItem.setMarkupStatus(item.isMarkupStatus());
		newItem.setAcceptedQuantity(batchMapper.getAcceptedQuantity() != null ? batchMapper.getAcceptedQuantity() : 0);
		newItem.setRejectedQuantity(batchMapper.getRejectedQuantity() != null ? batchMapper.getRejectedQuantity() : 0);
		return newItem;
	}
	/*
	 * end code for resolved HIS-10665 On patient issue acceptance indent issued qty
	 * shows wrong
	 */

	@Override
	public PatientConsumableIssueDetails patientConsumableIssueAcceptanceDetails(Integer id, Integer screenFlag,
			Integer storeId, Integer unitId) {

		Issue issue = patientConsumableIssueDao.patientConsumableIssueById(id, unitId);
		String doctorDeatil = "";
		Integer drId = 0;
		if (issue.getVisitType().getVisitTypeId().equals(ER)) {
			// doctor not found for emergency admission

		} else {

			if (issue.getDoctor() != null && issue.getDoctor().getDoctorId() != null) {
				doctorDeatil = issue.getDoctor().getFirstName() + " " + issue.getDoctor().getMiddleName() + " "
						+ issue.getDoctor().getLastName();
				drId = issue.getDoctor().getDoctorId();
			}
		}

		List<PatientSponsorDetails> patientSponsorDetails = null;
		PatientConsumableIssueDetails patientConsumableIssueDetails = new PatientConsumableIssueDetails();
		if (issue.getPatientIndent() != null) {
			String patientName = issue.getPatientIndent().getPatient().getFirstName() + " "
					+ issue.getPatientIndent().getPatient().getMiddleName() + " "
					+ issue.getPatientIndent().getPatient().getLastName();

			if (issue.getAdmission() != null) {
				patientConsumableIssueDetails = new PatientConsumableIssueDetails(issue.getId(),
						issue.getStore().getStoreName(), issue.getStore().getStoreId(),
						issue.getToStore().getStoreName(), issue.getToStore().getStoreId(),
						issue.getVisitType().getVisitTypeId(), issue.getVisitType().getVisitTypeCode(), patientName,
						issue.getPatientIndent().getPatient().getUhidNumber(),
						issue.getAdmission().getAdmissionNumber(), issue.getPatientIndent().getIndentNo(), drId,
						issue.getPatientIndent().getId());
				patientConsumableIssueDetails.setAdmissionId(issue.getAdmission().getAdmissionId());
				patientConsumableIssueDetails.setDoctorDetail(doctorDeatil);
				patientConsumableIssueDetails.setImagePath(issue.getPatientIndent().getPatient().getImagePath());
				patientConsumableIssueDetails
						.setGenderId(issue.getPatientIndent().getPatient().getGender().getGenderId());
				Date doa = null;
				Date pdd = null;
				AdmissionDetail admissionDetail = GlobalDao
						.getPatientWardBedDetails(issue.getAdmission().getAdmissionId());
				if (admissionDetail != null) {
					doa = admissionDetail.getDoa();
					pdd = admissionDetail.getPdd();
				}
				patientConsumableIssueDetails.setDoa(doa != null ? doa : null);
				patientConsumableIssueDetails.setDob(issue.getPatientIndent().getPatient().getDob());
				patientConsumableIssueDetails.setPdd(pdd != null ? pdd : null);
				patientConsumableIssueDetails.setWardName(
						(issue.getPatientIndent().getWard() != null) ? issue.getPatientIndent().getWard().getWardName()
								: null);
				patientConsumableIssueDetails.setBedNo(
						(issue.getPatientIndent().getBed() != null) ? issue.getPatientIndent().getBed().getBedNumber()
								: null);
				patientConsumableIssueDetails.setStatusId(issue.getStatus().getId());
				patientConsumableIssueDetails.setStatus(issue.getStatus().getStatus());
				patientConsumableIssueDetails.setIssueNo(issue.getIssueNo());
				patientConsumableIssueDetails.setIssueDate(issue.getAddedDate());
				patientConsumableIssueDetails.setPatientId(issue.getPatientIndent().getPatient().getPatientId());
				if (issue != null && issue.getAdmission() != null)
					patientSponsorDetails = iPatientSponsorDetailsDao
							.findByAdmissionId(issue.getAdmission().getAdmissionId());
			} else if (issue.getEncounter() != null) {
				patientConsumableIssueDetails = new PatientConsumableIssueDetails(issue.getId(),
						issue.getStore().getStoreName(), issue.getStore().getStoreId(),
						issue.getToStore().getStoreName(), issue.getToStore().getStoreId(),
						issue.getVisitType().getVisitTypeId(), issue.getVisitType().getVisitTypeCode(), patientName,
						issue.getPatientIndent().getPatient().getUhidNumber(),
						issue.getEncounter().getEncounterNumber(), issue.getPatientIndent().getIndentNo(), drId);
				patientConsumableIssueDetails.setDob(issue.getPatientIndent().getPatient().getDob());
				patientConsumableIssueDetails.setEncounterId(issue.getEncounter().getEncounterId());
				patientConsumableIssueDetails.setStatusId(issue.getStatus().getId());
				patientConsumableIssueDetails.setStatus(issue.getStatus().getStatus());
				patientConsumableIssueDetails.setIssueNo(issue.getIssueNo());
				patientConsumableIssueDetails.setIssueDate(issue.getAddedDate());
				patientConsumableIssueDetails.setDoctorDetail(doctorDeatil);
				patientConsumableIssueDetails.setImagePath(issue.getPatientIndent().getPatient().getImagePath());
				patientConsumableIssueDetails
						.setGenderId(issue.getPatientIndent().getPatient().getGender().getGenderId());

				patientConsumableIssueDetails
						.setGenderId(issue.getPatientIndent().getPatient().getGender().getGenderId());
				patientConsumableIssueDetails.setPatientId(issue.getPatientIndent().getPatient().getPatientId());

				if (issue != null && issue.getEncounter() != null)
					patientSponsorDetails = iPatientSponsorDetailsDao
							.findByEncounterId(issue.getEncounter().getEncounterId());
			}
		}
		if (issue.getImplantIndent() != null && issue.getIsImplant().equals(false)) {
			String patientName = issue.getImplantIndent().getPatient().getFirstName() + " "
					+ issue.getImplantIndent().getPatient().getMiddleName() + " "
					+ issue.getImplantIndent().getPatient().getLastName();

			patientConsumableIssueDetails = new PatientConsumableIssueDetails(issue.getId(),
					issue.getImplantIndent().getId(), issue.getStore().getStoreName(), issue.getStore().getStoreId(),
					issue.getToStore().getStoreName(), issue.getToStore().getStoreId(),
					issue.getVisitType().getVisitTypeId(), issue.getVisitType().getVisitTypeCode(), patientName,
					issue.getImplantIndent().getPatient().getUhidNumber(), issue.getAdmission().getAdmissionNumber(),
					issue.getImplantIndent().getImplantIndentNo(), drId);
			patientConsumableIssueDetails.setAdmissionId(issue.getAdmission().getAdmissionId());
			patientConsumableIssueDetails.setStatusId(issue.getStatus().getId());
			patientConsumableIssueDetails.setStatus(issue.getStatus().getStatus());
			patientConsumableIssueDetails.setIssueNo(issue.getIssueNo());
			patientConsumableIssueDetails.setIssueDate(issue.getAddedDate());
			patientConsumableIssueDetails.setDoctorDetail(doctorDeatil);
			patientConsumableIssueDetails.setImagePath(issue.getImplantIndent().getPatient().getImagePath());
			patientConsumableIssueDetails.setGenderId(issue.getImplantIndent().getPatient().getGender().getGenderId());
		}

		List<PatientConsumableIssueItem> itemList = new ArrayList<PatientConsumableIssueItem>();
		List<Integer> ids = patientConsumableIssueDao.getIssueDetailids(id, unitId);
		List<IssueDetail> issueDetailData = patientConsumableIssueDao.getpatientConsumableIssueDetailByIds(ids,
				screenFlag, unitId);
		// for (IssueDetail issueDetail : issueDetailData) {
		for (IssueDetail issueDetail : issue.getIssueDetail()) {
			if (issueDetail.getIsDeleted().equals(false)) {

				List<BatchResponse> medicineBatches = new ArrayList<BatchResponse>();
				List<Batch> batchList = new ArrayList<Batch>();
				// List<Integer>
				// batchMapperIds=patientConsumableIssueDao.getBatchMapperIds(issueDetail.getId());

				// List<IssueDetailBatchMapper> batchMapperList =
				// issueDetail.getIssueDetailBatchMapper();

				// List<IssueDetailBatchMapper> batchMapperList =
				// patientConsumableIssueDao.getBatchMapperList(batchMapperIds);

				if (issueDetail.getPatientIndentDetail() != null) {
					/*batchList = GlobalDao.getBatchDetailsPharmacyConsignment(
							issueDetail.getPatientIndentDetail().getItem().getId(), issue.getToStore().getStoreId(),
							unitId);*/
					batchList = GlobalDao.getBatchDetailsForPatientIssueAcceptanceNew(
							issueDetail.getPatientIndentDetail().getItem().getId(), issue.getToStore().getStoreId(),
							unitId);
					for (Batch batch : batchList) {
						medicineBatches.add(Util.PopulateBatchWithStockWithFlag(batch, false));
					}
					Long stockAvailable = GlobalDao.getStoreStockByItemPharmacy(issue.getToStore().getStoreId(),
							issueDetail.getPatientIndentDetail().getItem().getId(), unitId);
					if (stockAvailable < 0) {
						stockAvailable = 0l;
					}

					Integer issueQuantity = (issueDetail.getPatientIndentDetail().getIndentQuantity()
							- issueDetail.getPatientIndentDetail().getPendingQuantity());
					PatientConsumableIssueItem item = new PatientConsumableIssueItem(issueDetail.getId(),
							issueDetail.getPatientIndentDetail().getItem().getId(),
							issueDetail.getPatientIndentDetail().getItem().getCode(),
							issueDetail.getPatientIndentDetail().getItem().getName(),
							issueDetail.getPatientIndentDetail().getItem().getGeneric() != null
									? issueDetail.getPatientIndentDetail().getItem().getGeneric().getName()
									: null,
							issueDetail.getPatientIndentDetail().getUomType() != null
									? issueDetail.getPatientIndentDetail().getUomType().getType()
									: null,
							issueDetail.getPatientIndentDetail().getUomUnit() != null
									? issueDetail.getPatientIndentDetail().getUomUnit().getUnits()
									: null,
							issueDetail.getPatientIndentDetail().getIndentQuantity(), issueQuantity, stockAvailable,
							medicineBatches, issueDetail.getPatientIndentDetail().getId(), issueDetail.getParentIssueDetailId(),
							issueDetail.getPatientIndentDetail().getUomType() != null
							? issueDetail.getPatientIndentDetail().getUomType().getId()
							: null,
					issueDetail.getPatientIndentDetail().getUomUnit() != null
							? issueDetail.getPatientIndentDetail().getUomUnit().getId()
							: null,
							issueDetail.getPatientIndentDetail().getItem().getIsThreeUOMApplicable(),
							issueDetail.getPatientIndentDetail().getItem().getIsLasa() != null ? issueDetail.getPatientIndentDetail().getItem().getIsLasa() : false
							);
					if (issueDetail.getIssueDetailBatchMapper().size() > 0) {
						item.setBatchMapperId(issueDetail.getIssueDetailBatchMapper().getFirst().getId());
					}
					item.setStatus(issueDetail.getStatus().getStatus());
					item.setStatusId(issueDetail.getStatus().getId());
					item.setGenericId(issueDetail.getPatientIndentDetail().getItem().getGeneric() != null
							? issueDetail.getPatientIndentDetail().getItem().getGeneric().getId()
							: null);
					item.setBatchNo(batchList.size() > 0 ? batchList.getFirst().getBatchNo() : null);
					item.setExpiry(batchList.size() > 0 ? batchList.getFirst().getExpiryDate() : null);
					item.setRate(batchList.size() > 0 ? batchList.getFirst().getRate() : 0.0d);
					Integer productCategoryId = null;
					if (issueDetail.getPatientIndentDetail().getItem().getGeneric() != null) {
						productCategoryId = issueDetail.getPatientIndentDetail().getItem().getGeneric()
								.getProductCategory().getId();
					} else {
						productCategoryId = issueDetail.getPatientIndentDetail().getItem().getProductCategory().getId();
					}
					item.setProductCategoryId(productCategoryId);
					item.setStoreId(issue.getStore().getStoreId());
					if (patientSponsorDetails != null && patientSponsorDetails.size() > 0) {
						for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
							List<Integer> prodCatIds = new ArrayList<Integer>();
							prodCatIds.add(productCategoryId);

							List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
									sponsorDetail.getPaymentEntitlementId());
							if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
								if (pharmacyTariffs.size() > 1) {
									// throw exception?
									// Don't throw - get first object and set data.
								}

								PharmacyTariff pharmacyTariff = pharmacyTariffs.size() > 0 ? pharmacyTariffs.getFirst()
										: null;
								item.setMarkUpDown(pharmacyTariff.getMarkUpDown());
								item.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
								break;
							} else {
								item.setMarkUpDown(1); // default
								item.setMarkUpDownPer(0.00);
							}
						}
					}

					Integer assetCatId = 0;
					Integer prodCatId = 0;
					if (issueDetail.getPatientIndentDetail().getItem().getProductCategory() != null) {
						prodCatId = issueDetail.getPatientIndentDetail().getItem().getProductCategory().getId();
						assetCatId = issueDetail.getPatientIndentDetail().getItem().getProductCategory()
								.getAssetCategory().getId();
					} else {
						prodCatId = issueDetail.getPatientIndentDetail().getItem().getGeneric().getProductCategory()
								.getId();
						assetCatId = issueDetail.getPatientIndentDetail().getItem().getGeneric().getProductCategory()
								.getAssetCategory().getId();
					}
					PatientConsession patientConsession = null;

					if (issue.getEncounter() != null) {
						patientConsession = GlobalDao.getPatientConsessionEncounter(
								issueDetail.getPatientIndentDetail().getItem().getId(),
								issue.getEncounter().getEncounterId(),
								issue.getEncounter().getPatient().getPatientCategoryId(), assetCatId, prodCatId, null,
								null);
					} else if (issue.getAdmission() != null) {
						patientConsession = GlobalDao.getPatientConsessionAdmission(
								issueDetail.getPatientIndentDetail().getItem().getId(),
								issue.getAdmission().getAdmissionId(),
								issue.getAdmission().getPatient().getPatientCategoryId(), assetCatId, prodCatId);

					}

					if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
						item.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
						item.setMarkupStatus(patientConsession.isMarkupStatus());
					} else {
						item.setPatientClassPercentage(0.00);
						item.setMarkupStatus(false);
					}

					// itemList.add(item);
					/*
					 * start code for resolved HIS-10665 On patient issue acceptance indent issued
					 * qty shows wrong
					 */
					if (issueDetail.getIssueDetailBatchMapper().size() > 0) {
						for (IssueDetailBatchMapper batchMapper : issueDetail.getIssueDetailBatchMapper()) {
							itemList.add(fillPatientConsumableIssueItem(item, batchMapper, true)); // true for
																									// getPatientIndentDetail

						}
					} else {
						// itemList.add(item);
					}
					/*
					 * end code for resolved HIS-10665 On patient issue acceptance indent issued qty
					 * shows wrong
					 */

				}

				if (issueDetail.getImplantIndentDetail() != null) {
					batchList = GlobalDao.getBatchDetailsPharmacyConsignment(
							issueDetail.getImplantIndentDetail().getItem().getId(), storeId, unitId);
					for (Batch batch : batchList) {
						medicineBatches.add(Util.PopulateBatchWithStockWithFlag(batch, false));
					}
					Long stockAvailable = GlobalDao.getStoreStockByItemPharmacy(storeId,
							issueDetail.getImplantIndentDetail().getItem().getId(), unitId);
					Integer issueQuantity = (issueDetail.getImplantIndentDetail().getIndentQuantity()
							- issueDetail.getImplantIndentDetail().getPendingQuantity());
					PatientConsumableIssueItem item = new PatientConsumableIssueItem(issueDetail.getId(),
							issueDetail.getImplantIndentDetail().getId(),
							issueDetail.getImplantIndentDetail().getItem().getId(),
							issueDetail.getImplantIndentDetail().getItem().getCode(),
							issueDetail.getImplantIndentDetail().getItem().getName(),
							issueDetail.getImplantIndentDetail().getItem().getGeneric() != null
									? issueDetail.getImplantIndentDetail().getItem().getGeneric().getName()
									: null,
							issueDetail.getImplantIndentDetail().getUomType() != null
									? issueDetail.getImplantIndentDetail().getUomType().getType()
									: null,
							issueDetail.getImplantIndentDetail().getUomUnit() != null
									? issueDetail.getImplantIndentDetail().getUomUnit().getUnits()
									: null,
							issueDetail.getImplantIndentDetail().getIndentQuantity(), issueQuantity, stockAvailable,
							medicineBatches);
					if (issueDetail.getIssueDetailBatchMapper().size() > 0) {
						item.setBatchMapperId(issueDetail.getIssueDetailBatchMapper().getFirst().getId());
					}
					item.setStatus(issueDetail.getStatus().getStatus());
					item.setStatusId(issueDetail.getStatus().getId());
					item.setBatchNo(batchList.size() > 0 ? batchList.getFirst().getBatchNo() : null);
					item.setExpiry(batchList.size() > 0 ? batchList.getFirst().getExpiryDate() : null);
					item.setRate(batchList.size() > 0 ? batchList.getFirst().getRate() : null);
					Integer productCategoryId = null;
					if (issueDetail.getImplantIndentDetail().getItem().getGeneric() != null) {
						productCategoryId = issueDetail.getImplantIndentDetail().getItem().getGeneric()
								.getProductCategory().getId();
					} else {
						productCategoryId = issueDetail.getImplantIndentDetail().getItem().getProductCategory().getId();
					}
					item.setItemId(issueDetail.getImplantIndentDetail().getItem().getId());
					item.setProductCategoryId(productCategoryId);
					item.setStoreId(issue.getStore().getStoreId());
					item.setBatchNo(issueDetail.getBatch().getBatchNo());
					item.setExpiry(issueDetail.getBatch().getExpiryDate());
					item.setRate(issueDetail.getBatch().getRate());
					if (patientSponsorDetails != null && patientSponsorDetails.size() > 0) {
						for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
							List<Integer> prodCatIds = new ArrayList<Integer>();
							prodCatIds.add(productCategoryId);

							List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
									sponsorDetail.getPaymentEntitlementId());
							if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
								if (pharmacyTariffs.size() > 1) {
									// throw exception?
									// Don't throw - get first object and set data.
								}

								PharmacyTariff pharmacyTariff = pharmacyTariffs.size() > 0 ? pharmacyTariffs.getFirst()
										: null;

								item.setMarkUpDown(pharmacyTariff.getMarkUpDown());
								item.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
								break;
							} else {
								item.setMarkUpDown(1); // default
								item.setMarkUpDownPer(0.00);
							}
						}
					}

					Integer assetCatId = 0;
					Integer prodCatId = 0;
					if (issueDetail.getPatientIndentDetail().getItem().getProductCategory() != null) {
						prodCatId = issueDetail.getPatientIndentDetail().getItem().getProductCategory().getId();
						assetCatId = issueDetail.getPatientIndentDetail().getItem().getProductCategory()
								.getAssetCategory().getId();
					} else {
						prodCatId = issueDetail.getPatientIndentDetail().getItem().getGeneric().getProductCategory()
								.getId();
						assetCatId = issueDetail.getPatientIndentDetail().getItem().getGeneric().getProductCategory()
								.getAssetCategory().getId();
					}

					PatientConsession patientConsession = GlobalDao.getPatientConsessionAdmission(
							issueDetail.getPatientIndentDetail().getItem().getId(),
							issue.getAdmission().getAdmissionId(),
							issue.getAdmission().getPatient().getPatientCategoryId(), assetCatId, prodCatId);

					if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
						item.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
						item.setMarkupStatus(patientConsession.isMarkupStatus());
					} else {
						item.setPatientClassPercentage(0.00);
						item.setMarkupStatus(false);
					}
					// itemList.add(item);

					/*
					 * start code for resolved HIS-10665 On patient issue acceptance indent issued
					 * qty shows wrong
					 */
					if (issueDetail.getIssueDetailBatchMapper().size() > 0) {
						for (IssueDetailBatchMapper batchMapper : issueDetail.getIssueDetailBatchMapper()) {
							itemList.add(fillPatientConsumableIssueItem(item, batchMapper, false)); // false for
																									// getImplantIndentDetail
						}
					} else {
						itemList.add(item);
					}
					/*
					 * end code for resolved HIS-10665 On patient issue acceptance indent issued qty
					 * shows wrong
					 */

				}

			}

		}
		
		
		patientConsumableIssueDetails.setItems(itemList);
		List<Carrier> carrierList = new ArrayList<Carrier>();
		String carrierName = GlobalDao.getcarrier(issue.getId());
		
		for (User user : GlobalDao.getPickerUsers(unitId)) {
			
			Carrier carrier = new Carrier(user.getUserId(), user.getUserName());
			carrierList.add(carrier);
			
		}
		patientConsumableIssueDetails.setCarrierName(carrierName!= null ? carrierName : "");
		patientConsumableIssueDetails.setCarrier(carrierList);
		return patientConsumableIssueDetails;

	}

}