package com.param.CSSD.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.param.CSSD.dto.PreSterilizationProcessDto;
import com.param.CSSD.model.CssdOpeningBalanceDetailStock;
import com.param.CSSD.model.PreSterilizationProcess;
import com.param.CSSDMaster.dto.CssdKitDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IPreSterilizationProcessDao {

	
	
	Response getCleanedWorklistByMultipleFields(Date from,Date to,String name,String status,Integer organizationId,Integer unitId,Integer storeId);
	
	Response getPendingForCleaningWorklistByMultipleFields(Date from,Date to,String name,Integer organizationId,Integer unitId,Integer storeId);
	
	Response getInspectedWorklistByMultipleFields(Date from,Date to,String name,String status,Integer organizationId,Integer unitId,Integer storeId);
	
	Response getDisinfectedWorklistByMultipleFields(Date from,Date to,String name,String status,Integer organizationId,Integer unitId,Integer storeId);
	
	Response getPreSterilizationProcessByTransitionNumber(Integer organizationId,Integer unitId,Integer transitionNumber);
	
	Response saveCleanedWorklist(PreSterilizationProcessDto preSterilizationProcessDto);
	
	Response formKit(PreSterilizationProcessDto preSterilizationProcessDto);
	
	Response getKitFormationWorklistByMultipleFields(Date from,Date to,String name,String status,Integer organizationId,Integer unitId,Integer storeId);
	
	Response getKitFormationWorklistByTransitionNumber(Integer organizationId,Integer unitId,Integer storeId,Integer transitionNumber);
	
	Response replaceInstrument(Integer toBeReplaced,Integer replaceWith);
	
	Response getSterilizedKitsByKitId(Integer organizationId,Integer unitId,Integer storeId,Integer kitId,Integer limit);
	
	Response getKitInstrumentId(Integer kitId, Integer instrumentId);
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * This method is used to update the kit. 
	 * @param preSterilizationProcessDto
	 * @return Response
	 */
	Response updatePreSterilizationProcess(PreSterilizationProcessDto preSterilizationProcessDto);

	/**
	 * This method returns the list of kits that are under presterilization process.
	 * @return Response
	 */
	Response getPreSterilizationProcessList();
	
	Response searchPreSterilizationProcessList(String name);
	
	
	/**
	 * This method returns the list of kits. 
	 * @param from
	 * @param to
	 * @param name
	 * @param status
	 * @return Response
	 */
	Response createPreSterilizationProcess(PreSterilizationProcess preSterilizationProcess);
	
	/**
	 * This method returns list of kits pending for inspection.
	 * @return Response
	 */
	Response getKitListPendingForInspection();
	
	/**
	 * This method returns list of kits pending for disinfection. 
	 * @return Response
	 */
	Response getKitListPendingForDisInfection();
	
	/**
	 * This method returns list of kits pending for formation.
	 * @return Response
	 */
	Response getKitListPendingForKitFormation();
	
	/**
	 * This method returns list of formedKits.
	 * @return Response
	 */
	Response getFormedKitList();
	
	/**
	 * This method is used for Kit Formation
	 * @param kitFormationRequestDto
	 * @return Response
	 */
	

	Response getPreSterilizationProcessKitById(Integer kitId);

	Response getCssdStoreList(PreSterilizationProcessDto preSterilizationProcessDto);

	Response cssdStockLogUpdate(PreSterilizationProcessDto preSterilizationProcessDto);

	Response checkStoreStockByItemId(PreSterilizationProcessDto preSterilizationProcessDto);

	Response updateInTransitStock(PreSterilizationProcessDto preSterilizationProcessDto);

	Response saveSterilizedStockForIssue(CssdOpeningBalanceDetailStock details, Integer storeId);

	Response saveSterilizedKitStockForIssue(Integer storeId, Map<Integer, List<CssdOpeningBalanceDetailStock>> map);

	Response updateSterilizationStock(List<Integer> unitIds);

	Response getSterilizedKitByKitName(CssdKitDto kitDto);

	Response getSterilizedKitBatchList(CssdKitDto kitDto);

	
	
}
