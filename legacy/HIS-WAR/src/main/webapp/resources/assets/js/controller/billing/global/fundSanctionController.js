angular
		.module('myApp')
		.controller(
				"fundSanctionController",
				[	    	'$scope',
							'$http',
							'$localStorage',
							'$sessionStorage',
							'$cookies',
							'$rootScope',
							'GenericService',
							'PagerService',
							'promiseFactory',
							'BillingGenericService',
							'GenericServiceFileUpload',
							'reportService',
							'growl',
							'$state',
							'GenericServiceParamHeader',
							'$stateParams',
					function($scope, $http, $localStorage, $sessionStorage,
								$cookies, $rootScope, GenericService,promiseFactory,PagerService,
								BillingGenericService,GenericServiceFileUpload,reportService, growl, $state,GenericServiceParamHeader,
								$stateParams,$modalInstance) {

							$rootScope.loginpage = true;

						    $scope.saveBtnFlag = true;
							$scope.updateBtnFlag = false;
							//$scope.setPaymentModeId(1);
							/** Variables for pagination */
							$scope.offset = 0;
							$scope.noOfRecordsPerPage = 10;
							$scope.commonList = [];
							$scope.totalNoOfRecords;
							$scope.searchKeyword;
							$scope.commonListCount;
							
							

							$scope.visitTypeList = [ {
								id : 1,
								label : "OP"
							}, {
								id : 2,
								label : "IP"
							}, {
								id : 3,
								label : "Day Care"
							}, {
								id : 4,
								label : "ER"
							} ];
							$scope.selectedVisitType = [];
							$scope.patientList =[];
							$scope.patientDetails = {};
							 
							/** * Check user is Loggedin */
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							$scope.unitId = cookieObject.unitId;
							$scope.orgId = cookieObject.organizationId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							console.info($scope.orgId)
								var data = {};
							$scope.initCommon = function() {
								$scope.saveBtnFlag = true;
								$scope.updateBtnFlag = false;
								$scope.sanctionRequestObj = {
								    "fundsanctionrequestDetailsId" : "",
									"trustId" : "",
									"fundrequestAmount":"",
									"sanctionReqStatus" : "A",
									"createdBy" : $scope.userId,
									"createdDate" : "",
									"updatedBy" : $scope.userId,
									"updatedDate" : "",
									"orgId" : $scope.orgId,
									"unitId" : $scope.unitId,
									"toDate" :'',
									"fromDate" :'',
									"fundrequestRemark" : "",
									"patientId" : 0,
								}
							};
							$scope.initCommon();
							
							
							$scope.openDatePickerFromDate = function() {
								$scope.datepickerFromDate.opened = true;
							};

							$scope.datepickerFromDate = {
								opened : false
							};
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate : new Date()
								};
							$scope.dateOptions1 = {
									formatYear : 'yyyy',
									showWeeks : false,
									minDate : new Date()
								};
							
							$scope.openDatePicker = function() { 
								$scope.datepicker.opened = true;
							};
							$scope.openDatePickerSearch = function() {
								$scope.datepicker2.opened = true;
							};
							
							$scope.datepicker2 = {
									opened : false
								};
				
					$scope.setNoOfRecords = function() {
						$scope.initTrustMasterList($scope.orgId,
								$scope.offset,
								$scope.noOfRecordsPerPage);
					};
					
					
					$scope.getTrustActiveList = function() {

						var URI = BASE_URL+ ROOT_URL+ "/api/billing/getActiveTrustList";
						var data = {
                                      orgId : $scope.orgId ,
								      unitId:$scope.unitId
								    
								   }
						GenericService.serviceAction("POST", URI, data).then(function(response) {

									$rootScope.stopLoader();
									$scope.getTrustActiveList = [];
									$scope.getTrustActiveList = response.data.listObject;
	                                
	                               

								});
					}
					$scope.getTrustActiveList();
					
					
					
							//Get trust List
						$scope.getTrustList = function() {
				        var URI = BASE_URL + ROOT_URL +"/api/billing/getTrustList/"+$scope.orgId +"/"+$scope.unitId;
				    GenericService
				        .serviceAction(METHOD_GET, URI,
				                NOTIFICATION_MSG_STATUS_FALSE)
				        .then(
				                function(response) {
				                    console.log(response);
				                    if (response.data.status == "success"){
				                        $scope.trustList = response.data.listObject;
				                        console.log("list",$scope.trustList);
				                    }
				                    
				                });

						}
						$scope.getTrustList();
						
		
						// to and from date picker
						$scope.sanctionRequestObj.toDate= new Date();
						$scope.sanctionRequestObj.fromDate= new Date();
						$scope.openDatePicker1 = function() { 
							$scope.datepicker1.opened = true;
						};
						$scope.datepicker1 = {
								opened : false
							};
						$scope.openDatePicker2 = function() { 
							$scope.datepicker2.opened = true;
						};
						$scope.datepicker2 = {
								opened : false
							};
						
						$scope.openDatePicker3 = function() { 
							$scope.datepicker3.opened = true;
						};
						$scope.datepicker3 = {
								opened : false
							};
						var trustId="";
						$scope.getTrustDetails = function(id) {
						
							var data = {
								  trustId : id,
								  orgId : $scope.orgId ,
							      unitId:$scope.unitId
							};
							 var URI = BASE_URL + ROOT_URL +"/api/billing/getTrustDetailsById";
							BillingGenericService
							.serviceAction(METHOD_POST, URI, data,
									NOTIFICATION_MSG_STATUS_FALSE)
							.then(
									function(response) {
					                    console.log(response);
					                    if (response.data.status == "success"){
					                    	
					                    	$scope.getStateByCountry(response.data.object.countryId);
					                    	$scope.getDistrictByState(response.data.object.stateId);
					                    	$scope.getCityByDistrict(response.data.object.districtId);
					                    	$scope.getAreaByCity(response.data.object.cityId);
					                    	$scope.sanctionRequestObj  = response.data.object;
					                    	$scope.selectedVisitType=response.data.listObject;
					                    	$scope.trustObj.validFrom= response.data.object.validFrom
					                        console.log("contactPersonEmail",$scope.selectedVisitType );
					                    	$scope.sanctionRequestObj.trustId=id;
					                    	$scope.sanctionRequestObj.toDate= new Date();
											$scope.sanctionRequestObj.fromDate= new Date();
					                    }
					                   
										//Get CollectionList
										//
					                });
							    var toDate= new Date();
								var fromDate= new Date();
								var trustId=id;
								//$scope.collectionList(toDate,fromDate,trustId);
						}
						
						
						
						
						 
						$scope.initPatientDetails=function(){
							$scope.patientSearchObj = {
									"uhIdNumber"      : "",
									"patientName"     : "",
									"genderId"        : "",
									"mobileNo"        : "",
									"birthDate"       : "",
									"identificationNo":"",
									"patientType"	  :"1",
									"unitId"          :$scope.unitId,
									"organizationId"  :$scope.organizationId
							}
							
						};
						$scope.initPatientDetails();
						
						
						
							
			     	
			    	
			    	
			   
			    	$scope.searchPatient = function(patientsearchObj){
						
						if(patientsearchObj.uhIdNumber == "" && patientsearchObj.patientName == "" && patientsearchObj.genderId == "" && patientsearchObj.mobileNo == ""  && patientsearchObj.identificationTypeId == "" && patientsearchObj.identificationNo == "" && patientsearchObj.birthDate=="")
						{
							growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"});
							return false;
						}
							
						else{
								//alert("else AlERT");
							}
						if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
						{
							growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
							$scope.patientList = [];
							return false;
						}	
						//patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
						
						if(patientsearchObj.birthDate != "" && typeof(patientsearchObj.birthDate) != "undefined"){
							patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
						} 
						
						$rootScope.startLoader();
						$scope.patientList =[];
						var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
						BillingGenericService.serviceAction(METHOD_POST,URI, patientsearchObj, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
							
							if(response.data.listObject==null){
								growl.warning(response.data.message,{
									title : ERROR_MSG
								});
								$scope.patientSearchObj.birthDate="";
								$scope.patientList=[];
								return false;
							}
							
							if(Object.keys(response.data.listObject).length  == 0){
								growl.warning("No Result Found!",{
									title : ERROR_MSG
								});
								$scope.patientSearchObj.birthDate="";
								$scope.selectedPatientId="";
								return false;
							}
							else{
								$scope.patientList = [];
							angular.forEach(response.data.listObject, function (value, key) {
								 
								if(value.registrationTypeId != 5 && value.registrationTypeId !=2)
									{
									 $scope.patientList.push(value);
									}
								  
							  });
							$scope.patientSearchObj.birthDate="";
							$scope.selectedPatientId="";
							}
							
						});
						
					}
			    	
			    	$scope.getDocumentType = function(){
						$rootScope.startLoader();
						var URI = BASE_URL + ROOT_URL + GETACTIVEDOCUMENTTYPELIST;
						BillingGenericService.serviceAction(METHOD_GET, URI, "", NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
								$scope.documentTypeList = [];
								if (response.data.status == "success") {
									$scope.documentTypeList = response.data.listObject;
									 /*angular.forEach($scope.documentTypeList, function(value, key) 
									 {  
							 			$scope.corporateDocTypeId=value.documentTypeId;
									 });*/
								}
							});
					}
					$scope.getDocumentType();
					$scope.encounterDetailsList=[];
			    	$scope.getpatientSanctionRequest=function(){
			              try {
			            	  var data={
			            			  patientId : $scope.sanctionRequestObj.patientId,
			            			  orgId :     $scope.orgId,
									  unitId :    $scope.unitId
			            			  };
			            	  $rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
								+ "/api/billing/fundSanctionRequestByPatientId";
								GenericService.serviceAction("POST", URI,
										data)
								.then(
										function(response) {
											if (response.data.status == 'success') {
										       $rootScope.stopLoader();
										       $scope.commonlist = [];
										   	    $scope.commonlist = response.data.listObject;	
									
										} else if (response.data.status == 'error') {
											 $rootScope.stopLoader();
											growl.error(
															response.data.message,
															{
																title : 'Error!'
															});
											
										}
									});
							} catch (e) {
								console.log("Exception", e.message);
								 $rootScope.stopLoader();
								growl.error(
										response.data.message,
										{
											title : 'Error!'
										});
							}

			       }
			    	
			    	
			    	
			    	
			    	
			    	$scope.selectedPatientId = "";
					$scope.getPatientDetailsByIndex = function(index){
						$scope.abscondPatientFlag="";
						if($scope.patientList[index].isAbsconded==undefined || $scope.patientList[index].isAbsconded==null || $scope.patientList[index].isAbsconded=='N'){
//							$scope.initReferralObj();
							$scope.patientDetails = {};
							console.log("$scope.patientList",$scope.patientList[index]);
							///$rootScope.startLoader();
							$scope.patientDetails.age=($scope.patientList[index].age);
							$scope.patientDetails.patientId=($scope.patientList[index].patientId);
							$scope.patientDetails.patientName=($scope.patientList[index].patientName);
							$scope.patientDetails.genderCode=($scope.patientList[index].genderCode);
							$scope.patientDetails.uhIdNumber=($scope.patientList[index].uhIdNumber);
							$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
							$scope.patientDetails.birthDate=($scope.patientList[index].birthDate);
							$scope.patientDetails.patientProfile = ($scope.patientList[index].patientProfile);
							$scope.patientDetails.genderId = ($scope.patientList[index].genderId);
                            $scope.patientDetails.patientDischargeDate =  moment( $scope.datepickerPdd).format('DD/MM/YYYY');
                            $scope.patientDetails.dateOfAdmission = moment(new Date()).format('DD/MM/YYYY');
                            $scope.selectedPatientId = ($scope.patientList[index].patientId);
                            $scope.sanctionRequestObj.patientId=$scope.patientList[index].patientId;
							$scope.selectedPatient = 'Y';
							$(".patienttSearchResult").hide();
							$scope.getpatientSanctionRequest();
							$scope.getDocumentType();
						 }
					}
			    	
					$scope.createSanctionRequest=function(){
			              try {
			            	 
			            	  $scope.sanctionRequestObj.patientId= $scope.selectedPatientId;
			            	  if($scope.sanctionRequestObj.patientId=="" || $scope.sanctionRequestObj.patientId==0 ) 
								{
									growl.warning("Please select Patient ",
											{
												title : 'warning!'
											});
									return;
								}else if(  $scope.sanctionRequestObj.trustId==undefined || $scope.sanctionRequestObj.trustId==""){
									growl.warning("Please  select Trust Name ",
											{
												title : 'warning!'
											});
									
									return;
								}else if( $scope.sanctionRequestObj.fundrequestAmount==undefined || $scope.sanctionRequestObj.fundrequestAmount=="" ){
									growl.warning("Please enter Amount ",
											{
												title : 'warning!'
											});
									return;
								}
			            	  $rootScope.startLoader();
								var URI = BASE_URL + ROOT_URL
								+ "/api/billing/saveFundSanctionRequest";
								GenericService.serviceAction("POST", URI,
										$scope.sanctionRequestObj)
								.then(
										function(response) {
											if (response.data.status == 'success') {
												 $scope.getpatientSanctionRequest();
												$scope.initCommon();	
												$rootScope.stopLoader();
										      
												growl.success("Fund Request saved successfully",
															{
																title : 'Success!'
															});
									
										} else if (response.data.status == 'error') {
											$scope.initCommon();
											$rootScope.stopLoader();
											growl.error(
															response.data.message,
															{
																title : 'Error!'
															});
											
										}else{
											$scope.initCommon();
											$rootScope.stopLoader();
										}
											
											
//											$scope.searchPatient($scope.patientSearchObj);
									});
							} catch (e) {
								console.log("Exception", e.message);
								growl.error(
										response.data.message,
										{
											title : 'Error!'
										});
							}

			       }
					
					
					$scope.getDocumentUploadPopup=function(sanctionRequestObj){
						
//						   $('#fileId').val(undefined);
							$scope.sanctionDetails = {
									  fundsanctionrequestDetailsId : sanctionRequestObj.fundsanctionrequestDetailsId,
									  patientId : "",
									  documentPath : "",
									  documentTypeId : '',
									  documentTypeName : '',
									  status : 'A',
									  createdDate : "",
									  createdBy : $scope.userId,
									  updatedDate : "",
									  updatedBy : $scope.userId,
									  unitId : $scope.unitId,
									  orgId : $scope.orgId,
									  file : {}
									}
							$scope.sanctionDocDetailsList.push($scope.sanctionDetails);
							$scope.getSanctionRequestDocument($scope.sanctionDetails);
						$('#commonUploadDocuments').modal('show');
					}
					
					$scope.closecommonUploadDocuments=function(){
						
						$('#commonUploadDocuments').modal('hide');
						$scope.sanctionDocDetailsList=[];
						 $scope.finalDocUploadList=[];
					}
					
					$scope.getSanctionRequestDocument=function(sanctionRequestObj){
			              try {
			            	  $rootScope.startLoader();
//			            	  var data={ fundsanctionrequestDetailsId : sanctionRequestObj.fundsanctionrequestDetailsId};
//			            	  $scope.sanctionRequestObj.patientId= $scope.selectedPatientId;
								var URI = BASE_URL + ROOT_URL
								+ "/api/billing/getDocumentsByFundSanctionRequestId";
								GenericService.serviceAction("POST", URI,
										sanctionRequestObj)
								.then(
										function(response) {
											if (response.data.status == 'success') {
												$rootScope.stopLoader();
												
											       $scope.documentlist = [];
											       if( response.data.listObject.length > 0 )
											   	        $scope.documentlist = response.data.listObject;	
									
										} else if (response.data.status == 'error') {
											$rootScope.stopLoader();
											growl.error(
															response.data.message,
															{
																title : 'Error!'
															});
											
										}else{
											$rootScope.stopLoader();
										}
											
											
//											$scope.searchPatient($scope.patientSearchObj);
									});
							} catch (e) {
								console.log("Exception", e.message);
								growl.error(
										response.data.message,
										{
											title : 'Error!'
										});
							}

			       }
					
					$scope.sanctionDocDetailsList=[];
					
					 $scope.addsanctionDetailsForDoc = function(obj)
							{ 
//								 var sanctionDetails = angular.copy($scope.sanctionDetails);
								 var sanctionDetails = {
										  fundsanctionrequestDetailsId : obj.fundsanctionrequestDetailsId,
										  patientId : "",
										  documentPath : "",
										  documentTypeId : '',
										  documentTypeName : '',
										  status : 'A',
										  createdDate : "",
										  createdBy : $scope.userId,
										  updatedDate : "",
										  updatedBy : $scope.userId,
										  unitId : $scope.unitId,
										  orgId : $scope.orgId,
										  file : {}
										}
						 
								 $scope.sanctionDocDetailsList.push(sanctionDetails);
						 };
					
						 $scope.removesanctionDetailsForDoc = function(deleteIndex)
							{ 
							   $scope.sanctionDocDetailsList.splice(deleteIndex, 1);
						    }
					
						 $scope.uploadEncounterDoc = function(Obj)
							{
								//$('#uploadModal').modal('hide');
								 var fd = new FormData();
							        fd.append('fundsanctionrequestDetailsId', Obj.fundsanctionrequestDetailsId);
							        fd.append('documentTypeId', Obj.documentTypeId);
							        fd.append('createdBy',Obj.createdBy );
							        fd.append('updatedBy', Obj.updatedBy);
							        fd.append('unitId', Obj.unitId);
							        fd.append('orgId', Obj.orgId);
							        fd.append('file',Obj.file );
							        var URI1 = BASE_URL + ROOT_URL + "/api/billing/fundSanctionRequestDocument";
							        return GenericServiceFileUpload.serviceAction("POST",URI1,fd,{ 'Content-Type': undefined}).then(function(response) 
									{	
										return response;
									});
							}
						 
						 $scope.openImage = function(docobj)
			               { 
								$rootScope.startLoader();
								var href = $rootScope.currentHrefDoccument();
								$.fancybox.open({
									width: 700, // or whatever
									height: 420,
									href:href+docobj.documentPath,
									type: 'iframe'
								});
			          $rootScope.stopLoader();
			         }
						  $scope.uploadSanctionDoc = function()
						    {
							  $scope.finalDocUploadList=[];
							  var count=0;
							      angular.forEach($scope.sanctionDocDetailsList, function(value, key) {  
								  if(value.documentTypeId==undefined || value.documentTypeId=="" || value.documentTypeId==null){
										 growl.warning("Please select document type .",
																	{
																		title : 'warning!'
																	});
														   $rootScope.stopLoader();
										return false;
									}
								  if (angular.equals(value.file, {}) || value.file==undefined) {
											growl.warning('Please select file.',
															{
																title : 'warning!'
															});
											 $rootScope.stopLoader();
											return false;
										} 
									 var ext = value.file.name.split(".").pop().toLowerCase();
										if($.inArray(ext, ["doc","pdf",'docx',"txt"]) == -1) {
											growl.warning('You can only upload *.txt, *.pdf, *.docx, *.doc file',
													{
														title : 'warning!'
													});
											$rootScope.stopLoader();
											return false;
										}
										$scope.finalDocUploadList.push(value);
//										 console.log("final list",$scope.finalDoc);
							});
							 
							 console.log("final doc list",$scope.finalDocUploadList);
							 if($scope.sanctionDocDetailsList.length > $scope.finalDocUploadList.length){
								  return;
							 }else{
								
								 if($scope.sanctionDocDetailsList.length==$scope.finalDocUploadList.length){
									  $scope.count = 0;
									  $rootScope.startLoaderPopup();
									 angular.forEach($scope.finalDocUploadList, function(value, key) 
										{  
											var promise = $scope.uploadEncounterDoc(value);
											promise.then(function(response) 
													{
													  if(response.data.status == "success")
														  {
														    $scope.count++;
														    if($scope.sanctionDocDetailsList.length==$scope.count)
															 {
														    	
															   growl.success("Document Uploaded Successfully.",
																		{
																			title : 'Success!'
																		});
															   $('#commonUploadDocuments').modal('hide');
															   $scope.sanctionDocDetailsList=[];
															   $scope.finalDocUploadList=[];
															   $rootScope.stopLoader();
															   $rootScope.stopLoaderPopup();
//															   $scope.encounterDetailsList = [];
															 }
													      }
													  else 
														{  $scope.sanctionDocDetailsList=[];
														   $scope.finalDocUploadList=[];
															growl.error('Something went wrong.',
																	{
																		title : 'Error!'
																	});
															$rootScope.stopLoader();
															   $rootScope.stopLoaderPopup();
														}
												    });
												
										});
									  }
								/* $rootScope.startLoader();
				            	  var data ={ fundSanctionRequestDocument : $scope.sanctionDocDetailsList };
									var URI = BASE_URL + ROOT_URL
									+ "/api/billing/fundSanctionRequestDocument";
									GenericService.serviceAction("POST", URI,
											data)
									.then(
											function(response) {
												if (response.data.status == 'success') {
													$rootScope.stopLoader();
//											       $scope.getpatientSanctionRequest();
													growl.success("Fund Request saved successfully",
																{
																	title : 'Success!'
																});
										
											} else if (response.data.status == 'error') {
												$scope.initCommon();	
												growl.error(
																response.data.message,
																{
																	title : 'Error!'
																});
												
											}else{
											}
												
												
										});
								 */
								 
							 }
							
						    }
					
					
					
				}
						
]);