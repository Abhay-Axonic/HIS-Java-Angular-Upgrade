(function () {
    "use strict";

    function amendmentWorklistController($scope, $rootScope, $state, $cookies,GenericService, BillingGenericService ,PagerService, promiseFactoryWithObject,$sessionStorage, growl,reportService,$timeout,GenericServiceFileUpload,$stateParams,$filter) {
        

		$rootScope.loginpage = true;
		$scope.LISDynamicLabel = "worklistForReportRelease";
		
		$scope.firstLevel = true;
		$scope.secondLevel = true;
		$scope.thirdLevel = false;
		$scope.sendMailButton = false;

		/** Variables for pagination */
		$scope.offset = 0;
		$scope.noOfRecordsPerPage = 50;
		$scope.resultEntryworklistForValidation  = [];
		$scope.totalNoOfRecords;
		$scope.searchKeyword;
		$scope.commonListCount;
		$scope.labResultTestDtls;
		$scope.patientDertails = {};
		$scope.amendReason = "";
		/*** Check user is Loggedin */
		var cookieObject,cookieObjectAppConfig;
		[cookieObject,cookieObjectAppConfig] = $scope.checkCookies();
		$scope.orgUnitId = cookieObject.unitId;
		$scope.orgId = cookieObject.organizationId;
		$scope.userId = cookieObject.userId;
		$scope.blockReportPrint =  cookieObjectAppConfig.blockReportPrint;
		$scope.getUserListByRoleType($scope.orgUnitId,$scope.orgId);
		
		/*** Lab Configuration */
		var labConfig  =    $scope.checkLabConfig();
		$scope.getUnitByOrganization();
		$scope.assignUnit = $scope.orgUnitId;
		$scope.deptId = labConfig.labSpecialityId;
		$scope.photoRequired = labConfig.photoRequired;
		$scope.labSortingId =  labConfig.labSortingId;
		$scope.microbiologyId = labConfig.microbiologyId;
		$scope.subDeptId = 0;
	    $scope.organismGroupId = "";
	    $scope.organismId = "";
		$scope.listHelpValueMaster = [];
		var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
		if (cookieObjectAppConfigSecond != undefined) {
		$scope.isBillManipulationAllowedUserLevel = cookieObject.isBillManipulationAllowedUserLevel;
		$scope.isBillManipulationAllowedAppLevel = cookieObjectAppConfigSecond.isBillManipulationAllowedAppLevel;
		}
		$scope.isBillManipulationAllowed =  ($scope.isBillManipulationAllowedUserLevel && $scope.isBillManipulationAllowedAppLevel =='Y') ? true : false;
		if($scope.isBillManipulationAllowed == true){
			$scope.filterDummyService = 1;
		}else{
			$scope.filterDummyService = 2;
		}
		
	    var specDocObj = {
    			organizationId:$scope.organizationId,
    			userId:$scope.userId
        }
	    var URI = BASE_URL + ROOT_URL+ "/adt/getSpecialityListByUserIdByUnitId";
        BillingGenericService.serviceAction(METHOD_POST, URI,specDocObj,
				NOTIFICATION_MSG_STATUS_FALSE)
		.then(function(response) {
						if (response.data.status == "success"){
							$scope.staffSpecialityList = [];
							$scope.staffSubSpecialityList = [];
                            $scope.staffSpecialityList = response.data.listObject;
						    $scope.staffSubSpecialityList = response.data.object;
						}
                     });
        
	
		
		
		$scope.listLabResultMasterDto  = [];
		$scope.labResultMasterDto = {
				"labTestResId": "",
	            "orgId": $scope.orgId,
	            "orgUnitId":$scope.orgUnitId,
	            "testId": "",
	            "deptId": $scope.deptId,
	            "patientId": "",
	            "visitTypeId": "",
	            "visitAdmId": "",
	            "orderDetailsId": "",
	            "sampleNo": "",
	            "labSampleDtlsId": "",
	            "resultEnterDatetime": "",
	            "resultEnterBy": "",
	            "resultValidatedDatetime": "",
	            "resultValidatedBy": "",
	            "resultAuthorisedDatetime": "",
	            "resultAuthorisedBy": "",
	            "resultAuthorisedFlag": "",
	            "resultHandoverDatetime": "",
	            "resultHandoverBy": "",
	            "suggetionNotes": "",
	            "footsNotes": "",
	            "wardCode":"",
	            "bedNumber":"",
	            "sampleType":"",
	            "testCode":"",
	            "createdBy": $scope.userId,
	            "createdDate": "",
	            "updatedBy": $scope.userId,
	            "updatedDate": "",
	            "sampleBarcode": "",
	            "labSampleNo": "",
	            "subDeptId":"",
	            "visitType": "",
	            "uhid": "",
	            "patientDetails": "",
	            "doctorDetails": "",
	            "testDesc": "",
	            "priorityName": "",
	            "colorCode": "",
	            "panelCode":"",
	            "orderDateTime":"",
	            "sampleCollectionDatetime":"",
	            "panelId":"",
	            "genderId":"",
	            "resultLevel":"",
	            "sampleStatusId" :"",
	            "listLabSampleResultDetailsMaster":[]
		}

	
		$scope.setNoOfRecords = function() {
			$scope.initReportHandover($scope.orgId,
					$scope.orgUnitId,$scope.deptId,$scope.offset,
					$scope.noOfRecordsPerPage);
		};

		$scope.initReportHandover = function(orgId,
				orgUnitId,deptId, offset, noOfRecordsPerPage) {
			try {
				$rootScope.startLoader();
			
				var data1 = {
						"orgId":orgId,
						"orgUnitId" :$scope.assignUnit,
						"assignUnit" :$scope.orgUnitId ,
						"deptId":$scope.deptId,
						"subDeptId":$scope.subDeptId,
						"fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						"toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						"isMailSend" : $scope.isMailSend!=""?  $scope.isMailSend: null,
							
					};
					
				var data = {
						  "orgId" : $scope.orgId,
						  "orgUnitId" :$scope.assignUnit,
						  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						  "patientId" :  $scope.selectedPatient,
						  "labSampleDtlsId" :0,
						  "visitTypes" :$scope.selectedVisitType,
						  "testTypes" : $scope.selectedtestList,
						  "deptId" : $scope.deptId,
						  "subDeptId" : $scope.subDeptId,
						  "subDepts" : $scope.selectedDepartMentList,
						  "assignUnit" :$scope.orgUnitId ,
						  "offset":$scope.offset,
						  "recordPerPage":$scope.noOfRecordsPerPage,
						  "isMailSend" : $scope.isMailSend!=""?  $scope.isMailSend: null,
						  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
						  "userId" : $scope.userId,
						  "uhidNumber" : $scope.uhid
             }
				var URI1 = BASE_URL 
		        + ROOT_URL
				+ '/LIS/ReportHandOver/ReportHandOverList/Search'
				
				var URI2 = BASE_URL
						+ ROOT_URL
						+ LIS_REPORT_HAND_OVER
						+ LIST_FOR_REPORT_HANDOVER_RELEASE_COUNT;

				promiseFactoryWithObject
						.setPromisesWithObject(URI1, URI2, METHOD_POST,
								METHOD_POST,data,data1)
						.then(
								function(response) {
									$rootScope.stopLoader();
									 $scope.resultReportHandOver=[];
									 $scope.resultReportHandOverTest=[];
									$scope.resultReportHandOverTest = response[0].listObject;
									if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
										angular.forEach($scope.resultReportHandOverTest, function(value, key) {
											angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
												if(value.subDeptId == value1.subSpecialityMasterId){
													$scope.resultReportHandOver.push(value);
												}
											
											});		
										 });
									}else{
										$scope.resultReportHandOver = response[0].listObject;
									}
								
										
									angular.forEach($scope.resultReportHandOver,function(value,key){
										value.selectOnlyOneVisitType = true;
									});
									$scope.resultReportHandOverForFiltter = $scope.resultReportHandOver;
									$scope.filterByDummyServices();
									$scope.commonListCount = response[1].object;
									$scope.setPage(1,false);
									

								});
			} catch (e) {
				console.log(e.message);
			}
		}

		$timeout( function(){
			 if($stateParams.searchObj!=undefined && $stateParams.searchObj!=null && $stateParams.searchObj.assignUnit!=null){
				 $scope.assignUnit = $stateParams.searchObj.assignUnit;
					$scope.fromDate = $stateParams.searchObj.fromDate;
					$scope.toDate = $stateParams.searchObj.toDate;
					$scope.patientId = $stateParams.searchObj.patientId;
					$scope.selectedPatient = $stateParams.searchObj.selectedPatient;
					$scope.uhid = $stateParams.searchObj.uhid;
					$scope.selectedDepartMentList = $stateParams.searchObj.selectedDepartMentList;
					$scope.selectedVisitType = $stateParams.searchObj.selectedVisitType;
					$scope.selectedtestList = $stateParams.searchObj.selectedtestList;
				}
		$scope.initReportHandover($scope.orgId,
				$scope.orgUnitId,$scope.deptId,$scope.offset,
				$scope.noOfRecordsPerPage);
		},1000);

		$scope.getReportHandOverList = function(orgId,
				orgUnitId,deptId,offset,noOfRecordsPerPage) {
			try {
				$rootScope.startLoader();
				var data = {
						  "orgId" :orgId,
						  "orgUnitId" :$scope.assignUnit,
						  "assignUnit" :$scope.orgUnitId ,
						  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						  "patientId" :  $scope.selectedPatient,
						  "labSampleDtlsId" : $scope.labSampleDtlsId,
						  "visitTypes" :$scope.selectedVisitType,
						  "testTypes" : $scope.selectedtestList,
						  "deptId" : $scope.deptId,
						  "subDeptId" : $scope.subDeptId,
						  "subDepts" : $scope.selectedDepartMentList,
						  "offset":offset,
						  "recordPerPage":noOfRecordsPerPage,
						  "isMailSend" : $scope.isMailSend!=""?  $scope.isMailSend: null,
						  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
						  "userId" : $scope.userId,
						  "uhidNumber" : $scope.uhid
             };
			
			var URI = BASE_URL 
			        + ROOT_URL
					+ '/LIS/ReportHandOver/ReportHandOverList/Search';
				
				console.log("URI", URI);

				GenericService
						.serviceAction(METHOD_POST, URI, data)
						.then(
								function(response) {
									$rootScope.stopLoader();
									 $scope.resultReportHandOver=[];
									 $scope.resultReportHandOverTest=[];
									$scope.resultReportHandOverTest = response.data.listObject;
									
									if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
										angular.forEach($scope.resultReportHandOverTest, function(value, key) {
											angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
												if(value.subDeptId == value1.subSpecialityMasterId){
													$scope.resultReportHandOver.push(value);
												}
											
											});		
										 });
									}else{
										$scope.resultReportHandOver = response.data.listObject;
									}
									angular.forEach($scope.resultReportHandOver,function(value,key){
										value.selectOnlyOneVisitType = true;
									});
									
									$scope.resultReportHandOverForFiltter = $scope.resultReportHandOver;
									$scope.filterByDummyServices();
								});
				
			} catch (e) {
				console.log(e.message)
			}

		}
		
		$scope.filterByDummyServices= function()
        {
			$scope.resultReportHandOver  =[]
            if($scope.filterDummyService==1)//all 
            {
            	$scope.resultReportHandOver = angular.copy($scope.resultReportHandOverForFiltter);
            	
            }else if($scope.filterDummyService==2)//real
            {
                angular.forEach($scope.resultReportHandOverForFiltter, function (value, key) 
                {
                    if(value.isDummyService==false)
                    	$scope.resultReportHandOver.push(value);
                });
                 
                
            }else if($scope.filterDummyService==3 )//dummy
            {
            	  angular.forEach($scope.resultReportHandOverForFiltter, function (value, key) 
                          {
                              if(value.isDummyService==true)
                            	  $scope.resultReportHandOver.push(value);
                          });
                           
            }
        }
		
		
		$scope.clearFields = function()
		{
			$scope.patientId = undefined;
			$scope.selectedPatient = null;
			$scope.selectedVisitType = [];
			$scope.orderStatusId = '';
			$scope.priorityId = '';
			$scope.uhid = '';
			$scope.selectedtestList = [];
			$scope.selectedDepartMentList = [];
			$scope.fromDate = moment().toDate();
		    $scope.toDate = moment().toDate();
		    $scope.isMailSend = "";
			$scope.initReportHandover($scope.orgId,
					$scope.orgUnitId,$scope.deptId,$scope.offset,
					$scope.noOfRecordsPerPage);
		};
		
		
		$scope.patientList = [];
		
			 $scope.patientName = "";
			 
	
	
		$scope.pager = {};
		$scope.page;
		$scope.setPage = function(page, flag)
		{
			if (page < 1 )
			{
				return;
			}
			$scope.pager = PagerService.GetPager(
					$scope.commonListCount, page,
					$scope.noOfRecordsPerPage);
			if (flag) {
				$scope.getReportHandOverList(
						$scope.orgId, $scope.orgUnitId,$scope.deptId,
						$scope.pager.startIndex,
						$scope.pager.pageSize);
			}
		}
		
/*====================================SEARCH DETAILS=====================================*/
$scope.init = function(){
			
			$scope.listSensitivityTestResultMasterDto = [];
			$scope.listSensitivityTestResultDetailsMasterDto = [];
			$scope.listSensitivityTestResultSubDetailsMasterDto = [];
			$scope.labSampleDtlsId =0;
			$scope.SensitivityTestResultMasterDto = 
			{
					  "sensitivityResultId" : "",
					  "orgId" : $scope.orgId ,
					  "orgUnitId" : $scope.orgUnitId,
					  "createdBy" : $scope.userId,
					  "createdDate" : "",
					  "updatedBy" : "",
					  "updatedDate" : "",
					  "isDeleted" : 'N',
					  "checkTest" : "Y",
					  "labSampleDtlsId" : "",
					  "listSensitivityTestResultDetailsMaster" : $scope.listSensitivityTestResultDetailsMasterDto
			}
			
			$scope.SensitivityTestResultDetailsMasterDto = 
			{
					  "sesitivityResultDetailsId" : "",
					  "sensitivityResultId":"",
					  "orgId" : $scope.orgId ,
					  "orgUnitId" : $scope.orgUnitId,
					  "organismGroupId":"",
					  "organismGroupName":"",
					  "organismId" : "",
					  "organismName":"",
					  "selectedAll":"",
					  "createdBy" : $scope.userId,
					  "createdDate" : "",
					  "updatedBy" : "",
					  "updatedDate" : "",
					  "isDeleted" : "N",
					  "listSensitivityTestResultSubDetailsMaster" : $scope.listSensitivityTestResultSubDetailsMasterDto
			}
			
			$scope.flagCheck=true;
			$scope.SensitivityTestResultSubDetailsMasterDto = 
				{
					  "sesitivityResultSubDetailsId" : "",
					  "orgId" : $scope.orgId,
					  "orgUnitId" : $scope.orgUnitId,
					  "antibioticId" : "",
					  "antibioticName" : "",
					  "isChecked":"",
					  "sensitivityId":"",
					  "microLabPriorityId" : "",
					  "mic":"",
					  "remark":"",
					  "createdDate":"",
					  "createdBy" : $scope.userId,
					  "updatedBy" : "",
					  "updatedDate" : "",
					  "isDeleted" : "N",
					  "sesitivityResultDetailsId":""
				}
			

		}
		$scope.init();
		
		
		$scope.sampleStatusIds=[REPORT_RELEASED];
		$scope.selectedVisitTypeIds = [1,2];
		$scope.departmentList= [];
		$scope.visityTypeList = [];
		$scope.testList = [];
		
		$scope.selectedVisitType = [];
		$scope.selectedtestList = [];
		$scope.selectedDepartMentList = [];
      /**DatePicker Code  */
		
		$scope.fromDate = moment().toDate();
	    $scope.toDate = moment().toDate();
		
		$scope.dateOptions =
		{
			formatYear : 'yyyy',
			showWeeks : false,
			maxDate:new Date()
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
	    $scope.dropDownSetting = {
	    		scrollableHeight: '100px', 
	    		scrollable: true
	    }; 
	    
	    $scope.multiselectObj = {
	    		"id":"",
	    		"label":""
	    }
	    
	/** Service Autocomplete List * */
	$scope.getSendToCRList = function(searchKeyword)
	{
	   	if(searchKeyword.length == 3)
		{
		try 
		{
			var data = { 
					"orgId":$scope.orgId,
					"orgUnitId":$scope.orgUnitId,
					"visitTypes":$scope.selectedVisitTypeIds,
					"sampleStatusIds":$scope.sampleStatusIds,
					"searchKeyword":searchKeyword,
					"deptId":$scope.deptId,
					"subDeptId":$scope.subDeptId,
			}
			
			var URI = BASE_URL 
			        + ROOT_URL
					+ LIS_BIOCHEMISTRY
					+ BIOCHECMISTRY_PATIENT_LIST_SEARCH;
			console.log("URI", URI);
			return GenericService.serviceAction("POST", URI,
					data).then(function(response) {
				console.log("response.data.listObject",response.data.listObject);
				$scope.patientListNew =response.data.listObject;
				return response.data.listObject;
			});
		} catch (e) {
			console.log(e.message)
		}
	}	else {
        var filteredList = $filter('filter')($scope.patientListNew, { $: searchKeyword });
        if (filteredList.length === 0) {
            $scope.filteredPatientList = null;
        } else {
            $scope.filteredPatientList = filteredList;
        }
        return $scope.filteredPatientList;
    }
};
	    $scope.selectPatient = function($item, $model,$label)
		{
	      $scope.selectedPatient = $item.id;
	     // $scope.PatientName= $item.firstName +"  "+$item.lastName;
	      $scope.uhid=$item.uhid;
        }
	    
	
	    
	    
	/*get Visi tType List*/
	$scope.getVisitTypeList = function()
	{
		try 
		{
			var data = {
					"orgId":$scope.orgId,
					"orgUnitId":$scope.orgUnitId,
					"deptId":$scope.deptId,
					"subDeptId":$scope.subDeptId,
			}
			
			var URI = BASE_URL 
			        + ROOT_URL
					+ LIS_HISTOPATHOLOGY
					+ HISTO_MICRO_VISIT_LIST
			console.log("URI", URI);
			 GenericService.serviceAction("POST", URI,
					data).then(function(response) {
				console.log("response.data.listObject",response.data.listObject);
				angular.forEach(response.data.listObject, function(value, key) {
					var multiselectObj = angular.copy($scope.multiselectObj);
					multiselectObj.id = value.id;
					multiselectObj.label = 	value.label;
						$scope.visityTypeList.push(multiselectObj);
					});
				
				
			});
		} catch (e) {
			console.log(e.message)
		}
	}
	
	$scope.getVisitTypeList();
	
	
	$scope.getTestList = function()
	{
		try 
		{
			var data = {
					"orgId":$scope.orgId,
					"orgUnitId":$scope.orgUnitId,
					"deptId":$scope.deptId,
					"subDeptId":$scope.subDeptId
			}
			
			var URI = BASE_URL 
			        + ROOT_URL
					+ LIS_HISTOPATHOLOGY
					+ HISTO_MICRO_TEST_LIST
			console.log("URI", URI);
			 GenericService.serviceAction("POST", URI,
					data).then(function(response) {
				console.log("response.data.listObject",response.data.listObject);
				$scope.testList =  response.data.listObject;
			});
		} catch (e) {
			console.log(e.message)
		}
	}
	$scope.getTestList();
	
	$scope.getDepartMentList = function(orgId,orgUnitId) {
		try {
			var data = "";
			var URI = BASE_URL + ROOT_URL
					+ LIS_COMMON + GET_DEPARMENT_LIST
					+ S + orgId + S + orgUnitId;
			console.log("URI", URI);
			GenericService.serviceAction("GET", URI, data)
					  .then(function(response) {
						  
						  if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
							  angular.forEach($scope.staffSubSpecialityList, function(value, key) {
									var multiselectObj = angular.copy($scope.multiselectObj);
									multiselectObj.id = value.subSpecialityMasterId;
									multiselectObj.label = 	value.subSpecialityMasterName;
										$scope.departmentList.push(multiselectObj);
									});
						  }else{
							  angular.forEach(response.data.listObject, function(value, key) {
									var multiselectObj = angular.copy($scope.multiselectObj);
									multiselectObj.id = value.id;
									multiselectObj.label = 	value.name;
										$scope.departmentList.push(multiselectObj);
									});
		                      }
					  });
		   } catch (e) {
			console.log(e.message)
		}
	}
	$scope.getDepartMentList($scope.orgId,$scope.orgUnitId);
	
	
	
	
	 $scope.searchOutPatientList = function()
		{
			try 
			{
				$rootScope.startLoader();
				var searchObj = {
							  "orgId" : $scope.orgId,
							  "orgUnitId" :$scope.assignUnit,
							  "assignUnit" :$scope.orgUnitId ,
							  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
							  "patientId" :  $scope.selectedPatient,
							  "labSampleDtlsId" : 0,
							  "visitTypes" :$scope.selectedVisitType,
							  "testTypes" : $scope.selectedtestList,
							  "deptId" : $scope.deptId,
							  "subDeptId" : $scope.subDeptId,
							  "subDepts" : $scope.selectedDepartMentList,
							  "offset":$scope.offset,
							  "recordPerPage":$scope.noOfRecordsPerPage,
							  "isMailSend" : $scope.isMailSend!=""?  $scope.isMailSend: null,
							  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null,
							  "userId" : $scope.userId,
							  "uhidNumber":$scope.uhid
                 };
				
				var data1 = {
						"orgId":$scope.orgId,
						"orgUnitId" :$scope.assignUnit,
						"assignUnit" :$scope.orgUnitId ,
						"deptId":$scope.deptId,
						"subDeptId":$scope.subDeptId,
						"fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						"toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
						"isMailSend" : $scope.isMailSend!=""?  $scope.isMailSend: null
					};
				
				var URI = BASE_URL 
				        + ROOT_URL
						+ '/LIS/ReportHandOver/ReportHandOverList/Search'
				console.log("URI", URI);
				
				var URI2 = BASE_URL
				+ ROOT_URL
				+ LIS_REPORT_HAND_OVER
				+ LIST_FOR_REPORT_HANDOVER_RELEASE_COUNT;

		promiseFactoryWithObject
				.setPromisesWithObject(URI, URI2, METHOD_POST,
						METHOD_POST,searchObj,data1)
				.then(
						function(response) {
							$rootScope.stopLoader();
							 $scope.biochemWorklist=[];
							 $scope.resultReportHandOver=[];
							 $scope.resultReportHandOverTest=[];
							$scope.resultReportHandOverTest = response[0].listObject;
							
							if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
								angular.forEach($scope.resultReportHandOverTest, function(value, key) {
									angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
										if(value.subDeptId == value1.subSpecialityMasterId){
											$scope.resultReportHandOver.push(value);
										}
									
									});		
								 });
							}else{
								$scope.resultReportHandOver = response[0].listObject;
							}
								
							angular.forEach($scope.resultReportHandOver,function(value,key){
								value.selectOnlyOneVisitType = true;
							});
							$scope.resultReportHandOverForFiltter = $scope.resultReportHandOver;
							$scope.filterByDummyServices();
							$scope.commonListCount = response[1].object;
							$scope.setPage(1,false);
						});
		
			} catch (e) {
				console.log(e.message)
			}
		}
	 
	 
	 $scope.testObj = {};
	 
	 $scope.amendment = function(obj){
		 
		 if($scope.amendReason ==  undefined || $scope.amendReason == null || $scope.amendReason ==""){
			 
				growl.warning("Please enter amend reason",
						{
							title : 'warning!'
						});
				
				return false;
		 }
		 
		 var dataObj ={};
		 dataObj.labSampleDtlsId = obj.labSampleDtlsId;
		 dataObj.createdBy = $scope.userId;
		 dataObj.amendReason =$scope.amendReason;
		 var updateAmend = true;
		 
		 if(labConfig.serologyId != obj.subDeptId && labConfig.histopathologyId!= obj.subDeptId && labConfig.microbiologyId != obj.subDeptId)
			{
			 
			 dataObj.testType = 1;
			}else if(labConfig.serologyId == obj.subDeptId)
			{
				 dataObj.testType = 2;
			}else if(labConfig.histopathologyId==obj.subDeptId || labConfig.cystopathologyId==obj.subDeptId)
			{
				updateAmend = false;
					
			}else if(labConfig.microbiologyId==obj.subDeptId)
			{
				 dataObj.testType = 3;
			}
		 
		 if(updateAmend == true){
			 
				var URI = BASE_URL+ ROOT_URL+ "/LIS/transaction/amendReportReason";
				GenericService.serviceAction("POST", URI,dataObj).then(function(response) {
					$rootScope.stopLoader();
				if(response.data.object != null){
					$scope.amendReason = null;
					 $scope.selectedRepReleaseEntryList =[];
					
					                $rootScope.startLoader();
					          
					                	obj.createdBy = $scope.userId;
											
											var searchObj = {};
											 searchObj.assignUnit = $scope.assignUnit;
											 searchObj.fromDate = $scope.fromDate;
											 searchObj.toDate = $scope.toDate;
											 searchObj.patientId = $scope.patientId;
											 searchObj.selectedPatient = $scope.selectedPatient;
											 searchObj.uhid = $scope.uhid;
											 searchObj.selectedDepartMentList = $scope.selectedDepartMentList;
											 searchObj.selectedVisitType = $scope.selectedVisitType;
											 searchObj.selectedtestList = $scope.selectedtestList;
											 searchObj.selectedSampleNo = $scope.selectedSampleNo;
											 searchObj.amend = true;
										 $scope.selectedRepReleaseEntryList.push(obj);
										 if(labConfig.serologyId != obj.subDeptId && labConfig.histopathologyId!= obj.subDeptId && labConfig.microbiologyId != obj.subDeptId)
											{
											
											 $timeout(function () {
												 $rootScope.stopLoader();
												 $state.go('deatilsReportEntryWorklist',
															{
															  subDeptId : obj.subDeptId,
															  resultMstList : $scope.selectedRepReleaseEntryList,
															  searchObj : searchObj
															});

												 }, 2000);
											 
											 
											}else if(labConfig.serologyId == obj.subDeptId)
											{
												
												$timeout(function () {
													 $state.go('detailsSerologyReportEntry',
														{
													 		resultMstList : $scope.selectedRepReleaseEntryList,
													 		searchObj : searchObj,
														});
													 
												 }, 2000);
												
												
											}else if(labConfig.microbiologyId==obj.subDeptId)
											{
												$scope.amendReason = null;
													  $scope.testObj = {};
													  $scope.patientDertails.doctorDetails =obj.doctorDetails;
			                                          $scope.patientDertails.patientDetails =obj.patientDetails;
			                                          $scope.patientDertails.priorityName =obj.priorityName;
			                                          $scope.patientDertails.uhid = obj.uhid;
			                                          $scope.patientDertails.genderId = obj.genderId;
			                                          $scope.patientDertails.imagePath = obj.imagePath;
			                                          $scope.testObj = obj;
			                                          $scope.getSensitivityCheckTestDetails(obj.labSampleDtlsId);	
			                                          $scope.getBiochemResultEntryDetails();
											}
					
				}
			});
			 
		 }else{
			 

				var newarr ={};
				newarr.labSampleDtlsId = obj.subDeptId.labSampleDtlsId;
				var URI = BASE_URL+ ROOT_URL+ "/LIS/histopathology/microscopic/examination/labSampleDetails";
					GenericService.serviceAction("POST", URI,newarr).then(function(response) {
						$rootScope.stopLoader();
					if(response.data.object != null){
						
						 $scope.tSubSpecimanMaster = response.data.object;
						 $scope.tSubSpecimanMaster.orgId = $scope.orgId;
						 $scope.tSubSpecimanMaster.orgUnitId = $scope.orgUnitId;
						 $scope.tSubSpecimanMaster.createdBy = $scope.userId;
						 $scope.tSubSpecimanMaster.listTGrossMaster = [];
									$state.go('reportCreationRelease',
										{
										  backFlag : 2,
										  subSpecimenObj : $scope.tSubSpecimanMaster,
										  searchObj : searchObj
										});
						
					}
				});
			 
		 }
	
	 }
	 
	 $scope.amendObj = {};
	 $scope.reCall =  function(obj){
		 	$scope.amendObj = obj;
			$('#amend').modal('show');
		 
	 }
	 
	 
	 $scope.getBiochemResultEntryDetails = function() 
		{
			
			try {
					$rootScope.startLoader();
					$scope.labResultDetailsMasterDtoDtlsList =[];
					var dataList = [];
					$scope.testMasterResList = [];
					var resultEntry = $scope.testObj;
					$scope.testType= resultEntry.testType;
					//$scope.patientId  = resultEntry.patientId;
					$scope.labSampleDtlsId =resultEntry.labSampleDtlsId;
					
					var data = {
							"orgId": $scope.orgId,
							"orgUnitId":$scope.orgUnitId,
							"testId":resultEntry.testId,
							"testType":resultEntry.testType,
							"deptId":resultEntry.deptId,
							"subDeptId":resultEntry.subDeptId,
							"patientAgeDays":resultEntry.patientAgeDays,
							"labSampleNo":resultEntry.sampleNo,
							"genderId":resultEntry.genderId,
							"assingnedFrom":$scope.orgUnitId,
							"labSampleDtlsId":resultEntry.labSampleDtlsId
							
						};
						if(resultEntry.labTestResId != undefined && resultEntry.labTestResId != null){
							data.labResultId = resultEntry.labTestResId;
							$scope.labTestResId = resultEntry.labTestResId;
						}else{
							$scope.labTestResId = null;
							data.labResultId = null;
						}
						dataList.push(data);
			
						if( $scope.labTestResId != undefined && $scope.labTestResId != null){
							var URI = BASE_URL + ROOT_URL
							+ LIS_BIOCHEMISTRY
							+ GET_WORK_LIST_VALIDATION_ENTERY_DETAILS;
						}else{
							var URI = BASE_URL + ROOT_URL
							+ LIS_BIOCHEMISTRY
							+ GET_WORK_LIST_DEPT_RESULT_ENTERY_DETAILS;
						}
				

				GenericService .serviceAction(METHOD_POST, URI, dataList)
						.then(
								function(response) {
									$rootScope.stopLoader();
									if (response.data.status == 'success')
									{
										$rootScope.stopLoader();
										$scope.testMasterResList = [];
										$scope.testMasterResList = response.data.listObject;
										angular.forEach($scope.testMasterResList, function(value, key) 
										{
											value.isSelected = true;
										    value.isRecollected =false;
										    value.isRetestRecollected = false;
                                       
										    angular.forEach($scope.testMasterResList, function(value, key) {
											    if(value.labResultDetailsViewDto!=undefined && value.labResultDetailsViewDto!=null){
											    		if(value.labResultDetailsViewDto.listHelpValueMaster.length>0){
											    			angular.forEach(value.labResultDetailsViewDto.listHelpValueMaster,function(val2,indx2){
											    				
											    				if(val2.isDefault){
											    					value.labResultDetailsViewDto.finalResult = val2.helpValue;
											    					value.labResultDetailsViewDto.isAbnormal = val2.isAbnormal;
											    				}else{
											    					value.labResultDetailsViewDto.isAbnormal = val2.isAbnormal;
											    				}
											    			});
											    		}
											    }else if(value.listMultyParamTestHeaderDto!=undefined && value.listMultyParamTestHeaderDto!=null && value.listMultyParamTestHeaderDto.length>0){
									    			angular.forEach(value.listMultyParamTestHeaderDto,function(val2,indx2){
									    				if(val2.listLabResultDetailsViewDto!=undefined && val2.listLabResultDetailsViewDto!=null && val2.listLabResultDetailsViewDto.length>0){
									    					angular.forEach(val2.listLabResultDetailsViewDto,function(val3,indx3){
									    						if(val3.listHelpValueMaster!=undefined && val3.listHelpValueMaster!=null && val3.listHelpValueMaster.length>0){
									    						angular.forEach(val3.listHelpValueMaster,function(val4,indx4){
									    						if(val4.isDefault){
									    						    val3.finalResult = val4.helpValue;
									    						    val3.isAbnormal = val4.isAbnormal;
									    						}
									    					});
									    						}else{
									    							if(val3.firstLevelResult == null){
									    								 val3.finalResult = val3.firstLevelResult;
									    							}else if($scope.isOnHoldParameter == true) {
									    								 val3.finalResult = val3.firstLevelResult;
									    							}
									    						}
									    					});
									    				}

									    			});
									    }
										    });
										});
										
										$scope.getMachineResult(1);
										
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
				$rootScope.stopLoader();
				console.log(e.message)
			}

		}
	 
		$scope.showHelpValue = function(testIndex,headerIndex,resultIndex,testType,parameterId)
		{
			
			try 
			{
				$rootScope.startLoader();
				$scope.tIdx = testIndex;
				$scope.hIdx = headerIndex;
				$scope.rIdx = resultIndex;
				$scope.tType = testType;
				$scope.listHelpValueMaster = [];
				var URI = BASE_URL + ROOT_URL
				+ LIS_UNIT
				+ GET_HELP_VALUES +S + parameterId+S+S + $scope.orgId+S+$scope.orgUnitId;
				console.log("URI", URI);
					GenericService
					.serviceAction("GET", URI, {})
					.then(
							function(response) 
							{
							    if(response.data.status=="success")
							    {
							    	$scope.listHelpValueMaster = response.data.listObject;
							    	if($scope.listHelpValueMaster!=null)
							    		{
					                    	$('#helpValueSelect').modal('show'); 
							    		}
							    	else{
							    		  growl.warning("No help value found.",
													{
														title : 'Warning!'
													});
							    	   }
							    	$rootScope.stopLoader();
							    }
							    else if(response.data.status=="error")
							    	{
							    	   $scope.listHelpValueMaster =[];
							    	   $rootScope.stopLoader();
							    	}
								
							});
					
				}
			catch (e) {
				console.log(e.message)
			}
		}
		
		
		$scope.selectHelpValue = function(helpValue,isAbnormal)
		{
			if($scope.tType==0)
				{
				$scope.testMasterResList[$scope.tIdx].labResultDetailsViewDto.finalResult=helpValue; 
				$scope.testMasterResList[$scope.tIdx].labResultDetailsViewDto.isAbnormal=isAbnormal; 
				}
			else if($scope.tType==1)
				{
				$scope.testMasterResList[$scope.tIdx].listMultyParamTestHeaderDto[$scope.hIdx].listLabResultDetailsViewDto[$scope.rIdx].finalResult =helpValue;
				$scope.testMasterResList[$scope.tIdx].listMultyParamTestHeaderDto[$scope.hIdx].listLabResultDetailsViewDto[$scope.rIdx].isAbnormal =isAbnormal;
				}
			
			$('#helpValueSelect').modal('hide'); 
		}
		
		 $scope.machineList =[];
		   $scope.getMachineList = function() {
			   var data = {
			   "organisationId" : $scope.orgId,
			   "unitId" : $scope.orgUnitId,
			   }
			   var URI = BASE_URL + ROOT_URL + LIS_GLOBAL+ GETMACHINELIST;
			   GenericService.serviceAction("POST", URI, data).then(
					   function(response) {
						   $scope.machineList = [];
						   if (response.data.status == "success") {
							   $scope.machineList = response.data.listObject;
						   }
			   });

			    }
			   $scope.getMachineList();
			   
			   
			   $scope.selectMachine = function(indx,testRestObj) {
				   
				   if(testRestObj.machineId != null && testRestObj.machineId != ""){
					   
					   var data = {
							   "testId" : testRestObj.testId,
							   "machineId" : testRestObj.machineId,
							   "genderId" : $scope.testObj.genderId,
							   "patientAgeDays" :  $scope.testObj.patientAgeDays,
							   }
							   var URI = BASE_URL + ROOT_URL + "/LIS/BioChemistry/getParameterValueByMachineId";
							   GenericService.serviceAction("POST", URI, data).then(
									   function(response) {
										   if (response.data.status == "success") {
											   
											   if($scope.testMasterResList[indx].testType==0){
												   	if($scope.testMasterResList[indx].labResultDetailsViewDto!=null){

												   		if(response.data.listObject != null && response.data.listObject.length){
												   			if(response.data.listObject[0].parameterId == $scope.testMasterResList[indx].labResultDetailsViewDto.parameterId){
												   				if($scope.testMasterResList[indx].labResultDetailsViewDto.refrangeTypeId == 1 ){
																				
												   							$scope.testMasterResList[indx].labResultDetailsViewDto.parameterMin = response.data.listObject[0].parameterMin;
												   							$scope.testMasterResList[indx].labResultDetailsViewDto.parameterMax = response.data.listObject[0].parameterMax;
												   							$scope.testMasterResList[indx].labResultDetailsViewDto.paramAbnrmlMin = response.data.listObject[0].paramAbnrmlMin;
												   							$scope.testMasterResList[indx].labResultDetailsViewDto.paramAbnrmlMax = response.data.listObject[0].paramAbnrmlMax;
																			}else if($scope.testMasterResList[indx].labResultDetailsViewDto.refrangeTypeId == 2){
																				
																				$scope.testMasterResList[indx].labResultDetailsViewDto.textualRangeName = response.data.listObject[0].textualRangeName;
																			}else{
																				
																				$scope.testMasterResList[indx].labResultDetailsViewDto.multitextaulRange = response.data.listObject[0].multitextaulRange
																			}
												   				
												   							if(response.data.listObject[0].paramUnit!=null){
													   							$scope.testMasterResList[indx].labResultDetailsViewDto.paramUnit = response.data.listObject[0].paramUnit;
												   							}else{
													   							$scope.testMasterResList[indx].labResultDetailsViewDto.paramUnit = response.data.listObject[0].defaultParamUnit;
												   							}

																			$scope.changeArrowPosition(indx,0,0,$scope.testMasterResList[indx].testType);

												   			}
												   		}else{
												   			if($scope.testMasterResList[indx].labResultDetailsViewDto.refrangeTypeId == 1 ){
																		
												   							$scope.testMasterResList[indx].labResultDetailsViewDto.parameterMin = $scope.testMasterResList[indx].labResultDetailsViewDto.originalParameterMin;
												   							$scope.testMasterResList[indx].labResultDetailsViewDto.parameterMax = $scope.testMasterResList[indx].labResultDetailsViewDto.originalParameterMax;
												   							$scope.testMasterResList[indx].labResultDetailsViewDto.paramAbnrmlMin = $scope.testMasterResList[indx].labResultDetailsViewDto.originalParamAbnrmlMin;
												   							$scope.testMasterResList[indx].labResultDetailsViewDto.paramAbnrmlMax = $scope.testMasterResList[indx].labResultDetailsViewDto.originalParamAbnrmlMax; 
																		}else if($scope.testMasterResList[indx].labResultDetailsViewDto.refrangeTypeId == 2){
																			
																			$scope.testMasterResList[indx].labResultDetailsViewDto.textualRangeName = $scope.testMasterResList[indx].labResultDetailsViewDto.originalTextualRangeName;
                                                                            
																		}else{
																			
																			$scope.testMasterResList[indx].labResultDetailsViewDto.multitextaulRange = $scope.testMasterResList[indx].labResultDetailsViewDto.originalMultitextaulRange
																		}
												   			
												   						$scope.testMasterResList[indx].labResultDetailsViewDto.paramUnit = $scope.testMasterResList[indx].labResultDetailsViewDto.defaultParamUnit;
												 			   			$scope.changeArrowPosition(indx,0,0,$scope.testMasterResList[indx].testType);
												   		}
												   	}
												   }else {
													   angular.forEach($scope.testMasterResList[indx].listMultyParamTestHeaderDto,function(value,index) {
				        		                           
													 		  angular.forEach(value.listLabResultDetailsViewDto,function(value1,index1) {
																	var count = 0;
													 			  angular.forEach(response.data.listObject,function(value2,index2) {
																		
																			if(value1.parameterId == value2.parameterId){
																				count = 1;
																				if(value1.refrangeTypeId == 1 ){
																					
                                                                                   value1.parameterMin = value2.parameterMin;
                                                                                   value1.parameterMax = value2.parameterMax;
                                                                                   
                                                                                   value1.paramAbnrmlMin = value2.paramAbnrmlMin;
                                                                                   value1.paramAbnrmlMax = value2.paramAbnrmlMax;
																				}else if(value1.refrangeTypeId == 2){
																					
                                                                                    value1.textualRangeName = value2.textualRangeName;
                                                                                    
																				}else{
																					
                                                                                    value1.multitextaulRange = value2.multitextaulRange
																				}
																				
																				if(value2.paramUnit!=null){
																					value1.paramUnit = value2.paramUnit;
														   						}else{
														   							value1.paramUnit = value2.defaultParamUnit;
														   						}
																				$scope.changeArrowPosition(indx,index,index1,$scope.testMasterResList[indx].testType);
																			}
																		});
													 			  		
													 			   	   if(count == 0){
													 			   		   
													 			   		if(value1.refrangeTypeId == 1 ){
																			
                                                                               value1.parameterMin = value1.originalParameterMin;
                                                                               value1.parameterMax = value1.originalParameterMax;
                                                                               
                                                                               value1.paramAbnrmlMin = value1.originalParamAbnrmlMin;
                                                                               value1.paramAbnrmlMax = value1.originalParamAbnrmlMax;
																			}else if(value1.refrangeTypeId == 2){
																				
                                                                                value1.textualRangeName = value1.originalTextualRangeName;
                                                                                
																			}else{
																				
                                                                                value1.multitextaulRange = value1.originalMultitextaulRange
																			}
													 			   			value1.paramUnit = value1.defaultParamUnit;
													 			   			$scope.changeArrowPosition(indx,index,index1,$scope.testMasterResList[indx].testType);
													 			   	   }
													 			   	 
																	});
													 		  
													 		  		
					                                            });
												   }
											 	
										   }
							   });
				   }

				  }
		
		
		$scope.getMachineResult = function(onload) 
		{  
			try {
				
				 var mapObj = {};
				angular.forEach($scope.testMasterResList, function(value,key)
						{
					      mapObj[value.labSampleNo] = value.testId;
						});
				$rootScope.startLoader();
				var URI = BASE_URL + ROOT_URL
						+ LIS_BIOCHEMISTRY
						+ GET_LAB_MACHINE_RESULT;
				GenericService
						.serviceAction(METHOD_POST, URI,mapObj)
						.then(
								function(response) {
									if(response.data.status == 'success')
										{
										   if(response.data.listObject!=null)
											   {
											       var machineResList = response.data.listObject;
											       
											       if(machineResList.length == 0 && (onload == undefined || onload == null)){
											    	   growl.warning("Data not available..",
																{
																	title : 'warning!'
																});
													   $rootScope.stopLoader();
													   return false;
											       }
											       
											        function compareResult(parameterId,labSampleDtlsId)
											       {
											    	   var obj = machineResList.find(function(value, index) {
											    		   if(value.parameterId==parameterId && value.labSampleDtlsId==labSampleDtlsId)
											    		      {
											    		        return value.paramResult;
											    		      }
											    		 });
											       }
											       angular.forEach($scope.testMasterResList, function(testObj, key)
															{
											    	              var res = "";
															      if(testObj.testType==0)
															    	  {
															    	  var obj = machineResList.find(function(value, index) {
															    		   if(value.parameterId==testObj.labResultDetailsViewDto.parameterId &&  value.labSampleDtlsId==testObj.labSampleDtlsId)
															    		      {
															    			   testObj.labResultDetailsViewDto.finalResult= value.paramResult;
															    			   $scope.changeArrowPosition(key,0,0,testObj.testType);
															    		      }
															    		 });
															    	  }
															       else if(testObj.testType==1)
															    	  {
															    	  angular.forEach(testObj.listMultyParamTestHeaderDto, function(headerObj, headerkey)
																				{
																			       angular.forEach(headerObj.listLabResultDetailsViewDto, function(resultObj, resultKey)
																					{
																			    	   var obj = machineResList.find(function(value, index) {
																			    		   if(value.parameterId==resultObj.parameterId &&  value.labSampleDtlsId==testObj.labSampleDtlsId)
																			    		      {
																			    		       if(resultObj.refrangeTypeId !=undefined && resultObj.refrangeTypeId == 1 && resultObj.paramPrecision == 0){
																			    		    	     var toStringResult = Math.round(value.paramResult)
																			    		             resultObj.finalResult = toStringResult.toString();
																			    		       }else{
																			    		       	    resultObj.finalResult =value.paramResult;
																			    		       }
																			    			   $scope.changeArrowPosition(key,headerkey,resultKey,testObj.testType);
																			    		      }
																			    		 });
																					});
																				});
															    	  }
															        
															});
											       $rootScope.stopLoader();
											   }
										   else{
											   growl.error("No Result Found..",
														{
															title : 'Error!'
														});
											   $rootScope.stopLoader();
										      }
										   
										}
									else if(response.data.status == 'error')
										{
										  $rootScope.stopLoader();
										}
								});
			} catch (e) {
				console.log(e.message)
			}
		}
		
		
		
		$scope.changeArrowPosition  = function(testIndex,headerIndex,resultIndex,testType,isEditable)
		 {
			if(testType==0)
				{
			
				if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult  == undefined || $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult  == null || $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult  == "") 
				{
				 
				 if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.refrangeTypeId ==REFERENCE_VALUE){
					 $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.arrowClass  = '';
					 $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.isAbnormal  = 'N';
				 }
         	  
         }
			
			
			if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==REFERENCE_VALUE)
				{
				
				 if( $scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMax !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult > $scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMax) {
               
                $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = 'fa fa-arrow-up color-red';
                  $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'Y';
                 
            }else if($scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMin !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult < $scope.testMasterResList[testIndex].labResultDetailsViewDto.parameterMin){
               
                $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = 'fa fa-arrow-down color-red';
                  $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'Y';
                 
            }else{
               
                $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = '';
                  $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'N';
            }
           
	
				}
			else if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==TEXTUAL_RANGE)
				{
				   var finalResult = $scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult.trim().toLowerCase();
				   var textualRangeName = $scope.testMasterResList[testIndex].labResultDetailsViewDto.textualRangeName.trim().toLowerCase();
				   console.log("finalResult.localeCompare(textualRangeName)",finalResult.localeCompare(textualRangeName));
				   if(finalResult===textualRangeName)
				   {
					   $scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = '';
					   $scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'N'; 
				   }
				   else
				   {
				   }
				   var isDefaultHelpSelected = false;
				   if($scope.testMasterResList[testIndex].labResultDetailsViewDto.listHelpValueMaster!=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.listHelpValueMaster.length>0){
					   angular.forEach($scope.testMasterResList[testIndex].labResultDetailsViewDto.listHelpValueMaster,function(val,idx){
						  if(val.isDefault){
							  if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult == val.helpValue){
								  isDefaultHelpSelected = true;
							  }
						  } 
					   });
				   }
				
				   $rootScope.stopLoader();
				}
			else if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult !=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==MULTIVALUED_RANGE)
				{
				$scope.testMasterResList[testIndex].labResultDetailsViewDto.arrowClass  = '';
				$scope.testMasterResList[testIndex].labResultDetailsViewDto.isAbnormal  = 'N';
				
				
				  var isDefaultHelpSelected = false;
				   if($scope.testMasterResList[testIndex].labResultDetailsViewDto.listHelpValueMaster!=null && $scope.testMasterResList[testIndex].labResultDetailsViewDto.listHelpValueMaster.length>0){
					   angular.forEach($scope.testMasterResList[testIndex].labResultDetailsViewDto.listHelpValueMaster,function(val,idx){
						  if(val.isDefault){
							  if($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult == val.helpValue){
								  isDefaultHelpSelected = true;
							  }
						  } 
					   });
				   }
			
				}
			
			
			   if($scope.testMasterResList[testIndex].previousTestResultList.length>0)
				   {
				   
				   
				   var deltaChange = $scope.calculateDeltaPercentage($scope.testMasterResList[testIndex].labResultDetailsViewDto.finalResult,
						   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].labResultDetailsViewDto.finalResult,
						   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].labResultDetailsViewDto.refrangeTypeId,
						   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].labResultDetailsViewDto.deltaDaysId,
						   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].resultAuthorisedDatetime
						   
						   
				   );
				   if((deltaChange>$scope.testMasterResList[testIndex].labResultDetailsViewDto.deltaPer)||(deltaChange==-1))
					   {
					   $scope.testMasterResList[testIndex].labResultDetailsViewDto.isDeltaFlag = 'Y';
					   if($scope.testMasterResList[testIndex].labResultDetailsViewDto.refrangeTypeId ==REFERENCE_VALUE)
						   {
						    $scope.testMasterResList[testIndex].labResultDetailsViewDto.deltaChange = deltaChange;
						   }
					   
					   }
				   else{
					   $scope.testMasterResList[testIndex].labResultDetailsViewDto.isDeltaFlag = 'N';
					   $scope.testMasterResList[testIndex].labResultDetailsViewDto.deltaChange = 0;
				   }
				     
				   }
			
			}
		else if(testType==1)
			{ 
			
				if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == undefined || $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == null || $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == "") 
                 {
						 
						 if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==REFERENCE_VALUE){
							 $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
                               $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'N';
						 }
                 	  
                 }
			
			
			if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult !=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==REFERENCE_VALUE)
			{	

            if( $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMax !=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult > $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMax) {
               
                $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = 'fa fa-arrow-up color-red';
                  $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'Y';
                 
            }else if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMin !=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult < $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].parameterMin){
               
                $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = 'fa fa-arrow-down color-red';
                  $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'Y';
                 
            }else{
               
                $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
                  $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'N';
                 
            }
				
			}
			else if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult !=null 
					&& $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==TEXTUAL_RANGE)
			{
				 var finalResult = $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult.trim().toLowerCase();
				 var textualRangeName = $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].textualRangeName.trim().toLowerCase();
			   if(finalResult===textualRangeName)
				   {
				   $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
				   $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isAbnormal  = 'N';
				   }
			   else{
			   }
			   var isDefaultSelected =false;
			   if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].listHelpValueMaster!=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].listHelpValueMaster.length>0){
				   angular.forEach($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].listHelpValueMaster,function(value,idx){
					   if(value.isDefault){
						   if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == value.helpValue){
							   isDefaultSelected = true;
						   }
					   }
				   });
			   }
			
			   $rootScope.stopLoader();
			}
		   else if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult !=null 
				   && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId ==MULTIVALUED_RANGE)
			{
			
			   
			   $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].arrowClass  = '';
			   var isDefaultSelected =false;
			   if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].listHelpValueMaster!=null && $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].listHelpValueMaster.length>0){
				   angular.forEach($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].listHelpValueMaster,function(value,idx){
					   if(value.isDefault){
						   if($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult == value.helpValue){
							   isDefaultSelected = true;
						   }
					   }
				   });
			   }
			
			}
			if($scope.testMasterResList[testIndex].previousTestResultList!=null && $scope.testMasterResList[testIndex].previousTestResultList.length>0)
			   {
				
			   var deltaChange =  $scope.calculateDeltaPercentage(
					   $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult,
					   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].finalResult,
					   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].refrangeTypeId,
					   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].deltaDaysId,
					   $scope.testMasterResList[testIndex].previousTestResultList[testIndex].resultAuthorisedDatetime);
			   
			   if((deltaChange> $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].deltaPer)||(deltaChange==-1))
				   {
				     $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isDeltaFlag = 'Y';
				   }
			   else
			   {
				   $scope.testMasterResList[testIndex].listMultyParamTestHeaderDto[headerIndex].listLabResultDetailsViewDto[resultIndex].isDeltaFlag = 'N';
			    }
			   }
			
			if( $scope.override == false){
				if(isEditable == null || isEditable == 'N'){
					
					angular.forEach($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto, function(headerObj, key) 
							{
							      angular.forEach(headerObj.listLabResultDetailsViewDto, function(labResObj, key) 
									{
							    	   if(labResObj.isFormula=='Y')
							    		   {
							    		   var formula  = labResObj.parameterFormula;
							    		   console.log('labResObj',labResObj);
							    		   console.log('formula',formula);
										   var simplified = math.simplify(math.parse(formula));
										   var formulaDetailsList = labResObj.listFormulaDetails;
										    var obj = {};
										    var testList = [];
										    var checkParameter = false;
											  angular.forEach(formulaDetailsList, function(value, key)
											   {
												  
												  angular.forEach($scope.testMasterResList[testIndex].listMultyParamTestHeaderDto, function(paramInHeaderObj, key)
														    {
														        angular.forEach(paramInHeaderObj.listLabResultDetailsViewDto, function(paramObj, key)
														          {
																		//   if(paramObj.parameterId == value.parameterId &&paramObj.refrangeTypeId==REFERENCE_VALUE)
														        	if(paramObj.parameterId == value.parameterId)
																		   {
																			   if(paramObj.finalResult==null||paramObj.finalResult==""||paramObj==undefined)
																			   {
																				   paramObj.finalResult=null;
																				   checkParameter = true;
																			   }
																			   obj[value.paramVariable.toString()] = paramObj.finalResult==null?0.00:parseFloat(paramObj.finalResult);
																		   }
															    });  
															});
												  
											   });
											  var result = simplified.eval(obj);
											  if(result == 0.00 || isNaN(result)){
												  if(labResObj.finalResult == undefined ||labResObj.finalResult == null || labResObj.finalResult == ""){
													 if(checkParameter == true){
														 labResObj.finalResult==null;
													 }else{
														 labResObj.finalResult=0.00;
													 }
													 
												  }
												  else
													  {
													  labResObj.finalResult=0.00;
													  }
												 
											  } else {
													  labResObj.finalResult =result.toFixed(labResObj.paramPrecision);
												  
											  }
											  
												 if(labResObj.finalResult != undefined && labResObj.finalResult != null && labResObj.finalResult != "" ){
													 	
														 if(labResObj.parameterMax != null && labResObj.finalResult > labResObj.parameterMax) {
                                                              labResObj.arrowClass  = 'fa fa-arrow-up color-red';
											                  labResObj.isAbnormal  = 'Y';
														}else if(labResObj.parameterMin != null && labResObj.finalResult < labResObj.parameterMin){
                                                              labResObj.arrowClass  = 'fa fa-arrow-down color-red';
											                  labResObj.isAbnormal  = 'Y';
														}else{
                                                              labResObj.arrowClass  = '';
											                  labResObj.isAbnormal  = 'N';
														}
													 }
											  
							    		   }
								  
								    });
							});
					}
				}
			
			 };
			}
		
		
		$scope.labResultMasterDtoList = [];
		
		$scope.createLabResultObjectList = function(sampleStatusId)
		{
			angular.forEach($scope.testMasterResList, function(value, key) {
				
				if(value.isSelected==true)
					{
					 $scope.labResultMasterDto = {};
					
					
					if(value.testType==0)
					{
						$scope.labResultMasterDto.deptId =$scope.testObj.deptId;
						$scope.labResultMasterDto.genderId=$scope.testObj.genderId;
						$scope.labResultMasterDto.orderDetailsId=$scope.testObj.orderDetailsId;
						$scope.labResultMasterDto.patientId=$scope.testObj.patientId;
						$scope.labResultMasterDto.priorityId=1;
						$scope.labResultMasterDto.subDeptId=$scope.testObj.subDeptId;
						$scope.labResultMasterDto.testId=$scope.testObj.testId;
						$scope.labResultMasterDto.visitTypeId=$scope.testObj.visitTypeId;
						$scope.labResultMasterDto.patientAgeDays = $scope.testObj.patientAgeDays; 
						$scope.labResultMasterDto.paymentEntitlementId = $scope.testObj.paymentEntitlementId;
						
						  $scope.listLabResultEntryDtls = [];
						  $scope.labResultMasterDto.createdBy =$scope.userId;
						  $scope.labResultMasterDto.updatedBy =$scope.userId;
			    	      $scope.labResultMasterDto.orgId= $scope.orgId;
			    	      $scope.labResultMasterDto.orgUnitId=$scope.orgUnitId;
			    	      $scope.labResultMasterDto.testDesc = value.testDesc;
			    	      $scope.labResultMasterDto.footsNotes=value.footsNotes;
			    	      $scope.labResultMasterDto.internalNotes=value.internalNotes;
			    	      $scope.labResultMasterDto.srfId=value.srfId;
			    	      $scope.labResultMasterDto.icmrId=value.icmrId;
			    	      $scope.labResultMasterDto.isReportRecall=false;
			    	      value.labResultDetailsViewDto.orgId = $scope.orgId;
			    	      value.labResultDetailsViewDto.orgUnitId  =$scope.orgUnitId;
			    	      value.labResultDetailsViewDto.listHelpValueMaster = [];
			    	      value.labResultDetailsViewDto.listParamRefrengMaster = [];
			    	      if( value.labResultDetailsViewDto.isAbnormal == undefined ||  value.labResultDetailsViewDto.isAbnormal== null){
			    	      	  value.labResultDetailsViewDto.isAbnormal = "N";
			    	      }
			    	    //to append precision to int val 
					      if(value.labResultDetailsViewDto!=undefined && value.labResultDetailsViewDto!=null  &&  value.labResultDetailsViewDto.refrangeTypeId !=null && value.labResultDetailsViewDto.refrangeTypeId == 1){
					      		 if(! value.labResultDetailsViewDto.finalResult.isNaN &&  value.labResultDetailsViewDto.finalResult!=null && value.labResultDetailsViewDto.finalResult!=undefined && value.labResultDetailsViewDto.finalResult!="-"){
					      		     var arr =  value.labResultDetailsViewDto.finalResult.split('.');
					      		     if(arr[1]==undefined &&  value.labResultDetailsViewDto.paramPrecision > 0){
					      		     	 value.labResultDetailsViewDto.finalResult =  parseInt(value.labResultDetailsViewDto.finalResult).toFixed( value.labResultDetailsViewDto.paramPrecision); 
					      		     }	
					      		 }
					      }
			    	      
			    	      $scope.listLabResultEntryDtls.push(value.labResultDetailsViewDto);
					      $scope.labResultMasterDto.footsNotes = value.footsNotes;
			    	      $scope.labResultMasterDto.internalNotes=value.internalNotes;
					      $scope.labResultMasterDto.listLabSampleResultDetailsMaster = $scope.listLabResultEntryDtls;
					      $scope.labResultMasterDto.resultLevel = RESULT_ENTRY;
					      $scope.labResultMasterDto.sampleStatusId = sampleStatusId;
					      $scope.labResultMasterDto.remark = (value.remarks == undefined || value.remarks == null )? " " : value.remarks ;
					  
					      $scope.labResultMasterDto.insuranceId =  value.insuranceId;
					      $scope.labResultMasterDto.companyId =  value.companyId;
					      $scope.labResultMasterDto.machineId = value.machineId;
					      $scope.labResultMasterDto.amend =   true;
					      $scope.labResultMasterDto.labTestResId = null;
                      
                       $scope.labResultMasterDto.labSampleDtlsId = value.labSampleDtlsId;
					      $scope.labResultMasterDtoList.push($scope.labResultMasterDto);
					}
					else if(value.testType==1)
					{
						$scope.listLabResultEntryDtls  = [];
						$scope.labResultMasterDto.deptId =$scope.testObj.deptId;
						$scope.labResultMasterDto.genderId=$scope.testObj.genderId;
						$scope.labResultMasterDto.orderDetailsId=$scope.testObj.orderDetailsId;
						$scope.labResultMasterDto.patientId=$scope.testObj.patientId;
						$scope.labResultMasterDto.priorityId=1;
						$scope.labResultMasterDto.subDeptId=$scope.testObj.subDeptId;
						$scope.labResultMasterDto.testId=$scope.testObj.testId;
						$scope.labResultMasterDto.visitTypeId=$scope.testObj.visitTypeId;
						$scope.labResultMasterDto.patientAgeDays = $scope.testObj.patientAgeDays; 
						$scope.labResultMasterDto.paymentEntitlementId = $scope.testObj.paymentEntitlementId;
						$scope.labResultMasterDto.createdBy=$scope.userId;
						$scope.labResultMasterDto.updatedBy =$scope.userId;
	    	            $scope.labResultMasterDto.orgId= $scope.orgId;
	    	            $scope.labResultMasterDto.orgUnitId=$scope.orgUnitId;
	    	            $scope.labResultMasterDto.amend =   true;
					    $scope.labResultMasterDto.labTestResId = null;
	    	            $scope.labResultMasterDto.testDesc = value.testDesc;
	    	            
	    	            $scope.labResultMasterDto.footsNotes=value.footsNotes;
			    	      $scope.labResultMasterDto.internalNotes=value.internalNotes;
			    	      $scope.labResultMasterDto.srfId=value.srfId;
			    	      $scope.labResultMasterDto.icmrId=value.icmrId;
						angular.forEach(value.listMultyParamTestHeaderDto, function(headerParamObj, key) 
								{
							
							angular.forEach(headerParamObj.listLabResultDetailsViewDto, function(labResultDetails, key) 
									{
								
								      labResultDetails.orgId = $scope.orgId;
								      labResultDetails.orgUnitId =$scope.orgUnitId;
								      labResultDetails.listHelpValueMaster = [];
								      labResultDetails.listParamRefrengMaster = [];
								      if(labResultDetails.isAbnormal== null || labResultDetails.isAbnormal == undefined){
								    	  labResultDetails.isAbnormal = 'N';
								      }
								      
							      		 if(labResultDetails.finalResult!=undefined && labResultDetails.finalResult!=null 
							      				 && ! labResultDetails.finalResult.isNaN && labResultDetails.finalResult!='-'  
							      					 &&  labResultDetails.refrangeTypeId !=null && labResultDetails.refrangeTypeId== 1){
							      			if(labResultDetails.finalResult==0)
						      				 {
						      				labResultDetails.finalResult="0";
						      				 }
							      		     var arr =  labResultDetails.finalResult.split('.');
							      		     if(arr[1]==undefined &&  labResultDetails.paramPrecision > 0 ){
							      		    	labResultDetails.finalResult =  parseInt(labResultDetails.finalResult).toFixed( labResultDetails.paramPrecision); 
							      		     }else  if(arr[1]!=undefined &&  labResultDetails.paramPrecision > 0 ){
							      		    	labResultDetails.finalResult = parseFloat(labResultDetails.finalResult).toFixed(labResultDetails.paramPrecision);
							      		     }	
							      		 }
								      $scope.listLabResultEntryDtls.push(labResultDetails);
								  });
							});
						  $scope.labResultMasterDto.listLabSampleResultDetailsMaster = $scope.listLabResultEntryDtls;
						  $scope.labResultMasterDto.footsNotes = value.footsNotes;
			    	      $scope.labResultMasterDto.internalNotes=value.internalNotes;
			    	      $scope.labResultMasterDto.srfId=value.srfId;
			    	      $scope.labResultMasterDto.icmrId=value.icmrId;
						  $scope.labResultMasterDto.resultLevel = RESULT_ENTRY;
						  $scope.labResultMasterDto.labSampleDtlsId = value.labSampleDtlsId;
						  $scope.labResultMasterDto.sampleStatusId = sampleStatusId;
						  $scope.labResultMasterDto.remark = (value.remarks == undefined || value.remarks == null )? "" :value.remarks ;
						  $scope.labResultMasterDto.insuranceId =  value.insuranceId;
					      $scope.labResultMasterDto.companyId =  value.companyId;
					      $scope.labResultMasterDto.machineId = value.machineId;
						  $scope.labResultMasterDtoList.push($scope.labResultMasterDto);
					 }
					}
				});
		}
		
		
		$scope.saveReportEntry= function(sampleStatusId) 
		{
			$scope.$broadcast('show-errors-check-validity');
			if ($scope.worklistForReportEntryForm.$valid)
			{
				try 
				{
					$rootScope.startLoader();
					$scope.createLabResultObjectList(sampleStatusId);
                 if($scope.labResultMasterDtoList.length == 0){
                     $rootScope.stopLoader();
                     growl.warning("Please Select Test",
                                 {
                                     title : 'warning'
                                 });
                                 return false;
                 }
              
					var URI = BASE_URL + ROOT_URL
                                + LIS_BIOCHEMISTRY
                                + WORK_RESULT_ENTERY_DETAILS; 
					
					GenericService.serviceAction("POST", URI, $scope.labResultMasterDtoList).then(function(response) 
					{	
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							
								$scope.labResultMasterList = [];
								$scope.labResultMasterDtoList = [];
								$scope.testMasterResList = [];
								$scope.searchOutPatientList();
							
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
					$rootScope.stopLoader();
					console.log("Exception",e.message);
				}
				
			}
			else
			{
				/*growl.error("Please Fill All Mandatory Fields.",
						{
							title : 'Error!'
						});*/
			}
		}
		
	 
	 
	 
	 /** Micro_Lab priority List*/
		$scope.getMicroLabPriorityMaster = function()
		{
			var data = "";
			try{
				var URI = BASE_URL + ROOT_URL
			            	+ LIS_COMMON
			               	+ GET_MICRO_LAB_PRIORITY_LIST+S+$scope.orgId;
				console.log("URI", URI);

				 GenericService
						.serviceAction(METHOD_GET, URI, data)
						.then(
								function(response) 
								{
									$scope.microLabPriorityList = response.data.listObject;
									console.log("$scope.microLabPriorityList",$scope.anitibioticList);
									 response.data.listObject;
								});
			} catch (e) {
				console.log(e.message)
			}
		}
		
		$scope.getMicroLabPriorityMaster();
		
		/** Micro_organism Group List*/
		$scope.getAntibioticsByOrganism = function(orgId,organismId)
		{
			var data = "";
			try{
				var URI = BASE_URL + ROOT_URL
			            	+ LIS_COMMON
			               	+ GET_ANTIBIOTICS_BY_ORGAINSM+S+orgId+S+'organismId'+S+organismId;
				console.log("URI", URI);

				return GenericService
						.serviceAction(METHOD_GET, URI, data)
						.then(
								function(response) 
								{
									$scope.anitibioticList = response.data.listObject;
									console.log("$scope.anitibioticList",$scope.anitibioticList);
									return response.data.listObject;
								});
			} catch (e) {
				console.log(e.message)
			}
		}
	 
		/** Micro_organism Group List*/
		$scope.getMicroOrganismGroupList = function(orgId)
		{
			var data = "";
			try{
				var URI = BASE_URL + ROOT_URL
			            	+ LIS_COMMON
			               	+ MICRO_ORGANISM_GROUP_LIST_BY_ORG_ID+S+$scope.orgId;
				console.log("URI", URI);

				GenericService
						.serviceAction(METHOD_GET, URI, data)
						.then(
								function(response) {
									$scope.microOrganismGroupList = response.data.listObject;
									console.log("$scope.microOrganismGroupList",$scope.microOrganismGroupList);
								});
			} catch (e) {
				console.log(e.message)
			}
		}
		$scope.getMicroOrganismGroupList($scope.orgId);
	 
	 
		$scope.getMicroOrganismList = function(organismGroupId)
		{
			var data = "";
			try{
				var URI = BASE_URL + ROOT_URL
			            	+ LIS_COMMON
			               	+ MICRO_GET_ORGANISM_BY_GROUP+S+organismGroupId+S+'orgId'+S+$scope.orgId;
				console.log("URI", URI);

				GenericService
						.serviceAction(METHOD_GET, URI, data)
						.then(
								function(response) 
								{
									$scope.organismList = response.data.listObject;
									console.log("$scope.organismList",$scope.organismList);
								});
			} catch (e) {
				console.log(e.message)
			}
		}
		
		
		
		  $scope.addMicroOrganismGroup = function()
	      {
	    	 
	    	 var isExists;
	    	 var data = {
						"organismGroupId":"",
						"organismId":""
					};
	    	 
	    	 
	    	 data.organismGroupId = $scope.organismGroupId;
	    	 data.organismId = $scope.organismId;
	    	 
				function checkExistsOrNot(arr, val) {
					return arr
							.some(function(arr) {
								return (val.organismGroupId == arr.organismGroupId)
										&& (val.organismId == arr.organismId);
							});
				}
	    	
			
				isExists = checkExistsOrNot(
						$scope.listSensitivityTestResultDetailsMasterDto,
						data);
				
				if(isExists)
					{
						growl.warning("Organism Group And Organism Already Exists",
							{
								title : 'warning!'
							});
					
				       // alert("Organism Group And Organism Already Exists");	
					}
				else{
					
					 var SensitivityTestResultDetailsMasterDto = angular.copy($scope.SensitivityTestResultDetailsMasterDto);
			    	 $scope.miroOrgGroupList = $scope.microOrganismGroupList
						.filter(function(microOrgGroupObj) {
							if (microOrgGroupObj.id ==  $scope.organismGroupId) 
							{
								SensitivityTestResultDetailsMasterDto.organismGroupName = microOrgGroupObj.name;
								SensitivityTestResultDetailsMasterDto.organismGroupId = microOrgGroupObj.id;
							}
						});
			    	 $scope.orgList = $scope.organismList
						.filter(function(microOrgObj) {
							if (microOrgObj.id ==  $scope.organismId) 
							{
								SensitivityTestResultDetailsMasterDto.organismName = microOrgObj.name;
								SensitivityTestResultDetailsMasterDto.organismId = microOrgObj.id;
							}
						});
			    	 
			    	 var promise = $scope.getAntibioticsByOrganism($scope.orgId,$scope.organismId);
			    	 promise.then(function(commonList)
								{
									if (commonList != null)
										{
										$scope.listSensitivityTestResultSubDetailsMasterDto =[];
										angular
										.forEach(
												commonList,
												function(value, key) 
												{
													 var SensitivityTestResultSubDetailsMasterDto = angular
																.copy($scope.SensitivityTestResultSubDetailsMasterDto);
													 SensitivityTestResultSubDetailsMasterDto.antibioticId = value.id;
													 SensitivityTestResultSubDetailsMasterDto.antibioticName =value.name;
													 $scope.listSensitivityTestResultSubDetailsMasterDto.push(SensitivityTestResultSubDetailsMasterDto);
												});
										}
									
									 SensitivityTestResultDetailsMasterDto.listSensitivityTestResultSubDetailsMaster = $scope.listSensitivityTestResultSubDetailsMasterDto;
									 $scope.listSensitivityTestResultDetailsMasterDto.push(SensitivityTestResultDetailsMasterDto);
								}, function(commonList)
								{
									console.log("ERROR FETCHING SUPER Antibiotics", commonList);
								});
			    	 
			    	 
			    	 console.log("$scope.listSensitivityTestResultDetailsMasterDto",$scope.listSensitivityTestResultDetailsMasterDto);

					
				    }
				 $scope.organismId = "";
		    	 $scope.organismGroupId = "";
	    	 
	 }
	      
		  $scope.getAntibioticAutocompleteList = function(searchKeyword) {
				try {
					var data = "";
					var URI = BASE_URL + ROOT_URL
	            	+ LIS_COMMON
	               	+ GET_ANTIBIOTICS_AUTOCOMPLETE+S+$scope.orgId+S+'antibiotic'+S+searchKeyword;
				 	console.log("URI", URI);
				 	return GenericService.serviceAction("GET", URI,
							data).then(function(response) {
						return response.data.listObject;
					});
				} catch (e) {
					console.log(e.message)
				}
			}
		  
		  
		  $scope.selectAnitbiotic = function($item, $model,
					$label) 
					{
				   $scope.antibioticId = $item.id;
				   $scope.antibioticName = $item.name;
				
				}
			
		  $scope.addAntibiotic = function(index)
			{
				var isExists;
				function checkExistsOrNot(arr, val) {
					return arr
							.some(function(arr) {
								return (arr.antibioticId == val);
							});
				}
				
				isExists = checkExistsOrNot(
						$scope.listSensitivityTestResultDetailsMasterDto[index].listSensitivityTestResultSubDetailsMaster,$scope.antibioticId);	
			
				if(isExists)
					{
					
					growl.warning("Antibiotics is already exists.",
							{
								title : 'warning!'
							});
					// alert("Antibiotics is already exists.");
					 $scope.antibiotics = undefined;
					}
				else
				{
					if( $scope.antibioticId == undefined ||  $scope.antibioticId == null ||  $scope.antibioticId== ""){
						growl.warning("Please Select Antibiotic",
							{
								title : 'warning!'
							});
							return false;
					}
					var SensitivityTestResultSubDetailsMasterDto = angular.copy($scope.SensitivityTestResultSubDetailsMasterDto);
				     SensitivityTestResultSubDetailsMasterDto.antibioticId = $scope.antibioticId;
				     SensitivityTestResultSubDetailsMasterDto.antibioticName = $scope.antibioticName;
				     $scope.listSensitivityTestResultDetailsMasterDto[index].listSensitivityTestResultSubDetailsMaster
							.push(SensitivityTestResultSubDetailsMasterDto);	
				}
				$scope.antibiotics = undefined;
				this.antibiotics = undefined;
			}
			
			$scope.removeAntibiotics = function(parentIndex,childIndex)
			{
				$scope.listSensitivityTestResultDetailsMasterDto[parentIndex].listSensitivityTestResultSubDetailsMaster.splice(childIndex, 1);
			}
			
			
			$scope.getSensitivityCheckTestResultDetails = function(labSampleDtlsId) {
				try {
					
					$rootScope.startLoader();
					 $scope.listSensitivityTestResultDetailsMasterDto = [];
					 $scope.labSampleDtlsId = labSampleDtlsId;
					var data = {};
					var URI = BASE_URL + ROOT_URL
				            	+ LIS_MICROBIOLOGY
				               	+ GET_SENSITIVITY_CHECK_TEST_DETAILS+S+labSampleDtlsId+S+$scope.orgId+S+$scope.orgUnitId;
					console.log("URI", URI);
					GenericService
							.serviceAction(METHOD_GET, URI, data)
							.then(
									function(response)
									{
										if(response.data.status=='success')
											{
											  $rootScope.stopLoader();
											  
											  if(response.data.object==null){
											  	 	$scope.flagCheck = false;
											  	 	
											  	 	$scope.SensitivityTestResultMasterDto = 
													{
															  "sensitivityResultId" : "",
															  "orgId" : $scope.orgId ,
															  "orgUnitId" : $scope.orgUnitId,
															  "createdBy" : $scope.userId,
															  "createdDate" : "",
															  "updatedBy" : "",
															  "updatedDate" : "",
															  "isDeleted" : 'N',
															  "checkTest" : "Y",
															  "labSampleDtlsId" : "",
															  "orderDetailsId":"",
															  "listSensitivityTestResultDetailsMaster" : $scope.listSensitivityTestResultDetailsMasterDto
													}
													
													$scope.SensitivityTestResultDetailsMasterDto = 
													{
															  "sesitivityResultDetailsId" : "",
															  "sensitivityResultId":"",
															  "orgId" : $scope.orgId ,
															  "orgUnitId" : $scope.orgUnitId,
															  "organismGroupId":"",
															  "organismGroupName":"",
															  "organismId" : "",
															  "organismName":"",
															  "selectedAll":"",
															  "createdBy" : $scope.userId,
															  "createdDate" : "",
															  "updatedBy" : "",
															  "updatedDate" : "",
															  "isDeleted" : "N",
															  "listSensitivityTestResultSubDetailsMaster" : $scope.listSensitivityTestResultSubDetailsMasterDto
													}
											  }else{
												  $scope.flagCheck = true;
											  $scope.SensitivityTestResultMasterDto = response.data.object;
											  $scope.listSensitivityTestResultDetailsMasterDto = response.data.object.listSensitivityTestResultDetailsMaster;
											  angular.forEach($scope.listSensitivityTestResultDetailsMasterDto, function(value, key) {
												       value.isChecked = true
													});
											  
											  }
											}
										else if (response.data.status=='error')
											{
											  $rootScope.stopLoader();
											}
									
									});
				} catch (e) {
					console.log(e.message)
				}

			}
			
			
			$scope.getSensitivityCheckTestDetails = function(labSampleDtlsId)
			{
				
				 $('#sensitivityTesting').modal('show');
				 
				 if(labSampleDtlsId!=null)
					 {
					   $scope.getSensitivityCheckTestResultDetails(labSampleDtlsId);
					 }
			}
			
			
			$scope.saveResult = function(sampleStatusId)
			{
				$scope.SensitivityTestResultMasterDto.createdDate = moment().toDate().getTime();
				$scope.SensitivityTestResultMasterDto.reportReleaseBy = ($scope.pathologiestId!=null &&  $scope.pathologiestId>0)?$scope.pathologiestId:$scope.userId;
				$scope.SensitivityTestResultMasterDto.labSampleDtlsId = $scope.labSampleDtlsId;
				$scope.SensitivityTestResultMasterDto.orderDetailsId = $scope.orderDetailsId;
			    $scope.SensitivityTestResultMasterDto.listSensitivityTestResultDetailsMaster = $scope.listSensitivityTestResultDetailsMasterDto;
			    $scope.SensitivityTestResultMasterDto.amend = true;
			    $('#details').modal('hide');
				$rootScope.startLoader();
				try {
					console.log("$scope.listLabSampleDtls",JSON.stringify($scope.SensitivityTestResultMasterDto));
					var URI = BASE_URL + ROOT_URL
					        + LIS_MICROBIOLOGY
							+ SAVE_SENSITIVTY_DETAILS + S + sampleStatusId;
					console.log("SEND_TO_Microbiology WORKLIST", URI);
					GenericService
							.serviceAction(METHOD_POST, URI,
									$scope.SensitivityTestResultMasterDto)
							.then(
									function(response) {
										if (response.data.status == 'success') 
										{ 
											$scope.flagCheck = true;
											$rootScope.stopLoader();
											$scope.saveReportEntry(sampleStatusId);
											$scope.init();
										
											growl.success(response.data.message,
															{
																title : 'Success!'
															});
											$scope.$broadcast('show-errors-reset');
										
										} else if (response.data.status == 'error') {
											$rootScope.stopLoader();
											growl.error(response.data.message,
															{
																title : 'Error!'
															});
										}
									});
				} catch (e) {
					console.log("Exception", e.message);
				}
			}
			
			$scope.saveSensitivityResult = function(labSampleStausId,flag) 
			{
				
				if(flag)
					{
					var isExists =false;;
					var list = $scope.listSensitivityTestResultDetailsMasterDto
					angular.forEach($scope.listSensitivityTestResultDetailsMasterDto, function(value, key) {
						angular.forEach(value.listSensitivityTestResultSubDetailsMaster, function(value1, key) 
								{
							if( value1.isChecked == true)
								{
								  isExists =true;
								}
							});
						});
					
					if(!isExists)
						{
						growl.error("Please select Antibiotics.",
								{
									title : 'Error!'
								});
						return false;
						}
					else{
						$scope.saveResult(labSampleStausId);
					  }
					}
			
				else
				{
					$scope.saveResult(labSampleStausId);
				}
			}
			
			 $scope.typeSearchOutPatientList = function(searchKeyword)
				{
				 if(searchKeyword.length >= 3){
					try 
					{
						$rootScope.startLoader();
						var searchObj = {
									  "orgId" : $scope.orgId,
									  "orgUnitId" :$scope.assignUnit,
									  "assignUnit" :$scope.orgUnitId ,
									  "fromDt": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
									  "toDt":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
									  "patientId" :  $scope.selectedPatient,
									  "labSampleDtlsId" : 0,
									  "visitTypes" :$scope.selectedVisitType,
									  "testTypes" : $scope.selectedtestList,
									  "deptId" : $scope.deptId,
									  "subDeptId" : $scope.subDeptId,
									  "subDepts" : $scope.selectedDepartMentList,
									  "offset":$scope.offset,
									  "recordPerPage":$scope.noOfRecordsPerPage,
									  "searchField":searchKeyword,
									  "isMailSend" : $scope.isMailSend!=""?  $scope.isMailSend: null,
									  "labSortingId":($scope.labSortingId!=null && $scope.labSortingId!=undefined) ? $scope.labSortingId:null	
                         };
						
						var data1 = {
								"orgId":$scope.orgId,
								"orgUnitId" :$scope.assignUnit,
								"assignUnit" :$scope.orgUnitId ,
								"deptId":$scope.deptId,
								"subDeptId":$scope.subDeptId,
								"fromDate": $scope.fromDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.fromDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
								"toDate":  $scope.toDate==undefined?moment(new Date()).format("YYYY-MM-DD"):moment(moment($scope.toDate,'YYYY-MM-DD').toDate()).format("YYYY-MM-DD"),
								"isMailSend" : $scope.isMailSend!=""?  $scope.isMailSend: null
							};
						
						var URI = BASE_URL 
						        + ROOT_URL
								+ '/LIS/ReportHandOver/ReportHandOverList/Type/Search'
						console.log("URI", URI);
						
						var URI2 = BASE_URL
						+ ROOT_URL
						+ LIS_REPORT_HAND_OVER
						+ LIST_FOR_REPORT_HANDOVER_RELEASE_COUNT;

				promiseFactoryWithObject
						.setPromisesWithObject(URI, URI2, METHOD_POST,
								METHOD_POST,searchObj,data1)
						.then(
								function(response) {
									$rootScope.stopLoader();
									 $scope.biochemWorklist=[];
									 $scope.resultReportHandOver=[];
									 $scope.resultReportHandOver=[];
									 $scope.resultReportHandOverTest=[];
									 $scope.resultReportHandOverTest = response[0].listObject;
									
										if($scope.staffSubSpecialityList != null && $scope.staffSubSpecialityList.length > 0 ){
											angular.forEach($scope.resultReportHandOverTest, function(value, key) {
												angular.forEach($scope.staffSubSpecialityList ,function(value1,key1){
													if(value.subDeptId == value1.subSpecialityMasterId){
														$scope.resultReportHandOver.push(value);
													}
												
												});		
											 });
										}else{
											$scope.resultReportHandOver = response[0].listObject;
										}
									angular.forEach($scope.resultReportHandOver,function(value,key){
										value.selectOnlyOneVisitType = true;
									});
									$scope.resultReportHandOverForFiltter = $scope.resultReportHandOver;
									$scope.filterByDummyServices();
									$scope.commonListCount = response[1].object;
									$scope.setPage(1,false);
								});
						
					} catch (e) {
						console.log(e.message)
					}
				 }else  if(searchKeyword.length == 0){
					 $scope.searchOutPatientList();
				 }
				}
			 
			 $scope.labSortingList = [];
				$scope.getLabSortingMasterList = function () {
					var URI = BASE_URL + ROOT_URL + "/LIS/transaction/labSortingList";
					$scope.labSortingList = [];
					GenericService.serviceAction("GET", URI).then(function (response) {
						if (response.data.status == "success") {
							$scope.labSortingList = response.data.listObject;
						}
					});
				}
				
				$scope.getLabSortingMasterList();
			 
			
	
    
    
    }

    function config($stateProvider) {
        $stateProvider.state("amendmentWorklist", {
            url: '/amendmentWorklist',
            templateUrl: 'views/LIS/CentralArea/amendment-worklist.html',
            controller: "amendmentWorklistController",
            title: 'Released reports',
            controllerAs: 'vm',
            params: 
			        {
             		searchObj : {}
		            }
        });
    }

    angular
        .module("myApp")
        .config(config)
        .controller("amendmentWorklistController", amendmentWorklistController)
})();  