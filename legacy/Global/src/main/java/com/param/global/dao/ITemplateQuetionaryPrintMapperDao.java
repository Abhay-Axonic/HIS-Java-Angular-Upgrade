package com.param.global.dao;
import com.param.global.common.Response;
import com.param.global.dto.TemplateResponseDto;

import in.param.exception.ApplicationException;

import com.param.global.service.TemplateQuetionaryPrintDto;
@SuppressWarnings("rawtypes")
public interface ITemplateQuetionaryPrintMapperDao {

	Response saveTemplateQuetionaryPrintData(TemplateQuetionaryPrintDto dto1)throws ApplicationException;

	Response updateTemplateTransactionData(TemplateResponseDto dto)throws ApplicationException;

	Response updateTemplateQuetionaryPrintData(Integer tempTranId)throws ApplicationException;

}
