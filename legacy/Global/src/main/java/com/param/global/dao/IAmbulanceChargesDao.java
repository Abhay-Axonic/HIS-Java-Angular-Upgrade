package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.AmbulanceChargesDto;
import com.param.global.model.AmbulanceCharges;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IAmbulanceChargesDao  extends IGenericDao<AmbulanceCharges, Integer>{

	public Response saveAmbulanceCharges(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException;

	public Response getAmbulanceChargesList(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException;

	public Response getAmbulanceChargesActiveList(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException;

	public Response getSingleAmbulanceCharges(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException;

	public Response updateAmbulanceCharges(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException;

	public Response changeAmbulanceChargesStatus(AmbulanceChargesDto ambulanceChargesDto) throws ApplicationException;

	public Response cancelOrder(AmbulanceChargesDto ambulanceChargesDto)throws ApplicationException;
	
	
	
}
