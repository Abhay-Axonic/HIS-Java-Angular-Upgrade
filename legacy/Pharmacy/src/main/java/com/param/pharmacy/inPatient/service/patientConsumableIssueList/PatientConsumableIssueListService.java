package com.param.pharmacy.inPatient.service.patientConsumableIssueList;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.constants.Screen;
import com.param.entity.model.adt.WardMasters;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.IssueDetailBatchMapper;
import com.param.global.common.ICommonConstants;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.utility.Util;
import com.param.pharmacy.inPatient.dao.patientConsumableIssue.IPatientConsumableIssueDao;
import com.param.pharmacy.inPatient.dao.patientConsumableIssueList.IPatientConsumableIssueListDao;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueItem;
import com.param.pharmacy.inPatient.dto.patientConsumableIssue.PatientConsumableIssueListResponse;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientConsumableIssueListDetails;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientConsumableIssueListRequest;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientDetailsRequestForDrugRecall;
import com.param.pharmacy.inPatient.dto.patientConsumableIssueList.PatientDetailsResponseForDrugRecall;
import com.param.pharmacy.nursing.pharmacy.dto.patientIndent.PatientIndentDropdown;
import com.param.pharmacy.op.dao.billList.IBillListDao;
import com.param.procurement.quotation.dao.physicalInspection.IConsignmentStockConversionDao;
import com.param.service.global.SessionService;

@Service("patientConsumableIssueListService")
@Transactional
public class PatientConsumableIssueListService implements ICommonConstants,IPatientConsumableIssueListService {
	@Autowired
	private SessionService sessionService;

	@Autowired
	private IPatientConsumableIssueListDao patientConsumableIssueListDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IBillListDao BillListDao;
	@Autowired
	private IPatientConsumableIssueDao patientConsumableIssueDao;

	@Autowired
	IConsignmentStockConversionDao consignmentStockConversionDao;

	private Integer screenId = Screen.PatientIssueConsumable.getScreen();

	@Override
	@Transactional
	public PatientIndentDropdown getDropdownList(Integer unitId, Integer orgId, Integer userId) {
		PatientIndentDropdown response = new PatientIndentDropdown();

		for (ScreenStatus status : GlobalDao.getScreenStatus(screenId)) {
			response.addStatus(
					new com.param.global.dto.global.Status(status.getStatus().getId(), status.getStatus().getStatus()));
		}

		for (VisitType visitType : GlobalDao.getVisitTypeList()) {
			response.addVisitType(new com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
					visitType.getVisitTypeName()));
		}

		for (Store store : GlobalDao.getStoreList(unitId)) {
			response.addFromStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (Store store : GlobalDao.getUserStoreList(unitId,orgId,userId)) {
			response.addToStore(new com.param.global.dto.global.Store(store.getStoreId(), store.getStoreName()));
		}

		for (WardMasters ward : GlobalDao.getWardList(unitId)) {
			response.addWard(new com.param.global.dto.global.Ward(ward.getWardId(), ward.getWardName()));
		}

		return response;
	}

	@Override
	@Transactional
	public List<PatientConsumableIssueListResponse> patientConsumableIssueSearch(
			PatientConsumableIssueListRequest request) {
		List<PatientConsumableIssueListResponse> patientIssueList = new ArrayList<PatientConsumableIssueListResponse>();
		request.setToDate((new DateTime(request.getToDate())).plusDays(1).toDate());
		for (Issue issue : patientConsumableIssueListDao.patientConsumableIssueListSearch(request, screenId)) {

			/*
			 * String doctorDetail = ""; if(issue.getDoctor()!=null &&
			 * issue.getSpecialityMaster()!=null) { doctorDetail =
			 * issue.getDoctor().getFirstName() + " " + issue.getDoctor().getMiddleName() +
			 * " " + issue.getDoctor().getLastName() + " | " +
			 * issue.getSpecialityMaster().getSpecialityName(); }
			 */

			String doctorDetail = "";
			// no doctor for emergency admission
			if (issue.getDoctor() != null) {
				if (issue.getVisitType().getVisitTypeId().equals(ER)) {
					// no doctor for emergency admission
				} else {
					doctorDetail = issue.getDoctor().getFirstName() + " " + issue.getDoctor().getMiddleName() + " "
							+ issue.getDoctor().getLastName();
				}

			}
			if (issue.getSpecialityMaster() != null) {
				doctorDetail = doctorDetail + " | " + issue.getSpecialityMaster().getSpecialityName();
			}

			if (issue.getPatientIndent() != null) {
				if (issue.getAdmission() != null) {
					Integer age = Util.getAgeByBirthDate(
							issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " "
							+ issue.getAdmission().getPatient().getFirstName() + " "
							+ issue.getAdmission().getPatient().getMiddleName() + " "
							+ issue.getAdmission().getPatient().getLastName() + " | " + age + " | "
							+ issue.getAdmission().getPatient().getGender().getGenderCode();

					String patientName = issue.getPatientIndent().getPatient().getPrefix().getPrefixCode() + " "
							+ issue.getPatientIndent().getPatient().getFirstName() + " "
							+ issue.getPatientIndent().getPatient().getMiddleName() + " "
							+ issue.getPatientIndent().getPatient().getLastName();

					PatientConsumableIssueListResponse patientIssue = new PatientConsumableIssueListResponse(
							issue.getId(), issue.getPatientIndent().getIndentNo(), issue.getAddedDate(),
							issue.getVisitType().getVisitTypeCode(), issue.getAdmission().getAdmissionNumber(),
							issue.getAdmission().getPatient().getUhidNumber(), patientDetail, doctorDetail,
							issue.getStore().getStoreName(), issue.getToStore().getStoreName(),
							issue.getPatientIndent().getStatus().getId(),
							issue.getPatientIndent().getStatus().getStatus());
					patientIssue.setWardName((issue.getPatientIndent().getWard() != null)
							? issue.getPatientIndent().getWard().getWardName()
							: null);
					patientIssue.setBedNo((issue.getPatientIndent().getBed() != null)
							? issue.getPatientIndent().getBed().getBedNumber()
							: null);
					patientIssue.setIssueNo(issue.getIssueNo());
					patientIssue.setIssueDate(issue.getAddedDate());
					patientIssue.setImagePath(issue.getAdmission().getPatient().getImagePath());
					patientIssue.setGenderCode(issue.getAdmission().getPatient().getGender().getGenderCode());
					patientIssue.setGenderId(issue.getAdmission().getPatient().getGender().getGenderId());
					patientIssueList.add(patientIssue);
				} else if (issue.getEncounter() != null) {
					Integer age = Util.getAgeByBirthDate(
							issue.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = issue.getEncounter().getPatient().getPrefix().getPrefixCode() + " "
							+ issue.getEncounter().getPatient().getFirstName() + " "
							+ issue.getEncounter().getPatient().getMiddleName() + " "
							+ issue.getEncounter().getPatient().getLastName() + " | " + age + " | "
							+ issue.getEncounter().getPatient().getGender().getGenderCode();

					String patientName = issue.getPatientIndent().getPatient().getPrefix().getPrefixCode() + " "
							+ issue.getPatientIndent().getPatient().getFirstName() + " "
							+ issue.getPatientIndent().getPatient().getMiddleName() + " "
							+ issue.getPatientIndent().getPatient().getLastName();

					PatientConsumableIssueListResponse patientIssue = new PatientConsumableIssueListResponse(
							issue.getId(), issue.getPatientIndent().getIndentNo(), issue.getAddedDate(),
							issue.getVisitType().getVisitTypeCode(), issue.getEncounter().getEncounterNumber(),
							issue.getEncounter().getPatient().getUhidNumber(), patientDetail, doctorDetail,
							issue.getStore().getStoreName(), issue.getToStore().getStoreName(),
							issue.getPatientIndent().getStatus().getId(),
							issue.getPatientIndent().getStatus().getStatus(), age, patientName);
					patientIssue.setIssueNo(issue.getIssueNo());
					patientIssue.setIssueDate(issue.getAddedDate());
					patientIssue.setImagePath(issue.getEncounter().getPatient().getImagePath());
					patientIssue.setGenderCode(issue.getEncounter().getPatient().getGender().getGenderCode());
					patientIssue.setGenderId(issue.getEncounter().getPatient().getGender().getGenderId());
					patientIssueList.add(patientIssue);
				}
			}
			if (issue.getImplantIndent() != null && issue.getIsImplant().equals(false)) {

				Integer age = Util
						.getAgeByBirthDate(issue.getAdmission().getPatient().getDob().toLocalDateTime().toLocalDate());
				String patientDetail = issue.getAdmission().getPatient().getPrefix().getPrefixCode() + " "
						+ issue.getAdmission().getPatient().getFirstName() + " "
						+ issue.getAdmission().getPatient().getMiddleName() + " "
						+ issue.getAdmission().getPatient().getLastName() + " | " + age + " | "
						+ issue.getAdmission().getPatient().getGender().getGenderCode();

				String patientName = issue.getPatientIndent().getPatient().getPrefix().getPrefixCode() + " "
						+ issue.getPatientIndent().getPatient().getFirstName() + " "
						+ issue.getPatientIndent().getPatient().getMiddleName() + " "
						+ issue.getPatientIndent().getPatient().getLastName();

				PatientConsumableIssueListResponse patientIssue = new PatientConsumableIssueListResponse(issue.getId(),
						issue.getImplantIndent().getImplantIndentNo(), issue.getAddedDate(),
						issue.getVisitType().getVisitTypeCode(), issue.getAdmission().getAdmissionNumber(),
						issue.getAdmission().getPatient().getUhidNumber(), patientDetail, doctorDetail,
						issue.getStore().getStoreName(), issue.getToStore().getStoreName(),
						issue.getImplantIndent().getStatus().getId(), issue.getImplantIndent().getStatus().getStatus(),
						age, patientName);
				patientIssue.setIssueNo(issue.getIssueNo());
				patientIssue.setIssueDate(issue.getAddedDate());
				patientIssueList.add(patientIssue);
			}
		}
		return patientIssueList;
	}

	@Override
	@Transactional
	public List<PatientDetailsResponseForDrugRecall> patientConsumableIssueSearchForDrugRecall(
			PatientDetailsRequestForDrugRecall request) {
		List<PatientDetailsResponseForDrugRecall> response = new ArrayList<PatientDetailsResponseForDrugRecall>();
		List<PatientDetailsResponseForDrugRecall> sales = BillListDao.searchBillsForDrugRecall(request);
		return sales;

	}

	@Override
	@Transactional
	public PatientConsumableIssueListDetails patientConsumableIssueDetails(Integer id,Integer unitId) throws Exception {
		PatientConsumableIssueListDetails patientConsumableIssueListDetails = new PatientConsumableIssueListDetails();
		Issue issue = patientConsumableIssueListDao.patientIssueById(id,unitId);

		if (issue.getPatientIndent() != null) {
			if (issue.getAdmission() != null) {
				String patientName = issue.getAdmission().getPatient().getFirstName() + " "
						+ issue.getAdmission().getPatient().getMiddleName() + " "
						+ issue.getAdmission().getPatient().getLastName();
				patientConsumableIssueListDetails = new PatientConsumableIssueListDetails(issue.getId(),
						issue.getStore().getStoreName(), issue.getStore().getStoreId(),
						issue.getToStore().getStoreName(), issue.getToStore().getStoreId(),
						issue.getVisitType().getVisitTypeCode(), patientName,
						issue.getPatientIndent().getPatient().getUhidNumber(),
						issue.getAdmission().getAdmissionNumber(), issue.getPatientIndent().getIndentNo(),
						issue.getCarrier() != null ? issue.getCarrier().getUserId() : null,
						issue.getCarrier() != null ? issue.getCarrier().getUserName() : null);
				patientConsumableIssueListDetails.setWardName(issue.getPatientIndent().getWard().getWardName());
				patientConsumableIssueListDetails.setBedNo(issue.getPatientIndent().getBed().getBedNumber());
				patientConsumableIssueListDetails.setStatus(issue.getStatus().getStatus());
				patientConsumableIssueListDetails.setStatusId(issue.getStatus().getId());
				patientConsumableIssueListDetails.setRemark(issue.getRemark());
			} else if (issue.getEncounter() != null) {
				String patientName = issue.getEncounter().getPatient().getFirstName() + " "
						+ issue.getEncounter().getPatient().getMiddleName() + " "
						+ issue.getEncounter().getPatient().getLastName();
				patientConsumableIssueListDetails = new PatientConsumableIssueListDetails(issue.getId(),
						issue.getStore().getStoreName(), issue.getStore().getStoreId(),
						issue.getToStore().getStoreName(), issue.getToStore().getStoreId(),
						issue.getVisitType().getVisitTypeCode(), patientName,
						issue.getPatientIndent().getPatient().getUhidNumber(),
						issue.getEncounter().getEncounterNumber(), issue.getPatientIndent().getIndentNo(),
						issue.getCarrier().getUserId(), issue.getCarrier().getUserName());
				patientConsumableIssueListDetails.setStatus(issue.getStatus().getStatus());
				patientConsumableIssueListDetails.setStatusId(issue.getStatus().getId());
				patientConsumableIssueListDetails.setRemark(issue.getRemark());
			}
		}

		if (issue.getImplantIndent() != null && issue.getIsImplant() == false) {
			String patientName = issue.getAdmission().getPatient().getFirstName() + " "
					+ issue.getAdmission().getPatient().getMiddleName() + " "
					+ issue.getAdmission().getPatient().getLastName();
			patientConsumableIssueListDetails = new PatientConsumableIssueListDetails(issue.getId(),
					issue.getStore().getStoreName(), issue.getStore().getStoreId(), issue.getToStore().getStoreName(),
					issue.getToStore().getStoreId(), issue.getVisitType().getVisitTypeCode(), patientName,
					issue.getImplantIndent().getPatient().getUhidNumber(), issue.getAdmission().getAdmissionNumber(),
					issue.getImplantIndent().getImplantIndentNo(), issue.getCarrier().getUserId(),
					issue.getCarrier().getUserName());
			patientConsumableIssueListDetails.setStatus(issue.getStatus().getStatus());
			patientConsumableIssueListDetails.setStatusId(issue.getStatus().getId());
			patientConsumableIssueListDetails.setRemark(issue.getRemark());
		}

		// for (IssueDetail issueDetail :
		// patientConsumableIssueListDao.patientIssueDetailById(id)) {
		List<PatientConsumableIssueItem> items = new ArrayList<PatientConsumableIssueItem>();
		//for (IssueDetail issueDetail : patientConsumableIssueDao.getpatientConsumableIssueDetail(id)) {
			
		for (IssueDetail issueDetail : issue.getIssueDetail()) {
			
		if(issueDetail.getIsDeleted().equals(false))
		{
			
			// Long stockAvailable =
			// GlobalDao.getStoreStockByItemPharmacy(sessionService.getStoreId(),issueDetail.getPatientIndentDetail().getItem().getId());

			Long stockAvailable = 0l;

			for (IssueDetailBatchMapper batchMapper : issueDetail.getIssueDetailBatchMapper()) {
				
				if(batchMapper.getIsActive() == true)
				{

		             //   if(batchMapper.getIsActive().equals(true));
						Batch r = batchMapper.getBatch();

						if (r.getLotNo() != null && r.getBatchNo() != null) {
							// Integer sCstock =
							// consignmentStockConversionDao.getTotalConsignmentStockFromBatch(r.getBatchno(),r.getLotno());
							BigInteger availableQty = consignmentStockConversionDao.getAvailableStock(r.getBatchNo(),
									r.getLotNo(), r.getIsConsignment(), issueDetail.getIssue().getToStore().getStoreId(),
									issueDetail.getPatientIndentDetail().getItem().getId(),unitId);
							// r.setAvailablestockquantity(availableQty);
							// r.setTotalstock(sCstock);
							stockAvailable = availableQty.longValue();

						} else {
							// Integer sCstock =
							// consignmentStockConversionDao.getTotalStock(r.getBatchno());
							// r.setTotalstock(sCstock);
							boolean isConsignmentFlag = true;
							if (r.getIsConsignment() != null) {
								isConsignmentFlag = r.getIsConsignment();
							}

							if (r.getBatchNo() != null && r.getLotNo() == null) {
								BigInteger availableQty = consignmentStockConversionDao.getAvailableWithoutLotStock(
										r.getBatchNo(), isConsignmentFlag, issueDetail.getIssue().getToStore().getStoreId(),
										issueDetail.getPatientIndentDetail().getItem().getId(),unitId);
								// r.setAvailablestockquantity(availableQty);
								stockAvailable = availableQty.longValue();

							} else if (r.getBatchNo() == null && r.getLotNo() != null) {
								BigInteger availableQty = consignmentStockConversionDao.getAvailableWithoutBatchStock(
										r.getLotNo(), isConsignmentFlag, issueDetail.getIssue().getToStore().getStoreId(),
										issueDetail.getPatientIndentDetail().getItem().getId(),unitId);
								// r.setAvailablestockquantity(availableQty);
								stockAvailable = availableQty.longValue();

							}

							if (r.getIsConsignment() == null) {
								BigInteger availableQty = consignmentStockConversionDao.getAvailableBatchStock(r.getBatchNo(),
										r.getLotNo(), issueDetail.getIssue().getToStore().getStoreId(),
										issueDetail.getPatientIndentDetail().getItem().getId(),unitId);
								// r.setAvailablestockquantity(availableQty);
								stockAvailable = availableQty.longValue();
							}

						}

						if (issueDetail.getPatientIndentDetail() != null) {

							Integer issueQuantity = (issueDetail.getPatientIndentDetail().getIndentQuantity()
									- issueDetail.getPatientIndentDetail().getPendingQuantity());

							PatientConsumableIssueItem item = new PatientConsumableIssueItem(issueDetail.getId(),
									issueDetail.getPatientIndentDetail().getItem().getId(),
									issueDetail.getPatientIndentDetail().getItem().getCode(),
									issueDetail.getPatientIndentDetail().getItem().getName(),
									issueDetail.getPatientIndentDetail().getItem().getGeneric() != null
											? issueDetail.getPatientIndentDetail().getItem().getGeneric().getName()
											: null,
									batchMapper.getBatch().getId(), batchMapper.getBatch().getBatchNo(),
									batchMapper.getBatch().getExpiryDate(),
									batchMapper.getUomType() != null ? batchMapper.getUomType().getType() : null,
									batchMapper.getUomType() != null ? batchMapper.getUomType().getId() : null,
									batchMapper.getUomUnit() != null ? batchMapper.getUomUnit().getId() : null,
									batchMapper.getUomUnit() != null ? batchMapper.getUomUnit().getUnits() : null,
									//issueDetail.getPatientIndentDetail().getIndentQuantity(), stockAvailable, issueQuantity);
									/* chamge code for resolved HIS-10665 On patient issue acceptance indent issued qty shows wrong */
									issueDetail.getPatientIndentDetail().getIndentQuantity(), stockAvailable,  batchMapper.getIssueQuantity(),
									issueDetail.getPatientIndentDetail().getItem().getIsThreeUOMApplicable(),
									GlobalDao.getUnitOfMeasurementListByItem(issueDetail.getPatientIndentDetail().getItem().getId(), issueDetail.getOrganizationId()));
							if(item.getUomTypeId()==null)
							{
								item.setUomTypeId(issueDetail.getUomType().getId());
								item.setUomType(issueDetail.getUomType().getType());
								item.setUomUnit(issueDetail.getUomUnit().getUnits());
								item.setUomUnitId(issueDetail.getUomUnit().getId());
							}
							item.setIndentUomTypeId(issueDetail.getPatientIndentDetail().getUomType().getId());
							item.setStatus(issueDetail.getStatus().getStatus());
							item.setStatusId(issueDetail.getStatus().getId());
							item.setGenericId(issueDetail.getPatientIndentDetail().getItem().getGeneric() != null
											? issueDetail.getPatientIndentDetail().getItem().getGeneric().getId()
											: null);
							items.add(item);
						}

						if (issueDetail.getImplantIndentDetail() != null) {
							Integer issueQuantity = (issueDetail.getPatientIndentDetail().getIndentQuantity()
									- issueDetail.getPatientIndentDetail().getPendingQuantity());

							PatientConsumableIssueItem item = new PatientConsumableIssueItem(issueDetail.getId(),
									issueDetail.getImplantIndentDetail().getItem().getId(),
									issueDetail.getImplantIndentDetail().getItem().getCode(),
									issueDetail.getImplantIndentDetail().getItem().getName(),
									issueDetail.getPatientIndentDetail().getItem().getGeneric() != null
											? issueDetail.getPatientIndentDetail().getItem().getGeneric().getName()
											: null,
									batchMapper.getBatch().getId(), batchMapper.getBatch().getBatchNo(),
									batchMapper.getBatch().getExpiryDate(),
									batchMapper.getUomType() != null ? batchMapper.getUomType().getType() : null,
									batchMapper.getUomType() != null ? batchMapper.getUomType().getId() : null,
									batchMapper.getUomUnit() != null ? batchMapper.getUomUnit().getId() : null,
									batchMapper.getUomUnit() != null ? batchMapper.getUomUnit().getUnits() : null,
									//issueDetail.getImplantIndentDetail().getIndentQuantity(), stockAvailable, issueQuantity);
									/* chamge code for resolved HIS-10665 On patient issue acceptance indent issued qty shows wrong */
									issueDetail.getImplantIndentDetail().getIndentQuantity(), stockAvailable, batchMapper.getIssueQuantity(),
									issueDetail.getPatientIndentDetail().getItem().getIsThreeUOMApplicable(),
									GlobalDao.getUnitOfMeasurementListByItem(issueDetail.getPatientIndentDetail().getItem().getId(), issueDetail.getOrganizationId()));
							if(item.getUomTypeId()==null)
							{
								item.setUomTypeId(issueDetail.getUomType().getId());
								item.setUomType(issueDetail.getUomType().getType());
								item.setUomUnit(issueDetail.getUomUnit().getUnits());
								item.setUomUnitId(issueDetail.getUomUnit().getId());
							}
							item.setIndentUomTypeId(issueDetail.getPatientIndentDetail().getUomType().getId());
							item.setStatus(issueDetail.getStatus().getStatus());
							item.setStatusId(issueDetail.getStatus().getId());
							item.setGenericId(issueDetail.getPatientIndentDetail().getItem().getGeneric() != null
									? issueDetail.getPatientIndentDetail().getItem().getGeneric().getId()
									: null);
							items.add(item);
						}
						
				}
				else
				{
					
				}
				
				
			}
			patientConsumableIssueListDetails.setItems(items);
		}
		}

		return patientConsumableIssueListDetails;
	}

}