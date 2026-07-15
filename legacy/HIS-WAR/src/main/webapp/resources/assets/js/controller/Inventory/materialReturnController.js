(function () {
    "use strict";

    function materialReturnController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService,screenDataService, StatusService, cancelReasonService, commonDetailService, $state, growl, reportService) {
        var self = this;
        var returnId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {
                pagination: {
                    currentPage: 1,
                    totalPages: 0,
                    pageSize: '10',
                    search: '',
                    searchData: PaginationService.searchData,
                    searchDataByField:PaginationService.searchDataByField,
                    setTotalPages: PaginationService.setTotalPages,
                    getPagesList: PaginationService.getPagesList,
                    setPage: PaginationService.setPage,
                    setFirstPage: PaginationService.setFirstPage,
                    setPreviousPage: PaginationService.setPreviousPage,
                    setNextPage: PaginationService.setNextPage,
                    setLastPage: PaginationService.setLastPage,
                    sortBy: PaginationService.sortBy
                  },
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
                SelectedReturnIds: [],
                fromDate: new Date(),
                toDate: new Date(),
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate : new Date(),
                    minDate : moment(new Date()).subtract(180, 'days').toDate()
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate : new Date()
                },
            };
            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
            });

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });
            self.model.searchMaterialReturn = searchMaterialReturn;
            self.model.SelectAllReturns = SelectAllReturns;
            self.model.SelectReturnRequest = SelectReturnRequest;
            self.model.isSelected = isSelected;
            self.model.isDisable = isDisable;
            self.model.MaterialReturnDetails = MaterialReturnDetails;
            self.model.EditMaterialReturn = EditMaterialReturn;
            self.model.ApproveMaterialReturn = ApproveMaterialReturn;
            self.model.ValidateAndUpdate = ValidateAndUpdate;
            self.model.setStatus = setStatus;
            self.model.AddNewMaterialReturn = AddNewMaterialReturn;
            self.model.printMaterialReturnReport = printMaterialReturnReport;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;

            getDropdownsList();
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
            var mreturn = self.model;
            mreturn.fromStore = mreturn.fromStores[0];
            mreturn.toStore = mreturn.toStores[0];
            mreturn.status = mreturn.statuses[0];

            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    mreturn.fromStores = mreturn.fromStores.concat(response.data.fromStore);
                    mreturn.toStores = mreturn.toStores.concat(response.data.toStore);
                    mreturn.statuses = mreturn.statuses.concat(response.data.status);
                    $rootScope.stopLoader();
                    initialCall();
                    // GetStoreIndents();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                searchMaterialReturn()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Material Return') {

                        var requestData = dataobj.searchrequest;
                        self.model.fromStore = self.model.fromStores.find(function (fromStore) {
                            return requestData.fromStore.id == fromStore.id;
                        });
                        self.model.toStore = self.model.toStores.find(function (toStore) {
                            return requestData.toStore.id == toStore.id;
                        });
                        self.model.status = self.model.statuses.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                    }
                });
                searchMaterialReturn();
            }

        }

        var searchMaterialReturn = function () {
            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.SEARCH_API;
            var data = {
                "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                "statusId": self.model.status.id ? self.model.status.id : undefined,
                "returnNo": $scope.returnNo,
                "unitId":$scope.unitId,
                "userId":$scope.userId,
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            };
            $rootScope.startLoader()
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    for (var i = 0; i < response.data.length; i++) {
                        response.data[i]["isChecked"] = false;
                    }
                    self.model.pagination.data=[];
                    self.model.searchMaterialReturnData = response.data;
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchDataByField(self.model.searchMaterialReturnData, self.model.pagination);
                      }
                    self.model.selectAllRows = false;
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
            var screenData = {
                searchrequest: {
                    "fromStore": self.model.fromStore,
                    "toStore": self.model.toStore,
                    "status": self.model.status,
                },
                screenName: 'Material Return',
            }
            screenDataService.setScreenData(screenData);
        }

        var SelectAllReturns = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedReturnIds = [];
                self.model.searchMaterialReturnData.forEach(function (mreturn) {
                    if (!isDisable(mreturn)) {
                        mreturn.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedReturnIds.push(mreturn.id);
                    }
                });
            } else {
                self.model.SelectedReturnIds = [];
                self.model.searchMaterialReturnData.forEach(function (mreturn) {
                    mreturn.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var SelectReturnRequest = function (mreturn) {
            var returnIds = self.model.SelectedReturnIds;

            if (mreturn.isSelected) {
                returnIds.push(mreturn.id);
            } else {
                var index = returnIds.indexOf(mreturn.id);
                if (index > -1) {
                    returnIds.splice(index, 1);
                }
            }

            if (self.model.searchMaterialReturnData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.searchMaterialReturnData.forEach(function (mreturn) {
                if (mreturn.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }

        var isSelected = function (mreturn) {
            if (!isDisable(mreturn))
                return mreturn.isSelected;
            else
                return true;
        }

        var isDisable = function (mreturn) {
            if (mreturn.status != 'New') {
                return true;
            }
            return false;
        }
        var EditMaterialReturn = function (mreturn) {
            commonDetailService.setDataId(mreturn.id);
            $state.go('addNewMaterialReturn');
        }

        var MaterialReturnDetails = function (mreturn) {
            commonDetailService.setDataId(mreturn.id);
            $state.go('detailsMaterialReturn');
        }

        var AddNewMaterialReturn = function () {
            commonDetailService.setDataId(null);
            $state.go('addNewMaterialReturn');
        }

        
//     
        $scope.saveBtnDisabled=false;
        var ApproveMaterialReturn = function () {
            $scope.saveBtnDisabled=true;
            var pending = 0;
            var approved = 0;
            var reject = 0;
            var partial = 0;
            if (self.model.action == 'Update') {
               var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.APPROVE_API;
               var data = {
                   "returnId": self.model.SelectedReturnIds,
                   "statusId": setStatus('Pending'),
                   "approvalStatusId": setStatus('Pending Approval'),
                   "userId":$scope.userId 
               };
               $rootScope.startLoader();
               GenericService.serviceAction("PATCH", URI, data).then(
                   function (response) {
                       self.model.SelectedReturnIds = [];
                       $rootScope.stopLoader();
                       growl.success(response.data.message);
                       searchMaterialReturn();
                       self.model.selectAllRows = false;
                       $scope.saveBtnDisabled=false;
                   },
                   function (err) {
                       $rootScope.stopLoader();
                       growl.error(err.data.msg);
                   });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                    var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_RETURN.CANCEL_API + returnId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": setStatus('Cancelled'),
                        "approvalStatusId": setStatus('Rejected'),
                        "userId":$scope.userId
                    };
                    $rootScope.startLoader();
                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            self.model.SelectedReturnIds = [];
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            ModalDismiss('confirmCancel');
                            searchMaterialReturn();
                            self.model.cancelReason = '';
                            self.model.cancelNote = '';
                            $scope.saveBtnDisabled=false;
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.msg);
                        });
                }
            }
            self.model.isItemChecked = false;
            angular.element('#confirmLogout1').modal('hide');
        }
        var ValidateCancelation = function () {
            var isValid = true;
            if (!self.model.cancelReason) {
                isValid = false;
                growl.error('Please Select Reason to Cancel');
            }
            if(!isValid)
            {
            $scope.saveBtnDisabled=false;
            }
            return isValid;
        }

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
        }

        var ValidateAndUpdate = function (action, popupName, id) {
            self.model.cancelReason = undefined;
            returnId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }
        
        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }
        
        
        /***Chetana**/
        var printMaterialReturnReport = function (mreturn) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "Get_MaterialReturnDetails.rpt",
                "reportFormat": "PDF",
                "exportFileName": "MaterialReturn",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": mreturn.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
       /*****/ 
        
        initializeController();
    }


    function config($stateProvider) {
        $stateProvider.state("materialReturn", {
            url: "/materialReturn",
            templateUrl: 'views/PharmacyInventory/InPatient/material-return.html',
            controller: "MaterialReturn.Controller",
            title: 'Material Return',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 129);
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
        .controller("MaterialReturn.Controller", materialReturnController);
})() 