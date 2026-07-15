package com.param.adt.admission.controller;

import com.param.adt.admission.dto.HandHygieneScoreDTO;
import com.param.adt.admission.dto.LaboratoryInspectionListDTO;
import com.param.adt.admission.dto.LaboratoryInspectionScoreDTO;
import com.param.adt.admission.model.LaboratoryInspectionScore;
import com.param.adt.admission.service.ILaboratoryInspectionScoreService;
import com.param.adt.admission.service.INutritionalAssessmentScoreService;
import com.param.adt.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/adt")
public class LaboratoryInspectionScoreController {

    @Autowired
    private ILaboratoryInspectionScoreService iLaboratoryInspectionScoreService;

    @RequestMapping(value = "/saveLabInspectionScore", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveAsDraftHandHygieneScore(@RequestBody LaboratoryInspectionScoreDTO laboratoryInspectionScoreDTO) {
        try {
            return iLaboratoryInspectionScoreService.saveAsDraftLaboratoryInspectionScore(laboratoryInspectionScoreDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getSaveAsDraftLabInspectionScore/{unitId}/{organizationId}/{departmentId}", method = RequestMethod.GET)
    public @ResponseBody Response getSaveAsDraftLabInspectionScore(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                                   @PathVariable("departmentId") Integer departmentId) {
        try {
            return iLaboratoryInspectionScoreService.getSaveAsDraftLaboratoryInspectionScore(unitId, organizationId, departmentId);
        } catch (Exception e) {
            throw e;
        }
    }

}
