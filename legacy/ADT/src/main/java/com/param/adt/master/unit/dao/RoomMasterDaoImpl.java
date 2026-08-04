package com.param.adt.master.unit.dao;

import java.util.List;

import org.dozer.DozerBeanMapper;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.adt.master.unit.dto.RoomMasterDto;
import com.param.adt.master.unit.model.RoomMaster;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import com.param.global.service.VerifiedIdentityService;
@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class RoomMasterDaoImpl extends GenericDao<RoomMaster, Integer> implements IRoomMasterDao, ICommonConstants {

	@Autowired
	private VerifiedIdentityService verifiedIdentityService;

	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	private DozerBeanMapper mapper;

	public RoomMasterDaoImpl() {
		super(RoomMaster.class);
	}

	@Override
	public Response saveRoomMasterMaster(RoomMasterDto roomMasterDto) throws ApplicationException {
		try {
			RoomMaster roomMaster = mapper.map(roomMasterDto, RoomMaster.class, "RoomMasterDto_to_RoomMaster");
			// BATCH-5 org-trust fix: save previously wrote no org/unit/status (mapper doesn't carry them
			// from the DTO) — new rooms would be NULL-org, invisible to their own tenant's scoped queries.
			roomMaster.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
			roomMaster.setUnitId(verifiedIdentityService.getVerifiedUnitId());
			roomMaster.setStatus('A');
			roomMaster.setCreatedDate(TimeZoneComponent.getDateByzone(0));
			roomMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
			save(roomMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response getRoomMasterById(int roomId, int orgId, int unitId) throws ApplicationException {
		try {
			List<RoomMasterDto> roomMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ROOM_MASTER_BY_ID")
					.setParameter("roomId", roomId)
					.setParameter("orgId", orgId)
					.setParameter("unitId", unitId)
					.setResultTransformer(Transformers.aliasToBean(RoomMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, roomMasterDtoList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRoomMasterList(RoomMasterDto roomMasterDto) throws ApplicationException {
		try {
			List<RoomMasterDto> roomMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ROOM_MASTER_LIST")
					.setParameter("orgId", roomMasterDto.getOrganizationId())
					.setParameter("unitId", roomMasterDto.getUnitId())
					.setFirstResult(roomMasterDto.getOffset() != null ? roomMasterDto.getOffset() : 0)
					.setMaxResults(
							roomMasterDto.getNoOfRecordsPerPage() != null ? roomMasterDto.getNoOfRecordsPerPage() : 10)
					.setResultTransformer(Transformers.aliasToBean(RoomMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, roomMasterDtoList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRoomMaster(RoomMasterDto roomMasterDto) throws ApplicationException {
		try {
			RoomMaster roomMaster=findById(roomMasterDto.getRoomId());
			// BATCH-5 org-trust fix (write-by-ID class): reject rows not owned by the verified tenant.
			if (roomMaster == null || roomMaster.getOrganizationId() == null
					|| !roomMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			if(roomMasterDto.getBedCategoryId()!=null)
			roomMaster.setBedCategoryId(roomMasterDto.getBedCategoryId());
			if(roomMasterDto.getBillingBedCategoryId()!=null)
			roomMaster.setBillingBedCategoryId(roomMasterDto.getBillingBedCategoryId());
			roomMaster.setIsIsolation(roomMasterDto.getIsIsolation());
			roomMaster.setRoomCode(roomMasterDto.getRoomCode());
			roomMaster.setRoomDesc(roomMasterDto.getRoomDesc());
			roomMaster.setWardId(roomMasterDto.getWardId());	
			roomMaster.setIsModality(roomMasterDto.getIsModality());
			roomMaster.setUpdatedBy(roomMasterDto.getUpdatedBy());
			roomMaster.setErCategoryId(roomMasterDto.getErCategoryId());
			roomMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(roomMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(roomMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);

		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRoomMasterStatus(RoomMasterDto roomMasterDto) throws ApplicationException {
		try {
			RoomMaster roomMaster=findById(roomMasterDto.getRoomId());
			// BATCH-5 org-trust fix (write-by-ID class): reject rows not owned by the verified tenant.
			if (roomMaster == null || roomMaster.getOrganizationId() == null
					|| !roomMaster.getOrganizationId().equals(verifiedIdentityService.getVerifiedOrganizationId())) {
				return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
			}
			// BATCH-5 fix: ternary wrote ACTIVE/INACTIVE into varchar(1) — toggle never worked (same
			// pre-existing bug as bedCategory/ward). Persist the UI's 'A'/'I' directly.
			roomMaster.setStatus(roomMasterDto.getStatus() == 'A' ? 'A' : 'I');
			roomMaster.setUpdatedBy(roomMasterDto.getUpdatedBy());
			roomMaster.setUpdatedDate(
					GlobalCommonDateUtils.getDate(roomMasterDto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			update(roomMaster);
			return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRoomMasterCount(RoomMasterDto roomMasterDto) throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			Criteria c = session.createCriteria(RoomMaster.class);
			c.add(Restrictions.eq("organizationId", roomMasterDto.getOrganizationId()));
			c.add(Restrictions.eq("unitId", roomMasterDto.getUnitId()));
			c.setProjection(Projections.rowCount());
			Long count = (Long) c.uniqueResult();
			return new Response(SUCCESS, null, null, null, count);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRoomMasterByName(RoomMasterDto roomMasterDto) throws ApplicationException {
		try {
			List<RoomMasterDto> roomMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ROOM_MASTER_BY_NAME")
					.setParameter("orgId", roomMasterDto.getOrganizationId())
					.setParameter("unitId", roomMasterDto.getUnitId())
					.setParameter("roomName", roomMasterDto.getRoomDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(RoomMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, roomMasterDtoList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRoomMasterByNameNotById(RoomMasterDto roomMasterDto) throws ApplicationException {
		try {
			List<RoomMasterDto> roomMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ROOM_MASTER_BY_NAME_NOT_BY_ID")
					.setParameter("roomId", roomMasterDto.getRoomId())
					.setParameter("unitId", roomMasterDto.getUnitId())
					.setParameter("roomName", roomMasterDto.getRoomDesc().toLowerCase())
					.setResultTransformer(Transformers.aliasToBean(RoomMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, roomMasterDtoList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRoomByWardId(RoomMasterDto roomMasterDto) throws ApplicationException {
		try {
			List<RoomMasterDto> roomMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ROOM_BY_WARD_ID")
					.setParameter("wardId", roomMasterDto.getWardId())
					.setParameter("orgId", roomMasterDto.getOrganizationId())
					.setParameter("unitId", roomMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(RoomMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, roomMasterDtoList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveModalityRoom(RoomMasterDto roomMasterDto) throws ApplicationException {
		try {
			List<RoomMasterDto> roomMasterDtoList = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_ACTIVE_MODALITY_ROOM")
					.setParameter("orgId", roomMasterDto.getOrganizationId())
					.setParameter("unitId", roomMasterDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(RoomMasterDto.class)).list();
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, roomMasterDtoList, null);
		} catch (HibernateException he) {
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
