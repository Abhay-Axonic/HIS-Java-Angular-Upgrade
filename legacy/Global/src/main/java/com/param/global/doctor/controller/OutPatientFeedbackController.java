package com.param.global.doctor.controller;


import com.param.global.common.Response;
import com.param.global.doctor.dto.OutPatientFeedbackDTO;
import com.param.global.doctor.service.IOutPatientFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/outPatientFeedback")
public class OutPatientFeedbackController {

    @Autowired
    private IOutPatientFeedbackService iOutPatientFeedbackService;

    @RequestMapping(value = "/saveOutPatientFeedback", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveOutPatientFeedback(@RequestBody OutPatientFeedbackDTO outPatientFeedbackDTO) {
        try {
            return iOutPatientFeedbackService.saveOutPatientFeedback(outPatientFeedbackDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/{unitId}/{organizationId}/{patientId}/{visitType}/{visitTypeId}/{encounterId}", method = RequestMethod.GET)
    public @ResponseBody Response getOutPatientFeedbackList(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId,
                                                            @PathVariable("patientId") Integer patientId, @PathVariable("visitType") String visitType,
                                                            @PathVariable("visitTypeId") Integer visitTypeId,
                                                            @PathVariable("encounterId") Integer encounterId) {
        try {
            return iOutPatientFeedbackService.getOutPatientFeedbackList(unitId, organizationId, patientId, visitType, visitTypeId,encounterId);
        } catch (Exception e) {
            throw e;
        }
    }

}
