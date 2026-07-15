package com.param.billing.patientType.Controller;

import com.param.billing.patientType.dto.*;
import com.param.billing.patientType.service.IFranchiseShareService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api/billing/franchiseshare")
public class FranchiseShareController implements ICommonConstants {

    @Autowired
    private IFranchiseShareService iFranchiseShareService;

    @RequestMapping(value = "/unit" , method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response getShareUnit(@RequestBody FranchiseShareDto dto){
        return iFranchiseShareService.getShareUnit(dto);

    }

    @RequestMapping(value = "", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response saveFranchiseShare(@RequestBody FranchiseShareDto franchiseShareDto){
            return iFranchiseShareService.saveFranchiseShare(franchiseShareDto);
    }

    @RequestMapping(value = "/unit/{unitId}")
    public Response getFranchiseShareByUnit(@PathVariable("unitId") int unitId){
            return iFranchiseShareService.getFranchiseShareByUnit(unitId);
    }

    @RequestMapping(value = "/statusGroupWise" , method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response updateStatusGroupWise(@RequestBody FranchiseShareDetailsDto dto){
            return iFranchiseShareService.updateStatusGroupWise(dto);
    }

   @RequestMapping(value = "/statusUnitWise" , method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response updateStatusUnitWise(@RequestBody FranchiseShareDto dto){
            return iFranchiseShareService.updateStatusUnitWise(dto);

    }
}
