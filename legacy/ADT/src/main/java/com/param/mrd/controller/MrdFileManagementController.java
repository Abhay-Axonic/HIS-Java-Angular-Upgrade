package com.param.mrd.controller;

import com.param.global.common.Response;
import com.param.mrd.dto.MrdFileManagementDto;
import com.param.mrd.dto.MrdFileStatusUpdateDto;
import com.param.mrd.service.IMrdFileManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.param.adt.common.ICommonConstants.COMMON_ERROR_MESSAGE;
import static com.param.adt.common.ICommonConstants.ERROR;
import static com.param.global.common.ICommonConstants.ERROR_CODE;

@RestController
@RequestMapping("/adt/mrd")
public class MrdFileManagementController {

    @Autowired
    private IMrdFileManagementService iMrdFileManagementService;

    @RequestMapping(value = "/getEncounterNo/{patientId}/{visitType}", method = RequestMethod.GET)
    public @ResponseBody Response getEncounterNo(@PathVariable Integer patientId,
                                                 @PathVariable String visitType) {
        try {
            return iMrdFileManagementService.searchNumberByPatientIdAndType(visitType,patientId);
        } catch (Exception e) {

        }
        return new Response(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
    }

    @RequestMapping(value = "/saveMrdAllocation", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveMrdAllocation(@RequestBody MrdFileManagementDto mrdDto) {

        try
        {
        Response   response = iMrdFileManagementService.saveFileAllocation(mrdDto);
            return response;
        } catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
    }

    @RequestMapping(value = "/updateMrdAllocation", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response updateMrdAllocation(@RequestBody MrdFileManagementDto mrdDto) {
//        try
//        {
        System.out.println("in updateMrdAllocation");

            return iMrdFileManagementService.updateMrdAllocation(mrdDto);
//        } catch (Exception e)
//        {
//            throw e;
//        }
    }

    @RequestMapping(value = "/getMrdFiles/{patientId}/{unitId}/{organizationId}/{status}",method = RequestMethod.GET)
    public @ResponseBody Response getMrdFiles(@PathVariable Integer patientId,
                                              @PathVariable Integer unitId,
                                              @PathVariable Integer organizationId,
                                              @PathVariable String status){
        try {
        System.out.println("in mrd files");
            return iMrdFileManagementService.getMrdFiles(patientId,unitId,organizationId,status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

    @RequestMapping(value = "/updateMrdAllocationStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response updateMrdAllocationStatus(@RequestBody List<MrdFileStatusUpdateDto> mrdDto) {
        try
        {
            return iMrdFileManagementService.updateMrdAllocationStatus(mrdDto);
        } catch (Exception e)
        {
            throw e;
        }
    }
    @RequestMapping(value = "/validateMrdUser/{userId}/{unitId}/{organizationId}",method = RequestMethod.GET)
    public @ResponseBody Response validateMrdUser(@PathVariable Integer userId,
                                                  @PathVariable Integer unitId,
                                                  @PathVariable Integer organizationId){
        try {
            return iMrdFileManagementService.validateMrdUser(userId,unitId,organizationId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Response(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }
}
