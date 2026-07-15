(function () {
    "use strict";

    function pendingRejectedRejectionsController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,commonDetailService, $filter, $state, growl, CurrencyService, screenDataService , PaginationService, BillingGenericService, $window) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                visitTypeFromService :  commonDetailService.getDataIdForBedManagmentPatientIssueAcceptanceVisitType(),
                patientIdFromService :  commonDetailService.getDataIdForBedManagmentPatientIssueAcceptancePatientId(),
                uhidFromService :  commonDetailService.getDataIdForBedManagmentPatientIssueAcceptanceUhidNum(),
                floorIdFromService :  commonDetailService.getDataIdForBedManagmentPatientIssueAcceptanceFloorId(),
                doctorIdFromService: commonDetailService.getDataIdForBedManagmentPatientIssueAcceptanceDoctorId(),
                doctorNameFromService: commonDetailService.getDataIdForBedManagmentPatientIssueAcceptanceDoctorFullName(),
                patientNameFromService: commonDetailService.getDataIdForBedManagmentPatientIssueAcceptancePatientName(),
                fromDate: new Date(),
                toDate: new Date(),
                activeTab: 1,
                activeTab1: 1,
                activeTab2: 1,
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
                floorList: [{
                    "floor": "All"
                }],
                storeList: [{
                    "store": "All"
                }],
                visitDetails: [{
                    "visitNo": "Select Visit No"
                }],
                visitTypeList: [{
                    "type": "All"
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
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.SearchpatientNames = SearchpatientNames;
            self.model.SearchUhid = SearchUhid;
            self.model.AddPatientDetails = AddPatientDetails;
            self.model.SearchdoctorNames = SearchdoctorNames;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.PopulateDropdown1 = PopulateDropdown1;
            self.model.GetRejectedRejectionMedication = GetRejectedRejectionMedication;
            self.model.GetConsumableRejectedRejection = GetConsumableRejectedRejection;
            self.model.NavigateToDetail = NavigateToDetail;
            self.model.ChangeVisitNumber = ChangeVisitNumber;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.SetActiveTab = SetActiveTab;
            self.model.SetActiveTab1 = SetActiveTab1;
            self.model.SetActiveTab2 = SetActiveTab2;
            if(self.model.activeTab == 1){
                PopulateDropdown();
            }
            else{
                PopulateDropdown1();
            }
            // GetRejectedRejectionMedication();
            // GetConsumableRejectedRejection();
        }

        var SetActiveTab = function (tab) {
            self.model.activeTab = tab;
        }

        var SetActiveTab1 = function (tab) {
            self.model.activeTab1 = tab;
        }

        var SetActiveTab2 = function (tab) {
            self.model.activeTab2 = tab;
        }

        var PopulateDropdown = function () {
            if (self.model.activeTab1 == 2) {
                PopulateConsumableDropdown();
            } else {
                PopulateMedicationDropdown();
            }
        }

        var PopulateDropdown1 = function () {
            if (self.model.activeTab2 == 2) {
                PopulateConsumableDropdown();
            } else {
                PopulateMedicationDropdown();
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
                                //console.log(self.model.patientNames.itemName);
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

        var SearchUhid = function (search) {

            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
                self.model.prevSearch = search;

                var URI = CONSTANTS.GLOBAL.UHID_SEARCH_API + search +'/' + $scope.organizationId;

                $rootScope.startLoader();
                return GenericService.serviceAction("GET", URI, {})
                    .then(function (response) {
                        $rootScope.stopLoader();
                        if (!!response.data && response.data.length > 0) {
                            self.model.patientNames = response.data;
                            self.model.patientId = response.data.patientId;
                            self.model.uhid = response.data.uhid;
                            $rootScope.stopLoader();
                            //console.log(self.model.patientNames.itemName);
                            return $filter('filter')(self.model.patientNames, {
                                $: search
                            });
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

        var UHIDNotFound = function (search) {
            var item = {
                "itemFound": false,
                "details": "UHID Not found with search " + search
            };
            self.model.patientNames = [item];
            return self.model.patientNames;
        }

        var ChangeVisitNumber = function (visitData) {
            self.model.encounterId = visitData.encounterId;
            self.model.admissionId = visitData.admissionId;
            self.model.doctorId = visitData.doctorId;
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
                            self.model.uhid = response.data.uhid

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
        
        var ClearPatientInfo = function (){
            self.model.patientData = null;
            self.model.patientId = null;
            self.model.visitNumberText = null;
            self.model.encounterId = null;
            self.model.admissionId = null;
            self.model.doctorId = null;
            self.model.visitNumber = self.model.visitDetails.splice(1);
            self.model.visitNumber = self.model.visitDetails[0];
            self.model.patientInfo = null;
        }

        var ChangeVisitType = function(){
            self.model.patientName = null;   
            self.model.uhid = null;
            ClearPatientInfo();
        }
        
        var PatientNameKeyPress = function (event) {
            var search = $filter('filter')(self.model.patientNames, {
                $: self.model.patientName
            });
            if (!self.model.patientName) {      
                self.model.uhid = null;
                ClearPatientInfo();
            }
        }

        var uHIDKeyPress = function (event) {
            var search = $filter('filter')(self.model.patientNames, {
                $: self.model.uhid
            });
            if (!self.model.uhid) {
                self.model.patientName = null;
                ClearPatientInfo();
            }
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

        var PopulateMedicationDropdown = function () {

            var URI = CONSTANTS.PHARMACY.IP.PENDING_REJECTED_REJECTION.MEDICATION_DROPDOWN_API + '/' + $scope.unitId;

            self.model.visitType = self.model.visitTypeList[0];
            self.model.floor = self.model.floorList[0];

            self.model.visitTypeList = [];
            self.model.floorList = [];
            
            self.model.visitTypeList.push(self.model.visitType);
            self.model.floorList.push(self.model.floor);
            
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                    self.model.floorList = self.model.floorList.concat(response.data.floor);
                    initialCallForMedication();
                    $rootScope.stopLoader();
                });
        }

        var PopulateConsumableDropdown = function () {

            var URI = CONSTANTS.PHARMACY.IP.PENDING_REJECTED_REJECTION.CONSUMABLE_DROPDOWN_API + '/' + $scope.unitId; 

            self.model.visitType = self.model.visitTypeList[0];
            self.model.visitNumber = self.model.visitDetails[0];
            self.model.store = self.model.storeList[0];
            // self.model.ward = self.model.wardList[0];
            self.model.visitTypeList = [];
            self.model.storeList  = [];
            self.model.visitTypeList.push(self.model.visitType);
            self.model.storeList.push(self.model.store);
            
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                    self.model.storeList = self.model.storeList.concat(response.data.store);
                    // self.model.wardList = self.model.wardList.concat(response.data.ward);
                    initialCallForConsumable();
                    $rootScope.stopLoader();
                });
        }

        var initialCallForMedication = function () {
//            if (!screenDataService.getScreenData()) {
        	 if (angular.equals({},$state.params.obj)){
                GetRejectedRejectionMedication();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'pendingRejectedRejectionsMedication') {
                        var requestData = dataobj.searchrequest;
                        self.model.visitType = self.model.visitTypeList.find(function (visitType) {
                            if (requestData.visitType.id == visitType.id)
                                return visitType;
                        });
                        self.model.floor = self.model.floorList.find(function (floor) {
                            return requestData.floor.id == floor.id;
                        });

                        self.model.patientName = requestData.patientName
                        self.model.patientId = requestData.patientId
                        self.model.uhid = requestData.uhid
                        self.model.doctorName = requestData.doctorName
                        self.model.rejectedNo = requestData.rejectedNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetRejectedRejectionMedication();
            }
        }

        var initialCallForConsumable = function () {
//            if (!screenDataService.getScreenData()) {
        	 if (angular.equals({},$state.params.obj)){
                GetConsumableRejectedRejection();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'pendingRejectedRejectionsConsumable') {
                        var requestData = dataobj.searchrequest;
                        self.model.visitType = self.model.visitTypeList.find(function (visitType) {
                            if (requestData.visitType.id == visitType.id)
                                return visitType;
                        });
                        if( requestData.store.id){
                            self.model.store = self.model.storeList.find(function (store) {
                                return requestData.store.id == store.id;
                            });   
                        }
                        // self.model.ward = self.model.wardList.find(function (ward) {
                        //     return requestData.ward.id == ward.id;
                        // });
                        self.model.patientName = requestData.patientName
                        self.model.patientId = requestData.patientId
                        self.model.uhid = requestData.uhid
                        self.model.patientName = requestData.patientName
                        self.model.doctorName = requestData.doctorName
                        self.model.bedNo = requestData.bedNo
                        self.model.visitNo = requestData.visitNo
                        self.model.rejectedNo = requestData.rejectedNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetConsumableRejectedRejection();
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

        var GetRejectedRejectionMedication = function () {
            $rootScope.startLoader();
            self.model.medicationRejectedRejectionlist = [];
            self.model.medicationReturnlist = [];
            self.model.pagination.data = [];
            self.model.uhid = self.model.uhidFromService;
            self.model.doctorName = self.model.doctorNameFromService;
            self.model.patientName = self.model.patientNameFromService;
            var visitType = self.model.visitTypeList.find(function (data) {
                return self.model.visitTypeFromService == data.id;
              });
              self.model.visitType = visitType;
              var floor = self.model.floorList.find(function (data) {
                return self.model.floorIdFromService == data.id;
              });
              self.model.floor = floor;
            if (ValidateDate()) {
               
                var URI = CONSTANTS.PHARMACY.IP.PENDING_REJECTED_REJECTION.MEDICATION_SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": self.model.visitTypeFromService ? self.model.visitTypeFromService : self.model.visitType.id,
                  //  "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                   // "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                   // "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                   "patientId": self.model.patientIdFromService ?  self.model.patientIdFromService : self.model.patientId,
                   "patientName": !!self.model.patientNameFromService ? self.model.patientNameFromService : self.model.patientName,
                   // "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                   "uhid": self.model.uhidFromService ? self.model.uhidFromService: self.model.uhid,
                   // "doctorName": !!self.model.doctorName ? self.model.doctorName : undefined,
                   "doctorName": self.model.doctorNameFromService ? self.model.doctorName : self.model.doctorName,
                  //  "floor": !!self.model.floor && !!self.model.floor.id ? self.model.floor.id : undefined,
                  "floor":  self.model.floorIdFromService ? self.model.floorIdFromService : self.model.floor.id,
                    "rejectedNo": !!self.model.rejectNo ? self.model.rejectNo : undefined,
                    		"unitId":$scope.unitId
                };

                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.medicationRejectedRejectionlist = [];
                        self.model.medicationReturnlist = [];
                        angular.forEach(response.data, function(issueMedication){
                            if(issueMedication.requestFrom == 'Rejection'){
                                self.model.medicationRejectedRejectionlist.push(issueMedication);
                            }
                            else{
                                self.model.medicationReturnlist.push(issueMedication);
                            }
                        });
                        
                        self.model.pagination.data=[];
                        
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.medicationRejectedRejectionlist, self.model.pagination);
                        }
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
                        "patientId": self.model.patientId,
                        "patientName": self.model.patientName,
                        "uhid": self.model.uhid,
                        "doctorName": self.model.doctorName,
                        "floor": self.model.floor,
                        "rejectedNo": self.model.rejectedNo
                    },
                    screenName: 'pendingRejectedRejectionsMedication',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var GetConsumableRejectedRejection = function () {
            self.model.consumableRejectedRejectionlist = [];
            self.model.consumableReturnlist = [];
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
                var URI = CONSTANTS.PHARMACY.IP.PENDING_REJECTED_REJECTION.CONSUMABLE_SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
                    "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "rejectedNo": !!self.model.rejectNo ? self.model.rejectNo : undefined,
                    "unitId":$scope.unitId

                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.consumableRejectedRejectionlist = [];
                        self.model.consumableReturnlist = [];
                         angular.forEach(response.data, function(issueConsumable){
                            if(issueConsumable.requestFrom == 'Rejection'){
                            	issueConsumable.rejectedDate = moment(self.model.fromDate).format('DD/MM/YYYY');
                                self.model.consumableRejectedRejectionlist.push(issueConsumable);
                            }
                            else{
                                self.model.consumableReturnlist.push(issueConsumable);
                            }
                        });
                         
                         self.model.pagination.data=[];
                         
                         if(!!response && !!response.data && response.data.length > 0){            
                             PaginationService.searchData(self.model.consumableRejectedRejectionlist, self.model.pagination);
                           }
                    	 
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
                        "patientId": self.model.patientId,
                        "patientName": self.model.patientName,
                        "uhid": self.model.uhid,
                        // "ward": self.model.ward,
                        "bedNo": self.model.bedNo,
                        "visitNo": self.model.visitNumber,
                        "store": self.model.store,
                        "rejectedNo": self.model.rejectedNo
                    },
                    screenName: 'pendingRejectedRejectionsConsumable',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var NavigateToDetail = function (item, tab) {

            commonDetailService.setDataId(item.id);

            if (tab == 2) {
                $state.go('detailsPendingRejectedRejectionConsumable');
            } else {
                $state.go('detailsPendingRejectedRejectionMedication');
            }

        }

		$scope.patientDocumnetInformation = function(info) {
			$scope.patientDocumentFlipBook ={};
			$scope.patientDocumentFlipBook =info;
			
			
			
				var data =
				{
				"encounterId":$scope.patientDocumentFlipBook.encounterId,
				"patientId":$scope.patientDocumentFlipBook.patientId,
				"unitId":$scope.unitId,
				"organisationId":$scope.organizationId,
				"userId":$scope.userId
				}
				
				
			var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
			BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.patientDocumentListTest = [];
					if (response.data.status == "success") 
					{
						$scope.patientDocumentListTest = response.data.listObject;
						if($scope.patientDocumentListTest != null && $scope.patientDocumentListTest.length >0){
							$('#patientDocumentOption').modal('show');
						}
						$rootScope.stopLoader();
					}
					else if(response.data.status == "error")
						{
						$rootScope.stopLoader();
						}
				});
	
          };
          
          $scope.convertDateToTimestamp = function(dateString) {
                let parts = dateString.split("-");
                let day = parseInt(parts[0], 10);
                let month = parseInt(parts[1], 10) - 1; 
                let year = parseInt(parts[2], 10);
                let date = new Date(year, month, day);
                let timestampInMilliseconds = date.getTime();
                let timestampInSeconds = timestampInMilliseconds / 1000;
                return timestampInSeconds;
            };
            
	     		$scope.generateFlipbookData = function() {
	                  let flipbookDatalist = [];

		     			var filteredlist=[];
		     		    angular.forEach($scope.patientDocumentListTest,function(filterObj,index){
		     		    	
		     		    	if(filterObj.documentPath != null){
		     				var flipbookDataobj={};
		     				 let createdDate = filterObj.createdDate;
		     	            let timestamp = $scope.convertDateToTimestamp(createdDate);
		     				flipbookDataobj.timestamp=timestamp;
		     				flipbookDataobj.url=filterObj.documentPath;
		     				flipbookDataobj.tags=[];
		     				flipbookDataobj.tags.push("Document");
		     				flipbookDataobj.title=filterObj.documentName != null ? filterObj.documentName :"Lab Report" ; 
		     				flipbookDatalist.push(flipbookDataobj);
		     		    	}
		     				
		     			});

	                let data = {
	                    flipbookData: flipbookDatalist,
	                    logo: $scope.firstImgaa,
	                    privacy: "public"
	                };

	                let baseUrlLocation = $window.location.href;
	                let temp = baseUrlLocation.split('/');
	                let BASE_URL_CASESHEET = baseUrlLocation.replace(temp[temp.length - 1], "");

	                let generateFlipbookurl = "https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook";
	                
	        		try {
	        			var param={};
	        				$.ajax({
	        			        url : generateFlipbookurl,
	        					type : "POST",
	        					headers: {'X-Api-Key': "apikey_zHOw4R758ELytSeHFII99YibXj2sWHR6u9mxKdipN/g="},
	        					contentType : "application/json",
	        					dataType : "json",
	        					data : JSON.stringify(data),
	        			        //headers:getHeaders(),
	        					success : function(response){
	        						if(response.flipbookUrl!=undefined &&  response.flipbookUrl!=null  &&  response.flipbookUrl!="" ){
	        							  window.open(''+response.flipbookUrl, '_blank');
	        						}else{
	        							$.notify("Error occurred","error");
	        						}
	        			    	}
	        			});
	        	     } catch (e) {
	        	 		console.log(e );
	        		}
	            };
        
        
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("pendingRejectedRejections", {
            url: '/pendingRejectedRejections',
            templateUrl: 'views/PharmacyInventory/InPatient/pending-rejected-rejections.html',
            controller: "PendingRejectedRejections.Controller",
            title: 'Pending Rejected Rejection',
            params: {
                obj: {}
            },
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 108);
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
        .controller("PendingRejectedRejections.Controller", pendingRejectedRejectionsController)
})();