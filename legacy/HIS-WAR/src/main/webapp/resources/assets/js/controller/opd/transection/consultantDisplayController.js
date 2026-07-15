(function () {
    "use strict";

    function consultantDisplayController($scope, $cookies, $rootScope, GenericServiceForWithoutLogin,
								BillingGenericService, $state, $timeout,$interval,growl,orgId,unitId,cabinId) {
    	
							$scope.init = function() {

								$scope.unitId = unitId;
								$scope.organizationId = orgId;
								$scope.cabinId = cabinId;
								$scope.tokenList = [];
								$rootScope.loginpage = false;
								$scope.consultantDescAndPatientName ="";
								$scope.qualificationDesc = "";
								$scope.obj = {};

								
							}
							
							$scope.init();

                            $scope.firstImg;
                            $scope.imageBackground = function(){
                                            var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/screen-images/"+$scope.organizationId+"/3"
                                                GenericServiceForWithoutLogin.serviceAction("GET", URI).then(function(response) {
                                                    if(response.data.object != null){
                                                $scope.firstImg=response.data.object.imageURL;
                                                    }
                                                });
                                            }
                             $scope.imageBackground();


							
							$scope.getUpdatreDoctorCabin = function(){
								

								if($scope.cabinId != undefined && $scope.cabinId != null){
									
									$scope.commonObj = {
											cabinId : $scope.cabinId,
									};
									
									var URI = BASE_URL + ROOT_URL+ "/api/global/unit/getDoctorInCabin";
									
									GenericServiceForWithoutLogin.serviceAction("POST", URI, $scope.commonObj).then(
											function(response) {
												
												if (response.data.status == "success") 
												{
													$scope.obj = response.data.object;

												}
											});
								}
								
								
							}
							

							$scope.getCabinDoctorList =function (){
								
								if($scope.cabinId != undefined && $scope.cabinId != null){
									
									
									if($scope.obj != undefined && $scope.obj.doctorId != undefined && $scope.obj.doctorId != null){
										
										
										$scope.currentDate = new Date();

										$scope.commonObj = {
												unitId : $scope.unitId,
												organizationId : $scope.organizationId,
												currentDate: moment($scope.currentDate).format("YYYY-MM-DD"),
												doctorId : $scope.obj.doctorId,
										};
										
										var URI = BASE_URL + ROOT_URL+ "/api/opd/token/getDoctorTokenList";
										
										GenericServiceForWithoutLogin.serviceAction("POST", URI, $scope.commonObj).then(
												function(response) {
													
													if (response.data.status == "success") 
													{
														
														if(response.data.listObject != null){
															var check = false;
															angular.forEach(response.data.listObject,function(value,index){
																
																if(value.doctorDeskArrival!=null && value.doctorDeskArrival == 'N' && value.doctorLatestCall == true){
																	
																	check = true;
																	$scope.consultantDescAndPatientName = value.patientName;
																	$scope.qualificationDesc = "";
																}
															}); 
															
															if(check == false){
																
																if($scope.obj != undefined && $scope.obj.doctorName != undefined && $scope.obj.doctorName != null && $scope.obj.doctorName != " "){
																	
																	$scope.consultantDescAndPatientName ="Dr. "+$scope.obj.doctorName;
																	$scope.qualificationDesc = $scope.obj.qualificationDesc;
																	
																}else{
																	if($scope.obj != undefined && $scope.obj.cabinName != undefined && $scope.obj.cabinName != null && $scope.obj.cabinName != ""){
																		
																		$scope.consultantDescAndPatientName =$scope.obj.cabinName;
																		$scope.qualificationDesc = "";
																	}
																}
																
																
															}
															
														}

													}
												});
										
									}else{
										
										if($scope.obj != undefined && $scope.obj.cabinName != undefined && $scope.obj.cabinName != null && $scope.obj.cabinName != ""){
											
											$scope.consultantDescAndPatientName =$scope.obj.cabinName;
											$scope.qualificationDesc = "";
											
										}
										
										
									}
									
									
								}
								
								
							    }
						
							
							$interval(function() {
								$scope.getUpdatreDoctorCabin();
					          }, 15000);
							
							$interval(function() {
								$scope.getCabinDoctorList();
					          }, 10000);
								
						
	} function config($stateProvider) {
        $stateProvider.state("consultantDisplay", {
            url: '/consultantDisplay/:orgId/:unitId/:cabinId',
            templateUrl: 'views/Nursing/consultant-display.html',
            controller: "consultantDisplayController",
			 title: 'Consultant Display',
			 resolve : {

				 orgId: function($stateParams, GenericService)
		            {
		                return $stateParams.orgId;
		            },
		            unitId: function($stateParams, GenericService)
		            {
		                return $stateParams.unitId;
		            },
		            cabinId: function($stateParams, GenericService)
		            {
		                return $stateParams.cabinId;
		            }
		   		 }
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("consultantDisplayController", consultantDisplayController)
})();