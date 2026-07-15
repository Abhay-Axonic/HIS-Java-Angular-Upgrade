package com.param.adt.discharge.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.adt.discharge.dto.DischargeSummaryTemplateDto;
import com.param.adt.discharge.dto.DischargeTemplateDepartmentMapperDto;
import com.param.adt.discharge.model.DischargeSummaryTemplateMapper;
import com.param.adt.discharge.model.DischargeTemplateDepartmentMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.MedicationDTO;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class DischargeTemplateDepartmentMapperDaoImpl extends GenericDao<DischargeTemplateDepartmentMapper, Integer>implements IDischargeTemplateDepartmentMapperDao, ICommonConstants{

	public DischargeTemplateDepartmentMapperDaoImpl() {
		super(DischargeTemplateDepartmentMapper.class);
	}

	@Override
	public Response saveDischargeTemplateDepartmentMapper(DischargeTemplateDepartmentMapperDto deptDto)
			throws ApplicationException {
		try {
			DischargeTemplateDepartmentMapper dischargeTemplateDepartmentMapper=new DischargeTemplateDepartmentMapper();
			dischargeTemplateDepartmentMapper.setDepartmentId(deptDto.getDepartmentId());
			dischargeTemplateDepartmentMapper.setDischargeSummaryTempId(deptDto.getDischargeSummaryTempId());
			dischargeTemplateDepartmentMapper.setOrganizationId(deptDto.getOrganizationId());
			dischargeTemplateDepartmentMapper.setUnitId(deptDto.getUnitId());
			dischargeTemplateDepartmentMapper.setStatus(deptDto.getStatus());
			dischargeTemplateDepartmentMapper.setCreatedBy(deptDto.getCreatedBy());
			dischargeTemplateDepartmentMapper.setCreatedDate(GlobalCommonDateUtils.getDate(deptDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeTemplateDepartmentMapper.setUpdatedBy(deptDto.getUpdatedBy());
			dischargeTemplateDepartmentMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(deptDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(dischargeTemplateDepartmentMapper);
		
		return new Response<>(SUCCESS, null, null, null, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDischargeTempDeptListById(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)
			throws ApplicationException {
		try {
			String query="select  " + 
					"	mppr.discharge_summary_temp_id as \"dischargeSummaryTempId\",  " + 
					"	mppr.department_id as \"departmentId\" ,  " + 
					"	spl.speciality_name as \"specialityName\"  ,"+
					"	mppr.department_id as \"id\" ,  " + 
					"	spl.speciality_name as \"lable\"  " +  
					"from  " + 
					"	adt.t_discharge_template_department_mapper mppr  " + 
					"inner join doctor.m_speciality_master spl on  " + 
					"	spl.speciality_id = mppr.department_id  " + 
					"where mppr.status ='A'  " + 
					"and mppr.discharge_summary_temp_id =:id";
			List<DischargeSummaryTemplateDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("id", dischargeSummaryTemplateDto.getDischargeSummaryTempId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryTemplateDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}  catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);

	}

	@Override
	public Response updateDischargeTemplateDepartmentMapper(DischargeTemplateDepartmentMapperDto deptDto)
			throws ApplicationException {
		try {
			DischargeTemplateDepartmentMapper deptMapper=(DischargeTemplateDepartmentMapper)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DETAILED_DISCHARGE_SUMMARY_TEMPLATE_MASTER_BY_DEPARTMENT_ID")
					.setParameter("dischargeSummaryTempId", deptDto.getDischargeSummaryTempId())
					.setParameter("departmentId", deptDto.getDepartmentId())
					.setResultTransformer(Transformers.aliasToBean(DischargeTemplateDepartmentMapper.class)).uniqueResult();
			
			deptMapper.setStatus(deptDto.getStatus());
			deptMapper.setUpdatedBy(deptDto.getUpdatedBy());
			deptMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(deptDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
			deptMapper.setDepartmentId(deptDto.getDepartmentId());
			deptMapper.setDischargeSummaryTempId(deptDto.getDischargeSummaryTempId());
			deptMapper.setOrganizationId(deptDto.getOrganizationId());
			deptMapper.setUnitId(deptDto.getUnitId());
			deptMapper.setCreatedBy(deptDto.getCreatedBy());
			deptMapper.setCreatedDate(GlobalCommonDateUtils.getDate(deptDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(deptMapper);
		
		return new Response<>(SUCCESS, null, null, null, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		
	}
	

}
