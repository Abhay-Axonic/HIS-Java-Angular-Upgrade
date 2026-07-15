angular
		.module('myApp')
		.controller(
				'OTIMSdashboardController',
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
						'$window',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$window) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.updateBtnFlag = false;//added by shital nitin
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							$scope.visitTypeId = 0;
							$scope.fromDate = moment().toDate();
						    $scope.toDate = moment().toDate();
						    
							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
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
			
			
							$scope.procedureList = []; 
							$scope.serachProcedure = function()
							{
								$scope.procedureList = []; 
								try {
									$scope.startLoader();
									var data = {
											"fromDate":moment($scope.fromDate).format('YYYY-MM-DD'),
											"toDate":moment($scope.toDate).format('YYYY-MM-DD'),
											"visitTypeId" :$scope.visitTypeId,
											"orgId" : $scope.organizationId,
											"unitId" : $scope.unitId
										};
									var URI = BASE_URL + ROOT_URL+"/api/procedure/order/search";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
															angular.forEach(response.data.listObject,function(value,index){
															var procedures = "";
															var procedureTypes = "";
															var OTTypes = "";
															var totalDuration = 0;
															var preferedDateTime ="";
															var preferedTime = "";
																angular.forEach(value.procedureOrderMaster,function(value1,index){
																
																	if(index == 0){
																		
																		procedures = ""+value1.procedureName;
																		procedureTypes = ""+value1.procedureType;
																		OTTypes = ""+value1.theatreTypeName;
																	}else{
																		
																		procedures = procedures+","+value1.procedureName;
																		procedureTypes = procedureTypes+","+value1.procedureType;
																		OTTypes = OTTypes+","+value1.theatreTypeName;
																	}
																	
																	totalDuration = totalDuration + value1.duration;
																	
																	if(value1.isPrimarySecondary == "Y"){
																		preferedDateTime = value1.preferedDateTime;
																		preferedTime = value1.preferedTime;
																	}
															});
																value.procedures = procedures;
																value.procedureTypes = procedureTypes;
																value.totalDuration = totalDuration;
																value.OTTypes = OTTypes;
																value.preferedDateTime = preferedDateTime;
																value.preferedTime = preferedTime;
														});
														$scope.procedureList = response.data.listObject;
														$scope.stopLoader();
													});
									
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}

							}
							
							$scope.serachProcedure();
							
							$scope.plannedProcedureList = []; 
							$scope.searchPlannedProcedure = function(){
								
								$scope.plannedProcedureList = []; 
								try {
									$scope.startLoader();
									var data = {
											"fromDate":moment($scope.fromDate).format('YYYY-MM-DD'),
											"toDate":moment($scope.toDate).format('YYYY-MM-DD'),
											"visitTypeId" :$scope.visitTypeId,
											"orgId" : $scope.organizationId,
											"unitId" : $scope.unitId
										};
									var URI = BASE_URL + ROOT_URL+"/api/procedure/plannedProcedure/search";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														$scope.plannedProcedureList = response.data.listObject;
														$rootScope.stopLoader();
														
														console.log("$scope.plannedProcedureList",$scope.plannedProcedureList);
													});
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}
							}
							
/************************************* GET SURGIES OF THE DAY START *********************************************/
							
							$scope.surgeriesOfTheDayList =[];
							$scope.getSurgeriesOfTheDay = function(){
								$scope.surgeriesOfTheDayList = []; 
								try {
									$scope.startLoader();
									var data = {
											"createdDate":moment(new Date()).format('YYYY-MM-DD'),
											"organizationId" : $scope.organizationId,
											"unitId" : $scope.unitId
										};
									var URI = BASE_URL + ROOT_URL+"/api/otims/dashboard/getSurgeriesOfTheDayList";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														$scope.surgeriesOfTheDayList = response.data.listObject;
														$rootScope.stopLoader();
														
														console.log("$scope.surgeriesOfTheDayList",$scope.surgeriesOfTheDayList);
													});
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}	
							}
							
							$scope.getSurgeriesOfTheDay();
							
							$scope.diagnosisList =[];
							$scope.getDiagnosisByPatientId = function(obj){
								try {
									$scope.startLoader();
									$scope.diagnosisList = [];
									var visitId ="";
									var visitTypeId="";
									if(obj.admissionId!=null){
										visitTypeId = 2;
										visitId = obj.admissionId;
									}else{
										visitTypeId = 1;
										visitId = obj.encounterId;
									}
									var URI = BASE_URL + ROOT_URL+"/api/opd/patientDiagnosis/"+obj.patientId+"/"+visitId+"/"+visitTypeId;
									//var URI = BASE_URL + ROOT_URL+"/api/opd/patientDiagnosis/7816/5689/2";
									console.log("URI", URI);
									GenericService
											.serviceAction("GET", URI, "")
											.then(
													function(response) {
														$scope.diagnosisList = response.data.listObject;
														$rootScope.stopLoader();
														
														console.log("$scope.diagnosisList",$scope.diagnosisList);
													});
									$rootScope.stopLoader();
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}	
							}

							

							$scope.userOtTabs = {
									otDetails:false,
									procedureDetails:false,
									service:false,
									inventry:false,
									equipment:false,
									cssdKit:false,
									notes:false,
									timeSheet:false
							};
							$scope.tabAcces = false;
							
							$scope.getUserOtTabs = function(){
								$rootScope.startLoader();
								var URI = BASE_URL+ ROOT_URL+ "/api/global/getUserOtTabsByUserId/"+$scope.userId;
								console.log("URI", URI);
								GenericService.serviceAction("GET", URI,{}).then(function(response) {
									$rootScope.stopLoader();
									if(response.data.listObject != null && response.data.listObject.length>0){
										
										$scope.userOtTabs.cssdKit = response.data.listObject[0].cssdKit;
										$scope.userOtTabs.equipment = response.data.listObject[0].equipment;
										$scope.userOtTabs.inventry = response.data.listObject[0].inventry;
										$scope.userOtTabs.notes = response.data.listObject[0].notes;
										$scope.userOtTabs.otDetails = response.data.listObject[0].otDetails;
										$scope.userOtTabs.procedureDetails = response.data.listObject[0].procedureDetails;
										$scope.userOtTabs.service = response.data.listObject[0].service;
										$scope.userOtTabs.timeSheet = response.data.listObject[0].timeSheet;
									}
									
									if($scope.userOtTabs.cssdKit == false && $scope.userOtTabs.equipment ==false && $scope.userOtTabs.inventry == false
											&& $scope.userOtTabs.notes == false && $scope.userOtTabs.otDetails == false &&
											$scope.userOtTabs.procedureDetails == false && $scope.userOtTabs.service == false 
											&& $scope.userOtTabs.timeSheet == false){
										$scope.tabAcces = true;
									}
								});
							}
							$scope.getUserOtTabs();
							
							$scope.otCompletionDetails = function(info){
								info.renderedFromDashboard = 1;
								if($scope.tabAcces){
									growl.warning("Access Denied",
											{
												title: 'warning!'
											});
										return false;
								}else{
									
									info.cssdKit = $scope.userOtTabs.cssdKit;
									info.equipment = $scope.userOtTabs.equipment;
									info.inventry = $scope.userOtTabs.inventry;
									info.notes = $scope.userOtTabs.notes;
									info.otDetails = $scope.userOtTabs.otDetails;
									info.procedureDetails = $scope.userOtTabs.procedureDetails;
									info.service = $scope.userOtTabs.service;
									info.timeSheet = $scope.userOtTabs.timeSheet;
									
								$state.go('otBillingCompletionDetails',
										{
											resultCommonObj: info
										});
								
								}
								
							}
						
							$scope.goToPreOperative = function(info){
								
								$state.go('preOperative',
										{
											resultCommonObj: info
										});
							}
						
							
/************************************* GET SURGIES OF THE DAY END *********************************************/
							$scope.searchPlannedProcedure();
							
							$scope.setProcedureOrder = function(info){
								
								$state.go('OTIMSdashboardInfo',
										{
											procedureObj: info
										});
							}
							
							$scope.detailsPlannedProcedure = function(info){
								
								if(info.otReservationId == null){
									
									$state.go('plannedProcedure',
											{
												procedureObj: info
											});
									
								}else{
									
									$state.go('OTreservation',
											{
										commonObj: info
											});
									
									/* growl.warning(" This procedure Schedule from OT reservation .. So you can Reschedule from ot reservation worklist", {
			                                title : 'warning!'
			                            });*/
								}
								
								
							}
							
							$scope.switchTab =  function(){
								 $scope.setSecondTabLevel(1);
							} 
							
							$scope.goToOTChecklist = function(info){
								/*$state.go('OTchecklist',
										{
											checklistObj: info
										});*/
								info.renderedFromSurgery = 1; 
								$state.go('OTchecklistDetails',
										{
											checklistDetailsObj: info
										});
							}
							
							$scope.firstTabLevel = 1 ;	
							
							
							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.firstTabLevel === tabNum;
								
							};
							
							$scope.setFirstTabLevel= function(newTab)
							{
								$scope.firstTabLevel = newTab;
							};
							
							$scope.setFirstTabLevel(1);
							
							
							$scope.surgeonList = [];
							$scope.getSurgeonList = function(info){
								try {
									$scope.startLoader();
									$scope.surgeonList = [];
									var data = {
											//"roleId": 3,
											"procedureScheduleId":info.procedureScheduleId,
											"organizationId" : $scope.organizationId,		
											"unitId" :  $scope.unitId
										};
									var URI = BASE_URL + ROOT_URL+"/api/otims/dashboard/getProcedureSchedulingSurgeonList";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														$scope.surgeonList = response.data.listObject;
														$rootScope.stopLoader();
													});
									
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
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
						 

						
						}]);
						