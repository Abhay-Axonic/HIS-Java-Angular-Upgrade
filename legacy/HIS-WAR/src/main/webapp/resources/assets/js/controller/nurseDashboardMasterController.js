'use strict';
/**
 * @Author Sneha Dalave
 * @Author Chetna Dhokne 
 * @name myApp.controller:dashboardInfoMasterController
 * @description #dashboardInfoMasterController Controller of the myApp
 */
angular
    .module('myApp')
    .controller(
        'nurseDashboardMasterController',
        ['$scope', '$rootScope', '$state', '$http', '$cookies', 'growl','GenericService', '$sessionStorage', '$localStorage', '$location','$sce','$window',
            function ($scope, $rootScope, $state, $http, $cookies, growl,GenericService, $sessionStorage,  $localStorage ,$location,$sce,$window) {
        	
        		
			
			$scope.init=function(){
				
				var cookieObject = $cookies.getObject('cookieObject');
				if(cookieObject == undefined){
					$state.go('login');
					return;
				}
				$scope.unitId = cookieObject.unitId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				$scope.doctorIdForBedList  = cookieObject.doctorId;
				
				var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
				 if(cookieObjectAppConfig == undefined){
				  $state.go('login');
				  return;
				 }
				 else
				  {
				   //console.log('cookieObjectAppConfig',cookieObjectAppConfig);
				   
				  }
				 
			$scope.roleName = cookieObjectAppConfig.roleName;
			$scope.nurseRole = cookieObjectAppConfig.nurseRole;	 
			$scope.doctorRole = cookieObjectAppConfig.doctorRole;
			$scope.isShowPatientByBed=cookieObjectAppConfig.isShowPatientByBed;
			$scope.patientSearchDiv=true;
			$rootScope.linkHideShow=true;
			

			$rootScope.loginpage = true;

			// for popup
			$scope.showModal = false;
			$scope.buttonClicked = "";
			$scope.popUpFlag = true;
			// end for popup
			
			var cookieObjectStore = JSON.parse(sessionStorage.getItem('selectedTopStore'));
			if(cookieObjectStore==undefined){
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
				$scope.counterMstObj = JSON.parse(sessionStorage.getItem('counterMstObj'));
				if($scope.counterMstObj == undefined || $scope.counterMstObj == null || $scope.counterMstObj.counterId == undefined){
					growl.warning('Please select counter..!!');
					return false;
				}else if($scope.counterMstObj.counterId > 0){
					$scope.counterId = $scope.counterMstObj.counterId!=""?$scope.counterMstObj.counterId:"";
					angular.element('#counterDetailsModal').modal('hide');
				}
				
			 }
			/**================================COUNTER CODE END======================================*/
			$scope.selectsetNursingStation = function()
			{
			
				if($scope.departmentNursing == undefined || $scope.departmentNursing.length == 0){
					growl.warning('Please select nursing station..!!');
					return false;
				}else{
				var data = JSON.parse($scope.departmentNursing);
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
			}else{
				$scope.wardListForGetBedList = nursingStationsWardsList;
			}
			
			
			
			}
			$scope.init();
			
			
			//get bed list mapped to nurse
			var URI =  BASE_URL + ROOT_URL+"/api/global/getbedlistbyNurseId";
			var data={
					orgId:$scope.organizationId,
					unitId:$scope.unitId,
					nurseId:$scope.userId,
					shiftDate:moment(new Date()).format('DD-MM-YYYY')
			}
			GenericService
					.serviceAction("POST", URI, data)
					.then(
							function(response) {
								$scope.mappedNurseBedList = [];

								if (response.data.status == "success") {
									$scope.mappedNurseBedList = response.data.listObject;
									var i;
									var bedList=[];
									for(i=0;i<$scope.mappedNurseBedList.length;i++){
										bedList.push($scope.mappedNurseBedList[i].bedId);
									}
									 sessionStorage.setItem('mappedNurseBedList', JSON.stringify(bedList));
									//console.log("mappedNurseBedList:",$scope.mappedNurseBedList);
								}
							});	
			
			 $rootScope.$on("checkAccess", function($event,accessId){
                 $scope.checkAccess(accessId);
              });

             var permissions = JSON.parse(localStorage.getItem('permissions'));
             $scope.checkAccess = function (id) {
                 if (permissions == undefined) {
                     return false;
                 }
                 var arr = permissions.modules.filter(function (item) {
                     return item.id == id && item.moduleType == 1;
                 });
                 return arr.length > 0 ? true : false;
             }
             
			
			$scope.click= function(info)
        	{
        		$scope.data=info;
				$state.go('nurseDashboardInfo',
					{
					params : $scope.data,
					});
        	}
            $scope.showIframe = false;
        	$scope.openTriageNurse= function() {
        	            $rootScope.startLoader();
        	            $scope.TriageHeader = "Virtual Nurse";
                    	var URI =  "https://gpxucwzdeijkcnygwakq.supabase.co/functions/v1/generate-triage-session";
                        var data={
                                userId: "5c8e0c35-436d-4bd9-bd08-ed9944d2fb3c",
                                environment:"qa",
                                languageCode: "en"
                        }
                        GenericService.serviceAction("POST", URI, data) .then(
                            function(response) {
                                $rootScope.stopLoader();
                                if (response.data.success === true && response.data.webhookUrl) {
                                       $scope.iframeUrl = $sce.trustAsResourceUrl(response.data.webhookUrl);
                                       $scope.showIframe = true;
                                       angular.element('#iframForTriage').modal('show');
                                }
                        });
            }

            $scope.openTriageReceptionist= function() {
                        $rootScope.startLoader();
                        $scope.TriageHeader = "Virtual Secretary";
                        var URI =  "https://gpxucwzdeijkcnygwakq.supabase.co/functions/v1/generate-receptionist-session";
                        var data={
                                userId: "5c8e0c35-436d-4bd9-bd08-ed9944d2fb3c",
                                environment:"qa",
                                languageCode: "en",
                                workspace: "axoncare"
                        }
                        GenericService.serviceAction("POST", URI, data) .then(
                            function(response) {
                                if (response.data.success == true) {
                                 $rootScope.stopLoader();
                                 $scope.iframeUrl = $sce.trustAsResourceUrl(response.data.webhookUrl);
                                 $scope.showIframe = true;
                                 angular.element('#iframForTriage').modal('show');
                                }
                        });
            }



            $scope.closeTriage = function () {
                angular.element('#iframForTriage').modal('hide');
                $scope.showIframe = false;
                $scope.iframeUrl = null;
            };

			//end for nursing
		      
        }]);