package com.param.adt.discharge.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.param.global.model.CabinMaster;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.stereotype.Repository;

import com.param.adt.discharge.dto.DischargeSummaryTemplateDto;
import com.param.adt.discharge.dto.DischargeSummaryTemplateMapperDto;
import com.param.adt.discharge.model.DischargeSummaryTemplateMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class DischargeSummaryTemplateDaoImpl extends GenericDao<DischargeSummaryTemplateMaster, Integer>implements IDischargeSummaryTemplateDao, ICommonConstants{

	public DischargeSummaryTemplateDaoImpl() {
		super(DischargeSummaryTemplateMaster.class);
	}

	@Override
	public Response saveDischargeSummaryTemplate(DischargeSummaryTemplateDto dischargeSummaryTemplateDto) throws ApplicationException{
		try {
			DischargeSummaryTemplateMaster dischargeSummaryTemplateMaster=new DischargeSummaryTemplateMaster();
			dischargeSummaryTemplateMaster.setDischargeSummaryTempCode(dischargeSummaryTemplateDto.getDischargeSummaryTempCode());
			dischargeSummaryTemplateMaster.setDischargeSummaryTempDesc(dischargeSummaryTemplateDto.getDischargeSummaryTempDesc());
			dischargeSummaryTemplateMaster.setDepartmentId(dischargeSummaryTemplateDto.getDepartmentId());
			dischargeSummaryTemplateMaster.setOrganizationId(dischargeSummaryTemplateDto.getOrganizationId());
			dischargeSummaryTemplateMaster.setUnitId(dischargeSummaryTemplateDto.getUnitId());
			dischargeSummaryTemplateMaster.setStatus(dischargeSummaryTemplateDto.getStatus());
			dischargeSummaryTemplateMaster.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
			dischargeSummaryTemplateMaster.setCreatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryTemplateDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeSummaryTemplateMaster.setUpdatedBy(dischargeSummaryTemplateDto.getUpdatedBy());
			dischargeSummaryTemplateMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryTemplateDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeSummaryTemplateMaster.setIsHtml(dischargeSummaryTemplateDto.getIsHtml());
			dischargeSummaryTemplateMaster=save(dischargeSummaryTemplateMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, null, null, dischargeSummaryTemplateMaster);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDischargeSummaryTemplate(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)
			throws ApplicationException {
		try {
			DischargeSummaryTemplateMaster dischargeSummaryTemplateMaster=findById(dischargeSummaryTemplateDto.getDischargeSummaryTempId());
			dischargeSummaryTemplateMaster.setDischargeSummaryTempCode(dischargeSummaryTemplateDto.getDischargeSummaryTempCode());
			dischargeSummaryTemplateMaster.setDischargeSummaryTempDesc(dischargeSummaryTemplateDto.getDischargeSummaryTempDesc());
			dischargeSummaryTemplateMaster.setDepartmentId(dischargeSummaryTemplateDto.getDepartmentId());
			dischargeSummaryTemplateMaster.setOrganizationId(dischargeSummaryTemplateDto.getOrganizationId());
			dischargeSummaryTemplateMaster.setUnitId(dischargeSummaryTemplateDto.getUnitId());
			dischargeSummaryTemplateMaster.setStatus(dischargeSummaryTemplateDto.getStatus());
			//dischargeSummaryTemplateMaster.setCreatedBy(dischargeSummaryTemplateDto.getCreatedBy());
			//dischargeSummaryTemplateMaster.setCreatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryTemplateDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeSummaryTemplateMaster.setUpdatedBy(dischargeSummaryTemplateDto.getUpdatedBy());
			dischargeSummaryTemplateMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryTemplateDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeSummaryTemplateMaster.setIsHtml(dischargeSummaryTemplateDto.getIsHtml());
			 
			update(dischargeSummaryTemplateMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dischargeSummaryTemplateMaster);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateDischargeSummaryTemplateStatus(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException {
		try{
			Integer count=sessionFactory.getCurrentSession().createQuery("UPDATE  DischargeSummaryTemplateMaster diss set diss.status=:status,"
					+ " diss.updatedBy=:updatedBy,diss.updatedDate=:currdate where diss.dischargeSummaryTempId=:tempId " )
					.setParameter("status", dischargeSummaryTemplateDto.getStatus())
					.setParameter("updatedBy", dischargeSummaryTemplateDto.getUpdatedBy())
					.setParameter("currdate", TimeZoneComponent.getDateByzone(dischargeSummaryTemplateDto.getUnitId()))
					.setParameter("tempId", dischargeSummaryTemplateDto.getDischargeSummaryTempId())
					.executeUpdate();
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}


	@Override
	public Response getDischargeSummaryTemplateById(Integer dischargeSummaryTempId, Integer organizationId, Integer unitId)throws ApplicationException {
		try{
			DischargeSummaryTemplateDto dto=(DischargeSummaryTemplateDto)sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_BY_ID")
					.setParameter("dischargeSummaryTempId", dischargeSummaryTempId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryTemplateDto.class)).uniqueResult();
			
			List<DischargeSummaryTemplateDto> list1=sessionFactory.getCurrentSession().getNamedQuery("GET_DETAILED_DISCHARGE_SUMMARY_TEMPLATE_MASTER_LIST")
					                                .setParameter("dischargeSummaryTempId", dischargeSummaryTempId)
					            					.setParameter("organizationId", organizationId)
					            					.setParameter("unitId", unitId)
					            					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryTemplateDto.class)).list();
			
			String query=" select  " + 
					"	mppr.discharge_summary_temp_id as \"dischargeSummaryTempId\",  " + 
					"	mppr.department_id as \"id\" ,  " + 
					"	spl.speciality_name as \"lable\" , " + 
					"	mppr.department_id as \"departmentId\" ,  " + 
					"	spl.speciality_name as \"specialityName\"  " + 
					" from  " + 
					"	adt.t_discharge_template_department_mapper mppr  " + 
					" inner join doctor.m_speciality_master spl on  " + 
					"	spl.speciality_id = mppr.department_id  " + 
					" where mppr.status ='A'  " + 
					" and mppr.discharge_summary_temp_id =:id"
					+" and mppr.organization_id =:organizationId"
					+" and mppr.unit_id =:unitId";
			List<DischargeSummaryTemplateDto> list2 = sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("id", dischargeSummaryTempId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryTemplateDto.class)).list();   
			return new Response(SUCCESS, SUCCESS_CODE, null, list1, dto, list2);
		}
		catch(HibernateException he){
			throw he;
		}
		catch (Exception e) {
			throw e;
		}
//			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getDischargeSummaryTemplateMasterList(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException {
		try{

			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_LIST").getQueryString());

			if(dischargeSummaryTemplateDto.getSearchKeyword()!=null && !dischargeSummaryTemplateDto.getSearchKeyword().equals("") && !dischargeSummaryTemplateDto.getSearchKeyword().isEmpty()) {
				query.append(" AND ( "
						+ " lower( dischargeSummaryTemplateMaster.dischargeSummaryTempCode) like :searchKeyword "
						+ " OR lower(dischargeSummaryTemplateMaster.dischargeSummaryTempDesc) like :searchKeyword "
						+ ") ");
			}

			query.append(" ORDER BY dischargeSummaryTemplateMaster.status asc");

			Query createQuery = sessionFactory.getCurrentSession().createQuery(query.toString())
					.setParameter("organizationId", dischargeSummaryTemplateDto.getOrganizationId())
					.setParameter("unitId", dischargeSummaryTemplateDto.getUnitId())
					.setFirstResult(dischargeSummaryTemplateDto.getOffset() != null ? dischargeSummaryTemplateDto.getOffset() : 0)
					.setMaxResults(dischargeSummaryTemplateDto.getNoOfRecordsPerPage() != null ? dischargeSummaryTemplateDto.getNoOfRecordsPerPage() : 10);

			if(dischargeSummaryTemplateDto.getSearchKeyword()!=null && !dischargeSummaryTemplateDto.getSearchKeyword().equals("") && !dischargeSummaryTemplateDto.getSearchKeyword().isEmpty()) {
				createQuery.setParameter("searchKeyword", "%" + dischargeSummaryTemplateDto.getSearchKeyword().toLowerCase() + "%");
			}

			List<DischargeSummaryTemplateDto> list=createQuery.setResultTransformer(Transformers.aliasToBean(DischargeSummaryTemplateDto.class))
					.list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}
		catch(HibernateException he){
			throw he;
		}
		catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response getDischargeSummaryTemplateCount(Integer organizationId, Integer unitId, String searchKeyword)throws ApplicationException {
		try{

			Session session = sessionFactory.getCurrentSession();
			StringBuilder queryCount=new StringBuilder(session.getNamedQuery("GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_COUNT").getQueryString());

			if (searchKeyword != null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {
				queryCount.append(" AND ( "
						+ " lower(dstm.dischargeSummaryTempCode) like :searchKeyword "
						+ " OR lower(dstm.dischargeSummaryTempDesc) like :searchKeyword "
						+ ") ");
			}

			Query createQuery = sessionFactory.getCurrentSession().createQuery(queryCount.toString())
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					;

			if (searchKeyword != null && !searchKeyword.equals("") && !searchKeyword.isEmpty()) {
				createQuery.setParameter("searchKeyword", "%" + searchKeyword.toLowerCase() + "%");
			}

			Object count= 	createQuery.uniqueResult();

			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
		}
		catch (Exception e) {
			throw e;
		}
	}
	@Override
	public Response getActiveDischargeSummaryTemplate(Integer organizationId, Integer unitId) throws ApplicationException{
		try{
			List<DischargeSummaryTemplateDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DISCHARGE_SUMMARY_TEMPLATE_MASTER")
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
	public Response getDischargeSummaryTemplateByName(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException {
		try{
			List<DischargeSummaryTemplateDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_BY_NAME")
					.setParameter("dischargeSummaryTempDesc", dischargeSummaryTemplateDto.getDischargeSummaryTempDesc().toLowerCase())
					.setParameter("organizationId", dischargeSummaryTemplateDto.getOrganizationId())
					.setParameter("unitId", dischargeSummaryTemplateDto.getUnitId())
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
	public Response getDischargeSummaryTemplateByNameNotId(DischargeSummaryTemplateDto dischargeSummaryTemplateDto)throws ApplicationException {
		try{
			List<DischargeSummaryTemplateDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISCHARGE_SUMMARY_TEMPLATE_MASTER_BY_NAME_NOT_ID")
					.setParameter("dischargeSummaryTempId", dischargeSummaryTemplateDto.getDischargeSummaryTempId())
					.setParameter("dischargeSummaryTempDesc", dischargeSummaryTemplateDto.getDischargeSummaryTempDesc().toLowerCase())
					.setParameter("organizationId", dischargeSummaryTemplateDto.getOrganizationId())
					.setParameter("unitId", dischargeSummaryTemplateDto.getUnitId())
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
	public Response getDischargeSummaryTemplateByName(List<DischargeSummaryTemplateDto> dischargeSummaryTemplateDto) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response saveListDischargeSummaryTemplate(List<DischargeSummaryTemplateDto> dischargeSummaryTemplateDto) {
		List<DischargeSummaryTemplateMaster> d2=new ArrayList<>();
		try{
		for(DischargeSummaryTemplateDto list : dischargeSummaryTemplateDto){
			DischargeSummaryTemplateMaster dischargeSummaryTemplateMaster=new DischargeSummaryTemplateMaster();
			dischargeSummaryTemplateMaster.setDischargeFieldId(list.getDischargeFieldId());
			dischargeSummaryTemplateMaster.setDepartmentId(list.getDepartmentId());
			dischargeSummaryTemplateMaster.setDischargeSummaryTempCode(list.getDischargeSummaryTempCode());
			dischargeSummaryTemplateMaster.setDischargeSummaryTempDesc(list.getDischargeSummaryTempDesc());
			dischargeSummaryTemplateMaster.setOrganizationId(list.getOrganizationId());
			dischargeSummaryTemplateMaster.setUnitId(list.getUnitId());
			dischargeSummaryTemplateMaster.setStatus(list.getStatus());
			dischargeSummaryTemplateMaster.setCreatedBy(list.getCreatedBy());
			dischargeSummaryTemplateMaster.setCreatedDate(GlobalCommonDateUtils.getDate(list.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeSummaryTemplateMaster.setUpdatedBy(list.getUpdatedBy());
			dischargeSummaryTemplateMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(list.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			d2.add(dischargeSummaryTemplateMaster);
			save(dischargeSummaryTemplateMaster);
			}
	

		return new Response(SUCCESS, SUCCESS_CODE, null, null, d2);
	}
	catch(HibernateException he){
	}
	catch (Exception e) {
		
	}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	

}
