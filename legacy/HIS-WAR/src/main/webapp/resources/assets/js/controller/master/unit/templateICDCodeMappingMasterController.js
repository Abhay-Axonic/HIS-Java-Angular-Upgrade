/**
 * 
 */

angular.module('myApp')
		.controller("templateICDCodeMappingMasterController",
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
			
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
			$scope.tempDiagnosisMapper=[];
			$scope.diagnosisList = [];
			$scope.selectTemplateDiagnosisMapperList=[];
			
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						templateDiagnosisMapperId:"",
						templateId:"",
						diagnosisId:"",
						title:"",
						diagnosisDesc:"",
						isDeleted:"",
						diagnosisDesc:""
			
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			$scope.initTemplateDiagnosisMaster = function(){
				$scope.templateDiagnosisMaster =
				{
					 templateDiagnosisMapperId:$scope.common.templateDiagnosisMapperId,
					 diagnosisId:$scope.common.diagnosisId,
					 templateId:$scope.common.templateId,
				     status:$scope.common.status,
					 createdBy:$scope.createdBy,
					 updatedBy:$scope.updatedBy,
				     organisationId:$scope.organisationId,
				     unitId:$scope.unitId,
				     diagnosisDesc:$scope.common.diagnosisDesc, 
				     title:$scope.common.title,
				     isDeleted: "N"
				 }
				
				}
			
			//Get Diagnosis List
			  		
			$scope.searchGroupList = function() {
				
				var data = {};
				var paramObj = {
						
				};
				var headerObj = {									
					unit_id : $scope.unitId,
					organization_id : $scope.organisationId
				}
				 
				var URI = BASE_URL + ROOT_URL+ GETLISTOFDIAGNOSIS+S+$scope.organisationId+S+$scope.unitId;
				
				return GenericServiceParamHeader.serviceAction(METHOD_GET, URI,data,NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
						.then(function(response) {
							$scope.diagnosisList=[];
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.diagnosisList= response.data.listObject;
									console.log("diagnosisList",$scope.diagnosisList);
								});

			}
	
			//$scope.searchGroupList();
			$scope.addedDiagnosisList=[];
			$scope.diagonisIdforTemplate = 0;
			
			$scope.selectedDiagnosisDesc="";
			$scope.selectedDiagnosisId="";
			$scope.diagnosisName="";
			// Selected TypeHead on Diagnosis
			$scope.selectDignosisIdForDiagnosis = function(info, model,
					label,ind) {
				console.log(info);
				$scope.selectedDiagnosisId = info.diagnosisId;
				$scope.selectedDiagnosisDesc = info.diagnosisName;
				
			}
			
			// for ICD 10 Typehead
			$scope.searchGroupList = function(keyword) {
				
				var data = {};
				var paramObj = {
						diagnosisName : keyword
				};
				var headerObj = {
					// doctor_id :
					// $scope.appointmentDetailsObj.doctorId,
						// doctor_id :1,
					unit_id : $scope.unitId,
					organization_id : $scope.organizationId
				}
				console.log(data);

				var URI = BASE_URL + ROOT_URL
						+ DIAGNOSISAUTOFILLSEARCH;
				
				return GenericServiceParamHeader
						.serviceAction(METHOD_GET, URI,
								data,
								NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
						.then(
								function(response) {
									console.log(response);
									// $rootScope.startLoader();
									if (response.data.status == "success")
										return response.data.listObject;
								});

			}
			
				
				
			
			
			//Get Diagnosis List
		/*	$scope.tempSelectDiagnosis=[];
			$scope.addDiagnosis = function(diagnosisId,diagnosisDesc){
				
				var flag = false;
				angular.forEach($scope.tempSelectDiagnosis,function(value,index){
					if(value.diagnosisId == diagnosisId){
						$scope.tempSelectDiagnosis.splice(index,1);
						$scope.tempDiagnosisMapper[value.diagnosisId] = false;
						flag = true;
					}
				})
				if(!flag){
					var temp = {diagnosisId:diagnosisId,diagnosisDesc:diagnosisDesc};
					$scope.tempSelectDiagnosis.push(temp);
				}
			}*/
			
			$scope.addDiagnosis = function(){
				if($scope.selectedDiagnosisId==''){
					growl.warning("Select ICD 10 code first.",
							{
								title : 'Warning!'
							});
					return false;
				}
				var flag=false;
				angular.forEach($scope.selectTemplateDiagnosisMapperList,function(value,index){
					if(value.diagnosisId==$scope.selectedDiagnosisId){
						flag=true;
					}
				});
				
				if(flag==false){
					var newArr = {};
					newArr.diagnosisId = $scope.selectedDiagnosisId;
	    		 	newArr.diagnosisDesc = $scope.selectedDiagnosisDesc;
	    		 	$scope.selectTemplateDiagnosisMapperList.push(newArr);
					$scope.selectedDiagnosisDesc="";
					$scope.selectedDiagnosisId="";
					$scope.diagnosisName="";
				}else{
					$scope.selectedDiagnosisDesc="";
					$scope.selectedDiagnosisId="";
					$scope.diagnosisName="";
					growl.warning("ICD Code 10 Already Exist.",
							{
								title : 'Warning!'
							});
					return false;
				}
				
			}
			
			$scope.removeDiagnosis=function(index){
				$scope.selectTemplateDiagnosisMapperList.splice(index,1);
			}
			
			
			$scope.selectDiagnosis = function()
			{
				$scope.selectTemplateDiagnosisMapperList = angular.copy($scope.tempSelectDiagnosis);
			}
			//Get Diagnosis List end
			
			//Get Template List 
			$scope.getTemplateList = function(){
		  		
		  		var URI = BASE_URL + ROOT_URL + GET_TEMPLATE_LIST+S+$scope.organisationId+S+$scope.unitId;
		  		var data = {
		  				orgId:$scope.organisationId,
		  			      unitId:$scope.unitId
		  		}
		  		GenericService.serviceAction("GET",URI,data).then(function(response){
		  			if(response.data.status == "success"){
				  			$rootScope.stopLoader();					
				  			$scope.templateList = [];
				  			$scope.templateList=response.data.listObject;

		  			console.log("templateList",$scope.getTemplateList);
		  			}
		  		});
		  		}
		  		$scope.getTemplateList();
		  	//Get Template List end
		  		
		  	//Get Detailed List By Template Id
			$scope.getTemplateDiagnosisMapperByTemplateId = function(templateId) {
				try {
					var data = "";
					$rootScope.startLoader();	
					$scope.getListObject=[];
					var URI = BASE_URL + ROOT_URL+GET_TEMPLATE_DIAGNOSI_MAPPER_DETAILED_LIST+S+templateId+S+$scope.organisationId+S+$scope.unitId;
					console.log("URI", URI);
					GenericService
							.serviceAction("GET", URI, data)
							.then(
									function(response) {
										$scope.getListObject  = response.data.listObject;
										console.log("getListObject",JSON.stringify($scope.getListObject));
									});
					$rootScope.stopLoader();
				} catch (e) {
					console.log(e.message)
				}

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
				var data1 = "";
				
				var data2 = "";
				
				var URI1 = BASE_URL + ROOT_URL +GET_TEMPLATE_DIAGNOSIS_MAPPER_LIST+S+offset+S+noOfRecordsPerPage; 
				var URI2 = BASE_URL + ROOT_URL +GET_TEMPLATE_DIAGNOSIS_MAPPER_COUNT;
					
				
				GenericService.serviceAction("GET",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
					}
				});
				
				GenericService.serviceAction("GET",URI2,data2).then(function(response){
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
				
				var data = "";
				
				var URI = BASE_URL + ROOT_URL + GET_TEMPLATE_DIAGNOSIS_MAPPER_LIST+S+offset+S+noOfRecordsPerPage;;
				console.log("URI", URI);
				GenericService.serviceAction("GET", URI, data).then(function(response) {
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
				if ($scope.commonform.$valid)
				{
					try {
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
						
						$rootScope.startLoader();
						$scope.initTemplateDiagnosisMaster();
						$scope.listTemplateDiagnosisMaster =
						{
								 templateId:$scope.common.templateId,
							     organisationId:$scope.organisationId,
							     unitId:$scope.unitId,
							     status:"A",
								 createdBy:$scope.createdBy,
								 createdDate:$scope.dt,
								 updatedBy:$scope.updatedBy,
								 updatedDate:$scope.dt,
							     selectTemplateDiagnosisMapperList:[]
						     
						 }
						angular.forEach($scope.selectTemplateDiagnosisMapperList,function(value,index){
								$scope.listTemplateDiagnosisMaster.selectTemplateDiagnosisMapperList.push(value.diagnosisId);
						})
						console.log("TemplateDiagnosisMaster",JSON.stringify($scope.listTemplateDiagnosisMaster));
						var URI = BASE_URL + ROOT_URL + SAVE_TEMPLATE_DIAGNOSIS_MAPPER;
						GenericService.serviceAction("POST", URI, $scope.listTemplateDiagnosisMaster).then(function(response) 
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
								$scope.tempDiagnosisMapper=[];
								$scope.selectTemplateDiagnosisMapperList=[];
								$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error("Already exist",
										{
											title : 'Error!'
										});
							}
							
						});
					} catch (e) {
						console.log("Exception",e.message);
					}
					
				}
			}
			
			$scope.prevTemplateId="";
			$scope.showUpdateBtn = function(id)
			{
				try {
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateBtnFlag = true;
					var URI = BASE_URL + ROOT_URL +GET_TEMPLATE_DIAGNOSIS_MAPPER_BY_ID+S+id+S+$scope.organisationId+S+$scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
					{
						console.log("data",response.data);
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							$scope.common.templateId = response.data.listObject[0].templateId + "";
							$scope.selectTemplateDiagnosisMapperList = [];
							$scope.tempDiagnosis = [];
							$scope.prevTemplateId=$scope.common.templateId;
							angular.forEach($scope.tempDiagnosisMapper,function(value,index){
								$scope.tempDiagnosisMapper[index] = false;
							var i = $scope.tempSelectDiagnosis.indexOf(value);
							$scope.tempSelectDiagnosis.splice(i,1);
							})
							
							angular.forEach(response.data.listObject,function(value,index){
								var temp = {diagnosisId:value.diagnosisId,diagnosisDesc:value.diagnosisDesc};
								//$scope.tempSelectDiagnosis.push(temp);
								//$scope.tempDiagnosisMapper[value.diagnosisId] = true;
								$scope.selectTemplateDiagnosisMapperList.push(temp);
							});
							
							console.log("$scope.common",$scope.common);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							alert("Error In Fetching Data");
						}
					});
				} catch (e) {
					console.log("Exception",e.message);
				}
				
			}
			
			$scope.update = function() 
			{
				$scope.$broadcast('show-errors-check-validity');
				if ($scope.commonform.$valid)
				{
					try {
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');		
						
						$rootScope.startLoader();
						$scope.initTemplateDiagnosisMaster();
						$scope.listTemplateDiagnosisMaster =
						{
							 templateId:$scope.common.templateId,
						     organisationId:$scope.organisationId,
						     unitId:$scope.unitId,
						     status:"A",
							 createdBy:$scope.createdBy,
							 createdDate:$scope.dt,
							 updatedBy:$scope.updatedBy,
							 updatedDate:$scope.dt,
						     selectTemplateDiagnosisMapperList:[],
						   prevTemplateId :$scope.prevTemplateId
						 }
						angular.forEach($scope.selectTemplateDiagnosisMapperList,function(value,index){
								$scope.listTemplateDiagnosisMaster.selectTemplateDiagnosisMapperList.push(value.diagnosisId);
						})
						console.log("TemplateDiagnosisMaster",JSON.stringify($scope.listTemplateDiagnosisMaster));
					
						var URI = BASE_URL + ROOT_URL +UPDATE_TEMPLATE_DIAGNOSIS_MAPPER;
					    GenericService.serviceAction("POST", URI, $scope.listTemplateDiagnosisMaster).then(function(response) 
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
								$scope.tempDiagnosisMapper=[];
								$scope.selectTemplateDiagnosisMapperList=[];
								$scope.initMasterList($scope.organisationId, $scope.offset, $scope.noOfRecordsPerPage, $scope.unitId);
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error("Already exist",
										{
											title : 'Error!'
										});
							}
						});
					} catch (e) 
					{
						console.log("Exception",e.message);
					}
					
				}
			}
			
			$scope.updateStatus = function(id,status)
			{
				try {
					$rootScope.startLoader();
					$scope.initTemplateDiagnosisMaster();
					var URI = BASE_URL + ROOT_URL +UPDATE_TEMPLATE_DIAGNOSIS_MAPPER_STATUS+S+id+S+$scope.organisationId+S+$scope.unitId+S+status;
					console.log("ACT_INACT_URI",URI);
					GenericService.serviceAction("GET", URI, $scope.templateDiagnosisMaster).then(function(response) 
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
							$scope.tempDiagnosisMapper=[];
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