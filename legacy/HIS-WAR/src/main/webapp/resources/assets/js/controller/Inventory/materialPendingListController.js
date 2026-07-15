(function () {
    "use strict";

    function materialPendingListController($scope, $rootScope, $http, $state, $cookies, CONSTANTS, GenericService, PaginationService, screenDataService, commonDetailService, $filter, growl, reportService) {
        var self = this;
        var selectItemList = [];
        var masterId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {

                requestData: [],
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
            self.model.MaterialPendingListData = MaterialPendingListData;
            self.model.materialPendingListDetails = materialPendingListDetails;
            self.model.printMaterialPendingListReport = printMaterialPendingListReport;
            self.model.activeItem = '';
            getDropdownsList();
        }

        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                MaterialPendingListData()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Material Issue Pending List') {
                        var requestData = dataobj.searchrequest;

                        self.model.fromStore = self.model.fromStores.find(function (fromStore) {
                            return requestData.fromStore.id == fromStore.id;
                        });
                        self.model.toStore = self.model.toStores.find(function (toStore) {
                            return $scope.storeId== toStore.id;
                        });
                        self.model.indentType = self.model.indentTypes.find(function (indentType) {
                            return requestData.indentType.id == indentType.id;
                        });
                        self.model.status = self.model.statuses.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        $scope.indentNo = requestData.indentNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                MaterialPendingListData();
            }
        }
        var getDropdownsList = function () {
            var materialpending = self.model;
            materialpending.fromStore = materialpending.fromStores[0];
            materialpending.toStore = materialpending.toStores[0];
            materialpending.status = materialpending.statuses[0];
            materialpending.indentType = materialpending.indentTypes[0];


            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE.MATERIAL_PENDING_ISSUE.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    materialpending.fromStores = materialpending.fromStores.concat(response.data.fromStore);
                    materialpending.toStores = materialpending.toStores.concat(response.data.toStore);
                    materialpending.statuses = materialpending.statuses.concat(response.data.status);
                    materialpending.indentTypes = materialpending.indentTypes.concat(response.data.indentType);
                    self.model.toStore = materialpending.toStores.find(function (toStore) {
                        return $scope.storeId== toStore.id;
                    });
                    initialCall();
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }
        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }
        var ValidateDate = function () {
            var isValid = true;

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }

        var MaterialPendingListData = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE.MATERIAL_PENDING_ISSUE.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                    "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                    "indentNo": $scope.indentNo,
                    "statusId": self.model.status.id ? self.model.status.id : undefined,
                    "indentTypeId": self.model.indentType.id ? self.model.indentType.id : undefined,
                    "unitId":$scope.unitId,
                     "userId":$scope.userId,
		
                };
                $rootScope.startLoader()
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        self.model.pagination.data=[];
                        self.model.searchMaterialPendingListData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.searchMaterialPendingListData, self.model.pagination);
                          }
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
                /*var screenData = {
                    searchrequest: {
                        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                        "fromStore": self.model.fromStore,
                        "toStore": self.model.toStore,
                        "indentNo": self.model.indentNo,
                        "status": self.model.status,
                        "indentType": self.model.indentType,
                    },
                    screenName: 'Material Issue Pending List',
                }
                screenDataService.setScreenData(screenData);*/
            }
        }

        var materialPendingListDetails = function (pending) {
            commonDetailService.setDataId(pending.id);
            var screenData = {
                searchrequest: {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "fromStore": self.model.fromStore,
                    "toStore": self.model.toStore,
                    "indentNo": $scope.indentNo,
                    "status": self.model.status,
                    "indentType": self.model.indentType,
                },
                screenName: 'Material Issue Pending List',
            }
            screenDataService.setScreenData(screenData);
            $state.go('detailsMaterialPending');
        }

        /***Chetana**/
        var printMaterialPendingListReport = function (item) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_material_Issue.rpt",
                "reportFormat": "PDF",
                "exportFileName": "MaterialPendingList",
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
       /*****/ 

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("materialIssue.pendingIssue", {
            url: '/pendingIssue',
            templateUrl: 'views/PharmacyInventory/InPatient/material-pending-issue.html',
            controller: "MaterialPendingList.Controller",
            title: 'Material Issue',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 127);
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
            controllerAs: 'vm'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("MaterialPendingList.Controller", materialPendingListController)
})();
