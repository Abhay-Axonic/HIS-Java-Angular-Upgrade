(function () {
    "use strict";

    function assetIndentController($scope, StatusService, cancelReasonService, $state, commonDetailService,screenDataService, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl, reportService,PaginationService) {
        var self = this;
        var assetId;
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
                indentTypes: [{
                    "type": "All"
                }],
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
                SelectedRequestIds: [],
                selectAllRows: false,

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

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.AssetIndentSearch = AssetIndentSearch;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.isSelected = isSelected;
            self.model.isDisable = isDisable;
            self.model.SelectAssetIndentRequest = SelectAssetIndentRequest;
            self.model.ApproveAsset = ApproveAsset;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.AddAsset = AddAsset;
            self.model.EditAsset = EditAsset;
            self.model.AssetDetails = AssetDetails;
            self.model.printAssetIndentReport = printAssetIndentReport;

           

            PopulateDropdown()
            AssetIndentSearch()
        }

        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                AssetIndentSearch()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Asset Indent') {

                        var requestData = dataobj.searchrequest;
                        self.model.fromStore = self.model.fromStores.find(function (fromStore) {
                            return requestData.fromStore.id == fromStore.id;
                        });
                        self.model.toStore = self.model.toStores.find(function (toStore) {
                            return requestData.toStore.id == toStore.id;
                        });
                        self.model.indentType = self.model.indentTypes.find(function (indentType) {
                            return requestData.indentType.id == indentType.id;
                        });
                        self.model.status = self.model.statuses.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        self.model.indentNumber = requestData.indentNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);

                    }
                });
                AssetIndentSearch();
            }

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
            var assetIndent = self.model;
            assetIndent.fromStore = assetIndent.fromStores[0];
            assetIndent.toStore = assetIndent.toStores[0];
            assetIndent.status = assetIndent.statuses[0];
            assetIndent.indentType = assetIndent.indentTypes[0];

            var URI = CONSTANTS.ASSET.ASSET_INDENT.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    assetIndent.fromStores = assetIndent.fromStores.concat(response.data.fromStore);
                    assetIndent.toStores = assetIndent.toStores.concat(response.data.toStore);
                    assetIndent.statuses = assetIndent.statuses.concat(response.data.status);
                    assetIndent.indentTypes = assetIndent.indentTypes.concat(response.data.indentType);
                    initialCall();
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        var AssetIndentSearch = function () {
            var URI = CONSTANTS.ASSET.ASSET_INDENT.SEARCH_API;
            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                "indentNo": self.model.indentNumber ? self.model.indentNumber : undefined,
                "statusId": self.model.status.id ? self.model.status.id : undefined,
                "indentTypeId": self.model.indentType ? self.model.indentType.id : undefined,
                "unitId":$scope.unitId,
            };
            $rootScope.startLoader()
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    for (var i = 0; i < response.data.length; i++) {
                        response.data[i]["isChecked"] = false;
                    }
                    self.model.pagination.data=[];
                    self.model.assetIndentData = response.data;
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.assetIndentData, self.model.pagination);
                    }
                    self.model.selectAllRows = false;
                    self.model.isItemChecked = false;   
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
            
                var screenData = {
                    searchrequest: {
                      "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                      "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                      "fromStore": self.model.fromStore,
                      "toStore": self.model.toStore,
                      "indentNo": self.model.indentNumber,
                      "status": self.model.status,
                      "indentType": self.model.indentType,
                    },
                    screenName: 'Asset Indent',
                  }
                  screenDataService.setScreenData(screenData);
                
        }

        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.assetIndentData.forEach(function (indent) {
                    if (!isDisable(indent)) {
                        indent.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(indent.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.assetIndentData.forEach(function (indent) {
                    indent.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (indent) {
            if (!isDisable(indent))
                return indent.isSelected;
            else
                return true;
        }
        var isDisable = function (indent) {
            if (indent.statusId != 1) {
                return true;
            }
            return false;
        }

        var SelectAssetIndentRequest = function (indent) {
            var indentIds = self.model.SelectedRequestIds;

            if (indent.isSelected) {
                indentIds.push(indent.id);
            } else {
                var index = indentIds.indexOf(indent.id);
                if (index > -1) {
                    indentIds.splice(index, 1);
                }
            }

            if (self.model.assetIndentData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.assetIndentData.forEach(function (indent) {
                if (indent.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }


        var ApproveAsset = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.ASSET.ASSET_INDENT.APPROVE_API;

                var data = {
                    "indentId": self.model.SelectedRequestIds,
                    "statusId": setStatus('Pending'),
                    "approvalStatusId": setStatus('Pending Approval'),
                    "userId":$scope.userId,
                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        AssetIndentSearch();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.ASSET.ASSET_INDENT.CANCEL_API + assetId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        "userId":$scope.userId
                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedRequestIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            ModalDismiss('confirmCancel');
                            AssetIndentSearch();
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.msg);
                        });
                }
            }

        }
        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }
        var ValidateCancelation = function () {
            var isValid = true;
            if (!self.model.cancelReason) {
                isValid = false;
                growl.error('Please Select Reason to Cancel');
            }
            return isValid;
        }

        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }

        var ValidateAndUpdate = function (action, popupName, id) {
            assetId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
            self.model.cancelReason = '';
            self.model.cancelNote = '';
        }

        var AddAsset = function () {
            commonDetailService.setDataId(null);
            $state.go('addNewAssetIndent');
        }

        var EditAsset = function (asset) {
            commonDetailService.setDataId(asset.id);
            $state.go('addNewAssetIndent');
        }

        var AssetDetails = function (asset) {
            commonDetailService.setDataId(asset.id);
            $state.go('detailsAssetIndent');
        }
        
        /***Chetana**/
        var printAssetIndentReport = function (asset) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_Asset_indent.rpt",
                "reportFormat": "PDF",
                "exportFileName": "AssetIndent",
                "reportParams": [
                    {
                        "paramName": "id",
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
        $stateProvider.state("assetIndent", {
            url: "/assetIndent",
            templateUrl: 'views/master/UnitMaster/assetManagement/asset-indent.html',
            controller: "AssetIndent.Controller",
            title: 'Asset Indent',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 205);
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
        .controller("AssetIndent.Controller", assetIndentController);
})(); 