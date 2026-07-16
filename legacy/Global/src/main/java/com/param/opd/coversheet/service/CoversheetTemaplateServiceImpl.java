package com.param.opd.coversheet.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.entity.model.master.FreeTextOrderedServices;
import com.param.entity.model.master.Tax;
import com.param.global.billing.service.IContractConsumptionService;
import com.param.global.common.DateConverter;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IOrderDetailsMasterDao;
import com.param.global.dao.IOrderMasterDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.ContractConsumptionDto;
import com.param.global.dto.CoversheetTemplateMasterDto;
import com.param.global.dto.FreeTextOrderedServicesDto;
import com.param.global.dto.InvestigationOrderMasterNewDto;
import com.param.global.dto.OrderDetailsMasterDto;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.dto.OrderDetailsPayeeMapperDto;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.dto.items.BatchResponse;
import com.param.global.opd.dao.IEncounterMasterDao;
import com.param.global.service.IMPackageMasterService;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.global.service.IOrderMasterService;
import com.param.global.unit.dto.AllergyPatientMapperDto;
import com.param.global.unit.dto.CoversheetAccessMasterDto;
import com.param.opd.common.CommonDateUtils;
import com.param.opd.coversheet.dao.IAllergyPatientMapperDao;
import com.param.opd.coversheet.dao.IAppointmentVitalMapperDao;
import com.param.opd.coversheet.dao.IComplaintAppointmentMapperDao;
import com.param.opd.coversheet.dao.IConsultDoctorDao;
import com.param.opd.coversheet.dao.ICoversheetInstructionDao;
import com.param.opd.coversheet.dao.ICoversheetTemaplateDao;
import com.param.opd.coversheet.dao.IMedicationDAO;
import com.param.opd.coversheet.dao.IMedicationHistoryDao;
import com.param.opd.coversheet.dao.IObservationDao;
import com.param.opd.coversheet.dao.IPatientDiagnosisDao;
import com.param.opd.coversheet.dao.IPatientFamilyHistoryDao;
import com.param.opd.coversheet.dao.IPatientHabitDetailsMapperDao;
import com.param.opd.coversheet.dao.IPatientMedicalHistoryDao;
import com.param.opd.coversheet.dao.IPatientPersonalHistoryDetailsDao;
import com.param.opd.coversheet.dao.IPatientSurgicalHistoryDao;
import com.param.opd.coversheet.dao.IPatientTravelHistoryDetailsDao;
import com.param.opd.coversheet.dao.IPatientTreatmentHistoryDetailsDao;
import com.param.opd.coversheet.dao.IQuickNotesMapperDao;
import com.param.opd.coversheet.dto.AppointmentVitalMapperDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentDetailsDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentDto;
import com.param.opd.coversheet.dto.ComplaintAppointmentMapperDto;
import com.param.opd.coversheet.dto.ConsultDoctorDto;
import com.param.opd.coversheet.dto.CoversheetInstructionDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentDetailsDto;
import com.param.opd.coversheet.dto.DiagnosisPatientAppointmentMapperDto;
import com.param.opd.coversheet.dto.MedicationDTO;
import com.param.opd.coversheet.dto.MedicationHistoryDto;
import com.param.opd.coversheet.dto.ObservationDto;
import com.param.opd.coversheet.dto.PatientFamilyHistoryDto;
import com.param.opd.coversheet.dto.PatientHabitDetailsDto;
import com.param.opd.coversheet.dto.PatientMedicalHistoryDto;
import com.param.opd.coversheet.dto.PatientPersonalHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientSurgicalHistoryDto;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.dto.PatientTreatmentHistoryDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDetailsDto;
import com.param.opd.coversheet.dto.PrescriptionDto;
import com.param.opd.coversheet.dto.QuickNotesMapperDto;
import com.param.opd.coversheet.model.AppointmentVitalMapper;
import com.param.opd.coversheet.model.ComplaintAppointmentMapper;
import com.param.opd.model.QuickNotesMapper;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"unchecked"})
@Service
public class CoversheetTemaplateServiceImpl implements ICoversheetTemaplateService, ICommonConstants, IBillingCommonConstants {
	final static Logger logger = Logger.getLogger(CoversheetTemaplateServiceImpl.class);

	@Autowired
	private ICoversheetTemaplateDao iCoversheetTemaplateDao;
	
	@Autowired
	private IComplaintAppointmentMapperDao iComplaintAppointmentMapperDao;
	
	@Autowired
	private IPatientMedicalHistoryDao iPatientMedicalHistoryDao;
	
	@Autowired
	private IPatientSurgicalHistoryDao iPatientSurgicalHistoryDao;
	
	@Autowired
	private IPatientHabitDetailsMapperDao iPatientHabitDetailsMapperDao;
	
	@Autowired
	private IAllergyPatientMapperDao iAllergyPatientMapperDao;
	
	@Autowired
	private IMedicationHistoryDao iMedicationHistoryDao;
	
	@Autowired
	private IPatientTreatmentHistoryDetailsDao iPatientTreatmentHistoryDetailsDao;
	
	@Autowired
	private IPatientFamilyHistoryDao iPatientFamilyHistoryDao;
	
	@Autowired
	private IPatientPersonalHistoryDetailsDao iPatientPersonalHistoryDetailsDao;
	
	@Autowired
	private IPatientTravelHistoryDetailsDao iPatientTravelHistoryDetailsDao;
	
	@Autowired
	private IAppointmentVitalMapperDao iAppointmentVitalMapperDao;
	
	@Autowired
	IPatientDiagnosisDao iPatientDiagnosisDao;
	
	@Autowired
	private IMedicationDAO iMedicationDAO;
	
	@Autowired
	private IConsultDoctorDao iConsultDoctorDao;
	
	@Autowired
	 IObservationDao iObservationDao;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	private IOrderMasterService iOrderMasterService;
	
	@Autowired
	private IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;
	
	@Autowired
	IEncounterMasterDao iEncounterMasterDao;
	
	@Autowired
	IMPackageMasterService iMPackageMasterService;
	
	@Autowired
	IOrderMasterDao iOrderMasterDao;
	
	@Autowired
	IOrderDetailsMasterDao iOrderDetailsMasterDao;
	
	@Autowired
	ICoversheetInstructionDao iCoversheetInstructionDao;
	
	@Autowired
	IOrderDetailsMasterService iOrderDetailsMasterService; 
	
	@Autowired
	IPatientDiagnosisService iPatientDiagnosisService;
	
	@Autowired
	private IComplaintAppointmentMapperService iComplaintAppointmentMapperService;
	
	 @Autowired
	 IQuickNotesMapperDao iQuickNotesMapperDao;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired 
	IContractConsumptionService iContractConsumptionService;

	private MedicationDTO prescriptionDetailsDto;
	
	@Override
	@Transactional 
	public Response saveCoversheetAsTemaplate(CoversheetTemplateMasterDto coversheetTemplateMasterDto) throws ApplicationException{
		try {
			Response res=null;
			if(coversheetTemplateMasterDto.getCovTemplateId() != null) {
				Response resList= iCoversheetTemaplateDao.checkAvailableCoversheetAsTemaplate(coversheetTemplateMasterDto);
				if (resList.getListObject() != null && resList.getListObject().size() > 0) {

					return new Response<>(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
				}
			}			
			 else {
				 res = iCoversheetTemaplateDao.saveCoversheetAsTemaplate(coversheetTemplateMasterDto);
			}
			
			if(res.getObject()!=null) {
				CoversheetTemplateMasterDto resDto=(CoversheetTemplateMasterDto) res.getObject();
				
				//1 For complaints
				ComplaintAppointmentMapperDto complaintAppointmentMapperDto=new ComplaintAppointmentMapperDto();
				complaintAppointmentMapperDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				complaintAppointmentMapperDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				complaintAppointmentMapperDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
				Response complaintListRes = iComplaintAppointmentMapperDao.getListOfComplaintAppointmentMapper(complaintAppointmentMapperDto);
				if(complaintListRes.getListObject() != null && complaintListRes.getListObject().size() > 0){
					List<ComplaintAppointmentMapperDto> list=complaintListRes.getListObject();
					iComplaintAppointmentMapperDao.savefComplaintAppointmentMapper(list, resDto);
				}
				
				//2 for Surgery
				PatientSurgicalHistoryDto patientSurgicalHistoryDto=new PatientSurgicalHistoryDto();
				patientSurgicalHistoryDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientSurgicalHistoryDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientSurgicalHistoryDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientSurgicalHistoryDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response surgicalHistoryCurrentList = iPatientSurgicalHistoryDao.getCurrentPatientSurgicalHistoryForCoversheet(patientSurgicalHistoryDto);
				if(surgicalHistoryCurrentList.getListObject() != null && surgicalHistoryCurrentList.getListObject().size() > 0){
					List<PatientSurgicalHistoryDto> list=surgicalHistoryCurrentList.getListObject();
					iPatientSurgicalHistoryDao.saveSurgHistory(list, resDto);
				}
				
				//3 for medical history
				PatientMedicalHistoryDto patientMedicalHistoryDto=new PatientMedicalHistoryDto();
				patientMedicalHistoryDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientMedicalHistoryDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientMedicalHistoryDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientMedicalHistoryDto.setCreated_date(coversheetTemplateMasterDto.getCreatedDate());
				Response medicalHistoryCurrentList = iPatientMedicalHistoryDao.getCurrentPatientMedicalHistoryDetailsForCoversheet(patientMedicalHistoryDto);
				if(medicalHistoryCurrentList.getListObject() != null && medicalHistoryCurrentList.getListObject().size() > 0){
					@SuppressWarnings("unchecked")
					List<PatientMedicalHistoryDto> list=medicalHistoryCurrentList.getListObject();
					iPatientMedicalHistoryDao.saveMededicalHistoryCurrent(list, resDto);
				}
				
				//4 for Habit Master
				PatientHabitDetailsDto patientHabitDetailsDto=new PatientHabitDetailsDto();
				patientHabitDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientHabitDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientHabitDetailsDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientHabitDetailsDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response patientHabitList = iPatientHabitDetailsMapperDao.getCurrentPatientHabitDetails(patientHabitDetailsDto);
				if(patientHabitList.getListObject() != null && patientHabitList.getListObject().size() > 0){
					List<PatientHabitDetailsDto> list=patientHabitList.getListObject();
					iPatientHabitDetailsMapperDao.saveCurrentPatientHabitDetails(list, resDto);
				}
				
				//5 for Allergy
				AllergyPatientMapperDto allergyPatientMapperDto=new AllergyPatientMapperDto();
				allergyPatientMapperDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				allergyPatientMapperDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				allergyPatientMapperDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				allergyPatientMapperDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response allergyCurrentList = iAllergyPatientMapperDao.getCurrentAllergyPatientMapperForCoversheet(allergyPatientMapperDto);
				if(allergyCurrentList.getListObject() != null && allergyCurrentList.getListObject().size() > 0){
					List<AllergyPatientMapperDto> list=allergyCurrentList.getListObject();
					iAllergyPatientMapperDao.saveCurrentAllergyPatientMapper(list, resDto);
				}
				
				//6 for treatement
				PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto=new PatientTreatmentHistoryDetailsDto();
				patientTreatmentHistoryDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientTreatmentHistoryDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientTreatmentHistoryDetailsDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientTreatmentHistoryDetailsDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response treatmentHistoryCurrentList = iPatientTreatmentHistoryDetailsDao.getCurrentPatientTreatmentHistoryDetailsForCoversheet(patientTreatmentHistoryDetailsDto);
				if(treatmentHistoryCurrentList.getListObject() != null && treatmentHistoryCurrentList.getListObject().size() > 0){
					List<PatientTreatmentHistoryDetailsDto> list=treatmentHistoryCurrentList.getListObject();
					iPatientTreatmentHistoryDetailsDao.savePatientTreatmentHistoryDetails(list, resDto);
				}
				
				//7 for Medication history
				MedicationHistoryDto medicationHistoryDto=new MedicationHistoryDto();
				medicationHistoryDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				medicationHistoryDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				medicationHistoryDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				medicationHistoryDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response detailedMedicationsHistory = iMedicationHistoryDao.getMedicationHistoryList(medicationHistoryDto);
				if(detailedMedicationsHistory.getListObject() != null && detailedMedicationsHistory.getListObject().size() > 0){
					List<MedicationHistoryDto> list=detailedMedicationsHistory.getListObject();
					iMedicationHistoryDao.saveCurrentMedicationHistory(list, resDto);
				}
				
				//8 for family history
				PatientFamilyHistoryDto patientFamilyHistoryDto =new PatientFamilyHistoryDto();
				patientFamilyHistoryDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientFamilyHistoryDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientFamilyHistoryDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientFamilyHistoryDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response familyHistory = iPatientFamilyHistoryDao.getCurrentPatientFamilyHistoryForCoversheet(patientFamilyHistoryDto);
				if(familyHistory.getListObject() != null && familyHistory.getListObject().size() > 0){
					List<PatientFamilyHistoryDto> list=familyHistory.getListObject();
					iPatientFamilyHistoryDao.saveCurrentPatientFamilyHistory(list, resDto);
				}
				
				//9 for personal history
				PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto=new PatientPersonalHistoryDetailsDto();
				patientPersonalHistoryDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientPersonalHistoryDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientPersonalHistoryDetailsDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientPersonalHistoryDetailsDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response personalHistory = iPatientPersonalHistoryDetailsDao.getCurrentPatientPersonalHistoryDetailsForCoversheet(patientPersonalHistoryDetailsDto);
				if(personalHistory.getListObject() != null && personalHistory.getListObject().size() > 0){
					List<PatientPersonalHistoryDetailsDto> list=personalHistory.getListObject();
					iPatientPersonalHistoryDetailsDao.saveCurrentPatientPersonalHistoryDetails(list, resDto);
				}
				
				//10 for travel history 
				PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto=new PatientTravelHistoryDetailsDto();
				patientTravelHistoryDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientTravelHistoryDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientTravelHistoryDetailsDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientTravelHistoryDetailsDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response travelHistory=iPatientTravelHistoryDetailsDao.getCurrentPatientTravelHistoryDetailsForCoversheet(patientTravelHistoryDetailsDto);
				if(travelHistory.getListObject() != null && travelHistory.getListObject().size() > 0){
					List<PatientTravelHistoryDetailsDto> list=travelHistory.getListObject();
					iPatientTravelHistoryDetailsDao.saveCurrentPatientTravelHistoryDetails(list, resDto);
				}
				
//				for Investigation
				Response investListRes = iGlobalUnitMasterDao.getInvestigationsListForCoversheet(coversheetTemplateMasterDto.getPatientId(),false,coversheetTemplateMasterDto.getUnitId());
				if(investListRes.getListObject() != null && investListRes.getListObject().size() > 0){
					List<InvestigationOrderMasterNewDto> list=investListRes.getListObject();

					
					Integer selfPayeeId =null ;
					Integer defaultTariffId = null;
					Date date  = TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId());
					Response orderRes = null;
					Character isPreBilling = null;
					Integer defaultDiscountOwnership = 0;
					UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
						unitApplicationConfigurationDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId() );
						unitApplicationConfigurationDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
					Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
					
					if(appConfigRes.getObject()!=null)
					{
						UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();
						
						if(unitApplicationConfigurationDtos.getDefaultSelfTariffId()==null)
							return new Response<>(ERROR, COMMON_ERROR_CODE, "Default tariff is not defined !!", null, null);
						else
							defaultTariffId= unitApplicationConfigurationDtos.getDefaultSelfTariffId();
							
						if(unitApplicationConfigurationDtos.getSelfPayeeId()==null)
							return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee Id is not defined !!", null, null);
						else
							selfPayeeId=unitApplicationConfigurationDtos.getSelfPayeeId();
						
						if(unitApplicationConfigurationDtos.getIsPreOrPostBilling()==null)
							return new Response<>(ERROR, COMMON_ERROR_CODE, "Pre billing post billing is not defined !!", null, null);
						else	
							isPreBilling = unitApplicationConfigurationDtos.getIsPreOrPostBilling();
							
						defaultDiscountOwnership= unitApplicationConfigurationDtos.getDefaultDiscountOwnership();
					}else{
						return new Response<>(ERROR, COMMON_ERROR_CODE, "Unit Application Configuration is yet to set !!", null, null);
					}
			     
			   
					ListIterator<OrderMasterDto> iteratorOrderMaster = (ListIterator<OrderMasterDto>) list;
					
					while(iteratorOrderMaster.hasNext())	
					{
						List orderDetailsList = new ArrayList();
						OrderMasterDto orderMasterDto2 = iteratorOrderMaster.next();
						orderMasterDto2 = iGlobalUnitMasterDao.getPatientClassByVisitForNewCoversheet(orderMasterDto2);
						orderMasterDto2.setDefaultDiscountOwnership(defaultDiscountOwnership);
						if(orderMasterDto2.getListOrderDetailsMasterDto().size()>0)
						{
							if(orderMasterDto2.getEncounterId()==null && orderMasterDto2.getAdmissionId()==null)
							{    
								ListIterator<OrderDetailsMasterDto> iterator = orderMasterDto2.getListOrderDetailsMasterDto().listIterator();
								while(iterator.hasNext())
								{
									OrderMasterDto orderMasterDtoFutureOrder =  orderMasterDto2;
									OrderDetailsMasterDto detailsMasterDto = iterator.next();
									List orderDetailsListFuture = new LinkedList();
									OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
											orderDetailsMasterDto.setServiceId(detailsMasterDto.getServiceId());
											orderDetailsMasterDto.setServiceIsBilled(0);
											orderDetailsMasterDto.setCreatedBy(orderMasterDto2.getCreatedBy());
											orderDetailsMasterDto.setUpdatedBy(orderMasterDto2.getUpdatedBy());
											orderDetailsMasterDto.setOrderDate(orderMasterDto2.getCreatedDate());
											orderDetailsMasterDto.setOrderSchDate(orderMasterDto2.getCreatedDate());
											orderDetailsMasterDto.setCreatedDate(orderMasterDto2.getCreatedDate());
											orderDetailsMasterDto.setUpdatedDate(orderMasterDto2.getUpdatedDate());
											orderDetailsMasterDto.setStatus('A');
											orderDetailsMasterDto.setOrgId(orderMasterDto2.getOrgId());
											orderDetailsMasterDto.setOrgUnitId(orderMasterDto2.getOrgUnitId());
											orderDetailsMasterDto.setOrderQty(1);
											orderDetailsMasterDto.setOrdDocId(detailsMasterDto.getOrdDocId());
											orderDetailsMasterDto.setOrdDocSplId(detailsMasterDto.getOrdDocSplId());
											orderDetailsMasterDto.setServiceRenderingDeptId(detailsMasterDto.getServiceRenderingDeptId());
											
//											orderDetailsMasterDto.setPriorityId(2);
											orderDetailsMasterDto.setPriorityId(detailsMasterDto.getServicePriorityId());
											
											orderDetailsMasterDto.setIsOutsourced('N');
											orderDetailsMasterDto.setServiceRendered(0);
											orderDetailsMasterDto.setServiceChargeable(1);
											orderDetailsMasterDto.setOrdCancelled('N');
											orderDetailsMasterDto.setIsDrug('N');
											orderDetailsMasterDto.setIsInvestigationOrder('Y');
											orderDetailsMasterDto.setIsRefDoctorShare(detailsMasterDto.getIsRefDoctorShare());
											orderDetailsMasterDto.setDepartmentId(detailsMasterDto.getDepartmentId());
											orderDetailsMasterDto.setSubDepartmentId(detailsMasterDto.getSubDepartmentId());
											orderDetailsMasterDto.setOrderSetId(detailsMasterDto.getOrderSetId());
											orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
											orderDetailsMasterDto.setBedCategoryId(orderMasterDto2.getBillingBedCategoryId());
											orderDetailsMasterDto.setProcedureOrderMstId((detailsMasterDto.getProcedureOrderMstId()!=null && detailsMasterDto.getProcedureOrderMstId()!=0)?detailsMasterDto.getProcedureOrderMstId():null);
											orderDetailsMasterDto.setBedId(detailsMasterDto.getBedId());
											orderDetailsMasterDto.setWardId(detailsMasterDto.getWardId());
											orderDetailsMasterDto.setRoomId(detailsMasterDto.getRoomId());
											orderDetailsMasterDto.setOrdRemarks(detailsMasterDto.getOrdRemarks());
											if(orderDetailsMasterDto.getOrderSchDate()!=null) {
												orderDetailsMasterDto.setOrderSchDate(detailsMasterDto.getOrderSchDate());
											}
											else {
												orderDetailsMasterDto.setOrderSchDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime());
											}
								
									orderDetailsListFuture.add(orderDetailsMasterDto);
									orderMasterDtoFutureOrder.setListOrderDetailsMasterDto(orderDetailsListFuture);
									orderRes = iOrderMasterService.saveOrderMaster(orderMasterDtoFutureOrder);
								}
								
							}
							else
							{
								ListIterator<OrderDetailsMasterDto> iterator = orderMasterDto2.getListOrderDetailsMasterDto().listIterator();
								while(iterator.hasNext())
								{
									OrderDetailsMasterDto masterDto = iterator.next();
									
										OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
											orderDetailsMasterDto.setServiceId(masterDto.getServiceId());
											orderDetailsMasterDto.setServiceIsBilled(0);
											orderDetailsMasterDto.setOrderQty(1);
											orderDetailsMasterDto.setCreatedBy(orderMasterDto2.getCreatedBy());
											orderDetailsMasterDto.setUpdatedBy(orderMasterDto2.getUpdatedBy());
											orderDetailsMasterDto.setOrderDate(orderMasterDto2.getCreatedDate());
											orderDetailsMasterDto.setOrderSchDate(orderMasterDto2.getCreatedDate());
											orderDetailsMasterDto.setCreatedDate(orderMasterDto2.getCreatedDate());
											orderDetailsMasterDto.setUpdatedDate(orderMasterDto2.getUpdatedDate());
											orderDetailsMasterDto.setStatus('A');
											orderDetailsMasterDto.setOrgId(orderMasterDto2.getOrgId());
											orderDetailsMasterDto.setOrgUnitId(orderMasterDto2.getOrgUnitId());
											orderDetailsMasterDto.setOrdDocId(masterDto.getOrdDocId());
											orderDetailsMasterDto.setOrdDocSplId(masterDto.getPackageTypeId()!=null && masterDto.getPackageTypeId()==PACKAGE1 && masterDto.getOrdDocSplId()!=null && masterDto.getOrdDocSplId() > 1 ? masterDto.getOrdDocSplId() : masterDto.getOrdDocSplId());
											orderDetailsMasterDto.setServiceRenderingDeptId(masterDto.getServiceRenderingDeptId());
											orderDetailsMasterDto.setOrdCancelled('N');
											
//											orderDetailsMasterDto.setPriorityId(2);
											orderDetailsMasterDto.setPriorityId(masterDto.getServicePriorityId());

											orderDetailsMasterDto.setIsOutsourced('N');
											orderDetailsMasterDto.setIsInvestigationOrder('Y');
											orderDetailsMasterDto.setServiceRendered(0);
											orderDetailsMasterDto.setIsRefDoctorShare(masterDto.getIsRefDoctorShare());
											orderDetailsMasterDto.setDepartmentId(masterDto.getDepartmentId());
											orderDetailsMasterDto.setSubDepartmentId(masterDto.getSubDepartmentId());
											orderDetailsMasterDto.setOrderSetId(masterDto.getOrderSetId());
											orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
											orderDetailsMasterDto.setNursingServiceOrderFlag(masterDto.getNursingServiceOrderFlag());
											orderDetailsMasterDto.setBedCategoryId(orderMasterDto2.getBillingBedCategoryId());
											orderDetailsMasterDto.setProcedureOrderMstId((masterDto.getProcedureOrderMstId()!=null && masterDto.getProcedureOrderMstId()!=0)?masterDto.getProcedureOrderMstId():null);
											orderDetailsMasterDto.setBedId(masterDto.getBedId());
											orderDetailsMasterDto.setWardId(masterDto.getWardId());
											orderDetailsMasterDto.setRoomId(masterDto.getRoomId());
											orderDetailsMasterDto.setOrdRemarks(masterDto.getOrdRemarks());
										if(masterDto.isPackage())
										{
											    orderDetailsMasterDto.setServiceId(masterDto.getServiceId());
											    orderDetailsMasterDto.setServiceAmt(masterDto.getServiceAmt());
											    orderDetailsMasterDto.setServiceAmt(masterDto.getServiceAmt());
												Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(masterDto.getServiceId(),orderMasterDto2.getOrgUnitId());
												BigDecimal serviceAmt = BigDecimal.ZERO;
												BigDecimal netAmt= BigDecimal.ZERO;
												BigDecimal taxAmt= BigDecimal.ZERO;
												if(tax != null)
												{
													orderDetailsMasterDto.setTaxId(tax.getId());
													orderDetailsMasterDto.setTaxPer(new BigDecimal(tax.getTaxPercentage()));
													serviceAmt = masterDto.getServiceAmt()!=null?masterDto.getServiceAmt():new BigDecimal(0);
													taxAmt = serviceAmt.multiply((orderDetailsMasterDto.getTaxPer().multiply(new BigDecimal(0.01))));
													netAmt = serviceAmt.add(taxAmt);
												}else
												{
													orderDetailsMasterDto.setTaxPer(new BigDecimal(0.00));
													serviceAmt = masterDto.getServiceAmt();
													netAmt = serviceAmt;
												}
												orderDetailsMasterDto.setOrdTotalAmt(serviceAmt);
												orderDetailsMasterDto.setTaxAmount(taxAmt);
												orderDetailsMasterDto.setNetAmount(netAmt);
												orderDetailsMasterDto.setOrdConcession(masterDto.getOrdConcession());
												orderDetailsMasterDto.setOrdDiscount(masterDto.getOrdDiscount());
												orderDetailsMasterDto.setSelfPayable(masterDto.getSelfPayable());
												orderDetailsMasterDto.setCreditPayable(masterDto.getCreditPayable());
												orderDetailsMasterDto.setTariffId(defaultTariffId);
												orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
												orderDetailsMasterDto.setOrderEmergencyFlag('N');
												orderDetailsMasterDto.setServiceRendered(masterDto.getServiceRendered());
												orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
												orderDetailsMasterDto.setServiceChargeable(1);
												orderDetailsMasterDto.setServiceIsBilled(0);
												orderDetailsMasterDto.setPayeeId(selfPayeeId);
												orderDetailsMasterDto.setIsPackage(true);
												orderDetailsMasterDto.setPackage(true);
												orderDetailsMasterDto.setApportionedPrice(masterDto.getApportionedPrice());
												orderDetailsMasterDto.setPackageId(masterDto.getPackageId());
												orderDetailsMasterDto.setPackageTypeId(masterDto.getPackageTypeId());
												orderDetailsMasterDto.setIsPackageService(masterDto.getIsPackageService());
												//orderDetailsMasterDto.setPackageEitherOrGroupId(dto.get);
												orderDetailsMasterDto.setIsCapService(masterDto.getIsCapService());
												orderDetailsMasterDto.setPaymentEntitlementId(orderMasterDto2.getPaymentEntitlementTypeId());
												orderDetailsMasterDto.setOrdCancelled('N');
												orderDetailsMasterDto.setIsDrug('N');
												orderDetailsMasterDto.setServiceRendered(0);
												orderDetailsMasterDto.setStatus('A');
												orderDetailsMasterDto.setIsInvestigationOrder('N');
												orderDetailsMasterDto.setCoPayPer(masterDto.getCoPayPer());
												orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(masterDto.getOrderDetailsPayeeMapperDtosList());
												orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(masterDto.getOrderDetailsPackageMapperDtosList());
										}
										else if(masterDto.getPackageTypeId()!=null && masterDto.getPackageTypeId().intValue()==PACKAGE1 && !masterDto.isPackage())
										{
											orderDetailsMasterDto.setOrderDetailsId(masterDto.getOrderDetailsId());
										    orderDetailsMasterDto.setServiceId(masterDto.getServiceId());
										    orderDetailsMasterDto.setServiceAmt(masterDto.getServiceAmt());
											orderDetailsMasterDto.setTariffId(defaultTariffId);
											orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
											orderDetailsMasterDto.setOrderEmergencyFlag('N');
											orderDetailsMasterDto.setServiceRendered(masterDto.getServiceRendered());
											orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
											orderDetailsMasterDto.setServiceChargeable(1);
											orderDetailsMasterDto.setServiceIsBilled(0);
											orderDetailsMasterDto.setPayeeId(selfPayeeId);
											orderDetailsMasterDto.setIsPackage(false);
											orderDetailsMasterDto.setPackage(false);
											orderDetailsMasterDto.setApportionedPrice(masterDto.getApportionedPrice());
											orderDetailsMasterDto.setPackageId(masterDto.getPackageId());
											orderDetailsMasterDto.setPackageTypeId(masterDto.getPackageTypeId());
											orderDetailsMasterDto.setIsPackageService(masterDto.getIsPackageService());
											//orderDetailsMasterDto.setPackageEitherOrGroupId(dto.get);
											orderDetailsMasterDto.setIsCapService(masterDto.getIsCapService());
											orderDetailsMasterDto.setPaymentEntitlementId(orderMasterDto2.getPaymentEntitlementTypeId());
											orderDetailsMasterDto.setOrdCancelled('N');
											orderDetailsMasterDto.setIsDrug('N');
											orderDetailsMasterDto.setServiceRendered(0);
											orderDetailsMasterDto.setStatus('A');
											orderDetailsMasterDto.setIsInvestigationOrder('N');
											orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
											List<OrderDetailsPayeeMapperDto> list1 = new LinkedList<>();
											OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
												detailsPayeeMapperDto.setPayeeId(orderDetailsMasterDto.getPayeeId());
												detailsPayeeMapperDto.setPayeeAmount(BigDecimal.ZERO);
												detailsPayeeMapperDto.setServiceId(orderDetailsMasterDto.getServiceId());
											list1.add(detailsPayeeMapperDto);	
											orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(list1);
											orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(masterDto.getOrderDetailsPackageMapperDtosList());
											
										  }
										else{
											
											  ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
												consumptionDto.setOrganizationId(orderMasterDto2.getOrgId());
												consumptionDto.setUnitId(orderMasterDto2.getOrgUnitId());
												consumptionDto.setVisitTypeId(orderMasterDto2.getVisitTypeId());
												consumptionDto.setPatientTypeId(orderMasterDto2.getPatientTypeId());
												consumptionDto.setBillingBedCategoryId(orderMasterDto2.getBillingBedCategoryId());
												consumptionDto.setPaymentEntitlementId(orderMasterDto2.getPaymentEntitlementTypeId());
												consumptionDto.setServiceId(masterDto.getServiceId());
												consumptionDto.setCurrentDate(orderMasterDto2.getCurrentDate());
												consumptionDto.setContractId(orderMasterDto2.getContractId());
												consumptionDto.setEncounterId(orderMasterDto2.getEncounterId());
												consumptionDto.setAdmissionId(orderMasterDto2.getAdmissionId());
												consumptionDto.setDefaultSelfTariffId(defaultTariffId);
												consumptionDto.setSelfPayeeId(selfPayeeId);
												consumptionDto.setPatientId(orderMasterDto2.getPatientId());
												consumptionDto.setPatientClassId(orderMasterDto2.getPatientClassId());
											Response res1 =iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
												consumptionDto=(ContractConsumptionDto)res1.getObject();
											
											if(res1.getObject()!=null)
											{
												orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
												orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
												orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
												orderDetailsMasterDto.setTariffId(defaultTariffId);
												orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
												orderDetailsMasterDto.setOrderEmergencyFlag('N');
												orderDetailsMasterDto.setServiceRendered(0);
												orderDetailsMasterDto.setServiceChargeable(1);
												orderDetailsMasterDto.setServiceIsBilled(0);
												orderDetailsMasterDto.setIsDrug('N');
												orderDetailsMasterDto.setPackage(masterDto.isPackage());
												orderDetailsMasterDto.setPackageId(masterDto.getPackageId());
												orderDetailsMasterDto.setIsInvestigationOrder('Y');
												orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
												orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
												orderDetailsMasterDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));
												orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
												orderDetailsMasterDto.setOrdTotalAmt(BigDecimal.valueOf(consumptionDto.getServiceRate()));
												orderDetailsMasterDto.setTaxAmount(BigDecimal.valueOf(consumptionDto.getTaxAmt()));
												orderDetailsMasterDto.setNetAmount(BigDecimal.valueOf(consumptionDto.getNetAmount()));
												orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()));
												orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
												orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
												orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
												orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
												orderDetailsMasterDto.setTaxId(consumptionDto.getTaxId());
												orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
												orderDetailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
												orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
												if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
													orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
													
											}
										 }
									
										if(orderDetailsMasterDto.getOrderDetailsId()!=null && orderDetailsMasterDto.getOrderDetailsId()>0) {								
											orderDetailsMasterDto.setIsInvestigationOrder('Y');
											if(masterDto.getOrderId()==null)
												iOrderMasterDao.updateOrderDetailsByOdrDtlsId(orderDetailsMasterDto.getOrderDetailsId());
											else {
												 orderDetailsMasterDto.setOrderId(masterDto.getOrderId());
												iOrderDetailsMasterDao.updateOrderDetails(orderDetailsMasterDto,'N',selfPayeeId);
											}							
											
										}
											
										else
											 orderDetailsList.add(orderDetailsMasterDto);
										if(orderMasterDto2.getOrdDocSplId() == null) {
										   orderMasterDto2.setOrdDocSplId(masterDto.getPackageTypeId()!=null && masterDto.getPackageTypeId()==PACKAGE1 && masterDto.getOrdDocSplId()!=null && masterDto.getOrdDocSplId() > 1 ? masterDto.getOrdDocSplId() : masterDto.getOrdDocSplId());
										}
										   orderMasterDto2.setListOrderDetailsMasterDto(orderDetailsList);
								}
		
								if(orderMasterDto2.getSlotMasterDtoList()!=null && orderMasterDto2.getSlotMasterDtoList().size()>0){
									orderMasterDto2.setSlotMasterDtoList(orderMasterDto2.getSlotMasterDtoList());
								}
								if(orderMasterDto2.getListOrderDetailsMasterDto()!=null && orderMasterDto2.getListOrderDetailsMasterDto().size()>0)
									 orderRes = iOrderMasterService.saveOrderMaster(orderMasterDto2);
							}
						}
					}
//				return new Response(SUCCESS, null, "Order placed !!", null, selfPayeeId,orderRes,orderRes.getListObject2());
			
				}
				
				//11 for prescription 
				PrescriptionDto prescriptionDtoSave=new PrescriptionDto();
				prescriptionDtoSave.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				prescriptionDtoSave.setUnitId(coversheetTemplateMasterDto.getUnitId());
				prescriptionDtoSave.setPatientId(coversheetTemplateMasterDto.getPatientId());
				prescriptionDtoSave.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
				Response detailedMedications = iMedicationDAO.getMedicationDetailsByDoctorId(prescriptionDtoSave);
				if(detailedMedications.getListObject() != null && detailedMedications.getListObject().size() > 0){
					List<MedicationDTO> list=detailedMedications.getListObject();
					for (MedicationDTO medicationDTO : list) {
					 
						prescriptionDtoSave.setIsIntervention(FALSE);
						prescriptionDtoSave.setIsActive(TRUE);
						prescriptionDtoSave.setIsDeleted(FALSE);
						prescriptionDtoSave.setIsValidate(FALSE);
						prescriptionDtoSave.setIsDischarge(FALSE);
						prescriptionDtoSave.setReconciliationDone(FALSE);
						prescriptionDtoSave.setIsNonFormulary('N');
						prescriptionDtoSave.setJustificationStop(medicationDTO.getJustificationStop());
						prescriptionDtoSave.setIsMedicationStop('N');
						prescriptionDtoSave.setVisitTypeId(medicationDTO.getVisitTypeId());
						prescriptionDtoSave.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
						prescriptionDtoSave.setAuthorisedBy(coversheetTemplateMasterDto.getCreatedBy());
						prescriptionDtoSave.setAuthorisedDate(coversheetTemplateMasterDto.getCreatedDate());
						prescriptionDtoSave.setPriorityId(2);
						prescriptionDtoSave.setStatusId(1);
						prescriptionDtoSave.setPrescriptionDate(coversheetTemplateMasterDto.getCreatedDate());
						prescriptionDtoSave.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
						prescriptionDtoSave.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						prescriptionDtoSave.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
						prescriptionDtoSave.setMedicationType(1);
						prescriptionDtoSave.setAdmissionId(null);
						prescriptionDtoSave.setEncounterId(null);
						prescriptionDtoSave.setPatientId(null);
//						prescriptionDto.setPrescriptionNo(getPrescriptionNumber());	
						prescriptionDtoSave.setIsMedicationStop('N');	
						if(medicationDTO.getDoctorId() != null) {
							prescriptionDtoSave.setDoctorId(medicationDTO.getDoctorId());
						}
						if(medicationDTO.getDeptId() != null) {
						prescriptionDtoSave.setDeptId(medicationDTO.getDeptId());
						}
//						prescriptionDto.setJustification_stop(prescriptionDto.getJustification_stop());
						String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0, prescriptionDtoSave.getUnitId(), PRES_NO);
						prescriptionDtoSave.setIsNonFormulary('N');
						
						prescriptionDtoSave.setPrescriptionNo(prescriptionNo);
						Response presRes = iMedicationDAO.savePrescription(prescriptionDtoSave);
						prescriptionDtoSave = (PrescriptionDto) presRes.getObject();
					}
					List<PrescriptionDetailsDto> listDetails=new ArrayList<>();
					for (MedicationDTO medicationDTO : list) {
						PrescriptionDetailsDto details=new PrescriptionDetailsDto();
						details.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						details.setUnitId(coversheetTemplateMasterDto.getUnitId());
						details.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
						
						
						details.setIsDiscontinued(FALSE);
						details.setIsMedicationStop('N');
						details.setIsPreStop(FALSE);
						details.setIsIntervention(FALSE);
						details.setDuration(medicationDTO.getDuration()!=null ? medicationDTO.getDuration() :null);
						details.setDurationUnit(medicationDTO.getDurationUnit()!=null ? medicationDTO.getDurationUnit() :null);
						details.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
						details.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
						details.setOrderQty(medicationDTO.getOrderQty());
						details.setPriorityId(medicationDTO.getPriorityId());
						details.setIsDeleted(FALSE);
						details.setIsActive(TRUE);
						details.setStatusId(1);
						details.setPrnflag(FALSE);
						details.setItemId(medicationDTO.getItemId()!=null ? medicationDTO.getItemId():null);
//						details.setPrescribedDose(medicationDTO.getPrescribedDose() !=null ? medicationDTO.getPrescribedDose(): null);
						details.setStatflag(medicationDTO.getStatflag());
						details.setGenericName(medicationDTO.getGenericName());
						details.setItemName(medicationDTO.getItemName());
						details.setIsNonFormulary(medicationDTO.getIsNonFormulary());
						details.setInfusionflag(FALSE);
						details.setUpdatedBy(prescriptionDtoSave.getAddedBy());
						details.setInstructionDesc(medicationDTO.getInstructionDesc());
						details.setUpdatedDate(medicationDTO.getAddedDate());
						if(details.getIsDischarge()!=null){
							details.setIsDischarge(medicationDTO.getIsDischarge());
						}else{
							details.setIsDischarge(false);
						}
						listDetails.add(details);
					}
					
					prescriptionDtoSave.setPrescriptionDetailsDtosList(listDetails);
					ListIterator<PrescriptionDetailsDto> iterator = prescriptionDtoSave.getPrescriptionDetailsDtosList().listIterator();
					while(iterator.hasNext())
					{
						PrescriptionDetailsDto prescriptionDetailsDto = iterator.next();
							prescriptionDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
							prescriptionDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
							prescriptionDetailsDto.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
							prescriptionDetailsDto.setIsDiscontinued(FALSE);
							prescriptionDetailsDto.setIsMedicationStop('N');
							prescriptionDetailsDto.setIsPreStop(FALSE);
							prescriptionDetailsDto.setIsIntervention(FALSE);
							prescriptionDetailsDto.setDuration(prescriptionDetailsDto.getDuration()!=null ? prescriptionDetailsDto.getDuration() :null);
							prescriptionDetailsDto.setDurationUnit(prescriptionDetailsDto.getDurationUnit()!=null ? prescriptionDetailsDto.getDurationUnit() :null);
							prescriptionDetailsDto.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
							prescriptionDetailsDto.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
							prescriptionDetailsDto.setOrderQty(prescriptionDetailsDto.getOrderQty());
							prescriptionDetailsDto.setPriorityId(prescriptionDetailsDto.getPriorityId());
							prescriptionDetailsDto.setIsDeleted(FALSE);
							prescriptionDetailsDto.setIsActive(TRUE);
							prescriptionDetailsDto.setStatusId(1);
							prescriptionDetailsDto.setPrnflag(FALSE);
							prescriptionDetailsDto.setItemId(prescriptionDetailsDto.getItemId()!=null ? prescriptionDetailsDto.getItemId():null);
							prescriptionDetailsDto.setPrescribedDose(prescriptionDetailsDto.getPrescribedDose() !=null ? prescriptionDetailsDto.getPrescribedDose(): null);
							prescriptionDetailsDto.setStatflag(prescriptionDetailsDto.getStatflag());
							prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
							prescriptionDetailsDto.setFrequencyId(prescriptionDetailsDto.getFrequencyId());
							prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
							prescriptionDetailsDto.setIsNonFormulary(prescriptionDetailsDto.getIsNonFormulary());
							prescriptionDetailsDto.setInfusionflag(FALSE);
							prescriptionDetailsDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
							prescriptionDetailsDto.setInstructionDesc(prescriptionDetailsDto.getInstructionDesc());
							prescriptionDetailsDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
							if(prescriptionDetailsDto.getIsDischarge()!=null){
								prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
							}else{
								prescriptionDetailsDto.setIsDischarge(false);
							}
						Response resPreDtls = iMedicationDAO.savePrescriptionDetais(prescriptionDetailsDto);	
							
						prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls.getObject();
								
						iMedicationDAO.savePrescriptionFrequencyDetails(prescriptionDetailsDto);
					}
				
//				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, prescriptionDtoSave);
				}
				
				//12 consult order
				ConsultDoctorDto consultDoctorDto=new ConsultDoctorDto();
				consultDoctorDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
				Response consultOrdersList = iConsultDoctorDao.consultDoctorListByEncounter(consultDoctorDto.getEncounterId());
				if(consultOrdersList.getListObject() != null && consultOrdersList.getListObject().size() > 0){
					List<ConsultDoctorDto> list=consultOrdersList.getListObject();
					iConsultDoctorDao.savePatientConsultDoctor(list, resDto);
				}
				
				//13 for observation
				ObservationDto observationDto=new ObservationDto();
				observationDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				observationDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				observationDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				observationDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
				observationDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response observationList = iObservationDao.getObservationList(observationDto);
				if(observationList.getListObject() != null && observationList.getListObject().size() > 0){
					List<ObservationDto> list=observationList.getListObject();
					iObservationDao.savefPatientObservation(list, resDto);
				}
				
				//14 for Vital
				AppointmentVitalMapperDto appointmentVitalMapperDto=new AppointmentVitalMapperDto();
				appointmentVitalMapperDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				appointmentVitalMapperDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				appointmentVitalMapperDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				appointmentVitalMapperDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
				appointmentVitalMapperDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response patientViatalList = iAppointmentVitalMapperDao.getListOfVitalsByPatientIdForCoversheet(appointmentVitalMapperDto);
				if(patientViatalList.getListObject() != null && patientViatalList.getListObject().size() > 0){
					List<AppointmentVitalMapperDto> list=patientViatalList.getListObject();
					iAppointmentVitalMapperDao.saveCurrentAppointmentVitals(list, resDto);
				}
				
				//15 for Diagnosis
				DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto=new DiagnosisPatientAppointmentMapperDto();
				diagnosisPatientAppointmentMapperDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				diagnosisPatientAppointmentMapperDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				Response diagnosisPatientList = iPatientDiagnosisDao.getPatientDiagnosisByPatientId(diagnosisPatientAppointmentMapperDto);
				if(diagnosisPatientList.getListObject() != null && diagnosisPatientList.getListObject().size() > 0){
					List<DiagnosisPatientAppointmentMapperDto> list=diagnosisPatientList.getListObject();
					iPatientDiagnosisDao.saveDiagnosisPatientList(list, resDto);
				}
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getCoversheetAsTemaplateList(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		return iCoversheetTemaplateDao.getCoversheetAsTemaplateList(coversheetTemplateMasterDto);
	}

	@Override
	public Response getAllCoversheetListsByTemplateId(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try {
				return iCoversheetTemaplateDao.getAllCoversheetListsByTemplateId(coversheetTemplateMasterDto);
		} catch (Exception e) {
		logger.error("getAllCoversheetListsByTemplateId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateCoversheetAsTemaplate(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try {
			Response res=null;
			/*if(coversheetTemplateMasterDto.getCovTemplateId() != null) {
				Response resList= iCoversheetTemaplateDao.checkAvailableCoversheetAsTemaplate(coversheetTemplateMasterDto);
				if (resList.getListObject() != null && resList.getListObject().size() > 0) {

					return new Response<>(ERROR, ERROR_CODE, ALREADY_EXIST, null, null);
				}
			}			
			 else {
				 res = iCoversheetTemaplateDao.saveCoversheetAsTemaplate(coversheetTemplateMasterDto);
			}*/
			
			if(coversheetTemplateMasterDto.getCovTemplateId()!=null) {
//				CoversheetTemplateMasterDto resDto=(CoversheetTemplateMasterDto) res.getObject();
				
				//1 For complaints
				ComplaintAppointmentMapperDto complaintAppointmentMapperDto=new ComplaintAppointmentMapperDto();
				complaintAppointmentMapperDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				complaintAppointmentMapperDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				complaintAppointmentMapperDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
				Response complaintListRes = iComplaintAppointmentMapperDao.getListOfComplaintAppointmentMapper(complaintAppointmentMapperDto);
				if(complaintListRes.getListObject() != null && complaintListRes.getListObject().size() > 0){
					List<ComplaintAppointmentDto> list=complaintListRes.getListObject();
					iComplaintAppointmentMapperDao.updateCovTempComplaintAppointmentMapper(list, coversheetTemplateMasterDto);
				}
				
				//2 for Surgery
				PatientSurgicalHistoryDto patientSurgicalHistoryDto=new PatientSurgicalHistoryDto();
				patientSurgicalHistoryDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientSurgicalHistoryDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientSurgicalHistoryDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientSurgicalHistoryDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response surgicalHistoryCurrentList = iPatientSurgicalHistoryDao.getCurrentPatientSurgicalHistoryForCoversheet(patientSurgicalHistoryDto);
				if(surgicalHistoryCurrentList.getListObject() != null && surgicalHistoryCurrentList.getListObject().size() > 0){
					List<PatientSurgicalHistoryDto> list=surgicalHistoryCurrentList.getListObject();
					iPatientSurgicalHistoryDao.updateCovTempSurgHistory(list, coversheetTemplateMasterDto);
				}
				
				//3 for medical history
				PatientMedicalHistoryDto patientMedicalHistoryDto=new PatientMedicalHistoryDto();
				patientMedicalHistoryDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientMedicalHistoryDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientMedicalHistoryDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientMedicalHistoryDto.setCreated_date(coversheetTemplateMasterDto.getCreatedDate());
				Response medicalHistoryCurrentList = iPatientMedicalHistoryDao.getCurrentPatientMedicalHistoryDetailsForCoversheet(patientMedicalHistoryDto);
				if(medicalHistoryCurrentList.getListObject() != null && medicalHistoryCurrentList.getListObject().size() > 0){
					@SuppressWarnings("unchecked")
					List<PatientMedicalHistoryDto> list=medicalHistoryCurrentList.getListObject();
					iPatientMedicalHistoryDao.updateCovTempMededicalHistoryCurrent(list, coversheetTemplateMasterDto);
				}
				
				//4 for Habit Master
				PatientHabitDetailsDto patientHabitDetailsDto=new PatientHabitDetailsDto();
				patientHabitDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientHabitDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientHabitDetailsDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientHabitDetailsDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response patientHabitList = iPatientHabitDetailsMapperDao.getCurrentPatientHabitDetails(patientHabitDetailsDto);
				if(patientHabitList.getListObject() != null && patientHabitList.getListObject().size() > 0){
					List<PatientHabitDetailsDto> list=patientHabitList.getListObject();
					iPatientHabitDetailsMapperDao.updateCovTempCurrentPatientHabitDetails(list, coversheetTemplateMasterDto);
				}
				
				//5 for Allergy
				AllergyPatientMapperDto allergyPatientMapperDto=new AllergyPatientMapperDto();
				allergyPatientMapperDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				allergyPatientMapperDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				allergyPatientMapperDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				allergyPatientMapperDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response allergyCurrentList = iAllergyPatientMapperDao.getCurrentAllergyPatientMapperForCoversheet(allergyPatientMapperDto);
				if(allergyCurrentList.getListObject() != null && allergyCurrentList.getListObject().size() > 0){
					List<AllergyPatientMapperDto> list=allergyCurrentList.getListObject();
					iAllergyPatientMapperDao.updateCovTempCurrentAllergyPatientMapper(list, coversheetTemplateMasterDto);
				}
				
				//6 for treatement
				PatientTreatmentHistoryDetailsDto patientTreatmentHistoryDetailsDto=new PatientTreatmentHistoryDetailsDto();
				patientTreatmentHistoryDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientTreatmentHistoryDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientTreatmentHistoryDetailsDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientTreatmentHistoryDetailsDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response treatmentHistoryCurrentList = iPatientTreatmentHistoryDetailsDao.getCurrentPatientTreatmentHistoryDetailsForCoversheet(patientTreatmentHistoryDetailsDto);
				if(treatmentHistoryCurrentList.getListObject() != null && treatmentHistoryCurrentList.getListObject().size() > 0){
					List<PatientTreatmentHistoryDetailsDto> list=treatmentHistoryCurrentList.getListObject();
					iPatientTreatmentHistoryDetailsDao.updateCovTempPatientTreatmentHistoryDetails(list, coversheetTemplateMasterDto);
				}
				
				//7 for Medication history
				MedicationHistoryDto medicationHistoryDto=new MedicationHistoryDto();
				medicationHistoryDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				medicationHistoryDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				medicationHistoryDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				medicationHistoryDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response detailedMedicationsHistory = iMedicationHistoryDao.getMedicationHistoryList(medicationHistoryDto);
				if(detailedMedicationsHistory.getListObject() != null && detailedMedicationsHistory.getListObject().size() > 0){
					List<MedicationHistoryDto> list=detailedMedicationsHistory.getListObject();
					iMedicationHistoryDao.updateCovTempCurrentMedicationHistory(list, coversheetTemplateMasterDto);
				}
				
				//8 for family history
				PatientFamilyHistoryDto patientFamilyHistoryDto =new PatientFamilyHistoryDto();
				patientFamilyHistoryDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientFamilyHistoryDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientFamilyHistoryDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientFamilyHistoryDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response familyHistory = iPatientFamilyHistoryDao.getCurrentPatientFamilyHistoryForCoversheet(patientFamilyHistoryDto);
				if(familyHistory.getListObject() != null && familyHistory.getListObject().size() > 0){
					List<PatientFamilyHistoryDto> list=familyHistory.getListObject();
					iPatientFamilyHistoryDao.updateCovTempCurrentPatientFamilyHistory(list, coversheetTemplateMasterDto);
				}
				
				//9 for personal history
				PatientPersonalHistoryDetailsDto patientPersonalHistoryDetailsDto=new PatientPersonalHistoryDetailsDto();
				patientPersonalHistoryDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientPersonalHistoryDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientPersonalHistoryDetailsDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientPersonalHistoryDetailsDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response personalHistory = iPatientPersonalHistoryDetailsDao.getCurrentPatientPersonalHistoryDetailsForCoversheet(patientPersonalHistoryDetailsDto);
				if(personalHistory.getListObject() != null && personalHistory.getListObject().size() > 0){
					List<PatientPersonalHistoryDetailsDto> list=personalHistory.getListObject();
					iPatientPersonalHistoryDetailsDao.updateCovTempCurrentPatientPersonalHistoryDetails(list, coversheetTemplateMasterDto);
				}
				
				//10 for travel history 
				PatientTravelHistoryDetailsDto patientTravelHistoryDetailsDto=new PatientTravelHistoryDetailsDto();
				patientTravelHistoryDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				patientTravelHistoryDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				patientTravelHistoryDetailsDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				patientTravelHistoryDetailsDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response travelHistory=iPatientTravelHistoryDetailsDao.getCurrentPatientTravelHistoryDetailsForCoversheet(patientTravelHistoryDetailsDto);
				if(travelHistory.getListObject() != null && travelHistory.getListObject().size() > 0){
					List<PatientTravelHistoryDetailsDto> list=travelHistory.getListObject();
					iPatientTravelHistoryDetailsDao.updateCovTempCurrentPatientTravelHistoryDetails(list, coversheetTemplateMasterDto);
				}
				
				//11 for prescription 
				PrescriptionDto prescriptionDtoSave=new PrescriptionDto();
				prescriptionDtoSave.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				prescriptionDtoSave.setUnitId(coversheetTemplateMasterDto.getUnitId());
				prescriptionDtoSave.setPatientId(coversheetTemplateMasterDto.getPatientId());
				prescriptionDtoSave.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
				Response detailedMedications = iMedicationDAO.getMedicationDetailsByDoctorId(prescriptionDtoSave);
				if(detailedMedications.getListObject() != null && detailedMedications.getListObject().size() > 0){
					List<MedicationDTO> list=detailedMedications.getListObject();
					for (MedicationDTO medicationDTO : list) {
						prescriptionDtoSave.setIsIntervention(FALSE);
						prescriptionDtoSave.setIsActive(TRUE);
						prescriptionDtoSave.setIsDeleted(FALSE);
						prescriptionDtoSave.setIsValidate(FALSE);
						prescriptionDtoSave.setIsDischarge(FALSE);
						//prescriptionDto.setPriorityId(2);
						prescriptionDtoSave.setStatusId(1);
						prescriptionDtoSave.setCovTemplateId(coversheetTemplateMasterDto.getCovTemplateId());
						prescriptionDtoSave.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						prescriptionDtoSave.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
						prescriptionDtoSave.setMedicationType(1);
						prescriptionDtoSave.setAdmissionId(null);
						prescriptionDtoSave.setEncounterId(null);
						prescriptionDtoSave.setPatientId(null);
//						prescriptionDto.setPrescriptionNo(getPrescriptionNumber());	
						prescriptionDtoSave.setIsMedicationStop('N');	
//						prescriptionDto.setJustification_stop(prescriptionDto.getJustification_stop());
						String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0, prescriptionDtoSave.getUnitId(), PRES_NO);
						prescriptionDtoSave.setIsNonFormulary('N');
						
						prescriptionDtoSave.setPrescriptionNo(prescriptionNo);
						Response presRes = iMedicationDAO.updatePrescription(prescriptionDtoSave);
						prescriptionDtoSave = (PrescriptionDto) presRes.getObject();
					}
					List<PrescriptionDetailsDto> listDetails=new ArrayList<>();
					for (MedicationDTO medicationDTO : list) {
						PrescriptionDetailsDto details=new PrescriptionDetailsDto();
						details.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						details.setUnitId(coversheetTemplateMasterDto.getUnitId());
						details.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
						
						
						details.setIsDiscontinued(FALSE);
						details.setIsMedicationStop('N');
						details.setIsPreStop(FALSE);
						details.setIsIntervention(FALSE);
						details.setDuration(medicationDTO.getDuration()!=null ? medicationDTO.getDuration() :null);
						details.setDurationUnit(medicationDTO.getDurationUnit()!=null ? medicationDTO.getDurationUnit() :null);
						details.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
						details.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
						details.setOrderQty(medicationDTO.getOrderQty());
						details.setPriorityId(medicationDTO.getPriorityId());
						details.setIsDeleted(FALSE);
						details.setIsActive(TRUE);
						details.setStatusId(1);
						details.setPrnflag(FALSE);
						details.setItemId(medicationDTO.getItemId()!=null ? medicationDTO.getItemId():null);
//						details.setPrescribedDose(medicationDTO.getPrescribedDose() !=null ? medicationDTO.getPrescribedDose(): null);
						details.setStatflag(medicationDTO.getStatflag());
						details.setGenericName(medicationDTO.getGenericName());
						details.setItemName(medicationDTO.getItemName());
						details.setIsNonFormulary(medicationDTO.getIsNonFormulary());
						details.setInfusionflag(FALSE);
						details.setUpdatedBy(prescriptionDtoSave.getAddedBy());
						details.setInstructionDesc(medicationDTO.getInstructionDesc());
						details.setUpdatedDate(medicationDTO.getAddedDate());
						if(details.getIsDischarge()!=null){
							details.setIsDischarge(medicationDTO.getIsDischarge());
						}else{
							details.setIsDischarge(false);
						}
						listDetails.add(details);
					}
					
					prescriptionDtoSave.setPrescriptionDetailsDtosList(listDetails);
					ListIterator<PrescriptionDetailsDto> iterator = prescriptionDtoSave.getPrescriptionDetailsDtosList().listIterator();
					while(iterator.hasNext())
					{
						PrescriptionDetailsDto prescriptionDetailsDto = iterator.next();
							prescriptionDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
							prescriptionDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
							prescriptionDetailsDto.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
							prescriptionDetailsDto.setIsDiscontinued(FALSE);
							prescriptionDetailsDto.setIsMedicationStop('N');
							prescriptionDetailsDto.setIsPreStop(FALSE);
							prescriptionDetailsDto.setIsIntervention(FALSE);
							prescriptionDetailsDto.setDuration(prescriptionDetailsDto.getDuration()!=null ? prescriptionDetailsDto.getDuration() :null);
							prescriptionDetailsDto.setDurationUnit(prescriptionDetailsDto.getDurationUnit()!=null ? prescriptionDetailsDto.getDurationUnit() :null);
							prescriptionDetailsDto.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
							prescriptionDetailsDto.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
							prescriptionDetailsDto.setOrderQty(prescriptionDetailsDto.getOrderQty());
							prescriptionDetailsDto.setPriorityId(prescriptionDetailsDto.getPriorityId());
							prescriptionDetailsDto.setIsDeleted(FALSE);
							prescriptionDetailsDto.setIsActive(TRUE);
							prescriptionDetailsDto.setStatusId(1);
							prescriptionDetailsDto.setPrnflag(FALSE);
							prescriptionDetailsDto.setItemId(prescriptionDetailsDto.getItemId()!=null ? prescriptionDetailsDto.getItemId():null);
							prescriptionDetailsDto.setPrescribedDose(prescriptionDetailsDto.getPrescribedDose() !=null ? prescriptionDetailsDto.getPrescribedDose(): null);
							prescriptionDetailsDto.setStatflag(prescriptionDetailsDto.getStatflag());
							prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
							prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
							prescriptionDetailsDto.setIsNonFormulary(prescriptionDetailsDto.getIsNonFormulary());
							prescriptionDetailsDto.setInfusionflag(FALSE);
							prescriptionDetailsDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
							prescriptionDetailsDto.setInstructionDesc(prescriptionDetailsDto.getInstructionDesc());
							prescriptionDetailsDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
							if(prescriptionDetailsDto.getIsDischarge()!=null){
								prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
							}else{
								prescriptionDetailsDto.setIsDischarge(false);
							}
						Response resPreDtls = iMedicationDAO.updatePrescriptionDetail(prescriptionDetailsDto);	
							
						prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls.getObject();
								
						iMedicationDAO.updatePrescriptionFrequencyDetails(prescriptionDetailsDto);
					}
				
//				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, prescriptionDtoSave);
				}
				
				//12 consult order
				ConsultDoctorDto consultDoctorDto=new ConsultDoctorDto();
				consultDoctorDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
				Response consultOrdersList = iConsultDoctorDao.consultDoctorListByEncounter(consultDoctorDto.getEncounterId());
				if(consultOrdersList.getListObject() != null && consultOrdersList.getListObject().size() > 0){
					List<ConsultDoctorDto> list=consultOrdersList.getListObject();
					iConsultDoctorDao.updateCovTempPatientConsultDoctor(list, coversheetTemplateMasterDto);
				}
				
				//13 for observation
				ObservationDto observationDto=new ObservationDto();
				observationDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				observationDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				observationDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				observationDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
				observationDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response observationList = iObservationDao.getObservationList(observationDto);
				if(observationList.getListObject() != null && observationList.getListObject().size() > 0){
					List<ObservationDto> list=observationList.getListObject();
					iObservationDao.updateCovTempPatientObservation(list, coversheetTemplateMasterDto);
				}
				
				//14 for Vital
				AppointmentVitalMapperDto appointmentVitalMapperDto=new AppointmentVitalMapperDto();
				appointmentVitalMapperDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				appointmentVitalMapperDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				appointmentVitalMapperDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				appointmentVitalMapperDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
				appointmentVitalMapperDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
				Response patientViatalList = iAppointmentVitalMapperDao.getListOfVitalsByPatientIdForCoversheet(appointmentVitalMapperDto);
				if(patientViatalList.getListObject() != null && patientViatalList.getListObject().size() > 0){
					List<AppointmentVitalMapperDto> list=patientViatalList.getListObject();
					iAppointmentVitalMapperDao.updateCovTempCurrentAppointmentVitals(list, coversheetTemplateMasterDto);
				}
				
				//15 for Diagnosis
				DiagnosisPatientAppointmentMapperDto diagnosisPatientAppointmentMapperDto=new DiagnosisPatientAppointmentMapperDto();
				diagnosisPatientAppointmentMapperDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				diagnosisPatientAppointmentMapperDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				Response diagnosisPatientList = iPatientDiagnosisDao.getPatientDiagnosisByPatientId(diagnosisPatientAppointmentMapperDto);
				if(diagnosisPatientList.getListObject() != null && diagnosisPatientList.getListObject().size() > 0){
					List<DiagnosisPatientAppointmentMapperDto> list=diagnosisPatientList.getListObject();
					iPatientDiagnosisDao.updateCovTempDiagnosisPatientList(list, coversheetTemplateMasterDto);
				}
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		logger.error("updateCoversheetAsTemaplate failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveTemplateListDataForNewEMR(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try {
			Response res=null;
			CoversheetTemplateMasterDto resDto=null;
				Response resList= iCoversheetTemaplateDao.checkAvailableCoversheetAsTemaplate(coversheetTemplateMasterDto);
				if (resList.getListObject().size() == 0 ) {

					res = iCoversheetTemaplateDao.saveCoversheetAsTemaplate(coversheetTemplateMasterDto);
					resDto=(CoversheetTemplateMasterDto) res.getObject();
				}else {
					List<CoversheetTemplateMasterDto> list=resList.getListObject();
					for (CoversheetTemplateMasterDto dsdsb :list) {
						CoversheetTemplateMasterDto dsdsbss =new CoversheetTemplateMasterDto();
						dsdsbss.setCovTemplateId(dsdsb.getCovTemplateId());
						resDto=dsdsbss;
					}
					
				}
			
			if(resDto.getCovTemplateId() != null || res.getObject()!=null) {
				
				//1 For complaints
				if(coversheetTemplateMasterDto.getListComplaintData() != null && coversheetTemplateMasterDto.getListComplaintData().size() > 0) {
					iComplaintAppointmentMapperDao.savefComplaintAppointmentMapper(coversheetTemplateMasterDto.getListComplaintData(), coversheetTemplateMasterDto);
	
				}
					
				//15 for Diagnosis
				if(coversheetTemplateMasterDto.getListDiagnosisData() != null && coversheetTemplateMasterDto.getListDiagnosisData().size() > 0) {
					iPatientDiagnosisDao.updateDiagnosisPatientList(coversheetTemplateMasterDto.getListDiagnosisData(), coversheetTemplateMasterDto);
					Response resdig=iPatientDiagnosisDao.saveDiagnosisPatientList(coversheetTemplateMasterDto.getListDiagnosisData(), coversheetTemplateMasterDto);
				}
					
//				for Investigation
				if(coversheetTemplateMasterDto.getListLabOrderData() != null && coversheetTemplateMasterDto.getListLabOrderData().size() > 0){
					iOrderDetailsMasterDao.updateOrderDetailsList(coversheetTemplateMasterDto);
					
					List<Integer> orderIdLs = new ArrayList<>();
					List<InvestigationOrderMasterNewDto> list=coversheetTemplateMasterDto.getListLabOrderData();
					OrderMasterDto orderMasterDto=new OrderMasterDto();
					for (InvestigationOrderMasterNewDto ordDto : coversheetTemplateMasterDto.getListLabOrderData()) {
						if(ordDto.getServiceMasterId() != null) {
						orderMasterDto.setOrgId(coversheetTemplateMasterDto.getOrganizationId());
						orderMasterDto.setOrgUnitId(coversheetTemplateMasterDto.getUnitId());
						orderMasterDto.setOrderNo(ordDto.getOrderNo());
						orderMasterDto.setVisitTypeId(ordDto.getVisitTypeId());
						orderMasterDto.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
						orderMasterDto.setPriorityId(ordDto.getPriorityId());
						orderMasterDto.setOrdRemarks(ordDto.getOrdRemarks());
						orderMasterDto.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						orderMasterDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						orderMasterDto.setOrderStatus('A');
						orderMasterDto.setOrderStatusId(ordDto.getOrderStatusId());
						orderMasterDto.setIsPharmacy('N');
						orderMasterDto.setOrdDocSplId(ordDto.getSpecialityId());
						orderMasterDto.setHomeCollection(false);
						Response resp=iOrderMasterDao.saveOrderMaster(orderMasterDto);
						
						Integer orderId = (Integer) resp.getObject();

						if(orderId > 0 && coversheetTemplateMasterDto.getListLabOrderData() != null) 
						{
							OrderDetailsMasterDto ordDtlDto = new OrderDetailsMasterDto();
								ordDtlDto.setOrgId(coversheetTemplateMasterDto.getOrganizationId());
								ordDtlDto.setOrgUnitId(coversheetTemplateMasterDto.getUnitId());
								ordDtlDto.setOrderId(orderId);
								ordDtlDto.setServiceId(ordDto.getServiceMasterId());
								ordDtlDto.setPriorityId(ordDto.getPriorityId());
								ordDtlDto.setIsOutsourced('N');
								ordDtlDto.setCovTemplateId(coversheetTemplateMasterDto.getCovTemplateId() != null ? coversheetTemplateMasterDto.getCovTemplateId() : resDto.getCovTemplateId());
								ordDtlDto.setServiceAmt(ordDto.getServiceAmt());
								ordDtlDto.setOrdDocSplId(ordDto.getOrdDocSplId());
								ordDtlDto.setSubDepartmentId(ordDto.getSubDeptId());
								ordDtlDto.setServiceRendered(ordDto.getServiceRendered());
								ordDtlDto.setStatus('A');
								ordDtlDto.setIsOrderVisibleDepartment('A');
								ordDtlDto.setIsAppointableRadiology('N');
								ordDtlDto.setOrdRemarks(ordDto.getOrdRemarks());
								ordDtlDto.setOrdCancelled('N');
								ordDtlDto.setOrderQty(1);
								iOrderDetailsMasterDao.saveOrderDetailsMasterList(ordDtlDto);//								Integer orderDetailsIdForAccession = (Integer) orderDetails.getObject();
								
//							}
						}
					}else {
						if(ordDto.getServiceText() != null) {
								iOrderDetailsMasterDao.updateFreeTextInvestigations(coversheetTemplateMasterDto);
								
								FreeTextOrderedServicesDto freeTextOrderedServices=new FreeTextOrderedServicesDto();
								freeTextOrderedServices.setServiceText(ordDto.getServiceText() != null ? ordDto.getServiceText() : "");
								freeTextOrderedServices.setIsConsumed(false);
								freeTextOrderedServices.setCovTemplateId(coversheetTemplateMasterDto.getCovTemplateId() != null ? coversheetTemplateMasterDto.getCovTemplateId() : resDto.getCovTemplateId());
								freeTextOrderedServices.setDoctorId(ordDto.getDoctorId());
//								freeTextOrderedServices.setAdmissionId(ordDto.getAdmissionId() != null ? ordDto.getAdmissionId() : null);
								freeTextOrderedServices.setCreatedBy(ordDto.getCreatedBy());
//								freeTextOrderedServices.setPatientId(ordDto.getPatientId());
//								freeTextOrderedServices.setEncounterId(ordDto.getEncounterId() != null ? ordDto.getEncounterId() : null);
								freeTextOrderedServices.setOrderDetailsId(ordDto.getOrderDetailsId());
								freeTextOrderedServices.setOrderId(ordDto.getOrderId());
								freeTextOrderedServices.setUnitId(ordDto.getUnitId());
								freeTextOrderedServices.setOrganizationId(ordDto.getOrganizationId());
								freeTextOrderedServices.setStatus('A');
								freeTextOrderedServices.setUpdatedBy(ordDto.getUpdatedBy());
								iOrderDetailsMasterDao.saveFreeTextInvestigations(freeTextOrderedServices);
							
						}
						

					}
					}
			
				}
				
				//11 for prescription 
				PrescriptionDto prescriptionDtoSave=new PrescriptionDto();
				
				if(coversheetTemplateMasterDto.getListPrescriptionData() != null && coversheetTemplateMasterDto.getListPrescriptionData().size() > 0){
					iMedicationDAO.updatePrescriptionDetailList(coversheetTemplateMasterDto);

					List<MedicationDTO> list=coversheetTemplateMasterDto.getListPrescriptionData();
					for (MedicationDTO medicationDTO : list) {
					 
						prescriptionDtoSave.setIsIntervention(FALSE);
						prescriptionDtoSave.setIsActive(TRUE);
						prescriptionDtoSave.setIsDeleted(FALSE);
						prescriptionDtoSave.setIsValidate(FALSE);
						prescriptionDtoSave.setIsDischarge(FALSE);
						prescriptionDtoSave.setReconciliationDone(FALSE);
						prescriptionDtoSave.setIsNonFormulary('N');
						prescriptionDtoSave.setJustificationStop(medicationDTO.getJustificationStop());
						prescriptionDtoSave.setIsMedicationStop('N');
						prescriptionDtoSave.setVisitTypeId(medicationDTO.getVisitTypeId()!= null ? medicationDTO.getVisitTypeId():coversheetTemplateMasterDto.getVisitTypeId());
						prescriptionDtoSave.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
						prescriptionDtoSave.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
						prescriptionDtoSave.setAuthorisedBy(coversheetTemplateMasterDto.getCreatedBy());
						prescriptionDtoSave.setAuthorisedDate(coversheetTemplateMasterDto.getCreatedDate());
						prescriptionDtoSave.setPriorityId(2);
						prescriptionDtoSave.setStatusId(1);
						prescriptionDtoSave.setPrescriptionDate(coversheetTemplateMasterDto.getCreatedDate());
						prescriptionDtoSave.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
						prescriptionDtoSave.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						prescriptionDtoSave.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						prescriptionDtoSave.setUnitId(coversheetTemplateMasterDto.getUnitId());
						prescriptionDtoSave.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
						prescriptionDtoSave.setMedicationType(1);
						prescriptionDtoSave.setAdmissionId(null);
						prescriptionDtoSave.setEncounterId(null);
						prescriptionDtoSave.setPatientId(null);
//						prescriptionDto.setPrescriptionNo(getPrescriptionNumber());	
						prescriptionDtoSave.setIsMedicationStop('N');	
//						prescriptionDto.setJustification_stop(prescriptionDto.getJustification_stop());
						String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0, coversheetTemplateMasterDto.getUnitId(), PRES_NO);
						prescriptionDtoSave.setIsNonFormulary('N');
						
						prescriptionDtoSave.setPrescriptionNo(prescriptionNo);
						Response presRes = iMedicationDAO.savePrescription(prescriptionDtoSave);
						prescriptionDtoSave = (PrescriptionDto) presRes.getObject();
					}
					List<PrescriptionDetailsDto> listDetails=new ArrayList<>();
					for (MedicationDTO medicationDTO : list) {
						PrescriptionDetailsDto details=new PrescriptionDetailsDto();
						details.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						details.setUnitId(coversheetTemplateMasterDto.getUnitId());
						details.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
						details.setCovTemplateId(coversheetTemplateMasterDto.getCovTemplateId() != null ? coversheetTemplateMasterDto.getCovTemplateId() : resDto.getCovTemplateId());
						details.setIsDiscontinued(FALSE);
						details.setIsMedicationStop('N');
						details.setIsPreStop(FALSE);
						details.setIsIntervention(FALSE);
						details.setRouteId(medicationDTO.getRoaId());
						details.setStrength(medicationDTO.getStrength()!= null ? medicationDTO.getStrength() :medicationDTO.getDose());
						details.setDoseUnitId(medicationDTO.getDoseUnitId());
						details.setPrescribedDoseUnitId(medicationDTO.getPrescribedDoseUnitId());
						details.setDuration(medicationDTO.getDurationCount()!=null ? medicationDTO.getDurationCount() :null);
						details.setDurationUnit(medicationDTO.getDurationUnit()!=null ? medicationDTO.getDurationUnit() :null);
						details.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
						details.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
						details.setOrderQty(medicationDTO.getOrderQty());
						details.setPriorityId(medicationDTO.getPriorityId());
						details.setIsDeleted(FALSE);
						details.setIsActive(TRUE);
						details.setStatusId(1);
						details.setPrnflag(FALSE);
						details.setItemId(medicationDTO.getItemId()!=null ? medicationDTO.getItemId():null);
						details.setPrescribedDose(medicationDTO.getPrescribeDose() !=null ? medicationDTO.getPrescribeDose(): null);
						details.setStatflag(medicationDTO.getIsStat());
						details.setGenericName(medicationDTO.getGenericName());
						details.setItemName(medicationDTO.getItemName());
						details.setIsNonFormulary(medicationDTO.getIsNonFormulary() != null ? medicationDTO.getIsNonFormulary() :medicationDTO.getNonFormularyAdvice());
						details.setInfusionflag(FALSE);
						details.setUpdatedBy(prescriptionDtoSave.getAddedBy());
						details.setInstructionId(medicationDTO.getInstructionId());
						details.setInstructionDesc(medicationDTO.getInstructionText()!=null ? medicationDTO.getInstructionText() :"");
						details.setFrequencyId(medicationDTO.getFrequencyId());
						details.setStartDate(GlobalCommonDateUtils.getAvalDate(medicationDTO.getStartDateNew(), "yyyy-M-dd HH:mm:ss"));
						details.setStopDate(GlobalCommonDateUtils.getAvalDate(medicationDTO.getEndDateNew(), "yyyy-M-dd HH:mm:ss"));
						details.setUpdatedDate(medicationDTO.getAddedDate());
						if(medicationDTO.getIsDischarge() != null && medicationDTO.getIsDischarge() == true){
							details.setIsDischarge(medicationDTO.getIsDischarge());
						}else{
							details.setIsDischarge(false);
						}
						listDetails.add(details);
					}
					
					prescriptionDtoSave.setPrescriptionDetailsDtosList(listDetails);
					ListIterator<PrescriptionDetailsDto> iterator = prescriptionDtoSave.getPrescriptionDetailsDtosList().listIterator();
					while(iterator.hasNext())
					{
						PrescriptionDetailsDto prescriptionDetailsDto = iterator.next();
							prescriptionDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
							prescriptionDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
							prescriptionDetailsDto.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
							prescriptionDetailsDto.setCovTemplateId(coversheetTemplateMasterDto.getCovTemplateId() != null ? coversheetTemplateMasterDto.getCovTemplateId() : resDto.getCovTemplateId());
							prescriptionDetailsDto.setIsDiscontinued(FALSE);
							prescriptionDetailsDto.setIsMedicationStop('N');
							prescriptionDetailsDto.setIsPreStop(FALSE);
							prescriptionDetailsDto.setIsIntervention(FALSE);
							prescriptionDetailsDto.setDoseUnitId(prescriptionDetailsDto.getDoseUnitId());
							prescriptionDetailsDto.setDuration(prescriptionDetailsDto.getDuration()!=null ? prescriptionDetailsDto.getDuration() :null);
							prescriptionDetailsDto.setDurationUnit(prescriptionDetailsDto.getDurationUnit()!=null ? prescriptionDetailsDto.getDurationUnit() :null);
							prescriptionDetailsDto.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
							prescriptionDetailsDto.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
							prescriptionDetailsDto.setOrderQty(prescriptionDetailsDto.getOrderQty());
							prescriptionDetailsDto.setPriorityId(prescriptionDetailsDto.getPriorityId());
							prescriptionDetailsDto.setRouteId(prescriptionDetailsDto.getRoaId()!= null ? prescriptionDetailsDto.getRoaId() :prescriptionDetailsDto.getRouteId());
							prescriptionDetailsDto.setStrength(prescriptionDetailsDto.getStrength());
							prescriptionDetailsDto.setPrescribedDoseUnitId(prescriptionDetailsDto.getPrescribedDoseUnitId());
							prescriptionDetailsDto.setIsDeleted(FALSE);
							prescriptionDetailsDto.setIsActive(TRUE);
							prescriptionDetailsDto.setStatusId(1);
							prescriptionDetailsDto.setPrnflag(FALSE);
							prescriptionDetailsDto.setItemId(prescriptionDetailsDto.getItemId()!=null ? prescriptionDetailsDto.getItemId():null);
							prescriptionDetailsDto.setPrescribedDose(prescriptionDetailsDto.getPrescribedDose() !=null ? prescriptionDetailsDto.getPrescribedDose(): null);
							prescriptionDetailsDto.setStatflag(prescriptionDetailsDto.getStatflag());
							prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
							prescriptionDetailsDto.setFrequencyId(prescriptionDetailsDto.getFrequencyId());
							prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
							prescriptionDetailsDto.setIsNonFormulary(prescriptionDetailsDto.getIsNonFormulary());
							prescriptionDetailsDto.setInfusionflag(FALSE);
							prescriptionDetailsDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
							prescriptionDetailsDto.setInstructionId(prescriptionDetailsDto.getInstructionId());
							prescriptionDetailsDto.setInstructionDesc(prescriptionDetailsDto.getInstructionDesc());
							prescriptionDetailsDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
							if(prescriptionDetailsDto.getIsDischarge()!=null){
								prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
							}else{
								prescriptionDetailsDto.setIsDischarge(false);
							}
						Response resPreDtls = iMedicationDAO.savePrescriptionDetais(prescriptionDetailsDto);	
							
						prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls.getObject();
								
						iMedicationDAO.savePrescriptionFrequencyDetails(prescriptionDetailsDto);
					}
				
//				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, prescriptionDtoSave);
				}
				
				
				
				//15 for Advice
				if(coversheetTemplateMasterDto.getListAdviceData()!= null && coversheetTemplateMasterDto.getListAdviceData().size() > 0){
					iCoversheetInstructionDao.updateCoversheetInstructionList(coversheetTemplateMasterDto);

					List<CoversheetInstructionDto> list=coversheetTemplateMasterDto.getListAdviceData();
					CoversheetInstructionDto covInstructionDto=new CoversheetInstructionDto();
					for (CoversheetInstructionDto dto : list) {
						covInstructionDto.setAdvicedInstruction(dto.getAdvicedInstruction()!= null ? dto.getAdvicedInstruction() :"");
						covInstructionDto.setCovTemplateId(coversheetTemplateMasterDto.getCovTemplateId() != null ? coversheetTemplateMasterDto.getCovTemplateId() : resDto.getCovTemplateId());
						if(dto.getInstructionDetailsId() != null ) {
							covInstructionDto.setInstructionDetailsId(dto.getInstructionDetailsId());
						}		
						covInstructionDto.setActivityOrIntrcs(dto.getActivityOrIntrcs());
						covInstructionDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						covInstructionDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
						covInstructionDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						covInstructionDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
						covInstructionDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
						covInstructionDto.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						covInstructionDto.setStatus('A');
						iCoversheetInstructionDao.saveCoversheetInstruction(covInstructionDto);
					}
					
				}
				
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
		logger.error("saveTemplateListDataForNewEMR failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getTemplateDataByCovTempId(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		return iCoversheetTemaplateDao.getTemplateDataByCovTempId(coversheetTemplateMasterDto);
	
	}

	@Override
	public Response saveCovTemplateDataForPatient(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try {
			Response res= iCoversheetTemaplateDao.getTemplateDataByCovTempId(coversheetTemplateMasterDto);

			List<CoversheetTemplateMasterDto> mainList = new ArrayList<>();
			DiagnosisPatientAppointmentMapperDto diagnoDto=new DiagnosisPatientAppointmentMapperDto();
		if (res.getObject() != null) {
			CoversheetTemplateMasterDto resDto = (CoversheetTemplateMasterDto) res.getObject();
			if (resDto.getListDiagnosisData() != null
					&& resDto.getListDiagnosisData().size() > 0) {
				
				List<DiagnosisPatientAppointmentDetailsDto> listDiagnosisPatientAppointmentDetails = new ArrayList<>();
				
				diagnoDto.setCreated_by(coversheetTemplateMasterDto.getCreatedBy());
				diagnoDto.setCreated_date(coversheetTemplateMasterDto.getCreatedDate());
				diagnoDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
				diagnoDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				diagnoDto.setStatus('A');
				diagnoDto.setUpdated_by(coversheetTemplateMasterDto.getUpdatedBy());
				diagnoDto.setUpdated_date(coversheetTemplateMasterDto.getUpdatedDate());
				diagnoDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				diagnoDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				
				DiagnosisPatientAppointmentDetailsDto diagDtl=new DiagnosisPatientAppointmentDetailsDto();
				
				for (DiagnosisPatientAppointmentMapperDto dtoD : resDto.getListDiagnosisData()) {
					diagDtl  = new DiagnosisPatientAppointmentDetailsDto();
					diagDtl.setDiagnosisId(dtoD.getDiagnosisId());
					diagDtl.setDiagnosisType(dtoD.getDiagnosisType());
					diagDtl.setDiagnosisName(dtoD.getDiagnosisName() != null ? dtoD.getDiagnosisName() :dtoD.getDiagnosisText());
					diagDtl.setRemark(dtoD.getRemark());
					diagDtl.setIsDiagnosisName('N');
					if(dtoD.getDiagnosisText() != null) {
						diagDtl.setIsDiagnosisName('Y');
						diagDtl.setDiagnosisNameText(dtoD.getDiagnosisText());
					}
					diagDtl.setIsDiagnosisCancel('N');
					diagDtl.setType(1);
					listDiagnosisPatientAppointmentDetails.add(diagDtl);
				}
				diagnoDto.setListDiagnosisPatientAppointmentDetailsDto(listDiagnosisPatientAppointmentDetails);
				iPatientDiagnosisService.savePatientDiagnosis(diagnoDto);
				
				
			}
			if (resDto.getListLabOrderData() != null
					&& resDto.getListLabOrderData().size() > 0) {

				List<Integer> orderIdLs = new ArrayList<>();
				List<InvestigationOrderMasterNewDto> list = resDto.getListLabOrderData();
				OrderMasterDto orderMasterDto = new OrderMasterDto();
				
				for (InvestigationOrderMasterNewDto orderMasterDto2 : resDto.getListLabOrderData()) {
					if (orderMasterDto2.getServiceMasterId() != null) {
						Integer selfPayeeId =null ;
						Integer defaultTariffId = null;
						Date date  = TimeZoneComponent.getDateByzone(orderMasterDto2.getOrgUnitId());
						Response orderRes = null;
						Character isPreBilling = null;
						Integer defaultDiscountOwnership = 0;
						UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
						unitApplicationConfigurationDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						unitApplicationConfigurationDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
					Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
					
					if(appConfigRes.getObject()!=null)
					{
						UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();
						
						if(unitApplicationConfigurationDtos.getDefaultSelfTariffId()==null)
							return new Response<>(ERROR, COMMON_ERROR_CODE, "Default tariff is not defined !!", null, null);
						else
							defaultTariffId= unitApplicationConfigurationDtos.getDefaultSelfTariffId();
							
						if(unitApplicationConfigurationDtos.getSelfPayeeId()==null)
							return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee Id is not defined !!", null, null);
						else
							selfPayeeId=unitApplicationConfigurationDtos.getSelfPayeeId();
						
						if(unitApplicationConfigurationDtos.getIsPreOrPostBilling()==null)
							return new Response<>(ERROR, COMMON_ERROR_CODE, "Pre billing post billing is not defined !!", null, null);
						else	
							isPreBilling = unitApplicationConfigurationDtos.getIsPreOrPostBilling();
							
						defaultDiscountOwnership= unitApplicationConfigurationDtos.getDefaultDiscountOwnership();
					}
						Date currentDate = TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId());
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
						OrderMasterDto orderMasterDtos = new OrderMasterDto();
						orderMasterDtos.setVisitTypeId(OP);
						orderMasterDtos.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
						orderMasterDtos = iContractConsumptionService.getPatientClassByVisit(orderMasterDtos);
						List orderDetailsList = new ArrayList();
						
						orderMasterDto.setOrgId(coversheetTemplateMasterDto.getOrganizationId());
						orderMasterDto.setOrgUnitId(coversheetTemplateMasterDto.getUnitId());
						orderMasterDto.setOrderNo(orderMasterDto2.getOrderNo());
						orderMasterDto.setVisitTypeId(OP);
						orderMasterDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
						orderMasterDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
						orderMasterDto.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
						orderMasterDto.setPriorityId(orderMasterDto2.getPriorityId());
						orderMasterDto.setOrdRemarks(orderMasterDto2.getOrdRemarks());
						orderMasterDto.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						orderMasterDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						orderMasterDto.setOrderStatus('A');
						orderMasterDto.setOrderStatusId(coversheetTemplateMasterDto.getOrderStatusId());
						orderMasterDto.setIsPharmacy('N');
						orderMasterDto.setOrdDocSplId(orderMasterDto2.getOrdDocSplId());
						orderMasterDto.setHomeCollection(false);
						
						
						
						orderMasterDto.setDefaultDiscountOwnership(defaultDiscountOwnership);
								Integer labOrderId = null;
										OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
											orderDetailsMasterDto.setServiceId(orderMasterDto2.getServiceMasterId());
											orderDetailsMasterDto.setServiceIsBilled(0);
											orderDetailsMasterDto.setOrderQty(1);
											orderDetailsMasterDto.setCreatedBy(orderMasterDto2.getCreatedBy());
											orderDetailsMasterDto.setUpdatedBy(orderMasterDto2.getUpdatedBy());
											orderDetailsMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime());
											orderDetailsMasterDto.setOrderSchDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime());
											orderDetailsMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime());
											orderDetailsMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime());
											orderDetailsMasterDto.setStatus('A');
											orderDetailsMasterDto.setOrgId(coversheetTemplateMasterDto.getOrganizationId());
											orderDetailsMasterDto.setOrgUnitId(coversheetTemplateMasterDto.getUnitId());
											orderDetailsMasterDto.setOrdDocId(coversheetTemplateMasterDto.getDoctorId());
											orderDetailsMasterDto.setOrdDocSplId(orderMasterDto2.getPackageTypeId()!=null && orderMasterDto2.getPackageTypeId()==PACKAGE1 && orderMasterDto2.getOrdDocSplId()!=null && orderMasterDto2.getOrdDocSplId() > 1 ? orderMasterDto2.getOrdDocSplId() : orderMasterDto2.getOrdDocSplId());
											orderDetailsMasterDto.setServiceRenderingDeptId(orderMasterDto2.getSubSpecialityId());
											orderDetailsMasterDto.setOrdCancelled('N');
											orderDetailsMasterDto.setPriorityId(orderMasterDto2.getServicePriorityId());
											orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
											orderDetailsMasterDto.setIsOutsourced('N');
											orderDetailsMasterDto.setIsInvestigationOrder('Y');
											orderDetailsMasterDto.setServiceRendered(0);
											orderDetailsMasterDto.setIsRefDoctorShare(orderMasterDto2.getIsRefDoctorShare());
											orderDetailsMasterDto.setDepartmentId(orderMasterDto2.getDepartmentId());
											orderDetailsMasterDto.setSubDepartmentId(orderMasterDto2.getSubSpecialityId());
											orderDetailsMasterDto.setOrderSetId(orderMasterDto2.getOrderSetId());
											orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
											orderDetailsMasterDto.setNursingServiceOrderFlag('N');
											orderDetailsMasterDto.setBedCategoryId(coversheetTemplateMasterDto.getBillingBedCategoryId());
											orderDetailsMasterDto.setProcedureOrderMstId((orderMasterDto2.getProcedureOrderMstId()!=null && orderMasterDto2.getProcedureOrderMstId()!=0)?orderMasterDto2.getProcedureOrderMstId():null);
											orderDetailsMasterDto.setBedId(orderMasterDto2.getBedId());
											orderDetailsMasterDto.setWardId(orderMasterDto2.getWardId());
											orderDetailsMasterDto.setRoomId(orderMasterDto2.getRoomId());
											orderDetailsMasterDto.setOrdRemarks(orderMasterDto2.getOrdRemarks());
										if(orderMasterDto2.isPackage())
										{
											    orderDetailsMasterDto.setServiceId(orderMasterDto2.getServiceMasterId());
											    orderDetailsMasterDto.setServiceAmt(orderMasterDto2.getServiceAmt());
											    orderDetailsMasterDto.setServiceAmt(orderMasterDto2.getServiceAmt());
												Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(orderMasterDto2.getServiceMasterId(),coversheetTemplateMasterDto.getUnitId());
												BigDecimal serviceAmt = BigDecimal.ZERO;
												BigDecimal netAmt= BigDecimal.ZERO;
												BigDecimal taxAmt= BigDecimal.ZERO;
												if(tax != null)
												{
													orderDetailsMasterDto.setTaxId(tax.getId());
													orderDetailsMasterDto.setTaxPer(new BigDecimal(tax.getTaxPercentage()));
													serviceAmt = orderMasterDto2.getServiceAmt()!=null?orderMasterDto2.getServiceAmt():new BigDecimal(0);
													taxAmt = serviceAmt.multiply((orderDetailsMasterDto.getTaxPer().multiply(new BigDecimal(0.01))));
													netAmt = serviceAmt.add(taxAmt);
												}else
												{
													orderDetailsMasterDto.setTaxPer(new BigDecimal(0.00));
													serviceAmt = orderMasterDto2.getServiceAmt();
													netAmt = serviceAmt;
												}
												orderDetailsMasterDto.setOrdTotalAmt(serviceAmt);
												orderDetailsMasterDto.setTaxAmount(taxAmt);
												orderDetailsMasterDto.setNetAmount(netAmt);
												orderDetailsMasterDto.setOrdConcession(orderMasterDto2.getOrdConcession());
												orderDetailsMasterDto.setOrdDiscount(orderMasterDto2.getOrdDiscount());
												orderDetailsMasterDto.setSelfPayable(orderMasterDto2.getSelfPayable());
												orderDetailsMasterDto.setCreditPayable(orderMasterDto2.getCreditPayable());
												orderDetailsMasterDto.setTariffId(defaultTariffId);
												orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
												orderDetailsMasterDto.setOrderEmergencyFlag('N');
												orderDetailsMasterDto.setServiceRendered(orderMasterDto2.getServiceRendered());
												orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
												orderDetailsMasterDto.setServiceChargeable(1);
												orderDetailsMasterDto.setServiceIsBilled(0);
												orderDetailsMasterDto.setPayeeId(selfPayeeId);
												orderDetailsMasterDto.setIsPackage(true);
												orderDetailsMasterDto.setPackage(true);
												orderDetailsMasterDto.setApportionedPrice(orderMasterDto2.getApportionedPrice());
												orderDetailsMasterDto.setPackageId(orderMasterDto2.getPackageId());
												orderDetailsMasterDto.setPackageTypeId(orderMasterDto2.getPackageTypeId());
												orderDetailsMasterDto.setIsPackageService(orderMasterDto2.getIsPackageService());
												orderDetailsMasterDto.setIsCapService(orderMasterDto2.getIsCapService());
												orderDetailsMasterDto.setPaymentEntitlementId(coversheetTemplateMasterDto.getPaymentEntitlementTypeId());
												orderDetailsMasterDto.setOrdCancelled('N');
												orderDetailsMasterDto.setIsDrug('N');
												orderDetailsMasterDto.setServiceRendered(0);
												orderDetailsMasterDto.setStatus('A');
												orderDetailsMasterDto.setIsInvestigationOrder('N');
												orderDetailsMasterDto.setCoPayPer(orderMasterDto2.getCoPayPer());
												orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(orderMasterDto2.getOrderDetailsPayeeMapperDtosList());
												orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(orderMasterDto2.getOrderDetailsPackageMapperDtosList());
										}
										else if(orderMasterDto2.getPackageTypeId()!=null && orderMasterDto2.getPackageTypeId().intValue()==PACKAGE1 && !orderMasterDto2.isPackage())
										{
											orderDetailsMasterDto.setOrderDetailsId(orderMasterDto2.getOrderDetailsId());
										    orderDetailsMasterDto.setServiceId(orderMasterDto2.getServiceMasterId());
										    orderDetailsMasterDto.setServiceAmt(orderMasterDto2.getServiceAmt());
											orderDetailsMasterDto.setTariffId(defaultTariffId);
											orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
											orderDetailsMasterDto.setOrderEmergencyFlag('N');
											orderDetailsMasterDto.setServiceRendered(orderMasterDto2.getServiceRendered());
											orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
											orderDetailsMasterDto.setServiceChargeable(1);
											orderDetailsMasterDto.setServiceIsBilled(0);
											orderDetailsMasterDto.setPayeeId(selfPayeeId);
											orderDetailsMasterDto.setIsPackage(false);
											orderDetailsMasterDto.setPackage(false);
											orderDetailsMasterDto.setApportionedPrice(orderMasterDto2.getApportionedPrice());
											orderDetailsMasterDto.setPackageId(orderMasterDto2.getPackageId());
											orderDetailsMasterDto.setPackageTypeId(orderMasterDto2.getPackageTypeId());
											orderDetailsMasterDto.setIsPackageService(orderMasterDto2.getIsPackageService());
											orderDetailsMasterDto.setIsCapService(orderMasterDto2.getIsCapService());
											orderDetailsMasterDto.setPaymentEntitlementId(coversheetTemplateMasterDto.getPaymentEntitlementTypeId());
											orderDetailsMasterDto.setOrdCancelled('N');
											orderDetailsMasterDto.setIsDrug('N');
											orderDetailsMasterDto.setServiceRendered(0);
											orderDetailsMasterDto.setStatus('A');
											orderDetailsMasterDto.setIsInvestigationOrder('N');
											orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
											List<OrderDetailsPayeeMapperDto> listNew = new LinkedList<>();
											OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
												detailsPayeeMapperDto.setPayeeId(orderDetailsMasterDto.getPayeeId());
												detailsPayeeMapperDto.setPayeeAmount(BigDecimal.ZERO);
												detailsPayeeMapperDto.setServiceId(orderDetailsMasterDto.getServiceId());
												listNew.add(detailsPayeeMapperDto);	
											orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(listNew);
											orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(orderMasterDto2.getOrderDetailsPackageMapperDtosList());
											
										  }
										else{
											
											  ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
												consumptionDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
												consumptionDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
												consumptionDto.setVisitTypeId(OP);
												consumptionDto.setPatientTypeId(coversheetTemplateMasterDto.getPatientTypeId());
												consumptionDto.setBillingBedCategoryId(coversheetTemplateMasterDto.getBillingBedCategoryId());
												consumptionDto.setPaymentEntitlementId(coversheetTemplateMasterDto.getPaymentEntitlementTypeId());
												consumptionDto.setServiceId(orderMasterDto2.getServiceMasterId());
												consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
												consumptionDto.setContractId(orderMasterDto2.getContractId());
												consumptionDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
												consumptionDto.setAdmissionId(coversheetTemplateMasterDto.getAdmissionId());
												consumptionDto.setDefaultSelfTariffId(defaultTariffId);
												consumptionDto.setSelfPayeeId(selfPayeeId);
												consumptionDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
												consumptionDto.setPatientClassId(orderMasterDtos.getPatientClassId());
											Response reswefrd =iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
												consumptionDto=(ContractConsumptionDto)reswefrd.getObject();
											
											if(reswefrd.getObject()!=null)
											{
												orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
												orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
												orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
												orderDetailsMasterDto.setTariffId(defaultTariffId);
												orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
												orderDetailsMasterDto.setOrderEmergencyFlag('N');
												orderDetailsMasterDto.setServiceRendered(0);
												orderDetailsMasterDto.setServiceChargeable(1);
												orderDetailsMasterDto.setServiceIsBilled(0);
												orderDetailsMasterDto.setIsDrug('N');
												orderDetailsMasterDto.setPackage(orderMasterDto2.isPackage());
												orderDetailsMasterDto.setPackageId(orderMasterDto2.getPackageId());
												orderDetailsMasterDto.setIsInvestigationOrder('Y');
												orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
												orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
												orderDetailsMasterDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));
												orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
												orderDetailsMasterDto.setOrdTotalAmt(BigDecimal.valueOf(consumptionDto.getServiceRate()));
												orderDetailsMasterDto.setTaxAmount(BigDecimal.valueOf(consumptionDto.getTaxAmt()));
												orderDetailsMasterDto.setNetAmount(BigDecimal.valueOf(consumptionDto.getNetAmount()));
												orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()));
												orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
												orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
												orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
												orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
												orderDetailsMasterDto.setTaxId(consumptionDto.getTaxId());
												orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
												orderDetailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
												orderDetailsMasterDto.setBedCategoryId(coversheetTemplateMasterDto.getBillingBedCategoryId());
												if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
													orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
													
											}
										 }
									
									
											 orderDetailsList.add(orderDetailsMasterDto);
										
										
										if(orderMasterDto2.getOrdDocSplId() == null) {
											orderMasterDto.setOrdDocSplId(orderMasterDto2.getPackageTypeId()!=null && orderMasterDto2.getPackageTypeId()==PACKAGE1 && orderMasterDto2.getOrdDocSplId()!=null && orderMasterDto2.getOrdDocSplId() > 1 ? orderMasterDto2.getOrdDocSplId() : orderMasterDto2.getOrdDocSplId());
										}
										orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
										   
										 orderRes = iOrderMasterService.saveOrderMaster(orderMasterDto);
										 labOrderId = (Integer) orderRes.getListObject().getFirst();
								
					
						
						/*orderMasterDto.setOrgId(coversheetTemplateMasterDto.getOrganizationId());
						orderMasterDto.setOrgUnitId(coversheetTemplateMasterDto.getUnitId());
						orderMasterDto.setOrderNo(ordDto.getOrderNo());
						orderMasterDto.setVisitTypeId(ordDto.getVisitTypeId());
						orderMasterDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
						orderMasterDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
						orderMasterDto.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
						orderMasterDto.setPriorityId(ordDto.getPriorityId());
						orderMasterDto.setOrdRemarks(ordDto.getOrdRemarks());
						orderMasterDto.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						orderMasterDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						orderMasterDto.setOrderStatus('A');
						orderMasterDto.setOrderStatusId(ordDto.getOrderStatusId());
						orderMasterDto.setIsPharmacy('N');
						orderMasterDto.setOrdDocSplId(ordDto.getSpecialityId());
						orderMasterDto.setHomeCollection(false);
						Response resp = iOrderMasterDao.saveOrderMaster(orderMasterDto);

						Integer orderId = (Integer) resp.getObject();

						if (orderId > 0 && coversheetTemplateMasterDto.getListLabOrderData() != null) {
							OrderDetailsMasterDto ordDtlDto = new OrderDetailsMasterDto();
							ordDtlDto.setOrgId(coversheetTemplateMasterDto.getOrganizationId());
							ordDtlDto.setOrgUnitId(coversheetTemplateMasterDto.getUnitId());
							ordDtlDto.setOrderId(orderId);
							ordDtlDto.setServiceId(ordDto.getServiceMasterId());
							ordDtlDto.setPriorityId(ordDto.getPriorityId());
							ordDtlDto.setIsOutsourced('N');
							ordDtlDto.setOrderQty(1);
							ordDtlDto.setOrdCancelled('N');
							ordDtlDto.setServiceAmt(ordDto.getServiceAmt());
							ordDtlDto.setOrdDocSplId(ordDto.getOrdDocSplId());
							ordDtlDto.setSubDepartmentId(ordDto.getSubDeptId());
							ordDtlDto.setServiceRendered(ordDto.getServiceRendered());
							ordDtlDto.setStatus('A');
							ordDtlDto.setIsOrderVisibleDepartment('A');
							ordDtlDto.setIsAppointableRadiology('N');
							ordDtlDto.setOrdRemarks(ordDto.getOrdRemarks());
							ordDtlDto.setOrdCancelled('N');
							ordDtlDto.setOrderQty(1);
							iOrderDetailsMasterDao.saveOrderDetailsMasterList(ordDtlDto);
						}*/
						
						
						
						
					} else {
						if (orderMasterDto2.getServiceText() != null) {

							FreeTextOrderedServicesDto freeTextOrderedServices = new FreeTextOrderedServicesDto();
							freeTextOrderedServices
									.setServiceText(orderMasterDto2.getServiceText() != null ? orderMasterDto2.getServiceText() : "");
							freeTextOrderedServices.setIsConsumed(false);
							freeTextOrderedServices.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
							freeTextOrderedServices.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
							freeTextOrderedServices.setPatientId(coversheetTemplateMasterDto.getPatientId());
//							freeTextOrderedServices.setAdmissionId(ordDto.getAdmissionId() != null ? ordDto.getAdmissionId() : null);
							freeTextOrderedServices.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
							freeTextOrderedServices.setOrderDetailsId(orderMasterDto2.getOrderDetailsId());
							freeTextOrderedServices.setOrderId(orderMasterDto2.getOrderId());
							freeTextOrderedServices.setUnitId(coversheetTemplateMasterDto.getUnitId());
							freeTextOrderedServices.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
							freeTextOrderedServices.setStatus('A');
							freeTextOrderedServices.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
							iOrderDetailsMasterDao.saveFreeTextInvestigations(freeTextOrderedServices);

						}

					}
				}
				
			}
			if (resDto.getListPrescriptionData() != null
					&& resDto.getListPrescriptionData().size() > 0) {

				PrescriptionDto prescriptionDtoSave = new PrescriptionDto();
				MedicationDTO medicationDTO = resDto.getListPrescriptionData().getFirst();
//				for (MedicationDTO medicationDTO : list) {

					prescriptionDtoSave.setIsIntervention(FALSE);
					prescriptionDtoSave.setIsActive(TRUE);
					prescriptionDtoSave.setIsDeleted(FALSE);
					prescriptionDtoSave.setIsValidate(FALSE);
					prescriptionDtoSave.setIsDischarge(FALSE);
					prescriptionDtoSave.setReconciliationDone(FALSE);
					prescriptionDtoSave.setIsNonFormulary('N');
					prescriptionDtoSave.setJustificationStop(medicationDTO.getJustificationStop());
					prescriptionDtoSave.setIsMedicationStop('N');
					prescriptionDtoSave.setVisitTypeId(medicationDTO.getVisitTypeId());
					prescriptionDtoSave.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
					prescriptionDtoSave.setAuthorisedBy(coversheetTemplateMasterDto.getCreatedBy());
					prescriptionDtoSave.setAuthorisedDate(coversheetTemplateMasterDto.getCreatedDate());
					prescriptionDtoSave.setPriorityId(2);
					prescriptionDtoSave.setStatusId(1);
					prescriptionDtoSave.setPrescriptionDate(coversheetTemplateMasterDto.getCreatedDate());
					prescriptionDtoSave.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
					prescriptionDtoSave.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
					prescriptionDtoSave.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					prescriptionDtoSave.setUnitId(coversheetTemplateMasterDto.getUnitId());
					prescriptionDtoSave.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
					prescriptionDtoSave.setMedicationType(1);
					prescriptionDtoSave.setAdmissionId(null);
					prescriptionDtoSave.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
					prescriptionDtoSave.setPatientId(coversheetTemplateMasterDto.getPatientId());
//					prescriptionDto.setPrescriptionNo(getPrescriptionNumber());	
					prescriptionDtoSave.setIsMedicationStop('N');
//					prescriptionDto.setJustification_stop(prescriptionDto.getJustification_stop());
					String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0,
							coversheetTemplateMasterDto.getUnitId(), PRES_NO);
					prescriptionDtoSave.setIsNonFormulary('N');

					prescriptionDtoSave.setPrescriptionNo(prescriptionNo);
					Response presRes = iMedicationDAO.savePrescription(prescriptionDtoSave);
					prescriptionDtoSave = (PrescriptionDto) presRes.getObject();
//				}
					
	            List<MedicationDTO> listNew = resDto.getListPrescriptionData();
				List<PrescriptionDetailsDto> listDetails = new ArrayList<>();
				for (MedicationDTO medicationDTONew : listNew) {
					PrescriptionDetailsDto details = new PrescriptionDetailsDto();
					details.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					details.setUnitId(coversheetTemplateMasterDto.getUnitId());
					details.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
					details.setIsDiscontinued(FALSE);
					details.setIsMedicationStop('N');
					details.setIsPreStop(FALSE);
					details.setIsIntervention(FALSE);
					details.setDuration(
							medicationDTONew.getDurationCount() != null ? medicationDTONew.getDurationCount() : null);
					details.setDurationUnit(
							medicationDTONew.getDurationUnit() != null ? medicationDTONew.getDurationUnit() : null);
					details.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
					details.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
					details.setOrderQty(medicationDTONew.getOrderQty());
					details.setPriorityId(medicationDTONew.getPriorityId());
					details.setIsDeleted(FALSE);
					details.setIsActive(TRUE);
					details.setStatusId(1);
					details.setPrnflag(FALSE);
					details.setItemId(medicationDTONew.getItemId() != null ? medicationDTONew.getItemId() : null);
//					details.setPrescribedDose(medicationDTO.getPrescribedDose() !=null ? medicationDTO.getPrescribedDose(): null);
					details.setStatflag(medicationDTONew.getStatflag());
					details.setGenericName(medicationDTONew.getGenericName());
					details.setItemName(medicationDTONew.getItemName());
					details.setIsNonFormulary(medicationDTONew.getIsNonFormulary());
					details.setInfusionflag(FALSE);
					details.setUpdatedBy(prescriptionDtoSave.getAddedBy());
					details.setInstructionId(medicationDTONew.getInstructionId());
					details.setInstructionDesc(medicationDTONew.getInstructionText());
					details.setFrequencyId(medicationDTONew.getFrequencyId());
				//	details.setStartDate(
					//		GlobalCommonDateUtils.getAvalDate(medicationDTONew.getStartDateNew(), "yyyy-M-dd HH:mm:ss"));
					details.setStartDate(
							GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime(), "yyyy-M-dd HH:mm:ss"));
					Date startCals = GlobalCommonDateUtils.getDate(details.getStartDate(), "yyyy-M-dd HH:mm:ss");
					String durationh = null;
					 Date oneMinutesFromNow =null;
					if(medicationDTONew.getDurationUnit().equals("Day")) {
						 Calendar now = Calendar.getInstance();
						    now.add(Calendar.DAY_OF_MONTH, medicationDTONew.getDurationCount());
						     oneMinutesFromNow = now.getTime();
						
					}else if(medicationDTONew.getDurationUnit().equals("Month")) {
						 Calendar now = Calendar.getInstance();
						    now.add(Calendar.DAY_OF_MONTH, medicationDTONew.getDurationCount());
						     oneMinutesFromNow = now.getTime();
						
					}else if(medicationDTONew.getDurationUnit().equals("Year")) {
						 Calendar now = Calendar.getInstance();
						    now.add(Calendar.DAY_OF_YEAR, medicationDTONew.getDurationCount());
						     oneMinutesFromNow = now.getTime();
						
					}
			        
					
					
					details.setStopDate(GlobalCommonDateUtils.getStringDate(oneMinutesFromNow, "yyyy-M-dd HH:mm:ss"));
					if (details.getIsDischarge() != null) {
						details.setIsDischarge(medicationDTONew.getIsDischarge());
					} else {
						details.setIsDischarge(false);
					}
					listDetails.add(details);
					prescriptionDtoSave.setPrescriptionDetailsDtosList(listDetails);
					ListIterator<PrescriptionDetailsDto> iterator = prescriptionDtoSave.getPrescriptionDetailsDtosList().listIterator();
					while(iterator.hasNext())
					{
						PrescriptionDetailsDto prescriptionDetailsDto = iterator.next();
							prescriptionDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
							prescriptionDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
							prescriptionDetailsDto.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
							prescriptionDetailsDto.setIsDiscontinued(FALSE);
							prescriptionDetailsDto.setIsMedicationStop('N');
							prescriptionDetailsDto.setIsPreStop(FALSE);
							prescriptionDetailsDto.setIsIntervention(FALSE);
							prescriptionDetailsDto.setDuration(prescriptionDetailsDto.getDuration()!=null ? prescriptionDetailsDto.getDuration() :null);
							prescriptionDetailsDto.setDurationUnit(prescriptionDetailsDto.getDurationUnit()!=null ? prescriptionDetailsDto.getDurationUnit() :null);
							prescriptionDetailsDto.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
							prescriptionDetailsDto.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
							prescriptionDetailsDto.setOrderQty(prescriptionDetailsDto.getOrderQty());
							prescriptionDetailsDto.setPriorityId(prescriptionDetailsDto.getPriorityId());
							prescriptionDetailsDto.setIsDeleted(FALSE);
							prescriptionDetailsDto.setIsActive(TRUE);
							prescriptionDetailsDto.setStatusId(1);
							prescriptionDetailsDto.setPrnflag(FALSE);
							prescriptionDetailsDto.setItemId(prescriptionDetailsDto.getItemId()!=null ? prescriptionDetailsDto.getItemId():null);
							prescriptionDetailsDto.setPrescribedDose(prescriptionDetailsDto.getPrescribedDose() !=null ? prescriptionDetailsDto.getPrescribedDose(): null);
							prescriptionDetailsDto.setStatflag(prescriptionDetailsDto.getStatflag());
							prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
							prescriptionDetailsDto.setFrequencyId(prescriptionDetailsDto.getFrequencyId());
							prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
							prescriptionDetailsDto.setIsNonFormulary(prescriptionDetailsDto.getIsNonFormulary());
							prescriptionDetailsDto.setInfusionflag(FALSE);
							prescriptionDetailsDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
							prescriptionDetailsDto.setInstructionId(prescriptionDetailsDto.getInstructionId());
							prescriptionDetailsDto.setInstructionDesc(prescriptionDetailsDto.getInstructionDesc());
							prescriptionDetailsDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
							if(prescriptionDetailsDto.getIsDischarge()!=null){
								prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
							}else{
								prescriptionDetailsDto.setIsDischarge(false);
							}
						Response resPreDtls = iMedicationDAO.savePrescriptionDetais(prescriptionDetailsDto);	
							
						prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls.getObject();
								
						iMedicationDAO.savePrescriptionFrequencyDetails(prescriptionDetailsDto);
					}
				}
			
				if (resDto.getListAdviceData() != null
						&& resDto.getListAdviceData().size() > 0) {

					List<CoversheetInstructionDto> list1 = resDto.getListAdviceData();
					CoversheetInstructionDto covInstructionDto = new CoversheetInstructionDto();
					for (CoversheetInstructionDto dto : list1) {
						covInstructionDto.setAdvicedInstruction(
								dto.getAdvicedInstruction() != null ? dto.getAdvicedInstruction() : "");
						if (dto.getInstructionDetailsId() != null) {
							covInstructionDto.setInstructionDetailsId(dto.getInstructionDetailsId());
						}
						covInstructionDto.setActivityOrIntrcs(dto.getActivityOrIntrcs());
						covInstructionDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						covInstructionDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
						covInstructionDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						covInstructionDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
						covInstructionDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
						covInstructionDto.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						covInstructionDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
						covInstructionDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
						covInstructionDto.setStatus('A');
						iCoversheetInstructionDao.saveCoversheetInstruction(covInstructionDto);
					}

				}
				
				/*
				PrescriptionDto prescriptionDtoSave = new PrescriptionDto();
				List<MedicationDTO> list = resDto.getListPrescriptionData();
				for (MedicationDTO medicationDTO : list) {

					prescriptionDtoSave.setIsIntervention(FALSE);
					prescriptionDtoSave.setIsActive(TRUE);
					prescriptionDtoSave.setIsDeleted(FALSE);
					prescriptionDtoSave.setIsValidate(FALSE);
					prescriptionDtoSave.setIsDischarge(FALSE);
					prescriptionDtoSave.setReconciliationDone(FALSE);
					prescriptionDtoSave.setIsNonFormulary('N');
					prescriptionDtoSave.setJustificationStop(medicationDTO.getJustificationStop());
					prescriptionDtoSave.setIsMedicationStop('N');
					prescriptionDtoSave.setVisitTypeId(medicationDTO.getVisitTypeId());
					prescriptionDtoSave.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
					prescriptionDtoSave.setAuthorisedBy(coversheetTemplateMasterDto.getCreatedBy());
					prescriptionDtoSave.setAuthorisedDate(coversheetTemplateMasterDto.getCreatedDate());
					prescriptionDtoSave.setPriorityId(2);
					prescriptionDtoSave.setStatusId(1);
					prescriptionDtoSave.setPrescriptionDate(coversheetTemplateMasterDto.getCreatedDate());
					prescriptionDtoSave.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
					prescriptionDtoSave.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
					prescriptionDtoSave.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					prescriptionDtoSave.setUnitId(coversheetTemplateMasterDto.getUnitId());
					prescriptionDtoSave.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
					prescriptionDtoSave.setMedicationType(1);
					prescriptionDtoSave.setAdmissionId(null);
					prescriptionDtoSave.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
					prescriptionDtoSave.setPatientId(coversheetTemplateMasterDto.getPatientId());
//					prescriptionDto.setPrescriptionNo(getPrescriptionNumber());	
					prescriptionDtoSave.setIsMedicationStop('N');
//					prescriptionDto.setJustification_stop(prescriptionDto.getJustification_stop());
					String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0,
							coversheetTemplateMasterDto.getUnitId(), PRES_NO);
					prescriptionDtoSave.setIsNonFormulary('N');

					prescriptionDtoSave.setPrescriptionNo(prescriptionNo);
					Response presRes = iMedicationDAO.savePrescription(prescriptionDtoSave);
					prescriptionDtoSave = (PrescriptionDto) presRes.getObject();
				}
				List<PrescriptionDetailsDto> listDetails = new ArrayList<>();
				for (MedicationDTO medicationDTO : list) {
					PrescriptionDetailsDto details = new PrescriptionDetailsDto();
					details.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
					details.setUnitId(coversheetTemplateMasterDto.getUnitId());
					details.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
					details.setIsDiscontinued(FALSE);
					details.setIsMedicationStop('N');
					details.setIsPreStop(FALSE);
					details.setIsIntervention(FALSE);
					details.setDuration(
							medicationDTO.getDurationCount() != null ? medicationDTO.getDurationCount() : null);
					details.setDurationUnit(
							medicationDTO.getDurationUnit() != null ? medicationDTO.getDurationUnit() : null);
					details.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
					details.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
					details.setOrderQty(medicationDTO.getOrderQty());
					details.setPriorityId(medicationDTO.getPriorityId());
					details.setIsDeleted(FALSE);
					details.setIsActive(TRUE);
					details.setStatusId(1);
					details.setPrnflag(FALSE);
					details.setItemId(medicationDTO.getItemId() != null ? medicationDTO.getItemId() : null);
//					details.setPrescribedDose(medicationDTO.getPrescribedDose() !=null ? medicationDTO.getPrescribedDose(): null);
					details.setStatflag(medicationDTO.getStatflag());
					details.setGenericName(medicationDTO.getGenericName());
					details.setItemName(medicationDTO.getItemName());
					details.setIsNonFormulary(medicationDTO.getIsNonFormulary());
					details.setInfusionflag(FALSE);
					details.setUpdatedBy(prescriptionDtoSave.getAddedBy());
					details.setInstructionId(medicationDTO.getInstructionId());
					details.setInstructionDesc(medicationDTO.getInstructionText());
					details.setFrequencyId(medicationDTO.getFrequencyId());
					details.setStartDate(
							GlobalCommonDateUtils.getAvalDate(medicationDTO.getStartDateNew(), "yyyy-M-dd HH:mm:ss"));
					details.setStopDate(
							GlobalCommonDateUtils.getAvalDate(medicationDTO.getEndDateNew(), "yyyy-M-dd HH:mm:ss"));
					details.setUpdatedDate(medicationDTO.getAddedDate());
					if (details.getIsDischarge() != null) {
						details.setIsDischarge(medicationDTO.getIsDischarge());
					} else {
						details.setIsDischarge(false);
					}
					listDetails.add(details);
					prescriptionDtoSave.setPrescriptionDetailsDtosList(listDetails);
					ListIterator<PrescriptionDetailsDto> iterator = prescriptionDtoSave.getPrescriptionDetailsDtosList().listIterator();
					while(iterator.hasNext())
					{
						PrescriptionDetailsDto prescriptionDetailsDto = iterator.next();
							prescriptionDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
							prescriptionDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
							prescriptionDetailsDto.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
							prescriptionDetailsDto.setIsDiscontinued(FALSE);
							prescriptionDetailsDto.setIsMedicationStop('N');
							prescriptionDetailsDto.setIsPreStop(FALSE);
							prescriptionDetailsDto.setIsIntervention(FALSE);
							prescriptionDetailsDto.setDuration(prescriptionDetailsDto.getDuration()!=null ? prescriptionDetailsDto.getDuration() :null);
							prescriptionDetailsDto.setDurationUnit(prescriptionDetailsDto.getDurationUnit()!=null ? prescriptionDetailsDto.getDurationUnit() :null);
							prescriptionDetailsDto.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
							prescriptionDetailsDto.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
							prescriptionDetailsDto.setOrderQty(prescriptionDetailsDto.getOrderQty());
							prescriptionDetailsDto.setPriorityId(prescriptionDetailsDto.getPriorityId());
							prescriptionDetailsDto.setIsDeleted(FALSE);
							prescriptionDetailsDto.setIsActive(TRUE);
							prescriptionDetailsDto.setStatusId(1);
							prescriptionDetailsDto.setPrnflag(FALSE);
							prescriptionDetailsDto.setItemId(prescriptionDetailsDto.getItemId()!=null ? prescriptionDetailsDto.getItemId():null);
							prescriptionDetailsDto.setPrescribedDose(prescriptionDetailsDto.getPrescribedDose() !=null ? prescriptionDetailsDto.getPrescribedDose(): null);
							prescriptionDetailsDto.setStatflag(prescriptionDetailsDto.getStatflag());
							prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
							prescriptionDetailsDto.setFrequencyId(prescriptionDetailsDto.getFrequencyId());
							prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
							prescriptionDetailsDto.setIsNonFormulary(prescriptionDetailsDto.getIsNonFormulary());
							prescriptionDetailsDto.setInfusionflag(FALSE);
							prescriptionDetailsDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
							prescriptionDetailsDto.setInstructionId(prescriptionDetailsDto.getInstructionId());
							prescriptionDetailsDto.setInstructionDesc(prescriptionDetailsDto.getInstructionDesc());
							prescriptionDetailsDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
							if(prescriptionDetailsDto.getIsDischarge()!=null){
								prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
							}else{
								prescriptionDetailsDto.setIsDischarge(false);
							}
						Response resPreDtls = iMedicationDAO.savePrescriptionDetais(prescriptionDetailsDto);	
							
						prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls.getObject();
								
						iMedicationDAO.savePrescriptionFrequencyDetails(prescriptionDetailsDto);
					}
					
					Response resMed= iMedicationDAO.getMedicationDetailsByDoctorId(prescriptionDtoSave);
					List<MedicationDTO> listPres=resMed.getListObject();
					coversheetTemplateMasterDto.setListPrescriptionData(listPres);
				}
				if (resDto.getListAdviceData() != null
						&& resDto.getListAdviceData().size() > 0) {

					List<CoversheetInstructionDto> list1 = resDto.getListAdviceData();
					CoversheetInstructionDto covInstructionDto = new CoversheetInstructionDto();
					for (CoversheetInstructionDto dto : list1) {
						covInstructionDto.setAdvicedInstruction(
								dto.getAdvicedInstruction() != null ? dto.getAdvicedInstruction() : "");
						if (dto.getInstructionDetailsId() != null) {
							covInstructionDto.setInstructionDetailsId(dto.getInstructionDetailsId());
						}
						covInstructionDto.setActivityOrIntrcs(dto.getActivityOrIntrcs());
						covInstructionDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						covInstructionDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
						covInstructionDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						covInstructionDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
						covInstructionDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
						covInstructionDto.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						covInstructionDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
						covInstructionDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
						covInstructionDto.setStatus('A');
						iCoversheetInstructionDao.saveCoversheetInstruction(covInstructionDto);
					}

				}
			*/}
		}
		
		
	
		
		
		/*if(diagnoDto.getOrganizationId() != null) {
			Response resDigLst= iPatientDiagnosisDao.getPatientDiagnosisByPatientId(diagnoDto);
			List<DiagnosisPatientAppointmentMapperDto> listDig=resDigLst.getListObject();
			coversheetTemplateMasterDto.setListDiagnosisData(listDig);
		}
		
		Response resIns= iCoversheetInstructionDao.coversheetInstructionList(coversheetTemplateMasterDto.getEncounterId(),1);
		List<CoversheetInstructionDto> listIns=resIns.getListObject();
		coversheetTemplateMasterDto.setListAdviceData(listIns);
		
		Response resLabOrd= iOrderDetailsMasterDao.getInvestigationsListForNewEMR(coversheetTemplateMasterDto.getPatientId(),false,coversheetTemplateMasterDto.getUnitId());
		List<InvestigationOrderMasterNewDto> listLab=resLabOrd.getListObject();
		coversheetTemplateMasterDto.setListLabOrderData(listLab);*/
		
		return new Response<>(SUCCESS, SUCCESS_CODE, null, null, coversheetTemplateMasterDto);
		 } catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getCoversheetListData(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
			throws ApplicationException {
		try {
			return iCoversheetTemaplateDao.getCoversheetListData(coversheetTemplateMasterDto);
		} catch (Exception e) {
		logger.error("getCoversheetListData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response updateCovTemplateDataForPatient(CoversheetTemplateMasterDto coversheetTemplateMasterDto)
		throws ApplicationException {

			List<CoversheetTemplateMasterDto> mainList = new ArrayList<>();
			DiagnosisPatientAppointmentMapperDto diagnoDto=new DiagnosisPatientAppointmentMapperDto();
				if (coversheetTemplateMasterDto.getListDiagnosisData() != null
					&& coversheetTemplateMasterDto.getListDiagnosisData().size() > 0) {
				
				List<DiagnosisPatientAppointmentDetailsDto> listDiagnosisPatientAppointmentDetails = new ArrayList<>();
				
				diagnoDto.setCreated_by(coversheetTemplateMasterDto.getCreatedBy());
				diagnoDto.setCreated_date(coversheetTemplateMasterDto.getCreatedDate());
				diagnoDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
				diagnoDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
				diagnoDto.setStatus('A');
				diagnoDto.setUpdated_by(coversheetTemplateMasterDto.getUpdatedBy());
				diagnoDto.setUpdated_date(coversheetTemplateMasterDto.getUpdatedDate());
				diagnoDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
				diagnoDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
				
				DiagnosisPatientAppointmentDetailsDto diagDtl=new DiagnosisPatientAppointmentDetailsDto();
				
				for (DiagnosisPatientAppointmentMapperDto dtoD : coversheetTemplateMasterDto.getListDiagnosisData()) {
					diagDtl  = new DiagnosisPatientAppointmentDetailsDto();
					diagDtl.setDiagnosisId(dtoD.getDiagnosisId());
					diagDtl.setDiagnosisType(dtoD.getDiagnosisType());
					diagDtl.setDiagnosisName(dtoD.getDiagnosisName() != null ? dtoD.getDiagnosisName() :dtoD.getDiagnosisText());
					diagDtl.setRemark(dtoD.getRemark());
					diagDtl.setIsDiagnosisName('N');
					if(dtoD.getDiagnosisText() != null) {
						diagDtl.setIsDiagnosisName('Y');
						diagDtl.setDiagnosisNameText(dtoD.getDiagnosisText());
					}
					diagDtl.setIsDiagnosisCancel('N');
					diagDtl.setType(1);
					listDiagnosisPatientAppointmentDetails.add(diagDtl);
				}
				diagnoDto.setListDiagnosisPatientAppointmentDetailsDto(listDiagnosisPatientAppointmentDetails);
				iPatientDiagnosisService.savePatientDiagnosis(diagnoDto);
				
				
			}
			if (coversheetTemplateMasterDto.getListLabOrderData() != null
					&& coversheetTemplateMasterDto.getListLabOrderData().size() > 0) {

				List<Integer> orderIdLs = new ArrayList<>();
				List<InvestigationOrderMasterNewDto> list = coversheetTemplateMasterDto.getListLabOrderData();
				OrderMasterDto orderMasterDto = new OrderMasterDto();
				
				for (InvestigationOrderMasterNewDto orderMasterDto2 : coversheetTemplateMasterDto.getListLabOrderData()) {
					if (orderMasterDto2.getServiceMasterId() != null) {
						Integer selfPayeeId =null ;
						Integer defaultTariffId = null;
						Date date  = TimeZoneComponent.getDateByzone(orderMasterDto2.getOrgUnitId());
						Response orderRes = null;
						Character isPreBilling = null;
						Integer defaultDiscountOwnership = 0;
						UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
						unitApplicationConfigurationDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						unitApplicationConfigurationDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
					Response appConfigRes = iUnitApplicationBillingConfigurationDao.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
					
					if(appConfigRes.getObject()!=null)
					{
						UnitApplicationConfigurationDto unitApplicationConfigurationDtos = (UnitApplicationConfigurationDto)appConfigRes.getObject();
						
						if(unitApplicationConfigurationDtos.getDefaultSelfTariffId()==null)
							return new Response<>(ERROR, COMMON_ERROR_CODE, "Default tariff is not defined !!", null, null);
						else
							defaultTariffId= unitApplicationConfigurationDtos.getDefaultSelfTariffId();
							
						if(unitApplicationConfigurationDtos.getSelfPayeeId()==null)
							return new Response<>(ERROR, COMMON_ERROR_CODE, "Self Payee Id is not defined !!", null, null);
						else
							selfPayeeId=unitApplicationConfigurationDtos.getSelfPayeeId();
						
						if(unitApplicationConfigurationDtos.getIsPreOrPostBilling()==null)
							return new Response<>(ERROR, COMMON_ERROR_CODE, "Pre billing post billing is not defined !!", null, null);
						else	
							isPreBilling = unitApplicationConfigurationDtos.getIsPreOrPostBilling();
							
						defaultDiscountOwnership= unitApplicationConfigurationDtos.getDefaultDiscountOwnership();
					}
						Date currentDate = TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId());
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
						//String strCurrentDate = formatter.format(currentDate);
						OrderMasterDto orderMasterDtos = new OrderMasterDto();
						orderMasterDtos.setVisitTypeId(orderMasterDto2.getVisitTypeId());
						orderMasterDtos.setEncounterId(orderMasterDto2.getEncounterId());
						orderMasterDtos = iContractConsumptionService.getPatientClassByVisit(orderMasterDtos);
						List orderDetailsList = new ArrayList();
						
						orderMasterDto.setOrgId(coversheetTemplateMasterDto.getOrganizationId());
						orderMasterDto.setOrgUnitId(coversheetTemplateMasterDto.getUnitId());
						orderMasterDto.setOrderNo(orderMasterDto2.getOrderNo());
						orderMasterDto.setVisitTypeId(orderMasterDto2.getVisitTypeId());
						orderMasterDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
						orderMasterDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
						orderMasterDto.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
						orderMasterDto.setPriorityId(orderMasterDto2.getPriorityId());
						orderMasterDto.setOrdRemarks(orderMasterDto2.getOrdRemarks());
						orderMasterDto.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						orderMasterDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						orderMasterDto.setOrderStatus('A');
						orderMasterDto.setOrderStatusId(orderMasterDto2.getOrderStatusId());
						orderMasterDto.setIsPharmacy('N');
						orderMasterDto.setOrdDocSplId(orderMasterDto2.getOrdDocSplId());
						orderMasterDto.setHomeCollection(false);
						
						
						
						orderMasterDto.setDefaultDiscountOwnership(defaultDiscountOwnership);
								Integer labOrderId = null;
										OrderDetailsMasterDto orderDetailsMasterDto = new OrderDetailsMasterDto();
											orderDetailsMasterDto.setServiceId(orderMasterDto2.getServiceMasterId());
											orderDetailsMasterDto.setServiceIsBilled(0);
											orderDetailsMasterDto.setOrderQty(1);
											orderDetailsMasterDto.setCreatedBy(orderMasterDto2.getCreatedBy());
											orderDetailsMasterDto.setUpdatedBy(orderMasterDto2.getUpdatedBy());
											orderDetailsMasterDto.setOrderDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime());
											orderDetailsMasterDto.setOrderSchDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime());
											orderDetailsMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime());
											orderDetailsMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime());
											orderDetailsMasterDto.setStatus('A');
											orderDetailsMasterDto.setOrgId(coversheetTemplateMasterDto.getOrganizationId());
											orderDetailsMasterDto.setOrgUnitId(coversheetTemplateMasterDto.getUnitId());
											orderDetailsMasterDto.setOrdDocId(orderMasterDto2.getOrdDocId());
											orderDetailsMasterDto.setOrdDocSplId(orderMasterDto2.getPackageTypeId()!=null && orderMasterDto2.getPackageTypeId()==PACKAGE1 && orderMasterDto2.getOrdDocSplId()!=null && orderMasterDto2.getOrdDocSplId() > 1 ? orderMasterDto2.getOrdDocSplId() : orderMasterDto2.getOrdDocSplId());
											orderDetailsMasterDto.setServiceRenderingDeptId(orderMasterDto2.getServiceRenderingDeptId());
											orderDetailsMasterDto.setOrdCancelled('N');
											orderDetailsMasterDto.setPriorityId(orderMasterDto2.getServicePriorityId());
											orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
											orderDetailsMasterDto.setIsOutsourced('N');
											orderDetailsMasterDto.setIsInvestigationOrder('Y');
											orderDetailsMasterDto.setServiceRendered(0);
											orderDetailsMasterDto.setIsRefDoctorShare(orderMasterDto2.getIsRefDoctorShare());
											orderDetailsMasterDto.setDepartmentId(orderMasterDto2.getDepartmentId());
											orderDetailsMasterDto.setSubDepartmentId(orderMasterDto2.getSubDeptId());
											orderDetailsMasterDto.setOrderSetId(orderMasterDto2.getOrderSetId());
											orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
											orderDetailsMasterDto.setNursingServiceOrderFlag(orderMasterDto2.getNursingServiceOrderFlag());
											orderDetailsMasterDto.setBedCategoryId(orderMasterDto2.getBillingBedCategoryId());
											orderDetailsMasterDto.setProcedureOrderMstId((orderMasterDto2.getProcedureOrderMstId()!=null && orderMasterDto2.getProcedureOrderMstId()!=0)?orderMasterDto2.getProcedureOrderMstId():null);
											orderDetailsMasterDto.setBedId(orderMasterDto2.getBedId());
											orderDetailsMasterDto.setWardId(orderMasterDto2.getWardId());
											orderDetailsMasterDto.setRoomId(orderMasterDto2.getRoomId());
											orderDetailsMasterDto.setOrdRemarks(orderMasterDto2.getOrdRemarks());
										if(orderMasterDto2.isPackage())
										{
											    orderDetailsMasterDto.setServiceId(orderMasterDto2.getServiceMasterId());
											    orderDetailsMasterDto.setServiceAmt(orderMasterDto2.getServiceAmt());
											    orderDetailsMasterDto.setServiceAmt(orderMasterDto2.getServiceAmt());
												Tax tax = iEncounterMasterDao.getTaxPercentageByServiceId(orderMasterDto2.getServiceMasterId(),coversheetTemplateMasterDto.getUnitId());
												BigDecimal serviceAmt = BigDecimal.ZERO;
												BigDecimal netAmt= BigDecimal.ZERO;
												BigDecimal taxAmt= BigDecimal.ZERO;
												if(tax != null)
												{
													orderDetailsMasterDto.setTaxId(tax.getId());
													orderDetailsMasterDto.setTaxPer(new BigDecimal(tax.getTaxPercentage()));
													serviceAmt = orderMasterDto2.getServiceAmt()!=null?orderMasterDto2.getServiceAmt():new BigDecimal(0);
													taxAmt = serviceAmt.multiply((orderDetailsMasterDto.getTaxPer().multiply(new BigDecimal(0.01))));
													netAmt = serviceAmt.add(taxAmt);
												}else
												{
													orderDetailsMasterDto.setTaxPer(new BigDecimal(0.00));
													serviceAmt = orderMasterDto2.getServiceAmt();
													netAmt = serviceAmt;
												}
												orderDetailsMasterDto.setOrdTotalAmt(serviceAmt);
												orderDetailsMasterDto.setTaxAmount(taxAmt);
												orderDetailsMasterDto.setNetAmount(netAmt);
												orderDetailsMasterDto.setOrdConcession(orderMasterDto2.getOrdConcession());
												orderDetailsMasterDto.setOrdDiscount(orderMasterDto2.getOrdDiscount());
												orderDetailsMasterDto.setSelfPayable(orderMasterDto2.getSelfPayable());
												orderDetailsMasterDto.setCreditPayable(orderMasterDto2.getCreditPayable());
												orderDetailsMasterDto.setTariffId(defaultTariffId);
												orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
												orderDetailsMasterDto.setOrderEmergencyFlag('N');
												orderDetailsMasterDto.setServiceRendered(orderMasterDto2.getServiceRendered());
												orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
												orderDetailsMasterDto.setServiceChargeable(1);
												orderDetailsMasterDto.setServiceIsBilled(0);
												orderDetailsMasterDto.setPayeeId(selfPayeeId);
												orderDetailsMasterDto.setIsPackage(true);
												orderDetailsMasterDto.setPackage(true);
												orderDetailsMasterDto.setApportionedPrice(orderMasterDto2.getApportionedPrice());
												orderDetailsMasterDto.setPackageId(orderMasterDto2.getPackageId());
												orderDetailsMasterDto.setPackageTypeId(orderMasterDto2.getPackageTypeId());
												orderDetailsMasterDto.setIsPackageService(orderMasterDto2.getIsPackageService());
												orderDetailsMasterDto.setIsCapService(orderMasterDto2.getIsCapService());
												orderDetailsMasterDto.setPaymentEntitlementId(orderMasterDto2.getPaymentEntitlementTypeId());
												orderDetailsMasterDto.setOrdCancelled('N');
												orderDetailsMasterDto.setIsDrug('N');
												orderDetailsMasterDto.setServiceRendered(0);
												orderDetailsMasterDto.setStatus('A');
												orderDetailsMasterDto.setIsInvestigationOrder('N');
												orderDetailsMasterDto.setCoPayPer(orderMasterDto2.getCoPayPer());
												orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(orderMasterDto2.getOrderDetailsPayeeMapperDtosList());
												orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(orderMasterDto2.getOrderDetailsPackageMapperDtosList());
										}
										else if(orderMasterDto2.getPackageTypeId()!=null && orderMasterDto2.getPackageTypeId().intValue()==PACKAGE1 && !orderMasterDto2.isPackage())
										{
											orderDetailsMasterDto.setOrderDetailsId(orderMasterDto2.getOrderDetailsId());
										    orderDetailsMasterDto.setServiceId(orderMasterDto2.getServiceMasterId());
										    orderDetailsMasterDto.setServiceAmt(orderMasterDto2.getServiceAmt());
											orderDetailsMasterDto.setTariffId(defaultTariffId);
											orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
											orderDetailsMasterDto.setOrderEmergencyFlag('N');
											orderDetailsMasterDto.setServiceRendered(orderMasterDto2.getServiceRendered());
											orderDetailsMasterDto.setOrdRenderDatetime(date.getTime());
											orderDetailsMasterDto.setServiceChargeable(1);
											orderDetailsMasterDto.setServiceIsBilled(0);
											orderDetailsMasterDto.setPayeeId(selfPayeeId);
											orderDetailsMasterDto.setIsPackage(false);
											orderDetailsMasterDto.setPackage(false);
											orderDetailsMasterDto.setApportionedPrice(orderMasterDto2.getApportionedPrice());
											orderDetailsMasterDto.setPackageId(orderMasterDto2.getPackageId());
											orderDetailsMasterDto.setPackageTypeId(orderMasterDto2.getPackageTypeId());
											orderDetailsMasterDto.setIsPackageService(orderMasterDto2.getIsPackageService());
											orderDetailsMasterDto.setIsCapService(orderMasterDto2.getIsCapService());
											orderDetailsMasterDto.setPaymentEntitlementId(orderMasterDto2.getPaymentEntitlementTypeId());
											orderDetailsMasterDto.setOrdCancelled('N');
											orderDetailsMasterDto.setIsDrug('N');
											orderDetailsMasterDto.setServiceRendered(0);
											orderDetailsMasterDto.setStatus('A');
											orderDetailsMasterDto.setIsInvestigationOrder('N');
											orderDetailsMasterDto.setIsOrderVisibleDepartment('A');
											List<OrderDetailsPayeeMapperDto> listNew = new LinkedList<>();
											OrderDetailsPayeeMapperDto detailsPayeeMapperDto = new OrderDetailsPayeeMapperDto();
												detailsPayeeMapperDto.setPayeeId(orderDetailsMasterDto.getPayeeId());
												detailsPayeeMapperDto.setPayeeAmount(BigDecimal.ZERO);
												detailsPayeeMapperDto.setServiceId(orderDetailsMasterDto.getServiceId());
												listNew.add(detailsPayeeMapperDto);	
											orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(listNew);
											orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(orderMasterDto2.getOrderDetailsPackageMapperDtosList());
											
										  }
										else{
											
											  ContractConsumptionDto consumptionDto = new ContractConsumptionDto();
												consumptionDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
												consumptionDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
												consumptionDto.setVisitTypeId(orderMasterDto2.getVisitTypeId());
												consumptionDto.setPatientTypeId(orderMasterDto2.getPatientTypeId());
												consumptionDto.setBillingBedCategoryId(orderMasterDto2.getBillingBedCategoryId());
												consumptionDto.setPaymentEntitlementId(orderMasterDto2.getPaymentEntitlementTypeId());
												consumptionDto.setServiceId(orderMasterDto2.getServiceMasterId());
												consumptionDto.setCurrentDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()), "yyyy-MM-dd HH:mm:ss"));
												consumptionDto.setContractId(orderMasterDto2.getContractId());
												consumptionDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
												consumptionDto.setAdmissionId(coversheetTemplateMasterDto.getAdmissionId());
												consumptionDto.setDefaultSelfTariffId(defaultTariffId);
												consumptionDto.setSelfPayeeId(selfPayeeId);
												consumptionDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
												consumptionDto.setPatientClassId(orderMasterDtos.getPatientClassId());
											Response res =iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
												consumptionDto=(ContractConsumptionDto)res.getObject();
											
											if(res.getObject()!=null)
											{
												orderDetailsMasterDto.setIsRefDoctorShare(consumptionDto.getIsRefReq());
												orderDetailsMasterDto.setIsDocReq(consumptionDto.getIsDocReq());
												orderDetailsMasterDto.setContractId(consumptionDto.getContractId());
												orderDetailsMasterDto.setTariffId(defaultTariffId);
												orderDetailsMasterDto.setBillingClassId(1);//By default its OPD ---> Id==1
												orderDetailsMasterDto.setOrderEmergencyFlag('N');
												orderDetailsMasterDto.setServiceRendered(0);
												orderDetailsMasterDto.setServiceChargeable(1);
												orderDetailsMasterDto.setServiceIsBilled(0);
												orderDetailsMasterDto.setIsDrug('N');
												orderDetailsMasterDto.setPackage(orderMasterDto2.isPackage());
												orderDetailsMasterDto.setPackageId(orderMasterDto2.getPackageId());
												orderDetailsMasterDto.setIsInvestigationOrder('Y');
												orderDetailsMasterDto.setPatientClassPercentage(new BigDecimal(consumptionDto.getPatientClassPercentage()));
												orderDetailsMasterDto.setServiceAmt(new BigDecimal(consumptionDto.getServiceRate()));
												orderDetailsMasterDto.setOrdConcession(BigDecimal.valueOf(consumptionDto.getConcession()));
												orderDetailsMasterDto.setOrdDiscount(new BigDecimal(0.00));
												orderDetailsMasterDto.setOrdTotalAmt(BigDecimal.valueOf(consumptionDto.getServiceRate()));
												orderDetailsMasterDto.setTaxAmount(BigDecimal.valueOf(consumptionDto.getTaxAmt()));
												orderDetailsMasterDto.setNetAmount(BigDecimal.valueOf(consumptionDto.getNetAmount()));
												orderDetailsMasterDto.setTaxPer(BigDecimal.valueOf(consumptionDto.getTaxPercentage()));
												orderDetailsMasterDto.setSelfPayable(new BigDecimal(consumptionDto.getSelfPayable()));
												orderDetailsMasterDto.setPayeeId(consumptionDto.getPayeeId());
												orderDetailsMasterDto.setCreditPayable(new BigDecimal(consumptionDto.getCreaditPayable()));
												orderDetailsMasterDto.setPackageConsumptionAmt(consumptionDto.getPackageConsumptionAmt());
												orderDetailsMasterDto.setTaxId(consumptionDto.getTaxId());
												orderDetailsMasterDto.setOrderDetailsPayeeMapperDtosList(consumptionDto.getOrderDetailsPayeeMapperDtosList());
												orderDetailsMasterDto.setCoPayPer(new BigDecimal(consumptionDto.getCoShare()));
												orderDetailsMasterDto.setBedCategoryId(consumptionDto.getBillingBedCategoryId());
												if(consumptionDto.getOrderDetailsPackageMapperDtosList()!=null && consumptionDto.getOrderDetailsPackageMapperDtosList().size()>0)
													orderDetailsMasterDto.setOrderDetailsPackageMapperDtosList(consumptionDto.getOrderDetailsPackageMapperDtosList());
													
											}
										 }
									
									
											 orderDetailsList.add(orderDetailsMasterDto);
										
										
										if(orderMasterDto2.getOrdDocSplId() == null) {
											orderMasterDto.setOrdDocSplId(orderMasterDto2.getPackageTypeId()!=null && orderMasterDto2.getPackageTypeId()==PACKAGE1 && orderMasterDto2.getOrdDocSplId()!=null && orderMasterDto2.getOrdDocSplId() > 1 ? orderMasterDto2.getOrdDocSplId() : orderMasterDto2.getOrdDocSplId());
										}
										orderMasterDto.setListOrderDetailsMasterDto(orderDetailsList);
										   
										 orderRes = iOrderMasterService.saveOrderMaster(orderMasterDto);
										 labOrderId = (Integer) orderRes.getListObject().getFirst();
								
					
						
						/*orderMasterDto.setOrgId(coversheetTemplateMasterDto.getOrganizationId());
						orderMasterDto.setOrgUnitId(coversheetTemplateMasterDto.getUnitId());
						orderMasterDto.setOrderNo(ordDto.getOrderNo());
						orderMasterDto.setVisitTypeId(ordDto.getVisitTypeId());
						orderMasterDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
						orderMasterDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
						orderMasterDto.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
						orderMasterDto.setPriorityId(ordDto.getPriorityId());
						orderMasterDto.setOrdRemarks(ordDto.getOrdRemarks());
						orderMasterDto.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						orderMasterDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						orderMasterDto.setOrderStatus('A');
						orderMasterDto.setOrderStatusId(ordDto.getOrderStatusId());
						orderMasterDto.setIsPharmacy('N');
						orderMasterDto.setOrdDocSplId(ordDto.getSpecialityId());
						orderMasterDto.setHomeCollection(false);
						Response resp = iOrderMasterDao.saveOrderMaster(orderMasterDto);

						Integer orderId = (Integer) resp.getObject();

						if (orderId > 0 && coversheetTemplateMasterDto.getListLabOrderData() != null) {
							OrderDetailsMasterDto ordDtlDto = new OrderDetailsMasterDto();
							ordDtlDto.setOrgId(coversheetTemplateMasterDto.getOrganizationId());
							ordDtlDto.setOrgUnitId(coversheetTemplateMasterDto.getUnitId());
							ordDtlDto.setOrderId(orderId);
							ordDtlDto.setServiceId(ordDto.getServiceMasterId());
							ordDtlDto.setPriorityId(ordDto.getPriorityId());
							ordDtlDto.setIsOutsourced('N');
							ordDtlDto.setOrderQty(1);
							ordDtlDto.setOrdCancelled('N');
							ordDtlDto.setServiceAmt(ordDto.getServiceAmt());
							ordDtlDto.setOrdDocSplId(ordDto.getOrdDocSplId());
							ordDtlDto.setSubDepartmentId(ordDto.getSubDeptId());
							ordDtlDto.setServiceRendered(ordDto.getServiceRendered());
							ordDtlDto.setStatus('A');
							ordDtlDto.setIsOrderVisibleDepartment('A');
							ordDtlDto.setIsAppointableRadiology('N');
							ordDtlDto.setOrdRemarks(ordDto.getOrdRemarks());
							ordDtlDto.setOrdCancelled('N');
							ordDtlDto.setOrderQty(1);
							iOrderDetailsMasterDao.saveOrderDetailsMasterList(ordDtlDto);
						}*/
						
						
						
						
					} else {
						if (orderMasterDto2.getServiceText() != null) {

							FreeTextOrderedServicesDto freeTextOrderedServices = new FreeTextOrderedServicesDto();
							freeTextOrderedServices
									.setServiceText(orderMasterDto2.getServiceText() != null ? orderMasterDto2.getServiceText() : "");
							freeTextOrderedServices.setIsConsumed(false);
							freeTextOrderedServices.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
							freeTextOrderedServices.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
							freeTextOrderedServices.setPatientId(coversheetTemplateMasterDto.getPatientId());
//							freeTextOrderedServices.setAdmissionId(ordDto.getAdmissionId() != null ? ordDto.getAdmissionId() : null);
							freeTextOrderedServices.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
							freeTextOrderedServices.setOrderDetailsId(orderMasterDto2.getOrderDetailsId());
							freeTextOrderedServices.setOrderId(orderMasterDto2.getOrderId());
							freeTextOrderedServices.setUnitId(coversheetTemplateMasterDto.getUnitId());
							freeTextOrderedServices.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
							freeTextOrderedServices.setStatus('A');
							freeTextOrderedServices.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
							iOrderDetailsMasterDao.saveFreeTextInvestigations(freeTextOrderedServices);

						}

					}
				}
				
			}
			if (coversheetTemplateMasterDto.getListPrescriptionData() != null
					&& coversheetTemplateMasterDto.getListPrescriptionData().size() > 0) {
				PrescriptionDto prescriptionDtoSave = new PrescriptionDto();
//				List<MedicationDTO> medicationDTO =  new ArrayList<>();
				List<MedicationDTO> list = new ArrayList<>();
				CoversheetTemplateMasterDto resDto = new CoversheetTemplateMasterDto();
                if(coversheetTemplateMasterDto.getCovTemplateId() != null) {

					Response res = iCoversheetTemaplateDao.getTemplateDataByCovTempId(coversheetTemplateMasterDto);
					resDto = (CoversheetTemplateMasterDto) res.getObject();

					if (resDto.getListPrescriptionData() != null
							&& resDto.getListPrescriptionData().size() > 0) {
//						medicationDTO = resDto.getListPrescriptionData().get(0);
						list = resDto.getListPrescriptionData();
					} else {
//						medicationDTO = coversheetTemplateMasterDto.getListPrescriptionData().get(0);
						list = coversheetTemplateMasterDto.getListPrescriptionData();
					}
				}
				else {
						list = coversheetTemplateMasterDto.getListPrescriptionData();
					}
                MedicationDTO medicationDTO = list.getFirst();
//				for (MedicationDTO medicationDTO : list) {

						prescriptionDtoSave.setIsIntervention(FALSE);
						prescriptionDtoSave.setIsActive(TRUE);
						prescriptionDtoSave.setIsDeleted(FALSE);
						prescriptionDtoSave.setIsValidate(FALSE);
						prescriptionDtoSave.setIsDischarge(FALSE);
						prescriptionDtoSave.setReconciliationDone(FALSE);
						prescriptionDtoSave.setIsNonFormulary('N');
						prescriptionDtoSave.setJustificationStop(medicationDTO.getJustificationStop());
						prescriptionDtoSave.setIsMedicationStop('N');
						prescriptionDtoSave.setVisitTypeId(medicationDTO.getVisitTypeId());
						prescriptionDtoSave.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
						prescriptionDtoSave.setAuthorisedBy(coversheetTemplateMasterDto.getCreatedBy());
						prescriptionDtoSave.setAuthorisedDate(coversheetTemplateMasterDto.getCreatedDate());
						prescriptionDtoSave.setPriorityId(2);
						prescriptionDtoSave.setStatusId(1);
						prescriptionDtoSave.setPrescriptionDate(coversheetTemplateMasterDto.getCreatedDate());
						prescriptionDtoSave.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
						prescriptionDtoSave.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						prescriptionDtoSave.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						prescriptionDtoSave.setUnitId(coversheetTemplateMasterDto.getUnitId());
						prescriptionDtoSave.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
						prescriptionDtoSave.setMedicationType(1);
						prescriptionDtoSave.setAdmissionId(null);
						prescriptionDtoSave.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
						prescriptionDtoSave.setPatientId(coversheetTemplateMasterDto.getPatientId());
//					prescriptionDto.setPrescriptionNo(getPrescriptionNumber());
						prescriptionDtoSave.setIsMedicationStop('N');
//					prescriptionDto.setJustification_stop(prescriptionDto.getJustification_stop());
						String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0,
								coversheetTemplateMasterDto.getUnitId(), PRES_NO);
						prescriptionDtoSave.setIsNonFormulary('N');

						prescriptionDtoSave.setPrescriptionNo(prescriptionNo);
						Response presRes = iMedicationDAO.savePrescription(prescriptionDtoSave);
						prescriptionDtoSave = (PrescriptionDto) presRes.getObject();
//				}

						List<MedicationDTO> listNew	= new ArrayList<>();
						if(resDto.getListPrescriptionData() != null)
						{
							listNew = resDto.getListPrescriptionData();
						}
						else if(coversheetTemplateMasterDto.getListPrescriptionData() != null && coversheetTemplateMasterDto.getListPrescriptionData().size()>0){
							listNew = coversheetTemplateMasterDto.getListPrescriptionData();
						}
						List<PrescriptionDetailsDto> listDetails = new ArrayList<>();
						for (MedicationDTO medicationDTONew : listNew) {
							PrescriptionDetailsDto details = new PrescriptionDetailsDto();
							details.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
							details.setUnitId(coversheetTemplateMasterDto.getUnitId());
							details.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
							details.setIsDiscontinued(FALSE);
							details.setIsMedicationStop('N');
							details.setIsPreStop(FALSE);
							details.setIsIntervention(FALSE);
							details.setDuration(
									medicationDTONew.getDurationCount() != null ? medicationDTONew.getDurationCount() : null);
							details.setDurationUnit(
									medicationDTONew.getDurationUnit() != null ? medicationDTONew.getDurationUnit() : null);
							details.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
							details.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
							details.setOrderQty(medicationDTONew.getOrderQty());
							details.setPriorityId(medicationDTONew.getPriorityId());
							details.setIsDeleted(FALSE);
							details.setIsActive(TRUE);
							details.setStatusId(1);
							details.setPrnflag(FALSE);
							details.setItemId(medicationDTONew.getItemId() != null ? medicationDTONew.getItemId() : null);
//					details.setPrescribedDose(medicationDTO.getPrescribedDose() !=null ? medicationDTO.getPrescribedDose(): null);
							details.setStatflag(medicationDTONew.getStatflag());
							details.setGenericName(medicationDTONew.getGenericName());
							details.setItemName(medicationDTONew.getItemName());
							details.setIsNonFormulary(medicationDTONew.getIsNonFormulary());
							details.setInfusionflag(FALSE);
							details.setUpdatedBy(prescriptionDtoSave.getAddedBy());
							details.setInstructionId(medicationDTONew.getInstructionId());
							details.setInstructionDesc(medicationDTONew.getInstructionText());
							details.setFrequencyId(medicationDTONew.getFrequencyId());
							//	details.setStartDate(
							//		GlobalCommonDateUtils.getAvalDate(medicationDTONew.getStartDateNew(), "yyyy-M-dd HH:mm:ss"));
							details.setStartDate(
									GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime(), "yyyy-M-dd HH:mm:ss"));
							Date startCals = GlobalCommonDateUtils.getDate(details.getStartDate(), "yyyy-M-dd HH:mm:ss");
							String durationh = null;
							Date oneMinutesFromNow =null;
							if(medicationDTONew.getDurationUnit().equals("Day")) {
								Calendar now = Calendar.getInstance();
								now.add(Calendar.DAY_OF_MONTH, medicationDTONew.getDurationCount()!= null ? medicationDTONew.getDurationCount():1);
								oneMinutesFromNow = now.getTime();

							}else if(medicationDTONew.getDurationUnit().equals("Month")) {
								Calendar now = Calendar.getInstance();
								now.add(Calendar.DAY_OF_MONTH, medicationDTONew.getDurationCount());
								oneMinutesFromNow = now.getTime();

							}else if(medicationDTONew.getDurationUnit().equals("Year")) {
								Calendar now = Calendar.getInstance();
								now.add(Calendar.DAY_OF_YEAR, medicationDTONew.getDurationCount());
								oneMinutesFromNow = now.getTime();

							}



							details.setStopDate(GlobalCommonDateUtils.getStringDate(oneMinutesFromNow, "yyyy-M-dd HH:mm:ss"));
							if (details.getIsDischarge() != null) {
								details.setIsDischarge(medicationDTONew.getIsDischarge());
							} else {
								details.setIsDischarge(false);
							}
							listDetails.add(details);
							prescriptionDtoSave.setPrescriptionDetailsDtosList(listDetails);
							ListIterator<PrescriptionDetailsDto> iterator = prescriptionDtoSave.getPrescriptionDetailsDtosList().listIterator();
							while(iterator.hasNext())
							{
								PrescriptionDetailsDto prescriptionDetailsDto = iterator.next();
								prescriptionDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
								prescriptionDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
								prescriptionDetailsDto.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
								prescriptionDetailsDto.setIsDiscontinued(FALSE);
								prescriptionDetailsDto.setIsMedicationStop('N');
								prescriptionDetailsDto.setIsPreStop(FALSE);
								prescriptionDetailsDto.setIsIntervention(FALSE);
								prescriptionDetailsDto.setDuration(prescriptionDetailsDto.getDuration()!=null ? prescriptionDetailsDto.getDuration() :null);
								prescriptionDetailsDto.setDurationUnit(prescriptionDetailsDto.getDurationUnit()!=null ? prescriptionDetailsDto.getDurationUnit() :null);
								prescriptionDetailsDto.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
								prescriptionDetailsDto.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
								prescriptionDetailsDto.setOrderQty(prescriptionDetailsDto.getOrderQty());
								prescriptionDetailsDto.setPriorityId(prescriptionDetailsDto.getPriorityId());
								prescriptionDetailsDto.setIsDeleted(FALSE);
								prescriptionDetailsDto.setIsActive(TRUE);
								prescriptionDetailsDto.setStatusId(1);
								prescriptionDetailsDto.setPrnflag(FALSE);
								prescriptionDetailsDto.setItemId(prescriptionDetailsDto.getItemId()!=null ? prescriptionDetailsDto.getItemId():null);
								prescriptionDetailsDto.setPrescribedDose(prescriptionDetailsDto.getPrescribedDose() !=null ? prescriptionDetailsDto.getPrescribedDose(): null);
								prescriptionDetailsDto.setStatflag(prescriptionDetailsDto.getStatflag());
								prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
								prescriptionDetailsDto.setFrequencyId(prescriptionDetailsDto.getFrequencyId());
								prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
								prescriptionDetailsDto.setIsNonFormulary(prescriptionDetailsDto.getIsNonFormulary());
								prescriptionDetailsDto.setInfusionflag(FALSE);
								prescriptionDetailsDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
								prescriptionDetailsDto.setInstructionId(prescriptionDetailsDto.getInstructionId());
								prescriptionDetailsDto.setInstructionDesc(prescriptionDetailsDto.getInstructionDesc());
								prescriptionDetailsDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
								if(prescriptionDetailsDto.getIsDischarge()!=null){
									prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
								}else{
									prescriptionDetailsDto.setIsDischarge(false);
								}
								Response resPreDtls = iMedicationDAO.savePrescriptionDetais(prescriptionDetailsDto);

								prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls.getObject();

								iMedicationDAO.savePrescriptionFrequencyDetails(prescriptionDetailsDto);
							}
						}
//					}
				
			}
			else if(coversheetTemplateMasterDto.getListPrescriptionData() == null){
				if(coversheetTemplateMasterDto.getEncounterId()!=null){

					PrescriptionDto prescriptionDtoSave = new PrescriptionDto();
					MedicationDTO medicationDTO =  new MedicationDTO();
	                if(coversheetTemplateMasterDto.getCovTemplateId() != null) {
	    				Response res= iCoversheetTemaplateDao.getTemplateDataByCovTempId(coversheetTemplateMasterDto);
						CoversheetTemplateMasterDto resDto = (CoversheetTemplateMasterDto) res.getObject();
						if (resDto.getListPrescriptionData() != null
								&& resDto.getListPrescriptionData().size() > 0) {
							 medicationDTO = resDto.getListPrescriptionData().getFirst();
	                }else {
						 medicationDTO = coversheetTemplateMasterDto.getListPrescriptionData().getFirst();
	                }
//				for (MedicationDTO medicationDTO : list) {

							prescriptionDtoSave.setIsIntervention(FALSE);
							prescriptionDtoSave.setIsActive(TRUE);
							prescriptionDtoSave.setIsDeleted(FALSE);
							prescriptionDtoSave.setIsValidate(FALSE);
							prescriptionDtoSave.setIsDischarge(FALSE);
							prescriptionDtoSave.setReconciliationDone(FALSE);
							prescriptionDtoSave.setIsNonFormulary('N');
							prescriptionDtoSave.setJustificationStop(medicationDTO.getJustificationStop());
							prescriptionDtoSave.setIsMedicationStop('N');
							prescriptionDtoSave.setVisitTypeId(medicationDTO.getVisitTypeId());
							prescriptionDtoSave.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
							prescriptionDtoSave.setAuthorisedBy(coversheetTemplateMasterDto.getCreatedBy());
							prescriptionDtoSave.setAuthorisedDate(coversheetTemplateMasterDto.getCreatedDate());
							prescriptionDtoSave.setPriorityId(2);
							prescriptionDtoSave.setStatusId(1);
							prescriptionDtoSave.setPrescriptionDate(coversheetTemplateMasterDto.getCreatedDate());
							prescriptionDtoSave.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
							prescriptionDtoSave.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
							prescriptionDtoSave.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
							prescriptionDtoSave.setUnitId(coversheetTemplateMasterDto.getUnitId());
							prescriptionDtoSave.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
							prescriptionDtoSave.setMedicationType(1);
							prescriptionDtoSave.setAdmissionId(null);
							prescriptionDtoSave.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
							prescriptionDtoSave.setPatientId(coversheetTemplateMasterDto.getPatientId());
//					prescriptionDto.setPrescriptionNo(getPrescriptionNumber());
							prescriptionDtoSave.setIsMedicationStop('N');
//					prescriptionDto.setJustification_stop(prescriptionDto.getJustification_stop());
							String prescriptionNo = iGlobalUnitMasterDao.getGeneratedNo(0,
									coversheetTemplateMasterDto.getUnitId(), PRES_NO);
							prescriptionDtoSave.setIsNonFormulary('N');

							prescriptionDtoSave.setPrescriptionNo(prescriptionNo);
							Response presRes = iMedicationDAO.savePrescription(prescriptionDtoSave);
							prescriptionDtoSave = (PrescriptionDto) presRes.getObject();
//				}

							List<MedicationDTO> listNew = resDto.getListPrescriptionData();
							List<PrescriptionDetailsDto> listDetails = new ArrayList<>();
							for (MedicationDTO medicationDTONew : listNew) {
								PrescriptionDetailsDto details = new PrescriptionDetailsDto();
								details.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
								details.setUnitId(coversheetTemplateMasterDto.getUnitId());
								details.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
								details.setIsDiscontinued(FALSE);
								details.setIsMedicationStop('N');
								details.setIsPreStop(FALSE);
								details.setIsIntervention(FALSE);
								details.setDuration(
										medicationDTONew.getDurationCount() != null ? medicationDTONew.getDurationCount() : null);
								details.setDurationUnit(
										medicationDTONew.getDurationUnit() != null ? medicationDTONew.getDurationUnit() : null);
								details.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
								details.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
								details.setOrderQty(medicationDTONew.getOrderQty());
								details.setPriorityId(medicationDTONew.getPriorityId());
								details.setIsDeleted(FALSE);
								details.setIsActive(TRUE);
								details.setStatusId(1);
								details.setPrnflag(FALSE);
								details.setItemId(medicationDTONew.getItemId() != null ? medicationDTONew.getItemId() : null);
//					details.setPrescribedDose(medicationDTO.getPrescribedDose() !=null ? medicationDTO.getPrescribedDose(): null);
								details.setStatflag(medicationDTONew.getStatflag());
								details.setGenericName(medicationDTONew.getGenericName());
								details.setItemName(medicationDTONew.getItemName());
								details.setIsNonFormulary(medicationDTONew.getIsNonFormulary());
								details.setInfusionflag(FALSE);
								details.setUpdatedBy(prescriptionDtoSave.getAddedBy());
								details.setInstructionId(medicationDTONew.getInstructionId());
								details.setInstructionDesc(medicationDTONew.getInstructionText());
								details.setFrequencyId(medicationDTONew.getFrequencyId());
								//	details.setStartDate(
								//		GlobalCommonDateUtils.getAvalDate(medicationDTONew.getStartDateNew(), "yyyy-M-dd HH:mm:ss"));
								details.setStartDate(
										GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime(), "yyyy-M-dd HH:mm:ss"));
								Date startCals = GlobalCommonDateUtils.getDate(details.getStartDate(), "yyyy-M-dd HH:mm:ss");
								String durationh = null;
								Date oneMinutesFromNow =null;
								if(medicationDTONew.getDurationUnit().equals("Day")) {
									Calendar now = Calendar.getInstance();
									now.add(Calendar.DAY_OF_MONTH, medicationDTONew.getDurationCount());
									oneMinutesFromNow = now.getTime();

								}else if(medicationDTONew.getDurationUnit().equals("Month")) {
									Calendar now = Calendar.getInstance();
									now.add(Calendar.DAY_OF_MONTH, medicationDTONew.getDurationCount());
									oneMinutesFromNow = now.getTime();

								}else if(medicationDTONew.getDurationUnit().equals("Year")) {
									Calendar now = Calendar.getInstance();
									now.add(Calendar.DAY_OF_YEAR, medicationDTONew.getDurationCount());
									oneMinutesFromNow = now.getTime();

								}



								details.setStopDate(GlobalCommonDateUtils.getStringDate(oneMinutesFromNow, "yyyy-M-dd HH:mm:ss"));
								if (details.getIsDischarge() != null) {
									details.setIsDischarge(medicationDTONew.getIsDischarge());
								} else {
									details.setIsDischarge(false);
								}
								listDetails.add(details);
								prescriptionDtoSave.setPrescriptionDetailsDtosList(listDetails);
								ListIterator<PrescriptionDetailsDto> iterator = prescriptionDtoSave.getPrescriptionDetailsDtosList().listIterator();
								while(iterator.hasNext())
								{
									PrescriptionDetailsDto prescriptionDetailsDto = iterator.next();
									prescriptionDetailsDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
									prescriptionDetailsDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
									prescriptionDetailsDto.setPrescriptionId(prescriptionDtoSave.getPrescriptionId());
									prescriptionDetailsDto.setIsDiscontinued(FALSE);
									prescriptionDetailsDto.setIsMedicationStop('N');
									prescriptionDetailsDto.setIsPreStop(FALSE);
									prescriptionDetailsDto.setIsIntervention(FALSE);
									prescriptionDetailsDto.setDuration(prescriptionDetailsDto.getDuration()!=null ? prescriptionDetailsDto.getDuration() :null);
									prescriptionDetailsDto.setDurationUnit(prescriptionDetailsDto.getDurationUnit()!=null ? prescriptionDetailsDto.getDurationUnit() :null);
									prescriptionDetailsDto.setAddedBy(coversheetTemplateMasterDto.getCreatedBy());
									prescriptionDetailsDto.setAddedDate(coversheetTemplateMasterDto.getCreatedDate());
									prescriptionDetailsDto.setOrderQty(prescriptionDetailsDto.getOrderQty());
									prescriptionDetailsDto.setPriorityId(prescriptionDetailsDto.getPriorityId());
									prescriptionDetailsDto.setIsDeleted(FALSE);
									prescriptionDetailsDto.setIsActive(TRUE);
									prescriptionDetailsDto.setStatusId(1);
									prescriptionDetailsDto.setPrnflag(FALSE);
									prescriptionDetailsDto.setItemId(prescriptionDetailsDto.getItemId()!=null ? prescriptionDetailsDto.getItemId():null);
									prescriptionDetailsDto.setPrescribedDose(prescriptionDetailsDto.getPrescribedDose() !=null ? prescriptionDetailsDto.getPrescribedDose(): null);
									prescriptionDetailsDto.setStatflag(prescriptionDetailsDto.getStatflag());
									prescriptionDetailsDto.setGenericName(prescriptionDetailsDto.getGenericName());
									prescriptionDetailsDto.setFrequencyId(prescriptionDetailsDto.getFrequencyId());
									prescriptionDetailsDto.setItemName(prescriptionDetailsDto.getItemName());
									prescriptionDetailsDto.setIsNonFormulary(prescriptionDetailsDto.getIsNonFormulary());
									prescriptionDetailsDto.setInfusionflag(FALSE);
									prescriptionDetailsDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
									prescriptionDetailsDto.setInstructionId(prescriptionDetailsDto.getInstructionId());
									prescriptionDetailsDto.setInstructionDesc(prescriptionDetailsDto.getInstructionDesc());
									prescriptionDetailsDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
									if(prescriptionDetailsDto.getIsDischarge()!=null){
										prescriptionDetailsDto.setIsDischarge(prescriptionDetailsDto.getIsDischarge());
									}else{
										prescriptionDetailsDto.setIsDischarge(false);
									}
									Response resPreDtls = iMedicationDAO.savePrescriptionDetais(prescriptionDetailsDto);

									prescriptionDetailsDto = (PrescriptionDetailsDto) resPreDtls.getObject();

									iMedicationDAO.savePrescriptionFrequencyDetails(prescriptionDetailsDto);
								}
							}
						}
				}
			}
				if (coversheetTemplateMasterDto.getListAdviceData() != null
						&& coversheetTemplateMasterDto.getListAdviceData().size() > 0) {

					List<CoversheetInstructionDto> list1 = coversheetTemplateMasterDto.getListAdviceData();
					CoversheetInstructionDto covInstructionDto = new CoversheetInstructionDto();
					for (CoversheetInstructionDto dto : list1) {
						covInstructionDto.setAdvicedInstruction(
								dto.getAdvicedInstruction() != null ? dto.getAdvicedInstruction() : "");
						if (dto.getInstructionDetailsId() != null) {
							covInstructionDto.setInstructionDetailsId(dto.getInstructionDetailsId());
						}
						covInstructionDto.setActivityOrIntrcs(dto.getActivityOrIntrcs());
						covInstructionDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
						covInstructionDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
						covInstructionDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
						covInstructionDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
						covInstructionDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
						covInstructionDto.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
						covInstructionDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
						covInstructionDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
						covInstructionDto.setStatus('A');
						iCoversheetInstructionDao.saveCoversheetInstruction(covInstructionDto);
					}

				}
				
				if (coversheetTemplateMasterDto.getListComplaintData() != null
						&& coversheetTemplateMasterDto.getListComplaintData().size() > 0) {
					
						ComplaintAppointmentMapperDto complaintAppointmentMapperDto = new ComplaintAppointmentMapperDto();
							complaintAppointmentMapperDto.setIsResolved('N');
							complaintAppointmentMapperDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
							complaintAppointmentMapperDto.setRoleId(coversheetTemplateMasterDto.getRoleId());
							complaintAppointmentMapperDto.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
							complaintAppointmentMapperDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
							complaintAppointmentMapperDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
							complaintAppointmentMapperDto.setCeratedDate(coversheetTemplateMasterDto.getCreatedDate());
							complaintAppointmentMapperDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
							complaintAppointmentMapperDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
							complaintAppointmentMapperDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
							complaintAppointmentMapperDto.setStatus('A');
							List<ComplaintAppointmentDetailsDto> listComplaintAppointmentDetailsDto = new ArrayList<>();
							
							ComplaintAppointmentDetailsDto complaintDtl=new ComplaintAppointmentDetailsDto();
							
							for (ComplaintAppointmentMapperDto dtoComplaint : coversheetTemplateMasterDto.getListComplaintData()) {
								complaintDtl  = new ComplaintAppointmentDetailsDto();
								complaintDtl.setIsReviewedFlag(YES);
								complaintDtl.setCeratedDate(coversheetTemplateMasterDto.getCreatedDate());
								complaintDtl.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
							    complaintDtl.setIsReviewedFlag(NO);
								complaintDtl.setIsResolved('N');
								complaintDtl.setPatientId(coversheetTemplateMasterDto.getPatientId());
								complaintDtl.setRoleId(coversheetTemplateMasterDto.getRoleId());
								complaintDtl.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
								complaintDtl.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
								complaintDtl.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
								complaintDtl.setStatus('A');
								complaintDtl.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
								complaintDtl.setUnitId(coversheetTemplateMasterDto.getUnitId());
								complaintDtl.setDiagnosisId(dtoComplaint.getDiagnosisId());
								complaintDtl.setIsComplaintStop('N');
								complaintDtl.setIcpcCodeText(dtoComplaint.getIcpcCodeText());
								complaintDtl.setIcpcSystemId(dtoComplaint.getIcpcSystemId());
								complaintDtl.setDuration(dtoComplaint.getDuration());
								complaintDtl.setRemark(dtoComplaint.getRemark());
								complaintDtl.setSince(dtoComplaint.getSince());
								listComplaintAppointmentDetailsDto.add(complaintDtl);
							}
							complaintAppointmentMapperDto.setListComplaintAppointmentDetailsDto(listComplaintAppointmentDetailsDto);
							
							iComplaintAppointmentMapperDao.saveComplaintAppointmentMapper(complaintAppointmentMapperDto);
						
					}
				
					if(coversheetTemplateMasterDto.getPatientRemark() != null ) {
						Session session=sessionFactory.getCurrentSession();
						session.createSQLQuery("update  patient.t_patient_registration  set patient_remark ='"+coversheetTemplateMasterDto.getPatientRemark()+"'"
						+" where patient_id=:id and unit_id=:unitId and organization_id =:orgId ")
						.setInteger("id", coversheetTemplateMasterDto.getPatientId())
						.setInteger("unitId", coversheetTemplateMasterDto.getUnitId())
						.setInteger("orgId", coversheetTemplateMasterDto.getOrganizationId())					
						.executeUpdate();

					}
					if(coversheetTemplateMasterDto.getListQuickNotes() != null && coversheetTemplateMasterDto.getListQuickNotes().size() > 0) {
						
						
						//Check CURRENT ENCOUNTER data 
						
						StringBuilder query = new StringBuilder("select qnm.quick_notes_mapper_id  as \"quickNotesMapperId\",\r\n" + 
								"qnm.quick_notes_desc as \"quickNotesDesc\"\r\n" + 
								"from opd.t_quick_notes_mapper qnm\r\n" + 
								"where qnm.patient_id  =:patientId\r\n" + 
								" and qnm.encounter_id  =:encounterId\r\n" + 
								" and qnm.organization_id =:orgId\r\n" + 
								" and qnm.unit_id =:unitId");

						List<QuickNotesMapperDto> listSaved = null;
						listSaved = sessionFactory.getCurrentSession().createSQLQuery(query.toString())
								.setInteger("patientId", coversheetTemplateMasterDto.getPatientId())
								.setInteger("unitId", coversheetTemplateMasterDto.getUnitId())
								.setInteger("orgId", coversheetTemplateMasterDto.getOrganizationId())
								.setInteger("encounterId", coversheetTemplateMasterDto.getEncounterId())
								.setResultTransformer(Transformers.aliasToBean(QuickNotesMapperDto.class)).list();
						
						if(listSaved == null || listSaved.size() == 0) {
							List<QuickNotesMapperDto> listQuick = coversheetTemplateMasterDto.getListQuickNotes();
							for (QuickNotesMapperDto dtossss : listQuick) {
								QuickNotesMapperDto quickNotesMapperDto=new QuickNotesMapperDto();
								
								 quickNotesMapperDto.setQuickNotesDesc(dtossss.getQuickNotesDesc());
								 quickNotesMapperDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
								 if(coversheetTemplateMasterDto.getEncounterId()!=null){
								 quickNotesMapperDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
								 }else{
									 quickNotesMapperDto.setAdmissionId(coversheetTemplateMasterDto.getAdmissionId());
								 }
								 quickNotesMapperDto.setDoctorId(coversheetTemplateMasterDto.getDoctorId());
								 quickNotesMapperDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
								 quickNotesMapperDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
								 quickNotesMapperDto.setStatus('A');
								 quickNotesMapperDto.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
								 quickNotesMapperDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
								 quickNotesMapperDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
								 quickNotesMapperDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
								 iQuickNotesMapperDao.saveQuickNotesMapper(quickNotesMapperDto);
							}
						}else {
							List<QuickNotesMapperDto> listQuick = listSaved;
							for (QuickNotesMapperDto dtossss : listQuick) {
								Session session=sessionFactory.getCurrentSession();
								session.createSQLQuery("update  opd.t_quick_notes_mapper  set quick_notes_desc ='"+dtossss.getQuickNotesDesc()+"'"
								+" where quick_notes_mapper_id=:quickNotesMapperId")
								.setInteger("quickNotesMapperId", dtossss.getQuickNotesMapperId())
								.executeUpdate();
							}
						}
					
					}
					
					
					// VITAL SAVE
					if(coversheetTemplateMasterDto.getListAppointmentVitalMapperDto() != null && 
							coversheetTemplateMasterDto.getListAppointmentVitalMapperDto().size() > 0) {
				
						List<AppointmentVitalMapperDto> listAppointmentVitalMapperDtos = new ArrayList<>();
				
						AppointmentVitalMapperDto appointmentVitalMapperDto = new AppointmentVitalMapperDto();
				
						for (AppointmentVitalMapperDto dtoVital : coversheetTemplateMasterDto.getListAppointmentVitalMapperDto()) {
							appointmentVitalMapperDto  = new AppointmentVitalMapperDto();
							appointmentVitalMapperDto.setValue(dtoVital.getValue()!=""?
									dtoVital.getValue():"0");
							appointmentVitalMapperDto.setVitalId(dtoVital.getVitalId());
							appointmentVitalMapperDto.setIsEnteredInError('N');
							appointmentVitalMapperDto.setRoleId(appointmentVitalMapperDto.getRoleId());
							appointmentVitalMapperDto.setIsReviewedFlag(NO);
							appointmentVitalMapperDto.setIsSelfTriage(INACTIVE);
							if(dtoVital.getEncounterId()!=null)
								appointmentVitalMapperDto.setEncounterId(coversheetTemplateMasterDto.getEncounterId());
							else
								appointmentVitalMapperDto.setAdmissionId(coversheetTemplateMasterDto.getAdmissionId());
					
							appointmentVitalMapperDto.setIsPostFacto(dtoVital.getIsPostFacto());

							if(dtoVital.getPostFactoTime()!=null) {
								appointmentVitalMapperDto.setPostFactoTime(dtoVital.getPostFactoTime());
							}
							else {
								LocalTime currentTime = LocalTime.parse( Long.toString(TimeZoneComponent.getDateByzone(coversheetTemplateMasterDto.getUnitId()).getTime()));
								appointmentVitalMapperDto.setPostFactoTime(currentTime.toString());
							}
							if(dtoVital.getPostFactoDate()!=null) {
								appointmentVitalMapperDto.setPostFactoDate(dtoVital.getPostFactoDate());
							}
							if(dtoVital.getPatientDialysisRecId()!=null) {
								appointmentVitalMapperDto.setPatientDialysisRecId(dtoVital.getPatientDialysisRecId());
								appointmentVitalMapperDto.setDialysisEventId(dtoVital.getDialysisEventId());
							}
							appointmentVitalMapperDto.setCreatedBy(coversheetTemplateMasterDto.getCreatedBy());
							appointmentVitalMapperDto.setCreatedDate(coversheetTemplateMasterDto.getCreatedDate());
							appointmentVitalMapperDto.setOrganizationId(coversheetTemplateMasterDto.getOrganizationId());
							appointmentVitalMapperDto.setPatientId(coversheetTemplateMasterDto.getPatientId());
							appointmentVitalMapperDto.setStatus('A');
							appointmentVitalMapperDto.setUnitId(coversheetTemplateMasterDto.getUnitId());
							appointmentVitalMapperDto.setUpdatedBy(coversheetTemplateMasterDto.getUpdatedBy());
							appointmentVitalMapperDto.setUpdatedDate(coversheetTemplateMasterDto.getUpdatedDate());
							listAppointmentVitalMapperDtos.add(appointmentVitalMapperDto);
						}
						appointmentVitalMapperDto.setListAppointmentVitalMapperDto(listAppointmentVitalMapperDtos);
						iAppointmentVitalMapperDao.saveAppointmentVitalMapperEasyEmr(appointmentVitalMapperDto);
					}
					return new Response<>(SUCCESS, SUCCESS_CODE, null, null, coversheetTemplateMasterDto);
		}
}
