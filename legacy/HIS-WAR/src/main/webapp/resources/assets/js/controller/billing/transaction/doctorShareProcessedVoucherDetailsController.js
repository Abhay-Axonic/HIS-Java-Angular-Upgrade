angular.module("myApp").controller("doctorShareProcessedVoucherDetailsController", [ '$scope','$rootScope','BillingGenericService','$cookies','$state','growl','$timeout','PagerService',
	function($scope,$rootScope,BillingGenericService,$cookies,$state,growl,$timeout,PagerService){
	try{
		$rootScope.loginpage = true;
		
		$scope.init = function(){
		}
		var cookieObject = $cookies.getObject('cookieObject');
		if (cookieObject == undefined) {
			$state.go('login');
			return;
		}
		$scope.unitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		//$scope.init();
		
		
		/**DatePicker Code  */
		
		$scope.fromDate = moment().startOf('day').toDate();
	    $scope.toDate = moment().startOf('day').toDate();
	    $scope.completed='N';
		$scope.pending = 'N';
		$scope.outsourced = 'N';
		$scope.remark = "";
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
		
		/** Variables for pagination */
		$scope.offset = 0;
		$scope.noOfRecordsPerPage = 10;
		$scope.totalNoOfRecords;
		$scope.searchKeyword;
		$scope.commonListCount;
		$scope.doctorShareProcessedVoucherList = [];
		
		  $scope.initDoctorShareProcessedVoucherList = function(orgId,orgUnitId, offset, noOfRecordsPerPage) {
			  $rootScope.startLoader();
			  $scope.doctorShareProcessedVoucherList = [];
			  var fromDate=$scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
			  var toDate=$scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
			
			  var param = {"orgId":orgId,"orgUnitId":orgUnitId,"fromDate":fromDate,"toDate":toDate,"offset":offset,"recordPerPage":noOfRecordsPerPage}; 
			  
			  if($scope.firstTabLevel == 1){
				  
				  if ($scope.doctorId == '' || typeof($scope.doctorId) == "undefined" ){
					growl.warning("Please select doctor..!!",{
						title : WARNING_MSG
					});
					return false;
				  }
				  param.doctorId =  $scope.doctorId;
			  }else{
				  if ($scope.referralId == '' || typeof($scope.referralId) == "undefined" ){
						growl.warning("Please select referral..!!",{
							title : WARNING_MSG
						});
						return false;
					  }
				  param.referralId = $scope.referralId;
			  }
			   
				$rootScope.startLoader();	
			    var URI = BASE_URL + ROOT_URL + "/api/billing/getDoctorShareProcessedVoucherList"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.doctorShareProcessedVoucherList = response.data.listObject;
						 $scope.commonListCount = response.data.object;
						 $scope.setPage(1,false);
					}
					$rootScope.stopLoader();
				});
			  }
		  
		  $scope.setNoOfRecords = function() {
			  $scope.initDoctorShareProcessedVoucherList($scope.orgId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
			};
		
		 $scope.searchReferralProcessedVoucherList = function() {
			  $rootScope.startLoader();
			  $scope.doctorShareProcessedVoucherList = [];
			  var fromDate=$scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
			  var toDate=$scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
			  
				  if ($scope.referralId == '' || typeof($scope.referralId) == "undefined" ){
						growl.warning("Please select referral..!!",{
							title : WARNING_MSG
						});
						return false;
					  }
				$rootScope.startLoader();	
				  var param = {"orgId":$scope.orgId,"orgUnitId":$scope.unitId,"fromDate":fromDate,"toDate":toDate,"offset":$scope.offset,"recordPerPage":$scope.noOfRecordsPerPage,"referralId":$scope.referralId}; 
			    var URI = BASE_URL + ROOT_URL + "/api/billing/getDoctorShareProcessedVoucherList"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.doctorShareProcessedVoucherList = response.data.listObject;
						 $scope.commonListCount = response.data.object;
						 $scope.setPage(1,false);
					}
					$rootScope.stopLoader();
				});
			  }
		 
		 
		 $scope.searchDoctorShareProcessedVoucherList = function() {
			  $rootScope.startLoader();
			  $scope.doctorShareProcessedVoucherList = [];
			  var fromDate=$scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
			  var toDate=$scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
			  
			  if ($scope.doctorId == '' || typeof($scope.doctorId) == "undefined" ){
					growl.warning("Please select doctor..!!",{
						title : WARNING_MSG
					});
					return false;
				  }
				$rootScope.startLoader();	
				  var param = {"orgId":$scope.orgId,"orgUnitId":$scope.unitId,"fromDate":fromDate,"toDate":toDate,"offset":$scope.offset,"recordPerPage":$scope.noOfRecordsPerPage,"doctorId":$scope.doctorId}; 
			    var URI = BASE_URL + ROOT_URL + "/api/billing/getDoctorShareProcessedVoucherList"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.doctorShareProcessedVoucherList = response.data.listObject;
						 $scope.commonListCount = response.data.object;
						 $scope.setPage(1,false);
					}
					$rootScope.stopLoader();
				});
			  }
		  
		  $scope.getdoctorShareProcessedVoucherList = function(orgId,orgUnitId, offset, noOfRecordsPerPage) {
			  $rootScope.startLoader();
			  $scope.doctorShareProcessedVoucherList
			  var fromDate=$scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
			  var toDate=$scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD");
		
			  var param = {"orgId":orgId,"orgUnitId":orgUnitId,"fromDate":fromDate,"toDate":toDate,"offset":offset,"recordPerPage":noOfRecordsPerPage}; 
			  
			  if($scope.firstTabLevel == 1){
				  
				  if ($scope.doctorId == '' || typeof($scope.doctorId) == "undefined" ){
					growl.warning("Please select doctor..!!",{
						title : WARNING_MSG
					});
					return false;
				  }
				  param.doctorId =  $scope.doctorId;
			  }else{
				  if ($scope.referralId == '' || typeof($scope.referralId) == "undefined" ){
						growl.warning("Please select referral..!!",{
							title : WARNING_MSG
						});
						return false;
					  }
				  param.referralId = $scope.referralId;
			  }
				$rootScope.startLoader();	
			    var URI = BASE_URL + ROOT_URL + "/api/billing/getDoctorShareProcessedVoucherList"
			    BillingGenericService.serviceAction(METHOD_POST,URI,param,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
					if (response.data.status == "success"){
						 $scope.doctorShareProcessedVoucherList = response.data.listObject;
					}
					$rootScope.stopLoader();
				});
			  }
		  
			$scope.pager = {};
			$scope.page;

			$scope.setPage = function(page, flag) {
				if (page < 1 ) {
					return;
				}
				$scope.pager = PagerService.GetPager($scope.commonListCount, page,$scope.noOfRecordsPerPage);
				if (flag) {
					$scope.getdoctorShareProcessedVoucherList($scope.orgId,$scope.unitId,$scope.pager.startIndex,$scope.pager.pageSize);
				}
			}
			
		  /*========================= Serach Tab Funcationality Start==================*/
		  
		$scope.getDoctorList = function() {
				var data = {
					unitId : $scope.unitId,
					organizationId : $scope.orgId
				};
				$rootScope.startLoader();	
				var URI = BASE_URL + ROOT_URL + "/api/global/doctor/active";
				
				BillingGenericService.serviceAction(METHOD_POST, URI, data)
						.then(
								function(response) {
									$scope.doctorList = [];
									if (response.data.status == "success")
										$scope.doctorList = response.data.listObject;
										if($scope.doctorList!=null && $scope.doctorList.length>0){
											$scope.doctorId = $scope.doctorList[0].doctorId;
										}
									$rootScope.stopLoader();
								});

			}
		
		$scope.referralList = [];
		$scope.getReferralList = function() {
			var data = {};
			$rootScope.startLoader();	
			var URI = BASE_URL + ROOT_URL + "/adt/getActiveReferralList";
			
			BillingGenericService.serviceAction(METHOD_GET, URI, data)
					.then(
							function(response) {
								$scope.referralList = [];
								if (response.data.status == "success")
									$scope.referralList  = response.data.listObject;
								console.log("$scope.referralList",$scope.referralList);
									if($scope.referralList!=null && $scope.referralList.length>0){
										$scope.referralId = $scope.referralList[0].referralId;
									}
								$rootScope.stopLoader();
							});

			}
		
		  /*First Level*/
	    	
			$scope.setFirstTabLevel= function(newTab)
			{
				$scope.firstTabLevel = newTab;
				if(newTab == 1){
					$scope.offset = 0;
					$scope.noOfRecordsPerPage = 10;
					$scope.totalNoOfRecords;
					$scope.searchKeyword;
					$scope.commonListCount;
					$scope.doctorShareProcessedVoucherList = [];
					$scope.fromDate = moment().startOf('day').toDate();
				    $scope.toDate = moment().startOf('day').toDate();
					$scope.referralId = "";
					$scope.doctorId = "";
					$scope.getDoctorList();
					$timeout( function(){ 
						$scope.initDoctorShareProcessedVoucherList($scope.orgId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
					 },4000);
				}else{
					//referral
					$scope.offset = 0;
					$scope.noOfRecordsPerPage = 10;
					$scope.totalNoOfRecords;
					$scope.searchKeyword;
					$scope.commonListCount;
					$scope.doctorShareProcessedVoucherList = [];
					$scope.fromDate = moment().startOf('day').toDate();
				    $scope.toDate = moment().startOf('day').toDate();
					$scope.referralId = "";
					$scope.doctorId = "";
					$scope.getReferralList();
					$timeout( function(){
						$scope.initDoctorShareProcessedVoucherList($scope.orgId,$scope.unitId, $scope.offset,$scope.noOfRecordsPerPage);
					 },2000);
				}
			};
			
			$scope.setFirstTabLevel(1);
			
			$scope.isSetFirstTabLevel = function(tabNum)
			{
				return $scope.firstTabLevel === tabNum;
			};
			
			
	}catch(e){
		console.log(e.message);
	}
	
}]);