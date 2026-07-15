package com.param.adt.admission.controller;

import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.admission.service.INutritionalAssessmentScoreService;
import com.param.adt.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
@SuppressWarnings("rawtypes")
@Controller
@RequestMapping("/adt")
public class NutritionalAssessmentScoreController {

    @Autowired
    private INutritionalAssessmentScoreService iNutritionalAssessmentScoreService;

   
	@RequestMapping(value = "/saveAsDraftNutritionalAssessmentScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveAsDraftNutritionalAssessmentScore(@RequestBody NutritionalAssessmentScoreDTO nutritionalAssessmentScoreDTO) {
        try {
                return iNutritionalAssessmentScoreService.saveNutritionalAssessmentScore(nutritionalAssessmentScoreDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getSaveAsDraftNutritionalAssessmentScore/{unitId}/{organizationId}/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody Response getSaveAsDraftNutritionalAssessmentScore(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                          @PathVariable("admissionId") Integer admissionId) {
        try {
            return iNutritionalAssessmentScoreService.getSaveAsDraftNutritionalAssessmentScore(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getNutritionalIntailAnethesthiaAssessmentScore/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody com.param.global.common.Response getSaveAsDraftNutritionalAssessmentScore(@PathVariable("admissionId") Integer admissionId) {
        try {
            return iNutritionalAssessmentScoreService.getNutritionalIntailAnethesthiaAssessmentScore(admissionId);
        } catch (Exception e) {
            throw e;
        }
    }
}
