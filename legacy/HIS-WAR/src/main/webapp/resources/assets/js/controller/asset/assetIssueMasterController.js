(function () {
    "use strict";

    function assetIssueMasterController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, screenDataService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject');
        if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                fromDate: new Date(),
                toDate: new Date(),
                SetFirstTabLevel: 1,
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                fromStoreList: [{
                    "store": "All"
                }],
                toStoreList: [{
                    "store": "All"
                }],
                indentTypeList: [{
                    "type": "All"
                }],
                statusList: [{
                    "status": "All"
                }]
            };


            //Methods
            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.GetPendingIssue = GetPendingIssue;
            self.model.GetIssueList = GetIssueList;
            self.model.NavigateToDetail = NavigateToDetail;

            PopulateDropdown();
        }

        var PopulateDropdown = function () {
            if (self.model.SetFirstTabLevel == 2) {
                PopulateIssueListDropdown();
            } else {
                PopulatePendingIssueDropdown();
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

        var PopulatePendingIssueDropdown = function () {

            var URI = CONSTANTS.ASSET.ASEET_ISSUE.DROPDOWN_API;

            self.model.fromStore = self.model.fromStoreList[0];
            self.model.toStore = self.model.toStoreList[0];
            self.model.indentType = self.model.indentTypeList[0];
            self.model.status = self.model.statusList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.fromStoreList = self.model.fromStoreList.concat(response.data.fromStore);
                    self.model.toStoreList = self.model.toStoreList.concat(response.data.toStore);
                    self.model.indentTypeList = self.model.indentTypeList.concat(response.data.indentType);
                    self.model.statusList = self.model.statusList.concat(response.data.status);
                    initialCallForPendingIssue();
                    $rootScope.stopLoader();
                });
        }

        var PopulateIssueListDropdown = function () {

            var URI = CONSTANTS.ASSET.ASEET_ISSUE.ISSUELIST_DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            self.model.fromStore = self.model.fromStoreList[0];
            self.model.toStore = self.model.toStoreList[0];
            self.model.indentType = self.model.indentTypeList[0];
            self.model.status = self.model.statusList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.fromStoreList = self.model.fromStoreList.concat(response.data.fromStore);
                    self.model.toStoreList = self.model.toStoreList.concat(response.data.toStore);
                    self.model.indentTypeList = self.model.indentTypeList.concat(response.data.indentType);
                    self.model.statusList = self.model.statusList.concat(response.data.status);
                    initialCallForIssueList();
                    $rootScope.stopLoader();
                });
        }

        var initialCallForPendingIssue = function () {
            if (!screenDataService.getScreenData()) {
                GetPendingIssue();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'assetIssueMaster_PendingIssue') {
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
                        self.model.indentNo = requestData.indentNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetPendingIssue();
            }
        }

        var initialCallForIssueList = function () {
            if (!screenDataService.getScreenData()) {
                GetIssueList();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'assetIssueMaster_IssueList') {
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
                        self.model.indentNo = requestData.indentNo
                        self.model.issueNo = requestData.issueNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetIssueList();
            }
        }

        var GetPendingIssue = function () {
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
                var URI = CONSTANTS.ASSET.ASEET_ISSUE.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "indentType": !!self.model.indentType && !!self.model.indentType.id ? self.model.indentType.id : undefined,
                    "status": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    "indentNo": !!self.model.indentNo ? self.model.indentNo : undefined,
                    "unitId":$scope.unitId,
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.pendingIssue = response.data;
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
                        "visitType": self.model.visitType,
                        "indentType": self.model.indentType,
                        "status": self.model.status,
                        "indentNo": self.model.indentNo,   
                        "fromStore": self.model.fromStore,
                        "toStore": self.model.toStore,
                    },
                    screenName: 'assetIssueMaster_PendingIssue',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var GetIssueList = function () {
            if (ValidateDate()) {
                var visitNumber = '';
                if (self.model.visitType) {
                    if (self.model.visitType.id == 1) {
                        visitNumber = self.model.visitNumber.visitNo;
                    } else {
                        visitNumber = self.model.visitNumberText;
                    }
                }
                var URI = CONSTANTS.ASSET.ASEET_ISSUE.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "indentType": !!self.model.indentType && !!self.model.indentType.id ? self.model.indentType.id : undefined,
                    "status": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    "indentNo": !!self.model.indentNo ? self.model.indentNo : undefined,
                    "unitId":$scope.unitId,

                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.issuelist = response.data;
                        self.model.patientId = undefined;
                        $rootScope.stopLoader();
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        self.model.patientId = undefined;
                        growl.error('Error');
                    });
                var screenData = {
                    searchrequest: {
                        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                        "visitType": self.model.visitType,
                        "indentType": self.model.indentType,
                        "status": self.model.status,
                        "indentNo": self.model.indentNo,
                        "fromStore": self.model.fromStore,
                        "toStore": self.model.toStore,
                    },
                    screenName: 'assetIssueMaster_IssueList',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var NavigateToDetail = function (item, tab) {
            commonDetailService.setDataId(item.id);

            if (tab == 2) {
                $state.go('detailsAssetIssueList');
            } else {
                $state.go('DetailsAssetPendingIssue');
            }
        }
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("assetIssueMaster", {
            url: '/assetIssueMaster',
            templateUrl: 'views/master/UnitMaster/assetManagement/asset-issue-master.html',
            controller: "AssetIssueMaster.Controller",
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 214);
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
        .controller("AssetIssueMaster.Controller", assetIssueMasterController)
})();