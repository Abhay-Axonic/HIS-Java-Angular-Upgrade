
(function() {
	"use strict";

	function detailsReturnOfEmptyAmpoulesController($scope, $rootScope, $http,
			CONSTANTS, GenericService, $filter, $state, growl, CurrencyService,
			StatusService, commonDetailService, $cookies, GenericServiceParamHeader) {
		var self = this;

		// Default Constructor
		var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
		function initializeController() {
			$rootScope.loginpage = true;

			self.model = {
				SelectedRequestItems : []
			};

			// Methods
			CurrencyService.GetCurrency().then(function(currency) {
				self.model.Currency = currency;
			})
			StatusService.GetStatus().then(function(status) {
				self.model.Status = status;
			})

			self.model.GetReturnEmptyAmpoulesDetails = GetReturnEmptyAmpoulesDetails;
			self.model.SavePopup = SavePopup;
			self.model.SelectAllRequests = SelectAllRequests;
			self.model.SelectRequestItem = SelectRequestItem;
			self.model.SaveReturnEmptyAmpoules = SaveReturnEmptyAmpoules;
			self.model.NavigateToReturnOfEmptyAmpoules = NavigateToReturnOfEmptyAmpoules;
			self.model.ClearFields = ClearFields;
			 self.model.checkQuantity = checkQuantity;
			 self.model.getAllPatientDiagnosisForIcon = self.model.getAllPatientDiagnosisForIcon;
			GetReturnEmptyAmpoulesDetails(commonDetailService.getDataId());
		}

		var GetReturnEmptyAmpoulesDetails = function(itemId) {
			var URI = CONSTANTS.PHARMACY.NURSING.RETURN_EMPTY_AMPOULES.DETAIL_API
					+ itemId;

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						self.model.ReturnEmptyAmpoulesDetails = response.data;
						self.model.statusId=response.data.statusId;
						$rootScope.allergydata(response.data);
		                getAllPatientDiagnosisForIcon(response.data.patientId);
						self.model.ReturnEmptyAmpoulesDetails.items.forEach(function (item) {
							self.model.consumedQty=item.consumedQty;
						});
					});
			$rootScope.stopLoader();
		}

		var SelectAllRequests = function() {

			if (self.model.selectAllRows) {
				self.model.SelectedRequestItems = [];
				self.model.ReturnEmptyAmpoulesDetails.items.forEach(function(
						item) {
					item.isSelected = true;
					self.model.SelectedRequestItems.push(item);

				});
			} else {
				self.model.SelectedRequestItems = [];
				self.model.ReturnEmptyAmpoulesDetails.items.forEach(function(
						item) {
					item.isSelected = false;
				});
			}
			// console.log(self.model.SelectedRequestItems);
		}

		var isSelected = function(item) {
			return item.isSelected;
		}

		var SelectRequestItem = function(item) {

			var itemIds = self.model.SelectedRequestItems;

			if (item.isSelected) {
				itemIds.push(item);
			} else {
				var index = itemIds.indexOf(item);
				if (index > -1) {
					itemIds.splice(index, 1);
				}
			}

			if (self.model.ReturnEmptyAmpoulesDetails.items.every(isSelected)) {
				self.model.selectAllRows = true;
			} else {
				self.model.selectAllRows = false;
			}
			// console.log(itemIds);
		}

		var SavePopup = function() {
			var popup = angular.element('#confirmSave');
			popup.modal('show');
		}
		var checkQuantity= function(item){self.model.SelectedRequestItems
			.forEach(function(billitem) {

				if (billitem.quantityIssued == billitem.returedQuantity) {

					growl.error('No enough quantity available.')
				}
			});
		}
		
		
		var ValidateBill = function(bill) {

			var isValid = true;

			if (!self.model.SelectedRequestItems
					|| self.model.SelectedRequestItems.length <= 0) {
				isValid = false;
				growl.error("No items in the list to save");
			} 
			else {
				$scope.totalConsumedQuantity=0;
				$scope.totalReturnedQty=0;
				$scope.totalReturnQuantity=0;
				/*self.model.SelectedRequestItems
						.forEach(function(billitem) {*/
				bill.items.forEach(function(billitem) {
					var returnedQtyNew=billitem.returedQuantity;
					var consumedQtyNew=billitem.consumedQty;
					$scope.totalReturnedQty += parseInt(returnedQtyNew);
					$scope.totalConsumedQuantity += parseInt(consumedQtyNew);
					
					billitem['isQuantityValid'] = false;
					self.model.statusId=2;
					/*if(billitem.isSelected==true){
						var returnQtyNew=billitem.returnQuantity;
								$scope.totalReturnQuantity +=parseInt(returnQtyNew);
							 if(billitem.returnQuantity > (billitem.quantityIssued - billitem.consumedQty - billitem.returedQuantity)){
									billitem['isQuantityValid'] = true;
									isValid = false;
									growl.error('Enter valid quantity')
								}
								
							 else if (billitem.quantityIssued == billitem.returedQuantity) {

								billitem['isQuantityValid'] = true;
								isValid = false;
								growl.error('No enough quantity available.')
							}
							else if ((!billitem.returnQuantity)
									|| (billitem.quantityIssued < billitem.returnQuantity)) {

								billitem['isQuantityValid'] = true;
								isValid = false;
								growl.error('Enter valid quantity')
							}
							else if(billitem.returedQuantity > 0 && (parseInt(billitem.returedQuantity)+parseInt(billitem.returnQuantity))==billitem.quantityIssued){
								self.model.statusId=2;
							}
							else if((parseInt(billitem.consumedQty + billitem.rejectedQuantity)+parseInt(billitem.returedQuantity))==billitem.quantityIssued){
								self.model.statusId=2;
							}
							else if(billitem.quantityIssued > billitem.returnQuantity && billitem.returedQuantity!=undefined){
								billitem['isQuantityValid'] = false;
								isValid = true;
								self.model.statusId=3;
							}
							
							}*/
					if(billitem.isSelected==true){
						var returnQtyNew=billitem.returnQuantity;
								$scope.totalReturnQuantity +=parseInt(returnQtyNew);
							 if(billitem.returnQuantity > (billitem.consumedQty - billitem.returedQuantity)){
									billitem['isQuantityValid'] = true;
									isValid = false;
									growl.error('Enter valid quantity')
								}
								
							 else if (billitem.consumedQty == billitem.returedQuantity) {

								billitem['isQuantityValid'] = true;
								isValid = false;
								growl.error('No enough quantity available.')
							}
							else if ((!billitem.returnQuantity)
									|| (billitem.consumedQty < billitem.returnQuantity)) {

								billitem['isQuantityValid'] = true;
								isValid = false;
								growl.error('Enter valid quantity')
							}
							else if(billitem.returedQuantity > 0 && (parseInt(billitem.returedQuantity)+parseInt(billitem.returnQuantity))==billitem.consumedQty){
								self.model.statusId=2;
							}
							else if((parseInt(billitem.consumedQty + billitem.rejectedQuantity)+parseInt(billitem.returedQuantity))==billitem.consumedQty){
								self.model.statusId=2;
							}
							else if(billitem.consumedQty > billitem.returnQuantity && billitem.returedQuantity!=undefined){
								billitem['isQuantityValid'] = false;
								isValid = true;
								self.model.statusId=3;
							}
							
							}
						});
			}
			if(($scope.totalConsumedQuantity == $scope.totalReturnedQty)|| ($scope.totalConsumedQuantity ==$scope.totalReturnQuantity) ||
            		($scope.totalConsumedQuantity ==(parseInt($scope.totalReturnQuantity)+ parseInt($scope.totalReturnedQty)))){
				self.model.statusIdNew=2;
            }else{
            	self.model.statusIdNew=3;
            }
			return isValid;
		}
		
		
		
		var SaveReturnEmptyAmpoules = function(bill) {

			if (ValidateBill(bill)) {
				var URI = CONSTANTS.PHARMACY.NURSING.RETURN_EMPTY_AMPOULES.SAVE_API;
				// need to change status id
				var data = {
					"id" : !!bill.id ? bill.id : undefined,
					"statusId" : self.model.statusIdNew,
					"items" : [],
					"unitId":$scope.unitId,
		            "organizationId":$scope.organizationId,
		            "userId":$scope.userId,
		            "storeId":$scope.storeId
				}
				angular.forEach(bill.items, function(item, key) {
					if (item.isSelected) {

						// var statusId=
						var returnItem = {
							"id" : item.id,
							/*
							 * "returnQuantity": Number(item.returnQuantity),
							 * "pendingQuantity": Number(item.pendingQuantity),
							 */
							"quantityAccepted" : Number(item.returnQuantity),
							"quantityReturned" : Number(item.pendingQuantity),
							"statusId" : self.model.statusId,
						}
						data.items.push(returnItem);
					}
				});

				console.log(data)

				$rootScope.startLoader();
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									ClearFields(bill);
									growl
											.success('Return of empty ampoules Successfully saved');
									$rootScope.stopLoader();
									NavigateToReturnOfEmptyAmpoules();
								},
								function(err) {
									$rootScope.stopLoader();
									growl
											.error('Error while saving return of empty ampoules');
								});
			}
		}

		
		   var getAllPatientDiagnosisForIcon= function(patientId){
	            self.model.patientDiagnosisListIcon =[];
	            var cookieObject = $cookies.getObject('cookieObject');
	             var headerObjDia = {
	                         'unit_id' : cookieObject.unitId,
	                        'organization_id' : cookieObject.organizationId,
	                        'patient_id' : patientId,
	                       'Content-Type':'application/json',
	             };
	             var data={};
	             var URI = CONSTANTS.ROOT_URL
	             + "/api/opd/getPatientDiagnosisByPatientIdForIcon";
	             GenericServiceParamHeader
	             .serviceAction(METHOD_GET, URI,
	                data,
	                NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
	             .then(
	                     function(response) {
	                         console.log(response);
	                         // $rootScope.startLoader();
	                         if (response.data.status == "success")
	                         self.model.patientDiagnosisListIcon = response.data.listObject; 
	                         console.log("patientDiagnosisListIcon",self.model.patientDiagnosisListIcon);
	                             self.model.comorbidityFlag = false;
	                             self.model.notifiableFlag = false;
	                            
	                                if(self.model.patientDiagnosisListIcon.length > 0)
	                                    {
	                                        self.model.complaintDelete=false;
	                                        angular.forEach(self.model.patientDiagnosisListIcon,function(value,key) {
	                                            if(value.isNotifiable == 'Y')
	                                            {
	                                                self.model.notifiableFlag = true;
	                                            }
	                                            else if(value.isComorbidity == 'Y')
	                                            {
	                                                self.model.comorbidityFlag = true;
	                                            }
	                                        });
	                                    }
	                         
	                     });
	            }
		   
		var NavigateToReturnOfEmptyAmpoules = function() {
			document.getElementById('popupClose').click();
			setTimeout(function() {
				$state.go('returnOfEmptyAmpoules');
			}, 1000);
		}

		var ClearFields = function(bill) {

			bill.Id = "1" + (new Date()).getTime();
			bill.uhid = null;
			bill.patientDetail = null;
			bill.dob = null;
			bill.pdd = null;
			bill.doa = null;
			bill.wardName = null;
			bill.bedNo = null;
			bill.prescriptionDate = null;
			bill.doctorDetail = null;
			bill.prescriptionId = null;

			bill.items = [];
		}



		initializeController();
	}

	function config($stateProvider) {
		$stateProvider
				.state(
						"detailsReturnOfEmptyAmpoules",
						{
							url : "/detailsReturnOfEmptyAmpoules",
							templateUrl : 'views/PharmacyInventory/NarcoticsStore/details-return-of-empty-ampoules.html',
							controller : "DetailsReturnOfEmptyAmpoules.Controller",
							controllerAs : "vm",
							title : 'Return Empty Ampoules'
						});
	}

	angular.module("myApp").config(config).controller(
			"DetailsReturnOfEmptyAmpoules.Controller",
			detailsReturnOfEmptyAmpoulesController)
})();
