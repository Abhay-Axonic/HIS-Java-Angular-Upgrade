package com.param.global.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.DocUploadUtility;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ISubSpecialityMasterDao;
import com.param.global.dao.ITemplateMasterDao;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.FormsEngineTransactionDto;
import com.param.global.dto.TemplateMasterDto;
import com.param.global.dto.TemplateReqDto;
import com.param.global.dto.TemplateTransactionDto;
import com.param.global.dto.UnitTemplateConfigDto;
import com.param.global.org.dto.UnitMasterDto;
import com.param.utility.TimeZoneComponent;

@Service
@SuppressWarnings({"rawtypes","unlikely-arg-type"})
public class TemplateMasterServiceImpl implements ITemplateMasterService, ICommonConstants{

	@Autowired
	private ITemplateMasterDao iTemplateMasterDao;
	
	@Autowired
	private ISubSpecialityMasterDao iSubSpecialityMasterDao;
	
	@Autowired
    DocUploadUtility docUploadUtility;
	
	@Override
	@Transactional
	public Response saveTemplate(TemplateMasterDto dto) throws ApplicationException {
		try{
			
			Integer tempateFromatType = 3;
			dto.setCreatedDate(GlobalCommonDateUtils.getStringDate(TimeZoneComponent.getDateByzone(dto.getUnitId()), "dd-M-yyyy HH:mm:ss"));
			dto.setStatus(ACTIVE);
			Response res = iTemplateMasterDao.saveTemplate(dto);
			
			if(dto.getTemplateFormatId() == tempateFromatType && (dto.getTemplateTypeId() == 22 || dto.getTemplateTypeId() == 29 )) {
				
				Response res1 = iTemplateMasterDao.getOrgAndUnitName(dto.getUnitId());
				
				UnitMasterDto unit = (UnitMasterDto)res1.getObject();
				
				docUploadUtility.createS3Folder(unit.getUnitName(), unit.getOrganizationName(),dto.getTitle());
			}
			
			return res;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTemplateList(int orgId, int unitId) throws ApplicationException {
		try{
			return iTemplateMasterDao.getTemplateList(orgId,unitId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTemplateById(int id) throws ApplicationException {
		try{
			return iTemplateMasterDao.getTemplateById(id);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateTemplateStatus(int id, char status) throws ApplicationException {
		try{
			return iTemplateMasterDao.updateTemplateStatus(id, status);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateTemplate(TemplateMasterDto dto) throws ApplicationException {
		try{
			return iTemplateMasterDao.updateTemplate(dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTemplateTypes() throws ApplicationException {
		try{
			return iTemplateMasterDao.getTemplateTypes();
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTemplateByDoctorOrSpeciality(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			return iTemplateMasterDao.getTemplateByDoctorOrSpeciality(templateReqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	
	@Override
	public Response getTemplateQuestionaaireRadiology(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			return iTemplateMasterDao.getTemplateQuestionaaireRadiology(templateReqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTemplatesForComplaints(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			return iTemplateMasterDao.getTemplatesForComplaints(templateReqDto);
		}catch(Exception e){
			
			throw e;
		}
	}
	
	@Override
	@Transactional
	public Response getTemplatesForDiagonosis(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			DoctorMasterDto dto=new DoctorMasterDto();
			dto.setDoctorId(templateReqDto.getDoctorId());
			dto.setOrganizationId(templateReqDto.getOrgId());
			dto.setUnitId(templateReqDto.getUnitId());
			Response res=iSubSpecialityMasterDao.getSubSpecialityByDoctorId(dto);
			if(res.getObject()!=null) {
				DoctorMasterDto dto1=(DoctorMasterDto)res.getObject();
				if(dto1.getSubSpecialityId()!=null) {
				 templateReqDto.setSubSpecialityId(dto1.getSubSpecialityId());
				}
			}
			   return iTemplateMasterDao.getTemplatesForDiagonosis(templateReqDto);
		}catch(Exception e){
			
			throw e;
		}
	}


	@Override
	@Transactional
	public Response getTempalteByService(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			return iTemplateMasterDao.getTempalteByService(templateReqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTemplatesByType(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			return iTemplateMasterDao.getTemplatesByType(templateReqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response isRenderedingSpecilaity(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			return iTemplateMasterDao.isRenderedingSpecilaity(templateReqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTemplateConfigByUnit(UnitTemplateConfigDto reqDto) throws ApplicationException {
		try{
			return iTemplateMasterDao.getTemplateConfigByUnit(reqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response saveFormsEngineTransactionDetails(FormsEngineTransactionDto dto) throws ApplicationException {
		try{
			return iTemplateMasterDao.saveFormsEngineTransactionDetails(dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getTemplatesByTypeAndSpeciality(TemplateReqDto dto) throws ApplicationException {
		try{
			return iTemplateMasterDao.getTemplatesByTypeAndSpeciality(dto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getTemplateBy(TemplateReqDto templateReqDto) throws ApplicationException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	@Transactional
	public Response getTemplatesForHabit(TemplateReqDto templateReqDto) throws ApplicationException {
		// TODO Auto-generated method stub
		try{
			return iTemplateMasterDao.getTemplatesForHabit(templateReqDto);
		}catch(Exception e){
			
			throw e;
		}
		}

	@Override
	@Transactional
	public Response getTemplateBySort(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			return iTemplateMasterDao.getTemplateByDoctorOrSpecialityTemplateBySort(templateReqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getComplateTemplateBySort(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			return iTemplateMasterDao.getTemplateByDoctorOrSpecialityComplaintTemplateBySort(templateReqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updateTemplates(TemplateTransactionDto templateTransactionDto) throws ApplicationException {
		try{
			return iTemplateMasterDao.updateTemplates(templateTransactionDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response getCoversheetTemplateFavourite(TemplateReqDto templateReqDto) throws ApplicationException {
		try{
			return iTemplateMasterDao.getCoversheetTemplateFavourite(templateReqDto);
		}catch(Exception e){
			
			throw e;
		}
	}

	

	



}
