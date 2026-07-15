(function () {
    "use strict";

    function linenIssueController($scope, $rootScope, $http, $state, $cookies, CONSTANTS, GenericService, commonDetailService, $filter, growl) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            self.model = {

                requestData: [],
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

            };

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.MaterialPendingListData = MaterialPendingListData;
            self.model.materialPendingListDetails = materialPendingListDetails;
            self.model.ActiveTab = 1;
            setScreen();
            

        }

        var setScreen = function () {
            debugger
            if (self.model.ActiveTab == 1) {
                getDropdownsListPendingList();
            }
            else {
                getDropdownsListIssuList();
            }
        }

        var getDropdownsListPendingList = function () {
            var materialpending = self.model;
            materialpending.fromStore = materialpending.fromStores[0];
            materialpending.toStore = materialpending.toStores[0];
            materialpending.status = materialpending.statuses[0];
            materialpending.indentType = materialpending.indentTypes[0];

            var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_ISSUE.LINEN_PENDING_ISSUE.DROPDOWN_API+ '/' + $scope.unitId+ '/'+$scope.userId;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    materialpending.fromStores = materialpending.fromStores.concat(response.data.fromStore);
                    materialpending.toStores = materialpending.toStores.concat(response.data.toStore);
                    materialpending.statuses = materialpending.statuses.concat(response.data.status);
                    materialpending.indentTypes = materialpending.indentTypes.concat(response.data.indentType);
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

        var MaterialPendingListData = function () {

            var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_ISSUE.LINEN_PENDING_ISSUE.SEARCH_API;

            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                "indentNo": self.model.indentNo ? self.model.indentNo : undefined,
                "statusId": self.model.status.id ? self.model.status.id : undefined,
                "indentTypeId": self.model.indentType.id ? self.model.indentType.id : undefined,
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
                    self.model.searchMaterialPendingListData = response.data;
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }

        var materialPendingListDetails = function (pending) {
            commonDetailService.setDataId(pending.id);
            $state.go('linenIssuePendingDetails');
        }



        var getDropdownsListIssuList = function () {
            var materialissue = self.model;
            materialissue.fromStore = materialissue.fromStores[0];
            materialissue.toStore = materialissue.toStores[0];
            materialissue.status = materialissue.statuses[0];
            materialissue.indentType = materialissue.indentTypes[0];

            var URI = CONSTANTS.LINEN_LAUNDRY.LINEN_ISSUE.LINEN_ISSUE_LIST.DROPDOWN_API+ '/' + $scope.unitId +  '/'+$scope.userId;


            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    materialissue.fromStores = materialissue.fromStores.concat(response.data.fromStore);
                    materialissue.toStores = materialissue.toStores.concat(response.data.toStore);
                    materialissue.statuses = materialissue.statuses.concat(response.data.status);
                    materialissue.indentTypes = materialissue.indentTypes.concat(response.data.indentType);
                    initialCall();
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("linenIssue", {
            url: "/linenIssue",
            templateUrl: 'views/Linen/linen-issue.html',
            controller: "LinenIssue.Controller",
            title: 'Linen Issue',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 344);
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
        .controller("LinenIssue.Controller", linenIssueController);
})();

