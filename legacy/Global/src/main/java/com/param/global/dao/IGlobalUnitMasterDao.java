package com.param.global.dao;

import com.param.adt.master.global.model.UnitMaster;
import com.param.global.common.Response;
import com.param.global.dto.OrderMasterDto;
import com.param.global.dto.UserMasterDto;
import com.param.global.dto.sync.ScheduleAppointmentReqDto;
import com.param.global.org.dto.UnitMasterDto;

import com.param.global.org.dto.UnitSubscriptionConfigDto;
import com.param.global.org.dto.UnitSubscriptionDetailsDto;
import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IGlobalUnitMasterDao extends IGenericDao<UnitMaster, Integer>{

	public Response getUnitMasterForSyncById(Integer unitId)throws ApplicationException;
	public Response saveUnitMaster(UnitMasterDto unitMasterDto)throws ApplicationException;
	public Response updateUnitMaster(UnitMasterDto unitMasterDto)throws ApplicationException;
	public Response getUnitById(Integer unitId)throws ApplicationException;
	public Response getUnitByName(UnitMasterDto unitMasterDto)throws ApplicationException;
	public Response getUnitByNameAndNotById(UnitMasterDto unitMasterDto)throws ApplicationException;
	public Response updateUnitStatusById(UnitMasterDto unitMasterDto)throws ApplicationException;
	public Response getAllUnitList(UnitMasterDto unitMasterDto)throws ApplicationException;
	public Response getUnitTotalCount(int orgId)throws ApplicationException;
	public String getUnitImage(int unitId)throws ApplicationException;
	
	   /**@code Api Image upload}*/
	public Response updateFolderPath(UnitMasterDto unitMasterDto)throws ApplicationException;
    public Response updateImagePath(UnitMasterDto unitMasterDto)throws ApplicationException;
    public Response getUnitFolderPath(int unitId)throws ApplicationException;
    
    
    /**@Code API for prefix numberGeneration */
	public String getGeneratedNo(int storeId,int unitId,int screenId)throws ApplicationException;
	public Response getUnitByOrganisation(int orgId)throws ApplicationException;
	String getGeneratedGlobalNo(int storeId, int orgId, int unitId,int screenId) throws ApplicationException;
	public Response updateGlobalUnitId(Integer localUnitId, Integer globalUnitId);
	public Response getActiveUnitListForFolderCreation()throws ApplicationException;
	UnitMasterDto getUnitsAddress(Integer unitId) throws ApplicationException;
	public Response getAllLocalIdsByGlobalIds(ScheduleAppointmentReqDto scheduleAppointmentReqDto)throws ApplicationException;
	public Response getInvestigationsListForCoversheet(Integer patientId, boolean flag, Integer unitId);
	public OrderMasterDto getPatientClassByVisitForNewCoversheet(OrderMasterDto orderMasterDto2);

    //SubscriptionModel
	Response addUpdateSubscriptionConfig(UnitSubscriptionConfigDto dto) throws ApplicationException;
	Response getSubScriptionConfig(UnitMasterDto Dto) throws ApplicationException;
	Response checkSubscriptionExist(UnitSubscriptionDetailsDto dto) throws ApplicationException;
	Response addSubScrtionDetails(UnitSubscriptionDetailsDto dto) throws ApplicationException;
	Response getSubcriptionDetails(UnitSubscriptionDetailsDto dto) throws ApplicationException;
	Response inactivateSubcriptions() throws ApplicationException;
	Response findSubscriptionsExpiringInDays() throws ApplicationException;
	Response getAciveSubscriptionByUnit(UnitMasterDto Dto) throws ApplicationException;
	Response getSubscriptionDetailsByROrderId(String razorPayOrderId) throws ApplicationException;
	Response getSubscriptionById(UnitSubscriptionDetailsDto dto)throws ApplicationException;
}
