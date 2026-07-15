package com.param.global.unit.controller;

import com.param.global.common.Response;
import com.param.global.unit.dto.AnesthesiaAssessmentDTO;
import com.param.global.unit.dto.InitialAssessmentICUAndERDTO;
import com.param.global.unit.service.IAnesthesiaAssessmentService;
import com.param.global.unit.service.IInitialAssessmentICUAndERService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/public")
public class AnesthesiaAssessmentController {

    @Autowired
    private IAnesthesiaAssessmentService iAnesthesiaAssessmentService;

    @RequestMapping(value = "/saveAnesthesiaAssessment", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveAnesthesiaAssessment(@RequestBody AnesthesiaAssessmentDTO anesthesiaAssessmentDTO) {
        try {
            return iAnesthesiaAssessmentService.saveAnesthesiaAssessment(anesthesiaAssessmentDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getAnesthesiaAssessment/{unitId}/{organizationId}/{patientId}", method = RequestMethod.GET)
    public @ResponseBody Response getAnesthesiaAssessment(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                                   @PathVariable("patientId") Integer patientId) {
        try {
            return iAnesthesiaAssessmentService.getAnesthesiaAssessment(unitId, organizationId, patientId);
        } catch (Exception e) {
            throw e;
        }
    }

}
