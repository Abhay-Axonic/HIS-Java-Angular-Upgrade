package com.param.adt.discharge.service;

import in.param.exception.ApplicationException;
import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.adt.discharge.dao.IDischargeSumTabColumnsPrintMapperDao;
import com.param.adt.discharge.dao.IDischargeSummaryDao;
import com.param.adt.discharge.dao.IDischargeSummaryFieldDao;
import com.param.adt.discharge.dto.DischargeSummaryDto;
import com.param.adt.discharge.dto.DischargeSummaryFieldDto;
import com.param.adt.discharge.model.DischargeSummary;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.AdmissionDto;

import javax.transaction.Transactional;

@Service
@SuppressWarnings({"rawtypes"})
public class DischargeSummaryServiceImpl implements IDischargeSummaryService, ICommonConstants{

		@Autowired
		private IDischargeSummaryDao iDischargeSummaryDao;
		
		@Autowired
		private IDischargeSumTabColumnsPrintMapperDao iDischargeSumTabColumnsPrintMapperDao;
		
		@Autowired
		private IDischargeSummaryFieldDao DischargeSummaryFieldDao;
		
		@Override
		public Response getDischargeSummaryTemplateByPatientId(Integer patientId, Integer organizationId,
				Integer unitId) {
			return iDischargeSummaryDao.getDischargeSummaryTemplateByPatientId(patientId,organizationId,unitId);
		}

		@Override
		public Response getPatientDischargeSummaryList(DischargeSummaryDto dischargeSummaryDto) {
			return iDischargeSummaryDao.getPatientDischargeSummaryList(dischargeSummaryDto);
		}

		@Override
		public Response savePatientDischargeSummary(DischargeSummaryDto dischargeSummaryDto) {
			try {
			DischargeSummary dischargeSummary=null;
			Response inActiveTempRes=iDischargeSummaryDao.inactiveDischargeSummaryTemplates(dischargeSummaryDto);
			Response res=iDischargeSummaryDao.findByPatientIdTemplateId(dischargeSummaryDto);
			if(res.getStatus() == SUCCESS && res.getObject() != null)
			 {
			 	//update discharge summary detail table
				dischargeSummary = (DischargeSummary) res.getObject();
				if(dischargeSummaryDto.getDischargeSummaryTemplateList() != null && dischargeSummaryDto.getDischargeSummaryTemplateList().size() >0)
				{
					for(DischargeSummaryDto dischargeSummaryTemplateMapperDto : dischargeSummaryDto.getDischargeSummaryTemplateList()){
						  DischargeSummaryDto dto=new DischargeSummaryDto();
						  dto.setDischargeSummaryId(dischargeSummary.getDischargeSummaryId());
						  dto.setDischargeFieldId(dischargeSummaryTemplateMapperDto.getDischargeFieldId());
						  dto.setDischargeFieldDescription(dischargeSummaryTemplateMapperDto.getDischargeFieldDescription());
					      dto.setOrganizationId(dischargeSummaryDto.getOrganizationId());
					      dto.setUnitId(dischargeSummaryDto.getUnitId());
					      dto.setStatus(dischargeSummaryDto.getStatus());
					      dto.setCreatedBy(dischargeSummaryDto.getCreatedBy());
					      dto.setUpdatedBy(dischargeSummaryDto.getUpdatedBy());
					      dto.setCreatedDate(dischargeSummaryDto.getCreatedDate());
					      dto.setUpdatedDate(dischargeSummaryDto.getUpdatedDate());
						iDischargeSummaryDao.saveDischargeSummaryDetails(dto);						    
					}
				}
				
				 return new Response<>(SUCCESS, SUCCESS_CODE, "Discharge Summary Saved Successfully", null, dischargeSummary);
			 }
			 else {
			   Response response = iDischargeSummaryDao.savePatientDischargeSummary(dischargeSummaryDto);
			if(response.getStatus() == SUCCESS && response.getObject() != null){
				dischargeSummary = (DischargeSummary) response.getObject();
				if(dischargeSummaryDto.getDischargeSummaryTemplateList() != null && dischargeSummaryDto.getDischargeSummaryTemplateList().size() >0)
				{
					for(DischargeSummaryDto dischargeSummaryTemplateMapperDto : dischargeSummaryDto.getDischargeSummaryTemplateList()){
						  DischargeSummaryDto dto=new DischargeSummaryDto();
						  dto.setDischargeSummaryId(dischargeSummary.getDischargeSummaryId());
						  dto.setDischargeFieldId(dischargeSummaryTemplateMapperDto.getDischargeFieldId());
						  dto.setDischargeFieldDescription(dischargeSummaryTemplateMapperDto.getDischargeFieldDescription());
					      dto.setOrganizationId(dischargeSummaryDto.getOrganizationId());
					      dto.setUnitId(dischargeSummaryDto.getUnitId());
					      dto.setStatus(dischargeSummaryDto.getStatus());
					      dto.setCreatedBy(dischargeSummaryDto.getCreatedBy());
					      dto.setCreatedDate(dischargeSummaryDto.getCreatedDate());
					      dto.setUpdatedBy(dischargeSummaryDto.getUpdatedBy());
					      dto.setUpdatedDate(dischargeSummaryDto.getUpdatedDate());
					    iDischargeSummaryDao.saveDischargeSummaryDetails(dto);						    
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

		@Override
		public Response getFieldTemplateSummaryByTemplateId(Integer templateId, Integer organizationId,
				Integer unitId) {
			return iDischargeSummaryDao.getFieldTemplateSummaryByTemplateId(templateId,organizationId,unitId);
		}

		@Override
		public Response getPatientDischargeSummaryFieldList(DischargeSummaryDto dischargeSummaryDto) {
			return iDischargeSummaryDao.getPatientDischargeSummaryFieldList(dischargeSummaryDto);
		}

		@Override
		public Response getAdmissionReason(DischargeSummaryDto dischargeSummaryDto) {
			return iDischargeSummaryDao.getAdmissionReason(dischargeSummaryDto);
		}

		@Override
		public Response updateDischargeSummaryReportStatus(DischargeSummaryDto dischargeSummaryDto) {
			try{
				return iDischargeSummaryDao.updateDischargeSummaryReportStatus(dischargeSummaryDto);
			} catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public Response getSavedDischargeSummaryByAdmissionId(DischargeSummaryDto dischargeSummaryDto) {
			try{
				return iDischargeSummaryDao.getSavedDischargeSummaryByAdmissionId(dischargeSummaryDto);
			} catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

		@Override
		public ByteArrayOutputStream getDischargeSummaryPdf(DischargeSummaryDto dischargeSummaryDto) {
			try{
				//return iDischargeSummaryDao.createDischargePDF(dischargeSummaryDto);
				return iDischargeSummaryDao.createDischargePDF1(dischargeSummaryDto);
			} catch (Exception e) {
				
			}
			return null;
		}

		@Override
		public Response searchPatientDischargeSummary(DischargeSummaryDto dischargeSummaryDto) {
			try{
				return iDischargeSummaryDao.searchPatientDischargeSummary(dischargeSummaryDto);
			} catch (Exception e) {
				
			}
			return null;
		}

		@Override
		public Integer getDischargeSummaryIdByAdmissionId(Integer admissionId) {
			try{
				return iDischargeSummaryDao.getDischargeSummaryIdByAdmissionId(admissionId);
			} catch (Exception e) {
				
			}
			return null;
		}

		@Override
		public Response getFollowUpAppointment(DischargeSummaryDto dischargeSummaryDto) {
			try{
				return iDischargeSummaryDao.getFollowUpAppointment(dischargeSummaryDto);
			} catch (Exception e) {
				
			}
			return null;
		}

		@Override
		public Response getPatientDischargeSummaryTemplateData(DischargeSummaryDto dischargeSummaryDto) {
			try{
				return iDischargeSummaryDao.getPatientDischargeSummaryTemplateData(dischargeSummaryDto);
			} catch (Exception e) {
				
			}
			return null;
		}

		@Override
		public Response saveDischargeSumTabColumnsPrintMapper(DischargeSummaryDto dischargeSummaryDto) {
			try{
				Response res=iDischargeSumTabColumnsPrintMapperDao.checkDischSumTabColPrintMapperAvailable(dischargeSummaryDto);
				if(res.getListObject() != null && res.getListObject().size() > 0) {
				  Response	res1= iDischargeSumTabColumnsPrintMapperDao.updateDischargeSumTabColumnsPrintMapper(dischargeSummaryDto);
				  if(res1.getObject() != null) {
						DischargeSummaryFieldDto dto=(DischargeSummaryFieldDto)res.getObject();
						return DischargeSummaryFieldDao.updateDischSumColumnsRequired(dischargeSummaryDto);
					}
				}else {
					return iDischargeSumTabColumnsPrintMapperDao.saveDischargeSumTabColumnsPrintMapper(dischargeSummaryDto);
				}
				
			} catch (Exception e) {
				
			}
			return null;
		}

		@Override
		public Response getDischargeSummaryPrintType(DischargeSummaryDto dischargeSummaryDto) {
			try{
				return iDischargeSummaryDao.getDischargeSummaryPrintType(dischargeSummaryDto);
			} catch (Exception e) {
				
			}
			return null;
		}

		@Override
		public Response updateExtendPdd(AdmissionDto dto) {
			try{
				return iDischargeSummaryDao.updateExtendPdd(dto);
			} catch (Exception e) {
				
			}
			return null;
		}

		@Override
		public Response updateAdmittedPatientList(AdmissionDto dto) {
			try{
				return iDischargeSummaryDao.updateAdmittedPatientList(dto);
			} catch (Exception e) {
				
			}
		return null;
		}

	@Override
	@Transactional
	public Response sendDischargeSummaryReportToMyLife(DischargeSummaryDto dto) {
		try{
			return iDischargeSummaryDao.sendDischargeSummaryReportToMyLife(dto);
		} catch (Exception e) {
		throw new ApplicationException(e.getMessage(), e);
	}
		
	}
}
