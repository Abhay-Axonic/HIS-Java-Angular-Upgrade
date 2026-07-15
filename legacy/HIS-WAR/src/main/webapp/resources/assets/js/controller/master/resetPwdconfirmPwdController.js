(function () {
  "use strict";
						function resetPwdconfirmPwdController($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService, growl,
								$state, PagerService) {


							$scope.init = function() {
								
								
								var cookieObject = $cookies.getObject('cookieObject');
								if(cookieObject == undefined){
									$state.go('login');
									return;
								}
								
								$scope.unitId = cookieObject.unitId;
								$scope.organizationId = cookieObject.organizationId;
								$scope.userId = cookieObject.userId;
								
								$rootScope.loginpage = true;
								
							}

							$scope.init();
							
							$scope.existingPassword = "";
							$scope.password = "";
							$scope.confirmPassword = "";
							
							  $scope.validatePassword = function(password) {
							      var minLength = 8;
							      var maxLength = 16;
							      var hasUppercase = /[A-Z]/.test(password);
							      var hasLowercase = /[a-z]/.test(password);
							      var hasDigit = /[0-9]/.test(password);
							      var hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);
							      var isLengthValid = password.length >= minLength && password.length <= maxLength;

							      return hasUppercase && hasLowercase && hasDigit && hasSpecialChar && isLengthValid;
							    };
							
							
							
							$scope.resetPwd = function () 
							{
							
								
								 if($scope.existingPassword == undefined ||$scope.existingPassword == null ||$scope.existingPassword == "" ){
								    	
								    	growl.warning("Please Enter Existing Password  .",{
											title : ERROR_MSG
										});
								    	
								    	return false;
								    	
								    }
								 if($scope.password == undefined ||$scope.password == null ||$scope.password == "" ){
								    	
								    	growl.warning("Please Enter New Password  .",{
											title : ERROR_MSG
										});
								    	
								    	return false;
								    	
								    }
								
								 if($scope.confirmPassword == undefined ||$scope.confirmPassword == null ||$scope.confirmPassword == "" ){
								    	
								    	growl.warning("Please Enter Confirm Password  .",{
											title : ERROR_MSG
										});
								    	
								    	return false;
								    	
								    }
								 
								 if($scope.existingPassword == $scope.password){
								    	
								    	growl.warning("Existing Password and new password should be different .",{
											title : ERROR_MSG
										});
								    	
								    	return false;
								    	
								    }
								    
								    if($scope.confirmPassword != $scope.password){
								    	
								    	growl.warning("Confirm password and new password must be match .",{
											title : ERROR_MSG
										});
								    	
								    	return false;
								    	
								    }
								 
								    $scope.isPasswordValid = $scope.validatePassword($scope.password);
								    
								    if(!$scope.isPasswordValid){
								    	
								    	growl.warning("Password must be 8 to 16 characters long and include uppercase letters, lowercase letters, digits, and special characters.",{
											title : ERROR_MSG
										});
								    	
								    	return false;
								    	
								    }
								    
								    
								    $rootScope.startLoader();
								    
									var URI = BASE_URL + ROOT_URL + "/adt/resetPassword";
									var userObj = $cookies.getObject('cookieObject');
									var data = {
											
											userId:userObj.userId,
											existingPassword:$scope.existingPassword,
											userPwd:$scope.password,
										}
							
									GenericService.serviceAction("POST", URI, data).then(function (response) {
										
										if (response.data.status == "success") 
										{
											growl.success("Password Updated Successfully.",{
												title : SUCCESS_MSG
											});
//											this.$broadcast('show-errors-reset');
											
											$scope.existingPassword = "";
											$scope.password = "";
											$scope.confirmPassword = "";
											$rootScope.stopLoader();
										}
										else if (response.data.status == "error") {
											growl.warning(response.data.message,{
												title : ERROR_MSG
											});
											$rootScope.stopLoader();
										}
									});
								
							}

						} 
						 function config($stateProvider) {
					    $stateProvider.state('resetPwdconfirmPwd', {
					        url: '/resetPwdconfirmPwd',
					        templateUrl: 'views/reset-password-confirm-pwd.html',
					        controller:'resetPwdconfirmPwdController',
					        title: 'Reset Password'
					    })
					  }

  angular
    .module("myApp")
    .config(config)
    .controller("resetPwdconfirmPwdController", resetPwdconfirmPwdController);
})();	
