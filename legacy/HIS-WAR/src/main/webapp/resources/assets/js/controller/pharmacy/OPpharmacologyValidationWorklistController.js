(function () {
    "use strict";

    function OPpharmacologyValidationWorklistController($scope, $rootScope, $http, CONSTANTS, GenericService,PaginationService , $filter, $state, growl, CurrencyService, pharmacologyValidationService, screenDataService, cancelReasonService,$cookies) {
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
                visitTypeDataList: [{
                    "type": "All"
                }],
                statusDataList: [{
                    "status": "All"
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
            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.SearchpatientNames = SearchpatientNames;
            self.model.SearchUhid = SearchUhid;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.AddUHID = AddUHID;
            self.model.SearchdoctorNames = SearchdoctorNames;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.GetPharmacologyValidationWorklist = GetPharmacologyValidationWorklist;
            self.model.ActiveMedicationDetails = ActiveMedicationDetails;
            self.model.NavigateToDetail = NavigateToDetail;
            self.model.cancelBillPopup = cancelBillPopup;
            self.model.ModalDismiss = ModalDismiss;
            self.model.initialCall = initialCall;
            self.model.cancelItem = cancelItem;

            PopulateDropdown();
            
            var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
        }

        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        //  $scope.validatesearch = function () {
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

                if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length >= 3)) {
                    self.model.prevSearch = search;
                    var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search + "/" + self.model.visitType.id;
                    $rootScope.startLoader();
                    return GenericService.serviceAction("GET", URI, {})
                        .then(function (response) {
                            $rootScope.stopLoader();
                            if (!!response.data && response.data.length > 0) {
                                self.model.patientNames = response.data;
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

        var AddUHID = function (item) {
            self.model.patientData = item;
            self.model.uhid = item.uhid;
            self.model.patientId = item.patientId;
            self.model.patientName = item.patientName;
        }

        var PatientNameKeyPress = function (event) {
            var search = $filter('filter')(self.model.patientNames, {
                $: self.model.patientName
            });
            if (!self.model.patientName) {
                self.model.patientData = null;
                self.model.patientId = null;
                self.model.uhid = null;
            } else {
                // self.model.patientData = search[0];
                // self.model.patientId = search[0].patientId;
                // self.model.uhid = search[0].uhid;
            }
        }

        var uHIDKeyPress = function (event) {
            var search = $filter('filter')(self.model.patientNames, {
                $: self.model.uhid
            });
            if (!self.model.uhid) {
                self.model.patientData = null;
                self.model.patientId = null;
                self.model.patientName = null;
            } else {
                // self.model.patientData = search[0];
                // self.model.patientId = search[0].patientId;
                // self.model.patientName = search[0].patientName;
            }
        }
        
        // self.model.ChangeVisitType = ChangeVisitType;
        var ChangeVisitType = function(){
            self.model.patientName = null;   
            self.model.uhid = null;
        }

        var SearchUhid = function (search) {

            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length >= 1)) {
                self.model.prevSearch = search;

                var URI = CONSTANTS.GLOBAL.UHID_SEARCH_API + search +'/' + $scope.organizationId;

                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                        $rootScope.stopLoader();
                        if (!!response.data && response.data.length > 0) {
                        	 $rootScope.stopLoader();
                            return self.model.patientNames = response.data;
                           /* return $filter('filter')(self.model.patientNames, {
                                $: search
                            });*/
                        } else {
                            $rootScope.stopLoader();
                            return UHIDNotFound(search);
                        }
                    }, function (err) {
                        $rootScope.stopLoader();
                        return UHIDNotFound(search);
                    });
            } else {
                if (!!self.model.patientNames && self.model.patientNames.length > 0 &&
                    self.model.patientNames[0].itemFound != undefined && !self.model.patientNames[0].itemFound) {
                    return UHIDNotFound(search);
                } else {
                    return $filter('filter')(self.model.patientNames, {
                        $: search
                    });
                }
            }
        }
        
        
        var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.organizationId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
        $scope.userAccess = true;
        $scope.getPharmacyRole = function () {

        	var URI = BASE_URL + ROOT_URL +"/adt/getPharmacyUserRoleType";

                var data = {
                    "organizationId": $scope.organizationId,
                    "userId": $scope.userId,
                    "unitId": $scope.unitId,
                };

                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                    	if (response.data.listObject != null && response.data.listObject.length>0) {	 
                    		 $scope.userAccess = false;
                          }
                    });
        }

        $scope.getPharmacyRole();
        

        var UHIDNotFound = function (search) {
            var item = {
                "itemFound": false,
                "details": "UHID Not found with search " + search
            };
            self.model.patientNames = [item];
            return self.model.patientNames;
        }

        var SearchdoctorNames = function (search) {

            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;
                var URI = CONSTANTS.GLOBAL.DOCTOR_SEARCH_API +$scope.organizationId +"/"+$scope.unitId + "/" + search;

                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                        $rootScope.stopLoader();
                        if (!!response.data && response.data.length > 0) {
                            self.model.doctorNames = response.data;
                            $rootScope.stopLoader();
                            return $filter('filter')(self.model.doctorNames, {
                                $: search
                            });
                        } else {
                            $rootScope.stopLoader();
                            return DoctorNameNotFound(search);

                        }
                    }, function (err) {
                        $rootScope.stopLoader();
                        return DoctorNameNotFound(search);
                    });
            } else {
                if (!!self.model.doctorNames && self.model.doctorNames.length > 0 &&
                    self.model.doctorNames[0].itemFound != undefined && !self.model.doctorNames[0].itemFound) {
                    return DoctorNameNotFound(search);
                } else {
                    return $filter('filter')(self.model.doctorNames, {
                        $: search
                    });
                }
            }
        }

        var DoctorNameNotFound = function (search) {
            var item = {
                "itemFound": false,
                "details": "Doctor Not found with name " + search
            };
            self.model.patientNames = [item];
            return self.model.patientNames;
        }

        var PopulateDropdown = function () {
            var URI = CONSTANTS.PHARMACY.OP.PHARMACOLOGICAL_VALIDATION.DROPDOWN_API;
            self.model.visitType = self.model.visitTypeDataList[0];
            self.model.status = self.model.statusDataList[0];
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {

                    self.model.visitTypeDataList = self.model.visitTypeDataList.concat(response.data.visitType);
                    self.model.statusDataList = self.model.statusDataList.concat(response.data.status);
                    initialCall();
                    var visitTypeData = [];
                    self.model.visitTypeDataList.map(function (obj) {
                        if (obj.id !== 1) {
                            visitTypeData.push(obj);
                        }
                    });
                    self.model.visitTypeDataList = visitTypeData;

                    $rootScope.stopLoader();
                });
        }

        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                GetPharmacologyValidationWorklist()
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'Pharmacology Validation Worklist') {
                        var requestData = dataobj.searchrequest;
                        self.model.visitType = self.model.visitTypeDataList.find(function (visitType) {
                            if (requestData.visitType.id == visitType.id)
                                return visitType;
                        });
                        self.model.status = self.model.statusDataList.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        self.model.patientId = requestData.patientId
                        self.model.patientName = requestData.patientName
                        self.model.doctorName = requestData.doctorName
                        self.model.wardName = requestData.wardName
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetPharmacologyValidationWorklist();
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
        var ValidetReason = function () {
            var isValid = true;

            if (!self.model.cancelReason) {
                self.model.isReasonValid = true;
                isValid = false;
            }

            return isValid;
        }
        function setStatus(x) {
            var status = self.model.statusDataList.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }
        var cancelItem = function(id){
            if (ValidetReason()) {
                var URI = CONSTANTS.PHARMACY.OP.SALES_WORKLIST.CANCEL_API ;

                var data = {
                    "id": self.model.id,
                    "statusId": self.model.cancelReason ? parseInt(self.model.cancelReason) : undefined,
                    "cancelNote": self.model.rejectNote
                }

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        ModalDismiss('confirmCancel');
                        self.model.rejectNote = undefined;
                        self.model.cancelReason = undefined;
                        // 
                        $rootScope.stopLoader();
                        FixedAssetSearch();
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.message);
                    });
            }
        }
        var GetPharmacologyValidationWorklist = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.PHARMACY.OP.PHARMACOLOGICAL_VALIDATION.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "doctorName": !!self.model.doctorName ? self.model.doctorName : undefined,
                    "wardName": !!self.model.wardName ? self.model.wardName : undefined,
                    "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    "uhid": 	!!self.model.uhid ? self.model.uhid : undefined,
                    "unitId": $scope.unitId,
                    "storeId" : $scope.storeId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.pagination.data=[];
                        self.model.pharmacologyValidationWorklist = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.pharmacologyValidationWorklist, self.model.pagination);
                          }
                        self.model.patientId = undefined;
                        self.model.pharmacologyValidationWorklist.forEach(function (item) {
                            if (item.priority == 'High') {
                                item.highPriority = true;
                            }
                        });
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
                        "store": self.model.store,
                        "status": self.model.status,
                        "visitType": self.model.visitType,
                        "patientId": self.model.patientId,
                        "patientName": self.model.patientName,
                        "doctorName": self.model.doctorName,
                        "wardName": self.model.wardName
                    },
                    screenName: 'Pharmacology Validation Worklist',
                }
                screenDataService.setScreenData(screenData);

            }
        }

        var ActiveMedicationDetails = function (item) {
            var URI = CONSTANTS.PHARMACY.IP.PHARMACOLOGICAL_VALIDATION.ACTIVE_MEDICATION_API  + $scope.unitId + '/' + item.id;

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.ActiveMedicationDetailsList = response.data;
                });
            $rootScope.stopLoader();
        }

        var NavigateToDetail = function (item) {
            pharmacologyValidationService.setPhaarmacologyValidationId(item.id);
            $state.go('OPpharmacologyValidationWorklistDetails');
        }

        var cancelBillPopup = function (id) { 
            self.model.id = id;
            var popup = angular.element('#confirmCancel');
            popup.modal('show');
        }

        var ModalDismiss = function () {
            var popup = angular.element('#confirmCancel');
            popup.modal('hide');
        } 

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("OPpharmacologyValidationWorklist", {
            url: "/OPpharmacologyValidationWorklist",
            templateUrl: 'views/PharmacyInventory/InPatient/op-pharmacology-validation-worklist.html',
            controller: "OPpharmacologyValidationWorklist.Controller",
            title: 'Pharmacology Validation Worklist',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 855);
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
        .controller("OPpharmacologyValidationWorklist.Controller", OPpharmacologyValidationWorklistController)
})();