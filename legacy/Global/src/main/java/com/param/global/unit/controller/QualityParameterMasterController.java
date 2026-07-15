package com.param.global.unit.controller;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dto.IncidentReportMasterDto;
import com.param.global.unit.dto.QualityParameterMasterDTO;
import com.param.global.unit.service.IQualityParameterMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@SuppressWarnings({"rawtypes"})
@RequestMapping("/api/unit")
public class QualityParameterMasterController implements ICommonConstants {

    @Autowired
    private IQualityParameterMasterService iQualityParameterMasterService;

    @RequestMapping(value = "/saveQualityParam", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response saveQualityParamMaster(@RequestBody QualityParameterMasterDTO qualityParameterMasterDTO) {
        try {
            return iQualityParameterMasterService.saveQualityParameterMaster(qualityParameterMasterDTO);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getQualityParamList/{unitId}/{organizationId}", method = RequestMethod.GET)
    public Response getQualityParamMasterList(@PathVariable("unitId") Integer unitId, @PathVariable("organizationId") Integer organizationId) {
        try {
            return iQualityParameterMasterService.getQualityParameterMasterList(unitId, organizationId);
        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/getQualityParamById", method = RequestMethod.GET)
    public Response getQualityParamByMaster(@RequestBody QualityParameterMasterDTO qualityParameterMasterDTO) {
        try {
            return iQualityParameterMasterService.getQualityParameterMasterById(qualityParameterMasterDTO);

        } catch (Exception e) {
            throw e;
        }
    }

    @RequestMapping(value = "/updateStatus", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response updateStatus(@RequestBody QualityParameterMasterDTO qualityParameterMasterDTO) {
        try {
            return iQualityParameterMasterService.updateStatus(qualityParameterMasterDTO);
        } catch (Exception e) {
            throw e;
        }
    }
}
