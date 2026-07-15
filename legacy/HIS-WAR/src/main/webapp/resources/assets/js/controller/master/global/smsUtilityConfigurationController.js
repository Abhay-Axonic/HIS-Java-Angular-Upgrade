angular
.module('myApp')
.controller(
		"smsUtilityConfigurationController",
		[
			'$scope',
			'$rootScope',
			'$state',
			'$cookies',
			'GenericService',
			'PagerService',
			'promiseFactory',
			'$sessionStorage',
			'growl',
			'BillingGenericService',
			'PaginationService',
			function($scope, $rootScope, $state, $cookies,GenericService, PagerService, promiseFactory,$sessionStorage, growl, BillingGenericService,PaginationService) {
				$rootScope.loginpage = true;
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.updateScope = false;
				$scope.popUpFlag = true;
				$scope.disableText = true;
				$scope.disableTextForSchedule = true;
				$scope.mailDto = {};
				$scope.mailList = [];
				/** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.commonList = [];
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;
				$scope.selectedDate=null;
				$scope.userDefinedString=null;

				var cookieObject = $cookies.getObject('cookieObject');
				var cookieObjectAppConfigSecond = JSON.parse(localStorage.getItem('cookieObjectAppConfigSecond'));
				if (cookieObject == undefined) {$state.go('login');
				return;
				}

				$scope.unitId = cookieObject.unitId;
				$scope.organizationId = cookieObject.organizationId;
				$scope.userId = cookieObject.userId;
				$scope.createdBy = cookieObject.userId;
				$scope.updatedBy = cookieObject.userId;
				$scope.whatsAppOption = cookieObjectAppConfigSecond !== undefined && cookieObjectAppConfigSecond !== null && cookieObjectAppConfigSecond !== "" ? cookieObjectAppConfigSecond.whatsAppOption : 1;
				$scope.dataList = [];
				
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
					
				 
				$scope.initMaster = function() {
					$scope.saveBtnFlag = true;
					$scope.updateBtnFlag = false;
					$scope.selectedDate=null;
					$scope.userDefinedString=null;
					$scope.mailList = [];
					$scope.common = {
							smsUtilityId : "",
							triggerId : "",
							templateDescription : "",
							categoryId : "",
							orgId :$scope.organizationId,
							unitId : $scope.unitId,
							status : "",
							createdBy : $scope.createdBy,
							createdDate : "",
							updatedBy :$scope.updatedBy,
							updatedDate : "",
							triggerName : "",
							categoryName : "",
							isSMSSend :"N",
							isWhatsappSend :"N",
							
					};
					console.log("Common-init", $scope.common);
				}
				$scope.initMaster();
				
				$scope.getMobileNoConfigurationList=function(){
					//code for registration field configuration start
					
					
					var URI = BASE_URL + ROOT_URL + "/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/"+$scope.organizationId+"/"+$scope.unitId;
							
					GenericService.serviceAction("GET", URI).then(function (response) {
						if (response.data.status == 'success') {
							
							$scope.fieldConfigList=response.data.listObject;
							if($scope.fieldConfigList.length>0)
								{
								 $scope.registrationFieldObj=$scope.fieldConfigList[0];
								 console.log("registrationFieldObj",$scope.registrationFieldObj);
								}else{
									$scope.registrationFieldObj={
											prefix: "Y",
											firstName: "Y",
											middleName: "N",
											lastName: "Y",
											birthDate: "Y",
											sex: "Y",
											callingCode: "Y",
											mobileNo: "Y",
											mobileDigit:"10",
											nationality:"Y",
											identificationDoc:"Y",
											race:"Y",
											maritalStatus:"Y",
											occupation:"Y",
											patientCategory:"Y",
											email:"N"
									}
								}												

						} else if (response.data.status == 'error') {
							growl.error(response.data.message,
								{
									title: 'Error!'
								});
						}
					});
				
						//getting default field configuration start
				}
				$scope.getMobileNoConfigurationList();
				
				
				// function for popup ok button call
				$scope.PopupOkBtn = function() {
					$scope.popUpFlag = false;
					$scope.save();
				}

				 //dose list dropdown function
				$scope.triggerList=[];
				$scope.scheduleTriggerList=[];
				$scope.getTriggerList = function() {

					var URI = BASE_URL + ROOT_URL+ "/trigger-master";
				    var data={};
				    $scope.totalTriggerList=[];
				    
					GenericService.serviceAction("GET", URI, data).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.totalTriggerList = response.data.listObject;
									
									angular.forEach($scope.totalTriggerList, function (value, key) {
										if(value.isSms){
											if(value.triggerType==2){
												$scope.scheduleTriggerList.push(value);
											}else{
												$scope.triggerList.push(value);
											}
										}
						            });
									console.log("Trigger List",$scope.triggerList);
								}
							});
				}
				$scope.getTriggerList();
				
				$scope.categoryList=[];
				$scope.scheduleCategoryList=[];
				$scope.getCategoryMasterList = function() {

					var URI = BASE_URL + ROOT_URL+ "/category-master";
					var data = { };
					GenericService.serviceAction("GET", URI, data).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									$scope.totalCategoryList = response.data.listObject;
									angular.forEach($scope.totalCategoryList, function (value, key) {
										if(value.categoryType==2){
											$scope.scheduleCategoryList.push(value);
										}else{
											$scope.categoryList.push(value);
										}
						                
						            });
									console.log("Category list",$scope.categoryList);
								}
							});
				}
				$scope.getCategoryMasterList();

			

				$scope.save = function() {
					try {
						$scope.mobileNumberList=[];
						if ($scope.popUpFlag == false) {
							$rootScope.startLoader();
						}
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
						//for getting mobile no list
						if($scope.mobileNumbers!=undefined){												
							angular.forEach($scope.mobileNumbers, function (value, key) {								
								$scope.mobileNumberList.push(value.title);
				            });
						}
						if($scope.firstTabLevel==1){
							if($scope.common.triggerId == undefined || $scope.common.triggerId == null ||
									$scope.common.triggerId == "" || isNaN($scope.common.triggerId)){
								growl.warning("Please Select Trigger Point",
										{
											title : 'warning'
										});
								return false;
							}
							
							if($scope.common.categoryId == undefined || $scope.common.categoryId ==null 
									|| $scope.common.categoryId == "" || isNaN($scope.common.categoryId)){
								growl.warning("Please Select Category",
										{
											title : 'warning'
										});
								return false;
							}
							if($scope.common.templateDescription == undefined || $scope.common.templateDescription == null || $scope.common.templateDescription == ""){
								growl.warning("Please Add Template Description",
										{
											title : 'warning'
										});
								return false;
							}
							if(($scope.common.isSmsSend == undefined || $scope.common.isSmsSend == 'N') && ($scope.common.isWhatsappSend == undefined || $scope.common.isWhatsappSend == 'N')){
                                $scope.common.isSmsSend ='Y';
                            }
                            if(($scope.common.isWhatsappSend !== undefined && $scope.common.isWhatsappSend === 'Y') && ($scope.whatsAppOption == 4)){
                                if($scope.common.whatsAppTemplateMasterId === undefined || $scope.common.whatsAppTemplateMasterId === null ||
                                    $scope.common.whatsAppTemplateMasterId === "" || isNaN($scope.common.whatsAppTemplateMasterId)){
                                    growl.warning("Please Select Whatsapp Template",
                                        {
                                            title : 'warning'
                                        });
                                    return false;
                                }
                            }
						}
						if($scope.firstTabLevel==2){
							if($scope.commonSchedule.triggerId == undefined || $scope.commonSchedule.triggerId == null 
									|| $scope.commonSchedule.triggerId =="" || isNaN($scope.commonSchedule.triggerId)){
								growl.warning("Please Select Trigger Point",
										{
											title : 'warning'
										});
								return false;
							}
							
							if($scope.commonSchedule.scheduleCategoryId == undefined || $scope.commonSchedule.scheduleCategoryId ==null 
									|| $scope.commonSchedule.scheduleCategoryId == "" || isNaN($scope.commonSchedule.scheduleCategoryId)){
								growl.warning("Please Select Category",
										{
											title : 'warning'
										});
								return false;
							}
							
							if($scope.commonSchedule.scheduletemplateDescriptionForData == undefined || $scope.commonSchedule.scheduletemplateDescriptionForData == null || $scope.commonSchedule.scheduletemplateDescriptionForData == ""){
								growl.warning("Please Add Template Description",
										{
											title : 'warning'
										});
								return false;
							}
						}
						var smsUtilityId = null;
						if($scope.firstTabLevel==1){
							if($scope.common.smsUtilityId!="" && $scope.common.smsUtilityId!=null && $scope.common.smsUtilityId!=undefined){
								smsUtilityId = $scope.common.smsUtilityId;
							}
						}else if($scope.commonSchedule.smsUtilityId!="" && $scope.commonSchedule.smsUtilityId!=null && $scope.commonSchedule.smsUtilityId!=undefined){
							smsUtilityId = $scope.commonSchedule.smsUtilityId;
						}
						
                        var userDefinedMail = null;
                        var check = false;
                            angular.forEach($scope.mailList, function (value, key) {
                            if(check == false){
                                userDefinedMail = value.title;
                                check = true;
                            }else{
                                userDefinedMail = userDefinedMail+","+value.title; 
                            }
                        });
						
						var data = {
								smsUtilityId : smsUtilityId,
								triggerId :  $scope.firstTabLevel==1 ? $scope.common.triggerId :$scope.commonSchedule.triggerId ,
								templateDescription :  $scope.firstTabLevel==1 ? $scope.common.templateDescription :$scope.commonSchedule.scheduletemplateDescriptionForData,
								categoryId :   $scope.firstTabLevel==1 ? $scope.common.categoryId : $scope.commonSchedule.scheduleCategoryId,
								orgId :$scope.organizationId,
								unitId : $scope.unitId,
								status :  'A',
								createdBy : $scope.createdBy,
								createdDate : $scope.dt,
								updatedBy :$scope.updatedBy,
								updatedDate :$scope.dt,
								triggerName : "",
								categoryName : "",
								scheduleTypeId:$scope.firstTabLevel,
								mobileNoList:$scope.mobileNumberList,
								isEmail : false,
								userDefinedMail : userDefinedMail,
								isSMSSend : $scope.common.isSMSSend,
							    isWhatsappSend : $scope.common.isWhatsappSend,
							    whatsAppTemplateMasterId : $scope.common.whatsAppTemplateMasterId,
						}

						if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;

						} else {

							//alert($scope.common.day)
							//return false;
							var URI = BASE_URL + ROOT_URL+ "/sms-utility-config";
							console.log("Save-URI-", URI);
							console.log(" save OBJ", data);

							GenericService.serviceAction("POST", URI,data).then(function(response) {

								if (response.data.status == 'success') {
									if($scope.updateBtnFlag==true){
										growl.success("Updated Successfully",
												{
													title : 'Success!'
												});
	                                      
									}else{
										growl.success(response.data.message,
												{
													title : 'Success!'
												});
									}
								
									// $scope.$broadcast('show-errors-reset');
								
								    } else if (response.data.status == 'error') {
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
									
								   }
								$scope.initMaster();
								$scope.getSmsConfigurationList();
								$scope.mobileNumbers = [];
								$scope.scheduleSMS();
								$rootScope.stopLoader();
							});
							
						}

					} catch (e) {
						console.log("Exception-", e);
					}
				}

				$scope.showDetailsBtn = function(data) {
					$scope.configDetails = data;
				}
			
			$scope.smsConfigurationList=[];	
			$scope.smsScheduleConfigurationList=[];	
			$scope.getSmsConfigurationList=function(){
				$scope.smsConfigurationList=[];	
				$scope.smsScheduleConfigurationList=[];	
				var URI = BASE_URL + ROOT_URL+ "/sms-utility-config/"+$scope.organizationId +"/"+$scope.unitId;
				var data = { };
				GenericService.serviceAction("GET", URI, data).then(function(response) {
							if (response.data.status == "success") {
								$rootScope.stopLoader();
								$scope.pagination.data=[];
								$scope.totalSmsConfigurationList = response.data.listObject;
									angular.forEach($scope.totalSmsConfigurationList, function (value, key) {
										if(value.isEmail == null || !value.isEmail){
											if(value.scheduleTypeId==2){
												$scope.smsScheduleConfigurationList.push(value);
											}else{
												$scope.smsConfigurationList.push(value);
											}
										}
						            });
								
								 if($scope.smsConfigurationList.length > 0){            
			                         PaginationService.searchData($scope.smsConfigurationList , $scope.pagination);
			                       }
								 
								console.log("Sms config list",$scope.smsConfigurationList);
							}
						});
			}
			$scope.getSmsConfigurationList();
			
			
			
            $scope.addMailNumber = function() {
                
            	
                if($scope.mailDto.mailId == undefined || $scope.mailDto.mailId == null || $scope.mailDto.mailId == ""){
                    growl.warning('Please Enter User Defined data',{title : 'warning!'});
                    return false;
                }
                
                
				if($scope.mailDto.mailId.length==parseInt($scope.registrationFieldObj.mobileDigit)){
			        			
							}else{
								growl.error("Please enter valid mobile number",{
									title : ERROR_MSG
									});
									$('#mobileNo').focus();
									return false;
			    		}
                
                $scope.mailList.push({'title': $scope.mailDto.mailId, 'done':false})
                $scope.mailDto.mailId ="";
            }
            $scope.deleteMailId = function(index) {    
                $scope.mailList.splice(index, 1);
            }
            $scope.clearMobileTempFields = function() {
                $scope.mailList=[];
            }
            
			
			$scope.showData=function(data){
				//$rootScope.startLoader();
				$scope.saveBtnFlag = false;
				$scope.updateScope = true;
				$scope.updateBtnFlag = true;
				$scope.disableText=false;
				$scope.common.triggerName=data.triggerName;
				$scope.common.categoryName=data.categoryName;
				$scope.common.templateDescription=data.templateDescription;
				$scope.common.smsUtilityId=data.smsUtilityId;
				$scope.common.triggerId=data.triggerId.toString();
				$scope.common.categoryId=data.categoryId;
				$scope.common.isSMSSend = data.isSMSSend;
				$scope.common.isWhatsappSend = data.isWhatsappSend;
				$scope.common.whatsAppTemplateMasterId = data.whatsAppTemplateMasterId !== undefined && data.whatsAppTemplateMasterId !== null ? data.whatsAppTemplateMasterId : "";
				$scope.common.templateDesc = data.templateDesc !== undefined && data.templateDesc !== null ? data.templateDesc : "";
				$scope.common.isSMSSend = data.isSMSSend;
				$scope.getReqiredData($scope.common.triggerId);
				if(data.userDefinedMail != null){
				      var tArr = data.userDefinedMail.split(",");
		                angular.forEach(tArr, function (value, key) {
		                    $scope.mailList.push({'title': value, 'done':false})
		                });
				}
                
			}
			
			$scope.getReqiredData = function(triggerId) {

				var URI = BASE_URL + ROOT_URL+ "/requiredData/trigger/"+triggerId;
				var data = { };
				GenericService.serviceAction("GET", URI, data).then(function(response) {
							if (response.data.status == "success") {
								$rootScope.stopLoader();
								$scope.dataList = response.data.listObject;
								console.log("Datalist",$scope.dataList);
							}
						});
			}
			
			$scope.clearFields = function() {

				$scope.userDefinedString = "";
				$scope.selectedDate = "";
				$scope.common.templateDescription = "";
				$scope.commonSchedule.scheduletemplateDescriptionForData="";
			}
			
			$scope.appendString=function(selectedDate){
				
				if($scope.selectedDate==1)
					$scope.common.templateDescription += " "+$scope.userDefinedString;
				else 
					$scope.common.templateDescription += " "+$scope.selectedDate;
				
				$scope.userDefinedString = "";
				$scope.selectedDate = "";
			}
			
//			==========================================================
			$scope.mobileNumberList=[];
			$scope.appendMobileNoString=function(){				
				
					$scope.commonSchedule.schedulTtemplateDescription += " "+$scope.commonSchedule.mobileNumber+"\n";					
					$scope.mobileNumberList.push($scope.commonSchedule.mobileNumber);
					$scope.commonSchedule.mobileNumber = "";
			}
			$scope.clearMobileTempFields = function() {
				$scope.commonSchedule.schedulTtemplateDescription="";
				$scope.commonSchedule.mobileNumber = "";
				$scope.mobileNumberList=[];
			}
			console.log("mobileNumber LIst=",$scope.mobileNumberList);
			$scope.appendStringForSchedule=function(selectedDate){
				
				if($scope.selectedDate==1)
					$scope.commonSchedule.scheduletemplateDescriptionForData += " "+$scope.userDefinedString;
				else 
					$scope.commonSchedule.scheduletemplateDescriptionForData += " "+$scope.selectedDate;
				
				$scope.userDefinedString = "";
				$scope.selectedDate = "";
			}
			
			$scope.showDataForschedule=function(data){
				//$rootScope.startLoader();
				
				$scope.saveBtnFlag = false;
				$scope.updateScope = true;
				$scope.updateBtnFlag = true;
				$scope.disableTextForSchedule=false;
				$scope.commonSchedule.triggerName=data.triggerName;
				$scope.commonSchedule.categoryName=data.categoryName;
				$scope.commonSchedule.scheduletemplateDescriptionForData=data.templateDescription;
				$scope.commonSchedule.smsUtilityId=data.smsUtilityId;
				$scope.commonSchedule.triggerId=data.triggerId;
				$scope.commonSchedule.scheduleCategoryId=data.categoryId;
				if(data.categoryId==9){
					$("#scheduleLogMobileNo").removeClass("disabled");
					$("#scheduleLogMobileNo").addClass("enabled");
					
				}else{
					$("#scheduleLogMobileNo").removeClass("enabled");
					$("#scheduleLogMobileNo").addClass("disabled");
				}
				$scope.getMobileNoList(data.triggerId);
			}
			
			$scope.updateStatus = function(id, type) 
			{

				type = (type == "A") ? "A" : "I";

				var data = {
					smsUtilityId : id,
					status : type,
					updatedBy : $scope.userId,
				}
				console.log(data);
				var URI = BASE_URL + ROOT_URL
						+ "/update/status";
				GenericService
						.serviceAction("POST", URI, data)
						.then(
								function(response) {

									if (response.data.status == "success") {
										growl
												.success(
														'Updated Successfully...!!',
														{
															title : 'Success!'
														});
										
									} else {
										growl
												.error(
														'Something went wrong...!!',
														{
															title : 'Error!'
														});
									}
								});
			}
			
			$scope.getMobileNoList=function(triggerId){
				$scope.mobileNumbers=[];
				var URI = BASE_URL + ROOT_URL+ "/getMobileList/trigger/"+triggerId+"/"+$scope.organizationId+"/"+$scope.unitId;
			    $scope.mobileList=[];			    
				GenericService.serviceAction("GET", URI, "").then(function(response) {
					console.log("response",response);
					$scope.mobileList=response.data;
					angular.forEach($scope.mobileList, function (value, key) {
						var data={
								title:value.mobileNo
						}
						$scope.mobileNumbers.push(data);
		                
		            });
							/*if (response.data.status == "success") {
								$rootScope.stopLoader();
								$scope.mobileList = response.data.listObject;								
								
							}*/
						});
			}
			$scope.setFirstTabLevel = function(newTab) {
				$scope.firstTabLevel = newTab;
				
				if (newTab == '2') {
					
					console.log("second tab="+newTab);
					$scope.scheduleSMS();
					
				}
				if (newTab == '1') {
					
					console.log("firsrt tab="+newTab);
				}
				
			};

			$scope.isSetFirstTabLevel = function(tabNum) {
				return $scope.firstTabLevel === tabNum;
				
			};
			
				$scope.scheduleSMS=function(){
					$scope.commonSchedule={
							schedulTtemplateDescription:'',
							scheduletemplateDescriptionForData:'',
							scheduleCategoryId:'',
							mobileNumber:'',
							triggerId:'',
							smsUtilityId:'',
							triggerName:'',
							categoryName:''
					}
				}
				$scope.getSelectedCategoryForSchedule=function(selectedValue){
					if(selectedValue==9){
						$("#scheduleLogMobileNo").removeClass("disabled");
						$("#scheduleLogMobileNo").addClass("enabled");
						
					}else{
						$("#scheduleLogMobileNo").removeClass("enabled");
						$("#scheduleLogMobileNo").addClass("disabled");
					}
				}
				$scope.mobileNumbers = [];

				$scope.addMobileNumber = function() {
						if($scope.newmobileNumber.length==parseInt($scope.registrationFieldObj.mobileDigit)){
		        			
						}else{
							growl.error("Please enter valid mobile number",{
								title : ERROR_MSG
								});
								$('#mobileNo').focus();
								return false;
	        		}
					$scope.mobileNumbers.push({'title': $scope.newmobileNumber, 'done':false})
					$scope.newmobileNumber = ''
					$("#addMobBtn").removeClass("enabled");
					$("#addMobBtn").addClass("disabled");
				}
				$scope.getEnabledAddBtn=function(number){
					if(number.length==parseInt($scope.registrationFieldObj.mobileDigit)){
						$("#addMobBtn").removeClass("disabled");
						$("#addMobBtn").addClass("enabled");
					}else{
						$("#addMobBtn").removeClass("enabled");
						$("#addMobBtn").addClass("disabled");
					}
				}
				$scope.deleteMobileNumber = function(index) {	
					$scope.mobileNumbers.splice(index, 1);
				}
				console.log("mobile no=",$scope.mobileNumbers);

                $scope.whatsAppTemplateList =[];
				$scope.getWhatsAppTemplateList = function(){
				    if($scope.whatsAppOption !== undefined && $scope.whatsAppOption === 4){
                        var URI = BASE_URL + ROOT_URL+ "/getWhatsAppTemplateList/"+$scope.organizationId+"/"+$scope.unitId;
                        var data = { };
                        GenericService.serviceAction("GET", URI, data).then(function(response) {
                            if (response.data.status == "success") {
                                $rootScope.stopLoader();
                                $scope.whatsAppTemplateList = response.data.listObject;
                                console.log("WhatsApp Template list",$scope.whatsAppTemplateList);
                            }
                            else{
                                $rootScope.stopLoader();
                            }
                        });
                    }
				}
				$scope.getWhatsAppTemplateList();

			} ]);