package com.param.procurement.consignment.dao.ConsignmentInword;

import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.ConsignmentInward;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequest;

public interface IConsignmentInwordDao {

	List<ConsignmentInward> getConsignmentInwardList(ConsignmentRequest request, Integer screenId);

	ConsignmentInward getConsignmentInwardDetails(Integer id, Integer unitId);
	
	ConsignmentInward saveConsignmentInword(ConsignmentInward consignmentInward);

	ConsignmentInward updateConsignmentInword(ConsignmentInward consignmentInward);

	Batch saveBatch(Batch batch);

}
