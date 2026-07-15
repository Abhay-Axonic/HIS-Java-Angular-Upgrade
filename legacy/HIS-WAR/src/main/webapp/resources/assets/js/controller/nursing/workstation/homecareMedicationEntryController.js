(function () {
    "use strict";

    function homecareMedicationEntryController($scope, $rootScope, $http, CONSTANTS, $cookies, GenericService, $filter, $state, growl, commonDetailService, CurrencyService,BillingGenericService,GenericServiceParamHeader) {
        var self = this;

        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;

            self.model = {
                date: new Date(),
                dateToDay: new Date(),
                toDate: new Date(),
                uhidFromService :  commonDetailService.getUHIDForBedManagment(),
                admissionIdFromService :  commonDetailService.getAdmissionIdForBedManagment(),
                dateOptions: {
                    formatYear: 'yyyy',
                    showWeeks: false,
                    // maxDate: new Date()
                },
                reasonList: [{
                    "reason": "Select Reason"
                }],

                visitDetails: [{
                    "visitNo": "Select Visit No"
                }],
                currentDate: false,
                patientSearchObj: {
                    "uhIdNumber": null,
                    "patientName": null,
                    "gender": null,
                    "mobileNo": null,
                    "birthDate": null,
                    "identificationNo": null,
                },
                comorbidityFlag :false,
                notifiableFlag : false,
                patientDiagnosisListIcon: []
            };
            
            $scope.dateOptions = {
                formatYear : 'yyyy',
                showWeeks : false,
                maxDate: new Date(),
            };
          
            $scope.datepicker = {
                 opened: false
            };
            var cookieObject = {};
            var cookieObjectAppConfig = {};

            [cookieObject, cookieObjectAppConfig] = $rootScope.checkCookies();
            self.model.userId = cookieObject.userId;

            var cookieObject = $cookies.getObject('cookieObject');
            if (cookieObject == undefined) {
                $state.go('login');
                return;
            }

            var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
            if (cookieObjectAppConfig == undefined) {
                $state.go('login');
                return;
            }
            // else {
            // console.log('cookieObjectAppConfig', cookieObjectAppConfig);

            // }
            $scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
            $scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
            $scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;


            $scope.unitId = cookieObject.unitId;
            $scope.organizationId = cookieObject.organizationId;
            
            $scope.patientSearchObj = {
					"uhIdNumber" : "",
					"patientName" : "",
					"genderId" : "",
					"mobileNo" : "",
					"birthDate" : "",
					"identificationNo" : "",
					"patientType" : 1,
					"unitId" : $scope.unitId,
					"organizationId" : $scope.organizationId
				}
				$scope.patientDetails = {
					"patientName" : "",
					"genderCode" : "",
					"age" : "",
					"uhIdNumber" : "",
					"birthDate" : ""
				}

            //Methods

            self.model.SearchpatientNames = SearchpatientNames;
            self.model.AddPatientDetails = AddPatientDetails;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.OpenDate = OpenDate;
            self.model.GetDrugAdministrator = GetDrugAdministrator;
            ViewDrugInfo
            self.model.OpenPopUp = OpenPopUp;
            self.model.ViewDrugInfo = ViewDrugInfo;
            self.model.SaveDrugAdministrator = SaveDrugAdministrator;
            //self.model.searchPatient = searchPatient;
            //self.model.getPatientDetail = getPatientDetail;
            self.model.getAllPatientDiagnosisForIcon = getAllPatientDiagnosisForIcon;
            self.model.openDatePicker = openDatePicker;
            self.model.dateOnBlur = dateOnBlur;

            //PopulateDropdown();
            //PopulateVisitTypeDropdown();
          
            // GetDrugAdministrator();Comment Ganesh
        }

        /*var PopulateVisitTypeDropdown = function () {

            var URI = CONSTANTS.GLOBAL.PATIENT_ISSUE_DROPDOWN_API + "/"+$scope.organizationId +"/"+ $scope.unitId+"/"+$scope.userId;

            self.model.visitNumber = self.model.visitDetails[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                	//self.model.visitType = '';
                    self.model.visitTypeList = response.data.visitType;
                    $rootScope.stopLoader();
                    if(self.model.uhidFromService){
                        searchPatient();
                    }
                });
                
        }*/
        
        var OpenDate = function () {
            $scope.datepicker.opened = true;
        }
        var openDatePicker = function () {
            self.model.dateOpened = true;
            var dateString = self.model.doa // Oct 23
            var dateMomentObject = moment(dateString, "DD/MM/YYYY"); // 1st argument - string, 2nd argument - format
            var dateObject = dateMomentObject.toDate(); // convert moment.js object to Date object
            self.model.dateOptions.minDate = dateObject.toString();
        }        
        var dateOnBlur = function () {
            if( self.model.date > new Date()){
                growl.error('You have selected future date');
                } 
        }
        
        var SearchpatientNames = function (search) {

            if (search.length < 1) return;

            if ((!self.model.prevSearch || self.model.prevSearch != search) && (search.length == 1)) {
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

        var AddPatientDetails = function (item) {

            if (!!item.patientId) {
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
                            self.model.uhid = response.data.uhid
                        // self.model.bedNo = response.data.bedNo,
                        // self.model.bedId = response.data.bedId
                        // self.model.encounterId = response.data.encounterId,
                        // self.model.admissionId = response.data.admissionId

                        if (self.model.visitType.id == 1) {
                            self.model.visitDetails = self.model.visitDetails.concat(response.data.visitDetails);
                        } else if (self.model.visitType.id != 1) {
                            // self.model.visitNumberText = response.data.visitDetails[0].visitNo;
                            self.model.encounterId = response.data.visitDetails[0].encounterId,
                                self.model.admissionId = response.data.visitDetails[0].admissionId
                        }

                        $rootScope.stopLoader();
                    }, function (err) {
                        growl.error('Error while fetching the patient details');
                        $rootScope.stopLoader();
                    });
            }
        }


        var GetDrugAdministrator = function () {  
        	
          	$scope.past=false;
       	 $scope.D1=moment(self.model.date).format('YYYY-MM-DD');
            $scope.D2=moment(new Date()).format('YYYY-MM-DD');
            if(($scope.D1 < $scope.D2 && $scope.D1 != $scope.D2 )){
                	$scope.past=true;
                }
        	
            if(commonDetailService.getAdmissionIdForBedManagment()){
                var admissionId= self.model.admissionId = commonDetailService.getAdmissionIdForBedManagment();
                commonDetailService.setDataId(undefined, self.model.uhidFromService);
            }

            if (!self.model.admissionId) {
                growl.error('Please Select Patient');
            }
            else {
                var URI = CONSTANTS.PHARMACY.NURSING.DRUG_ADMINISTRATOR.SEARCH_API;

                var data = {
                    // "admissionId": 90,
                    "admissionId": !!self.model.admissionId ? self.model.admissionId : admissionId,
                    "date": moment(self.model.date).format('YYYY-MM-DD'),
                    "unitId":$scope.unitId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.drugAdministratorList = response.data;

                        //patientData;
                      	//var doctorName1 = self.model.drugAdministratorList.doctorDetail.split('|');
                        var doctorName1 = self.model.drugAdministratorList.doctorDetail ? self.model.drugAdministratorList.doctorDetail.split('|') : [];// addded for resolved HIS-9105
                    	self.model.doctorName =doctorName1[0];
                        self.model.doctorDetail = self.model.doctorName ? self.model.doctorName : undefined;
                        self.model.prescriptionId = response.data.id;

                        self.model.drugAdministratorList.items.forEach(function (item) {
                            var doseTime = item.doseTime;
                            item.doseTimeArray = [];
                            for (var i = 0; i <= 23; i++) {
                                item.doseTimeArray.push({})
                            }
                            doseTime.forEach(function (data) {
                            	data['currentTime'] = moment().format("HH:mm");
                            	//data['currentTime'] = "16:00";
                                data['convertedTime'] = data.administrationTime;
                          	    data['actualAdministrationTime'] = data.actualAdministrationTime;
                                data['preBufferAdministrationTimeCheck'] = data.preBufferAdministrationTime;
                                data['postBufferAdministrationTimeCheck'] = data.postBufferAdministrationTime;
                                
                                	 item.doseTimeArray[(parseInt(data['convertedTime']) - 1)] = doseTime.find(function (x) {
                                         return x.convertedTime == data['convertedTime'];
                                     });
                                //stat 
                             /*   else{
                                	  data['convertedTime'] = data.actualAdministrationTime;
                                	  item.doseTimeArray[(parseInt(data['convertedTime']) - 1)] = doseTime.find(function (x) {
                                         return x.convertedTime == data['convertedTime'];
                                     });
                                }*/
                               
                            });
                        });
                        console.log("drugData",self.model.drugAdministratorList);
                        $rootScope.stopLoader();
                      //  self.model.admissionId = undefined;
                        self.model.admissionIdFromService =undefined;
                        self.model.uhidFromService =undefined;
                    })
            }
        }

        /*var PopulateDropdown = function () {

            var URI = CONSTANTS.PHARMACY.NURSING.DRUG_ADMINISTRATOR.REASON_DROPDOWN_API;

            self.model.reason = self.model.reasonList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.reasonList = self.model.reasonList.concat(response.data.reasons);
                    $rootScope.stopLoader();
                });
        }*/

        var OpenPopUp = function (item, doseData, selectedDate) {

            self.model.actualAdministrationTime = doseData.actualAdministrationTime;
            self.model.newAdministrationTime = doseData.actualAdministrationTime;
            self.model.note = doseData.note;
            self.model.reason = self.model.reasonList.find(function (reason) {
                return reason.id == doseData.reasonId;
            })

            if (moment(new Date()).utc().format('DD/MM/YYYY') == moment(selectedDate).utc().format('DD/MM/YYYY')) {
                self.model.currentDate = true;
            } else {
                self.model.currentDate = false;
            }

            if (doseData.administrationTime) {
                if (item.prnFlag) {
                    var popup = angular.element('#checkSOSTabletstatus');
                    popup.modal('show');
                } else {
                    var popup = angular.element('#checkTabletStatus');
                    popup.modal('show');
                }
                AddPopUpInfo(item, doseData);
            }else{
            	 if(item.statFlag){
            		 var popup = angular.element('#checkTabletStatus');
                     popup.modal('show');
                     AddPopUpInfo(item, doseData);
            	 }
            }
        }

        var AddPopUpInfo = function (item, doseData) {
            self.model.selectedItem = item;
            self.model.selectedDoseData = doseData;
            item['isPrnFlagSet'] = item.prnFlag;
        }

        var ViewDrugInfo = function (item) {
            if (item.prnFlag) {
                self.model.action = 'Open';
                var popup = angular.element('#drugInformation');
                popup.modal('show');
            }
        }

        var getMilliseconds = function (time) {
            return (Number(time.split(':')[0]) * 60 + Number(time.split(':')[1])) * 60 * 1000;
        }

        var SaveDrugAdministrator = function () {

            self.model.selectedItem.newAdministrationTime = self.model.newAdministrationTime;
            self.model.selectedItem.frequencyDetailId = self.model.selectedDoseData.id;
            self.model.selectedItem.note = self.model.note;
            self.model.selectedItem.reason = self.model.reason;

            self.model.selectedDoseData.newAdministrationTime = getMilliseconds(self.model.newAdministrationTime);
            // self.model.selectedDoseData.actualAdministrationTime = (new Date(moment(new Date()).format('YYYY-MM-DD') + ' ' + self.model.newAdministrationTime + ':00'.replace('-', '/'))).getTime();

            if (ValidateBill()) {
                var URI = CONSTANTS.PHARMACY.NURSING.DRUG_ADMINISTRATOR.SAVE_API;

                var data = {
                    "patientId": self.model.drugAdministratorList.patientId,
                    "visitTypeId": self.model.drugAdministratorList.visitTypeId,
                    "admissionId": self.model.drugAdministratorList.admissionId,
                    "encounterId": self.model.drugAdministratorList.encounterId,
                    "prescriptionId": self.model.drugAdministratorList.prescriptionId,
                    "items": [],
                    "unitId":$scope.unitId,
                    "organizationId":$scope.organizationId,
                    "userId":$scope.userId,
                    "storeId":$scope.storeId

                }

                var item = {
                    "prescriptionDetailId": self.model.selectedItem.id,
                    "frequencyDetailId": self.model.selectedItem.frequencyDetailId,
                    "administrationTime": self.model.selectedItem.newAdministrationTime,
                    "note": self.model.selectedItem.note,
                    "reasonId": self.model.selectedItem.reason.id
                }
                data.items.push(item);

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.selectedDoseData.actualAdministrationTime = getMilliseconds(self.model.newAdministrationTime);

                        growl.success('Drug administrator Successfully saved');
                        var popup = angular.element('#checkSOSTabletstatus');
                        popup.modal('hide');
                        var popup = angular.element('#checkTabletStatus');
                        popup.modal('hide');
                        ClearFields();
                        GetDrugAdministrator();
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
                        growl.error('Error while saving drug administrator');
                    });
            }
        }

        var ClearFields = function () {
            self.model.isAdministrationTimeValid = false;
            self.model.isReasonIdValid = false;
            self.model.isNoteValid = false;
        }

        var ValidateBill = function (itemList) {
            var isValid = true;

            if (!self.model.selectedItem.newAdministrationTime) {
                self.model.isAdministrationTimeValid = true;
                isValid = false;
                growl.error("Please select time");
            }

            if (!self.model.selectedItem.isPrnFlagSet) {
                if (!(self.model.selectedDoseData.preBufferAdministrationTime < self.model.selectedDoseData.newAdministrationTime && self.model.selectedDoseData.newAdministrationTime < self.model.selectedDoseData.postBufferAdministrationTime)) {
                    if (!self.model.selectedItem.reason.id) {
                        self.model.isReasonIdValid = true;
                        isValid = false;
                        growl.error("Please select reason");
                    }
                }
            }

            if (!!self.model.selectedItem.isPrnFlagSet) {
                if (!self.model.selectedItem.note) {
                    self.model.isNoteValid = true;
                    isValid = false;
                    growl.error("Please enter note");
                }
            }
            return isValid;
        }


        $scope.searchPatient = function(patientsearchObj) {
			console.log(patientsearchObj)
			if(patientsearchObj.uhIdNumber=="" && patientsearchObj.patientName=="" && patientsearchObj.genderId==""
				 && patientsearchObj.mobileNo == "" && patientsearchObj.birthDate=="" && patientsearchObj.identificationTypeId=="" && patientsearchObj.identificationNo=="")
			  {
					growl.warning("Please enter searching criteria details",{title : "warning!"
					});
					return false;
				}
			if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
			{
				growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
				$scope.patientList = [];
				return false;
			}	
			if(patientsearchObj.birthDate!="")
			patientsearchObj.birthDate= moment(patientsearchObj.birthDate).format('YYYY-MM-DD');			
			console.log("patientsearchObj.birthDate ",patientsearchObj.birthDate);
				$scope.noRecordMsg = false;
				$rootScope.startLoader();
			var URI = BASE_URL + ROOT_URL + PATIENTSERACHBYMULTIPLECRITERIAUNKNOWN;
			BillingGenericService.serviceAction(METHOD_POST, URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE)
					.then(
							function(response) {
								console.log(response);
								$scope.patientList = [];
								// $rootScope.startLoader();
								if (response.data.status == "success")
									$scope.patientList = response.data.listObject;
								if(response.data.listObject==null){
			        				growl.warning(response.data.message,{
			        					title : ERROR_MSG
			        				});
			        				$scope.patientsearchObj.birthDate="";
			        				$scope.patientList=[];
			        				return false;
			        			}
								$scope.patientSearchObj.birthDate="";
								$rootScope.stopLoader();
							});
		}

		$scope.getPatientDetailsByIndex = function(index) {
			/*if($scope.patientList[index].admissionId){
				var message = "";
				if($scope.unitId==$scope.patientList[index].admissionUnitId)
            		message = "Patient is already admitted in IP...!!";
            	else
            		message = "Patient is already admitted in another unit...!!";
				
				growl.warning(message,{
					title : WARNING_MSG
				});

				$scope.patientList = [];
				return false;
			}*/
				$scope.patientSelectedIdx = index;
				$scope.patientDetails.age = ($scope.patientList[index].age);
				$scope.patientDetails.patientName = ($scope.patientList[index].patientName);
				$scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
				$scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhIdNumber);
				$scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
				
				$scope.patientDetails.firstName = ($scope.patientList[index].firstName);
				$scope.patientDetails.lastName = ($scope.patientList[index].lastName);
				$scope.patientDetails.middleName = ($scope.patientList[index].middleName);
				$scope.patientDetails.emailId = ($scope.patientList[index].email);
				$scope.patientDetails.birthDate = ($scope.patientList[index].birthDate);
				$scope.patientDetails.mobileNumber = ($scope.patientList[index].mobileNo);
				//$scope.patientDetails.emailId = ($scope.patientList[index].emailId);
				
				 var array = $scope.patientList[index].birthDate.split('/');
				 var newDate = "";
			     newDate = array[1]+'/'+array[0]+'/'+array[2];
				 $scope.datepickerDOB = new Date(newDate);
				$scope.age = $rootScope.getAge(newDate);
				
				if ($scope.patientList[index].patientType == 1) {
					$scope.patientDetails.patientId = $scope.patientList[index].patientId;
				} else {
					$scope.patientDetails.tPatientId = $scope.patientList[index].patientId;
				}

				$scope.patientDetails.prefixId = $scope.patientList[index].prefixId;
				//$scope.patientDetails.id = $scope.patientList[index].genderId;
				$scope.patientDetails.genderId = $scope.patientList[index].genderId;
				$scope.patientDetails.patientCategoryId = $scope.patientList[index].patientCategoryId;
				
				$scope.regType = 2;
				$scope.fieldDisabled = true;
				
				  	//$scope.patientDetails.uhIdNumber = patient.uhIdNumber ? patient.uhIdNumber : patient[0].uhIdNumber;
		            $scope.patientDetails.dob = $scope.patientList[index].birthDate? $scope.patientList[index].birthDate : $scope.patientList[0].birthDate;
		            $scope.patientDetails.doa = $scope.patientList[index].doa ? $scope.patientList[index].doa : $scope.patientList[0].doa;
		            $scope.patientDetails.pdd = $scope.patientList[index].pdd ? $scope.patientList[index].pdd : $scope.patientList[0].pdd;
		            $scope.patientDetails.patientId = $scope.patientList[index].patientId ? $scope.patientList[index].patientId : $scope.patientList[0].patientId;
		            $scope.patientDetails.doctorDetail = $scope.patientList[index].doctorName ? $scope.patientList[index].doctorName : $scope.patientList[0].doctorName;
		            $scope.patientDetails.wardName = $scope.patientList[index].wardName ? $scope.patientList[index].wardName : $scope.patientList[0].wardName;
		            $scope.patientDetails.bedNo = $scope.patientList[index].bedNumber ? $scope.patientList[index].bedNumber : $scope.patientList[0].bedNumber;

		}
        
        /*var searchPatient = function () {
            if (!!self.model.patientSearchObj.birthDate) {
                    var birthDate = moment(self.model.patientSearchObj.birthDate).format('YYYY-MM-DD');
                } else {
                    var birthDate = undefined;
            }
          
            var patientData = self.model.patientSearchObj
            var URI = CONSTANTS.ROOT_URL + '/adt/patientSearchByMultipleCriteria';

            var data = {
                "uhIdNumber": !!patientData.uhIdNumber ? patientData.uhIdNumber : self.model.uhidFromService,
                "patientName": !!patientData.patientName ? patientData.patientName : undefined,
                "genderId": !!patientData.genderId && !!patientData.genderId ? patientData.genderId : undefined,
                "mobileNo": !!patientData.mobileNo ? patientData.mobileNo : undefined,
                "birthDate": birthDate,
                "identificationNo": !!patientData.identificationNo ? patientData.identificationNo : undefined,
                "patientType": 1,
                "unitId": !!$scope.unitId ? $scope.unitId : undefined,
                "organizationId": !!$scope.organizationId ? $scope.organizationId : undefined
            }
            $rootScope.startLoader();
            GenericService.serviceAction("POST", URI, data).then(
                function (response) {
                    $rootScope.stopLoader();
                   
                    self.model.patientList = response.data.listObject;
                    if(self.model.uhidFromService){
                        var isFromService = true;
                       // self.model.patientList.push(isFromService); 
                       self.model.patientList.forEach(function (item) {
                        item.isFromService = true;
                      });
                    }
                  
                    // self.model.patientSearchData.forEach(function(){

                    // })
                    // self.model.patientId = response.data.listObject[0].patientId;
                    $rootScope.stopLoader();
                    if(self.model.admissionIdFromService){
                        GetDrugAdministrator();
                        getPatientDetailForSelectedPatientfromService(self.model.patientList)
                    }
                    else{
                        self.model.admissionId = undefined;
                        self.model.age = undefined;
                        self.model.patientDetail = undefined;
                        self.model.uhIdNumber = undefined;
                        self.model.dob = undefined;
                        self.model.doa = undefined;
                        self.model.pdd = undefined;
                        self.model.doctorDetail = undefined;
                        self.model.wardName = undefined;
                        self.model.bedNo = undefined;
                        self.model.admissionId = undefined;
                        self.model.admissionIdFromService =undefined;
                        self.model.uhidFromService =undefined;

                    }
                   
                },
                function errorCallback(response) {
                    $rootScope.stopLoader();
                    growl.error('Error');
                });
        }

        var getPatientDetailForSelectedPatientfromService = function (patient) {
            self.model.admissionId = patient.admissionId ? patient.admissionId : patient[0].admissionId;
            self.model.age = patient.age ? patient.age : patient[0].age;
            self.model.patientDetail = patient.patientName ? patient.patientName : patient[0].patientName;
            self.model.uhIdNumber = patient.uhIdNumber ? patient.uhIdNumber : patient[0].uhIdNumber;
            self.model.dob = patient.birthDate? patient.birthDate : patient[0].birthDate;
            self.model.doa = patient.doa ? patient.doa : patient[0].doa;
            self.model.pdd = patient.pdd ? patient.pdd : patient[0].pdd;
            self.model.patientId = patient.patientId ? patient.patientId : patient[0].patientId;
            self.model.doctorDetail = patient.doctorName ? patient.doctorName : patient[0].doctorName;
            self.model.wardName = patient.wardName ? patient.wardName : patient[0].wardName;
            self.model.bedNo = patient.bedNumber ? patient.bedNumber : patient[0].bedNumber;
          //  self.model.admissionId = undefined;
            self.model.admissionIdFromService =undefined;
            self.model.uhidFromService =undefined;
            if(self.model.patientId){
                $rootScope.allergydata(patient[0]);
                getAllPatientDiagnosisForIcon( self.model.patientId);
            }
            
            
        }
        var getPatientDetail = function (patient) {
            self.model.admissionId = patient.admissionId ? patient.admissionId : undefined;
            self.model.age = patient.age ? patient.age : undefined;
            self.model.patientDetail = patient.patientName ? patient.patientName : undefined;
            self.model.uhIdNumber = patient.uhIdNumber ? patient.uhIdNumber : undefined;
            self.model.dob = patient.birthDate? patient.birthDate : undefined;
            self.model.doa = patient.doa ? patient.doa : undefined;
            self.model.pdd = patient.pdd ? patient.pdd : undefined;
//            self.model.doctorDetail = patient.doctorName ? patient.doctorName : undefined;
            self.model.wardName = patient.wardName ? patient.wardName : undefined;
            self.model.bedNo = patient.bedNumber ? patient.bedNumber : undefined;
           // self.model.admissionId = undefined;
            self.model.admissionIdFromService =undefined;
            self.model.uhidFromService =undefined;
            if(patient.patientId){
                $rootScope.allergydata(patient);
                getAllPatientDiagnosisForIcon(patient.patientId);
                GetDrugAdministrator();
            }
        }*/
        
    	// For Get Active Gender List
    	$scope.getSexMasterList = function() {
    		var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
    		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    			$scope.sexMasterList = response.data.listObject;
    		});
    	}
    	
    	$scope.getIdentificationTypeList = function(){
    		var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
    		BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
    			if (response.data.status == "success")
    				$scope.identificationTypeList = response.data.listObject;
    		});
    	}
        
        var getAllPatientDiagnosisForIcon= function(patientId){
            self.model.patientDiagnosisListIcon =[];
            var cookieObject = $cookies.getObject('cookieObject');
             var headerObjDia = {
                         'unit_id' : cookieObject.unitId,
                        'organization_id' : cookieObject.organizationId,
                        'patient_id' : patientId,
                       'Content-Type':'application/json',
             };
             var data={};
             var URI = CONSTANTS.ROOT_URL
             + "/api/opd/getPatientDiagnosisByPatientIdForIcon";
             GenericServiceParamHeader
             .serviceAction(METHOD_GET, URI,
                data,
                NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
             .then(
                     function(response) {
                         console.log(response);
                         // $rootScope.startLoader();
                         if (response.data.status == "success")
                         self.model.patientDiagnosisListIcon = response.data.listObject; 
                         console.log("patientDiagnosisListIcon",self.model.patientDiagnosisListIcon);
                             self.model.comorbidityFlag = false;
                             self.model.notifiableFlag = false;
                            
                                if(self.model.patientDiagnosisListIcon.length > 0)
                                    {
                                        self.model.complaintDelete=false;
                                        angular.forEach(self.model.patientDiagnosisListIcon,function(value,key) {
                                            if(value.isNotifiable == 'Y')
                                            {
                                                self.model.notifiableFlag = true;
                                            }
                                            else if(value.isComorbidity == 'Y')
                                            {
                                                self.model.comorbidityFlag = true;
                                            }
                                        });
                                    }
                         
                     });
            }
        
    	$scope.getSexMasterList();
    	$scope.getIdentificationTypeList();

        initializeController();
    }

    function config($stateProvider) {
        $stateProvider.state('homecareMedicationEntry', {
		 url: '/homecareMedicationEntry',
		 templateUrl: 'views/PharmacyInventory/InPatient/homecare-medication-entry.html',
		 controller: "HomecareMedicationEntry.Controller",
		 title: 'Homecare Medication Entry',
		 resolve : {

	      			data: function($q, $state, $timeout) {
	      		        var deferred = $q.defer();
	      		
	      		        $timeout(function() {
	      		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	      				 	.modules.filter(function(item){
	      	            	   return (item.id == 80 ||item.id == 84);
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
	      		 }
	});
    }

    angular
        .module("myApp")
        .config(config)
        .controller("HomecareMedicationEntry.Controller", homecareMedicationEntryController)
})();