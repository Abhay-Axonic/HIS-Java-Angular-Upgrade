/**
 * 
 * Sneha Dalave
 */
angular.module('myApp')
		.controller("editReportedIncidentController",
		['$scope','$rootScope','$state','$cookies','GenericService','$timeout','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','commonDetailService','GenericServiceParamHeader','reportService',
		function($scope,$rootScope,$state, $cookies, GenericService,$timeout,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,commonDetailService,GenericServiceParamHeader,reportService){
			$rootScope.loginpage = true;
			$scope.LISDynamicLabel = "Antibiotic Addition";
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
		
			$scope.storeList = [];
		
		/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.usersList=[];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
		
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
			$scope.userName =cookieObject.userName;
		
		$scope.selectedRoleList=[];	
		$scope.selectedUserList=[];	
		$scope.selectedTemplateList=[];
		$scope.selectedTemplateList1=[];
		$scope.userTemplateList = [];
		
		$scope.GetIncidentReportingData = function(info){
			
			$scope.incidentReportingMapperId = info.incidentReportingMapperId;
			if($scope.incidentReportingMapperId == 0){
				$scope.incidentReportingMapperId =null;
			}
			if($scope.incidentReportingMapperId != null){
				try{
					
					$rootScope.startLoader();
					
					var URI = BASE_URL + ROOT_URL +"/api/unit/getIncidentReportingMapperById" ;
					var obj = {
							    "incidentReportingMapperId": $scope.incidentReportingMapperId,
								"organizationId" : $scope.organizationId,
								"unitId":$scope.unitId,
						    }
					BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						console.log(response);
						if(response.data.status == "success"){
							$scope.getEditedData =[];
							$scope.getEditedData = response.data.listObject;
							console.log("getSavedData",$scope.getEditedData)
							
							angular.forEach($scope.getActiveIncidentReportList,function(value1,key1){
									if($scope.getEditedData[0].incidentReportId == value1.incidentReportId){
										$scope.getActiveRoleList=[];
										$scope.getEditedData[0].incidentReportDesc=value1.incidentReportDesc;
										angular.forEach(value1.incidentReportRoleMapperList,function(value,index){
											var neeArr = {};
											neeArr.id = value.roleId;
											neeArr.label = value.roleName;
											$scope.getActiveRoleList.push(neeArr);
										
										});
										$scope.incidentReportTemplateMapperList=[];
										$scope.incidentReportTemplateMapperList=angular.copy(value1.incidentReportTemplateMapperList);
									}
								
								});
							
							$scope.obj2={
									'incidentReportId' :$scope.getEditedData[0].incidentReportId,
									'incidentReportDesc' :$scope.getEditedData[0].incidentReportDesc,
									
							}
							$scope.incidentReportingDate= $scope.getEditedData[0].incidentReportingDate;
							$scope.incidentReportingDateTime=$scope.getEditedData[0].incidentReportingTime,
							$scope.specialityId=$scope.getEditedData[0].specialityId;
							$scope.specialityName=$scope.getEditedData[0].specialityName;
							$scope.incidentCategoryId=$scope.getEditedData[0].incidentCategoryId;
							$scope.incidentCategoryDesc=$scope.getEditedData[0].incidentCategoryDesc;
							$scope.incidentReportDesc=$scope.getEditedData[0].incidentReportDesc;
							$scope.incidentReportId=$scope.getEditedData[0].incidentReportId;
							$scope.reportingTemplateId=$scope.getEditedData[0].reportingTemplateId;
							$scope.incidentTypeId=$scope.getEditedData[0].incidentTypeId;
							$scope.incidentRemark=$scope.getEditedData[0].incidentRemark;
							$scope.roleId=$scope.getEditedData[0].roleId;
							$scope.incidentUserId=$scope.getEditedData[0].userId;
							$scope.patientId=$scope.getEditedData[0].patientId;
							$scope.reportingTemplateObj=$scope.getEditedData[0].reportingTemplateObj;
							$scope.templateFormatId=$scope.getEditedData[0].templateFormatId;
							$scope.patientDetails.uhIdNumber=($scope.getEditedData[0].uhidNumber);
							$scope.patientDetails.patientName=($scope.getEditedData[0].patientName);
							$scope.parentUserId=($scope.getEditedData[0].createdBy);
							$scope.patientDetails.conversationId = ($scope.getEditedData[0].conversationId);
							$scope.patientDetails.admissionId = ($scope.getEditedData[0].admissionId);
							$scope.patientDetails.encounterId = ($scope.getEditedData[0].encounterId);
							$scope.patientDetails.patientId = ($scope.getEditedData[0].patientId);
							
							$scope.patientDetails.visitTypeId = 1;
							
							if($scope.templateFormatId==1)
							{
							    document.getElementById("htmlContent").innerHTML = $scope.reportingTemplateObj;
							}
							else if($scope.templateFormatId==2 || $scope.templateFormatId==3)
								{
								 $scope.value = $scope.reportingTemplateObj;
								}
							if(info.reporteIncMpprId != null){
							var URI = BASE_URL + ROOT_URL +"/api/unit/getReportedIncidentMapperById" ;
							var obj = {
									    "reportedIncMpprId": info.reporteIncMpprId,
//									    "patientId": info.patientId,
										"organizationId" : $scope.organizationId,
										"unitId":$scope.unitId,
								    }
							BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
								console.log(response);
								if(response.data.status == "success"){
									$scope.getReportedSavedData=[];
									$scope.getReportedUserSavedData=[];
									$scope.getReportedSavedData.push(response.data.listObject[0]);
									$scope.selectedRoleList =[];
									$scope.selectedUserList =[];
									$scope.selectedRoleListFinal =[];
									$scope.parentUserId = $scope.getReportedSavedData[0].parentUserId;
									angular.forEach($scope.getReportedSavedData[0].reportedIncidentRoleMapperList , function(value,key){
										$scope.repIncRoleMpprId = value.repIncRoleMpprId;
										var newArr = {};
										var newArr1 = {};
										newArr.id  = value.roleId;
										newArr1.roleId  = value.roleId;
										$scope.selectedRoleList.push(newArr);
										newArr1.repIncRoleMpprId = $scope.repIncRoleMpprId ;
										$scope.selectedRoleListFinal.push(newArr1);
									});
									$scope.getReportedUserSavedData = $scope.getReportedSavedData[0].reportedIncidentUserMapperList;
									$scope.reportedIncMpprId = $scope.getReportedSavedData[0].reportedIncMpprId;
									
									angular.forEach($scope.getReportedUserSavedData , function(value,key){
										$scope.repIncUserMpprId = value.repIncUserMpprId;
										var newArr = {};
										newArr.repIncUserMpprId  = value.repIncUserMpprId;
										newArr.parentRepIncUserId  = value.parentRepIncUserId;
										newArr.id  = value.userId;
										newArr.reportedTemplateObj  = value.reportedTemplateObj;
										value.conversationId = info.conversationId;
										if(value.userId == $scope.userId){
											value.editFlag=false;
											newArr.editFlag=false;
										}else{
											value.editFlag=true;
											newArr.editFlag=true;
										}
										$scope.selectedUserList.push(newArr);
									});
									$scope.templateId = $scope.getReportedSavedData[0].reportedTemplateId;
									$scope.reportedTemplateId = $scope.getReportedSavedData[0].reportedTemplateId;
									$scope.resolutionDetailsTypeId = $scope.getReportedSavedData[0].resolutionDetailsTypeId;
									
                                    if( info.isSubmit == 'Y'){
                                        var saveButton = document.getElementById('reportedIncidentSaveButton')
                                        var buttonInside = saveButton.querySelector('input');  
                                        if (buttonInside) {
                                            buttonInside.disabled = true;
                                        }
                                    }
									
									if($scope.resolutionDetailsTypeId == 2){
										angular.forEach($scope.getReportedUserSavedData , function(value,key){
											value.editFlag=true;
										});
									}
									$scope.getRolewiseUsers();
									$rootScope.stopLoader();
									console.log("getReportedSavedData",$scope.getReportedUserSavedData)
								}
							});
							}
						}
					});
				}catch (e) {
					console.log("Exception",e.message);
				}	
			}
		
		}
		
		$scope.initCommon = function()
					{
			            $scope.resolutionDetailsTypeId = 1;
						$scope.saveBtnFlag = true; 
						$scope.updateBtnFlag = false;
						$scope.reportedIncMpprId = null;
						$scope.repIncRoleMpprId = null;
						$scope.repIncUserMpprId = null;
						/*$scope.getReportedUserSavedData=[{
							 "reportedTemplateObj":"",
							 "repIncUserMpprId":"",
							 "userName":$scope.userName,
							 "userId":$scope.userId,
							 "repIncUserMpprId":"",
							 "editFlag":true,
							
						}];*/
						$scope.common =
						{
							 "userStoreMapperId":"",
							 "code":"",
							 "description":"",
						     "isActive":"A",
							 "createdBy":$scope.userId,
							 "createdDate":"",
							 "updatedBy":$scope.userId,
							 "updatedDate":"",
						     "organisationId":$scope.organisationId,

						}
						
						$scope.getActiveIncidentReport = function(){
							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/api/unit/getActiveIncidentReportMasterList";
							offset =  0;
							noOfRecordsPerPage = 10;
							var data1 = {
					  			      organizationId:$scope.organisationId,
					  			      unitId:$scope.unitId,
					  			      offset:offset,
					  			      noOfRecordsPerPage:noOfRecordsPerPage
					  		};
							GenericService.serviceAction("POST",URI,data1).then(function(response){
								if(response.data.status == "success"){
								$rootScope.stopLoader();					
								$scope.getActiveIncidentReportList = [];
								angular.forEach(response.data.listObject , function(value,key){
									if(value.status == 'A'){
										$scope.getActiveIncidentReportList.push(value);
									}
								});
								$scope.GetIncidentReportingData(commonDetailService.getDataId());
								}
							});
							}
					$scope.getActiveIncidentReport();
					
					
						
					}
		$scope.initCommon();
		
		
	
		/**********from date******************/
		
		$scope.openDatePickerfromDate = function() {
			$scope.datepickerfromDate.opened = true;
		};

		$scope.datepickerfromDate = {
			opened : false
		};
		
		$scope.dateOptionfromDate = {
				formatYear : 'yyyy',
				showWeeks : false,
				maxDate : new Date(),
			};
		
        /**********to date******************/
		
		$scope.openDatePickertoDate = function() {
			$scope.datepickertoDate.opened = true;
		};

		$scope.datepickertoDate = {
			opened : false
		};
		
		$scope.dateOptiontoDate = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date(),
			};
			
			$scope.clicked = function(type) {
				$scope.selectedRoleListFinal=[];
				if (type == 'selectedRoleList') {
					if ($scope.selectedRoleList.length > 0) {
						angular.forEach($scope.selectedRoleList,function(value,key){
							var newArr = {};
							newArr.roleId=value.id;
							newArr.repIncRoleMpprId = $scope.repIncRoleMpprId ;
							$scope.selectedRoleListFinal.push(newArr);
						});
						$scope.getRolewiseUsers()
					}
				}
			}
			
			$scope.getReportedUserSavedData=[];
			$scope.clickedUser = function(type) {
				$scope.selectedUserListFinal=[];
				if (type == 'selectedUserList') {
					if ($scope.selectedUserList.length > 0) {
						$scope.getReportedUserSavedData1=[];
						angular.forEach($scope.selectedUserList,function(value,key){
							var newArr = {};
							newArr.userId=value.id;
							newArr.repIncUserMpprId=value.repIncUserMpprId;
							newArr.reportedTemplateObj=value.reportedTemplateObj;
							$scope.selectedUserListFinal.push(newArr);
							
							angular.forEach($scope.getRoleWiseUsersList,function(value2,key2){
								if(value2.id == value.id){
									var newee={};
									if(value.reportedTemplateObj !=undefined){
										newee.reportedTemplateObj= value.reportedTemplateObj;
									}else{
										newee.reportedTemplateObj='';
									}
									
									newee.repIncUserMpprId='';
									newee.userName=value2.label;
									newee.userId=value2.id;
									
									if(value.id == $scope.userId){
										newee.editFlag=false;
									}else{
										newee.editFlag=true;
									}
									$scope.getReportedUserSavedData1.push(newee);
								}
							});
							
						});
						
						$scope.getReportedUserSavedData=angular.copy($scope.getReportedUserSavedData1);
					}
				}
			}
			$scope.reportedTemplateObj ="";
			
			$scope.addTemplate = function(info,index) {
				if(info.templateId){
					$scope.reportedTemplateId = info.templateId;
					angular.forEach($scope.incidentReportTemplateMapperList , function(value,key){
						if(value.templateId == info.templateId){
							$scope.template = {};
							$scope.template = value;
						}
					});
					$scope.reportedTemplateObj ="";
					$scope.templateFormatId =$scope.template.templateFormatId;
					if($scope.template.templateFormatId==1)
						{
					     	$scope.obj = {};
							$scope.template.templateObject = JSON.parse($scope.template.templateObject);
							$scope.obj = $scope.template.templateObject.obj;
							var replacedHtml = $scope.template.templateObject.html.replace(/ng-model="/g,'ng-model="obj.');
							document.getElementById("htmlContent").innerHTML = replacedHtml;
						}
					else if($scope.template.templateFormatId==2)
						{
						angular.forEach($scope.getReportedUserSavedData,function(value,key){
							if(value.userId == $scope.userId){
								value.reportedTemplateId = $scope.templateId;
								value.reportedTemplateObj = $scope.template.templateObject;
							}
						});
						}	 else if($scope.template.templateFormatId==3){
							   $rootScope.startLoaderPopup();
							    var templateTransaction = {};
								templateTransaction.templateId = $scope.template.templateId;
								templateTransaction.title = $scope.template.title;
								templateTransaction.specialityId = $scope.patientDetails.specialityId;
								templateTransaction.subSpecialityId = $scope.patientDetails.subSpecialityId;
								templateTransaction.templateTypeId = $scope.template.templateTypeId;
								templateTransaction.encounterId=$scope.patientDetails.encounterId != null ? $scope.patientDetails.encounterId :0;
								templateTransaction.admissionId = $scope.patientDetails.admissionId != null ? $scope.patientDetails.admissionId :0;
								templateTransaction.doctorId = $scope.patientDetails.doctorId != null? $scope.patientDetails.doctorId :0;
								templateTransaction.patientId = $scope.patientDetails.patientId != null ? $scope.patientDetails.patientId :0;
								templateTransaction.diagnosisId = 0;
								templateTransaction.mIcpcSystemId = 0;
								templateTransaction.createdBy =  info.userId;
								templateTransaction.unitId = $scope.unitId;
								templateTransaction.updatedBy =  info.userId;
								templateTransaction.orgId = $scope.organizationId;
                                templateTransaction.patientName = $scope.patientDetails.patientName !== undefined && $scope.patientDetails.patientName !== null ? $scope.patientDetails.patientName : "";
                                templateTransaction.doctorName = $scope.patientDetails.doctorName !== undefined && $scope.patientDetails.doctorName !== null ? $scope.patientDetails.doctorName : "";
                                templateTransaction.uhidNumber = $scope.patientDetails.uhidnumber !== undefined && $scope.patientDetails.uhidnumber !== null ? $scope.patientDetails.uhidnumber : $scope.patientDetails.uhidNumber;
                                templateTransaction.dob = $scope.patientDetails.birthDate !== undefined && $scope.patientDetails.birthDate !== null ? $scope.patientDetails.birthDate : "";
                                templateTransaction.ageString = $scope.patientDetails.age !== undefined && $scope.patientDetails.age !== null ? $scope.patientDetails.age : $scope.patientDetails.ageYear;
                                templateTransaction.doa = $scope.patientDetails.createdDate !== undefined && $scope.patientDetails.createdDate !== null ? $scope.patientDetails.createdDate : "";
                                templateTransaction.pdd = $scope.patientDetails.datepickerPdd !== undefined && $scope.patientDetails.datepickerPdd !== null ? $scope.patientDetails.datepickerPdd : "";
                                templateTransaction.admissionNumber = $scope.patientDetails.admissionNumber !== undefined && $scope.patientDetails.admissionNumber !== null ? $scope.patientDetails.admissionNumber : "";
                                templateTransaction.gender = $scope.patientDetails.genderCode !== undefined && $scope.patientDetails.genderCode !== null ? $scope.patientDetails.genderCode : "";
							
								
							  var URI = BASE_URL + ROOT_URL + NEW_TEMPLATE_SAVE;
								BillingGenericService.serviceAction(METHOD_POST, URI,templateTransaction,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											if (response.data.status == "success"){
												window.open(response.data.object+"&formType=digital&patientName="+$scope.patientDetails.patientName+
                                                     "&doctorName="+$scope.patientDetails.doctorName+
                                                     "&uhid="+$scope.patientDetails.uhidnumber+
                                                     "&dob="+$scope.patientDetails.birthDate+
                                                     "&age="+$scope.patientDetails.age+
                                                     "&gender="+$scope.patientDetails.genderCode+
                                                     "&admissionNo="+$scope.patientDetails.admissionNumber+
                                                     "&admissionDate="+$scope.patientDetails.createdDate+
                                                     "&dischargeDate="+$scope.patientDetails.datepickerPdd,'_blank');
												//$scope.getTemplateListForList();
												 $rootScope.stopLoaderPopup();
												 
												 $scope.getReportedUserSavedData[index].conversationId = response.data.resObject.conversationId;
												console.log("conversationId",$scope.conversationId);
											}
											else if(response.data.status == "error")
												{
												  $('#addtemplateModal').modal('hide');
												  $rootScope.stopLoaderPopup();
												}
											
										});	
								 }
				}
			}
			
			//Role wise user
			$scope.getRoleWiseUsersList = [];
			$scope.getRolewiseUsers = function(){
				$scope.getRoleWiseUsersList1 = [];
				$scope.getRoleWiseUsersList = [];
				$scope.roleIdList = [];
				angular.forEach($scope.selectedRoleList,function(value,key){
					$scope.roleIdList.push(value.id);
				});
					var URI = BASE_URL + ROOT_URL + "/api/unit/getRoleWiseUserList";
					offset =  0;
					noOfRecordsPerPage = 10;
					var data1 = {
			  			      organizationId:$scope.organisationId,
			  			      unitId:$scope.unitId,
			  			      roleIdList:$scope.roleIdList
			  			      
			  		};
					GenericService.serviceAction("POST",URI,data1).then(function(response){
						if(response.data.status == "success"){
						$rootScope.stopLoader();
						$scope.getRoleWiseUsersList1=response.data.listObject;
						console.log("$scope.getRoleWiseUsersList", $scope.getRoleWiseUsersList);
						
						angular.forEach($scope.getRoleWiseUsersList1,function(value,key){
							var newArr = {};
							newArr.id = value.userId;
							newArr.label = value.userName;
							$scope.getRoleWiseUsersList.push(newArr);
							
						});
						
						}
					});
//				});
				
				
			}
			
			$scope.set = function(tabValue) {
				$scope.resolutionDetailsTypeId = tabValue;
			}
			
			
			$scope.saveReportedIncident = function(){
				$scope.$broadcast('show-errors-check-validity');
					try {
						$rootScope.startLoader();
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						$scope.selectedUserListFinal=[];
						angular.forEach($scope.getReportedUserSavedData,function(value,key){
										var newArr = {};
										newArr.userId=value.userId;
										newArr.repIncUserMpprId=value.repIncUserMpprId;
										newArr.reportedTemplateObj=value.reportedTemplateObj;
										newArr.reportedTemplateId=value.templateId;
										newArr.conversationId=value.conversationId;
										$scope.selectedUserListFinal.push(newArr);
						});
						
						var data =
						{
								
						     "reportedIncMpprId":$scope.reportedIncMpprId,	
							"incidentReportId":$scope.incidentReportId,
							"parentUserId":$scope.parentUserId,
							"incidentReportingMapperId":$scope.incidentReportingMapperId,
							"resolutionDetailsTypeId":$scope.resolutionDetailsTypeId,
						     "reportedTemplateId":$scope.reportedTemplateId,
						     "reportedTemplateObj":$scope.reportedTemplateObj,
						     "isSubmit":'N',
						     "organizationId":$scope.organisationId,
						     "unitId":$scope.unitId,
						     "status":'A',
							 "createdBy":$scope.createdBy,
							 "createdDate":$scope.dt,
							 "updatedBy":$scope.updatedBy,
							 "updatedDate":$scope.dt,
						     "reportedIncidentRoleMapperList":$scope.selectedRoleListFinal,
						     "reportedIncidentUserMapperList":$scope.selectedUserListFinal,
															     
						 }
						console.log("save data",data);
						if($scope.selectedRoleList.length > 0 && $scope.selectedUserList.length > 0){
						var URI = BASE_URL + ROOT_URL + "/api/unit/saveReportedIncidentMapper";
						GenericService.serviceAction("POST", URI, data).then(function(response) 
						{	
							if (response.data.status == 'success')
							{
								
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								$scope.$broadcast('show-errors-reset');
								$rootScope.stopLoader();
								 $state.go('ReportedIncident');
								
								
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
							
						});
					}else{
						growl.warning("please select role and user",
								{
									title : 'warning!'
								});
						$rootScope.stopLoader();
					}
					} catch (e) {
						console.log("Exception",e.message);
					}
					
			}

		
			
			$scope.submitReportedIncident = function(){
				$scope.$broadcast('show-errors-check-validity');
				try {
					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					$scope.selectedUserListFinal=[];
					$scope.isSubmit ='N';
					$scope.parentClose = false;
					
					if($scope.parentClose == false){
						angular.forEach($scope.getReportedSavedData,function(value,key){
							if(value.parentUserId == $scope.userId){
								$scope.parentClose = true;
//								angular.forEach(value.reportedIncidentUserMapperList,function(value1,key1){
//									var newArr = {};
//								    newArr.isSubmit ='Y';
//									newArr.repIncUserMpprId=value1.repIncUserMpprId;
//									$scope.selectedUserListFinal.push(newArr);
//								});
							}
						});
					}
					
				if($scope.parentClose == true){
						
						angular.forEach($scope.getReportedUserSavedData,function(value,key){
										var newArr = {};
										if(value.userId == $scope.userId){
//											 if($scope.resolutionDetailsTypeId == '1'){
//												   newArr.resolutionDetailsTypeId = 1;
//												   newArr.isSubmit ='N';
//											   }else{
												   newArr.resolutionDetailsTypeId = 2;
												   newArr.isSubmit ='Y';
//											   }
										 } else{
											       newArr.resolutionDetailsTypeId = 1;
											       newArr.isSubmit ='N';
										 }
										newArr.repIncUserMpprId=value.repIncUserMpprId;
										$scope.selectedUserListFinal.push(newArr);
						});
					}
				
					
					
					 if($scope.parentClose == true){
						 $scope.resolutionDetailsTypeId = 2;
						 $scope.isSubmit ='Y';
					 }else{
						 $scope.resolutionDetailsTypeId= 1;
						 $scope.isSubmit ='N';
					 }
					
						var data =
						{
						     "reportedIncMpprId":$scope.reportedIncMpprId,	
						     "isSubmit":$scope.isSubmit, 
						     "resolutionDetailsTypeId":$scope.resolutionDetailsTypeId,
						     "reportedIncidentUserMapperList":$scope.selectedUserListFinal,
															     
						 }	
					
					
					console.log("save data",data);
				    if($scope.selectedRoleList.length == 0 && $scope.selectedUserList.length == 0){
				    	growl.error("Please select role and user ",
								{
									title : 'Error!'
								});
				    	$rootScope.stopLoader();
				    	return false;
				    }
				   /* if($scope.resolutionDetailsTypeId == 1){
				    	growl.error("Please Select Resolution Details Type close the Submit ",
								{
									title : 'Error!'
								});
				    	$rootScope.stopLoader();
				    	return false;
				    }*/
					var URI = BASE_URL + ROOT_URL + "/api/unit/submitReportedIncidentMapper";
					GenericService.serviceAction("POST", URI, data).then(function(response) 
					{	
						if (response.data.status == 'success')
						{
							
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$rootScope.stopLoader();
							 $state.go('ReportedIncident');
							
							
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
						
					});
				} catch (e) {
					console.log("Exception",e.message);
				}
				
		}
			
			$scope.getTemplateListData = function(info) 
	    	{
	    		$rootScope.startLoader();
	    		if(info.conversationId != null ){
	        	var data={
	        		  orgId : $scope.organizationId,
	        		  unitId : $scope.unitId,
	        		  doctorId : $scope.patientDetails.doctorId,
	        		  specialityId : $scope.patientDetails.specialityId,
	        		  templateTypeId : 22,
	        		  patientId : $scope.patientId,
	        		  visitTypeId: 2,
	        		  appointmentId:$scope.patientDetails.appointmentId,
	        		 encounterId:$scope.patientDetails.encounterId,
				     admissionId : $scope.patientDetails.admissionId,
				     conversationId : info.conversationId
	        		}
	        	
	        	console.log('data',JSON.stringify(data));
	    		var URI = BASE_URL + ROOT_URL + "/global/petient/template";
	    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
	    				{
	    	
	    			   if(response.data.status=='success')
	    			      {
	    				   	$scope.templateTransactionList = response.data.listObject;
	    				   	
	    				   	angular.forEach($scope.templateTransactionList ,function (value,key){
	    				   		value.printReport= false;	
	    				   	});
	    				   	$('#viewtemplateModal').modal('show');
	    				   	$rootScope.stopLoader();
	    			      }
	    			   else if(response.data.status=='error'){
	    				   $rootScope.stopLoader();
	    			   }
	    		});
	    	}
	    	}
			
		 	$scope.getTemplateList = function(templateTypeId) 
	    	{
	       		$scope.templateTypeId = templateTypeId;
	    		$rootScope.startLoader();
	        	var data={
	        		  orgId : $scope.organizationId,
	        		  unitId : $scope.unitId,
	        		  doctorId : $scope.patientDetails.doctorId,
	        		  specialityId : $scope.patientDetails.specialityId,
	        		  templateTypeId : templateTypeId,
	        		  patientId : $scope.patientId,
	        		  visitTypeId: 2,
	        		  appointmentId:$scope.patientDetails.appointmentId,
	        		 encounterId:$scope.patientDetails.encounterId,
				     admissionId : $scope.patientDetails.admissionId,
				     conversationId : $scope.patientDetails.conversationId
	        		}
	        	
	        	console.log('data',JSON.stringify(data));
	    		var URI = BASE_URL + ROOT_URL + "/global/petient/template";
	    		BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
	    				{
	    	
	    			   if(response.data.status=='success')
	    			      {
	    				   	$scope.templateTransactionList = response.data.listObject;
	    				   	
	    				   	angular.forEach($scope.templateTransactionList ,function (value,key){
	    				   		value.printReport= false;	
	    				   	});
	    				   	$('#viewtemplateModal').modal('show');
	    				   	$rootScope.stopLoader();
	    			      }
	    			   else if(response.data.status=='error'){
	    				   $rootScope.stopLoader();
	    			   }
	    		});
	    	}
	       	
		 	
           $scope.viewTemplate = function(template) {
        	   $scope.template={};
				if(template.templateFormatId != 4 ){
				$scope.editorData=template.templateObject;
				$scope.template.templateFormatId=template.templateFormatId;
				$scope.saveTrasactionFlag = false;
				$scope.updateTrasactionFlag = false;
				$scope.obj = {};
				$('#viewtemplateModal').modal('hide');
				
				if(template.templateFormatId != null && (template.templateFormatId == 1 || template.templateFormatId == 2)){
					$('#addviewTemplateModal11').modal('show');
					document.getElementById("htmlContent1").innerHTML = JSON.parse(template.templateObject).html;
					$scope.obj = JSON.parse(template.templateObject).obj;
					var element = angular.element(document.getElementById("htmlContent1"));
					$compile(element)($scope);
				}else{
					$('#addviewTemplateModal').modal('show');
                    var lines = template.templateObject.split('\n');
                    var formattedContent = lines.map(line => `<div class="styled-html">${line}</div>`).join('');
                    document.getElementById("htmlContent3").innerHTML =formattedContent;
				}
				}else{
					$scope.template = {};
					$scope.template = template;
					$scope.templateTypeId = template.templateTypeId;
					$rootScope.startLoader();
					$scope.savedList=[];
					$scope.savedEditedList = [];
					var URI = BASE_URL + ROOT_URL+ "/api/global/unit/template/tabular/mapper/getPatientTabularTemplateDataMapperById/"
					+parseInt(template.tabularTemplateId) +"/"+$scope.organizationId+"/"+$scope.unitId +"/"+$stateParams.patientDetailsObj.patientId+"/"+template.templateTypeId;
					GenericService
					.serviceAction(METHOD_GET, URI)
					.then(
							function(response) {

								if (response.data.status == 'success')
								{
									$rootScope.stopLoader();
									$scope.rowList = [];
									$scope.columnList = [];
									$scope.columnHeaderList = [];
									$scope.savedEditedList = [];
									$scope.savedEditedList = response.data.listObject;
									$scope.comObj = {};
									$scope.savedList = response.data.listObject;
									angular.forEach($scope.savedEditedList,function(value1,key1){
										value1.organizationId=$scope.organizationId;
										value1.unitId=$scope.unitId;
										value1.createdBy=$scope.userId;
										value1.updatedBy=$scope.userId;
										value1.status='A';
										value1.createdDate= moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
										value1.updatedDate= moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
									});
									console.log("$scope.columnList",$scope.columnList);
									$scope.comObj.numberOfRow = response.data.listObject[0].numberOfRow;
									$scope.comObj.numberOfCoulmn = response.data.listObject[0].numberOfCoulmn;
									$scope.tableName = response.data.listObject[0].tableName;
									
										for(var i = 0; i < $scope.comObj.numberOfRow ;i++){
											var newArr = {};
											newArr.rowId = i;
											newArr.coulmnId = i;
											newArr.rowName = '';
											newArr.inputbox = false;
											newArr.tabularTemplateId = $scope.tabularTemplateId;
											$scope.rowList.push(newArr);
									     }
										for(var i = 0; i < $scope.comObj.numberOfCoulmn ;i++){
											var newArr = {};
											newArr.coulmnId = i;
											newArr.rowId = i;
											newArr.columnName = '';
											newArr.inputValue = '';
											newArr.inputbox = false;
											newArr.tabularTemplateId = $scope.tabularTemplateId;
											$scope.columnList.push(newArr);
											$scope.columnHeaderList.push(newArr);
									     }
										$scope.tableName = response.data.listObject[0].tableName;
										angular.forEach($scope.rowList,function(value,key){
											angular.forEach($scope.savedEditedList,function(value1,key1){
												if(value.rowId == value1.rowId){
													value.rowName = value1.rowName;
												}
											});
										});
										angular.forEach($scope.columnList,function(value,key){
											angular.forEach($scope.savedEditedList,function(value1,key1){
												if(value.coulmnId == value1.coulmnId){
													value.columnName = value1.columnName;
												}
											});
										});
										angular.forEach($scope.columnHeaderList,function(value,key){
											angular.forEach($scope.savedEditedList,function(value1,key1){
												if(value.coulmnId == value1.coulmnId){
													value.columnName = value1.columnName;
												}
											});
										});
										
										console.log("$scope.rowList",$scope.rowList);
										console.log("$scope.columnList",$scope.columnList);
										
										$('#viewtemplateModal').modal('hide');
										$('#addviewTemplateModal11').modal('show');
										$scope.saveTrasactionFlag = true;
										$scope.updateTrasactionFlag = false;
								
										
			
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
		 	
           $scope.clearSearchFilter = function(){
          	 $scope.searchKeywordComplaint="";
          	 $scope.searchKeyword="";
           }
		 	 
		 	 
		 	$scope.printTemp=function(info){
        		if(info.templateFormatId == 3){
				if(info.isHtmlPrint == true){
					$scope.printDataObj={
                    		orgId : $scope.organizationId,
					        	unitId :  $scope.unitId,
					        	templateTransactionId:info.templateTransactionId,
                    	};
			        	
			        	var URI = BASE_URL + ROOT_URL + "/global/printHTMLTemplate";
			    		BillingGenericService.serviceAction(METHOD_POST, URI, $scope.printDataObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) 
			    				{
			    	
			    			   if(response.data.status=='success')
			    			      {
			    				   console.log("gg",response.data.object);
			    					 var headerSize = "";
			    					 var letterImgAndHeader="";
			    					 var patientDetails = "";
			    					 var completeTemplateContent = "";
			    						 headerSize = "105px;";
			    						 patientDetails = response.data.object.headerPart;
			    						 completeTemplateContent = patientDetails + response.data.object.templateHtml;
			    						 $scope.el= angular.element(completeTemplateContent).find('input');
			    						 console.log("inputttt", $scope.el);
			    						 angular.forEach($scope.el ,function (t,key){
			    							 var ind=completeTemplateContent.indexOf(t.outerHTML);
			    							 if(t.type == "text"){
				    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+t.value+"</label>");
			    							 }			    							 
			    							 console.log("completeTemplateContenttttt", completeTemplateContent);
			    						 });
			    						 
			    						 $scope.e2= angular.element(completeTemplateContent).find('textarea');
			    						 console.log("textareaaa", $scope.e2);
			    						 angular.forEach($scope.e2 ,function (t,key){
			    							 var ind=completeTemplateContent.indexOf(t.innerHTML);
			    							 if(t.type == "textarea"){
				    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+t.defaultValue+"</label>");
			    							 }			    							 
			    							 console.log("completeTemplateContenttttt", completeTemplateContent);
			    						 });
			    						 
			    						 $scope.e3= angular.element(completeTemplateContent).find('span');
			    						 console.log("spannn", $scope.e3);
			    						 angular.forEach($scope.e3 ,function (t,key){
			    							 var ind=completeTemplateContent.indexOf(t.innerHTML);
			    							
			    							 if(t.className == "ant-select-arrow"){
				    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+""+"</label>");
			    							 }
			    							 if(t.className == "anticon anticon-close-circle"){
				    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+""+"</label>");

			    							 }
			    							 if(t.className == "ant-select-clear"){
				    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+""+"</label>");

			    							 }
			    							 console.log("completeTemplateContenttttt", completeTemplateContent);
			    						 });
			    						 
			    						 $scope.e4= angular.element(completeTemplateContent).find('div');
			    						 console.log("divvvvv", $scope.e4);
			    						 angular.forEach($scope.e4 ,function (t,key){
			    							 var ind=completeTemplateContent.indexOf(t.innerHTML);
			    							 if(t.innerText == "Select Answer"){
			    								 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, ""); 
			    							 }
			    							 if(t.className == "ant-select-selector"){
				    							 completeTemplateContent =completeTemplateContent.replace(t.outerHTML, "<label>"+t.innerText+"</label>");
			    							 }	
			    							
			    							 
			    							 console.log("completeTemplateContenttttt", completeTemplateContent);
			    						 });
			    						 
			    						 
			    					 var printWindow = window.open('', '', 'height=500,width=700');  
                                        printWindow.document.write(completeTemplateContent);  
                                        printWindow.document.close();  
                                        $timeout( function(){
                                             printWindow.print();  
                                        },1000);			
			    			      }
			    			   else if(response.data.status=='error'){
			    				   $rootScope.stopLoader();
			    			   }
			    		});
				}
        		}else if(info.templateFormatId == 4){
					var data = {     
							"appName": "HIS",     
							"appSecretKey": "f306b6eccd9d5069d2dc71afe139271a",  
							  "reportDesignFilePath": "CoversheetTemplate_Tabular.rpt",   
							  "reportFormat": "PDF",   
							  "exportFileName": "CoversheetTemplate_Tabular",   
							  "reportParams": [     {    
								"paramName": "patient_id",    
								"paramType": "Integer",    
								"paramValue": info.patientId    
								 },    
								 {  
									  "paramName": "tabular_template_id", 
									    "paramType": "Integer",    
									"paramValue": info.tabularTemplateId    
									 },   
									  {    "paramName": "template_id",
										     "paramType": "Integer",
										     "paramValue": info.templateId  
										   },  
										   {   
											"paramName": "op_ip_id",  
											   "paramType": "Integer", 
											    "paramValue": info.admissionId  
											  } ]
					};
					reportService.generate(REPORT_API_URI1,data);
				}else{
					var data = {
							"appName" : "HIS",
							"appSecretKey" : "f306b6eccd9d5069d2dc71afe139271a",
							"reportDesignFilePath" : "CoversheetTemplate.rpt",
							"reportFormat" : "PDF",
							"exportFileName" : "Template",
							"reportParams" : [ {
								"paramName" : "template_transaction_id",
								"paramType" : "Integer",
								"paramValue" : info.templateTransactionId
							} ]
						};
						reportService.generate(REPORT_API_URI1,
								data);
				}
	}
}
]);