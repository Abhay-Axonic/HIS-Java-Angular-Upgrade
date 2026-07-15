(function () {
    "use strict";

    function patientIssueReturnController($scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, $filter, $state, growl, CurrencyService, detailService,StatusService, screenDataService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            var date = new Date();
            self.model = {
                fromDate: new Date(),
                toDate: new Date(),
                activeTab: 1,
                
                fromDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date(),
                    minDate : date.setDate((new Date()).getDate() - 90),
                },
                toDateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    maxDate: new Date(),
                    minDate : date.setDate((new Date()).getDate() - 90),
                },
                wardList: [{
                    "wardName": "All"
                }],
                visitDetails: [{
                    "visitNo": "Select Visit No"
                }],
                visitTypeList: [{
                    "type": "All"
                }],
                statusDataList: [{
                    "status": "All"
                }],
                visitTypeListConsume: [{
                    "type": "All"
                }],
                isConsignment: false,
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
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.GetMedicationIssueReturn = GetMedicationIssueReturn;
            self.model.GetConsumableIssueReturn = GetConsumableIssueReturn;
            self.model.NavigateToDetail = NavigateToDetail;
            self.model.ChangeVisitNumber = ChangeVisitNumber;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.SetActiveTab = SetActiveTab;

            PopulateDropdown();
             GetMedicationIssueReturn();
            // GetConsumableIssueReturn();
             
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
												  self.model.wardIdFromService = $scope.wardListForGetBedList;
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
        	self.model.wardIdFromService = [];
           
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
                        self.model.wardIdFromService = $scope.wardListForGetBedList;
                        PopulateDropdown();
                        angular.element('#nursingStationModal').modal('hide');
                    }
                });
            }
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

        // var PopulateDropdown = function () {

        //     var URI = CONSTANTS.GLOBAL.PATIENT_ISSUE_DROPDOWN_API + "/"+$scope.organizationId +"/"+ $scope.unitId+"/"+$scope.userId;
                   
        //     self.model.visitNumber = self.model.visitDetails[0];
        //     self.model.ward = self.model.wardList[0];

        //     $rootScope.startLoader();
        //     GenericService.serviceAction("GET", URI, {}).then(
        //         function (response) {
        //             self.model.visitTypeList = response.data.visitType;
        //             self.model.visitType = self.model.visitTypeList[0];
        //             self.model.wardList = self.model.wardList.concat(response.data.ward);
        //             $rootScope.stopLoader();
        //         });
        // }

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
                    .then(function (response) {/*
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
                    */

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

        var PopulateMedicationDropdown = function () {
        	self.model.wardList=[{  "wardName": "All" }];
            self.model.visitTypeList= [{ "type": "All"}];
            self.model.statusDataList= [{ "status": "All"}];

            var URI = CONSTANTS.GLOBAL.PATIENT_ISSUE_DROPDOWN_API + "/"+$scope.organizationId +"/"+ $scope.unitId+"/"+$scope.userId;

            self.model.visitType = self.model.visitTypeList[0];
            self.model.status = self.model.statusDataList[0];
            self.model.ward = self.model.wardList[0];
            $scope.visitList = [];
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
            function (response) {
                angular.forEach(response.data.visitType, function(visit){
                    if(visit.type != 'Out Patient'){
                        $scope.visitList.push(visit);
                    }
                });
                self.model.visitTypeList = self.model.visitTypeList.concat($scope.visitList);
                self.model.wardList = self.model.wardList.concat(response.data.ward);
                self.model.statusDataList = self.model.statusDataList.concat(response.data.status);
                initialCallForMedication();
                $rootScope.stopLoader();
            });
        }

        var PopulateConsumableDropdown = function () {
        	
        	self.model.wardList=[{  "wardName": "All" }];
            self.model.visitTypeListConsume= [{ "type": "All"}];
          

            var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.CONSUMABLE_DROPDOWN_API+ '/' + $scope.unitId;

            self.model.visitType = self.model.visitTypeListConsume[0];
            self.model.status = self.model.statusDataList[0];
            self.model.visitNumber = self.model.visitDetails[0];
            self.model.ward = self.model.wardList[0];
            $scope.visitListCons = [];
            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    angular.forEach(response.data.visitType, function(visit){
                        if(visit.type != 'Out Patient'){
                            $scope.visitListCons.push(visit);
                        }
                    });
                    self.model.visitTypeListConsume = self.model.visitTypeListConsume.concat($scope.visitListCons);
                    // self.model.statusDataList = self.model.statusDataList.concat(response.data.status);
                    self.model.wardList = self.model.wardList.concat(response.data.ward);
                    initialCallForConsumable();
                    $rootScope.stopLoader();
                });
        }

        var initialCallForMedication = function () {
            if (!screenDataService.getScreenData()) {
                GetMedicationIssueReturn();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'MedicationIssueReturn') {
                        var requestData = dataobj.searchrequest;
                        self.model.visitType = self.model.visitTypeList.find(function (visitType) {
                            if (requestData.visitType.id == visitType.id)
                                return visitType;
                        });
                        self.model.ward = self.model.wardList.find(function (ward) {
                            return requestData.ward.id == ward.id;
                        });
                        self.model.status = self.model.statusDataList.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        
                        self.model.patientName = requestData.patientName
                        self.model.patientId = requestData.patientId
                        self.model.uhid = requestData.uhid
                        self.model.returnNo = requestData.returnNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }
                });
                GetMedicationIssueReturn();
            }
        }

        var initialCallForConsumable = function () {
            if (!screenDataService.getScreenData()) {
                GetConsumableIssueReturn();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'ConsumableIssueReturn') {
                        var requestData = dataobj.searchrequest;
                        self.model.visitType = self.model.visitTypeListConsume.find(function (visitType) {
                            if (requestData.visitType.id == visitType.id)
                                return visitType;
                        });
                        self.model.status = self.model.statusDataList.find(function (status) {
                            return requestData.status.id == status.id;
                        });
                        self.model.ward = self.model.wardList.find(function (ward) {
                            return requestData.ward.id == ward.id;
                        });
                        self.model.patientName = requestData.patientName
                        self.model.patientId = requestData.patientId
                        self.model.uhid = requestData.uhid
                        self.model.bedNo = requestData.bedNo
                        self.model.visitNo = requestData.visitNo
                        self.model.returnNo = requestData.returnNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                    }

                });
                GetConsumableIssueReturn();
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

        var GetMedicationIssueReturn = function () {
            if (ValidateDate()) {
                var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.MEDICATION_SEARCH_API;
//                if(self.model.ward.id == undefined){
//                    self.model.ward.id = $scope.nursingStationsWardsListId;
//                }
                self.model.medicationIssueReturnlist = [];
                var ward = self.model.wardList.find(function (data) {
                	return self.model.wardIdFromService == data.id;
                	});
                if(self.model.ward.wardName!="All")
                {
                    self.model.wardIdFromService=[];
                self.model.wardIdFromService.push(self.model.ward.id);
                }
                if(self.model.ward.wardName=="All")
                {
                    self.model.wardIdFromService=[];
                    self.model.wardIdFromService = $scope.nursingStationsWardsListId;
                }


                	if(self.model.wardIdFromService == undefined){
                	self.model.wardIdFromService = $scope.nursingStationsWardsListId;
                	}
                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
//                    "wardList": !!self.model.ward.id ? self.model.ward.id : undefined,
                    "wardList": self.model.wardIdFromService ? self.model.wardIdFromService : self.model.ward.id,
                    "returnNo": !!self.model.returnNo ? self.model.returnNo : undefined,
                    "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    "unitId":$scope.unitId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {

                        self.model.medicationIssueReturnlist = [];
                        angular.forEach(response.data, function(item){
                            if(!item.fromMedicineIssues){
                                self.model.medicationIssueReturnlist.push(item);
                            }
                        });
//                        self.model.medicationIssueReturnlist = response.data;
                        self.model.patientId = undefined;
                        $rootScope.stopLoader();
                    },
                    function errorCallback(response) {
                        $rootScope.stopLoader();
                        self.model.patientId = undefined;
                        growl.error('Error');
                    });
               /* var screenData = {
                    searchrequest: {
                        "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                        "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                        "visitType": self.model.visitType,
                        "patientId": self.model.patientId,
                        "patientName": self.model.patientName,
                        "uhid": self.model.uhid,
                        "ward": self.model.ward,
                        "returnNo": self.model.returnNo
                    },
                    screenName: 'MedicationIssueReturn',
                }
                screenDataService.setScreenData(screenData);*/
            }
        }
        /**************************shubhangi-status*************************************************/
       /* var isDisable = function (item) {
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
        }*/
        
        var SetStatus = function (x) {
            var status = self.model.statusDataList.find(function (obj) {
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
        
        var GetConsumableIssueReturn = function () {
            if (ValidateDate()) {
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
//                if(self.model.ward.id == undefined){
//                    self.model.ward.id = $scope.nursingStationsWardsListId;
//                }
                var ward = self.model.wardList.find(function (data) {
                    return self.model.wardIdFromService == data.id;
                    });



                    if(self.model.wardIdFromService == undefined){
                    self.model.wardIdFromService = $scope.nursingStationsWardsListId;
                    }
                    if(self.model.ward.wardName!="All")
                    {
                        self.model.wardIdFromService=[];
                    self.model.wardIdFromService.push(self.model.ward.id);
                    }
                    if(self.model.ward.wardName=="All")
                    {
                        self.model.wardIdFromService=[];
                        self.model.wardIdFromService = $scope.nursingStationsWardsListId;
                    }
               
                var URI = CONSTANTS.PHARMACY.NURSING.PATIENT_ISSUE_RETURN.CONSUMABLE_SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
//                    "wardList": !!self.model.ward.id ? self.model.ward.id : undefined,
                    		"wardList": self.model.wardIdFromService ? self.model.wardIdFromService : self.model.ward.id,
                    "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
                    "bedNo": !!self.model.bedNo ? self.model.bedNo : undefined,
                    "returnNo": !!self.model.returnNo ? self.model.returnNo : undefined,
                    "statusId": !!self.model.status && !!self.model.status.id ? self.model.status.id : undefined,
                    "unitId":$scope.unitId
		
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.consumableIssueReturnlist = response.data;
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
                        "ward": self.model.ward,
                        "bedNo": self.model.bedNo,
                        "returnNo": self.model.returnNo
                    },
                    screenName: 'ConsumableIssueReturn',
                }
                screenDataService.setScreenData(screenData);*/
            }
        }

        var ClearSearchFields = function (){
            self.model.patientName = null,
            self.model.patientId = null,
            self.model.uhid = null,
            self.model.bedNo = null,
            self.model.bedId = null,
            self.model.encounterId = null,
            self.model.admissionId = null,
            self.model.visitDetails = null

        }

        var NavigateToDetail = function (item, tab) {
            detailService.setId(item.id);

            if (tab == 2) {
                $state.go('detailsPatientIssueReturnComsumable');
            } else {
                $state.go('detailsPatientIssueReturnMedication');
            }

        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("patientIssueReturn", {
            url: '/patientIssueReturn',
            templateUrl: 'views/PharmacyInventory/InPatient/patient-issue-return.html',
            controller: "PatientIssueReturn.Controller",
            title: 'Patient Issuance Return',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 55);
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
        .controller("PatientIssueReturn.Controller", patientIssueReturnController)
})();
