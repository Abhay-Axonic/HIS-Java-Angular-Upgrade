(function () {
    "use strict";

    function patientIssueAcceptanceController($cookies, $scope, $rootScope, $http, CONSTANTS,commonDetailService, GenericService,PaginationService, $filter, $state, growl, CurrencyService, detailService, screenDataService, BillingGenericService) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
        	var date = new Date();
            self.model = {
                visitTypeFromService :  commonDetailService.getDataIdForBedManagmentPatientIssueAcceptanceVisitType(),
                patientIdFromService :  commonDetailService.getDataIdForBedManagmentPatientIssueAcceptancePatientId(),
                uhidFromService :  commonDetailService.getDataIdForBedManagmentPatientIssueAcceptanceUhidNum(),
                wardIdFromService :  commonDetailService.getDataIdForBedManagmentPatientIssueAcceptanceWardId(),
                wardNameFromService :  commonDetailService.getDataIdForBedManagmentPatientIssueAcceptanceWardName(),
                floorIdFromService :  commonDetailService.getDataIdForBedManagmentPatientIssueAcceptanceFloorId(),
                doctorIdFromService: commonDetailService.getDataIdForBedManagmentPatientIssueAcceptanceDoctorId(),
                doctorNameFromService: commonDetailService.getDataIdForBedManagmentPatientIssueAcceptanceDoctorFullName(),
                patientNameFromService: commonDetailService.getDataIdForBedManagmentPatientIssueAcceptancePatientName(),
                fromDate: new Date(),
                toDate: new Date(),
                activeTab: null,
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
                fromstoreList: [{
                    "store": "All"
                  }],
                visitDetails: [{
                    "visitNo": "Select Visit No"
                }],
                visitTypeList: [{
                	"id":0, //added for resolved HIS-9181
                    "type": "All"
                }],
                isConsignment: false,
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
                },
                pagination1: {
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
            self.model.AddDoctorDetails = AddDoctorDetails;
            self.model.SearchdoctorNames = SearchdoctorNames;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.PopulateDropdown = PopulateDropdown;
            self.model.GetMedicationIssueAcceptance = GetMedicationIssueAcceptance;
            self.model.GetConsumableIssueAcceptance = GetConsumableIssueAcceptance;
            self.model.NavigateToDetail = NavigateToDetail;
            self.model.isConsignmentFlag = isConsignmentFlag;
            self.model.ChangeVisitNumber = ChangeVisitNumber;
            self.model.ChangeVisitType = ChangeVisitType;
            self.model.PatientNameKeyPress = PatientNameKeyPress;
            self.model.uHIDKeyPress = uHIDKeyPress;
            self.model.SetActiveTab = SetActiveTab;
            var cookieObject = JSON.parse(sessionStorage.getItem('bedManagmentCookies'));
            if(cookieObject != undefined){
                self.model.activeTab = cookieObject.patientConsumableTab;
            }
            PopulateDropdown();
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
                                        self.model.wardIdFromService = $scope.wardListForGetBedList;
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
            self.model.wardIdFromService = [];
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
                        self.model.wardIdFromService = $scope.wardListForGetBedList;
                        $scope.nursingStationCookieShow = JSON.parse(sessionStorage.getItem('nursingStationDesc'));
                        PopulateDropdown();
                        angular.element('#nursingStationModal').modal('hide');
                    }
                });
            }
        }

		$scope.patientDocumnetInformation = function(info) {
			$scope.patientDocumentFlipBook ={};
			$scope.patientDocumentFlipBook =info;
			console.log($scope.patientDocumentFlipBook);
				var data =
				{
/*				"encounterId":$scope.patientDocumentFlipBook.encounterId,
				"patientId":$scope.patientDocumentFlipBook.patientId,
				"unitId":$scope.unitId,
				"organisationId":$scope.organizationId,
				"userId":$scope.userId
*/				}
				
			var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
			BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.patientDocumentListTest = [];
					if (response.data.status == "success") 
					{
						$scope.patientDocumentListTest = response.data.listObject;
						//console.log($scope.patientDocumentListTest.length);
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

       

        var PopulateDropdown = function () {
            self.model.medicationIssueAcceptancelist = [];
            var activetab = detailService.getActiveTab();
            
            if(activetab) {
                self.model.activeTab = activetab;
            }
            if (self.model.activeTab == 2) {
               PopulateConsumableDropdown();
               self.model.activeTab = 2;
            	// GetConsumableIssueAcceptance();
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

        var SearchpatientNames = function (search) {
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

        var AddDoctorDetails = function (item) {
        	self.model.selectedDoctorId =item.id;
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

            var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_ACCEPTANCE.MEDICATION_DROPDOWN_API + '/' + $scope.unitId;
            
            self.model.visitTypeList = [{ "id":0,"type": "All" }]; //remove duplication added for resolved HIS-9181 
            self.model.floorList= [{ "floor": "All" }]; //remove duplication added for resolved HIS-9181 
            self.model.wardList=[{ "wardName": "All" }]; //remove duplication added for resolved HIS-9181 

            self.model.visitType = self.model.visitTypeList[0];
            self.model.floor = self.model.floorList[0];
            self.model.ward = self.model.wardList[0];
            $scope.status = [{ name: "Select All", id: 0 }, { name: "Completed", id: 4 }, { name: "Closed", id: 8 }];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    //self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                	self.model.visitTypeList = response.data.visitType ? self.model.visitTypeList.concat(response.data.visitType) : self.model.visitTypeList //added for resolved HIS-9181
                    self.model.floorList = response.data.floor ? self.model.floorList.concat(response.data.floor) : self.model.floorList ;//added for resolved HIS-9181
                    self.model.wardList = response.data.ward ? self.model.wardList.concat(response.data.ward) : self.model.wardList ;//added for resolved HIS-9181
                    initialCallForMedication();
                    $rootScope.stopLoader();
                });
        }

        var PopulateConsumableDropdown = function () {

       var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_ACCEPTANCE.NEW_CONSUMABLE_DROPDOWN_API +'/' +$scope.unitId + '/'+$scope.organizationId + '/'+$scope.userId
      // 	 var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_ACCEPTANCE.NEW_CONSUMABLE_SEARCH_API_FOR_ISSUE_ACCEPTANCE;
       
            self.model.visitTypeList = [{ "id":0,"type": "All" }]; //remove duplication added for resolved HIS-12088 
            self.model.wardList=[{ "wardName": "All" }]; //remove duplication added for resolved HIS-12088
            self.model.storeList= [{ "store": "All"}];
            self.model.fromstoreList= [{"store": "All"}];
            self.model.visitType = self.model.visitTypeList[0];
            self.model.visitNumber = self.model.visitDetails[0];
            self.model.store = self.model.storeList[0];
            self.model.fromstore = self.model.fromstoreList[0];
            self.model.ward = self.model.wardList[0];
            $scope.statusConsumable = [{ name: "Select All", id: 0 }, { name: "Completed", id: 4 }, { name: "Closed", id: 8 }];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    //self.model.visitTypeList = self.model.visitTypeList.concat(response.data.visitType);
                	self.model.visitTypeList = response.data.visitType ? self.model.visitTypeList.concat(response.data.visitType) : self.model.visitTypeList //added for resolved HIS-9181
                    self.model.storeList = response.data.store ? self.model.storeList.concat(response.data.store): self.model.storeList;//added for resolved HIS-9181
                    self.model.wardList = response.data.ward ? self.model.wardList.concat(response.data.ward): self.model.wardList;//added for resolved HIS-9181
                    self.model.storeList = response.data.toStore ? self.model.storeList.concat(response.data.toStore): self.model.storeList;//added for resolved HIS-9181
                    self.model.fromstoreList = response.data.fromStore ? self.model.fromstoreList.concat(response.data.fromStore) : self.model.fromstoreList;//added for resolved HIS-9181
                    initialCallForConsumable();
                    $rootScope.stopLoader();
                });
        }

        var initialCallForMedication = function () {
            if (!screenDataService.getScreenData()) {
                GetMedicationIssueAcceptance();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'patientIssueAcceptanceMedication') {
                        var requestData = dataobj.searchrequest;
                        self.model.visitType = self.model.visitTypeList.find(function (visitType) {
                            if (requestData.visitType.id == visitType.id)
                                return visitType;
                        });
                        self.model.floor = self.model.floorList.find(function (floor) {
                            return requestData.floor.id == floor.id;
                        });
                        self.model.ward = self.model.wardList.find(function (ward) {
                            return requestData.ward.id == ward.id;
                        });
                        self.model.patientName = requestData.patientName
                        self.model.patientId = requestData.patientId
                        self.model.uhid = requestData.uhid
                        self.model.doctorName = requestData.doctorName
                        self.model.issueNo = requestData.issueNo
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                        self.model.SetFirstTabLevel = requestData.SetFirstTabLevel
                    }
                });
                GetMedicationIssueAcceptance();
            }
        }

        var initialCallForConsumable = function () {
            if (!screenDataService.getScreenData()) {
                GetConsumableIssueAcceptance();
            } else {
                screenDataService.getScreenData().forEach(function (dataobj) {
                    if (dataobj.screenName == 'patientIssueAcceptanceConsumable') {
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
                        self.model.visitNo = requestData.visitNo
                        self.model.issueNo = requestData.issueNo
                        self.model.isConsignment = requestData.isConsignment
                        self.model.fromDate = new Date(requestData.fromDate);
                        self.model.toDate = new Date(requestData.toDate);
                        self.model.SetFirstTabLevel = requestData.SetFirstTabLevel
                    }
                });
                GetConsumableIssueAcceptance();
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

        var GetMedicationIssueAcceptance = function () {
            if (ValidateDate()) {
             self.model.uhid = self.model.uhidFromService;
             self.model.doctorName =  self.model.doctorNameFromService ? self.model.doctorName : self.model.doctorName;
             
             self.model.patientName = self.model.patientNameFromService;
              var ward = self.model.wardList.find(function (data) {
                return self.model.wardIdFromService == data.id;
              });
            //   self.model.ward = ward;
              var visitType = self.model.visitTypeList.find(function (data) {
            	 return self.model.visitTypeFromService == data.id;
              });
              self.model.visitTypeFromService = visitType;
              var floor = self.model.floorList.find(function (data) {
                return self.model.floorIdFromService == data.id;
              });
              self.model.floor = floor;

              if(self.model.wardIdFromService == undefined){
                    self.model.wardIdFromService = $scope.nursingStationsWardsListId;
              }

                var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_ACCEPTANCE.MEDICATION_SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                   // "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "patientId": self.model.patientIdFromService ?  self.model.patientIdFromService : self.model.patientId,
                    // "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "patientName": !!self.model.patientNameFromService ? self.model.patientNameFromService : self.model.patientName,
                    // "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    "uhid": self.model.uhidFromService ? self.model.uhidFromService: self.model.uhid,
                  //  "doctorName": !!self.model.doctorName ? self.model.doctorName : undefined,
                    "doctorName": self.model.doctorNameFromService ? self.model.doctorName : self.model.doctorName,
                  //  "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "visitType": self.model.visitType.id ? self.model.visitType.id : undefined,
                  //  "wardId": !!self.model.ward.id ? self.model.ward.id : undefined,
                    "wardList": self.model.wardIdFromService ? self.model.wardIdFromService : self.model.ward.id,
                  //  "floor": !!self.model.floor && !!self.model.floor.id ? self.model.floor.id : undefined,
                    "floor":  self.model.floorIdFromService ? self.model.floorIdFromService : self.model.floor.id,
                    "issueNo": !!self.model.issueNo ? self.model.issueNo : undefined,
                    "statusId": !!self.model.status ? self.model.status.id : undefined,
                    "unitId":$scope.unitId,
                    "doctorId":self.model.selectedDoctorId
                };
                if(self.model.doctorName ==''|| self.model.doctorName ==null){
                self.model.selectedDoctorId ='';
                data.doctorId =null;
                }
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        
                        self.model.medicationIssueAcceptancelist = response.data;
                        self.model.pagination.data = response.data;
                        if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.medicationIssueAcceptancelist, self.model.pagination);
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
                //         "SetFirstTabLevel": self.model.SetFirstTabLevel,
                //         "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                //         "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                //         "visitType": self.model.visitType,
                //         "patientId": self.model.patientId,
                //         "patientName": self.model.patientName,
                //         "uhid": self.model.uhid,
                //         "doctorName": self.model.doctorName,
                //         "floor": self.model.floor,
                //         "ward": self.model.ward,
                //         "issueNo": self.model.issueNo
                //     },
                //     screenName: 'patientIssueAcceptanceMedication',
                // }
                // screenDataService.setScreenData(screenData);
            }
        }

        var isConsignmentFlag = function (flag) {
            if (flag) {
                self.model.isConsignment = true;
            } else {
                self.model.isConsignment = false;
            }
        }

        var GetConsumableIssueAcceptance = function () {
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
                if(self.model.wardIdFromService == undefined){
                    self.model.wardIdFromService = $scope.nursingStationsWardsListId;
                }
                var URI = CONSTANTS.PHARMACY.IP.PATIENT_ISSUE_ACCEPTANCE.CONSUMABLE_SEARCH_API;

                var data = {
                    "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                    "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                    "visitType": !!self.model.visitType && !!self.model.visitType.id ? self.model.visitType.id : undefined,
                    "patientName": !!self.model.patientName ? self.model.patientName : undefined,
                    "patientId": !!self.model.patientId ? self.model.patientId : undefined,
                    "uhid": !!self.model.uhid ? self.model.uhid : undefined,
                    // "wardId": !!self.model.ward.id ? self.model.ward.id : undefined,
                    "wardList": (self.model.visitType.id == 0 || self.model.visitType.id == 1 ) ? [] : self.model.wardIdFromService,
                    "visitNo": !!self.model.visitNumber && !!visitNumber ? visitNumber : undefined,
                    "issueNo": !!self.model.issueNo ? self.model.issueNo : undefined,
                    "storeId": !!self.model.store && !!self.model.store.id ? self.model.store.id : undefined,
                    "statusId": !!self.model.statusConsumable ? self.model.statusConsumable.id : undefined,
                    "unitId":$scope.unitId

                    // "consignment": self.model.isConsignment,

                };
                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                      //  self.model.consumableIssueAcceptancelist = response.data.reverse();
                    	self.model.pagination1.data=[];
                    	
                    	/*const dataList=response.data;
                    	
                    	
                    	const uniqueArray = dataList.filter((dataList, index) => {
                    		  const _thing = JSON.stringify(dataList);
                    		  return index === dataList.findIndex(obj => {
                    		    return JSON.stringify(obj) === _thing;
                    		  });
                    		});
                    	*/
                    	$scope.dataList=response.data;	
                    	/*for (let i = 0; i < $scope.dataList.length; i++) {
                    	     for (let j = i + 1; j < $scope.dataList.length; j++) {
                    	       if ($scope.dataList[i].uhid === $scope.dataList[j].uhid) {
                    	    	   $scope.dataList.splice(i, 1);
                    	       }
                    	     }
                    	    }*/
                    	
                    	
                        self.model.consumableIssueAcceptancelist = $scope.dataList;
                          if(!!response && !!response.data && response.data.length > 0){            
                            PaginationService.searchData(self.model.consumableIssueAcceptancelist, self.model.pagination1);
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
                         "SetFirstTabLevel": self.model.SetFirstTabLevel,
                         "fromDate": moment(self.model.fromDate).format('YYYY-MM-DD'),
                         "toDate": moment(self.model.toDate).format('YYYY-MM-DD'),
                         "visitType": self.model.visitType,
                         "patientId": self.model.patientId,
                         "patientName": self.model.patientName,
                         "uhid": self.model.uhid,
                         "ward": self.model.ward,
                         "visitNo": self.model.visitNumber,
                         "store": self.model.store,
                         "issueNo": self.model.issueNo,
                         "consignment": self.model.isConsignment
                     },
                     screenName: 'patientIssueAcceptanceConsumable',
                 }
                 screenDataService.setScreenData(screenData);
                
                
            }
        }

        var NavigateToDetail = function (item) {
            detailService.setId(item.id);
            commonDetailService.setDataId(item.indentId);
            var activetab = detailService.getActiveTab();
            if (activetab == 2) {
                $state.go('detailsConsumablePatientIssueAcceptance');
            } else {
                $state.go('detailsPatientIssueAcceptance');
            }

        }

        var SetActiveTab = function (tab) {
            self.model.activeTab = tab;            
            detailService.setActiveTab(tab);
        }

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state("patientIssueAcceptance", {
            url: '/patientIssueAcceptance',
            templateUrl: 'views/PharmacyInventory/InPatient/patient-issue-acceptance.html',
            controller: "PatientIssueAcceptance.Controller",
            title: 'Issuance Acceptance',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 58);
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
        .controller("PatientIssueAcceptance.Controller", patientIssueAcceptanceController)
})();