package com.param.adt.discharge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.discharge.dao.IDischargeSummaryDao;
import com.param.adt.discharge.dao.IDischargeSummaryTabularTemplateDataDao;
import com.param.adt.discharge.dto.DischargeSummaryDto;
import com.param.adt.discharge.dto.DischargeSummaryTabularTemplateDataDto;
import com.param.adt.discharge.model.DischargeSummary;
import com.param.adt.discharge.model.DischargeSummaryMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})
public class DischargeSummaryTabularTemplateDataService implements IDischargeSummaryTabularTemplateDataService, ICommonConstants{
	@Autowired
	private IDischargeSummaryTabularTemplateDataDao iDischargeSummaryTabularTemplateDataDao;
	
	@Autowired
	private IDischargeSummaryDao iDischargeSummaryDao;

	@Override
	public Response saveDischargeSummTabularTemplateData( DischargeSummaryTabularTemplateDataDto dischargeSummaryTabularTemplateDataDto) throws ApplicationException {
		try {
		DischargeSummary dischargeSummary=null;
		DischargeSummaryMapper dischargeSummaryMapper=null;
		DischargeSummaryDto dischargeSummaryDto = new DischargeSummaryDto();
		dischargeSummaryDto.setAdmissionId(dischargeSummaryTabularTemplateDataDto.getAdmissionId());
		dischargeSummaryDto.setDischargeTemplateId(dischargeSummaryTabularTemplateDataDto.getDischargeTemplateId());
		dischargeSummaryDto.setOrganizationId(dischargeSummaryTabularTemplateDataDto.getOrganizationId());
		dischargeSummaryDto.setUnitId(dischargeSummaryTabularTemplateDataDto.getUnitId());
		dischargeSummaryDto.setPatientId(dischargeSummaryTabularTemplateDataDto.getPatientId());
		dischargeSummaryDto.setDepartmentId(dischargeSummaryTabularTemplateDataDto.getDepartmentId());
		dischargeSummaryDto.setDoctorId(dischargeSummaryTabularTemplateDataDto.getDoctorId());
		dischargeSummaryDto.setStatus('A');
		dischargeSummaryDto.setCreatedBy(dischargeSummaryTabularTemplateDataDto.getCreatedBy());
		dischargeSummaryDto.setCreatedDate(dischargeSummaryTabularTemplateDataDto.getCreatedDate());
		dischargeSummaryDto.setUpdatedBy(dischargeSummaryTabularTemplateDataDto.getUpdatedBy());
		dischargeSummaryDto.setUpdatedDate(dischargeSummaryTabularTemplateDataDto.getUpdatedDate());
		
		
		Response inActiveTempRes=iDischargeSummaryDao.inactiveDischargeSummaryTemplates(dischargeSummaryDto);
		Response res=iDischargeSummaryDao.findByPatientIdTemplateId(dischargeSummaryDto);
		if(res.getStatus() == SUCCESS && res.getObject() != null)
		 {
			
		 	//update discharge summary detail table
			dischargeSummary = (DischargeSummary) res.getObject();
			if(dischargeSummaryTabularTemplateDataDto.getDischargeSummaryTemplateList() != null && dischargeSummaryTabularTemplateDataDto.getDischargeSummaryTemplateList().size() > 0)
			{
				
				dischargeSummaryDto.setDischargeSummaryId(dischargeSummary.getDischargeSummaryId());
				Response ss=iDischargeSummaryTabularTemplateDataDao.inactiveDischargeSummaryTabularData(dischargeSummaryDto);
				for(DischargeSummaryTabularTemplateDataDto dischargeSummaryTemplateMapperDto : dischargeSummaryTabularTemplateDataDto.getDischargeSummaryTemplateList()){
					
					DischargeSummaryDto dto1=new DischargeSummaryDto();
					dto1.setDischargeSummaryId(dischargeSummary.getDischargeSummaryId());
					dto1.setDischargeFieldId(dischargeSummaryTemplateMapperDto.getDischargeFieldId());
					dto1.setDischargeFieldDescription(dischargeSummaryTemplateMapperDto.getDischargeFieldDescription());
					dto1.setOrganizationId(dischargeSummaryDto.getOrganizationId());
					dto1.setUnitId(dischargeSummaryDto.getUnitId());
					dto1.setStatus(dischargeSummaryDto.getStatus());
					dto1.setCreatedBy(dischargeSummaryDto.getCreatedBy());
					dto1.setUpdatedBy(dischargeSummaryDto.getUpdatedBy());
					dto1.setCreatedDate(dischargeSummaryDto.getCreatedDate());
					dto1.setUpdatedDate(dischargeSummaryDto.getUpdatedDate());
					dto1.setDischargeSummaryDataViewOptionId(dischargeSummaryTemplateMapperDto.getDischargeSummaryDataViewOptionId());
					Response resDischargeSumm = iDischargeSummaryDao.saveDischargeSummaryDetails(dto1);			
					if(resDischargeSumm.getStatus() == SUCCESS && resDischargeSumm.getObject() != null)
					 {
					 	//update discharge summary detail table
						dischargeSummary = (DischargeSummary) res.getObject();
						DischargeSummaryDto test=new DischargeSummaryDto();
						test = (DischargeSummaryDto) resDischargeSumm.getObject();
						
				      DischargeSummaryTabularTemplateDataDto dto=new DischargeSummaryTabularTemplateDataDto();
					  dto.setDischargeSummaryId(dischargeSummary.getDischargeSummaryId());
					  dto.setDischargeTemplateId(dischargeSummaryTabularTemplateDataDto.getDischargeTemplateId());
					  dto.setPatientId(dischargeSummaryTabularTemplateDataDto.getPatientId());
					  dto.setDoctorId(dischargeSummaryTabularTemplateDataDto.getDoctorId());
					  dto.setAdmissionId(dischargeSummaryTabularTemplateDataDto.getAdmissionId());
					  dto.setTableTypeId(dischargeSummaryTabularTemplateDataDto.getTableTypeId());
					  dto.setDepartmentId(dischargeSummaryTemplateMapperDto.getDepartmentId());
					  dto.setDischargeFieldId(dischargeSummaryTemplateMapperDto.getDischargeFieldId());
					  dto.setIsDischargeSummaryRelease(dischargeSummaryTabularTemplateDataDto.getIsDischargeSummaryRelease());
					  dto.setOrganizationId(dischargeSummaryTabularTemplateDataDto.getOrganizationId());
					  dto.setUnitId(dischargeSummaryTabularTemplateDataDto.getUnitId());
				      dto.setDischargeSummaryTemplateListdata(dischargeSummaryTemplateMapperDto.getDischargeSummaryTemplateListdata());
				      dto.setDischargeSummaryDetailId(test.getDischargeSummaryDetailId());
				      iDischargeSummaryTabularTemplateDataDao.saveDischargeSummTabularTemplateData(dto);	
					 }
				}
			}
			
			 return new Response<>(SUCCESS, SUCCESS_CODE, "Discharge Summary Saved Successfully", null, dischargeSummary);
		 }
		 else {
		   Response response = iDischargeSummaryDao.savePatientDischargeSummary(dischargeSummaryDto);
		if(response.getStatus() == SUCCESS && response.getObject() != null){
			dischargeSummary = (DischargeSummary) response.getObject();
			if(dischargeSummaryTabularTemplateDataDto.getDischargeSummaryTemplateList() != null && dischargeSummaryTabularTemplateDataDto.getDischargeSummaryTemplateList().size() >0)
			{
				dischargeSummaryDto.setDischargeSummaryId(dischargeSummary.getDischargeSummaryId());
				Response ss=iDischargeSummaryTabularTemplateDataDao.inactiveDischargeSummaryTabularData(dischargeSummaryDto);
				
				for(DischargeSummaryTabularTemplateDataDto dischargeSummaryTemplateMapperDto : dischargeSummaryTabularTemplateDataDto.getDischargeSummaryTemplateList()){
					
					 DischargeSummaryDto dto1=new DischargeSummaryDto();
					 dto1.setDischargeSummaryId(dischargeSummary.getDischargeSummaryId());
					 dto1.setDischargeFieldId(dischargeSummaryTemplateMapperDto.getDischargeFieldId());
					 dto1.setDischargeFieldDescription(dischargeSummaryTemplateMapperDto.getDischargeFieldDescription());
					 dto1.setOrganizationId(dischargeSummaryDto.getOrganizationId());
					 dto1.setUnitId(dischargeSummaryDto.getUnitId());
					 dto1.setStatus(dischargeSummaryDto.getStatus());
					 dto1.setCreatedBy(dischargeSummaryDto.getCreatedBy());
					 dto1.setCreatedDate(dischargeSummaryDto.getCreatedDate());
					 dto1.setUpdatedBy(dischargeSummaryDto.getUpdatedBy());
					 dto1.setUpdatedDate(dischargeSummaryDto.getUpdatedDate());
					 dto1.setDischargeSummaryDataViewOptionId(dischargeSummaryTemplateMapperDto.getDischargeSummaryDataViewOptionId());
					 Response resDischargeSumm = iDischargeSummaryDao.saveDischargeSummaryDetails(dto1);			
						if(resDischargeSumm.getStatus() == SUCCESS && resDischargeSumm.getObject() != null)
						 {
						 	//update discharge summary detail table
							DischargeSummaryDto test=new DischargeSummaryDto();
							test = (DischargeSummaryDto) resDischargeSumm.getObject();
							
				    
				      DischargeSummaryTabularTemplateDataDto dto=new DischargeSummaryTabularTemplateDataDto();
					  dto.setDischargeSummaryId(dischargeSummary.getDischargeSummaryId());
					  dto.setDischargeTemplateId(dischargeSummaryTabularTemplateDataDto.getDischargeTemplateId());
					  dto.setPatientId(dischargeSummaryTabularTemplateDataDto.getPatientId());
					  dto.setDoctorId(dischargeSummaryTabularTemplateDataDto.getDoctorId());
					  dto.setAdmissionId(dischargeSummaryTabularTemplateDataDto.getAdmissionId());
					  dto.setTableTypeId(dischargeSummaryTabularTemplateDataDto.getTableTypeId());
					  dto.setDepartmentId(dischargeSummaryTemplateMapperDto.getDepartmentId());
					  dto.setDischargeFieldId(dischargeSummaryTemplateMapperDto.getDischargeFieldId());
					  dto.setIsDischargeSummaryRelease(dischargeSummaryTabularTemplateDataDto.getIsDischargeSummaryRelease());
					  dto.setOrganizationId(dischargeSummaryTabularTemplateDataDto.getOrganizationId());
					  dto.setUnitId(dischargeSummaryTabularTemplateDataDto.getUnitId());
				      dto.setDischargeSummaryTemplateListdata(dischargeSummaryTemplateMapperDto.getDischargeSummaryTemplateListdata());
				      dto.setDischargeSummaryDetailId(test.getDischargeSummaryDetailId());
				      iDischargeSummaryTabularTemplateDataDao.saveDischargeSummTabularTemplateData(dto);	
						 }
				}		    
			}
		}
         return new Response<>(SUCCESS, SUCCESS_CODE, "Discharge Summary Saved Successfully", null, dischargeSummary);
	}
		
	} catch (Exception e) {
		
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		//return iDischargeSummaryDao.savePatientDischargeSummary(dischargeSummaryDto);
	}
}
