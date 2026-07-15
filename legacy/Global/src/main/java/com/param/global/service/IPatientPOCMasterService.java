package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.PatientPOCMasterDTO;

@Transactional
@SuppressWarnings({ "rawtypes" })
public interface IPatientPOCMasterService {
	
	Response savePOCMaster(PatientPOCMasterDTO patientPOCMasterDTO) throws Exception;
	
	Response searchPOCMaster(String patientPOCUniqueNo);
	
	Response updatePocMaster(PatientPOCMasterDTO patientPOCMasterDTO);

}
