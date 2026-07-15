'use strict';

angular
    .module('myApp')
    .controller(
        'sidebarController',
        ['$scope', '$rootScope', '$state', '$http', '$cookies', 'GenericService', '$sessionStorage','$timeout','$document','$localStorage', '$location','BillingGenericService','$window',
            function ($scope, $rootScope, $state, $http, $cookies, GenericService, $sessionStorage, $timeout,$document,permissions, $localStorage ,$location,BillingGenericService,$window) {
        	
        		
        	var cookieObject,cookieObjectAppConfig;
			[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
			
			var labConfig  =    $scope.checkLabConfig();
			$rootScope.isKioskForMenu = cookieObjectAppConfig.isKiosk;
			$rootScope.kioskLandingScreen = cookieObjectAppConfig.kioskLandingScreen;
			$rootScope.isAppointmentRequiredForMenu = cookieObjectAppConfig.isAppointmentRequired;
			$rootScope.isNursingAssessment = cookieObjectAppConfig.isNursingAssessment;
			$rootScope.isPACSRequired = cookieObjectAppConfig.isPACSRequired;
			$rootScope.refreshPageTime = cookieObjectAppConfig.refreshPageTime;
			$rootScope.isSuperUser = cookieObject.isSuperUser !=null ? cookieObject.isSuperUser : 'N' ;
			$rootScope.skipReadyForBilling = cookieObjectAppConfig.skipReadyForBilling !=null ? cookieObjectAppConfig.skipReadyForBilling : 'N' ;
			$rootScope.optimizeTransferFlow = cookieObjectAppConfig.optimizeTransferFlow !=null ? cookieObjectAppConfig.optimizeTransferFlow : 'N' ;
			$rootScope.skipHousekeeping = cookieObjectAppConfig.skipHousekeeping !=null ? cookieObjectAppConfig.skipHousekeeping : 'N' ;
			$rootScope.skipRISReportEntry= cookieObjectAppConfig.skipRISReportEntry!=null ?  cookieObjectAppConfig.skipRISReportEntry : 'N';
			$scope.nursingStationId  = JSON.parse(sessionStorage.getItem('nursingStationId'));
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.doctorId = cookieObject.doctorId;
			$rootScope.isSendToCrApplicable= labConfig.isSendToCrApplicable;
          	$rootScope.isSampleAcceptanceApplicable= labConfig.isSampleAcceptanceApplicable;
          	$rootScope.isAcceptanceInDepartment= labConfig.isAcceptanceInDepartment;
          	$rootScope.isWorklistDepartment= labConfig.isWorklistDepartment;
          	$rootScope.isReportRecall= labConfig.isReportRecall;
          	$rootScope.isValidationAndRelease= labConfig.isValidationAndRelease;
          	$rootScope.consolidatedResultEntry= labConfig.consolidatedResultEntry;
          	 $rootScope.skipValidation= labConfig.skipValidation;
         	$rootScope.incubationMasterApplicable = labConfig.incubationMasterApplicable;
           	$rootScope.macroscopicExaminationApplicable = labConfig.macroscopicExaminationApplicable;
           	$rootScope.biochemicalTestingApplicable = labConfig.biochemicalTestingApplicable;
           	$rootScope.microscopicExaminationApplicable = labConfig.microscopicExaminationApplicable;
        	var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
        	
			 if($rootScope.isKioskForMenu == undefined || $rootScope.isKioskForMenu == null)
				{
				  $rootScope.isKioskForMenu = "N";
				}
			else
				{
				  $rootScope.isKioskForMenu = $rootScope.isKioskForMenu;
				}
			 
			 
			 if($rootScope.isAppointmentRequiredForMenu  == undefined || $rootScope.isAppointmentRequiredForMenu == null)
				{
				  $rootScope.isAppointmentRequiredForMenu = "N";
				}
			else
				{
				  $rootScope.isAppointmentRequiredForMenu = $rootScope.isAppointmentRequiredForMenu;
				}
			 
			 	
			 
                $rootScope.$on("checkAccess", function($event,accessId){
                    $scope.checkAccess(accessId);
                 });

                var permissions = JSON.parse(localStorage.getItem('permissions'));
                
                $rootScope.actionListPopup=[];
                

			       
			      
        		//start for nursing
                if($scope.nursingStationId == undefined){
                	
                	var URI = BASE_URL+ROOT_URL+"/api/global/getDetailedNursingStationList/"+$scope.userId+"/"+$scope.organizationId+"/"+$scope.unitId;
  				  GenericService.serviceAction("GET",URI,{}).then(function(response){
  					  if (response.data.status == "success") {
  						  $scope.departmentNursingStationMapperDtoslist = [];
  						  
  							$scope.departmentNursingStationMapperDtoslist = response.data.listObject;
  							if($scope.departmentNursingStationMapperDtoslist.length > 0)
  								{
  									if($scope.departmentNursingStationMapperDtoslist.length > 1){
  										
  									}
  									else
  										{
  										var URI = BASE_URL+ROOT_URL+"/api/global/getDeptListByNursingStationId/"+$scope.departmentNursingStationMapperDtoslist[0].nursingStationId+"/"+$scope.organizationId+"/"+$scope.unitId;
  										  GenericService.serviceAction("GET",URI,{}).then(function(response){
  											  if (response.data.status == "success") {
  												  $scope.departmentNursingStationListC = {};
  												  $scope.departmentNursingStationListC.departmentNursingStationlist = response.data.listObject;
  												  
  												  sessionStorage.setItem('specialtyNursingStationCookies', JSON.stringify($scope.departmentNursingStationListC));
  												  sessionStorage.setItem('specialtyNursingStationCookiesNur', JSON.stringify($scope.departmentNursingStationListC));
  												  sessionStorage.setItem('nursingStationsWardsList', JSON.stringify(response.data.object));
  												  sessionStorage.setItem('nursingStationId', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationId));
  												  sessionStorage.setItem('nursingStationDesc', JSON.stringify($scope.departmentNursingStationMapperDtoslist[0].nursingStationDesc));
  												  $scope.nursingStationId = $scope.departmentNursingStationMapperDtoslist[0].nursingStationId;
  											  }
  										 });
  										}
  								}
  						}
  				 });
                }
    				
	        
	    
                
                var isActionExists;
				function checkActionExistsOrNot(arr, val) {
					return arr
							.some(function(arr) {
								if(val.id == parseInt(arr.id))
									{
										return true;
									}
								else
									{
										return false;
									}
							});
				}
				
				
				 var isActionInListExists;
					function checkActionInListExistsOrNot(arr, val) {
						return arr
								.some(function(arr) {
									if(val.id == parseInt(arr.actionId))
										{
											return true;
										}
									else
										{
											return false;
										}
								});
					}
					
				
				
                if (permissions != undefined) {
                angular.forEach(permissions.modules, function (value, key) {
                	
                	if(value.actionLink != '' && value.actionLink != null ){
                		
                		var temp = {id:value.id};
                         if(temp.id == 703){
                            if($rootScope.isKioskForMenu == 'Y'){
                                isActionExists=checkActionExistsOrNot($rootScope.actionListPopup,temp)
                                 if (!isActionExists) {
                                    $rootScope.actionListPopup.push(value);
                                }
                            }
                        }
                        else if(temp.id == 4 || temp.id == 865){
                            if($rootScope.isAppointmentRequiredForMenu == 'Y'){
                                isActionExists=checkActionExistsOrNot($rootScope.actionListPopup,temp)
                                 if (!isActionExists) {
                                    $rootScope.actionListPopup.push(value);
                                }
                            }
                        }
                        else if(temp.id == 702){
                            if($rootScope.isNursingAssessment == 'Y'){
                                isActionExists=checkActionExistsOrNot($rootScope.actionListPopup,temp)
                                 if (!isActionExists) {
                                    $rootScope.actionListPopup.push(value);
                                }
                            }
                        }
                        else if(temp.id == 74){
                            if($rootScope.skipReadyForBilling == 'N'){
                                isActionExists=checkActionExistsOrNot($rootScope.actionListPopup,temp)
                                 if (!isActionExists) {
                                    $rootScope.actionListPopup.push(value);
                                }
                            }
                        }
                        else if(temp.id == 334){
                            if($rootScope.skipReadyForBilling == 'N'){
                                isActionExists=checkActionExistsOrNot($rootScope.actionListPopup,temp)
                                 if (!isActionExists) {
                                    $rootScope.actionListPopup.push(value);
                                }
                            }
                        }
                        else{
                                isActionExists=checkActionExistsOrNot($rootScope.actionListPopup,temp)
                                if (!isActionExists) {
                                    $rootScope.actionListPopup.push(value);
                                }
                        }
                	}

                });
                }


                $rootScope.userFavoriteActionList = [];
                $rootScope.getUserFavoriteAction = function(){
                	
                	var URI = BASE_URL + ROOT_URL + "/api/global/UserActionFavourites/get/"+$scope.unitId+"/"+$scope.userId;
                	
                	GenericService.serviceAction("GET", URI, {}).then(function(response){
						// console.log(response)
						if(response.data.status == 'success')
						{
							$rootScope.userFavoriteActionList = response.data.listObject;
							
						}else
						{
							
						}
						
					});
					
                }
                $rootScope.getUserFavoriteAction();
                

                $scope.checkAccess = function (id)
				{
						if (permissions == undefined) {
							return false;
						}
						var arr = permissions.modules.filter(function (item)
						{
							return item.id == id && item.moduleType == 1;
						});
						return (arr.length > 0) ? true : false;

                }

                $rootScope.checkAccessT = function (id)
				{
                	//console.log("testtt");
						if (permissions == undefined) {
							return false;
						}
						var arr = permissions.modules.filter(function (item)
						{
							return item.id == id && item.moduleType == 1;
						});
						return (arr.length > 0) ? true : false;

                }

                $scope.getOtimsUrl = function () {
                    var url = OTIMS_URL + sessionStorage.token;
                    return url;
                }

                $scope.checkCssdStore = function(){
                	if(JSON.parse(sessionStorage.getItem('selectedCssdStore')) == undefined){
                		$rootScope.cssdStorePopup();
                		angular.element('#cssdStoreSelectionPopup').modal('show');
                	}
                }
                
                
                $rootScope.addActionList = function($item, $model, $label){
                	
                	$rootScope.redirectPopupObject = {};
                	$rootScope.redirectPopupObject = angular.copy($item);
                	 console.log($item);
                }
                
                $rootScope.addActionListMenu = function($item, $model, $label){
                	var link =$item.actionLink.substr(2);
                	var finalLink = link.replace(/[^a-zA-Z ]/g, ".");
                	
                	
                	$state.go(finalLink);
                	
                }
                
                
               $rootScope.redirectPageFromfavorite = function(){
            	   
            	   
            	   if (angular.equals({},
            			   $rootScope.redirectPopupObject)) {
            		}else{
            			angular.element('#favoriteModal').modal('hide');
                    	var link = $rootScope.redirectPopupObject.actionLink.substr(2);
                    	var finalLink = link.replace(/[^a-zA-Z ]/g, ".");
                    	$state.go(finalLink);
            		}
            	   
                }
               
               
               $rootScope.redirectPageFromfavoriteFromList = function(actionLink){
            	   angular.element('#favoriteModal').modal('hide');
                	var link = actionLink.substr(2);
                	
                	var finalLink = link.replace(/[^a-zA-Z ]/g, ".");
                	$state.go(finalLink);
                }
                
               $rootScope.addTofavorite = function(){
            	   
            	   
            	   if (angular.equals({},
            			   $rootScope.redirectPopupObject)) {
            		}else{
            	   var link = $rootScope.redirectPopupObject.actionLink.substr(2);
                   var actionId = $rootScope.redirectPopupObject.id;
                   
                   
                   var temp = {id:actionId};
           		isActionInListExists=checkActionInListExistsOrNot($rootScope.userFavoriteActionList,temp)
					if (!isActionInListExists) {
		              
		                   var data = {
		                		   userId : $scope.userId,
		                		   unitId:$scope.unitId,
		                		   organizationId : $scope.organizationId,
		                		   actionId : actionId,
		                   }
		                   $rootScope.startLoaderPopup();
		                   var URI = BASE_URL + ROOT_URL + "/api/global/UserActionFavourites/save";
		                   GenericService.serviceAction("POST", URI, data).then(function(response){
								// console.log(response)
								if(response.data.status == 'success')
								{
									$rootScope.getUserFavoriteAction();
									$rootScope.stopLoaderPopup();
					         
								}else
								{
									
								}
								
							});
					}
               }
               }
               
               
               
               $rootScope.removeFromFavorite = function(item,index){
            	   
                   
                   var URI = BASE_URL + ROOT_URL + "/api/global/UserActionFavourites/remove/"+item.id;
                   GenericService.serviceAction("GET", URI, {}).then(function(response){
						// console.log(response)
						if(response.data.status == 'success')
						{
							//$scope.userFavoriteActionList.splice(index, 1);
							$rootScope.getUserFavoriteAction();
						}else
						{
							
						}
						
					});
               }
  
  
             // user hit url from new tab then we get storage length as 0 START
            // Check if the session is valid
            // Check if the session is valid, if not, log the user out
               
               $scope.checkDuplicateTab = cookieObjectAppConfigSecond.duplicateTab;
               
               if (sessionStorage.length === 0) {
                   $state.go('logout');
                   return;
               }

               // If it's the first tab that opened
               if (!localStorage.getItem('appOpen')) {
                   // Mark that the app is open in localStorage
                   localStorage.setItem('appOpen', 'true');
                   console.log('This is the first tab.');
               } else {
                   // If 'appOpen' is already set, this tab is a duplicate, so close it
                 //  alert('This tab is a duplicate and will be closed.');
            	   
            	   if( $scope.checkDuplicateTab == false){
            		   $state.go("duplicate-tab");
            	   }
            	  
            	   
            	  
               }

               // Listen for changes in localStorage (across tabs)
               window.addEventListener('storage', function (e) {
                   // When 'appOpen' is set or changed, check if another tab is open
                   if (e.key === 'appOpen' && e.newValue === 'true') {
                       //alert('This tab is a duplicate and will be closed.');
                	   if( $scope.checkDuplicateTab == false){
                		   $state.go("duplicate-tab");
                	   }
                	  
                	  
                   }
               }, false);

               // Clean up when this tab is closed to prevent other tabs from being confused
               window.addEventListener('beforeunload', function() {
                   localStorage.removeItem('appOpen');
                   console.log('Tab closed, appOpen flag removed.');
               });

            	   // user hit url from new tab then we get storage length as 0 END
               /* $scope.isActive = function (destination){
                	
                	return destination === $location.path();
                }*/
                
                 //Start foR teST
				 console.log('starting run');
				    // Timeout timer value
				    var TimeOutTimerValue = $rootScope.refreshPageTime;
				    // Start a timeout
				    var TimeOut_Thread = $timeout(function(){ LogoutByTimer() } , TimeOutTimerValue);
				    var bodyElement = angular.element($document);
				    angular.forEach(['keydown', 'keyup', 'click', 'mousemove', 'DOMMouseScroll', 'mousewheel', 'mousedown', 'touchstart', 'touchmove', 'scroll', 'focus'],
				    function(EventName) {
				         bodyElement.bind(EventName, function (e) { TimeOut_Resetter(e) });
				    });
				    function LogoutByTimer(){
//				    	console.log("fff");
				    	if($rootScope.refreshPageTime != null && $rootScope.refreshPageTime != '' && $rootScope.refreshPageTime != undefined && $rootScope.refreshPageTime > 0){
				    		$state.go('logout');
				    		/*alert("gsdjfsdf");
				    		document.location.reload(true);*/
				    	}
				    }
				    function TimeOut_Resetter(e){
//				        console.log(' ' + e);
				        /// Stop the pending timeout
				        $timeout.cancel(TimeOut_Thread);
				        /// Reset the timeout
				        TimeOut_Thread = $timeout(function(){ LogoutByTimer() } , TimeOutTimerValue);
				    }
				    //eND fOR tEST
				    
                
            }]);