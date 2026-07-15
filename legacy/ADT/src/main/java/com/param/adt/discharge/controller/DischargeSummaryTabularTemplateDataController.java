package com.param.adt.discharge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.param.adt.discharge.dto.DischargeSummaryTabularTemplateDataDto;
import com.param.adt.discharge.service.IDischargeSummaryService;
import com.param.adt.discharge.service.IDischargeSummaryTabularTemplateDataService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("/adt/discharge")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DischargeSummaryTabularTemplateDataController implements ICommonConstants{
	@Autowired
	private IDischargeSummaryTabularTemplateDataService iDischargeSummaryTabularTemplateDataService;
	
	@RequestMapping(value = "/saveDischargeSummTabularTemplateData", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response savePatientTabularTemplateDataMapper(@RequestBody DischargeSummaryTabularTemplateDataDto dischargeSummaryTabularTemplateDataDto){
		
		try {
			return iDischargeSummaryTabularTemplateDataService.saveDischargeSummTabularTemplateData(dischargeSummaryTabularTemplateDataDto);
			
		} catch (Exception e) {
			
			return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	}
}
