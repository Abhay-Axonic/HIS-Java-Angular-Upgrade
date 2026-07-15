package com.param.billing.global.transaction.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.billing.controller.BillingException;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ITPackageConsumptionCapDetailsDao;

@Service
@SuppressWarnings("rawtypes")
public class TPackageConsumptionCapDetailsServiceImpl implements ITPackageConsumptionCapDetailsService, ICommonConstants{

	@Autowired
	private ITPackageConsumptionCapDetailsDao iTPackageConsumptionCapDetailsDao;
	
	@Override
	@Transactional
	public Response updateTPackageConsumptCapAmtByDeptSubDept(Integer packageId, Integer patientId, Integer encounterId,
			Integer departmentId, Integer subDepartmentId, BigDecimal serviceAmount, Integer admissionId,
			Integer visitTypeId) throws BillingException {
		try{
			return iTPackageConsumptionCapDetailsDao.updateTPackageConsumptCapAmtByDeptSubDept(packageId, patientId, encounterId, departmentId, subDepartmentId, serviceAmount, admissionId, visitTypeId);
		}catch(Exception e){
			
			throw e;
		}
	}

}
