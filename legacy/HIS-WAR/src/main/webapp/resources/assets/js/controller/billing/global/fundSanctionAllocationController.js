(function () {
  "use strict";
					function fundSanctionAllocationController($scope, $http, $localStorage, $sessionStorage,
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
						//patientsearchObj.birthDate = moment(patientsearchObj.birthDate).format('YYYY-MM-DD');
						if(patientsearchObj.patientName!="" && patientsearchObj.patientName.length < 3)
						{
							growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
							$scope.patientList = [];
							return false;
						}	
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
					$scope.sanctionApprovalObj={};
					$scope.getSanctionApprovePopup = function(obj){
						
						$scope.closeSanctionApprovePopup();
						
							$scope.sanctionApprovalObj={
								      fundsanctionrequestDetailsId : obj.fundsanctionrequestDetailsId,
									  patientId : $scope.selectedPatientId,
									  trustId : obj.trustId,
									  fundrequestNumber : obj.fundrequestNumber,
									  fundrequestAmount : obj.fundrequestAmount,
									  fundrequestDate:obj.fundrequestDate,
									  trustName:obj.trustName,
									  fundrequestStatusId : "",
									  requestApprovalAmount :"",
									  fundAcceptacepaymentTypeId:"",
									  fundInFlowDetailsDto : [],
									  status : 'A',
									  createdDate : "",
									  createdBy : $scope.userId,
									  updatedDate : "",
									  updatedBy : $scope.userId,
									  unitId : $scope.unitId,
									  orgId : $scope.orgId
							};
							
							$scope.getTrustFundList();
//							data-toggle="modal" data-target="#approval_modal"
							$('#approval_modal').modal('show');
					}
					
					
					
					$scope.setFundrequestStatusId=function(sanctionstatus,sanctionApprovalObj){
						if(sanctionstatus==2){
							$('#acceptancerow1').css('display','block');
//							$('#acceptancerow2').modal('show');
							$('#acceptancerow3').css('display','block');
							$('#useExistingFundRadiobtn').prop("checked", false);
							$('#virtualRadiobtn').prop("checked", false);
							
							$scope.sanctionApprovalObj.requestApprovalAmount =$scope.sanctionApprovalObj.fundrequestAmount;
							
						}
						
						if(sanctionstatus==3){
							$('#acceptancerow1').css('display','none');
							$('#acceptancerow2').css('display','none');
							$('#acceptancerow3').css('display','none');
							
							$('#rejectConformation').modal('show');
						}
					};
					
					
					$scope.setFundPaymentId=function(id){
						if(id==1){
							$scope.sanctionApprovalObj.fundAcceptacepaymentTypeId=id;	
							$('#acceptancerow2').css('display','block');
						}else if(id==2){
							$('#acceptancerow2').css('display','none');
							$scope.sanctionApprovalObj.fundAcceptacepaymentTypeId=2;
		            	  		 angular.forEach($scope.inflowtrustlist, function(value, key) { 
								   		value.tinflowflagforCheck=false;
								   		value.tinflowamt="";
								   	 });
						}
						$scope.sanctionApprovalObj.fundrequestStatusId=2;
					}
					
					$scope.rejectSacntionRequest= function (confirm){
						if(confirm=='Y'){
							//rejected
							$scope.sanctionApprovalObj.fundrequestStatusId=3;
							 $scope.rejectApprovalRequest();
							 $('#rejectConformation').modal('hide');
							 $('#approval_modal').modal('hide');
							 
						}else{
							
						}
						
						
						
					}
					$scope.closeSanctionApprovePopup =function (){
						
						$('#acceptancerow1').css('display','none');
						$('#acceptancerow2').css('display','none');
						$('#acceptancerow3').css('display','none');
						$('#acceptRadio').prop("checked", false);
						$('#rejectRadio').prop("checked", false);
						$('#useExistingFundRadiobtn').prop("checked", false);
						$('#virtualRadiobtn').prop("checked", false);
						
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
						
						 
						 $scope.inflowtrustlist = [];
							 $scope.getTrustFundList=function(){
				              try {
				            	  var data1={
				            			  trustId : $scope.sanctionApprovalObj.trustId,
				            			  orgId :     $scope.orgId,
										  unitId :    $scope.unitId
				            			  };
				            	  $rootScope.startLoaderPopup();
									var URI = BASE_URL + ROOT_URL
									+ "/api/billing/getfundInflowDetailsByTrust";
									GenericService.serviceAction("POST", URI,
											data1)
									.then(
											function(response) {
												if (response.data.status == 'success') {
													$rootScope.stopLoaderPopup();
													 $scope.inflowtrustlist = [];
											   	    $scope.inflowtrustlist = response.data.listObject;	
											   	    angular.forEach($scope.inflowtrustlist, function(value, key) { 
												   		value.tinflowflagforCheck=false;
												   		value.tinflowamt="";
												   	 });
											   	    
											   	    
											} else if (response.data.status == 'error') {
												 $rootScope.stopLoaderPopup();
												growl.error(
																response.data.message,
																{
																	title : 'Error!'
																});
												
											}
										});
								} catch (e) {
									console.log("Exception", e.message);
									 $rootScope.stopLoaderPopup();
									growl.error(
											response.data.message,
											{
												title : 'Error!'
											});
								}

				       }
							 $scope.finalAmount=0;
							 $scope.calculatetrusselectedAmt =function (){
								 var countblur=0;
								 $scope.finalAmount=0;
								 angular.forEach($scope.inflowtrustlist, function(value, key) { 
								 if(value.tinflowflagforCheck==true && value.tinflowamt!=undefined && value.tinflowamt!=="" ){
							   			if(value.availableAmount < value.tinflowamt){
							   				countblur++;
							   			}else{
							   				var amt=0;
							   				amt=parseFloat(value.tinflowamt);
							   				$scope.finalAmount=(parseFloat($scope.finalAmount)+ amt);
							   			}
							   			
							   			
							   		}
								 });
								 if(countblur>0){
									 growl.warning("Amount Should not be greater than Available Trust amount ",
												{
													title : 'warning!'
												});
										return;
								 }
							 }
							 
							 
							 
							 $scope.selectedTrustAmt =function (index){
								 
								 //=============================
								 
								 var countblur=0;
								 var approvedamt=$scope.sanctionApprovalObj.requestApprovalAmount;
								 var trustselectedamt=0;
								 
								 
								 angular.forEach($scope.inflowtrustlist, function(value, key) { 
									 if(key==index  ){
										 value.tinflowamt="";
									 }
									 if(value.tinflowflagforCheck==true &&value.tinflowamt!="" ){
											  trustselectedamt=parseFloat(trustselectedamt )+ parseFloat(value.tinflowamt);
									  }
								});
								 
								 
								 angular.forEach($scope.inflowtrustlist, function(value, key) { 
									 if(key==index ){
										 if(value.tinflowflagforCheck==true && trustselectedamt==0){
											       if(value.availableAmount > approvedamt){
											    	   value.tinflowamt=approvedamt;
											       }else{
											    	   value.tinflowamt=value.availableAmount;
											       }
											       if(value.fundtypeId==3){
											    	   growl.warning("This is Diagnosis Fundinflow ",
																{
																	title : 'warning!'
																});
											       }
											       
											       
										 }
										 if(value.tinflowflagforCheck==true && trustselectedamt!=0){
											 if( (value.availableAmount + trustselectedamt) <  approvedamt){
												 value.tinflowamt=value.availableAmount ;
												 trustselectedamt= trustselectedamt + value.tinflowamt;
											 }
											 else {
												  var diff=0;
												  diff= parseFloat(approvedamt) - parseFloat( trustselectedamt);
												if(diff > 0){
												  value.tinflowamt=diff;
												  trustselectedamt=trustselectedamt + value.tinflowamt;
												  
											     }else{
											    	 growl.warning("Approval Amount Should not be greater than selected Trust amount ",
																{
																	title : 'warning!'
																});
											    	 value.tinflowamt="";
											    	 value.tinflowflagforCheck=false;
											     }
												
												if(value.fundtypeId==3){
											    	   growl.warning("This is Diagnosis Fund inflow ",
																{
																	title : 'warning!'
																});
											       }
											 }
											 
											 
										 }
										 
										 
									  }
								});
								 
								 
								 $scope.calculatetrusselectedAmt();
							 }
							 
							 $scope.saveApprovalRequest=function(){
					              try {
					            	/*  var data1={
					            			  trustId : $scope.sanctionApprovalObj.trustId,
					            			  orgId :     $scope.orgId,
											  unitId :    $scope.unitId
					            			  };*/
					            	  
					            	  if($scope.sanctionApprovalObj.patientId=="" || $scope.sanctionApprovalObj.patientId==0 ) 
										{
											growl.warning("Please select Patient ",
													{
														title : 'warning!'
													});
											return;
										}else if(  $scope.sanctionApprovalObj.fundAcceptacepaymentTypeId==undefined || $scope.sanctionApprovalObj.fundAcceptacepaymentTypeId==""){
											growl.warning("Please  select Request Acceptance Type  ",
													{
														title : 'warning!'
													});
											
											return;
										}else if( $scope.sanctionApprovalObj.requestApprovalAmount==undefined ||  $scope.sanctionApprovalObj.requestApprovalAmount==""){
											growl.warning("Please enter Approval Amount ",
													{
														title : 'warning!'
													});
											return;
										}else if( $scope.sanctionApprovalObj.requestApprovalAmount >  $scope.sanctionApprovalObj.fundrequestAmount){
											growl.warning("Please enter valid Approval Amount ",
													{
														title : 'warning!'
													});
											return;
										}
					            	  
					            	  if($scope.sanctionApprovalObj.fundAcceptacepaymentTypeId==1){
					            		  $scope.inflowtrustlistchecklist=[];
					            	      var checklistamt=parseFloat(0);
					            	      var count=0;
					            	      var zeroamtenterd=0;
					            	      angular.forEach($scope.inflowtrustlist, function(value, key) { 
					            	    	  
										   		if(value.tinflowflagforCheck==true && value.tinflowamt!=undefined && value.tinflowamt!=="" && value.tinflowamt > 0 ){
										   			
										   			if(value.availableAmount < value.tinflowamt){
										   				count++;
										   			}else{
										   				$scope.inflowtrustlistchecklist.push(value);
										   			}
										   			
										   			
										   			
										   			var amt=0;
										   				amt=parseFloat(value.tinflowamt);
										   			checklistamt=(checklistamt + amt);
										   		}
										   		
										   		
										   		if(value.tinflowflagforCheck==true &&   ( value.tinflowamt==undefined || value.tinflowamt=="" || value.tinflowamt == 0 ) ){
										   			zeroamtenterd++;
										   		}
										   		
										   		
										   	 });
					            	      if(count>0){
					            	    	  growl.warning("Amount Should not be greater than Available Trust amount ",
														{
															title : 'warning!'
														});
												return;
					            	      }
					            	      
					            	      if(zeroamtenterd>0){
					            	    	  growl.warning("Amount selected Should not be blank or Zero  ",
														{
															title : 'warning!'
														});
												return;
					            	      }
					            	      
					            	      if(parseFloat(checklistamt)==0 ){
					            	    	  growl.warning("Please select At Least one Fund Inflow ",
														{
															title : 'warning!'
														});
												return;
					            	      }
					            	      
					            	      if(parseFloat($scope.sanctionApprovalObj.requestApprovalAmount) < parseFloat(checklistamt)){
					            	    	  growl.warning("Please enter valid Trust selected Approval Amount ",
														{
															title : 'warning!'
														});
												return;
					            	      }
					            	  }
					            	  	
					            	  if($scope.sanctionApprovalObj.fundAcceptacepaymentTypeId==1){
					            		 
					            		  
					            		  angular.forEach( $scope.inflowtrustlistchecklist, function(value, key) { 
					            			  var acceptdata={};
					            			  acceptdata.fundInFlowId=  value.fundInFlowId;
					            			  acceptdata.trustAcceptedAmount=parseFloat(value.tinflowamt);
					            			  acceptdata.availableAmount=parseFloat(value.availableAmount);
					            			  $scope.sanctionApprovalObj.fundInFlowDetailsDto.push(acceptdata);
					            		  });
					            	  }
					            	  
					            	  $rootScope.startLoaderPopup();
										var URI = BASE_URL + ROOT_URL
										+ "/api/billing/saveApprovalRequest";
										
										GenericService.serviceAction("POST", URI,
												 $scope.sanctionApprovalObj)
										.then(
												function(response) {
													if (response.data.status == 'success') {
														$rootScope.stopLoaderPopup();
														growl.success("Fund Request Accepted successfully",
																{
																	title : 'Success!'
																});
														$('#approval_modal').modal('hide');
														$scope.getpatientSanctionRequest();
											
												} else if (response.data.status == 'error') {
													 $rootScope.stopLoaderPopup();
													growl.error(
																	response.data.message,
																	{
																		title : 'Error!'
																	});
													$('#approval_modal').modal('hide');
													$scope.getpatientSanctionRequest();
												}
											});
									} catch (e) {
										console.log("Exception", e.message);
										 $rootScope.stopLoaderPopup();
										growl.error(
												response.data.message,
												{
													title : 'Error!'
												});
									}

					       }
								 
							 //reject
							 $scope.rejectApprovalRequest=function(){
					              try {
					            	  if($scope.sanctionApprovalObj.patientId=="" || $scope.sanctionApprovalObj.patientId==0 ) 
										{
											growl.warning("Please select Patient ",
													{
														title : 'warning!'
													});
											return;
										}
					            	  $rootScope.startLoaderPopup();
										var URI = BASE_URL + ROOT_URL
										+ "/api/billing/saveApprovalRequest";
										
										GenericService.serviceAction("POST", URI,
												 $scope.sanctionApprovalObj)
										.then(
												function(response) {
													if (response.data.status == 'success') {
														$rootScope.stopLoaderPopup();
														growl.success("Fund Request Rejected successfully",
																{
																	title : 'Success!'
																});
														$scope.getpatientSanctionRequest();
											
												} else if (response.data.status == 'error') {
													 $rootScope.stopLoaderPopup();
													 $scope.getpatientSanctionRequest();
													growl.error(
																	response.data.message,
																	{
																		title : 'Error!'
																	});
												}
											});
									} catch (e) {
										console.log("Exception", e.message);
										 $rootScope.stopLoaderPopup();
										growl.error(
												response.data.message,
												{
													title : 'Error!'
												});
									}

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
							 }
						    }
						
					
	}
function config($stateProvider) {
						    $stateProvider.state('sanctionAllocationApproval', {
								 url: '/sanctionAllocationApproval',
								 templateUrl: 'views/master/UnitMaster/fundManagement/saction-allocation-approval-form.html',
								 controller: "fundSanctionAllocationController",
								 title: 'Sanction Allocation Approval',
							})
						  }

						  angular
						    .module("myApp")
						    .config(config)
						    .controller("fundSanctionAllocationController", fundSanctionAllocationController);
						})();