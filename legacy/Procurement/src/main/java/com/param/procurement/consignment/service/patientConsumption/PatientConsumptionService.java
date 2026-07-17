package com.param.procurement.consignment.service.patientConsumption;

import java.math.BigDecimal;

//PatientConsumableIssueController

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.log4j.Logger;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.inventory.StockTransaction;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.Tax;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.entity.model.pharmacy.SaleDetailPayeeMapper;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.BillingMasterDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.items.BatchResponse;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.pharmacy.dto.PatientConsumtionIssueSave;
import com.param.global.pharmacy.dto.PatientConsumtionSaveItem;
import com.param.global.pharmacy.dto.SaleDetailPayeeMapperDto;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.procurement.consignment.dao.patientConsumption.IPatientConsumptionDao;
import com.param.procurement.consignment.dto.patientConsumption.Carrier;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumptionRequest;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumptionResponse;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionDetail;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionDetailtem;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionDropdown;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionIssueListDetails;
import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionIssueListResponse;
//import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionIssueSave;
//import com.param.procurement.consignment.dto.patientConsumption.PatientConsumtionIssueSave;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;


@Service
public class PatientConsumptionService implements IPatientConsumptionService {

	final static Logger logger = Logger.getLogger(PatientConsumptionService.class);

	@Autowired
	IPatientConsumptionDao PatientConsumptionDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	SessionService sessionService;
	
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
	

	private Integer screenId = Screen.PatientConsignment.getScreen();
	private Integer screenIdWalkInSale = Screen.WalkinSale.getScreen();
	private com.param.entity.model.master.Screen screen;
	
	@Override
	@Transactional
	public PatientConsumtionDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId)
	{
		PatientConsumtionDropdown response = new PatientConsumtionDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for(Store store : GlobalDao.getUserStoreList(unitId,orgId,userId))
		{
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for(WardMasters ward : GlobalDao.getWardList(unitId))
		{
			response.addWard(new com.param.global.dto.global.Ward(ward.getWardId(), ward.getWardName()));
		}

	  /* for (Store store : GlobalDao.getUserStoreList()) {
			response.addStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}*/
		return response;
	}
	
		//For Patient Pending Search
	@Override
	@Transactional
	public List<PatientConsumptionResponse> patientConsumptionSearch(PatientConsumptionRequest request) {

		List<PatientConsumptionResponse> patientIndentList = new ArrayList<PatientConsumptionResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		List<Issue> lst=PatientConsumptionDao.patientConsumptionSearch(request, screenId);
		//System.out.println(lst.size());
		for (Issue issue : lst) {
			
			String doctorDetail ="";
			if(issue.getDoctor()!=null && issue.getSpecialityMaster()!=null)
			{
				doctorDetail = issue.getDoctor().getFirstName() + " "
						+ issue.getDoctor().getMiddleName() + " "
						+ issue.getDoctor().getLastName() + " | "
						+ issue.getSpecialityMaster().getSpecialityName();
			}
			if (issue.getImplantIndent() != null && issue.getIsImplant() == true) {

				Integer age = Util.getAgeByBirthDate(
						issue.getImplantIndent().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = issue.getImplantIndent().getPatient().getPrefix().getPrefixCode() + " "
						+ issue.getImplantIndent().getPatient().getFirstName() + " "
						+ issue.getImplantIndent().getPatient().getMiddleName() + " "
						+ issue.getImplantIndent().getPatient().getLastName() + " | " + age + " | "
						+ issue.getImplantIndent().getPatient().getGender().getGenderCode();
				/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
						.doctorSearchByIdValue(issue.getImplantIndent().getDoctor().getDoctorId());*/

				if (issue.getImplantIndent().getAdmission() != null) {
					PatientConsumptionResponse response = new PatientConsumptionResponse(
							issue.getId(), issue.getImplantIndent().getImplantIndentNo(),
							issue.getImplantIndent().getAddedDate(),
							issue.getImplantIndent().getVisitType().getVisitTypeName(),
							issue.getImplantIndent().getAdmission().getAdmissionNumber(),
							issue.getImplantIndent().getPatient().getUhidNumber(), patientDetail, doctorDetail,
							issue.getImplantIndent().getStore().getStoreName(),
							issue.getStatus().getId(),
							issue.getStatus().getStatus(),
							issue.getImplantIndent().getUpdatedBy().getUserName(),
							issue.getIssueNo()
							);

					patientIndentList.add(response);
				}
			}
			
			if(issue.getPatientIndent() != null ) {
				if(issue.getAdmission() != null)
				{
					Integer age = Util.getAgeByBirthDate(issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " " + issue.getAdmission().getPatient().getFirstName() + " " + issue.getAdmission().getPatient().getMiddleName() + " " +
							issue.getAdmission().getPatient().getLastName() + " | " + age + " | " + issue.getAdmission().getPatient().getGender().getGenderCode();
					
					PatientConsumtionIssueListResponse patientIssue = new PatientConsumtionIssueListResponse(issue.getId(),
							issue.getVisitType().getVisitTypeName(), issue.getAdmission().getAdmissionNumber(),
							issue.getAdmission().getPatient().getUhidNumber(), patientDetail, doctorDetail, issue.getStore().getStoreName(),
							issue.getStatus().getId(), issue.getStatus().getStatus(),
							issue.getPatientIndent().getIndentNo(), issue.getAddedDate(),issue.getIssueNo());
					patientIssue.setWardName((issue.getPatientIndent().getWard() != null) ? issue.getPatientIndent().getWard().getWardName() : null);
					patientIssue.setBedNo((issue.getPatientIndent().getBed() != null) ? issue.getPatientIndent().getBed().getBedNumber() : null);
					patientIssue.setIssueNo(issue.getIssueNo());
					patientIssue.setIssueDate(issue.getAddedDate());
					patientIndentList.add(patientIssue);
				}
				else if(issue.getEncounter() != null)
				{
					Integer age = Util.getAgeByBirthDate(issue.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " " + issue.getEncounter().getPatient().getFirstName() + " " + issue.getEncounter().getPatient().getMiddleName() + " " +
							issue.getEncounter().getPatient().getLastName() + " | " + age + " | " + issue.getEncounter().getPatient().getGender().getGenderCode();
					PatientConsumtionIssueListResponse patientIssue = new PatientConsumtionIssueListResponse(issue.getId(),
							issue.getVisitType().getVisitTypeName(), issue.getEncounter().getEncounterNumber(),
							issue.getEncounter().getPatient().getUhidNumber(), patientDetail, doctorDetail, issue.getStore().getStoreName(),
							issue.getStatus().getId(), issue.getStatus().getStatus(),issue.getPatientIndent().getIndentNo(), issue.getAddedDate(),issue.getIssueNo());
					patientIssue.setIssueNo(issue.getIssueNo());
					patientIssue.setIssueDate(issue.getAddedDate());
					patientIndentList.add(patientIssue);
				}
			}
		}
		return patientIndentList;
	}
	
	@Override
	@Transactional
	public List<PatientConsumptionResponse> patientConsumtionIssueListSearch(PatientConsumptionRequest request) {

		List<PatientConsumptionResponse> patientIndentList = new ArrayList<PatientConsumptionResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());

		for (Issue issue : PatientConsumptionDao.patientConsumtionIssueListSearch(request, screenId)) {

			String doctorDetail = "";
			if(issue.getDoctor()!=null && issue.getSpecialityMaster()!=null)
			{
				doctorDetail = issue.getDoctor().getFirstName() + " "
						+ issue.getDoctor().getMiddleName() + " "
						+ issue.getDoctor().getLastName() + " | "
						+ issue.getSpecialityMaster().getSpecialityName();
			}
			
			if (issue.getImplantIndent() != null && issue.getIsImplant() == true) {

				Integer age = Util.getAgeByBirthDate(
						issue.getImplantIndent().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = issue.getImplantIndent().getPatient().getPrefix().getPrefixCode() + " "
						+ issue.getImplantIndent().getPatient().getFirstName() + " "
						+ issue.getImplantIndent().getPatient().getMiddleName() + " "
						+ issue.getImplantIndent().getPatient().getLastName() + " | " + age + " | "
						+ issue.getImplantIndent().getPatient().getGender().getGenderCode();
				/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
						.doctorSearchByIdValue(issue.getImplantIndent().getDoctor().getDoctorId());*/
				

				if (issue.getImplantIndent().getAdmission() != null) {
					PatientConsumptionResponse response = new PatientConsumptionResponse(
							issue.getId(), issue.getImplantIndent().getImplantIndentNo(),
							issue.getImplantIndent().getAddedDate(),
							issue.getImplantIndent().getVisitType().getVisitTypeName(),
							issue.getImplantIndent().getAdmission().getAdmissionNumber(),
							issue.getImplantIndent().getPatient().getUhidNumber(), patientDetail, doctorDetail,
							issue.getImplantIndent().getStore().getStoreName(),
							issue.getStatus().getId(),
							issue.getStatus().getStatus(),
							issue.getImplantIndent().getUpdatedBy().getUserName(),
							issue.getIssueNo()
							);

					patientIndentList.add(response);
				}
			}
			
			if(issue.getPatientIndent() != null ) {
				if(issue.getAdmission() != null && issue.getAdmission().getPatient() != null)
				{
					Integer age = Util.getAgeByBirthDate(
							issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " " + issue.getAdmission().getPatient().getFirstName() + " " + issue.getAdmission().getPatient().getMiddleName() + " " +
							issue.getAdmission().getPatient().getLastName() + " | " + age + " | " + issue.getAdmission().getPatient().getGender().getGenderCode();
					/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao.doctorSearchByIdValue(issue.getAdmission().getDoctor().getDoctorId());
					String doctorDetail = issue.getAdmission().getDoctor().getFirstName() + " | " + issue.getAdmission().getDoctor().getMiddleName() + " " +
							issue.getAdmission().getDoctor().getLastName() + " | " + doctorSpeciality.get(0).getSpeciality().getSpecialityName();*/
					PatientConsumtionIssueListResponse patientIssue = new PatientConsumtionIssueListResponse(issue.getId(),
							issue.getVisitType().getVisitTypeName(), issue.getAdmission().getAdmissionNumber(),
							issue.getAdmission().getPatient().getUhidNumber(), patientDetail, doctorDetail, issue.getStore().getStoreName(),
							issue.getStatus().getId(), issue.getStatus().getStatus(),
							issue.getPatientIndent().getIndentNo(), issue.getAddedDate(),issue.getIssueNo());
					patientIssue.setWardName((issue.getPatientIndent().getWard() != null) ? issue.getPatientIndent().getWard().getWardName() : null);
					patientIssue.setBedNo((issue.getPatientIndent().getBed() != null) ? issue.getPatientIndent().getBed().getBedNumber() : null);
					patientIssue.setIssueNo(issue.getIssueNo());
					patientIssue.setIssueDate(issue.getAddedDate());
					patientIndentList.add(patientIssue);
				}
				else if(issue.getEncounter() != null)
				{
					Integer age = Util.getAgeByBirthDate(issue.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " " + issue.getEncounter().getPatient().getFirstName() + " " + issue.getEncounter().getPatient().getMiddleName() + " " +
							issue.getEncounter().getPatient().getLastName() + " | " + age + " | " + issue.getEncounter().getPatient().getGender().getGenderCode();
					/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao.doctorSearchByIdValue(issue.getEncounter().getDoctor().getDoctorId());
					String doctorDetail = issue.getEncounter().getDoctor().getFirstName() + " | " + issue.getEncounter().getDoctor().getMiddleName() + " " +
							issue.getEncounter().getDoctor().getLastName() + " | " + doctorSpeciality.get(0).getSpeciality().getSpecialityName();*/
					PatientConsumtionIssueListResponse patientIssue = new PatientConsumtionIssueListResponse(issue.getId(),
							issue.getVisitType().getVisitTypeName(), issue.getEncounter().getEncounterNumber(),
							issue.getEncounter().getPatient().getUhidNumber(), patientDetail, doctorDetail, issue.getStore().getStoreName(),
							issue.getStatus().getId(), issue.getStatus().getStatus(),issue.getPatientIndent().getIndentNo(), issue.getAddedDate(),issue.getIssueNo());
					patientIssue.setIssueNo(issue.getIssueNo());
					patientIssue.setIssueDate(issue.getAddedDate());
					patientIndentList.add(patientIssue);
				}
			}
		}
		return patientIndentList;
	}
	//For PAtient List Search
	@Override
	@Transactional
	public List<PatientConsumtionIssueListResponse> patientConsumtionListSearch(PatientConsumptionRequest request)
	{
		
		List<PatientConsumtionIssueListResponse> patientIssueList = new ArrayList<PatientConsumtionIssueListResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		for(Issue issue : PatientConsumptionDao.patientConsumtionIssueListSearch(request, screenId))
		{
			String doctorDetail ="";
			
			if(issue.getDoctor()!=null && issue.getSpecialityMaster()!=null)
			{
				doctorDetail = issue.getDoctor().getFirstName() + " "
						+ issue.getDoctor().getMiddleName() + " "
						+ issue.getDoctor().getLastName() + " | "
						+ issue.getSpecialityMaster().getSpecialityName();
			}
			
		 if(issue.getPatientIndent() != null) {
			if(issue.getAdmission() != null)
			{
				Integer age = Util.getAgeByBirthDate(issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " " + issue.getAdmission().getPatient().getFirstName() + " " + issue.getAdmission().getPatient().getMiddleName() + " " +
						issue.getAdmission().getPatient().getLastName() + " | " + age + " | " + issue.getAdmission().getPatient().getGender().getGenderCode();
				/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao.doctorSearchByIdValue(issue.getAdmission().getDoctor().getDoctorId());
				;*/
				PatientConsumtionIssueListResponse patientIssue = new PatientConsumtionIssueListResponse(issue.getId(),
						issue.getPatientIndent().getIndentNo(), issue.getAddedDate(),
						issue.getVisitType().getVisitTypeCode(), issue.getAdmission().getAdmissionNumber(),
						issue.getAdmission().getPatient().getUhidNumber(), patientDetail, doctorDetail, issue.getStore().getStoreName(),
						issue.getPatientIndent().getStatus().getId(), issue.getPatientIndent().getStatus().getStatus());
				patientIssue.setWardName((issue.getPatientIndent().getWard() != null) ? issue.getPatientIndent().getWard().getWardName() : null);
				patientIssue.setBedNo((issue.getPatientIndent().getBed() != null) ? issue.getPatientIndent().getBed().getBedNumber() : null);
				patientIssue.setIssueNo(issue.getIssueNo());
				patientIssue.setIssueDate(issue.getAddedDate());
				patientIssueList.add(patientIssue);
			}
			else if(issue.getEncounter() != null)
			{
				Integer age = Util.getAgeByBirthDate(issue.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " " + issue.getEncounter().getPatient().getFirstName() + " " + issue.getEncounter().getPatient().getMiddleName() + " " +
						issue.getEncounter().getPatient().getLastName() + " | " + age + " | " + issue.getEncounter().getPatient().getGender().getGenderCode();
				/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao.doctorSearchByIdValue(issue.getEncounter().getDoctor().getDoctorId());
				String doctorDetail = issue.getEncounter().getDoctor().getFirstName() + " | " + issue.getEncounter().getDoctor().getMiddleName() + " " +
						issue.getEncounter().getDoctor().getLastName() + " | " + doctorSpeciality.get(0).getSpeciality().getSpecialityName();*/
				PatientConsumtionIssueListResponse patientIssue = new PatientConsumtionIssueListResponse(issue.getId(),
						issue.getPatientIndent().getIndentNo(), issue.getAddedDate(),
						issue.getVisitType().getVisitTypeCode(), issue.getEncounter().getEncounterNumber(),
						issue.getEncounter().getPatient().getUhidNumber(), patientDetail, doctorDetail, issue.getStore().getStoreName(),
						issue.getPatientIndent().getStatus().getId(), issue.getPatientIndent().getStatus().getStatus());
				patientIssue.setIssueNo(issue.getIssueNo());
				patientIssue.setIssueDate(issue.getAddedDate());
				patientIssueList.add(patientIssue);
			}
		}
		 if(issue.getImplantIndent() != null && issue.getIsImplant().equals(true)) {
			
			Integer age = Util.getAgeByBirthDate(issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " " + issue.getAdmission().getPatient().getFirstName() + " " + issue.getAdmission().getPatient().getMiddleName() + " " +
					issue.getAdmission().getPatient().getLastName() + " | " + age + " | " + issue.getAdmission().getPatient().getGender().getGenderCode();
			/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao.doctorSearchByIdValue(issue.getAdmission().getDoctor().getDoctorId());
			String doctorDetail = issue.getAdmission().getDoctor().getFirstName() + " | " + issue.getAdmission().getDoctor().getMiddleName() + " " +
					issue.getAdmission().getDoctor().getLastName() + " | " + doctorSpeciality.get(0).getSpeciality().getSpecialityName();*/

			PatientConsumtionIssueListResponse patientIssue = new PatientConsumtionIssueListResponse(issue.getId(),
					issue.getIssueNo(),issue.getAddedDate(),issue.getImplantIndent().getImplantIndentNo(),
					issue.getVisitType().getVisitTypeId(),issue.getVisitType().getVisitTypeCode(), issue.getVisitType().getVisitTypeName(),
					issue.getStore().getStoreId(),	issue.getStore().getStoreName(),
					issue.getAdmission().getPatient().getUhidNumber(), patientDetail, doctorDetail,
					issue.getImplantIndent().getStatus().getId(), issue.getImplantIndent().getStatus().getStatus());
			patientIssue.setIssueNo(issue.getIssueNo());
			patientIssue.setIssueDate(issue.getAddedDate());
			patientIssueList.add(patientIssue);
		  }
		}
		return patientIssueList;
	}
	

	
	//For Patient Pending Details
	@SuppressWarnings("unused")
	@Override
	@Transactional
	public PatientConsumtionDetail patientConsumptionDetails(Integer id,Integer unitId, Integer storeId,Integer orgId, Integer cookieStoreId) throws Exception
	{
		List<PatientSponsorDetails> patientSponsorDetails=null;
		Issue issue = PatientConsumptionDao.patientConsumption(id, unitId);
		
		PatientConsumtionDetail patientConsumtionIssueDetails = new PatientConsumtionDetail();
		if (issue.getPatientIndent()!= null) {
		String patientName = issue.getPatientIndent().getPatient().getFirstName() + " " + issue.getPatientIndent().getPatient().getMiddleName() + " " +
				issue.getPatientIndent().getPatient().getLastName();
		  if(issue.getAdmission() != null)
		   {
				
				
			  patientConsumtionIssueDetails = new PatientConsumtionDetail(issue.getId(), 
					issue.getStore().getStoreName(), issue.getStore().getStoreId(), issue.getVisitType().getVisitTypeId(),
					issue.getVisitType().getVisitTypeName(), patientName, issue.getPatientIndent().getPatient().getUhidNumber(), issue.getAdmission().getAdmissionNumber(),
					issue.getPatientIndent().getIndentNo(), issue.getDoctor().getDoctorId(), issue.getPatientIndent().getId());
			  patientConsumtionIssueDetails.setAdmissionId(issue.getAdmission().getAdmissionId());
			  patientConsumtionIssueDetails.setWardName((issue.getPatientIndent().getWard() != null) ? issue.getPatientIndent().getWard().getWardName() : null);
			  patientConsumtionIssueDetails.setBedNo((issue.getPatientIndent().getBed() != null) ? issue.getPatientIndent().getBed().getBedNumber() : null);
			  patientConsumtionIssueDetails.setStatusId(issue.getStatus().getId());
			  patientConsumtionIssueDetails.setStatus(issue.getStatus().getStatus());
			  patientSponsorDetails = iPatientSponsorDetailsDao.findByAdmissionId(issue.getAdmission().getAdmissionId());
		   }
		  else if(issue.getEncounter() != null)
		   {
			  patientConsumtionIssueDetails = new PatientConsumtionDetail(issue.getId(), 
					issue.getStore().getStoreName(), issue.getStore().getStoreId(), issue.getVisitType().getVisitTypeId(),
					issue.getVisitType().getVisitTypeName(), patientName, issue.getPatientIndent().getPatient().getUhidNumber(), issue.getEncounter().getEncounterNumber(),
					issue.getPatientIndent().getIndentNo(), issue.getDoctor().getDoctorId());
			  patientConsumtionIssueDetails.setEncounterId(issue.getEncounter().getEncounterId());
			  patientConsumtionIssueDetails.setStatusId(issue.getStatus().getId());
			  patientConsumtionIssueDetails.setStatus(issue.getStatus().getStatus());
			  
			  // for calculating sponsor details		  
				 patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(issue.getEncounter().getEncounterId());
		   }
		  
		 
		}
		if(issue.getImplantIndent() !=  null && issue.getIsImplant().equals(true)) {
			String patientName = issue.getImplantIndent().getPatient().getFirstName() + " " + issue.getImplantIndent().getPatient().getMiddleName() + " " +
					issue.getImplantIndent().getPatient().getLastName();
			
			patientConsumtionIssueDetails = new PatientConsumtionDetail(issue.getId(), issue.getImplantIndent().getId(),
						issue.getStore().getStoreName(), issue.getStore().getStoreId(), issue.getVisitType().getVisitTypeId(),
						issue.getVisitType().getVisitTypeName(), patientName, issue.getImplantIndent().getPatient().getUhidNumber(), 
						issue.getAdmission().getAdmissionNumber(), issue.getImplantIndent().getImplantIndentNo(), 
						issue.getDoctor().getDoctorId());
			patientConsumtionIssueDetails.setAdmissionId(issue.getAdmission().getAdmissionId());
			patientConsumtionIssueDetails.setStatusId(issue.getStatus().getId());
			patientConsumtionIssueDetails.setStatus(issue.getStatus().getStatus());
			
			// for calculating sponsor details		  
	         patientSponsorDetails = iPatientSponsorDetailsDao.findByAdmissionId(issue.getAdmission().getAdmissionId());
		}
		
		List<PatientConsumtionDetailtem> itemList = new ArrayList<PatientConsumtionDetailtem>();
		for(IssueDetail issueDetail : PatientConsumptionDao.getpatientConsumptionDetail(id, unitId))
		{
			List<BatchResponse> medicineBatches = new ArrayList<BatchResponse>();
			List<Batch> batchList =  new ArrayList<Batch>();
			
			List<IssueDetailBatchMapper> batchMapperList = issueDetail.getIssueDetailBatchMapper();
			
			if(issueDetail.getPatientIndentDetail() != null) {
			for(IssueDetailBatchMapper batchMapper : batchMapperList)
			{
				if(batchMapper.getBatch() != null)
				{
					batchList = GlobalDao.getBatchListPharmacy(issueDetail.getPatientIndentDetail().getItem().getId(), batchMapper.getBatch().getId(),issue.getStore().getStoreId(),unitId);
				}
				
				if(batchMapper.getBatch() != null)
				{
					medicineBatches.add(Util.PopulateBatchWithStockWithFlag(batchMapper.getBatch(), true));
				}
			}
			if(batchMapperList == null || batchMapperList.isEmpty())
			{
				batchList = GlobalDao.getBatchDetailsPharmacy(issueDetail.getPatientIndentDetail().getItem().getId(),storeId,unitId);
			}
			for (Batch batch : batchList) {
				medicineBatches.add(Util.PopulateBatchWithStockWithFlag(batch, false));
			}
				Long stockAvailable = GlobalDao.getStoreStockByItemPharmacy(issue.getStore().getStoreId() ,issueDetail.getPatientIndentDetail().getItem().getId(),unitId);
				Integer issueQuantity = (issueDetail.getPatientIndentDetail().getIndentQuantity() - issueDetail.getPatientIndentDetail().getPendingQuantity());
				PatientConsumtionDetailtem item = new PatientConsumtionDetailtem(issueDetail.getId(), issueDetail.getPatientIndentDetail().getItem().getId(),
						issueDetail.getPatientIndentDetail().getItem().getCode(), issueDetail.getPatientIndentDetail().getItem().getName(), 
						issueDetail.getPatientIndentDetail().getItem().getManufacturer().getName(),
						issueDetail.getPatientIndentDetail().getUomType().getType(), issueDetail.getPatientIndentDetail().getUomUnit().getUnits(), issueDetail.getPatientIndentDetail().getIndentQuantity(),
						issueQuantity, stockAvailable, medicineBatches, issueDetail.getPatientIndentDetail().getId());
				//item.setVendorName(issue.getPatientIndentDetail().getItem().getBatchList().get(0).getVendor().getName());
				item.setStatus(issueDetail.getStatus().getStatus());
				item.setStatusId(issueDetail.getStatus().getId());
				itemList.add(item);
			}
			
			if(issueDetail.getImplantIndentDetail() != null) {
				for(IssueDetailBatchMapper batchMapper : batchMapperList)
				{
					if(batchMapper.getBatch() != null)
					{
						batchList = GlobalDao.getBatchListPharmacy(issueDetail.getImplantIndentDetail().getItem().getId(), batchMapper.getBatch().getId(), issue.getStore().getStoreId(),unitId);
					}
					
					if(batchMapper.getBatch() != null)
					{
						medicineBatches.add(Util.PopulateBatchWithStockWithFlag(batchMapper.getBatch(), true));
					}
				}
				if(batchMapperList == null || batchMapperList.isEmpty())
				{
					batchList = GlobalDao.getBatchDetailsPharmacyConsignment(issueDetail.getImplantIndentDetail().getItem().getId(),issue.getStore().getStoreId(),unitId);
				}
				for (Batch batch : batchList) {
					medicineBatches.add(Util.PopulateBatchWithStockWithFlag(batch, false));
				}
					Long stockAvailable = GlobalDao.getStoreStockByItemPharmacy(issue.getStore().getStoreId() ,issueDetail.getImplantIndentDetail().getItem().getId(),unitId);
					Integer issueQuantity = (issueDetail.getImplantIndentDetail().getIndentQuantity() - issueDetail.getImplantIndentDetail().getPendingQuantity());
					PatientConsumtionDetailtem item = new PatientConsumtionDetailtem(issueDetail.getId(),
							issueDetail.getImplantIndentDetail().getId(), issueDetail.getImplantIndentDetail().getItem().getId(),
							issueDetail.getImplantIndentDetail().getItem().getCode(), issueDetail.getImplantIndentDetail().getItem().getName(), 
							issueDetail.getImplantIndentDetail().getItem().getManufacturer().getName(),
							issueDetail.getImplantIndentDetail().getUomType() != null ? issueDetail.getImplantIndentDetail().getUomType().getType() : null,
							issueDetail.getImplantIndentDetail().getUomUnit() != null ?	issueDetail.getImplantIndentDetail().getUomUnit().getUnits(): null, 
							issueDetail.getImplantIndentDetail().getIndentQuantity(), issueQuantity, stockAvailable, medicineBatches);
					
					//item.setVendorName((issueDetail.getPatientIndentDetail().getItem().getBatchList().get(0).getVendor().getName() != null) ? issueDetail.getPatientIndentDetail().getItem().getBatchList().get(0).getVendor().getName();
					item.setStatus(issueDetail.getStatus().getStatus());
					item.setStatusId(issueDetail.getStatus().getId());
					
					Integer productCategoryId =null;
					if(issueDetail.getImplantIndentDetail().getItem().getGeneric()!=null)
					{
						productCategoryId = issueDetail.getPrescriptionDetail().getItem().getGeneric().getProductCategory().getId();
					}
					else {
						productCategoryId = issueDetail.getImplantIndentDetail().getItem().getProductCategory().getId();
					}
					
					item.setItemId(issueDetail.getImplantIndentDetail().getItem().getId());
					item.setProductCategoryId(productCategoryId);
					item.setStoreId(cookieStoreId !=null ? cookieStoreId : sessionService.getStoreId());
					
					for(PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
					//	prodCatIds.add(issueDetail.getImplantIndentDetail().getItem().getGeneric().getProductCategory().getId());
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
					itemList.add(item);
				}
		}
		
		patientConsumtionIssueDetails.setItems(itemList);
		List<Carrier> carrierList = new ArrayList<Carrier>();
		for(User user : GlobalDao.getUser(orgId,unitId))
		{
			Carrier carrier = new Carrier(user.getUserId(), user.getUserName());
			carrierList.add(carrier);
		}
		patientConsumtionIssueDetails.setCarrier(carrierList);
		return patientConsumtionIssueDetails;
	}
	
	//For Implant Indend Issue List Detail 
	
	@Override
	@Transactional
	public PatientConsumtionIssueListDetails patientConsumtionIssueListDetails(Integer id,Integer unitId,Integer storeId,Integer orgId) throws Exception
	{
		PatientConsumtionIssueListDetails patientConsumtionIssueListDetails = new PatientConsumtionIssueListDetails();
		Issue issue = PatientConsumptionDao.patientIssueById(id,unitId);
		if(issue.getPatientIndent() != null)
		{ 
		  if(issue.getAdmission() != null)
		  {
			String patientName = issue.getAdmission().getPatient().getFirstName() + " " + issue.getAdmission().getPatient().getMiddleName() + " " +
					issue.getAdmission().getPatient().getLastName();
			patientConsumtionIssueListDetails = new PatientConsumtionIssueListDetails(issue.getId(),issue.getIssueNo(), issue.getAddedDate(),issue.getStore().getStoreName(),
					issue.getStore().getStoreId(), issue.getAdmission().getAdmissionNumber());
				patientConsumtionIssueListDetails.setRemark(issue.getRemark());
		  }
		  else if(issue.getEncounter() != null)
		  {
			String patientName = issue.getEncounter().getPatient().getFirstName() + " " + issue.getEncounter().getPatient().getMiddleName() + " " +
					issue.getEncounter().getPatient().getLastName();
			patientConsumtionIssueListDetails = new PatientConsumtionIssueListDetails(issue.getId(),issue.getIssueNo(), issue.getAddedDate(),issue.getStore().getStoreName(),
					issue.getStore().getStoreId(), issue.getAdmission().getAdmissionNumber());
				patientConsumtionIssueListDetails.setRemark(issue.getRemark());
		  }
		}
		
		if(issue.getImplantIndent() != null && issue.getIsImplant() == true) {
			String patientName = issue.getAdmission().getPatient().getFirstName() + " " + issue.getAdmission().getPatient().getMiddleName() + " " +
					issue.getAdmission().getPatient().getLastName();
			patientConsumtionIssueListDetails = new PatientConsumtionIssueListDetails(issue.getId(), issue.getIssueNo(),issue.getAddedDate(),issue.getStore().getStoreName(),
					issue.getStore().getStoreId(),issue.getAdmission().getAdmissionNumber());
				patientConsumtionIssueListDetails.setRemark(issue.getRemark());
		}

		for(IssueDetail issueDetail : PatientConsumptionDao.patientIssueDetailById(id,unitId))
		{
			List<PatientConsumtionDetailtem> items = new ArrayList<PatientConsumtionDetailtem>();
			
			Long stockAvailable = GlobalDao.getStoreStockByItemPharmacy(storeId ,
					issueDetail.getPatientIndentDetail() != null ? issueDetail.getPatientIndentDetail().getItem().getId() : issueDetail.getImplantIndentDetail().getItem().getId(),unitId);
			
			for(IssueDetailBatchMapper batchMapper : issueDetail.getIssueDetailBatchMapper())
			{
				if(issueDetail.getPatientIndentDetail() != null) {
					PatientConsumtionDetailtem item = new PatientConsumtionDetailtem(issueDetail.getId(), issueDetail.getPatientIndentDetail().getItem().getId(), 
						issueDetail.getPatientIndentDetail().getItem().getCode(), issueDetail.getPatientIndentDetail().getItem().getName(),
						issueDetail.getPatientIndentDetail().getItem().getManufacturer().getName(), batchMapper.getBatch().getId(), batchMapper.getBatch().getBatchNo(),
						batchMapper.getBatch().getExpiryDate(),
						batchMapper.getUomType() != null ? batchMapper.getUomType().getType() : null, 
						batchMapper.getUomType() != null ? batchMapper.getUomType().getId() : null, 
						batchMapper.getUomUnit() != null ? batchMapper.getUomUnit().getId() : null, 
						batchMapper.getUomUnit() != null ? batchMapper.getUomUnit().getUnits() : null,
						issueDetail.getPatientIndentDetail().getIndentQuantity(), 
						stockAvailable, batchMapper.getIssueQuantity());
				item.setStatus(issueDetail.getStatus().getStatus());
				item.setStatusId(issueDetail.getStatus().getId());
				items.add(item);
				}
				
				if(issueDetail.getImplantIndentDetail() != null) {
					PatientConsumtionDetailtem item = new PatientConsumtionDetailtem(issueDetail.getId(), issueDetail.getImplantIndentDetail().getItem().getId(), 
						issueDetail.getImplantIndentDetail().getItem().getCode(), issueDetail.getImplantIndentDetail().getItem().getName(),
						issueDetail.getImplantIndentDetail().getItem().getManufacturer().getName(),
						batchMapper.getBatch().getId(), batchMapper.getBatch().getBatchNo(), batchMapper.getBatch().getExpiryDate(),
						batchMapper.getUomType() != null ? batchMapper.getUomType().getType() : null, 
						batchMapper.getUomType() != null ? batchMapper.getUomType().getId() : null, 
						batchMapper.getUomUnit() != null ? batchMapper.getUomUnit().getId() : null, 
						batchMapper.getUomUnit() != null ? batchMapper.getUomUnit().getUnits() : null,
						issueDetail.getImplantIndentDetail().getIndentQuantity(), stockAvailable, batchMapper.getIssueQuantity());
					item.setStatus(issueDetail.getStatus().getStatus());
					item.setStatusId(issueDetail.getStatus().getId());
					items.add(item);
					patientConsumtionIssueListDetails.addItems(item);
				}
			}
			
		}
		
		return patientConsumtionIssueListDetails;
	}
	
	//For Patient Consumtion Save Or Update
	@Override
	@Transactional
	public Object patientConsumptionSave(PatientConsumtionIssueSave request) {
		try{
		Issue issue = PatientConsumptionDao.patientConsumption(request.getId(), request.getUnitId());
		issue.setRemark(request.getRemark() );
		issue.setStatus(new Status(request.getStatusId()));	
		
		Integer patientID = request.getPatientId();
		Integer admissionId = request.getAdmissionId();
		Integer visitTypeId = request.getVisitType();
		
		//Save sale
		/*Store storeNew = GlobalDao.getStore(request.getStoreId());
		Unit unit = GlobalDao.getUnit(sessionService.getUnitId());*/
		String billNo = GlobalService.getBillNoByGeneratedCode(request.getStoreId(), request.getUnitId(), screenIdWalkInSale);
		
		
		
		/*Double taxAmountP = 0.0, totalAmountP = 0.0, netAmountP = 0.0, paidAmt = 0.0;
		Sale sale = new Sale(request.getAdmissionId() != null ? new Admission(request.getAdmissionId()) : null, billNo, netAmountP, paidAmt,
				new com.param.entity.model.master.Status(com.param.common.constants.Status.Pending.getStatus()), 
				new Store(request.getStoreId()), taxAmountP,
				totalAmountP, netAmountP);
		sessionService.setEntityDefaults(sale);
		Map<Long,Integer> itemIds = new HashMap<>();*/
		
		
		
		
		
		
		for (PatientConsumtionSaveItem item : request.getItems()) 
		{
			IssueDetail issueDetail = PatientConsumptionDao.getpatientConsumptionDetails(item.getId(), request.getUnitId());

			Integer itemId = item.getId();
			
			Double taxAmount=0.00;
			Double netAmount=0.00;
			Double totalAmount=0.00;
			
			for (BatchResponse batch : item.getBatchList()) 
			{
				//issue.getStore().getStoreId()
				Batch batchObj = BatchStoreTransaction(request.getStoreId(), batch.getBatchId(),
						batch.getIssueQuantity(),batch.getUomTypeId() != null ? new UomType(batch.getUomTypeId()) : null,
								request.getOrganizationId(), request.getUnitId(), request.getUserId() , issueDetail);

				Integer leaseQuantity = Util.GetLeaseQuantity(batch.getIssueQuantity(), batchObj.getBatchUomList(),
						batch.getUomTypeId() != null ? new UomType(batch.getUomTypeId()) : null);
				
				
				Double itemRate = batchObj.getRate();
				if(item.getMarkUpDownPer()!=null && item.getMarkUpDownPer()>0)
				{
					if(item.getMarkUpDown()!=null && item.getMarkUpDown().intValue()==1)
					itemRate = itemRate+(itemRate*(item.getMarkUpDownPer()/100));
					
					if(item.getMarkUpDown()!=null && item.getMarkUpDown().intValue()==0)
						itemRate = itemRate-(itemRate*(item.getMarkUpDownPer()/100));
						
				}
				
				 item.setRate(itemRate);
				 item.setQuantity(batch.getIssueQuantity());
				 item.setBatchId(batch.getBatchId().intValue());
				 item.setTaxId(batchObj.getTax()!= null?batchObj.getTax().getId():null);
				 item.setTaxPercentage(batchObj.getTaxPercentage()!=null?batchObj.getTaxPercentage():0);
				 item.setLeaseQuantity(leaseQuantity);
				 
				 totalAmount = itemRate * batch.getIssueQuantity();
				 taxAmount = totalAmount * (batchObj.getTax() != null ? batchObj.getTax().getTaxPercentage() : 0.0 / 100);
				 netAmount = totalAmount + taxAmount;
				 
				/*Double saleDetailTotalAmount = batchObj.getCop() * batch.getIssueQuantity();
				Double saleDetailTaxAmount = saleDetailTotalAmount * (batchObj.getTax() != null ? batchObj.getTax().getTaxPercentage() : 0.0 / 100);
				Double saleDetailNetAmount = saleDetailTotalAmount + saleDetailTaxAmount;
				
				taxAmountP += saleDetailTaxAmount;
				totalAmountP += saleDetailTotalAmount;
				netAmountP += saleDetailNetAmount;
				itemIds.put(batch.getBatchId(), itemId);*/
				
				/*
				 * SaleDetail saleDetails=new SaleDetail(new Batch(batch.getBatchId()),
				 * leaseQuantity, saleDetailNetAmount, itemId, new
				 * com.param.entity.model.master.Status(com.param.common.constants.Status.
				 * Pending.getStatus()), batch.getIssueQuantity(), batchObj.getCop(),
				 * saleDetailNetAmount, batchObj.getTax() != null ? new
				 * Tax(batchObj.getTax().getId()) : null, batchObj.getTax() != null ?
				 * batchObj.getTax().getTaxPercentage() : null,
				 * 
				 * saleDetailTotalAmount, new Sale(sale.getId()),batch.getUomTypeId() != null ?
				 * new UomType(batch.getUomTypeId()) : null, batch.getUomUnitId() != null ? new
				 * UomUnit(batch.getUomUnitId()) : null);
				 */
				
			 	// sessionService.setEntityDefaults(saleDetails);
				// saleDetails = sale.addSaleDetailsList(saleDetails);
				 
				 UomType uType = null;
					UomUnit uUnit = null;
					if(batch.getUomTypeId() != null)
						uType = new UomType(batch.getUomTypeId());
					 item.setUomTypeId(batch.getUomTypeId());
					
					if(batch.getUomUnitId() != null)
						uUnit = new UomUnit(batch.getUomUnitId());
					 item.setUomUnitId(batch.getUomUnitId());

				Double amount = (leaseQuantity * batch.getSellingPrice());
				IssueDetailBatchMapper issueDetailBatchMapper=new IssueDetailBatchMapper(batchObj,batch.getUomTypeId() != null ? new UomType(batch.getUomTypeId()) : null,
						batch.getUomUnitId() != null ? new UomUnit(batch.getUomUnitId()) : null, batch.getIssueQuantity(), amount);
//				sessionService.setEntityDefaults(issueDetailBatchMapper);	
				issueDetailBatchMapper.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
						request.getUserId());
				issueDetail.addBatch(issueDetailBatchMapper);
				issueDetail.setStatus(new Status(item.getStatusId()));
				issueDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(issue.getUnitId()));
				issueDetail.setUpdatedByUser(request.getUserId());
			}
			item.setNetAmount(netAmount);
			item.setCreditPayable(0.00);
			item.setSelfPayable(netAmount);
			item.setTaxAmount(taxAmount);
			item.setTotalAmount(totalAmount);
		}
		
		//Check Contract
		
		Response patientConsumtionSaveRes = iWalkInSalesService.consumeItemFromPatientConsumtionSave(request,issue.getAdmission().getAdmissionId());
		
		PatientConsumtionIssueSave patientConsumtioncontract  = (PatientConsumtionIssueSave) patientConsumtionSaveRes.getObject();
		
		 Double totalSelfPayable = patientConsumtioncontract.getItems().stream() .filter(i
				  -> i.getSelfPayable()!=null) .mapToDouble(i -> i.getSelfPayable()).sum();
				  
				  Double totalCreditPayable = patientConsumtioncontract.getItems().stream()
				  .filter(i -> i.getCreditPayable()!=null) .mapToDouble(i ->
				  i.getCreditPayable()).sum();
				  
				  Double totalAmount = patientConsumtioncontract.getItems().stream()
						  .filter(i -> i.getTotalAmount()!=null) .mapToDouble(i ->
						  i.getTotalAmount()).sum();
				  
				  Double tatalNetAmount = patientConsumtioncontract.getItems().stream()
						  .filter(i -> i.getTotalAmount()!=null) .mapToDouble(i ->
						  i.getTotalAmount()).sum();
				 
				  Double totalTax = patientConsumtioncontract.getItems().stream()
						  .filter(i -> i.getTotalAmount()!=null) .mapToDouble(i ->
						  i.getTotalAmount()).sum();
				  
				  Sale sale = new Sale (request.getAdmissionId() != null ? new Admission(request.getAdmissionId()) : null,
							 billNo,
							 tatalNetAmount, 
							 0.00, 
							 new com.param.entity.model.master.Status(com.param.common.constants.Status.Pending.getStatus()),
							 new Store(request.getStoreId()),
							 totalTax,
							 totalAmount, 
							 tatalNetAmount,
							 null,
							 totalSelfPayable,
							 totalCreditPayable,com.param.common.constants.Screen.ConsumableAcceptance.getScreen());
//				  sessionService.setEntityDefaults(sale);
				  sale.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
							request.getUserId());
		/*sale.setNetAmount(netAmountP);
		sale.setTaxAmount(taxAmountP);
		sale.setTotalAmount(totalAmountP);*/
				  
				   for(PatientConsumtionSaveItem item : request.getItems())
					{
						  
							SaleDetail saleDetails=new SaleDetail(new Batch(item.getBatchId().longValue()), 
									item.getLeaseQuantity(),
									item.getNetAmount(), 
									item.getItemId(),
									new com.param.entity.model.master.Status(com.param.common.constants.Status.Pending.getStatus()),
									item.getQuantity(),
									item.getRate(), 
									item.getTaxAmount(), 
									item.getTaxId()!=null ? new Tax(item.getTaxId()) : null , 
									item.getTaxPercentage(),
									item.getTotalAmount(),	
									new Sale(sale.getId()),
									item.getUomTypeId() != null ?  new UomType(item.getUomTypeId()) : null,
											item.getUomUnitId() != null ? new UomUnit(item.getUomUnitId()) : null,item.getSelfPayable(),item.getCreditPayable(),item.getPayeeId(),item.getProductCategoryId(),item.getPackageConsumptionAmt());
							saleDetails.setItemId(item.getItemId());
						  
							List<SaleDetailPayeeMapperDto>  payeeMapper = item.getSaleDetailPayeeMapperDtoList();
							if(payeeMapper!=null && payeeMapper.size()>0)
							{
								
								//forSelf
								SaleDetailPayeeMapperDto self = new SaleDetailPayeeMapperDto();
								self.setOrganizationId(
										request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId());
								self.setUnitId(request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
								self.setItemId(item.getItemId());
								self.setStatus('A');
								self.setPayeeId(sessionService.getSelfPayeeId());
								self.setPayeeAmount(BigDecimal.valueOf(item.getSelfPayable()));
								payeeMapper.add(self);
								
								for (SaleDetailPayeeMapperDto dto : payeeMapper) {
									
									SaleDetailPayeeMapper mpper = new SaleDetailPayeeMapper();
									mpper.setOrganizationId(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId());
									mpper.setUnitId(request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
									mpper.setItemId(item.getItemId());
									mpper.setStatus('A');
									mpper.setContractId(dto.getContractId());
									mpper.setPayeeId(dto.getPayeeId());
									mpper.setPayeeAmount(dto.getPayeeAmount());
									saleDetails.addSaleDetailPayeeMapper(mpper);
								}
							}
							else 
							{
								 SaleDetailPayeeMapper mpper = new SaleDetailPayeeMapper();
								 mpper.setItemId(item.getItemId());
								 mpper.setStatus('A');
								 mpper.setOrganizationId(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId());
								 mpper.setUnitId(request.getUnitId()!=null ? request.getUnitId() : sessionService.getUnitId());
								 mpper.setPayeeId(sessionService.getSelfPayeeId());
								 mpper.setPayeeAmount(BigDecimal.valueOf(item.getSelfPayable()));
								 saleDetails.addSaleDetailPayeeMapper(mpper);
							}
//						    sessionService.setEntityDefaults(saleDetails);
							saleDetails.setCreateDefaults(request.getOrganizationId(),request.getUnitId(),
										request.getUserId());
							saleDetails = sale.addSaleDetailsList(saleDetails);
							IssueDetail issueDetail = PatientConsumptionDao.getpatientConsumptionDetails(item.getId(), request.getUnitId());
							issueDetail.setSaleDetail(saleDetails);
							 
					}
				    	  
				  
				  
		//map issue with sale		
		PatientConsumptionDao.saveSale(sale);
		issue.setSale(sale);
		
		
		Date date = TimeZoneComponent.getDateByzone(issue.getUnitId());
		double netAmount = 0;
		double taxAmount =0;
		double ordTotalAmt =0;
		Integer patientId=null;
		Map<Integer,OrderDetailsPayeeMapperDto> map = new HashMap<>();
		
		if(issue.getImplantIndent() != null  )
		{
			patientId=issue.getImplantIndent().getPatient().getPatientId();
		}
		else
		{
			patientId=issue.getPatientIndent().getPatient().getPatientId();

		}
		List<OrderDetailsMasterDto> orderDetailsList = new ArrayList<>();
		
			 UnitApplicationConfigurationDto appConfigDto =  iOrderMasterService.getPharmacyServiceId(sessionService.getOrganizationId() ,sessionService.getUnitId());
			 OrderMasterDto orderMasterDto = new OrderMasterDto();
			 orderMasterDto.setPatientId(patientId);
			 orderMasterDto.setOrgId(sessionService.getOrganizationId());
			 orderMasterDto.setOrgUnitId(sessionService.getUnitId());
			 orderMasterDto.setOrderDate(date.getTime());
			 orderMasterDto.setVisitTypeId(request.getVisitType());
			 orderMasterDto.setOrdDoctorId(request.getDoctorId());
			 orderMasterDto.setAdmissionId(request.getAdmissionId());
			 orderMasterDto.setOrderStatus('A');
			 orderMasterDto.setOrderStatusId(1);//1 for pending
			 orderMasterDto.setCreatedDate(date.getTime());
			 orderMasterDto.setUpdatedDate(date.getTime());
			 orderMasterDto.setIsPharmacy('Y');
			 orderMasterDto.setCreatedBy(sessionService.getUserId());
			 orderMasterDto.setUpdatedBy(sessionService.getUserId());
			 orderMasterDto.setSaleId(sale.getId().intValue());
			
				for(SaleDetail saleDetail : sale.getSaleDetailsList())
				{
					netAmount =netAmount+saleDetail.getNetAmount();
					taxAmount =taxAmount +saleDetail.getTaxAmount();
					ordTotalAmt= ordTotalAmt+saleDetail.getTotalAmount();
					
					OrderDetailsMasterDto dto = new OrderDetailsMasterDto();
					dto.setSaleDetailId(saleDetail.getId().intValue());
					dto.setOrderQty(saleDetail.getQuantity());
					dto.setTaxId(saleDetail.getTax()==null?null:saleDetail.getTax().getId());
					dto.setServiceAmt(BigDecimal.valueOf(saleDetail.getRate()));
					//dto.setServiceAmt(BigDecimal.valueOf(saleDetail.getNetAmount()));
					dto.setSelfPayable(BigDecimal.valueOf(saleDetail.getSelfPayable()));
					dto.setCreditPayable(BigDecimal.valueOf(saleDetail.getCreditPayable()));
					dto.setTaxAmount(BigDecimal.valueOf(saleDetail.getTaxAmount()));
					dto.setOrdTotalAmt(BigDecimal.valueOf(saleDetail.getTotalAmount()));
					dto.setTaxPer(saleDetail.getTaxPercentage()==null?BigDecimal.valueOf(0.0):BigDecimal.valueOf(saleDetail.getTaxPercentage()));
					dto.setDiscountValue(saleDetail.getDiscountAmount()==null?BigDecimal.valueOf(0.0):BigDecimal.valueOf(saleDetail.getDiscountAmount()));
					dto.setNetAmount(BigDecimal.valueOf(saleDetail.getNetAmount()));
					dto.setBatchId(saleDetail.getBatch().getId().intValue());
					Integer itemId = saleDetail.getItemId();
					dto.setDrugId(itemId);
					dto.setIsDrug('Y');
//					dto.setSelfPayable(BigDecimal.valueOf(saleDetail.getNetAmount()));
					dto.setCreatedDate(date.getTime());
					dto.setUpdatedDate(date.getTime());
					dto.setOrderSchDate(date.getTime());
					dto.setIsPackage(false);
					dto.setIsCapService('N');
					dto.setIsPackageService('N');
					dto.setIsInvestigationOrder('N');
					dto.setIsProcedureService('N');
					dto.setOrderEmergencyFlag('N');
					dto.setIsProcedure('N');
					dto.setIsProcedureService('N');
					dto.setIsAppointableRadiology('N');
					dto.setOrdCancelled('N');
					dto.setStatus('A');
					dto.setServiceIsBilled(0);
					dto.setCreatedBy(sessionService.getUserId());
					dto.setUpdatedBy(sessionService.getUserId());
					dto.setOrgId(sessionService.getOrganizationId());
					dto.setOrgUnitId(sessionService.getUnitId());
					dto.setOrderDate(date.getTime());
					dto.setServiceRendered(1);
					dto.setPayeeId(saleDetail.getPayeeId());
					List<OrderDetailsPayeeMapperDto> orderDetailsPayeeList = new ArrayList<OrderDetailsPayeeMapperDto>();
					for (SaleDetailPayeeMapper mppr : saleDetail.getSaleDetailPayeeMapperList())
					{
						
						OrderDetailsPayeeMapperDto orderDetailsMpper = new OrderDetailsPayeeMapperDto();
						orderDetailsMpper.setOrganizationId(sessionService.getOrganizationId());
						orderDetailsMpper.setUnitId(sessionService.getUnitId());
						orderDetailsMpper.setStatus('A');
						orderDetailsMpper.setPayeeAmount(mppr.getPayeeAmount());
						orderDetailsMpper.setContractId(mppr.getContractId());
						orderDetailsMpper.setPayeeId(mppr.getPayeeId());
						orderDetailsMpper.setItemId(mppr.getItemId());
						orderDetailsMpper.settSaleDetailsId(mppr.getSaleDetail().getId().intValue());
						orderDetailsPayeeList.add(orderDetailsMpper);
						
						if(map.containsKey(mppr.getPayeeId()))
						{
							OrderDetailsPayeeMapperDto mpprDto =map.get(mppr.getPayeeId());
							map.get(mppr.getPayeeId()).setPayeeAmount(mpprDto.getPayeeAmount().add(mppr.getPayeeAmount()));
						}
						else 
						{
							OrderDetailsPayeeMapperDto  mpprDto= new OrderDetailsPayeeMapperDto();
							mpprDto.setOrganizationId(sessionService.getOrganizationId());
							mpprDto.setUnitId(sessionService.getUnitId());
							mpprDto.setStatus('A');
							mpprDto.setPayeeAmount(mppr.getPayeeAmount());
							mpprDto.setContractId(mppr.getContractId());
							mpprDto.setPayeeId(mppr.getPayeeId());
							map.put(mppr.getPayeeId(), mpprDto);
						}
					}
					
					
					orderDetailsList.add(dto);
					
				}
				
				OrderDetailsMasterDto dtoToAdd = new OrderDetailsMasterDto();
				dtoToAdd.setServiceId(appConfigDto.getPharmacyServiceId());
		/*		dto.setSaleDetailId(saleDetail.getId().intValue());
				dto.setOrderQty(saleDetail.getQuantity());
				dto.setTaxId(saleDetail.getTax().getId());*/
				//dto.setServiceAmt(BigDecimal.valueOf(saleDetail.getRate()));
				dtoToAdd.setTaxAmount(BigDecimal.valueOf(taxAmount));
				dtoToAdd.setOrdTotalAmt(BigDecimal.valueOf(ordTotalAmt));
				//dto.setTaxPer(BigDecimal.valueOf(saleDetail.getTaxPercentage()));
				dtoToAdd.setNetAmount(BigDecimal.valueOf(netAmount));
				dtoToAdd.setServiceAmt(BigDecimal.valueOf(netAmount));
				dtoToAdd.setSelfPayable(BigDecimal.valueOf(totalSelfPayable));
				dtoToAdd.setCreditPayable(BigDecimal.valueOf(totalCreditPayable));
				dtoToAdd.setCreatedDate(date.getTime());
				dtoToAdd.setUpdatedDate(date.getTime());
				dtoToAdd.setOrderSchDate(date.getTime());
				dtoToAdd.setIsPackage(false);
				dtoToAdd.setIsCapService('N');
				dtoToAdd.setIsPackageService('N');
				dtoToAdd.setIsInvestigationOrder('N');
				dtoToAdd.setIsProcedureService('N');
				dtoToAdd.setIsProcedure('N');
				dtoToAdd.setIsProcedureService('N');
				dtoToAdd.setOrderEmergencyFlag('N');
				dtoToAdd.setIsAppointableRadiology('N');
				dtoToAdd.setIsDrug('N');
				dtoToAdd.setOrdCancelled('N');
				dtoToAdd.setStatus('A');
				dtoToAdd.setServiceIsBilled(0);
				dtoToAdd.setCreatedBy(sessionService.getUserId());
				dtoToAdd.setUpdatedBy(sessionService.getUserId());
				dtoToAdd.setOrgId(sessionService.getOrganizationId());
				dtoToAdd.setOrgUnitId(sessionService.getUnitId());
				dtoToAdd.setOrderDate(date.getTime());
				dtoToAdd.setServiceRendered(1);
				
				//FinalList
				List<OrderDetailsPayeeMapperDto> finalMpprList = new ArrayList<OrderDetailsPayeeMapperDto>();
				for (Map.Entry<Integer, OrderDetailsPayeeMapperDto> entry : map.entrySet()) {
					entry.getValue().setServiceId(appConfigDto.getPharmacyServiceId());
					finalMpprList.add(entry.getValue());
				}
				dtoToAdd.setOrderDetailsPayeeMapperDtosList(finalMpprList);
				orderDetailsList.add(dtoToAdd);
				
				orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
				
				
				BillingMasterDto billDto = new BillingMasterDto();
				billDto.setPatientId(patientId);
				billDto.setEncounterId(orderMasterDto.getAdmissionId());
				billDto.setCreatedBy(1);
				billDto.setVisitTypeId(orderMasterDto.getVisitTypeId());
				billDto.setOrganisationId(orderMasterDto.getOrgId());
				billDto.setUnitId(orderMasterDto.getOrgUnitId());
				iMPackageMasterService.packageConsumtionOfItem(billDto, orderMasterDto);
		
		
		issue.getImplantIndent().setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
		issue.getImplantIndent().setUpdatedDate(TimeZoneComponent.getDateByzone(issue.getUnitId()));
		issue.getImplantIndent().setUpdatedByUser(request.getUserId());
		issue.setUpdatedDate(TimeZoneComponent.getDateByzone(issue.getUnitId()));
		issue.setUpdatedByUser(request.getUserId());
		Issue issueResponse = PatientConsumptionDao.updateIssue(issue);
		CommonResponse response = new CommonResponse();
		response.setId(issueResponse.getId().longValue());
		response.setMessage("Patient Consumtion saved successfully");
		return response;
		
		}
		catch(Exception e){
			logger.error("patientConsumptionSave failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	private Batch BatchStoreTransaction(Integer storeId, Long batchId, Integer quantity, UomType uomType, Integer organizationId, Integer unitId, Integer userId, IssueDetail d) {

		screen = GlobalDao.getScreen(screenId);

		// get batch needs to be issued
		Batch batch = GlobalDao.getBatch(storeId, batchId,unitId);

		// Get existing batch store stock
		Integer avaialableStoreStock = GlobalDao.getStoreStockByBatch(storeId, batchId,0);//0 itemId

		// Get lease quantity
		Integer leaseQuantity = Util.GetLeaseQuantity(quantity, batch.getBatchUomList(), uomType);

		Integer balanceQuantity = avaialableStoreStock - leaseQuantity;

		// Deduct issued quantity from the batch store stock
		batch.getStoreStockList().getFirst().setAvailableQty(balanceQuantity);

		// Deduct issued quantity from the batch
		Integer currentQuantity = batch.getCurrentQuantity() - leaseQuantity;
		batch.setCurrentQuantity(currentQuantity);

		// Add stock transaction
		StockTransaction stockTransaction=new StockTransaction(balanceQuantity, screen, screen.getCode(), leaseQuantity,
				batch.getStoreStockList().getFirst().getStore(), d.getId());
//		sessionService.setEntityDefaults(stockTransaction);
		stockTransaction.setCreateDefaults(organizationId,unitId, userId);
		batch.addStockTransaction(stockTransaction);

		return batch;
	}

	

}
