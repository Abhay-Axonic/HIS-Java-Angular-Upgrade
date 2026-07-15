package com.param.lis.transaction.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.ServiceException;
import com.param.global.dto.LastInvestigationDto;
import com.param.global.dto.RadiologyOrdersListDto;
import com.param.global.dto.SlotMasterDto;
import com.param.global.dto.UnitApplicationConfigurationDto;
import com.param.global.service.IUnitApplicationBillingConfigurationService;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.Response;
import com.param.lis.global.common.SearchDto;
import com.param.lis.global.dto.BarcodeDto;
import com.param.lis.transaction.dto.CollectedSampleDto;
import com.param.lis.transaction.dto.LabDashBoardDto;
import com.param.lis.transaction.dto.LabReportDocumentDetailsDto;
import com.param.lis.transaction.dto.LabResultMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDto;
import com.param.lis.transaction.dto.LabSampleDetailsMasterDtoIP;
import com.param.lis.transaction.dto.LabSampleMasterDto;
import com.param.lis.transaction.dto.SearchCommonDto;
import com.param.lis.transaction.service.ILabTransactionService;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;
import com.param.workbench.service.IRISWorkbenchService;
import com.param.lis.global.common.ClinicalInterventionProcedureDto;

@RestController
@SuppressWarnings(
{ "rawtypes", "unchecked" })
@RequestMapping(value = "/LIS/transaction/")
public class LabTransactionController implements ICommonConstants, IError
{ 
	@Autowired
	ILabTransactionService iLabTransactionService;
	@Autowired
	IRISWorkbenchService iRISWorkbenchService;
	
	@Autowired
	IUnitApplicationBillingConfigurationService iUnitApplicationBillingConfigurationService;
	
	
	final static Logger logger = Logger.getLogger(LabTransactionController.class);
	
	@RequestMapping(value = "/getPhlebotomyWorlist/{orgId}/{orgUnitId}/{deptId}/{offset}/{recordPerPage}/{isPreOrPostBilling}", method = RequestMethod.GET)
	public @ResponseBody Response getPhlebotomyWorklist(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId,
			@PathVariable(value = "deptId") Integer deptId,
			@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage,@PathVariable(value = "isPreOrPostBilling") Character isPreOrPostBilling)
	{
		Response<LabSampleMasterDto, Integer> response = null;
		try
		{
			response = iLabTransactionService.getPhlebotomyWorklist(orgId, orgUnitId,deptId,offset, recordPerPage,isPreOrPostBilling);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}	
	}
	

	@RequestMapping(value = "/getTotalPhlebotomyWorkRecord", method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTotalPhlebotomyWorkRecord(@RequestBody SearchDto searchDto)
		{
			Response<LabSampleMasterDto, Integer> response = null;
			try
				{
					response = iLabTransactionService.getTotalPhlobotomyRecord(searchDto);
					return response;
				}
			catch (Exception e)
				{
					
					throw e;
				}
		}
	
	
	@RequestMapping(value = "/labServices/Inpatient/{orgId}/{orgUnitId}/{patientId}/{admissionId}/{deptId}", method = RequestMethod.GET)
	public @ResponseBody Response getInPatientOrders(@PathVariable Map<String,String> params)
		{
			Response<LabSampleDetailsMasterDtoIP, Integer> response = null;
			try
				{
				
				int orgId = Integer.parseInt(params.get("orgId").trim());
				int orgUnitId = Integer.parseInt(params.get("orgUnitId").trim());
				int patientId = Integer.parseInt(params.get("patientId").trim());
				int admissionId = Integer.parseInt(params.get("admissionId").trim());
				int deptId = Integer.parseInt(params.get("deptId").trim());
				response = iLabTransactionService.getInPatientOrders(orgId, orgUnitId,patientId,admissionId,deptId);
					return response;
				}
			catch (Exception e)
				{
					
					throw e;
				}
		}
	
	
	
	
	
	
	@RequestMapping(value = "/getPhlebotomyWorlistDetails/{orgId}/{orgUnitId}/{orderId}/{deptId}/{isPreOrPostBilling}", method = RequestMethod.GET)
	public @ResponseBody Response getPhlebotomyWorklistDetails(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId,@PathVariable(value = "orderId") Integer orderId
			,@PathVariable(value = "deptId") Integer deptId,@PathVariable(value = "isPreOrPostBilling") Character isPreOrPostBilling
			)
	{
		Response<LabSampleDetailsMasterDto, Integer> response = null;
		try
		{
			response = iLabTransactionService.getPhlebotomyWorklistDetails(orgId, orgUnitId, orderId,deptId,isPreOrPostBilling);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}	
	}
	
	
	@RequestMapping(value = "/sampleCollection", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody com.param.global.common.Response sampleCollection(@RequestBody LabSampleMasterDto labSampleMasterDto)
	{
		com.param.global.common.Response response = null;
		try
		{
			/*JsonCheck.mapJsonToObject(labSampleMasterDto, LabSampleMasterDto.class);*/
			response = iLabTransactionService.sampleCollection(labSampleMasterDto);
			if(response.getStatus().equals(SUCCESS)) {
				HashSet<Integer> orderIds = (HashSet<Integer>) response.getObject();
				for (Integer ordId : orderIds)
				{
					iLabTransactionService.updateOrderStatus(ordId);
				}
			}
			return response;
		} catch (Exception e)
		{	
			logger.error("Exception", e);
			return new com.param.global.common.Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}	
	}
	
	@RequestMapping(value = "/sampleCollection/IP", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody com.param.global.common.Response sampleCollectionIp(@RequestBody List<LabSampleMasterDto> listLabSampleMasterDto)
	{
		com.param.global.common.Response<LabSampleMasterDto, Integer> response = null;
		try
		{
			response = iLabTransactionService.sampleCollectionIp(listLabSampleMasterDto);
			return response;
		} catch (Exception e)
		{	
			logger.error("Exception", e);
			return new com.param.global.common.Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
		}	
	}
	
	
	@RequestMapping(value = "/getCollectedSample/{orgId}/{orgUnitId}/{deptId}/{offset}/{recordPerPage}", method = RequestMethod.GET)
	public @ResponseBody Response getCollectedSample(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId,@PathVariable(value = "offset") Integer offset,
			@PathVariable(value = "recordPerPage") Integer recordPerPage,@PathVariable(value = "deptId") Integer deptId)
	{
		Response<CollectedSampleDto, Integer> response = null;
		try
		{
			response = iLabTransactionService.getCollectedSample(orgId, orgUnitId,deptId,offset,recordPerPage);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}	
	}
	
	
	@RequestMapping(value = "/sendToCr", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response sendToCr(@RequestBody List<LabSampleDetailsMasterDto> listLabSampleDetailsMasterDto)
	{
		Response<LabSampleDetailsMasterDto, Integer> response = null;
		try
		{
			response = iLabTransactionService.sendToCr(listLabSampleDetailsMasterDto);
			return response;
		} catch (Exception e)
		{
			logger.error("Exection", e);
			throw e;
		}	
	}
	

	
	@RequestMapping(value = "/getTotalSendToCrRecord", method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getTotalSendToCrRecord(@RequestBody SearchDto searchDto)
		{
			Response<LabSampleMasterDto, Integer> response = null;
			try
				{
					response = iLabTransactionService.getTotalSendToCrRecord(searchDto);
					return response;
				}
			catch (Exception e)
				{
					
					throw e;
				}
		}

	

	
	@RequestMapping(value = "/V1/getPhlebotomyWorlistDetails/{orgId}/{orgUnitId}/{orderId}", method = RequestMethod.GET)
	public @ResponseBody Response v1GetPhlebotomyWorklistDetails(@PathVariable(value = "orgId") Integer orgId,
			@PathVariable(value = "orgUnitId") Integer orgUnitId,@PathVariable(value = "orderId") Integer orderId,@PathVariable(value = "deptId") Integer deptId)
	{
		Response<LabSampleDetailsMasterDto, Integer> response = null;
		try
		{
			response = iLabTransactionService.v1GetPhlebotomyWorklistDetails(orgId, orgUnitId, orderId);
			return response;
		} catch (Exception e)
		{
			logger.error("Exception", e);
			throw e;
		}	
	}
	
	/*
	 * Phlebotomy Work list Search
	 */
	  @RequestMapping(value = "/PhlebotomyWorklist/patient/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response searchPhlebotomyWorklistPatient(@RequestBody SearchCommonDto searchCommonDto)
	  {
	      Response<LabDashBoardDto, Integer> response = null;
	      try
	      {
	          response = iLabTransactionService.searchPhlebotomyWorklistPatient(searchCommonDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	  
	  @RequestMapping(value = "/PhlebotomyWorklist/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response getFilteredPhlebotomyWorklistPatient(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	          response = iLabTransactionService.getFilteredPhlebotomyWorklistPatient(searchDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	  
	  /*
		 * Send to Cr Search
		 */
	
	  
	  @RequestMapping(value = "/SendToCR/patient/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response searchSendToCRPatientlist(@RequestBody SearchCommonDto searchCommonDto)
	  {
	      Response<LabDashBoardDto, Integer> response = null;
	      try
	      {
	          response = iLabTransactionService.searchSendToCRPatientlist(searchCommonDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	  
	  @RequestMapping(value = "/SendToCRlist/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response getFilteredSendToCRPatientlist(@RequestBody SearchDto searchDto)
	  {
	      Response<LabSampleMasterDto, Integer> response = null;
	      try
	      {
	    	  
	          response = iLabTransactionService.getFilteredSendToCRPatientlist(searchDto);
	          return response;
	          
	          
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	  
	  
	  @RequestMapping(value = "/GenralLab/sampleNo/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response searchLabGenralLabSampleNo(@RequestBody SearchCommonDto searchCommonDto)
	  {
	      Response<LabDashBoardDto, Integer> response = null;
	      try
	      {
	          response = iLabTransactionService.searchLabGenralLabSampleNo(searchCommonDto);
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	  @RequestMapping(value = "/reprint/Barcode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	  public @ResponseBody Response reprintBarcode(@RequestBody SearchCommonDto dto)
	  {
	      Response<LabDashBoardDto, Integer> response = null;
	      try
	      {
	    	  List<String> sampleIds = dto.getSampleIds();
	          response = iLabTransactionService.reprintBarcode(sampleIds,dto.getLabSampleDetialsId());
	          return response;
	      } catch (Exception e)
	      {
	          logger.error("Exception", e);
	          throw e;
	      }
	  }
	  
	  
	  @RequestMapping(value = "/labServices/Inpatient/radiology/{orgId}/{orgUnitId}/{admissionId}", method = RequestMethod.GET)
		public @ResponseBody Response getInPatientRadiologyOrders(@PathVariable Map<String,String> params)
			{
				Response<LabSampleDetailsMasterDtoIP, Integer> response = null;
				try
					{
					
					int orgId = Integer.parseInt(params.get("orgId").trim());
					int orgUnitId = Integer.parseInt(params.get("orgUnitId").trim());
					int admissionId = Integer.parseInt(params.get("admissionId").trim());
					response = iLabTransactionService.getInPatientRadiologyOrders(orgId, orgUnitId,admissionId);
						return response;
					}
				catch (Exception e)
					{
						
						throw e;
					}
			}

	  
		@RequestMapping(value="/processCyclicServiceAppointment", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response processCyclicServiceAppointment(@RequestBody List<SlotMasterDto> slotMasterDtoList) {
			try {
				return iLabTransactionService.processCyclicServiceAppointment(slotMasterDtoList);
			}catch (Exception e) {
				
				throw e;
			}
			
		}
		
		@RequestMapping(value="/getPerformTestId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getPerformTestId(@RequestBody CollectedSampleDto collectedSampleDto) {
			try {
				return iLabTransactionService.getPerformTestId(collectedSampleDto);
			}catch (Exception e) {
				
				throw e;
			}
			
		}
		
		@RequestMapping(value="/getPatHistoryByEncounterId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getPatHistoryByEncounterId(@RequestBody LabSampleMasterDto labSampleMasterDto) {
			try {
				return iLabTransactionService.getPatHistoryByEncounterId(labSampleMasterDto);
			}catch (Exception e) {
				
				throw e;
			}
			
		}
		
		@RequestMapping(value="/updatePatHistoryByEncounterId", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response updatePatHistoryByEncounterId(@RequestBody EncounterMasterDto encounterMasterDto) {
			try {
				return iLabTransactionService.updatePatHistoryByEncounterId(encounterMasterDto);
			}catch (Exception e) {
				
				throw e;
			}
			
		}
		
		
		@RequestMapping(value="/getPhlebotomistList", method=RequestMethod.POST, consumes=MediaType.APPLICATION_JSON_VALUE)
		public @ResponseBody Response getPhlebotomistList(@RequestBody LabSampleMasterDto labSampleMasterDto) {
			try {
				return iLabTransactionService.getPhlebotomistList(labSampleMasterDto);
			}catch (Exception e) {
				
				throw e;
			}
			
		}
		
		
		@RequestMapping(value = "/userAllocationHomeCollection/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		  public @ResponseBody Response userAllocationHomeCollectionSearch(@RequestBody SearchDto searchDto)
		  {
		      Response<LabSampleMasterDto, Integer> response = null;
		      try
		      {
		          response = iLabTransactionService.userAllocationHomeCollectionSearch(searchDto);
		          return response;
		      } catch (Exception e)
		      {
		          logger.error("Exception", e);
		          throw e;
		      }
		  }
		  
		@RequestMapping(value = "/labSampleHomeCollection/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		  public @ResponseBody Response labSampleHomeCollectionSearch(@RequestBody SearchDto searchDto)
		  {
		      Response<LabSampleMasterDto, Integer> response = null;
		      try
		      {
		          response = iLabTransactionService.labSampleHomeCollectionSearch(searchDto);
		          return response;
		      } catch (Exception e)
		      {
		          logger.error("Exception", e);
		          throw e;
		      }
		  }
		
		
		@RequestMapping(value = "/getHomeColletionTestList/{orgId}/{orgUnitId}/{orderId}/{deptId}/{isPreOrPostBilling}", method = RequestMethod.GET)
		public @ResponseBody Response getHomeColletionTestList(@PathVariable(value = "orgId") Integer orgId,
				@PathVariable(value = "orgUnitId") Integer orgUnitId,@PathVariable(value = "orderId") Integer orderId
				,@PathVariable(value = "deptId") Integer deptId,@PathVariable(value = "isPreOrPostBilling") Character isPreOrPostBilling
				)
		{
			Response<LabSampleDetailsMasterDto, Integer> response = null;
			try
			{
				response = iLabTransactionService.getHomeColletionTestList(orgId, orgUnitId, orderId,deptId,isPreOrPostBilling);
				return response;
			} catch (Exception e)
			{
				logger.error("Exception", e);
				throw e;
			}	
		}
		
		@RequestMapping(value = "/updateLabSampleDetails/outsource", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		  public @ResponseBody Response updateLabSampleDetails(@RequestBody SearchDto searchDto)
		  {
		      Response<LabSampleMasterDto, Integer> response = null;
		      try
		      {
		          response = iLabTransactionService.updateLabSampleDetails(searchDto);
		          return response;
		      } catch (Exception e)
		      {
		          logger.error("Exception", e);
		          throw e;
		      }
		  }
		  
		
		
		@RequestMapping(value = "/updateAssignUnitAndTestId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		  public @ResponseBody Response updateAssignUnitAndTestId(@RequestBody LabSampleDetailsMasterDto dto)
		  {
		      Response<LabSampleMasterDto, Integer> response = null;
		      try
		      {
		          response = iLabTransactionService.updateAssignUnitAndTestId(dto);
		          return response;
		      } catch (Exception e)
		      {
		          logger.error("Exception", e);
		          throw e;
		      }
		  }
		
		
		@RequestMapping(value = "/getPatientLastHistoryOfTest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		  public @ResponseBody Response getPatientLastHistoryOfTest(@RequestBody LastInvestigationDto dto)
		  {
		      Response<LabSampleMasterDto, Integer> response = null;
		      try
		      {
		          response = iLabTransactionService.getPatientLastHistoryOfTest(dto);
		          return response;
		      } catch (Exception e)
		      {
		          logger.error("Exception", e);
		          throw e;
		      }
		  }
		
		
		  @RequestMapping(value = "/PhlebotomyWorklist/Type/Search", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		  public @ResponseBody Response getPhlebotomyWorklistType(@RequestBody SearchDto searchDto)
		  {
		      Response<LabSampleMasterDto, Integer> response = null;
		      try
		      {
		          response = iLabTransactionService.getPhlebotomyWorklistType(searchDto);
		          return response;
		      } catch (Exception e)
		      {
		          logger.error("Exception", e);
		          throw e;
		      }
		  }
		  
		  @RequestMapping(value = "/labSortingList", method = RequestMethod.GET)
		  public @ResponseBody Response getLabSortingList()
		  {
		      Response<LabSampleMasterDto, Integer> response = null;
		      try
		      {
		          response = iLabTransactionService.getLabSortingList();
		          return response;
		      } catch (Exception e)
		      {
		          logger.error("Exception", e);
		          throw e;
		      }
		  }
		  
		  
		  @RequestMapping(value = "/getPhlebotomyWorklistOnNewOPBill", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
		  public @ResponseBody Response getPhlebotomyWorklistOnNewOPBill(@RequestBody List<SearchDto> searchList)
		  {
		      Response<LabSampleMasterDto, Integer> response = null;
		      try
		      {
		          response = iLabTransactionService.getPhlebotomyWorklistOnNewOPBill(searchList);
		          return response;
		      } catch (Exception e)
		      {
		          logger.error("Exception", e);
		          throw e;
		      }
		  }
		  
		  
		  
		  
		  
		  
		  @RequestMapping(value = "/sampleCollectionOfOPDBilling", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			public @ResponseBody com.param.global.common.Response sampleCollectionOfOPDBilling(@RequestBody List<LabSampleMasterDto> labSampleMasterDto)
			{
				com.param.global.common.Response response = null;
				try
				{
					List<BarcodeDto> barcodeDtoList = new ArrayList<>();
					for (LabSampleMasterDto labSampleMasterDto2 : labSampleMasterDto) {
						response = iLabTransactionService.sampleCollection(labSampleMasterDto2);
						if(response.getStatus().equals(SUCCESS)) {
							HashSet<Integer> orderIds = (HashSet<Integer>) response.getObject();
							barcodeDtoList.addAll(response.getListObject());
							for (Integer ordId : orderIds)
							{
								iLabTransactionService.updateOrderStatus(ordId);
							}
						}
					}
					return new com.param.global.common.Response(SUCCESS, SUCCESS_200, SAMPLE_COLLECT_SUCC, barcodeDtoList,null);
					//return response;
				} catch (Exception e)
				{	
					logger.error("Exception", e);
					return new com.param.global.common.Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
				}	
			}

			
			 @RequestMapping(value = "/printBarcodeLab", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			  public @ResponseBody Response printBarcodeLab(@RequestBody RadiologyOrdersListDto dto)
			  {
			      try
			      {
			          return iLabTransactionService.printBarcodeLab(dto);
			      } catch (Exception e)
			      {
			          logger.error("Exception", e);
			          throw e;
			      }
			  }
			 @RequestMapping(value = "/checkForOrderCompletion", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
				public @ResponseBody Response checkForOrderCompletion(@RequestBody SearchDto searchDto)
				{
					try
					{
						return iLabTransactionService.checkForOrderCompletion(searchDto);
					} catch (Exception e)
					{
						 
				         throw e;
					}
				}
			 
			 
			 @RequestMapping(value = "/uploadLabReportDocument", headers=("content-type=multipart/*"), method = RequestMethod.POST)
		     public @ResponseBody Response uploadLabReportDocument(
		    		 @RequestParam("testId") Integer testId,
		    		 @RequestParam("createdBy") Integer createdBy,
		    		 @RequestParam("updatedBy") Integer updatedBy,
		    		 @RequestParam("unitId") Integer unitId,
		    		 @RequestParam("organizationId") Integer organizationId,
		    		 @RequestParam("labSampleDtlsId") Integer labSampleDtlsId,
		    		 @RequestParam("file") MultipartFile file
		    		 )
		         {
			      try
			      {
			    	  LabReportDocumentDetailsDto labReportDocumentDetailsDto = new LabReportDocumentDetailsDto();
				    	  labReportDocumentDetailsDto.setTestId(testId);
				    	  labReportDocumentDetailsDto.setCreatedBy(createdBy);
				    	  labReportDocumentDetailsDto.setUpdatedBy(updatedBy);
				    	  labReportDocumentDetailsDto.setOrganizationId(organizationId);
				    	  labReportDocumentDetailsDto.setUnitId(unitId);
				    	  labReportDocumentDetailsDto.setFile(file);
				    	  labReportDocumentDetailsDto.setLabSampleDtlsId(labSampleDtlsId);
				    	  labReportDocumentDetailsDto.setFileName(file.getOriginalFilename());
				    	  labReportDocumentDetailsDto.setImage(file.getBytes());
			         return iLabTransactionService.uploadLabReportDocument(labReportDocumentDetailsDto);
			       }catch(Exception e)
			      {
			           logger.error("Exception", e);
			           throw new ServiceException(e.getMessage());
			      }
		       }
			 
			 @RequestMapping(value = "/getLabReportDocumentDetailsByTest", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
				public @ResponseBody Response getLabReportDocumentDetailsByTest(@RequestBody SearchDto searchDto)
				{
					try
					{
						return iLabTransactionService.getLabReportDocumentDetailsByTest(searchDto);
					} catch (Exception e)
					{
						 
				         throw e;
					}
				}
			 
			 @RequestMapping(value = "/validateBarcode", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
				public @ResponseBody Response validateBarcode(@RequestBody LabSampleDetailsMasterDto searchDto)
				{
					try
					{
						return iLabTransactionService.validateBarcode(searchDto);
					} catch (Exception e)
					{
						 
				         throw e;
					}
				}
			 
			 @SuppressWarnings("unused")
			  @RequestMapping(value = "/PhlebotomyWorklist/getCompanyOrInsuranceNameById", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			  public @ResponseBody Response getCompanyOrInsuranceNameById(@RequestBody LabSampleMasterDto dto)
			  {
				Response<LabSampleMasterDto, Integer> response = null;
			      try
			      {
			          return iLabTransactionService.getCompanyOrInsuranceNameById(dto);
			      } catch (Exception e)
			      {
			          logger.error("Exception", e);
			          throw e;
			      }
			  }
			 
			 @RequestMapping(value = "/labReportPDF/getBinaryStringOfLABReport", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			  public @ResponseBody Response getBinaryStringOfLABReport(@RequestBody SearchDto dto) throws Exception
			  {
			      try
			      {
			          return iLabTransactionService.getBinaryStringOfLABReport(dto);
			      } catch (Exception e)
			      {
			          logger.error("Exception", e);
			          throw e;
			      }
			  }
			  @RequestMapping(value = "/getDatesByLabDetialsId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			  public @ResponseBody Response getDatesByLabDetialsId(@RequestBody LabSampleDetailsMasterDto dto)
			  {
				Response<LabSampleMasterDto, Integer> response = null;
			      try
			      {
			          return iLabTransactionService.getDatesByLabDetialsId(dto);
			      } catch (Exception e)
			      {
			          logger.error("Exception", e);
			          throw e;
			      }
			  }
			 
			 
			  @RequestMapping(value = "/appGetPhlebotomyWorklist/getPhlebotomyWorklistOnNewOPBill", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			  public @ResponseBody Response appGetPhlebotomyWorklist(@RequestBody SearchDto obj)
			  {
			      Response<LabSampleMasterDto, Integer> response = null;
			      try
			      {
			    	  List<SearchDto> searchList = new ArrayList<>();
			    	  if(obj.getOrderDetailsMasterDtosList()!=null && obj.getOrderDetailsMasterDtosList().size()>0) {
			    		  searchList = obj.getOrderDetailsMasterDtosList();
			    		  response = iLabTransactionService.getPhlebotomyWorklistOnNewOPBill(searchList);
			    	  }
			          return response;
			      } catch (Exception e)
			      {
			          logger.error("Exception", e);
			          throw e;
			      }
			  }
			  
			  @RequestMapping(value = "/updateDatesByLabDetialsId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			  public @ResponseBody Response updateDatesByLabDetialsId(@RequestBody LabSampleDetailsMasterDto dto)
			  {
				Response<LabSampleMasterDto, Integer> response = null;
			      try
			      {
			          return iLabTransactionService.updateDatesByLabDetialsId(dto);
			      } catch (Exception e)
			      {
			          logger.error("Exception", e);
			          throw e;
			      }
			  }
			  
			  @RequestMapping(value = "/appSampleCollection/collectSample", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
				public @ResponseBody com.param.global.common.Response appSampleCollection(@RequestBody LabSampleMasterDto labSampleMasterDto)
				{
					com.param.global.common.Response response = null;
					try
					{
							List<BarcodeDto> barcodeDtoList = new ArrayList<>();
								
							UnitApplicationConfigurationDto unitApplicationConfigurationDto = new UnitApplicationConfigurationDto();
								unitApplicationConfigurationDto.setOrganizationId(labSampleMasterDto.getOrgId());
								unitApplicationConfigurationDto.setUnitId(labSampleMasterDto.getOrgUnitId());
								com.param.global.common.Response appConfigRes = iUnitApplicationBillingConfigurationService.getUnitApplicationBillingConfigByOrgUnit(unitApplicationConfigurationDto);
									unitApplicationConfigurationDto = (UnitApplicationConfigurationDto)appConfigRes.getObject();
								
									labSampleMasterDto.setVisitTypeId(OP);
									labSampleMasterDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
									labSampleMasterDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
									labSampleMasterDto.setCreatedDate(TimeZoneComponent.getDateByzone(labSampleMasterDto.getOrgUnitId()));
									labSampleMasterDto.setUpdatedDate(TimeZoneComponent.getDateByzone(labSampleMasterDto.getOrgUnitId()));
									if(labSampleMasterDto.getListLabSampleDetailsMaster()!=null && labSampleMasterDto.getListLabSampleDetailsMaster().size()>0)
									{
										for (LabSampleDetailsMasterDto subDto : labSampleMasterDto.getListLabSampleDetailsMaster()) {
											subDto.setOrgId(labSampleMasterDto.getOrgId());
											subDto.setOrgUnitId(labSampleMasterDto.getOrgUnitId());
											subDto.setOrderQty(1);
											subDto.setSampleGenBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
											subDto.setIsOutsourced('N');
											subDto.setSampleStatusId(2); //sample collected
											subDto.setSampleCollectionBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
											subDto.setCreatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
											subDto.setUpdatedBy(unitApplicationConfigurationDto.getWebAppUserId()!=null ? unitApplicationConfigurationDto.getWebAppUserId():null);
											subDto.setCreatedDate(TimeZoneComponent.getDateByzone(labSampleMasterDto.getOrgUnitId()));
											subDto.setUpdatedDate(TimeZoneComponent.getDateByzone(labSampleMasterDto.getOrgUnitId()));
											subDto.setAssignUnit(labSampleMasterDto.getOrgUnitId());
											subDto.setPerformTestId(subDto.getTestId());
											subDto.setTempOutSource('N');
											subDto.setSampleCollection('Y');
											subDto.setIsReportRecall(false);
											subDto.setIsCentrifugationReq('N');
											subDto.setIsAlliquoteReq('N');
										}
									}
									response = iLabTransactionService.sampleCollection(labSampleMasterDto);
									if(response.getStatus().equals(SUCCESS)) {
										HashSet<Integer> orderIds = (HashSet<Integer>) response.getObject();
										barcodeDtoList.addAll(response.getListObject());
										for (Integer ordId : orderIds)
										{
											iLabTransactionService.updateOrderStatus(ordId);
										}
									}
							return new com.param.global.common.Response(SUCCESS, SUCCESS_200, SAMPLE_COLLECT_SUCC, barcodeDtoList,null);
					} catch (Exception e)
					{	
						logger.error("Exception", e);
						throw e;
					}	
				}
			  @RequestMapping(value = "/barcodePrinting", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			  public @ResponseBody Response printBarcodeLab1(@RequestBody List<RadiologyOrdersListDto> list)
			  {
			      try
			      {
			          return iLabTransactionService.printBarcodeLab1(list);
			      } catch (Exception e)
			      {
			          logger.error("Exception", e);
			          throw e;
			      }
			  }
			  
			  @RequestMapping(value = "/PhlebotomyWorklistDetails", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			  public @ResponseBody Response PhlebotomyWorklistDetails(@RequestBody SearchDto searchDto)
			  {
			      Response<LabSampleMasterDto, Integer> response = null;
			      try
			      {
			          response = iLabTransactionService.PhlebotomyWorklistDetails(searchDto);
			          return response;
			      } catch (Exception e)
			      {
			          logger.error("Exception", e);
			          return new Response(ERROR, ERR_500, ERR_500_MESSAGE, null, null);
			      }
			  }
			  @RequestMapping(value = "/wristBandPrnPrint", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			  public @ResponseBody Response printBarcodeLab2(@RequestBody RadiologyOrdersListDto dto)
			  {
			      try
			      {
			          return iLabTransactionService.printBarcodeLab2(dto);
			      } catch (Exception e)
			      {
			          logger.error("Exception", e);
			          throw e;
			      }
			  }
			  
			  
			  @RequestMapping(value = "/amendReportReason", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
			  public @ResponseBody Response amendReportReason(@RequestBody LabResultMasterDto dto)
			  {
			      try
			      {
			          return iLabTransactionService.amendReportReason(dto);
			      } catch (Exception e)
			      {
			          logger.error("Exception", e);
			          throw e;
			      }
			  }
			  
			  
			  @RequestMapping(value = "/getSampleData/{number}/{interfaceName}/{sampleID}/{unitId}/{selectedDate}", method = RequestMethod.GET)
				public @ResponseBody Response getSampleData(@PathVariable Map<String,String> params)
					{
						Response<LabSampleMasterDto, Integer> response = null;
						try
							{

							int orgUnitId = Integer.parseInt(params.get("unitId").trim());
							int apiNumber = Integer.parseInt(params.get("number").trim());
							String sampleID = (params.get("sampleID").toString().trim());
							String interfaceName  = (params.get("interfaceName").toString().trim());
							String selectedDate = (params.get("selectedDate").toString().trim());

							response = iLabTransactionService.getSampleDataForInterface(interfaceName, sampleID, selectedDate, apiNumber, orgUnitId);
								return response;
							}
						catch (Exception e)
							{
								
								throw e;
							}
					}

	@RequestMapping(value = "/getPocLabServices/ER", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getPOCLabServices(@RequestBody SearchDto searchDto)
	{
		Response<LabSampleDetailsMasterDtoIP, Integer> response = null;
		try
		{

			response = iLabTransactionService.getPOCLabServices(searchDto);
			return response;
		}
		catch (Exception e)
		{

			throw e;
		}
	}

	@RequestMapping(value = "/saveIntervention/ER", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveIntervention(@RequestBody ClinicalInterventionProcedureDto clinicalInterventionProcedureDto)
	{
		Response<LabSampleDetailsMasterDtoIP, Integer> response = null;
		try
		{

			response = iLabTransactionService.saveIntervention(clinicalInterventionProcedureDto);
			return response;
		}
		catch (Exception e)
		{

			throw e;
		}
	}

	@RequestMapping(value = "/updateInterventionForCompleteProcedure/ER", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response updateInterventionForCompleteProcedure(@RequestBody ClinicalInterventionProcedureDto clinicalInterventionProcedureDto)
	{
		Response<LabSampleDetailsMasterDtoIP, Integer> response = null;
		try
		{

			response = iLabTransactionService.updateInterventionForCompleteProcedure(clinicalInterventionProcedureDto);
			return response;
		}
		catch (Exception e)
		{

			throw e;
		}
	}
}
