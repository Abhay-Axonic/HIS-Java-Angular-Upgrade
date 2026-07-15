package com.param.procurement.consignment.dao.consignment;

import java.util.List;

import com.param.entity.model.inventory.Batch;
import com.param.entity.model.procurement.ConsignmentOutward;
import com.param.procurement.consignment.dto.consignment.ConsignmentRequest;

public interface IConsignmentOutwordDao {

	List<ConsignmentOutward> getConsignmentOutwardList(ConsignmentRequest consignmentOutwardRequest, Integer screenId);
	
	ConsignmentOutward saveConsignmentOutward(ConsignmentOutward consignmentOutward);
	
	ConsignmentOutward getConsignmentOutwardDetails(Integer id, Integer unitId);
	
	ConsignmentOutward updateConsignmentOutword(ConsignmentOutward consignmentOutward);

	Batch saveBatch(Batch batch);
	
	

}
