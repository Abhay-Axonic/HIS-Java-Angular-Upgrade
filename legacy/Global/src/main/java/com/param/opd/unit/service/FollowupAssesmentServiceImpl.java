package com.param.opd.unit.service;

import com.param.global.common.Response;
import com.param.global.dto.FollowupAssesmentDto;
import com.param.opd.unit.dao.IFollowupAssesmentDao;
import com.param.utility.TimeZoneComponent;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class FollowupAssesmentServiceImpl implements IFollowupAssesmentService{

    @Autowired
    IFollowupAssesmentDao iFollowupAssesmentDao;


    @Override
    @Transactional
    public Response saveFollowUpAssesment(FollowupAssesmentDto followupAssesmentDto) throws ApplicationException {
        followupAssesmentDto.setCreatedDate(TimeZoneComponent.getDateByzone(followupAssesmentDto.getUnitId()));
        followupAssesmentDto.setUpdatedDate(TimeZoneComponent.getDateByzone(followupAssesmentDto.getUnitId()));
        return iFollowupAssesmentDao.saveFollowUpAssesment(followupAssesmentDto);
    }

    @Override
    @Transactional
    public Response getFollowUpAssessmentList(FollowupAssesmentDto followupAssesmentDto) throws ApplicationException {
        return iFollowupAssesmentDao.getFollowUpAssessmentList(followupAssesmentDto);
    }
}
