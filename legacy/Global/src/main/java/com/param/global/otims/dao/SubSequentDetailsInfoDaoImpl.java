package com.param.global.otims.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.SubSequentDetailsDto;
import com.param.global.otims.dto.SubSequentDetailsInfoDto;
import com.param.otims.model.SubSequentDetailsInfo;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes","unchecked"})
public class SubSequentDetailsInfoDaoImpl  extends GenericDao<SubSequentDetailsInfo, Integer>implements ISubSequentDetailsInfoDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(SubSequentDetailsInfoDaoImpl.class);


	public SubSequentDetailsInfoDaoImpl() {
		super(SubSequentDetailsInfo.class);

	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveSubSequentDetailsInfo(SubSequentDetailsInfoDto dto) throws ApplicationException {
		try{
			
			SubSequentDetailsInfo mster=new SubSequentDetailsInfo();
			
			mster.setDiscPercentage(dto.getDiscPercentage());
			mster.setOrgId(dto.getOrgId());
			mster.setUnitId(dto.getUnitId());
			mster.setCreatedBy(dto.getCreatedBy());
			mster.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			mster.setUpdatedBy(dto.getUpdatedBy());
			mster.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			mster.setStatus(dto.getStatus());
			mster.setSubSequentDetailsId(dto.getSubSequentDetailsId());
			mster.setServiceId(dto.getServiceId());
			if(dto.getSubSequentDetailsInfoId()!= null) {
				mster.setSubSequentDetailsInfoId(dto.getSubSequentDetailsInfoId());
				mster = update(mster);
			}else {
				mster = save(mster);
			}
			
			
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mster);	
		
	}catch (HibernateException he) {
	logger.error("saveSubSequentDetailsInfo failed", he);
	}catch (Exception e) {
	logger.error("saveSubSequentDetailsInfo failed", e);
	}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getSubSequentDetailsInfo(SubSequentDetailsDto subSequentDetailsDto) throws ApplicationException {
		try{
			
			String query = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_SUBSEQUENT_DETAILS_INFO_LIST_BY_DETAILS_ID").getQueryString();
			
			StringBuffer subQuery = new StringBuffer();
			if(subSequentDetailsDto.getSkipZeroDiscount()!=null && subSequentDetailsDto.getSkipZeroDiscount()) {
				subQuery.append(" AND info.discPercentage !=0 ");
			}
			query = query.concat(subQuery.toString());
			List<SubSequentDetailsInfoDto> list = sessionFactory.getCurrentSession().createQuery(query)
					.setParameter("subSequentDetailsId", subSequentDetailsDto.getSubSequentDetailsId())
					.setParameter("orgId", subSequentDetailsDto.getOrgId())
					.setParameter("unitId", subSequentDetailsDto.getUnitId())
					.setResultTransformer(Transformers.aliasToBean(SubSequentDetailsInfoDto.class)).list();
			return new Response(SUCCESS, SUCCESS_CODE, null, list, null);	
	}catch (HibernateException he) {
	logger.error("getSubSequentDetailsInfo failed", he);
	}catch (Exception e) {
	logger.error("getSubSequentDetailsInfo failed", e);
	}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	
}
