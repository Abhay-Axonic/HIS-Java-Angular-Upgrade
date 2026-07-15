package com.param.global.unit.dao;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.billing.global.transaction.model.RequestDiscount;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.CabinMaster;
import com.param.global.model.UHIDMergeRequest;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.CoversheetAccessMasterDto;
import com.param.global.unit.dto.UHIDMergeDto;
import com.param.global.unit.service.IUHIDMergeRequestDao;
import com.param.opd.encounter.dto.EncounterMasterDto;
import com.param.utility.TimeZoneComponent;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes", "unchecked"})
@Repository
public class UHIDMergeRequestDaoImpl extends GenericDao<UHIDMergeRequest, Integer> implements IUHIDMergeRequestDao,ICommonConstants
{
	final static Logger logger = Logger.getLogger(UHIDMergeRequestDaoImpl.class);


	public UHIDMergeRequestDaoImpl() {
		super(UHIDMergeRequest.class);
	}

	@Autowired
	SessionFactory sessionFactory;
	
	@Override
	public Response getUserUHIDMergeList(UHIDMergeDto uHIDMergeDto) throws ApplicationException{
		try {
			List<UHIDMergeDto> list=sessionFactory.getCurrentSession()
					.getNamedQuery("GET_UHID_REQUEST_USERS_LIST")
					.setParameter("organizationId",  uHIDMergeDto.getOrganizationId())
					.setParameter("unitId",  uHIDMergeDto.getUnitId())
			        .setResultTransformer(Transformers.aliasToBean(UHIDMergeDto.class)).list();

		return new Response(SUCCESS, SUCCESS_CODE, null, list, null);
		
		} catch (HibernateException he) {
		logger.error("getUserUHIDMergeList failed", he);
		} catch (Exception e) {
		logger.error("getUserUHIDMergeList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveUHIDMergeMaster(UHIDMergeDto uHIDMergeDto)  throws ApplicationException{
       try{
    	   
    	   RequestDiscount requestDiscount=new RequestDiscount();
    	   requestDiscount.setFromPatientId(uHIDMergeDto.getFromPatientId());
    	   requestDiscount.setToPatientId(uHIDMergeDto.getToPatientId());
    	   requestDiscount.setOrganizationId(uHIDMergeDto.getOrganizationId());
    	   requestDiscount.setUnitId(uHIDMergeDto.getUnitId());
    	   requestDiscount.setRequestType(uHIDMergeDto.getRequestType());
      //   requestDiscount.setReqStatus(uHIDMergeDto.getReqStatus());
    	   requestDiscount.setReqStatus(1);
    	   requestDiscount.setRequestSentBy(uHIDMergeDto.getRequestFrom());
       //  requestDiscount.setUpdatedDate("updatedDate" GlobalCommonDateUtils.getDate(requestDiscount.getUpdatedDate(),"yyyy-M-dd HH:mm:yyyy"));
        //   requestDiscount.setRequestDate(GlobalCommonDateUtils.getDate(requestDiscount.getRequestDate(), "DD-MM-YYYY HH:mm:ss"));
    	   requestDiscount.setCreatedDate(GlobalCommonDateUtils.getDate(new Date(), "yyyy-M-dd"));
    	   requestDiscount.setRequestDate(GlobalCommonDateUtils.getDate(new Date(), "yyyy-M-dd"));
    	   requestDiscount.setUpdatedDate(GlobalCommonDateUtils.getDate(new Date(), "yyyy-M-dd"));
       //    requestDiscount.setCreatedDate(GlobalCommonDateUtils.getDate(requestDiscount.getCreatedDate(), "DD-MM-YYYY HH:mm:ss"));
    	   requestDiscount.setRequestSentTo(uHIDMergeDto.getRequestTo());
    	   requestDiscount.setUserName(uHIDMergeDto.getUsername());
    //     requestDiscount.setUpdatedDate(GlobalCommonDateUtils.getDate(requestDiscount.getUpdatedDate(), "dd-M-yyyy HH:mm:ss"));
			 
    	   sessionFactory.getCurrentSession().save(requestDiscount);
	return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
	
	} catch (HibernateException he) {
	logger.error("saveUHIDMergeMaster failed", he);
	} catch (Exception e) {
	logger.error("saveUHIDMergeMaster failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
}
