package com.param.adt.master.unit.dao;

import java.util.List;

import com.param.adt.common.Response;
import com.param.adt.master.global.dto.VisitTypeMasterDto;
import com.param.adt.master.unit.dto.BillingBedCategoryMasterDto;
import com.param.adt.master.unit.model.BillingBedVisitTypeMapper;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBillingBedCategoryMasterDao {

	Response getActiveBillingBedCategoryList(BillingBedCategoryMasterDto bedCategoryMasterDto)throws ApplicationException;

	Response getActiveBillingBedCategoryByBedCat(BillingBedCategoryMasterDto bedCategoryMasterDto)throws ApplicationException;
	
	Response saveBillingBedCategoryMaster(BillingBedCategoryMasterDto billingBedCategoryMasterDto)throws ApplicationException;

	Response getBillingBedCategoryById(int billingBedId, int orgId, int unitId)throws ApplicationException;

	Response getBillingBedCategoryList(BillingBedCategoryMasterDto billingBedCategoryMasterDto)throws ApplicationException;

	Response updateBillingBedCategoryMaster(BillingBedCategoryMasterDto billingBedCategoryMasterDto)throws ApplicationException;

	Response updateBillingBedCategoryMasterStatus(BillingBedCategoryMasterDto billingBedCategoryMasterDto)throws ApplicationException;

	Response getBillingBedCategoryMasterCount(BillingBedCategoryMasterDto billingBedCategoryMasterDto)throws ApplicationException;
	
	Response getBillingBedCategoryByName(BillingBedCategoryMasterDto billingBedCategoryMasterDto)throws ApplicationException;
	
	Response getBillingBedCategoryByNameNotById(BillingBedCategoryMasterDto billingBedCategoryMasterDto)throws ApplicationException;

	Integer saveBillingBedVisitTypeMapper(BillingBedVisitTypeMapper bedVisitTypeMapper)throws ApplicationException;

	List<VisitTypeMasterDto> getBillingBedCategoryVistList(BillingBedCategoryMasterDto billingBedCategoryMasterDto)
			throws ApplicationException;

	Response updatePreviousVisitTypeList(BillingBedVisitTypeMapper bedVisitTypeMapper)throws ApplicationException;

	Response getBillingBedCategoryByVistTypeId(int visitTypeId, int orgId, int unitId)throws ApplicationException;
	
	Response getBillingBedCategoryByVistTypeIdMultiple(BillingBedCategoryMasterDto billingBedCategoryMasterDto)throws ApplicationException;
	
	/*Response getActiveBillingBedCategoryMasterList(Integer orgId)throws ApplicationException;*/

}
