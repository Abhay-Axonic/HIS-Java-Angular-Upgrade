package com.param.otims.dao;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.otims.dto.EquipmentSchedulingDto;
import com.param.otims.model.EquipmentScheduling;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class EquipmentSchedulingDaoImpl extends GenericDao<EquipmentScheduling, Integer> implements IEquipmentSchedulingDao,ICommonConstants  {

	private static final Logger logger = Logger.getLogger(EquipmentSchedulingDaoImpl.class);

	public EquipmentSchedulingDaoImpl() {
		super(EquipmentScheduling.class);
	}

	@Override
	public Response saveEquipmentScheduling(List<EquipmentSchedulingDto> list) throws ApplicationException {
		try {
			
			for (EquipmentSchedulingDto equipmentSchedulingDto : list) {
			
				EquipmentScheduling mstr = new EquipmentScheduling();

				LocalTime fromTime1 = LocalTime.parse( equipmentSchedulingDto.getFromTime());
				LocalDate today = LocalDate.now();
				Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				LocalTime toTime1 = LocalTime.parse( equipmentSchedulingDto.getToTime());
				Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				mstr.setDayId(equipmentSchedulingDto.getDayId());
				mstr.setFromTime(fromTime);
				mstr.setToTime(toTime);
				Date date1=new SimpleDateFormat("yyyy-M-dd").parse(equipmentSchedulingDto.getEquipmentSchedulingDate());  
				mstr.setEquipmentSchedulingDate(date1);
				mstr.setAssetCategoryId(equipmentSchedulingDto.getAssetCategoryId());
				mstr.setBatchId(equipmentSchedulingDto.getBatchId());
				mstr.setItemId(equipmentSchedulingDto.getItemId());
				mstr.setCreatedBy(equipmentSchedulingDto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(equipmentSchedulingDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setOrgId(equipmentSchedulingDto.getOrgId());
				mstr.setStatus(equipmentSchedulingDto.getStatus());
				mstr.setUnitId(equipmentSchedulingDto.getUnitId());
				mstr.setUpdatedBy(equipmentSchedulingDto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(equipmentSchedulingDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				save(mstr);
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response checkOtScheduling(EquipmentSchedulingDto dto) throws ApplicationException {
		try {
			List<EquipmentSchedulingDto> list = sessionFactory.getCurrentSession()
				.getNamedQuery("TO_CHECK_ALREADY_EXITS_EQUIPMENT_SCHEDULING")
				.setParameter("organizationId", dto.getOrgId())
				.setParameter("unitId", dto.getUnitId())
				.setParameter("itemId", dto.getItemId())
				.setParameter("batchId", dto.getBatchId())
				.setParameter("equDate", dto.getEquipmentSchedulingDate())
				.setParameter("fromTime", dto.getFromTime())
				.setParameter("toTime", dto.getToTime())
				.setResultTransformer(Transformers.aliasToBean(EquipmentSchedulingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getEquipmentScheduling(EquipmentSchedulingDto equipmentSchedulingDto) throws ApplicationException {
		try {
			int count = 0;
			List<EquipmentSchedulingDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EQUIPMENT_SCHEDULING")
					.setFirstResult(equipmentSchedulingDto.getOffset() != null ? equipmentSchedulingDto.getOffset() : 0)
					.setMaxResults(equipmentSchedulingDto.getNoOfRecordsPerPage() != null ? equipmentSchedulingDto.getNoOfRecordsPerPage() : 10)
					.setParameter("organizationId", equipmentSchedulingDto.getOrgId())
					.setParameter("unitId", equipmentSchedulingDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(EquipmentSchedulingDto.class)).list();
			
			List<EquipmentSchedulingDto> listForCount = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_EQUIPMENT_SCHEDULING")
					.setParameter("organizationId", equipmentSchedulingDto.getOrgId())
					.setParameter("unitId", equipmentSchedulingDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(EquipmentSchedulingDto.class)).list();
			
			if(listForCount!=null && listForCount.size()>0) {
				count = listForCount.size();
			}
				return new Response(SUCCESS, SUCCESS_CODE, null, list, count);

			} catch (HibernateException he) {
				logger.error(he.getMessage(), he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response deActivateEquipmentScheduling(EquipmentSchedulingDto equipmentSchedulingDto)throws ApplicationException {
		try {

			EquipmentScheduling mstr = findById(equipmentSchedulingDto.getEquipmentSchedulingId());
			mstr.setStatus(equipmentSchedulingDto.getStatus());
			update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
			} catch (HibernateException he) {
				logger.error(he.getMessage(), he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
	}

	@Override
	public Response checkEquipmentavailableOrNot(EquipmentSchedulingDto dto)
			throws ApplicationException {
		try {
			List<EquipmentSchedulingDto> list = sessionFactory.getCurrentSession()
				.getNamedQuery("TO_CHECK_AVAILABILITY")
				.setParameter("organizationId", dto.getOrgId())
				.setParameter("unitId", dto.getUnitId())
				.setParameter("itemId", dto.getItemId())
				.setParameter("batchId", dto.getBatchId())
				.setParameter("equDate", dto.getEquipmentSchedulingDate())
				.setParameter("fromTime", dto.getFromTime())
				.setParameter("toTime", dto.getToTime())
				.setResultTransformer(Transformers.aliasToBean(EquipmentSchedulingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

}
