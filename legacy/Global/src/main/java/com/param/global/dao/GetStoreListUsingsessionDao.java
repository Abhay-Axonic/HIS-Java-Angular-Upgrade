package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.entity.model.master.UserStoreMapper;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.getStoreListBySessionUserId.StoreResponseList;

import in.param.common.dao.GenericDao;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GetStoreListUsingsessionDao extends GenericDao<UserStoreMapper, Integer> implements IGetStoreListUsingsessionDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(GetStoreListUsingsessionDao.class);


	public GetStoreListUsingsessionDao()
	{
		super(UserStoreMapper.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Response getStoreListUsingSessionByUserId(Integer userId,Integer unitId) {
		try {
			List<StoreResponseList> stores = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_STORE_LIST_BY_ID")
					.setParameter("userId", userId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(StoreResponseList.class)).list();
			return new Response(SUCCESS,stores);

		} catch (HibernateException he) {
		logger.error("getStoreListUsingSessionByUserId failed", he);
		} catch (Exception e) {
		logger.error("getStoreListUsingSessionByUserId failed", e);
		}
		return new Response(ERROR,null);
	}

}
