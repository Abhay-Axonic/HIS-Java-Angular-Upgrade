
'use strict';

/**
 * @Author By  shubhangi
 * @name myApp.controller:uhidMergeRequestController
 * @description #uhidMergeRequestController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'uhidMergeRequestController',
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
						'PagerService',
						'BillingGenericService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService, BillingGenericService) {

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.totalNoOfRecords;
							//alert("hii");
							/* init() function for form object create. */

							$scope.init = function() {

								/*
								 * $scope.unitId =
								 * sessionStorage.getItem("unitId");
								 * $scope.organizationId =
								 * sessionStorage.getItem("organizationId");
								 * $scope.userId =
								 * sessionStorage.getItem("userId");
								 */
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								
								$scope.searchKeyword;
								$scope.commonListCount;

								$rootScope.loginpage = true;
								
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								var data = {};
								// $scope.status = "NO";
								/*
								 * var URI = BASE_URL + ROOT_URL +
								 * GETCOUNTRYLIST; GenericService
								 * .serviceAction("GET", URI, data) .then(
								 * function(response) { $scope.countryList = [];
								 * 
								 * if (response.data.status == "success") {
								 * $scope.countryList =
								 * response.data.listObject; console
								 * .log($scope.countryList); } });
								 */
							}
					

							$scope.init();
							
							
							
							//UHID I
							$scope.initPatientSearchObj = function(){
								$scope.patientSearchObj = {
										"uhIdNumber"      : "",
										"patientName"     : "",
										"genderId"        : "",
										"mobileNo"        : "",
										"birthDate"       : "",
										"identificationNo":"",
										"patientType"	  :"1",
										"unitId"          :$scope.unitId,
										"organizationId"  :$scope.organizationId,
										"fromUhidNumber"  :"",
										"userId" :"",
										"toPatientName"  :"",
										
										

										
								}
								
							}
							$scope.patientObj = {
									registrationTypeId:1
							};
								
							$scope.fromUhidNumber ="";
							$scope.toUhidNumber="";
							
							$scope.initPatientSearchObj();
							$scope.patientListFrom = [];
							
							$scope.flag;
							
							$rootScope.searchPatient = function(patientsearchObj){
							
								var flag= false;
								if(patientsearchObj.fromUhidNumber!=undefined && patientsearchObj.fromUhidNumber!=null && patientsearchObj.fromUhidNumber!="")
	                            {
									patientsearchObj.uhIdNumber = patientsearchObj.fromUhidNumber;
									flag=true;
	                            }
								
								/*if(patientsearchObj.toUhidNumber!=undefined && patientsearchObj.toUhidNumber!=null && patientsearchObj.toUhidNumber!="")
								{
									$scope.patientsearchObj.uhIdNumber = patientsearchObj.toUhidNumber;
																
								}*/
								/**/
								if($scope.patientSearchObj.uhIdNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNo == '' && $('#datepickerSearch').val() == '' && ( patientsearchObj.identificationTypeId == "" || patientsearchObj.identificationTypeId == undefined ) && $scope.patientSearchObj.identificationNo == '')

								{
									growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"
									});
									return false;
								}
									
								else{
										//alert("else AlERT");
									}
								if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
								{
									growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
									$scope.patientListFrom=[];
									return false;
								}	
								$rootScope.startLoader();
								
							    if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
						            patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
						        } 
								var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
								BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									$scope.patientListFrom=[];
									
									if(response.data.listObject.length == 0){
										growl.warning("No Result Found!",{
											title : ERROR_MSG
										});
									}
									else{
//										$scope.patientListFrom=[];
									angular.forEach(response.data.listObject,function(value,key)
											{
										  		if(value.registrationTypeId != 5 && value.registrationTypeId != 4)
										  			{
										  			
										  				$scope.patientListFrom.push(value);
										  			
										  			}
										  		
											});
									}
									
								});
								
							}
							//select radio button
							$scope.getFromPatientDetailsById = function(patientId){
								$rootScope.startLoader();
								$scope.patientDetailsList = [];
								$scope.patientDetails = {}
								$scope.fromPatientId = patientId;
								
							/*	var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$scope.billSearch = {patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId};
										$scope.patientDetailsList = response.data.listObject;
										$scope.patientDetails = response.data.listObject[0];
										$scope.patientDetails.patientName = $scope.patientDetails.firstName + " " + $scope.patientDetails.lastName;
										$scope.patientDetails.genderCode = $scope.patientDetails.genderName.charAt(0).toUpperCase();
										$scope.patientDetails.age = $scope.patientDetails.ageString
										$scope.patientDetails.mobileNo = $scope.patientDetails.mobileNumber;
										$scope.billSearch.patientId = $scope.patientDetails.patientId; 
										$('.patienttSearchResult').toggle();
								
							
									}
								});*/
								
								$rootScope.stopLoader();
							}
							
							//second serch
							$scope.initPatientSearchObjTo = function(){
								$scope.patientSearchObjtTo = {
										"uhIdNumber"      : "",
										"patientName"     : "",
										"genderId"        : "",
										"mobileNo"        : "",
										"birthDate"       : "",
										"identificationNo":"",
										"patientType"	  :"1",
										"userId" 		  :"",
										"username"        :"",
										"unitId"          :$scope.unitId,
										"organizationId"  :$scope.organizationId,
										"fromUhidNumber"  :"",
										"toUhidNumber"    :"",
										"fromPatientId " :"",
										"toPatientId " :"",
										"toPatientName":"",
										
										
								}
								
							}
							
							
							$scope.initPatientSearchObjTo();
							
							$scope.toPatientName="";
							$scope.patientListTo = [];
							$scope.flagTo;
							
							$rootScope.searchPatientTo = function(patientsearchObj){
								
								
								patientsearchObj.patientName = $scope.toPatientName;
								
						
								var flagTo= false;
								
								if(patientsearchObj.toUhidNumber!=undefined && patientsearchObj.toUhidNumber!=null && patientsearchObj.toUhidNumber!="")
								{
									patientsearchObj.uhIdNumber = patientsearchObj.toUhidNumber;
									flagTo=true;							
								}
								if($scope.patientSearchObj.uhIdNumber == '' && $scope.patientSearchObj.toPatientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNo == '' && $('#datepickerSearch').val() == '' && ( patientsearchObj.identificationTypeId == "" || patientsearchObj.identificationTypeId == undefined ) && $scope.patientSearchObj.identificationNo == '')

								{
									growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"
									});
									return false;
								}
									
								else{
										//alert("else AlERT");

									}
								
								if($scope.patientSearchObj.toPatientName!="" && $scope.patientSearchObj.toPatientName.length < 3)
								{
									growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
									$scope.patientList = [];
									
									return false;
								}	
								
								
								$rootScope.startLoader();
								
							    if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
						            patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
						        } 
							    patientsearchObj.patientName= patientsearchObj.toPatientName;
							    
								var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
								BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									$scope.patientList = [];
									
									if(response.data.listObject.length == 0){
										growl.warning("No Result Found!",{
											title : ERROR_MSG
										});
									}
									else{
									$scope.patientListTo = [];
									angular.forEach(response.data.listObject,function(value,key)
											{
										  		if(value.registrationTypeId != 5 && value.registrationTypeId != 4)
										  			{
										  			$scope.patientListTo.push(value);
										  			
										  			
										  	}
										  		
											});
									}
									
								});
								
							}
							
							
							$scope.clearFields = function(){
								
								$scope.patientSearchObj = {
										"uhIdNumber"      : "",
										"patientName"     : "",
										"genderId"        : "",
										"mobileNo"        : "",
										"birthDate"       : "",
										"identificationNo":"",
										"patientType"	  :"1",
										"userId" 		  :"",
										"username"        :"",
										"unitId"          :$scope.unitId,
										"organizationId"  :$scope.organizationId,
										"fromPatientId " :"",
										"toPatientId " :"",
										"requestTouserId" :"",
										"username" :"",
										"toPatientName" :"",
										
										
										
								}
							}
							
							$scope.initPatientSearchObj();
							$scope.cancelAbscondedPatient=function(){
								$rootScope.stopLoader();
								$scope.initPatientSearchObj();
							}
							
				
						
							//select TO radio button 
							$scope.getToPatientDetailsById = function(patientId){
								$rootScope.startLoader();
								$scope.patientDetailsList = [];
								$scope.patientDetailsTo = {}
								$scope.toPatientId = patientId;
								
								/*var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$scope.billSearch = {patientId:"",orgId:$scope.organizationId,unitId:$scope.unitId};
										$scope.patientDetailsList = response.data.listObject;
										$scope.patientDetailsTo = response.data.listObject[0];
										$scope.patientDetailsTo.patientName = $scope.patientDetailsTo.firstName + " " + $scope.patientDetailsTo.lastName;
										$scope.patientDetailsTo.genderCode = $scope.patientDetailsTo.genderName.charAt(0).toUpperCase();
										$scope.patientDetailsTo.age = $scope.patientDetailsTo.ageString
										$scope.patientDetailsTo.mobileNo = $scope.patientDetailsTo.mobileNumber;
										$scope.billSearch.patientId = $scope.patientDetailsTo.patientId; 
										$('.patienttSearchResult').toggle();
								
							
									}
								});*/
						
								
								$rootScope.stopLoader();
							}
							//for select user dropdown
							$scope.getUHIDUseData=[];
							$scope.getUHIDUserList = function(){
							
								var data={
											unitId : $scope.unitId,
											organizationId : $scope.organizationId
											
											}
								
								 var URI = BASE_URL + ROOT_URL + "/api/global/unit/getUHIDUserList";	 
									BillingGenericService .serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response){
											if (response.data.status == "success"){
												$scope.getUHIDUseData = response.data.listObject;
												
												
											}
											
											});
								
							}
							$scope.getUHIDUserList();
							
							//for send request 
						/*	 $scope.isDisabled = false;*/
							 
							$scope.sendRequest=function(){
								
								
								 if(($scope.fromPatientId == undefined || $scope.fromPatientId == null || $scope.fromPatientId == "" )&&(
										 $scope.toPatientId == undefined || $scope.toPatientId == null || $scope.toPatientId == "") &&(
										 $scope.requestTouserId == undefined || $scope.requestTouserId == null|| $scope.requestTouserId == "")){
									 growl.error(" Please select from patient"
										
										);
									 growl.error(" Please select To patient"
												
										);
									 growl.error("Please select user");
							        	return false;
									}
									
								 else if($scope.fromPatientId == undefined || $scope.fromPatientId == null || $scope.fromPatientId == ""){
									 growl.error(" Please select from patient"
										);
							        	return false;
									}
									
								 else if($scope.toPatientId == undefined || $scope.toPatientId == null || $scope.toPatientId == ""){
									 growl.error(" Please select To patient"
										);
							        	return false;
									}
								 
								 else if($scope.requestTouserId == undefined ||$scope.requestTouserId == null|| $scope.requestTouserId == ""){
										//$scope.tab=1;
									
										growl.error("Please select user");
										$rootScope.stopLoader();
										return false;
									}
								 
								 var data ={
										 fromPatientId : $scope.fromPatientId,
										 toPatientId   : $scope.toPatientId,
									//	 requestDate   : $scope.requestDate.userId,
										 requestFrom   : $scope.userId,
										 unitId 	   : $scope.unitId,
										 organizationId: $scope.organizationId,
										 requestTo 	   : $scope.requestTouserId.userId,
										 requestType : 16
								 }
								 /*console.log("data",data);
								 return false;*/
								
								 $rootScope.startLoader();
								 var URI = BASE_URL + ROOT_URL + "/api/global/unit/saveUHIDMergeMaster";	 
									BillingGenericService .serviceAction("POST", URI, data)
									.then(
											function(response){
											if (response.data.status == "success"){
												 growl.success(" Successfully sent ..!!");
												$scope.initPatientSearchObj();
												$scope.patientSearchObj ={};
												$scope.requestTouserId.userId ="";
												$scope.patientListTo = [];
												$scope.patientListFrom = [];
											}
											$rootScope.stopLoader();
										});
									
									/* $scope.isDisabled = true;*/
									
									$state.reload();
							}
							//send request end
						
					
					} ]);