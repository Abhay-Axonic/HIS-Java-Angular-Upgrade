(function () {
    "use strict";

    function materialPickerController($scope, $rootScope, $http, $state, $cookies, CONSTANTS, GenericService,PaginationService, screenDataService, commonDetailService, $filter, growl, reportService ) {
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

            };
            self.model.printMaterialPickerReport = printMaterialPickerReport;
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.MaterialPickerData = MaterialPickerData;
            self.model.materialPickerDetails = materialPickerDetails;
            self.model.activeItem = '';
            getDropdownsList();
        }

        var getDropdownsList = function () {
            var materialpicker = self.model;
            materialpicker.fromStore = materialpicker.fromStores[0];
            materialpicker.toStore = materialpicker.toStores[0];
            materialpicker.status = materialpicker.statuses[0];
            materialpicker.indentType = materialpicker.indentTypes[0];

            var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_PICKER.DROPDOWN_API +"/"+$scope.unitId + "/"+$scope.organizationId + "/"+$scope.userId;;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    materialpicker.fromStores = materialpicker.fromStores.concat(response.data.fromStore);
                    materialpicker.toStores = materialpicker.toStores.concat(response.data.toStore);
                    materialpicker.statuses = materialpicker.statuses.concat(response.data.status);
                    materialpicker.indentTypes = materialpicker.indentTypes.concat(response.data.indentType);
                    $rootScope.stopLoader();
                    initialCall();
                    // GetStoreIndents();
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
                MaterialPickerData()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Material Picker') {

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
                        self.model.indentNo = requestData.indentNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);

                    }
                });
                MaterialPickerData();
            }

        }

        var MaterialPickerData = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.INVENTORY.STORE.MATERIAL_PICKER.SEARCH_API;
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

                        for (var i = 0; i < response.data.length; i++) {
                            response.data[i]["isChecked"] = false;
                        }
                        self.model.pagination.data=[];
                        self.model.searchMaterialPickerData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.searchMaterialPickerData, self.model.pagination);
                          }
                        $rootScope.stopLoader();
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });

                // var screenData = {
                //     searchrequest: {
                //         "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                //         "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                //         "fromStore": self.model.fromStore,
                //         "toStore": self.model.toStore,
                //         "indentNo": self.model.indentNo,
                //         "status": self.model.status,
                //         "indentType": self.model.indentType,
                //     },
                //     screenName: 'Material Picker',
                // }
                // screenDataService.setScreenData(screenData);
            }
        }
        var materialPickerDetails = function (picker) {
            commonDetailService.setDataId(picker.id);
            //$state.go('detailsMaterialPicker'); 
        }
        
        
        /***Chetana**/
        var printMaterialPickerReport = function (pickerData) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_material_picker.rpt",
                "reportFormat": "PDF",
                "exportFileName": "MaterialPicker",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": pickerData.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
       /*****/ 

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("materialPicker", {
            url: '/materialPicker',
            templateUrl: 'views/PharmacyInventory/InPatient/material-picker.html',
            controller: "MaterialPicker.Controller",
            title: 'Material Picker',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 132);
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
        .controller("MaterialPicker.Controller", materialPickerController)
})();
