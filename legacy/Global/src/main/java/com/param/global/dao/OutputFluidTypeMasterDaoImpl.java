package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.IntakeFluidTypeMaster;
import com.param.global.model.OutputTypeMaster;
import com.param.global.unit.dto.IntakeFluidTypeMasterDto;
import com.param.global.unit.dto.OutputTypeMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class OutputFluidTypeMasterDaoImpl extends GenericDao<OutputTypeMaster, Integer> implements IOutputFluidTypeMasterDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(OutputFluidTypeMasterDaoImpl.class);


	public OutputFluidTypeMasterDaoImpl() {
		super(OutputTypeMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getOutputFluidMasterList(OutputTypeMasterDto outputTypeMasterDto) throws ApplicationException {
		try {
			List<OutputTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OUTPUT_TYPE_MASTER_LIST")
					.setParameter("organizationId",  outputTypeMasterDto.getOrganizationId())
					.setParameter("unitId",  outputTypeMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(OutputTypeMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getOutputFluidMasterList failed", he);
		} catch (Exception e) {
		logger.error("getOutputFluidMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	

	
}
