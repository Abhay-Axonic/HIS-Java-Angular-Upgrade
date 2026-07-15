angular
		.module('myApp')
		.controller(
				'bulkRegistrationController',
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
						function($scope, $rootScope, $state, $http,
								GenericService,$sessionStorage,$cookies, growl, $localStorage,BillingGenericService) {
							
							var cookieObject = $cookies.getObject('cookieObject');
							if(cookieObject == undefined){
								$state.go('login');
								return;
							}
							
							var cookieObjectAppConfig = $cookies.getObject('cookieObjectAppConfig');
							$scope.minAge = cookieObjectAppConfig.minAge;
							$scope.maxAge = cookieObjectAppConfig.maxAge;
							$scope.defaultPatientCategoryId=cookieObjectAppConfig.defaultPatientCategoryId;
							
							
							$scope.saveBtnFlag = true;
							$rootScope.loginpage = true;
							$scope.unitId = cookieObject.unitId;
							$scope.organizationId = cookieObject.organizationId;
							$scope.userId = cookieObject.userId;
							
							$scope.dateOptions = {
									formatYear : 'yyyy',
									showWeeks : false,
									maxDate:new Date()
								};

								$scope.openDatePicker = function() {
									$scope.datepicker.opened = true;
								};
								$scope.datepicker = {
										opened : false
									};
						$scope.getcampList = function(){
								var data = {
										unitId:$scope.unitId,
										organizationId:$scope.organizationId
										};								
								var URI = BASE_URL + ROOT_URL+ "/global/bloodBank/getCampMasterList";
								BillingGenericService.serviceAction(METHOD_POST, URI, data,NOTIFICATION_MSG_STATUS_FALSE)
								.then(function(response) {
											//$scope.stateList = [];
											if (response.data.status == "success"){
												$rootScope.stopLoader();
													$scope.campList = response.data.listObject;
													console.log("$scope.compList", 	$scope.campList);
											}
										});
						}
						$scope.getcampList();
						
						$scope.init = function(){
							var data = {};
							
						var URI = BASE_URL + ROOT_URL
						+ GETACTIVEPREFIXLIST;
				BillingGenericService
						.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.prefixList = [];
									if (response.data.status == "success")
										$scope.prefixList = response.data.listObject;
								});

				var URI = BASE_URL + ROOT_URL
						+ GETACTIVEGENDERLIST;
				BillingGenericService
						.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.genderList = [];
									if (response.data.status == "success")
										$scope.genderList = response.data.listObject;
								});
				
				

				var URI = BASE_URL + ROOT_URL
						+ GETACTIVEMARITALSTATUSLIST;

				BillingGenericService
						.serviceAction(METHOD_GET, URI, data,
								NOTIFICATION_MSG_STATUS_FALSE)
						.then(
								function(response) {
									console.log(response);
									$scope.maritalStatusList = [];
									if (response.data.status == "success")
										$scope.maritalStatusList = response.data.listObject;
								});
				

				var URI = BASE_URL + ROOT_URL
						+ GETACTIVEIDENTIFICATION;
				BillingGenericService
				.serviceAction(METHOD_GET, URI, data,
						NOTIFICATION_MSG_STATUS_FALSE)
				.then(
						function(response) {
							console.log(response);
							$scope.identificationList = [];
							if (response.data.status == "success")
								$scope.identificationList = response.data.listObject;
						});
				
				
				$scope.patientObj = {
									prefixId : '',
									firstName : '',
									middleName : '',
									lastName : '',
									genderId : '',
									countryCallingCode : '',
									mobileNumber : '',
									dob : '',
									organizationId : $scope.organizationId,
									unitId : $scope.unitId,
									updatedBy : $scope.userId,
									createdBy : $scope.userId,
									status : 'A',
									patientCategoryId:$scope.defaultPatientCategoryId!=null ? $scope.defaultPatientCategoryId : null,
									registrationTypeId:1,
									campId : ''
					};
				
				
						}
						$scope.init();
				
						$scope.selectGender = function(id)
						{
							if(id > 0){
							var genId = $.grep($scope.prefixList, function (doc) {
			    	            return doc.prefixId == id;
			    	        })[0].id;
							
							$scope.patientObj.genderId = genId;
							}
							else
								{
								$scope.patientObj.prefixId = '';
								$scope.patientObj.genderId = '';
								}
							
						}
						

						
						$scope.datediff = function(dateFrom, dateTo)
						{
						    var from = {
						        d: dateFrom.getDate(),
						        m: dateFrom.getMonth(),
						        y: dateFrom.getFullYear()
						    };

						    var to = {
						        d: dateTo.getDate(),
						        m: dateTo.getMonth(),
						        y: dateTo.getFullYear()
						    };

						    var daysFebruary = to.y % 4 != 0 || (to.y % 100 == 0 && to.y % 400 != 0)? 28 : 29;
						    var daysInMonths = [31, daysFebruary, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];

						    if (to.d < from.d) {
						        to.d   += daysInMonths[parseInt(to.m)];
						        from.m += 1;
						    }
						    if (to.m < from.m) {
						        to.m   += 12;
						        from.y += 1;
						    }

						    /*return {
						        days:   to.d - from.d,
						        months: to.m - from.m,
						        years:  to.y - from.y
						    };*/
                            if(to.d == from.d && to.m == from.m && to.y== from.y && $scope.datepicker!=undefined && $scope.datepicker!=null && $scope.datepicker.opened == undefined){
                                return {
                                       days:   1,
                                       months: to.m - from.m,
                                       years:  to.y - from.y
                                   };
                           }else{
                                return {
                                       days:   to.d - from.d,
                                       months: to.m - from.m,
                                       years:  to.y - from.y
                                   };
                           }
						}
						
						/*$scope.subtractCurrDateFromDOB =function(cntOfYears,cntOfMonths,cntOfDays){
							var currDay = moment(new Date()).format('DD');
							var currMonth = moment(new Date()).format('MM');
							var currYear = moment(new Date()).format('YYYY');
							
							if(cntOfMonths>=12){
								var convertIntoYear = cntOfMonths/12;
								cntOfMonths = 0;
								cntOfYears = parseInt(cntOfYears)+ convertIntoYear; 
							}
							var calDay = currDay-cntOfDays;
							var calMonth = currMonth- cntOfMonths;
							var calYear = currYear- cntOfYears;
							
							$scope.datepicker = moment(calYear+"-"+calMonth+"-"+calDay).toDate();
						}*/
						
							$scope.newAge="";
							$scope.calDOBFlag = 0;
							$scope.calculateAge= function()
							{
								if($scope.datepicker == undefined || $scope.datepicker == null || $scope.datepicker == ''){
									 $scope.age = "";
									   growl.warning("Date is invalid",{
											title : "warning"
										});
										$scope.datepicker = new Date();
									
									   return false;
								}else{
								if($scope.datepicker > new Date())
									{
											       $scope.age = "";
												   growl.warning("DOB should not be greater than today's date",{
														title : "warning"
													});
													$scope.datepicker = new Date();
												
												   return false;
									}else{
										
										/*var dt = moment($scope.datepicker).format('MM/DD/YYYY');
										var nDt = moment(new Date()).format('MM/DD/YYYY');
									
										var milli_diff = new Date(nDt).getTime() - new Date(dt).getTime();//time difference in milliseonds
								        if( milli_diff < 0 ) {
								            alert('Please recheck date range');
								            return false;
								        }
								        var diff_days_total = (milli_diff / (1000 * 3600 * 24 ));
								        var diff_years_float = (diff_days_total / (365));
								        var diff_years = parseInt( diff_years_float );
								        var diff_months_float = ( ( diff_years_float - diff_years ) * 12 );
								        var diff_months = parseInt( diff_months_float );
								        var diff_days_float = ( ( diff_months_float - diff_months ) * 30 );
								        var diff_days = parseInt( diff_days_float );
								        
								        $scope.ageYear = diff_years;
								        $scope.ageMonth = diff_months;
								        $scope.ageDay = diff_days;*/
								        
								        //alert(diff_years + " years " + diff_months + " months " + diff_days + " days ");
										
										//Test
										
										var dt = moment($scope.datepicker).format('MM/DD/YYYY');
										var nDt = moment(new Date()).format('MM/DD/YYYY');
										
										
										var array = dt.split('/');
										var array1 = nDt.split('/');
									   
										var curday = array1[1];
										var curmon =array1[0];
										var curyear =array1[2];
										
										var calday =array[1];
										var calmon = array[0];
										var calyear = array[2];
										
											var curd = new Date(curyear,curmon-1,curday);
											var cald = new Date(calyear,calmon-1,calday);
											
									                    var diff =  Date.UTC(curyear,curmon-1,curday,0,0,0) - Date.UTC(calyear,calmon-1,calday,0,0,0);
									    
									                    var dife = $scope.datediff(cald,curd); 
									                   // $("#r1").val(dife.years+" yrs, "+dife.months+" m, & "+dife.days+" d");
									                    if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 1){
									                    	$scope.ageYear = dife.years;
									                    	$scope.ageMonth = dife.months;
									                    	$scope.ageDay = dife.days;
									                    	
									                    	 if($scope.ageYear!=undefined && $scope.ageYear!=null && $scope.ageYear!="" && $scope.ageYear!=0
									                    			 && $scope.ageMonth!=undefined && $scope.ageMonth!=null && $scope.ageMonth!="" && $scope.ageMonth!=0
									                    			 && $scope.ageDay!=undefined && $scope.ageDay!=null && $scope.ageDay!="" && $scope.ageDay!=0){
									                    		 $scope.calDOBFlag = 1;
									                    	 }
													      }
/*				
									                    
									                    var as = parseInt(calyear)+dife.years+1;
									    
									                    var diff =  Date.UTC(as,calmon-1,calday,0,0,0) - Date.UTC(curyear,curmon-1,curday,0,0,0);
									                    var datee = diff/1000/60/60/24;
									                    //$("#r5").val(datee+" days left for your next birthday");
									                    var percent = (datee/365)*100;
									                    $(".liquid").css("width",percent+"%");
									                    if((curday == calday) && (curmon == calmon))
									                    { 
									                        $("#rescont").html("<h2><marquee>Happy Birthday</marquee></h2> <iframe width='560' height='315' src='https://www.youtube.com/embed/qCJSNMqub8g?autoplay=1' frameborder='0' allow='autoplay; encrypted-media' allowfullscreen></iframe>");
															// qCJSNMqub8g
									                    }
									                    else
									                    {
									                        $("#rescont").html("<h2><marquee>"+datee+" days left until your next birthday</marquee></h2>");
									                    }
									                    $("#cake").html("<marquee direction='right'><img src='https://www.calculators.org/graphics/birthday-cake.png'></marquee>");
										*/
										//Test
									}
							}
							
							}
							
							$scope.myFunctDay = function(day) {
								if(day>31){
									$scope.ageDay = 0;
									growl.warning("Day should be less then 31 ",{
										title : "warning"
										});
								}else{
									var cntOfYears = 0;
									var cntOfMonths = 0;
									var cntOfDays = 0;
									
									if($scope.ageDay == "" || $scope.ageDay == null || $scope.ageDay == undefined){
										$scope.ageDay = 0;
									}else{
										cntOfDays = $scope.ageDay;
									}
									

									if($scope.ageMonth == "" || $scope.ageMonth == null || $scope.ageMonth == undefined){
										$scope.ageMonth = 0;
									}else{
										cntOfMonths = $scope.ageMonth;
									}
									
									if($scope.ageYear == "" || $scope.ageYear == null || $scope.ageYear == undefined){
										$scope.ageYear = 0;
									}else{
										cntOfYears = $scope.ageYear;
									}
									
									//$scope.subtractCurrDateFromDOB(cntOfYears,cntOfMonths,cntOfDays);
									if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 2){
										$scope.datepicker = new Date(moment().subtract(cntOfYears, 'years').subtract(cntOfMonths, 'months').subtract(cntOfDays, 'days'));
										if($scope.datepicker!=undefined && $scope.datepicker!=null && $scope.datepicker!=""
											&&  moment($scope.datepicker).format('YYYY-MM-DD')!=moment(new Date()).format('YYYY-MM-DD')){
											$scope.calDOBFlag = 2;
										}
									}
								}
								  
							}
							$scope.myFunctMonth = function(month) {
								if(month>12){
									$scope.ageMonth = 0;
									growl.warning("Month should be less then 12 ",{
										title : "warning"
										});
								}else{
									var cntOfYears = 0;
									var cntOfMonths = 0;
									var cntOfDays = 0;
									
									if($scope.ageDay == "" || $scope.ageDay == null || $scope.ageDay == undefined){
										$scope.ageDay = 0;
									}else{
										cntOfDays = $scope.ageDay;
									}
									

									if($scope.ageMonth == "" || $scope.ageMonth == null || $scope.ageMonth == undefined){
										$scope.ageMonth = 0;
									}else{
										cntOfMonths = $scope.ageMonth;
									}
									
									if($scope.ageYear == "" || $scope.ageYear == null || $scope.ageYear == undefined){
										$scope.ageYear = 0;
									}else{
										cntOfYears = $scope.ageYear;
									}
									
									//$scope.subtractCurrDateFromDOB(cntOfYears,cntOfMonths,cntOfDays);
									if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 2){
										$scope.datepicker = new Date(moment().subtract(cntOfYears, 'years').subtract(cntOfMonths, 'months').subtract(cntOfDays, 'days'));
										if($scope.datepicker!=undefined && $scope.datepicker!=null && $scope.datepicker!=""
											&&  moment($scope.datepicker).format('YYYY-MM-DD')!=moment(new Date()).format('YYYY-MM-DD')){
											$scope.calDOBFlag = 2;
										}
									}
								}
								  
							}
							$scope.myFunctYear = function(age) {

								if(age>=$scope.minAge && age <= $scope.maxAge){
									
									var cntOfYears = 0;
									var cntOfMonths = 0;
									var cntOfDays = 0;
									
									if($scope.ageDay == "" || $scope.ageDay == null || $scope.ageDay == undefined){
										$scope.ageDay = 0;
									}else{
										cntOfDays = $scope.ageDay;
									}
									

									if($scope.ageMonth == "" || $scope.ageMonth == null || $scope.ageMonth == undefined){
										$scope.ageMonth = 0;
									}else{
										cntOfMonths = $scope.ageMonth;
									}
									
									if($scope.ageYear == "" || $scope.ageYear == null || $scope.ageYear == undefined){
										$scope.ageYear = 0;
									}else{
										cntOfYears = $scope.ageYear;
									}
									
									//$scope.subtractCurrDateFromDOB(cntOfYears,cntOfMonths,cntOfDays);
									if($scope.calDOBFlag == 0 || $scope.calDOBFlag == 2){
										$scope.datepicker = new Date(moment().subtract(cntOfYears, 'years').subtract(cntOfMonths, 'months').subtract(cntOfDays, 'days'));
										if($scope.datepicker!=undefined && $scope.datepicker!=null && $scope.datepicker!=""
											&&  moment($scope.datepicker).format('YYYY-MM-DD')!=moment(new Date()).format('YYYY-MM-DD')){
											$scope.calDOBFlag = 2;
										}
									}
								}else{
									$scope.ageYear = "";
									growl.warning("Year should be in between  "+$scope.minAge+" to "+$scope.maxAge+" years",{
										title : "warning"
										});
								}
								  
							}
						$scope.saveBulkRegistration = function(){
							$scope.$broadcast('show-errors-check-validity');
					        if($scope.bulkRegistrationform.$valid){
					        	
					        	$scope.currentDate = new Date();
								$scope.currentDate = moment($scope.currentDate)
										.format('DD-MM-YYYY HH:mm:ss');
								
								$scope.patientObj.createdDate = $scope.currentDate;
								$scope.patientObj.updatedDate = $scope.currentDate;
								$scope.patientObj.firstName =   $rootScope.capitalize_Words($scope.patientObj.firstName);
								$scope.patientObj.middleName =  ($scope.patientObj.middleName == null || $scope.patientObj.middleName.trim() == "" || $scope.patientObj.middleName == undefined ?" ": $rootScope.capitalize_Words($scope.patientObj.middleName));
								$scope.patientObj.lastName =   ($scope.patientObj.lastName == null || $scope.patientObj.lastName.trim() == "" || $scope.patientObj.lastName == undefined ?" ": $rootScope.capitalize_Words($scope.patientObj.lastName));
								$scope.patientObj.dob = moment($scope.datepicker).format('DD-MM-YYYY HH:mm:ss');
								
								console.log($scope.patientObj);
					        }
						}
							
			}]);
							