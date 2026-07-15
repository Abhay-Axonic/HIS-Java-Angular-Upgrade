angular
		.module('myApp')
		.controller(
				'icpcCodesConfigurationController',
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
								GenericService,$sessionStorage,$cookies, growl, $localStorage ,PaginationService) {
							var self = this;
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							$scope.updateFlag=false;
						
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
							
							//start for ICPC System list
							$scope.icpcSystemList = [];
							$scope.getIcpcSystemList = function(){
							var URI = BASE_URL + ROOT_URL + "/api/opd/getICPCSYSTEMList";
						
							GenericService.serviceAction(METHOD_GET, URI).then(function(response) {
							if(response.data.status == "success"){
								$scope.icpcSystemList = response.data.listObject;
								console.log("$scope.icpcSystemList",$scope.icpcSystemList);
							}
						});
							//end for icpc system list
							}
						$scope.getIcpcSystemList();		
						
						
						$scope.icpcSystemObj={
								icpcSystemId:'',
								icpcSystemDesc:""
						};
						
						$scope.icpcMasterObj={
								icpcId:''	
						};
						
						//get All Active Speciality
						$scope.specialityList=[];
						$scope.selectedSpeacilityList = [];
						$scope.getSpecialityList = function(){
							var URI = BASE_URL + ROOT_URL + "/adt/getActiveSpecialityList";
							var obj = {organizationId:$scope.organizationId, unitId:$scope.unitId};
							GenericService.serviceAction(METHOD_POST,URI, obj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								if (response.data.status == "success"){
									
									angular.forEach(response.data.listObject,
											function(value,index) {
												var data = {};
												data.id = value.specialityId;
												data.label = value.specialityName;
												$scope.specialityList.push(data);
											});
								}
									
							});
						}	
					
						$scope.getSpecialityList();
						
						$scope.specialityType = [];
						$scope.clicked = function(type) {
							
							if (type == 'speciality') {
								
								$scope.selectedSubSpecialityList = [];
								$scope.selectedServiceList = [];
								$scope.subSpecialityList = [];
								$scope.specialityType = [];
								if ($scope.selectedSpeacilityList.length > 0) {
									
									angular.forEach($scope.selectedSpeacilityList,
											function(value, index) {
										$scope.specialityType.push(value.id);
											});
								
									var isExists;
									function checkExistsOrNot(arr, val) {
										return arr
												.some(function(arr) {
													return (val.id === arr.id);
												});
									}
									
									
								}

							}
						}
							
							// save Data
							$scope.saveICPCCodesConfigurationMapper = function(){
								if($scope.icpcSystemObj.icpcSystemId)
								{ 
									if($scope.selectedSpeacilityList.length > 0)
										{
										$rootScope.startLoader();
							
								
							
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										specialityIdList :$scope.specialityType,
										icpcSystemId: $scope.icpcSystemObj.icpcSystemId , 
										createdBy: $scope.userId,
										updatedBy: $scope.userId,
										status: "A",
										createdDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
										icpcCodeConfigurationId:$scope.icpcCodeConfigurationId
								};
								
							
								console.log(data);
								var URI = BASE_URL+ ROOT_URL+ "/api/adt/saveICPCCodesConfigurationMapper";
								GenericService
										.serviceAction(METHOD_POST,URI, data)
										.then(function(response) {
											
											if (response.data.status == 'success')
											{
												$rootScope.stopLoader();
												
												if(!$scope.updateFlag){
												growl.success(response.data.message,
														{
															title : 'Success!'
														});
												}else{
													growl.success("Updated successfully",
															{
																title : 'Success!'
															});
												}
												$scope.icpcSystemObj = {};
												$scope.selectedSpeacilityList = [];
												$scope.getSpecialityList();
												$scope.icpcCodeConfigurationId=null;
												$scope.getICPCCodesConfigurationList();
												$scope.updateFlag=false;
												$scope.$broadcast('show-errors-reset');
											} else if(response.data.status == 'error')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
											}
											else if(response.data.status == 'Already Exists.')
											{
												$rootScope.stopLoader();
												growl.error(response.data.message,
														{
															title : 'Error!'
														});
												$scope.getICPCCodesConfigurationList();
											}

										});
										}
									else {
										growl.error("Please Select Speciality",
												{
													title : 'Error!'
												});
									}
									
								}
								else
									{
									growl.error("Please Select System",
											{
												title : 'Error!'
											});
									}
							}
							
							//Get ICPCCodesConfigurationMapper List
							$scope.iCPCCodesConfigurationList = [];
						//	$scope.finalList = [];
							$scope.specialityNameList=[];
							
							$scope.getICPCCodesConfigurationList = function(){
								$rootScope.startLoader();
								var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										status: "A"
									//	createdBy: $scope.userId,
										
								};
								
								console.log(data);
								var URI = BASE_URL+ ROOT_URL+ "/api/adt/getICPCCodesConfigurationMapperList";
								GenericService
										.serviceAction(METHOD_POST,URI, data)
										.then(function(response) {
											if (response.data.status == 'success')
											{
												
												 $scope.pagination.data=[];
												 
												 $scope.iCPCCodesConfigurationList = response.data.listObject;
												 
												 if($scope.iCPCCodesConfigurationList.length > 0){            
							                         PaginationService.searchData($scope.iCPCCodesConfigurationList, $scope.pagination);
							                       }
												 
												/*
												console.log($scope.iCPCCodesConfigurationList);
												 if($scope.iCPCCodesConfigurationList.length>0 && $scope.iCPCCodesConfigurationList!=null)
						    					   {
						    					   angular.forEach($scope.iCPCCodesConfigurationList, function(value, key) 
						    						 {
						    						   console.log(value);
						    						   console.log(key);
						    							 //push data into list
			    										var listData ={
			    												icpcSystemName: value.systemName,
			    												specialityNameList : value.specialityNameList,
			    												status : value.status,
			    												icpcCodeConfigurationMpprIds : value.icpcCodeConfigurationMpprIds,
			    												
			    										};
			    										
			    										$scope.finalList.push(listData);*/
			    										
			    										$rootScope.stopLoader();
						    						// });
						    					   //console.log($scope.finalList);
						    					//   }
												// $rootScope.stopLoader();
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
							
							//get selectedsplList 
							$scope.getICPCCodesConfigurationList();
						//	$scope.selectedsplList=[];
							/*$scope.getSpeciality=function(splList)
							{
								$scope.selectedsplList=splList;
							}*/
							
							$scope.specialityListByID=[];
							$scope.specialityListForUpdate=[];
							$scope.getSpecialityListByID=function(info)
							{
								try{
									$rootScope.startLoader();
									var data = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										icpcConfigurationId: info.icpcSystemId
									};
									
									var URI = BASE_URL + ROOT_URL + "/api/adt/getSpecialityListByIcpcSystemId"
 									 		
									return GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											$scope.specialityListByID = response.data.listObject;
											console.log("$scope.specialityListByID",$scope.specialityListByID);
											$scope.specialityListForUpdate = [];
											angular.forEach(response.data.listObject,
													function(value,index) {
														var data = {};
														data.id = value.specialityId;
														data.label = value.specialityName;
														$scope.specialityListForUpdate.push(data);
													});
											return true;
											
										} else if(response.data.status == 'error')
										{
											$rootScope.stopLoader();
											growl.error(response.data.message,
													{
														title : 'Error!'
													});
											return false;
										}
									});
								}catch(e){
									console.log("Exception",e);
									return false;
								}
							}
						
							//update status
							$scope.updateStatus = function(icpcCodeConfigurationId,status){
								try{
									var date = new Date();
									
									$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
									var data = {
										updatedDate : $scope.dt,
										updatedBy : $scope.userId,
										status : status,
										icpcConfigurationId: icpcCodeConfigurationId
									};
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + "/api/adt/updateICPCCodesConfigurationStatus"
 									
									GenericService.serviceAction("POST",URI,data).then(function(response){
										if (response.data.status == 'success')
										{
											$rootScope.stopLoader();
											growl.success(response.data.message,
													{
														title : 'Success!'
													});
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
							
							$scope.icpcCodeConfigurationId=null;
							
							//updateDetails
							$scope.updateDetails = function(info)
							{
								
								try{
									$scope.icpcCodeConfigurationId=info.icpcCodeConfigurationId;
									$scope.updateFlag=true;
									$scope.selectedSpeacilityList = [];
									$scope.icpcSystemObj.icpcSystemId = info.icpcSystemId;
									$scope.icpcSystemObj.icpcSystemDesc = info.systemName;
									var promise = $scope.getSpecialityListByID(info);
									promise.then(function(flag) 
											{
											  if(flag)
												  {
													$rootScope.stopLoader();
													console.log("specialityListForUpdate",$scope.specialityListForUpdate);
													$scope.selectedSpeacilityList =  $scope.specialityListForUpdate;
												  }
											  else{
												  $rootScope.stopLoader();
												  growl.error("Something went wrong..",{
														title : ERROR_MSG
													});
											   }
										    });
									
								
								}catch(e){
									console.log("Exception",e);
								}
							}
			}]);
							