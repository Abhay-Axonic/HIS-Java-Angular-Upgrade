package com.param.lis.transaction.dao;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.model.BloodBagLoanIn;
import com.param.entity.lis.transaction.PhlebotomistHomeCollection;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureOrderMasterDto;
import com.param.lis.global.common.IError;
import com.param.lis.global.common.ITransactionConstants;
import com.param.lis.transaction.dto.PhlebotomistHomeCollectionDto;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({ "rawtypes","unused","unchecked"})
public class PhlebotomistHomeCollectionDaoImpl extends GenericDao<PhlebotomistHomeCollection, Integer>
implements IPhlebotomistHomeCollectionDao, ICommonConstants, ITransactionConstants, IError {

	@Autowired
	private SessionFactory sessionFactory;


	public PhlebotomistHomeCollectionDaoImpl() {
		super(PhlebotomistHomeCollection.class);
	}


	@Override
	public Response savePhlebotomistHomeCollection(PhlebotomistHomeCollectionDto dto) throws ApplicationException {
		try {
			LocalTime fromTime1 = LocalTime.parse( dto.getFromTime());
			LocalDate today = LocalDate.now();
			Instant fromTime = fromTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			LocalTime toTime1 = LocalTime.parse( dto.getToTime());
			Instant toTime = toTime1.atDate(today).atZone(ZoneId.of("UTC")).toInstant();

			PhlebotomistHomeCollection mstr = new PhlebotomistHomeCollection();
			mstr.setFromTime(fromTime);
			mstr.setOrderId(dto.getOrderId());
			mstr.setPhlebotomistCollectionDate(GlobalCommonDateUtils.getDate(dto.getPhlebotomistCollectionDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setToTime(toTime);
			mstr.setUserId(dto.getUserId());
			mstr.setCreatedBy(dto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setOrganizationId(dto.getOrganizationId());
			mstr.setStatus('A');
			mstr.setUnitId(dto.getUnitId());
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setStatusId(dto.getStatusId());
			if(dto.getPhlebotomistHomeCollectionId()!= null) {
				mstr.setPhlebotomistHomeCollectionId(dto.getPhlebotomistHomeCollectionId());
				mstr=update(mstr);
			}
			mstr=save(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);

	} catch (Exception e) {
			
			throw e;

	}
	}


	@Override
	public Response validateUserAlocation(PhlebotomistHomeCollectionDto dto) throws ApplicationException {
		try
        {
			List<PhlebotomistHomeCollectionDto> checkList = new ArrayList<PhlebotomistHomeCollectionDto>();
			if(dto.getPhlebotomistHomeCollectionId()!=null) {
				 checkList = sessionFactory.getCurrentSession()
						.createSQLQuery(
								"SELECT tphc.phlebotomist_home_collection_id AS \"phlebotomistHomeCollectionId\" " +
										"FROM lab.t_phlebotomist_home_collection tphc " +
										"WHERE tphc.unit_id = :unitId " +
										"AND tphc.organization_id = :organizationId " +
										"AND tphc.user_id = :userId " +
										"AND tphc.phlebotomist_home_collection_id <> :phlebotomistHomeCollectionId " +
										"AND to_char(tphc.phlebotomist_collection_date, 'yyyy-MM-dd') = :phlebotomistCollectionDate " +
										"AND ( (to_char(tphc.from_time, 'hh24:mi:ss') <= :fromTime " +
										"AND to_char(tphc.to_time, 'hh24:mi:ss') >= :fromTime) " +
										"OR (to_char(tphc.from_time, 'hh24:mi:ss') <= :toTime " +
										"AND to_char(tphc.to_time, 'hh24:mi:ss') >= :toTime) " +
										"OR (to_char(tphc.from_time, 'hh24:mi:ss') >= :fromTime " +
										"AND to_char(tphc.to_time, 'hh24:mi:ss') <= :toTime) )"
						)
						.setParameter("unitId", dto.getUnitId())
						.setParameter("organizationId", dto.getOrganizationId())
						.setParameter("userId", dto.getUserId())
						.setParameter("phlebotomistHomeCollectionId", dto.getPhlebotomistHomeCollectionId())
						.setParameter("phlebotomistCollectionDate", dto.getPhlebotomistCollectionDate())
						.setParameter("fromTime", dto.getFromTime())
						.setParameter("toTime", dto.getToTime())
						.setResultTransformer(Transformers.aliasToBean(PhlebotomistHomeCollectionDto.class))
						.list();

			}else {
				 checkList = sessionFactory.getCurrentSession()
						.createSQLQuery(
								"SELECT tphc.phlebotomist_home_collection_id AS \"phlebotomistHomeCollectionId\" " +
										"FROM lab.t_phlebotomist_home_collection tphc " +
										"WHERE tphc.unit_id = :unitId " +
										"AND tphc.organization_id = :organizationId " +
										"AND tphc.user_id = :userId " +
										"AND to_char(tphc.phlebotomist_collection_date, 'yyyy-MM-dd') = :phlebotomistCollectionDate " +
										"AND ( (to_char(tphc.from_time, 'hh24:mi:ss') <= :fromTime " +
										"AND to_char(tphc.to_time, 'hh24:mi:ss') >= :fromTime) " +
										"OR (to_char(tphc.from_time, 'hh24:mi:ss') <= :toTime " +
										"AND to_char(tphc.to_time, 'hh24:mi:ss') >= :toTime) " +
										"OR (to_char(tphc.from_time, 'hh24:mi:ss') >= :fromTime " +
										"AND to_char(tphc.to_time, 'hh24:mi:ss') <= :toTime) )"
						)
						.setParameter("unitId", dto.getUnitId())
						.setParameter("organizationId", dto.getOrganizationId())
						.setParameter("userId", dto.getUserId())
						.setParameter("phlebotomistCollectionDate", dto.getPhlebotomistCollectionDate())
						.setParameter("fromTime", dto.getFromTime())
						.setParameter("toTime", dto.getToTime())
						.setResultTransformer(Transformers.aliasToBean(PhlebotomistHomeCollectionDto.class))
						.list();

			}
			Boolean check = false;
			if(checkList != null && checkList.size()>0) {
				check = true;
			}
				return new Response<>(SUCCESS, SUCCESS_CODE, null, null, check);
		
        } catch (Exception e)
        {
            throw e;
        }
	}

}
