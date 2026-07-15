angular
		.module('myApp')
		.controller(
				'webAppController',
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
						'PagerService','$location',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$location) {

							$scope.organizationId = $state.params.orgId;
							$scope.unitId = $state.params.unitId;
							$scope.userId = 1;
							$scope.unitList=[];
							$scope.specialityMasterList=[];
							$scope.selectedUnitId=0;
							$scope.doctorList=[];
							$scope.isHideUnitList=false;
							$scope.loadingMsg="Loading...";
							
							 var cookieObj={
									 "orgId":$scope.organizationId,
									 "unitId":$scope.unitId
							 }
							 sessionStorage.setItem('webAppOrgUnitCookieObj', JSON.stringify(cookieObj));
							 
							$scope.getSpecialityMasterList = function(){
								try{
									var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
									var obj = {
												organizationId : $scope.organizationId,
												unitId         :$scope.unitId
										    }
									GenericService.serviceAction("POST", URI,obj).then(function(response) {
										if(response.data.status == "success"){
											$scope.specialityMasterList = response.data.listObject;
										}
									});
								}catch (e) {
									console.log("Exception",e.message);
								}
							}
							
							//$scope.getSpecialityMasterList();
							
							$scope.getUnitListByOrgId = function () {
								try{
								var URI = BASE_URL + ROOT_URL+ "/unit/org/"+$scope.organizationId;
								GenericService.serviceAction("GET", URI, {}).then(function(response) 
									{
								    if (response.data.status == "success") {
												$scope.stopLoader();
												$scope.unitList = response.data.listObject;
												if($scope.unitId==0){
												  $scope.selectedUnitId = $scope.unitList[0].unitId;
												}else{
													$scope.isHideUnitList=true;
													$scope.selectedUnitId=$scope.unitId;
												}
									}
								    $scope.unitId=$scope.selectedUnitId;
								    $scope.getSpecialityMasterList();
								    var reqObjForDocList = {
											organizationId:$scope.organizationId,
											unitId:$scope.selectedUnitId,
											flag:true,//$("#allDoctors").prop("checked"),
											specialityId:1
									}
								    $scope.loadingMsg="Loading...";
									var URI = BASE_URL + ROOT_URL+ "/api/global/webappointment/getDoctors";
									GenericService.serviceAction("POST", URI, reqObjForDocList).then(function(response) 
										{
									    if (response.data.status == "success") {
													$scope.stopLoader();
													$scope.doctorList = response.data.listObject;
										}
									    if($scope.doctorList.length==0){
											$scope.loadingMsg="No Doctors Available";
										}
									});
								});
							}catch (e) {
								console.log("Exception",e.message);
							}
						}
							
						$scope.getUnitListByOrgId();

						$scope.getDoctorsList=function(){
							try{
								$scope.doctorList=[];
								$scope.loadingMsg="Loading...";
								var reqObjForDocList = {
										organizationId:$scope.organizationId,
										unitId:$scope.selectedUnitId,
										flag:$("#allDoctors").prop("checked"),
										specialityId:$('input[name="specialities"]:checked').val()
								}
								var URI = BASE_URL + ROOT_URL+ "/api/global/webappointment/getDoctors";
								GenericService.serviceAction("POST", URI, reqObjForDocList).then(function(response) 
									{
								    if (response.data.status == "success") {
												$scope.stopLoader();
												$scope.doctorList = response.data.listObject;
												if($scope.doctorList.length==0){
													$scope.loadingMsg="No Doctors Available";
												}
									}
								});
							}catch (e) {
								console.log("Exception",e.message);
							}
						}
						$scope.getChangedUnitData=function(){
							$("#allDoctors").prop("checked","true");
							$scope.unitId=$scope.selectedUnitId;
							$scope.getSpecialityMasterList();
							$scope.getDoctorsList();
						}
						
						
						$scope.gotoBookAppointmentPage = function(obj){
							$scope.detailObj={};
							$scope.detailObj.organizationId=$scope.organizationId;
							$scope.detailObj.unitId=$scope.unitId;
							$scope.detailObj.doctorId=obj.doctorId;
							$scope.detailObj.specialityId=obj.specialityId;
							$scope.detailObj.userId=obj.userId;
							$scope.detailObj.doctorName=obj.firstName+" "+obj.lastName;
							$scope.detailObj.specialityName=obj.specialityName;
							$scope.detailObj.doctorImage=obj.doctorImage;
							$state.go('bookAppointment',
							{
								webAppDetailObj: $scope.detailObj
							});
						}
						
						} ]);
