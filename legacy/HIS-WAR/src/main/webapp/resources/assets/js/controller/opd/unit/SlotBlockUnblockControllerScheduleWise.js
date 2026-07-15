'use strict';

/**
 * @Author By  Vivek s
 * @name myApp.controller:SlotBlockUnblockControllerScheduleWise
 * @description #SlotBlockUnblockControllerScheduleWise Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'SlotBlockUnblockControllerScheduleWise',
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
						'$stateParams',
						'PagerService',
						'GenericServiceParamHeader',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,
								BillingGenericService, growl, $state,
								$stateParams,PagerService,GenericServiceParamHeader) {

							/* init() function for form object create. */
							$scope.init = function() {
								
								
								/*var a = "12:30";
								var b = "01:00";
								alert(parseInt(a) + parseInt(b) );*/
								
								$scope.dataList = [ {
									fromTime : '',
									toTime : '',
									timePerSlot : '',
									visitTypeId :1,
									noOfSlots:0,
									example8model:[],
									breakTimeList:[{breakFromTime:'',breakToTime:''}]
								} ];
								
								
								//Loop DatePicker Start
								// for datePicker
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

									// for datePicker2
									$scope.dateOptions = {
										formatYear : 'yyyy',
										showWeeks : false
									};
									$scope.openDatePickerTo = function() {
										$scope.datepickerTo.opened = true;
									};

									$scope.datepickerTo = {
										opened : false
									};

								//End To Date
								
								
								/** Variables for pagination */
								$scope.offset = 0;
								$scope.noOfRecordsPerPage = 10;
								$scope.totalNoOfRecords;
								$scope.searchKeyword;
								$scope.commonListCount;
								$scope.fromDate = '';
								$scope.toDate= '';
								
								// Loop DatePicker End
								var cookieObject = $cookies
										.getObject('cookieObject');
								if (cookieObject == undefined) {
									$state.go('login');
									return;
								}
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								$rootScope.loginpage = true;
								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;

								// for popup
								$scope.showModal = false;
								$scope.buttonClicked = "";
								$scope.popUpFlag = true;
								// end for popup
								
								
								$scope.slotTypeId  = 1;
								$scope.isBlocked = 1;
								$scope.isBlockedUnblocked = 2;

								var commonObj = {
									unitId : $scope.unitId,
									organizationId : $scope.organizationId
								};
								var data = {};

								var URI = BASE_URL + ROOT_URL
								+ GETACTIVEREASONLIST;
						GenericService
								.serviceAction("POST", URI, commonObj)
								.then(
										function(response) {
											$scope.reasonList = [];

											if (response.data.status == "success") {
												$scope.reasonList = response.data.listObject;
												
											}
										});
						
							
								 //For Speciality
								 $scope.example14model = [];
								    $scope.example14settings = {
								        scrollableHeight: '200px',
								        scrollable: true,
								        enableSearch: true
								    };
								   /* $scope.example14data = [{
								        "label": "Cardiology",
								            "id": "1"
								    }, {
								        "label": "Dental",
								            "id": "2"
								    }, {
								        "label": "Radiology",
								            "id": "3"
								    }];*/
								    var URI = BASE_URL + ROOT_URL
												+ GETACTIVESPECIALITY;
										BillingGenericService.serviceAction(METHOD_POST, URI,commonObj,
														NOTIFICATION_MSG_STATUS_FALSE)
												.then(function(response) {
															console.log(response);
															// $rootScope.startLoader();
															if (response.data.status == "success")
																{
																$scope.example14data = [];
																angular.forEach(response.data.listObject,function(value,index) {
																			var data = {};
																			data.id=value.specialityId;
																			data.label=value.specialityName;
																			$scope.example14data.push(data);
																		
																		});
																	 
																}
																
														});
										
										
										 //For Speciality under modality
										 $scope.example14ModalityType = [];
										 $scope.example14SubSpeciality=[];
										   
										   
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
																		{
																		$scope.example14ModalityTypeData = [];
																		angular.forEach(response.data.listObject,function(value,index) {
																					var data = {};
																					data.id=value.specialityId;
																					data.label=value.specialityName;
																					$scope.example14ModalityTypeData.push(data);
																				
																				});
																			 
																		}
																		
																});
										
										
										
										
								    $scope.example2settings = {
								        displayProp: 'id'
								    };
								    
								    //For Doctor
								    $scope.example14modelDoctor = [];
								    $scope.example2settingsDoctor = {
								        displayProp: 'id'
								    };
								    
								    //For Modality
								    $scope.example14modelModality = [];
								    $scope.example2settingsModality = {
								        displayProp: 'id'
								    };
								    
								
								//Testing pouspose End
								
							}

							$scope.init();
							
							$scope.searchBlockSlots = function()
							{
								var specialityList = [];
								var doctorList = [];
								var subSpecialityList = [];
								var modalityList = [];
								
							
								
								var URI = BASE_URL + ROOT_URL + GETALLSLOTSBLOCKUNBLOCK;
								var data = {
									organisationId : $scope.organizationId,
									unitId : $scope.unitId,
									slotTypeId :  $scope.firstTabLevel,
									isBlocked : 1,
									fromDate:moment($scope.fromDate).format('YYYY-MM-DD'),
									toDate:moment($scope.toDate).format('YYYY-MM-DD'),
									
								}
								
								if($scope.firstTabLevel == 1){
									/* angular.forEach($scope.example14dataDoctor,function(value,index) {
		                            	  var splittedStringArray = value.id.split("_");
		                            	  doctorList.push(splittedStringArray[0]); 
		                            	  specialityList.push(splittedStringArray[1]);
										});
									 	data.listSpecialityId  = specialityList;
										data.listDoctorId = doctorList;*/
									//getting selected doctor list
								   	angular.forEach($scope.example14modelDoctor,function(value,index) {
		                            	  var splittedStringArray = value.id.split("_");
		                            	  doctorList.push(splittedStringArray[0]); 		                            	  
										});
										data.listDoctorId = doctorList;
										if (doctorList == undefined || doctorList == null || doctorList.length == 0)
											{
												growl.error("Please Select One Doctor Atleast!",
												{
													title:'Warning!'
												});
												return false;
											}
										//getting selected speciality list
										angular.forEach($scope.example14model,function(value,index) {
											specialityList.push(value.id);
										});
									 	data.listSpecialityId  = specialityList;
									}
								else{
									 angular.forEach($scope.example14modelModality,function(value,index) {
		                            	  var splittedStringArray = value.id.split("_");
		                            	  modalityList.push(splittedStringArray[0]); 
		                            	  subSpecialityList.push(splittedStringArray[1]);
		                            	  specialityList.push(splittedStringArray[2]);
										});
									 
										data.listSpecialityId = specialityList;
										data.listSubSpecialityId  = subSpecialityList;
										data.listModalityId = modalityList;
								}
								
								console.log("Data",data);
								$rootScope.startLoader();
								//return false;
								// For Get Nationality List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.doctorSlotList = [];

													if (response.data.status == "success") {
														$scope.doctorSlotList = response.data.listObject;
														console.log("Doc List",$scope.doctorSlotList);
													}
													$rootScope.stopLoader();
												});
							}
							$scope.searchUnblockSlots = function()
							{
								var specialityList = [];
								var doctorList = [];
								var subSpecialityList = [];
								var modalityList = [];
								
								
								var URI = BASE_URL + ROOT_URL + GETALLSLOTSBLOCKUNBLOCK;
								var data = {
									organisationId : $scope.organizationId,
									unitId : $scope.unitId,
									slotTypeId : $scope.firstTabLevel,
									isBlocked : 2,
									fromDate:moment($scope.fromDate).format('YYYY-MM-DD'),
									toDate:moment($scope.toDate).format('YYYY-MM-DD'),
									fromTime:$scope.fromTime,
									toTime:$scope.toTime,
								}
								
								if($scope.firstTabLevel == 1){
									/* angular.forEach($scope.example14dataDoctor,function(value,index) {
		                            	  var splittedStringArray = value.id.split("_");
		                            	  doctorList.push(splittedStringArray[0]); 
		                            	  specialityList.push(splittedStringArray[1]);
										});
									 	data.listSpecialityId  = specialityList;
										data.listDoctorId = doctorList;*/
									//getting selected doctor list
									angular.forEach($scope.example14modelDoctor,function(value,index) {
		                            	  var splittedStringArray = value.id.split("_");
		                            	  doctorList.push(splittedStringArray[0]); 		                            	  
										});
										data.listDoctorId = doctorList;		
										
										if (doctorList == undefined || doctorList == null || doctorList.length == 0)
										{
											growl.error("Please Select One Doctor Atleast!",
											{
												title:'Warning!'
											});
											return false;
										}
										//getting selected speciality list
										angular.forEach($scope.example14model,function(value,index) {
											specialityList.push(value.id);
										});
									 	data.listSpecialityId  = specialityList;
									 									 
									}
								else{
									 angular.forEach($scope.example14modelModality,function(value,index) {
		                            	  var splittedStringArray = value.id.split("_");
		                            	  modalityList.push(splittedStringArray[0]); 
		                            	  subSpecialityList.push(splittedStringArray[1]);
		                            	  specialityList.push(splittedStringArray[2]);
										});
									 
										data.listSpecialityId = specialityList;
										data.listSubSpecialityId  = subSpecialityList;
										data.listModalityId = modalityList;
										
										if(data.listModalityId.length == 0){
							        		growl.warning("Please select at least one modality..!!",
							        	    		{
							        					title : WARNING_MSG
							        				});
							        	    		return false;
							        	}
								}
								
								
								
								console.log("Data",data);
								$rootScope.startLoader();
								//return false;
								// For Get Nationality List Using Offset
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.doctorSlotList = [];

													if (response.data.status == "success") {
														$scope.doctorSlotList = response.data.listObject;
														console.log("Doc List",$scope.doctorSlotList);
													}
													$rootScope.stopLoader();
												});
							}
							
							
							
							//Start For Modality
							/*$scope.searchUnblockSlotsModality = function()
							{
								var specialityList = [];
								var subSpecialityList = [];
								var modalityList = [];
								 angular.forEach($scope.example14dataModality,function(value,index) {
	                            	  var splittedStringArray = value.id.split("_");
	                            	  modalityList.push(splittedStringArray[0]); 
	                            	  subSpecialityList.push(splittedStringArray[1]);
	                            	  specialityList.push(splittedStringArray[2]);
									});
								
								var URI = BASE_URL + ROOT_URL + GETALLSLOTSBLOCKUNBLOCKMODALITY;
								var data = {
									listSpecialityId : specialityList,
									listSubSpecialityId : subSpecialityList,
									listModalityId : modalityList,
									organisationId : $scope.organizationId,
									unitId : $scope.unitId,
									slotTypeId : $scope.slotTypeId,
									isBlocked : 2,
									fromDate:moment($scope.datepicker).format('YYYY-MM-DD'),
									toDate:moment($scope.datepickerTo).format('YYYY-MM-DD'),
									fromTime:$scope.fromTime,
									toTime:$scope.toTime,
								}
								console.log("Data",data);
								//return false;
								
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.doctorSlotList = [];

													if (response.data.status == "success") {
														$scope.doctorSlotList = response.data.listObject;
														console.log("Doc List",$scope.doctorSlotList);
													}
												});
							}
							$scope.searchBlockSlotsModality = function()
							{
								var specialityList = [];
								var subSpecialityList = [];
								var modalityList = [];
								 angular.forEach($scope.example14dataModality,function(value,index) {
	                            	  var splittedStringArray = value.id.split("_");
	                            	  modalityList.push(splittedStringArray[0]); 
	                            	  subSpecialityList.push(splittedStringArray[1]);
	                            	  specialityList.push(splittedStringArray[2]);
									});
								
								var URI = BASE_URL + ROOT_URL + GETALLSLOTSBLOCKUNBLOCKMODALITY;
								var data = {
									listSpecialityId : specialityList,
									listSubSpecialityId : subSpecialityList,
									listModalityId : modalityList,
									organisationId : $scope.organizationId,
									unitId : $scope.unitId,
									slotTypeId : $scope.slotTypeId,
									isBlocked : 1,
									fromDate:moment($scope.datepicker).format('YYYY-MM-DD'),
									toDate:moment($scope.datepickerTo).format('YYYY-MM-DD'),
									
								}
								console.log("Data",data);
								//return false;
								GenericService
										.serviceAction("POST", URI, data)
										.then(
												function(response) {
													$scope.doctorSlotList = [];

													if (response.data.status == "success") {
														$scope.doctorSlotList = response.data.listObject;
														console.log("Doc List",$scope.doctorSlotList);
													}
												});
								}*/
							//End For Modality
							
							 /******* START TAB FOR ALL PAGES IN TREE LEVEL *******/
							/*First Level*/
							$scope.firstTabLevel = 1;

							$scope.setFirstTabLevel = function(newTab) {

								$scope.firstTabLevel = newTab;
								$scope.slotIds = [];
								$scope.doctorSlotList =[];
								

								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};
								$scope.datepicker = {
										opened : false
									};
								$scope.dateOptions = {
										formatYear : 'yyyy',
										showWeeks : false,
								}
								
								
								$scope.openDatePickerTo = function() {
									$scope.datepickerTo.opened = true;
								};
								$scope.datepickerTo = {
										opened : false
									};
										
								$scope.fromTime = "";
								$scope.toTime = "";
										
							};

							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstTabLevel === tabNum;
							};
							/*Second Level*/
							$scope.secondTabLevel = 1 ;
							
							$scope.setSecondTabLevel = function(newTab)
							{
								$scope.secondTabLevel = newTab;
								
										$scope.example14model = [];
										$scope.example14modelDoctor =[];
										$scope.doctorSlotList =[];
										$scope.slotIds = [];
										
										$scope.openDatePicker = function() {
											$scope.datepicker.opened = true;
										};
										$scope.datepicker = {
												opened : false
											};
										$scope.dateOptions = {
												formatYear : 'yyyy',
												showWeeks : false,
										}
										
										
										$scope.openDatePickerTo = function() {
											$scope.datepickerTo.opened = true;
										};
										$scope.datepickerTo = {
												opened : false
											};
												
										$scope.fromTime = "";
										$scope.toTime = "";
												
										if(newTab == 1){
											$scope.isBlockedUnblocked = 2;
											$scope.isBlocked = 1;
										}
										else
											{
											$scope.isBlockedUnblocked = 1;
											$scope.isBlocked = 2;
											}
										$scope.example14SubSpeciality = [];
										$scope.example14modelModality = [];
										$scope.example14ModalityType = [];
									
							};
							
							$scope.isSetSecondTabLevel = function(tabNum)
							{
								return $scope.secondTabLevel === tabNum;
							};
							
							
							
						/******* END TAB FOR ALL PAGES IN TREE LEVEL *******/
							
							
							
							  $scope.clicked = function(type)
							  {
	                              if(type=='doctor'){
								  if($scope.example14model.length > 0){
	                              var speciality = [];
	                              angular.forEach($scope.example14model,function(value,index) {
	                            	  speciality.push(value.id);
									
									});
	                              var data = {
	                            		  organizationId:$scope.organizationId,
	                            		  unitId:$scope.unitId,
	                            		  specialityArray:speciality
	                              }
	                              console.log(data);
	                              var URI = BASE_URL + ROOT_URL
									+ GETDOCTORSLISTAGANISTSPECIALITIES;
							BillingGenericService
									.serviceAction(METHOD_POST, URI,
											data,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												// $rootScope.startLoader();
												if (response.data.status == "success")
													{
													$scope.example14dataDoctor = [];
													angular.forEach(response.data.listObject,function(value,index) {
																var data = {};
																data.id=value.doctorId+'_'+value.doctorSpecialityId;
																data.label=value.firstName+' '+value.lastName;
																$scope.example14dataDoctor.push(data);
															
															});
																											 
													}
													
											});
							  }
	                          else
	                            {
	                        	  
	                            	  $scope.example14dataDoctor = [];
	                            }
								  
	                         }
	                              else if(type=='speciality'){
	                            	  if($scope.example14ModalityType.length > 0){
	    	                              var modalityType = [];
	    	                              angular.forEach($scope.example14ModalityType,function(value,index) {
	    	                            	  modalityType.push(value.id);
	    									
	    									});
	    	                              var data = {
	    	                            		  organizationId:$scope.organizationId,
	    	                            		  unitId:$scope.unitId,
	    	                            		  specialityIdList:modalityType
	    	                              }
	    	                              console.log(data);
	    	                              var URI = BASE_URL + ROOT_URL
	    									+ GETSUBSPECIALITYBYSPECIALITYARRAY;
	    							BillingGenericService
	    									.serviceAction(METHOD_POST, URI,
	    											data,
	    											NOTIFICATION_MSG_STATUS_FALSE)
	    									.then(
	    											function(response) {
	    												console.log(response);
	    												// $rootScope.startLoader();
	    												if (response.data.status == "success")
	    													{
	    													$scope.example14dataSubSpeciality = [];
	    													angular.forEach(response.data.listObject,function(value,index) {
	    																var data = {};
	    																//alert(value.modalityDesc);
	    																data.id=value.subSpecialityId;
	    																data.label=value.subSpecialityName;
	    																$scope.example14dataSubSpeciality.push(data);
	    															
	    															});
	    																											 
	    													}
	    													
	    											});
	    							  }
	    	                          else
	    	                            {
	    	                        	  
	    	                            	  $scope.example14dataModality = [];
	    	                            }
	                              }
	                              else
	                            	  {

	                            	  if($scope.example14SubSpeciality.length > 0){
	    	                              var subSpecialityType = [];
	    	                              angular.forEach($scope.example14SubSpeciality,function(value,index) {
	    	                            	  subSpecialityType.push(value.id);
	    									
	    									});
	    	                              var data = {
	    	                            		  organizationId:$scope.organizationId,
	    	                            		  unitId:$scope.unitId,
	    	                            		 subSpecialityArray:subSpecialityType
	    	                              }
	    	                              console.log(data);
	    	                              var URI = BASE_URL + ROOT_URL
	    									+ GETMODALITYBYSUBSPECIALITYARRAY
	    							BillingGenericService
	    									.serviceAction(METHOD_POST, URI,
	    											data,
	    											NOTIFICATION_MSG_STATUS_FALSE)
	    									.then(
	    											function(response) {
	    												console.log(response);
	    												// $rootScope.startLoader();
	    												if (response.data.status == "success")
	    													{
	    													$scope.example14dataModality = [];
	    													angular.forEach(response.data.listObject,function(value,index) {
	    																var data = {};
	    																//alert(value.modalityDesc);
	    																data.id=value.modalityId+'_'+value.subSpecialityId+'_'+value.specialityId;
	    																data.label=value.modalityDesc;
	    																$scope.example14dataModality.push(data);
	    															
	    															});
	    																											 
	    													}
	    													
	    											});
	    							  }
	    	                          else
	    	                            {
	    	                        	  
	    	                            	  $scope.example14dataModality = [];
	    	                            }
	                            	  }
							  }
							  
							 
								// function for initializa clockpicker
								$scope.initClockPicker = function() {
									$('.clockpicker').clockpicker();
								}

							
							
							//for Days
							  $scope.example8data = [ {id: 1, label: "Monday"}, {id: 2, label: "Tuesday"},{id: 3, label: "Wednesday"},{id: 4, label: "Thursday"},{id: 5, label: "Friday"},{id: 6, label: "Saturday"}, {id: 7, label: "Sunday"}]; 
							  $scope.example8settings = { 
									checkBoxes: true, 
									dynamicTitle: true, 
									showUncheckAll: false, 
									showCheckAll: false ,
									scrollable: true,
							  };
							  
								//Get Doctor Slot Details
								$scope.getSlotDetails = function(item)
								{
									//alert(item.date);
									 var array = item.appointmentDate.split('/');
									 	var appointmentDateTemp=array[1]+"/"+array[0]+"/"+array[2];
									 	var dt=new Date(appointmentDateTemp);
									var data = {
											specialityId : item.specialityId,
											//doctorId:item.doctorId,
											appointmentDate :  moment(dt).format('YYYY-MM-DD'),
											organizationId:$scope.organizationId,
											unitId : $scope.unitId,
											slotTypeId : $scope.firstTabLevel,
											fromTime:$scope.fromTime,
											toTime:$scope.toTime,
											isBlockedUnblocked :$scope.isBlockedUnblocked,
											dayId : item.dayId,
									};
									
									if($scope.firstTabLevel == 1)
										{
											data.doctorId = item.doctorId;
										}
									else
										{
											data.modalityId = item.modalityId;
											data.subSpecialityId = item.subSpecialityId;
											
										}
									console.log(data);
									$rootScope.startLoader();
									//return false;
							var URI = BASE_URL + ROOT_URL
										+ GETBLOCKEDUNBLOCKEDSLOTDETAILS;
								BillingGenericService
										.serviceAction(METHOD_POST, URI,
												data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													console.log(response);
													$scope.slotDetailsList = [];
													// $rootScope.startLoader();
													if (response.data.status == "success")
														{
														  $scope.slotDetailsList = response.data.listObject;
															console.log( $scope.slotDetailsList); 
														}
													$rootScope.stopLoader();
														
												});
								}
							
								
								// function for updateStatus
        $scope.updateStatus = function(id, type,index) {
         
         
         $scope.currentDate = new Date();
         $scope.currentDate = moment($scope.currentDate)
           .format('DD-MM-YYYY HH:mm:ss');

         type = (type == "A") ? "A" : "I";

         var data = {
          slotId : id,
          status : type,
          updatedBy : $scope.userId,
          updatedDate : $scope.currentDate,
          organizationId:$scope.organizationId,
          unitId : $scope.unitId
         }
         console.log(data);
         var URI = BASE_URL + ROOT_URL
           + UPDATESLOTSTATUS;
         GenericService
           .serviceAction("POST", URI, data)
           .then(
             function(response) {

              if (response.data.status == "success") {
               growl
                 .success(
                   response.data.message,
                   {
                    title : 'Success!'
                   });
              } else {
               growl
                 .error(
                   response.data.message,
                   {
                    title : 'Error!'
                   });
               if(type == 'A')
                {
                $scope.slotDetailsList[index].status = 'I';
                }
               else
                {
                $scope.slotDetailsList[index].status = 'A';
                }
              }
             });
        }
        
        
        

        $scope.slotIds = [];
        $scope.checkAll = function(chkAll) {
        	$rootScope.startLoader();
        	if(chkAll == 'Y'){
        	angular.forEach($scope.doctorSlotList,function(value,index) {
				value.selectSlot = 'Y';
				$scope.slotIds.push(value.slotId);
			});
        	}else{
        		$scope.slotIds = [];
        		angular.forEach($scope.doctorSlotList,function(value,index) {
    				value.selectSlot = 'N';
    			});
        	}
        	$rootScope.stopLoader();
        };
          $scope.singleSlotCheck = function(item,idx) {
          if(item.selectSlot == 'Y'){
        	  $scope.slotIds.push(item.slotId);
          }else{
        	  $scope.slotIds.splice(idx, 1);
          }
          };
        
          
          $scope.blockUnblockSingleSlot = function(obj){
        	  $scope.slotIds=[];
        	$scope.slotIds.push(obj.slotId);
        	
          	console.log("$scope.slotIds",$scope.slotIds);
          	
          	if(($scope.reasonId == undefined) || ($scope.reasonId == null)|| ($scope.reasonId =="") || ($scope.reasonId == 0)){
        		growl.warning("Please select Reason..!!",
        	    		{
        					title : 'Warning!'
        				});
        			return false;
        	}
          	else{
          	var data = {
          		listSlotId : $scope.slotIds,
          		reasonId : $scope.reasonId,
          		//remark : $scope.remark,
          		createdBy: $scope.userId,
          		createdDate : moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss'),
          		isBlockUnblock :$scope.isBlocked
          	}
          	var header = {
          			organisationId : $scope.organizationId,
          			unitId : $scope.unitId
          	}
          	var param = {};
          	console.log(data);
          	$rootScope.startLoader();
          	var URI = BASE_URL + ROOT_URL + BLOCKUNBLOCKSLOT;
          	GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE,param,header)
  			.then(function(response) {
  					console.log(response);
  					$scope.slotDetailsList = [];
  						// $rootScope.startLoader();
  					if (response.data.status == "success"){
  						$scope.msg="";
  						if(response.data.object=='Y'){
  							angular.forEach(response.data.listObject,function(value,index) {
  								$scope.msg+=""+value.fromTimeString+", ";
  							
  							});
  							$scope.msg+=" This slot alredy booked!"
  							//slot alredy booked
  							angular.element('#bookedSlotDetails').modal('show');
  							$rootScope.stopLoader();
  						}else{
  							$rootScope.stopLoader();
  							growl.success(response.data.message,{title : SUCCESS_MSG});
  						}
  						console.log(response); 
  						$scope.init();
  						$scope.fromTime="";
  						$scope.toTime="";
  						$scope.doctorSlotList = [];
  						$scope.slotIds = [];
  						$scope.reasonId="";
  						$scope.remark="";
  						$scope.checkAllT='N';
  					}
  							
  					});
          }
          }

        $scope.blockUnblockSlot = function(){
        	if($scope.slotIds.length == 0){
        		growl.warning("Please select at least one slot..!!",
        	    		{
        					title : WARNING_MSG
        				});
        	    		return false;
        	}
        	else if(($scope.reasonId == undefined) || ($scope.reasonId == null)|| ($scope.reasonId =="") || ($scope.reasonId == 0)){
        		growl.warning("Please select Reason..!!",
        	    		{
        					title : 'Warning!'
        				});
        			return false;
        	}
        	else{
        	console.log("$scope.slotIds",$scope.slotIds);
        	var data = {
        		listSlotId : $scope.slotIds,
        		reasonId : $scope.reasonId,
        		remark : $scope.remark,
        		createdBy: $scope.userId,
        		createdDate : moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss'),
        		isBlockUnblock :$scope.isBlocked
        	}
        	var header = {
        			organisationId : $scope.organizationId,
        			unitId : $scope.unitId
        	}
        	var param = {};
        	console.log(data);
        	$rootScope.startLoader();
        	var URI = BASE_URL + ROOT_URL + BLOCKUNBLOCKSLOT;
        	GenericServiceParamHeader.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_TRUE,param,header)
			.then(function(response) {
					console.log(response);
					$scope.slotDetailsList = [];
						// $rootScope.startLoader();
					if (response.data.status == "success"){
						$scope.msg="";
						if(response.data.object=='Y'){
							angular.forEach(response.data.listObject,function(value,index) {
								$scope.msg+=""+value.fromTimeString+", ";
							
							});
							$scope.msg+=" This slot alredy booked!"
							//slot alredy booked
							angular.element('#bookedSlotDetails').modal('show');
							$rootScope.startLoader();
						}else{
							$rootScope.stopLoader();
							growl.success("Added Successfully.",{title : SUCCESS_MSG});
						}
						console.log(response); 
						$scope.init();
						$scope.fromTime="";
						$scope.toTime="";
						$scope.doctorSlotList = [];
						$scope.slotIds = [];
						$scope.reasonId="";
						$scope.remark="";
						$scope.checkAllT='N';
					}
							
					});
        }
        }
        
   /*     $scope.checkAll = function()
   	 {
   		 $scope.slotSelectedList.slots = $scope.slotSelectedList.map(function(item) { return item.slotId; });
   		  
   	 };*/
     
        $scope.slotIds = [];
  /*      $scope.checkAll = function(chkAll) {
	       if(mainChkBox.checked == true){
	        	console.log('let me check all');
	        	$("input:checkbox[name=idds]").each(function(){
	        		alert();
	        		$(this).attr('selected',true);
	        	});

        	
        	
            //$scope.slotIds = $scope.slotDetailsList.map(function(item) { return item.id; });
        	//alert(chkAll);
        	
        };*/
          $scope.uncheckAll = function() {
            $scope.slotIds = [];
          };
          
         
        	
        /*  $scope.checkAll = function () {           
        	  $scope.slotIds = [];
        	  if($scope.selectedAll=='Y'){
        		  angular.forEach($scope.slotDetailsList, function (item) {
                	  $scope.slotIds.push(item.slotId);
                  });  
        	  }
             
          };*/
} ]);