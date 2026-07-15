package com.param.opd.unit.dao;

import com.param.global.common.Response;

import com.param.global.dto.FollowupAssesmentDto;
import in.param.exception.ApplicationException;

public interface IFollowupAssesmentDao {
    Response saveFollowUpAssesment(FollowupAssesmentDto followupAssesmentDto) throws ApplicationException;
    Response getFollowUpAssessmentList(FollowupAssesmentDto followupAssesmentDto)throws ApplicationException;

}
