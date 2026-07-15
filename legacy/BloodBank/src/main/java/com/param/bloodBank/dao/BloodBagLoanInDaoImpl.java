package com.param.bloodBank.dao;

import java.math.BigInteger;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.param.bloodBank.dto.BloodBagLoanInDto;
import com.param.bloodBank.model.BloodBagLoanIn;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodBagLoanInDaoImpl extends GenericDao<BloodBagLoanIn, Integer> implements IBloodBagLoanInDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagLoanInDaoImpl.class);


	
	public BloodBagLoanInDaoImpl() {
		super(BloodBagLoanIn.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Long getNextValBloodBagStockId() throws ApplicationException {
		try {
				Session session = sessionFactory.getCurrentSession();
				SQLQuery query = session.createSQLQuery("select nextval('blood_bank.loan_in_seq')");
				Long id = ((BigInteger) query.uniqueResult()).longValue();
				
				if(id!=1) {
					
					SQLQuery query2 = session.createSQLQuery("select setval('blood_bank.loan_in_seq'," + (id-1) + ")");
					query2.uniqueResult();
				}else {
					
					SQLQuery query2 = session.createSQLQuery("select setval('blood_bank.loan_in_seq'," + (id) + ")");
					query2.uniqueResult();
				}
				return id;
		} catch (HibernateException he) {
			logger.error("getNextValBloodBagStockId failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("getNextValBloodBagStockId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response savaloanIn(BloodBagLoanInDto bloodBagLoandto) throws ApplicationException {
		try {
	
				BloodBagLoanIn mstr = new BloodBagLoanIn();
				mstr.setCreatedBy(bloodBagLoandto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(bloodBagLoandto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setOrganizationId(bloodBagLoandto.getOrganizationId());
				mstr.setStatus('A');
				mstr.setUnitId(bloodBagLoandto.getUnitId());
				mstr.setUpdatedBy(bloodBagLoandto.getUpdatedBy());
				mstr.setLoanInNumber(bloodBagLoandto.getLoanInNumber());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(bloodBagLoandto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr=save(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);

		} catch (Exception e) {
			logger.error("savaloanIn failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response searchLoanInData(BloodBagLoanInDto dto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_LOAN_IN_DETAILS").getQueryString());
			
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


			List<BloodBagLoanInDto> list =	actualQuery.setResultTransformer(Transformers.aliasToBean(BloodBagLoanInDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("searchLoanInData failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getLoanInDataForApproval(BloodBagLoanInDto dto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("SEARCH_LOAN_IN_DETAILS_FOR_APPROVAL").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				subQuery.append(" AND to_char(mstr.createdDate,'YYYY-MM-DD') between :fromDate and :toDate ");
			}
			query = query.append(subQuery.toString());
			Query actualQuery = session.createQuery(query.toString());
			 actualQuery
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId());

			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				actualQuery.setParameter("fromDate",dto.getFromDate());
				actualQuery.setParameter("toDate",dto.getToDate());
			}


			List<BloodBagLoanInDto> list =actualQuery.setResultTransformer(Transformers.aliasToBean(BloodBagLoanInDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("getLoanInDataForApproval failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
}
