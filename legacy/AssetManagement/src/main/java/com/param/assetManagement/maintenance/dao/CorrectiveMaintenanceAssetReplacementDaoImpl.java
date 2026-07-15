package com.param.assetManagement.maintenance.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceAssetReplacementDto;
import com.param.assetManagement.maintenance.dto.CorrectiveMaintenanceScheduleDto;
import com.param.assetManagement.maintenance.model.CorrectiveMaintenanceAssetReplacement;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class CorrectiveMaintenanceAssetReplacementDaoImpl extends GenericDao<CorrectiveMaintenanceAssetReplacement, Integer> implements ICorrectiveMaintenanceAssetReplacementDao, ICommonConstants{

	private static final Logger logger = Logger.getLogger(CorrectiveMaintenanceAssetReplacementDaoImpl.class);

	public CorrectiveMaintenanceAssetReplacementDaoImpl() {
		super(CorrectiveMaintenanceAssetReplacement.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveCorrectiveMaintenanceAssetReplacement(
			CorrectiveMaintenanceAssetReplacementDto correctiveMaintenanceAssetReplacementDto)
			throws ApplicationException {
		try {
			CorrectiveMaintenanceAssetReplacement mstr = new CorrectiveMaintenanceAssetReplacement(); 
			mstr.setCorrectiveMaintenanceId(correctiveMaintenanceAssetReplacementDto.getCorrectiveMaintenanceId()== null?null:correctiveMaintenanceAssetReplacementDto.getCorrectiveMaintenanceId());
			mstr.setCorrectiveMaintenanceSchedulingId(correctiveMaintenanceAssetReplacementDto.getCorrectiveMaintenanceSchedulingId());
			mstr.setCreatedBy(correctiveMaintenanceAssetReplacementDto.getCreatedBy());
			mstr.setItemId(correctiveMaintenanceAssetReplacementDto.getItemId());
			mstr.setNewSerialNoId(correctiveMaintenanceAssetReplacementDto.getNewSerialNoId());
			mstr.setNewStoreId(correctiveMaintenanceAssetReplacementDto.getNewStoreId());
			mstr.setOrganizationId(correctiveMaintenanceAssetReplacementDto.getOrganizationId());
			mstr.setRequestedSerialNoId(correctiveMaintenanceAssetReplacementDto.getRequestedSerialNoId());
			mstr.setRequestedStoreId(correctiveMaintenanceAssetReplacementDto.getRequestedStoreId());
			mstr.setStatus('A');
			mstr.setReplaceStatusId(1);
			mstr.setUnitId(correctiveMaintenanceAssetReplacementDto.getUnitId());
			mstr.setUpdatedBy(correctiveMaintenanceAssetReplacementDto.getUpdatedBy());
			mstr.setCreatedBy(correctiveMaintenanceAssetReplacementDto.getCreatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceAssetReplacementDto.getUpdatedDate(),"dd-M-yyyy HH:mm:ss"));
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(correctiveMaintenanceAssetReplacementDto.getCreatedDate(),"dd-M-yyyy HH:mm:ss"));
			
			sessionFactory.getCurrentSession().save(mstr);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);

	} catch (HibernateException he) {
		logger.error("Error while saving corrective maintenance asset replacement", he);
		throw new ServiceException(he.getMessage());
	} catch (Exception e) {
		logger.error("Error while saving corrective maintenance asset replacement", e);
		throw new ServiceException(e.getMessage());
	}
	}

	@Override
	public Response getreplaceItemById(CorrectiveMaintenanceScheduleDto orrectiveMaintenanceScheduleDto)
			throws ApplicationException {
		try {
			List<CorrectiveMaintenanceAssetReplacementDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REPLACE_ITEM_LIST_BY_SCHEDULE_ID")
					.setParameter("unitId", orrectiveMaintenanceScheduleDto.getUnitId())
					.setParameter("organizationId", orrectiveMaintenanceScheduleDto.getOrganizationId())
					.setParameter("correctiveMaintenanceScheduleId", orrectiveMaintenanceScheduleDto.getCorrectiveMaintenanceScheduleId())
					.setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceAssetReplacementDto.class)).list();
			
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);

		}catch (Exception e) {
			logger.error("Error while getting replace item list by schedule id", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getreplaceDetailOnReturnByMultipleCrit(CorrectiveMaintenanceScheduleDto correctiveMaintenanceScheduleDto) throws ApplicationException {
try {
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_REPLACE_ITEM_LIST")
					.getQueryString();
		
			StringBuffer subQuery = new StringBuffer();
			if(correctiveMaintenanceScheduleDto.getFromDate()!=null && correctiveMaintenanceScheduleDto.getToDate()!=null){
				subQuery.append(" AND to_char(replace.createdDate, 'YYYY-MM-DD') BETWEEN :fromDate AND fromDate ");
			}
			
			if(correctiveMaintenanceScheduleDto.getStoreId()!=0){
				subQuery.append(" AND replace.newStoreId =:storeId ");
			}
			if(correctiveMaintenanceScheduleDto.getReplaceStatusId() != 0){
					subQuery.append(" AND replace.replaceStatusId =:replaceStatusId ");
			}
			if(correctiveMaintenanceScheduleDto.getCreatedBy() != 0){
				subQuery.append(" AND replace.createdBy =:createdBy ");
		}
		
			subQuery.append(" order by replace.assetReplacementId desc");
			query = query.concat(subQuery.toString());
			Query createQuery = sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("unitId", correctiveMaintenanceScheduleDto.getUnitId())
					.setParameter("organizationId", correctiveMaintenanceScheduleDto.getOrganizationId());

	if(correctiveMaintenanceScheduleDto.getFromDate()!=null && correctiveMaintenanceScheduleDto.getToDate()!=null){
		createQuery.setParameter("fromDate", correctiveMaintenanceScheduleDto.getFromDate());
		createQuery.setParameter("toDate", correctiveMaintenanceScheduleDto.getToDate());
	}

	if(correctiveMaintenanceScheduleDto.getStoreId()!=0){
		createQuery.setParameter("storeId", correctiveMaintenanceScheduleDto.getStoreId());
	}

	if(correctiveMaintenanceScheduleDto.getReplaceStatusId() != 0){
		createQuery.setParameter("replaceStatusId", correctiveMaintenanceScheduleDto.getReplaceStatusId());
	}
	if(correctiveMaintenanceScheduleDto.getCreatedBy() != 0){
		createQuery.setParameter("createdBy", correctiveMaintenanceScheduleDto.getCreatedBy());
	}

	List<CorrectiveMaintenanceAssetReplacementDto> list = createQuery
			.setResultTransformer(Transformers.aliasToBean(CorrectiveMaintenanceAssetReplacementDto.class))
			.list();
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
		}catch (Exception e) {
			logger.error("Error while getting replace detail on return by multiple criteria", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response returnAsset(CorrectiveMaintenanceAssetReplacement correctiveMaintenanceAssetReplacement)throws ApplicationException {
		try {
			
			CorrectiveMaintenanceAssetReplacement  mstr = findById(correctiveMaintenanceAssetReplacement.getAssetReplacementId());
			mstr.setReplaceStatusId(2);
			update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		}catch (Exception e) {
			logger.error("Error while returning asset", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response rejectAsset(CorrectiveMaintenanceAssetReplacement correctiveMaintenanceAssetReplacement)
			throws ApplicationException {
		try {
			
			CorrectiveMaintenanceAssetReplacement  mstr = findById(correctiveMaintenanceAssetReplacement.getAssetReplacementId());
			mstr.setReasonId(correctiveMaintenanceAssetReplacement.getReasonId());
			mstr.setReplaceStatusId(3);
			mstr.setReasonNote(correctiveMaintenanceAssetReplacement.getReasonNote());
			update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);

		}catch (Exception e) {
			logger.error("Error while rejecting asset", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
