package com.param.global.billing.dao;

import in.param.exception.ApplicationException;
import org.springframework.web.bind.annotation.ResponseBody;

import com.param.global.billing.dto.CreditNoteReasonMasterDto;
import com.param.global.common.Response;

@SuppressWarnings({"rawtypes"})
public interface ICreditNoteReasonMasterDao {
public @ResponseBody Response saveCreditNoteReasonMaster(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException;
	
	public @ResponseBody Response updateCreditNoteReasonMaster(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException;
	
	public @ResponseBody Response updateCreditNoteReasonMasterStatus(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException;
	
	public @ResponseBody Response getCreditNoteReasonMasterList(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException;
	
	public @ResponseBody Response getCreditNoteReasonMasterById(Integer creditNoteReasonId, Integer organisationId, Integer unitId) throws ApplicationException;
	
	public @ResponseBody Response getCreditNoteReasonMasterCount(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException;
	
	public @ResponseBody Response getActiveCreditNoteReasonMasterList(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException;
	
	public @ResponseBody Response getCreditNoteReasonMasterByName(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException;
	
	public @ResponseBody Response getCreditNoteReasonMasterByNameNotId(CreditNoteReasonMasterDto creditNoteReasonMasterDto) throws ApplicationException;
	
}
