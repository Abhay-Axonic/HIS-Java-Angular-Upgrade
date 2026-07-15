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

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IGlobalServiceMapperServiceDao;
import com.param.global.dao.IGlobalServiceMasterServiceDao;
import com.param.global.dao.IServiceMasterDao;
import com.param.global.dao.IUnitServiceMasterDetailsDao;
import com.param.global.dto.ServiceMasterDto;
import com.param.global.dto.SpecialityMasterDto;
import com.param.global.dto.SubSpecialityMasterDto;
import com.param.global.dto.UnitServiceMasterDetailsDto;
import com.param.global.org.dto.GeneralLedgerMasterDto;
import com.param.global.org.service.IGeneralLedgerMasterService;
import com.param.utility.TimeZoneComponent;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GlobalServiceMasterServiceImpl implements IGlobalServiceMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(GlobalServiceMasterServiceImpl.class);

	
	@Autowired
	private IGlobalServiceMasterServiceDao iGlobalServiceMasterServiceDao;
	
	@Autowired
	private IGlobalServiceMapperServiceDao iGlobalServiceMapperServiceDao;

	@Autowired
	ISpecialityService iSpecialityService;
	
	@Autowired
	ISubSpecialityService iSubSpecialityService;
	
	@Autowired
	IGeneralLedgerMasterService iGeneralLedgerMasterService;
	
	@Autowired
	private IServiceMasterService iServiceMasterService; 
	
	@Autowired
	private IServiceMasterDao iserviceMasterDao;
	
	@Autowired
	private ServletContext servletContext;
	
	@Override
	@Transactional
	public Response searchGlablServiceMaster(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			return iGlobalServiceMasterServiceDao.searchGlablServiceMaster(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	
	
	@Override
	@Transactional
	public Response glablServiceMaster(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) throws ApplicationException {
		try {
			return iGlobalServiceMasterServiceDao.glablServiceMaster(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response glablServiceMasterSpecialityName(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			return iGlobalServiceMasterServiceDao.glablServiceMasterSpecialityName(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response glablServiceMasterSubSpecialityName(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) {
		try {
			return iGlobalServiceMasterServiceDao.glablServiceMasterSubSpecialityName(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}



	@Override
	@Transactional
	public Response saveGlobalServiceMasterByExcel(ServiceMasterDto serviceMasterDto1) throws ApplicationException {
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
			
			/*Response genLedgerRes = iGeneralLedgerMasterService.getGeneralLedgerMasterActiveList(serviceMasterDto1.getOrganizationId(),serviceMasterDto1.getGeneralLedgerName());
			List<GeneralLedgerMasterDto> generalLedgerMasterDtosList = genLedgerRes.getListObject();*/
			
			Response serviceRes = iServiceMasterService.getAllServicesByOrgId(serviceMasterDto1.getOrganizationId());
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
		    			serviceMasterDto.setSpecialityId(splDto.get(0).getSpecialityId()); 
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
		    			 serviceMasterDto.setSubSpecialityId(subSplDto.get(0).getSubSpecialityMasterId()); 
		    		 }else {
		    			 if(remark.length()==0) 
		    			 {
			    				remark.append(" SubDepartment does not exist");
		    			 }else {
			    				remark.append(", SubDepartment is not exist");
		    			 }
		    		 }
		    		 
		    		/* Stream<GeneralLedgerMasterDto> GLStream = generalLedgerMasterDtosList.stream();
			    	 GLDto = GLStream.filter(str -> str.getStatus()=='A' && str.getGeneralLedgerName().toLowerCase().equals(strServices[4].trim().toLowerCase())).collect(Collectors.toList());*/
			    		
		    		/*serviceMasterDto.setGeneralLedgerName(strServices[4].trim()); 
		    		if(GLDto.size()>0) 
		    		{
		    			serviceMasterDto.setGeneralLedgerId(GLDto.get(0).getGeneralLedgerId());	
		    		}else {
		    			if(remark.length()==0) {
			    			remark.append(" General Ledger Code not exist");
		    			}else {
			    			remark.append(", General Ledger Code not exist");
						}
		    		}*/
			    		
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
			    buildExcelDocumentExcel(mapList,workbook, req,res);
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
		throw new ApplicationException(e.getMessage(), e);
	}


	}


	protected void buildExcelDocumentExcel(Map<String, Object> model, HSSFWorkbook workbook, HttpServletRequest request,
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
	@Transactional
	public Response saveGlobalMasterData(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto)
			throws ApplicationException {
		try {
			 return  iGlobalServiceMapperServiceDao.saveGlobalMasterData(unitServiceMasterDetailsDto);
			 
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}



	@Override
	@Transactional
	public Response callProcedure(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) throws ApplicationException {
		try {
			 return  iGlobalServiceMapperServiceDao.callProcedure(unitServiceMasterDetailsDto);
			 
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}

	@Override
	@Transactional
	public Response getAllGlablServiceMaster(UnitServiceMasterDetailsDto unitServiceMasterDetailsDto) throws ApplicationException {
		try {
			return iGlobalServiceMasterServiceDao.getAllGlablServiceMaster(unitServiceMasterDetailsDto);
		}catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
}
