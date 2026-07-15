angular
		.module('myApp')
		.controller(
				'OTreservationCancellationController',
				[
						'$scope',
						'$rootScope',
						'$state',
						'$http',
						'GenericService',
						'$sessionStorage',
						'$cookies',
						'growl',
						'$localStorage',
						'BillingGenericService',
						'PaginationService',
						'$window',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService,PaginationService,$window) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.updateBtnFlag = false;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							$scope.visitTypeId = 0;
							$scope.fromDate = moment().toDate();
						    $scope.toDate = moment().toDate();
							 $scope.reasonId = "",
							 $scope.reasonNote = "";
							$scope.dateOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate:new Date(),
								minDate : moment(new Date()).subtract(180, 'days').toDate()
							};

						    
						    $scope.dateOptions1 =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								minDate:new Date()
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
							
							 $scope.pagination= {
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
				                  };
							 
							//search reserved procedures
							$scope.procedureSearch = '';
							$scope.procedureDate;
							$scope.searchProcedures = function(searchKeyword) {

								$scope.reservedProcedureList = [];

								var data = {
										serviceName : searchKeyword,
										organizationId : $scope.organizationId,
										unitId :  $scope.unitId
								};
								var URI = BASE_URL + ROOT_URL + "/api/otims/OTReservation/searchReservedProcedureName";
										
								console.log('URI', URI);
								return BillingGenericService
										.serviceAction(METHOD_POST, URI, data,
												NOTIFICATION_MSG_STATUS_FALSE)
										.then(
												function(response) {
													if (response.data.status == "success") {
														$scope.reservedProcedureList = response.data.listObject;
													}
													return $scope.reservedProcedureList;
												});
							}
							
							$scope.serviceId = "";
							$scope.selectProcedureName = function($item,$model, $label) {
								
								$rootScope.startLoader();
								$scope.serviceId =  $item.serviceMasterId;
								console.log("$item.serviceStandardName",$item.serviceStandardName);
								
								$rootScope.stopLoader();
							}
							
							$scope.reservationList = []; 
							$scope.serachProcedure = function()
							{
								$scope.reservationList = []; 
								try {
									$scope.startLoader();
									var data = {
											"fromDate":moment($scope.fromDate).format('YYYY-MM-DD'),
											"toDate":moment($scope.toDate).format('YYYY-MM-DD'),
											"serviceId" :$scope.serviceId==""?null : $scope.serviceId,
											"organizationId" : $scope.organizationId,		
											"unitId" :  $scope.unitId
										};
									var URI = BASE_URL + ROOT_URL+"/api/otims/OTReservation/searchOTReservation";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														if(response.data.listObject.length>0){
															$scope.pagination.data=[];
															$scope.reservationList = response.data.listObject;
															 if(!!response && !!response.data && response.data.listObject.length > 0){            
										                            PaginationService.searchData($scope.reservationList, $scope.pagination);
										                          }
														}
														$rootScope.stopLoader();
													});
									
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}

							}
							
							
							$scope.reasonList = [];
							$scope.getReasonMasterList = function()
							{
								var data={
										organizationId:$scope.organizationId
								};
								var URI = BASE_URL + ROOT_URL+ GET_REASON_MASTER_LIST ;
								GenericService
								.serviceAction(METHOD_POST, URI ,data)
								.then(
										function(response) {

											if (response.data.status == "success") {
												$rootScope.stopLoader();
												$scope.reasonList = response.data.listObject;
												console.log("reasonList ",response.data.listObject);
											}
										});
							}
							$scope.getReasonMasterList();
							$scope.serachProcedure();
							
							$scope.surgeonList = [];
							$scope.getSurgeonList = function(info){
								try {
									$scope.startLoader();
									$scope.surgeonList = [];
									var data = {
											"procedureScheduleId":info.scheduleId,
											"organizationId" : $scope.organizationId,		
											"unitId" :  $scope.unitId
										};
									var URI = BASE_URL + ROOT_URL+"/api/otims/dashboard/getProcedureSchedulingSurgeonList";
									console.log("URI", URI);
									GenericService
											.serviceAction("POST", URI, data)
											.then(
													function(response) {
														$scope.surgeonList = response.data.listObject;
														$rootScope.stopLoader();
													});
									
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}

							}
							$scope.currentIdx = '';
							$scope.getIndex = function(indx){
									$scope.currentIdx = indx;
									$("#confirmCancellation").modal("show");
							}
							
							$scope.cancelOTReservation = function(){
								try {
									
									if( $scope.reasonId == ""){
										growl.warning("Please Select Reason ", {
											title : 'warning!'
										});
										return false;
									}else if($scope.reasonNote == ""){
										growl.warning("Please Enter Reason Note", {
											title : 'warning!'
										});
										return false;
									}else{
										
										$scope.startLoader();
										var data = {
												"proReqId":$scope.reservationList[$scope.currentIdx].proReqId,
												"reasonId":$scope.reasonId,
												"reasonNote":$scope.reasonNote
											};
										var URI = BASE_URL + ROOT_URL+"/api/otims/OTReservation/cancelOTReservation";
										console.log("URI", URI);
										GenericService
												.serviceAction("POST", URI, data)
												.then(
														function(response) {
															 $scope.reasonId = "",
															 $scope.reasonNote = "";
															 $scope.serachProcedure();
															 $rootScope.stopLoader();
														});
										
									}
									
								
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}

							}
							
							$scope.editOTReservation = function(info){
								info.isFromReservationCancellation = true;
								$state.go('OTreservation',
										{
									commonObj: info
										});
							}

							$scope.visitNumber = "";
							$scope.currentIdx = "";
							$scope.updateVisitType = function(visitNumber,index){
								$scope.visitNumber = visitNumber;
								$scope.currentIdx = index;
								$("#updateVisit").modal("show");
							}
							
							$scope.saveVisit = function(){
								$rootScope.startLoader();
								$scope.reservationList[$scope.currentIdx].visitTypeList
								var param = {};
								angular.forEach($scope.reservationList[$scope.currentIdx].visitTypeList,function(value,index){
									if(value.visitNumber == $scope.visitNumber){
										
										if(value.visitTypeId == 1){
											param.encounterId = value.admissionId;
										}else{
											param.admissionId = value.admissionId;
										}
										param.visitTypeId = value.visitTypeId;
										param.otReservationId =  $scope.reservationList[$scope.currentIdx].otReservationId;
										param.proReqId = $scope.reservationList[$scope.currentIdx].proReqId;
										param.patientId = $scope.reservationList[$scope.currentIdx].patientId;
									}
								});
								
								var URI = BASE_URL + ROOT_URL + "/api/procedure/BillOfMaterial/updateReservationAgainst/admission/encounter";
								GenericService.serviceAction(METHOD_POST,URI,param).then(function(response) {
									if (response.data.status == "success")
									{
										$rootScope.stopLoader();
										growl.success("Successfully Updated !!!",
												{
													title : 'Success!'
												});
										$scope.serachProcedure();
									}
									$rootScope.stopLoader();
								});
							}
							
							$scope.cancelVisit = function(){
								$scope.reservationList[$scope.currentIdx].visitNumber =null;
							}
							
							$scope.patientDocumnetInformation = function(info) {
								$scope.patientDocumentFlipBook ={};
								$scope.patientDocumentFlipBook =info;
								
								
								
									var data =
									{
									"encounterId":$scope.patientDocumentFlipBook.encounterId,
									"patientId":$scope.patientDocumentFlipBook.patientId,
									"unitId":$scope.unitId,
									"organisationId":$scope.organizationId,
									"userId":$scope.userId
									}
									
									
								var URI = BASE_URL + ROOT_URL + GET_PATIENT_DOCUMENT_ENCOUNTER;
								BillingGenericService.serviceAction(METHOD_POST, URI, data, NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
										$scope.patientDocumentListTest = [];
										if (response.data.status == "success") 
										{
											$scope.patientDocumentListTest = response.data.listObject;
											if($scope.patientDocumentListTest != null && $scope.patientDocumentListTest.length >0){
												$('#patientDocumentOption').modal('show');
											}
											$rootScope.stopLoader();
										}
										else if(response.data.status == "error")
											{
											$rootScope.stopLoader();
											}
									});
						
					          };
					          
					          $scope.convertDateToTimestamp = function(dateString) {
					                let parts = dateString.split("-");
					                let day = parseInt(parts[0], 10);
					                let month = parseInt(parts[1], 10) - 1; 
					                let year = parseInt(parts[2], 10);
					                let date = new Date(year, month, day);
					                let timestampInMilliseconds = date.getTime();
					                let timestampInSeconds = timestampInMilliseconds / 1000;
					                return timestampInSeconds;
					            };
					            
						     		$scope.generateFlipbookData = function() {
						                  let flipbookDatalist = [];

							     			var filteredlist=[];
							     		    angular.forEach($scope.patientDocumentListTest,function(filterObj,index){
							     		    	
							     		    	if(filterObj.documentPath != null){
							     				var flipbookDataobj={};
							     				 let createdDate = filterObj.createdDate;
							     	            let timestamp = $scope.convertDateToTimestamp(createdDate);
							     				flipbookDataobj.timestamp=timestamp;
							     				flipbookDataobj.url=filterObj.documentPath;
							     				flipbookDataobj.tags=[];
							     				flipbookDataobj.tags.push("Document");
							     				flipbookDataobj.title=filterObj.documentName != null ? filterObj.documentName :"Lab Report" ; 
							     				flipbookDatalist.push(flipbookDataobj);
							     		    	}
							     				
							     			});

						                let data = {
						                    flipbookData: flipbookDatalist,
						                    logo: $scope.firstImgaa,
						                    privacy: "public"
						                };

						                let baseUrlLocation = $window.location.href;
						                let temp = baseUrlLocation.split('/');
						                let BASE_URL_CASESHEET = baseUrlLocation.replace(temp[temp.length - 1], "");

						                let generateFlipbookurl = "https://feqituyulh.execute-api.ap-south-1.amazonaws.com/dev/api/generateFlipbook";
						                
						        		try {
						        			var param={};
						        				$.ajax({
						        			        url : generateFlipbookurl,
						        					type : "POST",
						        					headers: {'X-Api-Key': "apikey_zHOw4R758ELytSeHFII99YibXj2sWHR6u9mxKdipN/g="},
						        					contentType : "application/json",
						        					dataType : "json",
						        					data : JSON.stringify(data),
						        			        //headers:getHeaders(),
						        					success : function(response){
						        						if(response.flipbookUrl!=undefined &&  response.flipbookUrl!=null  &&  response.flipbookUrl!="" ){
						        							  window.open(''+response.flipbookUrl, '_blank');
						        						}else{
						        							$.notify("Error occurred","error");
						        						}
						        			    	}
						        			});
						        	     } catch (e) {
						        	 		console.log(e );
						        		}
						            };
							
			}]);
						