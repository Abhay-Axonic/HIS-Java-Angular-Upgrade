package com.param.CSSDMaster.dao;

import org.dozer.DozerBeanMapper;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.CSSDMaster.dto.KitInstrumentMapperDto;
import com.param.CSSDMaster.model.KitInstrumentMapper;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;

@Repository
public class KitEquipmentMapperDaoImpl extends GenericDao<KitInstrumentMapper, Integer>
		implements IKitEquipmentMapperDao, ICommonConstants {

	final static Logger logger = Logger.getLogger(KitEquipmentMapperDaoImpl.class);


	public KitEquipmentMapperDaoImpl() {
		super(KitInstrumentMapper.class);
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private DozerBeanMapper mapper;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response saveMapper(KitInstrumentMapperDto kitEquipmentMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			Session session = sessionFactory.getCurrentSession(); 
			KitInstrumentMapper kitEquipmentMapper = new KitInstrumentMapper();
			
			kitEquipmentMapper.setInstrumentId(kitEquipmentMapperDto.getInstrumentId());
			kitEquipmentMapper.setKitId(kitEquipmentMapperDto.getKitId());
			kitEquipmentMapper.setQuantity(kitEquipmentMapperDto.getQuantity());
			kitEquipmentMapper.setCreated_by(kitEquipmentMapperDto.getCreated_by());
			kitEquipmentMapper.setUpdated_by(kitEquipmentMapperDto.getUpdated_by());
			kitEquipmentMapper.setCreated_date(TimeZoneComponent.getDateByzone(0));
			kitEquipmentMapper.setUpdated_date(TimeZoneComponent.getDateByzone(0));
			session.save(kitEquipmentMapper);
			return new Response(SUCCESS, null, null, null, kitEquipmentMapper);
		} catch (HibernateException he) {
			logger.error("saveMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("saveMapper failed", e);
			throw new ServiceException(e.getMessage());
		} 
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Response updateMapper(KitInstrumentMapperDto kitEquipmentMapperDto) throws ApplicationException {
		// TODO Auto-generated method stub
		try {
			Session session = sessionFactory.getCurrentSession();
			KitInstrumentMapper kitEquipmentMapper = (KitInstrumentMapper) session.load(KitInstrumentMapper.class,
					kitEquipmentMapperDto.getKitInstrumentId());
			kitEquipmentMapper.setUpdated_by(kitEquipmentMapperDto.getUpdated_by());
			kitEquipmentMapper.setUpdated_date(TimeZoneComponent.getDateByzone(0));
			kitEquipmentMapper.setInstrumentId(kitEquipmentMapperDto.getInstrumentId());
			kitEquipmentMapper.setQuantity(kitEquipmentMapperDto.getQuantity());
			session.update(kitEquipmentMapper);

			return new Response(SUCCESS, null, null, null,kitEquipmentMapper);
		} catch (HibernateException he) {
			logger.error("updateMapper failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateMapper failed", e);
			throw new ServiceException(e.getMessage());
		} 		
	}

}
