package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.ProductionReleaseEmailNotificationConfigDto;
import in.param.exception.ApplicationException;

import java.util.List;

public interface  IProductionReleaseEmailNotificationConfigDao {
   public Response sendNotificationOnEmailOfReleaseDetails() throws ApplicationException;
}
