package com.param.global.unit.controller;

import com.param.global.common.Response;
import com.param.global.unit.dto.InitialAssessmentICUAndERDTO;
import com.param.global.unit.service.IInitialAssessmentICUAndERService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/public")
public class InitialAssessmentICUAndERController {

    @Autowired
    private IInitialAssessmentICUAndERService iInitialAssessmentICUAndERService;

    @RequestMapping(value = "/saveInitialAssessmentICUAndER", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveInitialAssessmentICUAndER(@RequestBody InitialAssessmentICUAndERDTO initialAssessmentICUAndERDTO) {
        try {
            return iInitialAssessmentICUAndERService.saveInitialAssessmentICUAndER(initialAssessmentICUAndERDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getInitialAssessmentICUAndERList/{unitId}/{organizationId}/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody Response getInitialAssessmentICUAndERList(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                                           @PathVariable("admissionId") Integer admissionId) {
        try {
            return iInitialAssessmentICUAndERService.getInitialAssessmentICUAndERList(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

}
