package com.param.global.unit.service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.IError;
import com.param.global.common.Response;
import com.param.global.unit.dao.IIncidentReportMasterDao;
import com.param.global.unit.dao.IQualityParameterMasterDao;
import com.param.global.unit.dto.QualityParameterMasterDTO;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class QualityParameterMasterServiceImpl implements IQualityParameterMasterService, ICommonConstants, IError {

    @Autowired
    IQualityParameterMasterDao iQualityParameterMasterDao;

    @Override
    public Response saveQualityParameterMaster(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException {
        try {
            if (qualityParameterMasterDTO.getUpdate() != 'Y') {
                iQualityParameterMasterDao.saveQualityParameterMaster(qualityParameterMasterDTO);
            } else {
                this.updateQualityParameterMaster(qualityParameterMasterDTO);
            }
            return new Response<>(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response updateQualityParameterMaster(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException {
        try {
            iQualityParameterMasterDao.updateQualityParameterMaster(qualityParameterMasterDTO);
            return new Response<>(SUCCESS, SUCCESS_CODE, COMMON_UPDATE, null, null);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getQualityParameterMasterById(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException {
        try {
            return iQualityParameterMasterDao.getQualityParameterMasterById(qualityParameterMasterDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response getQualityParameterMasterList(Integer unitId, Integer organizationId) throws ApplicationException {
        try {
            return iQualityParameterMasterDao.getQualityParameterMasterList(unitId, organizationId);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Response updateStatus(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException {
        try {
            return iQualityParameterMasterDao.updateStatus(qualityParameterMasterDTO);
        } catch (Exception e) {
            throw e;
        }
    }
}
