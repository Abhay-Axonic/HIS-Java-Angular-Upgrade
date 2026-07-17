package com.param.pharmacy.narcotics.issueWorklist.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.entity.model.master.DiscountCategory;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.pharmacy.BatchAllocation;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.global.common.Response;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.PatientConsession;
import com.param.global.dto.global.Ward;
import com.param.global.dto.items.BatchResponse;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.pharmacy.dao.IWalkInSalesDao;
import com.param.global.pharmacy.dto.PendingIssuesSave;
import com.param.global.pharmacy.dto.SubstituteItemDto;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.global.IGlobalService;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.assignPicker.IAssignPickerDao;
import com.param.pharmacy.ip.dao.pendingIssues.IPendingIssuesDao;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSaveItems;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.service.pendingIssues.acceptance.medication.IPendingIssuesAcceptanceMedicationService;
import com.param.pharmacy.master.unit.dao.applicationConfiguration.IApplicationConfigurationDao;
import com.param.pharmacy.narcotics.issueWorklist.dao.INarcoticsIssueWorklistDao;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsDropdown;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsRequest;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsResponse;
import com.param.pharmacy.op.dao.salesWorkList.ISalesWorkListDao;
import com.param.pharmacy.op.dto.salesWorkList.DiscountCategoryResponse;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
import com.param.service.global.SessionService;
@SuppressWarnings("rawtypes")
@Service("narcoticsIssueWorklistService")
@Transactional
public class NarcoticsIssueWorklistService implements INarcoticsIssueWorklistService {
	@Autowired
	private SessionService sessionService;

	@Autowired
	private INarcoticsIssueWorklistDao narcoticsIssueWorklistDao;

	@Autowired
	private IPendingIssuesDao pendingIssues;

	@Autowired
	IGoodReceiptNoteDao grnDao;
	
	@Autowired
	private IGlobalDao GlobalDao;

	private Integer screenId = Screen.IssueWorklist.getScreen();

	private Integer issueScreenId = Screen.PendingIssues.getScreen();

	@Autowired
	private IAssignPickerDao assignPickerDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IApplicationConfigurationDao iApplicationConfigurationDao;

	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao;

	@Autowired
	IWalkInSalesService iWalkInSalesService;

	@Autowired
	IGlobalDao iglobalDao;
	
	@Autowired
	private ISalesWorkListDao salesWorkListDao;

	@Autowired
	private IPendingIssuesAcceptanceMedicationService pendingIssuesAcceptanceMedicationService;
	@Autowired
	IWalkInSalesDao WalkInSalesDao;

	@Override
	@Transactional
	public NarcoticsDropdown getDropdownList(Integer unitId) {
		NarcoticsDropdown response = new NarcoticsDropdown();
		for (com.param.entity.model.master.VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (WardMasters ward : GlobalDao.getWardList(unitId)) {
			response.addWard(new Ward(ward.getWardId(), ward.getWardName()));
		}

		for (Status status : GlobalDao.getStatusList()) {
			response.addStatus(new com.param.global.dto.global.Status(status.getId(), status.getStatus()));
		}
		return response;
	}

	@Override
	@Transactional
	public List<NarcoticsResponse> narcoticsIssueWorklistSearch(NarcoticsRequest narcoticsRequest) {
		List<NarcoticsResponse> narcoticsResponseList = new ArrayList<NarcoticsResponse>();
		narcoticsRequest.setToDate((new DateTime(narcoticsRequest.getToDate())).plusDays(1).toDate());

		Integer storeId = sessionService.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		ApplicationConfiguration ac = iApplicationConfigurationDao.getApplicationConfiguration(null);

		if (storeNew.getIsNarcotic() || ac.getStoreIp() == storeNew.getStoreId()) {
			for (Issue issue : narcoticsIssueWorklistDao.narcoticsIssueWorklistSearch(narcoticsRequest, screenId)) {
//				List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
//						.doctorSearchByIdValue(issue.getDoctor().getDoctorId());
				String doctorDetail = issue.getDoctor().getFirstName() + " | " + issue.getDoctor().getMiddleName() + " "
						+ issue.getDoctor().getLastName() + " | ";

				if (issue.getPrescription().getSpecialityMaster() != null
						&& issue.getPrescription().getDoctor() == null) {
					doctorDetail = doctorDetail + issue.getPrescription().getSpecialityMaster().getSpecialityName();
				}

				if (issue.getAdmission() != null) {
					String wardName = "";
					String bedNumber = "";
					Date doa = null;
					Date pdd = null;
					AdmissionDetail admissionDetail = GlobalDao
							.getPatientWardBedDetails(issue.getAdmission().getAdmissionId());
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
					Integer age = Util.getAgeByBirthDate(
							issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " "
							+ issue.getAdmission().getPatient().getFirstName() + " "
							+ issue.getAdmission().getPatient().getMiddleName() + " "
							+ issue.getAdmission().getPatient().getLastName() + " | " + age + " | "
							+ issue.getAdmission().getPatient().getGender().getGenderCode();

					NarcoticsResponse narcoticsResponse = new NarcoticsResponse(issue.getId(),
							issue.getVisitType().getVisitTypeCode(), issue.getAdmission().getCreatedDate(),
							issue.getPrescription().getPrescriptionDate(),
							issue.getAdmission().getPatient().getUhidNumber(), patientDetail, doctorDetail,
							issue.getPrescription().getPriority().getPriorityDesc(), issue.getStatus().getStatus(),
							issue.getStatus().getId());
					narcoticsResponse.setWardName(!wardName.equals("") ? wardName : null);
					narcoticsResponse.setBedNo(!bedNumber.equals("") ? bedNumber : null);
					narcoticsResponseList.add(narcoticsResponse);
				} else if (issue.getEncounter() != null) {
					Integer age = Util.getAgeByBirthDate(
							issue.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " "
							+ issue.getEncounter().getPatient().getFirstName() + " "
							+ issue.getEncounter().getPatient().getMiddleName() + " "
							+ issue.getEncounter().getPatient().getLastName() + " | " + age + " | "
							+ issue.getEncounter().getPatient().getGender().getGenderCode();

					NarcoticsResponse narcoticsResponse = new NarcoticsResponse(issue.getId(),
							issue.getVisitType().getVisitTypeCode(), issue.getEncounter().getCreatedDate(),
							issue.getPrescription().getPrescriptionDate(),
							issue.getEncounter().getPatient().getUhidNumber(), patientDetail, doctorDetail,
							issue.getPrescription().getPriority().getPriorityDesc(), issue.getStatus().getStatus(),
							issue.getStatus().getId());
					narcoticsResponseList.add(narcoticsResponse);
				}
			}
		}
		return narcoticsResponseList;
	}

	@Override
	@Transactional
	public List<NarcoticsResponse> narcoticsIssueWorklistSearchPT(NarcoticsRequest narcoticsRequest) {
		List<NarcoticsResponse> narcoticsResponseList = new ArrayList<NarcoticsResponse>();
		narcoticsRequest.setToDate((new DateTime(narcoticsRequest.getToDate())).plusDays(1).toDate());

		Integer storeId = narcoticsRequest.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		ApplicationConfiguration ac = iApplicationConfigurationDao
				.getApplicationConfigurationByUnitId(narcoticsRequest.getUnitId());

		narcoticsResponseList = narcoticsIssueWorklistDao.narcoticsIssueWorklistSearchPT(narcoticsRequest, screenId,
				narcoticsRequest.getUnitId());

		if ((storeNew.getIsNarcotic() || ac.getStoreIp() == storeNew.getStoreId()) && narcoticsResponseList != null
				&& !narcoticsResponseList.isEmpty()) {
			List<Integer> doctorIds = narcoticsResponseList.stream().map(NarcoticsResponse::getDoctorId)
					.collect(Collectors.toList());
//			List<DoctorSpecialityResp> doctorSpecialityRespsAll = GlobalDao.doctorSearchByIdValueBP(doctorIds);
			for (NarcoticsResponse narcoticsResponse : narcoticsResponseList) {
//				List<DoctorSpecialityResp> doctorSpeciality = new ArrayList<DoctorSpecialityResp>();
//				if (!doctorSpecialityRespsAll.isEmpty()) {
//					doctorSpeciality = doctorSpecialityRespsAll.stream()
//							.filter(x -> x.getDoctorId().equals(narcoticsResponse.getDoctorId()))
//							.collect(Collectors.toList());
				if (narcoticsResponse.getVisitType().equals("ER")) {
					Integer age = 0;
					if (narcoticsResponse.getAdmissionDob() != null) {
						age = Util
								.getAgeByBirthDate(narcoticsResponse.getAdmissionDob().toLocalDateTime().toLocalDate());
					}
					narcoticsResponse.setPatientDetail(narcoticsResponse.getAdmissionPatDtls() + age);
				}
				if (narcoticsResponse.getSpecialityName() != null && narcoticsResponse.getDoctorDetail() != null)
					narcoticsResponse.setDoctorDetail(
							narcoticsResponse.getDoctorDetail() + narcoticsResponse.getSpecialityName());
//				}

				if (narcoticsResponse.getAdmissionUhid() != null) {
					Integer age = Util
							.getAgeByBirthDate(narcoticsResponse.getAdmissionDob().toLocalDateTime().toLocalDate());
					narcoticsResponse.setPatientDetail(narcoticsResponse.getAdmissionPatDtls() + age);
					narcoticsResponse.setVisitDate(narcoticsResponse.getAdmissionDate());
					narcoticsResponse.setUhid(narcoticsResponse.getAdmissionUhid());
					narcoticsResponse.setUhid(narcoticsResponse.getAdmissionUhid());
					narcoticsResponse.setGenderCode(narcoticsResponse.getGenderCode());
					narcoticsResponse.setGenderId(narcoticsResponse.getGenderId());
					narcoticsResponse.setImagePath(narcoticsResponse.getImagePath());
					narcoticsResponse.setPatientName(narcoticsResponse.getPatientName());
					narcoticsResponse.setAgeString(narcoticsResponse.getAgeString());
					narcoticsResponse.setMedicationType(narcoticsResponse.getMedicationType());
					String userName =GlobalDao.getUserNameByUserId(narcoticsResponse.getAddedByUser(), narcoticsRequest.getUnitId());
					narcoticsResponse.setUserName(userName);
                    String s = GlobalDao.getEntitlementDesc(narcoticsResponse.getAdmissionId());
                    String contractName = GlobalDao.getEntitlementContractName(narcoticsResponse.getAdmissionId());
                    narcoticsResponse.setEntitlementDesc(s);
                    narcoticsResponse.setContractName(contractName);

				} else if (narcoticsResponse.getEncounterUhid() != null) {
					Integer age = Util
							.getAgeByBirthDate(narcoticsResponse.getEncounterDob().toLocalDateTime().toLocalDate());
					narcoticsResponse.setPatientDetail(narcoticsResponse.getEncounterPatDtls() + age);
					narcoticsResponse.setVisitDate(narcoticsResponse.getEncounterDate());
					narcoticsResponse.setUhid(narcoticsResponse.getEncounterUhid());
					narcoticsResponse.setGenderCode(narcoticsResponse.getGenderCode());
					narcoticsResponse.setGenderId(narcoticsResponse.getGenderId());
					narcoticsResponse.setImagePath(narcoticsResponse.getImagePath());
					narcoticsResponse.setPatientName(narcoticsResponse.getPatientName());
					narcoticsResponse.setAgeString(narcoticsResponse.getAgeString());
					narcoticsResponse.setMedicationType(narcoticsResponse.getMedicationType());
					narcoticsResponse.setMedicationType(narcoticsResponse.getMedicationType());
					String userName =GlobalDao.getUserNameByUserId(narcoticsResponse.getAddedByUser(), narcoticsRequest.getUnitId());
					narcoticsResponse.setUserName(userName);
                    String s = GlobalDao.getEntitlementDesc(narcoticsResponse.getAdmissionId());
                    String contractName = GlobalDao.getEntitlementContractName(narcoticsResponse.getAdmissionId());
                    narcoticsResponse.setEntitlementDesc(s);
                    narcoticsResponse.setContractName(contractName);


				}
			}
			return narcoticsResponseList;
		}

		return null;
	}

	@Override
	@Transactional
	public PharmacologyValidationDetails narcoticsDetails(Integer id, Integer unitId, Integer storeId) {
		PharmacologyValidationDetails narcoticsDetails = new PharmacologyValidationDetails();
		List<PharmacologyItem> itemList = new ArrayList<PharmacologyItem>();
		Issue issue = narcoticsIssueWorklistDao.narcoticsIssueWorklistById(id);
		Integer orgId = issue.getOrganization().getOrganizationId();
		List<PatientSponsorDetails> patientSponsorDetails = null;
		if (issue != null && issue.getPrescription() != null && issue.getAdmission() != null)
			patientSponsorDetails = iPatientSponsorDetailsDao
					.findByAdmissionId(issue.getPrescription().getAdmission().getAdmissionId());

		List<PharmacologyItem> pharmacologyItemListAddedItems = new ArrayList<>();
		if (issue.getAssignPicker() != null) {
			if (!issue.getAssignPicker().getId().equals(null)) {
				pharmacologyItemListAddedItems = assignPickerDao
						.assignPickerAddedItemsDetailsBP(issue.getAssignPicker().getId());
			}
		}
		Response res = pendingIssues.issueDetailsIssueQuentity(id);
		List<IssueDetail> issueDetailsIssueQuentityList = new ArrayList<IssueDetail>();
		if (res.getListObject() != null && res.getListObject().size() > 0) {
			issueDetailsIssueQuentityList.addAll(res.getListObject());
		}

		List<PharmacologyItem> withOutsubstututeList = new ArrayList<>();
		List<IssueDetail> issueDetailList = new ArrayList<>();
		List<PharmacologyItem> savedListAll =  new ArrayList<>();
		issueDetailList = narcoticsIssueWorklistDao.narcoticsIssueWorklistDetails(id);
		savedListAll = salesWorkListDao.getissuedItemListForNewIssueItems(id);
		if(savedListAll != null && savedListAll.size() > 0) {
			for (PharmacologyItem dto : savedListAll) {
				PharmacologyItem dtoList=new PharmacologyItem();
				//dtoList.setParentIssueDetailsId(dto.getPrescriptionDetailId().intValue());
				dtoList.setBatchId((dto.getBatchIdNew()).longValue());
				dtoList.setUomTypeId(dto.getUomTypeId());
				dtoList.setUomUnitId(dto.getUomUnitId());
				dtoList.setItemId(dto.getItemId());
				dtoList.setItemName(dto.getItemName());
				dtoList.setItemCode(dto.getItemCode());
				dtoList.setGenericId(dto.getGenericId());
				dtoList.setGenericName(dto.getGenericName());
				dtoList.setInterventionFlag(false);
				dtoList.setQuantityOrdered(dto.getQuantityOrdered());
				dtoList.setManufacturerId(dto.getManufacturerId());
				dtoList.setManufacturerName(dto.getManufacturerName());
				dtoList.setTaxId(dto.getTaxId());
				dtoList.setTax(dto.getTax());
				dtoList.setTaxPercentage(dto.getTaxPercentage());
				dtoList.setIssuedQuantity(dto.getIssuedQuantity());
				dtoList.setQuantityIssued(dto.getIssuedQty());
				dtoList.setIsNarcotic(false);
				dtoList.setFormulationTypeId(dto.getFormulationTypeId());
				dtoList.setPriorityId(dto.getPriorityId());
				dtoList.setFrequencyDesc(dto.getFrequencyDesc());
				dtoList.setStartDate(dto.getStartDate());
				dtoList.setStopDate(dto.getStopDate());
				dtoList.setInstructionDesc(dto.getInstructionDesc()!=null ?dto.getInstructionDesc() :"");
//				dtoList.setDose(dto.getDose());
//				dtoList.setDoseUnit(dto.getDoseUnit());
				dtoList.setStatus(dto.getStatus());
				dtoList.setStatusId(dto.getStatusId());
				dtoList.setRate(dto.getRate());
				dtoList.setTotalAmount(dto.getTotalAmount());
				dtoList.setNetAmount(dto.getNetAmount());
				dtoList.setSelfPayable(dto.getSelfPayable());
				dtoList.setTaxAmount(dto.getTaxAmount());
				dtoList.setCreditPayable(dto.getCreditPayable());
				dtoList.setDiscountAmount(dto.getDiscountAmount());
				dtoList.setPatientClassPercentage(dto.getPatientClassPercentage());
				dtoList.setPatientClassAmount(dto.getPatientClassAmount());
				dtoList.setPackageConsumptionAmt(dto.getPackageConsumptionAmt());
				// Get the batch list by item id
				List<Batch> batchList = GlobalDao.getBatchDetailsPharmacyConsignment(
						dto.getItemId(), storeId, unitId);
				List<BatchResponse> medicineBatches = new ArrayList<>();
				for (Batch batch : batchList) {
					// Populate batch response
					BatchResponse batchResponse = Util.PopulateBatchWithStockWithFlag(batch, false);

					// Check if batch mapper is present
					 if (dtoList.getBatchId() != null && batch.getId().equals(dtoList.getBatchId())) {
						batchResponse.setIsSelected(Boolean.TRUE);
						batchResponse.setUomTypeId(dtoList.getUomTypeId());
						batchResponse.setUomUnitId(dtoList.getUomUnitId());
						batchResponse.setQuantity(dtoList.getQuantityIssued().intValue());
					}

					medicineBatches.add(batchResponse);
				}
				dtoList.setBatch(medicineBatches);
				itemList.add(dtoList);
			
			}
		}
		
		if (issueDetailList != null && issueDetailList.size() > 0) {

		} else {
			for (PharmacologyItem assignPickerItem : pharmacologyItemListAddedItems) {
				List<IssueDetail> assignPickerItemList = narcoticsIssueWorklistDao
						.addeIssueItems(assignPickerItem.getIssueDetailsId());
				if (assignPickerItemList != null && assignPickerItemList.size() > 0) {
					issueDetailList.addAll(assignPickerItemList);
				}
			}
		}

		for (IssueDetail issueDetail : issueDetailList) {
			if (issueDetail.getPrescriptionDetail() != null) {

				List<BatchResponse> medicineBatches = new ArrayList<BatchResponse>();
				List<IssueDetailBatchMapper> batchMapperList = issueDetail.getIssueDetailBatchMapper();

				List<Batch> batchList = GlobalDao.getBatchDetailsPharmacyConsignment(
						issueDetail.getPrescriptionDetail().getItem().getId(), storeId, unitId);
				for (Batch batch : batchList) {
					BatchResponse batchResponse = Util.PopulateBatchWithStockWithFlag(batch, false);
					Optional<IssueDetailBatchMapper> batchMapper = batchMapperList.stream()
							.filter(mapperBatch -> mapperBatch.getId().equals(batch.getId())).findFirst();

					if (batchMapper.isPresent()) {
						batchResponse.setIsSelected(Boolean.TRUE);
						batchResponse.setUomTypeId(batchMapper.get().getUomType().getId());
						batchResponse.setUomUnitId(batchMapper.get().getUomUnit().getId());
						batchResponse.setIssueQuantity(batchMapper.get().getIssueQuantity());
					} else if (issueDetail.getBatch() != null && issueDetail.getAssignPickerDetail() != null
							&& batch.getId().equals(issueDetail.getBatch().getId())) {
						batchResponse.setIsSelected(Boolean.TRUE);
						batchResponse.setUomTypeId(issueDetail.getAssignPickerDetail().getUomType() != null
								? issueDetail.getAssignPickerDetail().getUomType().getId()
								: issueDetail.getUomType().getId());
						batchResponse.setUomUnitId(issueDetail.getAssignPickerDetail().getUomUnit() != null
								? issueDetail.getAssignPickerDetail().getUomUnit().getId()
								: issueDetail.getUomUnit().getId());
						batchResponse.setIssueQuantity(issueDetail.getIssueQuantity());
					} else if (issueDetail.getBatch() != null && issueDetail.getAssignPickerDetail() == null
							&& batch.getId().equals(issueDetail.getBatch().getId())) {
						batchResponse.setIsSelected(Boolean.TRUE);
						batchResponse.setUomTypeId(issueDetail.getAssignPickerDetail() != null
								? issueDetail.getAssignPickerDetail().getUomType().getId()
								: issueDetail.getUomType().getId());
						batchResponse.setUomUnitId(issueDetail.getAssignPickerDetail() != null
								? issueDetail.getAssignPickerDetail().getUomUnit().getId()
								: issueDetail.getUomUnit().getId());
						batchResponse.setIssueQuantity(issueDetail.getIssueQuantity());
					}
					medicineBatches.add(batchResponse);
				}
				Long issueQuantity = narcoticsIssueWorklistDao
						.getIssuedQuantity(issueDetail.getPrescriptionDetail().getId());

				PharmacologyItem item = new PharmacologyItem((long) issueDetail.getId(),
						issueDetail.getPrescriptionDetail().getItem().getId(),
						issueDetail.getPrescriptionDetail().getItem().getCode(),
						issueDetail.getPrescriptionDetail().getItem().getName(),
						issueDetail.getPrescriptionDetail().getItem().getGeneric().getId(),
						issueDetail.getPrescriptionDetail().getItem().getGeneric().getName(),
						issueDetail.getPrescriptionDetail().getIsIntervention(),
						issueDetail.getPrescriptionDetail().getOrderQty(),
						issueDetail.getPrescriptionDetail().getItem().getManufacturer().getId(),
						issueDetail.getPrescriptionDetail().getItem().getManufacturer().getName(), medicineBatches,
						issueDetail.getPrescriptionDetail().getItem().getSalesTax().getId(),
						issueDetail.getPrescriptionDetail().getItem().getSalesTax().getName(),
						issueDetail.getPrescriptionDetail().getItem().getSalesTax().getTaxPercentage(),
						issueQuantity.intValue(), issueDetail.getStatus().getStatus(), issueDetail.getStatus().getId(),
						issueDetail.getPrescriptionDetail().getItem().getIsNarcotic(),
						issueDetail.getPrescriptionDetail().getItem().getIsThreeUOMApplicable());
				item.setProductCategoryId(
						issueDetail.getPrescriptionDetail().getItem().getGeneric().getProductCategory() != null
								? issueDetail.getPrescriptionDetail().getItem().getGeneric().getProductCategory()
										.getId()
								: null);
				item.setFormulationTypeId(issueDetail.getPrescriptionDetail().getItem().getFormulationType().getId());
				item.setPriorityId(issueDetail.getPrescriptionDetail().getPriorityId());
				item.setStatus(issueDetail.getStatus().getStatus());
				item.setStatusId(issueDetail.getStatus().getId());
				item.setPrescriptionDetailId(issueDetail.getPrescriptionDetail().getId());
				item.setIsFromAssignPicker(issueDetail.getIsFromAssignPicker());
				item.setPickedQuantity(0);
				item.setQuantityIssued(issueDetail.getIssueQuantity() != null ? issueDetail.getIssueQuantity() : 0);
				item.setRate(issueDetail.getRate() != null ? issueDetail.getRate() : null);
				item.setTotalAmount(issueDetail.getTotalAmount() != null ? issueDetail.getTotalAmount() : null);
				item.setNetAmount(issueDetail.getNetAmount() != null ? issueDetail.getNetAmount() : null);
				item.setSelfPayable(issueDetail.getSelfPayable() != null ? issueDetail.getSelfPayable() : null);
				item.setTaxAmount(issueDetail.getTaxAmount() != null ? issueDetail.getTaxAmount() : null);
				item.setCreditPayable(issueDetail.getCreditPayable() != null ? issueDetail.getCreditPayable() : null);
				item.setDiscountAmount(
						issueDetail.getDiscountAmount() != null ? issueDetail.getDiscountAmount() : null);
				item.setPatientClassPercentage(
						issueDetail.getConsessionPercentage() != null ? issueDetail.getConsessionPercentage() : null);
				item.setPatientClassAmount(
						issueDetail.getConsessionAmount() != null ? issueDetail.getConsessionAmount() : null);
				item.setStoreId(issueDetail.getPrescriptionDetail().getStore().getStoreId());
				item.setDose(issueDetail.getPrescriptionDetail().getItem().getStrength() != null
						? issueDetail.getPrescriptionDetail().getItem().getStrength()
						: null);
				item.setDoseUnit(issueDetail.getPrescriptionDetail().getItem().getStrengthUnit() != null
						? issueDetail.getPrescriptionDetail().getItem().getStrengthUnit().getUnits()
						: null);
				// item.setFrequencyDesc(issueDetail.get);
				if(issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails() != null && issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().size()>0) {
					item.setFrequencyDesc(issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().getFirst()
							.getFrequency().getDescription());
					item.setStartDate(
							issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().getFirst().getStartDate());
					
					item.setStopDate(
							issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().getFirst().getStopDate());
				}else {
					item.setFrequencyDesc("");
					item.setStartDate(null);
					item.setStopDate(null);
				}
			
				String instructionDesc = "";
				if (issueDetail.getPrescriptionDetail().getInstruction() != null) {

					instructionDesc = issueDetail.getPrescriptionDetail().getInstruction().getInstructionDesc();

				} else {

					if (issueDetail.getPrescriptionDetail().getInstructionDesc() != null) {
						instructionDesc = issueDetail.getPrescriptionDetail().getInstructionDesc();
					}
				}
				item.setInstructionDesc(instructionDesc);

				if (pharmacologyItemListAddedItems != null && pharmacologyItemListAddedItems.size() > 0) {

					for (IssueDetail issuedto : issueDetailsIssueQuentityList) {

						if (issueDetail.getId().equals(issuedto.getId())) {

							for (PharmacologyItem pharmacologyItemDto : pharmacologyItemListAddedItems) {
								if (Long.valueOf(issuedto.getBatchId()).equals(pharmacologyItemDto.getBatchId())) {
									item.setPickedQuantity(pharmacologyItemDto.getQuantity());
								}
							}
						}
					}
				}
				if (patientSponsorDetails != null && issueDetail.getPrescriptionDetail() != null
						&& issueDetail.getPrescriptionDetail().getItem() != null
						&& issueDetail.getPrescriptionDetail().getItem().getGeneric() != null
						&& issueDetail.getPrescriptionDetail().getItem().getGeneric().getProductCategory() != null) {

					for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(issueDetail.getPrescriptionDetail().getItem().getGeneric().getProductCategory()
								.getId());

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
					if (issueDetail.getPrescriptionDetail().getItem().getProductCategory() != null) {
						prodCatId = issueDetail.getPrescriptionDetail().getItem().getProductCategory().getId();
						assetCatId = issueDetail.getPrescriptionDetail().getItem().getProductCategory()
								.getAssetCategory().getId();
					} else {
						prodCatId = issueDetail.getPrescriptionDetail().getItem().getGeneric().getProductCategory()
								.getId();
						assetCatId = issueDetail.getPrescriptionDetail().getItem().getGeneric().getProductCategory()
								.getAssetCategory().getId();
					}

					PatientConsession patientConsession = iglobalDao.getPatientConsessionAdmission(
							issueDetail.getPrescriptionDetail().getItem().getId(),
							issue.getAdmission().getAdmissionId(),
							issue.getAdmission().getPatient().getPatientCategoryId(), assetCatId, prodCatId);

					if (patientConsession != null && patientConsession.isMarkupStatus() == false) {
						item.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
						if (patientConsession.getPatientClassPercentage() == 0.00) {
							item.setPatientClassAmount(0.00);
						}
						item.setMarkupStatus(patientConsession.isMarkupStatus());
					} else {
						item.setPatientClassPercentage(0.00);
						item.setPatientClassAmount(0.00);
						item.setMarkupStatus(false);
					}

				}
				List<PharmacologyItem> substututeList = new ArrayList<>();
				// For Selected Substitute List
				// with assign picker
				if (issueDetail.getIsFromAssignPicker() == true && issueDetail.getIsSubstitute() == false) {
					List<SubstituteItemDto> substitute = GlobalDao
							.getListOfNarcoticIssuesSubstitutes(issueDetail.getIssue().getId());
					for (SubstituteItemDto substituteList : substitute) {

						if (item.getPrescriptionDetailId() == substituteList.getParentSubstituteId().longValue()) {

							PharmacologyItem itm = new PharmacologyItem();
							itm.setId(substituteList.getIssueDtlId().longValue());
							itm.setPrescriptionDetailId(substituteList.getPrescriptionDetailid().longValue());
							itm.setFrequency(substituteList.getFrequency());
							itm.setDose(substituteList.getDose());
							itm.setGenericId(substituteList.getGenericId());
							itm.setGenericName(substituteList.getGenericName());
							itm.setProductCategoryId(substituteList.getProductCategoryId());
							itm.setItemId(substituteList.getItemId());
							itm.setItemName(substituteList.getItemName());
							itm.setItemCode(substituteList.getItemCode());
							itm.setManufacturerName(substituteList.getManufacturer());
							itm.setIsFromAssignPicker(true);
							itm.setManufacturerId(substituteList.getManufactureId());
							itm.setQuantityIssued(
									substituteList.getIssuedQuantity() != null ? substituteList.getIssuedQuantity()
											: 0);
							itm.setInterventionFlag(substituteList.getInterventionFlag());
							itm.setStatus(substituteList.getStatus());
							itm.setPickedQuantity(0);
							itm.setInstructionDesc(instructionDesc);
							if (pharmacologyItemListAddedItems != null && pharmacologyItemListAddedItems.size() > 0) {

								for (PharmacologyItem pharmacologyItemDto : pharmacologyItemListAddedItems) {

									if (Long.valueOf(substituteList.getBatchid())
											.equals(pharmacologyItemDto.getBatchId())) {

										itm.setPickedQuantity(substituteList.getQuantityIssued() != null
												? substituteList.getQuantityIssued()
												: 0);
									}

								}
							}
							itm.setStatusId(substituteList.getStatusId());
							itm.setTax(substituteList.getTax());
							itm.setTaxId(substituteList.getTaxId());
							itm.setProductCategoryId(substituteList.getProductCategoryId() != null
									? substituteList.getProductCategoryId()
									: null);
							itm.setRate(substituteList.getRate() != null ? substituteList.getRate() : null);
							itm.setTotalAmount(
									substituteList.getTotalAmount() != null ? substituteList.getTotalAmount() : null);
							itm.setNetAmount(
									substituteList.getNetAmount() != null ? substituteList.getNetAmount() : null);
							itm.setSelfPayable(
									substituteList.getSelfPayable() != null ? substituteList.getSelfPayable() : null);
							itm.setTaxAmount(
									substituteList.getTaxAmount() != null ? substituteList.getTaxAmount() : null);
							itm.setCreditPayable(
									substituteList.getCreditPayable() != null ? substituteList.getCreditPayable()
											: null);
							itm.setDiscountAmount(
									substituteList.getDiscountAmount() != null ? substituteList.getDiscountAmount()
											: null);
							itm.setItemDiscount(
									substituteList.getDiscountAmount() != null ? substituteList.getDiscountAmount()
											: null);
							itm.setTaxPercentage(
									substituteList.getTaxPercentage() != null ? substituteList.getTaxPercentage()
											: null);
							itm.setPatientClassAmount(substituteList.getConsessionAmount());
							itm.setPatientClassPercentage(substituteList.getConsessionPercentage());
							itm.setParentSubstituteId(substituteList.getParentSubstituteId());
							itm.setParentIssueDetailsId(substituteList.getParentIssueDetailsId());
							List<BatchResponse> mediBatchesSub = new ArrayList<>();
							List<IssueDetailBatchMapper> batchMapperListSub = issueDetail.getIssueDetailBatchMapper();

							// Get the batch list by item id
							List<Batch> batchListSub = GlobalDao
									.getBatchDetailsPharmacyConsignment(substituteList.getItemId(), storeId, unitId);

							for (Batch batchSub : batchListSub) {
								// Populate batch response
								BatchResponse batchResponseSub = Util.PopulateBatchWithStockWithFlag(batchSub, false);

								// Check if batch mapper is present
								@SuppressWarnings("unlikely-arg-type")
								Optional<IssueDetailBatchMapper> batchMapperSub = batchMapperListSub.stream()
										.filter(mapperBatch -> mapperBatch.getId().equals(batchSub.getId()))
										.findFirst();

								if (batchMapperSub.isPresent()) {
									batchResponseSub.setIsSelected(Boolean.TRUE);
									batchResponseSub.setUomTypeId(batchMapperSub.get().getUomType().getId());
									batchResponseSub.setUomUnitId(batchMapperSub.get().getUomUnit().getId());
									batchResponseSub.setIssueQuantity(batchMapperSub.get().getIssueQuantity());
									// batchResponse.setPickedQuantity(issueDetail.getIssueQuantity());
								} else if (substituteList.getBatchid() != null
										&& batchSub.getId() == substituteList.getBatchid().longValue()) {
									batchResponseSub.setIsSelected(Boolean.TRUE);
									batchResponseSub.setUomTypeId(substituteList.getUomTypeId());
									batchResponseSub.setUomUnitId(substituteList.getUomUnitId());
									batchResponseSub.setIssueQuantity(substituteList.getIssuedQuantity());
									// batchResponse.setPickedQuantity(issueDetail.getIssueQuantity());
								}

								mediBatchesSub.add(batchResponseSub);
							}
							itm.setBatch(mediBatchesSub);

							if (withOutsubstututeList.size() > 0) {
								int count = 0;
								for (PharmacologyItem dtoSelected : withOutsubstututeList) {

									if (dtoSelected.getId().equals(itm.getId())) {
										count = 1;
									}
								}
								if (count == 0) {
									withOutsubstututeList.add(itm);
								}
							} else {
								withOutsubstututeList.add(itm);
							}
							PatientConsession patientConsessionSubstitute = iglobalDao.getPatientConsessionAdmission(
									substituteList.getItemId(), issue.getAdmission().getAdmissionId(),
									issue.getAdmission().getPatient().getPatientCategoryId(),
									substituteList.getAssetCategoryId(), substituteList.getProductCategoryId());

							if (patientConsessionSubstitute != null
									&& patientConsessionSubstitute.isMarkupStatus() == false) {
								itm.setPatientClassPercentage(patientConsessionSubstitute.getPatientClassPercentage());
								if (patientConsessionSubstitute.getPatientClassPercentage() == 0.00) {
									itm.setPatientClassAmount(0.00);
								}
								itm.setMarkupStatus(patientConsessionSubstitute.isMarkupStatus());
							} else {
								itm.setPatientClassPercentage(0.00);
								itm.setPatientClassAmount(0.00);
								itm.setMarkupStatus(false);
							}

						}
					}

					itemList.add(item);

				} else if (issueDetail.getIsFromAssignPicker() == true) {
					itemList.add(item);
				}
				// without assign picker
				if (issueDetail.getIsFromAssignPicker() == false) {

					List<SubstituteItemDto> substitute = GlobalDao
							.getListOfNarcoticIssuesSubstitutes(issueDetail.getIssue().getId());
					for (SubstituteItemDto substituteList : substitute) {

						if (item.getPrescriptionDetailId() == substituteList.getParentSubstituteId().longValue()
								&& item.getPrescriptionDetailId() != null
								&& substituteList.getParentSubstituteId() != null) {

							PharmacologyItem itm = new PharmacologyItem();
							itm.setId(substituteList.getIssueDtlId().longValue());
							itm.setPrescriptionDetailId(substituteList.getPrescriptionDetailid().longValue());
							itm.setFrequency(substituteList.getFrequency());
							itm.setDose(substituteList.getDose());
							itm.setGenericId(substituteList.getGenericId());
							itm.setGenericName(substituteList.getGenericName());
							itm.setProductCategoryId(substituteList.getProductCategoryId());
							itm.setItemId(substituteList.getItemId());
							itm.setItemName(substituteList.getItemName());
							itm.setItemCode(substituteList.getItemCode());
							itm.setManufacturerName(substituteList.getManufacturer());
							itm.setManufacturerId(substituteList.getManufactureId());
							itm.setQuantityIssued(
									substituteList.getIssuedQuantity() != null ? substituteList.getIssuedQuantity()
											: 0);
							itm.setInterventionFlag(substituteList.getInterventionFlag());
							itm.setStatus(substituteList.getStatus());
							itm.setStatusId(substituteList.getStatusId());
							itm.setTax(substituteList.getTax());
							itm.setTaxId(substituteList.getTaxId());
							itm.setProductCategoryId(substituteList.getProductCategoryId() != null
									? substituteList.getProductCategoryId()
									: null);
							itm.setRate(substituteList.getRate() != null ? substituteList.getRate() : null);
							itm.setTotalAmount(
									substituteList.getTotalAmount() != null ? substituteList.getTotalAmount() : null);
							itm.setNetAmount(
									substituteList.getNetAmount() != null ? substituteList.getNetAmount() : null);
							itm.setSelfPayable(
									substituteList.getSelfPayable() != null ? substituteList.getSelfPayable() : null);
							itm.setTaxAmount(
									substituteList.getTaxAmount() != null ? substituteList.getTaxAmount() : null);
							itm.setCreditPayable(
									substituteList.getCreditPayable() != null ? substituteList.getCreditPayable()
											: null);
							itm.setDiscountAmount(
									substituteList.getDiscountAmount() != null ? substituteList.getDiscountAmount()
											: null);
							itm.setItemDiscount(
									substituteList.getDiscountAmount() != null ? substituteList.getDiscountAmount()
											: null);
							itm.setTaxPercentage(
									substituteList.getTaxPercentage() != null ? substituteList.getTaxPercentage()
											: null);
							itm.setPatientClassAmount(substituteList.getConsessionAmount());
							itm.setPatientClassPercentage(substituteList.getConsessionPercentage());
							itm.setIsFromAssignPicker(false);
							itm.setPickedQuantity(0);
							itm.setParentIssueDetailsId(substituteList.getParentIssueDetailsId());
							itm.setParentSubstituteId(substituteList.getParentSubstituteId());
							List<BatchResponse> mediBatchesSub = new ArrayList<>();
							List<IssueDetailBatchMapper> batchMapperListSub = issueDetail.getIssueDetailBatchMapper();
							itm.setInstructionDesc(instructionDesc);
							// Get the batch list by item id
							List<Batch> batchListSub = GlobalDao
									.getBatchDetailsPharmacyConsignment(substituteList.getItemId(), storeId, unitId);

							for (Batch batchSub : batchListSub) {
								// Populate batch response
								BatchResponse batchResponseSub = Util.PopulateBatchWithStockWithFlag(batchSub, false);

								// Check if batch mapper is present
								@SuppressWarnings("unlikely-arg-type")
								Optional<IssueDetailBatchMapper> batchMapperSub = batchMapperListSub.stream()
										.filter(mapperBatch -> mapperBatch.getId().equals(batchSub.getId()))
										.findFirst();

								if (batchMapperSub.isPresent()) {
									batchResponseSub.setIsSelected(Boolean.TRUE);
									batchResponseSub.setUomTypeId(batchMapperSub.get().getUomType().getId());
									batchResponseSub.setUomUnitId(batchMapperSub.get().getUomUnit().getId());
									batchResponseSub.setIssueQuantity(batchMapperSub.get().getIssueQuantity());
									// batchResponse.setPickedQuantity(issueDetail.getIssueQuantity());
								} else if (substituteList.getBatchid() != null
										&& batchSub.getId() == substituteList.getBatchid().longValue()) {
									batchResponseSub.setIsSelected(Boolean.TRUE);
									batchResponseSub.setUomTypeId(substituteList.getUomTypeId());
									batchResponseSub.setUomUnitId(substituteList.getUomUnitId());
									batchResponseSub.setIssueQuantity(substituteList.getIssuedQuantity());
									// batchResponse.setPickedQuantity(issueDetail.getIssueQuantity());
								}

								mediBatchesSub.add(batchResponseSub);
							}
							itm.setBatch(mediBatchesSub);
							if (withOutsubstututeList.size() > 0) {
								int count = 0;
								for (PharmacologyItem dtoSelected : withOutsubstututeList) {

									if (dtoSelected.getId().equals(itm.getId())) {
										count = 1;
									}
								}
								if (count == 0) {
									withOutsubstututeList.add(itm);
								}
							} else {
								withOutsubstututeList.add(itm);
							}
							PatientConsession patientConsessionSubstitute = iglobalDao.getPatientConsessionAdmission(
									substituteList.getItemId(), issue.getAdmission().getAdmissionId(),
									issue.getAdmission().getPatient().getPatientCategoryId(),
									substituteList.getAssetCategoryId(), substituteList.getProductCategoryId());

							if (patientConsessionSubstitute != null
									&& patientConsessionSubstitute.isMarkupStatus() == false) {
								itm.setPatientClassPercentage(patientConsessionSubstitute.getPatientClassPercentage());
								if (patientConsessionSubstitute.getPatientClassPercentage() == 0.00) {
									itm.setPatientClassAmount(0.00);
								}
								itm.setMarkupStatus(patientConsessionSubstitute.isMarkupStatus());
							} else {
								itm.setPatientClassPercentage(0.00);
								itm.setPatientClassAmount(0.00);
								itm.setMarkupStatus(false);
							}

						}
					}
					itemList.add(item);
				}
			}
		}

		String drSpeciality = "";
		String doctorDetail = "";
		if (issue.getPrescription().getSpecialityMaster() != null && issue.getPrescription().getDoctor() != null) {
			doctorDetail = issue.getDoctor().getFirstName() + " " + issue.getDoctor().getMiddleName() + " "
					+ issue.getDoctor().getLastName() + " | "
					+ issue.getPrescription().getSpecialityMaster().getSpecialityName();
		}

		String patientDetail = "";
		Integer genderId = 0;
		String imagePath = "";
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
			patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " "
					+ issue.getAdmission().getPatient().getFirstName() + " "
					+ issue.getAdmission().getPatient().getMiddleName() + " "
					+ issue.getAdmission().getPatient().getLastName() + " " + " | " + age + " | "
					+ issue.getAdmission().getPatient().getGender().getGenderCode();

			genderId = issue.getAdmission().getPatient().getGender().getGenderId();
			imagePath = issue.getAdmission().getPatient().getImagePath();

			if (issue.getAssignPicker() != null) {

				if (withOutsubstututeList.size() > 0) {
					List<Long> pricIds = new ArrayList<>();
					int count = 0;
					for (PharmacologyItem mainItem : itemList) {

						List<PharmacologyItem> tempSubstiList = new ArrayList<>();
						boolean occrsFlag = false;
						for (Long long1 : pricIds) {

							if (mainItem.getPrescriptionDetailId().equals(long1)) {
								occrsFlag = true;
							}
						}
						if (occrsFlag == false) {
							for (PharmacologyItem dtoSelected : withOutsubstututeList) {
								if(mainItem.getPrescriptionDetailId() != null){
								if (mainItem.getPrescriptionDetailId()
										.equals(dtoSelected.getParentSubstituteId().longValue())) {
									tempSubstiList.add(dtoSelected);
									pricIds.add(mainItem.getPrescriptionDetailId());
								}
								}
							}
						}

						if (tempSubstiList.size() > 0) {
							itemList.get(count).setSelectedItems(tempSubstiList);
						}

						count += 1;
					}
				}

			} else {
				if (withOutsubstututeList.size() > 0) {
					int count = 0;
					for (PharmacologyItem mainItem : itemList) {

						List<PharmacologyItem> tempSubstiList = new ArrayList<>();

						for (PharmacologyItem dtoSelected : withOutsubstututeList) {
							if(mainItem.getPrescriptionDetailId() != null){
							if (mainItem.getId().equals(dtoSelected.getParentIssueDetailsId().longValue())) {
								tempSubstiList.add(dtoSelected);
							}
							}
						}
						itemList.get(count).setSelectedItems(tempSubstiList);

						count += 1;
					}
				}
			}

			narcoticsDetails = new PharmacologyValidationDetails((long) issue.getId(),
					issue.getAdmission().getPatient().getPatientId(), patientDetail,
					issue.getAdmission().getPatient().getDob(),
					doctorDetail != "" ? issue.getPrescription().getDoctor().getDoctorId() : null, doctorDetail,
					issue.getAdmission().getPatient().getUhidNumber(),
					issue.getAdmission().getVisitType().getVisitTypeId(), PopulateDiscountCategory(orgId), itemList);
			narcoticsDetails.setPrescriptionId(issue.getPrescription().getId());
			narcoticsDetails.setStatus(issue.getStatus().getStatus());
			narcoticsDetails.setStatusId(issue.getStatus().getId());
			narcoticsDetails.setAdmissionId(issue.getAdmission().getAdmissionId());
			narcoticsDetails.setWardName(!wardName.equals("") ? wardName : null);
			narcoticsDetails.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			narcoticsDetails.setDoa(doa != null ? doa : null);
			narcoticsDetails.setPdd(pdd != null ? pdd : null);
			narcoticsDetails.setImagePath(imagePath);
			narcoticsDetails.setGenderId(genderId);
		}

		else if (issue.getEncounter() != null) {
			Integer age = Util
					.getAgeByBirthDate(issue.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
			patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " "
					+ issue.getEncounter().getPatient().getFirstName() + " "
					+ issue.getEncounter().getPatient().getMiddleName() + " "
					+ issue.getEncounter().getPatient().getLastName() + " " + " | " + age + " | "
					+ issue.getEncounter().getPatient().getGender().getGenderCode();

			if (issue.getAssignPicker() != null) {

				if (withOutsubstututeList.size() > 0) {
					List<Long> pricIds = new ArrayList<>();
					int count = 0;
					for (PharmacologyItem mainItem : itemList) {

						List<PharmacologyItem> tempSubstiList = new ArrayList<>();
						boolean occrsFlag = false;
						for (Long long1 : pricIds) {

							if (mainItem.getPrescriptionDetailId().equals(long1)) {
								occrsFlag = true;
							}
						}
						if (occrsFlag == false) {
							for (PharmacologyItem dtoSelected : withOutsubstututeList) {

								if (mainItem.getPrescriptionDetailId()
										.equals(dtoSelected.getParentSubstituteId().longValue())) {
									tempSubstiList.add(dtoSelected);
									pricIds.add(mainItem.getPrescriptionDetailId());
								}
							}
						}

						if (tempSubstiList.size() > 0) {
							itemList.get(count).setSelectedItems(tempSubstiList);
						}

						count += 1;
					}

					List<PharmacologyItem> tempSubstiList1 = new ArrayList<>();
					for (PharmacologyItem dtoSelected : withOutsubstututeList) {
						int fount = 0;
						for (PharmacologyItem mainItem : itemList) {

							if (mainItem.getPrescriptionDetailId()
									.equals(dtoSelected.getParentSubstituteId().longValue())) {
								fount = 1;
							}
						}
						if (fount == 0) {
							tempSubstiList1.add(dtoSelected);
						}

					}

					itemList.addAll(tempSubstiList1);
				}

			} else {
				if (withOutsubstututeList.size() > 0) {
					int count = 0;
					for (PharmacologyItem mainItem : itemList) {

						List<PharmacologyItem> tempSubstiList = new ArrayList<>();

						for (PharmacologyItem dtoSelected : withOutsubstututeList) {

							if (mainItem.getId().equals(dtoSelected.getParentIssueDetailsId().longValue())) {
								tempSubstiList.add(dtoSelected);
							}
						}
						itemList.get(count).setSelectedItems(tempSubstiList);

						count += 1;
					}
				}

			}

			narcoticsDetails = new PharmacologyValidationDetails((long) issue.getId(),
					issue.getEncounter().getPatient().getPatientId(), patientDetail,
					issue.getEncounter().getPatient().getDob(), issue.getDoctor().getDoctorId(), doctorDetail,
					issue.getEncounter().getPatient().getUhidNumber(),
					issue.getEncounter().getVisitType().getVisitTypeId(), PopulateDiscountCategory(orgId), itemList);
			narcoticsDetails.setStatus(issue.getStatus().getStatus());
			narcoticsDetails.setStatusId(issue.getStatus().getId());
			narcoticsDetails.setPrescriptionId(issue.getPrescription().getId());
			narcoticsDetails.setEncounterId(issue.getEncounter().getEncounterId());
		}
        narcoticsDetails.setRemark(issue.getRemark());
		return narcoticsDetails;

	}

	private Object savePatientIssueAcceptance(PendingIssuesSave pendingIssuesSave) {
		Issue issue = pendingIssues.getIssue(pendingIssuesSave.getId());

		PendingIssuesAcceptanceSave PendingIssuesAcceptanceSave = new PendingIssuesAcceptanceSave();
		PendingIssuesAcceptanceSave.setId(issue.getId());
		PendingIssuesAcceptanceSave.setStoreId(issue.getStore().getStoreId());
		PendingIssuesAcceptanceSave.setStatusId(8);
		PendingIssuesAcceptanceSave.setOrganizationId(pendingIssuesSave.getOrganizationId());
		PendingIssuesAcceptanceSave.setUnitId(pendingIssuesSave.getUnitId());
		PendingIssuesAcceptanceSave.setUserId(pendingIssuesSave.getUserId());

		for (IssueDetail id : issue.getIssueDetail()) {
			List<PendingIssuesAcceptanceSaveItems> items = new ArrayList<>();
			for (IssueDetailBatchMapper bm : id.getIssueDetailBatchMapper()) {
				PendingIssuesAcceptanceSaveItems pendingIssuesAcceptanceSaveItems = new PendingIssuesAcceptanceSaveItems();
				pendingIssuesAcceptanceSaveItems.setBatchMapperId(bm.getId());
				pendingIssuesAcceptanceSaveItems.setAcceptQuantity(bm.getIssueQuantity());
				pendingIssuesAcceptanceSaveItems.setRejectQuantity(0);
				items.add(pendingIssuesAcceptanceSaveItems);

			}
			PendingIssuesAcceptanceSave.setItems(items);
		}
		pendingIssuesAcceptanceMedicationService.pendingIssuesAcceptanceMedicationSave(PendingIssuesAcceptanceSave);
		CommonResponse response = new CommonResponse();
		// response.setId(issueResponse.getId().longValue());
		response.setMessage("Patient Issue saved successfully");
		return response;

	}

	@Transactional
	private Boolean revertAllocatedItemQuantityScheduled1(BatchAllocation batchAllocation, Integer screenId) {
		Boolean flag = false;
		Batch batch = batchAllocation.getBatch();
		// System.out.println("Batch id : "+batch.getId());
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(batchAllocation.getStore().getStoreId(),
				batch.getId(), 0);// 0 itemId);

		// Batch batch = GlobalDao.getBatch(sessionService.getStoreId(), batchId);

		Integer quantity = batchAllocation.getQuantity();

		Integer balanceQuantity = batch.getCurrentQuantity() + quantity;

		if (avaialableStoreStock == null || avaialableStoreStock < 0) {
			avaialableStoreStock = 0;

		}

		for (StoreStock s : batch.getStoreStockList()) {
			if (s.getStore().getStoreId().equals(batchAllocation.getStore().getStoreId())) {

				s.setAvailableQty((avaialableStoreStock + quantity) < 0 ? 0 : avaialableStoreStock + quantity);
				break;
			}
		}
		// batch.getStoreStockList().get(0).setAvailableQty(avaialableStoreStock +
		// quantity);

		batchAllocation.setQuantity(0);
		batchAllocation.setBatch(batch);

		com.param.entity.model.master.Screen screen1 = GlobalDao.getScreen(screenId);

		StockTransaction stockTransaction = new StockTransaction(avaialableStoreStock, screen1, screen1.getCode(),
				quantity, batch.getRate(), batchAllocation.getStore(), batch, 0);

		stockTransaction.setUpdatedByUser(1);
		stockTransaction.setAddedByUser(1);
		stockTransaction.setOrganization(batchAllocation.getOrganization());
		stockTransaction.setUnit(batchAllocation.getUnit());

		GlobalDao.saveStockTransaction(stockTransaction);
		flag = true;
		return flag;
	}

	private void saveStoreStockDetails1(Integer storeId, Long batchId, Integer quantity, UomType uomType,
			Integer unitId, PendingIssuesSave pendingIssuesSave) throws Exception {

		if (quantity > 0) {

			Batch batch = GlobalDao.getBatch(batchId);
			if (batch != null) {
				Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
				Integer tQuantity = 0;
				Integer availableQuantity = 0;
				Integer currentQuantity = 0;
				StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore1(batchId, storeId, unitId);
				StoreStock storeStockDetail = null;
				if (storeStockList == null) {
				} else {

					if (storeStockList.getAvailableQty() < leaseQuantity) {
						// throw new CustomException("Stock is not available with batch
						// "+batch.getBatchNo());
						throw new CustomException(batch.getBatchNo() != null ? batch.getBatchNo() : "NA");
					}

					tQuantity = leaseQuantity + storeStockList.gettQty();
					availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
					if (availableQuantity < 0) {
						availableQuantity = 0;
					}
					storeStockList.setAvailableQty(availableQuantity);

					storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
				}
				Batch batchObject = GlobalDao
						.getBatch(pendingIssuesSave.getStoreId() != null ? pendingIssuesSave.getStoreId()
								: sessionService.getStoreId(), storeStockDetail.getBatch().getId(), unitId);
				currentQuantity = batchObject.getCurrentQuantity() - leaseQuantity;
				batchObject.setCurrentQuantity(currentQuantity);
				GlobalDao.updateBatchLeaseQuantity(batchObject);
				saveStockTransaction(leaseQuantity, storeStockDetail, pendingIssuesSave);
			}

		}

	}

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStockDetail,
			PendingIssuesSave pendingIssuesSave) {

		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(1);
		StockTransaction stockTransaction = new StockTransaction(storeStockDetail.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStockDetail.getBatch().getRate(), new Store(sessionService.getStoreId()),
				new Batch(storeStockDetail.getBatch().getId()), 0);
		stockTransaction.setIsTransit(true);

		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(
				pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId() : sessionService.getUnitId(),
				pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId() : sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);

	}

	private List<DiscountCategoryResponse> PopulateDiscountCategory(Integer orgId) {
		List<DiscountCategoryResponse> discountCategoryList = new ArrayList<DiscountCategoryResponse>();
		for (DiscountCategory discountCategory : GlobalDao.getDiscountCategoryList(orgId, 0)) {
			DiscountCategoryResponse discountCategoryObject = new DiscountCategoryResponse(discountCategory.getId(),
					discountCategory.getCategory());
			discountCategoryList.add(discountCategoryObject);
		}
		return discountCategoryList;
	}
}