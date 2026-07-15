package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodTestResultMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({"rawtypes"})

public interface IBloodTestResultMasterService {

	public Response saveBloodTestResultMaster(BloodTestResultMasterDto bloodTestResultMasterDto) throws ApplicationException;
	
	public Response updateBloodTestResultMaster(BloodTestResultMasterDto bloodTestResultMasterDto) throws ApplicationException;
	
	public Response updateBloodTestResultMasterStatus(BloodTestResultMasterDto bloodTestResultMasterDto) throws ApplicationException;
	
	public Response getBloodTestResultMasterList(BloodTestResultMasterDto bloodTestResultMasterDto) throws ApplicationException;
	
	public Response getBloodTestResultMasterById(Integer bloodTestResultId, Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getActiveBloodTestResultMaster(Integer organizationId, Integer unitId) throws ApplicationException;
	
	public Response getBloodTestResultMasterCount(BloodTestResultMasterDto bloodTestResultMasterDto) throws ApplicationException;
	
	public Response getBloodTestResultMasterByName(BloodTestResultMasterDto bloodTestResultMasterDto) throws ApplicationException;
	
	public Response getBloodTestResultMasterByNameNotId(BloodTestResultMasterDto bloodTestResultMasterDto) throws ApplicationException;
}
