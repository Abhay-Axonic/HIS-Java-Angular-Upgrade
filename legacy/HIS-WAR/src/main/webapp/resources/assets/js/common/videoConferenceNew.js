(function () {
  "use strict";
						function videoConferenceNew($scope, $cookies, $rootScope, GenericService,
								BillingGenericService, $state, $timeout,$interval,
								$stateParams,GenericServiceParamHeader,growl,$compile,reportService) {
							
							
							$rootScope.loginpage = true;
							
							var cookieObject = $cookies
							.getObject('cookieObject');
							if (cookieObject == undefined) {
								$state.go('login');
								return;
							}
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							if(cookieObjectAppConfig == undefined){
								$state.go('applicationConfiguration');
								return;
							}
							else
								{
									console.log('cookieObjectAppConfig',cookieObjectAppConfig);
									
								}
							
							
							 
							$scope.doctorMails = ""; 
							$scope.patientsMails = "";
							$scope.time = "";
							$scope.remark = "";
							$scope.openDatePicker = function() {
								$scope.datepicker.opened = true;
							};

							$scope.datepicker = {
									opened : false
							};
							
							$scope.openFromDatePicker = function() {
								$scope.fromDatepicker.opened = true;
								
							};

							$scope.fromDatepicker = {
									opened : false
							};
							$scope.sndMeetingInvite1234 = function() {

                                 var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
                                 var email =$scope.patientsMails;
                                	if (!filter.test(email)) {

                                         growl.error(EMAIL_MSG,{
                                					title : ERROR_MSG

                                					});

                                			return false;
                                    }

                                 var doctoremail =$scope.doctorMails;
                                 if (!filter.test(doctoremail)) {

                                  growl.error(EMAIL_MSG,{
                                                title : ERROR_MSG
                                                });

                                                return false;
                                    }

                                    if($scope.doctorMails == $scope.patientsMails){
                                                    growl.error("Email id must be deferent",{title : ERROR_MSG });
                                                                 return false;
                                                           }



								$rootScope.startLoader();
								$scope.date = moment($scope.fromDatepicker).format('DD-MM-YYYY');
								var data = {};

								var URI = BASE_URL + ROOT_URL
										+ "/unit/sendMailInvite/"+$scope.doctorMails+"/"+$scope.patientsMails +"/"+ $scope.date +"/"+$scope.time +"/"+$scope.remark;  
								 GenericService.serviceAction("POST",
										URI, data).then(function(response) {
									if (response.data.status == "success") {
									
										$scope.doctorMails = ""; 
										$scope.patientsMails = "";
										$scope.time = "";
										$scope.remark = "";
										$scope.date = new Date();
										growl.success("Mail sent successfully !!",
												{
													title : 'Success!'
												});
										$rootScope.stopLoader();
										return response.data.listObject;

									}
								});

							}
							
						}
						 function config($stateProvider) {
					    $stateProvider.state('videoConferenceNew', {
					        url: '/videoConferenceNew',
					        templateUrl: 'views/common/video-conference.html',
					        controller:'videoConferenceNew',
					        title: 'Video Conference Call'
					    })
					  }

  angular
    .module("myApp")
    .config(config)
    .controller("videoConferenceNew", videoConferenceNew);
})();	
						