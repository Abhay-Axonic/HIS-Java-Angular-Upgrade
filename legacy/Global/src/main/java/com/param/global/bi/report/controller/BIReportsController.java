package com.param.global.bi.report.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.param.global.bi.report.dto.BIReportsCommonDto;
import com.param.global.bi.report.dto.BIReportsDto;
import com.param.global.bi.report.service.IBIReportsService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IUnitApplicationBillingConfigurationDao;
import com.param.opd.encounter.dto.EncounterMasterDto;

@RestController
@RequestMapping(value = "/api/global")
@SuppressWarnings({ "rawtypes" })
public class BIReportsController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(BIReportsController.class);

	@Autowired
	private IBIReportsService iBIReportsService;
	
	@Autowired
	IUnitApplicationBillingConfigurationDao iUnitApplicationBillingConfigurationDao;

	@RequestMapping(value = "/getBIReportsAll", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBIReportsAll(@RequestBody BIReportsDto bIReportsDto) {

		try {
			return iBIReportsService.getBIReportsAll(bIReportsDto);

		} catch (Exception e) {
			logger.error("Error while fetching BI reports", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getBIReportsAllForCsv", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBIReportsAllForCsv(@RequestBody BIReportsDto bIReportsDto) {

		try {
			return iBIReportsService.getBIReportsAllForCsv(bIReportsDto);

		} catch (Exception e) {
			logger.error("Error while fetching BI reports for csv", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getPendingAndPartialBillPayments", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPendingAndPartialBillPayments(@RequestBody BIReportsCommonDto bIReportsCommonDto) {

		try {
			return iBIReportsService.getPendingAndPartialBillPayments(bIReportsCommonDto);

		} catch (Exception e) {
			logger.error("Error while fetching pending and partial bill payments", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getPendingAndPartialBillPaymentsForCsv", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getPendingAndPartialBillPaymentsForCsv(@RequestBody BIReportsCommonDto bIReportsCommonDto) {

		try {
			return iBIReportsService.getPendingAndPartialBillPaymentsForCsv(bIReportsCommonDto);

		} catch (Exception e) {
			logger.error("Error while fetching pending and partial bill payments for csv", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getUnitWiseDailyPatientCountWithGender", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getUnitWiseDailyPatientCountWithGender(@RequestBody BIReportsCommonDto bIReportsCommonDto) {

		try {
			return iBIReportsService.getUnitWiseDailyPatientCountWithGender(bIReportsCommonDto);

		} catch (Exception e) {
			logger.error("Error while fetching unit wise daily patient count with gender", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getDailyPatientCountWithGenderForCsv", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getDailyPatientCountWithGenderForCsv(@RequestBody BIReportsCommonDto bIReportsCommonDto) {

		try {
			return iBIReportsService.getDailyPatientCountWithGenderForCsv(bIReportsCommonDto);

		} catch (Exception e) {
			logger.error("Error while fetching daily patient count with gender for csv", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getUnitWiseDailyCollection", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getUnitWiseDailyCollection(@RequestBody BIReportsCommonDto bIReportsCommonDto) {

		try {
			return iBIReportsService.getUnitWiseDailyCollection(bIReportsCommonDto);

		} catch (Exception e) {
			logger.error("Error while fetching unit wise daily collection", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/getUnitWiseDailyCollectionForCsv", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getUnitWiseDailyCollectionForCsv(@RequestBody BIReportsCommonDto bIReportsCommonDto) {

		try {
			return iBIReportsService.getUnitWiseDailyCollectionForCsvO(bIReportsCommonDto);

		} catch (Exception e) {
			logger.error("Error while fetching unit wise daily collection for csv", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getBIReportsAllCollectionList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBIReportsAllCollectionList(@RequestBody BIReportsCommonDto bIReportsCommonDto) {

		try {
			return iBIReportsService.getBIReportsAllCollectionList(bIReportsCommonDto);

		} catch (Exception e) {
			logger.error("Error while fetching BI reports all collection list", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@RequestMapping(value = "/getBIReportsAllCollectionNew", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBIReportsAllCollectionNew(@RequestBody BIReportsCommonDto bIReportsCommonDto) {

		try {
			return iBIReportsService.getBIReportsAllCollectionNew(bIReportsCommonDto);

		} catch (Exception e) {
			logger.error("Error while fetching BI reports all collection new", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
	@RequestMapping(value = "/getAllCollectionList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getAllCollectionList(@RequestBody BIReportsCommonDto bIReportsCommonDto) {

		try {
			return iBIReportsService.getAllCollectionList(bIReportsCommonDto);

		} catch (Exception e) {
			logger.error("Error while fetching all collection list", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	//----------------------------Discountpatientwise_BI
	
	@RequestMapping(value="/discountPatientWiseBIReport", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response downloadDiscountPatientWiseBIReport(@RequestBody BIReportsCommonDto bIReportsCommonDto) throws IOException {
		   ByteArrayOutputStream byteOutPutStream = null;
	       CloseableHttpClient httpClient = null;
	       OutputStream output = null;
	   try{
	    
		   httpClient = HttpClientBuilder.create().build();
		   byteOutPutStream  = new ByteArrayOutputStream();
	       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGenerationForBI(bIReportsCommonDto.getOrganizationId(),bIReportsCommonDto.getUnitId());
	       
	       if(API_URL==null)
	    	   return null;
	       
	      String reportParam=" {"
               +"'appName': 'HIS',"
               +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
               +"'reportDesignFilePath': 'Discountpatientwise_BI.rpt',"
               +"'reportFormat': 'PDF',"
               +"'exportFileName': 'Discountpatientwise_BI',"
               +"'reportParams': ["
                 +"{"
	               +"'paramName': 'FromDate',"
			       +"'paramType': 'string',"
			       +"'paramValue':'"+bIReportsCommonDto.getFromDate()+"'"
	              +"}," 
	              +"{"
	               +"'paramName': 'ToDate',"
			       +"'paramType': 'string',"
			       +"'paramValue':'"+bIReportsCommonDto.getToDate()+"'"
	              +"}," 
	              +"{"
	               +"'paramName': 'unit_id',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+bIReportsCommonDto.getUnitId()
	              +"}," 
	              +"{"
	               +"'paramName': 'organization_id',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+bIReportsCommonDto.getOrganizationId()
	              +"}"
               +"]"
               +"}" ;
	      
	       HttpPost  post = new HttpPost(API_URL);
	       post.addHeader("Content-Type","application/json");
	       post.setEntity(new StringEntity(reportParam));
	    
	       HttpResponse result = httpClient.execute(post);
	       HttpEntity resEntity = result.getEntity(); 
	       resEntity.writeTo(byteOutPutStream);
	       
	       byteOutPutStream.toByteArray();
	       httpClient.close();
	       byteOutPutStream.close();
	       return new Response<>(SUCCESS, null,null, null, byteOutPutStream.toByteArray());
	   }catch(Exception e)
	   {
	   logger.error("downloadDiscountPatientWiseBIReport failed", e);
	   }
	   finally {
		   if(output!=null)
			   output.close();
	       if(httpClient!=null)
	    	   httpClient.close();
	       if(byteOutPutStream!=null)
	    	   byteOutPutStream.close();
	    }
	   return null;
	  }

//-------------------------Miscellaneous_expense_BI


@RequestMapping(value="/miscellaneousExpenseBIReport", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
public Response downloadMiscellaneousExpenseBIReport(@RequestBody BIReportsCommonDto bIReportsCommonDto) throws IOException {
	   ByteArrayOutputStream byteOutPutStream = null;
       CloseableHttpClient httpClient = null;
       OutputStream output = null;
   try{
    
	   httpClient = HttpClientBuilder.create().build();
	   byteOutPutStream  = new ByteArrayOutputStream();
       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGenerationForBI(bIReportsCommonDto.getOrganizationId(),bIReportsCommonDto.getUnitId());
       
       if(API_URL==null)
    	   return null;
       
       String reportParam=" {"
               +"'appName': 'HIS',"
               +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
               +"'reportDesignFilePath': 'Miscellaneous_expense_BI.rpt',"
               +"'reportFormat': 'PDF',"
               +"'exportFileName': 'Miscellaneous_expense_BI',"
               +"'reportParams': ["
                 +"{"
	               +"'paramName': 'FromDate',"
			       +"'paramType': 'string',"
			       +"'paramValue':'"+bIReportsCommonDto.getFromDate()+"'"
	              +"}," 
	              +"{"
	               +"'paramName': 'ToDate',"
			       +"'paramType': 'string',"
			       +"'paramValue':'"+bIReportsCommonDto.getToDate()+"'"
	              +"}," 
	              +"{"
	               +"'paramName': 'unit_id',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+bIReportsCommonDto.getUnitId()
	              +"}," 
	              +"{"
	               +"'paramName': 'organization_id',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+bIReportsCommonDto.getOrganizationId()
	              +"}"
               +"]"
               +"}" ;
       HttpPost  post = new HttpPost(API_URL);
       post.addHeader("Content-Type","application/json");
       post.setEntity(new StringEntity(reportParam));
    
       HttpResponse result = httpClient.execute(post);
       HttpEntity resEntity = result.getEntity(); 
       resEntity.writeTo(byteOutPutStream);
       
       byteOutPutStream.toByteArray();
       httpClient.close();
       byteOutPutStream.close();
       return new Response<>(SUCCESS, null,null, null, byteOutPutStream.toByteArray());
   }catch(Exception e)
   {
   logger.error("downloadMiscellaneousExpenseBIReport failed", e);
   }
   finally {
	   if(output!=null)
		   output.close();
       if(httpClient!=null)
    	   httpClient.close();
       if(byteOutPutStream!=null)
    	   byteOutPutStream.close();
    }
   return null;
  }

//----------------------SalesRevenureport_BI

@RequestMapping(value="/salesRevenureportBIReport", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
public Response downloadSalesRevenureportBIReport(@RequestBody BIReportsCommonDto bIReportsCommonDto) throws IOException {
	   ByteArrayOutputStream byteOutPutStream = null;
       CloseableHttpClient httpClient = null;
       OutputStream output = null;
   try{
    
	   httpClient = HttpClientBuilder.create().build();
	   byteOutPutStream  = new ByteArrayOutputStream();
       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGenerationForBI(bIReportsCommonDto.getOrganizationId(),bIReportsCommonDto.getUnitId());
       
       if(API_URL==null)
    	   return null;
       
       String reportParam=" {"
               +"'appName': 'HIS',"
               +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
               +"'reportDesignFilePath': 'SalesRevenureport_BI.rpt',"
               +"'reportFormat': 'PDF',"
               +"'exportFileName': 'SalesRevenureport_BI',"
               +"'reportParams': ["
                 +"{"
	               +"'paramName': 'FromDate',"
			       +"'paramType': 'string',"
			       +"'paramValue':'"+bIReportsCommonDto.getFromDate()+"'"
	              +"}," 
	              +"{"
	               +"'paramName': 'ToDate',"
			       +"'paramType': 'string',"
			       +"'paramValue':'"+bIReportsCommonDto.getToDate()+"'"
	              +"}," 
	              +"{"
	               +"'paramName': 'unit_id',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+bIReportsCommonDto.getUnitId()
	              +"}," 
	              +"{"
	               +"'paramName': 'organization_id',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+bIReportsCommonDto.getOrganizationId()
	              +"}"
               +"]"
               +"}" ;
      
       HttpPost  post = new HttpPost(API_URL);
       post.addHeader("Content-Type","application/json");
       post.setEntity(new StringEntity(reportParam));
    
       HttpResponse result = httpClient.execute(post);
       HttpEntity resEntity = result.getEntity(); 
       resEntity.writeTo(byteOutPutStream);
       
       byteOutPutStream.toByteArray();
       httpClient.close();
       byteOutPutStream.close();
       return new Response<>(SUCCESS, null,null, null, byteOutPutStream.toByteArray());
   }catch(Exception e)
   {
   logger.error("downloadSalesRevenureportBIReport failed", e);
   }
   finally {
	   if(output!=null)
		   output.close();
       if(httpClient!=null)
    	   httpClient.close();
       if(byteOutPutStream!=null)
    	   byteOutPutStream.close();
    }
   return null;
  }


//-----------------------getOutstanding_BI
@RequestMapping(value="/getOutstandingBIReport", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
public Response downloadGetOutstandingBIReport(@RequestBody BIReportsCommonDto bIReportsCommonDto) throws IOException {
	   ByteArrayOutputStream byteOutPutStream = null;
       CloseableHttpClient httpClient = null;
       OutputStream output = null;
   try{
    
	   httpClient = HttpClientBuilder.create().build();
	   byteOutPutStream  = new ByteArrayOutputStream();
       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGenerationForBI(bIReportsCommonDto.getOrganizationId(),bIReportsCommonDto.getUnitId());
       
       if(API_URL==null)
    	   return null;
       
       String reportParam=" {"
               +"'appName': 'HIS',"
               +"'appSecretKey': 'f306b6eccd9d5069d2dc71afe139271a',"
               +"'reportDesignFilePath': 'getOutstanding_BI.rpt',"
               +"'reportFormat': 'PDF',"
               +"'exportFileName': 'getOutstanding_BI',"
               +"'reportParams': ["
                 +"{"
	               +"'paramName': 'FromDate',"
			       +"'paramType': 'string',"
			       +"'paramValue':'"+bIReportsCommonDto.getFromDate()+"'"
	              +"}," 
	              +"{"
	               +"'paramName': 'ToDate',"
			       +"'paramType': 'string',"
			       +"'paramValue':'"+bIReportsCommonDto.getToDate()+"'"
	              +"}," 
	              +"{"
	               +"'paramName': 'unit_id',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+bIReportsCommonDto.getUnitId()
	              +"}," 
	              +"{"
	               +"'paramName': 'organization_id',"
			       +"'paramType': 'Integer',"
			       +"'paramValue':"+bIReportsCommonDto.getOrganizationId()
	              +"}"
               +"]"
               +"}" ;
       HttpPost  post = new HttpPost(API_URL);
       post.addHeader("Content-Type","application/json");
       post.setEntity(new StringEntity(reportParam));
    
       HttpResponse result = httpClient.execute(post);
       HttpEntity resEntity = result.getEntity(); 
       resEntity.writeTo(byteOutPutStream);
       
       byteOutPutStream.toByteArray();
       httpClient.close();
       byteOutPutStream.close();
       return new Response<>(SUCCESS, null,null, null, byteOutPutStream.toByteArray());
   }catch(Exception e)
   {
   logger.error("downloadGetOutstandingBIReport failed", e);
   }
   finally {
	   if(output!=null)
		   output.close();
       if(httpClient!=null)
    	   httpClient.close();
       if(byteOutPutStream!=null)
    	   byteOutPutStream.close();
    }
   return null;
  }

	@RequestMapping(value = "/getBIReportsRoomOccupancy", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBIReportsRoomOccupancy(@RequestBody BIReportsCommonDto bIReportsCommonDto) {
		try {
			return iBIReportsService.getBIReportsRoomOccupancy(bIReportsCommonDto);

		} catch (Exception e) {
			logger.error("Error while fetching BI reports room occupancy", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@RequestMapping(value = "/getBIReports/dashboard", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response getBIReportsForDashboard(@RequestBody BIReportsCommonDto bIReportsCommonDto) {
		try {
			return iBIReportsService.getBIReportsForDashboard(bIReportsCommonDto);

		} catch (Exception e) {
			logger.error("Error while fetching BI reports for dashboard", e);
		}
	return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	//New API For REPORT PRINT for MIS-sakshi
	@RequestMapping(value="/printMISReport", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
	public Response printBIReport(@RequestBody BIReportsCommonDto bIReportsCommonDto) throws IOException {
		   ByteArrayOutputStream byteOutPutStream = null;
	       CloseableHttpClient httpClient = null;
	       OutputStream output = null;
	   try{
	    
		   httpClient = HttpClientBuilder.create().build();
		   byteOutPutStream  = new ByteArrayOutputStream();
	       String API_URL = iUnitApplicationBillingConfigurationDao.getURLForPdfGenerationForBI(bIReportsCommonDto.getOrgId(),bIReportsCommonDto.getUnitId());
	       
	       if(API_URL==null)
	    	   return null;
	              	       
	       String reportParam=bIReportsCommonDto.getCrData();
	       Gson gson = new Gson();

	        // Convert the string to JSON
	        Object jsonObject = gson.fromJson(reportParam, Object.class);	        
	        String jsonString = gson.toJson(jsonObject);
	        
	       HttpPost  post = new HttpPost(API_URL);
	       post.addHeader("Content-Type","application/json");
	       post.setEntity(new StringEntity(jsonString));
	    
	       HttpResponse result = httpClient.execute(post);
	       HttpEntity resEntity = result.getEntity(); 
	       resEntity.writeTo(byteOutPutStream);
	       
	       byteOutPutStream.toByteArray();
	       httpClient.close();
	       byteOutPutStream.close();
	       return new Response<>(SUCCESS, null,null, null, byteOutPutStream.toByteArray());
	   }catch(Exception e)
	   {
	   logger.error("printBIReport failed", e);
	   }
	   finally {
		   if(output!=null)
			   output.close();
	       if(httpClient!=null)
	    	   httpClient.close();
	       if(byteOutPutStream!=null)
	    	   byteOutPutStream.close();
	    }
	   return null;
	  }

	
}


