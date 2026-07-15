package com.param.pharmacy.ip.service.pendingIssues;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.param.pharmacy.ip.dto.pharmacology.*;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.doctor.DoctorSpecialityMapper;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.entity.model.master.DiscountCategory;
import com.param.entity.model.master.DiscountType;
import com.param.entity.model.master.Frequency;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.Order;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StrengthUnit;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.AssignPicker;
import com.param.entity.model.pharmacy.AssignPickerDetail;
import com.param.entity.model.pharmacy.BatchAllocation;
import com.param.entity.model.pharmacy.Instruction;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.SaleDetailPayeeMapper;
import com.param.entity.model.pharmacy.SaleType;
import com.param.global.billing.dao.IContractConsumptionDao;
import com.param.global.billing.service.IBillingService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.PatientSponsorDetailsDto;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.dto.global.PatientConsession;
import com.param.global.dto.items.BatchResponse;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.pharmacy.dao.IWalkInSalesDao;
import com.param.global.pharmacy.dto.IssueDetailsDto;
import com.param.global.pharmacy.dto.ItemRequest;
import com.param.global.pharmacy.dto.PendingIssuesSave;
import com.param.global.pharmacy.dto.SaleDetailPackageMapperDto;
import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;
import com.param.global.pharmacy.dto.SubstituteItemDto;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.global.IGlobalService;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.assignPicker.IAssignPickerDao;
import com.param.pharmacy.ip.dao.pendingIssues.IPendingIssuesDao;
import com.param.pharmacy.ip.dto.assignPicker.CancelRequest;
import com.param.pharmacy.ip.dto.assignPicker.PrescriptionDetailsDto;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSave;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSaveItems;
//import com.param.pharmacy.ip.dto.pendingIssues.PendingIssuesSave;
import com.param.pharmacy.ip.service.pendingIssues.acceptance.medication.IPendingIssuesAcceptanceMedicationService;
import com.param.pharmacy.narcotics.issueWorklist.dao.INarcoticsIssueWorklistDao;
import com.param.pharmacy.op.dao.salesWorkList.ISalesWorkListDao;
import com.param.pharmacy.op.dto.salesWorkList.DiscountCategoryResponse;
import com.param.procurement.purchase.dao.goodReceiptNote.IGoodReceiptNoteDao;
//import com.param.pharmacy.op.dto.walkInSales.SaleDetailPayeeMapperDto;
import com.param.service.global.SessionService;
import com.param.service.global.model.SaleDetailPackageMapper;
import com.param.utility.TimeZoneComponent;

@Service("pendingIssuesService")
@Transactional
@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
public class PendingIssuesService implements IPendingIssuesService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(PendingIssuesService.class);

	@Autowired
	IWalkInSalesService WalkInSalesService;

	@Autowired
	private SessionService sessionService;

	@Autowired
	private IPendingIssuesDao pendingIssues;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	@Autowired
	IOrderMasterService iOrderMasterService;

	@Autowired
	IMPackageMasterService iMPackageMasterService;

	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao;

	@Autowired
	IWalkInSalesService iWalkInSalesService;

	@Autowired
	IBillingService iBillingService;

	@Autowired
	IGlobalDao iglobalDao;

	@Autowired
	private IAssignPickerDao assignPickerDao;

	@Autowired
	private INarcoticsIssueWorklistDao narcoticsIssueWorklistDao;

	@Autowired
	private ISalesWorkListDao salesWorkListDao;

	@Autowired
	IContractConsumptionDao iContractConsumptionDao;

	private Integer screenId = Screen.PendingIssues.getScreen();
	@Autowired
	IGoodReceiptNoteDao grnDao;
	@Autowired
	private IPendingIssuesAcceptanceMedicationService pendingIssuesAcceptanceMedicationService;
	@Autowired
	IWalkInSalesDao WalkInSalesDao;

	@Override
	@Transactional
	public CommonDropdown getDropdownList() {
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<PharmacologyValidationSearchResult> pendingIssuesSearch(
			PharmacologyValidationSearch pharmacologyValidationSearch) {
		List<PharmacologyValidationSearchResult> assignPickerList = new ArrayList<PharmacologyValidationSearchResult>();
		pharmacologyValidationSearch
				.setToDate((new DateTime(pharmacologyValidationSearch.getToDate())).plusDays(1).toDate());

		List <Issue> issueList = pendingIssues.pendingIssuesSearch(pharmacologyValidationSearch, screenId);
		
		List <Long> ids = new ArrayList<>();
		List<Long> priIds =  new ArrayList<>();
		List<PatientSponsorDetailsDto> admissionDetailsList = new ArrayList<>();
		if(issueList != null && issueList.size()>0){
			for (Issue issue : issueList) {
				ids.add(issue.getPrescription().getId());
			}
			 priIds = GlobalDao.getPrescriptionDetails(ids);
			
			 priIds = priIds.stream().distinct().collect(Collectors.toList());
			 
			 List <Integer> admissionId = new ArrayList<>();
				for (Issue issue : issueList) {
					admissionId.add(issue.getAdmission().getAdmissionId());
				}
			 
			 admissionDetailsList = GlobalDao.getAdmiassionDetails(admissionId);	
			
		}
		
	
		
		for (Issue issue : issueList) {
			
           Long iteCheckIsNarcotic = null;
           
           for (Long preId : priIds) {
        	   
        	   if(issue.getPrescription().getId().equals(preId)){
        		   iteCheckIsNarcotic =  preId;
        	   }
			
           }
           
           if(iteCheckIsNarcotic != null){
        	   
        	    PharmacologyValidationSearchResult assignPickerObject = new PharmacologyValidationSearchResult();

	   			List<DoctorSpecialityMapper> doctorSpeciality = null;
	
	   			String doctorDetail = "";
	   			if (issue.getPrescription().getSpecialityMaster() != null && issue.getPrescription().getDoctor() != null) {
	   				doctorDetail = issue.getPrescription().getDoctor().getFirstName() + " "
	   						+ issue.getPrescription().getDoctor().getMiddleName() + " "
	   						+ issue.getPrescription().getDoctor().getLastName() + " | "
	   						+ issue.getPrescription().getSpecialityMaster().getSpecialityName();
	   			}
	   			String patientDetail = "";
	   			String patientName = "";
	   			if (issue.getPrescription().getAdmission() != null) {
	   				
	   				String wardName = "";
	   				String bedNumber = "";
	   				String s = "";
	   				String contractName ="";
	   				for (PatientSponsorDetailsDto dto : admissionDetailsList) {
						if(dto.getAdmissionId().equals(issue.getPrescription().getAdmission().getAdmissionId())){
							wardName = dto.getWardName();
							bedNumber = dto.getBedName();
							s = dto.getPaymentEntitlementDesc();
							contractName = dto.getCompanyDesc()!= null ? dto.getCompanyDesc() : "";
						}
					}
	   				
	   				
	   				Integer age = Util.getAgeByBirthDate(
	   						issue.getPrescription().getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
	   				patientDetail = issue.getPrescription().getAdmission().getPatient().getPrefix().getPrefixCode() + " "
	   						+ issue.getPrescription().getAdmission().getPatient().getFirstName() + " "
	   						+ issue.getPrescription().getAdmission().getPatient().getMiddleName() + " "
	   						+ issue.getPrescription().getAdmission().getPatient().getLastName() + " " + " | " + age + " | "
	   						+ issue.getPrescription().getAdmission().getPatient().getGender().getGenderCode();
	
	   				patientName = issue.getPrescription().getAdmission().getPatient().getPrefix().getPrefixCode() + " "
	   						+ issue.getPrescription().getAdmission().getPatient().getFirstName() + " "
	   						+ issue.getPrescription().getAdmission().getPatient().getMiddleName() + " "
	   						+ issue.getPrescription().getAdmission().getPatient().getLastName();
	
	   				assignPickerObject = new PharmacologyValidationSearchResult((long) issue.getId(),
	   						issue.getPrescription().getVisitType().getVisitTypeName(),
	   						issue.getPrescription().getAdmission().getCreatedDate(),
	   						issue.getPrescription().getPrescriptionDate(),
	   						issue.getPrescription().getAdmission().getPatient().getUhidNumber(), patientDetail,
	   						doctorDetail, issue.getPrescription().getPriority().getPriorityDesc(),
	   						issue.getStatus().getStatus(), issue.getStatus().getId(), patientName, age);
	                   assignPickerObject.setPriorityId(issue.getPrescription().getPriority().getPriorityId());
	   				assignPickerObject.setWardName(wardName!=null && !wardName.equals("") ? wardName : null);
	   				assignPickerObject.setBedNo(bedNumber!=null && !bedNumber.equals("") ? bedNumber : null);
	   				assignPickerObject.setAdmissionId(issue.getPrescription().getAdmission().getAdmissionId());
	   				assignPickerObject.setImagePath(issue.getPrescription().getAdmission().getPatient().getImagePath());
	   				assignPickerObject
	   						.setGenderId(issue.getPrescription().getAdmission().getPatient().getGender().getGenderId());
	   				assignPickerObject
	   						.setGenderCode(issue.getPrescription().getAdmission().getPatient().getGender().getGenderCode());
	   				assignPickerObject.setMedicationType(issue.getPrescription().getMedicationType());
	   				assignPickerObject.setEntitlementDesc(s);
	   				assignPickerObject.setContractName(contractName);
	   				String userName =GlobalDao.getUserNameByUserId(issue.getAddedByUser(), pharmacologyValidationSearch.getUnitId());
	   				assignPickerObject.setUserName(userName);
	   				Integer isFinalDischarge=GlobalDao.getIsFinalDischarge(issue.getPrescription().getAdmission().getAdmissionId());
	   				assignPickerObject.setIsFinalDischarge(isFinalDischarge);
	   				assignPickerObject.setPrescriptionId(issue.getPrescription().getId());
	   				assignPickerObject.setVisitTypeId(issue.getPrescription().getVisitType().getVisitTypeId());
	   				assignPickerObject.setPrescriptionNo((issue.getPrescription().getPrescriptionNo()!=null?issue.getPrescription().getPrescriptionNo():" "));
	   				assignPickerList.add(assignPickerObject);
	   			}
        	   
           }

		}
		return assignPickerList;
	}

	@Override
	@Transactional
	public List<PharmacologyValidationSearchResult> pendingIssuesSearchBP(
			PharmacologyValidationSearch pharmacologyValidationSearch) {
		List<PharmacologyValidationSearchResult> assignPickerList = new ArrayList<PharmacologyValidationSearchResult>();
		pharmacologyValidationSearch
				.setToDate((new DateTime(pharmacologyValidationSearch.getToDate())).plusDays(1).toDate());

		assignPickerList = pendingIssues.pendingIssuesSearchBP(pharmacologyValidationSearch, screenId);

		for (Issue issue : pendingIssues.pendingIssuesSearch(pharmacologyValidationSearch, screenId)) {
			if (issue.getPrescription().getAdmission() != null) {
				PharmacologyValidationSearchResult assignPickerObject = new PharmacologyValidationSearchResult();

//				List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
//						.doctorSearchByIdValue(issue.getPrescription().getDoctor().getDoctorId());
				String doctorDetail = issue.getPrescription().getDoctor().getFirstName() + " "
						+ issue.getPrescription().getDoctor().getMiddleName() + " "
						+ issue.getPrescription().getDoctor().getLastName() + " | "
						+ issue.getPrescription().getSpecialityMaster().getSpecialityName();

				String patientDetail = "";
				String wardName = "";
				String bedNumber = "";

				AdmissionDetail admissionDetail = GlobalDao
						.getPatientWardBedDetails(issue.getPrescription().getAdmission().getAdmissionId());
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
						issue.getPrescription().getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
				patientDetail = issue.getPrescription().getAdmission().getPatient().getPrefix().getPrefixCode() + " "
						+ issue.getPrescription().getAdmission().getPatient().getFirstName() + " "
						+ issue.getPrescription().getAdmission().getPatient().getMiddleName() + " "
						+ issue.getPrescription().getAdmission().getPatient().getLastName() + " " + " | " + age + " | "
						+ issue.getPrescription().getAdmission().getPatient().getGender().getGenderCode();

				assignPickerObject = new PharmacologyValidationSearchResult((long) issue.getId(),
						issue.getPrescription().getVisitType().getVisitTypeName(),
						issue.getPrescription().getAdmission().getCreatedDate(),
						issue.getPrescription().getPrescriptionDate(),
						issue.getPrescription().getAdmission().getPatient().getUhidNumber(), patientDetail,
						doctorDetail, issue.getPrescription().getPriority().getPriorityDesc(),
						issue.getStatus().getStatus(), issue.getStatus().getId());

				assignPickerObject.setWardName(!wardName.equals("") ? wardName : null);
				assignPickerObject.setBedNo(!bedNumber.equals("") ? bedNumber : null);

				assignPickerList.add(assignPickerObject);
			}

		}
		return assignPickerList;
	}

	@Override
	@Transactional
	public PharmacologyValidationDetails pendingIssuesDetails(Integer id, Integer unitId, Integer storeId,
			Integer orgId) {
		
		PharmacologyValidationDetails assignPickerDetailObject = new PharmacologyValidationDetails();
		List<PharmacologyItem> itemList = new ArrayList<>();
		List<PharmacologyItem> savedListAll =  new ArrayList<>();
		Issue issue = pendingIssues.pendingIssuesById(id);
		List<PatientSponsorDetails> patientSponsorDetails = null;
		if (issue != null && issue.getPrescription() != null && issue.getAdmission() != null)
			patientSponsorDetails = iPatientSponsorDetailsDao
					.findByAdmissionId(issue.getPrescription().getAdmission().getAdmissionId());
		
		savedListAll = salesWorkListDao.getissuedItemListForNewIssueItems(id);
		List<IssueDetail> issueDetails = pendingIssues.pendingIssuesDetails(id);
		if(savedListAll != null && savedListAll.size() > 0) {
			for (PharmacologyItem dto : savedListAll) {
				PharmacologyItem dtoList=new PharmacologyItem();
				//dtoList.setParentIssueDetailsId(dto.getPrescriptionDetailId().intValue());
				dtoList.setIsThreeUOMApplicable(dto.getIsThreeUOMApplicable());
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
				dtoList.setIssuedQty(dto.getIssuedQty());
				dtoList.setIsNarcotic(false);
				dtoList.setFormulationTypeId(dto.getFormulationTypeId());
				dtoList.setStorageUnitName(dto.getStorageUnitName());
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
				List<Batch> batchList = GlobalDao.getBatchDetailsPharmacyConsignmentAll(
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
						batchResponse.setIssueQuantity(dtoList.getIssuedQty().intValue());
					}

					medicineBatches.add(batchResponse);
				}
				dtoList.setBatch(medicineBatches);
				itemList.add(dtoList);
			
			}
		}
		// Nitin
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

		/* if (issueDetails != null && !issueDetails.isEmpty()) { */

		List<PharmacologyItem> issuedQuantityList = new ArrayList<>();
		if (issueDetails != null && issueDetails.size() > 0) {

		} else {
			for (PharmacologyItem assignPickerItem : pharmacologyItemListAddedItems) {
				List<IssueDetail> assignPickerItemList = narcoticsIssueWorklistDao
						.addeIssueItems(assignPickerItem.getIssueDetailsId());
				if (assignPickerItemList != null && assignPickerItemList.size() > 0) {

					issueDetails.addAll(assignPickerItemList);
					for (IssueDetail issueDetailDto : issueDetails) {
						issueDetailDto.setIsFromAssignPicker(true);
					}
				}
			}
			List<Long> prescriptionDetailId = issueDetails.stream()
					.map(issues -> issues.getPrescriptionDetail().getId()).collect(Collectors.toList());

			issuedQuantityList = pendingIssues.getIssuedQuantity(prescriptionDetailId);
		}

		for (IssueDetail issueDetail : issueDetails) {

			if (issueDetail.getPrescriptionDetail() != null && issueDetail.getPrescriptionDetail().getItem() != null
					&& !issueDetail.getPrescriptionDetail().getItem().getIsNarcotic()) {

//					List<PharmacologyItem> substututeList = new ArrayList<>();
				List<BatchResponse> medicineBatches = new ArrayList<>();
				List<IssueDetailBatchMapper> batchMapperList = issueDetail.getIssueDetailBatchMapper();

				// Get the batch list by item id
				List<Batch> batchList = GlobalDao.getBatchDetailsPharmacyConsignmentAll(
						issueDetail.getPrescriptionDetail().getItem().getId(), storeId, unitId);
				for (Batch batch : batchList) {
					// Populate batch response
					BatchResponse batchResponse = Util.PopulateBatchWithStockWithFlag(batch, false);

					// Check if batch mapper is present
					Optional<IssueDetailBatchMapper> batchMapper = batchMapperList.stream()
							.filter(mapperBatch -> mapperBatch.getId().equals(batch.getId())).findFirst();

					if (batchMapper.isPresent()) {
						batchResponse.setIsSelected(Boolean.TRUE);
						batchResponse.setUomTypeId(batchMapper.get().getUomType().getId());
						batchResponse.setUomUnitId(batchMapper.get().getUomUnit().getId());
						batchResponse.setIssueQuantity(batchMapper.get().getIssueQuantity());
						// batchResponse.setPickedQuantity(issueDetail.getIssueQuantity());
					}
					/*
					 * else if (issueDetail.getBatch() != null &&
					 * batch.getId().equals(issueDetail.getBatch().getId())) {
					 * batchResponse.setIsSelected(Boolean.TRUE);
					 * batchResponse.setUomTypeId(issueDetail.getAssignPickerDetail()!=
					 * null?issueDetail.getAssignPickerDetail().getUomType().getId():issueDetail.
					 * getUomType().getId());
					 * batchResponse.setUomUnitId(issueDetail.getAssignPickerDetail()!= null?
					 * issueDetail.getAssignPickerDetail().getUomUnit().getId():issueDetail.
					 * getUomUnit().getId());
					 * batchResponse.setIssueQuantity(issueDetail.getIssueQuantity()!=null?
					 * issueDetail.getIssueQuantity():0); //
					 * batchResponse.setPickedQuantity(issueDetail.getIssueQuantity()); }
					 */
					else if (issueDetail.getBatch() != null && issueDetail.getAssignPickerDetail() != null
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

				Optional<PharmacologyItem> issuedQuantity = issuedQuantityList.stream().filter(
						qty -> qty.getPrescriptionDetailId().equals(issueDetail.getPrescriptionDetail().getId()))
						.findFirst();
				// issueDetail.getAssignPickerDetail().getAssignPicker().getAssignPickerDetailAddedItems().get

				PharmacologyItem item = new PharmacologyItem((long) issueDetail.getId(),
						issueDetail.getPrescriptionDetail().getItem().getId(),
						issueDetail.getPrescriptionDetail().getItem().getCode(),

						issueDetail.getPrescriptionDetail().getItem().getName(),
						issueDetail.getPrescriptionDetail().getItem().getGeneric().getId(),
						issueDetail.getPrescriptionDetail().getItem().getGeneric().getName(),
						issueDetail.getPrescriptionDetail().getIsIntervention(),
						issueDetail.getPrescriptionDetail().getOrderQty(), 0,
						issueDetail.getPrescriptionDetail().getItem().getManufacturer().getId(),
						issueDetail.getPrescriptionDetail().getItem().getManufacturer().getName(), medicineBatches,
						issueDetail.getPrescriptionDetail().getItem().getSalesTax() != null ? issueDetail.getPrescriptionDetail().getItem().getSalesTax().getId() : null,
						issueDetail.getPrescriptionDetail().getItem().getSalesTax() != null ? issueDetail.getPrescriptionDetail().getItem().getSalesTax().getName() : null,
						issueDetail.getPrescriptionDetail().getItem().getSalesTax() != null ? issueDetail.getPrescriptionDetail().getItem().getSalesTax().getTaxPercentage() : null,
						issuedQuantity.isPresent() ? issuedQuantity.get().getIssuedQuantity() : 0L,
						issueDetail.getPrescriptionDetail().getItem().getIsNarcotic()
				/*
				 * issueDetail.getAssignPickerDetail() != null ?
				 * issueDetail.getAssignPickerDetail().getPickedQuantity() : 0
				 */
				);
				item.setIsThreeUOMApplicable(issueDetail.getPrescriptionDetail().getItem().getIsThreeUOMApplicable());
				item.setFormulationTypeId(issueDetail.getPrescriptionDetail().getItem().getFormulationType().getId());
				item.setStorageUnitName(issueDetail.getPrescriptionDetail().getItem().getStorageUnit().getUnitName());
				item.setDurationUnit(issueDetail.getPrescriptionDetail().getDurationUnit());
				item.setPriorityId(issueDetail.getPrescriptionDetail().getPriorityId());

				if(issueDetail.getPrescriptionDetail() != null){
					if(issueDetail.getPrescriptionDetail().getItem() != null)
						item.setIsLasa(issueDetail.getPrescriptionDetail().getItem().getIsLasa() != null ? issueDetail.getPrescriptionDetail().getItem().getIsLasa() : false);
				}

				if(issueDetail.getPrescriptionDetail()!=null && issueDetail.getPrescriptionDetail().getInfusionFlag()!=null)
				{
				item.setInfusionflag(issueDetail.getPrescriptionDetail().getInfusionFlag());
				}
				item.setIsSolution(issueDetail.getPrescriptionDetail().getIsSolution());
				if(issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().size()>0)
				{
					item.setFrequencyDesc(issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().get(0)
							.getFrequency().getDescription());
					item.setStartDate(
							issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().get(0).getStartDate());
					item.setStopDate(
							issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().get(0).getStopDate());
				}
				
				// ,
				String instructionDesc = "";
				if (issueDetail.getPrescriptionDetail().getInstruction() != null) {

					instructionDesc = issueDetail.getPrescriptionDetail().getInstruction().getInstructionDesc();

				} else {

					if (issueDetail.getPrescriptionDetail().getInstructionDesc() != null) {
						instructionDesc = issueDetail.getPrescriptionDetail().getInstructionDesc();
					}
				}
				item.setInstructionDesc(instructionDesc);

				item.setDose(issueDetail.getPrescriptionDetail().getItem().getStrength() != null
						? issueDetail.getPrescriptionDetail().getItem().getStrength()
						: null);
				item.setDoseUnit(issueDetail.getPrescriptionDetail().getItem().getStrengthUnit() != null
						? issueDetail.getPrescriptionDetail().getItem().getStrengthUnit().getUnits()
						: null);
				item.setIssueDetailsId(issueDetail.getId());
				item.setIssueDetailsId(issueDetail.getId());
				item.setIsFromAssignPicker(issueDetail.getIsFromAssignPicker());
				item.setPrescriptionDetailId(issueDetail.getPrescriptionDetail().getId());
				item.setStatus(issueDetail.getStatus().getStatus());
				item.setStatusId(issueDetail.getStatus().getId());
				item.setRate(issueDetail.getRate() != null ? issueDetail.getRate() : null);
				item.setTotalAmount(issueDetail.getSaleDetail()!=null && issueDetail.getSaleDetail().getTotalAmount() != null ? issueDetail.getSaleDetail().getTotalAmount() : null);
				item.setNetAmount(issueDetail.getSaleDetail()!=null && issueDetail.getSaleDetail().getNetAmount() != null ? issueDetail.getSaleDetail().getNetAmount() : null);
				item.setSelfPayable( issueDetail.getSaleDetail()!=null && issueDetail.getSaleDetail().getSelfPayable() != null ? issueDetail.getSaleDetail().getSelfPayable() : null);
				item.setTaxAmount(issueDetail.getSaleDetail()!=null && issueDetail.getSaleDetail().getTaxAmount() != null ? issueDetail.getSaleDetail().getTaxAmount() : null);
				item.setCreditPayable(issueDetail.getSaleDetail()!=null && issueDetail.getSaleDetail().getCreditPayable() != null ? issueDetail.getSaleDetail().getCreditPayable() : null);
				item.setDiscountAmount(
						issueDetail.getSaleDetail()!=null && issueDetail.getSaleDetail().getDiscountAmount()!= null ? issueDetail.getSaleDetail().getDiscountAmount() : null);
				item.setPatientClassPercentage(
						issueDetail.getSaleDetail()!=null && issueDetail.getSaleDetail().getConsessionPercentage() != null ? issueDetail.getSaleDetail().getConsessionPercentage() : null);
				item.setPatientClassAmount(
						issueDetail.getSaleDetail()!=null && issueDetail.getSaleDetail().getConsessionAmount() != null ? issueDetail.getSaleDetail().getConsessionAmount() : null);
				item.setStoreId(issueDetail.getPrescriptionDetail().getStore().getStoreId());

				item.setPackageConsumptionAmt(
						issueDetail.getSaleDetail()!=null && issueDetail.getSaleDetail().getPackageConsumptionAmt() != null ? issueDetail.getSaleDetail().getPackageConsumptionAmt() : null);
				if(issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().size()>0)
				{
					item.setFrequencyDesc(issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().get(0)
							.getFrequency().getDescription());
					item.setStartDate(
							issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().get(0).getStartDate());
					item.setStopDate(
							issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().get(0).getStopDate());
				}
				if (pharmacologyItemListAddedItems != null && pharmacologyItemListAddedItems.size() > 0) {

					for (IssueDetail issuedto : issueDetailsIssueQuentityList) {

						if (issueDetail.getId().equals(issuedto.getId())) {

							for (PharmacologyItem pharmacologyItemDto : pharmacologyItemListAddedItems) {
								if (new Long(issuedto.getBatchId()).equals(pharmacologyItemDto.getBatchId())) {
									item.setPickedQuantity(pharmacologyItemDto.getQuantity());
								}
							}
						}
					}
				} else {
					item.setPickedQuantity(0);
				}

				item.setIssuedQuantity(
						issueDetail.getIssueQuantity() != null ? issueDetail.getIssueQuantity().longValue() : 0);
				if (issueDetail.getPrescriptionDetail().getItem().getGeneric() != null) {
					item.setProductCategoryId(
							issueDetail.getPrescriptionDetail().getItem().getGeneric().getProductCategory().getId());
				} else {
					item.setProductCategoryId(
							issueDetail.getPrescriptionDetail().getItem().getProductCategory().getId());
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

							PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
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

				// List<PharmacologyItem> substututeList = new ArrayList<>();
				// For Selected Substitute List
				// with assign picker
				if (issueDetail.getIsFromAssignPicker() == true) {
					List<SubstituteItemDto> substitute = GlobalDao
							.getListOfPendingIssuesSubstitutes(issueDetail.getIssue().getId());
					if (substitute != null) {

					}
					for (SubstituteItemDto substituteList : substitute) {

						if (item.getPrescriptionDetailId() != substituteList.getPrescriptionDetailid().longValue()) {

							PharmacologyItem itm = new PharmacologyItem();
							itm.setIsThreeUOMApplicable(substituteList.getIsThreeUOMApplicable());
							itm.setId(substituteList.getIssueDtlId().longValue());
							itm.setPrescriptionDetailId(substituteList.getPrescriptionDetailid().longValue());
							itm.setParentSubstituteId(substituteList.getParentSubstituteId());
							itm.setFrequency(substituteList.getFrequency());
							itm.setDose(substituteList.getDose());
							itm.setIsFromAssignPicker(true);
							itm.setGenericId(substituteList.getGenericId());
							itm.setGenericName(substituteList.getGenericName());
							itm.setItemId(substituteList.getItemId());
							itm.setItemName(substituteList.getItemName());
							itm.setItemCode(substituteList.getItemCode());
							itm.setManufacturerName(substituteList.getManufacturer());
							itm.setManufacturerId(substituteList.getManufactureId());
							item.setIsLasa(substituteList.getIsLasa() != null ? substituteList.getIsLasa() : false);
							itm.setIssuedQuantity(substituteList.getIssuedQuantity() != null
									? substituteList.getIssuedQuantity().longValue()
									: 0);
							itm.setQuantity(
									substituteList.getQuantityIssued() != null ? substituteList.getQuantityIssued()
											: 0);
							if(issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().size()>0)
							{
								itm.setFrequencyDesc(issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails()
										.get(0).getFrequency().getDescription());
								itm.setStartDate(issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails()
										.get(0).getStartDate());
								itm.setStopDate(issueDetail.getPrescriptionDetail().getPrescriptionFrequencyDetails().get(0)
										.getStopDate());
							}
							
							if (pharmacologyItemListAddedItems != null && pharmacologyItemListAddedItems.size() > 0) {

								for (PharmacologyItem pharmacologyItemDto : pharmacologyItemListAddedItems) {

									if (new Long(substituteList.getBatchid())
											.equals(pharmacologyItemDto.getBatchId())) {

										itm.setPickedQuantity(substituteList.getQuantityIssued() != null
												? substituteList.getQuantityIssued()
												: 0);
									}

								}
							} else {
								itm.setPickedQuantity(0);
							}
							// itm.setPickedQuantity(substituteList.getQuantityIssued()!=null ?
							// substituteList.getQuantityIssued():0);
							itm.setStorageUnitId(substituteList.getStorageUnitId());
							itm.setStorageUnitName(substituteList.getStorageUnitName());
							itm.setInterventionFlag(substituteList.getInterventionFlag());
							itm.setStatus(substituteList.getStatus());
							itm.setStatusId(substituteList.getStatusId());
							itm.setQuantityOrdered(substituteList.getQuantityOrdered());
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
							itm.setTaxAmount(issueDetail.getTaxAmount() != null ? substituteList.getTaxAmount() : null);
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
							itm.setParentIssueDetailsId(substituteList.getParentIssueDetailsId());
							itm.setParentSubstituteId(substituteList.getParentSubstituteId());
							itm.setPatientClassAmount(substituteList.getConsessionAmount());
							itm.setPatientClassPercentage(substituteList.getConsessionPercentage());
							itm.setPackageConsumptionAmt(substituteList.getPackageConsumptionAmt() != null
									? substituteList.getPackageConsumptionAmt()
									: null);
							List<BatchResponse> mediBatchesSub = new ArrayList<>();
							List<IssueDetailBatchMapper> batchMapperListSub = issueDetail.getIssueDetailBatchMapper();
							itm.setInstructionDesc(instructionDesc);
							// Get the batch list by item id
							List<Batch> batchListSub = GlobalDao
									.getBatchDetailsPharmacyConsignmentAll(substituteList.getItemId(), storeId, unitId);

							for (Batch batchSub : batchListSub) {
								// Populate batch response
								BatchResponse batchResponseSub = Util.PopulateBatchWithStockWithFlag(batchSub, false);

								// Check if batch mapper is present
								Optional<IssueDetailBatchMapper> batchMapperSub = batchMapperListSub.stream()
										.filter(mapperBatch -> mapperBatch.getId().equals(batchSub.getId()))
										.findFirst();

								if (batchMapperSub.isPresent()) {
									batchResponseSub.setIsSelected(Boolean.TRUE);
									batchResponseSub.setUomTypeId(batchMapperSub.get().getUomType().getId());
									batchResponseSub.setActualLendingPrice(batchMapperSub.get().getBatch().getActualLendingPrice());
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
							
							for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
								List<Integer> prodCatIds = new ArrayList<Integer>();
								prodCatIds.add(substituteList.getProductCategoryId() != null? substituteList.getProductCategoryId(): null);



								List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
								sponsorDetail.getPaymentEntitlementId());
								if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
								if (pharmacyTariffs.size() > 1) {
								// throw exception?
								// Don't throw - get first object and set data.
								}



								PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
								itm.setMarkUpDown(pharmacyTariff.getMarkUpDown());
								itm.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());



								break;
								}
							}


						}

					}

					itemList.add(item);
				}

				// without assign picker
				if (issueDetail.getIsFromAssignPicker() == false) {

					List<SubstituteItemDto> substitute = GlobalDao
							.getListOfSubstitutesWithoutAssignPicker(issueDetail.getIssue().getId());
					if (substitute != null) {
						for (SubstituteItemDto substituteList : substitute) {

							if (item.getPrescriptionDetailId() != null && substituteList.getParentSubstituteId() != null 
									&& (item.getPrescriptionDetailId().equals(substituteList.getParentSubstituteId().longValue()))) {

								PharmacologyItem itm = new PharmacologyItem();
								itm.setIsThreeUOMApplicable(substituteList.getIsThreeUOMApplicable());
								itm.setId(substituteList.getIssueDtlId().longValue());
								itm.setPrescriptionDetailId(substituteList.getPrescriptionDetailid().longValue());
								itm.setParentSubstituteId(substituteList.getParentSubstituteId());
								itm.setFrequency(substituteList.getFrequency());
								itm.setIsFromAssignPicker(false);
								itm.setDose(substituteList.getDose());
								itm.setGenericId(substituteList.getGenericId());
								itm.setGenericName(substituteList.getGenericName());
								itm.setItemId(substituteList.getItemId());
								itm.setItemName(substituteList.getItemName());
								itm.setItemCode(substituteList.getItemCode());
								itm.setManufacturerName(substituteList.getManufacturer());
								itm.setManufacturerId(substituteList.getManufactureId());
								itm.setIssuedQuantity(substituteList.getIssuedQuantity() != null
										? substituteList.getIssuedQuantity().longValue()
										: 0);
								itm.setStorageUnitId(substituteList.getStorageUnitId());
								itm.setStorageUnitName(substituteList.getStorageUnitName());
								// itm.setQuantity(substituteList.getQuantityIssued()!=null ?
								// substituteList.getQuantityIssued():0);
								itm.setPickedQuantity(0);
								itm.setInterventionFlag(substituteList.getInterventionFlag());
								itm.setStatus("Completed");
								itm.setStatusId(substituteList.getStatusId());
								itm.setTax(substituteList.getTax());
								itm.setQuantityOrdered(substituteList.getQuantityOrdered());
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
								itm.setTaxAmount(issueDetail.getTaxAmount() != null ? substituteList.getTaxAmount() : null);
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
								itm.setParentIssueDetailsId(substituteList.getParentIssueDetailsId());
								itm.setPackageConsumptionAmt(substituteList.getPackageConsumptionAmt() != null
										? substituteList.getPackageConsumptionAmt()
										: null);
								itm.setPatientClassAmount(substituteList.getConsessionAmount());
								itm.setPatientClassPercentage(substituteList.getConsessionPercentage());
								itm.setInstructionDesc(instructionDesc);
								itm.setUomTypeId(substituteList.getUomTypeId());
								itm.setUomUnitId(substituteList.getUomUnitId());
								item.setIsLasa(substituteList.getIsLasa() != null ? substituteList.getIsLasa() : false);
								List<BatchResponse> mediBatchesSub = new ArrayList<>();
								List<IssueDetailBatchMapper> batchMapperListSub = issueDetail.getIssueDetailBatchMapper();

								// Get the batch list by item id
								List<Batch> batchListSub = GlobalDao
										.getBatchDetailsPharmacyConsignmentAll(substituteList.getItemId(), storeId, unitId);

								for (Batch batchSub : batchListSub) {
									// Populate batch response
									BatchResponse batchResponseSub = Util.PopulateBatchWithStockWithFlag(batchSub, false);

									// Check if batch mapper is present
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
								for (PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
									List<Integer> prodCatIds = new ArrayList<Integer>();
									prodCatIds.add(substituteList.getProductCategoryId() != null
											? substituteList.getProductCategoryId()
											: null);

									List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds,
											sponsorDetail.getPaymentEntitlementId());
									if (pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
										if (pharmacyTariffs.size() > 1) {
											// throw exception?
											// Don't throw - get first object and set data.
										}

										PharmacyTariff pharmacyTariff = pharmacyTariffs.get(0);
										itm.setMarkUpDown(pharmacyTariff.getMarkUpDown());
										itm.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());

										break;
									}
								}

								
							}
						}
					}
					
					itemList.add(item);
					// item.setSelectedItems(substututeList);

				}

			}
//			}
		}

		/*
		 * List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
		 * .doctorSearchByIdValue(issue.getPrescription().getDoctor().getDoctorId());
		 */
		String doctorDetail = "";
		if (issue.getPrescription().getSpecialityMaster() != null && issue.getPrescription().getDoctor() != null) {
			doctorDetail = issue.getPrescription().getDoctor().getFirstName() + " "
					+ issue.getPrescription().getDoctor().getMiddleName() + " "
					+ issue.getPrescription().getDoctor().getLastName() + " | "
					+ issue.getPrescription().getSpecialityMaster().getSpecialityName();
		}
		String patientDetail = "";
		if (issue.getPrescription().getAdmission() != null) {
			String wardName = "";
			String bedNumber = "";
			Date doa = null;
			Date pdd = null;
			AdmissionDetail admissionDetail = GlobalDao
					.getPatientWardBedDetails(issue.getPrescription().getAdmission().getAdmissionId());
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
					issue.getPrescription().getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
			patientDetail = issue.getPrescription().getAdmission().getPatient().getPrefix().getPrefixCode() + " "
					+ issue.getPrescription().getAdmission().getPatient().getFirstName() + " "
					+ issue.getPrescription().getAdmission().getPatient().getMiddleName() + " "
					+ issue.getPrescription().getAdmission().getPatient().getLastName() + " " + " | " + age + " | "
					+ issue.getPrescription().getAdmission().getPatient().getGender().getGenderCode();

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

								if (mainItem.getPrescriptionDetailId().equals(dtoSelected.getParentSubstituteId().longValue())) {
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
							if(mainItem.getPrescriptionDetailId() != null){
							if (mainItem.getPrescriptionDetailId()
									.equals(dtoSelected.getParentSubstituteId().longValue())) {
								fount = 1;
							}
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
							if(mainItem.getId() != null){
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
			assignPickerDetailObject = new PharmacologyValidationDetails((long) issue.getId(),
					issue.getPrescription().getAdmission().getPatient().getPatientId(), patientDetail,
					issue.getPrescription().getAdmission().getPatient().getDob(),
					doctorDetail != "" ? issue.getPrescription().getDoctor().getDoctorId() : null, doctorDetail,
					issue.getPrescription().getAdmission().getPatient().getUhidNumber(),
					issue.getPrescription().getAdmission().getVisitType().getVisitTypeId(),
					PopulateDiscountCategory(orgId), itemList);
			assignPickerDetailObject.setStatus(issue.getStatus().getStatus());
			assignPickerDetailObject.setStatusId(issue.getStatus().getId());
			assignPickerDetailObject.setPrescriptionId(issue.getPrescription().getId());
			assignPickerDetailObject.setAdmissionId(issue.getPrescription().getAdmission().getAdmissionId());
			assignPickerDetailObject.setWardName(!wardName.equals("") ? wardName : null);
			assignPickerDetailObject.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			assignPickerDetailObject.setDoa(doa != null ? doa : null);
			assignPickerDetailObject.setPdd(pdd != null ? pdd : null);
			assignPickerDetailObject
					.setGenderId(issue.getPrescription().getAdmission().getPatient().getGender().getGenderId());
			assignPickerDetailObject.setImagePath(issue.getPrescription().getAdmission().getPatient().getImagePath());
		} else if (issue.getPrescription().getEncounter() != null) {
			Integer age = Util.getAgeByBirthDate(
					issue.getPrescription().getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
			patientDetail = issue.getPrescription().getEncounter().getPatient().getPrefix().getPrefixCode() + " "
					+ issue.getPrescription().getEncounter().getPatient().getFirstName() + " "
					+ issue.getPrescription().getEncounter().getPatient().getMiddleName() + " "
					+ issue.getPrescription().getEncounter().getPatient().getLastName() + " " + " | " + age + " | "
					+ issue.getPrescription().getEncounter().getPatient().getGender().getGenderCode();

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

			assignPickerDetailObject = new PharmacologyValidationDetails((long) issue.getId(),
					issue.getPrescription().getEncounter().getPatient().getPatientId(), patientDetail,
					issue.getPrescription().getEncounter().getPatient().getDob(),
					issue.getPrescription().getDoctor().getDoctorId(), doctorDetail,
					issue.getPrescription().getEncounter().getPatient().getUhidNumber(),
					issue.getPrescription().getEncounter().getVisitType().getVisitTypeId(),
					PopulateDiscountCategory(orgId), itemList);
			assignPickerDetailObject.setPrescriptionId(issue.getPrescription().getId());
			assignPickerDetailObject.setStatus(issue.getStatus().getStatus());
			assignPickerDetailObject.setStatusId(issue.getStatus().getId());
			assignPickerDetailObject
					.setGenderId(issue.getPrescription().getEncounter().getPatient().getGender().getGenderId());
			assignPickerDetailObject.setImagePath(issue.getPrescription().getEncounter().getPatient().getImagePath());
			assignPickerDetailObject.setEncounterId(issue.getPrescription().getEncounter().getEncounterId());
		}
        assignPickerDetailObject.setRemark(issue.getRemark());
        ArrayList<PharmacologyItem> newList=new ArrayList<PharmacologyItem>();
        Integer count=0;
        for (PharmacologyItem mainItem : assignPickerDetailObject.getItems())
        {
        	if(count==0 && mainItem.getIssueDetailsId()!=null)
        	{
        		newList.add(mainItem);
        		count++;
        	}
        	else
        	{
        		boolean flag=false;
        		for (PharmacologyItem newItem : newList)
        		{
        			if(newItem.getIssueDetailsId()==mainItem.getIssueDetailsId())
        			{
        				flag=true;
        			}
        		}
        		if(flag==false)
        		{
        			if(mainItem.getIssueDetailsId()!=null)
        			{
        			newList.add(mainItem);
        			}
        		}
        	}
        }
        assignPickerDetailObject.setPrescriptionNo(issue.getPrescription().getPrescriptionNo());
        assignPickerDetailObject.setItems(newList);
		return assignPickerDetailObject;

	}

	private List<DiscountCategoryResponse> PopulateDiscountCategory(Integer orgId) {
		List<DiscountCategoryResponse> discountCategoryList = new ArrayList<DiscountCategoryResponse>();
		for (DiscountCategory discountCategory : GlobalDao.getDiscountCategoryList(orgId, null)) {
			DiscountCategoryResponse discountCategoryObject = new DiscountCategoryResponse(discountCategory.getId(),
					discountCategory.getCategory());
			discountCategoryList.add(discountCategoryObject);
		}
		return discountCategoryList;
	}

	@Override
	@Transactional
	//@Transactional(rollbackOn=CustomException.class)
	public CommonResponse pendingIssuesSave(PendingIssuesSave pendingIssuesSave) throws Exception {
		Boolean isAcceptance = false;
	 
			ApplicationConfiguration app = grnDao.getApplicationConfiguration(pendingIssuesSave.getUnitId());
			if (app != null && app.getIsAutoMedicationAcceptance() != null) {
				isAcceptance = app.getIsAutoMedicationAcceptance();
			}
			
			 List<com.param.common.dto.CustomException> errors = new ArrayList<com.param.common.dto.CustomException>();
			 List<Integer> list = new ArrayList<>();
			for (ItemRequest item : pendingIssuesSave.getItems()) {
				
			  Integer quantity=item.getQuantity();
	          Long batchId=item.getBatchId();
	          Integer storeId=pendingIssuesSave.getStoreId();
	          UomType uomType= new UomType(item.getUomTypeId());
	          Integer unitId=pendingIssuesSave.getUnitId();
				if (quantity > 0) { 
						Batch batch = GlobalDao.getBatch(batchId);
						if (batch != null) {
							Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
							StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore1(batchId, storeId, unitId);
						 
							if (storeStockList == null) {
							 
							} else {
								if( storeStockList.getAvailableQty() < leaseQuantity)
								{
									list.add(pendingIssuesSave.getItems().indexOf(item));
									com.param.common.dto.CustomException e=new com.param.common.dto.CustomException("Stock is not available",storeStockList.getAvailableQty(),(batch.getBatchNo()!=null ? batch.getBatchNo():"NA"));
									errors.add(e);
								 
								}
							}
						}
					 
				}
				
			}

//		if (!errors.isEmpty() && errors.size() > 0) {
//			throw new com.param.common.dto.CustomException("Stock is not available", errors);
//		}
			if(pendingIssuesSave.getItems().size()==list.size())
			{
				if (!errors.isEmpty() && errors.size() > 0) {
					throw new com.param.common.dto.CustomException("Stock is not available", errors);
				}
			}
			if(pendingIssuesSave.getItems().size()!=list.size() && list.size()>0)
			{
				pendingIssuesSave.setStatusId(18);
			}
			Integer count=0;
			for (Integer integer : list) {
				if(count>0)
				{
					integer=integer-1;
				}
				pendingIssuesSave.setNetAmount(pendingIssuesSave.getNetAmount()-pendingIssuesSave.getItems().get(integer).getNetAmount());
				pendingIssuesSave.setTotalAmount(pendingIssuesSave.getTotalAmount()-pendingIssuesSave.getItems().get(integer).getTotalAmount());
				pendingIssuesSave.setTotalSelfPayable(pendingIssuesSave.getTotalSelfPayable()-pendingIssuesSave.getItems().get(integer).getSelfPayable());
				pendingIssuesSave.setTotalCreditPayable(pendingIssuesSave.getTotalCreditPayable()-pendingIssuesSave.getItems().get(integer).getCreditPayable());
				pendingIssuesSave.setDiscount(pendingIssuesSave.getDiscount()-pendingIssuesSave.getItems().get(integer).getDiscount());
				pendingIssuesSave.setTaxAmount(pendingIssuesSave.getTaxAmount()-pendingIssuesSave.getItems().get(integer).getTaxAmount());
				pendingIssuesSave.getItems().remove(integer.intValue());
				count++;
			}
		Double outStandingAmount = pendingIssuesSave.getNetAmount();
		Double paidAmount = 0.0;
		Double totalConcession = 0.0;
		Date date = TimeZoneComponent.getDateByzone(pendingIssuesSave.getUnitId());
		List<SaleDetail> responseSaleDtlsList = new ArrayList<>();
		Sale sale = new Sale(
				(pendingIssuesSave.getOrderId() != null) ? new Order(pendingIssuesSave.getOrderId()) : null,
				pendingIssuesSave.getDiscount(),
				(pendingIssuesSave.getDiscountCategoryId() != null)
						? new DiscountCategory(pendingIssuesSave.getDiscountCategoryId()) : null,
				(pendingIssuesSave.getDiscountTypeId() != null)
						? new DiscountType(pendingIssuesSave.getDiscountTypeId()) : null,
				(pendingIssuesSave.getDoctorId() != null) ? new Doctor(pendingIssuesSave.getDoctorId()) : null,
				(pendingIssuesSave.getNetAmount() != null) ? pendingIssuesSave.getNetAmount() : null,
				(pendingIssuesSave.getNetAmount() != null) ? outStandingAmount : null, paidAmount,
				(pendingIssuesSave.getPatientId() != null) ? new PatientRegistration(pendingIssuesSave.getPatientId())
						: null,
				(pendingIssuesSave.getRoundOff() != null) ? pendingIssuesSave.getRoundOff() : null,
				new Store(pendingIssuesSave.getStoreId()),
				(pendingIssuesSave.getTaxAmount() != null) ? pendingIssuesSave.getTaxAmount() : null,
				(pendingIssuesSave.getTotalAmount() != null) ? pendingIssuesSave.getTotalAmount() : null,
				pendingIssuesSave.getTotalDiscountAmount(),
				(pendingIssuesSave.getVisitTypeId() != null) ? new VisitType(pendingIssuesSave.getVisitTypeId()) : null,
				(pendingIssuesSave.getSaleTypeId() != null) ? new SaleType(pendingIssuesSave.getSaleTypeId()) : null,
				(pendingIssuesSave.getPrescriptionId() != null)
						? new Prescription(new Long(pendingIssuesSave.getPrescriptionId())) : null,
				pendingIssuesSave.getTotalSelfPayable(), pendingIssuesSave.getTotalCreditPayable(),
                pendingIssuesSave.getRemark());
//		sale.setConsolidatedBill(pendingIssuesSave.getIsConsolidatedFlag());
		sale.setCop(pendingIssuesSave.getAtCopToSave());
		sale.setFromPendingIssue(pendingIssuesSave.isFromPendingIssue());
		sale.setFromNarcotics(pendingIssuesSave.isFromNarcotics());
		sale.setScreenId(pendingIssuesSave.isFromNarcotics() ? Screen.IssueWorklist.getScreen() : Screen.PendingIssues.getScreen());
		sale.setFromBillableConsumption(false);
		String billNumber = GlobalService.getBillNoByGeneratedCode(pendingIssuesSave.getStoreId(), pendingIssuesSave.getUnitId(),
				sale.getScreenId());
		
		sale.setBillNo(billNumber);
		
		sale.setCreateDefaults(
				pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId() : sessionService.getUnitId(),
				pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId() : sessionService.getUserId());
		if (pendingIssuesSave.getAdmissionId() != null) {
			sale.setAdmission(new Admission(pendingIssuesSave.getAdmissionId()));
		} else if (pendingIssuesSave.getEncounterId() != null) {
			sale.setEncounter(new Encounter(pendingIssuesSave.getEncounterId()));
		}
		if (pendingIssuesSave.getPrescriptionId() != null) {
			sale.setPrescription(new Prescription(new Long(pendingIssuesSave.getPrescriptionId())));
		}
		sale.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
		boolean isPreAuthExsist = iContractConsumptionDao.getPreAuthAmutByVisitId(pendingIssuesSave.getEncounterId(),
				pendingIssuesSave.getAdmissionId());
		int parentItemId = 0;
		for (ItemRequest item : pendingIssuesSave.getItems()) 
		{

			Integer storeId = pendingIssuesSave.getStoreId();
			Integer unitId = pendingIssuesSave.getUnitId();
			Long prescriptionDetailsId = item.getPrescriptionDetailId() != null
					? new Long(item.getPrescriptionDetailId()) : null;

			if (item.getPrescriptionDetailId() != null) {

				parentItemId = item.getItemId();

			}
			// for Substitute
			if (item.getPrescriptionDetailId() == null  && item.getParentItemId()!=null && item.getParentItemId()>0) 
			{
				// For prescription flow for substitute

				parentItemId = item.getParentItemId();
				PrescriptionDetailsDto prescriptionDtl = assignPickerDao.getPrescriptionDtlsByPrescriptionId(
						pendingIssuesSave.getPrescriptionId().longValue(), item.getParentItemId());
				PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
				prescriptionDetail.setAuthorisedBy(
						prescriptionDtl.getAuthorisedBy() != null ? prescriptionDtl.getAuthorisedBy() : null);
				prescriptionDetail.setAuthorisedDate(prescriptionDtl.getAuthorisedDate());
				prescriptionDetail.setPendingQty(prescriptionDtl.getPendingQty());
				prescriptionDetail.setOrderQty(prescriptionDtl.getOrderQty());
				prescriptionDetail.setPrescriptionDate(prescriptionDtl.getPrescriptionDate());
				if (prescriptionDtl.getInstructionId() != null) {
					Instruction ins = new Instruction();
					ins.setId(prescriptionDtl.getInstructionId());
					prescriptionDetail.setInstruction(ins);
				}
				if (prescriptionDtl.getInstructionDesc() != null) {
					prescriptionDetail.setInstructionDesc(prescriptionDtl.getInstructionDesc());
				}
				Store store = new Store();
				store.setStoreId(pendingIssuesSave.getStoreId());
				prescriptionDetail.setStore(store);
				prescriptionDetail.setInfusionSolutionId(prescriptionDtl.getInfusionSolutionId());
				prescriptionDetail.setIsDiscontinued(false);
				prescriptionDetail.setIsIntervention(false);
				prescriptionDetail.setIsPreStop(false);
				prescriptionDetail.setStatFlag(false);
				prescriptionDetail.setIsDeleted(false);
				prescriptionDetail.setIsActive(true);
				prescriptionDetail.setIsIntervention(false);
				prescriptionDetail.setPrnFlag(false);
				prescriptionDetail.setStrength(prescriptionDtl.getStrength());
				prescriptionDetail.setAddedDate(TimeZoneComponent.getDateByzone(unitId));
				prescriptionDetail.setIsMedicationStop('N');
				// prescriptionDetail.setInfusionFlag(prescriptionDtl.getInfusionflag());
				Item itm = new Item();
				itm.setId(item.getItemId());
				prescriptionDetail.setItem(itm);
				prescriptionDetail.setParentSubstituteId(prescriptionDtl.getPrescriptionDetailsid());
				Prescription pres = new Prescription();
				pres.setId(prescriptionDtl.getPrescriptionId().longValue());
				prescriptionDetail.setPrescription(pres);

				// sessionService.setEntityDefaults(prescriptionDetail);
				prescriptionDetail.setCreateDefaults(
						pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
								: sessionService.getOrganizationId(),
						pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId()
								: sessionService.getUnitId(),
						pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId()
								: sessionService.getUserId());

				GlobalDao.savePrescriptionDetailForSubstitute(prescriptionDetail);

				PrescriptionFrequencyDetail pfd = new PrescriptionFrequencyDetail();
				pfd.setPrescriptionDetail(prescriptionDetail);
				if (prescriptionDtl.getRouteId() != null) {
					RouteOfAdministration roa = new RouteOfAdministration();
					roa.setId(prescriptionDtl.getRouteId());
					pfd.setRouteOfAdministration(roa);
				}
				pfd.setStartDate(prescriptionDtl.getStartDate());
				pfd.setStopDate(prescriptionDtl.getStopDate());
				StrengthUnit strnUnit = new StrengthUnit();
				if (prescriptionDtl.getDoseUnitId() != null) {
					strnUnit.setId(prescriptionDtl.getDoseUnitId());
					pfd.setStrengthUnit(strnUnit);
				}
				pfd.setDose(prescriptionDtl.getDose());
				Frequency freq = new Frequency();
				freq.setId(prescriptionDtl.getFrequencyId());
				pfd.setFrequency(freq);
				pfd.setIsActive(true);
				pfd.setIsDeleted(false);
				StrengthUnit strngth = new StrengthUnit();
				if (prescriptionDtl.getDoseUnitId() != null) {
					strngth.setId(prescriptionDtl.getDoseUnitId());
					pfd.setStrengthUnit(strngth);
				}

				// sessionService.setEntityDefaults(pfd);
				pfd.setCreateDefaults(
						pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
								: sessionService.getOrganizationId(),
						pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId()
								: sessionService.getUnitId(),
						pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId()
								: sessionService.getUserId());

				GlobalDao.savePrescriptionFrequencyDetail(pfd);

				prescriptionDetailsId = prescriptionDetail.getId().longValue();
				
				item.setPrescriptionDetailId(prescriptionDetailsId.intValue());
				item.setId(null);
			}

			if (item.getPrescriptionDetailId() == null && item.getParentIssueDetailsId() == null && item.getParentItemId() == null) 
			{
				Batch batch = GlobalDao.getBatch(pendingIssuesSave.getStoreId(), item.getBatchId(),
						pendingIssuesSave.getUnitId());

				/*PrescriptionDetailsDto prescriptionDtl = assignPickerDao.getPrescriptionDtlsByPrescriptionId(
						pendingIssuesSave.getPrescriptionId().longValue(), pendingIssuesSave.getIte);*/
				PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
				prescriptionDetail.setOrderQty(0);
				Store store = new Store();
					store.setStoreId(pendingIssuesSave.getStoreId());
				prescriptionDetail.setStore(store);
				prescriptionDetail.setIsDiscontinued(false);
				prescriptionDetail.setIsIntervention(false);
				prescriptionDetail.setIsPreStop(false);
				prescriptionDetail.setStatFlag(false);
				prescriptionDetail.setIsDeleted(false);
				prescriptionDetail.setIsActive(true);
				prescriptionDetail.setIsIntervention(false);
				prescriptionDetail.setPrnFlag(false);
				prescriptionDetail.setInfusionFlag(false);
				prescriptionDetail.setIsDischarge(false);
				prescriptionDetail.setRevisedOrderedQuantity(0);
				prescriptionDetail.setIsMedicationStop('N');
				prescriptionDetail.setAddedDate(TimeZoneComponent.getDateByzone(unitId));
				Item itm = new Item();
					itm.setId(item.getItemId());
				prescriptionDetail.setItem(itm);
				Prescription pres = new Prescription();
					pres.setId(pendingIssuesSave.getPrescriptionId().longValue());
				prescriptionDetail.setPrescription(pres);
				prescriptionDetail.setIsManuallyAddedItem(true);
				prescriptionDetail.setParentSubstituteId(null);
				 prescriptionDetail.setIsMedicationStop('N');
				prescriptionDetail.setCreateDefaults(
						pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
								: sessionService.getOrganizationId(),
						pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId()
								: sessionService.getUnitId(),
						pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId()
								: sessionService.getUserId());

				GlobalDao.savePrescriptionDetailForSubstitute(prescriptionDetail);

				item.setPrescriptionDetailId(prescriptionDetail.getId().intValue());
				item.setId(null);
				prescriptionDetailsId = prescriptionDetail.getId().longValue();
			}

			SaleDetail saleDetail = new SaleDetail(new Batch(item.getBatchId()), item.getDiscountAmount(),
					item.getLeaseQuantity(), item.getNetAmount(), null,
                    new PrescriptionDetail(item.getPrescriptionDetailId()!=null ? item.getPrescriptionDetailId():prescriptionDetailsId),
					item.getQuantity(), item.getRate(), item.getTaxAmount(), new Tax(item.getTaxId()),
					item.getTaxPercentage(), item.getTotalAmount(), item.getUnitDiscountAmount(),
					new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
					new Status(com.param.common.constants.Status.Completed.getStatus()), item.getSelfPayable(),
					item.getCreditPayable(), item.getPayeeId(), item.getProductCategoryId(),
					item.getPackageConsumptionAmt());
			saleDetail.setConsessionAmount(item.getConsessionAmount());
			saleDetail.setConsessionPercentage(item.getConsessionPercentage());
			saleDetail.setAtCop(pendingIssuesSave.getAtCopToSave());
			totalConcession = totalConcession + item.getConsessionAmount();
			
			saleDetail.setConsumedItemId(item.getConsumedItemId());
			saleDetail.setConsumedPcategroyId(item.getConsumedPcategroyId());
			saleDetail.setItemId(item.getItemId());
			// sessionService.setEntityDefaults(saleDetail);
			saleDetail.setCreateDefaults(
					pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
							: sessionService.getOrganizationId(),
					pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId() : sessionService.getUnitId(),
					pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId() : sessionService.getUserId());

			// For package
			List<SaleDetailPackageMapperDto> packageMppr = item.getSaleDetailPackageMapperDtoList();
			if (packageMppr != null && packageMppr.size() > 0) {
				List<SaleDetailPackageMapper> pkgMpprList = new ArrayList<SaleDetailPackageMapper>();
				for (SaleDetailPackageMapperDto dto : packageMppr) {
					SaleDetailPackageMapper mpper = new SaleDetailPackageMapper();
					mpper.setOrganizationId(pendingIssuesSave.getOrganizationId());
					mpper.setUnitId(pendingIssuesSave.getUnitId());
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
					mpper.setCapOrQty(dto.getCapOrQty());
					mpper.setRate(BigDecimal.valueOf(dto.getRate()));
					mpper.setSaleDetail(saleDetail);
					pkgMpprList.add(mpper);
				}
				saleDetail.setSaleDetailPackageMapperList(pkgMpprList);
			}

			// For Contract
			List<SaleDetailPayeeMapperDto> payeeMapper = item.getSaleDetailPayeeMapperDtoList();
			if (payeeMapper != null && payeeMapper.size() > 0) {
				List<SaleDetailPayeeMapper> payeeList = new ArrayList<SaleDetailPayeeMapper>();
				for (SaleDetailPayeeMapperDto dto : payeeMapper) {
					SaleDetailPayeeMapper mpper = new SaleDetailPayeeMapper();
					mpper.setOrganizationId(pendingIssuesSave.getOrganizationId());
					mpper.setUnitId(pendingIssuesSave.getUnitId());
					mpper.setItemId(item.getItemId());
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
			// sessionService.setEntityDefaults(saleDetail);
			saleDetail.setCreateDefaults(
					pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
							: sessionService.getOrganizationId(),
					pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId() : sessionService.getUnitId(),
					pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId() : sessionService.getUserId());

			sale.addSaleDetailsList(saleDetail);
			// actual cap consumption
			item.setEncounterId(pendingIssuesSave.getEncounterId());
			item.setAdmissionId(pendingIssuesSave.getAdmissionId());
			item.setPatientId(pendingIssuesSave.getPatientId());
			item.setPreAuthExist(isPreAuthExsist);
			iWalkInSalesService.consumePackageContractItems(item);
			
			responseSaleDtlsList.add(saleDetail);
		}

		// sessionService.setEntityDefaults(sale);
		sale.setConcession(totalConcession);
		sale.setCreateDefaults(
				pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId() : sessionService.getUnitId(),
				pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId() : sessionService.getUserId());

		sale.setIssueId(pendingIssuesSave.getId());
		
		iWalkInSalesService.saveGlobalSale(sale);
		saveIssue(pendingIssuesSave, sale);
		/*
		 * WalkInSalesService.deleteAllocation(Screen.PendingIssues.getScreen(),
		 * Long.valueOf(pendingIssuesSave.getId()), null);
		 */
		CommonResponse response = new CommonResponse();
		response.setId(sale.getId().longValue());
		response.setListObject(responseSaleDtlsList);
		response.setMessage("Pending Issues saved successfully");
		if (isAcceptance.equals(true)) {
			response.setObject(pendingIssuesSave);
			// this.savePatientIssueAcceptance(pendingIssuesSave);
		}

		return response;

	}

	@Override
	@Transactional
	public Object savePatientIssueAcceptance(PendingIssuesSave pendingIssuesSave) {
		Issue issue = pendingIssues.getIssue(pendingIssuesSave.getId());

		PendingIssuesAcceptanceSave PendingIssuesAcceptanceSave = new PendingIssuesAcceptanceSave();
		PendingIssuesAcceptanceSave.setId(issue.getId());
		PendingIssuesAcceptanceSave.setStoreId(issue.getStore().getStoreId());
		PendingIssuesAcceptanceSave.setStatusId(8);
		PendingIssuesAcceptanceSave.setOrganizationId(pendingIssuesSave.getOrganizationId());
		PendingIssuesAcceptanceSave.setUnitId(pendingIssuesSave.getUnitId());
		PendingIssuesAcceptanceSave.setUserId(pendingIssuesSave.getUserId());
		List<PendingIssuesAcceptanceSaveItems> items = new ArrayList<>();
		for (IssueDetail id : issue.getIssueDetail()) {
			for (IssueDetailBatchMapper bm : id.getIssueDetailBatchMapper()) {
				if (bm.getAcceptedQuantity() == null) {
					PendingIssuesAcceptanceSaveItems pendingIssuesAcceptanceSaveItems = new PendingIssuesAcceptanceSaveItems();
					pendingIssuesAcceptanceSaveItems.setBatchMapperId(bm.getId());
					pendingIssuesAcceptanceSaveItems.setAcceptQuantity(id.getIssueQuantity());
					pendingIssuesAcceptanceSaveItems.setRejectQuantity(0);
					items.add(pendingIssuesAcceptanceSaveItems);
				}

			}
			PendingIssuesAcceptanceSave.setItems(items);
		}
		pendingIssuesAcceptanceMedicationService.pendingIssuesAcceptanceMedicationSave(PendingIssuesAcceptanceSave);
		CommonResponse response = new CommonResponse();
		// response.setId(issueResponse.getId().longValue());
		response.setMessage("Patient Issue saved successfully");
		return response;

	}

	private void updateStoreTransitStock(Integer storeId, Long batchId, Integer quantity, UomType uomType,
			Integer requestStoreId) {

		if (quantity > 0) {
			try {
				Batch batch = GlobalDao.getBatch(batchId);
				if (batch != null) {
					// Integer leaseQuantity = Util.GetLeaseQuantity(quantity,
					// batch.getBatchUomList(), uomType);

					StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(batchId, requestStoreId, null);
					StoreStock storeStockDetail = null;
					if (storeStockList != null) {
						// tQuantity = leaseQuantity + storeStockList.gettQty();
						// availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
						// storeStockList.setAvailableQty(availableQuantity);
						Integer qty = 0;
						if (storeStockList.gettQty() != null) {
							qty = storeStockList.gettQty();
						}
						storeStockList.settQty(qty + quantity);
						// storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
					}

				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
		}

	}

	private void saveIssue(PendingIssuesSave pendingIssuesSave, Sale sale) throws Exception {
		Issue issue = pendingIssues.getIssue(pendingIssuesSave.getId());
		issue.setSale(sale);
		Store str = new Store();
		str.setStoreId(pendingIssuesSave.getStoreId());
		issue.setStore(str);
		issue.setRemark((pendingIssuesSave.getRemark() != null) ? pendingIssuesSave.getRemark() : null);

		AssignPicker assignPicker = issue.getAssignPicker();

		List<IssueDetail> issueDetails = issue.getIssueDetail();

		// Get all the issued quantity list

		List<Long> prescriptionDetailId = issueDetails.stream()
				.map(issueDetail -> (issueDetail.getPrescriptionDetail() != null
						? issueDetail.getPrescriptionDetail().getId() : null))
				.collect(Collectors.toList());

		List<PharmacologyItem> quantityDetailsList = new ArrayList<PharmacologyItem>();
		if (prescriptionDetailId != null) {
			quantityDetailsList = pendingIssues.getQuantityDetails(prescriptionDetailId);

		}

		if (pendingIssuesSave.getStatusId() != null) {
			issue.setStatus(new Status(pendingIssuesSave.getStatusId()));
		}

		// added by Ganesh 22-02-22
		Map<Integer, Boolean> issueStuatusUpdate = new HashMap<>();

		List<IssueDetail> newItemList = new ArrayList<>();
		Integer infusionQty=0;
		for (ItemRequest item : pendingIssuesSave.getItems()) {
			if(item.getInfusionSolutionId() != null) {
				infusionQty +=item.getQuantity();
			}
			
		}
		
		for (ItemRequest item : pendingIssuesSave.getItems()) {
			if (item.getId() == null) {
				IssueDetail newItem = new IssueDetail();

				if (item.getStatusId() != null) {
					newItem.setStatus(new Status(item.getStatusId()));
				}

				if (item.getParentIssueDetailsId() != null && item.getParentIssueDetailsId() > 0) 
				{
					IssueDetailsDto issueDtl = GlobalDao.getIssueDtlsByPrescriptionId(item.getParentIssueDetailsId());

					Issue issueObj = new Issue();
					issueObj.setId(issueDtl.getIssueId());
					newItem.setIssue(issueObj);

					if (issueDtl.getAssignPickerDetailId() != null) {
						AssignPickerDetail assign = new AssignPickerDetail();
						assign.setId(issueDtl.getAssignPickerDetailId());
						newItem.setIsFromAssignPicker(true);
						newItem.setAssignPickerDetail(assign);
					} else {
						newItem.setAssignPickerDetail(null);
						newItem.setParentIssueDetailId(issueDtl.getIssueDetailId());
						newItem.setIsFromAssignPicker(false);
					}

				} else {
					newItem.setIssue(new Issue(issue.getId()));
					newItem.setIsFromAssignPicker(issueDetails.size() > 0
							? (issueDetails.get(0).getAssignPickerDetail() != null ? true : false) : false);
				}

				Batch batchObj = GlobalDao.getBatch(item.getBatchId());
				if (batchObj != null) {
					Integer leaseQuantity = Util.GetLeaseQuantity(item.getQuantity(), batchObj.getBatchUomList(),
							new UomType(item.getUomTypeId()));

					Double amount = (leaseQuantity * batchObj.getSellingPrice());
					IssueDetailBatchMapper issueDetailBatchMapper = new IssueDetailBatchMapper(batchObj,
							new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()), leaseQuantity, amount);
					newItem.setIssueQuantity(infusionQty!=0 ? infusionQty :item.getQuantity());
					issueDetailBatchMapper.setCreateDefaults(
							pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
									: sessionService.getOrganizationId(),
							pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId()
									: sessionService.getUnitId(),
							pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId()
									: sessionService.getUserId());
					newItem.addBatch(issueDetailBatchMapper);
				}

				newItem.setUpdatedByUser(pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId() : sessionService.getUserId());
				newItem.setUpdatedDate(TimeZoneComponent.getDateByzone(pendingIssuesSave.getUnitId()));
				newItem.setTaxAmount(item.getTaxAmount());
				newItem.setNetAmount(item.getNetAmount());
				newItem.setCreditPayable(item.getCreditPayable());
				newItem.setDiscountAmount(item.getDiscountAmount());
				newItem.setPackageConsumptionAmt(item.getPackageConsumptionAmt());
				newItem.setTaxPercentage(item.getTaxPercentage());
				newItem.setUnitConsessionAmount(item.getUnitConsessionAmount());
				newItem.setUnitDiscountAmount(item.getUnitDiscountAmount());
				newItem.setConsessionAmount(item.getConsessionAmount());
				newItem.setConsessionPercentage(item.getConsessionPercentage());
				newItem.setDiscountAmount(item.getDiscountAmount());

				Long saleDtltsId = sale.getSaleDetailsList().stream()
						.filter(saleDetail -> saleDetail.getItemId().equals(item.getItemId())
								&& saleDetail.getBatch().getId().equals(item.getBatchId())
								&& saleDetail.getUomType().getId().equals(item.getUomTypeId())
								&& item.getPrescriptionDetailId()
										.equals(saleDetail.getPrescriptionDetail().getId().intValue()))
						.map(obj -> obj.getId()).findAny().orElse(null);

				newItem.setSaleDetail(new SaleDetail(saleDtltsId));
				newItem.setPrescriptionDetail(item.getPrescriptionDetailId() != null ? new PrescriptionDetail(item.getPrescriptionDetailId().longValue()) : null);
				newItem.setBatch(new Batch(item.getBatchId()));
				newItem.setUomType(new UomType(item.getUomTypeId()));
				newItem.setUomUnit(new UomUnit(item.getUomUnitId()));
				newItem.setRate(item.getRate());
				newItem.setTotalAmount(item.getTotalAmount());
				newItem.setIsNarcoticSubstitute(pendingIssuesSave.isFromNarcotics());
				newItem.setSelfPayable(item.getSelfPayable());
				newItem.setIsSubstitute(item.getParentIssueDetailsId()!=null ? true : false);
				newItem.setCreateDefaults(
						pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
								: sessionService.getOrganizationId(),
						pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId()
								: sessionService.getUnitId(),
						pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId()
								: sessionService.getUserId());
				newItemList.add(newItem);
			} else {
				for (IssueDetail issueDetail : issueDetails) 
				{
					if (item.getPrescriptionDetailId() != null) 
					{
						if (item.getId() != null && item.getId().equals(issueDetail.getId())) 
						{
							Long saleDtltsId = sale.getSaleDetailsList().stream()
									.filter(saleDetail -> saleDetail.getItemId().equals(item.getItemId())
											&& saleDetail.getBatch().getId().equals(item.getBatchId())
											&& item.getPrescriptionDetailId()
													.equals(saleDetail.getPrescriptionDetail().getId().intValue()))
									.map(obj -> obj.getId()).findAny().orElse(null);

							issueDetail.setSaleDetail(new SaleDetail(saleDtltsId));

							if (item.getStatusId() != null) 
							{
								issueDetail.setStatus(new Status(item.getStatusId()));
							}

							Batch batchObj = GlobalDao.getBatch(item.getBatchId());
							if (batchObj != null) 
							{
								Integer leaseQuantity = Util.GetLeaseQuantity(item.getQuantity(),
										batchObj.getBatchUomList(), new UomType(item.getUomTypeId()));

								Double amount = (leaseQuantity * batchObj.getSellingPrice());
								IssueDetailBatchMapper issueDetailBatchMapper = new IssueDetailBatchMapper(batchObj,
										new UomType(item.getUomTypeId()), new UomUnit(item.getUomUnitId()),
										leaseQuantity, amount);
								issueDetail.setIssueQuantity(infusionQty!=0 ? infusionQty :item.getQuantity());
								issueDetailBatchMapper.setCreateDefaults(pendingIssuesSave.getOrganizationId() != null
										? pendingIssuesSave.getOrganizationId() : sessionService.getOrganizationId(),
										pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId()
												: sessionService.getUnitId(),
										pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId()
												: sessionService.getUserId());
								issueDetail.addBatch(issueDetailBatchMapper);

								Optional<PharmacologyItem> prescription = null;
								if (quantityDetailsList != null && quantityDetailsList.size() > 0) {
									prescription = quantityDetailsList.stream()
											.filter(qty -> qty.getPrescriptionDetailId()
													.equals(issueDetail.getPrescriptionDetail().getId()))
											.findFirst();
								}

								if (prescription != null && prescription.isPresent()) {
									Long issuedQuantity = item.getQuantity() + prescription.get().getIssuedQuantity();
									prescription.get()
											.setIssuedQuantity(issuedQuantity != null ? issuedQuantity : null);
								}
							}
							issueDetail.setUpdatedByUser(pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId() : sessionService.getUserId());
							issueDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(pendingIssuesSave.getUnitId()));
							issueDetail.setTaxAmount(item.getTaxAmount());
							issueDetail.setNetAmount(item.getNetAmount());
							issueDetail.setCreditPayable(item.getCreditPayable());
							issueDetail.setDiscountAmount(item.getDiscountAmount());
							issueDetail.setPackageConsumptionAmt(item.getPackageConsumptionAmt());
							issueDetail.setTaxPercentage(item.getTaxPercentage());
							issueDetail.setUnitConsessionAmount(item.getUnitConsessionAmount());
							issueDetail.setUnitDiscountAmount(item.getUnitDiscountAmount());
							issueDetail.setConsessionAmount(item.getConsessionAmount());
							issueDetail.setConsessionPercentage(item.getConsessionPercentage());
							issueDetail.setDiscountAmount(item.getDiscountAmount());
							issueDetail.setSelfPayable(item.getSelfPayable());
							issueDetail.setRate(item.getRate());
							issueDetail.setTotalAmount(item.getTotalAmount());
							if (assignPicker != null) {
								issueDetail.setIsFromAssignPicker(true);
							} else {
								issueDetail.setIsFromAssignPicker(false);
							}

							issueDetail.setBatch(new Batch(item.getBatchId()));
							issueDetail.setUomType(new UomType(item.getUomTypeId()));
							issueDetail.setUomUnit(new UomUnit(item.getUomUnitId()));
							issueDetail.setCreateDefaults(pendingIssuesSave.getOrganizationId() != null
									? pendingIssuesSave.getOrganizationId() : sessionService.getOrganizationId(),
									pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId()
											: sessionService.getUnitId(),
									pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId()
											: sessionService.getUserId());
						}
					}
				}

			}
			if (item.getOrderedQuantity() != null && item.getQuantity() != null
					&& item.getOrderedQuantity().equals(item.getQuantity()))
				issueStuatusUpdate.put(issue.getId(), true);
			else
				issueStuatusUpdate.put(issue.getId(), false);

		}

		issueDetails.addAll(newItemList);

		// Updating assign picker status
		issue.setUpdatedByUser(pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId() : sessionService.getUserId());
		issue.setUpdatedDate(TimeZoneComponent.getDateByzone(pendingIssuesSave.getUnitId()));
		Integer com = 4;
		Integer par = 18;
		if (assignPicker != null) {
			Response orderQty = pendingIssues.getOrederQty(issue.getPrescription().getId());
			IssueDetailsDto dto = (IssueDetailsDto) orderQty.getObject();
			if (dto.getStatusId().equals(com)) {

				Response iss = pendingIssues.getOtherIssueDetails(issue.getPrescription().getId(), issue.getId());

				if (iss.getListObject() != null && iss.getListObject().size() > 0) {

					assignPicker.setStatus(new Status(18));

				} else {

					if (issue.getStatus().getId() != null && issue.getStatus().getId().equals(com)) {

						assignPicker.setStatus(new Status(4));

					} else if (issue.getStatus().getId() != null && issue.getStatus().getId().equals(par)) {

						assignPicker.setStatus(new Status(18));
					}
				}

			} else {
				assignPicker.setStatus(new Status(18));
			}
			issue.setAssignPicker(assignPicker);
		}

		if (issue.getId() != null && issueStuatusUpdate.get(issue.getId()))
			issue.setStatus(new com.param.entity.model.master.Status(4));

		pendingIssues.pendingIssuesSave(issue);
	}
	@Transactional
	private void saveStoreStockDetails1(Integer storeId, Long batchId, Integer quantity, UomType uomType,
			Integer unitId, PendingIssuesSave pendingIssuesSave) throws Exception{
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
						// StoreStock storeStock = new StoreStock(leaseQuantity, new
						// Store(sessionService.getStoreId()), new Batch(batchId)); //comment for
						// resolved HIS-9152
						// start code for resolved HIS-9152 Store Stock Dashboard-The Qty which is
						// accepted in Patient Issue Acceptance is getting added to AvailableQty in
						// StoreStockDashboard.
						/*
						 * StoreStock storeStock = new StoreStock(leaseQuantity, new
						 * Store(pendingIssuesSave.getStoreId() != null ? pendingIssuesSave.getStoreId()
						 * : sessionService.getStoreId()), new Batch(batchId));
						 * storeStock.settQty(leaseQuantity); storeStock.setTotalQty(leaseQuantity); //
						 * end code for resolved HIS-9152 Store Stock Dashboard-The Qty which is //
						 * accepted in Patient Issue Acceptance is getting added to AvailableQty in //
						 * StoreStockDashboard. // sessionService.setEntityDefaults(storeStock);
						 * storeStock.setCreateDefaults( pendingIssuesSave.getOrganizationId() != null ?
						 * pendingIssuesSave.getOrganizationId() : sessionService.getOrganizationId(),
						 * pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId() :
						 * sessionService.getUnitId(), pendingIssuesSave.getUserId() != null ?
						 * pendingIssuesSave.getUserId() : sessionService.getUserId()); storeStockDetail
						 * = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
						 */
					} else {
						if( storeStockList.getAvailableQty() < leaseQuantity)
						{
							//throw new CustomException("Stock is not available with batch "+batch.getBatchNo());
							
							throw new CustomException(batch.getBatchNo()!=null ? batch.getBatchNo():"NA");
							
							/*CommonResponse response = new CommonResponse();
							response.setCode(batch.getBatchNo());
							response.setExceptionMessage("Stock is not available with batch "+batch.getBatchNo());
							response.setMessage("Available Stock is "+ storeStockList.getAvailableQty() );*/
						}
						tQuantity = leaseQuantity + storeStockList.gettQty();
						availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
						if (availableQuantity < 0) {
							availableQuantity = 0;
						}
						storeStockList.setAvailableQty(availableQuantity);

						Integer qty = 0;
						if (storeStockList.gettQty() != null) {
							qty = storeStockList.gettQty();
						}
						// storeStockList.settQty(qty + quantity); //comment for resolved HIS-9152

						// start code for resolved HIS-9152 Store Stock Dashboard-The Qty which is
						// accepted in Patient Issue Acceptance is getting added to AvailableQty in
						// StoreStockDashboard.
						storeStockList.settQty(qty + leaseQuantity);
						// GlobalDao.updateStoreStockTransitQty(storeStockList);
						// end code for resolved HIS-9152 Store Stock Dashboard-The Qty which is
						// accepted in Patient Issue Acceptance is getting added to AvailableQty in
						// StoreStockDashboard.

						// storeStockList.settQty(tQuantity);
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

	private void saveStoreStockDetails(Integer storeId, Long batchId, Integer quantity, UomType uomType, Integer unitId,
			PendingIssuesSave pendingIssuesSave, IssueDetail issue, Item item) {
		if (quantity > 0) {
			try {
				Batch batch = GlobalDao.getBatch(batchId);
				if (batch != null) {
					Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
					Integer tQuantity = 0;
					Integer availableQuantity = 0;
					Integer currentQuantity = 0;
					StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore1(batchId, storeId, unitId);
					StoreStock storeStockDetail = null;
					if (storeStockList == null) {
						// StoreStock storeStock = new StoreStock(leaseQuantity, new
						// Store(sessionService.getStoreId()), new Batch(batchId)); //comment for
						// resolved HIS-9152
						// start code for resolved HIS-9152 Store Stock Dashboard-The Qty which is
						// accepted in Patient Issue Acceptance is getting added to AvailableQty in
						// StoreStockDashboard.
						/*
						 * StoreStock storeStock = new StoreStock(leaseQuantity, new
						 * Store(pendingIssuesSave.getStoreId() != null ? pendingIssuesSave.getStoreId()
						 * : sessionService.getStoreId()), new Batch(batchId));
						 * storeStock.settQty(leaseQuantity); storeStock.setTotalQty(leaseQuantity); //
						 * end code for resolved HIS-9152 Store Stock Dashboard-The Qty which is //
						 * accepted in Patient Issue Acceptance is getting added to AvailableQty in //
						 * StoreStockDashboard. // sessionService.setEntityDefaults(storeStock);
						 * storeStock.setCreateDefaults( pendingIssuesSave.getOrganizationId() != null ?
						 * pendingIssuesSave.getOrganizationId() : sessionService.getOrganizationId(),
						 * pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId() :
						 * sessionService.getUnitId(), pendingIssuesSave.getUserId() != null ?
						 * pendingIssuesSave.getUserId() : sessionService.getUserId()); storeStockDetail
						 * = GlobalDao.saveStoreStockLeaseQuantity(storeStock);
						 */
					} else {
						tQuantity = leaseQuantity + storeStockList.gettQty();
						availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
						if (availableQuantity < 0) {
							availableQuantity = 0;
						}
						storeStockList.setAvailableQty(availableQuantity);

						Integer qty = 0;
						if (storeStockList.gettQty() != null) {
							qty = storeStockList.gettQty();
						}
						// storeStockList.settQty(qty + quantity); //comment for resolved HIS-9152

						// start code for resolved HIS-9152 Store Stock Dashboard-The Qty which is
						// accepted in Patient Issue Acceptance is getting added to AvailableQty in
						// StoreStockDashboard.
						storeStockList.settQty(qty + leaseQuantity);
						// GlobalDao.updateStoreStockTransitQty(storeStockList);
						// end code for resolved HIS-9152 Store Stock Dashboard-The Qty which is
						// accepted in Patient Issue Acceptance is getting added to AvailableQty in
						// StoreStockDashboard.

						// storeStockList.settQty(tQuantity);
						storeStockDetail = GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
					}
					Batch batchObject = GlobalDao
							.getBatch(pendingIssuesSave.getStoreId() != null ? pendingIssuesSave.getStoreId()
									: sessionService.getStoreId(), storeStockDetail.getBatch().getId(), unitId);
					currentQuantity = batchObject.getCurrentQuantity() - leaseQuantity;
					batchObject.setCurrentQuantity(currentQuantity);
					GlobalDao.updateBatchLeaseQuantity(batchObject);
					saveStockTransaction(leaseQuantity, storeStockDetail, pendingIssuesSave, issue, item);
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
		}
	}

	@Transactional
	private Boolean revertAllocatedItemQuantityScheduled(BatchAllocation batchAllocation, Integer screenId) {
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

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock, PendingIssuesSave pendingIssuesSave,
			IssueDetail issue, Item item) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), new Store(sessionService.getStoreId()),
				new Batch(storeStock.getBatch().getId()), issue.getId());
		stockTransaction.setIsTransit(true);
		stockTransaction.setTransitNo(issue.getIssue().getIssueNo());
		stockTransaction.setItem(item);
		stockTransaction.setIssue(issue);
		// stockTransaction.setPatientId(issue.getIssue().getPrescription().getPatient().getPatientId());
		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(
				pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId() : sessionService.getUnitId(),
				pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId() : sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);
	}

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock,
			PendingIssuesSave pendingIssuesSave) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), new Store(sessionService.getStoreId()),
				new Batch(storeStock.getBatch().getId()), 0);
		stockTransaction.setIsTransit(true);
		stockTransaction.setItem(storeStock.getBatch().getItem());
		// sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(
				pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId() : sessionService.getUnitId(),
				pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId() : sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);
	}

	@Override
	@Transactional
	public List<DrugItems> getDrugItems(PharmacologyValidationSearch request) {
		List<DrugItems> response =  pendingIssues.getDrugItems(request);
		List<Long> salesIds = response.stream()
				.map(item -> item.getSalesId().longValue())  // Convert BigInteger to Long
				.collect(Collectors.toList());
		List<ReturnDetails> returnDetailsList = pendingIssues.getReturnDetails(salesIds);

		for (DrugItems items : response) {

			List<ReturnDetails> returnDtoList = new ArrayList<>();

			if(returnDetailsList!= null){
				for (ReturnDetails dto : returnDetailsList) {
					if(dto.getSaleId().equals(items.getSalesId().longValue())){
						returnDtoList.add(dto);
					}
				}
			}

			items.setReturnDetails(returnDtoList);

		}

		return response;
	}

	@Override
	public void cancelPendingIssues(CancelRequest cancelReq) throws CustomException {

		Issue issue = pendingIssues.getIssue(cancelReq.getId());

		/*
		 * AssignPicker picker = issue.getAssignPicker(); Prescription prescription =
		 * picker.getPrescription();
		 * 
		 * List<Issue> issues =
		 * pendingIssues.getIssuesOfPicker(issue.getAssignPicker().getId()); if(issues
		 * != null && !issues.isEmpty()) picker.setStatus(new
		 * Status(com.param.common.constants.Status.Cancelled.getStatus())); else
		 * picker.setStatus(new
		 * Status(com.param.common.constants.Status.PartiallyCancelled.getStatus()));
		 * 
		 * issue.setStatus(new
		 * Status(com.param.common.constants.Status.Cancelled.getStatus()));
		 * prescription.setStatus(new
		 * Status(com.param.common.constants.Status.Cancelled.getStatus()));
		 */

		// Flow has been changed.
		// New flow(11-12-19 - By Divyesh) - When we cancel the issue. quantity of that
		// assign picker should be
		// back into that details and should not be freezed, as user should be able to
		// issue again with canceled quantity.
		// Partially completed and completed issues will never get canceled.

		if (issue.getStatus().getId().equals(com.param.common.constants.Status.Pending.getStatus())) {
			List<IssueDetail> issueDetails = issue.getIssueDetail();
			for (IssueDetail issueDetail : issueDetails) {
				issueDetail.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
				AssignPickerDetail assignPickerDetail = issueDetail.getAssignPickerDetail();
				assignPickerDetail
						.setIssueQuantity(assignPickerDetail.getIssueQuantity() - issueDetail.getIssueQuantity());
			}
		} else {
			throw new CustomException("Only pending issues can be cancelled");
		}
	}

	@Override
	@Transactional
	public Response cancelPendingIssuesRequest(CancelRequest cancelReq) throws CustomException {
		Prescription prescription = null;
		Issue issue = null;
		Integer prescriptionId = 0;
		if (cancelReq.getId() != null && cancelReq.getId() != 0) {
			issue = pendingIssues.pendingIssuesById(cancelReq.getId());
		}
		if (issue != null) {
			// if(issue.getPrescription()!=null && issue.getPrescription().getId()!=null &&
			// issue.getPrescription().getId()!=0) {
			prescriptionId = issue.getPrescription().getId().intValue();
			prescription = salesWorkListDao.getSalesWorkList(prescriptionId);
			// }
			if (prescription == null) {
				throw new CustomException("Prescription not found in DB");
			} else {
				prescription.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
				prescription.setCancelNote(cancelReq.getCancelNote());
				if (prescription.getPrescriptionDetailsList().size() > 0) {
					for (PrescriptionDetail preDtls : prescription.getPrescriptionDetailsList()) {
						preDtls.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
					}
				}

				salesWorkListDao.updateSalesWorkList(prescription);

				if (cancelReq.getIsFromAssignpicker() != null && cancelReq.getIsFromAssignpicker()) {
					AssignPicker assignPicker = assignPickerDao.getAssignPickerList(prescriptionId);
					if (assignPicker != null) {
						assignPicker.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
						// assignPicker.setCancelNote(cancelRequest.getCancelNote());
						if (assignPicker.getAssignPickerDetails().size() > 0) {
							for (AssignPickerDetail dtls : assignPicker.getAssignPickerDetails()) {
								dtls.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
							}
						}
						assignPickerDao.updateassignPicker(assignPicker);
					}
				}
				issue.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
				if (issue.getIssueDetail().size() > 0) {
					for (IssueDetail issueDtls : issue.getIssueDetail()) {
						issueDtls.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
					}
				}
				pendingIssues.pendingIssuesUpdate(issue);

			}
		}
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, null);
	}

	@Override
	public Response updateIssueDetailForSaledetailId(Integer prescriptionDtlsId,Integer batchId) throws CustomException {
		Response res = pendingIssues.updateIssueDetailForSaledetailId(prescriptionDtlsId,batchId);
		return res;
	}

	@Override
	@Transactional
	public List<PharmacologyValidationDetails> pendingPreviousIssues(
			PharmacologyValidationSearch pharmacologyValidationSearch) {
		List<PharmacologyValidationDetails> response = new ArrayList<>();

		List<Prescription> pdata = pendingIssues.getPreviousIssues(pharmacologyValidationSearch);

		for (Prescription p : pdata) {

			PharmacologyValidationDetails assignPickerDetailObject = new PharmacologyValidationDetails();

			List<PharmacologyItem> itemList = new ArrayList<>();

			for (PrescriptionDetail pd : p.getPrescriptionDetailsList()) {
				PharmacologyItem item = new PharmacologyItem((long) p.getId(), pd.getItem().getId(),
						pd.getItem().getCode(),

						pd.getItem().getName(), pd.getItem().getGeneric().getId(), pd.getItem().getGeneric().getName(),
						pd.getIsIntervention(), pd.getOrderQty(), 0, pd.getItem().getManufacturer().getId(),
						pd.getItem().getManufacturer().getName(), null, pd.getItem().getSalesTax().getId(),
						pd.getItem().getSalesTax().getName(), pd.getItem().getSalesTax().getTaxPercentage(), 0L,
						pd.getItem().getIsNarcotic()

				);
				itemList.add(item);
			}

			String doctorDetail = "";
			if (p.getSpecialityMaster() != null && p.getDoctor() != null) {
				doctorDetail = p.getDoctor().getFirstName() + " " + p.getDoctor().getMiddleName() + " "
						+ p.getDoctor().getLastName() + " | " + p.getSpecialityMaster().getSpecialityName();
			}

			String patientDetail = "";
			if (p.getAdmission() != null) {
				String wardName = "";
				String bedNumber = "";

				Integer age = Util
						.getAgeByBirthDate(p.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
				patientDetail = p.getAdmission().getPatient().getPrefix().getPrefixCode() + " "
						+ p.getAdmission().getPatient().getFirstName() + " "
						+ p.getAdmission().getPatient().getMiddleName() + " "
						+ p.getAdmission().getPatient().getLastName() + " " + " | " + age + " | "
						+ p.getAdmission().getPatient().getGender().getGenderCode();

				assignPickerDetailObject = new PharmacologyValidationDetails((long) p.getId(),
						p.getAdmission().getPatient().getPatientId(), patientDetail,
						p.getAdmission().getPatient().getDob(), doctorDetail != "" ? p.getDoctor().getDoctorId() : null,
						doctorDetail, p.getAdmission().getPatient().getUhidNumber(),
						p.getAdmission().getVisitType().getVisitTypeId(), itemList);
				assignPickerDetailObject.setPrescriptionId(p.getId());
				assignPickerDetailObject.setAdmissionId(p.getAdmission().getAdmissionId());
				assignPickerDetailObject.setWardName(!wardName.equals("") ? wardName : null);
				assignPickerDetailObject.setBedNo(!bedNumber.equals("") ? bedNumber : null);
				assignPickerDetailObject.setGenderId(p.getAdmission().getPatient().getGender().getGenderId());
				assignPickerDetailObject.setImagePath(p.getAdmission().getPatient().getImagePath());
			} else if (p.getEncounter() != null) {
				Integer age = Util
						.getAgeByBirthDate(p.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
				patientDetail = p.getEncounter().getPatient().getPrefix().getPrefixCode() + " "
						+ p.getEncounter().getPatient().getFirstName() + " "
						+ p.getEncounter().getPatient().getMiddleName() + " "
						+ p.getEncounter().getPatient().getLastName() + " " + " | " + age + " | "
						+ p.getEncounter().getPatient().getGender().getGenderCode();

				assignPickerDetailObject = new PharmacologyValidationDetails((long) p.getId(),
						p.getEncounter().getPatient().getPatientId(), patientDetail,
						p.getEncounter().getPatient().getDob(), p.getDoctor().getDoctorId(), doctorDetail,
						p.getEncounter().getPatient().getUhidNumber(), p.getEncounter().getVisitType().getVisitTypeId(),
						itemList);
				assignPickerDetailObject.setPrescriptionId(p.getId());
				assignPickerDetailObject.setGenderId(p.getEncounter().getPatient().getGender().getGenderId());
				assignPickerDetailObject.setImagePath(p.getEncounter().getPatient().getImagePath());
				assignPickerDetailObject.setEncounterId(p.getEncounter().getEncounterId());
			}

			response.add(assignPickerDetailObject);
		}
		return response;

	}

	@Override
	public Response pendingIssueStatus(PendingIssuesSave cancelReq, Integer id) {
		
		Integer issueSaveStatus = pendingIssues.getIssueStatus(cancelReq.getId());

		if(issueSaveStatus == com.param.common.constants.Status.Completed.getStatus() || issueSaveStatus == com.param.common.constants.Status.Closed.getStatus()) { 
			
			  return new Response<>(null, null, "This transaction already processed by other user", null, issueSaveStatus);
		  }
		else {
			  
//			  if(issueSaveStatus == com.param.common.constants.Status.PartiallyCompleted.getStatus()) { 
//					
//				  Integer issueSaveStatus1 = pendingIssues.getIssueDetialsStatus(cancelReq.getId(),id);
//				  
//				  if(issueSaveStatus1 == com.param.common.constants.Status.Completed.getStatus() || issueSaveStatus1 == com.param.common.constants.Status.Closed.getStatus()) { 
//					  
//					  return new Response<>(null, null, "This transaction already processed by other user", null, issueSaveStatus1);
//					  
//					}else {
//						
//						  return new Response<>(null, null, null, null, issueSaveStatus1);
//					}
//				  
//			  }else {
//				  
//				  return new Response<>(null, null, null, null, issueSaveStatus); 
//			  }
			return new Response<>(null, null, null, null, issueSaveStatus); 
		  }
		}

	@Override
	public void updateInsertIntoStoreStock(List<SaleDetail> details, Long id, PendingIssuesSave pendingIssuesSave) {
		
		for (ItemRequest item : pendingIssuesSave.getItems()) {
			
			  Integer quantity=item.getQuantity();
	          Long batchId=item.getBatchId();
	          Integer storeId=pendingIssuesSave.getStoreId();
	          UomType uomType= new UomType(item.getUomTypeId());
	          Integer unitId=pendingIssuesSave.getUnitId();
				if (quantity > 0) { 
						Batch batch = GlobalDao.getBatch(batchId);
						if (batch != null) {
							Long saleDetailId = 0l;
							Long salesDetails = 0l;
							for (SaleDetail itemRequest : details) {
								if(itemRequest.getItemId().equals(item.getItemId()) && itemRequest.getQuantity().equals(item.getQuantity())) {
									saleDetailId = itemRequest.getId();
								}
							}
								if(!salesDetails.equals(saleDetailId)) {
									
									Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
									Integer tQuantity = 0;
									Integer availableQuantity = 0;
									Integer currentQuantity = 0;
									StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore1(batchId, storeId, unitId);
											
											tQuantity = leaseQuantity + storeStockList.gettQty();
											availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
											if (availableQuantity < 0) {
												availableQuantity = 0;
											}
											storeStockList.setAvailableQty(availableQuantity);
	
											Integer qty = 0;
											if (storeStockList.gettQty() != null) {
												qty = storeStockList.gettQty();
											}
											storeStockList.settQty(qty + leaseQuantity);
												GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
												currentQuantity = batch.getCurrentQuantity() - leaseQuantity;
												batch.setCurrentQuantity(currentQuantity);
												GlobalDao.updateBatchLeaseQuantity(batch);
											
											saveStockTransaction(leaseQuantity, storeStockList, pendingIssuesSave,saleDetailId,id,item.getItemId());
											
								}
							
								}
						}
				}
				
			}

	private void saveStockTransaction(Integer leaseQuantity, StoreStock storeStock,
			PendingIssuesSave pendingIssuesSave, Long saleDetailId, Long id, Integer itemId) {
		com.param.entity.model.master.Screen screen = GlobalDao.getScreen(screenId);
		StockTransaction stockTransaction = new StockTransaction(storeStock.getAvailableQty(),
				new com.param.entity.model.master.Screen(screen.getId()), screen.getCode(), leaseQuantity,
				storeStock.getBatch().getRate(), new Store(pendingIssuesSave.getStoreId()!=null ? pendingIssuesSave.getStoreId() : sessionService.getStoreId()),
				new Batch(storeStock.getBatch().getId()), 0);
		stockTransaction.setIsTransit(false);
		stockTransaction.setItem(storeStock.getBatch().getItem());
		stockTransaction.setTransactionId(id.intValue());
		stockTransaction.setTransactionDetialsId(saleDetailId.intValue());
		stockTransaction.setOutQty(leaseQuantity);
		stockTransaction.setItem(new Item(itemId));
		stockTransaction.setCreateDefaults(
				pendingIssuesSave.getOrganizationId() != null ? pendingIssuesSave.getOrganizationId()
						: sessionService.getOrganizationId(),
				pendingIssuesSave.getUnitId() != null ? pendingIssuesSave.getUnitId() : sessionService.getUnitId(),
				pendingIssuesSave.getUserId() != null ? pendingIssuesSave.getUserId() : sessionService.getUserId());

		GlobalDao.saveStockTransaction(stockTransaction);
		
	}

}