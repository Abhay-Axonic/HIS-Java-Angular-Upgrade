package com.param.global.otims.dao;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dto.SubSequentDetailsDto;
import com.param.otims.model.SubSequentDetails;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class SubSequentDetailsDaoImpl extends GenericDao<SubSequentDetails, Integer>implements ISubSequentDetailsDao, ICommonConstants{
	final static Logger logger = Logger.getLogger(SubSequentDetailsDaoImpl.class);


	public SubSequentDetailsDaoImpl() {
		super(SubSequentDetails.class);

	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveSubSequentDetails(SubSequentDetailsDto dto) throws ApplicationException {
		try{
			
			SubSequentDetails mster=new SubSequentDetails();
			mster.setDiscPercentage(dto.getDiscPercentage()!=null ? dto.getDiscPercentage() : null);
			mster.setSequenceId(dto.getSequenceId());
			mster.setSubSequentProcConfigId(dto.getSubSequentProcConfigId());
			mster.setOrgId(dto.getOrgId());
			mster.setUnitId(dto.getUnitId());
			mster.setCreatedBy(dto.getCreatedBy());
			mster.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:mm:ss"));
			mster.setUpdatedBy(dto.getUpdatedBy());
			mster.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			mster.setStatus(dto.getStatus());
			if(dto.getSubSequentDetailsId()!= null) {
				mster.setSubSequentDetailsId(dto.getSubSequentDetailsId());
				mster = update(mster);
			}else {
				mster = save(mster);
			}
			
			
		return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mster);	
		
	}catch (HibernateException he) {
	logger.error("saveSubSequentDetails failed", he);
	}catch (Exception e) {
	logger.error("saveSubSequentDetails failed", e);
	}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
