(function () {
    "use strict";

    function recallDrugsController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
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
            };

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.RecallDrugSearch = RecallDrugSearch;
            RecallDrugSearch();
        }

        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var RecallDrugSearch = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.INVENTORY.STORE.DRUG_RECALL.RECALL_DRUG;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "unitId":$scope.unitId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        for (var i = 0; i < response.data.length; i++) {
                            response.data[i]["isChecked"] = false;
                        }
                        self.model.recallDrugData = response.data;
                        $rootScope.stopLoader();
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
            }
        }

        var ValidateDate = function () {
            var isValid = true;

            if (!self.model.fromDate) {
                growl.error('Enter From Date');
                isValid = false;
            }

            if (!self.model.toDate) {
                growl.error('Enter To Date');
                isValid = false;
            }

            if (!!self.model.fromDate && !!self.model.toDate && self.model.fromDate > self.model.toDate) {
                growl.error('From date Cannot be later than To date');
                isValid = false;
            }

            return isValid;
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("recallDrugs", {
            url: "/recallDrugs",
            templateUrl: 'views/PharmacyInventory/Store/recall-drugs.html',
            controller: "RecallDrugs.Controller",
            title: 'Recall Drugs',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 140);
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
        .controller("RecallDrugs.Controller", recallDrugsController);
})();