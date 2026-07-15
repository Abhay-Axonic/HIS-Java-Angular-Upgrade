package com.param.global.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import in.param.exception.ApplicationException;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.dto.TemplateResponseDto;
import com.param.global.model.TemplateQuetionaryPrintMapper;
import com.param.global.model.TemplateQuetionaryPrintMapper;
import com.param.global.model.TemplateTransaction;
import com.param.global.service.TemplateQuetionaryPrintDto;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class TemplateQuetionaryPrintMapperDaoImpl extends GenericDao<TemplateQuetionaryPrintMapper, Integer> implements ITemplateQuetionaryPrintMapperDao, ICommonConstants,IError{
	final static Logger logger = Logger.getLogger(TemplateQuetionaryPrintMapperDaoImpl.class);


	public TemplateQuetionaryPrintMapperDaoImpl() {
		super(TemplateQuetionaryPrintMapper.class);
	}

	@Override
	public Response saveTemplateQuetionaryPrintData(TemplateQuetionaryPrintDto dto1) throws ApplicationException {
		try {
				TemplateQuetionaryPrintMapper templateQuetionaryPrintMapper=new TemplateQuetionaryPrintMapper();
				templateQuetionaryPrintMapper.setAllowToPrint(dto1.getAllowToPrint());
				templateQuetionaryPrintMapper.setAnswer(dto1.getAnswer());
				templateQuetionaryPrintMapper.setQueTitle(dto1.getQueTitle());
				templateQuetionaryPrintMapper.setQueType(dto1.getQueType());
				templateQuetionaryPrintMapper.setQueuId(dto1.getQueuId());
				templateQuetionaryPrintMapper.setSectionTitle(dto1.getSectionTitle());
				templateQuetionaryPrintMapper.setSlugValue(dto1.getSlugValue());
				templateQuetionaryPrintMapper.setSlugValueNew(dto1.getSlugValueNew());
				templateQuetionaryPrintMapper.setSummaryForPrint(dto1.getSummaryForPrint());
				templateQuetionaryPrintMapper.setTemplateTransactionId(dto1.getTemplateTransactionId());
				templateQuetionaryPrintMapper.setStatus('A');
				templateQuetionaryPrintMapper.setUnitId(dto1.getUnitId());
				templateQuetionaryPrintMapper.setOrganizationId(dto1.getOrganizationId());
				templateQuetionaryPrintMapper.setAddedBy(dto1.getAddedBy());
				templateQuetionaryPrintMapper.setAddedDate(TimeZoneComponent.getDateByzone(dto1.getUnitId()));
				templateQuetionaryPrintMapper.setUpdatedBy(dto1.getUpdatedBy());
				templateQuetionaryPrintMapper.setUpdatedDate(TimeZoneComponent.getDateByzone(dto1.getUnitId()));
				save(templateQuetionaryPrintMapper);
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto1);
		
		} catch (HibernateException he) {
		logger.error("saveTemplateQuetionaryPrintData failed", he);
		} catch (Exception e) {
		logger.error("saveTemplateQuetionaryPrintData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTemplateTransactionData(TemplateResponseDto dto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			String query = "UPDATE public.t_template_transaction " +
					"SET status = :status " +
					"WHERE conversation_id = :conversationId";

			int rowsUpdated = session.createSQLQuery(query)
					.setParameter("status", "I")
					.setParameter("conversationId", dto.getConversationId())
					.executeUpdate();

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, dto);
		
		} catch (HibernateException he) {
		logger.error("updateTemplateTransactionData failed", he);
		} catch (Exception e) {
		logger.error("updateTemplateTransactionData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTemplateQuetionaryPrintData(Integer tempTranId) throws ApplicationException {
		try {
			if(tempTranId != null || tempTranId != 0) {
				Session session = sessionFactory.getCurrentSession();
				String query = "UPDATE public.t_template_quetionary_print_mapper " +
						"SET status = :status " +
						"WHERE template_transaction_id = :templateTransactionId";

				int rowsUpdated = session.createSQLQuery(query)
						.setParameter("status", "I")
						.setParameter("templateTransactionId", tempTranId)
						.executeUpdate();

			}
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		
		} catch (HibernateException he) {
		logger.error("updateTemplateQuetionaryPrintData failed", he);
		} catch (Exception e) {
		logger.error("updateTemplateQuetionaryPrintData failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
