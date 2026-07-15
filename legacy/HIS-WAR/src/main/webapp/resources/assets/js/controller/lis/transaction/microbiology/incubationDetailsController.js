
/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"incubationDetailsController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'PagerService',
						'promiseFactoryWithObject',
						'$sessionStorage',
						'growl',
						'$stateParams',
						function($scope, $rootScope, $state, $cookies,
								GenericService, PagerService, promiseFactoryWithObject,
								$sessionStorage, growl,$stateParams) {

							
							$scope.isRecollected = false;
							$rootScope.loginpage = true;
							$scope.thirdLevel = false;
							
							$scope.LISDynamicLabel = "Sample";

							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.resultEntryWorklist  = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							$scope.labResultTestDtls;
							
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							/*** Lab Configuration */
							var labConfig  =    $scope.checkLabConfig();
							$scope.deptId = labConfig.labSpecialityId;
						    $scope.triggerReleaseMail = labConfig.triggerReleaseMail;
							
							$scope.listHelpValueMaster = [];
							$scope.incubationStartDate = moment().startOf('day').toDate();
							$scope.incubationStartTime;
							$scope.incubationPeriodId;
							$('[data-toggle="deltatooltip"]').tooltip();
							
							/**DatePicker Code  */

							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								minDate : new Date(),
							};

							$scope.open = function() {
								$scope.popup.opened = true;
							};

							$scope.popup =
							{
								opened : false
							};
					
							
							
							$scope.getIncubationPeriodList = function(orgId)
							{
								var data = "";
								try{
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_COMMON
								               	+ INCUBATION_PERIOD_LIST+S+$scope.orgId;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_GET, URI, data)
											.then(
													function(response) {
														$scope.incubationPeriodList = response.data.listObject;
														console.log("$scope.incubationPeriodList",$scope.incubationPeriodList);
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getIncubationPeriodList($scope.orgId);
							
							
							  $scope.organismGroupId = "";
							  $scope.organismId = "";
							/** Micro_organism Group List*/
							$scope.getMicroOrganismGroupList = function()
							{
								var data = "";
								try{
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_COMMON
								               	+ MICRO_ORGANISM_GROUP_LIST_BY_ORG_ID+S+$scope.orgId;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_GET, URI, data)
											.then(
													function(response) {
														$scope.microOrganismGroupList = response.data.listObject;
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							$scope.getMicroOrganismGroupList();
							
							
							$scope.getMicroOrganismList = function(organismGroupId)
							{
								var data = "";
								try{
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_COMMON
								               	+ MICRO_GET_ORGANISM_BY_GROUP+S+organismGroupId+S+'orgId'+S+$scope.orgId;
									console.log("URI", URI);

									GenericService
											.serviceAction(METHOD_GET, URI, data)
											.then(
													function(response) 
													{
														$scope.organismList = response.data.listObject;
														console.log("$scope.organismList",$scope.organismList);
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.openOrganism = function(){
								
								if($scope.tMicroIncubationMasterDto.isGrowthFound == "Y"){
									 $('#Organism_details').modal('show');
								}
								
								if($scope.tMicroIncubationMasterDto.isGrowthFound == "N" && $scope.microOrganismList != null && $scope.microOrganismList.length >0 ){
									  growl.warning("Please Remove Organism Group List.",
												{
													title : 'warning!'
												});
									  $scope.tMicroIncubationMasterDto.isGrowthFound = "Y";
									   return false;
								}
								
							}
							
							
							$scope.microOrganismList =[];
							   $scope.addMicroOrganismGroup = function()
							      {
							    	 
								   
								   if($scope.organismGroupId == ""){
									   growl.warning("Please Select Organism Group ",
												{
													title : 'warning!'
												});
									   return false;
								   }
								   
								   if($scope.organismId == ""){
									   growl.warning("Please Select Organism Name",
												{
													title : 'warning!'
												});
									   return false;
								   }
								   
							    	 var isExists;
							    	 var data = {
												"organismGroupId":"",
												"organismId":""
											};
							    	 
							    	 
							    	 data.organismGroupId = $scope.organismGroupId;
							    	 data.organismId = $scope.organismId;
							    	 
										function checkExistsOrNot(arr, val) {
											return arr
													.some(function(arr) {
														return (val.organismGroupId == arr.organismGroupId)
																&& (val.organismId == arr.organismId);
													});
										}
							    	
									
										isExists = checkExistsOrNot(
												$scope.microOrganismList,
												data);
										
										if(isExists)
											{
												growl.warning("Organism Group And Organism Already Exists",
													{
														title : 'warning!'
													});
											return false;
											}
										else{
											
									    	$scope.microOrganismGroupList.filter(function(microOrgGroupObj) {
													if (microOrgGroupObj.id ==  $scope.organismGroupId) 
													{
														data.organismGroupName = microOrgGroupObj.name;
													}
												});
									    	 $scope.organismList.filter(function(microOrgObj) {
													if (microOrgObj.id ==  $scope.organismId) 
													{
														data.organismName = microOrgObj.name;
													}
												});
									    	
									    	 $scope.microOrganismList.push(data);
											 $scope.organismId = "";
									    	 $scope.organismGroupId = "";
										    }
										
										
							    	 
							 }
							
							$scope.saveMicroOrganismList = [];
							$scope.removeOrganism = function(index){
								
								if($scope.microOrganismList[index].sesitivityResultDetailsId != undefined && $scope.microOrganismList[index].sesitivityResultDetailsId != null){
									$scope.saveMicroOrganismList.push($scope.microOrganismList[index]);
								}
								
								$scope.microOrganismList.splice(index, 1);
								
								if($scope.microOrganismList == null || $scope.microOrganismList.length ==0 ){
									$scope.tMicroIncubationMasterDto.isCompleted = 'N';
									$scope.tMicroIncubationMasterDto.partially = 'N';
									$scope.tMicroIncubationMasterDto.isGrowthFound = 'N';
								}
								
							}
							
							
							
							
							$scope.getIncubationMedia = function(labSampleDtlsId)
							{
								try{
									$scope.listTMicroSampleMediaDto = [];
									var data = "";
									var URI = BASE_URL + ROOT_URL
								            	+ LIS_MICROBIOLOGY
								               	+ GET_INCUBATION_MEDIA+S+labSampleDtlsId+S+$scope.orgId+S+$scope.orgUnitId;
									console.log("URI", URI);
									GenericService
											.serviceAction(METHOD_GET, URI, data)
											.then(
													function(response) {
														$rootScope.stopLoader();
														$scope.listTMicroSampleMediaDto = response.data.listObject;
														$("#mediaDetailsViewModal").modal('show');
														console.log("$scope.listTMicroSampleMediaDto",$scope.listTMicroSampleMediaDto);
													});
								} catch (e) {
									console.log(e.message)
								}
							}
							
							$scope.getIncubationDetails= function(tIncubationId) 
							{
									try 
									{
										$rootScope.startLoader();
										console.log("labResultMasterDtoList",JSON.stringify($scope.labResultMasterDtoList));
										var URI =BASE_URL + ROOT_URL
										+ LIS_MICROBIOLOGY
										+ GET_INCUBATION_TRANSACTION+S+tIncubationId+S+$scope.orgId+S+$scope.orgUnitId;
										console.log("URI",URI);
										GenericService.serviceAction("GET", URI, {}).then(function(response) 
										{	
											$scope.tMicroIncubationMasterDto = 	response.data.object;
											var listTMicroIncubationDetails =[];
											
											if(response.data.object.listTMicroIncubationDetailsMaster != null && response.data.object.listTMicroIncubationDetailsMaster.length>0){
												
												angular.forEach(response.data.object.listTMicroIncubationDetailsMaster, function (value, key) 
											    		{
															if(value.isDeleted =='N'){
																listTMicroIncubationDetails.push(value);
															}
													
											    		});
											}
											$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster = listTMicroIncubationDetails;
											if(response.data.object.sensitivityTestResultDetailsMasterDto != null ){
												$scope.microOrganismList = response.data.object.sensitivityTestResultDetailsMasterDto;
											}
											
											
										});
										
									} catch (e) {
										$rootScope.stopLoader();
										console.log("Exception",e.message);
									}
								
							}
							
							$scope.listTMicroIncubationDetailsMaster = [];
							
							$scope.tMicroIncubationMasterDto = {
								  "tIncubationId" : "",
								  "orgId" : $scope.orgId,
								  "orgUnitId" : $scope.orgUnitId,
								  "labSampleDtlsId" : $stateParams.incuObsrvationObj.labSampleDtlsId,
								  "isGrowthFound" : "N",
								  "isCompleted" : "N",
								  "createdBy" : $scope.userId,
								  "createdDate" : "",
								  "updatedBy" : "",
								  "updatedDate" : "",
								  "isDeleted" : "N",
								  "incubationDueTime" : "",
								  "listTMicroIncubationDetailsMaster" : [],
								  "incubationType":"",
								  "partially" :"N"
							
								}
							
							$scope.incubationTypeList= [
										{
											"id" : 1,
											"name" : "Aerobic",
										},
										{
											"id" : 2,
											"name" : "Unaerobic",
										}
							]
							
							$scope.tMicroIncubationDetailsMasterDto =  {
								  "tIncubationDetailsId" : "",
								  "tIncubationId" : "",
								  "incubationPeriodId":"",
								  "orgId" : $scope.orgId,
								  "orgUnitId" : $scope.orgUnitId,
								  "incubationStartTime" : "",
								  "incubationDueTime" : "",
								  "incubationStopTime" : "",
								  "remark" : "",
								  "createdBy" : $scope.userId,
								  "createdDate" : "",
								  "updatedBy" : "",
								  "updatedDate" : "",
								  "isDeleted" : "N",
								  "isIncubationStop" : "N"
								}
							
							
							/**Redirect Page Code*/
							if(angular.equals({},$stateParams.incuObsrvationObj))
 							{
								$state.go('IncubationMaster');
							} else {
								$scope.sampleDtls={};
								$scope.sampleDtls  = $stateParams.incuObsrvationObj;
								$scope.getIncubationMedia($stateParams.incuObsrvationObj.labSampleDtlsId);
								if($stateParams.incuObsrvationObj.tIncubationId!=null)
									{
									$scope.getIncubationDetails($stateParams.incuObsrvationObj.tIncubationId);
									}
							}
							
						
							
							$scope.createDateTime = function(date,time)
							{
								$scope.incubationDate = "";
								hourMinuitArray = time.split(":");
								
								var currentDate = $scope.incubationStartDate;
								var years =	moment(currentDate.getTime()).year();
								var month 	= moment(currentDate.getTime()).month()+1;
									var date 	= moment(currentDate.getTime()).date();
									var hour 	= parseInt(hourMinuitArray[0]);
									var minute = parseInt(hourMinuitArray[1]); 
									var second =	moment().second();
									var millisecond =  moment().millisecond();	
									console.log("date",moment({ years:years, months:month, date:date, hours:hour, minutes:minute, seconds:second, milliseconds:millisecond}));
									$scope.incubationDate = moment({ years:years, months:month, date:date, hours:hour, minutes:minute, seconds:second, milliseconds:millisecond}).toDate();
								//	$scope.incubationDate = moment({ years:years, months:month, date:date, hours:hour, minutes:minute, seconds:second, milliseconds:millisecond}).toDate();
									var finalHours = hour>9?hour:"0"+hour;
									var finalSeconds = second>9?second:"0"+second;
									var finalMinute = minute>9?minute:"0"+minute;
									var finalMillisecond = millisecond>9?millisecond:"0"+millisecond;
									
									var dt = years+"-"+month+"-"+date+" "+finalHours+":"+finalMinute+":"+finalSeconds+":"+finalMillisecond;
									$scope.incubationDate = moment(dt).toDate();
								    return moment({ years:years, months:month, date:date, hours:hour, minutes:minute, seconds:second, milliseconds:millisecond});
							}
							
							
				
							
							
							$scope.stopIncubation = function(index)
							{
								if(index!=null)
									{
									$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].isIncubationStop = 'Y';
									$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].incubationStopTime = moment().toDate().getTime();
									growl.success('Incubation Stop Successfully',
											{
												title : 'Success!'
											});
									}
								else{
									growl.error('Something Went wrong..',
											{
												title : 'Error!'
											});
								}
							}
							
							$scope.getHours = function(incubationPeriodId)
							   {
								$scope.inHours = $scope.incubationPeriodList
										.filter(function(incubationPeriodObj) 
										{
											if (incubationPeriodObj.id == incubationPeriodId) 
											{
												return incubationPeriodObj;
											}
										});
							   }
							
							$scope.startIncubation = function()
							{
								
								
								if($scope.incubationStartDate==null || $scope.incubationStartDate=='')
								{
								  growl.warning("Please enter incubation start date.",{
									title : WARNING_MSG
								  });
								    return false;
								}
								
								if($scope.incubationStartTime==null || $scope.incubationStartTime=='')
								{
								  growl.warning("Please enter incubation time.",{
									title : WARNING_MSG
								  });
								    return false;
								}
								
								
								
								var tMicroIncubationDetailsMasterDto = angular.copy($scope.tMicroIncubationDetailsMasterDto);
								tMicroIncubationDetailsMasterDto.incubationPeriodId = $scope.incubationPeriodId;
								$scope.createDateTime($scope.incubationStartDate,$scope.incubationStartTime);
								tMicroIncubationDetailsMasterDto.incubationStartTime = $scope.incubationDate.getTime();
								/*$scope.getHours($scope.incubationPeriodId);
								tMicroIncubationDetailsMasterDto.incubationDueTime = moment(tMicroIncubationDetailsMasterDto.incubationStartTime).add(parseInt($scope.inHours[0].label), 'hours').toDate().getTime();
								tMicroIncubationDetailsMasterDto.observationTime = tMicroIncubationDetailsMasterDto.incubationDueTime;*/
								$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster.push(tMicroIncubationDetailsMasterDto);
								$scope.incubationPeriodId = "";
								$scope.incubationStartDate = undefined;
								$scope.incubationStartTime = "";
							}
							
							$scope.restartIncubation =function(index)
							{
								
								if($scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].incubationStopTime==null||$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].incubationStopTime==undefined||$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].incubationStopTime=='')
								{
									growl.error('Please Stop Incubation First.',
											{
												title : 'Error!'
											});
								}
								else {
									
									var tMicroIncubationDetailsMasterDto = angular.copy($scope.tMicroIncubationDetailsMasterDto);
									tMicroIncubationDetailsMasterDto.incubationPeriodId=$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].incubationPeriodId;
									tMicroIncubationDetailsMasterDto.incubationStartTime = moment().toDate().getTime();
									$scope.getHours(tMicroIncubationDetailsMasterDto.incubationPeriodId);
									tMicroIncubationDetailsMasterDto.incubationDueTime = moment(tMicroIncubationDetailsMasterDto.incubationStartTime).add(parseInt($scope.inHours[0].label), 'hours').toDate().getTime();
									$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster.push(tMicroIncubationDetailsMasterDto);
									
								}
								
							}
							
							$scope.microIncubationDetailsInavtiveList = [];
							$scope.removeIncubation = function(index){
								
								if($scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].tIncubationDetailsId == null || $scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].tIncubationDetailsId == ""){
									$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster.splice(index,1);
								}else{
									$scope.microIncubationDetailsInavtiveList.push($scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index]);
									$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster.splice(index,1);
								}
								
								
							}
							
							
							
							
							$scope.addIncubation = function(incubationStartTime)
							{
								
								var tMicroIncubationDetailsMasterDto = angular.copy($scope.tMicroIncubationDetailsMasterDto);
								tMicroIncubationDetailsMasterDto.incubationStartTime = incubationStartTime;
								$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster.push(tMicroIncubationDetailsMasterDto);
								
							}
							
							$scope.changeIncubation = function(index,incubationPeriodId){
								
								
								if(incubationPeriodId == 0){
									growl.error('please select incubation period',
											{
												title : 'Error!'
											});
									return false;
								}else{
									
									var checkDuplicate = false;
									angular.forEach($scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster, function (value, key) 
								    		{
												
												if(value.incubationPeriodId == incubationPeriodId && index!=key){
													checkDuplicate = true;
												}
										
								    		});
									
									if(checkDuplicate){
										growl.error('please select different incubation period',
												{
													title : 'Error!'
												});
										$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].incubationPeriodId="";
										return false;
									}
									
									$scope.getHours(incubationPeriodId);
									$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].incubationDueTime = moment($scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].incubationStartTime).add(parseInt($scope.inHours[0].label), 'hours').toDate().getTime();
									$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].observationTime = moment($scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster[index].incubationStartTime).add(parseInt($scope.inHours[0].label), 'hours').toDate().getTime(); 
								}
								
								
							}
							
							$scope.saveTranaction = function(flag,saveFlag)
							{
								
								if(saveFlag == true){
									
									
									if($scope.tMicroIncubationMasterDto.partially != null && $scope.tMicroIncubationMasterDto.partially == 'Y' && flag == true){
										$("#saveConformPopup").modal('show');
										
									}else{
										$("#saveConformPopup").modal('hide');
										if($stateParams.incuObsrvationObj.tIncubationId!=null){
											$scope.updateIncubationDetails();
										}
										else{
											$scope.saveIncubationDetails();
										}
										
									}
									
								}else{
									$("#saveConformPopup").modal('hide');
								}
								
							}
							

							$scope.saveIncubationDetails= function() 
							{
									try 
									{
										
										var saveIncubation = function()
										{
											 $rootScope.startLoader();
											var URI =BASE_URL + ROOT_URL
											+ LIS_MICROBIOLOGY
											+ SAVE_INCUBATION_TRANSACTION;
											
											$scope.tMicroIncubationMasterDto.createdDate = moment().toDate().getTime();
											$scope.tMicroIncubationMasterDto.cultureTest = $scope.sampleDtls.cultureTest;
											$scope.tMicroIncubationMasterDto.testDesc = $scope.sampleDtls.testDesc;
											$scope.tMicroIncubationMasterDto.patientId = $scope.sampleDtls.patientId;
											$scope.tMicroIncubationMasterDto.doctorId = $scope.sampleDtls.doctorId;
											$scope.tMicroIncubationMasterDto.triggerReleaseMail = ($scope.triggerReleaseMail != null && $scope.triggerReleaseMail) == 'Y'? true : false;
										
											var sensitivityTestResultDetailsMasterDto = [];
											angular.forEach($scope.microOrganismList, function (value, key) 
										    		{
														var obj = {};
														
														obj.organismGroupId = value.organismGroupId;
														obj.organismId = value.organismId;
														
														sensitivityTestResultDetailsMasterDto.push(obj);
														
												
										    		});
											$scope.tMicroIncubationMasterDto.sensitivityTestResultDetailsMasterDto = sensitivityTestResultDetailsMasterDto;
											
											
											GenericService.serviceAction("POST", URI,$scope.tMicroIncubationMasterDto).then(function(response) 
											{	
												if (response.data.status == 'success')
												{
													$rootScope.stopLoader();
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
													$state.go('IncubationMaster');
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
										
										if($scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster==null || $scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster.length==0)
											{
											  growl.warning("Please start Incubation.",{
												title : WARNING_MSG
											  });
											    return false;
											}
										
										 saveIncubation();
									} catch (e) {
										$rootScope.stopLoader();
										console.log("Exception",e.message);
									}
									
							}
							
							$scope.updateIncubationDetails= function() 
							{
								try 
								{
									$rootScope.startLoader();
									var URI =BASE_URL + ROOT_URL
									+ LIS_MICROBIOLOGY
									+ UPDATE_INCUBATION_TRANSACTION;
									
									$scope.tMicroIncubationMasterDto.createdDate = moment().toDate().getTime();
									$scope.tMicroIncubationMasterDto.cultureTest = $scope.sampleDtls.cultureTest;
									$scope.tMicroIncubationMasterDto.testDesc = $scope.sampleDtls.testDesc;
									$scope.tMicroIncubationMasterDto.patientId = $scope.sampleDtls.patientId;
									$scope.tMicroIncubationMasterDto.doctorId = $scope.sampleDtls.doctorId;
									$scope.tMicroIncubationMasterDto.triggerReleaseMail = ($scope.triggerReleaseMail != null && $scope.triggerReleaseMail) == 'Y'? true : false;
									
									angular.forEach($scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster, function (value, key) 
								    		{
												
												if(value.tIncubationDetailsId == undefined || value.tIncubationDetailsId == null || value.observationTime == undefined ||  value.observationTime == null){
													value.observationTime = moment().toDate().getTime();
												}
										
								    		});
									
									var sensitivityTestResultDetailsMasterDto = [];
									angular.forEach($scope.microOrganismList, function (value, key) 
								    		{
												var obj = {};
												
												obj.organismGroupId = value.organismGroupId;
												obj.organismId = value.organismId;
												obj.sesitivityResultDetailsId = value.sesitivityResultDetailsId;
												obj.isDeleted ='N';
												sensitivityTestResultDetailsMasterDto.push(obj);
												
										
								    		});
									
										angular.forEach($scope.saveMicroOrganismList, function (value, key) 
								    		{
												var obj = {};
												
												obj.organismGroupId = value.organismGroupId;
												obj.organismId = value.organismId;
												obj.sesitivityResultDetailsId = value.sesitivityResultDetailsId;
												obj.isDeleted ='Y';
												sensitivityTestResultDetailsMasterDto.push(obj);
												
										
								    		});
										
										 
										angular.forEach($scope.microIncubationDetailsInavtiveList, function (value, key) 
									    		{
													var obj = value
													obj.isDeleted ='Y';
													$scope.tMicroIncubationMasterDto.listTMicroIncubationDetailsMaster.push(obj);
													
											
									    		});
									
									
									$scope.tMicroIncubationMasterDto.sensitivityTestResultDetailsMasterDto = sensitivityTestResultDetailsMasterDto;
									
									GenericService.serviceAction("POST", URI,$scope.tMicroIncubationMasterDto).then(function(response) 
									{	
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											$scope.microIncubationDetailsInavtiveList =[];
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
											$state.go('IncubationMaster');
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											$scope.microIncubationDetailsInavtiveList =[];
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
										}
									 });
									
								} catch (e) {
									$rootScope.stopLoader();
									$scope.microIncubationDetailsInavtiveList =[];
									console.log("Exception",e.message);
								}
							}
							
							
							$scope.saveOrganism = function(){
								
								if($scope.microOrganismList == null || $scope.microOrganismList.length ==0 ){
									$scope.tMicroIncubationMasterDto.isCompleted = 'N';
								}else{
									$scope.tMicroIncubationMasterDto.isCompleted = 'Y';
								}
							}
							
							$scope.changeStatus = function(status){
								
								if(status){
									$scope.tMicroIncubationMasterDto.partially = 'N';
									
								}else{
									$scope.tMicroIncubationMasterDto.isCompleted = 'N';
								}
							}
							
							
							
							
						} ]);