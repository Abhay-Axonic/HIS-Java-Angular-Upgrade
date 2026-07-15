package com.param.global.service;

import java.io.ByteArrayOutputStream;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;
import com.param.global.dto.TemplateReqDto;
import com.param.global.dto.TemplateResponseDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.global.TTemplateResultDto;

@SuppressWarnings("rawtypes")
public interface ITemplateTransactionService {
	public Response saveTemplateTransaction(TemplateTransactionDto dto)throws ApplicationException;
	public Response updateTemplateTransaction(TemplateTransactionDto dto)throws ApplicationException, Exception;
	public Response getTemplateByPatient(TemplateReqDto dto)throws ApplicationException;
	
	public Response getQuestionaaiireDetails(TemplateReqDto dto)throws ApplicationException;
	public Response getTemplateQuestionaaire(TemplateReqDto dto)throws ApplicationException;
	public Response getTemplateQuestionaaireRadiology(TemplateReqDto dto) throws ApplicationException;
	
	public Response saveAdmissionConcent(AdmissionDto dto) throws ApplicationException;
	public Response updateTemplateAsInactive(TemplateTransactionDto dto)throws ApplicationException;
	public Response getSavedTemplateCount(TemplateTransactionDto templateTransactionDto)throws ApplicationException;
	public Response updateTemplateTransactionRIS(TemplateTransactionDto dto)throws ApplicationException;
	
	
	/**@author Ganesh Chaudhari
	 * @throws Exception */
	public Response sendTemplateURLS(TemplateTransactionDto dto)throws ApplicationException, Exception;
	public Response saveTemplateSummery(TemplateResponseDto dto)throws ApplicationException;
	public Response saveTemplateResult(TTemplateResultDto dto)throws ApplicationException;
	
	public Response printSavedTemplate(TemplateTransactionDto dto)throws ApplicationException;
	public Response printHTMLTemplate(TemplateTransactionDto dto)throws ApplicationException;


    Response getTemplateTransactionForIPDCoversheetByDate(TemplateTransactionDto dto) throws ApplicationException;

	Response getUniqueDateForIPDCoversheet(TemplateTransactionDto dto) throws ApplicationException;
	public Response getTemplateByPatientInOtims(TemplateReqDto templateReqDto) throws ApplicationException;
	public Response updateNABH(TemplateTransactionDto dto)throws ApplicationException;
}
