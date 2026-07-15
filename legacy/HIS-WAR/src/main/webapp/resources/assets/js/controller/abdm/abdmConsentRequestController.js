(function () {
    "use strict";

    function abdmConsentRequestController($scope, $interval ,$rootScope, $http, $cookies, CONSTANTS, GenericService,PaginationService, BillingGenericService, $filter, $state, growl, CurrencyService, commonDetailService, screenDataService, reportService ,$timeout) {
        var self = this;
//alert("hello consent");
        //Default Constructor
        var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
        function initializeController() {
            $rootScope.loginpage = true;
            var date = new Date();
            self.model = {};
        }


		$rootScope.loginpage = true;
		$scope.saveBtnFlag = true;
		$scope.updateBtnFlag = false;
		
		
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
		 
        var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
        $scope.aadhar={
				"myAadharNumber":""
		};
     	
        $scope.setFirstPage = function(){
        	 $scope.createAbhass = false;
        	 $scope.createAbhadiv = true;
        }
        
        $scope.patientIdentifier='';
       $scope.purposeOfRequest = '';
        $scope.healthInfoTypes = {};
        
        $scope.datepickerHealth = new Date();
        
        $scope.openDatePicker = function() {
			$scope.datepicker.opened = true;
		};
		$scope.datepicker = {
				opened : false
			};
        $scope.dateOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				maxDate:new Date()
			};
        
        
        
        
        /*====================================================================*/
        
 $scope.datepickerConsentExpiryHealth = new Date();
        
        $scope.openDatepickerConsentExpiry = function() {
			$scope.datepickerConsentExpiry.opened = true;
		};
		$scope.datepickerConsentExpiry = {
				opened : false
			};
        $scope.dateOptionsConsentExpiry = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate:new Date()
			};
        
        
        
        
        /*====================================================================*/
        
        $scope.datepickerHealthInfo = new Date();
               
               $scope.openDatepickerHealthInfo = function() {
       			$scope.datepickerHinfo.opened = true;
       		};
       		$scope.datepickerHinfo = {
       				opened : false
       			};
               $scope.dateOptionsDatepickerHealthInfo = {
       				formatYear : 'yyyy',
       				showWeeks : false,
       				maxDate:new Date()
       			};
               $scope.datas={};
        
               $scope.datas.fromTime='';
               
               $scope.initClockPicker = function() {
					$('.clockpicker').clockpicker({ autoclose: true});
				}
        
            
            $scope.searchPatient  = function(searchKeyword){
   			if(searchKeyword != undefined && searchKeyword != null && searchKeyword != ""){
   				$scope.patientList=[];
   				searchKeyword=searchKeyword;
   				var serData = {
   						organizationId : $scope.organizationId,
   						unitId : $scope.unitId,
   						searchKeyword : searchKeyword
   				}
   				var URI = BASE_URL + ROOT_URL + "/api/global/patientSearchPatientListForABDM";
   				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
   					if(response.data.status == "success"){
   						$scope.patientList = response.data.listObject;
   					}
   					return $scope.patientList;
   				});
   			}
   				
   			}
            
            $scope.link={};
            
            
            $scope.link.selectedEncounter = ""; 
            $scope.link.selectedAdmission="";
          //  $scope.encounter.encounterNumber= null;
            $scope.searchPatientNew  = function(searchKeyword){
       			if(searchKeyword != undefined && searchKeyword != null && searchKeyword != ""){
       				$scope.patientList=[];
       				searchKeyword=searchKeyword;
       				var serData = {
       						organizationId : $scope.organizationId,
       						unitId : $scope.unitId,
       						searchKeyword : searchKeyword
       				}
       				var URI = BASE_URL + ROOT_URL + "/api/global/patientSearchPatientListForABDM";
       				return BillingGenericService.serviceAction(METHOD_POST,URI, serData, NOTIFICATION_MSG_STATUS_FALSE).then(function(response){
       					if(response.data.status == "success"){
       						$scope.patientList = response.data.listObject;
       					}
       					return $scope.patientList;
       				});
       			}
       				
       			}
            
            
            
            
            $scope.selectedPatient ={};
            $scope.addSelectedPatient  = function(item,item1,item2){
            	 $scope.selectedPatient ={};
            	$scope.selectedPatient =item;
            	$scope.patientId=item.patientId;
   				console.log("item",item);
   			}
            
            $scope.updateDropdowns = function() {
                if ($scope.selectedVisitType === 'OP') {
                    // Fetch encounter list if needed (already hardcoded here for example)
                	$scope.getEncounterNumberByPatientId($scope.patientId);
                	
                } else if ($scope.selectedVisitType === 'IP') {
                    // Fetch admission list if needed (already hardcoded here for example)
                	$scope.getAdmissionNumberByPatientId($scope.patientId);
                }
            };
            
            
                 $scope.BackPage =function(){
                    $scope.mainTab=true;
                    $scope.aadhaarStepper=false;
                    $scope.drivingStepper=false;
                    $scope.usingAadharOP=false;


                 }

                 $scope.hiTypes = [
                     { id: 'OPConsultation', label: 'OP Consultation', checked: false },
                     { id: 'DiagnosticReport', label: 'Diagnostic Reports', checked: false },
                     { id: 'DischargeSummary', label: 'Discharge Summary', checked: false },
                     { id: 'ImmunizationRecord', label: 'Immunization Record', checked: false },
                     { id: 'WellnessRecord', label: 'Wellness Record', checked: false },
                     { id: 'HealthDocumentRecord', label: 'Health Document Record', checked: false },
                     { id: 'Prescription', label: 'Prescription', checked: false},
                     { id: 'Invoice', label: 'Invoice', checked: false}
                     ];
                 
                 $scope.consent={};
                 $scope.consent.expiryTime = '';
     			$scope.requestConsent = function(){
     				
     				 const selectedConsents = $scope.hiTypes.filter(consent => consent.checked);
     			    const selectedLabels = selectedConsents.map(consent => consent.id);
     			    
     			    if($scope.patientIdentifier == undefined || $scope.patientIdentifier == null || $scope.patientIdentifier == ""){
     			    	 growl.warning("Please enter a patient identifier ");
     			    	 return false;
     			    }
     			    
     				 if($scope.purposeOfRequest == undefined || $scope.purposeOfRequest == null || $scope.purposeOfRequest == ""){
      			    	 growl.warning("Please select purpose of request ");
      			    	 return false;
      			       }
     				 
     			    if($scope.datepickerHealth == undefined || $scope.datepickerHealth == null || $scope.datepickerHealth == ""){
    			    	 growl.warning("Please enter a from date ");
    			    	 return false;
    			    }
     			    
     			   if($scope.datepickerHealthInfo == undefined || $scope.datepickerHealthInfo == null || $scope.datepickerHealthInfo == ""){
  			    	 growl.warning("Please enter a to date ");
  			    	 return false;
  			       }
     			   
     			  if(selectedLabels == null || selectedLabels.length == 0){
   			    	 growl.warning("Please select health info type");
   			    	 return false;
   			       }
     			 $scope.todayDate = moment(new Date()).format('DD/MM/YYYY');

     			// Format the selected date to ISO format
     			var date = moment($scope.datepickerConsentExpiryHealth).format('YYYY-MM-DD'); // e.g., '2024-10-22'

     			// Use the time entered by the user
     			var time = $scope.consent.expiryTime; // Assuming input is in 'HH:mm:ss' or 'HH:mm' format

     			// Ensure the time includes seconds, and add random seconds if missing
     			if (time.split(':').length === 2) {
     			    // Generate random seconds if not provided
     			    var randomSeconds = Math.floor(Math.random() * 60); // 0 to 59
     			    time += `:${randomSeconds < 10 ? '0' + randomSeconds : randomSeconds}`; // Add leading zero if needed
     			}

     			// Concatenate date and time
     			var dateTimeString = date + ' ' + time; // e.g., '2024-10-22 16:30:45'

     			// Create a moment object from the concatenated string
     			var localDateTime = moment(dateTimeString, 'YYYY-MM-DD HH:mm:ss');

     			// Automatically append milliseconds to the time
     			var dateTimeWithMillis = localDateTime.add(Math.floor(Math.random() * 1000), 'milliseconds');

     			// Format the combined date and time as an ISO string with dynamic seconds and milliseconds
     			var isoDateTime = dateTimeWithMillis.format('YYYY-MM-DDTHH:mm:ss.SSS[Z]');

     			// Assign or use `isoDateTime` as required
     			console.log('ISO Date Time:', isoDateTime);
     				// ISO 8601 without time zone conversion

     		   
     			    
     		     if($scope.expiryDate <= $scope.todayDate){
     		    	 growl.warning("Please select future date");
   			    	 return false;
     		     }
     		
     		    $rootScope.startLoader();
       				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/consent/request/init";

       				  var data = {
       						  "organizationId" : $scope.organizationId,
       						  "unitId" : $scope.unitId,
       						  "healthId" : $scope.patientIdentifier,
       						  "healthInfoTypes" : selectedLabels,
       						  "purposeOfRequest" : $scope.purposeOfRequest,
       						  "healthInfoFromDate":$scope.datepickerHealth,
       						 "healthInfoToDate":$scope.datepickerHealthInfo,
       						"consentExpiryDate":isoDateTime,
       						"patientName": $scope.selectedPatient.patientId.toString(),
       						"patientFullName": $scope.selectedPatient.patientName,
       						"healthIdNumber": $scope.selectedPatient.healthNumber,
       						"mobileNumber": $scope.selectedPatient.mobileNumber,
       						  "eventId":41
       						}
       				GenericService.serviceAction("POST", URI, data).then(
       					function (response) {
       						if(response.data.status =="success"){
      						    growl.success(response.data.message);
      						$scope.patientIdentifier ="";
     						$scope.purposeOfRequest="";
     						$scope.datepickerHealth="";
     						$scope.datepickerHealthInfo="";
     						$scope.datepickerConsentExpiryHealth="";
     						 $scope.consent.expiryTime="";
     						  $scope.hiTypes = [
     		                     { id: 'OPConsultation', label: 'OP Consultation', checked: false },
     		                     { id: 'DiagnosticReport', label: 'Diagnostic Reports', checked: false },
     		                     { id: 'DischargeSummary', label: 'Discharge Summary', checked: false },
     		                     { id: 'ImmunizationRecord', label: 'Immunization Record', checked: false },
     		                     { id: 'WellnessRecord', label: 'Wellness Record', checked: false },
     		                     { id: 'HealthDocumentRecord', label: 'Health Document Record', checked: false },
     		                     { id: 'Prescription', label: 'Prescription', checked: false}
     		                     ];
     						 $scope.getconsentDetail();
       						$rootScope.stopLoader();
       						$('#consentRequestModal').modal('hide');
       						$rootScope.stopLoader();
       						}else{
       							$rootScope.stopLoader();
           						growl.error(response.data.message);
           					 $('#consentRequestModal').modal('hide');
       						}
       					},
       					function errorCallback(response) {
       						$rootScope.stopLoader();
       						growl.error('Connection time out');
       						$scope.patientIdentifier ="";
      						$scope.selectedLabels="";
      						$scope.consent.checked="";
     						$scope.purposeOfRequest="";
     						$scope.datepickerHealth="";
     						$scope.datepickerHealthInfo="";
     						$scope.datepickerConsentExpiryHealth="";
     						 $scope.consent.expiryTime="";
       					});
       			}
                 
                 
     			$scope.getconsentDetail= function(){

    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/getConsentRequestDetails";

    				var data={
  						  "organizationId" : $scope.organizationId,
  						  "unitId" : $scope.unitId
  					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						
    						$scope.pagination.data=[];
    						
    		                 
    		             	if(!!response && !!response.data && response.data.listObject.length > 0){            
    	                        PaginationService.searchData(response.data.listObject, $scope.pagination);
    	                      }
    		             	
    		             	$rootScope.stopLoader();
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
     			$scope.getconsentDetail();
     			
     			$rootScope.finalResultList =[];
     			$scope.getHiuDecryptedData = function(consentRequestIdHiu){

    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/getHiuDecryptedData";

    				var data={
  						  "organizationId" : $scope.organizationId,
  						"consentRequestIdHiu" : consentRequestIdHiu,
  						  "unitId" : $scope.unitId
  					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						 $scope.hiuList =[];
    						 $rootScope.finalResultList =[];
    						 
    		                 $scope.hiuList = response.data.listObject;
    		                 $rootScope.consentRequestIdHiu = consentRequestIdHiu;
    		                 console.log("fhir list final ",$scope.hiuList);
    		               
    		                 angular.forEach($scope.hiuList, function(v, k) {
     						    if (v.decryptedData) {
     						        try {
                                        let jsonData = JSON.parse(v.decryptedData);
 										// Access the first entry
 										let firstEntry = jsonData.entry && jsonData.entry[0];
 										
 										// Extract the text value from the type object
 										    let codeValue = '';
 											let displayValue = '';
 										if(firstEntry.resource.type != undefined && firstEntry.resource.type.coding != undefined && firstEntry.resource.type.coding.length > 0){
 											 codeValue = firstEntry.resource.type.coding[0].code;
 	 										 displayValue = firstEntry.resource.type.coding[0].display;
 	 										 console.log("code ,display",codeValue+" ,"+displayValue);
 	 										 
 	 										
 										}
 										let textValue = firstEntry.resource.type.text;
 										console.log("text ",textValue);
 										
 										var matchingData = false;

 										if(codeValue=="373942005"){
 											$.each($scope.grantedHitypesArray,function(index,data){ 
 											   matchingData =  data.toLowerCase().contains("discharge") 
 											   if(matchingData){
 												  return false;
 											   }
 											  
 											});
 											
 										}
 										if(codeValue=="419891008"){
											$.each($scope.grantedHitypesArray,function(index,data){ 
											   matchingData =  data.toLowerCase().contains("healthdocument") 
											   if(matchingData){
	 												  return false;
	 											   }
											});
											
										}
 										if(codeValue=="41000179103"){
											$.each($scope.grantedHitypesArray,function(index,data){ 
											   matchingData =  data.toLowerCase().contains("immunization") 
											   if(matchingData){
	 												  return false;
	 											   }
											});
											
										}
 										if(codeValue=="371530004"){
											$.each($scope.grantedHitypesArray,function(index,data){ 
											   matchingData =  data.toLowerCase().contains("consultation") 
											   if(matchingData){
	 												  return false;
	 											   }
											});
											
										}
 										if(codeValue=="440545006"){
											$.each($scope.grantedHitypesArray,function(index,data){ 
											   matchingData =  data.toLowerCase().contains("prescription") 
											   if(matchingData){
	 												  return false;
	 											   }
											});
											
										}
 										//diagnostic 
 										if( displayValue.toLowerCase().contains("diagnostic")  || textValue.toLowerCase().contains("diagnostic")){
										$.each($scope.grantedHitypesArray,function(index,data){ 
											   matchingData =  data.toLowerCase().contains("diagnostic") 
											   if(matchingData){
	 												  return false;
	 											   }
											});
											
										}
 										
 										//Wellness 
 										if(textValue.toLowerCase().contains("wellness")){
 											
 											/* matchingData =  $scope.grantedHitypesArray.filter(function(data) {
 											    return data.toLowerCase().includes("wellness");
 											});*/
 											
										$.each($scope.grantedHitypesArray,function(index,data){ 
											   matchingData =  data.toLowerCase().contains("wellness") 
											   
											   if(matchingData){
	 												  return false;
	 											   }
											});
											
										}
 										
 										if(textValue.toLowerCase().contains("invoice")){
											$.each($scope.grantedHitypesArray,function(index,data){ 
												   matchingData =  data.toLowerCase().contains("invoice") 
												   
												   if(matchingData){
		 												  return false;
		 											   }
												});
												
											}
 										
 										
 										
     						
     						            if (matchingData) {
     						              
     						                    $rootScope.finalResultList.push(JSON.parse(v.decryptedData));
     						              
     						            }
     						        } catch (error) {
     						            console.error(`Invalid JSON in decryptedData at index ${k}:`, error.message);
     						            console.error('Malformed decryptedData:', v.decryptedData);
     						        }
     						    } else {
     						        console.warn(`Missing decryptedData at index ${k}`);
     						    }
     						});
    		             	
    		                 
    		                 angular.forEach($scope.finalResultList, function(v, k) {
    		                	    angular.forEach(v.entry, function(entryc, k) {
    		                	        if (entryc.resource && entryc.resource.resourceType && entryc.resource.resourceType === 'Practitioner') {
    		                	            $rootScope.practitioner = (entryc.resource.name && entryc.resource.name[0] && entryc.resource.name[0].text) || 'N/A';
    		                	            $rootScope.visitDateAndTime = (entryc.resource.meta && entryc.resource.meta.lastUpdated) || 'N/A';
    		                	        }
    		                	        if (entryc.resource && entryc.resource.resourceType === 'Organization') {
    		                	            $rootScope.OrganizationNames = entryc.resource.name || 'N/A';
    		                	        }
    		                	        if (entryc.resource && entryc.resource.resourceType === 'Encounter') {
    		                	            $rootScope.encounterhh = (entryc.resource.class && entryc.resource.class.display) || 'N/A';
    		                	        }
    		                	    });
    		                	});

    		                 $rootScope.stopLoader();
    		                
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
                 
     			
     			$scope.getPatientData = function(id){

    				var URI = BASE_URL + ROOT_URL + "/api/abdm/getPatientDataByPatientId";

    				var data={
  						  "organizationId" : $scope.organizationId,
  						  "patientId" : id,
  						  "unitId" : $scope.unitId
  					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response) {
    						if(response.data.object != null){
    							 $scope.patientDetaild = {};
       		                     $scope.patientDetaild = response.data.object;
       		                     $rootScope.healthIdSelectedPatient=$scope.patientDetaild.healthId;
    			            	 $rootScope.healthNumberSelectedPatient=$scope.patientDetaild.healthNumber;
    			                 $rootScope.mobileNumber=$scope.patientDetaild.mobileNumber;
    			                 $rootScope.genderOfPatient=$scope.patientDetaild.genderCode;
   			            	     $rootScope.birthDate=$scope.patientDetaild.dob;
   			            	     $rootScope.patientnamek=$scope.patientDetaild.patientName;
    						}
    						 
    		                
    					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			}
     			
     			$scope.callFetchApi = function(info) {

    				$rootScope.startLoader();
    				
    				if (info.consentExpiryDate && info.consentExpiryDate) {
    					
    					
    					 const expiryTimeStr = info.consentExpiryDate; // Example: "15-11-2024, 13:20"

    					    const expiryDateParts = expiryTimeStr.split(', '); // Split date and time
    					    const datePart = expiryDateParts[0]; // "15-11-2024"
    					    const timePart = expiryDateParts[1]; // "13:20"

    					    const [day, month, year] = datePart.split('-').map(Number);
    					    const [hours, minutes] = timePart.split(':').map(Number);

    					    // Create a Date object for expiry time
    					    const expiryDate = new Date(year, month - 1, day, hours, minutes);

    					    // Get the current date and time
    					    const currentDate = new Date();

    					    // Compare if the expiry date is less than or equal to the current date
    					    if (expiryDate <= currentDate) {
        			            console.log('Expiry time is less than or equal to the current time.');
        			            growl.error('The Consent has Expired you can not Fetch the consent');
        			            
        			            var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/updateStatus";
        	    				var data={
        	  						"consentId" : info.consentRequestIdHiu,
        	  						"organizationId" : $scope.organizationId,
        	  					  "unitId" : $scope.unitId
        	  					}
        	    				GenericService.serviceAction("POST", URI, data).then(
        	    					function (response)  {
        	       						if(response.data.status =="success"){
        	      						  $timeout(function() {
        	        							$scope.getconsentDetail();
        	        		                	 },10000);
        	      						  
        	       						}else{
        	       							$rootScope.stopLoader();
        	       						}
        	       					},
        	    					function errorCallback(response) {
        	    						$rootScope.stopLoader();
        	    					});
        	    		
        	    				$rootScope.stopLoader();
        			            
        			            return false;
        			        }
    					
    			    }
    				
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/consent/fetch";

    				console.log("abha IDDD....." +info.abhaId);
    				
    				var data={
  						  "organizationId" : $scope.organizationId,
  						"healthId" : info.abhaId,
  						"consentRequestIdHiu" : info.consentRequestIdHiu,
  						"eventId":44,
  						  "unitId" : $scope.unitId
  					} 
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response)  {
       						if(response.data.status =="success"){
      						    growl.success("Fetch Record Successfully");
    			            	$scope.patientIdOfFetch = parseInt(info.patientName);
    			            	$scope.getPatientData($scope.patientIdOfFetch);
    			            	
      						  $timeout(function() {
      							$scope.getGrantedHiTypes(info);
      							 $rootScope.stopLoader();
                              },120000);
      						  
       		                        		                 
       						
       						}else{
       							$rootScope.stopLoader();
           						growl.error(response.data.message);
       						}
       					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			};
    			
    			
    			
    			
    			$scope.getGrantedHiTypes = function(info) {

    				$rootScope.startLoader();
    				
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/getGrantedHiTypes";

    				console.log("abha IDDD....." +info.abhaId);
    				
    				var data={
  						  "organizationId" : $scope.organizationId,
  						"healthId" : info.abhaId,
  						"consentRequestIdHiu" : info.consentRequestIdHiu,
  						  "unitId" : $scope.unitId
  					} 
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response)  {
       						if(response.data.status =="success"){
      						    //growl.success("Fetch Record Successfully");
    			            	$scope.grantedHiTypes = response.data.listObject[0].consentResponseHitypes;
    			            	 $scope.grantedHitypesArray = JSON.parse($scope.grantedHiTypes);
       						console.log("GRanted HITYPES: "+$scope.grantedHitypesArray);
    							$scope.getHiuDecryptedData(info.consentRequestIdHiu); 
    						  
       						console.log("Formated Granted Hityes: "+$scope.formattedData);
       						}else{
       							$rootScope.stopLoader();
           						growl.error(response.data.message);
       						}
       					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			};
    			
    			
    			
    			
    			$scope.checkStatusOfConsent  = function(info) {
                   console.log("consentId :" +info.consentId);
    				$rootScope.startLoader();
    				var URI = BASE_URL + ROOT_URL + "/api/abdm/v3/consent/request/status";
    				var data={
  						"consentId" : info.consentRequestIdHiu,
  						"organizationId" : $scope.organizationId,
  					  "unitId" : $scope.unitId
  					}
    				GenericService.serviceAction("POST", URI, data).then(
    					function (response)  {
       						if(response.data.status =="success"){
      						    growl.success(response.data.message);
      						  $timeout(function() {
        							$scope.getconsentDetail();
        		                	 },10000);
      						  
       						}else{
       							$rootScope.stopLoader();
           						growl.error(response.data.message);
       						}
       					},
    					function errorCallback(response) {
    						$rootScope.stopLoader();
    						growl.error('Connection time out');
    					});
    			};
    			
    			
    			$scope.getPdfDataUri = function(entrysub) {
			    if (entrysub.resource.content && entrysub.resource.content.length > 0) {
			      const attachmentData = entrysub.resource.content[0].attachment.data;
			      if (/^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)?$/.test(attachmentData)) {
			        return 'data:application/pdf;base64,' + attachmentData;
			      } else {
			        console.error('Invalid base64-encoded PDF data');
			        return null;
			      }
			    }
			    return null;
			  }
    			
    			
    			$scope.showHiType = function(hiTypesRequested) {
    				
    				$scope.hiTypesRequestedNew = [];
    			    if (hiTypesRequested != null) {
    			    	$scope.hiTypesRequestedNew =JSON.parse(hiTypesRequested);
    			  }
    			}
    			
    			
    }
    


    function config($stateProvider) {
    $stateProvider.state("abdmConsentRequest", {
      url: "/abdmConsentRequest",
      templateUrl: 'views/Abdm/abdm-consent-requestNew.html',
      controller: "AbdmConsentRequest.Controller",
      title: 'ABDM Consent Request',
      controllerAs: "vm",
    });
  }

  angular
    .module("myApp")
    .config(config)
    .controller("AbdmConsentRequest.Controller", abdmConsentRequestController);
})();
