package com.param.otims.dao;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.OrderDetailsPackageMapperDto;
import com.param.global.otims.dto.OtSchedulingDto;
import com.param.otims.model.OtScheduling;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class OtSchedulingDaoImpl extends GenericDao<OtScheduling, Integer> implements IOtSchedulingDao,ICommonConstants{

	private static final Logger logger = Logger.getLogger(OtSchedulingDaoImpl.class);

	public OtSchedulingDaoImpl() {
		super(OtScheduling.class);
	}

	@Override
	public Response saveOtScheduling(List<OtSchedulingDto>  list) throws ApplicationException {
	
		try {
			
			for (OtSchedulingDto otSchedulingDto : list) {

				LocalTime fromTime1 = LocalTime.parse( otSchedulingDto.getFromTime());
				LocalDate today = LocalDate.now();
				Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				LocalTime toTime1 = LocalTime.parse( otSchedulingDto.getToTime());
				Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

				OtScheduling mstr = new OtScheduling();
				//System.out.println("fromTime     "+fromTime);
				mstr.setDayId(otSchedulingDto.getDayId());
				mstr.setFromTime(fromTime);
				mstr.setToTime(toTime);
				Date date1=new SimpleDateFormat("yyyy-M-dd").parse(otSchedulingDto.getOtDate());  
				mstr.setOtDate(date1);
				mstr.setSwitchOverTime(otSchedulingDto.getSwitchOverTime());
				mstr.setTableId(otSchedulingDto.getTableId());
				mstr.setTheatreId(otSchedulingDto.getTheatreId());
				mstr.setCreatedBy(otSchedulingDto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(otSchedulingDto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setOrgId(otSchedulingDto.getOrgId());
				mstr.setStatus(otSchedulingDto.getStatus());
				mstr.setUnitId(otSchedulingDto.getUnitId());
				mstr.setUpdatedBy(otSchedulingDto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(otSchedulingDto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				save(mstr);
			
			}
			return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response getOtScheduling(OtSchedulingDto otSchedulingDto) throws ApplicationException {
		try {
			
			int count = 0;
			List<OtSchedulingDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OT_SCHEDULING")
					.setFirstResult(otSchedulingDto.getOffset() != null ? otSchedulingDto.getOffset() : 0)
					.setMaxResults(otSchedulingDto.getNoOfRecordsPerPage() != null ? otSchedulingDto.getNoOfRecordsPerPage() : 10)
					.setParameter("organizationId", otSchedulingDto.getOrgId())
					.setParameter("unitId", otSchedulingDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(OtSchedulingDto.class)).list();
			
			if(list!=null && list.size()>0) {
				List<OtSchedulingDto> listForCount = sessionFactory.getCurrentSession()
						.getNamedQuery("GET_OT_SCHEDULING")
						.setParameter("organizationId", otSchedulingDto.getOrgId())
						.setParameter("unitId", otSchedulingDto.getUnitId())
						.setResultTransformer(Transformers.aliasToBean(OtSchedulingDto.class)).list();
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
	public Response deactivateOtSchedule(OtSchedulingDto otSchedulingDto) throws ApplicationException {
		try {
			
			OtScheduling mstr = findById(otSchedulingDto.getOtSchedulingId());
			mstr.setStatus(otSchedulingDto.getStatus());
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
	public Response checkOtScheduling(OtSchedulingDto dto) throws ApplicationException {
		try {
			List<OtSchedulingDto> list = sessionFactory.getCurrentSession()
				.getNamedQuery("TO_CHECK_ALREADY_EXITS_OT_SCHEDULING")
				.setParameter("organizationId", dto.getOrgId())
				.setParameter("unitId", dto.getUnitId())
				.setParameter("opThreatre", dto.getTheatreId())
				.setParameter("opTable", dto.getTableId())
				.setParameter("otDate", dto.getOtDate())
				.setParameter("fromTime", dto.getFromTime())
				.setParameter("toTime", dto.getToTime())
				.setResultTransformer(Transformers.aliasToBean(OtSchedulingDto.class)).list();
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
	public Response toCheckSlotAvailability(OtSchedulingDto otSchedulingDto) throws ApplicationException {
		try {
			List<OtSchedulingDto> obj = sessionFactory.getCurrentSession()
				.getNamedQuery("TO_CHECK_OT_AVAILABILITY")
				.setParameter("organizationId", otSchedulingDto.getOrgId())
				.setParameter("unitId", otSchedulingDto.getUnitId())
				.setParameter("opThreatre", otSchedulingDto.getTheatreId())
				.setParameter("opTable", otSchedulingDto.getTableId())
				.setParameter("otDate", otSchedulingDto.getOtDate())
				.setParameter("fromTime", otSchedulingDto.getFromTime())
				.setParameter("toTime", otSchedulingDto.getToTime())
				.setResultTransformer(Transformers.aliasToBean(OtSchedulingDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, obj, null);

		} catch (HibernateException he) {
			logger.error(he.getMessage(), he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getOtSchedulingForOTcalender(OtSchedulingDto otSchedulingDto) throws ApplicationException {
		try {
			
			int count = 0;
			List<OtSchedulingDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_OT_SCHEDULING_FOR_OT_CALENDER")
					.setParameter("organizationId", otSchedulingDto.getOrgId())
					.setParameter("unitId", otSchedulingDto.getUnitId())
					.setParameter("fromDate", otSchedulingDto.getFromDate())
					.setParameter("toDate", otSchedulingDto.getToDate())
					.setResultTransformer(Transformers.aliasToBean(OtSchedulingDto.class)).list();
			
			
			
			String str1 = "";
			Integer i = 0;
			//For min time
			for (i = 0; i < list.size(); i++) {
				if(i == 0) {
					str1 = str1.concat(list.get(i).getFromTime()); 
				}else {
					str1 = str1.concat(",").concat(list.get(i).getFromTime());  
				}
			}
			
			String str2 = "";
			Integer i1 = 0;
			//For max time
			for (i1 = 0; i1 < list.size(); i1++) {
				if(i1 == 0) {
					str2 = str2.concat(list.get(i1).getToTime()); 
				}else {
					str2 = str2.concat(",").concat(list.get(i1).getToTime());  
				}
			}
			String[] allTimes = str1.replaceAll(" ", "").split(",");
			String minTime = Stream.of(allTimes).min(String::compareTo).get();
			
			String[] allTimes1 = str2.replaceAll(" ", "").split(",");
			String maxTime = Stream.of(allTimes1).max(String::compareTo).get();
			
			otSchedulingDto.setMaxTime(maxTime);
			otSchedulingDto.setMinTime(minTime);
	
				return new Response(SUCCESS, SUCCESS_CODE, null, list, otSchedulingDto);

			} catch (HibernateException he) {
				logger.error(he.getMessage(), he);
				throw new ServiceException(he.getMessage());
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
				throw new ServiceException(e.getMessage());
			}
	}
}
