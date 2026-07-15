(function () {
    "use strict";

    function materialIssueAcceptanceController($scope, $rootScope, $http, $state, GenericService,PaginationService, $cookies, CONSTANTS, screenDataService, commonDetailService, $filter, growl, reportService) {
        var self = this;
        var selectItemList = [];
        var masterId;
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
                requestData: [],
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
                searchMaterialIssueListData: undefined

            };

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.MaterialIssueListData = MaterialIssueListData;
            self.model.materialIssueListDetails = materialIssueListDetails;
            self.model.printMaterialIssueAcceptanceReport = printMaterialIssueAcceptanceReport;
            getDropdownsList();
        }

        var getDropdownsList = function () {
            var materialissue = self.model;
            materialissue.fromStore = materialissue.fromStores[0];
            materialissue.toStore = materialissue.toStores[0];
            materialissue.status = materialissue.statuses[0];
            materialissue.indentType = materialissue.indentTypes[0];

            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE_ACCEPTANCE.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    materialissue.fromStores = materialissue.fromStores.concat(response.data.toStore);
                    materialissue.toStores = materialissue.toStores.concat(response.data.fromStore);
                    materialissue.statuses = materialissue.statuses.concat(response.data.status);
                    materialissue.indentTypes = materialissue.indentTypes.concat(response.data.indentType);
                    $rootScope.stopLoader();
                    initialCall();
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

        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                MaterialIssueListData()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Material Issue Acceptance') {

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
                MaterialIssueListData();
            }

        }

        var MaterialIssueListData = function () {
            if (ValidateDate()) {
            	
            	var fromStoreIds = [];
            	
            	if(self.model.fromStore.id != null && self.model.fromStore.id != undefined){
            		
            		fromStoreIds.push(self.model.fromStore.id);
            		
            	}else{
            		
            		angular.forEach(self.model.fromStores,function(store){
                		if(store.id != null){
                			fromStoreIds.push(store.id);
                		}
                		
    				});
            	}
            	
            	
                var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE_ACCEPTANCE.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                    "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                    "issueNo": $scope.issueNo,
                    "statusId": self.model.status.id ? self.model.status.id : undefined,
                    "indentTypeId": self.model.indentType.id ? self.model.indentType.id : undefined,
                    "unitId":$scope.unitId,
                    "userId":$scope.userId,
                    "fromStoreIds" : fromStoreIds
                };
                $rootScope.startLoader()
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {

                        for (var i = 0; i < response.data.length; i++) {
                            response.data[i]["isChecked"] = false;
                        }
                        self.model.pagination.data=[];
                        self.model.searchMaterialIssueListData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchDataByField(self.model.searchMaterialIssueListData, self.model.pagination);
                          }
                        $rootScope.stopLoader();
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
                    screenName: 'Material Issue Acceptance',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var materialIssueListDetails = function (issue) {
            commonDetailService.setDataId(issue.id);
        }

        /***Chetana**/
        var printMaterialIssueAcceptanceReport = function (item) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
//                "reportDesignFilePath": "get_material_Issue.rpt",
                "reportDesignFilePath": "get_material_Issue_Acceptance.rpt",
                "reportFormat": "PDF",
                "exportFileName": "MaterialIssueAcceptance",
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
        $stateProvider.state("materialIssueAcceptance", {
            url: '/materialIssueAcceptance',
            templateUrl: 'views/PharmacyInventory/InPatient/material-issue-acceptance.html',
            controller: "MaterialIssueAcceptance.Controller",
            title: 'Material Issuance Acceptance',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 128);
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
        .controller("MaterialIssueAcceptance.Controller", materialIssueAcceptanceController)
})();  