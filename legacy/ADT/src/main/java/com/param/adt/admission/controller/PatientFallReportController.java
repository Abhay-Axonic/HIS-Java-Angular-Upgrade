package com.param.adt.admission.controller;

import com.param.adt.admission.dto.NutritionalAssessmentScoreDTO;
import com.param.adt.admission.dto.PatientFallReportDto;
import com.param.adt.admission.service.INutritionalAssessmentScoreService;
import com.param.adt.admission.service.IPatientFallReportService;
import com.param.adt.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/adt")
public class PatientFallReportController {

    @Autowired
    private IPatientFallReportService iPatientFallReportService;

    @RequestMapping(value = "/saveAsDraftPatientFallReporting", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveAsDraftPatientFallReporting(@RequestBody PatientFallReportDto patientFallReportDto) {
        try {
                return iPatientFallReportService.saveAsDraftPatientFallReport(patientFallReportDto);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getSaveAsDraftPatientFallReporting/{unitId}/{organizationId}/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody Response getSaveAsDraftPatientFallReporting(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                                           @PathVariable("admissionId") Integer admissionId) {
        try {
            return iPatientFallReportService.getSaveAsDraftPatientFallReport(unitId, organizationId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

}
