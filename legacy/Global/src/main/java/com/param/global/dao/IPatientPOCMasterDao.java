package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.PatientPOCMasterDTO;

@SuppressWarnings({ "rawtypes" })
public interface IPatientPOCMasterDao {
	
	Response savePOCMaster(PatientPOCMasterDTO patientPOCMasterDTO) throws Exception;
	
	Response searchPOCMaster(String patientPOCUniqueNo);
	
	Response updatePocMaster(PatientPOCMasterDTO patientPOCMasterDTO);

}
