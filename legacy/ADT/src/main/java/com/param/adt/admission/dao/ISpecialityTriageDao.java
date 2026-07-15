package com.param.adt.admission.dao;

import com.param.adt.common.Response;
import com.param.global.dto.AdmissionDto;
import in.param.exception.ApplicationException;

public interface ISpecialityTriageDao {
    public Response saveTriageDetails(AdmissionDto dto) throws ApplicationException;
}
