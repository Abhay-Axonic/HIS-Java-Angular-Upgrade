package com.param.global.pesapal.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.pesapal.dao.IPesaPalIntegrationDao;
import com.param.global.pesapal.dto.PesapalPaymentDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class PesaPalIntegrationServiceImpl implements IPesaPalIntegrationService,ICommonConstants {

	final static Logger logger = Logger.getLogger(PesaPalIntegrationServiceImpl.class);

	@Autowired
	IPesaPalIntegrationDao pesaPalIntegrationDao;
	
	@Override
	public Response getPesaPalAuthorizationData(PesapalPaymentDto reqDto) throws ApplicationException {
		try {
			return pesaPalIntegrationDao.getPesaPalAuthorizationData(reqDto);
			} catch (Exception e) {
			logger.error("getPesaPalAuthorizationData failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response savePesaPalPaymentRequestDetails(PesapalPaymentDto reqDto) throws ApplicationException {
		try {
			return pesaPalIntegrationDao.savePesaPalPaymentRequestDetails(reqDto);
			} catch (Exception e) {
			logger.error("savePesaPalPaymentRequestDetails failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updatePesapalTransactionStatus(PesapalPaymentDto reqDto) throws ApplicationException {
		try {
			return pesaPalIntegrationDao.updatePesapalTransactionStatus(reqDto);
			} catch (Exception e) {
			logger.error("updatePesapalTransactionStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getTransactionReferenceByTransactionId(PesapalPaymentDto reqDto) throws ApplicationException {
		try {
			return pesaPalIntegrationDao.getTransactionReferenceIdByTransactionId(reqDto);
			} catch (Exception e) {
			logger.error("getTransactionReferenceByTransactionId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response getTransactionDetailByTransactionId(PesapalPaymentDto reqDto) throws ApplicationException {
		try {
			return pesaPalIntegrationDao.getTransactionDetailByTransactionId(reqDto);
			} catch (Exception e) {
			logger.error("getTransactionDetailByTransactionId failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
