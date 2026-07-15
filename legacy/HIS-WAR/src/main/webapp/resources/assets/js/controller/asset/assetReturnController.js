(function () {
    "use strict";

    function assetReturnController($scope, $rootScope, $http, $cookies, CONSTANTS, commonDetailService,StatusService,cancelReasonService, $state, GenericService, growl,PaginationService,reportService) {
        var self = this;
        var returnId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                fromStores: [{
                    "store": "All Stores"
                }],
                toStores: [{
                    "store": "All Stores"
                }],
                statuses: [{
                    "status": "All"
                }],
                selectAllRows: false,
                SelectedRequestIds: [],
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
                fromDate: new Date(),
                toDate: new Date(),
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
            
      StatusService.GetStatus().then(function (status) {
        self.model.statuselect = status;
      });

      cancelReasonService.GetReason().then(function (reason) {
        self.model.reasons = reason;
      });

            getDropdownsList();
            AssetReturnSearch();
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectAssetReturn = SelectAssetReturn;
            self.model.ApproveAssetReturn = ApproveAssetReturn;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.AddAssetReturnDetails = AddAssetReturnDetails;
            self.model.EditAssetReturnDetails = EditAssetReturnDetails;
            self.model.AssetReturnDetails = AssetReturnDetails;
            self.model.AssetReturnSearch = AssetReturnSearch;
            self.model.isDisable =isDisable;    
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
        }

        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var getDropdownsList = function () {
            var assetReturn = self.model;
            assetReturn.fromStore = assetReturn.fromStores[0];
            assetReturn.toStore = assetReturn.toStores[0];
            assetReturn.status = assetReturn.statuses[0];

            var URI = CONSTANTS.ASSET.ASSET_RETURN.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    assetReturn.fromStores = assetReturn.fromStores.concat(response.data.fromStore);
                    assetReturn.toStores = assetReturn.toStores.concat(response.data.toStore);
                    assetReturn.statuses = assetReturn.statuses.concat(response.data.status);
                    // initialCall();
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }


        var AssetReturnSearch = function () {
        	
            var URI = CONSTANTS.ASSET.ASSET_RETURN.SEARCH_API;
            var data = {
                "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                "returnNo": self.model.returnNo ? self.model.returnNo : undefined,
                "statusId": self.model.status.id ? self.model.status.id : undefined,
                "unitId":$scope.unitId,
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD')
            };
            $rootScope.startLoader()
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    for (var i = 0; i < response.data.length; i++) {
                        response.data[i]["isChecked"] = false;
                    }
                    
                    
                 	self.model.pagination.data = [];
                 	self.model.assetReturnData = response.data;
                    
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.assetReturnData , self.model.pagination);
                      }
                    
                    self.model.selectAllRows = false;
                    self.model.isItemChecked = false;

                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }


        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.assetReturnData.forEach(function (asset) {
                    if (!isDisable(asset)) {
                        asset.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(asset.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.assetReturnData.forEach(function (asset) {
                    asset.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (asset) {
            if (!isDisable(asset))
                return asset.isSelected;
            else
                return true;
        }

        var isDisable = function (asset) {
            if (asset.status != 'New') {
                return true;
            }
            return false;
        }

        var SelectAssetReturn = function (asset) {
            var assetIds = self.model.SelectedRequestIds;

            if (asset.isSelected) {
                assetIds.push(asset.id);
            } else {
                var index = assetIds.indexOf(asset.id);
                if (index > -1) {
                    assetIds.splice(index, 1);
                }
            }

            if (self.model.assetReturnData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.assetReturnData.forEach(function (asset) {
                if (asset.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var ApproveAssetReturn = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.ASSET.ASSET_RETURN.APPROVE_API;

                var data = {
                    "returnId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Pending'),
                    "approvalStatusId": setStatus('Pending Approval'),
                    "userId":$scope.userId,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId
                    
                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        self.model.selectAllRows = false;
                        self.model.isItemChecked = false;
                        AssetReturnSearch();

                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.ASSET.ASSET_RETURN.CANCEL_API + returnId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        "userId":$scope.userId,
                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            ModalDismiss('confirmCancel');
                            AssetReturnSearch();
                            self.model.cancelReason = '';
                            self.model.cancelNote = '';
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.msg);
                        });
                }
            }

        }
        var ValidateCancelation = function () {
            var isValid = true;
            if (!self.model.cancelReason) {
                isValid = false;
                growl.error('Please Select Reason to Cancel');
            }
            return isValid;
        }

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var ValidateAndUpdate = function (action, popupName, id) {
            returnId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }


        var AddAssetReturnDetails = function () {
            commonDetailService.setDataId(null);
            $state.go('addNewAssetReturn');
        }

        var EditAssetReturnDetails = function (areturn) {
            commonDetailService.setDataId(areturn.id);
            $state.go('addNewAssetReturn');
        }

        var AssetReturnDetails = function (areturn) {
            commonDetailService.setDataId(areturn.id);
            $state.go('detailsAssetReturn');
        }
 
        $scope.assetReturn = function (item) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "rptAssetReturnAcceptance.rpt",
                "reportFormat": "PDF",
                "exportFileName": "AssetReturn",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": item.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
      
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("assetReturn", {
            url: '/assetReturn',
            templateUrl: 'views/master/UnitMaster/assetManagement/asset-return.html',
            controller: "AssetReturn.Controller",
            title: 'Asset Return',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 216);
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
        .controller("AssetReturn.Controller", assetReturnController);
})();
    