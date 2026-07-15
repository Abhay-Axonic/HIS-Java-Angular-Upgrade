package com.param.adt.discharge.dao;

import com.param.adt.discharge.model.DischargeFieldUnitWise;
import com.param.adt.discharge.model.DischargeFieldUnitWiseDTO;
import com.param.global.common.Response;

@SuppressWarnings({ "rawtypes" })
public interface IDischargeFieldUnitWiseDao {

	public Response saveDischargeFieldUnitWise(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO);

	public Response getDischargeFieldUnitWiseByUnitwisetidNotDischargeid(
			DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO);

	public Response updateDischargeFieldUnitWise(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO);

	public Response updateDischargeFieldUnitWiseStatus(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO);

	public Response getDischargeFieldUnitWiseById(Integer dischargeFieldUnitWiseId);

	public Response getDischargeFieldUnitWiseMasterList(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO);

	public Response getDischargeFieldUnitWiseCount(Integer organizationId, Integer unitId, String searchKeyword);

	public Response getActiveDischargeFieldUnitWise(Integer organizationId, Integer unitId);

	public Response checkDischargeFeildIdAlradyExist(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO);

	public Response getDischargeFieldUnitWiseList(DischargeFieldUnitWiseDTO dischargeFieldUnitWiseDTO);
}
