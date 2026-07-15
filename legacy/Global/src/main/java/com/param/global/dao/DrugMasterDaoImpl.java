package com.param.global.dao;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.DoctorMasterDto;
import com.param.global.dto.DrugMasterDto;
import com.param.global.model.DrugMaster;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class DrugMasterDaoImpl extends GenericDao<DrugMaster, Integer> implements IDrugMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(DrugMasterDaoImpl.class);


	
	public DrugMasterDaoImpl() {
		super(DrugMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getDrugMasterList(DrugMasterDto drugMasterDto)throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<DrugMasterDto> listDrugMasterDto = statlessSession.getNamedQuery("GET_DRUG_MASTER_LIST")
					.setParameter("unitId", drugMasterDto.getUnitId())
					.setParameter("orgId", drugMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(DrugMasterDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listDrugMasterDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getDrugMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

}
