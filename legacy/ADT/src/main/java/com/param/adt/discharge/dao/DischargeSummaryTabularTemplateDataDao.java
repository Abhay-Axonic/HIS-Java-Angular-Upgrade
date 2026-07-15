package com.param.adt.discharge.dao;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.discharge.dto.DischargeSummaryDto;
import com.param.adt.discharge.dto.DischargeSummaryTabularTemplateDataDto;
import com.param.adt.discharge.model.DischargeSummary;
import com.param.adt.discharge.model.DischargeSummaryTabularTemplateData;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.PatientTabularTemplateDataMapper;
import com.param.global.unit.dto.PatientTabularTemplateDataMapperDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DischargeSummaryTabularTemplateDataDao extends GenericDao<DischargeSummaryTabularTemplateData, Integer> implements IDischargeSummaryTabularTemplateDataDao,ICommonConstants{
	public DischargeSummaryTabularTemplateDataDao() {
		super(DischargeSummaryTabularTemplateData.class);
	}
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Response saveDischargeSummTabularTemplateData(
			DischargeSummaryTabularTemplateDataDto dischargeSummaryTabularTemplateDataDto) throws ApplicationException {
		try {
		 /*sessionFactory.getCurrentSession().createSQLQuery(
						" update adt.m_discharge_summary_tabular_template_data set status = 'I' where discharge_summary_id = :dischargeSummaryId "
						+ "  and patient_id =:patientId and organization_id =:organizationId "
						+ "  and admission_id =:admissionId "
						+ "  and unit_id =:unitId and discharge_template_id =:dischargeTemplateId ")
						.setParameter("dischargeSummaryId", dischargeSummaryTabularTemplateDataDto.getDischargeSummaryId())
						.setParameter("dischargeTemplateId", dischargeSummaryTabularTemplateDataDto.getDischargeTemplateId())
						.setParameter("patientId", dischargeSummaryTabularTemplateDataDto.getPatientId())
						.setParameter("admissionId", dischargeSummaryTabularTemplateDataDto.getAdmissionId())
						.setParameter("organizationId", dischargeSummaryTabularTemplateDataDto.getOrganizationId())
						.setParameter("unitId", dischargeSummaryTabularTemplateDataDto.getUnitId())
						.executeUpdate();*/
			 
			 if(dischargeSummaryTabularTemplateDataDto.getDischargeSummaryTemplateListdata() != null &&  dischargeSummaryTabularTemplateDataDto.getDischargeSummaryTemplateListdata().size() > 0) {
			for(DischargeSummaryTabularTemplateDataDto dto : dischargeSummaryTabularTemplateDataDto.getDischargeSummaryTemplateListdata())
			{
				DischargeSummaryTabularTemplateData dischargeSummaryTabularTemplateData=new DischargeSummaryTabularTemplateData();
				dischargeSummaryTabularTemplateData.setDischargeSummaryId(dischargeSummaryTabularTemplateDataDto.getDischargeSummaryId());
				dischargeSummaryTabularTemplateData.setDischargeTemplateId(dischargeSummaryTabularTemplateDataDto.getDischargeTemplateId());
				dischargeSummaryTabularTemplateData.setDepartmentId(dischargeSummaryTabularTemplateDataDto.getDepartmentId());
				dischargeSummaryTabularTemplateData.setDischargeFieldId(dischargeSummaryTabularTemplateDataDto.getDischargeFieldId());
				dischargeSummaryTabularTemplateData.setPatientId(dischargeSummaryTabularTemplateDataDto.getPatientId());
				dischargeSummaryTabularTemplateData.setDoctorId(dischargeSummaryTabularTemplateDataDto.getDoctorId());
				dischargeSummaryTabularTemplateData.setAdmissionId(dischargeSummaryTabularTemplateDataDto.getAdmissionId());
				dischargeSummaryTabularTemplateData.setTableTypeId(dto.getTableTypeId());
				dischargeSummaryTabularTemplateData.setIsDischargeSummaryRelease(dischargeSummaryTabularTemplateDataDto.getIsDischargeSummaryRelease());
				dischargeSummaryTabularTemplateData.setColumnName(dto.getColumnName());
				dischargeSummaryTabularTemplateData.setCoulmnId(dto.getCoulmnId());
				dischargeSummaryTabularTemplateData.setRowName(dto.getRowName());
				dischargeSummaryTabularTemplateData.setRowId(dto.getRowId());
				dischargeSummaryTabularTemplateData.setInputValue(dto.getInputValue());
				dischargeSummaryTabularTemplateData.setTableName(dto.getTableName());
				dischargeSummaryTabularTemplateData.setNumberOfCoulmn(dto.getNumberOfCoulmn());
				dischargeSummaryTabularTemplateData.setNumberOfRow(dto.getNumberOfRow());
				dischargeSummaryTabularTemplateData.setOrganizationId(dto.getOrganizationId());
				dischargeSummaryTabularTemplateData.setUnitId(dto.getUnitId());
				dischargeSummaryTabularTemplateData.setStatus('A');
				dischargeSummaryTabularTemplateData.setIsDischargeSummaryRelease('N');
				dischargeSummaryTabularTemplateData.setCreatedBy(dto.getCreatedBy());
				dischargeSummaryTabularTemplateData.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-MM-yyyy HH:mm:ss"));
				dischargeSummaryTabularTemplateData.setUpdatedBy(dto.getUpdatedBy());
				dischargeSummaryTabularTemplateData.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-MM-yyyy HH:mm:ss"));
				dischargeSummaryTabularTemplateData.setDischargeSummaryDetailId(dischargeSummaryTabularTemplateDataDto.getDischargeSummaryDetailId());
			 
			save(dischargeSummaryTabularTemplateData);
			}
			 }
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response inactiveDischargeSummaryTabularData(DischargeSummaryDto dischargeSummaryDto)
			throws ApplicationException {
		try {
				int count = sessionFactory.getCurrentSession().createSQLQuery(
						" update adt.m_discharge_summary_tabular_template_data set status = 'I' where discharge_summary_id = :dischargeSummaryId "
						+ "  and patient_id =:patientId and organization_id =:organizationId "
						+ "  and admission_id =:admissionId "
						+ "  and unit_id =:unitId and discharge_template_id =:dischargeTemplateId ")
						.setParameter("dischargeSummaryId", dischargeSummaryDto.getDischargeSummaryId())
						.setParameter("dischargeTemplateId", dischargeSummaryDto.getDischargeTemplateId())
						.setParameter("patientId", dischargeSummaryDto.getPatientId())
						.setParameter("admissionId", dischargeSummaryDto.getAdmissionId())
						.setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
						.setParameter("unitId", dischargeSummaryDto.getUnitId())
						.executeUpdate();
		
			return new Response(SUCCESS, SUCCESS_CODE, null, null, null);
		} catch (HibernateException he) {
			} catch (Exception e) {
				
			}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		
	}
	
}
