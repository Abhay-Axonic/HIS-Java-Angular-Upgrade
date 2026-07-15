package com.param.global.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.dto.PatientPanCardDocDto;
import com.param.global.service.IPatientPanCardDocService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@SuppressWarnings({ "rawtypes"})
@RequestMapping(value="billing/PanCard")
public class PatientPanCardDocController  implements IError, ICommonConstants{
	final static Logger logger = Logger.getLogger(PatientPanCardDocController.class);


	@Autowired
	IPatientPanCardDocService iPatientPanCardDocService;
	
		
	 @RequestMapping(value = "/uploadDoc", headers=("content-type=multipart/*"), method = RequestMethod.POST)
     public @ResponseBody Response saveDocumentAgainstEncounter(
    		 @RequestParam("organizationId") Integer organizationId,
    		 @RequestParam("billId") Integer billId,
    		 @RequestParam("isDeposite") Boolean isDeposite,
    		 @RequestParam("isPharmacy") Boolean isPharmacy,
    		 @RequestParam("isMiscellaneous") Boolean isMiscellaneous,
    		 @RequestParam("unitId") Integer unitId,
    		 @RequestParam("documentName") String documentName,
    		 @RequestParam("file") MultipartFile file
    		 )
         {
	      try
	      {
	    	  PatientPanCardDocDto dto = new PatientPanCardDocDto();
	    	  dto.setOrganizationId(organizationId);
	    	  dto.setDocName(documentName);
	    	  dto.setBillId(billId);
	    	  dto.setIsDeposite(isDeposite);
	    	  dto.setIsPharmacy(isPharmacy);
	    	  dto.setIsMiscellaneous(isMiscellaneous);
	    	  dto.setUnitId(unitId);
	    	  dto.setFile(file);
	         return iPatientPanCardDocService.savePanCardDocument(dto);
	       }catch(Exception e)
	      {
	      logger.error("method failed", e);
	      }
	      return new Response<>(ERROR, null, null, null, null);
       }
	 
	 @RequestMapping(value = "/url/{billId}/{screenId}", method = RequestMethod.GET)
		public Response getPanCardDetailsById(@PathVariable("billId")int billId,@PathVariable("screenId")int screenId,HttpServletRequest request){
			try{
				
				return iPatientPanCardDocService.getPanCardDetailsById(billId,screenId);
			}catch(Exception e){
			logger.error("getPanCardDetailsById failed", e);
				return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
			}
		}
	 @RequestMapping(value = "/inwardDataupdate/{consumenetamount}/{mateinwardid}/{id}", method = RequestMethod.GET)
		public Response inwardDataupdate(@PathVariable("consumenetamount")Double consumenetamount,@PathVariable("mateinwardid")Integer mateinwardid,@PathVariable("id")Integer id,HttpServletRequest request){
			try{
				
				return iPatientPanCardDocService.inwardDataupdate(consumenetamount,mateinwardid,id);
			}catch(Exception e){
			logger.error("inwardDataupdate failed", e);
				return new Response<>(ERROR, ERROR_CODE, e.getMessage(), null, null);
			}
		}
	
}
