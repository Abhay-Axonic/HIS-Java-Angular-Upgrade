package com.param.global.doctor.service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.doctor.dao.IInPatientFeedbackDao;
import com.param.global.doctor.dao.IOutPatientFeedbackDao;
import com.param.global.doctor.dto.OutPatientFeedbackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"rawtypes"})
public class IInPatientFeedbackServiceImpl implements IInPatientFeedbackService, ICommonConstants {

    @Autowired
    private IInPatientFeedbackDao iInPatientFeedbackDao;

    @Override
    public Response saveInPatientFeedback(OutPatientFeedbackDTO outPatientFeedbackDTO) {
        try{
            return iInPatientFeedbackDao.saveInPatientFeedback(outPatientFeedbackDTO);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response getInPatientFeedbackList(Integer unitId, Integer organizationId, Integer patientId, String visitType, Integer visitTypeId, Integer admissionId) {
        try{
            return iInPatientFeedbackDao.getInPatientFeedbackList(unitId, organizationId, patientId, visitType, visitTypeId, admissionId);
        }catch (Exception e){
            throw e;
        }
    }
}
