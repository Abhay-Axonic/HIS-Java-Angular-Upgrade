package com.param.pharmacy.nursing.narcoticsStore.service;

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
import com.param.entity.model.adt.BedMaster;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoules;
import com.param.entity.model.pharmacy.ReturnOfEmptyAmpoulesDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.dto.global.CommonSearchRequest;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dto.pendingIssues.acceptance.PendingIssuesAcceptanceSearchResult;
import com.param.pharmacy.nursing.narcoticsStore.dto.EmptyAmpoulesResponse;
import com.param.pharmacy.nursing.narcoticsStore.dto.EmptyAmpoulesSave;
import com.param.pharmacy.nursing.narcoticsStore.dto.EmptyAmpoulesSaveItem;
import com.param.pharmacy.nursing.narcoticsStore.dto.ReturnEmptyAmpoulesItem;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("returnEmptyAmpoulesService")
@Transactional
public class ReturnEmptyAmpoulesService implements IReturnEmptyAmpoulesService {
	@Autowired
	private SessionService sessionService;

	@Autowired
	private com.param.pharmacy.nursing.narcoticsStore.dao.IReturnEmptyAmpoulesDao IReturnEmptyAmpoulesDao;

	@Autowired
	private IGlobalDao GlobalDao;

	private Integer screenId = Screen.ReturnOfEmptyAmpoules.getScreen();

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
	public List<PendingIssuesAcceptanceSearchResult> returnEmptyAmpoulesSearch(CommonSearchRequest request) {
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		List<PendingIssuesAcceptanceSearchResult> pendingIssuesAcceptanceSearchResult = new ArrayList<PendingIssuesAcceptanceSearchResult>();
		List<ReturnOfEmptyAmpoules> dataList=IReturnEmptyAmpoulesDao.returnEmptyAmpoulesSearch(request,screenId);
		for (ReturnOfEmptyAmpoules returnOfEmptyAmpoules : dataList) 
		{
			/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
					.doctorSearchByIdValue(returnOfEmptyAmpoules.getDoctor().getDoctorId());
			String drSpeciality="";
			if(doctorSpeciality.size() > 0)
			{
				drSpeciality= doctorSpeciality.get(0).getSpeciality().getSpecialityName();
			}*/
			String doctorDeatil = "";
			if(returnOfEmptyAmpoules.getIssue().getPrescription().getSpecialityMaster()!=null && returnOfEmptyAmpoules.getIssue().getDoctor()!=null)
				doctorDeatil = returnOfEmptyAmpoules.getIssue().getDoctor().getFirstName() + " "
					+ returnOfEmptyAmpoules.getIssue().getDoctor().getMiddleName() + " "
					+ returnOfEmptyAmpoules.getIssue().getDoctor().getLastName() + " | "
					+ returnOfEmptyAmpoules.getIssue().getPrescription().getSpecialityMaster().getSpecialityName();

			if (returnOfEmptyAmpoules.getIssue().getAdmission() != null) {
				String wardName = "";
				String bedNumber = "";
				AdmissionDetail admissionDetail = GlobalDao
						.getPatientWardBedDetails(returnOfEmptyAmpoules.getIssue().getAdmission().getAdmissionId());
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
				Integer age = Util.getAgeByBirthDate(returnOfEmptyAmpoules.getIssue().getAdmission().getPatient()
						.getDob().toLocalDateTime().toLocalDate());
				String patientDetail = returnOfEmptyAmpoules.getPatient().getPrefix().getPrefixCode() + " "
						+ returnOfEmptyAmpoules.getPatient().getFirstName() + " "
						+ returnOfEmptyAmpoules.getPatient().getMiddleName() + " "
						+ returnOfEmptyAmpoules.getPatient().getLastName() + " | " + age + " | "
						+ returnOfEmptyAmpoules.getPatient().getGender().getGenderCode();

				PendingIssuesAcceptanceSearchResult pendingIssuesAcceptance = new PendingIssuesAcceptanceSearchResult(
						returnOfEmptyAmpoules.getId(), returnOfEmptyAmpoules.getIssue().getIssueNo(),
						returnOfEmptyAmpoules.getIssue().getAdmission().getPatient().getUhidNumber(), patientDetail,
						doctorDeatil, returnOfEmptyAmpoules.getIssue().getVisitType().getVisitTypeCode(),
						returnOfEmptyAmpoules.getPriority().getPriorityDesc(),
						returnOfEmptyAmpoules.getStatus().getStatus());
				pendingIssuesAcceptance.setWardName(!wardName.equals("") ? wardName : null);
				pendingIssuesAcceptance.setBedNo(!bedNumber.equals("") ? bedNumber : null);
				pendingIssuesAcceptanceSearchResult.add(pendingIssuesAcceptance);
			}
			
		}
		return pendingIssuesAcceptanceSearchResult;
	}

	@Override
	@Transactional
	public EmptyAmpoulesResponse returnEmptyAmpoulesDetails(Integer id) {
		ReturnOfEmptyAmpoules returnOfEmptyAmpoules = IReturnEmptyAmpoulesDao.returnEmptyAmpoulesById(id);
		EmptyAmpoulesResponse emptyAmpoulesResponse = new EmptyAmpoulesResponse();
		String doctorDeatil="";
		/*List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
				.doctorSearchByIdValue(returnOfEmptyAmpoules.getDoctor().getDoctorId());
		String drSpeciality = "";

		if (doctorSpeciality.size() > 0) {
			drSpeciality = doctorSpeciality.get(0).getSpeciality().getSpecialityName();
		}*/
		
		if(returnOfEmptyAmpoules.getIssue().getPrescription().getDoctor()!=null && returnOfEmptyAmpoules.getIssue().getPrescription().getSpecialityMaster()!=null) {
		doctorDeatil = returnOfEmptyAmpoules.getIssue().getPrescription().getDoctor().getFirstName() + " "
				+ returnOfEmptyAmpoules.getIssue().getPrescription().getDoctor().getMiddleName() + " "
				+ returnOfEmptyAmpoules.getIssue().getPrescription().getDoctor().getLastName(); 
//		}
//		if(returnOfEmptyAmpoules.getIssue().getPrescription().getSpecialityMaster() !=null)
//		{
			doctorDeatil= doctorDeatil+ " | " + returnOfEmptyAmpoules.getIssue().getPrescription().getSpecialityMaster().getSpecialityName();
		}

		if (returnOfEmptyAmpoules.getIssue().getAdmission() != null) {

			String wardName = "";
			String bedNumber = "";
			Integer wardId = 0;
			Integer bedId = 0;
			Date doa = null;
			Date pdd = null;
			AdmissionDetail admissionDetail = GlobalDao
					.getPatientWardBedDetails(returnOfEmptyAmpoules.getIssue().getAdmission().getAdmissionId());
			if (admissionDetail != null) {
				doa = admissionDetail.getDoa();
				pdd = admissionDetail.getPdd();
				WardMasters wardMaster = admissionDetail.getWardMaster();
				if (wardMaster != null) {
					wardName = wardMaster.getWardName();
					wardId = wardMaster.getWardId();
				}
				BedMaster bedMaster = admissionDetail.getBedMaster();
				if (bedMaster != null) {
					bedNumber = bedMaster.getBedNumber();
					bedId = bedMaster.getBedId();
				}
			}
			Integer age = Util
					.getAgeByBirthDate(returnOfEmptyAmpoules.getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = returnOfEmptyAmpoules.getPatient().getPrefix().getPrefixCode() + " "
					+ returnOfEmptyAmpoules.getPatient().getFirstName() + " "
					+ returnOfEmptyAmpoules.getPatient().getMiddleName() + " "
					+ returnOfEmptyAmpoules.getPatient().getLastName() + " | " + age + " | "
					+ returnOfEmptyAmpoules.getPatient().getGender().getGenderCode();

			emptyAmpoulesResponse = new EmptyAmpoulesResponse(returnOfEmptyAmpoules.getId(),
					returnOfEmptyAmpoules.getPatient().getUhidNumber(),
					returnOfEmptyAmpoules.getPatient().getPatientId(), patientDetail,
					returnOfEmptyAmpoules.getPatient().getDob(), doctorDeatil!=""?returnOfEmptyAmpoules.getIssue().getPrescription().getDoctor().getDoctorId():null,
					doctorDeatil, returnOfEmptyAmpoules.getPriority().getPriorityId());
			emptyAmpoulesResponse.setDoa(doa != null ? doa : null);
			emptyAmpoulesResponse.setPdd(pdd != null ? pdd : null);
			emptyAmpoulesResponse.setWardName(!wardName.equals("") ? wardName : null);
			emptyAmpoulesResponse.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			emptyAmpoulesResponse.setWardId(wardId != null ? wardId : null);
			emptyAmpoulesResponse.setBedId(bedId != null ? bedId : null);
			emptyAmpoulesResponse.setGenderId(returnOfEmptyAmpoules.getPatient().getGender().getGenderId());
			emptyAmpoulesResponse.setImagePath(returnOfEmptyAmpoules.getPatient().getImagePath());
			emptyAmpoulesResponse.setStatusId(returnOfEmptyAmpoules.getStatus().getId());
		}
		
		List<ReturnEmptyAmpoulesItem> items = new ArrayList<ReturnEmptyAmpoulesItem>();
		
//		for (PrescriptionDetail d : returnOfEmptyAmpoules.getIssue().getPrescription().getPrescriptionDetailsList()) {
//			consumedQty = consumedQty.add(
//					GlobalDao.getConsumedQty(returnOfEmptyAmpoules.getPatient().getPatientId(), d.getItem().getId(),0L));
//
//		}

		for (ReturnOfEmptyAmpoulesDetail detail : IReturnEmptyAmpoulesDao.returnEmptyAmpoulesDetails(id)) {
			
			IssueDetailBatchMapper batchMapper = detail.getIssueBatchMapper();
			{
				BigInteger consumedQty = BigInteger.valueOf(0);
					consumedQty = consumedQty.add(
							GlobalDao.getConsumedQty(returnOfEmptyAmpoules.getPatient().getPatientId(), batchMapper.getBatch().getItem().getId(), batchMapper.getBatch().getId(),0));

				
				ReturnEmptyAmpoulesItem item = new ReturnEmptyAmpoulesItem(detail.getId(),
						batchMapper.getBatch().getItem().getId(), batchMapper.getBatch().getItem().getCode(),
						batchMapper.getBatch().getItem().getName(),
						batchMapper.getBatch().getItem().getGeneric().getId(),
						batchMapper.getBatch().getItem().getGeneric().getName(), batchMapper.getIssueQuantity(),
						batchMapper.getBatch().getId(), batchMapper.getBatch().getBatchNo(),
						batchMapper.getBatch().getExpiryDate(), batchMapper.getUomType().getType(),
						batchMapper.getUomUnit().getUnits());
				item.setReturedQuantity(detail.getReturnedQuantity()!=null ? detail.getReturnedQuantity() :0 );
				//item.setReturedQuantity(detail.getIssueBatchMapper().getAcceptedQuantity());
				item.setConsumedQty(consumedQty);
				item.setRejectedQuantity(detail.getRejectedQuantity());
				item.setStatusId(detail.getStatus().getId());
				items.add(item);
			}
		}
		emptyAmpoulesResponse.setItems(items);
		return emptyAmpoulesResponse;
	}

	@Override
	public CommonResponse returnEmptyAmpoulesSave(EmptyAmpoulesSave request) {
		ReturnOfEmptyAmpoules returnOfEmptyAmpoules = IReturnEmptyAmpoulesDao.returnEmptyAmpoulesById(request.getId());

		returnOfEmptyAmpoules.updateReturnOfEmptyAmpoules(new Status(request.getStatusId()));

		for (ReturnOfEmptyAmpoulesDetail detail : returnOfEmptyAmpoules.getReturnOfEmptyAmpoulesDetail()) {
			Integer id = null;

			for (EmptyAmpoulesSaveItem item : request.getItems()) {
				if (item.getId() != null && item.getId().equals(detail.getId())) {
					id = item.getId();

					detail.updateReturnOfEmptyAmpoulesDetail(item.getQuantityReturned(),
							new Status(item.getStatusId()));
//					detail.setAcceptQuantity(item.getQuantityAccepted());
					Integer returedQty = 0;
					if (detail.getReturnedQuantity() != null) {
						returedQty = detail.getReturnedQuantity() + item.getQuantityAccepted();
					} else {
						returedQty = item.getQuantityAccepted();
					}
					detail.setReturnedQuantity(returedQty);
					break;
				}
			}
			detail.setUpdatedDate(TimeZoneComponent.getDateByzone(returnOfEmptyAmpoules.getUnitId()));
			detail.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
		}

		returnOfEmptyAmpoules.setUpdatedDate(TimeZoneComponent.getDateByzone(returnOfEmptyAmpoules.getUnitId()));
		returnOfEmptyAmpoules.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		IReturnEmptyAmpoulesDao.updateReturnOfEmptyAmpoules(returnOfEmptyAmpoules);

		CommonResponse response = new CommonResponse();
		response.setMessage("Return Of Empty Ampoules updated successfully");

		return response;
	}

}
