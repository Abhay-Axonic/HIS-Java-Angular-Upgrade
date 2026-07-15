
(function () {
    "use strict";
    function KITmasterPharmacyController($scope, $rootScope, $state, $http, $filter, $cookies, CONSTANTS, GenericService, growl, commonDetailService ,PaginationService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                kitTypeList: [{
                    "description": "Select Kit Type"
                }],
                SelectedItemsIds: [],
                itemList: [],
                ItemDetailList: [],
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
            self.model.ViewItemDetails = ViewItemDetails;
            self.model.ToggleStatus = ToggleStatus;
            self.model.NavigateToEdit = NavigateToEdit;
            self.model.NavigateToAddNew = NavigateToAddNew;
            GetKitMasterList();
        }

        //Method to populate KitMaster data  for editing
        var NavigateToAddNew = function () {
            $state.go('addKITmasterPharmacy');
        }

        var NavigateToEdit = function (data) {
            commonDetailService.setDataId(data.id);
            $state.go('addKITmasterPharmacy');
        }

        var GetKitMasterList = function () {
           var URI = CONSTANTS.MASTER.UNIT.KIT_MASTER.GET_ALL + $scope.organizationId + "/" +$scope.unitId ;
            //var URI = 'http://clini-his.predikly.com:8082/HIS-WAR/api/global/status';
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                	
                	self.model.pagination.data=[];
                	
                    self.model.kitMasterList = response.data;
                    
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.kitMasterList, self.model.pagination);
                      }
               	 
                    $rootScope.stopLoader();
                },
                function (err) {
                    $rootScope.stopLoader();
                });
        }

        var ViewItemDetails = function (data) {
            self.model.kitMasterItemsList = data.items;
        }

        var ToggleStatus = function (kitMaster) {
            var URI = CONSTANTS.MASTER.UNIT.KIT_MASTER.UPDATE_API + kitMaster.id;
            var data = {
                "status": kitMaster.status,
                "unitId" : $scope.unitId,
                "userId":$scope.userId
            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    growl.success('Status changed successfully');
                    //GetKitMasterList();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Something went wrong');
                });
        }
        
        $scope.updateStatus = function (kitMaster) {
        	var URIVALI = BASE_URL + ROOT_URL + "/api/units/kit/master/updateStatus";
            var data = {
                "status": kitMaster.status,
                "id":kitMaster.id
            };
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URIVALI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    growl.success('Status changed successfully');
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Something went wrong');
                });
        }
        
        
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("KITmasterPharmacy", {
            url: "/KITmasterPharmacy",
            title:"Kit Master",
            templateUrl: 'views/master/UnitMaster/pharmacy/KIT-master-pharmacy.html',
            controller: "KITmasterPharmacy.Controller",
            controllerAs: "vm"
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("KITmasterPharmacy.Controller", KITmasterPharmacyController)
})();
