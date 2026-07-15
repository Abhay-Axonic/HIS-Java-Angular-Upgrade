(function () {
    "use strict";

    function consumableAcceptanceController($scope, $rootScope, $http, $cookies, CONSTANTS, StatusService,PaginationService,commonDetailService, screenDataService, $state, GenericService, growl, reportService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            var date = new Date();
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
                stores: [{
                    "store": "All Stores"
                }], 
                statuses: [{
                    "status": "All Status"
                }],
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
                SelectedRequestIds: [],
                selectAllRows: false,
            };
            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
            });  
            self.model.ConsumableAcceptanceSearch = ConsumableAcceptanceSearch;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.isSelected = isSelected;
            self.model.isDisable = isDisable;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.SelectConsumableRequest = SelectConsumableRequest;
            self.model.setStatus = setStatus;
            self.model.ApproveConsumableAcceptance = ApproveConsumableAcceptance;
            self.model.ConsumableDetails = ConsumableDetails;
            self.model.printConsumableAcceptanceReport = printConsumableAcceptanceReport;

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
        var consumption = self.model; 
        consumption.store = consumption.stores[0];
        consumption.status = consumption.statuses[0]; 
        var URI = CONSTANTS.INVENTORY.NURSING.NON_BILLABLE_CONSUMPTION.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;
  
        $rootScope.startLoader();
        GenericService.serviceAction("GET", URI, {}).then(
          function (response) {
            consumption.stores = consumption.stores.concat(response.data.store); 
            consumption.statuses = consumption.statuses.concat(response.data.status); 
            initialCall();
            $rootScope.stopLoader();
            // GetStoreIndents();
          },
          function (err) {
            $rootScope.stopLoader();
          });
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
            ConsumableAcceptanceSearch()
        }
        else {
          screenDataService.getScreenData().forEach(function (dataobj) {
            if (dataobj.screenName == 'Consumable Acceptance') {
  
                var requestData = dataobj.searchrequest;
              
                self.model.store = self.model.stores.find(function (store) {
                  return requestData.store.id == store.id;
                });
           
                self.model.status = self.model.statuses.find(function (status) {
                  return requestData.status.id == status.id;
                });
                self.model.consumptionNo = requestData.consumptionNo
                self.model.fromDate = new Date(requestData.fromDate);
                self.model.toDate = new Date(requestData.toDate);
  
            }
          });
          ConsumableAcceptanceSearch();
        }
  
      }
      
        var ConsumableAcceptanceSearch = function () {
            if(ValidateDate()){
            var URI = CONSTANTS.INVENTORY.NURSING.CONSUMABLE_ACCEPTANCE.SEARCH_API;
            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "storeId": self.model.store.id,
                "statusId": self.model.status.id,
                "consumptionNo" : self.model.consumptionNo,
                "unitId":$scope.unitId

            };
            self.model.pagination.data=[];
            self.model.pagination.totalPages=0;
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    for (var i = 0; i < response.data.length; i++) {
                        response.data[i]["isChecked"] = false;
                    }
                    self.model.consumableData = response.data;
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.consumableData, self.model.pagination);
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
                      "store": self.model.store,
                      "consumptionNo": self.model.consumptionNo,
                      "status": self.model.status,
                    },
                    screenName: 'Consumable Acceptance',
                  }
                  screenDataService.setScreenData(screenData);
        }
    }

        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.consumableData.forEach(function (consumable) {
                    if (!isDisable(consumable)) {
                        consumable.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(consumable.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.consumableData.forEach(function (consumable) {
                    consumable.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }

        }

        var isSelected = function (consumable) {
            if (!isDisable(consumable))
                return consumable.isSelected;
            else
                return true;
        }
        var isDisable = function (consumable) {
            if (consumable.status != 'Pending') {
                return true;
            }
            return false;
        }

        var SelectConsumableRequest = function (consumable) {
            var consumableIds = self.model.SelectedRequestIds;

            if (consumable.isSelected) {
                consumableIds.push(consumable.id);
            } else {
                var index = consumableIds.indexOf(consumable.id);
                if (index > -1) {
                    consumableIds.splice(index, 1);
                }
            }

            if (self.model.consumableData.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.consumableData.forEach(function (consumable) {
                if (consumable.isSelected == true) {
                    self.model.isItemChecked = true;
                    return;
                }
            });
        }
        var ApproveConsumableAcceptance = function () {

            var URI = CONSTANTS.INVENTORY.NURSING.CONSUMABLE_ACCEPTANCE.APPROVE_API;

            var data = {
                "consumptionId": self.model.SelectedRequestIds,
                "statusId": setStatus('Closed'),
                "approvalStatusId": setStatus('Approved'),
                "unitId":$scope.unitId,
                "organizationId":$scope.organizationId,
                "userId":$scope.userId

            };

            $rootScope.startLoader();
            GenericService.serviceAction("PATCH", URI, data).then(
                function (response) {
                    self.model.SelectedRequestIds = [];
                    $rootScope.stopLoader();
                    growl.success(response.data.message);
                    ConsumableAcceptanceSearch();
                    self.model.selectAllRows= false;
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error(err.data.msg);
                });

        }
        function setStatus(x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }
        var ConsumableDetails = function (consumable) {
            commonDetailService.setDataId(consumable.id);
            $state.go('detailsConsumableAcceptance'); 
          }
      
        /***Chetana**/
        var printConsumableAcceptanceReport = function (item) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "getconsumableacceptance.rpt",
                "reportFormat": "PDF",
                "exportFileName": "ConsumableAcceptance",
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
        $stateProvider.state("consumableAcceptance", {
            url: "/consumableAcceptance",
            templateUrl: 'views/PharmacyInventory/InPatient/consumable-acceptance.html',
            controller: "ConsumableAcceptance.Controller",
            title: 'Consumable Acceptance',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 64);
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
        .controller("ConsumableAcceptance.Controller", consumableAcceptanceController);
})(); 