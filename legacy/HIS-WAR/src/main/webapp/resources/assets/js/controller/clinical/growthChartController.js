/**
 * @author Ganesh Chaudhari
 */

angular
		.module('myApp')
		.controller(
				"growthChartController",
				[
						'$scope',
						'$rootScope',
						'$state',
						'$cookies',
						'GenericService',
						'BillingGenericService',
						'PagerService',
						'promiseFactory',
						'$sessionStorage',
						'growl',
						function($scope, $rootScope, $state, $cookies,
								GenericService, 
								BillingGenericService ,PagerService, promiseFactory,
								$sessionStorage, growl) {

							$rootScope.loginpage = true;
							$scope.LISDynamicLabel = "";
							$scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							$scope.updateScope = false;
							$scope.growthChart = true;

							/** Variables for pagination */
							
							$scope.deltaNo = true;
							$scope.deltaText = false;
							
							/*** Check user is Loggedin */
							var cookieObject,cookieObjectAppConfig;
							[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
							$scope.orgUnitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.genderId;

							/* code for setting Label */
							
							$scope.openDatePickerPatiSearch = function() {
								$scope.datepicker1.opened = true;
							};

							$scope.datepicker1 = {
									opened : false
								};



							$scope.openRenderedDate = function() {
								$scope.ordRenderDatetime.opened = true;
							};

							$scope.ordRenderDatetime = {
									opened : false
								};


							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false
								};
							
							$scope.datepickerList = [];
							$scope.openDatePicker = function(index) 
							{
								$scope.datepickerList[index].opened = true;
							};
							
							$scope.patientSearchObj = {
								      "uhIdNumber"      : "",
								      "patientName"     : "",
								      "genderId"        : "",
								      "mobileNo"        : "",
								      "birthDate"       : "",
								      "identificationNo":"",
								      "patientType"   :"1",
								      "unitId"          :$scope.unitId,
								      "organizationId"  :$scope.organizationId
								    }
							
                          $scope.loadMoreData = function()
                            {
                                $scope.offset+=200;
                                $scope.loadMoreFlag=true;
                                $rootScope.searchPatient($scope.patientSearchObj);
                            }
                            $scope.offset=0;
                            $scope.loadMoreFlag=false;
                            $scope.noOfRecordsPerPage=200;
                            $scope.patientList = [];
							
							$scope.searchPatient = function(patientSearchObj)
							{
								
								$rootScope.startLoader();
								$scope.patientSearchObj.unitId = cookieObject.unitId;
								$scope.patientSearchObj.organizationId = cookieObject.organizationId;
								var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteriaGrowthChart";
								var patSearchObj = angular.copy($scope.patientSearchObj);
								if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
									var dt = new Date($scope.patientSearchObj.birthDate);
									patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
								}

								if($scope.loadMoreFlag==true)
                                	    	{
                                	    	patSearchObj.offset=$scope.offset;
                                	    	$scope.loadMoreFlag=false;
                                	    	}
                                	    else
                                	    	{
                                	    	patSearchObj.offset=0;
                                	    	$scope.loadMoreFlag=false;
                                	    	$scope.patientList = [];
                                	    	}
                                	    patSearchObj.noOfRecordsPerPage=$scope.noOfRecordsPerPage;

								BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.searchPatientList = [];
									console.log("response",response);
									if (response.data.status == "success")
										{     
										 $scope.patientList = response.data.listObject;
							            }
									  $rootScope.stopLoader();
								  }).catch(function(error) {
								        console.error('Error during patient search:', error);
								        $rootScope.stopLoader();
								});
								
							};
							
							function calculateAgeInMonths(birthDate) {
							 var today = new Date();
		                     var startDate = new Date(birthDate);
		                     var yearsDifference = today.getFullYear() - startDate.getFullYear();
		                     var ageInMonths = yearsDifference * 12 + yearsDifference;
		                     if (today.getDate() < startDate.getDate()) {
		                         ageInMonths--;
		                     }
		                     return ageInMonths;}
							
							$scope.patientDoB = "";
							$scope.patientId = null;
							
							$scope.getPatientDetailsById = function(patientId){
								$rootScope.startLoader();
								$scope.patientDetailsList = [];
								$scope.patientDetails = {}
								
							
								var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
								BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$scope.patientDetailsList = response.data.listObject;
										$scope.patientDetails = response.data.listObject[0];
										$scope.genderId = $scope.patientDetails.genderId;
										 var datePartsad = $scope.patientDetails.dob.split('-');
										 $scope.patientDoB = datePartsad[2]+"-"+ datePartsad[1]+"-"+ datePartsad[0]; 
											$scope.patientId = patientId;
										$scope.getGrowthChart(patientId);
										$scope.getGrowthDevelopmentalMilestone(patientId);
									}
									else if (response.data.status == "error"){
										$rootScope.stopLoader();
									}
								});

							}
							
							
							//For Get Active Gender List
							$scope.getSexMasterList = function() {
								var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
								BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									$scope.sexMasterList = response.data.listObject;
								});
							}

							$scope.getSexMasterList();
							$scope.getIdentificationTypeList = function(){
								var URI = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
								BillingGenericService.serviceAction(METHOD_GET, URI, "",NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
									if (response.data.status == "success"){
										$scope.identificationTypeList = response.data.listObject;
									}
								});
							}
							
							
							$scope.init = function()
							{
								$scope.growthChartMasterList = [];
								$scope.tGrowthChartMaster = {
										  "tGrowthChartId" : "",
										  "growthChartId" : "",
										  "growthChartDate" : "",
										  "weight" : "",
										  "height" : "",
										  "circumference" : "",
										  "ageInMonths" : "",
										  "weightFrom" : "",
										  "weightTo" : "",
										  "weightUnit" : "",
										  "weightUnitId" : "",
										  "heightFrom" : "",
										  "heightTo" : "",
										  "heightUnit" : "",
										  "heightUnitString" : "",
										  "circumferenceFrom" : "",
										  "circumferenceTo" : "",
										  "circumferenceUnit" : "",
										  "circumferenceUnitString" : "",
										  "patientId" : "",
										  "visitTypeId" : "",
										  "admissionId" : "",
										  "encounterId" : "",
										  "organisationId" : $scope.orgId,
										  "unitId" : $scope.orgUnitId,
										  "status" : "A",
										  "createdDate" : "",
										  "createdBy" : $scope.userId,
										  "updatedBy" : $scope.userId,
										  "updatedDate" : ""
										}
								
							}
							$scope.init();

							
							

					/** =====================>Code for Push Pop Objects<===============*/
				


							
							
							
							$scope.getGrowthChart = function(patientId)
							{
								try {
									$scope.patientId = patientId;
									$scope.growthChartList = []
									var data ={
											  "patientId" : patientId,
											  "visitTypeId" : "2",
											  "admissionId" :  "",
											  "encounterId" : "",
											  "organisationId" :$scope.orgId,
											  "unitId" : $scope.orgUnitId,
											  "genderId" : $scope.genderId
											}
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + GET_GROWTH_CHART_TRANSACTION;
									console.log("EDIT",URI);
									GenericService.serviceAction("POST", URI, data).then(function(response) 
									{
										console.log("data",response.data);
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											$scope.growthChartList = response.data.object;
											
											angular.forEach($scope.growthChartList, function(obj, key) 
													{
												     obj.growthChartDate = moment(obj.growthChartDate).toDate();
												     $scope.datepickerList.push({opened:false});
												});
                                            $scope.growthChartList.sort((a, b) => a.ageInMonths - b.ageInMonths);

										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											alert("Error In Fetching Data");
										}
									});
								} catch (e) {
									console.log("Exception",e.message);
								}
								
							}
							
							
							 $scope.openRenderedDate = function(index) {

						         for (var i = $scope.growthDevelopmentalMilestones.length - 1; i >= 0; i--) {
						             if (i === index) {

						                 $scope.ordRenderDatetime.opened[i] = true;
						             } else {
						                 $scope.ordRenderDatetime.opened[i] = false;
						             }
						         }
						     };
							
						     $scope.ordRenderDatetime = {
							         opened : [ false ],

							     };
						     
							$scope.saveGrowthChart = function() 
							{
								 var validationFailed = false;
								
								if($scope.selectedTab == 1){
									
									$scope.saveGrowthChartList = [];
									$scope.$broadcast('show-errors-check-validity');
										try 
										{    var saveFlagList = [];
										     var saveFlag = false;
											angular.forEach($scope.growthChartList, function(obj, key)
                                            {
                                              if(obj.tGrowthChartId==null || obj.tGrowthChartId=="")
                                              {
                                                 if((obj.growthChartDate !== undefined && obj.growthChartDate !== null && obj.growthChartDate !== "" && !isNaN(obj.growthChartDate.getTime())) && (!saveFlag))
                                                 {
                                                    if((obj.height === undefined || obj.height === null || obj.height === "") ||
                                                       (obj.circumference === undefined || obj.circumference === null || obj.circumference === "") ||
                                                       (obj.weight === undefined || obj.weight === null || obj.weight === ""))
                                                    {
                                                        saveFlag = false;
                                                        saveFlagList.push(saveFlag);
//                                                        obj.growthChartDate = null;
                                                        return false;
                                                    }
                                                    else{
                                                        saveFlag = true;
                                                    }
                                                 }

                                                 if((obj.height !== undefined && obj.height !== null && obj.height !== "") && (!saveFlag))
                                                 {
                                                    if((obj.growthChartDate === undefined || obj.growthChartDate === null || obj.growthChartDate === "") ||
                                                        (obj.circumference === undefined || obj.circumference === null || obj.circumference === "") ||
                                                        (obj.weight === undefined || obj.weight === null || obj.weight === ""))
                                                    {
                                                        saveFlag = false;
                                                        saveFlagList.push(saveFlag);
                                                        return false;
                                                    }
                                                    else{
                                                        saveFlag = true;
                                                    }
                                                 }

                                                 if((obj.circumference !== undefined && obj.circumference !== null && obj.circumference !== "") && (!saveFlag))
                                                 {
                                                    if((obj.height === undefined || obj.height === null || obj.height === "") ||
                                                       (obj.growthChartDate === undefined || obj.growthChartDate === null || obj.growthChartDate === "") ||
                                                       (obj.weight === undefined || obj.weight === null || obj.weight === ""))
                                                    {
                                                        saveFlag = false;
                                                        saveFlagList.push(saveFlag);
                                                        return false;
                                                    }
                                                    else{
                                                        saveFlag = true;
                                                    }
                                                 }

                                                 if((obj.weight !== undefined && obj.weight !== null && obj.weight !== "") && (!saveFlag))
                                                 {
                                                    if((obj.height === undefined || obj.height === null || obj.height === "") ||
                                                       (obj.circumference === undefined || obj.circumference === null || obj.circumference === "") ||
                                                       (obj.growthChartDate === undefined || obj.growthChartDate === null || obj.growthChartDate === ""))
                                                    {
                                                        saveFlag = false;
                                                        saveFlagList.push(saveFlag);
                                                        return false;
                                                    }
                                                    else{
                                                        saveFlag = true;
                                                    }
                                                 }

                                                 if(saveFlag){
                                                      var grwothchar = angular.copy(obj);
                                                     grwothchar.growthChartDate = moment(obj.growthChartDate).format('DD-MM-YYYY HH:mm:ss');
                                                     grwothchar.patientId = $scope.patientId;
                                                     grwothchar.visitTypeId= 2;
                                                     grwothchar.organisationId=$scope.orgId;
                                                     grwothchar.unitId= $scope.orgUnitId;
                                                     grwothchar.status='A';
                                                     grwothchar.createdBy= $scope.userId;
                                                     grwothchar.updatedBy= $scope.userId;
                                                     $scope.saveGrowthChartList.push(grwothchar);
                                                     saveFlag = false;
                                                 }
                                              }
                                            });

									           if(saveFlagList !== undefined && saveFlagList !== null && saveFlagList.length !== 0){
									                growl.warning("Enter Valid data!", {
                                                        title: "Warning"
                                                    });
                                                    $scope.saveGrowthChartList = [];
									           }
											   else{
                                                   if ($scope.saveGrowthChartList.length == 0) {
                                                       growl.warning("No valid growth chart data to save.", {
                                                           title: "Warning"
                                                       });
                                                       $scope.saveGrowthChartList = [];
                                                       return false;
                                                   }
									           }

                                            if($scope.saveGrowthChartList !== undefined && $scope.saveGrowthChartList !== null && $scope.saveGrowthChartList.length > 0){
                                                var URI = BASE_URL + ROOT_URL + SAVE_GROWTH_CHART_TRANSACTION;
                                                $rootScope.startLoader();
                                                GenericService.serviceAction("POST", URI, $scope.saveGrowthChartList).then(function(response)
                                                {
                                                    $rootScope.stopLoader();
                                                    if (response.data.status == 'success')
                                                    {
                                                        $rootScope.stopLoader();
                                                        growl.success(response.data.message,
                                                                {
                                                                    title : 'Success!'
                                                                });
                                                        $scope.$broadcast('show-errors-reset');
                                                        $scope.init();
                                                        $scope.getPatientDetailsById( $scope.patientId);

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
										} catch (e) {
											console.log("Exception",e.message);
											$rootScope.stopLoader();
										}
									
								}else{
									
									$scope.checkflag = false;
									
									$scope.saveDevelopmentalMilestone = [];
									
											angular.forEach($scope.growthDevelopmentalMilestones, function(obj, key) 
													{
												      if(obj.selected == true && obj.growthDevelopmentalMilestonesTransactionId == null )
												    	 {
												    	  var grwothchar = angular.copy(obj);
												         grwothchar.achievedDate = moment(obj.achievedDate).format('DD-MM-YYYY HH:mm:ss');
												         grwothchar.growthDevelopmentalMilestonesId = obj.growthDevelopmentalMilestonesId
												         grwothchar.patientId = $scope.patientId;
												         grwothchar.organizationId=$scope.orgId;
												         grwothchar.unitId= $scope.orgUnitId;
												         grwothchar.status='A';
												         grwothchar.createdBy= $scope.userId;
												         grwothchar.updatedBy= $scope.userId;
												         $scope.saveDevelopmentalMilestone.push(grwothchar);
												         
												         if(obj.achievedDate == null){
												        	 
												        	 $scope.checkflag = true;
												        	 
												         }
												         
												    	}
												});
											
											if($scope.checkflag){
									        	 
												 growl.warning("Please select achieved Date for selected milestone. ",{
														title : "warning"
													});
									    		 return false;									        	 
									         }
											
											if($scope.saveDevelopmentalMilestone.length == 0){
									        	 
												 growl.warning("Please select atleast one milestone. ",{
														title : "warning"
													});
									    		 return false;									        	 
									         }
											
										    var URI = BASE_URL + ROOT_URL + "/api/global/growthDevelopmentalMilestones/patient";
										    $rootScope.startLoader();
											GenericService.serviceAction("POST", URI, $scope.saveDevelopmentalMilestone).then(function(response) 
											{	
												 $rootScope.stopLoader();
												if (response.data.status == 'success')
												{
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
													$scope.getGrowthDevelopmentalMilestone($scope.patientId );
													
													
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
							
							$scope.selectedTab = 1;
							
							$scope.changetab = function(tab){
								$scope.selectedTab = tab;
							}
							
							$scope.getGrowthDevelopmentalMilestone = function(patientId)
							{
								try {
									$scope.patientId = patientId;
									$scope.growthDevelopmentalMilestones = []
									var data ={
											  "patientId" : patientId,
											  "organizationId" :$scope.orgId,
											  "unitId" : $scope.orgUnitId,
											}
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/global/growthDevelopmentalMilestones/patient/detail";
									console.log("EDIT",URI);
									GenericService.serviceAction("POST", URI, data).then(function(response) 
									{
										console.log("data",response.data);
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											$scope.growthDevelopmentalMilestones = response.data.listObject;
											
											angular.forEach($scope.growthDevelopmentalMilestones, function(obj, key) 
													{
														if(obj.growthDevelopmentalMilestonesTransactionId == null){
																obj.selected = false;
														}else{
																obj.achievedDate = moment(obj.achievedDate).toDate();
																obj.selected = true;
															}
												     
												});
											
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											alert("Error In Fetching Data");
										}
									});
								} catch (e) {
									console.log("Exception",e.message);
								}
								
							}

							$scope.calculateAge =  function(inx,achievedDate){
								
								var fromDate = new Date($scope.patientDoB);
						        var toDate = new Date(moment(achievedDate).format('DD-MM-YYYY HH:mm:ss'));

						        // Calculate the total number of months between two dates
						        var months = (toDate.getFullYear() - fromDate.getFullYear()) * 12;
						        months -= fromDate.getMonth();
						        months += toDate.getMonth();
						        
						        // Adjust for negative months
						        if (toDate.getDate() < fromDate.getDate()) {
						            months--;
						        }
						        $scope.growthDevelopmentalMilestones[inx].actualAgeOfMilestoneAchieved =months;
								
							}
							

						} ]);