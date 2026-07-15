package com.param.pharmacy.ip.service.pharmacology;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.doctor.Doctor;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.AssignPicker;
import com.param.entity.model.pharmacy.AssignPickerDetail;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.dto.global.DoctorSpecialityResp;
import com.param.global.service.global.IGlobalService;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.pharmacology.IPharmacologyValidationDao;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyUpdate;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyUpdateItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.dto.pharmacology.PrescriptionFrequencyDetailResp;
import com.param.pharmacy.master.unit.dao.applicationConfiguration.IApplicationConfigurationDao;
import com.param.pharmacy.prescription.dto.PrescriptionItem;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("pharmacologyValidationService")
@Transactional
public class PharmacologyValidationService implements IPharmacologyValidationService {
	
	@Autowired
	private SessionService sessionService;
	
	@Autowired
	private IPharmacologyValidationDao pharmacologyValidationDao;
	
	@Autowired
	IEmrService EmrService;
	
	@Autowired
	IApplicationConfigurationDao iApplicationConfigurationDao;
	
	@Autowired
	IGlobalService GlobalService;
	
	private Integer screenId = Screen.PharmacologyValidation.getScreen();
	
	@Override
	@Transactional
	public CommonDropdown getDropdownList()
	{
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}
		
		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}
		
		return response;
	}
	
	@Autowired
	private IGlobalDao GlobalDao;
	
	
	private Boolean checkStatMedication(Long prescriptionId) {
		List<PrescriptionDetail> prescriptionDetails = pharmacologyValidationDao.getPrescriptionDetailsList(prescriptionId);
		
		if(prescriptionDetails != null && !prescriptionDetails.isEmpty())
			return true;
	
		return false;
	}


	@Override
	@Transactional
	public List<PharmacologyValidationSearchResult> pharmacologyValidationSearchBP(PharmacologyValidationSearch pharmacologyValidationSearch)
	{
		List<PharmacologyValidationSearchResult> pharmacologyValidationSearchList = new ArrayList<PharmacologyValidationSearchResult>();
		List<PharmacologyValidationSearchResult> pharmacologyValidationSearchListResp = new ArrayList<PharmacologyValidationSearchResult>();
		pharmacologyValidationSearch.setToDate((new DateTime(pharmacologyValidationSearch.getToDate())).plusDays(1).toDate());

		pharmacologyValidationSearchList = pharmacologyValidationDao.pharmacologyValidationSearchBP(pharmacologyValidationSearch, screenId);
		
		if(!pharmacologyValidationSearchList.isEmpty()) {
			List<Integer> doctorIds = pharmacologyValidationSearchList.stream().map(PharmacologyValidationSearchResult::getDoctorId)
					.collect(Collectors.toList());
			//List<DoctorSpecialityResp> doctorSpecialityRespsAll = GlobalDao.doctorSearchByIdValueBP(doctorIds);
			for(PharmacologyValidationSearchResult pharmacologyValidation : pharmacologyValidationSearchList)
			{
				if(checkStatMedication(pharmacologyValidation.getId())) {
					if(pharmacologyValidation.getAdmissionId() != null)
					{
						List<DoctorSpecialityResp> doctorSpeciality = new ArrayList<DoctorSpecialityResp>();
						/*if(!doctorSpecialityRespsAll.isEmpty()){	
							doctorSpeciality = doctorSpecialityRespsAll.stream().filter(x -> x.getDoctorId().equals(pharmacologyValidation.getDoctorId())).collect(Collectors.toList());
							if(doctorSpeciality != null && !doctorSpeciality.isEmpty())
								pharmacologyValidation.setDoctorDetail(pharmacologyValidation.getDoctorDetail() + " | " + doctorSpeciality.get(0).getSpeciality());
						}*/
						Integer age = Util.getAgeByBirthDate(pharmacologyValidation.getPatientDob().toLocalDateTime().toLocalDate());
						pharmacologyValidation.setPatientDetail(pharmacologyValidation.getPatientDetail() + " | " + age);
						String userName =GlobalDao.getUserNameByUserId(pharmacologyValidation.getAddedByUser(), pharmacologyValidationSearch.getUnitId());
						pharmacologyValidation.setUserName(userName);
						pharmacologyValidationSearchListResp.add(pharmacologyValidation);
					}
				}
			}
		}
		return pharmacologyValidationSearchListResp;
	}
	
	@Override
	@Transactional
	public PharmacologyValidationDetails pharmacologyValidationDetailsBP(Long id, Integer unitId)
	{
		PharmacologyValidationDetails pharmacologyValidationDetails = new PharmacologyValidationDetails();
		List<PharmacologyItem> pharmacologyItemList = new ArrayList<PharmacologyItem>();

		pharmacologyValidationDetails = pharmacologyValidationDao.getPrescriptionByIdBP(id,unitId);
		
		if(pharmacologyValidationDetails.getAdmissionId() != null){
			
			/*List<DoctorSpecialityResp> docSpeciality = new ArrayList<DoctorSpecialityResp>();
			docSpeciality = GlobalDao.doctorSearchByIdValueBP(pharmacologyValidationDetails.getDoctorId());
			if(!docSpeciality.isEmpty())
				pharmacologyValidationDetails.setDoctorDetail(pharmacologyValidationDetails.getDoctorDetail() + " | " + docSpeciality.get(0).getSpeciality());*/
			
			Integer age = Util.getAgeByBirthDate(pharmacologyValidationDetails.getPatientDob().toLocalDateTime().toLocalDate());
			pharmacologyValidationDetails.setPatientDetail(pharmacologyValidationDetails.getPatientDetail() + " | " + age);
		}
		
		List<PharmacologyItem> pharmacologyItemsAll = pharmacologyValidationDao.pharmacologyValidationDetailsBP(id);
		List<Long> prescriptionDetailIds = pharmacologyItemsAll.stream().map(x -> x.getId()).collect(Collectors.toList());
		List<PrescriptionFrequencyDetailResp> prescriptionFrequencyDetailRespAll = pharmacologyValidationDao.getPrescriptionFrequencyDetailsBP(prescriptionDetailIds);
		for(PharmacologyItem pharmacologyItem : pharmacologyItemsAll) {
			List<PrescriptionFrequencyDetailResp> prescriptionFrequencyDetailRespSpecific = prescriptionFrequencyDetailRespAll.stream()
					.filter(x -> x.getPrescriptionDetaild().equals(pharmacologyItem.getId())).collect(Collectors.toList());
			for(PrescriptionFrequencyDetailResp prescriptionFrequencyDetail : prescriptionFrequencyDetailRespSpecific) {
				pharmacologyItem.setFrequency(prescriptionFrequencyDetail.getFreqDescription());
				pharmacologyItem.setDose(prescriptionFrequencyDetail.getDose());
				pharmacologyItem.setStartDate(prescriptionFrequencyDetail.getStartDate());
				pharmacologyItem.setStopDate(prescriptionFrequencyDetail.getStopDate()); 
				pharmacologyItem.setDoseUnit(prescriptionFrequencyDetail.getUnit());
				pharmacologyItemList.add(pharmacologyItem);
			}
		}
		pharmacologyValidationDetails.setItems(pharmacologyItemList);
		return pharmacologyValidationDetails;
	}
	
	
	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public Object pharmacologyValidationSave(PharmacologyUpdate pharmacologyUpdate)
	{
			Prescription prescription = pharmacologyValidationDao.getPrescriptionById(pharmacologyUpdate.getId());
		
			prescription.setIsValidate(pharmacologyUpdate.isValidate());
			prescription.setStatus(new Status(pharmacologyUpdate.getStatusId()));
			
			ApplicationConfiguration ac = iApplicationConfigurationDao.getApplicationConfigurationByUnitId(pharmacologyUpdate.getUnitId());

			if(ac != null 
					&& ac.getAssignPickerRequired() != null)
			{
				if(ac.getAssignPickerRequired())
				{
					this.saveAssignPickerDate(pharmacologyUpdate,prescription);
				}
				else if(!ac.getAssignPickerRequired())
				{
					this.saveIssueAndIssueDetails(pharmacologyUpdate,prescription,false);
					this.saveIssueAndIssueDetails(pharmacologyUpdate,prescription,true);
				}
			}
			else
			{
				this.saveAssignPickerDate(pharmacologyUpdate,prescription);
			}
			
			CommonResponse response = new CommonResponse();
			response.setId((prescription.getId().longValue()));
			response.setMessage("Pharmacology Validation saved successfully");
		

		return response;
	
	}


	private void saveIssueAndIssueDetails(PharmacologyUpdate pharmacologyUpdate, Prescription prescription, boolean isNarcotics) {
		Integer storeId=pharmacologyUpdate.getStoreId() != null ?pharmacologyUpdate.getStoreId() : 0;
		String issuetNumber = GlobalService.getBillNoByGeneratedCode(storeId,
				pharmacologyUpdate.getUnitId(), Screen.PendingIssues.getScreen());
		Doctor doctor = null;
		if (pharmacologyUpdate.getDoctorId() != null && pharmacologyUpdate.getDoctorId() > 0) {
			doctor = new Doctor(pharmacologyUpdate.getDoctorId());
		}
		Issue issue = new Issue(issuetNumber!=null ?issuetNumber :"ISS-001",  
				(pharmacologyUpdate.getId() != null) ? new Prescription(pharmacologyUpdate.getId()) : null,
				(pharmacologyUpdate.getVisitType() != null) ? new VisitType(pharmacologyUpdate.getVisitType())  :null,
				doctor,
				new Status(com.param.common.constants.Status.Pending.getStatus()));
		if(pharmacologyUpdate.getIssueId() != null) {
			issue.setId(pharmacologyUpdate.getIssueId());
		}
		
		issue.setIsImplant(false);
		issue.setIsConsignment(false);
		issue.setIsNarcotics(isNarcotics);
		issue.setInfusionTolalVolume(pharmacologyUpdate.getInfusionTotalVolume()!= null ? pharmacologyUpdate.getInfusionTotalVolume():0);
		issue.setCreateDefaults(pharmacologyUpdate.getOrganizationId()!=null ? pharmacologyUpdate.getOrganizationId() : sessionService.getOrganizationId(),pharmacologyUpdate.getUnitId() !=null ? pharmacologyUpdate.getUnitId() :sessionService.getUnitId() ,pharmacologyUpdate.getUserId()!=null ? pharmacologyUpdate.getUserId() : sessionService.getUserId());
		
		if(pharmacologyUpdate.getAdmissionId() != null)
		{
			issue.setAdmission(new Admission(pharmacologyUpdate.getAdmissionId()));
		}
		Integer saveFlag = 0 ;
		for (PrescriptionDetail prescriptionDeatil : prescription.getPrescriptionDetailsList())
		{
			for(PharmacologyUpdateItem pharmacologyItem : pharmacologyUpdate.getItems())
			{
				if(pharmacologyItem.getId().equals(prescriptionDeatil.getId()) && pharmacologyItem.getIsNarcotic() == isNarcotics)
				{
					prescriptionDeatil.setIsIntervention(pharmacologyItem.isIntervention());
					prescriptionDeatil.setStatus(new Status(pharmacologyItem.getItemStatusId()));
					prescriptionDeatil.setRemark(pharmacologyItem.getRemark());
					prescriptionDeatil.setUpdatedDate(TimeZoneComponent.getDateByzone(pharmacologyUpdate.getUnitId()));
					prescriptionDeatil.setUpdatedByUser(pharmacologyUpdate.getUserId()!=null ? pharmacologyUpdate.getUserId() : sessionService.getUserId());
					
					if(!pharmacologyItem.isIntervention()) {
						saveFlag = 1;
								IssueDetail issueDetail = new IssueDetail( (pharmacologyItem.getId() != null) ? new PrescriptionDetail(pharmacologyItem.getId()) : null,
										new Status(com.param.common.constants.Status.Pending.getStatus()));
								issue.setStore(new Store(pharmacologyUpdate.getStoreId()!=null ? pharmacologyUpdate.getStoreId() : (sessionService.getStoreId() !=null ? sessionService.getStoreId() :1)));
								issueDetail.setIsSubstitute(false);
								issueDetail.setIsFromAssignPicker(false);
								issueDetail.setIsNarcoticSubstitute(true);
								issueDetail.setIsFromAssignPicker(false);
								issueDetail.setCreateDefaults(pharmacologyUpdate.getOrganizationId()!=null ? pharmacologyUpdate.getOrganizationId() : sessionService.getOrganizationId(),pharmacologyUpdate.getUnitId() !=null ? pharmacologyUpdate.getUnitId() :sessionService.getUnitId() ,pharmacologyUpdate.getUserId()!=null ? pharmacologyUpdate.getUserId() : sessionService.getUserId());
								issue.addIssueDetail(issueDetail);
					}
							
					PrescriptionItem prescriptionItem = new PrescriptionItem();
					if(prescriptionDeatil.getItem() != null) {
						prescriptionItem.setItem_id(prescriptionDeatil.getItem().getId());
					}else {
						prescriptionItem.setItem_id(null);
					}
					
					prescriptionItem.setIs_intervention(prescriptionDeatil.getIsIntervention());
				}
			}
		}
		prescription.setUpdatedDate(TimeZoneComponent.getDateByzone(pharmacologyUpdate.getUnitId()));
		prescription.setUpdatedByUser(pharmacologyUpdate.getUserId()!=null ? pharmacologyUpdate.getUserId() : sessionService.getUserId());
		
		if(saveFlag == 1) {
			pharmacologyValidationDao.updatePrescription(prescription);
			if(pharmacologyUpdate.isValidate() == true)
			{
					pharmacologyValidationDao.saveIssue(issue);
			}	
		}
		
	}


	private void saveAssignPickerDate(PharmacologyUpdate pharmacologyUpdate, Prescription prescription) {
		
		AssignPicker assignPickerObject = new AssignPicker(new Prescription(pharmacologyUpdate.getId()),
				new Status(com.param.common.constants.Status.Pending.getStatus()));
		//sessionService.setEntityDefaults(assignPickerObject);
		assignPickerObject.setCreateDefaults(pharmacologyUpdate.getOrganizationId()!=null ? pharmacologyUpdate.getOrganizationId() : sessionService.getOrganizationId(),pharmacologyUpdate.getUnitId() !=null ? pharmacologyUpdate.getUnitId() :sessionService.getUnitId() ,pharmacologyUpdate.getUserId()!=null ? pharmacologyUpdate.getUserId() : sessionService.getUserId());
		

		for (PrescriptionDetail prescriptionDeatil : prescription.getPrescriptionDetailsList())
		{
			for(PharmacologyUpdateItem pharmacologyItem : pharmacologyUpdate.getItems())
			{
				if(pharmacologyItem.getId().equals(prescriptionDeatil.getId()))
				{
					prescriptionDeatil.setIsIntervention(pharmacologyItem.isIntervention());
					prescriptionDeatil.setStatus(new Status(pharmacologyItem.getItemStatusId()));
					prescriptionDeatil.setRemark(pharmacologyItem.getRemark()!=null ? pharmacologyItem.getRemark() : null);
					prescriptionDeatil.setUpdatedDate(TimeZoneComponent.getDateByzone(pharmacologyUpdate.getUnitId()));
					prescriptionDeatil.setUpdatedByUser(pharmacologyUpdate.getUserId()!=null ? pharmacologyUpdate.getUserId() : sessionService.getUserId());
					
					if(!pharmacologyItem.isIntervention()) {
						
					AssignPickerDetail assignPickerDetails = new AssignPickerDetail( new PrescriptionDetail(pharmacologyItem.getId()),
						new Status(com.param.common.constants.Status.Pending.getStatus()));
					//	sessionService.setEntityDefaults(assignPickerDetails);
					assignPickerDetails.setCreateDefaults(pharmacologyUpdate.getOrganizationId()!=null ? pharmacologyUpdate.getOrganizationId() : sessionService.getOrganizationId(),pharmacologyUpdate.getUnitId() !=null ? pharmacologyUpdate.getUnitId() :sessionService.getUnitId() ,pharmacologyUpdate.getUserId()!=null ? pharmacologyUpdate.getUserId() : sessionService.getUserId());
					
						assignPickerObject.addAssignPickerDetail(assignPickerDetails);
					}
							
					PrescriptionItem prescriptionItem = new PrescriptionItem();
					prescriptionItem.setItem_id(prescriptionDeatil.getItem().getId());
					prescriptionItem.setIs_intervention(prescriptionDeatil.getIsIntervention());
				}
			}
		}
		prescription.setUpdatedDate(TimeZoneComponent.getDateByzone(pharmacologyUpdate.getUnitId()));
		prescription.setUpdatedByUser(pharmacologyUpdate.getUserId()!=null ? pharmacologyUpdate.getUserId() : sessionService.getUserId());
		
		if(pharmacologyUpdate.isValidate() == true)
		{
				pharmacologyValidationDao.saveAssignPicker(assignPickerObject);
		}
		pharmacologyValidationDao.updatePrescription(prescription);
	}
}