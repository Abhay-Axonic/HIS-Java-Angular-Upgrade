package com.param.adt.master.unit.service;

import com.param.adt.master.unit.dto.DoctorFavouriteMedicationsMasterDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

import javax.transaction.Transactional;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IDoctorFavouriteMedicationsMasterService {
    Response saveDoctorFavouriteMedication(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException;

    Response removeFromFavourites(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException;

    Response updateDoctorFavouriteMedication(DoctorFavouriteMedicationsMasterDto favMedicationDto)throws ApplicationException;

    Response getDoctorFavouriteMedication(DoctorFavouriteMedicationsMasterDto favMedicationDto)throws ApplicationException;

    Response getDoctorFavouriteMedicationForCoversheet(DoctorFavouriteMedicationsMasterDto favMedicationDto)throws ApplicationException;
}
