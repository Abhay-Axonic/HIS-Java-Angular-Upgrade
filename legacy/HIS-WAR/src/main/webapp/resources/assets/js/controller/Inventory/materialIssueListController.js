(function () {
    "use strict";

    function materialIssueListController($scope, $rootScope, $http, $state, GenericService,PaginationService, screenDataService, $cookies, CONSTANTS, commonDetailService, $filter, growl, reportService) {
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
                searchMaterialIssueListData: undefined

            };

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.MaterialIssueListData = MaterialIssueListData;
            self.model.materialIssueListDetails = materialIssueListDetails;
            self.model.printMaterialIssueListReport = printMaterialIssueListReport;

            self.model.activeItem = '';
            getDropdownsList();
        }
        var ValidateDate = function () {
            var isValid = true;

            if (self.model.fromDate > self.model.toDate) {
                growl.error('From Date should lesser than To Date');
                isValid = false;
            }

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
                    if (dataobj.screenName == 'Material Issue List') {
                        var requestData = dataobj.searchrequest;

                        self.model.fromStore = self.model.fromStores.find(function (fromStore) {
                            return requestData.fromStore.id == fromStore.id;
                        });
                        self.model.toStore = self.model.toStores.find(function (toStore) {
                            return $scope.storeId == toStore.id;
                        });
                        self.model.indentType = self.model.indentTypes.find(function (indentType) {
                            return requestData.indentType.id == indentType.id;
                        });
                        self.model.status = self.model.statuses.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        self.model.indentNo = requestData.indentNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                        self.model.issueNo = requestData.issueNo
                    }
                });
                MaterialIssueListData();
            }

        }

        var getDropdownsList = function () {
            var materialissue = self.model;
            materialissue.fromStore = materialissue.fromStores[0];
            materialissue.toStore = materialissue.toStores[0];
            materialissue.status = materialissue.statuses[0];
            materialissue.indentType = materialissue.indentTypes[0];

            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE.MATERIAL_ISSUE_LIST.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;


            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    materialissue.fromStores = materialissue.fromStores.concat(response.data.fromStore);
                    materialissue.toStores = materialissue.toStores.concat(response.data.toStore);
                    materialissue.statuses = materialissue.statuses.concat(response.data.status);
                    materialissue.indentTypes = materialissue.indentTypes.concat(response.data.indentType);
                    self.model.toStore = materialissue.toStores.find(function (toStore) {
                        return $scope.storeId == toStore.id;
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

        var MaterialIssueListData = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_ISSUE.MATERIAL_ISSUE_LIST.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                    "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                    "indentNo": self.model.indentNo ? self.model.indentNo : undefined,
                    "statusId": self.model.status.id ? self.model.status.id : undefined,
                    "indentTypeId": self.model.indentType.id ? self.model.indentType.id : undefined,
                    "issueNo": self.model.issueNo ? self.model.issueNo : undefined,
                    "unitId":$scope.unitId,
                    "userId":$scope.userId
	
                };
                $rootScope.startLoader()
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        self.model.pagination.data=[];
                        self.model.searchMaterialIssueListData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.searchMaterialIssueListData, self.model.pagination);
                          }
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
                        "indentNo": self.model.indentNo,
                        "status": self.model.status,
                        "indentType": self.model.indentType,
                        "issueNo": self.model.issueNo
                    },
                    screenName: 'Material Issue List',
                }
                screenDataService.setScreenData(screenData);
            }
        }
        var materialIssueListDetails = function (issue) {
            commonDetailService.setDataId(issue.id);
            $state.go('detailsIssueList');
        }
        
        /***Chetana**/
        var printMaterialIssueListReport = function (issue) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_material_Issue.rpt",
                "reportFormat": "PDF",
                "exportFileName": "MaterialInwardsApproval",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": issue.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
        
        $scope.getClassForColor = function(info)
        {
            	   if(info.forceClose == true){
             		return 'td-bg-red';
            	   }
        }
        
       /*****/ 

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("materialIssue.issueList", {
            url: '/issueList',
            templateUrl: 'views/PharmacyInventory/InPatient/material-issue-list.html',
            controller: "MaterialIssueList.Controller",
            title: 'Material Issue',
            controllerAs: 'vm'
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("MaterialIssueList.Controller", materialIssueListController)
})(); 