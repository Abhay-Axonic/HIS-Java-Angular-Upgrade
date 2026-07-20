(function () {
	"use strict";

	function applicationConfigurationController($scope, $rootScope, $http, CONSTANTS, GenericService, growl,BillingGenericService, $cookies, $state) {
		var self = this;
		//Default Constructor
		var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
		function initializeController() {
			$rootScope.loginpage = true;
			self.model = {
				taxList: [{
					"name": "Select Tax Type"
				}],
				opstoreList: [
					{"store": " "},
					{"store": "All"}
				],
				ipstoreList: [
					{"store": " "},
					{"store": "All"}
				],
				virtualstoreList: [
					{"store": " "},
					{"store": "All"}
				],
			};

			//Methods

			// self.model.PopulateGenericName = PopulateGenericName;
			//  self.model.ToggleStatus = ToggleStatus;
			self.model.isItemNameFlag = isItemNameFlag;
			self.model.isIssueFlag = isIssueFlag;
			self.model.isSkipPhysicalInspection = isSkipPhysicalInspection;
			self.model.SaveAppConfig = SaveAppConfig;
			self.model.ClearFields = ClearFields;
			self.model.GetGenericData = GetGenericData;
			self.model.ValidateGenericMaster = ValidateGenericMaster;
			self.model.PopulateTaxDropdown = PopulateTaxDropdown;
			self.model.CreateAppConf = CreateAppConf;
			self.model.UpdateAppConf = UpdateAppConf;
			self.model.isItemName = "false";
			self.model.isIssue = "false";
			self.model.autoBatch = "false";
			// self.model.atCopRequired = "false";
			self.model.autoPrescribedQty ="false";
			// self.model.Popup = Popup;
			self.model.isEmrRequired = isEmrRequired;
			// getAssetType();
			// getAssetCategory();
			// getProductCategoryList();
			// GetGenericList(); 
			self.model.checkIsMarkup =checkIsMarkup;
			
			PopulateTaxDropdown();
			PopulateStoreDropdown();
			
		}

		var PopulateStoreDropdown = function () {
			var storeRequests = self.model;
			storeRequests.storeOp = storeRequests.opstoreList[0];
			storeRequests.storeIp = storeRequests.ipstoreList[0];
			storeRequests.virtualStore = storeRequests.virtualstoreList[0];

			var URI = CONSTANTS.PROCUREMENT.PURCHASE.PURCHASE_REQUEST.DROPDOWN_API + '/' + $scope.unitId+ '/'+$scope.userId;


			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(function (response) {
				storeRequests.opstoreList = storeRequests.opstoreList.concat(response.data.requestingStores);
				storeRequests.ipstoreList = storeRequests.ipstoreList.concat(response.data.requestingStores);
				storeRequests.virtualstoreList = storeRequests.virtualstoreList.concat(response.data.requestingStores);
				pharmacyGet();
				$rootScope.stopLoader();
			},
			function (err) {
				$rootScope.stopLoader();
			});
		}

		var pharmacyGet = function () {
			var confignew = self.model;
			var URI = CONSTANTS.MASTER.UNIT.APPLICATION_CONFIGURATION.APPLICATION_CONFIGURATION_GET_API + $scope.unitId;
			$rootScope.startLoader();

			GenericService.serviceAction("GET", URI, {}).then(
				function (response) {
					self.model.assignPickerRequired = response.data[0].assignPickerRequired;
					self.model.taxInclusive = response.data[0].taxInclusive;
					self.model.isMarkup = response.data[0].isMarkup;
					self.model.isDeptPharmacyRequired = response.data[0].isDeptPharmacyRequired;
					self.model.isPharmValidationOPRequired = response.data[0].isPharmValidationOPRequired;
					self.model.storeOp.id = response.data[0].storeId,
					self.model.storeIp.id = response.data[0].storeIp,
					self.model.virtualStore.id = response.data[0].virtualStoreId,
					self.model.isAssignPickerOPRequired = response.data[0].isAssignPickerOPRequired;
					self.model.isSkipPharmacySettlement = response.data[0].isSkipPharmacySettlement;
					self.model.isPoApproval = response.data[0].isPoApproval;
					self.model.isSkipPhysicalInspection= response.data[0].isSkipPhysicalInspection;
					self.model.markupUp = response.data[0].markupUp;
					self.model.appConfId = response.data[0].id;
					self.model.isItemName = response.data[0].itemName;
					self.model.tax.id = response.data[0].defaultTaxId;
					/* start code for resolved HIS-9192 Application configuration not saving properly for inventory */
					self.model.isIssue = response.data[0].issueMaterialPickerRequired;
					
					self.model.autoBatch = response.data[0].autoBatch;;
					self.model.autoPrescribedQty =response.data[0].autoPrescribedQty;
					
					/* end code for resolved HIS-9192 Application configuration not saving properly for inventory */
					self.model.isPharmacologicalValidationSkipIp = response.data[0].isPharmacologicalValidationSkipIp;
					self.model.opStatNotRequired = response.data[0].opStatNotRequired;
					self.model.ipStatNotRequired = response.data[0].ipStatNotRequired;
					self.model.isAutoConsumableAcceptance = response.data[0].isAutoConsumableAcceptance;
					self.model.isAutoMedicationAcceptance = response.data[0].isAutoMedicationAcceptance;
                    self.model.atCopRequired = response.data[0].atCopRequired;
                    self.model.isManualBarcodeRequired = response.data[0].isManualBarcodeRequired;
                    self.model.isPRNRequired = response.data[0].isPRNRequired;
                    self.model.isEmrRequired = response.data[0].isEmrRequired;
                    
					for(var i=0;i<self.model.opstoreList.length;i++)
					{
						if(self.model.opstoreList[i].id==response.data[0].storeId)
						{
							self.model.storeOp = self.model.opstoreList[i];
						}
					}
					for(var i=0;i<self.model.opstoreList.length;i++)
					{
						if(self.model.ipstoreList[i].id==response.data[0].storeIp)
						{
							self.model.storeIp = self.model.ipstoreList[i];
						}
						
					}
					
					for(var i=0;i<self.model.opstoreList.length;i++)
					{
						if(self.model.ipstoreList[i].id==response.data[0].virtualStoreId)
						{
							self.model.virtualStore = self.model.ipstoreList[i];
						}
						
					}
					
					
					for(var i=0;i<self.model.ipstoreList.length;i++)
					{
						if(self.model.ipstoreList[i].id==response.data[0].inventoryStoreId)
						{
							self.model.inventoryStoreId = self.model.ipstoreList[i];
						}
					}
					
					console.log("storeOp : ",self.model.storeOp);
					console.log("storeIp : ",self.model.storeIp);
					console.log("inventoryStoreId : ",self.model.inventoryStoreId);

					$rootScope.stopLoader();
				},
				function (err) {
					$rootScope.stopLoader();
				});
		}

		$scope.mailProviderList= [
			{"id": 1,"name":"AWS"},
			{"id": 2,"name":"SendGrid"},
		];

		$scope.payGatewayProviderList= [
			{"id": null,"name":"Please select Gateway"},
			{"id": 2,"name":"CCAvenue"},
			{"id": 1,"name":"Razor Pay"},
		];


		$scope.bucketList= [
			//{"id": null,"name":""},
			{"id": 1,"name":"AWS-Bucket"},
			{"id": 2,"name":"Local-Storage"},
		];
		
		$scope.msgType= [
			//{"id": null,"name":""},
			{"id": 1,"name":"HTTP"},
			{"id": 2,"name":"MSG-91"},
		];
		
		//Method to get the list of Generic 
		var GetGenericList = function () {

			var URI = CONSTANTS.GLOBAL.GENERIC_MASTER_API;
			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
				function (response) {
					self.model.genericList = response.data;
					$rootScope.stopLoader();
				},
				function errorCallback(response) {
					$rootScope.stopLoader();
				});
		}

		var checkIsMarkup = function(){
			if(self.model.isMarkup == false){
				self.model.markupUp = false;
			}
		}

		var PopulateTaxDropdown = function () {
			var appConf = self.model;
			appConf.tax = appConf.taxList[0];

			appConf.taxList.splice(1);
			var URI = CONSTANTS.MASTER.UNIT.APPLICATION_CONFIGURATION.TAX_DROPDOWN +"/"+$scope.organizationId;

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
				function (response) {
					appConf.taxList = appConf.taxList.concat(response.data);
					$rootScope.stopLoader();
				},
				function errorCallback(response) {
					$rootScope.stopLoader();
				});

		}


		//Method to populate ProductCategory for editing
		var GetGenericData = function (genericId) {
			var URI = CONSTANTS.GLOBAL.GENERIC_MASTER_API + genericId;

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
				function (response) {
					var assetType = self.model.assetTypeList.find(function (type) {
						return response.data.assetTypeId == type.id;
					});
					self.model.assetType = assetType;
					PopulateAssetCategoryDropdown(response.data, true);
					self.model.genericId = response.data.id;
					self.model.genericCode = response.data.code;
					self.model.genericName = response.data.name;
					$rootScope.stopLoader();
				});
		};

		var isItemNameFlag = function (flag) {
			if (flag) {
				self.model.isItemName = true;
			} else {
				self.model.isItemName = false;
			}
		}
		var isIssueFlag = function (flag) {
			if (flag) {
				self.model.isIssue = true;
			} else {
				self.model.isIssue = false;
			}
		}
		var isSkipPhysicalInspection = function (flag) {
			if (flag) {
				self.model.isSkipPhysicalInspection = true;
			} else {
				self.model.isSkipPhysicalInspection = false;
			}
		}
		
		var isEmrRequired = function (flag) {
			if (flag) {
				self.model.isEmrRequired = true;
			} else {
				self.model.isEmrRequired = false;
			}
		}
		
		 $scope.searchPharmacyService = function(searchKeyword){
				$scope.servicesList=[];
				searchKeyword=searchKeyword;
				var serData = {
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						searchKeyword : searchKeyword
				}
				var URI = BASE_URL + ROOT_URL + "/global/serviceMasterForUnitApplicationService";
				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.servicesList = response.data.listObject;
					}
					return $scope.servicesList;
				});
			}
		 
		 $scope.addpharmacyServiceId = function($item, $model, $label,flag){
	         	$scope.common.pharmacyServiceName = $scope.common.pharmacyServiceName;
	         	$scope.common.pharmacyServiceId = $item.serviceMasterId;
	         }

		 

		 $scope.searchPharmacyReturnSale = function(searchKeyword){
				$scope.servicesList=[];
				searchKeyword=searchKeyword;
				var serData = {
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						searchKeyword : searchKeyword
				}
				var URI = BASE_URL + ROOT_URL + "/global/serviceMasterForUnitApplicationService";
				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.servicesList = response.data.listObject;
					}
					return $scope.servicesList;
				});
			}
		 
		 $scope.addpharmacyrReturnSale = function($item, $model, $label,flag){
	         	$scope.common.pharmacyReturnSaleName = $scope.common.pharmacyReturnSaleName;
	         	$scope.common.pharmacyReturnSaleServiceId = $item.serviceMasterId;
	         }
		 $scope.searchOtRentService = function(searchKeyword){
			 if(searchKeyword=="" || searchKeyword == undefined){
				 $scope.common.otRentServiceId = null;
			 }
				$scope.servicesList=[];
				searchKeyword=searchKeyword;
				var serData = {
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						searchKeyword : searchKeyword
				}
				var URI = BASE_URL + ROOT_URL + "/global/serviceMasterForUnitApplicationService";
				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.servicesList = response.data.listObject;
					}
					return $scope.servicesList;
				});
			}
		 $scope.addotRentServiceId = function($item, $model, $label,flag){
	         	$scope.common.otRentServiceName = $scope.common.otRentServiceName;
	         	$scope.common.otRentServiceId = $item.serviceMasterId;
	         }
		 
		 $scope.searchAdditionalOtrentService = function(searchKeyword){
			 if(searchKeyword=="" || searchKeyword == undefined){
				 $scope.common.additionalOtrentService = null;
			 }
			 
				$scope.servicesList=[];
				searchKeyword=searchKeyword;
				var serData = {
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						searchKeyword : searchKeyword
				}
				var URI = BASE_URL + ROOT_URL + "/global/serviceMasterForUnitApplicationService";
				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.servicesList = response.data.listObject;
					}
					return $scope.servicesList;
				});
			}
		 
		 $scope.addadditionalOtrentId = function($item, $model, $label,flag){
	         	$scope.common.additionalOtrentServiceName = $scope.common.additionalOtrentServiceName;
	         	$scope.common.additionalOtrentService = $item.serviceMasterId;
	         }
		 
		 $scope.searchSurgeonChargesService = function(searchKeyword){
				$scope.servicesList=[];
				searchKeyword=searchKeyword;
				var serData = {
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						searchKeyword : searchKeyword
				}
				var URI = BASE_URL + ROOT_URL + "/global/serviceMasterForUnitApplicationService";
				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.servicesList = response.data.listObject;
					}
					return $scope.servicesList;
				});
			}
		 
		 $scope.addsurgeonChargesServiceId = function($item, $model, $label,flag){
	         	$scope.common.surgeonChargesServiceName = $scope.common.surgeonChargesServiceName;
	         	$scope.common.surgeonChargesServiceId = $item.serviceMasterId;
	         }
		 
		 $scope.searchSurchargeService = function(searchKeyword){
				$scope.servicesList=[];
				searchKeyword=searchKeyword;
				var serData = {
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						searchKeyword : searchKeyword
				}
				var URI = BASE_URL + ROOT_URL + "/global/serviceMasterForUnitApplicationService";
				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.servicesList = response.data.listObject;
					}
					return $scope.servicesList;
				});
			}
		 $scope.addsurchargeServiceId = function($item, $model, $label,flag){
	         	$scope.common.surchargeServiceName = $scope.common.surchargeServiceName;
	         	$scope.common.surchargeServiceId = $item.serviceMasterId;
	         }
		 
		 $scope.searchAdmissionCharges = function(searchKeyword){
				$scope.servicesList=[];
				searchKeyword=searchKeyword;
				var serData = {
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						searchKeyword : searchKeyword
				}
				var URI = BASE_URL + ROOT_URL + "/global/serviceMasterForUnitApplicationService";
				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.servicesList = response.data.listObject;
					}
					return $scope.servicesList;
				});
			}
		 
		 
		 $scope.ambulanceChargesService = function(searchKeyword){
				$scope.servicesList=[];
				searchKeyword=searchKeyword;
				var serData = {
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						searchKeyword : searchKeyword
				}
				var URI = BASE_URL + ROOT_URL + "/global/serviceMasterForUnitApplicationService";
				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success"){
						$scope.servicesList = response.data.listObject;
					}
					return $scope.servicesList;
				});
		 }
		 
				$scope.addAmbulanceChargesServiceId = function($item, $model, $label,flag){

			         	$scope.common.ambulanceChargesService = $scope.common.ambulanceChargesService;
			         	$scope.common.ambulanceChargesServiceId = $item.serviceMasterId;
			  }
		 
		 $scope.addAdmissionChargesServiceId = function($item, $model, $label,flag){

	         	$scope.common.admissionChargesServiceName = $scope.common.admissionChargesServiceName;
	         	$scope.common.admissionChargesServiceId = $item.serviceMasterId;
	         }
		 $scope.addmrdChargesServiceId = function($item, $model, $label,flag){

	         	$scope.common.mrdChargesServiceName = $scope.common.mrdChargesServiceName;
	         	$scope.common.mrdChargesServiceId = $item.serviceMasterId;
	         }
		 $scope.addHospitalChargeServiceId = function($item, $model, $label,flag){

	         	$scope.common.hospitalChargesServiceName = $scope.common.hospitalChargesServiceName;
	         	$scope.common.hospitalChargesServiceId = $item.serviceMasterId;
	         }
		 $scope.addBiomedicalChargesServiceId = function($item, $model, $label,flag){

	         	$scope.common.biomedicalsChargesServiceName = $scope.common.biomedicalsChargesServiceName;
	         	$scope.common.biomedicalsChargesServiceId = $item.serviceMasterId;
	         }
		 

		 
		//Method to add the Generic
		var CreateAppConf = function () {
			
			var URI = CONSTANTS.MASTER.UNIT.APPLICATION_CONFIGURATION.APPLICATION_CONFIGURATION_SAVE_API;
			if(self.model.inventoryStoreId == undefined || self.model.inventoryStoreId.id == undefined){
				self.model.inventoryStoreId = {};
				self.model.inventoryStoreId.id = 0;
			}
			var data = {
				"itemName": self.model.isItemName,
				"defaultTaxId": self.model.tax.id,
				"issueMaterialPickerRequired": self.model.isIssue,
				"taxInclusive": self.model.taxInclusive,
				"assignPickerRequired": self.model.assignPickerRequired,
				"isMarkup": self.model.isMarkup,
				"isDeptPharmacyRequired": self.model.isDeptPharmacyRequired,
				"isPharmValidationOPRequired": self.model.isPharmValidationOPRequired,
				"isAssignPickerOPRequired": self.model.isAssignPickerOPRequired,
				"isSkipPharmacySettlement" : self.model.isSkipPharmacySettlement,
				"isPoApproval" :self.model.isPoApproval,
				"autoBatch" :self.model.autoBatch,
				"autoPrescribedQty" :self.model.autoPrescribedQty,
				"markupUp": self.model.markupUp,
				"storeOp": self.model.storeOp.id,
				"storeIp": self.model.storeIp.id,
				"inventoryStoreId":self.model.inventoryStoreId.id,
				"isSkipPhysicalInspection":self.model.isSkipPhysicalInspection,
				"unitId":$scope.unitId,
	            "organizationId":$scope.organizationId,
	            "userId":$scope.userId,
	            "storeId":$scope.storeId,
	            "autoBatch" :self.model.autoBatch,
	            "autoPrescribedQty" :self.model.autoPrescribedQty,
	            "isPharmacologicalValidationSkipIp" :self.model.isPharmacologicalValidationSkipIp,
	            "opStatNotRequired" :self.model.opStatNotRequired,
	            "ipStatNotRequired" :self.model.ipStatNotRequired,
				"isAutoMedicationAcceptance": self.model.isAutoMedicationAcceptance,
                "atCopRequired": self.model.atCopRequired,
				"isAutoConsumableAcceptance": self.model.isAutoConsumableAcceptance,
				"isManualBarcodeRequired": self.model.isManualBarcodeRequired,
				"isPRNRequired": self.model.isPRNRequired,
				"isEmrRequired": self.model.isEmrRequired,
				"virtualStoreId": self.model.virtualStore.id,
			}

			var taxData = self.model.taxList.find(function (type) {
				return type.id == self.model.tax.id;
			});

			$rootScope.startLoader();
			GenericService.serviceAction("POST", URI, data).then(
				function (response) {
					var taxComponentData = {
						"id": response.data.id,
						"defaultTaxId": taxData.component,
						"itemName": self.model.isItemName,
						"issueMaterialPickerRequired": self.model.isIssue
					};
					self.model.appConfId = response.data.id,
						//  self.model.genericList.push(genericData);
						// ClearFields();

						$rootScope.stopLoader();
					pharmacyGet();
					growl.success('Record saved successfully');
				},
				function errorCallback(response) {
					$rootScope.stopLoader();
					growl.error('Something went wrong');
				});
		};

		//Method to add or update the Generic
		var SaveAppConfig = function () {
			if (!self.model.appConfId) {
				CreateAppConf();
			} else {
				UpdateAppConf();
			}
		};


		var CreatePharmacyConfig = function () {
			var URI = CONSTANTS.MASTER.UNIT.APPLICATION_CONFIGURATION.APPLICATION_CONFIGURATION_SAVE_API;

			var data = {
				"taxInclusive": self.model.taxInclusive,
				"assignPickerRequired": self.model.assignPickerRequired,
				"unitId":$scope.unitId,
	            "organizationId":$scope.organizationId,
	            "userId":$scope.userId,
	            "storeId":$scope.storeId
			}
			angular.element('#confirmSavePharmacy').modal('hide');
			$(".modal-backdrop").hide();
			$rootScope.startLoader();
			GenericService.serviceAction("POST", URI, data).then(
				function (response) {
					$rootScope.stopLoader();
					growl.success(response.data.message);
				},
				function errorCallback(response) {
					$rootScope.stopLoader();
					growl.error('Something went wrong');
				});
		};
		//Method to update the  Generic
		var UpdateAppConf = function () {
			var URI = CONSTANTS.MASTER.UNIT.APPLICATION_CONFIGURATION.APPLICATION_CONFIGURATION_UPDATE_API + self.model.appConfId;
			if(self.model.inventoryStoreId == undefined){
				self.model.inventoryStoreId = {};
				self.model.inventoryStoreId.id = 0;
			}
			var data = {
				"itemName": self.model.isItemName,
				"defaultTaxId": self.model.tax.id,
				"issueMaterialPickerRequired": self.model.isIssue,
				"taxInclusive": self.model.taxInclusive,
				"assignPickerRequired": self.model.assignPickerRequired,
				"isMarkup": self.model.isMarkup,
				"markupUp": self.model.markupUp,
				"isDeptPharmacyRequired": self.model.isDeptPharmacyRequired,
				"isPharmValidationOPRequired": self.model.isPharmValidationOPRequired,
				"isAssignPickerOPRequired": self.model.isAssignPickerOPRequired,
				"isSkipPharmacySettlement" :self.model.isSkipPharmacySettlement,
				"isPoApproval" :self.model.isPoApproval,
				"storeOp": self.model.storeOp.id,
				"storeIp": self.model.storeIp.id,
				"inventoryStoreId":self.model.inventoryStoreId.id,
				"isSkipPhysicalInspection":self.model.isSkipPhysicalInspection,
				"unitId":$scope.unitId,
	            "organizationId":$scope.organizationId,
	            "userId":$scope.userId,
	            "storeId":$scope.storeId,
	            "autoBatch" :self.model.autoBatch,
	            "autoPrescribedQty" :self.model.autoPrescribedQty,
	            "isPharmacologicalValidationSkipIp" :self.model.isPharmacologicalValidationSkipIp,
	            "opStatNotRequired" :self.model.opStatNotRequired,
	            "ipStatNotRequired" :self.model.ipStatNotRequired,
				"isAutoMedicationAcceptance": self.model.isAutoMedicationAcceptance,
                "atCopRequired": self.model.atCopRequired,
				"isAutoConsumableAcceptance": self.model.isAutoConsumableAcceptance,
				"isManualBarcodeRequired": self.model.isManualBarcodeRequired,
				"isPRNRequired" : self.model.isPRNRequired,
				"isEmrRequired" : self.model.isEmrRequired,
				"virtualStoreId": self.model.virtualStore.id,
			}
			var cookiedata = {
				"taxInclusive": self.model.taxInclusive,
				"assignPickerRequired": self.model.assignPickerRequired
			}
			angular.element('#confirmSavePharmacy').modal('hide');
			$(".modal-backdrop").hide();
			// $rootScope.startLoader();
			GenericService.serviceAction("PATCH", URI, data).then(
				function (response) {
					$rootScope.stopLoader();
					
					pharmacyGet();
					var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
					var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
					cookieObjectAppConfig.taxInclusive = self.model.taxInclusive;
					cookieObjectAppConfig.assignPickerRequired = self.model.taxInclusive;
					cookieObjectAppConfig.autoPrescribedQty = self.model.autoPrescribedQty;

					cookieObjectAppConfigSecond.whatAppOption = $scope.common.whatsAppOption !== undefined  && $scope.common.whatsAppOption == "" ? $scope.common.whatsAppOption : 3;
					$cookies.putObject('cookieObjectAppConfig', cookieObjectAppConfig);
					localStorage.setItem('cookieObjectAppConfigSecond', JSON.stringify(cookieObjectAppConfigSecond));
					growl.success(response.data.message);
					//$state.reload();
					// self.model.activeProductCategoryId = undefined;
					// self.model.isEdit = false;
				});
		};


		//Method to clear the fileds
		function ClearFields() {
			self.model.tax = self.model.taxList[0];
			self.model.isItemName = "";
			self.model.isIssue = "";
			self.model.taxList.splice(1);
		}
		var ValidateGenericMaster = function () {
			var isValid = true;

			if (!self.model.assetType.id) {
				isValid = false;
				self.model.isAssetTypeIdValid = true;
			}
			if (!self.model.assetCategory.id) {
				isValid = false;
				self.model.isValidCode = true;
			}
			if (!self.model.productCategory.id) {
				isValid = false;
				self.model.isValidProductCat = true;
			}
			if (!self.model.genericCode) {
				isValid = false;
				self.model.isValidGenericCode = true;
			}
			if (!self.model.genericName) {
				isValid = false;
				self.model.isValidGenericName = true;
			}
			return isValid;
		}
		initializeController();

		/**Billing Start**/


		$scope.obj={
				selectedPinPadDeviceId:"",
				selectedOutsourceLABId:""
					}
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}

		$scope.unitId = cookieObject.unitId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		$scope.createdBy = cookieObject.userId;
		$scope.updatedBy = cookieObject.userId;
		$scope.commonList = [];
		$scope.commonListCount;

		//cookie app config
		var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		$scope.registeredPinPadDevice = cookieObjectAppConfig != undefined ? cookieObjectAppConfig.registeredPinPadDevice : null;
	
		
		
		$scope.listPOS=[];
		$scope.listPOS.push({id:1, text:"Pin Pad"});
		$scope.listPOS.push({id:2, text:"POS Machine"});
		
		$scope.listOutsourceLAB=[];
		$scope.listOutsourceLAB.push({id:1, text:"Neuron Plus"});
		$scope.listOutsourceLAB.push({id:2, text:"Medall"});
		
		$scope.billingInitMaster = function () {
			$scope.searchName = '';
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;

			$scope.common = {
				unitApplicationBillingConfigurationId: "",
				defaultSelfTariffId: "",
				isPreOrPostBilling: "N",
				isAppointmentRequired: "N",
				isCounterApplicable: "N",
				patientCategoryId: "",
				radiologySpecialityId: "",
				tariffCode: "",
				specialityName: "",
				tariffDesc: "",
				adminRoleId:"",
				staffRoleId: "",
				nurseRoleId: "",
				docRoleId: "",
				specialityId:"",
				subSpecialityId:"",
				subSpecialityName:"",
				opdBillingCatId:"",
				selfPayeeId:"",
				companyDesc:"",
				isServiceWiseSplitBill:"",
				pdfGenerationUrl:"",
				pdfGenerationUrlMis:"",
				patientRegSerId:"",
				consultationSpecialityId:"",
				consultationSubSpecialityId:"",
				senderId:"",
				id:"",
				serviceStandardName:"",
				serviceMasterId:"",
				roleName:"",
				roleId:"",
				casualtySpecialityId:"",
				paediatricsSpecialityId:"",
				pharmacyServiceId:"",
				pharmacyServiceName:"",
				serviceStandardName:"",
				pharmacyReturnSaleName:"",
				additionalOtrentServiceName:"",
				pharmacyReturnSaleServiceId:"",
				isKioskRequired:"",
				surgeonChargesServiceId:"",
				surgeonChargesServiceName:"",
				otRentServiceId:"",
				otRentServiceName:"",
				additionalOtrentService:"",
				surchargeServiceId:"",
				insurancePatientCategory:"",
				companyPatientCategory:"",
				isNursingAssessment:"",
				isRenderedServiceRateEditable:"",
				erSpecialityId:"",
				admissionChargesServiceId:"",
				admissionChargesServiceName : "",
				isSmartCardRequired:"",
				isDigitalSignatureRequired:"",
				isPinPadRequired:"",
				isShowPatientByBed:"",
				groupPracticeNo:"",
				groupPracticeName:"",
				practiceNo:"",
				doctorSlipTDS:"",
				cashAmountValidation:"",
				startDateFormat:"",
				isCorporateEmpVerificationReq:"",
				// isAdviceTabVisible :'N',
				isSendMailOnMailPrescription:'N',
				isRateVisibleOnCoversheet :"",
				isPACSRequired :"",
				obgynSpecialityId :"",
				hemodialysisSpecialityId :"",
				isConsolidatedBill:"N",
				assetMaintenanceSpecialityId:"",
				physiotherapySpecialityId: "",
				//isProcessShareAgainstSettledBill : "N",
				morningFromTime:"",
				morningToTime:"",
				eveningFromTime:"",
				eveningToTime:"",
				skipReadyForBilling : 'N',
				skipAdmissionAcceptance : 'N',
				skipHousekeeping : 'N',
				isFreeVisitReq : 'N',
				isHospitalVisitReq : 'N',
				isReviewAppReq : 'N',
				isLabReq : 'N',
				isEHCReq : 'N',
				isRadiologyReq : 'N',
				isDocConsultReq : 'N',
				optimizeTransferFlow : 'N',
				skipTransferAcceptance : 'N',
				vaccinationChartHide : false,
				allowOtherDoctorToConsult : 'N',
				diagnosisRequire : 'N',
				isGstVisible : 'N',
				skipRISReportEntry :'N',
				defaultDiscountOwnership : 0,
				kioskLandingScreen : "",
				biomedicalsChargesServiceId:"",
				biomedicalsChargesServiceName : "",
				hospitalChargesServiceId:"",
				hospitalChargesServiceName : "",
				mrdChargesServiceId:"",
				mrdChargesServiceName : "",
				emailId : "",
				password : "",
				isPaymentGatewayReqForWebApp :'N',
				isRisPatientDetailsOnFirstPage : 'N',
				razorPayKeyId:"",
				razorPaySecretKey:"",
				isReportAddIntoDischargeSummary:'N',
				admissionCancellationTime:"",
				isPackageApplicableDateEditable:'N',
				webAppUserId : null,
				mailProviderId : "",
				mailCredential : "",
				isKioskAnnouncementRequired:'N',
				cashAmountLimit :null,
				bccEmailId: "",
				whatsAppOption: "",
				blockReportPrint:"",
				payGatewayProviderId :"",
				cimsOption :"N",
				emailValidationRequired:"N",
				mobileValidationRequired:"N",
				backDateAdmissionDays:2,
				dateFormat:"",
				selfPaidConsultAlert:false,
				serviceWiseDoctorListReq : false,
				depositTypeWiseRequired:false,
//				selectedPinPadDeviceId:"",
				selectedPinPadDevice:"",
				appointmentUniqueIdSearch :"N",
				gradeWiseSeviceApplicable : false,
				couponSystemApplicable:false,
				tokenNumber : 'Y',
				mandatoryScan : 'N',
				qmsPrnPrint:'N',
				dischargeSummaryDataViewOptionId : "",
				isOutsourceLABIntegrationReq:"",
				isEmrRequired : false,
				ambulanceChargesService:'',
				ambulanceChargesServiceId:'',
				roa :"N",
				dosage :"N",
				localStorageJson:'',
				bucketFlag:'',
				smsType:'',
				smsTypejson:'',
				isBillManipulationAllowed:"N",
				invoiceCreationInsurance:"N",
				invoiceCreationCorporate:"N",
			};
//			$scope.getConfigMasterList();
			
			console.log("Common-init", $scope.common);
		}
		$scope.billingInitMaster();
		
		if($scope.registeredPinPadDevice == "Pin Pad"){
			$scope.obj.selectedPinPadDeviceId="1";
		}else if($scope.registeredPinPadDevice == "POS Machine"){
			$scope.obj.selectedPinPadDeviceId="2";
		}else{
			$scope.obj.selectedPinPadDeviceId=null;
		}
		
		$scope.initClockPicker = function() {
			$('.clockpicker').clockpicker();
		}

		//Active list of Tariff 

		$scope.getTariffActiveList = function () {

			var URI = BASE_URL + ROOT_URL + "/api/unit/getActiveTariffMasterList";
			var data = {
				
				organizationId: $scope.organizationId,
				unitId: $scope.unitId
			}
			GenericService.serviceAction("POST", URI, data).then(function (response) {
				if (response.data.status == "success") {
					$rootScope.stopLoader();
					$scope.getTariffActiveList = [];
					$scope.getTariffActiveList = response.data.listObject;

					console.log("getTariffActiveList", $scope.getTariffActiveList);
				}
			});
		}
		$scope.getTariffActiveList();
		
	

		//Active list of Speciality

		$scope.getSepecialityActiveList = function () {

			var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
			var data = {
				organizationId: $scope.organizationId,
				unitId: $scope.unitId
			}
			$scope.getSepecialityActiveList = [];
			GenericService.serviceAction("POST", URI, data).then(function (response) {
				if (response.data.status == "success") {
					$rootScope.stopLoader();
					$scope.getSepecialityActiveList = response.data.listObject;
					
					console.log("getSepecialityActiveList", $scope.getSepecialityActiveList);
				}
			});
		}
		$scope.getSepecialityActiveList();
		
		//list of Pharmacy Services-------Chetana

		$scope.getPharmacyServiceList = function () {

			var URI = BASE_URL + ROOT_URL + "/billing/getListOfServiceMaster/"+$scope.orgId+"/"+$scope.unitId;
			
			$scope.getSepecialityActiveList = [];
			GenericService.serviceAction("GET", URI).then(function (response) {
				if (response.data.status == "success") {
					$rootScope.stopLoader();
					$scope.getPharmacyServiceList = response.data.listObject;
					
					console.log("getPharmacyServiceList", $scope.getPharmacyServiceList);
				}
			});
		}
//		$scope.getPharmacyServiceList();
		//list of Pharmacy Services End-------Chetana

		$scope.getActiveSubSpecialtiyList = function () {

			var URI = BASE_URL + ROOT_URL + "/adt/subSpeciality/"+$scope.organizationId+"/"+$scope.unitId;
			GenericService.serviceAction("GET", URI, {}).then(function (response) {
				if (response.data.status == "success") {
					$scope.subSepecialityActiveList = [];
					angular.forEach(response.data.listObject,
						function (value, index) {
							var data = {};
							data.id = value.subSpecialityMasterId;
							data.label = value.subSpecialityMasterName;
							data.splId = value.specialityId;
							$scope.subSepecialityActiveList.push(data);
						});
					console.log("subSepecialityActiveList", $scope.subSepecialityActiveList);
				}
			});
		}
		$scope.getActiveSubSpecialtiyList();
		// get loyalty card Patient catagory List

		$scope.getPatientCategory = function () {
			var URI = BASE_URL + ROOT_URL + "/global/patientcategory/"+$scope.organizationId;
			GenericService
				.serviceAction("GET", URI, {})
				.then(
					function (response) {
						$scope.patientCategoryList = [];

						if (response.data.status == "success") {
							$scope.patientCategoryList = response.data.listObject;
							console.log($scope.patientCategoryList);

						}
					});
		}
		$scope.getPatientCategory();
		
		
		
		
		
		
		
		

		//--------------------------//
		
		// get Doctor role list
		$scope.getActiveList = function(){
			
			var URI = BASE_URL + ROOT_URL + "/api/global/getActiveUserRoleMasterList";
			var data = {
					organisationId:$scope.organizationId,
				      unitId:$scope.unitId
			}
			GenericService.serviceAction("POST",URI,data)
			.then(function(response){
				$scope.getActiveList = [];
				if(response.data.status == "success"){
					$scope.getDoctorList=response.data.listObject;
					$scope.getNurseList=response.data.listObject;
					$scope.getStaffList=response.data.listObject;
					$scope.getAdminList=response.data.listObject;
				}
			});
			}
			$scope.getActiveList();
			//--------------------------//
			
			//Get Sub Speciality List
			var commonObj = {
					unitId : $scope.unitId,
					organizationId : $scope.organizationId
				};
			
			var URI = BASE_URL + ROOT_URL
			+ GETACTIVESPECIALITY;

	GenericService
			.serviceAction(METHOD_POST, URI,
					commonObj,
					NOTIFICATION_MSG_STATUS_FALSE)
			.then(
					function(response) {
						console.log(response);
						// $rootScope.startLoader();
						if (response.data.status == "success")
							$scope.specialityList = response.data.listObject;
					});
			
			$scope.getSubSpecialityBySpeciality = function(id)
			{
				var data = {
						specialityId: id,
						unitId : $scope.unitId,
						organizationId : $scope.organizationId
					};
					var URI = BASE_URL + ROOT_URL
							+ GETSUBSPECIALITYBYSPECIALITYID;

					
					GenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.subSpecialityList = [];
										// $rootScope.startLoader();
										if (response.data.status == "success")
											$scope.subSpecialityList = response.data.listObject;
										console.log("subSpecialityList", $scope.subSpecialityList);
									});
				
			}
			//-----------------//
			
			// Get OPD billing bed Catagory List
			
			$scope.getOPDBillingBedCatagory= function()
			{
				
				var URI=BASE_URL + ROOT_URL+ "/api/unit/applicationConfiguration/getOPDBillingBedCatagory/"+$scope.organizationId+"/"+$scope.unitId;
				
				GenericService
					.serviceAction(METHOD_GET, URI, 
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response){
						
							$scope.opdBillingBedList = [];
							if(response.data.status=="success")
								 $scope.opdBillingBedList = response.data.listObject;
							console.log("opdBillingBedList",$scope.opdBillingBedList);
							});
			}
			$scope.getOPDBillingBedCatagory();
			//---------------//
			
			// Get Self Payee List
			
			$scope.getSelfPayee= function()
			{
				
				var URI=BASE_URL + ROOT_URL+ "/api/unit/applicationConfiguration/getSelfPayeeList/"+$scope.organizationId+"/"+$scope.unitId;
				
				GenericService
					.serviceAction(METHOD_GET, URI, 
							NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response){
						
							$scope.selfPayeeList = [];
							if(response.data.status=="success")
								 $scope.selfPayeeList = response.data.listObject;
							console.log("selfPayeeList",$scope.selfPayeeList);
							});
			}
			$scope.getSelfPayee();
			//---------------//
			

			//------------- Get All Services by Speciality and sub-speciality id=1000(Consultation) ---------------//
			/*/splId/{splId}/subSplId/{subSplId}*/
			/*$scope.getPatientServicesBySpecialityId = function(){
				try{
					$rootScope.startLoader();
					var URI = BASE_URL + ROOT_URL+ "/billing/service/consultations/org/"+$scope.organizationId+"/unit/"+$scope.unitId+"/splId/"+$scope.common.consultationSpecialityId+"/subSplId/"+$scope.common.consultationSubSpecialityId;
					GenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						if(response.data.status == "success")
							$scope.servicesList = response.data.listObject;
							console.log('service list',$scope.servicesList)
					});
				}catch (e) {
					console.log("Exception",e.message);
				}
			}*/
//			$scope.getPatientServicesBySpecialityId();
			$scope.getPatientServiceBySpeAndSubSpe=function(subSpeciality){
				
				console.log("IDS=",$scope.common.consultationSpecialityId+","+subSpeciality);
				if($scope.common.consultationSpecialityId!="" && $scope.common.consultationSpecialityId!=undefined && subSpeciality !=undefined && subSpeciality!="-"){
					try{
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL+ "/billing/service/consultations/org/"+$scope.organizationId+"/unit/"+$scope.unitId+"/splId/"+$scope.common.consultationSpecialityId+"/subSplId/"+subSpeciality;
						GenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							if(response.data.status == "success")
								$scope.servicesList = response.data.listObject;
								console.log('service list',$scope.servicesList)
								$rootScope.stopLoader();
						});
					}catch (e) {
						console.log("Exception",e.message);
					}
				}
			}
			//----------------//
			
			$scope.getConfigMasterList=function(){
				var data1 = {

						organizationId: $scope.organizationId,
						unitId: $scope.unitId

					};

					var URI1 = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";


					GenericService.serviceAction("POST", URI1,data1).then(function (response) {
						if (response.data.status == "success") {
//							$rootScope.stopLoader();
							$scope.configList = response.data.listObject;
							if($scope.configList.length>0)
							{
							 $scope.common=$scope.configList[0];
							   if($scope.common.consultationSpecialityId!=null && $scope.common.consultationSubSpecialityId!=null){
								 $scope.getSubSpecialityBySpeciality($scope.common.consultationSpecialityId);
								 $scope.getPatientServiceBySpeAndSubSpe($scope.common.consultationSubSpecialityId);
							   }
							}
							console.log("commonList", $scope.common);
							if($scope.common.bucketFlag==2 && $scope.common.bucketFlag != null){
								document.getElementById("textAreaDiv").style.display = "block";
							}else {
								document.getElementById("textAreaDiv").style.display = "none";
							}
							
							if($scope.common.smsType==2 && $scope.common.smsType != null){
								document.getElementById("textAreaDiv1").style.display = "block";
							}else {
								document.getElementById("textAreaDiv1").style.display = "none";
							}
							
						}
						
					});
			}
			$scope.getConfigMasterList();
			
			$scope.getAutoRenderService=function(){
				$scope.autoRenderServiceList=[];
				var URI = BASE_URL + ROOT_URL+ "/billing/service/autoRendered/"+$scope.organizationId+"/"+$scope.unitId;
				GenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					if(response.data.status == "success")
						$scope.autoRenderServiceList = response.data.listObject;
						console.log('autoRenderServiceList',$scope.autoRenderServiceList)
				});
			}
			$scope.getAutoRenderService();
		// function for popup ok button call
		$scope.PopupOkBtn = function () {
			$scope.popUpFlag = false;
			$scope.saveBilling();
		}
		
		$scope.saveBilling = function(){
			try {

				if ($scope.popUpFlag == false) {
					$rootScope.startLoader();
				}
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				// 
				
				var textareaaaaaa=	document.getElementById("textArea");
				
				$scope.common.localStorageJson = textareaaaaaa.value;
				
				var selectedType= document.getElementById("selectBox");
				$scope.common.bucketFlag = selectedType.value;
				//console.log("my valueee is :"+ $scope.common.bucketFlag);
				
				var smsTextArea=	document.getElementById("textArea1");
              $scope.common.smsTypejson = smsTextArea.value;
				
				var selectedType= document.getElementById("selectBox1");
				$scope.common.smsType = selectedType.value;
				console.log("my MSGGGG valueee is :"+ $scope.common.smsType);
				
				if($scope.obj.selectedPinPadDeviceId == "1"){
					$scope.common.selectedPinPadDevice="Pin Pad";
				}else if($scope.obj.selectedPinPadDeviceId == "2"){
					$scope.common.selectedPinPadDevice="POS Machine";
				}else{
					$scope.common.selectedPinPadDevice=null;
				}
				
				
				
				if(($scope.common.morningFromTime != undefined && $scope.common.morningFromTime != null && $scope.common.morningFromTime!= "") &&
						($scope.common.morningToTime == undefined || $scope.common.morningToTime == null || $scope.common.morningToTime== "") ){
					
						growl.error("Please select morning to time",
									{
										title: 'Error!'
									});
						return false;
					
				} 
				
				if(($scope.common.morningToTime != undefined && $scope.common.morningToTime != null && $scope.common.morningToTime!= "") &&
						($scope.common.morningFromTime == undefined || $scope.common.morningFromTime == null || $scope.common.morningFromTime== "") ){
					
						growl.error("Please select morning From time",
									{
										title: 'Error!'
									});
						return false;
					
				} 
				
				if(($scope.common.eveningFromTime != undefined && $scope.common.eveningFromTime != null && $scope.common.eveningFromTime!= "") &&
						($scope.common.eveningToTime == undefined || $scope.common.eveningToTime == null || $scope.common.eveningToTime== "") ){
					
						growl.error("Please select evening to time",
									{
										title: 'Error!'
									});
						return false;
					
				} 
				
				if(($scope.common.eveningToTime != undefined && $scope.common.eveningToTime != null && $scope.common.eveningToTime!= "") &&
						($scope.common.eveningFromTime == undefined || $scope.common.eveningFromTime == null || $scope.common.eveningFromTime== "") ){
					
						growl.error("Please select evening From time",
									{
										title: 'Error!'
									});
						return false;
					
				} 
				
				
				
				
				if(($scope.common.morningFromTime != undefined && $scope.common.morningFromTime != null && $scope.common.morningFromTime!= "")&&
						($scope.common.morningToTime != undefined && $scope.common.morningToTime != null && $scope.common.morningToTime!= "") ){
					var morningFromTime1 = getTimeAsNumberOfMinutes($scope.common.morningFromTime);
					var morningToTime2 = getTimeAsNumberOfMinutes($scope.common.morningToTime);
					
					if(parseInt(morningFromTime1) > parseInt(morningToTime2)){
						growl.error("Morning To Time should be greater than Morning From time",
									{
										title: 'Error!'
									});
						return false;
					}
					
				}
				
				if(($scope.common.eveningFromTime != undefined && $scope.common.eveningFromTime != null && $scope.common.eveningFromTime!= "")&&
						($scope.common.eveningToTime != undefined && $scope.common.eveningToTime != null && $scope.common.eveningToTime!= "") ){
					var eveningFromTime1 = getTimeAsNumberOfMinutes($scope.common.eveningFromTime);
					var eveningToTime2 = getTimeAsNumberOfMinutes($scope.common.eveningToTime);
					
					if(parseInt(eveningFromTime1) > parseInt(eveningToTime2)){
						growl.error("Evening To Time should be greater than Evening From time",
									{
										title: 'Error!'
									});
						return false;
					}
					
				}
				
					if($scope.common.backDateAdmissionDays ==0 && $scope.common.isBackDatedAdmission =='Y'){
						growl.error("Back Date Admission(Days) should be greater than Zero Days",
									{
										title: 'Error!'
									});
						return false;
					}
					
				var data = 
				{
					unitApplicationBillingConfigurationId: $scope.common.unitApplicationBillingConfigurationId,
					defaultSelfTariffId: $scope.common.defaultSelfTariffId,
					isPreOrPostBilling: $scope.common.isPreOrPostBilling,
					isAppointmentRequired: $scope.common.isAppointmentRequired,
					isCounterApplicable: $scope.common.isCounterApplicable,
					patientCategoryId: $scope.common.patientCategoryId,
					defaultPatientCategoryId: $scope.common.defaultPatientCategoryId,
					status: 'A',
					adminRoleId: $scope.common.adminRoleId,
					staffRoleId: $scope.common.staffRoleId,
					docRoleId: $scope.common.docRoleId,
					nurseRoleId: $scope.common.nurseRoleId,
					opdBillingCatId: $scope.common.opdBillingCatId,
					selfPayeeId: $scope.common.selfPayeeId,
					skipRISReportEntry : $scope.common.skipRISReportEntry,
					isServiceWiseSplitBill: $scope.common.isServiceWiseSplitBill,
					pdfGenerationUrl: $scope.common.pdfGenerationUrl,
					pdfGenerationUrlMis: $scope.common.pdfGenerationUrlMis,
					patientRegSerId: $scope.common.patientRegSerId,
					consultationSpecialityId: $scope.common.consultationSpecialityId,
					paediatricsSpecialityId: $scope.common.paediatricsSpecialityId,
					consultationSubSpecialityId: $scope.common.consultationSubSpecialityId,
					pharmacyServiceId: $scope.common.pharmacyServiceId,
					senderId: $scope.common.senderId,
					casualtySpecialityId: $scope.common.casualtySpecialityId,
					createdBy: $scope.createdBy,
					createdDate: $scope.dt,
					updatedBy: $scope.createdBy,
					updatedDate: $scope.dt,
					organizationId: $scope.organizationId,
					unitId: $scope.unitId,
					pharmacyReturnSaleServiceId: $scope.common.pharmacyReturnSaleServiceId,
					isKioskRequired: $scope.common.isKioskRequired,
					surgeonChargesServiceId: $scope.common.surgeonChargesServiceId,
					otRentServiceId: $scope.common.otRentServiceId,
					additionalOtrentService: $scope.common.additionalOtrentService,
					surchargeServiceId: $scope.common.surchargeServiceId,
					companyPatientCategory: $scope.common.companyPatientCategory,
					insurancePatientCategory: $scope.common.insurancePatientCategory,
					isNursingAssessment:$scope.common.isNursingAssessment,
					radiologySpecialityId: $scope.common.radiologySpecialityId,
					isRenderedServiceRateEditable:$scope.common.isRenderedServiceRateEditable,
					erSpecialityId:$scope.common.erSpecialityId,
					admissionChargesServiceId:$scope.common.admissionChargesServiceId,
					isSmartCardRequired:$scope.common.isSmartCardRequired,
					isDigitalSignatureRequired:$scope.common.isDigitalSignatureRequired,
					isPinPadRequired:$scope.common.isPinPadRequired,
					isShowPatientByBed:$scope.common.isShowPatientByBed,
					groupPracticeNo:$scope.common.groupPracticeNo,
					groupPracticeName:$scope.common.groupPracticeName,
					gradeWiseSeviceApplicable : $scope.common.gradeWiseSeviceApplicable,
					practiceNo:$scope.common.practiceNo,
					doctorSlipTDS:$scope.common.doctorSlipTDS,
					cashAmountValidation:$scope.common.cashAmountValidation,
					startDateFormat:$scope.common.startDateFormat,
					isCorporateEmpVerificationReq:$scope.common.isCorporateEmpVerificationReq,
					// isAdviceTabVisible :$scope.common.isAdviceTabVisible,
					isSendMailOnMailPrescription :$scope.common.isSendMailOnMailPrescription,
					isRateVisibleOnCoversheet :$scope.common.isRateVisibleOnCoversheet,
					isVitalQualifierVisible :$scope.common.isVitalQualifierVisible,
					isAdvisedDateVisible :$scope.common.isAdvisedDateVisible,
					skipTransferAcceptance :$scope.common.skipTransferAcceptance,
//					vaccinationChartHide :$scope.common.vaccinationChartHide,
					isPACSRequired : $scope.common.isPACSRequired,
					obgynSpecialityId :$scope.common.obgynSpecialityId,
					hemodialysisSpecialityId:$scope.common.hemodialysisSpecialityId,
					isConsolidatedBill:$scope.common.isConsolidatedBill,
					assetMaintenanceSpecialityId:$scope.common.assetMaintenanceSpecialityId,
					physiotherapySpecialityId: $scope.common.physiotherapySpecialityId,
					//isProcessShareAgainstSettledBill : $scope.common.isProcessShareAgainstSettledBill,
					morningFromTime:$scope.common.morningFromTime == ""?null:$scope.common.morningFromTime,
					morningToTime:$scope.common.morningToTime == ""?null:$scope.common.morningToTime,
					eveningFromTime:$scope.common.eveningFromTime == ""?null:$scope.common.eveningFromTime,
					eveningToTime:$scope.common.eveningToTime == ""?null:$scope.common.eveningToTime,
				    badDebtsDays : $scope.common.badDebtsDays == ""?0:$scope.common.badDebtsDays,
				    pddDays : ($scope.common.pddDays == null|| $scope.common.pddDays == "" || $scope.common.pddDays == undefined)?0:$scope.common.pddDays,
				    isSchemeDiscount : ($scope.common.isSchemeDiscount == "" || $scope.common.isSchemeDiscount == null || $scope.common.isSchemeDiscount == undefined)?'N':$scope.common.isSchemeDiscount,
				    isBackDatedAdmission : ($scope.common.isBackDatedAdmission == "" || $scope.common.isBackDatedAdmission == null || $scope.common.isBackDatedAdmission == undefined)?'N':$scope.common.isBackDatedAdmission,
				    skipReadyForBilling : $scope.common.skipReadyForBilling,
				    skipAdmissionAcceptance : ($scope.common.skipAdmissionAcceptance == "" || $scope.common.skipAdmissionAcceptance == null || $scope.common.skipAdmissionAcceptance == undefined)?'N':$scope.common.skipAdmissionAcceptance,
				    skipHousekeeping : ($scope.common.skipHousekeeping == "" || $scope.common.skipHousekeeping == null || $scope.common.skipHousekeeping == undefined)?'N':$scope.common.skipHousekeeping,
				    isFreeVisitReq : ($scope.common.isFreeVisitReq == "" || $scope.common.isFreeVisitReq == null || $scope.common.isFreeVisitReq == undefined)?'N':$scope.common.isFreeVisitReq,
				    isHospitalVisitReq : ($scope.common.isHospitalVisitReq == "" || $scope.common.isHospitalVisitReq == null || $scope.common.isHospitalVisitReq == undefined)?'N':$scope.common.isHospitalVisitReq,
				    isReviewAppReq : ($scope.common.isReviewAppReq == "" || $scope.common.isReviewAppReq == null || $scope.common.isReviewAppReq == undefined)?'N':$scope.common.isReviewAppReq,
				    isLabReq : ($scope.common.isLabReq == "" || $scope.common.isLabReq == null || $scope.common.isLabReq == undefined)?'N':$scope.common.isLabReq,
				    isEHCReq :($scope.common.isEHCReq == "" || $scope.common.isEHCReq == null || $scope.common.isEHCReq == undefined)?'N':$scope.common.isEHCReq,
				   	isRadiologyReq : ($scope.common.isRadiologyReq == "" || $scope.common.isRadiologyReq == null || $scope.common.isRadiologyReq == undefined)?'N':$scope.common.isRadiologyReq,
				   	isDocConsultReq :($scope.common.isDocConsultReq == "" || $scope.common.isDocConsultReq == null || $scope.common.isDocConsultReq == undefined)?'N':$scope.common.isDocConsultReq,
				   	isGstVisible : ($scope.common.isGstVisible == null|| $scope.common.isGstVisible == "" || $scope.common.isGstVisible == undefined)?0:$scope.common.isGstVisible,
				   	optimizeTransferFlow : ($scope.common.optimizeTransferFlow == null|| $scope.common.optimizeTransferFlow == "" || $scope.common.optimizeTransferFlow == undefined)?0:$scope.common.optimizeTransferFlow,
				    skipTransferAcceptance : ($scope.common.skipTransferAcceptance == null|| $scope.common.skipTransferAcceptance == "" || $scope.common.skipTransferAcceptance == undefined)?'N':$scope.common.skipTransferAcceptance,
				    vaccinationChartHide : ($scope.common.vaccinationChartHide == null|| $scope.common.vaccinationChartHide == "" || $scope.common.vaccinationChartHide == undefined) ? false:$scope.common.vaccinationChartHide,
				   	allowOtherDoctorToConsult :($scope.common.allowOtherDoctorToConsult == "" || $scope.common.allowOtherDoctorToConsult == null || $scope.common.allowOtherDoctorToConsult == undefined)?'N':$scope.common.allowOtherDoctorToConsult,
				   	diagnosisRequire  :($scope.common.diagnosisRequire == "" || $scope.common.diagnosisRequire == null || $scope.common.diagnosisRequire == undefined)?'N':$scope.common.diagnosisRequire,
				   	defaultDiscountOwnership :($scope.common.defaultDiscountOwnership == "" || $scope.common.defaultDiscountOwnership == null || $scope.common.defaultDiscountOwnership == undefined)? 0 :$scope.common.defaultDiscountOwnership,
				   	kioskLandingScreen:	($scope.common.kioskLandingScreen == "" || $scope.common.kioskLandingScreen == null || $scope.common.kioskLandingScreen == undefined)? 0 :$scope.common.kioskLandingScreen,
				   	biomedicalsChargesServiceId:$scope.common.biomedicalsChargesServiceId,
				   	hospitalChargesServiceId:$scope.common.hospitalChargesServiceId,
				   	mrdChargesServiceId:$scope.common.mrdChargesServiceId,
				   	emailId : $scope.common.emailId,
				   	password : $scope.common.password,
				   	isPaymentGatewayReqForWebApp : ($scope.common.isPaymentGatewayReqForWebApp == null|| $scope.common.isPaymentGatewayReqForWebApp == "" || $scope.common.isPaymentGatewayReqForWebApp == undefined)?'N':$scope.common.isPaymentGatewayReqForWebApp,
				   	isRisPatientDetailsOnFirstPage : ($scope.common.isRisPatientDetailsOnFirstPage == null|| $scope.common.isRisPatientDetailsOnFirstPage == "" || $scope.common.isRisPatientDetailsOnFirstPage == undefined)?'N':$scope.common.isRisPatientDetailsOnFirstPage,
				   	razorPayKeyId:$scope.common.razorPayKeyId,	
				   	razorPaySecretKey:$scope.common.razorPaySecretKey,
				   	isReportAddIntoDischargeSummary:$scope.common.isReportAddIntoDischargeSummary,
				   	admissionCancellationTime:$scope.common.admissionCancellationTime,
				   	isPackageApplicableDateEditable: $scope.common.isPackageApplicableDateEditable,
				   	webAppUserId : $scope.common.webAppUserId,
				   	mailProviderId : $scope.common.mailProviderId,
					mailCredential : $scope.common.mailCredential,
				   	isKioskAnnouncementRequired : $scope.common.isKioskAnnouncementRequired,
				   	cashAmountLimit : $scope.common.cashAmountLimit,
				   	bccEmailId : $scope.common.bccEmailId,
				   	whatsAppOption : parseInt($scope.common.whatsAppOption),
				   	blockReportPrint : $scope.common.blockReportPrint,
				   	payGatewayProviderId : $scope.common.payGatewayProviderId,
				   	emailValidationRequired : $scope.common.emailValidationRequired,
				   	mobileValidationRequired : $scope.common.mobileValidationRequired,
				   	cimsOption : $scope.common.cimsOption,
				   	dateFormat: $scope.common.dateFormat,
				   	selfPaidConsultAlert:$scope.common.selfPaidConsultAlert,
				   	serviceWiseDoctorListReq:$scope.common.serviceWiseDoctorListReq,
				   	depositTypeWiseRequired:$scope.common.depositTypeWiseRequired,
				   	registeredPinPadDevice:$scope.common.selectedPinPadDevice,
				   	backDateAdmissionDays : $scope.common.backDateAdmissionDays,
				   	appointmentUniqueIdSearch :  $scope.common.appointmentUniqueIdSearch,
				   	couponSystemApplicable : $scope.common.couponSystemApplicable,
				   	tokenNumber : $scope.common.tokenNumber,
				   	mandatoryScan : $scope.common.mandatoryScan,
				   	exceededBillAmountSettlement:$scope.common.exceededBillAmountSettlement,
				   	runTimeRateChangeUsingTarrif:$scope.common.runTimeRateChangeUsingTarrif,
				   	qmsPrnPrint : $scope.common.qmsPrnPrint,
				   	dischargeSummaryDataViewOptionId : $scope.common.dischargeSummaryDataViewOptionId,
				   	isEmrRequired : $scope.common.isEmrRequired,
				   	ambulanceChargesServiceId:$scope.common.ambulanceChargesServiceId,
				   	gstForBillingBedCategory:$scope.common.gstForBillingBedCategory,
				   	roa : $scope.common.roa,
				   	dosage : $scope.common.dosage,
				   	localStorageJson:$scope.common.localStorageJson,
				   	bucketFlag:$scope.common.bucketFlag,				   	
				   	smsTypejson:$scope.common.smsTypejson,
				   	smsType:$scope.common.smsType,				   	
				   	isBillManipulationAllowed : $scope.common.isBillManipulationAllowed,
				   	invoiceCreationInsurance : $scope.common.invoiceCreationInsurance,
				   	invoiceCreationCorporate : $scope.common.invoiceCreationCorporate,
				   	ispdfPasswordRequire  :($scope.common.ispdfPasswordRequire == "" || $scope.common.ispdfPasswordRequire == null || $scope.common.ispdfPasswordRequire == undefined)?'N':$scope.common.ispdfPasswordRequire,
				   	tallyIp : $scope.common.tallyIp,
				   	tallyPortNo : $scope.common.tallyPortNo,
				   	isTallyLocalHost : $scope.common.isTallyLocalHost,
				   	adminServiceMasterId : $scope.common.adminServiceMasterId,
				   	otherOrgLogoRequired : $scope.common.otherOrgLogoRequired,
				   	maxInputAmount : $scope.common.maxInputAmount,
				   	isAiDoctorEnabled : $scope.common.isAiDoctorEnabled

				}
				
				if ($scope.popUpFlag) {
//					$scope.showModal = !$scope.showModal;

				} else {
					var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/saveUnitApplicationBillingConfig";
					console.log("Save-URI-", URI);
					console.log(" save OBJ", data);
//					return false;
					GenericService.serviceAction("POST", URI, data).then(function (response) {

						if (response.data.status == 'success') {
							growl.success(response.data.message,
								{
									title: 'Success!'
								});
							
							$scope.billingInitMaster();
							$scope.getConfigMasterList();
							 $('#confirmLogOut').modal('show');
//							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							//  							$scope.$broadcast('show-errors-reset');

						} else if (response.data.status == 'error') {
							growl.error(response.data.message,
								{
									title: 'Error!'
								});
						}
					});
					$rootScope.stopLoader();
				}

			} catch (e) {
				console.log("Exception-", e);
			}
		}
		
		function getTimeAsNumberOfMinutes(time){
			
		    var timeParts = time.split(":");
		    var timeInMinutes = (timeParts[0] * 60) + timeParts[1];
		    return timeInMinutes;
		    
		}
		
		$scope.configDetails = {};

		$scope.logOut=function(){
			$('#confirmLogOut').hide('hide');
			$(".modal-backdrop").hide();
			$state.go('logout');
		}

	

		/**Billing End**/
		
		/**Laboratory Start**/
		$scope.labInitMaster = function () {
			$scope.searchName = '';
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;

			$scope.commonLabObj = {
					tLabConfigId: "",
					labSpecialityId: "",
					biochemistryId: "",
					hematologyId: "",
					microbiologyId: "",
					histopathologyId: "",
					cystopathologyId: "",
					serologyId: "",
					clinicalPathologyId:'',
					isPrebarcodeGenerantion: "N",
					biochemistryDept: "",
					heamotologyDept:"",
					microbioDept: "",
					histopathDept: "",
					cytopathDept: "",
					seologyDept:"",
					specialityName:"",
					id:"",
					isPinApplicable:false,
					isSendToCrApplicable:"N",
					isSampleAcceptanceApplicable :"N",
					isAcceptanceInDepartment : "N",
					isWorklistDepartment :"N",
                    isReportRecall: "N",
                    isValidationAndRelease :"N",
                    reportReleasePin : "N",
                    photoRequired : "N",
                    consolidatedResultEntry : "N",
                    groupRelease : "N",
                    triggerReleaseMail : "N",
                    isPRNPrint : "N",
                    collectSampleOnOPBilling : "N",
                    labSortingId :'',
                    skipValidation : 'N',
                    bySampleType : 'N',
                    byDepartment : 'N',
                    byContainer : 'N',
                    sendMailOnOrderCompletion : 'N',
                    manualSampleCollectionTime : 'N',
                    incubationMasterApplicable : 'N',
                    macroscopicExaminationApplicable : 'N',
                    biochemicalTestingApplicable : 'N',
                    microscopicExaminationApplicable : 'N',
                    qrCodeRequired : 'Y',
                    printLabReportBeforRelease : 'Y',
                    outsourceLabId:""
			};
			console.log("CommonLabObj-init", $scope.commonLabObj);
		}
		$scope.labInitMaster();
		
		
		$scope.labSortingList = [];
		
		$scope.getLabSortingMasterList = function () {

			var URI = BASE_URL + ROOT_URL + "/LIS/transaction/labSortingList";
			
			$scope.labSortingList = [];
			GenericService.serviceAction("GET", URI).then(function (response) {
				if (response.data.status == "success") {
					$scope.labSortingList = response.data.listObject;
					
				}
			});
		}
		
		$scope.getLabSortingMasterList();
		
		$scope.getLabConfigDetails=function(){
			var data1 = {
					organizationId: $scope.organizationId,
					unitId: $scope.unitId
				};	

				var URI = BASE_URL + ROOT_URL + "/lab/config/list/" + $scope.organizationId + "/" + $scope.unitId;

				GenericService.serviceAction("GET", URI).then(function (response) {
					if (response.data.status == "success") {
						$rootScope.stopLoader();
						$scope.commonLabObj = response.data.listObject[0];
						console.log("commonLabObj=", $scope.commonLabObj);
						
							if($scope.commonLabObj.outsourceLabId != undefined && $scope.commonLabObj.outsourceLabId != null && $scope.commonLabObj.outsourceLabId != "" ){
								if($scope.commonLabObj.outsourceLabId == 1){
									$scope.commonLabObj.isOutsourceLABIntegrationReq = 'Y';
									$scope.obj.selectedOutsourceLABId = 1;
								}
								else if($scope.commonLabObj.outsourceLabId == 2){
									$scope.commonLabObj.isOutsourceLABIntegrationReq = 'Y';
									$scope.obj.selectedOutsourceLABId = 2;
								}else{
									$scope.commonLabObj.isOutsourceLABIntegrationReq = 'N';
								}
								
							}
						
					}else{
						$rootScope.stopLoader();
					}
					
				});
		}
		$scope.getLabConfigDetails();
	
		
		
		
		
		// function for popup ok button call
		$scope.getSelectedLABId=function(id){
			$scope.commonLabObj.outsourceLabId=id;
		}
		
		$scope.PopupOkBtn = function () {
			$scope.popUpFlag = false;
			$scope.saveLab();
		}

		$scope.saveLab = function () {
			try {

				if ($scope.popUpFlag == false) {
					$rootScope.startLoader();
				}
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				//  					
				var data = {
					tLabConfigId:$scope.commonLabObj.tLabConfigId,
					labSpecialityId:$scope.commonLabObj.labSpecialityId,
					biochemistryId:$scope.commonLabObj.biochemistryId,
					hematologyId:$scope.commonLabObj.hematologyId,
					microbiologyId:$scope.commonLabObj.microbiologyId,
					histopathologyId:$scope.commonLabObj.histopathologyId,
					cystopathologyId:$scope.commonLabObj.cystopathologyId,
					serologyId:$scope.commonLabObj.serologyId,
					clinicalPathologyId:$scope.commonLabObj.clinicalPathologyId,
					isPrebarcodeGenerantion:$scope.commonLabObj.isPrebarcodeGenerantion,
					isPinApplicable:$scope.commonLabObj.isPinApplicable,
					isSendToCrApplicable:$scope.commonLabObj.isSendToCrApplicable,
					isSampleAcceptanceApplicable:$scope.commonLabObj.isSampleAcceptanceApplicable,
					isAcceptanceInDepartment:$scope.commonLabObj.isAcceptanceInDepartment,
					isWorklistDepartment:$scope.commonLabObj.isWorklistDepartment,
					status: 'A',
					createdBy: $scope.createdBy,
					createdDate: $scope.dt,
					updatedBy: $scope.createdBy,
					updatedDate: $scope.dt,
					organisationId: $scope.organizationId,
					unitId: $scope.unitId,
					biochemistryDept:"",
					heamotologyDept:"",
					microbioDept:"",
					histopathDept:"",
					cytopathDept:"",
					seologyDept:"",
					isReportRecall: $scope.commonLabObj.isReportRecall,
					isValidationAndRelease: $scope.commonLabObj.isValidationAndRelease,
					reportReleasePin: $scope.commonLabObj.reportReleasePin,
					photoRequired: $scope.commonLabObj.photoRequired,
					consolidatedResultEntry : $scope.commonLabObj.consolidatedResultEntry,
					groupRelease : $scope.commonLabObj.groupRelease,
					triggerReleaseMail : $scope.commonLabObj.triggerReleaseMail,
					isPRNPrint : $scope.commonLabObj.isPRNPrint,
					collectSampleOnOPBilling : $scope.commonLabObj.collectSampleOnOPBilling,
					labSortingId :$scope.commonLabObj.labSortingId,
					skipValidation : $scope.commonLabObj.skipValidation,
					bySampleType : $scope.commonLabObj.bySampleType,
	                byDepartment : $scope.commonLabObj.byDepartment,
	                byContainer : $scope.commonLabObj.byContainer,
	                sendMailOnOrderCompletion : $scope.commonLabObj.sendMailOnOrderCompletion,
	                manualSampleCollectionTime : $scope.commonLabObj.manualSampleCollectionTime,
	                incubationMasterApplicable : $scope.commonLabObj.incubationMasterApplicable,
	                macroscopicExaminationApplicable : $scope.commonLabObj.macroscopicExaminationApplicable,
	                biochemicalTestingApplicable : $scope.commonLabObj.biochemicalTestingApplicable,
	                microscopicExaminationApplicable : $scope.commonLabObj.microscopicExaminationApplicable,
	                qrCodeRequired : $scope.commonLabObj.qrCodeRequired,
	                printLabReportBeforRelease : $scope.commonLabObj.printLabReportBeforRelease ,
	                outsourceLabId : $scope.commonLabObj.isOutsourceLABIntegrationReq =='Y' ? $scope.commonLabObj.outsourceLabId: 0
				}

				if ($scope.popUpFlag) {
//					$scope.showModal = !$scope.showModal;

				} else {
					var URI = BASE_URL + ROOT_URL + "/lab/config";
					console.log("Save-URI-", URI);
					console.log(" save OBJ", data);
					$rootScope.startLoader();
					GenericService.serviceAction("POST", URI, data).then(function (response) {

						if (response.data.status == 'success') {
							growl.success(response.data.message,
								{
									title: 'Success!'
								});
							$scope.getLabConfigDetails();
							 $('#confirmLogOut').modal('show');
//							$scope.initMasterList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
							//$scope.$broadcast('show-errors-reset');

						} else if (response.data.status == 'error') {
							growl.error(response.data.message,
								{
									title: 'Error!'
								});
						}
					});
					$rootScope.stopLoader();
				}

			} catch (e) {
				console.log("Exception-", e);
			}
		}
		$scope.configDetails = {};


		$scope.showDetailsBtn = function (data) {
			$scope.configDetails = data;
		}

		//code for registration field configuration start
		$scope.registrationFieldObj = {
				unitApplRegistrationConfigId: "",
				prefix: "Y",
				firstName: "Y",
				middleName: "N",
				sourceId:"N",
				lastName: "N",
				birthDate: "Y",
				sex: "Y",
				callingCode: "N",
				email:"N",
				mobileNo: "Y",
				mobileDigit:"10",
				nationality:"N",
				identificationDoc:"N",
				race:"N",
				maritalStatus:"N",
				occupation:"N",
				patientCategory:"Y",
				photo:"N",
				thumbImpression:"N",
				address : "N",
				kinDetails : "N",
				kinprefix :"N",
				kinName:"N",
				kinMobileNo:"N",
				kinAddress:"N",
				relationId : "N",
				phoneNo :"N",
				identificationId :"N",
				identificationNo :"N",
				countryId :"N",
				stateId :"N",
				districtId:"N",
				cityId:"N",
				areaId:"N",
				postCode:"N"
				}
		$scope.PopupOkBtn = function () {
			$scope.popUpFlag = false;
			$scope.saveRegistrationFieldConfiguration();
		}
		$scope.saveRegistrationFieldConfiguration = function () {
			try {

				if ($scope.popUpFlag == false) {
					$rootScope.startLoader();
				}
				var date = new Date();
				$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
				//  					
				var data = {
						unitApplRegistrationConfigId:$scope.registrationFieldObj.unitApplRegistrationConfigId,
						prefix:$scope.registrationFieldObj.prefix,
						firstName:$scope.registrationFieldObj.firstName,
						middleName:$scope.registrationFieldObj.middleName,
						lastName:$scope.registrationFieldObj.lastName,
						birthDate:$scope.registrationFieldObj.birthDate,
						sex:$scope.registrationFieldObj.sex,
						callingCode:$scope.registrationFieldObj.callingCode,
						codeDigit:$scope.registrationFieldObj.codeDigit,
						mobileNo:$scope.registrationFieldObj.mobileNo,
						email:$scope.registrationFieldObj.email,
						mobileDigit:$scope.registrationFieldObj.mobileDigit,
						nationality:$scope.registrationFieldObj.nationality,
						identificationDoc:$scope.registrationFieldObj.identificationDoc,
						race:$scope.registrationFieldObj.race,
						maritalStatus:$scope.registrationFieldObj.maritalStatus,					
						occupation:$scope.registrationFieldObj.occupation,
						patientCategory:$scope.registrationFieldObj.patientCategory,
						photo:$scope.registrationFieldObj.photo,
						thumbImpression:$scope.registrationFieldObj.thumbImpression,
						address:$scope.registrationFieldObj.address,
						kinDetails:$scope.registrationFieldObj.kinDetails,
//						status: 'A',
						saveUpdateFlag:"Y",
						createdBy: $scope.createdBy,
						createdDate: $scope.dt,
						updatedBy: $scope.createdBy,
						updatedDate: $scope.dt,
						organizationId: $scope.organizationId,
						unitId: $scope.unitId,
						sourceId:$scope.registrationFieldObj.sourceId,
						kinprefix:$scope.registrationFieldObj.kinprefix,
						kinName:$scope.registrationFieldObj.kinName,
						kinMobileNo:$scope.registrationFieldObj.kinMobileNo,
						kinAddress:$scope.registrationFieldObj.kinAddress,
						relationId:$scope.registrationFieldObj.relationId,
						phoneNo:$scope.registrationFieldObj.phoneNo,
						identificationId:$scope.registrationFieldObj.identificationId,
						identificationNo:$scope.registrationFieldObj.identificationNo,
						countryId:$scope.registrationFieldObj.countryId,
						stateId:$scope.registrationFieldObj.stateId,
						districtId:$scope.registrationFieldObj.districtId,
						cityId:$scope.registrationFieldObj.cityId,
						areaId:$scope.registrationFieldObj.areaId,
						postCode:$scope.registrationFieldObj.postCode
				}

				if ($scope.popUpFlag) {
//					$scope.showModal = !$scope.showModal;

				} else {
					var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/saveUnitApplicationRegistrationFieldConfig";
					console.log("Save-URI-", URI);
					console.log(" save OBJ", data);

					GenericService.serviceAction("POST", URI, data).then(function (response) {

						if (response.data.status == 'success') {
							growl.success(response.data.message,
								{
									title: 'Success!'
								});
							$scope.getRegistrationFieldConfiguration();

							//$scope.$broadcast('show-errors-reset');

						} else if (response.data.status == 'error') {
							growl.error(response.data.message,
								{
									title: 'Error!'
								});
						}
					});
					$rootScope.stopLoader();
				}

			} catch (e) {
				console.log("Exception-", e);
			}
		}
		
		$scope.getRegistrationFieldConfiguration = function () {
			var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/"+$scope.organizationId+"/"+$scope.unitId;
			

			GenericService.serviceAction("GET", URI).then(function (response) {
				if (response.data.status == 'success') {
					
					$scope.fieldConfigList=response.data.listObject;
					if($scope.fieldConfigList.length>0)
						{
						 $scope.registrationFieldObj=$scope.fieldConfigList[0];
						}
					


				} else if (response.data.status == 'error') {
					growl.error(response.data.message,
						{
							title: 'Error!'
						});
				}
			});
		}
		$scope.resetDigit=function(flag,forWhom){
			alert(flag);
			if(flag=='Y' && forWhom=="mobNo"){
				$scope.registrationFieldObj.mobileDigit="";
			}
			if(flag=='Y' && forWhom=="callCode"){
				$scope.registrationFieldObj.codeDigit="";
			}
		}
		$scope.getRegistrationFieldConfiguration();
		//code for registration field configuration End
		/**List And Pagination Start**/
		$scope.setNoOfRecords = function () {
			$scope.initMasterList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
		};

		$scope.initMasterList = function (organizationId, offset, noOfRecordsPerPage, unitId) {
			$rootScope.startLoader();

			offset = offset != null ? offset : 0;
			noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
			var data1 = {

				organizationId: $scope.organizationId,
				unitId: $scope.unitId

			};
		

			var URI = BASE_URL + ROOT_URL + "/lab/config/list/" + $scope.organizationId + "/" + $scope.unitId;

			GenericService.serviceAction("GET", URI).then(function (response) {
				if (response.data.status == "success") {
					$rootScope.stopLoader();
					$scope.commonLabList = response.data.listObject;
					console.log("commonLabList", $scope.commonLabList);
				}
			});

		}


		$scope.initMasterList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);

		/**List And Pagination End**/
		/**Laboratory End**/
		
		
		//First Level
		
		$scope.firstTabLevel = 1 ;
		
		$scope.setFirstTabLevel= function(newTab)
		{
			$scope.firstTabLevel = newTab;
			
		};
		$scope.isSetFirstTabLevel = function(tabNum)
		{
			return $scope.firstTabLevel === tabNum;
		};
		
		$scope.webAppIntegrationUserList = [];
		$scope.getUserListForWebApp = function(){
			$rootScope.startLoader();
			var data = {
				organizationId: $scope.organizationId,
				unitId: $scope.unitId
			};
		
			var URI = BASE_URL + ROOT_URL + "/adt/getUserListForWebAppByOrgIdAndUnitId";
			GenericService.serviceAction("POST", URI,data).then(function (response) {
				if (response.data.status == "success") {
					$rootScope.stopLoader();
					$scope.webAppIntegrationUserList = [];
					$scope.webAppIntegrationUserList = response.data.listObject;
				}
			});
		}
		$scope.getUserListForWebApp();
		
		$scope.checkValidationAndRelease = function(){
				$scope.commonLabObj.skipValidation = $scope.commonLabObj.isValidationAndRelease;
		}

        $scope.getAdminServices = function () {

            var URI = BASE_URL + ROOT_URL + "/api/global/getAdminServicesForConfiguration";
            var data = {
                unitId: $scope.unitId
            }
            $scope.adminServiceList = [];
            GenericService.serviceAction("POST", URI, data).then(function (response) {
                if (response.data.status == "success") {
                    $rootScope.stopLoader();
                    $scope.adminServiceList = response.data.listObject;

                    console.log("adminServiceList", $scope.adminServiceList);
                }
            });
        }
        $scope.getAdminServices();
	}

	function config($stateProvider) {
		$stateProvider.state("applicationConfiguration", {
			url: "/applicationConfiguration",
			templateUrl: 'views/master/UnitMaster/configuration/application-master.html',
			controller: "ApplicationConfiguration.Controller",
			title: 'Application Configuration',
			resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 660);
	   				 	});
	   					if(arr.length <= 0){
	   						$state.go("accessDenied")
	   						deferred.reject();
	   					 } else {
	   						deferred.resolve();
	   					 }
	   		        });
	   		
	   		        return deferred.promise;
	   		      }
	   		 },
			controllerAs: "vm"
		});
	}



	angular
		.module("myApp")
		.config(config)
		.controller("ApplicationConfiguration.Controller", applicationConfigurationController);
})();
