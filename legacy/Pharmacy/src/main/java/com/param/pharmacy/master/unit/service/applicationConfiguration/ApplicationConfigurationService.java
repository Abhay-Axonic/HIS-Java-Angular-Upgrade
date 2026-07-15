package com.param.pharmacy.master.unit.service.applicationConfiguration;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.common.dto.CommonResponse;
import com.param.entity.model.master.ApplicationConfiguration;
import com.param.entity.model.master.Unit;
import com.param.pharmacy.master.unit.dao.applicationConfiguration.IApplicationConfigurationDao;
import com.param.pharmacy.master.unit.dto.applicationConfiguration.ApplicationConfigurationRequest;
import com.param.pharmacy.master.unit.dto.applicationConfiguration.ApplicationConfigurationResponse;
import com.param.service.global.SessionService;
import com.param.utility.TimeZoneComponent;

@Service("ApplicationConfigurationService")
public class ApplicationConfigurationService implements IApplicationConfigurationService {

	@Autowired
	IApplicationConfigurationDao ApplicationConfigurationDao;

	@Autowired
	private SessionService sessionService;

	@Override
	@Transactional
	public List<ApplicationConfigurationResponse> getAll(Integer unitId) {
		List<ApplicationConfigurationResponse> responseList = new ArrayList<ApplicationConfigurationResponse>();

		for (ApplicationConfiguration application : ApplicationConfigurationDao.getAll(unitId)) {

			ApplicationConfigurationResponse applicationConfigurationResponse = new ApplicationConfigurationResponse(
					application.getId(), application.getIsActive(),
					application.getItemName() != null ? application.getItemName() : null,
					application.getDefaultTaxId() != null ? application.getDefaultTaxId() : null,
					application.getIssueMaterialPickerRequired() != null ? application.getIssueMaterialPickerRequired()
							: null,
					application.getTaxInclusive() != null ? application.getTaxInclusive() : null,
					application.getAssignPickerRequired() != null ? application.getAssignPickerRequired() : null,
					application.getIsMarkup() != null ? application.getIsMarkup() : null,
					application.getMarkupUp() != null ? application.getMarkupUp() : null,
					application.getIsDeptPharmacyRequired() != null ? application.getIsDeptPharmacyRequired() : null,
					application.getIsPharmValidationOPRequired() != null ? application.getIsPharmValidationOPRequired()
							: null,
					application.getIsAssignPickerOPRequired() != null ? application.getIsAssignPickerOPRequired()
							: null,
					application.getStoreId() != null ? application.getStoreId() : 0,
					application.getStoreIp() != null ? application.getStoreIp() : 0,
					application.getInventoryStoreId() != null ? application.getInventoryStoreId() : 0, 
					 application.getIsPharmacologicalValidationSkipIp(), application.getOpStatNotRequired(), 
					 application.getIpStatNotRequired(),application.getIsManualBarcodeRequired(),application.getIsPRNRequired());
			applicationConfigurationResponse.setIsSkipPharmacySettlement(
					application.getIsSkipPharmacySettlement() != null ? application.getIsSkipPharmacySettlement()
							: true);
			applicationConfigurationResponse.setIsSkipPhysicalInspection(
					application.getIsSkipPhysicalInspection() != null ? application.getIsSkipPhysicalInspection()
							: false);
			applicationConfigurationResponse.setIsPoApproval(
					application.getIsPoApproval() != null ? application.getIsPoApproval()
							: false);
			applicationConfigurationResponse.setAutoBatch(application.getAutoBatch() != null ? application.getAutoBatch() : false);
			applicationConfigurationResponse.setAutoPrescribedQty(application.getAutoPrescribedQty() !=null ? application.getAutoPrescribedQty() : false);
			
			applicationConfigurationResponse.setIsAutoConsumableAcceptance(application.getIsAutoConsumableAcceptance()!=null ? application.getIsAutoConsumableAcceptance() :false);
			applicationConfigurationResponse.setIsAutoMedicationAcceptance(application.getIsAutoMedicationAcceptance()!=null ? application.getIsAutoMedicationAcceptance() :false);
            applicationConfigurationResponse.setatCopRequired(application.getatCopRequired()!=null ? application.getatCopRequired() :false);
            applicationConfigurationResponse.setVirtualStoreId(application.getVirtualStoreId()!= null ? application.getVirtualStoreId() : 0);
			responseList.add(applicationConfigurationResponse);
		}
		return responseList;
	}

	@Override
	@Transactional
	public CommonResponse saveNew(ApplicationConfigurationRequest request) {
		ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDao.getApplicationConfiguration(request.getUnitId());
		if (applicationConfiguration != null) {
			applicationConfiguration.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			Long appliConfiguration = new Long(0);

			appliConfiguration = ApplicationConfigurationDao.getCountApplicationConfiguration(request.getUnitId());

			if (appliConfiguration > 0) {
				applicationConfiguration.updateApplicationConfiguration(
						request.getItemName() != null ? request.getItemName() : applicationConfiguration.getItemName(),
						request.getDefaultTaxId() != null ? request.getDefaultTaxId()
								: applicationConfiguration.getDefaultTaxId(),
						request.getIssueMaterialPickerRequired() != null ? request.getIssueMaterialPickerRequired()
								: applicationConfiguration.getIssueMaterialPickerRequired(),
						request.getTaxInclusive() != null ? request.getTaxInclusive()
								: applicationConfiguration.getTaxInclusive(),
						request.getAssignPickerRequired() != null ? request.getAssignPickerRequired()
								: applicationConfiguration.getAssignPickerRequired(),
						request.getIsMarkup() != null ? request.getIsMarkup() : applicationConfiguration.getIsMarkup(),
						request.getMarkupUp() != null ? request.getMarkupUp() : applicationConfiguration.getMarkupUp(),
						request.getIsDeptPharmacyRequired() != null ? request.getIsDeptPharmacyRequired()
								: applicationConfiguration.getIsDeptPharmacyRequired(),
						request.getIsPharmValidationOPRequired() != null ? request.getIsPharmValidationOPRequired()
								: applicationConfiguration.getIsPharmValidationOPRequired(),
						request.getIsAssignPickerOPRequired() != null ? request.getIsAssignPickerOPRequired()
								: applicationConfiguration.getIsAssignPickerOPRequired(),
						request.getStoreOp() != null ? request.getStoreOp() : applicationConfiguration.getStoreId(),
						request.getStoreIp() != null ? request.getStoreIp() : applicationConfiguration.getStoreIp(),
						request.getInventoryStoreId() != null ? request.getInventoryStoreId()
								: applicationConfiguration.getInventoryStoreId(), 
						request.getIsPharmacologicalValidationSkipIp(), request.getOpStatNotRequired(), 
						request.getIpStatNotRequired(),request.getIsManualBarcodeRequired(),request.getIsPRNRequired());
				applicationConfiguration.setIsSkipPharmacySettlement(
						request.getIsSkipPharmacySettlement() != null ? request.getIsSkipPharmacySettlement()
								: applicationConfiguration.getIsSkipPharmacySettlement());
				applicationConfiguration.setUpdatedDate(TimeZoneComponent.getDateByzone(applicationConfiguration.getUnitId()));
				applicationConfiguration.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
				applicationConfiguration.setIsSkipPhysicalInspection(
						request.getIsSkipPhysicalInspection() != null ? request.getIsSkipPhysicalInspection()
								: applicationConfiguration.getIsSkipPhysicalInspection());
				
				applicationConfiguration.setIsPoApproval(
						request.getIsPoApproval() != null ? request.getIsPoApproval()
								: applicationConfiguration.getIsPoApproval());
				
				applicationConfiguration.setAutoBatch(request.getAutoBatch() != null ? request.getAutoBatch() :applicationConfiguration.getAutoBatch());
				applicationConfiguration.setAutoPrescribedQty(request.getAutoPrescribedQty() !=null ? request.getAutoPrescribedQty() :applicationConfiguration.getAutoPrescribedQty());
				applicationConfiguration.setIsAutoConsumableAcceptance(request.getIsAutoConsumableAcceptance()!=null ? request.getIsAutoConsumableAcceptance() :applicationConfiguration.getIsAutoConsumableAcceptance());
				applicationConfiguration.setIsAutoMedicationAcceptance(request.getIsAutoMedicationAcceptance()!=null ? request.getIsAutoConsumableAcceptance() :applicationConfiguration.getIsAutoMedicationAcceptance());
                applicationConfiguration.setatCopRequired(request.getatCopRequired()!=null ? request.getatCopRequired() :applicationConfiguration.getatCopRequired());
                applicationConfiguration.setVirtualStoreId(request.getVirtualStoreId()!= null ? request.getVirtualStoreId() : null);
				
				ApplicationConfigurationDao.update(applicationConfiguration);

				CommonResponse response = new CommonResponse();
				response.setMessage("ApplicationConfiguration updated successfully");

				return response;
			}
		} else {

			ApplicationConfiguration appConfiguration = new ApplicationConfiguration(
					request.getItemName() != null ? request.getItemName() : false,
					request.getDefaultTaxId() != null ? request.getDefaultTaxId() : null,
					request.getIssueMaterialPickerRequired() != null ? request.getIssueMaterialPickerRequired() : false,
					request.getTaxInclusive() != null ? request.getTaxInclusive() : false,
					request.getAssignPickerRequired() != null ? request.getAssignPickerRequired() : false,
					request.getIsMarkup() != null ? request.getIsMarkup() : false,
					request.getMarkupUp() != null ? request.getMarkupUp() : false,
					request.getIsDeptPharmacyRequired() != null ? request.getIsDeptPharmacyRequired() : false,
					request.getIsPharmValidationOPRequired() != null ? request.getIsPharmValidationOPRequired() : false,
					request.getIsAssignPickerOPRequired() != null ? request.getIsAssignPickerOPRequired() : false,
					request.getStoreOp() != null ? request.getStoreOp() : 0,
					request.getStoreIp() != null ? request.getStoreIp() : 0,
					request.getInventoryStoreId() != null ? request.getInventoryStoreId() : 0);
			appConfiguration.setIsSkipPharmacySettlement(
					request.getIsSkipPharmacySettlement() != null ? request.getIsSkipPharmacySettlement() : true);
			appConfiguration.setIsSkipPhysicalInspection( request.getIsSkipPhysicalInspection() != null ? request.getIsSkipPhysicalInspection()
							: false);
			appConfiguration.setIsPoApproval(
					request.getIsPoApproval() != null ? request.getIsPoApproval()
							: false);
			appConfiguration.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			appConfiguration.setAutoBatch(request.getAutoBatch() != null ? request.getAutoBatch() :false);
			appConfiguration.setAutoPrescribedQty(request.getAutoPrescribedQty() !=null ? request.getAutoPrescribedQty() :false);
			appConfiguration.setIsAutoConsumableAcceptance(request.getIsAutoConsumableAcceptance()!=null ? request.getIsAutoConsumableAcceptance() :false);
			appConfiguration.setIsAutoMedicationAcceptance(request.getIsAutoMedicationAcceptance()!=null ? request.getIsAutoConsumableAcceptance() :false);
            appConfiguration.setatCopRequired(request.getatCopRequired()!=null ? request.getatCopRequired() :false);
            appConfiguration.setVirtualStoreId(request.getVirtualStoreId()!= null ? request.getVirtualStoreId() : null);
			ApplicationConfigurationDao.saveApplicationConfiguration(appConfiguration);

			CommonResponse response = new CommonResponse();
			response.setId(appConfiguration.getId().longValue());
			response.setMessage("Application Configuration saved successfully");

			return response;

		}

		return null;
	}

	@Override
	@Transactional
	public CommonResponse updateApplicationConfiguration(ApplicationConfigurationRequest request) {

		ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDao
				.getApplicationConfiguration(request.getId());

		applicationConfiguration.updateApplicationConfiguration(
				request.getItemName() != null ? request.getItemName() : applicationConfiguration.getItemName(),
				request.getDefaultTaxId() != null ? request.getDefaultTaxId()
						: applicationConfiguration.getDefaultTaxId(),
				request.getIssueMaterialPickerRequired() != null ? request.getIssueMaterialPickerRequired()
						: applicationConfiguration.getIssueMaterialPickerRequired(),
				request.getTaxInclusive() != null ? request.getTaxInclusive()
						: applicationConfiguration.getTaxInclusive(),
				request.getAssignPickerRequired() != null ? request.getAssignPickerRequired()
						: applicationConfiguration.getAssignPickerRequired(),
				request.getIsMarkup() != null ? request.getIsMarkup() : applicationConfiguration.getIsMarkup(),
				request.getMarkupUp() != null ? request.getMarkupUp() : applicationConfiguration.getMarkupUp(),
				request.getIsDeptPharmacyRequired() != null ? request.getIsDeptPharmacyRequired()
						: applicationConfiguration.getIsDeptPharmacyRequired(),
				request.getIsPharmValidationOPRequired() != null ? request.getIsPharmValidationOPRequired()
						: applicationConfiguration.getIsPharmValidationOPRequired(),
				request.getIsAssignPickerOPRequired() != null ? request.getIsAssignPickerOPRequired()
						: applicationConfiguration.getIsAssignPickerOPRequired(),
				request.getStoreOp() != null ? request.getStoreOp() : applicationConfiguration.getStoreId(),
				request.getStoreIp() != null ? request.getStoreIp() : applicationConfiguration.getStoreIp(),
				request.getInventoryStoreId() != null ? request.getInventoryStoreId()
						: applicationConfiguration.getInventoryStoreId(),
				request.getIsPharmacologicalValidationSkipIp(), request.getOpStatNotRequired(), 
				request.getIpStatNotRequired(),request.getIsManualBarcodeRequired(),request.getIsPRNRequired());
		applicationConfiguration.setIsSkipPharmacySettlement(
				request.getIsSkipPharmacySettlement() != null ? request.getIsSkipPharmacySettlement()
						: applicationConfiguration.getIsSkipPharmacySettlement());
		
		applicationConfiguration.setIsSkipPhysicalInspection( request.getIsSkipPhysicalInspection() != null ? request.getIsSkipPhysicalInspection()
				: applicationConfiguration.getIsSkipPhysicalInspection());
		applicationConfiguration.setIsPoApproval(
				request.getIsPoApproval() != null ? request.getIsPoApproval()
						: applicationConfiguration.getIsPoApproval());
		
		applicationConfiguration.setAutoBatch(request.getAutoBatch() != null ? request.getAutoBatch() :applicationConfiguration.getAutoBatch());
		applicationConfiguration.setAutoPrescribedQty(request.getAutoPrescribedQty() !=null ? request.getAutoPrescribedQty() :applicationConfiguration.getAutoPrescribedQty());
		applicationConfiguration.setIsAutoConsumableAcceptance(request.getIsAutoConsumableAcceptance()!=null ? request.getIsAutoConsumableAcceptance() :false);
		applicationConfiguration.setIsAutoMedicationAcceptance(request.getIsAutoMedicationAcceptance()!=null ? request.getIsAutoMedicationAcceptance() :false);
        applicationConfiguration.setatCopRequired(request.getatCopRequired()!=null ? request.getatCopRequired() :false);
        applicationConfiguration.setVirtualStoreId(request.getVirtualStoreId()!= null ? request.getVirtualStoreId() : null);
		applicationConfiguration.setUpdatedDate(TimeZoneComponent.getDateByzone(applicationConfiguration.getUnitId()));
		applicationConfiguration.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

		ApplicationConfigurationDao.update(applicationConfiguration);

		CommonResponse response = new CommonResponse();
		response.setMessage("Application Configuration updated successfully");

		return response;
	}

	@Override
	@Transactional
	public CommonResponse update(ApplicationConfigurationRequest request) {

		ApplicationConfiguration applicationConfiguration = ApplicationConfigurationDao
				.getApplicationConfiguration(request.getUnitId());

		Long appliConfiguration = new Long(0);

		appliConfiguration = ApplicationConfigurationDao.getCountApplicationConfiguration(request.getUnitId());

		if (appliConfiguration > 0) {
			applicationConfiguration.updateApplicationConfiguration(
					request.getItemName() != null ? request.getItemName() != null : null,
					request.getDefaultTaxId() != null ? request.getDefaultTaxId() : null,
					request.getIssueMaterialPickerRequired() != null ? request.getIssueMaterialPickerRequired() : null,
					request.getTaxInclusive() != null ? request.getTaxInclusive() : null,
					request.getAssignPickerRequired() != null ? request.getAssignPickerRequired() : null,
					request.getUnitId() != null ? new Unit(request.getUnitId()) : null);

			applicationConfiguration.setUpdatedDate(TimeZoneComponent.getDateByzone(applicationConfiguration.getUnitId()));
			applicationConfiguration.setUpdatedByUser(request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());

			ApplicationConfigurationDao.update(applicationConfiguration);

			CommonResponse response = new CommonResponse();
			response.setMessage("ApplicationConfiguration updated successfully");

			return response;
		}

		if (appliConfiguration == 0) {
			ApplicationConfiguration appConfiguration = new ApplicationConfiguration(
					request.getUnitId() != null ? new Unit(request.getUnitId()) : null,
					request.getItemName() != null ? request.getItemName() : null,
					request.getDefaultTaxId() != null ? request.getDefaultTaxId() : null,
					request.getIssueMaterialPickerRequired() != null ? request.getIssueMaterialPickerRequired() : null,
					request.getTaxInclusive() != null ? request.getTaxInclusive() : null,
					request.getAssignPickerRequired() != null ? request.getAssignPickerRequired() : null);
			
			appConfiguration.setIsSkipPhysicalInspection( request.getIsSkipPhysicalInspection() != null ? request.getIsSkipPhysicalInspection()
					: false);
			
			appConfiguration.setCreateDefaults(request.getOrganizationId()!=null ? request.getOrganizationId() : sessionService.getOrganizationId(),request.getUnitId() !=null ? request.getUnitId() :sessionService.getUnitId() ,request.getUserId()!=null ? request.getUserId() : sessionService.getUserId());
			ApplicationConfigurationDao.saveApplicationConfiguration(appConfiguration);

			CommonResponse response = new CommonResponse();
			response.setId(appConfiguration.getId().longValue());
			response.setMessage("Application Configuration saved successfully");

			return response;
		}
		return null;
	}

	@Override
	public CommonResponse saveApplicationConfiguration(
			ApplicationConfigurationRequest applicationConfigurationRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonResponse savePharmacyApplicationConfiguration(
			ApplicationConfigurationRequest applicationConfigurationRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ApplicationConfigurationResponse> getAllPharmacy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CommonResponse updatePharmacyApplicationConfiguration(
			ApplicationConfigurationRequest applicationConfigurationRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @Override
	 * 
	 * @Transactional public CommonResponse
	 * saveApplicationConfiguration(ApplicationConfigurationRequest request) {
	 * 
	 * ApplicationConfiguration applicationConfiguration = new
	 * ApplicationConfiguration(request.getItemName(), request.getDefaultTaxId(),
	 * request.getIssueMaterialPickerRequired());
	 * 
	 * ApplicationConfiguration applicationConfigurationresponse =
	 * ApplicationConfigurationDao
	 * .saveApplicationConfiguration(applicationConfiguration);
	 * 
	 * CommonResponse response = new CommonResponse();
	 * response.setId(applicationConfigurationresponse.getId().longValue());
	 * response.setMessage("ApplicationConfiguration saved successfully");
	 * 
	 * return response; }
	 * 
	 * @Override public CommonResponse savePharmacyApplicationConfiguration(
	 * ApplicationConfigurationRequest request) {
	 * 
	 * ApplicationConfiguration applicationConfiguration = new
	 * ApplicationConfiguration(request.getTaxInclusive(),
	 * request.getAssignPickerRequired());
	 * ApplicationConfigurationDao.saveApplicationConfiguration(
	 * applicationConfiguration);
	 * 
	 * CommonResponse response = new CommonResponse();
	 * response.setId(applicationConfiguration.getId().longValue());
	 * response.setMessage("Application Configuration saved successfully");
	 * 
	 * return response; }
	 * 
	 * @Override public List<ApplicationConfigurationResponse> getAllPharmacy() {
	 * 
	 * List<ApplicationConfigurationResponse> responseList = new
	 * ArrayList<ApplicationConfigurationResponse>();
	 * 
	 * for (ApplicationConfiguration application :
	 * ApplicationConfigurationDao.getAll()) { ApplicationConfigurationResponse
	 * applicationConfigurationResponse = new ApplicationConfigurationResponse(
	 * application.getId(), application.getIsActive(),
	 * application.getTaxInclusive(), application.getAssignPickerRequired());
	 * 
	 * responseList.add(applicationConfigurationResponse); } return responseList; }
	 */

}