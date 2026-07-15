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
import com.param.global.unit.dto.IntakeFluidTypeMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class IntakeFluidTypeMasterDaoImpl extends GenericDao<IntakeFluidTypeMaster, Integer> implements IIntakeFluidTypeMasterDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(IntakeFluidTypeMasterDaoImpl.class);


	public IntakeFluidTypeMasterDaoImpl() {
		super(IntakeFluidTypeMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	

	@Override
	public Response getIntakeFluidTypeMasterListById(IntakeFluidTypeMasterDto intakeFluidTypeMasterDto)
			throws ApplicationException {
		try {
			List<IntakeFluidTypeMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INTAKE_FLUID_TYPE_MASTER_LIST")
//					.setParameter("organizationId",  intakeFluidTypeMasterDto.getOrganizationId())
//					.setParameter("unitId",  intakeFluidTypeMasterDto.getUnitId())
					.setParameter("intakeFluidId",  intakeFluidTypeMasterDto.getIntakeFluidId())
					.setResultTransformer(Transformers.aliasToBean(IntakeFluidTypeMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getIntakeFluidTypeMasterListById failed", he);
		} catch (Exception e) {
		logger.error("getIntakeFluidTypeMasterListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
