package com.param.bloodBank.dao;

import com.param.bloodBank.dto.BloodRedCellDto;
import com.param.bloodBank.dto.TransfusionTransmittedDiseasesDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IBloodRedCellDao {

	Response saveOrUpdateRedCell(BloodRedCellDto dto) throws ApplicationException;

	Response updateRedCellGrouping(BloodRedCellDto dto)  throws ApplicationException;

	Response updateRedCellSerum(BloodRedCellDto dto) throws ApplicationException;

	Response updateBloodConfirmed(BloodRedCellDto dto)throws ApplicationException;

	Response getRedCellDetails(BloodRedCellDto dto)throws ApplicationException;

	Response getRedCellDetailsOnComponentSeparation(TransfusionTransmittedDiseasesDto dto)throws ApplicationException;

}
