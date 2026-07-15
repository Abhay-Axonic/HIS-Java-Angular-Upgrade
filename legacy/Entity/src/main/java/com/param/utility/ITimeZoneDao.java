package com.param.utility;

import java.util.List;

public interface ITimeZoneDao {

	List<TimeZoneDto> getTimeZones();

	List<UnitMasterDto> getUnitTimeZones();

}
