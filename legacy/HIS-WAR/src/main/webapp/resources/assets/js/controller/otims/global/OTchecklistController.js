angular
		.module('myApp')
		.controller(
				'OTchecklistController',
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
						'$stateParams',
						'$window',
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService, $stateParams,$window) {
							
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
							
							$scope.fromDatepickerOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								maxDate:new Date()
							};

							$scope.toDatepickerOptions =
							{
								formatYear : 'yyyy',
								showWeeks : false,
								minDate:new Date()
							};

							
							$scope.fromDatepickerOpen = function() {
								$scope.fromDatepickerPopup.opened = true;
							};

							$scope.fromDatepickerPopup =
							{
								opened : false
							};
							
							$scope.toDatepickerPopup =
							{
								opened : false
							};
							
							$scope.toDatepickerOpen = function() 
							{
								$scope.toDatepickerPopup.opened = true;
							};
							
							$scope.searchObj = {
									visitTypeId:0,
									fromDate:new Date(),
									toDate:new Date()
							};
							$scope.otChecklist = [];
							var date = new Date();
							$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
							
							/*$scope.checklistObj = $stateParams.checklistObj;
							console.log("$scope.checklistObj",$scope.checklistObj);
				
							if($scope.checklistObj!=undefined){
								$scope.otChecklist.push($scope.checklistObj);
							}*/
							
							
							$scope.searchOTCheck = function(){
								$scope.otChecklist = []; 
								try {
									$scope.startLoader();
									var data = {
											"toDate":moment($scope.searchObj.toDate).format('YYYY-MM-DD'),
											"fromDate":moment($scope.searchObj.fromDate).format('YYYY-MM-DD'),
											"organizationId" : $scope.organizationId,
											"unitId" : $scope.unitId,
											"visitTypeId" : $scope.searchObj.visitTypeId,
										};
									var URI = BASE_URL + ROOT_URL+"/api/otims/dashboard/getOtCheckList";
									console.log("URI", URI);
									GenericService .serviceAction("POST", URI, data) .then(
													function(response) {
														$scope.otChecklist = response.data.listObject;
														$rootScope.stopLoader();
														
														console.log("$scope.surgeriesOfTheDayList",$scope.surgeriesOfTheDayList);
													});
								} catch (e) {
									console.log(e.message)
									$scope.stopLoader();
								}	
							}
							
							$scope.searchOTCheck();
						
							$scope.checklistDetails = function(info){
								info.mapOfBed = 1;
								$state.go('OTchecklistDetails',
										{
									checklistDetailsObj: info
										});
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
		