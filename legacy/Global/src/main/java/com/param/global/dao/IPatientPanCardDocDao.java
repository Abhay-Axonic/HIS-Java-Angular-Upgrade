package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.PatientPanCardDocDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IPatientPanCardDocDao {

	Response savePanCardDocument(PatientPanCardDocDto dto);

	Response getPanCardDetailsById(int billId, int screenId)throws ApplicationException;

	Response inwardDataupdate(Double consumenetamount, Integer mateinwardid,Integer id)throws ApplicationException;

	Response addConsumeAmount(Double consumenetamount, String rgnumber, String inwordno)throws ApplicationException;

}
