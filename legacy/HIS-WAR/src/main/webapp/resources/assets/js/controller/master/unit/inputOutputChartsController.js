/**
 * @author sneha dalave
 * @author chetna dhokne
 */

angular
		.module('myApp')
		.controller(
				"inputOutputChartsController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactory',
						'$sessionStorage',
						'growl',
						'$interval',
						'$stateParams',
						'GenericServiceParamHeader',
						'BillingGenericService',
						'$window',
						'reportService',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactory,
								$sessionStorage, growl,$interval,$stateParams,GenericServiceParamHeader,BillingGenericService,$window, reportService) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.admissionObj = {};
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;

							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.createdBy = cookieObject.userId;
							$scope.updatedBy = cookieObject.userId;
							
							$scope.stopAndSaveDisabled = true;
							$scope.startIOFlag = true;
							$scope.stopIOFlag = true;
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
						
							

							
								// function for initializa clockpicker
								$scope.initClockPicker = function() {

									$('.clockpicker').clockpicker();
								}

								$scope.nursingWardList = [];
								var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
								 if(nursingStationsWardsList == undefined){
									 growl.warning(
												'No ward mapped with selected nursing station !!',
												{
													title : 'Success!'
												});
								 }
								 else
								  {
								   console.log('nursingStationsWardsList',nursingStationsWardsList);
								   $scope.nursingWardList =nursingStationsWardsList; 
								  
								  console.log('hshsajsa',$scope.nursingWardList);
								  }
								 
								
								 $scope.addDateOptions =function()
								 {
									 var dateOption = {
												formatYear : 'yyyy',
												showWeeks : false,
												maxDate : new Date( ),
												minDate : new Date( $scope.forOutPutMinDate),
												
											};
								      $scope.dateOptionsVacc.push(dateOption);
								 }
								 $scope.addDateOptionsOutput =function()
								 {
									 var dateOptionOutput = {
												formatYear : 'yyyy',
												showWeeks : false,
												maxDate : new Date(),
												minDate :new Date($scope.setOutputDate)
											};
								      $scope.dateOptionsVaccOutPut.push(dateOptionOutput);
								 }
								 
								 
								 $scope.openDatePickerSearch = function() {
										$scope.datepicker2.opened = true;
										};

										$scope.datepicker2 = {
										opened : false
										};
										
										
								 $scope.init = function(){
										
										$scope.saveBtnFlag = true;
										$scope.updateBtnFlag = false;
										
										$scope.startFlag=true;
										$scope.stopFlag=true;
										
										   // object for Intake
										
										
										   $scope.dateOptionsVacc =[];
										   $scope.addDateOptions();
										   $scope.dateOptionsVaccOutPut =[];
										   $scope.addDateOptionsOutput();
										   $scope.dateOptions = {
													formatYear : 'yyyy',
													showWeeks : false,
													maxDate : new Date(),
												};
											 
									     $scope.intakeListForMorning = [{
									    	 intakeStartDate:"",
									    	 intakeStartTime:"",
									    	 intakeFluidId:"",
									    	 intakeTypeFluidId:"",
									    	 intakeShiftId:"",
									    	 intakeQuantity:"",
									    	 listOfIntakeTypeFluidById:[],
									    	 getListOfActiveShiftForIntake:[]
										}];
									     
									
									     $scope.OutputListFor = [{
									    	 outputStartDate:"",
									    	 outputStartTime:"",
									    	 outputShiftId:"",
									    	 outputTypeId:"",
									    	 outputQuantity:"",
									    	 getListOfActiveShiftForOutput:[]
										}];
									     
									     
							
							
						 }
						$scope.init();
						$scope.initPatient = function()
						{
							$scope.patientDetails = {
									"patientName" : "",
									"genderCode" : "",
									"age" : "",
									"uhIdNumber" : "",
									"dob" : "",
									"doctorFullName" : "",
									"dateOfAdmission" : "",
									"patientDischargeDate" : "",
									"wardName" : "",
									"bedNumber" : "",
									"imagePath":"",
									"patientId":"",
									"visitTypeId":"",
									"admissionId":"",
									"genderId":"",
								}
							$scope.patientSearchObj = {
								      "uhIdNumber"      : "",
								      "patientName"     : "",
								      "genderId"        : "",
								      "mobileNo"        : "",
								      "birthDate"       : "",
								      "identificationNo":"",
								      "patientType"   :"1",
								      "unitId"          :$scope.unitId,
								      "organizationId"  :$scope.organizationId,
								      "wardList" :$scope.nursingWardList
								    }
						}
						$scope.initPatient();
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
							
						
							$scope.getCardTypesList = function(){
								var URI = BASE_URL + ROOT_URL + "/api/billing/cardTypeMaster";
								var param = {orgnisationId:$scope.organizationId,unitId:$scope.unitId};
								BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$scope.cardTypeList = response.data.listObject;
									}
								});
							}
							
							$scope.initPaymentDetailsObj = function(){
								$scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:""});
							}
					   
							$scope.test = function()
							{
								console.log('asefsv',$scope.intakeDate);
								$scope.forOutPutMinDate = moment($scope.intakeDate).format('MM/DD/YYYY');
								console.log('forOutPutMinDate',$scope.forOutPutMinDate);
								 $scope.dateOptionsVacc[0].minDate = new Date($scope.forOutPutMinDate);
								$scope.dateOptionss.minDate =  new Date($scope.forOutPutMinDate);
								$scope.setOutputDate = moment($scope.outputDate).format('MM/DD/YYYY');
								 $scope.dateOptionsVaccOutPut[0].minDate=new Date($scope.forOutPutMinDate);
								
							}
							
							
							$scope.searchPatient = function()
							{
								$rootScope.startLoader();
								$scope.DischargeSummaryFinalList=[];
								$scope.patientTemplateList=[];
								var URI = BASE_URL + ROOT_URL + "/adt/searchListOfPatientsByWardId";
								var patSearchObj = angular.copy($scope.patientSearchObj);
								if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
									var dt = new Date($scope.patientSearchObj.birthDate);
									patSearchObj.birthDate = (dt.getMonth() + 1) + "-" + dt.getDate() + "-" + dt.getFullYear();
								} 
								BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.searchPatientList = [];
									if (response.data.status == "success")
										{
										$scope.patientList = response.data.listObject;
										patSearchObj = {};
										
										$scope.initPaymentDetailsObj();
										$scope.listPaymentDetails = [];
										$scope.patientDetailsList = [];
										$scope.patientDetails = {};
										$rootScope.stopLoader();
										}
									else{
										$rootScope.stopLoader();
									}
									
								});
							}
							$scope.patientList=[];
							$scope.patientDetails.visitTypeId = 1;
							$scope.getPatientDetailsByIndex = function(index){
									$scope.patientDetails = {};
									$scope.patientDetails.age=($scope.patientList[index].age);
									$scope.patientDetails.patientId=($scope.patientList[index].patientId);
									$scope.patientDetails.patientName=($scope.patientList[index].patientName);
									$scope.patientDetails.admissionId=($scope.patientList[index].admissionId);
									$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
									$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
									$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
									$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
									$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
//									$scope.patientDetails.dateOfAdmissionInput = ($scope.patientList[index].createdDate);
									$scope.patientDetails.bednumber = ($scope.patientList[index].bedNumber);
									$scope.patientDetails.admissionDate = ($scope.patientList[index].createdDate);
									$scope.patientDetails.admissionTime = ($scope.patientList[index].createdTime);
									$scope.patientDetails.wardName = $scope.patientList[index].wardName;
									$scope.patientDetails.doctorFullname ="Dr."+' '+ $scope.patientList[index].doctorName;
									$scope.patientDetails.dateOfAdmissionInput = $scope.patientList[index].doa;
									$scope.patientDetails.pdd = $scope.patientList[index].pdd;
									console.log("admissionDate",$scope.patientDetails.admissionDate);
									console.log("admissionTime",$scope.patientDetails.admissionTime);
									$scope.patientDetails.visitTypeId = 1;
									$scope.patientDetails.inputOutputchart=true;
								    
								    $scope.getListIntakOutputChart();
								    $scope.allergydata();
								    $scope.getAllPatientDiagnosis($scope.patientDetails.patientId);
								    
								    $scope.startIOFlag = false;
									$scope.stopIOFlag = false;
									
									
									 
								    $scope.dateOptionsi = {
											formatYear : 'yyyy',
											showWeeks : false,
											maxDate : new Date(),
											minDate : moment($scope.patientDetails.admissionDate, "DD-MM-YYYY").toDate(),
										};
								    
								    $scope.dateOptionss = {
											formatYear : 'yyyy',
											showWeeks : false,
											minDate: moment(new Date()).subtract(1, 'days'),
											maxDate:new Date()
										};
								    
								    
								    $scope.dateOptionsVacc[0].minDate = moment($scope.patientDetails.admissionDate, "DD-MM-YYYY").toDate();
								    $scope.dateOptionsVaccOutPut[0].minDate = moment($scope.patientDetails.admissionDate, "DD-MM-YYYY").toDate();
								   
								   
								    $scope.dateOptionsIntake = {
											formatYear : 'yyyy',
											showWeeks : false,
											maxDate : new Date(),
											minDate : moment($scope.patientDetails.admissionDate, "DD-MM-YYYY").toDate(),
										};
								    
								   /* $scope.dateOptionsOutPut = {
											formatYear : 'yyyy',
											showWeeks : false,
											maxDate : new Date(),
											minDate : moment($scope.patientDetails.admissionDate, "DD-MM-YYYY").toDate(),
										};*/
							}
							
							
							/**********for intake date******************/
							
								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};
								
								
								/**********for output date******************/
								
									$scope.openDatePickerr = function() {
										$scope.datepickerr.opened = true;
									};

									$scope.datepickerr = {
										opened : false
									};
								/****************for start intake******************/
								
								
									$scope.openDatePickerIntake = function() {
										$scope.datepickerIntake.opened = true;
									};

									$scope.datepickerIntake = {
										opened : false
									};
									
									

									    
									   /** Sneha Dalave*/
									     $scope.datepickervaccinePicker = function(index) {

				                             for (var i = $scope.intakeListForMorning.length - 1; i >= 0; i--) {
				                                 if (i === index) {
				                                     $scope.datepickervaccine.opened[i] = true;
				                                 } else {
				                                	
				                                     $scope.datepickervaccine.opened[i] = false;
				                                 }
				                             }
				                         };


				                         
				                         
				                         $scope.datepickervaccine = {
				                             opened : [ false ],

				                         };
									
									
									/****************for start OutPut******************/
									
									
										$scope.openDatePickerOutPut = function() {
											$scope.datepickerOutPut.opened = true;
										};

										$scope.datepickerOutPut = {
											opened : false
										};
										
										

										     $scope.datepickervaccinePickerOutPut = function(index) {

					                             for (var i = $scope.OutputListFor.length - 1; i >= 0; i--) {
					                                 if (i === index) {
					                                     $scope.datepickervaccineOutPut.opened[i] = true;
					                                 } else {
					                                	
					                                     $scope.datepickervaccineOutPut.opened[i] = false;
					                                 }
					                             }
					                         };
					                         
					                         $scope.datepickervaccineOutPut = {
					                             opened : [ false ],

					                         };
										
							
							
							$scope.getListOfIntakeFluid = function() {

								var URI = BASE_URL + ROOT_URL+ "/api/global/getIntakeFluidMasterList";
								var data = {
										
										organizationId:$scope.organizationId,
										unitId : $scope.unitId
								}
								GenericService.serviceAction("POST", URI, data).then(function(response) {
									
											$rootScope.stopLoader();
											$scope.listOfIntakeFluid = [];
											$scope.listOfIntakeFluid = response.data.listObject;
											console.log("listOfIntakeFluid",$scope.listOfIntakeFluid);

										});
							}
							$scope.getListOfIntakeFluid();
							
							$scope.getListOfIntakeTypeFluid = function(id,index) {
								
								var intakeFluidId =$scope.intakeListForMorning[index].id

								var URI = BASE_URL + ROOT_URL+ "/api/global/getIntakeFluidTypeMasterListById";
								var data = {
										
										organizationId:$scope.organizationId,
										unitId : $scope.unitId,
										intakeFluidId : id
								}
								
								GenericService.serviceAction("POST", URI, data).then(function(response) {
									
											$rootScope.stopLoader();
											$scope.listOfIntakeTypeFluidById = [];
											$scope.listOfIntakeTypeFluidById = response.data.listObject;
											console.log("listOfIntakeTypeFluid",$scope.listOfIntakeTypeFluid);
											$scope.intakeListForMorning[index].listOfIntakeTypeFluidById=$scope.listOfIntakeTypeFluidById;
											console.log("fdf",$scope.intakeListForMorning);

										});
							}
							
							
							$scope.getListOfOutputFluid = function() {

								var URI = BASE_URL + ROOT_URL+ "/api/global/getOutputFluidMasterList";
								var data = {
										
										organizationId:$scope.organizationId,
										unitId : $scope.unitId
								}
								
								GenericService.serviceAction("POST", URI, data).then(function(response) {
									
											$rootScope.stopLoader();
											$scope.listOfOutputFluid = [];
											$scope.listOfOutputFluid = response.data.listObject;
											console.log("listOfOutputFluid",$scope.listOfOutputFluid);

										});
							}
							$scope.getListOfOutputFluid();
							$scope.getListIntakOutputChart= function() {
								$scope.dt=moment(new Date()).format('YYYY-MM-DD');
								var URI = BASE_URL + ROOT_URL+ "/api/opd/getIntakeOutputChartListByPatientId";
								var data = {
										patientId:$scope.patientDetails.patientId,
										createdDate:$scope.dt,
										organizationId:$scope.organizationId,
										unitId : $scope.unitId
								}
								
								GenericService.serviceAction("POST", URI, data).then(function(response) {
									
											$rootScope.stopLoader();
											$scope.getListIntakOutputChartByPatientId = [];
											$scope.getListIntakOutputChartByPatientId = response.data.listObject;
											if($scope.getListIntakOutputChartByPatientId.length >0){
												$scope.startFlag=false;
												$scope.stopAndSaveDisabled = false;
											}else{
												$scope.startFlag=true;
												$scope.stopAndSaveDisabled = true;
												$scope.intakeDate='';
												$scope.intakeTime='';
											}
											console.log("getListIntakOutputChartByPatientId",$scope.getListIntakOutputChartByPatientId);
											
											
												$scope.CumulativeBalance=0;
												$scope.sum=0;
												$scope.sumCumulative=0;
												
												angular.forEach($scope.getListIntakOutputChartByPatientId, function(value , index) {
													$scope.newArr={}
													$scope.CumulativeBalance += parseInt(value.balance != null ? value.balance  :"0");
													 console.log("CumulativeBalance",$scope.CumulativeBalance );
												     $scope.getListIntakOutputChartByPatientId[index].CumulativeBalance=$scope.CumulativeBalance;
													 console.log("tt", $scope.getListIntakOutputChartByPatientId[index].CumulativeBalance );
													 $scope.isIoStart = value.isIoStart;
													 if($scope.isIoStart == true)
														 {
														
														   $scope.startIOFlag = true;
															$scope.stopIOFlag = false;
														
														 
														 }
													 else{
														   $scope.startIOFlag = false;
															$scope.stopIOFlag = true;
															 
														 }
													 console.log("$scope.isIoStart",$scope.isIoStart);
													 if(value.intakeShiftName != null ){
														 $scope.newArr.shift=value.intakeShiftName;
													 }
													 if(value.outputShiftName != null ){
														 $scope.newArr.shift=value.outputShiftName;
													 }
													 if(value.startIntakeDate != null ){
														 var forDate=moment(value.startIntakeDate,"DD-MM-YYYY").toDate();
													 $scope.newArr.date =moment(forDate).format('DD/MM/YYYY');
													 }
													 if(value.startOutputDate != null ){
														 var forDateOut=moment(value.startOutputDate,"DD-MM-YYYY").toDate();
													 $scope.newArr.date =moment(forDateOut).format('DD/MM/YYYY');
													 }
													 if(value.startOutputTime != null ){
														 $scope.newArr.time=value.startOutputTime
													 }
													 if(value.startIntakeTime != null ){
														 $scope.newArr.time=value.startIntakeTime
													 }
													 if(value.intakeDate != null){
														 $scope.intakeDate=moment(value.intakeDate,"DD-MM-YYYY").toDate();
														 $scope.test();
													 }
													 if(value.intakeTime != null){
														 $scope.intakeTime=value.intakeTime
													 }
													 
													 /*if(value.outputDate != null){
														 $scope.outputDate=moment(value.outputDate,"DD-MM-YYYY").toDate();
														 $scope.test();
													 }
													 if(value.outputTime != null){
														 $scope.outputTime=value.outputTime
													 }*/
													 
													 
													 if(value.startIntakeQuantity != null ){
														 $scope.newArr.startIntakeQuantity=value.startIntakeQuantity
														 $scope.newArr.startOutputQuantity=0
													 }
													 if(value.startOutputQuantity != null ){
														 $scope.newArr.startOutputQuantity=value.startOutputQuantity
														 $scope.newArr.startIntakeQuantity=0
													 }
													 if($scope.CumulativeBalance == 0){
													 $scope.newArr.sum = ($scope.newArr.startIntakeQuantity -  $scope.newArr.startOutputQuantity) + $scope.CumulativeBalance;
													 $scope.CumulativeBalance=$scope.newArr.sum ;
													 $scope.sumCumulative=$scope.newArr.sum;
													 }else{
														 $scope.newArr.sum = ($scope.newArr.startIntakeQuantity -  $scope.newArr.startOutputQuantity) + $scope.sumCumulative;
														 $scope.sumCumulative=$scope.newArr.sum;
													 }
													 $scope.getListIntakOutputChartByPatientId[index].shift=$scope.newArr.shift;
													 $scope.getListIntakOutputChartByPatientId[index].time=$scope.newArr.time;
													 $scope.getListIntakOutputChartByPatientId[index].date=$scope.newArr.date;
													 $scope.getListIntakOutputChartByPatientId[index].sum=$scope.newArr.sum;
													 console.log("shiftmerge",$scope.getListIntakOutputChartByPatientId);
												});
												
										});
							}
//							$scope.getListIntakOutputChart();
							
						
							$scope.validateTime=function(){
								
								
								var date = new Date();
								var time = moment().format("HH:mm");
								var currtime = moment(date).format('DD-MM-YYYY');
								var intakeDate = moment($scope.intakeDate).format('DD-MM-YYYY');
								console.log("currtime",currtime);
								console.log("intakeDate",$scope.intakeDate);
								console.log("intakeTime",$scope.intakeTime);
								var compareTime=$scope.intakeTime+":00";
								var compareDate=moment($scope.intakeDate+$scope.intakeTime).format('DD-MM-YYYY HH:mm');
								
								if(currtime == intakeDate)
									{
										 if( time  >= $scope.intakeTime )
										 {
										 $scope.intakeTime=$scope.intakeTime;
										 }
									 else{
										 growl.warning("Time Should be equal to or less than Current time.",
													{
														title : 'Warning!'
													});
											$scope.intakeTime = time;
											return false;
									 }
									}
								else{
									 $scope.intakeTime=$scope.intakeTime;
								}
								
								//added by shital
								var adtDt =  moment($scope.patientDetails.admissionDate,"DD-MM-YYYY").toDate();
								var adtDt1 = moment(adtDt).format('DD-MM-YYYY')
								if( intakeDate == adtDt1){
									
									if($scope.intakeTime <  $scope.patientDetails.admissionTime){
										
										 growl.warning("Intake Time cannot be less than admission time .",
													{
														title : 'Warning!'
													});
											return false;
									}
								}
								
								if(currtime == adtDt1){
									
									$scope.dateOptionss.minDate =  moment(new Date()).subtract(1, 'days');
									$scope.dateOptionss.maxDate =  new Date();
								}
															
							}
							
							
							$scope.validateOutputTime=function(){
								var date = new Date();
								var time = moment().format("HH:mm");
								var intakeDate = moment($scope.intakeDate).format('DD-MM-YYYY');
								var outputDate = moment($scope.outputDate).format('DD-MM-YYYY');
								if(outputDate == intakeDate)
								{
									 if( $scope.outputTime  >= $scope.intakeTime )
									 {
									 $scope.outputTime=$scope.outputTime;
									 }
								 else{
									 growl.warning("Time Should be equal to or greater than intake time .",
												{
													title : 'Warning!'
												});
										$scope.outputTime = $scope.intakeTime;
										return false;
								 }
								}
							else{
								 $scope.outputTime=$scope.outputTime;
							}

							}
							
							$scope.getListShift = function(idx) {
								
								var date = new Date();
								var time = moment().format("HH:mm");
								var currtime = moment(date).format('DD-MM-YYYY');
								var intakeDate = moment($scope.intakeDate).format('DD-MM-YYYY');
								var outputDate = moment($scope.outputDate).format('DD-MM-YYYY');
								var intakeTime=$scope.intakeTime;
								angular.forEach($scope.intakeListForMorning, function(value , index) {
									if(index ==idx )
										{
										
										
										if(intakeDate == moment(value.intakeStartDate).format('DD-MM-YYYY'))
										{
											 if( value.intakeStartTime  >= $scope.intakeTime )
											 {
											$scope.intakeListForMorning[index].intakeStartTime = value.intakeStartTime;
											 }
										 else{
											 growl.warning("Time should be equal to or greater than Start I/O time.",
														{
															title : 'Warning!'
														});
											 $scope.intakeListForMorning[index].intakeStartTime = $scope.intakeTime;
										 }
											
										}
									
										else if(currtime == moment(value.intakeStartDate).format('DD-MM-YYYY') )
										{
											 if( value.intakeStartTime  <= time)
											 {
											$scope.intakeListForMorning[index].intakeStartTime = value.intakeStartTime;
											 }
										 else{
											 growl.warning("Time should be equal to or less than current time.",
														{
															title : 'Warning!'
														});
											 $scope.intakeListForMorning[index].intakeStartTime = time;
										 }
	                                    
										}
									
									
								    var fromTime=value.intakeStartTime+":00";
									var URI = BASE_URL + ROOT_URL+ "/api/common/getShiftlistByTime";
									var data = {
											
											organizationId:$scope.organizationId,
											unitId : $scope.unitId,
											fromTime:fromTime
									}
									
									GenericService.serviceAction("POST", URI, data).then(function(response) {
											
												$rootScope.stopLoader();
												$scope.getListOfActiveShiftForIntake = [];
												$scope.getListOfActiveShiftForIntake = response.data.listObject;
												$scope.intakeListForMorning[idx].getListOfActiveShiftForIntake=$scope.getListOfActiveShiftForIntake;
												
												console.log("getListOfActiveShiftForIntake",$scope.getListOfActiveShiftForIntake);

											});
								
										}
									
							
								});
							}
							
							$scope.getListShiftOutput = function(idx) {
								var date = new Date();
								var time = moment().format("HH:mm");
								var currtime = moment(date).format('DD-MM-YYYY');
								var intakeDate = moment($scope.intakeDate).format('DD-MM-YYYY');
								var outputDate = moment($scope.outputDate).format('DD-MM-YYYY');
								var intakeTime=$scope.intakeTime;
								var outputTime=$scope.outputTime;
								angular.forEach($scope.OutputListFor, function(value , index) {
									if(idx == index){
										
										if(intakeDate == moment(value.outputStartDate).format('DD-MM-YYYY'))
										{
											 if( value.outputStartTime  >= $scope.intakeTime )
											 {
											$scope.OutputListFor[index].outputStartTime = value.outputStartTime;
											 }
										 else{
											 growl.warning("Time should be equal to or greater than Start I/O time.",
														{
															title : 'Warning!'
														});
											 $scope.OutputListFor[index].outputStartTime = $scope.intakeTime;
										 }
										}
									
										else if(currtime == moment(value.outputStartDate).format('DD-MM-YYYY') )
										{
										 if( value.outputStartTime  <= time)
											 {
											$scope.OutputListFor[index].outputStartTime = value.outputStartTime;
											 }
										 else{
											 growl.warning("Time should be equal to or less than current time.",
														{
															title : 'Warning!'
														});
											 $scope.OutputListFor[index].outputStartTime = time;
										 }
										}
									var fromTime=value.outputStartTime+":00";
									var URI = BASE_URL + ROOT_URL+ "/api/common/getShiftlistByTime";
									var data = {
											
											organizationId:$scope.organizationId,
											unitId : $scope.unitId,
											fromTime:fromTime
									}
									
									GenericService.serviceAction("POST", URI, data).then(function(response) {
										
												$rootScope.stopLoader();
												$scope.getListOfActiveShiftForOutput = [];
												$scope.getListOfActiveShiftForOutput = response.data.listObject;
												$scope.OutputListFor[idx].getListOfActiveShiftForOutput=$scope.getListOfActiveShiftForOutput;
												console.log("getListOfActiveShift",$scope.getListOfActiveShift);

											});
										
									}
						
								});
								
							}
							// for added addIntakeList
							 $scope.addIntakeListMorning = function(index) 
							 {
								 $scope.addDateOptions();
								 console.log("index add",index);
									var data = {
											
											 intakeDate:"",
									    	 intakeTime:"",
									    	 intakeShiftId:"",
									    	 intakeFluidId:"",
									    	 intakeTypeFluidId:"",
									    	 intakeQuantity:"",
									};
									var len = $scope.intakeListForMorning.length;
									
									$scope.intakeListForMorning.push(data);
									
								};

								 $scope.removeIntake = function(deleteIndex)
									{
										$scope.intakeListForMorning.splice(deleteIndex, 1);
										$scope.dateOptionsVacc.splice(deleteIndex, 1);
								     }
								    
								 
								 $scope.calculateTotalIntakeRemove=function(select){
										if(select != ""){
										$scope.totalIntakeQuantity -= parseInt(select);
										}
									}
								
							
									 $scope.addOutputList = function(index) 
									 {
										 $scope.addDateOptionsOutput();
										 
										 console.log("index add",index);
											var data = {
													 outputDate:"",
											    	 outputTime:"",
											    	 outputTypeId:"",
											    	 outputQuantity:"",
											};
											var len = $scope.OutputListFor.length;
											
											$scope.OutputListFor.push(data);
										};
										
										
										 $scope.remove = function(deleteIndex)
											{
												$scope.OutputListFor.splice(deleteIndex, 1);
												$scope.dateOptionsVaccOutPut.splice(deleteIndex, 1);
										     }
										    
										
										/***********calculation for intake quantity*****************/
										$scope.totalIntakeQuantity=0;
										 $scope.intakeListForMorning = [{
									    	
									    	 intakeQuantity:""
									    	
										}];
										 $scope.select='';
										$scope.calculateTotalIntake=function(select,oldValue,idx){
											
											if(select == undefined){
												select =0;
											}
											if(oldValue!=""){						
												$scope.totalIntakeQuantity -= parseInt(oldValue);
											}						
											if(select != ""){
											$scope.totalIntakeQuantity += parseInt(select);
											}
										}
										/***********calculation for output quantity*****************/
										$scope.totalOutPutQuantity=0;
										 $scope.OutputListFor = [{
									    	
									    	 outputQuantity:""
										}];
									     $scope.oldOutput='';
										$scope.calculateTotalOutput=function(output,oldOutput,idx){
											if(output == undefined) {
												output = 0;
												}
											if(oldOutput!=""){						
												$scope.totalOutPutQuantity -= parseInt(oldOutput);
											}						
											if(output != ""){
											$scope.totalOutPutQuantity += parseInt(output);
											}
										}
										
										$scope.calculateTotalOutputRemove=function(output){
											if(output != ""){
											$scope.totalOutPutQuantity -= parseInt(output);
											}
										}
										
										$scope.balance=0;
										
										$scope.save=function(){
											
											$rootScope.startLoader();
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											$scope.balance= parseInt($scope.totalIntakeQuantity)-parseInt($scope.totalOutPutQuantity);
											if($scope.outputDate != undefined){
												var Outd = moment($scope.outputDate).format('DD-MM-YYYY HH:mm:ss');
											}
											
											var data={
													
													"intakeDate":moment($scope.intakeDate).format('DD-MM-YYYY HH:mm:ss'),
													"intakeTime":$scope.intakeTime!=undefined ? $scope.intakeTime+":00" :null,
													"outputDate":Outd,
													"outputTime":$scope.outputTime!=undefined ?$scope.outputTime+":00" :null,
													"admissionId":$scope.patientDetails.admissionId,
													"patientId":$scope.patientDetails.patientId,
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
													"isIoStart" : $scope.isIoStart
											}
											
											$scope.intakeListForMorningList=[];
											console.log('ds',$scope.intakeListForMorning);
										angular.forEach($scope.intakeListForMorning, function(value , key) {
												 var dData = {
														 startIntakeDate : moment(value.intakeStartDate).format('DD-MM-YYYY HH:mm:ss'),
														 startIntakeTime:value.intakeStartTime+":00",
														 intakeFluidId:value.intakeFluidId,
														 intakeTypeFluidId:value.intakeTypeFluidId,
														 startIntakeQuantity : value.intakeQuantity,
														 totalIntake:$scope.totalIntakeQuantity,
														 shiftId:value.intakeShiftId,
														 status:"A",
														 unitId:$scope.unitId,
												         organizationId:$scope.organizationId,
														 createdBy:$scope.createdBy,
														 createdDate:$scope.dt,
														 updatedBy:$scope.updatedBy,
														 updatedDate:$scope.dt,
									 				}
												 if(value.intakeFluidId!=undefined && value.intakeFluidId!=""){
									 				$scope.intakeListForMorningList.push(dData);
												 }
												
											data.intakeOutputChartMasterDtoList = $scope.intakeListForMorningList;
											console.log("finalIntakeList",data.intakeOutputChartMasterDtoList);
												
										});
										$scope.OutputListForList=[];
										console.log("output",$scope.OutputListFor);
										angular.forEach($scope.OutputListFor, function(value , key) {
												 var dData = {
														 startOutputDate : moment(value.outputStartDate).format('DD-MM-YYYY HH:mm:ss'),
														 startOutputTime:value.outputStartTime+":00",
														 outputTypeId:value.outputTypeId,
														 startOutputQuantity:value.outputQuantity,
														 totalOutput:$scope.totalOutPutQuantity,
														 shiftId:value.outputShiftId,
														 status:"A",
														 unitId:$scope.unitId,
												         organizationId:$scope.organizationId,
														createdBy:$scope.createdBy,
														createdDate:$scope.dt,
														updatedBy:$scope.updatedBy,
														updatedDate:$scope.dt
									 				}
												 if(value.outputTypeId !=undefined && value.outputTypeId !=""){
									 				$scope.OutputListForList.push(dData);
												 }
												 
											data.outputChartMasterDtoList = $scope.OutputListForList;
											console.log("finalOutPutList",data.outputChartMasterDtoList);
										});
										
										console.log("FinalData",data);
										
										if((data.intakeDate == undefined || data.intakeDate == null || data.intakeDate == "" || data.intakeDate == "Invalid date") 
												|| (data.intakeTime ==undefined || data.intakeTime == null || data.intakeTime == "" || data.intakeTime == ":00")){
											growl.warning("Please enter Intake date and time",
													{
														title : 'Error!'
													});
											$rootScope.stopLoader();
											return false;
										}
//										else if((data.outputDate == undefined || data.outputDate == null || data.outputDate == "") 
//												&& (data.outputTime == undefined || data.outputTime == null || data.outputTime == "")){
//											growl.warning("Please enter Output date and time",
//													{
//														title : 'Error!'
//													});
//											$rootScope.stopLoader();
//											return false;
//										}
										
										if(data.intakeOutputChartMasterDtoList.length != 0 || data.outputChartMasterDtoList.length != 0){
											var URI = BASE_URL + ROOT_URL+ "/api/opd/saveIntakeOutputChart";
											console.log("Save-URI-", URI);
											console.log(" save OBJ", data);

											GenericService.serviceAction("POST", URI,data).then(function(response) {

												if (response.data.status == 'success') {
													growl.success(response.data.message,
															{
																title : 'Success!'
															});

													 $scope.init();
													$scope.startFlag=false;
												    $scope.getListIntakOutputChart();
												    $scope.totalIntakeQuantity=0;
												    $scope.totalOutPutQuantity=0;
												    $rootScope.stopLoader();
												
												    } else if (response.data.status == 'error') {
													growl.error(response.data.message,
															{
																title : 'Error!'
															});
													$rootScope.stopLoader();
												   }
											});
										} 
										else{
											growl.error("Please enter Intake or Output",
													{
														title : 'Error!'
													});
											$rootScope.stopLoader();
										}
											
											
										}
										
										$scope.start=function(){
											$scope.startFlag=false;
											$scope.stopAndSaveDisabled = false;
											$scope.isIoStart=true;
											/***chetana*/
											try{
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											 var data ={
											updatedBy : $scope.updatedBy,
											updatedDate : $scope.dt,
											isIoStart :false,
											patientId : $scope.patientDetails.patientId
											}
											$rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL + "/api/opd/updateStatusForStartIOChart";
											GenericService.serviceAction("POST",URI,data).then(function(response){
												if (response.data.status == 'success')
												{
													$rootScope.stopLoader();
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
//													$scope.getListIntakOutputChart();
													$scope.intakeTime='';
													$scope.intakeDate='';
													$scope.forOutPutMinDate='';
													$scope.setOutputDate='';
													$scope.startIOFlag = false;
													$scope.stopIOFlag = true;
												} else if(response.data.status == 'error')
												{
													$rootScope.stopLoader();
													growl.error(response.data.message,
															{
																title : 'Error!'
															});
												}
											});
										}catch(e){
											console.log("Exception",e);
										}
										/***/
										}
										
										$scope.stop=function(){
											$scope.startFlag = true;
											$scope.isIoStart = false;
											
											try{
											var date = new Date();
											$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
											 var data ={
											updatedBy : $scope.updatedBy,
											updatedDate : $scope.dt,
											isIoStart :$scope.isIoStart,
											outputDate:moment($scope.outputDate).format('YYYY-MM-DD HH:mm:ss'),
											outputTime:$scope.outputTime!=undefined ?$scope.outputTime+":00" :null,
											patientId : $scope.patientDetails.patientId
											}
											$rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL + "/api/opd/updateInputOutputStartStatus";
											GenericService.serviceAction("POST",URI,data).then(function(response){
												if (response.data.status == 'success')
												{
													$rootScope.stopLoader();
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
//													$scope.getListIntakOutputChart();
													$scope.intakeTime='';
													$scope.intakeDate='';
													$scope.outputDate='';
													 $scope.outputTime='';
													$scope.startIOFlag = false;
													$scope.stopIOFlag = true;
												} else if(response.data.status == 'error')
												{
													$rootScope.stopLoader();
													growl.error(response.data.message,
															{
																title : 'Error!'
															});
												}
											});
										}catch(e){
											console.log("Exception",e);
										}
										}
										
										/**Chetana*/
										$scope.alertStopIO=function(){
											if(($scope.outputDate==null || $scope.outputDate==undefined || $scope.outputDate=="")
												|| ($scope.outputTime==null || $scope.outputTime==undefined || $scope.outputTime=="")){
												 growl.warning(" Please enter date and time to stop I/O. ",
															{
																title : 'warning!'
															});
											}else{
													$('#StopIO').modal('show');
											}
										}
										/***/
										 $scope.allergyFlag = false;
										$scope.allergydata=function(){ 
										$scope.dt = moment(new Date()).format('DD-MM-YYYY');
										var headerObj = {
												unit_id : $scope.unitId,
												organization_id : $scope.organizationId
											};

											var paramObjForHistList = {
												patientId : $scope.patientDetails.patientId,
											};
									var data = {};
								var URI = BASE_URL + ROOT_URL + "/api/opd/getPatientAllergyListForIcon";
							GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,
										NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList,headerObj)
								.then(
										function(response) {
											console.log(response);
											if (response.data.status == "success")
											$scope.activeAllergyListForIcon = response.data.listObject;
											console.log("list for allergy icon",$scope.activeAllergyListForIcon)
											if($scope.activeAllergyListForIcon.length > 0)
											{
												$scope.allergySymbolList=[];
											   angular.forEach($scope.activeAllergyListForIcon,function(value,index){
												   if(value.isNoKnownAllergies !='Y'){
													   $scope.allergySymbolList.push(value);
												   }
												   if($scope.allergySymbolList.length > 0){
													   $scope.allergyFlag = true;
												   }
												   else{
													   $scope.allergyFlag = false;
												   }
											   });
												
											}else{
												   $scope.allergyFlag = false;
											   }
											
										});
							}
										$scope.getAllergyDescriptions = function() {
										    if ($scope.activeAllergyListForIcon && $scope.activeAllergyListForIcon.length > 0) {
										        return $scope.activeAllergyListForIcon
										            .filter(value => value.isNoKnownAllergies !== 'Y') 
										            .map(value => value.allergyDesc) 
										            .join(', '); 
										    }
										    return ''; 
										};
										
										$scope.getAllPatientDiagnosis=function(patientId){
											$scope.patientDiagnosisListIcon =[];
											 var headerObjDia = {
													 	'unit_id' : $scope.unitId,
														'organization_id' : $scope.organizationId,
														'patient_id' : patientId,
														'Content-Type':'application/json',
										    };
											 var data={};
				                             var URI = BASE_URL + ROOT_URL
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
				                                             $scope.patientDiagnosisListIcon = response.data.listObject; 
				                                         console.log("patientDiagnosisListIcon",$scope.patientDiagnosisListIcon);
				                                         	$scope.comorbidityFlag = false;
				                                         	$scope.notifiableFlag = false;
															
																if($scope.patientDiagnosisListIcon.length > 0)
																	{
																	    $scope.complaintDelete=false;
																		angular.forEach($scope.patientDiagnosisListIcon,function(value,key) {
																			if(value.isNotifiable == 'Y')
																			{
																				$scope.notifiableFlag = true;
																			}
																			else if(value.isComorbidity == 'Y')
																			{
																				$scope.comorbidityFlag = true;
																			}
																		});
																	}
				                                         
				                                     });
											}
										$scope.printIntakeOutputChart=function(){
											var intakeId=0;
											var outputId=0;
											$scope.listIds=[];
											$scope.tempData="";
											angular.forEach($scope.getListIntakOutputChartByPatientId, function(val, key){
												
												if(val.intakeOutputChartId != undefined && val.intakeOutputChartId !=null){
													$scope.listIds.push(val.intakeOutputChartId);
												}
											});
											$scope.tempData=$scope.listIds.toString();
											var data = {
												    "appName" : "HIS",
													"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
													"reportDesignFilePath" : "intakeoutputchart.rpt",
													"reportFormat" : "PDF",
													"exportFileName" : "Intake Output Chart",
													"reportParams" : [ {
														"paramName" : "intake_output_chart_id",
														"paramType" : "String",
														"paramValue" : $scope.tempData
				
													} ]
												};
										reportService.generate(REPORT_API_URI1, data);
							}
				
						} ]);