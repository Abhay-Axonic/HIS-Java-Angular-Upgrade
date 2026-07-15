(function () {
    "use strict";

    function addFixedAssetMasterController($scope, $rootScope, $http, $filter, $state, GenericService, commonDetailService, cancelReasonService, rejectReasonService, StatusService, growl, CONSTANTS,$cookies , reportService ,PaginationService) {
        var self = this;

        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                fromDate: new Date(),
                toDate: new Date(),
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date(),
                    minDate : moment(new Date()).subtract(180, 'days').toDate()
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                selectAllRows: false,
                SelectedRequestIds: [],
                storeList: [{
                    "store": "All"
                }],
                statuses: [{
                    "status": "All"
                }],
                
                
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

            rejectReasonService.GetReason().then(function (reason) {
                self.model.rejectReasons = reason;
            });

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });

            StatusService.GetStatus().then(function (status) {
                self.model.statusList = status;
            });

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.FixedAssetSearch = FixedAssetSearch;
            self.model.NavigateToAddNew = NavigateToAddNew;
            self.model.NavigateToDetails = NavigateToDetails;
            self.model.NavigateToEdit = NavigateToEdit;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectFixedAssetRequest = SelectFixedAssetRequest;
            self.model.isDisable = isDisable;
            self.model.UpdateFixedAssetRequest = UpdateFixedAssetRequest;
            self.model.OpenPopup = OpenPopup;
            self.model.ModalDismiss = ModalDismiss;
            self.model.isEdit = isEdit;
            self.model.isCancel = isCancel;
            self.model.printAddfixedAssetMasterReport = printAddfixedAssetMasterReport;
            PopulateDropdown();
        }

        $scope.searchKeyword = "";
        var setStatus = function (x) {
            var status = self.model.statusList.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var PopulateDropdown = function () {
            self.model.store = self.model.storeList[0];
            self.model.status = self.model.statuses[0];

            var URI = CONSTANTS.ASSET.FIXED_ASSET.ADD_FIXED_ASSET.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.storeList = self.model.storeList.concat(response.data.store);
                    self.model.statuses = self.model.statuses.concat(response.data.status);
                    $rootScope.stopLoader();
                    FixedAssetSearch();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var ValidateDate = function () {
            var isValid = true;
            if (self.model.fromDate > self.model.toDate) {
                growl.error('From Date should lesser than To Date');
                isValid = false;
            }

            return isValid;
        }

        var FixedAssetSearch = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.ASSET.FIXED_ASSET.ADD_FIXED_ASSET.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "storeId": self.model.store.id,
                    "statusId": self.model.status.id,
                    "unitId":$scope.unitId,
                    "userId":$scope.userId,
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                    	self.model.pagination.data = [];
                        self.model.fixedAssetData = response.data;
                        
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.fixedAssetData , self.model.pagination);
                          }
                        
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                    }
                );
            }
        }

        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.fixedAssetData.forEach(function (fixedAsset) {
                    if (!isDisable(fixedAsset)) {
                        fixedAsset.isSelected = true;
                        self.model.SelectedRequestIds.push(fixedAsset.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.fixedAssetData.forEach(function (fixedAsset) {
                    fixedAsset.isSelected = false;
                });
            }
        }

        var isDisable = function (fixedAsset) {
            if (fixedAsset.status != 'New') {
                return true;
            }
            return false;
        }

        var isEdit = function (fixedAsset) {
            if (fixedAsset.status == 'Pending') {
                return true;
            } else if (fixedAsset.status == 'New') {
                return false;
            } else if (fixedAsset.status == 'Closed' || fixedAsset.status == 'Cancelled') {
                return true;
            }
        }

        var isCancel = function (fixedAsset) {
            if (fixedAsset.status == 'Pending') {
                return false;
            } else if (fixedAsset.status == 'New') {
                return false;
            } else if (fixedAsset.status == 'Closed' || fixedAsset.status == 'Cancelled') {
                return true;
            }
        }

        var isSelected = function (fixedAsset) {
            if (!isDisable(fixedAsset))
                return fixedAsset.isSelected;
            else
                return true;
        }

        var SelectFixedAssetRequest = function (fixedAsset) {
            var fixedAssetIds = self.model.SelectedRequestIds;

            if (fixedAsset.isSelected) {
                fixedAssetIds.push(fixedAsset.id);
            } else {
                var index = fixedAssetIds.indexOf(fixedAsset.id);
                if (index > -1) {
                    fixedAssetIds.splice(index, 1);
                }
            }

            if (self.model.fixedAssetData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
        }

        var UpdateFixedAssetRequest = function () { 
            if (self.model.action == 'Update') {
                if (ValidateStatus()) {
                    var URI = CONSTANTS.ASSET.FIXED_ASSET.ADD_FIXED_ASSET.APPROVE_API;

                    var data = {
                        "openingBalanceId": self.model.SelectedRequestIds,
                        "statusId": setStatus('Closed'),
                        "userId":$scope.userId,
                        "unitId":$scope.unitId,
                        "organizationId":$scope.organizationId
                        
                    };

                    $rootScope.startLoader();
                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            FixedAssetSearch();
                            self.model.selectAllRows = false;

                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.message);
                        });
                } else {
                    growl.error('Select fixed asset request to approve');
                    return;
                }
            } else if (self.model.action == 'Cancel') {
                if (ValidetReason()) {
                    var URI = CONSTANTS.ASSET.FIXED_ASSET.ADD_FIXED_ASSET.CANCEL_API + self.model.id;

                    var data = {
                        "statusId": setStatus('Closed'),
                        "cancelReasonId": self.model.cancelReason ? self.model.cancelReason : undefined,
                        "cancelNote": self.model.rejectNote,
                        "userId":$scope.userId
                    }

                    $rootScope.startLoader();
                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            ModalDismiss('cancelFixedAssetReqest');
                            self.model.rejectNote = undefined;
                            self.model.cancelReason = undefined;
                            // 
                            FixedAssetSearch();
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.message);
                        });
                }
            }
        }

        var ValidateStatus = function () {
            var isValid = true;

            if (!self.model.SelectedRequestIds || self.model.SelectedRequestIds.length == 0) {
                isValid = false;
            }

            return isValid;
        }

        var ValidetReason = function () {
            var isValid = true;

            if (!self.model.cancelReason) {
                self.model.isReasonValid = true;
                isValid = false;
            }

            return isValid;
        }

        var OpenPopup = function (action, popupName, data) {
            self.model.action = action;
            self.model.id = data.id;
            var popup = angular.element('#' + popupName);

            self.model.isReasonValid = false;
            self.model.rejectNote = undefined;
            popup.modal('show');
        }

        var ModalDismiss = function (modal) {
            var popup = angular.element('#' + modal);
            popup.modal('hide');
        }

        var NavigateToAddNew = function () {
            $state.go('addNewfixedAssetMaster');
        }

        var NavigateToDetails = function (fixedAsset) {
            commonDetailService.setDataId(fixedAsset.id);
            $state.go('detailsFixedAssetMaster');
        }

        var NavigateToEdit = function (fixedAsset) {
            commonDetailService.setDataId(fixedAsset.id);
            $state.go('addNewfixedAssetMaster');
        }

        /***Chetana**/
        var printAddfixedAssetMasterReport = function (asset) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_asset_OpBalance.rpt",
                "reportFormat": "PDF",
                "exportFileName": "Asset",
                "reportParams": [
                    {
                        "paramName": "asset_id",
                        "paramType": "Integer",
                        "paramValue": asset.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
       /*****/ 
        
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("addfixedAssetMaster", {
            url: "/addfixedAssetMaster",
            templateUrl: 'views/master/UnitMaster/assetManagement/add-fixed-asset-master.html',
            controller: "AddfixedAssetMaster.Controller",
            title: 'Add Fixed Asset Opening Balance',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 220);
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
        .controller("AddfixedAssetMaster.Controller", addFixedAssetMasterController);
})();