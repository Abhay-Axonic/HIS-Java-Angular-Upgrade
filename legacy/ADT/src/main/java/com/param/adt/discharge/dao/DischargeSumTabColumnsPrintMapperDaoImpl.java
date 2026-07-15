package com.param.adt.discharge.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.discharge.dto.DischargeSummaryDto;
import com.param.adt.discharge.model.DischargeSumTabColumnsPrintMapper;
import com.param.adt.discharge.model.DischargeSummary;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.SlotMasterDto;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DischargeSumTabColumnsPrintMapperDaoImpl extends GenericDao<DischargeSumTabColumnsPrintMapper, Integer> implements IDischargeSumTabColumnsPrintMapperDao,ICommonConstants{
	
	public DischargeSumTabColumnsPrintMapperDaoImpl() {
		super(DischargeSumTabColumnsPrintMapper.class);
	}
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Response saveDischargeSumTabColumnsPrintMapper(DischargeSummaryDto dischargeSummaryDto) {
		try {
			
				DischargeSumTabColumnsPrintMapper dischargeSumTabColumnsPrintMapper=new DischargeSumTabColumnsPrintMapper();
				dischargeSumTabColumnsPrintMapper.setDischargeFieldId(dischargeSummaryDto.getDischargeFieldId());
				dischargeSumTabColumnsPrintMapper.setTabColumnJson(dischargeSummaryDto.getTabColumnJson());
				dischargeSumTabColumnsPrintMapper.setOrganizationId(dischargeSummaryDto.getOrganizationId());
				dischargeSumTabColumnsPrintMapper.setUnitId(dischargeSummaryDto.getUnitId());
				dischargeSumTabColumnsPrintMapper.setStatus(dischargeSummaryDto.getStatus());
				dischargeSumTabColumnsPrintMapper.setCreatedBy(dischargeSummaryDto.getCreatedBy());
				dischargeSumTabColumnsPrintMapper.setCreatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				dischargeSumTabColumnsPrintMapper.setUpdatedBy(dischargeSummaryDto.getUpdatedBy());
				dischargeSumTabColumnsPrintMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				dischargeSumTabColumnsPrintMapper=save(dischargeSumTabColumnsPrintMapper);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkDischSumTabColPrintMapperAvailable(DischargeSummaryDto dischargeSummaryDto) {
		try {
			List<DischargeSummaryDto> list = sessionFactory.getCurrentSession()
					.createSQLQuery("  select mppr.discharge_field_id as \"dischargeFieldId\", " + 
							" mppr.tab_column_json as \"tabColumnJson\"  " + 
							" from emr.t_discharge_sum_tab_columns_print_mapper mppr " + 
							" where mppr.discharge_field_id =:fieldId  " + 
							" and mppr.organization_id =:orgId " + 
							" and mppr.unit_id =:unitId " + 
							" and mppr.status ='A'")
					.setParameter("fieldId", dischargeSummaryDto.getDischargeFieldId())
					.setParameter("unitId", dischargeSummaryDto.getUnitId())
					.setParameter("orgId", dischargeSummaryDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryDto.class)).list();
					
			 return new Response(SUCCESS, null, null, list, null);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public Response updateDischargeSumTabColumnsPrintMapper(DischargeSummaryDto dischargeSummaryDto) {
		try {
			sessionFactory.getCurrentSession()
//			.createSQLQuery("update emr.t_discharge_sum_tab_columns_print_mapper mppr set mppr.tab_column_json="+"'"+dischargeSummaryDto.getTabColumnJson()+"'"
			.createSQLQuery("update " + 
					"	emr.t_discharge_sum_tab_columns_print_mapper mppr " + 
					"set " + 
					"	tab_column_json ="+"'"+dischargeSummaryDto.getTabColumnJson()+"'" + 
					"where " + 
					"	mppr.discharge_field_id =:fieldId "+
					"	and mppr.organization_id =:orgId "+
					"	and mppr.unit_id =:unitId "+
					"	and mppr.status = 'A'")
			.setParameter("fieldId", dischargeSummaryDto.getDischargeFieldId())
			.setParameter("orgId", dischargeSummaryDto.getOrganizationId())
			.setParameter("unitId", dischargeSummaryDto.getUnitId())
			.executeUpdate();
					
			 return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, dischargeSummaryDto);
		}catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return null;
	}
	
}
