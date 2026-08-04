package com.param.global.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.document.AbstractExcelView;

import com.param.common.dto.CommonResponse;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.dao.IOTCompletionDao;
import com.param.global.dao.IServiceItemMapperDao;
import com.param.global.dao.IServiceMasterDao;
import com.param.global.service.VerifiedIdentityService;
import com.param.global.dto.OrderDetailsDiscountDto;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.ServiceSearchResDto;
import com.param.global.dto.SpecialityMasterDto;
import com.param.global.dto.SubSpecialityMasterDto;
import com.param.global.model.ServiceMaster;
import com.param.global.org.dto.GeneralLedgerMasterDto;
import com.param.global.org.service.IGeneralLedgerMasterService;
import com.param.global.otims.dto.BatchDto;
import com.param.global.pharmacy.service.IWalkInSalesService;
import com.param.module.service.ServiceItemMapperDto;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked","unused"})
public class ServiceMasterServiceImpl extends AbstractExcelView implements IServiceMasterService,IError,ICommonConstants {
	final static Logger logger = Logger.getLogger(ServiceMasterServiceImpl.class);


	@Autowired
	private ServletContext servletContext;

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;
	
	@Autowired
	private IServiceMasterDao iserviceMasterDao;
	
	@Autowired
	IEmrService EmrService;
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	IWalkInSalesService walkInSalesService;
	
	@Autowired
	IServiceItemMapperDao iServiceItemMapperDao;

	@Autowired
	SessionService sessionService;
	
	@Autowired
	IOTCompletionDao OTCompletionDao;
	
	@Autowired
	ISpecialityService iSpecialityService;
	
	@Autowired
	ISubSpecialityService iSubSpecialityService;
	
	@Autowired
	IGeneralLedgerMasterService iGeneralLedgerMasterService;
	
	@Override
	@Transactional
	public CommonResponse saveOrganizationServiceMaster(ServiceMasterDto xServiceMasterDto) throws ApplicationException {
		try {
			// BR-16x-class fix: verified-tenant bind on the write path (org-trust).
			xServiceMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			CommonResponse serviceCodeResponse = iserviceMasterDao.checkForUniqueServiceCode(xServiceMasterDto);
			if(serviceCodeResponse.getListObject()!=null && serviceCodeResponse.getListObject().size()>0) {
				return new CommonResponse(ERROR, ALREADY_EXIST_CODE, "Service Code is Already Exist.", null, null);
			}
			
			CommonResponse serviceNameResponse = iserviceMasterDao.checkForUniqueServiceName(xServiceMasterDto);
			if(serviceNameResponse.getListObject()!=null && serviceNameResponse.getListObject().size()>0) {
				return new CommonResponse(ERROR, ALREADY_EXIST_CODE, "Service Name is Already Exist.", null, null);
			}
			xServiceMasterDto.setIsAvailableForPackage('I');
			xServiceMasterDto.setIsAllowMultipleQuantity('I');
			xServiceMasterDto.setIsAutorendered('I');
			xServiceMasterDto.setIsDiscountApplicable('I');
			xServiceMasterDto.setIsDoctorRequired('I');
			xServiceMasterDto.setIsOrderSet('I');
			xServiceMasterDto.setIsOtProcedure('I');
			xServiceMasterDto.setIsOutsourceService('I');
			xServiceMasterDto.setIsRateEditable('I');
			xServiceMasterDto.setIsTaxApplicable('I');
			
			 Response xServiceMaster = iserviceMasterDao.saveOrganizationServiceMaster(xServiceMasterDto);
			 if(xServiceMaster.getCode().equals(SUCCESS_CODE))
			 {
				 ServiceMaster mstr = (ServiceMaster)xServiceMaster.getObject();
				 
				 if(xServiceMasterDto.getServiceItemMapperList()!= null && xServiceMasterDto.getServiceItemMapperList().size()>0) {
					 iServiceItemMapperDao.saveServiceItemMapper(xServiceMasterDto.getServiceItemMapperList(),mstr.getServiceMasterId());
				 }
				 return new	CommonResponse(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, xServiceMaster);
			 }
			 else {
				return new CommonResponse(ERROR, ALREADY_EXIST_CODE, SERVICE_NAME_ALREADY_EXIST, null, xServiceMaster);
			 }
			 
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response changeOrganizationServiceMasterStatus(ServiceMasterDto xServiceMasterDto) throws ApplicationException {
		try {
			// BR-16x-class fix: verified-tenant bind on the write path (org-trust).
			xServiceMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			return iserviceMasterDao.changeOrganizationServiceMasterStatus(xServiceMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response listOrganizationServiceMasterMaster(Integer orgId, Integer offset, Integer recordPerPage)
			throws ApplicationException {
		try {
			// BR-16x-class fix (2026-08-04, live-proven IDOR): the orgId was client-supplied
			// from the URL path with no verification — forged orgId=999 returned org-999 rows
			// (SVC-999-X) to an org-1 session. Rebind to the verified tenant identity.
			orgId = verifiedIdentityService.getVerifiedOrganizationId();
			return iserviceMasterDao.listOrganizationServiceMasterMaster(orgId, offset, recordPerPage);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getOrganizationServiceMasterTotalCount(Integer orgId) throws ApplicationException {
		try {
			orgId = verifiedIdentityService.getVerifiedOrganizationId();
			return iserviceMasterDao.getOrganizationServiceMasterTotalCount(orgId);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getOrganizationServiceMasterById(ServiceMasterDto xServiceMasterDto) {
		try {
			// BR-161-class READ-path fix: verified-tenant rebind (reads leak too).
			xServiceMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			return iserviceMasterDao.getOrganizationServiceMasterById(xServiceMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public CommonResponse updateOrganizationServiceMaster(ServiceMasterDto xServiceMasterDto) throws ApplicationException {
		try {
			// BR-16x-class fix: verified-tenant bind on the write path (org-trust).
			xServiceMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			CommonResponse serviceCodeResponse = iserviceMasterDao.checkForUniqueServiceCode(xServiceMasterDto);
			if(serviceCodeResponse.getListObject()!=null && serviceCodeResponse.getListObject().size()>0) {
				return new CommonResponse(ERROR, ALREADY_EXIST_CODE, "Service Code is Already Exist.", null, null);
			}

			CommonResponse serviceNameResponse = iserviceMasterDao.checkForUniqueServiceName(xServiceMasterDto);
			if(serviceNameResponse.getListObject()!=null && serviceNameResponse.getListObject().size()>0) {
				return new CommonResponse(ERROR, ALREADY_EXIST_CODE, "Service Name is Already Exist.", null, null);
			}
			
			xServiceMasterDto.setIsAvailableForPackage('I');
			xServiceMasterDto.setIsAllowMultipleQuantity('I');
			xServiceMasterDto.setIsAutorendered('I');
			xServiceMasterDto.setIsDiscountApplicable('I');
			xServiceMasterDto.setIsDoctorRequired('I');
			xServiceMasterDto.setIsOrderSet('I');
			xServiceMasterDto.setIsOtProcedure('I');
			xServiceMasterDto.setIsOutsourceService('I');
			xServiceMasterDto.setIsRateEditable('I');
			xServiceMasterDto.setIsTaxApplicable('I');
			Response xServiceMaster = iserviceMasterDao.updateOrganizationServiceMaster(xServiceMasterDto);
			
			 if(xServiceMaster.getCode().equals(SUCCESS_CODE))
			 {
				 if(xServiceMasterDto.getServiceItemMapperList()!= null && xServiceMasterDto.getServiceItemMapperList().size()>0) {
					 
					 iServiceItemMapperDao.updateServiceItemMapper(xServiceMasterDto.getServiceItemMapperList(),xServiceMasterDto.getServiceMasterId());
					 
				 }
				 return new	CommonResponse(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, xServiceMaster);
			 }
			 else {
				return new CommonResponse(ERROR, ALREADY_EXIST_CODE, SERVICE_NAME_ALREADY_EXIST, null, xServiceMaster);
			 }
			 
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getOrganizationServiceMasterBySpecialityAndSubSpeciality(ServiceMasterDto xServiceMasterDto)
			throws ApplicationException {
		try {					
			// BR-161-class READ-path fix: verified-tenant rebind (reads leak too).
			xServiceMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			return iserviceMasterDao.getOrganizationServiceMasterBySpecialityAndSubSpeciality(xServiceMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getServiceDetailsByServiceId(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			return iserviceMasterDao.getServiceDetailsByServiceId(serviceMasterDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getConsultationService(Integer orgId,Integer unitId,Integer splId,Integer subSplId) throws ApplicationException {
		try {
			return iserviceMasterDao.getConsultationService(orgId,unitId,splId,subSplId);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getAutoCompleteServicesLab(int orgId, int orgUnitId, int subDeptId, String serviceName)
			throws ApplicationException {
		try {
			return iserviceMasterDao.getAutoCompleteServicesLab(orgId,orgUnitId,subDeptId,serviceName);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getListOfServiceMaster(Integer orgId, Integer unitId) throws ApplicationException {
		orgId = verifiedIdentityService.getVerifiedOrganizationId();
		unitId = verifiedIdentityService.getVerifiedUnitId();
		
		try {
			return iserviceMasterDao.getListOfServiceMaster(orgId,unitId);
		}catch (Exception e) {
		logger.error("getListOfServiceMaster failed", e);
		}
		return new Response<>(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	
	@Override
	public Response getAllServicesByOrgId(Integer orgId) throws ApplicationException {
		orgId = verifiedIdentityService.getVerifiedOrganizationId();
		
		try {
			return iserviceMasterDao.getAllServicesByOrgId(orgId);
		}catch (Exception e) {
		logger.error("getAllServicesByOrgId failed", e);
		}
		return new Response<>(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response readCSVFile(ServiceMasterDto serviceMasterDto1) throws ApplicationException {
		try {
			
			SpecialityMasterDto dto = new SpecialityMasterDto();
				dto.setOrganizationId(serviceMasterDto1.getOrganizationId());
			Response splResponse = iSpecialityService.getSpecialityListOfActiveInactive(dto);
			List<SpecialityMasterDto> specialityMasterList = splResponse.getListObject();
			
			SubSpecialityMasterDto subSpecialityMasterDto = new SubSpecialityMasterDto();
				subSpecialityMasterDto.setOrganizationId(serviceMasterDto1.getOrganizationId());
				subSpecialityMasterDto.setNoOfRecordsPerPage(1000);
			Response subSplResponse = iSubSpecialityService.getSubSpecialityMasterList(subSpecialityMasterDto);
			List<SubSpecialityMasterDto> subSpecialityMasterDtoList = subSplResponse.getListObject();
			
			Response genLedgerRes = iGeneralLedgerMasterService.getGeneralLedgerMasterActiveList(serviceMasterDto1.getOrganizationId(),serviceMasterDto1.getGeneralLedgerName());
			List<GeneralLedgerMasterDto> generalLedgerMasterDtosList = genLedgerRes.getListObject();
			
			Response serviceRes = this.getAllServicesByOrgId(serviceMasterDto1.getOrganizationId());
			List<ServiceMasterDto> allServicesList = serviceRes.getListObject();
			
			List<ServiceMasterDto> services=new ArrayList<ServiceMasterDto>();
			  
		    List<ServiceMasterDto> faultyServiceList=new ArrayList<>();

			InputStreamReader reader=new InputStreamReader(serviceMasterDto1.getFile().getInputStream());
		    BufferedReader br =new BufferedReader(reader);  
		    String line="";
		    
		    //count variable
		    int count = 0;
		    Integer successCount=0,failureCount=0;
		    //read line by line.
		    while((line = br.readLine())!=null) 
		    {
		    	ServiceMasterDto serviceMasterDto=new ServiceMasterDto();
		    	List<SpecialityMasterDto> splDto = new ArrayList<>();
		    	List<SubSpecialityMasterDto> subSplDto = new ArrayList<>();
		    	List<GeneralLedgerMasterDto> GLDto = new ArrayList<>();
		    	List<ServiceMasterDto> serviceDto = new ArrayList<>();
		    	List<ServiceMasterDto> newlyAddedServiceDto = new ArrayList<>();
		    	StringBuilder remark = new StringBuilder();
		    	
		    	//after reading line at once, split by , bz, csv format & store in String[].
		    	String [] strServices=line.trim().split(",");
		    	
		    	//count++
		    	count++;
		    	
		    	if(count>1) 
		    	{    
		    		Stream<SpecialityMasterDto> specialityMasterStream = specialityMasterList.stream();
		    		splDto = specialityMasterStream.filter(str -> str.getStatus()=='A' && str.getSpecialityName().toLowerCase().equals(strServices[0].trim().toLowerCase())).collect(Collectors.toList());
		    		
		    		serviceMasterDto.setSpecialityName(strServices[0].trim()); 
		    		if(splDto.size()>0) {
		    			serviceMasterDto.setSpecialityId(splDto.getFirst().getSpecialityId()); 
		    		}else{
		    			if(remark.length()==0) {
		    				remark.append(" Department does not exist");
		    			}else {
		    				remark.append(", Department does not exist");
						}
		    		}
		    		
		    		Stream<SubSpecialityMasterDto> subSpecialityMasterStream = subSpecialityMasterDtoList.stream();
		    		subSplDto = subSpecialityMasterStream.filter(str -> str.getStatus()=='A' && str.getSubSpecialityMasterName().toLowerCase().equals(strServices[1].trim().toLowerCase())).collect(Collectors.toList());
		    		
		    		serviceMasterDto.setSubSpecialityName(strServices[1].trim()); 
		    		 
		    		 if(subSplDto.size()>0) 
		    		 {
		    			 serviceMasterDto.setSubSpecialityId(subSplDto.getFirst().getSubSpecialityMasterId()); 
		    		 }else {
		    			 if(remark.length()==0) 
		    			 {
			    				remark.append(" SubDepartment does not exist");
		    			 }else {
			    				remark.append(", SubDepartment is not exist");
		    			 }
		    		 }
		    		 
		    		 Stream<GeneralLedgerMasterDto> GLStream = generalLedgerMasterDtosList.stream();
			    	 GLDto = GLStream.filter(str -> str.getStatus()=='A' && str.getGeneralLedgerName().toLowerCase().equals(strServices[4].trim().toLowerCase())).collect(Collectors.toList());
			    		
		    		serviceMasterDto.setGeneralLedgerName(strServices[4].trim()); 
		    		if(GLDto.size()>0) 
		    		{
		    			serviceMasterDto.setGeneralLedgerId(GLDto.getFirst().getGeneralLedgerId());	
		    		}else {
		    			if(remark.length()==0) {
			    			remark.append(" General Ledger Code not exist");
		    			}else {
			    			remark.append(", General Ledger Code not exist");
						}
		    		}
			    		
		    		Stream<ServiceMasterDto> exisitngServicesStream = allServicesList.stream();
		    		serviceDto = exisitngServicesStream.filter(str -> str.getServiceStandardName().toLowerCase().equals(strServices[2].trim().toLowerCase())).collect(Collectors.toList());
		    		
		    		Stream<ServiceMasterDto> newlyAdded = services.stream();
		    		newlyAddedServiceDto = newlyAdded.filter(str -> str.getServiceStandardName().toLowerCase().equals(strServices[2].trim().toLowerCase())).collect(Collectors.toList());
		    		
		    		if(serviceDto.size()>0 || newlyAddedServiceDto.size()>0) 
		    		{
		    			if(remark.length()==0) {
			    			remark.append("Service already exist");
		    			}else {
			    			remark.append(", Service already exist");
						}
		    		}
		    		 
		    		serviceMasterDto.setServiceStandardName(strServices[2].trim());
		    		serviceMasterDto.setServiceStandardCode(strServices[3].trim());
		    		serviceMasterDto.setValidFrom(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-MM-YYYY HH:mm:ss"));
		    		serviceMasterDto.setValidFrom(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-MM-YYYY HH:mm:ss"));
		    		serviceMasterDto.setStatus('A');
		    		serviceMasterDto.setIsAvailableForPackage('I');
					serviceMasterDto.setIsAllowMultipleQuantity('I');
					serviceMasterDto.setIsAutorendered('I');
					serviceMasterDto.setIsDiscountApplicable('I');
					serviceMasterDto.setIsDoctorRequired('I');
					serviceMasterDto.setIsOrderSet('I');
					serviceMasterDto.setIsOtProcedure('I');
					serviceMasterDto.setIsOutsourceService('I');
					serviceMasterDto.setIsRateEditable('I');
					serviceMasterDto.setIsTaxApplicable('I');
					serviceMasterDto.setOrganizationId(serviceMasterDto1.getOrganizationId());
					serviceMasterDto.setCreatedBy(serviceMasterDto1.getCreatedBy());
					serviceMasterDto.setUpdatedBy(serviceMasterDto1.getUpdatedBy());
					serviceMasterDto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-MM-YYYY HH:mm:ss"));
					serviceMasterDto.setUpdatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(0), "dd-MM-YYYY HH:mm:ss"));
		    		
		    		if(remark.length()>0) 
		    		{
		    			failureCount++;
		    			serviceMasterDto.setFailedRemark(remark.toString());
		    			faultyServiceList.add(serviceMasterDto);
		    		}else {
		    			successCount++;
		    			services.add(serviceMasterDto);
		    		}
		    	}
		    }
		    
		    if(!faultyServiceList.isEmpty()) 
		    {
			    Map<String,Object> mapList=new HashMap<>();
			    mapList.put("faultyServiceList",faultyServiceList);
			    HSSFWorkbook workbook=new HSSFWorkbook();
			    HttpServletRequest req = null;
			    HttpServletResponse res = null;
			    buildExcelDocument(mapList,workbook, req,res);
		    }
		    
		    //***********Save service master call****************
	       if(!services.isEmpty()) 
	       {
	    	   Response saveServiceRes=iserviceMasterDao.readCSVFile(services);
	       }
	       ServiceMasterDto finalServiceMasterDto=new ServiceMasterDto();
		   finalServiceMasterDto.setSuccessCount(successCount);
		   finalServiceMasterDto.setFailureCount(failureCount);
		   return new Response(SUCCESS,SUCCESS_CODE, SUCCESS_MESSAGE, null, finalServiceMasterDto);
		
		} catch (Exception e) {
		logger.error("readCSVFile failed", e);
			return new Response(null,null,null,null,null);
		}


	}

	protected void buildExcelDocument(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
			// get data model which is passed by the Spring container
			List<ServiceMasterDto> finalFaultyServiceList = (List<ServiceMasterDto>) model.get("faultyServiceList");
		
			// create a new Excel sheet
			HSSFSheet sheet = workbook.createSheet("Faulty_Entries_Book");
	        sheet.setDefaultColumnWidth(30);
	        //searching for any method...sheet.
	        // create style for header cells
	        CellStyle style = workbook.createCellStyle();
	        Font font = workbook.createFont();
	        font.setFontName("Arial");
	        style.setFillForegroundColor(HSSFColor.WHITE.index);
	        //style.setFillPattern(CellStyle.SOLID_FOREGROUND);//erases cell border..req here to skip...
	        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	        font.setColor(HSSFColor.BLACK.index);
	        style.setFont(font);
        
        
	     	// create header row
	        HSSFRow header = sheet.createRow(0);
	         
	        header.createCell(0).setCellValue("Department");
	        header.getCell(0).setCellStyle(style);
	         
	        header.createCell(1).setCellValue("SubDepartment");
	        header.getCell(1).setCellStyle(style);
	         
	        header.createCell(2).setCellValue("ServiceName");
	        header.getCell(2).setCellStyle(style);
	         
	        header.createCell(3).setCellValue("ServiceCode");
	        header.getCell(3).setCellStyle(style);
			
	        header.createCell(4).setCellValue("General LedgerCode");
	        header.getCell(5).setCellStyle(style);
	        
	        header.createCell(5).setCellValue("Failed Remark");
	        header.getCell(5).setCellStyle(style);
	        // create data rows
	        int rowCount = 1;
         
	        for (ServiceMasterDto sMasterDto : finalFaultyServiceList) 
	        {
	           
	        	//actual row creation from for each object.
	        	HSSFRow aRow = sheet.createRow(rowCount++);
	            aRow.createCell(0).setCellValue(sMasterDto.getSpecialityName());
	            aRow.createCell(1).setCellValue(sMasterDto.getSubSpecialityName());
	            aRow.createCell(2).setCellValue(sMasterDto.getServiceStandardName());
	            aRow.createCell(3).setCellValue(sMasterDto.getServiceStandardCode());
	            aRow.createCell(4).setCellValue(sMasterDto.getGeneralLedgerName());
	            aRow.createCell(5).setCellValue(sMasterDto.getFailedRemark());
	
	        }
        
            String realPath = servletContext.getRealPath("/")+"resources"+ File.separator + "service-master-failed-records.xls";
        	File file = new File(realPath);//write this workbook to an Outputstream.
        	if(file.exists()==false) {
        		//file.mkdir();
        		workbook.write(new FileOutputStream(file));
        	}else {
        		workbook.write(new FileOutputStream(file));
        	}
        
	}	

	
	@Override
	public Response getSurchargeServiceDetails(ServiceMasterDto dto) throws ApplicationException {
		try {
			
			 List<OrderDetailsDiscountDto> OrderDetailsDiscountDtoList = new ArrayList<OrderDetailsDiscountDto>();
				Response surchargeRes = null;
				if(dto.getSaleIds()!=null && dto.getSaleIds().size()>0) {
			  for (Integer saleId: dto.getSaleIds()) 
			      { 
			    	 if(dto.getPharmacyServiceId()!=null && dto.getPharmacyServiceId()>0 && saleId!=null)
			    	 {
			    		    surchargeRes =   walkInSalesService.getPharamcySurchargeSericeId(saleId, dto.getPaymentEntitlementTypeId(), dto.getPatientClassId(), dto.getPatientCategoryId());
							OrderDetailsDiscountDto OrderDetailsDiscountDtos = (OrderDetailsDiscountDto) surchargeRes.getObject();
							OrderDetailsDiscountDtos.setServiceId(dto.getPharmacyServiceId());
							OrderDetailsDiscountDtoList.add(OrderDetailsDiscountDtos);
			    	 }
		         } }
		     for (Integer serviceId: dto.getServiceIds()) 
		      { 
		    	 if(!serviceId.equals(dto.getPharmacyServiceId()))
		    	 {
		    		    dto.setServiceMasterId(serviceId);
						surchargeRes =   iserviceMasterDao.getSurchargeServiceDetails(dto);
						OrderDetailsDiscountDto OrderDetailsDiscountDtos = (OrderDetailsDiscountDto) surchargeRes.getObject();
						OrderDetailsDiscountDtos.setServiceId(serviceId);
						OrderDetailsDiscountDtoList.add(OrderDetailsDiscountDtos);
		    	 }
		      } 
			List<ServiceSearchResDto> list = (List<ServiceSearchResDto>)sessionFactory.getCurrentSession()
					.getNamedQuery("SURCHARGE_SERVICE_DETAILS")
					.setInteger("orgId", dto.getOrganizationId())
					.setInteger("unitId", dto.getUnitId())
			.setResultTransformer(Transformers.aliasToBean(ServiceSearchResDto.class)).list();
			list.getFirst().setDiscountDetailsList(OrderDetailsDiscountDtoList);
			return new Response(SUCCESS,SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
		logger.error("getSurchargeServiceDetails failed", e);
		}
		return new Response<>(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getCancelServices(Integer admissionId, Integer visitTypeId , Integer orgId) throws ApplicationException {
		try {
			return iserviceMasterDao.getCancelServices(admissionId,visitTypeId,orgId);
		}catch (Exception e) {
		logger.error("getCancelServices failed", e);
		}
		return new Response<>(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getAutoRenderedServiceDetails(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iserviceMasterDao.getAutoRenderedServiceDetails(orgId,unitId);
		}catch (Exception e) {
		logger.error("getAutoRenderedServiceDetails failed", e);
		}
		return new Response<>(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}
	
	@Override
	@Transactional
	public Response getOutsourcedServices(Integer unitId,String servcieName) throws ApplicationException {
		try {
			return iserviceMasterDao.getOutSourcedService(unitId,servcieName);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	public Response getConsultationDeptByServiceId(Integer serviceId, Integer unitId) throws ApplicationException {
		try {
			return iserviceMasterDao.getConsultationDeptByServiceId(serviceId,unitId);
		}catch (Exception e) {
		logger.error("getConsultationDeptByServiceId failed", e);
		}
		return new Response<>(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response serviceMasterUniversalSearch(ServiceMasterDto serviceMasterDto) throws ApplicationException {
		try {
			return iserviceMasterDao.serviceMasterUniversalSearch(serviceMasterDto);
		}catch (Exception e) {
		logger.error("serviceMasterUniversalSearch failed", e);
		}
		return new Response<>(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getServiceItemMapperDetails(ServiceItemMapperDto serviceItemMapperDto) throws ApplicationException {
		try {
			return iServiceItemMapperDao.getServiceItemMapperDetails(serviceItemMapperDto);
		}catch (Exception e) {
		logger.error("getServiceItemMapperDetails failed", e);
		}
		return new Response<>(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
	}

	@Override
	public Response getServiceItemMapperDetailsByServiceId(ServiceItemMapperDto serviceItemMapperDto)
			throws ApplicationException {
		try {
			List<ServiceItemMapperDto> finalList =  new ArrayList<>();
			Response res =  iServiceItemMapperDao.getServiceItemMapperDetails(serviceItemMapperDto); 
			if(res.getListObject()!=null && res.getListObject().size()>0) {
				List<ServiceItemMapperDto> list = res.getListObject();
				for(ServiceItemMapperDto dto : list) {
					
					if(finalList.size() == 0) {
						finalList.add(dto);
					}else {
						int i = 0;
						for(ServiceItemMapperDto compareDto : finalList) {
							
							if(dto.getItemId().equals(compareDto.getItemId())) {
								Integer assignQty = compareDto.getAssignQty() + dto.getAssignQty();
								compareDto.setAssignQty(assignQty);
								i = 1;
							}
						}
						
						if(i == 0) {
							finalList.add(dto);
						}
					}
					
				}
				
				for(ServiceItemMapperDto dto : finalList) {
					dto.setBatchReq(true);
					Integer storeId = serviceItemMapperDto.getStoreId() != null ? serviceItemMapperDto.getStoreId() : sessionService.getStoreId();
					Response res1 = OTCompletionDao.getBatchListByItemId(storeId, dto.getItemId(),serviceItemMapperDto.getOrganizationId(),serviceItemMapperDto.getUnitId());
					if(res1.getStatus().equals(SUCCESS)) {
						List<BatchDto> batchList = res1.getListObject();
						
						if(batchList.size() == 1) {
							if(batchList.getFirst().getBatchNo() == null) {
								dto.setBatchReq(false);
							}
						}
							
						for (BatchDto  batchDto : batchList) {
							Response res2 = OTCompletionDao.getUOMListByBatchId(batchDto.getBatchId(),serviceItemMapperDto.getOrganizationId(),serviceItemMapperDto.getUnitId());
							if(res2.getStatus().equals(SUCCESS)) {
								batchDto.setUom(res2.getListObject());
							}
						}
						dto.setBatchList(batchList);
					}
				}
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, finalList, null);
			}
			return res;
		} catch (Exception e) {
			logger.error("Exception", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	public Response getBatchDetailsByItemList(ServiceItemMapperDto serviceItemMapperDto) throws ApplicationException {
		try {
			List<ServiceItemMapperDto> list = new ArrayList<>();
			if(serviceItemMapperDto.getItemIds()!=null && serviceItemMapperDto.getItemIds().size()>0) {
				 list = serviceItemMapperDto.getItemIds();
				for(ServiceItemMapperDto dto : list) {
					dto.setBatchReq(true);
					Response res1 = OTCompletionDao.getBatchListByItemId(serviceItemMapperDto.getStoreId(),dto.getItemId(),serviceItemMapperDto.getOrganizationId(),serviceItemMapperDto.getUnitId());
					if(res1.getStatus().equals(SUCCESS)) {
						List<BatchDto> batchList = res1.getListObject();
						if(batchList.size() == 1) {
							if(batchList.getFirst().getBatchNo() == null) {
								dto.setBatchReq(false);
							}
						}
						for (BatchDto  batchDto : batchList) {
							Response res2 = OTCompletionDao.getUOMListByBatchId(batchDto.getBatchId(),serviceItemMapperDto.getOrganizationId(),serviceItemMapperDto.getUnitId());
							if(res2.getStatus().equals(SUCCESS)) {
								batchDto.setUom(res2.getListObject());
							}
						}
						dto.setBatchList(batchList);
					}
				}
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		} catch (Exception e) {
			logger.error("Exception", e);
			return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}
	}

	@Override
	@Transactional
	public Response listOfServicesForWebAppIntegration(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iserviceMasterDao.listOfServicesForWebAppIntegration(orgId,unitId);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

}
