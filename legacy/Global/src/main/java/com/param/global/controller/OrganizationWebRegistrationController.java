package com.param.global.controller;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.dto.OrganizationMasterDto;
import com.param.global.service.IUserMasterService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@SuppressWarnings({"rawtypes","unchecked"})
public class OrganizationWebRegistrationController implements ICommonConstants {

    final static Logger logger = Logger.getLogger(OrganizationWebRegistrationController.class);

    @Autowired
    IUserMasterService iUserMasterService;

    @RequestMapping(value = "/createOrganizationFromWeb " , method = RequestMethod.POST , consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response createOrganizationFromWeb(@RequestBody OrganizationMasterDto organizationMasterForWebDto) {
        try {
            iUserMasterService.createOrganizationFromWeb(organizationMasterForWebDto);
            return new Response(SUCCESS, SUCCESS_CODE, null, null, null);

        }catch(java.lang.ClassCastException cce){
            logger.error(cce.getMessage(), cce);
            return new Response(ERROR, ERROR_CODE, null, null, null);

        } catch (Exception e) {
            throw e;

        }
    }

    @RequestMapping(value = "/refreshAllCaches" , method = RequestMethod.GET )
    public Response refreshAllCaches(@RequestParam Integer userId,@RequestParam String userName) {
        try {
            iUserMasterService.refreshAllCaches(userId,userName);
            return new Response(SUCCESS, null, null, null, null);
        } catch (Exception e) {

            throw e;

        }
    }
}
