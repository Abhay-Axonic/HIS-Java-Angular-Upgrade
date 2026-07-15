package com.param.global.mrd.controller;

import com.param.global.common.Response;
import com.param.global.mrd.dto.RowFilterDto;
import com.param.global.mrd.dto.RowRequestDto;
import com.param.global.mrd.service.IMrdRowMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mrdrow")
public class MrdRowMasterController {
    @Autowired
    private IMrdRowMasterService iMrdRowMasterService;

    @RequestMapping(value = "/saveMrdRow", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveMrdRow(@RequestBody RowRequestDto rowRequestDto) {
            return iMrdRowMasterService.saveRowMaster(rowRequestDto);
    }

    @RequestMapping(value="/getActiveMrdRows/{orgId}/{unitId}",method=RequestMethod.GET)
    public Response getActiveMrdRows(@PathVariable Integer orgId,
                                     @PathVariable Integer unitId) {
            return iMrdRowMasterService.getActiveRowList(orgId,unitId);
    }

    @RequestMapping(value = "/getMrdRowById/{rowId}/{orgId}/{unitId}", method = RequestMethod.GET)
    public @ResponseBody Response getMrdRowById(@PathVariable Integer rowId,
                                                    @PathVariable Integer orgId,
                                                 @PathVariable Integer unitId) {
      return iMrdRowMasterService.getRowById(rowId,orgId,unitId);
    }

    @RequestMapping(value = "/getMrdRowList", method = RequestMethod.POST)
    public @ResponseBody Response getMrdRowList(@RequestBody RowFilterDto rowFilterDto) {
            return iMrdRowMasterService.getRowList(rowFilterDto);
    }

    @RequestMapping(value = "/updateMrdRow", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response updateRoomMaster(@RequestBody RowRequestDto rowRequestDto) {
            return iMrdRowMasterService.updateRow(rowRequestDto);
    }

    @RequestMapping(value = "/updateMrdRowStatus", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response updateMrdRowStatus(@RequestBody RowRequestDto rowRequestDto) {
        return iMrdRowMasterService.updateRowStatus(rowRequestDto);
    }
}
