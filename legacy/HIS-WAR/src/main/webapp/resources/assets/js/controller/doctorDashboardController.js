'use strict';
/**
 * @Author Nitin More
 * @name myApp.controller:doctorDashboardController
 * @description #doctorDashboardController Controller of the myApp
 */
angular
.module('myApp')
.controller(
		'doctorDashboardController',
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
				'$stateParams',
				function($scope, $rootScope, $state, $http,
						GenericService,$sessionStorage,$cookies, growl, $localStorage,$stateParams) {
        	

			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$rootScope.loginpage = true;
			
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			var labConfig = JSON.parse(localStorage.getItem('labConfig'));
			 if(cookieObjectAppConfig == undefined){
			  $state.go('login');
			  return;
			 }
			 else
			  {
			   //console.log('cookieObjectAppConfig',cookieObjectAppConfig);
			   
			  }
			 
		$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
		$scope.hemodialysisSpecialityId = cookieObjectAppConfig.hemodialysisSpecialityId;
		if (labConfig != undefined) {
		$scope.labSpecialityId = labConfig.labSpecialityId;
		}
		$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
		$scope.doctorRole = cookieObjectAppConfig.doctorRole;	 
		$scope.roleName = cookieObjectAppConfig.roleName;
			
		if(!$rootScope.storeReady()){ // store-accessor campaign: dashboard family — access-check only, its OWN popup (storePopupAcesss)
			$rootScope.storePopupAcesss();
		}
		
		
			/**================================COUNTER CODE START======================================*/
			$scope.isCounterApplicable = JSON.parse(sessionStorage.getItem('isCounterApplicable'));
			$scope.isUserCounterApplicable = JSON.parse(sessionStorage.getItem('isUserCounterApplicable'));
			if($scope.isCounterApplicable=='Y' && $scope.isUserCounterApplicable=='Y')
				{
				     var flag = $scope.getCounterDetails();
				     if(flag)
				    	 {
				    	 $rootScope.counterId = $rootScope.counterMstObj.counterId!=""?$rootScope.counterMstObj.counterId:"";
				    	 	$rootScope.counterDesc = $rootScope.counterMstObj.counterDesc!=""?$rootScope.counterMstObj.counterDesc:"";
				    	 }
				     else
				     {
				    	 $scope.counterList = JSON.parse(localStorage.getItem('counterList'));
				     }
				}else{
					$rootScope.counterId = 0;
				}
			$scope.confirmCounter = function()
			 {
				/*$scope.counterMstObj = $cookies.getObject('counterMstObj');
				$scope.counterId = $scope.counterMstObj.counterId!=""?$scope.counterMstObj.counterId:"";*/
				$scope.counterMstObj = JSON.parse(sessionStorage.getItem('counterMstObj'));
				if($scope.counterMstObj == undefined || $scope.counterMstObj == null || $scope.counterMstObj.counterId == undefined){
					growl.warning('Select Counter');
					return false;
				}else if($scope.counterMstObj.counterId > 0){
					$scope.counterId = $scope.counterMstObj.counterId!=""?$scope.counterMstObj.counterId:"";
					angular.element('#counterDetailsModal').modal('hide');
				}
			 }
			/**================================COUNTER CODE END======================================*/
			
			var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
			if(nursingStationsWardsList == undefined)
			{
				//start for nursing
				var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
				  GenericService.serviceAction("GET",URI,{}).then(function(response){
					  if (response.data.status == "success") {
						  $scope.departmentNursingStationMapperDtoslist = [];
						  
							$scope.departmentNursingStationMapperDtoslist = response.data.listObject;
							//alert($scope.roleName);
							if($scope.nurseRole == 1){
							if($scope.departmentNursingStationMapperDtoslist.length > 0)
								{
									if($scope.departmentNursingStationMapperDtoslist.length > 1){
										angular.element('#nursingStationModal').modal('show');
										//$('#nursingStationModal').modal({backdrop: 'static', keyboard: false});
									}
									else
										{
										var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
										  GenericService.serviceAction("GET",URI,{}).then(function(response){
											  if (response.data.status == "success") {
												  $scope.departmentNursingStationListC = {};
												 // console.log("fff",response.data.object);
												  $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
												  
												  sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
												  sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
												  sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
												  sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
												  //$cookies.putObject('nursingStationDesc',$scope.departmentNursingStationMapperDtoslist.nursingStationDesc);
												  sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
												  $scope.wardListForGetBedList = response.data.object;
												  
											  }
										 });
										}
								}
					  		}
						}
				 });
				//end for nursing
			}
			else
			{
				$scope.wardListForGetBedList = nursingStationsWardsList;
			}
			
			
			
			$scope.departmentNursing = {};
			$scope.selectsetNursingStation = function()
			{
				if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
					growl.warning('Please select nursing station..!!');
					return false;
				}else{
				$scope.wardListForGetBedList=[];
				var data = JSON.parse($scope.departmentNursing);
				//console.log("$scope.departmentNursing",$scope.departmentNursing);
				//console.log("$scope.departmentNursing ID",data.nursingStationId);
				var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+data.nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
				  GenericService.serviceAction("GET",URI,{}).then(function(response){
					  if (response.data.status == "success") {
						  $scope.departmentNursingStationListC = {};
						  //console.log('nursingStationsWardsList',response.data.object);
						    $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
							sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
							sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
							sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
							sessionStorage.setItem('nursingStationDesc', JSON.stringify(data.nursingStationDesc));
							sessionStorage.setItem('nursingStationId', JSON.stringify(data.nursingStationId));
							$scope.wardListForGetBedList = response.data.object;
							angular.element('#nursingStationModal').modal('hide');
						}
				 });
			}
			}
                
            	 $rootScope.$on("checkAccess", function($event,accessId){
                     $scope.checkAccess(accessId);
                  });

                 var permissions = JSON.parse(localStorage.getItem('permissions'));
                 $scope.checkAccess = function (id) {
                     var arr = permissions.modules.filter(function (item) {
                         return item.id == id && item.moduleType == 1;
                     });
                     return arr.length > 0 ? true : false;
                 }
                 
                 
            	$scope.click= function(info)
            	{
            		$scope.data=info;
					$state.go('doctorDashboardInfo',
						{
						params : $scope.data,
						});
            	}
            		
            }]);