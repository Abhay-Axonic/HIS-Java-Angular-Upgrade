package com.param.global.billing.controller;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.global.model.PatientCouponTemplateMapper;
import com.param.global.billing.dto.PatientCouponTemplateMapperDto;
import com.param.global.billing.service.IPatientCouponTemplateMapperService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("api/billing/Patientcoupon")
@SuppressWarnings({ "rawtypes","unchecked" })
public class PatientCouponTemplateMapperController implements ICommonConstants{

	@Autowired
	IPatientCouponTemplateMapperService iPatientCouponTemplateMapperService;
	
	@RequestMapping(value = "/savePatientCouponTemplateMapper", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response savePatientCouponTemplateMapper(@RequestBody List<PatientCouponTemplateMapperDto> dto) {
		try {
			Response res =  iPatientCouponTemplateMapperService.savePatientCouponTemplateMapper(dto);
			
			if(res.getListObject()!= null && res.getListObject().size()>0) {
				
				List<PatientCouponTemplateMapper> saveList = res.getListObject(); 
			
				for (PatientCouponTemplateMapper patientCouponTemplateMapperDto : saveList) {
					
					
					int count = 0, num = patientCouponTemplateMapperDto.getPatientCouponTemplateMapperId();

					
					
				    for (; num != 0; num /= 10, ++count) {
				    }
					
				    int randomSize = 8-count;
					
					String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
			        StringBuilder sb = new StringBuilder(randomSize);
			        String s =  "";
			        for (int i = 0; i < randomSize; i++) {
			        	int index  = (int)(AlphaNumericString.length() * ThreadLocalRandom.current().nextDouble());
			            sb.append(AlphaNumericString.charAt(index));
			        }
			  
			         s =  sb.toString();
			         s = s + patientCouponTemplateMapperDto.getPatientCouponTemplateMapperId().toString();
			         patientCouponTemplateMapperDto.setCouponCode(s);
			         
				}
				
				 iPatientCouponTemplateMapperService.updateCouponCode(saveList);
				
			}
			
			return res;
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/forceStopPatientCouponTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response forceStopPatientCouponTemplate(@RequestBody PatientCouponTemplateMapperDto dto) {
		try {
			return iPatientCouponTemplateMapperService.forceStopPatientCouponTemplate(dto);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	
	@RequestMapping(value = "/getMappedPatientCouponTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response getMappedPatientCouponTemplate(@RequestBody PatientCouponTemplateMapperDto dto) {
		try {
			return iPatientCouponTemplateMapperService.getMappedPatientCouponTemplate(dto);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
	@RequestMapping(value = "/counsumCouponTemplate", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response counsumCouponTemplate(@RequestBody PatientCouponTemplateMapperDto dto) throws Exception {
		try {
			return iPatientCouponTemplateMapperService.counsumCouponTemplate(dto);
		} catch (Exception e) {
			
			throw e;
		}
	}
	
}
