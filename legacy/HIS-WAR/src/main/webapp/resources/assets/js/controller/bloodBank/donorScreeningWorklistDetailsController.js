angular
		.module('myApp')
		.controller(
				'donorScreeningWorklistDetailsController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'GenericServiceParamHeader',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'$stateParams',
						function($scope, $rootScope, $state, $http,
								GenericService,GenericServiceParamHeader, $sessionStorage,$cookies, growl, $localStorage,$stateParams) {
							
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							$scope.donorDetailsObj = {
									heightInFt:"",
									heightInInches:"",
									donorWeight:"",
									weightType:"",
									age:"",
									donorTemperature:"",
									temperatureType:"",
									donorPulse:"",
									donorHB:"",
									donorBPSystolic:"",
									donorId:"",
									donorHeight:"",
									donorBpDiastolic:"",
									hbDoneById:"",
									hBType:"",
									bleedingDoneById:"",
									screeningResultStatusId:"",
									deferralDays:"",
									screeningRemarks:"",
									donorScreeningMapperList:[],
									donorScreeningId:0,
									exportedToBagCollection:false,
									organizationId: $scope.organizationId,
									unitId: $scope.unitId,
							}
							
							console.log("$scope.donorDetailsObj",$scope.donorDetailsObj);

							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
	
							$scope.common = $stateParams.donorObjResult;
							$scope.donorDetailsObj.donorId = $scope.common.donorId;
							$scope.dId = $scope.common.donorId;
							console.log("com",$scope.common);
							
						
							
							$scope.openFromDatePicker1 = function() {
								$scope.fromDatepicker1.opened = true;
							};

							$scope.fromDatepicker1 = {
									opened : false
							};
							
							$scope.datepickervaccinePicker = function(index) {

								for (var i = $scope.temporaryList.length - 1; i >= 0; i--) {
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
								
							$scope.parameterList = [];
							$scope.temporaryList = [];
							$scope.permanentList = [];
							$scope.getparameterList = function() {

								var data = {
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									genderId : $scope.common.genderId
								}
								var URI = BASE_URL
										+ ROOT_URL
										+ "/global/bloodBank/getDonorScreeningListByGenderId";
								GenericService
										.serviceAction(METHOD_POST, URI, data)
										.then(
												function(response) {

													if (response.data.status == "success") {
														$scope.parameterList = response.data.listObject;
														console
																.log($scope.parameterList);
														angular
																.forEach(
																		$scope.parameterList,
																		function(
																				value,
																				key) {
																			var data1 = {
																				parameter : "",
																				deferredTypeId : "",
																				deferredType : "",
																				genderId : "",
																				gender : "",
																				radioId : null,
																				id:"",
																				donorDeferralTillDate : ""
																			};
																			if (value.deferredTypeId == 1) {
																				data1.id = value.id;
																				data1.parameter = value.parameter;
																				data1.deferredTypeId = value.deferredTypeId;
																				data1.deferredType = value.deferredType;
																				data1.genderId = value.genderId;
																				data1.gender = value.gender;
																				//data1.radioId = false;
																				//data1.donorDeferralTillDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
																				$scope.temporaryList
																						.push(data1);
																				
																			} else {
																				data1.id = value.id;
																				data1.parameter = value.parameter;
																				data1.deferredTypeId = value.deferredTypeId;
																				data1.deferredType = value.deferredType;
																				data1.genderId = value.genderId;
																				data1.gender = value.gender;
																				//data1.radioId = false;

																				$scope.permanentList
																						.push(data1);
																				
																			
																			}

																		});

													}
												});
								//$scope.donorDetailsObj.screeningResultStatusId=1;
								console.log("$scope.temporaryList",$scope.temporaryList);
								console.log("$scope.permanentList",$scope.permanentList);
							}
							
							if($scope.common.genderId!=null && $scope.common.genderId!=undefined)
							{
								$scope.getparameterList();
							}
							$scope.obj={
									temporaryParam:[],
									permanentParam:[]
							};
							
						//	$scope.dHeight=[];
							
							$scope.donorScreening={};
							$scope.DonorScreeningParameterList=[];
							$scope.getDonorScreeningDetails = function() {
								
								$scope.startLoader();
								var URI = BASE_URL+ ROOT_URL+ "/api/bloodbank/getDonorScreeningDetailByDonorId/"
										+ $scope.common.donorId + "/"
										+ $scope.organizationId + "/"
										+ $scope.unitId;
								GenericService.serviceAction(METHOD_GET, URI)
										.then(function(response) {
													if (response.data.status == "success") {
														$scope.stopLoader();
														console.log("response.data.object",response.data.object);
														$scope.calculateAge();
														$scope.getVitalList();
														if(response.data.object!=null ){
														$scope.donorDetailsObj = response.data.object;
														if($scope.donorDetailsObj.deferralDays!=null)
														{
															$scope.donorDetailsObj.deferralDays=new Date($scope.donorDetailsObj.deferralDays);
															console.log("DD",$scope.donorDetailsObj.deferralDays);
															//$scope.donorDetailsObj.deferralDays= moment($scope.donorDetailsObj.deferralDays).format('dd/mm/yyyy');
															
														}else{
															//$scope.donorDetailsObj.deferralDays=new Date("DD/MM/YYYY");
														}
														
														var height=$scope.donorDetailsObj.donorHeight.toString();;
														//$scope.dHeight.push(height.split("."));
														$scope.dHeight=height.split(".");
														$scope.donorDetailsObj.heightInFt=$scope.dHeight[0];
														$scope.donorDetailsObj.heightInInches=$scope.dHeight[1];
														$scope.DonorScreeningParameterList = $scope.donorDetailsObj.donorScreeningMapperList;
														console.log("OBJ______",$scope.donorDetailsObj);
														console.log("OBJ______",$scope.donorDetailsObj);
														$scope.screeningResultStatusId=$scope.donorDetailsObj.screeningResultStatusId;
														if ($scope.donorDetailsObj != null) {
															$scope.temporaryList = [];
															$scope.permanentList = [];
															
															angular.forEach($scope.parameterList,function(value,key) {
																		flag = false;
																		var data1 = {
																				parameter : "",
																				deferredTypeId : "",
																				deferredType : "",
																				genderId : "",
																				gender : "",
																				id : "",
																				donorDeferralTillDate : "",
																				parameterResultStatus : ""
																				}
																		angular.forEach($scope.DonorScreeningParameterList,function(value1,key1) {

																				
																				if (value1.donorScreeningParameterId == value.id) {
																						flag = true;
																						if (value.deferredTypeId == 1) {
																								data1.id = value.id;
																								data1.parameter = value.parameter;
																								data1.deferredTypeId = value.deferredTypeId;
																								data1.donorScreeningDetailMapperId=value1.donorScreeningDetailMapperId;
																								data1.deferredType = value.deferredType;
																								data1.genderId = value.genderId;
																								data1.gender = value.gender;
																								data1.parameterResultStatus = value1.parameterResultStatus;
																								if(value1.parameterResultStatus=="Y")
																								{
																									data1.radioId = true;
																									data1.donorDeferralTillDate= new Date(value1.donorDeferralTillDate);
																								}else if(value1.parameterResultStatus=="N")
																								{
																									data1.radioId = false;
																								}
																								
																								$scope.temporaryList.push(data1);
																							//	$scope.obj.temporaryParam.push(data1);
																								
																							} else {
																								
																								data1.id = value.id;
																								data1.parameter = value.parameter;
																								data1.donorScreeningDetailMapperId=value1.donorScreeningDetailMapperId;
																								data1.deferredTypeId = value.deferredTypeId;
																								data1.deferredType = value.deferredType;
																								data1.genderId = value.genderId;
																								data1.gender = value.gender;
																								data1.parameterResultStatus = value1.parameterResultStatus;
																								if(value1.parameterResultStatus=="Y")
																								{
																									data1.radioId = true;
																								}
																								else if(value1.parameterResultStatus=="N")
																								{
																									data1.radioId = false;
																								}/*else
																								{
																									data1.radioId = false;
																								}*/
																								$scope.permanentList.push(data1);
																								//$scope.obj.permanentParam.push(data1);
																							}

																					}

																		});

																		if (flag == false) {
																			
																				if (value.deferredTypeId == 1) {
																						data1.id = value.id;
																						data1.parameter = value.parameter;
																						data1.deferredTypeId = value.deferredTypeId;
																						data1.deferredType = value.deferredType;
																						data1.genderId = value.genderId;
																						data1.gender = value.gender;
																						//data1.radioId = false;
																						//$scope.donorDetailsObj.screeningResultStatus='A';
																						$scope.temporaryList.push(data1);
																					} else {
																						data1.id = value.id;
																						data1.parameter = value.parameter;
																						data1.deferredTypeId = value.deferredTypeId;
																						data1.deferredType = value.deferredType;
																						data1.genderId = value.genderId;
																						data1.gender = value.gender;
																						//data1.radioId = false;
																						//$scope.donorDetailsObj.screeningResultStatus='A';
																						$scope.permanentList
																								.push(data1);
																					}

																		}
															 });
															console.log("permanentList________",$scope.permanentList);
															console.log("temporaryList",$scope.temporaryList);
														}
														$scope.stopLoader();

													}
													
											}	
													$scope.stopLoader();
												});
							
							}
							
							if($scope.common.donorId!=null && $scope.common.donorId!=undefined)
							{
								$scope.getDonorScreeningDetails();
							}
							
							//get selected ParameterList
							$scope.finalParameterList = [];
							
							$scope.screeningResultStatusId="";
							
							
							$scope.temporaryParamNoList=[];
							$scope.permanentParamNoList=[];
							$scope.getSelectedParameter = function(info,isSelected,sts)
							{
								
								console.log("info",info);
							
								console.log("isSelected",isSelected);
								console.log("sts",sts);
								
								if(isSelected && (sts=='T')){
									$scope.obj.temporaryParam.push(info);
									$scope.temporaryParamNoList.splice(info, 1);
								}else if(sts=='T'){
									info.donorDeferralTillDate="";
									$scope.obj.temporaryParam.splice(info, 1);
									$scope.temporaryParamNoList.push(info);
								}
								
								if(isSelected && (sts=='P')){
									$scope.obj.permanentParam.push(info);
									
									$scope.permanentParamNoList.splice(info, 1);
								}else if(sts=='P'){
									info.donorDeferralTillDate="";
									$scope.obj.permanentParam.splice(info, 1);
									$scope.permanentParamNoList.push(info);
								}
								console.log("Permanent List Selected:",$scope.obj.permanentParam);
								console.log("Te List Selected:",$scope.obj.permanentParam);
								
								if($scope.obj.permanentParam.length > 0 ){
									$scope.screeningResultStatusId=3;//3="P"
								}
								else if($scope.obj.temporaryParam.length >0){
									$scope.screeningResultStatusId=2;//2="R"
								}
								else{
									$scope.screeningResultStatusId=1;//1="A"
									$scope.donorDetailsObj.deferralDays="";
									
								}
						
								console.log("$scope.screeningResultStatusId",$scope.obj.permanentParam);
								console.log("$scope.screeningResultStatusId",$scope.obj.temporaryParam);
								console.log("$scope.screeningResultStatusId",$scope.screeningResultStatusId);
								
							}
							
							$scope.ageValidation=function(){
								
								if(	$scope.donorDetailsObj.age <= 18  || $scope.donorDetailsObj.age >= 65){
									$scope.stopLoader();
									growl.warning("Age should be Greater than 18 and Less than 65",
											{
												title : 'warning!'
											});
									$scope.screeningResultStatusId=2;//2="R"
								}else{
									$scope.screeningResultStatusId=1;//1="A"
								}
							}
							
							$scope.getVitalList = function() {
                                $rootScope.startLoader();

                                var headerObj={
										"unit_id" : $scope.unitId,
										"organization_id" : $scope.organizationId
								};
								var paramObjForPer={

								};
								var data={};
							 var URI = BASE_URL + ROOT_URL
                             + GETVITALMASTERLIST;
							 GenericServiceParamHeader
                             .serviceAction(METHOD_GET, URI, data,
                                     NOTIFICATION_MSG_STATUS_FALSE,
                                     paramObjForPer, headerObj)
                             .then(
                                     function(response) {
                                         console.log(response);
                                         // $rootScope.startLoader();
                                         if (response.data.status == "success")
                                        	 $scope.vitalsList = [];
                                        	 if($scope.patientDetails.isIcuu == true){
                                        		 $scope.vitalsList = response.data.listObject;
                                             	 $scope.printListForVitals();
                                        	 }else{
                                        		 angular.forEach(response.data.listObject,function(value,key){
                                        			 if(value.isIcu == false){
                                        				 $scope.vitalsList.push(value);
                                        			 }
                                        		 });
//                                             	 $scope.printListForVitals();
                                        	 }

                                         console.log("Vital List", $scope.vitalsList);


                                     });
                         $rootScope.stopLoader();
						}
						
							
							$scope.calculateAge = function() {
							    var dob = $scope.common.dob;
							    // Validate input format
							    var dobRegex = /^(0[1-9]|[12][0-9]|3[01])\/(0[1-9]|1[0-2])\/\d{4}$/; // DD/MM/YYYY format
							    if (!dob.match(dobRegex)) {
							        console.error("Invalid date of birth format. Please use DD/MM/YYYY.");
							        return null;
							    }
							
							    // Parse date components
							    var dobArray = dob.split('/');
							    var dobDay = parseInt(dobArray[0], 10);
							    var dobMonth = parseInt(dobArray[1], 10);
							    var dobYear = parseInt(dobArray[2], 10);
							
							    // Calculate age
							    var today = new Date();
							    var age = today.getFullYear() - dobYear;
							    var m = today.getMonth() + 1 - dobMonth; // Note: Adding 1 to get month in 1-12 range
							    
							    // Check if birthday has occurred this year
							    if (m < 0 || (m == 0 && today.getDate() < dobDay)) {
							        age--;
							    }
							
							   $scope.donorDetailsObj.age = age;
							};

	             	           $scope.Temperature = 0;
	             	           $scope.donorDetailsObj.temperatureType='C';
	             	           
	             	           
	             	          $scope.calculateTemeperature = function(){
	             	      	   if($scope.donorDetailsObj.temperatureType =="C"){
	             	      		   $scope.donorDetailsObj.donorTemperature =(($scope.donorDetailsObj.donorTemperature-32)*5/9).toFixed(2); 
	             	      	   }
	             	      	   if($scope.donorDetailsObj.temperatureType =="F"){
	             	      		   $scope.donorDetailsObj.donorTemperature =(($scope.getinitTemprature * 9 / 5) + 32).toFixed(2);
	             	      	   }
	             	         	 $scope.temperatureValidation();
	             	           }
	             	       
	             	         $scope.getinitTemprature = '';
							$scope.getin = function()
							{
								$scope.getinitTemprature =  $scope.donorDetailsObj.donorTemperature;
								if($scope.getinitTemprature==undefined || $scope.getinitTemprature==null || $scope.getinitTemprature=="" ){									   
									growl.warning("Please Enter Temprature", {
							                    title: 'warning!'
							                });
							                return false;
								}
								else{
									$scope.temperatureValidation();
								}
							}
							
							
							$scope.temperatureValidation = function() {
								 if($scope.donorDetailsObj.temperatureType =="C"){
									 if ($scope.getinitTemprature <= 0) {
							                $scope.stopLoader();
							                growl.warning("Temperature should be Greater than 0 °C", {
							                    title: 'warning!'
							                });
							                $scope.donorDetailsObj.donorTemperature="";
							                return false;
							            }
									 else if ($scope.getinitTemprature >= 37) {
							                $scope.stopLoader();
							                growl.warning("Temperature should be Less than 37 °C", {
							                    title: 'warning!'
							                });
							                $scope.donorDetailsObj.donorTemperature="";
							                return false; 
							            }
								 }
								   if($scope.donorDetailsObj.temperatureType =="F"){
									   $scope.donorDetailsObj.donorTemperature =(($scope.getinitTemprature * 9 / 5) + 32).toFixed(2);
									   if ( $scope.donorDetailsObj.donorTemperature <= 32) {
							                $scope.stopLoader();
							                growl.warning("Temperature should be Greater than 0 °F", {
							                    title: 'warning!'
							                });
							                $scope.donorDetailsObj.donorTemperature="";
							                return false;
							            }
									   else if ( $scope.donorDetailsObj.donorTemperature > 98.6) {
							                $scope.stopLoader();
							                growl.warning("Temperature should be Less than 98.6 °F", {
							                    title: 'warning!'
							                });
							                $scope.donorDetailsObj.donorTemperature="";
							                return false; 
							            }
								   }
							}
						
							$scope.pulseValidation = function() {
								angular.forEach($scope.vitalsList, function(value) {
								    if (value.vitalId == 4) {
								        if ($scope.donorDetailsObj.donorPulse == null || $scope.donorDetailsObj.donorPulse === undefined || $scope.donorDetailsObj.donorPulse === '') {
								            $scope.stopLoader();
								            growl.warning("Please Enter Pulse", {
								                title: 'warning!'
								            });
								            return false;
								        } else if ($scope.donorDetailsObj.donorPulse <= parseInt(value.minValue)) {
								            $scope.stopLoader();
								            growl.warning("Pulse should be Greater than " + value.minValue + " bpm", {
								                title: 'warning!'
								            });
								            $scope.screeningResultStatusId = 2; // 2="R"
								            $scope.donorDetailsObj.donorPulse ="";
								            return false;
								        } else if ($scope.donorDetailsObj.donorPulse >= parseInt(value.maxValue)) {
								            $scope.stopLoader();
								            growl.warning("Pulse should be Less than " + value.maxValue + " bpm", {
								                title: 'warning!'
								            });
								            $scope.screeningResultStatusId = 1; // 1="A"
								            $scope.donorDetailsObj.donorPulse="";
								            return false;
								        }
								    }
								});

							}

	             	
								$scope.sBPValidation = function() {
								    angular.forEach($scope.vitalsList, function(value) {
								        if (value.vitalId == 3) {
								        	if ($scope.donorDetailsObj.donorBPSystolic  == null || $scope.donorDetailsObj.donorBPSystolic  === undefined || $scope.donorDetailsObj.donorBPSystolic  === '') {
									            $scope.stopLoader();
									            growl.warning("Please Enter Blood Pressure (Systolic)", {
									                title: 'warning!'
									            });
									            return false;
								        	}else if ($scope.donorDetailsObj.donorBPSystolic <= parseInt(value.minValue)) {
								                $scope.stopLoader();
								                growl.warning("Blood Pressure (Systolic) should be Greater than 0 sbp", {
								                    title: 'warning!'
								                });
								                $scope.screeningResultStatusId = 2; // 2="R"
								                $scope.donorDetailsObj.donorBPSystolic = "";
								                return false; 
								            } else if ($scope.donorDetailsObj.donorBPSystolic >= parseInt(value.maxValue)) {
								                $scope.stopLoader();
								                growl.warning("Blood Pressure (Systolic) should be Less than "+ value.maxValue + " sbp", {
								                    title: 'warning!'
								                });
								                $scope.screeningResultStatusId = 1; // 1="A"
								                $scope.donorDetailsObj.donorBPSystolic ="";
								                return false; 
								            }
								        }
								    });
								}
							     $scope.dSPValidation = function() {
									    angular.forEach($scope.vitalsList, function(value) {
									        if (value.vitalId == 3) {
									        	if ($scope.donorDetailsObj.donorBpDiastolic  == null || $scope.donorDetailsObj.donorBpDiastolic  === undefined || $scope.donorDetailsObj.donorBpDiastolic  === '') {
										            $scope.stopLoader();
										            growl.warning("Please Enter Blood Pressure (Diastolic)", {
										                title: 'warning!'
										            });
										            return false;
									        	}
									        	else if ($scope.donorDetailsObj.donorBpDiastolic <= parseInt(value.minValue)) {
									                $scope.stopLoader();
									                growl.warning("Blood Pressure (Diastolic) should be Greater than 0 dbp", {
									                    title: 'warning!'
									                });
									                $scope.screeningResultStatusId = 2; // 2="R"
									                $scope.donorDetailsObj.donorBpDiastolic="";
									                return false; 
									            } else if ($scope.donorDetailsObj.donorBpDiastolic >= parseInt(value.maxValue)) {
									                $scope.stopLoader();
									                growl.warning("Blood Pressure (Diastolic) should be Less than "+ value.maxValue + " dbp", {
									                    title: 'warning!'
									                });
									                $scope.screeningResultStatusId = 1; // 1="A"
									                $scope.donorDetailsObj.donorBpDiastolic="";
									                return false;
									            }
									        }
									    });
									}
	           
	             	         
	             	         
//	             	    	$scope.temperatureValidation=function(){
//								
//								if($scope.donorDetailsObj.temperatureType == 'C')
//								{
//									
//									if(	$scope.donorDetailsObj.donorTemperature<37.6)
//									{
//										$scope.stopLoader();
//										growl.warning("Temperature should be Greater than or Equal to 37.6 °C",
//											{
//												title : 'warning!'
//											});
//										$scope.screeningResultStatusId=2;//2="R"
//									}else{
//										$scope.screeningResultStatusId=1;//1="A"
//									}
//								}else if($scope.donorDetailsObj.temperatureType == 'F'){
//										
//									if(	$scope.donorDetailsObj.donorTemperature < 99.5){
//										
//										$scope.stopLoader();
//										growl.warning("Temperature should be Greater than or Equal to 99.5 °F",
//											{
//												title : 'warning!'
//											});
//										$scope.screeningResultStatusId=2;//2="R"
//									}else{
//										$scope.screeningResultStatusId=1;//1="A"
//									}
//								}else{
//									$scope.stopLoader();
//									growl.warning("please Select Temperature Type",
//										{
//											title : 'warning!'
//										});
//								}
//								
//							}
							
//							$scope.pulseValidation=function(){
//								
//								if(	$scope.donorDetailsObj.donorPulse < 50  || $scope.donorDetailsObj.donorPulse >100){
//									$scope.stopLoader();
//									growl.warning("Pulse should be Greater than 50 and Less than 100",
//											{
//												title : 'warning!'
//											});
//									$scope.screeningResultStatusId=2;//2="R"
//								}else{
//									$scope.screeningResultStatusId=1;//1="A"
//								}
//								
//							}
//							$scope.sBPValidation=function(){
//								
//								if(	$scope.donorDetailsObj.donorBPSystolic < 100  || $scope.donorDetailsObj.donorBPSystolic >140){
//									$scope.stopLoader();
//									growl.warning("BPSystolic should be Greater than 100 and Less than 140",
//											{
//												title : 'warning!'
//											});
//									$scope.screeningResultStatusId=2;//2="R"
//								}else{
//									$scope.screeningResultStatusId=1;//1="A"
//								}
//							}
//							$scope.dSPValidation=function(){
//								
//								if(	$scope.donorDetailsObj.donorBpDiastolic < 60  || $scope.donorDetailsObj.donorBpDiastolic >90){
//									$scope.stopLoader();
//									growl.warning("BpDiastolic should be Greater than 60 and Less than 90",
//											{
//												title : 'warning!'
//											});
//									$scope.screeningResultStatusId=2;//2="R"
//								}else{
//									$scope.screeningResultStatusId=1;//1="A"
//								}
//							}
							
							$scope.weightValidation=function(){
								
								if($scope.donorDetailsObj.weightType == 'Kg')
								{
									
									if(	$scope.donorDetailsObj.donorWeight < 47)
									{
										$scope.stopLoader();
										growl.warning("weight should be Greater than or Equal to 47 Kg",
											{
												title : 'warning!'
											});
										$scope.screeningResultStatusId=2;//2="R"
									}else{
										$scope.screeningResultStatusId=1;//1="A"
									}
								}else if($scope.donorDetailsObj.weightType == 'lbs'){
										
									if(	$scope.donorDetailsObj.donorWeight < 104){
										
										$scope.stopLoader();
										growl.warning("weight should be Greater than or Equal to 104 lbs",
											{
												title : 'warning!'
											});
										$scope.screeningResultStatusId=2;//2="R"
									}else{
										$scope.screeningResultStatusId=1;//1="A"
									}
								}else{
									$scope.stopLoader();
									growl.warning("please Select weight Type",
										{
											title : 'warning!'
										});
									return false;
								}

								
							}
							$scope.hBValidation=function(){
								
								if($scope.donorDetailsObj.hBType == 'dl')
								{
									
									if(	$scope.donorDetailsObj.donorHB < 12.5)
									{
										$scope.stopLoader();
										growl.warning("HB should be Greater than or Equal to 12.5 dl",
											{
												title : 'warning!'
											});
										$scope.screeningResultStatusId=2;//2="R"
									}else{
										$scope.screeningResultStatusId=1;//1="A"
									}
								}else if($scope.donorDetailsObj.hBType == 'g'){
										
									if(	$scope.donorDetailsObj.donorHB < 38){
										
										$scope.stopLoader();
										growl.warning("HB should be Greater than or Equal to 38 g",
											{
												title : 'warning!'
											});
										$scope.screeningResultStatusId=2;//2="R"
									}else{
										$scope.screeningResultStatusId=1;//1="A"
									}
								}else{
									$scope.stopLoader();
									growl.warning("please Select HB Type",
										{
											title : 'warning!'
										});
									return false;
								}
								
							}
							$scope.finalParameterList1 = [];
							$scope.saveDonorScreeningDetails = function()
							{
								$scope.startLoader();
								console.log("$scope.donorDetailsObj",$scope.donorDetailsObj);
								if($scope.donorDetailsObj.age ==""){
									
									$scope.stopLoader();
									growl.warning("Please fill age details",
										{
											title : 'warning!'
										});
									return false;
								}else if($scope.donorDetailsObj.donorTemperature ==""){
									
									$scope.stopLoader();
									growl.warning("Please fill Temperature ",
										{
											title : 'warning!'
										});
									return false;
								}else if($scope.donorDetailsObj.donorPulse ==""){

									$scope.stopLoader();
									growl.warning("Please fill Pulse ",
										{
											title : 'warning!'
										});
									return false;
									
								}else if($scope.donorDetailsObj.donorBPSystolic ==""){

									$scope.stopLoader();
									growl.warning("Please Enter Blood Pressure (Systolic) ",
										{
											title : 'warning!'
										});
									return false;
									
								}else if($scope.donorDetailsObj.donorBpDiastolic ==""){
									$scope.stopLoader();
									growl.warning("Please Enter Blood Pressure (Diastolic) ",
										{
											title : 'warning!'
										});
									return false;
								}else if($scope.donorDetailsObj.donorWeight ==""){
									$scope.stopLoader();
									growl.warning("Please fill weight ",
										{
											title : 'warning!'
										});
									return false;
																			
									
								}else if($scope.donorDetailsObj.donorHB ==""){
										$scope.stopLoader();
										growl.warning("Please fill HB Details",
											{
												title : 'warning!'
											});
										return false;
								}else if($scope.donorDetailsObj.heightInFt ==""){
									$scope.stopLoader();
									growl.warning("Please fill height In Feet",
										{
											title : 'warning!'
										});
									return false;
								}else if($scope.donorDetailsObj.heightInInches ==""){
									$scope.stopLoader();
									growl.warning("Please fill height In Inches",
										{
											title : 'warning!'
										});
									return false;
								}
								
								
								angular.forEach($scope.obj.temporaryParam,function(value1,inx) {
									
									if($scope.obj.temporaryParam[inx].radioId)
										{
										$scope.obj.temporaryParam[inx].parameterResultStatus="Y";
										}
									else
										{
										$scope.obj.temporaryParam[inx].parameterResultStatus="N";
										}
										
										var newArr={};
										newArr.donorScreeningParameterId = $scope.obj.temporaryParam[inx].id;
										newArr.parameterResultStatus =$scope.obj.temporaryParam[inx].parameterResultStatus;
										newArr.donorScreeningDetailMapperId=$scope.obj.temporaryParam[inx].donorScreeningDetailMapperId;
										newArr.organizationId = $scope.organizationId;
										newArr.unitId = $scope.unitId;
										newArr.status = 'A'; 
										if($scope.screeningResultStatusId == 3 || $scope.obj.temporaryParam[inx].parameterResultStatus == "N" )
										{
											//newArr.donorDeferralTillDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
											newArr.donorDeferralTillDate = null;
										}
										else{
											newArr.donorDeferralTillDate = moment($scope.obj.temporaryParam[inx].donorDeferralTillDate).format('DD-MM-YYYY HH:mm:ss');
										}
										newArr.createdBy = $scope.userId;
										newArr.updatedBy =$scope.userId;
										newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										$scope.finalParameterList1.push(newArr);
									//	}
								});
								
								angular.forEach($scope.temporaryParamNoList,function(value1,inx) {
									
									if($scope.temporaryParamNoList[inx].radioId)
										{
											$scope.temporaryParamNoList[inx].parameterResultStatus="Y";
										}
									else
										{
											$scope.temporaryParamNoList[inx].parameterResultStatus="N";
										}
										
										var newArr={};
										newArr.donorScreeningParameterId = $scope.temporaryParamNoList[inx].id;
										newArr.parameterResultStatus =$scope.temporaryParamNoList[inx].parameterResultStatus;
										newArr.donorScreeningDetailMapperId=$scope.temporaryParamNoList[inx].donorScreeningDetailMapperId;
										newArr.organizationId = $scope.organizationId;
										newArr.unitId = $scope.unitId;
										newArr.status = 'A'; 
										if($scope.screeningResultStatusId == 3 || $scope.temporaryParamNoList[inx].parameterResultStatus == "N" )
										{
											//newArr.donorDeferralTillDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
											newArr.donorDeferralTillDate = null;
										}
										else{
											newArr.donorDeferralTillDate = moment($scope.temporaryParamNoList[inx].donorDeferralTillDate).format('DD-MM-YYYY HH:mm:ss');
										}
										newArr.createdBy = $scope.userId;
										newArr.updatedBy =$scope.userId;
										newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										$scope.finalParameterList1.push(newArr);
									//	}
								});
								angular.forEach($scope.obj.permanentParam,function(value1,inx) {
									if($scope.obj.permanentParam[inx].radioId)
									{
										$scope.obj.permanentParam[inx].parameterResultStatus="Y";
									}
									else
									{
										$scope.obj.permanentParam[inx].parameterResultStatus="N";
									}
									var newArr={};
									newArr.donorScreeningParameterId = $scope.obj.permanentParam[inx].id;
									newArr.parameterResultStatus =$scope.obj.permanentParam[inx].parameterResultStatus;
									newArr.donorScreeningDetailMapperId=$scope.obj.permanentParam[inx].donorScreeningDetailMapperId;
									newArr.donorDeferralTillDate = null;
									newArr.organizationId = $scope.organizationId;
									newArr.unitId = $scope.unitId;
									newArr.status = 'A';
									newArr.createdBy = $scope.userId;
									newArr.updatedBy =$scope.userId;
									newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									$scope.finalParameterList1.push(newArr);
									//}

								});
								
								angular.forEach($scope.permanentParamNoList,function(value1,inx) {
									if($scope.permanentParamNoList[inx].radioId)
									{
										$scope.permanentParamNoList[inx].parameterResultStatus="Y";
									}
									else
									{
										$scope.permanentParamNoList[inx].parameterResultStatus="N";
									}
									var newArr={};
									newArr.donorScreeningParameterId = $scope.permanentParamNoList[inx].id;
									newArr.parameterResultStatus = $scope.permanentParamNoList[inx].parameterResultStatus;
									newArr.donorScreeningDetailMapperId=$scope.permanentParamNoList[inx].donorScreeningDetailMapperId;
									newArr.donorDeferralTillDate = null;
									newArr.organizationId = $scope.organizationId;
									newArr.unitId = $scope.unitId;
									newArr.status = 'A';
									newArr.createdBy = $scope.userId;
									newArr.updatedBy =$scope.userId;
									newArr.updatedDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									newArr.createdDate = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
									$scope.finalParameterList1.push(newArr);
									//}

								});
							
								$scope.donorDetailsFinalObj = {
										"donorWeight":$scope.donorDetailsObj.donorWeight,
										"donorTemperature":$scope.donorDetailsObj.donorTemperature,
										"donorPulse":$scope.donorDetailsObj.donorPulse,
										"donorHB":$scope.donorDetailsObj.donorHB,
										"donorBPSystolic":$scope.donorDetailsObj.donorBPSystolic,
										"donorId": $scope.common.donorId,
										"donorHeight":$scope.donorDetailsObj.heightInFt+"."+$scope.donorDetailsObj.heightInInches,
										"donorBpDiastolic":$scope.donorDetailsObj.donorBpDiastolic,
										"hbDoneById":$scope.donorDetailsObj.hbDoneById,
										"bleedingDoneById":$scope.donorDetailsObj.bleedingDoneById,
										"screeningResultStatusId":$scope.screeningResultStatusId,
										"deferralDays":($scope.screeningResultStatusId==3 || $scope.screeningResultStatusId==1 ?$scope.donorDetailsObj.deferralDays=null:moment($scope.donorDetailsObj.deferralDays).format('DD-MM-YYYY HH:mm:ss')),
										"screeningRemarks":$scope.donorDetailsObj.screeningRemarks,
										"donorScreeningMapperList":$scope.finalParameterList1,
										"donorScreeningId":$scope.donorDetailsObj.donorScreeningId,
										"organizationId": $scope.organizationId,
										"unitId": $scope.unitId,
										"status": 'A',
										"age": $scope.donorDetailsObj.age,
										"temperatureType": $scope.donorDetailsObj.temperatureType,
										"weightType": $scope.donorDetailsObj.weightType,
										"hBType": $scope.donorDetailsObj.hBType,
										"exportedToBagCollection":false,
										"createdBy":$scope.userId,
										"updatedBy":$scope.userId,
										"updatedDate": moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										"createdDate": moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
								};
								
								console.log("$scope.donorDetailsFinalObj",$scope.donorDetailsFinalObj);
								
										var URI = BASE_URL+ ROOT_URL+ "/api/bloodbank/saveDonorScreeningDetail"
										
								GenericService.serviceAction(METHOD_POST, URI, $scope.donorDetailsFinalObj)
										.then(function(response) {
										
													if (response.data.status == "success") {
														$scope.stopLoader();
														growl.success(response.data.message,
																{
																	title : 'Success!'
																});
														$scope.finalParameterList=[];
														if($scope.common.donorId!=null && $scope.common.donorId!=undefined)
														{
															$scope.getDonorScreeningDetails();
														}
														if($scope.common.genderId!=null && $scope.common.genderId!=undefined)
														{
															$scope.getparameterList();
														}
														
													}
													else
														{
														
														growl.error(response.data.message,
																{
																	title : 'error!'
																});
														$scope.finalParameterList=[];
														if($scope.common.donorId!=null && $scope.common.donorId!=undefined)
														{
															$scope.getDonorScreeningDetails();
														}
														if($scope.common.genderId!=null && $scope.common.genderId!=undefined)
														{
															$scope.getparameterList();
														}
														$scope.stopLoader();
														}
													$scope.donorDetailsObj={};
													});		
							}
							
							$scope.manageDays = function(res)
							{
								//alert(res);
								 if(res!=2)
								{
									 $scope.acceptDays = false;
								}
								 else
								{
									 $scope.acceptDays = true;
								}
								
							}
						
							$scope.usersList = [];
							$scope.getEmployeeList = function(){
								
								try{
									$rootScope.startLoader();
									
									var data = {
											organizationId:$scope.organizationId,
											unitId:$scope.unitId
									};
									var URI = BASE_URL + ROOT_URL
									+ GETUSERSLIST;
							GenericService.serviceAction("POST", URI, data)
									.then(
											function(response) {
												$scope.usersList = [];
												if (response.data.status == "success") {
													$scope.usersList = response.data.listObject;
													console.log("$scope.usersList",$scope.usersList);
												}
											});
								}catch(e){
									console.log("Exception-",e);
								}
							}
							$scope.getEmployeeList();
						
		}]);
							