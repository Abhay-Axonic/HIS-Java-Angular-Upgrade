package com.param.adt.discharge.dao;

import com.param.adt.discharge.dto.DischargeSummaryDto;
import com.param.global.common.Response;

public interface IDischargeSumTabColumnsPrintMapperDao {
	public Response saveDischargeSumTabColumnsPrintMapper(DischargeSummaryDto dischargeSummaryDto);

	public Response checkDischSumTabColPrintMapperAvailable(DischargeSummaryDto dischargeSummaryDto);

	public Response updateDischargeSumTabColumnsPrintMapper(DischargeSummaryDto dischargeSummaryDto);

}
