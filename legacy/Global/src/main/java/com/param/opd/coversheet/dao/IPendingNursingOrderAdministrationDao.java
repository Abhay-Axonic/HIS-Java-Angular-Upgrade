package com.param.opd.coversheet.dao;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.PendingNursingOrderAdministrationDto;

import in.param.exception.ApplicationException;

public interface IPendingNursingOrderAdministrationDao {

	Response saveActualAdministrationTime(PendingNursingOrderAdministrationDto pendingNursingOrderAdministrationDto)throws ApplicationException ;

	Response getPendingNurOrdListByPatientId(PendingNursingOrderAdministrationDto pendingNursingOrderAdministrationDto) throws ApplicationException ;

}
