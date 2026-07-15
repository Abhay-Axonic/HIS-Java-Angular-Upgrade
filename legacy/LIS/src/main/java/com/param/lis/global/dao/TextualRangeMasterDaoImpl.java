package com.param.lis.global.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.global.TextualRangeMaster;
import com.param.global.common.ICommonConstants;
import com.param.lis.global.common.Response;
import com.param.lis.global.dto.TextualRangeMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class TextualRangeMasterDaoImpl extends GenericDao<TextualRangeMaster, Integer>implements ITextualRangeMasterDao, ICommonConstants{

	public TextualRangeMasterDaoImpl() {
		super(TextualRangeMaster.class);
	
	}
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveTextualRangeMaster(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			TextualRangeMaster textualRangeMaster=new TextualRangeMaster();
			textualRangeMaster.setTextualRangeCode(textualRangeMasterDto.getTextualRangeCode());
			textualRangeMaster.setTextualRangeName(textualRangeMasterDto.getTextualRangeName());
			textualRangeMaster.setOrgId(textualRangeMasterDto.getOrgId());
			textualRangeMaster.setOrgUnitId(textualRangeMasterDto.getOrgUnitId());
			textualRangeMaster.setCreatedBy(textualRangeMasterDto.getCreatedBy());
			textualRangeMaster.setCreatedDate(TimeZoneComponent.getDateByzone(textualRangeMasterDto.getOrgUnitId()).getTime());
			textualRangeMaster.setUpdatedBy(textualRangeMasterDto.getUpdatedBy());
			textualRangeMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(textualRangeMasterDto.getOrgUnitId()).getTime());
			textualRangeMaster.setStatus(textualRangeMasterDto.getStatus());
			save(textualRangeMaster);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);	
			
		}catch (HibernateException he) {
			throw he;
		}catch (Exception e) {
				throw e;
		}
	}
	

	@Override
	public Response updateTextualRangeMaster(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			
			TextualRangeMaster textualRangeMaster=findById(textualRangeMasterDto.getTextualRangeId());
			textualRangeMaster.setTextualRangeCode(textualRangeMasterDto.getTextualRangeCode());
			textualRangeMaster.setTextualRangeName(textualRangeMasterDto.getTextualRangeName());
			textualRangeMaster.setOrgId(textualRangeMasterDto.getOrgId());
			textualRangeMaster.setOrgUnitId(textualRangeMasterDto.getOrgUnitId());
			textualRangeMaster.setUpdatedBy(textualRangeMasterDto.getUpdatedBy());
			textualRangeMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(textualRangeMasterDto.getOrgUnitId()).getTime());
			update(textualRangeMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);	
			
		}catch (HibernateException he) {
			throw he;
		}catch (Exception e) {
				throw e;
		}
	}
	
	
	@Override
	public Response updateTextualRangeMasterStatus(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			TextualRangeMaster textualRangeMaster=findById(textualRangeMasterDto.getTextualRangeId());
			textualRangeMaster.setUpdatedBy(textualRangeMasterDto.getUpdatedBy());
			textualRangeMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(textualRangeMasterDto.getOrgUnitId()!=null ? textualRangeMasterDto.getOrgUnitId():null).getTime());
			textualRangeMaster.setStatus(textualRangeMasterDto.getStatus());

			update(textualRangeMaster);
			
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);	
		}catch (HibernateException he) {
			throw he;
		}catch (Exception e) {
				throw e;
		}
	}
	
	
	@Override
	public Response getTextualRangeMasterById(Integer textualRangeId, Integer orgId, Integer orgUnitId) throws ApplicationException{

	 try{
			List<TextualRangeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEXTUAL_RANGE_MASTER_BY_ID")
					.setParameter("textualRangeId", textualRangeId)
					.setParameter("orgId", orgId)
					//.setParameter("orgUnitId", orgUnitId) //dilip
					.setResultTransformer(Transformers.aliasToBean(TextualRangeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);	
		}catch (HibernateException he) {
			throw he;
		}catch (Exception e) {
				throw e;
		}
	}

	
	@Override
	public Response getTextualRangeMasterList(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException {
		try{
			List<TextualRangeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEXTUAL_RANGE_MASTER_LIST")
					.setParameter("orgId", textualRangeMasterDto.getOrgId())
					//.setParameter("orgUnitId", textualRangeMasterDto.getOrgUnitId()) //dilip
					.setFirstResult(textualRangeMasterDto.getOffset() != null ? textualRangeMasterDto.getOffset() : 0)
					.setMaxResults(textualRangeMasterDto.getNoOfRecordPerPage() != null ? textualRangeMasterDto.getNoOfRecordPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(TextualRangeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);	
		}catch (HibernateException he) {
			throw he;
		}catch (Exception e) {
				throw e;
		}
	}

	
	@Override
	public Response getActiveTextualRangeMasterList(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException {
		try{
			List<TextualRangeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_TEXTUAL_RANGE_MASTER_LIST")
					.setParameter("orgId", textualRangeMasterDto.getOrgId())
					//.setParameter("orgUnitId", textualRangeMasterDto.getOrgUnitId()) //dilip
					.setResultTransformer(Transformers.aliasToBean(TextualRangeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);	
		}catch (HibernateException he) {
			throw he;
		}catch (Exception e) {
				throw e;
		}
	}
	

	@Override
	public Response getTextualRangeMasterCount(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			Session session = sessionFactory.getCurrentSession();
			Criteria crit = session.createCriteria(TextualRangeMaster.class);
			crit.add(Restrictions.eq("orgId", textualRangeMasterDto.getOrgId()));
			crit.setProjection(Projections.rowCount());
			Long count = (Long) crit.uniqueResult();
			return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			
		}catch (HibernateException he) {
			throw he;
		}catch (Exception e) {
				throw e;
		}
	}
	

	@Override
	public Response getTextualRangeMasterByName(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			List<TextualRangeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEXTUAL_RANGE_MASTER_BY_NAME")
					.setParameter("orgId", textualRangeMasterDto.getOrgId())
					.setParameter("textualRangeName", textualRangeMasterDto.getTextualRangeName().trim().toLowerCase())
					//.setString("code", textualRangeMasterDto.getTextualRangeCode().trim().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(TextualRangeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);	
		}catch (HibernateException he) {
			throw he;
		}catch (Exception e) {
				throw e;
		}
	}
	
	
	@Override
	public Response getTextualRangeMasterByNameNotId(TextualRangeMasterDto textualRangeMasterDto) throws ApplicationException{
		try{
			List<TextualRangeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEXTUAL_RANGE_MASTER_BY_NAME_NOT_ID")
					.setParameter("orgId", textualRangeMasterDto.getOrgId())
					.setParameter("textualRangeId", textualRangeMasterDto.getTextualRangeId())
					.setString("textualRangeName", textualRangeMasterDto.getTextualRangeName().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(TextualRangeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);	
		}catch (HibernateException he) {
			throw he;
		}catch (Exception e) {
				throw e;
		}
	}


	@Override
	public Response getTextualRangeMasterByCode(TextualRangeMasterDto textualRangeMasterDto)
			throws ApplicationException {
		try{
			List<TextualRangeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEXTUAL_RANGE_MASTER_BY_CODE")
					.setParameter("orgId", textualRangeMasterDto.getOrgId())
					.setParameter("code", textualRangeMasterDto.getTextualRangeCode().trim().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(TextualRangeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);	
		}catch (HibernateException he) {
			throw he;
		}catch (Exception e) {
				throw e;
		}
	}


	@Override
	public Response getTextualRangeMasterByCodeNotId(TextualRangeMasterDto textualRangeMasterDto)
			throws ApplicationException {
		try{
			List<TextualRangeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TEXTUAL_RANGE_MASTER_BY_CODE_NOT_ID")
					.setParameter("orgId", textualRangeMasterDto.getOrgId())
					.setParameter("textualRangeId", textualRangeMasterDto.getTextualRangeId())
					.setParameter("textualRangeCode", textualRangeMasterDto.getTextualRangeCode().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(TextualRangeMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);	
		}catch (HibernateException he) {
			throw he;
		}catch (Exception e) {
				throw e;
		}
	}
}
