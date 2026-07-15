package com.param.CSSD.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.CSSD.dto.SterilizedBatchDto;
import com.param.CSSD.model.LoadBatch;
import com.param.CSSD.model.SterilizedBatch;

import in.param.common.dao.GenericDao;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class SterilizedBatchDaoImpl extends GenericDao<SterilizedBatch, Integer>
		implements ISterilizedBatchDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(SterilizedBatchDaoImpl.class);


	public SterilizedBatchDaoImpl() {
		super(SterilizedBatch.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;


	@Override
	public Response transferToSterilizeZone(SterilizedBatchDto sterilizedBatchDto ) {
		try {
			
			Session session=sessionFactory.getCurrentSession();
			
			SterilizedBatch sterilizedBatch = mapper.map(sterilizedBatchDto, SterilizedBatch.class,
					"SterilizedBatchDto_to_SterilizedBatch");
			session.save(sterilizedBatch);
			LoadBatch loadBatch=(LoadBatch)session.get(LoadBatch.class, sterilizedBatchDto.getBatchNumber());
			if(!loadBatch.getBiologicalIndicator())
			{
				loadBatch.setStatus("sterilized");
				session.update(loadBatch);
			}
			
			return new Response(SUCCESS, "200","Batch transferred to sterilize zone.", null, null);
		} catch (Exception e) {
			logger.error("transferToSterilizeZone failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
	@Override
	public Response getIndicatorsByBatchNo(Integer batchNo) {
		try {
			StatelessSession statelessSession = sessionFactory.openStatelessSession();

			List<SterilizedBatchDto> sterilizedBatchDtoList = statelessSession
					.getNamedQuery("GET_INDICATORS_BY_BATCH_NO")
					.setParameter("batchNo", batchNo)
					.setResultTransformer(Transformers.aliasToBean(SterilizedBatchDto.class)).list();
			statelessSession.close();
			return new Response(SUCCESS, null,null, sterilizedBatchDtoList, null);
		} catch (Exception e) {
			logger.error("getIndicatorsByBatchNo failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
