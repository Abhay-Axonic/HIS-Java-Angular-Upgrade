package com.param.opd.unit.service;

import com.param.global.common.Response;
import com.param.global.dto.FollowupAssesmentDto;
import in.param.exception.ApplicationException;

public interface IFollowupAssesmentService {
    Response saveFollowUpAssesment(FollowupAssesmentDto followupAssesmentDto) throws ApplicationException;
    Response getFollowUpAssessmentList(FollowupAssesmentDto followupAssesmentDto)throws ApplicationException;

}
