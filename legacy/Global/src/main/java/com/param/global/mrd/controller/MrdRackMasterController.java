package com.param.global.mrd.controller;

import com.param.global.billing.dto.CurrencyMasterDto;
import com.param.global.common.Response;
import com.param.global.mrd.dto.RackFilterDto;
import com.param.global.mrd.dto.RackRequestDto;
import com.param.global.mrd.service.IMrdRackMasterService;
import com.param.global.unit.dto.ActionMasterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static com.param.global.common.ICommonConstants.ERROR;

@RestController
@RequestMapping("/mrdrack")
public class MrdRackMasterController {
    @Autowired
    private IMrdRackMasterService iMrdRackMasterService;

    @RequestMapping(value = "/saveMrdRack", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveMrdRack(@RequestBody RackRequestDto rackRequestDto) {
            return iMrdRackMasterService.saveRackMaster(rackRequestDto);
    }

    @RequestMapping(value="/getActiveMrdRacks/{orgId}/{unitId}",method=RequestMethod.GET)
    public Response getActiveMrdRacks(@PathVariable Integer orgId,
                                      @PathVariable Integer unitId) {
            return iMrdRackMasterService.getActiveRackList(orgId,unitId);
    }

    @RequestMapping(value = "/getMrdRackById/{rackId}/{orgId}/{unitId}", method = RequestMethod.GET)
    public @ResponseBody Response getMrdRackById(@PathVariable Integer rackId,
                                                    @PathVariable Integer orgId,
                                                 @PathVariable Integer unitId) {
      return iMrdRackMasterService.getRackById(rackId,orgId,unitId);
    }

    @RequestMapping(value = "/getMrdRackList", method = RequestMethod.POST)
    public @ResponseBody Response getMrdRackList(@RequestBody RackFilterDto rackFilterDto) {
            return iMrdRackMasterService.getRackList(rackFilterDto);
    }

    @RequestMapping(value = "/updateMrdRack", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response updateRackMaster(@RequestBody RackRequestDto rackRequestDto) {
            return iMrdRackMasterService.updateRack(rackRequestDto);
    }

    @RequestMapping(value = "/updateMrdRackStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response updateMrdRackStatus(@RequestBody RackRequestDto rackRequestDto) {
        return iMrdRackMasterService.updateRackStatus(rackRequestDto);
    }
}
