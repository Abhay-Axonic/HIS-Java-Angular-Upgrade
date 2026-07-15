package com.param.global.unit.service;

import com.param.global.common.Response;
import com.param.global.unit.dto.UHIDMergeDto;

public interface IUHIDMergeRequestDao {

	Response getUserUHIDMergeList(UHIDMergeDto uHIDMergeDto);

	Response saveUHIDMergeMaster(UHIDMergeDto uHIDMergeDto);

}
