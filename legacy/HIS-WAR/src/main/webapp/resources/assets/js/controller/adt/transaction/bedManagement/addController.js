angular
.module('myApp')
.controller(
		"addController",
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
				
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;

				/*** Check user is Loggedin */
				var cookieObject,cookieObjectAppConfig;
				[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
				$scope.orgUnitId = cookieObject.unitId;
				$scope.orgId = cookieObject.organizationId;
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
				

				
				
				$scope.dateOptions =
				{
					formatYear : 'yyyy',
					showWeeks : false,
					maxDate : new Date(),
				};

				$scope.open = function() {
					$scope.popup.opened = true;
				};

				$scope.popup =
				{
					opened : false
				};
				
				$scope.popup1 =
				{
					opened : false
				};
				
				$scope.open1 = function() 
				{
					$scope.popup1.opened = true;
				};
				
				$scope.dateOptions = {
						formatYear : 'yyyy',
						showWeeks : false
					};

					$scope.openDatePicker = function() {
						$scope.datepicker.opened = true;
					};

					$scope.datepicker = {
						opened : false
					};
					
					$scope.datepicker = new Date();
				
					$scope.patientDetails = {};
//					$scope.admissionObj = {};
					/**Redirect Page Code*/
					console.log("$stateParams.obj", $stateParams.obj1);
					if(!angular.equals({},$stateParams.obj1))
						{
//						$scope.admissionObj =  $stateParams.obj1;
						$scope.patientDetails.patientName = $scope.admissionObj.pFirstName;
						$scope.patientDetails.genderCode= ($scope.admissionObj.genderId==1)?"M":($scope.admissionObj.genderId==2?"F":($scope.admissionObj.genderId==3?"OTR":"-"));
//						$scope.patientDetails.age= moment().diff($scope.admissionObj.dob, 'years');
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
						
					
					}	
					
			
					
					     
				$scope.sum = 0;
				$scope.systolic='';
				$scope.heart='';
				$scope.respiratory='';
				$scope.selectedTemperature='';
				$scope.avpu='';
				
				$scope.commnadditionSys=function (systolic,oldValue){					
					if(oldValue!=""){						
						 $scope.sum -= parseInt(oldValue);
					}						
					 $scope.sum += systolic;
					 
					 console.log("sum", $scope.sum);
				}
				$scope.commnadditionHeart=function (heart,oldValue){					
					
					if(oldValue!=""){						
						 $scope.sum -= parseInt(oldValue);
					}						
					 $scope.sum += heart;
					 
					 console.log("sum", $scope.sum);
				}
                
	             $scope.commnadditionrespiratory=function (respiratory,oldValue){					
					
					if(oldValue!=""){						
						 $scope.sum -= parseInt(oldValue);
					}						
					 $scope.sum += respiratory;
					 
					 console.log("sum", $scope.sum);
				}

	             $scope.commnadditiontemperature=function (selectedTemperature,oldValue){					
						
						if(oldValue!=""){						
							 $scope.sum -= parseInt(oldValue);
						}						
						 $scope.sum += selectedTemperature;
						 
						 console.log("sum", $scope.sum);
					}
	             $scope.commnadditionavpu=function (avpu,oldValue){					
						
						if(oldValue!=""){						
							 $scope.sum -= parseInt(oldValue);
						}						
						 $scope.sum += avpu;
						 
						 console.log("sum", $scope.sum);
					}
	             
	             
	             /*$scope.getOrderProcedureList = function()
					{*/
	            	 
	            	 var data={};
					var URI = BASE_URL + ROOT_URL 
					+ "/adt/getCountOnMewsMapper/"+$scope.patientDetails.patientId+"/"+$scope.organizationId+"/"+$scope.unitId ;
				GenericService.serviceAction("GET", URI,data).then(function(response) {
					
						if (response.data.status == 'success') {
							$scope.finalscoreList=response.data.listObject;
							console.log("score", $scope.finalscoreList);
						}
					});
					/*}*/
	             
	             $scope.save=function(){
	            	 
	            	 
	            	 
	            	 var data ={
	            	 totalScore:$scope.sum,
	            	 systolicBloodPressureId:$scope.systolic,
	        		 heartRateId : $scope.heart,
	        		 temperatureId : $scope.selectedTemperature,
	        		 avpuScoreId : $scope.avpu,
	        		 respiratoryRateId : $scope.respiratory,
	            	 organizationId : $scope.organizationId,
					 unitId : $scope.unitId,
					 createdBy : $scope.userId,
					 updatedBy : $scope.userId,
					 status    : 'A',
					 createdDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					 updatedDate : moment(new Date()).format('DD-MM-YYYY HH:mm:ss'),
					 patientId : $scope.patientDetails.patientId,
					 admissionId : $scope.patientDetails.admissionId
        	  }
	            	  

	            	  if(data != null){

	            	  var URI = BASE_URL + ROOT_URL+ "/adt/saveMewsMapper";
	            	  GenericService.serviceAction("POST", URI,data).then(function(response) {

							if (response.data.status == 'success') {
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								if($scope.patientDetails.visitTypeId == 4 ){
									$state.reload('mapOfBedForER');
								}else{
									
									$state.reload('bedManagement');
								}
							
								return;

							    } else if (response.data.status == 'error') {
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							   }
						});

	            	  }
	            	  else  {
	            		  growl.error("Please Select MEWS",
									{
										title : 'Error!'
									});
	            	  }
	             }
	             

	            	 
	             
	             

			} ]);