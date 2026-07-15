package com.param.lis.transaction.service;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.param.lis.global.common.*;
import com.param.lis.transaction.dao.IClinicalInterventionProcedureDao;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.entity.lis.transaction.LabSampleMaster;
import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.IBillingCommonConstants;
import com.param.global.common.ServiceException;
import com.param.global.dao.IGlobalUnitMasterDao;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.global.dto.LastInvestigationDto;
import com.param.global.dto.PatientConsentMapperDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.opd.dao.IAppointmentSchedulingDao;
import com.param.global.opd.service.IEncounterMasterService;
import com.param.global.org.dao.IGlobalOrganisationMasterDao;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.service.IOrderDetailsMasterService;
import com.param.global.unit.dao.ILabConfigurationDao;
import com.param.global.unit.dto.LabConfigurationDto;
import com.param.lis.global.dto.BarcodeDto;
import com.param.lis.transaction.dao.ILabTransactionDao;
import com.param.lis.transaction.dto.CollectedSampleDto;
import com.param.lis.transaction.dto.LabReportDocumentDetailsDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDtoIP;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "rawtypes", "unchecked" ,"unused"})
public class LabTransactionServiceImpl implements ILabTransactionService, ICommonConstants, IError,ITransactionConstants,IBillingCommonConstants {
	
	@Autowired
	IOrderDetailsMasterService iOrderDetailsMasterService;
	
	@Autowired
	ILabTransactionDao iLabTransactionDao;
	
	@Autowired
	private SessionFactory sessionFactory;
	
	
	@Autowired
	IAppointmentSchedulingDao iAppointmentSchedulingDao;
	
	@Autowired
	IEncounterMasterService iEncounterMasterService;
	
	@Autowired
	ILabConfigurationDao ILabConfigurationDao;
	
	@Autowired
    IGlobalUnitMasterDao iGlobalUnitMasterDao;
	
	@Autowired
	IGlobalOrganisationMasterDao iGlobalOrganisationMasterDao;
	
	@Autowired
	DocUploadUtility docUploadUtility;
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;

	@Autowired
	IClinicalInterventionProcedureDao iClinicalInterventionProcedureDao;

	final static Logger logger = Logger.getLogger(LabTransactionServiceImpl.class);
	
	@Override
	@Transactional
	public Response getPhlebotomyWorklist(Integer orgId, Integer orgUnitId,Integer deptId, Integer offset, Integer recordPerPage,Character isPreOrPostBilling)
			throws ApplicationException {
		try {
			Response<LabSampleMasterDto, Integer> response = iLabTransactionDao.getPhlebotomyWorklist(orgId,
					orgUnitId,deptId, offset, recordPerPage,isPreOrPostBilling);
			List<LabSampleMasterDto> listLabSampleMasterDto = response.getListObject();
			Comparator<LabSampleMasterDto> filteredWorklist = (e1, e2) -> {
				if (e1 == null)
					return (e2 == null) ? 0 : -1;
				if (e2 == null)
					return -1;
				if (e1.getCollectionTime() == null)
					return 0;
				return e1.getCollectionTime().isAfter(e1.getCollectionTime()) ? 1 : -1;
			};
			List<LabSampleMasterDto> result = listLabSampleMasterDto.stream().sorted(filteredWorklist)
					.collect(Collectors.toList());

			return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, result, null);
		} catch (Exception e) {
			
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTotalPhlobotomyRecord(SearchDto searchDto) throws ApplicationException {
		try {
			return iLabTransactionDao.getTotalPhlobotomyRecord(searchDto);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getPhlebotomyWorklistDetails(Integer orgId, Integer orgUnitId, Integer orderId,Integer deptId,Character isPreOrPostBilling)
			throws ApplicationException {
		try {
			return iLabTransactionDao.getPhlebotomyWorklistDetails(orgId, orgUnitId, orderId,deptId,isPreOrPostBilling);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public com.param.global.common.Response sampleCollection(LabSampleMasterDto labSampleMasterDto) throws ApplicationException {
		try {
			
			
			
			Character byContainer = 'Y'; 
			Character bySampleType = 'Y';
			Character byDepartment = 'Y';
			Character barcodeSys = 'Y';
			Date date = TimeZoneComponent.getDateByzone(labSampleMasterDto.getOrgUnitId());
			Set<Integer> orderIds = new HashSet<Integer>();
			Set<Integer> orderDetailsIds = new HashSet<Integer>();
			List<Integer> finalIds = null;
			for (LabSampleDetailsMasterDto dto : labSampleMasterDto.getListLabSampleDetailsMaster()) {
				orderDetailsIds.add(dto.getOrderDetailsId());
			}
			finalIds = new LinkedList<>(orderDetailsIds);
			com.param.global.common.Response res = iOrderDetailsMasterService.renderAndPostRates(finalIds);
			if (res.getStatus().equals(ERROR)) {
				return res;
			}
			
			com.param.global.common.Response res11 =ILabConfigurationDao.getLabConfigurationList(labSampleMasterDto.getOrgId(), labSampleMasterDto.getOrgUnitId());
			
				List<LabConfigurationDto> listLabConfig= new ArrayList<>();
				listLabConfig.addAll(res11.getListObject());
				LabConfigurationDto labdto11 = listLabConfig.get(0);
			
			 Map<Integer, List<LabSampleDetailsMasterDto>>  mapDetails =  new HashMap<Integer,List<LabSampleDetailsMasterDto>>();  
			 
			 if(labdto11.getIsPrebarcodeGenerantion() != null && labdto11.getIsPrebarcodeGenerantion().equals(barcodeSys) ) {
				 
				 Integer count = 1;
					for (LabSampleDetailsMasterDto detailsDto : labSampleMasterDto.getListLabSampleDetailsMaster()) {
						Character barcode = 'Y';
						if(detailsDto.getIsUniqueBarcode() != null && detailsDto.getIsUniqueBarcode().equals(barcode)) {
							List<LabSampleDetailsMasterDto> detailsList = new ArrayList<>();
							detailsList.add(detailsDto);
							mapDetails.put(count,detailsList);  
							count++;
						}
						
					}
					
					List<LabSampleDetailsMasterDto> detailsList = new ArrayList<>();
					for (LabSampleDetailsMasterDto detailsDto : labSampleMasterDto.getListLabSampleDetailsMaster()) {
						Character barcode = 'N';
						if(detailsDto.getIsUniqueBarcode() == null || detailsDto.getIsUniqueBarcode().equals(barcode)) {
							detailsList.add(detailsDto);
						}
					}
					if(detailsList!=null && detailsList.size()>0) {
						mapDetails.put(count,detailsList);  
					}
					
			 }else if(labdto11.getIsPrebarcodeGenerantion() != null && !labdto11.getIsPrebarcodeGenerantion().equals(barcodeSys)){
				 
				 
				 List<String> uniqueBarcodeList = new ArrayList<>();
				 
					for (LabSampleDetailsMasterDto detailsDto : labSampleMasterDto.getListLabSampleDetailsMaster()) {
						
						if(uniqueBarcodeList == null || uniqueBarcodeList.size() == 0) {
							uniqueBarcodeList.add(detailsDto.getSampleBarcode());  
						}else {
							boolean find = false;
							for (String uniq : uniqueBarcodeList) {
								if(detailsDto.getSampleBarcode().equals(uniq)) {
									find = true;
								}
							}
							if(find == false) {
								uniqueBarcodeList.add(detailsDto.getSampleBarcode());  
							}
						}
					}
					Integer count= 1;
					if(uniqueBarcodeList!=null && uniqueBarcodeList.size()>0) {
						for (String uniq : uniqueBarcodeList) {
							
							 List<LabSampleDetailsMasterDto> detailsList = new ArrayList<>();
								for (LabSampleDetailsMasterDto detailsDto : labSampleMasterDto.getListLabSampleDetailsMaster()) {
									
									if(detailsDto.getSampleBarcode().equals(uniq)) {
										detailsList.add(detailsDto);
									}
										
								}
								
								if(detailsList!=null && detailsList.size()>0) {
									mapDetails.put(count,detailsList);  
									count++;
								}
						}
						
					}
				
			 }
			
			
		
			List<BarcodeDto> barcodeDtoList = new ArrayList<>();
			
			
            for (Entry<Integer, List<LabSampleDetailsMasterDto>> entry1 :  mapDetails.entrySet()) {

						LabSampleMasterDto dto = labSampleMasterDto;
						dto.setCreatedDate(date);
						dto.setUpdatedDate(date);
						dto.setSampleGenDatetime(date);
						BigInteger sampleNo = null;
						if(labdto11.getIsPrebarcodeGenerantion() != null && labdto11.getIsPrebarcodeGenerantion().equals(barcodeSys)) {
							String SAMPLENUMBER = iGlobalUnitMasterDao.getGeneratedNo(0, dto.getOrgUnitId(), SAMPLE_NUMBER);
							 sampleNo = (BigInteger) sessionFactory.getCurrentSession()
									.getNamedQuery("GENERATE_SAMPLE_NO").uniqueResult();
							dto.setSampleNo(CommonUtils.numberToString(sampleNo, SAMPLENUMBER != null ? SAMPLENUMBER :SAMPLE_PREFIX, NUMBER_WIDTH));
							dto.setSampleId(CommonUtils.numberToStringWithLength(sampleNo, NUMBER_WIDTH));
						} 
						

						List<LabSampleDetailsMasterDto> labSampleDetailsMasterDto = new ArrayList<>();

						int loopCount = 0;
						for (Iterator iterator = entry1.getValue().iterator(); iterator.hasNext();) {
							LabSampleDetailsMasterDto sampleDetailsDto = (LabSampleDetailsMasterDto) iterator.next();
						   
						    if(sampleDetailsDto.getManualSampleCollectionDateTime() == null) {
						    	sampleDetailsDto.setSampleCollectionDatetime(date.getTime());
								sampleDetailsDto.setSampleGenDatetime(date);
								sampleDetailsDto.setManualSampleCollectionDateTimeInLong(date.getTime());
								
						    }else {
						    	SimpleDateFormat formatter6=new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");  
								Date date1=formatter6.parse(sampleDetailsDto.getManualSampleCollectionDateTime()); 
						    	sampleDetailsDto.setSampleCollectionDatetime(date1.getTime());
								sampleDetailsDto.setSampleGenDatetime(date1);
								sampleDetailsDto.setManualSampleCollectionDateTimeInLong(date.getTime());
						    } 
							
							sampleDetailsDto.setCreatedDate(date);
							sampleDetailsDto.setUpdatedDate(date);
							sampleDetailsDto.setPatientVisitAge(date.getTime());
							sampleDetailsDto.setCreatedBy(dto.getCreatedBy());
							
							if(labdto11.getIsPrebarcodeGenerantion() != null && labdto11.getIsPrebarcodeGenerantion().equals(barcodeSys)) {
								sampleDetailsDto.setSampleNo(sampleNo);
							} 
							
							
							if(loopCount == 0) {
								if(labdto11.getIsPrebarcodeGenerantion() != null && !labdto11.getIsPrebarcodeGenerantion().equals(barcodeSys)){
									dto.setSampleNo(sampleDetailsDto.getSampleBarcode());
									dto.setSampleId(sampleDetailsDto.getSampleBarcode());
									sampleDetailsDto.setSampleNo(new BigInteger(sampleDetailsDto.getSampleBarcode()));
									sampleNo = new BigInteger(sampleDetailsDto.getSampleBarcode());
									loopCount++;
								}
							}else {
								sampleDetailsDto.setSampleNo(sampleNo);
							}
							
							
							labSampleDetailsMasterDto.add(sampleDetailsDto);
						}
						dto.setListLabSampleDetailsMaster(labSampleDetailsMasterDto);
						Response labSampleRes = this.collectSample(labSampleMasterDto);

						if (labSampleRes.getCode().equals(SUCCESS_200) && labSampleRes.getObject() != null) {

							LabSampleMaster labSampleMst = (LabSampleMaster) labSampleRes.getObject();
							
							if(res11.getListObject() != null && res11.getListObject().size()>0) {
								List<LabConfigurationDto> list= new ArrayList<>();
								list.addAll(res11.getListObject());
								LabConfigurationDto labdto = list.get(0);
								
								List<LabSampleDetailsMasterDto> labSampleDetailsList = labSampleRes.getListObject1();
								List<LabSampleDetailsMasterDto> labSampleList = new ArrayList<>();
								labSampleList.addAll(dto.getListLabSampleDetailsMaster());
								for(LabSampleDetailsMasterDto dto1 : labSampleList)
								{
									String key = "";
									if(labdto.getBySampleType().equals(bySampleType) || (!labdto.getBySampleType().equals(bySampleType) && !labdto.getByContainer().equals(byContainer) && !labdto.getByDepartment().equals(byDepartment)))
									{
										key = dto1.getSampleTypeId().toString();
									}
									if(labdto.getByContainer().equals(byContainer))
									{
										key = key.concat(dto1.getContainerId().toString());
									}
									if(labdto.getByDepartment().equals(byDepartment))
									{
										key = key.concat(dto1.getSubDeptId().toString());
									}
									dto1.setBarCodeGenerationKey(key);
									
								}
								
								Map<String, List<LabSampleDetailsMasterDto>> map = labSampleList.stream().collect(Collectors.groupingBy(LabSampleDetailsMasterDto::getBarCodeGenerationKey));

								for(Entry<String, List<LabSampleDetailsMasterDto>> entry4 : map.entrySet())
								{
								
										List<LabSampleDetailsMasterDto> sampleDetailsDto =  entry4.getValue(); 
										
										BarcodeDto barcodeDto = new BarcodeDto();
										barcodeDto.setUhid(labSampleMasterDto.getUhid());
										barcodeDto.setPatientDetails(labSampleMasterDto.getPatientDetails());
										barcodeDto.setPatientId(labSampleMst.getPatientId());
										barcodeDto.setSampleId(labSampleMst.getSampleId());
										barcodeDto.setLabSampleId(labSampleMst.getLabSampleId());
										barcodeDto.setVisitType(GlobalCommonDateUtils.getVisitType(labSampleMst.getVisitTypeId()));
										barcodeDto.setGenerationDateTime(GlobalCommonDateUtils.getStringDate(labSampleMst.getSampleGenDatetime(), "dd/MM/yyyy"));
										barcodeDto.setGenerationTime(GlobalCommonDateUtils.getStringDate(labSampleMst.getSampleGenDatetime(), "HH:mm:ss"));
										
										for (LabSampleDetailsMasterDto sampleDetailsObj : labSampleDetailsList) {
											
											if(sampleDetailsObj.getOrderDetailsId().equals(sampleDetailsDto.get(0).getOrderDetailsId())) {
												barcodeDto.setLabSampleDtlsId(sampleDetailsObj.getLabSampleDtlsId());
											}
										}
										barcodeDto.setDeptName(sampleDetailsDto.get(0).getDeptName());
										barcodeDto.setSampleName(sampleDetailsDto.get(0).getSampleName());
										barcodeDtoList.add(barcodeDto);
									
								}
							}
						}
						
				}
			
			orderIds.add(labSampleMasterDto.getOrderId());
//			CommonUtils.generateSampleEPLString(barcodeDtoList);
				return new com.param.global.common.Response(SUCCESS, SUCCESS_200, SAMPLE_COLLECT_SUCC, barcodeDtoList,orderIds);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}

	@Override
	@Transactional
	public Response getCollectedSample(Integer orgId, Integer orgUnitId, Integer deptId,Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			return iLabTransactionDao.getCollectedSample(orgId, orgUnitId,deptId, offset, recordPerPage);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response sendToCr(List<LabSampleDetailsMasterDto> listLabSampleDetailsMasterDto)
			throws ApplicationException {
		try {
			return iLabTransactionDao.sendToCr(listLabSampleDetailsMasterDto);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}


	@Override
	@Transactional
	public Response getTotalSendToCrRecord(SearchDto searchDto) throws ApplicationException {
		try {
			return iLabTransactionDao.getTotalSendToCrRecord(searchDto);
		} catch (Exception e) {
			
			throw e;
		}
	}

	
	

	@Override
	@Transactional
	public Response v1GetPhlebotomyWorklistDetails(Integer orgId, Integer orgUnitId, Integer orderId)
			throws ApplicationException {
		try {
			return iLabTransactionDao.v1GetPhlebotomyWorklistDetails(orgId, orgUnitId, orderId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchPhlebotomyWorklistPatient(SearchCommonDto searchCommonDto) throws ApplicationException {
		try {
			return iLabTransactionDao.searchPhlebotomyWorklistPatient(searchCommonDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getFilteredPhlebotomyWorklistPatient(SearchDto searchDto) throws ApplicationException {
		try {
			Response res = iLabTransactionDao.getFilteredPhlebotomyWorklistPatient(searchDto);
			 
			if(res.getListObject()!= null && res.getListObject().size()>0){
				List<LabSampleMasterDto> list = res.getListObject();
				List<Integer> orderIds = list.stream().map(LabSampleMasterDto::getOrderId).collect(Collectors.toList());
				Response res1 = iLabTransactionDao.getOrderDetailsData(orderIds);
				
				if(res1.getListObject()!= null && res1.getListObject().size()>0){
					
					List<LabSampleMasterDto> listfor = res1.getListObject();
					
					for (LabSampleMasterDto labSampleMasterDto : list) {
						
						LabSampleMasterDto dummyserviceCount = listfor.stream()
								  .filter(order -> labSampleMasterDto.getOrderId().equals(order.getOrderId()))
								  .findAny()
								  .orElse(null);
						if(dummyserviceCount!=null){
							labSampleMasterDto.setRealServiceCount(dummyserviceCount.getRealServiceCount());
						}else{
							labSampleMasterDto.setRealServiceCount(BigInteger.valueOf(0));
						}
						
						
					}
					
					
				}
				
				if(res1.getListObject1()!= null && res1.getListObject1().size()>0){
					
					List<LabSampleMasterDto> listfor = res1.getListObject1();
					
					for (LabSampleMasterDto labSampleMasterDto : list) {
						
						LabSampleMasterDto dummyserviceCount = listfor.stream()
								  .filter(order -> labSampleMasterDto.getOrderId().equals(order.getOrderId()))
								  .findAny()
								  .orElse(null);
						if(dummyserviceCount!= null){
							labSampleMasterDto.setPriorityId(2);
						}else{
							labSampleMasterDto.setPriorityId(1);
						}
						
					}
					
				}else{
					
					for (LabSampleMasterDto labSampleMasterDto : list) {
						
						labSampleMasterDto.setPriorityId(1);
						
					}
					
				}
				
				
			}
			
			/*
			 * List<LabSampleMasterDto> list = res.getListObject();
			 * 
			 * for(LabSampleMasterDto dto : list) {
			 * dto.setTestDesc(iLabTransactionDao.getTestName(dto.getOrderId(),dto.
			 * getOrgUnitId())); } res.setListObject(list);
			 */
			 return res;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchSendToCRPatientlist(SearchCommonDto searchCommonDto) throws ApplicationException {
		try {
			return iLabTransactionDao.searchSendToCRPatientlist(searchCommonDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getFilteredSendToCRPatientlist(SearchDto searchDto) throws ApplicationException {
		try {
			Response res =  iLabTransactionDao.getFilteredSendToCRPatientlist(searchDto);
			Integer count = 0;
			if(res.getListObject().size()>0) {
				Response res1 =  iLabTransactionDao.getFilteredSendToCRPatientlistCount(searchDto);
				  if(res1.getObject()!=null ) {
					  count  = (Integer)res1.getObject();
					  return new Response(SUCCESS, SUCCESS_200_MESSAGE,null,res.getListObject() ,count);
				  }
			}
				  return new Response(SUCCESS, SUCCESS_200_MESSAGE,"No Records Found.",res.getListObject() ,count);
			
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response searchLabGenralLabSampleNo(SearchCommonDto searchCommonDto) throws ApplicationException {
		try {
			return iLabTransactionDao.searchLabGenralLabSampleNo(searchCommonDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getInPatientOrders(int orgId, int orgUnitId, int patientId, int admissionId , int deptId)
			throws ApplicationException {
		try {
			return iLabTransactionDao.getInPatientOrders(orgId,orgUnitId,patientId,admissionId,deptId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public com.param.global.common.Response sampleCollectionIp(List<LabSampleMasterDto> listLabSampleMasterDto) throws ApplicationException 
	{
		try {
			
			Character byContainer = 'Y'; 
			Character bySampleType = 'Y';
			Character byDepartment = 'Y';
			Character barcodeSys = 'Y';
			Date date = TimeZoneComponent.getDateByzone(listLabSampleMasterDto!=null && listLabSampleMasterDto.size() > 0 
					? listLabSampleMasterDto.get(0).getOrgUnitId() : null);
			 List<BarcodeDto> barcodeDtoList = new ArrayList<>();
			 List<Integer> finalIds = new LinkedList<>(); 
			 Set<Integer> orderIds = new HashSet<>(); 
			 Set<Integer> orderDetailsIds = new HashSet<Integer>();
			 for (LabSampleMasterDto labSampleMasterDto : listLabSampleMasterDto) 
			 {
				 for(LabSampleDetailsMasterDto dto : labSampleMasterDto.getListLabSampleDetailsMaster())
				 	{
				 		orderDetailsIds.add(dto.getOrderDetailsId());
				 	}
			 }
			 finalIds = new LinkedList<>(orderDetailsIds);
			 com.param.global.common.Response res = iOrderDetailsMasterService.renderAndPostRates(finalIds);
	    	 if(res.getStatus().equals(ERROR))
	    	 {
	    		 return res;
	    	 }
			 
	    	 com.param.global.common.Response res11 = ILabConfigurationDao.getLabConfigurationList(listLabSampleMasterDto.get(0).getOrgId(),listLabSampleMasterDto.get(0).getOrgUnitId());
	    	 List<LabConfigurationDto> listLabConfig= new ArrayList<>();
				listLabConfig.addAll(res11.getListObject());
				LabConfigurationDto labdto11 = listLabConfig.get(0);
			for (LabSampleMasterDto labSampleMasterDto : listLabSampleMasterDto) {
				
				 Map<Integer, List<LabSampleDetailsMasterDto>>  mapDetails =  new HashMap<Integer,List<LabSampleDetailsMasterDto>>();  
				 if(labdto11.getIsPrebarcodeGenerantion() != null && labdto11.getIsPrebarcodeGenerantion().equals(barcodeSys) ) {
					 
					 Integer count = 1;
						for (LabSampleDetailsMasterDto detailsDto : labSampleMasterDto.getListLabSampleDetailsMaster()) {
							Character barcode = 'Y';
							if(detailsDto.getIsUniqueBarcode() != null && detailsDto.getIsUniqueBarcode().equals(barcode)) {
								List<LabSampleDetailsMasterDto> detailsList = new ArrayList<>();
								detailsList.add(detailsDto);
								mapDetails.put(count,detailsList);  
								count++;
							}
							
						}
						
						List<LabSampleDetailsMasterDto> detailsList = new ArrayList<>();
						for (LabSampleDetailsMasterDto detailsDto : labSampleMasterDto.getListLabSampleDetailsMaster()) {
							Character barcode = 'N';
							if(detailsDto.getIsUniqueBarcode() == null || detailsDto.getIsUniqueBarcode().equals(barcode)) {
								detailsList.add(detailsDto);
							}
						}
						if(detailsList!=null && detailsList.size()>0) {
							mapDetails.put(count,detailsList);  
						}
						
				 }else if(labdto11.getIsPrebarcodeGenerantion() != null && !labdto11.getIsPrebarcodeGenerantion().equals(barcodeSys)){
					 
					 
					 List<String> uniqueBarcodeList = new ArrayList<>();
					 
						for (LabSampleDetailsMasterDto detailsDto : labSampleMasterDto.getListLabSampleDetailsMaster()) {
							
							if(uniqueBarcodeList == null || uniqueBarcodeList.size() == 0) {
								uniqueBarcodeList.add(detailsDto.getSampleBarcode());  
							}else {
								boolean find = false;
								for (String uniq : uniqueBarcodeList) {
									if(detailsDto.getSampleBarcode().equals(uniq)) {
										find = true;
									}
								}
								if(find == false) {
									uniqueBarcodeList.add(detailsDto.getSampleBarcode());  
								}
							}
						}
						Integer count= 1;
						if(uniqueBarcodeList!=null && uniqueBarcodeList.size()>0) {
							for (String uniq : uniqueBarcodeList) {
								
								 List<LabSampleDetailsMasterDto> detailsList = new ArrayList<>();
									for (LabSampleDetailsMasterDto detailsDto : labSampleMasterDto.getListLabSampleDetailsMaster()) {
										
										if(detailsDto.getSampleBarcode().equals(uniq)) {
											detailsList.add(detailsDto);
										}
											
									}
									
									if(detailsList!=null && detailsList.size()>0) {
										mapDetails.put(count,detailsList);  
										count++;
									}
							}
							
						}
					
				 }
				
				    	 for (Entry<Integer, List<LabSampleDetailsMasterDto>> entry2 :  mapDetails.entrySet()) 
					        {
							    LabSampleMasterDto dto =  labSampleMasterDto;
							    dto.setCreatedDate(date);
							    dto.setUpdatedDate(date);
							    dto.setSampleGenDatetime(date);
							    BigInteger sampleNo = null;
								if(labdto11.getIsPrebarcodeGenerantion() != null && labdto11.getIsPrebarcodeGenerantion().equals(barcodeSys)) {
									String SAMPLENUMBER = iGlobalUnitMasterDao.getGeneratedNo(0, dto.getOrgUnitId(), SAMPLE_NUMBER);
									 sampleNo = (BigInteger) sessionFactory.getCurrentSession()
											.getNamedQuery("GENERATE_SAMPLE_NO").uniqueResult();
									dto.setSampleNo(CommonUtils.numberToString(sampleNo, SAMPLENUMBER != null ? SAMPLENUMBER :SAMPLE_PREFIX, NUMBER_WIDTH));
									dto.setSampleId(CommonUtils.numberToStringWithLength(sampleNo, NUMBER_WIDTH));
								} 
								
							    List<LabSampleDetailsMasterDto> labSampleDetailsMasterDto = new ArrayList<>();
							    int loopCount = 0;
							    for (Iterator iterator = entry2.getValue().iterator(); iterator.hasNext();) 
							    {
							    	LabSampleDetailsMasterDto sampleDetailsDto = (LabSampleDetailsMasterDto) iterator.next();
							    	sampleDetailsDto.setSampleCollectionDatetime(date.getTime());
							    	sampleDetailsDto.setSampleGenDatetime(date);
							    	sampleDetailsDto.setCreatedBy(dto.getCreatedBy());
							    	sampleDetailsDto.setPatientVisitAge(date.getTime());
							    	if(labdto11.getIsPrebarcodeGenerantion() != null && labdto11.getIsPrebarcodeGenerantion().equals(barcodeSys)) {
										sampleDetailsDto.setSampleNo(sampleNo);
									} 
							    	
							    	if(loopCount == 0) {
										if(labdto11.getIsPrebarcodeGenerantion() != null && !labdto11.getIsPrebarcodeGenerantion().equals(barcodeSys)){
											dto.setSampleNo(sampleDetailsDto.getSampleBarcode());
											dto.setSampleId(sampleDetailsDto.getSampleBarcode());
											sampleDetailsDto.setSampleNo(new BigInteger(sampleDetailsDto.getSampleBarcode()));
											sampleNo = new BigInteger(sampleDetailsDto.getSampleBarcode());
											loopCount++;
										}
									}else {
										sampleDetailsDto.setSampleNo(sampleNo);
									}
							    	
							    	labSampleDetailsMasterDto.add(sampleDetailsDto);
							    	
								}
							    dto.setListLabSampleDetailsMaster(labSampleDetailsMasterDto);
							     Response labSampleRes =  this.collectSample(labSampleMasterDto);
							   
							     if(labSampleRes.getCode().equals(SUCCESS_200)&&labSampleRes.getObject()!=null)
							     {
							    	 LabSampleMaster labSampleMst = (LabSampleMaster) labSampleRes.getObject();
							    	 
							    	 if(res11.getListObject() != null && res11.getListObject().size()>0) {
											List<LabConfigurationDto> list= new ArrayList<>();
											list.addAll(res11.getListObject());
											LabConfigurationDto labdto = list.get(0);
											List<LabSampleDetailsMasterDto> labSampleDetailsList = labSampleRes.getListObject1();
											List<LabSampleDetailsMasterDto> labSampleList = new ArrayList<>();
											labSampleList.addAll(dto.getListLabSampleDetailsMaster());
											for(LabSampleDetailsMasterDto dto1 : labSampleList)
											{
												String key = "";
												if(labdto.getBySampleType().equals(bySampleType) || (!labdto.getBySampleType().equals(bySampleType) && !labdto.getByContainer().equals(byContainer) && !labdto.getByDepartment().equals(byDepartment)))
												{
													key = dto1.getSampleTypeId().toString();
												}
												if(labdto.getByContainer().equals(byContainer))
												{
													key = key.concat(dto1.getContainerId().toString());
												}
												if(labdto.getByDepartment().equals(byDepartment))
												{
													key = key.concat(dto1.getSubDeptId().toString());
												}
												dto1.setBarCodeGenerationKey(key);
												
											}
											
											Map<String, List<LabSampleDetailsMasterDto>> map = labSampleList.stream().collect(Collectors.groupingBy(LabSampleDetailsMasterDto::getBarCodeGenerationKey));

											for(Entry<String, List<LabSampleDetailsMasterDto>> entry4 : map.entrySet())
											{
											
													List<LabSampleDetailsMasterDto> sampleDetailsDto =  entry4.getValue(); 
													
													BarcodeDto barcodeDto = new BarcodeDto();
													barcodeDto.setUhid(labSampleMasterDto.getUhid());
													barcodeDto.setPatientDetails(labSampleMasterDto.getPatientDetails());
													barcodeDto.setPatientId(labSampleMst.getPatientId());
													barcodeDto.setSampleId(labSampleMst.getSampleId());
													barcodeDto.setLabSampleId(labSampleMst.getLabSampleId());
													barcodeDto.setVisitType(GlobalCommonDateUtils.getVisitType(labSampleMst.getVisitTypeId()));
													barcodeDto.setGenerationDateTime(GlobalCommonDateUtils.getStringDate(labSampleMst.getSampleGenDatetime(), "dd/MM/yyyy"));
													barcodeDto.setGenerationTime(GlobalCommonDateUtils.getStringDate(labSampleMst.getSampleGenDatetime(), "HH:mm:ss"));
													
													for (LabSampleDetailsMasterDto sampleDetailsObj : labSampleDetailsList) {
														
														if(sampleDetailsObj.getOrderDetailsId().equals(sampleDetailsDto.get(0).getOrderDetailsId())) {
															barcodeDto.setLabSampleDtlsId(sampleDetailsObj.getLabSampleDtlsId());
														}
													}
													barcodeDto.setDeptName(sampleDetailsDto.get(0).getDeptName());
													barcodeDto.setSampleName(sampleDetailsDto.get(0).getSampleName());
													barcodeDtoList.add(barcodeDto);
												
											}
							        }
							     }
					     }
				//}
			    orderIds.add(labSampleMasterDto.getOrderId());
			}
			for(Integer i : orderIds)
			{
				this.updateOrderStatus(i);
			}
//			CommonUtils.generateSampleEPLString(barcodeDtoList);
			return new com.param.global.common.Response(SUCCESS, SUCCESS_200, SAMPLE_COLLECT_SUCC, barcodeDtoList, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response<LabSampleDetailsMasterDtoIP, Integer> getInPatientRadiologyOrders(int orgId, int orgUnitId,int admissionId)
			throws ApplicationException {
		try {
			return iLabTransactionDao.getInPatientRadiologyOrders(orgId,orgUnitId,admissionId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response reprintBarcode(List<String> sampleIds, Integer labSampleDetailsId) throws ApplicationException {
		try {
			return iLabTransactionDao.reprintBarcode(sampleIds,labSampleDetailsId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response collectSample(LabSampleMasterDto labSampleMasterDto) throws ApplicationException {
		try {
			return iLabTransactionDao.sampleCollection(labSampleMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateOrderStatus(Integer orderId) throws ApplicationException {
		try {
			return iLabTransactionDao.updateOrderStatus(orderId);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response processCyclicServiceAppointment(List<SlotMasterDto> slotMasterDtoList) throws ApplicationException {
		try {
			for(SlotMasterDto dto:slotMasterDtoList) {
				//render lab service
				if(dto.getAdmissionId()!=null && dto.getServiceSpecialityId().equals(dto.getLabSpecialityId()) 
				&& (dto.getDoctorId()!=null && dto.getSpecialityId()!=null && dto.getIsDoctorShareReq()!=null && dto.getIsDoctorShareReq().equals('Y')
				|| dto.getIsDoctorShareReq()!=null && dto.getIsDoctorShareReq().equals('N') ) ) {
					Response res=iLabTransactionDao.getInPatientOrders(dto.getOrganizationId(),dto.getUnitId(),dto.getPatientId(),dto.getAdmissionId(),dto.getLabSpecialityId());
					List<LabSampleDetailsMasterDtoIP> detailsMasterDtoIPs=res.getListObject();
					LabSampleDetailsMasterDtoIP labSampleDetailsMasterDtoIP = detailsMasterDtoIPs.stream()
							  .filter(orderDet -> orderDet.getOrderDetailsId().equals(dto.getOrderDetailsId()))
							  .findAny()
							  .orElse(null);
				}
				  iAppointmentSchedulingDao.rescheduleCyclicServiceAppointment(dto);
			}
			  return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getPerformTestId(CollectedSampleDto collectedSampleDto) throws ApplicationException {
		try {
			return iLabTransactionDao.getPerformTestId(collectedSampleDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getPatHistoryByEncounterId(LabSampleMasterDto labSampleMasterDto) throws ApplicationException {
		try {
			return iLabTransactionDao.getPatHistoryByEncounterId(labSampleMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updatePatHistoryByEncounterId(EncounterMasterDto encounterMasterDto) {
		try {
			return iLabTransactionDao.updatePatHistoryByEncounterId(encounterMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getPhlebotomistList(LabSampleMasterDto labSampleMasterDto) throws ApplicationException {
		try {
			return iLabTransactionDao.getPhlebotomistList(labSampleMasterDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response userAllocationHomeCollectionSearch(SearchDto searchDto) throws ApplicationException {
		try {
			Response res = iLabTransactionDao.userAllocationHomeCollectionSearch(searchDto);
			 
			 List<LabSampleMasterDto> list = res.getListObject();
			 
			 for(LabSampleMasterDto dto : list)
			 {
				 dto.setTestDesc(iLabTransactionDao.getTestName(dto.getOrderId(),dto.getOrgUnitId()));
			 }
			 res.setListObject(list);
			 return res;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response labSampleHomeCollectionSearch(SearchDto searchDto) throws ApplicationException {
		try {
			Response res = iLabTransactionDao.labSampleHomeCollectionSearch(searchDto);
			 
			 List<LabSampleMasterDto> list = res.getListObject();
			 
			 for(LabSampleMasterDto dto : list)
			 {
				 dto.setTestDesc(iLabTransactionDao.getTestName(dto.getOrderId(),dto.getOrgUnitId()));
			 }
			 res.setListObject(list);
			 return res;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getHomeColletionTestList(Integer orgId, Integer orgUnitId, Integer orderId, Integer deptId, Character isPreOrPostBilling) throws ApplicationException {
		try {
			return iLabTransactionDao.getHomeColletionTestList(orgId, orgUnitId, orderId,deptId,isPreOrPostBilling);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateLabSampleDetails(SearchDto searchDto) throws ApplicationException {
		try {
			return iLabTransactionDao.updateLabSampleDetails(searchDto);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateAssignUnitAndTestId(LabSampleDetailsMasterDto dto) throws ApplicationException {
		try {
			return iLabTransactionDao.updateAssignUnitAndTestId(dto);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getPatientLastHistoryOfTest(LastInvestigationDto dto) throws ApplicationException {
		try {
			Response res = iLabTransactionDao.getPatientLastHistoryOfTest(dto);
			List<LastInvestigationDto> lastInvestigationList = new ArrayList<>();
		
			List<LastInvestigationDto> dateList1 = new ArrayList<>();
			List<LastInvestigationDto> parameterList1 = new ArrayList<>();

			LastInvestigationDto finalNewDto = new LastInvestigationDto();

			if (res.getListObject() != null && res.getListObject().size() > 0) {
				lastInvestigationList = res.getListObject();
				for (LastInvestigationDto lastInvestigationDto : lastInvestigationList) {


					if (dateList1 != null && dateList1.size() > 0) {
						LastInvestigationDto dateExits = dateList1.stream()
								.filter(d -> d.getReleaseDate().equals(lastInvestigationDto.getReleaseDate())).findAny()
								.orElse(null);
						if (dateExits == null) {
							LastInvestigationDto dateDto = new LastInvestigationDto();
							dateDto.setReleaseDate(lastInvestigationDto.getReleaseDate());
							dateList1.add(dateDto);
						}
					} else {
						LastInvestigationDto dateDto = new LastInvestigationDto();
						dateDto.setReleaseDate(lastInvestigationDto.getReleaseDate());
						dateList1.add(dateDto);
					}

					if (parameterList1 != null && parameterList1.size() > 0) {

						LastInvestigationDto paraExits = parameterList1.stream()
								.filter(p -> p.getParameterId().equals(lastInvestigationDto.getParameterId())).findAny()
								.orElse(null);

						if (paraExits == null) {
							//unique parameter list
							LastInvestigationDto paramDto = new LastInvestigationDto();
							paramDto.setParameterId(lastInvestigationDto.getParameterId());
							paramDto.setParamName(lastInvestigationDto.getParamName());
							paramDto.setReleaseDate(lastInvestigationDto.getReleaseDate());
							paramDto.setParamUnit(lastInvestigationDto.getParamUnit());
							parameterList1.add(paramDto);
						}
					} else {
						LastInvestigationDto paramDto = new LastInvestigationDto();
						paramDto.setParameterId(lastInvestigationDto.getParameterId());
						paramDto.setParamName(lastInvestigationDto.getParamName());
						paramDto.setReleaseDate(lastInvestigationDto.getReleaseDate());
						paramDto.setParamUnit(lastInvestigationDto.getParamUnit());
						parameterList1.add(paramDto);
					}

				}
				
				List<LastInvestigationDto> finalList = new ArrayList<>();
				for (LastInvestigationDto lastInvestigationParamDto : lastInvestigationList) {
					//for (LastInvestigationDto dtDto : dateList1) {
						for (LastInvestigationDto paramDto : parameterList1) {
							if (paramDto.getParameterId().equals(lastInvestigationParamDto.getParameterId())) {

								if (paramDto.getReleaseDate().equals(lastInvestigationParamDto.getReleaseDate())) {
									int occurs = 0;
									if(finalList!=null && finalList.size()>0) {
										for (LastInvestigationDto lastInvestigationDto : finalList) {
											String finalResultDesc = "";
											if(lastInvestigationDto.getParameterId().equals(lastInvestigationParamDto.getParameterId()) 
													&& lastInvestigationDto.getReleaseDate().equals(lastInvestigationParamDto.getReleaseDate())) {
												finalResultDesc = lastInvestigationDto.getFinalResult() +","+ lastInvestigationParamDto.getFinalResult();
												lastInvestigationDto.setFinalResult(finalResultDesc);
												occurs ++;
											}
										}
										if(occurs == 0) {
											finalList.add(lastInvestigationParamDto);
										}
										
									}else {
										finalList.add(lastInvestigationParamDto);
									}
									
								}else {
									finalList.add(lastInvestigationParamDto);
								}
							}
						}
					//}
						
				}

				finalNewDto.setParameterList(parameterList1);
				finalNewDto.setReleaseDateList(dateList1);
				finalNewDto.setLastInvestigationList(finalList);
			}

			return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, null, finalNewDto);
		
		
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getPhlebotomyWorklistType(SearchDto searchDto) throws ApplicationException {
		try {
			Response res = iLabTransactionDao.getPhlebotomyWorklistType(searchDto);
			 
			if(res.getListObject()!= null && res.getListObject().size()>0){
				List<LabSampleMasterDto> list = res.getListObject();
				List<Integer> orderIds = list.stream().map(LabSampleMasterDto::getOrderId).collect(Collectors.toList());
				Response res1 = iLabTransactionDao.getOrderDetailsData(orderIds);
				
				if(res1.getListObject()!= null && res1.getListObject().size()>0){
					
					List<LabSampleMasterDto> listfor = res1.getListObject();
					
					for (LabSampleMasterDto labSampleMasterDto : list) {
						
						LabSampleMasterDto dummyserviceCount = listfor.stream()
								  .filter(order -> labSampleMasterDto.getOrderId().equals(order.getOrderId()))
								  .findAny()
								  .orElse(null);
						
						if(dummyserviceCount!=null){
							labSampleMasterDto.setRealServiceCount(dummyserviceCount.getRealServiceCount());
						}else{
							labSampleMasterDto.setRealServiceCount(BigInteger.valueOf(0));
						}
						
					}
					
					
				}
				
				if(res1.getListObject1()!= null && res1.getListObject1().size()>0){
					
					List<LabSampleMasterDto> listfor = res1.getListObject1();
					
					for (LabSampleMasterDto labSampleMasterDto : list) {
						
						LabSampleMasterDto dummyserviceCount = listfor.stream()
								  .filter(order -> labSampleMasterDto.getOrderId().equals(order.getOrderId()))
								  .findAny()
								  .orElse(null);
						if(dummyserviceCount!= null){
							labSampleMasterDto.setPriorityId(2);
						}else{
							labSampleMasterDto.setPriorityId(1);
						}
						
					}
					
				}else{
					
					for (LabSampleMasterDto labSampleMasterDto : list) {
						
						labSampleMasterDto.setPriorityId(1);
						
					}
					
				}
			}
			/*
			 * List<LabSampleMasterDto> list = res.getListObject();
			 * 
			 * for(LabSampleMasterDto dto : list) {
			 * dto.setTestDesc(iLabTransactionDao.getTestName(dto.getOrderId(),dto.
			 * getOrgUnitId())); } res.setListObject(list);
			 */
			 return res;
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getLabSortingList() throws ApplicationException {
		try {			
			return iLabTransactionDao.getLabSortingList();	
			
		} catch (Exception e) {
			logger.error("Exception", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getPhlebotomyWorklistOnNewOPBill(List<SearchDto> searchDto) throws ApplicationException {
			try {
					
					return iLabTransactionDao.getPhlebotomyWorklistOnNewOPBill(searchDto);
				
			} catch (Exception e) {
				logger.error("Exection", e);
				throw e;
			}
	}

	@Override
	@Transactional
	public Response printBarcodeLab(RadiologyOrdersListDto dto) {
		try {
			PatientConsentMapperDto consentMapperDto = new PatientConsentMapperDto();
			consentMapperDto.setPatientId(dto.getPatientId());
		    consentMapperDto.setEncounterId(dto.getEncounterId());
		    consentMapperDto.setAdmissionId(dto.getAdmissionId());
//		    consentMapperDto.setOrderDetailsId(dto.getOrderDetailsId());	
			//consentMapperDto.setTemplateDesc(dto.getTemplateDesc());
			String finalPRNFormat="";
			if(dto.getSampleIds()!=null && dto.getSampleIds().size()>0) {
				
				Response response = iLabTransactionDao.reprintBarcode(dto.getSampleIds(),dto.getLabSampleDetailsId());
				if(response.getListObject()!=null && response.getListObject().size()>0) {	
					ListIterator<BarcodeDto> listItr=response.getListObject().listIterator();
					while(listItr.hasNext()) {
						BarcodeDto barcodeRes=(BarcodeDto)listItr.next();
						consentMapperDto.setTemplateDesc(dto.getTemplateDesc());
						consentMapperDto.setLabSampleDtlsId(barcodeRes.getLabSampleDtlsId());
						 com.param.global.common.Response res= iEncounterMasterService.appendCensentData(consentMapperDto);
						  if(res.getStatus().equals("success") && res.getObject()!=null) {
							  PatientConsentMapperDto resDto=(PatientConsentMapperDto)res.getObject();
							  if(finalPRNFormat!="" && finalPRNFormat!=null) {
								  finalPRNFormat= finalPRNFormat+"\n"+resDto.getTemplateDesc();
							  }else {
								  finalPRNFormat=resDto.getTemplateDesc();
							  }
						  }
					}
				}
				
			}
			dto.setTemplateDesc(finalPRNFormat);
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, dto);
		} catch (Exception e) {
			throw e;
		}
	}
	
	
	@Override
	@Transactional
	public Response checkForOrderCompletion(SearchDto searchDto) throws ApplicationException {
		try {
			return iLabTransactionDao.checkForOrderCompletion(searchDto);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response uploadLabReportDocument(LabReportDocumentDetailsDto labReportDocumentDetailsDto)
			throws ApplicationException {
		try {
			if (labReportDocumentDetailsDto.getUnitId() > 0) 
			{
				com.param.global.common.Response orgRes = iGlobalOrganisationMasterDao.getOrganiazationFolderPath(labReportDocumentDetailsDto.getOrganizationId());
				OrganizationMasterDto organizationMasterDto = (OrganizationMasterDto) orgRes.getObject();
				String docPath = "";
				
					docPath = docUploadUtility.uploadLabReportPatientDocuments(labReportDocumentDetailsDto.getFile(),
							organizationMasterDto.getOrganizationFolder(), labReportDocumentDetailsDto.getOrganizationId(),
							labReportDocumentDetailsDto.getUnitId(),labReportDocumentDetailsDto.getLabSampleDtlsId(),labReportDocumentDetailsDto.getTestId());
				
				labReportDocumentDetailsDto.setFilePath(docPath);
				iLabTransactionDao.uploadLabReportDocument(labReportDocumentDetailsDto);
			}
			return new Response<>(SUCCESS, "200", null, null, null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getLabReportDocumentDetailsByTest(SearchDto searchDto) throws ApplicationException {
		try {
			return iLabTransactionDao.getLabReportDocumentDetailsByTest(searchDto);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response validateBarcode(LabSampleDetailsMasterDto searchDto) throws ApplicationException {
		try {
			return iLabTransactionDao.validateBarcode(searchDto);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getCompanyOrInsuranceNameById(LabSampleMasterDto dto) throws ApplicationException {
		try {
			return iLabTransactionDao.getCompanyOrInsuranceNameById(dto);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getBinaryStringOfLABReport(SearchDto dto) throws Exception {
        try {
            String json = "{ "
		            + " \"appName\": \"HIS\","
		            + " \"appSecretKey\": \"f306b6eccd9d5069d2dc71afe139271a\","
		            + " \"reportDesignFilePath\": \"Multireportprint_listview.rpt\","
		            + " \"reportFormat\": \"PDF\","
		            + " \"exportFileName\": \"pathologyReport\","
		            + " \"reportParams\": [ "
		            + "  	{ "
			        + "    		\"paramName\": \"lab_sample_dtls_id\","
			        + "     	\"paramType\": \"String\","
			        + "     	\"paramValue\": "+dto.getLabSampleDtlsIds().toString().replace("[", "").replace("]", "")
		            + "  	}, "
		            + "  	{ "
				    + "     	\"paramName\": \"isOriginal\","
				    + "     	\"paramType\": \"Integer\","
				    + "   		\"paramValue\": 1 "
			        + "  	 },"
			        + " 	 { "
				    + "        	\"paramName\": \"isHeader\","
				    + "        	\"paramType\": \"Integer\","
				    + "        	\"paramValue\": 0 "
			        + "  	} "
		            + "	]"
		            +"}";
                ByteArrayOutputStream byteOutPutStream = new ByteArrayOutputStream();
                   CloseableHttpClient httpClient = HttpClientBuilder.create().build();
                   String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGeneration(dto.getOrgId(), dto.getOrgUnitId());

                   if (API_URL == null)
                       return null;

                   HttpPost post = new HttpPost(API_URL);
                   post.addHeader("Content-Type", "application/json");
                   post.setEntity(new StringEntity(json));

                   HttpResponse result = httpClient.execute(post);
                   HttpEntity resEntity = result.getEntity();
                   resEntity.writeTo(byteOutPutStream);
                   byteOutPutStream.close();
                   return new Response(SUCCESS, "200", null, null, byteOutPutStream.toByteArray());
       } catch (Exception e) {
    	   logger.error("Exection", e);
			throw e;
       }
	}
	
	@Override
	@Transactional
	public Response getDatesByLabDetialsId(LabSampleDetailsMasterDto dto) throws ApplicationException {
		try {
			return iLabTransactionDao.getDatesByLabDetialsId(dto);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateDatesByLabDetialsId(LabSampleDetailsMasterDto dto) throws ApplicationException {
		try {
			return iLabTransactionDao.updateDatesByLabDetialsId(dto);
		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	@Override
	@Transactional
	public Response printBarcodeLab1(List<RadiologyOrdersListDto> list) {
		try {
			PatientConsentMapperDto consentMapperDto = new PatientConsentMapperDto();

			for(RadiologyOrdersListDto dto : list){
				consentMapperDto.setBatchId(dto.getBatchId());
				String finalPRNFormat="";
				if(dto.getBatchId()!=null && dto.getBatchId()>0) {
					consentMapperDto.setTemplateDesc(dto.getTemplateDesc());
					consentMapperDto.setPrnCount(dto.getPrnCount());
					consentMapperDto.setBonusBatchFlag(dto.getBonusBatchFlag()!=null ? dto.getBonusBatchFlag(): false);
					consentMapperDto.setBarcodeScreen(dto.getBarcodeScreen()!=null ? dto.getBarcodeScreen(): false);
					consentMapperDto.setGrnId(dto.getGrnId() != null ? dto.getGrnId():"0");
					com.param.global.common.Response res= iEncounterMasterService.appendCensentData(consentMapperDto);
					if(res.getStatus().equals("success") && res.getObject()!=null) {
						PatientConsentMapperDto resDto=(PatientConsentMapperDto)res.getObject();
						if(finalPRNFormat!="" && finalPRNFormat!=null) {
							finalPRNFormat= finalPRNFormat+"\n"+resDto.getTemplateDesc();
						}else {
							finalPRNFormat=resDto.getTemplateDesc();
						}
					}
				}
				dto.setTemplateDesc(finalPRNFormat);
			}

			return new Response(SUCCESS, null, COMMON_SUCCESS, list, null);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional
	public Response<LabSampleMasterDto, Integer> PhlebotomyWorklistDetails(SearchDto searchDto)
			throws ApplicationException {
		try {
		
			Response res = iLabTransactionDao.PhlebotomyWorklistDetails(searchDto);
			 
			 if(res.getObject() != null) {
				 LabSampleMasterDto dto = (LabSampleMasterDto) res.getObject();
				 
				 dto.setTestDesc(iLabTransactionDao.getTestNamePhlebotomy(dto.getOrderId(),searchDto.getOrgUnitId(),searchDto));
				 Response res1 =  iLabTransactionDao.getCompanyOrInsuranceNameById(dto);
				 LabSampleMasterDto pay =  (LabSampleMasterDto) res1.getObject();
				 dto.setCompanyDesc(pay.getCompanyDesc());
			 }
			
			 return res;
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
	}
	
	
	@Override
	public Response getSpecialityAndSubSpecialityListByUserIdByUnitId(SearchDto searchDto)
			throws ApplicationException {
		try {
			return iLabTransactionDao.getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response printBarcodeLab2(RadiologyOrdersListDto dto) {
		try {
			PatientConsentMapperDto consentMapperDto = new PatientConsentMapperDto();
			consentMapperDto.setPatientId(dto.getPatientId());
		    consentMapperDto.setEncounterId(dto.getEncounterId());
		    consentMapperDto.setAdmissionId(dto.getAdmissionId());
			String finalPRNFormat="";
						consentMapperDto.setTemplateDesc(dto.getTemplateDesc());
						consentMapperDto.setWristBandPrnSize(dto.getWristBandPrnSize());
						 com.param.global.common.Response res= iEncounterMasterService.appendCensentData(consentMapperDto);
						  if(res.getStatus().equals("success") && res.getObject()!=null) {
							  PatientConsentMapperDto resDto=(PatientConsentMapperDto)res.getObject();
							  if(finalPRNFormat!="" && finalPRNFormat!=null) {
								  finalPRNFormat= finalPRNFormat+"\n"+resDto.getTemplateDesc();
							  }else {
								  finalPRNFormat=resDto.getTemplateDesc();
							  }
						  }
			dto.setTemplateDesc(finalPRNFormat);
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, dto);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional
	public Response amendReportReason(LabResultMasterDto dto) throws ApplicationException {

		try {
			return iLabTransactionDao.amendReportReason(dto);
		} catch (Exception e) {
			logger.error("Exception", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response<LabSampleMasterDto, Integer> getSampleDataForInterface(String interfaceName , String sampleID, String sampleInfoDate, int apiNumber, int orgUnitId) throws ApplicationException {
		
		try{

			Response<LabSampleMasterDto, Integer> response = iLabTransactionDao.getSampleDataForInterface(interfaceName , sampleID, sampleInfoDate, apiNumber, orgUnitId);
			if(response.getCode() == "200"){
				List<LabSampleMasterDto> listLabSampleMasterDto = response.getListObject();
				return new Response(SUCCESS, SUCCESS_200, SUCCESS_200_MESSAGE, listLabSampleMasterDto, null);

			}
			else
				return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);

		}
		catch(Exception e){
			logger.error("Exception", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getPOCLabServices(SearchDto searchDto)
			throws ApplicationException {
		try {

			Response resp = iLabTransactionDao.getSpecialityAndSubSpecialityListByUserIdByUnitId(searchDto);
			List<Integer> deptIds = new ArrayList<>();
			List<Integer> subDeptIds = new ArrayList<>();
			List<SearchDto> specialityList = new ArrayList<>();
			List<SearchDto> subSpecialityList = new ArrayList<>();
			List<Integer>  testTypes = new ArrayList<Integer>();
			List<Integer>  visitTypes = new ArrayList<Integer>();

			visitTypes = Arrays.asList(ICommonConstants.ER);
//			searchDto.getVisitTypes().set(0).setId(ICommonConstants.ER);

//			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size()>0) {
//				visitTypes = searchDto.getVisitTypes().stream()
//						.map(obj -> obj.getId())
//						.collect(Collectors.toList());
//			}

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size()>0) {
				testTypes = searchDto.getTestTypes().stream()
						.map(obj -> obj.getId())
						.collect(Collectors.toList());
			}

			if(resp.getListObject() != NULL && resp.getListObject().size() > 0 )  {
				specialityList = resp.getListObject();
				if(resp.getListObject1() != null) {
					subSpecialityList = resp.getListObject1();
				}
			}
			if(specialityList != null && specialityList.size() > 0) {
				for (Iterator iterator = specialityList.iterator(); iterator.hasNext();) {
					SearchDto dto = (SearchDto) iterator.next();
					deptIds.add(dto.getSpecialityId());
				}
			}else {
				deptIds.add(searchDto.getDeptId());
			}

			if(subSpecialityList != null && subSpecialityList.size() > 0) {
				for (Iterator iterator = subSpecialityList.iterator(); iterator.hasNext();) {
					SearchDto dto2 = (SearchDto) iterator.next();
					subDeptIds.add(dto2.getSubSpecialityMasterId());
				}
			}
			searchDto.setSubDeptIds(subDeptIds);

			Integer serologyId = 0;
			LabResultMasterDto dto = (LabResultMasterDto) sessionFactory.getCurrentSession()
					.createSQLQuery(" select " +
							" distinct(tlc.serology_id ) as \"serologyId\" " +
							"	 from t_lab_configuration tlc " +
							"	 where " +
							"	 tlc.organisation_id =:orgId and tlc.unit_id =:orgUnitId")
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).uniqueResult();

			boolean isSerologyIdPresent = false;

			if(dto.getSerologyId() != null) {
				for (obj item : searchDto.getSubDepts()) {
					if (dto.getSerologyId().equals(item.getId())) {
						isSerologyIdPresent = true;
						break;
					}
				}
			}

			List<SearchDto> filteredList = subSpecialityList.stream()
					.filter(bill -> bill.getSubSpecialityMasterId().equals(dto.getSerologyId())).collect(Collectors.toList());
			List<LabResultMasterDto> list =  new ArrayList<>();
			Integer countSero = 0;
			//if(searchDto.getIsOnHoldParameter() == false) {
			if(dto.getSerologyId() != null && dto.getSerologyId() != 0 && (filteredList!= null && filteredList.size() > 0 && subSpecialityList.size() > 0  || (subSpecialityList.size() == 0)) && isSerologyIdPresent==true){

				String query1 = sessionFactory.getCurrentSession().getNamedQuery("GET_ALL_POC_SERO_RESULT_ENTRY_LIST").getQueryString().toString();

				StringBuilder sb1 = ganarateQuery(query1,searchDto);

				Query createdQuery1 = sessionFactory.getCurrentSession().createSQLQuery(sb1.toString());



				     /* if (searchDto.getSearchKeyword() != null && !searchDto.getSearchKeyword().isEmpty()) {
				    	    sb1.append(" AND ( "
				    	            + " lbsampledtls.sub_dept_id = " + searchDto.getSearchKeyword()
								//	+ " AND lower(subspe_mst.sub_speciality_name) LIKE concat('%',LOWER('"+searchDto.getSearchField()+ "%'"

				    	            + " ) ");
				    	}
*/


				List<LabResultMasterDto> listSero  = createdQuery1
						.setParameter("orgId", searchDto.getOrgId())
						.setParameter("orgUnitId", searchDto.getOrgUnitId())
						.setParameterList("deptId", deptIds)
//						.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
//						.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
						.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

//				String querySer = sessionFactory.getCurrentSession().getNamedQuery("GET_ALL_POC_SERO_RESULT_ENTRY_LIST_COUNT").getQueryString().toString();
//
//				StringBuilder sbSer = ganarateQuery(querySer,searchDto);
//
//				Query createdQuerySer = sessionFactory.getCurrentSession().createSQLQuery(sbSer.toString());
//				List<LabResultMasterDto> listcount  = createdQuerySer
//						.setParameter("orgId", searchDto.getOrgId())
//						.setParameter("orgUnitId", searchDto.getOrgUnitId())
//						.setParameterList("deptId", deptIds)
//						.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

				if(listSero != null && listSero.size()>0) {
					countSero = listSero.size();
					list.addAll(listSero);
				}
			}

			if(searchDto.getSubDepts().size()==1 && isSerologyIdPresent == true){
				return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list, list.size());
			}

			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ALL_POC_SUBDEPTS_RESULT_ENTRY_LIST").getQueryString().toString();

			searchDto.setSeroDeptId(dto.getSerologyId() != null ? dto.getSerologyId(): null);
			StringBuilder sb = ganarateAllSubDeptQuery(query,searchDto);

			Query createdQuery = sessionFactory.getCurrentSession()
					.createSQLQuery(sb.toString());
			List<Integer> statusIds = new ArrayList<Integer>();
			List<Integer> subSpeciIds = new ArrayList<Integer>();

			if(searchDto.getIsOnHoldParameter() == false) {

				if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {

					statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);

				}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {

					statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,RETESTING);

				}else {

					statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
				}
			}

			if (searchDto.getSubDeptIds() != null  && searchDto.getSubDeptIds().size() > 0) {
				subSpeciIds = searchDto.getSubDeptIds();
				createdQuery.setParameterList("subDeptIds", subSpeciIds);

			}

			if (searchDto.getFromDt() != null ) {
				createdQuery.setParameter("fromDt", searchDto.getFromDt());
			}

			if (searchDto.getToDt() != null ) {
				createdQuery.setParameter("toDt", searchDto.getToDt());
			}

			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
				createdQuery.setParameter("patientId", searchDto.getPatientId());
			}

			if (visitTypes != null && visitTypes.size() > 0){
				createdQuery.setParameterList("visitTypes", visitTypes);
			}
			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0 )

				if(searchDto.getIsAcceptanceInDepartment() == 'Y') {

					List<obj> withoutSerosubDepts = new ArrayList<>();
					String sero = "Serology";
					for (obj lan : searchDto.getSubDepts()) {

						if(lan.getLabel()==null ) {
							if(searchDto.getDepartmentList()!=null) {

								obj newDto = searchDto.getDepartmentList().stream()
										.filter(ob -> ob.getId().equals(lan.getId()))
										.findAny()
										.orElse(null);
								if(newDto!=null) {
									lan.setLabel(newDto.getLabel());
								}
							}
						}

						if(lan.getLabel()!=null && !(lan.getLabel().toLowerCase()).equals(sero.toLowerCase()) && searchDto.getSeroDeptId() != null) {
							withoutSerosubDepts.add(lan);
						}
					}

					if (withoutSerosubDepts != null && withoutSerosubDepts.size() > 0) {
						List<Integer>  withoutSerosubDeptsInt = new ArrayList<Integer>();
						withoutSerosubDeptsInt = withoutSerosubDepts.stream()
								.map(obj -> obj.getId())
								.collect(Collectors.toList());

						createdQuery.setParameterList("withoutSerosubDepts", withoutSerosubDeptsInt);
					}

				}else {
					List<Integer>  subDepts = new ArrayList<Integer>();
					subDepts = searchDto.getSubDepts().stream()
							.map(obj -> obj.getId())
							.collect(Collectors.toList());
					createdQuery.setParameterList("subDepts", subDepts);
				}

			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0){
				createdQuery.setParameterList("testTypes", testTypes);
			}

			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0){
				createdQuery.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
			}

			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
				createdQuery.setParameter("assignUnit", searchDto.getAssignUnit());
			}

			if(searchDto.getUhidNumber()!="") {
				createdQuery.setParameter("uhidNumber", searchDto.getUhidNumber());
			}

			if(searchDto.getSampleNo()!="") {
				createdQuery.setParameter("sampleNo", searchDto.getSampleNo());
			}

			if (searchDto.getIsOnHoldParameter() == true) {
				statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
				createdQuery.setParameterList("statusIds", statusIds);
				createdQuery.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
			}
			if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
				createdQuery.setParameterList("statusIds", statusIds);
			}
			List<LabResultMasterDto> listall  = createdQuery
					.setParameter("orgId", searchDto.getOrgId())
					.setParameter("orgUnitId", searchDto.getOrgUnitId())
//						.setParameterList("sampleStatusId", statusIds)
					//.setParameter("deptId", searchDto.getDeptId())
					.setParameterList("deptId", deptIds)
//					.setFirstResult(searchDto.getOffset() != null ? searchDto.getOffset() : 0)
//					.setMaxResults(searchDto.getRecordPerPage() != null ? searchDto.getRecordPerPage() : 50)
					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

//			String query2 = sessionFactory.getCurrentSession()
//					.getNamedQuery("GET_ALL_POC_SUBDEPTS_RESULT_ENTRY_LIST_COUNT").getQueryString().toString();
//
//			StringBuilder  sb2 = ganarateAllSubDeptQuery(query2,searchDto);
//
//			Query createdQuery2 = sessionFactory.getCurrentSession()
//					.createSQLQuery(sb2.toString());
//			if (searchDto.getSubDeptIds() != null  && searchDto.getSubDeptIds().size() > 0) {
//				subSpeciIds = searchDto.getSubDeptIds();
//				createdQuery2.setParameterList("subDeptIds", subSpeciIds);
//
//			}
//
//			if (searchDto.getFromDt() != null ) {
//				createdQuery2.setParameter("fromDt", searchDto.getFromDt());
//			}
//
//			if (searchDto.getToDt() != null ) {
//				createdQuery2.setParameter("toDt", searchDto.getToDt());
//			}
//
//			if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0){
//				createdQuery2.setParameter("patientId", searchDto.getPatientId());
//			}
//
//			if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0){
//				createdQuery2.setParameterList("visitTypes", visitTypes);
//			}
//			if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0 )
//
//				if(searchDto.getIsAcceptanceInDepartment() == 'Y') {
//
//					List<obj> withoutSerosubDepts = new ArrayList<>();
//					String sero = "Serology";
//					for (obj lan : searchDto.getSubDepts()) {
//
//						if(lan.getLabel()==null ) {
//							if(searchDto.getDepartmentList()!=null) {
//
//								obj newDto = searchDto.getDepartmentList().stream()
//										.filter(ob -> ob.getId().equals(lan.getId()))
//										.findAny()
//										.orElse(null);
//								if(newDto!=null) {
//									lan.setLabel(newDto.getLabel());
//								}
//							}
//						}
//
//						if(lan.getLabel()!=null && !(lan.getLabel().toLowerCase()).equals(sero.toLowerCase()) && searchDto.getSeroDeptId() != null) {
//							withoutSerosubDepts.add(lan);
//						}
//					}
//
//					if (withoutSerosubDepts != null && withoutSerosubDepts.size() > 0) {
//						List<Integer>  withoutSerosubDeptsInt = new ArrayList<Integer>();
//						withoutSerosubDeptsInt = withoutSerosubDepts.stream()
//								.map(obj -> obj.getId())
//								.collect(Collectors.toList());
//
//						createdQuery2.setParameterList("withoutSerosubDepts", withoutSerosubDeptsInt);
//					}
//
//				}else {
//					List<Integer>  subDepts = new ArrayList<Integer>();
//					subDepts = searchDto.getSubDepts().stream()
//							.map(obj -> obj.getId())
//							.collect(Collectors.toList());
//					createdQuery2.setParameterList("subDepts", subDepts);
//				}
//
//			if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0){
//				createdQuery2.setParameterList("testTypes", testTypes);
//			}
//
//			if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0){
//				createdQuery2.setParameter("labSampleDtlsId", searchDto.getLabSampleDtlsId());
//			}
//
//			if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0){
//				createdQuery2.setParameter("assignUnit", searchDto.getAssignUnit());
//			}
//
//			if(searchDto.getUhidNumber()!="") {
//				createdQuery2.setParameter("uhidNumber", searchDto.getUhidNumber());
//			}
//
//			if(searchDto.getSampleNo()!="") {
//				createdQuery2.setParameter("sampleNo", searchDto.getSampleNo());
//			}
//
//			if (searchDto.getIsOnHoldParameter() == true) {
//				statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
//				createdQuery2.setParameterList("statusIds", statusIds);
//				createdQuery2.setParameter("isOnHoldParameter", searchDto.getIsOnHoldParameter());
//			}
//			if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
//				createdQuery2.setParameterList("statusIds", statusIds);
//			}
//			List<LabResultMasterDto> list1  = createdQuery2
//					.setParameter("orgId", searchDto.getOrgId())
//					.setParameter("orgUnitId", searchDto.getOrgUnitId())
////						.setParameterList("sampleStatusId", statusIds)
//					.setParameterList("deptId", deptIds)
//					//.setParameter("deptId", searchDto.getDeptId())
//					.setResultTransformer(Transformers.aliasToBean(LabResultMasterDto.class)).list();

			Integer count = 0;

			if(listall != null && listall.size()>0) {
				list.addAll(listall);
			}

			if (list.isEmpty()) {

				return new Response(SUCCESS, SUCCESS_200_MESSAGE, "No Records Found.", list, count);

			} else {

				if(listall != null && listall.size()>0) {
					count = listall.size();
					count = count + countSero;
				}else {
					count = count + countSero;
				}

				return new Response(SUCCESS, SUCCESS_200_MESSAGE, null, list, count);
			}

		} catch (Exception e) {
			logger.error("Exection", e);
			throw e;
		}
	}

	private StringBuilder ganarateQuery(String query, SearchDto searchDto) {

		List<Integer> statusIds = new ArrayList<Integer>();

		if(searchDto.getIsOnHoldParameter() == false) {

			if(searchDto.getIsAcceptanceInDepartment() == 'Y') {

				statusIds = bioChemStatus;

			}else if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {

				statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);

			}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {

				statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,RETESTING);

			}else {

				statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
			}



		}

		StringBuilder sb = new StringBuilder(query);

		if (searchDto.getFromDt() != null ) {
			sb.append(
					" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >= '" + searchDto.getFromDt()+ "' ");
		}

		if (searchDto.getToDt() != null ) {
			sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <= '" + searchDto.getToDt() + "' ");
		}

		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0)
			sb
					.append(" AND pati_mst.patient_id = " + searchDto.getPatientId());

		if (searchDto.getVisitTypes() != null && searchDto.getVisitTypes().size() > 0)
			sb.append(" AND visit_type_master.visit_type_id IN ("
					+ StringCommonMethods.convertToStringJoiner(searchDto.getVisitTypes()) + ")");

		if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0)
			sb.append(" AND test_mst.test_id in ( " +StringCommonMethods.convertToStringJoiner( searchDto.getTestTypes())+")");

		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
			sb
					.append(" AND lbsampledtls.lab_sample_dtls_id =" + searchDto.getLabSampleDtlsId());

		if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
			sb.append("  and lbsampledtls.assign_unit  ="+searchDto.getAssignUnit());

		if(searchDto.getUhidNumber()!="") {
			sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%',LOWER('"+searchDto.getUhidNumber()+"'),'%') ");
		}

		if(searchDto.getSampleNo()!="") {
			sb.append(" AND lower(sample_mst.sample_id) LIKE concat('%',LOWER('"+searchDto.getSampleNo()+"'),'%') ");
		}

		if (searchDto.getIsOnHoldParameter() == true) {
			statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
			sb.append(" and lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+")");
			sb.append(" AND lbsampledtls.is_on_hold_parameter =" + searchDto.getIsOnHoldParameter());
		}
		if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
			sb.append(" and ( lbsampledtls.sample_status_id IN("+statusIds.toString().toString().replace('[', '\n').replace(']', '\n')+") or lbsampledtls.is_on_hold_parameter = true )");
		}
		if (searchDto.getLabSortingId() != null) {
			if (searchDto.getLabSortingId() == 1) {
				sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
			} else if (searchDto.getLabSortingId() == 2) {
				sb.append(" ORDER BY   order_master.order_id DESC ");
			} else if (searchDto.getLabSortingId() == 3) {
				sb.append(" ORDER BY    sample_mst.sample_id  DESC ");
			} else if (searchDto.getLabSortingId() == 4) {
				sb.append(" ORDER BY    lbsampledtls.updated_date DESC ");
			}
		} else {
			sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
		}

		return sb;
	}

	private StringBuilder ganarateAllSubDeptQuery(String query, SearchDto searchDto) {


		List<Integer> statusIds = new ArrayList<Integer>();
		List<Integer> subSpeciIds = new ArrayList<Integer>();
		List<Integer> visitTypes = new ArrayList<Integer>();

		visitTypes = Arrays.asList(ICommonConstants.ER);

		if(searchDto.getIsOnHoldParameter() == false) {

			if(searchDto.getIsSampleAcceptanceApplicable() == 'Y') {

				statusIds = Arrays.asList(SAMPLE_SEND_TO_DEPT,RETESTING);

			}else if (searchDto.getIsSendToCrApplicable() != null && searchDto.getIsSendToCrApplicable() == 'Y') {

				statusIds =Arrays.asList(SAMPLE_SEND_TO_CR,RETESTING);

			}else {

				statusIds = Arrays.asList(SAMPLE_COLLECTED,RETESTING);
			}
		}

		StringBuilder sb = new StringBuilder(query);

		if (searchDto.getSubDeptIds() != null  && searchDto.getSubDeptIds().size() > 0) {
			sb.append(
					" AND (CASE WHEN tlc.consolidated_result_entry = 'N' THEN lbsampledtls.sub_dept_id NOT IN (:subDeptIds) "
							+ " ELSE (lbsampledtls.sub_dept_id NOT IN (tlc.serology_id) OR tlc.serology_id IS NULL OR tlc.serology_id = 0) END) ");
		}else {
			sb.append(
					"  AND (case when tlc.consolidated_result_entry = 'N' then lbsampledtls.sub_dept_id not in (tlc.biochemistry_id ,tlc.hematology_id ,tlc.clinical_pathology_id ,tlc.serology_id) else  (lbsampledtls.sub_dept_id not in (tlc.serology_id) or tlc.serology_id is  null or tlc.serology_id =0 )  end) ");
		}

		if (searchDto.getFromDt() != null ) {
			sb.append(
					" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') >=:fromDt  ");
		}

		if (searchDto.getToDt() != null ) {
			sb.append(" AND to_char(lbsampledtls.updated_date,'YYYY-MM-DD') <=:toDt  ");
		}

		if (searchDto.getPatientId() != null && searchDto.getPatientId() > 0) {
			sb.append(" AND pati_mst.patient_id =:patientId  ");
		}
		if (visitTypes != null && visitTypes.size() > 0) {
			sb.append(" AND visit_type_master.visit_type_id IN (:visitTypes)");
		}
		if (searchDto.getSubDepts() != null && searchDto.getSubDepts().size() > 0 )

			if(searchDto.getIsAcceptanceInDepartment() == 'Y') {

				List<obj> withoutSerosubDepts = new ArrayList<>();
				String sero = "Serology";
				for (obj lan : searchDto.getSubDepts()) {

					if(lan.getLabel()==null ) {
						if(searchDto.getDepartmentList()!=null) {

							obj newDto = searchDto.getDepartmentList().stream()
									.filter(ob -> ob.getId().equals(lan.getId()))
									.findAny()
									.orElse(null);
							if(newDto!=null) {
								lan.setLabel(newDto.getLabel());
							}
						}
					}

					if(lan.getLabel()!=null && !(lan.getLabel().toLowerCase()).equals(sero.toLowerCase()) && searchDto.getSeroDeptId() != null) {
						withoutSerosubDepts.add(lan);
					}
				}

				if (withoutSerosubDepts != null && withoutSerosubDepts.size() > 0) {
					sb.append(" AND test_mst.dept_id IN (:withoutSerosubDepts)");
				}

			}else {
				sb.append(" AND test_mst.dept_id IN (:subDepts)");
			}

		if (searchDto.getTestTypes() != null && searchDto.getTestTypes().size() > 0 )
			sb.append(" AND test_mst.test_id in (:testTypes)");

		if (searchDto.getLabSampleDtlsId() != null && searchDto.getLabSampleDtlsId() > 0)
			sb
					.append(" AND lbsampledtls.lab_sample_dtls_id =:labSampleDtlsId ");

		if (searchDto.getAssignUnit() != null && searchDto.getAssignUnit() > 0)
			sb.append("  and lbsampledtls.assign_unit  =:assignUnit ");

		if(searchDto.getUhidNumber()!="") {
			sb.append(" AND lower(pati_mst.uhid_number) LIKE concat('%',LOWER(:uhidNumber),'%') ");
		}

		if(searchDto.getSampleNo()!="") {
			sb.append(" AND lower(sample_mst.sample_id) LIKE concat('%',LOWER(:sampleNo),'%') ");
		}

		if (searchDto.getIsOnHoldParameter() == true) {
			statusIds = Arrays.asList(RESULT_ENTRY_DONE, RESULT_VALIDATION_DONE, REPORT_RELEASED, PARTIAL_REPORT_RELEASED, PROVISIONAL_REPORT_RELEASED);
			sb.append(" and lbsampledtls.sample_status_id IN(:statusIds)");
			sb.append(" AND lbsampledtls.is_on_hold_parameter =:isOnHoldParameter ");
		}
		if (statusIds != null && statusIds.size() > 0 && searchDto.getIsOnHoldParameter() == false) {
			sb.append("  AND  (lbsampledtls.sub_dept_id not in (tlc.serology_id) or tlc.serology_id is  null or tlc.serology_id =0 ) and (lbsampledtls.sample_status_id IN(:statusIds) or lbsampledtls.is_on_hold_parameter = true ) ");
		}

		if (searchDto.getLabSortingId() != null) {
			if (searchDto.getLabSortingId() == 1) {
				sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
			} else if (searchDto.getLabSortingId() == 2) {
				sb.append(" ORDER BY    order_master.order_id  DESC ");
			} else if (searchDto.getLabSortingId() == 3) {
				sb.append(" ORDER BY    sample_mst.sample_id  DESC ");
			} else if (searchDto.getLabSortingId() == 4) {
				sb.append(" ORDER BY    lbsampledtls.updated_date DESC ");
			}
		} else {
			sb.append(" ORDER BY    lbsampledtls.patient_id,  lbsampledtls.updated_date  DESC ");
		}

		return sb;
	}

	@Override
	@Transactional
	public Response saveIntervention(ClinicalInterventionProcedureDto clinicalInterventionProcedureDto) throws ApplicationException {

		try{

			return iClinicalInterventionProcedureDao.saveIntervention(clinicalInterventionProcedureDto);

		}
		catch(Exception e){
			logger.error("Exception", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateInterventionForCompleteProcedure(ClinicalInterventionProcedureDto clinicalInterventionProcedureDto) throws ApplicationException {

		try{

			return iClinicalInterventionProcedureDao.updateInterventionForCompleteProcedure(clinicalInterventionProcedureDto);

		}
		catch(Exception e){
			logger.error("Exception", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
