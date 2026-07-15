(function () {
    "use strict";

    function rejectedMaterialAcceptanceController($scope, $cookies, CONSTANTS, $rootScope, $http, $state, GenericService,PaginationService, screenDataService, commonDetailService, $filter, growl, reportService) {
        var self = this;
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
                fromStores: [{
                    "store": "All Stores"
                }],
                toStores: [{
                    "store": "All Stores"
                }],
                statuses: [{
                    "status": "All"
                }],
                searchMaterialRejectData: undefined

            };

            self.model.MaterialRejectAcceptanceData = MaterialRejectAcceptanceData;
            self.model.materialRejectAcceptanceDetails = materialRejectAcceptanceDetails;
            self.model.printRejectedMaterialAcceptanceReport = printRejectedMaterialAcceptanceReport;

            getDropdownsList();
        }

        var getDropdownsList = function () {
            var materialpicker = self.model;
            materialpicker.fromStore = materialpicker.fromStores[0];
            materialpicker.toStore = materialpicker.toStores[0];
            materialpicker.status = materialpicker.statuses[0];

            var URI = CONSTANTS.INVENTORY.STORE.REJECTED_MATERIAL_ACCEPTANCE.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    materialpicker.fromStores = materialpicker.fromStores.concat(response.data.fromStore);
                    materialpicker.toStores = materialpicker.toStores.concat(response.data.toStore);
                    materialpicker.statuses = materialpicker.statuses.concat(response.data.status);
                    $rootScope.stopLoader();
                    initialCall();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                MaterialRejectAcceptanceData()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Rejected Material Acceptance') {

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
                MaterialRejectAcceptanceData();
            }

        }

        var MaterialRejectAcceptanceData = function () {
            var URI = CONSTANTS.INVENTORY.STORE.REJECTED_MATERIAL_ACCEPTANCE.SEARCH_API;
            var data = {

                "fromStoreId": self.model.fromStore.id ? self.model.fromStore.id : undefined,
                "toStoreId": self.model.toStore.id ? self.model.toStore.id : undefined,
                "returnNo": $scope.searchReturnNo,
                "statusId": self.model.status.id ? self.model.status.id : undefined,
                "unitId":$scope.unitId,
                "userId":$scope.userId

            };
            $rootScope.startLoader()
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.pagination.data=[];
                    self.model.searchMaterialRejectData = response.data;
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchDataByField(self.model.searchMaterialRejectData, self.model.pagination);
                      }
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
                screenName: 'Rejected Material Acceptance',
            }
            screenDataService.setScreenData(screenData);
        }

        var materialRejectAcceptanceDetails = function (reject) {
            commonDetailService.setDataId(reject.id);
        }


      //Chetana
        var printRejectedMaterialAcceptanceReport=function(item){
        	
  	      var data = {
  	        "appName": "HIS",
  	        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
  	        "reportDesignFilePath": "get_rejected_material_acceptance.rpt",
  	        "reportFormat": "PDF",
  	        "exportFileName": "RejectedMaterialAcceptance",
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

 //------//

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("rejectedMaterialAcceptance", {
            url: '/rejectedMaterialAcceptance',
            templateUrl: 'views/PharmacyInventory/InPatient/rejected-material-acceptance.html',
            controller: "RejectedMaterialAcceptance.Controller",
            title: 'Rejected Material Acceptance',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 131);
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
        .controller("RejectedMaterialAcceptance.Controller", rejectedMaterialAcceptanceController)
})();

