(function () {
    "use strict";

    function patientIssueRejectionController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, $filter, $state, growl, CurrencyService, commonDetailService, screenDataService, detailService, BillingGenericService, $window) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                fromDate: new Date(),
                toDate: new Date(),
                fromDateMedication: new Date(),
                toDateMedication: new Date(),
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
                fromDateOptionsMedication: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date(),
                    minDate : moment(new Date()).subtract(180, 'days').toDate()
                    
                },
                toDateOptionsMedication: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                floorList: [{
                    "floor": "All"
                }],
                wardList: [{
                    "wardName": "All"
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
                SetFirstTabLevel: 1,
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
            self.model.OpenFromDateMedication = OpenFromDateMedication;
            self.model.OpenToDateMedication = OpenToDateMedication;
            self.model.SearchpatientNames = SearchpatientNames;
            self.model.SearchUhid = SearchUhid;
            self.model.AddPatientDetails = AddPatientDetails;
            self.model.SearchdoctorNames = SearchdoctorNames;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.GetIssueRejectionMedication = GetIssueRejectionMedication;
            self.model.GetConsumableIssueRejection = GetConsumableIssueRejection;
            self.model.NavigateToDetail = NavigateToDetail;
            self.model.ChangeVisitNumber = ChangeVisitNumber;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.SetActiveTab = SetActiveTab;

            PopulateDropdown();
        }

        var PopulateDropdown = function () {
            self.model.pagination.data = [];
            var activetab = detailService.getActiveTab();
            if(activetab) {
                self.model.activeTab = activetab;
            }
            if (self.model.activeTab == 2) {
                PopulateConsumableDropdown();
            } else {
                PopulateMedicationDropdown();
                self.model.activeTab = 1;
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

        var OpenFromDateMedication = function () {
            self.model.fromDateOpenedMedication = true;
            self.model.toDateOptionsMedication.minDate = self.model.fromDateMedication;
        }

        var OpenToDateMedication = function () {
            self.model.toDateOpenedMedication = true;
            self.model.toDateOptionsMedication.minDate = self.model.fromDateMedication;
        }

        var PopulateMedicationDropdown = function () {

            var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_REJECTION.MEDICATION_DROPDOWN_API + "/"+$scope.unitId;

            self.model.visitType = self.model.visitTypeList[0];
            self.model.floor = self.model.floorList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                	 self.model.visitTypeList = [];
                	 self.model.floorList = [];
                	 
                	 self.model.visitTypeList.push(self.model.visitType);
                	 self.model.floorList.push(self.model.floor);
                	 
                    self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                    self.model.floorList = self.model.floorList.concat(response.data.floor);
                    initialCallForMedication();
                });
        }

        var PopulateConsumableDropdown = function () {

            var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_REJECTION.CONSUMABLE_DROPDOWN_API + '/' + $scope.unitId;

            self.model.visitType = self.model.visitTypeList[0];
            self.model.visitNumber = self.model.visitDetails[0];
            self.model.store = self.model.storeList[0];
            self.model.ward = self.model.wardList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                	 self.model.visitTypeList = [];
                	 self.model.storeList = [];
                	 self.model.wardList = [];
                	 self.model.visitTypeList.push(self.model.visitType);
                	 self.model.storeList.push(self.model.store);
                	 self.model.wardList.push(self.model.ward);
                    
                	 self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                    self.model.storeList = self.model.storeList.concat(response.data.store);
                    self.model.wardList = self.model.wardList.concat(response.data.ward);
                    initialCallForConsumable();
                });
        }

        var SearchpatientNames = function (search) {
        	var data={
//	        		unitId : $scope.unitId,
	        		firstName : search,
//	        		lastName : search
	        };
            if (search.length < 3) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3)) {
                self.model.prevSearch = search;

//                var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search + "/" + self.model.visitType.id;
                var URI = BASE_URL + ROOT_URL + "/api/global/getPatientNameSearchByName";
                $rootScope.startLoader();
                return GenericService.serviceAction("POST", URI, data)
                    .then(function (response) {
						$rootScope.stopLoader();
						if (!!response.data.listObject && response.data.listObject.length > 0) {
							self.model.patientNames = response.data.listObject;
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

        var initialCallForMedication = function () {
//            if (!screenDataService.getScreenData()) {
        	 if (angular.equals({},$state.params.obj)){
                GetIssueRejectionMedication();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'patientIssueRejectionMedication') {
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
                        self.model.issueRejectNo = requestData.issueRejectNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);


                    }
                });
                GetIssueRejectionMedication();
            }
        }

        var initialCallForConsumable = function () {

//            if (!screenDataService.getScreenData()) {
        	 if (angular.equals({},$state.params.obj)){
                GetConsumableIssueRejection();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'patientIssueRejectionConsumable') {
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
                        self.model.doctorName = requestData.doctorName
                        self.model.bedNo = requestData.bedNo
                        self.model.visitNo = requestData.visitNo
                        self.model.issueRejectNo = requestData.issueRejectNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetConsumableIssueRejection();
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

            if (self.model.fromDateMedication > self.model.toDateMedication) {
                growl.error('From Date should lesser than To Date');
                isValid = false;
            }

            if (!self.model.fromDateMedication || !self.model.toDateMedication) {
                growl.error('Enter Valid Date');
                isValid = false;
            }


            return isValid;
        }

        var GetIssueRejectionMedication = function () {
            if (ValidateDate()) {
             
                var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_REJECTION.MEDICATION_SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDateMedication).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDateMedication).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "doctorName": !!self.model.doctorName ? self.model.doctorName : undefined,
                    "floor": !!self.model.floor && !!self.model.floor.id ? self.model.floor.id : undefined,
                    "issueRejectNo": !!self.model.issueRejectNo ? self.model.issueRejectNo : undefined,
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId	
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.pagination.data=[];
                        self.model.medicationIssueRejectionlist = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.medicationIssueRejectionlist, self.model.pagination);
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
                        "fromDate": moment(self.model.fromDateMedication).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDateMedication).format('YYYY-MM-DD'),
                        "visitType": self.model.visitType,
                        "patientId": self.model.patientId,
                        "patientName": self.model.patientName,
                        "uhid": self.model.uhid,
                        "doctorName": self.model.doctorName,
                        "floor": self.model.floor,
                        "issueRejectNo": self.model.issueRejectNo
                    },
                    screenName: 'patientIssueRejectionMedication',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var GetConsumableIssueRejection = function () {
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
                var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_REJECTION.CONSUMABLE_SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "wardId": !!self.model.ward && !!self.model.ward.id ? self.model.ward.id : undefined,
                    "bedNo": !!self.model.bedNo ? self.model.bedNo : undefined,
                    "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
                    "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "issueRejectNo": !!self.model.issueRejectNo ? self.model.issueRejectNo : undefined,
                    		"unitId":$scope.unitId
                    // "consignment": self.model.isConsignment
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.pagination.data=[];
                        self.model.consumableIssueRejectionlist = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.consumableIssueRejectionlist, self.model.pagination);
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
                        "bedNo": self.model.bedNo,
                        "visitNo": self.model.visitNumber,
                        "store": self.model.store,
                        "issueRejectNo": self.model.issueRejectNo
                    },
                    screenName: 'patientIssueRejectionConsumable',
                }
                $rootScope.stopLoader();
                screenDataService.setScreenData(screenData);
            }
        }

        var NavigateToDetail = function (item, tab) {
            commonDetailService.setDataId(item.id);

            if (tab == 2) {
                $state.go('detailsPatientIssueRejectionConsumable');
            } else {
                $state.go('detailsPatientIssueRejectionMedication');
            }
        }

        var SetActiveTab = function (tab) {
            self.model.activeTab = tab;
            detailService.setActiveTab(tab);
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
        $stateProvider.state("patientIssueRejection", {
            url: '/patientIssueRejection',
            templateUrl: 'views/PharmacyInventory/InPatient/patient-issue-rejection.html',
            controller: "PatientIssueRejection.Controller",
            title: 'Patient Issuance Rejection',
            params: {
                obj: {}
            },
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 107);
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
        .controller("PatientIssueRejection.Controller", patientIssueRejectionController)
})();