package com.param.pharmacy.nursing.workstation.service.drugAdministration;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.master.global.model.ReasonMaster;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.Admission;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.FrequencyDetail;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.opd.Encounter;
import com.param.entity.model.patient.PatientRegistration;
import com.param.entity.model.pharmacy.DrugAdministration;
import com.param.entity.model.pharmacy.DrugAdministrationDetail;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.pharmacy.dto.AntibioticTestDoseDto;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.global.unit.dto.ListOfAllergyPatientMapperDto;
import com.param.global.utility.Util;
import com.param.opd.coversheet.dao.IAllergyPatientMapperDao;
import com.param.pharmacy.master.global.dao.frequency.IFrequencyDao;
import com.param.pharmacy.nursing.workstation.dao.drugAdministration.IAntibioticTestDoseDao;
import com.param.pharmacy.nursing.workstation.dao.drugAdministration.IDrugAdministrationDao;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DoseTime1;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationDropdown;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationItem;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationRequest;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationRequestItem;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.DrugAdministrationResponse;
import com.param.pharmacy.nursing.workstation.dto.drugAdministration.Reason;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service("drugAdministrationService")
@SuppressWarnings("rawtypes")
@Transactional
public class DrugAdministrationService implements IDrugAdministrationService ,ICommonConstants{
	@Autowired
	private SessionService sessionService;
	@Autowired
	private IDrugAdministrationDao drugAdministrationDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IFrequencyDao FrequencyDao;
	
	@Autowired
	IAntibioticTestDoseDao iAntibioticTestDoseDao;
	
	@Autowired
	private IAllergyPatientMapperDao iAllergyPatientMapperDao;

	private static final Logger logger = Logger.getLogger(DrugAdministrationService.class);

	@Override
	@Transactional
	public DrugAdministrationDropdown drugAdministrationDropdown() {
		DrugAdministrationDropdown response = new DrugAdministrationDropdown();

		for (ReasonMaster reason : GlobalDao.getReasonList()) {
			response.addReason(new Reason(reason.getReasonMasterId(), reason.getReasonDesc()));
		}
		return response;
	}

	@Override
	@Transactional
	public DrugAdministrationResponse drugAdministrationSearch(DrugAdministrationRequest request) {
		List<Issue> issue = drugAdministrationDao.getPatientDetails(request);
		DrugAdministrationResponse drugAdministrationResponse = new DrugAdministrationResponse();
		List<DrugAdministrationItem> items = new ArrayList<DrugAdministrationItem>();
		List<DrugAdministrationItem> statItems = null;
		DrugAdministrationItem responseObj = null;
		DrugAdministrationItem responseObj1 = null;
		DrugAdministrationItem responseObj2 = null;
		if (!issue.isEmpty()) {
			String doctorDetail="";
			if(issue.get(0).getDoctor()!=null && issue.get(0).getSpecialityMaster()!=null) {
			doctorDetail = issue.get(0).getDoctor().getFirstName() + " "
					+ issue.get(0).getDoctor().getMiddleName() + " " + issue.get(0).getDoctor().getLastName() + " | "
					+ issue.get(0).getSpecialityMaster().getSpecialityName() ;
			}
			if (issue.get(0).getAdmission() != null) {
				String wardName = "";
				String bedNumber = "";
				Date doa = null;
				Date pdd = null;
				AdmissionDetail admissionDetail = GlobalDao
						.getPatientWardBedDetails(issue.get(0).getAdmission().getAdmissionId());
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
						issue.get(0).getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = issue.get(0).getAdmission().getPatient().getPrefix().getPrefixCode() + " "
						+ issue.get(0).getAdmission().getPatient().getFirstName() + " "
						+ issue.get(0).getAdmission().getPatient().getMiddleName() + " "
						+ issue.get(0).getAdmission().getPatient().getLastName() + " | " + age + " | "
						+ issue.get(0).getAdmission().getPatient().getGender().getGenderCode();

				drugAdministrationResponse = new DrugAdministrationResponse(
						issue.get(0).getAdmission().getPatient().getPatientId(),
						issue.get(0).getAdmission().getPatient() != null
								? issue.get(0).getAdmission().getPatient().getUhidNumber()
								: null,
						patientDetail, doctorDetail, issue.get(0).getAdmission().getPatient().getDob(),
						issue.get(0).getPrescription() != null ? issue.get(0).getPrescription().getAddedDate() : null);
				drugAdministrationResponse.setWardName(!wardName.equals("") ? wardName : null);
				drugAdministrationResponse.setBedNo(!bedNumber.equals("") ? bedNumber : null);
				drugAdministrationResponse.setDoa(doa != null ? doa : null);
				drugAdministrationResponse.setPdd(pdd != null ? pdd : null);
				drugAdministrationResponse.setAdmissionId(issue.get(0).getAdmission().getAdmissionId());
				drugAdministrationResponse.setVisitType(issue.get(0).getAdmission().getVisitType().getVisitTypeCode());
				drugAdministrationResponse.setVisitTypeId(issue.get(0).getAdmission().getVisitType().getVisitTypeId());
				drugAdministrationResponse.setPrescriptionId(
						issue.get(0).getPrescription() != null ? issue.get(0).getPrescription().getId() : null);
			} else if (issue.get(0).getEncounter() != null) {
				Integer age = Util.getAgeByBirthDate(
						issue.get(0).getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = issue.get(0).getEncounter().getPatient().getPrefix().getPrefixCode() + " "
						+ issue.get(0).getEncounter().getPatient().getFirstName() + " "
						+ issue.get(0).getEncounter().getPatient().getMiddleName() + " "
						+ issue.get(0).getEncounter().getPatient().getLastName() + " | " + age + " | "
						+ issue.get(0).getEncounter().getPatient().getGender().getGenderCode();

				drugAdministrationResponse = new DrugAdministrationResponse(
						issue.get(0).getEncounter().getPatient() != null
								? issue.get(0).getEncounter().getPatient().getPatientId()
								: null,
						issue.get(0).getEncounter().getPatient() != null
								? issue.get(0).getEncounter().getPatient().getUhidNumber()
								: null,
						patientDetail, doctorDetail, issue.get(0).getEncounter().getPatient().getDob(),
						issue.get(0).getPrescription() != null ? issue.get(0).getPrescription().getAddedDate() : null);
				drugAdministrationResponse.setEncounterId(
						issue.get(0).getEncounter() != null ? issue.get(0).getEncounter().getEncounterId() : null);
				drugAdministrationResponse.setVisitType(issue.get(0).getEncounter().getVisitType() != null
						? issue.get(0).getEncounter().getVisitType().getVisitTypeCode()
						: null);
				drugAdministrationResponse.setVisitTypeId(issue.get(0).getEncounter().getVisitType() != null
						? issue.get(0).getEncounter().getVisitType().getVisitTypeId()
						: null);
			}

//			items = drugAdministrationDao.patientIssueReturnSearch(request);
			items = drugAdministrationDao.patientIssueReturnForDrugAdministration(request);
		} else if (issue.isEmpty()) {

			List<Prescription> prescriptions = drugAdministrationDao.getPatientDetailsPrescription(request);

			if (prescriptions != null && !prescriptions.isEmpty()) {
				/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
						.doctorSearchByIdValue(prescriptions.get(0).getDoctor().getDoctorId());
				String drSpeciality = "";
				if (doctorSpeciality.size() > 0) {
					drSpeciality = doctorSpeciality.get(0).getSpeciality().getSpecialityName();
				}*/
				String doctorDetail="";
				if(prescriptions.get(0).getDoctor()!=null && prescriptions.get(0).getSpecialityMaster()!=null) {
				 doctorDetail = prescriptions.get(0).getDoctor().getFirstName() + " "
						+ prescriptions.get(0).getDoctor().getMiddleName() + " "
						+ prescriptions.get(0).getDoctor().getLastName() + " | " + prescriptions.get(0).getSpecialityMaster().getSpecialityName();
				}
				if (prescriptions.get(0).getAdmission() != null) {
					String wardName = "";
					String bedNumber = "";
					Date doa = null;
					Date pdd = null;
					AdmissionDetail admissionDetail = GlobalDao
							.getPatientWardBedDetails(prescriptions.get(0).getAdmission().getAdmissionId());
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
							prescriptions.get(0).getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = prescriptions.get(0).getAdmission().getPatient().getPrefix().getPrefixCode()
							+ " " + prescriptions.get(0).getAdmission().getPatient().getFirstName() + " "
							+ prescriptions.get(0).getAdmission().getPatient().getMiddleName() + " "
							+ prescriptions.get(0).getAdmission().getPatient().getLastName() + " | " + age + " | "
							+ prescriptions.get(0).getAdmission().getPatient().getGender().getGenderCode();

					drugAdministrationResponse = new DrugAdministrationResponse(
							prescriptions.get(0).getAdmission().getPatient().getPatientId(),
							prescriptions.get(0).getAdmission().getPatient() != null
									? prescriptions.get(0).getAdmission().getPatient().getUhidNumber()
									: null,
							patientDetail, doctorDetail, prescriptions.get(0).getAdmission().getPatient().getDob(),
							prescriptions.get(0) != null ? prescriptions.get(0).getAddedDate() : null);
					drugAdministrationResponse.setWardName(!wardName.equals("") ? wardName : null);
					drugAdministrationResponse.setBedNo(!bedNumber.equals("") ? bedNumber : null);
					drugAdministrationResponse.setDoa(doa != null ? doa : null);
					drugAdministrationResponse.setPdd(pdd != null ? pdd : null);
					drugAdministrationResponse.setAdmissionId(prescriptions.get(0).getAdmission().getAdmissionId());
					drugAdministrationResponse
							.setVisitType(prescriptions.get(0).getAdmission().getVisitType().getVisitTypeCode());
					drugAdministrationResponse
							.setVisitTypeId(prescriptions.get(0).getAdmission().getVisitType().getVisitTypeId());
					drugAdministrationResponse
							.setPrescriptionId(prescriptions.get(0) != null ? prescriptions.get(0).getId() : null);
				}
			}
		}
		List<DrugAdministrationItem> itemList = new ArrayList<DrugAdministrationItem>();

		//statItems = drugAdministrationDao.patientIssueReturnSearchStatMedication(request);
		if (statItems != null)
			items.addAll(statItems);

		for (DrugAdministrationItem item : items) {
			request.setId(item.getId());
			request.setOrderQty(item.getOrderQty());
			request.setPrescriptionId(item.getPrescriptionId());
			request.setToDate((new DateTime(request.getDate())).plusDays(1).toDate());
			List<DoseTime1> doseTimeListFinal=new ArrayList<DoseTime1>();
			List<DoseTime1> doseTimeList = drugAdministrationDao.getDrugAdministrationItem(request);
			responseObj = drugAdministrationDao.checkReturnQuantity(request);
			responseObj1  = drugAdministrationDao.checkRejectedIssueQuantity(request);
			responseObj2  = drugAdministrationDao.checkReturnedIssueQuantity(request);
			if(responseObj2 != null) {
				for (DoseTime1 list : doseTimeList) {
					
				}
			}
			item.setAcceptQty(responseObj.getAcceptQty());
			item.setRejectedQuantity(responseObj1.getRejectedQuantity());
			item.setReturnQuantity(responseObj2.getReturnQuantity());
			item.setDoseTime(doseTimeList);
			itemList.add(item);
		}
		drugAdministrationResponse.setItems(itemList);
		return drugAdministrationResponse;
	}

	@Override
	@Transactional
	public CommonResponse drugAdministrationSave(DrugAdministrationRequest request) throws ParseException {
		DrugAdministration drugAdministration = new DrugAdministration(new VisitType(request.getVisitTypeId()),
				new PatientRegistration(request.getPatientId()),
				request.getPrescriptionId() != null ? new Prescription(request.getPrescriptionId()) : null);
		//sessionService.setEntityDefaults(drugAdministration);
		drugAdministration.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		if (request.getAdmissionId() != null) {
			drugAdministration.setAdmission(new Admission(request.getAdmissionId()));
		} else if (request.getEncounterId() != null) {
			drugAdministration.setEncounter(new Encounter(request.getEncounterId()));
		}
		for (DrugAdministrationRequestItem items : request.getItems()) {
			String sDate = items.getAdministrationTime();
			DateFormat formatter;
			formatter = new SimpleDateFormat("HH:mm");
			Date date = new Date();
			if(sDate != null) {
				date = (Date) formatter.parse(sDate);
			}
			Instant timeStampDate1 = null;
			Instant timeStampDate2 = null;
			Date startDate=null;
			Date stopDate=null;
			
			if(items.getStartAdministrationTime() != null && items.getStopAdministrationTime() != null) {
				LocalTime time1 = LocalTime.parse(items.getStartAdministrationTime());
				LocalDate today = LocalDate.now();
				timeStampDate1 = time1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				LocalTime time2 = LocalTime.parse(items.getStopAdministrationTime());
				timeStampDate2 = time2.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

//				String startTime = items.getStartAdministrationTime();
//				String stopTime = items.getStopAdministrationTime();
				startDate = GlobalCommonDateUtils.getDate(items.getStartAdministrationDate(), "yyyy-mm-dd HH:mm:ss");
				stopDate =  GlobalCommonDateUtils.getDate(items.getStopAdministrationDate(), "yyyy-mm-dd HH:mm:ss");
//				Date  startTime1= (Date) formatter.parse(startTime);
//				Date stopTime1 = (Date) formatter.parse(stopTime);
//				timeStampDate1 = new Timestamp(startTime1.getTime());
//				timeStampDate2 = new Timestamp(stopTime1.getTime());
			}
			
			
//			Date startDate1 = (Date) formatter.parse(startDate);
//			Date stopDate1 = (Date) formatter.parse(stopDate);
			java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
			
//			java.sql.Timestamp timeStampDate3 = new Timestamp(startDate1.getTime());
//			java.sql.Timestamp timeStampDate4 = new Timestamp(stopDate1.getTime());
			DrugAdministrationDetail drugAdministrationDetail = 
					new DrugAdministrationDetail(
					new PrescriptionDetail(items.getPrescriptionDetailId()), timeStampDate,
					(items.getReasonId() != null) ? new ReasonMaster(items.getReasonId()) : null,
					(items.getNote() != null) ? items.getNote() : null,
			new FrequencyDetail(items.getFrequencyDetailId()), items.getBatchId()!=null ? new Batch(items.getBatchId().longValue()) : null ,
					(timeStampDate1!= null ? timeStampDate1 : null),(timeStampDate2!= null ? timeStampDate2 : null),
					(startDate!= null ? startDate : null),(stopDate!= null ? stopDate : null));
		
			drugAdministrationDetail.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			drugAdministrationDetail.setAddedDate(items.getAddedDate());
			drugAdministration.addDrugAdministrationDetail(drugAdministrationDetail);
			
		//	sessionService.setEntityDefaults(drugAdministrationDetail);
			
			
		}
		
		drugAdministrationDao.drugAdministrationSave(drugAdministration);
		CommonResponse response = new CommonResponse();
		response.setId(drugAdministration.getId().longValue());
		response.setMessage("Drug Administration saved successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse drugAdministrationUpdate(DrugAdministrationRequest request) throws ParseException {
		for (DrugAdministrationRequestItem items : request.getItems()) {
			DrugAdministrationDetail drugAdministrationDetail = drugAdministrationDao
					.getDrugAdministrationDetailById(items.getId());
			String sDate = items.getAdministrationTime();
			DateFormat formatter;
			formatter = new SimpleDateFormat("HH:mm");
			Date date = (Date) formatter.parse(sDate);
			java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
			drugAdministrationDetail.updateDrugAdministrationDetail(
					new PrescriptionDetail(items.getPrescriptionDetailId()), timeStampDate,
					(items.getReasonId() != null) ? new ReasonMaster(items.getReasonId()) : null,
					(items.getNote() != null) ? items.getNote() : null,
					new FrequencyDetail(items.getFrequencyDetailId()));

			drugAdministrationDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(request.getUnitId()));
			drugAdministrationDetail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

			drugAdministrationDao.drugAdministrationDetailUpdate(drugAdministrationDetail);
		//	sessionService.setEntityDefaults(drugAdministrationDetail);
			drugAdministrationDetail.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());


		}
		CommonResponse response = new CommonResponse();
		response.setMessage("Drug Administration updated successfully");

		return response;
	}

	@Override
	@Transactional
	public DrugAdministrationDropdown getreasonDropdownList(Integer orgId) {

		DrugAdministrationDropdown response = new DrugAdministrationDropdown();

		for (ReasonMaster reason : GlobalDao.getreasonDropdownList(orgId)) {
			response.addReason(new Reason(reason.getReasonMasterId(), reason.getReasonDesc()));
		}
		return response;

	}

	@Override
	@Transactional
	public Response getBatchDetailsByPresDetailsId(Integer id) throws ParseException {
		try {
			
			return GlobalDao.getBatchDetailsByPresDetailsId(id);
	
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Response saveAntibioticTestDose(AntibioticTestDoseDto dto) throws ApplicationException {

		try {
			Response res =  iAntibioticTestDoseDao.saveAntibioticTestDose(dto);
			
			if(dto.getStopAdministration() != null && dto.getStopAdministration() == true) {
				 iAntibioticTestDoseDao.StopAdministration(dto);
			}
			
			if(dto.getObservationDate() != null ) {
				
				AllergyPatientMapperDto alleDto = new AllergyPatientMapperDto();
				
				alleDto.setAdmissionId(dto.getAdmissionId() != null ? dto.getAdmissionId() : null);
				alleDto.setEncounterId(dto.getEncounterId() != null ? dto.getEncounterId() : null);
				alleDto.setUpdatedBy(dto.getUpdatedBy());
				alleDto.setCreatedBy(dto.getCreatedBy());
				alleDto.setCreatedDate(dto.getCreatedDate());
				alleDto.setUpdatedDate(dto.getUpdatedDate());
				alleDto.setStatus('A');
				alleDto.setPatientId(dto.getPatientId());
				alleDto.setRoleId(3);
				alleDto.setComments("");
				alleDto.setOrganizationId(dto.getOrganizationId());
				alleDto.setUnitId(dto.getUnitId());
				Calendar c = Calendar.getInstance();
				int year = c.get(Calendar.YEAR);
				int month = c.get(Calendar.MONTH);
				
			
				
			    List<ListOfAllergyPatientMapperDto> listMapDto = new ArrayList<>();
			    ListOfAllergyPatientMapperDto mapDto = new ListOfAllergyPatientMapperDto();
			    mapDto.setIsNoKnownAllergies('N');
			    mapDto.setAllergyDescText("");
			    
			    Response res2 = iAntibioticTestDoseDao.getAllergyType(dto);
				
				if(res2.getListObject() != null && res2.getListObject().size()>0) {
					
					  List<AntibioticTestDoseDto> list =res2.getListObject();
					  mapDto.setAllergyTypeId(list.get(0).getAllergyTypeId());
				}else {
					 mapDto.setAllergyTypeId(0);
				}
			    
			  
			    mapDto.setGenericId(dto.getGenericId());
			    mapDto.setMonth(month);
			    mapDto.setYear(year);
			    mapDto.setType("Confirm");
			    mapDto.setNatureOfReaction("Allergies");
			    mapDto.setSeverityId(dto.getAllergyId());
			    mapDto.setRemark("");
			    mapDto.setIsEnterInErrorStatus('N');
			    mapDto.setIsReviewedFlag('N');
			    listMapDto.add(mapDto);
			    
				alleDto.setListOfAllergyPatientMapperDto(listMapDto);
				iAllergyPatientMapperDao.saveAllergyPatientMapper(alleDto);
			}
			
			return res;
	
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	
	}

	@Override
	@Transactional
	public Response getAntibioticTestDose(AntibioticTestDoseDto dto) throws ApplicationException {


		try {
			return iAntibioticTestDoseDao.getAntibioticTestDose(dto);
	
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	
	
	}

	@Override
	@Transactional
	public Response checkObservation(AntibioticTestDoseDto dto) throws ApplicationException {
		try {
			return iAntibioticTestDoseDao.checkObservation(dto);
	
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	
	
	
	}
	
	


}