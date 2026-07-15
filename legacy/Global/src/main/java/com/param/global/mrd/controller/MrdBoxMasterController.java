package com.param.global.mrd.controller;

import com.param.global.common.Response;
import com.param.global.mrd.dto.BoxFilterDto;
import com.param.global.mrd.dto.BoxRequestDto;
import com.param.global.mrd.service.IMrdBoxMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mrdbox")
public class MrdBoxMasterController {
    @Autowired
    private IMrdBoxMasterService iMrdBoxMasterService;

    @RequestMapping(value = "/saveMrdBox", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveMrdBox(@RequestBody BoxRequestDto boxRequestDto) {
            return iMrdBoxMasterService.saveBoxMaster(boxRequestDto);
    }

    @RequestMapping(value="/getActiveMrdBoxes/{orgId}/{unitId}",method=RequestMethod.GET)
    public Response getActiveMrdBoxes(@PathVariable Integer orgId,
                                     @PathVariable Integer unitId) {
            return iMrdBoxMasterService.getActiveBoxList(orgId,unitId);
    }

    @RequestMapping(value = "/getMrdBoxById/{boxId}/{orgId}/{unitId}", method = RequestMethod.GET)
    public @ResponseBody Response getMrdBoxById(@PathVariable Integer boxId,
                                                    @PathVariable Integer orgId,
                                                 @PathVariable Integer unitId) {
      return iMrdBoxMasterService.getBoxById(boxId,orgId,unitId);
    }

    @RequestMapping(value = "/getMrdBoxList", method = RequestMethod.POST)
    public @ResponseBody Response getMrdBoxList(@RequestBody BoxFilterDto boxFilterDto) {
            return iMrdBoxMasterService.getBoxList(boxFilterDto);
    }

    @RequestMapping(value = "/updateMrdBox", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response updateBoxMaster(@RequestBody BoxRequestDto boxRequestDto) {
            return iMrdBoxMasterService.updateBox(boxRequestDto);
    }

    @RequestMapping(value = "/updateMrdBoxStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response updateMrdBoxStatus(@RequestBody BoxRequestDto boxRequestDto) {
        return iMrdBoxMasterService.updateBoxStatus(boxRequestDto);
    }
}
