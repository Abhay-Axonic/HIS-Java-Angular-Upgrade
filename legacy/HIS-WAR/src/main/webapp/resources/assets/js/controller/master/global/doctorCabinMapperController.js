/**
 * 
 */

angular.module('myApp')
		.controller("doctorCabinMapperController",
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
			$scope.doctorList=[];
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
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			
			$scope.DocCabinMapper=[];
			$scope.cabinList = [];
			$scope.selectDoctorCabinMapperList=[];
			
			
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						doctorCabinMpprId:"",
						cabinId:"",
						doctorId:"",
						doctorName:"",
						isDeleted:"",
						cabinDesc:""
			
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
			$scope.initDoctorCabinMapper = function(){
				$scope.DoctorCabinMapper =
				{
					 doctorCabinMpprId:$scope.common.doctorCabinMpprId,
					 cabinId:$scope.common.cabinId,
					 doctorId:$scope.common.doctorId,
				     status:$scope.common.status,
					 createdBy:$scope.createdBy,
					 updatedBy:$scope.updatedBy,
				     organisationId:$scope.organisationId,
				     unitId:$scope.unitId,
				     cabinDesc:$scope.common.cabinDesc, 
				     title:$scope.common.title,
				     isDeleted: "N"
				 }
				
				}
			
			//Get Diagnosis List
			  		
			$scope.searchGroupList = function() {
				
				var data = {
						unitId : $scope.unitId,
						organizationId : $scope.organizationId
				};
				var paramObj = {
						
				};
				var headerObj = {									
					unitId : $scope.unitId,
					organizationId : $scope.organizationId
				}

				var URI = BASE_URL + ROOT_URL+ "/api/global/unit/getActiveCabinMaster/"+$scope.organizationId+"/"+$scope.unitId;
				
				return GenericServiceParamHeader.serviceAction("GET", URI,data,NOTIFICATION_MSG_STATUS_FALSE,paramObj,headerObj)
						.then(function(response) {
							$scope.cabinList=[];
									// $rootScope.startLoader();
									if (response.data.status == "success")
										$scope.cabinList= response.data.listObject;
									console.log("cabinList",$scope.cabinList);
								});

			}
	
			$scope.searchGroupList();
			
			//Get Diagnosis List
			$scope.docSelectCabin=[];
			$scope.addCabin = function(cabinId,cabinDesc){
				
				var flag = false;
				angular.forEach($scope.docSelectCabin,function(value,index){
					if(value.cabinId == cabinId){
						$scope.docSelectCabin.splice(index,1);
						$scope.DocCabinMapper[value.cabinId] = false;
						flag = true;
					}
				})
				if(!flag){
					var temp = {cabinId:cabinId,cabinDesc:cabinDesc};
					$scope.docSelectCabin.push(temp);
				}
			}
			$scope.selectCabin = function()
			{
				$scope.selectDoctorCabinMapperList = angular.copy($scope.docSelectCabin);
			}
			//Get Diagnosis List end
			
			//Get Template List 
			$scope.getDoctorList = function(){
		  		
		  		var URI = BASE_URL + ROOT_URL + GET_LIST_OF_DOCTOR+S+$scope.organisationId+S+$scope.unitId;
		  		
		  		GenericService.serviceAction("GET",URI).then(function(response){
		  			if(response.data.status == "success"){
				  			$rootScope.stopLoader();					
				  			$scope.doctorList = [];
				  			angular.forEach(response.data.listObject,function(value,key){
				  				if(value.isRMO == 'N'){
				  					$scope.doctorList.push(value);
				  				}
				  			});
		  			console.log("doctorList",$scope.doctorList);
		  			}
		  		});
		  		}
		  		$scope.getDoctorList();
		  	//Get Template List end
		  		
		  	//Get Detailed List By Template Id
			$scope.getDoctorCabinMapperByDoctorId = function(doctorId) {
				try {
					var data = "";
					$rootScope.startLoader();	
					$scope.getListObject=[];
					var URI = BASE_URL + ROOT_URL+GET_DETAILED_CABIN_LIST+S+doctorId+S+$scope.organisationId+S+$scope.unitId;
					console.log("URI", URI);
					GenericService
							.serviceAction("GET", URI, "")
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
				var data1 ={ 
						  organisationId:$scope.organisationId,
		  			      unitId:$scope.unitId,
		  			      offset:offset,
		  			      noOfRecordsPerPage:noOfRecordsPerPage
					};
				
				var data2 = "";
				
				var URI1 = BASE_URL + ROOT_URL +GET_DOCTOR_CABIN_MAPPER_LIST; 
				var URI2 = BASE_URL + ROOT_URL +GET_DOCTOR_CABIN_MAPPER_COUNT+S+$scope.organisationId+S+$scope.unitId;
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
					}
				});
				
				GenericService.serviceAction("GET",URI2,"").then(function(response){
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
							  organisationId:$scope.organisationId,
			  			      unitId:$scope.unitId,
			  			      offset:offset,
			  			      noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var URI = BASE_URL + ROOT_URL + GET_DOCTOR_CABIN_MAPPER_LIST
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
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
//				if ($scope.commonform.$valid)
//				{
					try {
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
						
						$rootScope.startLoader();
						$scope.initDoctorCabinMapper();
						$scope.listDoctorCabinMapper =
						{
								 doctorId:$scope.common.doctorId,
							     organisationId:$scope.organisationId,
							     unitId:$scope.unitId,
							     status:"A",
								 createdBy:$scope.createdBy,
								 createdDate:$scope.dt,
								 updatedBy:$scope.updatedBy,
								 updatedDate:$scope.dt,
							     selectDoctorCabinMapperList:[]
						     
						 }
						angular.forEach($scope.selectDoctorCabinMapperList,function(value,index){
								$scope.listDoctorCabinMapper.selectDoctorCabinMapperList.push(value.cabinId);
						})
						console.log("TemplateDiagnosisMaster",JSON.stringify($scope.listDoctorCabinMapper));
			//			
                        if($scope.listDoctorCabinMapper.doctorId=="" || $scope.listDoctorCabinMapper.doctorId==null || $scope.listDoctorCabinMapper.doctorId==undefined){
							
							growl.warning("Please select user.",
									{
										title : 'Warning!'
									});
									$rootScope.stopLoader();
									return false;

						}
						else if($scope.listDoctorCabinMapper.selectDoctorCabinMapperList.length <= 0){
							
							growl.warning("Please select cabin.",
									{
										title : 'Warning!'
									});
									$rootScope.stopLoader();
									return false;

						}
            //          
						var URI = BASE_URL + ROOT_URL + ADD_DOCTOR_CABIN_MAPPER;
						GenericService.serviceAction("POST", URI, $scope.listDoctorCabinMapper).then(function(response) 
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
								$scope.selectDoctorCabinMapperList=[];
								$scope.DocCabinMapper=[];
								$scope.docSelectCabin = [];
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
					} catch (e) {
						console.log("Exception",e.message);
					}
					
//				}
			}
			
			
			$scope.showUpdateBtn = function(id)
			{
				try {
					$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateBtnFlag = true;
					var URI = BASE_URL + ROOT_URL +GET_DOCTOR_CABIN_MAPPER_BY_ID+S+id+S+$scope.organisationId+S+$scope.unitId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET", URI, $scope.common).then(function(response) 
					{
						console.log("data",response.data);
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							$scope.common.doctorId = response.data.listObject[0].doctorId + "";
							$scope.selectDoctorCabinMapperList = [];
							$scope.tempDiagnosis = [];
							
							angular.forEach($scope.DocCabinMapper,function(value,index){
								$scope.DocCabinMapper[index] = false;
							var i = $scope.docSelectCabin.indexOf(value);
							$scope.docSelectCabin.splice(i,1);
							})
							
							angular.forEach(response.data.listObject,function(value,index){
								var temp = {cabinId:value.cabinId,cabinDesc:value.diagnosisDesc};
								$scope.docSelectCabin.push(temp);
								$scope.DocCabinMapper[value.cabinId] = true;
								$scope.selectDoctorCabinMapperList.push(temp);
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
//				if ($scope.commonform.$valid)
//				{
					try {
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');		
						
						$rootScope.startLoader();
						$scope.initDoctorCabinMapper();
						$scope.listDoctorCabinMapper =
						{
							 doctorId:$scope.common.doctorId,
						     organisationId:$scope.organisationId,
						     unitId:$scope.unitId,
						     status:"A",
							 createdBy:$scope.createdBy,
							 createdDate:$scope.dt,
							 updatedBy:$scope.updatedBy,
							 updatedDate:$scope.dt,
						     selectDoctorCabinMapperList:[]
						     
						 }
						angular.forEach($scope.selectDoctorCabinMapperList,function(value,index){
								$scope.listDoctorCabinMapper.selectDoctorCabinMapperList.push(value.cabinId);
						})
						console.log("TemplateDiagnosisMaster",JSON.stringify($scope.listDoctorCabinMapper));
					
						var URI = BASE_URL + ROOT_URL +UPDATE_DOCTOR_CABIN_MAPPER;
					    GenericService.serviceAction("POST", URI, $scope.listDoctorCabinMapper).then(function(response) 
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
								$scope.selectDoctorCabinMapperList=[];
								$scope.DocCabinMapper=[];
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
					} catch (e) 
					{
						console.log("Exception",e.message);
					}
					
//				}
			}
			
			$scope.updateStatus = function(id,status)
			{
				try {
					$rootScope.startLoader();
					$scope.initDoctorCabinMapper();
					var URI = BASE_URL + ROOT_URL +UPDATE_DOCTOR_CABIN_MAPPER_STATUS+S+id+S+$scope.organisationId+S+$scope.unitId+S+status;
					console.log("ACT_INACT_URI",URI);
					GenericService.serviceAction("GET", URI, $scope.DoctorCabinMapper).then(function(response) 
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
							$scope.selectDoctorCabinMapperList=[];
							$scope.DocCabinMapper=[];
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