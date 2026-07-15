angular
		.module('myApp')
		.controller(
				'bloodIssueAcceptanceWorklistController',
				[
						'$scope',
						'$rootScope',
						'$state',	
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'$stateParams',
						'BillingGenericService',
						'GenericServiceParamHeader',
						'PagerService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams,BillingGenericService,GenericServiceParamHeader,PagerService) {
							
							/**Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.totalNoOfRecords;
							$scope.listCount;
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.mpesaRequired = cookieObject.mpesaRequired;
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
							$scope.dateTimeZone= JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
							
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.fromTimeDoa = moment().format("HH:mm");
//							$scope.startTimeDate= new Date();

							$scope.searchObj={
									
									"fromDate":"",
									"toDate":""
								}; 
							 $scope.search = {
								        doctorName: "",
								        doctorId: "",
								        floorId : 0,
										wardId : 0
								    };
	                            $scope.searchObj.toDate=new Date();
	                            $scope.searchObj.fromDate=new Date();
								
							 //var date = new Date();
							 var dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							 var currentDateNew = new Date().toLocaleString('en-US', { timeZone: $scope.dateTimeZone != undefined ? $scope.dateTimeZone.timeZone : undefined});
                             $scope.datepicker = new Date();
                             $scope.startTimeDate= new Date(dt);
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
							};
							$scope.intraTransfusionMonitoringMapperlist=[];
							$scope.transfusionDetailsObj = {
									bloodComponentDesc:"",
									bloodBagNo:"",
									bloodGroupName:"",
									expiryDate:"",
									patientId:"",
									bloodBagComponenId:"",
									vSiteId:$scope.vSiteId,
									vSiteDecs:$scope.vSiteDecs,
									ivAccessId:$scope.ivAccessId,
									ivAccessDesc:$scope.ivAccessDesc,
									donorPulse:$scope.donorPulse,
									donorTemperature:$scope.donorTemperature,
									donorBPSystolic:$scope.donorBPSystolic,
									donorBpDiastolic:$scope.donorBpDiastolic,
									donarRespirationRate:$scope.donarRespirationRate,
									startTime:$scope.startTime,
									createdBy:$scope.userId,
									updatedBy:$scope.userId,
									organizationId: $scope.organizationId,
									unitId: $scope.unitId,
									intraTransfusionMonitoringMapperlist:[],
									transfusionId:$scope.transfusionId,
									terminationId:$scope.terminationId
							}

							$scope.fromDatepicker = {
									opened : false
							};
				
							$scope.openToDatePicker = function() {
								$scope.toDatepicker.opened = true;
							};

							$scope.openToDatePicker3 = function() {
								$scope.datepicker6.opened = true;
							};
							$scope.openDatePicker = function() {
								$scope.datepicker6.opened = true;
							};

							$scope.toDatepicker = {
									opened : false
							};

							$scope.startDatepicker = {
									opened : false
							};
							
							$scope.datepicker6={
									opened:false
							}

							$scope.initClockPicker = function() {
								$("#transfusionDetails").scroll(function() {
							        var inputField = $("input[id$='time']");
							        var topPos = inputField.offset().top;
							        var leftPos = inputField.offset().left;
							        var inputHeight = inputField.outerHeight();
							        $(".popover").css("top", topPos + inputHeight + 5);
							        $(".popover").css("left", leftPos);
							    });
								
								$('.clockpicker').clockpicker({
								    afterDone: function() {
								        var selectedDate = moment($scope.datepicker, 'DD/MM/YYYY');
								        var selectedTime = moment($scope.fromTimeDoa, 'hh:mm');
								        var todayDate = moment();
								        var currentTime = moment();
								        if (selectedDate.isSame(todayDate, 'day') && selectedTime.isAfter(currentTime)) {
								            growl.warning("Time should be equal or less than the current time.", { title: "WARNING!" });
								            $scope.fromTimeDoa = moment().format('hh:mm');
								            $scope.startTime = $scope.fromTimeDoa;
								            $scope.incubationStartTime = $scope.fromTimeDoa;
								        }
								    }
								});


							    
							}

							$scope.datepicker = {
									opened : false
								};
							var date = new Date();
							$scope.dateOptions = {
								formatYear : 'yyyy',
								showWeeks : false,
								minDate : date.setDate((new Date()).getDate() - 90),
							};
							
							$scope.issuedBagList = [];
						/*	$scope.getIssuedBags = function()
							{
								$rootScope.startLoader();
								var data = {
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										fromDate:moment($scope.searchObj.fromDate).format('YYYY-MM-DD'),
										toDate:moment($scope.searchObj.toDate).format('YYYY-MM-DD')
										
										};
								var URI1 = BASE_URL + ROOT_URL + "/global/bloodBank/getIssueBloodBagOnIssueAcceptanceScreen";
								console.log("URI1", URI1);
								GenericService.serviceAction("POST", URI1,data).then(function(response) {
									if(response.data.status=="success"){
									$rootScope.stopLoader();
									//$scope.issuedBagList = response.data.listObject;
									$scope.issuedBagList = [];
									 angular.forEach(response.data.listObject,function(value,index){
											
											var newArr = {};
											newArr.bloodBagCapacityDesc = value.bloodBagCapacityDesc;
											newArr.bloodBagCapacityId = value.bloodBagCapacityId;
											newArr.bloodBagComponenId = value.bloodBagComponenId;
											newArr.bloodBagIssueAcceptanceId = value.bloodBagIssueAcceptanceId;
											newArr.bloodBagIssueAcceptanceStatusId = "";
											newArr.bloodBagNo = value.bloodBagNo;
											newArr.rejectReasonId = "";
											newArr.note = "";
											newArr.bloodGroupId = value.bloodGroupId;
											newArr.bloodBagTypeDesc = value.bloodBagTypeDesc;
											newArr.bloodBagTypeId = value.bloodBagTypeId;
											newArr.bloodComponentDesc = value.bloodComponentDesc;
											newArr.bloodGroupName = value.bloodGroupName;
											newArr.expiryDate = value.expiryDate;
											newArr.issueBloodBagDetailsId = value.issueBloodBagDetailsId;
											newArr.issueDate = value.issueDate;
											newArr.issueNo = value.issueNo;
											newArr.patientId = value.patientId;
											newArr.patientName = value.patientName;
											newArr.requisitionNumber = value.requisitionNumber;
											newArr.uhid =  value.uhid;
											newArr.imagePath =  value.imagePath;
											newArr.prefixCode =  value.prefixCode;
											newArr.genderCode =  value.genderCode;
											newArr.ageString =  value.ageString;
											newArr.chkValue = false;
												
											$scope.issuedBagList.push(newArr);
										});
									console.log("issuedBagList",$scope.issuedBagList);
									}else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}
								});
								
							}
							
							$scope.getIssuedBags();*/
							

							/**List And Pagination Start**/
							$scope.setNoOfRecords = function() {
								$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							};
							
							$scope.initMasterList = function(organizationId,offset, noOfRecordsPerPage,unitId)
							{
								$rootScope.startLoader();
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;			
								var data = {
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										fromDate:moment($scope.searchObj.fromDate).format('YYYY-MM-DD'),
										toDate:moment($scope.searchObj.toDate).format('YYYY-MM-DD'),
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage,
										};
								var URI1 = BASE_URL + ROOT_URL + "/global/bloodBank/getIssueBloodBagOnIssueAcceptanceScreen";
								console.log("URI1", URI1);
								GenericService.serviceAction("POST", URI1,data).then(function(response) {
									if(response.data.status=="success"){
									$rootScope.stopLoader();
									$scope.listCount = response.data.listObject;
									$scope.issuedBagList = [];
									
									 angular.forEach(response.data.listObject,function(value,index){
											
											var newArr = {};
											newArr.bloodBagCapacityDesc = value.bloodBagCapacityDesc;
											newArr.bloodBagCapacityId = value.bloodBagCapacityId;
											newArr.bloodBagComponenId = value.bloodBagComponenId;
											newArr.bloodBagIssueAcceptanceId = value.bloodBagIssueAcceptanceId;
											newArr.bloodBagIssueAcceptanceStatusId = "";
											newArr.bloodBagNo = value.bloodBagNo;
											newArr.rejectReasonId = "";
											newArr.note = "";
											newArr.bloodGroupId = value.bloodGroupId;
											newArr.bloodBagTypeDesc = value.bloodBagTypeDesc;
											newArr.bloodBagTypeId = value.bloodBagTypeId;
											newArr.bloodComponentDesc = value.bloodComponentDesc;
											newArr.bloodGroupName = value.bloodGroupName;
											newArr.expiryDate = value.expiryDate;
											newArr.issueBloodBagDetailsId = value.issueBloodBagDetailsId;
											newArr.issueDate = value.issueDate;
											newArr.issueNo = value.issueNo;
											newArr.patientId = value.patientId;
											newArr.patientName = value.patientName;
											newArr.requisitionNumber = value.requisitionNumber;
											newArr.uhid =  value.uhid;
											newArr.imagePath =  value.imagePath;
											newArr.prefixCode =  value.prefixCode;
											newArr.genderCode =  value.genderCode;
											newArr.ageString =  value.ageString;
											newArr.chkValue = false;
												
											$scope.issuedBagList.push(newArr);
										});
									 $scope.setPage(1,false);
									console.log("issuedBagList",$scope.issuedBagList);
									$rootScope.stopLoader();
									}else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}
								});
								
							}	
							
							$scope.getAdmittedPatientByFloorWardId = function() 
							{
								$rootScope.startLoader();
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									doctorId : $scope.search.doctorId,
									floorId : $scope.search.floorId,
									wardId : $scope.search.wardId
									
								};
								
								data.doctorId = $scope.search.doctorId != undefined ?$scope.search.doctorId : null;
								data.admissionTypeId = $scope.search.admissionTypeId != undefined ? $scope.search.admissionTypeId :null;
								if($scope.search.admissionDate != undefined){
									data.doa = moment($scope.search.admissionDate).format("YYYY-MM-DD");	
								}
								
								
								var URI = BASE_URL + ROOT_URL
										+ GETADMITTEDPATIENTLISTBYFLOORWARD;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.listOfAdmittedPatientList = [];
													$scope.admittedPatientList = [];
													if (response.data.status == "success") 
													{
//														$scope.admittedPatientList = response.data.listObject;
														$scope.listOfAdmittedPatientList = response.data.listObject;
														$scope.commonListCount = response.data.object;
														$scope.setPage(1,false);
														$scope.totalCountPatient  = response.data.object;
														$rootScope.stopLoader();
													}
													else if (response.data.status == "error") {
														$rootScope.stopLoader();
													}
												})
							}
							
							var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
							
							
							   var URI =  BASE_URL + ROOT_URL+GETACTIVEWARDMASTERLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.wardListForView = [];

													if (response.data.status == "success") {
														$scope.wardListForView = response.data.listObject;
													}
												});
							
								$scope.searchGroupSurgen = function(keyword) {

									var data = {};
									var paramObj = {
										doctor_name : keyword
									};
									var headerObj = {
										unit_id : $scope.unitId,
										organization_id : $scope.organizationId
									}
									console.log(data);

									var URI = BASE_URL + ROOT_URL
											+ GETDOCTORMASTERLISTAUTOFILLSERACH;

									return GenericServiceParamHeader
											.serviceAction(METHOD_GET, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE,
													paramObj, headerObj)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															return response.data.listObject;
													});

								}
								
								
								$scope.patientSearchObj = {
										"uhIdNumber"      : "",
										"patientName"     : "",
										"genderId"        : "",
										"mobileNo"        : "",
										"birthDate"       : "",
										"wardNo"          : "",
										"doctorId"		  : $scope.search.doctorId,
										"doctorName"      : $scope.search.doctorName,
										"patientType"	  :"1",
										"unitId"          :$scope.unitId,
										"organizationId"  :$scope.organizationId
								}
								
								$scope.selectDoctor = function($item, $model, $label, idx) {
									$scope.search.doctorName = $item.doctorName;
									$scope.search.doctorId = $item.doctorId;
									$scope.getAdmittedPatientByFloorWardId();
								}
								
							$scope.finalOtReservationList = [];
						    $scope.loadMoreData = function()
						    {
						        $scope.offset+=200;
						        $scope.loadMoreFlag=true;
						        $scope.searchPatient($scope.patientSearchObj);
						    }
						    $scope.offset=0;
						    $scope.loadMoreFlag=false;
						    $scope.noOfRecordsPerPage=200;
						    $scope.patientList = [];
							$scope.searchPatient = function(patientsearchObj){
								
								if(patientsearchObj.patientId=="" && patientsearchObj.uhIdNumber == "" && patientsearchObj.patientName == "" 
								&& patientsearchObj.genderId == "" && patientsearchObj.mobileNo == ""  && patientsearchObj.wardId == "" 
									&& patientsearchObj.wardNo == "" && $scope.search.doctorId == "")
								{
									growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"});
									return false;
								}
								if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
								{
									growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
									$scope.patientList = [];
									return false;
								}	
								
								$rootScope.startLoader();
						        if($scope.loadMoreFlag==true)
					            {
					            patientsearchObj.offset=$scope.offset;
					            $scope.loadMoreFlag=false;
					            }
					        else
					            {
					            patientsearchObj.offset=0;
					            $scope.loadMoreFlag=false;
					            $scope.patientList = [];
					            }
					        patientsearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;
					        patientsearchObj.doctorId=$scope.search.doctorId;
								var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
								BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.listObject==null){
										growl.warning(response.data.message,{
											title : ERROR_MSG
										});
//										$scope.patientSearchObj.birthDate="";
										return false;
									}
//									$scope.getAdmittedPatientByFloorWardId();
									if(Object.keys(response.data.listObject).length  == 0){
										growl.warning("No Result Found!",{
											title : ERROR_MSG
										});
//										$scope.patientSearchObj.birthDate="";
										return false;
									}else if(patientsearchObj.patientId!=null && patientsearchObj.patientId>0 && (response.data.listObject).length  == 1){
										$scope.patientList.push(response.data.listObject[0]);
										$scope.getPatientDetailsByIndex(0);
									}else{
										 $scope.patientList = [];
										angular.forEach(response.data.listObject, function (value, key) {
										if(value.registrationTypeId != 5 && value.registrationTypeId !=2)
											{
												$scope.patientList.push(value);
											}
									 	});
//										$scope.patientSearchObj.birthDate="";
									}
									
								});
								
							}
							
							$scope.openConfirmationModal = function() {
							    $('#confirmationModal').modal('show');
							};

							
							
							$scope.getIssuedBags = function(organizationId,offset, noOfRecordsPerPage,unitId) {
								$rootScope.startLoader();
								
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;			
								var data = {
										organizationId:$scope.organizationId,
										unitId:$scope.unitId,
										fromDate:moment($scope.searchObj.fromDate).format('YYYY-MM-DD'),
										toDate:moment($scope.searchObj.toDate).format('YYYY-MM-DD'),
										offset:offset,
										noOfRecordsPerPage:noOfRecordsPerPage,
										patientName:$scope.searchObj.patientName 
										};
//								if(data.patientId =="" ||data.patientId == null || data.patientId == undefined)
//								{
//									$rootScope.stopLoader();
//									growl.warning("Please Select patient !!!",{title : "WARNING!"});
//									return false;
//									
//								}
								var URI1 = BASE_URL + ROOT_URL + "/global/bloodBank/getIssueBloodBagOnIssueAcceptanceScreen";
								console.log("URI1", URI1);
								GenericService.serviceAction("POST", URI1,data).then(function(response) {
									if(response.data.status=="success"){
									$rootScope.stopLoader();
									$scope.listCount = response.data.listObject;
									$scope.issuedBagList = [];
									
									 angular.forEach(response.data.listObject,function(value,index){
											
											var newArr = {};
											newArr.bloodBagCapacityDesc = value.bloodBagCapacityDesc;
											newArr.bloodBagCapacityId = value.bloodBagCapacityId;
											newArr.bloodBagComponenId = value.bloodBagComponenId;
											newArr.bloodBagIssueAcceptanceId = value.bloodBagIssueAcceptanceId;
											newArr.bloodBagIssueAcceptanceStatusId = "";
											newArr.bloodBagNo = value.bloodBagNo;
											newArr.rejectReasonId = "";
											newArr.note = "";
											newArr.bloodGroupId = value.bloodGroupId;
											newArr.bloodBagTypeDesc = value.bloodBagTypeDesc;
											newArr.bloodBagTypeId = value.bloodBagTypeId;
											newArr.bloodComponentDesc = value.bloodComponentDesc;
											newArr.bloodGroupName = value.bloodGroupName;
											newArr.expiryDate = value.expiryDate;
											newArr.issueBloodBagDetailsId = value.issueBloodBagDetailsId;
											newArr.issueDate = value.issueDate;
											newArr.issueNo = value.issueNo;
											newArr.patientId = value.patientId;
											newArr.patientName = value.patientName;
											newArr.requisitionNumber = value.requisitionNumber;
											newArr.uhid =  value.uhid;
											newArr.imagePath =  value.imagePath;
											newArr.prefixCode =  value.prefixCode;
											newArr.genderCode =  value.genderCode;
											newArr.ageString =  value.ageString;
											newArr.chkValue = false;
											newArr.saveAsDraftFlag = value.saveAsDraftFlag;
											$scope.saveAsDraftFlag = newArr.saveAsDraftFlag;
											$scope.getStatus();
											$scope.issuedBagList.push(newArr);
										});
									 $scope.finalSave=false;
									 angular.forEach($scope.issuedBagList,function(value,index){
										if(value.saveAsDraftFlag=='N'){
											$scope.acceptedFun(true);
											$scope.finalSave=true;
										}
									 });
									console.log("issuedBagList",$scope.issuedBagList);
									$rootScope.stopLoader();
									}else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
										
									}
								});
								
							}
							$scope.getStatus = function() {
								 
							    if ($scope.saveAsDraftFlag == 'Y') {
							    	$scope.statusForBIA = "In Process";
							        return "In Process";
							    } else if ($scope.saveAsDraftFlag == 'N') {
							    	$scope.statusForBIA = "Completed";
							        return "Completed";
							    } else {
							    	 $scope.statusForBIA = "Pending";
							    }
							};		
							

                            $scope.vSite=[
                             {id:0,name:'Saphenous'},
                             {id:1,name:'Cephalic'},
                             {id:2,name:'External Jugular'},
                             {id:3,name:'Median Cubital'},
                             {id:4,name:'Basilic'}
                            ]

                            $scope.ivAccess=[
                             {id:0,name:'16- 18G'},
                             {id:1,name:'20 - 22 G'},
                             {id:2,name:'Any size'},
                             {id:3,name:'22 - 25 G'},
                             {id:4,name:'Central Venous Access Device'}
                            ]

                            $scope.reasonTermination=[
                             {id:0,name:'Febrile Reaction'},
                             {id:1,name:'Transfusion associated circulatory overload'},
                             {id:2,name:'Allergic Reaction'},
                             {id:3,name:'TRALI'},
                             {id:4,name:'Fatal Hemolysis'}
                            ]

							$scope.pager = {};
							$scope.page;

							$scope.setPage = function(page, flag) {
								if (page < 1 || page > $scope.pager.totalPages)
								{
									return;
								}
								$scope.pager = PagerService.GetPager($scope.listCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
								if (flag)
								{
									$scope.getIssuedBags($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
								}
							}

							$scope.initMasterList($scope.organizationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							
							/**List And Pagination End**/
							
							
							$scope.reasonList = [];
							$scope.getReasonMasterList = function()
							{
								var data={
										organizationId:$scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL+ GET_REASON_MASTER_LIST ;
								GenericService
								.serviceAction(METHOD_POST, URI ,data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.reasonList = response.data.listObject;
												console.log("reasonList ",response.data.listObject);
											}
										});
							}
							
							$scope.clear = function()
							{
								$scope.incubationStartTime = "";
								$scope.pulse = "";
								$scope.bpSystolic = "";
								$scope.bpDiastolic = "";
								$scope.temperature = "";
								$scope.respirationRate = "";
							}
							
							
							$scope.checkTimes = function(incubationStartTime, startTime) {
							    var incubationTimeParts = incubationStartTime.split(':');
							    var startTimeParts = startTime.split(':');

							    var incubationHours = parseInt(incubationTimeParts[0]);
							    var incubationMinutes = parseInt(incubationTimeParts[1]);
							    var startHours = parseInt(startTimeParts[0]);
							    var startMinutes = parseInt(startTimeParts[1]);

							    var incubationTotalMilliseconds = (incubationHours * 60 * 60 * 1000) + (incubationMinutes * 60 * 1000);
							    var startTotalMilliseconds = (startHours * 60 * 60 * 1000) + (startMinutes * 60 * 1000);

							    if (incubationTotalMilliseconds <= startTotalMilliseconds) {
							        growl.warning("Intra Transfusion Monitoring Time should be greater than Start Time");
							        return false;
							    }
							};
							
							$scope.intraTransfusionData=[];
							$scope.add = function() {
								
								if (!$scope.incubationStartTime) {
								    growl.warning('Start Time should not be empty');return false;
								} 
								
								$scope.checkTimes($scope.incubationStartTime,$scope.startTime);
								
								if ($scope.pulse == null || $scope.pulse == undefined || $scope.pulse == "") {
								    growl.warning('Donor Pulse should not be empty');return false;
								} else if ($scope.pulse <= 0) {
								    growl.warning('Donor Pulse should be greater than 0 bpm');return false;
								} else if ($scope.pulse > 200) {
								    growl.warning('Donor Pulse should be less than 200 bpm');return false;
								}
								
								if ($scope.temperature == null || $scope.temperature == undefined || $scope.temperature == "") {
								    growl.warning('Donor Temperature should not be empty');return false;
								} else if ($scope.temperature < 0) {
								    growl.warning('Donor Temperature should be greater than 0 °C');return false;
								} else if ($scope.temperature > 36) {
								    growl.warning('Donor Temperature should be less than 36 °C');return false;
								}
								
								if ($scope.respirationRate == null || $scope.respirationRate == undefined || $scope.respirationRate == "") {
								    growl.warning('Donar Respiration Rate should not be empty');return false;
								} else if ($scope.respirationRate <= 0) {
									growl.warning('Donar Respiration Rate should be greater than 0 bpm');return false;
								} else if ($scope.respirationRate > 20) {
								    growl.warning('Donar Respiration Rate should be less than 20 bpm');return false;
								}
								
								if ($scope.bpSystolic == null || $scope.bpSystolic == undefined || $scope.bpSystolic == "") {
								    growl.warning('Donor BP Systolic should not be empty');return false;
								} else if ($scope.bpSystolic <= 0) {
								    growl.warning('Donor BP Systolic should be greater than 0 mmHg');return false;
								} else if ($scope.bpSystolic > 200) {
								    growl.warning('Donor BP Systolic should be less than 200 mmHg');return false;
								}
								
								if ($scope.bpDiastolic == null || $scope.bpDiastolic == undefined || $scope.bpDiastolic == "") {
								    growl.warning('Donor BP Diastolic should not be empty');return false;
								} else if ($scope.bpDiastolic <= 0) {
								    growl.warning('Donor BP Diastolic should be greater than 0 mmHg');return false;
								} else if ($scope.bpDiastolic > 100) {
								    growl.warning('Donor BP Diastolic should be less than 100 mmHg');return false;
								}
								 
							        var monitoringObj={};
							        monitoringObj.incubationStartTime = $scope.incubationStartTime;
							        monitoringObj.pulse=$scope.pulse;
							        monitoringObj.bpSystolic=$scope.bpSystolic;
							        monitoringObj.bpDiastolic=$scope.bpDiastolic;
							        monitoringObj.temperature  = $scope.temperature;
							        monitoringObj.createdBy=$scope.userId;
							        monitoringObj.respirationRate=$scope.respirationRate,
							        monitoringObj.updatedBy=$scope.userId;
							        monitoringObj.organizationId=$scope.organizationId; 
							        monitoringObj.unitId=$scope.unitId;
								 monitoringObj.status="A";
//							        if($scope.status == "A"){
								$scope.intraTransfusionData.push(monitoringObj);
//							        }
								$scope.clear();
							};
							$scope.removeIntraTransfusionMonitoring = function($index)
                            {
								var deletedEntry = $scope.intraTransfusionData[$index];

								if (deletedEntry.intraTransfusionMonitoringMapperId == null) {
								    $scope.intraTransfusionData.splice($index, 1);
								} else 
								    $scope.intraTransfusionData[$index].status = 'I';
								
                            }
							
							$scope.getReasonMasterList();
							
							$scope.acceptFlag=true;
							$scope.acceptedFlag=false;
							$scope.rejectFlag=true;
							$scope.rejectedFlag=false;
			
							$scope.acceptedFun=function(flag){
								if(flag==true){
									$scope.rejectFlag=false;
									$scope.acceptedFlag=true;
									$scope.acceptFlag=false;
									
								}
									
							};
							$scope.showRejecPopup = function(isAccRej,rejectIndx,flag) {
								
									if(flag==true){
										$scope.acceptedFlag=false;
										$scope.acceptFlag=false;
										$scope.rejectFlag=false;
									    $scope.rejectedFlag=true;
									}

								if(isAccRej==2)
									{
									$scope.rejectIndx = rejectIndx;
									 $("#accepatReject").modal('show');
									}
							}
							$scope.saveasdraftFlag='';
							$scope.bloodBagComponenId = "";
							$scope.selectedOption = "";
							$scope.value = "";
							$scope.site = "";
							$scope.item = "";
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							$scope.saveTransfusionDetails = function(saveasdraftFlag){
								$scope.saveasdraftFlagNew=saveasdraftFlag;
								  $('#confirmationModal').modal('hide');
								  
								if (!$scope.site) {
								     growl.warning('Please select V-Site');
								     return false;
								}
								if (!$scope.item) {
								     growl.warning('Please select IV-Access');
								     return false;
								}
								if ($scope.donorPulse == null || $scope.donorPulse == undefined || $scope.donorPulse == "") {
								    growl.warning('Donor Pulse should not be empty');return false;
								} else if ($scope.donorPulse <= 0) {
								    growl.warning('Donor Pulse should be greater than 0 bpm');return false;
								} else if ($scope.donorPulse > 200) {
								    growl.warning('Donor Pulse should be less than 200 bpm');return false;
								}
								
								if ($scope.donorTemperature == null || $scope.donorTemperature == undefined || $scope.donorTemperature == "") {
								    growl.warning('Donor Temperature should not be empty');return false;
								} else if ($scope.donorTemperature <= 0) {
								    growl.warning('Donor Temperature should be greater than 0 °C');return false;
								} else if ($scope.donorTemperature > 36) {
								    growl.warning('Donor Temperature should be less than 36 °C');return false;
								}

								if ($scope.donorBPSystolic == null || $scope.donorBPSystolic == undefined || $scope.donorBPSystolic == "") {
								    growl.warning('Donor BP Systolic should not be empty');return false;
								} else if ($scope.donorBPSystolic <= 0) {
								    growl.warning('Donor BP Systolic should be greater than 0 mmHg');return false;
								} else if ($scope.donorBPSystolic > 200) {
								    growl.warning('Donor BP Systolic should be less than 200 mmHg');return false;
								}

								if ($scope.donorBpDiastolic == null || $scope.donorBpDiastolic == undefined || $scope.donorBpDiastolic == "") {
								    growl.warning('Donor BP Diastolic should not be empty');return false;
								} else if ($scope.donorBpDiastolic <= 0) {
								    growl.warning('Donor BP Diastolic should be greater than 0 mmHg');return false;
								} else if ($scope.donorBpDiastolic > 100) {
								    growl.warning('Donor BP Diastolic should be less than 100 mmHg');return false;
								}

								if ($scope.donarRespirationRate == null || $scope.donarRespirationRate == undefined || $scope.donarRespirationRate == "") {
								    growl.warning('Donar Respiration Rate should not be empty');return false;
								} else if ($scope.donarRespirationRate <= 0) {
								    growl.warning('Donar Respiration Rate should be greater than 0 bpm');return false;
								} else if ($scope.donarRespirationRate > 20) {
								    growl.warning('Donar Respiration Rate should be less than 20 bpm');return false;
								}

								if (!$scope.startTime) {
								    growl.warning('Start Time should not be empty');return false;
								}
								
								angular.forEach($scope.issuedBagList, function(value) {
									
											$scope.bloodComponentDesc = value.bloodBagCapacityDesc,
											$scope.bloodBagNo=value.bloodBagNo,
											$scope.bloodGroupName = value.bloodGroupName,
											$scope.expiryDate = value.expiryDate,
											$scope.patientId = value.patientId,
											$scope.bloodBagComponenId =value.bloodBagComponenId
								
								});
								var transfusionDetails = {
									vSiteId : parseInt($scope.site),
									ivAccessId : parseInt($scope.item),
									donorPulse : parseFloat($scope.donorPulse),
									donorTemperature : parseFloat($scope.donorTemperature),
									donorBPSystolic : parseFloat($scope.donorBPSystolic),
									donorBpDiastolic : parseFloat($scope.donorBpDiastolic),
									donarRespirationRate : parseInt($scope.donarRespirationRate),
									startTime : $scope.startTime,
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									updatedDate : $scope.dt,
									createdDate : $scope.dt,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									status : 'A',
									intraTransfusionMonitoringMapperlist : angular
											.copy($scope.intraTransfusionData),
									transfusionId : $scope.selectedOption,
									terminationId : parseInt($scope.value),
									bloodComponentDesc : $scope.bloodComponentDesc,
									bloodBagNo : $scope.bloodBagNo,
									bloodGroupName : $scope.bloodGroupName,
									expiryDate : $scope.expiryDate,
									patientId : $scope.patientId,
									bloodBagComponenId : $scope.bloodBagComponenId,
									startDate : $scope.startTimeDate,
									saveAsDraftFlag : saveasdraftFlag
									}; 
								if(transfusionDetails.terminationId==null){
									transfusionDetails.terminationId="";
								}
								 
								var URI = BASE_URL + ROOT_URL + "/api/bloodbank/issueAcceptance/saveTransfusionDetails"
								GenericService.serviceAction("POST", URI,transfusionDetails).then(function(response) {
									if (response.data.status == "success") {
										$rootScope.stopLoader();
										
										growl.success(response.data.message,
												{
													title : 'success!'
												});
										$state.go('bloodIssueAcceptanceWorklist');
										}else if(response.data.status == 'error'){
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
									
								});	
							};
							$scope.getTransfusionDetails = function(){
								angular.forEach($scope.issuedBagList, function(value) {
									
									$scope.patientId = value.patientId
									});
								var URI = BASE_URL
										+ ROOT_URL
										+ "/api/bloodbank/issueAcceptance/getTransfusionDetailsList"
										+ "/" + $scope.patientId + "/"
										+ $scope.organizationId + "/"
										+ $scope.unitId;
								GenericService.serviceAction("GET", URI,"").then(function(response) {
									if (response.data.status == "success") {
										$rootScope.stopLoader();
										          
														var data = response.data.object;
														console.log(data);
														$scope.donorPulse = data.donorPulse;
														$scope.donarRespirationRate = data.donarRespirationRate;
														$scope.donorBPSystolic = data.donorBPSystolic;
														$scope.donorBpDiastolic = data.donorBpDiastolic;
														$scope.donorTemperature = data.donorTemperature;
														$scope.startTimeDate = new Date(data.startDate);
														$scope.startTime = data.startTime;
														$scope.terminationId = data.terminationId;
														$scope.transfusionId = data.transfusionId;
														$scope.site = data.vSiteId.toString();
														$scope.item = data.ivAccessId.toString();
														$scope.startDate = data.startDate;
														$scope.intraTransfusionData = data.intraTransfusionMonitoringMapperlist;
														$scope.incubationStartTime = data.incubationStartTime;
														$scope.pulse = data.pulse;
														$scope.temperature = data.temperature;
														$scope.respirationRate = data.respirationRate;
														$scope.selectedOption = data.transfusionId;
														$scope.value = data.terminationId.toString();
														$scope.bpSystolic = data.bpSystolic;
														$scope.bpDiastolic = data.bpDiastolic;
														$scope.saveAsDraftFlag =data.saveAsDraftFlag;
													
//										console.log("commonList",$scope.bloodBankTransfusionList);
										
										growl.success(response.data.message,
												{
													title : 'success!'
												});
										
										}else if(response.data.status == 'error'){
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
									
								});	
								
							};
							
							$scope.saveIssueAcceptance = function(){
								try{
									$rootScope.startLoader();
									var flag = false;
									$scope.issuedBagFinalList = [];
									 angular.forEach($scope.issuedBagList,function(value,index){
										
											if(value.chkValue){
												 if(value.bloodBagIssueAcceptanceStatusId == ""){
													 flag = true;
												 }
												var newArr = {};
												newArr.acceptedBy = $scope.userId;
												newArr.bloodBagCapacityId = value.bloodBagCapacityId;
												newArr.bloodBagComponenId = value.bloodBagComponenId;
												newArr.bloodGroupId = value.bloodGroupId;
												//newArr.bloodBagIssueAcceptanceId = value.bloodBagIssueAcceptanceId;
												newArr.bloodBagIssueAcceptanceStatusId = value.bloodBagIssueAcceptanceStatusId;
												newArr.bloodBagNo = value.bloodBagNo;
												newArr.bloodBagReasonId = value.rejectReasonId;
												newArr.bloodBagReasonNote = value.note;
												newArr.bloodBagTypeId = value.bloodBagTypeId;
												newArr.createdBy =  $scope.userId;
												newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
											//	newArr.expiryDate = moment(value.expiryDate).format('DD-MM-YYYY HH:mm:ss');
												newArr.issueBloodBagDetailsId = value.issueBloodBagDetailsId;
												newArr.issueDate = $rootScope.getChangedFormatedDateShalse(value.issueDate)+" 00:00:00";
												newArr.issueNo = value.issueNo;
												newArr.organizationId = $scope.organizationId;
												newArr.patientId = value.patientId;
												//newArr.requisitionNo = value.requisitionNo;
												newArr.requisitionNumber = value.requisitionNumber;
												newArr.status = "A";
												newArr.uhid =  value.uhid;
												newArr.unitId = $scope.unitId;
												newArr.updatedBy = $scope.userId;
												newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
													
												$scope.issuedBagFinalList.push(newArr);
											}
											
										});
									console.log("issuedBagFinalList",$scope.issuedBagFinalList);
								
									
								if($scope.issuedBagFinalList.length==0)
								{
									$rootScope.stopLoader();
									growl.warning("Please Select List",
											{
												title : 'warning!'
											});
								}
								else if(flag == true) {
									
									$rootScope.stopLoader();
									growl.warning("Please Select Action of all selected Bags",
											{
												title : 'warning!'
											});
									
								}else{
								
								var URI = BASE_URL + ROOT_URL + "/api/bloodbank/issueAcceptance/saveBloodBagIssueAcceptance";
								GenericService.serviceAction("POST", URI,$scope.issuedBagFinalList).then(function(response) {
									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.getIssuedBags();
										$scope.issuedBagFinalList = [];
										$scope.issuedBagList = [] ;
										growl.success(response.data.message,
												{
													title : 'success!'
												});
										
										
									}else if(response.data.status == 'error'){
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
									
								});
								}
							}catch(e){
								console.log("Exception-",e);
								$rootScope.stopLoader();
							}
								}
								
							
			}]);
							