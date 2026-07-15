(function () {
    "use strict";

    function implantIndentAcceptanceController($scope, $rootScope, $http, $state, GenericService,PaginationService, $cookies, CONSTANTS, screenDataService, commonDetailService, $filter, growl) {
        var self = this;
        var selectItemList = [];
        var masterId;
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; function initializeController() {
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
                implantIndAceeptanceData: undefined

            };

            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.SearchpatientNames = SearchpatientNames;
            self.model.AddPatientDetails = AddPatientDetails;
            self.model.ImplantIndAceeptanceData = ImplantIndAceeptanceData;
            self.model.ImplantIndAceeptanceDetails = ImplantIndAceeptanceDetails;
            getDropdownsList();
        }

        var getDropdownsList = function () {
            self.model.toStore = self.model.toStores[0];
            self.model.status = self.model.statuses[0];
            self.model.indentType = self.model.indentTypes[0];

            var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT_ACCEPATNCE.DROPDOWN_API  + '/'+$scope.organizationId  + '/' + $scope.unitId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
  
                    self.model.toStores = self.model.toStores.concat(response.data.store);
                    self.model.statuses = self.model.statuses.concat(response.data.status);
                    self.model.indentTypes = self.model.indentTypes.concat(response.data.indentType);
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
        var ValidateDate = function () {
            var isValid = true;
            if (self.model.fromDate > self.model.toDate) {
                growl.error('From Date should lesser than To Date');
                isValid = false;
              } else if (self.model.fromDate == self.model.toDate) {
                isValid = true;
              }
            if (!self.model.fromDate || !self.model.toDate) {
                growl.error('Enter Valid Date');
                isValid = false;
            }

            return isValid;
        }

        // $scope.validatesearch = function () {
        //     var isValid = true;
        //     if(self.model.visitType.id == 0){
        //         growl.error("Please select visit type");
        //         isValid = false;
        //     }
        //     return isValid;
        // }

        var SearchpatientNames = function (search) {
            // if($scope.validatesearch()){
                if (search.length < 3) return;

                if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3)) {
                    self.model.prevSearch = search;

                    var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search + "/" + self.model.visitType.id;

                    $rootScope.startLoader();
                    return GenericService.serviceAction("GET", URI, {})
                        .then(function (response) {
                            $rootScope.stopLoader();
                            if (!!response.data && response.data.length > 0) {
                                self.model.patientNames = response.data;
                                self.model.patientId = response.data.patientId;
                                self.model.uhid = response.data.uhid;
                                $rootScope.stopLoader();
                                return $filter('filter')(self.model.patientNames, {
                                    $: search
                                });
                            } else {
                                $rootScope.stopLoader();
                                return PatientNameNotFound(search);
                            }
                        }, function (err) {
                            $rootScope.stopLoader();
                            return PatientNameNotFound(search);
                        });
                } else {
                    if (!!self.model.patientNames && self.model.patientNames.length > 0 &&
                        self.model.patientNames[0].itemFound != undefined && !self.model.patientNames[0].itemFound) {
                        return PatientNameNotFound(search);
                    } else {
                        return $filter('filter')(self.model.patientNames, {
                            $: search
                        });
                    }
                }
            // }
        }

        var PatientNameNotFound = function (search) {
            var item = {
                "itemFound": false,
                "details": "Patient Not found with name " + search
            };
            self.model.patientNames = [item];
            return self.model.patientNames;
        }

        var AddPatientDetails = function (item) {

            if (!!item.patientId) {
                var URI = CONSTANTS.GLOBAL.PATIENT_DETAILS_SEARCH_API +'/' + $scope.organizationId + '/'+$scope.unitId;

                var data = {
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : 0,
                    "patientId": !!item.patientId ? item.patientId : undefined,
                    // "uhid": !!item.uhid ? item.uhid : undefined,
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data)
                    .then(function (response) {
                        self.model.patientInfo = response.data,
                            self.model.patientName = response.data.patientName,
                            self.model.patientId = response.data.patientId,
                            self.model.uhid = response.data.uhid,
                            self.model.bedNo = response.data.bedNo,
                            self.model.bedId = response.data.bedId
                        if (!!response.data.visitDetails.length) {
                            if (self.model.visitType.id == 1) {
                                self.model.visitDetails = self.model.visitDetails.concat(response.data.visitDetails);
                            } else if (self.model.visitType.id != 1) {
                                self.model.visitNumberText = response.data.visitDetails[0].visitNo;
                                self.model.encounterId = response.data.visitDetails[0].encounterId,
                                    self.model.admissionId = response.data.visitDetails[0].admissionId,
                                    self.model.doctorId = response.data.visitDetails[0].doctorId
                            }
                        }
                   
                        $rootScope.stopLoader();
                    }, function (err) {
                        growl.error(err.data.summary);
                        $rootScope.stopLoader();
                    });
            }
        }
        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                ImplantIndAceeptanceData()
            }
            else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Implant Indent Acceptance') {

                        var requestData = dataobj.searchrequest;
                      
                        self.model.toStore = self.model.toStores.find(function (toStore) {
                            return requestData.toStore.id == toStore.id;
                        });
                        self.model.indentType = self.model.indentTypes.find(function (indentType) {
                            return requestData.indentType.id == indentType.id;
                        });
                        self.model.status = self.model.statuses.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        self.model.indentNumber = requestData.indentNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);

                    }
                });
                ImplantIndAceeptanceData();
            }

        }

        var ImplantIndAceeptanceData = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.PROCUREMENT.IMPLANT_INDENT_ACCEPATNCE.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "storeId": !!self.model.toStore && !!self.model.toStore.id ? self.model.toStore.id : undefined,
                    "issueNo": !!self.model.issueNo ? self.model.issueNo : undefined,
                    "bedNo": !!self.model.bedNo ? self.model.bedNo : undefined,
                    "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
                    "indentNo": self.model.indentNo ? self.model.indentNo : undefined,
                    "statusId": self.model.status.id ? self.model.status.id : undefined,
                    "indentTypeId": self.model.indentType.id ? self.model.indentType.id : undefined,
                    "cookieStoreId":$scope.storeId,
                    "unitId":$scope.unitId,
                    
                };
                $rootScope.startLoader()
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        $rootScope.stopLoader();
                        for (var i = 0; i < response.data.length; i++) {
                            response.data[i]["isChecked"] = false;
                        }
                        self.model.pagination.data=[];
                        self.model.totalPages=0;
                        self.model.implantIndAceeptanceData = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.implantIndAceeptanceData, self.model.pagination);
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
                        "indentNo": self.model.indentNumber,
                        "status": self.model.status,
                        "indentType": self.model.indentType,
                    },
                    screenName: 'Implant Indent Acceptance',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var ImplantIndAceeptanceDetails = function (issue) {
            commonDetailService.setDataId(issue.id);
            $state.go('detailsImplantIndentAcceptance');
        }
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("implantIndentAcceptance", {
            url: '/implantIndentAcceptance',
            templateUrl: 'views/master/TheatreManagement/implant-indent -acceptance.html',
            controller: "ImplantIndentAcceptance.Controller",
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 327);
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
        .controller("ImplantIndentAcceptance.Controller", implantIndentAcceptanceController)
})();  