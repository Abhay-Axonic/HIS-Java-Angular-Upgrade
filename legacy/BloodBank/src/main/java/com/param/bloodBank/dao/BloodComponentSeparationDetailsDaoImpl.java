package com.param.bloodBank.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.BloodComponentSeparationDetailsDto;
import com.param.bloodBank.model.BloodComponentSeparationDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class BloodComponentSeparationDetailsDaoImpl  extends GenericDao<BloodComponentSeparationDetails, Integer> implements IBloodComponentSeparationDetailsDao, ICommonConstants{

	final static Logger logger = Logger.getLogger(BloodComponentSeparationDetailsDaoImpl.class);


	
	public BloodComponentSeparationDetailsDaoImpl() {
		super(BloodComponentSeparationDetails.class);
	}

	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveBloodComponentSeparationDetails(BloodComponentSeparationDetailsDto dto)
			throws ApplicationException {
		try {
			
			BloodComponentSeparationDetails mstr = new BloodComponentSeparationDetails();
			mstr.setBagWeight(dto.getBagWeight());
			mstr.setBloodComponentSeparationId(dto.getBloodComponentSeparationId());
			mstr.setBloodBagNo(dto.getBloodBagNo());
			mstr.setComponentId(dto.getComponentId());
			mstr.setRemark(dto.getRemark()!= null ? dto.getRemark() : "");
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
		logger.error("saveBloodComponentSeparationDetails failed", e);
		throw new ServiceException(e.getMessage());
	}
	}
}
