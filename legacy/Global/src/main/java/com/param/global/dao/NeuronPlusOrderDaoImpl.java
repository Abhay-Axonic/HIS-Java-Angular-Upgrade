package com.param.global.dao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.OrderMasterDto;
import com.param.global.model.NeuronplusDataSavePhlebotomy;
import com.param.global.model.OrderMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class NeuronPlusOrderDaoImpl extends GenericDao<NeuronplusDataSavePhlebotomy, Integer> implements INeuronPlusOrderDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(NeuronPlusOrderDaoImpl.class);

	public NeuronPlusOrderDaoImpl() {
		super(NeuronplusDataSavePhlebotomy.class);
	}

	@Override
	public Response saveNeuronPlusorderedData(OrderMasterDto dto) {
		try {
			NeuronplusDataSavePhlebotomy neuronplusDataSavePhlebotomy = new  NeuronplusDataSavePhlebotomy();
			 neuronplusDataSavePhlebotomy.setOrganizationId(dto.getOrgId());
			 neuronplusDataSavePhlebotomy.setOrgUnitId(dto.getOrgUnitId());
			 neuronplusDataSavePhlebotomy.setNeuronPlusUrl(dto.getNeuronPlusUrl());
			 neuronplusDataSavePhlebotomy.setOrderId(dto.getOrderId());	 sessionFactory.getCurrentSession().save(neuronplusDataSavePhlebotomy);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		}catch (Exception e) {
			logger.error("Error while saving Neuron Plus ordered data", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE,COMMON_ERROR_MESSAGE, null, null);
	}
}
