/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"sampleCollectionIPController",
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
								$sessionStorage, growl,$interval,$stateParams,GenericServiceParamHeader,BillingGenericService,$window,reportService) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.sampleCollectionList = [];
							$scope.admissionObj = {};
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.commonList = [];
							$scope.getUnitByOrganization();
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.patientCategoryId = cookieObjectAppConfig.patientCategoryId;
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
							
							 $scope.nephrologySpecialityId=cookieObjectAppConfig.hemodialysisSpecialityId;
							 
							 $scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
								
						     $scope.dateFormat = cookieObjectAppConfig.dateFormat;
								
							 if($scope.isAppointmentRequired  == undefined || $scope.isAppointmentRequired == null)
								{
								  $scope.isAppointmentRequired = "N";
								}
							else
								{
								  $scope.isAppointmentRequired = $scope.isAppointmentRequired;
								}
							 
							/* code for setting Label */
							$(".selectedPageName").text("Sample Collection");
							var labConfig  =    $scope.checkLabConfig();
							$scope.isPrebarcodeGenerantion = labConfig.isPrebarcodeGenerantion;
							$scope.deptId = labConfig.labSpecialityId;
							$rootScope.getSexMasterList();
							$rootScope.getIdentificationTypeList();
							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate : new Date(),
							};

							$scope.open = function() {
								$scope.popup.opened = true;
							};

							$scope.popup =
							{
								opened : false
							};
							
							$scope.popup1 =
							{
								opened : false
							};
							
							$scope.open1 = function() 
							{
								$scope.popup1.opened = true;
							};
							
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
								};

							
							$scope.dateOptionsSlot = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
							
								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};
								
								$scope.datepicker = new Date();
							
								$scope.openDatePickerPatiSearch = function() {
									$scope.datepicker1.opened = true;
									};

									$scope.datepicker1 = {
									opened : false
									};
									
								
							$scope.initLabSampleMaster = function() {
								
								$scope.listSampleDtls = [];
								$scope.labSampleMaster = {
									"labSampleId" : "",
									"orgId" : $scope.orgId,
									"orgUnitId" : $scope.orgUnitId,
									"sampleGenDatetime" : "",
									"orderId" : "",
									"visitTypeId" : $scope.patientDetails.visitTypeId,
									"admissionId" : $scope.patientDetails.admissionId,
									"encounterId" : "",
									"patientId" : $scope.patientDetails.patientId,
									"doctorId" : "",
									"createdBy" : $scope.userId,
									"createdDate" : "",
									"updatedBy" : $scope.userId,
									"updatedDate" : "",
									"uhid" : "",
									"patientDetails" : "",
									"doctorDetails" : "",
									"priorityId" : "",
									"priorityName" : "",
									"colorCode" : "",
									"isOrderComplete" : "",
									"orderStatusId":"1", //code to change
									"genderId" :$scope.patientDetails.genderId,
									"visitType":"",
									"patientDetails":"",
									"listLabSampleDetailsMaster" : $scope.listSampleDtls
								}
							}

							
							$scope.labSampleDtls = {
								"labSampleDtlsId" : "",
								"orgId" : $scope.orgId,
								"orgUnitId" : $scope.orgUnitId,
								"testId" : "",
								"deptId" : "",
								"subDeptId" : "",
								"deptName" : "",
								"labSampleId" : "",
								"sampleBarcode" : "",
								"sampleId" : "",
								"sampleNo" : "",
								"orderDetailsId" : "",
								"orderId" : "",
								"orderQty" : "",
								"serviceId" : "",
								"testCode" : "",
								"testDesc" : "",
								"sampleName" : "",
								"sampleVolume" : "",
								"unitName" : "",
								"profileId" : "",
								"panelCode":"",
								"packageId" : "",
								"isCentrifugationReq" : "",
								"isAlliquoteReq" : "",
								"sampleTypeId" : "",
								"containerId" : "",
								"containerName" : "",
								"sampleReqCount" : "",
								"samplePendingCount" : "",
								"sampleGenDatetime" : "",
								"sampleGenBy" : $scope.userId,
								"currStatus" : "",
								"sampleStatusId" : "",
								"isOutsourced" : "",
								"outsourcedLabId" : "",
								"sampleSendtocrDatetime" : "",
								"sampleSendtocrBy" : "",
								"sampleCollectionDatetime" : "",
								"sampleCollectionBy" : $scope.userId,
								"sampleCentrifugationDatetime" : "",
								"sampleCentrifugationBy" : "",
								"sampleAcceptDatetime" : "",
								"sampleAcceptBy" : "",
								"sampleRejectDatetime" : "",
								"sampleRejectBy" : "",
								"sampleRejectReasonId" : "",
								"sampleRejectReason" : "",
								"sampleRecollectFlag" : "",
								"sampleRecollectAgainstId" : "",
								"createdBy" : $scope.userId,
								"createdDate" : "",
								"updatedBy" : "",
								"updatedDate" : "",
								"sampleTestTime":"",
								"patientVisitAge":"",
								"genderId":"",
								"visitType":"",
								"listTMicroSampleMediaDto":[]	
								/*"sampleTestTime":"",
								"patientVisitAge":"",
								"genderId":""*/
									
							}

							
							$scope.patientDetails = {
									"patientName" : "",
									"genderCode" : "",
									"age" : "",
									"uhidNumber" : "",
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
								      "organizationId"  :$scope.organizationId
								    }
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
					   
							$scope.getFormattedDate = function(date,isTime){
								var tempDate = new Date(date);
								var date = tempDate.getDate().toString().length == 1 ? "0"+tempDate.getDate().toString() : tempDate.getDate().toString();
								var month =  (tempDate.getMonth() + 1).toString().length == 1 ? "0"+(tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
								var year = tempDate.getFullYear();
								if(isTime)
									return date + "-" + month + "-" + year + " "+tempDate.getHours()+":"+tempDate.getMinutes()+":"+tempDate.getSeconds();
								else
									return date + "-" + month + "-" + year + " 00:00:00";
							}
							
							
							$scope.searchPatient = function()
							{
								$rootScope.startLoader();
								$scope.DischargeSummaryFinalList=[];
								$scope.patientTemplateList=[];
								if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
								{
									growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
									$scope.patientList = [];
									$rootScope.stopLoader();

									return false;
								}	
								var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
								var patSearchObj = angular.copy($scope.patientSearchObj);
								if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
									var dt = new Date($scope.patientSearchObj.birthDate);
									patSearchObj.birthDate = (dt.getMonth() + 1) + "-" + dt.getDate() + "-" +  dt.getFullYear();
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
										$scope.patientSearchObj.birthDate="";
										$rootScope.stopLoader();
										}
									else if(response.data.listObject==null){
										growl.warning(response.data.message,{
											title : ERROR_MSG
										});
										$scope.patientSearchObj.birthDate="";
										$scope.patientList=[];
										return false;
									}
									else{
										$rootScope.stopLoader();
									}
									
								});
							}
							$scope.setFirstTabLevel = function(newTab) {
								$scope.firstTabLevel = newTab;								
//								if (newTab == '2') {
//								}
//								if (newTab == '1') {
//								}								
							};

							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstTabLevel === tabNum;
								
							};
							$scope.admissionId=null;
							$scope.departmentId=null;
							//get Patient Id,department id and template id
							$scope.patientId=null;
							
							
							
							
							
							
							
							$scope.getInPatientOrderedServices = function() 
							{
								$rootScope.startLoader();
								try {

									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_TRANSACTION
											+ GET_IN_PATIENT_ORDERED_SERVICES
											+ S + $scope.orgId + S + $scope.orgUnitId + S
											+ $scope.admissionObj.patientId + S + $scope.admissionObj.admissionId
											+ S + $scope.deptId ;
									console.log("GET_IN_PATIENT_ORDERED_SERVICES", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) 
													{
														$scope.sampleCollectionList=[];
														$scope.sampleCollectionList = response.data.listObject;
														angular .forEach( $scope.sampleCollectionList, function(value, key) {
															
															value.performTestId  = value.testId;
															if(value.assignUnit == null){
																value.assignUnit  = value.orgUnitId;
															}
																		
														 });
														$rootScope.stopLoader();
														
													});
									$rootScope.stopLoader();
								} catch (e) {
									console.log(e.message)
								}
							}
							
							
							
							$scope.getPatientDetailsById = function(patientId){
								$rootScope.startLoader();
								$scope.patientId=patientId;
								$scope.patientDetailsList = [];
								$scope.patientDetails = {};
								$scope.masterObj = {};
								$scope.DischargeSummaryFinalList=[];
								$scope.patientTemplateList=[];
								
								var patientO =  $scope.patientList.find(x => x.patientId ==patientId);
								$scope.patientDetails.age=patientO.age;
								$scope.admissionObj.patientId =patientO.patientId;
							     $scope.admissionObj.admissionId =patientO.admissionId;
								var admissionId = $.grep($scope.patientList, function (doc) {
				    	            return doc.patientId == patientId;
				    	        })[0].admissionId;
								
								if(admissionId>0)
								{
									var URI = BASE_URL + ROOT_URL + "/api/opd/getListOfRadiologyTests/"+admissionId+"/"+$scope.organizationId+"/"+$scope.unitId;
									BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success"){
											$scope.nephrologyList=[];
											$scope.radiology = [];
											angular.forEach(response.data.listObject,function(value,index){
												if(cookieObjectAppConfig.radiologySpecialityId==value.specialityId)
												{
													$scope.radiology.push(value);
												}else if(cookieObjectAppConfig.hemodialysisSpecialityId==value.specialityId){
													$scope.nephrologyList.push(value);
												}else{
													
												}
											})
											console.log("radiology:",$scope.radiology);
											console.log("nephrology list:",$scope.nephrologyList);
											$rootScope.stopLoader();
										}
										else if(response.data.status == "error")
											{
											 $rootScope.stopLoader();
											}
									});
								}
								
								if(admissionId>0)
								{
									this.billing = false;	
									$scope.admissionObj ={};
									$scope.admissionObj.admissionId = admissionId;
									$scope.admissionObj.patientId = patientId;
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+admissionId;
										BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											if(response.data.status == "success")
											{
												$scope.admissionDetails = response.data.object;
												
												
												$scope.patientDetails.patientName = $scope.admissionDetails.patientName;
												$scope.patientDetails.uhidNumber= $scope.admissionDetails.uhidNumber;
												$scope.patientDetails.genderCode= patientO.genderCode;
												$scope.patientDetails.dob= $scope.admissionDetails.dob;
												$scope.patientDetails.dobNew = $scope.admissionDetails.dob;
												$scope.patientDetails.age= $rootScope.getAge($scope.admissionDetails.dob);
												$scope.patientDetails.doctorFullName= $scope.admissionDetails.doctorName;
												$scope.patientDetails.dateOfAdmission= $scope.admissionDetails.doa;
		                                          
												var dateParseDOA = $scope.patientDetails.dateOfAdmission.split('/');
	                                            var dateObjectDOA = new Date(dateParseDOA[2], dateParseDOA[1] - 1, dateParseDOA[0]); 
		                                        var longValueDOA = dateObjectDOA.getTime();
		                                        $scope.patientDetails.dateOfAdmission = longValueDOA;
//		                                        	moment(longValueDOA).format($scope.dateFormat.toUpperCase());
		                                          
												$scope.patientDetails.patientDischargeDate= $scope.admissionDetails.pdd;
												
												var dateParsePDD = $scope.patientDetails.patientDischargeDate.split('/');
	                                            var dateObjectPDD = new Date(dateParsePDD[2], dateParsePDD[1] - 1, dateParsePDD[0]); 
		                                        var longValuePDD = dateObjectPDD.getTime();
		                                        $scope.patientDetails.patientDischargeDate = longValuePDD;
												
												$scope.patientDetails.wardName= $scope.admissionDetails.wardNo;
												$scope.patientDetails.bedNumber= $scope.admissionDetails.bedNo;
												$scope.patientDetails.imagePath= "";
												$scope.patientDetails.patientId= patientId;
												$scope.patientDetails.visitTypeId= $scope.admissionDetails.visitTypeId;
												$scope.patientDetails.admissionId= $scope.admissionDetails.admissionId;
												$scope.patientDetails.genderId= $scope.admissionDetails.genderId;
												$rootScope.stopLoader();
												$scope.initLabSampleMaster();
												$scope.getInPatientOrderedServices();
											}
											else if(response.data.status == "error")
												{
												  $rootScope.stopLoader();
												} 
										});	
								}
								else{
									 $scope.patientDetails.patientName = patientO.patientName;
									 $scope.patientDetails.genderCode= patientO.genderCode;
									 $scope.patientDetails.uhidNumber= patientO.uhIdNumber;
									 $scope.patientDetails.dob=  moment(patientO.birthDate,'DD/MM/YYYY').format('DD-MM-YYYY');
									 $scope.patientDetails.genderId  = patientO.genderId;
									 $scope.sampleCollectionList=[];
									 $rootScope.stopLoader();
									$scope.billing = true;	
								   }
								
								
							}
							
					
							
							$scope.getRadioLogyServices = function(admissionId)
							{
								//$scope.radiology = [];
								var URI = BASE_URL + ROOT_URL + "/api/opd/getListOfRadiologyTests/"+admissionId+"/"+$scope.organizationId+"/"+$scope.unitId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$scope.nephrologyList=[];
										$scope.radiology = [];
										angular.forEach(response.data.listObject,function(value,index){
											if(cookieObjectAppConfig.radiologySpecialityId==value.specialityId)
											{
												$scope.radiology.push(value);
											}else if(cookieObjectAppConfig.hemodialysisSpecialityId==value.specialityId){
												$scope.nephrologyList.push(value);
											}else{
												
											}
										})
										console.log("radiology:",$scope.radiology);
										console.log("nephrology list:",$scope.nephrologyList);
									}
								});
							}
							
							
							/*$scope.getInPatientOrderedServicesForRadiology = function() 
							{
								$rootScope.startLoader();
								try {

									var data = "";
									var URI = BASE_URL + ROOT_URL
											+ LIS_TRANSACTION
											+ GET_IN_PATIENT_ORDERED_SERVICES_RADIOLOGY
											+ S + $scope.orgId + S + $scope.orgUnitId+ S + $scope.admissionObj.admissionId;
									console.log("GET_IN_PATIENT_ORDERED_SERVICES_RADIOLOGY", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) 
													{
														$scope.radiologyServicesList=[];
														$scope.radiologyServicesList = response.data.listObject;
														$rootScope.stopLoader();
														
													});
									$rootScope.stopLoader();
								} catch (e) {
									console.log(e.message)
								}
							}*/
							/**Sneha start*/
							
							$scope.comorbidityFlag = false;
                         	$scope.notifiableFlag = false;
							$scope.getAllPatientDiagnosisForIcon=function(patientId){
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
							
							/**Redirect Page Code*/
							if(!angular.equals({},$stateParams.obj))
 							{
								this.billing = false;	
								$scope.patientDetails = {};
								$scope.admissionObj = $stateParams.obj;
								$scope.patientDetails.patientName = $scope.admissionObj.pFirstName !== undefined && $scope.admissionObj.pFirstName !== null && $scope.admissionObj.pFirstName !== ""? $scope.admissionObj.pFirstName : $scope.admissionObj.patientName;
								$scope.patientDetails.genderCode= ($scope.admissionObj.genderId==1)?"M":($scope.admissionObj.genderId==2?"F":($scope.admissionObj.genderId==3?"OTR":"-"));
								$scope.patientDetails.age= $scope.admissionObj.dob !== undefined && $scope.admissionObj.dob !== null ? moment().diff($scope.admissionObj.dob, 'years') : moment().diff($scope.admissionObj.birthDate, 'years');
								$scope.patientDetails.uhidNumber= $scope.admissionObj.uhidnumber !== undefined && $scope.admissionObj.uhidnumber !== null && $scope.admissionObj.uhidnumber !== "" ? $scope.admissionObj.uhidnumber : $scope.admissionObj.uhIdNumber;
								$scope.patientDetails.dob= $scope.admissionObj.dob !== undefined && $scope.admissionObj.dob !== null && $scope.admissionObj.dob !== "" ? $scope.admissionObj.dob : $scope.admissionObj.birthDate;
								$scope.patientDetails.dobNew= $scope.admissionObj.dob !== undefined && $scope.admissionObj.dob !== null && $scope.admissionObj.dob !== "" ? $scope.admissionObj.dob : $scope.admissionObj.birthDate;
								$scope.patientDetails.doctorFullName= $scope.admissionObj.dFirstName;
								$scope.patientDetails.dateOfAdmission= $scope.admissionObj.doa !== undefined && $scope.admissionObj.doa !== null && $scope.admissionObj.doa !== "" ? $scope.admissionObj.doa : $scope.admissionObj.dateOfAdmission;
								$scope.patientDetails.patientDischargeDate= $scope.admissionObj.pdd !== undefined && $scope.admissionObj.pdd !== null && $scope.admissionObj.pdd !== "" ? $scope.admissionObj.pdd : $scope.admissionObj.patientDischargeDate;
								$scope.patientDetails.wardName=  $scope.admissionObj.wardName !== undefined && $scope.admissionObj.wardName !== null && $scope.admissionObj.wardName !== "" ? $scope.admissionObj.wardName : $scope.admissionObj.wardNameCover;
								$scope.patientDetails.bedNumber= $scope.admissionObj.bedNumber;
								$scope.patientDetails.imagePath= "";
								$scope.patientDetails.patientId= $scope.admissionObj.patientId;
								$scope.patientDetails.visitTypeId= $scope.admissionObj.visitTypeId;
								$scope.patientDetails.admissionId= $scope.admissionObj.admissionId;
								$scope.patientDetails.genderId= $scope.admissionObj.genderId;

								if($scope.patientDetails.doctorFullName!== undefined && $scope.patientDetails.doctorFullName !== null && $scope.patientDetails.doctorFullName !== "" && $scope.patientDetails.doctorFullName.includes("Dr.")){
								    $scope.patientDetails.doctorFullName = $scope.patientDetails.doctorFullName.replace("Dr.","");
								}

								$scope.getInPatientOrderedServices();
								$scope.initLabSampleMaster();
								$rootScope.allergydata($scope.patientDetails);
								$scope.getAllPatientDiagnosisForIcon($scope.patientDetails.patientId);
								
								
								var URI = BASE_URL + ROOT_URL + "/api/opd/getListOfRadiologyTests/"+$scope.admissionObj.admissionId+"/"+$scope.organizationId+"/"+$scope.unitId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$scope.nephrologyList=[];
										$scope.radiology = [];
										angular.forEach(response.data.listObject,function(value,index){
											if(cookieObjectAppConfig.radiologySpecialityId==value.specialityId)
											{
												$scope.radiology.push(value);
											}else if(cookieObjectAppConfig.hemodialysisSpecialityId==value.specialityId){
												$scope.nephrologyList.push(value);
											}else{
												
											}
										})
										console.log("radiology:",$scope.radiology);
										console.log("nephrology list:",$scope.nephrologyList);
									}
								});
								
							}
							
							$scope.labSampleDetails = {};
							
							$scope.allergydata=function(){
								var headerObj = {
										unit_id : $scope.unitId,
										organization_id : $scope.organizationId
									};

									var paramObjForPer = {
										patientId :  $scope.patientDetails.patientId,
										roleId : 2
									};
									
									
									$scope.dt = moment(new Date()).format('DD-MM-YYYY');
									var headerObj = {
											unit_id : $scope.unitId,
											organization_id : $scope.organizationId
										};

										var paramObjForHistList = {
											patientId :  $scope.patientDetails.patientId,
											created_date:$scope.dt
										};
									$rootScope.startLoader();
									var data = {};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL
											+ GETOLDALLERGYPATIENTMAPPER;
									GenericServiceParamHeader
											.serviceAction(METHOD_GET, URI, "",
													NOTIFICATION_MSG_STATUS_FALSE,
													paramObjForHistList, headerObj)
											.then(
													function(response) {
														if (response.data.status == "success")
															$scope.allergyOldList = response.data.listObject;
														console
																.log(
																		"allergyOldList",
																		$scope.allergyOldList);
														if($scope.allergyOldList.length > 0)
														{
															$scope.allergySymbolList=[];
														   angular.forEach($scope.allergyOldList,function(value,index){
															   if(value.isNoKnownAllergies !='Y'){
																   $scope.allergySymbolList.push(value);
															   }
															   if($scope.allergySymbolList.length > 0){
																   $scope.allergyFlag = true;
															   }
															  
														   });
															
														}
													});

									var URI = BASE_URL + ROOT_URL
											+ GETCURRENTALLERGYPATIENTMAPPER;
									GenericServiceParamHeader
											.serviceAction(METHOD_GET, URI, "",
													NOTIFICATION_MSG_STATUS_FALSE,
													paramObjForPer, headerObj)
											.then(
													function(response) {
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.allergyCurrentList = response.data.listObject;
														console
																.log(
																		"allergyCurrentList",
																		$scope.allergyCurrentList);

														$scope.allergyCurrentListDoctor = [];
														if ($scope.roleId == '2') {
															angular
																	.forEach(
																			response.data.listObject,
																			function(
																					value,
																					key) {

																				if (value.roleId == 3
																						&& value.isReviewedFlag == 'N') {
																					var data = {};
																					data.isNoKnownAllergies = value.isNoKnownAllergies;
																					data.remark = value.remark;
																					data.isReviewedFlag = 'N';
																					data.isEnterInErrorStatus = 'N';
																					data.allergyId = value.allergyId;
																					data.allergyTypeId = value.allergyTypeId;
																					data.type = value.type;
																					data.natureOfReaction = value.natureOfReaction;
																					data.severityId = value.severityId;
																					data.month = value.month;
																					data.year = value.year;
																					data.allergyPatientMapperId = value.allergyPatientMapperId;
																					$scope.allergyCurrentListDoctor
																							.push(data);
																				}
																			});

															
														}

														if($scope.allergyCurrentList.length > 0)
														{
															$scope.allergySymbolList=[];
														   angular.forEach($scope.allergyCurrentList,function(value,index){
															   if(value.isNoKnownAllergies !='Y'){
																   $scope.allergySymbolList.push(value);
															   }
															   if($scope.allergySymbolList.length > 0){
																   $scope.allergyFlag = true;
															   }
															  
														   });
															
														}
														$rootScope.stopLoader();
													});
								}
							
							/** code for setting all checkboxes */
							$scope.checkAll = function() {
								if ($scope.selectedAll) {
									$scope.selectedAll = true;
									$scope.isItemChecked = true;
								} else {
									$scope.selectedAll = false;
									$scope.isItemChecked = false;
								}
								angular
										.forEach(
												$scope.sampleCollectionList,
												function(labDetailsObj) {
													if(labDetailsObj.sampleBarcode==null)
													{
													 labDetailsObj.sampleStatusId = $scope.selectedAll;
													}
												});
							}

							
							
							Array.prototype.groupBy = function(prop) {
								  return this.reduce(function(groups, item) {
								    const val = item[prop]
								    groups[val] = groups[val] || []
								    groups[val].push(item)
								    return groups
								  }, {})
								}
								  
							/**Code for printing Barcode*/
							$scope.printBarCode = function()
							{
								var printContents = $window.document.getElementById('barcodePrintingId').innerHTML;
						        var popupWin = $window.open('', '_blank', 'width=300,height=300');
						        popupWin.document.open();
						        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
						        popupWin.document.close();
							}
							
							$scope.options =  {
									   width: 2,
							          height: 60,
							          displayValue: false,
							          font: 'monospace',
							          textAlign: 'left',
							          textPosition: 'bottom',
							          fontSize: 12,
							          backgroundColor: '#ffffff',
							          lineColor: '#000000',
							          margin: 0
							        };
								
							$scope.barcode = {
							        type: "code128",
							        code: '',
							        itemCode: '',
							        batchCode: "123456",
							        uhid : '',
							        patientDetails:'',
							        generationDateTime:''
							      }
							 
							$scope.barcodeList = [];
							
							
							
							
							
							
							
							
							
							
							
							//Save Sample
							
							$scope.saveCollectSample = function()
							{
								
								$scope.sortedList = [];
								$scope.labSampleMasterList = [];
								$scope.sortedList = $scope.listSampleDtls.groupBy('orderId');
								 var labSampleIds = [];
								//console.log("$scope.sortedList",JSON.stringify($scope.sortedList));
								
								if(Object.keys($scope.sortedList).length>1)
									{
									angular.forEach(
											$scope.sortedList,
											function(value, key) 
											{
												var labSampleMaster = angular.copy($scope.labSampleMaster);
												labSampleMaster.patientDetails = $scope.patientDetails.patientName+" "+$scope.patientDetails.age+"/"+$scope.patientDetails.genderCode;
												labSampleMaster.uhid = $scope.patientDetails.uhidNumber;
												labSampleMaster.collectionTime = null;
												labSampleMaster.orderId = key;
												labSampleMaster.admissionId = $scope.admissionObj.admissionId;
												labSampleMaster.updatedBy = $scope.userId;
												labSampleMaster.updatedDate  = new Date().getTime();
												labSampleMaster.listLabSampleDetailsMaster = value;
												$scope.labSampleMasterList.push(labSampleMaster);
											});
									}
								else{
									var labSampleMaster = angular.copy($scope.labSampleMaster);
									labSampleMaster.patientDetails = $scope.patientDetails.patientName+" "+$scope.patientDetails.age+"/"+$scope.patientDetails.genderCode;
									labSampleMaster.uhid = $scope.patientDetails.uhidNumber;
									labSampleMaster.collectionTime = null;
									labSampleMaster.orderId = Object.keys($scope.sortedList)[0];
									labSampleMaster.listLabSampleDetailsMaster = Object.values($scope.sortedList)[0];
									labSampleMaster.updatedBy = $scope.userId;
									labSampleMaster.updatedDate  = new Date().getTime();
									$scope.labSampleMasterList.push(labSampleMaster);
									
								}
							
								//if($scope.saveFlag)
								//	{
									//console.log("$scope.labSampleMaster", JSON.stringify($scope.labSampleMasterList));
									var URI = BASE_URL + ROOT_URL
											+ LIS_TRANSACTION + COLLECT_SAMPLE_IP;

									try {
										console.log("SAVE_SAMPLE_URI", URI);
										GenericService
												.serviceAction("POST", URI,
														$scope.labSampleMasterList)
												.then(
														function(response) {
															if (response.data.status == 'success') 
															{
																$scope.checkLabButn = false;
																$scope.isItemChecked = false;
																$rootScope.stopLoader();
																growl.success(
																				response.data.message,
																				{
																					title : 'Success!'
																				});
																$scope.$broadcast('show-errors-reset');
																if($scope.isPrebarcodeGenerantion == 'Y'){
																$scope.barcodeList = [];
																console.log('response.data.listObject',response.data.listObject);
																angular.forEach(response.data.listObject,
																		function(value, key) 
																		{
																			var barcode  = angular.copy($scope.barcode);
																			barcode.code =value.sampleId;
																			barcode.itemCode =value.patientId;
																			barcode.uhid =value.uhid;
																			barcode.patientDetails =value.patientDetails;
																			barcode.sampleName =value.sampleName;
																			barcode.generationDateTime =value.generationDateTime;
																			$scope.barcodeList.push(barcode);
																			
																			labSampleIds.push(value.labSampleDtlsId);
																			
																		});
																console.log('$scope.barcodeList',JSON.stringify($scope.barcodeList));
																
																if(labSampleIds!=undefined && labSampleIds!=null && labSampleIds.length>0){
																	var data =  
																	{
																            "appName": "HIS",
																            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
																            "reportDesignFilePath": "PathologybarcodePhebo.rpt",
																            "reportFormat": "PDF",
																            "exportFileName": "pathologyReport",
																            "reportParams": [
																              {
																	             "paramName": "lab_sample_Detils_id",
																	             "paramType": "String",
																	             "paramValue": labSampleIds.toString()
																              }
																            ]
																      };
																	  reportService.generate(REPORT_API_URI1,data);
																}
															//	$('#sampleBarCodeIP').modal('show');
																}
																$scope.getInPatientOrderedServices();
																
																$rootScope.stopLoader();
															} else if (response.data.status == 'error') 
															{	
																$scope.checkLabButn = false;
																$rootScope.stopLoader();
																if(response.data.code==409)
																{
																	growl.warning(response.data.message,
																			{
																				title : 'Warning!'
																			});
																	
																	$scope.getInPatientOrderedServices();
																	
																}else{
																	$scope.checkLabButn = false;
																	growl.error(response.data.message,
																			{
																				title : 'Error!'
																			});
																}
															}
														});
										
										
									} catch (e) {
										$scope.checkLabButn = false;
										console.log("Exception", e.message);
										$rootScope.stopLoader();
									}
									
						
							}
							
							
							
							$scope.checkLabButn = false;
							
							$scope.collectAndSaveSample = function() 
							{
								if($scope.checkLabButn == true ){
									return false;
								}
								$scope.checkLabButn = true;
								$('#collectSample').modal('hide');
								
								
								$scope.barcodeValid = false;
								angular.forEach($scope.sampleCollectionList,function(value, key) {
									if (value.sampleStatusId == true && $scope.isPrebarcodeGenerantion == 'N') {
										if(value.sampleBarcode == undefined || value.sampleBarcode == null || value.sampleBarcode == ""){
											$scope.barcodeValid = true;
										}
										
									}
								});
								if($scope.barcodeValid)
								{
									$rootScope.stopLoader();
									growl.warning('Please enter barcode...',
											{
												title : 'warning!!!'
											});
									$scope.checkLabButn = false;
									  return false;
								}
								
								$rootScope.startLoader();
								$scope.listSampleDtls = [];
								var profileOrderDetailsIds =[];
								
								angular.forEach(
												$scope.sampleCollectionList,
												function(value, key) {
													if (value.sampleStatusId == true) {
														var worklistDetails = angular.copy(value);
														worklistDetails.priorityName = undefined;
														worklistDetails.colorCode = undefined;
														worklistDetails.sampleStatusId = SAMPLE_COLLECTED;
														worklistDetails.patientId = $scope.patientDetails.patientId;
														worklistDetails.genderId = $scope.patientDetails.genderId;
														worklistDetails.performTestId  = value.performTestId;
														worklistDetails.assignUnit  = value.assignUnit;
														worklistDetails.colorCode= undefined;
														worklistDetails.orderNo= undefined;
														worklistDetails.serviceCode= undefined;
														worklistDetails.serviceName= undefined;
														worklistDetails.admissionId= undefined;
														worklistDetails.uhidNumber= undefined;
														worklistDetails.patientName= undefined;
														worklistDetails.specialityId= undefined;
														worklistDetails.sampleCollectionBy = $scope.userId;
														worklistDetails.collectionTime = null;
														worklistDetails.updatedDate  = new Date().getTime();
														worklistDetails.createdDate  = new Date().getTime();
														worklistDetails.listTMicroSampleMediaDto=[];	
														
														if(worklistDetails.profileId!=null)
															{
															  profileOrderDetailsIds.push(worklistDetails.orderDetailsId);
															}
														$scope.listSampleDtls
																.push(worklistDetails);
													}
												});
								
								if($scope.listSampleDtls.length>0)
								{
								 $scope.saveFlag = true;
								 if(profileOrderDetailsIds.length>0)
									 {
									 var count = 0;
									 var sortedList = new Set(profileOrderDetailsIds);
									 angular.forEach(sortedList,
												function(orderDetailsId, key)
												{
										          count++;
											      var found = $scope.sampleCollectionList.find(ar=>(ar.orderDetailsId==orderDetailsId && (ar.sampleStatusId==false||ar.sampleStatusId==null) && ar.sampleBarcode==null));
											      if(found)
											    	 {
											    		growl.error("Please select all panel services.",
																{
																	title : 'Error!'
																});
											    		$rootScope.stopLoader();
											    		$scope.saveFlag = false;
											    		$scope.checkLabButn = false;
											    	    return false;
											    	 }
											      else if(count==sortedList.size && $scope.saveFlag)
											    	  {
											    	    $scope.saveCollectSample();
											    	  }
										        });
									 }
								 else{
									   $scope.saveCollectSample();
								     }								
								}
								else{
									$scope.barcodeList = [];
									$rootScope.stopLoader();	
									$scope.checkLabButn = false;
									growl.error('Please Select Sample.',
											{
												title : 'Error!'
											});
								}
							}
							$scope.reprintBarCode = function(sampleId,labSampleDtlsId)
							{
								try 
								{
									if(sampleId==null)
									{
									$rootScope.stopLoader();
									growl.error('Please Collect Sample to Generate Barcode.',
											{
												title : 'Error!'
											});
									  return false;
									}
									$scope.barcodeList = [];
									var newDate = {};
									
									var labSampleIds = [];
									var data =[];
									data.push(sampleId);
									newDate.sampleIds = data;
									newDate.labSampleDetialsId = labSampleDtlsId;
									$rootScope.startLoader();
									var URI = BASE_URL 
									        + ROOT_URL
									        + LIS_TRANSACTION
											+ '/reprint/Barcode'
									console.log("URI", URI);
									 GenericService.serviceAction("POST", URI,
											 newDate).then(
													 function(response) {
															if (response.data.status == 'success') {
																$rootScope.stopLoader();
																angular.forEach(response.data.listObject,
																		function(value, key) 
																		{
																			var barcode  = angular.copy($scope.barcode);
																			barcode.code =value.sampleId;
																			barcode.itemCode =value.patientId;
																			barcode.uhid =value.uhid;
																			barcode.patientDetails =value.patientDetails;
																			barcode.sampleName =value.sampleName;
																			barcode.generationDateTime =value.generationDateTime;
																			$scope.barcodeList.push(barcode);
																		});
																labSampleIds.push(labSampleDtlsId);
																
																if(labSampleIds!=undefined && labSampleIds!=null && labSampleIds.length>0){
																	var data =  
																	{
																            "appName": "HIS",
																            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
																            "reportDesignFilePath": "PathologybarcodePhebo.rpt",
																            "reportFormat": "PDF",
																            "exportFileName": "pathologyReport",
																            "reportParams": [
																              {
																	             "paramName": "lab_sample_Detils_id",
																	             "paramType": "String",
																	             "paramValue": labSampleIds.toString()
																              }
																            ]
																      };
																	  reportService.generate(REPORT_API_URI1,data);
																}
															//	$('#sampleBarCodeIP').modal('show');
																//$scope.printBarCode();
																$rootScope.stopLoader();
															} else if (response.data.status == 'error') {
																growl
																		.error(
																				response.data.message,
																				{
																					title : 'Error!'
																				});
																$rootScope.stopLoader();
															}
														});
								} catch (e) {
									console.log(e.message)
								}
							}
					
							//$scope.getInPatientOrderedServicesForRadiology();
							
							//vivek Start
							
							/*$scope.getRadiologyServices = function() 
							{
								
								$scope.encounterSearchObject = {
										'organizationId' : $scope.organizationId,
										'unitId' : $scope.unitId,
										'radiologySpecialityId' :cookieObjectAppConfig.radiologySpecialityId
									}
								
								$rootScope.startLoader();
								try {

									var URI = BASE_URL + ROOT_URL
									+ "/api/opd/getRadiologyListForSampleCollection";

							GenericServiceParamHeader
									.serviceAction(METHOD_POST, URI,
											$scope.encounterSearchObject,
											NOTIFICATION_MSG_STATUS_FALSE,
											"", "")
									.then(
											function(response) {
												console.log(response);
												// $rootScope.startLoader();
												$scope.radiologyServiceList = [];
												if (response.data.status == "success")
													{
													  $scope.radiologyServiceList = response.data.listObject;
													}
												$rootScope.stopLoader();
													});
								} catch (e) {
									console.log(e.message)
								}
							}*/
							
							/*$scope.nephrologySpecialityId="";
							$scope.nephrologyId = function() {
							var appunitdata = {
									  organizationId : $scope.organizationId,
									  unitId:$scope.unitId
								    
								   }
							
							var URI = BASE_URL + ROOT_URL
							+ "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";								
						   GenericService
							.serviceAction("POST", URI, appunitdata
									)
							.then(
									function(response) {
										console.log(response);
										$scope.configList = [];
										if (response.data.status == "success")
											$scope.configList = response.data.listObject;
										if($scope.configList[0].hemodialysisSpecialityId!=null){
											$scope.nephrologySpecialityId=$scope.configList[0].hemodialysisSpecialityId;
											console.log($scope.nephrologySpecialityId)
											
										}else{
											 growl.warning(
														'Hemodialysis speciality is not configure so, please configure .... !!',
														{
															title : 'Warning!'
														});
											return false;
										}
										
									});
							}
							*/
						/*	$scope.nephrologyId(); */
							
							$scope.clearDetails=function(){
								$scope.fromTimeString = "";
								$scope.fromTimeStringSlot="";
								$scope.slotId="";
								 $scope.modalityId="";
								 $scope.slotDetailsList=[];
								 $scope.loadingMsg="";
							}
							
							$scope.radSpecialityId = 0;
							$scope.radSubSpecialityId = 0;
							
							$scope.selectedSpecialityId="";
							$scope.getModalityBySubSpeciality = function(item) {
								
							console.log($scope.nephrologySpecialityId)
								$scope.slotCheckTime = moment(new Date()).format("HH:mm");
								$scope.fromTimeString = "";
								$scope.modalityMasterList = [];
								$scope.admissionId = item.admissionId;
								$scope.radSpecialityId = item.specialityId;
								$scope.radSubSpecialityId = item.subSpecialityId;
								$scope.orderDetailsId = item.orderDetailsId;
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									specialityId : item.specialityId,
									subSpecialityId :item.subSpecialityId
								}
								console.log(data);
								//$rootScope.startLoader();
								//return false
								if(item.specialityId==cookieObjectAppConfig.hemodialysisSpecialityId){
									$scope.selectedSpecialityId=item.specialityId;
									$scope.modalityMasterList= null;
									var dataObj = {
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
									};
									console.log(data);
									var URI = BASE_URL + ROOT_URL
									+ "/api/adt/getDialysisChair";								
									GenericService
									.serviceAction("POST", URI, dataObj)
							.then(
									function(response) {
										console.log(response);
										$scope.bedList = [];
										if (response.data.status == "success")
											$scope.bedList = response.data.listObject;
										console.log($scope.chairList)
									});
								}
								
								
								if(item.specialityId!=cookieObjectAppConfig.hemodialysisSpecialityId){
									$scope.selectedSpecialityId=item.specialityId;
									$scope.bedList = null;
									var URI = BASE_URL + ROOT_URL
										+ GETMODALITYBYSUBSPECIALITYID;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													$scope.modalityMasterList = [];
													if (response.data.status == "success") {
														$scope.modalityMasterList = response.data.listObject;
													 console.log($scope.modalityMasterList)
													}

												});
								}
							}
							
							//For Date Difference For Modality Start
					       	$scope.daysDiffrence = function(date) {
					       	    var today = new Date();
					       	    var dd = today.getDate();
					       	    var mm = today.getMonth() + 1; //January is 0!
					       	    var yyyy = today.getFullYear();
					       	    if (dd < 10) {
					       	      dd = '0' + dd
					       	    }
					       	    if (mm < 10) {
					       	      mm = '0' + mm
					       	    }
					       	    today = yyyy + '/' + mm + '/' + dd;
					       	    $scope.today = today;
					       	    var date2 = new Date(today);
					       	    var date1 = new Date(date);
					       	    var timeDiff = Math.abs(date2.getTime() - date1.getTime());
					       	    $scope.dayDifference = Math.ceil(timeDiff / (1000 * 3600 * 24));
					       	    return $scope.dayDifference;
					       	  }
					       	//For Date Difference for Modality END
							//Function For Get Date Wise Slots
							  $scope.itemId="";
							  $scope.itemObj={};
							  $scope.globleObj={};
							  $scope.loadingMsg="";
							$scope.getSlotsOfDate = function(item) {
								$scope.slotDetailsList = [];
								$scope.loadingMsg="Loading....";
								if(item!=undefined){
									$scope.itemObj=JSON.parse(item);
									$scope.globleObj=JSON.parse(item);
								}else{
									$scope.itemObj=$scope.globleObj;
								}
								console.log(item)
								
								
								$scope.mobilityId="";
								//$scope.itemObj=JSON.parse(item);
								$scope.itemId= $scope.itemObj.itemId;
								$scope.mobilityId=$scope.itemObj.modalityId
								console.log($scope.itemObj.itemId);
								$scope.slotDetailsList = [];
								$scope.fromTimeString = "";
								 
								if($scope.itemObj.bedId > 0 || $scope.mobilityId > 0)
								{
								var dt = moment($scope.datepicker).format('YYYY-MM-DD');
								$scope.diffDaysMod = $scope.daysDiffrence(moment($scope.datepicker).format('YYYY/MM/DD'));
								//$scope.radSpecialityId = 0;
								//$scope.radSubSpecialityId = 0;
								
									var data = {
										subSpecialityId : $scope.radSubSpecialityId,
										specialityId : $scope.radSpecialityId,
									//	modalityId : $scope.mobilityId,
										appointmentDate : dt,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										
									};
									
									if($scope.itemObj.bedId!=null && $scope.itemObj.bedId!=undefined){
										console.log($scope.itemId)
										data.slotTypeId =8;
										data.bedId=$scope.itemObj.bedId;
										//data.modalityId=$scope.itemId;
									}
									else{
										
										data.slotTypeId =2;
										data.visitTypeId =$scope.patientDetails.visitTypeId,
										data.modalityId=$scope.mobilityId;	
									}
								console.log(data);
								//return false;
								var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													
													// $rootScope.startLoader();
													if (response.data.status == "success") {
														$scope.slotDetailsList = response.data.listObject;
														if($scope.slotDetailsList.length==0){
															$scope.loadingMsg="Slots are not created for selected date...!";
														}
														console.log($scope.slotDetailsList);
						}

												});
							}
								else
									{
									growl.error("Please Select Modality",{
										title : ERROR_MSG
									});
									return false;
										
									}

							}
							
							$scope.slotBooking = function(item)
							{
								$scope.fromTimeStringSlot = item.fromTimeString;
								$scope.fromTimeStringSlotShow = item.fromTimeString2;
								 $scope.slotId = item.slotId;
							}
							
							$scope.checkRIS = false;
							$scope.saveAppointment = function(){
								
								if($scope.checkRIS == true){
									return false;
								}
								$scope.checkRIS = true;
								angular.element('#info').modal('hide');
								console.log($scope.itemId);
								if($scope.modalityId=="" || $scope.modalityId==undefined){
									growl.error("Please Select Modality",{
										title : ERROR_MSG
									});
									$scope.checkRIS = false;
									return false;
								}
								if($scope.slotId=="" || $scope.slotId==undefined){
									growl.error("Please Select Slots",{
										title : ERROR_MSG
									});
									$scope.checkRIS = false;
									return false;
								}
								
								$rootScope.startLoader();
								$scope.currentDate = new Date();
								$scope.currentDate = moment(
										$scope.currentDate).format(
										'DD-MM-YYYY HH:mm:ss');
								console.log($scope.admissionObj.patientId);
								var data = {
									slotId : $scope.slotId,
									remark : "",
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate,
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									status : 'A',
									slotStatusId : 4,
									priorityId : 1,
									allowedOverBookingSlots : 0,
									admissionId : $scope.admissionId,
									orderDetailsId:$scope.orderDetailsId,
									appointmentStatusId:1,
									patientId :$scope.admissionObj.patientId
								}
								if($scope.selectedSpecialityId==cookieObjectAppConfig.hemodialysisSpecialityId){
									data.appointmentTypeId=6;
								}else{
									data.appointmentTypeId=2;
								}
									
										var URI = BASE_URL + ROOT_URL
											+ "/api/opd/scheduleRadiologyAppointmentForInPatient";
								
								console.log(data);
								//return false; 

								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_TRUE)
										.then(
												function(response) {
													console.log(response);
													//$rootScope.startLoader();
													if (response.data.status == "success") {
														$scope.checkRIS = false;
													     $rootScope.stopLoader();
														//$scope.getPatientDetailsById($scope.admissionObj.patientId);
														$scope.getRadioLogyServices($scope.admissionObj.admissionId);
														$scope.getInPatientOrderedServices();
														//$scope.getInPatientOrderedServicesForRadiology();
														$scope.clearDetails();
														$scope.getInPatientOrderedServices();
														$rootScope.stopLoader();
														
														
													}
													else if(response.data.status == "error") 
													{
															angular
															.element(
																	'#info')
															.modal(
																	'hide');
															$scope.checkRIS = false;
															$rootScope.stopLoader();
													}
												});
							}
							
							 $scope.getPerformTestId = function(collectedSample,index){
								 

	                             var newarr = {};
	                             newarr.serviceId = collectedSample.serviceId;
	                             newarr.assignUnit = collectedSample.assignUnit;
	                             
		                           $rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/LIS/transaction/getPerformTestId";
									GenericService.serviceAction("POST",URI,newarr).then(function(response){
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											if(response.data.object!= null){
												collectedSample.testId = response.data.object.performTestId;
											}else{
												collectedSample.assignUnit = $scope.orgUnitId;
												
												growl.error("Assign Unit Does Not Perform This Test !!!",
														{
															title : 'Error!'
														});
											}
										
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									});
	                             
								 
							 }
							 $scope.getClassForColor = function(info)
			                    {
			                        	   if(info.priorityId==2){
	                                 		return 'td-bg-red';
			                        	   }
			                    }	
							 
							 /** Priyanka start*/
							 $scope.isItemChecked = false;
							 $scope.SelectedRequestIds = [];
							 
							 $scope.SelectStoreIndent = function (indent) {
							      var indentIds = $scope.SelectedRequestIds;

							      if (indent.sampleStatusId) {
							        indentIds.push(indent.orderDetailsId);
							      } else {
							        var index = indentIds.indexOf(indent.orderDetailsId);
							        if (index > -1) {
							          indentIds.splice(index, 1);
							        }
							      }

//							      if ($scope.sampleCollectionList.every($scope.sampleStatusId())) {
//							    	  $scope.selectedAll = true;
//							      } else {
//							    	  $scope.selectedAll = false;
//							      }
							      $scope.isItemChecked = false;
							      $scope.sampleCollectionList.forEach(function (indent) {
							        if (indent.sampleStatusId == true) {
							        	$scope.isItemChecked = true;
							          return;
							        }
							      });
							    }
							 
//							 $scope.sampleStatusId = function (indent) {
//							      if (!$scope.isDisable(indent))
//							        return indent.sampleStatusId;
//							      else
//							        return true;
//							    }
//							 $scope.isDisable = function (indent) {
//							      if (indent.status != 'New') {
//							        return true;
//							      }
//							      return false;
//							    }
							 
							
						} ]);