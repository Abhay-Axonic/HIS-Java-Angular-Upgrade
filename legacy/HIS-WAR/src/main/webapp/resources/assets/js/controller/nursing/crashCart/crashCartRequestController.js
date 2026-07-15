(function () {
    "use strict";

    function crashCartRequestController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, commonDetailService, StatusService, cancelReasonService, reportService ,PaginationService) {
        var self = this;
        var itemId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            var date = new Date();
            self.model = {
                fromDate: new Date(),
                toDate: new Date(),
                SetFirstTabLevel: 1,
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date(),
                    minDate : date.setDate((new Date()).getDate() - 90),
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date()
                },
                visitTypeList: [{
                    "type": "All"
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

            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            });

            StatusService.GetStatus().then(function (status) {
                self.model.statuses = status;
            });

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });

            //Methods
            self.model.OpenFromDate = OpenFromDate;
            self.model.OpenToDate = OpenToDate;
            self.model.SearchpatientNames = SearchpatientNames;
            self.model.SearchUhid = SearchUhid;
            self.model.AddPatientDetails = AddPatientDetails;
            self.model.SearchdoctorNames = SearchdoctorNames;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.GetCrashCartRequest = GetCrashCartRequest;
            self.model.Cancel = Cancel;
            self.model.CancelItem = CancelItem;
            self.model.isDisable = isDisable;
            self.model.NavigateToDetail = NavigateToDetail;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.printCrashCartRequestReport = printCrashCartRequestReport;
            PopulateDropdown();
            GetCrashCartRequest();
        }

        var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
        $scope.nurseRole = cookieObjectAppConfig.nurseRole;	
        $scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));

        var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
        $scope.nursingStationsWardsListId = nursingStationsWardsList;
			if(nursingStationsWardsList == undefined)
			{
				//start for nursing
				var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
				  GenericService.serviceAction("GET",URI,{}).then(function(response){
					  if (response.data.status == "success") {
						  $scope.departmentNursingStationMapperDtoslist = [];
						  
							$scope.departmentNursingStationMapperDtoslist = response.data.listObject;
							//alert($scope.roleName);
							if($scope.nurseRole == 1){
							if($scope.departmentNursingStationMapperDtoslist.length > 0)
								{
									if($scope.departmentNursingStationMapperDtoslist.length > 1){
										angular.element('#nursingStationModal').modal('show');
										//$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
									}
									else
										{
										var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
										  GenericService.serviceAction("GET",URI,{}).then(function(response){
											  if (response.data.status == "success") {
												  $scope.departmentNursingStationListC = {};
												 // console.log("fff",response.data.object);
												  $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
												  
												  sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
												  sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
												  sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
												  sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
												  //$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
												  sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
												  $scope.wardListForGetBedList = response.data.object;
											  }
										 });
										}
								}
					  		}
						}
				 });
			}else{
				$scope.wardListForGetBedList = nursingStationsWardsList;
			}

        $scope.isWardChanged = function()
        {      
            //start for nursing
            var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
            GenericService.serviceAction("GET",URI,{}).then(function(response){
                if (response.data.status == "success") {
                    $scope.departmentNursingStationMapperDtoslist = [];			  
                    $scope.departmentNursingStationMapperDtoslist = response.data.listObject;
                    //alert($scope.roleName);
                    if($scope.nurseRole == 1){
                        if($scope.departmentNursingStationMapperDtoslist.length > 0)
                        {
                            if($scope.departmentNursingStationMapperDtoslist.length > 1){
                                angular.element('#nursingStationModal').modal('show');
                                //$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
                            }
                            else
                            {
                                var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
                                GenericService.serviceAction("GET",URI,{}).then(function(response){
                                    if (response.data.status == "success") {
                                        $scope.departmentNursingStationListC = {};
                                        // console.log("fff",response.data.object);
                                        $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;					  
                                        sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
                                        sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
                                        sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
                                        sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
                                        //$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
                                        sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
                                        $scope.wardListForGetBedList = response.data.object;
                                    }
                                });
                            }
                        }
                    }
                }
            });        
        }

        $scope.departmentNursing = {};
        $scope.selectsetNursingStation = function()
        {
            if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
                growl.warning('Please select nursing station..!!');
                return false;
            }else{
            $scope.wardListForGetBedList=[];
            var data = JSON.parse($scope.departmentNursing);
            //console.log("$scope.departmentNursing",$scope.departmentNursing);
            //console.log("$scope.departmentNursing ID",data.nursingStationId);
            var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+data.nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
                GenericService.serviceAction("GET",URI,{}).then(function(response){
                    if (response.data.status == "success") {
                        $scope.departmentNursingStationListC = {};
                        //console.log('nursingStationsWardsList',response.data.object);
                        $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
                        sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
                        sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
                        sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
                        sessionStorage.setItem('nursingStationDesc', JSON.stringify(data.nursingStationDesc));
                        sessionStorage.setItem('nursingStationId', JSON.stringify(data.nursingStationId));
                        $scope.wardListForGetBedList = response.data.object;
                        self.model.ward.id = $scope.wardListForGetBedList;
                        $scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
                        GetCrashCartRequest();
                        angular.element('#nursingStationModal').modal('hide');
                    }
                });
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

        var PopulateDropdown = function () {

            var URI = CONSTANTS.GLOBAL.PATIENT_ISSUE_DROPDOWN_API + "/"+$scope.organizationId +"/"+ $scope.unitId+"/"+$scope.userId;

            self.model.visitType = self.model.visitTypeList[0];
            self.model.visitNumber = self.model.visitDetails[0];
            self.model.store = self.model.storeList[0];
            self.model.ward = self.model.wardList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                    self.model.storeList = self.model.storeList.concat(response.data.store);
                    self.model.wardList = self.model.wardList.concat(response.data.ward);
                    $rootScope.stopLoader();
                });
        }
        var ChangeVisitType = function () {
            self.model.patientName = null;
            self.model.uhid = null;
            self.model.patientNames = [];
            ClearPatientInfo();
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

                if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3) && (self.model.visitType.id)) {
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
                    if(!self.model.visitType.id){
                        growl.error('Please select visit type for fetching the patient name');
                    }
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

            if (!!item.patientId && !!self.model.visitType.id) {
                var URI = CONSTANTS.GLOBAL.PATIENT_DETAILS_SEARCH_API +'/' + $scope.organizationId + '/'+$scope.unitId;

                var data = {
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!item.patientId ? item.patientId : undefined,
                    // "uhid": !!item.uhid ? item.uhid : undefined,
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data)
                    .then(function (response) {

                        self.model.patientName = response.data.patientName,
                            self.model.patientId = response.data.patientId,
                            self.model.uhid = response.data.uhid,
                            self.model.bedNo = response.data.bedNo,
                            self.model.bedId = response.data.bedId,
                            self.model.encounterId = response.data.encounterId,
                            self.model.admissionId = response.data.admissionId

                        if (self.model.visitType.id == 1) {
//                            self.model.visitDetails = self.model.visitDetails.concat(response.data.visitDetails);
                        	var date = new Date();
							$scope.dt = moment(date).format('YYYY-MM-DD');
							angular.forEach(response.data.visitDetails,function(value,key){
                                
								if(moment(value.encounterDate).format('YYYY-MM-DD') ==  $scope.dt) {
								self.model.visitDetails = self.model.visitDetails.concat(value);
								self.model.visitNumber = self.model.visitDetails[1];
								self.model.doctorId = value.doctorId;
								
								}
							})
                        	
                        } else if (self.model.visitType.id != 1) {
                            self.model.visitNumberText = response.data.visitDetails[0].visitNo;
                            
                        }
                        // self.model.visitDetails = self.model.visitDetails.concat(response.data.visitDetails);
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

        var SearchUhid = function (search) {

            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1) && (self.model.patientNames.length > 0)) {
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
                if(self.model.patientNames.length == 0){
                    $rootScope.stopLoader();
                    growl.error('Please select patient name for fetching UHID');
                }
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

        var GetCrashCartRequest = function () {
            var URI = CONSTANTS.PHARMACY.NURSING.CRASH_CART.SEARCH_API+"/"+$scope.unitId;
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

            if(self.model.ward.id == undefined){
                self.model.ward.id = $scope.nursingStationsWardsListId;
            }

            var data = {
                "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
                "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                "wardList": !!self.model.ward.id ? self.model.ward.id : undefined,
                "crashCartNo": !!self.model.crashCartNo ? self.model.crashCartNo : undefined,
                "unitId":$scope.unitId
            };

            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                	
                	 self.model.pagination.data=[];
                	 
                	 self.model.crashCartRequestlist = response.data;
                	 
                	 if(!!response && !!response.data && response.data.length > 0){            
                         PaginationService.searchData(self.model.crashCartRequestlist, self.model.pagination);
                       }
                	 
                    
                    $rootScope.stopLoader();
                })
           
        }

        var Cancel = function (action, popupName, id) {
            itemId = id;
            self.model.action = action;
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        var SetStatus = function (x) {
            var status = self.model.statuses.find(function (obj) {
                return obj.status == x;
            });
        }
        var ValidateCancelation = function () {
            var isValid = true;
            if (!self.model.cancelReason) {
                isValid = false;
                growl.error('Please Select Reason to Cancel');
            }
            return isValid;
        }

        var CancelItem = function () {
            if (self.model.action == 'Cancel') {
                if (ValidateCancelation()) {

                    var URI = CONSTANTS.PHARMACY.NURSING.CRASH_CART.CANCEL_API + $scope.unitId + "/" + itemId;

                    var data = {
                        "cancelReasonId": self.model.cancelReason,
                        "cancelNote": self.model.cancelNote,
                        "statusId": 9
                    };

                    GenericService.serviceAction("PATCH", URI, data).then(
                        function (response) {
                            $rootScope.stopLoader();
                            growl.success(response.data.message);
                            GetCrashCartRequest();
                            ModalDismiss('confirmCancel');
                        },
                        function (err) {
                            $rootScope.stopLoader();
                            growl.error(err.data.msg);
                        });
                }
            }
        }
        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
          }
      
        var isDisable = function (Item) {
            if (Item.status == 'Cancelled' || Item.status == 'Approved'  ) {
                return true;
            }
            return false;
        }

        var NavigateToDetail = function (item) {
            commonDetailService.setDataId(item.id);

            $state.go('detailsCrashCartRequest');

        }
//Chetana
        var printCrashCartRequestReport=function(requestItem){
        	
  	      var data = {
  	        "appName": "HIS",
  	        "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
  	        "reportDesignFilePath": "get_Crash_Cart_Request.rpt",
  	        "reportFormat": "PDF",
  	        "exportFileName": "CrashCartRequest",
  	        "reportParams": [
  	          {
  	            "paramName": "id",
  	            "paramType": "Integer",
  	            "paramValue": requestItem.id
  	          }
  	        ]
  	      };
  	      reportService.generate(REPORT_API_URI1, data);
      }

 //------//

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("crashCartRequest", {
            url: '/crashCartRequest',
            templateUrl: 'views/PharmacyInventory/InPatient/crash-cart-request.html',
            controller: "CrashCartRequest.Controller",
            title: 'Crash Cart Request',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 86);
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
        .controller("CrashCartRequest.Controller", crashCartRequestController)
})();