package com.param.opd.unit.dao;


import com.param.global.common.Response;
import com.param.global.dto.AbcdeAssessmentDto;
import com.param.global.dto.ERPatientDashboard;
import in.param.exception.ApplicationException;

public interface IAbcdeAssessmentDao {
    Response saveABCDAssesment(AbcdeAssessmentDto abcdeAssessmentDto) throws ApplicationException;
    Response getABCDEAssessmentList(AbcdeAssessmentDto abcdeAssessmentDto)throws ApplicationException;

    Response getERPatientDashBoardData(ERPatientDashboard eRPatientDashboard) throws ApplicationException;
}
