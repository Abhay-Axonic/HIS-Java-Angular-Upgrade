package com.param.adt.discharge.service;

import javax.transaction.Transactional;

import com.param.adt.discharge.model.DischargeFieldUnitWiseDTO;
import com.param.global.common.Response;

@Transactional
@SuppressWarnings({ "rawtypes" })
public interface IDischargeFieldUnitWiseService {

	public Response saveDischargeFieldUnitWise(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO);

	public Response updateDischargeFieldUnitWise(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO);

	public Response updateDischargeFieldUnitWiseStatus(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO);

	public Response getDischargeFieldUnitWiseById(Integer dischargeFieldUnitWiseId);

	public Response getDischargeFieldUnitWiseMasterList(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO);

	public Response getDischargeFieldUnitWiseCount(Integer organizationId, Integer unitId,  String searchKeyword);

	public Response getActiveDischargeFieldUnitWise(Integer organizationId, Integer unitId);

	public Response getDischargeFieldUnitWiseList(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO);

}
