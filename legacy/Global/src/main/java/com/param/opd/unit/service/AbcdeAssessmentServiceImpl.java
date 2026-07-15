package com.param.opd.unit.service;

import com.param.global.common.Response;
import com.param.global.dto.AbcdeAssessmentDto;
import com.param.global.dto.ERPatientDashboard;
import com.param.opd.unit.dao.IAbcdeAssessmentDao;
import com.param.utility.TimeZoneComponent;
import in.param.exception.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class AbcdeAssessmentServiceImpl implements IAbcdeAssessmentService {

    @Autowired
    private IAbcdeAssessmentDao iAbcdeAssessmentDao;

    @Override
    @Transactional
    public Response saveABCDAssesment(AbcdeAssessmentDto abcdeAssessmentDto) throws ApplicationException {
        abcdeAssessmentDto.setCreatedDate(TimeZoneComponent.getDateByzone(abcdeAssessmentDto.getUnitId()));
        abcdeAssessmentDto.setUpdatedDate(TimeZoneComponent.getDateByzone(abcdeAssessmentDto.getUnitId()));
        return iAbcdeAssessmentDao.saveABCDAssesment(abcdeAssessmentDto);
    }

    @Override
    @Transactional
    public Response getABCDEAssessmentList(AbcdeAssessmentDto abcdeAssessmentDto) throws ApplicationException {
        return iAbcdeAssessmentDao.getABCDEAssessmentList(abcdeAssessmentDto);
    }

    @Override
    @Transactional
    public Response getERPatientDashBoardData(ERPatientDashboard eRPatientDashboard) throws ApplicationException {
        return iAbcdeAssessmentDao.getERPatientDashBoardData(eRPatientDashboard);
    }


}
