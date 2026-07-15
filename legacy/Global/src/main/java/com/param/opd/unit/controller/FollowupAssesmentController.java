package com.param.opd.unit.controller;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.FollowupAssesmentDto;
import com.param.opd.unit.service.IFollowupAssesmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@RestController
@RequestMapping("api/opd")
public class FollowupAssesmentController implements ICommonConstants {
	final static Logger logger = Logger.getLogger(FollowupAssesmentController.class);


    @Autowired
    IFollowupAssesmentService iFollowupAssesmentService;

    @RequestMapping(value="/saveFollowUpEassessment", method= RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveFollowUpEassessment(@RequestBody FollowupAssesmentDto followupAssesmentDto) {
        try
        {
            return iFollowupAssesmentService.saveFollowUpAssesment(followupAssesmentDto);

        }catch(Exception e)
        {
        logger.error("method failed", e);
        }
        return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }


    @RequestMapping(value="/getFollowUpassessmentList", method= RequestMethod.POST, consumes= MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response getFollowUpassessmentList(@RequestBody FollowupAssesmentDto followupAssesmentDto) {
        try
        {
            return iFollowupAssesmentService.getFollowUpAssessmentList(followupAssesmentDto);

        }catch(Exception e)
        {
        logger.error("method failed", e);
        }
        return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }
}
