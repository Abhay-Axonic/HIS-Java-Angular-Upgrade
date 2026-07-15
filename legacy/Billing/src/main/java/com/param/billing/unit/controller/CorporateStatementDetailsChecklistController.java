package com.param.billing.unit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.billing.common.ICommonConstants;
import com.param.billing.common.Response;
import com.param.billing.unit.service.ICorporateStatementDetailsChecklistService;
import com.param.global.dto.CorporateStatementDetailsChecklistDto;
import com.param.global.dto.CorporateStatementDto;

@RestController
@RequestMapping(value="/unit")
@SuppressWarnings({"rawtypes","unchecked"})

public class CorporateStatementDetailsChecklistController implements ICommonConstants{
	@Autowired
	ICorporateStatementDetailsChecklistService iCorporateStatementDetailsChecklistService;
	
	
	@RequestMapping(value = "/saveCorporateStatementDetailsChecklist", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody Response saveCorporateStatementDetailsChecklist(@RequestBody CorporateStatementDetailsChecklistDto corporateStatementDetailsChecklistDto) {
		try {
			return iCorporateStatementDetailsChecklistService.saveCorporateStatementDetailsChecklist(corporateStatementDetailsChecklistDto);
		} catch (Exception e) {
			
		}
		return new Response(ERROR, null, null, null, null);
	}

}
