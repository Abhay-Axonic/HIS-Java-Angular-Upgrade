angular
		.module('myApp')
		.controller(
				'OTchecklistDetailsController',
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
						'$stateParams',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService, $stateParams) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.updateBtnFlag = false;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.mapOfBed = 1;
							$scope.currentTab = 1;
							$scope.isSetFirstTabLevel = function(tabNum) {
								return $scope.currentTab  === tabNum;
								
							};
							$scope.isSetFirstTabLevel(1);
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							$scope.checklistDetailsObj = $stateParams.checklistDetailsObj;
							
							
							console.log("$scope.checklistDetailsObj",$scope.checklistDetailsObj);
							$scope.newpreferedDateTime = moment($scope.checklistDetailsObj.preferedDateTime).format('DD/MM/YYYY');
							$scope.newOrdereDate = moment($scope.checklistDetailsObj.createdDate).format('DD/MM/YYYY');
							/*$scope.uhidNum = $scope.checklistDetailsObj.uhidNumber;
							$scope.dateOfAdmission =  $scope.checklistDetailsObj.doa;
							$scope.patientDischargeDate = $scope.checklistDetailsObj.pdd;
							$scope.bedNumber =  $scope.checklistDetailsObj.bedNumber;
							$scope.doctorFullName = $scope.checklistDetailsObj.doctorName;*/
							$scope.mapOfBed = $scope.checklistDetailsObj.mapOfBed;
							$scope.patientDetails.admissionId = $scope.checklistDetailsObj.admissionId;
							$scope.patientDetails.patientName = $scope.checklistDetailsObj.patientName;
							$scope.patientDetails.genderCode = $scope.checklistDetailsObj.genderCode;
							$scope.patientDetails.age = $scope.checklistDetailsObj.age;
							$scope.patientDetails.uhIdNumber = $scope.checklistDetailsObj.uhidNumber;
							$scope.patientDetails.birthDate = $scope.checklistDetailsObj.birthDate;
							$scope.patientDetails.dateOfAdmission = $scope.checklistDetailsObj.doa;
							$scope.patientDetails.patientDischargeDate = $scope.checklistDetailsObj.pdd;
							$scope.patientDetails.bedNumber = $scope.checklistDetailsObj.bedNumber;
							$scope.patientDetails.doctorFullName = "Dr. "+$scope.checklistDetailsObj.doctorName;
							$scope.patientDetails.wardName = $scope.checklistDetailsObj.wardName;
							$scope.patientDetails.visitTypeId = $scope.checklistDetailsObj.visitTypeId;
							$scope.patientDetails.isFromOtCompletion = true;
							if($scope.patientDetails.visitTypeId!=1){
								var dateDoa = moment($scope.patientDetails.dateOfAdmission, 'DD/MM/YYYY');
								var datePdd = moment($scope.patientDetails.patientDischargeDate, 'DD/MM/YYYY');
								$scope.patientDetails.los = $rootScope.dateDiffrence(dateDoa,datePdd);
							}  
							
							$scope.checklistDetailsList = [] ;
							$scope.consentList = [];
							$scope.preOperativeList = [];
							$scope.intraOperativeList = [];
							$scope.postOperativeList = [];
							$scope.dischargeList = [];
							$scope.wardList = [];
							
						$scope.getOTChecklistDetails = function(){
							try {
								$scope.startLoader();
								$scope.checklistDetailsList = [] ;
								
								var URI = BASE_URL + ROOT_URL+"/api/procedure/procedureOrderChecklist/"+$scope.checklistDetailsObj.proReqId+"/"+$scope.userId ;
								console.log("URI", URI);
								GenericService
										.serviceAction("GET", URI, "")
										.then(
												function(response) {
													$scope.checklistDetailsList = response.data.listObject;
													$rootScope.stopLoader();
													console.log("$scope.checklistDetailsList",$scope.checklistDetailsList);
													
													if($scope.checklistDetailsList!=null && $scope.checklistDetailsList.length>0){
														angular.forEach($scope.checklistDetailsList,function(value,index){
															if(value.stageId == 1){
																$scope.consentList = value.otCheckList;
															}
															if(value.stageId == 2){
																$scope.preOperativeList = value.otCheckList;
															}
															if(value.stageId == 3){
																$scope.intraOperativeList = value.otCheckList;
															}
															if(value.stageId == 4){
																$scope.postOperativeList = value.otCheckList;
															}
															if(value.stageId == 5){
																$scope.dischargeList = value.otCheckList;
															}
															if(value.stageId == 6){
																$scope.wardList = value.otCheckList;
															}
														});
													}else{
														
														growl.warning("This action is not applicable for this user",
																{
																	title : 'warning!'
																});
													}
												});
								console.log("$scope.consentList",$scope.consentList);
								console.log("$scope.preOperativeList",$scope.preOperativeList);
								console.log("$scope.intraOperativeList",$scope.intraOperativeList);
								console.log("$scope.postOperativeList",$scope.postOperativeList);
								console.log("$scope.dischargeList",$scope.dischargeList);
								
							} catch (e) {
								console.log(e.message)
								$scope.stopLoader();
							}	
						}
						$scope.getOTChecklistDetails();
						
						$scope.currentIndex;
						$scope.getChecklistItemListById = function(info,index,flag){
							$scope.currentIndex = index;
							if(flag == "edit"){
							if(info.stageId == 1){
								$('#consentModal').modal('show');
								}
								if(info.stageId == 2){
									$('#preOperative').modal('show');
								}
								if(info.stageId == 3){
									$('#intraOperative').modal('show');
								}
								if(info.stageId == 4){
									$('#postOperative').modal('show');
								}
								if(info.stageId == 5){
									$('#discharge').modal('show');
								}
								if(info.stageId == 6){
									$('#wardz').modal('show');
								}
							}else{
								
								if(info.stageId == 1){
									$('#viewConsentModal').modal('show');
									}
									if(info.stageId == 2){
										$('#viewPreOperative').modal('show');
									}
									if(info.stageId == 3){
										$('#viewIntraOperative').modal('show');
									}
									if(info.stageId == 4){
										$('#viewPostOperative').modal('show');
									}
									if(info.stageId == 5){
										$('#viewDischarge').modal('show');
									}
									if(info.stageId == 6){
										$('#viewWard').modal('show');
									}
							}
							
							}
						
						$scope.finalList = [];
						$scope.saveOtChecklistDetails = function(checklistId,stageId,checklistStatusId,list){
							try {
								$scope.startLoader();
								$scope.finalList = [];
								var checkCnt =0 ;
								if(list.length>0){
									angular.forEach(list,function(value,index){
										if(value.checklistStatusId==2){
											checklistStatusId = 2;
										}else if(value.checklistStatusId==3){
											checklistStatusId = 3 ;
										}else{
											checklistStatusId = 1;
										}
										
										if(value.checkStatus &&  value.otChecklistDetailsId == null){
											if(value.checklistStatusId !=1 ){
											var newArr = {};
											newArr.otChecklistDetailsId = null;
											newArr.proReqId = $scope.checklistDetailsObj.proReqId;
											newArr.description = value.description;
											newArr.checklistId = checklistId ;
											newArr.checklistMasterId = value.checklistItemMasterId
											newArr.checklistStatusId = value.checklistStatusId;
											newArr.remark = value.remark;
											newArr.stageId = stageId;
											newArr.status = 'A';
											newArr.organizationId = value.organizationId;
											newArr.unitId = value.unitId;
											newArr.createdBy = $scope.userId ;
											newArr.createdDate = $scope.dt ;
											newArr.updatedBy = $scope.userId;
											newArr.updatedDate = $scope.dt
											$scope.finalList.push(newArr);
										}
									}else{
										checkCnt ++;
									}
									});
									
									if(list.length == checkCnt){
										$rootScope.stopLoader();
										growl.warning("Please select atleast one item ",
												{
													title : 'warning!'
												});
										return false;
									}
								}
								var URI = BASE_URL + ROOT_URL+"/api/otims/OTchecklist/saveOTChecklistDetails";
								console.log("URI", URI);
								GenericService
										.serviceAction("POST", URI, $scope.finalList)
										.then(
												function(response) {
													$rootScope.stopLoader();
													
													 if($scope.patientDetails.admissionId  != null){
                                                         var datdd ={
                                                                 admissionId : $scope.patientDetails.admissionId  ,
                                                                 otStatus:2
                                                         };
                                                         var URI = BASE_URL + ROOT_URL + "/adt/updateOtStatusAgainstAdmission";
                                                         GenericService.serviceAction("POST",URI,datdd).then(function(response){
                                                             if (response.data.status == 'success')
                                                             {
                                                                 $rootScope.stopLoader();
                                                             } 
                                                         });
                                                     }
													growl.success(response.data.message,
															{
																title : 'Success!'
															});
												//	$state.go('OTIMSdashboard');
													$scope.getOTChecklistDetails();
												});
								
							} catch (e) {
								console.log(e.message)
								$scope.stopLoader();
							}
							
						}
						
						$scope.overrideChecklistDetails = function(){
							
							try {
								$scope.startLoader();
								$scope.finalList = [];
								if($scope.checklistDetailsList.length>0){
									angular.forEach($scope.checklistDetailsList,function(value,index){
										if(value.otCheckList!=null && value.otCheckList.length>0){
											angular.forEach(value.otCheckList,function(val,idx){
												if(val.checklistItemList!=null && val.checklistItemList.length>0){
												 angular.forEach(val.checklistItemList,function(val1,idx1){
													 if( val1.checklistStatusId == 1 &&  val1.otChecklistDetailsId == null){
													 var newArr = {};
														newArr.otChecklistDetailsId = null;
														newArr.proReqId = $scope.checklistDetailsObj.proReqId;
														newArr.description = val1.description;
														newArr.checklistId = val.checklistId ;
														newArr.checklistMasterId = val1.checklistItemMasterId
														newArr.checklistStatusId = 3;
														newArr.remark = val1.remark;
														newArr.stageId = val.stageId;
														newArr.status = 'A';
														newArr.organizationId = val1.organizationId;
														newArr.unitId = val1.unitId;
														newArr.createdBy = $scope.userId ;
														newArr.createdDate = $scope.dt ;
														newArr.updatedBy = $scope.userId;
														newArr.updatedDate = $scope.dt
														$scope.finalList.push(newArr);
													 }
													});
												}
												 })	;
												}
										
									});
								}
								var URI = BASE_URL + ROOT_URL+"/api/otims/OTchecklist/saveOTChecklistDetails";
								console.log("URI", URI);
								GenericService
										.serviceAction("POST", URI, $scope.finalList)
										.then(
												function(response) {
													$rootScope.stopLoader();

													growl.success(response.data.message,
															{
																title : 'Success!'
															});
													$scope.getOTChecklistDetails();
												//	$state.go('OTIMSdashboard');
												
												});
								
							} catch (e) {
								console.log(e.message)
								$scope.stopLoader();
							}
							
						}
						
						
						
						
                        $scope.disableFourTab = false;
                        $scope.disableThreeTab = false;
                        $scope.disableTwoTab = false;
                        $scope.disableFiveTab = false;
                        $scope.setFirstTabLevel = function(tabNumber){
                        	 $scope.disableFourTab = false;
                             $scope.disableThreeTab = false;
                             $scope.disableTwoTab = false;
                             $scope.disableFiveTab = false;
                             
                             if(tabNumber == 1){
                            	 $scope.currentTab = tabNumber;
                             }
                        	if(tabNumber == 2){
                        		 if($scope.consentList.length>0){
                                     angular.forEach($scope.consentList,function(value,index){
                                         if(value.checklistStatusId==1){
                                             $scope.disableTwoTab = true;
                                         }
                                     });
                                 }
                             	if($scope.disableTwoTab){
                             		growl.warning("Please complete Previous Stage !",
     										{
     											title : 'warning!'
     										});
                             		return false;
                             	}else{
                             		$scope.currentTab = tabNumber;
                             	}
                        	
                        	}
                          	if(tabNumber == 6){
                       		 if($scope.consentList.length>0){
                                    angular.forEach($scope.consentList,function(value,index){
                                        if(value.checklistStatusId==1){
                                            $scope.disableTwoTab = true;
                                        }
                                    });
                                }
                            	if($scope.disableTwoTab){
                            		growl.warning("Please complete Previous Stage !",
    										{
    											title : 'warning!'
    										});
                            		return false;
                            	}else{
                            		$scope.currentTab = tabNumber;
                            	}
                       	
                       	}
                        	if(tabNumber == 3){
                        		
                        		
                        		 if($scope.consentList.length>0){
                                     angular.forEach($scope.consentList,function(value,index){
                                         if(value.checklistStatusId==1){
                                             $scope.disableTwoTab = true;
                                         }
                                     });
                                 }
                             	if($scope.disableTwoTab){
                             		growl.warning("Please complete Previous Stage !",
     										{
     											title : 'warning!'
     										});
                             		return false;
                             	}
                        		 if($scope.preOperativeList.length>0){
                                     angular.forEach($scope.preOperativeList,function(value,index){
                                         if(value.checklistStatusId==1){
                                             $scope.disableThreeTab = true;
                                         }
                                     });
                        		 }
                                     if($scope.disableThreeTab){
                                 		growl.warning("Please complete Previous Stages !",
         										{
         											title : 'warning!'
         										});
                                 		return false;
                                 	}else{
                                 		$scope.currentTab = tabNumber;
                                 	}
                       	
                       	}
                        if(tabNumber == 4){
                        	

                   		 if($scope.consentList.length>0){
                                angular.forEach($scope.consentList,function(value,index){
                                    if(value.checklistStatusId==1){
                                        $scope.disableTwoTab = true;
                                    }
                                });
                            }
                        	if($scope.disableTwoTab){
                        		growl.warning("Please complete Previous Stage !",
										{
											title : 'warning!'
										});
                        		return false;
                        	}
                   		
                   		 if($scope.preOperativeList.length>0){
                                angular.forEach($scope.preOperativeList,function(value,index){
                                    if(value.checklistStatusId==1){
                                        $scope.disableThreeTab = true;
                                    }
                                });
                   		 }
                                if($scope.disableThreeTab){
                            		growl.warning("Please complete Previous Stages !",
    										{
    											title : 'warning!'
    										});
                            		return false;
                            	}
                        		if($scope.intraOperativeList.length>0){
                                    angular.forEach($scope.intraOperativeList,function(value,index){
                                        if(value.checklistStatusId==1){
                                            $scope.disableFourTab = true;
                                        }
                                    });
                        		 }
                                    if($scope.disableFourTab){
                                 		growl.warning("Please complete Previous Stages !",
         										{
         											title : 'warning!'
         										});
                                 		return false;
                                 	}else{
                                 		$scope.currentTab = tabNumber;
                                 	}
                              
                      	}	
                        
                        if(tabNumber == 5){
                        	

                      		 if($scope.consentList.length>0){
                                   angular.forEach($scope.consentList,function(value,index){
                                       if(value.checklistStatusId==1){
                                           $scope.disableTwoTab = true;
                                       }
                                   });
                               }
                           	if($scope.disableTwoTab){
                           		growl.warning("Please complete Previous Stage !",
   										{
   											title : 'warning!'
   										});
                           		return false;
                           	}
                      		 if($scope.preOperativeList.length>0){
                                   angular.forEach($scope.preOperativeList,function(value,index){
                                       if(value.checklistStatusId==1){
                                           $scope.disableThreeTab = true;
                                       }
                                   });
                      		 }
                                   if($scope.disableThreeTab){
                               		growl.warning("Please complete Previous Stages !",
       										{
       											title : 'warning!'
       										});
                               		return false;
                               	}
                           		if($scope.intraOperativeList.length>0){
                                       angular.forEach($scope.intraOperativeList,function(value,index){
                                           if(value.checklistStatusId==1){
                                               $scope.disableFourTab = true;
                                           }
                                       });
                           		 }
                                       if($scope.disableFourTab){
                                    		growl.warning("Please complete Previous Stages !",
            										{
            											title : 'warning!'
            										});
                                    		return false;
                                    	} 
                                       
                        	if($scope.postOperativeList.length>0){
                                angular.forEach($scope.postOperativeList,function(value,index){
                                    if(value.checklistStatusId==1){
                                        $scope.disableFiveTab = true;
                                    }
                                });
                      		}
                                if($scope.disableFiveTab){
                              		growl.warning("Please complete Previous Stages !",
      										{
      											title : 'warning!'
      										});
                              		return false;
                              	}else{
                              		$scope.currentTab = tabNumber;
                             	}
                  	}	
                    }
                        
                        $scope.setFirstTabLevel(1);
                        if($scope.mapOfBed == 2){
							$scope.currentTab = 6;
							$scope.isSetFirstTabLevel(6);
							$scope.setFirstTabLevel(6);
						}
                        
                        $scope.goToBackPage = function(){
                        	if($stateParams.checklistDetailsObj.renderedFromSurgery!=undefined && $stateParams.checklistDetailsObj.renderedFromSurgery==1){
                        		$state.go('OTIMSdashboard');
                        	}else{
                        		$state.go('OTchecklist');
                        	}
                        }

            $scope.goToNursingScore = function(){
                $scope.checklistDetailsObj.dob = $scope.checklistDetailsObj.birthDate;
                if($scope.checklistDetailsObj.visitTypeId > 1){
                    $state.go('nursingScale',
                    {
                        obj : $scope.checklistDetailsObj,
                    });
                }
            }

						
			}]);
		