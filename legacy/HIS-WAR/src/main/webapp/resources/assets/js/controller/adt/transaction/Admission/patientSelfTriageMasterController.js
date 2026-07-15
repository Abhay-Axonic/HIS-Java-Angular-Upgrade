/**
 *  @Author By  Sidheshwar Lamture
 */

angular.module('myApp')
		.controller("patientSelfTriageMasterController",
		['$scope','$rootScope','$state','$timeout','$cookies','GenericService','BillingGenericService','GenericServiceParamHeader','PagerService','promiseFactory','$sessionStorage','growl','reportService',
		function($scope,$rootScope,$state,$timeout,$cookies, GenericService,BillingGenericService,GenericServiceParamHeader,PagerService,promiseFactory,$sessionStorage,growl,reportService){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.billing = true;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.dischargeSummaryId;
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			$scope.masterObj = {};
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			 $scope.fromDate =undefined;
			 $scope.toDate = undefined;
			
			var cookieObject,cookieObjectAppConfig;
			[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
			$scope.patientCategoryId = cookieObjectAppConfig.patientCategoryId;

			
			var date = new Date();
			
			$scope.dateOptions = {
					formatYear : 'yyyy',
					showWeeks : false,
					minDate : date.setDate((new Date()).getDate() - 90),
				};
			$scope.openDatePicker = function() {
				$scope.datepicker.opened = true;
			};
			
			
			 $scope.openFromDatePicker = function() {
					$scope.fromDatepicker.opened = true;
				};

				$scope.fromDatepicker = {
						opened : false
				
				};
	
				$scope.openToDatePicker = function() {
					$scope.toDatepicker.opened = true;
				};

				$scope.toDatepicker = {
						opened : false
				};
				
				$scope.openDatePickerPatiSearch = function() {
					$scope.datepicker1.opened = true;
					};

					$scope.datepicker1 = {
					opened : false
					};
			
			// for popup
			$scope.showModal = false;
			$scope.buttonClicked = "";
			$scope.popUpFlag = true;
			// end for popup
			
		
			
			$scope.initPaymentDetailsObj = function(){
				$scope.paymentDetails = angular.copy({paymentModeId:1,cashAmount:"",bankId:"",chequeNumber:"",chequeDate:"",chequeAmount:"",credCardHolderName:"",credCardTypeId:"",credAmount:"",credCardNumber:"",credExpiryDate:"",debCardHolderName:"",debCardTypeId:"",debAmount:"",debCardNumber:"",debExpiryDate:""});
			}
	   
			
			$scope.patientSearchObj = {
				      "uhIdNumber"      : "",
				      "patientName"     : "",
				      "genderId"        : "",
				      "mobileNo"        : "",
				      "birthDate"       : "",
				      "identificationNo":"",
				      "patientType"   :"1",
				      "unitId"          :$scope.unitId,
				      "organizationId"  :$scope.organizationId
				    }
			
			$scope.searchPatient = function()
			{
				$scope.enableChart='N';
				$scope.patientVitalList=[];
				$rootScope.startLoader();
				$scope.patientTemplateList=[];
				if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
				{
					growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
					$scope.patientList = [];
					$rootScope.stopLoader();
					return false;
				}	
				var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
				var patSearchObj = angular.copy($scope.patientSearchObj);
				if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
					var dt = new Date($scope.patientSearchObj.birthDate);
					patSearchObj.birthDate = (dt.getMonth() + 1) + "-" + dt.getDate() + "-" +  dt.getFullYear();
				} 
				BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					$scope.searchPatientList = [];
					if (response.data.status == "success")
						{
						$scope.patientList = response.data.listObject;
						patSearchObj = {};
						
						$scope.initPaymentDetailsObj();
						$scope.listPaymentDetails = [];
						$scope.patientDetailsList = [];
						$scope.patientDetails = {};
						$scope.patientSearchObj.birthDate="";
						$rootScope.stopLoader();
						}
					else{
						if(response.data.listObject==null){
							growl.warning(response.data.message,{
								title : ERROR_MSG
							});
							$scope.patientSearchObj.birthDate="";
							$scope.patientList = [];
							return false;
						}
						$rootScope.stopLoader();
					}
					
				});
			}
			
			$scope.admissionId=null;
			$scope.departmentId=null;
			//get Patient Id,department id and template id
			$scope.patientId=null;
			$scope.getPatientDetailsById = function(patientId)
			{
				$scope.patientId=patientId;
				$scope.patientDetailsList = [];
				$scope.patientDetails = {};
				$scope.masterObj = {};
				$scope.patientVitalList=[];
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL + "/api/global/patient/billing/"+patientId;
				BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						$scope.patientDetailsList = response.data.listObject;
						$scope.patientDetails = response.data.listObject[0];
						$scope.patientDetails.patientName= response.data.listObject[0].firstName +" "+ response.data.listObject[0].lastName;
						$scope.patientDetails.dob= response.data.listObject[0].dob ;
						$scope.patientDetails.uhidNumber= response.data.listObject[0].uhidNumber ;
						console.log("PatientDetailList:",$scope.patientDetailsList);
						console.log("PatientDetail:",$scope.patientDetails);
						$rootScope.allergydata($scope.patientDetails);
						$scope.getAllPatientDiagnosisForIcon();
					}
				});
				var data = {};
				//$scope.patientId=1761;
				var URI = BASE_URL + ROOT_URL + "/api/opd/getCurrentAppointmentVitalSelfTriage/"+$scope.patientId;
				GenericService.serviceAction("GET", URI, data).then(function(response) {
					if (response.data.status == "success"){
						$scope.patientVitalList = response.data.listObject;
						if($scope.patientVitalList.length==0){
							 growl.warning(
										'Vitals doen not exist for selected patient..!!',
										{
											title : 'Warning!'
										});
						}
						console.log("patientVitalList:",$scope.patientVitalList);
					}
					$rootScope.stopLoader();
				});
			
			}
			
			//for date
			$scope.getFormattedDate = function(date,isTime){
				var tempDate = new Date(date);
				var date = tempDate.getDate().toString().length == 1 ? "0"+tempDate.getDate().toString() : tempDate.getDate().toString();
				var month =  (tempDate.getMonth() + 1).toString().length == 1 ? "0"+(tempDate.getMonth() + 1).toString() : (tempDate.getMonth() + 1).toString();
				var year = tempDate.getFullYear();
				if(isTime)
					return date + "-" + month + "-" + year + " "+tempDate.getHours()+":"+tempDate.getMinutes()+":"+getSeconds();
				else
					return date + "-" + month + "-" + year + " 00:00:00";
			}
			
			$scope.enableChart='N'
			$scope.showChartEnable=function(){
				$scope.enableChart='Y';
			}
			$scope.hideChartEnable=function(){
				$scope.enableChart='N';
			}
			
			$scope.onClick = function () {
				//$rootScope.startLoader();
				if($scope.enableChart=='N')
					return false;
				 $scope.vitalname=[];
				 $scope.vital=[];
				 angular.forEach($scope.patientVitalList, function(value1 , key1) {
					 var vitalName = {};
					 var Vital = {};
					 vitalName.vitalname=value1.vitalName;
					 Vital.vital=value1.value;
					 
					 $scope.vitalname.push(value1.vitalName);
					 $scope.vital.push(value1.value);
					 
        		});
				 		
				  $scope.drawChart();	
				// $rootScope.stopLoader();
				    };

				    $scope.drawChart=function(){	
					 var ctx = document.getElementById("line").getContext('2d');
					 var myChart = new Chart(ctx, {
					     type: 'line',
					     
					     
					     data: {
					         labels: $scope.vitalname,
					         datasets: [{
					        	 
					             label: '# of Votes',
					             data:$scope.vital,
					             backgroundColor: [
					                 'rgba(255, 99, 132, 0.2)',
					                 'rgba(54, 162, 235, 0.2 )',
					                 'rgba(255, 206, 86, 0.2)',
					                 'rgba(75, 192, 192, 0.2)',
					                 'rgba(153, 102, 255, 0.2)',
					                 'rgba(255, 159, 64, 0.2)',
					                 'rgba(256, 158, 65, 0.2)',
					                 'rgba(75, 192, 192, 0.2)',
					                 'rgba(153, 102, 255, 0.2)',
					                 'rgba(255, 159, 64, 0.2)',
					                 'rgba(256, 158, 65, 0.2)'
					             ],
					             borderColor: [
					                 'rgba(255,99,132,1)',
					                 'rgba(54, 162, 235, 1)',
					                 'rgba(255, 206, 86, 1)',
					                 'rgba(75, 192, 192, 1)',
					                 'rgba(153, 102, 255, 1)',
					                 'rgba(255, 159, 64, 1)',
					                 'rgba(54, 162, 235, 1)',
					                 'rgba(255, 206, 86, 1)',
					                 'rgba(75, 192, 192, 1)',
					                 'rgba(153, 102, 255, 1)',
					                 'rgba(255, 159, 64, 1)'
					             ],
					             borderWidth: 1
					         }]
					     },
					     options: {
					         scales: {
					             yAxes: [{
					                 ticks: {
					                     beginAtZero:true
					                 }
					             }]
					         }
					     }
					 });
				    }
				    
				    
				    $scope.getAllPatientDiagnosisForIcon=function(){
						$scope.patientDiagnosisListIcon =[];
						 var headerObjDia = {
								 	'unit_id' : $scope.unitId,
									'organization_id' : $scope.organizationId,
									'patient_id' : $scope.patientDetails.patientId,
									'Content-Type':'application/json',
					     };
						 var data={};
                         var URI = BASE_URL + ROOT_URL
                         + "/api/opd/getPatientDiagnosisByPatientIdForIcon";
                     GenericServiceParamHeader
                         .serviceAction(METHOD_GET, URI,
                                 data,
                                 NOTIFICATION_MSG_STATUS_FALSE,data,headerObjDia)
                         .then(
                                 function(response) {
                                     console.log(response);
                                     // $rootScope.startLoader();
                                     if (response.data.status == "success")
                                         $scope.patientDiagnosisListIcon = response.data.listObject; 
                                     console.log("patientDiagnosisListIcon",$scope.patientDiagnosisListIcon);
                                     	$scope.comorbidityFlag = false;
                                     	$scope.notifiableFlag = false;
										
											if($scope.patientDiagnosisListIcon.length > 0)
												{
												    $scope.complaintDelete=false;
													angular.forEach($scope.patientDiagnosisListIcon,function(value,key) {
														if(value.isNotifiable == 'Y')
														{
															$scope.notifiableFlag = true;
														}
														else if(value.isComorbidity == 'Y')
														{
															$scope.comorbidityFlag = true;
														}
													});
												}
                                     
                                 });
						}
					
				    
					$scope.searchVitalList = function() {
						var data4 = {
								patientId:$scope.patientId,
								fromDate:$scope.fromDate===undefined?null:moment($scope.fromDate).format('YYYY-MM-DD'),
								toDate:$scope.toDate===undefined?null:moment($scope.toDate ).format('YYYY-MM-DD')
						}
						
						if($scope.fromDate==undefined || $scope.toDate==undefined){
							growl.warning("select from date and to date",
									{
										title : 'Warning!'
									});
							return false;
						}else{
						$rootScope.startLoader();
						$scope.comList=[];
						var URI4 =ROOT_URL+ "/api/opd/getSearchAppointmentVitalSelfTriage";
						GenericService.serviceAction("POST", URI4, data4).then(
										function(response) {
											// $rootScope.startLoader();
											if (response.data.status == "success"){
												$rootScope.stopLoader();
												if(response.data.listObject.length==0){
													growl.warning("Vitals not available",
															{
																title : 'Warning!'
															});
												}
												$scope.patientVitalList = response.data.listObject;
												console.log("patientVitalList:",$scope.patientVitalList);
											}
										
										});
						}

					}
			
			
}]);