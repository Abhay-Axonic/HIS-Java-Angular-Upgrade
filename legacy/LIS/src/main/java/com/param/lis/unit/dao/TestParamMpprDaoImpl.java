package com.param.lis.unit.dao;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import com.param.entity.lis.unit.TestParamMppr;
import com.param.lis.global.common.ICommonConstants;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
public class TestParamMpprDaoImpl extends GenericDao<TestParamMppr, Integer>
implements ITestParamMpprDao, ICommonConstants, IError, ITransactionConstants
{

	public TestParamMpprDaoImpl() {
		super(TestParamMppr.class);
	}
	

	final static Logger logger = Logger.getLogger(TestParamMpprDaoImpl.class);

	@Override
	public TestParamMppr addTestParamMppr(TestParamMppr testParamMppr) throws ApplicationException
	{
		try
		{
				return save(testParamMppr);
		} catch (HibernateException er)
		{
			logger.error("Exception", er);throw er;
		} catch (Exception e)
		{
			logger.error("Exception", e);throw e;
		}
	}

	@Override
	public TestParamMppr updateTestParamMppr(TestParamMppr testParamMppr) throws ApplicationException
	{
		try
		{
				return update(testParamMppr);
		} catch (HibernateException er)
		{
			logger.error("Exception", er);throw er;
		} catch (Exception e)
		{
			logger.error("Exception", e);throw e;
		}
	}
	
}
