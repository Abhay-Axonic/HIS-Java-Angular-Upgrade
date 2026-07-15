package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.PatientPanCardDocDto;
import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IPatientPanCardDocService {

	Response savePanCardDocument(PatientPanCardDocDto dto)throws ApplicationException;

	Response getPanCardDetailsById(int billId, int screenId)throws ApplicationException;

	Response inwardDataupdate(Double consumenetamount, Integer mateinwardid,Integer id)throws ApplicationException;

}
	