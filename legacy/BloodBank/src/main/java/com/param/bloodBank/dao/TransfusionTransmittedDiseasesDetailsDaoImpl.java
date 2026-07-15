package com.param.bloodBank.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDetailsDto;
import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.bloodBank.model.TransfusionTransmittedDiseasesDetails;
import com.param.global.common.GlobalCommonDateUtils;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.common.dao.GenericDao;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Repository
@SuppressWarnings({"rawtypes", "unchecked"})
public class TransfusionTransmittedDiseasesDetailsDaoImpl extends GenericDao<TransfusionTransmittedDiseasesDetails, Integer> implements ITransfusionTransmittedDiseasesDetailsDao,ICommonConstants{

	final static Logger logger = Logger.getLogger(TransfusionTransmittedDiseasesDetailsDaoImpl.class);


	public TransfusionTransmittedDiseasesDetailsDaoImpl() {
		super(TransfusionTransmittedDiseasesDetails.class);
	}
	
	@Autowired
	SessionFactory sessionFactory;

	@Override
	public Response saveTransfusionTransmittedDiseasesDetails(TransfusionTransmittedDiseasesDetailsDto dto)throws ApplicationException {
	
		try {
			TransfusionTransmittedDiseasesDetails transfusionTransmittedDiseasesDetails = new TransfusionTransmittedDiseasesDetails();
			transfusionTransmittedDiseasesDetails.setTransfusionTransmittedDiseasesId(dto.getTransfusionTransmittedDiseasesId());
			transfusionTransmittedDiseasesDetails.setBloodGroupId(dto.getBloodGroupId());
			transfusionTransmittedDiseasesDetails.setCreatedBy(dto.getCreatedBy());
			transfusionTransmittedDiseasesDetails.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			transfusionTransmittedDiseasesDetails.setOrganizationId(dto.getOrganizationId());
			transfusionTransmittedDiseasesDetails.setStatus('A');
			transfusionTransmittedDiseasesDetails.setTestId(dto.getTestId());
			transfusionTransmittedDiseasesDetails.setTestResult(dto.getTestResult());
			transfusionTransmittedDiseasesDetails.setUnitId(dto.getUnitId());
			transfusionTransmittedDiseasesDetails.setUpdatedBy(dto.getCreatedBy());
			transfusionTransmittedDiseasesDetails.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			transfusionTransmittedDiseasesDetails=save(transfusionTransmittedDiseasesDetails);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, transfusionTransmittedDiseasesDetails);
	}
	 catch (Exception e) {
	 	logger.error("saveTransfusionTransmittedDiseasesDetails failed", e);
	 	throw new ServiceException(e.getMessage());
	 }
		
	}

	@Override
	public Response getTTDDetailsById(TransfusionTransmittedDiseasesDto transfusionTransmittedDiseasesDto)
			throws ApplicationException {
		try {
			
			List<TransfusionTransmittedDiseasesDetailsDto> list = sessionFactory.getCurrentSession()
					.getNamedQuery("GET_TTD_LIST_BY_ID")
					.setParameter("organizationId", transfusionTransmittedDiseasesDto.getOrganizationId())
					.setParameter("unitId", transfusionTransmittedDiseasesDto.getUnitId())
					.setParameter("transfusionTransmittedDiseasesId", transfusionTransmittedDiseasesDto.getTransfusionTransmittedDiseasesId())
					.setResultTransformer(Transformers.aliasToBean(TransfusionTransmittedDiseasesDetailsDto.class)).list();
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, list, null);
	}
	 catch (Exception e) {
	 	logger.error("getTTDDetailsById failed", e);
	 	throw new ServiceException(e.getMessage());
	 }
	}

	@Override
	public Response updateTransfusionTransmittedDiseasesDetails(TransfusionTransmittedDiseasesDetailsDto dto)throws ApplicationException {
		try {
			TransfusionTransmittedDiseasesDetails transfusionTransmittedDiseasesDetails = new TransfusionTransmittedDiseasesDetails();
			transfusionTransmittedDiseasesDetails.setTransfusionTransmittedDiseasesDetailsId(dto.getTransfusionTransmittedDiseasesDetailsId());
			transfusionTransmittedDiseasesDetails.setTransfusionTransmittedDiseasesId(dto.getTransfusionTransmittedDiseasesId());
			transfusionTransmittedDiseasesDetails.setBloodGroupId(dto.getBloodGroupId());
			transfusionTransmittedDiseasesDetails.setCreatedBy(dto.getCreatedBy());
			transfusionTransmittedDiseasesDetails.setCreatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			transfusionTransmittedDiseasesDetails.setOrganizationId(dto.getOrganizationId());
			transfusionTransmittedDiseasesDetails.setStatus('A');
			transfusionTransmittedDiseasesDetails.setTestId(dto.getTestId());
			transfusionTransmittedDiseasesDetails.setTestResult(dto.getTestResult());
			transfusionTransmittedDiseasesDetails.setUnitId(dto.getUnitId());
			transfusionTransmittedDiseasesDetails.setUpdatedBy(dto.getCreatedBy());
			transfusionTransmittedDiseasesDetails.setUpdatedDate(GlobalCommonDateUtils.getDate(dto.getCreatedDate(), "dd-M-yyyy HH:ss:mm"));
			transfusionTransmittedDiseasesDetails=update(transfusionTransmittedDiseasesDetails);
			
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, transfusionTransmittedDiseasesDetails);
	}
	 catch (Exception e) {
	 	logger.error("updateTransfusionTransmittedDiseasesDetails failed", e);
	 	throw new ServiceException(e.getMessage());
	 }
	}
	
	
}
