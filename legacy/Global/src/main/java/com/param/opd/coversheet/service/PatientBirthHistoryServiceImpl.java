package com.param.opd.coversheet.service;

import com.param.global.common.Response;
import com.param.opd.coversheet.dao.IPatientBirthHistoryDao;
import com.param.opd.coversheet.dto.PatientBirthHistoryDTO;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class PatientBirthHistoryServiceImpl implements IPatientBirthHistoryService{

    @Autowired
    private IPatientBirthHistoryDao iPatientBirthHistoryDao;

    @Override
    @Transactional
    public Response savePatientBirthHistory(PatientBirthHistoryDTO patientBirthHistoryDTO) throws ApplicationException {
        try{
            return iPatientBirthHistoryDao.savePatientBirthHistory(patientBirthHistoryDTO);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    @Transactional
    public Response getPatientBirthHistory(Integer patientId) throws ApplicationException {
        try{
            return iPatientBirthHistoryDao.getPatientBirthHistory(patientId);
        }catch (Exception e){
            throw e;
        }
    }
}
