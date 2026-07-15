package com.param.adt.admission.dao;

import com.param.adt.admission.model.AdmittedPatientOrdersWorkbench;
import com.param.adt.common.Response;
import com.param.global.dto.AdmissionDto;
import in.param.exception.ApplicationException;

public interface IAdmittedPatientOrdersWorkbench {

   public AdmittedPatientOrdersWorkbench savePatientOrdersWorkbench(AdmittedPatientOrdersWorkbench admittedPatientOrdersWorkbench) throws ApplicationException;
   public Response checkPatientOrdersWorkbenchExist(AdmissionDto admissionDto) throws ApplicationException;
   public AdmittedPatientOrdersWorkbench updatePatientOrdersWorkbench(AdmittedPatientOrdersWorkbench admittedPatientOrdersWorkbench) throws ApplicationException;
}
