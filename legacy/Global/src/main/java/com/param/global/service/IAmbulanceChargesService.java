package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.AmbulanceChargesDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IAmbulanceChargesService {

	
	@Transactional
	Response saveAmbulanceCharges(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException;

	@Transactional
	Response getAmbulanceChargesList(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException;

	@Transactional
	Response getAmbulanceChargesActiveList(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException;

	@Transactional
	Response getSingleAmbulanceCharges(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException;

	@Transactional
	Response updateAmbulanceCharges(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException;

	@Transactional
	Response changeAmbulanceChargesStatus(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException;

	@Transactional
	Response cancelOrder(AmbulanceChargesDto ambulanceChargesDto)throws ApplicationException;

}
