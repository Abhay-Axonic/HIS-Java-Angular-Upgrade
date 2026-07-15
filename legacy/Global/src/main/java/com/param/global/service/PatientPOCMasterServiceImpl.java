package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IPatientPOCMasterDao;
import com.param.global.dto.PatientPOCMasterDTO;

@Service
@SuppressWarnings("rawtypes")
public class PatientPOCMasterServiceImpl implements IPatientPOCMasterService, ICommonConstants {

	@Autowired
	IPatientPOCMasterDao iPatientPOCMasterDao;

	@Override
	public Response savePOCMaster(PatientPOCMasterDTO patientPOCMasterDTO) throws Exception {

		try {
			return iPatientPOCMasterDao.savePOCMaster(patientPOCMasterDTO);
		} catch (Exception e) {
			throw e;
		}

	}

	@Override
	public Response searchPOCMaster(String patientSearch) {
		try {
			return iPatientPOCMasterDao.searchPOCMaster(patientSearch);
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public Response updatePocMaster(PatientPOCMasterDTO patientPOCMasterDTO) {
		try {
			return iPatientPOCMasterDao.updatePocMaster(patientPOCMasterDTO);
		} catch (Exception e) {
			throw e;
		}
	}

}
