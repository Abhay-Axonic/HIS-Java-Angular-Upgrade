package com.param.global.opd.dao;

import com.param.global.common.Response;
import com.param.global.dto.global.AIAppointmentQueueDto;
import in.param.exception.ApplicationException;

public interface IAiAppointmentQueueDao {
    public Response saveAIAppointmentInQueue(AIAppointmentQueueDto aiAppointmentDto)throws ApplicationException;
    public Response updateQueueByAppointmentId(AIAppointmentQueueDto aiAppointmentDto)throws ApplicationException;

    Response getAIAppointmentCount(AIAppointmentQueueDto dto);

    Response getAISummaryData(AIAppointmentQueueDto dto);
}
