package com.param.global.unit.service;

import com.param.global.common.Response;
import com.param.global.unit.dto.IncidentReportMasterDto;
import com.param.global.unit.dto.QualityParameterMasterDTO;
import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

@SuppressWarnings("rawtypes")
@Transactional
public interface IQualityParameterMasterService {

    public Response saveQualityParameterMaster(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException;

    public Response updateQualityParameterMaster(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException;

    public Response getQualityParameterMasterById(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException;

    public Response getQualityParameterMasterList(Integer unitId, Integer organizationId) throws ApplicationException;

    public Response updateStatus(QualityParameterMasterDTO qualityParameterMasterDTO) throws ApplicationException;

}
