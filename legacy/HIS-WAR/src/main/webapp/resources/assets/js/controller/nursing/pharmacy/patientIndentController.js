(function () {
    "use strict";

    function patientIndentController($scope,$cookies,$stateParams, $rootScope, $http,  CONSTANTS, GenericService,PaginationService, $filter, $state, growl, CurrencyService, commonDetailService, StatusService, cancelReasonService, screenDataService, reportService, BillingGenericService, $window) {
        var self = this;
        var itemId;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            var specialtyNursingStationCookies = JSON.parse(sessionStorage.getItem('specialtyNursingStationCookiesNur'));
			var nursingStationCookie = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
			 if(specialtyNursingStationCookies == undefined){
				 growl.warning(
							'No nursing station and specialty mapped with current user!!',
							{
								title : 'Success!'
							});
				 return false;
			 }
			 else
			  {
			   console.log('specialtyNursingStationCookies',specialtyNursingStationCookies);
			   console.log('nursingStationCookie',nursingStationCookie);
			   $scope.nursingStationDesc=nursingStationCookie;
			   $scope.departmentNursingStationMapperDtoslist = specialtyNursingStationCookies.departmentNursingStationlist;
				console.log("ggg",$scope.departmentNursingStationMapperDtoslist);
			  }
			 
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
                statusDataList: [{
                    "status": "All"
                }],
                floorList: [{
                    "floor": "All"
                }],
                wardList: [{
                    "wardName": "All"
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
                SelectedRequestIds: [],
               SetFirstTabLevel:2,
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
            self.model.SearchdoctorNames = SearchdoctorNames;
            self.model.SearchUhid = SearchUhid;
            self.model.AddPatientDetails = AddPatientDetails;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.GetPatientConsumableIndent = GetPatientConsumableIndent;
            self.model.PatientConsumableIndentDetails = PatientConsumableIndentDetails;
            self.model.EditPatientConsumableIndentDetails = EditPatientConsumableIndentDetails;
            self.model.OpenPopUp = OpenPopUp;
            self.model.isDisable = isDisable;
            self.model.SelectItem = SelectItem;
            self.model.SelectAllRequests = SelectAllRequests;
            self.model.ApproveItem = ApproveItem;
            self.model.isEdit = isEdit;
            self.model.isCancel = isCancel;
            self.model.ChangeVisitNumber = ChangeVisitNumber;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.SetActiveTab = SetActiveTab;
            self.model.print = print;

            if($stateParams.obj == true){
                self.model.activeTab = 2;
            }
            else{
                self.model.activeTab = 2;
            }

            PopulateDropdown();
            GetRejectReasonList();
            // GetPatientConsumableIndent();

            CurrencyService.GetCurrency().then(function (currency) {
                self.model.Currency = currency;
            })

            StatusService.GetStatus().then(function (status) {
                self.model.statuselect = status;
            });

            cancelReasonService.GetReason().then(function (reason) {
                self.model.reasons = reason;
            });
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
                        GetPatientConsumableIndent();
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
                    var URI = CONSTANTS.GLOBAL.PATIENT_SEARCH_API_PATIENT_INDENT + $scope.unitId +  "/" + search + "/" +self.model.visitType.id;
                    $rootScope.startLoader();
                    return GenericService.serviceAction("GET", URI, {})
                        .then(function (response) {
                            $rootScope.stopLoader();
                            if (!!response.data && response.data.length > 0) {
                                self.model.patientNames = response.data;
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

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1) && (self.model.visitType.id)) {
                self.model.prevSearch = search;

                var URI = CONSTANTS.GLOBAL.UHID_SEARCH_API_SALES_RETURN + search + "/" +self.model.visitType.id + "/"+ $scope.organizationId;

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

        var PopulateDropdown = function () {

            var URI = CONSTANTS.PHARMACY.IP.PATIENT_INDENT.DROPDOWN_API + '/' + $scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId;

            self.model.visitType = self.model.visitTypeList[0];
            self.model.status = self.model.statusDataList[0];
            self.model.floor = self.model.floorList[0];
            self.model.ward = self.model.wardList[0];
            self.model.store = self.model.storeList[0];
            self.model.fromstore = self.model.fromstoreList[0];


            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                    self.model.statusDataList = self.model.statusDataList.concat(response.data.status);
                    self.model.floorList = self.model.floorList.concat(response.data.floor);
                    self.model.wardList = self.model.wardList.concat(response.data.ward);
                    self.model.storeList = self.model.storeList.concat(response.data.toStore);
                    self.model.fromstoreList = self.model.fromstoreList.concat(response.data.fromStore);
                    initialCallForConsumable();
                    $rootScope.stopLoader();
                });
        }

        var initialCallForConsumable = function () {
            if (!screenDataService.getScreenData()) {
                GetPatientConsumableIndent();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'patientIndentConsumable') {
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
                        self.model.status = self.model.statusDataList.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        self.model.floor = self.model.floorList.find(function (floor) {
                            return requestData.floor.id == floor.id;
                        });
                        self.model.patientName = requestData.patientName
                        self.model.patientId = requestData.patientId
                        self.model.uhid = requestData.uhid
                        self.model.doctorName = requestData.doctorName
                        self.model.visitNo = requestData.visitNo
                        self.model.rejectedNo = requestData.rejectedNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetPatientConsumableIndent();
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

        var GetPatientConsumableIndent = function () {
            self.model.selectAllRows = false;
            self.model.SelectedRequestIds = [];
            self.model.ward.ids=[];
            self.model.isItemChecked = false;   
            $scope.doctorId = 0;
            
            if(self.model.doctorNames != undefined){
            	self.model.doctorNames.forEach(function (value) {
                if ( self.model.doctorName == value.doctorName) {
                    $scope.doctorId = value.id;
                }
            });
            
            }
            if (ValidateDate()) {
//                if(self.model.ward.id == undefined){
//                    self.model.ward.id = $scope.nursingStationsWardsListId;
//                }
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
                var URI = CONSTANTS.PHARMACY.IP.PATIENT_INDENT.SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "doctorId": $scope.doctorId == 0 ? undefined : $scope.doctorId  ,
                    "wardList": !!self.model.ward && !!self.model.ward.id ? self.model.ward.id : undefined,
                    "floor": !!self.model.floor && !!self.model.floor.id ? self.model.floor.id : undefined,
                    "visitNo": self.model.visitNo != undefined ? self.model.visitNo : (self.model.visitNumberText != undefined ? self.model.visitNumberText : undefined),
                    "fromStoreId": !!self.model.fromstore && !!self.model.fromstore.id ? self.model.fromstore.id : undefined,
                    "toStoreId":  !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    "unitId":$scope.unitId,
                    "userId":$scope.userId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.patientConsumableIndentlist = response.data;
//                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.patientConsumableIndentlist, self.model.pagination);
//                          }
                        self.model.patientId = undefined;
                        self.model.selectAllRows = false;
                        self.model.isItemChecked = false;
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
                //         "visitType": self.model.visitType,
                //         "patientId": self.model.patientId,
                //         "patientName": self.model.patientName,
                //         "uhid": self.model.uhid,
                //         "doctorName": self.model.doctorName,
                //         "ward": self.model.ward,
                //         "floor": self.model.floor,
                //         "visitNo": self.model.visitNo,
                //         "store": self.model.store,
                //         "status": self.model.status
                //     },
                //     screenName: 'patientIndentConsumable',
                // }
                // screenDataService.setScreenData(screenData);
            }
        }

        var PatientConsumableIndentDetails = function (item) {
            commonDetailService.setDataId(item.id);
            $state.go('deatilsPatientIndent');
        }

        var EditPatientConsumableIndentDetails = function (item) {
            commonDetailService.setDataId(item.id);
            $state.go('consumableIndent');
        }

        var OpenPopUp = function (action, popupName, id) {
            itemId = id;
            self.model.action = action;
            self.model.cancelNote = '';
            self.model.cancelReason='';
            var popup = angular.element('#' + popupName);
            popup.modal('show');
        }

        var GetRejectReasonList = function () {
            var URI = CONSTANTS.GLOBAL.CANCEL_REASON_API;
            var data = {};
            $rootScope.startLoader()
            GenericService.serviceAction("GET", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                    self.model.rejectReasonList = response.data;
                });
        }

        var SelectItem = function (Item) {

            var itemIds = self.model.SelectedRequestIds;

            if (Item.isSelected) {
                itemIds.push(Item.id);
            } else {
                var index = itemIds.indexOf(Item);
                if (index > -1) {
                    itemIds.splice(index, 1);
                }
            }

            if (self.model.patientConsumableIndentlist.every(isSelected)) {
                self.model.selectAllRows = true;
            } else {
                self.model.selectAllRows = false;
            }
            self.model.isItemChecked = false;
            self.model.patientConsumableIndentlist.forEach(function (item) {
              if (item.isSelected == true) {
                self.model.isItemChecked = true;
                return;
              }
            });
        }

        var SelectAllRequests = function () {
            if (self.model.selectAllRows) {
                self.model.SelectedRequestIds = [];
                self.model.patientConsumableIndentlist.forEach(function (indent) {
                    if (!isDisable(indent)) {
                        indent.isSelected = true;
                        self.model.isItemChecked = true;
                        self.model.SelectedRequestIds.push(indent.id);
                    }
                });
            } else {
                self.model.SelectedRequestIds = [];
                self.model.patientConsumableIndentlist.forEach(function (indent) {
                    indent.isSelected = false;
                    self.model.isItemChecked = false;
                });
            }
        }

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

        $scope.saveDisabled = true;
        var ApproveItem = function () {
        	$scope.saveDisabled = false;
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
                    	$scope.saveDisabled = true;
                        self.model.SelectedRequestIds = [];
                        self.model.selectAllRows = false;
                        self.model.isItemChecked = false;
                        $rootScope.stopLoader();
                        var popup = angular.element('#confirmLogout1');
                        popup.modal('hide');
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

        var ModalDismiss = function (popupName) {
            var popup = angular.element('#' + popupName);
            popup.modal('hide');
          }
        

	    var print=function(item)
	    {
	    //$scope.save();

	    var data = {
	    "appName": "HIS",
	    "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
	    "reportDesignFilePath": "get_patient_indent.rpt",
	    "reportFormat": "PDF",
	    "exportFileName": "Certificate",
	    "reportParams": [
	    {
	    "paramName": "id",
	    "paramType": "Integer",
	    "paramValue": item.id

	    }
	    ]
	    };
	    reportService.generate(REPORT_API_URI1,data);

	    }
	    
        var ValidateCancelation = function () {
            var isValid = true;
            if (!self.model.cancelReason) {
              isValid = false;
              growl.error('Please Select Reason to Cancel');
            }
            return isValid;
          }

        var SetActiveTab = function (tab) {
           self.model.activeTab = tab;
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
        $stateProvider.state("patientIndent", {
            url: '/patientIndent',
            templateUrl: 'views/PharmacyInventory/InPatient/patient-indent.html',
            controller: "PatientIndent.Controller",
            title: 'Consumable Indent',
            params: {
                obj: {}
            },
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 59);
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
        .controller("PatientIndent.Controller", patientIndentController)
})();