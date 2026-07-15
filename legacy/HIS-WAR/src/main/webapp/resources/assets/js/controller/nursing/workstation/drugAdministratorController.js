(function () {
    "use strict";

    function drugAdministratorController($scope, $rootScope, $http, CONSTANTS, $cookies, GenericService, $filter, $state, growl, commonDetailService, CurrencyService,BillingGenericService,GenericServiceParamHeader, $timeout) {
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
            
            /*******for start admin date****/
            $scope.dateOptionsScheduleStartOption = {
             		formatYear : 'yyyy',
                    showWeeks : false,
                    minDate : moment(new Date()).subtract(1, 'days').toDate(),
                    maxDate : moment(new Date()).add(1, 'days').toDate(),
					};
			$scope.openDatePickerStartDate = function() {
				self.model.startAdministrationDate.opened = true;
			};

			self.model.startAdministrationDate = {
				opened : false
			};
			
			 /*******for stop admin date****/
            $scope.dateOptionsScheduleStopOption = {
             		formatYear : 'yyyy',
						showWeeks : false,
						minDate:new Date(),
					    minDate : moment(new Date()).subtract(1, 'days').toDate(),
		                maxDate : moment(new Date()).add(1, 'days').toDate(),
//						maxDate : new Date()// max
					};
			$scope.openDatePickerStopDate = function() {
				self.model.stopAdministrationDate.opened = true;
			};

			self.model.stopAdministrationDate = {
				opened : false
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

            //Methods

            self.model.SearchpatientNames = SearchpatientNames;
            self.model.AddPatientDetails = AddPatientDetails;
            self.model.search = '';
            self.model.prevSearch = '';
            self.model.patientNames = [];
            self.model.OpenDate = OpenDate;
            self.model.GetDrugAdministrator = GetDrugAdministrator;
            self.model.OpenPopUp = OpenPopUp;
            self.model.ViewDrugInfo = ViewDrugInfo;
            self.model.SaveDrugAdministrator = SaveDrugAdministrator;
           
            self.model.getPatientDetail = getPatientDetail;
            self.model.getAllPatientDiagnosisForIcon = getAllPatientDiagnosisForIcon;
            self.model.openDatePicker = openDatePicker;
            self.model.dateOnBlur = dateOnBlur;
            self.model.getBatchDetails = getBatchDetails;

            PopulateDropdown();
            PopulateVisitTypeDropdown();
            self.model.searchPatient = searchPatient;
            // GetDrugAdministrator();Comment Ganesh
        }
        
        $scope.initClockPicker = function() 
        { 
			$('.clockpicker').clockpicker({ autoclose: true});

        }
        
        
		$scope.openFun = function() {
			$scope.popupDate.opened = true;
		};

		$scope.popupDate =
		{
			opened : false
		};

        // rejectReasonService.GetReason().then(function (reason) {
        //     self.model.rejectReasons = reason;
        // });
        var PopulateVisitTypeDropdown = function () {

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
                
        }
        
        var OpenDate = function () {
           
            $scope.datepicker.opened = true;
        }
        var openDatePicker = function () {
            self.model.dateOpened = true;
        //     var convertedDate1=   self.model.doa.toDate()
        //   //  $scope.dateOptions.minDate = self.model.doa.toString();
        //     var convertedDate=  moment(self.model.doa).format('MM-DD-YYYY')
           
             // $scope.dateOptions.minDate=$filter('date')(convertedDate,'MM/dd/yyyy');

             var dateString = self.model.doa // Oct 23

var dateMomentObject = moment(dateString, "DD/MM/YYYY"); // 1st argument - string, 2nd argument - format
var dateObject = dateMomentObject.toDate(); // convert moment.js object to Date object

  self.model.dateOptions.minDate = dateObject.toString();
        }
        var dateOnBlur = function () {
           /* if( self.model.date > new Date()){
                growl.error('You have selected future date');
                }*/
           
          
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

        $scope.flagSubCheck=false;
       /* var GetDrugAdministrator = function () {  
          	$scope.past = false;
          	$scope.future = false;
       	    $scope.D1=moment(self.model.date).format('YYYY-MM-DD');
            $scope.D2=moment(new Date()).format('YYYY-MM-DD');
            if(($scope.D1 < $scope.D2 && $scope.D1 != $scope.D2 )){
                	$scope.past = true;
                }
            
            if(($scope.D1 > $scope.D2 && $scope.D1 != $scope.D2 )){
            	$scope.future = true;
            }
            
            if(commonDetailService.getAdmissionIdForBedManagment()){
                var admissionId= self.model.admissionId = commonDetailService.getAdmissionIdForBedManagment();
                commonDetailService.setDataId(undefined, self.model.uhidFromService);
            }

            if (!self.model.admissionId) {
                growl.warning('Please select admitted patient');
            }
            else {
                var URI = CONSTANTS.PHARMACY.NURSING.DRUG_ADMINISTRATOR.SEARCH_API;
                var date = moment(self.model.date).format('YYYY-MM-DD');
                if($scope.ddtt == true){
                if(date > $scope.stopDate){
                	
                	var startDat = moment($scope.stopDate).toDate();
            		var kk =  moment(startDat).add(1, 'days').toDate();
            		 if(date ==  moment(kk).format('YYYY-MM-DD')){
            			 date = $scope.stopDate;
            		 }
                   }
                }
                var data = {
                    "admissionId": !!self.model.admissionId ? self.model.admissionId : admissionId,
                    "date": date,
                    "unitId":$scope.unitId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.drugAdministratorList = response.data;
                        
                        $scope.tempList=response.data;
                        var doctorName1 = self.model.drugAdministratorList.doctorDetail ? self.model.drugAdministratorList.doctorDetail.split('|') : [];// addded for resolved HIS-9105
                    	self.model.doctorName =doctorName1[0];
                        self.model.doctorDetail = self.model.doctorName ? self.model.doctorName : undefined;
                        self.model.prescriptionId = response.data.id;
                        
                        
                        var intCount=1;
                        
                        self.model.drugAdministratorList.items.forEach(function (item , index) {
                            item.flag = false;
                            $scope.ddtt = false;
                            angular.forEach(item.doseTime, function(d){
								 if(d.administrationTime < item.startTime){
									item.flag = true;
									$scope.ddtt = true;
								} 
                        	});
                        	if(item.flag == true){
								item.time = [];
								angular.forEach(item.doseTime, function(d){
									if(moment(item.startDate).format('YYYY-MM-DD') == moment(self.model.date).format('YYYY-MM-DD')){     
										if(d.administrationTime > item.startTime){
											item.time.push(d);
										}
									}
									else if(moment(item.stopDate).format('YYYY-MM-DD') < moment(self.model.date).format('YYYY-MM-DD')){     
										if(d.administrationTime < item.startTime){
											item.time.push(d);
										}
									}
									else{
										item.time.push(d);
									}
								});	
                        	}
                        	else{
                        		item.time = item.doseTime;
                        	}

               
                            var doseTime = item.time;
                            item.doseTimeArray = [];
                            var startDate = moment(item.startDate).format('YYYY-MM-DD');
                            item.startDateNew = moment(item.startDate).format('YYYY-MM-DD');
                            var stopDate = moment(item.stopDate).format('YYYY-MM-DD');
                            $scope.stopDate = stopDate;
                            item.stopDateNew = moment(item.stopDate).format('YYYY-MM-DD');
                            item.duration = moment(stopDate).diff(moment(startDate), 'days');
                            item.duration = item.duration + 1;
                            
                            for (var i = 0; i <= 23; i++) {
                                item.doseTimeArray.push({})
                            }
                            doseTime.forEach(function (data) {
                            	data['currentTime'] = moment().format("HH:mm");
                                data['convertedTime'] = data.administrationTime;
                          	    data['actualAdministrationTime'] = data.actualAdministrationTime;
                                data['preBufferAdministrationTimeCheck'] = data.preBufferAdministrationTime;
                                data['postBufferAdministrationTimeCheck'] = data.postBufferAdministrationTime;
                                data['batchNo'] = data.batchNo;
                                data['batchId'] = data.batchId;
                                item.doseTimeArray[(parseInt(data['convertedTime']) - 1)] = doseTime.find(function (x) {
                                    return x.convertedTime == data['convertedTime'];
                                });
                                
                                item.doseTimeArray[(parseInt(data['convertedTime']))] = doseTime.find(function (x) {
                                    return x.convertedTime == data['convertedTime'];
                                });
                                
                            });
                            
                            if(item.rejectedQuantity != undefined && item.rejectedQuantity !=null && item.rejectedQuantity != 0){
                            var acceptQty = item.acceptQty;
	                    	var orderQty = item.orderQty;
	                    	var noOfAdministration = item.noOfAdministration;
	                    	 var rejectedQuantity = item.rejectedQuantity;
	                    	 
	                    	 if(rejectedQuantity != null && rejectedQuantity != 0){
	                    		 var acceptQty = 0;
	                    		 if(item.acceptQty == null || item.acceptQty == undefined){
	                    			 item.acceptQty = 0;
	                    		 }
	                    		 var acceptQty = item.acceptQty + item.rejectedQuantity
	                    	 }
	                    	if(acceptQty != null){
	                    	if(item.flag == true){
	                    	
	                    		var remainingQuantity = orderQty - acceptQty;
	                    		item.remainingQuantity = remainingQuantity;
	                    		var duration = remainingQuantity/noOfAdministration;
	                    		var startDat = moment(item.startDateNew).toDate();
	                    		var newEndDate =  moment(startDat).add(duration, 'days').toDate();
	                    		 var calnewEndDate = moment(newEndDate).format('YYYY-MM-DD');
	                    		 $scope.stopDate = calnewEndDate;
	                    		 item.remainingQuantity = calnewEndDate;
	                    		 if(moment(self.model.date).format('YYYY-MM-DD') == calnewEndDate){
	                    			 item.time=[];
	                    				angular.forEach(item.doseTime, function(d){
	    									if(moment(item.startDate).format('YYYY-MM-DD') == moment(self.model.date).format('YYYY-MM-DD')){     
	    										if(d.administrationTime > item.startTime){
	    											item.time.push(d);
	    										}
	    									}
	    									else if(moment($scope.stopDate).format('YYYY-MM-DD') == moment(self.model.date).format('YYYY-MM-DD')){     
	    										if(d.administrationTime < item.startTime){
	    											item.time.push(d);
	    										}
	    									}
	    									else{
	    										item.time.push(d);
	    									}
	    								});	
	                    				 var doseTime = item.time;
	                                     item.doseTimeArray = [];
	                                     
	                                     for (var i = 0; i <= 23; i++) {
	                                         item.doseTimeArray.push({})
	                                     }
	                                     doseTime.forEach(function (data) {
	                                     	data['currentTime'] = moment().format("HH:mm");
	                                         data['convertedTime'] = data.administrationTime;
	                                   	    data['actualAdministrationTime'] = data.actualAdministrationTime;
	                                         data['preBufferAdministrationTimeCheck'] = data.preBufferAdministrationTime;
	                                         data['postBufferAdministrationTimeCheck'] = data.postBufferAdministrationTime;
	                                         data['batchNo'] = data.batchNo;
	                                         data['batchId'] = data.batchId;
	                                         item.doseTimeArray[(parseInt(data['convertedTime']))] = doseTime.find(function (x) {
	                                             return x.convertedTime == data['convertedTime'];
	                                         });
	                                     });
	                    		 }
	                    		 
	                    		 if(moment(self.model.date).format('YYYY-MM-DD')  > calnewEndDate){
	                    			 
	                    			 self.model.drugAdministratorList.items.splice(index, 1);
	                    		 }
	                    		 
	                    		 
	                    	}else{
		                    		var remainingQuantity = orderQty - acceptQty;
		                    		item.remainingQuantity = remainingQuantity;
		                    		var duration = remainingQuantity/noOfAdministration;
		                    		var startDat = moment(item.startDateNew).toDate();
		                    		var newEndDate =  moment(startDat).add(duration-1, 'days').toDate();
		                    		 var calnewEndDate = moment(newEndDate).format('YYYY-MM-DD');
		                    		 item.remainingQuantity = calnewEndDate;
		                    		 if(date > calnewEndDate){
		                    			 self.model.drugAdministratorList.items.splice(index, 1);
		                    			
		                    		 }
	                    	}
	                    	}
                            }
                            if(item.returnQuantity != undefined && item.returnQuantity !=null && item.returnQuantity != 0){

                                var acceptQty = item.acceptQty;
    	                    	var orderQty = item.orderQty;
    	                    	var noOfAdministration = item.noOfAdministration;
    	                    	 var returnQuantity = item.returnQuantity;
    	                    	 
    	                    	 if(returnQuantity != null && returnQuantity != 0){
    	                    		 var acceptQty = 0;
    	                    		 if(item.acceptQty == null || item.acceptQty == undefined){
    	                    			 item.acceptQty = 0;
    	                    		 }
    	                    		 var acceptQty = item.returnQuantity;
    	                    	 }
    	                    	if(acceptQty != null){
    	                    	if(item.flag == true){
    	                    	
    	                    		var remainingQuantity = orderQty - acceptQty;
    	                    		item.remainingQuantity = remainingQuantity;
    	                    		var duration = remainingQuantity/noOfAdministration;
    	                    		var startDat = moment(item.startDateNew).toDate();
    	                    		var newEndDate =  moment(startDat).add(duration, 'days').toDate();
    	                    		 var calnewEndDate = moment(newEndDate).format('YYYY-MM-DD');
    	                    		 $scope.stopDate = calnewEndDate;
    	                    		 item.remainingQuantity = calnewEndDate;
    	                    		 if(moment(self.model.date).format('YYYY-MM-DD') == calnewEndDate){
    	                    			 item.time=[];
    	                    				angular.forEach(item.doseTime, function(d){
    	    									if(moment(item.startDate).format('YYYY-MM-DD') == moment(self.model.date).format('YYYY-MM-DD')){     
    	    										if(d.administrationTime > item.startTime){
    	    											item.time.push(d);
    	    										}
    	    									}
    	    									else if(moment($scope.stopDate).format('YYYY-MM-DD') == moment(self.model.date).format('YYYY-MM-DD')){     
    	    										if(d.administrationTime < item.startTime){
    	    											item.time.push(d);
    	    										}
    	    									}
    	    									else{
    	    										item.time.push(d);
    	    									}
    	    								});	
    	                    				 var doseTime = item.time;
    	                                     item.doseTimeArray = [];
    	                                     
    	                                     for (var i = 0; i <= 23; i++) {
    	                                         item.doseTimeArray.push({})
    	                                     }
    	                                     doseTime.forEach(function (data) {
    	                                     	data['currentTime'] = moment().format("HH:mm");
    	                                         data['convertedTime'] = data.administrationTime;
    	                                   	    data['actualAdministrationTime'] = data.actualAdministrationTime;
    	                                         data['preBufferAdministrationTimeCheck'] = data.preBufferAdministrationTime;
    	                                         data['postBufferAdministrationTimeCheck'] = data.postBufferAdministrationTime;
    	                                         data['batchNo'] = data.batchNo;
    	                                         data['batchId'] = data.batchId;
    	                                         item.doseTimeArray[(parseInt(data['convertedTime']))] = doseTime.find(function (x) {
    	                                             return x.convertedTime == data['convertedTime'];
    	                                         });
    	                                     });
    	                    		 }
    	                    		 
    	                    		 if(moment(self.model.date).format('YYYY-MM-DD')  >= calnewEndDate){
    	                    			 
    	                    			 self.model.drugAdministratorList.items.splice(index, 1);
    	                    		 }
    	                    		 
    	                    		 
    	                    	}else{
    		                    		var remainingQuantity = orderQty - acceptQty;
    		                    		item.remainingQuantity = remainingQuantity;
    		                    		var duration = remainingQuantity/noOfAdministration;
    		                    		var startDat = moment(item.startDateNew).toDate();
    		                    		var newEndDate =  moment(startDat).add(duration-1, 'days').toDate();
    		                    		 var calnewEndDate = moment(newEndDate).format('YYYY-MM-DD');
    		                    		 item.remainingQuantity = calnewEndDate;
    		                    		 if(date > calnewEndDate){
    		                    			 self.model.drugAdministratorList.items.splice(index, 1);
    		                    			
    		                    		 }
    	                    	}
    	                    	}
                                
                            }
                            
                            if(intCount == $scope.tempList.length){
                            	  self.model.demolist = []
                                  self.model.paretItemIds = [];

                                  self.model.drugAdministratorList.items.forEach(function (item ,ind) {
                                      if(item.isSubstitute == true){
          								var arr = {};
          								arr.id = item.id,
          								arr.parentSubstituteId = item.parentSubstituteId,
          								arr.duration = item.duration ,
          								$scope.parentSubstituteId = item.parentSubstituteId,
          								arr.indexSub =ind,
          								arr.index = self.model.drugAdministratorList.items.findIndex(record => record.id == item.parentSubstituteId)
          								self.model.paretItemIds.push(arr);
          								console.log("paretItemIds " , self.model.paretItemIds);
                                      }
                                  });
                                      if(self.model.paretItemIds.length > 0){
          								self.model.demolist = self.model.drugAdministratorList;
          								self.model.demolist1 = self.model.drugAdministratorList;

          								self.model.demolist.items.forEach(function (vaule1,index1) {
          									$scope.count = 1;
          										self.model.paretItemIds.forEach(function (value,ind) {
          											if(value.parentSubstituteId == vaule1.id){
          													$scope.count++;
          												vaule1.count = $scope.count;
          												}
          										});
                                          });

                                  self.model.paretItemIds.forEach(function (value,ind) {
          							self.model.demolist.items.forEach(function (vaule1,index1) {
          								if(value.parentSubstituteId == vaule1.id){
          									var count = vaule1.duration / vaule1.count;
          									var diff = moment( $scope.D1).diff(moment(vaule1.startDateNew), 'days');
          									if(diff < count){
          										var indexk = self.model.demolist1.items.findIndex(record => record.id == value.id)
          										self.model.drugAdministratorList.items.splice(indexk, 1);
          									}else{
          										 var indexNew = self.model.drugAdministratorList.items.findIndex(record => record.id == value.parentSubstituteId)
          										    self.model.drugAdministratorList.items.splice(indexNew, 1);
          									}

          								}
          							});
                                  });
                                  }
                            }
                            intCount++;
                        });
                        
                       
                
                        
                      
                        console.log("drugData",self.model.drugAdministratorList);
                        $rootScope.stopLoader();
                        self.model.admissionIdFromService =undefined;
                        self.model.uhidFromService =undefined;
                    })
            }
        }
*/	
        $scope.tempNewDataList=[]; 
        var GetDrugAdministrator = function () {
            $scope.selectedDate=moment(self.model.date).format('YYYY-MM-DD')

        	$scope.tempNewDataList=[];  
          	$scope.past = false;
          	$scope.future = false;
       	    $scope.D1=moment(self.model.date).format('YYYY-MM-DD');
            $scope.D2=moment(new Date()).format('YYYY-MM-DD');
            if(($scope.D1 < $scope.D2 && $scope.D1 != $scope.D2 )){
                	$scope.past = true;
                }
            
            if(($scope.D1 > $scope.D2 && $scope.D1 != $scope.D2 )){
            	$scope.future = true;
            }
            
            if(commonDetailService.getAdmissionIdForBedManagment()){
                var admissionId= self.model.admissionId = commonDetailService.getAdmissionIdForBedManagment();
                commonDetailService.setDataId(undefined, self.model.uhidFromService);
            }

            if (!self.model.admissionId) {
                growl.warning('Please select admitted patient');
            }
            else {
                var URI = CONSTANTS.PHARMACY.NURSING.DRUG_ADMINISTRATOR.SEARCH_API;
                var date = moment(self.model.date).format('YYYY-MM-DD');
                if($scope.ddtt == true){
                if(date > $scope.stopDate){
                	
                	var startDat = moment($scope.stopDate).toDate();
            		var kk =  moment(startDat).add(1, 'days').toDate();
            		 if(date ==  moment(kk).format('YYYY-MM-DD')){
            			 date = $scope.stopDate;
            		 }
                   }
                }
                var data = {
                    "admissionId": !!self.model.admissionId ? self.model.admissionId : admissionId,
                    "date": date,
                    "dateString": moment(date).format('YYYY-MM-DD'),
                    "unitId":$scope.unitId
                };

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                        self.model.drugAdministratorList = response.data;
                       
                        $scope.tempList=angular.copy(response.data);
                        var doctorName1 = self.model.drugAdministratorList.doctorDetail ? self.model.drugAdministratorList.doctorDetail.split('|') : [];// addded for resolved HIS-9105
                    	self.model.doctorName =doctorName1[0];
                        self.model.doctorDetail = self.model.doctorName ? self.model.doctorName : undefined;
                        self.model.prescriptionId = response.data.id;
                        
                        $scope.dummyList=[];
                        var intCount=1;
                        
                        self.model.drugAdministratorList.items.forEach(function (item , index) {
                            item.flag = false;
                            $scope.ddtt = false;
                            angular.forEach(item.doseTime, function(d){
								 if(d.administrationTime < item.startTime){
									item.flag = true;
									$scope.ddtt = true;
								} 
                        	});
                        	if(item.flag == true){
								item.time = [];
								angular.forEach(item.doseTime, function(d){
									if(moment(item.startDate).format('YYYY-MM-DD') == moment(self.model.date).format('YYYY-MM-DD')){     
										if(d.administrationTime > item.startTime){
											item.time.push(d);
										}
									}
									else if(moment(item.stopDate).format('YYYY-MM-DD') < moment(self.model.date).format('YYYY-MM-DD')){     
										if(d.administrationTime < item.startTime){
											item.time.push(d);
										}
									}
									else{
										item.time.push(d);
									}
								});	
                        	}
                        	else{
                        		item.time = item.doseTime;
                        	}

               
                            var doseTime = item.time;
                            item.doseTimeArray = [];
                            var startDate = moment(item.startDate).format('YYYY-MM-DD');
                            item.startDateNew = moment(item.startDate).format('YYYY-MM-DD');
                            var stopDate = moment(item.stopDate).format('YYYY-MM-DD');
                            $scope.stopDate = stopDate;
                            item.stopDateNew = moment(item.stopDate).format('YYYY-MM-DD');
                            item.duration = moment(stopDate).diff(moment(startDate), 'days');
                            item.duration = item.duration + 1;
                            
                            for (var i = 0; i <= 23; i++) {
                                item.doseTimeArray.push({})
                            }
                            doseTime.forEach(function (data) {
                            	data['currentTime'] = moment().format("HH:mm");
                                data['convertedTime'] = data.administrationTime;
                          	    data['actualAdministrationTime'] = data.actualAdministrationTime;
                                data['preBufferAdministrationTimeCheck'] = data.preBufferAdministrationTime;
                                data['postBufferAdministrationTimeCheck'] = data.postBufferAdministrationTime;
                                data['batchNo'] = data.batchNo;
                                data['batchId'] = data.batchId;
                                /*item.doseTimeArray[(parseInt(data['convertedTime']) - 1)] = doseTime.find(function (x) {
                                    return x.convertedTime == data['convertedTime'];
                                });*/
                                
                                item.doseTimeArray[(parseInt(data['convertedTime']))] = doseTime.find(function (x) {
                                    return x.convertedTime == data['convertedTime'];
                                });
                                
                            });
                            
                            if(item.rejectedQuantity != undefined && item.rejectedQuantity !=null && item.rejectedQuantity != 0){
                            var acceptQty = item.acceptQty;
	                    	var orderQty = item.orderQty;
	                    	var noOfAdministration = item.noOfAdministration;
	                    	 var rejectedQuantity = item.rejectedQuantity;
	                    	 
	                    	 if(rejectedQuantity != null && rejectedQuantity != 0){
	                    		 var acceptQty = 0;
	                    		 if(item.acceptQty == null || item.acceptQty == undefined){
	                    			 item.acceptQty = 0;
	                    		 }
	                    		 var acceptQty = item.acceptQty + item.rejectedQuantity
	                    	 }
	                    	if(acceptQty != null){
	                    	if(item.flag == true){
	                    	
	                    		var remainingQuantity = orderQty - acceptQty;
	                    		item.remainingQuantity = remainingQuantity;
	                    		var duration = remainingQuantity/noOfAdministration;
	                    		var startDat = moment(item.startDateNew).toDate();
	                    		var newEndDate =  moment(startDat).add(duration, 'days').toDate();
	                    		 var calnewEndDate = moment(newEndDate).format('YYYY-MM-DD');
	                    		 $scope.stopDate = calnewEndDate;
	                    		 item.remainingQuantity = calnewEndDate;
	                    		 if(moment(self.model.date).format('YYYY-MM-DD') == calnewEndDate){
	                    			 item.time=[];
	                    			 var stopcount=0;
	                    				angular.forEach(item.doseTime, function(d){
	    									if(moment(item.startDate).format('YYYY-MM-DD') == moment(self.model.date).format('YYYY-MM-DD')){     
	    										if(d.administrationTime > item.startTime){
	    											item.time.push(d);
	    										}
	    									}
	    									else if(moment($scope.stopDate).format('YYYY-MM-DD') == moment(self.model.date).format('YYYY-MM-DD')){     
	    										stopcount++;
	    										if(item.doseTime.length >= stopcount){
	    											item.time.push(d);
	    										}
	    									}
	    									else{
	    										item.time.push(d);
	    									}
	    								});	
	                    				 var doseTime = item.time;
	                                     item.doseTimeArray = [];
	                                     
	                                     for (var i = 0; i <= 23; i++) {
	                                         item.doseTimeArray.push({})
	                                     }
	                                     doseTime.forEach(function (data) {
	                                     	data['currentTime'] = moment().format("HH:mm");
	                                         data['convertedTime'] = data.administrationTime;
	                                   	    data['actualAdministrationTime'] = data.actualAdministrationTime;
	                                         data['preBufferAdministrationTimeCheck'] = data.preBufferAdministrationTime;
	                                         data['postBufferAdministrationTimeCheck'] = data.postBufferAdministrationTime;
	                                         data['batchNo'] = data.batchNo;
	                                         data['batchId'] = data.batchId;
	                                         item.doseTimeArray[(parseInt(data['convertedTime']))] = doseTime.find(function (x) {
	                                             return x.convertedTime == data['convertedTime'];
	                                         });
	                                     });
	                    		 }
	                    		 
	                    		 if(moment(self.model.date).format('YYYY-MM-DD')  > calnewEndDate){
	                    			 
	                    			 self.model.drugAdministratorList.items.splice(index, 1);
	                    		 }
	                    		 
	                    		 
	                    	}else{
		                    		var remainingQuantity = orderQty - acceptQty;
		                    		item.remainingQuantity = remainingQuantity;
		                    		var duration = remainingQuantity/noOfAdministration;
		                    		var startDat = moment(item.startDateNew).toDate();
		                    		var newEndDate =  moment(startDat).add(duration-1, 'days').toDate();
		                    		 var calnewEndDate = moment(newEndDate).format('YYYY-MM-DD');
		                    		 item.remainingQuantity = calnewEndDate;
		                    		 if(date > calnewEndDate){
		                    			 self.model.drugAdministratorList.items.splice(index, 1);
		                    			
		                    		 }
	                    	}
	                    	}
                            }
                            if((item.acceptQty != undefined && item.acceptQty !=null && item.acceptQty != 0)
                            		&& (item.returnQuantity != undefined && item.returnQuantity !=null && item.returnQuantity != 0)){

                                var acceptQty = item.acceptQty;
    	                    	var orderQty = item.orderQty;
    	                    	var noOfAdministration = item.noOfAdministration;
    	                    	 var returnQuantity = item.returnQuantity;
    	                    	 
    	                    	 if(returnQuantity != null && returnQuantity != 0){
    	                    		 var acceptQty = 0;
    	                    		 if(item.acceptQty == null || item.acceptQty == undefined){
    	                    			 item.acceptQty = 0;
    	                    		 }
    	                    		 var acceptQty = item.returnQuantity + item.acceptQty;
    	                    	 }
    	                    	if(acceptQty != null){
    	                    	if(item.flag == true){
    	                    	
    	                    		var remainingQuantity = orderQty - acceptQty;
    	                    		item.remainingQuantity = remainingQuantity;
    	                    		var duration = remainingQuantity/noOfAdministration;
    	                    		var tempDur=duration;
    	                    		var dur2=tempDur.toString();
    	                    		var dur3=dur2.split('.');
    	                    		var dur1=dur3[0];
    	                    		var startDat = moment(item.startDateNew).toDate();
    	                    		var newEndDate =  moment(startDat).add(dur1, 'days').toDate();
    	                    		 var calnewEndDate = moment(newEndDate).format('YYYY-MM-DD');
    	                    		 $scope.stopDate = calnewEndDate;
    	                    		 item.remainingQuantity = calnewEndDate;
    	                    		 
    	                    		 if(moment(self.model.date).format('YYYY-MM-DD') == calnewEndDate){
    	                    			 item.time=[];
    	                    			 var stopcount=0;
    	                    			 var date = new Date();
    	 								var time = moment().format("HH:mm");
    	 								var currtime = moment(date).format('DD-MM-YYYY');
    	 								var doseCount=1;
    	                    			
    	                    				angular.forEach(item.doseTime, function(d){
    	                    					if(remainingQuantity >= doseCount){
    	                    						if((moment(item.startDate).format('YYYY-MM-DD') == moment(self.model.date).format('YYYY-MM-DD'))
    	                    								){ 
//    	                    							&& (moment($scope.stopDate).format('YYYY-MM-DD') != moment(self.model.date).format('YYYY-MM-DD'))
    	    										/*if(d.administrationTime > item.startTime){
    	    											item.time.push(d);
    	    										}*/
//    	                    							if(time <= d.administrationTime){
        	                    							/*if(d.administrationTime > item.startTime){
            	    											item.time.push(d);
            	    											doseCount++;
            	    										}*/
    	                    							
            	                    							if(d.administrationTime > item.startTime){
                	    											item.time.push(d);
                	    											doseCount++;
                	    										
            	                    						}
    	                    							/*stopcount++;
    		    										if(item.doseTime.length >= stopcount){
    		    											item.time.push(d);
    		    										}*/
//        	                    						}
    	    									}
    	    									else if(moment($scope.stopDate).format('YYYY-MM-DD') == moment(self.model.date).format('YYYY-MM-DD')){     
    	    										/*if(d.administrationTime < item.startTime){
    	    											item.time.push(d);
    	    											doseCount++;
    	    										}*/
//    	    										if(time <= d.administrationTime){
//    	                    							if(d.administrationTime > item.startTime){
//        	    											item.time.push(d);
//        	    											doseCount++;
//        	    										}
    	                    							
        	                    							/*if(d.administrationTime < item.startTime){
            	    											item.time.push(d);
            	    											doseCount++;
            	    										
        	                    						}*/
    	    											stopcount++;
    		    										if(item.doseTime.length >= stopcount){
    		    											item.time.push(d);
    		    										}
//    	                    						}
    	    									}
    	    									else{
    	    										item.time.push(d);
    	    										doseCount++;
    	    									}
    	                    					}
   	    									

    	    									//doseCount++;
    	    									});	
    	                    				 var doseTime = item.time;
    	                                     item.doseTimeArray = [];
    	                                     
    	                                     for (var i = 0; i <= 23; i++) {
    	                                         item.doseTimeArray.push({})
    	                                     }
    	                                     doseTime.forEach(function (data) {
    	                                     	data['currentTime'] = moment().format("HH:mm");
    	                                         data['convertedTime'] = data.administrationTime;
    	                                   	    data['actualAdministrationTime'] = data.actualAdministrationTime;
    	                                         data['preBufferAdministrationTimeCheck'] = data.preBufferAdministrationTime;
    	                                         data['postBufferAdministrationTimeCheck'] = data.postBufferAdministrationTime;
    	                                         data['batchNo'] = data.batchNo;
    	                                         data['batchId'] = data.batchId;
    	                                         item.doseTimeArray[(parseInt(data['convertedTime']))] = doseTime.find(function (x) {
    	                                             return x.convertedTime == data['convertedTime'];
    	                                         });
    	                                     });
    	                                     
    	                                     if(moment(self.model.date).format('YYYY-MM-DD')  >= calnewEndDate && item.orderQty == item.returnQuantity){
    	    	                    			 
    	     	                    			// self.model.drugAdministratorList.items.splice(index, 1);
    	     	                    			 $scope.dummyList.push(index);
    	     	                    		 }else{
    	                                             $scope.tempNewDataList.push(item);
    	     	                    		 }
    	                    		 }else{
    	                    			 if(parseInt(dur1) == 0){
    	                    				 item.time=[];
        	                    			 var stopcount=1;
        	                    			 var date = new Date();
        	 								var time = moment().format("HH:mm");
        	 								var currtime = moment(date).format('DD-MM-YYYY');
        	 								var doseCount=1;
        	                    			
        	                    				 angular.forEach(item.doseTime, function(d){
        	                    					 if(remainingQuantity > doseCount){
        	                    					 doseCount++;
     		    										if(item.doseTime.length >= 1){
     		    											item.time.push(d);
     		    										}
//         	                    					}
        	                    					 }
         	    									});	
        	                    			 
        	                    				
        	                    				 var doseTime = item.time;
        	                                     item.doseTimeArray = [];
        	                                     
        	                                     for (var i = 0; i <= 23; i++) {
        	                                         item.doseTimeArray.push({})
        	                                     }
        	                                     doseTime.forEach(function (data) {
        	                                     	data['currentTime'] = moment().format("HH:mm");
        	                                         data['convertedTime'] = data.administrationTime;
        	                                   	    data['actualAdministrationTime'] = data.actualAdministrationTime;
        	                                         data['preBufferAdministrationTimeCheck'] = data.preBufferAdministrationTime;
        	                                         data['postBufferAdministrationTimeCheck'] = data.postBufferAdministrationTime;
        	                                         data['batchNo'] = data.batchNo;
        	                                         data['batchId'] = data.batchId;
        	                                         item.doseTimeArray[(parseInt(data['convertedTime']))] = doseTime.find(function (x) {
        	                                             return x.convertedTime == data['convertedTime'];
        	                                         });
        	                                     });
        	                                     
        	                                     if(moment(self.model.date).format('YYYY-MM-DD')  >= calnewEndDate && item.orderQty == item.returnQuantity){
        	    	                    			 
        	     	                    			 $scope.dummyList.push(index);
        	     	                    		 }else{
        	                                             $scope.tempNewDataList.push(item);
        	     	                    		 }
    	                    			 }
    	                    			 
    	                    		 }
    	                    		 
    	                    		 
    	                    		 
    	                    		 
    	                    	}else{
    		                    		var remainingQuantity = orderQty - acceptQty;
    		                    		item.remainingQuantity = remainingQuantity;
    		                    		var duration = remainingQuantity/noOfAdministration;
    		                    		var startDat = moment(item.startDateNew).toDate();
    		                    		var newEndDate =  moment(startDat).add(duration-1, 'days').toDate();
    		                    		 var calnewEndDate = moment(newEndDate).format('YYYY-MM-DD');
    		                    		 item.remainingQuantity = calnewEndDate;
    		                    		 if(date > calnewEndDate){
    		                    			 self.model.drugAdministratorList.items.splice(index, 1);
    		                    			
    		                    		 }
    	                    	}
    	                    	}
                                
                            }else{
                            	$scope.tempNewDataList = self.model.drugAdministratorList.items;
                            }
                            
                            if(intCount == $scope.tempList.items.length){
                            	self.model.drugAdministratorList.items=$scope.tempNewDataList;
                            	
                            	  self.model.demolist = []
                                  self.model.paretItemIds = [];

                                  self.model.drugAdministratorList.items.forEach(function (item ,ind) {
                                      if(item.isSubstitute == true){
          								var arr = {};
          								arr.id = item.id,
          								arr.parentSubstituteId = item.parentSubstituteId,
          								arr.duration = item.duration ,
          								$scope.parentSubstituteId = item.parentSubstituteId,
          								arr.indexSub =ind,
          								arr.index = self.model.drugAdministratorList.items.findIndex(record => record.id == item.parentSubstituteId)
          								self.model.paretItemIds.push(arr);
          								console.log("paretItemIds " , self.model.paretItemIds);
                                      }
                                  });
                                      if(self.model.paretItemIds.length > 0){
          								self.model.demolist = self.model.drugAdministratorList;
          								self.model.demolist1 = self.model.drugAdministratorList;

          								self.model.demolist.items.forEach(function (vaule1,index1) {
          									$scope.count = 1;
          										self.model.paretItemIds.forEach(function (value,ind) {
          											if(value.parentSubstituteId == vaule1.id){
          													$scope.count++;
          												vaule1.count = $scope.count;
          												}
          										});
                                          });

                                  self.model.paretItemIds.forEach(function (value,ind) {
          							self.model.demolist.items.forEach(function (vaule1,index1) {
          								if(value.parentSubstituteId == vaule1.id){
          									var count = vaule1.duration / vaule1.count;
          									var diff = moment( $scope.D1).diff(moment(vaule1.startDateNew), 'days');
          									if(diff < count){
          										var indexk = self.model.demolist1.items.findIndex(record => record.id == value.id)
          										self.model.drugAdministratorList.items.splice(indexk, 1);
          									}else{
          										 var indexNew = self.model.drugAdministratorList.items.findIndex(record => record.id == value.parentSubstituteId)
          										    self.model.drugAdministratorList.items.splice(indexNew, 1);
          									}

          								}
          							});
                                  });
                                  }else{
                                	  self.model.testlist = [];
                                	  self.model.testlist = self.model.drugAdministratorList.items;
                                	  self.model.drugAdministratorList.items = [];
                                	  self.model.testlist.forEach(function (vaule1,index1) {
                                			  var found = self.model.drugAdministratorList.items.find(ar=>ar.id == vaule1.id);
                                			  if(vaule1.doseTimeArray != undefined){
                                				  if(found == undefined){
      												self.model.drugAdministratorList.items.push(vaule1);
      											} 
                                			  }
                                	  });
                                	  self.model.testlist.forEach(function (vaule1,index1) { 
                                	  if(vaule1.orderQty == vaule1.returnQuantity){
	     	                    			 self.model.drugAdministratorList.items.splice(index1, 1);
	     	                    		 }
                                	  });
                                  }
                                  
                                     
                                  			
                                  	
                                  
                            }
                            intCount++;
                        });
                        
                       
                
                        
                      
                        console.log("drugData",self.model.drugAdministratorList);
                        $rootScope.stopLoader();
                        self.model.admissionIdFromService =undefined;
                        self.model.uhidFromService =undefined;
                    })
            }
        }
        var PopulateDropdown = function () {

            var URI = CONSTANTS.PHARMACY.NURSING.DRUG_ADMINISTRATOR.REASON_DROPDOWN_API + "/" + $scope.organizationId;

            self.model.reason = self.model.reasonList[0];

            $rootScope.startLoader();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
                    self.model.reasonList = self.model.reasonList.concat(response.data.reasons);
                    $rootScope.stopLoader();
                });
        }

       
        var getBatchDetails=function(id){
        	self.model.batchDetailList = [];
        	var URI = BASE_URL + ROOT_URL+ "/api/pharmacy/nursing/workstation/drug/administration/getBatchDetailsByPresDetailsId"+ "/" + id;

        	 $rootScope.startLoaderPopup();
            GenericService.serviceAction("GET", URI, {}).then(
                function (response) {
               	 self.model.batchDetailList = response.data.listObject;
               	 if(self.model.batchDetailList !=null && self.model.batchDetailList.length > 0 &&  self.model.actualAdministrationTime==undefined){
               		 
               		 self.model.batch=self.model.batchDetailList[0].batchId;
               	 }
//                    $rootScope.stopLoader();
                });
        	
        }
        $scope.setTime = function (){
        	
        	var time = moment().format("HH:mm");
        	var admiTime = self.model.newAdministrationTime;
        	var startTime = self.model.startAdministrationTime;
        	var startDate = self.model.startAdministrationDate;
        	var stopTime = self.model.stopAdministrationTime;
        	var stopDate = self.model.stopAdministrationDate;
        	
        	if(time < admiTime){
        		growl.warning("Administration time can not be greater than current time.",
						{
							title : 'Warning!'
						});
        		self.model.newAdministrationTime = time;
        		self.model.startAdministrationTime = startTime;
        		self.model.startAdministrationDate = startDate;
        		self.model.stopAdministrationTime = stopTime;
        		self.model.stopAdministrationDate = stopDate;
				return false;
        	}
        	
        }
        
    
        
        var OpenPopUp = function (item, doseData, selectedDate) {
        	
        	if(item.antibioticTest == true){
        	        $scope.checkObservation(item, doseData, selectedDate);
        	}else{
        		$scope.getAdmindose(item, doseData, selectedDate);
        	}
        	
        }

        $scope.getAdmindose = function(item, doseData, selectedDate){
        	
        	self.model.startAdministrationDate=new Date();
        	self.model.stopAdministrationDate=new Date();
        	$scope.checkInfusion=false;
        	if(item.infusionSolutionId != undefined && item.infusionSolutionId != null && item.infusionSolutionId != ""){
        		$scope.checkInfusion=true;
        		$scope.getadditivelist(item);
        	}
        	getBatchDetails(item.id);
        	 $timeout( function(){
            self.model.actualAdministrationTime = doseData.actualAdministrationTime;
            self.model.newAdministrationTime = doseData.actualAdministrationTime;
            self.model.note = doseData.note;
            if(self.model.actualAdministrationTime != undefined && self.model.actualAdministrationTime != null){
				$scope.saveBtnDisabled = true;
			}
            self.model.reason = self.model.reasonList.find(function (reason) {
                return reason.id == doseData.reasonId;
            })
            
            if (self.model.batchDetailList.length > 1) {
				var count = item.duration
						/ self.model.batchDetailList.length;
				
				var diff = moment($scope.D1).diff(
						moment(item.startDateNew), 'days');

				if (diff < count) {
					self.model.test = self.model.batchDetailList;
					self.model.batchDetailList = []
					angular.forEach(self.model.test, function(
							value, index) {
						if (index == 0) {
							self.model.batchDetailList
									.push(self.model.test[0]);
						}
					});
				} else {
					self.model.test = self.model.batchDetailList;
					self.model.batchDetailList = []
					angular.forEach(self.model.test, function(
							value, index) {
						if (index == 1) {
							self.model.batchDetailList
									.push(self.model.test[1]);
						}
					});
				}
			}
            
            self.model.batch = self.model.batchDetailList.find(function (batch) {
                return batch.batchId == doseData.batchId;
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
                    $rootScope.startLoaderPopup();
                } else {
                    var popup = angular.element('#checkTabletStatus');
                    popup.modal('show');
                    $rootScope.startLoaderPopup();
                }
                AddPopUpInfo(item, doseData);
            }else{
            	 if(item.statFlag){
            		 var popup = angular.element('#checkTabletStatus');
                     popup.modal('show');
                     $rootScope.startLoaderPopup();
                     AddPopUpInfo(item, doseData);
            	 }
            }
           $rootScope.stopLoaderPopup();
           
        },2000);
        
        }
        
        
        $scope.checkObservation = function(item, doseData, selectedDate){
        	
			
			var data ={
		    		
		    		 "saleDetailsId": item.saleDetailsId,
		    		 "admissionId": self.model.admissionId,
					  
		    };
			var URI3 = BASE_URL + ROOT_URL+ "/api/pharmacy/nursing/workstation/drug/administration/checkObservation";
		    
			GenericService.serviceAction("POST", URI3, data).then(function(response) {
				
						
						if(response.data.listObject != null && response.data.listObject.length>0){
							$scope.getAdmindose(item, doseData, selectedDate);
						}else{
							 growl.warning("Please complete Test Dose Administration Result.",
		    							{
		    								title : 'Warning!'
		    							});
						}
				
					});
			$rootScope.stopLoader();
			
		
        
        }
        
        
        
        
        var AddPopUpInfo = function (item, doseData) {
            self.model.selectedItem = item;
            self.model.selectedDoseData = doseData;
            item['isPrnFlagSet'] = item.prnFlag;
            $rootScope.stopLoaderPopup();
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

        
        $scope.validateintakeDates=function(){
        	if(
        			(moment(self.model.startAdministrationDate).format("YYYY-MM-DD") !=undefined 
        			&& moment(self.model.startAdministrationDate).format("YYYY-MM-DD") !=null)
        			
        			&& (moment(self.model.stopAdministrationDate).format("YYYY-MM-DD") !=undefined 
        					&& moment(self.model.stopAdministrationDate).format("YYYY-MM-DD") !=null)
        					
        			&& (moment(self.model.stopAdministrationTime).format("HH:mm")  !=undefined 
        					&& moment(self.model.stopAdministrationTime).format("HH:mm") !=null)
        					
        			&& (moment(self.model.startAdministrationTime).format("HH:mm")  !=undefined 
        					&& (moment(self.model.startAdministrationTime).format("HH:mm") !=null)
        					
        					)){
        		if(moment(self.model.stopAdministrationDate).format("YYYY-MM-DD") < moment(self.model.startAdministrationDate).format("YYYY-MM-DD"))
    			{
    				 growl.warning("Stop date shoulde be greater than start date",
    							{
    								title : 'Warning!'
    							});
    					$scope.intakeTime = time;
    					return false;
    			 
    			}
        		if(self.model.startAdministrationTime > self.model.stopAdministrationTime)
    			{
    				 growl.warning("Start time shoulde be less than stop time",
    							{
    								title : 'Warning!'
    							});
    					$scope.intakeTime = time;
    					return false;
    			 
    			}
        	}
        	
        }
        
		$scope.saveBtnDisabled = false;
        var SaveDrugAdministrator = function () {
        	
        	
			$scope.saveBtnDisabled = true;
			
			
			self.model.actualAdministrationTime = self.model.newAdministrationTime;
            self.model.selectedItem.newAdministrationTime = self.model.newAdministrationTime;
            self.model.selectedItem.startAdministrationTime = self.model.startAdministrationTime!=undefined ? self.model.startAdministrationTime+":00" : moment(new Date()).format("HH:mm:ss");
            self.model.selectedItem.stopAdministrationTime = self.model.stopAdministrationTime!=undefined ? self.model.stopAdministrationTime+":00" : moment(new Date()).format("HH:mm:ss");
            self.model.selectedItem.startAdministrationDate = moment(self.model.startAdministrationDate).format("DD-MM-YYYY HH:mm:ss");
            self.model.selectedItem.stopAdministrationDate =  moment(self.model.stopAdministrationDate).format("DD-MM-YYYY HH:mm:ss");
            self.model.selectedItem.frequencyDetailId = self.model.selectedDoseData.id;
            self.model.selectedItem.note = self.model.note;
            self.model.selectedItem.reason = self.model.reason;
            if(self.model.newAdministrationTime != undefined){
            	 self.model.selectedDoseData.newAdministrationTime = getMilliseconds(self.model.newAdministrationTime);
            }
           
            // self.model.selectedDoseData.actualAdministrationTime = (new Date(moment(new Date()).format('YYYY-MM-DD') + ' ' + self.model.newAdministrationTime + ':00'.replace('-', '/'))).getTime();

            if (ValidateBill()) {
				$scope.saveBtnDisabled = true;
				self.model.actualAdministrationTime = self.model.newAdministrationTime;
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
                    "startAdministrationTime": self.model.selectedItem.startAdministrationTime,
                    "stopAdministrationTime": self.model.selectedItem.stopAdministrationTime,
                    "startAdministrationDate": self.model.selectedItem.startAdministrationDate,
                    "stopAdministrationDate": self.model.selectedItem.stopAdministrationDate,
                    "addedDate": moment(self.model.date).format('YYYY-MM-DD'),
                    "note": self.model.selectedItem.note,
                    "reasonId": self.model.selectedItem.reason.id,
                    "batchId": self.model.batch!=undefined ? self.model.batch.batchId : null
                }
                data.items.push(item);
                if($scope.getsaveadditivelist!=null && $scope.getsaveadditivelist !=undefined && $scope.getsaveadditivelist.length > 0){
                	angular.forEach($scope.getsaveadditivelist,function(value,key){
                		var item = {
                                "prescriptionDetailId": value.id,
                                "frequencyDetailId": self.model.selectedItem.frequencyDetailId,
                                "administrationTime": self.model.selectedItem.newAdministrationTime,
                                "startAdministrationTime": self.model.selectedItem.startAdministrationTime,
                                "stopAdministrationTime": self.model.selectedItem.stopAdministrationTime,
                                "startAdministrationDate": self.model.selectedItem.startAdministrationDate,
                                "stopAdministrationDate": self.model.selectedItem.stopAdministrationDate,
                                "addedDate": moment(self.model.date).format('YYYY-MM-DD'),
                                "note": self.model.selectedItem.note,
                                "reasonId": self.model.selectedItem.reason.id,
                                "batchId": value.batchId
                            }
                            data.items.push(item);
                	});
                }
                

                $rootScope.startLoader();
                GenericService.serviceAction("POST", URI, data).then(
                    function (response) {
                    	if(self.model.newAdministrationTime != undefined){
                            self.model.selectedDoseData.actualAdministrationTime = getMilliseconds(self.model.newAdministrationTime);
                    	}
                        
                        //For intake output chart
                        
                        
                    	var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						
						var data={
								
									"intakeDate":self.model.selectedItem.startAdministrationDate,
									"intakeTime":self.model.selectedItem.startAdministrationTime!=undefined ? self.model.selectedItem.startAdministrationTime :null,
									"outputDate":$scope.dt,
									"outputTime":$scope.outputTime!=undefined ?$scope.outputTime :null,
									"patientId": self.model.drugAdministratorList.patientId,
					                "admissionId": self.model.drugAdministratorList.admissionId,
					                "encounterId": self.model.drugAdministratorList.encounterId,
									"unitId":$scope.unitId,
							        "organizationId":$scope.organizationId,
									"userId":$scope.userId,
									"totalIntake":$scope.totalIntakeQuantity,
									"totalOutput":$scope.totalOutPutQuantity,
									"status":"A",
									"createdBy":$scope.createdBy,
									"createdDate":$scope.dt,
									"updatedBy":$scope.updatedBy,
									"updatedDate":$scope.dt,
									"balance": $scope.balance,
									"isIoStart" : true
						}
						
						$scope.intakeListForMorningList=[];
						
						
						
//					angular.forEach($scope.intakeListForMorning, function(value , key) {
							 var dData = {

									 startIntakeDate : self.model.selectedItem.stopAdministrationDate,
									 startIntakeTime:self.model.selectedItem.stopAdministrationTime,
									 intakeFluidId:2,
									 intakeTypeFluidId:self.model.selectedItem.itemId,
									 startIntakeQuantity : self.model.selectedItem.infusionTolalVolume,
									 totalIntake:self.model.selectedItem.infusionTolalVolume,
									 shiftId:null,
									 status:"A",
									 unitId:$scope.unitId,
							         organizationId:$scope.organizationId,
									 createdBy:$scope.createdBy,
									 createdDate:$scope.dt,
									 updatedBy:$scope.updatedBy,
									 updatedDate:$scope.dt,
				 				}
							 if(dData.intakeFluidId!=undefined && dData.intakeFluidId!=""){
				 				$scope.intakeListForMorningList.push(dData);
							 }
							
							 if($scope.getsaveadditivelist!=null && $scope.getsaveadditivelist !=undefined && $scope.getsaveadditivelist.length > 0){
				                	angular.forEach($scope.getsaveadditivelist,function(value,key){
				                		 var dData = {

												 startIntakeDate : self.model.selectedItem.stopAdministrationDate,
												 startIntakeTime:self.model.selectedItem.stopAdministrationTime,
												 intakeFluidId:2,
												 intakeTypeFluidId:value.itemId,
												 startIntakeQuantity : self.model.selectedItem.infusionTolalVolume,
												 totalIntake:self.model.selectedItem.infusionTolalVolume,
												 shiftId:null,
												 status:"A",
												 unitId:$scope.unitId,
										         organizationId:$scope.organizationId,
												 createdBy:$scope.createdBy,
												 createdDate:$scope.dt,
												 updatedBy:$scope.updatedBy,
												 updatedDate:$scope.dt,
							 				}
				                		 if(dData.intakeFluidId!=undefined && dData.intakeFluidId!=""){
								 				$scope.intakeListForMorningList.push(dData);
											 }
				                	});
				                }
								
						data.intakeOutputChartMasterDtoList = $scope.intakeListForMorningList;
							
//					});
					
					if(data.intakeOutputChartMasterDtoList.length != 0){
						$scope.saveBtnDisabled = true;
						var URI = BASE_URL + ROOT_URL+ "/api/opd/saveIntakeOutputChart";
						console.log("Save-URI-", URI);
						console.log(" save OBJ", data);

						GenericService.serviceAction("POST", URI,data).then(function(response) {

							if (response.data.status == 'success') {
//								growl.success(response.data.message,
//										{
//											title : 'Success!'
//										});

//								 $scope.init();
								$scope.startFlag=false;
//							    $scope.getListIntakOutputChart();
							    $scope.totalIntakeQuantity=0;
							    $scope.totalOutPutQuantity=0;
								$scope.saveBtnDisabled = false;
							    $rootScope.stopLoader();
							    } else if (response.data.status == 'error') {
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
								$scope.saveBtnDisabled = false;
								$rootScope.stopLoader();
							   }
						});
					} 
                        growl.success('Drug administration successfully saved');/*Drug administrator Successfully saved*/
                        var popup = angular.element('#checkSOSTabletstatus');
                        popup.modal('hide');
                        var popup = angular.element('#checkTabletStatus');
                        popup.modal('hide');
                        ClearFields();
						$scope.saveBtnDisabled = false;
                        GetDrugAdministrator();
                        $rootScope.stopLoader();
                    },
                    function (err) {
                        $rootScope.stopLoader();
						$scope.saveBtnDisabled = false;
                        growl.error('Error while saving drug administrator');
                    });
            }else{
            	$scope.saveBtnDisabled = false;
            }
        }

        var ClearFields = function () {
            self.model.isAdministrationTimeValid = false;
            self.model.isReasonIdValid = false;
            self.model.isNoteValid = false;
        }

        var ValidateBill = function (itemList) {
            var isValid = true;

//            if (!self.model.selectedItem.newAdministrationTime) {
//                self.model.isAdministrationTimeValid = true;
//                isValid = false;
//                growl.error("Please select time");
//            }
			$scope.saveBtnDisabled = true;
            if (!self.model.selectedItem.isPrnFlagSet) {
                if (!(self.model.selectedDoseData.preBufferAdministrationTime < self.model.selectedDoseData.newAdministrationTime && self.model.selectedDoseData.newAdministrationTime < self.model.selectedDoseData.postBufferAdministrationTime)) {
                    if (!self.model.selectedItem.reason.id) {
                        self.model.isReasonIdValid = true;
                        isValid = false;
                        growl.error("Please select reason");
						$scope.saveBtnDisabled = false;
                    }
                }
            }
            if(self.model.selectedItem.stopAdministrationTime == "undefined:00")
    		{
    		    self.model.selectedItem.stopAdministrationTime ="";
		    		growl.error("Please select end time");
		       	 isValid = false;
		       	 $scope.saveBtnDisabled = false;
    		}
            if($scope.checkInfusion==false)
            	{
            	if(self.model.newAdministrationTime==undefined || self.model.newAdministrationTime==null || self.model.newAdministrationTime=="")
            		{
            		growl.error("Please select time");
               	    isValid = false;
					$scope.saveBtnDisabled = false;
            		}
            	}
            else
            	{
            	 if(self.model.startAdministrationTime ==undefined || self.model.startAdministrationTime==null ||self.model.startAdministrationTime==""){
                 	growl.error("Please select start time");
                 	 isValid = false;
                 	 $scope.saveBtnDisabled = false;
                 }
            if(self.model.stopAdministrationTime ==null || self.model.stopAdministrationTime==undefined ||self.model.stopAdministrationTime=="" || self.model.stopAdministrationTime =="undefined:00"){
            	growl.error("Please select end time");
            	 isValid = false;
            	 $scope.saveBtnDisabled = false;
            }
        }
            if (!!self.model.selectedItem.isPrnFlagSet) {
                if (!self.model.selectedItem.note) {
                    self.model.isNoteValid = true;
                    isValid = false;
                    growl.error("Please enter note");
					$scope.saveBtnDisabled = false;
                }
            }
            
            if(self.model.batchDetailList !=null && self.model.batchDetailList.length > 0 && self.model.batch == undefined){
				growl.error("Please select batch.");
				 isValid = false;
				 $scope.saveBtnDisabled = false;
            }
            return isValid;
        }


        var searchPatient = function () {
        	if(self.model.patientSearchObj.patientName!=null && self.model.patientSearchObj.patientName.length < 3)
    		{
    			growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
    			self.model.patientList = [];
    			return false;
    		}	
        	
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
                    if(response.data.listObject==null){
        				growl.warning(response.data.message,{
        					title : ERROR_MSG
        				});
        				self.model.patientSearchObj.birthDate="";
        				 self.model.patientList=[];
        				return false;
        			}
                  
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
                        self.model.patientSearchObj.birthDate="";
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
            self.model.genderId = patient[0].genderId;
            self.model.admissionIdFromService =undefined;
            self.model.uhidFromService =undefined;
            if(self.model.patientId){
                $rootScope.allergydata(patient[0]);
                getAllPatientDiagnosisForIcon( self.model.patientId);
            }
            
            
        }
        $scope.patientId = null;
        var getPatientDetail = function (patient) {
            self.model.admissionId = patient.admissionId ? patient.admissionId : undefined;
            self.model.age = patient.age ? patient.age : undefined;
            self.model.patientDetail = patient.patientName ? patient.patientName : undefined;
            self.model.uhIdNumber = patient.uhIdNumber ? patient.uhIdNumber : undefined;
            self.model.dob = patient.birthDate? patient.birthDate : undefined;
            self.model.doa = patient.doa ? patient.doa : undefined;
            self.model.pdd = patient.pdd ? patient.pdd : undefined;
            self.model.wardName = patient.wardName ? patient.wardName : undefined;
            self.model.bedNo = patient.bedNumber ? patient.bedNumber : undefined;
            self.model.patientProfile = patient.patientProfile;
            self.model.genderId = patient.genderId;
            $scope.patientId = patient.patientId;
           // self.model.admissionId = undefined;
            self.model.admissionIdFromService =undefined;
            self.model.uhidFromService =undefined;
            if(patient.patientId){
                $rootScope.allergydata(patient);
                getAllPatientDiagnosisForIcon(patient.patientId);
                GetDrugAdministrator();
            }
            $scope.doctorDetail = patient.doctorName ? patient.doctorName : undefined;
            
        }
        
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
    	
    	$scope.getadditivelist= function(info){
			
			
			var data ={
		    		
		    		 "organizationId": $scope.organizationId,
					  "unitId": $scope.unitId,
					      "infusionSolutionId":info.infusionSolutionId
		    };
			var URI3 = BASE_URL + ROOT_URL+ "/api/opd/getAdditiveListById";
		    
			GenericService.serviceAction("POST", URI3, data).then(function(response) {
				
						$rootScope.stopLoader();
						$scope.getsaveadditivelist = [];
						$scope.getsaveadditivelist = response.data.listObject;
						angular.forEach($scope.getsaveadditivelist,function(value,key){
							$timeout( function(){
							var URI = BASE_URL + ROOT_URL+ "/api/pharmacy/nursing/workstation/drug/administration/getBatchDetailsByPresDetailsId"+ "/" + value.id;

				          //  $rootScope.startLoader();
				            GenericService.serviceAction("GET", URI, {}).then(
				                function (response) {
				                 self.model.batchDetailList1 = [];
				               	 self.model.batchDetailList1 = response.data.listObject;
				               	 if(self.model.batchDetailList1 !=null && self.model.batchDetailList1.length > 0){
				               		 
				               		value.batchId = self.model.batchDetailList1[0].batchId;
				               	 }
//				                    $rootScope.stopLoader();
				                });
							 },2000);
						})
						console.log("additivelist",$scope.getsaveadditivelist);

					});
			
			
		}

        initializeController();
        
        
        $scope.initClockPicker1 = function() 
        { 
        	$('.clockpicker').clockpicker({ autoclose: true});
        }
        
        
        $scope.dateOptions =
		{
			formatYear : 'yyyy',
			showWeeks : false,
		};
        
        $scope.open = function() {
			$scope.popup.opened = true;
		};

		$scope.popup =
		{
			opened : false
		};
        
        $scope.dateOptions1 =
		{
			formatYear : 'yyyy',
			showWeeks : false,
		};
        
        $scope.open1 = function() {
			$scope.popup.opened1 = true;
		};

		$scope.popup =
		{
			opened1 : false
		};

		$scope.item = {};
		$scope.saveBtn = true;
        $scope.getAntibioticTest = function(obj){
        	$scope.item = {};
        	$scope.saveBtn = false;
			var data ={
		    		
		    		 "saleDetailsId": obj.saleDetailsId,
		    		 "admissionId" :self.model.admissionId,
					  
		    };
			var URI3 = BASE_URL + ROOT_URL+ "/api/pharmacy/nursing/workstation/drug/administration/getAntibioticTestDose";
		    
			GenericService.serviceAction("POST", URI3, data).then(function(response) {
				
						if(response.data.object == 2){
							
							growl.warning("Already administrator time captured.",
	    							{
	    								title : 'Warning!'
	    							});
	    					return false;
							
						}else {
							
							if(response.data.object == 1){
								$scope.saveBtn = true;
							}
							
							$scope.item = response.data.listObject[0];
							
							if($scope.item.observationDate != undefined && $scope.item.observationDate != null){
				        		$scope.item.observationDate  = moment($scope.item.observationDate,"YYYY-MM-DD").toDate(); 
				        	}
				        	if($scope.item.adminDate != undefined && $scope.item.adminDate != null){
				        		$scope.item.adminDate  =  moment($scope.item.adminDate,"YYYY-MM-DD").toDate(); 
				        	}
				        	if($scope.item.roa != undefined && $scope.item.roa != null){
				        		$scope.item.roa  =  $scope.item.roa.toString();
				        	}
				        	if($scope.item.allergyId != undefined && $scope.item.allergyId != null){
				        		$scope.item.allergyId  =  $scope.item.allergyId.toString();
				        	}
							 
							
							$scope.responseList = [];
							var URI = BASE_URL
							+ ROOT_URL
							+ "/api/opd/getRoaList"
							+ "?unitId="
							+ $scope.unitId
							+ "&orgId="
							+ $scope.organizationId
							+ "&formulationTypeId="
							+ $scope.item.formulationTypeId
							+ "&genericId="
							+ $scope.item.genericId
							+ "&patientId="
							+  $scope.patientId;

					GenericServiceParamHeader
							.serviceAction(METHOD_GET, URI)
							.then(
									function(response) {
										if (response.data.status == "success") {
											if (response.data.listObject.length !== 0) {
												$scope.responseList = response.data.listObject;
												
											}
										}
									});
							angular.element('#antiBioTest').modal('show');
							
						}
						
					});
			$rootScope.stopLoader();
			
		
        }
        
        
        $scope.saveAntibioticPopop =function(flag){
        	
        	if($scope.item.stopAdministration != undefined && $scope.item.stopAdministration == true){
        		 angular.element('#testAntiPopup').modal('show');
        	}else{
        		$scope.saveAntibiotic(flag);
        	}
        	
        }
        
        $scope.saveAntibiotic = function(flag){
        	
        	if($scope.item.adminDate == undefined || $scope.item.adminDate == null || $scope.item.adminDate == ""){
        		growl.warning("Please select admin date ",
						{
							title : 'Warning!'
						});
        		
        		return false;
        	}
        	
        	if($scope.item.adminTime == undefined || $scope.item.adminTime == null || $scope.item.adminTime == ""){
        		growl.warning("Please select admin time ",
						{
							title : 'Warning!'
						});
        		
        		return false;
        	}
        	
        	
        	if(flag == true && $scope.item.antibioticTestDoseId == undefined && $scope.item.antibioticTestDoseId == null){
        		growl.warning("Please save first administration  date and time then result ",
						{
							title : 'Warning!'
						});
        		
        		return false;
        	}
        	
        	if(flag == true && ($scope.item.observationDate == undefined || $scope.item.observationDate == null || $scope.item.observationDate == "" )){
        		growl.warning("Please select observation date ",
						{
							title : 'Warning!'
						});
        		
        		return false;
        	}
        	
        	
        	if(flag == true && ($scope.item.observationTime == undefined || $scope.item.observationTime == null || $scope.item.observationTime == "" )){
        		growl.warning("Please select observation time ",
						{
							title : 'Warning!'
						});
        		
        		return false;
        	}
        	
        	var date = new Date();
			$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
        	$scope.item.unitId = $scope.unitId;
        	$scope.item.organizationId = $scope.organizationId
        	$scope.item.updatedBy = self.model.userId;
        	$scope.item.createdBy = self.model.userId;
        	$scope.item.status = 'A';
        	$scope.item.patientId = $scope.patientId;
        	$scope.item.admissionId = self.model.admissionId;
        	$scope.item.createdDate = $scope.dt;
			$scope.item.updatedDate = $scope.dt;
        	$scope.item.allergyId = $scope.item.allergyId !== undefined && $scope.item.allergyId !== null && $scope.item.allergyId !== "" && !isNaN($scope.item.allergyId) ? $scope.item.allergyId : parseInt($scope.item.allergyId);
        	if($scope.item.observationDate != undefined && $scope.item.observationDate != null){
        		$scope.item.observationDate  =moment($scope.item.observationDate).format('DD-MM-YYYY');  
        	}
        	if($scope.item.adminDate != undefined && $scope.item.adminDate != null){
        		$scope.item.adminDate  = moment($scope.item.adminDate).format('DD-MM-YYYY');  
        	}
        	
        	var URI3 = BASE_URL + ROOT_URL+ "/api/pharmacy/nursing/workstation/drug/administration/saveAntibioticTestDose";
		    
			GenericService.serviceAction("POST", URI3, $scope.item).then(function(response) {
				
							if (response.data.status == "success") {
								
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								 GetDrugAdministrator();
								 var popup = angular.element('#antiBioTest');
						            popup.modal('hide'); 
							}
				

					});
        	
        }

        $scope.getSeverityMaster = function(){

            var data = {};
            var headerObj = {
                unit_id : $scope.unitId,
                organization_id : $scope.organizationId
            };

            var URI = BASE_URL + ROOT_URL + GETSEVERITYMASTER;
               GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,
                     NOTIFICATION_MSG_STATUS_FALSE,data,headerObj)
                 .then(
                    function(response) {
                        console.log(response);
                        if (response.data.status == "success")
                            $scope.severityList = response.data.listObject;
                       });
        }

        $scope.getSeverityMaster();
        
        
    }

    function config($stateProvider) {
        $stateProvider.state("drugAdministrator", {
            url: '/drugAdministrator',
            templateUrl: 'views/Nursing/drug-administrator.html',
            controller: "DrugAdministrator.Controller",
            title: 'Drug Administration',
        	resolve : {

	   			data: function($q, $state, $timeout) {
	   		        var deferred = $q.defer();
	   		
	   		        $timeout(function() {
	   		        	var arr = JSON.parse(localStorage.getItem('permissions'))
	   				 	.modules.filter(function(item){
	   	            	   return (item.id == 49);
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
        .controller("DrugAdministrator.Controller", drugAdministratorController)
})();