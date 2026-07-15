package com.param.global.doctor.service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.doctor.dao.IOutPatientFeedbackDao;
import com.param.global.doctor.dto.OutPatientFeedbackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"rawtypes"})
public class OutPatientFeedbackServiceImpl implements IOutPatientFeedbackService, ICommonConstants {

    @Autowired
    private IOutPatientFeedbackDao iOutPatientFeedbackDao;

    @Override
    public Response saveOutPatientFeedback(OutPatientFeedbackDTO outPatientFeedbackDTO) {
        try{
            return iOutPatientFeedbackDao.saveOutPatientFeedback(outPatientFeedbackDTO);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response getOutPatientFeedbackList(Integer unitId, Integer organizationId, Integer patientId, String visitType, Integer visitTypeId , Integer encounterId) {
        try{
            return iOutPatientFeedbackDao.getOutPatientFeedbackList(unitId, organizationId, patientId, visitType, visitTypeId , encounterId);
        }catch (Exception e){
            throw e;
        }
    }
}
