package com.param.global.unit.service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.model.InitialAssessmentICUAndER;
import com.param.global.unit.dao.IInitialAssessmentICUAndERDao;
import com.param.global.unit.dao.IInitialAssessmentICUAndERMapperDao;
import com.param.global.unit.dto.InitialAssessmentICUAndERDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"rawtypes"})
public class InitialAssessmentICUAndERServiceImpl implements IInitialAssessmentICUAndERService, ICommonConstants {

    @Autowired
    private IInitialAssessmentICUAndERDao iInitialAssessmentICUAndERDao;

    @Autowired
    private IInitialAssessmentICUAndERMapperDao iInitialAssessmentICUAndERMapperDao;

    @Override
    public Response saveInitialAssessmentICUAndER(InitialAssessmentICUAndERDTO initialAssessmentICUAndERDTO) {
        try{

            Response getListResponse = iInitialAssessmentICUAndERDao.getInitialAssessmentICUAndERActiveList(initialAssessmentICUAndERDTO.getUnitId(), initialAssessmentICUAndERDTO.getOrganizationId(), initialAssessmentICUAndERDTO.getAdmissionId());
            Response getListResponse1 = iInitialAssessmentICUAndERMapperDao.getInitialAssessmentICUAndERMapperActiveList(initialAssessmentICUAndERDTO.getUnitId(), initialAssessmentICUAndERDTO.getOrganizationId(), initialAssessmentICUAndERDTO.getAdmissionId());

            if(!getListResponse.getListObject().isEmpty()){
                iInitialAssessmentICUAndERDao.inactiveInitialAssessmentICUAndERActiveList(getListResponse.getListObject());
            }
            if(!getListResponse1.getListObject().isEmpty()){
                iInitialAssessmentICUAndERMapperDao.inactiveInitialAssessmentICUAndERMapperActiveList(getListResponse1.getListObject());
            }

            Response  res = iInitialAssessmentICUAndERDao.saveInitialAssessmentICUAndER(initialAssessmentICUAndERDTO); 
            if(initialAssessmentICUAndERDTO.getSelectedKeyValueList().size()>0) {
            	 InitialAssessmentICUAndER initialAssessmentICUAndER =  (InitialAssessmentICUAndER) res.getObject();
            	 initialAssessmentICUAndERDTO.setInitialAssessmentId(initialAssessmentICUAndER.getInitialAssessmentId());
            	iInitialAssessmentICUAndERMapperDao.saveInitialAssessmentICUAndERMapper(initialAssessmentICUAndERDTO);
            }
            return res;
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public Response getInitialAssessmentICUAndERList(Integer unitId, Integer organizationId, Integer admissionId) {
        try{
            return iInitialAssessmentICUAndERDao.getInitialAssessmentICUAndERList(unitId, organizationId, admissionId);
        }catch (Exception e){
            throw e;
        }
    }
}
