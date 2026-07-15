(function () {
    "use strict";

    function narcoticsIssueWorklistController($scope,  $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, $filter, $state, growl, CurrencyService, commonDetailService, screenDataService, reportService, cancelReasonService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                fromDate: new Date(),
                toDate: new Date(),
                SetFirstTabLevel: 1,
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
                wardList: [{
                    "wardName": "All"
                }],
                statusList: [{
                    "status": "All"
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
            var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
            $scope.storeName = cookieObjectStore.storeName;
            if (cookieObjectStore == undefined) {
                $rootScope.storePopup();
                angular.element('#pharmacyPopupBeforLeftexpand').modal('show');
            }
       
    		
            //$scope.userAccess = true;
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
            self.model.AddPatientDetails = AddPatientDetails;
            self.model.SearchdoctorNames = SearchdoctorNames;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.GetNarcoticsIssueWorklist = GetNarcoticsIssueWorklist;
            self.model.NavigateToDetail = NavigateToDetail;
            self.model.ChangeVisitNumber = ChangeVisitNumber;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.cancelBillPopup = cancelBillPopup;
            self.model.cancelNarcoticsIssue = cancelNarcoticsIssue;
            self.model.isCancel = isCancel;
            self.model.ModalDismiss = ModalDismiss;
            // self.model.SetFirstTabLevel = SetFirstTabLevel;
            self.model.printNarcoticsIssueReport = printNarcoticsIssueReport;
            PopulateDropdown();
            // GetNarcoticsIssueWorklist();
        }

        var cancelBillPopup = function (item) { 
            self.model.id = item.id;
            var popup = angular.element('#confirmCancel');
            popup.modal('show');
        }
        
        var isCancel = function (item) {
            if (item.status == 'Completed' || item.status == 'Cancelled' ) {
                return true;
            } 
        }

        $scope.popupStore = function () {
			var popup = angular.element('#storePopUp');
			popup.modal('show');
		}
        
        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = moment(self.model.fromDate).format('YYYY-MM-DD HH:mm:ss');
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = moment(self.model.toDate).format('YYYY-MM-DD HH:mm:ss');
        }

        var PopulateDropdown = function () {

            var URI = CONSTANTS.PHARMACY.NARCOTICS.NARCOTICS_ISSUE_WORKLIST.DROPDOWN_API + '/' + $scope.unitId;

            self.model.visitType = self.model.visitTypeList[0];
            self.model.status = self.model.statusList[0];
            self.model.ward = self.model.wardList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                    self.model.statusList = self.model.statusList.concat(response.data.status);
                    self.model.wardList = self.model.wardList.concat(response.data.ward);
                    initialCall();
                    $rootScope.stopLoader();
                });
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

        var initialCall = function () {
            if (angular.equals({},$state.params.obj)){
                GetNarcoticsIssueWorklist();
            } 
            else{
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'narcoticsIssueWorklist') {
                        var requestData = dataobj.searchrequest;
                        self.model.visitType = self.model.visitTypeList.find(function (visitType) {
                            if (requestData.visitType.id == visitType.id)
                                return visitType;
                        });
                        self.model.status = self.model.statusList.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        self.model.ward = self.model.wardList.find(function (ward) {
                            return requestData.ward.id == ward.id;
                        });
                        self.model.patientName = requestData.patientName
                        self.model.patientId = requestData.patientId
                        self.model.uhid = requestData.uhid
                        self.model.doctorName = requestData.doctorName
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetNarcoticsIssueWorklist();
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

        var GetNarcoticsIssueWorklist = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.PHARMACY.NARCOTICS.NARCOTICS_ISSUE_WORKLIST.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "doctorName": !!self.model.doctorName ? self.model.doctorName : undefined,
                    "wardId": !!self.model.ward.id ? self.model.ward.id : undefined,
                    "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    "storeId":$cookies.getObject('cookieObject').storeId,
                    "unitId":$scope.unitId

                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.pagination.data=[];
                        self.model.narcoticsIssueWorklist = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.narcoticsIssueWorklist, self.model.pagination);
                          }
                        $rootScope.stopLoader();
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        growl.error('Error');
                    });
                var screenData = {
                    searchrequest: {
                        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                        "visitType": self.model.visitType,
                        "patientName": self.model.patientName,
                        "patientId": self.model.patientId,
                        "uhid": self.model.uhid,
                        "doctorName": self.model.doctorName,
                        "ward": self.model.ward,
                        "status": self.model.status,
                    },
                    screenName: 'narcoticsIssueWorklist',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var NavigateToDetail = function (item) {
            commonDetailService.setDataId(item.id);
            $state.go('detailsNarcoticsIssueWorklist');
        }
        //chetana
        var printNarcoticsIssueReport=function(item){
          	
  	      var data = {
  	        "appName": "HIS",
  	        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
  	        "reportDesignFilePath": "getIssue_Worklist.rpt",
  	        "reportFormat": "PDF",
  	        "exportFileName": "NarcoticIssuesDetails",
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
        
//Cancel code
        var ModalDismiss = function () {
            var popup = angular.element('#confirmCancel');
            popup.modal('hide');
        } 
        
        var ValidetReason = function () {
            var isValid = true;

            if (!self.model.cancelReason) {
                self.model.isReasonValid = true;
                isValid = false;
            }

            return isValid;
        }
        
        var cancelNarcoticsIssue = function(){
            if (ValidetReason()) {
            	   var URI = CONSTANTS.PHARMACY.IP.PENDING_ISSUES.CANCEL_PENDING_ISSUE_REQUEST;

                var data = {
                    "id": self.model.id,
                    "statusId": self.model.cancelReason ? parseInt(self.model.cancelReason) : undefined,
                    "cancelNote": self.model.rejectNote,
                    "isFromAssignpicker": $scope.assignPickerRequired 
                }

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.SelectedRequestIds = [];
                        ModalDismiss('confirmCancel');
                        self.model.rejectNote = undefined;
                        self.model.cancelReason = undefined;
//                        FixedAssetSearch();
                        GetNarcoticsIssueWorklist();
                        $rootScope.stopLoader();
                        if(response.data.status == "success"){
                            growl.success("Status Cancelled Successfully");    
                        }
                      
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error(err.data.message);
                    });
            }
        }
        
        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("narcoticsIssueWorklist", {
            url: '/narcoticsIssueWorklist',
            templateUrl: 'views/PharmacyInventory/NarcoticsStore/narcotics-issue-worklist.html',
            controller: "NarcoticsIssueWorklist.Controller",
            title: 'Issuance Worklist',
            params: {
                obj: {}
            },
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 116);
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
        .controller("NarcoticsIssueWorklist.Controller", narcoticsIssueWorklistController)
})();