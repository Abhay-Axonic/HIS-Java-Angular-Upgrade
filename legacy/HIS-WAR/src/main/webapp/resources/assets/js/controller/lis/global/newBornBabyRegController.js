/**
 * @author  Tushar & Sufiyan
 */

angular.module('myApp').controller(
		"newBornBabyRegController",
		[ '$scope',
			'$http',
			'$localStorage',
			'$sessionStorage',
			'$cookies',
			'$rootScope',
			'GenericService',
			'BillingGenericService',
			'growl',
			'$state',
			'$interval',
			'PagerService',
			function($scope, $http, $localStorage, $sessionStorage,
					$cookies, $rootScope, GenericService,BillingGenericService, growl,
					$state,$interval, PagerService) {
			
					$scope.init = function() {
						var cookieObject = $cookies.getObject('cookieObject');
						if(cookieObject == undefined){
							$state.go('login');
							return;
						}
						
						$scope.unitId = cookieObject.unitId;
						$scope.organizationId = cookieObject.organizationId;
						$scope.userId = cookieObject.userId;

						/** Variables for pagination */
						$scope.offset = 0;
						$scope.noOfRecordsPerPage = 10;
						$scope.newBornRegistrationList = [];
						$scope.totalNoOfRecords;
						$scope.searchKeyword;
						$scope.newBornRegistrationListCount;
						$scope.aPatientList = [];
						$scope.prefixList = [];
						$scope.genderList = [];
						
						$rootScope.loginpage = true;
						$scope.newBornRegistration = {};
						$scope.noOfBabiesList = [];
						$scope.babyObjList = {};
						$scope.saveBtnFlag = true;
						$scope.updateBtnFlag = false;

						// for popup
						$scope.showModal = false;
						$scope.buttonClicked = "";
						$scope.mothersName = "";
						$scope.popUpFlag = true;
						//$scope.OutcomeOptions = [{label:"Live Birth", value:"live",tick : "true"}, {label:"Still Birth", value:"still", tick : "false"}];
						var data = {
							unitId : $scope.unitId,	
							organizationId : $scope.organizationId
						}
						$rootScope.startLoader();

						var URI = BASE_URL + ROOT_URL + GETADMITTEDPATIENTLIST;
						console.log("URI", URI);
						GenericService.serviceAction("POST", URI, data)
							.then(function(response) {
									$scope.admittedPatientList = [];
									if (response.data.status == "success") {
										$rootScope.stopLoader();
										$scope.admittedPatientList = [];
										console.log(response.data.listObject);
										angular.forEach(response.data.listObject, function(info, key) {
											if(info.deliveryFlag==0  && info.genderId==2 &&  info.isLaborWard=='Y'){
												$scope.admittedPatientList.push(info);
											}
										});
										$scope.aPatientList = $scope.admittedPatientList;
									}
								});$rootScope.stopLoader();
								
						var URI = BASE_URL + ROOT_URL + GETACTIVEPREFIXLIST;
						BillingGenericService.serviceAction(METHOD_GET, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
							.then(function(response) {
								console.log(response);
								$scope.prefixList = [];
								if (response.data.status == "success")
									$scope.prefixList = response.data.listObject;
								console.log("Prefix List ",$scope.prefixList);
							});
								
						var URI = BASE_URL + ROOT_URL + GETACTIVEGENDERLIST;
						BillingGenericService.serviceAction(METHOD_GET, URI, data, NOTIFICATION_MSG_STATUS_FALSE)
							.then(function(response) {
									console.log("$scope.genderList",response);
									$scope.genderList = [];
									if (response.data.status == "success")
										$scope.genderList = response.data.listObject;
								});		
					}
					$scope.init();
					
					//clock picker
					$scope.initClockPicker = function() {
						$('.clockpicker').clockpicker();
					}
					
					/* code for setting Label */
					$(".selectedPageName").text("New Born Registration");
					
					$scope.setNoOfBabiesRow= function(numberOfBabies){
						$scope.noOfBabiesList = [];
						for(var i=1;i<=numberOfBabies;i++){
							var babyObjTemp = {
								babyPrefix : '',	
								babyName : '',
								weight : '',
								babyDOB : '',
								babyTOB : '',
								babyGender : '',
								deliveryType : 'normal',
								babyAge : '',
								outcomeStatus : '',
								babyTerm : '',
								id : i
							};
							$scope.noOfBabiesList.push(babyObjTemp);
						}
					}
					
					var yesterdaysDate = new Date();
					yesterdaysDate.setDate(yesterdaysDate.getDate() - 1);
					
					//for datepicker Search
					$scope.dateOptions = {
							formatYear : 'yyyy',
							todayHighlight:'TRUE',
							startDate: yesterdaysDate,
							endDate: new Date(),
							maxDate:new Date(),
							minDate:yesterdaysDate,
							showWeeks : false
						};
					
					$scope.openDatePickerDOB = function(index){
						 for (var i = $scope.noOfBabiesList.length - 1; i >= 0; i--) {
                             if (i === index) {
                                 $scope.babyDOB.opened[i] = true;
                             } else {
                                 $scope.babyDOB.opened[i] = false;
                             }
                         }
					};

				     $scope.babyDOB = { opened : [ false ], };
				
					// function for popup ok button call
					$scope.PopupOkBtn = function() {
						$scope.popUpFlag = false;
						$scope.saveNewBornBaby();
					}
						
						// Function For Save New Born Baby
						$scope.saveNewBornBaby = function() {
							
							if($scope.newBornRegistration.recordSelected == 0 || $scope.newBornRegistration.recordSelected == undefined)
							{
							  growl.error("Please Select Patient..!!",{title : "WARNING!"});
							  return false;
							}else{
							$scope.currentDate = new Date();
							$scope.currentDate = moment($scope.currentDate)
									.format('DD-MM-YYYY HH:mm:ss');
							$scope.$broadcast('show-errors-check-validity');
							var noOfbabies = $scope.newBornRegistration.numberOfBabies;
							var DOA =  new Date();	
							DOA = moment($scope.newBornRegistration.doaSelected).format('DD-MM-YYYY');
							
							var data = {
								patientId : $scope.newBornRegistration.recordSelected,
								fatherName :$scope.newBornRegistration.fatherName,
								/*deliveryType :$scope.newBornRegistration.deliveryType,*/
								numberOfBabies:$scope.newBornRegistration.numberOfBabies,
								status : 'A',
								createdBy : $scope.userId,
								updatedBy : $scope.userId,
								unitId : $scope.unitId,
								organizationId : $scope.organizationId,
								createdDate : $scope.currentDate,
								updatedDate : $scope.currentDate
							};
							
						var saveOrderDetailsObj = [];
				
							var currentDateComp = new Date(); currentDateComp = moment(currentDateComp).format('DD-MM-YYYY');
							var currentTimeComp = new Date(); currentTimeComp = moment(currentTimeComp).format('HH:mm');
							if(DOA>currentDateComp){	growl.error('Can not select date Prior to Admission Date',{title : 'Invalid Date!'});return false;}
							
							var validObj = $scope.noOfBabiesList.find(obj => {return (obj.babyPrefix == undefined || obj.babyPrefix == '' ||
									obj.babyDOB == undefined || obj.babyDOB == '' ||
									obj.babyTOB == undefined || obj.babyTOB == '' ||
									obj.babyGender == undefined || obj.babyGender == ''||
									obj.deliveryType == undefined || obj.deliveryType == '' ||
									obj.outcomeStatus == undefined || obj.outcomeStatus == '' ||
									obj.babyAge == undefined || isNaN(Number(obj.babyAge)) 
									)});
							
							if(validObj!=undefined){
								growl.error('Please Record the Details !!',{title : 'Error!'});return false;
							}
							var isValidDOB = false;
							var isValidTOB = false;
							var isValidBabyAge = false;
							angular.forEach($scope.noOfBabiesList, function(value, key) {
								var DOB = moment(value.babyDOB).format('DD-MM-YYYY');
								var TOB = moment(value.babyTOB,'HH:mm').format('HH:mm');
								console.log("babyAge ",value.babyAge);
								
								// Validations
							/*if(value.babyPrefix == undefined || value.babyPrefix == ''){
								growl.error('Please select a valid prefix',{title : 'Invalid Prefix!'});return false;
							}
							if(DOB!='Invalid date'){
								if(currentDateComp<DOB){
									growl.error('Can not select future date',{title : 'Invalid Date!'});return false;
								}
							}else{
								growl.error('Please select a valid date',{title : 'Invalid Date!'});return false;
							}
							if(TOB != undefined && TOB != '' && TOB!='Invalid date' && TOB!='Invalid time'){
								if(currentDateComp==DOB && currentTimeComp < TOB){
									growl.error('Invalid Time ...!!',{title : 'Invalid Data !'});
								}	
							}else{
								growl.error('Please select a valid time',{title : 'Invalid Time!'});return false;
							}
							if(value.babyGender == undefined || value.babyGender == ''){
								growl.error('Please select a gender',{title : 'Invalid Gender!'});return false;
							}
							if(value.deliveryType == undefined || value.deliveryType == ''){
								growl.error('Please select Type of Delivery',{title : 'Invalid Type of Delivery!'});return false;
							}
							if(value.outcomeStatus == undefined || value.outcomeStatus == ''){
								growl.error('Please select an outcome of Delivery',{title : 'Invalid Outcome of Delivery!'});return false;
							}
							if(value.babyAge == undefined || isNaN(Number(value.babyAge))){
								growl.error('Please enter a valid number of weeks',{title : 'Invalid Weeks!'});return false;
							}else{
								if((Number(value.babyAge)<16 && Number(value.babyAge)> 44)){
									growl.error('Please enter weeks between 16 and 44 ',{title : 'Invalid Weeks!'});return false;
								}
							}*/
								var DOB = moment(value.babyDOB).format('DD-MM-YYYY');
								if(currentDateComp<DOB){
									isValidDOB = true;
								}
								
								if(TOB != undefined && TOB != '' && TOB!='Invalid date' && TOB!='Invalid time'){
									if(currentDateComp==DOB && currentTimeComp < TOB){
										isValidTOB = true;
									}	
								}
								
								if((Number(value.babyAge)<16 && Number(value.babyAge)> 44)){
									isValidBabyAge = true;
								}
								
								var saveArr = {
										babyPrefix : value.babyPrefix,
										babyName : value.babyName,
										weight : value.weight,
										dob : DOB,
										tob : TOB,
										sex : value.babyGender,
										deliveryType : value.deliveryType,
										gestationalAge : Number(value.babyAge),
										birthTerm : value.babyTerm ,
										outcome : value.outcomeStatus
								};
								saveOrderDetailsObj.push(saveArr);
							});
							
							if(isValidDOB){
								growl.error('Can not select future date',{title : 'Invalid Date!'});return false;
							}
							
							if(isValidTOB){
								growl.error('Invalid Time ...!!',{title : 'Invalid Data !'});return false;
							}
							
							if(isValidBabyAge){
								growl.error('Please enter weeks between 16 and 44 ',{title : 'Invalid Weeks!'});return false;
							}
							//	if(saveOrderDetailsObj.length == 0){ growl.error('Please Record the Details !!',{title : 'Error!'});return false;}
							data.babyDetails = saveOrderDetailsObj;
							console.log(data); 
							//if($scope.newBornRegistrationForm.$valid) {
								if ($scope.popUpFlag) {
									$scope.showModal = !$scope.showModal;
								} else {
									$rootScope.startLoader();
									var URI = BASE_URL + ROOT_URL + ADDNEWBORNBABYREG;
									GenericService.serviceAction("POST", URI, data)
										.then(function(response) {
											if (response.data.status == "success") {
												$rootScope.stopLoader();
												growl.success('New Born Baby Added Successfully...!!',{title : 'Success!'});
												$state.go('newBornDetails');
												/*growl.success('New Born Baby added successfully...!!',{title : 'Success!'});
												$scope.$broadcast('show-errors-reset');
												$scope.init();*/
											} else {
												growl.error('Could not register New Born Baby...!!',{title : 'Error!'});
											}
										});
									}
								//}
							}
						}
						
						$scope.searchPatientInNewBornReg = function(){
							console.log("in function searchPatientInNewBornReg()");
							var keyword =$scope.searchKeyword;
							console.log("searchKeyword == ",$scope.searchKeyword);
							if(keyword!=""){
								$scope.admittedPatientList = [];
								for (var index = 0; index < $scope.aPatientList.length; ++index) {
									var name = $scope.aPatientList[index].pFirstName.toLowerCase();
								    if (name.includes(keyword.toLowerCase())) {
								    	$scope.admittedPatientList.push($scope.aPatientList[index]);
								    }name="";
								}
							}else{
								$scope.admittedPatientList =($scope.aPatientList);
							}
						}
					
						$scope.setMothersName = function(patientId){
							$scope.mothersName = "";
							angular.forEach($scope.admittedPatientList, function(val2, key) {
                  	          if(angular.equals(patientId, val2.patientId)){$scope.mothersName = val2.pFirstName }; 
                  	        });
						}
						
						$scope.setBirthType = function(id){
							console.log("id",id);
							setTimeout(function() {  
								var term = "";
								angular.forEach($scope.noOfBabiesList, function(val2, key) {
		                  	         if(angular.equals(id, val2.id)){
		                  	        	 var age = Number(val2.babyAge);
		  								if(age>=16 && age<=44){
											if(age>=16 && age<=37)	term= "Pre-Term";
											else if(age>37 && age<42)	term= "Term";
											else	term= "Post-Term";
										}else{
											growl.error('Invalid Gestational Age ...!!',{title : 'Invalid Data !'});	
											val2.babyAge = ''; 
										}
		  								val2.babyTerm = term; 
		  							}   
		                  	    });
							}, 1000);
						}
						
						$scope.setbabyGender = function(prefixId,babyId){
							angular.forEach($scope.prefixList, function(value,i){
								if(value.prefixId === prefixId){
									var genId = value.id;
									angular.forEach($scope.noOfBabiesList, function(val2, key) {
			                  	         if(angular.equals(babyId, val2.id)){
			                  	        	val2.babyGender = genId+"";
			  							}   
			                  	    });
								}
							});
						}
						
						$scope.validateTime = function(datedd,timett){
							console.log("Date ",datedd,"\t Time ",timett);
							var currentDateComp = new Date(); 
							var currentTimeComp = new Date(); 	
							var selectedDate = moment(datedd).format('DD-MM-YYYY');
							var selectedTime = moment(timett,'HH:mm').format('HH:mm');
							console.log("S Date ",selectedDate,"\t S Time ",selectedTime);
							currentDateComp = moment(currentDateComp).format('DD-MM-YYYY');
							currentTimeComp = moment(currentTimeComp).format('HH:mm');
							console.log("C Date ",currentDateComp,"\t C Time ",currentTimeComp);
							if(selectedTime != 'Invalid date'){
								if(selectedDate==currentDateComp && currentTimeComp < selectedTime){
									var msg = selectedTime +'birth time is greater than current Time i.e. '+currentTimeComp;
									growl.error(msg,{title : 'Invalid Data !'});
								}
							}else{
								growl.error('Invalid Time',{title : 'Invalid Data !'});
							}
								
						}
						
		} ]);