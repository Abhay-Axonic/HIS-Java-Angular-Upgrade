(function() {
	"use strict";

	function dischargeMedicationReconciliationController($scope, $rootScope,
			$http, CONSTANTS, GenericService, growl, $cookies,PaginationService) {
		var self = this;

		// Default Constructor
		var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
		function initializeController() {
			$rootScope.loginpage = true;
			
			var date = new Date();
			self.model = {
				fromDate : new Date(),
				toDate : new Date(),
				fromDateOptions : {
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date(),
					minDate : date.setDate((new Date()).getDate() - 90),
				},

				toDateOptions : {
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date(),
//					minDate : date.setDate((new Date()).getDate() - 90),
				},
				statusDataList : [ {
					"status" : "All"
				} ],
				floors : [ {
					"floor" : "All Floors"
				} ],
				wards : [ {
					"wardName" : "All Wards"
				} ],

				visitTypes : [ {
					"type" : "All Visit Types"
				} ],
				nursingStations : [ {
					"nursingStationDesc" : "All Nursing Stations"
				} ],
				
				 pagination: {
	                    currentPage: 1,
	                    totalPages: 0,
	                    pageSize: '10',
	                    search: '',
	                    searchData: PaginationService.searchData,
	                    setTotalPages: PaginationService.setTotalPages,
	                    getPagesList: PaginationService.getPagesList,
	                    setPage: PaginationService.setPage,
	                    setFirstPage: PaginationService.setFirstPage,
	                    setPreviousPage: PaginationService.setPreviousPage,
	                    setNextPage: PaginationService.setNextPage,
	                    setLastPage: PaginationService.setLastPage,
	                    sortBy: PaginationService.sortBy
	                  }

			};
			self.model.OpenFromDate = OpenFromDate;
			self.model.OpenToDate = OpenToDate;
			self.model.ReconcilationData = ReconcilationData;
			self.model.GetMedicationReconcilation = GetMedicationReconcilation;
			self.model.saveMedicationReconcilation = saveMedicationReconcilation;
			self.model.qtyValidation = qtyValidation;
			self.model.calculateQty = calculateQty;
			self.model.patientName = '';
			self.model.doctorName= '';
			self.model.uhid= '';
			self.model.search = '';
            self.model.prevSearch = '';
			self.model.nursingStationDesc = '';
			PopulateDropdown();
			ReconcilationData();
			$scope.saveFlag = false;

		}
		var OpenFromDate = function() {
			self.model.fromDateOpened = true;
			self.model.toDateOptions.minDate = self.model.fromDate;
		}

		var OpenToDate = function() {
			self.model.toDateOpened = true;
			self.model.toDateOptions.minDate = self.model.fromDate;
		}

		var PopulateDropdown = function() {
			var discharge = self.model;
			discharge.status = discharge.statusDataList[0];
			discharge.floor = discharge.floors[0];
			discharge.ward = discharge.wards[0];
			discharge.visitType = discharge.visitTypes[0];
			// discharge.nursingStationDesc=discharge.nursingStations[0];
			var URI = CONSTANTS.PHARMACY.NURSING.DISCHARGE_RECONCILATION.DROPDOWN_API + '/' + $scope.unitId;
			$rootScope.startLoader();

			GenericService
					.serviceAction("GET", URI, {})
					.then(
							function(response) {
								discharge.statusDataList = discharge.statusDataList
										.concat(response.data.status);
								discharge.floors = discharge.floors
										.concat(response.data.floor);
								discharge.wards = discharge.wards
										.concat(response.data.ward);
								discharge.visitTypes = discharge.visitTypes
										.concat(response.data.visitType);
								discharge.nursingStations = discharge.nursingStations
										.concat(response.data.nursingStations);
								console
										.log(
												"Selected Nursing Station : ",
												JSON
														.parse(sessionStorage.getItem('nursingStationDesc')));
								for (var i = 0; i < discharge.nursingStations.length; i++) {
									if (discharge.nursingStations[i].nursingStationDesc == JSON
											.parse(sessionStorage.getItem('nursingStationDesc'))) {
										discharge.nursingStationDesc = discharge.nursingStations[i];
										console.log("Nursing Station :",
												discharge.nursingStationDesc);
									}
								}
								$rootScope.stopLoader();
							}, function(err) {
								$rootScope.stopLoader();
							});
		}
		var ReconcilationData = function() {
			var URI = CONSTANTS.PHARMACY.NURSING.DISCHARGE_RECONCILATION.SEARCH_API;
			var data = {
				"fromDate" : moment(self.model.fromDate).format('YYYY-MM-DD'),
				"toDate" : moment(self.model.toDate).format('YYYY-MM-DD'),
				"visitType" : !!self.model.visitType
						&& !!self.model.visitType.id ? self.model.visitType.id
						: undefined,
			/*	"patientId" : !!self.model.patientId ? self.model.patientId
						: undefined,*/
				"statusId" : !!self.model.status && !!self.model.status.id ? self.model.status.id
						: undefined,
				
				"patientName" : !!self.model.patientName ? self.model.patientName
								: undefined,
								
				"doctorName" : !!self.model.doctorName ? self.model.doctorName
										: undefined,
				"uhid" : !!self.model.uhid ? self.model.uhid : undefined,
				"wardId" : !!self.model.ward && !!self.model.ward.id ? self.model.ward.id
								: undefined,
				"floorId" : !!self.model.floor && !!self.model.floor.id ? self.model.floor.id
										: undefined,				
				"nursingStationId" : !!self.model.nursingStationDesc && !!self.model.nursingStationDesc.nursingStationId ? self.model.nursingStationDesc.nursingStationId : undefined,
				"unitId":$scope.unitId
											
			};
			$rootScope.startLoader()
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						$rootScope.stopLoader();
						self.model.pagination.data=[];
						self.model.dischargeReconciliationData = response.data;
						 if(!!response && !!response.data && response.data.length > 0){            
	                            PaginationService.searchData(self.model.dischargeReconciliationData, self.model.pagination);
	                          }
						self.model.selectAllRows = false;
						self.model.isItemChecked = false;

					}, function errorCallback(response) {
						$rootScope.stopLoader();
						growl.error('Error');
					});
		}
		
		var GetMedicationReconcilation = function(data, id, statusId) {

			if (statusId == 4) {
				$scope.saveFlag = true;
			}
			else
				{
				$scope.saveFlag = false;
				}
			
			var URI = CONSTANTS.PHARMACY.NURSING.DISCHARGE_RECONCILATION.DETAIL_API + '/' + $scope.unitId +'/'+ id;

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						
						self.model.ItemDetailList = response.data;
						 
						self.model.ItemDetailList.statusId = statusId;
						if(self.model.ItemDetailList.items.length == 0)
						{
								$scope.saveFlag = true;
						}
						 angular.forEach(self.model.ItemDetailList.items,function(i){
							 self.model.qtyValidation(i);
							 self.model.calculateQty(i)
					            if(i.totalIssuedQty==undefined || i.totalIssuedQty ==0){
					            	$scope.saveFlag = true;
					            }
					          });						
						$('#medicationDetailsModal').modal();
						 $('#medicationDetailsModal').show();
						 $rootScope.allergydata(data);
						$rootScope.stopLoader();
					}, function(err) {
						$rootScope.stopLoader();
					});
		}

		var saveMedicationReconcilation = function() {
			var discharge = self.model;

			var URI = CONSTANTS.PHARMACY.NURSING.DISCHARGE_RECONCILATION.SAVE_API;

			var data = {
				"id" : discharge.ItemDetailList.id,
				"statusId" : 2,
				"items" : [],
				"unitId":$scope.unitId,
	            "organizationId":$scope.organizationId,
	            "userId":$scope.userId,
	            "storeId":$scope.storeId

			};
		    $scope.saveFlagApi=true;
			angular.forEach(discharge.ItemDetailList.items,
					function(item, key) {
				//if(item.qtyToOrdered == 0)
				if(item.qtyToOrdered == 0 || Number(item.qtyToOrdered) > (item.orderedQty+(item.totalIssuedQty-item.consumedQty)))
				{
					growl.error('Enter valid Qty to ordered');
                    $scope.saveFlagApi=false;
				}
						var storeItem = {
							"id" : item.id,
							// "consumedQty":item.consumedQty,
							"quantityToOrdered" : item.qtyToOrdered
						}
						data.items.push(storeItem);
					});
			 if($scope.saveFlagApi == true)
	           {
			$rootScope.startLoader();
			GenericService.serviceAction("PATCH", URI, data).then(
					function(response) {
						 $('#medicationDetailsModal').hide();
						$('body').removeClass('modal-open');
                         $('.modal-backdrop').remove();
						ReconcilationData();
						$rootScope.stopLoader();
						growl.success(response.data.message);
					},

					function(err) {
						$rootScope.stopLoader();
						growl.error('Something Went Wrong');
					});
	           }
			 else
				 {
				 $('#medicationDetailsModal').modal();
				 }
		}

		var qtyValidation = function(item){
			var consumedQty = parseInt(item.consumedQty);
			if(consumedQty > (item.totalIssuedQty - item.returnQty)){
				growl.error("Entered consume qty is less than issued qty");
				item.consumedQty = 0;
			}
		}

		var calculateQty = function(item){
			var qtyOrdered = item.totalIssuedQty - item.returnQty - item.consumedQty;
			var finalOrderedqty=item.orderedQty - qtyOrdered;
			if(finalOrderedqty>0){
				item.qtyToOrdered = finalOrderedqty;
			}else{
				item.qtyToOrdered =0;
			}
			
			
		}

		initializeController();
	}

	function config($stateProvider) {
		$stateProvider
				.state(
						"dischargeMedicationReconciliation",
						{
							url : "/dischargeMedicationReconciliation",
							templateUrl : 'views/PharmacyInventory/InPatient/discharge-medication-reconciliation.html',
							controller : "DischargeMedicationReconciliation.Controller",
							title : 'Medication at Hospital Discharge Reconciliation',
							resolve : {

								data : function($q, $state, $timeout) {
									var deferred = $q.defer();

									$timeout(function() {
										var arr = JSON.parse(localStorage
												.getItem('permissions')).modules
												.filter(function(item) {
													return (item.id == 72);
												});
										if (arr.length <= 0) {
											$state.go("accessDenied")
											deferred.reject();
										} else {
											deferred.resolve();
										}
									});

									return deferred.promise;
								}
							},
							controllerAs : "vm"
						});
	}

	angular.module("myApp").config(config).controller(
			"DischargeMedicationReconciliation.Controller",
			dischargeMedicationReconciliationController);
})();
