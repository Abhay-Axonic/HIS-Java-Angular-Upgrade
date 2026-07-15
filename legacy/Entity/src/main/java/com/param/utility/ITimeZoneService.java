package com.param.utility;

import java.util.List;

public interface ITimeZoneService {

	List<UnitMasterDto> getUnitTimeZones();
	
	List<TimeZoneDto> getTimeZones();

}
