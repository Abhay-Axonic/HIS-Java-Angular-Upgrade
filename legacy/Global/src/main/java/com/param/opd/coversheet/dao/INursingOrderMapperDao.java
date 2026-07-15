package com.param.opd.coversheet.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.opd.coversheet.dto.NursingOrderMapperDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
public interface INursingOrderMapperDao {

	public Response saveNursingOrderMapper(List<NursingOrderMapperDto> nursingOrderMapperDtoList) throws ApplicationException ;

	public Response updateNursingOrderMapper(List<NursingOrderMapperDto> nursingOrderMapperDtoList)throws ApplicationException  ;

	public Response getNursingOrderMapperById(Integer nursingOrderMapperId, Integer organizationId, Integer unitId) throws ApplicationException;

	public Response getNursingOrderMapperList(NursingOrderMapperDto nursingOrderMapperDto) throws ApplicationException;

	public Response getActiveNursingOrderMapperList(NursingOrderMapperDto nursingOrderMapperDto) throws ApplicationException;

	public Response getListOfNursingOrders(NursingOrderMapperDto nursingOrderMapperDto) throws ApplicationException;

	public Response updateNursingOrdersStatusByNurse(NursingOrderMapperDto nursingOrderMapperDto) throws ApplicationException;

	public Response updateNursingOrdersStartOrStop(NursingOrderMapperDto nursingOrderMapperDto) throws ApplicationException;

	public Response getNursingOrderMapperListByAdmissionId(NursingOrderMapperDto nursingOrderMapperDto) throws ApplicationException;

	public Response getNurseOrdersForPendingOrdersScreen(NursingOrderMapperDto nursingOrderMapperDto) throws ApplicationException;

	public Response getNurseOrdersForPendingOrdersScreenByDate(NursingOrderMapperDto nursingOrderMapperDto)throws ApplicationException;

	public Response getNurseOrdersDateWiseList(NursingOrderMapperDto nursingOrderMapperDto)throws ApplicationException;

	public Response getNurseOrdersDateWiseListWebApp(NursingOrderMapperDto nursingOrderMapperDto)throws ApplicationException;

}
