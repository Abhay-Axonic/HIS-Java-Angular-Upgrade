package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodTestResultMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})

public interface IBloodTestResultMasterDao {
	
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
