package com.param.global.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.model.ServiceTarrifBedtypeManager;
import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TarrifBedCategoryMpprDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
public class TarrifBedCategoryDaoImpl extends GenericDao<ServiceTarrifBedtypeManager, Integer> implements ITarrifBedCategoryDao, ICommonConstants, IError {

	private static final Logger logger = Logger.getLogger(TarrifBedCategoryDaoImpl.class);

	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private DozerBeanMapper mapper;
	
	public TarrifBedCategoryDaoImpl() {
		super(ServiceTarrifBedtypeManager.class);
	}

	@Override
	public Response saveServiceTarrifMaster(TarrifBedCategoryMpprDto tarrifBedCategoryMpprDto)
			throws ApplicationException {
		
		try {
			tarrifBedCategoryMpprDto.setTarrifBedTypeManagerId(null);
			ServiceTarrifBedtypeManager serviceTarrifBedtypeManager = mapper.map(tarrifBedCategoryMpprDto, ServiceTarrifBedtypeManager.class,
					"TarrifBedCategoryMpprDto_to_serviceTarrifBedtypeManager");
			serviceTarrifBedtypeManager.setStatus('A');
			serviceTarrifBedtypeManager = save(serviceTarrifBedtypeManager);

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, serviceTarrifBedtypeManager);
		} catch (HibernateException he) {
			logger.error("saveServiceTarrifMaster failed", he);
		} catch (Exception e) {
			logger.error("saveServiceTarrifMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response saveListServiceTarrifMaster(List<TarrifBedCategoryMpprDto> listTarrifBedCategoryMpprDto)
			throws ApplicationException {
		try {

			for(TarrifBedCategoryMpprDto bedCategoryMpprDto : listTarrifBedCategoryMpprDto){
				ServiceTarrifBedtypeManager serviceTarrifBedtypeManager = mapper.map(bedCategoryMpprDto, ServiceTarrifBedtypeManager.class,
						"TarrifBedCategoryMpprDto_to_serviceTarrifBedtypeManager");
				serviceTarrifBedtypeManager.setStatus('A');
				serviceTarrifBedtypeManager = save(serviceTarrifBedtypeManager);
			}

			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
			logger.error("saveListServiceTarrifMaster failed", he);
		} catch (Exception e) {
			logger.error("saveListServiceTarrifMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response getBedCategoryFactorByTarrifId(Integer serviceTarrifId)
			throws ApplicationException {
		try{
			List<TarrifBedCategoryMpprDto> listBedCategoryMpprDto = sessionFactory.getCurrentSession().
				getNamedQuery("GET_ACTIVE_BET_TYPE_FACTOR_BY_TARRIF_ID")
				.setInteger("serviceTarrifId", serviceTarrifId)
				.setResultTransformer(Transformers.aliasToBean(TarrifBedCategoryMpprDto.class)).list();
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, listBedCategoryMpprDto, null);
		
		}catch(HibernateException he){
			throw he;
		}catch (Exception e) {
			throw e;
		}
		
	}

}
