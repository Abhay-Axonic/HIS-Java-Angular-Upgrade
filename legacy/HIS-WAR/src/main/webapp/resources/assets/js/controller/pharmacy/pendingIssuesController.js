(function () {
    "use strict";

    function pendingIssuesController($scope, $cookies,$rootScope, $http, CONSTANTS, GenericService,PaginationService, $filter, $state, growl, CurrencyService, commonDetailService, screenDataService,pharamcyContractService,cancelReasonService,reportService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        
        $scope.cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
        $scope.storeName = $scope.cookieObjectStore.storeName;
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
                    "type": "All",
                    	"id": 0,
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
            self.model.SearchdoctorNames = SearchdoctorNames;
            self.model.AddUHID = AddUHID;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.GetPendingIssues = GetPendingIssues;
            self.model.NavigateToDetail = NavigateToDetail;
            self.model.isCancel = isCancel;
            self.model.cancelBillPopup = cancelBillPopup;
            self.model.ModalDismiss = ModalDismiss;
            self.model.cancelPendingIssue = cancelPendingIssue;
            PopulateDropdown();
        }

        $scope.popupStore = function () {
			var popup = angular.element('#storePopUp');
			popup.modal('show');
		}

        var OpenFromDate = function () {
            self.model.fromDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

        var OpenToDate = function () {
            self.model.toDateOpened = true;
            self.model.toDateOptions.minDate = self.model.fromDate;
        }

          $scope.validatesearch = function () {
             var isValid = true;
             if(self.model.visitType.id == 0){
                 growl.error("Please select visit type");
                 isValid = false;
             }
             return isValid;
         }

        var SearchpatientNames = function (search) {
            // if($scope.validatesearch()){
                if (search.length < 3) return;
                if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3)) {
                    self.model.prevSearch = search;
                    //var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search;
                    /* comment above and added below by Minesh */
                    var newVisitType =0;
                    if(self.model.visitType.id == 0){
                    	newVisitType = 2;
                    }else{
                    	newVisitType = self.model.visitType.id;
                    }
                    var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search + S +newVisitType;
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

        var AddUHID = function (item) {
            self.model.patientData = item;
            self.model.uhid = item.uhid;
            self.model.patientId = item.patientId;
            self.model.patientName = item.patientName;
        }

        var PatientNameNotFound = function (search) {
            var item = {
                "itemFound": false,
                "details": "Patient Not found with name " + search
            };
            self.model.patientNames = [item];
            return self.model.patientNames;
        }

        var PatientNameKeyPress = function (event) {
            var search = $filter('filter')(self.model.patientNames, {
                $: self.model.patientName
            });
            if (search.length != 1) {
                self.model.patientData = null;
                self.model.patientId = null;
                self.model.uhid = null;
            } else {
                self.model.patientData = search[0];
                self.model.patientId = search[0].patientId;
                self.model.uhid = search[0].uhid;
            }
        }

        var ChangeVisitType = function () {
            self.model.patientName = null;
            self.model.uhid = null;
        }

        var uHIDKeyPress = function (event) {
            var search = $filter('filter')(self.model.patientNames, {
                $: self.model.uhid
            });
            if (search.length != 1) {
                self.model.patientData = null;
                self.model.patientId = null;
                self.model.patientName = null;
            } else {
                self.model.patientData = search[0];
                self.model.patientId = search[0].patientId;
                self.model.patientName = search[0].patientName;
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
                            $rootScope.stopLoader();
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
                            return DoctorNotFound(search);
                        }
                    }, function (err) {
                        $rootScope.stopLoader();
                        return DoctorNotFound(search);
                    });
            } else {
                if (!!self.model.doctorNames && self.model.doctorNames.length > 0 &&
                    self.model.doctorNames[0].itemFound != undefined && !self.model.doctorNames[0].itemFound) {
                    return DoctorNotFound(search);
                } else {
                    return $filter('filter')(self.model.doctorNames, {
                        $: search
                    });
                }
            }
        }

        var DoctorNotFound = function (search) {
            var item = {
                "itemFound": false,
                "details": "DOCTOR Not found with name " + search
            };
            self.model.patientNames = [item];
            return self.model.patientNames;
        }

        var PopulateDropdown = function () {
            var URI = CONSTANTS.PHARMACY.IP.PENDING_ISSUES.DROPDOWN_API;
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
//        	 if (!screenDataService.getScreenData()) {
        	  if (angular.equals({},$state.params.obj)){
                GetPendingIssues()
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'pendingIssues') {
                        var requestData = dataobj.searchrequest;
                        self.model.visitType = self.model.visitTypeDataList.find(function (visitType) {
                            if (requestData.visitType.id == visitType.id)
                                return visitType;
                        });
                        self.model.status = self.model.statusDataList.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        self.model.patientName = requestData.patientName
                        self.model.patientId = requestData.patientId
                        self.model.doctorName = requestData.doctorName
                        self.model.wardName = requestData.wardName
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetPendingIssues();
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

        var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		
		 var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
		$scope.assignPickerRequired = cookieObjectAppConfig.assignPickerRequired; 
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

        $scope.PrintPendingIssuesDetailsReport=function(printId){
        	
  	      var data = {
  	        "appName": "HIS",
  	        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
  	        "reportDesignFilePath": "get_getpendingIssuesDetails.rpt",
  	        "reportFormat": "PDF",
  	        "exportFileName": "PendingIssuesDetails",
  	        "reportParams": [
  	          {
  	            "paramName": "id",
  	            "paramType": "Integer",
  	            "paramValue": printId
  	          }
  	        ]
  	      };
  	      reportService.generate(REPORT_API_URI1, data);
      }
        var GetPendingIssues = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.PHARMACY.IP.PENDING_ISSUES.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitTypeId" : $scope.visitTypeId,
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "doctorName": !!self.model.doctorName ? self.model.doctorName : undefined,
                    "wardName": !!self.model.wardName ? self.model.wardName : undefined,
                    "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                     "unitId":$scope.unitId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.pagination.data=[];
                        self.model.pendingIssueslist = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.pendingIssueslist, self.model.pagination);
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
                        "status": self.model.status,
                        "visitType": self.model.visitType,
                        "patientId": self.model.patientId,
                        "patientName": self.model.patientName,
                        "doctorName": self.model.doctorName,
                        "wardName": self.model.wardName
                    },
                    screenName: 'pendingIssues',
                }
                screenDataService.setScreenData(screenData);
            }
        }

        var NavigateToDetail = function (item) {
            commonDetailService.setDataId(item.id);
            pharamcyContractService.billObj=item;
            if(pharamcyContractService.billObj.isFinalDischarge==2)
            	{
            	growl.error("Final Bill Is Already Generated for This Patient.");
            	$state.go('pendingIssuesDetails');
            	}
            else
            	{
            	$state.go('pendingIssuesDetails');
            	}
        }

        var isCancel = function (item) {
            if (item.status == 'Completed' || item.status == 'Cancelled' ) {
                return true;
            } 
        }

        var cancelBillPopup = function (item) { 
            self.model.id = item.id;
            var popup = angular.element('#confirmCancel');
            popup.modal('show');
        }
        
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
        var cancelPendingIssue = function(){
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
                        GetPendingIssues();
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
        $stateProvider.state("pendingIssues", {
            url: "/pendingIssues",
            templateUrl: 'views/PharmacyInventory/InPatient/pending-issues.html',
            controller: "PendingIssues.Controller",
            title: 'Pending Issuance',
            params: {
                obj: {}
            },
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 105);
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
        .controller("PendingIssues.Controller", pendingIssuesController)
})();