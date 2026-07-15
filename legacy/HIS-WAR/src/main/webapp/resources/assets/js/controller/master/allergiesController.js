/**
 * @author sneha d
 */

angular
		.module('myApp')
		.controller(
				"allergiesController",
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
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactory,
								$sessionStorage, growl,$interval,$stateParams,GenericServiceParamHeader,BillingGenericService,$window) {

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

							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							 if(cookieObjectAppConfig == undefined){
							  $state.go('login');
							  return;
							 }
							 else
							  {
							   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
							   
							  }
							

							 var labConfig  =    $scope.checkLabConfig();
								$scope.deptId = labConfig.labSpecialityId;
							/* code for setting Label */
							$(".selectedPageName").text("Allergy");
							
							$scope.openDatePickerPatiSearch = function() {
								$scope.datepicker1.opened = true;
							};

							$scope.datepicker1 = {
									opened : false
								};
							
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

								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};
								
								$scope.datepicker = new Date();
							
								
								//for resolved Allergy
								 $scope.dateOptionsForResolvedAllergy =
									{
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate :new Date()
									};
							        
						        $scope.openResolvedAllergyPicker = function() {
									$scope.resolvedAllergyDatepicker.opened = true;
								};
								$scope.resolvedAllergyDatepicker = {
										opened : false
								};
									
									
								 $scope.billing = true;
								 
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
								      "organizationId"  :$scope.organizationId
								    }
							$scope.patientAllergyObject = {
									isNoKnownAllergies : 'N',
									allergyTypeId : 0,
									allergyId : 0,
									month : "",
									year : "",
									type : "",
									natureOfReaction : "",
									severityId : "",
									remark : "",
									isEnterInErrorStatus : "N",
									isReviewedFlag : "N",
									genericId : 0

								};
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
										$scope.patientDetails.visitTypeId=1;
										$scope.patientDetails = {};
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
									$scope.patientSearchObj.birthDate="";
								});
							}
							$scope.admissionId=null;
							$scope.departmentId=null;
							//get Patient Id,department id and template id
							$scope.patientId=null;
							$scope.viewpage=false;
							$scope.patientDetails.visitTypeId=1;
							
							
							
							$scope.getPatientAllergyDetails = function(patientId){
								$scope.dt = moment(new Date()).format('DD-MM-YYYY');
								var headerObj = {
										unit_id : $scope.orgUnitId,
										organization_id : $scope.orgId
									};

									var paramObjForHistList = {
										patientId :patientId,
										created_date:$scope.dt
									};
								var URI = BASE_URL + ROOT_URL
								+  "/api/opd/getCurrentAllergyPatientMapperForCoversheet";
						GenericServiceParamHeader
								.serviceAction(METHOD_GET, URI, "",
										NOTIFICATION_MSG_STATUS_FALSE,
										paramObjForHistList, headerObj)
								.then(
										function(response) {
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.allergyCurrentList = response.data.listObject;
										});
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
									console .log( "allergyOldList",$scope.allergyOldList);
								
								});
				
				 
					var headerObj = {
							unit_id : $scope.unitId,
							organization_id : $scope.organizationId
						};

						var paramObjForHistListf = {
							 patientId :patientId,
						};
				var data = {};
			var URI = BASE_URL + ROOT_URL + "/api/opd/getPatientAllergyListForIcon";
		GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,
					NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistListf,headerObj)
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
							  
						   });
							
						}
						
					});
		
		
		
		
								$rootScope.startLoader();
								var headerObj1 = {
										unit_id : $scope.unitId,
										organization_id : $scope.organizationId
									};
						
									var paramObjForHistList1 = {
											patientId : patientId,
										};
								var URI = BASE_URL + ROOT_URL
								+ "/api/opd/getResolvedAllergyPatientMapperForCoversheet";
							GenericServiceParamHeader
								.serviceAction(METHOD_GET, URI,
										data,
										NOTIFICATION_MSG_STATUS_FALSE,paramObjForHistList1,headerObj1)
								.then(
										function(response) {
											console.log(response);
											$scope.resolvedAllergyHistoryList=[];
											if (response.data.status == "success"){
												$scope.resolvedAllergyHistoryList = response.data.listObject;
												$rootScope.stopLoader();
											}else{
												$rootScope.stopLoader();
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
		
							$scope.getPatientDetailsById = function(patientId){
							
								$scope.patientDetailsList = [];
								$scope.patientDetails = {};
								$scope.masterObj = {};
								$scope.DischargeSummaryFinalList=[];
								$scope.patientTemplateList=[];
								$scope.viewpage=true;
								$scope.patientId=patientId;
								$scope.patientDetails.patientId=patientId;
								var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$scope.patientDetailsList = response.data.listObject;
										$scope.patientDetails = response.data.listObject[0];
										console.log("PatientDetailList:",$scope.patientDetailsList);
										console.log("PatientDetail:",$scope.patientDetails);
										
//										$scope.admissionObj = $stateParams.obj;
										
										$scope.patientDetails.patientName = $scope.patientDetailsList[0].firstName + " " +$scope.patientDetailsList[0].lastName;
										$scope.patientDetails.genderCode= ($scope.patientDetailsList[0].genderName=="Male")?"M":($scope.patientDetailsList[0].genderName=="Female"?"F":($scope.admissionObj.genderId==3?"OTR":"-"));
										$scope.patientDetails.age= $scope.patientDetailsList[0].ageString;
										$scope.patientDetails.uhIdNumber= $scope.patientDetailsList[0].uhidNumber;
										$scope.patientDetails.birthDate= $scope.patientDetailsList[0].dob;
										$scope.patientDetails.dateOfAdmission= $scope.patientDetailsList[0].dateOfAdmission;
										$scope.patientDetails.patientDischargeDate= $scope.admissionObj.pdd;
										$scope.patientDetails.bedNumber= $scope.patientDetailsList[0].bedNumber;
										$scope.patientDetails.imagePath= "";
										$scope.patientDetails.patientId= $scope.patientDetailsList[0].patientId;
										$scope.patientDetails.visitTypeId= $scope.patientDetailsList[0].visitTypeId;
										$scope.patientDetails.admissionId= $scope.patientDetailsList[0].admissionId;
										$scope.patientDetails.genderId= $scope.patientDetailsList[0].genderId;
										$scope.patientDetails.visitTypeId=1;

										var patientIndex = $scope.patientList.findIndex(function(patient) {
										  return patient.patientId === patientId;
										});
										var doctorNameForInfo = '';
										if(patientIndex !== undefined && patientIndex !== null){
										   doctorNameForInfo = $scope.patientList[patientIndex].doctorName;
										}

										$scope.patientDetails.doctorFullName = doctorNameForInfo !== undefined && doctorNameForInfo !== null && doctorNameForInfo !== '' ? doctorNameForInfo : $scope.patientDetailsList[0].doctorName;
									}
								});
								
								$scope.getPatientAllergyDetails($scope.patientId);
							
							}
							
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
											+ S + $scope.deptId;
									console.log("GET_IN_PATIENT_ORDERED_SERVICES", URI);
									GenericService
											.serviceAction("GET", URI, data)
											.then(
													function(response) 
													{
														$scope.sampleCollectionList=[];
														$scope.sampleCollectionList = response.data.listObject;
														$rootScope.stopLoader();
														
													});
									$rootScope.stopLoader();
								} catch (e) {
									console.log(e.message)
								}
							}
							
							
							
							
							$scope.patientDetails
                            
							/**Redirect Page Code*/
							if(!angular.equals({},$stateParams.obj))
 							{
								
								$scope.viewpage=true;
								$scope.admissionObj = $stateParams.obj;
								$scope.patientDetails.patientName = $scope.admissionObj.pFirstName;
								$scope.patientDetails.genderCode= ($scope.admissionObj.genderId==1)?"M":($scope.admissionObj.genderId==2?"F":($scope.admissionObj.genderId==3?"OTR":"-"));
								$scope.patientDetails.age= moment().diff($scope.admissionObj.dob, 'years');
								$scope.patientDetails.uhIdNumber= $scope.admissionObj.uhidnumber;
								$scope.patientDetails.birthDate= $scope.admissionObj.dob;
								$scope.patientDetails.doctorFullName= $scope.admissionObj.dFirstName;
								$scope.patientDetails.dateOfAdmission= $scope.admissionObj.doa;
								$scope.patientDetails.patientDischargeDate= $scope.admissionObj.pdd;
//								$scope.patientDetails.wardName= $scope.admissionObj.wardName;
								$scope.patientDetails.initiateTransferOfCare=true;
								$scope.patientDetails.wardNameInitiate= $scope.admissionObj.wardName;
								$scope.patientDetails.bedNumber= $scope.admissionObj.bedNumber;
								$scope.patientDetails.imagePath= "";
								$scope.patientDetails.patientId= $scope.admissionObj.patientId;
								$scope.patientDetails.visitTypeId= $scope.admissionObj.visitTypeId;
								$scope.patientDetails.admissionId= $scope.admissionObj.admissionId;
								$scope.patientDetails.genderId= $scope.admissionObj.genderId;
								
								var dateDoa = moment( $stateParams.obj.doa, 'DD/MM/YYYY');
								var datePdd = moment( $stateParams.obj.pdd, 'DD/MM/YYYY');
								
								$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
								
								
								$scope.getInPatientOrderedServices();
								$scope.initLabSampleMaster();
								$scope.getPatientAllergyDetails($scope.patientDetails.patientId);
							
							}
							
							$scope.labSampleDetails = {};
							
							// Start get 50 Previous Year with Current Year
							// and month

							var year = new Date().getFullYear();
							var range = [];

							range.push(year);

							for (var i = 1; i < 50; i++) {
								range.push(year - i);
							}

							$scope.years = range;

							$scope.months = [];
							$scope.months.push({
								value : 1,
								text : 'January'
							});
							$scope.months.push({
								value : 2,
								text : 'February'
							});
							$scope.months.push({
								value : 3,
								text : 'March'
							});
							$scope.months.push({
								value : 4,
								text : 'April'
							});
							$scope.months.push({
								value : 5,
								text : 'May'
							});
							$scope.months.push({
								value : 6,
								text : 'June'
							});
							$scope.months.push({
								value : 7,
								text : 'July'
							});
							$scope.months.push({
								value : 8,
								text : 'August'
							});
							$scope.months.push({
								value : 9,
								text : 'September'
							});
							$scope.months.push({
								value : 10,
								text : 'October'
							});
							$scope.months.push({
								value : 11,
								text : 'November'
							});
							$scope.months.push({
								value : 12,
								text : 'December'
							});

							// End 50 Previous Year with Current Year
							// and month

							   $scope.removeTempServicesByIndex = function(index){
						    		// var serviceIndex = $scope.servicesListPopup.indexOf(index);
						    		$scope.allergyTypeList.splice(index, 1);
						    	}
						        
							$scope.selectGenericIdForAllergy = function(info,
									model, label) {
								// alert('dv'+info.genericId);
								$scope.patientAllergyObject.genericId = info.genericId;
							}
                           
							
							$scope.allergyList = function() {
								var data = {};
								var headerObj = {
										organization_id : $scope.orgId,
										 unit_id : $scope.orgUnitId,
									};
									var URI = BASE_URL + ROOT_URL
									+ GETALLERGYTYPEMASTER;
							GenericServiceParamHeader
									.serviceAction(METHOD_GET, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE,
											data, headerObj)
									.then(
											function(response) {
												console.log(response);
												if (response.data.status == "success")
													$scope.allergyTypeList = response.data.listObject;
											});
									}
							$scope.allergyList();
							$scope.severityList = function() {
								var data = {};
								var headerObj = {
										unit_id : $scope.orgId,
										organization_id : $scope.orgUnitId,
									};
							var URI = BASE_URL + ROOT_URL
							+ GETSEVERITYMASTER;
					GenericServiceParamHeader
							.serviceAction(METHOD_GET, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE,
									data, headerObj)
							.then(
									function(response) {
										console.log(response);
										// $rootScope.startLoader();
										if (response.data.status == "success")
											$scope.severityList = response.data.listObject;
									});
							}
							$scope.severityList();
							$scope.allergyShowFlag = true;
							
							//$scope.setAllergyDetails();	 Ganesh Comment
							$scope.getAllergenListByAllergyId = function(id) {
								var isGeneric = $.grep($scope.allergyTypeList,
										function(disCat) {
											return disCat.allergyTypeId == id;
										})[0].isGeneric;
								if (isGeneric == 'N') {
									$scope.allergyShowFlag = true;
									var paramObjForPer = {
										allergy_type_id : id
									};
									var data = {};
									var headerObj = {
										organization_id : $scope.organizationId,
										unit_id : $scope.unitId
									};
									var URI = BASE_URL + ROOT_URL
											+ GETALLERGYMASTERBYALLERYTYPEID;
									GenericServiceParamHeader
											.serviceAction(
													METHOD_GET,
													URI,
													data,
													NOTIFICATION_MSG_STATUS_FALSE,
													paramObjForPer, headerObj)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.allergenList = response.data.listObject;
													});
								} else {
									$scope.allergyShowFlag = false;
								}

							}
							$scope.allergyButtonFlag = true;
							
					        $scope.searchAllergeon = function(searchKeyword)
					        {
					        	var data = {};
								
								var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									genericSearchName : searchKeyword
								}
								console.log(data);

								var URI = BASE_URL + ROOT_URL
										+ GETGENERICLISTAUTOFILLSERACH;
								
								return GenericServiceParamHeader
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE,"","")
										.then(
												function(response) {
													console.log(response);
													// $rootScope.startLoader();
													if (response.data.status == "success")
														return response.data.listObject;
												});
					        }
					        
				
				$scope.savePatientAllergy = function() {
				$rootScope.startLoader();
				if($scope.viewpage == false)
				  {
					growl.warning("Please select Patient.",{
						title : ERROR_MSG
						
					});
					$rootScope.stopLoader();
					return false;
				  }
				if( $scope.patientAllergyObject.isNoKnownAllergies=='Y'){
					var flag=false;
					if($scope.allergyCurrentList.length>0){								
						
						angular.forEach($scope.allergyCurrentList, function(value,key) {
							if (value.isNoKnownAllergies=='Y' && value.isReviewedFlag == 'N') {										
								flag=true;
								return false;
							}
						});
						
					}
					if(flag){
						growl.error("Already saved",
								{
									title : 'Error!'
								});
						$rootScope.stopLoader();
						return false;
					}
				}
				else{
					 
					if($scope.patientAllergyObject.allergyTypeId ==""){
						
						growl.error("Please Select Allergy Type ", {
							title : 'Error!'
						});
						$rootScope.stopLoader();
						return false;
					} else if($scope.patientAllergyObject.allergyId == 0 && $scope.patientAllergyObject.genericId == 0 ){
					
						growl.error("Please Select Allergen", {
							title : 'Error!'
						});
						$rootScope.stopLoader();
						return false;
					}else if($scope.patientAllergyObject.month == ""){
						
						growl.error("Please Select Month", {
							title : 'Error!'
						});
						$rootScope.stopLoader();
						return false;
					}else if($scope.patientAllergyObject.year ==""){
						
						growl.error("Please Select Year", {
							title : 'Error!'
						});
						$rootScope.stopLoader();
						return false;
					}else if($scope.patientAllergyObject.type == ""){
						
						growl.error("Please Select Type", {
							title : 'Error!'
						});
						$rootScope.stopLoader();
						return false;
					}else if($scope.patientAllergyObject.natureOfReaction ==""){
						
						growl.error("Please Select Nature of Reaction ", {
							title : 'Error!'
						});
						$rootScope.stopLoader();
						return false;
					}else if($scope.patientAllergyObject.severityId ==""){
						
						growl.error("Please Select Severity", {
							title : 'Error!'
						});
						$rootScope.stopLoader();
						return false;
					}
				}
				$scope.saveAllergyList = [];
				$scope.saveAllergyList.push($scope.patientAllergyObject);
				var data = {
						createdDate : moment(new Date()).format(
								'DD-MM-YYYY HH:mm:ss'),
						updatedDate : moment(new Date()).format(
								'DD-MM-YYYY HH:mm:ss'),
						updatedBy : $scope.userId,
						createdBy : $scope.userId,
						status : 'A',
						patientId : $scope.patientDetails.patientId,
						admissionId : $scope.patientDetails.admissionId,
						roleId : 2,
						comments : ''
					

					}

					console.log('ds',$scope.allergyCurrentListDoctor);
				
				$scope.allergyCurrentListDoctor1=[];
					// angular.extend($scope.dataListComplaints,$scope.complaintsPatientListDoctor);
					angular.forEach($scope.saveAllergyList, function(value , key) {
							if(value.allergyTypeId > 0){
								if($scope.allergyCurrentList!=undefined && $scope.allergyCurrentList.length>0){
									var chkStatus= $scope.allergyCurrentList.filter(function(item)
									{ 
										return (item.allergyTypeId==value.allergyTypeId && 
												(item.allergyId==value.allergyId ||
												item.genericId==value.genericId) &&
												item.month==value.month &&
												item.year==value.year);
	    									})
    	    								if(chkStatus.length>0){
    	    									growl.error("Already exist",
    			    	    							{
    			    	    							title : 'Error!'
    			    	    							});
    			    	    							$rootScope.stopLoader();
    			    	    							return false;
    	    								}
	    								}
						
								
								if($scope.allergyOldList!=undefined && $scope.allergyOldList.length>0){
									var chkStatus= $scope.allergyOldList.filter(function(item)
									{ 
										return (item.allergyTypeId==value.allergyTypeId && 
												(item.allergyId==value.allergyId ||
												item.genericId==value.genericId) &&
												item.month==value.month &&
												item.year==value.year);
	    									})
    	    								if(chkStatus.length>0){
    	    									growl.error("Already exist",
    			    	    							{
    			    	    							title : 'Error!'
    			    	    							});
    			    	    							$rootScope.stopLoader();
    			    	    							return false;
    	    								}
	    								}
								
				 				var dData = {
				 						isNoKnownAllergies:value.isNoKnownAllergies,
							    	 	allergyTypeId:value.allergyTypeId,
							    	 	allergyId:value.allergyId,
							    	 	genericId:value.genericId,
							    	 	month:value.month,
							    	 	year:value.year,
							    	 	type:value.type,
							    	 	natureOfReaction:value.natureOfReaction,
							    	 	severityId:value.severityId,
							    	 	remark:value.remark,
							    	 	comments:value.comments,
							    	 	isEnterInErrorStatus:value.isEnterInErrorStatus,
										isReviewedFlag:value.isReviewedFlag,
				 				}
				 				
				 				$scope.allergyCurrentListDoctor1.push(dData);
							}else if(value.allergyTypeId == 0 && value.isNoKnownAllergies=='Y'){
								var dData = {
										isNoKnownAllergies:value.isNoKnownAllergies,
							    	 	allergyTypeId:value.allergyTypeId,
							    	 	allergyId:value.allergyId,
							    	 	genericId:value.genericId,
							    	 	month:value.month,
							    	 	year:value.year,
							    	 	type:value.type,
							    	 	natureOfReaction:value.natureOfReaction,
							    	 	severityId:value.severityId,
							    	 	remark:value.remark,
							    	 	comments:value.comments,
							    	 	isEnterInErrorStatus:value.isEnterInErrorStatus,
										isReviewedFlag:value.isReviewedFlag,
									}
								$scope.allergyCurrentListDoctor1.push(dData);
							}else {
								growl.error("Please Enter Allergy ",
										{
											title : 'Error!'
										});
								$rootScope.stopLoader();
								return false;
								
							}
						
						});
					data.listOfAllergyPatientMapperDto = $scope.allergyCurrentListDoctor1;
					if($scope.allergyCurrentListDoctor1.length > 0){
						
						
						// console.log($scope.patientAllergyObject);
						var paramObj = {};
						var headers = {
							'organization_id' : $scope.organizationId,
							'unit_id' : $scope.unitId,
							'Content-Type' : 'application/json'
						};
						console.log(data);
						// return false;
						var URI = BASE_URL + ROOT_URL
								+ SAVEALLERGYPATIENTMAPPER;

						GenericServiceParamHeader.serviceAction(
								METHOD_POST, URI, data,
								NOTIFICATION_MSG_STATUS_TRUE, paramObj,
								headers).then(function(response) {
							console.log(response);
							// $rootScope.startLoader();
							if (response.data.status == "success")
								$rootScope.stopLoader();
							$scope.allergyCurrentListDoctor=[];
							$scope.genericName="";
							$scope.patientAllergyObject.genericId="";
							$scope.patientAllergyObject.allergyTypeId="";
							$scope.patientAllergyObject.allergyId="";
							$scope.patientAllergyObject.month="";
							$scope.patientAllergyObject.year="";
							$scope.patientAllergyObject.type="";
							$scope.patientAllergyObject.natureOfReaction="";
							$scope.patientAllergyObject.severityId="";
							$scope.patientAllergyObject.remark="";
							$scope.patientAllergyObject.isNoKnownAllergies="N";
							$scope.genericName = "";
							$scope.genericId = "";
							 $scope.allergyButtonFlag  = true;
							 $scope.allergyShowFlag = true;
								$rootScope.stopLoader();
//								$scope.getAllergyList();
								$scope.getPatientAllergyDetails($scope.patientDetails.patientId);
//								$scope.patientAllergyObject="";
								return response.data.listObject;
						});
						}
				}
				
				
				$scope.showUpdateAllergryHistory = function(item)
				{
					 $scope.allergyPatientMapperId=item.allergyPatientMapperId;
					$scope.allergyButtonFlag = false;
					console.log(item);
					if(item.isNoKnownAllergies=='Y'){
						$scope.patientAllergyObject = {
								isNoKnownAllergies:item.isNoKnownAllergies,
								allergyPatientMapperId : item.allergyPatientMapperId
						}
					} else if (item.allergyId > 0){
							$scope.patientAllergyObject = {
									isNoKnownAllergies:item.isNoKnownAllergies,
						    	 	allergyTypeId:item.allergyTypeId.toString(),
					    	 	    allergyId:item.allergyId.toString(),
						    	 	month:item.month.toString(),
						    	 	year:item.year,
						    	 	type:item.type,
						    	 	natureOfReaction:item.natureOfReaction,
						    	 	severityId:item.severityId.toString(),
						    	 	remark:item.remark,
						    	 	isEnterInErrorStatus:item.isEnterInErrorStatus,
									isReviewedFlag:item.isReviewedFlag,
									allergyPatientMapperId : item.allergyPatientMapperId
							};
							$scope.itemRoleId = item.roleId;
							
							$scope.getAllergenListByAllergyId(item.allergyTypeId);
						} else {
                             $scope.patientAllergyObject = {
									isNoKnownAllergies:item.isNoKnownAllergies,
						    	 	allergyTypeId:item.allergyTypeId.toString(),
							    	 	genericId:item.genericId.toString(),
						    	 	month:item.month.toString(),
						    	 	year:item.year,
						    	 	type:item.type,
						    	 	natureOfReaction:item.natureOfReaction,
						    	 	severityId:item.severityId.toString(),
						    	 	remark:item.remark,
						    	 	isEnterInErrorStatus:item.isEnterInErrorStatus,
									isReviewedFlag:item.isReviewedFlag,
									allergyPatientMapperId : item.allergyPatientMapperId,
									
							};
                             $scope.genericName = item.genericName;
                             $scope.allergyShowFlag = false;
							$scope.itemRoleId = item.roleId;
							$scope.getAllergenListByAllergyId(item.allergyTypeId); 
					}
					
				}
				
				
				$scope.updateAllergyHistory = function()
				{
					
					$rootScope.startLoader();
					$scope.saveAllergyList = [];
					if ($scope.patientAllergyObject.isNoKnownAllergies != 'Y') { 
						
						if($scope.patientAllergyObject.allergyTypeId ==""){
							
							growl.error("Please Select Allergy Type ", {
								title : 'Error!'
							});
							$rootScope.stopLoader();
							return false;
						} else if($scope.patientAllergyObject.allergyId ==""){
						
							growl.error("Please Select Allergen", {
								title : 'Error!'
							});
							$rootScope.stopLoader();
							return false;
						}else if($scope.patientAllergyObject.month == ""){
							
							growl.error("Please Select Month", {
								title : 'Error!'
							});
							$rootScope.stopLoader();
							return false;
						}else if($scope.patientAllergyObject.year ==""){
							
							growl.error("Please Select Year", {
								title : 'Error!'
							});
							$rootScope.stopLoader();
							return false;
						}else if($scope.patientAllergyObject.type == ""){
							
							growl.error("Please Select Type", {
								title : 'Error!'
							});
							$rootScope.stopLoader();
							return false;
						}else if($scope.patientAllergyObject.natureOfReaction ==""){
							
							growl.error("Please Select Nature of Reaction ", {
								title : 'Error!'
							});
							$rootScope.stopLoader();
							return false;
						}else if($scope.patientAllergyObject.severityId ==""){
							
							growl.error("Please Select Severity", {
								title : 'Error!'
							});
							$rootScope.stopLoader();
							return false;
						}
					}
					
					if($scope.allergyShowFlag == true){
						$scope.patientAllergyObject.genericId="";
					}else{
						$scope.patientAllergyObject.allergyId="";
					}
					
					
					$scope.saveAllergyList.push($scope.patientAllergyObject);
							var data = {
									
						   allergyPatientMapperId:$scope.allergyPatientMapperId,
									//allergyPatientMapperId		
							createdDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
							updatedDate:moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
							updatedBy:$scope.userId,
							createdBy:$scope.userId,
							status:'A',
							roleId : 2,
							patientId : $scope.patientDetails.patientId,
							admissionId : $scope.patientDetails.admissionId,
							comments:'',
							listOfAllergyPatientMapperDto:$scope.saveAllergyList
							
					};
					
					console.log(data);
					// return false;
					var paramObj = {};
					var headers={
							'organization_id': $scope.organizationId,
							'unit_id':$scope.unitId,
							'Content-Type':'application/json'
					};
					// return false;
					var URI = BASE_URL + ROOT_URL
					+ UPDATEALLERGYPATIENTMAPPER;
			
					GenericServiceParamHeader
					.serviceAction(METHOD_POST, URI,
							data,
							NOTIFICATION_MSG_STATUS_TRUE,paramObj,headers)
					.then(
							function(response) {
								console.log(response);
								// $rootScope.startLoader();
								if (response.data.status == "success"){
									$rootScope.stopLoader();
									 $scope.genericName="";
									 $scope.patientAllergyObject.genericId="";
										$scope.patientAllergyObject.allergyTypeId="";
										$scope.patientAllergyObject.allergyId="";
										$scope.patientAllergyObject.month="";
										$scope.patientAllergyObject.year="";
										$scope.patientAllergyObject.type="";
										$scope.patientAllergyObject.natureOfReaction="";
										$scope.patientAllergyObject.severityId="";
										$scope.patientAllergyObject.remark="";
										$scope.patientAllergyObject.isNoKnownAllergies="N";
										$scope.getPatientAllergyDetails($scope.patientDetails.patientId);
										$scope.allergyCurrentListDoctor=[];
										$scope.genericName="";
										$scope.patientAllergyObject.genericId="";
										$scope.patientAllergyObject.allergyTypeId="";
										$scope.patientAllergyObject.allergyId="";
										$scope.patientAllergyObject.month="";
										$scope.patientAllergyObject.year="";
										$scope.patientAllergyObject.type="";
										$scope.patientAllergyObject.natureOfReaction="";
										$scope.patientAllergyObject.severityId="";
										$scope.patientAllergyObject.remark="";
										$scope.patientAllergyObject.isNoKnownAllergies="N";
										$scope.genericName = "";
										$scope.genericId = "";
										 $scope.allergyButtonFlag  = true;
										 $scope.allergyShowFlag = true;
									return response.data.listObject;
									
								}
									

							});
				}
				

				
				
				 /**********************Allergy resolved start *****************/
                $scope.selectedResolvedAllergyDate = function(info){
                	$scope.allergyPatientMapperId= info.allergyPatientMapperId;
                }
                
                
            	$scope.saveResolvedAllergyDate=function(ResolvedDateAllergy, resolvedAllergyRemark){
					$rootScope.startLoader();
					try{
						var data={
								    allergyPatientMapperId:$scope.allergyPatientMapperId,
									resolvedDate:moment(ResolvedDateAllergy).format('DD-MM-YYYY HH:mm:ss'),
									resolvedRemark:resolvedAllergyRemark,
								
						};
						
						if((ResolvedDateAllergy != null && ResolvedDateAllergy !="" && ResolvedDateAllergy != undefined)&&
								(resolvedAllergyRemark != null && resolvedAllergyRemark !="" && resolvedAllergyRemark != undefined)){
						var URI = BASE_URL + ROOT_URL  + "/api/opd/saveResolvedAllergyDate";
						GenericService.serviceAction("POST",URI,data).then(function(response){
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								$scope.ResolvedDateAllergy="";
								$scope.resolvedAllergyRemark="";
								$scope.getPatientAllergyDetails($scope.patientDetails.patientId);
								  
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.warning(response.data.message,
										{
											title : 'Warning!'
										});
							}
						});
					}else{
						growl.warning("Please enter date and remark.",
							{
								title : 'warning!'
							});
						$rootScope.stopLoader();
				}
					}catch(e){
						console.log("Exception",e);
					}
				}
            	
            	
                /**********************Allergy resolved end*****************/
            	
            	
						} ]);