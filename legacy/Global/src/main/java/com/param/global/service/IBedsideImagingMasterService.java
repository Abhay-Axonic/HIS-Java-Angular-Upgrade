package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.BedsideImagingMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
@Transactional
public interface IBedsideImagingMasterService {

	public Response saveBedsideImagingMaster(BedsideImagingMasterDto bedsideImagingMasterDto) throws ApplicationException;

	public Response updateBedsideImagingMaster(BedsideImagingMasterDto bedsideImagingMasterDto) throws ApplicationException;

	public Response updateBedsideImagingMasterStatus(BedsideImagingMasterDto bedsideImagingMasterDto) throws ApplicationException;

	public Response getBedsideImagingMasterList(BedsideImagingMasterDto bedsideImagingMasterDto) throws ApplicationException;

	public Response getActiveBedsideImagingMasterList(BedsideImagingMasterDto bedsideImagingMasterDto) throws ApplicationException;

	public Response getBedsideImagingMasterByName(BedsideImagingMasterDto bedsideImagingMasterDto) throws ApplicationException;

	public Response getBedsideImagingMasterByNameNotId(BedsideImagingMasterDto bedsideImagingMasterDto) throws ApplicationException;



}
