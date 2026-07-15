package com.param.opd.coversheet.controller;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.opd.coversheet.dto.PatientBirthHistoryDTO;
import com.param.opd.coversheet.dto.PatientTravelHistoryDetailsDto;
import com.param.opd.coversheet.service.IPatientBirthHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/*FUNCTIONALITY : Birth HISTORY
DATE:05/12/2023
CREATED BY: Sib10*/
@RestController
@RequestMapping("/api/opd")
@SuppressWarnings({"rawtypes", "unchecked"})
public class PatientBirthHistoryController implements ICommonConstants {

    @Autowired
    private IPatientBirthHistoryService iPatientBirthHistoryService;

    @RequestMapping(value = "/savePatientBirthHistory", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response savePatientBirthHistory(@RequestBody PatientBirthHistoryDTO patientBirthHistoryDTO) {
        try {
            return iPatientBirthHistoryService.savePatientBirthHistory(patientBirthHistoryDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getPatientBirthHistory/{patientId}", method = RequestMethod.GET)
    public Response getPatientBirthHistory(@PathVariable("patientId") Integer patientId) {
        try {
            return iPatientBirthHistoryService.getPatientBirthHistory(patientId);
        } catch (Exception e) {
            throw e;
        }
    }

}
