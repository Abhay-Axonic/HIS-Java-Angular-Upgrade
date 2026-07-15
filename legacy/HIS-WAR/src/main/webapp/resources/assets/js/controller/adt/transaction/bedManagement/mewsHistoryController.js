angular
.module('myApp')
.controller(
		"mewsHistoryController",
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
			'$interval',
			'$stateParams',
			'GenericServiceParamHeader',
			'BillingGenericService',
			'$window',
			function($scope, $rootScope, $state, $cookies,
					GenericService, PagerService, promiseFactory,
					$sessionStorage, growl,$interval,$stateParams,GenericServiceParamHeader,BillingGenericService,$window) {
				$rootScope.loginpage = true;
				$scope.LISDynamicLabel = "Sample";

				/** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.sampleCollectionList = [];
				$scope.admissionObj = {};
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;

				/*** Check user is Loggedin */
				var cookieObject,cookieObjectAppConfig;
				[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
				$scope.unitId = cookieObject.unitId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				
				var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
				 if(cookieObjectAppConfig == undefined){
				  $state.go('login');
				  return;
				 }
				 else
				  {
				   console.log('cookieObjectAppConfig',cookieObjectAppConfig);
				   
				  }
					/**Redirect Page Code*/
					if(!angular.equals({},$stateParams.obj))
						{
						$scope.admissionObj =  $stateParams.obj;
						$scope.patientDetails.patientName = $scope.admissionObj.pFirstName;
						$scope.patientDetails.genderCode= ($scope.admissionObj.genderId==1)?"M":($scope.admissionObj.genderId==2?"F":($scope.admissionObj.genderId==3?"OTR":"-"));
						/*$scope.patientDetails.age= moment().diff($scope.admissionObj.dob, 'years');*/
						$scope.patientDetails.uhIdNumber= $scope.admissionObj.uhidnumber;
						$scope.patientDetails.dob= $scope.admissionObj.dob;
						$scope.patientDetails.doctorFullName= $scope.admissionObj.dFirstName;
						$scope.patientDetails.dateOfAdmission= $scope.admissionObj.doa;
						$scope.patientDetails.patientDischargeDate= $scope.admissionObj.pdd;
						$scope.patientDetails.wardName= $scope.admissionObj.wardName;
						$scope.patientDetails.bedNumber= $scope.admissionObj.bedNumber;
						$scope.patientDetails.imagePath= "";
						$scope.patientDetails.patientId= $scope.admissionObj.patientId;
						$scope.patientDetails.visitTypeId= $scope.admissionObj.visitTypeId;
						$scope.patientDetails.admissionId= $scope.admissionObj.admissionId;
						$scope.patientDetails.genderId= $scope.admissionObj.genderId;
						 $scope.getMewsList();
					
					}
					

				
	             /***************/
	             $scope.getMewsList = function()
	 	        {
	 			 $scope.mewsList = [];
	 	      		
	 	    		var URI = BASE_URL + ROOT_URL + "/adt/getMewsMapperList/"+$scope.patientDetails.patientId+"/"+$scope.organizationId+"/"+$scope.unitId
	 	    		GenericService.serviceAction(METHOD_GET, URI)
	 	    		.then(
	 	    			function(response) {
	 	    				if (response.data.status == "success") {
	 	    					$scope.mewsList = response.data.listObject;
	 	    					
	 	    					console.log("mewsList", $scope.mewsList);

	 	    				}
	 	    			});
	 	        }
	 		
	             /***************/
			} ]);