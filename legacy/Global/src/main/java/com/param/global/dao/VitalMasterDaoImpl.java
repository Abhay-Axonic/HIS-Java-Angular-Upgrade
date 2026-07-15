package com.param.global.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.ModalityMasterDto;
import com.param.global.dto.TVitalDetailsDto;
import com.param.global.dto.TVitalDto;
import com.param.global.dto.VitalMasterDto;
import com.param.global.model.VitalMaster;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class VitalMasterDaoImpl extends GenericDao<VitalMaster, Integer> implements IVitalMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(VitalMasterDaoImpl.class);


	public VitalMasterDaoImpl() {
		super(VitalMaster.class);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Response getVitalMasterList(VitalMasterDto vitalMasterDto)throws ApplicationException {
		try {
			List<VitalMasterDto> VitalMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VITAL_MASTER_LIST")
					.setParameter("organizationId", vitalMasterDto.getOrganizationId())
					.setResultTransformer(Transformers.aliasToBean(VitalMasterDto.class)).list();
			return new Response(SUCCESS, null, null, VitalMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getVitalMasterList failed", he);
		} catch (Exception e) {
		logger.error("getVitalMasterList failed", e);
		} 
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getVitalsForNursing(int orgId, int unitId) throws ApplicationException {
		try {
			List<TVitalDetailsDto> VitalMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_VITAL_MASTER_LIST_FOR_NURSING")
					.setResultTransformer(Transformers.aliasToBean(TVitalDetailsDto.class)).list();
			return new Response(SUCCESS, null, null, VitalMasterDtoList, null);

		} catch (HibernateException he) {
		logger.error("getVitalsForNursing failed", he);
		} catch (Exception e) {
		logger.error("getVitalsForNursing failed", e);
		} 
		return new Response(ERROR, null, null, null, null);
	}

}
