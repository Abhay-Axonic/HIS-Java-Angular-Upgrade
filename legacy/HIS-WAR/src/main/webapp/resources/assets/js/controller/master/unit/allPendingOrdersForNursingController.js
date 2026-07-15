'use strict';

/**
 * @Author By Sneha Dalave
 * @name myApp.controller:allPendingOrdersForNursingController
 * @description #allPendingOrdersForNursingController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'allPendingOrdersForNursingController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'growl',
						'BillingGenericService',
						'$state',
						'PagerService',
						'GenericServiceParamHeader',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl, BillingGenericService,
								$state, PagerService,GenericServiceParamHeader) {
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = false;
							$scope.popUpFlag = true;
							

							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							$scope.unitId = cookieObject.unitId;
							$scope.organisationId = cookieObject.organizationId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.createdBy =cookieObject.userId;
							$scope.updatedBy =cookieObject.userId;
							
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.patientCategoryId = cookieObjectAppConfig.patientCategoryId;
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							$scope.defaultSelfTariffId = cookieObjectAppConfig.defaultSelfTariffId;
							$scope.isPreOrPostBilling = cookieObjectAppConfig.isPreOrPostBilling;
							$scope.paediatricsSpecialityId = cookieObjectAppConfig.paediatricsSpecialityId;
							$scope.selfPayeeId = cookieObjectAppConfig.selfPayeeId;
							
                            $scope.nursingWardList = [];
                            var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
                             if(nursingStationsWardsList == undefined){
                                 growl.warning(
                                            'No ward mapped with selected nursing station !!',
                                            {
                                                title : 'Success!'
                                            });
                                 return false;
                             }
                             else
                              {
                               console.log('nursingStationsWardsList',nursingStationsWardsList);
                               $scope.nursingWardList =nursingStationsWardsList; 
                              
                              console.log('hshsajsa',$scope.nursingWardList);
                              
                              
                              $scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};

								$scope.datepicker= {
									opened : false
								};
								
                  			//For selected date list
  							$scope.dateOptionsNursingOrder = {
  									formatYear : 'yyyy',
  									showWeeks : false,
  									maxDate : new Date(),
  								};

  								$scope.openDatePickerNursingOrder = function() {
  									$scope.datepickerNursingOrder1.opened = true;
  								};

  								$scope.datepickerNursingOrder1= {
  									opened : false
  								};
  								
  								// function for initializa clockpicker
  								$scope.initClockPicker = function() {

  									$('.clockpicker').clockpicker();
  								}
                             
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
                               	 $scope.isAppointmentRequired = cookieObjectAppConfig.isAppointmentRequired;
    								
    								
       							 if($scope.isAppointmentRequired  == undefined || $scope.isAppointmentRequired == null)
       								{
       								  $scope.isAppointmentRequired = "N";
       								}
       							else
       								{
       								  $scope.isAppointmentRequired = $scope.isAppointmentRequired;
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
                                    
                                }
                                $scope.initPatient();
                                

							
							
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
                                $scope.patientDetails.bednumber = ($scope.patientList[index].bedNumber);
                                $scope.patientDetails.doctorId = ($scope.patientList[index].doctorId);
                                $scope.patientDetails.admissionDate = ($scope.patientList[index].createdDate);
                                $scope.patientDetails.admissionTime = ($scope.patientList[index].createdTime);
                                $scope.patientDetails.wardName = $scope.patientList[index].wardName;
                                $scope.patientDetails.doctorFullname ="Dr."+' '+ $scope.patientList[index].doctorName;
                                $scope.patientDetails.dateOfAdmissionInput = $scope.patientList[index].doa;
                                $scope.patientDetails.pdd = $scope.patientList[index].pdd;
                                $scope.patientDetails.visitDate = $scope.patientList[index].createdDate;
                                $scope.patientDetails.visitTime = $scope.patientList[index].createdTime;
                                $scope.patientDetails.inputOutputchart=true;
                                $scope.patientDetails.patientCategoryId = ($scope.patientList[index].patientCategoryId);
                                $scope.patientDetails.billingBedCategoryId = ($scope.patientList[index].billingBedCategoryId);
                                $scope.patientDetails.specialityId = ($scope.patientList[index].specialityId);
                                $scope.patientDetails.visitTypeId1 = ($scope.patientList[index].visitTypeId);
                                $scope.patientDetails.contractId = ($scope.patientList[index].contractId);
                                $scope.patientDetails.paymentEntitlementId = ($scope.patientList[index].paymentEntitlementId);
                                $scope.patientDetails.visitTypeId = 1;
                                
                                //for nursing Order 
                                $scope.getDataSaved();
                            	$scope.getNursinOrderListForAll();
                            	$scope.datepickerNursingOrder = new Date();
                             }
                            
                            

                         $scope.getDataSaved=function(){
//							$rootScope.startLoader();
							
							var data = {
									"patientId":$scope.patientDetails.patientId,
									"organizationId": $scope.organizationId
							};
							var URI = BASE_URL + ROOT_URL + "/api/opd/pendingOrders/getPendingNurOrdListByPatientId";
							BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								if(response.data.status == "success"){
									  $scope.getDataSavedList=[];
                                     $scope.getDataSavedList = response.data.listObject;
									console.log("$scope.listNursinOrdersForAll",$scope.listNursinOrdersForAll);
								}
							});
						        $rootScope.stopLoader();
                           }
						
							//For all list
							$scope.getNursinOrderListForAll = function(){
//								$rootScope.startLoader();
								
								var data = {
										"patientId":$scope.patientDetails.patientId,
										"organizationId": $scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL + "/api/opd/pendingOrders/getNurseOrdersDateWiseList";
								BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										
                                         $scope.listNursinOrdersForAll = response.data.listObject;
                                         $scope.currentTime = moment().format("HH:mm");
                                         angular.forEach($scope.listNursinOrdersForAll,function(value,key){
                                        	 angular.forEach(value.nursingOrderMapperDtoList,function(value1,key1){
                                        		 //Date Less Than Todays Date
                                        		 var dateCom =moment(new Date()).format("YYYY-MM-DD");
		                                         if(value1.administrationDate > dateCom || value1.administrationDate == dateCom){
		                                        			 value1.validateDate=true;
		                                        			 
		                                        			 angular.forEach(value1.nursingOrderAdministeredTimeList,function(value2,key2){
		                                                		 value2.pendingNurOrdAdministrationTime=null;
		                                        				 value2.administrationTimeMaster=null;
		                                        				 value2.currentTime=$scope.currentTime;
		                                                		 });
		                                        			 
		                                        			 
		                                        			 if(value1.administrationDate == dateCom){
		                                        			 angular.forEach(value1.nursingOrderAdministeredTimeList,function(value2,key2){
		                                        				 value2.futureDate=true;
		                                        				  value2.yellow=false;
		                                         				 if (value2.currentTime < value2.preBufferAdministrationTime && value2.futureDate == true){
		                                         					 value2.yellow=true;
		           		                                          }
		                                         				if(value2.currentTime > value2.postBufferAdministrationTime && value2.futureDate == true){
		                                         					 value2.red=true;
		                                         				}
		                                         				if ((value2.preBufferAdministrationTime < value2.currentTime)
			                                                             && (value2.currentTime < value2.postBufferAdministrationTime)
			                                                             ){
		                                         					 value2.orange=true;
		                                         				}
		                                                		 });
		                                        			 }else{
		                                        				 angular.forEach(value1.nursingOrderAdministeredTimeList,function(value2,key2){
		                                            				 value2.futureDate=false;
		                                            				  value2.yellow=false;
		                                            				 if(value2.futureDate == false){
		                                             					 value2.yellow=true;
		                                             					 value2.red=false;
		                                             					 value2.orange=false;
		                                             				 }
		                                                    		 });
		                                        			 }
		                                        			
		                                         }else{
                                        			 value1.validateDate=false;
                                        			 angular.forEach(value1.nursingOrderAdministeredTimeList,function(value2,key2){
                                                		  value2.pendingNurOrdAdministrationTime=null;
                                        				  value2.administrationTimeMaster=null;
                                        				  value2.currentTime=$scope.currentTime;
                                        				  value2.red=true;
                                        				  value2.yellow=false;
                                        				  value2.orange=false;
                                                		 });
                                        		 }
                                        	
                                        		 
                                        		 
                                        		 angular.forEach( $scope.getDataSavedList,function(value4,key4){
                                        			 if(value.nursingOrderMapperId == value4.nursingOrderMapperId){
		                                        		 if(value1.administrationDate == value4.pendingNurOrdAdministrationDate ){
			                                        		 angular.forEach(value1.nursingOrderAdministeredTimeList,function(value2,key2){
			                                        			 if(value2.administrationTime == value4.administrationTime ){
			                                        				 value2.administrationTime =value4.pendingNurOrdAdministrationTime;
			                                        				 value2.pendingNurOrdAdministrationTime=value4.pendingNurOrdAdministrationTime;
			                                        				 value2.administrationTimeMaster=value4.administrationTime;
			                                        				 value2.currentTime=$scope.currentTime;
			                                        				 value2.green=true;
			                                        				  value2.red=false;
			                                        				  value2.yellow=false;
			                                        				  value2.orange=false;
			                                        			 }
			                                        	     });
		                                        		  }
                                        			 }
                                        	    });
                                        	 });
                                        	 
                                         });
                                         angular.forEach($scope.listNursinOrdersForAll,function(value3,key3){
                                        	 angular.forEach(value3.nursingOrderMapperDtoList,function(value,key){
                                        		 value.administrationDateShow = value.administrationDate 
                                        		 var doseTime = value.nursingOrderAdministeredTimeList;
    			                            	 value.doseTimeArray = [];
    				                               for (var i = 0; i <= 23; i++) {
    				                            	   value.doseTimeArray.push({})
    				                                }
    			                            	 angular.forEach(doseTime,function(value1,key1){
    						                               value1['convertedTime'] =(value1.administrationTime);
    		
    						                               value.doseTimeArray[(parseInt(value1['convertedTime']))] = doseTime.find(function (x) {
    					                                        return x.convertedTime == value1['convertedTime'];
    					                                    });
    			                            	   });
                                        	 });
                                        	 
			                            	 $rootScope.stopLoader();
			                               });
                                         var dtob=moment(new Date()).format("YYYY-MM-DD");
                                     	$scope.getNursinOrderListForDueForToday(dtob);
										console.log("$scope.listNursinOrdersForAll",$scope.listNursinOrdersForAll);
									}
								});
							$rootScope.stopLoader();
							}
							
				
							$scope.getNursinOrderListForDueForToday = function(datepickerNursingOrder){
								
								
								$rootScope.startLoader();
								 $scope.date1=moment(datepickerNursingOrder).format("YYYY-MM-DD")
								var data = {
										"patientId":$scope.patientDetails.patientId,
										"organizationId": $scope.organizationId,
										"startDate": moment(datepickerNursingOrder).format("YYYY-MM-DD")
								};
								
								
								$scope.nursinOrderListForDueForToday=[];
	                               angular.forEach($scope.listNursinOrdersForAll,function(value3,key3){
	                            	   
                                 	 angular.forEach(value3.nursingOrderMapperDtoList,function(value,key){
                                 		 if( moment(datepickerNursingOrder).format("YYYY-MM-DD") == value.administrationDate){
                                 			 value.nursingOrderMapperId = value3.nursingOrderMapperId
                                 		     value.nursingOrderDesc = value3.nursingOrderDesc
                                 		     value.frequencyDesc = value3.frequencyDesc
                                 		     value.instruction = value3.instruction
                                 		     value.checkStatus= value3.checkStatus
                                 		     value.administrationDateShow = value.administrationDate 
                                 		 var doseTime = value.nursingOrderAdministeredTimeList;
			                            	 value.doseTimeArray = [];
				                               for (var i = 0; i <= 23; i++) {
				                            	   value.doseTimeArray.push({})
				                                }
			                            	 angular.forEach(doseTime,function(value1,key1){
			                            	      
						                               value1['convertedTime'] =(value1.administrationTime);
		
						                               value.doseTimeArray[(parseInt(value1['convertedTime']))] = doseTime.find(function (x) {
					                                        return x.convertedTime == value1['convertedTime'];
					                                    });
			                            	   });
			                            		$scope.nursinOrderListForDueForToday.push(value);
                                 		 }
                                 	    });
                                 	 console.log("DataByDate",$scope.nursinOrderListForDueForToday);
		                            	 $rootScope.stopLoader();
		                               });
	                               $rootScope.stopLoader();
	                               
							/*	var URI = BASE_URL + ROOT_URL + "/api/opd/pendingOrders/getNurseOrdersByDate";
								BillingGenericService.serviceAction(METHOD_POST,URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
//										$scope.nursinOrderListForDueForToday=response.data.listObject;
										console.log("BydateData",$scope.nursinOrderListForDueForToday);
			                               angular.forEach($scope.nursinOrderListForDueForToday,function(value,key){
//			                                   if(moment(datepickerNursingOrder).format("YYYY-MM-DD")){
			                            	 var doseTime = value.nursingOrderAdministeredTimeList;
			                            	 value.doseTimeArray = [];
				                               for (var i = 0; i <= 23; i++) {
				                            	   value.doseTimeArray.push({})
				                                }
			                            	 angular.forEach(doseTime,function(value1,key1){
			                            	      
						                               value1['convertedTime'] =(value1.administrationTime);
		
						                               value.doseTimeArray[(parseInt(value1['convertedTime']) - 1)] = doseTime.find(function (x) {
					                                        return x.convertedTime == value1['convertedTime'];
					                                    });
			                            	   });
//			                               }
			                            	 $rootScope.stopLoader();
			                               });
									}
								});*/
							
							}
							
							$scope.ReasonList = [];
								$scope.getReasonListByReasonTypeId = function(){
									var URI = BASE_URL + ROOT_URL+ "/api/common/getReasonMasterListByReasonTypeId";	
									var data = {
											reasonTypeId : 17,
											organizationId : $scope.organizationId
												}
									GenericService.serviceAction("POST", URI, data).then(
													function(response) {
														$scope.ReasonList = [];
														if (response.data.status == "success") {
															$scope.ReasonList = response.data.listObject;
														}
													});
								}
								$scope.getReasonListByReasonTypeId();
							
								$scope.showInstruction = function(data){
									$scope.instruction=data;
								}
								$scope.Dataobj={
										reasonId:"",	
										remark:"",	
										pendingNurOrdAdministrationTime:""
								}
							
								
							$scope.openPopup = function(item,doseData,date){
									
									 var dateCom =moment(new Date()).format("YYYY-MM-DD");
									 if(date.administrationDate == dateCom){
										 date.validateDate = false;
									 }
									$scope.saveDisable=false;
									$scope.Dataobj={
											reasonId:"",	
											remark:"",	
											pendingNurOrdAdministrationTime:""
									}
									
									
									 if(!date.validateDate){
									  if (doseData.administrationTime) {
									     $('#checkTabletStatus').modal('show');
									  }
									 
									  $scope.nursingOrderMapperId = item.nursingOrderMapperId;
									  $scope.pendingNurOrdAdministrationDate = date.administrationDate;
									  $scope.Dataobj.pendingNurOrdAdministrationTime=doseData.administrationTime;
									  $scope.administrationTime=doseData.administrationTime;
									  $scope.frequencyId=doseData.frequencyId;
									  
									  angular.forEach($scope.getDataSavedList,function(value,key){
										  if(date.administrationDate == value.pendingNurOrdAdministrationDate 
												  && doseData.administrationTime ==value.pendingNurOrdAdministrationTime
												  && item.nursingOrderMapperId ==value.nursingOrderMapperId){
											  $scope.Dataobj.reasonId=value.reasonId.toString();
											  $scope.Dataobj.remark=value.remark;
											  if($scope.Dataobj.remark != "" ||  $scope.Dataobj.reasonId != ""){
												  $scope.saveDisable=true;
											  }
												 
										  }
									  });
									 }else{
										     growl.warning('You have selected future date..!!');
											 return false;
									 }
								}
								
								
								$scope.validateTimeForCurrentDate= function(){
									$scope.validTime=false;
									var dateComp =moment(new Date()).format("YYYY-MM-DD");
									if($scope.pendingNurOrdAdministrationDate == dateComp){
										var currtime = moment().format("HH:mm");
										 if(currtime < $scope.Dataobj.pendingNurOrdAdministrationTime){
											 $scope.validTime=true;
										 }
									}
									if($scope.validTime){
										  growl.warning('Time Should be equal to or less than Current time.');
										  $scope.Dataobj.pendingNurOrdAdministrationTime=currtime;
											 return false;
											
									}
									
								}
								$scope.SaveNursingOrderAdministrator = function(){
									var date = new Date();
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data = {
										  
										  "nursingOrderMapperId":$scope.nursingOrderMapperId,
										  "pendingNurOrdAdministrationDate":$scope.pendingNurOrdAdministrationDate,
										  "pendingNurOrdAdministrationTime":$scope.Dataobj.pendingNurOrdAdministrationTime,
										  "reasonId":$scope.Dataobj.reasonId,
										  "administrationTime":$scope.administrationTime,
										  "frequencyId":$scope.frequencyId,
										  "admissionId":$scope.patientDetails.admissionId,
										  "organizationId": $scope.organizationId,
										  "unitId": $scope.unitId,
										  "patientId":$scope.patientDetails.patientId,
										  "status":"A",
										  "createdBy":$scope.userId ,
										  "createdDate":$scope.dt,
										  "updatedBy":$scope.userId,
										  "updatedDate":$scope.dt,
										  "remark":$scope.Dataobj.remark

										}
									console.log("saveData",data);
									if($scope.Dataobj.reasonId != ""){
									var URI = BASE_URL + ROOT_URL+ "/api/opd/pendingOrders/saveActualAdministrationTime";
									console.log("Save-URI-", URI);
									console.log(" save OBJ", data);

									GenericService.serviceAction("POST", URI,data).then(function(response) {

										if (response.data.status == 'success') {
											growl.success("Nursing order executed successfully",
													{
														title : 'Success!'
													});
											
											$scope.getDataSaved();
			                            	$scope.getNursinOrderListForAll();
			                            	$scope.getNursinOrderListForDueForToday($scope.date1);
										    $rootScope.stopLoader();
											//for service order by nursing order 
											if($scope.orderServiceId>0 && $scope.orderServiceId != undefined &&  $scope.orderServiceId != "") {
												angular.element('#nursingOrderMpper').modal('show');
											}
										    } else if (response.data.status == 'error') {
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
											$rootScope.stopLoader();
										   }
									});
									}else{
										growl.warning("Please Enter Reason.",
		    	    							{
		    	    							title : 'Warning!'
		    	    							});
									}
								}
							
//=============================================== for procedure workbench start ============================================ 
								// for getting workbenchlist
								$scope.getRadioLogyServices = function(admissionId)
								{
									//$scope.radiology = [];
									var URI = BASE_URL + ROOT_URL + "/api/opd/getListOfRadiologyTests/"+admissionId+"/"+$scope.organizationId+"/"+$scope.unitId;
									BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										if (response.data.status == "success"){
											$scope.nephrologyList=[];
											$scope.radiology = [];
											$scope.procedureList = [];
											angular.forEach(response.data.listObject,function(value,index){
												if(cookieObjectAppConfig.radiologySpecialityId==value.specialityId)
												{
													$scope.radiology.push(value);
												}else if(cookieObjectAppConfig.hemodialysisSpecialityId==value.specialityId){
													$scope.nephrologyList.push(value);
												}else{
													$scope.procedureList.push(value);
												}
											})
											console.log("radiology:",$scope.radiology);
											console.log("nephrology list:",$scope.nephrologyList);
											console.log("procedure list:",$scope.procedureList);
										}
									});
								}
								
								$scope.clearDetails=function(){
									$scope.fromTimeString = "";
									$scope.fromTimeStringSlot="";
									$scope.slotId="";
									 $scope.modalityId="";
									 $scope.slotDetailsList=[];
									 $scope.loadingMsg="";
								}
				
								
								$scope.selectedSpecialityId="";
								$scope.radSpecialityId = 0;
								$scope.radSubSpecialityId = 0;
								
								// get modality list by speaciality and subspeciality 
								$scope.getModalityBySubSpeciality = function(item) {
									
									$scope.slotCheckTime = moment(new Date()).format("HH:mm");
									$scope.fromTimeString = "";
									$scope.modalityMasterList = [];
									$scope.admissionId = item.admissionId;		
									$scope.orderDetailsId = item.orderDetailsId;
									$scope.radSpecialityId = item.specialityId;
									$scope.radSubSpecialityId = item.subSpecialityId;
									var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										specialityId : item.specialityId,
										subSpecialityId :item.subSpecialityId
									}
									console.log(data);			
									
										$scope.selectedSpecialityId=item.specialityId;
										$scope.bedList = null;
										var URI = BASE_URL + ROOT_URL + GETMODALITYBYSUBSPECIALITYID;
									BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
											.then(function(response) {
														console.log(response);
														$scope.modalityMasterList = [];
														if (response.data.status == "success") {
															$scope.modalityMasterList = response.data.listObject;
														      console.log($scope.modalityMasterList)
														}

													});
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
								  $scope.loadingMsg="";
								  $scope.itemObj={};
								  $scope.globleObj={};
								  
								  // get slot details  
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
									$scope.itemId= $scope.itemObj.itemId;
									$scope.mobilityId=$scope.itemObj.modalityId
									$scope.slotDetailsList = [];
									$scope.fromTimeString = "";
									 
									if($scope.mobilityId > 0)
									{
										var dt = moment($scope.datepicker).format('YYYY-MM-DD');
										$scope.diffDaysMod = $scope.daysDiffrence(moment($scope.datepicker).format('YYYY/MM/DD'));
											var data = {
												subSpecialityId : $scope.radSubSpecialityId,
												specialityId : $scope.radSpecialityId,
												appointmentDate : dt,
												organizationId : $scope.organizationId,
												unitId : $scope.unitId,
												slotTypeId: 2,
												modalityId:$scope.mobilityId											
											};
												
										console.log(data);
										//return false;
										var URI = BASE_URL + ROOT_URL + GETDAILYSLOTS;
										BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
												.then(function(response) {														
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
								$scope.clearDetails=function(){
									$scope.fromTimeString = "";
									$scope.fromTimeStringSlot="";
									$scope.slotId="";
									 $scope.modalityId="";
									 $scope.slotDetailsList=[];
									 $scope.loadingMsg="";
								}
								
								$scope.slotBooking = function(item)
								{
									$scope.fromTimeStringSlot = item.fromTimeString;
									$scope.fromTimeStringSlotShow = item.fromTimeString2;
									 $scope.slotId = item.slotId;
								}
								
			                   $scope.saveAppointment = function(){
									
									if($scope.modalityId=="" || $scope.modalityId==undefined){
										growl.error("Please Select Modality",{
											title : ERROR_MSG
										});
										return false;
									}
									if($scope.slotId=="" || $scope.slotId==undefined){
										growl.error("Please Select Slots",{
											title : ERROR_MSG
										});
										return false;
									}
									
									$rootScope.startLoader();
									$scope.currentDate = new Date();
									$scope.currentDate = moment(
											$scope.currentDate).format(
											'DD-MM-YYYY HH:mm:ss');
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
										patientId :$scope.patientId,
										appointmentTypeId:2
									}
									/*if($scope.selectedSpecialityId==cookieObjectAppConfig.hemodialysisSpecialityId){
										data.appointmentTypeId=6;
									}else{
										data.appointmentTypeId=2;
									}*/
									var URI = BASE_URL + ROOT_URL+ "/api/opd/scheduleRadiologyAppointmentForInPatient";									

									BillingGenericService.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE)
											.then(function(response) {
														console.log(response);
														if (response.data.status == "success") {
															angular.element('#modalitySlotPopUp').modal('hide');
														     $rootScope.stopLoader();
															$scope.clearDetails();
															$scope.getRadioLogyServices($scope.patientDetails.admissionId);
															$rootScope.stopLoader();
														}
														else if(response.data.status == "error") 
														{  
															angular.element('#modalitySlotPopUp').modal('hide');
																$rootScope.stopLoader();
														}
													});
								}
//			                   ==================tab selection============================
								$scope.setFirstTabLevel = function(newTab) {
									$scope.firstTabLevel = newTab;	
									//for getting workbenchList
									if (newTab == '8') {
										if($scope.patientDetails.admissionId!="" && $scope.patientDetails.admissionId!=null && $scope.patientDetails.admissionId!=undefined){
											$scope.getRadioLogyServices($scope.patientDetails.admissionId);
										}else{
											growl.warning("please select patient !",
													{
														title : 'warning!'
													});
											return false;
										}
										
									}
									if (newTab == '1') {
										console.log('other')
									}								
								};

								$scope.isSetFirstTabLevel = function(tabNum) {
									return $scope.firstTabLevel === tabNum;
									
								};
								
								//service order code 
								
								$scope.checkServiceMappedOrNot = function(info){
									console.log("data",info);
									if(info.serviceId != null){
										$scope.orderServiceId = info.serviceId;
										$scope.orderSpecialtyId = info.specialityId;
										$scope.orderSubSpecialtyId  = info.subSpecialityId;
										$scope.orderServiceName = info.serviceStandardName;
										$scope.orderSpecialtyName = info.specialityName;
										$scope.orderSubSpecialtyName = info.subSpecialityName;
										$scope.search = info.serviceStandardName;
										$scope.selectedServiceTypeSpecialityId = info.specialityId;
										$scope.selectedSubServiceTypeSubSpecialityId = info.subSpecialityId;
									}
								}
								
                              }
                             
                             $scope.nursingOrderMpperService = function(dat){
 								if(dat == true){
 									$scope.serviceHideFlag=true;
 								$scope.addInvestigation();	
// 								$scope.getSpecialityList();
 								growl.success('service order recorded successfully ',
 										{
 											title : 'Success!'
 										})
 								}else{
 									$scope.orderServiceId = "";
 									$scope.orderSpecialtyId = "";
 									$scope.orderSubSpecialtyId  = "";
 									$scope.orderServiceName = "";
 									$scope.orderSpecialtyName = "";
 									$scope.orderSubSpecialtyName = "";
 									$scope.search = "";
 									$scope.selectedServiceTypeSpecialityId = "";
 									$scope.selectedSubServiceTypeSubSpecialityId = "";
 								}
 							}
                             
                             
                         	$scope.addInvestigation = function() {
                         		$scope.addedServiceOrdersForDisplay=[];
								$scope.editserviceHideFlag=false;
								$rootScope.startLoader();
								// validate service in current added package
									var newArr = {};
									if ($scope.orderServiceId != undefined
											&& $scope.orderServiceId != "") {

										
										newArr.specialityId = $scope.orderSpecialtyId;
										newArr.serviceSpecialityId = $scope.orderSpecialtyId;
										newArr.serviceSubSpecialityId = $scope.orderSubSpecialtyId;
										newArr.subSpecialityId = $scope.orderSubSpecialtyId;
										newArr.serviceMasterId = $scope.orderServiceId;
										newArr.serviceStandardName = $scope.orderServiceName;
										newArr.specialityName = $scope.orderSpecialtyName;
										newArr.subSpecialityName = $scope.orderSubSpecialtyName;
										newArr.serviceRenderingDeptId = $scope.orderSubSpecialtyId;

										newArr.createdBy = $scope.userId;
										newArr.createdDate = new Date()
												.getTime();
										newArr.selfPayable = "0";
										newArr.creditPayable = "0";
										newArr.serviceRendered = "0";
										newArr.doctorId = $scope.patientDetails.doctorId;
										newArr.discountPercentage = 0;
										newArr.discount = 0;
										newArr.quantity = 1;
										newArr.basePrice = 0;
										newArr.basePriceOld = 0;
										newArr.totalAmt = 0;
										newArr.concession = 0;
										newArr.selectedForFuture = 'N';
										newArr.payeeId = $scope.patientDetails.payeeId;
										newArr.apportionedPrice = null;
										newArr.packageId = null;
										newArr.packageTypeId = null;
										newArr.packageEitherOrGroupId = null;
										newArr.packageCapDetailsId = null;
										newArr.isCapService = null;
										newArr.status = 'A';
										newArr.isRefDoctorShare = $scope.orderIsRefDoctorShare;
										newArr.packageEitherOrGroupId = null;
										newArr.isPackageService = 'N';
										newArr.discountDetailsList = [];
										newArr.investigationFutureDateToShowdddd = moment(new Date()).format('DD/MM/YYYY');
										if($scope.investigationFutureDate != "" && $scope.investigationFutureDate != undefined && $scope.selectedForFuture == "Y" ){
							        		newArr.investigationFutureDate = $scope.investigationFutureDate
							        	
							        		newArr.investigationFutureDateToShowdddd = moment($scope.investigationFutureDate).format('DD/MM/YYYY')
							        		}
							        		
							        		if( $scope.investigationFutureDate == undefined && $scope.selectedForFuture == "Y"){
							        			
							        			growl.error("Please select Date !!",
												   		{
															title : 'Error!'
														});
														$rootScope.stopLoader();
														return false;
							            		}
							            		
							        		
							                if( $scope.investigationFutureDate == ""  && $scope.selectedForFuture == "Y"){
							        			
							        			growl.error("Please select Date !!",
												   		{
															title : 'Error!'
														});
														$rootScope.stopLoader();
														return false;
							            		}
							                $scope.dv = new Date();
							                if($scope.investigationFutureDate != "" && $scope.investigationFutureDate != undefined && $scope.selectedForFuture == "Y"){
							                 if( $scope.investigationFutureDate < $scope.dv){
							        			
							        			growl.error("Please select greater than todays date !!",
												   		{
															title : 'Error!'
														});
														$rootScope.stopLoader();
														return false;
							            		}
							                }
							                
										if ($scope.search == ''
												|| $scope.selectedServiceTypeSpecialityId == ''
												|| $scope.selectedSubServiceTypeSubSpecialityId == '') {
											growl.error(
													"Please select Service !!",
													{
														title : 'Error!'
													});
											$scope.selectedForFuture = 'N';
							        		$scope.fromDatepickerForFuture='';
							        		$scope.investigationFutureDate = "";
											$rootScope.stopLoader();
											return false;
										} else 
										{
											
											if($scope.fromOrderSetServices == true){
						        				$scope.addedServiceOrdersForDisplay.splice($scope.editInvestigation , 1);
						        			}
													  $scope.addedServiceOrdersForDisplay.push(newArr);
													  $scope.saveInvestigation();
													  $scope.search = '';
													  $scope.selectedForFuture = 'N';
										            	$scope.investigationFutureDate='';
										        		$scope.fromDatepickerForFuture='';
														$scope.selectedServiceTypeSpecialityId = '';
														$scope.selectedSubServiceTypeSubSpecialityId = '';
											
										}
										
										$rootScope.stopLoader();
									} else {
										growl.error("Please select Service !!",
												{
													title : 'Error!'
												});
										$rootScope.stopLoader();
									}
							}
                         	
                         	
                        	$scope.saveInvestigation = function() {
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
									patientId : $scope.patientDetails.patientId,
									admissionId : $scope.patientDetails.admissionId,
									defaultSelfTariffId : $scope.defaultSelfTariffId,
									paymentEntitlementTypeId : $scope.patientDetails.paymentEntitlementId,
									doctorId : $scope.patientDetails.doctorId,
									priorityId : 2,
									visitTypeId : $scope.patientDetails.visitTypeId1,
									orderDate : new Date().getTime(),
									orderStatus : 'A',
									orderStatusId : 1,
									contractId : $scope.patientDetails.contractId,
									payeeId : $scope.selfPayeeId,
									billingBedCategoryId : $scope.patientDetails.billingBedCategoryId,
									paymentEntitlementTypeId : $scope.patientDetails.paymentEntitlementId,
									patientTypeId : $scope.patientDetails.patientCategoryId
								}
								
								var data1 = {
					        			
					        			currentDate : moment(new Date()).format('YYYY-MM-DD HH:mm:ss'),
					        			createdDate:new Date().getTime(),
					        			updatedDate:new Date().getTime(),
					        			orgId : $scope.organizationId,
					        			orgUnitId:$scope.unitId,
					        			createdBy  : $scope.userId,
					    				updatedBy : $scope.userId,
					    				patientId : $scope.patientDetails.patientId,
					    				defaultSelfTariffId : $scope.defaultSelfTariffId,
					    				paymentEntitlementTypeId :$scope.patientDetails.paymentEntitlementId,
					    				doctorId :$scope.patientDetails.doctorId,
					    				priorityId : 2,
					    				visitTypeId : $scope.patientDetails.visitTypeId1,
					    				orderDate:new Date().getTime(),
					    				orderStatus:'A',
					    				orderStatusId:1,
					    				billingBedCategoryId: $scope.patientDetails.billingBedCategoryId,
					    				patientTypeId:$scope.patientDetails.patientCategoryId
					        	}
					        	
								var finalOrderServiceList = [];
								var finalOrderServiceListForFuture = [];
					        	var justChkFlgFut = 0;
					        	var justChkFlgCurr = 0;
								angular .forEach( $scope.addedServiceOrdersForDisplay, function(value, key) {
													if(value.selectedForFuture == 'N')
								        			{
													var newArr = {};
													newArr.ordDocId = $scope.patientDetails.doctorId;
													newArr.serviceId = value.serviceMasterId;
													newArr.ordDocSplId = $scope.patientDetails.specialityId;
													newArr.serviceRenderingDeptId = value.subSpecialityId;
													newArr.apportionedPrice = value.apportionedPrice;
													newArr.isPackageService = value.isPackageService;
													newArr.packageId = value.packageId;
													newArr.orderSetId = value.orderSetId;
													newArr.packageTypeId = value.packageTypeId;
													newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
													newArr.packageCapDetailsId = value.packageCapDetailsId;
													newArr.isCapService = value.isCapService;
													newArr.packageConsumptionAmt = value.packageConsumptionAmt;
													newArr.isPackage = false;
													newArr.status = 'A';
													newArr.isRefDoctorShare = value.isRefDoctorShare;
													newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
													newArr.isPackageService = value.isPackageService;
													newArr.departmentId = value.specialityId;
													newArr.isInvestigationOrder = 'Y';
													newArr.subDepartmentId = value.subSpecialityId;
													newArr.orderSetId = value.orderSetId;
													newArr.nursingServiceOrderFlag ='Y';
													finalOrderServiceList.push(newArr);
													justChkFlgCurr = 1;
								        			}
													if(value.selectedForFuture == 'Y')
									    			{
														var newArr = {};
														newArr.ordDocId = $scope.patientDetails.doctorId;
														newArr.serviceId = value.serviceMasterId;
														newArr.ordDocSplId = $scope.patientDetails.specialityId;
														newArr.serviceRenderingDeptId = value.subSpecialityId;
														newArr.apportionedPrice = value.apportionedPrice;
														newArr.isPackageService = value.isPackageService;
														newArr.packageId = value.packageId;
														newArr.orderSetId = value.orderSetId;
														newArr.packageTypeId = value.packageTypeId;
														newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
														newArr.packageCapDetailsId = value.packageCapDetailsId;
														newArr.isCapService = value.isCapService;
														newArr.packageConsumptionAmt = value.packageConsumptionAmt;
														newArr.isPackage = false;
														newArr.status = 'A';
														newArr.isRefDoctorShare = value.isRefDoctorShare;
														newArr.packageEitherOrGroupId = value.packageEitherOrGroupId;
														newArr.isPackageService = value.isPackageService;
														newArr.departmentId = value.specialityId;
														newArr.isInvestigationOrder = 'Y';
														newArr.subDepartmentId = value.subSpecialityId;
														newArr.orderSetId = value.orderSetId;
									            		newArr.orderSchDate=new Date(value.investigationFutureDate).getTime(),
									        			finalOrderServiceListForFuture.push(newArr);
									        			justChkFlgFut = 1;
									        			
										    		}
												});
								data.listOrderDetailsMasterDto = finalOrderServiceList;
								data1.listOrderDetailsMasterDto = finalOrderServiceListForFuture;
									if(justChkFlgCurr == 1)
							        	listOrderMasterDto.push(data);
						        	
						        	if(justChkFlgFut == 1)
						        	listOrderMasterDto.push(data1);
								finalData.listOrderMasterDto = listOrderMasterDto;
//								console.log($scope.addedServiceOrdersForDisplay.serviceId);
								console.log("INV", finalData);
								if ($scope.addedServiceOrdersForDisplay.length > 0) {
									var URI = BASE_URL + ROOT_URL
											+ SAVEINVESTIGATION
									GenericServiceParamHeader
											.serviceAction(METHOD_POST, URI,
													finalData,
													NOTIFICATION_MSG_STATUS_TRUE)
											.then(
													function(response) {
														if (response.data.status == "success") {

//															$scope.getInvetigation();
//															$scope.selectedServiceTypeSpecialityId = "";
//															$scope.selectedSubServiceTypeSubSpecialityId = "";
															$scope.addedServiceOrdersForDisplay = [];
															$scope.fromOrderSetServices= false;
//															$scope.orderServiceId = "";
															$scope.init();

														}
													});
								} else {
									growl
											.error(
													"Please add investigation and then save !!",
													{
														title : 'Error!'
													});
									$rootScope.stopLoader();
								}

							}
                        	
                         	
						} ]);
