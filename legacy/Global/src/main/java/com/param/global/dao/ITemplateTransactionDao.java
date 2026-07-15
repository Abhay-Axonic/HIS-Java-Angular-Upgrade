package com.param.global.dao;

import java.io.ByteArrayOutputStream;


import in.param.exception.ApplicationException;

import com.param.entity.lis.transaction.LabSampleDetailsMaster;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.TemplateReqDto;
import com.param.global.dto.TemplateResponseDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.global.TTemplateResultDto;
import com.param.global.model.TemplateTransaction;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITemplateTransactionDao extends IGenericDao<TemplateTransaction, Integer>{
	public Response saveTemplateTransaction(TemplateTransactionDto dto)throws ApplicationException;
	public Response updateTemplateTransaction(TemplateTransactionDto dto)throws ApplicationException;
	public Response getTemplateByPatient(TemplateReqDto dto)throws ApplicationException;
	public Response getQuestionaaiireDetails(TemplateReqDto dto)throws ApplicationException;
	public Response getTemplateQuestionaaire(TemplateReqDto dto)throws ApplicationException;
	public Response saveAdmissionConcent(AdmissionDto dto) throws ApplicationException;
	public Response updateTemplateAsInactive(TemplateTransactionDto dto)throws ApplicationException;
	public Response getSavedTemplateCount(TemplateTransactionDto templateTransactionDto)throws ApplicationException;

	public Response saveTemplateResult(TTemplateResultDto dto)throws ApplicationException;
	public Response updateLabSampleDetailsMaster(LabSampleDetailsMaster master);
	public Response updatePdfByte(byte[] bs, Integer templateTransactionId)throws ApplicationException;
	public Response getPasswordAndUserId(Integer unitId, Integer organizationId);
	public void updateLabSampleDetailsMailStatus(Integer labSampleDtlsId)throws ApplicationException;
	public Response checkPateintConsent(String conversationId, String HtmlString)throws ApplicationException, Exception;
	public Response printSavedTemplate(TemplateTransactionDto dto)throws ApplicationException;
	public Response printHTMLTemplate(TemplateTransactionDto dto)throws ApplicationException;
	public Response checkTemplateTransactionExits(TemplateTransactionDto dto)throws ApplicationException;
	public Response getTemplateByPatientInOtims(TemplateReqDto templateReqDto)throws ApplicationException;
	
}
