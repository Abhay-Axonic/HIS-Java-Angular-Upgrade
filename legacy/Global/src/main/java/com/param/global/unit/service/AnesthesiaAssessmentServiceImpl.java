package com.param.global.unit.service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.IAnesthesiaAssessmentDao;
import com.param.global.unit.dao.IInitialAssessmentICUAndERDao;
import com.param.global.unit.dto.AnesthesiaAssessmentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"rawtypes"})
public class AnesthesiaAssessmentServiceImpl implements IAnesthesiaAssessmentService, ICommonConstants {

    @Autowired
    private IAnesthesiaAssessmentDao iAnesthesiaAssessmentDao;

    @Override
    public Response saveAnesthesiaAssessment(AnesthesiaAssessmentDTO anesthesiaAssessmentDTO) {
        try{

            Response res = iAnesthesiaAssessmentDao.getAnesthesiaAssessment(anesthesiaAssessmentDTO.getUnitId(), anesthesiaAssessmentDTO.getOrganizationId(), anesthesiaAssessmentDTO.getPatientId());

            if(res.getObject()!=null){
                AnesthesiaAssessmentDTO anesthesiaAssessment = (AnesthesiaAssessmentDTO)res.getObject();
                        iAnesthesiaAssessmentDao.inactiveAnesthesiaAssessment(anesthesiaAssessment);
            }

            return iAnesthesiaAssessmentDao.saveAnesthesiaAssessment(anesthesiaAssessmentDTO);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response getAnesthesiaAssessment(Integer unitId, Integer organizationId, Integer patientId) {
        try{
            return iAnesthesiaAssessmentDao.getAnesthesiaAssessment(unitId, organizationId, patientId);
        }catch (Exception e){
            throw e;
        }
    }
}
