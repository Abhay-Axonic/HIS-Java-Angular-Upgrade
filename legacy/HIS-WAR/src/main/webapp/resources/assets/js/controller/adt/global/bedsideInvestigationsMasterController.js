/**
 * @Auth:Sneha Dalave
 * 
 */
angular.module('myApp')
		.controller("bedsideInvestigationsMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','PaginationService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,PaginationService){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;


			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
				$state.go('login');
				return;
			}
			
			$scope.unitId = cookieObject.unitId;
			$scope.organizationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
			if(cookieObjectAppConfig == undefined){
			$state.go('login');
			return;
			}
			else
			{
			console.log('cookieObjectAppConfig',cookieObjectAppConfig);

			}

			if(cookieObjectAppConfig.radiologySpecialityId!=null || cookieObjectAppConfig.radiologySpecialityId !="" || cookieObjectAppConfig.radiologySpecialityId!=undefined)
			{
			$scope.radiologySpecialityId = cookieObjectAppConfig.radiologySpecialityId;
			}else{
			growl.warning("Radiology department is not configured...!!",{
			title : 'Warning!'
			});
			return false;
			}		
			
			 $scope.pagination = {
	                    currentPage: 1,
	                    totalPages: 0,
	                    pageSize: '10',
	                    search: '',
	                    searchData: PaginationService.searchData,
	                    setTotalPages: PaginationService.setTotalPages,
	                    getPagesList: PaginationService.getPagesList,
	                    setPage: PaginationService.setPage,
	                    setFirstPage: PaginationService.setFirstPage,
	                    setPreviousPage: PaginationService.setPreviousPage,
	                    setNextPage: PaginationService.setNextPage,
	                    setLastPage: PaginationService.setLastPage,
	                    sortBy: PaginationService.sortBy
	                  }
			 
			$scope.firstTabLevel = 1;
			
			$scope.setFirstTabLevel = function(newTab) {
				
				$scope.newTab=newTab;
				$scope.setFirstLevelTab(newTab);
				$scope.firstTabLevel = newTab;
				if (newTab == '1') {
//					$scope.setSecondTabLevel(1);
				}
				if (newTab == '2') {
					/*$scope.setSecondTabLevel();*/
				}
				if (newTab == '3') {
//					$scope.setHabitDetails();
				}
				
			};
			
			$scope.isSetFirstTabLevel = function(tabNum) {
				return $scope.firstTabLevel === tabNum;
			};
			
			$scope.setFirstLevelTab = function(newTab) {
				$scope.newTab=newTab;
				switch (newTab) {
				case 1:
					$scope.test = "Lab"
					$scope.tab($scope.test);
					break;
				case 2:
					$scope.test = "Imaging"
					$scope.tab($scope.test);
					break;
				case 3:
					$scope.test = "Procedures"
					$scope.tab($scope.test);
					break;
				}
			}
			
			$scope.tab=function(test){
				
				 $scope.pagination = {
		                    currentPage: 1,
		                    totalPages: 0,
		                    pageSize: '10',
		                    search: '',
		                    searchData: PaginationService.searchData,
		                    setTotalPages: PaginationService.setTotalPages,
		                    getPagesList: PaginationService.getPagesList,
		                    setPage: PaginationService.setPage,
		                    setFirstPage: PaginationService.setFirstPage,
		                    setPreviousPage: PaginationService.setPreviousPage,
		                    setNextPage: PaginationService.setNextPage,
		                    setLastPage: PaginationService.setLastPage,
		                    sortBy: PaginationService.sortBy
		                  }
				 
			 $scope.test=test;
			if($scope.test == "Lab"){
//				alert("lab");
				
				
				$scope.initMasterForLab = function(){
					$scope.saveBtnFlag = true;
					$scope.updateBtnFlag = false;
					$scope.commonlab = {
							
							bedsideLabId:"",
							bedsideLabCode:"",
							bedsideLabDesc:"",
							bedsideUnitId:"",
							maxReferenceRange:"",
							minReferenceRange:"",
							status:""
								
					};
				}
				$scope.initMasterForLab();
				
				$scope.getUnitActiveList = function() {

			/*		var URI = BASE_URL + ROOT_URL+ "/api/globals/strength/units";
					var data = {
                                     organizationId : $scope.organizationId
                               }
					GenericService.serviceAction("GET", URI, data).then(function(response) {

								$rootScope.stopLoader();
								$scope.getUOMActiveList = [];
								$scope.getUOMActiveList = response.data;
                                console.log("getUOMActiveList",$scope.getUOMActiveList);

							});*/
					
					var URI =BASE_URL + ROOT_URL +"/api/globals/strength/units/"+$scope.organizationId;
					GenericService.serviceAction("GET", URI, {}).then(
					function(response) {
					$scope.getUOMActiveList = [];
					$scope.getUOMActiveList = response.data;

					});
				}
				$scope.getUnitActiveList();
				
				// function for popup ok button call
				$scope.PopupOkBtn = function() {
					$scope.popUpFlag = false;
					$scope.saveLab();
				}
				
				$scope.saveLab = function(){
					try{
						$rootScope.startLoader();
						if($scope.popUpFlag==false)
							{
							  $rootScope.startLoader();
							}
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						$scope.$broadcast('show-errors-check-validity');
						var data={	
								
//								"bedsideLabId":2,
								"bedsideLabCode":$scope.commonlab.bedsideLabCode,
								"bedsideLabDesc":$scope.commonlab.bedsideLabDesc,
								"bedsideUnitId":$scope.commonlab.bedsideUnitId,
								"maxReferenceRange":$scope.commonlab.maxReferenceRange,
								"minReferenceRange":$scope.commonlab.minReferenceRange,
								"status":"A",
								"organizationId":$scope.organizationId,
								"unitId":$scope.unitId,
								"createdBy":$scope.createdBy,
								"createdDate":$scope.dt,
								"updatedBy":$scope.updatedBy,
								"updatedDate":$scope.dt,
							
							}
						
						if($scope.commonlab.bedsideLabCode != "" && $scope.commonlab.bedsideLabDesc != "" && $scope.commonlab.maxReferenceRange != ""  &&
								$scope.commonlab.minReferenceRange !=""){
						
						if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;

						} else {
						var URI = BASE_URL + ROOT_URL + "/api/global/saveBedsideLabInvestigation";
						GenericService.serviceAction("POST",URI,data).then(function(response){
							
							if (response.data.status == 'success')
							{
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								
								$scope.$broadcast('show-errors-reset');
								$scope.initMasterForLab();
								$scope.getActiveLabInvestigation();
								$rootScope.stopLoader();
							} else if(response.data.status == 'error')
							{
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
								$rootScope.stopLoader();
							}
						});
						$rootScope.stopLoader();
					}
						}
						else{
							 growl.error("Please Enter all Fields",
										{
											title : 'Error!'
										});
								$rootScope.stopLoader();
						}
						
					}catch(e){
						console.log("Exception-",e);
					}
				}
				
				$scope.getActiveLabInvestigation = function() {

					var URI = BASE_URL + ROOT_URL+ "/api/global/getBedsideLabInvestigationList";
					var data={
							
							"organizationId":$scope.organizationId,
							"unitId":$scope.unitId,
					}
					
					GenericService.serviceAction("POST", URI,data).then(function(response) {

								$rootScope.stopLoader();
								$scope.pagination.data=[];
								$scope.getLabInvestigation = [];
								$scope.getLabInvestigation = response.data.listObject;
								
								 if($scope.getLabInvestigation.length > 0){            
			                         PaginationService.searchData($scope.getLabInvestigation , $scope.pagination);
			                       }
                                console.log("getLabInvestigation",$scope.getLabInvestigation);

							});
				}
				$scope.getActiveLabInvestigation();
				
				
				$scope.updateStatusLab = function(bedsideLabId,status){
					try{
						$rootScope.startLoader();
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						
						$scope.commonlab.updatedBy=$scope.updatedBy,
						$scope.commonlab.updatedDate=$scope.dt,
						$scope.commonlab.status=status,
						$scope.commonlab.bedsideLabId=bedsideLabId,
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/global/updateBedsideLabInvestigationStatus";
						console.log("update status",$scope.commonlab);
						//return false;
						GenericService.serviceAction("POST",URI,$scope.commonlab).then(function(response){
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
//								$scope.$broadcast('show-errors-reset');
								$scope.initMasterForLab();
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
						});
					}catch(e){
						console.log("Exception",e);
					}
				}
				
				
				$scope.showUpdateBtnLab = function(id){
					try{
						$rootScope.startLoader();
						$scope.saveBtnFlag = false;
						$scope.updateScope = true;
						$scope.updateBtnFlag = true;
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/global/getBedsideLabInvestigationById/" +S+ id +S+ $scope.organizationId +S+ $scope.unitId;
						console.log("EDIT",URI);
						GenericService.serviceAction("GET",URI).then(function(response){
							console.log("Edit",response);
							
							if(response.data.status == "success"){
								$rootScope.stopLoader();
								$scope.commonlab = response.data.listObject[0];
								$scope.commonlab.bedsideLabId=response.data.listObject[0].bedsideLabId;
							}else if(response.data.status == 'error'){
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
						});
						
					}catch(e){
						console.log("Exception-",e);
					}
				}
				
				
				
				$scope.updateLab = function(){
					try{
					
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
						$scope.$broadcast('show-errors-check-validity');
						$rootScope.startLoader();

						var data={	
								   "bedsideLabId":$scope.commonlab.bedsideLabId,
									"bedsideLabCode":$scope.commonlab.bedsideLabCode,
									"bedsideLabDesc":$scope.commonlab.bedsideLabDesc,
									"bedsideUnitId":$scope.commonlab.bedsideUnitId,
									"maxReferenceRange":$scope.commonlab.maxReferenceRange,
									"minReferenceRange":$scope.commonlab.minReferenceRange,
									"status":$scope.commonlab.status,
									"organizationId":$scope.organizationId,
									"unitId":$scope.unitId,
									"updatedBy":$scope.updatedBy,
									"updatedDate":$scope.dt,
								
							}
									
						var URI = BASE_URL + ROOT_URL + "/api/global/updateBedsideLabInvestigation";
						console.log("Update-URI-",URI);
						console.log("Update OBJ",data);
						$rootScope.startLoader(); 
					
						GenericService.serviceAction("POST",URI,data).then(function(response){
							
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								$scope.$broadcast('show-errors-reset');
								$scope.initMasterForLab();
								$scope.getActiveLabInvestigation();
								
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
						});
						
					}catch(e){
						console.log("Exception-",e);
					}
				}
				
				/********************end Lab**************/
			}else if($scope.test == "Imaging"){
//				alert("Imaging");
				/********************start Imaging **************/
				

				$scope.initMasterForImagging = function(){
					$scope.saveBtnFlag = true;
					$scope.updateBtnFlag = false;
					$scope.commonImagging = {
							
							bedsideImagingId:"",
							bedsideImagingCode:"",
							bedsideImagingDesc:"",
							subSpecialityId:"",
							status:""
								
					};
				}
				$scope.initMasterForImagging();
				
				$scope.getSubSpecialityBySpeciality = function(obj)
				{
					
					
					var data = {
							unitId : $scope.unitId,
							organizationId : $scope.organizationId,
							specialityId:$scope.radiologySpecialityId
						};
						var URI = BASE_URL + ROOT_URL
								+ GETSUBSPECIALITYBYSPECIALITYID;
						GenericService
								.serviceAction(METHOD_POST, URI, data,
										NOTIFICATION_MSG_STATUS_FALSE)
								.then(
										function(response) {
											console.log(response);
											$scope.subSpecialityList = [];
											// $rootScope.startLoader();
											if (response.data.status == "success")
												$scope.subSpecialityList = response.data.listObject;
											console.log("$scope.subSpecialityList",$scope.subSpecialityList);
										});
					
				}
				$scope.getSubSpecialityBySpeciality();
				
				
				$scope.PopupOkBtn = function() {
					$scope.popUpFlag = false;
					$scope.saveImaging();
				}
				
				$scope.saveImaging = function(){
					try{
						$rootScope.startLoader();
						if($scope.popUpFlag==false)
							{
							  $rootScope.startLoader();
							}
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						$scope.$broadcast('show-errors-check-validity');
						var data={	
								
								"bedsideImagingCode":$scope.commonImagging.bedsideImagingCode,
								"bedsideImagingDesc":$scope.commonImagging.bedsideImagingDesc,
								"subSpecialityId":$scope.commonImagging.subSpecialityId,
								"status":"A",
								"organizationId":$scope.organizationId,
								"unitId":$scope.unitId,
								"createdBy":$scope.createdBy,
								"createdDate":$scope.dt,
								"updatedBy":$scope.updatedBy,
								"updatedDate":$scope.dt,
							
							}
						if($scope.commonImagging.bedsideImagingCode != "" && $scope.commonImagging.bedsideImagingDesc !="" &&  $scope.commonImagging.subSpecialityId !=""){
						if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;

						} else {
						var URI = BASE_URL + ROOT_URL + "/api/global/saveBedsideImagingMaster";
						GenericService.serviceAction("POST",URI,data).then(function(response){
							
							if (response.data.status == 'success')
							{
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								
								$scope.$broadcast('show-errors-reset');
								$scope.initMasterForImagging();
								$scope.getActiveImaggingInvestigation();
								$rootScope.stopLoader();
							} else if(response.data.status == 'error')
							{
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
								$rootScope.stopLoader();
							}
						});
						$rootScope.stopLoader();
					}
						}
						else{
							 growl.error("Please Enter all Fields",
										{
											title : 'Error!'
										});
								$rootScope.stopLoader();
						}
						
					}catch(e){
						console.log("Exception-",e);
					}
				}
				
				
				$scope.getActiveImaggingInvestigation = function() {

					var URI = BASE_URL + ROOT_URL+ "/api/global/getBedsideImagingMasterList";
					var data={
							
							"organizationId":$scope.organizationId,
							"unitId":$scope.unitId,
					}
					
					GenericService.serviceAction("POST", URI,data).then(function(response) {

								$rootScope.stopLoader();
								
								$scope.pagination.data=[];
								$scope.getImaggingInvestigation = [];
								$scope.getImaggingInvestigation = response.data.listObject;
								
								if($scope.getImaggingInvestigation.length > 0){            
			                         PaginationService.searchData($scope.getImaggingInvestigation, $scope.pagination);
			                       }
                                console.log("getImaggingInvestigation",$scope.getImaggingInvestigation);

							});
				}
				$scope.getActiveImaggingInvestigation();
				
				
				$scope.showUpdateBtnImaging = function(info){
					try{
						$scope.saveBtnFlag = false;
						$scope.updateScope = true;
						$scope.updateBtnFlag = true;
						$scope.commonImagging = info;
						$scope.commonImagging.bedsideImagingId=info.bedsideImagingId;
						
					}catch(e){
						console.log("Exception-",e);
					}
				}
				
				$scope.updateImagging = function(){
					try{
					
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
						$scope.$broadcast('show-errors-check-validity');
						$rootScope.startLoader();

						var data={	
								 "bedsideImagingId":$scope.commonImagging.bedsideImagingId,
								"bedsideImagingCode":$scope.commonImagging.bedsideImagingCode,
								"bedsideImagingDesc":$scope.commonImagging.bedsideImagingDesc,
								"subSpecialityId":$scope.commonImagging.subSpecialityId,
								"status":"A",
								"organizationId":$scope.organizationId,
								"unitId":$scope.unitId,
								"createdBy":$scope.createdBy,
								"createdDate":$scope.dt,
								"updatedBy":$scope.updatedBy,
								"updatedDate":$scope.dt
								
							}
									
						var URI = BASE_URL + ROOT_URL + "/api/global/updateBedsideImagingMaster";
						$rootScope.startLoader(); 
					
						GenericService.serviceAction("POST",URI,data).then(function(response){
							
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								$scope.$broadcast('show-errors-reset');
								$scope.initMasterForImagging();
								$scope.getActiveImaggingInvestigation();
								
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
						});
						
					}catch(e){
						console.log("Exception-",e);
					}
				}
				
			
				$scope.updateStatusImaging = function(bedsideImagingId,status){
					try{
						$rootScope.startLoader();
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						
						$scope.commonImagging.updatedBy=$scope.updatedBy,
						$scope.commonImagging.updatedDate=$scope.dt,
						$scope.commonImagging.status=status,
						$scope.commonImagging.bedsideImagingId=bedsideImagingId,
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/global/updateBedsideImagingMasterStatus";
						console.log("update status",$scope.commonlab);
						//return false;
						GenericService.serviceAction("POST",URI,$scope.commonImagging).then(function(response){
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
//								$scope.$broadcast('show-errors-reset');
								$scope.initMasterForImagging();
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
						});
					}catch(e){
						console.log("Exception",e);
					}
				}

				
				/***************END IMAGINGN***************/
			}
			else{
//				alert("Procedures");
				
				
              /********************start Procedures **************/
				

				$scope.initMasterForPocedure = function(){
					$scope.saveBtnFlag = true;
					$scope.updateBtnFlag = false;
					$scope.commonPocedure = {
							
							bedsideProcedureId:"",
							bedsideProcedureCode:"",
							bedsideProcedureDesc:"",
							isAnaesthesia:'N',
							duration:"",
							status:""
								
					};
				}
				$scope.initMasterForPocedure();
				
				
				
				$scope.PopupOkBtn = function() {
					$scope.popUpFlag = false;
					$scope.saveProcedure();
				}
				
				$scope.saveProcedure = function(){
					try{
						$rootScope.startLoader();
						if($scope.popUpFlag==false)
							{
							  $rootScope.startLoader();
							}
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						$scope.$broadcast('show-errors-check-validity');
						var data={	
								
								"bedsideProcedureCode":$scope.commonPocedure.bedsideProcedureCode,
								"bedsideProcedureDesc":$scope.commonPocedure.bedsideProcedureDesc,
								"isAnaesthesia":$scope.commonPocedure.isAnaesthesia,
								"duration":$scope.commonPocedure.duration,
								"status":"A",
								"organizationId":$scope.organizationId,
								"unitId":$scope.unitId,
								"createdBy":$scope.createdBy,
								"createdDate":$scope.dt,
								"updatedBy":$scope.updatedBy,
								"updatedDate":$scope.dt,
							
							}
						if($scope.commonPocedure.bedsideProcedureCode != "" && $scope.commonPocedure.bedsideProcedureDesc != ""){
						if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;

						} else {
						var URI = BASE_URL + ROOT_URL + "/api/global/saveBedsideProcedureMaster";
						GenericService.serviceAction("POST",URI,data).then(function(response){
							
							if (response.data.status == 'success')
							{
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								
								$scope.$broadcast('show-errors-reset');
								$scope.initMasterForPocedure();
								$scope.getBedsideProcedureMasterList();
								$rootScope.stopLoader();
							} else if(response.data.status == 'error')
							{
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
								$rootScope.stopLoader();
							}
						});
						$rootScope.stopLoader();
					}
						}
						else
							{
							
							growl.error("Please Enter all Fields",
									{
										title : 'Error!'
									});
							$rootScope.stopLoader();
							
							}
					}catch(e){
						console.log("Exception-",e);
					}
				}
				
				
				$scope.getBedsideProcedureMasterList = function() {

					var URI = BASE_URL + ROOT_URL+ "/api/global/getBedsideProcedureMasterList";
					var data={
							
							"organizationId":$scope.organizationId,
							"unitId":$scope.unitId,
					}
					
					GenericService.serviceAction("POST", URI,data).then(function(response) {

								$rootScope.stopLoader();
								$scope.pagination.data = [];
								$scope.getBedsideProcedureList = [];
								$scope.getBedsideProcedureList = response.data.listObject;
								
								 if($scope.getBedsideProcedureList.length > 0){            
			                         PaginationService.searchData($scope.getBedsideProcedureList, $scope.pagination);
			                       }
                                console.log("getBedsideProcedureList",$scope.getBedsideProcedureList);

							});
				}
				$scope.getBedsideProcedureMasterList();
				
				
				$scope.showUpdateBtnProcedure = function(info){
					try{
						$scope.saveBtnFlag = false;
						$scope.updateScope = true;
						$scope.updateBtnFlag = true;
						$scope.commonPocedure = info;
						$scope.commonPocedure.bedsideProcedureId=info.bedsideProcedureId;
						
					}catch(e){
						console.log("Exception-",e);
					}
				}
				
				$scope.updateProcedure = function(){
					try{
					
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');	
						$scope.$broadcast('show-errors-check-validity');
						$rootScope.startLoader();

						var data={	
								 "bedsideProcedureId":$scope.commonPocedure.bedsideProcedureId,
								 "bedsideProcedureCode":$scope.commonPocedure.bedsideProcedureCode,
								 "bedsideProcedureDesc":$scope.commonPocedure.bedsideProcedureDesc,
								 "isAnaesthesia":$scope.commonPocedure.isAnaesthesia,
								 "duration":$scope.commonPocedure.duration,
								 "status":"A",
							     "organizationId":$scope.organizationId,
							     "unitId":$scope.unitId,
								 "updatedBy":$scope.updatedBy,
								 "updatedDate":$scope.dt,
								
							}
									
						var URI = BASE_URL + ROOT_URL + "/api/global/updateBedsideProcedureMaster";
						$rootScope.startLoader(); 
					
						GenericService.serviceAction("POST",URI,data).then(function(response){
							
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
								$scope.$broadcast('show-errors-reset');
								$scope.initMasterForPocedure();
								$scope.getBedsideProcedureMasterList();
								
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
						});
						
					}catch(e){
						console.log("Exception-",e);
					}
				}
				
			
				$scope.updateStatusProcedure = function(bedsideProcedureId,status){
					try{
						$rootScope.startLoader();
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						
						$scope.commonPocedure.updatedBy=$scope.updatedBy,
						$scope.commonPocedure.updatedDate=$scope.dt,
						$scope.commonPocedure.status=status,
						$scope.commonPocedure.bedsideProcedureId=bedsideProcedureId,
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + "/api/global/updateBedsideProcedureMasterStatus";
						console.log("update status",$scope.commonlab);
						//return false;
						GenericService.serviceAction("POST",URI,$scope.commonPocedure).then(function(response){
							if (response.data.status == 'success')
							{
								$rootScope.stopLoader();
								growl.success(response.data.message,
										{
											title : 'Success!'
										});
//								$scope.$broadcast('show-errors-reset');
								$scope.initMasterForImagging();
							} else if(response.data.status == 'error')
							{
								$rootScope.stopLoader();
								growl.error(response.data.message,
										{
											title : 'Error!'
										});
							}
						});
					}catch(e){
						console.log("Exception",e);
					}
				}

				
				/********************End Procedures **************/
			}
			}
			
		
	
			
		
}]);