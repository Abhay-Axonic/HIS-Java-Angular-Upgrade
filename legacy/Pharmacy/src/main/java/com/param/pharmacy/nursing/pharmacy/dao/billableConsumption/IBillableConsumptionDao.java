package com.param.pharmacy.nursing.pharmacy.dao.billableConsumption;

import java.util.List;

import com.param.entity.model.pharmacy.BillableConsumption;
import com.param.entity.model.pharmacy.BillableConsumptionDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.global.pharmacy.dto.BillableConsumptionSave;
import com.param.pharmacy.nursing.pharmacy.dto.billableConsumption.BillableConsumptionRequest;

public interface IBillableConsumptionDao {

	List<BillableConsumption> billableConsumptionSearch(BillableConsumptionRequest patientIndentRequest, Integer screenId);
	
	BillableConsumption billableConsumptionById(Integer id, Integer unitId);
	
	List<BillableConsumptionDetail> billableConsumptionDetailById(Integer id, Integer unitId);
	
	BillableConsumption saveBillableConsumption(BillableConsumption billableConsumption);
	
	public Sale saveBill(Sale sale);

	List<BillableConsumptionSave> getBillableConsumptionDetails(Long id);
}
