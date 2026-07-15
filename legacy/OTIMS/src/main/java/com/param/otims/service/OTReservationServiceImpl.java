package com.param.otims.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dto.PatientRegistrationDto;
import com.param.global.mail.IMailingService;
import com.param.global.otims.dao.IImplantDetailsDao;
import com.param.global.otims.dao.IMedicationDetailsDao;
import com.param.global.otims.dao.IProcedureOrderChecklistDao;
import com.param.global.otims.dao.IProcedureOrderConsentDao;
import com.param.global.otims.dao.IProcedureOrderEquipmentDao;
import com.param.global.otims.dao.IProcedureOrderPatientEducationDao;
import com.param.global.otims.dao.IProcedureOrderServiceDao;
import com.param.global.otims.dao.IProcedureOrderStaffDao;
import com.param.global.otims.dto.ImplantDetailsDto;
import com.param.global.otims.dto.MedicationDetailsDto;
import com.param.global.otims.dto.OTReservationDto;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.global.otims.dto.ProcedureReqMasterDto;
import com.param.global.otims.dto.ProcedureStaffScheduleDto;
import com.param.global.otims.dto.SurgicalCalenderMapperDto;
import com.param.global.otims.service.ISurgicalCalendarMasterService;
import com.param.global.procedure.VO.AssetVO;
import com.param.global.service.IPatientRegistrationService;
import com.param.otims.dao.IOTIMSDashboardDao;
import com.param.otims.dao.IOTReservationDao;
import com.param.otims.model.OTIMSDashboard;
import com.param.otims.model.ProcedureOrderMaster;
import com.param.otims.model.ProcedureReqMaster;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes","unchecked","unused"})
public class OTReservationServiceImpl  implements IOTReservationService, ICommonConstants ,IBillingCommonConstants{

	private static final Logger logger = Logger.getLogger(OTReservationServiceImpl.class);

	@Autowired
	IOTReservationDao iOTReservationDao;
	
	@Autowired
	IPatientRegistrationService patientRegistrationServiceImpl;
	
	@Autowired
	IOTIMSDashboardService iOTIMSDashboardService;
	
	@Autowired
	IProcedureOrderConsentDao iProcedureOrderConsentDao;
	
	@Autowired
	IProcedureOrderServiceDao iProcedureOrderServiceDao;
	
	@Autowired
	IProcedureOrderChecklistDao iProcedureOrderChecklistDao;
	
	@Autowired
	IProcedureOrderPatientEducationDao iProcedureOrderPatientEducationDao;
	
	@Autowired
	IProcedureOrderStaffDao iProcedureOrderStaffDao;
	
	@Autowired
	IMedicationDetailsDao iMedicationDetailsDao;

	@Autowired
	private IImplantDetailsDao iImplantDetailsDao;
	
	@Autowired 
	IMailingService iMailingService;
	
	@Autowired
	IProcedureOrderEquipmentDao iProcedureOrderEquipmentDao;
	
	@Autowired
	private ISurgicalCalendarMasterService iSurgicalCalendarMasterService;
	
	@Autowired
	private IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	private IOTIMSDashboardDao iOTIMSDashboardDao;

	@Override
	public Response saveOTReservation(OTReservationDto oTReservationdto, HttpServletRequest request) throws ApplicationException {
		try {
			
			String email = "";
			Date date = TimeZoneComponent.getDateByzone(oTReservationdto.getProcedureOrderMasterDto()!=null ? oTReservationdto.getProcedureOrderMasterDto().get(0)!=null ? oTReservationdto.getProcedureOrderMasterDto().get(0).getUnitId(): null : null);
			String dateTime = GlobalCommonDateUtils.getStringDate(date, "dd-M-yyyy HH:mm:ss");
			Integer proRequestId = 0;
			Integer otreservation = 0;
			if(oTReservationdto.getProReqId()==null)
			{
				if(oTReservationdto.getPatientId() == null || oTReservationdto.getPatientId()==0)
				{
				
					PatientRegistrationDto patientRegistrationDto = new PatientRegistrationDto();
						patientRegistrationDto.setOrganizationId(oTReservationdto.getPatientRegistrationDto().getOrganizationId());
						patientRegistrationDto.setUnitId(oTReservationdto.getPatientRegistrationDto().getUnitId());
						patientRegistrationDto.setPrefixId(oTReservationdto.getPatientRegistrationDto().getPrefixId());
						patientRegistrationDto.setFirstName(oTReservationdto.getPatientRegistrationDto().getFirstName());
						patientRegistrationDto.setMiddleName(oTReservationdto.getPatientRegistrationDto().getMiddleName());
						patientRegistrationDto.setLastName(oTReservationdto.getPatientRegistrationDto().getLastName());
						patientRegistrationDto.setDob(oTReservationdto.getPatientRegistrationDto().getDob());
						patientRegistrationDto.setGenderId(oTReservationdto.getPatientRegistrationDto().getGenderId());
						patientRegistrationDto.setMobileNumber(oTReservationdto.getPatientRegistrationDto().getMobileNumber());
						patientRegistrationDto.setEmail(oTReservationdto.getPatientRegistrationDto().getEmail()!=null ? oTReservationdto.getPatientRegistrationDto().getEmail() : null);
						patientRegistrationDto.setRegistrationTypeId(oTReservationdto.getPatientRegistrationDto().getRegistrationTypeId());
						patientRegistrationDto.setCreatedBy(oTReservationdto.getPatientRegistrationDto().getCreatedBy());
						patientRegistrationDto.setCreatedDate(oTReservationdto.getPatientRegistrationDto().getCreatedDate());
						patientRegistrationDto.setUpdatedBy(oTReservationdto.getPatientRegistrationDto().getUpdatedBy());
						patientRegistrationDto.setUpdatedDate(oTReservationdto.getPatientRegistrationDto().getCreatedDate());
						patientRegistrationDto.setPatientId(oTReservationdto.getPatientId());
						patientRegistrationDto.setStatus('A');
					Response patientRegistraion = patientRegistrationServiceImpl.savePatientRegistration(patientRegistrationDto);
					if(patientRegistraion.getStatus().equals(SUCCESS)) {
						Integer pateintId = (Integer)patientRegistraion.getObject();
						oTReservationdto.setPatientId(pateintId);
						email  = patientRegistrationDto.getEmail();
					
					}else {
						return new Response(ERROR, null, "Patient Already Exist !!", null, null);
					}
				}
				otreservation = 1;
				ProcedureOrderMaster mst = new ProcedureOrderMaster();
				ProcedureReqMaster procedureReqMaster = new ProcedureReqMaster();
				Response procedureReqRes = null;
				for (Iterator iterator = oTReservationdto.getProcedureOrderMasterDto().iterator(); iterator.hasNext();) {
						ProcedureOrderMasterDto procedureOrderMasterDto = (ProcedureOrderMasterDto) iterator.next();
						if(procedureOrderMasterDto.getProcedureReqMasterDto()!=null && procedureOrderMasterDto.getIsPrimarySecondary() == 'Y') {
							
							ProcedureReqMasterDto procedureReqMasterDto = procedureOrderMasterDto.getProcedureReqMasterDto();
								procedureReqMasterDto.setPreferedDateTime(procedureOrderMasterDto.getPreferedDateTime());
								procedureReqMasterDto.setPrimaryProcedureId(procedureOrderMasterDto.getServiceId());
								procedureReqMasterDto.setPatientId(oTReservationdto.getPatientId());
								procedureReqMasterDto.setCreatedDate(dateTime);
								procedureReqMasterDto.setRequestDate(dateTime);
								procedureReqMasterDto.setRequestBy(procedureOrderMasterDto.getCreatedBy());
								procedureReqMasterDto.setProcedureStatusId(procedureOrderMasterDto.getProcedureStatusId());
								procedureReqMasterDto.setOrgId(procedureOrderMasterDto.getOrgId());
								procedureReqMasterDto.setUnitId(procedureOrderMasterDto.getUnitId());
								procedureReqMasterDto.setUpdatedDate(dateTime);
								procedureReqMasterDto.setPreferedTime(procedureOrderMasterDto.getPreferedTime());
								procedureReqRes = iOTReservationDao.saveProcedureReq(procedureReqMasterDto);
								procedureReqMaster = (ProcedureReqMaster) procedureReqRes.getObject();
								proRequestId = procedureReqMaster.getProReqId();
						}
						
					
						if(procedureOrderMasterDto.getProReqId() != 0 ) {
							
							proRequestId = procedureOrderMasterDto.getProReqId();
						}
						
						if(procedureOrderMasterDto.getStatus().equals("A")) {
								procedureOrderMasterDto.setProReqId(proRequestId);
								procedureOrderMasterDto.setCreatedDate(dateTime);
								procedureOrderMasterDto.setOrderDateTime(dateTime);
								procedureOrderMasterDto.setExportedToOtims(false);
								Response res = iOTReservationDao.saveProcedureOrder(procedureOrderMasterDto);
								mst = (ProcedureOrderMaster) res.getObject();
							
								/***********************  Start Save All Procedure Order ***********************/
								
								if(procedureOrderMasterDto.getProcedureOrderConsentList()!=null && procedureOrderMasterDto.getProcedureOrderConsentList().size()>0) {
									
									 iProcedureOrderConsentDao.saveProcedureOrderConsent(procedureOrderMasterDto.getProcedureOrderConsentList(),proRequestId,mst.getProcedureOrderId());
									
								}
								
								if(procedureOrderMasterDto.getProcedureOrderCheckList()!=null && procedureOrderMasterDto.getProcedureOrderCheckList().size()>0) {
									
									 iProcedureOrderChecklistDao.saveProcedureOrderCheckList(procedureOrderMasterDto.getProcedureOrderCheckList(),proRequestId,mst.getProcedureOrderId());
									
								}
								
								if(procedureOrderMasterDto.getProcedureOrderServiceList()!=null && procedureOrderMasterDto.getProcedureOrderServiceList().size()>0) {
									
									  iProcedureOrderServiceDao.saveProcedureOrderServiceList(procedureOrderMasterDto.getProcedureOrderServiceList(),proRequestId,mst.getProcedureOrderId());
									
								}
								
								if(procedureOrderMasterDto.getProcedureOrderStaffList()!=null && procedureOrderMasterDto.getProcedureOrderStaffList().size()>0) {
									
									   iProcedureOrderStaffDao.saveProcedureOrderStaffList(procedureOrderMasterDto.getProcedureOrderStaffList(),proRequestId,mst.getProcedureOrderId());
									
								}
								
								if(procedureOrderMasterDto.getProcedureOrderPatientEducationDto()!=null) {
									
									   Response patieRes = iProcedureOrderPatientEducationDao.saveProcedureOrderPatientEducation(procedureOrderMasterDto.getProcedureOrderPatientEducationDto(),proRequestId,mst.getProcedureOrderId());
									
									   if(patieRes.getObject()!=null) {
												   
												   if(email!=null){
													   procedureOrderMasterDto.getProcedureOrderPatientEducationDto().setEmail(email);
													   iMailingService.sentPatinetEducationTemplate(procedureOrderMasterDto.getProcedureOrderPatientEducationDto());
												   }
										  }
								}
								
								if(procedureOrderMasterDto.getImplantDetailsList()!=null && procedureOrderMasterDto.getImplantDetailsList().size()>0) {
									
									for(ImplantDetailsDto implantList:procedureOrderMasterDto.getImplantDetailsList())
									{ 
										implantList.setPatientId(oTReservationdto.getPatientId());
									}
									iImplantDetailsDao.saveImplantdetails(procedureOrderMasterDto.getImplantDetailsList(),proRequestId,mst.getProcedureOrderId());
									
								}
								
								if(procedureOrderMasterDto.getMedicationDetailsList()!=null && procedureOrderMasterDto.getMedicationDetailsList().size()>0) {
									
									for(MedicationDetailsDto medicationList:procedureOrderMasterDto.getMedicationDetailsList())
									{
										
										medicationList.setPatientId(oTReservationdto.getPatientId());
									}
									
									iMedicationDetailsDao.saveMedicationDetails(procedureOrderMasterDto.getMedicationDetailsList(),mst.getProcedureOrderId());
									
								}
								
								if(procedureOrderMasterDto.getProcedureOrderEquipmentList()!=null && procedureOrderMasterDto.getProcedureOrderEquipmentList().size()>0) {
									
									iProcedureOrderEquipmentDao.saveProcedureOrderEquipment(procedureOrderMasterDto.getProcedureOrderEquipmentList(),proRequestId,mst.getProcedureOrderId());
									
								}
								
								/***********************End Save All Procedure Order ***********************/
	
	
								if (procedureOrderMasterDto.getListCalenderMapperDto() != null && procedureOrderMasterDto.getListCalenderMapperDto().size() > 0) {
									for (Iterator iterator2 = procedureOrderMasterDto.getListCalenderMapperDto().iterator(); iterator2.hasNext();) {
										SurgicalCalenderMapperDto dto = (SurgicalCalenderMapperDto) iterator2.next();
										dto.setProcedureOrderId(mst.getProcedureOrderId());
										dto.setOrganisationId(procedureOrderMasterDto.getOrgId());
										dto.setUnitId(procedureOrderMasterDto.getUnitId());
										dto.setIsDeleted('N');
										dto.setStatus('A');
										dto.setCreatedDate(dateTime);
										dto.setUpdatedDate(dateTime);
										iSurgicalCalendarMasterService.saveCalenderSlotMpper(dto);
									}
								}
							}
				}
				oTReservationdto.getProcedureSchedulingDto().setProReqId(proRequestId);
		}else {
			
			
			otreservation = 1;
			ProcedureOrderMaster mst = new ProcedureOrderMaster();
			ProcedureReqMaster procedureReqMaster = new ProcedureReqMaster();
			Response procedureReqRes = null;
			for (Iterator iterator = oTReservationdto.getProcedureOrderMasterDto().iterator(); iterator.hasNext();) {
					ProcedureOrderMasterDto procedureOrderMasterDto = (ProcedureOrderMasterDto) iterator.next();
					if(procedureOrderMasterDto.getProcedureReqMasterDto()!=null && procedureOrderMasterDto.getIsPrimarySecondary() == 'Y') {
						
						ProcedureReqMasterDto procedureReqMasterDto = procedureOrderMasterDto.getProcedureReqMasterDto();
							procedureReqMasterDto.setPreferedDateTime(procedureOrderMasterDto.getPreferedDateTime());
							procedureReqMasterDto.setPrimaryProcedureId(procedureOrderMasterDto.getServiceId());
							procedureReqMasterDto.setPatientId(oTReservationdto.getPatientId());
							procedureReqMasterDto.setCreatedDate(dateTime);
							procedureReqMasterDto.setRequestDate(dateTime);
							procedureReqMasterDto.setRequestBy(procedureOrderMasterDto.getCreatedBy());
							procedureReqMasterDto.setProcedureStatusId(procedureOrderMasterDto.getProcedureStatusId());
							procedureReqMasterDto.setOrgId(procedureOrderMasterDto.getOrgId());
							procedureReqMasterDto.setUnitId(procedureOrderMasterDto.getUnitId());
							procedureReqMasterDto.setUpdatedDate(dateTime);
							procedureReqMasterDto.setPreferedTime(procedureOrderMasterDto.getPreferedTime());
							if(procedureOrderMasterDto.getProReqId()!=null && procedureOrderMasterDto.getProReqId()!=0) {
								procedureReqMasterDto.setProReqId(procedureOrderMasterDto.getProReqId());
							}
							procedureReqRes = iOTReservationDao.saveProcedureReq(procedureReqMasterDto);
							procedureReqMaster = (ProcedureReqMaster) procedureReqRes.getObject();
							proRequestId = procedureReqMaster.getProReqId();
					}
					
				
					if(procedureOrderMasterDto.getProReqId() != 0 ) {
						
						proRequestId = procedureOrderMasterDto.getProReqId();
					}
					
					if(procedureOrderMasterDto.getStatus().equals("A")) {
							procedureOrderMasterDto.setProReqId(proRequestId);
							procedureOrderMasterDto.setCreatedDate(dateTime);
							procedureOrderMasterDto.setOrderDateTime(dateTime);
							procedureOrderMasterDto.setExportedToOtims(false);
							Response res = iOTReservationDao.saveProcedureOrder(procedureOrderMasterDto);
							mst = (ProcedureOrderMaster) res.getObject();
						
							/***********************  Start Save All Procedure Order ***********************/
							
							if(procedureOrderMasterDto.getProcedureOrderConsentList()!=null && procedureOrderMasterDto.getProcedureOrderConsentList().size()>0) {
								
								 iProcedureOrderConsentDao.saveProcedureOrderConsent(procedureOrderMasterDto.getProcedureOrderConsentList(),proRequestId,mst.getProcedureOrderId());
								
							}
							
							if(procedureOrderMasterDto.getProcedureOrderCheckList()!=null && procedureOrderMasterDto.getProcedureOrderCheckList().size()>0) {
								
								 iProcedureOrderChecklistDao.saveProcedureOrderCheckList(procedureOrderMasterDto.getProcedureOrderCheckList(),proRequestId,mst.getProcedureOrderId());
								
							}
							
							if(procedureOrderMasterDto.getProcedureOrderServiceList()!=null && procedureOrderMasterDto.getProcedureOrderServiceList().size()>0) {
								
								  iProcedureOrderServiceDao.saveProcedureOrderServiceList(procedureOrderMasterDto.getProcedureOrderServiceList(),proRequestId,mst.getProcedureOrderId());
								
							}
							
							if(procedureOrderMasterDto.getProcedureOrderStaffList()!=null && procedureOrderMasterDto.getProcedureOrderStaffList().size()>0) {
								
								   iProcedureOrderStaffDao.saveProcedureOrderStaffList(procedureOrderMasterDto.getProcedureOrderStaffList(),proRequestId,mst.getProcedureOrderId());
								
							}
							
							if(procedureOrderMasterDto.getProcedureOrderPatientEducationDto()!=null) {
								
								   Response patieRes = iProcedureOrderPatientEducationDao.saveProcedureOrderPatientEducation(procedureOrderMasterDto.getProcedureOrderPatientEducationDto(),proRequestId,mst.getProcedureOrderId());
								
								   if(patieRes.getObject()!=null) {
											   
											   if(email!=null){
												   procedureOrderMasterDto.getProcedureOrderPatientEducationDto().setEmail(email);
												   iMailingService.sentPatinetEducationTemplate(procedureOrderMasterDto.getProcedureOrderPatientEducationDto());
											   }
									  }
							}
							
							if(procedureOrderMasterDto.getImplantDetailsList()!=null && procedureOrderMasterDto.getImplantDetailsList().size()>0) {
								
								for(ImplantDetailsDto implantList:procedureOrderMasterDto.getImplantDetailsList())
								{ 
									implantList.setPatientId(oTReservationdto.getPatientId());
								}
								iImplantDetailsDao.saveImplantdetails(procedureOrderMasterDto.getImplantDetailsList(),proRequestId,mst.getProcedureOrderId());
								
							}
							
							if(procedureOrderMasterDto.getMedicationDetailsList()!=null && procedureOrderMasterDto.getMedicationDetailsList().size()>0) {
								
								for(MedicationDetailsDto medicationList:procedureOrderMasterDto.getMedicationDetailsList())
								{
									
									medicationList.setPatientId(oTReservationdto.getPatientId());
								}
								
								iMedicationDetailsDao.saveMedicationDetails(procedureOrderMasterDto.getMedicationDetailsList(),mst.getProcedureOrderId());
								
							}
							
							if(procedureOrderMasterDto.getProcedureOrderEquipmentList()!=null && procedureOrderMasterDto.getProcedureOrderEquipmentList().size()>0) {
								
								iProcedureOrderEquipmentDao.saveProcedureOrderEquipment(procedureOrderMasterDto.getProcedureOrderEquipmentList(),proRequestId,mst.getProcedureOrderId());
								
							}
							
							/***********************End Save All Procedure Order ***********************/

						}else {
							iOTReservationDao.updateAllProcedureOrderDetailsStatus(procedureOrderMasterDto);
						}
			}
			
			
		}
			
			Response res = iOTIMSDashboardService.saveProcedureSchedulingDetails(oTReservationdto.getProcedureSchedulingDto(),otreservation);
		
			if(oTReservationdto.getProReqId()==null)
			{
				oTReservationdto.setProReqId(proRequestId);
				if(res.getStatus().equals(SUCCESS)) {
					OTIMSDashboard dto = (OTIMSDashboard) res.getObject();
					oTReservationdto.setScheduleId(dto.getProcedureScheduleId());
					String otReservationNo = iGlobalUnitMasterDao.getGeneratedNo(0, oTReservationdto.getUnitId(), OT_RESERVATION_NO);
					oTReservationdto.setOtReservationNo(otReservationNo);
					 iOTReservationDao.saveOTReservation(oTReservationdto);
				}
			}else {
				 iOTReservationDao.updateOTReservation(oTReservationdto);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,oTReservationdto);
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getEquipmentTyoeAndAssetItem(int orgId, int unitId) throws ApplicationException {
		try {
			Response res =  iOTReservationDao.getEquipmentTyoeAndAssetItem(orgId,unitId);
			
			List<AssetVO> assetTypelist = new ArrayList<>();
			List<AssetVO> assetTypelistFinal = new ArrayList<>();
				if(res.getListObject().size()>0) {
					assetTypelist = res.getListObject();
					AssetVO asstItem = new AssetVO();
					for (AssetVO assetCategory : assetTypelist) {
						Response res1 =  iOTReservationDao.getAssetAndBatch(assetCategory);
						
						if(res1.getListObject().size()>0){
							asstItem.setAssetCategoryId(assetCategory.getAssetCategoryId());
							asstItem.setAssetList(res1.getListObject());
							assetTypelistFinal.add(asstItem);
						}
					}
				}
				
				return new Response<>(SUCCESS, SUCCESS_CODE, null, assetTypelistFinal, null);
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response updateProReqMasterStatus(Integer proReqId) throws ApplicationException {
		try {
			return iOTReservationDao.updateProReqMasterStatus(proReqId);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response searchOTReservation(ProcedureOrderMasterDto procedureOrderMasterDto) throws ApplicationException {
		try {
			Response res =  iOTReservationDao.searchOTReservation(procedureOrderMasterDto);
			if(res.getListObject().size()>0) {
				List<OTReservationDto> otDtoList = res.getListObject(); 
				for (OTReservationDto dto : otDtoList ) {
					if(dto.getAdmissionId()==null && dto.getEncounterId()==null) {
					Response res1 =  iOTReservationDao.getVisitTypeListByPatientId(dto);
					if(res1.getListObject().size()>0) {
						dto.setVisitTypeList(res1.getListObject());
					}
					}
				}
				
			}
			return res;
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response searchReservedProcedureName(ProcedureOrderMasterDto procedureOrderMasterDto)
			throws ApplicationException {
		try {
			return iOTReservationDao.searchReservedProcedureName(procedureOrderMasterDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response cancelOTReservation(OTReservationDto oTReservationdto) throws ApplicationException {
		try {
			Response procedureOrderRes  = iOTReservationDao.cancelProcedureOrder(oTReservationdto);
			if(procedureOrderRes.getStatus().equals(SUCCESS)) {
				Response res = iOTIMSDashboardService.cancelProcedureSchedulingDetails(oTReservationdto);
				if(res.getStatus().equals(SUCCESS)) {
					return  iOTReservationDao.cancelOTReservation(oTReservationdto);
				}
			}
	} catch (Exception e) {
		logger.error(e.getMessage(), e);
		throw new ServiceException(e.getMessage());
	}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response getOTReservationListByPatientId(OTReservationDto oTReservationdto) throws ApplicationException {
		try {
			return iOTReservationDao.getOTReservationListByPatientId(oTReservationdto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getOTReservationListByPatientIdForDeposite(OTReservationDto oTReservationdto)
			throws ApplicationException {
		try {
			return iOTReservationDao.getOTReservationListByPatientIdForDeposite(oTReservationdto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	@Override
	public Response getOTReservationForOtCalender(ProcedureOrderMasterDto procedureOrderMasterDto)
			throws ApplicationException {
		try {
			Response res =  iOTReservationDao.getOTReservationForOtCalender(procedureOrderMasterDto);
			if(res.getListObject().size()>0) {
				List<OTReservationDto> otDtoList = res.getListObject(); 
				for (OTReservationDto dto : otDtoList ) {
					if(dto.getAdmissionId()==null && dto.getEncounterId()==null) {
					Response res1 =  iOTReservationDao.getVisitTypeListByPatientId(dto);
					if(res1.getListObject().size()>0) {
						dto.setVisitTypeList(res1.getListObject());
					}
					}
				}
				for (OTReservationDto dtod : otDtoList ) {
					ProcedureStaffScheduleDto dto = new ProcedureStaffScheduleDto();
					dto.setProReqId(dtod.getProReqId());
					dto.setOrganizationId(dtod.getOrganizationId());
					dto.setUnitId(dtod.getUnitId());
					
					Response res1dd = iOTIMSDashboardDao.getProcedureSchedulingSurgeonListForCalender(dto);
					if(res1dd != null && res1dd.getListObject().size()>0) {
						dtod.setSurgeonList(res1dd.getListObject());
					}
				}
			}
			return res;
		} catch (Exception e) {
			
			throw e;
		}
	}

}
