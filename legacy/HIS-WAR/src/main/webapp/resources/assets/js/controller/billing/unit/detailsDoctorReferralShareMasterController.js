'use strict';

/**
 * @Author By  Vivek Satle
 * @name myApp.controller:countryMasterController
 * @description #countryMasterController Controller of the myApp
 */
angular
		.module('myApp')
		.controller(
				'detailsDoctorReferralShareMasterController',
				[
						'$scope',
						'$http',
						'$localStorage',
						'$sessionStorage',
						'$cookies',
						'$rootScope',
						'GenericService',
						'growl',
						'$state',
						'PagerService',
						'$stateParams',
						'BillingGenericService',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$stateParams,BillingGenericService) {

							/* init() function for form object create. */

							$scope.init = function() {

								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								/*if($stateParams.doctorReferralObj!=null && $stateParams.doctorReferralObj!=undefined){
									$state.go('doctorReferralShareMaster');
									return;
								}*/
								$rootScope.loginpage = true;
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;

								
							}
							console.log("Doctor Object=",$stateParams.doctorReferralObj);
							$scope.init();
							
							if($stateParams.doctorReferralObj !== null || $stateParams.doctorReferralObj != undefined)
							{
							$scope.common = $stateParams.doctorReferralObj;
							console.log("DoctorGlobalShareObj:",$scope.common);
							
							}
						
						
						
						$scope.doctorShareGroupList=[];
						$scope.doctorShareServiceList=[];
						$scope.getDoctorShareDetailList=function(){
							try 
							{
							
								 var data={
										 referralId:$scope.common.referralId,
										 orgId:$scope.organizationId,
									     unitId:$scope.unitId
								 };
								 if(data.referralId > 0){
										$rootScope.startLoader();
								 var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getIndividualDoctorReferralDetailsByReferralId";
								  BillingGenericService.serviceAction("POST", URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											//var list=response.data.object.listTIndividualDocShareGroupWiseDto;
											//if(list.length>0 && list!=undefined &&  list!=null)
											$scope.doctorShareGroupList=response.data.object.listTIndividualDocShareGroupWiseDto;
											//else	
											$scope.doctorShareServiceList=response.data.object.listTIndividualDocShareServiceWiseDto;

											//console.log("Group List:",$scope.doctorShareGroupList);
											//console.log("service List:",$scope.doctorShareServiceList);
											$rootScope.stopLoader();
										});
								 }
								  
							} catch (e) {
								console.log(e.message)
							}
						}
						$scope.getDoctorShareDetailList();
						
							/*$scope.getDoctorReferralDetailsByRefDocId=function(){
								if(!(angular.equals({}, $stateParams.doctorReferralObj)) && $stateParams.doctorReferralObj!=null && $stateParams.doctorReferralObj!=undefined){
									var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/getDoctorReferralShareDetailsByRefDocId";
									var obj=
									      { 
											orgId:$scope.organizationId,
											unitId:$scope.unitId,
											docReferralId:$stateParams.doctorReferralObj
									      };
										$rootScope.startLoader();
										BillingGenericService.serviceAction(METHOD_POST, URI, obj).then(function(response) {
												$scope.referralConfigurationDetailsList = [];
												if (response.data.status == "success") {
													$scope.referralConfigurationDetailsList = response.data.listObject;
												}
												$rootScope.stopLoader();
										});
								}else{
//									$state.go('doctorReferralShareMaster');
//									return;
								}
							}
							$scope.getDoctorReferralDetailsByRefDocId();
							$scope.updateStatus=function(id,status){
								
								var param = {
											status:status,
											doctorReferalShareMapperId:id,
											updatedBy:$scope.userId,
											updatedDate: moment(new Date()).format('DD-MM-YYYY HH:mm:ss')
										}

								$rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL + "/unit/DoctorShare/updateStatusDoctorReferralShare";
								BillingGenericService.serviceAction(METHOD_POST,URI, param,NOTIFICATION_MSG_STATUS_TRUE).then(function(response) {
									if (response.data.status == "success")
										growl.success("Updated successfully",{
											title : "success"
										});
									$rootScope.stopLoader();
								});
								$rootScope.stopLoader();
							}*/
							
						} ]);
