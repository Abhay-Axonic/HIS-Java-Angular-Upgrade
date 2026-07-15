package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.config.model.VariablePricingSequenceMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.VariablePricingSequenceDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"unchecked","rawtypes"})
public class VariablePricingSequenceDaoImpl extends GenericDao<VariablePricingSequenceMaster, Integer> implements IVariablePricingSequenceDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(VariablePricingSequenceDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DozerBeanMapper mapper;
	
	public VariablePricingSequenceDaoImpl(){
		super(VariablePricingSequenceMaster.class);
	}

	@Override
	public Response saveVariablePricingSequenceMaster(
			VariablePricingSequenceDto variablePricingSequenceDto)
			throws ApplicationException {
		
		try {
			VariablePricingSequenceMaster variablePricingSequenceMaster = mapper.map(variablePricingSequenceDto, VariablePricingSequenceMaster.class,
					"variablePricingSequenceDto_to_variablePricingSequenceMaster");
			variablePricingSequenceMaster.setStatus('A');
			variablePricingSequenceMaster = save(variablePricingSequenceMaster);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, variablePricingSequenceMaster);
		} catch (HibernateException he) {
			logger.error("saveVariablePricingSequenceMaster failed", he);
		} catch (Exception e) {
			logger.error("saveVariablePricingSequenceMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getVariablePricingSequenceByOrganisationId(
			VariablePricingSequenceDto variablePricingSequenceDto) throws ApplicationException {
		
		List<VariablePricingSequenceDto> listVariablePricingDto = sessionFactory.getCurrentSession().
				getNamedQuery("GET_VARIABLE_PRICING_SEQ_BY_ORG_ID")
				.setParameter("orgId", variablePricingSequenceDto.getOrgId())
				.setParameter("unitId", variablePricingSequenceDto.getUnitId())
				.setParameter("visitType", variablePricingSequenceDto.getVisitType())
				.setResultTransformer(Transformers.aliasToBean(VariablePricingSequenceDto.class)).list();
		
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listVariablePricingDto, null);
	}
	
}
