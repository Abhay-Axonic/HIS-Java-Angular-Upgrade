package com.param.global.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.RoomModalityMasterDto;
import com.param.global.model.RoomModalityMaster;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RoomModalityMasterDaoImpl extends GenericDao<RoomModalityMaster, Integer>implements IRoomModalityMasterDao, ICommonConstants {
	final static Logger logger = Logger.getLogger(RoomModalityMasterDaoImpl.class);


	public RoomModalityMasterDaoImpl() {
		super(RoomModalityMaster.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveRoomModalityMasterMaster(RoomModalityMasterDto dto) throws ApplicationException {
		try {
			RoomModalityMaster master =new RoomModalityMaster();
			master.setCreatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()),"dd-MM-yyyy HH-mm-ss"));
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()),"dd-MM-yyyy HH-mm-ss"));
			master.setCreatedBy(dto.getCreatedBy());
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setUnitId(dto.getUnitId());
			master.setOrganizationId(dto.getOrganizationId());
			master.setModalityId(dto.getModalityId());
			master.setRoomModalityId(dto.getRoomModalityId());
			master.setSpecialityId(dto.getSpecialityId());
			master.setSubSpecialityId(dto.getSubSpecialityId());
			master.setStatus('A');
			sessionFactory.getCurrentSession().save(master);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		logger.error("saveRoomModalityMasterMaster failed", he);
		} catch (Exception e) {
		logger.error("saveRoomModalityMasterMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getRoomModalityById(int id, int orgId, int unitId) throws ApplicationException {
		try {
			List<RoomModalityMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_ROOM_MODALITY_BY_ID")
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setParameter("id", id)
					.setResultTransformer(Transformers.aliasToBean(RoomModalityMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		} catch (HibernateException he) {
		logger.error("getRoomModalityById failed", he);
		} catch (Exception e) {
		logger.error("getRoomModalityById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getRoomModalityList(RoomModalityMasterDto dto) throws ApplicationException {
		try {
			List<RoomModalityMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_ROOM_MODALITY_LIST")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setFirstResult(dto.getOffset() != null ? dto.getOffset() : 0)
					.setMaxResults(dto.getNoOfRecordsPerPage() != null ? dto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(RoomModalityMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		} catch (HibernateException he) {
		logger.error("getRoomModalityList failed", he);
		} catch (Exception e) {
		logger.error("getRoomModalityList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateRoomModalityMaster(RoomModalityMasterDto dto) throws ApplicationException {
		try {
			RoomModalityMaster master=findById(dto.getRoomModalityMasterId());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()),"dd-MM-yyyy HH-mm-ss"));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setModalityId(dto.getModalityId());
			master.setRoomModalityId(dto.getRoomModalityId());
			master.setSpecialityId(dto.getSpecialityId());
			master.setSubSpecialityId(dto.getSubSpecialityId());
			sessionFactory.getCurrentSession().update(master);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateRoomModalityMaster failed", he);
		} catch (Exception e) {
		logger.error("updateRoomModalityMaster failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateRoomModalityMasterStatus(RoomModalityMasterDto dto) throws ApplicationException {
		try {
			RoomModalityMaster master=findById(dto.getRoomModalityMasterId());
			master.setUpdatedDate(GlobalCommonDateUtils.getDate(TimeZoneComponent.getDateByzone(dto.getUnitId()),"dd-MM-yyyy HH-mm-ss"));
			master.setUpdatedBy(dto.getUpdatedBy());
			master.setStatus(dto.getStatus()!=null ? dto.getStatus(): 'I' );
			sessionFactory.getCurrentSession().update(master);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		logger.error("updateRoomModalityMasterStatus failed", he);
		} catch (Exception e) {
		logger.error("updateRoomModalityMasterStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateRoomModalityMasterCount(RoomModalityMasterDto dto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(RoomModalityMaster.class);
			c.add(Restrictions.eq("organizationId", dto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", dto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		logger.error("updateRoomModalityMasterCount failed", he);
		} catch (Exception e) {
		logger.error("updateRoomModalityMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response checkAlredyExistOrNot(RoomModalityMasterDto dto) throws ApplicationException {
		try {
			List<RoomModalityMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("CHECK_ALREDY_EXIST_OR_NOT")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("roomModalityId", dto.getRoomModalityId())
					.setParameter("subSpecialityId", dto.getSubSpecialityId())
					.setParameter("modalityId", dto.getModalityId())
					.setResultTransformer(Transformers.aliasToBean(RoomModalityMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		} catch (HibernateException he) {
		logger.error("checkAlredyExistOrNot failed", he);
		} catch (Exception e) {
		logger.error("checkAlredyExistOrNot failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response checkNotById(RoomModalityMasterDto dto) throws ApplicationException {
		try {
			List<RoomModalityMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("CHECK_NOT_BY_ID")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("roomModalityId", dto.getRoomModalityId())
					.setParameter("subSpecialityId", dto.getSubSpecialityId())
					.setParameter("modalityId", dto.getModalityId())
					.setParameter("roomModalityMasterId", dto.getRoomModalityMasterId())
					.setResultTransformer(Transformers.aliasToBean(RoomModalityMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		} catch (HibernateException he) {
		logger.error("checkNotById failed", he);
		} catch (Exception e) {
		logger.error("checkNotById failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getModalityDetailsByModalityId(RoomModalityMasterDto dto) throws ApplicationException {
		try {
			List<RoomModalityMasterDto> dtoList=sessionFactory.getCurrentSession().getNamedQuery("GET_MODALITY_DETAILS_BY_MODALITY_ID")
					.setParameter("orgId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())					
					.setParameter("modalityId", dto.getModalityId())
					.setResultTransformer(Transformers.aliasToBean(RoomModalityMasterDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, dtoList, null);
		} catch (HibernateException he) {
		logger.error("getModalityDetailsByModalityId failed", he);
		} catch (Exception e) {
		logger.error("getModalityDetailsByModalityId failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

}
