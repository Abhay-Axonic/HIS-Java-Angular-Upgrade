package com.param.pharmacy.narcotics.deliveryWorklist.dao;

import java.util.List;

import com.param.entity.model.pharmacy.Issue;
import com.param.entity.model.pharmacy.Prescription;
import com.param.entity.model.pharmacy.PrescriptionDetail;
import com.param.entity.model.pharmacy.PrescriptionFrequencyDetail;
import com.param.entity.model.pharmacy.Sale;
import com.param.pharmacy.narcotics.issueWorklist.dto.NarcoticsRequest;

public interface INarcoticsDeliveryWorklistDao {
	
	List<Sale> narcoticsDeliveryWorklistSearch(NarcoticsRequest narcoticsRequest, Integer screenId);
	
	List<PrescriptionDetail> narcoticsIssueWorklistDetails(Long id);
	
	PrescriptionFrequencyDetail getPrescriptionFrequencyDetails(Long id);
	
	Prescription getPrescriptionById(Long id);
	
	Issue getIssueBySaleId(Long id);
	
}