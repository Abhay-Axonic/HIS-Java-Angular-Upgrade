package com.param.pharmacy.master.global.dao.strengthUnit;

import java.util.List;

import com.param.entity.model.master.StrengthUnit;
import com.param.pharmacy.master.global.CustomeException;
import com.param.pharmacy.master.global.dto.strengthUnit.StrengthUnitListResponse;

public interface IStrengthUnitDao {

	List<StrengthUnit> getAll(Integer orgId);

	StrengthUnit getById(Integer id);

	Integer create(StrengthUnit strengthUnit);

	Integer update(StrengthUnit strengthUnit) throws CustomeException;

	Long getStrengthUnitDaoCountFromItem(Integer id) ;

	List<StrengthUnitListResponse> getStrengthUnitList(Integer organizationId);

}
