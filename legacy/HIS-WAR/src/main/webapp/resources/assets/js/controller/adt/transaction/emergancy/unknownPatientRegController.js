'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:unknownPatientRegController
 * @description #unknownPatientRegController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'unknownPatientRegController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'growl',
						'$state',
						'$stateParams',
						'BillingGenericService',
						'GenericServiceFileUpload',
						'$window',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state,$stateParams,BillingGenericService,GenericServiceFileUpload, $window) {

                            console.log($scope.unknownPatient);
							// for datePicker
							$scope.dateOptionsBirth = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};
							
							$scope.dateOptionsSearch = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};
							
							$scope.openDatePickerSearch = function() {
								$scope.datepickerSearch.opened = true;
							};

							$scope.datepickerSearch = {
								opened : false
							};
							
							$scope.dateOptions = {
								formatYear : 'yyyy',
								showWeeks : false
							};
							
							$scope.openDatePickerDOB = function() {
								// alert("in");
								$scope.datepickerDOB.opened = true;
							};

							$scope.datepickerDOB = {
								opened : false
							};
							
							$scope.openDatePicker = function() {
								// alert("in");
								$scope.datepicker.opened = true;
							};

							$scope.datepicker = {
								opened : false
							};

							$scope.openDatePickerForNewDOA = function() {

								$scope.datepickerForNewDOA.opened = true;
							};

							$scope.datepickerForNewDOA = {
								opened : false
							};
							
							
							// for datePicker1
							$scope.dateOptionsExpiry1 = {
								formatYear : 'yyyy',
								showWeeks : false,
								minDate : new Date()
							};
							
							$scope.openDatePickerExpiry1 = function() {
								$scope.datepickerExpiry1.opened = true;
							};

							$scope.datepickerExpiry1 = {
								opened : false
							};
//							$scope.filepreviewMLC = "";
							 $scope.expiryDateDisabled = true;
							 $scope.enableDisabledValidated=false;
							 $scope.addressFlag =false;
							 $scope.tab =1;
							 $scope.patientRegSuccessFlag=false;

							 //searchPatientCallERData start

							 							$scope.searchPatientDataEr = function(patientsearchObj){
                             								$rootScope.startLoader();
                             								var patSearchObj = angular.copy(patientsearchObj);
                             								if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
                             								{
                             									growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
                             									$scope.patientList = [];
                             									$rootScope.stopLoader();
                             									return false;
                             								}
                             								if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
                             									var dt = new Date($scope.patientSearchObj.birthDate);
                             									patSearchObj.birthDate = (dt.getMonth() + 1) + "-" + dt.getDate() + "-" + dt.getFullYear();
                             								}
                             								var URI = BASE_URL + ROOT_URL
                             								+ PATIENTSERACHBYMULTIPLECRITERIAUNKNOWN;
                             						GenericService
                             								.serviceAction("POST", URI, patSearchObj)
                             								.then(
                             										function(response) {
                             											$scope.patientList = [];

                             											if (response.data.status == "success") {
                             												$scope.patientList = response.data.listObject;
                             												if($scope.patientList.length > 0){
                             												      $scope.patientRegSuccessFlag=true;
                             												      $scope.getPatientDetailsByIndex(0);
                                                                                $(".patienttSearchResult").hide();
                                                                            }
                             												$rootScope.stopLoader();
                             												$scope.patientSearchObj.birthDate="";
                             											}
                             											if(response.data.listObject==null){
                             												growl.warning(response.data.message,{
                             													title : ERROR_MSG
                             												});
                             												$scope.patientSearchObj.birthDate="";
                             												$scope.patientList=[];
                             												$rootScope.stopLoader();
                             												return false;
                             											}
                             										});
                             							}
							 //searchPatientCallERData end

							/* init() function for form object create. */
							$scope.init = function() {
                            if ($stateParams.patientDataERFlow) {
                            /*$scope.patientSearchObj = {
                                    uhidNumber: $stateParams.patientDataERFlow.uhidNumber
                                };*/

                              $scope.patientSearchObj = {
                                        "uhIdNumber"      : $stateParams.patientDataERFlow.uhidNumber,
                                        "patientName"     : "",
                                        "genderId"        : "",
                                        "mobileNo"        : "",
                                        "birthDate"       : "",
                                        "identificationNo":"",
                                        "patientType" : 1,
                                        "unitId"          :$scope.unitId,
                                        "organizationId"  :$scope.organizationId
                                            }
                                $scope.searchPatientDataEr($scope.patientSearchObj);
                                }

                            console.log("Unknown Patient:", $scope.unknownPatient);
								 $scope.tab =3;
								 if($scope.isSetFirstTabLevel(1)){
								     $scope.setSecondTabLevel(1);
								     $scope.tab =1;

								    // $scope.patientCategoryId = cookieObjectAppConfig.defaultPatientCategoryId;
								   // $scope.tab =2;
								 }
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
								 if(cookieObjectAppConfig == undefined){
									 $state.go('applicationConfiguration');
								  return;
								 }
								 else
								  {
								   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
								   
								  }

									$scope.patientObj = {

                                								prefixId : '',
                                								firstName : '',
                                								middleName : '',
                                								lastName : '',
                                								genderId : '',
                                								countryCallingCode : '',
                                								email : '',
                                								nationalityId : '',
                                								raceId : '',
                                								maritalStatusId : '',
                                								mobileNumber : '',
                                								dob : '',
                                								identificationTypeId : '',
                                								identificationNumber : '',
                                								isVip : '',
                                								address : '',
                                								countryId : '',
                                								stateId : '',
                                								districtId : '',
                                								cityId :'',
                                								areaId : '',
                                								zipCode : '',
                                								phoneCode : '',
                                								phoneNumber : '',
                                								companyName : '',
                                								companyAddress : '',
                                								companyCountryId : '',
                                								companyStateId : '',
                                								companyDistrictId : '',
                                								companyCityId : '',
                                								companyAreaId : '',
                                								companyZipCode : '',
                                								companyMobileNumber : '',
                                								organizationId : $scope.organizationId,
                                								unitId : $scope.unitId,
                                								updatedBy : $scope.userId,
                                								createdBy : $scope.userId,
                                								status : 'A',
                                								patientCategoryId:cookieObjectAppConfig.defaultPatientCategoryId,
                                								permanentAddress:'',
                                								permanentCountryId:'',
                                								permanentStateId:'',
                                								permanentDistrictId:'',
                                								permanentCityId:'',
                                								permanentAreaId:'',
                                								permanentZipCode:'',
                                								permanentPhoneCode:'',
                                								permanentPhoneNumber:'',
                                								aliseName:'',
                                								identificationExpiryDate:'',
                                								occupationId:'',
                                								registrationTypeId:1,
                                								previousId:'',
                                								patientId:0,
                                								patientDetailsId:'',
                                								imagePath:'',
                                								thumbImage:'',
                                								isFromER:'Y',
                                								kinList:[]
                                							};

								$scope.patientCategoryId = cookieObjectAppConfig.defaultPatientCategoryId;

								$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
								$scope.minAge = cookieObjectAppConfig.minAge;
								$scope.maxAge = cookieObjectAppConfig.maxAge;
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								$scope.unitCountryId = cookieObject.unitCountryId;
								$scope.unitStateId = cookieObject.unitStateId;
								$scope.unitDistrictId = cookieObject.unitDistrictId;
								$scope.unitCityId = cookieObject.unitCityId;
								$scope.unitAreaId = cookieObject.unitAreaId;
								
								
								
							
								$rootScope.loginpage = true;
								 $scope.numberToDisplay = 10;
								 $scope.numberToDisplayPer = 10;
								
								/*$scope.unknownPatient = {
									isMedicoLegal : 'N'
								}*/
								$scope.initKinDetails = function(){
									  /* Add Next Of Kin Details */
								    $scope.kinDetailsList = [];
								    $scope.kinDetails = {
											prefixId   		: '',
											kinName  		: '',
											relationId 		: '',
											mobileNo 		: '',
											phoneNo 		: '',
											expiry 			: '',
											identificationId: '',
											identificationNo: '',
											isGuarantor 	: 'N',
											address 		: '',
											countryId 		: '',
											stateId 		: '',
											districtId 		: '',
											cityId 			: '',
											areaId 			: '',
											postCode 		: '',
											patientId       : '',
											unitId			: $scope.unitId,
											organizationId  : $scope.organizationId,
											createdBy		: $scope.userId,
											updatedBy		: $scope.userId,
											createdDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											updatedDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
											isExpiry		:'N'
										};
									
									$scope.kinDetailsList.push(angular.copy($scope.kinDetails));
								}
								$scope.initKinDetails();
								 $scope.patobj={
										 	filepreviewMLC:"",
								    		patientImage1:""
								    }
								$rootScope.readOnlyFlag = false;
								$scope.patientList = [];
								$scope.patientSearchObj = {
										"uhIdNumber"      : "",
										"patientName"     : "",
										"genderId"        : "",
										"mobileNo"        : "",
										"birthDate"       : "",
										"identificationNo":"",
										"patientType" : 1,
										"unitId"          :$scope.unitId,
										"organizationId"  :$scope.organizationId
								}
								
								$rootScope.unknownPatient = {
										"patientName" : "Unknown Patient",
										"genderCode"  : "",
										"age"         : "", 
										"uhIdNumber"  : "",
										"genderId"    : "",
										"mobileNo"    : "",
										"ageFormat" : 'Y',
										"isMedicoLegal" : 'N',
										"patientId":0,
										"tPatientId":0,
										"remark":"",
										"modeOfArrival":"",
										"priorityId":0,
										"accompaniedBy":"",
										"kinDetailsId":"",
								}
								$rootScope.patientLable = {
										"patientName" : "",
										"genderCode"  : "",
										"age"         : "", 
										"uhIdNumber"  : "",
										"birthDate"  : ""
								}
								
								
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
								$scope.appointmentMasterObj = {
										paymentEntitlementTypeId : 1
								};
								
								
                                $scope.itemSelecteds = {};
								
								
								$scope.initCoEmp=function(){
									$scope.corEmpObj={
											typeOfCoEmp:'3',
//											employeeNumber:"",
											note:"",
											employeeName:"",
											relation:"",
											medicalCardNo:""
												
									}
								}
								$scope.initCoEmp();
								$scope.initEmpObj=function(){
									$scope.empObj={
											employeeNumber:""
												
									}
								}
								$scope.initEmpObj();
								
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
											'companyId':$scope.selfPayeeId,
											'tariffId':$scope.defaultSelfTariffId,
											'contractId':null,
											'typeId' : 1,
											'insuredAmount' : '',
											'billingBedCategoryId':null
										}];
								
								var data = {};

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEGENDERLIST;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.genderList = [];

													if (response.data.status == "success") {
														$scope.genderList = response.data.listObject;

													}
												});
								

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
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEPRIORITYLIST;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.priorityListForMLC = [];

													if (response.data.status == "success") {
														$scope.priorityListForMLC = response.data.listObject;

													}
												});

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								$scope.sameAsLocal = 'N';
								$scope.addressFlag =false;
								//$scope.firstTabLevel = 1;

							}

							$scope.init();
							
							
						    $scope.initAreaMasterList = function(offset, noOfRecordsPerPage)
							{
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
												var data = {
														offset:offset,
														noOfRecordsPerPage:noOfRecordsPerPage,
													    cityId : $scope.idd
												};
												var URI = BASE_URL + ROOT_URL
														+  "/adt/getAreaByCityIdPagination";
												BillingGenericService
												.serviceAction(METHOD_POST, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															$scope.areaList = [];
															if (response.data.status == "success")
															{
																 $scope.logEvents = [];
																 $scope.logEvents = response.data.listObject;
																
															}
														});

							}	
						    
						    $scope.initPermanenetAreaMasterList = function(offset, noOfRecordsPerPage)
							{
								$rootScope.startLoader();
								offset = offset != null ? offset : 0;
								noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
												var data = {
														offset:offset,
														noOfRecordsPerPage:noOfRecordsPerPage,
													    cityId : $scope.patientObj.permanentCityId,
												};
												var URI = BASE_URL + ROOT_URL
														+  "/adt/getAreaByCityIdPagination";
												BillingGenericService
												.serviceAction(METHOD_POST, URI, data,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(
														function(response) {
															$scope.permanentAreaList = [];
															if (response.data.status == "success")
															{
																$scope.logEventsPer = [];
																 $scope.logEventsPer = response.data.listObject;
																
															}
														});

							}	
							
							 $scope.loadMore = function()
						       {
						    	   $scope.numberToDisplay += 5;
						           $scope.initAreaMasterList($scope.offset,$scope.numberToDisplay);
						       }
							 
							 $scope.loadMore1 = function()
						       {
						    	   $scope.numberToDisplayPer += 5;
						           $scope.initPermanenetAreaMasterList($scope.offset,$scope.numberToDisplayPer);
						       }

						        $scope.searchAreaByCityIdPermanent = function(searchKeyword){

                                            $scope.areaList=[];
                                            var serData = {
                                                    cityId : $scope.patientObj.permanentCityId,
                                                    searchKeyword : searchKeyword
                                            }
                                            var URI = BASE_URL + ROOT_URL + "/global/serviceAreaBysearchKeyword";
                                            return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
                                                if(response.data.status == "success"){
                                                    $scope.permanentAreaList = response.data.listObject;
                                                    if($scope.permanentAreaList.length == 0){

                                                        $scope.employeeObj.permanentAreaId=null;

                                                        growl.warning("No record found for serarch crateria please add new area",
                                                                {
                                                                    title : 'warning!'
                                                                });
                                                    }
                                                }
                                                return $scope.permanentAreaList;
                                            });
                               	  }
							 
							 $scope.applySize = function()
						       {
						    	   $('#selRegsPer').focus(function () {
						    	 	    $('#selRegsPer').attr("size", "4");
						    	 	});
						    		
						    	 	$('#selRegsPer').change(function () {
						    	 	    $('#selRegsPer').attr("size", "1");
						    	 	    $("#selRegsPer").blur();
					    	 	});						       
						    	 	}
							 
							 
							 $scope.applySizeKin = function()
						       {
						    	   $('#selRegskin').focus(function () {
						    	 	    $('#selRegskin').attr("size", "4");
						    	 	});
						    		
						    	 	$('#selRegskin').change(function () {
						    	 	    $('#selRegskin').attr("size", "1");
						    	 	    $("#selRegskin").blur();
					    	 	});						       
						    	 	}
							 
							$scope.loadingMsg="";
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
							$scope.todaysDate=moment(new Date()).format('DD/MM/YYYY');
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
									if($scope.corEmpVerificationFlag){
										$scope.enableDisabledValidated=false;
										$scope.enableDisabledApply=true;	
									}else{
										$scope.enableDisabledValidated=false;
										$scope.enableDisabledApply=true;
									}
								});
								
								var len = $scope.priorityList.length;
								 $scope.priorityList.splice(len-1,1);
								 
								$scope.paymentEntitleMentList = newDataList;
								
								$scope.allDocumentFinalListTemp=[];
								$scope.allDocumentFinalListTemp=$scope.allDocumentFinalList;
									if($scope.allDocumentFinalListTemp!=undefined && $scope.allDocumentFinalListTemp.length>0){
										$scope.allDocumentFinalList=[];
										angular.forEach(newDataList, function(v) {											
										
												angular.forEach($scope.allDocumentFinalListTemp, function(v1) {												
													if(v.paymentEntitlementId==v1.paymentEntitlementId){
														$scope.allDocumentFinalList.push(v1);
													}
													
												});
											
											
										});
									}
									console.log("final list after removing",$scope.allDocumentFinalList);
								}
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
							
							$scope.getInsuranceContract = function(insuranceItem)
							{
									var companyTypeId = $scope.appointmentMasterObj.paymentEntitlementTypeId;
									var companyId = insuranceItem.companyId;
									var asscoiatedCompanyId = insuranceItem.associateCompanyId;
									var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,associateCompanyId:asscoiatedCompanyId,visitTypeId:4};
									
									$scope.assoContractList = [];
									$scope.paymentEntitleMentInsurance[0].contractId = '';
								    var URI = BASE_URL + ROOT_URL + "/unit/CompanyContract/getContractByCompanyId"
								    $rootScope.startLoader();
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
										}$rootScope.stopLoader();
									});
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
								
								var objTmp = angular.copy($scope.paymentEntitleMentInsurance[0]);
								 objTmp.multidelete("insuranceNumber","datepickerInss","datepickerInsE","insuredAmt");
									 var isEmpty = !(Object.values(objTmp).every(x =>(x != null &&  x != '')));
									 if(isEmpty)
										 {
										 if($scope.paymentEntitleMentInsurance[0].companyId == null || $scope.paymentEntitleMentInsurance[0].companyId == '' || $scope.paymentEntitleMentInsurance[0].companyId == undefined){
											 growl.warning("Please select company.",{
													title : "WARNING"
												});
												return; 
										 }
										 
										 if($scope.paymentEntitleMentInsurance[0].associateCompanyId == null || $scope.paymentEntitleMentInsurance[0].associateCompanyId == '' || $scope.paymentEntitleMentInsurance[0].associateCompanyId == undefined){
											 growl.warning("Please select associate company.",{
													title : "WARNING"
												});
												return; 
										 }
										 
										 if($scope.paymentEntitleMentInsurance[0].contractId == null || $scope.paymentEntitleMentInsurance[0].contractId == '' || $scope.paymentEntitleMentInsurance[0].contractId == undefined){
											 growl.warning("Please select contract.",{
													title : "WARNING"
												});
												return; 
										 }
										 
										 
										 if($scope.paymentEntitleMentInsurance[0].insuranceNumber == null || $scope.paymentEntitleMentInsurance[0].insuranceNumber == '' || $scope.paymentEntitleMentInsurance[0].insuranceNumber == undefined){
											 growl.warning("Please enter insurance number.",{
													title : "WARNING"
												});
												return; 
										 }
										 
										 if($scope.paymentEntitleMentInsurance[0].datepickerInss == null || $scope.paymentEntitleMentInsurance[0].datepickerInss == '' || $scope.paymentEntitleMentInsurance[0].datepickerInss == undefined){
											 growl.warning("Please select start date.",{
													title : "WARNING"
												});
												return; 
										 }
										 
										 if($scope.paymentEntitleMentInsurance[0].datepickerInsE == null || $scope.paymentEntitleMentInsurance[0].datepickerInsE == '' || $scope.paymentEntitleMentInsurance[0].datepickerInsE == undefined){
											 growl.warning("Please select end date.",{
													title : "WARNING"
												});
												return; 
										 }
										 
										 if($scope.paymentEntitleMentInsurance[0].insuredAmt == null || $scope.paymentEntitleMentInsurance[0].insuredAmt == '' || $scope.paymentEntitleMentInsurance[0].insuredAmt == undefined){
											 growl.warning("Please enter amount.",{
													title : "WARNING"
												});
												return; 
										 }
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
									  var isAfter = true;
							
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
								 // newArr.tariffName = tarrifName;
								  newArr.contractName = insuranceObj.contractName;
								  newArr.insuranceName = associatedName;
								  newArr.insuranceId = insuranceId;
								  newArr.insuranceNumber = $scope.paymentEntitleMentInsurance[0].insuranceNumber;
								 // newArr.startDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;
								 newArr.startDate = $scope.paymentEntitleMentInsurance[0].datepickerInss != null ?moment($scope.paymentEntitleMentInsurance[0].datepickerInss).format('DD-MM-YYYY HH:mm:ss'): null;//contractObj.validityFrom;
								  newArr.endDate = $scope.paymentEntitleMentInsurance[0].datepickerInsE != null ? moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss'): null;//contractObj.validityFrom;datepickerInsE/ newArr.endDate = moment($scope.paymentEntitleMentInsurance[0].datepickerInsE).format('DD-MM-YYYY HH:mm:ss');//contractObj.validityFrom;datepickerInsE
								  newArr.priority = "";
								 // newArr.tariffId = $scope.paymentEntitleMentInsurance[0].tariffId;
								  newArr.contractId = insuranceObj.contractId;
								  newArr.typeId = 2;
								  newArr.insuredAmount = $scope.paymentEntitleMentInsurance[0].insuredAmt;
								  newArr.billingBedCategoryId = insuranceObj.billingBedCategoryId;
								  newArr.claimNumber = $scope.paymentEntitleMentInsurance[0].claimNumber;
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
							};
							
							
							 $scope.addImageDetailsForContract = function(obj)
								{ 
									var encounterDetails = angular.copy($scope.encounterDetailsContract);
									encounterDetails.patientId = $scope.patientDetails.patientId;
									//insurance
									if( obj.paymentEntitlementId==2){
										encounterDetails.documentTypeId = '30';	
										encounterDetails.paymentEntitlementId =obj.paymentEntitlementId;	
										
									}else if(obj.paymentEntitlementId==3){
										encounterDetails.documentTypeId = '19';	
										encounterDetails.paymentEntitlementId =obj.paymentEntitlementId;	
									}else{
										encounterDetails.paymentEntitlementId =obj.paymentEntitlementId;	
									}
									
									$scope.encounterDetailsContractList.push(encounterDetails);
								};

							  $scope.removeImageDetailsContract = function(deleteIndex)
								{ 
								   $scope.encounterDetailsContractList.splice(deleteIndex, 1);
							    };
							
							
							    $scope.openDocument = function(obj)
								{

								  $('#fileId').val(undefined);
									$scope.encounterDetailsContract = {
											  encounterDocId : "",
											  encounterId : 0,
											  admissionId:0,
											  visitTypeId:4,
											  patientId : "",
											  documentPath : "",
											  documentTypeId : '',
											  status : 'A',
											  createdDate : "",
											  createdBy : $scope.userId,
											  updatedDate : "",
											  updatedBy : $scope.userId,
											  unitId : $scope.unitId,
											  organisationId : $scope.organizationId,
											  file : {}
											}
								
										if($scope.unknownPatient.patientId==undefined || $scope.unknownPatient.patientId==null || $scope.unknownPatient.patientId==''){
											/*growl.warning(
													'Please select atleast one patient!!!.',
													{
														title : 'warning!'
													});
												$scope.encounterDetailsContractList =[];
												return;*/
											
											//$scope.tab=1;
											$scope.secondTabLevel = 1 ;
											/*growl.warning("Please Select Patient",{
												title : WARNING_MSG
											});
											return;*/
											
											$scope.$broadcast('show-errors-check-validity');
											// $scope.secondTabLevel=1;
									        if($scope.patientRegistrationform.$valid){
									        	//alert("valid");
									        	
									        	var contryCode=$scope.patientObj.countryCallingCode;
									        	 // validating country code
												 var regEx = /^[+-]?\d+$/;
												      if(!regEx.test(contryCode)){
												    	  growl.warning("Please enter valid country calling code",{
																title : 'Warning !'
																});
																return false;
												      } 
									        	
									        	var mobileNo=$scope.patientObj.mobileNumber;
									        	if(contryCode==91){
									        		if(mobileNo.length==10){
									        			
														}else{
															growl.warning("Please enter valid mobile number",{
																title :'Warning !'
																});
																$('#mobileNo').focus();
																return false;
									        		}
									        	}else{
										        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
										        			
														}else{
															growl.warning("Please enter valid mobile number",{
																title : 'Warning !'
																});
																$('#mobileNo').focus();
																return false;
									        		}
									        	}


									        	if($scope.patientObj.patientCategoryId==null || $scope.patientObj.patientCategoryId==undefined||$scope.patientObj.patientCategoryId==""){
									        		growl.warning("Please select patient category",{
																title : 'Warning !'
																});
																
																return false;
									        	}
												/*if($scope.patientRegistrationform2.$valid){*/
									        		 if($scope.patientObj.phoneCode !='' && $scope.patientObj.phoneCode!=undefined && $scope.patientObj.phoneCode!=null){
									        			 var phoneCode=$scope.patientObj.phoneCode;
											        	 // validating country code
														 var regEx = /^[+-]?\d+$/;
														      if(!regEx.test(phoneCode)){
														    	  growl.warning("Please enter valid country calling code",{
																		title : 'Warning !'
																		});
																		return false;
														      } 
									        		 }
									        		 //test start
									        		 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
								       			 }
									        		 if($scope.patientObj.permanentZipCode !='' && $scope.patientObj.permanentZipCode!=undefined && $scope.patientObj.permanentZipCode!=null){
								       			 }
									        		 if($scope.patientObj.permanentPhoneCode !='' && $scope.patientObj.permanentPhoneCode!=undefined && $scope.patientObj.permanentPhoneCode!=null){
									        			 var permanentPhoneCode=$scope.patientObj.permanentPhoneCode;
											        	 // validating country code
														 var regEx = /^[+-]?\d+$/;
														      if(!regEx.test(permanentPhoneCode)){
														    	  growl.warning("Please enter valid country calling code",{
																		title : 'Warning !'
																		});
																		return false;
														      } 
									        		 }
									        		 if($scope.patientObj.email == null){
									        			 $scope.patientObj.email = "";
									        		 }
									        		 if($scope.patientObj.email != "")
									           			{
										           		 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
										           		 var email = $scope.patientObj.email;
										           		    if (!filter.test(email)) {

									        				    growl.error(EMAIL_MSG,{
																	title : ERROR_MSG

																});
									        				    $('#email').focus();
									        				    return false;
										           		    }
										           		    else{
										           		    	if($scope.patientObj.phoneNumber == undefined)
										           		    			$scope.patientObj.phoneNumber='';
										           		    	if($scope.patientObj.phoneNumber == null)
										           		    			$scope.patientObj.phoneNumber='';
										           		     if($scope.patientObj.phoneNumber != '')
												        		{
												        		}
										           		     else
										           		    	 {
										           		    	 }
										           		    }
									           			}
									        		 else
									        			 {
									        			 if($scope.patientObj.phoneNumber == undefined)
									        				 $scope.patientObj.phoneNumber='';
									        			 if($scope.patientObj.zipCode !='' && $scope.patientObj.zipCode!=undefined && $scope.patientObj.zipCode!=null){
									        			 }
									        			 }
									        		
									        		 //test end
									        			 
									        }else{
									        	growl.warning(" Please select patient or add patient details..!! ",{
													title : WARNING_MSG
												});
									        	return false;
									        }
									        
										}
									
									/*	$scope.encounterDetailsContractList =[];
										var encounterDetails = angular.copy($scope.encounterDetailsContract);
										encounterDetails.patientId = $scope.patientDetails.patientId;	
										$scope.encounterDetailsContractList.push(encounterDetails);
										 $scope.getPatientDocumentList();*/
										 
										 
										 
										 if(obj.paymentEntitlementId==1){			
												
											 $scope.encounterDetailsContractList =[];
												var encounterDetails = angular.copy($scope.encounterDetailsContract);
												encounterDetails.patientId = $scope.unknownPatient.patientId;
												encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
												$scope.encounterDetailsContractList.push(encounterDetails);	
												$scope.getPatientDocumentList(encounterDetails);
												$('#documentUploadModalForAdmission').modal('show');
											}else if(obj.paymentEntitlementId==2){
												
												$scope.encounterDetailsContractList =[];
												var encounterDetails = angular.copy($scope.encounterDetailsContract);
												encounterDetails.documentTypeId = '30';	
												encounterDetails.patientId = $scope.unknownPatient.patientId;
												encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
												$scope.encounterDetailsContractList.push(encounterDetails);
												 $scope.getPatientDocumentList(encounterDetails);
												 $('#documentUploadModalForAdmission').modal('show');
												
											}else if(obj.paymentEntitlementId==3){
												$scope.encounterDetailsContractList =[];
												var encounterDetails = angular.copy($scope.encounterDetailsContract);
												encounterDetails.documentTypeId = '19';	
												encounterDetails.patientId = $scope.unknownPatient.patientId;	
												encounterDetails.paymentEntitlementId = obj.paymentEntitlementId;
												$scope.encounterDetailsContractList.push(encounterDetails);
												 $scope.getPatientDocumentList(encounterDetails);		
												$('#documentUploadModalForAdmission').modal('show');
											}
									
								}
							  
							  
							  $scope.uploadEncounterContractDoc = function(Obj)
								{
									 var fd = new FormData();
								        fd.append('encounterId', Obj.admissionId);
								        fd.append('visitTypeId', Obj.visitTypeId);
								        fd.append('admissionId', Obj.admissionId);
								        fd.append('patientId',Obj.patientId );
								        fd.append('documentTypeId', Obj.documentTypeId);
								        fd.append('createdBy',Obj.createdBy );
								        fd.append('updatedBy', Obj.updatedBy);
								        fd.append('unitId', Obj.unitId);
								        fd.append('organisationId', Obj.organisationId);
								        fd.append('file',Obj.file );
								        fd.append('expenseId', 0);
								        fd.append('grnId',0);
										fd.append('poId',0);
								        fd.append('incidentReportingMapperId',0);
								        fd.append('companyExpenseId',0);
								        fd.append('createdDate',null);
										fd.append('fileEditedName',null);
										fd.append('opbId',0);
										fd.append('vendorId',0);
								        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
								        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
										{	
											return response;
										});
								}
								 $scope.uploadContractDoc = function()
								    {
									  $scope.countFrValidation = 0;
									  if($scope.encounterDetailsContractList != undefined && $scope.encounterDetailsContractList.length != 0){
									 angular.forEach($scope.encounterDetailsContractList, function(value, key) {  
										  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
												 growl.warning("Please select document type .",
																			{
																				title : 'warning!'
																			});
																   $rootScope.stopLoader();
												return false;
											}
//										  if (value.file == undefined || value.file == "" || value.file == null ) {
										  if (angular.equals(value.file, {}) || value.file==undefined || value.file==null) {
													growl.warning('Please select file.',
																	{
																		title : 'warning!'
																	});
													 $rootScope.stopLoader();
													return false;
												} 
											 var ext = value.file.name.split(".").pop().toLowerCase();
												if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
													growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
															{
																title : 'warning!'
															});
													$rootScope.stopLoader();
													return false;
												}
												$scope.countFrValidation++;
									});
								    }
									  if($scope.encounterDetailsContractList != undefined){
										  if($scope.encounterDetailsContractList.length!=$scope.countFrValidation){
												 return false;
											 }
									  }
									 $rootScope.startLoader();
									 if($scope.unknownPatient.patientId > 0 || ($scope.encounterDetailsContractList !=null && $scope.encounterDetailsContractList.length>0 && $scope.encounterDetailsContractList[0].patientId>0)){
									  $scope.count = 0;
									 angular.forEach($scope.encounterDetailsContractList, function(value, key) 
										{  
										 $('#documentUploadModalForAdmission').modal('hide');
											var promise = $scope.uploadEncounterContractDoc(value);
											promise.then(function(response) 
													{
													  if(response.data.status == "success")
														  {
														    $scope.count++;
														    if($scope.encounterDetailsContractList.length==$scope.count)
															 {
															   growl.success("Document Uploaded Successfully.",
																		{
																			title : 'Success!'
																		});
															   $rootScope.stopLoader();
															   $scope.encounterDetailsContractList = [];
															   $scope.getPatientDocumentList();
															   
															 }
													      }
													  else 
														{
															growl.error('Something went wrong.',
																	{
																		title : 'Error!'
																	});
															 $rootScope.stopLoader();
														}
												    });
												
										});
								    }else{
								    	$rootScope.stopLoader();
								    	 $('#documentUploadModalForAdmission').modal('hide');
								    	 
								    }
									
								    }
								 $scope.removeDoc=function(index){
									 $scope.allDocumentFinalList.splice(index, 1);
								 }
								 
								 
									$scope.getDocumentType = function(){
										$rootScope.startLoader();
										var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
										BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												$scope.documentTypeList = [];
												if (response.data.status == "success") {
													$scope.documentTypeList = response.data.listObject;
													 angular.forEach($scope.documentTypeList, function(value, key) 
													 {  
											 			$scope.corporateDocTypeId=value.documentTypeId;
													 });
												}
											});
									}
									$scope.getDocumentType();
								 
								 
									$scope.getPatientDocumentList = function(obj)
								     {
								      
								      if($scope.unknownPatient.patientId > 0){

								    	  $rootScope.startLoader();
								       var data =
								        {
								        "patientId":$scope.unknownPatient.patientId,
								        "unitId":$scope.unitId,
								        "organisationId":$scope.organizationId,
								        "userId":$scope.userId,
								        "visitTypeId":1,
								        "admissionId":0,
								       }
								      var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
								      BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								        $scope.patientDocumentList = [];
								        if (response.data.status == "success") 
								        {
//								        	$scope.patientDocumentList = response.data.listObject;  
								            $scope.patientAllDocumentList = response.data.listObject;
								            $scope.patientDocumentList=[];
								            angular.forEach($scope.patientAllDocumentList, function(value, key) 
													{  
								            if(obj.paymentEntitlementId==1){
								            	if(value.documentTypeId !=30 && value.documentTypeId!=19){				            				
								         			
								         			var data={
								         					documentTypeId:value.documentTypeId,
								         					documentName:value.documentName,
								         					documentPath:value.documentPath,
								         					createdDate:value.createdDate
								         			}
								         			$scope.patientDocumentList.push(data);
								     			}
								            }else if(obj.paymentEntitlementId==2){
								            	if(value.documentTypeId==30){				            				
								         			
								         			var data={
								         					documentTypeId:value.documentTypeId,
								         					documentName:value.documentName,
								         					documentPath:value.documentPath,
								         					createdDate:value.createdDate
								         			}
								         			$scope.patientDocumentList.push(data);
								     			}
								            }else if(obj.paymentEntitlementId==3){
								            		if(value.documentTypeId==19){				            				
								         			
								         			var data={
								         					documentTypeId:value.documentTypeId,
								         					documentName:value.documentName,
								         					documentPath:value.documentPath,
								         					createdDate:value.createdDate
								         			}
								         			$scope.patientDocumentList.push(data);
								     			}
								            }else{
								            	
								            }	
								 			
									});
								            $rootScope.stopLoader();
								        }
								        else if(response.data.status == "error")
								         {
								        	$rootScope.stopLoader();
								         }
								       });
								     }
								     }
							 
							 
							 $scope.setPostCodeForKin = function(areaId,index){
									 angular.forEach($scope.areaList,function(value2,key){
											
											if(value2.areaId==areaId)
											{
												$scope.kinDetailsList[index].postCode = value2.postCode;
											}
										});
								}
							 
							 $scope.priorityList = [{
									id:1,
									value:1
								}]

							$scope.patientsearchObj = {
							    	  "patientName" :"",
										"uhIdNumber" : "",
										"birthDate":"",
										"encounterId":"",
										"genderId":"",
										"roleId" :"",
										"patientId":"",
										"doctorId":"",
										"doctorName":"",
										"genderId":"",
										"mobileNo":"",
										"identificationNo":"",
										"patientType"   :"1",
										"unitId": 1, 
										"visitTypeId" :4,
										"organizationId"  :1
							    }
						
							

							$scope.searchPatient = function(patientsearchObj){
								$rootScope.startLoader();
								var patSearchObj = angular.copy(patientsearchObj);
								if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
								{
									growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
									$scope.patientList = [];
									$rootScope.stopLoader();
									return false;
								}	
								if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
									var dt = new Date($scope.patientSearchObj.birthDate);
									patSearchObj.birthDate = (dt.getMonth() + 1) + "-" + dt.getDate() + "-" + dt.getFullYear();
								} 
								var URI = BASE_URL + ROOT_URL
								+ PATIENTSERACHBYMULTIPLECRITERIAUNKNOWN;
						GenericService
								.serviceAction("POST", URI, patSearchObj)
								.then(
										function(response) {
											$scope.patientList = [];

											if (response.data.status == "success") {
												$scope.patientList = response.data.listObject;
												$rootScope.stopLoader();
												$scope.patientSearchObj.birthDate="";
											}
											if(response.data.listObject==null){
												growl.warning(response.data.message,{
													title : ERROR_MSG
												});
												$scope.patientSearchObj.birthDate="";
												$scope.patientList=[];
												$rootScope.stopLoader();
												return false;
											}
										});
							}
							/*
							$scope.secondTabLevel = 1;
							$scope.firstTabLevel = 1;
							$scope.setFirstTabLevel = function(newTab) {
								$scope.firstTabLevel = newTab;
								$scope.setFirstLevelTab(newTab);
							}
							$scope.setFirstLevelTab = function(newTab) {
								switch(newTab) 
								{
									case 10 : $scope.currentTab = "complaints"
										
									break;
									case 5 : $scope.currentTab = "vitals"
										     
										  break;
									case 9 : $scope.currentTab = "investigation"
										break;
								}
							}
						
							$scope.setSecondTabLevel = function(tabNum) {
								return $scope.secondTabLevel === tabNum;
							};*/
							// $scope.tab=1;
							$scope.setTbah = function(tab) {
								if(tab==2){
									$scope.tab=3;
									$scope.patientList = [];
									$scope.init();
									$scope.kinsList=[];
								}else{
									$scope.tab=1;

								}

							};
							
							$scope.getPatientDetailsByIndex = function(index)
							{
								
								if($scope.patientList[index].isDeath =='Y')
								{
							 		growl.warning("Patient is marked as death",{
										title : WARNING_MSG
									});
							 		$scope.patientList = [];
							 		return false;
								}
								
								var message="";
								if($scope.patientList[index].admissionId != null && $scope.patientList[index].visitTypeId == 2){
									
									if($scope.unitId==$scope.patientList[index].admissionUnitId)
					            		message = "Patient is already admitted in IP...!!";
					            	else
					            		message = "Patient is already admitted in another unit...!!";
									
									growl.warning(message,{
										title : WARNING_MSG
									});
									$scope.patientList = [];
									return false;
								}
								
								if($scope.patientList[index].admissionId != null && $scope.patientList[index].visitTypeId == 3){
									if($scope.unitId==$scope.patientList[index].admissionUnitId)
					            		message = "Patient is already admitted in Day Care...!!";
					            	else
					            		message = "Patient is already admitted in another unit...!!";
									
									growl.warning(message,{
										title : WARNING_MSG
									});
									$scope.patientList = [];
									return false;
								}
								
								if($scope.patientList[index].admissionId != null && $scope.patientList[index].visitTypeId == 4){
									if($scope.patientRegSuccessFlag==false){
									if($scope.unitId==$scope.patientList[index].admissionUnitId)
                                        message = "Patient is already admitted in ER...!!";
                                    else
                                        message = "Patient is already admitted in another unit...!!";

                                    growl.warning(message,{
                                        title : WARNING_MSG
                                    });
									}
									$scope.patientList = [];
									$scope.patientRegSuccessFlag=false;
									return false;
								}
								$scope.tab=1;
								$scope.patientDetails = {};
								$rootScope.readOnlyFlag = true;
								if($scope.patientList[index].isAbsconded==undefined || $scope.patientList[index].isAbsconded==null || $scope.patientList[index].isAbsconded=='N'){
								$rootScope.patientLable.age=($scope.patientList[index].age);
								$rootScope.patientLable.patientName=($scope.patientList[index].patientName);
								$rootScope.patientLable.genderCode=($scope.patientList[index].genderCode);
								$rootScope.patientLable.uhIdNumber=($scope.patientList[index].uhIdNumber);
								$rootScope.patientLable.birthDate=($scope.patientList[index].birthDate);
								
								$rootScope.unknownPatient.age=($scope.patientList[index].age);
								$rootScope.unknownPatient.patientName=($scope.patientList[index].patientName);
								$rootScope.unknownPatient.genderCode=($scope.patientList[index].genderCode);
								$rootScope.unknownPatient.uhIdNumber=($scope.patientList[index].uhIdNumber);
								$rootScope.unknownPatient.genderId=($scope.patientList[index].genderId).toString();
								$rootScope.unknownPatient.mobileNo=($scope.patientList[index].mobileNo);
								$rootScope.unknownPatient.patientType=($scope.patientList[index].patientType);
								$rootScope.unknownPatient.patientId=($scope.patientList[index].patientId);
								$rootScope.unknownPatient.admissionId=($scope.patientList[index].admissionId);
								$rootScope.unknownPatient.legacyVisitId=($scope.patientList[index].legacyVisitId);
								$rootScope.unknownPatient.priorityId = $stateParams.patientDataERFlow.priorityId !== undefined || $stateParams.patientDataERFlow.priorityId !== null ? $stateParams.patientDataERFlow.priorityId : null;
								$rootScope.unknownPatient.modeOfArrival = $stateParams.patientDataERFlow.modeOfArrival !== undefined || $stateParams.patientDataERFlow.modeOfArrival !== null ? $stateParams.patientDataERFlow.modeOfArrival : '';

								$scope.patientDetails.patientName=($scope.patientList[index].patientName);
								$scope.getKinDetailsByPatientId();
								
								if(($scope.patientList[index].prefixId)> 0){
									$scope.unknownPatient.prefixId = ($scope.patientList[index].prefixId).toString();
									$scope.patientObj.prefixId = ($scope.patientList[index].prefixId);
							 }
								if($scope.patientList[index].patientType == 4)
									{
										$rootScope.unknownPatient.ageFormat = $scope.patientList[index].age.slice($scope.patientList[index].age.length-1);
										$rootScope.unknownPatient.age = $scope.patientList[index].age.slice(0,-1);
										
									}
								else
									{
									 var array = $scope.patientList[index].age.split(' ');
									 $rootScope.unknownPatient.ageFormat = array[0].slice( array[0].length-1);
										$rootScope.unknownPatient.age =  array[0].slice(0,-1);
									
									}
								}else{
									    $scope.abscondIndex=index;
										$scope.abscondPatientFlag='Y';
										angular.element('#abscondedPatientPopUp').modal('show');
								}
								console.log($rootScope.unknownPatient);


                                 console.log($scope.patientList[index])
                                  var dateString = $scope.patientList[index].birthDate;

                                 if (!dateString) return;

                                 var parts = dateString.split("/");

                                 var day = parseInt(parts[0], 10);
                                 var month = parseInt(parts[1], 10) - 1;
                                 var year = parseInt(parts[2], 10);
                                 var ageFromIndex = $scope.patientList[index].age !== undefined && $scope.patientList[index].age !== '' ? $scope.patientList[index].age.replace(/Y/g, ' ') : 0;
                                 $scope.patientObj.firstName=($scope.patientList[index].firstName)
                                 $scope.patientObj.middleName=($scope.patientList[index].middleName)
                                 $scope.patientObj.lastName=($scope.patientList[index].lastName)
								$scope.patientObj.mobileNumber=($scope.patientList[index].mobileNo);
								$scope.datepicker = new Date(year, month, day);
								$scope.age = parseInt(ageFromIndex, 10) || 0;
								$scope.patientObj.genderId=($scope.patientList[index].genderId);
								$scope.patientObj.address=($scope.patientList[index].address);
								$scope.patientObj.permanentAddress=$scope.patientList[index].permanentAddress !== undefined ? $scope.patientList[index].permanentAddress : '';
								$scope.patientObj.areaId= $scope.patientList[index].areaId !== undefined ? $scope.patientList[index].areaId : 1;
								var array = $scope.patientList.age.split(' ');
								$scope.age=array;
								$rootScope.unknownPatient.genderId=($scope.patientList[index].genderId).toString();
								$scope.patientObj.mobileNumber=($scope.patientList[index].mobileNo);
								$scope.patientObj.legacyVisitId=($scope.patientList[index].legacyVisitId);


                                if(($scope.patientList[index].prefixId)> 0){
                                            $scope.patientObj.prefixId = ($scope.patientList[index].prefixId).toString();
                                            $scope.patientObj.prefixId = ($scope.patientList[index].prefixId);
                                	}
                                	if($scope.patientList[index].patientType == 4)
                                            {
                                                $scope.age = $scope.patientList[index].age.slice($scope.patientList[index].age.length-1);
                                                $scope.age = $scope.patientList[index].age.slice(0,-1);

                                            }


								
								
								if($scope.unknownPatient.patientId > 0){
									var URI = BASE_URL + ROOT_URL + "/api/opd/lastSponsors/"+$scope.unknownPatient.patientId+"/4/"+$scope.unitId;
									BillingGenericService
											.serviceAction(METHOD_GET, URI, {},
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														if (response.data.status == "success") {
															$scope.forCheckingPatientClassAndEntitlement =[];
															$scope.forCheckingPatientClassAndEntitlement = response.data.listObject;
															if( response.data.object > 0){
																$scope.admissionRequestOrAdmissionObj.patientClassId = response.data.object;
															}
															if($scope.forCheckingPatientClassAndEntitlement != null ){
																$scope.paymentEntitleMentList = [];
																 $scope.priorityList = [];
																 var i = 1;
																 angular.forEach($scope.forCheckingPatientClassAndEntitlement,function(value,key)
																			{
																		  		var newArr = {};
																		  		newArr.paymentEntitlementId =value.paymentEntitlementId;
																		  		
																		  	   newArr.contractName = value.contractName;
																		  		newArr.insuranceName = value.insuranceDesc;
																		  		      newArr.insuranceNumber = value.insuranceNumber;
																		  		      newArr.startDate = value.validityFrom;
																		  		      newArr.endDate = value.validityTo;
																		  		      newArr.priority = value.priorityId;
																		  		      newArr.companyId = value.companyId;
																		  		      newArr.insuranceId = value.insuranceId;
																		  		      newArr.contractId = value.contractId;
																		  		      newArr.insuredAmount = value.insuredAmt;
																		  		      newArr.claimNumber = value.claimNumber;
																			  		  newArr.patientSponsorDetailsId = value.patientSponsorDetailsId;
																		  		if(value.paymentEntitlementId ==1){
																		  			  newArr.paymentEntitlementName = "Self";
																		  			  newArr.companyName = "Self";
																		  			  newArr.tariffName = "Self Tariff";
																		  			  newArr.tariffId = $scope.defaultSelfTariffId;
																		  			  newArr.companyId = $scope.selfPayeeId;
																		  		}else{
																		  			  newArr.paymentEntitlementName = value.paymentEntitlementDesc;
																		  			  newArr.companyName = value.companyDesc;
																		  			  newArr.tariffName = value.tariffDesc;
																		  			  newArr.tariffId = value.tariffId;
																		  		}
																		  		$scope.paymentEntitleMentList.push(newArr);
																		  		
																		  	
																		  		var prioObj = {
																						  id:i,
																						  value:i,
																					  }
																					  $scope.priorityList.push(prioObj);
																		  		i++;
																			});
															}
														}

													});
												}
								
								$scope.setSecondTabLevel(2);
								$scope.setTbah(1);
								
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
						    
						    $scope.getContractByCompanyId = function(companyId)
							{
						    	$scope.showNameDetails=false;
						    	console.log("getContractByCompanyId ",companyId);
								var companyTypeId = $scope.appointmentMasterObj.paymentEntitlementTypeId;
								if(companyId!=undefined && companyId!="" && companyId!=null && !isNaN(companyId)){
									$scope.corporateCompanyId=companyId;
									if($scope.corEmpVerificationFlag){
										$scope.enableDisabledValidated=true;
										$scope.enableDisabledApply=false;
									}else{
										$scope.enableDisabledValidated=false;
										$scope.enableDisabledApply=true;
									}
								}else{
									$scope.corporateCompanyId="";
									$scope.enableDisabledValidated=false;
								}
								$rootScope.startLoader();
								var param = {organizationId:$scope.organizationId,unitId:$scope.unitId,companyId:companyId,companyTypeId:companyTypeId,visitTypeId:4};
							
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
									}$rootScope.stopLoader();
								});
							}
						    
						    
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
								
								
							
							$scope.applyContractDetails = function(id)
							{
								
								 var isEmpty = !(Object.values($scope.paymentEntitleMentCorporate[0]).every(x =>(x != null &&  x != '')));
								 if(isEmpty)
									 {
									 	
									 if($scope.paymentEntitleMentCorporate[0].companyId == null || $scope.paymentEntitleMentCorporate[0].companyId == '' || $scope.paymentEntitleMentCorporate[0].companyId == undefined){
										 growl.warning("Please select company.",{
												title : "WARNING"
											});
											return; 
									 }
									 									 
									 if($scope.paymentEntitleMentCorporate[0].contractId == null || $scope.paymentEntitleMentCorporate[0].contractId == '' || $scope.paymentEntitleMentCorporate[0].contractId == undefined){
										 growl.warning("Please select contract.",{
												title : "WARNING"
											});
											return; 
									 }
									 }
								 else{
							
								isContractAlreadyExists=checkContractAlreadyExistsOrNot($scope.paymentEntitleMentList,3);
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
								  
								  if($scope.employeeNumberManual != null || $scope.employeeNumberManual != undefined || $scope.employeeNumberManual != '')
									  newArr.employeeNoManual = $scope.employeeNumberManual;
								
								  if($scope.employeeName != null && $scope.employeeName != undefined && $scope.employeeName != ''){
									  newArr.employeeName = $scope.employeeName;
								  }
								  
								  if($scope.relation != null && $scope.relation != undefined && $scope.relation != ''){
									  newArr.relation = $scope.relation;
								  }
								  
								  if($scope.medicalCardNo != null && $scope.medicalCardNo != undefined && $scope.medicalCardNo != ''){
										newArr.medicalCardNo = $scope.medicalCardNo;
								  }
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
								  newArr.claimNumber = $scope.paymentEntitleMentCorporate[0].claimNumber;
								  if($scope.CompanyObjDoc.companyId!=undefined && $scope.CompanyObjDoc.companyId!=null && $scope.CompanyObjDoc.companyId!="" && $scope.paymentEntitleMentCorporate[0].companyId==$scope.CompanyObjDoc.companyId){
									  newArr.reportPath=$scope.CompanyObjDoc.reportPath;
								  }

								  isInsuranceExists=checkInsuranceExistsOrNot($scope.paymentEntitleMentList,newArr)
									if (!isInsuranceExists) {
										$scope.enableDisabledApply=false;
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
										$scope.employeeNumberManual= '';
										$scope.employeeName= '';
										$scope.relation= '';
										$scope.medicalCardNo= '';
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
						    
							$scope.creatingEncounterForAbscondedPatient=function(){
								var index=$scope.abscondIndex;
								///$rootScope.startLoader();
								$rootScope.patientLable.age=($scope.patientList[index].age);
								$rootScope.patientLable.patientName=($scope.patientList[index].patientName);
								$rootScope.patientLable.genderCode=($scope.patientList[index].genderCode);
								$rootScope.patientLable.uhIdNumber=($scope.patientList[index].uhIdNumber);
								$rootScope.patientLable.birthDate=($scope.patientList[index].birthDate);
								
								$rootScope.unknownPatient.age=($scope.patientList[index].age);
								$rootScope.unknownPatient.patientName=($scope.patientList[index].patientName);
								$rootScope.unknownPatient.genderCode=($scope.patientList[index].genderCode);
								$rootScope.unknownPatient.uhIdNumber=($scope.patientList[index].uhIdNumber);
								$rootScope.unknownPatient.genderId=($scope.patientList[index].genderId).toString();
								$rootScope.unknownPatient.mobileNo=($scope.patientList[index].mobileNo);
								$rootScope.unknownPatient.patientType=($scope.patientList[index].patientType);
								$rootScope.unknownPatient.patientId=($scope.patientList[index].patientId);
								
								if(($scope.patientList[index].prefixId)> 0){
									$scope.unknownPatient.prefixId = ($scope.patientList[index].prefixId).toString();
									$scope.patientObj.prefixId = ($scope.patientList[index].prefixId);
							    }
								if($scope.patientList[index].patientType == 4)
									{
										$rootScope.unknownPatient.ageFormat = $scope.patientList[index].age.slice($scope.patientList[index].age.length-1);
										$rootScope.unknownPatient.age = $scope.patientList[index].age.slice(0,-1);
										
									}
								else
									{
									 var array = $scope.patientList[index].age.split(' ');
									 $rootScope.unknownPatient.ageFormat = array[0].slice( array[0].length-1);
										$rootScope.unknownPatient.age =  array[0].slice(0,-1);
									
									}
							}
							
							
							// check the corporate verification is required or not
							$scope.corEmpVerificationFlag=false;
							$scope.getCoEmpVerificationReq = function(){
								try{
									var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList";
									var obj = {
												organizationId : $scope.organizationId,
												unitId         :$scope.unitId
										    }
									BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if(response.data.status == "success"){
											$scope.unitConfigurationObject = response.data.listObject[0];
											if($scope.unitConfigurationObject.isCorporateEmpVerificationReq=='Y'){
												$scope.corEmpVerificationFlag=true;
												$scope.enableDisabledApply=false;	
												$scope.enableDisabledValidated=false;

											}else{
												$scope.corEmpVerificationFlag=false;
												$scope.enableDisabledApply=true;	
												$scope.enableDisabledValidated=false;

											}
										}
									});
								}catch (e) {
									console.log("Exception",e.message);
								}
							}
							// check the corporate verification is required or not
							$scope.getCoEmpVerificationReq();
							
							// kin details update and remove end
							
							$scope.clearCoEmpDetails=function(){
								
								 if($scope.paymentEntitleMentCorporate[0].companyId == null || $scope.paymentEntitleMentCorporate[0].companyId == '' || $scope.paymentEntitleMentCorporate[0].companyId == undefined){
									 growl.warning("Please select company.",{
											title : "WARNING"
										});
										return; 
								 }
								 else if($scope.paymentEntitleMentCorporate[0].contractId == null || $scope.paymentEntitleMentCorporate[0].contractId == '' || $scope.paymentEntitleMentCorporate[0].contractId == undefined){
									 growl.warning("Please select contract.",{
											title : "WARNING"
										});
										return; 
								 }
								 else{
									 $scope.corEmpDependantList=[];
										$scope.showNameDetails=false;
										$scope.showHideAcceptRejectBtn=false;
										$scope.showHideOkBtn=false;
										$scope.initCoEmp();
										$scope.initEmpObj();
										$scope.getTypeOfCoEmp(3);
										angular.element('#validationEmployeeModel').modal('show');
								 }
								 
								
							}
							
							$scope.empObj.employeeNumber="";
							$scope.validateCorporateEmp=function(){
								$scope.name="";
								if($scope.corEmpObj.typeOfCoEmp==undefined || $scope.corEmpObj.typeOfCoEmp=="" || $scope.corEmpObj.typeOfCoEmp==null){
									growl.warning("Please type of verification",{
										title : WARNING_MSG
									});
									return;
								}
									/*if($scope.patientDetails.patientId==undefined || $scope.patientDetails.patientId=="" || $scope.patientDetails.patientId==null){
										growl.warning("Please select patient",{
											title : WARNING_MSG
										});
										return;
									}*/
									if($scope.corporateCompanyId==undefined || $scope.corporateCompanyId=="" || $scope.corporateCompanyId==null){
										growl.warning("Please select company name",{
											title : WARNING_MSG
										});
										return;
									}
								//for staff or employee 
									$scope.corEmpListObj=[];
									$scope.corEmpDependantList = [];
									$scope.displayMsg="";
								if($scope.corEmpObj.typeOfCoEmp=='1'){
									if($scope.empObj.employeeNumber==undefined || $scope.empObj.employeeNumber=="" || $scope.empObj.employeeNumber==null){
										growl.warning("Please enter employee number",{
											title : WARNING_MSG
										});
										return;
									}
									var data={
											typeOfEmp:$scope.corEmpObj.typeOfCoEmp,
											employeeNumber:$scope.empObj.employeeNumber,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											firstName:$scope.patientDetails.firstName,
											middleName:$scope.patientDetails.middleName,
											lastName:$scope.patientDetails.lastName,
											corporateId:$scope.corporateCompanyId
//											genderId:$scope.patientDetails.genderId,
//											birthDate:$scope.patientDetails.birthDate
									    }
									var URI = BASE_URL + ROOT_URL + "/api/global/verifyCorporateEmployee";
									BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											if (response.data.status == "success") {
											
												if(response.data.object!=null && response.data.listObject!=null){
													
													$scope.corEmpObj = response.data.object;
													$scope.corEmpListObj=response.data.listObject[0];
													
													$scope.name=$scope.corEmpListObj.firstName+" "+$scope.corEmpListObj.middleName+" "+$scope.corEmpListObj.lastName;
													if($scope.corEmpObj==1){
														
															$scope.color="green";
															$scope.enableDisabledValidated=false;											
															$scope.showHideAcceptRejectBtn=true;
															
														}else if($scope.corEmpObj==2){
															
															$scope.color="red";		
															$scope.showHideAcceptRejectBtn=true;
														}else{
														}
													$scope.showHideOkBtn=false;
												}else{
													$scope.color="red";	
													$scope.name="record not found.."
													$scope.showHideAcceptRejectBtn=false;
													$scope.showHideOkBtn=true;
												}
												$scope.showNameDetails=true;
												 console.log("$scope.corEmplist",$scope.corEmpObj);
//												 $scope.initCoEmp();
												 angular.element('#validationEmployeeModel').modal('show');
											}
										});
								
								}else if($scope.corEmpObj.typeOfCoEmp=='2'){
									if($scope.empObj.employeeNumber==undefined || $scope.empObj.employeeNumber=="" || $scope.empObj.employeeNumber==null){
										growl.warning("Please enter employee number",{
											title : WARNING_MSG
										});
										return;
									}
									var data={
											typeOfEmp:$scope.corEmpObj.typeOfCoEmp,
											employeeNumber:$scope.empObj.employeeNumber,
											organizationId:$scope.organizationId,
											unitId:$scope.unitId,
											corporateId:$scope.corporateCompanyId
//											
									    }
									var URI = BASE_URL + ROOT_URL + "/api/global/verifyCorporateEmployee";
									BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											$scope.corEmpDependantList = [];
											if (response.data.status == "success") {
											
												if(response.data.listObject!=null && response.data.listObject.length>0){
													$scope.corEmpDependantList = response.data.listObject;
													$scope.showNameDetails=true;
													angular.forEach($scope.corEmpDependantList, function (value, key) {
														if(value.employeeNumber != null && value.employeeNumber != undefined && value.employeeNumber != ""){
															$scope.name = value.empName;
															return;
														}
													});
//													angular.element('#validationEmployeeModel').modal('hide');
													
												}else{
													  $scope.color="red";
													  $scope.showNameDetails=true;
													  $scope.name="record not found.."
													  $scope.showHideAcceptRejectBtn=false;
													  $scope.showHideOkBtn=true;
												}
												
											}
//											$scope.initCoEmp();
										});
								}
								
								$scope.getEmpDependantDetailsByIndex=function(index){
									$scope.showNameDetails=false;
									     if($scope.patientDetails.firstName==$scope.corEmpDependantList[index].firstName) {
//									    	 $scope.name=$scope.corEmpDependantList[index].firstName+" "+$scope.corEmpDependantList[index].middleName+" "+$scope.corEmpDependantList[index].lastName;
									    	 if($scope.patientDetails.genderId==$scope.corEmpDependantList[index].genderId){
									    		       
//									    		        $scope.color="green";
														$scope.enableDisabledValidated=false;										
														$scope.showHideAcceptRejectBtn=true;
													
									    	 }else{
//									    		    $scope.color="red";
													$scope.enableDisabledValidated=false;										
													$scope.showHideAcceptRejectBtn=true;
												}
									     }else{
//									    	   $scope.color="red";
												$scope.enableDisabledValidated=false;										
												$scope.showHideAcceptRejectBtn=true;
											}
									 	$scope.showNameDetails=true;
//									     $scope.initCoEmp();
								}
								
							}
							
							$scope.dateOptionsKinDetails = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
								$scope.openDatePickerExpiry = function(index) {
									// alert("in");
									// $scope.expiry.opened = true;
									for (var i = $scope.kinDetailsList.length - 1; i >= 0; i--) {
										if (i === index) {

											$scope.expiry.opened[i] = true;
										} else {
											$scope.expiry.opened[i] = false;
										}
									}
								};

								$scope.expiry = {
									opened : [ false ],

								};
								
							$scope.disableEnableExpiryKin = function(id,idx)
							{
								
								angular.forEach($scope.identificationList,function(value,key){
									if(value.identificationId == id){
									   if(value.isExpiryRequired == 'Y'){
										   $scope.kinDetailsList[idx].isExpiry='Y';
									   }else{
										   $scope.kinDetailsList[idx].isExpiry='N';
									   }
									}
								});
							
							}
							
								$scope.verifyCorEmp=function(type){
									if(type=='A'){
										$scope.enableDisabledApply=true;	
										$scope.enableDisabledValidated=false;
									}else{
										$scope.enableDisabledApply=false;	
										$scope.enableDisabledValidated=true;
									}
									$scope.initCoEmp();
									$scope.initEmpObj();
								}
								
								$scope.closePopupValidate = function()
								{
									angular.element('#validationEmployeeModel').modal('hide');
								}
								
								$scope.saveMannualDetails=function(){
									if($scope.empObj.employeeNumber==undefined || $scope.empObj.employeeNumber=="" || $scope.empObj.employeeNumber==null)
										{
											growl.warning("Please enter employee number.",{
												title : WARNING_MSG
											});
											return;
										
										}
									/*else if($scope.corEmpObj.note==undefined || $scope.corEmpObj.note=="" || $scope.corEmpObj.note==null){
										growl.warning("Please enter note",{
											title : WARNING_MSG
										});
										return;
									}*/
									if($scope.corEmpObj.employeeName==undefined || $scope.corEmpObj.employeeName=="" || $scope.corEmpObj.employeeName==null){
										growl.warning("Please enter Employee Name",{
											title : WARNING_MSG
										});
										return;
									}
									
									if($scope.corEmpObj.relation==undefined || $scope.corEmpObj.relation=="" || $scope.corEmpObj.relation==null){
										growl.warning("Please enter Relation",{
											title : WARNING_MSG
										});
										return;
									}
									$scope.enableDisabledApply=true;	
									$scope.enableDisabledValidated=false;
									angular.element('#validationEmployeeModel').modal('hide');
									$scope.employeeNumberManual = $scope.empObj.employeeNumber;
									$scope.employeeName = $scope.corEmpObj.employeeName;
									$scope.relation = $scope.corEmpObj.relation;
									$scope.medicalCardNo = $scope.corEmpObj.medicalCardNo;
									$scope.initEmpObj();
								}
								
								 
								$scope.empNoShowHide=true; // for empNo text field hide show
								$scope.noteShowHide=false; // for note text area hide show
								$scope.dependTblShowHode=false; // for table hide show
								$scope.showHidevalidate=true; //for validate buttton 	
								$scope.showHideSaveBtn=false; // for mannual co-emp verification
								$scope.showHideAcceptRejectBtn=false;
								$scope.showNameDetails=false;
								$scope.showHideOkBtn=false;
								
								$scope.getTypeOfCoEmp=function(id){
									console.log("getTypeOfCoEmp id ",id);
									$scope.empObj.employeeNumber = '';
									$scope.corEmpObj.employeeName = '';
									$scope.corEmpObj.relation = '';
									$scope.corEmpObj.medicalCardNo = '';
									$scope.corEmpObj.note = '';
									  if(id=="1"){
										  $scope.corEmpObj.typeOfCoEmp="1";
										  $scope.dependTblShowHode=false;
										  $scope.noteShowHide=false;
										  $scope.showHidevalidate=true;
										  $scope.showHideSaveBtn=false; 
										  $scope.showNameDetails=false;
										  $scope.showHideOkBtn=false;
									  }
									  if(id=="2"){
										  $scope.corEmpObj.typeOfCoEmp="2";
										  $scope.dependTblShowHode=true;
										  $scope.showHidevalidate=true;
										  $scope.noteShowHide=false;
										  $scope.showHideSaveBtn=false; 
										  $scope.showNameDetails=false;
										  $scope.showHideOkBtn=false;
									  }
									  if(id=="3"){
										  $scope.corEmpObj.typeOfCoEmp="3";
										  $scope.dependTblShowHode=false;
										  $scope.noteShowHide=true; 
										  $scope.showHidevalidate=false; 
										  $scope.showHideSaveBtn=true; 
										  $scope.showNameDetails=false;
										  $scope.showHideAcceptRejectBtn=false;
										  $scope.showHideOkBtn=false;
									  }
								}
								$scope.getTypeOfCoEmp(3);
							//kin details
							$scope.getKinDetailsByPatientId = function(){
								var URI = BASE_URL + ROOT_URL + KIN_DETAILS + PATIENT + SLASH + $rootScope.unknownPatient.patientId  + UNIT + SLASH +  $scope.unitId  + ORG + SLASH + $scope.organizationId;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success") {
										$scope.kinsList = response.data.listObject;
									}
								});
							}
							
							
							// kin details update and remove start
							$scope.getKinDetailsById = function(kinDetailsId){
								if($scope.unknownPatient.kinDetailsId != ""){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + KIN_DETAILS + SLASH + kinDetailsId;
								BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											if (response.data.status == "success") {
												$scope.kinDetailsList[0]=(angular.copy(response.data.object));
												$scope.kinDetailsList[0].prefixId = ($scope.kinDetailsList[0].prefixId).toString();
												$scope.getStateByCountry($scope.kinDetailsList[0].countryId);
												$scope.getDistrictByState($scope.kinDetailsList[0].stateId);
												$scope.getCityByDistrict($scope.kinDetailsList[0].districtId);
												$scope.getAreaByCity($scope.kinDetailsList[0].cityId);
												$scope.kinDetailsList[0].identificationId = ($scope.kinDetailsList[0].identificationId).toString();
												console.log($scope.kinDetailsList);
												$scope.nextOfKinForm.$pristine=false;
											}
										});
								}else{
									growl.warning(" Please select details. ",
											{
												title : 'warning!'
											});
								}
							}
							$scope.updateKinDetailsStatus = function(kinDetailsId){
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + KIN_DETAILS + SLASH + kinDetailsId + STATUS + SLASH + 'I';
								BillingGenericService.serviceAction(METHOD_PUT, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success") {
										$scope.unknownPatient.kinDetailsId="";
										$scope.getKinDetailsByPatientId();
									}
								});
							}
							
							
							$scope.quickReg = function()
							{
								$rootScope.readOnlyFlag = false;
								$scope.init();
							}

							$scope.saveUnknownPatient = function() {
								if($scope.unknownPatient.prefixId == '' || $scope.unknownPatient.prefixId == null || $scope.unknownPatient.prefixId == undefined){
									growl.warning(
											'Please select prefix...!!',
											{
												title : 'Warning!'
											});
									return false;
									
								}								
								else if($scope.unknownPatient.patientName == '' || $scope.unknownPatient.patientName == null ||  $scope.unknownPatient.patientName == undefined){
									growl.warning(
											'Please enter patient name...!!',
											{
												title : 'Warning!'
											});
									return false;
									
								}else if($scope.unknownPatient.genderId == '' || $scope.unknownPatient.genderId == null || $scope.unknownPatient.genderId == undefined){
									growl.warning(
											'Please select gender...!!',
											{
												title : 'Warning!'
											});
									return false;
									
								}else if($scope.unknownPatient.age == '' || $scope.unknownPatient.age == null || $scope.unknownPatient.age == undefined){
									growl.warning(
											'Please enter age...!!',
											{
												title : 'Warning!'
											});
									return false;
								}else if($scope.unknownPatient.mobileNo == '' || $scope.unknownPatient.mobileNo == null || $scope.unknownPatient.mobileNo == undefined){
									growl.warning(
											'Please enter contact number...!!',
											{
												title : 'Warning!'
											});
									return false;
								}
								else if($scope.unknownPatient.mobileNo.length!=10){
				        			
//								}else{
									growl.warning("Please enter valid mobile number",{
											title : 'Warning!'
										});
										return false;
								}
								
								else{
									 if($scope.unknownPatient.isMedicoLegal == 'Y'){
										
												if($scope.unknownPatient.accompaniedName == '' || $scope.unknownPatient.accompaniedName == null || $scope.unknownPatient.accompaniedName == undefined){
													growl.warning(
															'Please enter Accompained by...!!',
															{
																title : 'Warning!'
															});
													return false;
												}else if($scope.unknownPatient.policeStationName == '' || $scope.unknownPatient.policeStationName == null || $scope.unknownPatient.policeStationName == undefined){
													growl.warning(
															'Please enter Police Station Name...!!',
															{
																title : 'Warning!'
															});
													return false;
												}else if($scope.unknownPatient.policeBuckleNo == '' || $scope.unknownPatient.policeBuckleNo == null || $scope.unknownPatient.policeBuckleNo == undefined){
													growl.warning(
															'Please enter Police Buckle No...!!',
															{
																title : 'Warning!'
															});
													return false;
												}
											}
									
									$scope.kinDetailsFlag =false;
									
									angular.forEach($scope.kinDetailsList, function( value, index){
										if(value.kinName != ''){
											if(value.mobileNo != ""){
												var contryCode=$scope.patientObj.countryCallingCode;
									        	var mobileNo=value.mobileNo;
									        	if(contryCode==91){
									        		if(mobileNo.length==10){
									        			
														}else{
															growl.error("Please enter valid mobile number",{
																title : ERROR_MSG
																});
																$('#mobileNo').focus();
																$scope.setFirstTabLevel(2);
																$scope.kinDetailsFlag =true;
																return false;
									        		}
									        	}else{
										        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
										        			
														}else{
															growl.error("Please enter valid mobile number",{
																title : ERROR_MSG
																});
																$('#mobileNo').focus();
																$scope.setFirstTabLevel(2);
																$scope.kinDetailsFlag =true;
																return false;
									        		}
									        	}
											}
									        	
										}
									});
									
									if($scope.kinDetailsFlag){
										return false;
									}
									
									
									isPriorityZeroExists=checkPriorityZeroOrNot($scope.paymentEntitleMentList);
									  if (isPriorityZeroExists) {
										
										  growl.warning("Please select priority of all paymententitlement list.",{	
											title : "WARNING"
										});
										// $scope.tab = 3;
										return false;
										
									  }
									  if($scope.patobj.patientImage1!=undefined &&  !angular.equals({}, $scope.patobj.patientImage1)  && $scope.patobj.patientImage1!="")
									   {
										     var ext = $scope.patobj.patientImage1.name.split(".").pop().toLowerCase();
												if($.inArray(ext, ["jpg","jpeg",'png']) == -1) {
													growl.warning('You can only upload *.jpg, *.jpeg, *.png',
															{
																title : 'warning!'
															});
													$rootScope.stopLoader();
													return false;
												}
									   }
									  
									$rootScope.startLoader();
									if ($scope.popUpFlag) {
										$scope.showModal = !$scope.showModal;

									} else {
										// $rootScope.startLoader();
										/*
										 * var NewDateDoa =
										 * $rootScope.getFormatDate($scope.datepicker);
										 * var NewDatePdd =
										 * $rootScope.getFormatDate($scope.datepickerPdd);
										 */
										 var dob="";
										// alert($scope.unknownPatient.ageFormat);
										
										 if($scope.unknownPatient.ageFormat == 'Y'){
											 dob = moment(new Date()).subtract('years',$scope.unknownPatient.age).format('DD-MM-YYYY HH:mm:ss');
										 }else if($scope.unknownPatient.ageFormat == 'M')
											 {
											 dob = moment(new Date()).subtract('months',$scope.unknownPatient.age).format('DD-MM-YYYY HH:mm:ss');
											 }else{
												 dob = moment(new Date()).subtract('days',$scope.unknownPatient.age).format('DD-MM-YYYY HH:mm:ss');
											 }
										 
										 //return false;
										$scope.currentDate = new Date();
										$scope.currentDate = moment($scope.currentDate)
												.format('DD-MM-YYYY HH:mm:ss');
									
										var data = {

											patientName : $scope.unknownPatient.patientName,
											genderId : $scope.unknownPatient.genderId,
											// age : $scope.unknownPatient.age,
											age : parseInt($scope.unknownPatient.age.replace(/\D/g, '')),
											ageFormat : $scope.unknownPatient.ageFormat,
											accompaniedBy : $scope.unknownPatient.accompaniedName,
											firNo        :$scope.unknownPatient.firNo,
											policeBuckleNo :$scope.unknownPatient.policeBuckleNo,
											policeStationName:$scope.unknownPatient.policeStationName,
											mlcContactNo:$scope.unknownPatient.mlcContactNo,
											mlcRelationId:$scope.unknownPatient.mlcRelationId,
											mlcRelationId:($scope.unknownPatient.mlcRelationId != "0" &&  $scope.unknownPatient.mlcRelationId != null)?  $scope.unknownPatient.mlcRelationId : null,
											priorityId : ($scope.unknownPatient.priorityId != "0" &&  $scope.unknownPatient.priorityId != null)?  $scope.unknownPatient.priorityId : null,
											isMedicoLegal : $scope.unknownPatient.isMedicoLegal,
											status : 'A',
											createdBy : $scope.userId,
											updatedBy : $scope.userId,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											createdDate:$scope.currentDate,
											updatedDate:$scope.currentDate,
											visitTypeId:4,
											admissionTypeId : 4,
											tUhid : $scope.unknownPatient.uhIdNumber,
											remark : $scope.unknownPatient.remark,
											modeOfArrival : $scope.unknownPatient.modeOfArrival,
											mobileNo:$scope.unknownPatient.mobileNo,
											kinArray :$scope.kinDetailsList,
											kinList :[],
											kinDetailsId : $scope.unknownPatient.kinDetailsId != "" ? $scope.unknownPatient.kinDetailsId : 0,
											dob:dob,
											prefixId:$scope.unknownPatient.prefixId,
											patientCategoryId : $scope.patientCategoryId,
											defaultSelfTariffId : $scope.defaultSelfTariffId,
											payeeId:$scope.selfPayeeId,
											legacyVisitId:$scope.legacyVisitId
										}
										
										
										console.log('$scope.paymentEntitleMentList',$scope.paymentEntitleMentList);
										var sponserList = [];
										angular.forEach($scope.paymentEntitleMentList, function (value, key) {
											var newArr = {};
											newArr.organizationId = $scope.organizationId ;
											newArr.unitId = $scope.unitId;
											newArr.visitTypeId = 4;
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
											newArr.employeeNoManual = value.employeeNoManual;
											newArr.employeeName = value.employeeName;
											newArr.relation = value.relation;
											newArr.medicalCardNo = value.medicalCardNo;
											newArr.claimNumber = value.claimNumber;
											if(value.priority == 1)
												{
													data.companyId = value.companyId;
													data.contractId = value.contractId;
													data.paymentEntitlementId = value.paymentEntitlementId;
												}
											sponserList.push(newArr);
										});
										data.patientSponsorDetailsDtosList = sponserList;
										
										
										if($rootScope.unknownPatient.patientType == 4)
											{
											data.tPatientId  = $scope.unknownPatient.patientId;
											}
										else if($rootScope.unknownPatient.patientType == 1)
											{
												data.patientId = $scope.unknownPatient.patientId;
											}
										else
											{
											
											}
										console.log(data);
										//return false;
										if($scope.tab==1 && $scope.unknownPatient.ageFormat==""){
											growl.warning(
													'Please select age in year,month and day',
													{
														title : 'Warning!'
													});
											$rootScope.stopLoader();
											return false;
										}
										
										var URI = BASE_URL + ROOT_URL
												+ ADDUNKNOWNPATIENT;
										GenericService
												.serviceAction("POST", URI, data)
												.then(
														function(response) {
															$rootScope.stopLoader();
															if (response.data.status == "success") {
																growl
																		.success(
																				'Patient Registered Successfully...!!',
																				{
																					title : 'Success!'
																				});
																if($scope.patobj.patientImage1!=undefined &&  !angular.equals({}, $scope.patobj.patientImage1) && $scope.patobj.patientImage1!="")
																{
//																    $scope.uploadPatientImage($scope.patobj.patientImage1,response.data.object.patientId,$scope.organizationId,$scope.unitId);
																	$scope.admissionList=response.data.object;
																	$scope.uploadPatientMLCImage();
																   }
																
																if($scope.allDocumentFinalList != undefined && $scope.allDocumentFinalList.length>0){
																	 $scope.count=0;
																	 angular.forEach($scope.allDocumentFinalList, function(value, key) 
																				{  		
																		            if(response.data.object != null){
																		            	value.admissionId=response.data.object.admissionId;
																			 			value.patientId=response.data.object.patientId;
																		            }else{
																		            	value.admissionId=$rootScope.unknownPatient.admissionId;
																			 			value.patientId=$rootScope.unknownPatient.patientId;
																		            }
																					var promise = $scope.uploadEncounterContractDoc(value);
																					promise.then(function(response) 
																							{
																							  if(response.data.status == "success")
																								  {
																								    $scope.count++;
																								    if($scope.finalDocUploadList.length==$scope.count)
																									 {
																									  
																									   $rootScope.stopLoader();
																									   $scope.encounterDetailsContractList = [];
																									   $scope.finalDocUploadList=[];
																									   $scope.allDocumentFinalList=[];
//																									   $scope.getPatientDocumentList();
																									 }
																							      }
																							  else 
																								{																						
																									 $rootScope.stopLoader();
																								}
																						    });
																						
																				});
																}
																$scope.kinsList=[];
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
																			'companyId':$scope.selfPayeeId,
																			'tariffId':$scope.defaultSelfTariffId,
																			'contractId':null,
																			'typeId' : 1,
																			'insuredAmount' : '',
																			'billingBedCategoryId':null
																		}];
																$scope.init();
																
															} else {
																growl
																		.error(
																				'Something went wrong...!!',
																				{
																					title : 'Error!'
																				});
																$rootScope.stopLoader();
															}

														});
									}
									
									
									
									
									
								}
								
								
								
								

							}
							
							//*********Added by Sidheshwar
							$scope.newAge="";
							/*$scope.calculateAge= function()
							{
								var dt = moment($scope.datepicker).format('MM/DD/YYYY');
								$scope.newAge = $rootScope.getAge(dt);
								if($scope.newAge != undefined){
									if($scope.newAge>=$scope.minAge && $scope.newAge <= $scope.maxAge){
										$scope.age=$scope.newAge;	
									}else{
										$scope.age = "";
										growl.warning("Age should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
											title : "warning"
											});
									}
								}
								
							}
							*/
							
							
							/*$scope.setFirstTabLevel= function(newTab)
							{
								$scope.tab = newTab;
								$scope.addressFlag =false;
								$scope.sameAsLocal ='Y';
							}*/
							
							$scope.getDataByPinCode = function(){
								$rootScope.startLoader();
								if($scope.patientObj.zipCode != null || $scope.patientObj.zipCode != undefined){
									var zipC = $scope.patientObj.zipCode;
									
								var URI = BASE_URL + ROOT_URL+"/adt/getDataByPinCode/"+zipC+"/"+$scope.organizationId;
								BillingGenericService
										.serviceAction(METHOD_GET,
												URI, "",
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success"){
														
														if(response.data.object.countryId != '' ){
															$scope.patientObj.countryId = response.data.object.countryId;
														}
														
														if(response.data.object.stateId != ''){
															$scope.getStateByCountry(response.data.object.countryId,'L');
															$scope.patientObj.stateId = response.data.object.stateId;
														}
														
														if(response.data.object.districtId != ''){
															$scope.getDistrictByState(response.data.object.stateId,'L');
															$scope.patientObj.districtId = response.data.object.districtId;
														}
														
														if(response.data.object.cityId != '')
															$scope.getCityByDistrict(response.data.object.districtId,'L');
															$scope.patientObj.cityId = response.data.object.cityId;
														
														if(response.data.object.areaId != ''){
															$scope.getAreaByCity(response.data.object.cityId,'L');
															$scope.patientObj.areaId = response.data.object.areaId;
														}
														
														$rootScope.stopLoader();
													}else{
														$rootScope.stopLoader();
													}
												});
								}
								
							}
							
							$scope.calculateAge= function()
							{
								if($scope.datepicker == undefined || $scope.datepicker == null || $scope.datepicker == ''){
									 $scope.age = "";
									   growl.warning("Date is invalid",{
											title : "warning"
										});
										$scope.datepicker = new Date();
									
									   return false;
								}else{
								if($scope.datepicker > new Date())
									{
											       $scope.age = "";
												   growl.warning("DOB should not be greater than today's date",{
														title : "warning"
													});
													$scope.datepicker = new Date();
												
												   return false;
									}else{
										
										var dt = moment($scope.datepicker).format('MM/DD/YYYY');
										$scope.newAge = $rootScope.getAge(dt);
										if($scope.newAge != undefined){
											if($scope.newAge>=$scope.minAge && $scope.newAge <= $scope.maxAge){
												$scope.age=$scope.newAge;	
											}else{
												$scope.age = "";
												growl.warning("Age should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
													title : "warning"
													});
											}
										}
										
									}
							}
							
							}
							
							$scope.myFunct = function(age) {
								/*if(age>=$scope.minAge && age <= $scope.maxAge){
									 if($('#datepicker').val()!=""){
											var currentYear = (new Date()).getFullYear();
										    var finalYear = currentYear - age;									    
										}else{
											var currentYear = (new Date()).getFullYear();
										    var finalYear = currentYear - age;
										    $scope.datepicker = new Date("01/01/"+finalYear);
										}
								}else{
									$scope.age = "";
									growl.warning("Age should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
										title : "warning"
										});
								}
								*/
								
								if(age>=$scope.minAge && age <= $scope.maxAge){
									 if($('#datepicker').val()!=""){
											/*var currentYear = (new Date()).getFullYear();
										    var finalYear = currentYear - age;*/
										 var dt = moment($scope.datepicker).format('MM/DD/YYYY');
											var newAgeE = $rootScope.getAge(dt);
											
											if(newAgeE != age)
												{
												var currentYear = (new Date()).getFullYear();
											    var finalYear = currentYear - age;
											    $scope.datepicker = new Date("01/01/"+finalYear);
												}
										    
										}else{
											var currentYear = (new Date()).getFullYear();
										    var finalYear = currentYear - age;
										    $scope.datepicker = new Date("01/01/"+finalYear);
										}
								}else{
									$scope.age = "";
									growl.warning("Age should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
										title : "warning"
										});
								}
								
								  
							}
							
							
							$scope.disableEnableExpiry = function(isExpiryVal)
							{
								 var splittedStringArray = isExpiryVal.split("_");
								if(splittedStringArray[1] == 'Y')
								{
									$scope.expiryDateDisabled = false;
//									$scope.datepickerExpiry=null;
								}
								else
									{
									 $scope.expiryDateDisabled = true;
									}
							}
							
							$scope.hideShowAlise = function(vipVal)
							{
								if(vipVal == 'Y')
									$scope.aliseFlag = true;
								else
									$scope.aliseFlag = false;
									
							}
							
							$scope.ph_numbr = /^\+?\d{10}$/;
					        $scope.eml_add = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
							

							
					     $scope.addNewKinDetails = function(){
							    	$scope.kinDetails.patientId = $scope.patientDetails.patientId;
							    	$scope.kinDetailsList.push(angular.copy($scope.kinDetails));
							    };
							    $scope.removeKinDetails = function(indexToBeDeleted){	
									  $scope.kinDetailsList.splice(indexToBeDeleted, 1);
							    };
							    
								$scope.setKinId = function(kinId){
									$scope.unknownPatient.kinDetailsId = kinId;
								}
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVERELATIONLIST;
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.relationList = [];

											if (response.data.status == "success") {
												$scope.relationList = response.data.listObject;

											}
										});
				
						$scope.sameAsLocalCopy = function(vipVal)
						{
							if(vipVal == 'Y')
								{
								
								if($scope.patientObj.countryId != '' )
								$scope.getStateByCountry($scope.patientObj.countryId,'LTOP');
								
								if($scope.patientObj.stateId != '' )
								$scope.getDistrictByState($scope.patientObj.stateId,'LTOP');
								
								if($scope.patientObj.districtId != '' )
								$scope.getCityByDistrict($scope.patientObj.districtId,'LTOP');
								
								if($scope.patientObj.cityId != '' )
								$scope.getAreaByCity($scope.patientObj.cityId,'LTOP');
								
								$scope.patientObj.permanentAddress = $scope.patientObj.address;
								$scope.patientObj.permanentCountryId = $scope.patientObj.countryId;
								$scope.patientObj.permanentStateId = $scope.patientObj.stateId;
								$scope.patientObj.permanentDistrictId = $scope.patientObj.districtId;
								$scope.patientObj.permanentCityId = $scope.patientObj.cityId;
								$scope.patientObj.permanentAreaId = $scope.patientObj.areaId;
								$scope.patientObj.permanentZipCode = $scope.patientObj.zipCode;
								$scope.patientObj.permanentPhoneCode = $scope.patientObj.phoneCode;
								$scope.patientObj.permanentPhoneNumber = $scope.patientObj.phoneNumber;
								
                                $scope.addressFlag =true;
								}
							else
								{
								
                                $scope.addressFlag =false;
								}
						}
								/*$scope.sameAsLocalCopy = function(vipVal)
								{
									if(vipVal == 'Y')
										{
										
										if($scope.patientObj.address =="" || $scope.patientObj.address ==null
												||  $scope.patientObj.countryId =="" ||  $scope.patientObj.countryId ==null 
												||  $scope.patientObj.stateId ==null ||  $scope.patientObj.stateId =="" 
												|| $scope.patientObj.districtId =="" || $scope.patientObj.districtId == null
												|| $scope.patientObj.cityId ==""  ||$scope.patientObj.cityId ==null
												|| $scope.patientObj.areaId == "" || $scope.patientObj.areaId == null
												|| $scope.patientObj.phoneCode =="" || $scope.patientObj.phoneNumber ==""){
										
											 $scope.sameAsLocal='N';
											 growl.warning("Please Select All Required fields",{
													title : 'Warning !'
													});
													return false;
										}
										
										if($scope.patientObj.countryId != '' )
										$scope.getStateByCountry($scope.patientObj.countryId,'LTOP');
										
										if($scope.patientObj.stateId != '' )
										$scope.getDistrictByState($scope.patientObj.stateId,'LTOP');
										
										if($scope.patientObj.districtId != '' )
										$scope.getCityByDistrict($scope.patientObj.districtId,'LTOP');
										
										if($scope.patientObj.cityId != '' )
										$scope.getAreaByCity($scope.patientObj.cityId,'LTOP');
										
										$scope.patientObj.permanentAddress = $scope.patientObj.address;
										$scope.patientObj.permanentCountryId = $scope.patientObj.countryId;
										$scope.patientObj.permanentStateId = $scope.patientObj.stateId;
										$scope.patientObj.permanentDistrictId = $scope.patientObj.districtId;
										$scope.patientObj.permanentCityId = $scope.patientObj.cityId;
										$scope.patientObj.permanentAreaId = $scope.patientObj.areaId;
										$scope.patientObj.permanentZipCode = $scope.patientObj.zipCode;
										$scope.patientObj.permanentPhoneCode = $scope.patientObj.phoneCode;
										$scope.patientObj.permanentPhoneNumber = $scope.patientObj.phoneNumber;
										$scope.addressFlag =true;
										}
									else
										{
										$scope.patientObj.permanentAddress ='';
										$scope.patientObj.permanentCountryId = '';
										$scope.patientObj.permanentStateId ='';
										$scope.patientObj.permanentDistrictId ='';
										$scope.patientObj.permanentCityId ='';
										$scope.patientObj.permanentAreaId ='';
										$scope.patientObj.permanentZipCode ='';
										$scope.patientObj.permanentPhoneCode ='';
										$scope.patientObj.permanentPhoneNumber ='';
										$scope.addressFlag =false;
										}
								}*/
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVESPECIALITY;
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
									var commonObj1 = {
										organizationId : $scope.organizationId
									};

									var data = {};
									
						BillingGenericService
								.serviceAction(METHOD_POST, URI,
										commonObj,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.specialityList = response.data.listObject;
										});
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEPATIENTCATEGORY;
						GenericService
								.serviceAction("GET", URI, data)
								.then(
										function(response) {
											$scope.patientCategoryList = [];

											if (response.data.status == "success") {
												$scope.patientCategoryList = response.data.listObject;
												console
														.log($scope.patientCategoryList);

											}
										});

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEPREFIXLIST;
								BillingGenericService
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.prefixList = [];
													if (response.data.status == "success")
														$scope.prefixList = response.data.listObject;
												});

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEGENDERLIST;
								BillingGenericService
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.genderList = [];
													if (response.data.status == "success")
														$scope.genderList = response.data.listObject;
												});
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEOCCUPATIONLIST;
						BillingGenericService
								.serviceAction(METHOD_GET, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											$scope.occupationList = [];
											if (response.data.status == "success")
												$scope.occupationList = response.data.listObject;
										});
						
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVENATIONALITYLIST;
								BillingGenericService
										.serviceAction(METHOD_GET, URI, "",
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.nationalityList = [];
													if (response.data.status == "success")
														$scope.nationalityList = response.data.listObject;
													console.log("$scope.nationalityList",$scope.nationalityList);
												});

								var URI = BASE_URL + ROOT_URL + GETRACELIST;

								BillingGenericService
										.serviceAction(METHOD_POST, URI, commonObj1,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.raceList = [];
													if (response.data.status == "success")
														$scope.raceList = response.data.listObject;
												});
								
								

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEMARITALSTATUSLIST;

								BillingGenericService
										.serviceAction(METHOD_GET, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.maritalStatusList = [];
													if (response.data.status == "success")
														$scope.maritalStatusList = response.data.listObject;
												});
								

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEIDENTIFICATION;
								BillingGenericService
								.serviceAction(METHOD_GET, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											$scope.identificationList = [];
											if (response.data.status == "success")
												$scope.identificationList = response.data.listObject;
										});

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVECOUNTRYLIST+$scope.organizationId;								
								BillingGenericService
								.serviceAction(METHOD_GET, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											$scope.countryList = [];
											if (response.data.status == "success")
												$scope.countryList = response.data.listObject;
										});
								//getting default race start
								var URI = BASE_URL + ROOT_URL
								+ "/adt/getDefaultRaceList/"+$scope.organizationId;
						BillingGenericService.serviceAction(METHOD_GET, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											$scope.defaultRaceList = [];
											if (response.data.status == "success")
												if(response.data.listObject.length > 0){
													$scope.patientObj.raceId = response.data.listObject[0].raceId;
												}
												
											console.log("defaultRaceList=",response.data.listObject[0]);
										});
						//getting default race end
						
						//get calling code by using country id start
						$scope.getCallingCode=function(data,type){
							var URI = BASE_URL + ROOT_URL
							+ GETSINGLECOUNTRY;
							GenericService.serviceAction("POST", URI, data).then(
									function(response) {
										if (response.data.status == "success" && response.data.listObject.length >0) {
											$scope.patientObj.countryCallingCode = response.data.listObject[0].countryCallingCode;
											if(type=="PA"){
												$scope.patientObj.permanentPhoneCode = response.data.listObject[0].countryCallingCode;
											}else{
												$scope.patientObj.phoneCode = response.data.listObject[0].countryCallingCode;
												if($scope.sameAsLocal == 'Y'){
													$scope.patientObj.permanentPhoneCode = response.data.listObject[0].countryCallingCode;
												}
											}
										} 
									});
						}
						//get calling code by using country id end
						//get nationality code by using country id start
						$scope.getNationalityByContryId=function(data){
							data.organizationId = $scope.organizationId;
							var URI = BASE_URL + ROOT_URL
							+ "/adt/getNationalityByCountryId";
							GenericService.serviceAction("POST", URI, data).then(
									function(response) {
										if (response.data.status == "success") {
											if(response.data.listObject.length > 0){
											console.log("nationality List ",response.data.listObject[0]);
											if($scope.patientObj.nationalityId == ""){
												$scope.patientObj.nationalityId = response.data.listObject[0].nationalityId;
											}
											
											}

										} else {
//											alert("Error!!");
										}
									});
						}
						
						var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/"+$scope.organizationId+"/"+$scope.unitId;
						
						GenericService.serviceAction("GET", URI).then(function (response) {
							if (response.data.status == 'success') {
								
								$scope.fieldConfigList=response.data.listObject;
								if($scope.fieldConfigList.length>0)
									{
									 $scope.registrationFieldObj=$scope.fieldConfigList[0];
									 console.log("registrationFieldObj",$scope.registrationFieldObj);
									}else{
										$scope.registrationFieldObj={
												prefix: "Y",
												firstName: "Y",
												middleName: "N",
												lastName: "Y",
												birthDate: "Y",
												sex: "Y",
												callingCode: "Y",
												mobileNo: "Y",
												mobileDigit:"10",
												nationality:"Y",
												identificationDoc:"Y",
												race:"Y",
												maritalStatus:"Y",
												occupation:"Y",
												patientCategory:"Y",
												email:"N"
										}
									}												

							} else if (response.data.status == 'error') {
								growl.error(response.data.message,
									{
										title: 'Error!'
									});
							}
						});
						//get nationality code by using country id end
						
						// for get state by country ID
						$scope.getStateByCountry = function(id,type) {
							$scope.startLoader();
							var data = {
								countryId : id
							};								
							
							var dataorg = {
									countryId : id,
									organizationId : $scope.organizationId
								};	
							
							$scope.getCallingCode(dataorg,type);
							$scope.getNationalityByContryId(data);
							var URI = BASE_URL + ROOT_URL
									+ GETSTATELISTBYCOUNTRYID;
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										//$scope.stateList = [];
										if (response.data.status == "success"){
											if(type == 'P')
												{
												$scope.stateList = [];
												$scope.stateList = response.data.listObject;
												
												$scope.patientObj.stateId="";
												$scope.patientObj.districtId="";
												$scope.patientObj.cityId="";
												$scope.patientObj.areaId="";
												$scope.patientObj.zipCode="";
												
												}
											else if(type == 'PA')
												{
												$scope.permanentStateList = [];
												$scope.permanentStateList = response.data.listObject;
												
												$scope.patientObj.permanentStateId="";
												$scope.patientObj.permanentDistrictId="";
												$scope.patientObj.permanentCityId="";
												$scope.patientObj.permanentAreaId="";
												$scope.patientObj.permanentZipCode="";
												}
											else if(type == 'L')
											{
											$scope.stateList = [];
											$scope.stateList = response.data.listObject;
											
											}else if(type == 'LTOP')
											{
												$scope.permanentStateList = [];
												$scope.permanentStateList = response.data.listObject;
												
											}
											else
												{
												$scope.CompanyStateList = [];
												$scope.CompanyStateList = response.data.listObject;
												}
										}
										$scope.stopLoader();
									});


						}
						
						
						$scope.selectGender = function(id)
						{
							if(id > 0){
							var genId = $.grep($scope.prefixList, function (doc) {
			    	            return doc.prefixId == id;
			    	        })[0].id;
							
							$scope.unknownPatient.genderId = genId;
							$scope.patientObj.genderId = genId;
							}
							else
								{
								$scope.unknownPatient.prefixId = '';
								$scope.unknownPatient.genderId = '';
								}
							
						}
						

						$scope.getDistrictByState = function(id,type) {
							$scope.startLoader();
							var data = {
								stateId : id
							};
							var URI = BASE_URL + ROOT_URL
									+ GETDISTRICTLISTBYSTATEID;
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										
										if (response.data.status == "success")
										{
											//$scope.districtList = response.data.listObject;
											if(type == 'P')
											{
											$scope.districtList = [];
											$scope.districtList = response.data.listObject;
											
											$scope.patientObj.districtId="";
											$scope.patientObj.cityId="";
											$scope.patientObj.areaId="";
											$scope.patientObj.zipCode="";
											}
											else if(type == 'PA')
											{
											$scope.permanentDistrictList = [];
											$scope.permanentDistrictList = response.data.listObject;
											$scope.patientObj.permanentDistrictId="";
											$scope.patientObj.permanentCityId="";
											$scope.patientObj.permanentAreaId="";
											$scope.patientObj.permanentZipCode="";
											}else if(type == 'L')
											{
												$scope.districtList = [];
												$scope.districtList = response.data.listObject;
												
											}
											else  if(type == 'LTOP')
											{
											$scope.permanentDistrictList = [];
											$scope.permanentDistrictList = response.data.listObject;
											
										}
										else
											{
											$scope.companyDistrictList = [];
											$scope.companyDistrictList = response.data.listObject;
											}
										}
										$scope.stopLoader();
									});
							

						}

						$scope.getCityByDistrict = function(id,type) {
							$scope.startLoader();
							var data = {
								districtId : id
							};
							var URI = BASE_URL + ROOT_URL
									+ GETCITYLISTBYDISTRICTID;
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										
										if (response.data.status == "success")
											//$scope.cityList = response.data.listObject;
										{
											//$scope.districtList = response.data.listObject;
											if(type == 'P')
											{
											 $scope.cityList = [];
											 $scope.cityList = response.data.listObject;
											 $scope.patientObj.cityId="";
											 $scope.patientObj.areaId="";
											 $scope.patientObj.zipCode="";
											}
											else if(type == 'PA')
											{
											$scope.permanentCityList = [];
											$scope.permanentCityList = response.data.listObject;
											
											$scope.patientObj.permanentCityId="";
											$scope.patientObj.permanentAreaId="";
											$scope.patientObj.permanentZipCode="";
											}else if(type == 'L')
											{
												 $scope.cityList = [];
												 $scope.cityList = response.data.listObject;
												
											}else if(type == 'LTOP')
											{
												$scope.permanentCityList = [];
												$scope.permanentCityList = response.data.listObject;
											}	
										else
											{
											$scope.CompanyCityList = [];
											$scope.CompanyCityList = response.data.listObject;
											}
										}
										$scope.stopLoader();
									});

						}
						$scope.getAreaByCity = function(id,type) {
							$scope.startLoader();
							$scope.idd = id;
							/*var data = {
								cityId : id
							};
							var URI = BASE_URL + ROOT_URL
									+ GETAREALISTBYCITY;*/
							var data = {
									offset:0,
									noOfRecordsPerPage:10,
								    cityId : id
							};
							var URI = BASE_URL + ROOT_URL
									+ "/adt/getAreaByCityIdPagination";
							
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										
										if (response.data.status == "success")
											//$scope.areaList = response.data.listObject;
										{
											if(type == 'P')
											{
												$scope.areaList = [];
												$scope.logEvents = [];
												 $scope.logEvents =  response.data.listObject;
											
											 $scope.patientObj.areaId="";
											$scope.patientObj.zipCode="";
											}
											else if(type == 'PA')
											{
												$scope.permanentAreaList = [];
												$scope.logEventsPer = [];
												$scope.logEventsPer =response.data.listObject;
											$scope.patientObj.permanentAreaId="";
											$scope.patientObj.permanentZipCode="";
											}else if(type == 'L')
											{
												$scope.areaList = [];
												$scope.logEvents = [];
												 $scope.logEvents =  response.data.listObject;
												console.log($scope.areaList)
											}else if(type == 'LTOP')
											{
												$scope.permanentAreaList = [];
												$scope.logEventsPer = [];
												$scope.logEventsPer = response.data.listObject;
											}	
										else
											{
											$scope.CompanyAreaList = [];
											$scope.CompanyAreaList = response.data.listObject;
											}
										}
										$scope.stopLoader();
									});

						}
						
						if($scope.unitCountryId > 0)
							{
							  $scope.patientObj.countryId  = $scope.unitCountryId;
							  $scope.patientObj.stateId  =  $scope.unitStateId;
							  $scope.patientObj.districtId = $scope.unitDistrictId;
							  $scope.patientObj.cityId = $scope.unitCityId;
							  $scope.patientObj.areaId = $scope.unitAreaId;
							  $scope.getStateByCountry($scope.patientObj.countryId,'L');
							  $scope.getDistrictByState($scope.patientObj.stateId,'L');
							  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
							  $scope.getAreaByCity($scope.patientObj.cityId,'L');
							}

						
				    	$scope.PopupOkBtn = function() {
							$scope.popUpFlag = false;
							if($scope.tab==3){
							$scope.saveNormalPatientRegistration();
							}else{
								$scope.saveUnknownPatient();
							}
						}

						$scope.saveNormalPatientRegistration = function()
						{
							$scope.$broadcast('show-errors-check-validity');
							$scope.tab=3;
							var contryCode=$scope.patientObj.countryCallingCode;
                            var mobileNo=$scope.patientObj.mobileNumber;
//                            if(contryCode==91){
//                                if(mobileNo.length==10){
//
//                                    }else{
//                                        growl.error("Please enter valid mobile number",{
//                                            title : ERROR_MSG
//                                            });
//                                            $('#mobileNo').focus();
//                                            return false;
//                                }
//                            }else{
                                    if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){

                                    }else{
                                        growl.error("Please enter valid mobile number",{
                                            title : ERROR_MSG
                                            });
                                            $('#mobileNo').focus();
                                            return false;
                                }
//                            }
							if($scope.patientRegistrationform.$valid==false){
								$scope.setFirstTabLevel(1);

								return false;
							}
							if($scope.patientRegistrationform2.$valid==false){
								$scope.setFirstTabLevel(2);
								return false;
							}
							
						
							
					        if($scope.patientRegistrationform.$valid && $scope.patientRegistrationform2.$valid ){
					        	
					        	$scope.kinDetailsFlag =false;
					        	angular.forEach($scope.kinDetailsList, function( value, index){
									if(value.kinName != ''){
										 if(value.mobileNo !=''){
											 var contryCode=$scope.patientObj.countryCallingCode;
									        	var mobileNo=value.mobileNo;
									        	if(contryCode==91){
									        		if(mobileNo.length==10){
									        			
														}else{
															growl.error("Please enter valid mobile number",{
																title : ERROR_MSG
																});
																$('#mobileNo').focus();
																$scope.setFirstTabLevel(2);
																$scope.kinDetailsFlag =true;
																return false;
									        		}
									        	}else{
										        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
										        			
														}else{
															growl.error("Please enter valid mobile number",{
																title : ERROR_MSG
																});
																$('#mobileNo').focus();
																$scope.setFirstTabLevel(2);
																$scope.kinDetailsFlag =true;
																return false;
									        		}
									        	}
										 }
								        	
									}
								});
					        	
					        	
					        	isPriorityZeroExists=checkPriorityZeroOrNot($scope.paymentEntitleMentList);
								  if (isPriorityZeroExists) {
									
									  growl.warning("Please select priority of all paymententitlement list.",{	
										title : "WARNING"
									});
									// $scope.tab = 3;
									return false;
									
								  }
								  
					        	if($scope.kinDetailsFlag){
									return false;
								}
					        	var contryCode=$scope.patientObj.countryCallingCode;
					        	var mobileNo=$scope.patientObj.mobileNumber;
//					        	if(contryCode==91){
//					        		if(mobileNo.length==10){
//
//										}else{
//											growl.error("Please enter valid mobile number",{
//												title : ERROR_MSG
//												});
//												$('#mobileNo').focus();
//												return false;
//					        		}
//					        	}else{
						        		if(mobileNo.length==parseInt($scope.registrationFieldObj.mobileDigit)){
						        			
										}else{
											growl.error("Please enter valid mobile number",{
												title : ERROR_MSG
												});
												$('#mobileNo').focus();
												return false;
					        		}
//					        	}
					        	 if($scope.patientRegistrationform2.$valid){
					        		 //test start
					        		 if($scope.patientObj.email == null){
					        			 $scope.patientObj.email = "";
					        		 }
					        		 if($scope.patientObj.email != "")
					           			{
						           		 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
						           		 var email = $scope.patientObj.email;
						           		    if (!filter.test(email)) {
					        				    growl.error(EMAIL_MSG,{
													title : ERROR_MSG
												});
					        				    $('#email').focus();
					        				    return false;
						           		    }
						           		    else{
						           		    	if($scope.patientObj.phoneNumber == undefined)
						           		    			$scope.patientObj.phoneNumber='';
						           		    	if($scope.patientObj.phoneNumber == null)
						           		    			$scope.patientObj.phoneNumber='';
						           		     if($scope.patientObj.phoneNumber != '')
								        		{
						           		    	 $scope.storePatientDetails();
								        		}
						           		     else
						           		    	 {
						           		    	 $scope.storePatientDetails();
						           		    	 }
						           		    }
					           			}
					        		 else
					        			 {
					        			 if($scope.patientObj.phoneNumber == undefined)
					        				 $scope.patientObj.phoneNumber='';
					        			 if($scope.patientObj.phoneNumber != '')
							        		{
					        				   $scope.storePatientDetails();
							        		}
					        			 else
					        				 {
					        				 $scope.storePatientDetails();
					        				 }
					        			 }
					        		 //test end
							        };
					        }
					        
						}

						
						  /***============================================Code Image Upload==============================================*/
					    /**PatientImages*/
					    $scope.patientImage1;
					    $scope.patientImage2;
					    $scope.patientImage3;
					    
					    
					    $scope.convertBase64ToImage = function (dataurl, filename) {
						    var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
						        bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
						    while(n--){
						        u8arr[n] = bstr.charCodeAt(n);
						    }
						    return new File([u8arr], filename, {type:mime});
						}
					    
					    $scope.uploadPatientImage = function(inputFile,data,orgId,unitId)
					    {
					    	    var file = inputFile;
						        var fd = new FormData();
						        fd.append('file', file );
								var URI1 = BASE_URL + ROOT_URL + PATIENT_IMAGE_UPLOAD+orgId+"/"+unitId+"/"+data.patientId;
								GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
								{	
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
									}
								});
					    }
					    
					    
					    $scope.openImage = function(index)
                        { 
							
							$rootScope.startLoader();
							var imageUrl = $scope.patientDocumentList[index].documentPath;
							var bFlag = $scope.patientDocumentList[index].bucketFlag;
							var href = $rootScope.currentHrefDoccument();
							if(bFlag == 1){
							$.fancybox.open({
								width: 1000, // or whatever
								height: 1000,
								href:href+imageUrl,
								type: 'iframe'
							});
							
								$rootScope.stopLoader();
						
							}else{
								$.fancybox.open({
									width: 1000, // or whatever
									height: 1000,
									href:imageUrl,
									type: 'iframe'
								});
								
									$rootScope.stopLoader();
							}
                      }
					    
					    
					    /***============================================Code Image Upload==============================================*/
					    
					    
						  var _video = null,
					        patData = null;

					    $scope.patOpts = {x: 0, y: 0, w: 25, h: 25};

					    // Setup a channel to receive a video property
					    // with a reference to the video element
					    // See the HTML binding in main.html
					    $scope.channel = {};

					    $scope.webcamError = false;
					    $scope.onError = function (err) {
					        $scope.$apply(
					            function() {
					                $scope.webcamError = err;
					            }
					        );
					    };

					    $scope.onSuccess = function () {
					        // The video element contains the captured camera data
					        _video = $scope.channel.video;
					        _video.width = 117;
					        _video.height = 100;
					        $scope.$apply(function() {
					        	//alert(_video.width);
					            $scope.patOpts.w = _video.width;
					            $scope.patOpts.h = _video.height;
					            //$scope.showDemos = true;
					        });
					    };

					    $scope.onStream = function (stream) {
					        // You could do something manually with the stream.
					    };

						$scope.makeSnapshot = function(type) {
					        if (_video) {
					        	
					            var patCanvas = document.querySelector('#snapshot'+type);
					            if (!patCanvas) return;
					            //alert(_video.width);
					            patCanvas.width = _video.width;
					            patCanvas.height = _video.height;
					            var ctxPat = patCanvas.getContext('2d');

					            var idata = getVideoData($scope.patOpts.x, $scope.patOpts.y, $scope.patOpts.w, $scope.patOpts.h);
					            ctxPat.putImageData(idata, 0, 0);

					            sendSnapshotToServer(patCanvas.toDataURL());

					            patData = idata;
					        }
					    };
					    
					    /**
					     * Redirect the browser to the URL given.
					     * Used to download the image by passing a dataURL string
					     */
					    $scope.downloadSnapshot = function downloadSnapshot(dataURL) {
					        window.location.href = dataURL;
					    };
					    
					    var getVideoData = function getVideoData(x, y, w, h) {
					        var hiddenCanvas = document.createElement('canvas');
					        hiddenCanvas.width = _video.width;
					        hiddenCanvas.height = _video.height;
					        var ctx = hiddenCanvas.getContext('2d');
					        ctx.drawImage(_video, 0, 0, _video.width, _video.height);
					        return ctx.getImageData(x, y, w, h);
					    };

					    /**
					     * This function could be used to send the image data
					     * to a backend server that expects base64 encoded images.
					     *
					     * In this example, we simply store it in the scope for display.
					     */
					    var sendSnapshotToServer = function sendSnapshotToServer(imgBase64) {
					        $scope.snapshotData = imgBase64;
					    };
						
						//for end camera
						
					    $scope.capture = function(id) {
				            try {
				            	var quality = 60; //(1 to 100) (recommanded minimum 55)
				                var timeout = 10; // seconds (minimum=10(recommanded), maximum=60, unlimited=0 )
				                var res = CaptureFinger(quality, timeout);	
				                if (res.httpStaus) {
				                    if (res.data.ErrorCode == "0") {
				                        document.getElementById('imgFinger'+id).src = "data:image/bmp;base64," + res.data.BitmapData;
				                        $scope.thumbImage = res.data.BitmapData;
				                        $scope.patientObj.thumbImage = res.data.BitmapData;
										//alert(res.data.BitmapData);
				                    }
				                }
				                else {
				                    alert(res.err);
				                }
				            }
				            catch (e) {
				                alert(e);
				            }
				            return false;
				        }
					    
					    
					    $scope.cancelPatientImg = function(info){
							
							if(info.patientId > 0){
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL+"/api/global/patient/deletePatientImage/"+info.patientId+"/"+$scope.organizationId;
									BillingGenericService
											.serviceAction(METHOD_GET,
													URI, "",
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														if (response.data.status == "success"){
															$scope.filepreview = "";
															$scope.patientImage1 = null;
															$scope.patientImage2 = null;
															$scope.patientImage3 = null;
															$scope.snapshotData = "";
															$('#pieChartContainer').html(''); //remove canvas from container
															$('#pieChartContainer').html('<canvas id="snapshot1" height="217" width="217"></canvas>'); 
															angular.element("input[type='file']").val(null);
															$rootScope.stopLoader();
														}else{
															$rootScope.stopLoader();
														}
													});
							}else{
								$scope.filepreview = "";
								$scope.snapshotData = "";
								$('#pieChartContainer').html(''); //remove canvas from container
								$('#pieChartContainer').html('<canvas id="snapshot1" height="217" width="217"></canvas>'); 
								$scope.patientImage1 = null;
								angular.element("input[type='file']").val(null);
							}
						}
					    
					    $scope.storePatientDetails = function()
						{
							console.log("PatientOBJ",$scope.patientObj);
							console.log("Kin Details",$scope.kinDetailsList);
//							$scope.patientObj.kinList=$scope.kinDetailsList;
							if ($scope.popUpFlag) {
								$scope.showModal = !$scope.showModal;

							} else {
								
									if($scope.patientImage1!=undefined &&  !angular.equals({}, $scope.patientImage1) && $scope.patientImage1 != "")
									   {
										     var ext = $scope.patientImage1.name.split(".").pop().toLowerCase();
												if($.inArray(ext, ["jpg","jpeg",'png']) == -1) {
													growl.warning('You can only upload *.jpg, *.jpeg, *.png',
															{
																title : 'warning!'
															});
													$rootScope.stopLoader();
													return false;
												}
									   }
								
								var flag=false;
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								$scope.patientObj.kinDetailsId = $scope.unknownPatient.kinDetailsId != "" ? $scope.unknownPatient.kinDetailsId : 0;
								$scope.patientObj.createdDate = $scope.currentDate;
								$scope.patientObj.updatedDate = $scope.currentDate;
								$scope.patientObj.registrationTypeId=1,
								$scope.patientObj.organizationId = $scope.organizationId,
								$scope.patientObj.unitId = $scope.unitId,
								$scope.patientObj.firstName =   $rootScope.capitalize_Words($scope.patientObj.firstName);
								$scope.patientObj.middleName =  ($scope.patientObj.middleName == null || $scope.patientObj.middleName.trim() == "" || $scope.patientObj.middleName == undefined ?" ": $rootScope.capitalize_Words($scope.patientObj.middleName));
								$scope.patientObj.lastName =   ($scope.patientObj.lastName == null || $scope.patientObj.lastName.trim() == "" || $scope.patientObj.lastName == undefined ?" ": $rootScope.capitalize_Words($scope.patientObj.lastName));
								$scope.patientObj.dob = moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss');
								/*if($scope.datepickerExpiry != '')
									{
										$scope.patientObj.identificationExpiryDate = moment($scope.datepickerPdd).format('DD-MM-YYYY HH:mm:ss');
									}*/
								var ConDate = $('#datepickerExpiry').val();
								//if(ConDate != '')
									//$scope.patientObj.identificationExpiryDate = moment($scope.datepickerExpiry).format('DD-MM-YYYY HH:mm:ss');
								if($scope.expiryDateDisabled==false && (ConDate != '' && ConDate != undefined)){
									var newExDate = new Date($rootScope.getChangedFormatedDateShalseTwo(ConDate));
									$scope.patientObj.identificationExpiryDate = moment(newExDate).format('DD-MM-YYYY HH:mm:ss');
								}
								
//								 var splittedStringArray = $scope.patientObj.identificationTypeId.split("_");
//								 $scope.patientObj.identificationTypeId = splittedStringArray[0]; 
								if($scope.patientObj.identificationTypeId==undefined || $scope.patientObj.identificationTypeId=="")
									$scope.patientObj.identificationTypeId=null;
								
								if($scope.patientObj.identificationTypeId !=null)
								{
							      var splittedStringArray = $scope.patientObj.identificationTypeId.split("_");
							      $scope.patientObj.identificationTypeId = splittedStringArray[0];
							      if(splittedStringArray[1]=='Y'){
								      if($scope.patientObj.identificationExpiryDate==undefined || $scope.patientObj.identificationExpiryDate==null || $scope.patientObj.identificationExpiryDate==""){
										   flag=true;
									   }
							      }
								}
								else
								{
									$scope.patientObj.identificationTypeId = null;
								}
								if(flag){
									growl.error(
											'Please select expiry date',
											{
												title : 'Error!'
											});
									return;
								}
								 console.log($scope.patientObj);
//								return false;
								 $rootScope.startLoader();
								 
								 if($scope.sameAsLocal == 'Y' || $scope.sameAsLocal == 'N'){
									 
										$scope.patientObj.permanentAddress = $scope.patientObj.address;
										$scope.patientObj.permanentCountryId = $scope.patientObj.countryId;
										$scope.patientObj.permanentStateId = $scope.patientObj.stateId;
										$scope.patientObj.permanentDistrictId = $scope.patientObj.districtId;
										$scope.patientObj.permanentCityId = $scope.patientObj.cityId;
										$scope.patientObj.permanentAreaId = $scope.patientObj.areaId;
										$scope.patientObj.permanentZipCode = $scope.patientObj.zipCode;
										$scope.patientObj.permanentPhoneCode = $scope.patientObj.phoneCode;
										$scope.patientObj.permanentPhoneNumber = $scope.patientObj.phoneNumber;
										}
								 
								 
							
								var data={
										patientRegistrationDto:$scope.patientObj,
										kinArray:$scope.kinDetailsList,
										kinList :[],
										accompaniedBy : $scope.unknownPatient.accompaniedName,
										priorityId : $scope.unknownPatient.priorityId,
										isMedicoLegal : $scope.unknownPatient.isMedicoLegal,
										status : 'A',
										createdBy : $scope.userId,
										updatedBy : $scope.userId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										createdDate:$scope.currentDate,
										updatedDate:$scope.currentDate,
										visitTypeId:4,
										admissionTypeId : 4,
										kinDetailsId : $scope.unknownPatient.kinDetailsId != "" ? $scope.unknownPatient.kinDetailsId : 0,
										tUhid : $scope.unknownPatient.uhIdNumber,
										remark : $scope.unknownPatient.remark,
										modeOfArrival : $scope.unknownPatient.modeOfArrival,
										defaultSelfTariffId : $scope.defaultSelfTariffId,
										payeeId:$scope.selfPayeeId
									}
								console.log('$scope.paymentEntitleMentList',$scope.paymentEntitleMentList);
								var sponserList = [];
								angular.forEach($scope.paymentEntitleMentList, function (value, key) {
									var newArr = {};
									newArr.organizationId = $scope.organizationId ;
									newArr.unitId = $scope.unitId;
									newArr.visitTypeId = 4;
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
									newArr.claimNumber = value.claimNumber;
									if(value.priority == 1)
										{
											data.companyId = value.companyId;
											data.contractId = value.contractId;
											data.paymentEntitlementId = value.paymentEntitlementId;
										}
									sponserList.push(newArr);
								});
								data.patientSponsorDetailsDtosList = sponserList;
								
								
									var URI = BASE_URL + ROOT_URL
											+ ADDUNKNOWNPATIENT;
								
									BillingGenericService
											.serviceAction(METHOD_POST,
													URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														$rootScope.stopLoader();
														if (response.data.status == "success")
														{
															$scope.$broadcast('show-errors-reset');
															growl.success(
																	'Patient Registered Successfully...!!',
																	{
																		title : 'Success!'
																	});
															if($scope.patientImage1!=undefined &&  !angular.equals({}, $scope.patientImage1))
															{
															  $scope.uploadPatientImage($scope.patientImage1,response.data.object,$scope.organizationId,$scope.unitId);
															}
															
															var checkPatientMapOrNot = true;
															 angular.forEach($scope.encounterDetailsContractList, function(value, key) {  
																 if(value.patientId !=undefined &&  value.patientId !=null && value.patientId !='' ){
																	 checkPatientMapOrNot = false;
																 }else{
																	 value.patientId = response.data.object.patientId;
																 }
																 
															 });
															 
															 if(checkPatientMapOrNot == true){
																 $scope.uploadContractDoc();
															 }
															 
															//document upload against admission start 
															if($scope.allDocumentFinalList != undefined && $scope.allDocumentFinalList != null && $scope.allDocumentFinalList.length>0){
																 $scope.count=0;
																 angular.forEach($scope.allDocumentFinalList, function(value, key) 
																			{  									
																	 			value.admissionId=response.data.object.admissionId;
																	 			value.patientId=response.data.object.patientId;
																				var promise = $scope.uploadEncounterContractDoc(value);
																				promise.then(function(response) 
																						{
																						  if(response.data.status == "success")
																							  {
																							    $scope.count++;
																							    if($scope.finalDocUploadList.length==$scope.count)
																								 {
																								  
																								   $rootScope.stopLoader();
																								   $scope.encounterDetailsContractList = [];
																								   $scope.finalDocUploadList=[];
																								   $scope.allDocumentFinalList=[];
//																								   $scope.getPatientDocumentList();
																								 }
																						      }
																						  else 
																							{																						
																								 $rootScope.stopLoader();
																							}
																					    });
																					
																			});
															}
															
															if($scope.snapshotData!=undefined && $scope.snapshotData!="")
															{
															  var file =$scope.convertBase64ToImage($scope.snapshotData,moment().toDate().getYear()+".jpeg");
															  $scope.uploadPatientImage(file,response.data.object,$scope.organizationId,$scope.unitId);
															}
																$state.reload();
																$scope.patientObj={};
																$scope.patientObj.patientCategoryId='1',
																$scope.datepicker="";
																$scope.age = "";
																$scope.filepreview = "";
																$scope.patientImage1="";
																$scope.file="";
																$scope.initKinDetails();
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
																			'companyId':$scope.selfPayeeId,
																			'tariffId':$scope.defaultSelfTariffId,
																			'contractId':null,
																			'typeId' : 1,
																			'insuredAmount' : '',
																			'billingBedCategoryId':null
																		}];
																if($scope.unitCountryId > 0)
																{
																  $scope.patientObj.countryId  = $scope.unitCountryId;
																  $scope.patientObj.stateId  =  $scope.unitStateId;
																  $scope.patientObj.districtId = $scope.unitDistrictId;
																  $scope.patientObj.cityId = $scope.unitCityId;
																  $scope.patientObj.areaId = $scope.unitAreaId;
																  $scope.getStateByCountry($scope.patientObj.countryId,'L');
																  $scope.getDistrictByState($scope.patientObj.stateId,'L');
																  $scope.getCityByDistrict($scope.patientObj.districtId,'L');
																  $scope.getAreaByCity($scope.patientObj.cityId,'L');
																}
																$scope.sameAsLocal='N';
																$scope.addressFlag = false;
														}
														else{
															growl.error(
																	'Error in patient Registration ...!!',
																	{
																		title : 'Error!'
																	});
														}
														$('.modal-backdrop').remove()
				                                        $(document.body).removeClass("modal-open");
													});
							
									
							}
						}
					    //MLC image upload
					    $scope.uploadPatientMLCImage = function()
					    {
						  $rootScope.startLoader();
								var promise = $scope.uploadMLCEncounterDoc($scope.admissionList);
								promise.then(function(response) 
										{
										  if(response.data.status == "success")
											  {
//												   growl.success("Document Uploaded Successfully.",
//															{
//																title : 'Success!'
//															});
												   $rootScope.stopLoader();
												 }
									    });
									
						
					    }
						$scope.uploadMLCEncounterDoc = function(Obj)
						{
							 var fd = new FormData();
						        fd.append('encounterId', 0);
						        fd.append('visitTypeId', Obj.visitTypeId);
						        fd.append('admissionId', Obj.admissionId);
						        fd.append('patientId',Obj.patientId );
						        fd.append('documentTypeId', 35);
						        fd.append('createdBy',Obj.createdBy );
						        fd.append('updatedBy', Obj.updatedBy);
						        fd.append('unitId', Obj.unitId);
						        fd.append('organisationId', Obj.organizationId);
						        fd.append('uploadDoccumentDate', moment(Obj.createdDate).format('DD-MM-YYYY HH:mm:ss'));
						        fd.append('file',$scope.patobj.patientImage1);
						        fd.append('expenseId', 0);
						        fd.append('grnId',0);
								fd.append('poId',0);
						        fd.append('incidentReportingMapperId',0);
						        fd.append('companyExpenseId',0);
						        fd.append('createdDate',null);
								fd.append('fileEditedName',null);
								fd.append('opbId',0);
								fd.append('vendorId',0);
						        var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
						        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
								{	
									return response;
								});
						}
						
						 $scope.cancelPatientImgMLC = function(){
									$scope.patobj.filepreviewMLC = "";
									$scope.patobj.patientImage1 = null;
									angular.element("input[type='file']").val(null);
									 
							}

						 $scope.updateClaimNumber=function(info){
								
								$rootScope.startLoaderPopup();
								if(info.patientSponsorDetailsId == null){
									growl.warning("The record first you have to save then update !!!",
											{
												title : 'Warning!'
											});
									$rootScope.stopLoaderPopup();
									return false;
								}
								var data ={
										
										claimNumber:info.claimNumber,
										patientSponsorDetailsId:info.patientSponsorDetailsId,
								};
							if(info.claimNumber != null && info.claimNumber !="" && info.claimNumber != undefined){
								var URI = BASE_URL + ROOT_URL + "/api/opd/updatePatientSponserDetailsClaimNumber";
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success')
									{
										$rootScope.stopLoaderPopup();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoaderPopup();
										growl.warning(response.data.message,
												{
													title : 'Warning!'
												});
									}
								});
								
							}
							else{
								$rootScope.stopLoaderPopup();
								 growl.warning(" Please enter claim number. ",
											{
												title : 'warning!'
											});
							}
								
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

							$scope.normalERFunction = function (flag) {
								if (flag === "ER-Registration") {
								    $scope.init();
									$state.go('patientRegistration', {
										screenType: flag,
										patientObj: angular.copy($scope.patientObj)
									});
								}
							};



						} ]);
