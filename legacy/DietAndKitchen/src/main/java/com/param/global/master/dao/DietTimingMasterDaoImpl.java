package com.param.global.master.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.master.dto.DietTimingMasterDto;
import com.param.global.master.model.DietTimingMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class DietTimingMasterDaoImpl extends GenericDao<DietTimingMaster, Integer>
		implements IDietTimingMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(DietTimingMasterDaoImpl.class);

	public DietTimingMasterDaoImpl() {
		super(DietTimingMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	@Override
	public Response getAllDietTimingMaster() throws ApplicationException {
		try {
			Query query = sessionFactory.getCurrentSession().getNamedQuery("DIET_TIMING_MASTER.findAll")
					.setResultTransformer(Transformers.aliasToBean(DietTimingMasterDto.class));
			List<DietTimingMasterDto> dietTimingMasterDtoList = query.list();
			return new Response(SUCCESS, null, null, dietTimingMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
