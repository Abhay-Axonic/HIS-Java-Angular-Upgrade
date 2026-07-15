(function() {
	"use strict";

	function assetIndentApprovalController($scope, $rootScope, $http,
			commonDetailService, $state, $cookies, CONSTANTS, GenericService, growl,
			reportService, cancelReasonService,PaginationService) {
		var self = this;

		// Default Constructor
		var cookieObject = $cookies.getObject('cookieObject'); 
		if(cookieObject == undefined){
			$state.go('login');
			return;
			}
		$scope.unitId = cookieObject.unitId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		$scope.storeId = cookieObject.storeId;
		
		function initializeController() {
			$rootScope.loginpage = true;
			self.model = {
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
				fromDate : new Date(),
				toDate : new Date(),
				SelectedRequestIds : [],
				selectAllRows : false,
				
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
			cancelReasonService.GetReason().then(function(reason) {
				self.model.reasons = reason;
			});
			self.model.OpenFromDate = OpenFromDate;
			self.model.OpenToDate = OpenToDate;
			self.model.AssetIndentApprovalDetails = AssetIndentApprovalDetails;
			self.model.AssetIndentApprovalSearch = AssetIndentApprovalSearch;
			self.model.printAssetIndentApprovalReport = printAssetIndentApprovalReport;
			self.model.cancelIndentApproval = self.model.cancelIndentApproval;
			self.model.OpenPopup = OpenPopup;

			PopulateDropdown();
			AssetIndentApprovalSearch();
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
			var assetIndent = self.model;
			assetIndent.fromStore = assetIndent.fromStores[0];
			assetIndent.toStore = assetIndent.toStores[0];
			assetIndent.status = assetIndent.statuses[0];
			assetIndent.indentType = assetIndent.indentTypes[0];

			var URI = CONSTANTS.ASSET.ASSET_INDENT_APPROVAL.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

			$rootScope.startLoader();
			GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
						assetIndent.fromStores = assetIndent.fromStores
								.concat(response.data.fromStore);
						assetIndent.toStores = assetIndent.toStores
								.concat(response.data.toStore);
						assetIndent.statuses = assetIndent.statuses
								.concat(response.data.status);
						assetIndent.indentTypes = assetIndent.indentTypes
								.concat(response.data.indentType);
						$rootScope.stopLoader();
						// GetStoreassetIndent();
					}, function(err) {
						$rootScope.stopLoader();
					});
		}

		var AssetIndentApprovalSearch = function() {
			var URI = CONSTANTS.ASSET.ASSET_INDENT_APPROVAL.SEARCH_API;
			var data = {
				"fromDate" : moment(self.model.fromDate).format('YYYY-MM-DD'),
				"toDate" : moment(self.model.toDate).format('YYYY-MM-DD'),
				"fromStoreId" : self.model.fromStore.id ? self.model.fromStore.id
						: undefined,
				"toStoreId" : self.model.toStore.id ? self.model.toStore.id
						: undefined,
				"indentNo" : self.model.indentNumber ? self.model.indentNumber
						: undefined,
				"statusId" : self.model.status.id ? self.model.status.id
						: undefined,
				"approvalStatus" : self.model.approxvalStatus ? self.model.approvalStatus
						: undefined,
				"indentTypeId" : self.model.indentType ? self.model.indentType.id
						: undefined,
				"approvalStatusId" : self.model.approvalStatusId ? self.model.approvalStatusId
						: undefined,
						"unitId":$scope.unitId,
			};
			$rootScope.startLoader()
			GenericService.serviceAction("POST", URI, data).then(
					function(response) {
						$rootScope.stopLoader();
						for (var i = 0; i < response.data.length; i++) {
							response.data[i]["isChecked"] = false;
						}
						 self.model.pagination.data=[];
						self.model.assetIndentApprovalData = response.data;
						if(!!response && !!response.data && response.data.length > 0){            
	                        PaginationService.searchData(self.model.assetIndentApprovalData, self.model.pagination);
	                    }
					}, function errorCallback(response) {
						$rootScope.stopLoader();
						growl.error('Error');
					});
		}
		var AssetIndentApprovalDetails = function(asset) {
			commonDetailService.setDataId(asset.id);
			$state.go('detailsAssetIndentApproval');
		}

		/** *Chetana* */
		var printAssetIndentApprovalReport = function(asset) {
			var data = {
				"appName" : "HIS",
				"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
				"reportDesignFilePath" : "get_Asset_indent.rpt",
				"reportFormat" : "PDF",
				"exportFileName" : "AssetIndentApproval",
				"reportParams" : [ {
					"paramName" : "id",
					"paramType" : "Integer",
					"paramValue" : asset.id
				} ]
			};
			reportService.generate(REPORT_API_URI1, data);
		}
		/** ** */
		$scope.id = 0;
		var OpenPopup = function(action, popupName, data) {
			$scope.id = data.id;
			self.model.action = action;
			var popup = angular.element('#' + popupName);
			self.model.id = data.id;
			popup.modal('show');
		}
		$scope.ModalDismiss = function(modal) {
			var popup = angular.element('#' + modal);
			popup.modal('hide');
			self.model.rejectNote = undefined;
			self.model.cancelReason = undefined;
		}

		$scope.validate = function () {
			var isValid = true;
			if(!self.model.cancelReason){
				growl.error("Please select reason");
				isValid = false;
			}
			return isValid;
		}

		/** *Sharad* */
		$scope.cancelIndentApproval1 = function() {
			if($scope.validate()){
				var URI = CONSTANTS.ASSET.ASSET_INDENT_APPROVAL.CANCEL_API + "/" + $scope.id;
				var data = {
					"statusId" : 9,
					"cancelReasonId" : self.model.cancelReason ? self.model.cancelReason
							: undefined,
					"cancelNote" : self.model.rejectNote,
					"unitId":$scope.unitId,
					"userId":$scope.userId,
					"organizationId":$scope.organizationId,
				};
				$rootScope.stopLoaderPopup();
				GenericService.serviceAction("PATCH", URI, data).then(
				function(response) {
					$rootScope.stopLoaderPopup();
					$scope.ModalDismiss('cancel');
					AssetIndentApprovalSearch();
					growl.success("Cancel Successfully");
				}, function errorCallback(response) {
				
					$rootScope.stopLoaderPopup();
					growl.error('Error');
				});
			}
		}

		initializeController();
	}

	function config($stateProvider) {
		$stateProvider
				.state(
						"assetIndentApproval",
						{
							url : "/assetIndentApproval",
							templateUrl : 'views/master/UnitMaster/assetManagement/asset-indent-approval.html',
							controller : "AssetIndentApproval.Controller",
							title : 'Asset Indent Approval',
							resolve : {

								data : function($q, $state, $timeout) {
									var deferred = $q.defer();

									$timeout(function() {
										var arr = JSON.parse(localStorage
												.getItem('permissions')).modules
												.filter(function(item) {
													return (item.id == 206);
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
			"AssetIndentApproval.Controller", assetIndentApprovalController);
})();
