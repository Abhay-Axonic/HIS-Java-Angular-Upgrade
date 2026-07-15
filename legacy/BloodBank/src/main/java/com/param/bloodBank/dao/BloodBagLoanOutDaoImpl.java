package com.param.bloodBank.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodBagLoanOutDto;
import com.param.bloodBank.model.BloodBagLoanOut;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodBagLoanOutDaoImpl extends GenericDao<BloodBagLoanOut, Integer> implements IBloodBagLoanOutDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagLoanOutDaoImpl.class);


	
	public BloodBagLoanOutDaoImpl() {
		super(BloodBagLoanOut.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveloanout(BloodBagLoanOutDto dto) throws ApplicationException {
		try {
			
			BloodBagLoanOut mstr = new BloodBagLoanOut();
			mstr.setCreatedBy(dto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setOrganizationId(dto.getOrganizationId());
			mstr.setStatus('A');
			mstr.setUnitId(dto.getUnitId());
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setLoanOutNumber(dto.getLoanOutNumber());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setLoanOutStatusId(16);//loan Out Pending
			mstr=save(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);

	} catch (Exception e) {
		logger.error("saveloanout failed", e);
		throw new ServiceException(e.getMessage());
	}

	}

	@Override
	public Response getLoanOutList(BloodBagLoanOutDto dto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("GET_BLOOD_BAG_LOAN_OUT_LIST").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				subQuery.append(" AND to_char(mstr.createdDate,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			
			query = query.append(subQuery.toString());
			Query actualQuery = session.createQuery(query.toString());
			actualQuery.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId());

			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				actualQuery.setParameter("fromDate",dto.getFromDate());
				actualQuery.setParameter("toDate",dto.getToDate());
			}

			List<BloodBagLoanOutDto> list = actualQuery.setResultTransformer(Transformers.aliasToBean(BloodBagLoanOutDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("getLoanOutList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response approveLoanOut(BloodBagLoanOutDto dto) {
		try {
			
			BloodBagLoanOut mstr = findById(dto.getBloodBagLoanOutId());
			mstr.setApprovedBy(dto.getApprovedBy());
			mstr.setLoanOutStatusId(13);//loan Out Pending
			mstr=update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);

	} catch (Exception e) {
		logger.error("approveLoanOut failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
}
