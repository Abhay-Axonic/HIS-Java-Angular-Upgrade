package com.param.bloodBank.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodBagLoanOutDetailsDto;
import com.param.bloodBank.dto.BloodBagLoanOutDto;
import com.param.bloodBank.model.BloodBagLoanOutDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodBagLoanOutDetailsDaoImpl extends GenericDao<BloodBagLoanOutDetails, Integer> implements IBloodBagLoanOutDetailsDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodBagLoanOutDetailsDaoImpl.class);


	
	public BloodBagLoanOutDetailsDaoImpl() {
		super(BloodBagLoanOutDetails.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveBloodBagLoanOutDetails(BloodBagLoanOutDetailsDto dto) {
		try {
			
			BloodBagLoanOutDetails mstr = new BloodBagLoanOutDetails();
			mstr.setBagCapacityId(dto.getBagCapacityId());
			mstr.setBagTypeId(dto.getBagTypeId());
			mstr.setBagUnitNo(dto.getBagUnitNo());
			mstr.setBarcode(dto.getBarcode());
			mstr.setBatchNo(dto.getBatchNo());
			mstr.setBloodBagLoanOutId(dto.getBloodBagLoanOutId());
			mstr.setBloodBagNo(dto.getBloodBagNo());
			mstr.setBloodBankId(dto.getBloodBankId());
			mstr.setBloodGroupId(dto.getBloodGroupId());
			mstr.setComponentId(dto.getComponentId());
			mstr.setRemark(dto.getRemark());
			mstr.setSegmentNo(dto.getSegmentNo());
			mstr.setShortName(dto.getShortName());
			mstr.setBloodBagStockId(dto.getBloodBankStockId());
			mstr.setCreatedBy(dto.getCreatedBy());
			mstr.setExpiryDate(GlobalCommonDateUtils.getDate(dto.getExpiryDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setOrganizationId(dto.getOrganizationId());
			mstr.setStatus('A');
			mstr.setUnitId(dto.getUnitId());
			mstr.setUpdatedBy(dto.getUpdatedBy());
			mstr.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getUpdatedDate(), "dd-M-yyyy HH:ss:mm"));
			mstr.setLoanOutStatusId(dto.getLoanOutStatusId());
			mstr=save(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);

	} catch (Exception e) {
		logger.error("saveBloodBagLoanOutDetails failed", e);
		throw new ServiceException(e.getMessage());
	}

	}

	@Override
	public Response getLoanOutDeatilsList(BloodBagLoanOutDto dto) throws ApplicationException {
		try{
			
			try
	        {
				List<BloodBagLoanOutDetailsDto> checkAdmitionId = sessionFactory.getCurrentSession().createSQLQuery(
					"select " + 
					"mbbsm.bag_unit_no as \"bagUnitNo\","
					+ " mbbsm.blood_bag_loan_out_details_id as \"bloodBagLoanOutDetailsId\","
					+ " mbbsm.blood_bag_stock_id as \"bloodBankStockId\"," + 
					"mbbsm.barcode as \"barcode\"," + 
					"mbbsm.batch_no as \"batchNo\"," + 
					"mbbsm.blood_bag_no as \"bloodBagNo\"," + 
					"mbbsm.segment_no as \"segmentNo\"," + 
					"mbbsm.short_name as \"shortName\"," + 
					"mbbsm.remark as \"remark\"," + 
					"mbbcm.blood_bag_capacity_desc as \"bloodBagCapacity\"," + 
					"mbbtm.blood_bag_type_desc as \"bloodBagType\"," + 
					"mbbm.blood_bank_desc as \"bloodBankName\"," + 
					"mbcm.blood_component_desc as \"bloodComponentName\","
				 +  " to_char(mbbsm.expiry_date,'yyyy-mm-dd') as \"expiryDate\"," + 
					" mbgm.blood_group_desc as \"bloodGroup\","
					+ " mbbsm.bag_capacity_id as \"bagCapacityId\"," + 
					"	mbbsm.bag_type_id as \"bagTypeId\"," + 
					"	mbbsm.blood_bank_id as \"bloodBankId\"," + 
					"	mbbsm.blood_group_id as \"bloodGroupId\"," + 
					"	mbbsm.component_id as \"componentId\" " + 
					"from blood_bank.t_blood_bag_loan_out_details mbbsm " + 
					"inner join blood_bank.m_blood_bag_capacity_master mbbcm  on  mbbsm.bag_capacity_id = mbbcm.blood_bag_capacity_id " + 
					"inner join blood_bank.m_blood_bag_type_master mbbtm on mbbtm.blood_bag_type_id  = mbbsm.bag_type_id " + 
					"inner join blood_bank.m_blood_bank_master mbbm on mbbm.blood_bank_id = mbbsm.blood_bank_id " + 
					"inner join blood_bank.m_blood_component_master mbcm on mbcm.blood_component_id = mbbsm.component_id " + 
					"inner join blood_bank.m_blood_group_master mbgm on mbgm.blood_group_id = mbbsm.blood_group_id " + 
					"where mbbsm.blood_bag_loan_out_id = "+dto.getBloodBagLoanOutId() + 
					"  ")
					.setResultTransformer(Transformers.aliasToBean(BloodBagLoanOutDetailsDto.class)).list();
				
					return new Response<>(SUCCESS, SUCCESS_CODE, null, checkAdmitionId, null);
			
	        } catch (Exception e)
	        {
	        	logger.error("getLoanOutDeatilsList failed", e);
	        	throw new ServiceException(e.getMessage());
	        }
			
		}catch(Exception e){
			logger.error("getLoanOutDeatilsList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response approveBloodBagLoanOutDetails(BloodBagLoanOutDetailsDto dtailsDto) {
		try {
			
			BloodBagLoanOutDetails mstr = findById(dtailsDto.getBloodBagLoanOutDetailsId());
			mstr.setLoanOutStatusId(13);//loan Out Pending
			mstr=update(mstr);
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, mstr);

		} catch (Exception e) {
			logger.error("approveBloodBagLoanOutDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
		}

}
