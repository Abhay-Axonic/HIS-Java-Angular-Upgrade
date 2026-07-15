package com.param.adt.admission.dao;

import com.param.adt.admission.dto.MallamapattiClassDto;
import com.param.adt.common.Response;
import in.param.exception.ApplicationException;

import java.util.List;

public interface IMallamapattiClassDao {
    void saveMallampattiClass(MallamapattiClassDto mallamapattiClassDto) throws ApplicationException;

    Response getDraftedMallampattiClass(Integer unitId, Integer organizationId, Integer admissionId, Integer encounterId) throws ApplicationException;

    Response inactiveDraftedMallampattiClass(List<MallamapattiClassDto> mallamapattiClassDto) throws ApplicationException;
}
