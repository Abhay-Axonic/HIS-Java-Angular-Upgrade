package com.param.bloodBank.dao;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodBankStockDetailsDto;
import com.param.bloodBank.model.BloodBankStock;
import com.param.bloodBank.model.BloodBankStockDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class BloodBankStockDetailsDaoImpl extends GenericDao<BloodBankStockDetails, Integer> implements IBloodBankStockDetailsDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBankStockDetailsDaoImpl.class);


	public BloodBankStockDetailsDaoImpl() {
		super(BloodBankStockDetails.class);
	}

	@Override
	public Response searchBloodBankStockDetails(BloodBankStockDetailsDto dto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();

			BloodBankStockDetailsDto obj = (BloodBankStockDetailsDto)session.getNamedQuery("SEARCH_BLOOD_BANK_STOCK_DETAILS")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("bloodBankId", dto.getBloodBankId())
					.setParameter("bloodGroupId", dto.getBloodGroupId())
					.setParameter("bloodBagCapacityId", dto.getBloodBagCapacityId())
					.setParameter("bloodBagTypeId", dto.getBloodBagTypeId())
					.setParameter("bloodComponentId", dto.getBloodComponentId())
					.setResultTransformer(Transformers.aliasToBean(BloodBankStockDetailsDto.class)).uniqueResult();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, null, obj);
		}catch(Exception e){
			logger.error("searchBloodBankStockDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response saveBloodBankStockDetails(BloodBankStockDetailsDto dto) throws ApplicationException {
		try {
			BloodBankStockDetails mstr = new BloodBankStockDetails();
			mstr.setBloodBagTypeId(dto.getBloodBagTypeId());
			mstr.setAvailableStock(dto.getAvailableStock());
			mstr.setBloodBankId(dto.getBloodBankId());
			mstr.setBloodBagCapacityId(dto.getBloodBagCapacityId());
			mstr.setBloodGroupId(dto.getBloodGroupId());
			mstr.setCreatedBy(dto.getCreatedBy());
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(),"dd-M-yyyy HH:ss:mm"));
			mstr.setOrganizationId(dto.getOrganizationId());
			mstr.setStatus(dto.getStatus());
			mstr.setUnitId(dto.getUnitId());
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setBloodComponentId(dto.getBloodComponentId());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
			save(mstr);
	return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
} catch (Exception e) {
	logger.error("saveBloodBankStockDetails failed", e);
	throw new ServiceException(e.getMessage());
}
	}

	@Override
	public Response updateBloodBankStockDetails(BloodBankStockDetailsDto dto) throws ApplicationException {
		try {
			BloodBankStockDetails mstr = findById(dto.getBloodBankStockDetailsId());
			mstr.setAvailableStock(dto.getAvailableStock());
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(),"dd-M-yyyy HH:ss:mm"));
			update(mstr);
	return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
} catch (Exception e) {
	logger.error("updateBloodBankStockDetails failed", e);
	throw new ServiceException(e.getMessage());
}
	}

	@Override
	public Response getBloodBankStockByComponentId(BloodBankStockDetailsDto dto) throws ApplicationException {
		try{
			Session session = sessionFactory.getCurrentSession();

			List<BloodBankStockDetailsDto> list = session.getNamedQuery("GET_BLOOD_BANK_STOCK_BY_COMPONENTID")
					.setParameter("organizationId", dto.getOrganizationId())
					.setParameter("unitId", dto.getUnitId())
					.setParameter("bloodComponentId", dto.getBloodComponentId())
					.setResultTransformer(Transformers.aliasToBean(BloodBankStockDetailsDto.class)).list();
			
			return new Response<>(SUCCESS, SUCCESS_CODE, null, list, null);
		}catch(Exception e){
			logger.error("getBloodBankStockByComponentId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateRedCellStatus(Integer redCellId) throws ApplicationException {
		try{	
			Integer count=sessionFactory.getCurrentSession().createSQLQuery(" update blood_bank.t_blood_red_cell set blood_confirmation_status = 6 where red_cell_id = "+redCellId+" ")
					.executeUpdate();
			if(count>0) {
				 return new Response(SUCCESS, SUCCESS_CODE, null, null, count);
			}else {
				 return new Response(SUCCESS, SUCCESS_CODE, null, null, 0);
			}
			  
			
		}catch (HibernateException he) {
			logger.error("updateRedCellStatus failed", he);
			throw new ServiceException(he.getMessage());
		} catch (Exception e) {
			logger.error("updateRedCellStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
