package com.param.bloodBank.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodComponentSeparationDetailsDto;
import com.param.bloodBank.dto.BloodComponentSeparationDto;
import com.param.bloodBank.model.BloodComponentSeparation;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodComponentSeparationDaoImpl  extends GenericDao<BloodComponentSeparation, Integer> implements IBloodComponentSeparationDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodComponentSeparationDaoImpl.class);


	
	public BloodComponentSeparationDaoImpl() {
		super(BloodComponentSeparation.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveBloodComponentSeparation(BloodComponentSeparationDto dto) throws ApplicationException {
		try {
			
				BloodComponentSeparation mstr = new BloodComponentSeparation();
				mstr.setBagCapacity(dto.getBagCapacity());
				mstr.setBloodGroupId(dto.getBloodGroupId());
				mstr.setCollectedVolume(dto.getCollectedVolume());
				mstr.setRedCellId(dto.getRedCellId());
				mstr.setSeperationDate(GlobalCommonDateUtils.getDate(dto.getSeperationDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setTechnicianId(dto.getTechnicianId());
				mstr.setTransfusionTransmittedDiseasesId(dto.getTransfusionTransmittedDiseasesId());
				mstr.setCreatedBy(dto.getCreatedBy());
				mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr.setOrganizationId(dto.getOrganizationId());
				mstr.setStatus('A');
				mstr.setUnitId(dto.getUnitId());
				mstr.setUpdatedBy(dto.getUpdatedBy());
				mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
				mstr=save(mstr);
				return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);

		} catch (Exception e) {
			logger.error("saveBloodComponentSeparation failed", e);
			throw new ServiceException(e.getMessage());
		}

	}

	@Override
	public Response getComponentSeparationListForStockUpdation(BloodComponentSeparationDetailsDto dto)
			throws ApplicationException {
		try {
			Session session = sessionFactory.getCurrentSession();
			StringBuilder query=new StringBuilder(session.getNamedQuery("GET_COMPONENT_SEPARATION_LIST_FOR_STOCK_UPDATION").getQueryString());
			
			StringBuffer subQuery = new StringBuffer();
			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				subQuery.append(" AND to_char(compSep.created_date,'YYYY-MM-DD') between :fromDate and :toDate");
			}
			
			/*if(dto.getExpiryDate()!=null && dto.getExpiryDate()!=null ){
				subQuery.append(" AND to_char(tbrc.expiry_date,'YYYY-MM-DD') = '"+dto.getExpiryDate()+"'");
			}
			*/
			if(dto.getComponentId()!=null && dto.getComponentId()!=0){
				subQuery.append(" AND comSepDetails.component_id  =:componentId ");
			}
		
			query = query.append(subQuery.toString());
			Query actualQuery = session.createSQLQuery(query.toString());
			 actualQuery
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId());


			if(dto.getFromDate()!=null && dto.getToDate()!=null && dto.getFromDate()!="" && dto.getToDate()!=""){
				actualQuery.setParameter("fromDate",dto.getFromDate());
				actualQuery.setParameter("toDate",dto.getToDate());
			}

			if(dto.getComponentId()!=null && dto.getComponentId()!=0){
				actualQuery.setParameter("componentId",dto.getComponentId());
			}


			List<BloodComponentSeparationDetailsDto> list =actualQuery.setResultTransformer(Transformers.aliasToBean(BloodComponentSeparationDetailsDto.class)).list();
		
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
	}
	 catch (Exception e) {
	 	logger.error("getComponentSeparationListForStockUpdation failed", e);
	 	throw new ServiceException(e.getMessage());
	 }
	}

}
