package com.param.CSSD.dao;

import org.dozer.DozerBeanMapper;
import org.hibernate.StatelessSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.CSSD.model.KitMovementRecord;
import com.param.global.common.ICommonConstants;

import in.param.common.dao.GenericDao;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class KitMovementRecordDaoImpl extends GenericDao<KitMovementRecord, Integer>
implements IKitMovementRecordDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(KitMovementRecordDaoImpl.class);

	
	public KitMovementRecordDaoImpl() {
		super(KitMovementRecord.class);
		// TODO Auto-generated constructor stub
	}
	
	@SuppressWarnings("unused")
	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public void createKitMovementRecord(KitMovementRecord kitMovementRecord) {
		try {
//			StatelessSession statelessSession = sessionFactory.openStatelessSession();

				} catch (Exception e) {
					logger.error("createKitMovementRecord failed", e);
					throw new ServiceException(e.getMessage());
		}
		
	}

}


