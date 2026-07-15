package com.param.billing.global.transaction.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.ITPackageConsumptionServiceDetailDao;

@Service
@SuppressWarnings("rawtypes")
public class TPackageConsumptionServiceDetailServiceImpl implements ITPackageConsumptionServiceDetailService, ICommonConstants{

	@Autowired
	private ITPackageConsumptionServiceDetailDao iTPackageConsumptionServiceDetailDao;
	
	@Override
	@Transactional
	public Response updatePackageConsumptionServiceQty(Integer packageId, Integer patientId, Integer encounterId,
			Integer serviceId, Integer admissionId, Integer visitTypeId) throws ServiceException {
		try{
			return iTPackageConsumptionServiceDetailDao.updatePackageConsumptionServiceQty(packageId, patientId, encounterId, serviceId, admissionId, visitTypeId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updatePackageConsumptionEOBalanceQty(Integer packageId, Integer patientId, Integer encounterId,
			Integer serviceId, Integer admissionId, Integer visitTypeId) throws ServiceException {
		try{
			return iTPackageConsumptionServiceDetailDao.updatePackageConsumptionEOBalanceQty(packageId, patientId, encounterId, serviceId, admissionId, visitTypeId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updatePackageConsumptionCapBalanceQty(Integer packageId, Integer patientId, Integer encounterId,
			Integer departmentId, Integer subDepartmentId, BigDecimal serviceAmount, Integer admissionId,
			Integer visitTypeId) throws ServiceException {
		try{
			return iTPackageConsumptionServiceDetailDao.updatePackageConsumptionCapBalanceQty(packageId, patientId, encounterId, departmentId, subDepartmentId, serviceAmount, admissionId, visitTypeId);
		}catch(Exception e){
			
			throw e;
		}
	}

	@Override
	@Transactional
	public Response updatePackageConsumptSevcQtyOnSrvCancl(Integer packageId, Integer patientId, Integer encounterId,
			Integer serviceId, Integer admissionId, Integer visitTypeId,Integer createdBy) throws ServiceException {
		try{
			return iTPackageConsumptionServiceDetailDao.updatePackageConsumptSevcQtyOnSrvCancl(packageId, patientId, encounterId, serviceId, admissionId, visitTypeId,createdBy);
		}catch(Exception e){
			
			throw e;
		}
	}

}
