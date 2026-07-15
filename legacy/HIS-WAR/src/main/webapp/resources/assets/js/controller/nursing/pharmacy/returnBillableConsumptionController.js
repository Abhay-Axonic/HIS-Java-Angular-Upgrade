(function () {
    "use strict";

    function returnBillableConsumptionController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, $filter, $state, growl, CurrencyService, commonDetailService, screenDataService, reportService, BillingGenericService, $window) {
        var self = this;

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
            self.model.GetReturnBillableConsumption = GetReturnBillableConsumption;
            self.model.NavigateToDetail = NavigateToDetail;
            self.model.ChangeVisitNumber = ChangeVisitNumber;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.printReturnBillableConsumptionReport = printReturnBillableConsumptionReport;

            PopulateDropdown();
            // GetReturnBillableConsumption();
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
                        GetReturnBillableConsumption();
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

            var URI = CONSTANTS.PHARMACY.NURSING.RETURN_BILLABLE_CONSUMPTION.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;
                   
            self.model.visitNumber = self.model.visitDetails[0];
            self.model.visitType = self.model.visitTypeList[0];
            self.model.store = self.model.storeList[0];
            self.model.ward = self.model.wardList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                    self.model.storeList = self.model.storeList.concat(response.data.store);
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

                if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 3) && (self.model.visitType.id)) {
                    self.model.prevSearch = search;

                    var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API + $scope.unitId + "/" + search  + "/" +self.model.visitType.id;

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
//                                self.model.visitDetails = self.model.visitDetails.concat(response.data.visitDetails);

								var date = new Date();
								$scope.dt = moment(date).format('YYYY-MM-DD');
								angular.forEach(response.data.visitDetails,function(value,key){
	                                
									if(moment(value.encounterDate).format('YYYY-MM-DD') ==  $scope.dt) {
									self.model.visitDetails = self.model.visitDetails.concat(value);
									self.model.visitNumber = self.model.visitDetails[1];
									self.model.encounterId = value.encounterId;
									self.model.admissionId = value.admissionId;
									self.model.doctorId = value.doctorId;
									
									}
								})
									
							
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
            self.model.patientNames = [];
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
        
        var initialCall = function () {
            if (!screenDataService.getScreenData()) {
                GetReturnBillableConsumption()
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'returnBillableConsumption'){
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
                    self.model.consumptionNo = requestData.consumptionNo
                    self.model.visitNo = requestData.visitNo
                    self.model.fromDate = new Date(requestData.fromDate);
                    self.model.toDate = new Date(requestData.toDate);
                }
                });
                GetReturnBillableConsumption();
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

        var GetReturnBillableConsumption = function () {
        	self.model.ward.ids=[];
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

                if(self.model.ward.id == undefined){
                    self.model.ward.id = $scope.nursingStationsWardsListId;
                }
                if(self.model.ward.wardName!="All")
                {
                	if(self.model.ward.id[0]==undefined)
                		{
                		self.model.ward.ids.push(self.model.ward.id)
                        self.model.ward.id=self.model.ward.ids;
                		}else
                			{
                			self.model.ward.id=self.model.ward.id;
                			}
                }
                if(self.model.ward.wardName=="All")
                {
                    self.model.ward.id = $scope.nursingStationsWardsListId;
                }
            var URI = CONSTANTS.PHARMACY.NURSING.RETURN_BILLABLE_CONSUMPTION.SEARCH_API;
            
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
                "returnNo": !!self.model.returnNo ? self.model.returnNo : undefined,
                "unitId":$scope.unitId
            };

            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    self.model.pagination.data=[];
                    self.model.returnBillableConsumptionlist = response.data;
                    if(!!response && !!response.data && response.data.length > 0){            
                        PaginationService.searchData(self.model.returnBillableConsumptionlist, self.model.pagination);
                      }
                    self.model.patientId = undefined;
                    $rootScope.stopLoader();
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    self.model.patientId = undefined;
                    growl.error('Error');
                });
            // var screenData = {
            //     searchrequest: {
            //         "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
            //         "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
            //         "uhid": self.model.uhid,
            //         "visitType": self.model.visitType,
            //         "patientId": self.model.patientId,
            //         "patientName": self.model.patientName,
            //         "returnNo": self.model.returnNo,
            //         "ward": self.model.ward,
            //         "store": self.model.store,
            //         "visitNo": self.model.visitNumber
            //     },
            //     screenName: 'returnBillableConsumption',
            // }
            // screenDataService.setScreenData(screenData);
        }
    }

        var NavigateToDetail = function (item) {
            commonDetailService.setDataId(item.id);

            $state.go('detailsReturnBillableConsumption');

        }
        /***Chetana**/
        var printReturnBillableConsumptionReport = function (returnConsumptionItem) {
            var data = {
                "appName": "HIS",
                "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
                "reportDesignFilePath": "get_billableconsumption_return.rpt",
                "reportFormat": "PDF",
                "exportFileName": "ReturnBillableConsumption",
                "reportParams": [
                    {
                        "paramName": "id",
                        "paramType": "Integer",
                        "paramValue": returnConsumptionItem.id
                    }
                ]
            };
            reportService.generate(REPORT_API_URI1, data);
        }
       /*****/ 
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
        $stateProvider.state("returnBillableConsumption", {
            url: '/returnBillableConsumption',
            templateUrl: 'views/PharmacyInventory/InPatient/return-billable-consumption.html',
            controller: "ReturnBillableConsumption.Controller",
            title: 'Return Billable Consumption',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 61);
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
        .controller("ReturnBillableConsumption.Controller", returnBillableConsumptionController)
})();
