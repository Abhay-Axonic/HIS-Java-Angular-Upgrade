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
import com.param.global.master.dto.WeekDaysMasterDto;
import com.param.global.master.model.WeekDaysMaster;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class WeekDayMasterDaoImpl extends GenericDao<WeekDaysMaster, Integer>
		implements IWeekDaysMasterDao, ICommonConstants {

	private static final Logger logger = Logger.getLogger(WeekDayMasterDaoImpl.class);

	public WeekDayMasterDaoImpl() {
		super(WeekDaysMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	DozerBeanMapper mapper;

	@Override
	public Response getAllWeekDaysMaster() throws ApplicationException {
		try {
			Query query = sessionFactory.getCurrentSession().getNamedQuery("WEEK_DAYS_MASTER.findAll")
					.setResultTransformer(Transformers.aliasToBean(WeekDaysMasterDto.class));
			List<WeekDaysMasterDto> weekDaysMasterDtoList = query.list();
			return new Response(SUCCESS, null, null, weekDaysMasterDtoList, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
