package com.param.pharmacy.narcotics.acceptanceOfEmptyAmpoules.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.common.dto.CommonResponse;
import com.param.entity.model.adt.AdmissionDetail;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoules;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoulesDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.dto.global.CommonSearchRequest;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearchResult;
import com.param.pharmacy.narcotics.acceptanceOfEmptyAmpoules.dao.IAcceptanceEmptyAmpoulesDao;
import com.param.pharmacy.nursing.narcoticsStore.dto.EmptyAmpoulesResponse;
import com.param.pharmacy.nursing.narcoticsStore.dto.EmptyAmpoulesSave;
import com.param.pharmacy.nursing.narcoticsStore.dto.EmptyAmpoulesSaveItem;
import com.param.pharmacy.nursing.narcoticsStore.dto.ReturnEmptyAmpoulesItem;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("acceptanceEmptyAmpoulesService")
@Transactional
public class AcceptanceEmptyAmpoulesService implements IAcceptanceEmptyAmpoulesService {
	@Autowired
	private IAcceptanceEmptyAmpoulesDao acceptanceEmptyAmpoulesDao;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private IGlobalDao GlobalDao;

	private Integer screenId = Screen.AcceptanceEmptyAmpoules.getScreen();

	@Override
	@Transactional
	public CommonDropdown getDropdownList(Integer unitId) {
		CommonDropdown response = new CommonDropdown();

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (WardMasters wardMaster : GlobalDao.getWardList(unitId)) {
			response.addWard(new com.param.global.dto.global.Ward(wardMaster.getWardId(), wardMaster.getWardName()));
		}

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<PendingIssuesAcceptanceSearchResult> acceptanceEmptyAmpoulesSearch(CommonSearchRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		List<PendingIssuesAcceptanceSearchResult> acceptanceEmptyAmpoulesList = new ArrayList<PendingIssuesAcceptanceSearchResult>();
		for (ReturnOfEmptyAmpoules returnOfEmptyAmpoules : acceptanceEmptyAmpoulesDao
				.acceptanceEmptyAmpoulesSearch(request, screenId)) {
			/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
					.doctorSearchByIdValue(returnOfEmptyAmpoules.getDoctor().getDoctorId());
			String drSpeciality = "";
			if (doctorSpeciality.size() > 0) {
				drSpeciality = doctorSpeciality.get(0).getSpeciality().getSpecialityName();
			}*/
			String splName="";
			String doctorDeatil="";
			if(returnOfEmptyAmpoules.getIssue().getDoctor()!=null && returnOfEmptyAmpoules.getIssue().getPrescription().getSpecialityMaster()!=null) {
				splName = returnOfEmptyAmpoules.getIssue().getPrescription().getSpecialityMaster().getSpecialityName();
				doctorDeatil = returnOfEmptyAmpoules.getIssue().getDoctor().getFirstName() + " "
						+ returnOfEmptyAmpoules.getIssue().getDoctor().getMiddleName() + " "
						+ returnOfEmptyAmpoules.getIssue().getDoctor().getLastName() + " | " + splName;

			}
			
			Integer age = Util
					.getAgeByBirthDate(returnOfEmptyAmpoules.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = returnOfEmptyAmpoules.getPatient().getPrefix().getPrefixCode() + " "
					+ returnOfEmptyAmpoules.getPatient().getFirstName() + " "
					+ returnOfEmptyAmpoules.getPatient().getMiddleName() + " "
					+ returnOfEmptyAmpoules.getPatient().getLastName() + " | " + age + " | "
					+ returnOfEmptyAmpoules.getPatient().getGender().getGenderCode();

			PendingIssuesAcceptanceSearchResult pendingIssuesAcceptance = new PendingIssuesAcceptanceSearchResult(
					returnOfEmptyAmpoules.getId(), returnOfEmptyAmpoules.getIssue().getIssueNo(),
					returnOfEmptyAmpoules.getPatient().getUhidNumber(), patientDetail, doctorDeatil,
					returnOfEmptyAmpoules.getIssue().getVisitType().getVisitTypeCode(),
					returnOfEmptyAmpoules.getPriority().getPriorityDesc(),
					returnOfEmptyAmpoules.getStatus().getStatus());
			
			pendingIssuesAcceptance.setWardName(
					(returnOfEmptyAmpoules.getWard() != null) ? returnOfEmptyAmpoules.getWard().getWardName() : null);
			pendingIssuesAcceptance.setBedNo(
					(returnOfEmptyAmpoules.getBed() != null) ? returnOfEmptyAmpoules.getBed().getBedNumber() : null);
			acceptanceEmptyAmpoulesList.add(pendingIssuesAcceptance);
		}
		return acceptanceEmptyAmpoulesList;
	}

	@Override
	@Transactional
	public EmptyAmpoulesResponse acceptanceEmptyAmpoulesDetails(Integer id) {
		ReturnOfEmptyAmpoules returnOfEmptyAmpoules = acceptanceEmptyAmpoulesDao.acceptanceEmptyAmpoulesById(id);
		EmptyAmpoulesResponse emptyAmpoulesResponse = new EmptyAmpoulesResponse();
		/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
				.doctorSearchByIdValue(returnOfEmptyAmpoules.getDoctor().getDoctorId());
		String drSpeciality = "";
		if (doctorSpeciality.size() > 0) {
			drSpeciality = doctorSpeciality.get(0).getSpeciality().getSpecialityName();
		}*/
		String drSpeciality = "";
		if(returnOfEmptyAmpoules.getIssue().getSpecialityMaster()!=null)
		{
			drSpeciality=	returnOfEmptyAmpoules.getIssue().getSpecialityMaster().getSpecialityName();
		}
		String doctorDeatil="";
		if(returnOfEmptyAmpoules.getIssue().getDoctor()!=null ) {
		doctorDeatil = returnOfEmptyAmpoules.getIssue().getDoctor().getFirstName() + " "
				+ returnOfEmptyAmpoules.getIssue().getDoctor().getMiddleName() + " "
				+ returnOfEmptyAmpoules.getIssue().getDoctor().getLastName() + " | " +drSpeciality ;
		}
		Integer age = Util
				.getAgeByBirthDate(returnOfEmptyAmpoules.getPatient().getDob().toLocalDateTime().toLocalDate());
		String patientDetail = returnOfEmptyAmpoules.getPatient().getPrefix().getPrefixCode() + " "
				+ returnOfEmptyAmpoules.getPatient().getFirstName() + " "
				+ returnOfEmptyAmpoules.getPatient().getMiddleName() + " "
				+ returnOfEmptyAmpoules.getPatient().getLastName() + " | " + age + " | "
				+ returnOfEmptyAmpoules.getPatient().getGender().getGenderCode();

		emptyAmpoulesResponse = new EmptyAmpoulesResponse(returnOfEmptyAmpoules.getId(),
				returnOfEmptyAmpoules.getPatient().getUhidNumber(), returnOfEmptyAmpoules.getPatient().getPatientId(),
				patientDetail, returnOfEmptyAmpoules.getPatient().getDob(),
				doctorDeatil!=""?returnOfEmptyAmpoules.getIssue().getDoctor().getDoctorId():null, doctorDeatil,
				returnOfEmptyAmpoules.getPriority().getPriorityId());

		if (returnOfEmptyAmpoules.getIssue().getAdmission() != null) {
			Date doa = null;
			Date pdd = null;
			AdmissionDetail admissionDetail = GlobalDao
					.getPatientWardBedDetails(returnOfEmptyAmpoules.getIssue().getAdmission().getAdmissionId());
			if (admissionDetail != null) {
				doa = admissionDetail.getDoa();
				pdd = admissionDetail.getPdd();

			}
			emptyAmpoulesResponse.setDoa(doa != null ? doa : null);
			emptyAmpoulesResponse.setPdd(pdd != null ? pdd : null);
		}
		emptyAmpoulesResponse.setWardId(
				(returnOfEmptyAmpoules.getWard() != null) ? returnOfEmptyAmpoules.getWard().getWardId() : null);
		emptyAmpoulesResponse.setWardName(
				(returnOfEmptyAmpoules.getWard() != null) ? returnOfEmptyAmpoules.getWard().getWardName() : null);
		emptyAmpoulesResponse
				.setBedId((returnOfEmptyAmpoules.getBed() != null) ? returnOfEmptyAmpoules.getBed().getBedId() : null);
		emptyAmpoulesResponse.setBedNo(
				(returnOfEmptyAmpoules.getBed() != null) ? returnOfEmptyAmpoules.getBed().getBedNumber() : null);
		emptyAmpoulesResponse.setStatusId(returnOfEmptyAmpoules.getStatus().getId());
		emptyAmpoulesResponse.setImagePath(returnOfEmptyAmpoules.getPatient().getImagePath());
		emptyAmpoulesResponse.setGenderId(returnOfEmptyAmpoules.getPatient().getGender().getGenderId());
		List<ReturnEmptyAmpoulesItem> items = new ArrayList<ReturnEmptyAmpoulesItem>();
		BigInteger consumedQty = BigInteger.valueOf(0);
		for (PrescriptionDetail d : returnOfEmptyAmpoules.getIssue().getPrescription().getPrescriptionDetailsList()) {
			consumedQty = consumedQty.add(
					GlobalDao.getConsumedQty(returnOfEmptyAmpoules.getPatient().getPatientId(), d.getItem().getId(),0L, 0));

		}
		for (ReturnOfEmptyAmpoulesDetail returnOfEmptyAmpoulesDetail : acceptanceEmptyAmpoulesDao
				.acceptanceEmptyAmpoulesDetails(id)) {
			IssueDetailBatchMapper batchMapper = returnOfEmptyAmpoulesDetail.getIssueBatchMapper();
			ReturnEmptyAmpoulesItem item = new ReturnEmptyAmpoulesItem(returnOfEmptyAmpoulesDetail.getId(),
					batchMapper.getBatch().getItem().getId(), batchMapper.getBatch().getItem().getCode(),
					batchMapper.getBatch().getItem().getName(), batchMapper.getBatch().getItem().getGeneric().getId(),
					batchMapper.getBatch().getItem().getGeneric().getName(), batchMapper.getIssueQuantity(),
					batchMapper.getBatch().getId(), batchMapper.getBatch().getBatchNo(),
					batchMapper.getBatch().getExpiryDate(), batchMapper.getUomType().getType(),
					batchMapper.getUomUnit().getUnits());

			item.setReturedQuantity(returnOfEmptyAmpoulesDetail.getReturnedQuantity());
			item.setAcceptedQuantity(returnOfEmptyAmpoulesDetail.getAcceptQuantity());
			item.setStatusId(returnOfEmptyAmpoulesDetail.getStatus().getId());
			item.setConsumedQty(consumedQty);
			items.add(item);
		}
		emptyAmpoulesResponse.setItems(items);
		return emptyAmpoulesResponse;
	}

	@Override
	public CommonResponse acceptanceEmptyAmpoulesSave(EmptyAmpoulesSave request) {
		ReturnOfEmptyAmpoules returnOfEmptyAmpoules = acceptanceEmptyAmpoulesDao
				.acceptanceEmptyAmpoulesById(request.getId());

		for (EmptyAmpoulesSaveItem items : request.getItems()) {
			ReturnOfEmptyAmpoulesDetail returnOfEmptyAmpoulesDetail = acceptanceEmptyAmpoulesDao
					.acceptanceEmptyAmpoulesDetailsById(items.getId());
			returnOfEmptyAmpoulesDetail.setAcceptQuantity(items.getQuantityAccepted());
			
		  int returedQty=0;
		  if(items.getQuantityAccepted()!=null)
		  {
			  returedQty=returedQty+items.getQuantityAccepted();
		  }
		  else
		  {
			  returedQty= items.getQuantityAccepted();
		  }
//			returnOfEmptyAmpoulesDetail.setReturnedQuantity(returedQty);
			returnOfEmptyAmpoulesDetail.setAcceptQuantity(returedQty);
			returnOfEmptyAmpoulesDetail.setRejectedQuantity(items.getQuantityRejected());
			returnOfEmptyAmpoulesDetail.setStatus(new Status(items.getStatusId()));
			returnOfEmptyAmpoulesDetail.setUpdatedDate(TimeZoneComponent.getDateByzone(returnOfEmptyAmpoules.getUnitId()));
			returnOfEmptyAmpoulesDetail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		}
		returnOfEmptyAmpoules.setStatus(new Status(request.getStatusId()));
		returnOfEmptyAmpoules.setUpdatedDate(TimeZoneComponent.getDateByzone(returnOfEmptyAmpoules.getUnitId()));
		returnOfEmptyAmpoules.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		acceptanceEmptyAmpoulesDao.acceptanceEmptyAmpoulesSave(returnOfEmptyAmpoules);
		CommonResponse response = new CommonResponse();
		response.setId(returnOfEmptyAmpoules.getId().longValue());
		response.setMessage("Acceptance Of Empty Ampoules saved successfully");

		return response;
	}

}
