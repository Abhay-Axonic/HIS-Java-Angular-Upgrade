package com.param.adt.master.unit.dao;

import com.param.adt.master.unit.dto.DoctorFavouriteMedicationsMasterDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes","unchecked"})
public interface IDoctorFavouriteMedicationsMasterDao {

    Response saveDoctorFavouriteMedication(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException;
    Response checkIsExist(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException;

    Response removeFromFavourites(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException;

    Response updateDoctorFavouriteMedication(DoctorFavouriteMedicationsMasterDto favMedicationDto)throws ApplicationException;

    Response getDoctorFavouriteMedication(DoctorFavouriteMedicationsMasterDto favMedicationDto)throws ApplicationException;

    Response getDoctorFavouriteMedicationForCoversheet(DoctorFavouriteMedicationsMasterDto favMedicationDto)throws ApplicationException;
}
