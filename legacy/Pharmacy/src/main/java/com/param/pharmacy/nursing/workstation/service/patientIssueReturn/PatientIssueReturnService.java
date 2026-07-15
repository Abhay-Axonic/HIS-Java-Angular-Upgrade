package com.param.pharmacy.nursing.workstation.service.patientIssueReturn;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.global.common.ServiceException;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.PatientIssueReturnDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.ReturnReason;
import com.param.global.dto.items.UomResponse;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.nursing.workstation.dao.patientIssueReturn.IPatientIssueReturnDao;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstIssueResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstItemResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnAddItem;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnAddItemResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnDetailItem;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnDetails;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnDropdown;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnSave;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnSaveItem;
import com.param.service.global.SessionService;
@SuppressWarnings({"unused","rawtypes"})
@Service("patientIssueReturnService")
@Transactional
public class PatientIssueReturnService implements IPatientIssueReturnService {

	private static final Logger logger = Logger.getLogger(PatientIssueReturnService.class);

	@Autowired
	private SessionService sessionService;

	@Autowired
	private IPatientIssueReturnDao patientIssueReturnDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	private Integer screenId = Screen.PatientIssueReturn.getScreen();
	private com.param.entity.model.master.Screen screen;

	@Override
	@Transactional
	public PatientIssueReturnDropdown getDropdownList(Integer unitId) {
		PatientIssueReturnDropdown response = new PatientIssueReturnDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		
		for (ScreenStatus screenStatus : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(screenStatus.getStatus().getId(),
			screenStatus.getStatus().getStatus()));
			}

		return response;
	}

	@Override
	@Transactional
	public List<PatientIssueReturnResponse> patientIssueReturnSearch(
			PatientIssueReturnRequest patientIssueReturnRequest) {
		List<PatientIssueReturnResponse> patientIssueReturnList = new ArrayList<PatientIssueReturnResponse>();
		patientIssueReturnRequest.setToDate((new DateTime(patientIssueReturnRequest.getToDate())).plusDays(1).toDate());
		for (PatientIssueReturn patientIssueReturn : patientIssueReturnDao
				.patientIssueReturnSearch(patientIssueReturnRequest, screenId)) {
			PatientIssueReturnResponse patientIssueReturnResponse = null;

			Integer age = Util
					.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " "
					+ patientIssueReturn.getPatient().getFirstName() + " "
					+ patientIssueReturn.getPatient().getMiddleName() + " "
					+ patientIssueReturn.getPatient().getLastName() + " | " + age + " | "
					+ patientIssueReturn.getPatient().getGender().getGenderCode();
			if (patientIssueReturn.getAdmission() != null) {
				patientIssueReturnResponse = new PatientIssueReturnResponse(patientIssueReturn.getId(),
						patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(),
						patientIssueReturn.getPatient().getUhidNumber(), patientDetail,
						patientIssueReturn.getVisitType().getVisitTypeCode(),
						patientIssueReturn.getAdmission().getAdmissionNumber(),
						patientIssueReturn.getStore().getStoreName(), patientIssueReturn.getStatus().getStatus(), patientIssueReturn.getFromMedicineIssue());

				String wardName = "";
				if (patientIssueReturn.getWardMaster() != null) {
					wardName = patientIssueReturn.getWardMaster().getWardName();
				}
				String BedNumber = "";
				if (patientIssueReturn.getBedMaster() != null) {
					BedNumber = patientIssueReturn.getBedMaster().getBedNumber();
				}

				patientIssueReturnResponse.setWardName(wardName);
				patientIssueReturnResponse.setBedNo(BedNumber);

			} else if (patientIssueReturn.getEncounter() != null) {
				patientIssueReturnResponse = new PatientIssueReturnResponse(patientIssueReturn.getId(),
						patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(),
						patientIssueReturn.getPatient().getUhidNumber(), patientDetail,
						patientIssueReturn.getVisitType().getVisitTypeCode(),
						patientIssueReturn.getEncounter().getEncounterNumber(),
						patientIssueReturn.getStore().getStoreName(), patientIssueReturn.getStatus().getStatus(), patientIssueReturn.getFromMedicineIssue());
			}
			if (patientIssueReturnResponse != null) {
				patientIssueReturnList.add(patientIssueReturnResponse);
			}
			/*
			 * for(PatientIssueReturnDetail patientIssueReturnDetail :
			 * patientIssueReturn.getPatientIssueReturnDetail()) {
			 * //System.out.println("PatientIndendID"+patientIssueReturnDetail.
			 * getIssueDetailBatchMapper().getIssueDetail().getIssue().getPatientIndent());
			 * if(patientIssueReturnDetail.getIssueDetailBatchMapper().getIssueDetail().
			 * getIssue().getPatientIndent() == null) { Integer age =
			 * Util.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().
			 * toLocalDateTime().toLocalDate()); String patientDetail =
			 * patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " " +
			 * patientIssueReturn.getPatient().getFirstName() + " " +
			 * patientIssueReturn.getPatient().getMiddleName() + " " +
			 * patientIssueReturn.getPatient().getLastName() + " | " + age + " | " +
			 * patientIssueReturn.getPatient().getGender().getGenderCode();
			 * if(patientIssueReturn.getAdmission() != null) { patientIssueReturnResponse =
			 * new PatientIssueReturnResponse(patientIssueReturn.getId(),
			 * patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(),
			 * patientIssueReturn.getPatient().getUhidNumber(), patientDetail,
			 * patientIssueReturn.getVisitType().getVisitTypeCode(),
			 * patientIssueReturn.getAdmission().getAdmissionNumber(),
			 * patientIssueReturn.getStore().getStoreName(),
			 * patientIssueReturn.getStatus().getStatus());
			 * patientIssueReturnResponse.setWardName(patientIssueReturn.getWardMaster().
			 * getWardName());
			 * patientIssueReturnResponse.setBedNo(patientIssueReturn.getBedMaster().
			 * getBedNumber());
			 * 
			 * } else if(patientIssueReturn.getEncounter() != null) {
			 * patientIssueReturnResponse = new
			 * PatientIssueReturnResponse(patientIssueReturn.getId(),
			 * patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(),
			 * patientIssueReturn.getPatient().getUhidNumber(), patientDetail,
			 * patientIssueReturn.getVisitType().getVisitTypeCode(),
			 * patientIssueReturn.getEncounter().getEncounterNumber(),
			 * patientIssueReturn.getStore().getStoreName(),
			 * patientIssueReturn.getStatus().getStatus()); } } }
			 */

		}
		return patientIssueReturnList;
	}

	@Override
	@Transactional
	public PatientIssueReturnDetails patientIssueReturnById(Integer id, Integer unitId) {
		PatientIssueReturnDetails patientIssueReturnDetails = new PatientIssueReturnDetails();
		PatientIssueReturn patientIssueReturn = patientIssueReturnDao.patientIssueReturnById(id, unitId);

		Integer age = Util.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());
		String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " "
				+ patientIssueReturn.getPatient().getFirstName() + " " + patientIssueReturn.getPatient().getMiddleName()
				+ " " + patientIssueReturn.getPatient().getLastName() + " | " + age + " | "
				+ patientIssueReturn.getPatient().getGender().getGenderCode();
		patientIssueReturnDetails = new PatientIssueReturnDetails(patientIssueReturn.getId(),
				patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getPatient().getPatientId(),
				patientIssueReturn.getPatient().getUhidNumber(), patientIssueReturn.getStore().getStoreName(),
				patientIssueReturn.getReturnNo(), patientDetail);
		patientIssueReturnDetails.setWardName(
				patientIssueReturn.getWardMaster() != null ? patientIssueReturn.getWardMaster().getWardName() : null);
		patientIssueReturnDetails.setBedNo(
				patientIssueReturn.getBedMaster() != null ? patientIssueReturn.getBedMaster().getBedNumber() : null);

		if (patientIssueReturn.getAdmission() != null) {
			patientIssueReturnDetails.setVisitNo(patientIssueReturn.getAdmission().getAdmissionNumber());
		} else if (patientIssueReturn.getEncounter() != null) {
			patientIssueReturnDetails.setVisitNo(patientIssueReturn.getEncounter().getEncounterNumber());
		}
		List<PatientIssueReturnDetailItem> items = new ArrayList<PatientIssueReturnDetailItem>();
		List<com.param.entity.model.master.ReturnReason> reasonList = new ArrayList<>();
		reasonList = GlobalDao.getReturnReason(unitId);
		for (PatientIssueReturnDetail patientIssueReturnDetail : patientIssueReturnDao.patientIssueReturnDetailById(id,
				unitId)) {
			PatientIssueReturnDetailItem item = new PatientIssueReturnDetailItem(patientIssueReturnDetail.getId(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getName(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getName(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getUomType().getType(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getUomUnit().getUnits(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getBatchNo(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getExpiryDate(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getIssueQuantity(),
					patientIssueReturnDetail.getReturnQuantity(),
					patientIssueReturnDetail.getReturnReason().getReasonDesc(), 
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getIsThreeUOMApplicable(),
					GlobalDao.getUnitOfMeasurementListByItem(patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getId(), patientIssueReturn.getOrganizationId()));
			item.setIssueNo(
					patientIssueReturnDetail.getIssueDetailBatchMapper().getIssueDetail().getIssue().getIssueNo());
			item.setIssueDate(
					patientIssueReturnDetail.getIssueDetailBatchMapper().getIssueDetail().getIssue().getAddedDate());

			if (reasonList != null && reasonList.size() > 0) {
				for (com.param.entity.model.master.ReturnReason returnReason : reasonList) {
					if (returnReason.getId() == patientIssueReturnDetail.getReturnReason().getReasonMasterId()) {
						item.setReasonForReturn(returnReason.getReason());
					}
				}
			}
			items.add(item);
		}
		patientIssueReturnDetails.setItems(items);
		return patientIssueReturnDetails;
	}

	@Override
	@Transactional
	public List<PatientAgainstIssueResponse> patientAgainstIssueSearch(
			PatientIssueReturnRequest patientIssueReturnRequest) {
		List<PatientAgainstIssueResponse> patientAgainstIssueResponseList = new ArrayList<PatientAgainstIssueResponse>();
		patientIssueReturnRequest.setToDate((new DateTime(patientIssueReturnRequest.getToDate())).plusDays(1).toDate());
		List<PatientIssueReturnRequest> patientIssueReturnRequestList = patientIssueReturnDao.patientAgainstIssueSearch(patientIssueReturnRequest);
		
		
		for (PatientIssueReturnRequest issue : patientIssueReturnRequestList) {
			Boolean flag=false;
			
			List<PatientIssueReturnRequest> issueDetailsList = patientIssueReturnDao.patientAgainstIssueDetailsSearch(issue);
			
			for(PatientIssueReturnRequest issueDetail :issueDetailsList )
			{
				List<PatientIssueReturnRequest> getIssueDetailBatchMapperList = patientIssueReturnDao.patientAgainstIssueDetailsBatchMapperSearch(issueDetail);
				
				for(PatientIssueReturnRequest bm : getIssueDetailBatchMapperList)
				{
					if(bm.getReturnAcceptedQuantity()==null)
					{
						flag=true;
					}
					
				}
				
			}
			
	/*		if (!issue.getStatus().getId().equals(2) && flag.equals(false))
				if (issue.getPatientIndent() == null) {
					if (issue.getAdmission() != null) {
						Integer age = Util.getAgeByBirthDate(
								issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
						String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " "
								+ issue.getAdmission().getPatient().getFirstName() + " "
								+ issue.getAdmission().getPatient().getMiddleName() + " "
								+ issue.getAdmission().getPatient().getLastName() + " | " + age + " | "
								+ issue.getAdmission().getPatient().getGender().getGenderCode();
						PatientAgainstIssueResponse patientAgainstIssueResponse = new PatientAgainstIssueResponse(
								issue.getId(), issue.getIssueNo(), issue.getAddedDate(), patientDetail,
								issue.getAdmission().getPatient().getUhidNumber(),
								issue.getAdmission().getCreatedDate(), issue.getAdmission().getAdmissionNumber());
						patientAgainstIssueResponse.setStoreName(issue.getStore().getStoreName());
						patientAgainstIssueResponse.setStoreId(issue.getStore().getStoreId());
						patientAgainstIssueResponseList.add(patientAgainstIssueResponse);
					} else if (issue.getEncounter() != null) {
						Integer age = Util.getAgeByBirthDate(
								issue.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
						String patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " "
								+ issue.getEncounter().getPatient().getFirstName() + " "
								+ issue.getEncounter().getPatient().getMiddleName() + " "
								+ issue.getEncounter().getPatient().getLastName() + " | " + age + " | "
								+ issue.getEncounter().getPatient().getGender().getGenderCode();
						PatientAgainstIssueResponse patientAgainstIssueResponse = new PatientAgainstIssueResponse(
								issue.getId(), issue.getIssueNo(), issue.getAddedDate(), patientDetail,
								issue.getEncounter().getPatient().getUhidNumber(),
								issue.getEncounter().getCreatedDate(), issue.getEncounter().getEncounterNumber());
						patientAgainstIssueResponse.setStoreName(issue.getStore().getStoreName());
						patientAgainstIssueResponse.setStoreId(issue.getStore().getStoreId());
						patientAgainstIssueResponseList.add(patientAgainstIssueResponse);
					}
				}*/
			if (!issue.getStatusId().equals(2) && flag.equals(false))
				if (issue.getPatientIndentId() == null) {
					if (issue.getAdmissionId() != null) {
						Integer age = (Util.getAgeByBirthDate(issue.getPatientDob().toLocalDateTime().toLocalDate()));
						
						String patientDetail = issue.getPatientDetails() + " | " + age + " | " +issue.getGenderCode();
						
						PatientAgainstIssueResponse patientAgainstIssueResponse = new PatientAgainstIssueResponse(
								issue.getId(), issue.getIssueNo(), issue.getCreatedDate(), patientDetail,
								issue.getUhidNumber(),
								issue.getCreatedDate(), issue.getAdmissionNumber());
						
						patientAgainstIssueResponse.setStoreName(issue.getStoreName());
						patientAgainstIssueResponse.setStoreId(issue.getStoreId());
						patientAgainstIssueResponseList.add(patientAgainstIssueResponse);
					} else if (issue.getEncounterId() != null) {
						Integer age = (Util.getAgeByBirthDate(issue.getPatientDob().toLocalDateTime().toLocalDate()));
						String patientDetail = issue.getPatientDetailsEnc() + " | " + age + " | " +issue.getGenderCode();
					
						PatientAgainstIssueResponse patientAgainstIssueResponse = new PatientAgainstIssueResponse(
								issue.getId(), issue.getIssueNo(), issue.getCreatedDate(), patientDetail,
								issue.getUhidNumber(),
								issue.getCreatedDate(), issue.getAdmissionNumber());
						patientAgainstIssueResponse.setStoreName(issue.getStoreName());
						patientAgainstIssueResponse.setStoreId(issue.getStoreId());
						patientAgainstIssueResponseList.add(patientAgainstIssueResponse);
					}
				}
		}
		return patientAgainstIssueResponseList;
	}

	@Override
	@Transactional
	public Object patientAgainstItemSearch(PatientIssueReturnRequest patientIssueReturnRequest) {
		patientIssueReturnRequest.setToDate((new DateTime(patientIssueReturnRequest.getToDate())).plusDays(1).toDate());
		List<PatientAgainstItemResponse> list=patientIssueReturnDao.patientAgainstItemSearch(patientIssueReturnRequest);
		List<PatientAgainstItemResponse> list1=new ArrayList<PatientAgainstItemResponse>();
		for (PatientAgainstItemResponse patientAgainstItemResponse : list) {
			Integer qty=patientIssueReturnDao.patientIssueReturnDao(patientAgainstItemResponse.getBatchMapperId());
			patientAgainstItemResponse.setPatientIssueReturnQuantity(qty);
			if(patientAgainstItemResponse.getReturnQuantity()!=patientAgainstItemResponse.getPatientIssueReturnQuantity())
			{
				if(patientAgainstItemResponse.getReturnQuantity()!=null && patientAgainstItemResponse.getReturnRejectedQuantity()!=null)
				{
				if((patientAgainstItemResponse.getReturnQuantity()!=null ? patientAgainstItemResponse.getReturnQuantity() :0)!=((patientAgainstItemResponse.getReturnRejectedQuantity()!=null ? patientAgainstItemResponse.getReturnRejectedQuantity() : 0)+(patientAgainstItemResponse.getReturnIssuedQty()!=null ? patientAgainstItemResponse.getReturnIssuedQty():0)))
				{
					list1.add(patientAgainstItemResponse);
				}
			}
			}
		}
		list=list1;
		return list;
	}

	@Override
	@Transactional
	public PatientIssueReturnAddItemResponse patientIssueReturnAddNew(
			PatientIssueReturnAddItem patientIssueReturnAddItem) {
		PatientIssueReturnAddItemResponse patientIssueReturnAddItemResponse = new PatientIssueReturnAddItemResponse();
		// Integer orgId=0;
		if (patientIssueReturnAddItem.getItemIds() != null && patientIssueReturnAddItem.getIssueIds() != null) {
			List<Object> items = new ArrayList<Object>();
			List<Issue> issueList = null;
			if (patientIssueReturnAddItem.getScreenId() == null) {
				issueList = patientIssueReturnDao.patientIssueReturnAddItemByItemId(patientIssueReturnAddItem);
			} else if (patientIssueReturnAddItem.getScreenId().equals(1)) {
				issueList = patientIssueReturnDao.patientIssueReturnAddItemByItemId(patientIssueReturnAddItem);
			}

			if (!issueList.isEmpty()) {
				Issue issue = issueList.get(0);
				if (issue.getAdmission() != null) {
					Integer wardId = 0;
					Integer bedId = 0;
					AdmissionDetail admissionDetail = GlobalDao
							.getPatientWardBedDetails(issue.getAdmission().getAdmissionId());
					if (admissionDetail != null) {
						WardMasters wardMaster = admissionDetail.getWardMaster();
						if (wardMaster != null) {
							wardId = wardMaster.getWardId();
						}
						BedMaster bedMaster = admissionDetail.getBedMaster();
						if (bedMaster != null) {
							bedId = bedMaster.getBedId();
						}
					}
					patientIssueReturnAddItemResponse = new PatientIssueReturnAddItemResponse(
							issue.getAdmission().getPatient().getPatientId(), issue.getVisitType().getVisitTypeId(),
							issue.getStore().getStoreId(), issue.getStore().getStoreName());
					patientIssueReturnAddItemResponse.setAdmissionId(issue.getAdmission().getAdmissionId());
					patientIssueReturnAddItemResponse.setWardId(wardId != null ? wardId : null);
					patientIssueReturnAddItemResponse.setBedId(bedId != null ? bedId : null);
					patientIssueReturnAddItemResponse
							.setToStoreId(issue.getToStore() != null ? issue.getToStore().getStoreId() : null);
					patientIssueReturnAddItemResponse
							.setToStoreName(issue.getToStore() != null ? issue.getToStore().getStoreName() : null);
				} else if (issue.getEncounter() != null) {
					patientIssueReturnAddItemResponse = new PatientIssueReturnAddItemResponse(
							issue.getEncounter().getPatient().getPatientId(), issue.getVisitType().getVisitTypeId(),
							issue.getStore().getStoreId(), issue.getStore().getStoreName());
					patientIssueReturnAddItemResponse
							.setToStoreId(issue.getToStore() != null ? issue.getToStore().getStoreId() : null);
					patientIssueReturnAddItemResponse
							.setToStoreName(issue.getToStore() != null ? issue.getToStore().getStoreName() : null);
					patientIssueReturnAddItemResponse.setEncounterId(issue.getEncounter().getEncounterId());
				}
			}
			for (Issue issue : issueList) {
				List<PatientAgainstItemResponse> item = patientIssueReturnDao.getItemByIssueAndItemId(issue.getId(),
						patientIssueReturnAddItem.getItemIds(), patientIssueReturnAddItem.getUnitId(),patientIssueReturnAddItem.getBatchIds());
				List<PatientAgainstItemResponse> item1=new ArrayList<PatientAgainstItemResponse>();
				if (issue.getAdmission() != null) {
					for (PatientAgainstItemResponse r : item) {
						Integer qty=patientIssueReturnDao.patientIssueReturnDao(r.getBatchMapperId());
						r.setPatientIssueReturnQuantity(qty);
						BigInteger consumedQty = GlobalDao.getConsumedQty(
								issue.getAdmission().getPatient().getPatientId(), r.getItemId(), r.getBatchId(), r.getIssuDetailId());
						r.setComsumedQty(consumedQty);
						List<UomResponse> uomList = GlobalDao.getUnitOfMeasurementListByItem(r.getItemId(), patientIssueReturnAddItem.getOrganizationId());
						r.setUomList(uomList);
						if(r.getPatientIssueReturnQuantity()!=r.getReturnQuantity())
						{
							item1.add(r);
						}
					}
					item=item1;
				} else if (issue.getEncounter() != null) {
					// add above code in if condition for resolved HIS-10635
					/*
					 * start code for resolved HIS-10635 Unable to add sales return issue from get
					 * issue pop up throwing an error for OP patient on patient issue return screen
					 */
					for (PatientAgainstItemResponse r : item) {
						BigInteger consumedQty = GlobalDao.getConsumedQty(
								issue.getEncounter().getPatient().getPatientId(), r.getItemId(), r.getBatchId(), r.getIssuDetailId());
						r.setComsumedQty(consumedQty);
						List<UomResponse> uomList = GlobalDao.getUnitOfMeasurementListByItem(r.getItemId(), patientIssueReturnAddItem.getOrganizationId());
						r.setUomList(uomList);
					}
				}
				/*
				 * end code for resolved HIS-10635 Unable to add sales return issue from get
				 * issue pop up throwing an error for OP patient on patient issue return screen
				 */

				// item.add(consumedQty);
				items.addAll(item);

			}
			patientIssueReturnAddItemResponse.setItems(items);
		} else if (patientIssueReturnAddItem.getIssueIds() != null) {
			List<Object> items = new ArrayList<Object>();
			List<Issue> issueList = patientIssueReturnDao.patientIssueReturnAddItemByIssueId(patientIssueReturnAddItem);
			if (!issueList.isEmpty()) {
				Issue issue = issueList.get(0);
				if (issue.getAdmission() != null) {
					Integer wardId = 0;
					Integer bedId = 0;
					AdmissionDetail admissionDetail = GlobalDao
							.getPatientWardBedDetails(issue.getAdmission().getAdmissionId());
					if (admissionDetail != null) {
						WardMasters wardMaster = admissionDetail.getWardMaster();
						if (wardMaster != null) {
							wardId = wardMaster.getWardId();
						}
						BedMaster bedMaster = admissionDetail.getBedMaster();
						if (bedMaster != null) {
							bedId = bedMaster.getBedId();
						}
					}
					patientIssueReturnAddItemResponse = new PatientIssueReturnAddItemResponse(
							issue.getAdmission().getPatient().getPatientId(), issue.getVisitType().getVisitTypeId(),
							issue.getStore().getStoreId(), issue.getStore().getStoreName());
					patientIssueReturnAddItemResponse.setAdmissionId(issue.getAdmission().getAdmissionId());
					patientIssueReturnAddItemResponse.setWardId(wardId != null ? wardId : null);
					patientIssueReturnAddItemResponse.setBedId(bedId != null ? bedId : null);
					patientIssueReturnAddItemResponse
							.setToStoreId(issue.getToStore() != null ? issue.getToStore().getStoreId() : null);
					patientIssueReturnAddItemResponse
							.setToStoreName(issue.getToStore() != null ? issue.getToStore().getStoreName() : null);
				} else if (issue.getEncounter() != null) {
					patientIssueReturnAddItemResponse = new PatientIssueReturnAddItemResponse(
							issue.getEncounter().getPatient().getPatientId(), issue.getVisitType().getVisitTypeId(),
							issue.getStore().getStoreId(), issue.getStore().getStoreName());
					patientIssueReturnAddItemResponse
							.setToStoreId(issue.getToStore() != null ? issue.getToStore().getStoreId() : null);
					patientIssueReturnAddItemResponse
							.setToStoreName(issue.getToStore() != null ? issue.getToStore().getStoreName() : null);
					patientIssueReturnAddItemResponse.setEncounterId(issue.getEncounter().getEncounterId());
				}
			}
			
			List<Integer> issueIds = issueList.stream().map(Issue::getId).collect(Collectors.toList());
			
			List<PatientAgainstItemResponse> itemList = patientIssueReturnDao.getItemByIssueId(issueIds,
					patientIssueReturnAddItem.getUnitId());
			
			List<Integer> itemsIds = itemList.stream().map(PatientAgainstItemResponse::getItemId).collect(Collectors.toList());
			List<UomResponse> uomLists=null;
			if(itemsIds!=null && itemsIds.size()>0)
			{
				 uomLists = GlobalDao.getUnitOfMeasurementListByItemIds(itemsIds, patientIssueReturnAddItem.getOrganizationId());
			}
			List<Integer> batchMapperIds = itemList.stream().map(PatientAgainstItemResponse::getBatchMapperId).collect(Collectors.toList());
			
			List<PatientAgainstItemResponse> qtyBatchMapperId = patientIssueReturnDao.patientIssueReturnList(batchMapperIds);
			
			for (Issue issue : issueList) {
				
				List<PatientAgainstItemResponse> item = itemList.stream().filter( issu -> issu.getId().equals(issue.getId())).collect(Collectors.toList());
				
				Integer itmId=0;
				Long bthId=0L;
				
				if (issue.getAdmission() != null) {

					List<PatientAgainstItemResponse> item1=new ArrayList<PatientAgainstItemResponse>(); 
					
					for (PatientAgainstItemResponse r : item) {
						
						
						PatientAgainstItemResponse returnQty = qtyBatchMapperId.stream()
								  .filter(order -> r.getBatchMapperId().equals(order.getBatchMapperId()))
								  .findAny()
								  .orElse(null);
						if(returnQty!=null){
							r.setPatientIssueReturnQuantity(returnQty.getReturnQuantity());
						}else{
							r.setPatientIssueReturnQuantity(0);
						}
						
						/*Integer qty=patientIssueReturnDao.patientIssueReturnDao(r.getBatchMapperId());
						r.setPatientIssueReturnQuantity(qty);*/
						
						
						
						if(!itmId.equals(r.getItemId()) && !bthId.equals( r.getBatchId()))
						{
							BigInteger consumedQty = GlobalDao.getConsumedQty(
									issue.getAdmission().getPatient().getPatientId(), r.getItemId(), r.getBatchId(), r.getIssuDetailId());
							r.setComsumedQty(consumedQty);
							
							List<UomResponse> uomList = uomLists.stream().filter( issu -> issu.getItemId().equals(r.getItemId())).collect(Collectors.toList());
							
							///List<UomResponse> uomList = GlobalDao.getUnitOfMeasurementListByItem(r.getItemId(), patientIssueReturnAddItem.getOrganizationId());
							r.setUomList(uomList);
						}else {
							r.setComsumedQty(BigInteger.ZERO);
							List<UomResponse> uomList = uomLists.stream().filter( issu -> issu.getItemId().equals(r.getItemId())).collect(Collectors.toList());
							r.setUomList(uomList);
						}
						if(r.getPatientIssueReturnQuantity()!=r.getReturnQuantity())
						{
							item1.add(r);
						}
						itmId=r.getItemId();
						bthId=r.getBatchId();

					}
					item=item1;
				} else if (issue.getEncounter() != null) {
					// add above code in if condition for resolved HIS-10635
					/*
					 * start code for resolved HIS-10635 Unable to add sales return issue from get
					 * issue pop up throwing an error for OP patient on patient issue return screen
					 */
					for (PatientAgainstItemResponse r : item) {
						if(!itmId.equals(r.getItemId()) && !bthId.equals( r.getBatchId()))
						{
							BigInteger consumedQty = GlobalDao.getConsumedQty(
									issue.getEncounter().getPatient().getPatientId(), r.getItemId(), r.getBatchId(), r.getIssuDetailId());
							r.setComsumedQty(consumedQty);
							List<UomResponse> uomList = uomLists.stream().filter( issu -> issu.getItemId().equals(r.getItemId())).collect(Collectors.toList());
							r.setUomList(uomList);
						}else {
							r.setComsumedQty(BigInteger.ZERO);
							List<UomResponse> uomList = uomLists.stream().filter( issu -> issu.getItemId().equals(r.getItemId())).collect(Collectors.toList());
							r.setUomList(uomList);
						}
						
						itmId=r.getItemId();
						bthId=r.getBatchId();
					}
				}
				/*
				 * end code for resolved HIS-10635 Unable to add sales return issue from get
				 * issue pop up throwing an error for OP patient on patient issue return screen
				 */

				items.addAll(item);
			}
			patientIssueReturnAddItemResponse.setItems(items);
		}
		List<ReturnReason> returnReasons = new ArrayList<ReturnReason>();

		// for (com.param.entity.model.master.ReturnReason returnReason :
		// GlobalDao.getReturnReason(patientIssueReturnAddItem.getUnitId())) {
		for (ReasonMaster returnReason : GlobalDao.getreasonDropdownListByTypeId(null, 18)) {
			ReturnReason reason = new ReturnReason(returnReason.getReasonMasterId(), returnReason.getReasonDesc());
			returnReasons.add(reason);
		}

		patientIssueReturnAddItemResponse.setReturnReason(returnReasons);
		return patientIssueReturnAddItemResponse;
	}

	@Override
	@Transactional
	public Object patientIssueReturnSave(PatientIssueReturnSave patientIssueReturnSave) {
		
		HashMap<Integer,List<PatientIssueReturnSaveItem>> clubItems = new HashMap<Integer,List<PatientIssueReturnSaveItem>>();
		for (PatientIssueReturnSaveItem item : patientIssueReturnSave.getItems()) {
			
			if(clubItems.containsKey(item.getSaleId())) {
				
				clubItems.get(item.getSaleId()).add(item);
				
			}else {
				
				List<PatientIssueReturnSaveItem> its = new ArrayList<>();
				its.add(item);
				clubItems.put(item.getSaleId(), its);
			}
		}
		
	CommonResponse response = new CommonResponse();
	for (Map.Entry<Integer,List<PatientIssueReturnSaveItem>> mapData : clubItems.entrySet()) {
	
		patientIssueReturnSave.setItems(null);
		
		patientIssueReturnSave.setItems(mapData.getValue());
	
		
		
		PatientIssueReturn patientIssueReturn = new PatientIssueReturn();

		String patIssueRetNumber = GlobalService.getBillNoByGeneratedCode(patientIssueReturnSave.getStoreId(),
				patientIssueReturnSave.getUnitId(), screenId);

		if (patientIssueReturnSave.getAdmissionId() != null) {
			patientIssueReturn = new PatientIssueReturn(patIssueRetNumber,
					new PatientRegistration(patientIssueReturnSave.getPatientId()),
					new VisitType(patientIssueReturnSave.getVisitType()),
					new Admission(patientIssueReturnSave.getAdmissionId()),
					new Store(patientIssueReturnSave.getStoreId()), new WardMasters(patientIssueReturnSave.getWardId()),
					new BedMaster(patientIssueReturnSave.getBedId()), new Status(patientIssueReturnSave.getStatusId()));
			patientIssueReturn.setFromMedication(patientIssueReturnSave.getFromMedication());
			// sessionService.setEntityDefaults(patientIssueReturn);
			patientIssueReturn.setTostore(
					patientIssueReturnSave.getToStoreId() != null ? new Store(patientIssueReturnSave.getToStoreId())
							: null);
			patientIssueReturn.setCreateDefaults(
					patientIssueReturnSave.getOrganizationId() != null ? patientIssueReturnSave.getOrganizationId()
							: sessionService.getOrganizationId(),
					patientIssueReturnSave.getUnitId() != null ? patientIssueReturnSave.getUnitId()
							: sessionService.getUnitId(),
					patientIssueReturnSave.getUserId() != null ? patientIssueReturnSave.getUserId()
							: sessionService.getUserId());
			
			
			if(patientIssueReturnSave.getReturnDate()!= null){
				SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");  
				Date today = null;
				try {
					today = formatter2.parse(patientIssueReturnSave.getReturnDate());
					patientIssueReturn.setAddedDate(today);
				} catch (ParseException e) {
					logger.error(e.getMessage(), e);
					throw new ServiceException(e.getMessage());
				}
				
				
			}

		} else if (patientIssueReturnSave.getEncounterId() != null) {
			patientIssueReturn = new PatientIssueReturn(patIssueRetNumber,
					new PatientRegistration(patientIssueReturnSave.getPatientId()),
					new VisitType(patientIssueReturnSave.getVisitType()),
					new Encounter(patientIssueReturnSave.getEncounterId()),
					new Store(patientIssueReturnSave.getStoreId()), new Status(patientIssueReturnSave.getStatusId()));
			patientIssueReturn.setFromMedication(patientIssueReturnSave.getFromMedication());
			// sessionService.setEntityDefaults(patientIssueReturn);
			patientIssueReturn.setTostore(
					patientIssueReturnSave.getToStoreId() != null ? new Store(patientIssueReturnSave.getToStoreId())
							: null);
			patientIssueReturn.setCreateDefaults(
					patientIssueReturnSave.getOrganizationId() != null ? patientIssueReturnSave.getOrganizationId()
							: sessionService.getOrganizationId(),
					patientIssueReturnSave.getUnitId() != null ? patientIssueReturnSave.getUnitId()
							: sessionService.getUnitId(),
					patientIssueReturnSave.getUserId() != null ? patientIssueReturnSave.getUserId()
							: sessionService.getUserId());

		}
		for (PatientIssueReturnSaveItem item : patientIssueReturnSave.getItems()) {
			PatientIssueReturnDetail patientIssueReturnDetail = new PatientIssueReturnDetail(
					new IssueDetailBatchMapper(item.getBatchMapperId()),
					new Status(patientIssueReturnSave.getStatusId()), item.getReturnQuantity(),
					(item.getReturnReasonId() != null) ? new ReasonMaster(item.getReturnReasonId()) : null);
			patientIssueReturnDetail.setCreateDefaults(
					patientIssueReturnSave.getOrganizationId() != null ? patientIssueReturnSave.getOrganizationId()
							: sessionService.getOrganizationId(),
					patientIssueReturnSave.getUnitId() != null ? patientIssueReturnSave.getUnitId()
							: sessionService.getUnitId(),
					patientIssueReturnSave.getUserId() != null ? patientIssueReturnSave.getUserId()
							: sessionService.getUserId());
			
			if(patientIssueReturnSave.getReturnDate()!= null){
				SimpleDateFormat formatter2=new SimpleDateFormat("dd-MM-yyyy");  
				Date today = null;
				try {
					today = formatter2.parse(patientIssueReturnSave.getReturnDate());
					patientIssueReturnDetail.setAddedDate(today);
				} catch (ParseException e) {
					logger.error(e.getMessage(), e);
					throw new ServiceException(e.getMessage());
				}
			}
			
			patientIssueReturn.addPatientIssueReturnDetail(patientIssueReturnDetail);
			com.param.entity.model.pharmacy.Issue issue = patientIssueReturnDao.getIssue(item.getId());
			issue.setIsReturn(true);
			patientIssueReturn.setFromMedicineIssue(patientIssueReturnSave.getFromMedicineIssue());
			patientIssueReturnDao.updateIssue(issue);
			IssueDetailBatchMapper bm = GlobalDao.getIssueDetailBatchMapperById(item.getBatchMapperId());
			patientIssueReturnSave
					.setStoreId(patientIssueReturnSave.getToStoreId() != null ? patientIssueReturnSave.getToStoreId()
							: patientIssueReturnSave.getStoreId());
			
		}

		PatientIssueReturn patientIssueReturnResponse = patientIssueReturnDao.patientIssueReturnSave(patientIssueReturn);
		
		response.setId(patientIssueReturnResponse.getId().longValue());
		response.setMessage("Patient Issue Return saved successfully");
	}
		return response;
	}

	
	private void updateStoreTransitStock(Integer storeId, Long batchId, Integer quantity, Integer requestStoreId,
			UomType uomType, PatientIssueReturnSave request, Issue issue, IssueDetailBatchMapper d,
			PatientIssueReturnSaveItem i) {

		if (quantity > 0) {
			try {
				Batch batch = GlobalDao.getBatch(batchId);
				if (batch != null) {
					// Integer leaseQuantity = Util.GetLeaseQuantity(quantity,
					// batch.getBatchUomList(), uomType);
					Integer inTransitQuantity = GlobalDao.getTransitQuantityByBatch(storeId, batch.getId());
					Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);
					Integer tQty = (inTransitQuantity != null ? inTransitQuantity : 0) + leaseQuantity;
					// StoreStock storeStockList = GlobalDao.getStoreStockByBatchAndStore(batchId,
					// requestStoreId,null);
					List<StoreStock> storeStockLists = GlobalDao.getStoreStockByBatchAndStoreList(batchId,
							requestStoreId, batch.getUnitId());
					for (StoreStock storeStockList : storeStockLists) {

						// StoreStock storeStockDetail = null;
						if (storeStockList.getStore().getStoreId().equals(requestStoreId) && leaseQuantity > 0) {
							// tQuantity = leaseQuantity + storeStockList.gettQty();
							// availableQuantity = storeStockList.getAvailableQty() - leaseQuantity;
							// storeStockList.setAvailableQty(availableQuantity);
							// Integer availableQty=storeStockList.getAvailableQty();
							// if(availableQty >= leaseQuantity)
							// {
							// as discussed with vishal , divyesh , rahul
							// storeStockList.setAvailableQty(storeStockList.getAvailableQty()-leaseQuantity);
							com.param.entity.model.master.Screen screen1 = GlobalDao.getScreen(screenId);
							// leaseQuantity=leaseQuantity-leaseQuantity;
							storeStockList.settQty(tQty);
							GlobalDao.updateStoreStockLeaseQuantity(storeStockList);
							StockTransaction stockTransaction = new StockTransaction(storeStockList.getAvailableQty(),
									screen1, screen1.getCode(), leaseQuantity,storeStockList.getStore(), d.getId());
							// sessionService.setEntityDefaults(stockTransaction);
							stockTransaction.setIsTransit(true); 
							 stockTransaction.setItem(batch.getItem());
 
							stockTransaction.setTransitNo(issue.getIssueNo());
							stockTransaction.setCreateDefaults(request.getOrganizationId(), request.getUnitId(),
									request.getUserId());
							batch.addStockTransaction(stockTransaction);
							GlobalDao.saveStockTransaction(stockTransaction);
							break;
							// leaseQuantity = leaseQuantity-leaseQuantity;
							// }
							// else if(availableQty < leaseQuantity)
							// {
							// Integer qtt=leaseQuantity-availableQty; // 5=15-10
							// 10-15=-5
							// as discussed with vishal , divyesh , rahul
							// storeStockList.setAvailableQty( (storeStockList.getAvailableQty()
							// -leaseQuantity) <= 0 ? 0 :storeStockList.getAvailableQty() -leaseQuantity);
							// storeStockList.settQty(tQty);
							// leaseQuantity=qtt;
							// }

							// StoreStock ss=batch.getStoreStockList().get(0);

						}
					}
				}
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
		}

	}

}