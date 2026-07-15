angular
		.module('myApp')
		.controller(
				'webAppointmentBookingConfirmationController',
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
						'PagerService','$location','$stateParams',
						function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService,$location,$stateParams) {

							
							$scope.webAppConfDetObj = $stateParams.webAppConfirmationObj;
							 var isEmpty=angular.equals('{}',$scope.webAppConfDetObj)
	                         if(isEmpty==true){
								 var orgUnitObj = JSON.parse(sessionStorage.getItem('webAppOrgUnitCookieObj'));
	   							   if(orgUnitObj != undefined){
	   								$state.go('/webAppointmentBooking/'+(parseInt(orgUnitObj.orgId))+"/"+(parseInt(orgUnitObj.unitId)));
	   							  }	
							}
							$scope.webAppConfDetObj.scheduledDate=moment($scope.webAppConfDetObj.appointmentDate).format("DD MMM YYYY");
							var formatedCurrDate=moment(new Date()).format("YYYY/MM/DD");
							var fromTime=$scope.webAppConfDetObj.fromTime;
							var toTime=$scope.webAppConfDetObj.toTime;
							
							
							var date1 = new Date(formatedCurrDate+' '+fromTime);
							var date2 = new Date(formatedCurrDate+' '+toTime);
							var milliseconds = date2-date1;
							var minutes = milliseconds /(1000*60);
							
						    $scope.webAppConfDetObj.visitDuration=minutes;
						    var visit="";	
						    var visitTypeId=parseInt($scope.webAppConfDetObj.visitTypeId);
						    var isTeleConsultApp=$scope.webAppConfDetObj.isVideoApp;
							if(visitTypeId==1 && isTeleConsultApp==0){
								visit="Consultation (In Person)";
							}else if(visitTypeId==2 && isTeleConsultApp==0){
								visit="Follow Up (In Person)";
							}else if(visitTypeId==2 && isTeleConsultApp==1){
								visit="Consultation (Video)";
							}else{
								visit="Follow Up (Video)";
							}
							 $scope.webAppConfDetObj.patientVisit=visit;
						
							  function isEmpty(obj) {
							  	    for(var key in obj) {
							  	        if(obj.hasOwnProperty(key))
							  	            return true;
							  	    }
							  	    return false;
							  	}
							  
						} ]);

