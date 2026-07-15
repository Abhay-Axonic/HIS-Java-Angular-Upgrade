angular
		.module('myApp')
		.controller(
				'patientTypeController',
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
						'PaginationService',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,PaginationService) {
							
							$(".selectedPageName").text("Patient Type Details ")
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$rootScope.loginpage = true;
							$scope.saveBtnFlag = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.specialityList=[];
							$scope.subSpecialityList=[];
							$scope.groupDetailList=[];
							$scope.groupDetailsObj = {
									specialityId: '',
									subSpecialityId: '',
									markupStatus: ''
								};
							$scope.serviceDetailsObj = {
									specialityId: '',
									subSpecialityId: '',
									markupStatus: '',
									specialityName: '',
									subSpecialityName: '',
									percentage: '',
									serviceId: ''
								};
							$scope.patientTypeObj = {
									code: '',
									description: '',
									patientTypeId: ''
								};
							
							$scope.pagination = {
				                    currentPage: 1,
				                    totalPages: 0,
				                    pageSize: '10',
				                    search: '',
				                    searchData: PaginationService.searchData,
				                    setTotalPages: PaginationService.setTotalPages,
				                    getPagesList: PaginationService.getPagesList,
				                    setPage: PaginationService.setPage,
				                    setFirstPage: PaginationService.setFirstPage,
				                    setPreviousPage: PaginationService.setPreviousPage,
				                    setNextPage: PaginationService.setNextPage,
				                    setLastPage: PaginationService.setLastPage,
				                    sortBy: PaginationService.sortBy
				                  }
					
							$scope.patientTypeList = [];
							$scope.specialityList= [];
							$scope.serviceDetailList=[];
							$scope.serviceListBySpeciality=[];
							$scope.getSpecialityList = function () {
								var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
								var obj = { organizationId: $scope.organizationId, unitId: $scope.unitId };
								GenericService.serviceAction(METHOD_POST, URI, obj).then(function (response) {
									if (response.data.status == "success")
										$scope.specialityList = response.data.listObject;
								});
							}
							$scope.saveBtnFlag = true;
							$scope.getSpecialityList();
							
							
							
							var isGroupSubgroupExists;
							function checkGroupSubgroupExistsOrNot(arr, val) {
								return arr
										.some(function(arr) {
											if(val.specialityId == parseInt(arr.specialityId) && val.subSpecialityId == parseInt(arr.subSpecialityId))
												{
													return true;
												}
											else
												{
													return false;
												}
										});
							}
							
							$scope.removeProcedure = function(index){
								$scope.procedureWiseList.splice(index,1);
							}
							
							
							
							$scope.getSubSpecialityBySpeciality = function(obj)
							{
								$scope.groupDetailsObj.specialityName = obj.specialityName;
								$scope.groupDetailsObj.specialityId = obj.specialityId; 
								
								$scope.serviceDetailsObj.specialityName = obj.specialityName;
								$scope.serviceDetailsObj.specialityId = obj.specialityId; 
								
								var data = {
										unitId : $scope.unitId,
										organizationId : $scope.organizationId
									};
									data.specialityId = obj.specialityId;
									var URI = BASE_URL + ROOT_URL
											+ GETSUBSPECIALITYBYSPECIALITYID;
									GenericService
											.serviceAction(METHOD_POST, URI, data,
													NOTIFICATION_MSG_STATUS_FALSE)
											.then(
													function(response) {
														console.log(response);
														$scope.subSpecialityList = [];
														// $rootScope.startLoader();
														if (response.data.status == "success")
															$scope.subSpecialityList = response.data.listObject;
													});
								
							}
							
							$scope.getSubSpecialityDetails = function(obj2)
							{
								$scope.groupDetailsObj.subSpecialityName = obj2.subSpecialityName;
								$scope.groupDetailsObj.subSpecialityId = obj2.subSpecialityId; 
								
								$scope.serviceDetailsObj.subSpecialityName = obj2.subSpecialityName;
								$scope.serviceDetailsObj.subSpecialityId = obj2.subSpecialityId; 
							}
							
							
							$scope.addGroupDetailsToList = function()
							{
								

								if($scope.patientTypeId == '' || $scope.patientTypeId == null)
									{
									    growl.warning("Please select patient type",
											{
												title : 'Warning!'
											});
									    return false;

									}
								
								else if($scope.groupDetailsObj.specialityId == '' || $scope.groupDetailsObj.specialityId == null)
									{
									    growl.warning("Please select specialty",
											{
												title : 'Warning!'
											});
									    return false;

									}
								else if($scope.groupDetailsObj.subSpecialityId == '' || $scope.groupDetailsObj.subSpecialityId == null)
									{
										growl.warning("Please select sub specialty",
												{
													title : 'Warning!'
												});
										    return false;
									}
								else if($scope.groupDetailsObj.markupStatus == '' || $scope.groupDetailsObj.markupStatus == null)
									{
										growl.warning("Please select markup status",
												{
													title : 'Warning!'
												});
										    return false;
									}
								else
									{
									
									
									var data={
											markupStatus : $scope.groupDetailsObj.markupStatus,
											specialityId : $scope.groupDetailsObj.specialityId,
											specialityName : $scope.groupDetailsObj.specialityName,
											subSpecialityId : $scope.groupDetailsObj.subSpecialityId,
											subSpecialityName : $scope.groupDetailsObj.subSpecialityName,
											percentage: 0,
											organizationId : $scope.organizationId,
											unitId: $scope.unitId,
											createdBy: $scope.userId,
											updatedBy: $scope.userId,
											patientTypeId:$scope.patientTypeId
									};
						
									isGroupSubgroupExists=checkGroupSubgroupExistsOrNot($scope.groupDetailList,data)
									if (!isGroupSubgroupExists) {
										$scope.groupDetailList.push(data);
										console.log($scope.groupDetailList);
										
									}
									else
										{
											growl.warning("Already added in the list",{
												title : "WARNING"
											});
											return;
										}
								}
								
							}
							
							
							$scope.addServiceDetailsToList = function()
							{
								
								var data={
										markupStatus : $scope.groupDetailsObj.markupStatus	,
										specialityId : $scope.groupDetailsObj.specialityId,
										specialityName : $scope.groupDetailsObj.specialityName,
										subSpecialityId : $scope.groupDetailsObj.subSpecialityId,
										subSpecialityName : $scope.groupDetailsObj.subSpecialityName,
										serviceId: '',
										percentage: '',
										markupStatus: '',
										organizationId : $scope.organizationId,
										unitId: $scope.unitId,
										createdBy: $scope.userId,
										updatedBy: $scope.userId,
										patientTypeId:''
								};
								
								$scope.serviceDetailList.push(data);
								console.log($scope.serviceDetailList);
							}
							
						$scope.savePatientType = function(patientTypeObj)
						{
							if($scope.patientTypeObj.code=="" ){
								
								growl.warning("Please Enter Code",
										{
											title : 'warning!'
										});
								return false;
							}else if($scope.patientTypeObj.description=="" ){
								
								growl.warning("Please Enter Description",
										{
											title : 'warning!'
										});
								return false;
							}else{
							console.log(patientTypeObj);
							var dataForPatientType = {
								createdBy : $scope.userId,
								organizationId : $scope.organizationId,
								unitId : $scope.unitId,
								updatedBy: $scope.userId,
								code : patientTypeObj.code,
								description: patientTypeObj.description
							};
							
							var URI = BASE_URL + ROOT_URL +"/api/billing/savePatientType";
						GenericService
							.serviceAction(METHOD_POST, URI, dataForPatientType,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.startLoader();
										if (response.data.status == "success"){
											$scope.stopLoader();
											$scope.saveBtnFlag = true;
											$scope.updateBtnFlag = false;
											$scope.patientTypeObj = {};
											$scope.getActivePatientTypeList();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										}
										else{
											$scope.stopLoader();
											growl.warning(response.data.message,
													{
														title : 'warning!'
													});
										}
									});
							}
						}
						
						
						$scope.getActivePatientTypeList = function()
						{
							var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientType/"+$scope.organizationId+"/"+$scope.unitId;
						GenericService
							.serviceAction(METHOD_GET, URI,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										if (response.data.status == "success"){
											
											 $scope.pagination.data=[];
											$scope.patientTypeList = response.data.listObject;
											
											
											 if($scope.patientTypeList.length > 0){            
						                         PaginationService.searchData($scope.patientTypeList, $scope.pagination);
						                       }
											console.log("list",$scope.patientTypeList);
										}
										
									});
							
						}
						
						$scope.getActivePatientTypeList();
						
						$scope.getServicesBySpecialityAndSubSpeciality = function()
						{
							console.log($scope.serviceDetailsObj);
							
							 $scope.data={

										"subSpecialityIdList":[$scope.serviceDetailsObj.subSpecialityId],
										"orgnisationId":$scope.organizationId,
										"unitId":$scope.unitId
										
							 }
							 var URI = BASE_URL + ROOT_URL+ "/global/searchUnitServiceByMultipleSpecialityAndSubSpeciality";
							GenericService
							.serviceAction(METHOD_POST, URI, $scope.data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										$scope.startLoader();
										if (response.data.status == "success")
											$scope.stopLoader();
									//	$scope.serviceListBySpeciality = response.data.listObject;
										console.log("spl List",response.data.listObject);
										
										angular
										.forEach(response.data.listObject,function(value,index) {
											var newArr = {};
											newArr.serviceId = value.serviceId;
											newArr.serviceName = value.serviceStandardName;
											newArr.specialityId = value.specialityId,
											newArr.specialityName = value.specialityName,
											newArr.percentage = '',
											newArr.markupStatus = $scope.serviceDetailsObj.markupStatus,
											newArr.subSpecialityId = value.subSpecialityId,
											newArr.subSpecialityName = value.subSpecialityName,
											newArr.organizationId = $scope.organizationId,
											newArr.unitId= $scope.unitId,
											newArr.createdBy= $scope.userId,
											newArr.updatedBy= $scope.userId,
											newArr.patientTypeId= $scope.groupDetailsObj.patientTypeId
											$scope.serviceListBySpeciality.push(newArr);
											});
									
											console.log($scope.serviceListBySpeciality);
										
									});
						}
						
						$scope.savePatientTypeDetails = function()
						{
							if($scope.serviceListBySpeciality.length > 0 && $scope.groupDetailList.length > 0)
							{
							 $rootScope.startLoader();
							var dataForSave = {
									patientTypeServiceWiseDetails:$scope.serviceListBySpeciality,
									patientTypeGroupWiseDetails:$scope.groupDetailList
								
							};
							
							var URI = BASE_URL + ROOT_URL +"/api/billing/savePatientTypeWiseDetails";
						GenericService
							.serviceAction(METHOD_POST, URI, dataForSave,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
										
										if (response.data.status == "success"){
											
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
										$scope.serviceListBySpeciality = [];
										$scope.groupDetailList = [];
										}
										else{
											growl.warning(response.data.message,
													{
														title : 'warning!'
													});
										}
										$rootScope.stopLoader();
									});
							}
							else
								{
									growl.warning("Please add group and service",
										{
											title : 'Warning!'
										});
									return false;
								}
						}
						
						$scope.patientTypeWiseDetails={};
						$scope.patientTypeServiceWiseDetailsList=[];
						$scope.patientTypeGroupWiseDetailsList=[];
						
						$scope.getPatientTypeDetailsById = function(id)
						{
							
						
							var URI = BASE_URL + ROOT_URL +"/api/billing/getPatientTypeServiceWiseDetails/"+id+"/"+$scope.organizationId+"/"+$scope.unitId;
						GenericService
							.serviceAction(METHOD_GET, URI,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
										console.log(response);
									//	$scope.startLoader();
										if (response.data.status == "success"){
										//	$scope.stopLoader();
											
											$scope.patientTypeWiseDetails = response.data.object;
											//$scope.patientTypeServiceWiseDetailsList = $scope.patientTypeWiseDetails.patientTypeServiceWiseDetails;
											$scope.patientTypeGroupWiseDetailsList = $scope.patientTypeWiseDetails.patientTypeGroupWiseDetails;
											console.log("srv",$scope.patientTypeWiseDetails);
											/*growl.success(response.data.message,
												{
													title : 'Success!'
												});*/
										}
										else{
											growl.success(response.data.message,
													{
														title : 'error!'
													});
										}
									});
							
						}
						
						
						$scope.removeAddedGroup = function(groupDetailList,index) {
							$scope.groupDetailList.splice(index,1)
						}
						
						$scope.removeAddedServices = function(serviceListBySpeciality,index) {
							$scope.serviceListBySpeciality.splice(index,1)
						}
						
						
						$scope.updateStatus = function(Id,status){
							try{
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								var data={
										updatedBy:$scope.userId,
										updatedDate:$scope.dt,
										status:status,
										patientTypeId:Id
								};
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/billing//updatePatientTypeStatus";
								console.log("update status",data);
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
//										$scope.$broadcast('show-errors-reset');
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								});
							}catch(e){
								console.log("Exception",e);
							}
						}
						
						$scope.showUpdateBtn = function(info){
							try{
								
								$scope.patientTypeObj.code = info.code;
								$scope.patientTypeObj.description = info.description;
								$scope.patientTypeObj.patientTypeId = info.patientTypeId;
 								$scope.saveBtnFlag = false;
								$scope.updateBtnFlag = true;
							
							}catch(e){
								console.log("Exception-",e);
							}
						}
						
						$scope.updatePatientType = function(){
							try{
								if($scope.patientTypeObj.code=="" || $scope.patientTypeObj.code==undefined){
									
									growl.warning("Please Enter Code",
											{
												title : 'warning!'
											});
									return false;
								}else if($scope.patientTypeObj.description=="" || $scope.patientTypeObj.description==undefined){
									
									growl.warning("Please Enter Description",
											{
												title : 'warning!'
											});
									return false;
								}else{
							
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');			
								var dataForPatientType = {
										createdBy : $scope.userId,
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										updatedBy: $scope.userId,
										code : $scope.patientTypeObj.code,
										description: $scope.patientTypeObj.description,
										patientTypeId : $scope.patientTypeObj.patientTypeId
									};
									var URI = BASE_URL + ROOT_URL +"/api/billing/updatePatientType";
								GenericService
									.serviceAction(METHOD_POST, URI, dataForPatientType,
											NOTIFICATION_MSG_STATUS_FALSE)
									.then(
											function(response) {
												console.log(response);
												$scope.startLoader();
												if (response.data.status == "success"){
													$scope.stopLoader();
													$scope.getActivePatientTypeList();
													$scope.patientTypeObj.description='';
													$scope.patientTypeObj.patientTypeId='';
													$scope.patientTypeObj.code='';
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												
												$scope.saveBtnFlag = true;
												$scope.updateBtnFlag = false;
												}
												else{
													$scope.stopLoader();
													growl.warning(response.data.message,
															{
																title : 'warning!'
															});
												}
											});
									
								}
							}catch(e){
								console.log("Exception-",e);
							}
						}
						
						$scope.updateGroupStatus = function(Id,status){
							try{
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								var data={
										updatedBy:$scope.userId,
										updatedDate:$scope.dt,
										status:status,
										patientTypeGroupId:Id
								};
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/billing/updatePatientTypeGroupStatus";
								console.log("update status",data);
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
//										$scope.$broadcast('show-errors-reset');
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								});
							}catch(e){
								console.log("Exception",e);
							}
						}
						
						$scope.updateServiceStatus = function(Id,status){
							try{
								var date = new Date();
								$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
								var data={
										updatedBy:$scope.userId,
										updatedDate:$scope.dt,
										status:status,
										patientTypeServiceId:Id
								};
								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/api/billing//updatePatientTypeServiceStatus";
								console.log("update status",data);
								GenericService.serviceAction("POST",URI,data).then(function(response){
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
//										$scope.$broadcast('show-errors-reset');
									} else if(response.data.status == 'error')
									{
										$rootScope.stopLoader();
										growl.error(response.data.message,
												{
													title : 'Error!'
												});
									}
								});
							}catch(e){
								console.log("Exception",e);
							}
						}
						
						
						/*First Level*/
				    	$scope.firstTabLevel = 1 ;
						
						$scope.setFirstTabLevel= function(newTab)
						{
							$scope.firstTabLevel = newTab;
							if(newTab == 2)
								{
								
								$rootScope.startLoader();
								
								var subGroupIds = [];
								angular.forEach($scope.groupDetailList,function(value,index){
									subGroupIds.push(value.subSpecialityId);
								});
								
								
									http://localhost:8081/HIS-WAR
	
									var data = 
									{
										"orgnisationId":$scope.organizationId,
										"unitId":$scope.unitId,
										"subSpecialityIdList":subGroupIds
									}
							
									var URI = BASE_URL + ROOT_URL + "/global/searchUnitServiceByMultipleSpecialityAndSubSpeciality";
									console.log("update status",data);
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$scope.serviceListBySpeciality = [];
											angular.forEach(response.data.listObject,function(value,index){
												angular.forEach($scope.groupDetailList,function(value1,index1){
													if(value.specialityId == value1.specialityId && value.subSpecialityId == value1.subSpecialityId)
														{
														var data={
																markupStatus : value1.markupStatus	,
																specialityId : value.specialityId,
																specialityName : value.specialityName,
																subSpecialityId : value.subSpecialityId,
																subSpecialityName : value.subSpecialityName,
																serviceId: value.serviceId,
																percentage: value1.percentage,
																organizationId : $scope.organizationId,
																unitId: $scope.unitId,
																createdBy: $scope.userId,
																updatedBy: $scope.userId,
																patientTypeId:$scope.patientTypeId,
																serviceName : value.serviceStandardName
														};
														$scope.serviceListBySpeciality.push(data);
														
														}
												});
											});
											
										}
										$rootScope.stopLoader();
									});
							
							
								}
						};
						
						$scope.isSetFirstTabLevel = function(tabNum)
						{
							return $scope.firstTabLevel === tabNum;
						};
						
			}]);
							