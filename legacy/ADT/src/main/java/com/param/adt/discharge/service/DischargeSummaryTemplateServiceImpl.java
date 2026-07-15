package com.param.adt.discharge.service;


import java.util.ArrayList;
import java.util.List;

import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.discharge.dao.IDischargeSummaryTemplateDao;
import com.param.adt.discharge.dao.IDischargeSummaryTemplateMapperDao;
import com.param.adt.discharge.dao.IDischargeTemplateDepartmentMapperDao;
import com.param.adt.discharge.dto.DischargeSummaryTemplateDto;
import com.param.adt.discharge.dto.DischargeSummaryTemplateMapperDto;
import com.param.adt.discharge.dto.DischargeTemplateDepartmentMapperDto;
import com.param.adt.discharge.model.DischargeSummaryTemplateMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@Service
@SuppressWarnings({"rawtypes"})
public class DischargeSummaryTemplateServiceImpl implements IDischargeSummaryTemplateService, ICommonConstants{

	@Autowired
	IDischargeSummaryTemplateDao iDischargeSummaryTemplateDao;
	
	@Autowired
	IDischargeSummaryTemplateMapperDao iDischargeSummaryTemplateMapperDao;
	
	@Autowired
	IDischargeTemplateDepartmentMapperDao iDischargeTemplateDepartmentMapperDao;
	
	@Override
	public Response saveDischargeSummaryTemplate(DischargeSummaryTemplateDto dischargeSummaryTemplateDto) throws ApplicationException {
		try {
			DischargeSummaryTemplateMaster dischargeSummaryTemplateMaster= null;
			Response res = iDischargeSummaryTemplateDao.getDischargeSummaryTemplateByName(dischargeSummaryTemplateDto);
			if(res.getListObject() != null && res.getListObject().size() > 0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				
			}else{
				Response response = iDischargeSummaryTemplateDao.saveDischargeSummaryTemplate(dischargeSummaryTemplateDto);
				if(response.getStatus() == SUCCESS && response.getObject() != null){
					 dischargeSummaryTemplateMaster = (DischargeSummaryTemplateMaster) response.getObject();
					
					if(dischargeSummaryTemplateDto.getSelectDischargeSummaryTemplateList() != null && dischargeSummaryTemplateDto.getSelectDischargeSummaryTemplateList().size() >0)
					{
						for(DischargeSummaryTemplateMapperDto dischargeSummaryTemplateMapperDto : dischargeSummaryTemplateDto.getSelectDischargeSummaryTemplateList()){
						  if(dischargeSummaryTemplateMapperDto!=null)
						  {
							  DischargeSummaryTemplateMapperDto dto=new DischargeSummaryTemplateMapperDto();
							  dto.setDischargeSummaryTempId(dischargeSummaryTemplateMaster.getDischargeSummaryTempId());
							  dto.setDischargeFieldId(dischargeSummaryTemplateMapperDto.getDischargeFieldId());
						       dto.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
						       dto.setOrganizationId(dischargeSummaryTemplateDto.getOrganizationId());
								dto.setUnitId(dischargeSummaryTemplateDto.getUnitId());
								dto.setStatus(dischargeSummaryTemplateDto.getStatus());
								dto.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
								dto.setCreatedDate(dischargeSummaryTemplateDto.getCreatedDate());
								dto.setUpdatedBy(dischargeSummaryTemplateDto.getUpdatedBy());
								dto.setUpdatedDate(dischargeSummaryTemplateDto.getUpdatedDate());
						    iDischargeSummaryTemplateMapperDao.saveDischargeSummaryFieldDetails(dto);						    
						  }
						}
					}
					if(dischargeSummaryTemplateDto.getSelectDischargeTemplateDepartmentList() != null && dischargeSummaryTemplateDto.getSelectDischargeTemplateDepartmentList().size() >0)
					{
						for(DischargeTemplateDepartmentMapperDto deptTempDto : dischargeSummaryTemplateDto.getSelectDischargeTemplateDepartmentList()){
						  if(deptTempDto!=null)
						  {
							  DischargeTemplateDepartmentMapperDto deptDto=new DischargeTemplateDepartmentMapperDto();
							  deptDto.setDischargeSummaryTempId(dischargeSummaryTemplateMaster.getDischargeSummaryTempId());
							  deptDto.setDepartmentId(deptTempDto.getId());
							  deptDto.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
							  deptDto.setOrganizationId(dischargeSummaryTemplateDto.getOrganizationId());
							  deptDto.setUnitId(dischargeSummaryTemplateDto.getUnitId());
							  deptDto.setStatus(dischargeSummaryTemplateDto.getStatus());
							  deptDto.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
							  deptDto.setCreatedDate(dischargeSummaryTemplateDto.getCreatedDate());
							  deptDto.setUpdatedBy(dischargeSummaryTemplateDto.getUpdatedBy());
							  deptDto.setUpdatedDate(dischargeSummaryTemplateDto.getUpdatedDate());
							  iDischargeTemplateDepartmentMapperDao.saveDischargeTemplateDepartmentMapper(deptDto);						    
						  }
						}
					}
				}
			}
			
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
			

		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDischargeSummaryTemplate(DischargeSummaryTemplateDto dischargeSummaryTemplateDto) throws ApplicationException{
		try{
			DischargeSummaryTemplateMaster dischargeSummaryTemplateMaster= null;
			List<DischargeSummaryTemplateDto> alreadySavedFieldList=new ArrayList<>();
			List<DischargeSummaryTemplateDto> alreadySavedDeptList=new ArrayList<>();
			Response response = iDischargeSummaryTemplateDao.getDischargeSummaryTemplateByNameNotId(dischargeSummaryTemplateDto);
			if(response.getStatus() == SUCCESS && response.getObject() != null ){
				return new Response<>(ERROR, null, ALREADY_EXIST, null, null);
			}
			else{
				 Response orgainzationRes = iDischargeSummaryTemplateDao.updateDischargeSummaryTemplate(dischargeSummaryTemplateDto);
					if(orgainzationRes.getStatus().equals("success") && orgainzationRes.getObject() != null){
						 dischargeSummaryTemplateMaster = (DischargeSummaryTemplateMaster) orgainzationRes.getObject();
						 
						 Response resList=iDischargeSummaryTemplateDao.getDischargeSummaryTemplateById(dischargeSummaryTemplateMaster.getDischargeSummaryTempId(), dischargeSummaryTemplateDto.getOrganizationId(), dischargeSummaryTemplateDto.getUnitId());
						 if(resList.getStatus().equals("success") && resList.getListObject() != null){
							 alreadySavedFieldList=resList.getListObject(); 
						 }
						 if(resList.getStatus().equals("success") && resList.getResObject() != null){
							 alreadySavedDeptList=(List<DischargeSummaryTemplateDto>) resList.getResObject(); 
						 }
							List<DischargeSummaryTemplateDto> newList=new ArrayList<>();
							List<DischargeSummaryTemplateDto> newListDept=new ArrayList<>();
							//for update dischargesummaryTemplateMapperfield
						/*	if(alreadySavedFieldList.size()>dischargeSummaryTemplateDto.getSelectDischargeSummaryTemplateList().size())
							{*/
								 for(DischargeSummaryTemplateDto dto1:alreadySavedFieldList) {
									 Boolean flag=true;
									 for(DischargeSummaryTemplateMapperDto dto2 : dischargeSummaryTemplateDto.getSelectDischargeSummaryTemplateList()){
										 if(dto1.getDischargeFieldId().equals(dto2.getDischargeFieldId())) {
											flag=false;
									      } 
									 }
									 
									 if(flag) {
										 newList.add(dto1);
									 }
								 }
								 
								 if(alreadySavedFieldList.size()>0) {
									 for(DischargeSummaryTemplateDto dischargeSummaryTemplateMapperDto : alreadySavedFieldList){
										  if(dischargeSummaryTemplateMapperDto!=null)
										  {
											  DischargeSummaryTemplateMapperDto dto=new DischargeSummaryTemplateMapperDto();
											  dto.setDischargeSummaryTempId(dischargeSummaryTemplateMaster.getDischargeSummaryTempId());
											  dto.setDischargeFieldId(dischargeSummaryTemplateMapperDto.getDischargeFieldId());
										       dto.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
										       dto.setOrganizationId(dischargeSummaryTemplateDto.getOrganizationId());
												dto.setUnitId(dischargeSummaryTemplateDto.getUnitId());
												dto.setStatus('I');
												dto.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
												dto.setCreatedDate(dischargeSummaryTemplateDto.getCreatedDate());
												dto.setUpdatedBy(dischargeSummaryTemplateDto.getUpdatedBy());
												dto.setUpdatedDate(dischargeSummaryTemplateDto.getUpdatedDate());
										    iDischargeSummaryTemplateMapperDao.updateDischargeSummaryFieldDetails(dto);						    
										  }
										}
									 
								 }
							/*}*/
							if(alreadySavedDeptList.size()>dischargeSummaryTemplateDto.getSelectDischargeTemplateDepartmentList().size())
							{
								 for(DischargeSummaryTemplateDto dtoDept:alreadySavedDeptList) {
									 Boolean flag=true;
									 for(DischargeSummaryTemplateMapperDto dtoDept2 : dischargeSummaryTemplateDto.getSelectDischargeSummaryTemplateList()){
										 if(dtoDept.getDischargeFieldId().equals(dtoDept2.getDischargeFieldId())) {
											flag=false;
									      } 
									 }
									 
									 if(flag) {
										 newListDept.add(dtoDept);
									 }
								 }
								 
								 if(newListDept.size()>0) {
									 for(DischargeSummaryTemplateDto dischargeSummaryTemplateMapperDto : newListDept){
										  if(dischargeSummaryTemplateMapperDto!=null)
										  {
										    DischargeTemplateDepartmentMapperDto deptDto=new DischargeTemplateDepartmentMapperDto();
											  deptDto.setDischargeSummaryTempId(dischargeSummaryTemplateMaster.getDischargeSummaryTempId());
											  deptDto.setDepartmentId(dischargeSummaryTemplateMapperDto.getId());
											  deptDto.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
											  deptDto.setOrganizationId(dischargeSummaryTemplateDto.getOrganizationId());
											  deptDto.setUnitId(dischargeSummaryTemplateDto.getUnitId());
											  deptDto.setStatus('I');
											  deptDto.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
											  deptDto.setCreatedDate(dischargeSummaryTemplateDto.getCreatedDate());
											  deptDto.setUpdatedBy(dischargeSummaryTemplateDto.getUpdatedBy());
											  deptDto.setUpdatedDate(dischargeSummaryTemplateDto.getUpdatedDate());
											  iDischargeTemplateDepartmentMapperDao.updateDischargeTemplateDepartmentMapper(deptDto);		
										  }
										}
									 
								 }
							}
							//for save if extra field added in previous list
							List<DischargeSummaryTemplateMapperDto> newList1=new ArrayList<>();
							List<DischargeTemplateDepartmentMapperDto> newListDept1=new ArrayList<>();
							/*if(alreadySavedFieldList.size()<dischargeSummaryTemplateDto.getSelectDischargeSummaryTemplateList().size())
							{*/
								for(DischargeSummaryTemplateMapperDto dto1 : dischargeSummaryTemplateDto.getSelectDischargeSummaryTemplateList()){
									 Boolean flag=true;
									 for(DischargeSummaryTemplateDto dto2:alreadySavedFieldList) {
										 if(dto1.getDischargeFieldId().equals(dto2.getDischargeFieldId())) {
											flag=false;
									      } 
									 }
									 
									 if(flag) {
										 newList1.add(dto1);
									 }
								 }
								 
								 if(dischargeSummaryTemplateDto.getSelectDischargeSummaryTemplateList().size()>0) {
									 for(DischargeSummaryTemplateMapperDto dischargeSummaryTemplateMapperDto : dischargeSummaryTemplateDto.getSelectDischargeSummaryTemplateList()){
										  if(dischargeSummaryTemplateMapperDto!=null)
										  {
											  DischargeSummaryTemplateMapperDto dto=new DischargeSummaryTemplateMapperDto();
											  dto.setDischargeSummaryTempId(dischargeSummaryTemplateMaster.getDischargeSummaryTempId());
											  dto.setDischargeFieldId(dischargeSummaryTemplateMapperDto.getDischargeFieldId());
										       dto.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
										       dto.setOrganizationId(dischargeSummaryTemplateDto.getOrganizationId());
												dto.setUnitId(dischargeSummaryTemplateDto.getUnitId());
												dto.setStatus(dischargeSummaryTemplateDto.getStatus());
												dto.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
												dto.setCreatedDate(dischargeSummaryTemplateDto.getCreatedDate());
												dto.setUpdatedBy(dischargeSummaryTemplateDto.getUpdatedBy());
												dto.setUpdatedDate(dischargeSummaryTemplateDto.getUpdatedDate());
										    iDischargeSummaryTemplateMapperDao.saveDischargeSummaryFieldDetails(dto);						    
										  }
										}
								 }
							/*}*/
							if(alreadySavedDeptList.size()<dischargeSummaryTemplateDto.getSelectDischargeTemplateDepartmentList().size())
							{
								for(DischargeTemplateDepartmentMapperDto dtoDept1 : dischargeSummaryTemplateDto.getSelectDischargeTemplateDepartmentList()){
									 Boolean flag=true;
									 for(DischargeSummaryTemplateDto dtoDept2:alreadySavedDeptList) {
										 if(dtoDept1.getId().equals(dtoDept2.getDepartmentId())) {
											flag=false;
									      } 
									 }
									 
									 if(flag) {
										 newListDept1.add(dtoDept1);
									 }
								 }
								 
								 if(newListDept1.size()>0) {
									 for(DischargeTemplateDepartmentMapperDto dischargeSummaryTemplateMapperDto : newListDept1){
										  if(dischargeSummaryTemplateMapperDto!=null)
										  {
											  DischargeTemplateDepartmentMapperDto deptDto=new DischargeTemplateDepartmentMapperDto();
											  deptDto.setDischargeSummaryTempId(dischargeSummaryTemplateMaster.getDischargeSummaryTempId());
											  deptDto.setDepartmentId(dischargeSummaryTemplateMapperDto.getId());
											  deptDto.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
											  deptDto.setOrganizationId(dischargeSummaryTemplateDto.getOrganizationId());
											  deptDto.setUnitId(dischargeSummaryTemplateDto.getUnitId());
											  deptDto.setStatus(dischargeSummaryTemplateDto.getStatus());
											  deptDto.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
											  deptDto.setCreatedDate(dischargeSummaryTemplateDto.getCreatedDate());
											  deptDto.setUpdatedBy(dischargeSummaryTemplateDto.getUpdatedBy());
											  deptDto.setUpdatedDate(dischargeSummaryTemplateDto.getUpdatedDate());
											  iDischargeTemplateDepartmentMapperDao.saveDischargeTemplateDepartmentMapper(deptDto);									    
										  }
										}
								 }
							}
							
					}
				return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			}
		}catch(Exception e){
			
		}
		return new Response<>(ERROR, null, null, null, null);
	}

	@Override
	public Response updateDischargeSummaryTemplateStatus(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException {
		try {
			return iDischargeSummaryTemplateDao.updateDischargeSummaryTemplateStatus(dischargeSummaryTemplateDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getDischargeSummaryTemplateById(Integer dischargeSummaryTempId, Integer organizationId, Integer unitId) throws ApplicationException{
		try {
			return iDischargeSummaryTemplateDao.getDischargeSummaryTemplateById(dischargeSummaryTempId, organizationId, unitId);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeSummaryTemplateMasterList(DischargeSummaryTemplateDto dischargeSummaryTemplateDto) throws ApplicationException{
		try {
			return iDischargeSummaryTemplateDao.getDischargeSummaryTemplateMasterList(dischargeSummaryTemplateDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeSummaryTemplateCount(Integer organizationId, Integer unitId, String searchKeyword) throws ApplicationException{
		try {
			return iDischargeSummaryTemplateDao.getDischargeSummaryTemplateCount(organizationId, unitId, searchKeyword);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveDischargeSummaryTemplate(Integer organizationId, Integer unitId) throws ApplicationException{
		try {
			return iDischargeSummaryTemplateDao.getActiveDischargeSummaryTemplate(organizationId, unitId);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@SuppressWarnings("unused")
	@Override
	public Response saveListDischargeSummaryTemplate(List<DischargeSummaryTemplateDto> dischargeSummaryTemplateDto) {
		try {
			DischargeSummaryTemplateMaster dischargeSummaryTemplateMaster= null;
			Response res = iDischargeSummaryTemplateDao.getDischargeSummaryTemplateByName(dischargeSummaryTemplateDto);
			if(res.getListObject() != null && res.getListObject().size() > 0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
				
			}else{
				
				return iDischargeSummaryTemplateDao.saveListDischargeSummaryTemplate(dischargeSummaryTemplateDto);
				
			}
			
		
		
		
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeTempDeptListById(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)
			throws ApplicationException {
		try {
			return iDischargeTemplateDepartmentMapperDao.getDischargeTempDeptListById(dischargeSummaryTemplateDto);
			
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
