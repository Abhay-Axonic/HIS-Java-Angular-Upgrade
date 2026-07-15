package com.param.adt.master.unit.service;

import com.param.adt.master.unit.dao.DoctorFavouriteMedicationsMasterDaoImpl;
import com.param.global.common.ICommonConstants;
import com.param.opd.coversheet.dto.DoctorFavoritesDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import com.param.adt.master.unit.dao.IDoctorFavouriteMedicationsMasterDao;
import com.param.adt.master.unit.dto.DoctorFavouriteMedicationsMasterDto;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;

import java.util.ListIterator;


@Service
@SuppressWarnings({"rawtypes"})
public class DoctorFavouriteMedicationsMasterServiceImpl implements IDoctorFavouriteMedicationsMasterService, ICommonConstants {

    @Autowired
    private IDoctorFavouriteMedicationsMasterDao iDoctorFavouriteMedicationsMasterDao;
    @Transactional
    @Override
    public Response saveDoctorFavouriteMedication(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException {
        try {
            if(favMedicationDto.getStatus().equals('A'))
            {
                Response res = iDoctorFavouriteMedicationsMasterDao.checkIsExist(favMedicationDto);
                if(res.getListObject().size()>0){
                    ListIterator<DoctorFavoritesDto> iterator= res.getListObject().listIterator();
                    while(iterator.hasNext())
                    {
//                        DoctorFavoritesDto favoritesDto = iterator.next();

                        return new Response(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
                    }
                }else{
                    return iDoctorFavouriteMedicationsMasterDao.saveDoctorFavouriteMedication(favMedicationDto);
                }
            }else
            {
                return iDoctorFavouriteMedicationsMasterDao.removeFromFavourites(favMedicationDto);
            }

        }catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
        return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
    }

    @Transactional
    @Override
    public Response removeFromFavourites(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException {
        try {
                return iDoctorFavouriteMedicationsMasterDao.removeFromFavourites(favMedicationDto);
        }catch(Exception e){
		throw new ApplicationException(e.getMessage(), e);
	}
        
    }

    @Override
    public Response updateDoctorFavouriteMedication(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException {
        return iDoctorFavouriteMedicationsMasterDao.updateDoctorFavouriteMedication(favMedicationDto);

    }

    @Override
    public Response getDoctorFavouriteMedication(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException {
        return iDoctorFavouriteMedicationsMasterDao.getDoctorFavouriteMedication(favMedicationDto);
    }

    @Override
    public Response getDoctorFavouriteMedicationForCoversheet(DoctorFavouriteMedicationsMasterDto favMedicationDto) throws ApplicationException {
        return iDoctorFavouriteMedicationsMasterDao.getDoctorFavouriteMedicationForCoversheet(favMedicationDto);
    }
}
