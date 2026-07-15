(function () {
    "use strict";

    function patientReturnAcceptanceController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService,  $filter, $state, growl, CurrencyService, commonDetailService,StatusService, screenDataService,pharamcyContractService,reportService, BillingGenericService, $window) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId;
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                fromDate: new Date(),
                toDate: new Date(),
                activeTab: 1,
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
                wardListMedication: [{
                    "wardName": "All"
                }],
                storeListConsumable: [{
                    "store": "All"
                }],
                visitDetails: [{
                    "visitNo": "Select Visit No"
                }],
                statusDataList: [{
                    "status": "All"
                }],
                visitTypeListMedication: [{
                    "type": "All"
                }],
                visitTypeListConsumable: [{
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
               StatusService.GetStatus().then(function (status) {
                 self.model.statuselect = status;
             });
            
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
            self.model.GetMedicationReturnAcceptance = GetMedicationReturnAcceptance;
            self.model.GetConsumableReturnAcceptance = GetConsumableReturnAcceptance;
            self.model.NavigateToDetail = NavigateToDetail;
            self.model.ChangeVisitNumber = ChangeVisitNumber;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.SetActiveTab = SetActiveTab;

            PopulateDropdown();
            // GetMedicationReturnAcceptance();
            // GetConsumableReturnAcceptance();
        }

        var SetActiveTab = function (tab) {
            self.model.activeTab = tab;
        }

        var PopulateDropdown = function () {
            if (self.model.activeTab == 2) {
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
                            self.model.uhid = response.data.uhid,
                            self.model.bedNo = response.data.bedNo,
                            self.model.bedId = response.data.bedId
                        // self.model.encounterId = response.data.encounterId,
                        // self.model.admissionId = response.data.admissionId

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

                        self.model.ward = self.model.wardList.find(function (ward) {
                            return response.data.wardId == ward.id;
                        });


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
            self.model.bedNo = null;
            self.model.bedId = null;
            self.model.ward = self.model.wardList[0];
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
            self.model.statusDataList= [{ "status": "All"}];

            var URI = CONSTANTS.PHARMACY.IP.PATIENT_RETURN_ACCEPTANCE.MEDICATION_DROPDOWN_API + '/' + $scope.unitId;

            self.model.visitType = self.model.visitTypeListMedication[0];
            self.model.ward = self.model.wardListMedication[0];
            self.model.status = self.model.statusDataList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.visitTypeList = self.model.visitTypeListMedication.concat(response.data.visitType);
                    self.model.wardList = self.model.wardListMedication.concat(response.data.ward);
                    self.model.statusDataList = self.model.statusDataList.concat(response.data.status);
                    initialCallForMedication();
                    $rootScope.stopLoader();
                });
        }

        var PopulateConsumableDropdown = function () {
           
            
            var URI = CONSTANTS.PHARMACY.IP.PATIENT_RETURN_ACCEPTANCE.CONSUMABLE_DROPDOWN_API + '/' + $scope.unitId;

            self.model.visitType = self.model.visitTypeListConsumable[0];
            self.model.visitNumber = self.model.visitDetails[0];
            self.model.store = self.model.storeListConsumable[0];
            self.model.status = self.model.statusDataList[0];
            // self.model.ward = self.model.wardList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.visitTypeList = self.model.visitTypeListConsumable.concat(response.data.visitType);
                    self.model.storeList = self.model.storeListConsumable.concat(response.data.store);
                    // self.model.statusDataList = self.model.statusDataList.concat(response.data.status);
                    // self.model.wardList = self.model.wardList.concat(response.data.ward);
                    initialCallForConsumable();
                    $rootScope.stopLoader();
                });
        }

        var initialCallForMedication = function () {
//            if (!screenDataService.getScreenData()) {
        	if (angular.equals({},$state.params.obj)){
                GetMedicationReturnAcceptance();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'patientReturnAcceptanceMedication') {
                        var requestData = dataobj.searchrequest;
                        self.model.visitType = self.model.visitTypeList.find(function (visitType) {
                            if (requestData.visitType.id == visitType.id)
                                return visitType;
                        });
                        self.model.ward = self.model.wardList.find(function (ward) {
                            return requestData.ward.id == ward.id;
                        });
                        self.model.patientName = requestData.patientName
                        self.model.patientId = requestData.patientId
                        self.model.uhid = requestData.uhid
                        self.model.doctorName = requestData.doctorName
                        self.model.returnNo = requestData.returnNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetMedicationReturnAcceptance();
            }
        }

        var initialCallForConsumable = function () {
//            if (!screenDataService.getScreenData()) {
        	if (angular.equals({},$state.params.obj)){
                GetConsumableReturnAcceptance();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'patientReturnAcceptanceConsumable') {
                        var requestData = dataobj.searchrequest;
                        self.model.visitType = self.model.visitTypeList.find(function (visitType) {
                            if (requestData.visitType.id == visitType.id)
                                return visitType;
                        });
                        self.model.store = self.model.storeList.find(function (store) {
                            return requestData.store.id == store.id;
                        });
                       /* self.model.status = self.model.statusDataList.find(function (status) {
                            return requestData.status.id == status.id;
                        });*/
                        // self.model.ward = self.model.wardList.find(function (ward) {
                        //     return requestData.ward.id == ward.id;
                        // });
                        self.model.patientName = requestData.patientName
                        self.model.patientId = requestData.patientId
                        self.model.uhid = requestData.uhid
                        self.model.doctorName = requestData.doctorName
                        self.model.visitNo = requestData.visitNo
                        self.model.returnNo = requestData.returnNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }

                });
                GetConsumableReturnAcceptance();
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

        var GetMedicationReturnAcceptance = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.PHARMACY.IP.PATIENT_RETURN_ACCEPTANCE.MEDICATION_SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "doctorName": !!self.model.doctorName ? self.model.doctorName : undefined,
                    "wardId": !!self.model.ward.id ? self.model.ward.id : undefined,
                    "returnNo": !!self.model.returnNo ? self.model.returnNo : undefined,
                    "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    "unitId":$scope.unitId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.pagination.data=[];
                        self.model.medicationReturnItemAcceptancelist = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.medicationReturnItemAcceptancelist, self.model.pagination);
                          }
                        self.model.patientId = undefined;
                        $rootScope.stopLoader();
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        self.model.patientId = undefined;
                        growl.error('Error');
                    });
                /*var screenData = {
                    searchrequest: {
                        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                        "visitType": self.model.visitType,
                        "patientId": self.model.patientId,
                        "patientName": self.model.patientName,
                        "uhid": self.model.uhid,
                        "doctorName": self.model.doctorName,
                        "ward": self.model.ward,
                        "returnNo": self.model.returnNo
                    },
                    screenName: 'patientReturnAcceptanceMedication',
                }
                screenDataService.setScreenData(screenData);*/
            }
        }
        /**************************shubhangi-status*************************************************/
        var isDisable = function (item) {
            if (item.status != 'New') {
                return true;
            }
            return false;
        }

        var isEdit = function (item) {
            if (item.status == 'Pending' || item.status == 'Completed') {
                return true;
            } else if (item.status == 'New') {
                return false;
            } else if (item.status == 'Closed' || item.status == 'Cancelled') {
                return true;
            }
        }

        var isCancel = function (item) {
            if (item.status == 'Pending' || item.status == 'Completed') {
                return true;
            } else if (item.status == 'New') {
                return false;
            } else if (item.status == 'Closed' || item.status == 'Cancelled') {
                return true;
            }
        }

        var isSelected = function (indent) {
            if (!isDisable(indent))
                return indent.isSelected;
            else
                return true;
        }
        var SetStatus = function (x) {
            var status = self.model.statuselect.find(function (obj) {
                return obj.status == x;
            });

            return status.id;
        }
        var ApproveItem = function () {
            if (self.model.action == 'Update') {
                var URI = CONSTANTS.PHARMACY.IP.PATIENT_INDENT.APPROVE_API;

                var data = {
                    "indentId": self.model.SelectedRequestIds,
                    "statusId": SetStatus('Pending'),
                    "approvalStatusId": SetStatus('Pending Approval'),
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        self.model.selectAllRows = false;
                        self.model.isItemChecked = false;
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        GetPatientConsumableIndent();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        // growl.error(err.data.msg);
                    });
            } else if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {
                var URI = CONSTANTS.PHARMACY.IP.PATIENT_INDENT.CANCEL_API + itemId;

                var data = {
                    "cancelReasonId": self.model.cancelReason,
                    "cancelNote": self.model.cancelNote,
                    "statusId": SetStatus('Cancelled'),
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId
                };

                GenericService.serviceAction("PATCH", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        $rootScope.stopLoader();
                        growl.success(response.data.message);
                        ModalDismiss('confirmCancel');
                        GetPatientConsumableIndent();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        // growl.error(err.data.msg);
                    });
            }
        }

        }
        /************************************************************************************/
        var GetConsumableReturnAcceptance = function () {
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
                var URI = CONSTANTS.PHARMACY.IP.PATIENT_RETURN_ACCEPTANCE.CONSUMABLE_SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
                    "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "returnNo": !!self.model.returnNo ? self.model.returnNo : undefined,
                    "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    "unitId":$scope.unitId		
                    		
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.pagination.data=[];
                        self.model.consumableReturnAcceptancelist = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.consumableReturnAcceptancelist, self.model.pagination);
                          }
                        self.model.patientId = undefined;
                        $rootScope.stopLoader();
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        self.model.patientId = undefined;
                        growl.error('Error');
                    });
                /*var screenData = {
                    searchrequest: {
                        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                        "visitType": self.model.visitType,
                        "patientId": self.model.patientId,
                        "patientName": self.model.patientName,
                        "uhid": self.model.uhid,
                        "visitNo": self.model.visitNumber,
                        "store": self.model.store,
                        "returnNo": self.model.returnNo
                    },
                    screenName: 'patientReturnAcceptanceConsumable',
                }
                screenDataService.setScreenData(screenData);*/
            }
        }

        var NavigateToDetail = function (item, tab) {
            commonDetailService.setDataId(item.id);
            pharamcyContractService.billObj=item;
            if (tab == 2) {
                $state.go('detailsPatientReturnAcceptanceConsumable');
            } else {
                $state.go('detailsPatientReturnAcceptanceMedication');
            }
        }
        $scope.printPatientReturnAcceptance=function(info){
        	var data = {
					"appName" : "HIS",
					"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
					"reportDesignFilePath" : "Patient_Return_Acceptance.rpt",
					"reportFormat" : "PDF",
					"exportFileName" : "PatientReturnAcceptance",
					"reportParams" : [ {
						"paramName" : "id",
						"paramType" : "Integer",
						"paramValue" : info.id
					} ]
				};
				reportService.generate(REPORT_API_URI1, data);
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
        $stateProvider.state("patientReturnAcceptance", {
            url: '/patientReturnAcceptance',
            templateUrl: 'views/PharmacyInventory/InPatient/patient-return-acceptance.html',
            controller: "PatientReturnAcceptance.Controller",
            title: 'Patient Return Acceptance',
            params: {
                obj: {}
            },
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 109);
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
        .controller("PatientReturnAcceptance.Controller", patientReturnAcceptanceController)
})();