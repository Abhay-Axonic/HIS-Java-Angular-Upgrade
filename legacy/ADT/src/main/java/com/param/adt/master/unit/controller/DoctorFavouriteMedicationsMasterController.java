package com.param.adt.master.unit.controller;

import com.param.global.mrd.dto.RackRequestDto;
import com.param.global.mrd.service.IMrdRackMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.param.adt.master.unit.dto.DoctorFavouriteMedicationsMasterDto;
import com.param.adt.master.unit.service.IDoctorFavouriteMedicationsMasterService;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

@RestController
@RequestMapping("/api/unit/clinical")
public class DoctorFavouriteMedicationsMasterController {

    @Autowired
    private IDoctorFavouriteMedicationsMasterService IDoctorFavouriteMedicationsMasterService;

    @RequestMapping(value = "/saveDoctorFavouriteMedication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response saveDoctorFavouriteMedication(@RequestBody DoctorFavouriteMedicationsMasterDto favMedicationDto) {
        return IDoctorFavouriteMedicationsMasterService.saveDoctorFavouriteMedication(favMedicationDto);
    }

    @RequestMapping(value = "/removeFromDoctorFavouritesMedication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response removeFromDoctorFavouritesMedication(@RequestBody DoctorFavouriteMedicationsMasterDto favMedicationDto) {
        return IDoctorFavouriteMedicationsMasterService.removeFromFavourites(favMedicationDto);
    }

    @RequestMapping(value = "/updateDoctorFavouriteMedication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response updateDoctorFavouriteMedication(@RequestBody DoctorFavouriteMedicationsMasterDto favMedicationDto) {
        return IDoctorFavouriteMedicationsMasterService.updateDoctorFavouriteMedication(favMedicationDto);
    }

    @RequestMapping(value = "/getDoctorFavouriteMedication", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response getDoctorFavouriteMedication(@RequestBody DoctorFavouriteMedicationsMasterDto favMedicationDto) {
        return IDoctorFavouriteMedicationsMasterService.getDoctorFavouriteMedication(favMedicationDto);
    }

    @RequestMapping(value = "/getDoctorFavouriteMedicationForCoversheet", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody Response getDoctorFavouriteMedicationForCoversheet(@RequestBody DoctorFavouriteMedicationsMasterDto favMedicationDto) {
        return IDoctorFavouriteMedicationsMasterService.getDoctorFavouriteMedicationForCoversheet(favMedicationDto);
    }

}
