

(function () {
    "use strict";

    function linenIndentApprovalController($scope, $rootScope, $http, $cookies, CONSTANTS, screenDataService, $state, commonDetailService, GenericService, growl) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
            $rootScope.loginpage = true;
            var date = new Date();
            
            self.model = {
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date(),
                    minDate : date.setDate((new Date()).getDate() - 90),
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
                }]


            };
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.LinenIndentApprovalData = LinenIndentApprovalData;
            self.model.LinenIndentApprovalDetails = LinenIndentApprovalDetails;
            PopulateDropdown();
        }
        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                LinenIndentApprovalData()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Store Indent')
                        var requestData = dataobj.searchrequest;
                    self.model.fromStoreId = self.model.fromStores.find(function (fromStore) {
                        return requestData.fromStore.id == fromStore.id;
                    });
                    self.model.toStoreId = self.model.toStores.find(function (toStore) {
                        return requestData.toStore.id == toStore.id;
                    });
                    self.model.indentNumber = requestData.indentNo
                    self.model.fromDate = new Date(requestData.fromDate);
                    self.model.toDate = new Date(requestData.toDate);
                });

                LinenIndentApprovalData();
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
            var indent = self.model;
            indent.fromStore = indent.fromStores[0];
            indent.toStore = indent.toStores[0];
            indent.status = indent.statuses[0];
            var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT_APPROVAL.DROPDOWN_API+ '/' + $scope.unitId+ '/'+$scope.userId;
            $rootScope.startLoader();

            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    indent.fromStores = indent.fromStores.concat(response.data.fromStore);
                    indent.toStores = indent.toStores.concat(response.data.toStore);
                    indent.statuses = indent.statuses.concat(response.data.status);
                    initialCall();
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

            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }

        var LinenIndentApprovalData = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_INDENT_APPROVAL.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                    "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                    "indentNo": self.model.indentNumber ? self.model.indentNumber : undefined,
                    "statusId": self.model.status.id ? self.model.status.id : undefined,
                    "indentTypeId": self.model.indentType ? self.model.indentType.id : undefined,
            		"unitId":$scope.unitId,
            		"userId":$scope.userId,
                };
                $rootScope.startLoader()
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        for (var i = 0; i < response.data.length; i++) {
                            response.data[i]["isChecked"] = false;
                        }
                        self.model.searchLinenIndentApprovalData = response.data;
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
                        "statusId": self.model.status,
                        "indentTypeId": self.model.indentType,
                    },
                    screenName: 'Store Indent',
                }
                screenDataService.setScreenData(screenData);
            }
        }
        var LinenIndentApprovalDetails = function (indent) {
            commonDetailService.setDataId(indent.id);
            $state.go('linenIndentApprovalDetails');
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("linenIndentApproval", {
            url: "/linenIndentApproval",
            templateUrl: 'views/Linen/linen-indent-approval.html',
            controller: "LinenIndentApproval.Controller",
            title: 'Linen Indent Approval',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 95);
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
        .controller("LinenIndentApproval.Controller", linenIndentApprovalController);
})();
