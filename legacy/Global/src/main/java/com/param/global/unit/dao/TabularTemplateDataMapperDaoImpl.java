package com.param.global.unit.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.model.TabularTemplateDataMapper;
import com.param.global.model.TabularTemplateMaster;
import com.param.global.unit.dto.TabularTemplateDataMapperDto;
import com.param.global.unit.dto.TabularTemplateMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class TabularTemplateDataMapperDaoImpl extends GenericDao<TabularTemplateDataMapper, Integer> implements ITabularTemplateDataMapperDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(TabularTemplateDataMapperDaoImpl.class);


	public TabularTemplateDataMapperDaoImpl() {
		super(TabularTemplateDataMapper.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveTabularTemplateDataMapper(TabularTemplateDataMapperDto tabularTemplateDataMapperDto)
			throws ApplicationException {
		try {
			 sessionFactory.getCurrentSession().createSQLQuery(
						"update public.m_tabular_template_data_mapper set status = 'I' where tabular_template_id = :tabularTemplateId")
						.setParameter("tabularTemplateId", tabularTemplateDataMapperDto.getTabularTemplateId()).executeUpdate();	
			 
			for(TabularTemplateDataMapperDto dto : tabularTemplateDataMapperDto.getToSaveList())
			{
			TabularTemplateDataMapper tabularTemplateDataMapper=new TabularTemplateDataMapper();
			tabularTemplateDataMapper.setTabularTemplateId(dto.getTabularTemplateId());
			tabularTemplateDataMapper.setTableName(dto.getTableName());
			tabularTemplateDataMapper.setColumnName(dto.getColumnName());
			tabularTemplateDataMapper.setCoulmnId(dto.getCoulmnId());
			tabularTemplateDataMapper.setRowName(dto.getRowName());
			tabularTemplateDataMapper.setRowId(dto.getRowId());
			tabularTemplateDataMapper.setInputValue(dto.getInputValue());
			tabularTemplateDataMapper.setNumberOfCoulmn(dto.getNumberOfCoulmn());
			tabularTemplateDataMapper.setNumberOfRow(dto.getNumberOfRow());
			tabularTemplateDataMapper.setOrganizationId(dto.getOrganizationId());
			tabularTemplateDataMapper.setUnitId(dto.getUnitId());
			tabularTemplateDataMapper.setStatus(dto.getStatus());
			tabularTemplateDataMapper.setCreatedBy(dto.getCreatedBy());
			tabularTemplateDataMapper.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			tabularTemplateDataMapper.setUpdatedBy(dto.getUpdatedBy());
			tabularTemplateDataMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
			save(tabularTemplateDataMapper);
			}
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("saveTabularTemplateDataMapper failed", he);
		} catch (Exception e) {
		logger.error("saveTabularTemplateDataMapper failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTabularTemplateDataMapper(TabularTemplateDataMapperDto tabularTemplateDataMapperDto)
			throws ApplicationException {
		try {
			
			 sessionFactory.getCurrentSession().createSQLQuery(
					"update public.m_tabular_template_data_mapper set status = 'I' where tabular_template_id = :getTabularTemplateId")
					.setParameter("tabularTemplateId", tabularTemplateDataMapperDto.getTabularTemplateId()).executeUpdate();	
			
			 for(TabularTemplateDataMapperDto dto : tabularTemplateDataMapperDto.getToSaveList())
				{
				TabularTemplateDataMapper tabularTemplateDataMapper=new TabularTemplateDataMapper();
				tabularTemplateDataMapper.setTabularTemplateId(dto.getTabularTemplateId());
				tabularTemplateDataMapper.setTableName(dto.getTableName());
				tabularTemplateDataMapper.setColumnName(dto.getColumnName());
				tabularTemplateDataMapper.setCoulmnId(dto.getCoulmnId());
				tabularTemplateDataMapper.setRowName(dto.getRowName());
				tabularTemplateDataMapper.setRowId(dto.getRowId());
				tabularTemplateDataMapper.setInputValue(dto.getInputValue());
				tabularTemplateDataMapper.setNumberOfCoulmn(dto.getNumberOfCoulmn());
				tabularTemplateDataMapper.setNumberOfRow(dto.getNumberOfRow());
				tabularTemplateDataMapper.setOrganizationId(dto.getOrganizationId());
				tabularTemplateDataMapper.setUnitId(dto.getUnitId());
				tabularTemplateDataMapper.setStatus(dto.getStatus());
				tabularTemplateDataMapper.setCreatedBy(dto.getCreatedBy());
				tabularTemplateDataMapper.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
				tabularTemplateDataMapper.setUpdatedBy(dto.getUpdatedBy());
				tabularTemplateDataMapper.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
				 
				save(tabularTemplateDataMapper);
				}
	return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
	
	} catch (HibernateException he) {
	logger.error("updateTabularTemplateDataMapper failed", he);
	} catch (Exception e) {
	logger.error("updateTabularTemplateDataMapper failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getTabularTemplateDataMapperById(Integer tabularTemplateId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			List<TabularTemplateDataMapperDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TABULAR_TEMPLATE_DATA_MAPPER_BY_ID")
					.setParameter("tabularTemplateId", tabularTemplateId)
					.setParameter("organizationId", organizationId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(TabularTemplateDataMapperDto.class)).list();
	
		
		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getTabularTemplateDataMapperById failed", he);
		} catch (Exception e) {
		logger.error("getTabularTemplateDataMapperById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	

}
