package com.param.adt.discharge.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.adt.discharge.dto.DischargeSumColumnDto;
import com.param.adt.discharge.dto.DischargeSumTableColumnsMasterDto;
import com.param.adt.discharge.dto.DischargeSummaryDto;
import com.param.adt.discharge.dto.DischargeSummaryFieldDto;
import com.param.adt.discharge.model.DischargeSummaryFieldMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class DischargeSummaryFieldDaoImpl extends GenericDao<DischargeSummaryFieldMaster, Integer>implements IDischargeSummaryFieldDao, ICommonConstants{

	public DischargeSummaryFieldDaoImpl() {
		super(DischargeSummaryFieldMaster.class);
	}

	@Override
	public Response saveDischargeSummaryField(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try {
			DischargeSummaryFieldMaster dischargeSummaryFieldMaster=new DischargeSummaryFieldMaster();
			dischargeSummaryFieldMaster.setDischargeFieldCode(dischargeSummaryFieldDto.getDischargeFieldCode());
			dischargeSummaryFieldMaster.setDischargeFieldDesc(dischargeSummaryFieldDto.getDischargeFieldDesc());
			dischargeSummaryFieldMaster.setSummaryFieldDesc(dischargeSummaryFieldDto.getSummaryFieldDesc());
			dischargeSummaryFieldMaster.setOrganizationId(dischargeSummaryFieldDto.getOrganizationId());
			dischargeSummaryFieldMaster.setUnitId(dischargeSummaryFieldDto.getUnitId());
			dischargeSummaryFieldMaster.setStatus(dischargeSummaryFieldDto.getStatus());
			dischargeSummaryFieldMaster.setCreatedBy(dischargeSummaryFieldDto.getCreatedBy());
			dischargeSummaryFieldMaster.setCreatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryFieldDto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeSummaryFieldMaster.setUpdatedBy(dischargeSummaryFieldDto.getUpdatedBy());
			dischargeSummaryFieldMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryFieldDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			dischargeSummaryFieldMaster.setDischargeFieldTemplateObj(dischargeSummaryFieldDto.getDischargeFieldTemplateObj());
			save(dischargeSummaryFieldMaster);
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDischargeSummaryField(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try{
			DischargeSummaryFieldMaster dischargeSummaryFieldMaster=findById(dischargeSummaryFieldDto.getDischargeFieldId());
			dischargeSummaryFieldMaster.setDischargeFieldCode(dischargeSummaryFieldDto.getDischargeFieldCode());
			dischargeSummaryFieldMaster.setDischargeFieldDesc(dischargeSummaryFieldDto.getDischargeFieldDesc());
			dischargeSummaryFieldMaster.setSummaryFieldDesc(dischargeSummaryFieldDto.getSummaryFieldDesc());
			dischargeSummaryFieldMaster.setDischargeFieldTemplateObj(dischargeSummaryFieldDto.getDischargeFieldTemplateObj());
//			dischargeSummaryFieldMaster.setOrganizationId(dischargeSummaryFieldDto.getOrganizationId());
//			dischargeSummaryFieldMaster.setUnitId(dischargeSummaryFieldDto.getUnitId());
			//dischargeSummaryFieldMaster.setStatus(dischargeSummaryFieldDto.getStatus());
			dischargeSummaryFieldMaster.setUpdatedBy(dischargeSummaryFieldDto.getUpdatedBy());
			dischargeSummaryFieldMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryFieldDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(dischargeSummaryFieldMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response updateDischargeSummaryFieldStatus(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try{
			DischargeSummaryFieldMaster dischargeSummaryFieldMaster=findById(dischargeSummaryFieldDto.getDischargeFieldId());
			dischargeSummaryFieldMaster.setStatus(dischargeSummaryFieldDto.getStatus());
			dischargeSummaryFieldMaster.setUpdatedBy(dischargeSummaryFieldDto.getUpdatedBy());
			dischargeSummaryFieldMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryFieldDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			update(dischargeSummaryFieldMaster);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}


	@Override
	public Response getDischargeSummaryFieldById(Integer dischargeFieldId) {
		try{
			List<DischargeSummaryFieldDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISCHARGE_SUMMARY_FIELD_MASTER_BY_ID")
					.setParameter("dischargeFieldId", dischargeFieldId)
//					.setParameter("organizationId", organizationId)
//					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryFieldDto.class)).list();
	
		
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getDischargeSummaryFieldMasterList(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try{

			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("GET_DISCHARGE_SUMMARY_FIELD_MASTER_LIST").getQueryString());

			if(dischargeSummaryFieldDto.getSearchKeyword()!=null && !dischargeSummaryFieldDto.getSearchKeyword().equals("") && !dischargeSummaryFieldDto.getSearchKeyword().isEmpty()) {
				query.append(" AND ( "
						+ " lower( dischargeSummaryFieldMaster.dischargeFieldCode ) like :searchKeyword "
						+ " OR lower( dischargeSummaryFieldMaster.dischargeFieldDesc ) like :searchKeyword "
						+ " OR lower( dischargeSummaryFieldMaster.summaryFieldDesc ) like :searchKeyword "
						+ ") ");
			}

			query.append(" ORDER BY dischargeSummaryFieldMaster.dischargeFieldId asc");

			Query createQuery = sessionFactory.getCurrentSession().createQuery(query.toString())
					.setParameter("organizationId", dischargeSummaryFieldDto.getOrganizationId())
					.setFirstResult(dischargeSummaryFieldDto.getOffset() != null ? dischargeSummaryFieldDto.getOffset() : 0)
					.setMaxResults(dischargeSummaryFieldDto.getNoOfRecordsPerPage() != null ? dischargeSummaryFieldDto.getNoOfRecordsPerPage() : 10);


			if(dischargeSummaryFieldDto.getSearchKeyword()!=null && !dischargeSummaryFieldDto.getSearchKeyword().equals("") && !dischargeSummaryFieldDto.getSearchKeyword().isEmpty()) {
				createQuery.setParameter("searchKeyword","%" + dischargeSummaryFieldDto.getSearchKeyword().toLowerCase() + "%");
			}

			List<DischargeSummaryFieldDto> list=createQuery.setResultTransformer(Transformers.aliasToBean(DischargeSummaryFieldDto.class))
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
	public Response getDischargeSummaryFieldCount(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try{

			Session session = sessionFactory.getCurrentSession();
			StringBuilder queryCount=new StringBuilder(session.getNamedQuery("GET_DISCHARGE_SUMMARY_FIELD_MASTER_COUNT").getQueryString());

			if (dischargeSummaryFieldDto.getSearchKeyword() != null && !dischargeSummaryFieldDto.getSearchKeyword().equals("") && !dischargeSummaryFieldDto.getSearchKeyword().isEmpty()) {
				queryCount.append(" AND ( "
						+ " lower( dischargeSummaryFieldMaster.dischargeFieldCode ) like :searchKeyword "
						+ " OR lower( dischargeSummaryFieldMaster.dischargeFieldDesc ) like :searchKeyword "
						+ " OR lower( dischargeSummaryFieldMaster.summaryFieldDesc ) like :searchKeyword "
						+ ") ");
			}

			if (dischargeSummaryFieldDto.getSearchKeyword() != null && !dischargeSummaryFieldDto.getSearchKeyword().equals("") && !dischargeSummaryFieldDto.getSearchKeyword().isEmpty()) {

			}

			Query createQuery = sessionFactory.getCurrentSession().createQuery(queryCount.toString())
				//	.setParameter("searchKeyword", "%" + dischargeSummaryFieldDto.getSearchKeyword().toLowerCase() + "%")
					.setParameter("organizationId", dischargeSummaryFieldDto.getOrganizationId());
			if (dischargeSummaryFieldDto.getSearchKeyword() != null && !dischargeSummaryFieldDto.getSearchKeyword().equals("") && !dischargeSummaryFieldDto.getSearchKeyword().isEmpty()) {
				createQuery.setParameter("searchKeyword", "%" + dischargeSummaryFieldDto.getSearchKeyword().toLowerCase() + "%");
			}
			Long count= (Long)	createQuery.uniqueResult();
			
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	@Override
	public Response getActiveDischargeSummaryField(Integer organisationId) {
		try{
			List<DischargeSummaryFieldDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DISCHARGE_SUMMARY_FIELD_MASTER")
					.setParameter("organizationId", organisationId)
//					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryFieldDto.class)).list();

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
	public Response getActiveDischargeSummaryFieldMapper(Integer organisationId) {
		try{
			List<DischargeSummaryFieldDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_DISCHARGE_SUMMARY_FIELD_MAPPER_MASTER")
					.setParameter("organizationId", organisationId)
//					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryFieldDto.class)).list();

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
	public Response getDischargeSummaryFieldByName(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try{
			List<DischargeSummaryFieldDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISCHARGE_SUMMARY_FIELD_MASTER_BY_NAME")
					.setParameter("dischargeFieldDesc", dischargeSummaryFieldDto.getDischargeFieldDesc().toLowerCase())
//					.setParameter("organizationId", dischargeSummaryFieldDto.getOrganizationId())
//					.setParameter("unitId", dischargeSummaryFieldDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryFieldDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getDischargeSummaryFieldByNameNotId(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try{
			List<DischargeSummaryFieldDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_DISCHARGE_SUMMARY_FIELD_MASTER_BY_NAME_NOT_ID")
					.setParameter("dischargeFieldId", dischargeSummaryFieldDto.getDischargeFieldId())
//					.setParameter("organizationId", dischargeSummaryFieldDto.getOrganizationId())
					.setParameter("dischargeFieldDesc", dischargeSummaryFieldDto.getDischargeFieldDesc().toLowerCase())
//					.setParameter("unitId", dischargeSummaryFieldDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryFieldDto.class)).list();

			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getDischargeSumColumnDetails(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try{
			List<DischargeSummaryFieldDto> listMain=new ArrayList<>();
			
			String query = " select " 
					+ " fieldMst.discharge_field_id as \"dischargeFieldId\"," + 
					" fieldMst.summary_field_desc as \"summaryFieldDesc\" "
					+ " from "
					+ "	adt.m_discharge_summary_field_master fieldMst "
					+ " where "
					+ " 	fieldMst.discharge_field_id = :id " ;
			listMain=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("id", dischargeSummaryFieldDto.getDischargeFieldId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryFieldDto.class)).list();
	
			for (DischargeSummaryFieldDto dto : listMain) {
				List<DischargeSummaryFieldDto> listChild=new ArrayList<>();
				String queryChild = " select " 
						+ "	mst.disch_sum_tab_col_mst_id as \"dischSumTabColMstId\", "
						+ "	mst.column_name as \"columnName\", "
						+ " mst.discharge_field_id as \"dischargeFieldId\"," + 
						" fieldMst.summary_field_desc as \"summaryFieldDesc\" "
						+ " from "
						+ "	emr.m_discharge_sum_tab_columns_master mst "
						+ " inner join adt.m_discharge_summary_field_master fieldMst on fieldMst.discharge_field_id = mst.discharge_field_id "
						+ " where "
						+ "	mst.discharge_field_id = :id " 
						+ "	and mst.organization_id = :organizationId";
				
				listChild=sessionFactory.getCurrentSession().createSQLQuery(queryChild)
						.setParameter("organizationId", dischargeSummaryFieldDto.getOrganizationId())
						.setParameter("id", dischargeSummaryFieldDto.getDischargeFieldId())
						.setResultTransformer(Transformers.aliasToBean(DischargeSummaryFieldDto.class)).list();
				
				dto.setListOfColumns(listChild);
			}
			 
		
			return new Response(SUCCESS, SUCCESS_CODE, null, listMain, null);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response updateDischSumColumnsToPrint(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try{
			
			
			DischargeSummaryFieldMaster dischargeSummaryFieldMaster=findById(dischargeSummaryFieldDto.getDischargeFieldId());
			dischargeSummaryFieldMaster.setDischargeFieldCode(dischargeSummaryFieldDto.getDischargeFieldCode());
			dischargeSummaryFieldMaster.setDischargeFieldDesc(dischargeSummaryFieldDto.getDischargeFieldDesc());
			dischargeSummaryFieldMaster.setSummaryFieldDesc(dischargeSummaryFieldDto.getSummaryFieldDesc());
			dischargeSummaryFieldMaster.setDischargeFieldTemplateObj(dischargeSummaryFieldDto.getDischargeFieldTemplateObj());
			dischargeSummaryFieldMaster.setDischargeFieldColumnJson(dischargeSummaryFieldDto.getDischargeFieldColumnJson());
//			dischargeSummaryFieldMaster.setOrganizationId(dischargeSummaryFieldDto.getOrganizationId());
//			dischargeSummaryFieldMaster.setUnitId(dischargeSummaryFieldDto.getUnitId());
			//dischargeSummaryFieldMaster.setStatus(dischargeSummaryFieldDto.getStatus());
			dischargeSummaryFieldMaster.setUpdatedBy(dischargeSummaryFieldDto.getUpdatedBy());
			dischargeSummaryFieldMaster.setUpdatedDate(GlobalCommonDateUtils.getDate(dischargeSummaryFieldDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(dischargeSummaryFieldMaster);
//			updateDischSumColumnsRequired(dischargeSummaryFieldDto);
			return new Response(SUCCESS, SUCCESS_CODE, null, null, dischargeSummaryFieldDto);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getDischSumColumnsDetailsForPrint(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try{
			List<DischargeSummaryFieldDto> listMain=new ArrayList<>();
			
			String query = " select " 
					+ " mppr.discharge_field_id as \"dischargeFieldId\"," + 
					" mppr.tab_column_json as \"dischargeFieldColumnJson\" " + 
					" from " + 
					" emr.t_discharge_sum_tab_columns_print_mapper mppr "
					+ " where "
					+ " mppr.discharge_field_id = "+dischargeSummaryFieldDto.getDischargeFieldId() 
					+ " and mppr.organization_id = "+dischargeSummaryFieldDto.getOrganizationId()
					+ " and mppr.unit_id = "+dischargeSummaryFieldDto.getUnitId() 
			+ " and mppr.status = 'A'"; 
			listMain=sessionFactory.getCurrentSession().createSQLQuery(query)
//					.setParameter("id", dischargeSummaryFieldDto.getDischargeFieldId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSummaryFieldDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMain, null);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getDischSumColumnsList(DischargeSummaryFieldDto dischargeSummaryFieldDto) {
		try{/*
			List<DischargeSumTableColumnsMasterDto> listMain=new ArrayList<>();
			
			String query = " select " + 
					"	mst.disch_sum_tab_col_mst_id as \"dischSumTabColMstId\", " + 
					"	mst.discharge_field_id as \"dischargeFieldId\", " + 
					"	mst.table_id as \"tableId\", " + 
					"	mst.table_name as \"tableName\", " + 
					"	mst.column_name as \"columnName\", " + 
					"	mst.is_column_required as \"isColumnRequired\", " + 
					"	mst.status as \"status\", " + 
					"	mst.unit_id as \"unitId\", " + 
					"	mdsfm.discharge_field_desc as \"dischargeFieldDesc\", " + 
					"	mst.organization_id as \"organizationId\" " + 
					" from " + 
					"	emr.m_discharge_sum_tab_columns_master mst " + 
					" inner join adt.m_discharge_summary_field_master mdsfm on " + 
					"	mdsfm.discharge_field_id = mst.discharge_field_id " + 
					" where " + 
					"	mst.discharge_field_id = :id " + 
					"	and mst.organization_id = :organizationId" ;
			listMain=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("id", dischargeSummaryFieldDto.getDischargeFieldId())
					.setParameter("organizationId", dischargeSummaryFieldDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSumTableColumnsMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, listMain, null);
		*/
			
			List<DischargeSumTableColumnsMasterDto> listMain=new ArrayList<>();
			
			String query = " select " + 
					"	distinct (mst.discharge_field_id) as \"dischargeFieldId\"," + 
					"	mdsfm.discharge_field_desc as \"dischargeFieldDesc\"," + 
					"	mdsfm.discharge_field_code as \"dischargeFieldCode\"," + 
					"	mppr.tab_column_json as \"dischargeFieldColumnJson\"," + 
					"	mdsfm.summary_field_desc as \"summaryFieldDesc\"," + 
//					"	mst.organization_id as \"organizationId\" ,"
					 " mst.table_id as \"tableId\", " + 
					"	mst.table_name as \"tableName\" " + 
					" from " + 
					"	emr.m_discharge_sum_tab_columns_master mst " + 
					" inner join adt.m_discharge_summary_field_master mdsfm on " + 
					"	mdsfm.discharge_field_id = mst.discharge_field_id "
					+ " left join emr.t_discharge_sum_tab_columns_print_mapper mppr on " + 
					"	mdsfm.discharge_field_id = mppr.discharge_field_id " + 
					" where " + 
					"	mst.discharge_field_id = :id " +
					"	and mst.status = 'A' " +
					"	and mst.unit_id = :unitId " +
					"	and mst.organization_id = :organizationId"  +
					"	and mppr.status = 'A' " +
					"	and mppr.unit_id = :unitId " +
					"	and mppr.organization_id = :organizationId"  ;
			listMain=sessionFactory.getCurrentSession().createSQLQuery(query)
					.setParameter("id", dischargeSummaryFieldDto.getDischargeFieldId())
					.setParameter("unitId", dischargeSummaryFieldDto.getUnitId())
					.setParameter("organizationId", dischargeSummaryFieldDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DischargeSumTableColumnsMasterDto.class)).list();
	
			for (DischargeSumTableColumnsMasterDto dto : listMain) {
				List<DischargeSumTableColumnsMasterDto> listChild=new ArrayList<>();
				String queryChild = " select " + 
						"	mst.disch_sum_tab_column_id as \"dischSumTabColMstId\", " + 
						"	mst.discharge_field_id as \"dischargeFieldId\", " + 
						"	mst.table_id as \"tableId\", " + 
						"	mst.table_name as \"tableName\", " + 
						"	mst.column_name as \"columnName\", " + 
						"	mst.is_column_required as \"isColumnRequired\", " + 
//						"	mst.status as \"status\" ," + 
//						"	mst.unit_id as \"unitId\", " + 
						"	mdsfm.discharge_field_desc as \"dischargeFieldDesc\" " + 
//						"	mst.organization_id as \"organizationId\" " + 
						" from " + 
						"	emr.m_discharge_sum_tab_columns_master mst " + 
						" inner join adt.m_discharge_summary_field_master mdsfm on " + 
						"	mdsfm.discharge_field_id = mst.discharge_field_id "
						+ " where "
						+ "	mst.table_id = :id " +
						"	and mst.status = 'A' " 
						+ "	and mst.unit_id = :unitId " 
						+ "	and mst.organization_id = :organizationId";
				
				listChild=sessionFactory.getCurrentSession().createSQLQuery(queryChild)
						.setParameter("organizationId", dischargeSummaryFieldDto.getOrganizationId())
						.setParameter("unitId", dischargeSummaryFieldDto.getUnitId())
						.setParameter("id", dto.getTableId())
						.setResultTransformer(Transformers.aliasToBean(DischargeSumColumnDto.class)).list();
				
				dto.setListOfColumns(listChild);
			}
			return new Response(SUCCESS, SUCCESS_CODE, null, listMain, null);
		}
		catch(HibernateException he){
		}
		catch (Exception e) {
			
		}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response updateDischSumColumnsRequired(DischargeSummaryDto dischargeSummaryDto) {
		try{
			for (DischargeSumTableColumnsMasterDto dto : dischargeSummaryDto.getColumnsListData()) {
				Session session=sessionFactory.getCurrentSession();
				session.createSQLQuery(" update emr.m_discharge_sum_tab_columns_master  set is_column_required =:isColumnRequired "
				+ " where disch_sum_tab_column_id=:dischSumTabColMstId "
				+ " and organization_id=:organizationId "
				+ " and unit_id=:unitId "
				+ " and status='A'"
				)
				.setParameter("dischSumTabColMstId", dto.getDischSumTabColMstId())
				.setParameter("organizationId", dischargeSummaryDto.getOrganizationId())
				.setParameter("unitId", dischargeSummaryDto.getUnitId())
				.setParameter("isColumnRequired", dto.getIsColumnRequired())
				.executeUpdate();
			}
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		}
		catch(HibernateException he){
			System.out.println(he);
		}
		catch (Exception e) {
			System.out.println(e);
		}
			return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

}
