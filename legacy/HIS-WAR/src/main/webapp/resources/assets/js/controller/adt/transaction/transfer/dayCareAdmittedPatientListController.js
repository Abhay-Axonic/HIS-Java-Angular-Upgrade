angular
.module('myApp')
.controller(
		"dayCareAdmittedPatientListController",
		[
			'$scope',
			'$rootScope',
			'$state',
			'$cookies',
			'GenericService',
			'PagerService',
			'promiseFactory',
			'$sessionStorage',
			'growl',
			'BillingGenericService',
			function($scope, $rootScope, $state, $cookies,GenericService, PagerService, promiseFactory,$sessionStorage, growl, BillingGenericService) {
				var cookieObject = $cookies
				.getObject('cookieObject');
					if (cookieObject == undefined) {
						$state.go('login');
						return;
					}
					$scope.unitId = cookieObject.unitId;
					$scope.organizationId = cookieObject.organizationId;
					$scope.userId = cookieObject.userId;
				    $scope.searchKeyword="";
				    $scope.nursingWardList = [];
				    
					var nursingStationsWardsList = JSON.parse(sessionStorage.getItem('nursingStationsWardsList'));
					 if(nursingStationsWardsList == undefined){
						 growl.warning(
									'No ward mapped with selected nursing station !!',
									{
										title : 'Success!'
									});
					 }
					 else
					  {
					   console.log('nursingStationsWardsList',nursingStationsWardsList);
					   $scope.nursingWardList =nursingStationsWardsList; 
					  
					   console.log('nursingWardList',$scope.nursingWardList);
				
				var commonObjDoc = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId,
						wardList : $scope.nursingWardList,
					};
					var URI = BASE_URL + ROOT_URL+ "/adt/getListOfDayCareNursingAccepted";
					$rootScope.startLoader();
						GenericService
								.serviceAction("POST", URI, commonObjDoc)
								.then(
										function(response) {
											//$scope.listOfAdmittedPatientList = [];
										$scope.patientBedList = [];
										$rootScope.stopLoader();
										if (response.data.status == "success") {
											$scope.patientBedList = response.data.listObject;
									console.log("$scope.patientBedList",$scope.patientBedList);
								}
							});
	
	
						$scope.generateIpdCoverSheetUrl = function(patientDetailsObj)
						{
							//$rootScope.getTemplateCountByAdmissionOrEncounter(patientDetailsObj.patientId,patientDetailsObj.admissionId,null);
							if(patientDetailsObj!=null)
							  {
								patientDetailsObj.startConsultanceStartTime = moment(new Date()).format('DD-MM-YYYY HH:mm:ss'); 
								console.log("patientDetails",JSON.stringify(patientDetailsObj));
											$state.go('dayCareNursingCoversheet',
												{
												patientDetailsObj :patientDetailsObj
												});
							  }
						  else
						     {
							    growl.error("Something went Wrong.",{title : 'Error!'});
						     }
						}
						
					  }
			} ]);