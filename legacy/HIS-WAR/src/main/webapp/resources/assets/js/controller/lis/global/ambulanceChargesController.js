/**
 * @author  Tushar
 */

angular.module('myApp').controller(
		"ambulanceChargesController",
		[ '$scope','$http','$localStorage','$sessionStorage','$cookies','$rootScope','$state',
			'GenericService','BillingGenericService','growl','PagerService',
			function($scope, $http, $localStorage, $sessionStorage, $cookies, $rootScope,$state,
						GenericService, BillingGenericService, growl, PagerService) {
			
			$scope.init = function() {
						
						var cookieObject = $cookies.getObject('cookieObject');
						if(cookieObject == undefined){
							$state.go('login');
							return;
						}
						
						var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
						if (cookieObjectAppConfigSecond != undefined) {
						$scope.serviceId = cookieObjectAppConfigSecond.ambulanceChargesServiceId;
						}
						$scope.unitId = cookieObject.unitId;
						$scope.organizationId = cookieObject.organizationId;
						$scope.userId = cookieObject.userId;

						/** Variables for pagination */
						$scope.offset = 0;
						$scope.noOfRecordsPerPage = 10;
						$scope.commonList = [];
						$scope.totalNoOfRecords;
						$scope.searchKeyword;
						$scope.commonListCount;
						$scope.ambulanceChargesList = [];
						$scope.ambulanceChargesAList = [];
						$scope.totalNoOfRecords;
						$scope.searchKeyword;
						$scope.ambulanceChargesListCount;
						$scope.patientSearchObj.uhidNumber ='';
			            $scope.patientSearchObj.mobileNumber ='';
			            $scope.patientSearchObj.identificationNumber ='';
			            $scope.patientSearchObj.patientName = '' ;
						$rootScope.loginpage = true;
						$scope.ambulanceCharges = {}
						$scope.saveBtnFlag = true;
						$scope.updateBtnFlag = false;

						$scope.showModal = false;
						$scope.fieldValidate = false;
						$scope.buttonClicked = "";
						$scope.popUpFlag = true;
					
						var data = {};
						var data1 = {	organizationId : $scope.organizationId };
						
						var URI1 = BASE_URL + ROOT_URL+ GETACTIVEGENDERLIST;
						BillingGenericService.serviceAction(METHOD_GET, URI1, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
										console.log(response);
										$scope.genderList = [];
										if (response.data.status == "success")
											$scope.genderList = response.data.listObject;
									});
						
						var URI2 = BASE_URL + ROOT_URL + GETACTIVEIDENTIFICATION;
						BillingGenericService.serviceAction(METHOD_GET, URI2, data,NOTIFICATION_MSG_STATUS_FALSE).then(
								function(response) {
									console.log(response);
									$scope.identificationList = [];
									if (response.data.status == "success")
										$scope.identificationList = response.data.listObject;
								});
						
						
						var URI3 = BASE_URL + ROOT_URL + GETAMBULANCETARRIFACTIVELIST;
						GenericService.serviceAction("POST", URI3, data1).then(
								function(response) {
									$scope.ambulanceTypeList = [];
									if (response.data.status == "success") {
										$scope.ambulanceTypeList = response.data.listObject;
										console.log("ambulanceTypeList ",$scope.ambulanceTypeList);
									}
								});
				
						$rootScope.searchPatient = function(patientsearchObj){
							$scope.visitNumber ='';
							$scope.ambulanceCharges.fromAddress ='';
							$scope.ambulanceCharges.toAddress ='';
							$scope.ambulanceCharges.ambulanceType ='';
							$scope.ambulanceCharges.distance ='';
							$scope.ambulanceCharges.ambulanceProvider ='';
							$scope.ambulanceCharges.outSourceVenderName ='';
							$scope.ambulanceCharges.vehicleNo ='';
							if($scope.patientSearchObj.uhidNumber == '' && $scope.patientSearchObj.patientName == '' && $scope.patientSearchObj.genderId == '' && $scope.patientSearchObj.mobileNumber == '' && $('#datepickerSearch').val() == '' && ( patientsearchObj.identificationTypeId == "" || patientsearchObj.identificationTypeId == undefined ) && $scope.patientSearchObj.identificationNumber == '')
							{
								growl.warning(SearchFilter_REQUIRED,{title : "WARNING!"
								});
								return false;
							}
								
							else{
									//alert("else AlERT");
								}
							if($scope.patientSearchObj.patientName!="" && $scope.patientSearchObj.patientName.length < 3)
							{
								growl.warning("Please enter atleast 3 characters..!",{title : "WARNING!"});
								$scope.patientList = [];
								return false;
							}	
							$rootScope.startLoader();
							
						    if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
						    	$scope.patientSearchObj.birthDate = moment($scope.patientSearchObj.birthDate).format('YYYY-MM-DD');
					        }	
							$scope.patientSearchObj.unitId=$scope.unitId;
			                $scope.patientSearchObj.organizationId=$scope.organizationId;
			                $scope.patientSearchObj.uhIdNumber=$scope.patientSearchObj.uhidNumber!=undefined ? $scope.patientSearchObj.uhidNumber : "";
			                $scope.patientSearchObj.mobileNo=$scope.patientSearchObj.mobileNumber;
			                $scope.patientSearchObj.identificationNo=$scope.patientSearchObj.identificationNumber;

							$rootScope.startLoader();
							var URI = BASE_URL + ROOT_URL + "/adt/patientSearchByMultipleCriteria";
							var patSearchObj = angular.copy($scope.patientSearchObj);
							patSearchObj.patientType = 1;
							
							/*if($scope.patientSearchObj.birthDate != "" && typeof($scope.patientSearchObj.birthDate) != "undefined"){
								var dt = new Date($scope.patientSearchObj.birthDate);
								patSearchObj.birthDate = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + dt.getFullYear();
							} */
							BillingGenericService.serviceAction(METHOD_POST,URI, patSearchObj,NOTIFICATION_MSG_STATUS_FALSE).
								then(function(response) {
									$scope.searchPatientList1 = [];
									if (response.data.status == "success")
										$scope.searchPatientList1 = response.data.listObject;
									
									if(response.data.listObject==null){
										growl.warning(response.data.message,{
											title : ERROR_MSG
										});
										$scope.patientSearchObj.birthDate="";
										$scope.searchPatientList1=[];
										return false;
									}
									console.log("$scope.searchPatientList1 " ,$scope.searchPatientList1);
									if($scope.searchPatientList1.length == 0){
										growl.warning("No Result Found!",{	title : ERROR_MSG	});
									}
									$scope.patientSearchObj.birthDate="";
									patSearchObj = {};
									
									$scope.saveBtnFlag = true;
									$scope.updateBtnFlag = false;
								});
						}
						
						// For Paginations Start
						$scope.setNoOfRecords = function() {
							$scope.initAmbulanceChargesList(
									$scope.organizationId, $scope.offset,
									$scope.noOfRecordsPerPage);
						};
						
			}
			$scope.init();
					
			/* code for setting Label */
			$(".selectedPageName").text("Ambulance Master");
			
			//for datepicker Search
			$scope.dateOptions = {
				formatYear : 'yyyy',
				showWeeks : false
			};
			
			$scope.openDatePicker = function() {
				$scope.datepicker.opened = true;};
			$scope.datepicker = {
				opened : false};

			$scope.openDatePickerSearch = function() {
				$scope.datepickerSearch.opened = true;};
			$scope.datepickerSearch = {
				opened : false
			};
			
			// For Paginations Start
			$scope.setNoOfRecords = function() {
				$scope.initAmbulanceChargesList($scope.organizationId, $scope.offset,$scope.noOfRecordsPerPage);
			};
				$scope.initAmbulanceChargesList = function(orgId, offset, noOfRecordsPerPage) {
					offset = offset != null ? offset : 0;
					noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;

					var data3 = {
						organizationId : $scope.organizationId,
						offset : offset,
						noOfRecordsPerPage : noOfRecordsPerPage
					};

					// For Get Ambulance Charges List Using Offset
					var URIACL = BASE_URL + ROOT_URL + GETAMBULANCECHARGESLIST;
					GenericService.serviceAction("POST", URIACL, data3)
							.then(function(response) {
									$scope.ambulanceChargesList = [];
									if (response.data.status == "success") {
										$scope.ambulanceChargesList = response.data.listObject;
										$scope.commonListCount = response.data.object;
										$scope.setPage(1,false);
									}
								});
					
					/* var URI = BASE_URL + ROOT_URL + GETAMBULANCEMASTERDATALIST;
					 // For Count Ambulance List
					GenericService.serviceAction("POST", URI1, data1).then(
						function(response) {
							if (response.data.status == "success") {
								console.log(response.data);
								$scope.ambulanceMasterListCount = response.data.object;
								$scope.setPage(1,false);

							}
						});*/
				}
				
				
			    $scope.selectPatientById = function (patient) {
			        $rootScope.startLoader();
			        $scope.visitNumbers = [];
			        
			        $scope.encounterId = null;
			        $scope.admissionId = null;
			        $scope.visitTypeId =null;
			        $scope.visitNumber ='';
					$scope.ambulanceCharges.fromAddress ='';
					$scope.ambulanceCharges.toAddress ='';
					$scope.ambulanceCharges.ambulanceType ='';
					$scope.ambulanceCharges.distance ='';
					$scope.ambulanceCharges.ambulanceProvider ='';
					$scope.ambulanceCharges.outSourceVenderName ='';
					$scope.ambulanceCharges.vehicleNo ='';
			        if(patient.admissionId!=undefined && patient.admissionId!=null && patient.admissionId>0)
			  	    {
			  	  	  $scope.admissionId = patient.admissionId;
			  	  	  $scope.encounterId = null;
			  	  	  $scope.visitTypeId = patient.visitTypeId;
			  	  	  $scope.getVisitNo($scope.admissionId,0);
			  	/*	    growl.warning("Patient is admitted..!!",{
			  				  title : WARNING_MSG
			  		    });
			  	  	  var URI = BASE_URL + ROOT_URL + "/api/billing/getBillData/admission/"+$scope.admissionId;
			          BillingGenericService.serviceAction(METHOD_GET,URI,null,NOTIFICATION_MSG_STATUS_FALSE).then(function(response) {
			  					$scope.billNumber =response.data.billNumber;
			  			  });*/
			  	    } else if(patient.encounterId!=null && patient.encounterId>0){
			      	    $scope.encounterId = patient.encounterId;
			      	    $scope.visitTypeId = 1;
			      	    $scope.admissionId = null
			      	    $scope.getVisitNo(0,$scope.encounterId);
			      	    
			      	}else{
			      		growl.warning("Patient not admitted.");
			      		$rootScope.stopLoader();
			      	}
			        	
			      }

			    $scope.getVisitNo = function(admisisonId,encounterId)
			    {
			 	    if(encounterId>0){
			 	    	var URI = BASE_URL + ROOT_URL + "/api/opd/encounters/walkinSales/orgId/"+$scope.organizationId+"/unitId/"+$scope.unitId+"/patientId/"+$scope.ambulanceCharges.selectedPatientId;
			 			BillingGenericService.serviceAction(METHOD_GET,URI, {}, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			 				if(response.data.status == "success"){
			 					$scope.patientEncounterList = [];
			 					$scope.patientEncounterList = response.data.listObject;
			 					$scope.visitNumbers = [];
			 					if($scope.patientEncounterList!=null && $scope.patientEncounterList.length>0){
			 						angular.forEach($scope.patientEncounterList, function(value, key) {
			 							$scope.visitNumbers.push(value.encounterNumber+""+"|"+""+value.encounterDateString);
			             });
			 						$scope.visitNumber = $scope.patientEncounterList[0].encounterNumber;
			 						$scope.visitNumber = $scope.visitNumbers.find(function (obj) {
			 					          return obj;
			 					        });
			 				   }
			 				}
			 			});
			 	    }
			 	    else if(admisisonId>0){
			 	    	var URI = BASE_URL + ROOT_URL + "/api/ipd/admissions/patient/"+$scope.admissionId;
			 			BillingGenericService.serviceAction(METHOD_GET,URI, null, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
			 				if(response.data.status == "success"){
			 					$scope.visitNumbers = [];
			 					$scope.admissionDetails = response.data.object;
			 					if($scope.admissionDetails!=null){
			 						$scope.commonDoctorId=$scope.admissionDetails.doctorId;
			 						$scope.visitNumbers.push($scope.admissionDetails.admissionNumber);
			 						$scope.visitNumber = $scope.admissionDetails.admissionNumber;
			 				     }
			 				   }
			 				});
			 	    }
			 		
			    }
				
			    $scope.visitDoctorName = function(){
			        angular.forEach($scope.patientEncounterList, function(value, key) {
				          if(self.model.visitNumber.split("|")[0] == value.encounterNumber){
					            if($scope.encounterId != null && $scope.encounterId >0 ){
					              	$scope.encounterId = value.encounterId;
					              }
				          }
			        });
			      }
			    
				
				$scope.masterList = function(organizationId,offset, noOfRecordsPerPage,unitId) {
					$rootScope.startLoader();
					console.log("offset", offset);
					offset = offset != null ? offset : 0;
					noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
					
					var data = {
							organizationId:organizationId,
							offset:offset,
							noOfRecordsPerPage:noOfRecordsPerPage,
							};
					
					var URIACL = BASE_URL + ROOT_URL + GETAMBULANCECHARGESLIST;
					GenericService.serviceAction("POST", URIACL, data)
							.then(function(response) {
									$scope.ambulanceChargesList = [];
									if (response.data.status == "success") {
										$scope.ambulanceChargesList = response.data.listObject;
										$scope.commonListCount = response.data.object;
										$rootScope.stopLoader();
									}
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
						$scope.masterList($scope.organizationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
					}
				}
				
				$scope.initAmbulanceChargesList($scope.organizationId, $scope.offset, $scope.noOfRecordsPerPage);
				
				var URI4 = BASE_URL + ROOT_URL	+ GETSINGLEAMBULANCETARRIF;
				// setting values onchange event of AmbulanceType
				$scope.setAmbTypeDetails = function(){
					var data2 = {
						ambulanceTarrifMasterId : $scope.ambulanceCharges.ambulanceType,
						organizationId : $scope.organizationId
					};
					GenericService.serviceAction("POST", URI4, data2)
					.then(function(response) {
						if (response.data.status == "success") {
							$scope.ambulanceCharges.outSourceVenderName = response.data.listObject[0].ambulanceOutSrcVender;
							if(response.data.listObject[0].ambulanceProvider == 'In House' || response.data.listObject[0].ambulanceProvider == 'inHouse')
							{
								$scope.ambulanceCharges.ambulanceProvider ='inHouse';
								$("#outSourceVenderDiv").hide();
								$("#vechileNoDiv").show();
								$("#LPOButtonDiv").hide();
							}
							else if(response.data.listObject[0].ambulanceProvider == "Out Sourced" || response.data.listObject[0].ambulanceProvider == "outSource")
							{
								$scope.ambulanceCharges.ambulanceProvider ='outSource';
								$scope.getAssignValues();
								$("#LPOButtonDiv").show();
								$("#outSourceVenderDiv").show();
								$("#vechileNoDiv").hide();
							}
							else
							{
								$scope.ambulanceCharges.ambulanceProvider ='';
							}
						}
					});	
				}
				
				// setting LPO values on click (when outsource)
				$scope.getAssignValues = function(){
					var ambTypeId = 0;
					$scope.ambulanceCharges.lpoDistance = $scope.ambulanceCharges.distance;						
                	$scope.ambulanceCharges.lpoFromLocation = $scope.ambulanceCharges.fromAddress;
                	$scope.ambulanceCharges.lpoToLocation = $scope.ambulanceCharges.toAddress;
                	$scope.ambulanceCharges.lpoVenderName = $scope.ambulanceCharges.outSourceVenderName;
                	$scope.ambulanceCharges.lpoAmbulanceProvider = $scope.ambulanceCharges.ambulanceProvider;
                	ambTypeId = $scope.ambulanceCharges.ambulanceType;
                	$scope.calculateDistance(ambTypeId);  	
				}
					
				// function to calculate the the fare according to distance 	
				
				$scope.changeOrgDistance = function(){
					$scope.ambulanceCharges.distance = $scope.ambulanceCharges.lpoDistance ;
				}
				
				
				$scope.calculateDistance = function (ambTypeId){
				
			  	var kms = Number(0);
            	kms =  Number($scope.ambulanceCharges.ambulanceProvider == 'outSource' ? $scope.ambulanceCharges.lpoDistance : $scope.ambulanceCharges.distance) ;
        		var data2 = {
						ambulanceTarrifMasterId : ambTypeId,
						organizationId : $scope.organizationId
					};
					GenericService.serviceAction("POST", URI4, data2)
					.then(function(response) {
							if (response.data.status == "success") {
								var firstDistance =  Number(response.data.listObject[0].ambulanceFirstDist);
								var secondDistance = Number(response.data.listObject[0].ambulanceAddMileage);
								var initialFare = Number(response.data.listObject[0].ambulanceFirstDistRate);
								var afterFare = Number(response.data.listObject[0].ambulanceAddMileageRate);
								var range = secondDistance - firstDistance;
								var fareDiff = afterFare - initialFare;
								var finalFare = 0;
								if(kms<=firstDistance){
										finalFare= initialFare;
								}else if(kms > secondDistance){
										var leftKm = kms - firstDistance;
										var inAdditionalDist = leftKm/secondDistance;
										finalFare= initialFare;
										finalFare = finalFare +(inAdditionalDist*afterFare) ;
								}
							$scope.ambulanceCharges.lpoAmount= finalFare;
							} else {window.alert("Error!!");
						}
					});
				}	
					
				// function for popup ok button call
				$scope.PopupOkBtn = function() {
					$scope.popUpFlag = false;
					$scope.saveBtnFlag = true;
					$scope.updateBtnFlag = false;
					$scope.saveAmbulanceCharges();
				}
					
				$scope.saveAmbulanceCharges = function() {
					
					if($scope.serviceId == undefined || $scope.serviceId == null ){
						growl.warning("Ambulance service is not configured at application configuration",{
							title : "warning"
						});
						return false;
					}
					
					$scope.currentDate = new Date();
					$scope.currentDate = moment($scope.currentDate).format('DD-MM-YYYY HH:mm:ss');
					$scope.$broadcast('show-errors-check-validity');
					
					$scope.chargesFormValidation();
					
					if($scope.fieldValidate == false)
						return false;
					
					var dataToSave = {
						patientId : $scope.ambulanceCharges.selectedPatientId ,
						ambulanceType : $scope.ambulanceCharges.ambulanceType ,
						distance : $scope.ambulanceCharges.distance ,
						ambulanceProvider : $scope.ambulanceCharges.ambulanceProvider ,
						fromAddress : $scope.ambulanceCharges.fromAddress ,
						toAddress : $scope.ambulanceCharges.toAddress ,
						lpoFromLocation : $scope.ambulanceCharges.lpoFromLocation ,
						lpoToLocation : $scope.ambulanceCharges.lpoToLocation ,
						lpoNo : $scope.ambulanceCharges.lpoNo ,
						lpoDate : $scope.ambulanceCharges.lpoDate ,
						lpoVenderName : $scope.ambulanceCharges.lpoVenderName ,
						lpoVenderAddress : $scope.ambulanceCharges.lpoVenderAddress ,
						lpoAmbulanceProvider : $scope.ambulanceCharges.ambulanceProvider ,
						lpoDistance : $scope.ambulanceCharges.lpoDistance ,
						lpoAmount : $scope.ambulanceCharges.lpoAmount ,
						outSourceVenderName : $scope.ambulanceCharges.lpoVenderName ,
						vehicleNo : $scope.ambulanceCharges.vehicleNo ,
						status : 'A',
						createdBy : $scope.userId,
						updatedBy : $scope.userId,
						organizationId : $scope.organizationId,
						unitId : $scope.unitId,
						createdDate : $scope.currentDate,
						updatedDate : $scope.currentDate,
						serviceId : $scope.serviceId,
						admissionId : $scope.admissionId,
						encounterId : $scope.encounterId,
					};
			
					/*if ($scope.ambulanceChargesForm.$valid) {
						window.alert("2");*/
						if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;
						} else {
							console.log(dataToSave);
							var URIS = BASE_URL + ROOT_URL + ADDAMBULANCECHARGES;
							GenericService.serviceAction("POST", URIS,dataToSave)
									.then(function(response) {
											if (response.data.status == "success") {
												growl.success('Ambulance Charges Added Successfully...!!',{title : 'Success!'});
												$scope.$broadcast('show-errors-reset');
												$scope.init();
											} else {
												growl.error('Ambulance Charges already exist...!!',{title : 'Error!'});
											}
											$scope.initAmbulanceChargesList(
															$scope.organizationId,
															$scope.offset,
															$scope.noOfRecordsPerPage);
										});
						}
					//}
						$scope.fieldValidate = false;
				}
				
				
				
				$scope.orderCancel = function(orderDetailsId) {
					
					
					var data2 = {
							orderDetailsId : orderDetailsId,
						}
					
				var URISI = BASE_URL + ROOT_URL + "/adt/cancelOrder";
				console.log("URI", URISI);
				GenericService.serviceAction("POST", URISI, data2)
					.then(function(response) {
								if (response.data.status == "success") {
									growl.success('Ambulance Charges Cancel Successfully...!!',{title : 'Success!'});
									$scope.init();
									$scope.initAmbulanceChargesList(
											$scope.organizationId,
											$scope.offset,
											$scope.noOfRecordsPerPage);
								}
							});
				}
				
				
				
					
					$scope.getAmbChargesDetailsById = function(id) {
						var data2 = {
								ambulanceChargesId : id,
								organizationId : $scope.organizationId
							}
						
					var URISI = BASE_URL + ROOT_URL + GETSINGLEAMBULANCECHARGES;
					console.log("URI", URISI);
					GenericService.serviceAction("POST", URISI, data2)
						.then(function(response) {
							var ambulanceChargesObj  = {};
									if (response.data.status == "success") {
										ambulanceChargesObj = response.data.listObject[0];
											console.log("ambulanceChargesObj.",ambulanceChargesObj);
										$scope.ambulanceCharges.selectedPatientId = ambulanceChargesObj.patientId;
										$scope.ambulanceCharges.ambulanceType = ambulanceChargesObj.ambulanceType.toString();
										$scope.ambulanceCharges.distance = ambulanceChargesObj.distance;
										$scope.ambulanceCharges.fromAddress  = ambulanceChargesObj.fromAddress ;
										$scope.ambulanceCharges.toAddress  = ambulanceChargesObj.toAddress;
										$scope.ambulanceCharges.ambulanceProvider = ambulanceChargesObj.ambulanceProvider;
										
										if(ambulanceChargesObj.lpoAmbulanceProvider=='outSource'){
											
											$("#LPOButtonDiv").show();
											$("#outSourceVenderDiv").show();
											$("#vechileNoDiv").hide();
											
											$scope.ambulanceCharges.outSourceVenderName = ambulanceChargesObj.outSourceVenderName;
											$scope.ambulanceCharges.lpoFromLocation  = ambulanceChargesObj.lpoFromLocation;
											$scope.ambulanceCharges.lpoToLocation  = ambulanceChargesObj.lpoToLocation;
											$scope.ambulanceCharges.lpoNo  = ambulanceChargesObj.lpoNo;
											$scope.ambulanceCharges.lpoDate  = ambulanceChargesObj.lpoDate;
											$scope.ambulanceCharges.lpoVenderName = ambulanceChargesObj.lpoVenderName;
											$scope.ambulanceCharges.lpoVenderAddress = ambulanceChargesObj.lpoVenderAddress;
											$scope.ambulanceCharges.lpoAmbulanceProvider = ambulanceChargesObj.lpoAmbulanceProvider;
											$scope.ambulanceCharges.lpoDistance = ""+ambulanceChargesObj.lpoDistance;
											$scope.ambulanceCharges.lpoVenderName = ambulanceChargesObj.outSourceVenderName;
											$scope.ambulanceCharges.lpoAmount = ambulanceChargesObj.lpoAmount;
											
										}else{
											$scope.ambulanceCharges.vehicleNo = ambulanceChargesObj.vehicleNo ;
										}
										$scope.saveBtnFlag = false;
										$scope.updateBtnFlag = true;
										$scope.ambulanceCharges.ambulanceChargesId= id;
									}
								});
					}
					
					
				
					
					$scope.chargesFormValidation = function (){
						if($scope.ambulanceCharges.selectedPatientId==undefined || $scope.ambulanceCharges.selectedPatientId == null || $scope.ambulanceCharges.selectedPatientId =='' ){
							growl.error("Please select a patient");	document.getElementByName("patientName").focus();	fieldValidate = false;	 return false;
						}
						if($scope.ambulanceCharges.fromAddress==undefined || $scope.ambulanceCharges.fromAddress == null || $scope.ambulanceCharges.fromAddress =='' ){
							growl.error("Please select the source place");	document.getElementByName("fromAddress").focus();	fieldValidate = false;	 return false;
						}
						if($scope.ambulanceCharges.toAddress==undefined || $scope.ambulanceCharges.toAddress == null || $scope.ambulanceCharges.toAddress =='' ){
							growl.error("Please select the destination place");	document.getElementByName("toAddress").focus();	fieldValidate = false;	 return false;
						}
						if($scope.ambulanceCharges.distance==undefined || $scope.ambulanceCharges.distance == null || $scope.ambulanceCharges.distance =='' ){
							growl.error("Please select the distance between source and destination place");	document.getElementByName("distance").focus();	fieldValidate = false;	 return false;
						}
						if($scope.ambulanceCharges.ambulanceType==undefined || $scope.ambulanceCharges.ambulanceType == null || $scope.ambulanceCharges.ambulanceType =='' ){
							growl.error("Please select Ambulance Type");	document.getElementByName("ambulanceType").focus();	fieldValidate = false;	 return false;
						}
						if($scope.ambulanceCharges.ambulanceProvider==undefined || $scope.ambulanceCharges.ambulanceProvider == null || $scope.ambulanceCharges.ambulanceProvider =='' ){
							growl.error("Please select Ambulance Provider ");	document.getElementByName("ambulanceProvider").focus();	fieldValidate = false;	return false;
						}
						$scope.fieldValidate = true;	
					}
					
				} ]);