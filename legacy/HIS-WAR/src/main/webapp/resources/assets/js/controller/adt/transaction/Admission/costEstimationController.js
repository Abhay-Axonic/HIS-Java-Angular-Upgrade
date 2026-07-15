'use strict';

/**
 * @Author By  Kaustubh
 * @name myApp.controller:costEstimationController
 */
angular
		.module('myApp')
		.controller(
				'costEstimationController',
				[
						'$scope',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'$state',
						'$timeout',
						'$interval',
						'GenericServiceParamHeader',
						'GenericServiceFileUpload',
						'growl',
						'Pubnub',
						'$stateParams',
						'reportService',
						'servicesFactory',
						function($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,GenericServiceParamHeader,GenericServiceFileUpload,growl,Pubnub,$stateParams,reportService,servicesFactory) {
						
							
							
						
						
							
							$scope.init = function()
							{
								
								// for datePicker
								$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
								};
								
								//Patient Search DOB Start
								$scope.openDatePickerSearch = function() {
									$scope.datepicker2.opened = true;
								};
								
								$scope.datepicker1 = {
										opened : false
									};
								//Patient Search DOB End
								
								
								
								$scope.datepicker2 = {
										opened : false
									};
								
								$scope.openDatePickerPdd = function() {
									$scope.datepickerPdd.opened = true;
								};

								$scope.datepickerPdd = {
									opened : false
								};
								
								
								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker = {
									opened : false
								};
								
								
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.enabaleBackButton = false;
								$scope.addServiceFlag = false;
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								if(cookieObjectAppConfig == undefined){
									$state.go('applicationConfiguration');
									return;
								}
								else
									{
										console.log('cookieObjectAppConfig',cookieObjectAppConfig);
										
									}
								
								
								$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.tariffId = $scope.defaultSelfTariffId;
								$scope.optionTariffId = $scope.defaultSelfTariffId;
								$scope.pharmacyServiceId = cookieObjectAppConfig.pharmacyServiceId;
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								$scope.opdBillingCatId = cookieObjectAppConfig.opdBillingCatId;
								$rootScope.loginpage = true;
								$scope.remark = ""; 
								$scope.datepicker = new Date();
								
								 var new_date = moment($scope.datepicker).add(5, 'days');
								 $scope.datepickerPdd = new Date(new_date);
								 
								
								$scope.dateOptionsCost = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date()
									};
								
								$scope.dateOptions = {
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate : new Date()
									};
								
								$scope.dateOptionsPaymentEntitlement = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date()
									};
								$scope.dateOptionsPaymentEntitlementStart = {
										formatYear : 'yyyy',
										showWeeks : false,
										maxDate : new Date()
									};
								
								$scope.openDatePickerContractStartDate = function(index) {

									for (var i = $scope.paymentEntitleMentCorporate.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.datepickerCons.opened[i] = true;
										} else {
											$scope.datepickerCons.opened[i] = false;
										}
									}
								};

								$scope.datepickerCons = {
									opened : [ false ],

								};
								
								$scope.openDatePickerContractEndDate = function(index)
								{

									for (var i = $scope.paymentEntitleMentCorporate.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.datepickerConE.opened[i] = true;
										} else {
											$scope.datepickerConE.opened[i] = false;
										}
									}
								};

								$scope.datepickerConE = {
									opened : [ false ],

								};
								
								$scope.openDatePickerInsuranceStartDate = function(index) {

									for (var i = $scope.paymentEntitleMentInsurance.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.datepickerInss.opened[i] = true;
										} else {
											$scope.datepickerInss.opened[i] = false;
										}
									}
								};

								$scope.datepickerInss = {
									opened : [ false ],

								};
								

								$scope.openDatePickerInsuranceEndDate = function(index) {

									for (var i = $scope.paymentEntitleMentInsurance.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.datepickerInsE.opened[i] = true;
										} else {
											$scope.datepickerInsE.opened[i] = false;
										}
									}
								};

								$scope.datepickerInsE = {
									opened : [ false ],

								};
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEPATIENTCATEGORYBYORGID+"/"+$scope.organizationId;
								GenericService.serviceAction("GET", URI, {})
										.then(
												function(response) {
													$scope.patientTypeList = [];
													if (response.data.status == "success") 
													{
														$scope.patientTypeList=response.data.listObject;
														console.log('patientTypeList',$scope.patientTypeList);
													}
												});
						
								var data = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
								// For Get bed Category List 
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEBEDCATEGORYLISTBYUNIT;
						
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.bedCategoryList = [];
				
													if (response.data.status == "success") {
														$scope.bedCategoryList = response.data.listObject;	
														console.log("bedCategoryList",$scope.bedCategoryList);
													}
												});
						
								
								$scope.packageSearchObj = {packageTypeId : "0", serviceCode:"",serviceName:""}
								$scope.getPackageTypeList = function() {
									var URI = BASE_URL + ROOT_URL + "/Unit/Service/EHCpackage/getPackgeType/"+$scope.organizationId+"/"+$scope.unitId;
									BillingGenericService.serviceAction("GET", URI, null).then(function(response){
										$scope.packageTypeList = response.data.listObject;
									});
								}
								$scope.getPackageTypeList();
								// for getting active visit type

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEVISITTYPEMASTER+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.visitTypeList = [];

													if (response.data.status == "success") {
														angular.forEach(response.data.listObject,
																		function(value,index) {
																			var data = {};
																			data.id = value.visitTypeId;
																			data.label = value.visitTypeName;
																			$scope.visitTypeList.push(data);
																		});
														console.log('visitTypeList',$scope.visitTypeList);
													}
												});
								
								$scope.paymentEntitleMentCorporate=
									[{
										'companyId':"1",
										'contractId':"",
										//'statrDate':"",
										//'endDate':""
									}];
								
								$scope.paymentEntitleMentInsurance=
									[{
										'companyId':"1",
										'associateCompanyId':"",
										'contractId':"",
										//'statrDate':"",
										//'endDate':"",
										'insuranceNumber':"",
										'insuredAmt':""
									}];
								
								//for paymententitlement
								$scope.paymentEntitleMentList=
									[{
										'paymentEntitlementName':"Self",
										'paymentEntitlementId':"1",
										'companyName':"Self",
										'tariffName':"Self Tariff",
										'contractName':"",
										'insuranceName':"",
										'insuranceNumber':"",
										'startDate':"",
										'endDate':"",
										'priority':"1",
										'companyId':'1',
										'tariffId':$scope.defaultSelfTariffId,
										'contractId':null,
										'typeId' : 1,
										'insuredAmount' : '',
										'billingBedCategoryId':null
									}];
								
								$scope.appointmentMasterObj = {
										paymentEntitlementTypeId : 1
								};
								
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEPAYMENTENTITLEMENT;
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.paymentEntitlementList = [];

											if (response.data.status == "success") {
												$scope.paymentEntitlementList = response.data.listObject;
											}
										});
						
						
						$scope.paymentEntitleMentCorporate=
							[{
								'companyId':"",
								'contractId':"",
								//'statrDate':"",
								//'endDate':"",
							}];
						
						$scope.paymentEntitleMentInsurance=
							[{
								'companyId':"",
								'associateCompanyId':"",
								'contractId':"",
								//'statrDate':"",
								//'endDate':"",
								'insuranceNumber':"",
								'insuredAmt':""
							}];
						//End Paymententitlement
						

						$scope.patientSearchObj = {
								"uhIdNumber"      : "",
								"patientName"     : "",
								"genderId"        : "",
								"mobileNo"        : "",
								"birthDate"       : "",
								"identificationNo":"",
								"patientType"	  :"1",
								"unitId"          :$scope.unitId,
								"organizationId"  :$scope.organizationId
						}
						
	                    $scope.priorityList = [{
	                        id:1,
	                        value:1
	                    }]

						
					}
							$scope.init();
							$scope.visitTypeId = 1;
							$scope.optionVisitTypeId = 1;
							$scope.getTariffList = function()
						    {
								var obj = {
										"organizationId":$scope.organizationId ,
										"unitId":$scope.unitId 
									};
							var URI = BASE_URL + ROOT_URL
							+ GETTARIFFMASTERLISTOPDBILLING;
							BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								if(response.data.status=="success")
								{
									$scope.tariffList=response.data.listObject;
								}
							});
						    }
							$scope.getTariffList();
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
								
								if(patientsearchObj.uhIdNumber == '' && patientsearchObj.patientName == '' && patientsearchObj.genderId == '' && patientsearchObj.mobileNo == '' && $('#datepickerSearch').val() == '' && patientsearchObj.identificationNo == '')
								{
									growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
									});
									return false;
								}
									
								else{
										//alert("else AlERT");
									}
								if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
								{
									growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
									$scope.patientList = [];
									return false;
									
								}	
								//patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
								
								if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
									patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
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
								var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
								BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									
									if(response.data.listObject==null){
										growl.warning(response.data.message,{
											title : ERROR_MSG
										});
										$scope.patientSearchObj.birthDate="";
										return false;
									}
									if(Object.keys(response.data.listObject).length  == 0){
										growl.warning("No Result Found!",{
											title : ERROR_MSG
										});
									}
									else{
									angular.forEach(response.data.listObject, function (value, key) {
										 
										if(value.registrationTypeId != 5 && value.registrationTypeId !=2)
											{
											 $scope.patientList.push(value);
											}
										  
									  });
									$scope.patientSearchObj.birthDate="";
									}
									
								});
								
							}
							
                              $scope.selectedPatient = {};
  							$scope.getPatientDetailsByIndex = function(index)
  							{
  								$scope.abscondPatientFlag="";
  								$(".patienttSearchResult").toggle();
  								$scope.optionVisitTypeId = 1;
  								$scope.changeVisitType();
  								$scope.visitTypeId=1;
                                $scope.selectedPatient.patientFullName = ($scope.patientList[index].patientName); 
                                $scope.selectedPatient.age=($scope.patientList[index].age);
                                $scope.selectedPatient.uhidnumber=($scope.patientList[index].uhIdNumber);
                                $scope.selectedPatient.genderId = ($scope.patientList[index].genderId);
                                $scope.selectedPatient.patientTypeId  = ($scope.patientList[index].patientType);
                                $scope.selectedPatient.patientId= ($scope.patientList[index].patientId);
                                $scope.selectedPatient.mobileNo= ($scope.patientList[index].mobileNo !== undefined && $scope.patientList[index].mobileNo !== null ? $scope.patientList[index].mobileNo : $scope.patientList[index].mobileNumber);
                                $scope.patientTypeId = ($scope.patientList[index].patientType);
								$scope.genderId =($scope.patientList[index].genderId);
								$scope.patientTypeId = ($scope.patientList[index].patientCategoryId);
								$scope.selectedServiceDetails = [];
								$scope.totalAmountMain = parseFloat(0);
					    		$scope.selfAmountMain = parseFloat(0);
					    		$scope.creditAmountMain = parseFloat(0);
									
  							}
							$scope.getCompanyMasterList = function(){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + COMPANY_MASTER + UNIT + SLASH + $scope.unitId + ORG + SLASH + $scope.organizationId;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.companyMasterList = [];
										$scope.associatedCompanyList = [];
										if (response.data.status == "success") {
											$scope.companyMasterList = response.data.listObject;
										}
									});
							}
							$scope.getCompanyMasterList();
							
							$scope.getContractDetails = function(item,idx,contId)
							{
								//alert(idx);
								var contractObj = null;
								contractObj = $.grep($scope.contractList, function (doc) {
						            return doc.contractId == contId;
						        })[0];
								console.log("contractObj",contractObj);
								if(contractObj != undefined || contractObj != null)
									{
									 //alert("Some Values");
									  $scope.paymentEntitleMentCorporate[0].datepickerCons =contractObj.validityFrom;
									  $scope.paymentEntitleMentCorporate[0].datepickerConE = contractObj.validityTo;
									
									  
									  
									}
								
								
							}
							
							$scope.isTempPatientReg= {
								
									prefixList: [{
								          "prefix": "Select"
								        }],
								        patientGenderList: [{
								            "gender": "Select"
								          }],
								        "prefix":"",
								        "isPatientNameValid":"",
								        "mobileNo":"",
								        "patientName":"",
								        "patientAddress":"",
								        "toggleButton":false,
								        "patientGender":"",
								        "emailId":"",
								
							};
							
							$scope.isTempPatientReg.isemailIdValid =false;
							//$scope.isTempPatientReg.mobileNumber="56765456";
							
							//$scope.isTempPatientReg.isPatientMobileNumberValid=true;
							
							$scope.getToggleValues = function (bill, event) {
					      if($scope.isTempPatientReg.toggleButton == true){
					        $('#hidePatientSearch').hide();
					      }
					      else{      
				       $('#hidePatientSearch').show();
//					        $scope.searchPatient = true;				       
				       $scope.isTempPatientReg.mobileNumber="";
					      $scope.isTempPatientReg.patientAddress="";
							 $scope.isTempPatientReg.patientName="";
							 $scope.isTempPatientReg.patientGender="";
							 $scope.isTempPatientReg.emailId="";
							 $scope.isTempPatientReg.prefix="";
				      }
					      
//					      $scope.isTempPatientReg.mobileNumber="";
//					      $scope.isTempPatientReg.patientAddress="";
					      $scope.isTempPatientReg.isPatientGenderNewValid=false;
					    
							}	
							
							$scope.getInsuranceContract = function(insuranceItem)
							{
								
								var companyTypeId = $scope.appointmentMasterObj.paymentEntitlementTypeId;
								var companyId = insuranceItem.companyId;
									var asscoiatedCompanyId = insuranceItem.associateCompanyId;
										
									
									var param = {
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											companyId:companyId,
											companyTypeId:companyTypeId,
											associateCompanyId:asscoiatedCompanyId,
											visitTypeId:$scope.visitTypeId
										};
									
									$scope.assoContractList = [];
									$scope.paymentEntitleMentInsurance[0].contractId = '';
									var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
								    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success"){
											 $scope.assoContractList = response.data.listObject;
											 //getting insurance document name start
											 if(asscoiatedCompanyId!=undefined && asscoiatedCompanyId!=null && asscoiatedCompanyId != ""){
												 var URI = BASE_URL + ROOT_URL+ "/api/unit/billing/getInsuranceMasterById/"+asscoiatedCompanyId+"/"+ $scope.organizationId+"/"+$scope.unitId;
													GenericService.serviceAction("GET", URI, "").then(function(response) {
																		if (response.data.status == "success") {
																			$scope.insuranceObjDoc=response.data.listObject[0];													
																		} else {
																			alert("Error!!");
																		}
																	});
											 }
											//getting insurance document name End
										}
									});
							}
							
							$scope.getContractByCompanyId = function(companyId)
							{
								
								var companyTypeId = $scope.appointmentMasterObj.paymentEntitlementTypeId;
								
								var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,visitTypeId:2};
								 $scope.contractList = [];
								 $scope.paymentEntitleMentCorporate[0].contractId = '';
								  $scope.paymentEntitleMentCorporate[0].datepickerCons ='';
								  $scope.paymentEntitleMentCorporate[0].datepickerConE ='';
								
								var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
							    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										 $scope.contractList = response.data.listObject;
										 //getting insurance document name start
										 if(companyId!=null && companyId!="" && companyId!=undefined){
												var URI = BASE_URL + ROOT_URL+"/api/global/getCompanyMasterListById/unit/"+$scope.unitId+"/org/"+$scope.organizationId+"/company/"+companyId;
												GenericService.serviceAction("GET", URI, "")
														.then(function(response) {
																	if (response.data.status == "success") {
																		$scope.CompanyObjDoc=response.data.listObject[0];											
																	} else {
																		alert("Error!!");
																	}
																});
											 }
										//getting insurance document name End
									}
								});
							}
							
							$scope.getAssociatedCompanyMasterListByCompanyId = function(companyId){
								 var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId};
								 $scope.associateCompanyMasterList = [];
								 $scope.paymentEntitleMentInsurance[0].associateCompanyId = '';
								 
								    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getAssociateCompanyMaster"
								    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success"){
											 $scope.associateCompanyMasterList = response.data.listObject;
											  console.log("associateCompanyMasterList",JSON.stringify($scope.associateCompanyMasterList));
										}
									});
							}
							
							
							$scope.getDetailsEntitlement = function(type)
							{
								/*if(type == '2')
								{
									
								}*/
								if(type == '2')
									{
									
									var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:type};
								    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
								    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success"){
											 $scope.companyMasterInsuranceList = response.data.listObject;
										}
									});
									}
								if(type == '3')
									{
									     var param = {organizationId:$scope.orgId,unitId:$scope.unitId,companyTypeId:type};
										    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getCompanyMasterList"
										    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												if (response.data.status == "success"){
													 $scope.companyMasterList = response.data.listObject;
													 console.log("companyMasterList",JSON.stringify($scope.companyMasterList));
												}
											});
										  
									}
							}
							
							
								$scope.checkDateDoa = function(){
								
								/*if($scope.datepicker === undefined);
								{
									growl.warning("Doa is invalid",{
										title : WARNING_MSG
									});
									$scope.datepicker = new Date();
									return false;
								}*/
									 if(!angular.isDate($scope.datepicker)){
										 growl.warning("Doa is invalid",{
												title : WARNING_MSG
											});
											$scope.datepicker = new Date();
											return false;
										}
									 }
							$scope.checkDatePdd = function(){
								
								/*if($scope.datepickerPdd == undefined);
									{
										growl.warning("Pdd is invalid",{
											title : WARNING_MSG
										});
										$scope.datepickerPdd = new Date();
										return false;
									}*/
								 if(!angular.isDate($scope.datepickerPdd)){
									 growl.warning("Pdd is invalid",{
											title : WARNING_MSG
										});
										$scope.datepickerPdd = new Date();
										return false;
								 }
							}
							
							
							 var isInsuranceExists;
								function checkInsuranceExistsOrNot(arr, val) {
									return arr
											.some(function(arr) {
												if(val.paymentEntitlementId == parseInt(arr.paymentEntitlementId) && val.contractId == parseInt(arr.contractId))
													{
														return true;
													}
												else
													{
														return false;
													}
											});
								}
								
								 var insuranceCount;
									function checkInsuranceCount(arr) {
										var countInsurance = 0;
										angular.forEach($scope.paymentEntitleMentList, function (value, key) {
											 
											  if(value.paymentEntitlementId == "2")
												  {
												   countInsurance++;
												  }
											  
											  
										  });
										return countInsurance;
									}
									
									
							$scope.applyInsuranceDetails = function()
							{
								
									 var isEmpty = !(Object.values($scope.paymentEntitleMentInsurance[0]).every(x =>(x != null &&  x != '')));
									 if(isEmpty)
										 {
										   growl.warning("Please select all fields.",{
												title : "WARNING"
											});
											return;
										 }
									 else{
										 
								insuranceCount=checkInsuranceCount($scope.paymentEntitleMentList)
								if (insuranceCount >= 2) {
									growl.warning("You can't add greater than two insurance.",{
										title : "WARNING"
									});
									return;
								}
								else
									{
										
								var insuranceObj = null;
								insuranceObj = $.grep($scope.assoContractList, function (doc) {
						            return doc.contractId == $scope.paymentEntitleMentInsurance[0].contractId;
						        })[0];
								
								var associatedName = $.grep($scope.associateCompanyMasterList, function (doc) 
									{
						            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
						        })[0].name;
								
								var insuranceId = $.grep($scope.associateCompanyMasterList, function (doc) 
										{
							            return doc.id == $scope.paymentEntitleMentInsurance[0].associateCompanyId;
							        })[0].id;
								
								console.log("$scope.paymentEntitleMentInsurance",$scope.paymentEntitleMentInsurance);
								
								if(insuranceObj != undefined || insuranceObj != null)
								{
									 $scope.getBedPopUpFlag = false;
								  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '2')
								  {
									  var sInsDt =  moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss');
									  var eInsDt =  moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss');
									  
									  var isAfter = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).isAfter($scope.paymentEntitleMentInsurance[0].datepickerInss);
									    
									  if(!isAfter)
										  {
											  growl.warning("Start date should be less then end date..!!",{
													title : "WARNING"
												});
												return false;
										  }else{
											  
                                              var prioObj = {
                                                      id:$scope.priorityList.length + 1,
                                                      value:$scope.priorityList.length + 1,
                                                  }
                                                  $scope.priorityList.push(prioObj);

								  var newArr = {};
								  newArr.paymentEntitlementName=insuranceObj.paymentEntitlementName;
								  newArr.paymentEntitlementId="2";
								  newArr.companyName = insuranceObj.assCompanyName;
								  newArr.companyId = insuranceObj.companyId;
								  newArr.contractName = insuranceObj.contractName;
								  newArr.insuranceName = associatedName;
								  newArr.insuranceId = insuranceId;
								  newArr.insuranceNumber = $scope.paymentEntitleMentInsurance[0].insuranceNumber;
								  newArr.startDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;
								  newArr.endDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;datepickerInsE
								  newArr.priority = "";
								  newArr.contractId = insuranceObj.contractId;
								  newArr.typeId = 2;
								  newArr.insuredAmount = $scope.paymentEntitleMentInsurance[0].insuredAmt;
								  newArr.billingBedCategoryId = insuranceObj.billingBedCategoryId;
								  if($scope.insuranceObjDoc.insuranceId!=undefined && $scope.insuranceObjDoc.insuranceId!=null  && $scope.paymentEntitleMentInsurance[0].associateCompanyId==$scope.insuranceObjDoc.insuranceId){
									  newArr.reportPath=$scope.insuranceObjDoc.reportPath;
								  }
								  isInsuranceExists=checkInsuranceExistsOrNot($scope.paymentEntitleMentList,newArr)
									if (!isInsuranceExists) {
										var lenIns = $scope.paymentEntitleMentList.length;
										
										if($scope.paymentEntitleMentList.length > 0)
										{
											angular.forEach($scope.paymentEntitleMentList, function (value, key) {
												 
												  if(value.paymentEntitlementId == "1")
													  {
													   value.priority = lenIns + 1;
													  }
												  if(lenIns == 1)
													  {
													  	newArr.priority = "1";
													  }
												  if(lenIns >= 2)
													  {
													  angular.forEach($scope.paymentEntitleMentList, function (value1, key1) {
														  if(value1.paymentEntitlementId != 1)
														  {
														  value1.priority =  "";
														  }
													  });
													  	
													  }
												  
												  
											  });
										}
										
										$scope.paymentEntitleMentList.push(newArr);
										$scope.paymentEntitleMentInsurance=
											[{
												'companyId':"",
												'associateCompanyId':"",
												'contractId':"",
												'insuranceNumber':"",
												'insuredAmt':""
											}];
									}
									else
										{
											growl.warning("Already added in the list",{
												title : "WARNING"
											});
											return;
										}
								  
								  }
								  
								  
								  }
								 
								  
										}
									}
							}
								
							}
							
							 var isContractAlreadyExists;
								function checkContractAlreadyExistsOrNot(arr, val) {
									  var found = arr.find(ar=>ar.paymentEntitlementId==val);
									  if(found == undefined)
										  {
										  	return false;
										  }
									  else
										  {
										  	return true;
										  }
									  
								}
							
							$scope.applyContractDetails = function(id)
							{
								//alert(idx);
								
								 var isEmpty = !(Object.values($scope.paymentEntitleMentCorporate[0]).every(x =>(x != null &&  x != '')));
								 if(isEmpty)
									 {
									   growl.warning("Please select all fields.",{
											title : "WARNING"
										});
										return;
									 }
								 else{
							
								isContractAlreadyExists=checkContractAlreadyExistsOrNot($scope.paymentEntitleMentList,3);
									  //var found = $scope.array.find(ar=>ar==char);
								if (isContractAlreadyExists) {
									growl.warning("Can't add two contract.",{
										title : "WARNING"
									});
									return;
								}
								else
									{
								var contractObj = null;
								contractObj = $.grep($scope.contractList, function (doc) {
						            return doc.contractId == $scope.paymentEntitleMentCorporate[0].contractId;
						        })[0];
								
						/*		var tarrifName = $.grep($scope.tariffList, function (doc) {
						            return doc.tariffId == $scope.paymentEntitleMentCorporate[0].tariffId;
						        })[0].tariffDesc;*/
								
								
								console.log("contractObj",contractObj);
								if(contractObj != undefined || contractObj != null)
								{
									 $scope.getBedPopUpFlag = false;
								  if($scope.appointmentMasterObj.paymentEntitlementTypeId == '3')
								  {
									  
                                      var prioObj = {
                                              id:$scope.priorityList.length + 1,
                                              value:$scope.priorityList.length + 1,
                                          }
                                          $scope.priorityList.push(prioObj);

								  var newArr = {};
								  newArr.paymentEntitlementName=contractObj.paymentEntitlementName;
								  newArr.paymentEntitlementId="3";
								  newArr.companyName = contractObj.assCompanyName;
								  newArr.companyId = contractObj.companyId;
								  newArr.contractName = contractObj.contractName;
								  newArr.insuranceName = "";
								  newArr.insuranceNumber = "";
								  newArr.startDate = $rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerCons)+" 00:00:00";
								  newArr.endDate =$rootScope.getChangedFormatedDateShalse($scope.paymentEntitleMentCorporate[0].datepickerConE)+" 00:00:00";
								  newArr.priority = "";
								  newArr.contractId = contractObj.contractId;
								  newArr.insuredAmount = '';
								  newArr.typeId = 3;
								  newArr.billingBedCategoryId = contractObj.billingBedCategoryId;
								  
								  if($scope.CompanyObjDoc.companyId!=undefined && $scope.CompanyObjDoc.companyId!=null && $scope.CompanyObjDoc.companyId!="" && $scope.paymentEntitleMentCorporate[0].companyId==$scope.CompanyObjDoc.companyId){
									  newArr.reportPath=$scope.CompanyObjDoc.reportPath;
								  }

								  isInsuranceExists=checkInsuranceExistsOrNot($scope.paymentEntitleMentList,newArr)
									if (!isInsuranceExists) {
										var lenCon = $scope.paymentEntitleMentList.length;
										if($scope.paymentEntitleMentList.length > 0)
										{
											angular.forEach($scope.paymentEntitleMentList, function (value, key) {
												 
												  if(value.paymentEntitlementId == "1")
													  {
													   value.priority = lenCon + 1;
													  }
												  
												  if(lenCon == 1)
												  {
												  	newArr.priority = "1";
												  }
											  if(lenCon >= 2)
												  {
												  angular.forEach($scope.paymentEntitleMentList, function (value1, key1) {
													  if(value1.paymentEntitlementId != 1)
													  {
													  value1.priority =  "";
													  }
												  });
												  	
												  }
												  
												  
											  });
										}
										
										$scope.paymentEntitleMentList.push(newArr);
										$scope.paymentEntitleMentCorporate=
											[{
												'companyId':"",
												'contractId':"",
											}];
									}
									else
										{
											growl.warning("Already added in the list",{
												title : "WARNING"
											});
											return;
										}
								  
								  
								  
								  
								  }
								
								  
										}
									}
								 }		
								
								
								
							}
							
							$scope.addContracts = function()
							{
								var data = {
										'companyId':"",
										'contractId':"",
										//'statrDate':"",
										//'endDate':""
									};
								
								$scope.paymentEntitleMentCorporate.push(data);
								
							}
							
						    $scope.activeTab = 1;

						    $scope.showTabContent = function(tabNumber) {
						      $scope.activeTab = tabNumber;
						      
						      if($scope.activeTab ==1){
						    	  $('#hidePatientSearch').show();
						    	  $scope.selectedServiceDetails = [];
						    	  $scope.totalAmountMain = 0;
						    	  $scope.selfAmountMain = 0;
						    	  $scope.creditAmountMain = 0;

						      }
						      
						      if($scope.isTempPatientReg.toggleButton == true && $scope.activeTab ==2){
						    	  $('#hidePatientSearch').hide();
							      }
							      else{      
						       $('#hidePatientSearch').show();
//							        $scope.searchPatient = true;				       
						       $scope.isTempPatientReg.mobileNumber="";
						       $scope.servicesListPopupOption  = [];
						       $scope.selectedServiceDetailsOption = [];
						       $scope.billingBedCategoryCalculationList[0].totalAmount =0;
						       $scope.billingBedCategoryCalculationList[0].netAmount =0;
						      
							      $scope.isTempPatientReg.patientAddress="";
									 $scope.isTempPatientReg.patientName="";
									 $scope.isTempPatientReg.patientGender="";
									 $scope.isTempPatientReg.emailId="";
									 $scope.isTempPatientReg.prefix="";
						      }
						      
						    };

							$scope.removeContract = function(index)
							{
								console.log("removeIndex",index);
								var newDataList = [];
								var le = $scope.paymentEntitleMentList.length;
								var i = 1;
								if(le != i){
								angular.forEach($scope.paymentEntitleMentList, function(v) {
									if (index != i) {
										/*if(v.priority > 1){
										v.priority = v.priority - 1;
										}*/
										newDataList.push(v);
										
										if(le == 2)
										{
											if(v.paymentEntitlementId == "1")
												{
													v.priority = le - 1;
												}else
													{
													 v.priority = 1;
													}
										}
									if(le > 2)
									{
										
										if(v.priority != 1)
										{
										 //v.priority = "";
										 v.priority = v.priority - 1;
										}
										
										/*if(v.paymentEntitlementId == "1")
											{
												v.priority = le - 1;
											}else
												{
												v.priority = "";
												}*/
									}
									
									}
									i++;
								});
								
                                
                                var len = $scope.priorityList.length;
                                 $scope.priorityList.splice(len-1,1);

								$scope.paymentEntitleMentList = newDataList;
								//$scope.paymentEntitleMentCorporate =[];
								//$scope.addContracts();
								}
							}
							
							
							
							$scope.checkDuplicatePriority = function(item,idx,priorityVal)
							{
								
								var newDataList = [];
								var le = $scope.paymentEntitleMentList.length;
								var i = 0;
								angular.forEach($scope.paymentEntitleMentList, function(v) {
									if (idx != i) {
										/*if(v.priority > 1){
										v.priority = v.priority - 1;
										}*/
										newDataList.push(v);
									}
									i++;
								});
								
								//$scope.paymentEntitleMentList = newDataList;
								
								var found = newDataList.find(ar=>ar.priority==priorityVal);
								  if(found == undefined)
									  {
									  	//return false;
									  }
								  else
									  {
									  $scope.paymentEntitleMentList[idx].priority = 0;
									  growl.warning("You are aleady assign "+priorityVal+" priority",{
											title : "WARNING"
										});
										return;
									  }
							}
							
							
							$scope.removeNotSelectedService = function(index,item){
								
								$scope.selectedServiceDetails.splice(index,1);
								$scope.l1=[];
								$scope.l1.push(item);
								angular.forEach($scope.l1, function (value, index2) {
										$scope.totalAmountMain = parseFloat($scope.totalAmountMain) - parseFloat(value.totalAmt);
						    			$scope.selfAmountMain = parseFloat($scope.selfAmountMain) - parseFloat(value.selfPayable);
						    			$scope.creditAmountMain = parseFloat($scope.creditAmountMain) - parseFloat(value.creditPayable);
					    		});
								
							}
							   
						    $scope.servicesListPopup = [];
						    $scope.servicesListPopupOption = [];
						    $scope.fromOptionCheck = false;
							$scope.getservicesPop = function()
						    {
								if($scope.patientTypeId == null || $scope.patientTypeId == '' ||  isNaN($scope.patientTypeId))
							    {
								    	 growl.warning("Please select patient category..!!",{	
												title : "WARNING"
											});
											return false;
							    }
							    if($scope.visitTypeId!=1 && ($scope.bedCategoryId == null || $scope.bedCategoryId == '' ||  isNaN($scope.bedCategoryId)))
						    	{
							    	 growl.warning("Please select bed category..!!",{	
											title : "WARNING"
										});
										return false;
						    	}
							    angular.element('#addServicePopup').modal('show');
						    	$scope.servicesListPopup = [];
								$scope.selectedPackageList = [];
						    	console.log("ff",$scope.paymentEntitleMentList);
						        $scope.fromOptionCheck = false;  
						    }
							
							
							$scope.getservicesPopOption = function()
						    {
								if($scope.patientTypeId == null || $scope.patientTypeId == '' ||  isNaN($scope.patientTypeId))
							    {
								    	 growl.warning("Please select patient category..!!",{	
												title : "WARNING"
											});
											return false;
							    }
							   
							    angular.element('#addServicePopup').modal('show');
						    	$scope.servicesListPopup = [];
						    	$scope.fromOptionCheck = true;  
						    }
							
							
							
							 $scope.search="";
								$scope.searchServices = function(searchKeyword){
									$scope.servicesList=[];
									if($scope.genderId == undefined || $scope.genderId == ""){
										$scope.genderId =$scope.isTempPatientReg.genderId;
									}
									searchKeyword=searchKeyword+"---"+$scope.genderId;
									var serData = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											searchKeyword : searchKeyword
									}
									var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
									//var URI = BASE_URL + ROOT_URL + "/global/serviceMaster/"+searchKeyword+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
									return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if(response.data.status == "success"){
											$scope.servicesList = response.data.listObject;
										}
										return $scope.servicesList;
									});
								}
								  $scope.getPayeeCreditDetails = function(itemList)
								    {
								    	$scope.payeeCreditDetailslList = [];
								    	$scope.payeeCreditDetailslList = itemList;
								    }
								
								//get cancel services by admission id end
								$scope.optionalServiceList = [];
								$scope.forServicePayeeId  = $scope.selfPayeeId;
								$scope.servicesListPopupTemp = [];
							    $scope.addServiceToTempServiceList = function($item, $model, $label){
							    	$scope.forServicePayeeId  = $scope.selfPayeeId;
									console.log($item);
									
									$scope.search = '';
									$('#searchServiceId').val('');
									
									
									if($scope.fromOptionCheck == true){
										
										var checkExits = false;
										angular.forEach($scope.servicesListPopupOption, function (value, key) {
							    			if(value.serviceMasterId == $item.serviceMasterId){
							    				checkExits = true;
							    			}
							    		});
										
										angular.forEach($scope.servicesListPopupTemp, function (value, key) {
							    			if(value.serviceMasterId == $item.serviceMasterId){
							    				checkExits = true;
							    			}
							    		});
										
										if(checkExits == true){
											growl.warning("This service already added ",{
												title : ERROR_MSG
											});
											return false;
										}
										
										var list = [];
										angular.forEach($scope.billingBedCategoryOptionFiltterList, function (value, index) {
											var obj = {
													"patientId" : $scope.selectedPatient.patientId,
													"contractId" :$scope.contractIdForRate,
													"serviceId" :$item.serviceMasterId,
													"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
													"organizationId":$scope.organizationId,
													"unitId":$scope.unitId,
													"visitTypeId":$scope.optionVisitTypeId!=null ? $scope.optionVisitTypeId : 2,
													"billingBedCategoryId":$scope.optionVisitTypeId==1 ? $scope.opdBillingCatId : value.billingBedCategoryId,
													"patientCategoryId":$scope.patientTypeId,
													"defaultSelfTariffId":$scope.optionTariffId !=undefined && $scope.optionTariffId != null ? $scope.optionTariffId : $scope.defaultSelfTariffId ,
												    "selfPayeeId" :$scope.selfPayeeId,
												    "paymentEntitlementId":1
												};
											list.push(obj);
										});
										
										
										
										
										console.log('Base prise',obj)
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/global/consumption/costEstimation/service/billingbed";
										BillingGenericService.serviceAction(METHOD_POST,URI, list, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											if(response.data.status=="success"){
												if(response.data.object != "undefined" && response.data.object != null)
													{
													$scope.forServicePayeeId = response.data.object.payeeId;
													$item.basePrice=response.data.object.serviceRate;
													$item.selfPayable = (response.data.object.selfPayable).toFixed(2);
													$item.creditPayable = (response.data.object.creaditPayable).toFixed(2);
													$item.selfPayableOld = (response.data.object.selfPayable).toFixed(2);
													$item.creditPayableOld = (response.data.object.creaditPayable).toFixed(2);
													$item.contractId = response.data.object.contractId;
													$item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
													$item.tariffId=$scope.optionTariffId !=undefined && $scope.optionTariffId != null ? $scope.optionTariffId : $scope.defaultSelfTariffId
													$item.finalPrice = (response.data.object.finalPrice).toFixed(2);
													$item.finalPriceOld = (response.data.object.finalPrice).toFixed(2);
													$item.billingBedCategoryId = response.data.object.billingBedCategoryId;
													$item.quantity = 1;
													$scope.servicesListPopup.push($item);
													$scope.servicesListPopupTemp.push($item);
													}
												
													angular.forEach(response.data.listObject, function (val, inx) {
													
														var obj = {};
														obj.basePrice = $item.basePrice;
														obj.basePriceForContract= $item.basePriceForContract;
														obj.billingBedCategoryId= $item.billingBedCategoryId;
														obj.concession = $item.concession;
														obj.creditPayable = $item.creditPayable;
														obj.creditPayableOld = $item.creditPayableOld;
														obj.finalPrice = $item.finalPrice;
														obj.finalPriceOld = $item.finalPriceOld;
														obj.isAppointable = $item.isAppointable;
														obj.isDiscountApplicable= $item.isDiscountApplicable;
														obj.isDoctorRequired= $item.isDoctorRequired;
														obj.isQuantityEditable= $item.isQuantityEditable;
														obj.isRateEditable= $item.isRateEditable;
														obj.isRefDoctorShare= $item.isRefDoctorShare;
														obj.isRenderingSpeciality= $item.isRenderingSpeciality;
														obj.isTaxApplicable= $item.isTaxApplicable;
														obj.quantity= $item.quantity;
														obj.selfPayable= $item.selfPayable;
														obj.selfPayableOld= $item.selfPayableOld;
														obj.serviceMasterId= $item.serviceMasterId;
														obj.serviceStandardCode= $item.serviceStandardCode;
														obj.serviceStandardName= $item.serviceStandardName;
														obj.specialityId= $item.specialityId;
														obj.specialityName= $item.specialityName;
														obj.subSpecialityId= $item.subSpecialityId;
														obj.subSpecialityName= $item.subSpecialityName;
														obj.basePrice=val.serviceRate;
														obj.quantity=$item.quantity;
														obj.selfPayable = (val.selfPayable).toFixed(2);
														obj.creditPayable = (val.creaditPayable).toFixed(2);
														obj.selfPayableOld = (val.selfPayable).toFixed(2);
														obj.creditPayableOld = (val.creaditPayable).toFixed(2);
														obj.contractId = val.contractId;
														obj.orderDetailsPayeeMapperDtosList=val.orderDetailsPayeeMapperDtosList;
														obj.tariffId=$scope.optionTariffId !=undefined && $scope.optionTariffId != null ? $scope.optionTariffId : $scope.defaultSelfTariffId;
														obj.billingBedCategoryId = val.billingBedCategoryId;
														obj.finalPrice = (val.finalPrice).toFixed(2);
														obj.finalPriceOld = (val.finalPrice).toFixed(2);
														$scope.optionalServiceList.push(obj);
													});
													
													
											}
												else
													{
													growl.warning("Please add base price of this service and then add..!!",{
														title : ERROR_MSG
													});
													return false
													}
												//concession
												console.log("FINAL ITEM",$item);
												
											
										});
									
										
									}else{
										
										if(parseInt($scope.contractId) == 0)
										{
										$scope.contractId = null;
										}
										
										var obj = {
												"patientId" : $scope.selectedPatient.patientId,
												"contractId" :$scope.contractIdForRate,
												"serviceId" :$item.serviceMasterId,
												"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
												"organizationId":$scope.organizationId,
												"unitId":$scope.unitId,
												"visitTypeId":$scope.visitTypeId!=null ? $scope.visitTypeId : 2,
												"billingBedCategoryId":$scope.visitTypeId==1 ? $scope.opdBillingCatId : $scope.billingBedCategoryId,
												"patientCategoryId":$scope.patientTypeId,
												"defaultSelfTariffId":$scope.tariffId !=undefined && $scope.tariffId != null ? $scope.tariffId : $scope.defaultSelfTariffId ,
											    "selfPayeeId" :$scope.selfPayeeId,
											    "paymentEntitlementId":$scope.paymentEntitlementIdForRate
											};
										
										console.log('Base prise',obj)
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/global/consumption/costEstimation/service";
										BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											if(response.data.status=="success"){
												if(response.data.object != "undefined" && response.data.object != null)
													{
													$scope.forServicePayeeId = response.data.object.payeeId;
													$item.basePrice=response.data.object.serviceRate;
													$item.quantity=1;
													$item.selfPayable = (response.data.object.selfPayable).toFixed(2);
													$item.creditPayable = (response.data.object.creaditPayable).toFixed(2);
													$item.selfPayableOld = (response.data.object.selfPayable).toFixed(2);
													$item.creditPayableOld = (response.data.object.creaditPayable).toFixed(2);
													$item.contractId = response.data.object.contractId;
													$item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
													$item.tariffId=response.data.object.tariffId;
													$item.finalPrice = (response.data.object.finalPrice).toFixed(2);
													$item.finalPriceOld = (response.data.object.finalPrice).toFixed(2);
													$scope.servicesListPopup.push($item);
													}
												else
													{
													growl.warning("Please add base price of this service and then add..!!",{
														title : ERROR_MSG
													});
													return false
													}
												//concession
												console.log("FINAL ITEM",$item);
												
											}
										});
									}
									
									
									
									$scope.search = "";
								}
							    
							    
							    
							    $scope.getServicerateByTariff = function(serviceMasterId,tariffId,obj){
							    	
							    	
							    	if(obj.packageMasterId != null){
							    		var list = [];
										angular.forEach($scope.billingBedCategoryOptionFiltterList, function (value, index) {
										var obj1 = {	
												"billingBedCategoryId": $scope.optionVisitTypeId==1 ? $scope.opdBillingCatId : value.billingBedCategoryId,
												"defaultSelfTariffId": $scope.optionTariffId !=undefined && $scope.optionTariffId != null ? $scope.optionTariffId : $scope.defaultSelfTariffId,
												"isDiscountApplicable": "Y",
												"organizationId": $scope.organizationId,
												"packageMasterId": obj.packageMasterId,
												"packageTypeId": obj.packageTypeId,
												"patientCategoryId": $scope.patientTypeId,
												"quantity": obj.quantity,
												"selfPayeeId": $scope.selfPayeeId,
												"serviceMasterId": obj.serviceMasterId,
												"sexId": 0,
												"specialityId": obj.specialityId,
												"subSpecialityId": obj.subSpecialityId,
												"unitId": $scope.unitId,
												"updateCap": 'N',
												"visitTypeId":$scope.optionVisitTypeId!=null ? $scope.optionVisitTypeId : 2 ,
												"basePrice":obj.basePrice		
											}
											list.push(obj1);
										});
										
											$rootScope.startLoader();
											var URI = BASE_URL + ROOT_URL + "/global/consumption/packageForCostE";
											BillingGenericService.serviceAction(METHOD_POST,URI, list, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
												if(response.data.status=="success"){
													
														angular.forEach(response.data.listObject, function (resval, resinx) {
														
															angular.forEach($scope.selectedServiceDetailsOption, function (val, inx) {
																
																if(val.billingBedCategoryId == resval.billingBedCategoryId && val.serviceMasterId == serviceMasterId){
																	val.totalAmt = (resval.finalPrice).toFixed(2);
																	val.tariffId = tariffId;
																}
																
															});
															
															
														});
														 $scope.calculateTotalAmountForDiscount();
												}
													else
														{
														growl.warning("Please add base price of this service and then add..!!",{
															title : ERROR_MSG
														});
														return false
														}
												
											});
											
											
							    	}else{
							    		
							    		
							    		var list = [];
										angular.forEach($scope.billingBedCategoryOptionFiltterList, function (value, index) {
											var obj = {
													"patientId" : $scope.selectedPatient.patientId,
													"contractId" :$scope.contractIdForRate,
													"serviceId" :serviceMasterId,
													"currentDate" : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
													"organizationId":$scope.organizationId,
													"unitId":$scope.unitId,
													"visitTypeId":$scope.optionVisitTypeId!=null ? $scope.optionVisitTypeId : 2,
													"billingBedCategoryId":$scope.optionVisitTypeId==1 ? $scope.opdBillingCatId : value.billingBedCategoryId,
													"patientCategoryId":$scope.patientTypeId,
													"defaultSelfTariffId":tariffId,
												    "selfPayeeId" :$scope.selfPayeeId,
												    "paymentEntitlementId":1
												};
											list.push(obj);
										});
										
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/global/consumption/costEstimation/service/billingbed";
										BillingGenericService.serviceAction(METHOD_POST,URI, list, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											if(response.data.status=="success"){
												
													angular.forEach(response.data.listObject, function (resval, resinx) {
													
														angular.forEach($scope.selectedServiceDetailsOption, function (val, inx) {
															
															if(val.billingBedCategoryId == resval.billingBedCategoryId && val.serviceMasterId == serviceMasterId){
																val.totalAmt = (resval.finalPrice).toFixed(2);
																val.tariffId = tariffId;
															}
															
														});
														
														
													});
													 $scope.calculateTotalAmountForDiscount();
											}
												else
													{
													growl.warning("Please add base price of this service and then add..!!",{
														title : ERROR_MSG
													});
													return false
													}
											
										});
							    		
							    	}
							    	
							    
							    	
							    }
							    
							    
							    

								 var isPriorityZeroExists;
									function checkPriorityZeroOrNot(arr) {
										return arr
												.some(function(arr) {
													if(parseInt(arr.priority) == 0 || arr.priority == "")
														{
															return true;
														}
													else
														{
															return false;
														}
												});
									}
								
									
							    $scope.savePaymentEntitlementForCost = function()
							    {
							    	
							    	var dateOfAdm  =  moment($scope.datepicker).format('YYYY-MM-DD');
									var dischargeDate = moment($scope.datepickerPdd).format('YYYY-MM-DD');
							    	
							    	if($scope.selectedPatient.patientId == null || $scope.selectedPatient.patientId == '')
							    		{
							    			 growl.warning("Please select patient..!!",{	
													title : "WARNING"
												});
												return;
							    }else if($scope.patientTypeId == null || $scope.patientTypeId == '' ||  isNaN($scope.patientTypeId))
							    	{
								    	 growl.warning("Please select patient category..!!",{	
												title : "WARNING"
											});
											return;
							    	}
							    else if($scope.visitTypeId!=1 && ($scope.bedCategoryId == null || $scope.bedCategoryId == '' ||  isNaN($scope.bedCategoryId)))
						    	{
							    	 growl.warning("Please select bed category..!!",{	
											title : "WARNING"
										});
										return;
						    	}
							    	else{

								    	isPriorityZeroExists=checkPriorityZeroOrNot($scope.paymentEntitleMentList);
										  if (isPriorityZeroExists) {
											
											  growl.warning("Please select priority of all paymententitlement list.",{	
												title : "WARNING"
											});
											// $scope.tab = 3;
											return;
											
										  }
										  else{
											  $rootScope.startLoader();
											  var data = {};
											  var sponserList = [];
												angular.forEach($scope.paymentEntitleMentList, function (value, key) 
												{
													var newArr = {};
													newArr.organizationId = $scope.organizationId ;
													newArr.unitId = $scope.unitId;
													newArr.visitTypeId = $scope.visitTypeId;
													newArr.paymentEntitlementId =value.paymentEntitlementId;
													newArr.payeeId = value.companyId;
													newArr.priorityId = value.priority;
													newArr.createdBy =$scope.userId;
													newArr.createdDate = $scope.currentDate;
													newArr.updatedBy = $scope.userId;
													newArr.updatedDate = $scope.currentDate;
													newArr.status = 'A';
													newArr.tariffId =value.tariffId;
													newArr.contractId =value.contractId;
													newArr.insuranceNumber = value.insuranceNumber;
													newArr.insuredAmount = value.insuredAmount;
													newArr.insuranceId = value.insuranceId;
													newArr.validityTo = value.endDate;
													newArr.validityFrom = value.startDate;
													newArr.patientId = $scope.selectedPatient.patientId
													if(value.companyId != $scope.selfPayeeId && value.priority == 1)
												    {
													     $scope.contractIdForRate = value.contractId;
													     $scope.companyIdForRate = value.companyId;
													     $scope.paymentEntitlementIdForRate = value.paymentEntitlementId;
												    }
													sponserList.push(newArr);
												});
												data.patientSponsorDetailsDtosList = sponserList;
												data.visitTypeId = $scope.visitTypeId!=null ? $scope.visitTypeId : 2;
												data.billingBedCategoryId = $scope.visitTypeId==1 ? $scope.opdBillingCatId : $scope.billingBedCategoryId;
												data.patientId = $scope.selectedPatient.patientId;
												data.patietCategoryId = $scope.patientTypeId;
												data.doa = dateOfAdm;
												data.pdd = dischargeDate;
												data.unitId = $scope.unitId;
												data.organizationId = $scope.organizationId;
												var URI = BASE_URL + ROOT_URL + "/api/opd/addPatientSponsorDetails/costEstimation";
												BillingGenericService.serviceAction(METHOD_POST, URI,data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
													if (response.data.status == "success") {
														 $scope.selectedServiceDetails = [];
														angular.forEach(response.data.listObject, function (value, key) 
														{
											    			var newArr = {};
											    			newArr.contractId = value.contractId;//Ganesh
											    			newArr.specialityId = value.specialityId;
											    			newArr.serviceSpecialityId = value.serviceSpecialityId;
											    			newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
											    			newArr.serviceMasterId = value.serviceMasterId;
											    			newArr.serviceStandardCode = value.serviceStandardCode;
											    			newArr.serviceStandardName = value.serviceName;
											    			newArr.specialityName = value.specialityName;
											    			newArr.quantity = value.quantity!=null ? value.quantity : 1	;
											    			newArr.basePrice = value.serviceRate;
											    			newArr.totalAmt = value.finalPrice;
											    			newArr.totalAmtOld = value.finalPriceOld;
											    			newArr.selfPayable = value.selfPayable;
											    			newArr.creditPayable = value.creaditPayable;
											    			newArr.createdBy  = $scope.userId;
											    			newArr.createdDate = new Date().getTime();
											    			newArr.isPackageService = value.isPackageService;
											    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
											    			newArr.isCapService = value.isCapService;
											    			newArr.packagePayable = value.packagePayable;
											    			newArr.subSpecialityId = value.subSpecialityId;
											    			newArr.packageId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
											    			newArr.packageTypeId = value.packageTypeId;
											    			newArr.packageMasterId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
											    			newArr.serviceRendered = 1;
											    			newArr.status = "A";
											    			newArr.ordCancelReasonId  = '';
											    			newArr.ordCancelDatetime  = new Date().getTime();
											    			newArr.ordCancelRemark  = '';
											    			newArr.ordRenderDatetime =moment(value.renderingDate).format('x');
											    			newArr.payeeId  = value.payeeId;
											        		newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
											        		newArr.tariffId = value.tariffId;
											        		$scope.selectedServiceDetails.push(newArr);
											        		
											        		
													});
											    		$scope.calculateTotalAmount();
														
														$scope.addServiceFlag = true;
														$rootScope.stopLoader();
													}
													else
														{
														$rootScope.stopLoader();
														 /*growl.error("Error with saving recoed..!!",{	
																title : "ERROR"
															});
															return;*/
														   
														}
												});
										  }
							    		}
							    }
							    
							    $scope.calculateTotalAmount = function()
							    {
							    		
							    		$scope.totalAmountMain = parseFloat(0);
							    		$scope.selfAmountMain = parseFloat(0);
							    		$scope.creditAmountMain = parseFloat(0);
							    		
							    		angular.forEach($scope.selectedServiceDetails, function (value, key) {
							    			$scope.totalAmountMain = (parseFloat($scope.totalAmountMain) + parseFloat(value.totalAmt)).toFixed(2);
							    			$scope.selfAmountMain = (parseFloat($scope.selfAmountMain) + parseFloat(value.selfPayable)).toFixed(2);
							    			$scope.creditAmountMain = (parseFloat($scope.creditAmountMain) + parseFloat(value.creditPayable)).toFixed(2);
							    		});
							    }
							    
							    $scope.calculateTotalAmountOption = function()
							    {
							    		
							    		$scope.totalAmountMain = parseFloat(0);
							    		$scope.selfAmountMain = parseFloat(0);
							    		$scope.creditAmountMain = parseFloat(0);
							    		
							    		angular.forEach($scope.selectedServiceDetailsOption, function (value, key) {
							    			$scope.totalAmountMain = (parseFloat($scope.totalAmountMain) + parseFloat(value.totalAmt)).toFixed(2);
							    			$scope.selfAmountMain = (parseFloat($scope.selfAmountMain) + parseFloat(value.selfPayable)).toFixed(2);
							    			$scope.creditAmountMain = (parseFloat($scope.creditAmountMain) + parseFloat(value.creditPayable)).toFixed(2);
							    		});
							    }
							    
							    $scope.removeTempServicesByIndex = function(index,serviceMasterId){
									
							    	if($scope.fromOptionCheck == true){
							    		
							    		var serviceDetailsList = [];
										
										angular.forEach($scope.optionalServiceList, function (value, key) {
											if(value.serviceMasterId != serviceMasterId){
												serviceDetailsList.push(value);
											}
										});
										
										$scope.optionalServiceList = serviceDetailsList;
										$scope.servicesListPopupTemp.splice(index, 1);
										$scope.servicesListPopup.splice(index, 1);
							    	}else{
							    		$scope.servicesListPopup.splice(index, 1);
							    	}
							    	
								}
							    
							    $scope.changeAmount = function(qty,idx)
							    {
							    	if(qty > 0)
							    		{
							    		 
							    		 if($scope.servicesListPopup[idx].orderDetailsPayeeMapperDtosList!=null)
						    			 {
							    			 
							    			 if( $scope.contractIdForRate==undefined || $scope.contractIdForRate==null)
							    				 {
								    				 $scope.servicesListPopup[idx].finalPrice = $scope.servicesListPopup[idx].finalPriceOld * qty;
										    		 $scope.servicesListPopup[idx].selfPayable = $scope.servicesListPopup[idx].selfPayableOld * qty;
										    		 $scope.servicesListPopup[idx].creditPayable = $scope.servicesListPopup[idx].creditPayableOld * qty;
									    		 
							    				 	angular.forEach($scope.servicesListPopup[idx].orderDetailsPayeeMapperDtosList, function (value, key) {
											    		
								    				 	if(value.payeeId==$scope.selfPayeeId)
							    				 		{
								    				 		value.payeeAmount = $scope.servicesListPopup[idx].finalPrice;
							    				 		}
								    				 
										    		});
							    				 }
							    			 else{
							    				 if($scope.servicesListPopup[idx].isQuantityEditable=='Y' && $scope.servicesListPopup[idx].isRateEditable=='Y')
							    					 {
								    					 $scope.servicesListPopup[idx].finalPrice = $scope.servicesListPopup[idx].finalPriceOld * qty;
											    		 $scope.servicesListPopup[idx].selfPayable = $scope.servicesListPopup[idx].selfPayableOld * qty;
											    		 $scope.servicesListPopup[idx].creditPayable = $scope.servicesListPopup[idx].creditPayableOld * qty;
										    		 
								    				 	angular.forEach($scope.servicesListPopup[idx].orderDetailsPayeeMapperDtosList, function (value, key) {
									    				 	if(value.payeeId!=$scope.selfPayeeId && value.isTariffRateApplicable=='Y')
								    				 		{
									    				 		value.payeeAmount = $scope.servicesListPopup[idx].creditPayable;
								    				 		}
								    				 	});
							    					 }
							    				 
							    				 
							    			 }
						    			 }
							    		 
							    		 
							    		 angular.forEach($scope.optionalServiceList, function (value, key) {
							    			 
						    				 	if( $scope.servicesListPopup[idx].serviceMasterId == value.serviceMasterId)
					    				 		{
						    				 		
						    				 		value.quantity = qty;
						    				 		value.finalPrice = value.finalPriceOld * qty;
						    				 		value.selfPayable = value.selfPayableOld * qty;
						    				 		value.creditPayable = value.creditPayableOld * qty;
						    				 		value.orderDetailsPayeeMapperDtosList[0].payeeAmount = value.finalPrice;
						    				 		
					    				 		}
						    				 	
					    				 	});
							    		 
							    		 
							    		 
							    		 
							    		 
							    		}
							    	else
							    		{
								    		growl.warning("Quantity should be greater than ZERO(0)..!!",{
												title : WARNING_MSG
											});
								    		
											return false
							    		}
							    }
							    
							    $scope.selectedServiceDetails = [];
							    $scope.selectedServiceDetailsOption = [];
							    $scope.addTempServiesToServiceMasterList = function(){
							    	
							    	if($scope.fromOptionCheck == true){
							    		
							    		angular.forEach($scope.servicesListPopupTemp, function (value, key) {
							    			$scope.servicesListPopupOption.push(value);
							    		});
							    		
								    	var i =1;
							    		angular.forEach($scope.optionalServiceList, function (value, key) {
							    	    	
							    			var newArr = {};
							    			newArr.contractId = value.contractId;//Ganesh
							    			newArr.specialityId = value.specialityId;
							    			newArr.serviceSpecialityId = value.serviceSpecialityId;
							    			newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
							    			newArr.serviceMasterId = value.serviceMasterId;
							    			newArr.serviceStandardCode = value.serviceStandardCode;
							    			newArr.serviceStandardName = value.serviceStandardName;
							    			newArr.specialityName = value.specialityName;
							    			newArr.quantity = value.quantity;
							    			newArr.basePrice = value.basePrice;
							    			newArr.totalAmt = value.finalPrice;
							    			newArr.totalAmtOld = value.finalPriceOld;
							    			newArr.selfPayable = value.selfPayable;
							    			newArr.creditPayable = value.creditPayable;
							    			newArr.createdBy  = $scope.userId;
							    			newArr.createdDate = new Date().getTime();
							    			newArr.isPackageService = value.isPackageService;
							    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
							    			newArr.isCapService = value.isCapService;
							    			newArr.packagePayable = value.packagePayable;
							    			newArr.subSpecialityId = value.subSpecialityId;
							    			newArr.packageId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
							    			newArr.packageTypeId = value.packageTypeId;
							    			newArr.packageMasterId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
							    			newArr.serviceRendered = 1;
							    			newArr.status = "A";
							    			newArr.ordCancelReasonId  = '';
							    			newArr.ordCancelDatetime  = new Date().getTime();
							    			newArr.ordCancelRemark  = '';
							    			newArr.ordRenderDatetime =moment(value.renderingDate).format('x');
							    			newArr.payeeId  = value.payeeId;
							        		newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
							        		newArr.tariffId = value.tariffId;
							        		newArr.billingBedCategoryId = value.billingBedCategoryId;
							        		$scope.selectedServiceDetailsOption.push(newArr);
							        		
							        		if(i == $scope.optionalServiceList.length)
							        			{
							        			 
							        			  angular.element('#addServicePopup').modal('hide');
							        			}
							        		i++;
									});
							    		$scope.calculateTotalAmountOption();
						    
							    		$scope.optionalServiceList = [];
							    		$scope.servicesListPopupTemp = [];
							    		$scope.selectedPackageList = [];
							    		 $scope.calculateTotalAmountForDiscount();
							    		
							    	}else{
							    	
									    	var i =1;
									    	var j = 1;
									    
								    		angular.forEach($scope.servicesListPopup, function (value, key) {
								    	    	
								    			var newArr = {};
								    			newArr.contractId = value.contractId;//Ganesh
								    			newArr.specialityId = value.specialityId;
								    			newArr.serviceSpecialityId = value.serviceSpecialityId;
								    			newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
								    			newArr.serviceMasterId = value.serviceMasterId;
								    			newArr.serviceStandardCode = value.serviceStandardCode;
								    			newArr.serviceStandardName = value.serviceStandardName;
								    			newArr.specialityName = value.specialityName;
								    			newArr.quantity = value.quantity;
								    			newArr.basePrice = value.basePrice;
								    			newArr.totalAmt = value.finalPrice;
								    			newArr.totalAmtOld = value.finalPriceOld;
								    			newArr.selfPayable = value.selfPayable;
								    			newArr.creditPayable = value.creditPayable;
								    			newArr.createdBy  = $scope.userId;
								    			newArr.createdDate = new Date().getTime();
								    			newArr.isPackageService = value.isPackageService;
								    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
								    			newArr.isCapService = value.isCapService;
								    			newArr.packagePayable = value.packagePayable;
								    			newArr.subSpecialityId = value.subSpecialityId;
								    			newArr.packageId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
								    			newArr.packageTypeId = value.packageTypeId;
								    			newArr.packageMasterId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
								    			newArr.serviceRendered = 1;
								    			newArr.status = "A";
								    			newArr.ordCancelReasonId  = '';
								    			newArr.ordCancelDatetime  = new Date().getTime();
								    			newArr.ordCancelRemark  = '';
								    			newArr.ordRenderDatetime =moment(value.renderingDate).format('x');
								    			newArr.payeeId  = value.payeeId;
								        		newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
								        		newArr.tariffId = value.tariffId;
								        		$scope.selectedServiceDetails.push(newArr);
								        		
								        		if(i == $scope.servicesListPopup.length)
								        			{
								        			 
								        			  angular.element('#addServicePopup').modal('hide');
								        			}
								        		i++;
										});
								    		
								    		angular.forEach($scope.selectedPackageList, function (value, key) {
								    	    	
								    			var newArr = {};
								    			newArr.contractId = value.contractId;//Ganesh
								    			newArr.specialityId = value.specialityId;
								    			newArr.serviceSpecialityId = value.serviceSpecialityId;
								    			newArr.serviceSubSpecialityId = value.serviceSubSpecialityId;
								    			newArr.serviceMasterId = value.serviceMasterId;
								    			newArr.serviceStandardCode = value.serviceStandardCode;
								    			newArr.serviceStandardName = value.serviceStandardName;
								    			newArr.specialityName = value.specialityName;
								    			newArr.quantity = value.quantity;
								    			newArr.basePrice = value.basePrice;
								    			newArr.totalAmt = value.basePrice;
								    			newArr.totalAmtOld = value.finalPriceOld;
								    			newArr.selfPayable = value.selfPayable;
								    			newArr.creditPayable = value.creditPayable;
								    			newArr.createdBy  = $scope.userId;
								    			newArr.createdDate = new Date().getTime();
								    			newArr.isPackageService = value.isPackageService;
								    			newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
								    			newArr.isCapService = value.isCapService;
								    			newArr.packagePayable = value.packagePayable;
								    			newArr.subSpecialityId = value.subSpecialityId;
								    			newArr.packageId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
								    			newArr.packageTypeId = value.packageTypeId;
								    			newArr.packageMasterId = typeof(value.packageId) != "undefined" ? value.packageId : value.packageMasterId;
								    			newArr.serviceRendered = 1;
								    			newArr.status = "A";
								    			newArr.ordCancelReasonId  = '';
								    			newArr.ordCancelDatetime  = new Date().getTime();
								    			newArr.ordCancelRemark  = '';
								    			newArr.ordRenderDatetime =moment(value.renderingDate).format('x');
								    			newArr.payeeId  = value.payeeId;
								        		newArr.orderDetailsPayeeMapperDtosList = value.orderDetailsPayeeMapperDtosList;
								        		newArr.tariffId = value.tariffId;
								        		$scope.selectedServiceDetails.push(newArr);
								        		
								        		if(j == $scope.selectedPackageList.length)
								        			{
								        			 
								        			  angular.element('#addServicePopup').modal('hide');
								        			}
								        		j++;
										});
								    		$scope.calculateTotalAmount();
							    }
						    }
							  
							    $scope.checkList = function()
							    {
							    	if($scope.selectedServiceDetails.length == 0)
									{
										growl.warning("Please add atleast one service..!!",{
											title : WARNING_MSG
										});
										return false
									}
									else{
										 angular.element('#confirmPopupCost').modal('show');
									}
							    }
							    
							    
							    $scope.checkListOption = function()
							    {
							    	if($scope.selectedServiceDetailsOption.length == 0)
									{
										growl.warning("Please add atleast one service..!!",{
											title : WARNING_MSG
										});
										return false
									}
									else{
										 angular.element('#confirmPopupCostOption').modal('show');
									}
							    }
							    
							    
							    
							    
							$scope.saveEstimate= function()
							{
								if($scope.selectedServiceDetails.length == 0)
								{
									growl.warning("Please add atleast one service..!!",{
										title : WARNING_MSG
									});
									return false
								}
								 if($scope.patientTypeId == null || $scope.patientTypeId == '' ||  isNaN($scope.patientTypeId))
						    	{
							    	 growl.warning("Please select patient category..!!",{	
											title : "WARNING"
										});
										return;
						    	}
						     if($scope.visitTypeId!=1 && ($scope.bedCategoryId == null || $scope.bedCategoryId == '' ||  isNaN($scope.bedCategoryId)))
					    	{
						    	 growl.warning("Please select bed category..!!",{	
										title : "WARNING"
									});
									return;
					    	}

								var data = {
									visitTypeId : $scope.visitTypeId!=null ? $scope.visitTypeId : 2,
									billingBedCategoryId : $scope.visitTypeId==1 ? $scope.opdBillingCatId : $scope.billingBedCategoryId,
									patientId : $scope.selectedPatient.patientId,
									patietCategoryId : $scope.patientTypeId,
									doa : moment($scope.datepicker).format('YYYY-MM-DD HH:mm:ss'),
									pdd : moment($scope.datepickerPdd).format('YYYY-MM-DD HH:mm:ss'),
									unitId : $scope.unitId,
									orgId : $scope.organizationId,
									totalAmount : $scope.totalAmountMain,
									selfPayable : $scope.selfAmountMain,
									creditPayable : $scope.creditAmountMain,
									costEstimationDetailsDtosList : $scope.selectedServiceDetails,
									newWhatsAppNo : $scope.newWhatsAppNumber !== undefined && $scope.newWhatsAppNumber !== null ? $scope.newWhatsAppNumber : ""
								}
								
								
								
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/billing/bill/estimate";
								BillingGenericService.serviceAction(METHOD_POST, URI,data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
									if (response.data.status == "success") {
										$scope.addServiceFlag = false;
										$scope.selectedServiceDetails = [];
										$scope.init();
										
										var data ={
								            "appName": "HIS",
								            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								            "reportDesignFilePath": "IpdBillEstimation.rpt",
								            "reportFormat": "PDF",
								            "exportFileName": "Bill Estimation",
								            "reportParams": [
								              {
									             "paramName": "id",
									             "paramType": "integer",
									             "paramValue": response.data.object.object
								              }
								              
								            ]
								         }
										
										console.log('data',data);
										reportService.generate(REPORT_API_URI1,data);
										$rootScope.stopLoader();
									}
								});
								
								console.log('####Estimate',$scope.selectedServiceDetails);
							}
							
							$scope.saveEstimateOption = function(){
								
								if($scope.selectedServiceDetailsOption.length == 0)
								{
									growl.warning("Please add atleast one service..!!",{
										title : WARNING_MSG
									});
									return false
								}
								 
								 var list = [];
								 
								 angular.forEach($scope.billingBedCategoryOptionFiltterList, function (value, key) {
										
									 if(value.isSelected ==true){
											
											 angular.forEach($scope.selectedServiceDetailsOption, function (value1, key1) {
												 
													if(value.billingBedCategoryId == value1.billingBedCategoryId ){
														
														list.push(value1);
														
													}
												});
										}
									});

								 if(list.length == 0)
									{
										growl.warning("Please add atleast one billing bed category..!!",{
											title : WARNING_MSG
										});
										return false
									}
								 
								
								 if($scope.patientTypeId == null || $scope.patientTypeId == '' ||  isNaN($scope.patientTypeId))
						    	{
							    	 growl.warning("Please select patient category..!!",{	
											title : "WARNING"
										});
										return;
						    	}
							    
								
								 
								 
								 $scope.totalAmountMain = parseFloat(0);
						    		$scope.selfAmountMain = parseFloat(0);
						    		$scope.creditAmountMain = parseFloat(0);
						    		$scope.discountAmount = parseFloat(0);
						    		angular.forEach($scope.billingBedCategoryCalculationList, function (value, key) {
						    			var check = false;
						    			angular.forEach(list , function (value1, key1) {
											 
											if(value.billingBedCategoryId == value1.billingBedCategoryId ){
												check = true;
												
											}
										});
						    			if(check){
						    				$scope.totalAmountMain = $scope.totalAmountMain + parseFloat(value.totalAmount);
											$scope.selfAmountMain = $scope.selfAmountMain + parseFloat(value.totalAmount);
											$scope.discountAmount = $scope.discountAmount + parseFloat(value.discount);
						    			}
						    			$scope.creditAmountMain = 0;
						    		});

						    		
								var data = {
									visitTypeId : $scope.optionVisitTypeId!=null ? $scope.optionVisitTypeId : 2,
									billingBedCategoryId : $scope.optionVisitTypeId==1 ? $scope.opdBillingCatId : list[0].billingBedCategoryId,
									patientId : $scope.selectedPatient.patientId,
									patietCategoryId : $scope.patientTypeId,
									doa : moment($scope.datepicker).format('YYYY-MM-DD HH:mm:ss'),
									pdd : moment($scope.datepickerPdd).format('YYYY-MM-DD HH:mm:ss'),
									unitId : $scope.unitId,
									orgId : $scope.organizationId,
									totalAmount : $scope.totalAmountMain,
									selfPayable : $scope.selfAmountMain,
									creditPayable : $scope.creditAmountMain,
									discountAmount : $scope.discountAmount,
									mail:$scope.isTempPatientReg.emailId,
									patientName : $scope.isTempPatientReg.patientName,
									prefixId : $scope.isTempPatientReg.prefix,
									updatedBy : $scope.userId,
									createdBy : $scope.userId,
									registrationTypeId:4,
									mobileNumber : $scope.isTempPatientReg.mobileNumber,
									patientGender : $scope.isTempPatientReg.patientGender.id,
									costEstimationDetailsDtosList : list,
									remark : $scope.remark,
									newWhatsAppNo : $scope.newWhatsAppNumberOption !== undefined && $scope.newWhatsAppNumberOption !== null ? $scope.newWhatsAppNumberOption : "",
								    isManuallySend : $scope.isManuallySend
								}					
								
								
								
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/billing/bill/estimate";
								BillingGenericService.serviceAction(METHOD_POST, URI,data, NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
									if (response.data.status == "success") {
										$scope.addServiceFlag = false;
										$scope.init();
										$scope.selectedServiceDetailsOption =[];
										$scope.servicesListPopupOption = [];
										$scope.remark = "";
										$scope.isManuallySend = false;
										$scope.changeVisitType();
										var data ={
								            "appName": "HIS",
								            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								            "reportDesignFilePath": "IpdBillEstimation.rpt",
								            "reportFormat": "PDF",
								            "exportFileName": "Bill Estimation",
								            "reportParams": [
								              {
									             "paramName": "id",
									             "paramType": "integer",
									             "paramValue":  response.data.object.object
								              }
								              
								            ]
								         }
										
										console.log('data',data);
										$scope.isTempPatientReg.mobileNumber="";
									      $scope.isTempPatientReg.patientAddress="";
											 $scope.isTempPatientReg.patientName="";
											 $scope.isTempPatientReg.patientGender="";
											 $scope.isTempPatientReg.emailId="";
											 $scope.isTempPatientReg.prefix="";
										reportService.generate(REPORT_API_URI1,data);
										
										$rootScope.stopLoader();
									}
									$scope.isManuallySend = false;
								});
								
								
							}
							  
							$scope.addInsurances = function()
							{
								var data = {
										'companyId':"",
										'associateCompanyId':"",
										'contractId':"",
										//'statrDate':"",
										//'endDate':"",
										'insuranceNumber':"",
										'insuredAmt':""
									};
								$scope.paymentEntitleMentInsurance.push(data);
							}
							
							
							$scope.getBedCategory=function(id)
							{
								if(id!=null)
								{
									var URI = BASE_URL + ROOT_URL
									+ GETACTIVEBILLINGBEDCATEGORYBYBEDCATEGORYID;
									var data = {
										organizationId:$scope.organizationId,
										bedCategoryId:id
									}
									GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.billingBedCategoryList = [];

													if (response.data.status == "success") {
														$scope.billingBedCategoryList = [];
														$scope.billingBedCategoryList = response.data.listObject;
														$scope.billingBedCategoryId = $scope.billingBedCategoryList[0].billingBedCategoryId;
														console.log("billingBedCategoryList",$scope.billingBedCategoryList);
													}
												});
								}
								
							}
							$scope.billingBedCategoryOptionList = [];
							$scope.billingBedCategoryOptionFiltterList = [];
							$scope.getbillingBedCategoryOptionList = function()
							{
									var URI = BASE_URL + ROOT_URL+ "/adt/getBedCategoryListByVisitId";
									var data = {
										organizationId:$scope.organizationId,
										unitId:$scope.unitId
									}
									GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.billingBedCategoryOptionList = [];
													$scope.billingBedCategoryOptionFiltterList = [];
													if (response.data.status == "success") {
														$scope.billingBedCategoryOptionList = [];
														$scope.billingBedCategoryOptionList = response.data.listObject;
														
														angular.forEach($scope.billingBedCategoryOptionList, function (value, key) {
															value.isSelected = false;
															if(value.visitTypeId == $scope.optionVisitTypeId){
																$scope.billingBedCategoryOptionFiltterList.push(value);
															}
														});
													}
												});
								
							}
							
							$scope.billingBedCategoryCalculationList = [];
							$scope.changeVisitType = function(){
								$scope.billingBedCategoryOptionFiltterList = [];
								$scope.billingBedCategoryCalculationList = [];
								angular.forEach($scope.billingBedCategoryOptionList, function (value, key) {
									value.isSelected = false;
									if(value.visitTypeId == $scope.optionVisitTypeId){
										$scope.billingBedCategoryOptionFiltterList.push(value);
									}
								});
								
								$scope.billingBedCategoryCalculationList = $scope.billingBedCategoryOptionFiltterList;
								
								angular.forEach($scope.billingBedCategoryCalculationList, function (value, key) {
									value.netAmount = 0;
									value.totalAmount = 0;
									value.discount = 0;
									
								});
								$scope.selectedServiceDetailsOption = [];
								$scope.servicesListPopupOption = [];
								
							}
							
							
						
							
							$scope.getbillingBedCategoryOptionList();
							
							if(!angular.equals({},$stateParams.selectedPatient))
							{
								$scope.selectedPatient =$stateParams.selectedPatient ;
							}
							console.log('selectedpatient',$scope.selectedPatient);
							if($scope.selectedPatient != null && $scope.selectedPatient != undefined && $scope.selectedPatient.admissionNoteId!=null)
							{
								$(".patienttSearchResult").toggle();
								$scope.enabaleBackButton = true;
								$scope.patientTypeId = $scope.selectedPatient.patientCategoryId;
								$scope.bedCategoryId = $scope.selectedPatient.bedCategoryId;
								$scope.patientId = $scope.selectedPatient.patientId;
								$scope.getBedCategory($scope.bedCategoryId);
							
								$scope.genderId = $scope.selectedPatient.genderId;
								
								if($scope.selectedPatient.visitTypeId == 3 || $scope.selectedPatient.visitTypeId == 2 || $scope.selectedPatient.visitTypeId == 4)
									{
										$scope.visitTypeId = 2;
									}
								else
									{
									  $scope.visitTypeId = $scope.selectedPatient.visitTypeId;
									}
								
								
								 $scope.datepicker = new Date($scope.selectedPatient.datepicker);
								 $scope.datepickerPdd = new Date($scope.selectedPatient.datepickerPdd);
								 
								
							}
							
							$scope.clear = function(){
								$scope.optionalServiceList = [];
								$scope.servicesListPopupTemp = [];
								$scope.selectedPackageList = [];
							}
							
							$scope.removeServiceOption = function(index,serviceMasterId){
								
								var serviceDetailsList = [];
								
								angular.forEach($scope.selectedServiceDetailsOption, function (value, key) {
									if(value.serviceMasterId != serviceMasterId){
										serviceDetailsList.push(value);
									}
								});
								
								$scope.selectedServiceDetailsOption = serviceDetailsList;
								$scope.servicesListPopupOption.splice(index, 1);
								 $scope.calculateTotalAmountForDiscount();
								
							}
							
							 $scope.calculateTotalAmountForDiscount = function()
							    {
							    		
							    		angular.forEach($scope.billingBedCategoryCalculationList, function (value, key) {
							    			$scope.totalAmount = parseFloat(0);
							    			angular.forEach($scope.selectedServiceDetailsOption, function (value1, key1) {
								    			
								    			if(value.billingBedCategoryId == value1.billingBedCategoryId ){
								    				
								    				$scope.totalAmount = $scope.totalAmount + parseFloat(value1.totalAmt);
								    			}

								    		});

							    			value.totalAmount = $scope.totalAmount;
							    			value.netAmount = $scope.totalAmount - parseFloat(value.discount);
							    			if(!value.netAmount){
                                            	value.netAmount=value.totalAmount;
                                            	}
							    			
							    		});
							    }
							 
							 self.model = {
								        isOTC: false,
								        walkInSale: {
								          id: "1" + (new Date()).getTime(),
								          items: [],
								          discountType: '1'
								        },
								        isBatchValid: [],
								        isUOMValid: [],
								        isQuantityValid: [],
								        SelectedItems: [],
								        prefixList: [{
								          "prefix": "Select"
								        }],
								        patientGenderList: [{
								          "gender": "Select"
								        }],
								        discountCategoryList: [{
								          "discountCategory": "Select Discount Category"
								        }],
								        //patientSearchFlag: true
								      };
							 
							 
							 
							 
							$scope.getPrefixWithGender = function () {
						    	    var URI = BASE_URL + ROOT_URL+ GETACTIVEPREFIXLIST;
						    	    $rootScope.startLoader();
						    	    GenericService.serviceAction(METHOD_GET, URI, {},NOTIFICATION_MSG_STATUS_FALSE).then(
						    				function(response) {
						    					//console.log(response);
						    					$scope.prefixListWithGender = [];
						    					if (response.data.status == "success"){
						    						$scope.prefixListWithGender = response.data.listObject;
						    					}				
						    					 $rootScope.stopLoader();
						    				});
						    	    }
							$scope.getPrefixWithGender();
							 
							 
							 $scope.isTempPatientReg.isPrefixValid = "";
							 $scope.isTempPatientReg.isPatientAddressValid=false;
							 
							 $scope.selectGender = function(prefix)
								{
						    	 let id = prefix;
						    	 if(id > 0){
										var genId = $.grep($scope.prefixListWithGender, function (doc) {
						    	            return doc.prefixId == id;
						    	        })[0].id;
										$scope.isTempPatientReg.genderId= genId;
										//$scope.isTempPatientReg.patientGender = genId;
										}
										else
											{
											//$scope.patientObj.prefixId = '';
											//$scope.patientObj.genderId = '';
											}
										
								}
							
							 
							 $scope.GetPrefixWithGender = function () {
						    	    var URI = BASE_URL + ROOT_URL+ GETACTIVEPREFIXLIST;
						    	    $rootScope.startLoader();
						    	    GenericService.serviceAction(METHOD_GET, URI, {},NOTIFICATION_MSG_STATUS_FALSE).then(
						    				function(response) {
						    					//console.log(response);
						    					$scope.isTempPatientReg.prefixListWithGender = [];
						    					if (response.data.status == "success"){
						    						$scope.isTempPatientReg.prefixListWithGender = response.data.listObject;
						    					}				
						    					 $rootScope.stopLoader();
						    				});
						    	    }
							 
							 
							 
							 function updateValues() {
								    var title = document.getElementById("titleDropdown").value;
								  }
							 
							 function genderDropdown() {
								    var title = document.getElementById("genderDropdown").value;
								  }
							 
							 
								$scope.searchPackages = function(packageSearchKeyword)
								{
									    var packageSearchObj = {};
										packageSearchObj.organisationId = $scope.organizationId;
										packageSearchObj.unitId = $scope.unitId;
										packageSearchObj.visitTypeId = $scope.optionVisitTypeId!=null ? $scope.optionVisitTypeId : 2;// default op
										packageSearchObj.bedBillingCategoryId = $scope.billingBedCategoryOptionFiltterList[0].billingBedCategoryId;// default
																										// op
										packageSearchObj.genderId = $scope.genderId;
										packageSearchObj.paymentEntitlementId = 1;
										packageSearchObj.patientTypeId = $scope.patientTypeId;
										packageSearchObj.serviceCode = packageSearchKeyword;
										packageSearchObj.serviceName = packageSearchKeyword;
										return servicesFactory.searchPackageForBilling(packageSearchObj,$scope);
								}
							
								$scope.selectedPackageList = [];
								
								$scope.selectPackageService = function($item, $model, $label){
								    
								    var checkExits = false;
									angular.forEach($scope.servicesListPopupOption, function (value, key) {
						    			if(value.serviceMasterId == $item.serviceMasterId){
						    				checkExits = true;
						    			}
						    		});
									
									angular.forEach($scope.servicesListPopupTemp, function (value, key) {
						    			if(value.serviceMasterId == $item.serviceMasterId){
						    				checkExits = true;
						    			}
						    		});
									
									if(checkExits == true){
										growl.warning("This service already added ",{
											title : ERROR_MSG
										});
										return false;
									}
									$scope.packageResultObj =  $item;
									var list = [];
									angular.forEach($scope.billingBedCategoryOptionFiltterList, function (value, index) {
									var obj = {	
											"billingBedCategoryId": $scope.optionVisitTypeId==1 ? $scope.opdBillingCatId : value.billingBedCategoryId,
											"defaultSelfTariffId": $scope.optionTariffId !=undefined && $scope.optionTariffId != null ? $scope.optionTariffId : $scope.defaultSelfTariffId,
											"isDiscountApplicable": "Y",
											"organizationId": $scope.organizationId,
											"packageMasterId": $item.packageMasterId,
											"packageTypeId": $item.packageTypeId,
											"patientCategoryId": $scope.patientTypeId,
											"quantity": $item.quantity,
											"selfPayeeId": $scope.selfPayeeId,
											"serviceMasterId": $item.serviceMasterId,
											"sexId": 0,
											"specialityId": $item.specialityId,
											"subSpecialityId": $item.subSpecialityId,
											"unitId": $scope.unitId,
											"updateCap": 'N',
											"visitTypeId":$scope.optionVisitTypeId!=null ? $scope.optionVisitTypeId : 2 ,
											"basePrice":$item.basePrice		
										}
										list.push(obj);
									});
									
									
								    
										// return false;
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + "/global/consumption/packageForCostE";
										BillingGenericService.serviceAction(METHOD_POST,URI, list, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											if(response.data.status=="success"){
												if(response.data.object != "undefined" && response.data.object != null)
													{
													$scope.forServicePayeeId = response.data.object.payeeId;
													$item.basePrice=response.data.object.serviceRate;
													$item.quantity=1;
													$item.coPayPer = response.data.object.coShare==null?0:response.data.object.coShare;
													$item.selfPayable = response.data.object.selfPayable;
													$item.creditPayable = response.data.object.creaditPayable;
													$item.packageConsumptionAmt = response.data.object.packageConsumptionAmt;
													$item.contractId = response.data.object.contractId;
													$item.patientClassPercentage = isNaN(parseFloat(response.data.object.patientClassPercentage)) ? parseFloat(0).toFixed(2) : (parseFloat(response.data.object.patientClassPercentage));
													$item.markupStatus = response.data.object.markupStatus;
													$item.minRateEditable = response.data.object.minRateEditable;
													$item.maxRateEditable = response.data.object.maxRateEditable;
													$item.netPay = response.data.object.finalPrice;
													$item.orderDetailsPayeeMapperDtosList=response.data.object.orderDetailsPayeeMapperDtosList;
													$item.bedCategoryId=response.data.object.billingBedCategoryId;
													$item.isPackage = true;
													$item.packAppDate=new Date();
													$item.tariffId=$scope.optionTariffId !=undefined && $scope.optionTariffId != null ? $scope.optionTariffId : $scope.defaultSelfTariffId
													// for package
													$item.orderDetailsPackageMapperDtosList=response.data.object.orderDetailsPackageMapperDtosList;
													
													$item.payeeId=response.data.object.payeeId;
													
													$scope.selectedPackageList.push($item);
													$scope.servicesListPopupTemp.push($item);
													
													angular.forEach(response.data.listObject, function (val, inx) {
														
														var obj = {};
														obj.basePrice = $item.basePrice;
														obj.basePriceForContract= $item.basePriceForContract;
														obj.billingBedCategoryId= $item.billingBedCategoryId;
														obj.concession = $item.concession;
														obj.creditPayable = $item.creditPayable;
														obj.creditPayableOld = $item.creditPayableOld;
														obj.finalPrice = $item.finalPrice;
														obj.finalPriceOld = $item.finalPriceOld;
														obj.isAppointable = $item.isAppointable;
														obj.isDiscountApplicable= $item.isDiscountApplicable;
														obj.isDoctorRequired= $item.isDoctorRequired;
														obj.isQuantityEditable= $item.isQuantityEditable;
														obj.isRateEditable= $item.isRateEditable;
														obj.isRefDoctorShare= $item.isRefDoctorShare;
														obj.isRenderingSpeciality= $item.isRenderingSpeciality;
														obj.isTaxApplicable= $item.isTaxApplicable;
														obj.quantity= $item.quantity;
														obj.selfPayable= $item.selfPayable;
														obj.selfPayableOld= $item.selfPayableOld;
														obj.serviceMasterId= $item.serviceMasterId;
														obj.serviceStandardCode= $item.serviceStandardCode;
														obj.serviceStandardName= $item.serviceStandardName;
														obj.specialityId= $item.specialityId;
														obj.specialityName= $item.specialityName;
														obj.subSpecialityId= $item.subSpecialityId;
														obj.subSpecialityName= $item.subSpecialityName;
														obj.basePrice=val.serviceRate;
														obj.quantity=$item.quantity;
														obj.selfPayable = (val.selfPayable).toFixed(2);
														obj.creditPayable = (val.creaditPayable).toFixed(2);
														obj.selfPayableOld = (val.selfPayable).toFixed(2);
														obj.creditPayableOld = (val.creaditPayable).toFixed(2);
														obj.contractId = val.contractId;
														obj.orderDetailsPayeeMapperDtosList=val.orderDetailsPayeeMapperDtosList;
														obj.tariffId=$scope.optionTariffId !=undefined && $scope.optionTariffId != null ? $scope.optionTariffId : $scope.defaultSelfTariffId;
														obj.billingBedCategoryId = val.billingBedCategoryId;
														obj.finalPrice = (val.finalPrice).toFixed(2);
														obj.finalPriceOld = (val.finalPrice).toFixed(2);
														obj.packageMasterId = $item.packageMasterId;
														obj.packageTypeId = $item.packageTypeId,
														$scope.optionalServiceList.push(obj);
													});
													
													$scope.packageSearchObj = {packageTypeId : "0", serviceCode:"",serviceName:""}
													}
												else
													{
													growl.warning("Please add base price of this service and then add..!!",{
														title : ERROR_MSG
													});
													return false
													}
											}
										});
										
										
									}
								
								
								$scope.removeSelectedPackage = function(index){
									$scope.selectedPackageList.splice(index,1);
								}

                                $scope.newWhatsAppNumber = '';
                                $scope.openPopupForWhatsApp = function(){
                                    if($scope.selectedPatient.patientId !== undefined && $scope.selectedPatient.patientId !== null){
                                        $scope.newWhatsAppNumber = '+91'+$scope.selectedPatient.mobileNo;
                                        $("#manualWhatsAppConfirmation").modal("show");
                                    }
                                    else{
                                        growl.warning("Please select patient!",
                                        {
                                            title : 'Warning!'
                                        });
                                    }
                                }
                                
                                $scope.newWhatsAppNumberOption = '';
                                $scope.openPopupForWhatsAppOption = function(){
                                    if($scope.selectedPatient.patientId !== undefined && $scope.selectedPatient.patientId !== null){
                                        $scope.newWhatsAppNumberOption = '+91'+$scope.selectedPatient.mobileNo;
                                        $("#manualWhatsAppConfirmationOption").modal("show");
                                    }
                                    else{
                                        growl.warning("Please select patient!",
                                        {
                                            title : 'Warning!'
                                        });
                                    }
                                }

                                $scope.sendWhatsAppManually = function(){

                                    if($scope.newWhatsAppNumber !== undefined && $scope.newWhatsAppNumber !== null && $scope.newWhatsAppNumber !== ""){

                                        if ($scope.newWhatsAppNumber.includes("+")) {
                                            $scope.newWhatsAppNumber =  $scope.newWhatsAppNumber.replace("+","");
                                        }else{
                                               $scope.newWhatsAppNumber =  $scope.newWhatsAppNumber;
                                        }

                                        if($scope.newWhatsAppNumber.length >=10 && $scope.newWhatsAppNumber.length <= 12){
                                            if($scope.newWhatsAppNumber.length === 10){
                                               $scope.newWhatsAppNumber = "91"+$scope.newWhatsAppNumber;
                                            }
                                            else if($scope.newWhatsAppNumber.length === 11){
                                                growl.warning("Enter valid whatsapp number",
                                                {
                                                    title : 'Warning!'
                                                });
                                            }

                                            $scope.saveEstimate();
                                        }
                                        else{
                                            growl.warning("Enter valid whatsapp number",
                                            {
                                                title : 'Warning!'
                                            });
                                        }
                                    }
                                    else{
                                        growl.warning("Enter valid whatsapp number",
                                        {
                                            title : 'Warning!'
                                        });
                                    }
                                };

								$scope.isManuallySend = false;
                                $scope.sendWhatsAppManuallyOption = function(){

                                    if($scope.newWhatsAppNumberOption !== undefined && $scope.newWhatsAppNumberOption !== null && $scope.newWhatsAppNumberOption !== ""){

                                        if ($scope.newWhatsAppNumberOption.includes("+")) {
                                            $scope.newWhatsAppNumberOption =  $scope.newWhatsAppNumberOption.replace("+","");
                                        }else{
                                               $scope.newWhatsAppNumberOption =  $scope.newWhatsAppNumberOption;
                                        }

                                        if($scope.newWhatsAppNumberOption.length >=10 && $scope.newWhatsAppNumberOption.length <= 12){
                                            if($scope.newWhatsAppNumberOption.length === 10){
                                               $scope.newWhatsAppNumberOption = "91"+$scope.newWhatsAppNumberOption;
                                            }
                                            else if($scope.newWhatsAppNumberOption.length === 11){
                                                growl.warning("Enter valid whatsapp number",
                                                {
                                                    title : 'Warning!'
                                                });
                                            }
                                            $scope.isManuallySend = true;
                                            $scope.saveEstimateOption();
                                        }
                                        else{
                                             $scope.isManuallySend = false;
                                            growl.warning("Enter valid whatsapp number",
                                            {
                                                title : 'Warning!'
                                            });
                                        }
                                    }
                                    else{
                                        $scope.isManuallySend = false;
                                        growl.warning("Enter valid whatsapp number",
                                        {
                                            title : 'Warning!'
                                        });
                                    }
                                };
								
								
							
							
						} ]);