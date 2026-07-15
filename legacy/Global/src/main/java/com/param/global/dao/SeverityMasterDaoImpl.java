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
import com.param.global.dto.AllergyTypeMasterDto;
import com.param.global.dto.SeverityMasterDto;
import com.param.global.model.SeverityMaster;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SeverityMasterDaoImpl extends GenericDao<SeverityMaster, Integer> implements ISeverityMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(SeverityMasterDaoImpl.class);


	
	public SeverityMasterDaoImpl() {
		super(SeverityMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getListOfSeverityMaster(SeverityMasterDto severityMasterDto)
			throws ApplicationException {
		try
		{
			StatelessSession statlessSession = sessionFactory.openStatelessSession();
			List<SeverityMasterDto> listSeverityMasterDto = statlessSession.getNamedQuery("GET_LIST_SEVERITY_MASTER")
//					.setInteger("organizationId", severityMasterDto.getOrganizationId())
//					.setInteger("unitId", severityMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SeverityMasterDto.class)).list();
			statlessSession.close();
			return new Response(SUCCESS, SUCCESS_CODE, null, listSeverityMasterDto, null);
		}catch(HibernateException he) { 
			throw new HibernateException(he.getMessage());
		}catch(Exception e) {
		logger.error("getListOfSeverityMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, SERVER_ERROR, null, null);
	}

}
