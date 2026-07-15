package com.param.opd.unit.controller;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.AbcdeAssessmentDto;
import com.param.global.dto.ERPatientDashboard;
import com.param.opd.unit.service.IAbcdeAssessmentService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/opd")
public class ABCDEassesmentController  implements ICommonConstants {

    private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(ABCDEassesmentController.class);

    @Autowired
    private IAbcdeAssessmentService iAbcdeAssessmentService;

    @RequestMapping(value="/saveABCDEassessment", method= RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveABCDEassessment(@RequestBody AbcdeAssessmentDto abcdeAssessmentDto) {
        try
        {
            return iAbcdeAssessmentService.saveABCDAssesment(abcdeAssessmentDto);

        }catch(Exception e)
        {
            logger.error("saveABCDEassessment failed", e);
        }
        return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }


    @RequestMapping(value="/getABCDEassessmentList", method= RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response getABCDEassessmentList(@RequestBody AbcdeAssessmentDto abcdeAssessmentDto) {
        try
        {
            return iAbcdeAssessmentService.getABCDEAssessmentList(abcdeAssessmentDto);

        }catch(Exception e)
        {
            logger.error("getABCDEassessmentList failed", e);
        }
        return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

    @RequestMapping(value="/getERPatientDashBoardData", method= RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response getERPatientDashBoardData(@RequestBody ERPatientDashboard eRPatientDashboard) {
        try
        {
            return iAbcdeAssessmentService.getERPatientDashBoardData(eRPatientDashboard);

        }catch(Exception e)
        {
            logger.error("getERPatientDashBoardData failed", e);
        }
        return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }
}
