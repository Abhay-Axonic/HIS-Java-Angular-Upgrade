package com.param.bloodBank.service;

import javax.transaction.Transactional;

import com.param.bloodBank.dto.BloodRedCellDto;
import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IBloodRedCellService {

	Response saveOrUpdateRedCell(BloodRedCellDto dto)throws ApplicationException;

	Response getRedCellDetails(BloodRedCellDto dto)throws ApplicationException;

	Response getRedCellDetailsOnComponentSeparation(TransfusionTransmittedDiseasesDto dto)throws ApplicationException;

}
