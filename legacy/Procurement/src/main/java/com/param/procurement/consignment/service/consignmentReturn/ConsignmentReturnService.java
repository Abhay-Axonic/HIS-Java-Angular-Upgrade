package com.param.procurement.consignment.service.consignmentReturn;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.PatientIssueReturnDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.ReturnReason;
import com.param.global.dto.global.Ward;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.consignment.dao.consignmentReturn.IConsignmentReturnDao;
import com.param.procurement.consignment.dto.consignmentReturn.ConsignmentReturnDropdown;
import com.param.procurement.consignment.dto.consignmentReturn.ImplantReturnSave;
import com.param.procurement.consignment.dto.consignmentReturn.ImplantReturnSaveItem;
import com.param.procurement.consignment.dto.consignmentReturn.PatientAgainstIssueResponse;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnAddItem;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnAddItemResponse;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnDetailItem;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnDetails;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnRequest;
import com.param.procurement.consignment.dto.consignmentReturn.PatientIssueReturnResponse;
import com.param.service.global.SessionService;
@Service("ConsignmentReturnService")
public class ConsignmentReturnService implements IConsignmentReturnService {

	@Autowired
	private SessionService sessionService;

	@Autowired 
	IGlobalDao GlobalDao;
	
	@Autowired
	IGlobalService  GlobalService ;
	
	@Autowired
	IConsignmentReturnDao ConsignmentReturnDao;
	
	private Integer screenId = Screen.ConsignmentReturn.getScreen();
	
	@Override
	@Transactional
	public ConsignmentReturnDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId)
	{
		 //For Main Dash Board Dropdown
		ConsignmentReturnDropdown response = new ConsignmentReturnDropdown();
		for (VisitType visitType : GlobalDao.getVisitTypeList())
		{
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(), visitType.getVisitTypeCode()));
		}
		for (WardMasters ward : GlobalDao.getWardList(unitId))
		{
			response.addWard(new Ward(ward.getWardId(), ward.getWardName()));
		}
		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId))
		{
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}
		return response;
	}
	
	//Consignment Main Dash Board Search 
	@Override
	@Transactional
	public List<PatientIssueReturnResponse> consignmentMainSearch(PatientIssueReturnRequest patientIssueReturnRequest)
	{
		List<PatientIssueReturnResponse> patientIssueReturnList = new ArrayList<PatientIssueReturnResponse>();
		patientIssueReturnRequest.setToDate((new DateTime(patientIssueReturnRequest.getToDate())).plusDays(1).toDate());
		for(PatientIssueReturn patientIssueReturn : ConsignmentReturnDao.consignmentMainSearch(patientIssueReturnRequest, screenId))
		{
			for(PatientIssueReturnDetail patientIssueReturnDetail : patientIssueReturn.getPatientIssueReturnDetail())
			{
				if(patientIssueReturnDetail.getIssueDetailBatchMapper().getIssueDetail().getIssue().getPatientIndent() == null)
				{
					Integer age = Util.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " " + patientIssueReturn.getPatient().getFirstName() + " " + patientIssueReturn.getPatient().getMiddleName()
							+ " " + patientIssueReturn.getPatient().getLastName() + " | " + age + " | " + patientIssueReturn.getPatient().getGender().getGenderCode();
					if(patientIssueReturn.getAdmission() != null)
					{
						PatientIssueReturnResponse patientIssueReturnResponse = new PatientIssueReturnResponse(patientIssueReturn.getId(),
								patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(), patientIssueReturn.getPatient().getUhidNumber(),
								patientDetail, patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getAdmission().getAdmissionNumber(),
								patientIssueReturn.getStore().getStoreName(), patientIssueReturn.getStatus().getStatus());
						patientIssueReturnResponse.setWardName(patientIssueReturn.getWardMaster().getWardName());
						patientIssueReturnResponse.setBedNo(patientIssueReturn.getBedMaster().getBedNumber());
						
						patientIssueReturnList.add(patientIssueReturnResponse);
					}
					else if(patientIssueReturn.getEncounter() != null)
					{
						PatientIssueReturnResponse patientIssueReturnResponse = new PatientIssueReturnResponse(patientIssueReturn.getId(),
								patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(), patientIssueReturn.getPatient().getUhidNumber(),
								patientDetail, patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getEncounter().getEncounterNumber(),
								patientIssueReturn.getStore().getStoreName(), patientIssueReturn.getStatus().getStatus());
						patientIssueReturnList.add(patientIssueReturnResponse);
					}
				}
			}
				
		}
		return patientIssueReturnList;
	}
	//Implant Main Dash Board Search 
		@Override
		@Transactional
		public List<PatientIssueReturnResponse> implantMainSearch(PatientIssueReturnRequest patientIssueReturnRequest)
		{
			List<PatientIssueReturnResponse> patientIssueReturnList = new ArrayList<PatientIssueReturnResponse>();
			patientIssueReturnRequest.setToDate((new DateTime(patientIssueReturnRequest.getToDate())).plusDays(1).toDate());
			for(PatientIssueReturn patientIssueReturn : ConsignmentReturnDao.consignmentMainSearch(patientIssueReturnRequest, screenId))
			{
				for(PatientIssueReturnDetail patientIssueReturnDetail : patientIssueReturn.getPatientIssueReturnDetail())
				{
					if(patientIssueReturnDetail.getIssueDetailBatchMapper().getIssueDetail().getIssue().getPatientIndent() == null)
					{
						Integer age = Util.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());
						String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " " + patientIssueReturn.getPatient().getFirstName() + " " + patientIssueReturn.getPatient().getMiddleName()
								+ " " + patientIssueReturn.getPatient().getLastName() + " | " + age + " | " + patientIssueReturn.getPatient().getGender().getGenderCode();
						if(patientIssueReturn.getAdmission() != null)
						{
							PatientIssueReturnResponse patientIssueReturnResponse = new PatientIssueReturnResponse(patientIssueReturn.getId(),
									patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(), patientIssueReturn.getPatient().getUhidNumber(),
									patientDetail, patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getAdmission().getAdmissionNumber(),
									patientIssueReturn.getStore().getStoreName(), patientIssueReturn.getStatus().getStatus());
							
						//	patientIssueReturnResponse.setIndentNo(patientIssueReturn.getPatientIndent().getIndentNo());
							patientIssueReturnResponse.setWardName(patientIssueReturn.getWardMaster().getWardName());
							patientIssueReturnResponse.setBedNo(patientIssueReturn.getBedMaster().getBedNumber());
							
							patientIssueReturnList.add(patientIssueReturnResponse);
						}
						else if(patientIssueReturn.getEncounter() != null)
						{
							PatientIssueReturnResponse patientIssueReturnResponse = new PatientIssueReturnResponse(patientIssueReturn.getId(),
									patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(), patientIssueReturn.getPatient().getUhidNumber(),
									patientDetail, patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getEncounter().getEncounterNumber(),
									patientIssueReturn.getStore().getStoreName(), patientIssueReturn.getStatus().getStatus());
							
							patientIssueReturnList.add(patientIssueReturnResponse);
						}
					}
				}
					
			}
			return patientIssueReturnList;
		}
	//Implant Return Get Details List Of Consginment And Implant Return
	@Override
	@Transactional
	public PatientIssueReturnDetails patientIssueReturnById(Integer id, Integer unitId)
	{
		PatientIssueReturnDetails patientIssueReturnDetails = new PatientIssueReturnDetails();
		PatientIssueReturn patientIssueReturn = ConsignmentReturnDao.patientIssueReturnById(id, unitId);
		
		Integer age = Util.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());
		String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " " + patientIssueReturn.getPatient().getFirstName() + " " + patientIssueReturn.getPatient().getMiddleName()
				+ " " + patientIssueReturn.getPatient().getLastName() + " | " + age + " | " +patientIssueReturn.getPatient().getGender().getGenderCode();
		patientIssueReturnDetails = new PatientIssueReturnDetails(patientIssueReturn.getId(), patientIssueReturn.getVisitType().getVisitTypeCode(),
				patientIssueReturn.getPatient().getPatientId(), patientIssueReturn.getPatient().getUhidNumber(),
				patientIssueReturn.getStore().getStoreName(),patientIssueReturn.getWardMaster().getWardName(),patientIssueReturn.getBedMaster().getBedCode(), 
				patientIssueReturn.getReturnNo(),patientDetail);
		
		if(patientIssueReturn.getAdmission() != null)
		{
			patientIssueReturnDetails.setVisitNo(patientIssueReturn.getAdmission().getAdmissionNumber());
		}
		else if(patientIssueReturn.getEncounter() != null)
		{
			patientIssueReturnDetails.setVisitNo(patientIssueReturn.getEncounter().getEncounterNumber());
		}
		List<PatientIssueReturnDetailItem> items = new ArrayList<PatientIssueReturnDetailItem>();
		for(PatientIssueReturnDetail patientIssueReturnDetail : ConsignmentReturnDao.patientIssueReturnDetailById(id, unitId))
		{
			PatientIssueReturnDetailItem item = new PatientIssueReturnDetailItem(patientIssueReturnDetail.getId(), 
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getName(), patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getItem().getGeneric().getName(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getUomType().getType(), patientIssueReturnDetail.getIssueDetailBatchMapper().getUomUnit().getUnits(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getBatchNo(), patientIssueReturnDetail.getIssueDetailBatchMapper().getBatch().getExpiryDate(),
					patientIssueReturnDetail.getIssueDetailBatchMapper().getIssueQuantity(), patientIssueReturnDetail.getIssueDetailBatchMapper().getAcceptedQuantity(),
					patientIssueReturnDetail.getReturnReason().getReasonDesc());
			items.add(item);
		}
		patientIssueReturnDetails.setItems(items);
		return patientIssueReturnDetails;
	}
	
	@Override
	@Transactional
	public List<PatientAgainstIssueResponse> patientAgainstIssueSearch(PatientIssueReturnRequest patientIssueReturnRequest)
	{ 
		//consignment retrun Against Issue Search
		List<PatientAgainstIssueResponse> patientAgainstIssueResponseList = new ArrayList<PatientAgainstIssueResponse>();
		patientIssueReturnRequest.setToDate((new DateTime(patientIssueReturnRequest.getToDate())).plusDays(1).toDate());
		for(Issue issue : ConsignmentReturnDao.patientAgainstIssueSearch(patientIssueReturnRequest))
		{
			if(issue.getStatus().getId() == 8) {
			if(issue.getAdmission() != null)
				{
					Integer age = Util.getAgeByBirthDate(issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " " + issue.getAdmission().getPatient().getFirstName() + " " + issue.getAdmission().getPatient().getMiddleName() 
							+ " " + issue.getAdmission().getPatient().getLastName() + " | " + age + " | " + issue.getAdmission().getPatient().getGender().getGenderCode();
					PatientAgainstIssueResponse patientAgainstIssueResponse = new PatientAgainstIssueResponse(issue.getId(), issue.getIssueNo(), issue.getAddedDate(),
							patientDetail, issue.getAdmission().getPatient().getUhidNumber(), issue.getAdmission().getCreatedDate(), issue.getAdmission().getAdmissionNumber());
					patientAgainstIssueResponse.setIndentNo(issue.getPatientIndent().getIndentNo());
					patientAgainstIssueResponseList.add(patientAgainstIssueResponse);
				}
				else if(issue.getEncounter() != null)
				{
					Integer age = Util.getAgeByBirthDate(issue.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " " + issue.getEncounter().getPatient().getFirstName() + " " + issue.getEncounter().getPatient().getMiddleName() 
							+ " " + issue.getEncounter().getPatient().getLastName() + " | " + age + " | " + issue.getEncounter().getPatient().getGender().getGenderCode();
					PatientAgainstIssueResponse patientAgainstIssueResponse = new PatientAgainstIssueResponse(issue.getId(), issue.getIssueNo(), issue.getAddedDate(),
							patientDetail, issue.getEncounter().getPatient().getUhidNumber(), issue.getEncounter().getCreatedDate(), issue.getEncounter().getEncounterNumber());
					patientAgainstIssueResponse.setIndentNo(issue.getPatientIndent().getIndentNo());
					patientAgainstIssueResponseList.add(patientAgainstIssueResponse);
				}
			}
		}
		return patientAgainstIssueResponseList;
	}
	
	//consignment retrun Against Item Search	
	@Override
	@Transactional
	public Object patientAgainstItemSearch(PatientIssueReturnRequest patientIssueReturnRequest)
	{
		return ConsignmentReturnDao.patientAgainstItemSearch(patientIssueReturnRequest);
	}
	
	//consignment And Implant Retrun Against Item add new
	@Override
	@Transactional
	public PatientIssueReturnAddItemResponse patientIssueReturnAddNew(PatientIssueReturnAddItem patientIssueReturnAddItem)
	{
		PatientIssueReturnAddItemResponse patientIssueReturnAddItemResponse = new PatientIssueReturnAddItemResponse();
		
		if(patientIssueReturnAddItem.getItemIds() != null && patientIssueReturnAddItem.getIssueIds() != null)
		{
			List<Object> items = new ArrayList<Object>();
			List<Issue> issueList = ConsignmentReturnDao.patientIssueReturnAddItemByItemId(patientIssueReturnAddItem);
			if(!issueList.isEmpty())
			{
				Issue issue = issueList.get(0);
				if(issue.getAdmission() != null)
				{
					Integer wardId = 0;
					Integer bedId = 0;
					AdmissionDetail admissionDetail = GlobalDao.getPatientWardBedDetails(issue.getAdmission().getAdmissionId());
					if(admissionDetail != null)
					{
						WardMasters wardMaster = admissionDetail.getWardMaster();
						if(wardMaster != null)
						{
							wardId = wardMaster.getWardId();
						}
						BedMaster bedMaster = admissionDetail.getBedMaster();
						if(bedMaster != null)
						{
							bedId = bedMaster.getBedId();
						}
					}
					patientIssueReturnAddItemResponse = new PatientIssueReturnAddItemResponse(issue.getAdmission().getPatient().getPatientId(), 
							issue.getVisitType().getVisitTypeId(), issue.getStore().getStoreId(), issue.getStore().getStoreName());
					patientIssueReturnAddItemResponse.setAdmissionId(issue.getAdmission().getAdmissionId());
					patientIssueReturnAddItemResponse.setWardId(wardId != null ? wardId : null);
					patientIssueReturnAddItemResponse.setBedId(bedId != null ? bedId : null);
				}
				else if(issue.getEncounter() != null)
				{
					patientIssueReturnAddItemResponse = new PatientIssueReturnAddItemResponse(issue.getEncounter().getPatient().getPatientId(), 
							issue.getVisitType().getVisitTypeId(), issue.getStore().getStoreId(), issue.getStore().getStoreName());
					patientIssueReturnAddItemResponse.setEncounterId(issue.getEncounter().getEncounterId());
				}
			}
			for(Issue issue : issueList)
			{
				List<Object> item = ConsignmentReturnDao.getItemByIssueAndItemId(issue.getId(), patientIssueReturnAddItem.getItemIds(),patientIssueReturnAddItem.getUnitId());
				items = item;
				patientIssueReturnAddItemResponse.setItems(items);
			}
		}
		else if(patientIssueReturnAddItem.getIssueIds() != null)
		{
			List<Object> items = new ArrayList<Object>();
			List<Issue> issueList = ConsignmentReturnDao.patientIssueReturnAddItemByIssueId(patientIssueReturnAddItem);
			if(!issueList.isEmpty())
			{
				Issue issue = issueList.get(0);
				if(issue.getAdmission() != null)
				{
					Integer wardId = 0;
					Integer bedId = 0;
					AdmissionDetail admissionDetail = GlobalDao.getPatientWardBedDetails(issue.getAdmission().getAdmissionId());
					if(admissionDetail != null)
					{
						WardMasters wardMaster = admissionDetail.getWardMaster();
						if(wardMaster != null)
						{
							wardId = wardMaster.getWardId();
						}
						BedMaster bedMaster = admissionDetail.getBedMaster();
						if(bedMaster != null)
						{
							bedId = bedMaster.getBedId();
						}
					}
					patientIssueReturnAddItemResponse = new PatientIssueReturnAddItemResponse(issue.getAdmission().getPatient().getPatientId(), 
							issue.getVisitType().getVisitTypeId(), issue.getStore().getStoreId(), issue.getStore().getStoreName());
					patientIssueReturnAddItemResponse.setAdmissionId(issue.getAdmission().getAdmissionId());
					patientIssueReturnAddItemResponse.setWardId(wardId != null ? wardId : null);
					patientIssueReturnAddItemResponse.setBedId(bedId != null ? bedId : null);
				}
				else if(issue.getEncounter() != null)
				{
					patientIssueReturnAddItemResponse = new PatientIssueReturnAddItemResponse(issue.getEncounter().getPatient().getPatientId(), 
							issue.getVisitType().getVisitTypeId(), issue.getStore().getStoreId(), issue.getStore().getStoreName());
					patientIssueReturnAddItemResponse.setEncounterId(issue.getEncounter().getEncounterId());
				}
			}
			for(Issue issue : issueList)
			{
				List<Object> item = ConsignmentReturnDao.getItemByIssueId(issue.getId(),patientIssueReturnAddItem.getUnitId());
				items = item;
				patientIssueReturnAddItemResponse.setItems(items);
			}
			
		}
		List<ReturnReason> returnReasons = new ArrayList<ReturnReason>();

		for (com.param.entity.model.master.ReturnReason returnReason : GlobalDao.getReturnReason(patientIssueReturnAddItem.getUnitId())) {
			ReturnReason reason = new ReturnReason(returnReason.getId(), returnReason.getReason());
			returnReasons.add(reason);
		}
		
		patientIssueReturnAddItemResponse.setReturnReason(returnReasons);
		return patientIssueReturnAddItemResponse;
	}
	
	@Override
	@Transactional
	public Object implantReturnSave(ImplantReturnSave implantReturnSave)
	{
		PatientIssueReturn patientIssueReturn = new PatientIssueReturn();
		
		/*Integer storeId = implantReturnSave.getStoreId();

		Store storeNew = GlobalDao.getStore(storeId);

		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/

		String implantReturnRetNumber = GlobalService.getBillNoByGeneratedCode(implantReturnSave.getStoreId(), implantReturnSave.getUnitId(), screenId);

		if(implantReturnSave.getAdmissionId() != null)
		{
			patientIssueReturn = new PatientIssueReturn(implantReturnRetNumber, new PatientRegistration(implantReturnSave.getPatientId()),
					new VisitType(implantReturnSave.getVisitType()), new Admission(implantReturnSave.getAdmissionId()),
					new Store(implantReturnSave.getStoreId()), new WardMasters(implantReturnSave.getWardId()),
					new BedMaster(implantReturnSave.getBedId()), new Status(implantReturnSave.getStatusId()));
//			sessionService.setEntityDefaults(patientIssueReturn);
			patientIssueReturn.setCreateDefaults(implantReturnSave.getOrganizationId(),implantReturnSave.getUnitId(),
					implantReturnSave.getUserId());
		}
		else if(implantReturnSave.getEncounterId() != null)
		{
			patientIssueReturn = new PatientIssueReturn(implantReturnRetNumber, new PatientRegistration(implantReturnSave.getPatientId()),
					new VisitType(implantReturnSave.getVisitType()), new Encounter(implantReturnSave.getEncounterId()),
					new Store(implantReturnSave.getStoreId()), new Status(implantReturnSave.getStatusId()));
//			sessionService.setEntityDefaults(patientIssueReturn);
			patientIssueReturn.setCreateDefaults(implantReturnSave.getOrganizationId(),implantReturnSave.getUnitId(),
					implantReturnSave.getUserId());
		}
		for(ImplantReturnSaveItem item : implantReturnSave.getItems())
		{
			PatientIssueReturnDetail patientIssueReturnDetail=
					new PatientIssueReturnDetail(new IssueDetailBatchMapper(item.getBatchMapperId()),
					item.getReturnQuantity(), (item.getReturnReasonId() != null) ? new ReasonMaster(item.getReturnReasonId()) : null,
							new Status(implantReturnSave.getStatusId()));
			patientIssueReturn.addPatientIssueReturnDetail(patientIssueReturnDetail);
//			sessionService.setEntityDefaults(patientIssueReturnDetail);
			patientIssueReturnDetail.setCreateDefaults(implantReturnSave.getOrganizationId(),implantReturnSave.getUnitId(),
					implantReturnSave.getUserId());
		}
		PatientIssueReturn patientIssueReturnResponse = ConsignmentReturnDao.implantReturnSave(patientIssueReturn);
		CommonResponse response = new CommonResponse();
		response.setId(patientIssueReturnResponse.getId().longValue());
		response.setMessage("Implant Return saved successfully");

		return response;
	}
	
	
}
