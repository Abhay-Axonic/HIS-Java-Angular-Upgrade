'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:admittedPatientListController
 * @description # admittedPatientListController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'admittedPatientListController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'BillingGenericService',
						'growl',
						'$state',
						'$window',
						'reportService',
						'GenericServiceFileUpload',
						'$compile',
						'loyaltiCardCallFactory',
						'PagerService',
						'GenericServiceParamHeader',
						'$location',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,BillingGenericService, growl,
								$state,$window,reportService,GenericServiceFileUpload,$compile,loyaltiCardCallFactory,PagerService,GenericServiceParamHeader,$location) {

							/* init() function for form object create. */
							$scope.unitName="";
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.mpesaRequired = cookieObject.mpesaRequired;
							$scope.mpesaRigths = JSON.parse(localStorage.getItem('mpesaRigths'));
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.unitName=cookieObject.unitNme;
							$scope.unitCountryId = cookieObject.unitCountryId;
							$scope.unitStateId = cookieObject.unitStateId;
							$scope.unitDistrictId = cookieObject.unitDistrictId;
							$scope.unitCityId = cookieObject.unitCityId;
							$scope.unitAreaId = cookieObject.unitAreaId;
							$scope.unitZipCode = cookieObject.unitZipCode;
				             $scope.extendPdd = cookieObject.extendPdd;
							$scope.init = function() {
								$scope.dietTypeId = null;
								$scope.mealPrefId = null;
								$scope.dietRemarkDesc = '';
								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
								$scope.selectedTemplates = [];
								$scope.resend =false;
								$scope.send =false;
								$scope.validatTextFieldFlag = true;
								var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
                                var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));

								 if(cookieObjectAppConfig == undefined){
								  $state.go('login');
								  return;
								 }
								 else
								  {
								   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
								   
								  }
								$scope.isPinPadRequired = cookieObjectAppConfig.isPinPadRequired;
						  		$scope.registeredPinPadDevice= cookieObjectAppConfig.registeredPinPadDevice;
								$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
								if (cookieObjectAppConfigSecond != undefined) {
								$scope.admissionCancellationTime=cookieObjectAppConfigSecond.admissionCancellationTime;
								}
								$scope.cashAmountLimit =  cookieObjectAppConfig.cashAmountLimit;
								$scope.dateFormat = cookieObjectAppConfig.dateFormat;
								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								$scope.dateOptionsTrancs = {
										formatYear : 'yyyy',
										showWeeks : false,
										minDate : new Date()
									};
								
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								$scope.serviceName = "";
								
								
								$scope.admittedPatient = {};
								$scope.itemSelecteds = {};
								$scope.visitorPassPopUpFlag = false;
								$scope.consentFormPopUpFlag = false;
								$scope.commonPackagePopUpFlag = false;
								$scope.viewBillPopUpFlag = false;
								$scope.depositeMatrixPopUpFlag = false;
								$scope.commonUploadDocPopUpFlag = false;
								$scope.insurancePopUpFlag = false;
								$scope.assignContractPopUpFlag = false;
								$scope.barCodePopUpFlag = false;
								$scope.wristPopUpFlag = false;
								$scope.viewBillListModalPopUpFlag = false;
							    $scope.flagForSale=false;
							     $scope.flagForPay=false;
							     $scope.flagForBharatQr=false;

								
								$scope.options =  {
							          width: 1,
							          height: 15,
							          displayValue: false,
							          font: 'monospace',
							          textAlign: 'center',
							          textPosition: 'bottom',
							          fontSize: 10,
							          backgroundColor: '#ffffff',
							          lineColor: '#000000',
							          margin: 0
							        };
								$scope.entitlementList = [{
										entitlement:'Self Entitlement',
										entitlementId:1
								},
								{
									entitlement:'Insurance',
									entitlementId:2
								},
								{
									entitlement:'Company',
									entitlementId:3
								}
								]

								var data = {};
								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEADMISSIONTYPELIST+"/"+$scope.organizationId;
						GenericService
								.serviceAction("GET", URI, {})
								.then(
										function(response) {
											$scope.admissionTypeList = [];

											if (response.data.status == "success") {
												$scope.admissionTypeList = response.data.listObject;
												console.log("$scope.admissionTypeList",$scope.admissionTypeList);

											}
										});
						

						// 
						          $scope.getDietList=function(){
						        	  var data1 = {
										        organizationId:$scope.organizationId,
												//offset:offset,
												//noOfRecordsPerPage:noOfRecordsPerPage,
												unitId:$scope.unitId
												};
									    $scope.dietList=[];
										var URI1 = BASE_URL + ROOT_URL + "/api/kitchen/master/dietTypeMaster/getDietTypeMasterPaginationList"; 
										GenericService.serviceAction("POST",URI1,data1).then(function(response){
																	if(response.data.status == "success"){
																	$rootScope.stopLoader();					
																	$scope.dietList = response.data.listObject;
																	console.log("dietList",$scope.dietList);
																	console.log("commonList",$scope.commonList);
																	}
																});
						          }
						          
						          $scope.getDietList();
						
											
						
						
						
						// Search Surgen with Typehead
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
						
						$scope.selectDoctor = function($item, $model, $label, idx) {
							$scope.search.doctorName = $item.doctorName;
							$scope.search.doctorId = $item.doctorId;
							$scope.getAdmittedPatientByFloorWardId();
						}

						
								/*var URI = BASE_URL + ROOT_URL + GETACTIVEVISITORPASSTYPELIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.visitorPassTypeList = [];

													if (response.data.status == "success") {
														$scope.visitorPassTypeList = response.data.listObject;

													}
												}); */

							/*	var URI = BASE_URL + ROOT_URL
										+ GETADMITTEDPATIENTLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.listOfAdmittedPatientList = [];
													$scope.admittedPatientList = [];

													if (response.data.status == "success") {
//														$scope.admittedPatientList = response.data.listObject;
														$scope.listOfAdmittedPatientList = response.data.listObject;
														angular
																.forEach(
																		$scope.admittedPatientList,
																		function(
																				value,
																				index) {

																			var newArr = {};
																			newArr.specialityName = value.specialityName;
																			newArr.doctorFullName = value.dFirstName;
																			newArr.patientFullName = value.pFirstName;
																			newArr.reasonDesc = value.reasonDesc;
																			newArr.dob = value.dob;
																			newArr.datepicker = value.doa;
																			newArr.datepickerPdd = value.pdd;
																			newArr.genderCode = value.genderCode;
																			newArr.uhidnumber = value.uhidnumber;
																			newArr.patientId = value.patientId;
																			newArr.tPatientId = value.tPatientId;
																			newArr.paymentEntitlementDesc = value.paymentEntitlementDesc;
																			newArr.bedNumber = value.bedNumber;
																			newArr.admissionId = value.admissionId;
																			newArr.prefixCode = value.prefixCode;
																			newArr.identificationNo = value.identificationNo;
																			newArr.wardName = value.wardName;
																			newArr.concent = value.concent;
																			newArr.bedCategoryId=value.bedCategoryId;
																			newArr.patientCategoryId=value.patientCategoryId;
																			newArr.visitTypeId=value.visitTypeId;
																			newArr.paymentEntitlementId=value.paymentEntitlementId;
																			newArr.isBarCodeSelected =false;
																			$scope.listOfAdmittedPatientList
																					.push(newArr);

																		});

													}
												});*/

								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEFLOORLIST;
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													$scope.activeFloorList = [];

													if (response.data.status == "success") {
														$scope.activeFloorList = response.data.listObject;

													}
												});
								$scope.visitPassObj();
								
								var commonObj = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
									
									var URI = BASE_URL + ROOT_URL
									+ GETACTIVESPECIALITY;
						
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

							}
							
							$scope.openDatePickerPdd = function() {
								$scope.datepickerPdd.opened = true;
							};

							$scope.datepickerPdd = {
								opened : false
							};
							
							
							var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};

							var URI = BASE_URL + ROOT_URL
							+ GETACTIVEADMISSIONTYPELIST+"/"+$scope.organizationId;
					        GenericService.serviceAction("GET", URI, {})
							.then(
									function(response) {
										$scope.admissionTypeList = [];

										if (response.data.status == "success") {
											$scope.admissionTypeList = response.data.listObject;
											console.log("$scope.admissionTypeList",$scope.admissionTypeList);

										}
									});
					
					   
								
								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEREASONLIST;
						
						BillingGenericService
								.serviceAction(METHOD_POST, URI,
										commonObj,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.reasonList = response.data.listObject;
										});
						
						var URI = BASE_URL + ROOT_URL
						+ GETACTIVEMEALPREFERENCELIST;
				GenericService
						.serviceAction("POST", URI, commonObj)
						.then(
								function(response) {
									$scope.mealPreferenceList = [];

									if (response.data.status == "success") {
										$scope.mealPreferenceList = response.data.listObject;

									}
								});
				
				$scope.getReferralList = function(){
					var URI = BASE_URL + ROOT_URL + GETACTIVEREFERRALTYPELIST;
					var obj={organizationId : $scope.organizationId};
						BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
								$scope.referralTypeList = [];
								if (response.data.status == "success") {
									$scope.referralTypeList = response.data.listObject;
								}
						});
					};
				$scope.getReferralList();
				
				$scope.searchByreferralId = function(searchKeyword,index){
		        	
					var data={
		        			organizationId: $scope.organizationId,
		        			referralTypeId:$scope.admissionRequestOrAdmissionObj.referralTypeId,
		        			desc: searchKeyword
		        	};
					var URI = BASE_URL + ROOT_URL + "/adt/getReferralListByReferralTypeId/forTypeSearch" 
					
		    		return GenericService.serviceAction("POST",URI,data).then(function(response){
		    			if(response.data.status == "success"){
		    				$scope.referralMasterList = [];
		    				$scope.referralMasterList = response.data.listObject;
		    			}
		    			return $scope.referralMasterList;
		    		});
		    	}
		        
				
		        
		    	$scope.selectReferralId = function(info, model, label,ind) {
					console.log(info);
					$scope.admissionRequestOrAdmissionObj.referralId = info.referralId;
		    	}
		    	
		    	  // object for Diagnosis List added by sneha
			     $scope.dataListDiagnosis = [ {
						diagnosisId : '',
						diagnosisType:'1',
						type : '1',
						remark : '',
						diagnosisNameText:'',
						isDiagnosisName : 'N',
						diagnosisCode:'',
						diagnosisName :null
					}];
			    
			     
		    	$scope.initKinDetails = function(){
			  		  /* Add Next Of Kin Details */
		    		$scope.tab = 2;
			    	$scope.patientObj = {};
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
			  				countryId 		: $scope.unitCountryId!=undefined && $scope.unitCountryId !=null ? $scope.unitCountryId : '',
			  				stateId 		:  $scope.unitStateId!=undefined && $scope.unitStateId !=null ? $scope.unitStateId : '',
			  				districtId 		:  $scope.unitDistrictId!=undefined && $scope.unitDistrictId !=null ? $scope.unitDistrictId : '',
			  				cityId 			: $scope.unitCityId!=undefined && $scope.unitCityId !=null ? $scope.unitCityId : '',
			  				areaId 			: $scope.unitAreaId!=undefined && $scope.unitAreaId !=null ? $scope.unitAreaId : '',
			  				postCode 		: '',
			  				patientId       : '',
			  				unitId			: $scope.unitId,
			  				organizationId  : $scope.organizationId,
			  				createdBy		: $scope.userId,
			  				updatedBy		: $scope.userId,
//			  				expiryDateDisabled 		: true,
			  				createdDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
			  				updatedDate		: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
			  			};
			  		
			  		$scope.kinDetailsList.push(angular.copy($scope.kinDetails));
			  	}
			  	$scope.initKinDetails();
			  	
			  	
			    $scope.addNewKinDetails = function(){
			    	$scope.kinDetails.patientId = $scope.admissionRequestOrAdmissionObj.patientId;
			    	$scope.validateNexK=false;
			    	angular.forEach($scope.kinDetailsList,function(value,index){
			    		if(value.prefixId == "" || value.kinName == "" 
			    			|| value.relationId == "" || value.mobileNo == "")
			    		{
			    			$scope.validateNexK=true;
			    			growl.error("Please enter details ",{
								title : ERROR_MSG
								});
			    			return false;
			    		}
			    	});
			    	
			    	if(!$scope.validateNexK){
			    		$scope.kinDetailsList.push(angular.copy($scope.kinDetails));
			    	}
			    	
			    };
			    $scope.removeKinDetails = function(indexToBeDeleted){	
					  $scope.kinDetailsList.splice(indexToBeDeleted, 1);
			    };
			    
			    
				$scope.getPrefixList = function(){
					var URI = BASE_URL + ROOT_URL + GETACTIVEPREFIXLIST;
					BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(
							function(response) {
								$scope.prefixList = [];
								if (response.data.status == "success") {
									$scope.prefixList = response.data.listObject;
								}
							});
				}
				$scope.getPrefixList();
				
				
				$scope.getMLCPriorityList=function(){

					var URI = BASE_URL + ROOT_URL+ "/adt/getActivePriorityList";
					GenericService.serviceAction("GET", URI, {})
					.then(function(response) {
						$scope.priorityListForMLC = [];
						$scope.priorityListForMLC = response.data.listObject;
					});
				}

				$scope.getMLCPriorityList();

				
				
				// Country List
				$scope.getCountryList = function(){
					var URI = BASE_URL + ROOT_URL + GETACTIVECOUNTRYLIST+$scope.organizationId;
					BillingGenericService.serviceAction("GET", URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								$scope.countryList = [];
								if (response.data.status == "success") {
									$scope.countryList = response.data.listObject;
								}
							});
				}
				$scope.getCountryList();
				//get calling code by using country id start
				$scope.getCallingCode=function(data){
					var URI = BASE_URL + ROOT_URL
					+ GETSINGLECOUNTRY;
					GenericService.serviceAction("POST", URI, data).then(
							function(response) {
								if (response.data.status == "success" && response.data.listObject.length >0) {
									$scope.countryCallingCode = response.data.listObject[0].countryCallingCode;
									
									$scope.patientObj.countryCallingCode = response.data.listObject[0].countryCallingCode;
									$scope.patientObj.whatsappCallingCode = response.data.listObject[0].countryCallingCode;
									
									//if(type=="PA"){
										$scope.permanentPhoneCode = response.data.listObject[0].countryCallingCode;
									//}else{
										$scope.phoneCode = response.data.listObject[0].countryCallingCode;
									//}
								} else {
									//alert("Error!!");
								}
							});
				}
				
				$scope.getNationalityByContryId=function(data){
					data.organizationId = $scope.organizationId;
					var URI = BASE_URL + ROOT_URL
					+ "/adt/getNationalityByCountryId";
					GenericService.serviceAction("POST", URI, data).then(
							function(response) {
								if (response.data.status == "success" && response.data.listObject.length > 0) {
									console.log("nationality List ",response.data.listObject[0]);
									$scope.patientObj.nationalityId = response.data.listObject[0].nationalityId;

								} else {
//									alert("Error!!");
								}
							});
				}
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
										/*if($scope.sameAsLocal == 'Y'){
											$scope.patientObj.permanentCountryId = id;
										}*/
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
											
										}else
										{
										$scope.CompanyStateList = [];
										$scope.CompanyStateList = response.data.listObject;
										}
								}
								$scope.stopLoader();
							});

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
									
								}else
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
					var data = {
						cityId : id
					};
					var URI = BASE_URL + ROOT_URL
							+ GETAREALISTBYCITY;
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
									 $scope.areaList = response.data.listObject;
									console.log($scope.areaList)
									$scope.patientObj.areaId="";
									$scope.patientObj.zipCode="";
									}
									else if(type == 'PA')
									{
									$scope.permanentAreaList = [];
									$scope.permanentAreaList = response.data.listObject;
									
									$scope.patientObj.permanentAreaId="";
									$scope.patientObj.permanentZipCode="";
									}else if(type == 'L')
									{
										$scope.areaList = [];
										 $scope.areaList = response.data.listObject;
										console.log($scope.areaList)
									}else if(type == 'LTOP')
									{
										$scope.permanentAreaList = [];
										$scope.permanentAreaList = response.data.listObject;
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
				
				//relation list
				$scope.getRelationshipList = function(){
					var URI = BASE_URL + ROOT_URL + GETACTIVERELATIONLIST;
					BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								$scope.relationList = [];
								if (response.data.status == "success") {
									$scope.relationList = response.data.listObject;
								}
							});
				}
				$scope.getRelationshipList();
				
				if($scope.unitCountryId > 0)
				{
				
				  $scope.getStateByCountry($scope.unitCountryId,'L');
				  $scope.getDistrictByState($scope.unitStateId ,'L');
				  $scope.getCityByDistrict($scope.unitDistrictId,'L');
				  $scope.getAreaByCity($scope.unitCityId,'L');
				}
				
			    	$scope.kinsList=[];
			    	
			    	//kin details
			    	$scope.getKinDetailsByPatientId = function(){
			    		var URI = BASE_URL + ROOT_URL + KIN_DETAILS + PATIENT + SLASH + $scope.admissionRequestOrAdmissionObj.patientId  + UNIT + SLASH +  $scope.unitId  + ORG + SLASH + $scope.organizationId;
			    		BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			    			if (response.data.status == "success") {
			    				$scope.kinsList = response.data.listObject;
			    			}
			    		});
			    	}
			    	
			    	$scope.getKinDetailsById = function(kinDetailsId){
			    		$rootScope.startLoader();
			    		var URI = BASE_URL + ROOT_URL + KIN_DETAILS + SLASH + kinDetailsId;
			    		BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			    					if (response.data.status == "success") {
			    						$scope.kinDetailsList[0]=(angular.copy(response.data.object));
			    						if($scope.kinDetailsList[0].identificationId > 0){
			    							$scope.kinDetailsList[0].identificationId = $scope.kinDetailsList[0].identificationId+'_'+$scope.kinDetailsList[0].isExpiry;
			    						}
			    						if($scope.kinDetailsList[0].countryId != null){
			    							$scope.getStateByCountry($scope.kinDetailsList[0].countryId);
			    						}
			    						if($scope.kinDetailsList[0].stateId != null){
			    						$scope.getDistrictByState($scope.kinDetailsList[0].stateId);
			    						}
			    						if($scope.kinDetailsList[0].districtId != null){
			    						$scope.getCityByDistrict($scope.kinDetailsList[0].districtId);
			    						}
			    						if($scope.kinDetailsList[0].cityId != null){
			    						$scope.getAreaByCity($scope.kinDetailsList[0].cityId);
			    						}
			    						if($scope.kinDetailsList[0].expiry != null){
				    						 $scope.kinDetailsList[0].expiry = new Date($scope.kinDetailsList[0].expiry );
				    						}
			    						console.log($scope.kinDetailsList);
			    					}
			    				});
			    	}
			    	
			    	
			    	$scope.updateKinDetailsStatus = function(kinDetailsId){
			    		$rootScope.startLoader();
			    		var URI = BASE_URL + ROOT_URL + KIN_DETAILS + SLASH + kinDetailsId + STATUS + SLASH + 'I';
			    		BillingGenericService.serviceAction(METHOD_PUT, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			    			if (response.data.status == "success") {
			    				$scope.getKinDetailsByPatientId();
			    			}
			    		});
			    	}
			    	
			    	$scope.setKinId = function(kinId){
			    		$scope.admissionRequestOrAdmissionObj.kinDetailsId = kinId;
			    	}
			    	
					$scope.openDatePickerExpiry1 = function() {
						$scope.datepickerExpiry1.opened = true;
					};

					$scope.datepickerExpiry1 = {
						opened : false
					};
					
					
					$scope.getMedicolegalDetails = function()
					{
						$scope.medicolegalDetailsList=[];
						$rootScope.startLoader();
						var data = 
									{
										organizationId : $scope.organizationId,
										admissionId : $scope.admissionRequestOrAdmissionObj.admissionId
									};
						
			    		var URI = BASE_URL + ROOT_URL + "/adt/getMedicolegalDetailsForIcon" ;
			    		GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE)
			    		.then(
			    			function(response) {
			    				if (response.data.status == "success") 
			    				{
			    					$scope.medicolegalDetailsList = response.data.listObject;
			    					$scope.admissionRequestOrAdmissionObj.isMedicoLegal = 'N';
			    					if($scope.medicolegalDetailsList.length > 0){
				    					$scope.medicoLegalId = $scope.medicolegalDetailsList[0].medicoLegalId;
				    					$scope.admissionRequestOrAdmissionObj.isMedicoLegal = 'Y';
				    					$scope.admissionRequestOrAdmissionObj.accompaniedName=$scope.medicolegalDetailsList[0].accompaniedBy;
				    					$scope.admissionRequestOrAdmissionObj.priorityId=$scope.medicolegalDetailsList[0].priorityId != null ? $scope.medicolegalDetailsList[0].priorityId.toString() : "";
				    					$scope.admissionRequestOrAdmissionObj.mlcRelationId=$scope.medicolegalDetailsList[0].relationId != null ? $scope.medicolegalDetailsList[0].relationId.toString() : "";
				    					$scope.relationName=$scope.medicolegalDetailsList[0].relationName;
//				    					$scope.admissionRequestOrAdmissionObj.mlcRelationId = $scope.medicolegalDetailsList[0].mlcRelationId;
				    					$scope.admissionRequestOrAdmissionObj.mlcContactNo = $scope.medicolegalDetailsList[0].mlcContactNo;
				    					$scope.admissionRequestOrAdmissionObj.policeStationName = $scope.medicolegalDetailsList[0].policeStationName;
				    					$scope.admissionRequestOrAdmissionObj.policeBuckleNo = $scope.medicolegalDetailsList[0].policeBuckleNo;
				    					$scope.admissionRequestOrAdmissionObj.firNo = $scope.medicolegalDetailsList[0].firNo;
				    					$scope.admissionRequestOrAdmissionObj.remark = $scope.medicolegalDetailsList[0].remark;
			    					}else{
			    						$scope.medicoLegalId =null;
			    					}
			    					console.log("medicolegalDetailsList", $scope.medicolegalDetailsList);
			    					$rootScope.stopLoader();
			    				}
			    				
			    			});
					}
					
					
					$scope.updateMedicolegalDetails = function() {
					
							$scope.currentDate = new Date();
							$scope.currentDate = moment($scope.currentDate)
									.format('DD-MM-YYYY HH:mm:ss');
						
							var data = {
								accompaniedBy : $scope.admissionRequestOrAdmissionObj.accompaniedName,
								firNo        :$scope.admissionRequestOrAdmissionObj.firNo,
								policeBuckleNo :$scope.admissionRequestOrAdmissionObj.policeBuckleNo,
								policeStationName:$scope.admissionRequestOrAdmissionObj.policeStationName,
								mlcContactNo:$scope.admissionRequestOrAdmissionObj.mlcContactNo,
								mlcRelationId:$scope.admissionRequestOrAdmissionObj.mlcRelationId,
								mlcRelationId:($scope.admissionRequestOrAdmissionObj.mlcRelationId != "0" &&  $scope.admissionRequestOrAdmissionObj.mlcRelationId != null)?  $scope.admissionRequestOrAdmissionObj.mlcRelationId : null,
								priorityId : ($scope.admissionRequestOrAdmissionObj.priorityId != "0" &&  $scope.admissionRequestOrAdmissionObj.priorityId != null)?  $scope.admissionRequestOrAdmissionObj.priorityId : null,
								status : 'A',
								updatedBy : $scope.userId,
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								admissionId:$scope.admissionRequestOrAdmissionObj.admissionId,
								updatedDate:$scope.currentDate,
								medicoLegalId: $scope.medicoLegalId
							}
							
							var URI = BASE_URL + ROOT_URL
									+"/adt/updateMedicolegalDetails";
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
													$scope.getMedicolegalDetails();
													
												} 
													
												$rootScope.stopLoader();
											});
						
					

				}
					
					
				
					$scope.editPatientVisitDetails = function(info)
				    {
						
						$scope.patobj={
					    		patientImage1:"",
					    		filepreviewMLC : ""
					    }
						
						console.log("info",info);
						$scope.filepreview = "";
						$rootScope.startLoaderPopup();
						$scope.updateEditPatientVisitDetailsBtn = false;
						$scope.admissionRequestOrAdmissionObj={};
						$scope.admissionRequestOrAdmissionObj.patientId = info.patientId;
						$scope.admissionRequestOrAdmissionObj.admissionId = info.admissionId;      
						$scope.admissionRequestOrAdmissionObj.bedCategoryId = info.bedCategoryId;
						$scope.admissionRequestOrAdmissionObj.visitTypeId = info.visitTypeId;
						 $scope.admissionRequestOrAdmissionObj.treatingDoctorId=info.treatingDoctorId;
						 $scope.admissionRequestOrAdmissionObj.doctorId=info.admittingDoctorId;
						$scope.admissionRequestOrAdmissionObj.treatingDoctorName=info.treatingDoctorName;
						$scope.admissionRequestOrAdmissionObj.admittingDoctorName = info.admittingDoctorName;
						
		
						var data = {
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								admissionId : info.admissionId,
							};

						
						
							var URI = BASE_URL + ROOT_URL
									+ "/adt/editAdmissionDetailsGetById";
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {
												$scope.editDetailsList = [];
												if (response.data.status == "success") 
												{
													if(response.data.listObject != null && response.data.listObject.length > 0){
													$scope.editDetailsList = response.data.listObject;
													$scope.admissionRequestOrAdmissionObj.admissionTypeId =$scope.editDetailsList[0].admissionTypeId;
													$scope.admissionRequestOrAdmissionObj.admissionDetailsId = $scope.editDetailsList[0].admissionDetailsId;
													$scope.admissionRequestOrAdmissionObj.reasonId = $scope.editDetailsList[0].reasonId;
													$scope.datepickerPdd = moment($scope.editDetailsList[0].datepickerPdd ,"DD-MM-YYYY").toDate();
													$scope.admissionRequestOrAdmissionObj.specialityId=$scope.editDetailsList[0].specialityId;
													$scope.admissionRequestOrAdmissionObj.referalDetailsId = $scope.editDetailsList[0].referalDetailsId;
													$scope.admissionRequestOrAdmissionObj.referralDesc = $scope.editDetailsList[0].referralName;
													if($scope.editDetailsList[0].referralId != null){
													$scope.admissionRequestOrAdmissionObj.referralId = $scope.editDetailsList[0].referralId.toString();
													}
													if($scope.editDetailsList[0].mealPrefId != null){
													$scope.admissionRequestOrAdmissionObj.mealPrefId = $scope.editDetailsList[0].mealPrefId.toString();
													}
													if($scope.editDetailsList[0].attendentMealPrefId != null){
													$scope.admissionRequestOrAdmissionObj.attendentMealPrefId = $scope.editDetailsList[0].attendentMealPrefId.toString();
													}
													$scope.admissionRequestOrAdmissionObj.referralRemark = $scope.editDetailsList[0].referralRemark;
													$scope.referenceNumber = $scope.editDetailsList[0].referenceNumber;
													if($scope.editDetailsList[0].referralTypeId != null){
														$scope.admissionRequestOrAdmissionObj.referralTypeId = $scope.editDetailsList[0].referralTypeId.toString();
														
													}
													

													 $scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId=response.data.listObject[0].treatingDoctorSpecialityId;
													 $scope.admissionRequestOrAdmissionObj.specialityId=response.data.listObject[0].admittingSpecialityId;
													 $scope.admissionRequestOrAdmissionObj.treatingDoctorId= $scope.editDetailsList[0].treatingDoctorId;
													 $scope.admittingDoctorflag = false;
													 $scope.treatingDoctorflag = false;
													 $scope.getDoctorBySpeciality($scope.admissionRequestOrAdmissionObj.specialityId,$scope.admittingDoctorflag);
													 $scope.getDoctorBySpecialityForTreating($scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId,$scope.treatingDoctorflag);
													 $scope.admissionRequestOrAdmissionObj.doctorId= $scope.editDetailsList[0].admittingDoctorId;
													 $scope.admissionRequestOrAdmissionObj.treatingDoctorName= $scope.editDetailsList[0].treatingDoctorName;
													 $scope.admissionRequestOrAdmissionObj.admittingDoctorName= $scope.editDetailsList[0].admittingDoctorName;
													}
													console.log("editeddaata", $scope.editDetailsList);
													$rootScope.stopLoaderPopup();
												
													
												}
												else if (response.data.status == "error") {
													$rootScope.stopLoader();
												}
											});
							
					    $scope.initKinDetails();
						$scope.getKinDetailsByPatientId(); 
						$scope.getVisitorPassList(info);
						$scope.getMedicolegalDetails();
						$scope.getPatientMLCDocumentList();
						
						var data5 = {
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								admissionId : info.admissionId
							};
							
							$scope.visitPassObj();
							
							var URI = BASE_URL + ROOT_URL
									+ GETPASSISSUEDLIST;
							GenericService
									.serviceAction("POST", URI, data)
									.then(
											function(response) {
												$scope.passIssuedList = [];

												if (response.data.status == "success") {
													$scope.passIssuedList1 = response.data.listObject;
													$scope.listVisiterPassMaster =[];
													
													angular.forEach($scope.passIssuedList1 , function(value,key){
														$scope.visiterPassMaster = {};
														$scope.visiterPassMaster.visitorPassTypeId = value.visitorPassTypeId;
														$scope.visiterPassMaster.passNumber = value.passNumber;
														$scope.visiterPassMaster.visitorPatientId = value.visitorPatientId;
														$scope.visiterPassMaster.visitorPatientMapperId = value.visitorPatientMapperId;
														$scope.listVisiterPassMaster.push($scope.visiterPassMaster);
													})
													
												}
												
											});
							
							var abc="data:image/png;base64,";
							if($scope.patientObj.bucketFlag == undefined || $scope.patientObj.bucketFlag == 1 ){
								if( $scope.patientPreviosMLCDocumentList != null && $scope.patientPreviosMLCDocumentList.length > 0){
									
									$scope.patobj.filepreviewMLC = $scope.patientPreviosMLCDocumentList[0].documentPath;
								}

							}else{
								if( $scope.patientPreviosMLCDocumentList != null && $scope.patientPreviosMLCDocumentList.length > 0){
									   $scope.patobj.filepreviewMLC = abc+$scope.patientPreviosMLCDocumentList[0].documentPath;
										}

							  }
				    }
					
					
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
				
				//Admitting doctor speciality
//				$scope.getDoctorBySpeciality = function(id) {
//
//					var data = {
//						specialityId : id,
//						unitId : $scope.unitId,
//						organizationId : $scope.organizationId
//					};
//					var URI = BASE_URL + ROOT_URL
//							+ GETDOCTORBYSPECIALITYID;
//					
//					BillingGenericService
//							.serviceAction(METHOD_POST, URI, data,
//									NOTIFICATION_MSG_STATUS_FALSE)
//							.then(
//									function(response) {
//										console.log(response);
//										$scope.doctorList = [];
//										// $rootScope.startLoader();
//										if (response.data.status == "success")
//											$scope.doctorList = response.data.listObject;
//									});
//
//				}

				
				//treating doctor
				$scope.getDoctorBySpeciality = function(id,admitingDoctorflag) {

					var data = {
						specialityId : id,
						unitId : $scope.unitId,
						organizationId : $scope.organizationId
					};
					var URI = BASE_URL + ROOT_URL
							+ GETDOCTORBYSPECIALITYID;
					
					BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.doctorList = [];
									if(admitingDoctorflag == true)
										{
										$scope.admissionRequestOrAdmissionObj.doctorId = "";
										}
									
									
										// $rootScope.startLoader();
										if (response.data.status == "success")
											$scope.doctorList = response.data.listObject;
									});

				}
				
				$scope.getDoctorBySpecialityForTreating = function(id, treatingDoctorFlagcheck) {
				var pddConfig = $.grep($scope.specialityList, function (mppr) {
				return mppr.specialityId == id;
				})[0].pddDays;
				var new_date = moment($scope.datepicker).add(pddConfig, 'days');
			//	$scope.datepickerPdd = new Date(new_date);
				$scope.patientDetails.patientDischargeDate = moment( new Date(new_date)).format('DD/MM/YYYY');
				var dateDoa = moment( $scope.datepicker, 'DD/MM/YYYY');
				var datePdd = moment(new Date(new_date), 'DD/MM/YYYY');
				$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
				var data = {
				specialityId : id,
				unitId : $scope.unitId,
				organizationId : $scope.organizationId
				};
				var URI = BASE_URL + ROOT_URL
				+ GETDOCTORBYSPECIALITYID;
				BillingGenericService
				.serviceAction(METHOD_POST, URI, data,
				NOTIFICATION_MSG_STATUS_FALSE)
				.then(
				function(response) {
				console.log(response);
				$scope.doctorListTreating= [];
			
				if(treatingDoctorFlagcheck == true){
					$scope.admissionRequestOrAdmissionObj.treatingDoctorId = "";
				}
				// $rootScope.startLoader();
				
				if (response.data.status == "success")
				$scope.doctorListTreating = response.data.listObject;
				}); }
				
				$scope.getDoctorBySpecialityForTreatingRedirect = function(id) {
					
					var data = {
						specialityId : id,
						unitId : $scope.unitId,
						organizationId : $scope.organizationId
					};
					var URI = BASE_URL + ROOT_URL
							+ GETDOCTORBYSPECIALITYID;
					
					BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.doctorListTreating = [];
										// $rootScope.startLoader();
										if (response.data.status == "success")
											$scope.doctorListTreating = response.data.listObject;
									});

				}



		    	
							$scope.visitPassObj=function(){

					        	$scope.visiterPassMaster={
					        			"visitorPassTypeId":0,
					        			"passNumber":""
					        	}
					        	$scope.listVisiterPassMaster=[];
								var visiterPassMaster = angular.copy($scope.visiterPassMaster);
					        	$scope.listVisiterPassMaster.push(visiterPassMaster);
					        	
							}
							$scope.init();
							$scope.getWardByFloorId = function() {
								var commonData = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									floorId : $scope.search.floorId
								};

								var URI = BASE_URL + ROOT_URL
										+ GETWARDLISTBYFLOORIDORGUNITID;
								GenericService
										.serviceAction("POST", URI, commonData)
										.then(
												function(response) {
													$scope.wardList = [];

													if (response.data.status == "success") {
														$scope.wardList = response.data.listObject;

													}
												});
							}

							$scope.search = {
								floorId : 0,
								wardId : 0,
							}

							$scope.getAdmittedPatientByFloorWardId = function() 
							{

								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									floorId : $scope.search.floorId,
									wardId : $scope.search.wardId,
									paymentEntitlementId:$scope.search.entitlementId
									
								};
								
								data.doctorId = $scope.search.doctorId != undefined ?$scope.search.doctorId : null;
								data.admissionTypeId = $scope.search.admissionTypeId != undefined ? $scope.search.admissionTypeId :null;
								if($scope.search.admissionDate != undefined){
									data.doa = moment($scope.search.admissionDate).format("YYYY-MM-DD");	
								}

								$rootScope.startLoader();

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
														$rootScope.stopLoader();
														$scope.setPage(1,false);
														$scope.totalCountPatient  = response.data.object;
													}
													else if (response.data.status == "error") {
														$rootScope.stopLoader();
													}
												})
							}

							$scope.kinDetails = function(infoObj) {
								// alert(infoObj.admissionId);
								if(infoObj.patientId > 0)
								{
								var age = $rootScope
								.getAge(infoObj.dob);
								$scope.patientFullName = infoObj.patientFullName
								+ " "
								+age
								+ "/"
								+ infoObj.genderCode;
								}
							else
								{
								$scope.patientFullName = infoObj.patientFullName
								+ " "
								+ infoObj.dob
								+ "/"
								+ infoObj.genderCode;
								}
								
							
							$scope.uhidNum = infoObj.uhidnumber;
							$scope.doctorFullName =infoObj.doctorFullName;
								$scope.bedNumber = infoObj.bedNumber;
								$scope.dateOfAdmission = infoObj.datepicker;
								$scope.patientDischargeDate = infoObj.datepickerPdd;
								$scope.pddActual = $scope.patientDischargeDate;
								$scope.doaActual = $scope.dateOfAdmission;
								$scope.identificationNo = infoObj.identificationNo;
								$scope.admissionNumber =infoObj.admissionNumber;
								$scope.department =infoObj.specialityName;
								$scope.specialityName =infoObj.specialityName;

								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									patientId : infoObj.patientId
								}

								var URI = BASE_URL + ROOT_URL + "/api/global/kinDetails/patient/"+infoObj.patientId+"/unit/"+$scope.unitId+"/org/"+$scope.organizationId;
								GenericService
										.serviceAction("GET", URI, data)
										.then(
												function(response) {
													$scope.kinList = [];

													if (response.data.status == "success") {
														$scope.kinList = response.data.listObject;

													}
												});
							}
							
							
							$scope.getPatientSponcerDetails = function(admissionId,paymentEnId)
							{
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/opd/getSponsoredDetails/"+admissionId+"/"+paymentEnId;
								GenericService
										.serviceAction("GET", URI, {})
										.then(
												function(response) {
													$scope.paymentEntitlementList = [];
													if (response.data.status == "success") {
														$scope.paymentEntitlementList = response.data.listObject;
														$rootScope.stopLoader();
													}
													else if(response.data.status == "error")
														{ 
														  $rootScope.stopLoader();
														}
												});
							}
							$scope.selectedPatientId = "";
							$scope.visitTypeId = "";
							$scope.selectPatient = function(item) {

								$scope.itemSelecteds = [];
								// If checkbox is checked
								if ($scope.admittedPatient) {
									$scope.itemSelecteds = item;
									$scope.visitTypeId = item.visitTypeId;
									$scope.visitorPassPopUpFlag = true;
									$scope.consentFormPopUpFlag = true;
									$scope.commonPackagePopUpFlag = true;
									$scope.viewBillPopUpFlag = true;
									$scope.depositeMatrixPopUpFlag = true;
									$scope.commonUploadDocPopUpFlag = true;
									$scope.barCodePopUpFlag = true;
									$scope.wristPopUpFlag = true;
									$scope.insurancePopUpFlag = item.paymentEntitlementId==2?true:false;
									//temp change by dinesh on 18-03-19 start
									$scope.assignContractPopUpFlag = item.paymentEntitlementId==3?true:false;
									//temp change by dinesh on 18-03-19 end
									$scope.viewBillListModalPopUpFlag=true;

								}
								$scope.selectedPatientId = item.patientId;
								loyaltiCardCallFactory.getLoyaltyCardList(item.patientId,$scope);
								
							}
							var data = {};
							
							var URI = BASE_URL + ROOT_URL
							+ GETACTIVEIDENTIFICATION;
					GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
										$scope.identificationList = [];

										if (response.data.status == "success") {
											$scope.identificationList = response.data.listObject;
											

										}
									});
					
					
					//checking expire datepicker is show or not
					$scope.disableEnableExpiry = function(data,isExpiryVal,idx,flag)
					{
					   console.log(data)
					   if(flag==true ){
                                data.identificationNo="";
                                data.identificationExpiryDate="";
                                data.expiry="";
                            }
						 var splittedStringArray = isExpiryVal.split("_");
						if(splittedStringArray[1] == 'Y')
						{
							$scope.kinDetailsList[idx].isExpiry='Y';
						}
						else
							{
							$scope.kinDetailsList[idx].isExpiry='N';
							}
					}
					
					
					// for added diagnosis
					$scope.selectedOrderSet= [];
					
					$scope.callOrderset=function(index){
						
						$scope.diagnosisIdForOrderSet=0;
						$scope.diagnosisIdForOrderSet =  $scope.dataListDiagnosis[index].diagnosisId;
						$scope.ordersetFlag=true;
						$scope.getOrderset(index);
					}
					
					$scope.addDiagnosis = function(index) 
					 {
						 
						 console.log("index add",index);
							var data = {
									diagnosisId : '',
									type :'',
									remark:'',
									diagnosisName:null
							};
							var len = $scope.dataListDiagnosis.length;
							
							
							angular.forEach($scope.dataListDiagnosis,function(value,index1){
								if(index == index1){
									if(value.diagnosisId == "" || value.type == "")
									{
										growl.warning("Please Enter Diagnosis.",
		    	    							{
		    	    							title : 'Warning!'
		    	    							});
										return false;
									}
									else{
										$scope.dataListDiagnosis.push(data);
									}
								}
							});
							
							//for diagnosis template
							var tempObj={};
                        	 tempObj.templateObject={};
                        	 tempObj.diagonisIdforTemplate=0;
                        	 tempObj.icpcSystemId=0;
                        	 tempObj.icpcId=0;
                        	 $scope.templateListForSave.push(tempObj);
						};
						
						// for remove diagnosis
						$scope.removeDiagnosis = function(index) {
							console.log("removeIndex",index);
							var newDataList = [];
							var le = $scope.dataListDiagnosis.length;
							var i = 1;
							if(le != i){
							angular.forEach($scope.dataListDiagnosis, function(v) {
								if (index != i) {
									newDataList.push(v);
								}
								i++;
							});
							
							$scope.dataListDiagnosis = newDataList;
							}
							
							//for remove Diagnosis template
							var newTempList = [];
							var tempLen = $scope.templateListForSave.length;
							var j = 1;
							if (tempLen != j) {
								angular.forEach($scope.templateListForSave,
										function(v) {
											if (index != j) {
												newTempList.push(v);
											}
											j++;
										});
								$scope.templateListForSave = newTempList;
							}
						};
						
						$scope.orderSetList = [];
				       	$scope.diagnosisList = [];
				       	$scope.selectedOrderSet = [];
				       	$scope.orderSetOrderList = [];
//				       	$scope.ordersetFlag=false;
//				       	$scope.ordersetButton=false;
				       	$scope.addbutDis=false;
				       	$scope.getOrderset = function(index) 
				    	{
				       		
				       		if($scope.diagnosisIdForOrderSet==0)
				       			{
				       			growl.warning("Please select Diagnosis first..!",{
				    				title : 'Warning!'
				    			 });
				       			return;
				       			}

				       		
				       		$scope.orderSetIndex = index;
				    		$rootScope.startLoader();
				        	var data={
				        		  ordersetMasterId:[]	,
				        		  orgId : $scope.organizationId,
				        		  unitId : $scope.unitId,
				        		  doctorId : $scope.admissionRequestOrAdmissionObj.doctorId,
				        		  specialityId : $scope.admissionRequestOrAdmissionObj.specialityId,
				        		  diagnosisId:$scope.diagnosisIdForOrderSet
				        		}
				        
				        	
				        	console.log('data',JSON.stringify(data));
				    		var URI = BASE_URL + ROOT_URL + GET_ORDER_SET;
				    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
				    				{
				    			   if(response.data.status=='success')
				    			      {
				    				    $scope.orderSetList = response.data.listObject;
				    				    console.log("yes order set ",$scope.orderSetList);
				    				
				    				    if($scope.orderSetList!=null && $scope.orderSetList.length>0)
										{
											$scope.counts=0;
											$scope.countcheck =$scope.orderSetList.length;
											$scope.selectedlist = $scope.selectedOrderSet;
											angular.forEach($scope.selectedlist,function(value,index){
												
												angular.forEach($scope.orderSetList,function(value1,index1){
													if(value == value1.ordersetMasterId)
														{
														$scope.counts++;
														}
													
												});
											});
											
											if($scope.addSelectedOrderSet == true){
												if($scope.counts == $scope.countcheck){
													$scope.addbutDis=true;
												}else{
													$scope.addbutDis=false;
												}
											}
											$( '#addOrdersetModal').modal('show');
										}
				    				    else{
				    				    	growl.warning(
													"No order set mapped with this diagnosis , Please select diagnsis which have order set ..!",
													{
														title : 'Warning!'
													});
				    				    	// object for Diagnosis List
											$scope.dataListDiagnosis = [ {
												diagnosisId : '',
												diagnosisType:'',
												type : '',
												remark : '',
												diagnosisName :null
											}];
				    				    	$rootScope.stopLoader();
											return;
				    				    }
				    				   	$rootScope.stopLoader();
				    			      }
				    			   else if(response.data.status=='error'){
				    				   $rootScope.stopLoader();
				    				   growl.warning("Something went wrong...",{
						    				title : 'Warning!'
						    			 });
				    			   }
				    				
				    		});
				    	}
				       	
				       	$scope.totalOrderSetids = [];
				        $scope.selectedOrderSet = [];
						$scope.fromOrderSetServices = false;
						$scope.addSelectedOrderSet = false;
						$scope.addSelectedOrderSettoAdd = function(index)
						{
							if ($scope.selectedOrderSet.length == 0) {
								growl.warning("Please select Order set..!", {
									title : 'Warning!'
								});
								return;
							}
							else{ 
								$scope.addSelectedOrderSet = true;
								 $scope.dataListDiagnosis[index].selectedOrderSet = $scope.selectedOrderSet;
							    }
							$scope.selectedOrderSet1=[];
							var dataDiagnosisList = $scope.dataListDiagnosis;
							for (var i = 0; i < dataDiagnosisList.length; i++) 
							{
								if($scope.dataListDiagnosis[i].selectedOrderSet!=null && $scope.dataListDiagnosis[i].selectedOrderSet.length>0)
									{
									var data = {
											ordersetMasterId : $scope.dataListDiagnosis[i].selectedOrderSet,
											orgId : $scope.organizationId,
											unitId : $scope.unitId,
											doctorId : $scope.admissionRequestOrAdmissionObj.doctorId,
											specialityId :  $scope.admissionRequestOrAdmissionObj.specialityId,
											diagnosisId : $scope.dataListDiagnosis[i].diagnosisId
										}
									$scope.selectedOrderSet1.push(data);
									}
							}
							
							if($scope.selectedOrderSet1.length>0)
								{
								$scope.addOrderSetOrders($scope.selectedOrderSet1);
								}
						}
						
						 var isServiceExistsForNotSelected;
							function checkServiceIdExistsForNotSelectedTemeporaryAdd(arr, val) {
								if(arr != null && arr.length >0){
								return arr.some(function(arr) {
											if(arr.serviceMasterId === val && arr.serviceRendered == 0 && arr.status == 'A')
												{
													return true;
												}
											else
												{
													return false;
												}
										});
								}else{
									 return false;
									}
							}
						
				       	$scope.addOrderSetOrders = function(orderSetObjList)
				       	{
				       		$scope.dublicateSerivceList = [];
				       		if($scope.selectedOrderSet.lengh==0)
			       			{
			       			growl.warning("Please select Order set..!",{
			    				title : 'Warning!'
			    			 });
			       			return;
			       			}
			    		$rootScope.startLoader();
			        	var data={
			        		  ordersetMasterId:$scope.selectedOrderSet1	,
			        		  orgId : $scope.organizationId,
			        		  unitId : $scope.unitId,
			        		  doctorId : $scope.admissionRequestOrAdmissionObj.doctorId,
			        		  specialityId : $scope.admissionRequestOrAdmissionObj.specialityId,
			        		  diagnosisId:$scope.diagnosisIdForOrderSet
			        		}
			        	$scope.addedServiceOrdersForDisplay=[];
			        	
			    		var URI = BASE_URL + ROOT_URL + GET_ORDER_SET_ORDERS;
			    		console.log('dataget',JSON.stringify(data));
			    		console.log('URI',URI);
			    		BillingGenericService.serviceAction(METHOD_POST, URI, orderSetObjList,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
			    				{
			    			   if(response.data.status=='success')
			    			      {
			    				    $scope.orderSetOrderList = response.data.listObject;
			    				    console.log('$scope.orderSetOrderLis',$scope.orderSetOrderList);
			    				    angular.forEach($scope.orderSetOrderList, function(value, key) 
			    				    		{
			    				    	
			    				    	  angular.forEach(value.ordersetInvestigations, function(value, key) 
					    				    		{
			    				    		 
										        	var newArr = {};
										        	newArr.serviceId = value.serviceId;
													newArr.specialityId = value.specialityId;
													newArr.selectedForFuture = 'N';
													newArr.treatingDoctorId=value.treatingDoctorId;
													newArr.admittingDoctorId=value.admittingDoctorId;
													newArr.treatingSpecialityId=value.treatingSpecialityId;
													newArr.admittingSpecialityId=vlaue.admittingSpecialityId;
													newArr.specialityName = value.specialityName;
													newArr.treatingDoctorName=value.treatingDoctorNamr;
													newArr.admittingDoctorName=value.admittingDoctorName;
													newArr.orderSetId = value.ordersetMasterId;
													newArr.subSpecialityId = value.subSpecilaityId;
													newArr.serviceStandardName = value.serviceStandardName;
													newArr.selectedForFuture = $scope.selectedForFuture;
													
													newArr.specialityId = value.specialityId;
													newArr.serviceSpecialityId = value.specialityId;
													newArr.subSpecialityId = value.subSpecialityId;
													
													newArr.serviceMasterId = value.serviceId;
													newArr.specialityName = value.specialityName;
													newArr.serviceRenderingDeptId = value.subSpecialityId;
													newArr.serviceSubSpecialityId = value.subSpecialityId;
													
													newArr.isAppointable = value.isAppointable;
													newArr.createdBy = $scope.userId;
													newArr.createdDate = new Date()
															.getTime();
													newArr.selfPayable = "0";
													newArr.creditPayable = "0";
													newArr.serviceRendered = "0";
													newArr.doctorId = $scope.admissionRequestOrAdmissionObj.doctorId;
													newArr.discountPercentage = 0;
													newArr.discount = 0;
													newArr.quantity = 1;
													newArr.basePrice = 0;
													newArr.basePriceOld = 0;
													newArr.totalAmt = 0;
													newArr.concession = 0;
													newArr.payeeId = $scope.selfPayeeId;
													newArr.apportionedPrice = null;
													newArr.packageId = null;
													newArr.packageTypeId = null;
													newArr.packageEitherOrGroupId = null;
													newArr.packageCapDetailsId = null;
													newArr.isCapService = null;
													newArr.status = 'A';
													newArr.isRefDoctorShare = value.isRefDoctorShare;
													newArr.packageEitherOrGroupId = null;
													newArr.isPackageService = 'N';
													newArr.discountDetailsList = [];
													$scope.fromOrderSetServices = true;
													newArr.investigationFutureDateToShowdddd = moment(new Date()).format('DD/MM/YYYY');
													$scope.addedServiceOrdersForDisplay.push(newArr);
								
					    				    	    });
			    				    	  $scope.medicationObj={};
			    				    	  
			    				    	 angular.forEach(value.ordersetMedications, function(value, index1) 
					    				    		{
			    				    		  if($scope.medicationDetails!=null && $scope.medicationDetails.length==1 &&$scope.medicationDetails[0].itemId=='' )
		    				    			  {
		    				    			     $scope.medicationDetails = [];
		    				    			  }
					    				    		});
			    				    	  angular.forEach(value.ordersetMedications, function(value, index) 
					    				    {
			    				    		  if($scope.medicationDetails!=null)
			    				    			  {
			    				    			    $scope.medicationObj.itemId = value.itemMasterId;
			    				    			    $scope.medicationObj.doseUnitId = value.doseUnitId;
			    				    			    $scope.medicationObj.dose = value.dose;
			    				    			    $scope.medicationObj.roaId = value.routeId;
			    				    			    $scope.medicationObj.frequencyId = value.frequencyId;
			    				    			    $scope.medicationObj.formulationTypeId= value.formulationTypeId;
			    				    			    $scope.medicationObj.medicationStartDate = moment(new Date()).format('YYYY-MM-DD');
													$scope.medicationObj.itemName = value.itemName;
													$scope.medicationObj.roaDesc =value.roaDesc;
													$scope.medicationObj.strengthDesc =value.strengthDesc;
													$scope.medicationObj.frequencyDesc =value.frequencyDesc;
													$scope.medicationObj.priorityId =1;
													$scope.medicationObj.genericName = value.genericName;
													$scope.medicationObj.searchGeneric = value.genericName;
													$scope.medicationObj.genericId=value.genericId;
													$scope.medicationObj.searchItem = value.itemName;
													$scope.medicationObj.medicationName = value.itemName;
											        $scope.medicationObj.searchItemFre = value.frequencyDesc;
											        $scope.medicationObj.instructionId = value.instructionId;
											        $scope.medicationObj.orderSetId = value.ordersetMasterId;
													  var URI = BASE_URL + ROOT_URL + "/api/opd/getRoaList" + "?unitId=" + 
														$scope.unitId + "&orgId=" + $scope.organizationId + "&formulationTypeId=" +
														$scope.medicationObj.formulationTypeId+ "&genericId="
                                                         + $scope.medicationObj.genericId+ "&patientId=" +
                                                         $scope.patientDetails.patientId;
													
														GenericServiceParamHeader.serviceAction(METHOD_GET, URI).then(function(response) {
														if(response.data.status == "success")
														{
															$scope.medicationDetails[index].roaList=[];
															var idx = $scope.medicationDetails.findIndex(item => item.itemId == value.itemMasterId);
															$scope.medicationDetails[index].roaList = response.data.listObject;
														}});
														

												    $scope.medicationObj.instructionDesc = value.instructionDesc;
													$scope.medicationObj.medicationStartDate = new Date();
													$scope.medicationObj.nonFormularyAdvice='N' ;
													$scope.medicationObj.isGeneric='N' ;
													$scope.medicationObj.isInstruction='N' ;
													
													$scope.medicationObj.frequencyId=value.frequencyId;
													$scope.medicationObj.frequencyDesc=value.frequencyDesc;
													$scope.medicationObj.noOfAdministrationTime=value.noOfAdministrationTime;
													$scope.medicationObj.durationUnit='Day';
										    		$scope.freQua = 0;
										    		$scope.freQua = value.noOfAdministrationTime;
										    		var dur = 0;
										    		$scope.medicationObj.quantity = value.noOfAdministrationTime * dur;
										    		var checkFlagValidation = true;
												    $scope.medicationObj.checkFlagValidation = checkFlagValidation;
												    $scope.medicationObj.selected = 'Y';
												    
													var found = $scope.medicationDetails.find(ar=>ar.itemId==value.itemMasterId);
													   if(found==undefined)
														   {
														      $scope.medicationDetails.push($scope.medicationObj);
														      console.log("testOrderSet" ,$scope.medicationDetails);
														   }
													   
													   
													$scope.medicationObj={};
			    				    			  }
			    				    		
					    				    	 });
			    				    	  
			    				  		
			    				    	  angular.forEach($scope.medicationDetails,function(value,idx){
			    				    		//get formuation dosage list
			    				    	$scope.prescribedDosageUnitListorderswet=[];
			    				  			var dataobj = {
													organizationId : $scope.organizationId,
													unitId : $scope.unitId,
													formulationTypeId: value.formulationTypeId
											};
											value.prescribeDose="";
											var URIForPresc = BASE_URL + ROOT_URL+ "/api/global/getFormulationTypeToDosageMapperListByFormulationTypeId";
											GenericService.serviceAction("POST", URIForPresc, dataobj).then(function(response) {
														$rootScope.stopLoader();
														$scope.counterFormulation=1;
														$scope.medicationDetails[idx].formulationCounterIndex="";
														$scope.prescribedDosageUnitListorderswet=[];
														angular.forEach(response.data.listObject,function(value,index){
															var unitObj={};
															unitObj.formulationDosageFromUnit=value.fromConversionUnit;
															unitObj.formulationDosageFromUnitId=value.fromConversionFactorUnit;
															unitObj.formulationDosageToUnit=value.toConversionUnit;
															unitObj.formulationDosageToUnitId=value.toConversionFactorUnit;
															unitObj.counterIndex=$scope.counterFormulation++;
															unitObj.formulationDosageFromConversionValue=value.fromConversionFactor;
															unitObj.formulationDosageToConversionValue=value.toConversionFactor;
															unitObj.formulationFactorId=value.factorId;
															unitObj.formulationDosage=value.dosageForm;
															unitObj.formulationDosageMappId=value.formulationDosageMappId;
															$scope.prescribedDosageUnitListorderswet.push(unitObj);
														});
														
														angular.forEach(response.data.listObject,function(value,index){
															var unitObj={};
															if(value.factorId==1){
																unitObj.formulationDosageFromUnit=value.toConversionUnit;
																unitObj.formulationDosageFromUnitId=value.toConversionFactorUnit;
																unitObj.formulationDosageToUnit=value.fromConversionUnit;
																unitObj.formulationDosageToUnitId=value.fromConversionFactorUnit;
																unitObj.counterIndex=$scope.counterFormulation++;
																unitObj.formulationDosageFromConversionValue=value.toConversionFactor;
																unitObj.formulationDosageToConversionValue=value.fromConversionFactor;
																unitObj.formulationFactorId=value.factorId;
																unitObj.formulationDosage=value.dosageForm;
																unitObj.formulationDosageMappId=value.formulationDosageMappId;
																$scope.prescribedDosageUnitListorderswet.push(unitObj);
															}
														});
														value.prescribedDosageUnitList=$scope.prescribedDosageUnitListorderswet;
														var list=[];
														if(value.prescribedDosageUnitList.length>0 && value.prescribedDosageUnitList[0].formulationFactorId==1){
															 list=$.grep(value.prescribedDosageUnitList, function (obj) {
														        return value.doseUnitId === obj.formulationDosageFromUnitId;
														    });
															if(list.length>0){	
																value.formulationCounterIndex=list[0].counterIndex;
															}
														}
											});
			    				    		  
			    				    	  })
			    				    	  
			    				    	    });
			    					
			    				    $('#addOrdersetModal').modal('hide');
			    				   	$rootScope.stopLoader();
			    			      }
			    			   else if(response.data.status=='error'){
			    				   $rootScope.stopLoader();
			    				   growl.warning("Something went wrong...",{
					    				title : 'Warning!'
					    			 });
			    			   }
			    				
			    		});
				       	}
				       	
				       	var commonObj = {
			    				isMedication : 'Y',
								organizationId : $scope.organizationId
							}

						var URI = BASE_URL + ROOT_URL
								+ GETMEDICATIONINSTRUCTIONS;
					
						BillingGenericService.serviceAction(METHOD_POST, URI,
										commonObj,
										NOTIFICATION_MSG_STATUS_FALSE).then(
										function(response) {
											console.log(response);
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.medicationInstructions =[];
												$scope.medicationInstructions = response.data.listObject;
										});
						
						 $scope.removeTempServicesByIndex = function(index){
					    		$scope.addedServiceOrdersForDisplay.splice(index, 1);
					    	}
					        
						 
						 $scope.removeTempMedicationByIndex = function(index){
					    		$scope.medicationDetails.splice(index, 1);
					    	}
					        
					
						 
							// to show a default empty row in medication
							// detail
							$scope.emptyMedicationRow = {
									itemId:"",
									favorite_status:false,
									medicationStartDate:new Date(),
									roaId:'',
									dose:'',
									doseUnitId:'',
									durationCount:'',
									durationUnit:'Day',
									instructionId:'',
									medicationEndDate:'',
									frequencyId:'',
									quantity:'',
									uomTypeId:'',
									conversions:'',
									strength :'',
									nonFormularyAdvice:'N',
									medicationName:'',
									itemName:'',
									genericName:'',
									isStat:false,
									isGeneric:'N',
									isInstruction:'N',
									checkFlagValidation:false,
									prescribedDosageUnitList:[]
									
								};
							
							$scope.medicationDetails = [$scope.emptyMedicationRow];
							
							$scope.addedServiceOrdersForDisplay = [{
								"serviceMasterId":""
							}];
							
							var URI =BASE_URL + ROOT_URL +"/api/globals/strength/units/"+$scope.organizationId;
							GenericService.serviceAction("GET", URI, {}).then(
							function(response) {
							$scope.doseList = [];
							$scope.doseList = response.data;

							});
							
							 // for Medication Advice
                         $scope.openDatePickerMedicationAdvice = function(index) {

                         	
                         	
                         	angular.forEach( $scope.medicationDetails,function(value,idx){
                         		if(idx==index){
                         			 $scope.datepickerMedicationAdvice.opened[idx] = true;
                         		}else{
                         			 $scope.datepickerMedicationAdvice.opened[idx] = false;
                         		}
                         	});
                          
                         };

                         $scope.datepickerMedicationAdvice = {
                             opened : [ false ],

                         };
                         $scope.dateOptionsMedicationAdvice = {
                         		formatYear : 'yyyy',
 								showWeeks : false,
 								minDate:new Date(), //max 
 							};
                         
                         
                         $scope.openDatePickerMedicationAdviceEndDate = function(index) {

                             for (var i = $scope.medicationDetails.length - 1; i >= 0; i--) {
                                 if (i === index) {

                                     $scope.datepickerMedicationAdviceEndDate.opened[i] = true;
                                 } else {
                                     $scope.datepickerMedicationAdviceEndDate.opened[i] = false;
                                 }
                             }
                         };

                         $scope.datepickerMedicationAdviceEndDate = {
                             opened : [ false ],

                         };
                         // End Travel
                         $scope.dateOptionsMedicationAdvicesEndDate = {
                         		formatYear : 'yyyy',
 								showWeeks : false,
 								minDate:new Date(), //max 
 							};
 							
                         
                         $scope.onStartDateChange = function(medication) {
								$scope.calculateEndDate(medication)
								if(medication.medicationStartDate  < $scope.currentDate){
									alert("date should not be leass than todays date");
								}
							}
							
							$scope.calculateEndDate = function(medication) {
								var currentMedication = medication
								var medicationStartDate = currentMedication.medicationStartDate
							
								var indexOfCurrentMedication = $scope.medicationDetails.indexOf(currentMedication)
								var durationCount = parseInt(currentMedication["durationCount"])
								var durationUnit = currentMedication["durationUnit"]
							
								    if(durationUnit == "Day") {
									var durationCount = parseInt(currentMedication["durationCount"]);
									}
								    else if(durationUnit == "Week") {
										var durationCount = parseInt(currentMedication["durationCount"])
										var durationCount = durationCount* 7;
									}
									else if(durationUnit == "Month") {
										var durationCount = parseInt(currentMedication["durationCount"])
										var durationCount = durationCount* 30;
									}
									else if(durationUnit == "Year") {
										var durationCount = parseInt(currentMedication["durationCount"])
										var months = durationCount * 12
										var durationCount = months* 30;
									}
								
								if(durationCount && durationUnit && medicationStartDate) {
									if(durationUnit == "Day") {
										var endDate = moment(medicationStartDate).add(durationCount-1, 'days').format('YYYY-MM-DD');
									}
									else if(durationUnit == "Week") {
										var days = durationCount * 7
										var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
									}
									else if(durationUnit == "Month") {
										var days = durationCount * 30
										var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
									}
									else if(durationUnit == "Year") {
										var days = durationCount * 365;
										var endDate = moment(medicationStartDate).add(durationCount, 'days').format('YYYY-MM-DD');
									}
									currentMedication["medicationEndDate"] = endDate
									currentMedication["medicationEndDateFormatted"] = moment(endDate).toDate();
									
									$scope.medicationDetails[indexOfCurrentMedication] = currentMedication
									$scope.model=currentMedication.searchItemFre;
									$scope.label=currentMedication.searchItemFre;
									$scope.idx=indexOfCurrentMedication;
									//$scope.addFrequencyItemList($scope.storeItemOrder,$scope.model,$scope.label,$scope.idx);
									$scope.calculateFre($scope.idx);
								}else{
									$scope.medicationDetails[indexOfCurrentMedication].quantity="";
								}
							}
							
							$scope.addFrequencyItemList = function($item, $model, $label,idx){
					    		console.log($item);
					    		if($item != undefined){
					    		$scope.storeItemOrder=$item;
					    		$scope.freQua = 0;
					    		$scope.medicationDetails[idx].frequencyId = $item.frequency_id;
					    		$scope.freQua = $item.noOfAdministrationTime;
					    		}else{
					    			$scope.freQua = 0;
					    			$item={};
					    			$scope.medicationDetails[idx].frequencyId = $scope.medicationDetails[idx].frequencyId;
					        		$scope.freQua =$scope.medicationDetails[idx].noOfAdministrationTime;
					        		$item.noOfAdministrationTime =$scope.medicationDetails[idx].noOfAdministrationTime;
					    		}
					    		var dur = 0;
					    		if($scope.medicationDetails[idx].durationCount == '')
					    			{
					    			dur = 0;
					    			}
					    		else
					    			{
					    				
					    				 if($scope.medicationDetails[idx].durationUnit == "Day")
						    				{
					    					 dur = $scope.medicationDetails[idx].durationCount;
						    				 }
					    				 else if($scope.medicationDetails[idx].durationUnit == "Week")
							    			{
							    			  var  caldur = $scope.medicationDetails[idx].durationCount;
							    			    dur = caldur * 7;
							    			}
						    			else if($scope.medicationDetails[idx].durationUnit == "Month")
							    			{
							    			  var  caldur = $scope.medicationDetails[idx].durationCount;
							    			    dur = caldur * 30;
							    			}
								    		else if($scope.medicationDetails[idx].durationUnit == "Year")
							    			{
								    				
								    			var  caldur = $scope.medicationDetails[idx].durationCount;
							    				dur = caldur * 365;
							    		    }
					    			}
					    		
					    		$scope.medicationDetails[idx].quantity = $item.noOfAdministrationTime * dur;
					    		$scope.calculateFre(idx);
					    	}
					        
					        $scope.calculateFre = function(idx)
					        {
								var list=$.grep($scope.medicationDetails[idx].prescribedDosageUnitList, function (obj) {
							        return $scope.medicationDetails[idx].formulationCounterIndex === obj.counterIndex;
							    });
								var totalDuration="";
								if($scope.medicationDetails[idx].durationUnit=="Day"){
									totalDuration=parseInt($scope.medicationDetails[idx].durationCount)*1;
								}else if($scope.medicationDetails[idx].durationUnit=="Week"){
									totalDuration=parseInt($scope.medicationDetails[idx].durationCount)*7;
								}else if($scope.medicationDetails[idx].durationUnit=="Month"){
									totalDuration=parseInt($scope.medicationDetails[idx].durationCount)*30;
								}else{
									totalDuration=parseInt($scope.medicationDetails[idx].durationCount)*30*12;
								}
								if(list.length>0){	
									var presDoseObj=list[0];
									if(list.length>0  && presDoseObj.formulationFactorId==2){
										$scope.medicationDetails[idx].prescriptionDoseUnit=presDoseObj.formulationDosage;
									}else{
										$scope.medicationDetails[idx].prescriptionDoseUnit=presDoseObj.formulationDosageFromUnit;
									}
									$scope.medicationDetails[idx].prescribedDoseUnitId=presDoseObj.formulationDosageFromUnitId;
								}
								if(list.length>0  && $scope.medicationDetails[idx].prescribeDose!="" && $scope.medicationDetails[idx].prescribeDose!=undefined && $scope.medicationDetails[idx].prescribeDose!=null 
										&& $scope.medicationDetails[idx].formulationCounterIndex!="" && $scope.medicationDetails[idx].formulationCounterIndex!=undefined && $scope.medicationDetails[idx].formulationCounterIndex!=null){
									//for conversion factor
										if($scope.medicationDetails[idx].prescribedDoseUnitId==$scope.medicationDetails[idx].doseUnitId && $scope.medicationDetails[idx].dose!=0){
											$scope.medicationDetails[idx].quantity =Math.ceil(($scope.freQua* totalDuration*$scope.medicationDetails[idx].prescribeDose)/$scope.medicationDetails[idx].dose);
										}else{
											if(list.length>0 && $scope.medicationDetails[idx].dose!=0){	
											if(presDoseObj.formulationDosageToUnitId==$scope.medicationDetails[idx].doseUnitId){
												var convValue=Math.round(($scope.medicationDetails[idx].prescribeDose*presDoseObj.formulationDosageToConversionValue)/presDoseObj.formulationDosageFromConversionValue);
												 $scope.medicationDetails[idx].quantity = Math.ceil(($scope.freQua* totalDuration*convValue)/$scope.medicationDetails[idx].dose);
											}else{
											  $scope.medicationDetails[idx].quantity = Math.ceil(($scope.freQua* totalDuration*$scope.medicationDetails[idx].prescribeDose)/$scope.medicationDetails[idx].dose);
											}
										  }else{
											  $scope.medicationDetails[idx].quantity = Math.ceil(($scope.freQua
														* totalDuration)); 
										  }
											
										}
								}else{
									//using frequency factor 
									if(list.length>0  && presDoseObj.formulationFactorId==2){
										var splitString=(presDoseObj.formulationDosage).split(" ");
										var convValue=0,i;
										for(i=0;i<splitString.length;i++){
											var b=parseFloat(splitString[i]);
											if(Number.isNaN(b)==false){
												convValue=convValue+(math.number(math.fraction(splitString[i])));
											}
										}
										 $scope.medicationDetails[idx].quantity = Math.ceil($scope.freQua
													* totalDuration*(convValue==0?1:convValue)); 
									}else{
										$scope.medicationDetails[idx].quantity = Math.ceil(($scope.freQua * totalDuration));
									}
								}
							
					        }
					        
							$scope.onDurationCountChange = function(medication,idx) {
								$scope.calculateEndDate(medication);
								$scope.medicationExist=false;
								var AA={};
								var AA=medication;
								//$scope.calculateEndDate(medication)
								$scope.ListCheckDuplicate = [];
								$scope.ListCheckDuplicate.push(AA);
								console.log("entered data",$scope.ListCheckDuplicate);
								
								angular.forEach($scope.ListCheckDuplicate,function(value1,index){
									
										if($scope.medicationDetails.length>0){
		                                    $scope.medicationStartDateEnter = moment(value1.medicationStartDate).format('YYYY-MM-DD');
		                                    angular.forEach($scope.medicationDetails,function(value,index){
		                                        if(idx != index){
		                                        if ( value1.searchItem == value.searchItem
		                                                && moment(value1.medicationStartDate).format('YYYY-MM-DD') == moment(value.medicationStartDate).format('YYYY-MM-DD')
		//                                                && $scope.medicationObj.medicationEndDate == value.medicationEndDate
		                                                && value1.durationCount == value.durationCount
		                                                && value1.searchItemFre == value.searchItemFre
		                                                && value1.doseUnitId == value.doseUnitId
		                                                && value1.roaId == value.roaId
		                                                && value1.quantity == value.quantity) {
		                                            
		                                            $scope.medicationExist =true;
		                                        }
		                                       }
		                                    });
		                                }
							   });	
								 if($scope.medicationExist){
									 
	                                    growl.warning( "Medication Already Added",
	                                            {
	                                                title : 'Warning!'
	                                            });
	                                    $scope.medicationDetails[idx].durationCount="";
	                                    $scope.medicationDetails[idx].durationUnit="Day";
	                                    $scope.medicationDetails[idx].medicationStartDate=new Date();
	                                    $scope.medicationDetails[idx].medicationEndDateFormatted="";
	                                    $scope.medicationDetails[idx].quantity="";
	                                   $rootScope.stopLoader();
	                                  return false;
	                                 }
								 
							}
							
							$scope.searchMedicationFreItems = function(searchKeyword,ind){

					        	$scope.itemList=[];
					        	var unitId = $scope.unitId
								var orgId = $scope.organizationId
								
								var URI = BASE_URL + ROOT_URL + "/api/opd/getFrequencyList" + "?unitId=" + 
									unitId + "&orgId=" + orgId + "&searchPar=" + searchKeyword
					    		return BillingGenericService.serviceAction(METHOD_GET,URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					    			if(response.data.status == "success"){
					    				$scope.itemList = response.data.listObject;
					    			}
					    			if($scope.itemList.length > 0)
					    				{
					    				return $scope.itemList;
					    				}else{
					    					$scope.medicationDetails[ind].searchItemFre='';
					    					$scope.medicationDetails[ind].frequencyId='';
					    				}
					    			
					    		});
					    	}
							$scope.onDurationUnitChange = function(medication,idx) {
								$scope.medicationExist=false;
								var AA={};
								var AA=medication;
								$scope.calculateEndDate(medication)
								$scope.ListCheckDuplicate = [];
								$scope.ListCheckDuplicate.push(AA);
								console.log("entered data",$scope.ListCheckDuplicate);
								
								angular.forEach($scope.ListCheckDuplicate,function(value1,index){
									
										if($scope.medicationDetails.length>0){
		                                    $scope.medicationStartDateEnter = moment(value1.medicationStartDate).format('YYYY-MM-DD');
		                                    angular.forEach($scope.medicationDetails,function(value,index){
		                                        if(idx != index){
		                                        if ( value1.searchItem == value.searchItem
		                                                && moment(value1.medicationStartDate).format('YYYY-MM-DD') == moment(value.medicationStartDate).format('YYYY-MM-DD')
		//                                                && $scope.medicationObj.medicationEndDate == value.medicationEndDate
		                                                && value1.durationCount == value.durationCount
		                                                && value1.searchItemFre == value.searchItemFre
		                                                && value1.doseUnitId == value.doseUnitId
		                                                && value1.roaId == value.roaId
		                                                && value1.quantity == value.quantity) {
		                                            
		                                            $scope.medicationExist =true;
		                                        }
		                                        }
		                                    });
		                                }
							   });	.2
								 if($scope.medicationExist){
	                                    growl.warning( "Medication Already Added",
	                                            {
	                                                title : 'Warning!'
	                                            });
	                                    $scope.medicationDetails[idx].durationCount="";
	                                    $scope.medicationDetails[idx].durationUnit="Day";
	                                    $scope.medicationDetails[idx].medicationStartDate=new Date();
	                                    $scope.medicationDetails[idx].medicationEndDateFormatted="";
	                                    $scope.medicationDetails[idx].quantity="";
	                                   $rootScope.stopLoader();
	                                   $rootScope.stopLoader();
	                                  return false;
	                                 }
							}
							
							 
						$scope.flagDigByGen=false;
						$scope.searchGroupList = function(keyword,index) {
							  $scope.DiagnosisByGenderList=[];
									
									var data = {
											organizationId : $scope.organizationId,
											diagnosisName : keyword,
											};
									
									console.log(data);
									$scope.varOr = "|";
									var URI = BASE_URL + ROOT_URL
											+ "/api/opd/getActiveEmergencyDiagnosis";
									
									return GenericService.serviceAction("POST", URI,data)
											.then(
													function(response) {
														console.log(response);
														// $rootScope.startLoader();
														if (response.data.status == "success")
															 $scope.DiagnosisByGenderList = response.data.listObject;
														if($scope.DiagnosisByGenderList.length == 0){
															
															$scope.flagDigByGen=true;
															var newArr = {};
															newArr.diagnosisName="Diagnosis not applicable";
															newArr.diagnosisId='';
															$scope.DiagnosisByGenderList.push(newArr);
															$scope.varOr="";
															return response.data.listObject= $scope.DiagnosisByGenderList;
														} else{
															$scope.flagDigByGen=false;
														}
															return response.data.listObject;
															
													});

								}
						
						// Selected TypeHead on Diagnosis
						$scope.selectDignosisIdForDiagnosis = function(info, model,
								label,ind) {
							console.log(info);
							
							$scope.dataListDiagnosis[ind].diagnosisId = info.diagnosisId;
							$scope.diagonisIdforTemplate = $scope.dataListDiagnosis[ind].diagnosisId;
							$scope.dataListDiagnosis[ind].selectedOrderSet = [];
						}
						
					     $scope.allReadyExistDia=function(index){
								
								//saved data check
								$scope.isDiagnosisExists= true;
								angular.forEach($scope.dataListDiagnosis, function(value , key) {
								if($scope.patientDiagnosisList!=undefined && $scope.patientDiagnosisList.length>0){
									var chkStatus= $scope.patientDiagnosisList.filter(function(item)
									{ 
										return (item.diagnosisId==value.diagnosisId && 
												item.type==value.type && item.diagnosisType==value.diagnosisType &&
												item.admissionId == $scope.patientDetails.admissionId);
	    									})
    	    								if(chkStatus.length>0){
    	    									growl.warning("Diagnosis already exists.",
    			    	    							{
    			    	    							title : 'Warning!'
    			    	    							});
    	    								        	$scope.isDiagnosisExists= false;
    	    								        	$scope.dataListDiagnosis[index].type='';
    			    	    							$rootScope.stopLoader();
    			    	    							return false;
    	    								}
	    								}
								});
								
								
								
								//addeded data check
								
								$scope.list=[];
								$scope.list2=[];
								angular.forEach($scope.dataListDiagnosis, function(value , index1) {
									if(index1 == index){
										var newArr ={};
										newArr.diagnosisId=value.diagnosisId;
										newArr.type=value.type;
										$scope.list.push(newArr);
									}
								});
								angular.forEach($scope.dataListDiagnosis, function(value , index2) {
									if(index2 != index){
										var newArr ={};
										newArr.diagnosisId=value.diagnosisId;
										newArr.type=value.type;
										$scope.list2.push(newArr);
									}
								});
								
								angular.forEach($scope.list, function(value , key) {
									if($scope.dataListDiagnosis.length > 1){
									if($scope.dataListDiagnosis!=undefined && $scope.dataListDiagnosis.length>0){
										var chkStatus= $scope.list2.filter(function(item)
										{ 
											return (item.diagnosisId==value.diagnosisId && 
													item.type==value.type && item.diagnosisType==value.diagnosisType );
		    									})
	    	    								if(chkStatus.length>0){
	    	    									growl.warning("Diagnosis already added.",
	    			    	    							{
	    			    	    							title : 'Warning!'
	    			    	    							});
	    	    								        	$scope.isDiagnosisExists= false;
	    	    								        	$scope.dataListDiagnosis[index].type='';
	    			    	    							$rootScope.stopLoader();
	    			    	    							return false;
	    	    								}
		    								}
								     }
									});
								
								//once dai added as final dont again it as provisionl
								angular.forEach($scope.list, function(value , key) {
									if($scope.dataListDiagnosis.length > 1){
									if($scope.dataListDiagnosis!=undefined && $scope.dataListDiagnosis.length>0){
										var chkStatus= $scope.list2.filter(function(item)
										{ 
											return (item.diagnosisId==value.diagnosisId && 
													item.type==2 && item.diagnosisType == value.diagnosisType);
		    									})
	    	    								if(chkStatus.length>0){
	    	    									growl.warning("Diagnosis is already added as Final diagnosis.",
	    			    	    							{
	    			    	    							title : 'Warning!'
	    			    	    							});
	    	    								        	$scope.isDiagnosisExists= false;
	    	    								        	$scope.dataListDiagnosis[index].type='';
	    			    	    							$rootScope.stopLoader();
	    			    	    							return false;
	    	    								}
		    								}
								     }
									});
								//once dai save as final dont again it as provisionl
								if($scope.isDiagnosisExists){
								angular.forEach($scope.dataListDiagnosis, function(value , key) {
									if($scope.patientDiagnosisList!=undefined && $scope.patientDiagnosisList.length>0){
										var chkStatus= $scope.patientDiagnosisList.filter(function(item)
										{ 
											return (item.diagnosisId==value.diagnosisId && 
													item.type == 2 && item.diagnosisType==value.diagnosisType && 
													item.admissionId == $scope.patientDetails.admissionId);
		    									})
	    	    								if(chkStatus.length>0){
	    	    									growl.warning("Diagnosis is already saved as Final diagnosis.",
	    			    	    							{
	    			    	    							title : 'Warning!'
	    			    	    							});
	    	    									$scope.dataListDiagnosis[index].type='';
	    	    									$scope.isDiagnosisExists= false;
	    			    	    							$rootScope.stopLoader();
	    			    	    							return false;
	    	    								}
		    								}
									});
								}
							}
					     
							$scope.checkDiagnosisTypeExist=function(index){
								$scope.diaSec=index;
								//sneha
								$scope.Diaflag=false;
								$scope.DiaflagSave=true;
								angular.forEach($scope.dataListDiagnosis, function(value , index1) {
									if(index != index1){
									if(value.diagnosisType == 1){
										$scope.Diaflag=true;
									}
									}
								 });
								
								angular.forEach($scope.dataListDiagnosis, function(value , index1) {
								
									if(index == index1){
									if(value.diagnosisId == ""){
										$scope.DiaflagSave=false;
									}
									}
								 });
								
								
									angular.forEach($scope.dataListDiagnosis, function(value , key) 
									{
										if($scope.DiaflagSave){
										if($scope.Diaflag){
										if(value.diagnosisType == 1){
											$('#confirmDiagnosisTypePopup').modal();
										}
										}
										}
										
									});
								
							}
							
							$scope.setDiagTypeSecondary=function(){
								
								angular.forEach($scope.dataListDiagnosis,function(value,index){
									if($scope.diaSec == index){
										value.diagnosisType= 2;
									}
								});
								
							}
							
							//For multiple Diagnosis type save validtion
							$scope.checkDiagTypePrimary=function(){
								var counter=0;
								angular.forEach($scope.dataListDiagnosis, function(value , index1) {
									if($scope.diaSec != index1){
									value.diagnosisType=2;	
									}
								 });
								
							}
							//---------//
							
							$scope.checkDiagnosisTypeNotExist=function(index){
								//chetana
								var counter=0;
								if($scope.patientDiagnosisList.length > 0){
								angular.forEach($scope.patientDiagnosisList, function(value , key) 
								{
									if(value.diagnosisType == 1){
										counter++;
									}
									
								});
								}
								if(counter <= 0){
									growl.warning("Atleast one diagnosis should be primary.!!",
	    	    							{
	    	    							title : 'Warning!'
	    	    							});
								        	//$scope.isDiagnosisExists= false;
								        	$scope.dataListDiagnosis[index].diagnosisType='';
	    	    							$rootScope.stopLoader();
	    	    							return false;
								}
							}
				       	$scope.getDiagonosis = function(templateTypeId,obj,index) {
							$scope.icpcSystemId = 0;
							$scope.icpcId = 0;
							$scope.tempTemplateIndex=index;
							$scope.diagonisIdforTemplate=obj.diagnosisId;
							if ($scope.diagonisIdforTemplate == 0 ||$scope.diagonisIdforTemplate== 0 ) 
							{
								growl.warning(
										"Please select ICD 10 first...!!",
										{
											title : 'Warning!'
										});
								return;
							}
							$rootScope.startLoader();
							var data = {
								orgId : $scope.organizationId,
								unitId : $scope.unitId,
								doctorId : $scope.admissionRequestOrAdmissionObj.doctorId,
				        		specialityId : $scope.admissionRequestOrAdmissionObj.specialityId,
				        		templateTypeId : templateTypeId,
				        		patientId : $scope.patientDetails.patientId,
				        		visitTypeId: $scope.patientDetails.visitTypeId,
				        		admissionId : $scope.patientDetails.admissionId,
								diagnosisId : $scope.diagonisIdforTemplate,
								mIcpcSystemId : $scope.icpcSystemId,
								mIcpcId :  $scope.diagonisIdforTemplate
							}

							angular.forEach($scope.templateListForSave,function(value,index){
								if(index==$scope.tempTemplateIndex){
                        		value.diagonisIdforTemplate=$scope.diagonisIdforTemplate;
                        		value.icpcSystemId=0;
                        		value.icpcId=0;
								}
							});
							
							console.log('data', JSON.stringify(data));
							var URI = BASE_URL + ROOT_URL
									+ "/api/global/templates/diagnosis";
							BillingGenericService
									.serviceAction(METHOD_POST, URI, data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												if (response.data.status == 'success') {
													$scope.templateList = response.data.listObject;
													if($scope.templateList.length==0){
														growl.warning(
																"No template mapped with selected ICD10 code..!",
																{
																	title : 'Warning!'
																});
														return false;
													}
													$('#addtemplateModal')
															.modal('show');
													$rootScope.stopLoader();
												} else if (response.data.status == 'error') {
													$rootScope.stopLoader();
													growl.warning(
																	"Template is already filled..!",
																	{
																		title : 'Warning!'
																	});
												}

											});
						}
			       	/*************************************Diagnosis Code End***********************************/
	
						 $scope.addVisitorPass = function(index) 
						 {
							 $scope.visiterPassMaster={
					        			"visitorPassTypeId":0,
					        			"passNumber":""
					        	}
					        	$scope.listVisiterPassMaster.push($scope.visiterPassMaster);
							};
							
							$scope.removeVisitorPass = function(index){
								/*if(index != 0)*/{
									$scope.listVisiterPassMaster.splice(index,1);
								}
							}
							$scope.visitorPassTypeList = [];

							$scope.getVisitorPassList=function(info){
								var URI = BASE_URL + ROOT_URL + "/api/unit/getVisitorPassMapperListByBedCategoryNVisitType";
								var commonObj={
										organisationId : $scope.organizationId,
										unitId : $scope.unitId,
										bedCategoryId  :info.bedCategoryId,
										visitTypeId : info.visitTypeId
								}
								GenericService
										.serviceAction("POST", URI, commonObj)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.visitorPassTypeList = response.data.listObject;
													}
												}); 
							}
							$scope.PopUpOpen = function() {

								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
								} else {

								/*	if($scope.itemSelecteds.paymentEntitlementId != 2)
									 {
										 growl
											.warning(
													'Please is not insurance..!!',
													{
														title : 'Warning!'
													});
										 return false;
									 }
								 else{*/
									
									if($scope.itemSelecteds.patientId > 0)
										{
										var age = $rootScope
										.getAge($scope.itemSelecteds.dob);
										$scope.patientFullName = $scope.itemSelecteds.patientFullName
										+ " "
										+age
										+ "/"
										+ $scope.itemSelecteds.genderCode;
										}
									else
										{
										$scope.patientFullName = $scope.itemSelecteds.patientFullName
										+ " "
										+ $scope.itemSelecteds.dob
										+ "/"
										+ $scope.itemSelecteds.genderCode;
										}
										
									$scope.getVisitorPassList($scope.itemSelecteds);
									$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
									$scope.genderCode =$scope.itemSelecteds.genderCode;
									$scope.doctorFullName =$scope.itemSelecteds.doctorFullName;
									$scope.bedNumber = $scope.itemSelecteds.bedNumber;
									$scope.admissionNumber = $scope.itemSelecteds.admissionNumber;
									$scope.department = $scope.itemSelecteds.specialityName;
									$scope.dateOfAdmission = $scope.itemSelecteds.datepicker;
									$scope.doaActual = $scope.dateOfAdmission;
									$scope.specialityName= $scope.itemSelecteds.specialityName;
									$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
									$scope.pddActual = $scope.patientDischargeDate;
									$scope.getPassIssuedList($scope.itemSelecteds.admissionId);
//									 $scope.getDepositMatrixList($scope.itemSelecteds.patientCategoryId,$scope.itemSelecteds.paymentEntitlementId,$scope.itemSelecteds.bedCategoryId);
									 $scope.getDepositMatrixList($scope.itemSelecteds.patientCategoryId,$scope.itemSelecteds.paymentEntitlementId,$scope.itemSelecteds.billingBedCategoryId,$scope.itemSelecteds.admissionTypeId);
									 
										 if($scope.insurancePopUpFlag)
										 {
										 $scope.getPatientSponcerDetails($scope.itemSelecteds.admissionId,$scope.itemSelecteds.paymentEntitlementId);
										 }

									 $scope.searchBills($scope.itemSelecteds.patientId,$scope.itemSelecteds.admissionId);
									 if($scope.assignContractPopUpFlag){
										 $scope.getPatientSponcerDetails($scope.itemSelecteds.admissionId,$scope.itemSelecteds.paymentEntitlementId);
										 $scope.corporateUploadFunctionlity($scope.itemSelecteds);
									 }
//									 }
									
							
									 
								}

							}
							
							$scope.commonFuntion = function(typeId) {
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning'
													});
								} else {

									
									
									if($scope.itemSelecteds.patientId > 0)
										{
										var age = $rootScope
										.getAge($scope.itemSelecteds.dob);
										$scope.patientFullName = $scope.itemSelecteds.patientFullName
										+ " "
										+age
										+ "/"
										+ $scope.itemSelecteds.genderCode;
										}
									else
										{
										$scope.patientFullName = $scope.itemSelecteds.patientFullName
										+ " "
										+ $scope.itemSelecteds.dob
										+ "/"
										+ $scope.itemSelecteds.genderCode;
										}
										
									
									$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
									$scope.doctorFullName =$scope.itemSelecteds.doctorFullName;
									$scope.bedNumber = $scope.itemSelecteds.bedNumber;
									$scope.dateOfAdmission = $scope.itemSelecteds.datepicker;
									$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
									$scope.pddActual = $scope.patientDischargeDate;
									$scope.doaActual = $scope.dateOfAdmission;
									$scope
											.getPassIssuedList($scope.itemSelecteds.admissionId);
									
									
								/*	var URI = BASE_URL + ROOT_URL+LIS_UNIT
									+ GET_TEMPLATE+"/"+$scope.organizationId+"/"+$scope.unitId+"/0/"+templateTypeId;
									GenericService
									.serviceAction("GET", URI, templeteData)
									.then(
											function(response) {
												$scope.templateList = [];

												if (response.data.status == "success") {
													$scope.templateList = response.data.listObject;
													//$scope.value =$scope.templateList[0].html;
													
													$scope.templateObj = {
															templetText:$scope.templateList[0].html,
															templeId  : $scope.templateList[0].labTemplateId,
															organizationId : $scope.organizationId,
															unitId : $scope.unitId,
															admissionId : $scope.admissionId,
															typeId  : templateTypeId
													}
													
													console.log("Templete For Dama",$scope.templateList);
												}
											});*/
									
						       		
						/*        	var data={
						        		  orgId : $scope.organizationId,
						        		  unitId : $scope.unitId,
						        		  templateTypeId : 1,
						        		  patientId : '',
						        		  visitTypeId: '',
						        		  encounterId: ''
						        		}
						        	
						        	$rootScope.startLoader();
					        	    var URI = BASE_URL + ROOT_URL + "/api/global/templates/bytype";
					        	    GenericService.serviceAction(METHOD_POST,URI,data).then(function(response) {
						    			if (response.data.status == "success")
						    			{
						    				
						    			 	$scope.templateList = response.data.listObject;
						    			 	$('#concentTemplate').modal('show');
											$rootScope.stopLoader();
										}
						    			else if(response.data.status == "error")
						    				{
						    				$rootScope.stopLoader();
						    				}
										
						    		});*/
						    	
									if(typeId==9){
										$("#checkBx").prop("checked", false);
										$scope.templateObj = { "consentTransactionId":"",
											"consentMasterId":"",
											"admissionId":$scope.itemSelecteds.admissionId,
											"encounterId":"",
											"templateDesc":"",
											"status":"A",
											"createdBy":$scope.userId,
											"createdDate":"",
											"updatedBy":$scope.userId,
											"updatedDate":"",
											"unitId":$scope.unitId,
											"organizationId":$scope.organizationId,
											"consentTypeId":"1"
										}
										
										
						    		  var data={
						    				  organizationId : $scope.organizationId,
						    				  consentTypeId : 1,
							        		  admissionId:$scope.itemSelecteds.admissionId
							        		}
							        	
							        	$rootScope.startLoader();
						        	    var URI = BASE_URL + ROOT_URL + "/adt/consentTemplate";
						        	    GenericService.serviceAction(METHOD_POST,URI,data).then(function(response) {
							    			if (response.data.status == "success")
							    			{
							    				$scope.templateList = [];
							    			 	$scope.templateList = response.data.listObject;
							    			 	$('#concentTemplateAdmittedList').modal('show');
												$rootScope.stopLoader();
											}
							    			else if(response.data.status == "error")
							    				{
							    				$rootScope.stopLoader();
							    				}
											
							    		});
									}
									if(typeId==13){
										$scope.serviceName = "";
										$scope.getListPackageOrder();
										$('#packageOrderList').modal('show');
									}
									//Code for package
								
						        	
								}
								
								
							}
							
							
							$scope.viewTemplate  = function(templateObj,index)
					    	{
								
								if(templateObj.templeteTypeId == 1){
									if(templateObj.templateDesc!=null)
									{
										$scope.value = templateObj.templateDesc;
										$scope.templateIndex = index;
										$('#addviewTemplateModalAdmission').modal('show');
									} 
								}else{
									if(templateObj.documentPath != null ){
										$scope.templateIndex = index;
										 $window.open(templateObj.documentPath, '_blank');
									}
									 
								}
								
															
								
					    	}
							
							
							$scope.printTemplate = function()
							{
								if($scope.selectedTemplates.length==0)
									{
									growl
									.error(
											'Please Select Template.',
											{
												title : 'Error!'
											});
									}
								else{
									
									var templateSaveList = [];
									angular.forEach($scope.selectedTemplates, function(value, key)
											{
										            var obj = angular.copy($scope.templateObj);
										            obj.consentTransactionId = value.consentTransactionId;
										            obj.consentMasterId =value.consentMasterId; 
										            obj.templateDesc =value.templateDesc;
										            templateSaveList.push(obj);
										   });
							        	
							         $rootScope.startLoader();
							         angular.element('#concentTemplateAdmittedList').modal('hide');
							         angular.element('#addviewTemplateModalAdmission').modal('hide');
						    		/* $('#concentTemplate').modal('hide');
						    		 $('#addviewTemplateModalAdmission').modal('hide');*/
						        	    var URI = BASE_URL + ROOT_URL + "/adt/patientConsent";
						        	    GenericService.serviceAction(METHOD_POST,URI,templateSaveList).then(function(response) {
							    			if (response.data.status == "success")
							    			{
												$rootScope.stopLoader();
												angular.forEach($scope.selectedTemplates, function(value, key)
														{
													
															if(value.templeteTypeId == 1){
																
																var data =  
																  {
															            "appName": "HIS",
															            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
															            "reportDesignFilePath": "getConsent.rpt",
															            "reportFormat": "PDF",
															            "exportFileName": "consentRpt",
															            "reportParams": [
															              {
																             "paramName": "admission_id",
																             "paramType": "Integer",
																             "paramValue": $scope.itemSelecteds.admissionId
															              },
															              {
																	             "paramName": "consent_master_id",
																	             "paramType": "Integer",
																	             "paramValue": value.consentMasterId
			                                                              },
			                                                              {
			                                                                    "paramName": "consent_type_id",
			                                                                    "paramType": "Integer",
			                                                                    "paramValue": 1//admission consent
			                                                              }
															            ]
															         };
																 reportService.generate(REPORT_API_URI1,data);
															}else{
																if(value.documentPath != null ){
																	 $window.open(value.documentPath, '_blank');
																}
															}
															 
													
													   });
												$scope.selectedTemplates = [];
												$scope.templateList = []
												$scope.commonFuntion(9);
											}
							    			else if(response.data.status == "error")
							    				{
							    				$rootScope.stopLoader();
							    				}
							    		});
									
									 
								}
								
							}
						
							$scope.saveTemplate = function()
							{
								 $scope.templateList[$scope.templateIndex].templateDesc = $scope.value;
								 $('#addviewTemplateModalAdmission').modal('hide');
									growl
									.success(
											'Consent Saved Successfully...!!',
											{
												title : 'Success!'
											});
							};
							
							$scope.checkNoOfPassAllowed=function(passTypeId){
								 $("#addPassIssueId").addClass("disabled");
								 $("#addPassIssueId").removeClass("enabled");
								 $scope.visiterPassMaster.passNumber="";
								 $scope.flag=false;
								if(passTypeId==0){
									 return false;
								}								
							
								var data={
										admissionId:$scope.itemSelecteds.admissionId,
										visitTypeId :  $scope.itemSelecteds.visitTypeId,
										bedCategoryId :$scope.itemSelecteds.bedCategoryId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										visitorPassTypeId : passTypeId,
									}
								var URI = BASE_URL + ROOT_URL+ "/adt/checkNoOfPassIssueAgainstAdmission";
								GenericService.serviceAction("POST", URI, data)
								.then(function(response) {
											if (response.data.status == "success") {												
												$scope.noOfPassIssuedList = response.data.listObject[0];
												$scope.noOfPassHasToGiven=$scope.noOfPassIssuedList.noOfPassCount;
												if($scope.noOfPassIssuedList.noOfPassAllowed==null){
													
													 $("#addPassIssueId").addClass("disabled");
													 $("#addPassIssueId").removeClass("enabled");
													 $scope.flag=true;
													 growl.warning(
																'visitor pass is not configure.',
																{
																	title : 'warning!'
																});
													 return false;
												}else
												if($scope.noOfPassHasToGiven>=$scope.noOfPassIssuedList.noOfPassAllowed){
													$("#addPassIssueId").addClass("disabled");
													$("#addPassIssueId").removeClass("enabled");
													$scope.flag=true;
													growl.warning('Cannot add visitor passes as beyond limit..',
															{
																title : 'Limit Exceeded!'
															});
													return false;
												}else{
													$("#addPassIssueId").removeClass("disabled");
													$("#addPassIssueId").addClass("enabled");
													$scope.getPassNumber("");
												}
												
											}
										});
				 
							}
							 $scope.getPassNumber=function(passNo){
								 $("#addPassIssueId").addClass("disabled");
								 $("#addPassIssueId").removeClass("enabled");
								 if($scope.visiterPassMaster.visitorPassTypeId==0){
									 growl.warning(
												'please select visitor pass type.',
												{
													title : 'warning!'
												});
									 return false;
								 }
								
								 var data = {
										   organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											passNumber : passNo.trim()
										};
								 if(passNo=="" || $scope.flag){									
									 return false;
								 }
										var URI = BASE_URL + ROOT_URL
												+ "/adt/checkPassNumberAlreadyIssueOrNot";
										GenericService
												.serviceAction("POST", URI, data)
												.then(
														function(response) {
															$scope.passNumberExistList ="";

															if (response.data.status == "success") {
																$scope.passNumberExistList = response.data.object;																	
																if($scope.passNumberExistList>0){
																	$("#addPassIssueId").addClass("disabled");
																	$("#addPassIssueId").removeClass("enabled");
																
																	 growl.warning(
																				'pass already issued.',
																				{
																					title : 'warning!'
																				});
																	 return false;
																	 
																}else{
																	$("#addPassIssueId").removeClass("disabled");
																	$("#addPassIssueId").addClass("enabled");
																}
															}
														});
								 
							 }
							$scope.savePassIssue = function() {
								$("#addPassIssueId").addClass("disabled");
								$("#addPassIssueId").removeClass("enabled");
								$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								var pddDate = moment($scope.itemSelecteds.datepickerPdd).format('DD/MM/YYYY');
								var pdd = $rootScope
										.getChangedFormatedDateShalse(pddDate);
								$scope.listVisiterPassMaster=[];
								var data1={
										"visitorPassTypeId":$scope.visiterPassMaster.visitorPassTypeId,
					        			"passNumber":$scope.visiterPassMaster.passNumber,
					        			"patientId":$scope.selectedPatientId
								}
								$scope.listVisiterPassMaster.push(data1);
								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									visitorPassTypeId : $scope.visiterPassMaster.visitorPassTypeId,
									passNumber : $scope.visiterPassMaster.passNumber,
									patientId : $scope.selectedPatientId,
									admissionId : $scope.itemSelecteds.admissionId,
									expiryDate : pdd + ' 00:00:00',
									createdBy : $scope.userId,
									updatedBy : $scope.userId,
									status : 'A',
									visitorPassStatus : 'I',
									visitTypeId :  $scope.itemSelecteds.visitTypeId,
									bedCategoryId :$scope.itemSelecteds.bedCategoryId,
									createdDate : $scope.currentDate,
									updatedDate : $scope.currentDate,
									listVisiterPassMaster:$scope.listVisiterPassMaster

								};
								console.log(data);
//								 return false;
								$scope.data1 = data;
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
										+ ADDVISITORPASSISSUE;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														growl
																.success(
																		'Pass Issued Successfully...!!',
																		{
																			title : 'Success!'
																		});
														$scope
																.$broadcast('show-errors-reset');
														$("#addPassIssueId").addClass("disabled");
														$("#addPassIssueId").removeClass("enabled");
														$scope
																.getPassIssuedList($scope.itemSelecteds.admissionId);
														$scope.visitPassObj();
													} else {
														growl
																.error(
																		'Something went wrong...!!',
																		{
																			title : 'Error!'
																		});
														$("#addPassIssueId").addClass("disabled");
														$("#addPassIssueId").removeClass("enabled");
													}
													$rootScope.stopLoader();
												});
							}

							$scope.printWristBand = function()
							{
								var printContents = $window.document.getElementById('opdWristCode').innerHTML;
								var popupWin = $window.open('', '_blank', 'width=300,height=300');
								popupWin.document.open();
								popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
								popupWin.document.close();
							}

							$scope.isPRNRequired;
							$scope.wristBandList;
							$scope.renderInfo;

							$scope.callPrnFileSizeConfiguration = function()
							{
                             if (Object.keys($scope.itemSelecteds).length === 0) {
                             	growl
                             	.warning(
                             	'Please select atleast one patient!!!.',
                             		{
                             			title : 'Warning!'
                             			});
                             			return false;
                             								}
								$('#wristBandAdmitted').modal('hide');
								var data={
									"organizationId" :$scope.organizationId,
									"unitId" :$scope.unitId
								}
								var URI = BASE_URL + ROOT_URL + "/api/opd/prnConfiguration";
								GenericService
								.serviceAction("POST",URI,data)
								.then(function(response) {
									console.log("response",response)
									$('#Prn').modal('show');
									$scope.isPRNRequired=response.data.listObject[0];
									$scope.wristBandList=response.data.object;

								})
							}

							$scope.generateWristBand1 = function(info,labelName)
							{
								$scope.prn=info.labelName;
								$scope.prnFormat=info.prnFormat;
							}

							$scope.generateWristBand = function(info)
							{
								info = $scope.itemSelecteds;
								if($scope.isPRNRequired==true)
								{
									var info1={
										patientId:info.patientId,
										templateDesc:$scope.prnFormat,
										admissionId:info.admissionId,
										encounterId:info.encounterId,
										wristBandPrnSize:$scope.prn,
									}
						    		$scope.printRadiologyReport(info1,null);
									$('#Prn').modal('hide');
								}
								else
								{
									$('#wristBandAdmitted').modal('show');
									if(info.patientId > 0)
									{
										var age = $rootScope.getAge(info.dob);
										$scope.patientFullName = info.patientName + " " + age + "/" + info.genderCode;
									}
									else
									{
										$scope.patientFullName = info.patientName + " " + info.dob + "/" + info.genderCode;
									}
									
									$scope.barcode = {
										type: "code128",
										code: info.uhidNumber,
										hospital: $scope.unitName,
										itemCode: info.encounterId.toString(),
										batchCode: "123456",
										patientFullName : $scope.patientFullName,
										doctorName : (info.doctorName!= undefined && info.doctorName != null ) ?  info.doctorName : '',       
									}
								}     
							}

							$scope.printRadiologyReport=function(info,printWithHeader){
							    	
								info.organizationId = $scope.organizationId;
								info.unitId =  $scope.unitId;
//							    	info.printWithHeader = printWithHeader;
								var URI = BASE_URL + ROOT_URL + "/LIS/transaction/wristBandPrnPrint";
								GenericService.serviceAction(METHOD_POST, URI, info,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
										{
							
									   if(response.data.status=='success')
										  {
										   
										   if($scope.isPRNRequired!=undefined && $scope.isPRNRequired!=null && $scope.isPRNRequired==true && response.data.object.templateDesc!=null){
												//JSPrinterManager call
//													print($scope.finalPRN);
												//For PRN Print manager
													$scope.printPRNPrint(response.data.object.templateDesc);
											}														
										   console.log("finalPRN",response.data.object.templateDesc);
										  }
									   else if(response.data.status=='error'){
										   $rootScope.stopLoader();
									   }
								});
							
							}

							var labConfig  =    $scope.checkLabConfig();
							$scope.prnUrl = labConfig.prnUrl;

							$scope.printPRNPrint= function(prnObject){						
									
									
								var url = $scope.prnUrl;

								var xhr = new XMLHttpRequest();
								xhr.open("POST", url);

								xhr.setRequestHeader("Content-Type", "text/plain");

								xhr.onreadystatechange = function () {
								   if (xhr.readyState === 4) {
									  console.log(xhr.status);
									  console.log(xhr.responseText);
								   }};

								var data = prnObject;
								xhr.send(data);
							}
							
							$scope.generateBarCode = function()
							{
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient!!!.',
													{
														title : 'Warning!'
													});
								} else {
								console.log("ffd",$scope.itemSelecteds);
								
								if($scope.itemSelecteds.patientId > 0)
								{
								var age = $rootScope
								.getAge($scope.itemSelecteds.dob);
								$scope.patientFullName = $scope.itemSelecteds.patientFullName
								+ " "
								+age
								+ "/"
								+ $scope.itemSelecteds.genderCode;
								}
							else
								{
								$scope.patientFullName = $scope.itemSelecteds.patientFullName
								+ " "
								+ $scope.itemSelecteds.dob
								+ "/"
								+ $scope.itemSelecteds.genderCode;
								}
								
								$scope.barcode = {
									        type: "code128",
									        code: $scope.itemSelecteds.uhidnumber,
									        hospital: $scope.unitName,
									        itemCode: $scope.itemSelecteds.admissionId.toString(),
									        batchCode: "123456",
									        patientFullName : $scope.patientFullName,
									        bedNumber:$scope.itemSelecteds.bedNumber,
									        wardName:$scope.itemSelecteds.wardName,
									        doctorName : $scope.itemSelecteds.doctorFullName
									       
									      }
								
							}
									      
							}
							
							$scope.printWristBand = function()
							{
								var printContents = $window.document.getElementById('admittedWristCode').innerHTML;
						        var popupWin = $window.open('', '_blank', 'width=300,height=300');
						        popupWin.document.open();
						        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
						        popupWin.document.close();
							}
							
							
							$scope.generateBarCode = function()
							{
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
								} else {
								console.log("ffd",$scope.itemSelecteds);
								
								if($scope.itemSelecteds.patientId > 0)
								{
								var age = $rootScope
								.getAge($scope.itemSelecteds.dob);
								$scope.patientFullName = $scope.itemSelecteds.patientFullName
								+ " "
								+age
								+ "/"
								+ $scope.itemSelecteds.genderCode;
								}
							else
								{
								$scope.patientFullName = $scope.itemSelecteds.patientFullName
								+ " "
								+ $scope.itemSelecteds.dob
								+ "/"
								+ $scope.itemSelecteds.genderCode;
								}
								
								$scope.barcode = {
									        type: "code128",
									        code: $scope.itemSelecteds.uhidnumber,
									        hospital: $scope.unitName,
									        itemCode: $scope.itemSelecteds.admissionId.toString(),
									        batchCode: "123456",
									        patientFullName : $scope.patientFullName,
									        bedNumber:$scope.itemSelecteds.bedNumber,
									        wardName:$scope.itemSelecteds.wardName
									       
									      }
								
							}
									      
							}
							
							$scope.printBarCode = function()
							{
								var printContents = $window.document.getElementById('admittedBarCode').innerHTML;
						        var popupWin = $window.open('', '_blank', 'width=300,height=300');
						        popupWin.document.open();
						        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
						        popupWin.document.close();
							}
							
							 $scope.removeVisiterPassMaster = function(deleteIndex)
								{
									$scope.listVisiterPassMaster.splice(deleteIndex, 1);
							     }
/**===========Deposit Collection On Reservation========================*/
							

							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
								};
							
							$scope.checkDateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate:moment().subtract(3, 'months').toDate()
								};
							
							$scope.CardDateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate:new Date()
								};
							
							$scope.dateOptionsDoa = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate:new Date()
								};
							
							
							$scope.openDatePickeradmissionDate = function() {
								$scope.datepickeradmissionDate.opened= true;
							};
							
							$scope.datepickeradmissionDate = {
									opened : false
								};
							
							$scope.openDatePicker = function() {
								$scope.datepicker.opened = true;
							};
							
							
							//for cheque
							$scope.openDatePickerCheque = function() {
								$scope.datepickercheque.opened = true;
							};
							$scope.datepickercheque = {
									opened : false
								};
							//------

							$scope.datepicker = {
								opened : false
							};
							
							$scope.openDatePickerPatiSearch = function() {
								$scope.datepicker1.opened = true;
							};

							$scope.datepicker1 = {
									opened : false
								};
							
							$scope.openDatePickerFromDate = function() {
								$scope.datepickerFrom.opened = true;
							};

							$scope.datepickerFrom = {
									opened : false
								};
							
							$scope.openDatePickerToDate = function() {
								$scope.datepickerTo.opened = true;
							};

							$scope.datepickerTo = {
									opened : false
								};
							
							
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
							
							$scope.collectDeposit = function()
								{
								
								
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
								}
								else
								{
								$rootScope.startLoader();
								$scope.patientDetails.patientName = $scope.itemSelecteds.patientFullName;
								$scope.patientDetails.genderCode= $scope.itemSelecteds.genderCode;
								$scope.patientDetails.age= moment().diff($scope.itemSelecteds.dob, 'years');
								$scope.patientDetails.uhidNumber= $scope.itemSelecteds.uhidnumber;
								$scope.patientDetails.dob= $scope.itemSelecteds.dob;
								$scope.patientDetails.doctorFullName= $scope.itemSelecteds.doctorFullName;
								$scope.patientDetails.dateOfAdmission= $scope.itemSelecteds.datepicker;
								$scope.patientDetails.patientDischargeDate=$scope.itemSelecteds.datepickerPdd;
								$scope.patientDetails.pddActual = $scope.patientDetails.patientDischargeDate;
								$scope.patientDetails.doaActual = $scope.patientDetails.dateOfAdmission;
								$scope.patientDetails.wardName= $scope.itemSelecteds.wardName;
								$scope.patientDetails.bedNumber= $scope.itemSelecteds.bedNumber;
								$scope.patientDetails.imagePath= "";
								$scope.patientDetails.patientId= $scope.itemSelecteds.patientId;
								$scope.patientDetails.visitTypeId= $scope.itemSelecteds.visitTypeId;
								$scope.patientDetails.admissionId= $scope.itemSelecteds.admissionId;
								$scope.patientDetails.genderId= $scope.itemSelecteds.genderId;
								$scope.deposit.patientId= $scope.itemSelecteds.patientId;
								$scope.deposit.visitTypeId =  $scope.itemSelecteds.visitTypeId;
								$scope.deposit.depositTypeId =2;
								 $scope.deposit.patientId =$scope.itemSelecteds.patientId;
							     $scope.billing = true;
							     $scope.comorbidityFlag = false;
								
								var promise = $scope.getEncounters();
								promise.then(function(encounterRes) 
										{
										  if(encounterRes.data.status == "success") 
											  {
											    if(encounterRes.data.listObject!=null && encounterRes.data.listObject.length>0)
											    	{
											    	angular.forEach(encounterRes.data.listObject, function(value, key)
															{
														 if(value.encounterId==$scope.itemSelecteds.admissionId)
												    	   {
												    	      $scope.deposit.encounterId = value.encounterId+"";
												    	      $rootScope.stopLoader();
												    	   }
														});
											    	}
											    else{
											    	  $scope.deposit.depositTypeId=1+"";
											        }
												$('#collectDepositModal').modal('show');
										      }
										  else{
											  $rootScope.stopLoader();
										  }
									    });
								
								}
								    
								  
								
							}
							
							
							$scope.collectPayment = function()
							{
								$scope.skipMpesaValidation = false;
								$scope.pinpadAmounFlag = false;
								if($scope.deposit.depositTypeId==null||$scope.deposit.depositTypeId=="" || isNaN( $scope.deposit.depositTypeId ))
								{
								growl.warning("Please Select Deposit Against.",{
									title : WARNING_MSG
								});
								    return false;
								}
							  if($scope.deposit.depositTypeId==2)
							  {
								  if($scope.deposit.visitTypeId==''||$scope.deposit.visitTypeId==null)
									  {
									  growl.warning("Please Select Visit Type.",{
											title : WARNING_MSG
										   });
										    return false;
									  }
								  if($scope.deposit.encounterId==''||$scope.deposit.encounterId==null)
									  {
									  growl.warning("Please Select Visit Number.",{
											title : WARNING_MSG
										   });
										    return false;
									  }
							  }
								$scope.dueAmount = isNaN(Number(parseFloat($scope.dueAmount)))?0:$scope.dueAmount;
								if($scope.dueAmount == 0){
									growl.error("Please Enter Valid Amount.",{
										title : ERROR_MSG
									});
									return;
								}else{
									$scope.send=false;
									$scope.resend=false;	
									$scope.paymentDetails.otp ="";
									$scope.validatTextFieldFlag = true;
									$scope.initPaymentDetailsObj();
									$scope.listPaymentDetails = [];
									$scope.setFirstTabLevel(1);
							        $scope.setPaymentModeId(1);
								 $scope.paymentDetails.cashAmount = $scope.dueAmount;
								 $scope.paymentDetails.loyaltyCardNo = '';
								 $scope.paymentDetails.cardBalAmount = '';
								 $scope.calculateDueAmount();
								 $scope.calculateTotalAmount();
								 $scope.loyaltyCardObj ={
											
											"loyaltyCardNo" :"",
											"loyaltyCardBalance" :"",
											"authKey":0
									}
								 $('#collectPaymentModal').modal('show');
								}
							}
							
						
							$scope.tempVisitTypeId = '';
							$scope.tempEncounterId = '';
								$scope.changeVisit = function(depositTypeId){
									if(depositTypeId == 1){
										$scope.tempVisitTypeId = $scope.deposit.visitTypeId;
							            $scope.tempEncounterId = $scope.deposit.encounterId ;
										$scope.deposit.visitTypeId = '';
										$scope.deposit.encounterId = '';
									}else{
										$scope.deposit.visitTypeId = $scope.tempVisitTypeId;
										$scope.deposit.encounterId = $scope.tempEncounterId;
									}
								}
							
							//get encounters for deposit 
							$scope.getEncounters = function()
							{
								$scope.visitNoList=[];
								if($scope.deposit.visitTypeId != null && $scope.deposit.visitTypeId != '' && typeof($scope.deposit.visitTypeId) != "undefined" && $scope.deposit.patientId != '' && $scope.deposit.patientId != null && typeof($scope.deposit.patientId) != 'undefined'){
								var param = {patientId:$scope.deposit.patientId,orgId:$scope.organizationId,unitId:$scope.unitId,visitTypeId:$scope.deposit.visitTypeId};
								var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit/encounters";
								return BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success")
									{
										$scope.visitNoList = response.data.listObject;
									}
									return response;
								});
								}
							}
							
							
						
							
							$scope.getBankList = function(){
								var URI = BASE_URL + ROOT_URL + "/api/billing/bankMaster";
								var param = {orgnisationId:$scope.organizationId,unitId:$scope.unitId};
								BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$scope.bankList = response.data.listObject;
									}
								});
							}
							
							//ADDED LATER GANESH
							$scope.getHospitalBankList = function()
							{
								var URI = BASE_URL + ROOT_URL + "/api/billing/hospitalBank/"+$scope.organizationId+"/"+$scope.unitId;
								BillingGenericService.serviceAction(METHOD_GET,URI,{},NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
							       {
									if (response.data.status == "success")
									{
										$scope.hospitalBankList = response.data.listObject;
									}
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
								$scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:$scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
							}
							
							$scope.initDepositObj = function(){
								$scope.deposit = angular.copy({depositTypeId:"",visitTypeId:"",depositAmount:"",depositPending:"",remarks:"",receivedFrom:"",identificationNo:"",patientId:"",encounterId:"",admNoteId:""})
							}
							
							$scope.initDepositSearchObj = function(){
								$scope.depositSearch = angular.copy({fromDate:"",toDate:"",patientName:"",uhidNumber:"",visitTypeId:"",depositTypeId:""});
							}
							
							$scope.initDepositSearchDtls = function(){
								$scope.depositSearchDtls = angular.copy({totalDepositAmount:parseFloat(0).toFixed(2),totalConsumedDepositAmount:parseFloat(0).toFixed(2),totalBalanceAmount:parseFloat(0).toFixed(2)});
							}

							$scope.isSetFirstTabLevel = function(tabNum)
							{
								return $scope.firstTabLevel === tabNum;
							};
							$scope.addBtnId=false;
							$scope.pinpadAmounFlag =false;
							$scope.setPaymentModeId = function(id)
							{
								if(id==3 && $scope.isPinPadRequired =='Y'){
									$scope.addBtnId=true;
									// $scope.pinpadAmounFlag =true;
									$scope.objPosMachine.deviceName= $cookies.getObject('deviceName');
								}else if(id==6){
									$scope.addBtnId=true;
								}
								else{
									$scope.addBtnId=false;
									 $scope.pinpadAmounFlag =false;
								}
								$scope.paymentDetails.paymentModeId = id;
							}
							
							$scope.addPaymentDetails = function(){
								
								//check amount collected is greater tha deposit amount
								var depositAmount = $scope.deposit.depositAmount != "" && typeof($scope.deposit.depositAmount) != "undefined" ? parseFloat($scope.deposit.depositAmount) : parseFloat(0) ;
								var collectedAmount = 0;
								var tempAmount = 0;
								
								switch($scope.paymentDetails.paymentModeId){
								case 1 :
									tempAmount = $scope.paymentDetails.cashAmount != '' && typeof($scope.paymentDetails.cashAmount) != 'undefined' ? parseFloat($scope.paymentDetails.cashAmount) : parseFloat(0);
									if(parseFloat($scope.paymentDetails.cashAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return;
									}
									var cashLimit = 0;
									angular.forEach($scope.listPaymentDetails,function(value,key){
										
										if(value.paymentModeId == 1){
											cashLimit = parseFloat(cashLimit) + parseFloat(value.amount);
										}
									});
									cashLimit = cashLimit + tempAmount;
									if(cashLimit > ($scope.cashAmountLimit != undefined && $scope.cashAmountLimit != null ? $scope.cashAmountLimit : 0)  ){
										growl.warning("System can not accept more than "+$scope.cashAmountLimit +" in cash amount",{
											title : WARNING_MSG
										});
										return;
									}
									
										break;
								case 2 :
									
									if($scope.paymentDetails.bankId==null||$scope.paymentDetails.bankId==''){
										growl.warning("Please select bank.",{
											title : WARNING_MSG
										});
										return;
									}
									if($scope.paymentDetails.hospitalAccountId==null||$scope.paymentDetails.hospitalAccountId==''){
										growl.warning("Please select hospital account..",{
											title : WARNING_MSG
										});
										return;
									}
									if($scope.paymentDetails.chequeNumber==null||$scope.paymentDetails.chequeNumber==''){
										growl.warning("Please enter cheque number..",{
											title : WARNING_MSG
										});
										return;
									}
									if($scope.paymentDetails.chequeDate==null||$scope.paymentDetails.chequeDate==undefined||$scope.paymentDetails.chequeDate==''){
										growl.warning("Please enter cheque date..",{
											title : WARNING_MSG
										});
										return;
									}
									tempAmount = $scope.paymentDetails.chequeAmount != '' && typeof($scope.paymentDetails.chequeAmount) != 'undefined' ? parseFloat($scope.paymentDetails.chequeAmount) : parseFloat(0);
									if(parseFloat($scope.paymentDetails.chequeAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return;
									}
									
									
									var chequeNo = false;
									angular.forEach($scope.listPaymentDetails,function(value,key){
										if(value.paymentModeId == 2 && value.chequeNumber == $scope.paymentDetails.chequeNumber){
											chequeNo = true;
										}
									});
									if(chequeNo){
										growl.warning("this cheque number already  added",{
											title : WARNING_MSG
										});
										return;
									}
									
									break;
								
								case 3 :
									
								/*	if($scope.paymentDetails.credCardHolderName==null||$scope.paymentDetails.credCardHolderName==''){
										growl.warning("Please enter card holder name.",{
											title : WARNING_MSG
										});
										return;
									}
									if($scope.paymentDetails.credCardTypeId==null||$scope.paymentDetails.credCardTypeId==''){
										growl.warning("Please select card type..",{
											title : WARNING_MSG
										});
										return;
									}*/
									if($scope.paymentDetails.credCardNumber==null||$scope.paymentDetails.credCardNumber==''){
										growl.warning("Please enter card number..",{
											title : WARNING_MSG
										});
										return;
									}
									if(typeof($scope.paymentDetails.transactionNo) == "undefined" || $scope.paymentDetails.transactionNo == null || 
											$scope.paymentDetails.transactionNo == "" ){
										growl.error("Please enter transaction no",{
											title : ERROR_MSG
										});
										return;
									}
									/*if($scope.paymentDetails.credExpiryDate==null||$scope.paymentDetails.credExpiryDate==undefined||$scope.paymentDetails.credExpiryDate==''){
										growl.warning("Please enter expiry date..",{
											title : WARNING_MSG
										});
										return;
									}*/
									tempAmount = $scope.paymentDetails.credAmount != '' && typeof($scope.paymentDetails.credAmount) != 'undefined' ? parseFloat($scope.paymentDetails.credAmount) : parseFloat(0);
									if(parseFloat($scope.paymentDetails.credAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return;
									}
									break;
								
								case 4 :
									tempAmount = $scope.paymentDetails.debAmount != '' && typeof($scope.paymentDetails.debAmount) != 'undefined' ? parseFloat($scope.paymentDetails.debAmount) : parseFloat(0);
									if(parseFloat($scope.paymentDetails.debAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return;
									}
									break;
									
								case 5 :
									
									if($scope.mpesaObject.transactionId == undefined || $scope.mpesaObject.transactionId == null || $scope.mpesaObject.transactionId == ""){
										growl.warning("Please Enter Transaction Id !!",{
											title : WARNING_MSG
										});
										return false;
									}
									if($scope.mpesaObject.mobileNo == undefined || $scope.mpesaObject.mobileNo == null || $scope.mpesaObject.mobileNo == ""){
										growl.warning("Please Enter Mobile Number !!",{
											title : WARNING_MSG
										});
										return false;
									}
									
									tempAmount = $scope.mpesaObject.mpesaAmount!=null && $scope.mpesaObject.mpesaAmount != '' && typeof($scope.mpesaObject.mpesaAmount) != 'undefined' ? parseFloat($scope.mpesaObject.mpesaAmount) : parseFloat(0);
									
									if(parseFloat($scope.mpesaObject.mpesaAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return false;
									}
									var invalidMpesa = false;
									angular.forEach($scope.listPaymentDetails,function(value,key){
										if($scope.mpesaObject.transactionId == value.cardNo)
										{
											growl.warning("Collected MPesa is already used in current tranasction",{
												title : WARNING_MSG
											});
											invalidMpesa = true;
										}
									});
									if(invalidMpesa == true)
										return false;
									
									break;
									
								case 8:
									
									tempAmount = $scope.eTransferObj.transferAmount != '' && typeof($scope.eTransferObj.transferAmount) != 'undefined' ? parseFloat($scope.eTransferObj.transferAmount) : parseFloat(0);
									
									if(parseFloat($scope.eTransferObj.transferAmount) > depositAmount){
										growl.warning("Entered amount should be equal to or less than deposit amount",{
											title : WARNING_MSG
										});
										return;
									}
									if($scope.eTransferObj.transactionobj.id == ""){
										growl.warning("Please Select Transaction Type",{
											title : WARNING_MSG
										});
										return;	
									}
									if($scope.eTransferObj.transferAmount == 0 || $scope.eTransferObj.transferAmount == null){
										growl.warning("Amount cannot be zero",{
											title : WARNING_MSG
										});
										return;
									}
									if($scope.eTransferObj.transBankId == "" || $scope.eTransferObj.transBankId == "-"){
										growl.warning("Please Select Bank",{
											title : WARNING_MSG
										});
										return;	
									}
									if($scope.eTransferObj.transactionNo == "" || $scope.eTransferObj.transactionNo == undefined){
										growl.warning("Please Enter Transaction Number",{
											title : WARNING_MSG
										});
										return;	
									}
									if($scope.eTransferObj.transTransactionDate == ""){
										growl.warning("Please Select Transaction Date",{
											title : WARNING_MSG
										});
										return;	
									}
									var invalidETransfer = false;
									angular.forEach($scope.listPaymentDetails,function(value,key){
										if(value.paymentModeId == 8 && $scope.eTransferObj.transactionNo == value.transactionNo)
										{
											growl.warning("Collected E-Transfer is already used in current tranasction",{
												title : WARNING_MSG
											});
											invalidETransfer = true;
										}
									});
									if(invalidETransfer == true)
										return;
									break;
									
								case 6 :
									
									tempAmount = $scope.paymentDetails.loyaltyCardAmt != '' && typeof($scope.paymentDetails.loyaltyCardAmt) != 'undefined' ? parseFloat($scope.paymentDetails.loyaltyCardAmt) : parseFloat(0);
									if(typeof($scope.paymentDetails.loyaltyCardAmt) == "undefined" || $scope.paymentDetails.loyaltyCardAmt == ""){
										growl.error("Please insert an amount",{
											title : ERROR_MSG
										});
										return;
									}
									
										if(parseFloat($scope.paymentDetails.loyaltyCardAmt) > parseFloat($scope.paymentDetails.cardBalAmount))
										{
											growl.error("Entered Amount Should be less than Card Amount.",{
												title : ERROR_MSG
											});
											return 
										 }
										break;
								}
								
								if(tempAmount == 0){
									growl.warning("Amount cannot be zero",{
										title : WARNING_MSG
									});
									return;
								}
								
								angular.forEach($scope.listPaymentDetails,function(value,key){
									collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
								});
								collectedAmount = collectedAmount + tempAmount;
								if(collectedAmount > depositAmount){
									growl.warning("Entered amount should be equal to or less than deposit amount",{
										title : WARNING_MSG
									});
									return;
								}
								
								switch($scope.paymentDetails.paymentModeId){
									case 1 :
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.cashAmount).toFixed(2),createdBy:$scope.userId};
										$scope.listPaymentDetails.push(paymentDetails);
										break;
									case 2 :
										var bankName = "";
										//Get Bank Name
										angular.forEach($scope.bankList,function(value,key){
											if($scope.paymentDetails.bankId == value.bankId)
												bankName = value.bankName;
										})
										var chequeDate = $scope.getFormattedDate($scope.paymentDetails.chequeDate,false);
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.chequeAmount).toFixed(2),createdBy:$scope.userId, bankId:$scope.paymentDetails.bankId, chequeNumber:$scope.paymentDetails.chequeNumber, chequeDate:chequeDate, bankName:bankName,hospitalAccountId:$scope.paymentDetails.hospitalAccountId};
										$scope.listPaymentDetails.push(paymentDetails);
										break;
									case 3 :
										var expiryDate =  $scope.getFormattedDate($scope.paymentDetails.credExpiryDate,false);
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.credAmount).toFixed(2),createdBy:$scope.userId, cardHolderName:$scope.paymentDetails.credCardHolderName,cardTypeId:$scope.paymentDetails.credCardTypeId,cardNo:$scope.paymentDetails.credCardNumber,expiryDate:expiryDate,transactionNo:$scope.paymentDetails.transactionNo,skipMpesaValidation:$scope.pinpadAmounFlag!=undefined ? $scope.pinpadAmounFlag : null, requestedUniqueId:$scope.paymentDetails.requestedUniqueId,deviceName : $scope.objPosMachine.deviceName, creditDebitCardType:$scope.paymentDetails.creditDebitCardType, transactionTypeName:$scope.paymentDetails.transactionTypeName, transactionDetails:$scope.paymentDetails.transactionDetails};
										$scope.listPaymentDetails.push(paymentDetails);
										$scope.clearCardFields();
										break;
									case 4 :
										var expiryDate =  $scope.getFormattedDate($scope.paymentDetails.debExpiryDate,false);
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.debAmount).toFixed(2),createdBy:$scope.userId, cardHolderName:$scope.paymentDetails.debCardHolderName,cardTypeId:$scope.paymentDetails.debCardTypeId,cardNo:$scope.paymentDetails.debCardNumber,expiryDate:expiryDate};
										$scope.listPaymentDetails.push(paymentDetails);
										break;
										
									case 5:				
										var paymentDetails = { paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.mpesaObject.mpesaAmount).toFixed(2), createdBy:$scope.userId, cardNo:$scope.mpesaObject.transactionId,transactionNo:$scope.mpesaObject.transactionId,skipMpesaValidation : $scope.skipMpesaValidation };
										$scope.listPaymentDetails.push(paymentDetails);
										$scope.clearMpesaFields();
										break;
										
									case 8 : 
										var bankName = "";
										//Get Bank Name
										angular.forEach($scope.bankList,function(value,key){
											if($scope.eTransferObj.transBankId == value.bankId)
												bankName = value.bankName;
										})
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.eTransferObj.transferAmount).toFixed(2), transactionType:$scope.eTransferObj.transactionobj.name, transactionTypeId:$scope.eTransferObj.transactionobj.id,transTransactionDate:moment($scope.eTransferObj.transTransactionDate).format('DD-MM-YYYY HH:mm:ss'),bankId:$scope.eTransferObj.transBankId,transactionNo:$scope.eTransferObj.transactionNo,createdBy:$scope.userId,bankName:bankName};
										   $scope.listPaymentDetails.push(paymentDetails);
										break;
										
									case 6:				
										var paymentDetails = {paymentModeId:$scope.paymentDetails.paymentModeId, amount:parseFloat($scope.paymentDetails.loyaltyCardAmt).toFixed(2),cardNo:$scope.paymentDetails.loyaltyCardNo,createdBy:$scope.userId};
										$scope.listPaymentDetails.push(paymentDetails);
										 $scope.send=false;
										 $scope.resend=false;
										 $scope.validatTextFieldFlag = true;
										break;
								}
								$scope.initPaymentDetailsObj();
								$scope.setFirstTabLevel(1);
						        $scope.setPaymentModeId(1);
								$scope.calculateDueAmount();
								$scope.calculateTotalAmount();
								
							}
							
							$scope.calculateDueAmount = function(){
								//calculate due amount
								var collectedAmount = parseFloat(0).toFixed();
								angular.forEach($scope.listPaymentDetails,function(value,key){
									collectedAmount = parseFloat(collectedAmount) + parseFloat(value.amount);
								});
								$scope.dueAmount = $scope.deposit.depositAmount != '' && typeof($scope.deposit.depositAmount) != 'undefined' ? parseFloat(parseFloat($scope.deposit.depositAmount) - collectedAmount).toFixed(2) : parseFloat(0).toFixed(2);
								$scope.paymentDetails.cashAmount = $scope.dueAmount;
								$scope.paymentDetails.chequeAmount = $scope.dueAmount;
								$scope.paymentDetails.debAmount = $scope.dueAmount;
								$scope.paymentDetails.credAmount = $scope.dueAmount;
								$scope.eTransferObj.transferAmount = $scope.dueAmount;
								$scope.paymentDetails.loyaltyCardAmt =  $scope.dueAmount;
							}
							
							$scope.calculateTotalAmount = function(){
								//calculate due amount
								var tatalAmount = parseFloat(0).toFixed();
								angular.forEach($scope.listPaymentDetails,function(value,key){
									tatalAmount = parseFloat(tatalAmount) + parseFloat(value.amount);
								});
								$scope.tatalAmount = tatalAmount;
							//	$scope.deposit.depositAmount  = $scope.tatalAmount;
							}
							
							//remove payment details
							$scope.removePaymentDetails = function(index){
								
								var tempObj = (index==undefined)?null:$scope.listPaymentDetails[index];
								if(tempObj!=null && (tempObj.cardNo == $scope.paymentDetails.loyaltyCardNo))
									{
									 $scope.paymentDetails.cardBalAmount = $scope.paymentDetails.cardBalAmount +parseFloat(tempObj.amount);
									 $scope.send=false;
									 $scope.resend=false;
									}
								$scope.listPaymentDetails.splice(index,1);
								$scope.calculateDueAmount();
								$scope.calculateTotalAmount();
							}
							
							$scope.parseFloatNum = function(number){
								return (number != '' && typeof(number) != 'undefined' && !isNaN(number)) ? parseFloat(number).toFixed(2) : parseFloat(0).toFixed(2);
							}
							
							$scope.addDeposit = function(){
								
								
								
								
								$('#collectPaymentModal').modal('hide');
								$('#collectDepositModal').modal('hide');
								
								
								
								if($scope.dueAmount > 0){
									growl.error("Please clear the due amount",{
										title : ERROR_MSG
									});
									return;
								}
								$rootScope.startLoader();
								$scope.deposit.patientId = $scope.itemSelecteds.patientId;
								
								$scope.deposit.createdBy = $scope.userId;
								$scope.deposit.orgId = $scope.organizationId;
								$scope.deposit.unitId = $scope.unitId; 
								
								if($scope.deposit.depositTypeId == 1)
									{
									$scope.deposit.payeeId = 1;
									$scope.deposit.admissionId = '';
									$scope.deposit.visitTypeId = '';
									}
								else{
									$scope.deposit.admissionId = $scope.itemSelecteds.admissionId;
									$scope.deposit.visitTypeId = $scope.itemSelecteds.visitTypeId;
								
								}
								var param = {depositMasterDto:$scope.deposit,listPaymentDepositDetailsDto:$scope.listPaymentDetails};
								var URI = BASE_URL + ROOT_URL + "/api/billing/unit/deposit";
								BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
									if (response.data.status == "success"){
										$scope.send = false;
										$scope.validatTextFieldFlag = true;
										$scope.initPaymentDetailsObj();
										$scope.listPaymentDetails = [];
										$scope.initDepositObj();
										$scope.patientDetailsList = [];
										$scope.patientDetails = {};
										$scope.dueAmount = parseFloat(0).toFixed(2);
										$rootScope.stopLoader();
										var data =  {
								                 "appName": "HIS",
								                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								                 "reportDesignFilePath": "DepositSlip.rpt",
								                 "reportFormat": "PDF",
								                 "exportFileName": "DepositSlip",
								                 "reportParams": [
								                   {
								                  "paramName": "@deposit_master_id",
								                  "paramType": "Integer",
								                  "paramValue": response.data.object
								                }
								                 ]
								         };
										reportService.generate(REPORT_API_URI1,data);
										
									}
									else if (response.data.status == "error")
										{
											$rootScope.stopLoader();
										}
								});
							}
							
							$scope.getFormattedDate = function(date,isTime){
								var tempDate = new Date(date);
								var date = tempDate.getDate().toString().length == 1 ? "0"+tempDate.getDate().toString() : tempDate.getDate().toString();
								var month =  (tempDate.getMonth() + 1).toString().length == 1 ? "0"+(tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
								var year = tempDate.getFullYear();
								if(isTime)
									return date + "-" + month + "-" + year + " "+tempDate.getHours()+":"+tempDate.getMinutes()+":"+getSeconds();
								else
									return date + "-" + month + "-" + year;
							}
							$scope.initDeposit = function()
							{
								/**Init BedReservation Deposit*/
								$scope.deposit = {};
								$scope.initDepositObj();
								$scope.paymentDetails = {};
								$scope.initPaymentDetailsObj();
								$scope.listPaymentDetails = [];
								$scope.getBankList();
								$scope.getHospitalBankList();//ADDED LATER GANESH
								$scope.getCardTypesList();
								$scope.dueAmount = parseFloat(0).toFixed(2);	
							}
							$scope.initDeposit();
							/***************Admission Paper Report**************Chetana*********/
							$scope.rePrintAdmissionPaper=function(admObj)
							{
								
									var data =  {
								            "appName": "HIS",
								            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								            "reportDesignFilePath": "AdmissionPaper_listview.rpt",
								            "reportFormat": "PDF",
								            "exportFileName": "AdmissionPaper",
								            "reportParams": [
								              {
									             "paramName": "@admission_id",
									             "paramType": "Integer",
									             "paramValue": admObj.admissionId
								              }
								            ]
								 };
							reportService.generate(REPORT_API_URI1,data);
							
						}
							
							$scope.rePrintAdmissionPaperAddresso=function(admObj)
							{
								
									var data =  {
								            "appName": "HIS",
								            "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
								            "reportDesignFilePath": "AddressGraph.rpt",
								            "reportFormat": "PDF",
								            "exportFileName": "AdmissionPaper",
								            "reportParams": [
								              {
									             "paramName": "admission_id",
									             "paramType": "Integer",
									             "paramValue": admObj.admissionId
								              }
								            ]
								 };
							reportService.generate(REPORT_API_URI1,data);
							
						}

							/*************Admission Paper Report END*************************/
							
  /**========================DOCUMENT UPLOAD =========================================*/
							
							
							
							
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
							
							
							 $scope.getPatientDocumentList = function()
						        {
						         $rootScope.startLoader();
						         var data =
						           {
						           "patientId":$scope.itemSelecteds.patientId,
						           "unitId":$scope.unitId,
						           "organisationId":$scope.organizationId,
						           "userId":$scope.userId,
						           "visitTypeId":$scope.itemSelecteds.visitTypeId,
						           "admissionId":$scope.itemSelecteds.admissionId,
						          }
						         var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
						         BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						           $scope.patientDocumentList = [];
						           $scope.patientDocumentListTemp = [];
						           $scope.patientCorporateDocumentList = [];
						           $scope.patientInsuranceDocumentList = [];
						           if (response.data.status == "success") 
						           {
						            $scope.patientDocumentListTemp = response.data.listObject;
						            
						            angular.forEach($scope.patientDocumentListTemp, function(value, key) 
											{  
						            			if(value.documentTypeId==19){				            				
						            			
							            			var data={
							            					documentTypeId:value.documentTypeId,
							            					documentName:value.documentName,
							            					documentPath:value.documentPath,
							            					createdDate:value.createdDate,
							            					bucketFlag:value.bucketFlag,
							            					patientId:$scope.itemSelecteds.patientId,
							            					videolink:value.videolink,
							            					encounterDocId : value.encounterDocId !== undefined && value.encounterDocId !== null ? value.encounterDocId : 0,
							            			}
							            			$scope.patientCorporateDocumentList.push(data);
						            			}
						            			if(value.documentTypeId==30){				            				
							            			
							            			var data={
							            					documentTypeId:value.documentTypeId,
							            					documentName:value.documentName,
							            					documentPath:value.documentPath,
							            					createdDate:value.createdDate,
							            					bucketFlag:value.bucketFlag,
							            					patientId:$scope.itemSelecteds.patientId,
							            					videolink:value.videolink,
							            					encounterDocId : value.encounterDocId !== undefined && value.encounterDocId !== null ? value.encounterDocId : 0,
							            			}
							            			$scope.patientInsuranceDocumentList.push(data);
						            			}
						            			if(value.documentTypeId !=30 && value.documentTypeId!=19){				            				
							            			
							            			var data={
							            					documentTypeId:value.documentTypeId,
							            					documentName:value.documentName,
							            					documentPath:value.documentPath,
							            					createdDate:value.createdDate,
							            					bucketFlag:value.bucketFlag,
							            					patientId:$scope.itemSelecteds.patientId,
							            					videolink:value.videolink,
							            					encounterDocId : value.encounterDocId !== undefined && value.encounterDocId !== null ? value.encounterDocId : 0,
							            			}
							            			$scope.patientDocumentList.push(data);
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
							
							$scope.encounterDetailsList = [];
							$scope.encounterDetails = {
								  encounterDocId : "",
								  encounterId : "",
								  admissionId:"",
								  visitTypeId:2,
								  patientId : "",
								  documentPath : "",
								  documentTypeId : "",
								  status : 'A',
								  createdDate : "",
								  createdBy : $scope.userId,
								  updatedDate : "",
								  updatedBy : $scope.userId,
								  unitId : $scope.unitId,
								  organisationId : $scope.organizationId,
								  file : {}
							}

							$scope.showDocUPloadPoup   = function()
                                {
                                     $('#fileId').val(undefined);
                                    $scope.encounterDetailsList=[];
                                    $scope.encounterDetails.patientId = $scope.admissionRequestOrAdmissionObj.patientId;
                                    $scope.encounterDetailsList.push($scope.encounterDetails);
                                    $scope.getPatientListDocument();
                             }
							
							$scope.openDocument = function()
							{
								
								$scope.encounterDetails = {
										  encounterDocId : "",
										  encounterId : "",
										  admissionId:"",
										  visitTypeId:2,
										  patientId : "",
										  documentPath : "",
										  documentTypeId : "",
										  status : 'A',
										  createdDate : "",
										  createdBy : $scope.userId,
										  updatedDate : "",
										  updatedBy : $scope.userId,
										  unitId : $scope.unitId,
										  organisationId : $scope.organizationId,
										  file : {}
										}
								if (Object.keys($scope.itemSelecteds).length === 0) {
									growl
											.warning(
													'Please select atleast one patient...!!',
													{
														title : 'Warning!'
													});
									$scope.encounterDetailsList =[];
								}
								else
								{
									$scope.encounterDetailsList =[];
									if($scope.itemSelecteds.patientId > 0)
									{
									var age = $rootScope
									.getAge($scope.itemSelecteds.dob);
									$scope.patientFullName = $scope.itemSelecteds.patientFullName
									+ " "
									+age
									+ "/"
									+ $scope.itemSelecteds.genderCode;
									}
								else
									{
									$scope.patientFullName = $scope.itemSelecteds.patientFullName
									+ " "
									+ $scope.itemSelecteds.dob
									+ "/"
									+ $scope.itemSelecteds.genderCode;
									}
									
								
								$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
								$scope.genderCode =$scope.itemSelecteds.genderCode;
								$scope.doctorFullName =$scope.itemSelecteds.doctorFullName;
								$scope.bedNumber = $scope.itemSelecteds.bedNumber;
								$scope.dateOfAdmission = $scope.itemSelecteds.datepicker;
								$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
								$scope.pddActual = $scope.patientDischargeDate;
								$scope.doaActual = $scope.dateOfAdmission;
								$scope.admissionNumber =$scope.itemSelecteds.admissionNumber;
								$scope.department =$scope.itemSelecteds.specialityName;
								$scope.specialityName =$scope.itemSelecteds.specialityName;
									
									$scope.encounterDetailsList =[];
									var encounterDetails = angular.copy($scope.encounterDetails);
									encounterDetails.admissionId = $scope.itemSelecteds.admissionId;
									encounterDetails.patientId = $scope.itemSelecteds.patientId;	
									$scope.encounterDetailsList.push(encounterDetails);
									$('#UploadDocumentsAdmitted').modal('show');
									$scope.getPatientDocumentList();
								}
							}
							
							
							$scope.addImageDetails = function()
							{ 
								var encounterDetails = angular.copy($scope.encounterDetails);
								encounterDetails.admissionId = $scope.itemSelecteds.admissionId;
								encounterDetails.patientId = $scope.itemSelecteds.patientId;
								$scope.encounterDetailsList.push(encounterDetails);
							};

						  $scope.removeImageDetails = function(deleteIndex)
							{ 
							   $scope.encounterDetailsList.splice(deleteIndex, 1);
						    }		
							
							
							$scope.uploadEncounterDoc = function(Obj)
							{
								$('#commonUploadDocuments').modal('hide');
								 var fd = new FormData();
							        fd.append('encounterId', 0);
							        fd.append('visitTypeId', Obj.visitTypeId);
							        fd.append('admissionId', Obj.admissionId);
							        fd.append('patientId',Obj.patientId );
							        fd.append('documentTypeId', Obj.documentTypeId);
							        fd.append('createdBy',Obj.createdBy );
							        fd.append('updatedBy', Obj.updatedBy);
							        fd.append('unitId', Obj.unitId);
							        fd.append('grnId',0);
									fd.append('poId',0);
							        fd.append('organisationId', Obj.organisationId);
							        fd.append('file',Obj.file );
							        fd.append('expenseId', 0);
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
							 $scope.uploadDoc = function()
							    {
								 if ($scope.uploading) {
										return true;
									}
								 $rootScope.startLoader();
								 $scope.uploading = true;
								 $scope.finalDocUploadList=[];
								  angular.forEach($scope.encounterDetailsList, function(value, key) {  
									  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
											 growl.warning("Please select document type .",
																		{
																			title : 'warning!'
																		});
															   $rootScope.stopLoader();
															   $scope.uploading = false;
											return false;
										}
//									  if (value.file == undefined || value.file == "" || value.file == null ) {
									  if (angular.equals(value.file, {}) || value.file==undefined) {
												growl.warning('Please select file.',
																{
																	title : 'warning!'
																});
												 $rootScope.stopLoader();
												 $scope.uploading = false;
												return false;
											} 
										 var ext = value.file.name.split(".").pop().toLowerCase();
											if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
												growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
														{
															title : 'warning!'
														});
												$rootScope.stopLoader();
												$scope.uploading = false;
												return false;
											}
											$scope.finalDocUploadList.push(value);
								});
								  if($scope.encounterDetailsList.length==$scope.finalDocUploadList.length){
									  
								
								  $scope.count = 0;
								 angular.forEach($scope.finalDocUploadList, function(value, key) 
									{  
									 if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
										$rootScope.stopLoader();
										$scope.uploading = false;
										return false;
									}
									 if (value.file.size == undefined || value.file.size == "" || value.file.size == 0 ) {
										
											 $rootScope.stopLoader();
											 $scope.uploading = false;
											return false;
										} 
									 var ext = value.file.name.split(".").pop().toLowerCase();
										if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
											$scope.uploading = false;
											$rootScope.stopLoader();
											return false;
										}
										 
										var promise = $scope.uploadEncounterDoc(value);
										promise.then(function(response) 
												{
												  if(response.data.status == "success")
													  {
													    $scope.count++;
													    if($scope.finalDocUploadList.length==$scope.count)
														 {
														   growl.success("Document Uploaded Successfully.",
																	{
																		title : 'Success!'
																	});
														   $rootScope.stopLoader();
														   $scope.uploading = false;
														   $scope.encounterDetailsList = [];
														 }
												      }
												  else 
													{
														growl.error('Something went wrong.',
																{
																	title : 'Error!'
																});
														 $rootScope.stopLoader();
														 $scope.uploading = false;
													}
											    });
											
									});			
									$rootScope.stopLoader();
								  }
								  $scope.uploading = false;
								  $rootScope.stopLoader();
							    }
								
							 
							 
								$scope.openImage = function(index)
		                              { 
										$rootScope.startLoader();
										var imageUrl = $scope.patientDocumentList[index].documentPath;
										var href = $rootScope.currentHrefDoccument();
										$.fancybox.open({
											width: 600, // or whatever
											height: 320,
											href:href+imageUrl,
											type: 'iframe'
										});
		                         $rootScope.stopLoader();
		                        }
								$scope.openInsuranceImage = function(index)
	                              { 
									$rootScope.startLoader();
									var imageUrl = $scope.patientInsuranceDocumentList[index].documentPath;
									var href = $rootScope.currentHrefDoccument();
									$.fancybox.open({
										width: 600, // or whatever
										height: 320,
										href:href+imageUrl,
										type: 'iframe'
									});
	                         $rootScope.stopLoader();
	                        }
								$scope.openCorporateImage = function(index)
	                              { 
									$rootScope.startLoader();
									var imageUrl = $scope.patientCorporateDocumentList[index].documentPath;
									var href = $rootScope.currentHrefDoccument();
									$.fancybox.open({
										width: 600, // or whatever
										height: 320,
										href:href+imageUrl,
										type: 'iframe'
									});
	                         $rootScope.stopLoader();
	                        }
								//getting deposit matrix list
								/* $scope.getDepositMatrixList = function(patientTypeId,paymentEntitlementId,bedCategoryId)
									{
//										$('.deposit-matrix-bed-reservation').show();
										$rootScope.startLoader();
										$scope.depositMatrixList=[];
										var URI = BASE_URL + ROOT_URL + "/api/billing/unit/depositMatrixConfig";
										
										var param = 
										    {
												  patientTypeId :patientTypeId,
												  paymentEntitlementId : paymentEntitlementId,
												  bedCategoryId :bedCategoryId,
												  organsationId : $scope.organizationId,
												  unitId : $scope.unitId,
												  sergicalOrMedical : 2,
												  againstDeposit : 1//for Reservation
											};
										console.log('param',JSON.stringify(param));
//										//return false;
										 GenericService.serviceAction(METHOD_POST,URI,param).then(function(response) {
											if (response.data.status == "success"){
												$scope.depositMatrixList = response.data.listObject;
												console.log("Deposit matrix =",$scope.depositMatrixList);
												$rootScope.stopLoader();
											}
											else if(response.data.status == "error")
												{
												  $rootScope.stopLoader();
												}
										});
									}*/
								$scope.getDepositMatrixList = function(patientTypeId,paymentEntitlementId,billingBedCategoryId,admissionTypeId)
								{
//									$('.deposit-matrix-bed-reservation').show();
									$rootScope.startLoader();
									$scope.depositMatrixList=[];
									var URI = BASE_URL + ROOT_URL + "/api/billing/unit/depositMatrixConfig";
									
									var param = 
									    {
											  patientTypeId :patientTypeId,
											  paymentEntitlementId : paymentEntitlementId,
											  bedCategoryId :billingBedCategoryId,
											  organsationId : $scope.organizationId,
											  unitId : $scope.unitId,
											  sergicalOrMedical : admissionTypeId,
											  againstDeposit :2//for Admission
										};
									console.log('param',JSON.stringify(param));
//									//return false;
									 GenericService.serviceAction(METHOD_POST,URI,param).then(function(response) {
										if (response.data.status == "success"){
											$scope.depositMatrixList = response.data.listObject;
											console.log("Deposit matrix =",$scope.depositMatrixList);
											$rootScope.stopLoader();
										}
										else if(response.data.status == "error")
											{
											  $rootScope.stopLoader();
											}
									});
								}
								 $scope.searchBills = function(patientId,admissionId){
									 	$rootScope.startLoaderPopup();
									 	$scope.billListPopupMsg = "Searching...";
												if($scope.itemSelecteds.patientId > 0)
												{
													var billSearchObj ={												
														patientId:patientId,
														visitTypeId:$scope.itemSelecteds.visitTypeId,
														orgId:$scope.organizationId,
														unitId:$scope.unitId,
														admissionId:admissionId
													};
													$scope.billList=[];										
													
												var URI = BASE_URL + ROOT_URL + "/api/billing/billlist/filter";
												BillingGenericService.serviceAction(METHOD_POST,URI,billSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
													if (response.data.status == "success"){
														console.log(response.data.listObject);
														if(response.data.listObject.length > 0)
														{
															$scope.billListPopupMsg = "No records found...!!";
															$scope.billList = response.data.listObject;
															$rootScope.stopLoaderPopup();
														}else{
															$scope.billListPopupMsg = "No records found...!!";
															$rootScope.stopLoaderPopup();
														}
														$rootScope.stopLoaderPopup();
													}else{
														$rootScope.stopLoaderPopup();
													}
												});
											}										
									
								}
								
								 $scope.addVisiterPassMaster = function(index){
									 console.log(index);
									 var visitorPassObj=$scope.listVisiterPassMaster[index];
									 console.log("visitorPassObj",visitorPassObj);
									 if(visitorPassObj.passNumber=="" || visitorPassObj.visitorPassTypeId==""){
										 growl.error(
													'Please enter all fields.',
													{
														title : 'Error!'
													});
										 return;
									 }
									 if(flag){
										 return false;
									 }
								    	console.log($scope.visiterPassMaster);
//								    	if($scope.updateScope ==false)
					        	        {
								    	  var visiterPassMaster = angular.copy($scope.visiterPassMaster);
										  $scope.listVisiterPassMaster.push(visiterPassMaster);
					        	       }
								      /*else if($scope.updateScope ==true)
							        	{
								    	  var visiterPassMaster = angular.copy($scope.visiterPassMaster);
										  $scope.listVisiterPassMaster.push(visiterPassMaster);
										  $scope.listVisiterPassMaster.splice(deleteIndex, 1);
							        	}	*/
								    }
/**							===================corporate document upload Start ================   */
								 
								 $scope.encounterDetailsContractList = [];
									$scope.encounterDetailsContract = {
										  encounterDocId : "",
										  encounterId : "",
										  admissionId:"",
										  visitTypeId:2,
										  patientId : "",
										  documentPath : "",
										  documentTypeId : '19',
										  status : 'A',
										  createdDate : "",
										  createdBy : $scope.userId,
										  updatedDate : "",
										  updatedBy : $scope.userId,
										  unitId : $scope.unitId,
										  organisationId : $scope.organizationId,
										  file : {}
										}
									
								 $scope.addImageDetailsForContract = function()
									{ 
										var encounterDetails = angular.copy($scope.encounterDetailsContract);
										encounterDetails.admissionId = $scope.itemSelecteds.admissionId;
										encounterDetails.patientId = $scope.itemSelecteds.patientId;
										$scope.encounterDetailsContractList.push(encounterDetails);
									};

								  $scope.removeImageDetailsContract = function(deleteIndex)
									{ 
									   $scope.encounterDetailsContractList.splice(deleteIndex, 1);
								    }
								  $scope.openCorporateDocument = function()
									{
										
										$scope.encounterDetailsContract = {
												  encounterDocId : "",
												  encounterId : "",
												  admissionId:"",
												  visitTypeId:2,
												  patientId : "",
												  documentPath : "",
												  documentTypeId : '19',
												  status : 'A',
												  createdDate : "",
												  createdBy : $scope.userId,
												  updatedDate : "",
												  updatedBy : $scope.userId,
												  unitId : $scope.unitId,
												  organisationId : $scope.organizationId,
												  file : {}
												}
										if (Object.keys($scope.itemSelecteds).length === 0) {
											growl
													.warning(
															'Please select atleast one patient...!!',
															{
																title : 'Warning!'
															});
											$scope.encounterDetailsContractList =[];
										}
										else
										{
											if($scope.itemSelecteds.paymentEntitlementId != 3)
												{
													growl
													.warning(
															'Selected patient is not a corporate patient...!!',
															{
																title : 'Warning!'
															});
													return false;
												
												}
											else{
											$scope.getPatientSponcerDetails($scope.itemSelecteds.admissionId,$scope.itemSelecteds.paymentEntitlementId);
											$scope.encounterDetailsContractList =[];
											if($scope.itemSelecteds.patientId > 0)
											{
											var age = $rootScope
											.getAge($scope.itemSelecteds.dob);
											$scope.patientFullName = $scope.itemSelecteds.patientFullName
											+ " "
											+age
											+ "/"
											+ $scope.itemSelecteds.genderCode;
											}
										else
											{
											$scope.patientFullName = $scope.itemSelecteds.patientFullName
											+ " "
											+ $scope.itemSelecteds.dob
											+ "/"
											+ $scope.itemSelecteds.genderCode;
											}
											
										
										$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
										$scope.doctorFullName =$scope.itemSelecteds.doctorFullName;
										$scope.bedNumber = $scope.itemSelecteds.bedNumber;
										$scope.dateOfAdmission = $scope.itemSelecteds.datepicker;
										$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
										$scope.pddActual = $scope.patientDischargeDate;
										$scope.doaActual = $scope.dateOfAdmission;
										 $scope.admissionNumber = $scope.itemSelecteds.admissionNumber;
	                                        $scope.specialityName = $scope.itemSelecteds.specialityName;

											$scope.encounterDetailsContractList =[];
											var encounterDetails = angular.copy($scope.encounterDetailsContract);
											encounterDetails.admissionId = $scope.itemSelecteds.admissionId;
											encounterDetails.patientId = $scope.itemSelecteds.patientId;	
											$scope.encounterDetailsContractList.push(encounterDetails);
//											$('#commonUploadDocuments').modal('show');
											$scope.getPatientDocumentList();
										}
										}
									}
								  
								  $scope.openInsurancePopUp = function()
									{


										if (Object.keys($scope.itemSelecteds).length === 0) {
											growl
													.warning(
															'Please select atleast one patient...!!',
															{
																title : 'Warning!'
															});
										} else {

											if($scope.itemSelecteds.paymentEntitlementId != 2)
											 {
												 growl
													.warning(
															'Selected patient is not an insurance patient...!!',
															{
																title : 'Warning!'
															});
												 return false;
											 }
										 else{
											
											if($scope.itemSelecteds.patientId > 0)
												{
												var age = $rootScope
												.getAge($scope.itemSelecteds.dob);
												$scope.patientFullName = $scope.itemSelecteds.patientFullName
												+ " "
												+age
												+ "/"
												+ $scope.itemSelecteds.genderCode;
												}
											else
												{
												$scope.patientFullName = $scope.itemSelecteds.patientFullName
												+ " "
												+ $scope.itemSelecteds.dob
												+ "/"
												+ $scope.itemSelecteds.genderCode;
												}
												
											
											$scope.uhidNum = $scope.itemSelecteds.uhidnumber;
											$scope.genderCode =$scope.itemSelecteds.genderCode;
											$scope.doctorFullName =$scope.itemSelecteds.doctorFullName;
											$scope.bedNumber = $scope.itemSelecteds.bedNumber;
											$scope.dateOfAdmission = $scope.itemSelecteds.datepicker;
											$scope.patientDischargeDate = $scope.itemSelecteds.datepickerPdd;
											$scope.pddActual = $scope.patientDischargeDate;
											$scope.doaActual = $scope.dateOfAdmission;
											$scope.admissionNumber = $scope.itemSelecteds.admissionNumber;
											$scope.specialityName = $scope.itemSelecteds.specialityName;
											$scope.getPassIssuedList($scope.itemSelecteds.admissionId);
											 $scope.getDepositMatrixList($scope.itemSelecteds.patientCategoryId,$scope.itemSelecteds.paymentEntitlementId,$scope.itemSelecteds.bedCategoryId);
											 
											 
												 if($scope.insurancePopUpFlag)
												 {
												 $scope.getPatientSponcerDetails($scope.itemSelecteds.admissionId,$scope.itemSelecteds.paymentEntitlementId);
												 }

											 $scope.searchBills($scope.itemSelecteds.patientId,$scope.itemSelecteds.admissionId);
											 if($scope.assignContractPopUpFlag){
												 $scope.getPatientSponcerDetails($scope.itemSelecteds.admissionId,$scope.itemSelecteds.paymentEntitlementId);
												 $scope.corporateUploadFunctionlity($scope.itemSelecteds);
											 }
											 // insuranceDocument
											 $scope.encounterDetailsInsuranceList = [];
												$scope.encounterDetailsInsurance = {
													  encounterDocId : "",
													  encounterId : "",
													  admissionId:"",
													  visitTypeId:2,
													  patientId : "",
													  documentPath : "",
													  documentTypeId : '30',
													  status : 'A',
													  createdDate : "",
													  createdBy : $scope.userId,
													  updatedDate : "",
													  updatedBy : $scope.userId,
													  unitId : $scope.unitId,
													  organisationId : $scope.organizationId,
													  file : {}
													}
												$scope.encounterDetailsInsuranceList =[];
												var encounterDetails = angular.copy($scope.encounterDetailsInsurance);
												encounterDetails.admissionId = $scope.itemSelecteds.admissionId;
												encounterDetails.patientId = $scope.itemSelecteds.patientId;	
												$scope.encounterDetailsInsuranceList.push(encounterDetails);
//												$('#commonUploadDocuments').modal('show');
												$scope.getPatientDocumentList();
											 }
											
											
											 
										}

									
									}
								  $scope.uploadConDoc = function(Obj)
									{
										$('#assignContract').modal('hide');
										  $('#insurance').modal('hide');
										 var fd = new FormData();
									        fd.append('encounterId', 0);
									        fd.append('visitTypeId', Obj.visitTypeId);
									        fd.append('admissionId', Obj.admissionId);
									        fd.append('patientId',Obj.patientId );
									        fd.append('documentTypeId', Obj.documentTypeId);
									        fd.append('createdBy',Obj.createdBy );
									        fd.append('updatedBy', Obj.updatedBy);
									        fd.append('unitId', Obj.unitId);
									        fd.append('grnId',0);
											fd.append('poId',0);
									        fd.append('organisationId', Obj.organisationId);
									        fd.append('file',Obj.file );
									        fd.append('expenseId', 0);
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
									 $scope.uploadContractDoc = function(paymentEntitleMentId)
									    {
										 $rootScope.startLoader();
										  $scope.count = 0;
										  $scope.finalDocUploadList=[];
										  $scope.finalList=[];
										  
										  //2 for insurance document
										  // 3 for corporate document									  
										
										  
										  if(paymentEntitleMentId==2){
											
											  $scope.finalList=$scope.encounterDetailsInsuranceList;
										  }else if(paymentEntitleMentId==3){											 
											  $scope.finalList= $scope.encounterDetailsContractList;
										  }else{
											  
										  }
										  
										  angular.forEach($scope.finalList, function(value, key) {  
											  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
													 growl.warning("Please select document type .",
																				{
																					title : 'warning!'
																				});
																	   $rootScope.stopLoader();
													return false;
												}
											  if (angular.equals(value.file, {}) || value.file==undefined) {
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
													$scope.finalDocUploadList.push(value);
										});
										  
										  if($scope.finalList.length==$scope.finalDocUploadList.length){
											
										 angular.forEach($scope.finalDocUploadList, function(value, key) 
											{  
												var promise = $scope.uploadConDoc(value);
												promise.then(function(response) 
														{
														  if(response.data.status == "success")
															  {
															    $scope.count++;
															    if($scope.finalList.length==$scope.count)
																 {
																   growl.success("Document Uploaded Successfully.",
																			{
																				title : 'Success!'
																			});
																   $scope.getPatientDocumentList();
																   $rootScope.stopLoader();
																   $scope.encounterDetailsContractList = [];
																   $scope.finalList = [];
																   $scope.encounterDetailsInsuranceList=[];
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
										  }
										
									    }

										$scope.openCorporateImage = function(index)
				                              { 
												$rootScope.startLoader();
												var imageUrl = $scope.patientCorporateDocumentList[index].documentPath;
												var href = $rootScope.currentHrefDoccument();
												$.fancybox.open({
													width: 600, // or whatever
													height: 320,
													href:href+imageUrl,
													type: 'iframe'
												});
				                         $rootScope.stopLoader();
				                        }
								 $scope.corporateUploadFunctionlity=function(){
								
								 
								 }
								
								 /**===================corporate document upload END ================   */
	   /**========================DOCUMENT UPLOAD =========================================*/		

									$scope.printBarCodeBulk = function()
									{
										var printContents = $window.document.getElementById('admittedBarCodeBulk').innerHTML;
								        var popupWin = $window.open('', '_blank');
								        popupWin.document.open();
								        popupWin.document.write('<html><head><link rel="stylesheet" type="text/css" href="style.css" /></head><body onload="window.print()">' + printContents + '</body></html>');
								        popupWin.document.close();
									}
								 
								 //BulkBarcode Pring
								 $scope.selectedBarCodeList = [];
								 $scope.checkAll = function()
								 {
									 $scope.selectedBarCodeList = [];
									 if ($scope.selectedAll) {
											$scope.selectedAll = true;
										} else {
											$scope.selectedAll = false;
										}
										angular
												.forEach(
														$scope.listOfAdmittedPatientList,
														function(obj) {
																   obj.isBarCodeSelected=$scope.selectedAll;;
														});
									 
								
									  
								 };
								 
								 $scope.printBulkBarcode  = function()
								 {
									 
									 $scope.barcodeList = [];
									 var count = 0;
										angular.forEach($scope.listOfAdmittedPatientList, function(value, key)
												{
											if(value.isBarCodeSelected==true)
											{
												count++;
											}
											
								    });
									
										
										if(count==0)
											{
											growl
											.error(
													'Please select atleast one Barcode To print...!!',
													{
														title : 'Error!'
													});
											}
										else {	
											
											angular.forEach($scope.listOfAdmittedPatientList, function(value, key)
									{
												
											
												if(value.isBarCodeSelected==true)
													{
													if(value.patientId > 0)
													{
													var age = $rootScope.getAge(value.dob);
													$scope.patientFullName = value.patientFullName
													+ " "
													+age
													+ "/"
													+ value.genderCode;
													}
												else
													{
													$scope.patientFullName = value.patientFullName
													+ " "
													+ value.dob
													+ "/"
													+ value.genderCode;
													}
													
													$scope.barcode = {
													        type: "code128",
													        code: value.uhidnumber,
													        hospital: $scope.unitName,
													        itemCode: value.admissionId.toString(),
													        batchCode: "123456",
													        patientFullName : $scope.patientFullName,
													        bedNumber:value.bedNumber,
													        wardName:value.wardName
													      }		
													
													 $scope.barcodeList.push($scope.barcode);
													}
											
									
										});
											$('#barCodeAdmittedBulk').modal('show');
								   }
								 }
								 
								 
								 /* *************************************Mpesa Integration start****************************************** */
									
									
									
								 /* *************************************Mpesa Integration start****************************************** */
									$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
									$scope.validateMpesaDetails = function(mpesaObject){
										console.log("MPesa Object:",mpesaObject);
										$rootScope.startLoaderPopup();
										$scope.skipMpesaValidation = true;
										var data ={
												organizationId :$scope.organizationId,
												unitId : $scope.unitId,
												createdBy : $scope.userId,
												createdDate : moment(new Date()).format("DD-MM-YYYY"),
												updatedDate :  moment(new Date()).format("DD-MM-YYYY"),
												updatedBy:$scope.userId,
												mpesaTxcode:$scope.mpesaObject.transactionId,
												uhid: $scope.selectedUhid,
												mpesaSendermobile:$scope.mpesaObject.mobileNo,
												mpesaAmount:$scope.mpesaObject.mpesaAmount
										};
									
										if($scope.mpesaObject.transactionId!=null && $scope.mpesaObject.mobileNo!=null){
										var URI9 = BASE_URL + ROOT_URL + "/Billing/validateAmountByNumber";
										BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											
											if (response.data.status == "success")
												{
												
													$scope.mpesaObject.mpesaAmount = response.data.object.mpesaAmount;
													$scope.mpesaObject.resTransactionId = response.data.object.mpesaTxcode;
													 $scope.addPaymentDetails();
													$rootScope.stopLoaderPopup();
												}
											else if(response.data.status == "error"){
												
												growl.error(response.data.message,{
													title : ERROR_MSG
												});
												$scope.mpesaObject.mpesaAmount=null;
												$rootScope.stopLoaderPopup();
												$scope.skipMpesaValidation = false;
											}
											$rootScope.stopLoaderPopup();
										});
										}else{
											growl.error("Enter both transaction id and mobile number",{
												title : ERROR_MSG
											});
											$rootScope.stopLoaderPopup();
											$scope.skipMpesaValidation = false;
										}
									}
									
									/*$scope.mobileNumber ="";
									$scope.validateMpesaAmount ="";*/
									
									$scope.mpesaPaymentRequest = function(validateMpesaAmount,mobileNumber,countryCallingCode){
										
										$scope.skipMpesaValidation = true;
										var data ={
												organizationId :$scope.organizationId,
												unitId : $scope.unitId,
												createdBy : $scope.userId,
												createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
												updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
												updatedBy:$scope.userId,
												uhidNumber: $scope.selectedUhid,
												phoneNumber:parseInt(countryCallingCode+""+mobileNumber),
												partyA:mobileNumber,
												amount:validateMpesaAmount
										};
									
										if((mobileNumber != undefined && mobileNumber != null && mobileNumber != "") ||
												(countryCallingCode != undefined && countryCallingCode != null && countryCallingCode != "")){
										$rootScope.startLoaderPopup();
										var URIVALI = BASE_URL + ROOT_URL + "/Billing/requestSTKPushPayment";
										BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											
											if (response.data.status == "success")
												{
												
												growl.success(response.data.message,{
													title : SUCCESS_MSG
												});
												$rootScope.stopLoaderPopup();
												angular.element('#paymentReqModal').modal('hide');
												}
											else if(response.data.status == "error"){
												
												growl.error(response.data.message,{
													title : ERROR_MSG
												});
												$rootScope.stopLoaderPopup();
												$scope.skipMpesaValidation = false;
											}
											
										});
										}else{
											growl.error("Please enter mobile code and mobile number.",{
												title : ERROR_MSG
											});
											$scope.skipMpesaValidation = false;
											$rootScope.stopLoaderPopup();
										}
										
									}
									
								$scope.validateSTKPushPayment = function(){
										
										var data ={
												organizationId :$scope.organizationId,
												unitId : $scope.unitId,
												uhidNumber: $scope.selectedUhid
												
										};
										$rootScope.startLoader();
										var URIVALI = BASE_URL + ROOT_URL + "/Billing/validateSTKPushPayment";
										BillingGenericService.serviceAction(METHOD_POST,URIVALI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											
											if (response.data.status == "success")
												{
												$scope.mpesaObject.mpesaAmount = response.data.object.amount;
												$scope.mpesaObject.transactionId = response.data.object.checkoutRequestId;
												$scope.mpesaObject.resTransactionId = response.data.object.checkoutRequestId;
												$scope.mpesaObject.mobileNo = response.data.object.phoneNumber;
												 $scope.addPaymentDetails();
												$scope.skipMpesaValidation = false;
												growl.success(response.data.message,{
													title : SUCCESS_MSG
												});
												$rootScope.stopLoader();
												}
											else if(response.data.status == "error"){
												
												growl.error(response.data.message != null ? response.data.message :"Please wait",{
													title : ERROR_MSG
												});
												$rootScope.stopLoader();
											}
											
										});
										
									}
									
									/* *************************************Mpesa Integration END****************************************** */
									
								 /******POS machine***********/
								$scope.getPOSMachineList=function(){
									$scope.pOSMachineList=[];
									 var data={
												   organizationId:$scope.organizationId,
								 			}
								           var URI = BASE_URL + ROOT_URL + "/api/getPOSMachineDeviceMasterList";
									 
										BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											
											$scope.otReservationList = [];
											if (response.data.status == "success")
											{
												$scope.pOSMachineList  = response.data.listObject;
											}
										});
								}
								$scope.getPOSMachineList();
							    
								$scope.selectedDevice=function(obj){
									
									if($scope.isPinPadRequired == 'Y' && $scope.registeredPinPadDevice == "POS Machine"){
									$scope.configDetail=[];
									angular.forEach($scope.pOSMachineList, function(value, key){
										if(value.deviceName == obj.deviceName.toString()){
											$scope.machineConfigDetails=value.machineConfigDetails;
											$scope.uploadLink=value.uploadLink;
										}
									});
										
									$scope.configDetail=JSON.parse($scope.machineConfigDetails);
									
									$scope.custUserId = $scope.configDetail.custUserId;
									$scope.deviceid = obj.deviceName;
									$scope.password = $scope.configDetail.password;
									}

								}
								

								$scope.objPosMachine={
										isSale:false, 
										isSMSpay : false,
										isBharatQr:false, 
										posMachineDeviceId:"",
										deviceName:"",
										machineConfigDetails:"",
										mobileNumberForPOS:""
								};
								
								$scope.getFlagSale=function(){
									if($scope.objPosMachine.isSale =="isSale"){
										$scope.objPosMachine.isSale = true;
										$scope.flagForSale=true;
										 $scope.flagForSMSpay=false;
										 $scope.flagForBharatQr=false;
										 $scope.objPosMachine.isBharatQr ="";
										 $scope.objPosMachine.isSMSpay=""
									}
									
								}
							 
							 $scope.getFlagBharatQr=function(){
								
									if($scope.objPosMachine.isBharatQr =="isBharatQr"){
										$scope.objPosMachine.isBharatQr = true;
										$scope.flagForBharatQr=true;
										 $scope.flagForSale=false;
										 $scope.flagForSMSpay=false;
										 $scope.objPosMachine.isSale ="";
										 $scope.objPosMachine.isSMSpay="";
									}
								}
							 $scope.getFlagSMSpay=function(){
									
									if($scope.objPosMachine.isSMSpay =="isSMSpay"){
										$scope.objPosMachine.isSMSpay = true;
										$scope.flagForBharatQr=false;
										 $scope.flagForSale=false;
										 $scope.flagForSMSpay=true;
										 $scope.objPosMachine.isSale ="";
										 $scope.objPosMachine.isBharatQr ="";
									}
								}
							 
								 $scope.sendRequestForPOSMachine=function(){

										if($scope.objPosMachine.deviceName != undefined && $scope.objPosMachine.deviceName !=null && $scope.objPosMachine.deviceName != "" ){
											$rootScope.startLoaderPopup();
											if($scope.flagForSale == true){
											$scope.sale=
//											{

											/*"deviceid":($scope.objPosMachine.deviceName).toString(),
											 "custUserId":$scope.custUserId,
											 "password":$scope.password,
											 "custMobileNo":$scope.patientDetails.mobileNumber,
											 "invoice":Date.now().toString(),
											 "amount":$scope.paymentDetails.credAmount,
											 "transactionType":"Sale"
											}*/
													{
														  "userName": $scope.custUserId,
														  "password": $scope.password,
														  "customerMobileNo":$scope.patientDetails.mobileNumber,
														  "customerEmail": $scope.patientDetails.email,
														  "invoiceNo": Date.now().toString(),
														  "transactionAmount": $scope.paymentDetails.credAmount,
														  "transactionType": "1",  
														  "payerName": $scope.patientDetails.firstName,
														  "autoInitiateFlag": "N"
														}


											$scope.selectedJson=JSON.stringify($scope.sale);
											
										}
										if($scope.flagForBharatQr == true){
											$scope.bharatQr=
											{
													  "userName": $scope.custUserId,
													  "password": $scope.password,
													  "customerMobileNo":$scope.patientDetails.mobileNumber,
													  "customerEmail": $scope.patientDetails.email,
													  "invoiceNo": Date.now().toString(),
													  "transactionAmount": $scope.paymentDetails.credAmount,
													  "transactionType": "24",  
													  "payerName": $scope.patientDetails.firstName,
													  "autoInitiateFlag": "N"
													}

											$scope.selectedJson=JSON.stringify($scope.bharatQr);
										}
										if($scope.flagForSMSpay == true){
											$scope.smsPay=
											{
													  "userName": $scope.custUserId,
													  "password": $scope.password,
													  "customerMobileNo":$scope.patientDetails.mobileNumber,
													  "customerEmail": $scope.patientDetails.email,
													  "invoiceNo": Date.now().toString(),
													  "transactionAmount": $scope.paymentDetails.credAmount,
													  "transactionType": "19",  
													  "payerName": $scope.patientDetails.firstName,
													  "autoInitiateFlag": "N"
													}

											$scope.selectedJson=JSON.stringify($scope.smsPay);
										}
										var date = new Date();
										$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
										var data={
												patientId:$scope.patientDetails.patientId,
												transactionAmount:parseInt($scope.paymentDetails.credAmount),
												organizationId:$scope.organizationId,
												unitId:$scope.unitId,
												createdBy:$scope.createdBy,
												updatedBy:$scope.updatedBy,
												status:'A',
												createdDate:$scope.dt,
												updatedDate:$scope.dt,
												posMachineDeviceId:$scope.deviceid,
												selectedPaymentModeJson:$scope.selectedJson,
											}
								        var URI = BASE_URL + ROOT_URL + "/api/savePOSMachineDeviceMaster";
									 
										BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {

											if(response.data.status != "success"){
												growl.error(response.data.message);
											}else{
												growl.success(response.data.message);
											}

											$rootScope.stopLoaderPopup();
										});
								 }else{
									 growl.warning("Please select device.");
									 $rootScope.stopLoader();
								 }
								 }
							
									$scope.checkValidatePinPad = function()
									{
										if(($scope.registeredPinPadDevice != undefined || $scope.registeredPinPadDevice != null || $scope.registeredPinPadDevice != "") 
												&& $scope.registeredPinPadDevice == "POS Machine"){
											$scope.getResposeList=[];
											$scope.getGeneratedResponse=[];
											$rootScope.startLoaderPopup();
											var data = {
													patientId : $scope.patientDetails.patientId,
													organizationId : $scope.organizationId,
													unitId : $scope.unitId,
													transactionAmount:$scope.paymentDetails.credAmount,
											}
											
											
											var URI = BASE_URL + ROOT_URL + "/api/updatePOSMachineDetails";
											BillingGenericService.serviceAction(METHOD_POST,URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												
												if (response.data.status == "success")
													{
														console.log("saved Card Details");
														if(response.data.object != null && response.data.object.listObject.length > 0){
															$scope.getResposeList=response.data.object.listObject;
															$scope.getGeneratedResponse = JSON.parse($scope.getResposeList[0].generatedReponse);
															$scope.paymentDetails.credCardHolderName=$scope.getGeneratedResponse.cardHolderName;
//															$scope.paymentDetails.credCardTypeId=$scope.getGeneratedResponse.cardType;
															$scope.paymentDetails.credAmount=$scope.getGeneratedResponse.transactionAmount;
															if($scope.flagForBharatQr == false){
																$scope.paymentDetails.credCardNumber=$scope.getGeneratedResponse.transactionCardNumber.slice($scope.getGeneratedResponse.transactionCardNumber.length - 4);
															}else{
																$scope.paymentDetails.credCardNumber=$scope.getGeneratedResponse.transactionCardNumber;
															}
															$scope.paymentDetails.transactionNo=$scope.getGeneratedResponse.transactionID;
															$scope.paymentDetails.credExpiryDate=moment($scope.getGeneratedResponse.expiryDate).toDate();
															$scope.paymentDetails.requestedUniqueId=$scope.getGeneratedResponse.billNumber/*$scope.getGeneratedResponse.invoiceNumber*/;
															$scope.paymentDetails.creditDebitCardType=$scope.getGeneratedResponse.creditDebitCardType;
															$scope.paymentDetails.transactionTypeName=$scope.getGeneratedResponse.transactionTypeName;
															$scope.paymentDetails.transactionDetails=($scope.paymentDetails.creditDebitCardType!= null && $scope.paymentDetails.creditDebitCardType!= "" ? $scope.paymentDetails.creditDebitCardType + ' || ':"")
															+ ($scope.paymentDetails.credCardNumber!= null && $scope.paymentDetails.credCardNumber!= "" ? $scope.paymentDetails.credCardNumber + ' || ':"")+ ($scope.paymentDetails.transactionNo!= null && $scope.paymentDetails.transactionNo!= "" ? $scope.paymentDetails.transactionNo + ' || ':"") + ($scope.paymentDetails.transactionTypeName!= null && $scope.paymentDetails.transactionTypeName!= "" ? $scope.paymentDetails.transactionTypeName:"")
															
															$scope.getGeneratedResponse.cardType=angular.lowercase($scope.getGeneratedResponse.cardType);
															angular.forEach($scope.cardTypeList,function(value, key){
																value.cardTypeName1=angular.lowercase(value.cardTypeName);
																if(value.cardTypeName1 == $scope.getGeneratedResponse.cardType){
																	$scope.paymentDetails.credCardTypeId=value.cardTypeId;
																}
															});
															$scope.addPaymentDetails();
															$rootScope.stopLoaderPopup();
														}
														else {
															
															growl.error("Please wait user did not paid amount.",{
																title : ERROR_MSG
															});
															$rootScope.stopLoaderPopup();
														}

													}
												else if(response.data.status == "error"){
													
													growl.error("Please wait user did not paid amount.",{
														title : ERROR_MSG
													});
													$rootScope.stopLoader();
												}
												
											});
										}else{
										//alert($location.host());
										//var IAddress = $location.host();
										//alert("IP ADRRESS");
										//alert(IAddress);
										var data ={
												organizationId :$scope.organizationId,
												unitId : $scope.unitId,
												createdBy : $scope.userId,
												createdDate : moment(new Date()).format("DD-MM-YYYY HH:mm:ss"),
												tillId:'50002',
												patientId: $scope.patientDetails.patientId,
												//ip : IAddress
										};
										$scope.pinpadAmounFlag = true;
										$rootScope.startLoaderPopup();
										var URI9 = BASE_URL + ROOT_URL + "/Billing/savePinPanDetaild";
										BillingGenericService.serviceAction(METHOD_POST,URI9, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
											
											if (response.data.status == "success")
												{
												
													$scope.uniqueNumber = response.data.object.uniqueNumber;
													$scope.pinpadTransectionId = response.data.object.pinpadTransectionId;
													$scope.ipAddress = response.data.object.ipAddress;
													var dataForPinPad = {
														    amount: parseFloat($scope.dueAmount) * 100,
															//"amount":100,
															//tellerId: "50002",
															tranKey: $scope.uniqueNumber
													}
													
												/*	
													$http.post('http://localhost:9991/api/sale',dataForPinPad)
														    .then(function(response) {
														            alert();
														    });*/
													
													/* return $http({
											                headers: {
											                    "Accept": "application/json;odata=verbose",
											                    "content-type": "application/json;odata=verbose"
											                },
											                method: "POST",
											                url: "http://localhost:9991/api/sale",
											                data: {
											                    __metadata: {
											                        "type": "SP.Data.SongsListItem"
											                    },
											                    "amount": "100",
											                    "tellerId": "50002",
											                    "tranKey": "201903282"
											                }


											            }).then(saveContact)
											            .catch(function(message) {
											                console.log("addQuestion() error:" + message);
											            });
													
													 function saveContact(data, status, headers, config) {
												            alert("Item added successfully");
												        }*/
													
													/*var URI = "http://localhost:9991/api/sale";
													var headerObjDia = {
															'Content-Type':'application/json',
											     };
												 var data={};
								               
								             GenericServiceParamHeader
								                 .serviceAction(METHOD_POST, URI,
								                		 dataForPinPad,
								                         NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
								                 .then(
								                         function(response) {
								                             console.log(response);
								                             alert();
								                         });*/
								             
								             
//													/$scope.ipAddress+
													var urlprotocol = $location.protocol();
													//var finalUrl = "http://"+$scope.ipAddress+":9991/api/sale";
													var finalUrl = "http://127.0.0.1:9991/api/sale";
													var URI = finalUrl;
													BillingGenericService.serviceAction(METHOD_POST,URI,dataForPinPad,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
														/*if (response.data.status == "success"){
															console.log("ree",response);
															console.log("reew",response.data);
															console.log("reer",response.data.rspCode);*/
														if (response !=  undefined){	
															if(response.data.rspCode == '100' || response.data.rspCode == '003' || response.data.rspCode == '000')
																{
																	$scope.paymentDetails.credCardHolderName = response.data.cardholderName;
																	$scope.paymentDetails.credAmount = parseFloat(response.data.amount)/100;
																	$scope.schema = response.data.scheme;
																	var pan = response.data.pan;
																	var lastFour = pan.substr(pan.length - 4);
																	$scope.paymentDetails.credCardNumber = lastFour;
																	$scope.paymentDetails.transactionNo = response.data.retRefNr;
																	
																	
																	var cardTypeId = $.grep($scope.cardTypeList, function (card) {
													    	            return card.pinpadScheme == $scope.schema;
													    	        })[0].cardTypeId;
																	$scope.paymentDetails.credCardTypeId = cardTypeId;
																	
																	$scope.resString = JSON.stringify(response);
																	console.log("resString",$scope.resString); 
																	
																	var dataUPDATE = {
																			response : $scope.resString,
																			pinpadTransectionId : $scope.pinpadTransectionId,
																			patientId : $scope.patientDetails.patientId,
																			organizationId : $scope.organizationId,
																			unitId : $scope.unitId,
																			createdDate : moment(new Date()).format("YYYY-MM-DD"),
																			createdBy: $scope.userId,
																			status:'A'
																	}
																	
																	
																	var URIUPDATE = BASE_URL + ROOT_URL + "/Billing/updatePinpadDetails";
																	BillingGenericService.serviceAction(METHOD_POST,URIUPDATE, dataUPDATE,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
																		
																		if (response.data.status == "success")
																			{
																				console.log("saved Card Details");
																			    $scope.addPaymentDetails();
																			}
																		else if(response.data.status == "error"){
																			
																			growl.error("Somting wrong with save data of card..",{
																				title : ERROR_MSG
																			});
																			$rootScope.stopLoaderPopup();
																		}
																		
																	});
																	
																	growl.success(response.data.message,{
																		title : "SUCCESS!"
																	});
																	$rootScope.stopLoaderPopup();
																
																}
															else
																{
																 $scope.pinpadAmounFlag = false;
																	growl.error(response.data.message,{
																		title : ERROR_MSG
																	});
																	$rootScope.stopLoaderPopup();
																	return false;
																	
																}
														}else{
															 
															 $scope.pinpadAmounFlag = false;
															 growl.warning("Pin Pad Not Configure",{
																	title : "Warning"
																});
															 	$rootScope.stopLoaderPopup();
																return false;
														 }
													});
												}
											else {
												 $scope.pinpadAmounFlag = false;
												growl.error("Someting went wrong..!!",{
													title : ERROR_MSG
												});
												$rootScope.stopLoaderPopup();
											}
										});
									}
									}
									
									
									/***********************************************Loylty card Tab START *****************************************/
									
									
									$scope.isBillList = 0;
									
									
									$scope.checkAmt = function(amt)
									{
										loyaltiCardCallFactory.checkAmt(amt,$scope);
										
									}
									
									$scope.getLoyaltyCardByNo = function(cardNo)
									{
										loyaltiCardCallFactory.getLoyaltyCardByNo(cardNo,$scope);
										
									}
									
									$scope.sendOTP = function(flag)
									{
										loyaltiCardCallFactory.sendOTP(flag,$scope);
									}
									
									$scope.reSendOTP = function(flag)
									{
										loyaltiCardCallFactory.sendOTP(flag,$scope);
										//$scope.resend = (flag=='N')?true:false;
										//$scope.send =false;
									}
									
									$scope.clearLoylty = function(){
										
										$scope.send = false;
										$scope.validatTextFieldFlag = true;
										$scope.paymentDetails.otp ="";
										//$scope.calculateTotalDueRefAmount();
										$scope.calculateDueAmount();
										$scope.calculateTotalAmount();
										loyaltiCardCallFactory.getLoyaltyCardList($scope.selectedPatientId,$scope);
									}
									
									
									$scope.validateOtp = function()
									{
										
										if($scope.paymentDetails.otp==0||$scope.paymentDetails.otp==''||$scope.paymentDetails.otp==undefined||
												$scope.paymentDetails.otp==null)
											{
											growl.error("Please enter valid OTP",
													{
														title : 'Error!'
													});
											    return false;
											 }
										else{
										$rootScope.startLoader();
											var data = {
													  "patientId" : '',
													  "organisationId" : $scope.organizationId,
													  "unitId" : $scope.unitId,
													  "status" : "A",
													  "isDeleted" : "N",
													  "isUserDefined":"Y",
													  "otp": $scope.paymentDetails.otp,
													  "cardNo":$scope.paymentDetails.loyaltyCardNo,
													  "patientLoyaltyCardMpprId":$scope.loyaltyCardObj.patientLoyaltyCardMpprId,
													  "consumeAmount":$scope.paymentDetails.loyaltyCardAmt,
													  "authKey":$scope.loyaltyCardObj.authKey
											}
											var URI = BASE_URL + ROOT_URL + "/api/loyaltyCard/validateOTP";
											BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
												if (response.data.status == "success")
												{
														$rootScope.stopLoader();
														growl.success(response.data.message,
																{
																	title : 'Success!'
																});
															$scope.loyaltyCardObj.authKey = 0;
															$scope.send=false;
															$scope.resend=false;
															$scope.validatTextFieldFlag = true;
															var paymentObj = angular.copy($scope.paymentDetails);
															$scope.addPaymentDetails();
															$scope.paymentDetails.loyaltyCardNo = $scope.loyaltyCardObj.cardNo;
															$scope.paymentDetails.cardBalAmount = paymentObj.cardBalAmount - parseFloat(paymentObj.loyaltyCardAmt);
														
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
									}
									
									/*$scope.getClassForColor = function(info)
									{
										   if(info.isCriticalOrder==1){
											   return 'td-bg-red';
										   }else if (info.isVip=='Y') 
										   {
											    return 'td-bg-blue';
										   }else if(info.encounterTypeId==6){
											   return 'td-bg-green';
										   }
									}*/
									
									/***********************************************Loylty card Tab END *****************************************/
									/***********************************************E-Transfer Tab START *****************************************/
									$scope.eTransferObj={
											transBankId:"",
											transferAmount:"",
											transTransactionDate:"",
											transactionNo:"",
											transactionobj:{
													id:"",
													name:""
											}	
									}
									
									
									$scope.transactionTypesList=[];
									 $scope.getTransactionTypes = function(){
										var URI = BASE_URL + ROOT_URL + API + BILLING + "/transactiontypes/"+$scope.organizationId+"/"+$scope.unitId;
										BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
											if(response.data.status == "success"){
												$scope.transactionTypesList = response.data.listObject;
											}
											
											console.log("transactionTypesList",$scope.transactionTypesList);
										});
									}
									 
									 $scope.getTransactionTypes();
									 
									 /* **********************************************E-Transfer Tab END **************************************** */
									
									// For Paginations Start for nursing patient acceptance
										$scope.setNoOfRecords = function() {
											$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
										};
										$scope.initAdmittedPatientList = function(orgId,unitId,offset, noOfRecordsPerPage) {
											
											$scope.search ={
													floorId:0,
													wardId:0,
													admissionTypeId:'',
													doctorName:'',
													doctorId:'',
													admissionDate:undefined,
													entitlementId:''
													
											}
											offset = offset != null ? offset : 0;
											noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
											
											var URI = BASE_URL + ROOT_URL +"/adt/getAdmittedPatientListPagination";
											var URI1 = BASE_URL + ROOT_URL+ "/adt/getAdmittedPatientListPaginationCount";
											
											var commonObj = {
													unitId : $scope.unitId,
													organizationId : $scope.organizationId,
													offset : offset,
													noOfRecordsPerPage : noOfRecordsPerPage
													
													};
																							
												$rootScope.startLoader();
												GenericService.serviceAction("POST", URI, commonObj)
												.then(
														function(response) {
															$scope.newAdmittedPatientList = [];
//		
															if (response.data.status == "success") {
																$scope.listOfAdmittedPatientList = response.data.listObject;
																
																angular.forEach($scope.listOfAdmittedPatientList,function(value,key){
																	 var datePartsad = value.datepicker.split('/');
									                                    var dateObjectad = new Date(datePartsad[2], datePartsad[1] - 1, datePartsad[0]); 
									                                    var longValuead = dateObjectad.getTime();
									                                    value.datepicker = longValuead;
									                                    var datePartspdd = value.datepickerPdd.split('/');
									                                    var dateObjectpdd = new Date(datePartspdd[2], datePartspdd[1] - 1, datePartspdd[0]); 
									                                    var longValuepdd = dateObjectpdd.getTime();
									                                    value.datepickerPdd = longValuepdd;
																	    if(value.mealPrefId != undefined && value.mealPrefId != ''){
																		   value.mealPreferenceId = value.mealPrefId;
																		   value.setdietTypeId = value.dietTypeId;		
																	    }
																 });
																
																$rootScope.stopLoader();
															}else{
//																$rootScope.stoptLoader();
															}
														});

											GenericService.serviceAction("POST", URI1, commonObj).then(
															function(response) {
																if (response.data.status == "success") {
																	console.log(response.data);
																	$scope.commonListCount = response.data.object;
																	$scope.totalCountPatient = response.data.object;
																	$scope.setPage(1,false);
																	$rootScope.stopLoader();

																}
															});
										}

										
										$scope.consentView=function(obj,index){

											obj.patientId = $scope.selectedPatientId;
											obj.admissionId  =  $scope.itemSelecteds.admissionId;
											obj.status='A'
									        	$rootScope.startLoader();
								        	    var URI = BASE_URL + ROOT_URL + "/api/opd/appendCensentData";
								        	    GenericService.serviceAction(METHOD_POST,URI,obj).then(function(response) {
									    			if (response.data.status == "success")
									    			{
									    				/*$scope.templateList = [];
									    			 	$scope.templateList = response.data.listObject;
									    			 	$('#concentTemplate').modal('show');
														$rootScope.stopLoader();*/
									    				$rootScope.stopLoader();
									    				obj.templateDesc = response.data.object.templateDesc;
									    				$scope.viewTemplate(obj,index)
													}
									    			else if(response.data.status == "error")
								    				{
									    				$rootScope.stopLoader();
								    				}
													
									    		});
								        	
										}
										
										
										$scope.getAdmittedPatientList = function(orgId,unitId,offset, noOfRecordsPerPage) {
											console.log("offset", offset);
											var data = "";
											offset = offset != null ? offset : 0;
											noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage: 10;
											var URI = BASE_URL + ROOT_URL + "/adt/getAdmittedPatientListPagination";
											
											var commonObj = {
													unitId : $scope.unitId,
													organizationId : $scope.organizationId,
													offset : offset,
													noOfRecordsPerPage : noOfRecordsPerPage
													};										
												$rootScope.startLoader();
												GenericService.serviceAction("POST", URI, commonObj)
												.then(
														function(response) {
															$scope.listOfAdmittedPatientList = [];

															if (response.data.status == "success") {
																$scope.listOfAdmittedPatientList = response.data.listObject;
																angular.forEach($scope.listOfAdmittedPatientList,function(value,key){
																	 if(value.mealPrefId != undefined && value.mealPrefId != ''){
																		 value.mealPreferenceId = value.mealPrefId;
																		 value.setdietTypeId = value.dietTypeId;
																	 }
																 });
																
																$rootScope.stopLoader();
															}else{
																$rootScope.stoptLoader();
															}
														});
										}

										$scope.pager = {};
										$scope.page;

										$scope.setPage = function(page, flag) {
											if (page < 1 || page > $scope.pager.totalPages) {
												return;
											}
											$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
											if (flag) {
												$scope.getAdmittedPatientList($scope.organizationId,$scope.unitId,$scope.pager.startIndex,$scope.pager.pageSize);
											}
										}

										$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);
									    	// For Paginations End
										
			/**================================package order========================================================= */
										$scope.searchPackages = function(keyword) {

											$scope.packageSearchObj = {
												organisationId : $scope.organizationId,
												unitId : $scope.unitId,
												visitTypeId : $scope.itemSelecteds.visitTypeId,
												bedBillingCategoryId : $scope.itemSelecteds.billingBedCategoryId,
												paymentEntitlementId : $scope.itemSelecteds.paymentEntitlementId,
												serviceName : keyword,
												patientTypeId : $scope.itemSelecteds.patientCategoryId,
											}

											var URI = BASE_URL + ROOT_URL
													+ "/api/packages/bill/services/search";
											return BillingGenericService
													.serviceAction("POST", URI,
															$scope.packageSearchObj)
													.then(
															function(response) {
																if (response.data.status == "success") {
																	return response.data.listObject;
																}
															});
										  }	
										
										
									$scope.selectPackageService = function($item,
												$model, $label) 
										{
											$scope.serviceMasterId = $item.serviceMasterId;
											$scope.specialityId = $item.specialityId;
											$scope.subSpecialityId = $item.subSpecialityId;
											$scope.apportionedPrice = $item.basePrice;
											$scope.packageId = $item.packageMasterId;
											$scope.packageTypeId = $item.packageTypeId;
											$scope.packagePayable = $item.basePrice;
											$scope.taxPercentage = $item.taxPercentage;
										}
										
										$scope.getListPackageOrder = function() {

											var URI = BASE_URL
													+ ROOT_URL
													+ "/api/packages/getPackageListForIPDPackageOrder/"
													+ $scope.itemSelecteds.admissionId
													+ "/" + $scope.organizationId + "/"+ $scope.unitId+"/"+$scope.visitTypeId;
											GenericService
													.serviceAction("GET", URI)
													.then(
															function(response) 
															{
																$rootScope.stopLoader();
																$scope.listPackageOrder = [];
																$scope.listPackageOrder = response.data.listObject;
															});
										}	
										$scope.savepackage = function() {

											var packageId = $scope.listPackageOrder.find(packageObj => packageObj.packageId == $scope.packageId );
										    if(packageId!=undefined)
												{
												growl.warning("Package is already Ordered!!", {
													title : 'Warning!'
												});
												$rootScope.stopLoader();
												}
										  else{
											$rootScope.startLoader();
											var listOrderMasterDto = [];
											var finalData = {
												orgId : $scope.organizationId,
												orgUnitId : $scope.unitId,
											};
											var data = {

												currentDate : moment(new Date()).format(
														'YYYY-MM-DD HH:mm:ss'),
												createdDate : new Date().getTime(),
												updatedDate : new Date().getTime(),
												orgId : $scope.organizationId,
												orgUnitId : $scope.unitId,
												createdBy : $scope.userId,
												updatedBy : $scope.userId,
												patientId : $scope.itemSelecteds.patientId,
												admissionId : $scope.itemSelecteds.admissionId,
												defaultSelfTariffId : $scope.defaultSelfTariffId,
												paymentEntitlementTypeId : $scope.itemSelecteds.paymentEntitlementId,
												doctorId : $scope.itemSelecteds.doctorId,
												priorityId : 2,
												visitTypeId : $scope.itemSelecteds.visitTypeId,
												orderDate : new Date().getTime(),
												orderStatus : 'A',
												orderStatusId : 1,
												contractId : $scope.itemSelecteds.contractId,
												payeeId : $scope.itemSelecteds.payeeId,
												billingBedCategoryId : $scope.itemSelecteds.billingBedCategoryId,
												paymentEntitlementTypeId : $scope.itemSelecteds.paymentEntitlementId,
												patientTypeId :$scope.itemSelecteds.patientCategoryId,
												isPreOrPostBilling :$scope.isPreOrPostBilling
											}
											$scope.addedServicePackageOrdersForDisplay = [];
											
											$scope.packageogbejct = 
											{
												serviceMasterId : $scope.serviceMasterId,
												specialityId : $scope.specialityId,
												subSpecialityId : $scope.subSpecialityId,
												apportionedPrice : $scope.apportionedPrice,
												packageId : $scope.packageId,
												contractId: $scope.contractId,
												packageTypeId : $scope.packageTypeId,
												packageEitherOrGroupId : null,
												packageCapDetailsId : null,
												isCapService : null,
												packagePayable : $scope.packagePayable,
												isRefDoctorShare : $scope.orderIsRefDoctorShare,
												taxPercentage: $scope.taxPercentage
											}
											
											$scope.addedServicePackageOrdersForDisplay.push($scope.packageogbejct);
											var finalOrderServiceList = [];
											
											angular
													.forEach(
															$scope.addedServicePackageOrdersForDisplay,
															function(value, key) {

																var newArr = {};
																newArr.ordDocId =$scope.itemSelecteds.doctorId;
																newArr.serviceId = value.serviceMasterId;
																newArr.ordDocSplId = $scope.itemSelecteds.specialityId;
																newArr.serviceRenderingDeptId = value.subSpecialityId;
																newArr.apportionedPrice = value.apportionedPrice;
																newArr.isPackageService = "N";
																newArr.orderQty = 1;
																newArr.packageId = value.packageId;
																newArr.packageTypeId = value.packageTypeId;
																newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
																newArr.packageCapDetailsId = value.packageCapDetailsId;
																newArr.isCapService = value.isCapService;
																newArr.packagePayable = value.packagePayable;
																newArr.taxPer = value.taxPercentage;
																newArr.status = 'A';
																newArr.isRefDoctorShare = value.isRefDoctorShare;
																newArr.isPackage = true;
																newArr.departmentId = value.specialityId;
																newArr.subDepartmentId = value.subSpecialityId;
																newArr.serviceRendered = 0;
																newArr.isPackage = true;
																newArr.serviceRendered = 1;
																newArr.isPackageService = 'N';
																newArr.nursingServiceOrderFlag ='N';
																finalOrderServiceList.push(newArr);
															});
											data.listOrderDetailsMasterDto = finalOrderServiceList;
											listOrderMasterDto.push(data);
											finalData.listOrderMasterDto = listOrderMasterDto;
										  if ($scope.addedServicePackageOrdersForDisplay[0].packageId != undefined
													&& $scope.addedServicePackageOrdersForDisplay[0].packageId != "") {
												var URI = BASE_URL + ROOT_URL
														+ SAVEINVESTIGATION
														BillingGenericService
														.serviceAction(METHOD_POST, URI,
																finalData,
																NOTIFICATION_MSG_STATUS_TRUE)
														.then(
																function(response) {
																	if (response.data.status == "success") 
																	{
																		$scope.addedServiceOrdersForDisplay = [];
																		$scope.getListPackageOrder();
																		$scope.serviceName = "";
																		$scope.packageId = "";
																	}
																});
											} else {
												growl.warning("Please add Package!!", {
													title : 'Warning!'
												});
												$rootScope.stopLoader();
											}
										  }
										}
										
										
										$scope.getPackagesDetails = function(packageId)
										{
											
											   $scope.activePackagesListByPatient = [];
                                                   if($scope.itemSelecteds !== undefined && $scope.itemSelecteds !== null){
                                                       if($scope.itemSelecteds.patientId === undefined || $scope.itemSelecteds.patientId === null || $scope.itemSelecteds.patientId ===''){
                                                            $scope.itemSelecteds.patientId = 0;
                                                       }
                                                   }
                                                   else{
                                                        $scope.itemSelecteds.patientId = 0;
                                                   }
												var URI =BASE_URL + ROOT_URL + "/api/packages/patientId/"+$scope.itemSelecteds.patientId+"/encounterId/"+$scope.itemSelecteds.admissionId+"/visitTypeId/"+$scope.itemSelecteds.visitTypeId;
												var header = {organisationId: $scope.organizationId,unitId:$scope.unitId,packageId:packageId};
												GenericServiceParamHeader.serviceAction("GET", URI, null,NOTIFICATION_MSG_STATUS_FALSE,null,header).then(function(response){
													if(response.data.status == "success"){
														$scope.activePackagesListByPatient = response.data.listObject;
														   $('#packageDetailsIP').modal('show');
													}
												});
										}
					/**================================package order========================================================= */		
				/*		========================================Insurance document upload============================*/
										
										
										 $scope.addImageDetailsForInsurance = function()
											{ 
												var encounterDetails = angular.copy($scope.encounterDetailsInsurance);
												encounterDetails.admissionId = $scope.itemSelecteds.admissionId;
												encounterDetails.patientId = $scope.itemSelecteds.patientId;
												$scope.encounterDetailsInsuranceList.push(encounterDetails);
											};

										  $scope.removeImageDetailsInsurance = function(deleteIndex)
											{ 
											   $scope.encounterDetailsInsuranceList.splice(deleteIndex, 1);
										    }
										  
										$scope.clearDocumentList=function(){
											 $scope.encounterDetailsContractList = [];
											   $scope.finalList = [];
											   $scope.encounterDetailsInsuranceList=[];
										}
										
										$scope.searchAdmittedPatient = function(searchString){
											$rootScope.startLoader();
											$scope.bedMasterList=[];
											$scope.loadingMsg="Loading...";
											if(searchString!=''){
												var data = {
														unitId : $scope.unitId,
														organizationId : $scope.organizationId,
														offset : $scope.offset,
														noOfRecordsPerPage : $scope.noOfRecordsPerPage,
														patientName : searchString
												};
											var URI = BASE_URL + ROOT_URL + "/adt/admittedPatientListUniversalSearch"
													
											console.log("URI", URI);
											GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
																$rootScope.stopLoader();
																$scope.listOfAdmittedPatientList = response.data.listObject;
																
																if($scope.listOfAdmittedPatientList.length==0)
																	$scope.loadingMsg="No record found...";
												});
											
											}else{
												$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);

											}
											$rootScope.stopLoader();
										}
										
										
										$scope.printVisitorPass=function(info){
											  var data =  {
											                 "appName": "HIS",
											                 "appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",
											                 "reportDesignFilePath": "PatientGatepass.rpt",
											                 "reportFormat": "PDF",
											                 "exportFileName": "Coversheet",
											                 "reportParams": [
											                	 {
													                  "paramName": "visitor_patient_id",
													                  "paramType": "Integer",
													                  "paramValue":  info.visitorPatientId
												                },
												                
									                 ]
									      };
											    reportService.generate(REPORT_API_URI1,data);
											}
										
										
										$scope.getPassIssuedList = function(admissionId) {
											var data = {
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												admissionId : admissionId
											};
											
											$scope.visitPassObj();
											
											var URI = BASE_URL + ROOT_URL
													+ GETPASSISSUEDLIST;
											GenericService
													.serviceAction("POST", URI, data)
													.then(
															function(response) {
																$scope.passIssuedList = [];

																if (response.data.status == "success") {
																	$scope.passIssuedList = response.data.listObject;
																	
																}
																
															});
										}
										
										$scope.cancelPaymentReqModal = function(){
											angular.element('#paymentReqModal').modal('hide');
										}
										
										$scope.clearMpesaFields = function(){
											$scope.skipMpesaValidation = false;
											$scope.mpesaObject={"transactionId":null,"mobileNo":null,"mpesaAmount":null,"resTransactionId":null};
										}
										
										$scope.clearCardFields =  function(){
											$scope.flagForSale=false;
											 $scope.flagForSMSpay=false;
											 $scope.flagForBharatQr=false;
											 $scope.objPosMachine.deviceName = undefined;
											 $scope.objPosMachine.isSale = false;
											 $scope.objPosMachine.isBharatQr = false;
											 $scope.objPosMachine.isSMSpay = false;
											$scope.pinpadAmounFlag = false;
											$scope.paymentDetails = angular.copy({paymentModeId:$scope.paymentDetails.paymentModeId,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:"",hospitalAccountId:"",cardBalAmount:$scope.paymentDetails.cardBalAmount,loyaltyCardNo:$scope.paymentDetails.loyaltyCardNo,loyaltyCardAmt:""});
											$scope.calculateDueAmount();
										}
										
										
										$scope.updateEditPatientVisitDetailsBtn = false;
										 var flagKin=false;
										$scope.updateEditPatientVisitDetails = function(){
											var checkKinPri = false;
											 var checkKinName = false;
											 var checkKinMobile=false;
											 var checkKinRelation=false;
											 var checkKinPhone=false;
											 var checkKinAddress=false;
											 var checkKinContry=false;
											 var checkKinState=false;
											 var checkKinDistrict=false;
											 var checkKinCity=false;
											 var checkKinArea=false;
											 var checkKinPost=false;
											 var checkKinIdenti=false;
											 var checkKinIdentiNo=false;
											/*$rootScope.startLoaderPopup();*/
											$scope.updateEditPatientVisitDetailsBtn = true;
											angular.element('#editEncounterModal').modal('hide');
											/*$rootScope.startLoaderPopup();*/

											var dischargeDate = moment($scope.datepickerPdd).format('YYYY-MM-DD HH:mm:ss');
											 var  dateOfPdd = dischargeDate;
											 
											 $scope.currentDate = new Date();
											 $scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
											  
											  
											 var data = {
													 
													    patientId : $scope.admissionRequestOrAdmissionObj.patientId,
													    admissionId:$scope.admissionRequestOrAdmissionObj.admissionId,
													    admissionDetailsId : $scope.admissionRequestOrAdmissionObj.admissionDetailsId,
											            admissionTypeId:$scope.admissionRequestOrAdmissionObj.admissionTypeId,
											            reasonId:$scope.admissionRequestOrAdmissionObj.reasonId,
														pdd : dateOfPdd,
														mealPrefId : $scope.admissionRequestOrAdmissionObj.mealPrefId,
														attendentMealPrefId : $scope.admissionRequestOrAdmissionObj.attendentMealPrefId,
														referalDetailsId : $scope.admissionRequestOrAdmissionObj.referalDetailsId,
														referralTypeId : $scope.admissionRequestOrAdmissionObj.referralTypeId,
														referralId : $scope.admissionRequestOrAdmissionObj.referralId,
														referralRemark :  $scope.admissionRequestOrAdmissionObj.referralRemark,
														medicoLegalId : $scope.medicoLegalId,
														accompaniedBy : $scope.admissionRequestOrAdmissionObj.accompaniedName,
														firNo        :$scope.admissionRequestOrAdmissionObj.firNo,
														policeBuckleNo :$scope.admissionRequestOrAdmissionObj.policeBuckleNo,
														policeStationName:$scope.admissionRequestOrAdmissionObj.policeStationName,
														mlcContactNo:$scope.admissionRequestOrAdmissionObj.mlcContactNo,
														mlcRelationId:$scope.admissionRequestOrAdmissionObj.mlcRelationId,
														mlcRelationId:($scope.admissionRequestOrAdmissionObj.mlcRelationId != "0" &&  $scope.admissionRequestOrAdmissionObj.mlcRelationId != null)?  $scope.admissionRequestOrAdmissionObj.mlcRelationId : null,
														priorityId : ($scope.admissionRequestOrAdmissionObj.priorityId != "0" &&  $scope.admissionRequestOrAdmissionObj.priorityId != null)?  $scope.admissionRequestOrAdmissionObj.priorityId : null,
														isMedicoLegal : $scope.admissionRequestOrAdmissionObj.isMedicoLegal,
														remark : $scope.admissionRequestOrAdmissionObj.remark,
														//end MLC
														status:'A',
														organizationId : $scope.organizationId,
														unitId : $scope.unitId,
														updatedBy : $scope.userId,
														createdBy : $scope.userId,
														createdDate : $scope.currentDate,
														updatedDate : $scope.currentDate,
														kinDetailsId : $scope.admissionRequestOrAdmissionObj.kinDetailsId,
														reasonId:$scope.admissionRequestOrAdmissionObj.reasonId,
														referenceNumber : $scope.referenceNumber,
														kinArray : $scope.kinDetailsList,
														admittingSpecialityId:$scope.admissionRequestOrAdmissionObj.specialityId,
														admittingDoctorId:$scope.admissionRequestOrAdmissionObj.doctorId,
														doctorId : $scope.admissionRequestOrAdmissionObj.treatingDoctorId != null ? $scope.admissionRequestOrAdmissionObj.treatingDoctorId : $scope.admissionRequestOrAdmissionObj.doctorId,
												        specialityId : $scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId != null ? $scope.admissionRequestOrAdmissionObj.treatingDoctorSpecialityId : $scope.admissionRequestOrAdmissionObj.specialityId,
												        listVisiterPassMaster:$scope.listVisiterPassMaster,
														
													}
									
											 if($scope.listVisiterPassMaster.length > 0 &&  $scope.listVisiterPassMaster[0].passNumber != ""){
													data.listVisiterPassMaster = $scope.listVisiterPassMaster;
												}
											 if($scope.kinDetailsList.length >0){
												 angular.forEach($scope.kinDetailsList,function(value,key){
												  if(value.identificationId== undefined || value.identificationId==null || value.identificationId==0){
                                                 		return false
                                                 		}
													 var splittedStringArray = value.identificationId.split("_");
													 $scope.kinDetailsList.identificationId = splittedStringArray[0];
													 value.identificationId =parseInt(splittedStringArray[0]);
														if($scope.registrationFieldObj.kinprefix== "Y"){
															if(value.prefixId == undefined || value.prefixId == null || value.prefixId ==""){
																checkKinPri = true;
																$scope.updateEditPatientVisitDetailsBtn = false;
															}
														}
														 if($scope.registrationFieldObj.kinName== "Y"){
															if(value.kinName == undefined || value.kinName == null || value.kinName ==""){
																checkKinName = true;
																$scope.updateEditPatientVisitDetailsBtn = false;
															}
														}
														 if($scope.registrationFieldObj.relationId== "Y"){
																if(value.relationId == undefined || value.relationId == null || value.relationId ==""){
																	checkKinRelation = true;
																	$scope.updateEditPatientVisitDetailsBtn = false;
																}
															}
														 if($scope.registrationFieldObj.kinMobileNo== "Y"){
																if(value.mobileNo == undefined || value.mobileNo == null || value.mobileNo ==""){
																	checkKinMobile = true;
																	$scope.updateEditPatientVisitDetailsBtn = false;
																}
															}
														
														 if($scope.registrationFieldObj.phoneNo== "Y"){
																if(value.phoneNo == undefined || value.phoneNo == null || value.phoneNo ==""){
																	checkKinPhone=true;
																	$scope.updateEditPatientVisitDetailsBtn = false;
																}
															}
														 if($scope.registrationFieldObj.kinAddress== "Y"){
																if(value.address == undefined || value.address == null || value.address ==""){
																	checkKinAddress=true;
																	$scope.updateEditPatientVisitDetailsBtn = false;
																}
															}
														 if($scope.registrationFieldObj.countryId== "Y"){
																if(value.countryId == undefined || value.countryId == null || value.countryId ==""){
																	checkKinContry=true;
																	$scope.updateEditPatientVisitDetailsBtn = false;
																}
															}
														 if($scope.registrationFieldObj.stateId== "Y"){
																if(value.stateId == undefined || value.stateId == null || value.stateId ==""){
																	checkKinState=true;
																}
															}
														 if($scope.registrationFieldObj.districtId== "Y"){
																if(value.districtId == undefined || value.districtId == null || value.districtId==""){
																	checkKinDistrict=true;
																	$scope.updateEditPatientVisitDetailsBtn = false;
																}
															}
															 if($scope.registrationFieldObj.cityId== "Y"){
																if(value.cityId == undefined || value.cityId == null || value.cityId ==""){
																	checkKinCity=true;
																	$scope.updateEditPatientVisitDetailsBtn = false;
																}
															}
															 if($scope.registrationFieldObj.areaId== "Y"){
																if(value.areaId == undefined || value.areaId == null || value.areaId ==""){
																	checkKinArea=true;
																	$scope.updateEditPatientVisitDetailsBtn = false;
																}
															}
															 if($scope.registrationFieldObj.postCode== "Y"){
																if(value.postCode == undefined || value.postCode == null ||value.postCode ==""){
																	checkKinPost=true;
																	$scope.updateEditPatientVisitDetailsBtn = false;
																}
															}
															 if($scope.registrationFieldObj.identificationId== "Y"){
																if(value.identificationId == undefined || value.identificationId == null ||value.identificationId ==""){
																	checkKinIdenti=true;
																	$scope.updateEditPatientVisitDetailsBtn = false;
																}
															}
															 if($scope.registrationFieldObj.identificationNo== "Y"){
																if(value.identificationNo == undefined || value.identificationNo == null || value.identificationNo ==""){
																	checkKinIdentiNo=true;
																	$scope.updateEditPatientVisitDetailsBtn = false;
																}
															}
												 });
												 if($scope.admissionRequestOrAdmissionObj.doctorId == ""){
													 growl.error("Please select admitting doctor",{});
													 return false;
												 }
												 if($scope.admissionRequestOrAdmissionObj.treatingDoctorId == ""){
													 growl.error("Please select treating doctor",{});
													 return false;
												 }
												 if(checkKinPri == true){
													 growl.warning("Please Add Prefix For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinName == true){
													 growl.warning("Please Add Name For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinRelation == true){
													 growl.warning("Please Add Relation For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinMobile == true){
													 growl.warning("Please Add Mobile No For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinPhone == true){
													 growl.warning("Please Add Phone No For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinAddress == true){
													 growl.warning("Please Add Address For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinContry == true){
													 growl.warning("Please Add Country For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinState == true){
													 growl.warning("Please Add State For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinDistrict == true){
													 growl.warning("Please Add District For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinCity == true){
													 growl.warning("Please Add City For Next Of KIN",{
														}); 
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinArea == true){
													 growl.warning("Please Add Area For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinPost == true){
													 growl.warning("Please Add Post Code For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinIdenti == true){
													 growl.warning("Please Add Identification Type For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
												 if(checkKinIdentiNo == true){
													 growl.warning("Please Add Identification No For Next Of KIN",{
														});
													 flagKin = true;
													 $rootScope.stopLoaderPopup();
														return false;
												 }
											 }else{
												 flagKin==false;
								    		 }
										
											    var URI = BASE_URL + ROOT_URL + "/adt/editAdmissionDetailsByAdissionIdAdmiitedPatientList";
			    								GenericService.serviceAction("POST",URI,data).then(function(response){
			    									if (response.data.status == 'success')
			    									{
			    										/*$rootScope.stopLoaderPopup();*/
			    										$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);
			    										$scope.getKinDetailsByPatientId();
			    										$scope.medicoLegalId = null;
			    										$scope.initKinDetails();
														growl.success('Updated Successfully',
																{
																	title : 'Success!'
																});
														angular.element('#editEncounterModal').modal('hide');
														$scope.updateEditPatientVisitDetailsBtn = false;
			    									} 

			    									 else{
														 growl.warning("Please select at least one visitor pass type",{
																title : WARNING_MSG
															});
															return;
													 }
			    									

			    								});
											 
										}
																				
										$scope.patientObj.bucketFlag="";
										
										$scope.uploadDocument = function()
                                            {
                                              $scope.finalDocUploadList=[];
                                              var count=0;
                                          if($scope.patobj.patientImage1 != ""){
                                            $scope.encounterDetailsList[0].file =  $scope.patobj.patientImage1;
                                             angular.forEach($scope.encounterDetailsList, function(value, key) {
                                                  if (angular.equals(value.file, {}) || value.file==undefined) {
                                                        growl.warning('Please select file.',
                                                                        {
                                                                            title : 'warning!'
                                                                        });
                                                         $rootScope.stopLoader();
                                                        return false;
                                                    }
            //										 var ext = value.file.name.split(".").pop().toLowerCase();
            //											if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
            //												growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
            //														{
            //															title : 'warning!'
            //														});
            //												$rootScope.stopLoader();
            //												return false;
            //											}
                                                        $scope.finalDocUploadList.push(value);
                                                         console.log("final list",$scope.finalDoc);
                                            });

                                             if($scope.encounterDetailsList.length==$scope.finalDocUploadList.length){
                                                   $('#uploadDetails').modal('hide');

                                             $rootScope.startLoader();
                                              $scope.count = 0;
                                             angular.forEach($scope.encounterDetailsList, function(value, key)
                                                {
                                                    var promise = $scope.uploadEncounterDoc(value);
                                                    promise.then(function(response)
                                                            {
                                                              if(response.data.status == "success")
                                                                  {
                                                                    $scope.count++;
                                                                    if($scope.encounterDetailsList.length==$scope.count)
                                                                     {
                                                                       growl.success("Document Uploaded Successfully.",
                                                                                {
                                                                                    title : 'Success!'
                                                                                });
                                                                       $scope.finalDocUploadList=[];
                                                                       $scope.encounterDetailsList = [];
                                                                       $rootScope.stopLoader();
                                                                     }
                                                                  }
                                                              else
                                                                {
                                                                    growl.error('Something went wrong.',
                                                                            {
                                                                                title : 'Error!'
                                                                            });
                                                                }
                                                            });

                                                });
                                             }
                                             }

                                            }


                                            $scope.uploadEncounterDoc = function(Obj)
                                            {
                                                 var fd = new FormData();
                                                    fd.append('encounterId', 0);
                                                    fd.append('visitTypeId', 2);
                                                    fd.append('admissionId', Obj.admissionId);
                                                    fd.append('patientId',Obj.patientId );
                                                    fd.append('documentTypeId', Obj.documentTypeId );
                                                    fd.append('createdBy',Obj.createdBy );
                                                    fd.append('updatedBy', Obj.updatedBy);
                                                    fd.append('unitId', Obj.unitId);
                                                    fd.append('organisationId', Obj.organisationId);
                                                    fd.append('expenseId', 0);
                                                    fd.append('file',Obj.file );
                                                    fd.append('expenseId', 0);
                                                    fd.append('grnId',0);
                                                    fd.append('poId',0);
                                                    fd.append('incidentReportingMapperId',0);
                                                    fd.append('companyExpenseId',0);
                                                    fd.append('remark','');
                                                    fd.append('createdDate',null);
                                                    fd.append('fileEditedName',null);
                                                    fd.append('opbId',0);
                                                    fd.append('vendorId',0);
                                                    fd.append('vendorId',0);
                                                    var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
                                                    return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response)
                                                    {
                                                        return response;
                                                    });
                                            }


                                            $scope.getPatientListDocument = function()
                                             {

                                              $rootScope.startLoader();
                                              var data =
                                                {
                                                "patientId":$scope.admissionRequestOrAdmissionObj.patientId,
                                                "unitId":$scope.unitId,
                                                "organisationId":$scope.organizationId,
                                                "userId":undefined,
                                                "visitTypeId":undefined,
                                                "admissionId":undefined,
                                               }
                                              var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
                                              BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
                                                $scope.patientListDocument = [];
                                                if (response.data.status == "success")
                                                {
                                                    $scope.patientListDocument = response.data.listObject;

                                                    $rootScope.stopLoader();
                                                }
                                                else if(response.data.status == "error")
                                                 {
                                                    $rootScope.stopLoader();
                                                 }
                                               });
                                             }
										
										$scope.patientPreviosMLCDocumentList = [];
		                                 $scope.getPatientMLCDocumentList = function(){
		                                        var data ={
		                                                patientId : $scope.admissionRequestOrAdmissionObj.patientId,
		                                                unitId : $scope.unitId,
		                                                organizationId :     $scope.organizationId
		                                        }; 
		                                            $scope.patientPreviosMLCDocumentList = [];
		                                        var URI = BASE_URL + ROOT_URL+"/api/global/getPatientMLCDocumentDetailList";
		                                        BillingGenericService
		                                                .serviceAction(METHOD_POST,URI, data)
		                                                .then(
		                                                        function(response) {
		                                                            if (response.data.status == "success"){

		                                                                if(response.data.listObject != null && response.data.listObject.length > 0){
		                                                                	$scope.patientPreviosMLCDocumentList = response.data.listObject;
			                                                                $scope.patientObj.bucketFlag = response.data.listObject[0].bucketFlag;
			                                                                var abc="data:image/png;base64,";
			                                    							if($scope.patientObj.bucketFlag == undefined || $scope.patientObj.bucketFlag == 1 ){
			                                    								$scope.patobj.filepreviewMLC = $scope.patientPreviosMLCDocumentList[0].documentPath;
			                                    							}else{
			                                    							$scope.patobj.filepreviewMLC = abc+$scope.patientPreviosMLCDocumentList[0].documentPath;
			                                    							  }		                                                                
			                                                                $rootScope.stopLoader();
		                                                                }

		                                                            }else{
		                                                                $rootScope.stopLoader();
		                                                            }
		                                                        });
		                                        }
																			
										$scope.admissionCancelObj={};
										$scope.setObject=function(obj){
											$scope.admissionCancelObj=obj;
										}
										
										$scope.cancelAdmission=function(){
											//$scope.admissionCancellationTime=
											if($scope.admissionCancellationTime==undefined || $scope.admissionCancellationTime==null){
												  growl.warning("Admission Cancellation Time Limit is not Configured",
															{
																title : 'Warning!'
															});
												   return false;
											}
											var admDate=($scope.admissionCancelObj.createdDate).split("/");
											var admissionDate=admDate[2]+"-"+admDate[1]+"-"+admDate[0];//: "30/07/2021"
											var admissionTime=$scope.admissionCancelObj.createdTime;//: "01:39 PM" +
											var date1_ms=new Date(moment(new Date(admissionDate+" "+admissionTime)).format("YYYY-MM-DD HH:mm")).getTime();
											var date2_ms =new Date(moment(new Date()).format("YYYY-MM-DD HH:mm")).getTime();
											  // Calculate the difference in milliseconds
										  var difference_ms = date2_ms - date1_ms;
										  //take out milliseconds
										  difference_ms = difference_ms/1000;
										  var seconds = Math.floor(difference_ms % 60);
										  difference_ms = difference_ms/60; 
										  var minutes = Math.floor(difference_ms % 60);
										  difference_ms = difference_ms/60; 
										  var hours = Math.floor(difference_ms % 24);  
										  var days = Math.floor(difference_ms/24);
										  var totalMinutes=0;
										   if(days>0){
											   totalMinutes=(days*24*hours*60)*(minutes>0?minutes:1); 
										   }else{
											   totalMinutes=(hours*60)+(minutes>0?minutes:1);
										   }	
										   var cancellationTimeLimitInHours=$scope.admissionCancellationTime*60;
										   if(totalMinutes>(cancellationTimeLimitInHours)){
											   growl.warning("Admission can not cancelled, that patient has been admitted before 24hrs.",
														{
															title : 'Warning!'
														});
											   return false;
										   }
										   
											$scope.currentDate = new Date();
											$scope.currentDate = moment($scope.currentDate)
										  
										   var data={
												   "admissionId":$scope.admissionCancelObj.admissionId,
												   "userId":$scope.userId,
												   "unitId" : $scope.unitId,
												   "updatedDate": $scope.currentDate,
												   "updatedBy":$scope.userId,
													"organizationId":$scope.organizationId,	
													"bedId" : $scope.admissionCancelObj.bedId,
										   }
											var URI = BASE_URL + ROOT_URL + "/adt/admissionCancellation";
		    								GenericService.serviceAction("POST",URI,data).then(function(response){
		    									$('#admissionCancelModal').modal('hide');

//		    									$('#admissionCancelModal').hide('hide');
//		    									$(".modal-backdrop").hide();
		    									if (response.data.status == 'success')
		    									{
		    										growl.success('Admission Cancelled Successfully',
															{
																title : 'Success!'
															});
		    										$scope.initAdmittedPatientList($scope.organizationId,$scope.unitId,$scope.offset, $scope.noOfRecordsPerPage);
		    									}else{
		    										growl.warning(response.data.message,
															{
																title : 'Warning!'
															});
		    									}
		    								});
										}
										 $scope.dateOptions = {							 
											      formatYear: 'yyyy',
											      showWeeks: false,
											      minDate: new Date(),
											  };
											 $scope.openDatePicker = function() {
											      $scope.datepickerFutureDate.opened = true;
											    };
											    $scope.datepickerFutureDate = {
													      opened: false
													    };

										
										$scope.reasonList = [];
										$scope.getResonList = function(){
											
											var data = {
													organizationId : $scope.organizationId
											};
											var URI = BASE_URL + ROOT_URL
											+ GETACTIVEREASONLIST;
											GenericService.serviceAction("POST", URI, data).then(
													function(response) {
														$scope.reasonList = [];

														if (response.data.status == "success") {
															$scope.reasonList = response.data.listObject;

														}
													});
											}
										$scope.getResonList();
										
										
										$scope.popupDischarge = function(item)
										{
											$scope.dischargeTypeId =0;
											$scope.itemForDischargePatient = item;
										}
										
										$scope.dueForDischarge = function(item)
										{
											$scope.note="";
											$scope.reasonId = 0;
											$scope.itemForDischargePatient = item;
										}


										$scope.updateButton = false;

										$scope.getMealPrefId = function (patient) {
										    $scope.mealPrefId = patient.mealPreferenceId;
//										    $scope.dietTypeId = null;
//										    $scope.dietRemarkDesc='';
										    $scope.updateAdmittedPatientList(patient);
										    
										};
										$scope.getDietTypeId = function (patient) {
										    $scope.dietTypeId = patient.dietTypeId;
//										    $scope.mealPrefId = null;
//										    $scope.dietRemarkDesc='';
										    $scope.updateAdmittedPatientList(patient);
										   
										};

										$scope.getPatientInfo = function (patient) {
										    $scope.admissionId = patient.admissionId;
										    $scope.patientObj1 = patient;
										    $scope.dietTypeId = patient.dietTypeId;
										    $scope.mealPrefId = patient.mealPreferenceId;
										    $scope.dietRemarkDesc = patient.dietRemarkDesc;
										    
										};

										$scope.updateAdmittedPatientList = function (patientObj1) {
										    console.log(patientObj1)
										    if($scope.dietRemarkDesc==undefined || $scope.dietRemarkDesc==null || $scope.dietRemarkDesc==""){
										       growl
                                                .warning(
                                                        'Please Enter Remark..!!',
                                                        {
                                                            title : 'Warning!'
                                                        });
                                                return false;
										    }
										    $scope.updateButton = true;
										    $scope.admissionId = patientObj1.admissionId;
										    if ($scope.admissionId == undefined) {
										        return false;
										    }

										    var data = {
										        mealPrefId: $scope.mealPrefId != undefined ? $scope.mealPrefId : null,
										        dietTypeId: $scope.dietTypeId != undefined ? $scope.dietTypeId : null,
										        dietRemarkDesc: $scope.dietRemarkDesc != undefined ? $scope.dietRemarkDesc : '',
										        admissionId: $scope.admissionId,
										    };
										    var URI = BASE_URL + ROOT_URL + '/adt/discharge/updateAdmittedPatientList';
										    GenericService.serviceAction('POST', URI, data).then(
										    		 function (response) {
										                if (response.data.status == 'success') {
											                $rootScope.stopLoader();
											                angular.element('#dietTyperemark').modal('hide');
											                growl.success('Admitted Patient list updated successfully', {
											                    title: 'Success!',
											                });
  
											                $scope.updateButton = false;
										                $scope.init();
										                $scope.initAdmittedPatientList($scope.organizationId, $scope.unitId, $scope.offset, $scope.noOfRecordsPerPage);

										              
										            } else if (response.data.status == 'error') {
										                $rootScope.stopLoader();
										                $scope.updateButton = false;
										                growl.error(response.data.message, {
										                    title: 'Error!',
										                });
										            }
										        }
										    );
										};


										$scope.SearchObjectClear = function(item)
										{
											$scope.admissionRequestOrAdmissionObj.referralTypeId="";
											$scope.admissionRequestOrAdmissionObj.referralId="";
											$scope.admissionRequestOrAdmissionObj.referralDesc="";
											$scope.admissionRequestOrAdmissionObj.referralRemark="";
										}
										
					                    $scope.closeModel=function(){
					                         
					                         angular.element('#editEncounterModal').modal('hide');
					                         }

										$scope.updateExtendPdd = function()
										{
											if($scope.reasonId == 0 || $scope.reasonId == '')
											{
											growl
											.warning(
													'Please select reason..!!',
													{
														title : 'Warning!'
													});
											return false;
											
											}
										else if($scope.datepicker == undefined || $scope.datepicker == "")
											{
												growl
												.warning(
														'Please select future date..!!',
														{
															title : 'Warning!'
														});
												return false;
											}
										else{
													$('#popupDischargeCancel').modal('hide');
													$rootScope.startLoader();
													$scope.currentDate = new Date();
													$scope.currentDate = moment($scope.currentDate)
															.format('DD-MM-YYYY HH:mm:ss');
//													/updateExtendPdd
													if($scope.itemForDischargePatient.dischargeId == undefined || $scope.itemForDischargePatient.dischargeId ==  null ){
														
														var data = {
																admissionId : $scope.itemForDischargePatient.admissionId,
																pdd : moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
															};
															var URI = BASE_URL + ROOT_URL+ "/adt/discharge/updateExtendPdd";
															GenericService.serviceAction("POST", URI, data).then(
																			function(response) {

																				if (response.data.status == "success") {
																					$scope.pddDate=moment($scope.datepicker).format('DD-MM-YYYY')
																					$rootScope.stopLoader();
																					growl.success(
																									'Discharge request cancelled and Extended PDD is '+$scope.pddDate,
																									{
																										title : 'Success!'
																									});
																					angular.element('#popupDischargeCancel').modal('hide');
																					$scope.initAdmittedPatientList();
																					$scope.datepicker = undefined;
																				} else if (response.data.status == "error")
																				{
																					$rootScope.stopLoader();
																					growl.error(response.data.message,
																									{
																										title : 'Error!'
																									});
																					$scope.init();
																				}
																			});
														
														
														
													}else{
														
														var data = {
																organizationId : $scope.organizationId,
																dischargeId : $scope.itemForDischargePatient.dischargeId,
																unitId : $scope.unitId,
																reasonId : $scope.reasonId,
																admissionId : $scope.itemForDischargePatient.admissionId,
																updatedBy : $scope.userId,
																updatedDate : $scope.currentDate,
																pdd : moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss'),
																noteCancel : $scope.note,
																dischargeStatusId :6
															};
															console.log(data);
															//return false;
															var URI = BASE_URL + ROOT_URL
																	+ CANCELDISCHARGEINBILL;
															GenericService
																	.serviceAction("POST", URI, data)
																	.then(
																			function(response) {

																				if (response.data.status == "success") {
																					$scope.pddDate=moment($scope.datepicker).format('DD-MM-YYYY')
																					$rootScope.stopLoader();
																					growl
																							.success(
																									'Discharge request cancelled and Extended PDD is '+$scope.pddDate,
																									{
																										title : 'Success!'
																									});
																					angular.element('#popupDischargeCancel').modal('hide');
																					$scope.getAdmittedPatientByFloorWardId();
																					$scope.datepicker = undefined;
																				} else if (response.data.status == "error")
																				{
																					$rootScope.stopLoader();
																					growl
																							.error(response.data.message,
																									{
																										title : 'Error!'
																									});
																					$scope.init();
																				}
																			});
														
													}
											
											
										  	}
								}

		$scope.analyzeReport = function(document){
            data = {};
            if(document.documentTypeId === 27 || document.documentTypeId === 100){
                data.callBackBaseUrl = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT;
                data.documentPath = document.documentPath;
                data.documentName = document.documentName;
                data.documentTypeId = document.documentTypeId;
                data.unitId = $scope.unitId;
                data.encounterDocId = document.encounterDocId;
                data.patientId = document.patientId;

                var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT + "/analyzeReportsWithAI";
                $rootScope.startLoader();
                GenericService.serviceAction("POST",URI1,data).then(function(response){
                    if (response.data.status == 'success') {
                        growl.success('Document sent for analysis.',
                        {
                            title : 'Success!'
                        });
                    }
                    else{
                        growl.warning(response.data.message,
                        {
                            title : 'Warning!'
                        });
                    }
                    $rootScope.stopLoader();
                });
            }
            else{
                growl.warning("Document type not supported for AI analysis.",
                {
                    title : 'Warning!'
                });
            }
		};

        $scope.getAnalyzedReport = function(document){

            var URI1 = BASE_URL + ROOT_URL + UPLOAD_ENCOUNTER_DOCUMENT + "/getAnalyzedReport" + "/" +document.encounterDocId;
            $rootScope.startLoader();
            GenericService.serviceAction("GET",URI1).then(function(response){
                if ((response.data.status == 'success') && (response.data.object != undefined && response.data.object != null)) {

                    var responseObj = response.data.object;
                    angular.forEach($scope.patientDocumentList,function(value,index){
                        if(value.encounterDocId === responseObj.encounterDocId){
                            value.videolink = responseObj.videolink;
                        }
                    });
                    response.data.object.encounterDocId = document.encounterDocId;
                    console.log(response);
                }
                else{
                    growl.warning(response.data.message,
                    {
                        title : 'Warning!'
                    });
                }
                $rootScope.stopLoader();
            });

        };
										$scope.registrationFieldObj = {};
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
																email:"N",
																sourceId:"N"
														}
													}
												
												

											} else if (response.data.status == 'error') {
												growl.error(response.data.message,
													{
														title: 'Error!'
													});
											}
										});			
										
				    } ]);
