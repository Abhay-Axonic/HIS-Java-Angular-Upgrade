package com.param.pharmacy.op.service.salesWorkList;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.common.constants.Screen;
import com.param.entity.model.inventory.Batch;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.entity.model.master.DiscountCategory;
import com.param.entity.model.master.Status;
import com.param.entity.model.pharmacy.AssignPicker;
import com.param.entity.model.pharmacy.AssignPickerDetail;
import com.param.entity.model.pharmacy.PharmacyTariff;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.entity.model.pharmacy.SaleDetail;
import com.param.global.billing.dao.IBillingDao;
import com.param.global.common.Response;
import com.param.global.dao.IPatientSponsorDetailsDao;
import com.param.global.dao.global.IGlobalDao;
import com.param.global.dto.AdmissionsForBatchProcessDto;
import com.param.global.dto.OrderDetailsByEncounterIdDto;
import com.param.global.dto.global.PatientConsession;
import com.param.global.dto.items.BatchResponse;
import com.param.global.dto.items.UomResponse;
import com.param.global.model.PatientSponsorDetails;
import com.param.global.pharmacy.dto.SubstituteItemDto;
import com.param.global.utility.Util;
import com.param.pharmacy.ip.dao.assignPicker.IAssignPickerDao;
import com.param.pharmacy.ip.dto.assignPicker.CancelRequest;
import com.param.pharmacy.master.global.dao.equipment.IDepartmentStoreMapperDao;
import com.param.pharmacy.master.global.dto.equipment.DepartmentStoreMapperDto;
import com.param.pharmacy.master.unit.dao.applicationConfiguration.IApplicationConfigurationDao;
import com.param.pharmacy.op.dao.salesReturn.ISalesReturnDao;
import com.param.pharmacy.op.dao.salesWorkList.ISalesWorkListDao;
import com.param.pharmacy.op.dto.SaleDetailResp;
import com.param.pharmacy.op.dto.SalesWorkQty;
import com.param.pharmacy.op.dto.salesWorkList.DiscountCategoryResponse;
import com.param.pharmacy.op.dto.salesWorkList.FrequencyResponse;
import com.param.pharmacy.op.dto.salesWorkList.PrescriptionDetailItemResponse;
import com.param.pharmacy.op.dto.salesWorkList.PrescriptionDetailResponse;
import com.param.pharmacy.op.dto.salesWorkList.ResponseWorkListSearch;
import com.param.pharmacy.op.dto.salesWorkList.SalesWorkListDropdowns;
import com.param.pharmacy.op.dto.salesWorkList.WorkListSearch;
import com.param.pharmacy.op.service.walkInSales.CustomException;
import com.param.service.global.SessionService;

@Service("salesWorkListService")
@Transactional
public class SalesWorkListService implements ISalesWorkListService {

	@Autowired
	private ISalesWorkListDao salesWorkListDao;

	@Autowired
	private ISalesReturnDao salesReturnDao;

	@Autowired
	private IGlobalDao GlobalDao;

	@Autowired
	IGlobalDao IGlobalDao;

	@Autowired
	IPatientSponsorDetailsDao iPatientSponsorDetailsDao;
	
	@Autowired
	IApplicationConfigurationDao iApplicationConfigurationDao;
	
	@Autowired
	private IDepartmentStoreMapperDao iDepartmentStoreMapperDao;
	
	@Autowired
	private IAssignPickerDao assignPickerDao;
	
	@Autowired
	IBillingDao iBillingDao;
	
	
	private Integer screenId = Screen.SaleWorkList.getScreen();

	@Override
	public SalesWorkListDropdowns getDropdownList() {
		SalesWorkListDropdowns response = new SalesWorkListDropdowns();
		response.setStatus(IGlobalDao.getScreenStatusBetterPerformance(screenId));
		response.setVisitType(IGlobalDao.getVisitTypeListBetterPerformance());
		/*Original Code
		 * for (ScreenStatus status : IGlobalDao.getScreenStatus(screenId)) {
		 * response.addStatus( new
		 * com.param.global.dto.global.Status(status.getStatus().getId(),
		 * status.getStatus().getStatus())); }
		 * 
		 * for (VisitType visitType : IGlobalDao.getVisitTypeList()) {
		 * response.addVisitType(new
		 * com.param.global.dto.global.VisitType(visitType.getVisitTypeId(),
		 * visitType.getVisitTypeName())); }
		 */
		 
		return response;
	}

	/*	@Override
		public List<ResponseWorkListSearch> worklistSearch(WorkListSearch WorkListSearch) {
		
			List<ResponseWorkListSearch> responseWorkListSearchList = new ArrayList<ResponseWorkListSearch>();
			WorkListSearch.setToDate((new DateTime(WorkListSearch.getToDate())).plusDays(1).toDate());
		
			for (Prescription prescriptionDetail : salesWorkListDao.worklistSearchByFields(WorkListSearch, screenId)) {
				List<DoctorSpecialityMapper> doctorSpeciality = IGlobalDao
						.doctorSearchByIdValue(prescriptionDetail.getDoctor().getDoctorId());
				String doctorDetail = prescriptionDetail.getDoctor().getFirstName() + " "
						+ prescriptionDetail.getDoctor().getMiddleName() + " "
						+ prescriptionDetail.getDoctor().getLastName() + " | "
						+ doctorSpeciality.get(0).getSpeciality().getSpecialityName();
				ResponseWorkListSearch ResponseWorkListSearch = new ResponseWorkListSearch();
				if ( prescriptionDetail.getEncounter().getPatient() != null) {
					
					Integer age = Util.getAgeByBirthDate(prescriptionDetail.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
					String patientDetail = "";
					String uhid = "";
		
					Util.getAgeByBirthDate(
							prescriptionDetail.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
					patientDetail = prescriptionDetail.getEncounter().getPatient().getPrefix().getPrefixCode() + " "
							+ prescriptionDetail.getEncounter().getPatient().getFirstName() + " "
							+ prescriptionDetail.getEncounter().getPatient().getMiddleName() + " "
							+ prescriptionDetail.getEncounter().getPatient().getLastName() + " | " + age + " | "
							+ prescriptionDetail.getEncounter().getPatient().getGender().getGenderCode();
		
					uhid = prescriptionDetail.getEncounter().getPatient().getUhidNumber();
		
					ResponseWorkListSearch = new ResponseWorkListSearch(prescriptionDetail.getId(),
							prescriptionDetail.getVisitType().getVisitTypeCode(), prescriptionDetail.getPrescriptionDate(),
							prescriptionDetail.getPriority().getPriorityDesc(), prescriptionDetail.getStatus().getStatus(),
							doctorDetail, prescriptionDetail.getEncounter().getCreatedDate(), uhid, patientDetail,
							prescriptionDetail.getPrescriptionNo(), prescriptionDetail.getStatus().getId());
				}
				responseWorkListSearchList.add(ResponseWorkListSearch);
			}
			return responseWorkListSearchList;
		}*/
	
	
	@Override
	public List<ResponseWorkListSearch> worklistSearch(WorkListSearch WorkListSearch) throws CustomException {
	
		List<ResponseWorkListSearch> responseWorkListSearchList = new ArrayList<ResponseWorkListSearch>();
		List<ResponseWorkListSearch> responseWorkListSearchListNew = new ArrayList<ResponseWorkListSearch>();
		WorkListSearch.setToDate((new DateTime(WorkListSearch.getToDate())).plusDays(1).toDate());
		
		List<ApplicationConfiguration> applicationConfigurations = iApplicationConfigurationDao.getAll(WorkListSearch.getUnitId());
		if(applicationConfigurations != null && !applicationConfigurations.isEmpty() && applicationConfigurations.getFirst().getIsDeptPharmacyRequired()) {
			Response resp = iDepartmentStoreMapperDao.getDeptStoremappings(WorkListSearch.getUnitId(), WorkListSearch.getStoreId());
			List<DepartmentStoreMapperDto> deptList = resp.getListObject();
			if(deptList == null || deptList.isEmpty())
				throw new CustomException("No departments are mapped to the store");
				
			List<Integer> deptIds = deptList.stream().map(x -> x.getDeptId()).collect(Collectors.toList());
			responseWorkListSearchList = salesWorkListDao.worklistSearchByFieldsAndDepartment(WorkListSearch, screenId, deptIds,WorkListSearch.getUnitId());
		}
		else
			responseWorkListSearchList = salesWorkListDao.worklistSearchByFieldsBP(WorkListSearch, screenId,WorkListSearch.getUnitId());
		
		if(responseWorkListSearchList != null && !responseWorkListSearchList.isEmpty())
		{
			 List<Integer> doctorIds = responseWorkListSearchList.stream().map(ResponseWorkListSearch::getDoctorId)
					.collect(Collectors.toList());
//			 List<DoctorSpecialityResp> doctorSpecialityRespsAll = IGlobalDao.doctorSearchByIdValueBP(doctorIds);
			
			 for(ResponseWorkListSearch responseWorkListSearch : responseWorkListSearchList)
			 {
//				 if(doctorSpecialityRespsAll != null && !doctorSpecialityRespsAll.isEmpty()){
//					 List<DoctorSpecialityResp> doctorSpeciality = doctorSpecialityRespsAll.stream()
//					 	.filter(x -> x.getDoctorId().equals(responseWorkListSearch.getDoctorId())).collect(Collectors.toList());
					 if(responseWorkListSearch.getSpecialityName()!=null && responseWorkListSearch.getDoctorDetail()!= null)
						 responseWorkListSearch.setDoctorDetail(responseWorkListSearch.getDoctorDetail() + responseWorkListSearch.getSpecialityName());
//				 }
				 
				 int age = Util.getAgeByBirthDate(responseWorkListSearch.getPatientDob().toLocalDateTime().toLocalDate());
				 responseWorkListSearch.setPatientDetail(responseWorkListSearch.getPatientDetail() + " | " + age);
				 String userName =GlobalDao.getUserNameByUserId(responseWorkListSearch.getAddedByUser(), WorkListSearch.getUnitId());
				 responseWorkListSearch.setUserName(userName);
				 responseWorkListSearchListNew.add(responseWorkListSearch);
				 
				 
			 }
			 return responseWorkListSearchListNew;
		 }
		return null;
	}
	

	/*@Override
	public PrescriptionDetailResponse prescriptionDetailsById(Long prescriptionId) throws Exception {
		PrescriptionDetailResponse itemListObject = new PrescriptionDetailResponse();
		List<PrescriptionDetailItemResponse> itemLists = new ArrayList<PrescriptionDetailItemResponse>();
		try {
			Prescription prescription = salesWorkListDao.prescriptionByIdValue(prescriptionId);
//			List<DoctorSpecialityMapper> doctorSpeciality = IGlobalDao
//					.doctorSearchByIdValue(prescription.getDoctor().getDoctorId());
			String doctorDetail = prescription.getDoctor().getFirstName() + " "
					+ prescription.getDoctor().getMiddleName() + " " + prescription.getDoctor().getLastName() + " | "
					+ prescription.getSpecialityMaster().getSpecialityName();

			Sale sale = salesWorkListDao.getSaleValueByPrescriptionId(prescriptionId);

			if (prescription.getEncounter() != null) {
				String patientDetail = "";
				if (prescription.getEncounter().getPatient() != null) {
					Integer age = Util.getAgeByBirthDate(
							prescription.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
					patientDetail = prescription.getEncounter().getPatient().getPrefix().getPrefixCode() + " "
							+ prescription.getEncounter().getPatient().getFirstName() + " "
							+ prescription.getEncounter().getPatient().getMiddleName() + " "
							+ prescription.getEncounter().getPatient().getLastName() + " | " + age + " | "
							+ prescription.getEncounter().getPatient().getGender().getGenderCode();
				}
				itemListObject = new PrescriptionDetailResponse(
						prescription.getEncounter().getPatient().getUhidNumber(), patientDetail, prescription.getId(),
						doctorDetail, PopulateDiscountCategory(prescription.getOrganization().getOrganizationId()),
						prescription.getEncounter().getPatient().getPatientId(), prescription.getDoctor().getDoctorId(),
						prescription.getPrescriptionDate(), prescription.getPrescriptionNo(),
						prescription.getStatus().getId(), prescription.getStatus().getStatus(),
						prescription.getEncounter().getEncounterId(), sale != null ? sale.getId() : null,
						sale != null ? sale.getBillNo() : null, sale != null ? sale.getAddedDate() : null);
			}
			for (PrescriptionDetail itemList : salesWorkListDao.prescriptionDetailsByIdValue(prescriptionId)) {
				List<BatchResponse> medicineBatches = new ArrayList<BatchResponse>();
				List<Batch> batchList = IGlobalDao.getBatchDetailsPharmacy(itemList.getItem().getId());

				List<FrequencyResponse> frequency = new ArrayList<FrequencyResponse>();
				for (PrescriptionFrequencyDetail frequencyDetail : salesWorkListDao
						.prescriptionFreqDetailByPresDetailId(itemList.getId())) {

					String dose = frequencyDetail.getDose() + " " + frequencyDetail.getStrengthUnit().getUnits();
					FrequencyResponse response = new FrequencyResponse(dose,
							frequencyDetail.getFrequency().getDescription(), frequencyDetail.getInstructions());
					frequency.add(response);
				}
				Long batchId = salesWorkListDao.getBatchId(itemList.getItem().getId(),
						sale != null ? sale.getId() : null);

				for (Batch batch : batchList) {
					if (batchId != null && batchId.equals(batch != null ? batch.getId() : null)) {
						medicineBatches.add(Util.PopulateBatchWithStockWithFlag(batch, true));
					} else {
						medicineBatches.add(Util.PopulateBatchWithStock(batch));
					}
				}

				Long quantityDispensed = 0L;

				Long quantity = salesWorkListDao.getDispensedQuantity(itemList.getId());

				Long quantityReturned = salesReturnDao.getReturnedQuantity(itemList.getId());

				quantityDispensed = quantity - quantityReturned;

				
				 * PrescriptionDetailItemResponse item = new
				 * PrescriptionDetailItemResponse(itemList.getId(), itemList.getItem().getId(),
				 * itemList.getItem().getCode(), itemList.getItem().getName(),
				 * itemList.getItem().getGeneric().getId(),
				 * itemList.getItem().getGeneric().getName(),
				 * itemList.getItem().getManufacturer().getId(),
				 * itemList.getItem().getManufacturer().getName(), itemList.getOrderQty(),
				 * medicineBatches,
				 * itemList.getItem().getIsOtc(),itemList.getItem().getSalesTax().getId(),
				 * itemList.getItem().getSalesTax().getName(),itemList.getItem().getSalesTax().
				 * getTaxPercentage(), itemList.getStatus().getId(),
				 * itemList.getStatus().getStatus(), quantityDispensed);
				 
//				for (Batch batch : batchList) {
					
			
					
					PrescriptionDetailItemResponse item = new PrescriptionDetailItemResponse(itemList.getId(),
							itemList.getItem().getId(), itemList.getItem().getCode(), itemList.getItem().getName(),
							itemList.getItem().getGeneric().getId(), itemList.getItem().getGeneric().getName(),
							itemList.getItem().getManufacturer().getId(),
							itemList.getItem().getManufacturer().getName(), itemList.getOrderQty(), medicineBatches,
							frequency, itemList.getItem().getIsOtc(), itemList.getItem().getSalesTax().getId(),
							itemList.getItem().getSalesTax().getName(),
							itemList.getItem().getSalesTax().getTaxPercentage(), itemList.getStatus().getId(),
							itemList.getStatus().getStatus(), quantityDispensed,
							itemList.getInstruction().getInstructionDesc());
//					item.setBatchDetails(batch.getBatchNo() + " | " + batch.getExpiryDate());
					for(SaleDetail saleDetail : salesWorkListDao.getSaleDetail(itemList.getId()))	{
					item.setBatchDetails(saleDetail.getBatch().getBatchNo() + " | " + saleDetail.getBatch().getExpiryDate());
					}
					itemLists.add(item);
//				}
			}
			itemListObject.setItems(itemLists);
		} catch (Exception e) {
			
		}
		return itemListObject;
	}*/
	
	
	
	/*List<ResponseWorkListSearch> responseWorkListSearchList = new ArrayList<ResponseWorkListSearch>();
	WorkListSearch.setToDate((new DateTime(WorkListSearch.getToDate())).plusDays(1).toDate());
	
	for (Prescription prescriptionDetail : salesWorkListDao.worklistSearchByFields(WorkListSearch, screenId)) {
		List<DoctorSpecialityMapper> doctorSpeciality = IGlobalDao
				.doctorSearchByIdValue(prescriptionDetail.getDoctor().getDoctorId());
		String doctorDetail = prescriptionDetail.getDoctor().getFirstName() + " "
				+ prescriptionDetail.getDoctor().getMiddleName() + " "
				+ prescriptionDetail.getDoctor().getLastName() + " | "
				+ doctorSpeciality.get(0).getSpeciality().getSpecialityName();
		ResponseWorkListSearch ResponseWorkListSearch = new ResponseWorkListSearch();
		if ( prescriptionDetail.getEncounter().getPatient() != null) {
			
			Integer age = Util.getAgeByBirthDate(prescriptionDetail.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
			String patientDetail = "";
			String uhid = "";
	
			Util.getAgeByBirthDate(
					prescriptionDetail.getEncounter().getPatient().getDob().toLocalDateTime().toLocalDate());
			patientDetail = prescriptionDetail.getEncounter().getPatient().getPrefix().getPrefixCode() + " "
					+ prescriptionDetail.getEncounter().getPatient().getFirstName() + " "
					+ prescriptionDetail.getEncounter().getPatient().getMiddleName() + " "
					+ prescriptionDetail.getEncounter().getPatient().getLastName() + " | " + age + " | "
					+ prescriptionDetail.getEncounter().getPatient().getGender().getGenderCode();
	
			uhid = prescriptionDetail.getEncounter().getPatient().getUhidNumber();
	
			ResponseWorkListSearch = new ResponseWorkListSearch(prescriptionDetail.getId(),
					prescriptionDetail.getVisitType().getVisitTypeCode(), prescriptionDetail.getPrescriptionDate(),
					prescriptionDetail.getPriority().getPriorityDesc(), prescriptionDetail.getStatus().getStatus(),
					doctorDetail, prescriptionDetail.getEncounter().getCreatedDate(), uhid, patientDetail,
					prescriptionDetail.getPrescriptionNo(), prescriptionDetail.getStatus().getId());
		}
		responseWorkListSearchList.add(ResponseWorkListSearch);
	}
	return responseWorkListSearchList;*/
	
	
	@SuppressWarnings("unlikely-arg-type")
	public PrescriptionDetailResponse prescriptionDetailsByIdBP(Long prescriptionId,Integer unitId, Integer storeId) throws Exception {
		PrescriptionDetailResponse itemListObject = new PrescriptionDetailResponse();
		//List<PrescriptionDetailItemResponse> itemLists = new ArrayList<PrescriptionDetailItemResponse>();
		try 
		{
			List<PrescriptionDetailResponse> itemListObjects = salesWorkListDao.prescriptionByIdValueBP(prescriptionId);
			itemListObject = itemListObjects.getFirst();
			if(itemListObject != null)
			{
//				List<DoctorSpecialityResp> docSpeciality = IGlobalDao.doctorSearchByIdValueBP(itemListObject.getDoctorId());
//				if(docSpeciality != null && !docSpeciality.isEmpty())
					itemListObject.setDoctorDetail(itemListObject.getDoctorDetail()+ " | " + itemListObject.getSpecialityName());
				Integer age1 = Util.getAgeByBirthDate(itemListObject.getPatientDob().toLocalDateTime().toLocalDate());
				itemListObject.setPatientDetail(itemListObject.getPatientDetail() + " | " + age1);
				itemListObject.setDiscountCategory(PopulateDiscountCategory(itemListObject.getOrganizationId()));
                List<PrescriptionDetailItemResponse> itemListAll =  new ArrayList<>();
                
                List<PrescriptionDetailItemResponse> savedListAllTemp =  new ArrayList<>();
                
                List<PrescriptionDetailItemResponse> originalPrescListAll =  new ArrayList<>();
                AdmissionsForBatchProcessDto batchProcessDto = iBillingDao.getBillByEncounterId(itemListObject.getEncounterId());
                if(batchProcessDto!=null)
                {
                	itemListObject.setBillId(batchProcessDto.getBillId());
                	itemListObject.setBillNumber(batchProcessDto.getBillNumber());
                }
                
                savedListAllTemp = salesWorkListDao.getissuedItemList(prescriptionId);
                List<PrescriptionDetailItemResponse> savedListAll =  new ArrayList<>();
                for (PrescriptionDetailItemResponse prescriptionDetailItemResponse : savedListAllTemp) {
					
                	if(savedListAll.size() == 0) {
                		savedListAll.add(prescriptionDetailItemResponse);
                	}else {
                		Boolean checkExits = false;
                		 for (PrescriptionDetailItemResponse testDto : savedListAll) {
         					
                         	if(testDto.getIssueDetailId().equals(prescriptionDetailItemResponse.getIssueDetailId())) {
                         		checkExits = true;
                         	}
                         	
         				}
                		
                		 if(checkExits == false) {
                				savedListAll.add(prescriptionDetailItemResponse);
                		 }
                	}
                	
				}
                
              
                
                
                
                originalPrescListAll = salesWorkListDao.prescriptionDetailsByIdValueBP(prescriptionId);
                
                int saveFlag = 0;
                if((savedListAll!=null && savedListAll.size()>0) || (itemListObject.getStatusId() == 18 || itemListObject.getStatusId() == 4) ) {
                	saveFlag = 1;
                	for (PrescriptionDetailItemResponse originalPrec : originalPrescListAll) {
                		int counter = 0;
						for (PrescriptionDetailItemResponse savedPrec : savedListAll) {
							if(savedPrec.getPricId().longValue() == originalPrec.getPrescriptionDetailId() && savedPrec.getStatusId() != 9) {
								counter = 1;
							}
						}
						if(counter == 0 && originalPrec.getParentSubstituteId()==null ) {
							originalPrec.setPricId(BigInteger.valueOf(originalPrec.getPrescriptionDetailId()));
							
							savedListAll.add(originalPrec);
							
						}
					}
                	itemListAll.addAll(savedListAll);
                	
                }else {
                	List<PrescriptionDetailItemResponse> originalPrescListAllNew =  new ArrayList<>();
                	for (PrescriptionDetailItemResponse subItemsRemove : originalPrescListAll) {
                    if(itemListObject.getStatusId()==1 && subItemsRemove.getParentSubstituteId()==null && subItemsRemove.getIsActive()==true )
                    {
                    	originalPrescListAllNew.add(subItemsRemove);
                    }
                    if(itemListObject.getStatusId()==1 && subItemsRemove.getParentSubstituteId()!=null && subItemsRemove.getParentSubstituteId()>0)
                    {
                    	salesWorkListDao.inActiveSaleDetailForSubItems(subItemsRemove.getSaleDetailId());
                    }
                	}
                	if(originalPrescListAllNew.size()>0)
                	{
                		itemListAll = originalPrescListAllNew;
                	}
                	else
                	{
                		List<PrescriptionDetailItemResponse> itemList = originalPrescListAll.stream().filter(p->p.getIsActive()==true).collect(Collectors.toList());
                		itemListAll = itemList;
                	}
                }
          
				if(itemListAll != null && !itemListAll.isEmpty())
				{
					List<Integer> itemIds = itemListAll.stream().map(PrescriptionDetailItemResponse::getItemId).collect(Collectors.toList());
					
					//List<Long> detailIds = itemListAll.stream().map(x -> x.getId()).collect(Collectors.toList());
					
					
					List<Long> detailIds = new ArrayList<>();// itemListAll.stream().map(x -> x.getId()).collect(Collectors.toList());
					
					  if(savedListAll!=null && savedListAll.size()>0) {
						  for(PrescriptionDetailItemResponse itemDto : itemListAll)
							{
								Long id = 0L;
								
								if(id.equals(itemDto.getPricId().longValue())) {
									
								}else {
									detailIds.add(itemDto.getPricId().longValue());
								}
							}
					  }else {
						  detailIds = itemListAll.stream().map(x -> x.getId()).collect(Collectors.toList());
					  }
					
					
					
					
					List<BatchResponse> medicineBatchesAll = IGlobalDao.getBatchDetailsPharmacyBP(itemIds,unitId,storeId);
					List<UomResponse> uomsAll = new ArrayList<UomResponse>();
					if(!medicineBatchesAll.isEmpty())
						uomsAll = IGlobalDao.getBatchUomListPharmacyBP(medicineBatchesAll.stream().map(BatchResponse::getBatchId).collect(Collectors.toList()));
					List<SalesWorkQty> batchIdsAll = new ArrayList<SalesWorkQty>();
					batchIdsAll = salesWorkListDao.getBatchIdBP(itemIds,itemListObject.getSaleId());
					List<SalesWorkQty> quantityAll = salesWorkListDao.getDispensedQuantityBP(detailIds,unitId);
					List<SalesWorkQty> quantityReturnedAll = salesReturnDao.getReturnedQuantityBP(detailIds,unitId);
					List<FrequencyResponse> frequencyAll = salesWorkListDao.prescriptionFreqDetailByPresDetailIdBP(detailIds);
					List<SaleDetailResp> saleDetailsAll = salesWorkListDao.getSaleDetailBP(detailIds);
					
					List<PatientSponsorDetails> patientSponsorDetails = null;
					patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(itemListObject.getEncounterId());
					
					for(PrescriptionDetailItemResponse itemList : itemListAll)
					{
						List<SaleDetailResp> saleDetails = new ArrayList<>();
						
						List<SalesWorkQty> quantitySpecific = new ArrayList<>();
						 
						List<SalesWorkQty> quantityReturnedSpecific = new ArrayList<>();
						
						 if(savedListAll!=null && savedListAll.size()>0) {
							 itemList.setPackageConsumptionAmt(itemList.getPackageConsumptionAmt());
							List<FrequencyResponse> frq = new ArrayList<>();
							
								 for(FrequencyResponse itemDto : frequencyAll) {
									 
									 Long id = itemList.getPricId().longValue();
										
										if(id.equals(itemDto.getDetailId())) {
											frq.add(itemDto);
										}
								 }
								 itemList.setFrequency(frq);
								 
								 for(SalesWorkQty qtyretr : quantityReturnedAll) {
									 
									 Long id = itemList.getPricId().longValue();
										
										if(id.equals(qtyretr.getDetailId())) {
											quantityReturnedSpecific.add(qtyretr);
										}
								 }
								 
								 for(SalesWorkQty qty : quantityAll) {
									 
									 Long id = itemList.getPricId().longValue();
										
										if(id.equals(qty.getDetailId())) {
											quantitySpecific.add(qty);
										}
								 }
								 
								 for(SaleDetailResp qty : saleDetailsAll) {
									 
									 Long id = itemList.getPricId().longValue();
										
										if(id.equals(qty.getDetailId())) {
											saleDetails.add(qty);
										}
								 }
								 
							}else {
							  itemList.setFrequency(frequencyAll.stream()
										.filter(x -> x.getDetailId().equals(itemList.getId())).collect(Collectors.toList()));
							  
							  
							   quantitySpecific = quantityAll.stream().filter(x -> x.getDetailId().equals(itemList.getId()))
										.collect(Collectors.toList());
							   quantityReturnedSpecific = quantityReturnedAll.stream().filter(x -> x.getDetailId().equals(itemList.getId()))
										.collect(Collectors.toList());
								
								 saleDetails = saleDetailsAll.stream()
										.filter(x -> x.getDetailId().equals(itemList.getId())).collect(Collectors.toList());
						  }
						
						/*itemList.setFrequency(frequencyAll.stream()
								.filter(x -> x.getDetailId().equals(itemList.getId())).collect(Collectors.toList()));*/
						
						List<SalesWorkQty> batchIdsSpecific = batchIdsAll.stream().filter(a -> a.getItemId().equals(itemList.getItemId()))
								.collect(Collectors.toList());
						List<BatchResponse> batchResp = medicineBatchesAll.stream()
								.filter(x -> x.getItemId().equals(itemList.getItemId())).collect(Collectors.toList());

						// Get the batch list by item id 
						List<BatchResponse> batchList = GlobalDao
								.getBatchListByItemId(itemList.getItemId().longValue(),storeId,unitId);


						//batchResp = batchList.stream()
						//		.filter(x -> x.getItemId().equals(itemList.getItemId())).collect(Collectors.toList());
						List<BatchResponse> finalBatchList = new ArrayList<BatchResponse>();
						for(BatchResponse batch : batchList)
						{
							
									if(batch.getAvailableStockQuantity()>0);
									List<UomResponse> uomsSpecific = uomsAll.stream().filter(x -> x.getBatchId().equals(batch.getBatchId())).collect(Collectors.toList());
									batch.setUom(uomsSpecific);
									//if(batchIdsSpecific != null && !batchIdsSpecific.isEmpty() && batchIdsSpecific.get(0).getQty().equals(batch.getBatchId()))
									if(itemList.getBatchId() != null && batch.getBatchId().equals(itemList.getBatchId().longValue()))
									{
										batch.setIsSelected(true);
		                                batch.setSelectedUomTypeId(itemList.getUomTypeId());
		                                batch.setSelectedUnitId(itemList.getUomUnitId());

									}
									else
									{
										batch.setIsSelected(false);
										UomResponse opUom = Util.getOpDispenseUom(uomsSpecific);
										if(opUom!=null)
										batch.setOpUomId(opUom.getUomUnitId());
									}
									batch.setExpiry(batch.getExpiryDate());
									batch.setItemId(itemList.getItemId());
									batch.setTaxId(batch.getTaxId());
									batch.setTaxPercentage(batch.getTaxPercentage());
									finalBatchList.add(batch);
									
						}
						
						
						Long quantityDispensed = 0L;
						quantityDispensed = (quantitySpecific != null && !quantitySpecific.isEmpty() ? quantitySpecific.getFirst().getQty() : 0L)
								- (quantityReturnedSpecific != null && !quantityReturnedSpecific.isEmpty() ? quantityReturnedSpecific.getFirst().getQty() : 0L);
					
						
						
						if(saleDetails != null && !saleDetails.isEmpty())
							itemList.setBatchDetails(saleDetails.getFirst().getBatchDetails());
						
						itemList.setBatch(finalBatchList);


//			commented by Ganesh			if(itemListObject.getStatusId()==1)
//						{
//							itemList.setStatusId(1);
//							itemList.setStatus("New");
//						}
						
						itemList.setQuantityDispensed(quantityDispensed);
						
						//for substitute
						
					if(saveFlag == 1) {
							List<PrescriptionDetailItemResponse> substututeList = new ArrayList<>();
							Long priscriptionId = itemList.getPrescriptionId() !=null ? itemList.getPrescriptionId() : itemList.getTempPrescriptionId().longValue();
							List<SubstituteItemDto> substitute=GlobalDao.getListOfSubstitutesForOpSales(priscriptionId);
							
								for (SubstituteItemDto substituteList : substitute) {
									
								if(itemList.getId()==substituteList.getParentSubstituteId().longValue())  {
									boolean getSubItemAtCOPFlag=GlobalDao.getSubItemAtCOPFlag(substituteList.getPrescriptionDetailid(),unitId);
									PrescriptionDetailItemResponse itm=new PrescriptionDetailItemResponse();
									itm.setPrescriptionDetailId(substituteList.getPrescriptionDetailid().longValue());
									itm.setGenericId(substituteList.getGenericId());
									itm.setGenericName(substituteList.getGenericName());
									itm.setItemId(substituteList.getItemId());
									itm.setItemName(substituteList.getItemName());
									itm.setItemCode(substituteList.getItemCode());
									itm.setManufacturerName(substituteList.getManufacturer());
									itm.setManufacturerId(substituteList.getManufactureId());
									itm.setStorageUnitId(substituteList.getStorageUnitId());
									itm.setStorageUnitName(substituteList.getStorageUnitName());
									itm.setUomTypeId(substituteList.getUomTypeId());
									itm.setUomUnitId(substituteList.getUomUnitId());
									itm.setStatus("Completed");
									itm.setStatusId(4);
									itm.setTax(substituteList.getTax());
									itm.setTaxId(substituteList.getTaxId());
									itm.setNewDispQty(substituteList.getNewDispQty().longValue());
									itm.setQtyPrescribed(substituteList.getQtyPrescribed());
									itm.setProductCategoryId(substituteList.getProductCategoryId()!=null? substituteList.getProductCategoryId():null);
									itm.setManufacturerName(substituteList.getManufacturerName());
									itm.setRate(substituteList.getRate()!=null? substituteList.getRate():null);
									itm.setTotalAmount(substituteList.getTotalAmount()!=null?substituteList.getTotalAmount():null);
									itm.setNetAmount(substituteList.getNetAmount()!=null?substituteList.getNetAmount():null);
									itm.setSelfPayable(substituteList.getSelfPayable()!=null?substituteList.getSelfPayable():null);
									itm.setTaxAmount(substituteList.getTaxAmount()!=null?substituteList.getTaxAmount():null);
									itm.setCreditPayable(substituteList.getCreditPayable()!=null?substituteList.getCreditPayable():null);
									itm.setDiscountAmount(substituteList.getDiscountAmount()!=null?substituteList.getDiscountAmount():null);
									itm.setTaxPercentage(substituteList.getTaxPercentage()!=null?substituteList.getTaxPercentage():null);
									itm.setPatientClassAmount(substituteList.getConsessionAmount());
									itm.setPatientClassPercentage(substituteList.getConsessionPercentage());
									itm.setPackageConsumptionAmt(substituteList.getPackageConsumptionAmt());
									itm.setAtCop(getSubItemAtCOPFlag);
									itm.setIsThreeUOMApplicable(substituteList.getIsThreeUOMApplicable());
									itm.setIsLasa(substituteList.getIsLasa() != null ? substituteList.getIsLasa() : false);
									//itm.setFrequencyDesc(substituteList.getFrequencyDesc());
									//itm.setStartDate(substituteList.getStartDate());
									//itm.setStopDate(substituteList.getStopDate());
									List<Integer> itemIdsSub = substitute.stream().map(SubstituteItemDto::getItemId).collect(Collectors.toList());
									List<BatchResponse> medicineBatchesAllSub = IGlobalDao.getBatchDetailsPharmacyBP(itemIdsSub,unitId,storeId);
									
									List<SalesWorkQty> batchIdsAllSub = new ArrayList<SalesWorkQty>();
									batchIdsAllSub = salesWorkListDao.getBatchIdBP(itemIdsSub,itemListObject.getSaleId());
									
									List<SalesWorkQty> batchIdsSpecificSub = batchIdsAllSub.stream().filter(a -> a.getItemId().equals(substituteList.getItemId()))
											.collect(Collectors.toList());
									List<Long> detailIdsSub = substitute.stream().map(x -> x.getPrescriptionDetailid().longValue()).collect(Collectors.toList());
									List<UomResponse> uomsAllSub = new ArrayList<UomResponse>();
									if(!medicineBatchesAllSub.isEmpty())
										uomsAllSub = IGlobalDao.getBatchUomListPharmacyBP(medicineBatchesAllSub.stream().
												map(BatchResponse::getBatchId).collect(Collectors.toList()));
									
									List<BatchResponse> batchRespSub = medicineBatchesAllSub.stream()
											.filter(x -> x.getItemId().equals(substituteList.getItemId())).collect(Collectors.toList());
									
									// Get the batch list by item id 
									List<BatchResponse> subBatchList = GlobalDao
											.getBatchListByItemId(substituteList.getItemId().longValue(),storeId,unitId);
									List<BatchResponse> finalSubBatchList = new ArrayList<BatchResponse>();
									for(BatchResponse batchSub : subBatchList)
									{
										if(batchSub.getAvailableStockQuantity()>0);
										List<UomResponse> uomsSpecific = uomsAllSub.stream().filter(x -> x.getBatchId().equals(batchSub.getBatchId())).collect(Collectors.toList());
										batchSub.setUom(uomsSpecific);
											//if(batchIdsSpecific != null && !batchIdsSpecific.isEmpty() && batchIdsSpecific.get(0).getQty().equals(batch.getBatchId()))
										if(substituteList.getBatchID() != null && batchSub.getBatchId().equals(substituteList.getBatchID().longValue()))
										{
											batchSub.setIsSelected(true);
											batchSub.setSelectedUomTypeId(substituteList.getUomTypeId());
											batchSub.setSelectedUnitId(substituteList.getUomUnitId());
										}
										else
										{
											batchSub.setIsSelected(false);
											UomResponse opUom = Util.getOpDispenseUom(uomsSpecific);
											if(opUom!=null)
												batchSub.setOpUomId(opUom.getUomUnitId());
										}
										batchSub.setExpiry(batchSub.getExpiryDate());
										batchSub.setItemId(substituteList.getItemId());
										batchSub.setTaxId(batchSub.getTaxId());
										batchSub.setTaxPercentage(batchSub.getTaxPercentage());
										finalSubBatchList.add(batchSub);
									}
									List<SaleDetailResp> saleDetailsAllSub = salesWorkListDao.getSaleDetailBP(detailIdsSub);
									List<SaleDetailResp> saleDetailsSub = saleDetailsAllSub.stream()
											.filter(x -> x.getDetailId().equals(substituteList.getPrescriptionDetailid().longValue())).collect(Collectors.toList());
									if(saleDetailsSub != null && !saleDetailsSub.isEmpty())
										itm.setBatchDetails(saleDetailsSub.getFirst().getBatchDetails());
									itm.setBatch(finalSubBatchList);
									
									boolean checkExits = false;
									for(PrescriptionDetailItemResponse itemserach : itemListAll)
									{
										if(itemserach.getSelectedItems() !=null) {
											
											for (PrescriptionDetailItemResponse saleDetailResp : itemserach.getSelectedItems()) {
												
												if(itm.getPrescriptionDetailId().equals(saleDetailResp.getPrescriptionDetailId())) {
													checkExits = true;
												}
												
											}
										}
										
									}
									
									if(checkExits == false) {
										substututeList.add(itm);
									}
									
									
									/*if(withOutsubstututeList.size()>0) {
										int count = 0;
										for (PharmacologyItem dtoSelected : withOutsubstututeList) {
												
											if(dtoSelected.getId().equals(itm.getId())) {
												count = 1;	
											}
										}
										if(count == 0) {
											withOutsubstututeList.add(itm);
										}
									}else {
										withOutsubstututeList.add(itm);
									}*/
									
								
									}
								}
								
								itemList.setSelectedItems(substututeList);
						}
						
						if(patientSponsorDetails != null && itemList.getGenProdCatId() != null) 
						{
							for(PatientSponsorDetails sponsorDetail : patientSponsorDetails) 
							{
								List<Integer> prodCatIds = new ArrayList<Integer>();
								prodCatIds.add(itemList.getGenProdCatId());
								
								List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds, sponsorDetail.getPaymentEntitlementId());
								if(pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
									if(pharmacyTariffs.size() > 1){
										//throw exception?
										//Don't throw - get first object and set data.
									}
									
									PharmacyTariff pharmacyTariff = pharmacyTariffs.getFirst();
									itemList.setMarkUpDown(pharmacyTariff.getMarkUpDown());
									itemList.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
									break;
								}
							}
							
						}
						
						
						PatientConsession patientConsession = IGlobalDao.getPatientConsessionEncounter(itemList.getItemId(), itemListObject.getEncounterId(), 
								itemListObject.getPatientCategoryId(), itemList.getAssetCategoryId(),
								itemList.getProductCatId() != null ? itemList.getProductCatId() : itemList.getGenProdCatId(),null,null);
						
						if(patientConsession!=null && patientConsession.isMarkupStatus()==false) 
						{
							itemList.setPatientClassPercentage(patientConsession.getPatientClassPercentage());
							if(patientConsession.getPatientClassPercentage()==0.00) {
								itemList.setPatientClassAmount(0.00);
							}
							itemList.setMarkupStatus(patientConsession.isMarkupStatus());
						}
						else {
							itemList.setPatientClassPercentage(0.00);
							itemList.setPatientClassAmount(0.00);
							itemList.setMarkupStatus(patientConsession.isMarkupStatus());
						}
						itemList.setPriorityId(patientConsession.getPriorityId());
						itemList.setIsNarcotic(itemList.getIsNarcotic());
					}
					for (PrescriptionDetailItemResponse itemListAll1 : itemListAll) {
						itemListAll1.setColorCode(GlobalDao.getColorCode(itemListAll1.getGenericId(),itemListObject.getOrganizationId(),unitId));
					}
					itemListObject.setItems(itemListAll);
					itemListObject.setRemark(itemListObject.getRemark());
					itemListObject.setCop(itemListObject.getCop());

				}
			}
		} catch (Exception e) {
			
			throw e;
		}
		//return itemListObject;
		return itemListObject;
	}

	/*@Override
	public PrescriptionDetailResponse getSubstitute(Integer id, Integer itemId) throws Exception {
		PrescriptionDetailResponse itemListObject = new PrescriptionDetailResponse();
		List<PrescriptionDetailItemResponse> itemLists = new ArrayList<PrescriptionDetailItemResponse>();
		for (Item itemList : salesWorkListDao.getSubstituteById(id, itemId)) {
			List<BatchResponse> medicineBatches = new ArrayList<BatchResponse>();
			List<Batch> batchList = IGlobalDao.getBatchDetailsPharmacy(itemList.getId());
	
			for (Batch batch : batchList) {
				medicineBatches.add(Util.PopulateBatchWithStock(batch));
			}
	
			PrescriptionDetailItemResponse item = new PrescriptionDetailItemResponse(itemList.getId(),
					itemList.getCode(), itemList.getName(), itemList.getGeneric().getId(),
					itemList.getGeneric().getName(), itemList.getManufacturer().getId(),
					itemList.getManufacturer().getName(), medicineBatches, itemList.getIsOtc(),
					itemList.getSalesTax().getId(), itemList.getSalesTax().getName(),
					itemList.getSalesTax().getTaxPercentage());
			itemLists.add(item);
			itemListObject.setItems(itemLists);
		}
		return itemListObject;
	}*/
	
	
	@Override
	public PrescriptionDetailResponse getSubstitute(Integer id, Integer itemId) throws Exception {
		PrescriptionDetailResponse itemListObject = new PrescriptionDetailResponse();
		List<PrescriptionDetailItemResponse> itemLists = new ArrayList<PrescriptionDetailItemResponse>();
		
		List<PrescriptionDetailItemResponse> items = salesWorkListDao.getSubstituteByIdBP(id, itemId);
		if(!items.isEmpty())
		{
			List<Integer> itemIds = items.stream().map(PrescriptionDetailItemResponse::getItemId).collect(Collectors.toList());
			List<BatchResponse> medicineBatchesAll = IGlobalDao.getBatchDetailsPharmacyBP(itemIds,null,null);
			List<UomResponse> uomsAll = new ArrayList<UomResponse>();
			if(!medicineBatchesAll.isEmpty())
				uomsAll = IGlobalDao.getBatchUomListPharmacyBP(medicineBatchesAll.stream().map(BatchResponse::getBatchId).collect(Collectors.toList()));
			
			for(PrescriptionDetailItemResponse itemList : items){
				List<BatchResponse> medicineBatches = medicineBatchesAll.stream()
						.filter(x -> x.getItemId().equals(itemList.getItemId())).collect(Collectors.toList());
				for(BatchResponse batch : medicineBatches){
					batch.setUom(uomsAll.stream()
							.filter(x -> x.getBatchId().equals(batch.getBatchId())).collect(Collectors.toList()));
				}
				itemList.setBatch(medicineBatches);
			}
		}
		itemListObject.setItems(items);
		return itemListObject;
	}

	private List<DiscountCategoryResponse> PopulateDiscountCategory(Integer orgId) 
	{
		List<DiscountCategoryResponse> discountCategoryList = new ArrayList<DiscountCategoryResponse>();
		for (DiscountCategory discountCategory : IGlobalDao.getDiscountCategoryList(orgId,0)) 
		{
			DiscountCategoryResponse discountCategoryObject = new DiscountCategoryResponse(discountCategory.getId(),discountCategory.getCategory());
			discountCategoryList.add(discountCategoryObject);
		}
		return discountCategoryList;
	}

	@Override
	public PrescriptionDetailResponse getSubstitute(Integer id, Integer itemId, Integer encounterId) {
		PrescriptionDetailResponse itemListObject = new PrescriptionDetailResponse();
		List<PrescriptionDetailItemResponse> itemLists = new ArrayList<PrescriptionDetailItemResponse>();
		
		List<PrescriptionDetailItemResponse> items = salesWorkListDao.getSubstituteByIdBP(id, itemId);
		if(!items.isEmpty())
		{
			List<Integer> itemIds = items.stream().map(PrescriptionDetailItemResponse::getItemId).collect(Collectors.toList());
			List<BatchResponse> medicineBatchesAll = IGlobalDao.getBatchDetailsPharmacyBP(itemIds,null,null);
			List<UomResponse> uomsAll = new ArrayList<UomResponse>();
			if(!medicineBatchesAll.isEmpty())
				uomsAll = IGlobalDao.getBatchUomListPharmacyBP(medicineBatchesAll.stream().map(BatchResponse::getBatchId).collect(Collectors.toList()));
			
			List<PatientSponsorDetails> patientSponsorDetails = null;
			patientSponsorDetails = iPatientSponsorDetailsDao.findByEncounterId(encounterId);
			
			for(PrescriptionDetailItemResponse itemList : items){
				List<BatchResponse> medicineBatches = medicineBatchesAll.stream()
						.filter(x -> x.getItemId().equals(itemList.getItemId())).collect(Collectors.toList());
				for(BatchResponse batch : medicineBatches){
					batch.setUom(uomsAll.stream()
							.filter(x -> x.getBatchId().equals(batch.getBatchId())).collect(Collectors.toList()));
				}
				
				if(patientSponsorDetails != null && itemList.getGenProdCatId() != null) 
				{
					for(PatientSponsorDetails sponsorDetail : patientSponsorDetails) {
						List<Integer> prodCatIds = new ArrayList<Integer>();
						prodCatIds.add(itemList.getGenProdCatId());
						
						List<PharmacyTariff> pharmacyTariffs = GlobalDao.getTariff(prodCatIds, sponsorDetail.getPaymentEntitlementId());
						if(pharmacyTariffs != null && !pharmacyTariffs.isEmpty()) {
							if(pharmacyTariffs.size() > 1){
								//throw exception?
								//Don't throw - get first object and set data.
							}
							
							PharmacyTariff pharmacyTariff = pharmacyTariffs.getFirst();
							itemList.setMarkUpDown(pharmacyTariff.getMarkUpDown());
							itemList.setMarkUpDownPer(pharmacyTariff.getMarkUpDownPer());
							break;
						}
					}
					
				}
				
				itemList.setBatch(medicineBatches);
			}
		}
		itemListObject.setItems(items);
		return itemListObject;
	}

	@Override
	public void cancelSalesWorklist(CancelRequest cancelRequest) throws CustomException {
		
		Prescription prescription = salesWorkListDao.getSalesWorkList(cancelRequest.getId());
		
		if(prescription == null)
			throw new CustomException("Prescription not found in DB");
		
		/*if((!prescription.getStatus().getId().equals(com.param.common.constants.Status.Pending.getStatus())))
			throw new CustomException("Can not CANCEL. Status is : "+prescription.getStatus().getStatus());*/
		
		//prescription.setStatus(new Status(cancelRequest.getStatusId()));
		prescription.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
		prescription.setCancelNote(cancelRequest.getCancelNote());
		if(prescription.getPrescriptionDetailsList().size()>0) {
			for (PrescriptionDetail preDtls : prescription.getPrescriptionDetailsList()) {
				preDtls.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
			}
		}
		
		salesWorkListDao.updateSalesWorkList(prescription);
		
		if(cancelRequest.getIsFromAssignpicker()!=null && cancelRequest.getIsFromAssignpicker()) {
			AssignPicker assignPicker = assignPickerDao.getAssignPickerList(cancelRequest.getId());
			
			assignPicker.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
			//assignPicker.setCancelNote(cancelRequest.getCancelNote());
			if(assignPicker.getAssignPickerDetails().size()>0) {
				for (AssignPickerDetail dtls : assignPicker.getAssignPickerDetails()) {
					dtls.setStatus(new Status(com.param.common.constants.Status.Cancelled.getStatus()));
				}
			}
			assignPickerDao.updateassignPicker(assignPicker);
		}
	}
}
