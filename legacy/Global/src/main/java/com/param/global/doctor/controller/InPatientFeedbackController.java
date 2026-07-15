package com.param.global.doctor.controller;


import com.param.global.common.Response;
import com.param.global.doctor.dto.OutPatientFeedbackDTO;
import com.param.global.doctor.service.IInPatientFeedbackService;
import com.param.global.doctor.service.IOutPatientFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/inPatientFeedback")
public class InPatientFeedbackController {

    @Autowired
    private IInPatientFeedbackService iInPatientFeedbackService;

    @RequestMapping(value = "/saveInPatientFeedback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveInPatientFeedback(@RequestBody OutPatientFeedbackDTO outPatientFeedbackDTO) {
        try {
            return iInPatientFeedbackService.saveInPatientFeedback(outPatientFeedbackDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/{unitId}/{organizationId}/{patientId}/{visitType}/{visitTypeId}/{admissionId}", method = RequestMethod.GET)
    public @ResponseBody Response getInPatientFeedbackList(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                            @PathVariable("patientId") Integer patientId, @PathVariable("visitType") String visitType,
                                                            @PathVariable("visitTypeId") Integer visitTypeId,  @PathVariable("admissionId") Integer admissionId) {
        try {
            return iInPatientFeedbackService.getInPatientFeedbackList(unitId, organizationId, patientId, visitType, visitTypeId, admissionId);
        } catch (Exception e) {
            throw e;
        }
    }

}
