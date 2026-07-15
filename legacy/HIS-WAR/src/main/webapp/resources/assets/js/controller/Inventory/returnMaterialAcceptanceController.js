(function() {
	"use strict";

	function returnMaterialAcceptanceController($scope, $rootScope, $http,
			$state, $cookies, CONSTANTS, GenericService, PaginationService,
			screenDataService, commonDetailService, $filter, growl,
			reportService) {
		var self = this;
		var selectItemList = [];
		var masterId;
		// Default Constructor
		var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
		function initializeController() {
			$rootScope.loginpage = true;
			self.model = {
				pagination : {
					currentPage : 1,
					totalPages : 0,
					pageSize : '10',
					search : '',
					searchData : PaginationService.searchData,
					searchDataByField : PaginationService.searchDataByField,
					setTotalPages : PaginationService.setTotalPages,
					getPagesList : PaginationService.getPagesList,
					setPage : PaginationService.setPage,
					setFirstPage : PaginationService.setFirstPage,
					setPreviousPage : PaginationService.setPreviousPage,
					setNextPage : PaginationService.setNextPage,
					setLastPage : PaginationService.setLastPage,
					sortBy : PaginationService.sortBy
				},
				fromDate : new Date(),
				toDate : new Date(),
				fromDateOptions : {
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date(),
					minDate : moment(new Date()).subtract(180, 'days').toDate()
				},
				toDateOptions : {
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date()
				},
				requestData : [],
				fromStores : [ {
					"store" : "All Stores"
				} ],
				toStores : [ {
					"store" : "All Stores"
				} ],
				statuses : [ {
					"status" : "All"
				} ],
				indentTypes : [ {
					"type" : "All"
				} ],
				materialReturnData : undefined

			};

			self.model.MaterialReturnAcceptanceSearch = MaterialReturnAcceptanceSearch;
			self.model.materialReturnAcceptanceDetails = materialReturnAcceptanceDetails;
			self.model.printMaterialReturnAcceptanceReport = printMaterialReturnAcceptanceReport;
			self.model.OpenFromDate = OpenFromDate;
		    	self.model.OpenToDate = OpenToDate;

			getDropdownsList();
			MaterialReturnAcceptanceSearch();
		}

		var getDropdownsList = function() {
			var materialreturn = self.model;
			materialreturn.fromStore = materialreturn.fromStores[0];
			materialreturn.toStore = materialreturn.toStores[0];
			materialreturn.status = materialreturn.statuses[0];
			materialreturn.indentType = materialreturn.indentTypes[0];

			var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN_ACCEPTANCE.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						materialreturn.fromStores = materialreturn.fromStores
								.concat(response.data.fromStore);
						materialreturn.toStores = materialreturn.toStores
								.concat(response.data.toStore);
						materialreturn.statuses = materialreturn.statuses
								.concat(response.data.status);
						materialreturn.indentTypes = materialreturn.indentTypes
								.concat(response.data.indentType);
						$rootScope.stopLoader();
						initialCall();
						// GetStoreIndents();
					}, function(err) {
						$rootScope.stopLoader();
					});
		}

		var OpenFromDate = function() {
			self.model.fromDateOpened = true;
			self.model.toDateOptions.minDate = self.model.fromDate;
		}

		var OpenToDate = function() {
			self.model.toDateOpened = true;
			self.model.toDateOptions.minDate = self.model.fromDate;
		}
		var initialCall = function() {
			if (!screenDataService.getScreenData()) {
				MaterialReturnAcceptanceSearch()
			} else {
				screenDataService
						.getScreenData()
						.forEach(
								function(dataobj) {
									if (dataobj.screenName == 'Material Return Acceptance') {

										var requestData = dataobj.searchrequest;
										self.model.fromStore = self.model.fromStores
												.find(function(fromStore) {
													return requestData.fromStore.id == fromStore.id;
												});
										self.model.toStore = self.model.toStores
												.find(function(toStore) {
													return requestData.toStore.id == toStore.id;
												});
										self.model.indentType = self.model.indentTypes
												.find(function(indentType) {
													return requestData.indentType.id == indentType.id;
												});
										self.model.status = self.model.statuses
												.find(function(status) {
													return requestData.status.id == status.id;
												});

									}
								});
				MaterialReturnAcceptanceSearch();
			}

		}

		var MaterialReturnAcceptanceSearch = function() {
			var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN_ACCEPTANCE.SEARCH_API;
			var data = {
				"fromStoreId" : self.model.fromStore.id ? self.model.fromStore.id
						: undefined,
				"toStoreId" : self.model.toStore.id ? self.model.toStore.id
						: undefined,
				"returnNo" : self.model.returnNo ? self.model.returnNo
						: undefined,
				"statusId" : self.model.status.id ? self.model.status.id
						: undefined,
				"indentTypeId" : self.model.indentType.id ? self.model.indentType.id
						: undefined,
				 "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
				 "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
				 "unitId":$scope.unitId,
	             "userId":$scope.userId

			};
			$rootScope.startLoader()
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						self.model.pagination.data = [];
						self.model.materialReturnData = response.data;
						if (!!response && !!response.data
								&& response.data.length > 0) {
							PaginationService.searchDataByField(
									self.model.materialReturnData,
									self.model.pagination);
						}
						$rootScope.stopLoader();
					}, function errorCallback(response) {
						$rootScope.stopLoader();
						growl.error('Error');
					});
			var screenData = {
				searchrequest : {
					"fromStore" : self.model.fromStore,
					"toStore" : self.model.toStore,
					"status" : self.model.status,
					"indentType" : self.model.indentType,
				},
				screenName : 'Material Return Acceptance',
			}
			screenDataService.setScreenData(screenData);
		}

		var materialReturnAcceptanceDetails = function(mreturn) {
			commonDetailService.setDataId(mreturn.id);
		}

		/** *Chetana* */
		var printMaterialReturnAcceptanceReport = function(mreturn) {
			var data = {
				"appName" : "HIS",
				"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
				"reportDesignFilePath" : "Get_MaterialReturnDetails.rpt",
				"reportFormat" : "PDF",
				"exportFileName" : "MaterialReturnAcceptance",
				"reportParams" : [ {
					"paramName" : "id",
					"paramType" : "Integer",
					"paramValue" : mreturn.id
				} ]
			};
			reportService.generate(REPORT_API_URI1, data);
		}
		/** ** */

		initializeController();
	}

	function config($stateProvider) {
		$stateProvider
				.state(
						"materialReturnAcceptance",
						{
							url : '/materialReturnAcceptance',
							templateUrl : 'views/PharmacyInventory/InPatient/material-return-acceptance.html',
							controller : "ReturnMaterialAcceptance.Controller",
							title : 'Material Return Acceptance',
							resolve : {

								data : function($q, $state, $timeout) {
									var deferred = $q.defer();

									$timeout(function() {
										var arr = JSON.parse(localStorage
												.getItem('permissions')).modules
												.filter(function(item) {
													return (item.id == 130);
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
							controllerAs : 'vm'
						});
	}

	angular.module("myApp").config(config).controller(
			"ReturnMaterialAcceptance.Controller",
			returnMaterialAcceptanceController)
})();