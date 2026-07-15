(function () {
    "use strict";

    function patientConsumableIssueController($scope, $state, $rootScope, $http, $cookies, CONSTANTS, GenericService, PaginationService, $filter, growl, CurrencyService, commonDetailService, StatusService, cancelReasonService, screenDataService, pharamcyContractService, detailService, reportService) {
        var self = this;
        var itemId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                fromDate: new Date(),
                toDate: new Date(),
                activeTab: null,
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
                statusList: [{
                    "status": "All"
                }],
                storeList: [{
                    "store": "All"
                }],
                fromstoreList: [{
                    "store": "All"
                  }],
                visitDetails: [{
                    "visitNo": "Select Visit No"
                }],
                visitTypeList: [{
                    "type": "All"
                }],
                wardList: [{
                    "wardName": "All"
                }],
                SelectedRequestIds: [],
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
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.SearchpatientNames = SearchpatientNames;
            self.model.SearchUhid = SearchUhid;
            self.model.AddPatientDetails = AddPatientDetails;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.GetPatientConsumablePendingIndent = GetPatientConsumablePendingIndent;
            self.model.GetPatientConsumableIssueList = GetPatientConsumableIssueList;
            self.model.DetailsPatientPendingIndent = DetailsPatientPendingIndent;
            self.model.DetailsPatientIssueList = DetailsPatientIssueList;
            self.model.ChangeVisitNumber = ChangeVisitNumber;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.SetActiveTab = SetActiveTab;
            self.model.printPatientConsumableIssueReport = printPatientConsumableIssueReport;
            self.model.printIssueListReport = printIssueListReport;
            self.model.printPatientConsumableIssueReport = printPatientConsumableIssueReport;
            self.model.activeTab=1;
            PopulateDropdown();
            // GetPatientConsumablePendingIndent();
            // GetPatientConsumableIssueList();
            
            self.model.DetailsPatientPendingOT = DetailsPatientPendingOT;

        }
        
        var PopulateDropdown = function () {
            self.model.medicationIssueAcceptancelist = [];
            var activetab = detailService.getActiveTab();
            if(activetab == undefined)
            {
              activetab=1;      
            }
            if (activetab ==1) {
                self.model.activeTab = activetab;
            }
            if (self.model.activeTab == 2) {
            	  self.model.activeTab = activetab;
                PopulateIssueListDropdown();
            } else {
                PopulatePendingIndentDropdown();
                self.model.activeTab = activetab ? activetab : 1;
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

        var ClearPatientInfo = function () {
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

        var ChangeVisitType = function () {
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

        var PopulatePendingIndentDropdown = function () {

            var URI = CONSTANTS.PHARMACY.IN_PATIENT_PHARMACY.PATIENT_CONSUMABLE_ISSUE.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            self.model.visitType = self.model.visitTypeList[0];
            self.model.store = self.model.storeList[0];
            self.model.fromstore = self.model.fromstoreList[0];
            self.model.status = self.model.statusList[0];
            self.model.ward = self.model.wardList[0];
            self.model.visitNumber = self.model.visitDetails[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                	 $rootScope.stopLoader();
                    self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                    self.model.visitTypeList =removeDumplicateValue(self.model.visitTypeList);
                    self.model.storeList = self.model.storeList.concat(response.data.toStore);
                    self.model.storeList =removeDumplicateValue(self.model.storeList);
                    self.model.fromstoreList = self.model.fromstoreList.concat(response.data.fromStore);
                    self.model.fromstoreList =removeDumplicateValue(self.model.fromstoreList);
                    self.model.statusList = self.model.statusList.concat(response.data.status);
                    self.model.statusList =removeDumplicateValue(self.model.statusList);
                    self.model.wardList = self.model.wardList.concat(response.data.ward);
                    self.model.wardList =removeDumplicateValue(self.model.wardList);
                    function removeDumplicateValue(myArray){ 
                        var newArray = [];
                     
                        angular.forEach(myArray, function(value, key) {
                          var exists = false;
                          angular.forEach(newArray, function(val2, key) {
                        	  if(val2 !=undefined)
                         	 {
                         	   if(val2.id !=undefined && value!=undefined)
                               {
                                  if(angular.equals(value.id, val2.id)){ exists = true };       
                               }       
                         	 }
                          });
                          if(exists == false && value !=undefined) { newArray.push(value); }
                        });
                      
                        return newArray;
                      }
                     
                    initialCallForPendingIndent();
                });
        }

        var PopulateIssueListDropdown = function () {

            var URI = CONSTANTS.PHARMACY.IN_PATIENT_PHARMACY.PATIENT_CONSUMABLE_ISSUE.ISSUE_DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            self.model.visitType = self.model.visitTypeList[0];
            self.model.store = self.model.storeList[0];
            self.model.status = self.model.statusList[0];
            self.model.store = self.model.storeList[0];
            self.model.fromstore = self.model.fromstoreList[0];
            // self.model.ward = self.model.wardList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                	 $rootScope.stopLoader();
                    self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                    self.model.visitTypeList = removeDumplicateValue(self.model.visitTypeList);
                    self.model.storeList = self.model.storeList.concat(response.data.store);
                    self.model.storeList = removeDumplicateValue(self.model.storeList);
                    self.model.statusList = self.model.statusList.concat(response.data.status);
                    self.model.statusList = removeDumplicateValue(self.model.statusList);
                    self.model.storeList = self.model.storeList.concat(response.data.toStore);
                    self.model.storeList = removeDumplicateValue(self.model.storeList);
                    self.model.fromstoreList = self.model.fromstoreList.concat(response.data.fromStore);
                    self.model.fromstoreList = removeDumplicateValue(self.model.fromstoreList);
                    // self.model.wardList = self.model.wardList.concat(response.data.ward);
                    function removeDumplicateValue(myArray){ 
                        var newArray = [];
                     
                        angular.forEach(myArray, function(value, key) {
                          var exists = false;
                          angular.forEach(newArray, function(val2, key) {
                        	  if(val2 !=undefined)
                         	 {
                         	   if(val2.id !=undefined && value!=undefined)
                               {
                                  if(angular.equals(value.id, val2.id)){ exists = true };       
                               }       
                         	 }
                          });
                          if(exists == false && value !=undefined) { newArray.push(value); }
                        });
                      
                        return newArray;
                      }
                    initialCallForIssueList();
                });
        }

        var initialCallForPendingIndent = function () {
            if (!screenDataService.getScreenData()) {
                GetPatientConsumablePendingIndent();

            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'patientConsumablePendingIndent') {
                        var requestData = dataobj.searchrequest;
                        self.model.visitType = self.model.visitTypeList.find(function (visitType) {
                            if (requestData.visitType.id == visitType.id)
                                return visitType;
                        });
                        self.model.ward = self.model.wardList.find(function (ward) {
                            return requestData.ward.id == ward.id;
                        });
                        self.model.store = self.model.storeList.find(function (store) {
                        	 if( requestData.store!=undefined)
                             {
                             return requestData.store.id == store.id;        
                             }
                        });
                        self.model.status = self.model.statusList.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        self.model.patientName = requestData.patientName
                        self.model.patientId = requestData.patientId
                        self.model.uhid = requestData.uhid
                        self.model.indentNo = requestData.indentNo
                        self.model.bedNo = requestData.bedNo
                        self.model.visitNo = requestData.visitNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetPatientConsumablePendingIndent();
            }
        }

        var initialCallForIssueList = function () {
            if (!screenDataService.getScreenData()) {
                GetPatientConsumableIssueList();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'patientConsumableIssueList') {
                        var requestData = dataobj.searchrequest;
                        self.model.visitType = self.model.visitTypeList.find(function (visitType) {
                            if (requestData.visitType.id == visitType.id)
                                return visitType;
                        });
                        self.model.store = self.model.storeList.find(function (store) {
                            return requestData.store.id == store.id;
                        });
                        self.model.ward = self.model.wardList.find(function (ward) {
                            return requestData.ward.id == ward.id;
                        });
                        self.model.patientName = requestData.patientName
                        self.model.patientId = requestData.patientId
                        self.model.uhid = requestData.uhid
                        self.model.bedNo = requestData.bedNo
                        self.model.issueNo = requestData.issueNo
                        self.model.visitNo = requestData.visitNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetPatientConsumableIssueList();
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

        var GetPatientConsumablePendingIndent = function () {
            if (ValidateDate()) {
                var visitNumber = '';
                if (self.model.visitType) {
                    if (self.model.visitType.id == 1) {
                        visitNumber = self.model.visitNumber.visitNo == "Select Visit No" ? null : self.model.visitNumber.visitNo;
                    } else {
                        visitNumber = self.model.visitNumberText;
                    }
                }
                var URI = CONSTANTS.PHARMACY.IN_PATIENT_PHARMACY.PATIENT_CONSUMABLE_ISSUE.SEARCH_API;
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "indentNo": !!self.model.indentNo ? self.model.indentNo : undefined,
                    "wardId": !!self.model.ward && !!self.model.ward.id ? self.model.ward.id : undefined,
                    "bedNo": !!self.model.bedNo ? self.model.bedNo : undefined,
                    "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
                    "fromStoreId": !!self.model.fromstore && !!self.model.fromstore.id ? self.model.fromstore.id : undefined,
                    "toStoreId":  !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    "unitId":$scope.unitId,
                    "userId":$scope.userId
                };
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                    	if (!!response && !!response.data && response.data.length > 0) {
                            self.model.patientConsumablePendingIndentList = response.data;
                        } else {
                            self.model.patientConsumablePendingIndentList = [];
                        }
                        PaginationService.searchData(self.model.patientConsumablePendingIndentList, self.model.pagination);
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
                        "bedNo": self.model.bedNo,
                        "ward": self.model.ward,
                        "indentNo": self.model.indentNo,
                        "visitNo": self.model.visitNo,
                        "store": self.model.store,
                        "status": self.model.status
                    },
                    screenName: 'patientConsumablePendingIndent',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var GetPatientConsumableIssueList = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.PHARMACY.IN_PATIENT_PHARMACY.PATIENT_CONSUMABLE_ISSUE.ISSUE_SEARCH_API;
                var visitNumber = '';
                if (self.model.visitType) {
                    if (self.model.visitType.id == 1) {
                        visitNumber = self.model.visitNumber.visitNo;
                    } else {
                        visitNumber = self.model.visitNumberText;
                    }
                }
                if (visitNumber == "Select Visit No") {
                    visitNumber = undefined
                }

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "wardId": !!self.model.ward && !!self.model.ward.id ? self.model.ward.id : undefined,
                    "bedNo": !!self.model.bedNo ? self.model.bedNo : undefined,
                     "issueNo": !!self.model.issueNo  ? self.model.issueNo : undefined,
                    "storeId": !!self.model.fromstore && !!self.model.fromstore.id ? self.model.fromstore.id : undefined,
                    "toStoreId":  !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
                    // "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    		 "unitId":$scope.unitId

                };
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.patientConsumableIssueList = response.data;
                        if (!!response && !!response.data && response.data.length > 0) {
                            PaginationService.searchData(self.model.patientConsumableIssueList, self.model.pagination);
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
                        "ward": self.model.ward,
                        "bed": self.model.bed,
                        "issueNo": self.model.issueNo,
                        "store": self.model.store,
                        "visitNo": self.model.visitNo
                    },
                    screenName: 'patientConsumableIssueList',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var DetailsPatientPendingIndent = function (item) {
            if(item.isFinalBill=='Y')
            {
             growl.warning("The final bill has been generated for the selected patient; therefore, patient consumable issue is not possible.");
             return false;
            }
            commonDetailService.setDataId(item.id);
            pharamcyContractService.billObj = item;
            $state.go('detailsPatientPendingIndent');
        }

        var DetailsPatientIssueList = function (item) {
            commonDetailService.setDataId(item.id);
            $state.go('detailsPatientIssueList');
        }
        
       /* start code for resolved HIS-10997  Issued Indent not showing in issued list */
        var DetailsPatientPendingOT = function (item) {
        	self.model.patientPendingOTDetail=undefined;
        	let itemId = item.id;
            //self.model.carrier = self.model.carrierList[0];
            var URI = CONSTANTS.PHARMACY.IN_PATIENT_PHARMACY.PATIENT_CONSUMABLE_ISSUE.DETAIL_API + $scope.unitId + "/" + $scope.storeId + "/"+ itemId +"/1";
            $rootScope.startLoaderPopup();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                  
                    self.model.patientPendingOTDetail = response.data;
                    $scope.storeId = self.model.patientPendingOTDetail.storeId;
                    self.model.carrierList = response.data.carrier;
                    angular.forEach(self.model.patientPendingOTDetail.items, function (value, key) {
                    	
                    	// added by minesh for total of availableQuantity from
						// all batch and get single Unique Batch
                        var batchNo = [];
                        var uniqueBatches = [];
                    	
                    		if (!!value.batch) {
                    		value.batch.forEach(function (batch) {
                    			var index = batchNo.indexOf(batch.batchNo);                    			 
                            	if(index === -1)
                            	{
                            		batchNo.push(batch.batchNo);
                            		uniqueBatches.push(batch);
                            	}else{
                            			uniqueBatches[index].availableStockQuantity += batch.availableStockQuantity;
                    					//batch.availableStockQuantity = uniqueBatches[index].availableStockQuantity ;
                            	}
                    		});
                    		}                	
                    	value.batch = uniqueBatches;
                    	// added by minesh for total of availableQuantity from all  batch and get single Unique Batch
                    	
                        $scope.patientClassPercentage = value.patientClassPercentage;
                        value.batch.forEach(function(batch) {
                            if (!!value.markUpDownPer) {
                                var markUpDown = (batch.sellingPrice * (value.markUpDownPer / 100));

                                if (value.markUpDown == 1) {
                                    batch.sellingPrice = batch.sellingPrice + markUpDown;
                                } else if (value.markUpDown == 0) {
                                    batch.sellingPrice = batch.sellingPrice - markUpDown;
                                }
                            }
                        })
                        value['batchList'] = [value.batch[0]];
                        value['tempIssueQuantity'] = value.issueQuantity;
                    });
                    $rootScope.stopLoaderPopup();
                },
                function (err) {
                    $rootScope.stopLoader();
                    growl.error('Something Went Wrong');
                });
        }
        /* end code for resolved HIS-10997  Issued Indent not showing in issued list */
        

        var SetActiveTab = function (tab) {
            self.model.activeTab = tab;
            detailService.setActiveTab(tab);
        }
        
        /***Chetana**/
        var printPatientConsumableIssueReport = function (pendingIndent) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_Patient_Consumable_PendingIndent.rpt",
                "reportFormat": "PDF",
                "exportFileName": "PatientConsumableIssue",
                "reportParams": [
                    {
                        "paramName": "patientIndentid",
                        "paramType": "Integer",
                        "paramValue": pendingIndent.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
       /*****/ 
        /***Chetana**/
        var printIssueListReport = function (pendingIndent) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_PatientConsumableIssue.rpt",
                "reportFormat": "PDF",
                "exportFileName": "IssueList",
                "reportParams": [
                    {
                        "paramName": "issueid",
                        "paramType": "Integer",
                        "paramValue": pendingIndent.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
       /*****/ 
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("patientConsumableIssue", {
            url: '/patientConsumableIssue',
            templateUrl: 'views/PharmacyInventory/InPatient/patient-consumable-Issue.html',
            controller: "PatientConsumableIssue.Controller",
            title: 'Patient Consumable Issue',
            resolve: {

                data: function ($q, $state, $timeout) {
                    var deferred = $q.defer();

                    $timeout(function () {
                        var arr = JSON.parse(localStorage.getItem('permissions'))
                            .modules.filter(function (item) {
                                return (item.id == 120);
                            });
                        if (arr.length <= 0) {
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
        .controller("PatientConsumableIssue.Controller", patientConsumableIssueController)
})();