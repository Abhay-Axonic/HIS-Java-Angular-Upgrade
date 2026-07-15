(function () {
    "use strict";

    function abdmController($scope,$sce, $interval ,$rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, BillingGenericService, $filter, $state, growl, CurrencyService, commonDetailService, screenDataService, reportService ,$timeout) {
        var self = this;
//alert("hello ABDM");
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            var date = new Date();
            self.model = {};
        }

        var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
        $scope.aadhar={
				"myAadharNumber":""
		};
     	
        $scope.setFirstPage = function(){
        	 $scope.createAbhass = false;
        	 $scope.createAbhadiv = true;
        }
        

        $scope.openVerificationDiv = function() {
            $scope.abhaVerificationDiv = !$scope.abhaVerificationDiv;
        };
        //started abdm code 
        $scope.init = function(){
        	 $("#mobileNumberValidaionPoupUp").hide();
			 $("#pngCardPopUP").hide();
        	 $scope.createAbhass = true;
        	 $scope.createAbhadiv = false;
        	 $scope.createOTP = false;
        	 $scope.enterMobileNumber = false;
             $scope.enterOtp = false;
             $scope.addressAbha = true;
             $scope.modesAbha = false;
             $scope.authontification = false;
             $scope.demograpgyAbha = false;
             $scope.authModeDroDown = "4";
             $scope.createAbha = '0';
             $scope.confirmOTP = '';
             $scope.gender = '0';
             $scope.dateOfBirth = '';
             $scope.mobileNumber = '';
             $scope.searchHealthid = true;
             $scope.searchOTPVerification = false;
             $scope.searchOTPAuthentication = false;
             $scope.isButtonDisabled = true;
             $scope.userName = cookieObject.userName;
             $scope.verifyAadharOrMobile = true;
             $scope.verifyAadharOrMobileSearch = true;
             $scope.verifyOtpForLoginDiv = false;
             $scope.verifyABHAaddressAndNumber = true;
             $scope.abbhaAddressVerficationofV3 = true;
             $scope.verifyOtpForLoginABHAaddressAndNumber = false;
             $scope.abbhaAddressVerficationofSearchSlide = true;
             $scope.abbhaAddressVerficationofV3AuthMode = false;
             $scope.createNewAccount = false;
             $scope.abhaVerificationDiv = false;
             $scope.updateMobiletestOTP = false;
             $scope.verifyObj = {}
             if ($scope.consents && Array.isArray($scope.consents)) {
                 // Reset the checkboxes by setting the 'checked' property to false for each consent
                 $scope.consents.forEach(function (consent) {
                   consent.checked = false;
                   if(consent.id == "checkboxLast") {
                	   consent.additionalConsent1.checked = false;
                	   consent.additionalConsent2.checked = false;
                   }
                 });
               }
             
         	$scope.obj={
					"sbxId":"",
					"sbxIdNew":""
			}
         	
         	$scope.createOTPObject={
					"otpInput":"",
					"mobileInput":"",
					"verifyotpInput":"",
					"profileDetails":""
			};
         	$scope.enterOtpObject={
					"otpInput":"",
					"mobileInput":""
			};
         	
        	$scope.authOBJECT={
					"confirmOTP":"",
					"suggestions":""
			};
        	
        	$scope.search={
					"searchByHealthOTP":""
			};
        	
        	$scope.aadhar={
					"myAadharNumber":""
			};
        	  $scope.verifyObj = {
             		 "fetchById" : "1",
             		"fetchByIdMobile" : "2",
             		 "data" : "",
             		 "dataMobile" : "",
             		 "loginOtpVerification":"",
             		 "otpInput":"",
             		 "ABHANumber":""
             			 
              }
         	 
        }
        $scope.init();
        
        $scope.setSuggestion = function(){
      	  $scope.createOTPObject.profileDetails = $scope.authOBJECT.suggestions;
  	    }
       
           $scope.showRadio = function(){
        	  $scope.createAbha = true;
    	  }
    		  $scope.openPopup = function(){
    			  $("#myModal").show();
    		  }
    		  
    		  $scope.closekk = function(){
    			  $("#myModal").hide();
    		  }
    		  
    	
    			  
    			  var countdown = function() {
    				    if ($scope.timer > 0) {
    				      $scope.timer--;
    				    } else {
    				      $interval.cancel(timerPromise);
    				      $scope.resendDisabled = false; // Enable the "Resend" button
    				    }
    				  };
    				 
    				 
    				  var timerPromise = $interval(countdown, 1000, $scope.timer);
    			  
    					var dataS ={};
						var URI = BASE_URL + ROOT_URL
						+ GETACTIVEPREFIXLIST;
				BillingGenericService
						.serviceAction(METHOD_GET, URI, dataS,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.prefixList = [];
									if (response.data.status == "success")
										angular.forEach(response.data.listObject, function(prefix) {
											if(prefix.isShowPatientRegistration == true){
												$scope.prefixList.push(prefix);
											}
										});
								});
				
    		      $scope.saveData = function(){
    			  
    			  if($scope.aadhar.myAadharNumber == null || $scope.aadhar.myAadharNumber == undefined || $scope.aadhar.myAadharNumber == ''){
    				    $rootScope.stopLoader();
						growl.error('Please Enter Aadhaar Card Number');
						return false;
    			  }
    			  
    			   if($scope.aadhar.myAadharNumber.length < 12  ){
   				    $rootScope.stopLoader();
						growl.error('Please Enter 12 Digit Aadhaar Card Number');
						return false;
   			      }
    			  
    			  $("#myModal").hide();
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/request/otp";
    				 var data={
    						  "aadharNumber":$scope.aadhar.myAadharNumber,
    						  "organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    						  "userId" : $scope.userId,
    						  "eventId":2
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						$scope.requestOtpObject = JSON.parse(response.data.object);
    						$scope.createAbhadiv = false;
    			    		$scope.createOTP = true;
    			    		$scope.timer = 60;     
    			    		// Start the timer again
    			    	    timerPromise = $interval(countdown, 1000, $scope.timer);
    						$rootScope.stopLoader();
    						 growl.success($scope.requestOtpObject.message);
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error($scope.requestOtpObject.message);
    					});
    			}
    		      
    		      
	             $scope.enrollMobileNumber = function(){
	            	 
	            	 if($scope.createOTPObject.otpInput =='' || $scope.createOTPObject.otpInput.length < 6){
                		 growl.error("OTP enterd is incorrect");
							$rootScope.stopLoader();
							return false;
                	 }
	            	 
        				$rootScope.startLoader();
        				var URI = BASE_URL + ROOT_URL + "/api/abdm/enrollment/auth/byAbdm";
        				 var data={
        						  "organizationId" : $scope.organizationId,
        						  "unitId" : $scope.unitId,
        						  "userId" : $scope.userId,
        						  "transactionId" : $scope.updateMobileNumberObject.txnId,
        						  "mobileNumber":$scope.createOTPObject.mobileInput,
        						  "otp" : $scope.createOTPObject.otpInput,
        						  "eventId":5
        					}
        				GenericService.serviceAction("POST", URI, data).then(
        					function (response) {
        						var jsonString = response.data.object;
        			    		var parsedObject = JSON.parse(jsonString);
        			    		var message = parsedObject.message;

        			    		console.log(message);
        						if(message =="Please enter a valid OTP. Entered OTP is either expired or incorrect."){
          			    		  $rootScope.stopLoader();
                                    growl.error("OTP enterd in incorrect");
                                    return;
          					}
        						$scope.createAbhadiv = false;
        						$scope.updateMobileNumberstrip = false;
        			    		$scope.createOTP = true;
        			    		
        						 if($scope.enrollmentEnrolByAadhaarObject.isNew == true){
     								$scope.xtoken =$scope.enrollmentEnrolByAadhaarObject.tokens.token;
     								$scope.getsuggesionForNew();
     							  }
     							  else if($scope.enrollmentEnrolByAadhaarObject.isNew == false){
     								$scope.xtoken =$scope.enrollmentEnrolByAadhaarObject.tokens.token;
     								$scope.profileAccountNew();
     							}
        						$scope.enrollMobileNumber = JSON.parse(response.data.object);
        						$scope.enrollmentEnrolByAadhaarObject.txnId = $scope.enrollMobileNumber.txnId;
        						$scope.createAbhadiv = false;
        						$scope.updateMobileNumberstrip = false;
        						$scope.createAbhadiv = false;
           			    		$scope.createOTP = false;
           			    		$scope.updateMobiletest = false;
           			    		$scope.updateMobiletestOTP = false;
        						$rootScope.stopLoader();
//        						 growl.success($scope.requestOtpObject.message);
        					},
        					function errorCallback(response) {
        						$rootScope.stopLoader();
        						growl.error($scope.requestOtpObject.message);
        					});
        			}
	      
	             $scope.isResendDisabledupdateMobileNumber = true;
	    			$scope.retryAttemptsupdateMobileNumber = 1;
    		      $scope.updateMobileNumber = function(){
    		    	  
    		    	  
    		    	  var test = true;
      				if ($scope.retryAttemptsupdateMobileNumber <= 2) {
      					console.log("retry attempt : " + $scope.retryAttemptsupdateMobileNumber);
      					$scope.retryAttemptsupdateMobileNumber++;
  					} else {
  						growl.error("Max resend limit reached.Please try again !!");
  						return false;
  					}
      				
    		    	  $("#mobileNumberValidaionPoupUp").hide();
        				$rootScope.startLoader();
        				var URI = BASE_URL + ROOT_URL + "/api/abdm/request/otp";
        				 var data={
        						  "organizationId" : $scope.organizationId,
        						  "unitId" : $scope.unitId,
        						  "userId" : $scope.userId,
        						  "transactionId" : $scope.enrollmentEnrolByAadhaarObject.txnId,
        						  "mobileNumber":$scope.createOTPObject.mobileInput,
        						  "eventId":4
        					}
        				GenericService.serviceAction("POST", URI, data).then(
        					function (response) {

    	   						if (response.data.status == "success") {
        						$scope.updateMobileNumberObject = JSON.parse(response.data.object);
        						$scope.createAbhadiv = false;
        			    		$scope.createOTP = false;
        			    		$scope.createAbhadiv = false;
           			    		$scope.createOTP = false;
           			    		$scope.updateMobiletest = false;
           			    		$scope.updateMobiletestOTP = true;
        			    		$scope.timer = 60;     
        			    		// Start the timer again
        			    	    timerPromise = $interval(countdown, 1000, $scope.timer);
        						$rootScope.stopLoader();
        						 growl.success($scope.updateMobileNumberObject.message);
    	   						}else{
    	   							$rootScope.stopLoader();
            						growl.error(response.data.message);
            						return null;
    	   						}
        					},
        					function errorCallback(response) {
        						$rootScope.stopLoader();
        						growl.error($scope.requestOtpObject.message);
        					});
        			}
    		      
    		      $scope.viewProfileCreateABHA = function(data){
    	   				$rootScope.startLoader();
    	   				var URI = BASE_URL + ROOT_URL + "/api/abdm/profile/account/abha-card";
    	   				$scope.mobile="";
    	   				 var data={
    	   						 "token":$scope.enrollmentEnrolByAadhaarObject.tokens.token,
    	   						  "transactionId" : $scope.enrollmentEnrolByAadhaarObject.txnId,
    	   						  "organizationId" : $scope.organizationId,
    	   						  "unitId" : $scope.unitId,
    	   						  "transactionType":"Creation",
    	   						  "eventId" :9
    	   					}
    	   				GenericService.serviceAction("POST", URI, data).then(
    	   					function (response) {
    	   						if (response.data.status == "success") {
    	   							$scope.alreadyExistAbhaPopuopCreateABHA = false;
    	   							$scope.pngCard='data:image/png;base64,'+response.data.object.base64PngCard;
    	    						$("#pngCardPopUP").show();
    	    						  $scope.verifyObjABHAAddAndNumBack();
    	    						  $scope.resetABHAAddAndNumBack();
    	    						$rootScope.stopLoader();
    	   						}
    	  		    				else if(response.data.status == "error")
    	  		    					{
    	  		    					   $rootScope.stopLoader();
    	  			   	   						$rootScope.stopLoader();
    	  			   	   						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
    	  		    					}
    	   					
    	   					},
    	   					function errorCallback(response) {
    	   						$rootScope.stopLoader();
    	   						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
    	   					});
    	   			}
    		      
    		  
    		  $scope.viewProfile = function(data){
   				$rootScope.startLoader();
   				var URI = BASE_URL + ROOT_URL + "/api/abdm/profile/account/abha-card";
   				$scope.mobile="";
   				 var data={
   						 "token":$scope.enrollmentEnrolByAadhaarObject.tokens.token,
   						  "transactionId" : $scope.enrollmentEnrolByAadhaarObject.txnId,
   						  "organizationId" : $scope.organizationId,
   						  "unitId" : $scope.unitId,
   						  "transactionType":"Verification",
   						  "eventId" :9
   					}
   				GenericService.serviceAction("POST", URI, data).then(
   					function (response) {
   						if (response.data.status == "success") {
   							$scope.alreadyExistAbhaPopuop = false;
   							$scope.pngCard='data:image/png;base64,'+response.data.object.base64PngCard;
    						$("#pngCardPopUP").show();
    						/*$scope.init();
    						$scope.verifyObjABHAAddAndNumBack();
    						$scope.resetABHAAddAndNumBack();*/
    						$rootScope.stopLoader();
   						}
  		    				else if(response.data.status == "error")
  		    					{
  		    					   $rootScope.stopLoader();
  			   	   						$rootScope.stopLoader();
  			   	   						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
  		    					}
   					
   					},
   					function errorCallback(response) {
   						$rootScope.stopLoader();
   						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
   					});
   			}
    		  
    		  
    		  $scope.profileAccountNewForNew  = function(){
    			  $("#mobileNumberValidaionPoupUp").hide();
   				$rootScope.startLoader();
   				var URI = BASE_URL + ROOT_URL + "/api/abdm/profile/account";
   				$scope.mobile="";
   				 var data={
   						 "token":$scope.enrollmentEnrolByAadhaarObject.tokens.token,
   						 "transactionId":$scope.enrollmentEnrolByAadhaarObject.txnId,
   						  "organizationId" : $scope.organizationId,
   						  "unitId" : $scope.unitId,
   						  "transactionType":"Creation",
   						  "eventId" :8
   					}
   				GenericService.serviceAction("POST", URI, data).then(
   					function (response) {
   						if (response.data.status == "success") {
   							$scope.profileAccountObject = JSON.parse(response.data.object);
   							console.log("/profile/account",$scope.profileAccountObject);
   						    $scope.viewProfile();
   						$rootScope.stopLoader();
   						}
  		    				else if(response.data.status == "error")
  		    					{
  		    					   $rootScope.stopLoader();
  			   	   						$rootScope.stopLoader();
  			   	   						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
  		    					}
   					
   					},
   					function errorCallback(response) {
   						$rootScope.stopLoader();
   						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
   					});
   			}
    		  
    		  $scope.profileAccountNewCreateABHA = function(){
    			  $("#mobileNumberValidaionPoupUpCreateABHA").hide();
 				$rootScope.startLoader();
 				var URI = BASE_URL + ROOT_URL + "/api/abdm/profile/account";
 				$scope.mobile="";
 				 var data={
 						 "token":$scope.enrollmentEnrolByAadhaarObject.tokens.token,
 						 "transactionId":$scope.enrollmentEnrolByAadhaarObject.txnId,
 						  "organizationId" : $scope.organizationId,
 						  "unitId" : $scope.unitId,
 						  "transactionType":"Creation",
 						  "eventId" :8
 					}
 				GenericService.serviceAction("POST", URI, data).then(
 					function (response) {
 						if (response.data.status == "success") {
 							$scope.profileAccountObject = JSON.parse(response.data.object);
 							console.log("$scope.profileAccountObject",$scope.profileAccountObject);
 							console.log("/profile/account",$scope.profileAccountObject);
 							var arr = $scope.profileAccountObject.ABHANumber.split('-');
 							$scope.abhaNumber = arr[3];
 							$scope.alreadyExistAbhaPopuopCreateABHA = true;
 						    $rootScope.stopLoader();
 						}
		    				else if(response.data.status == "error")
		    					{
		    					   $rootScope.stopLoader();
			   	   						$rootScope.stopLoader();
			   	   						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
		    					}
 					
 					},
 					function errorCallback(response) {
 						$rootScope.stopLoader();
 						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
 					});
 			}
    		  
    		  $scope.profileAccountNew = function(){
    			  $("#mobileNumberValidaionPoupUp").hide();
 				$rootScope.startLoader();
 				var URI = BASE_URL + ROOT_URL + "/api/abdm/profile/account";
 				$scope.mobile="";
 				 var data={
 						 "token":$scope.enrollmentEnrolByAadhaarObject.tokens.token,
 						 "transactionId":$scope.enrollmentEnrolByAadhaarObject.txnId,
 						  "organizationId" : $scope.organizationId,
 						  "unitId" : $scope.unitId,
 						 "transactionType":"Verification",
 						  "eventId" :8
 					}
 				GenericService.serviceAction("POST", URI, data).then(
 					function (response) {
 						if (response.data.status == "success") {
 							$scope.profileAccountObject = JSON.parse(response.data.object);
 							console.log("$scope.profileAccountObject",$scope.profileAccountObject);
 							console.log("/profile/account",$scope.profileAccountObject);
 							var arr = $scope.profileAccountObject.ABHANumber.split('-');
 							$scope.abhaNumber = arr[3];
 							$scope.alreadyExistAbhaPopuop = true;
 						    $rootScope.stopLoader();
 						}
		    				else if(response.data.status == "error")
		    					{
		    					   $rootScope.stopLoader();
			   	   						$rootScope.stopLoader();
			   	   						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
		    					}
 					
 					},
 					function errorCallback(response) {
 						$rootScope.stopLoader();
 						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
 					});
 			}
    			
    		  $scope.validateProfileDetails = function () {
    			    const value = $scope.createOTPObject.profileDetails;

    			    // Condition 1: Minimum length 8 characters
    			    if (value.length < 8) {
    			        return 'Profile details must be at least 8 characters long.';
    			    }

    			    // Condition 2: Maximum length 18 characters
    			    if (value.length > 18) {
    			        return 'Profile details must be no more than 18 characters long.';
    			    }

    			    // Condition 3: Only one dot (.), one underscore (_), and one at (@) allowed
    			    const dotCount = (value.match(/\./g) || []).length;
    			    const underscoreCount = (value.match(/_/g) || []).length;
    			    const atCount = (value.match(/@/g) || []).length;
    			    if (dotCount > 1 || underscoreCount > 1 || atCount > 1) {
    			        return 'Profile details can only contain one dot (.), one underscore (_), and one at (@).';
    			    }

    			    // Condition 4: Dot, underscore, or at (@) cannot be at the start or end
    			    if (value.startsWith('.') || value.startsWith('_') || value.startsWith('@') ||
    			        value.endsWith('.') || value.endsWith('_') || value.endsWith('@')) {
    			        return 'Special characters (dot, underscore, or at) cannot be at the beginning or end.';
    			    }

    			    // Condition 5: Only alphanumeric characters, dot (.), underscore (_), and at (@) are allowed
    			   // const alphanumericRegex = /^[a-zA-Z0-9._@]+$/;
    			    const alphanumericRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[_.])[a-zA-Z0-9_.]+$/;
    			    const regex = /^[a-zA-Z0-9._]+$/;
    			    // Test if the input string matches the regular expression
    			    //return regex.test(input);
    			    if (!alphanumericRegex.test(value)) {
    			    	
    			    	var onlyLettersAndUnderscores = /^[a-zA-Z_]+$/;
    			    	 if (onlyLettersAndUnderscores.test(value)) {
    	    			        return 'Profile details cannot contain only letters (e.g., test_abhaaddress@sbx). Please use a alphaNumeric of letters.';
    	    			    }
    			    	
    			    	 if ($.isNumeric(value)) {
    	    			        return 'Profile details cannot contain only numbers (e.g., 9876546577777@sbx). Please use a alphaNumeric letters.';
    	    			    }
    			    	
    			        return 'Profile details can only contain alphanumeric characters which can contain , dot (.) or underscore (_).';
    			    }

    			    // Condition 6: Disallow only letters (e.g., test_abhaaddress@sbx)
    			   

    			    // Condition 7: Disallow only numbers (e.g., 9876546577777@sbx)
    			   

    			    return ''; // Valid input
    			};
    			
    		  
    		  $scope.enrollAbhaAddress = function(){
    			  
    				var inputElement = document.getElementById('profileDetailsNew');
    		        var inputValue = inputElement.value;

    		        const errorMessage = $scope.validateProfileDetails();
    		        if (errorMessage) {
    		        	growl.error(errorMessage);
    		        	return false;
    		        } 
    		        
   				$rootScope.startLoader();
   				var URI = BASE_URL + ROOT_URL + "/api/abdm/enrollment/enrol/abha-address";
   				$scope.mobile="";
   				 var data={
   						  "transactionId" :$scope.suggesionForNewObject.txnId,
   						  "abhaId" :$scope.createOTPObject.profileDetails,
   						  "organizationId" : $scope.organizationId,
   						  "unitId" : $scope.unitId,
   						  "eventId" :7
   					}
   				GenericService.serviceAction("POST", URI, data).then(
   					function (response) {
   						if (response.data.status == "success") {
   							$scope.abhaAddressNewObject = JSON.parse(response.data.object);
   							$scope.profileDetails = false;
   							$scope.createPhrAddress = true;
   							$scope.createOTPObject.healthNumber = $scope.abhaAddressNewObject.healthIdNumber;
   							$scope.createOTPObject.profileDetails = $scope.abhaAddressNewObject.preferredAbhaAddress;
   							$scope.txnIdProfileAccount = $scope.abhaAddressNewObject.txnId;
   							
   						    $rootScope.stopLoader();
   						}
  		    				else if(response.data.status == "error")
  		    					{
  		    					   $rootScope.stopLoader();
  			   	   						$rootScope.stopLoader();
  			   	   				        growl.error("ABHA Address is already exists");
  		    					}
   					
   					},
   					function errorCallback(response) {
   						$rootScope.stopLoader();
   						growl.error("ABHA Address is already exists");
   					});
   			}
    		  
    		  $scope.getsuggesionForNew = function(){
    			  $scope.createAbhass = false;
    			  $("#mobileNumberValidaionPoupUp").hide();
    			  $("#mobileNumberValidaionPoupUpCreateABHA").hide();
    			  $scope.enrollmentEnrolByAadhaarObject.isNew = true;
    				$("#pngCardPopUP").hide();
 				$rootScope.startLoader();
 				var URI = BASE_URL + ROOT_URL + "/api/abdm/enrollment/enrol/suggestion";
 				$scope.mobile="";
 				 var data={
 						  "transactionId" :$scope.requestOtpObject.txnId,
 						  "organizationId" : $scope.organizationId,
 						  "unitId" : $scope.unitId,
 						  "eventId" :6
 					}
 				GenericService.serviceAction("POST", URI, data).then(
 					function (response) {
 						if (response.data.status == "success") {
 							$scope.phrSuggestions = [];
 							$scope.suggesionForNewObject = JSON.parse(response.data.object);
 							$scope.phrSuggestions=$scope.suggesionForNewObject.abhaAddressList;
 							$scope.profileDetails = true;
 						    $rootScope.stopLoader();
 						}
		    				else if(response.data.status == "error")
		    					{
		    					   $rootScope.stopLoader();
			   	   						$rootScope.stopLoader();
			   	   						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
		    					}
 					
 					},
 					function errorCallback(response) {
 						$rootScope.stopLoader();
 						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
 					});
 			}
    			$scope.verifyOTP = function(){
    				var test = true;
    				
    				 if($scope.createOTPObject.otpInput == null || $scope.createOTPObject.otpInput == undefined || $scope.createOTPObject.otpInput == ''){
     				    $rootScope.stopLoader();
 						growl.error('Please Enter OTP');
 						return false;
     			  }
    				 
    				 if($scope.createOTPObject.mobileInput == null || $scope.createOTPObject.mobileInput == undefined || $scope.createOTPObject.mobileInput == ''){
      				    $rootScope.stopLoader();
  						growl.error('Please Enter Mobile Number');
  						return false;
      			   }
                     var indianMobileRegex =  /^([6-9]{1})[0-9]{9}$/;
                     test=indianMobileRegex.test($scope.createOTPObject.mobileInput);
                     
                     if(test == false){
                         growl.error("Please enter valid mobile number",{
                             title : ERROR_MSG
                             });
                             return false;
                     }
                     
    				$("#otpModal").hide();
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/enrollment/enrol/byAadhaar";
    				$scope.mobile="";
    				 var data={
    						 "aadharNumber":$scope.aadhar.myAadharNumber,
    						  "otp":$scope.createOTPObject.otpInput,
    						  "transactionId" : $scope.requestOtpObject.txnId,
    						  "organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    						  "mobileNumber":$scope.createOTPObject.mobileInput,
    						  "eventId" :3
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						if (response.data.status == "success") {
    							$scope.enrollmentEnrolByAadhaarObject = JSON.parse(response.data.object);
    							console.log("$scope.enrollmentEnrolByAadhaarObject",$scope.enrollmentEnrolByAadhaarObject);
    							$scope.txnIdProfileAccount = $scope.enrollmentEnrolByAadhaarObject.txnId;
    							//$scope.enrollmentEnrolByAadhaarObject.isNew =true;
    							
    							if($scope.enrollmentEnrolByAadhaarObject.message != undefined ){
    								growl.success($scope.enrollmentEnrolByAadhaarObject.message);
    							}else{
    								growl.error($scope.enrollmentEnrolByAadhaarObject.error.message);
    								$rootScope.stopLoader();
    								return false;
    							}
    						
    							
    							
    							  if($scope.enrollmentEnrolByAadhaarObject.ABHAProfile.mobile == null || $scope.enrollmentEnrolByAadhaarObject.ABHAProfile.mobile != $scope.createOTPObject.mobileInput ){
    								   $scope.updateMobileNumberstrip = false;
    								   $scope.updateMobiletest = true;
    								   $scope.createOTP = false;
    					    		   $scope.createAbhadiv = false;
    					    		   $scope.enterMobileNumber = false;
    					    		   $scope.createOTPObject.otpInput = "";
    					    		   $scope.timer = 90;     
    	        			    		// Start the timer again
    	        			    	    timerPromise = $interval(countdown, 1000, $scope.timer);
    								   $scope.updateMobileNumber();  
    							   }else{
    								   $scope.updateMobileNumberstrip = false;
    								   $scope.createOTP = false;
    					    		   $scope.createAbhadiv = false;
    					    		   $scope.enterMobileNumber = false;
    								   $("#mobileNumberValidaionPoupUpCreateABHA").show();
    							   }
				    		   $scope.createOTP = false;
				    		   $scope.createAbhadiv = false;
				    		   $scope.enterMobileNumber = false;
				    		   
    						   $rootScope.stopLoader();
    						}
		    				else if(response.data.status == "error")
		    					{
		    					
		    					if(response.data.code==="504"){
		    						$rootScope.stopLoader();
		    						growl.error(response.data.message);	
		    						$scope.createAbhass = true;
		    						 $scope.enterMobileNumber = false;
		    	                        $scope.enterOtp = false;
		    					}else if(response.data.code==="422"){
		    						$rootScope.stopLoader();
		    						 growl.error("OTP enterd in incorrect");
		    						$scope.createAbhass = false;
		    						 $scope.enterMobileNumber = false;
		    	                        $scope.enterOtp = false;
		    					}
		    					else{
		    					   $rootScope.stopLoader();
                                   growl.error("OTP enterd in incorrect");
		    					}
		    					}
    					
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
    					});
    			}
    			
                $scope.barcodeScanned= false;
                $scope.handleDoubleClick=false;
                self.model={};
                self.model.barcode='';
                $scope.BarcodeScan = function (event, barcode) {
                    $scope.handleDoubleClick=false;
                    if(self.model.barcode == undefined || self.model.barcode.length == 0 || self.model.barcode == null)
                    {
                        growl.error("Barcode Number Should Not Be Blank...")
                        return false;
                    }
                    if(self.model.barcode.length > 0){﻿
                    var originalString = self.model.barcode;
                    const fixedStr = originalString.replace(/ä/g, '"a');
                    const fixedStr1 = fixedStr.replace('Ä','"A');
					var jsonObj = JSON.parse(fixedStr1);
					console.log(jsonObj);
					 var phr = jsonObj.phr;
                    console.log(phr);
                    $scope.obj.sbxId = phr;
                    $scope.fetchMode();
                    }
                   
                }
    			
    			
                $scope.toggleInputField = function () {
                	$rootScope.startLoader();
                	 $scope.showInputField = !$scope.showInputField;
                	 $timeout(function() {
                    document.getElementById("barcode").focus();
                	 }, 500);
                   
                };
    			
    			$scope.closePNGcard = function(){
    				$("#pngCardPopUP").hide();
    				 $(".modal-backdrop").hide();
    				 $state.reload();
    				 var dataPatient = {};
    				 $scope.init();
    				 dataPatient =$scope.profileAccountObject;
    				 dataPatient.prefixList =$scope.prefixList ;
    				 $state.go("patientRegistration",{
    					 patientRegistrationObjAbdm :dataPatient
						});
    				
    			}
    			$scope.getPngCard = function(){
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/getpngCard";
    				 var data={
    						 "aadharNumber":"725360661391",
    					
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						$scope.pngCard='data:image/png;base64,'+response.data.object.abdmHealthCard;
    						$("#pngCardPopUP").show();
    						growl.success("Health Card Generated Successfully.");
    						$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
    			
    			
    			$scope.isResendDisabled = true;
    			$scope.retryAttempts = 1;
    			$scope.resendOtp = function(){
    				
    				/*if($scope.createOTPObject.mobileInput == null || $scope.createOTPObject.mobileInput == undefined || $scope.createOTPObject.mobileInput == ''){
      				    $rootScope.stopLoader();
  						growl.error('Please Enter Mobile Number');
  						return false;
      			   }*/
    				
    				var test = true;
    				if ($scope.retryAttempts <= 2) {
    					console.log("retry attempt : " + $scope.retryAttempts);
    					$scope.retryAttempts++;
					} else {
						growl.error("Max resend limit reached.Please try again !!");
						return false;
					}
    				
 				
                   $rootScope.startLoader();
 					var URI = BASE_URL + ROOT_URL + "/api/abdm/request/otp";
   				 var data={
   						  "aadharNumber":$scope.aadhar.myAadharNumber,
   						  "mobileNumber":$scope.createOTPObject.mobileInput,
   						  "organizationId" : $scope.organizationId,
   						 "transactionId" : $scope.requestOtpObject.txnId,
   						  "unitId" : $scope.unitId,
   						  "userId" : $scope.userId,
   						  "eventId":2
   					}
   				GenericService.serviceAction("POST", URI, data).then(
   					function (response) {
   						$scope.requestOtpObject = JSON.parse(response.data.object);
   						$scope.createAbhadiv = false;
   			    		$scope.createOTP = true;
   			    		$scope.timer = 60;
   			    	    timerPromise = $interval(countdown, 1000, $scope.timer);
   						$rootScope.stopLoader();
   						growl.success("OTP Resent Successfully.");
   					},
   					function errorCallback(response) {
   						$rootScope.stopLoader();
   						growl.error($scope.requestOtpObject.message);
   					});
    			}
    		
    			
    			
    			$scope.resendMobileUnlinkedOtp = function(){
    				$rootScope.startLoader();
    				
    				// $scope.timer = 60;
 				    // Start the timer again
 				    timerPromise = $interval(countdown, 1000, $scope.timer);
 				   $scope.isResendDisabled = false;
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/mobile/resendOtp";

    				 var data={
    						 "aadharNumber":$scope.aadhar.myAadharNumber,
    						"transactionId" : $scope.txnId,
    						"organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						//$scope.txnId=response.data.object;
    						$scope.txnId=response.data.object.transactionId;
    						$scope.timer = 90; 
    						 timerPromise = $interval(countdown, 1000, $scope.timer);
    						growl.success("OTP Resent Successfully.");
    						$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
    			
    			$scope.createOTPForUnlinked=false;
    			$scope.checkAndGenerateMobileOTPFunction = function(){
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/aadhaar/checkAndGenerateMobileOTP";

    				 var data={
    						 "mobileNumber":$scope.createOTPObject.mobileInput,
    						 "aadharNumber":$scope.aadhar.myAadharNumber,
    						"transactionId" : $scope.txnId,
    						"organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						if (response.data.status =="success") {
    							$scope.enterMobileNumber = false;
	    						$scope.checkAndGenerate=response.data.object.checkAndGenerate;
	    						
	    						if($scope.checkAndGenerate === "true"){
	    							$scope.createHealthIdWithPreVerified();
	    						}else{
	    							$scope.createOTPObject.otpInput='';
	    							$scope.createOTPForUnlinked=true;
	    							$scope.resendMobileUnlinkedOtp();
	    						}
	    						$rootScope.stopLoader();
    						} else {
    							$rootScope.stopLoader();
    							growl.error('Please enter valid mobile number.');
    						}
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
    			
    			
    			
    			
    			$scope.verifyMobileOTP = function(){
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/aadhaar/verifyMobileOTP";

    				 var data={
    						 "otp":$scope.createOTPObject.mobileInput,
    						"transactionId" : $scope.txnId,
    						"organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						$scope.checkAndGenerateMobileOTP=response.data.object;
    						$scope.verifyhOTP = true;
    						$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
    			
    			
    			$scope.verifyMobileOTPNotLinkedToAadhar = function(){
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/aadhaar/verifyMobileOTP";

    				 var data={
    						 "otp":$scope.createOTPObject.otpInput,
    						"transactionId" : $scope.txnId,
    						"organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						$scope.txnId=response.data.object.transactionId;
    						if($scope.txnId != null){
    							$scope.createHealthIdWithPreVerified();
    						}
    						$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
    			
    			
    			$scope.createHealthIdWithPreVerified = function(){
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/aadhaar/createHealthIdWithPreVerified";

    				 var data={
    						 "mobileNumber":$scope.createOTPObject.mobileInput,
    						 "aadharNumber":$scope.aadhar.myAadharNumber,
    						"transactionId" : $scope.txnId,
    						"organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						$scope.token=response.data.object.token;
    						$scope.createOTPObject.healthId=response.data.object.healthId;
    						$scope.createOTPObject.healthNumber=response.data.object.healthIdNumber;
    						$scope.verifyhOTP = false;
    						$scope.profileDetails = true;
    						$scope.profileDetailsFunction();
    						growl.success("Successfully.");
    						$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
    			
    			
    			$scope.profileDetailsFunction = function(){
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/aadhaar/profileDetails";

    				 var data={
    						 "token":$scope.token,
    						 "organizationId" : $scope.organizationId,
    						 "unitId" : $scope.unitId,
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						$scope.tnxId=response.data.object.transactionId;
    						$scope.suggestions();
    						growl.success("Successfully.");
    						$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
    			
    			
    			
    			$scope.suggestions = function(){
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/aadhaar/suggestion";

    				 var data={
    						 "transactionId":$scope.tnxId,
    						"organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						$scope.phrSuggestions=JSON.parse(response.data.object.phrSuggestions);
    						growl.success("Successfully.");
    						$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
    			
    			
    			
    			$scope.isExistFunction = function(){
    				
    				
    				var inputElement = document.getElementById('profileDetailsNew');
    		        var inputValue = inputElement.value;

    		        // Validation rules
    		        var minLength = 8;
    		        var maxLength = 18;
    		        var allowedSpecialCharacters = ['.', '_'];

    		        // Check minimum length
    		        if (inputValue.length < minLength) {
    		        	growl.warning('Address is too short. Minimum length is ' + minLength + ' characters.');
    		            return false;
    		        }

    		        // Check maximum length
    		        if (inputValue.length > maxLength) {
    		        	growl.warning('Address is too long. Maximum length is ' + maxLength + ' characters.');
    		            return false;
    		        }
    		     // Validation rule
    		        var regex = /^[a-zA-Z0-9]+([._][a-zA-Z0-9]+)?$/;

    		        // Check if the address matches the pattern
    		        if (!regex.test(inputValue)) {
    		        	growl.warning('Invalid address format. It should have at least one alphanumeric character and optionally one dot or underscore in between.');
    		            return false;
    		        }


    				$scope.profileDetails = false;
    				$scope.createPhrAddress = true;
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/aadhaar/isExist";

    				 var data={
    						 "sbxId":$scope.createOTPObject.profileDetails,
    						 "organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						$scope.existResp=response.data.object.exists;
    						if($scope.existResp == false){

    		    				var URI = BASE_URL + ROOT_URL + "/api/abdm/aadhaar/createPhrAddress";

    		    				 var data={
    		    						 "sbxId":$scope.createOTPObject.profileDetails,
    		    						 "transactionId":$scope.tnxId,
    		    						 "organizationId" : $scope.organizationId,
    		    						 "unitId" : $scope.unitId,
    		    					}
    		    				GenericService.serviceAction("POST", URI, data).then(
    		    					function (response) {
    		    						$scope.tokenForPng=response.data.object.token;
    		    						growl.success("Successfully.");
    		    						$rootScope.stopLoader();
    		    					},
    		    					function errorCallback(response) {
    		    						$rootScope.stopLoader();
    		    						growl.error('Connection time out');
    		    					});
    		    			
    						}
    						growl.success("Successfully.");
    						$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					}); 				     				 
    			
    			}
    			
    			
    			$scope.getPngCardNew = function(){
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/aadhaar/getPngCardNew";

    				 var data={
    						 "token":$scope.tokenForPng,
    						 "aadharNumber":$scope.aadhar.myAadharNumber,
    						 "organizationId" : $scope.organizationId,
    						 "unitId" : $scope.unitId,
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						
    						$scope.pngCard='data:image/png;base64,'+response.data.object.base64PngCard;
    						$("#pngCardPopUP").show();
    						$scope.patientDetails=response.data.object.aadharPreverifiedJson;
    						console.log("$scope.patientDetails",$scope.patientDetails);
    						growl.success("Successfully.");
    						$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
    			
    			var qrcode = new QRCode(document.getElementById("qrcode"), {
					width : 200,
					height : 200
				});
    		
    			$scope.makeCodea=function(elText) {		
    				if (elText=='') {
    					return;
    				}
    				qrcode.makeCode(elText);
    			}
    			
    			$scope.generateQRcode=function(){
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/getCredentialDetails";
    				var data = {
    					"unitId":$scope.unitId,
    		            "organizationId":$scope.organizationId,
    				}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						$scope.hipId = response.data.object.id
    						//sbx abdm
    			        	  //var qURL="https://phrsbx.abdm.gov.in/share-profile?hip-id="+$scope.hipId+"&counter-id=12345";
    			        	 //prod abdm
    			        	  var qURL="https://phr.abdm.gov.in/share-profile?hip-id="+$scope.hipId+"&counter-id=12345";
    			        	  $("#qrDetailsPopup").modal();
    					      $scope.makeCodea(qURL);
    					     $rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    				}
    			
    			$scope.getTransctionTableDetails = function(){
    				
  				 $scope.checkCall =""
  					 if($scope.fetModeResponse){
  						 $scope.requestIdFetch = $scope.fetModeResponse.requestId;
  						 $scope.checkCall = 'fetch';
  					 }
  				  
  				  if($scope.initResponse){
						 $scope.requestIdFetch = $scope.initResponse.requestIdInit;
						 $scope.checkCall = 'init';
					 }
  				  
  				  if($scope.confirmResponse){
						 $scope.requestIdFetch = $scope.confirmResponse.requestIdConfirm;
						 $scope.checkCall = 'confirm';
					 }
  				  
  				if($scope.hipInitiateLinkingByGenerateTokenObject){
						 $scope.requestIdFetch = $scope.hipInitiateLinkingByGenerateTokenObject.transactionId;
						 $scope.checkCall = 'carecontext';
					 }
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/auth/getTranscationTableDetailsById";
    				 var data={
    						  "requestId" : $scope.requestIdFetch ,
    						  "unitId" : $scope.unitId,
    						  "checkCall":$scope.checkCall
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {

    		                
    		                 if( $scope.checkCall == 'fetch'){
    		                	 $scope.getTransacrtionDetails = response.data.object.callBackJsonoNFetchMode;
    		                	 var responsnedObject ={};
    		                	 responsnedObject = JSON.parse($scope.getTransacrtionDetails);
    		                	 $scope.authMethod = [];
    		                	// $scope.authMethod=responsnedObject.modes;
    		                	 $scope.authMethod=responsnedObject.auth.modes;
    		                 }
    		                 if($scope.checkCall == 'carecontext'){
    		                  $scope.getTransacrtionDetails = JSON.parse(response.data.object.apiResponseEvent40);
    		                  console.log("carecontext call back json",$scope.getTransacrtionDetails);
    		                  
    		                  if( $scope.getTransacrtionDetails.status != undefined ){
    		                	  growl.success($scope.getTransacrtionDetails.status);
    		                  }else{
    		                	  growl.error($scope.getTransacrtionDetails.error.message);
    		                  }
    		                  
    		                 }
    		                   $scope.checkCall ="";
    		                 $rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
    			
    			$scope.obj={
    					"sbxId":"",
    					"sbxIdNew":""
    			}
    			$scope.fetchMode = function(){
    				
    				$rootScope.startLoader();
    				 if($scope.obj.sbxId == null || $scope.obj.sbxId == undefined || $scope.obj.sbxId == ''){
      				    $rootScope.stopLoader();
  						growl.error('Please Enter ID');
  						return false;
      			     }
    				
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/auth/fetch-modes";
    				 var data={
    						 "sbxId":$scope.obj.sbxId,
    						  "organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						$rootScope.startLoader();
    		                 $scope.fetModeResponse = response.data.object;
    		                 $scope.fetchId=response.data.object.requestId;
    		                 console.log("fetchMode Response", $scope.fetModeResponse);
    		                 if($scope.fetchDataView==false){
    		                	 $timeout( function(){
        		                	 $scope.getTransctionTableDetails();
    		                    },5000); 
    		                 }else{
    		                	 $rootScope.stopLoader();
    		                 }
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
    			
    			
    			$scope.initMode = function(){
    				
   				 if($scope.obj.sbxId == null || $scope.obj.sbxId == undefined || $scope.obj.sbxId == ''){
     				    $rootScope.stopLoader();
 						growl.error('Please Enter ID');
 						return false;
     			  }
   				$rootScope.startLoader();
   				var URI = BASE_URL + ROOT_URL + "/api/abdm/auth/init";
   				 var data={
   						 "sbxId":$scope.obj.sbxId,
   						  "organizationId" : $scope.organizationId,
   						  "unitId" : $scope.unitId,
   						  "requestIdFetch": $scope.fetchId,
   						  "authMode":$scope.authOBJECT.authMode
   					}

   				GenericService.serviceAction("POST", URI, data).then(
   					function (response) {

   		                 $scope.initResponse = response.data.object;
   		              console.log("initResponse Response", $scope.initResponse);
   		               //  $("#confirmOTP").modal(); 
   		              if($scope.authOBJECT.authMode == "DEMOGRAPHICS"){
   		               $("#confirmPopup").modal();
   		               $scope.enterOtp = false;
   		              }
   						 $rootScope.stopLoader();
   					},
   					function errorCallback(response) {
   						$rootScope.stopLoader();
   						growl.error('Connection time out');
   					});
   			}
    			
    			$scope.callConfirm = function(){
    				
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/auth/confirm";

    				if($scope.authOBJECT.authMode != "DEMOGRAPHICS"){
    					 var data={
        						 // "otp":$scope.authOBJECT.confirmOTP,
        						  "otp":$scope.enterOtpObject.confirmOTP,
        						  "sbxId": $scope.fetModeResponse.sbxId,
        						  "organizationId" : $scope.organizationId,
        						  "unitId" : $scope.unitId,
        						  "requestId" :$scope.fetModeResponse.requestId,
        						  "authMode":$scope.authOBJECT.authMode,
        						  "isFromCoversheet":'N'
        					}
    				}else{
    					 var data={
        						  "sbxId": $scope.fetModeResponse.sbxId,
        						  "organizationId" : $scope.organizationId,
        						  "unitId" : $scope.unitId,
        						  "requestId" :$scope.fetModeResponse.requestId,
        						  "authMode":$scope.authOBJECT.authMode,
        						  "isFromCoversheet":'N'
        					}
    					 
    					  if($scope.shareObject!= null &&  $scope.shareObject!= undefined &&  $scope.shareObject!=""){
      	   					data.name=$scope.shareObject.profile.patient.name;
      	   					data.gender=$scope.shareObject.profile.patient.gender;
      	   					data.dob=$scope.shareObject.profile.patient.yearOfBirth+'-0'+$scope.shareObject.profile.patient.monthOfBirth+'-'+$scope.shareObject.profile.patient.dayOfBirth;
      	   					data.identifier=JSON.stringify($scope.shareObject.profile.patient.identifiers[0]);
      	   				    data.tokenNumber = $scope.tokenNumberShare;
      	   				   $scope.enterOtp = false;
      	   				 }
    				}
    				 
    				  
    			
    				  
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {

    		                 $scope.confirmModeResponse = response.data.object;
    		                 $scope.init();
    		                 growl.success('ABHA Verified Successfully');
    		                 if($scope.shareObject!= null &&  $scope.shareObject!= undefined &&  $scope.shareObject!=""){
	    		                 $scope.patientDetails = $scope.shareObject.profile.patient;
	    		                 var addressh  = $scope.shareObject.profile.patient.address.line+" "+$scope.shareObject.profile.patient.address.district+" "+$scope.shareObject.profile.patient.address.state+" "+$scope.shareObject.profile.patient.address.pincode;
	    		                 $scope.patientDetails.address = addressh;
	    		                 $scope.patientDetails.districtName = $scope.shareObject.profile.patient.address.district;
	    		                 $scope.patientDetails.stateName = $scope.shareObject.profile.patient.address.stateName;
	    		                 $scope.patientDetails.pincode = $scope.shareObject.profile.patient.address.pincode;
	    		                 angular.forEach($scope.shareObject.profile.patient.identifiers,function(v,key){
	    		                	 if(v.type =="MOBILE"){
	    		                		 $scope.patientDetails.mobile = v.value;
	    		                	 }
	    		                 });
	    		             	var name= $scope.patientDetails.name.split(' ');
	    		                 $scope.patientDetails.firstName = name[0];
	    		                 $scope.patientDetails.middleName = name[1];
	    		                 $scope.patientDetails.lastName = name[2];
	    		                 $scope.patientDetails.prefixList =$scope.prefixList;
	    		                 $scope.shareObject =null;
	    		                 $("#pngCardPopUP").hide();
	    		                 $("#confirmPopup").hide();
	    	    				 $(".modal-backdrop").hide();
	    	    				 $state.go("patientRegistration",{
	    	    					 patientRegistrationObjAbdm :$scope.patientDetails
	    							});
    		                 }
    		                 
    		                 self.model.barcode="";
    						$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}  
    			
    			
    			
               $scope.newSearchByHealthId = function(){
            	  // $scope.obj.sbxId = $scope.obj.sbxIdNew;
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/searchByHealthId";

    				  var data= {
    						  "healthId": $scope.obj.sbxIdNew,
    						  "organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    						}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						if( response.data.status =="success" ){
    						 $scope.authMethods =[];
    		                 $scope.searchByHealthIdResponse = response.data.object;
    		                 
    		                 console.log("reepsones search health id",$scope.searchByHealthIdResponse);
    		                 
    		                 $scope.authMethod =JSON.parse($scope.searchByHealthIdResponse.authMethods);
    		                 
    		                 var lengthofArray = $scope.authMethod.length;
    		                 
    		                 $scope.searchHealthid = false;
    		                 $scope.searchOTPAuthentication = true;
    						$rootScope.stopLoader();
    						}else{
    							$rootScope.stopLoader();
        						growl.error(response.data.message);
    						}
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}  
               
               
               $scope.downloadPNG = function() {
            	    // Add your base64 data here
            	    var base64Data = $scope.pngCard;

            	    // Convert base64 to Blob
            	    var byteCharacters = atob(base64Data.split(',')[1]);
            	    var byteNumbers = new Uint8Array(byteCharacters.length);
            	    for (var i = 0; i < byteCharacters.length; i++) {
            	      byteNumbers[i] = byteCharacters.charCodeAt(i);
            	    }
            	    var blob = new Blob([byteNumbers], { type: 'image/png' });

            	    // Create a URL for the Blob
            	    var blobUrl = URL.createObjectURL(blob);

            	    // Create a link element
            	    var link = document.createElement('a');
            	    link.href = blobUrl;
            	    link.download = 'image.png';

            	    // Trigger the click event to initiate the download
            	    link.click();

            	    // Clean up by revoking the Blob URL
            	    URL.revokeObjectURL(blobUrl);
            	  };
            	  
               $scope.initModeSearchByHealthId = function(){
            	   
    				 if($scope.obj.sbxIdNew == null || $scope.obj.sbxIdNew == undefined || $scope.obj.sbxIdNew == ''){
      				    $rootScope.stopLoader();
  						growl.error('Please Enter ID');
  						return false;
      			  }
    				 
    				 if($scope.authOBJECT.authMode == 'AADHAAR_OTP'){
    					 growl.warning('OTP sent on your aadhar linked mobile number ');
    				 }
    				 
    				 if($scope.authOBJECT.authMode == 'MOBILE_OTP'){
    					 growl.warning('OTP send on your linked mobile number');
    				 }
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/auth/init/SerachByHealthId";
    				 var data={
    						 "sbxId":$scope.obj.sbxIdNew,
    						  "organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    						  "requestIdFetch": $scope.fetchId,
    						  "authMode":$scope.authOBJECT.authMode
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						if( response.data.status =="success" ){
    		                 $scope.initResponse = response.data.object;
    		              console.log("initResponse Response", $scope.initResponse);
    		                $scope.searchOTPVerification = true;
 		                    $scope.searchOTPAuthentication = false;
    						 $rootScope.stopLoader();
    						}else{
    							$rootScope.stopLoader();
        						growl.error(response.data.message);
    						}
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
               
               $scope.CONFIRMByHealthId = function(){
            	   
            	   
   				if($scope.authOBJECT.authMode == 'AADHAAR_OTP'){
   					
   					$rootScope.startLoader();
   	   				var URI = BASE_URL + ROOT_URL + "/api/abdm/confirmWithAadhaarOtp";

   	   		           var data= {
  					      "otp": $scope.search.searchByHealthOTP,
     	   		          "transactionId": $scope.initResponse.transactionId,
  						  "organizationId" : $scope.organizationId,
  						  "unitId" : $scope.unitId,
  						}
   	   				GenericService.serviceAction("POST", URI, data).then(
   	   					function (response) {
   	   		              $scope.confirmWithMobileOtpResponse = response.data.object.aadharPreverifiedJson;
   	   		              $scope.confirmWithMobileOtpResponseNew = JSON.parse($scope.confirmWithMobileOtpResponse);
		                  $scope.profilePhoto='data:image/png;base64,'+$scope.confirmWithMobileOtpResponseNew.profilePhoto;
		                  $scope.patientDetails=response.data.object.aadharPreverifiedJson;
   	   		              $("#confirmWithMobileOtp").modal(); 
   	   						$rootScope.stopLoader();
   	   					},
   	   					function errorCallback(response) {
   	   						$rootScope.stopLoader();
   	   					 growl.warning(response.data.message);
   	   					});
   				}
   				
   				if($scope.authOBJECT.authMode == 'MOBILE_OTP'){
   					
   					$rootScope.startLoader();
   	   				var URI = BASE_URL + ROOT_URL + "/api/abdm/confirmWithMobileOtp";

   	   				  var data= {
   	   					      "otp": $scope.search.searchByHealthOTP,
   	      	   		          "transactionId": $scope.initResponse.transactionId,
   	   						  "organizationId" : $scope.organizationId,
   	   						  "unitId" : $scope.unitId,
   	   						}
   	   				GenericService.serviceAction("POST", URI, data).then(function (response) {
	   						if(response.data.status=="success"){
	   	   					     $scope.confirmWithMobileOtpResponse = response.data.object.aadharPreverifiedJson;
		   		                 $scope.confirmWithMobileOtpResponseNew = JSON.parse($scope.confirmWithMobileOtpResponse);
		   		                    $scope.profilePhoto='data:image/png;base64,'+$scope.confirmWithMobileOtpResponseNew.profilePhoto;
		   		                    $scope.patientDetails=response.data.object.aadharPreverifiedJson;
		   		                 $("#confirmWithMobileOtp").modal(); 
		   						$rootScope.stopLoader();
	   	   						}else{
	   	   						   $rootScope.stopLoader();
	   	   						   growl.warning(response.data.message);
	   	   						}
	   	   					},
	   	   					function errorCallback(response) {
	   	   						$rootScope.stopLoader();
	   	   						growl.error(response.data.message);
	   	   					}
	);
   				}
   			
   			}  
                var d ="I here by declare that:";
                var l1="I am voluntarily sharing my Aadhaar Number / Virtual ID issued by the Unique Identification Authority of India (“UIDAI”), and my demographic information for the purpose of creating an";
                var l2="Ayushman Bharat Health Account number (“ABHA number”) and Ayushman Bharat Health Account address (“ABHA Address”) I authorize NHA to use my Aadhaar number / Virtual ID ";
                var l3="for performing Aadhaar based authentication with UIDAI as per the provisions of the Aadhaar (Targeted Delivery of Financial and other Subsidies,Benefits and Services) Act, 2016 for ";
                var l4="the aforesaid purpose. I understand that UIDAI will share my e-KYC details,or response of “Yes” with NHA upon successful authentication.";
                var l5="";
                
                var combinedText1 = d+ "\n" + l1 + "\n" + l2 + "\n" + l3 + "\n" + l4 + "\n" + l5;
                $scope.combinedText = $sce.trustAsHtml(combinedText1.replace(/\n/g, "<br>"));
                
               $scope.consents = [
                   { id: 'checkbox1', label: $scope.combinedText, checked: false },
                   { id: 'checkbox3', label: 'I consent to usage of my ABHA address and ABHA number for linking of my legacy (past)  health records and those which will be generated during this encounter.', checked: false },
                   { id: 'checkbox1', label: 'I authorize the sharing of all my health records with healthcare provider(s) for the purpose of providing healthcare services to me during this encounter.', checked: false },
                  // { id: 'checkbox1', label: 'I consent to the anonymization and subsequent use of my government health records for public health purposes.', checked: false },
                   //{ id: 'checkbox1', label: 'I, (bareficiary name), have been explained about the ccmsent as stated above and hereby vyovi& my the aforenxnticmed purposes.', checked: false },
                   { id: 'checkboxLast', label: 'I consent to the anonymization and subsequent use of my  health records for public health purposes.', checked: false, showChildCheckboxes: true, additionalConsent1: { checked: false }, additionalConsent2: { checked: false } },
                  // { id: 'checkbox1', label: 'I agree', checked: false }
                   ];

         
                 $scope.agreeChecked = false; 
                 $scope.isButtonDisabled = true; 
                 $scope.isResetButtonDisabled= true; 
                 
                 $scope.updateButtonState = function () {
                	 $scope.isButtonDisabled = !$scope.consents.every(consent => consent.checked);

                 $scope.isResetButtonDisabled = !$scope.consents.every(consent => consent.checked) || !$scope.consents[$scope.consents.length - 1].checked;
                 };      
                 
                 
                 $scope.showAdditionalCheckboxes = false;

                 $scope.handleCheckboxChange = function (consent) {
                	 
                	 if (consent.id === 'checkboxLast') {
                		 if (consent.id === 'checkboxLast') {
                		        consent.showChildCheckboxes = consent.checked;
                		    }
                	    }

                	    $scope.updateButtonState();
                 };
                    
               
            $scope.getShareDetailsd = function(){

    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/getShareDetails";

    				  var data={
    						  "organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						 $scope.shareList =[];
    		                 $scope.shareList = response.data.listObject;
    		             	$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
            $scope.getShareDetailsd();
            
            $scope.patientReqDetails = function(){

				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/abdm/getPatientReqDetails";

				  var data={
						  "organizationId" : $scope.organizationId,
						  "unitId" : $scope.unitId,
					}
				GenericService.serviceAction("POST", URI, data).then(
					function (response) {
						 $scope.getPatientReqDetails = [];
		                 $scope.getPatientReqDetails = response.data.listObject;
		             	$rootScope.stopLoader();
					},
					function errorCallback(response) {
						$rootScope.stopLoader();
						growl.error('Connection time out');
					});
			}
        $scope.patientReqDetails();
        
    		//ended abdm code  
            
            
            $scope.updateShareTable = function(){

				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/abdm/updateShareDetailsByTokenNumber";

				  var data={
						  "tokenNumber" :  $scope.tokenNumberShare,
					}
				GenericService.serviceAction("POST", URI, data).then(
					function (response) {
						 $scope.shareList =[];
		                 $scope.shareList = response.data.listObject;
		             	$rootScope.stopLoader();
					},
					function errorCallback(response) {
						$rootScope.stopLoader();
						growl.error('Connection time out');
					});
			}
            
            $scope.shareObject = null;
            $scope.fetchDataView=false;
            $scope.fetchModeForView = function(info){
                $scope.obj.sbxId = info.sbxId;
                $scope.shareObject = {};
                $scope.shareObject = JSON.parse(info.shareJson);
                $scope.tokenNumberShare = info.tokenNumber;
                console.log("shareJson",$scope.shareObject);
                $scope.patientDetails={};
                if($scope.shareObject!= null &&  $scope.shareObject!= undefined &&  $scope.shareObject!=""){
	                 $scope.patientDetails = $scope.shareObject.profile.patient;
	                 var addressh  = $scope.shareObject.profile.patient.address.line+" "+$scope.shareObject.profile.patient.address.district+" "+$scope.shareObject.profile.patient.address.state+" "+$scope.shareObject.profile.patient.address.pincode;
	                 $scope.patientDetails.address = $scope.shareObject.profile.patient.address.line;
	                 $scope.patientDetails.districtName = $scope.shareObject.profile.patient.address.district;
	                 $scope.patientDetails.ABHANumber = $scope.shareObject.profile.patient.abhaNumber;
	                 $scope.patientDetails.preferredAbhaAddress = $scope.shareObject.profile.patient.abhaAddress;
	                 $scope.patientDetails.mobile = $scope.shareObject.profile.patient.phoneNumber;
	                 $scope.patientDetails.stateName = $scope.shareObject.profile.patient.address.stateName;
	                 $scope.patientDetails.pincode = $scope.shareObject.profile.patient.address.pincode;
	               
	             	var name= $scope.patientDetails.name.split(' ');
	                 $scope.patientDetails.firstName = name[0];
	                 $scope.patientDetails.middleName = name[1];
	                 $scope.patientDetails.lastName = name[2];
	                 $scope.patientDetails.prefixList =$scope.prefixList;
	                 $scope.shareObject =null;
	                 $scope.updateShareTable();
	                 $("#pngCardPopUP").hide();
	                 $("#confirmPopup").hide();
   				     $(".modal-backdrop").hide();
   				     $state.go("patientRegistration",{
   					 patientRegistrationObjAbdm :$scope.patientDetails
						});
                }
                
            }
            
            $scope.searchPatient  = function(searchKeyword){
   			if(searchKeyword != undefined && searchKeyword != null && searchKeyword != ""){
   				$scope.patientList=[];
   				searchKeyword=searchKeyword;
   				var serData = {
   						organizationId : $scope.organizationId,
   						unitId : $scope.unitId,
   						searchKeyword : searchKeyword
   				}
   				var URI = BASE_URL + ROOT_URL + "/api/global/patientSearchPatientListForABDM";
   				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
   					if(response.data.status == "success"){
   						$scope.patientList = response.data.listObject;
   					}
   					return $scope.patientList;
   				});
   			}
   				
   			}
            
            $scope.link={};
            
            
            $scope.link.selectedEncounter = ""; 
            $scope.link.selectedAdmission="";
          //  $scope.encounter.encounterNumber= null;
            $scope.searchPatientNew  = function(searchKeyword){
       			if(searchKeyword != undefined && searchKeyword != null && searchKeyword != ""){
       				$scope.patientList=[];
       				searchKeyword=searchKeyword;
       				var serData = {
       						organizationId : $scope.organizationId,
       						unitId : $scope.unitId,
       						searchKeyword : searchKeyword
       				}
       				var URI = BASE_URL + ROOT_URL + "/api/global/patientSearchPatientListForABDM";
       				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
       					if(response.data.status == "success"){
       						$scope.patientList = response.data.listObject;
       					}
       					return $scope.patientList;
       				});
       			}
       				
       			}
            
            
            
            $scope.searchPatientDeepLink  = function(searchKeyword){
       			if(searchKeyword != undefined && searchKeyword != null && searchKeyword != ""){
       				$scope.patientList=[];
       				searchKeyword=searchKeyword;
       				var serData = {
       						organizationId : $scope.organizationId,
       						unitId : $scope.unitId,
       						searchKeyword : searchKeyword
       				}
       				var URI = BASE_URL + ROOT_URL + "/api/global/patientSearchPatientListForABDMForDeepLinking";
       				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
       					if(response.data.status == "success"){
       						$scope.patientList = response.data.listObject;
       					}
       					return $scope.patientList;
       				});
       			}
       				
       			}
            
            
            
            
            $scope.addSelectedPatient  = function(item,item1,item2){
            	$scope.linkCareContextObject =item;
            	
   				console.log("item",item);
   			}
            
            $scope.addSelectedPatientNew  = function(item,item1,item2){
            	$scope.linkCareContextObject =item;
            	$scope.patientId=item.patientId;
   				console.log("item",item);
   				$scope.dateAndTime ='';
   			}
            
            $scope.addSelectedPatientDeepLink  = function(item,item1,item2){
            	$scope.linkCareContextObject =item;
            	$scope.patientId=item.patientId;
            	$scope.mobileNumber=item.mobileNumber;
   				console.log("item",item);
   			}
            
            $scope.updateDropdowns = function() {
                if ($scope.selectedVisitType === 'OP') {
                    // Fetch encounter list if needed (already hardcoded here for example)
                	$scope.getEncounterNumberByPatientId($scope.patientId);
                	$scope.dateAndTime ='';
                	
                } else if ($scope.selectedVisitType === 'IP') {
                    // Fetch admission list if needed (already hardcoded here for example)
                	$scope.getAdmissionNumberByPatientId($scope.patientId);
                	$scope.dateAndTime ='';
                }
            };
            
            
            
            $scope.onEncounterChange = function(selectedEncounter) {
                console.log("Selected Encounter Changed: ", selectedEncounter);
            };

                
            	$scope.getEncounterNumberByPatientId = function(patientId) {
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/abdm/getEncounterNumberByPatientId";

				  var data={
						   organizationId : $scope.organizationId,
						   unitId : $scope.unitId,
						  "patientId" : $scope.patientId
					}
				GenericService.serviceAction("POST", URI, data).then(
					function (response) {
						 $scope.getEncounterNumberList = [];
		                 $scope.getEncounterNumberList = response.data.listObject;
		          console.log("encounterList",$scope.getEncounterNumberList);
		                 
		             	$rootScope.stopLoader();
					},
					function errorCallback(response) {
						$rootScope.stopLoader();
						growl.error('Connection time out');
					});
			}
            
            
            	$scope.getAdmissionNumberByPatientId = function(patientId) {
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/getAdmissionNumberByPatientId";

    				  var data={
    						  organizationId : $scope.organizationId,
   						   unitId : $scope.unitId,
   						  "patientId" : $scope.patientId
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						 $scope.getAdmissionNumberList = [];
    		                 $scope.getAdmissionNumberList = response.data.listObject;
    		             	$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
            
            
            	 $scope.notifyToABDM = function(){
              	   if($scope.mobileNumber != undefined && $scope.mobileNumber != null){
       				$rootScope.startLoader();
       				
       				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/link/patient/links/sms/notify2";
       				$scope.mobile="";
       				 var data={
        					    "mobileNumber" : $scope.mobileNumber,
        					    "organizationId" : $scope.organizationId,
    						    "unitId" : $scope.unitId,
        					    "eventId":31
        					}
       				GenericService.serviceAction("POST", URI, data).then(
       					function (response) {
       						if (response.data.status == "success") {
       							growl.success("Message send succesfully on aadhar linked mobile number");
       							 $rootScope.stopLoader();
       						}
      		    				else if(response.data.status == "error")
      		    					{
      		    					   $rootScope.stopLoader();
      			   	   						$rootScope.stopLoader();
      			   	   						growl.error(response.data.message);
      		    					}
       					
       					},
       					function errorCallback(response) {
       						$rootScope.stopLoader();
       						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
       					});
              	   }else{
              		   growl.error("Please select patient and mobile number is required !!!");
              	   }
       			}
            	
            	
            
			
			$scope.alreadyAbha = 0;
		    //initializeController();
				if (cookieObject == undefined) {
				$state.go('login');
				return;
				}
		    $scope.usingAadhaar = 0;
		    $scope.timer = 0;    
		    
		 // The multiplication table
		    var d = [
		    [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
		    [1, 2, 3, 4, 0, 6, 7, 8, 9, 5],
		    [2, 3, 4, 0, 1, 7, 8, 9, 5, 6],
		    [3, 4, 0, 1, 2, 8, 9, 5, 6, 7],
		    [4, 0, 1, 2, 3, 9, 5, 6, 7, 8],
		    [5, 9, 8, 7, 6, 0, 4, 3, 2, 1],
		    [6, 5, 9, 8, 7, 1, 0, 4, 3, 2],
		    [7, 6, 5, 9, 8, 2, 1, 0, 4, 3],
		    [8, 7, 6, 5, 9, 3, 2, 1, 0, 4],
		    [9, 8, 7, 6, 5, 4, 3, 2, 1, 0]
		    ];
		    // permutation table p
		    var p = [
		    [0, 1, 2, 3, 4, 5, 6, 7, 8, 9],
		    [1, 5, 7, 6, 2, 8, 3, 0, 9, 4],
		    [5, 8, 0, 3, 7, 9, 6, 1, 4, 2],
		    [8, 9, 1, 6, 0, 4, 3, 5, 2, 7],
		    [9, 4, 5, 3, 1, 2, 6, 8, 7, 0],
		    [4, 2, 8, 6, 5, 7, 3, 9, 0, 1],
		    [2, 7, 9, 3, 8, 0, 6, 4, 1, 5],
		    [7, 0, 4, 6, 9, 1, 3, 2, 5, 8]
		    ];
		    // inverse table inv
		    var inv = [0, 4, 3, 2, 1, 5, 6, 7, 8, 9];
		    
		 // generates checksum
		    function generate(array) {
		    var c = 0;
		    var invertedArray = array.reverse();
		    for (var i = 0; i < invertedArray.length; i++) {
		    c = d[c][p[((i + 1) % 8)][invertedArray[i]]];
		    }
		    return inv[c];
		    }
		   
		       $scope.next = function(clickTab){
		    	   
		    	   if(clickTab == "createAbhadivChack"){
		    		   
		    		   if($scope.aadhar.myAadharNumber == null || $scope.aadhar.myAadharNumber == undefined || $scope.aadhar.myAadharNumber == ''){
	    				    $rootScope.stopLoader();
							growl.warning('Please Enter Aadhaar Card Number');
							return false;
	    			  }
	    			  
	    			   if($scope.aadhar.myAadharNumber.length < 12  ){
	   				    $rootScope.stopLoader();
							growl.warning('Please Enter 12 Digit Aadhaar Card Number');
							return false;
	   			       }
	    				  if ($scope.aadhar.myAadharNumber.match(/[^$,.\d]/)) {
	     				  growl.warning('Special characters allowed: 1 dot (.) and/or 1 underscore (_)');
	     				  return false;
	     			      }
	    				  
	    				  var aadhaarArray = $scope.aadhar.myAadharNumber.split('');
	    				  var toCheckChecksum = aadhaarArray.pop();
	    				  
	    				  if (generate(aadhaarArray) == toCheckChecksum) {
	    					  } else {
	    						 growl.warning('Aadhaar Card Number Not Valid ');
	    	     				  return false;
	    					  }
		    		   $scope.createAbhadiv = false;
		    		   $scope.createOTP = true;
		    		   $scope.saveData();
		    		   
		          	 }
		    	   if(clickTab =="createOTPCheck"){
		    		   $scope.verifyOTP();
		          	 }
		    	   
		    	   if(clickTab =="createOTPForUnlinkedCheck"){
		    		   $scope.createOTPForUnlinked = false;
		    		   $scope.enterMobileNumber = false;
		    		   $scope.verifyMobileOTPNotLinkedToAadhar();
		          	 }
		    	   
		    	   if(clickTab =="enterMobileNumber"){
//		    		   $scope.enterMobileNumber = false;
		    		   $scope.checkAndGenerateMobileOTPFunction();
		    		   
		    		  
		          	 }
		    	   
		    	   if(clickTab =="addressAbhaCheck"){
		    		   $scope.addressAbha = false;
		    		   $scope.authontification = true;
		    		   $scope.fetchMode();
		          	 }
		    	   
		    	   if(clickTab =="authontificationCheck"){
		    		   $scope.authontification = false;
		    		   $scope.enterOtp = true;
		    		   $scope.initMode();
		          	 }
		    	   
		    	   if(clickTab =="enterOtpCheck"){
		    		   $scope.enterOtp = false;
		    		   $scope.addressAbha = true;
		    		   $scope.callConfirm();
		          	 }
		      }
		       
		       $scope.downloadImage = function () {
		    	   var specificAnchor = document.getElementById("link1");
		    	   specificAnchor.href = $scope.pngCard;
		    	   specificAnchor.id = "link1";
		    	   specificAnchor.download = "image.png";
		    	   specificAnchor.click();
		    	 };
		    	 
        initializeController();

        //    UICode
                 $scope.mainTab=true;
                 $scope.usingAadhar=false;
                 $scope.usingDriving=false;
                 $scope.aadhaarStepper=false;
                 $scope.drivingStepper=false;
                 $scope.usingAadharOP=false;
                 $scope.otpMobile=false;
                 $scope.profileDetails=false;


                 $scope.usingAadharBtn = function(flag){
                   if(flag==true){
                   $scope.usingAadhar=true;
                   $scope.usingDriving=false;
                   }
                   else{
                   $scope.usingAadhar=false;
                   $scope.usingDriving=false;
                   }

                 }

                  $scope.usingDrivingBtn = function(flag){
                   if(flag==true){
                   $scope.usingDriving=true;
                   $scope.usingAadhar=false;
                   }
                   else{
                   $scope.usingAadhar=false;
                   $scope.usingDriving=false;
                   }
                 }

                 $scope.nextPage = function(){
                   if($scope.usingAadhar==true){
                    $scope.aadhaarStepper=true;
                    $scope.usingAadharOP=true;
                    $scope.usingDriving=false;
                    $scope.mainTab=false;
                     if($scope.usingAadharOP==true && $scope.aahaarNumber!=undefined && $scope.aahaarNumber!=null){

                     }

                   }
                    else if($scope.usingDriving==true){
                      $scope.aadhaarStepper=false;
                      $scope.drivingStepper=true;
                      $scope.mainTab=false;
                    }
                   else{
                   $scope.aadhaarStepper=false;
                   $scope.usingDriving=false;
                   }
                 }
                 $scope.BackPage =function(){
                    $scope.mainTab=true;
                    $scope.aadhaarStepper=false;
                    $scope.drivingStepper=false;
                    $scope.usingAadharOP=false;


                 }

               $scope.ConsentComplete = function(){
                   $scope.otpMobile=true;
                   $scope.usingAadharOP=false;

                   $scope.getClassforColor1 = function(){
                                                return "completed";
                                               }
                 }

                 $scope.ConsentBack = function(){
                   $scope.otpMobile=false;
                   $scope.usingAadharOP=true;
                 }
                 $scope.otpBack = function(){
                   $scope.otpMobile=true;
                   $scope.usingAadharOP=false;
                   $scope.profileDetails=false;
                 }


                 $scope.otpMobileFun = function(){
                  $scope.profileDetails=true;
                  $scope.otpMobile=false;
                  $scope.usingAadharOP=false;
                   $scope.getClassforColor2 = function(){
                       return "completed";
                      }
                 }
                 $scope.profileDetailsFun =function(){
                       $scope.profileDetails=false;
                       $scope.otpMobile=false;
                       $scope.usingAadharOP=false;
                     $scope.getClassforColor3 = function(){
                                        return "completed";
                                       }
                 }
                 
                 $scope.verifyObj = {
                		 "fetchById" : "1",
                		 "fetchByIdMobile" : "2",
                		 "data" : "",
                		 "dataMobile" : "",
                		 "loginOtpVerification":"",
                		 "otpInput":"",
                		 "ABHANumber":""
                			 
                 }
            	 $scope.firstSlideABHAddress=true;
        		 $scope.secondSlideABHAddress=false;
                 $scope.clickSearch =function() {
                		 $scope.firstSlideABHAddress=false;
                		 $scope.secondSlideABHAddress=true;
                			 
                 }
                 
                 
                 $scope.verifyObjABHAAddAndNum = {
                		 "fetchById" : "4",
                		 "fetchByIdV3" : "3",
                		 "fetchByIdMobile" : "2",
                		 "data" : "",
                		 "dataMobile" : "",
                		 "dataV3" : "",
                		 "otpInputV3" : "",
                		 "loginOtpVerification":"",
                		 "loginOtpVerificationV3":"",
                		 "loginOtpVerificationDescV3":"",
                		 "otpInput":""
                			 
                 }
                 
                 
                 $scope.verifyObjBack = function() {
                	 $scope.verifyObj = {
                    		 "fetchById" : "1",
                    		 "fetchByIdMobile" : "2",
                    		 "dataMobile" : "",
                    		 "data" : "",
                    		 "loginOtpVerification":"",
                    		 "otpInput":"",
                    		 "ABHANumber":""
                    			 
                     }
                	  $scope.init();
                 }
                 
                 $scope.verifyObjABHAAddAndNumBack = function() {
                	    $scope.verifyObjABHAAddAndNum = {
			                		 "fetchById" : "4",
			                		 "fetchByIdMobile" : "2",
			                		 "fetchByIdV3" : "3",
			                		 "data" : "",
			                		 "dataV3" : "",
			                		 "otpInputV3" : "",
			                		 "loginOtpVerification":"",
			                		 "loginOtpVerificationV3":"",
			                		 "loginOtpVerificationDescV3":"",
			                		 "otpInput":""
			                			 
			                 }

                   	 $scope.createAbhass = true;
                   	 $scope.createAbhadiv = false;
                   	 $scope.createOTP = false;
                   	 $scope.enterMobileNumber = false;
                        $scope.enterOtp = false;
                        $scope.addressAbha = true;
                        $scope.modesAbha = false;
                        $scope.authontification = false;
                        $scope.demograpgyAbha = false;
                        $scope.authModeDroDown = "4";
                        $scope.createAbha = '0';
                        $scope.confirmOTP = '';
                        $scope.gender = '0';
                        $scope.dateOfBirth = '';
                        $scope.mobileNumber = '';
                        $scope.searchHealthid = true;
                        $scope.searchOTPVerification = false;
                        $scope.searchOTPAuthentication = false;
                        $scope.isButtonDisabled = true;
                        $scope.userName = cookieObject.userName;
                        $scope.verifyAadharOrMobile = true;
                        $scope.verifyOtpForLoginDiv = false;
                        $scope.verifyABHAaddressAndNumber = true;
                        $scope.abbhaAddressVerficationofV3 = true;
                        $scope.verifyOtpForLoginABHAaddressAndNumber = false;
                        $scope.abbhaAddressVerficationofSearchSlide = true;
                        $scope.abbhaAddressVerficationofV3AuthMode = false;
                        $scope.firstSlideABHAddress= true;
                        $scope.secondSlideABHAddress  = false;
                        $scope.abhaVerificationDiv = false;
                        $scope.verifyObj = {}
                        if ($scope.consents && Array.isArray($scope.consents)) {
                            // Reset the checkboxes by setting the 'checked' property to false for each consent
                            $scope.consents.forEach(function (consent) {
                              consent.checked = false;
                              if(consent.id == "checkboxLast") {
                           	   consent.additionalConsent1.checked = false;
                           	   consent.additionalConsent2.checked = false;
                              }
                            });
                          }
                        
                    	$scope.obj={
           					"sbxId":"",
           					"sbxIdNew":""
           			}
                    	
                    	$scope.createOTPObject={
           					"otpInput":"",
           					"mobileInput":"",
           					"verifyotpInput":"",
           					"profileDetails":""
           			};
                    	$scope.enterOtpObject={
           					"otpInput":"",
           					"mobileInput":""
           			};
                    	
                   	$scope.authOBJECT={
           					"confirmOTP":"",
           					"suggestions":""
           			};
                   	
                   	$scope.search={
           					"searchByHealthOTP":""
           			};
                   	
                   	$scope.aadhar={
           					"myAadharNumber":""
           			};
                   	  $scope.verifyObj = {
                        		 "fetchById" : "1",
                        		 "fetchByIdMobile" : "2",
                        		 "data" : "",
                        		 "dataMobile" : "",
                        		 "loginOtpVerification":"",
                        		 "otpInput":"",
                        		 "ABHANumber":""
                        			 
                         }
                    	 
                   
                	 //   $scope.init();
                 }
                 
                 $scope.resetABHAAddAndNumBack = function() {
                	 $scope.verifyObjABHAAddAndNum = {
                    		 "fetchById" : $scope.verifyObjABHAAddAndNum.fetchById,
                    		 "data" : "",
                    		 "loginOtpVerification":"",
                    		 "otpInput":""
                    			 
                     }
                 }
                 
                 
             	$scope.retryAttemptsResendOtp = 1;
     			$scope.fetchDetailsForLoginResendOtp = function(){
     				
     				if ($scope.verifyObj.fetchById == 0) {
     					growl.error("Please Select Login Criteria ...");
 						return false;
     				}
     				if ($scope.retryAttemptsResendOtp <= 2) {
     					console.log("retry attempt : " + $scope.retryAttemptsResendOtp);
     					$scope.retryAttemptsResendOtp++;
 					} else {
 						growl.error("Max resend limit reached.Please try again !!");
 						return false;
 					}
       				$rootScope.startLoader();
       				var URI = BASE_URL + ROOT_URL + "/api/abdm/login/otp";

       				  var data = {
       						  "organizationId" : $scope.organizationId,
       						  "unitId" : $scope.unitId,
       						  "aadharNumber" : $scope.verifyObj.data,
       						  "mobileNumber" : $scope.verifyObj.data,
       						  "fetchById" : $scope.verifyObj.fetchById,
       						  "eventId":14
       						}
       				GenericService.serviceAction("POST", URI, data).then(
       					function (response) {
       						if(response.data.status =="success" ){
       							
       							$scope.requestOtpObjectLoginOTP = JSON.parse(response.data.object);
       							$scope.verifyAadharOrMobile = false;
       							$scope.verifyOtpForLoginDiv = true;
       							$scope.timer = 60;
       							timerPromise = $interval(countdown, 1000, $scope.timer);
       						    growl.success($scope.requestOtpObjectLoginOTP.message);
       		                    console.log("response transaction ID : ",response.data.object);
       		                        		                 
       						$rootScope.stopLoader();
       						}else{
       							$rootScope.stopLoader();
           						growl.error(response.data.message);
       						}
       					},
       					function errorCallback(response) {
       						$rootScope.stopLoader();
       						growl.error('Connection time out');
       					});
       			}
                 
                 
     			 $scope.isValidMobileNumber = function() {
     			    var indianMobileRegex = /^([7-9]{1})[0-9]{9}$/;
     			    return indianMobileRegex.test($scope.verifyObj.data);
     			  };
     			  
                 $scope.fetchDetailsForLogin = function(){
                	 var test = true;
                	 const userAnswer = parseInt($scope.verifyObj.userAnswer, 10);
                     const correctAnswer = calculateAnswer($scope.captcha);
                     if (userAnswer === correctAnswer) {
                         $scope.resultMessage = 'Correct!';
                     } else {
                         $scope.resultMessage = 'Incorrect. Try again.';
                         $scope.captcha = generateCaptcha();
                         $scope.verifyObj.userAnswer = '';
                         growl.warning('Please enter a valid captcha result. Entered captcha result is incorrect. ');
                         return false;// regenerate captcha if incorrect
                     }
                     $scope.verifyObj.userAnswer = '';
                     
                	 if($scope.verifyObj.fetchById == 1){
                		  var aadhaarArray = $scope.verifyObj.data.split('');
	    				  var toCheckChecksum = aadhaarArray.pop();
	    				  
	    				  if (generate(aadhaarArray) == toCheckChecksum) {
	    					  } else {
	    						 growl.warning('Aadhaar Number is not valid ');
	    	     				  return false;
	    					  }
                	 }
                	 
                	 if($scope.verifyObj.fetchById == 2){
            		   if($scope.verifyObj.data == undefined || $scope.verifyObj.data== null || $scope.verifyObj.data ==""){
            				growl.error(" Please enter valid mobile number",{
            					title : ERROR_MSG
            					});
            					return false;
            			}
            		   var indianMobileRegex =  /^([6-9]{1})[0-9]{9}$/;
        			     test=indianMobileRegex.test($scope.verifyObj.data);
        			    
        			    if(test == false){
               				growl.error("Please enter valid mobile number",{
               					title : ERROR_MSG
               					});
               					return false;
               			}
        			    
            		   if($scope.verifyObj.data.length < 10 ){
           				growl.error(" Please enter valid mobile number",{
           					title : ERROR_MSG
           					});
           					return false;
           			}
               	     }
                	 
                	 if($scope.verifyObj.fetchById == 3){
              		   if($scope.verifyObj.data == undefined || $scope.verifyObj.data== null || $scope.verifyObj.data ==""){
              				growl.error(" Please enter valid ABHA Address",{
              					title : ERROR_MSG
              					});
              					return false;
              			}
                 	     }
                	 
                	 if($scope.verifyObj.fetchById == 4){
              		   if($scope.verifyObj.data == undefined || $scope.verifyObj.data== null || $scope.verifyObj.data ==""){
              				growl.error(" Please enter valid ABHA Number",{
              					title : ERROR_MSG
              					});
              					return false;
              			}
                 	   }
                	 
       				$rootScope.startLoader();
       				var URI = BASE_URL + ROOT_URL + "/api/abdm/login/otp";

       				  var data = {
       						  "organizationId" : $scope.organizationId,
       						  "unitId" : $scope.unitId,
       						  "aadharNumber" : $scope.verifyObj.data,
       						  "mobileNumber" : $scope.verifyObj.data,
       						  "fetchById" : $scope.verifyObj.fetchById,
       						  "loginOtpVerification" : $scope.verifyObj.loginOtpVerification,
       						  "eventId":51
       						}
       				GenericService.serviceAction("POST", URI, data).then(
       					function (response) {
       						if(response.data.status =="success" ){
       						    $scope.verifyObj.userAnswer = '';
       							$scope.requestOtpObjectLoginOTP = JSON.parse(response.data.object);
       							$scope.verifyAadharOrMobile = false;
       							$scope.verifyOtpForLoginDiv = true;
       							$scope.timer = 60;
       							timerPromise = $interval(countdown, 1000, $scope.timer);
       						    growl.success($scope.requestOtpObjectLoginOTP.message);
       						   $scope.captcha = generateCaptcha();
       		                    console.log("response transaction ID : ",response.data.object);
       		                        		                 
       						$rootScope.stopLoader();
       						}else{
       							$rootScope.stopLoader();
        						growl.error(response.data.message);
        						$scope.verifyAadharOrMobile = false;
       							$scope.verifyOtpForLoginDiv = false;
        						$scope.createNewAccount =true;
       						}
       					},
       					function errorCallback(response) {
       						$rootScope.stopLoader();
       						growl.error('Connection time out');
       					});
       			}
                
                 
                 $scope.searchAbhaNumberByMobileNumber = function(){
                	 var test = true;
                	 if($scope.verifyObj.fetchByIdMobile == 2){
            		   if($scope.verifyObj.dataMobile == undefined || $scope.verifyObj.dataMobile== null || $scope.verifyObj.dataMobile ==""){
            				growl.error(" Please enter valid mobile number",{
            					title : ERROR_MSG
            					});
            					return false;
            			}
            		   var indianMobileRegex =  /^([6-9]{1})[0-9]{9}$/;
        			     test=indianMobileRegex.test($scope.verifyObj.dataMobile);
        			    
        			    if(test == false){
               				growl.error("Please enter valid mobile number",{
               					title : ERROR_MSG
               					});
               					return false;
               			}
        			    
            		   if($scope.verifyObj.dataMobile.length < 10 ){
           				growl.error(" Please enter valid mobile number",{
           					title : ERROR_MSG
           					});
           					return false;
           			}
               	     }
                	 
           
                	 
       				$rootScope.startLoader();
       				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/profile/account/abha/search";

       				  var data = {
       						  "organizationId" : $scope.organizationId,
       						  "unitId" : $scope.unitId,
       						  "aadharNumber" : $scope.verifyObj.dataMobile,
       						  "mobileNumber" : $scope.verifyObj.dataMobile,
       						  "fetchById" : $scope.verifyObj.fetchByIdMobile,
       						  "loginOtpVerification" : $scope.verifyObj.loginOtpVerification,
       						  "eventId":50
       						}
       				GenericService.serviceAction("POST", URI, data).then(
       					function (response) {
       						if(response.data.status =="success" ){
       						    $scope.searchABHANbumberByMobileNumberObject = [];
    						    $scope.searchABHANbumberByMobileNumber = [];
    							$scope.searchABHANbumberByMobileNumberObject = JSON.parse(response.data.object);
								$scope.searchABHANbumberByMobileNumber = $scope.searchABHANbumberByMobileNumberObject[0].ABHA;
       						    $("#loginVerifyV3Mobile").modal("show"); 
       						 
       		                    console.log("response transaction ID : ",response.data.object);
       		                        		                 
       						$rootScope.stopLoader();
       						}else{
       							$rootScope.stopLoader();
        						growl.error(response.data.message);
        						$scope.verifyAadharOrMobile = false;
       							$scope.verifyOtpForLoginDiv = false;
        						$scope.createNewAccount =true;
       						}
       					},
       					function errorCallback(response) {
       						$rootScope.stopLoader();
       						growl.error('Connection time out');
       					});
       			}
                 
                 $scope.verifyOtpObject = {
                		 "otpInput" : '',
                 }
                 
                 $scope.verifyOtpForLoginNewwwww = function(){
                	 $rootScope.startLoader();
                	  
                	 if($scope.verifyOtpObject.otpInput =='' || $scope.verifyOtpObject.otpInput.length < 6){
                		 growl.error("OTP enterd is incorrect");
							$rootScope.stopLoader();
							return false;
                	 }
                	 
        				var URI = BASE_URL + ROOT_URL + "/api/abdm/login/verify";

        				  var data = {
        						  "organizationId" : $scope.organizationId,
        						  "unitId" : $scope.unitId,
        						  "transactionId" : $scope.requestOtpObjectLoginOTP.txnId,
        						  "otp" : $scope.verifyOtpObject.otpInput,
        						  "fetchById" : 2,
        						  "eventId":15
        						}
        				GenericService.serviceAction("POST", URI, data).then(
        					function (response) {
        						if( response.data.status =="success" ){
        							$scope.verifyOtpForLoginDiv = false;
        							$scope.loginVerifyOTPObject = JSON.parse(response.data.object.aadharPreverifiedJson);
        							$scope.allVerifyOTPObject = JSON.parse(response.data.object.shareJson);
        		                    console.log("aadharPreverifiedJson : ",$scope.loginVerifyOTPObject);
        		                    
        		                    localStorage.setItem('user', JSON.stringify($scope.loginVerifyOTPObject));
              		                $scope.user=JSON.parse(localStorage.getItem("user"));
              		                 console.log("localStorageJson is : ",$scope.user);
              		                 
              		               if ($scope.user != undefined) {
	              		               angular.forEach($scope.user.list, function(item) {
              		            	    console.log(item.profilePhoto);
              		            	});
              		               }

              		             angular.forEach($scope.loginVerifyOTPObject , function(v){
                            			 v.profilePhotoNew='data:image/png;base64,'+v.profilePhoto;
                            	 });
              		                 
        		                    
        		                    if($scope.loginVerifyOTPObject != null && $scope.loginVerifyOTPObject.length>0){
        		                    	$scope.patientDetails=$scope.loginVerifyOTPObject[0];
            		                    $scope.profilePhoto='data:image/png;base64,'+$scope.loginVerifyOTPObject[0].profilePhoto;
            		                    $scope.enrollmentEnrolByAadhaarObject={};
            		                    $scope.enrollmentEnrolByAadhaarObject.tokens ={};
            		                    $scope.enrollmentEnrolByAadhaarObject.tokens.token = response.data.object.token;
            		                    $scope.enrollmentEnrolByAadhaarObject.txnId = response.data.object.transactionId;
            		                    $scope.txnIdSDSS = response.data.object.transactionId;
            		                    growl.success($scope.allVerifyOTPObject.message);
            		                   
            		                    if($scope.verifyObj.fetchById ==1){
            		                    	 $scope.profileAccountforShow();
            		                    	  $("#loginVerifyV3").modal("show"); 
            		                    }
            		                    if($scope.verifyObj.fetchById ==2){
          		                    	  $("#loginVerifyV3Mobile").modal("show"); 
          		                        }
        		                    }
        		                    
        						    $rootScope.stopLoader();
        						}else{
        							
            						if(response.data.message != "Not Implemented"){
            							$rootScope.stopLoader();
                						//growl.error(response.data.message);
            							
            							if(response.data.message == "Not Found"){
            								$scope.createNewAccount =true;
            							}
            							else{
                						growl.error(response.data.message);
                						}
                						
           							}else{
           								growl.error(response.data.message);
           								$rootScope.stopLoader();
           							}
            						
        						}
        					},
        					function errorCallback(response) {
        						$rootScope.stopLoader();
        						growl.error('Connection time out');
        					});
                 }
                 
                 
                 
                 $scope.verifyOtpForLogin = function(){
                	 $rootScope.startLoader();
                	  
                	 if($scope.verifyOtpObject.otpInput =='' || $scope.verifyOtpObject.otpInput.length < 6){
                		 growl.error("OTP enterd is incorrect");
							$rootScope.stopLoader();
							return false;
                	 }
                	 
        				var URI = BASE_URL + ROOT_URL + "/api/abdm/login/verify";

        				  var data = {
        						  "organizationId" : $scope.organizationId,
        						  "unitId" : $scope.unitId,
        						  "transactionId" : $scope.requestOtpObjectLoginOTP.txnId,
        						  "otp" : $scope.verifyOtpObject.otpInput,
        						  "fetchById" : $scope.verifyObj.fetchById,
        						  "eventId":15
        						}
        				GenericService.serviceAction("POST", URI, data).then(
        					function (response) {
        						if( response.data.status =="success" ){
        							$scope.verifyOtpForLoginDiv = false;
        							$scope.loginVerifyOTPObject = JSON.parse(response.data.object.aadharPreverifiedJson);
        							$scope.allVerifyOTPObject = JSON.parse(response.data.object.shareJson);
        		                    console.log("aadharPreverifiedJson : ",$scope.loginVerifyOTPObject);
        		                    
        		                    localStorage.setItem('user', JSON.stringify($scope.loginVerifyOTPObject));
              		                $scope.user=JSON.parse(localStorage.getItem("user"));
              		                 console.log("localStorageJson is : ",$scope.user);
              		                 
              		               if ($scope.user != undefined) {
	              		               angular.forEach($scope.user.list, function(item) {
              		            	    console.log(item.profilePhoto);
              		            	});
              		               }

              		             angular.forEach($scope.loginVerifyOTPObject , function(v){
                            			 v.profilePhotoNew='data:image/png;base64,'+v.profilePhoto;
                            	 });
              		                 
        		                    
        		                    if($scope.loginVerifyOTPObject != null && $scope.loginVerifyOTPObject.length>0){
        		                    	$scope.patientDetails=$scope.loginVerifyOTPObject[0];
            		                    $scope.profilePhoto='data:image/png;base64,'+$scope.loginVerifyOTPObject[0].profilePhoto;
            		                    $scope.enrollmentEnrolByAadhaarObject={};
            		                    $scope.enrollmentEnrolByAadhaarObject.tokens ={};
            		                    $scope.enrollmentEnrolByAadhaarObject.tokens.token = response.data.object.token;
            		                    $scope.enrollmentEnrolByAadhaarObject.txnId = response.data.object.transactionId;
            		                    $scope.txnIdSDSS = response.data.object.transactionId;
            		                    growl.success($scope.allVerifyOTPObject.message);
            		                   
            		                    if($scope.verifyObj.fetchById ==1){
            		                    	 $scope.profileAccountforShow();
            		                    	  $("#loginVerifyV3").modal("show"); 
            		                    }
            		                    if($scope.verifyObj.fetchById ==2){
          		                    	  $("#loginVerifyV3Mobile").modal("show"); 
          		                        }
        		                    }
        		                    
        						    $rootScope.stopLoader();
        						}else{
        							
            						if(response.data.message != "Not Implemented"){
            							$rootScope.stopLoader();
                						//growl.error(response.data.message);
            							
            							if(response.data.message == "Not Found"){
            								$scope.createNewAccount =true;
            							}
            							else{
                						growl.error("OTP enterd is incorrect");
                						}
                						
           							}else{
           								growl.error("OTP enterd is incorrect");
           								$rootScope.stopLoader();
           							}
            						
        						}
        					},
        					function errorCallback(response) {
        						$rootScope.stopLoader();
        						growl.error('Connection time out');
        					});
                 }
                 
                 
                 
                 $scope.verifyUser = function(id){
					 $scope.verifyObj.ABHANumber =id;
                	 $rootScope.startLoader();
                	 
                	 angular.forEach($scope.loginVerifyOTPObject , function(v){
                		 if(v.preferredAbhaAddress == $scope.verifyObj.ABHANumber){
                			 $scope.verifyObj.ABHANumber = v.ABHANumber;
                			 $scope.preferredAbhas=v.preferredAbhaAddress;
                			 $scope.profilePhoto='data:image/png;base64,'+v.profilePhoto;
                			 $scope.patientDetails.profilePhoto='data:image/png;base64,'+v.profilePhoto;
                		 }
                	 });
        				var URI = BASE_URL + ROOT_URL + "/api/abdm/login/verify/user";

        				  var data = {
        						  "organizationId" : $scope.organizationId,
        						  "unitId" : $scope.unitId,
        						  "transactionId" : $scope.txnIdSDSS,
        						  "token" : $scope.enrollmentEnrolByAadhaarObject.tokens.token,
        						  "abhaNumber":$scope.verifyObj.ABHANumber,
        						  "fetchById" : $scope.verifyObj.fetchById,
        						  "eventId":17
        						}
        				  
        				GenericService.serviceAction("POST", URI, data).then(
        					function (response) {
        						if( response.data.status =="success" ){
        							$scope.verifyOtpForLoginDiv = false;
        							$scope.loginVerifyUserObject = JSON.parse(response.data.object);
        		                    console.log("loginVerifyUserObject : ",$scope.loginVerifyUserObject);
        		                    
        		                    if($scope.loginVerifyUserObject != null && $scope.loginVerifyOTPObject.length>0){
        		                    	$scope.enrollmentEnrolByAadhaarObject.tokens.token = $scope.loginVerifyUserObject.token;
        		                    	$scope.profileAccountforShow();
        		                    	 $("#loginVerifyV3").modal("show"); 
        		                    }else{
        		                    	// create new account
        		                    	$scope.createNewAccount =true;
        		                    }
        		                    
        						    $rootScope.stopLoader();
        						}else{
        							$rootScope.stopLoader();
            						growl.error(response.data.message);
        						}
        					},
        					function errorCallback(response) {
        						$rootScope.stopLoader();
        						growl.error('Connection time out');
        					});
                 }
                 
                 $scope.profileAccountforShow = function(){
     				$rootScope.startLoader();
                     var URI = BASE_URL + ROOT_URL + "/api/abdm/profile/account";
     				$scope.mobile="";
     				 var data={
     						 "token":$scope.enrollmentEnrolByAadhaarObject.tokens.token,
     						 "transactionId":$scope.enrollmentEnrolByAadhaarObject.txnId,
     						  "organizationId" : $scope.organizationId,
     						  "unitId" : $scope.unitId,
     						  "transactionType":"Verification",
     						  "eventId" :8
     					}
     				GenericService.serviceAction("POST", URI, data).then(
     					function (response) {
     						if (response.data.status == "success") {
     							$scope.patientDetails = JSON.parse(response.data.object);
     							console.log("patientDetails",$scope.patientDetails);
     						}
    		    				else if(response.data.status == "error")
    		    					{
    		    					   $rootScope.stopLoader();
    			   	   						$rootScope.stopLoader();
    			   	   						growl.error(response.data.message);
    		    					}
     					
     					},
     					function errorCallback(response) {
     						$rootScope.stopLoader();
     						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
     					});
     			}
                 
                 
                 $scope.profileAccountVerify = function(){
    				$rootScope.startLoader();
                    var URI = BASE_URL + ROOT_URL + "/api/abdm/profile/account";
    				$scope.mobile="";
    				 var data={
    						 "token":$scope.enrollmentEnrolByAadhaarObject.tokens.token,
    						 "transactionId":$scope.enrollmentEnrolByAadhaarObject.txnId,
    						  "organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    						  "transactionType":"Verification",
    						  "eventId" :8
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						if (response.data.status == "success") {
    							$scope.profileAccountObject = JSON.parse(response.data.object);
    							console.log("$scope.profileAccountObject",$scope.profileAccountObject);
    							console.log("/profile/account",$scope.profileAccountObject);
    							$scope.verifyOtpForLoginDiv = false;
    							$scope.viewProfile();
    						}
   		    				else if(response.data.status == "error")
   		    					{
   		    					   $rootScope.stopLoader();
   			   	   						$rootScope.stopLoader();
   			   	   						growl.error(response.data.message);
   		    					}
    					
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
    					});
    			}
              
               $scope.getAccountgetCard = function(){
            	   
             	$rootScope.startLoader();
             	var URI = BASE_URL + ROOT_URL + "/api/abdm/account/getCard";
   				$scope.mobile="";
   				 var data={
   						  "healthId":$scope.enrollmentEnrolByAadhaarObject.txnId,
   						  "organizationId" : $scope.organizationId,
   						  "unitId" : $scope.unitId,
   						  "eventId" :13
   					}
   				GenericService.serviceAction("POST", URI, data).then(
   					function (response) {
   						if (response.data.status == "success") {
   							$scope.getAccountgetCardObject = JSON.parse(response.data.object);
   							console.log("$scope.getAccountgetCardObject",$scope.getAccountgetCardObject);
   							$scope.verifyOtpForLoginDiv = false;
   							$scope.viewProfile();
   						}
  		    				else if(response.data.status == "error")
  		    					{
  		    					   $rootScope.stopLoader();
  			   	   						$rootScope.stopLoader();
  			   	   						growl.error(response.data.message);
  		    					}
   					
   					},
   					function errorCallback(response) {
   						$rootScope.stopLoader();
   						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
   					});
   			}
    
                           
               // Verification of ABHA aadress and Abha Number 
             	$scope.retryATTE = 1;
               $scope.verifyObjABHAAddAndNumResendOtp = function(){
            	   if ($scope.retryATTE <= 2) {
    					console.log("retry attempt : " + $scope.retryATTE);
    					$scope.retryATTE++;
					} else {
						growl.error("Max resend limit reached.Please try again !!");
						return false;
					}
            
                	 
       				$rootScope.startLoader();
       				var URI = BASE_URL + ROOT_URL + "/api/abdm/login/otp";

       				  var data = {
       						  "organizationId" : $scope.organizationId,
       						  "unitId" : $scope.unitId,
       						  "aadharNumber" : $scope.verifyObjABHAAddAndNum.data,
       						  "mobileNumber" : $scope.verifyObjABHAAddAndNum.data,
       						  "fetchById" : $scope.verifyObjABHAAddAndNum.fetchById,
       						  "loginOtpVerification" : $scope.verifyObjABHAAddAndNum.loginOtpVerification,
       						  "eventId":14
       						}
       				GenericService.serviceAction("POST", URI, data).then(
       					function (response) {
       						if(response.data.status =="success" ){
       							
       							$scope.requestabhaOtpObjectLoginOTP = JSON.parse(response.data.object);
       							$scope.verifyABHAaddressAndNumber = false;
       							$scope.verifyOtpForLoginABHAaddressAndNumber = true;
       							$scope.timer = 60;
       							timerPromise = $interval(countdown, 1000, $scope.timer);
       						    growl.success($scope.requestabhaOtpObjectLoginOTP.message);
       		                    console.log("response transaction ID : ",response.data.object);
       		                        		                 
       						$rootScope.stopLoader();
       						}else{
       							$rootScope.stopLoader();
           						growl.error(response.data.message);
       						}
       					},
       					function errorCallback(response) {
       						$rootScope.stopLoader();
       						growl.error('Connection time out');
       					});
       			}
               
               
               $scope.verifyObjABHAAddAndNumLogin = function(){
              	
              	 if($scope.verifyObjABHAAddAndNum.fetchById == 3){
            		   if($scope.verifyObjABHAAddAndNum.data == undefined || $scope.verifyObjABHAAddAndNum.data== null || $scope.verifyObjABHAAddAndNum.data ==""){
            				growl.error(" Please enter valid ABHA Address",{
            					title : ERROR_MSG
            					});
            					return false;
            			}
               	     }
              	 
              	 if($scope.verifyObjABHAAddAndNum.fetchById == 4){
            		   if($scope.verifyObjABHAAddAndNum.data == undefined || $scope.verifyObjABHAAddAndNum.data== null || $scope.verifyObjABHAAddAndNum.data ==""){
            				growl.error(" Please enter valid ABHA Number",{
            					title : ERROR_MSG
            					});
            					return false;
            			}
               	   }
              	 
     				$rootScope.startLoader();
     				var URI = BASE_URL + ROOT_URL + "/api/abdm/login/otp";

     				  var data = {
     						  "organizationId" : $scope.organizationId,
     						  "unitId" : $scope.unitId,
     						  "aadharNumber" : $scope.verifyObjABHAAddAndNum.data,
     						  "mobileNumber" : $scope.verifyObjABHAAddAndNum.data,
     						  "fetchById" : $scope.verifyObjABHAAddAndNum.fetchById,
     						  "loginOtpVerification" : $scope.verifyObjABHAAddAndNum.loginOtpVerification,
     						  "eventId":14
     						}
     				GenericService.serviceAction("POST", URI, data).then(
     					function (response) {
     						if(response.data.status =="success" ){
     							
     							$scope.requestabhaOtpObjectLoginOTP = JSON.parse(response.data.object);
     							$scope.verifyABHAaddressAndNumber = false;
     							$scope.verifyOtpForLoginABHAaddressAndNumber = true;
     							$scope.timer = 60;
     							timerPromise = $interval(countdown, 1000, $scope.timer);
     						    growl.success($scope.requestabhaOtpObjectLoginOTP.message);
     		                    console.log("response transaction ID : ",response.data.object);
     		                        		                 
     						$rootScope.stopLoader();
     						}else{
     							$rootScope.stopLoader();
         						growl.error(response.data.message);
     						}
     					},
     					function errorCallback(response) {
     						$rootScope.stopLoader();
     						growl.error('Connection time out');
     					});
     			}
               
               
               
               $scope.verifyObjABHAAddAndNumOtpForLogin = function(){
              	 $rootScope.startLoader();
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/login/verify";

      				var data = {
     						  "organizationId" : $scope.organizationId,
     						  "unitId" : $scope.unitId,
     						  "aadharNumber" : $scope.verifyObjABHAAddAndNum.data,
     						  "mobileNumber" : $scope.verifyObjABHAAddAndNum.data,
     						  "transactionId" : $scope.requestabhaOtpObjectLoginOTP.txnId,
     						  "otp" : $scope.verifyObjABHAAddAndNum.otpInput,
     						  "fetchById" : $scope.verifyObjABHAAddAndNum.fetchById,
     						  "loginOtpVerification" : $scope.verifyObjABHAAddAndNum.loginOtpVerification,
     						  "eventId":15
     						}
      				  
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if( response.data.status =="success" ){
      							$scope.verifyOtpForLoginDiv = false;
      							$scope.loginVerifyOTPObject = JSON.parse(response.data.object.aadharPreverifiedJson);
      							$scope.allVerifyOTPObject = JSON.parse(response.data.object.shareJson);
      		                    console.log("aadharPreverifiedJson : ",$scope.loginVerifyOTPObject);
      		                 
      		                    if($scope.loginVerifyOTPObject != null && $scope.loginVerifyOTPObject.length>0){
      		                    	$scope.patientDetails=$scope.loginVerifyOTPObject[0];
          		                    $scope.profilePhoto='data:image/png;base64,'+$scope.loginVerifyOTPObject[0].profilePhoto;
          		                    $scope.enrollmentEnrolByAadhaarObject={};
          		                    $scope.enrollmentEnrolByAadhaarObject.tokens ={};
          		                    $scope.enrollmentEnrolByAadhaarObject.tokens.token = response.data.object.token;
          		                    $scope.enrollmentEnrolByAadhaarObject.txnId = response.data.object.transactionId;
          		                    $scope.txnIdSDSS = response.data.object.transactionId;
          		                     $scope.profileAccountforShow();
          		                  
          		                    growl.success($scope.allVerifyOTPObject.message);
          		                    if($scope.verifyObj.fetchById ==1){
          		                    	  $("#loginVerifyV3").modal("show"); 
          		                    }
          		                    if($scope.verifyObj.fetchById ==2){
        		                    	  $("#loginVerifyV3Mobile").modal("show"); 
        		                        }
      		                    }else{
      		                    	// create new account
      		                    	$scope.createNewAccount =true;
      		                    }
      		                    
      						    $rootScope.stopLoader();
      						}else{
      							$rootScope.stopLoader();
          						growl.error(response.data.message);
      						}
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      						growl.error('Connection time out');
      					});
               }
               
               
               $scope.verifyObjABHAdressByInit = function(){
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/v2/auth/init";
    				$scope.mobile="";
    				 var data={
    						  "healthId":$scope.verifyObjABHAAddAndNum.data,
    						  "fetchById" : $scope.verifyObjABHAAddAndNum.fetchById,
    						  "loginOtpVerification" : $scope.verifyObjABHAAddAndNum.loginOtpVerification,
    						  "organizationId" : $scope.organizationId,
    						  "unitId" : $scope.unitId,
    						  "eventId" :13
    					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						if (response.data.status == "success") {
    							$rootScope.stopLoader();
    							$scope.verifyAuthInitObject = response.data.object;
    							console.log("$scope.verifyAuthInitObject",$scope.verifyAuthInitObject);
    							$scope.verifyABHAaddressAndNumber = false;
     							$scope.verifyOtpForLoginABHAaddressAndNumber = true;
    							$rootScope.stopLoader();
    						}
   		    				else if(response.data.status == "error")
   		    					{
   		    					   $rootScope.stopLoader();
   			   	   						$rootScope.stopLoader();
   			   	   						growl.error(response.data.message);
   		    					}
    					
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
    					});
    			}
               
               $scope.verifyObjABHAAdressConfirmOtp = function(){
     				$rootScope.startLoader();
     				
     				if($scope.verifyObjABHAAddAndNum.loginOtpVerification == 1){
     					
     					var URI = BASE_URL + ROOT_URL + "/api/abdm/v2/auth/confirmWithAadhaarOtp";
         				$scope.mobile="";
         				 var data={
         						  "transactionId":$scope.verifyAuthInitObject.txnId,
         						   "otp":$scope.verifyObjABHAAddAndNum.otpInput,
         						  "organizationId" : $scope.organizationId,
         						  "unitId" : $scope.unitId,
         						  "eventId" :19
         					}
         				GenericService.serviceAction("POST", URI, data).then(
         					function (response) {
         						if (response.data.status == "success") {
         							$scope.confirmWithAadhaarOtpObject = JSON.parse(response.data.object);
         							console.log("$scope.confirmWithAadhaarOtp",$scope.confirmWithAadhaarOtp);
         							$scope.enrollmentEnrolByAadhaarObject.tokens.token = $scope.confirmWithAadhaarOtpObject.token;
          		                    $scope.enrollmentEnrolByAadhaarObject.txnId = $scope.confirmWithAadhaarOtpObject.transactionId;
          		                    
         							$scope.verifyOtpForLoginABHAaddressAndNumber = false;
         						}
        		    				else if(response.data.status == "error")
        		    					{
        		    					   $rootScope.stopLoader();
        			   	   						$rootScope.stopLoader();
        			   	   						growl.error(response.data.message);
        		    					}
         					
         					},
         					function errorCallback(response) {
         						$rootScope.stopLoader();
         						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
         					});
         				 
     				}else if($scope.verifyObjABHAAddAndNum.loginOtpVerification == 2){

         				$rootScope.startLoader();
         				var URI = BASE_URL + ROOT_URL + "/api/abdm/v2/auth/confirmWithMobileOTP";
         				$scope.mobile="";
         				 var data={
         						  "transactionId":$scope.verifyAuthInitObject.txnId,
         						   "otp":$scope.verifyObjABHAAddAndNum.otpInput,
         						  "organizationId" : $scope.organizationId,
         						  "unitId" : $scope.unitId,
         						  "eventId" :18
         					}
         				GenericService.serviceAction("POST", URI, data).then(
         					function (response) {
         						if (response.data.status == "success") {
         							$scope.confirmWithMobileOTPV3Object = JSON.parse(response.data.object);
         							console.log("$scope.profileAccountObject",$scope.confirmWithMobileOTPV3Object);
         							$scope.enrollmentEnrolByAadhaarObject.tokens.token = $scope.confirmWithMobileOTPV3Object.token;
          		                    $scope.enrollmentEnrolByAadhaarObject.txnId = $scope.confirmWithMobileOTPV3Object.transactionId;
         							$scope.verifyOtpForLoginABHAaddressAndNumber = false;
         						}
        		    				else if(response.data.status == "error")
        		    					{
        		    					   $rootScope.stopLoader();
        			   	   						$rootScope.stopLoader();
        			   	   						growl.error(response.data.message);
        		    					}
         					
         					},
         					function errorCallback(response) {
         						$rootScope.stopLoader();
         						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
         					});
         			
     				}
     				
     			
     			}

                
                $scope.getAccountProfileVrify = function(){
      				$rootScope.startLoader();
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/account/profile";
      				$scope.mobile="";
      				 var data={
      						  "healthId":$scope.enrollmentEnrolByAadhaarObject.txnId,
      						  "organizationId" : $scope.organizationId,
      						  "unitId" : $scope.unitId,
      						  "eventId" :13
      					}
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if (response.data.status == "success") {
      							$scope.getAccountProfileObjectVerify = JSON.parse(response.data.object);
      							console.log("$scope.profileAccountObject",$scope.getAccountProfileObjectVerify);
      							$scope.verifyOtpForLoginDiv = false;
      							$scope.viewProfile();
      						}
     		    				else if(response.data.status == "error")
     		    					{
     		    					   $rootScope.stopLoader();
     			   	   						$rootScope.stopLoader();
     			   	   						growl.error(response.data.message);
     		    					}
      					
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
      					});
      			}
                
                function generateCaptcha() {
                    const num1 = Math.floor(Math.random() * 10);
                    const num2 = Math.floor(Math.random() * 10);
                    const operator = ['+', '-', '*'][Math.floor(Math.random() * 3)];
                    const expression = `${num1} ${operator} ${num2}`;
                    $scope.captchaExpression = expression;
                    return { num1, num2, operator };
                }

                // Function to calculate the answer of the captcha
                function calculateAnswer(captcha) {
                    switch (captcha.operator) {
                        case '+':
                            return captcha.num1 + captcha.num2;
                        case '-':
                            return captcha.num1 - captcha.num2;
                        case '*':
                            return captcha.num1 * captcha.num2;
                        default:
                            return null;
                    }
                }

                // Initialize the captcha
                $scope.captcha = generateCaptcha();

                // Function to validate the user's answer
                $scope.validateCaptcha = function() {
                    const userAnswer = parseInt($scope.verifyObj.userAnswer, 10);
                    const correctAnswer = calculateAnswer($scope.captcha);
                    if (userAnswer === correctAnswer) {
                        $scope.resultMessage = 'Correct!';
                    } else {
                        $scope.resultMessage = 'Incorrect. Try again.';
                        $scope.captcha = generateCaptcha(); // regenerate captcha if incorrect
                    }
                    $scope.verifyObj.userAnswer = ''; // Clear the input field
                };
                
                $scope.refreshCaptcha = function() {
               
                        $scope.captcha = generateCaptcha(); // regenerate captcha if incorrect
                        $scope.verifyObj.userAnswer = ''; // Clear the input field
                };
                
               
        		$scope.isResendDisabled = true;
    			$scope.retryAttemptstt = 1;
    			$scope.searchAbhaAddressResend = function(){
    				
    				
    				 if($scope.verifyObjABHAAddAndNum.fetchByIdV3 == 3){
              		   if($scope.verifyObjABHAAddAndNum.dataV3 == undefined || $scope.verifyObjABHAAddAndNum.dataV3== null || $scope.verifyObjABHAAddAndNum.dataV3 ==""){
              				growl.error(" Please enter valid ABHA Address",{
              					title : ERROR_MSG
              					});
              					return false;
              			}
                 	     }
    				 
    				if ($scope.retryAttemptstt <= 2) {
    					console.log("retry attempt : " + $scope.retryAttempts);
    					$scope.retryAttemptstt++;
					} else {
						growl.error("Max resend limit reached.Please try again !!");
						return false;
					}
    				
      				$rootScope.startLoader();
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/phr/web/login/abha/request/otp";
      				$scope.mobile="";
      				 var data={
      						  "otp":$scope.verifyObjABHAAddAndNum.otpInputV3,
      						  "healthId":$scope.verifyObjABHAAddAndNum.dataV3,
      						  "organizationId" : $scope.organizationId,
      						  "loginOtpVerification" : $scope.verifyObjABHAAddAndNum.loginOtpVerificationV3,
      						  "unitId" : $scope.unitId,
      						  "eventId" :22
      					}
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if (response.data.status == "success") {
      							$scope.getOTPVerifyObjectAbhaAddress = JSON.parse(response.data.object);
      							console.log("$scope.getOTPVerifyObjectAbhaAddress",$scope.getOTPVerifyObjectAbhaAddress);
      							growl.success($scope.getOTPVerifyObjectAbhaAddress.message);
      							$scope.abbhaAddressVerficationofV3slide2 = true;
      							$scope.timer = 60;
      	   			    	    timerPromise = $interval(countdown, 1000, $scope.timer);
      							 $rootScope.stopLoader();
      						}
     		    				else if(response.data.status == "error")
     		    					{
     		    					   $rootScope.stopLoader();
     			   	   						$rootScope.stopLoader();
     			   	   						growl.error(response.data.message);
     		    					}
      					
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
      					});
      			}
                
                $scope.searchAbhaAddress = function(){
      				$rootScope.startLoader();
      				
      				if($scope.verifyObjABHAAddAndNum.fetchByIdV3 == 3){
               		   if($scope.verifyObjABHAAddAndNum.dataV3 == undefined || $scope.verifyObjABHAAddAndNum.dataV3== null || $scope.verifyObjABHAAddAndNum.dataV3 ==""){
               				growl.error(" Please enter valid ABHA Address",{
               					title : ERROR_MSG
               					});
               					return false;
               			}
                  	     }
      				
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/phr/web/login/abha/search";
      				$scope.mobile="";
      				 var data={
      						  "healthId":$scope.verifyObjABHAAddAndNum.dataV3,
      						  "organizationId" : $scope.organizationId,
      						  "loginOtpVerification" : $scope.verifyObjABHAAddAndNum.loginOtpVerificationV3,
      						  "unitId" : $scope.unitId,
      						  "eventId" :21
      					}
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if (response.data.status == "success") {
      							$scope.getAccountProfileObjectVerify = JSON.parse(response.data.object);
      							console.log("$scope.profileAccountObject",$scope.getAccountProfileObjectVerify);
      							if($scope.getAccountProfileObjectVerify != null && $scope.getAccountProfileObjectVerify.status =="ACTIVE"){
      								//$scope.resuestOTPAbhaAddress();
      								
      								$scope.authModesAbhaAddress =[];
      								$scope.authModesAbhaAddress =$scope.getAccountProfileObjectVerify.authMethods;
      								$scope.abbhaAddressVerficationofSearchSlide = false;
          							$scope.abbhaAddressVerficationofV3AuthMode = true;
      							}else{
      								growl.error("Account does not exist !!!");
      							}
      							
      							 $rootScope.stopLoader();
      						}
     		    				else if(response.data.status == "error")
     		    					{
     		    					   $rootScope.stopLoader();
     			   	   						$rootScope.stopLoader();
     			   	   						growl.error(response.data.message);
     		    					}
      					
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
      					});
      			}
                
                $scope.resuestOTPAbhaAddress = function(){
      				$rootScope.startLoader();
      				
      				if($scope.verifyObjABHAAddAndNum.loginOtpVerificationDescV3 =='AADHAAR_OTP'){
      					$scope.verifyObjABHAAddAndNum.loginOtpVerificationV3 = 1;
      				}
                    if($scope.verifyObjABHAAddAndNum.loginOtpVerificationDescV3 =='MOBILE_OTP'){
                    	$scope.verifyObjABHAAddAndNum.loginOtpVerificationV3= 2;
      				}
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/phr/web/login/abha/request/otp";
      				$scope.mobile="";
      				 var data={
      						  "otp":$scope.verifyObjABHAAddAndNum.otpInputV3,
      						  "healthId":$scope.verifyObjABHAAddAndNum.dataV3,
      						  "organizationId" : $scope.organizationId,
      						  "loginOtpVerification" : $scope.verifyObjABHAAddAndNum.loginOtpVerificationV3,
      						  "unitId" : $scope.unitId,
      						  "eventId" :22
      					}
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if (response.data.status == "success") {
      							$scope.getOTPVerifyObjectAbhaAddress = JSON.parse(response.data.object);
      							console.log("$scope.getOTPVerifyObjectAbhaAddress",$scope.getOTPVerifyObjectAbhaAddress);
      							growl.success($scope.getOTPVerifyObjectAbhaAddress.message);
      							$scope.abbhaAddressVerficationofV3 = false;
      							$scope.abbhaAddressVerficationofV3slide2 = true;
      							$scope.abbhaAddressVerficationofSearchSlide = false;
      							$scope.abbhaAddressVerficationofV3AuthMode = false;
      							$scope.timer = 60;
      	   			    	    timerPromise = $interval(countdown, 1000, $scope.timer);
      							 $rootScope.stopLoader();
      						}
     		    				else if(response.data.status == "error")
     		    					{
     		    					   $rootScope.stopLoader();
     			   	   						$rootScope.stopLoader();
     			   	   						growl.error(response.data.message);
     		    					}
      					
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
      					});
      			}
                
                
                
                $scope.verifyOTPAbhaAddress = function(){
      				$rootScope.startLoader();
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/phr/web/login/abha/verify";
      				$scope.mobile="";
      				 var data={
      						  "otp":$scope.verifyObjABHAAddAndNum.otpInputV3,
      						  "transactionId":$scope.getOTPVerifyObjectAbhaAddress.txnId,
      						  "organizationId" : $scope.organizationId,
      						  "loginOtpVerification" : $scope.verifyObjABHAAddAndNum.loginOtpVerificationV3,
      						  "unitId" : $scope.unitId,
      						  "eventId" :23
      					}
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if (response.data.status == "success") {
      							$scope.getOTPVerifyObjectAbhaAddress = JSON.parse(response.data.object);
      							console.log("$scope.getOTPVerifyObjectAbhaAddress",$scope.getOTPVerifyObjectAbhaAddress);
      							
      							if($scope.getOTPVerifyObjectAbhaAddress.authResult != "failed"){
      							growl.success($scope.getOTPVerifyObjectAbhaAddress.message);
      							}
      							
      							if($scope.getOTPVerifyObjectAbhaAddress.authResult == "failed"){
      								$rootScope.stopLoader();
      								growl.error($scope.getOTPVerifyObjectAbhaAddress.message);
      								return;
      							}
      							$scope.abbhaAddressVerficationofV3slide2 = false;
      						    $scope.abhaProfileAbhaAddress();
      							 $rootScope.stopLoader();
      						}
     		    				else if(response.data.status == "error")
     		    					{
     		    					
     		    					       if(response.data.code == 400){
     		    					    	  $rootScope.stopLoader();
                                              growl.error("OTP enterd in incorrect");
     		    					       }else{
     		    					    	  $rootScope.stopLoader();
       			   	   						  growl.error(response.data.message);   
     		    					       }
     		    					       
     		    					}
      					
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
      					});
      			}
                
                
                
                $scope.abhaProfileAbhaAddress = function(){
      				$rootScope.startLoader();
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/phr/web/login/profile/abha-profile";
      				$scope.mobile="";
      				 var data={
      						  "token":$scope.getOTPVerifyObjectAbhaAddress.tokens.token,
	   						  "transactionId" : $scope.getOTPVerifyObjectAbhaAddress.txnId,
      						  "otp":$scope.verifyObjABHAAddAndNum.otpInputV3,
      						  "organizationId" : $scope.organizationId,
      						  "loginOtpVerification" : $scope.verifyObjABHAAddAndNum.loginOtpVerificationV3,
      						  "unitId" : $scope.unitId,
      						  "eventId" :24
      					}
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if (response.data.status == "success") {
      							$scope.profileAccountObject = JSON.parse(response.data.object);
      							console.log("$scope.profileAccountObject",$scope.profileAccountObject);
      							$scope.profileAccountObject.ABHANumber = $scope.profileAccountObject.abhaNumber;
      							$scope.profileAccountObject.preferredAbhaAddress = $scope.profileAccountObject.abhaAddress;
      							$scope.patientDetails=$scope.profileAccountObject;
      		                 
      		                    	  $("#loginVerifyV3New").modal("show"); 
      		                    	  
      							$scope.abbhaAddressVerficationofV3slide2 = false;
      							$scope.abbhaAddressVerficationofV3 = true;
      							$scope.abbhaAddressVerficationofV3slide2 = false;
      							$scope.abbhaAddressVerficationofSearchSlide = true;
      							$scope.abbhaAddressVerficationofV3AuthMode = false;
      						    $scope.verifyObjABHAAddAndNum = {
      			                		 "fetchById" : "4",
      			                		 "fetchByIdV3" : "3",
      			                		 "data" : "",
      			                		 "dataV3" : "",
      			                		 "otpInputV3" : "",
      			                		 "loginOtpVerification":"",
      			                		 "loginOtpVerificationV3":"",
      			                		 "loginOtpVerificationDescV3":"",
      			                		 "otpInput":""
      			                			 
      			                 }
      							 $rootScope.stopLoader();
      						}
     		    				else if(response.data.status == "error")
     		    					{
     		    					   $rootScope.stopLoader();
     			   	   						$rootScope.stopLoader();
     			   	   						growl.error(response.data.message);
     		    					}
      					
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
      					});
      			}
                
                $scope.phrCardAbhaAddress = function(){
      				$rootScope.startLoader();
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/phr/web/login/profile/abha/phr-card";
      				$scope.mobile="";
      				 var data={
      						  "token":$scope.getOTPVerifyObjectAbhaAddress.tokens.token,
	   						  "transactionId" : $scope.getOTPVerifyObjectAbhaAddress.txnId,
      						  "otp":$scope.verifyObjABHAAddAndNum.otpInputV3,
      						  "organizationId" : $scope.organizationId,
      						  "loginOtpVerification" : $scope.verifyObjABHAAddAndNum.loginOtpVerificationV3,
      						  "unitId" : $scope.unitId,
      						  "eventId" :25
      					}
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if (response.data.status == "success") {
      							$scope.pngCard='data:image/png;base64,'+response.data.object.base64PngCard;
	    						$("#pngCardPopUP").show();
	    						  $scope.verifyObjABHAAddAndNumBack();
	    						  $scope.resetABHAAddAndNumBack();
      							 $rootScope.stopLoader();
      						}
     		    				else if(response.data.status == "error")
     		    					{
     		    					   $rootScope.stopLoader();
     			   	   						$rootScope.stopLoader();
     			   	   						growl.error(response.data.message);
     		    					}
      					
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
      					});
      			}
                
                $scope.linkedCareContextList=[];
                //HIP Initiated Linking
                $scope.getLinkedCareContext =function(id){

      				$rootScope.startLoader();
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/getLinkedCareContextById";
      				$scope.mobile="";
      				 var data={
      						  "ids":id,
      					}
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if (response.data.status == "success") {
      							$scope.linkedCareContextList=[];
      							$scope.linkedCareContextList=response.data.listObject;
      							 $rootScope.stopLoader();
      						 }
      						
     		    				else if(response.data.status == "error")
     		    					{
     		    					   $rootScope.stopLoader();
     		    					}
      					
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      					});
      			
                }
                
                $scope.setDateAndTime =function(){
                	
                	$scope.dateAndTime ='';
                	if($scope.selectedVisitType =='OP'){
                		$rootScope.startLoader();
                		const selectedEncounter = $scope.link.selectedEncounter;
                        const selectedInfo = $scope.getEncounterNumberList.find(info => info.encounterNumber === selectedEncounter);
                        $scope.selectedEncounterOrAdmissionId =selectedInfo.encounterId;
                        if (selectedInfo) {
                            console.log('Selected Encounter Date:', selectedInfo.careContextTime);
                            $scope.dateAndTime=selectedInfo.careContextTime
                        }
                        $scope.getLinkedCareContext($scope.selectedEncounterOrAdmissionId);
                	}else{$rootScope.startLoader();
	                	const selectedEncounter = $scope.link.selectedEncounter;
	                    const selectedInfo = $scope.getAdmissionNumberList.find(info => info.admissionNumber === selectedEncounter);
	                    $scope.selectedEncounterOrAdmissionId =selectedInfo.admissionId;
	                    if (selectedInfo) {
	                        console.log('Selected Encounter Date:', selectedInfo.careContextTime);
	                        $scope.dateAndTime=selectedInfo.careContextTime
	                    }
	                    $scope.getLinkedCareContext($scope.selectedEncounterOrAdmissionId);
                    }
                	
                	
                	
                }
                
                $scope.setDateAndTime2 =function(){
                	$scope.dateAndTime2= moment(new Date()).format('DD-MM-YYYY HH:mm:ss');
                }
                
                
                $scope.hipInitiateLinkingByGeerateToken = function(){
                	
                	if($scope.linkCareContextObject == undefined || $scope.linkCareContextObject == null || $scope.linkCareContextObject == ""){
                		growl.warning("Please Select Patient");
                		return false;
                	}
                	
                	
      				$rootScope.startLoader();
      				var dateOfBirth = $scope.linkCareContextObject.dob.split("/");
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/token/generate-token";
      				$scope.mobile="";
      				 var data={
      					    "abhaNumber":$scope.linkCareContextObject.healthNumber.replace(/-/g, ''),
      					    "healthId": $scope.linkCareContextObject.healthId,
      					    "name": $scope.linkCareContextObject.patientName,
      					    "gender": $scope.linkCareContextObject.genderCode,
      					    "yearOfBirth": dateOfBirth[2],
      					    "organizationId" : $scope.organizationId,
  						    "unitId" : $scope.unitId,
      					    "eventId":28
      					}
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if (response.data.status == "success") {
      							$scope.hipInitiateLinkingByGenerateTokenObject =response.data.object;
      							var dateOfBirth = $scope.linkCareContextObject.dob.split("/");
      							 $timeout( function(){
      								$("#hipInitiateLinkingByGenerateTokenModel").show();
      								$rootScope.stopLoader();
    		                    },5000); 
      						
      							growl.success(response.data.message);
      							 
      						}
     		    				else if(response.data.status == "error")
     		    					{
     		    					   $rootScope.stopLoader();
     			   	   						$rootScope.stopLoader();
     			   	   						growl.error(response.data.message);
     		    					}
      					
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
      					});
      			}
                
                
                 $scope.hipInitiateLinkingByGeerateTokenNew = function(){
                	 
                	 
                	
                	if($scope.linkCareContextObject == undefined || $scope.linkCareContextObject == null || $scope.linkCareContextObject == ""){
                		growl.warning("Please Select Patient");
                		return false;
                	}
                	if($scope.linkedCareContextList == undefined || $scope.linkedCareContextList == null || $scope.linkedCareContextList.length  ==  8){
                		growl.warning("Health record is already linked");
                		return false;
                	}
                	
      				$rootScope.startLoader();
      				var dateOfBirth = $scope.linkCareContextObject.dob.split("/");
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/token/generate-token";
      				$scope.mobile="";
      				 var data={
      					    "abhaNumber":$scope.linkCareContextObject.healthNumber.replace(/-/g, ''),
      					    "healthId": $scope.linkCareContextObject.healthId,
      					    "name": $scope.linkCareContextObject.patientName,
      					    "gender": $scope.linkCareContextObject.genderCode,
      					    "yearOfBirth": dateOfBirth[2],
      					    "organizationId" : $scope.organizationId,
  						    "unitId" : $scope.unitId,
      					    "eventId":28
      					}
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if (response.data.status == "success") {
      							$scope.hipInitiateLinkingByGenerateTokenObject =response.data.object;
      							var dateOfBirth = $scope.linkCareContextObject.dob.split("/");
      							 $timeout( function(){
      								$("#hipInitiateLinkingByGenerateTokenModel1").show();
      								$rootScope.stopLoader();
    		                    },5000); 
      						
      							growl.success(response.data.message);
      							 
      						}
     		    				else if(response.data.status == "error")
     		    					{
     		    					   $rootScope.stopLoader();
     			   	   						$rootScope.stopLoader();
     			   	   						growl.error(response.data.message);
     		    					}
      					
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
      					});
      			}
                
                
                $scope.hipInitiateLinkingcarecontext = function(){
                	
              /*  	if($scope.link.selectedEncounter == undefined || $scope.link.selectedEncounter  == "" || $scope.link.selectedEncounter  == null){
                        growl.warning("Please select a valid Encounter or Admission number.");
                        return;
                    }*/
                	
      				$rootScope.startLoader();
      				var dateOfBirth = $scope.linkCareContextObject.dob.split("/");
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/link/carecontext";
      				$scope.mobile="";
      				 var data={
      						 
      						"abhaNumber":$scope.linkCareContextObject.healthNumber.replace(/-/g, ''),
      					    "healthId": $scope.linkCareContextObject.healthId,
      					    "name": $scope.linkCareContextObject.patientName,
      					    "gender": $scope.linkCareContextObject.genderCode,
      					    "yearOfBirth": dateOfBirth[2],
       					    "transactionId" : $scope.hipInitiateLinkingByGenerateTokenObject.transactionId,
       					    "organizationId" : $scope.organizationId,
   						    "unitId" : $scope.unitId,
       					    "eventId":29
       					}
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if (response.data.status == "success") {
      							$scope.hipInitiateLinkingByGenerateTokenObject =response.data.object;
      							$("#hipInitiateLinkingByGenerateTokenMode").hide();
      							 $timeout( function(){
        		                	 $scope.getTransctionTableDetails();
    		                    },5000); 
      							 
      							
      							 $rootScope.stopLoader();
      						}
     		    				else if(response.data.status == "error")
     		    					{
     		    					   $rootScope.stopLoader();
     			   	   						$rootScope.stopLoader();
     			   	   						growl.error(response.data.message);
     		    					}
      					
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
      					});
      			}
                
                
                $scope.hipInitiateLinkingcarecontext1 = function(){
                	if($scope.link.selectedEncounter == undefined || $scope.link.selectedEncounter  == "" || $scope.link.selectedEncounter  == null){
                        growl.warning("Please select a valid Encounter or Admission number.");
                        return;
                    }
      				$rootScope.startLoader();
      				var dateOfBirth = $scope.linkCareContextObject.dob.split("/");
      				
      				if($scope.link.selectedEncounter != undefined && $scope.link.selectedEncounter != "" && $scope.link.selectedEncounter != null){
      					var selectedEncounter = $scope.link.selectedEncounter.split(" ");
      				}
      			
      				
      				
      				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/link/carecontextNew";
      				$scope.mobile="";
      				 var data={
      						 
      						"abhaNumber":$scope.linkCareContextObject.healthNumber.replace(/-/g, ''),
      					    "healthId": $scope.linkCareContextObject.healthId,
      					    "name": $scope.linkCareContextObject.patientName,
      					    "gender": $scope.linkCareContextObject.genderCode,
      					    "yearOfBirth": dateOfBirth[2],
       					    "transactionId" : $scope.hipInitiateLinkingByGenerateTokenObject.transactionId,
       					    "organizationId" : $scope.organizationId,
       					    "encounterNumber" :(selectedEncounter[0]!= null && selectedEncounter[0]!= undefined && selectedEncounter[0] !== "")? selectedEncounter[0] :$scope.link.selectedAdmission,
   						    "unitId" : $scope.unitId,
       					    "eventId":29
       					}
      				GenericService.serviceAction("POST", URI, data).then(
      					function (response) {
      						if (response.data.status == "success") {
      							$scope.hipInitiateLinkingByGenerateTokenObject =response.data.object;
      							$("#hipInitiateLinkingByGenerateTokenModel1").hide();
      							$scope.link.selectedEncounter="";
     							$scope.link.selectedAdmission="";
     							$scope.selectedVisitType="";
     							$scope.patientNameNew ="";
      							 $timeout( function(){
        		                	 $scope.getTransctionTableDetails();
    		                    },5000); 
      							 
      							
      							 $rootScope.stopLoader();
      							
      							
      						}
     		    				else if(response.data.status == "error")
     		    					{
     		    					$scope.link.selectedEncounter="";
         							$scope.link.selectedAdmission="";
         							$scope.selectedVisitType="";
         							$scope.patientNameNew ="";
     		    					   $rootScope.stopLoader();
     			   	   						$rootScope.stopLoader();
     			   	   						growl.error(response.data.message);
     		    					}
      					
      					},
      					function errorCallback(response) {
      						$rootScope.stopLoader();
      						growl.error($scope.enrollmentEnrolByAadhaarObject.message);
      					});
      			}
                $scope.retryAttemptsResendOtp = 1;
                $scope.fetchDetailsForLoginResendOtpaaaa = function(){
    				$rootScope.startLoader();
    				if ($scope.verifyObj.fetchById == 0) {
     					growl.error("Please Select Login Criteria ...");
 						return false;
     				}
     				if ($scope.retryAttemptsResendOtp <= 2) {
     					console.log("retry attempt : " + $scope.retryAttemptsResendOtp);
     					$scope.retryAttemptsResendOtp++;
 					} else {
 						growl.error("Max resend limit reached.Please try again !!");
 						$rootScope.stopLoader();
 						return false;
 					}
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/profile/login/request/otp";
    				  var data = {
    						  
    						  "organizationId" : $scope.organizationId,
       						  "unitId" : $scope.unitId,
       						  "aadharNumber" : $scope.verifyObj.dataMobile,
       						  "mobileNumber" : $scope.verifyObj.dataMobile,
       						  "fetchById" : $scope.verifyObj.fetchByIdMobile,
       						  "loginOtpVerification" : $scope.verifyObj.loginOtpVerification,
       						  "index":$scope.resendOptionObject.index,
       						  "transactionId":$scope.searchABHANbumberByMobileNumberObject[0].txnId,
    						  "eventId":14
    						}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						if(response.data.status =="success" ){
    							$scope.requestOtpObjectLoginOTP = JSON.parse(response.data.object);
    							$scope.verifyAadharOrMobileSearch = false;
    							$scope.verifyOtpForLoginByMobileNumber = true;
    							$scope.timer = 60;
    							timerPromise = $interval(countdown, 1000, $scope.timer);
    						    growl.success($scope.requestOtpObjectLoginOTP.message);
    		                    console.log("response transaction ID : ",response.data.object);
    		                    
    		                        		                 
    						$rootScope.stopLoader();
    						}else{
    							$rootScope.stopLoader();
        						growl.error(response.data.message);
    						}
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
                
                
                $scope.verifyUserforMobileNew = function(info){
                	$scope.resendOptionObject = info;
        				$rootScope.startLoader();
        				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/profile/login/request/otp";
        				  var data = {
        						  
        						  "organizationId" : $scope.organizationId,
           						  "unitId" : $scope.unitId,
           						  "aadharNumber" : $scope.verifyObj.dataMobile,
           						  "mobileNumber" : $scope.verifyObj.dataMobile,
           						  "fetchById" : $scope.verifyObj.fetchByIdMobile,
           						  "loginOtpVerification" : $scope.verifyObj.loginOtpVerification,
           						  "index":info.index,
           						  "transactionId":$scope.searchABHANbumberByMobileNumberObject[0].txnId,
        						  "eventId":14
        						}
        				GenericService.serviceAction("POST", URI, data).then(
        					function (response) {
        						if(response.data.status =="success" ){
        							
        							$scope.requestOtpObjectLoginOTP = JSON.parse(response.data.object);
        							$scope.verifyAadharOrMobileSearch = false;
        							$scope.verifyOtpForLoginByMobileNumber = true;
        							$scope.timer = 60;
        							timerPromise = $interval(countdown, 1000, $scope.timer);
        						    growl.success($scope.requestOtpObjectLoginOTP.message);
        		                    console.log("response transaction ID : ",response.data.object);
        		                        		                 
        						$rootScope.stopLoader();
        						}else{
        							$rootScope.stopLoader();
            						growl.error(response.data.message);
        						}
        					},
        					function errorCallback(response) {
        						$rootScope.stopLoader();
        						growl.error('Connection time out');
        					});
        			}
                
                
                $scope.rule1 = function (info) {
                	  return info && info.length >= 8; // Minimum length of 8 characters
                	};

                	$scope.rule2 = function (info) {
                	  return info && info.length <= 18; // Maximum length of 18 characters
                	};

                	$scope.rule3 = function (info) {
                	  const specialCharPattern = /[^a-zA-Z0-9._]/; // Only letters, numbers, dot, underscore
                	  return info && !specialCharPattern.test(info);
                	};

                	$scope.rule4 = function (info) {
                	  return info && !(info.startsWith('.') || info.startsWith('_') || info.endsWith('.') || info.endsWith('_'));
                	};

                	$scope.rule5 = function (info) {
                	  const alphanumericPattern = /^[a-zA-Z0-9]+$/;
                	  const dotUnderscorePattern = /(\.|_)/g;
                	  return info && alphanumericPattern.test(info.replace(dotUnderscorePattern, ''));
                	};

                	$scope.validateAbha = function (info) {
                	  // All rules must pass
                	  return $scope.rule1(info) && $scope.rule2(info) && $scope.rule3(info) && $scope.rule4(info) && $scope.rule5(info);
                	};

                
                
    }

    function config($stateProvider) {
    $stateProvider.state("abdmRegistration", {
      url: "/abdmRegistration",
      templateUrl: 'views/Abdm/abdm-registrationNew.html',
      controller: "AbdmRegistration.Controller",
      title: 'ABDM Registration',
      controllerAs: "vm",
      params: {
     	 patientRegistrationObjAbdm: {},
      }
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AbdmRegistration.Controller", abdmController);
})();
