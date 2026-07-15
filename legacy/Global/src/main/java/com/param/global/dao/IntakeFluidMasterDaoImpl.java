package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.IntakeFluidMaster;
import com.param.global.unit.dto.IntakeFluidMasterDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class IntakeFluidMasterDaoImpl extends GenericDao<IntakeFluidMaster, Integer> implements IIntakeFluidMasterDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(IntakeFluidMasterDaoImpl.class);


	public IntakeFluidMasterDaoImpl() {
		super(IntakeFluidMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response getIntakeFluidMasterList(IntakeFluidMasterDto intakeFluidMasterDto) throws ApplicationException {
		try {
			List<IntakeFluidMasterDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_INTAKE_FLUID_MASTER_LIST")
					.setParameter("organizationId",  intakeFluidMasterDto.getOrganizationId())
//					.setParameter("unitId",  intakeFluidMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(IntakeFluidMasterDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getIntakeFluidMasterList failed", he);
		} catch (Exception e) {
		logger.error("getIntakeFluidMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
