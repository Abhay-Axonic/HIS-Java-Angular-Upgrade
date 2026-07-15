package com.param.pharmacy.master.unit.service.pharmacyTariff;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.pharmacy.master.unit.dao.pharmacyTariff.IPharmacyTariffDao;
import com.param.pharmacy.master.unit.dto.pharmacyTariff.PharmacyTariffDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class PharmacyTariffServiceImpl implements IPharmacyTariffService,ICommonConstants {

	private static final Logger logger = Logger.getLogger(PharmacyTariffServiceImpl.class);

	@Autowired
	IPharmacyTariffDao iPharmacyTariffDao;
	
	@Override
	@Transactional
	public Response savePharmacyTariff(PharmacyTariffDto pharmacyTariffDto) throws ApplicationException {
		try {
			for(PharmacyTariffDto dto : pharmacyTariffDto.getPharmacyTariffDtoList())
			{
				if(dto.getId()==null || dto.getId()==0)
					iPharmacyTariffDao.savePharmacyTariff(dto);
			}
			return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response getAllPharmacyTariff(Integer orgId, Integer unitId) throws ApplicationException {
		try {
			return iPharmacyTariffDao.getAllPharmacyTariff(orgId,unitId);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updateStatus(Integer id,Character status) throws ApplicationException {
		try {
			return iPharmacyTariffDao.updateStatus(id,status);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	@Transactional
	public Response updatePharmacyTariff(PharmacyTariffDto pharmacyTariffDto) throws ApplicationException {
		try {
			return iPharmacyTariffDao.updatePharmacyTariff(pharmacyTariffDto);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}
	
}
