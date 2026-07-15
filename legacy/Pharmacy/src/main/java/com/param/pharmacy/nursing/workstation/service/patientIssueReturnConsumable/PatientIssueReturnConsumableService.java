package com.param.pharmacy.nursing.workstation.service.patientIssueReturnConsumable;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.PatientIssueReturnDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.utility.Util;
import com.param.pharmacy.nursing.workstation.dao.patientIssueReturnConsumable.IPatientIssueReturnConsumableDao;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstIssueResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientAgainstItemResponse;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnRequest;
import com.param.pharmacy.nursing.workstation.dto.patientIssueReturn.PatientIssueReturnResponse;

@Service("patientIssueReturnConsumableService")
@Transactional
public class PatientIssueReturnConsumableService implements IPatientIssueReturnConsumableService
{
	@Autowired
	private IPatientIssueReturnConsumableDao patientIssueReturnConsumableDao;
	
	@Autowired
	private IGlobalDao GlobalDao;
	
	private Integer screenId = Screen.PatientIssueReturn.getScreen();
	
	@Override
	@Transactional
	public CommonDropdown getDropdownList(Integer unitId)
	{
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}
		
		for (WardMasters wardMaster : GlobalDao.getWardList(unitId)) {
			response.addWard(new com.param.global.dto.global.Ward(wardMaster.getWardId(),
					wardMaster.getWardName()));
		}
		
		return response;
	}
	
	@Override
	@Transactional
	public List<PatientIssueReturnResponse> patientIssueReturnSearch(PatientIssueReturnRequest patientIssueReturnRequest)
	{
		List<PatientIssueReturnResponse> patientIssueReturnList = new ArrayList<PatientIssueReturnResponse>();
		patientIssueReturnRequest.setToDate((new DateTime(patientIssueReturnRequest.getToDate())).plusDays(1).toDate());
		for(PatientIssueReturn patientIssueReturn : patientIssueReturnConsumableDao.patientIssueReturnSearch(patientIssueReturnRequest, screenId))
		{
			Integer age = Util.getAgeByBirthDate(patientIssueReturn.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = patientIssueReturn.getPatient().getPrefix().getPrefixCode() + " " + patientIssueReturn.getPatient().getFirstName() + " " + patientIssueReturn.getPatient().getMiddleName()
					+ " " + patientIssueReturn.getPatient().getLastName() + " | " + age + " | " + patientIssueReturn.getPatient().getGender().getGenderCode();

			if(patientIssueReturn.getAdmission() != null)
			{
				PatientIssueReturnResponse patientIssueReturnResponse = new PatientIssueReturnResponse(patientIssueReturn.getId(),
						patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(), patientIssueReturn.getPatient().getUhidNumber(),
						patientDetail, patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getAdmission().getAdmissionNumber(),
						patientIssueReturn.getStore().getStoreName(), patientIssueReturn.getStatus().getStatus(), patientIssueReturn.getFromMedicineIssue());
					patientIssueReturnResponse.setWardName(patientIssueReturn.getWardMaster()!=null? patientIssueReturn.getWardMaster().getWardName():null);
					patientIssueReturnResponse.setBedNo(patientIssueReturn.getBedMaster()!=null ? patientIssueReturn.getBedMaster().getBedNumber():null);
				String indentNo="";
				
				if(patientIssueReturn.getPatientIssueReturnDetail() !=null && patientIssueReturn.getPatientIssueReturnDetail().size() >0)
				{
					if(patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail().getIssue().getPatientIndent()!=null)
					{
						indentNo=patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail().getIssue().getPatientIndent().getIndentNo();
					}
					
				}
				
				patientIssueReturnResponse.setIndentNo(indentNo);
				
				patientIssueReturnList.add(patientIssueReturnResponse);
			}
			else if(patientIssueReturn.getEncounter() != null)
			{
				PatientIssueReturnResponse patientIssueReturnResponse = new PatientIssueReturnResponse(patientIssueReturn.getId(),
						patientIssueReturn.getReturnNo(), patientIssueReturn.getAddedDate(), patientIssueReturn.getPatient().getUhidNumber(),
						patientDetail, patientIssueReturn.getVisitType().getVisitTypeCode(), patientIssueReturn.getEncounter().getEncounterNumber(),
						patientIssueReturn.getStore().getStoreName(), patientIssueReturn.getStatus().getStatus(), patientIssueReturn.getFromMedicineIssue());
              String indentNo="";
				
				if(patientIssueReturn.getPatientIssueReturnDetail() !=null && patientIssueReturn.getPatientIssueReturnDetail().size() >0)
				{
					if(patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail().getIssue().getPatientIndent()!=null)
					{
						indentNo=patientIssueReturn.getPatientIssueReturnDetail().get(0).getIssueDetailBatchMapper().getIssueDetail().getIssue().getPatientIndent().getIndentNo();
					}
					
				}
				patientIssueReturnResponse.setIndentNo(indentNo);
				patientIssueReturnList.add(patientIssueReturnResponse);
			}
			/*for(PatientIssueReturnDetail patientIssueReturnDetail : patientIssueReturn.getPatientIssueReturnDetail())
			{
				//System.out.println("PatientIndendID"+patientIssueReturnDetail.getIssueDetailBatchMapper().getIssueDetail().getIssue().getPatientIndent());
				if(patientIssueReturnDetail.getIssueDetailBatchMapper().getIssueDetail().getIssue().getPatientIndent() != null)
				{
								
				}
			}*/
		}
		return patientIssueReturnList;
	}
	
	@Override
	@Transactional
	public List<PatientAgainstIssueResponse> patientAgainstIssueSearch(PatientIssueReturnRequest patientIssueReturnRequest)
	{
		List<PatientAgainstIssueResponse> patientAgainstIssueResponseList = new ArrayList<PatientAgainstIssueResponse>();
		patientIssueReturnRequest.setToDate((new DateTime(patientIssueReturnRequest.getToDate())).plusDays(1).toDate());
		for(Issue issue : patientIssueReturnConsumableDao.patientAgainstIssueSearch(patientIssueReturnRequest))
		{
			if(issue.getStatus().getId() != 2) {
			if(issue.getAdmission() != null)
				{
					Integer age = Util.getAgeByBirthDate(issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " " + issue.getAdmission().getPatient().getFirstName() + " " + issue.getAdmission().getPatient().getMiddleName() 
							+ " " + issue.getAdmission().getPatient().getLastName() + " | " + age + " | " + issue.getAdmission().getPatient().getGender().getGenderCode();
					PatientAgainstIssueResponse patientAgainstIssueResponse = new PatientAgainstIssueResponse(issue.getId(), issue.getIssueNo(), issue.getAddedDate(),
							patientDetail, issue.getAdmission().getPatient().getUhidNumber(), issue.getAdmission().getCreatedDate(), issue.getAdmission().getAdmissionNumber());
					patientAgainstIssueResponse.setIndentNo(issue.getPatientIndent().getIndentNo());
					patientAgainstIssueResponse.setToStoreId(issue.getToStore()!=null ?issue.getToStore().getStoreId():null );
					patientAgainstIssueResponse.setToStoreName(issue.getToStore()!=null ?issue.getToStore().getStoreName():null );
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
					patientAgainstIssueResponse.setToStoreId(issue.getToStore()!=null ?issue.getToStore().getStoreId():null );
					patientAgainstIssueResponse.setToStoreName(issue.getToStore()!=null ?issue.getToStore().getStoreName():null );
					patientAgainstIssueResponseList.add(patientAgainstIssueResponse);
				}
			}
		}
		return patientAgainstIssueResponseList;
	}
	
	@Override
	@Transactional
	public Object patientAgainstItemSearch(PatientIssueReturnRequest patientIssueReturnRequest)
	{
		patientIssueReturnRequest.setToDate((new DateTime(patientIssueReturnRequest.getToDate())).plusDays(1).toDate());
		List<PatientAgainstItemResponse> list=patientIssueReturnConsumableDao.patientAgainstItemSearch(patientIssueReturnRequest);
		List<PatientAgainstItemResponse> list1=new ArrayList<PatientAgainstItemResponse>();
		for (PatientAgainstItemResponse patientAgainstItemResponse : list) {
			Integer qty=patientIssueReturnConsumableDao.patientIssueReturnDao(patientAgainstItemResponse.getBatchMapperId());
			patientAgainstItemResponse.setPatientIssueReturnQuantity(qty);
			if(patientAgainstItemResponse.getReturnQuantity()!=patientAgainstItemResponse.getPatientIssueReturnQuantity())
			{
				list1.add(patientAgainstItemResponse);
			}
		}
		list=list1;
		return list;
	}
	
}