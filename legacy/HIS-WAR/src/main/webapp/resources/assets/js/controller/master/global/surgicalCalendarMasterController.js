/**
 * 
 */

angular.module('myApp')
		.controller("surgicalCalendarMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','GenericServiceParamHeader',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,GenericServiceParamHeader){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.templateList=[];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			$scope.specialityMasterList = [];
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			$scope.tempDiagnosisMapper=[];
			$scope.surgicalCalendarMaster=[];
			$scope.selectSurgicalCalendarMasterList=[];
			
//From Date
			$scope.dateOptions = {
					formatYear : 'yyyy',
					showWeeks : false
				};
			
				$scope.openDatePicker = function() {
					// alert("in");
					$scope.datepicker.opened = true;
				};

				$scope.datepicker = {
					opened : false
				};

				$scope.openDatePickerForNewDOA = function() {

					$scope.datepickerForNewDOA.opened = true;
				};

				$scope.datepickerForNewDOA = {
					opened : false
				};
				
//From Date End
//To Date
				$scope.dateOptions = {
						formatYear : 'yyyy',
						showWeeks : false
					};
				
				$scope.openDatePicker1 = function() {
					// alert("in");
					$scope.datepicker1.opened = true;
				};

				$scope.datepicker1 = {
					opened : false
				};

				$scope.openDatePicker1ForNewDOA = function() {

					$scope.datepicker1ForNewDOA.opened = true;
				};

				$scope.datepicker1ForNewDOA = {
					opened : false
				};
//To Date End
				
				$scope.fromDate;
				$scope.toDate;
				
				$scope.calenderDetails = {
						  "surgicalCalenderId" : "",
						  "specialityId" : "",
						  "doctorId" : "",
						  "fromDate" : "",
						  "toDate" : "",
						  "dayId" : "",
						  "day": "",
						  "fromTime" : "",
						  "toTime" : "",
						  "status" : "A",
						  "createdBy" : $scope.userId,
						  "createdDate" : "",
						  "updatedBy" : $scope.userId,
						  "updatedDate" : "",
						  "organisationId" : $scope.organisationId,
						  "unitId" : $scope.unitId,
						  "isDeleted" : "N",
						  "calenderStatusId" : "1",
						  "procedureDate" : ""
						}
				
				
				$scope.addDays = function()
				{
					$scope.calenderDetailsList = [];
					var day1 = angular.copy($scope.calenderDetails);
					day1.day = "Monday";
					day1.dayId = 1;
					$scope.calenderDetailsList.push(day1);
					
					var day2 = angular.copy($scope.calenderDetails);
					day2.day = "Tuesday";
					day2.dayId = 2;
					$scope.calenderDetailsList.push(day2);
					
					var day3 = angular.copy($scope.calenderDetails);
					day3.day = "Wednesday";
					day3.dayId = 3;
					$scope.calenderDetailsList.push(day3);
					
					
					var day4 = angular.copy($scope.calenderDetails);
					day4.day = "Thursday";
					day4.dayId = 4;
					$scope.calenderDetailsList.push(day4);
					
					
					var day5 = angular.copy($scope.calenderDetails);
					day5.day = "Friday";
					day5.dayId = 5;
					$scope.calenderDetailsList.push(day5);
					
					
					var day6 = angular.copy($scope.calenderDetails);
					day6.day = "Saturday";
					day6.dayId = 6;
					$scope.calenderDetailsList.push(day6);
					
					var day7 = angular.copy($scope.calenderDetails);
					day7.day = "Sunday";
					day7.dayId = 7;
					$scope.calenderDetailsList.push(day7);
				}
				
				
				
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				
				$scope.addDays();
				$scope.selectedDayList= [];
				
				
				
			
				$scope.surgicalCalenderMaster =
				{
						  "specialityId" : "",
						  "doctorId" : "",
						  "fromDate" : "",
						  "toDate" : "",
						  "createdBy" : $scope.userId,
						  "createdDate" : "",
						  "updatedBy" : $scope.userId,
						  "updatedDate" : "",
						  "organisationId" : $scope.organizationId,
						  "unitId" : $scope.unitId,
						  "doctorName" : "",
						  "specialityName" : "",
						  "dayName" : "",
						  "surgicalCalendarDetailsDtoList" :[]
				}
			}
			$scope.initMaster();
			
        $scope.initTime = function(){
        	$('.clockpicker').clockpicker();
        }
			
			//Get Day List end
			
			//------------ Get Speciality Master List ------------//
			$scope.getSpecialityMasterList = function(){
				try{
					var URI = BASE_URL + ROOT_URL + GETACTIVESPECIALITY;
					var obj = {
								organizationId : $scope.organizationId,
								unitId         :$scope.unitId
						    }
					BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
						console.log(response);
						if(response.data.status == "success")
							$scope.specialityMasterList = response.data.listObject;
						console.log("speciality",$scope.specialityMasterList);
					});
				}catch (e) {
					console.log("Exception",e.message);
				}
			}
			$scope.getSpecialityMasterList();
			
			//------------ Get Doctors By SpecialityId ----------//
			$scope.getDoctorsBySpecialityId = function(specialityId){
				$rootScope.startLoader();
//				alert(specialityId);
				//$scope.unitService.subSpecialityId="";
				$scope.doctorList=[];
				  var URI = BASE_URL + ROOT_URL + GETDOCTORBYSPECIALITYID;
				  var obj = {
							organizationId : $scope.organizationId,
							unitId         : $scope.unitId,
							specialityId   : specialityId
					    }
				BillingGenericService.serviceAction(METHOD_POST,URI, obj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
					console.log("Doctor list",response);
					if(response.data.status == "success")
						$scope.doctorList = response.data.listObject;
				});
			}
			
			
			
			/**Crud Operations Start**/
		
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			
			$scope.initMasterList = function(organisationId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
								orgId : $scope.organisationId,
								unitId         : $scope.unitId,
								offset         : offset,
								recordPerPage : noOfRecordsPerPage,
								status:"A"
				    };
				
				var data2 = "";
				
				var URI1 = BASE_URL + ROOT_URL +"/api/otims/surgicalCalender/list"; 
				var URI2 = BASE_URL + ROOT_URL +"/api/otims/surgicalCalender/list/count";
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
					}
				});
				
				GenericService.serviceAction("POST",URI2,data1).then(function(response){
					if(response.data.status == "success"){					
						$rootScope.stopLoader();
						$scope.commonListCount = response.data.object;
						$scope.setPage(1,false);
						console.log("commonListCount",response.data.object);
					}
				});
				
			}		
			
			$scope.masterList = function(organisationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						orgId : $scope.organisationId,
						unitId         : $scope.unitId,
						offset         : offset,
						recordPerPage : noOfRecordsPerPage,
						status:"A"
		             };
				
				var URI = BASE_URL + ROOT_URL + "/api/otims/surgicalCalender/list";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) 
				{
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
				});
			}

			$scope.pager = {};
			$scope.page;

			$scope.setPage = function(page, flag) {
				if (page < 1 || page > $scope.pager.totalPages)
				{
					return;
				}
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage,$scope.unitId);
				if (flag)
				{
					$scope.masterList($scope.organisationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
				}
			}

			$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			
			/**List And Pagination End**/
	
			
			$scope.save = function() 
			{
				$scope.$broadcast('show-errors-check-validity');

				
			
					try {
						
						$rootScope.startLoader();
						$scope.fromTimeFlag = false;
						$scope.toTimeFlag = false;
						$scope.surgicalCalenderMaster.fromDate = $scope.fromDate!=undefined?moment($scope.fromDate).format("YYYY-MM-DD"):"";
					    $scope.surgicalCalenderMaster.toDate = $scope.toDate!=undefined?moment($scope.toDate).format("YYYY-MM-DD"):"";
					    $scope.surgicalCalenderMaster.surgicalCalendarDetailsDtoList = $scope.selectedDayList;
					    
					    angular.forEach( $scope.surgicalCalenderMaster.surgicalCalendarDetailsDtoList,function(value,index){
					    	if(value.fromTime==""){
					    		$scope.fromTimeFlag = true;
					    	}
					    	if(value.toTime==""){
					    		$scope.toTimeFlag = true;
					    	}
					    });
					    
					    if($scope.fromTimeFlag){
					    	
					    	$rootScope.stopLoader();
							growl.warning("Please Enter From Time",
									{
										title : 'warning!'
									});
							return false;
					    }
					    if($scope.toTimeFlag){
					    	
					    	$rootScope.stopLoader();
							growl.warning("Please Enter To Time",
									{
										title : 'warning!'
									});
							return false;
					    }
						console.log("surgicalCalenderMaster",JSON.stringify($scope.surgicalCalenderMaster));
						$rootScope.stopLoader();
						if ($scope.surgicalCalendarform.$valid) {
						var URI = BASE_URL + ROOT_URL + "/api/otims/surgicalCalender";
					   GenericService.serviceAction("POST", URI, $scope.surgicalCalenderMaster).then(function(response) 
						{	
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								$scope.$broadcast('show-errors-reset');
								$scope.initMaster();
								$scope.fromDate= undefined;
								$scope.toDate=undefined;
								angular.forEach($scope.calenderDetailsList,function(value,index){
									$('#checkbox'+index).prop('checked', false); 
								});
								
								$scope.getSpecialityMasterList();
								$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
							
						});
						}
					} catch (e) {
						console.log("Exception",e.message);
					}
					
//				}
			}
			

			$scope.getCalenderSlotsByProcedure = function(obj)
			{
				try {
					$rootScope.startLoader();
					var data={
							  "recordPerPage" : 0,
							  "offset" : 0,
							  "procedureDate" : obj.procedureDate,
							  "orgId" : $scope.organisationId,
							  "unitId" : $scope.unitId,
							  "dayId":obj.dayId,
							  "doctorId" : obj.doctorId,
							  "specialityId" : obj.specialityId,
							  "status" : "A"
							}
					var URI = BASE_URL + ROOT_URL +"/api/otims/surgicalCalender/details";
					console.log("ACT_INACT_URI",URI);
					GenericService.serviceAction("POST", URI, data).then(function(response) 
					{
						$scope.calenderSlotDetailsList= [];
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							$scope.calenderSlotDetailsList = response.data.listObject;
							$('#calenderDetailsPopup').modal('show');
							$scope.$broadcast('show-errors-reset');
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				} catch (e) {
					console.log("Exception",e.message);
				}
			}
			
			
			$scope.updateStatus = function(id,status)
			{
				try {
					$rootScope.startLoader();
					$scope.initSurgicalCalendarMasterList();
					var URI = BASE_URL + ROOT_URL +"/api/otims/surgicalCalender/"+id+"/status/"+status;
					console.log("ACT_INACT_URI",URI);
					GenericService.serviceAction("GET", URI, $scope.surgicalCalendar).then(function(response) 
					{
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				} catch (e) {
					console.log("Exception",e.message);
				}
			}
			
			$scope.updateStatusByProcedureDate = function(obj)
			{
				try {
					$rootScope.startLoader();
					$scope.initSurgicalCalendarMasterList();
					
					var data={
							  "recordPerPage" : 0,
							  "offset" : 0,
							  "procedureDate" : obj.procedureDate,
							  "orgId" : $scope.organisationId,
							  "unitId" : $scope.unitId,
							  "dayId":obj.dayId,
							  "doctorId" : obj.doctorId,
							  "specialityId" : obj.specialityId,
							  "status" : "I"
							}
					var URI = BASE_URL + ROOT_URL +"/api/otims/surgicalCalender/all/status";
					console.log("ACT_INACT_URI",URI);
					GenericService.serviceAction("POST", URI, data).then(function(response) 
					{
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.initMaster();
							$scope.initMasterList($scope.organisationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				} catch (e) {
					console.log("Exception",e.message);
				}
			}
			
			
			/**Crud Operations End**/
}]);