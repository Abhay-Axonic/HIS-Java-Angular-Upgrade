
package com.param.pharmacy.ip.service.assignPicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

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
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.Frequency;
import com.param.entity.model.master.Item;
import com.param.entity.model.master.RouteOfAdministration;
import com.param.entity.model.master.ScreenStatus;
import com.param.entity.model.master.Status;
import com.param.entity.model.master.Store;
import com.param.entity.model.master.StrengthUnit;
import com.param.entity.model.master.UomType;
import com.param.entity.model.master.UomUnit;
import com.param.entity.model.master.User;
import com.param.entity.model.master.VisitType;
import com.param.entity.model.pharmacy.AssignPicker;
import com.param.entity.model.pharmacy.AssignPickerDetail;
import com.param.entity.model.pharmacy.AssignPickerDetailAddedItems;
import com.param.entity.model.pharmacy.Instruction;
import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.IssueDetail;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.global.CommonDropdown;
import com.param.global.dto.global.DoctorSpecialityResp;
import com.param.global.dto.global.Rack;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.Quantities;
import com.param.global.dto.items.UomResponse;
import com.param.global.pharmacy.dto.SubstituteItemDto;
import com.param.global.service.global.IGlobalService;
import com.param.global.service.items.CustomException;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.assignPicker.IAssignPickerDao;
import com.param.pharmacy.ip.dao.pharmacology.IPharmacologyValidationDao;
import com.param.pharmacy.ip.dto.assignPicker.AssignPickerDetailItem;
import com.param.pharmacy.ip.dto.assignPicker.AssignPickerSave;
import com.param.pharmacy.ip.dto.assignPicker.CancelRequest;
import com.param.pharmacy.ip.dto.assignPicker.PrescriptionDetailsDto;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyItem;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationDetails;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearch;
import com.param.pharmacy.ip.dto.pharmacology.PharmacologyValidationSearchResult;
import com.param.pharmacy.ip.dto.pharmacology.PrescriptionFrequencyDetailResp;
import com.param.pharmacy.op.dao.salesWorkList.ISalesWorkListDao;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("assignPickerService")
@Transactional
public class AssignPickerService implements IAssignPickerService {

	@Autowired
	private SessionService sessionService;

	@Autowired
	private ISalesWorkListDao salesWorkListDao;

	@Autowired
	private IAssignPickerDao assignPickerDao;

	@Autowired
	private IPharmacologyValidationDao pharmacologyValidationDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalService GlobalService;

	private Integer screenId = Screen.AssignPicker.getScreen();

	private Integer issueScreenId = Screen.PendingIssues.getScreen();

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
	public List<PharmacologyValidationSearchResult> assignPickerSearch(
			PharmacologyValidationSearch pharmacologyValidationSearch) {
		List<PharmacologyValidationSearchResult> pharmacologyValidationSearchList = new ArrayList<PharmacologyValidationSearchResult>();
		pharmacologyValidationSearch
				.setToDate((new DateTime(pharmacologyValidationSearch.getToDate())).plusDays(1).toDate());
		for (AssignPicker assignPicker : assignPickerDao.assignPickerSearch(pharmacologyValidationSearch, screenId)) {
			if (assignPicker.getPrescription().getIsValidate() == true) {
//				List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
//						.doctorSearchByIdValue(assignPicker.getPrescription().getDoctor().getDoctorId());
				String doctorDetail = assignPicker.getPrescription().getDoctor().getFirstName() + " | "
						+ assignPicker.getPrescription().getDoctor().getMiddleName() + " "
						+ assignPicker.getPrescription().getDoctor().getLastName() + " | "
						+ assignPicker.getPrescription().getSpecialityMaster().getSpecialityName();
				if (assignPicker.getPrescription().getAdmission() != null) {
					Integer age = Util.getAgeByBirthDate(assignPicker.getPrescription().getAdmission().getPatient()
							.getDob().toLocalDateTime().toLocalDate());
					String patientDetail = assignPicker.getPrescription().getAdmission().getPatient().getPrefix()
							.getPrefixCode() + " "
							+ assignPicker.getPrescription().getAdmission().getPatient().getFirstName() + " "
							+ assignPicker.getPrescription().getAdmission().getPatient().getMiddleName() + " "
							+ assignPicker.getPrescription().getAdmission().getPatient().getLastName() + " | " + age
							+ " | "
							+ assignPicker.getPrescription().getAdmission().getPatient().getGender().getGenderCode();

					String wardName = "";
					String bedNumber = "";
					AdmissionDetail admissionDetail = GlobalDao
							.getPatientWardBedDetails(assignPicker.getPrescription().getAdmission().getAdmissionId());
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

					PharmacologyValidationSearchResult pharmacologyValidation = new PharmacologyValidationSearchResult(
							(long) assignPicker.getId(),
							assignPicker.getPrescription().getVisitType().getVisitTypeName(),
							assignPicker.getPrescription().getAdmission().getCreatedDate(),
							assignPicker.getPrescription().getPrescriptionDate(),
							assignPicker.getPrescription().getAdmission().getPatient().getUhidNumber(), patientDetail,
							doctorDetail, assignPicker.getPrescription().getPriority().getPriorityDesc(),
							assignPicker.getStatus().getStatus(), assignPicker.getStatus().getId());
					pharmacologyValidation.setPrescriptionId(assignPicker.getPrescription().getId());
					pharmacologyValidation.setAssignPickerId(assignPicker.getId());
					pharmacologyValidation
							.setAdmissionId(assignPicker.getPrescription().getAdmission().getAdmissionId());
					pharmacologyValidation.setWardName(!wardName.equals("") ? wardName : null);
					pharmacologyValidation.setBedNo(!bedNumber.equals("") ? bedNumber : null);
					pharmacologyValidationSearchList.add(pharmacologyValidation);

				}
			}
		}
		return pharmacologyValidationSearchList;
	}

	@Override
	@Transactional
	public List<PharmacologyValidationSearchResult> assignPickerSearchBP(
			PharmacologyValidationSearch pharmacologyValidationSearch) {
		List<PharmacologyValidationSearchResult> pharmacologyValidationSearchList = new ArrayList<PharmacologyValidationSearchResult>();
		pharmacologyValidationSearch
				.setToDate((new DateTime(pharmacologyValidationSearch.getToDate())).plusDays(1).toDate());

		pharmacologyValidationSearchList = assignPickerDao.assignPickerSearchBP(pharmacologyValidationSearch, screenId,pharmacologyValidationSearch.getUnitId());
		if (pharmacologyValidationSearchList != null && !pharmacologyValidationSearchList.isEmpty()) {
			List<Integer> doctorIds = pharmacologyValidationSearchList.stream()
					.map(PharmacologyValidationSearchResult::getDoctorId).collect(Collectors.toList());
//			List<DoctorSpecialityResp> doctorSpecialityRespsAll = GlobalDao.doctorSearchByIdValueBP(doctorIds);
			for (PharmacologyValidationSearchResult pharmacologyValidation : pharmacologyValidationSearchList) {
				if (pharmacologyValidation.getAdmissionId() != null) {
					pharmacologyValidation.setId((long) pharmacologyValidation.getAssignPickerId());
					List<DoctorSpecialityResp> doctorSpeciality = new ArrayList<DoctorSpecialityResp>();
//					if (!doctorSpecialityRespsAll.isEmpty()) {
//						doctorSpeciality = doctorSpecialityRespsAll.stream()
//								.filter(x -> x.getDoctorId().equals(pharmacologyValidation.getDoctorId()))
//								.collect(Collectors.toList());
						if (pharmacologyValidation.getSpecialityName() != null && pharmacologyValidation.getDoctorDetail()!=null)
							pharmacologyValidation.setDoctorDetail(pharmacologyValidation.getDoctorDetail() + " | "
									+ pharmacologyValidation.getSpecialityName());
//					}

					Integer age = Util
							.getAgeByBirthDate(pharmacologyValidation.getPatientDob().toLocalDateTime().toLocalDate());
					pharmacologyValidation.setPatientDetail(pharmacologyValidation.getPatientDetail() + " | " + age);
				}
			}
		}
		return pharmacologyValidationSearchList;
	}

	/*@Override
	@Transactional
	public PharmacologyValidationDetails assignPickerDetails(Integer id) throws Exception {
		PharmacologyValidationDetails pharmacologyValidationDetails = new PharmacologyValidationDetails();
		List<PharmacologyItem> pharmacologyItemList = new ArrayList<PharmacologyItem>();
		try {
			AssignPicker assignPicker = assignPickerDao.getAssignPicker(id);
//			List<DoctorSpecialityMapper> doctorSpeciality = GlobalDao
//					.doctorSearchByIdValue(assignPicker.getPrescription().getDoctor().getDoctorId());
			User user = GlobalDao.getUserById(sessionService.getUserId());
			String pickerName = user.getUserName();
			String doctorDetail = assignPicker.getPrescription().getDoctor().getFirstName() + " | "
					+ assignPicker.getPrescription().getDoctor().getMiddleName() + " "
					+ assignPicker.getPrescription().getDoctor().getLastName() + " | "
					+ assignPicker.getPrescription().getSpecialityMaster().getSpecialityName();
			Integer age = Util.getAgeByBirthDate(assignPicker.getPrescription().getAdmission().getPatient().getDob()
					.toLocalDateTime().toLocalDate());
			String patientDetail = assignPicker.getPrescription().getAdmission().getPatient().getPrefix()
					.getPrefixCode() + " " + assignPicker.getPrescription().getAdmission().getPatient().getFirstName()
					+ " " + assignPicker.getPrescription().getAdmission().getPatient().getMiddleName() + " "
					+ assignPicker.getPrescription().getAdmission().getPatient().getLastName() + " | " + age + " | "
					+ assignPicker.getPrescription().getAdmission().getPatient().getGender().getGenderCode();

			if (assignPicker.getPrescription().getAdmission() != null) {
				String wardName = "";
				String bedNumber = "";
				Date doa = null;
				Date pdd = null;
				AdmissionDetail admissionDetail = GlobalDao
						.getPatientWardBedDetails(assignPicker.getPrescription().getAdmission().getAdmissionId());
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

				pharmacologyValidationDetails = new PharmacologyValidationDetails((long) assignPicker.getId(),
						assignPicker.getPrescription().getAdmission().getPatient().getPatientId(), patientDetail,
						assignPicker.getPrescription().getAdmission().getPatient().getDob(),
						assignPicker.getPrescription().getDoctor().getDoctorId(), doctorDetail, doa, pdd,
						user.getUserId(), pickerName);
				pharmacologyValidationDetails
						.setVisitType(assignPicker.getPrescription().getVisitType().getVisitTypeId());
				pharmacologyValidationDetails.setPrescriptionId(assignPicker.getPrescription().getId());
				pharmacologyValidationDetails.setStatus(assignPicker.getStatus().getStatus());
				pharmacologyValidationDetails.setStatusId(assignPicker.getStatus().getId());
				pharmacologyValidationDetails
						.setAdmissionId(assignPicker.getPrescription().getAdmission().getAdmissionId());
				pharmacologyValidationDetails.setWardName(!wardName.equals("") ? wardName : null);
				pharmacologyValidationDetails.setBedNo(!bedNumber.equals("") ? bedNumber : null);
			}

			for (AssignPickerDetail assignPickerDetail : assignPickerDao.assignPickerDetails(id)) {
				List<BatchResponse> medicineBatches = new ArrayList<BatchResponse>();
				List<Batch> batchList = new ArrayList<Batch>();
				if (assignPickerDetail.getBatch() != null) {
					batchList = GlobalDao.getBatchListPharmacy(
							assignPickerDetail.getPrescriptionDetail().getItem().getId(),
							assignPickerDetail.getBatch().getId(), sessionService.getStoreId());
				} else {
					batchList = GlobalDao
							.getBatchDetailsPharmacy(assignPickerDetail.getPrescriptionDetail().getItem().getId());
				}
				for (Batch batch : batchList) {
					medicineBatches.add(Util.PopulateBatchWithStockWithFlag(batch, false));
				}

				if (assignPickerDetail.getBatch() != null) {
					medicineBatches.add(Util.PopulateBatchWithStockWithFlag(assignPickerDetail.getBatch(), true));
				}

				Long storeStock = GlobalDao.getStoreStockByItemPharmacy(sessionService.getStoreId(),
						assignPickerDetail.getPrescriptionDetail().getItem().getId());

				for (PrescriptionFrequencyDetail prescriptionFrequencyDetail : pharmacologyValidationDao
						.getPrescriptionFrequencyDetails(assignPickerDetail.getPrescriptionDetail().getId())) {
					PharmacologyItem pharmacologyItem = new PharmacologyItem((long) assignPickerDetail.getId(),
							assignPickerDetail.getPrescriptionDetail().getItem().getId(),
							assignPickerDetail.getPrescriptionDetail().getItem().getCode(),
							assignPickerDetail.getPrescriptionDetail().getItem().getName(),
							assignPickerDetail.getPrescriptionDetail().getItem().getGeneric().getName(),
							prescriptionFrequencyDetail != null ? prescriptionFrequencyDetail.getDose() : null,
							assignPickerDetail.getPrescriptionDetail().getPrescription().getIsIntervention(),
							assignPickerDetail.getPrescriptionDetail().getOrderQty(), storeStock, "Rack-001",
							medicineBatches);
					pharmacologyItem
							.setGenericId(assignPickerDetail.getPrescriptionDetail().getItem().getGeneric().getId());
					pharmacologyItem.setFrequency(prescriptionFrequencyDetail != null
							? prescriptionFrequencyDetail.getFrequency().getDescription()
							: null);
					pharmacologyItem.setStartDate(
							prescriptionFrequencyDetail != null ? prescriptionFrequencyDetail.getStartDate() : null);
					pharmacologyItem.setStopDate(
							prescriptionFrequencyDetail != null ? prescriptionFrequencyDetail.getStopDate() : null);
					pharmacologyItem.setPrescriptionDetailId(assignPickerDetail.getPrescriptionDetail().getId());
					pharmacologyItem.setStatusId(assignPickerDetail.getStatus().getId());
					pharmacologyItem.setStatus(assignPickerDetail.getStatus().getStatus());
					User userDetails = GlobalDao.userSearch(sessionService.getUserId());
					pharmacologyItem.setPrescriptionDetailId(assignPickerDetail.getPrescriptionDetail().getId());
					pharmacologyItem.setPicker(userDetails.getUserName());
					pharmacologyItem.setPickerId(userDetails.getUserId());
					pharmacologyItem.setQuantity(assignPickerDetail.getIssueQuantity());

					pharmacologyItemList.add(pharmacologyItem);
				}
			}
			pharmacologyValidationDetails.setItems(pharmacologyItemList);
		} catch (Exception e) {
			
		}
		return pharmacologyValidationDetails;
	}*/

	@SuppressWarnings("unlikely-arg-type")
	@Override
	@Transactional
	public PharmacologyValidationDetails assignPickerDetailsBP(Integer id,Integer unitId, Integer orgId, Integer storeId, Integer userId) throws Exception {
		try {
			// Fetch details for assign picker
			PharmacologyValidationDetails pharmacologyValidationDetails = assignPickerDao.getAssignPickerBP(id,unitId);
			User userDetails = GlobalDao.userSearch(userId);

			if (pharmacologyValidationDetails.getAdmissionId() != null) {
				pharmacologyValidationDetails.setId((long) pharmacologyValidationDetails.getAssignPickerId());
				pharmacologyValidationDetails.setPicker(userDetails.getUserName());
				pharmacologyValidationDetails.setPickerId(userDetails.getUserId());
//				List<DoctorSpecialityResp> docSpeciality = GlobalDao
//						.doctorSearchByIdValueBP(pharmacologyValidationDetails.getDoctorId());

				if (pharmacologyValidationDetails.getSpecialityName()!=null)
					pharmacologyValidationDetails.setDoctorDetail(pharmacologyValidationDetails.getDoctorDetail()
							+ " | " + pharmacologyValidationDetails.getSpecialityName());

				Integer age = Util.getAgeByBirthDate(
						pharmacologyValidationDetails.getPatientDob().toLocalDateTime().toLocalDate());
				pharmacologyValidationDetails
						.setPatientDetail(pharmacologyValidationDetails.getPatientDetail() + " | " + age);
			}

			List<PharmacologyItem> pharmacologyItemListFinale = new ArrayList<PharmacologyItem>();
			
			// Fetch items list
			List<PharmacologyItem> comparePharmacologyItemList  = new ArrayList<>();
			List<PharmacologyItem> pharmacologyItemList = assignPickerDao.assignPickerDetailsBP(id);
			List<PharmacologyItem> pharmacologyItemListAddedItems = assignPickerDao.assignPickerAddedItemsDetailsBP(id);
			List<PharmacologyItem> issuedQty = assignPickerDao.getIssuedQty(pharmacologyValidationDetails.getPrescriptionId());
			List<PharmacologyItem> issuedQuantityListAddedItems = 
					assignPickerDao.getIssuedQuantity(pharmacologyItemListAddedItems.
							stream().map(x -> x.getPrescriptionDetailId())
							.collect(Collectors.toList()), pharmacologyItemListAddedItems
							.stream().map(x -> x.getAssignPickerDetailId())
							.collect(Collectors.toList()));
			
			pharmacologyItemListAddedItems.forEach(x -> x.setAddedItem(true));
			
			comparePharmacologyItemList.addAll(pharmacologyItemList);
			if(pharmacologyItemListAddedItems != null && pharmacologyItemListAddedItems.size()>0) {
				
				for (PharmacologyItem pharmacologyItem : comparePharmacologyItemList) {
					if(pharmacologyItem.getStatusId() == 9) {
						pharmacologyItemListAddedItems.add(pharmacologyItem);
					}
				}
		/*		List<PharmacologyItem> pendingList = new ArrayList<>();
				for (PharmacologyItem pharmacologyItem : comparePharmacologyItemList) {
					int notBatch = 0;
					for (PharmacologyItem addeItem : pharmacologyItemListAddedItems) {
						
						if(pharmacologyItem.getPrescriptionDetailId().equals(addeItem.getPrescriptionDetailId())) {
							notBatch = 1; 
						}
					}
					if(notBatch == 0) {
						if(pendingList.size() == 0 ) {
							pendingList.add(pharmacologyItem);

						}else {
							int f = 0;
							for (PharmacologyItem pendingDto : pendingList) {
								
								if(pendingDto.getPrescriptionDetailId().equals(pharmacologyItem.getPrescriptionDetailId())) {
									f=1;
								}
							}
							if(f==0) {
								pendingList.add(pharmacologyItem);
							}
						}
					}
				}
				pharmacologyItemListAddedItems.addAll(pendingList);*/
				pharmacologyItemList = pharmacologyItemListAddedItems; 
			}else {
				pharmacologyItemList.addAll(pharmacologyItemListAddedItems);
			}
		
			
			// Get the list of itemId for available stock
			List<Integer> itemIds = pharmacologyItemList.stream().map(x -> x.getItemId()).collect(Collectors.toList());

			// Fetch available stock for the items
			List<Quantities> storeStocks = GlobalDao.getStoreStockByItemPharmacyBP(storeId,
					itemIds,unitId);

			// Fetch the list of all the batches
			List<BatchResponse> batchesList = GlobalDao.getBatchDetailsPharmacyBP(itemIds,unitId,storeId);

			// Get the list of batchId for UOM
			List<Long> batchIds = batchesList.stream().map(x -> x.getBatchId()).collect(Collectors.toList());

			// Get rack list
			List<Rack> rackList = new ArrayList<>();
			if (!batchIds.isEmpty())
				rackList = GlobalDao.getRackList(batchIds, storeId);

			// Fetch the UOM list
			List<UomResponse> uomList = new ArrayList<>();

			if (!batchIds.isEmpty())
				uomList = GlobalDao.getBatchUomListPharmacyBP(batchIds);

			// Get the list of prescriptionDetailId for prescription
			List<Long> prescriptionDetailIds = pharmacologyItemList.stream().map(x -> x.getPrescriptionDetailId())
					.collect(Collectors.toList());

			// Fetch the prescription
			List<PrescriptionFrequencyDetailResp> prescriptionFrequencyDetailRespAll = pharmacologyValidationDao
					.getPrescriptionFrequencyDetailsBP(prescriptionDetailIds);

			// Get list of already issued quantity
			List<PharmacologyItem> issuedQuantityList = assignPickerDao.getIssuedQuantity(prescriptionDetailIds);

			List<PharmacologyItem> pensingIssueQuantityList = assignPickerDao.getPendingIssueQuantity(prescriptionDetailIds);

			for (PharmacologyItem pharmacologyItem : pharmacologyItemList) {
				
				// Get pending issue quantity
				Optional<PharmacologyItem> pensingIssueQuantity = pensingIssueQuantityList.stream().filter(
						issue -> issue.getPrescriptionDetailId().equals(pharmacologyItem.getPrescriptionDetailId()))
						.findFirst();

				pharmacologyItem.setIsNarcotic(pharmacologyItem.getIsNarcotic());
				pharmacologyItem.setPendingIssueQuantity(0L);
				if(pharmacologyItemListAddedItems!=null && pharmacologyItemListAddedItems.size()>0) {
					for (PharmacologyItem  pendingIssueQuantityDto: issuedQty) {
							if(pharmacologyItem.getIssueDetailsId().equals(pendingIssueQuantityDto.getIssueDetailsId())) {
								pharmacologyItem.setPendingIssueQuantity(pendingIssueQuantityDto.getQuantityIssued().longValue());
							}
					}
					
				}
				int parentNotAvialable = 0; 
				int parentNotAvialableFlag = 0;
			
				
				List<PharmacologyItem> substututeList = new ArrayList<>();
				if(pharmacologyItem.getParentSubstituteId()!=null) {
					
					if(pharmacologyItemListAddedItems != null && pharmacologyItemListAddedItems.size()>0) {
						Long priscriDetailsId = null;
						Long batchId = null;
						Integer AssignPickerDetailsId = null;
						Integer counFlag = 0;
						for (PharmacologyItem compar : comparePharmacologyItemList) {
							if(compar.getPrescriptionDetailId().equals(pharmacologyItem.getPrescriptionDetailId())
									&& pharmacologyItem.getBatchId().equals(compar.getBatchId())) {
								
								for (PharmacologyItem comparAssiDetal : comparePharmacologyItemList) {
										if(compar.getParentAssignPickerId().equals(comparAssiDetal.getAssignPickerDetailId())) {
											 counFlag = 1;
											 priscriDetailsId = comparAssiDetal.getPrescriptionDetailId();
											 batchId = comparAssiDetal.getBatchId();
											 AssignPickerDetailsId = compar.getAssignPickerDetailId();
									}
									
								}
								
							}
						} 
						if(counFlag.equals(1)) {
							
							List<SubstituteItemDto> substitute=GlobalDao.getListOfSubstitutes(pharmacologyItem.getAssignPickerId(),pharmacologyItem.getAssignPickerDetailId());
							
							
							for (SubstituteItemDto substituteList : substitute) {
								
								PharmacologyItem itm=new PharmacologyItem();
								itm.setAssignPickerDetailId(AssignPickerDetailsId);
								itm.setPrescriptionDetailId(substituteList.getPrescriptionDetailid().longValue());
								itm.setFrequency(substituteList.getFrequency());
								itm.setDose(substituteList.getDose());
								itm.setGenericId(substituteList.getGenericId());
								itm.setGenericName(substituteList.getGenericName());
								itm.setItemId(substituteList.getItemId());
								itm.setItemName(substituteList.getItemName());
								itm.setInterventionFlag(substituteList.getInterventionFlag());
								itm.setRackNo("Rack-001");
								itm.setPendingIssueQuantity(0L);
								itm.setStatus(substituteList.getStatus());
								itm.setQuantityOrdered(substituteList.getQuantityOrdered());
								itm.setStatusId(4);
								if(pharmacologyItemListAddedItems!=null && pharmacologyItemListAddedItems.size()>0) {
									for (PharmacologyItem  pendingIssueQuantityDto: issuedQty) {
										if(substituteList.getBatchid() != null && pendingIssueQuantityDto.getTempBatchId()!=null) {
											if(pharmacologyItem.getPrescriptionDetailId().equals(pendingIssueQuantityDto.getParentIssueDetailsId().longValue())) {
												itm.setPendingIssueQuantity(pendingIssueQuantityDto.getQuantityIssued().longValue());
											}
										}
									}
								}
								itm.setIssuedQuantity(substituteList.getIssuedQuantity()!=null?substituteList.getIssuedQuantity().longValue():0);
								itm.setParentSubstituteId(substituteList.getParentSubstituteId());
								List<Integer> itemIds1 = substitute.stream().map(x -> x.getItemId()).collect(Collectors.toList());
								List<BatchResponse> batchesListSub = GlobalDao.getBatchDetailsPharmacyBP(itemIds1,unitId,storeId);
								List<Long> batchIds1 = batchesListSub.stream().map(x -> x.getBatchId()).collect(Collectors.toList());
								
								List<Rack> rackList1 = new ArrayList<>();
								if (!batchIds1.isEmpty())
									rackList1 = GlobalDao.getRackList(batchIds1, storeId);

								List<UomResponse> uomList1 = new ArrayList<>();

								if (!batchIds1.isEmpty())
									uomList1 = GlobalDao.getBatchUomListPharmacyBP(batchIds1);
								
								
								List<BatchResponse> mediBatchesSub = new ArrayList<>();
								List<BatchResponse> medicineBatchesSub = batchesListSub.stream()
										.filter(batch -> batch.getItemId().equals(substituteList.getItemId()))
										.collect(Collectors.toList());
				
								for (BatchResponse batchSub : medicineBatchesSub) {
				
									BatchResponse batchSub1 = (BatchResponse) batchSub.clone();
									Integer subBatchValue=batchSub1.getBatchId().intValue();
									if(subBatchValue.equals(substituteList.getBatchid())) {
										batchSub1.setIsSelected(true);
										batchSub1.setUomTypeId(substituteList.getUomTypeId());
										batchSub1.setUomUnitId(substituteList.getUomUnitId());
									}
				
									if (!uomList1.isEmpty()) {
										batchSub1.setUom(uomList1.stream().filter(uom -> uom.getBatchId().equals(batchSub1.getBatchId()))
												.collect(Collectors.toList()));
									}
				
									Optional<Rack> rackDetailsSub = rackList1.stream()
											.filter(rack -> rack.getBatchId().equals(batchSub1.getBatchId())).findFirst();
				
									if (rackDetailsSub.isPresent()) {
										batchSub1.setRackNo(rackDetailsSub.get().getRack());
									}
				
									mediBatchesSub.add(batchSub1);
								}
								itm.setBatch(mediBatchesSub);
								
							
				
								substututeList.add(itm);
							
							}
							
							if(batchId != null) {
								Integer indx = 0;
								for (PharmacologyItem dto : pharmacologyItemList) {
									if(dto.getPrescriptionDetailId().equals(priscriDetailsId) && dto.getBatchId().equals(batchId)) {
										pharmacologyItemList.get(indx).setSelectedItems(substututeList);
									}
									indx++;
								}
								parentNotAvialable = 1;
							}else {
								
								if(pharmacologyItemList !=null && pharmacologyItemList.size()>0) {
									Integer idx = 0;
									boolean checkexist = false;
									for (PharmacologyItem pharcheck : pharmacologyItemList) {
										
										if(pharcheck.getSelectedItems() != null) {
											for (PharmacologyItem subitem : pharcheck.getSelectedItems()) {
												
												if(subitem.getParentSubstituteId().equals(substututeList.get(0).getParentSubstituteId())) {
													checkexist = true;
												}
											}
											if(checkexist) {
												pharmacologyItemList.get(idx).getSelectedItems().addAll(substututeList);
												parentNotAvialable = 1;
											}
										}
										
										idx++;
									}
									if(!checkexist) {
										parentNotAvialable = 0;
										parentNotAvialableFlag = 1;
									}
								}else {
									parentNotAvialable = 0;
									parentNotAvialableFlag = 1;
								}
																	
							}
							
						}

						
					}else {
						pharmacologyItem.setSelectedItems(null);
					}
					
					
				}
				if(parentNotAvialable == 0) {
					Integer AssignPickerDetailsId = null;
					if(parentNotAvialableFlag == 1) {
						
						for (PharmacologyItem comparAssiDetal : comparePharmacologyItemList) {
							if(pharmacologyItem.getParentSubstituteId() !=null) {
								if(comparAssiDetal.getPrescriptionDetailId().equals(pharmacologyItem.getParentSubstituteId().longValue())) {
									pharmacologyItem.setItemId(comparAssiDetal.getItemId());
									pharmacologyItem.setItemCode(comparAssiDetal.getItemCode());
									pharmacologyItem.setItemName(comparAssiDetal.getItemName());
									pharmacologyItem.setParentSubstituteId(null);
									AssignPickerDetailsId = comparAssiDetal.getAssignPickerDetailId();
								}

							}
													
						}
					}else {
						for (PharmacologyItem compar : comparePharmacologyItemList) {
							if(compar.getPrescriptionDetailId().equals(pharmacologyItem.getPrescriptionDetailId())) {
								
								AssignPickerDetailsId = compar.getAssignPickerDetailId();
							}
						} 
					}
					
					
					
					pharmacologyItem.setAssignPickerDetailId(AssignPickerDetailsId);
					List<BatchResponse> mediBatches = new ArrayList<>();
					/*List<BatchResponse> medicineBatches = batchesList.stream()
							.filter(batch -> batch.getItemId().equals(pharmacologyItem.getItemId()))
							.collect(Collectors.toList());*/
					// Get the batch list by item id 
					List<BatchResponse> medicineBatches = GlobalDao
							.getBatchListByItemId(pharmacologyItem.getItemId().longValue(),storeId,unitId);

					List<UomResponse> uomsAll = new ArrayList<UomResponse>();
					if(!medicineBatches.isEmpty())
						uomsAll = GlobalDao.getBatchUomListPharmacyBP(medicineBatches.stream().map(BatchResponse::getBatchId).collect(Collectors.toList()));
					
	
					for (BatchResponse batch1 : medicineBatches) {
	
						BatchResponse batch = (BatchResponse) batch1.clone();
						
						if(batch.getAvailableStockQuantity()>0);
						List<UomResponse> uomsSpecific = uomsAll.stream().filter(x -> x.getBatchId().equals(batch.getBatchId())).collect(Collectors.toList());
						batch.setUom(uomsSpecific);
						
						if (batch.getBatchId().equals(pharmacologyItem.getBatchId())) {
							batch.setIsSelected(true);
							batch.setUomTypeId(pharmacologyItem.getUomTypeId());
							batch.setUomUnitId(pharmacologyItem.getUomUnitId());
							
							// Set uom for all the batches
							if (!uomList.isEmpty()) {
								batch.setUom(uomList.stream().filter(uom -> uom.getBatchId().equals(batch.getBatchId()))
										.collect(Collectors.toList()));
							}
						}
						else
						{
							batch.setIsSelected(false);
							UomResponse opUom = Util.getOpDispenseUom(uomsSpecific);
							if(opUom!=null) {
							batch.setUomUnitId(opUom.getUomUnitId());
							batch.setUomTypeId(opUom.getUomTypeId());
							}
						}
	
					
						
						
						// Get rack details
						Optional<Rack> rackDetails = rackList.stream()
								.filter(rack -> rack.getBatchId().equals(batch.getBatchId())).findFirst();
	
						// Set rack details
						if (rackDetails.isPresent()) {
							batch.setRackNo(rackDetails.get().getRack());
						}
	
						mediBatches.add(batch);
					}
	
					// Set batch list
					pharmacologyItem.setBatch(mediBatches);
	
					// Get available store stock
					Optional<Quantities> specificStoreStock = storeStocks.stream()
							.filter(x -> x.getItemId().equals(pharmacologyItem.getItemId())).findFirst();
	
					pharmacologyItem
							.setStockAvailable(specificStoreStock.isPresent() ? specificStoreStock.get().getQuantity() : 0);
	
					// Get prescription details
					Optional<PrescriptionFrequencyDetailResp> prescriptionFrequencyDetail = prescriptionFrequencyDetailRespAll
							.stream()
							.filter(x -> x.getPrescriptionDetaild().equals(pharmacologyItem.getPrescriptionDetailId()))
							.findFirst();
	
					if (prescriptionFrequencyDetail.isPresent()) {
						pharmacologyItem.setDose(prescriptionFrequencyDetail.get().getDose());
	
						pharmacologyItem.setFrequency(prescriptionFrequencyDetail.get().getFreqDescription());
						pharmacologyItem.setStartDate(prescriptionFrequencyDetail.get().getStartDate());
						pharmacologyItem.setStopDate(prescriptionFrequencyDetail.get().getStopDate());
						pharmacologyItem.setDoseUnit(prescriptionFrequencyDetail.get().getUnit());
						
					}
					
					// Get issued quantity
					Optional<PharmacologyItem> issuedQuantity = pensingIssueQuantity;
					if(pharmacologyItem.isAddedItem()) {
						issuedQuantity =
								issuedQuantityListAddedItems
										.stream()
										.filter(issue -> issue.getPrescriptionDetailId().equals(pharmacologyItem.getPrescriptionDetailId())
												&& issue.getAddedItemId().equals(pharmacologyItem.getAssignPickerDetailId()))
										.findFirst();
					}
					else {
						issuedQuantity =
							issuedQuantityList
									.stream()
									.filter(issue -> issue.getPrescriptionDetailId().equals(pharmacologyItem.getPrescriptionDetailId()))
									.findFirst();
					}
	
					// Set issued quantity
					pharmacologyItem.setIssuedQuantity(0L);
					if (issuedQuantity.isPresent()) {
						pharmacologyItem.setIssuedQuantity(issuedQuantity.get().getIssuedQuantity());
					}
					pharmacologyItem.setParentAssignPickerId(pharmacologyItem.getParentAssignPickerId());
					
					// Set rack number
					pharmacologyItem.setRackNo("Rack-001");
	
					pharmacologyItem.setId((long) pharmacologyItem.getAssignPickerDetailId());
					
					if(parentNotAvialableFlag == 0 ) {
						if(pharmacologyItem.getParentSubstituteId()==null) {
							
							pharmacologyItemListFinale.add(pharmacologyItem);
						}
					}else {
						
						pharmacologyItem.setSelectedItems(substututeList);
						pharmacologyItemListFinale.add(pharmacologyItem);
					}
					
				}	
				
			}

			pharmacologyValidationDetails.setItems(pharmacologyItemListFinale);

			return pharmacologyValidationDetails;
		} catch (Exception e) {
			
			throw e;
		}
	}

	/*@Override
	@Transactional
	public List<PharmacologyItem> getSubstitute(Integer id, Integer itemId, Boolean isNarcotic, Integer storeId,Integer unitId) throws Exception {
		List<PharmacologyItem> pharmacologyItemList = new ArrayList<PharmacologyItem>();
		for (Item assignPickerItem : salesWorkListDao.getSubstituteById(id, itemId, isNarcotic, storeId)) {
			List<BatchResponse> medicineBatches = new ArrayList<BatchResponse>();
			List<Batch> batchList = GlobalDao.getBatchDetailsPharmacyConsignment(assignPickerItem.getId(),storeId,unitId);

			for (Batch batch : batchList) {
				medicineBatches.add(Util.PopulateBatchWithStock(batch));
			}

			Long storeStock = GlobalDao.getStoreStockByItemPharmacy(storeId,
					assignPickerItem.getId(),unitId);
			
			Boolean isOtc=assignPickerItem.getIsOtc()!=null ? assignPickerItem.getIsOtc():null;
			String manufacturerName=assignPickerItem.getManufacturer().getName()!=null ?assignPickerItem.getManufacturer().getName():null;
			Integer taxId=assignPickerItem.getSalesTax().getId();
			Double taxPercentage=assignPickerItem.getSalesTax().getTaxPercentage();
			Integer productCategoryId=assignPickerItem.getGeneric().getProductCategory().getId()!=null ? assignPickerItem.getGeneric().getProductCategory().getId():null;
//			Integer productCategoryId=assignPickerItem.getProductCategory()!=null ? assignPickerItem.getProductCategory().getId():null;
			Boolean narcoticFlag=assignPickerItem.getIsNarcotic();
			
			if(!medicineBatches.isEmpty() && storeStock != 0 ) {
				PharmacologyItem pharmacologyItem = new PharmacologyItem(assignPickerItem.getId(),
						assignPickerItem.getCode(), assignPickerItem.getName(), assignPickerItem.getGeneric().getName(),
						storeStock, "Rack-001", medicineBatches, taxId, productCategoryId, narcoticFlag, manufacturerName, isOtc,taxPercentage);

				pharmacologyItem.setGenericId(assignPickerItem.getGeneric().getId());
				pharmacologyItem.setIsBatchRequired(assignPickerItem.getIsBatchRequired());
				List<Integer> itemIds=new ArrayList<>();
				itemIds.add(assignPickerItem.getId());
				List<UomResponse> uoms =  GlobalDao.getUnitOfMeasurementListByItemBP(itemIds,assignPickerItem.getOrganizationId());
				pharmacologyItem.setUoms(uoms);
				pharmacologyItemList.add(pharmacologyItem);
			}
			
		}
		return pharmacologyItemList;
	}*/
	
	/**Added by chetana on 26-11-2021 for HIS-17599 QNRI - Substitute screen should sort depending on formulation type added in prescription. Old API has been commented above.**/
	@Override
	@Transactional
	public List<PharmacologyItem> getSubstitute(Integer id, Integer itemId, Boolean isNarcotic, Integer storeId,Integer unitId, Integer formulationTypeId) throws Exception {
		List<PharmacologyItem> pharmacologyItemList = new ArrayList<PharmacologyItem>();
		List<Batch> batchListAll = new ArrayList<Batch>();
        List<UomResponse> uomResponsesAll = GlobalDao.getAllUomListAllByUnit(unitId);
        List<SubstituteItemDto> subItemsAll=salesWorkListDao.getSubstituteById(id, itemId, isNarcotic, storeId, formulationTypeId);
        List<Integer> subDetailsIds = subItemsAll.stream().map(SubstituteItemDto::getItemId).collect(Collectors.toList());
        List<UomResponse> storeStockAll= new ArrayList<UomResponse>();
        if(subDetailsIds.size()>0)
        {
        	storeStockAll=GlobalDao.getStoreStockByItemPharmacyAll(subDetailsIds,storeId,unitId);
			batchListAll =  GlobalDao.getBatchDetailsPharmacyConsignmentAll(storeId,unitId,subDetailsIds);
        }
		for (SubstituteItemDto assignPickerItem : subItemsAll) {
			List<BatchResponse> medicineBatches = new ArrayList<BatchResponse>();
			
            Stream<Batch> batchListAllStream = batchListAll.stream();
            List<Batch> batchList= batchListAllStream
            .filter(str -> str.getItem().getId().equals(assignPickerItem.getItemId()))
            .collect(Collectors.toList());
			for (Batch batch : batchList) 
			{
				medicineBatches.add(Util.PopulateBatchWithStock(batch));
			}
			Long storeStock=(long) 0;
			if(storeStockAll!=null && storeStockAll.size()>0)
			{
			UomResponse detailsDto = storeStockAll.stream()
                    .filter(order -> assignPickerItem.getItemId().equals(order.getItemId()))
                    .findAny()
                    .orElse(null);
			
            if(detailsDto!=null)
            {
            	 storeStock = detailsDto.getStockAvailable();
            }
			}
			Boolean isOtc=assignPickerItem.getIsOtc()!=null ? assignPickerItem.getIsOtc():null;
			String manufacturerName=assignPickerItem.getManufacturerName()!=null ?assignPickerItem.getManufacturerName():null;
			Integer taxId=assignPickerItem.getTaxId();
			Double taxPercentage=assignPickerItem.getTaxPercentage();
			Integer productCategoryId=assignPickerItem.getProductCategoryId()!=null ? assignPickerItem.getProductCategoryId():null;
//			Integer productCategoryId=assignPickerItem.getProductCategory()!=null ? assignPickerItem.getProductCategory().getId():null;
			Boolean narcoticFlag=assignPickerItem.getIsNarcotic();
			
			if(!medicineBatches.isEmpty() && storeStock != 0 ) {
				PharmacologyItem pharmacologyItem = new PharmacologyItem(assignPickerItem.getItemId(),
						assignPickerItem.getItemCode(), assignPickerItem.getItemName(), assignPickerItem.getGenericName(),
						storeStock, assignPickerItem.getStorageUnitName(), medicineBatches, taxId, productCategoryId, narcoticFlag, manufacturerName, isOtc,taxPercentage);

				pharmacologyItem.setGenericId(assignPickerItem.getGenericId());
				pharmacologyItem.setIsBatchRequired(assignPickerItem.getIsBatchRequired());
				pharmacologyItem.setIsThreeUOMApplicable(assignPickerItem.getIsThreeUOMApplicable());
				pharmacologyItem.setIsLasa(assignPickerItem.getIsLasa() != null ? assignPickerItem.getIsLasa() : false);
				List<Integer> itemIds=new ArrayList<>();
				itemIds.add(assignPickerItem.getItemId());
				pharmacologyItem.setDetails(assignPickerItem.getItemCode()+" | "+assignPickerItem.getItemName());
				
                Stream<UomResponse> uomListStream = uomResponsesAll.stream();
                List<UomResponse> uoms=uomListStream
                .filter(str -> str.getItemId()!=null && str.getItemId().equals(assignPickerItem.getItemId()))
                .collect(Collectors.toList());
				pharmacologyItem.setUoms(uoms);
				pharmacologyItem.setFormulationTypeId(assignPickerItem.getFormulationTypeId());
				pharmacologyItemList.add(pharmacologyItem);
			}
			
		}
		return pharmacologyItemList;
	}


	@Override
	@Transactional
	public List<PharmacologyItem> getSubstituteBP(Integer id, Integer itemId) throws Exception {
		List<PharmacologyItem> pharmacologyItemList = salesWorkListDao.getSubstituteByIdAssignPickerBP(id, itemId);
		List<Integer> itemIds = pharmacologyItemList.stream().map(PharmacologyItem::getItemId)
				.collect(Collectors.toList());
		List<BatchResponse> medicineBatchesAll = GlobalDao.getBatchDetailsPharmacyBP(itemIds,null,null);
		List<UomResponse> uomsAll = new ArrayList<UomResponse>();
		if (!medicineBatchesAll.isEmpty())
			uomsAll = GlobalDao.getBatchUomListPharmacyBP(
					medicineBatchesAll.stream().map(BatchResponse::getBatchId).collect(Collectors.toList()));
		List<Quantities> storeStocks = GlobalDao.getStoreStockByItemPharmacyBP(sessionService.getStoreId(), itemIds,null);
		for (PharmacologyItem pharmacologyItem : pharmacologyItemList) {
			List<BatchResponse> medicineBatches = medicineBatchesAll.stream()
					.filter(x -> x.getItemId().equals(pharmacologyItem.getItemId())).collect(Collectors.toList());
			for (BatchResponse batch : medicineBatches) {
				batch.setUom(uomsAll.stream().filter(x -> x.getBatchId().equals(batch.getBatchId()))
						.collect(Collectors.toList()));
			}
			pharmacologyItem.setBatch(medicineBatches);

			List<Quantities> storeStockSpecific = storeStocks.stream()
					.filter(x -> x.getItemId().equals(pharmacologyItem.getItemId())).collect(Collectors.toList());
			if (storeStockSpecific != null && storeStockSpecific.isEmpty())
				pharmacologyItem.setStockAvailable(storeStockSpecific.get(0).getQuantity());

		}
		return pharmacologyItemList;
	}

	/*
	 * @Override
	 * 
	 * @Transactional public Object assignPickerSave(AssignPickerSave
	 * assignPickerSave) { AssignPicker assignPickerObject = new AssignPicker(new
	 * User(assignPickerSave.getPickerId()), new
	 * Prescription(assignPicker


.getPrescriptionId()), new
	 * Status(assignPickerSave.getStatusId()));
	 * 
	 * for (AssignPickerDetailItem item : assignPickerSave.getItems()) {
	 * assignPickerObject.addAssignPickerDetail(new AssignPickerDetail( new
	 * PrescriptionDetail(item.getPrescriptionDetailId()), new
	 * Batch(item.getBatchId()), new UomType(item.getUomTypeId()), new
	 * UomUnit(item.getUomUnitId()), item.getQuanityIssued(), new
	 * Status(assignPickerSave.getStatusId()))); } AssignPicker assignPickerResponse
	 * = assignPickerDao.saveAssignPicker(assignPickerObject); CommonResponse
	 * response = new CommonResponse();
	 * response.setId(assignPickerResponse.getId().longValue());
	 * response.setMessage("Assign Picker saved successfully");
	 * 
	 * return response; }
	 */

	@SuppressWarnings({ "rawtypes"})
	@Override
	@Transactional
	public Object assignPickerSave(AssignPickerSave assignPickerSave, boolean isNarcotics) throws CustomException 
	{
		AssignPicker assignPickerObject = assignPickerDao.getAssignPicker(assignPickerSave.getId());
		assignPickerObject.updateAssignPicker(new User(assignPickerSave.getPickerId()),
				new Status(assignPickerSave.getStatusId()));
		for (AssignPickerDetailItem item : assignPickerSave.getItems()) 
		{
			if(item.getPrescriptionDetailId() == null) {
				  PrescriptionDetailsDto prescriptionDtl=new PrescriptionDetailsDto();
                  prescriptionDtl = assignPickerDao.getPrescriptionDtlsByPrescriptionId(assignPickerSave.getPrescriptionId(), item.getParentItemId());
                  item.setPrescriptionDetailsDto(prescriptionDtl);
                  
                  AssignPickerDetailItem assignPickerDetailItem=new AssignPickerDetailItem();
                  assignPickerDetailItem = assignPickerDao.getAssignPickerDetailItemByPrescriptionId(assignPickerSave.getId(), prescriptionDtl.getPrescriptionDetailsid());
				  item.setAssignPickerDetailItemDto(assignPickerDetailItem);
			}
		}
		
		 saveIssueAndIssueDetails(assignPickerSave,assignPickerObject,isNarcotics);
		CommonResponse response = new CommonResponse();
		response.setMessage("Assign Picker saved successfully");

		return response;
		
	}

	@SuppressWarnings("unused")
	private void saveIssueAndIssueDetails(AssignPickerSave assignPickerSave,AssignPicker assignPickerObject, boolean isNarcotis) throws CustomException {
		String billNumber = GlobalService.getBillNoByGeneratedCode(sessionService.getStoreId(), sessionService.getUnitId(), issueScreenId);

		if (assignPickerSave.getVisitType() != 4 && assignPickerSave.getDoctorId() == null)
			throw new CustomException("Doctor id can not be null");
		
			Issue issue = new Issue(billNumber,
					(assignPickerSave.getPrescriptionId() != null) ? new Prescription(assignPickerSave.getPrescriptionId())
							: null,
					(assignPickerSave.getVisitType() != null) ? new VisitType(assignPickerSave.getVisitType()) : null,
					(assignPickerSave.getDoctorId() != null) ? new Doctor(assignPickerSave.getDoctorId()) : null,
					new Store(sessionService.getStoreId()),
					new Status(com.param.common.constants.Status.Pending.getStatus()),
					new AssignPicker(assignPickerObject.getId()));
			issue.setIsImplant(false);
			issue.setIsNarcotics(isNarcotis);
			issue.setIsConsignment(false);
			Store str=new Store();
			str.setStoreId(assignPickerSave.getStoreId());
			issue.setStore(str);
			//sessionService.setEntityDefaults(issue);
			issue.setCreateDefaults(assignPickerSave.getOrganizationId()!=null ? assignPickerSave.getOrganizationId() : sessionService.getOrganizationId(),assignPickerSave.getUnitId() !=null ? assignPickerSave.getUnitId() :sessionService.getUnitId() ,assignPickerSave.getUserId()!=null ? assignPickerSave.getUserId() : sessionService.getUserId());

			if (assignPickerSave.getAdmissionId() != null) {
				issue.setAdmission(new Admission(assignPickerSave.getAdmissionId()));
			}

			List<AssignPickerDetailAddedItems> assignPickerDetailAddedItems = new ArrayList<AssignPickerDetailAddedItems>();
			 PrescriptionDetailsDto prescriptionDtlObj=null;
			 AssignPickerDetailItem assignPickerDetailItemObj=null;
			Integer saveFlag = 0;
			Integer saveFlagSubstitute = 0;
			Integer saveFlagSubstituteAssignPic = 0;
			for (AssignPickerDetail detail : assignPickerObject.getAssignPickerDetails()) 
			{
				detail.getPrescriptionDetail().getItem().getId();
				for (AssignPickerDetailItem item : assignPickerSave.getItems()) 
				{
					if(item.getPrescriptionDetailId() != null && item.getIsNarcotics() !=null && item.getIsNarcotics() == isNarcotis) 
					{
						if (item.getId() != null && detail.getId().equals(item.getId())) {
							saveFlag = 1;
							saveFlagSubstitute=1;
							saveFlagSubstituteAssignPic=1;

							detail.updateAssignPickerDetail(new Batch(item.getBatchId()), new UomType(item.getUomTypeId()),
									new UomUnit(item.getUomUnitId()), item.getQuanityIssued(), new Status(item.getStatusId()));
							if(detail.getIssueQuantity() != null)
								detail.setIssueQuantity(item.getQuanityIssued() + detail.getIssueQuantity());
							else
								detail.setIssueQuantity(item.getQuanityIssued());
							
							IssueDetail issueDetail = new IssueDetail(new AssignPickerDetail(detail.getId()),
									(item.getPrescriptionDetailId() != null)
											? new PrescriptionDetail(item.getPrescriptionDetailId())
											: null,
									new Status(com.param.common.constants.Status.Pending.getStatus()), item.getQuanityIssued());
							issueDetail.setBatch(new Batch(item.getBatchId()));
							issueDetail.setUomType(new UomType(item.getUomTypeId()));
							issueDetail.setUomUnit(new UomUnit(item.getUomUnitId()));
							issueDetail.setIsFromAssignPicker(true);
							issueDetail.setIsNarcoticSubstitute(false);
							issueDetail.setIssueQuantity(0);
							issueDetail.setIsSubstitute(false);
						//	sessionService.setEntityDefaults(issueDetail);
							issueDetail.setCreateDefaults(assignPickerSave.getOrganizationId()!=null ? assignPickerSave.getOrganizationId() : sessionService.getOrganizationId(),assignPickerSave.getUnitId() !=null ? assignPickerSave.getUnitId() :sessionService.getUnitId() ,assignPickerSave.getUserId()!=null ? assignPickerSave.getUserId() : sessionService.getUserId());

							issue.addIssueDetail(issueDetail);
							
							Integer leaseQuantity = 0 ;
								Batch batchObj = GlobalDao.getBatch(item.getBatchId());
								if (batchObj != null) {
									 leaseQuantity = Util.GetLeaseQuantity(item.getQuanityIssued(), batchObj.getBatchUomList(),
											new UomType(item.getUomTypeId()));
								}
							 
							 
								AssignPickerDetailAddedItems addedItems2 = new AssignPickerDetailAddedItems();
								addedItems2.setPrescriptionDetail(new PrescriptionDetail(item.getPrescriptionDetailId()));
								addedItems2.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
								addedItems2.setBatch(new Batch(item.getBatchId()));
								addedItems2.setUomType(new UomType(item.getUomTypeId()));
								addedItems2.setIsActive(true);
								addedItems2.setIsDeleted(false);
								addedItems2.setUomUnit(new UomUnit(item.getUomUnitId()));
								addedItems2.setIssueQuantity(item.getQuanityIssued());
								addedItems2.setAssignPicker(assignPickerObject);
								addedItems2.setIssueDetail(issueDetail);
								addedItems2.setLeaseQuantity(leaseQuantity);
								addedItems2.setCreateDefaults(assignPickerSave.getOrganizationId()!=null ? assignPickerSave.getOrganizationId() : sessionService.getOrganizationId(),
								assignPickerSave.getUnitId() !=null ? assignPickerSave.getUnitId() :sessionService.getUnitId() ,assignPickerSave.getUserId()!=null ? assignPickerSave.getUserId() : sessionService.getUserId());

								assignPickerDetailAddedItems.add(addedItems2);
						}
						
					}
					if(item.getPrescriptionDetailId() == null) {
                        if (item.getId() != null && detail.getId().equals(item.getId())) {
                        	
                      /* 
                        PrescriptionDetailsDto prescriptionDtl=new PrescriptionDetailsDto();
                        if(prescriptionDtlObj==null) {
                        	prescriptionDtl = assignPickerDao.getPrescriptionDtlsByPrescriptionId(assignPickerSave.getPrescriptionId(), item.getParentItemId());
                    		prescriptionDtlObj=prescriptionDtl;
                        }else {
                        	if(saveFlagSubstitute==1) {
                        		prescriptionDtl = assignPickerDao.getPrescriptionDtlsByPrescriptionId(assignPickerSave.getPrescriptionId(), item.getParentItemId());
                        		prescriptionDtlObj=prescriptionDtl;
                        	}else {
                        		prescriptionDtl=prescriptionDtlObj;
                        	}
                        }
                        	
						*/
						
						if(item.getPrescriptionDetailsDto().getIsNarcotic() == isNarcotis) {
									saveFlag = 1;
									saveFlagSubstitute=0;
									saveFlagSubstituteAssignPic = 0;
									PrescriptionDetail prescriptionDetail = new PrescriptionDetail();
									prescriptionDetail.setAuthorisedBy(item.getPrescriptionDetailsDto().getAuthorisedBy()!= null ?item.getPrescriptionDetailsDto().getAuthorisedBy():null );
									prescriptionDetail.setAuthorisedDate(item.getPrescriptionDetailsDto().getAuthorisedDate());
									if(item.getPrescriptionDetailsDto().getInstructionId()!=null) {
										Instruction ins=new Instruction();
										ins.setId(item.getPrescriptionDetailsDto().getInstructionId());
										prescriptionDetail.setInstruction(ins);
									}
									if(item.getPrescriptionDetailsDto().getInstructionDesc()!=null) {
										prescriptionDetail.setInstructionDesc(item.getPrescriptionDetailsDto().getInstructionDesc());
									}
									prescriptionDetail.setPendingQty(item.getPrescriptionDetailsDto().getPendingQty());
									prescriptionDetail.setOrderQty(item.getPrescriptionDetailsDto().getOrderQty());
									prescriptionDetail.setPrescriptionDate(item.getPrescriptionDetailsDto().getPrescriptionDate());
									Store store=new Store();
									store.setStoreId(sessionService.getStoreId());
									prescriptionDetail.setStore(store);
									prescriptionDetail.setIsDiscontinued(false);
									prescriptionDetail.setIsIntervention(false);
									prescriptionDetail.setIsPreStop(false);
									prescriptionDetail.setIsDeleted(false);
									prescriptionDetail.setIsActive(true);
									prescriptionDetail.setStrength(item.getPrescriptionDetailsDto().getStrength());
									prescriptionDetail.setAddedDate(TimeZoneComponent.getDateByzone(assignPickerSave.getUnitId()));
									prescriptionDetail.setInfusionFlag(item.getPrescriptionDetailsDto().getInfusionflag());
									Item itm=new Item();
									itm.setId(item.getItemId());
									prescriptionDetail.setItem(itm);
									prescriptionDetail.setParentSubstituteId(item.getPrescriptionDetailsDto().getPrescriptionDetailsid());
									Prescription pres = new Prescription();
									pres.setId(item.getPrescriptionDetailsDto().getPrescriptionId().longValue());
									prescriptionDetail.setPrescription(pres);
									
									//sessionService.setEntityDefaults(prescriptionDetail);
									prescriptionDetail.setCreateDefaults(assignPickerSave.getOrganizationId()!=null ? assignPickerSave.getOrganizationId() : sessionService.getOrganizationId(),assignPickerSave.getUnitId() !=null ? assignPickerSave.getUnitId() :sessionService.getUnitId() ,assignPickerSave.getUserId()!=null ? assignPickerSave.getUserId() : sessionService.getUserId());

									prescriptionDetail = GlobalDao.savePrescriptionDetailForSubstitute(prescriptionDetail);
									
									
										PrescriptionFrequencyDetail pfd = new PrescriptionFrequencyDetail();
										
										PrescriptionDetail pd = new PrescriptionDetail();
										pd.setId(prescriptionDetail.getId().longValue());
										pfd.setPrescriptionDetail(pd);
										if(item.getPrescriptionDetailsDto().getRouteId()!=null){
											RouteOfAdministration roa=new RouteOfAdministration();
											roa.setId(item.getPrescriptionDetailsDto().getRouteId());
											pfd.setRouteOfAdministration(roa);
										}
										pfd.setStartDate(item.getPrescriptionDetailsDto().getStartDate());
										pfd.setStopDate(item.getPrescriptionDetailsDto().getStopDate());
										StrengthUnit strnUnit=new StrengthUnit();
										if(item.getPrescriptionDetailsDto().getDoseUnitId()!=null) {
											strnUnit.setId(item.getPrescriptionDetailsDto().getDoseUnitId());
											pfd.setStrengthUnit(strnUnit);
										}
										pfd.setDose(item.getPrescriptionDetailsDto().getDose());
										Frequency freq=new Frequency();
										freq.setId(item.getPrescriptionDetailsDto().getFrequencyId());
										pfd.setFrequency(freq);
										pfd.setIsActive(true);
										pfd.setIsDeleted(false);
										StrengthUnit strngth=new StrengthUnit();
										if(item.getPrescriptionDetailsDto().getDoseUnitId()!=null){
											strngth.setId(item.getPrescriptionDetailsDto().getDoseUnitId());
											pfd.setStrengthUnit(strngth);
										}
										//sessionService.setEntityDefaults(pfd);
										pfd.setCreateDefaults(assignPickerSave.getOrganizationId()!=null ? assignPickerSave.getOrganizationId() : sessionService.getOrganizationId(),assignPickerSave.getUnitId() !=null ? assignPickerSave.getUnitId() :sessionService.getUnitId() ,assignPickerSave.getUserId()!=null ? assignPickerSave.getUserId() : sessionService.getUserId());

										pfd = GlobalDao.savePrescriptionFrequencyDetail(pfd);
										
//								AssignPickerDetailItem assignPickerDetailItem = assignPickerDao.getAssignPickerDetailItemByPrescriptionId(assignPickerSave.getId(), prescriptionDtl.getPrescriptionDetailsid());
									/*	AssignPickerDetailItem assignPickerDetailItem=new AssignPickerDetailItem();
					                        if(assignPickerDetailItemObj==null) {
					                        	assignPickerDetailItem = assignPickerDao.getAssignPickerDetailItemByPrescriptionId(assignPickerSave.getId(), prescriptionDtl.getPrescriptionDetailsid());
					                        	assignPickerDetailItemObj=assignPickerDetailItem;
					                        }else {
					                        	if(saveFlagSubstituteAssignPic==1) {
					                        		assignPickerDetailItem = assignPickerDao.getAssignPickerDetailItemByPrescriptionId(assignPickerSave.getId(), prescriptionDtl.getPrescriptionDetailsid());
					                        		assignPickerDetailItemObj=assignPickerDetailItem;
					                        	}else {
					                        		assignPickerDetailItem=assignPickerDetailItemObj;
					                        	}
					                        }*/
					                        	
									AssignPickerDetail assignPickerDetail=new AssignPickerDetail();
									AssignPicker assignPicker=new AssignPicker();
									assignPicker.setId(item.getAssignPickerDetailItemDto().getAssignPickerId());
									assignPickerDetail.setAssignPicker(assignPicker);
									PrescriptionDetail presdtl=new PrescriptionDetail();
									presdtl.setId(prescriptionDetail.getId().longValue());
									assignPickerDetail.setPrescriptionDetail(presdtl);
									assignPickerDetail.setParentAssignPickerId(item.getAssignPickerDetailItemDto().getId());
									assignPickerDetail.setIsActive(true);
									assignPickerDetail.setIsDeleted(false);
									assignPickerDetail.setIssueQuantity(item.getAssignPickerDetailItemDto().getQuanityIssued());
									assignPickerDetail.setPickedQuantity(item.getQuanityIssued());
									Batch bth=new Batch();
									bth.setId(item.getBatchId().longValue());
									assignPickerDetail.setBatch(bth);
									UomType uom=new UomType();
									uom.setId(item.getUomTypeId());
									assignPickerDetail.setUomType(uom);
									UomUnit uomUnit=new UomUnit();
									uomUnit.setId(item.getUomUnitId());
									assignPickerDetail.setUomUnit(uomUnit);
									Status sts=new Status();
									sts.setId(item.getAssignPickerDetailItemDto().getStatusId());
									assignPickerDetail.setStatus(sts);
									//sessionService.setEntityDefaults(assignPickerDetail);
									assignPickerDetail.setCreateDefaults(assignPickerSave.getOrganizationId()!=null ? assignPickerSave.getOrganizationId() : sessionService.getOrganizationId(),assignPickerSave.getUnitId() !=null ? assignPickerSave.getUnitId() :sessionService.getUnitId() ,assignPickerSave.getUserId()!=null ? assignPickerSave.getUserId() : sessionService.getUserId());

									GlobalDao.saveAssignPickerDetailItem(assignPickerDetail);
									
									if (item.getId() != null && detail.getId().equals(item.getId())) {
										
										if(detail.getIssueQuantity() != null)
											detail.setIssueQuantity(item.getQuanityIssued() + detail.getIssueQuantity());
										else
											detail.setIssueQuantity(item.getQuanityIssued());
										
										IssueDetail issueDetail = new IssueDetail(new AssignPickerDetail(detail.getId()),
												(prescriptionDetail.getId() != null)
														? new PrescriptionDetail(prescriptionDetail.getId())
														: null,
												new Status(com.param.common.constants.Status.Pending.getStatus()), item.getQuanityIssued());
										if(item.getPrescriptionDetailsDto().getIsNarcotic()==true) {
											issueDetail.setIsNarcoticSubstitute(true);
										}else {
											issueDetail.setIsNarcoticSubstitute(false);
										}
										issueDetail.setBatch(new Batch(item.getBatchId()));
										issueDetail.setIsFromAssignPicker(true);
										issueDetail.setIsSubstitute(true);
										issueDetail.setIssueQuantity(0);
										issueDetail.setUomType(new UomType(item.getUomTypeId()));
										issueDetail.setUomUnit(new UomUnit(item.getUomUnitId()));
										//sessionService.setEntityDefaults(issueDetail);
										issueDetail.setCreateDefaults(assignPickerSave.getOrganizationId()!=null ? assignPickerSave.getOrganizationId() : sessionService.getOrganizationId(),assignPickerSave.getUnitId() !=null ? assignPickerSave.getUnitId() :sessionService.getUnitId() ,assignPickerSave.getUserId()!=null ? assignPickerSave.getUserId() : sessionService.getUserId());

										issue.addIssueDetail(issueDetail);
										
										
										Integer leaseQuantity = 0 ;
											Batch batchObj = GlobalDao.getBatch(item.getBatchId());
											if (batchObj != null) {
												 leaseQuantity = Util.GetLeaseQuantity(item.getQuanityIssued(), batchObj.getBatchUomList(),
														new UomType(item.getUomTypeId()));
											}
										 
										
										AssignPickerDetailAddedItems addedItems=new AssignPickerDetailAddedItems();
										
										PrescriptionDetail presdtl1=new PrescriptionDetail();
										presdtl1.setId(prescriptionDetail.getId().longValue());
										addedItems.setPrescriptionDetail(presdtl);
										AssignPicker assignPicker1=new AssignPicker();
										assignPicker1.setId(item.getAssignPickerDetailItemDto().getAssignPickerId());
										addedItems.setAssignPicker(assignPicker1);
										addedItems.setIsActive(true);
										addedItems.setIsDeleted(false);
										addedItems.setUomType(new UomType(item.getUomTypeId()));
										addedItems.setUomUnit(new UomUnit(item.getUomUnitId()));
										addedItems.setIssueQuantity(item.getQuanityIssued());
										Batch bth1=new Batch();
										bth1.setId(item.getBatchId().longValue());
										addedItems.setBatch(bth1);
										Status sts1=new Status();
										sts1.setId(item.getAssignPickerDetailItemDto().getStatusId());
										assignPickerDetail.setStatus(sts1);
										addedItems.setStatus(new Status(com.param.common.constants.Status.Completed.getStatus()));
										addedItems.setIssueDetail(issueDetail);
										addedItems.setLeaseQuantity(leaseQuantity);
										//sessionService.setEntityDefaults(addedItems);
										addedItems.setCreateDefaults(assignPickerSave.getOrganizationId()!=null ? assignPickerSave.getOrganizationId() : sessionService.getOrganizationId(),assignPickerSave.getUnitId() !=null ? assignPickerSave.getUnitId() :sessionService.getUnitId() ,assignPickerSave.getUserId()!=null ? assignPickerSave.getUserId() : sessionService.getUserId());

										addedItems=GlobalDao.saveAssignPickerDetailAddedItem(addedItems);										
										
										
								}
									
		                        }

									
							}
					
					}	
				}
				}
			
			if(saveFlag ==1) {
				
				if (!assignPickerDetailAddedItems.isEmpty())
					assignPickerObject.setAssignPickerDetailAddedItems(assignPickerDetailAddedItems);

				AssignPicker assignPickerResponse = assignPickerDao.updateAssignPicker(assignPickerObject);
				assignPickerDao.saveIssue(issue);
			}
			
		
	}

	@Override
	@Transactional
	public void cancelAssignPicker(CancelRequest cancelRequest) throws CustomException {

		AssignPicker assignPicker = assignPickerDao.getAssignPicker(cancelRequest.getId());

		if (assignPicker == null)
			throw new CustomException("AssignPicker not found in DB");

		if ((!assignPicker.getStatus().getId().equals(com.param.common.constants.Status.Pending.getStatus())))
			throw new CustomException("Can not CANCEL. Status is : " + assignPicker.getStatus().getStatus());

		assignPicker.setStatus(new Status(cancelRequest.getStatusId()));
		assignPickerDao.updateAssignPicker(assignPicker);
	}
}