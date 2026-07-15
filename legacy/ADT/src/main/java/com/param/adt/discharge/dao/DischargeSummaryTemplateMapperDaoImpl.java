package com.param.adt.discharge.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.stereotype.Repository;

import com.param.adt.discharge.dto.DischargeSummaryTemplateDto;
import com.param.adt.discharge.dto.DischargeSummaryTemplateMapperDto;
import com.param.adt.discharge.model.DischargeSummaryTemplateMapper;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.CabinMaster;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class DischargeSummaryTemplateMapperDaoImpl extends GenericDao<DischargeSummaryTemplateMapper, Integer>implements IDischargeSummaryTemplateMapperDao, ICommonConstants{

	public DischargeSummaryTemplateMapperDaoImpl() {
		super(DischargeSummaryTemplateMapper.class);
	}

	@Override
	public Response saveDischargeSummaryFieldDetails(DischargeSummaryTemplateMapperDto dischargeSummaryTemplateMapperDto) throws ApplicationException {
		try {
			DischargeSummaryTemplateMapper dischargeSummaryTemplateMapper=new DischargeSummaryTemplateMapper();
			dischargeSummaryTemplateMapper.setDischargeFieldId(dischargeSummaryTemplateMapperDto.getDischargeFieldId());
			dischargeSummaryTemplateMapper.setDischargeSummaryTempId(dischargeSummaryTemplateMapperDto.getDischargeSummaryTempId());
			dischargeSummaryTemplateMapper.setOrganizationId(dischargeSummaryTemplateMapperDto.getOrganizationId());
			dischargeSummaryTemplateMapper.setUnitId(dischargeSummaryTemplateMapperDto.getUnitId());
			dischargeSummaryTemplateMapper.setStatus(dischargeSummaryTemplateMapperDto.getStatus());
			dischargeSummaryTemplateMapper.setCreatedBy(dischargeSummaryTemplateMapperDto.getCreatedBy());
			dischargeSummaryTemplateMapper.setCreatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryTemplateMapperDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeSummaryTemplateMapper.setUpdatedBy(dischargeSummaryTemplateMapperDto.getUpdatedBy());
			dischargeSummaryTemplateMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryTemplateMapperDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(dischargeSummaryTemplateMapper);
		
		return new Response<>(SUCCESS, null, null, null, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getDetailedDischargeSummaryTemplateList(Integer dischargeSummaryTempId, Integer organizationId, Integer unitId)throws ApplicationException {
		try{
			List<DischargeSummaryTemplateDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DETAILED_DISCHARGE_SUMMARY_TEMPLATE_MASTER_LIST")
					.setParameter("dischargeSummaryTempId", dischargeSummaryTempId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryTemplateDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response updateDischargeSummaryFieldDetails(DischargeSummaryTemplateMapperDto dto) {
		try {
			/*DischargeSummaryTemplateMapper dischargeSummaryTemplateMapper=(DischargeSummaryTemplateMapper)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DETAILED_DISCHARGE_SUMMARY_TEMPLATE_MASTER_BY_DISCHARGE_FIELD_ID")
					.setParameter("dischargeSummaryTempId", dto.getDischargeSummaryTempId())
					.setParameter("dischargeFieldId", dto.getDischargeFieldId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryTemplateMapper.class)).uniqueResult();*/
			/*DischargeSummaryTemplateMapper dischargeSummaryTemplateMapper=findById(dto.getDischargeFieldId());
			dischargeSummaryTemplateMapper.setStatus(dto.getStatus());
			dischargeSummaryTemplateMapper.setUpdatedBy(dto.getUpdatedBy());
			dischargeSummaryTemplateMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			
		    dischargeSummaryTemplateMapper.setDischargeFieldId(dto.getDischargeFieldId());
			dischargeSummaryTemplateMapper.setDischargeSummaryTempId(dto.getDischargeSummaryTempId());
			dischargeSummaryTemplateMapper.setOrganizationId(dto.getOrganizationId());
			dischargeSummaryTemplateMapper.setUnitId(dto.getUnitId());
			dischargeSummaryTemplateMapper.setCreatedBy(dto.getCreatedBy());
			 
			update(dischargeSummaryTemplateMapper);*/
			
			String qry = null;
			
			 qry = "update adt.t_discharge_summary_template_mapper set status ='I' WHERE discharge_summary_temp_id=:dischargeSummaryTempId "
			+"and  discharge_field_id=:dischargeFieldId ";
			 sessionFactory.getCurrentSession()
				.createSQLQuery(qry)
					 .setParameter("dischargeSummaryTempId", dto.getDischargeSummaryTempId())
					 .setParameter("dischargeFieldId", dto.getDischargeFieldId())
		        .executeUpdate();
		
		return new Response<>(SUCCESS, null, null, null, null);
		
		} catch (HibernateException he) {
			throw he;
		} catch (Exception e) {
			throw e;
		}
		//return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		
	}

}
