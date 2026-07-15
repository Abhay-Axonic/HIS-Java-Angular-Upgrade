(function () {
    "use strict";

    function assetIssueAcceptanceController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, screenDataService ,PaginationService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
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
                fromStoreList: [{
                    "store": "All Stores"
                }],
                toStoreList: [{
                    "store": "All Stores"
                }],
                indentTypeList: [{
                    "type": "All Types"
                }],
                statusList: [{
                    "status": "All Statuses"
                }],
                priorityList: [{
                    "priority": "All Priorities"
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

            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.GetAssetIssueAcceptance = GetAssetIssueAcceptance;
            self.model.NavigateToDetail = NavigateToDetail;
            self.model.initialCall = initialCall

            PopulateDropdown();
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

            var URI = CONSTANTS.ASSET.ASEET_ISSUE_ACCEPTANCE.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            self.model.fromStore = self.model.fromStoreList[0];
            self.model.toStore = self.model.toStoreList[0];
            self.model.indentType = self.model.indentTypeList[0];
            self.model.status = self.model.statusList[0];
            self.model.priority = self.model.priorityList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.fromStoreList = self.model.fromStoreList.concat(response.data.fromStore);
                    self.model.toStoreList = self.model.toStoreList.concat(response.data.toStore);
                    self.model.indentTypeList = self.model.indentTypeList.concat(response.data.indentType);
                    self.model.statusList = self.model.statusList.concat(response.data.status);
                    self.model.priorityList = self.model.priorityList.concat(response.data.priority);
                    initialCall();
                    $rootScope.stopLoader();
                });
        }

        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                GetAssetIssueAcceptance()
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'assetIssueAcceptance') {
                        var requestData = dataobj.searchrequest;
                        self.model.fromStore = self.model.fromStoreList.find(function (fromStore) {
                            return requestData.fromStore.id == fromStore.id;
                        });
                        self.model.toStore = self.model.toStoreList.find(function (toStore) {
                            return requestData.toStore.id == toStore.id;
                        });
                        self.model.indentType = self.model.indentTypeList.find(function (indentType) {
                            return requestData.indentType.id == indentType.id;
                        });
                        self.model.status = self.model.statusList.find(function (status) {
                            return requestData.status.id == status.id;
                        }); 
                        if(requestData.priority){
                            self.model.priority = self.model.priorityList.find(function (priority) {
                                return requestData.priority.id == priority.id;
                            });
                        }
                     
                        self.model.issueNo = requestData.issueNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetAssetIssueAcceptance();
            }
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

        var GetAssetIssueAcceptance = function () {
            if (ValidateDate()) {
                var visitNumber = '';
                if (self.model.visitType) {
                    if (self.model.visitType.id == 1) {
                        visitNumber = self.model.visitNumber.visitNo;
                    } else {
                        visitNumber = self.model.visitNumberText;
                    }
                }
                if(visitNumber == "Select Visit No"){
                    visitNumber = undefined
                }
                var URI = CONSTANTS.ASSET.ASEET_ISSUE_ACCEPTANCE.SEARCH_API;
                

                var data = {
                		 "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                         "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                         "fromStoreId": !!self.model.fromStore && !!self.model.fromStore.id ? self.model.fromStore.id : undefined,
                         "toStoreId": !!self.model.toStore && !!self.model.toStore.id ? self.model.toStore.id : undefined,
                         "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                         "indentTypeId": !!self.model.indentType && !!self.model.indentType.id ? self.model.indentType.id : undefined,
                         "priorityId": !!self.model.priority && !!self.model.priority.id ? self.model.priority.id : undefined,
                         "issueNo": !! self.model.issueNo ?  self.model.issueNo : undefined,
                         "userId":$scope.userId,
                         "unitId":$scope.unitId,
                    		
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        
                    	self.model.pagination.data = [];
                    	self.model.assetIssueAcceptanceList = response.data;
                        
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.assetIssueAcceptanceList , self.model.pagination);
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
                        "status": self.model.status,
                        "indentType": self.model.indentType,
                        "issueNo": self.model.issueNo,
                    },
                    screenName: 'assetIssueAcceptance',
                }
                screenDataService.setScreenData(screenData);

            }
        }

        var NavigateToDetail = function (item) {
            commonDetailService.setDataId(item.id);
            $state.go('detailsAssetIssueAcceptance');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("assetIssueAcceptance", {
            url: "/assetIssueAcceptance",
            templateUrl: 'views/master/UnitMaster/assetManagement/asset-issue-acceptance.html',
            controller: "AssetIssueAcceptance.Controller",
            title: 'Asset Issuance Acceptance',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 215);
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
        .controller("AssetIssueAcceptance.Controller", assetIssueAcceptanceController)
})();