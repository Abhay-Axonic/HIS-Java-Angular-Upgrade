package com.param.global.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.model.ServiceTarrifMaster;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.dto.VariableFactorDto;
import com.param.global.dto.VariablePricingDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class ServiceTarrifMasterDaoImpl extends GenericDao<ServiceTarrifMaster, Integer> implements IServiceTarrifMasterDao, ICommonConstants, IError{
	final static Logger logger = Logger.getLogger(ServiceTarrifMasterDaoImpl.class);


	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DozerBeanMapper mapper;
	
	public ServiceTarrifMasterDaoImpl() {
		super(ServiceTarrifMaster.class);
	}

	@Override
	public Response saveServiceTarrifMaster(VariablePricingDto variablePricingDto) 
			throws ApplicationException {
		try {
			
			updateServiceTarrifMaster(variablePricingDto);
			
			variablePricingDto.setServiceTarrifMasterId(0);
			ServiceTarrifMaster serviceTarrifMaster = mapper.map(variablePricingDto, ServiceTarrifMaster.class,
					"VariablePricingDto_to_ServiceTarrifMaster");
			serviceTarrifMaster.setStatus('A');
			serviceTarrifMaster = save(serviceTarrifMaster);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, serviceTarrifMaster);
		} catch (HibernateException he) {
			logger.error("saveServiceTarrifMaster failed", he);
		} catch (Exception e) {
			logger.error("saveServiceTarrifMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getServiceTarrifMaster(VariablePricingDto variablePricingDto)
			throws ApplicationException {
		try{
			List<VariablePricingDto> listVariablePricingDto = sessionFactory.getCurrentSession().
					getNamedQuery("GET_VARIABLE_PRICING_FACTOR_BY_VISITTYPE")
					.setParameter("organisationId", variablePricingDto.getOrganisationId())
					.setParameter("unitId", variablePricingDto.getUnitId())
					.setParameter("visitTypeId", variablePricingDto.getVisitTypeId())
					.setResultTransformer(Transformers.aliasToBean(VariablePricingDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listVariablePricingDto, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response updateServiceTarrifMaster(VariablePricingDto variablePricingDto) 
			throws ApplicationException {
		try{
			int updatedRowCount = sessionFactory.getCurrentSession().
					getNamedQuery("UPDATE_VARIABLE_PRICING_FACTOR_STATUS_BY_VISITTYPE")
					.setParameter("organisationId", variablePricingDto.getOrganisationId())
					.setParameter("unitId", variablePricingDto.getUnitId())
					.setParameter("visitTypeId", variablePricingDto.getVisitTypeId()).executeUpdate();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	public Response getVariableFactorDetailsByVisitType(
			VariablePricingDto variablePricingDto) throws ApplicationException {
		try{
			List<VariableFactorDto> listVariableFactorDto = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VIEW_VARIABLE_FACTOR_FOR_ALL_TYPES")
					.setParameter("organisationId", variablePricingDto.getOrganisationId())
					.setParameter("unitId", variablePricingDto.getUnitId())
					.setParameter("visitTypeId", variablePricingDto.getVisitTypeId())
					.setResultTransformer(Transformers.aliasToBean(VariableFactorDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listVariableFactorDto, null);
		}catch(HibernateException he){
			throw he;
		}catch(Exception e){
			
			throw e;
		}
	}
	

}
