package com.param.adt.admission.controller;


import com.param.adt.admission.dto.HandHygieneScoreDTO;
import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.admission.service.IHandHygieneScoreService;
import com.param.adt.admission.service.INutritionalAssessmentScoreService;
import com.param.adt.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/adt")
public class HandHygieneScoreController {

    @Autowired
    private IHandHygieneScoreService iHandHygieneScoreService;

    @RequestMapping(value = "/saveAsDraftHandHygieneScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveAsDraftHandHygieneScore(@RequestBody HandHygieneScoreDTO handHygieneScoreDTO) {
        try {
            if(handHygieneScoreDTO.getSaveAsDraft()=='Y') {
                return iHandHygieneScoreService.saveAsDraftHandHygieneScore(handHygieneScoreDTO);
            }else {
                return iHandHygieneScoreService.saveHandHygieneScore(handHygieneScoreDTO);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getSaveAsDraftHandHygieneScore/{unitId}/{organizationId}/{wardId}", method = RequestMethod.GET)
    public @ResponseBody Response getSaveAsDraftNutritionalAssessmentScore(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                                           @PathVariable("wardId") Integer wardId) {
        try {
            return iHandHygieneScoreService.getSaveAsDraftHandHygieneScore(unitId, organizationId, wardId);
        } catch (Exception e) {
            throw e;
        }
    }

}
