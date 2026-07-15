package com.param.billing.global.transaction.dao;

import com.param.global.common.Response;
import com.param.global.dto.FreeTextOrderedServicesDto;

import in.param.exception.ApplicationException;

public interface IFreeTextInvestigationsDAO {

	Response saveFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)throws ApplicationException;

	Response getFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)throws ApplicationException;

	Response searchFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)throws ApplicationException;

	Response cancelFreeTextInvestigations(FreeTextOrderedServicesDto freeTextOrderedServicesDto)throws ApplicationException;

}
