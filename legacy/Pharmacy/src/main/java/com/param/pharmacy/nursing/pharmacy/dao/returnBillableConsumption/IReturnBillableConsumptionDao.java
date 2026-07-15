package com.param.pharmacy.nursing.pharmacy.dao.returnBillableConsumption;

import java.util.List;

import com.param.entity.model.pharmacy.BillableConsumption;
import com.param.entity.model.pharmacy.BillableConsumptionDetail;
import com.param.entity.model.pharmacy.ReturnBillableConsumption;
import com.param.entity.model.pharmacy.ReturnBillableConsumptionDetail;
import com.param.global.pharmacy.dto.BillableConsumptionItem;
import com.param.pharmacy.ip.dto.returnAcceptance.PharmacySaleReturnDto;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionRequest;

public interface IReturnBillableConsumptionDao {

	List<ReturnBillableConsumption> returnBillableConsumptionSearch(BillableConsumptionRequest request, Integer screenId);
	
	ReturnBillableConsumption returnBillableConsumptionById(Integer id, Integer unitId);
	
	List<ReturnBillableConsumptionDetail> returnBillableConsumptionDetailById(Integer id, Integer unitId);
	
	ReturnBillableConsumption saveReturnBillableConsumption(ReturnBillableConsumption returnBillableConsumption);
	
	List<BillableConsumption> returnBillableConsumptionSearchIssue(BillableConsumptionRequest request);
	
	List<BillableConsumptionDetail> returnBillableConsumptionAdd(Integer id, Integer unitId);
	
	Long getReturnQuantity(Integer billableConsumptionDetailId, Integer unitId);

	PharmacySaleReturnDto getSaleDetailIdByByBillalbleComptionDetails(Integer detailsId);
	
	/*start code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns*/
	Long getTotalConsumeQuantity(Integer billableConsumptionDetailId, Integer unitId);
	/*end code for resolved HIS-9214 Return billable consumption to have issues, consumed and returned columns*/

	Integer getReturnQuantityById(Integer id);

	List<PharmacySaleReturnDto> getSaleDetailIdByByBillalbleComptionDetailsAll(
			List<Integer> billableConsumptionDetailsIds);
}
