package com.param.pharmacy.ip.dao.returnAcceptance.medication;

import java.math.BigInteger;
import java.util.List;

import com.param.entity.model.inventory.StoreStock;
import com.param.entity.model.pharmacy.PatientIssueReturn;
import com.param.entity.model.pharmacy.Reject;
import com.param.pharmacy.ip.dto.returnAcceptance.ReturnAcceptanceRequest;

public interface IReturnAcceptanceMedicationDao {
	
	List<PatientIssueReturn> returnAcceptanceSearch(ReturnAcceptanceRequest returnAcceptanceRequest, Integer screenId);

	PatientIssueReturn updatePatientIssueReturn(PatientIssueReturn patientIssueReturn);
	
	Reject saveRejectDetails(Reject reject);

	StoreStock saveStoreStock(StoreStock storeStockNew);

	BigInteger getSaleId(Integer id);

	String getDoctorName(BigInteger saleId);
}