'use strict';
angular.module('myApp').controller(
				'couponTemplateController',
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
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,$stateParams ) {
							
							$(".selectedPageName").text("Coupon Template Master")
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
									subSpecialityId: ''
								};
							$scope.percentage = '';
							$scope.amount = '';
							$scope.couponTemplateName = '';
							$scope.serviceId = null;
							$scope.couponTemplateDescription = '';
							$scope.expireDays = '';
							$scope.couponTemplateMasterId= null;
							$scope.saveRemoveList = [];
							$scope.serviceDetailsObj = {
									specialityId: '',
									subSpecialityId: '',
									markupStatus: '',
									specialityName: '',
									subSpecialityName: '',
									percentage: '',
									serviceId: ''
								};
							 $scope.search="";
							 $scope.serviceName="";
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
							
							
							$scope.getcouponTemplateDetailsById = function(couponTemplateMasterId)
							{
									 var data={
												"couponTemplateMasterId":couponTemplateMasterId
									 }
									
								var URI = BASE_URL + ROOT_URL +"/api/billing/coupon/getCouponTemplateDetails";
							GenericService .serviceAction("POST", URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(
										function(response) {
											console.log(response);
											if (response.data.status == "success"){
												
												$scope.couponTemplateName = response.data.object.couponTemplateName;
												$scope.serviceName = response.data.object.serviceName;
												$scope.serviceId = response.data.object.serviceId;
												$scope.lable = response.data.object.serviceName;
												$scope.id = response.data.object.serviceId;
												$scope.couponTemplateDescription = response.data.object.couponTemplateDescription;
												$scope.expireDays = response.data.object.expireDays;
												$scope.couponTemplateMasterId= response.data.object.couponTemplateMasterId;
												
												angular.forEach(response.data.object.couponTemplateDetailsDtoList,function(value,index) {
												
													var data={
															couponTemplateDetailsId : value.couponTemplateDetailsId,
															couponTemplateMasterId : value.couponTemplateMasterId,
															specialityId : value.specialityId,
															specialityName : value.specialityName,
															subSpecialityId : value.subSpecialityId,
															subSpecialityName : value.subSpecialityName,
															serviceId : value.serviceId,
													    	serviceName : value.serviceName,
															percentage: value.percentage,
															organizationId : $scope.organizationId,
															unitId: $scope.unitId,
															createdBy: $scope.userId,
															updatedBy: $scope.userId,
															status: value.status,
															amount:value.amount
													};
													
													$scope.groupDetailList.push(data);
													
												});
											}
											
										});
								
							}
							
							if($stateParams.searchObj!=null)
 							{
								$scope.info = $stateParams.searchObj;
								if($scope.info.couponTemplateMasterId != undefined){
									$scope.getcouponTemplateDetailsById($scope.info.couponTemplateMasterId);
								}
																
 							}
							
							$scope.searchServices = function(searchKeyword){
								$scope.servicesList=[];
								searchKeyword=searchKeyword+"---"+0;
								var serData = {
										organizationId : $scope.organizationId,
										unitId : $scope.unitId,
										searchKeyword : searchKeyword
								}
								var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
								
								return BillingGenericService.serviceAction(METHOD_POST,URI,serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
									if(response.data.status == "success"){
										$scope.servicesList = response.data.listObject;
									}
									return $scope.servicesList;
								});
							}
							
						    $scope.addServiceToTempServiceList = function($item, $model, $label)
						    {
						    	$scope.selectedServiceId = $item.serviceMasterId;
						    	$scope.selectedServiceName = $item.serviceStandardName +'/' + $item.serviceStandardCode;
						    	
						    	$scope.groupDetailsObj.specialityName = $item.specialityName;
								$scope.groupDetailsObj.specialityId = $item.specialityId; 
								
								$scope.groupDetailsObj.subSpecialityName = $item.subSpecialityName;
								$scope.groupDetailsObj.subSpecialityId = $item.subSpecialityId; 
								
							}
						    
						  
							
							var isGroupExists;
							function checkGroupExistsOrNot(arr, val) {
								return arr
										.some(function(arr) {
											if(val.specialityId == parseInt(arr.specialityId) && (arr.subSpecialityId == '' || arr.subSpecialityId == null) && (arr.serviceId == '' || arr.serviceId == null || arr.serviceId == undefined))
												{
													return true;
												}
											else
												{
													return false;
												}
										});
							}
							
							
							var isGroupSubgroupExists;
							function checkGroupSubGroupExistsOrNot(arr, val) {
								return arr
										.some(function(arr) {
											if(val.specialityId == parseInt(arr.specialityId) && val.subSpecialityId == parseInt(arr.subSpecialityId)  && (arr.serviceId == '' || arr.serviceId == null || arr.serviceId == undefined))
												{
													return true;
												}
											else
												{
													return false;
												}
										});
							}
							
							
								var isServiceExists;
							function checkSerivceExistsOrNot(arr, val) {
								return arr.some(function(arr) {
											if(val.specialityId === parseInt(arr.specialityId) && val.subSpecialityId === parseInt(arr.subSpecialityId)  && val.serviceId === parseInt(arr.serviceId))
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
							
							
							$scope.popupForSaveDetails = function(){
								
								if($scope.couponTemplateName == '' || $scope.couponTemplateName == null)
								{
								    growl.warning("Please enter coupon template name",
										{
											title : 'Warning!'
										});
								    return false;

								}

							else if($scope.couponTemplateDescription == '' || $scope.couponTemplateDescription == null)
							{
							    growl.warning("Please enter coupon template description",
									{
										title : 'Warning!'
									});
							    return false;

							}
							else if($scope.expireDays == '' || $scope.expireDays == null)
							{
							    growl.warning("Please enter expire days",
									{
										title : 'Warning!'
									});
							    return false;

							}
							else{
							
								if($scope.groupDetailList.length == null ||  $scope.groupDetailList.length == 0){
									growl.warning("Please add atleast one service",
											{
												title : 'Warning!'
											});
								}
								
								angular.forEach($scope.groupDetailList,function(value,index) {
								if(value.percentage<0)
									{
									growl.warning("Negative data should not be accepted",
											{
												title : 'Warning!'
											});
									}
								else
									{
									angular.element('#confirmSavePatientTypeDetails').modal('show');
									}
									
								})
								
							}
								
								
							}
							
							$scope.getSubSpecialityBySpeciality = function(obj)
							{
								$scope.groupDetailsObj.specialityName = obj.specialityName;
								$scope.groupDetailsObj.specialityId = obj.specialityId; 
								
								$scope.groupDetailsObj.subSpecialityName = null;
								$scope.groupDetailsObj.subSpecialityId = null; 
								
								
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
														if (response.data.status == "success")
															$scope.subSpecialityList = response.data.listObject;
													});
								
							}
							
							$scope.getSubSpecialityDetails = function(obj2)
							{
								$scope.groupDetailsObj.subSpecialityName = obj2.subSpecialityName;
								$scope.groupDetailsObj.subSpecialityId = obj2.subSpecialityId; 
								
							}
							
							
							
							$scope.addGroupDetailsToList = function()
							{
								
								if(($scope.groupDetailsObj.specialityId == '' || $scope.groupDetailsObj.specialityId == null) &&  ($scope.selectedServiceId == '' || $scope.selectedServiceId == null || $scope.selectedServiceId == undefined))
									{
									    growl.warning("Please select specialty/service",
											{
												title : 'Warning!'
											});
									    return false;

									}
								else if(($scope.amount == 0 || $scope.amount == '') && ($scope.percentage == 0 || $scope.percentage == '')){
									growl.warning(" Please Enter Percentage Or Amount",
											{
												title : 'Warning!'
											});
									    return false;
								}
								else
									{
									
									var data={
											specialityId : $scope.groupDetailsObj.specialityId,
											specialityName : $scope.groupDetailsObj.specialityName,
											subSpecialityId : $scope.groupDetailsObj.subSpecialityId,
											subSpecialityName : $scope.groupDetailsObj.subSpecialityName,
											serviceId : $scope.selectedServiceId,
									    	serviceName : $scope.selectedServiceName,
											percentage: $scope.percentage,
											organizationId : $scope.organizationId,
											unitId: $scope.unitId,
											createdBy: $scope.userId,
											updatedBy: $scope.userId,
											status: 'A',
											amount:$scope.amount
									};
									
									$scope.groupDetailList.push(data);
									console.log($scope.groupDetailList);
									$scope.groupDetailsObj = {};
									$scope.selectedServiceId = null;
							    	$scope.selectedServiceName = null;
							    	$scope.search = '';
							    	$scope.obj = null;
							    	$scope.obj1 = null;
							    	$scope.percentage = '';
							    	$scope.amount = '';
								
								}
								
							}
							
							$scope.removeAddedGroup = function(groupDetailList,index) {
								
								if(groupDetailList.couponTemplateDetailsId != undefined && groupDetailList.couponTemplateDetailsId != null) {
									groupDetailList.status = 'I';
									$scope.saveRemoveList.push(groupDetailList);
								}
								
								$scope.groupDetailList.splice(index,1)
							}
							
						
							
							
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
										console.log("spl List",response.data.listObject);
										
										angular.forEach(response.data.listObject,function(value,index) {
											var newArr = {};
											newArr.serviceId = value.serviceId;
											newArr.serviceName = value.serviceStandardName;
											newArr.specialityId = value.specialityId,
											newArr.specialityName = value.specialityName,
											newArr.percentage = '',
											newArr.amount = '';
											newArr.markupStatus = $scope.serviceDetailsObj.markupStatus,
											newArr.subSpecialityId = value.subSpecialityId,
											newArr.subSpecialityName = value.subSpecialityName,
											newArr.organizationId = $scope.organizationId,
											newArr.unitId= $scope.unitId,
											newArr.createdBy= $scope.userId,
											newArr.updatedBy= $scope.userId,
											$scope.serviceListBySpeciality.push(newArr);
											});
									
											console.log($scope.serviceListBySpeciality);
										
									});
						}
						
						$scope.savePatientTypeDetails = function()
						{
							$scope.startLoader();
							 var data={

										"organizationId":$scope.organizationId,
										"unitId":$scope.unitId,
										"expireDays":$scope.expireDays,
										"couponTemplateName":$scope.couponTemplateName,
										"couponTemplateDescription":$scope.couponTemplateDescription,
										"updatedBy":$scope.userId,
										"createdBy":$scope.userId,
										"couponTemplateMasterId" :$scope.couponTemplateMasterId
							 }
							 
							if( $scope.serviceName == null || $scope.serviceName == ""){
								data.serviceId = null;
							} else{
								data.serviceId = $scope.serviceId;
							}
							 
							 
							var couponDetailsList = []; 
							 angular.forEach($scope.groupDetailList,function(value,index) {
									
								 var newArr = {};
									newArr.serviceId = value.serviceId;
									newArr.specialityId = value.specialityId,
									newArr.percentage =value.percentage,
									newArr.amount = value.amount;
									newArr.subSpecialityId = value.subSpecialityId,
									newArr.organizationId = $scope.organizationId,
									newArr.unitId= $scope.unitId,
									newArr.createdBy= $scope.userId,
									newArr.updatedBy= $scope.userId,
									newArr.status= value.status,
									newArr.couponTemplateDetailsId = value.couponTemplateDetailsId,
									couponDetailsList.push(newArr);
									
									});
							 couponDetailsList =  couponDetailsList.concat($scope.saveRemoveList); 
							 data.couponTemplateDetailsDtoList = couponDetailsList;
							 
							 var URI = BASE_URL + ROOT_URL+ "/api/billing/coupon/saveCouponTemplateMaster";
							GenericService.serviceAction(METHOD_POST, URI,data,NOTIFICATION_MSG_STATUS_FALSE).then(
									function(response) {
									
										angular.element('#confirmSavePatientTypeDetails').modal('hide');
										$scope.stopLoader();
										
										if (response.data.status == "success")
											 $('.modal-backdrop').remove();
											$state.go('couponTemplateMaster');
										
									});
						}
						
						/*First Level*/
				    	$scope.firstTabLevel = 1 ;
						
						$scope.setFirstTabLevel= function(newTab)
						{
							$scope.firstTabLevel = newTab;
							if(newTab == 2)
								{
								}
						};
						
						$scope.backB = function(){
							  $state.go('couponTemplateMaster');
						}
						
						$scope.isSetFirstTabLevel = function(tabNum)
						{
							return $scope.firstTabLevel === tabNum;
						};
						  
						  function cartesian() {
							    var r = [], arg = arguments, max = arg.length-1;
							    function helper(arr, i) {
							        for (var j=0, l=arg[i].length; j<l; j++) {
							            var a = arr.slice(0); // clone arr
							            a.push(arg[i][j]);
							            if (i==max)
							                r.push(a);
							            else
							                helper(a, i+1);
							        }
							    }
							    helper([], 0);
							    return r;
							}
						  
						  $scope.checkAmountAndPer = function(flag){
							  
							  if(flag == 1){
								  
								  if($scope.percentage >100){
									  growl.warning("Percentage should not be greater than 100 %",{
											title : "WARNING"
										});
									  $scope.percentage = 0;
								  }
								  
								  $scope.amount = 0;
							  }else{
								  $scope.percentage = 0;
							  }
							  
						  }
						  
						  
							 $scope.searchServices = function(searchKeyword){
									$scope.servicesList=[];
									var serData = {
											organizationId : $scope.organizationId,
											unitId : $scope.unitId,
											searchKeyword : searchKeyword
									}
									var URI = BASE_URL + ROOT_URL + "/global/serviceMaster";
									return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
										if(response.data.status == "success"){
											$scope.servicesList = response.data.listObject;
										}
										return $scope.servicesList;
									});
								}
							 $scope.addServiceToTempServiceList1 = function($item, $model, $label){
								 $scope.serviceId = $item.serviceMasterId;
								 
							 }
						  
			}]);
							