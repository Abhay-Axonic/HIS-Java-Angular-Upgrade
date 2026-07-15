angular
		.module('myApp')
		.controller(
				'OTreservationController',
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
						'BillingGenericService',
						'$timeout',
						'$stateParams',
						'$filter',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$timeout,$stateParams,$filter) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.regiType = 2;
							$scope.visitTypeId = null;
							$scope.oldDate = "";
							$scope.oldTime = "";
							$scope.rescheduleOrNot = 0; // 0 for scheduling  and 1 for Re-Scheduling 
							$scope.identificationList = [];
							$scope.prefixList = [];
							$scope.genderList = [];
							$scope.equipcheckList = [];
							$scope.staffcheckList = [];
							$scope.OTTObj = {};
							$scope.ph_numbr = /^\+?\d{10}$/;
					        $scope.eml_add = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/;
					        $scope.dt = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
					        $scope.commonObj = $stateParams.commonObj;
					        $scope.saveProReqId = 0;
					        $scope.getSaveProceudreFlag = false;
					        $scope.editOtReservationflag = true;
							if($scope.commonObj.scheduleDate!= undefined && $scope.commonObj.scheduleDate!= null){
								 $scope.saveProReqId = $scope.commonObj.proReqId;
								 $scope.editOtReservationflag = false;
							}
					        $scope.hidePatientDetails = false;
					        console.log("$scope.commonObj",$scope.commonObj);
					        $scope.initPatientObj = function(){
					        	 $scope.patientObj = {
											prefixId : '',
											firstName : '',
											middleName : '',
											lastName : '',
											genderId : '',
											email : '',
											mobileNumber : '',
											dob : '',
											createdDate : $scope.dt,
											updatedDate : $scope.dt,
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											updatedBy : $scope.userId,
											createdBy : $scope.userId,
											status : 'A',
											registrationTypeId:5, //for Pre-Registration Patient
											patientId:0,
											remark:'',
											patientCategoryId:1
										};	
					        	 	$scope.newAge="";
					        	 	$scope.age = '';
					        	 
					        	 	$scope.changeOfTime = "";
					        	 	$scope.changeOfDate = "";
					        	 	 
					        	 	//Object For Patient Search
										$scope.patientSearchObj = {
											"uhidNumber" : "",
											"patientName" : "",
											"genderId" : "",
											"mobileNumber" : "",
											"dob" : "",
											"identificationNumber" : "",
											"identificationTypeId" : "",
											"unitId" : $scope.unitId,
											"organizationId" : $scope.organizationId,
											"registrationTypeIdList" : [1,2,3,4,6]
										}
										 $scope.patientDetails = {};
										$scope.patientDetails.isFromOtCompletion = true;
										
									$scope.staffObj = {};
									$scope.roleObj = {};
									$scope.staffList = [];
									$scope.newProcList = [];
									$scope.procedureOrderList = [];
							
									$scope.otList = [];
									$scope.otAddedList = [];
									$scope.otAddedStaffDetailsList = [];
									$scope.otStaffDetailsList = [];
									$scope.equipmentDetailList = [];
									$scope.equipmentBatchList = [];
									
									$scope.procedureOTScheduleList = [];
									$scope.procedureStaffScheduleList = [];
									$scope.procedureEquipmentScheduleList = [];
									$scope.patientList = [];
									//$scope.staffList1 = [];
									$scope.datepicker="";
					        }
					       
					        $scope.initPatientObj();
							
					        if($scope.commonObj.scheduleDate!=undefined){
					        	 if( $scope.commonObj.otReservationId!=null || $scope.commonObj.otReservationId!=undefined){
					        		 $scope.hidePatientDetails = true;
							        }
					        	 if($scope.commonObj.patientId!=null || $scope.commonObj.patientId!=undefined){
				                       /* $scope.patientDetails.patientName  = $scope.commonObj.prefixCode+" "+$scope.commonObj.patientName;
				                        $scope.patientDetails.age = $scope.commonObj.age
				                        $scope.patientDetails.uhIdNumber = $scope.commonObj.uhidNumber;
				                        $scope.patientDetails.birthDate = $scope.commonObj.dob;
				                        $scope.patientDetails.genderCode = $scope.commonObj.genderCode;*/
					        		 	$scope.patientDetails.patientProfile = $scope.commonObj.imagePath;
					        			$scope.patientDetails.genderId = $scope.commonObj.genderId;
					        		 	$scope.patientDetails.patientName = $scope.commonObj.patientName;
										$scope.patientDetails.genderCode = $scope.commonObj.genderCode;
										$scope.patientDetails.age = $scope.commonObj.age;
										$scope.patientDetails.uhIdNumber = $scope.commonObj.uhidNumber;
										$scope.patientDetails.birthDate = $scope.commonObj.dob;
										$scope.patientDetails.dateOfAdmission = $scope.commonObj.doa;
										$scope.patientDetails.patientDischargeDate = $scope.commonObj.pdd;
										$scope.patientDetails.bedNumber = $scope.commonObj.bedNumber;
										$scope.patientDetails.doctorFullName = $scope.commonObj.doctorName;
										$scope.patientDetails.wardName = $scope.commonObj.wardName;
										$scope.patientDetails.visitTypeId = $scope.commonObj.visitTypeId;
										$scope.patientDetails.isFromOtCompletion = true;
										if($scope.patientDetails.visitTypeId!=1){
											var dateDoa = moment($scope.patientDetails.dateOfAdmission, 'DD/MM/YYYY');
											var datePdd = moment($scope.patientDetails.patientDischargeDate, 'DD/MM/YYYY');
											$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
										} 
					        		 /*$scope.uhidNum = $scope.common.uhidNumber;
										$scope.dateOfAdmission =  $scope.common.doa;
										$scope.patientDischargeDate = $scope.common.pdd;
										$scope.bedNumber =  $scope.common.bedNumber;
										$scope.doctorFullName = $scope.common.doctorName;*/
					        	 }
					        	 
					        }
					       
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							$scope.minAge = cookieObjectAppConfig.minAge;
							$scope.maxAge = cookieObjectAppConfig.maxAge;
							
							$scope.dateOptions = {
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
							
							
							$scope.datepickerDob = {
									opened : false
								};
							
							$scope.datepickerOpen = {
									opened : false
								};
							
							$scope.openDatePicker = function() {
								$scope.datepickerDob.opened = true;
							};
							
							$scope.dateOptions1 = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
							
							$scope.datepickerOpen1 = {
									opened : false
								};
							
							$scope.openDatepicker1 = function() {
								$scope.datepickerOpen1.opened = true;
							};
							
						  
							
							var data = {};
							var URI = BASE_URL + ROOT_URL + GETACTIVEPREFIXLIST;
							BillingGenericService.serviceAction(METHOD_GET, URI, data, NOTIFICATION_MSG_STATUS_FALSE) .then(
								function(response) {
									console.log(response);
									$scope.prefixList = [];
									if (response.data.status == "success")
										$scope.prefixList = response.data.listObject;
								});
							
							var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
							BillingGenericService .serviceAction(METHOD_GET, URI, data, NOTIFICATION_MSG_STATUS_FALSE) .then(
									function(response) {
										console.log(response);
										$scope.genderList = [];
										if (response.data.status == "success")
											$scope.genderList = response.data.listObject;
									});
							
							

							//For Get Active Gender List
							$scope.getSexMasterList = function() {
								var URI = BASE_URL + ROOT_URL
										+ GETACTIVEGENDERLIST;
								BillingGenericService
										.serviceAction(METHOD_GET, URI, "",
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													$scope.sexMasterList = response.data.listObject;
												});
							}
							$scope.getSexMasterList();
							
							
							var URI = BASE_URL + ROOT_URL+ GETACTIVEIDENTIFICATION;
							BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(
							function(response) {
								console.log(response);
								$scope.identificationList = [];
								if (response.data.status == "success")
									$scope.identificationList = response.data.listObject;
							});

					
					
					$scope.calculateAge= function()
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
					
					$scope.myFunct = function(age) {
						if(age>=$scope.minAge && age <= $scope.maxAge){
							 if($('#datepicker').val()!=""){
									/*var currentYear = (new Date()).getFullYear();
								    var finalYear = currentYear - age;*/
								 var dt = moment($scope.datepicker).format('MM/DD/YYYY');
									var newAgeE = $rootScope.getAge(dt);
									var tArr = dt.split("/");
                                        var m = tArr[0];
                                         var d=tArr[1];
									if(newAgeE != age)
										{
										var currentYear = (new Date()).getFullYear();
									    var finalYear = currentYear - age;
									     var mon=$scope.ageMonth
                                          var dat=$scope.ageDay
                                          $scope.datepicker = new Date(m +'/' +d +'/'  +finalYear);
										}
								    
								}else{
									var currentYear = (new Date()).getFullYear();
								    var finalYear = currentYear - age;
								    $scope.datepicker = new Date(m +'/' +d +'/'  +finalYear);
								}
						}else{
							$scope.age = "";
							growl.warning("Age should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
								title : "warning"
								});
						}
						  
					}
					
					$scope.selectGender = function(id)
					{
						if(id > 0){
						var genId = $.grep($scope.prefixList, function (doc) {
		    	            return doc.prefixId == id;
		    	        })[0].id;
						
						$scope.patientObj.genderId = genId;
						}
						else
							{
							$scope.patientObj.prefixId = '';
							$scope.patientObj.genderId = '';
							}
						
					}
					
					$scope.staffList1 = [];
					$scope.getOTIMSStaffList = function(){
						$scope.startLoader();
						 $scope.staffList1 = [];
					
							var data = {
									orgId:$scope.organizationId, 
									unitId:$scope.unitId
							};
							
							var URI = BASE_URL + ROOT_URL + "/global/otims/getOTIMSStaffList";
							console.log("URI", URI);
							GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
								$scope.staffList1 = response.data.listObject;
								$scope.stopLoader();
							});
					}
					
					$scope.getOTIMSStaffList();
					
					$scope.equipmentAssetAndBatchList = [];
					$scope.getEquipmentTypeList = function () {
						var URI = BASE_URL + ROOT_URL + "/api/otims/OTReservation/getEquipmentTyoeAndAssetItem/"+$scope.organizationId+"/"+$scope.unitId; //2 for Fixed asset type
						BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							$scope.equipmentAssetAndBatchList = response.data.listObject;
							console.log("$scope.equipmentTypeList",$scope.equipmentAssetAndBatchList);
							
						});
					   }
					
					$scope.getEquipmentTypeList();
					
					//code for registration field configuration start
					
					
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
				
		//getting default field configuration start
					
					$scope.specialityList = [];
					 $scope.getSpecialityList = function () {
						var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
						var obj = { organizationId: $scope.organizationId, unitId: $scope.unitId };
						BillingGenericService.serviceAction(METHOD_POST, URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function (response) {
							if (response.data.status == "success")
								$scope.specialityList = [];
								$scope.specialityList.push({specialityId:0,specialityName:"Select Speciality"});
								$scope.specialityList = $scope.specialityList.concat(response.data.listObject);
						});
					}
					
					 $scope.getSpecialityList();
					

					
							//Function For Search Patient
							$rootScope.searchPatient = function(patientsearchObj) {
								//alert();
								if($scope.patientSearchObj.uhidNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNumber == '' && $('#datepickerSearch').val() == '' && $scope.patientSearchObj.identificationTypeId == '' && $scope.patientSearchObj.identificationNumber == '')
								{
									growl.error(SearchFilter_REQUIRED,{title : "WARNING!"
									});
									return false;
								}
									
								else{
										//alert("else AlERT");
									}
								patientsearchObj.dob =	$('#datepickerSearch').val()!="" && $('#datepickerSearch').val()!=undefined ? $rootScope.getChangedFormatedDateShalse($('#datepickerSearch').val()) : "";
								var URI = BASE_URL + ROOT_URL
										+ GETTEMPORYPATIENTFORPREREGISTRATION;
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												patientsearchObj,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													$scope.patientList = response.data.listObject;
													console.log("PatientList",$scope.patientList);
													if($scope.patientList.length == 0){
														growl.warning("No Result Found!",{
															title : ERROR_MSG
														});
													}
												});
							}
					
                    //Function For Selected Patient assign to patient Details Object
                    $scope.getPatientDetailsByIndex = function(index) {
                        
                        console.log("selected Patient",$scope.patientList[index]);
                        $scope.patientDetails.patientName  =$scope.patientList[index].prefixCode+" "+$scope.patientList[index].firstName+" "+
                        $scope.patientList[index].middleName+" "+$scope.patientList[index].lastName;
                        $scope.patientDetails.age = ($scope.patientList[index].ageString);
                        $scope.patientDetails.uhIdNumber = ($scope.patientList[index].uhidNumber);
                        $scope.patientDetails.birthDate = ($scope.patientList[index].dob);
                        $scope.patientDetails.genderCode = ($scope.patientList[index].genderCode);
						$scope.patientDetails.dateOfAdmission = $scope.patientList[index].doa;
						$scope.patientDetails.patientDischargeDate =  $scope.patientList[index].pdd;
						$scope.patientDetails.bedNumber =  $scope.patientList[index].bedNumber;
						$scope.patientDetails.doctorFullName =  $scope.patientList[index].doctorName;
						$scope.patientDetails.wardName =  $scope.patientList[index].wardName;
						$scope.patientDetails.visitTypeId =  $scope.patientList[index].visitTypeId;
					 	$scope.patientDetails.patientProfile =$scope.patientList[index].imagePath;
	        			$scope.patientDetails.genderId = $scope.patientList[index].genderId;
						$scope.patientDetails.isFromOtCompletion = true;
						if($scope.patientDetails.visitTypeId!=1){
							var dateDoa = moment($scope.patientDetails.dateOfAdmission, 'DD/MM/YYYY');
							var datePdd = moment($scope.patientDetails.patientDischargeDate, 'DD/MM/YYYY');
							$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
						} 
                      /*  $scope.uhidNum = $scope.patientList[index].uhidNumber;
						$scope.dateOfAdmission =  $scope.common.doa;
						$scope.patientDischargeDate = $scope.common.pdd;
						$scope.bedNumber =  $scope.common.bedNumber;
						$scope.doctorFullName = $scope.common.doctorName;*/
                        
                        $scope.patientObj.prefixId = $scope.patientList[index].prefixId;
                        $scope.patientObj.firstName = $scope.patientList[index].firstName;
                        $scope.patientObj.middleName = $scope.patientList[index].middleName;
                        $scope.patientObj.lastName = $scope.patientList[index].lastName;
                        $scope.patientObj.genderId = $scope.patientList[index].genderId;
                        $scope.patientObj.email = $scope.patientList[index].email;
                        $scope.patientObj.mobileNumber = $scope.patientList[index].mobileNumber;
                        $scope.patientObj.dob = moment(moment($scope.patientList[index].dob,'DD/MM/YYYY').toDate()).format('DD-MM-YYYY HH:mm:ss');
                     //   $scope.patientObj.dob = $scope.patientList[index].dob; "05/17/1988"
                        $scope.patientObj.registrationTypeId = $scope.patientList[index].registrationTypeId;
                        $scope.patientObj.patientId = $scope.patientList[index].patientId;
                        if($scope.patientList[index].remark!=null){
                        	 $scope.patientObj.remark = $scope.patientList[index].remark;
                        }
                       
                        $scope.patientObj.patientCategoryId = $scope.patientList[index].patientCategoryId;
                    }
	
					
				$scope.newPatientChange = function(){
					 $scope.initPatientObj();
				}

/********************************************** Procedure Details Tab START **************************************************************/
			
				$scope.dateOptionsProc = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : new Date(),
					
				};

				$scope.procedureOpen = {
					opened : false
				};

				$scope.openproc = function() {
					$scope.procedureOpen.opened = true;
				};
				
                // function for initializa clockpicker
                $scope.initClockPicker = function() {

                    $('.clockpicker').clockpicker();
                }

				$scope.setNormalEmergency = function(tabValue) {
					$scope.procedureOrderMaster.urgency = tabValue;
				}
				
				$scope.initProcedureOrder = function() {

					$scope.rate = 0.00;
					$scope.procedureSubSpeciality = {specialityId:0,specialityName:"Select Speciality"};
					$scope.procedureDate = undefined;
                    $scope.preferedTime = "";
					$scope.procedureSearch = '';
					$scope.updateProcCurrentIdx = -1;//edit Saved Procedure flag
					$scope.procedureReqMaster = {
						proReqId : "",
						primaryProcedureId : "",
						patientId : "",
						admissionId : "",
						encounterId : "",
						visitTypeId : "",
						preferedDateTime : "",
						requestDate : "",
						requestBy : $scope.userId,
						procedureStatusId : "",
						otCompletionDateTime : "",
						otCompletionBy : "",
						otBillCompletionDateTime : "",
						otBillCompletionBy : "",
						orgId : $scope.organizationId,
						unitId : $scope.unitId,
						createdBy : $scope.userId,
						createdDate : $scope.dt,
						updatedBy : $scope.userId,
						updatedDate : $scope.dt
					}

					$scope.procedureOrderMaster = angular
							.copy({
								procedureOrderId : "",
								serviceId : "",
								preferedDateTime : "",
								orgId : $scope.organizationId,
								unitId : $scope.unitId,
								patientId : "",
								doctorId : "",
								admissionId : "",
								visitTypeId : "",
								orderDateTime : "",
								procedureStatusId : "3",
								createdBy : $scope.userId,
								createdDate : $scope.dt,
								updatedBy : $scope.userId,
								updatedDate : $scope.dt,
								isPrimarySecondary : "N",
								amount : "",
								status : "A",
								urgency : "0",
								requestReason : "",
								isIcuRequired : "N",
								isBloodComponentRequired : "N",
								isPacRequired : "N",
								parentProcedureOrderId : "",
								uhidNumber : "",
								patientName : "",
								dob : "",
								cptCode : "",
								service : "",
								procedureType : "",
								fromDate : "",
								toDate : "",
								genderCode : "",
								age : "",
								duration : "",
								paymentEntitlementId : "",
								patientTypeId : $scope.patientObj.patientCategoryId,
								billingBedCategoryId : "",
								serviceCode : "",
								speciality : "",
								proReqId : $scope.commonObj.proReqId!=undefined ? $scope.commonObj.proReqId:0,
								specialityId : "",
								specialityName : "",
								procedureName : "",
								procedureReqMasterDto : {},
								listCalenderMapperDto : []
							});

					/*$scope.calandarSlotMppr = angular
							.copy({
								surgicalCalenderMpprId : "",
								specialityId : $stateParams.patientDetailsObj.specialityId != undefined ? $stateParams.patientDetailsObj.specialityId
										: "",
								doctorId : $stateParams.patientDetailsObj.doctorId,
								procedureOrderId : "",
								surgicalCalenderId : "",
								patientId : $scope.patientObj.patientId,
								encounterId : '',
								admissionId : null,
								status : "A",
								createdBy : $scope.userId,
								createdDate : "",
								updatedBy : $scope.userId,
								updatedDate : "",
								organisationId : $scope.organizationId,
								unitId : $scope.unitId,
								isDeleted : "N"
							});*/
					
					$scope.consentDetailList = [];
					$scope.checklistDetailList = [];
					$scope.staffDetailList = [];
					$scope.finalServiceList = [];
					$scope.equipmentDetailList = [];
					$scope.medicationList= [];
					$scope.implantList = [];
					//$scope.addServicesList = [];
					$scope.operationTheatreTypeId = 0;

				}

				
				$scope.procedureSequenceMasterList = [
					{
						id : 1,
						sequenceName : "Primary"
					},
					{
						id : 2,
						sequenceName : "Secondary"
					},
					{
						id : 3,
						sequenceName : "Tertiary"
					},
					{
						id : 4,
						sequenceName : "Quaternary"
					},
					{
						id : 5,
						sequenceName : "Quinary"
					}
					];
				
				$scope.procedureSequenceList = [
					{
						id : 0,
						sequenceName : " "
					}
					];
				
				$scope.procedureOrderedSequenceList = [
					{
						id : 0,
						sequenceName : " "
					}
					];
				
				$scope.procedureSubSpeciality = {specialityId:0,specialityName:"Select Speciality"};
				$scope.selectSpeciality = function(selectedSubServiceType) {
					//var selectedSubServiceType = JSON.parse($scope.procedureSubSpeciality);
					$scope.procedureOrderMaster.specialityName = selectedSubServiceType.specialityName;
					$scope.procedureOrderMaster.specialityId = selectedSubServiceType.specialityId;
					console.log("$scope.procedureOrderMaster.specialityName",$scope.procedureOrderMaster.specialityName);
				}
				$scope.procedureSearch = '';
				$scope.procedureDate;
				$scope.searchProcedures = function(searchKeyword) {
					$rootScope.startLoader();
					$scope.procedureMasterList = [];
					var selectedSubServiceType = $scope.procedureSubSpeciality;

					 $scope.regex = /^[A-Za-z0-9 ]+$/;
					 
						var isValid = $scope.regex.test(searchKeyword);
						if (!isValid) {
							
							$scope.procedureSearch = '';
							$rootScope.stopLoader();
							return false;
							
						} else {
					
					
						/*	var URI = BASE_URL + ROOT_URL
									+ "/global/procedureSearch/"
									+ searchKeyword + "/unit/"
									+ $scope.unitId + "/org/"
									+ $scope.organizationId
									+ "/speciality/"
									+ selectedSubServiceType.specialityId*/
							var URI = BASE_URL + ROOT_URL + "/api/procedure/searchProcedureByNameOrCode";
							var data = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId,
									serviceName : searchKeyword
							};
							if(selectedSubServiceType.specialityId!=undefined && selectedSubServiceType.specialityId!=null 
									&& selectedSubServiceType.specialityId!="" && selectedSubServiceType.specialityId!=0){
								data.specialityId = selectedSubServiceType.specialityId;
							}
							console.log('URI', URI);
							return BillingGenericService
									.serviceAction(METHOD_POST, URI, data)
									.then(
											function(response) {
												if (response.data.status == "success") {
													
													if(response.data.listObject != null){
														$scope.procedureMasterList = [] ;
														angular.forEach(response.data.listObject,function(value, key) {
														
															if(value.procedureTypeId == 3 && value.procedureConfigurationId!=null){
																$scope.procedureMasterList.push(value);
															}
															
														});
														
													}
													$rootScope.stopLoader();
												}
												$rootScope.stopLoader();
												return $scope.procedureMasterList;
											});
						}
				}


				
/****************************************************************  Patient Education Tab START      *****************************************************************/
				$scope.patientEduObj = {
						patientEducationTempale:"",
						procedureOrderPatientEducationId:null,
						organizationId:$scope.organizationId,
						unitId:$scope.unitId,
						createdBy:$scope.userId,
						updatedBy:$scope.userId,
						status:'A',
						createdDate:$scope.dt,
						updatedDate:$scope.dt
				};
				
/****************************************************************  Patient Education Tab END      *****************************************************************/

/****************************************************************  Select Procedure Start *****************************************************************/
				$scope.currentIndex = "";//added procedure Index on Info icon(before proc save)
				$scope.consentDetailList = [];
				$scope.checklistDetailList = [];
				$scope.staffDetailList = [];
				$scope.finalServiceList = [];
				$scope.equipmentDetailList = [];
				$scope.addServicesList = [];
				$scope.totalDuration = 0;
				$scope.selectProcedureName = function($item,$model, $label) {
					
					$rootScope.startLoader();
					$scope.rate = 0.00;
					$scope.totalDuration = $scope.totalDuration + parseInt($item.procedureDuration);
					$scope.procedureOrderMaster.serviceId = $item.serviceMasterId;
					$scope.procedureOrderMaster.procedureName = $item.serviceStandardName;
					$scope.procedureDuration = $item.procedureDuration;
					$scope.procedureSubSpeciality.specialityId = $item.specialityId;
					$scope.procedureSubSpeciality.specialityName = $item.specialityName;
					$scope.procedureOrderMaster.specialityName =  $scope.procedureSubSpeciality.specialityName;
                    $scope.procedureOrderMaster.specialityId =  $scope.procedureSubSpeciality.specialityId;
			//		$scope.procedures = ""+$item.serviceStandardName;
					$rootScope.stopLoader();
		
					$scope.getProcedureTemplate($item.serviceMasterId);
					
						
				}
				
				//get procedure configuration details by service id on procedure type head search
			
				$scope.getProcedureTemplate = function(serviceMasterId){
					$rootScope.startLoader();
					
					var URI = BASE_URL + ROOT_URL + "/api/otims/getProcedureConfigTemplate";
					var obj = {
							orgId:$scope.organizationId, 
							unitId:$scope.unitId,
							serviceId:serviceMasterId
							};
					BillingGenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
						if (response.data.status == "success"){
	
							$scope.procedureTemplateObj = response.data.object;
							
							$scope.procedureOrderMaster.theatreTypeName =  $scope.procedureTemplateObj.theatreTypeName;
							$scope.procedureOrderMaster.procedureType = $scope.procedureTemplateObj.procedureType
							
						//	$scope.procedureTypes = ""+$scope.procedureTemplateObj.procedureType;
							//$scope.OTTypes = ""+$scope.procedureTemplateObj.theatreTypeName;
							
							$scope.procedureOrderMaster.operationTheatreTypeId = $scope.procedureTemplateObj.operationTheatreTypeId == null?0:$scope.procedureTemplateObj.operationTheatreTypeId;
							$scope.consentDetailList = response.data.object.consentDetailList;
							$scope.checklistDetailList = response.data.object.checklistDetailList;
							$scope.staffDetailList = response.data.object.staffDetailList;
							$scope.finalServiceList = response.data.object.serviceList;
							$scope.equipmentDetailList = response.data.object.equipmentDetailList;
							if(response.data.object.patientEduObj!=null){
								$scope.patientEduObj.patientEducationTempale = response.data.object.patientEduObj.patientEducationTempale;
								$scope.patientEduObj.procedureConfigPatientEducationId = response.data.object.patientEduObj.procedureConfigPatientEducationId;
							}
							console.log("$scope.consentDetailListsadvfbghjkl;",$scope.consentDetailList);
							
							$scope.medicationList=[];
							$scope.implantList=[];
							var date = new Date();
						
							
							//split implant and medications from inventory list
							angular.forEach(response.data.object.inventoryItemList,function(value, key) {
								
								if(value.isImplant == true){
									var data ={}
									data.itemId = value.itemId;
									data.itemName = value.itemName;
									data.qty = value.qty;
									data.isItemOrKit = value.isItemOrKit;
									data.admissionId = null;
									data.patientId = $scope.patientObj.patientId;
									data.organizationId = $scope.organizationId;
									data.unitId = $scope.unitId;
									data.status = "A";
									data.createdBy = $scope.userId;
									data.createdDate = $scope.dt;
									data.updatedBy = $scope.userId;
									data.updatedDate = $scope.dt;
									$scope.implantList.push(data);
								}else{
									
									var data ={}
									data.itemId = value.itemId;
									data.itemName = value.itemName;
									data.qty = value.qty;
									data.isItemOrKit = value.isItemOrKit;
									data.admissionId = null;
									data.patientId = $scope.patientObj.patientId;
									data.organizationId = $scope.organizationId;
									data.unitId = $scope.unitId;
									data.status = "A";
									data.createdBy = $scope.userId;
									data.createdDate = $scope.dt;
									data.updatedBy = $scope.userId;
									data.updatedDate = $scope.dt;
									$scope.medicationList.push(data);
								}
							});
						} 
						$rootScope.stopLoader();
					});
				}
				
/****************************************************************  Select Procedure End *****************************************************************/
	
					
				
					
/****************************************************************  add Procedure Order Start *****************************************************************/		
					
				
				$scope.procedureRequestMaster ={};	//temp obj for edit procedure (for isPrimarySecondary)
				$scope.procedureOrderList = [];
				$scope.preferedProcTime = "";
				$scope.preferedProcDate = "";
				$scope.specialityName  = "";
				$scope.specialityId = "";
				if($scope.commonObj.scheduleDate!=undefined){
					$scope.preferedProcTime = $scope.commonObj.preferTime;
					//$scope.preferedProcDate = $scope.commonObj.scheduleDate;	
				}
				$scope.checkPrimaryPro = false;  //to change procedure  from primary to secondary or secondry to primary
/*				$scope.addProcedureOrder = function() {
					$scope.procedureRequestMaster ={};	
					var checkProcedureExist=false; // to check procedure already exist or not 
					var primeCheckflag = false;  // to make procedure defult primary  add into procedureOrderList is greater than 0 length location 
					if($scope.procedureOrderList.length>0){
					angular.forEach($scope.procedureOrderList,function(value,index){
					
						 var oldpreferedDate = moment(moment(value.preferedDateTime,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
                        var oldprocedureDate = moment($scope.procedureDate).format('YYYY-MM-DD');

						if(value.serviceId == $scope.procedureOrderMaster.serviceId && parseInt(value.procedureStatusId) == 1 && $scope.updateProcCurrentIdx != index){
							
							if($scope.procedureOrderMaster.proReqId == 0){
								checkProcedureExist=true;
							}
							
						}
						if(value.isPrimarySecondary == 'Y'){
							$scope.preferedProcTime =  value.preferedTime ;
							$scope.preferedProcDate =  value.preferedDateTime;
							$scope.specialityName  = value.specialityName;
							$scope.specialityId = value.specialityId;
						}
						
						
						if(value.proReqId == 0 && value.isPrimarySecondary == 'Y' && oldpreferedDate == oldprocedureDate){
							 primeCheckflag = true;
						}
					});
					}
					
					if($scope.orderedProcedureList.length>0){
						angular.forEach($scope.orderedProcedureList,function(value,index){
							
							if(value.serviceId == $scope.procedureOrderMaster.serviceId && parseInt(value.procedureStatusId) == 1 && $scope.updateProcCurrentIdx != index){
								
								checkProcedureExist=true;
							}
						});
						}
					
					if(checkProcedureExist){
						
						growl.warning("Procedure Already Exist", {
							title : 'warning!'
						});
						
						$scope.initProcedureOrder();
						$rootScope.stopLoader();
						
					}else{ if($scope.procedureSearch=="" || $scope.procedureSearch==undefined){
							
							growl.warning("Please select Procedure", {
								title : 'warning!'
							});
							$rootScope.stopLoader();
						}else if($scope.procedureDate==undefined){
							
							growl.warning("Please select Procedure Date", {
								title : 'warning!'
							});
							$rootScope.stopLoader();
							
						}else if($scope.preferedTime==undefined || $scope.preferedTime==""){
                            growl.warning("Please select Procedure Time", {
                                title : 'warning!'
                            });
                            $rootScope.stopLoader();
                            return false;
						}
						else{
							
							var updateProcCurtIdx = $scope.updateProcCurrentIdx;
							if(updateProcCurtIdx >= 0){
								$scope.orderedProcedureList.splice(updateProcCurtIdx, 1);
							}
						var procedureOrderMaster = angular.copy($scope.procedureOrderMaster);
						procedureOrderMaster.patientId = $scope.patientObj.patientId;
						procedureOrderMaster.doctorId = null;
						procedureOrderMaster.visitTypeId = $scope.visitTypeId;
						procedureOrderMaster.admissionId = null;
						procedureOrderMaster.requestReason = $scope.procedureOrderMaster.requestReason;
						procedureOrderMaster.amount = $scope.rate;
						if(procedureOrderMaster.isPrimarySecondary == 'Y'){
							$scope.preferedProcTime =  procedureOrderMaster.preferedTime ;
							$scope.preferedProcDate =  procedureOrderMaster.preferedDateTime;
							$scope.specialityName  = procedureOrderMaster.specialityName;
							$scope.specialityId = procedureOrderMaster.specialityId;
						}
						
						
						if($scope.patientObj.email==""){
							procedureOrderMaster.email = null;
						}else{
							procedureOrderMaster.email = $scope.patientObj.email;
						}
						
					//	procedureOrderMaster.preferedDateTime = $scope.procedureDate;
                        procedureOrderMaster.preferedDateTime = moment($scope.procedureDate).format('DD-MM-YYYY');
                        procedureOrderMaster.preferedTime = $scope.preferedTime;

						if($scope.procedureOrderList.length == 0){
							
							//to edit  saved procedure with primary at 0th index
							if($scope.procedureOrderMaster.proReqId != 0 && procedureOrderMaster.isPrimarySecondary == 'Y'){  
								procedureOrderMaster.isPrimarySecondary = 'Y';
								var procedureReqMaster = angular.copy($scope.procedureReqMaster);
								procedureReqMaster.patientId = $scope.patientObj.patientId;
								procedureReqMaster.visitTypeId = $scope.visitTypeId;
								procedureReqMaster.admissionId = null;
								procedureReqMaster.preferedDateTime = moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
								procedureOrderMaster.procedureReqMasterDto = procedureReqMaster;
								
								$scope.procedureRequestMaster = angular.copy(procedureReqMaster);
								
                                if(procedureOrderMaster.isPrimarySecondary == 'Y'){
                                    $scope.preferedProcTime =  procedureOrderMaster.preferedTime ;
                                    $scope.preferedProcDate =  procedureOrderMaster.preferedDateTime;
                                    $scope.specialityName  = procedureOrderMaster.specialityName;
                                    $scope.specialityId = procedureOrderMaster.specialityId;
                                }

								
							}else if($scope.procedureOrderMaster.proReqId != 0 && procedureOrderMaster.isPrimarySecondary == 'N'){ //to edit  saved procedure with secondary at 0th index
								var procedureReqMaster = angular.copy($scope.procedureReqMaster);
								procedureReqMaster.patientId = $scope.patientObj.patientId;
								procedureReqMaster.visitTypeId = $scope.visitTypeId;
								procedureReqMaster.admissionId = null;
								procedureReqMaster.preferedDateTime = moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
								$scope.procedureRequestMaster = angular.copy(procedureReqMaster);
							}
							if($scope.procedureOrderMaster.proReqId == 0){ //to add  new procedure with primary at 0th index
								$scope.checkPrimaryPro = true;
								procedureOrderMaster.isPrimarySecondary = 'Y';
								var procedureReqMaster = angular.copy($scope.procedureReqMaster);
								procedureReqMaster.patientId = $scope.patientObj.patientId;
								procedureReqMaster.visitTypeId = $scope.visitTypeId;
								procedureReqMaster.admissionId = null;
								procedureReqMaster.preferedDateTime = moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
								procedureOrderMaster.procedureReqMasterDto = procedureReqMaster;
							//	$scope.preferedProcTime =  procedureOrderMaster.preferedTime ;
								//$scope.preferedProcDate =  procedureOrderMaster.preferedDateTime;
								$scope.procedureRequestMaster = angular.copy(procedureReqMaster);
							//	$scope.specialityName  = procedureOrderMaster.specialityName;
							//	$scope.specialityId = procedureOrderMaster.specialityId;
								
                                if(procedureOrderMaster.isPrimarySecondary == 'Y'){
                                    $scope.preferedProcTime =  procedureOrderMaster.preferedTime ;
                                    $scope.preferedProcDate =  procedureOrderMaster.preferedDateTime;
                                    $scope.specialityName  = procedureOrderMaster.specialityName;
                                    $scope.specialityId = procedureOrderMaster.specialityId;
                                }

							}
						}else{
							//to edit  saved procedure with primary at 0th index
							if($scope.procedureOrderMaster.proReqId != 0 && procedureOrderMaster.isPrimarySecondary == 'Y'){
								procedureOrderMaster.isPrimarySecondary = 'Y';
								var procedureReqMaster = angular.copy($scope.procedureReqMaster);
								procedureReqMaster.patientId = $scope.patientObj.patientId;
								procedureReqMaster.visitTypeId = $scope.visitTypeId;
								procedureReqMaster.admissionId = null;
								procedureReqMaster.preferedDateTime = moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
								procedureOrderMaster.procedureReqMasterDto = procedureReqMaster;
								
								$scope.procedureRequestMaster = angular.copy(procedureReqMaster);
								
							}else if($scope.procedureOrderMaster.proReqId != 0 && procedureOrderMaster.isPrimarySecondary == 'N'){  //to edit  saved procedure with secondary at 0th index
								var procedureReqMaster = angular.copy($scope.procedureReqMaster);
								procedureReqMaster.patientId = $scope.patientObj.patientId;
								procedureReqMaster.visitTypeId = $scope.visitTypeId;
								procedureReqMaster.admissionId = null;
								procedureReqMaster.preferedDateTime = moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
								$scope.procedureRequestMaster = angular.copy(procedureReqMaster);
							} else if(primeCheckflag == false && $scope.procedureOrderMaster.proReqId == 0){ //to add  new procedure with primary at 1++++ index
								
								procedureOrderMaster.isPrimarySecondary = 'Y';
								var procedureReqMaster = angular.copy($scope.procedureReqMaster);
								procedureReqMaster.patientId = $scope.patientObj.patientId;
								procedureReqMaster.visitTypeId = $scope.visitTypeId;
								procedureReqMaster.admissionId = null;
								procedureReqMaster.preferedDateTime = moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
								procedureOrderMaster.procedureReqMasterDto = procedureReqMaster;
								
								$scope.procedureRequestMaster = angular.copy(procedureReqMaster);
								
                                if(procedureOrderMaster.isPrimarySecondary == 'Y'){
                                    $scope.preferedProcTime =  procedureOrderMaster.preferedTime ;
                                    $scope.preferedProcDate =  procedureOrderMaster.preferedDateTime;
                                    $scope.specialityName  = procedureOrderMaster.specialityName;
                                    $scope.specialityId = procedureOrderMaster.specialityId;
                                }

							}
						}
						procedureOrderMaster.listCalenderMapperDto = $scope.calandarSlotMpprList;
						procedureOrderMaster.implantDetailsList = $scope.implantList;
						procedureOrderMaster.medicationDetailsList = $scope.medicationList;
						procedureOrderMaster.procedureOrderConsentList = $scope.consentDetailList;
						procedureOrderMaster.procedureOrderCheckList = $scope.checklistDetailList;
						procedureOrderMaster.procedureOrderStaffList = $scope.staffDetailList;
						procedureOrderMaster.procedureOrderEquipmentList = $scope.equipmentDetailList;
						procedureOrderMaster.procedureOrderServiceList = $scope.finalServiceList;
						procedureOrderMaster.procedureOrderPatientEducationDto = $scope.patientEduObj;
						
						if($scope.procedureOrderMaster.proReqId == 0 && updateProcCurtIdx >= 0){
							if($scope.procedureOrderMaster.isPrimarySecondary == "Y"){

								var proReqObj = $scope.procedureOrderList[updateProcCurtIdx].procedureReqMasterDto;
								procedureOrderMaster.procedureReqMasterDto = proReqObj;
							}
							
							$scope.procedureOrderList.splice(updateProcCurtIdx, 1);
							$scope.procedureOrderList.push(procedureOrderMaster);
							
						}else{
							
							$scope.procedureOrderList.push(procedureOrderMaster);
						}
					
						$scope.pDate = moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
						
						$scope.initProcedureOrder();
						}		
					}
					
					console.log("$scope.procedureOrderList dfvbjkdfjbvfigbviufbv",$scope.procedureOrderList);
					
				}*/
				
				$scope.addProcedureOrder = function() {
					$scope.procedureRequestMaster ={};	
					var checkProcedureExist=false; // to check procedure already exist or not 
					var primeCheckflag = false;  // to make procedure defult primary  add into procedureOrderList is greater than 0 length location 
					
					
					//check for already Exist in current list
					if($scope.procedureOrderList.length>0){
						angular.forEach($scope.procedureOrderList,function(value,index){
							
							if(value.serviceId == $scope.procedureOrderMaster.serviceId && parseInt(value.procedureStatusId) == 3 && $scope.updateProcCurrentIdx != index){
								
								checkProcedureExist=true;
							}
						});
						}
					
					//check for already Exist in Saved list
					if($scope.orderedProcedureList.length>0){
						angular.forEach($scope.orderedProcedureList,function(value,index){
							
							if(value.serviceId == $scope.procedureOrderMaster.serviceId && parseInt(value.procedureStatusId) == 3 && $scope.updateProcCurrentIdx != index){
								
								checkProcedureExist=true;
							}
						});
						}
					
					if(checkProcedureExist){
						
						growl.warning("Procedure Already Exist", {
							title : 'warning!'
						});
						
						$scope.initProcedureOrder();
						$rootScope.stopLoader();
						
					}else{ 
						if($scope.procedureSearch=="" || $scope.procedureSearch==undefined){
							
							growl.warning("Please select Procedure", {
								title : 'warning!'
							});
							$rootScope.stopLoader();
						}else if($scope.procedureDate==undefined){
							
							growl.warning("Please select Procedure Date", {
								title : 'warning!'
							});
							$rootScope.stopLoader();
							
						}else if($scope.preferedTime==undefined || $scope.preferedTime==""){
                            growl.warning("Please select Procedure Time", {
                                title : 'warning!'
                            });
                            $rootScope.stopLoader();
                            return false;
						}
						else{
																	
						var procedureOrderMaster = angular.copy($scope.procedureOrderMaster);
						procedureOrderMaster.patientId = $scope.patientObj.patientId;
						procedureOrderMaster.doctorId = null;
						procedureOrderMaster.visitTypeId = $scope.visitTypeId;
						procedureOrderMaster.admissionId = null;
						procedureOrderMaster.requestReason = $scope.procedureOrderMaster.requestReason;
						procedureOrderMaster.amount = $scope.rate;
					//	procedureOrderMaster.preferedDateTime = $scope.procedureDate;
                        procedureOrderMaster.preferedDateTime = moment($scope.procedureDate).format('DD-MM-YYYY');
                        procedureOrderMaster.preferedTime = $scope.preferedTime;
                        
                        if($scope.patientObj.email==""){
							procedureOrderMaster.email = null;
						}else{
							procedureOrderMaster.email = $scope.patientObj.email;
						}
                        
						
						procedureOrderMaster.listCalenderMapperDto = $scope.calandarSlotMpprList;
						procedureOrderMaster.implantDetailsList = $scope.implantList;
						procedureOrderMaster.medicationDetailsList = $scope.medicationList;
						procedureOrderMaster.procedureOrderConsentList = $scope.consentDetailList,
						procedureOrderMaster.procedureOrderCheckList = $scope.checklistDetailList,
						procedureOrderMaster.procedureOrderStaffList = $scope.staffDetailList,
						procedureOrderMaster.procedureOrderEquipmentList = $scope.equipmentDetailList,
						procedureOrderMaster.procedureOrderServiceList = $scope.finalServiceList,
						procedureOrderMaster.procedureOrderPatientEducationDto = $scope.patientEduObj
					
						if($scope.updateProcCurrentIdx!=-1 && $scope.updateProcCurrentIdx!=undefined){
							
							if(procedureOrderMaster.proReqId!=null && procedureOrderMaster.proReqId!=0){
								
								if(procedureOrderMaster.procedureSequence == 1){
									var procedureReqMaster = angular.copy($scope.procedureReqMaster);
									procedureReqMaster.patientId = $stateParams.patientDetailsObj.patientId;
									procedureReqMaster.visitTypeId = $stateParams.patientDetailsObj.visitTypeId;
									procedureReqMaster.admissionId = $stateParams.patientDetailsObj.admissionId;
									procedureReqMaster.preferedDateTime =  moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
									procedureOrderMaster.procedureReqMasterDto = procedureReqMaster;
								}
								
								
								$scope.orderedProcedureList[$scope.updateProcCurrentIdx] = angular.copy(procedureOrderMaster);
								$scope.orderedProcedureList[$scope.updateProcCurrentIdx].isEdited = true;
							}else{
								
								if(procedureOrderMaster.procedureSequence == 1){
									var procedureReqMaster = angular.copy($scope.procedureReqMaster);
									procedureReqMaster.patientId = $stateParams.patientDetailsObj.patientId;
									procedureReqMaster.visitTypeId = $stateParams.patientDetailsObj.visitTypeId;
									procedureReqMaster.admissionId = $stateParams.patientDetailsObj.admissionId;
									procedureReqMaster.preferedDateTime =  moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
									procedureOrderMaster.procedureReqMasterDto = procedureReqMaster;
								}
								
								
								$scope.procedureOrderList[$scope.updateProcCurrentIdx] = angular.copy(procedureOrderMaster);
							}
							
						}else{
							
							if(procedureOrderMaster.proReqId!=null && procedureOrderMaster.proReqId!=0){
							//add new procedure into saved request	
								if($scope.orderedProcedureList.length == 0){
									var sequenceObj = $scope.procedureSequenceList.find(obj => {return obj.id == 1});
									procedureOrderMaster.procedureSequence = 1;
									procedureOrderMaster.isPrimarySecondary = 'Y';
									
									var procedureReqMaster = angular.copy($scope.procedureReqMaster);
									procedureReqMaster.patientId = $scope.patientObj.patientId;
									procedureReqMaster.visitTypeId = $scope.visitTypeId;
									procedureReqMaster.admissionId = null;
									procedureReqMaster.preferedDateTime =  moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
									procedureOrderMaster.procedureReqMasterDto = procedureReqMaster;
									
									if(sequenceObj == undefined){
										$scope.procedureSequenceList.push($scope.procedureSequenceMasterList[0]);
									}	
									
									 $scope.preferedProcTime =  procedureOrderMaster.preferedTime ;
	                                 $scope.preferedProcDate =  procedureOrderMaster.preferedDateTime;
								}else{
									procedureOrderMaster.procedureSequence =  $scope.orderedProcedureList.length+1;
									var sequenceObj = $scope.procedureSequenceList.find(obj => {return obj.id == procedureOrderMaster.procedureSequence});
									if(sequenceObj == undefined){
										$scope.procedureSequenceList.push($scope.procedureSequenceMasterList[ $scope.orderedProcedureList.length]);
									}	
								}
								$scope.orderedProcedureList.push(procedureOrderMaster);
							}else{
								if($scope.procedureOrderList.length == 0){
									var sequenceObj = $scope.procedureOrderedSequenceList.find(obj => {return obj.id == 1});
									procedureOrderMaster.procedureSequence = 1;
									procedureOrderMaster.isPrimarySecondary = 'Y';
									
									var procedureReqMaster = angular.copy($scope.procedureReqMaster);
									procedureReqMaster.patientId = $scope.patientObj.patientId;
									procedureReqMaster.visitTypeId = $scope.visitTypeId;
									procedureReqMaster.admissionId = null;
									procedureReqMaster.preferedDateTime =  moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
									procedureOrderMaster.procedureReqMasterDto = procedureReqMaster;
									
									if(sequenceObj == undefined){
										$scope.procedureOrderedSequenceList.push($scope.procedureSequenceMasterList[0]);
									}	
									 $scope.preferedProcTime =  procedureOrderMaster.preferedTime ;
	                                 $scope.preferedProcDate =  procedureOrderMaster.preferedDateTime;
								}else{
									procedureOrderMaster.procedureSequence =  $scope.procedureOrderList.length+1;
									var sequenceObj = $scope.procedureOrderedSequenceList.find(obj => {return obj.id == procedureOrderMaster.procedureSequence});
									if(sequenceObj == undefined){
										$scope.procedureOrderedSequenceList.push($scope.procedureSequenceMasterList[ $scope.procedureOrderList.length]);
									}	
								}
									$scope.procedureOrderList.push(procedureOrderMaster);
							}
						}

						$scope.pDate = $scope.procedureDate;
						$scope.initProcedureOrder();
							
						}		
					}
					console.log("$scope.procedureOrderList dfvbjkdfjbvfigbviufbv",$scope.procedureOrderList);
				}
				
				$scope.orderedProcedureList = [];
				$scope.addNewProc = function(){
					
					var addproFlag = false;
					if($scope.patientDetails.patientName  == undefined ){
						
						 if($scope.patientObj.prefixId =='' || $scope.patientObj.firstName ==''|| $scope.patientObj.genderId ==''){
							 addproFlag = true;
							}
					}
					if(addproFlag == true){
						 growl.warning("Please select Patient Details", {
                             title : 'warning!'
                         });
					}else{
						
						  if($scope.procedureDate==undefined){
		                         growl.warning("Please select Procedure Date", {
		                             title : 'warning!'
		                         });
		                     }else if($scope.preferedTime==undefined || $scope.preferedTime==""){
		                    	 growl.warning("Please select Procedure Time", {
		                    		 title : 'warning!'
		                    	 });
		                    	 $rootScope.stopLoader();
		                    	 return false;
							}else{
								var currTime = moment(new Date()).format('HH:MM');   
								var oldprocedureDate = moment($scope.procedureDate).format('YYYY-MM-DD');
								var todayDates = moment(new Date()).format('YYYY-MM-DD');
							
								if(oldprocedureDate == todayDates && currTime > $scope.preferedTime){
								
									growl.warning("Please select valid Time", {
										title : 'warning!'});
									$rootScope.stopLoader();
									return false;
								
									}else{
										
										if($scope.procedureOrderList.length>0){
											var checkSAmeDate = false;
											    angular.forEach($scope.procedureOrderList, function(value, key) {
											    	var datef = value.preferedDateTime.split(" ");
											    	var finapDate  = datef[0];
											    //	var finalSameDate = finapDate[2]+"-"+finapDate[1]+"-"+finapDate[0];
											    	var finalSameDate = moment(moment(finapDate,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
											    	if(finalSameDate == oldprocedureDate){
											    		checkSAmeDate = true;
											    	}
											    });
											   if(checkSAmeDate == true ){
												   
												   $scope.addProcedureOrder();
												   
											   }else{
												   growl.warning("Please select Same Date.", {
														title : 'warning!'});
													$rootScope.stopLoader();
													return false;
											   } 
											   
										}else{
											
											$scope.addProcedureOrder();
										}
									
				            $rootScope.stopLoader();
				            return false;
									}
							
		                     }
					}
					 
				}
/****************************************************************  add Procedure Order End *****************************************************************/								
				$scope.idxForCancelOrderedProc = "";
                $scope.cancelProcedure = function(index,removeFlag){
                    if(removeFlag == 2){  
                    	
                    	if($scope.orderedProcedureList[index].procedureOrderId==null || $scope.orderedProcedureList[index].procedureOrderId == 0 || $scope.orderedProcedureList[index].procedureOrderId == "")
                    	{
                    		$scope.removeProcedureOder(index,2);
                    	}else{
                    		$scope.idxForCancelOrderedProc = index;
                    		angular.element('#cancelOrderedProcedure').modal('show');
                    	}	
                    }
                }
				
				
				$scope.tempProcList  = []; // removed procedures with inactive flag
				$scope.removeProcedureOder = function(index,removeFlag) {
					var procedureSequence;
					if(removeFlag == 1){  //removeFlag == 1 for procedureOrderList
						
						if($scope.procedureOrderList[index].procedureOrderId!= "" ){
							if($scope.procedureOrderList[index].isPrimarySecondary == 'Y'){
								$scope.checkPrimaryPro = false;
								$scope.procedureOrderList[index].isPrimarySecondary == 'N';
							}
							$scope.procedureOrderList[index].status="I";
							 var obj = angular.copy($scope.procedureOrderList[index]);
							   $scope.tempProcList.push(obj);
							   
							    procedureSequence = $scope.procedureOrderList[index].procedureSequence;
								$scope.procedureOrderedSequenceList.splice($scope.procedureOrderList.length,1);
								
							$scope.procedureOrderList.splice(index, 1);
							angular.forEach($scope.procedureOrderList,function(value,idx){
								if(value.procedureSequence > procedureSequence){
									value.procedureSequence = value.procedureSequence -1;
								}
							});
							}else{
								var procedureSequence = $scope.procedureOrderList[index].procedureSequence;
								$scope.procedureOrderedSequenceList.splice($scope.procedureOrderList.length,1);
								
							$scope.procedureOrderList.splice(index, 1);
							angular.forEach($scope.procedureOrderList,function(value,idx){
								if(value.procedureSequence > procedureSequence){
									value.procedureSequence = value.procedureSequence -1;
								}
							});
						}
					}else{ //removeFlag == 2 for orderedProcedureList 

						if($scope.orderedProcedureList[index].procedureOrderId==null || $scope.orderedProcedureList[index].procedureOrderId == 0 || $scope.orderedProcedureList[index].procedureOrderId == "")
                    	{ //when added new procedure in saved list then remove it without selecting reason
							procedureSequence = $scope.orderedProcedureList[index].procedureSequence;
							$scope.procedureSequenceList.splice($scope.orderedProcedureList.length,1);
							$scope.orderedProcedureList.splice(index,1);
							angular.forEach($scope.orderedProcedureList,function(value,idx){
								if(value.procedureSequence > procedureSequence){
									value.procedureSequence = value.procedureSequence -1;
									value.isEdited = true;
								}
							});
							
                    	}else{
                    		if($scope.orderedProcedureList[$scope.idxForCancelOrderedProc].reasonId == "" || $scope.orderedProcedureList[$scope.idxForCancelOrderedProc].reasonId == null || $scope.orderedProcedureList[$scope.idxForCancelOrderedProc].reasonId == undefined){
								
								growl.warning("Please select reason..!!", {
									title : 'Warning!'
								});
								return false;
								
								}else{
								 angular.element('#cancelOrderedProcedure').modal('hide');
								 $scope.orderedProcedureList[index].status="I";
								 var obj = angular.copy($scope.orderedProcedureList[index]);
								 $scope.tempProcList.push(obj);
							
								  procedureSequence = $scope.orderedProcedureList[index].procedureSequence;
								 $scope.procedureSequenceList.splice($scope.orderedProcedureList.length,1);
								 $scope.orderedProcedureList.splice(index,1);
								 angular.forEach($scope.orderedProcedureList,function(value,idx){
									 if(value.procedureSequence > procedureSequence){
										 value.procedureSequence = value.procedureSequence -1;
										 value.isEdited = true;
									 }
								 });
								}
                    	}
					}
					
					
				}
				
				$scope.editProcedureOder = function(procedreOrderObj,idx){
					
					if(procedreOrderObj.specialityId != ""){
						$scope.procedureSubSpeciality.specialityId = procedreOrderObj.specialityId;
					}
					$scope.procedureSubSpeciality.specialityName = procedreOrderObj.specialityName;
					$scope.procedureSearch = procedreOrderObj.procedureName;
					$scope.procedureOrderMaster.procedureName =  procedreOrderObj.procedureName;
					$scope.procedureOrderMaster.specialityId = procedreOrderObj.specialityId;
					$scope.procedureOrderMaster.specialityName = procedreOrderObj.specialityName;
					$scope.procedureOrderMaster.serviceId = procedreOrderObj.serviceId;
					$scope.procedureOrderMaster.isIcuRequired = procedreOrderObj.isIcuRequired;
					$scope.procedureOrderMaster.amount = procedreOrderObj.amount;
					$scope.procedureOrderMaster.createdBy =  procedreOrderObj.createdBy;
                    $scope.preferedTime =  procedreOrderObj.preferedTime;
                    var updateDate = procedreOrderObj.preferedDateTime.split(" ");
                    var newdate = updateDate[0].split("-").reverse().join("-");
                    $scope.procedureDate = moment(newdate,'YYYY-MM-DD').toDate();
					$scope.procedureOrderMaster.createdDate =  procedreOrderObj.createdDate;
					$scope.procedureOrderMaster.isBloodComponentRequired =  procedreOrderObj.isBloodComponentRequired;
					$scope.procedureOrderMaster.isIcuRequired =  procedreOrderObj.isIcuRequired;
					$scope.procedureOrderMaster.isPacRequired =  procedreOrderObj.isPacRequired;
					$scope.procedureOrderMaster.isPrimarySecondary =  procedreOrderObj.isPrimarySecondary;
					$scope.procedureOrderMaster.orgId =  procedreOrderObj.orgId;
					$scope.procedureOrderMaster.status =  procedreOrderObj.status;
					$scope.procedureOrderMaster.unitId =  procedreOrderObj.orgId;
					$scope.procedureOrderMaster.updatedBy =  procedreOrderObj.updatedBy;
					$scope.procedureOrderMaster.updatedDate =  procedreOrderObj.updatedDate;
					//$scope.procedureDate = procedreOrderObj.preferedDateTime;
					$scope.procedureOrderMaster.urgency = procedreOrderObj.urgency;
					$scope.procedureOrderMaster.requestReason = procedreOrderObj.requestReason;
					$scope.procedureOrderMaster.procedureSequence = procedreOrderObj.procedureSequence;
					//$scope.procedureReqMaster = angular.copy($scope.procedureRequestMaster);
					$scope.procedureReqMaster.proReqId = procedreOrderObj.proReqId;
					
					$scope.procedureOrderMaster.proReqId = procedreOrderObj.proReqId;
					$scope.procedureOrderMaster.procedureOrderId = procedreOrderObj.procedureOrderId;
					$scope.implantList = procedreOrderObj.implantDetailsList;
					$scope.medicationList = procedreOrderObj.medicationDetailsList;
					$scope.consentDetailList = procedreOrderObj.procedureOrderConsentList;
					$scope.checklistDetailList = procedreOrderObj.procedureOrderCheckList;
					$scope.staffDetailList = procedreOrderObj.procedureOrderStaffList;
					$scope.equipmentDetailList = procedreOrderObj.procedureOrderEquipmentList;
					$scope.finalServiceList = procedreOrderObj.procedureOrderServiceList;
					$scope.patientEduObj = procedreOrderObj.procedureOrderPatientEducationDto;
					$scope.updateProcCurrentIdx = idx; //to get current saved  procedure index
				}
				
/****************************************************************  Save Procedure Order Start *****************************************************************/								
				$scope.initProcedureOrder();
				
				$scope.opearationTheatreAndTableObj  = {};
				$scope.getOTTThetreAndTableByServiceId = function(serviceId){
					$scope.opearationTheatreAndTableObj  ={};
					$scope.startLoader();
					var d = {};
					var URI = BASE_URL
					+ ROOT_URL
					+ "/api/otims/getOTTThetreAndTableByServiceId/"+serviceId+"/"+$scope.organizationId+"/"+$scope.unitId ; 

					GenericService.serviceAction("GET", URI, d).then(
									function(response) {
										if (response.data.status == "success") {
											$scope.stopLoader();
											$scope.opearationTheatreAndTableObj = response.data.object;
											console.log("opearationTheatreAndTableObj",$scope.opearationTheatreAndTableObj);
											if($scope.commonObj.isFromReservationCancellation!=undefined && $scope.commonObj.isFromReservationCancellation){
												 $timeout(function () {
												$scope.getScheduleDetailsProc();
												 },1000);
											}
										}
										$scope.stopLoader();
									});
					$scope.stopLoader();
				}
				
/****************************************************************  Save Procedure Order End *****************************************************************/								
				
				$scope.orderedProcedureList = [];
				$scope.proSequenceIds = [];
				$scope.getOrderProcedureList = function() {
					$scope.orderedProcedureList = [];
					$scope.startLoader();
					var d = {};
				var URI = BASE_URL
					+ ROOT_URL
					+ "/api/procedure/getProcedureOrderListByAdmissionId/0/0/"
					+ $scope.commonObj.proReqId;

					GenericService.serviceAction("GET", URI, d).then(
									function(response) {
										if (response.data.status == "success") {
											$scope.stopLoader();
											$scope.orderedProcedureList = [];
											$scope.procedureSequenceList = [
												{
													id : 0,
													sequenceName : " "
												}
												];
											$scope.proSequenceIds = [];
											$scope.orderedProcedureList = response.data.listObject;
										
											angular.forEach($scope.orderedProcedureList,function(value,index){
												
												if(value.procedureSequence == 1){
													$scope.getOTTThetreAndTableByServiceId(value.serviceId);
												}
												
												var arr = {};
												arr.procedureSequence = value.procedureSequence;
												$scope.proSequenceIds.push(arr);
											});
											
											if($scope.proSequenceIds.length>0){
												$scope.proSequenceIds = $filter('orderBy')($scope.proSequenceIds, 'procedureSequence', false) ;
												angular.forEach($scope.proSequenceIds , function(val , idx){
													if(val.procedureSequence == 1){
														var newArr = {
																id : 1,
																sequenceName : "Primary"
														};
														if($scope.procedureSequenceList.length>0){
															var obj = $scope.procedureSequenceList.find(ar=>ar.id == 1);
															if(obj == undefined){
																$scope.procedureSequenceList.push(newArr);
															}
														}else{
															$scope.procedureSequenceList.push(newArr);
														}
													}else if(val.procedureSequence == 2){
														var newArr = {
																id : 2,
																sequenceName : "Secondary"
														};
														if($scope.procedureSequenceList.length>0){
															var obj = $scope.procedureSequenceList.find(ar=>ar.id == 2);
															if(obj == undefined){
																$scope.procedureSequenceList.push(newArr);
															}
														}else{
															$scope.procedureSequenceList.push(newArr);
														}
													}else if(val.procedureSequence == 3){
														var newArr = {
																id : 3,
																sequenceName : "Tertiary"
														};
														if($scope.procedureSequenceList.length>0){
															var obj = $scope.procedureSequenceList.find(ar=>ar.id == 3);
															if(obj == undefined){
																$scope.procedureSequenceList.push(newArr);
															}
														}else{
															$scope.procedureSequenceList.push(newArr);
														}
													}else if(val.procedureSequence == 4){
														var newArr = {
																id : 4,
																sequenceName : "Quaternary"
														};
														if($scope.procedureSequenceList.length>0){
															var obj = $scope.procedureSequenceList.find(ar=>ar.id == 4);
															if(obj == undefined){
																$scope.procedureSequenceList.push(newArr);
															}
														}else{
															$scope.procedureSequenceList.push(newArr);
														}
													}else if(val.procedureSequence == 5){
														var newArr = {
																id : 5,
																sequenceName : "Quinary"
														};
														if($scope.procedureSequenceList.length>0){
															var obj = $scope.procedureSequenceList.find(ar=>ar.id == 5);
															if(obj == undefined){
																$scope.procedureSequenceList.push(newArr);
															}
														}else{
															$scope.procedureSequenceList.push(newArr);
														}
													}
												});
											}
											console.log("orderedProcedureList from reservation Cancellation ",$scope.orderedProcedureList);
										}
									});
					$scope.stopLoader();
				}
				
				if($scope.commonObj.scheduleDate!=undefined){
		        	 if( $scope.commonObj.proReqId!=null){
		        		 $scope.getOrderProcedureList();
				        }
		        }
				
				$scope.procedureDisabledFlag=false;
				$scope.getProcedureConfigDetails = function(index,indexFlag){
				
					$scope.currentIndex = "";
					$scope.orderedCurrentIndex = "";
					$scope.indexFlag="";
					$scope.procedureDisabledFlag=false;
					$scope.indexFlag=indexFlag;
					
					if(indexFlag == 1){
						$scope.currentIndex =index;
					}else{
						$scope.orderedCurrentIndex =index;
						$scope.procedureDisabledFlag=true;
					}
				}
				
/********************************************** Procedure Details Tab END **************************************************************/								
				
		
	/**************************************** Scheduling Details Tab START ******************************************************/
											
											$scope.finalSum = "";
											$scope.calTim = function(fromTime ,duration){
												$scope.finalSum = "";
												var num = duration; //$scope.common.totalDuration;
							    				var hours = (num / 60);
							    				var rhours = Math.floor(hours);
							    				var minutes = (hours - rhours) * 60;
							    				var rminutes = Math.round(minutes);
							    				var sOtTime =rhours+":" + rminutes;
							    				console.log("___________",rhours+":" + rminutes);
												
							    				var toTime = fromTime ;//$scope.common.preferedTime;
							    				//var selectedToTime = ;
							    				 var selectedToTimeRes = toTime.split(":");
							    				
							    				 var selectedSwitchTimeRes = sOtTime.split(":");
							    				
							    				 var sumofHrs = parseInt(selectedToTimeRes[0]) + parseInt(selectedSwitchTimeRes[0]);
							    				 var sumOfMin =  parseInt(selectedToTimeRes[1]) + parseInt(selectedSwitchTimeRes[1]);
							    				 $scope.finalSum;
							    				
												 if(sumOfMin>=60){
							    					 
							    					 var newsumofHrs = sumofHrs+":"+"00";
							    					 
							    					 var num = sumOfMin ;
							    						var hours = (num / 60);
							    						var rhours = Math.floor(hours);
							    						var minutes = (hours - rhours) * 60;
							    						var rminutes = Math.round(minutes);
							    						var sOtTime1 =rhours+":" + rminutes;
							    						
							    						 var selectedToTimeRes1 = newsumofHrs.split(":");
							    							
							    						 var selectedSwitchTimeRes1 = sOtTime1.split(":");
							    						
							    						 var sumofHrs1 = parseInt(selectedToTimeRes1[0]) + parseInt(selectedSwitchTimeRes1[0]);
							    						 var sumOfMin1 =  parseInt(selectedToTimeRes1[1]) + parseInt(selectedSwitchTimeRes1[1]);
							    						
							    						 $scope.finalSum = (sumofHrs1<10?"0"+sumofHrs1:sumofHrs1)+":"+(sumOfMin1<10?"0"+sumOfMin1:sumOfMin1);
							    				 }else{
							    					 
							    					 $scope.finalSum = (sumofHrs<10?"0"+sumofHrs:sumofHrs)+":"+(sumOfMin<10?"0"+sumOfMin:sumOfMin);
							    					 
							    				 }
												 
												 console.log("finalSum",$scope.finalSum);
												 $scope.finalSum =  $scope.finalSum +":"+"00";
											}
											
							$scope.checkAvailableOTSlot = function(info,flag){
							$scope.startLoader();
							
							var data = {
								orgId : $scope.organizationId, 
								unitId : $scope.unitId,
								otDate : $scope.commonObj.scheduleDate!=undefined ? $scope.commonObj.scheduleDate : info.otDate == undefined ? moment(moment($scope.preferedProcDate,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD'):info.otDate,
								tableId :info.tableId == undefined ? info.selectedTableId :info.tableId ,
								theatreId : info.theatreId == undefined ?info.selectedTheatreId: info.theatreId,
								fromTime : info.fromTime == undefined ?$scope.preferedProcTime:info.fromTime,
								toTime : info.toTime == undefined ? $scope.finalSum:info.toTime,
								proReqId : $scope.saveProReqId
							};
							$scope.OTTObj = data;
							var URI = BASE_URL + ROOT_URL + "/global/otims/toCheckSlotAvailability";
							console.log("URI", URI);
							GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
								if (response.data.status == "success"){
									if(flag==0){
										info.color="green";
									}else{
										
										angular.forEach($scope.otList,function(value,index){
											if($scope.otList[index].color!=""){
												$scope.otList[index].color="green";
											}
										});
										
										  angular.forEach($scope.otAddedList,function(value,index){
											  if($scope.otAddedList[index].color!=""){
												  $scope.otAddedList[index].color="green";
											  }
			                                });
									}
									}else if(response.data.status =="error"){
										if(flag==0){
											info.color="red";											
										}else{
											angular.forEach($scope.otList,function(value,index){
												if($scope.otList[index].color!=""){
													$scope.otList[index].color="red";
												}
											});
										
										 angular.forEach($scope.otAddedList,function(value,index){
											 if($scope.otAddedList[index].color!=""){
												 $scope.otAddedList[index].color="red";
											 }
			                                });
										}
									}
								$scope.stopLoader();
							});
							} 			
							
							
							/******************** checkAvailabilityOfEquipment *****************/
							$scope.checkAvailabilityOfEquipment = function(equipObj,idx,flag){
								$scope.startLoader();
								var batchId ="";
								var itemId ="";
								if(flag==0){
									batchId = equipObj.selectedItemBatchObj.batchId ;
									itemId = equipObj.selectedItemBatchObj.itemId
								}else{
									batchId = equipObj.batchId ;
									itemId = equipObj.itemId
								}
							var isExist = false;
									if($scope.equipmentDetailList.length>0){
										angular.forEach($scope.equipmentDetailList,function(value,index){
											if(value.batchId == equipObj.batchId && index!=idx){
												
												isExist = true;
											}
										});
									}
									
									if($scope.equipmentBatchList.length>0){
										angular.forEach($scope.equipmentBatchList,function(value,index){
											if(value.selectedItemBatchObj.batchId == batchId  && index!=idx){
												isExist = true;
											}
										});
									}
									
									if(isExist){
										growl.warning("Equipment Already Exist",
												{
													title : 'warning!'
												});
										$rootScope.stopLoader();
										equipObj.color = "";
										equipObj= {};
										return false;
									}else{
								//	$scope.calTim($scope.common.preferedTime,$scope.common.totalDuration) ;
									var data = {
										orgId : $scope.organizationId, 
										unitId : $scope.unitId,
										batchId : batchId,
										itemId : itemId,
										equipmentSchedulingDate : $scope.commonObj.scheduleDate!=undefined ? $scope.commonObj.scheduleDate : moment(moment($scope.preferedProcDate,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD'),
										fromTime : $scope.preferedProcTime,
										toTime : $scope.finalSum,
										proReqId : $scope.saveProReqId
									};
									
									  if($scope.equipcheckList.length>0){
		                                    var found = $scope.equipcheckList.find(ar=>ar.itemId == itemId && ar.batchId == batchId);
		                                       if(found==undefined)
		                                           {
		                                    	     	$scope.equipcheckList.push(data);
		                                           }
		                                }else{
		                                	 $scope.equipcheckList.push(data);
		                                }
									
									console.log("data",data);
									var URI1 = BASE_URL + ROOT_URL + "/global/otims/equipmentScheduling/checkEquipmentavailableOrNot";
									console.log("URI", URI1);
									GenericService.serviceAction(METHOD_POST, URI1, data).then(function(response) {
										if (response.data.status == "success"){
											equipObj.color=response.data.message;
											}else if(response.data.status =="error"){
												equipObj.color=response.data.message;
												$scope.stopLoader();
											}
										$scope.stopLoader();
									});
									}
							} 			
							
							
							/******************** checkAvailabilityOfEquipment  END *****************/
							
							/******************** checkAvailabilityOfStaff  START *****************/
							$scope.checkAvailabilityOfStaff = function(staffObj,flag,idx){
							$scope.startLoader();
							var userId ="";
							if(flag==0){
								userId = staffObj.userId;
							}else{
								userId = staffObj.selectedStaffId.userId;
								var isExist = false;
								if($scope.otStaffDetailsList.length>0){
									angular.forEach($scope.otStaffDetailsList,function(value,index){
										if(value.roleId == staffObj.roleId && value.selectedStaffId.userId == staffObj.selectedStaffId.userId && index!=idx){
											isExist = true;
										}
									});
								}
								
								if($scope.otAddedStaffDetailsList.length>0){
									angular.forEach($scope.otAddedStaffDetailsList,function(value,index){
										if(value.roleId == staffObj.roleId && value.userId == staffObj.selectedStaffId.userId){
											isExist = true;
										}
									});
								}
								
								if(isExist){
									growl.warning("Staff Already Exist",
											{
												title : 'warning!'
											});
									/*staffObj.selectedStaffId.userId = "";*/
									staffObj.color = "";
									staffObj.selectedStaffId = {};
									$rootScope.stopLoader();
									return false;
								}
							}
							//$scope.calTim($scope.common.preferedTime,$scope.common.totalDuration) ;
							 
							
								var data = {
										orgId : $scope.organizationId, 
										unitId : $scope.unitId,
										userId : userId,
										roleId : staffObj.roleId,
										staffSchedulingDate :$scope.commonObj.scheduleDate!=undefined ? $scope.commonObj.scheduleDate : moment(moment($scope.preferedProcDate,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD'),
										fromTime : $scope.preferedProcTime,
										toTime : $scope.finalSum,
										proReqId : $scope.saveProReqId
									};
								
                                if($scope.staffcheckList.length>0){
                                    var found = $scope.staffcheckList.find(ar=>ar.userId == userId && ar.roleId == staffObj.roleId);
                                       if(found==undefined)
                                           {
                                    	     	$scope.staffcheckList.push(data);
                                           }
                                }else{
                                	 $scope.staffcheckList.push(data);
                                }

									console.log("data",data);
									var URI = BASE_URL + ROOT_URL + "/global/otims/checkStaffAvailableOrNot";
									console.log("URI", URI);
									GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
										if (response.data.status == "success"){
											staffObj.color="green";
											}else if(response.data.status =="error"){
												staffObj.color="red";
											}
										$scope.stopLoader();
									});
							} 			
							
							/******************** checkAvailabilityOfStaff  END *****************/
							
							$scope.setColor = function(info){
							if(info.color == "red"){
								  return {"background-color": "red"};
							}else if(info.color == "green"){
								  return {"background-color": "#b4da80"};
							}else if(info.color == "gray"){
								  return {"background-color": "gray"};
							}
							}

				
				/********************************** OT Tab START **********************************************/							
				$scope.theatreObj ={};
				$scope.tableObj = {};
				
				//Get Active Operation Theatre List
				$scope.activeOperationTheatreList=[];	
				 $scope.getActiveOperationTheatreList = function() {
					// $scope.startLoader();
					var data = {
							orgId:$scope.organizationId, 
							unitId:$scope.unitId
					};
					$scope.operationTheatreTypeId = $scope.commonObj.operationTheatreTypeId!=undefined?$scope.commonObj.operationTheatreTypeId:$scope.operationTheatreTypeId;
				//	var URI = BASE_URL + ROOT_URL + GET_ALL_ACTIVE_OPERATION_THEATRE_LIST;
					var URI = BASE_URL + ROOT_URL + "/api/otims/getOperationTheatreByTypeId" 
					+ S + $scope.operationTheatreTypeId + S + $scope.organizationId + S + $scope.unitId;
					console.log("URI", URI);
					GenericService.serviceAction(METHOD_GET, URI, "").then(function(response) {
						$scope.activeOperationTheatreList = response.data.listObject;
						console.log("activeOperationTheatreList",$scope.activeOperationTheatreList);
					});
					//$scope.stoptLoader();
				}
				// $scope.getActiveOperationTheatreList();
	
				 //get Tabletheatre list by theatre type Id
				 $scope.theatreList = [];
				 $scope.otList = [];
					$scope.getOperationTheatreByOTTypeId = function(theatreTypeId){
						$scope.startLoader();
						$scope.otList = [];
						$scope.theatreList = [];
						var URI = BASE_URL + ROOT_URL + "/api/otims/getOperationTheatreByTypeId" 
						+ S + theatreTypeId + S + $scope.organizationId + S + $scope.unitId;
						console.log("URI", URI);
						GenericService.serviceAction(METHOD_GET, URI, "").then(function(response) {
							$scope.theatreList = response.data.listObject;
							
							var newArr = {};
							newArr.theatreList = $scope.theatreList;
							newArr.selectedTheatreId = "";
							newArr.theatreTypeName = "";
							newArr.tableList = "";
							newArr.color = "";
							newArr.status="A";
							newArr.selectedTableId = "";
							$scope.otList.push(newArr);
							
							$scope.stopLoader();
						});
						
						$scope.stopLoader();
					}
				 
			 
				 $scope.activeOperationTableList = [];
				 $scope.otAddedList = [];
				 $scope.getOperationTableByTheatreId = function(theatreId,flag,index) {
					 $scope.startLoader();
					 $scope.activeOperationTableList = [];
						var data = {
								orgId:$scope.organizationId, 
								unitId:$scope.unitId,
								operationTheatreId : theatreId
						};
						
						var URI = BASE_URL + ROOT_URL + "/api/otims/getActiveOperationTableListByTheatreId";
						console.log("URI", URI);
						GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
							if(flag == 0){
							$scope.activeOperationTableList = response.data.listObject;
							console.log("activeOperationTableList",$scope.activeOperationTableList);
							}else{
								
								$scope.otList[index].tableList = response.data.listObject;
							}
							$scope.stopLoader();
						});
					}
				
				 $scope.tempOtList = [];
					$scope.addOTDetails = function(){
						$scope.startLoader();
						var isExist = false;
						if($scope.otAddedList.length>0){
							angular.forEach($scope.otAddedList,function(value,index){
								isExist = true;
								/*if(value.color == "" || value.color == "green"){
									isExist = true;
								}*/
							});
						}
						
						if($scope.otList.length>0){
							angular.forEach($scope.otList,function(value,index){
								//isExist = true;
								if(value.status =="A"){
									isExist = true;
								}
								/*if(value.color == "" || value.color == "green"){
									isExist = true;
								}*/
							});
						} 
						
						if(isExist){
							growl.warning("Table Already added in List ",
									{
										title : 'warning!'
									});
							$scope.stopLoader();
							return false;
							
						}else{
							
							if($scope.theatreObj.operationTheatreId == "" || $scope.theatreObj.operationTheatreId == undefined){
								growl.warning("Please Select Opration Theatre ",
										{
											title : 'warning!'
										});
								$scope.stopLoader();
								return false;
							}
							if($scope.tableObj.operationTableId == "" || $scope.tableObj.operationTableId == undefined){
								growl.warning("Please Select Opration Table ",
										{
											title : 'warning!'
										});
								$scope.stopLoader();
								return false;
							}
							
						var isAddedInOTList =false;	
						if($scope.tempOtList.length>0){
							angular.forEach($scope.tempOtList,function(value,index){
								if(value.selectedTheatreId == $scope.theatreObj.operationTheatreId && value.selectedTableId ==$scope.tableObj.operationTableId){
									value.status = 'A';
									isAddedInOTList = true;
									$scope.otList.push(value);
									$scope.checkAvailableOTSlot(value,0);
								}
							});
						}	
						if(!isAddedInOTList){
						var newArr = {};
						newArr.selectedTheatreId = $scope.theatreObj.operationTheatreId;
						newArr.theatreTypeName = $scope.theatreObj.theatreName;
						newArr.tableName = $scope.tableObj.tableName;
						newArr.color = "";
						newArr.status = "A";
						newArr.selectedTableId = $scope.tableObj.operationTableId;
						$scope.otAddedList.push(newArr);
						
						$scope.checkAvailableOTSlot(newArr,0);
						}else{
							$scope.tempOtList = [];
						}
						$scope.tableObj = {};
						$scope.theatreObj = {};
						}
						$scope.stopLoader();
					};
					  
					$scope.removeOTDetails = function (index,flag){
						if(flag==0){
							if($scope.otList[index].procedureOtScheduleId!=null){
								$scope.otList[index].status = 'I';
								
								$scope.otList[index].status="I";
								 var obj = angular.copy($scope.otList[index]);
								   $scope.tempOtList.push(obj);
								$scope.otList.splice(index, 1);
								
							}else{
								$scope.otList.splice(index);
							}	
						}else{
						$scope.otAddedList.splice(index);
						}
					}
					
	/********************************** OT Tab END **********************************************/		
				$scope.filtterStaffList = [];
				$scope.filtterEquipList = [];
				$scope.otStaffDetailsList = [];
				$scope.OTTypes = "";
				$scope.procedures = "";
				$scope.procedureTypes = "";
				$scope.getScheduleDetailsFromProc = function(){
					 $scope.filtterStaffList = [];
					 $scope.filtterEquipList = [];
					 $scope.otStaffDetailsList = [];
					if($scope.procedureOrderList.length>0 && $scope.procedureOrderList!=undefined){
						angular.forEach($scope.procedureOrderList,function(value,index){
							if(value.procedureOrderStaffList.length>0){
								angular.forEach(value.procedureOrderStaffList,function(value1,index1){
									if(value.procedureOrderStaffList.length>0){

											if(index == 0){
												$scope.filtterStaffList.push(value1);
											}else{
												var count = 0;
												angular.forEach($scope.filtterStaffList,function(value2,index2){
													
													if(value1.roleId == value2.roleId){
														count++;
														if(value1.staffNumber > value2.staffNumber){
															value2.staffNumber = value1.staffNumber;
														}	
													}
												});
												if(count == 0){
													$scope.filtterStaffList.push(value1);
												}
											}
									}
								});
							}

							if(value.procedureOrderEquipmentList.length>0){
								angular.forEach(value.procedureOrderEquipmentList,function(value1,index1){
									if(value.procedureOrderEquipmentList.length>0){

											if(index == 0){
												$scope.filtterEquipList.push(value1);
											}else{
												var count = 0;
												angular.forEach($scope.filtterEquipList,function(value2,index2){
													
													if(value1.itemId == value2.itemId){
														count++;
														if(value1.qty > value2.qty){
															value2.qty = value1.qty;
														}	
													}
												});
												if(count == 0){
													$scope.filtterEquipList.push(value1);
												}
											}
									}
								});
							}
						});
					}
					
					if($scope.filtterStaffList.length>0 && $scope.filtterStaffList!=undefined){
						$scope.otStaffDetailsList = [];
								angular.forEach($scope.filtterStaffList,function(value,index){

									if(value.staffNumber>1){
										
										for(var i = 0; i < value.staffNumber;i++){
											
											angular.forEach($scope.staffList1,function(value1,index1){

												if(value1.roleId == value.roleId){
													
													var newArr = {};
													newArr.roleId = value.roleId;
													newArr.roleName = value.roleName;
													newArr.emailId = "";
													newArr.staffList = value1.staffList;
													newArr.color = "";
													newArr.status = "A";
													newArr.selectedStaffId = "";
													$scope.otStaffDetailsList.push(newArr);
												}

											});
											
										}
									}else{
										
										var staffCount = 0;
										angular.forEach($scope.staffList1,function(value1,index1){

										if(value1.roleId == value.roleId){
										staffCount = 1;
										var newArr = {};
										newArr.roleId = value.roleId;
										newArr.roleName = value.roleName;
										newArr.emailId = "";
										newArr.staffList = value1.staffList;
										newArr.color = "";
										newArr.status = "A";
										newArr.selectedStaffId = "";
										$scope.otStaffDetailsList.push(newArr);
										}

										});

										if(staffCount == 0){
										var newArr = {};
										newArr.roleId = value.roleId;
										newArr.roleName = value.roleName;
										newArr.emailId = "";
										newArr.staffList = [];
										newArr.color = "";
										newArr.status = "A";
										newArr.selectedStaffId = "";
										$scope.otStaffDetailsList.push(newArr);
										}
										
									}

								});
					
				}

					if($scope.filtterEquipList.length>0 && $scope.filtterEquipList!=undefined){
							$scope.equipmentBatchList = [];
							$scope.itemAndBatch = [];

									angular.forEach($scope.filtterEquipList,function(value,index){

										if(value.qty>1){
											
											for(var i = 0; i < value.qty;i++){
												$scope.itemAndBatch = [];
												angular.forEach($scope.equipmentAssetAndBatchList,function(value1,index1){

													if(value.equipmentTypeId == value1.assetCategoryId){
														
																		 var newArr = {};
																		newArr.equipmentTypeId = value.equipmentTypeId;
																		newArr.equipmentTypeName = value.equipmentTypeName;
																		newArr.itemName= value.itemName;
																		newArr.color= "";
																		newArr.status = "A" ;
																		angular.forEach(value1.assetList,function(value2,index2){
																			
																			if(value.itemId == value2.itemId){
																			
																				$scope.itemAndBatch.push(value2);
																			}

																		});	
																		newArr.itemBatchList = $scope.itemAndBatch;
																		newArr.selectedItemBatchObj = "";
																		$scope.equipmentBatchList.push(newArr);
																		$scope.itemAndBatch = [];
													}

												});
												console.log("equipmentBatchList",$scope.equipmentBatchList);												
											}
										}else{
											$scope.itemAndBatch = [];
											if($scope.equipmentAssetAndBatchList != null && $scope.equipmentAssetAndBatchList.length > 0){
												angular.forEach($scope.equipmentAssetAndBatchList,function(value1,index1){
	
														if(value.equipmentTypeId == value1.assetCategoryId){
															
														
															angular.forEach(value1.assetList,function(value2,index2){
																
																if(value.itemId == value2.itemId){
																
																	$scope.itemAndBatch.push(value2);
																}
	
															});
	
																			 var newArr = {};
																			newArr.equipmentTypeId = value.equipmentTypeId;
																			newArr.equipmentTypeName = value.equipmentTypeName;
																			newArr.color= "";
																			newArr.status = "A";
	                                                                        newArr.itemName= value.itemName;
																			newArr.itemBatchList = $scope.itemAndBatch;
																			newArr.selectedItemBatchObj = "";
																			$scope.equipmentBatchList.push(newArr);
																			$scope.itemAndBatch = [];
														}
	
													});
												console.log("equipmentBatchList",$scope.equipmentBatchList);
											}else{
												
												 var newArr = {};
													newArr.equipmentTypeId = value.equipmentTypeId;
													newArr.equipmentTypeName = value.equipmentTypeName;
													newArr.color= "";
													newArr.status = "A";
													newArr.itemName = value.itemName;
													newArr.itemBatchList = [];
													newArr.selectedItemBatchObj = "";
													$scope.equipmentBatchList.push(newArr);
											}
										}

									});
						
									
					}
					
					$scope.calTim($scope.preferedProcTime,$scope.totalDuration) 
					
					angular.forEach($scope.procedureOrderList,function(value1,index){
						
						if(value1.isPrimarySecondary == 'Y'){
							$scope.operationTheatreTypeId = value1.operationTheatreTypeId;
						}
						
						if(index== 0){
							$scope.procedures = value1.procedureName;
							$scope.procedureTypes = value1.procedureType;
							$scope.OTTypes = value1.theatreTypeName;
							
						}else{
							$scope.procedures = $scope.procedures+","+value1.procedureName;
							$scope.procedureTypes = $scope.procedureTypes+","+value1.procedureType;
							$scope.OTTypes = $scope.OTTypes+","+value1.theatreTypeName;
						}
						
					});
						$scope.equipcheckList = [];
						$scope.staffcheckList = [];
						$scope.OTTObj = {};
						
						if($scope.operationTheatreTypeId == 0){
							$rootScope.stopLoader();
							growl.warning("Please add atleast one procedure",
									{
										title : 'warning!'
									});
							$scope.firstLevelTab = 001;

						}else{
							$scope.getOperationTheatreByOTTypeId($scope.operationTheatreTypeId);
						}
				}
				$scope.procedureDetails = {};
				$scope.getSchedulingDetailsByScheduledId = function(scheduleId){
					try {
						$scope.startLoader();
						$scope.procedureDetails = {};
						$scope.otList  = [];
						$scope.otStaffDetailsList = [];
						$scope.equipmentBatchList = [];
						$scope.equipmentDetailList = [];
						$scope.otAddedStaffDetailsList = [];
						$scope.otAddedList = [];
						$scope.equipcheckList = [];
						$scope.staffcheckList = [];
						$scope.OTTObj = {};
						var data = {
								"procedureScheduleId":scheduleId,
								"organizationId" : $scope.organizationId,
								"unitId" : $scope.unitId
							};
						var URI = BASE_URL + ROOT_URL+"/api/otims/dashboard/getProcedureSchedulingDetails";
						console.log("URI", URI);
						GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {
									if(response.data.object!=null){
										$scope.procedureDetails = response.data.object;
										$scope.totalDuration = $scope.procedureDetails.totalDuration;
										
										$scope.calTim( $scope.preferedProcTime,$scope.totalDuration);
										
										if($scope.procedureDetails.procedureOTScheduleList!=null && $scope.procedureDetails.procedureOTScheduleList.length>0 ){
											if($scope.opearationTheatreAndTableObj!=null){
												angular.forEach($scope.procedureDetails.procedureOTScheduleList,function(value,index){
													var newArr = {};
												
													newArr.color =value.color;
													newArr.status = value.status;
													newArr.procedureOtScheduleId = value.procedureOtScheduleId;
													if($scope.opearationTheatreAndTableObj.operationTheatreList.length>0){
														newArr.theatreList = $scope.opearationTheatreAndTableObj.operationTheatreList;
														newArr.theatreTypeName = $scope.opearationTheatreAndTableObj.theatreTypeName;
														
														angular.forEach($scope.opearationTheatreAndTableObj.operationTheatreList,function(val , idx){
															
															if(value.theatreId == val.operationTheatreId){
																	newArr.selectedTheatreId = val.operationTheatreId;
																if(val.operationTableList.length>0){
																	newArr.tableList = val.operationTableList;
																	angular.forEach(val.operationTableList,function(value1,index1){
																		if(value.tableId == value1.operationTableId){
																			newArr.selectedTableId =value1.operationTableId;
																			$scope.otList.push(newArr);
																		}
																	});
																}
															}
														});
													
													}
												});
												
												console.log("$scope.otList",$scope.otList);
											}
											var data = {
													orgId : $scope.organizationId, 
													unitId : $scope.unitId,
													otDate : $scope.procedureDetails.procedureOTScheduleList[0].prodeureDate,
													tableId :$scope.procedureDetails.procedureOTScheduleList[0].tableId ,
													theatreId : $scope.procedureDetails.procedureOTScheduleList[0].theatreId,
													fromTime : $scope.procedureDetails.procedureOTScheduleList[0].fromTime,
													toTime : $scope.procedureDetails.procedureOTScheduleList[0].toTime,
													proReqId : $scope.saveProReqId,
													procedureOtScheduleId : $scope.procedureDetails.procedureOTScheduleList[0].procedureOtScheduleId
												};
												$scope.OTTObj = data;
										}
										
										if($scope.procedureDetails.procedureStaffScheduleList!=null  && $scope.procedureDetails.procedureStaffScheduleList.length>0){
											angular.forEach($scope.procedureDetails.procedureStaffScheduleList,function(value,index){
												angular.forEach($scope.staffList1,function(value1,index1){

													if(value1.roleId == value.roleId){

														var newArr = {};
														newArr.roleId = value.roleId;
														newArr.procedureStaffScheduleId = value.procedureStaffScheduleId;
														newArr.roleName = value.roleName;
														newArr.emailId = "";
														newArr.staffList = value1.staffList;
														newArr.color = value.color;
														newArr.status = "A";
														newArr.selectedStaffId = value;
														$scope.otStaffDetailsList.push(newArr);
														
														var data = {
																procedureStaffScheduleId : value.procedureStaffScheduleId,
																orgId : $scope.organizationId, 
																unitId : $scope.unitId,
																userId : value.userId,
																roleId : value.roleId,
																staffSchedulingDate :value.prodeureDate,
																fromTime : value.fromTime,
																toTime : value.toTime,
																proReqId : $scope.saveProReqId
															};
														$scope.staffcheckList.push(data);
													}

												});

											});
										}

										if( $scope.procedureDetails.procedureEquipmentScheduleList!=null && $scope.procedureDetails.procedureEquipmentScheduleList.length>0 ){
											angular.forEach($scope.procedureDetails.procedureEquipmentScheduleList,function(value,index){
												$scope.itemAndBatch = [];
												angular.forEach($scope.equipmentAssetAndBatchList,function(value1,index1){

														if(value.assetCatId == value1.assetCategoryId){

															angular.forEach(value1.assetList,function(value2,index2){

																if(value.itemId == value2.itemId){

																	$scope.itemAndBatch.push(value2);
																}

															});

																			var newArr = {};
																			newArr.equipmentTypeId = value.equipmentTypeId;
																			newArr.procedureEquipmentScheduleId = value.procedureEquipmentScheduleId;
																			newArr.equipmentTypeName = value.equipmentTypeName;
																			newArr.color= value.color;
																			newArr.status= "A";
																			newArr.itemName= value.itemName;
																			newArr.itemBatchList = $scope.itemAndBatch;
																			newArr.selectedItemBatchObj = value;
																			$scope.equipmentBatchList.push(newArr);
																			$scope.itemAndBatch = [];
														}
														
														var data = {
															  	procedureEquipmentScheduleId : value.procedureEquipmentScheduleId,
																orgId : $scope.organizationId, 
																unitId : $scope.unitId,
																batchId : value.batchId,
																itemId : value.itemId,
																equipmentSchedulingDate : value.prodeureDate,
																fromTime : value.fromTime,
																toTime : value.toTime,
																proReqId : $scope.saveProReqId
															};
															
														$scope.equipcheckList.push(data);

													});
											});
										}
									}
											$rootScope.stopLoader();
											console.log("$scope.procedureDetails",$scope.procedureDetails);
										});
					} catch (e) {
						console.log(e.message)
						$scope.stopLoader();
					}
				}
				
				$scope.getScheduleDetailsFromChangedProc = function(){

					 $scope.filtterStaffList = [];
					 $scope.filtterEquipList = [];
					 $scope.otStaffDetailsList = [];
					if($scope.orderedProcedureList.length>0 && $scope.orderedProcedureList!=undefined){
						angular.forEach($scope.orderedProcedureList,function(value,index){
							if(value.procedureOrderStaffList.length>0){
								angular.forEach(value.procedureOrderStaffList,function(value1,index1){
									if(value.procedureOrderStaffList.length>0){

											if(index == 0){
												$scope.filtterStaffList.push(value1);
											}else{
												var count = 0;
												angular.forEach($scope.filtterStaffList,function(value2,index2){
													
													if(value1.roleId == value2.roleId){
														count++;
														if(value1.staffNumber > value2.staffNumber){
															value2.staffNumber = value1.staffNumber;
														}	
													}
												});
												if(count == 0){
													$scope.filtterStaffList.push(value1);
												}
											}
									}
								});
							}

							if(value.procedureOrderEquipmentList.length>0){
								angular.forEach(value.procedureOrderEquipmentList,function(value1,index1){
									if(value.procedureOrderEquipmentList.length>0){

											if(index == 0){
												$scope.filtterEquipList.push(value1);
											}else{
												var count = 0;
												angular.forEach($scope.filtterEquipList,function(value2,index2){
													
													if(value1.itemId == value2.itemId){
														count++;
														if(value1.qty > value2.qty){
															value2.qty = value1.qty;
														}	
													}
												});
												if(count == 0){
													$scope.filtterEquipList.push(value1);
												}
											}
									}
								});
							}
						});
					}
					
					if($scope.filtterStaffList.length>0 && $scope.filtterStaffList!=undefined){
						$scope.otStaffDetailsList = [];
								angular.forEach($scope.filtterStaffList,function(value,index){

									if(value.staffNumber>1){
										
										for(var i = 0; i < value.staffNumber;i++){
											
											angular.forEach($scope.staffList1,function(value1,index1){

												if(value1.roleId == value.roleId){
													
													var newArr = {};
													newArr.roleId = value.roleId;
													newArr.roleName = value.roleName;
													newArr.emailId = "";
													newArr.staffList = value1.staffList;
													newArr.color = "";
													newArr.status = "A";
													newArr.selectedStaffId = "";
													$scope.otStaffDetailsList.push(newArr);
												}

											});
											
										}
									}else{
										
										var staffCount = 0;
										angular.forEach($scope.staffList1,function(value1,index1){

										if(value1.roleId == value.roleId){
										staffCount = 1;
										var newArr = {};
										newArr.roleId = value.roleId;
										newArr.roleName = value.roleName;
										newArr.emailId = "";
										newArr.staffList = value1.staffList;
										newArr.color = "";
										newArr.status = "A";
										newArr.selectedStaffId = "";
										$scope.otStaffDetailsList.push(newArr);
										}

										});

										if(staffCount == 0){
										var newArr = {};
										newArr.roleId = value.roleId;
										newArr.roleName = value.roleName;
										newArr.emailId = "";
										newArr.staffList = [];
										newArr.color = "";
										newArr.status = "A";
										newArr.selectedStaffId = "";
										$scope.otStaffDetailsList.push(newArr);
										}
										
									}

								});
					
				}

					if($scope.filtterEquipList.length>0 && $scope.filtterEquipList!=undefined){
							$scope.equipmentBatchList = [];
							$scope.itemAndBatch = [];

									angular.forEach($scope.filtterEquipList,function(value,index){

										if(value.qty>1){
											
											for(var i = 0; i < value.qty;i++){
												$scope.itemAndBatch = [];
												angular.forEach($scope.equipmentAssetAndBatchList,function(value1,index1){

													if(value.equipmentTypeId == value1.assetCategoryId){
														
																		 var newArr = {};
																		newArr.equipmentTypeId = value.equipmentTypeId;
																		newArr.equipmentTypeName = value.equipmentTypeName;
																		newArr.itemName= value.itemName;
																		newArr.color= "";
																		newArr.status = "A" ;
																		angular.forEach(value1.assetList,function(value2,index2){
																			
																			if(value.itemId == value2.itemId){
																			
																				$scope.itemAndBatch.push(value2);
																			}

																		});	
																		newArr.itemBatchList = $scope.itemAndBatch;
																		newArr.selectedItemBatchObj = "";
																		$scope.equipmentBatchList.push(newArr);
																		$scope.itemAndBatch = [];
													}

												});
												console.log("equipmentBatchList",$scope.equipmentBatchList);												
											}
										}else{
											$scope.itemAndBatch = [];
											if($scope.equipmentAssetAndBatchList != null && $scope.equipmentAssetAndBatchList.length > 0){
												angular.forEach($scope.equipmentAssetAndBatchList,function(value1,index1){
	
														if(value.equipmentTypeId == value1.assetCategoryId){
															
														
															angular.forEach(value1.assetList,function(value2,index2){
																
																if(value.itemId == value2.itemId){
																
																	$scope.itemAndBatch.push(value2);
																}
	
															});
	
																			 var newArr = {};
																			newArr.equipmentTypeId = value.equipmentTypeId;
																			newArr.equipmentTypeName = value.equipmentTypeName;
																			newArr.color= "";
																			newArr.status = "A";
	                                                                        newArr.itemName= value.itemName;
																			newArr.itemBatchList = $scope.itemAndBatch;
																			newArr.selectedItemBatchObj = "";
																			$scope.equipmentBatchList.push(newArr);
																			$scope.itemAndBatch = [];
														}
	
													});
												console.log("equipmentBatchList",$scope.equipmentBatchList);
											}else{
												
												 var newArr = {};
													newArr.equipmentTypeId = value.equipmentTypeId;
													newArr.equipmentTypeName = value.equipmentTypeName;
													newArr.color= "";
													newArr.status = "A";
													newArr.itemName = value.itemName;
													newArr.itemBatchList = [];
													newArr.selectedItemBatchObj = "";
													$scope.equipmentBatchList.push(newArr);
											}
										}

									});
						
									
					}
					
					$scope.calTim($scope.preferedProcTime,$scope.totalDuration) 
					
					angular.forEach($scope.orderedProcedureList,function(value1,index){
						
						if(value1.isPrimarySecondary == 'Y'){
							$scope.operationTheatreTypeId = value1.operationTheatreTypeId;
						}
						
						if(index== 0){
							$scope.procedures = value1.procedureName;
							$scope.procedureTypes = value1.procedureType;
							$scope.OTTypes = value1.theatreTypeName;
							
						}else{
							$scope.procedures = $scope.procedures+","+value1.procedureName;
							$scope.procedureTypes = $scope.procedureTypes+","+value1.procedureType;
							$scope.OTTypes = $scope.OTTypes+","+value1.theatreTypeName;
						}
						
					});
						$scope.equipcheckList = [];
						$scope.staffcheckList = [];
						$scope.OTTObj = {};
						if($scope.operationTheatreTypeId == undefined || $scope.operationTheatreTypeId == 0){
							$scope.operationTheatreTypeId = $scope.commonObj.operationTheatreTypeId!=undefined?$scope.commonObj.operationTheatreTypeId:$scope.operationTheatreTypeId;
						}
						if($scope.operationTheatreTypeId == 0){
							$rootScope.stopLoader();
							growl.warning("Please add atleast one procedure",
									{
										title : 'warning!'
									});
							$scope.firstLevelTab = 001;

						}else{
							$scope.getOperationTheatreByOTTypeId($scope.operationTheatreTypeId);
						}
				
				}
				
				$scope.isEditedFromProc = false;
				$scope.getScheduleDetailsProc = function(){
					
					 if($scope.procedureOrderList.length > 0){
	                    	angular.forEach($scope.procedureOrderList,function(value,index){
	                    		if(value.isPrimarySecondary == 'Y'){
	         	                   $scope.changeOfDate = moment(value.preferedDateTime,'DD-MM-YYYY').toDate();
	                               $scope.changeOfTime  = value.preferedTime ;
	                               if(value.procedureSequence == 1){
	                    				 $scope.operationTheatreTypeId = value.operationTheatreTypeId!=undefined?  value.operationTheatreTypeId
		                                         : ($scope.commonObj.operationTheatreTypeId!=undefined ? $scope.commonObj.operationTheatreTypeId :$scope.operationTheatreTypeId);
	                    			 }
	                    		}
	                    	});
	                    }
	                
					 if($scope.orderedProcedureList.length > 0){
						 $scope.isEditedFromProc = false;
	                    	angular.forEach($scope.orderedProcedureList,function(value,index){
	                    		if(value.isEdited ){
	                    			 $scope.isEditedFromProc = true;
	                    			 if(value.procedureSequence == 1){
	                    				 $scope.operationTheatreTypeId = value.operationTheatreTypeId!=undefined?  value.operationTheatreTypeId
		                                         : ($scope.commonObj.operationTheatreTypeId!=undefined ? $scope.commonObj.operationTheatreTypeId :$scope.operationTheatreTypeId);
	                    			 }
	                    		}
	                    	});
	                    }
					 
					 if($scope.commonObj.scheduleDate!=null || $scope.commonObj.scheduleDate!=undefined){
                         $scope.changeOfDate = moment($scope.commonObj.scheduleDate,'YYYY-MM-DD').toDate();
					}
					if($scope.commonObj.preferTime!=null || $scope.commonObj.preferTime!=undefined){
						  $scope.changeOfTime  = $scope.commonObj.preferTime ;
					}
					$scope.otAddedStaffDetailsList =[];
					$scope.otAddedList =[];
					$scope.equipmentDetailList =[];
					 if(($scope.commonObj.scheduleId!=null || $scope.commonObj.scheduleId!=undefined ) && !$scope.isEditedFromProc){
	                    $scope.getSchedulingDetailsByScheduledId($scope.commonObj.scheduleId);   
	                    $timeout(function () {
	                    var comDate  = moment(new Date()).format('YYYY-MM-DD');
	                     var comTime  = moment(new Date()).format('HH:mm:ss');
	                     if($scope.commonObj.scheduleDate<=comDate){

	                     	 if($scope.commonObj.scheduleDate == comDate){
	                     	        
	                     	         if($scope.commonObj.preferTime < comTime){
	                     	        	$scope.changeOfDate = new Date();
	                     	         	$scope.changeOfTime = comTime;
	                     	            $scope.changeDatefunc();
	                                 }
	                     	    
	                         }else{
                                       $scope.changeOfDate = comDate;
	                     	         	$scope.changeOfTime = comTime;
	                     	            $scope.changeDatefunc();
	                         }
	                     }
	                    }, 6000);
		        	 }else if($scope.isEditedFromProc){
		        		 $scope.getScheduleDetailsFromChangedProc(); 
		        	 }else{
		        		 $scope.getScheduleDetailsFromProc(); 
		        	 }
				}
					
					/**************************************** Staff Tab *****************************************/
					$scope.staffObj = {};
					$scope.roleObj = {};
					$scope.otimsRoleList = [];
					$scope.otimsRole = function() {
						$rootScope.startLoader();
						var data = {
								organisationId:$scope.organizationId,
								unitId:$scope.unitId
								};
						
						var URI = BASE_URL + ROOT_URL + "/api/global/getActiveOTRoleList";
						GenericService.serviceAction("POST", URI, data).then(function(response) {
							$rootScope.stopLoader();
							$scope.otimsRoleList = response.data.listObject;
							console.log("otimsRoleList",$scope.otimsRoleList);
						});
					}				
					$scope.otimsRole();
					
					$scope.staffList = [];
					$scope.otStaffDetailsList = [];
					$scope.getStaffListByRoleId = function(value,flag){
						$scope.startLoader();
						 $scope.staffList = [];
						 var roleId = "";
						 if(flag == 1){
							 roleId = value.roleId; 
						 }else{
							 roleId = value
						 }
							var data = {
									orgId:$scope.organizationId, 
									unitId:$scope.unitId,
									roleId : roleId
							};
							
							var URI = BASE_URL + ROOT_URL + "/global/otims/getStaffListByRoleId";
							console.log("URI", URI);
							GenericService.serviceAction(METHOD_POST, URI, data).then(function(response) {
								if(flag==0){
								$scope.staffList = response.data.listObject;
								}else{
									
									var newArr = {};
									newArr.roleId = value.roleId;
									newArr.roleName = value.roleName;
									newArr.emailId = value.emailId;
									newArr.staffList = response.data.listObject;
									newArr.color = "";
									newArr.status = "A";
									newArr.selectedStaffId = "";
									$scope.otStaffDetailsList.push(newArr);
									
								}
								$scope.stopLoader();
							});
					}
					
					$scope.otAddedStaffDetailsList = [];
					$scope.tempStaffList = [];
					$scope.addStaffSDetails = function(){
						$scope.startLoader();
						
						$scope.isStaffExist = false;
						
						if($scope.otAddedStaffDetailsList.length>0){
							angular.forEach($scope.otAddedStaffDetailsList,function(value,index){
								if(value.roleId == $scope.roleObj.roleId && value.userId == $scope.staffObj.userId){
									$scope.isStaffExist = true;
								}
							});
						}
						
						if($scope.otStaffDetailsList.length>0){
							angular.forEach($scope.otStaffDetailsList,function(value,index){
								if(value.roleId == $scope.roleObj.roleId && value.selectedStaffId.userId == $scope.staffObj.userId){
									$scope.isStaffExist = true;
								}
							});
						}
						
						
						if($scope.roleObj.roleId=="" || $scope.roleObj.roleId == undefined){
							$rootScope.stopLoader();
							growl.warning("Please select Role",
									{
										title : 'warning!'
									});
							return false;
						}else if($scope.staffObj.userId=="" || $scope.staffObj.userId == undefined){
							$rootScope.stopLoader();
							growl.warning("Please Select Staff Name",
									{
										title : 'warning!'
									});
							return false;
						}else{
							if($scope.isStaffExist){
								growl.warning("Role And Staff Already Exist",
										{
											title : 'warning!'
										});
								$rootScope.stopLoader();
								return false;
							}else{
								var tempListIndex = [];
								var isAddedInStaffList =false;	
								if($scope.tempStaffList.length>0){
									angular.forEach($scope.tempStaffList,function(value,index){
										if(value.roleId == $scope.roleObj.roleId && value.selectedStaffId.userId == $scope.staffObj.userId){
											value.status = 'A';
											isAddedInStaffList = true;
											$scope.otStaffDetailsList.push(value);
											tempListIndex.push(index);
											
											var newArr = {};
											newArr.roleId = value.roleId;
											newArr.userId = value.selectedStaffId.userId;
											
											$scope.checkAvailabilityOfStaff(newArr,0)
										}
									});
								}
								if(!isAddedInStaffList){
									var newArr = {};
									newArr.roleId = $scope.roleObj.roleId;
									newArr.roleName = $scope.roleObj.roleName;
									newArr.userId = $scope.staffObj.userId;
									newArr.emailId = $scope.staffObj.emailId;
									newArr.userName = $scope.staffObj.userName;
									newArr.color="";
									$scope.otAddedStaffDetailsList.push(newArr);
									
									$scope.checkAvailabilityOfStaff(newArr,0)
								}	else{
									if(tempListIndex.length>0){
										angular.forEach(tempListIndex,function(value,index){
											$scope.tempStaffList.splice(index);
										});
									}
								}	
								
							}
						}
						$scope.staffObj = {};
						$scope.roleObj = {};
						$scope.staffList = [];
						$rootScope.stopLoader();
					}
					
					$scope.removeStaffDetails = function(index,flag){
						if(flag == 0){
							$scope.otAddedStaffDetailsList.splice(index,1);
						}else{
							if($scope.otStaffDetailsList[index].procedureStaffScheduleId!=null){
								$scope.otStaffDetailsList[index].status = 'I';
								 var obj = angular.copy($scope.otStaffDetailsList[index]);
								   $scope.tempStaffList.push(obj);
								$scope.otStaffDetailsList.splice(index, 1);
							}else{
							$scope.otStaffDetailsList.splice(index,1);
							}
						}
					}
					
					
					/**************************staff tab end*********************/
					
					/************************************ equipment Tab START ********************************/
					
					$scope.equipmentTypeObj = {
							id:"",
							category:""
					}
					
					$scope.equipmentNameObj = {
							id:"",
							name:""
					}
					
					$scope.equipmentTypeList = [];
					$scope.getEquipmentTypeList = function () {
						var URI = BASE_URL + ROOT_URL + "/api/global/assets/categories/2/"+$scope.unitId+"/"+$scope.organizationId; //2 for Fixed asset type
						BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							$scope.equipmentTypeList = response.data;
							console.log("$scope.equipmentTypeList",$scope.equipmentTypeList);
							
						});
					   }
					
					$scope.getEquipmentTypeList();
					
					
					
					$scope.equipmentNameList = [];
					$scope.equipmentBasicNameList = [];
					
					$scope.getEquipmentNamesByEquipmentId = function (obj) {
						$rootScope.startLoader();
						var data = {
								unitId:$scope.unitId,
								assetCategoryId:obj.id
						};
						var URI = BASE_URL + ROOT_URL + "/api/global/items/otims/getItemAndBatchList"; 
						BillingGenericService.serviceAction(METHOD_POST,URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							$scope.equipmentBasicNameList = response.data;
						
								angular.forEach($scope.equipmentBasicNameList,
										function(value,index) {
											var data = {};
											data.id = value.batchId;
											data.itemName = value.itemName;
											data.equipmentName = value.equipmentName;
											data.itemId = value.itemId;
											$scope.equipmentNameList.push(data);
										});
							$rootScope.stopLoader();	
						});
						
					   }				
					
					$scope.equipmentDetailList = [];
					$scope.tempEquipList = [];
					$scope.addEquipmentDetails = function(){
						try{
							$rootScope.startLoader();
					
							$scope.isEquipmentExist = false;
							
							if($scope.equipmentDetailList.length>0){
								angular.forEach($scope.equipmentDetailList,function(value,index){
									if(value.batchId == $scope.equipmentNameObj.id){
										$scope.isEquipmentExist = true;
									}
								});
							}
							
							if($scope.equipmentBatchList.length>0){
							angular.forEach($scope.equipmentBatchList,function(value,index){
								if(value.selectedItemBatchObj!=undefined){
								if(value.selectedItemBatchObj.batchId == $scope.equipmentNameObj.id){
									$scope.isEquipmentExist = true;
								}
								}
							});
							}
							if($scope.equipmentTypeObj.id==""){
								$rootScope.stopLoader();
								growl.warning("Please select Equipment Type",
										{
											title : 'warning!'
										});
							}else if($scope.equipmentNameObj.id==""){
								$rootScope.stopLoader();
								growl.warning("Please Select Equipment Name",
										{
											title : 'warning!'
										});
							}else{
								if($scope.isEquipmentExist){
									growl.warning("Equipment Already Exist",
											{
												title : 'warning!'
											});
									$rootScope.stopLoader();
									return false;
									
								}else{
								
							
						var tempEquipListIndex = [];
						var isAddedInEquipList =false;	
						if($scope.tempEquipList.length>0){
										angular.forEach($scope.tempEquipList,function(value,index){
											if(value.selectedItemBatchObj.batchId == $scope.equipmentNameObj.id){
												value.status = 'A';
												isAddedInEquipList = true;
												$scope.equipmentBatchList.push(value);
												tempEquipListIndex.push(index);
												
												var newArr = {};
												newArr.itemId = $scope.equipmentNameObj.itemId;
												newArr.batchId =$scope.equipmentNameObj.id;
												$scope.checkAvailabilityOfEquipment(newArr,index,1)
											}
										});
							}			
							if(!isAddedInEquipList){		
							var newArr = {};
							newArr.equipmentTypeId = $scope.equipmentTypeObj.id;
							newArr.equipmentTypeName = $scope.equipmentTypeObj.category;
							newArr.itemId = $scope.equipmentNameObj.itemId;
							newArr.equipmentName =$scope.equipmentNameObj.equipmentName;
							newArr.itemName =$scope.equipmentNameObj.itemName;
							newArr.batchId =$scope.equipmentNameObj.id;
							newArr.color= "";
							$scope.equipmentDetailList.push(newArr);
							
							$scope.checkAvailabilityOfEquipment(newArr,$scope.equipmentDetailList.length-1,1);
							}else{
								if(tempEquipListIndex.length>0){
									angular.forEach(tempEquipListIndex,function(value,index){
										$scope.tempEquipList.splice(index);
									});
								}
							}
							$scope.equipmentTypeObj.id = "";
							$scope.equipmentTypeObj.category = "";
							$scope.equipmentNameObj.id = "";
							$scope.equipmentNameObj.name = "";
							$scope.equipmentNameList = [];
							
								}
							}
					
							$scope.equipmentTypeObj.id = "";
							$scope.equipmentTypeObj.category = "";
							$scope.equipmentNameObj.id = "";
							$scope.equipmentNameObj.name = "";
							$scope.equipmentNameList = [];
							$rootScope.stopLoader();
						}catch(e){
							$rootScope.stopLoader();
							console.log("Exception",e);
						}
					}
					
					$scope.removeEquipment = function(index,flag){
						if(flag == 0){
								
								$scope.equipmentDetailList.splice(index,1);
						}else{
							
							if($scope.equipmentBatchList[index].procedureEquipmentScheduleId!=null){
								$scope.equipmentBatchList[index].status = 'I';
								 var obj = angular.copy($scope.equipmentBatchList[index]);
								   $scope.tempEquipList.push(obj);
								$scope.equipmentBatchList.splice(index, 1);
							}else{
							$scope.equipmentBatchList.splice(index,1);
							}
						}
						
					}
					
					/************************************************equipment tab End ******************************/
				
    /*************************************** Scheduling Details Tab END *********************************************************/
					$scope.thirdTabLevel = 30 ;
					$scope.isSetThirdTabLevel = function(tabNum) {
						return $scope.thirdTabLevel === tabNum;
					};
					


					$scope.setThirdTabLevel = function(newTab)
					{
						$scope.thirdTabLevel = newTab;
					};
					
					
					$scope.procedureOTScheduleList = [];
					$scope.procedureStaffScheduleList = [];
					$scope.procedureEquipmentScheduleList = [];
					$scope.newProcList = [];
					$scope.saveProcedureSchedule = function(){
						try{
							$scope.newProcList = [];
							$scope.procedureOTScheduleList = [];
							$scope.procedureStaffScheduleList = [];
							$scope.procedureEquipmentScheduleList = [];
							var trueFlag = false;
							var prolist = [];
							if($scope.commonObj.scheduleDate==undefined){
									
								trueFlag = true;
							}
							
							if(trueFlag == true){
								
								if ($scope.procedureOrderList.length > 0 || $scope.tempProcList.length > 0) {
									var counter=0;
									prolist = prolist.concat($scope.procedureOrderList);
									//only one procedure should primary in same procedure request
									angular.forEach($scope.procedureOrderList,function(value,index){
										
										if(value.procedureSequence == 1 && value.proReqId == 0){
											
											counter++;
											
										}else if(value.procedureSequence != 1 && value.proReqId != 0) {
											
											counter++;
											
										}else if(value.procedureSequence == 1 && value.proReqId != 0) {
											
											counter++;
										}
									});
									
									var saveFlagProcedureOrder = false;
									if(counter == 0 ){
										
										if($scope.tempProcList.length == 0){
										
											saveFlagProcedureOrder = true;
										}
									}
									if(saveFlagProcedureOrder){
											
										growl.error("Please select at least one Primary Procedure", {
												title : 'Error!'
											});
											$rootScope.stopLoader();
									}else{
										$scope.newProcList = [];
										
										angular.forEach($scope.procedureOrderList,function(value,index){
											
											if(value.procedureSequence == 1){
												$scope.newProcList.push(value);
												
												var oldpreferedDateTime = value.preferedDateTime.split(" ");
												var oldpreferedDate = oldpreferedDateTime[0];
												
												$scope.procedureOrderList.splice(index,1);
												angular.forEach($scope.procedureOrderList,function(value1,index1){
													var oldprocedureDateTime = value1.preferedDateTime.split(" ");
													var oldprocedureDate = oldprocedureDateTime[0];
													if(value1.procedureSequence == 1 && oldpreferedDate == oldprocedureDate){
														$scope.newProcList.push(value1);
														$scope.procedureOrderList.splice(index1,1);
													}
												});
											}
										});
										
										if($scope.procedureOrderList.length>0){
											$scope.newProcList = $scope.newProcList.concat($scope.procedureOrderList); // concat remaning procedure to newProcList
										}
										if($scope.tempProcList.length>0){
											$scope.newProcList = $scope.newProcList.concat($scope.tempProcList); // concat removed procedure to newProcList
										}
										
										console.log('$scope.procedureOrderList',JSON.stringify($scope.newProcList)); 
										if($scope.orderedProcedureList.length>0){
											
											angular.forEach($scope.orderedProcedureList,function(value,index){
												
													if((value.isEdited != undefined && value.isEdited == true) || value.procedureOrderId == ""){
													
														 $scope.newProcList.push(value); // concat remaning procedure to newProcList
												
													}
											});
										}
									}

									} else {
										
									growl.error("Please add procedure", {
										title : 'Error!'
									});
									$rootScope.stopLoader();
									return false;
										
								}
								
							}
					
							
							
							//scheduling
							
							if($scope.tempOtList.length>0){
								$scope.otList = $scope.otList.concat($scope.tempOtList);
							}
							var isValid = false;
							var isOtValid = false;
							$rootScope.startLoader();
							var otcount = 0;
							if($scope.otList.length>0){
								angular.forEach($scope.otList,function(value,index){
									var newArr = {};
									if(value.color == "red" || value.color == ""){
										isValid = true;
										isOtValid = true;
									}
									if(value.color == "green"){
										newArr.procedureOtScheduleId = value.procedureOtScheduleId != undefined?value.procedureOtScheduleId:null,
										newArr.proReqId = value.proReqId,
										newArr.theatreId = parseInt(value.selectedTheatreId),
										newArr.tableId = parseInt(value.selectedTableId);
										newArr.prodeureDate =  moment($scope.changeOfDate).format('DD-MM-YYYY HH:mm:ss'),//$scope.preferedProcDate;
										newArr.fromTime = $scope.preferedProcTime;
										newArr.toTime = $scope.finalSum;
										newArr.color = value.color;
										newArr.organizationId = $scope.organizationId;
										newArr.unitId = $scope.unitId;
										newArr.createdBy = $scope.userId;
										newArr.status = value.status;
										newArr.updatedBy = $scope.userId;
										newArr.createdDate = $scope.dt;
										newArr.updatedDate = $scope.dt;
										$scope.procedureOTScheduleList.push(newArr);
									}
								
									
								});
								
								console.log("$scope.procedureOTScheduleList111111",$scope.procedureOTScheduleList);
							}else{
								otcount = otcount + 1;
							}
							if($scope.otAddedList.length>0){
								angular.forEach($scope.otAddedList,function(value,index){
									var newArr = {};
									if(value.color == "red" || value.color == ""){
										isValid = true;
										isOtValid = true;
									}
									if(value.color == "green"){
										newArr.proReqId = null,
										newArr.theatreId = parseInt(value.selectedTheatreId),
										newArr.tableId = parseInt(value.selectedTableId);
										newArr.prodeureDate =   moment($scope.changeOfDate).format('DD-MM-YYYY HH:mm:ss');
										newArr.fromTime = $scope.preferedProcTime;
										newArr.toTime = $scope.finalSum;
										newArr.color = value.color;
										newArr.organizationId = $scope.organizationId;
										newArr.unitId = $scope.unitId;
										newArr.createdBy = $scope.userId;
										newArr.status = 'A';
										newArr.updatedBy = $scope.userId;
										newArr.createdDate = $scope.dt;
										newArr.updatedDate = $scope.dt;
										$scope.procedureOTScheduleList.push(newArr);
									}
								});
								
								console.log("$scope.procedureOTScheduleList2222222",$scope.procedureOTScheduleList);
							}else{
								otcount = otcount + 1;
							}
							
							if(otcount == 2){
								isOtValid = true;
							}
							$scope.unselectedStaff = false;
							if($scope.otAddedStaffDetailsList.length>0){
								angular.forEach($scope.otAddedStaffDetailsList,function(value,index){
									var newArr = {};
									if(value.color == "red"){
										isValid = true;
									}
									angular.forEach($scope.staffList1,function(value1,index1){
										if(value.roleId == value1.roleId){
											angular.forEach(value1.staffList,function(value2,index2){

												if(value.userId == value2.userId){
													newArr.emailId = value2.emailId;
												}
											});
										}
									});
									if(value.color != ""){
										newArr.proReqId = null,
										newArr.roleId = value.roleId,
										newArr.userId = value.userId;
										newArr.prodeureDate =   moment($scope.changeOfDate).format('DD-MM-YYYY HH:mm:ss');
										newArr.fromTime = $scope.preferedProcTime;
										newArr.toTime = $scope.finalSum;
										newArr.organizationId = $scope.organizationId;
										newArr.unitId = $scope.unitId;
										newArr.color = value.color;
										newArr.createdBy = $scope.userId;
										newArr.status = 'A';
										newArr.updatedBy = $scope.userId;
										newArr.createdDate = $scope.dt;
										newArr.updatedDate = $scope.dt;
										$scope.procedureStaffScheduleList.push(newArr);
									}else{
										$scope.unselectedStaff = true;
									}
								});
								
								console.log("procedureOTScheduleList 1111",$scope.procedureStaffScheduleList);
							}
							if($scope.tempStaffList.length>0){
								$scope.otStaffDetailsList = $scope.otStaffDetailsList.concat($scope.tempStaffList);
							}
							if($scope.otStaffDetailsList.length>0){
								angular.forEach($scope.otStaffDetailsList,function(value,index){
									var newArr = {};
									if(value.color == "red"){
										isValid = true;
									}
									angular.forEach($scope.staffList1,function(value1,index1){
										if(value.roleId == value1.roleId){
											angular.forEach(value1.staffList,function(value2,index2){
												if(value.selectedStaffId.userId == value2.userId){
													newArr.emailId = value2.emailId;
												}
											});
										}
									});
									if(value.color != ""){
										newArr.procedureStaffScheduleId = value.procedureStaffScheduleId != undefined? value.procedureStaffScheduleId : null,
										newArr.proReqId = null,
										newArr.roleId = value.roleId,
										newArr.userId = value.selectedStaffId.userId;
										newArr.prodeureDate =   moment($scope.changeOfDate).format('DD-MM-YYYY HH:mm:ss');
										newArr.fromTime = $scope.preferedProcTime;
										newArr.toTime = $scope.finalSum;
										newArr.organizationId = $scope.organizationId;
										newArr.unitId = $scope.unitId;
										newArr.color = value.color;
										newArr.createdBy = $scope.userId;
										newArr.status = value.status;
										newArr.updatedBy = $scope.userId;
										newArr.createdDate = $scope.dt;
										newArr.updatedDate = $scope.dt;
										$scope.procedureStaffScheduleList.push(newArr);
									}else{
										$scope.unselectedStaff = true;
									}
								});
								
								console.log("procedureOTScheduleList 222",$scope.procedureStaffScheduleList);
							}
								
							if($scope.unselectedStaff == true){
								$rootScope.stopLoader();
								growl.warning("Please select  Staff Name",
										{
											title : 'warning!'
										});
								 $scope.procedureOrderList = prolist;
								return false;
								
							}
							
							$scope.unselectedEquip = false;
							
							if($scope.equipmentDetailList.length>0){
								angular.forEach($scope.equipmentDetailList,function(value,index){
									var newArr = {};
									if(value.color == "red"){
										isValid = true;
									}else if(value.color == "gray"){
										isValid = true;
									}
									if(value.color != ""){
										newArr.proReqId = null,
										newArr.assetCatId = value.equipmentTypeId,
										newArr.itemId = value.itemId;
										newArr.batchId = value.batchId;
										newArr.prodeureDate = moment($scope.changeOfDate).format('DD-MM-YYYY HH:mm:ss');
										newArr.fromTime = $scope.preferedProcTime;
										newArr.toTime = $scope.finalSum;
										newArr.color = value.color;
										newArr.status = 'A';
										newArr.organizationId = $scope.organizationId;
										newArr.unitId = $scope.unitId;
										newArr.createdBy = $scope.userId;
										newArr.updatedBy = $scope.userId;
										newArr.createdDate = $scope.dt;
										newArr.updatedDate = $scope.dt;
										$scope.procedureEquipmentScheduleList.push(newArr);
									}else{
										$scope.unselectedEquip = true;
									}
								});
								
								console.log("procedureEquipmentScheduleList 111",$scope.procedureEquipmentScheduleList);
							}
								
							if($scope.tempEquipList.length>0){
								$scope.equipmentBatchList = $scope.equipmentBatchList.concat($scope.tempEquipList);
							}
							
							if($scope.equipmentBatchList.length>0){
								angular.forEach($scope.equipmentBatchList,function(value,index){
									var newArr = {};
									if(value.color == "red"){
										isValid = true;
									}else if(value.color == "gray"){
										isValid = true;
									}
									if(value.color != ""){
										newArr.procedureEquipmentScheduleId = value.procedureEquipmentScheduleId != undefined ?value.procedureEquipmentScheduleId : null,
										newArr.proReqId = null,
										newArr.assetCatId = value.equipmentTypeId,
										newArr.itemId = value.selectedItemBatchObj.itemId;
										newArr.batchId = value.selectedItemBatchObj.batchId;
										newArr.prodeureDate = moment($scope.changeOfDate).format('DD-MM-YYYY HH:mm:ss');
										newArr.fromTime = $scope.preferedProcTime;
										newArr.toTime = $scope.finalSum;
										newArr.organizationId = $scope.organizationId;
										newArr.unitId = $scope.unitId;
										newArr.createdBy = $scope.userId;
										newArr.status = value.status;
										newArr.color = value.color;
										newArr.updatedBy = $scope.userId;
										newArr.createdDate = $scope.dt;
										newArr.updatedDate = $scope.dt;
										$scope.procedureEquipmentScheduleList.push(newArr);
									}else{
										$scope.unselectedEquip = true;
									}
								});
								
								console.log("procedureEquipmentScheduleList 222",$scope.procedureEquipmentScheduleList);
							}
							
							if($scope.unselectedEquip == true){
								$rootScope.stopLoader();
								growl.warning("Please select  Equipment and batch",
										{
											title : 'warning!'
										});
								 $scope.procedureOrderList = prolist;
								return false;
							}
							
							if(isOtValid){
								$rootScope.stopLoader();
								growl.warning("OT Resources not available",
										{
											title : 'warning'
										});
								 $scope.procedureOrderList = prolist;
							}else{
							if(isValid){
								$rootScope.stopLoader();
								 $scope.procedureOrderList = prolist;
								$("#resourceNotAvailable").modal("show");
							}else{
								$scope.finalSave();
							}
							}
							
							
						}catch(e){
							console.log("Exception",e);
						}
					}
					
			$scope.previousDate = "";
			$scope.previousTime = "";
			$scope.changeDatefunc = function(){
				  $scope.startLoader();
						
						if($scope.changeOfDate == undefined || $scope.changeOfDate == ""){
							$scope.stopLoader();
							growl.warning("Please select Change Of Date ",
									{
										title : 'warning!'
									});
							return false;
						}
						
						if($scope.changeOfTime == undefined || $scope.changeOfTime == ""){
							$scope.stopLoader();
							growl.warning("Please select Change Of Time ",
									{
										title : 'warning!'
									});
							return false;
						}
						var timeLength = $scope.changeOfTime.split('');
						if(timeLength.length<8){
							$scope.changeOfTime =  $scope.changeOfTime+":00";
						}else{
							$scope.changeOfTime =  $scope.changeOfTime;
						}
						
						$scope.previousDate = $scope.changeOfDate;
						$scope.previousTime = $scope.changeOfTime;
						
						var currTime = moment(new Date()).format('HH:mm:ss');   
						var oldprocedureDate = moment($scope.changeOfDate).format('YYYY-MM-DD');
						var todayDates = moment(new Date()).format('YYYY-MM-DD');
					
						if(oldprocedureDate == todayDates && currTime > $scope.changeOfTime){
						
							growl.warning("Please select valid Time", {
								title : 'warning!'});
							$rootScope.stopLoader();
							return false;
						
							}
						
						$scope.calTim($scope.changeOfTime,$scope.totalDuration);
						
						$scope.preferedProcDate = moment($scope.changeOfDate).format('DD-MM-YYYY HH:mm:ss');
						$scope.preferedProcTime =  $scope.changeOfTime;
						angular.forEach($scope.procedureOrderList,function(value1,index){
							
							value1.preferedDateTime = moment($scope.changeOfDate).format('DD-MM-YYYY HH:mm:ss');
							value1.preferedTime = $scope.changeOfTime;
						});
						
						if($scope.commonObj.scheduleDate!= undefined && $scope.commonObj.scheduleDate!= null){
							
							angular.forEach($scope.orderedProcedureList,function(value,index){	
								$scope.commonObj.scheduleDate = moment($scope.changeOfDate).format('YYYY-MM-DD');
									if(value.isPrimarySecondary == 'Y'){
										
										$scope.oldDate = moment(moment(value.preferedDateTime,'dd-mm-yyyy').toDate()).format('YYYY-MM-DD HH:mm:ss');
										$scope.oldTime = value.preferedTime;
										$scope.rescheduleOrNot = 1;
									}
							});
						}
						
						angular.forEach($scope.staffcheckList,function(value,index){
							
							value.staffSchedulingDate =moment(moment($scope.changeOfDate,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
							value.fromTime = $scope.changeOfTime;
							value.toTime = $scope.finalSum;
						});
						
						
						angular.forEach($scope.equipcheckList,function(value,index){
							value.equipmentSchedulingDate =moment(moment($scope.changeOfDate,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
							value.fromTime = $scope.changeOfTime;
							value.toTime = $scope.finalSum;
						});
						
						var URI = BASE_URL + ROOT_URL + "/global/otims/checkStaffListAvailableOrNot"; 
						BillingGenericService.serviceAction(METHOD_POST,URI,$scope.staffcheckList,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							$scope.staffChangeDateList = response.data.listObject;
						
								angular.forEach($scope.staffChangeDateList, function(value,index) {
									$scope.startLoader();
										angular.forEach($scope.otStaffDetailsList,function(value1,index){
											if(value.roleId == value1.roleId && value1.selectedStaffId.userId == value.userId){
												value1.color  = value.color;
											}
										});
									
										angular.forEach($scope.otAddedStaffDetailsList,function(value1,index){
											if(value1.roleId == value.roleId && value1.userId == value.userId){
												value1.color  = value.color;
											}
										});
									
								});
								$scope.stopLoader();	
						});
						
						var URI = BASE_URL + ROOT_URL + "/global/otims/equipmentScheduling/checkEquipmentListAvailableOrNot"; 
						BillingGenericService.serviceAction(METHOD_POST,URI,$scope.equipcheckList,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
							$scope.equiChangeDateList = response.data.listObject;
							$scope.startLoader();
								angular.forEach($scope.equiChangeDateList, function(value,index) {
									
										angular.forEach($scope.equipmentDetailList,function(value1,index){
											if(value1.batchId == value.batchId && value1.itemId == value.itemId){
												value1.color  = value.color;
											}
										});
										
										angular.forEach($scope.equipmentBatchList,function(value1,index){
											if(value1.selectedItemBatchObj.batchId == value.batchId && value1.selectedItemBatchObj.itemId == value.itemId){
												value1.color  = value.color;
											}
										});
								});
								$scope.stopLoader();	
						});
						
						$scope.OTTObj.otDate =moment(moment($scope.changeOfDate,'DD-MM-YYYY').toDate()).format('YYYY-MM-DD');
						$scope.OTTObj.fromTime = $scope.changeOfTime;
						$scope.OTTObj.toTime = $scope.finalSum;
						$scope.checkAvailableOTSlot($scope.OTTObj,1);		
						
						$scope.stopLoader();
					
					}
				
					
					$scope.finalSave = function(){
						$scope.startLoader();
						console.log("$scope.patientObj",$scope.patientObj);
						console.log("$scope.visitTypeId",$scope.visitTypeId);
						console.log("$rootScope.registrationFieldObj",$rootScope.registrationFieldObj);

						/**************** Create Procedure list for save START **********************************/
							console.log('$scope.procedureOrderList save Data',$scope.procedureOrderList);
							//$rootScope.startLoader();
							$scope.procedureOrderList = $scope.newProcList;
						
							if($scope.tempProcList.length>0){
								$scope.newProcList = $scope.newProcList.concat($scope.tempProcList); // concat removed procedure to newProcList
							}
							
							if($scope.orderedProcedureList.length>0){
								
								angular.forEach($scope.orderedProcedureList,function(value,index){
									
										if((value.isEdited != undefined && value.isEdited == true) || value.procedureOrderId == ""){
										
											 $scope.newProcList.push(value); // concat remaning procedure to newProcList
									
										}
								});
							}
						/******************************* Procedure List for save END ***********************************/
						
						/****************************** OT Scheduling save START ***************************************/	
						
							var patientName = $scope.patientObj.firstName+" "+$scope.patientObj.middleName+" "+$scope.patientObj.lastName;
							
							if($scope.regiType==1){
								$scope.patientObj.dob = moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss');
							}
							var procedureScheduling ={
									procedureScheduleId : $scope.commonObj.scheduleId == undefined?null: $scope.commonObj.scheduleId,
									procedureOTScheduleList :$scope.procedureOTScheduleList,
									procedureStaffScheduleList :$scope.procedureStaffScheduleList,
									procedureEquipmentScheduleList : $scope.procedureEquipmentScheduleList,
									organizationId:$scope.organizationId,
									unitId:$scope.unitId,
									proReqId : $scope.saveProReqId !=0?$scope.saveProReqId:null,
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									createdBy : $scope.userId,
									status : 'A',
									updatedBy : $scope.userId,
									createdDate : $scope.dt,
									updatedDate : $scope.dt,
									procedureName : $scope.commonObj.procedureName!=undefined ? $scope.commonObj.procedureName :$scope.procedures,
									otType : $scope.OTTypes,
									procedureType : $scope.procedureTypes,
									scheduleDate : moment($scope.changeOfDate).format('DD-MM-YYYY HH:mm:ss'),
									preferTime : $scope.preferedProcTime,
									totalDuration : $scope.totalDuration,
									department : $scope.specialityName,
									departmentId : $scope.specialityId,
									proceduerStatusId : 3,
									changeProcedureDate : ($scope.changeOfDate!=null && $scope.changeOfDate!=undefined && $scope.changeOfDate!="") ?moment($scope.changeOfDate).format('DD-MM-YYYY HH:mm:ss') :$scope.preferedProcDate,
									changeTime : ($scope.changeOfTime!=null && $scope.changeOfTime!=undefined && $scope.changeOfTime!="") ?$scope.changeOfTime:$scope.preferedProcTime,
									oldDate:moment($scope.oldDate).format('YYYY-MM-DD'),
									oldTime:$scope.oldTime,
									rescheduleOrNot:$scope.rescheduleOrNot,
									patientName : $scope.commonObj.patientName == undefined?patientName: $scope.commonObj.patientName,
								    patientId : ($scope.patientObj.patientId!=undefined && $scope.patientObj.patientId!=0)? $scope.patientObj.patientId: $scope.commonObj.patientId!=undefined ? $scope.commonObj.patientId: null,

									
								};
								console.log("procedureScheduling",procedureScheduling);
								
							
						/****************************** OT Scheduling save END ***************************************/	
				if($scope.saveProReqId == 0){
					if($scope.patientObj.patientId == 0 || $scope.regiType==1){
						if($scope.patientObj.prefixId == ""){
							$scope.stopLoader();
							growl.warning("Please select Prefix",
									{
										title : 'warning!'
									});
							return false;
						}	
						
						if($scope.patientObj.firstName==""){
							$scope.stopLoader();
							growl.warning("Please Enter First Name",
									{
										title : 'warning!'
									});
							return false;
						}	
						
						if($scope.patientObj.lastName==""){
							$scope.stopLoader();
							growl.warning("Please Enter Last Name",
									{
										title : 'warning!'
									});
							return false;
						}	
						
						if($scope.patientObj.genderId==""){
							$scope.stopLoader();
							growl.warning("Please select Gender",
									{
										title : 'warning!'
									});
							return false;
						}	
						
						if($scope.patientObj.email==""){
							$scope.stopLoader();
							growl.warning("Please Enter email",
									{
										title : 'warning!'
									});
							return false;
						}	
						
						if($scope.patientObj.mobileNumber==""){
							$scope.stopLoader();
							growl.warning("Please select Mobile Number",
									{
										title : 'warning!'
									});
							return false;
						}	
						
						if($scope.patientObj.dob==""){
							$scope.stopLoader();
							growl.warning("Please Enter Birth Date",
									{
										title : 'warning!'
									});
							return false;
						}	
						
						if($scope.age==""){
							$scope.stopLoader();
							growl.warning("Please Enter Age",
									{
										title : 'warning!'
									});
							return false;
						}	
						
					}
				}
					
						var finalObj = {
								otReservationId : $scope.commonObj.otReservationId!=undefined ?$scope.commonObj.otReservationId:null, 
								proReqId : $scope.saveProReqId !=0?$scope.saveProReqId:null,
								patientId : ($scope.patientObj.patientId!=undefined && $scope.patientObj.patientId!=0)? $scope.patientObj.patientId: $scope.commonObj.patientId!=undefined ? $scope.commonObj.patientId: null,
//								admissionId : null,
	                            admissionId  :  $scope.commonObj.admissionId,
								encounterId : null,
								visitTypeId : $scope.visitTypeId,
								scheduleId : null,
								reservationStatusId : 1,
								status : 'A',
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								createdBy : $scope.userId,
								createdDate : $scope.dt,
								updatedBy : $scope.userId,
								updatedDate : $scope.dt,
								procedureOrderMasterDto : $scope.newProcList,
								procedureSchedulingDto : procedureScheduling,
								patientRegistrationDto : $scope.patientObj,
						}		
						
						console.log("finalObj",finalObj);
						
						var URI = BASE_URL + ROOT_URL + "/api/otims/OTReservation/saveOTReservation";
						
						GenericService.serviceAction(METHOD_POST, URI, finalObj).then(function (response) {
							if (response.data.status == "success"){
								$scope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
										$scope.commonObj = {};
										$scope.orderedProcedureList = [];
										$scope.tempOtList = [];
										$scope.tempEquipList = [];
										$scope.tempStaffList = [];
										$scope.initPatientObj();
										$scope.initProcedureOrder();
										$scope.getOTIMSStaffList();
										
							} else if(response.data.status == 'error')
							{
								$scope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
						});
					}					
					
					$scope.firstLevelTab = 001;
					$scope.secondLevelTab = 111;
					
					$scope.setSecondTabLevel = function(tabNum){
						$scope.secondLevelTab = tabNum;
					} 
					
					$scope.isSetFirstTabLevel = function(newTab){
						return $scope.firstLevelTab==newTab;
					}
					
					$scope.isSetSecondTabLevel = function(newTab){
						return $scope.secondLevelTab==newTab;
					}
					
					$scope.setFirstTabLevel = function(tabNum){
						$scope.firstLevelTab = tabNum;
						
						if(tabNum==002){
							
							$scope.setSecondTabLevel(111);
						}
					}
					
					
					//$scope.setFirstTabLevel(001);
					
					  if($scope.commonObj.isFromReservationCancellation!=undefined && $scope.commonObj.isFromReservationCancellation){
				        	$scope.setFirstTabLevel(002);
				        	$scope.getActiveOperationTheatreList()
				        }else{
				        	$scope.setFirstTabLevel(001);
				        }
					
					$scope.switchTab = function(){
						$("#switchTab").modal("show");
					} 
					
					$scope.yesSwitch = function(){
						$scope.setFirstTabLevel(001);
					}
					
					$scope.registerPatientChange = function(){
						if($scope.regiType==2){
							 $scope.initPatientObj();
						}else{
							
						}
					}
					
					$scope.cancelChangeDate = function(){
						if($scope.previousDate!="" && $scope.previousTime !=""){
							 $scope.changeOfDate = $scope.previousDate;
							  $scope.changeOfTime  = $scope.previousTime ;
						}
					}
					
					//change procedure sequence
					$scope.changeProcedureSequence = function(index,flag){
						if(flag == 1){
							var isprocedureSequenceExist = false;
							if($scope.orderedProcedureList!=null && $scope.orderedProcedureList.length>0){
								angular.forEach($scope.orderedProcedureList,function(v1,i1){
									angular.forEach($scope.procedureOrderList,function(value,idx){
										if(idx!=index){
											if(v1.proReqId == value.proReqId && v1.procedureSequence == value.procedureSequence){
												isprocedureSequenceExist = true;
											}
										}
									});
								});
							}
							
							if(isprocedureSequenceExist){
								 growl.warning("Selected Procedure Sequence Is Already Exist", {
                                        title : 'warning!'
                                    });
								
								$scope.procedureOrderList[index].procedureSequence = $scope.procedureOrderedSequenceList[0].id;
								return false;
							}
							
							var isDuplicatePrimary = false;
							var isDuplicateSecondary = false;
							var isDuplicateTertiary = false;
							var isDuplicateQuaternary = false;
							var isDuplicateQuinary = false;
							
							angular.forEach($scope.procedureOrderList,function(value,idx){
								if(idx!=index && $scope.procedureOrderList[index].procedureSequence==1){
									if($scope.procedureOrderList[index].proReqId == value.proReqId && value.procedureSequence == 1){
										isDuplicatePrimary = true;
									}
								}else if(idx!=index && $scope.procedureOrderList[index].procedureSequence==2){
									if($scope.procedureOrderList[index].proReqId == value.proReqId && value.procedureSequence == 2){
										isDuplicateSecondary = true;
									}
								}else if(idx!=index && $scope.procedureOrderList[index].procedureSequence==3){
									if($scope.procedureOrderList[index].proReqId == value.proReqId && value.procedureSequence == 3){
										isDuplicateTertiary = true;
									}
								}else if(idx!=index && $scope.procedureOrderList[index].procedureSequence==4){
									if($scope.procedureOrderList[index].proReqId == value.proReqId && value.procedureSequence == 4){
										isDuplicateQuaternary = true;
									}
								}else if(idx!=index && $scope.procedureOrderList[index].procedureSequence==5){
									if($scope.procedureOrderList[index].proReqId == value.proReqId && value.procedureSequence == 5){
										isDuplicateQuinary = true;
									}
								}
							});
							
							if(isDuplicatePrimary){
								 growl.warning("Primary Procedure Is Already Exist", {
                                        title : 'warning!'
                                    });
								
								$scope.procedureOrderList[index].procedureSequence = $scope.procedureOrderedSequenceList[0].id;
								return false;
							}
							
							if(isDuplicateSecondary){
								 growl.warning("Secondary Procedure Is Already Exist", {
                                        title : 'warning!'
                                    });
								
								$scope.procedureOrderList[index].procedureSequence = $scope.procedureOrderedSequenceList[0].id;
								return false;
							}
							
							if(isDuplicateTertiary){
								 growl.warning("Tertiary Procedure Is Already Exist", {
                                        title : 'warning!'
                                    });
								
								$scope.procedureOrderList[index].procedureSequence = $scope.procedureOrderedSequenceList[0].id;
								return false;
							}
							
							if(isDuplicateQuaternary){
								 growl.warning("Quaternary Procedure Is Already Exist", {
                                        title : 'warning!'
                                    });
								
								$scope.procedureOrderList[index].procedureSequence = $scope.procedureOrderedSequenceList[0].id;
								return false;
							}
							
							if(isDuplicateQuinary){
								 growl.warning("Quinary Procedure Is Already Exist", {
                                        title : 'warning!'
                                    });
								
								$scope.procedureOrderList[index].procedureSequence = $scope.procedureOrderedSequenceList[0].id;
								return false;
							}
							$scope.procedureOrderList[index].procedureSequence = parseInt($scope.procedureOrderList[index].procedureSequence);
							if($scope.procedureOrderList[index].procedureSequence == 1){
								$scope.procedureOrderList[index].isPrimarySecondary = 'Y';
								
								var procedureReqMaster = angular.copy($scope.procedureReqMaster);
								procedureReqMaster.patientId = $scope.patientObj.patientId;
								procedureReqMaster.visitTypeId = $scope.visitTypeId;
								procedureReqMaster.encounterId = null;
								procedureReqMaster.preferedDateTime =  moment($scope.procedureDate).format('DD-MM-YYYY HH:mm:ss');
								$scope.procedureOrderList[index].procedureReqMasterDto = procedureReqMaster;
								
							}else{
								$scope.procedureOrderList[index].isPrimarySecondary = 'N';
								$scope.procedureOrderList[index].procedureReqMasterDto = {};
							}
							
						}else{
							var isDuplicatePrimary = false;
							var isDuplicateSecondary = false;
							var isDuplicateTertiary = false;
							var isDuplicateQuaternary = false;
							var isDuplicateQuinary = false;
							angular.forEach($scope.orderedProcedureList,function(value,idx){
								if(idx!=index && $scope.orderedProcedureList[index].procedureSequence==1){
									if($scope.orderedProcedureList[index].proReqId == value.proReqId && value.procedureSequence == 1){
										isDuplicatePrimary = true;
									}
								}else if(idx!=index && $scope.orderedProcedureList[index].procedureSequence==2){
									if($scope.orderedProcedureList[index].proReqId == value.proReqId && value.procedureSequence == 2){
										isDuplicateSecondary = true;
									}
								}else if(idx!=index && $scope.orderedProcedureList[index].procedureSequence==3){
									if($scope.orderedProcedureList[index].proReqId == value.proReqId && value.procedureSequence == 3){
										isDuplicateTertiary = true;
									}
								}else if(idx!=index && $scope.orderedProcedureList[index].procedureSequence==4){
									if($scope.orderedProcedureList[index].proReqId == value.proReqId && value.procedureSequence == 4){
										isDuplicateQuaternary = true;
									}
								}else if(idx!=index && $scope.orderedProcedureList[index].procedureSequence==5){
									if($scope.orderedProcedureList[index].proReqId == value.proReqId && value.procedureSequence == 5){
										isDuplicateQuinary = true;
									}
								}
							});
							if(isDuplicatePrimary){
								 growl.warning("Primary Procedure Is Already Exist", {
                                        title : 'warning!'
                                    });
								
								$scope.orderedProcedureList[index].procedureSequence = $scope.procedureSequenceList[0].id;
								return false;
							}
							
							if(isDuplicateSecondary){
								 growl.warning("Secondary Procedure Is Already Exist", {
                                        title : 'warning!'
                                    });
								
								$scope.orderedProcedureList[index].procedureSequence = $scope.procedureSequenceList[0].id;
								return false;
							}
							
							if(isDuplicateTertiary){
								 growl.warning("Tertiary Procedure Is Already Exist", {
                                        title : 'warning!'
                                    });
								
								$scope.orderedProcedureList[index].procedureSequence = $scope.procedureSequenceList[0].id;
								return false;
							}
							
							if(isDuplicateQuaternary){
								 growl.warning("Quaternary Procedure Is Already Exist", {
                                        title : 'warning!'
                                    });
								
								$scope.orderedProcedureList[index].procedureSequence = $scope.procedureSequenceList[0].id;
								return false;
							}
							
							if(isDuplicateQuinary){
								 growl.warning("Quinary Procedure Is Already Exist", {
                                        title : 'warning!'
                                    });
								
								$scope.orderedProcedureList[index].procedureSequence = $scope.procedureSequenceList[0].id;
								return false;
							}
							
							if($scope.orderedProcedureList[index].procedureSequence == 1){
								$scope.orderedProcedureList[index].isPrimarySecondary = 'Y';
								$scope.commonObj.operationTheatreTypeId = $scope.orderedProcedureList[index].operationTheatreTypeId;
							}else{
								$scope.orderedProcedureList[index].isPrimarySecondary = 'N';
							}
							$scope.orderedProcedureList[index].isEdited = true;
						}
					}
					
					//----------------------------Procedure order cancellation START --------------------------------------------------------------//	
					
					$scope.procedureCancellationReasonList = [];
					$scope.getReasonListByReasonTypeId = function(){
						var URI = BASE_URL + ROOT_URL+ "/api/common/getReasonMasterListByReasonTypeId";	
						var data = {
								reasonTypeId : 16,
								organizationId : $scope.organizationId
									}
						GenericService.serviceAction("POST", URI, data).then(
										function(response) {
											$scope.procedureCancellationReasonList = [];
											if (response.data.status == "success") {
												$scope.procedureCancellationReasonList = response.data.listObject;
											}
										});
					}
					$scope.getReasonListByReasonTypeId();
				
					//----------------------------Procedure order cancellation END --------------------------------------------------------------//	
			}]);
							