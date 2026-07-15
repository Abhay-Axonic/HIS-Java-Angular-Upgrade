package com.param.global.dao;

import in.param.exception.ApplicationException;
import org.springframework.web.bind.annotation.RequestBody;

import com.param.global.common.Response;
import com.param.global.dto.FormsEngineTransactionDto;
import com.param.global.dto.TemplateMasterDto;
import com.param.global.dto.TemplateReqDto;
import com.param.global.dto.TemplateResponseDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.UnitTemplateConfigDto;
import com.param.global.model.TemplateMaster;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITemplateMasterDao extends IGenericDao<TemplateMaster, Integer>{
	public Response saveTemplate(TemplateMasterDto dto)throws ApplicationException;
	public Response getTemplateList(int orgId, int unitId)throws ApplicationException;
	public Response getTemplateById(int id)throws ApplicationException;
	public Response updateTemplateStatus(int id,char status)throws ApplicationException;
	public Response updateTemplate(TemplateMasterDto dto)throws ApplicationException;
	public Response getTemplateTypes()throws ApplicationException;
	public Response getTemplatesForOPCoversheet(int doctorId, int specialtyId, int orgId, int unitId)throws ApplicationException;
	public Response getTemplateByDoctorOrSpeciality(TemplateReqDto templateReqDto)throws ApplicationException;
	public Response getTemplatesForComplaints(TemplateReqDto templateReqDto)throws ApplicationException;
	public Response getTemplatesForDiagonosis(TemplateReqDto templateReqDto)throws ApplicationException;
	public Response getTempalteByService(TemplateReqDto templateReqDto)throws ApplicationException;
	public Response getTemplatesByType(TemplateReqDto templateReqDto)throws ApplicationException;
	public Response isRenderedingSpecilaity(@RequestBody TemplateReqDto templateReqDto)throws ApplicationException;
	public Response getTemplateQuestionaaireRadiology(TemplateReqDto templateReqDto)throws ApplicationException;
	public Response getTemplateConfigByUnit(UnitTemplateConfigDto reqDto)throws ApplicationException;
	public Response saveFormsEngineTransactionDetails(FormsEngineTransactionDto dto)throws ApplicationException;
	public Response getTemplateByConversationId(TemplateResponseDto dto)throws ApplicationException;
	public Response getTemplatesByTypeAndSpeciality(TemplateReqDto dto)throws ApplicationException;
	public Response getTemplatesForHabit(TemplateReqDto templateReqDto)throws ApplicationException;
	public Response getTemplateByDoctorOrSpecialityTemplateBySort(TemplateReqDto templateReqDto)throws ApplicationException;
	public Response getTemplateByDoctorOrSpecialityComplaintTemplateBySort(TemplateReqDto templateReqDto) throws ApplicationException;
	public Response updateTemplates(TemplateTransactionDto templateTransactionDto)throws ApplicationException;
	public Response getCoversheetTemplateFavourite(TemplateReqDto templateReqDto)throws ApplicationException;
	public Response getOrgAndUnitName(Integer integer)throws ApplicationException;
	public Integer getTemplateTypeId(Integer templateId)throws ApplicationException;
	public Response getSpecialityName(String conversationId)throws ApplicationException;
	public Response getUpdatePath(String docPath, Integer templateTransactionId)throws ApplicationException;
	

}
