package com.param.lis.transaction.dao;

import com.param.lis.global.common.Response;
import org.springframework.web.bind.annotation.RequestBody;
import com.param.entity.lis.transaction.ClinicalInterventionProcedure;
import com.param.lis.global.common.ClinicalInterventionProcedureDto;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

public interface IClinicalInterventionProcedureDao extends IGenericDao<ClinicalInterventionProcedure, Integer>{
    public Response saveIntervention(ClinicalInterventionProcedureDto clinicalInterventionProcedureDto) throws ApplicationException;

    public Response getInterventionDetails(ClinicalInterventionProcedureDto interventionDto) throws ApplicationException;

    public Response updateInterventionForCompleteProcedure(ClinicalInterventionProcedureDto clinicalInterventionProcedureDto) throws ApplicationException;
}
