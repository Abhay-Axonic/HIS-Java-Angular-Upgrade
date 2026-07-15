package com.param.global.unit.dao;

import com.param.global.common.Response;
import com.param.global.unit.dto.QualityParameterMasterDTO;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IQualityParameterMasterDao {

    public Response saveQualityParameterMaster(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException;

    public Response updateQualityParameterMaster(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException;

    public Response getQualityParameterMasterById(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException;

    public Response getQualityParameterMasterList(Integer unitId, Integer organizationId) throws ApplicationException;

    public Response updateStatus(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException;

}
