angular
.module('myApp')
.controller(
		"mailUtilityConfigurationController",
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
				/** Variables for pagination */
				$scope.offset = 0;
				$scope.noOfRecordsPerPage = 10;
				$scope.commonList = [];
				$scope.totalNoOfRecords;
				$scope.searchKeyword;
				$scope.commonListCount;
				$scope.selectedDate=null;
				$scope.userDefinedString=null;
				$scope.mailList = [];	
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
							mailSubject : "",
							isWhatsappSend :"N",
							criticalReport :"N",
							
					};
				}
				$scope.initMaster();
				
				// function for popup ok button call
				$scope.PopupOkBtn = function() {
					$scope.popUpFlag = false;
					$scope.save();
				}

				$scope.triggerList=[];
				$scope.scheduleTriggerList=[];
				$scope.getTriggerList = function() {
					var URI = BASE_URL + ROOT_URL+ "/trigger-master";
				    var data={};
					GenericService.serviceAction("GET", URI, data).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									if(response.data.listObject!=null && response.data.listObject.length>0)
										angular.forEach(response.data.listObject, function (value, key) {
											if(value.triggerType == 1 && value.isEmail){
												$scope.triggerList.push(value);
											}
											if(value.triggerType == 2 && value.isEmail){
												$scope.scheduleTriggerList.push(value);
											}
										});
									}
							});
				}
				$scope.getTriggerList();
				$scope.common.type;
				$scope.categoryList=[];
				$scope.scheduleCategoryList=[];
				$scope.getCategoryMasterList = function() {
					var URI = BASE_URL + ROOT_URL+ "/category-master";
					var data = { };
					GenericService.serviceAction("GET", URI, data).then(function(response) {
								if (response.data.status == "success") {
									$rootScope.stopLoader();
									if(response.data.listObject!=null && response.data.listObject.length>0){
										angular.forEach(response.data.listObject, function (value, key) {
											if(value.categoryType == 1){
												$scope.categoryList.push(value);
											}if(value.categoryType == 2){
												$scope.scheduleCategoryList.push(value);
											}
										});
									}
								}
							});
				}
				$scope.getCategoryMasterList();

				$scope.save = function() {
					try {
						if ($scope.popUpFlag == false) {
							$rootScope.startLoader();
						}
						
						if($scope.common.triggerId ==undefined || $scope.common.triggerId == null || $scope.common.triggerId == ""){
							growl.warning("Please Select Trigger",
									{
										title : 'warning!'
									});
							return false;
						}
						
						if($scope.common.categoryId ==undefined || $scope.common.categoryId == null || $scope.common.categoryId == ""){
							growl.warning("Please Select Category",
									{
										title : 'warning!'
									});
							return false;
						}
						
						if($scope.common.mailSubject ==undefined || $scope.common.mailSubject == null || $scope.common.mailSubject == ""){
							growl.warning("Please Enter Mail Subject",
									{
										title : 'warning!'
									});
							return false;
						}
						
						if($scope.common.templateDescription ==undefined || $scope.common.templateDescription == null || $scope.common.templateDescription == ""){
							growl.warning("Please Enter Mail Body",
									{
										title : 'warning!'
									});
							return false;
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
						
						var date = new Date();
						$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
						
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
								smsUtilityId :  $scope.common.smsUtilityId ,
								triggerId : $scope.common.triggerId  ,
								templateDescription : $scope.common.templateDescription ,
								categoryId :  $scope.common.categoryId ,
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
								isEmail : true,
								mailSubject : $scope.common.mailSubject,
								isWhatsappSend : $scope.common.isWhatsappSend,
								criticalReport : $scope.common.criticalReport,
								userDefinedMail : userDefinedMail,
								type:$scope.common.type,
								whatsAppTemplateMasterId : $scope.common.whatsAppTemplateMasterId,
						}

						if ($scope.popUpFlag) {
							$scope.showModal = !$scope.showModal;

						} else {
							var URI = BASE_URL + ROOT_URL+ "/sms-utility-config";

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
								    } else if (response.data.status == 'error') {
									growl.error(response.data.message,
											{
												title : 'Error!'
											});
									
								   }
								$scope.initMaster();
								$scope.getMailConfigurationList();
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
			
			$scope.mailConfigurationList=[];
			$scope.mailConfigurationScheduledList=[];
			$scope.getMailConfigurationList=function(){
				$scope.mailConfigurationList=[];	
				var URI = BASE_URL + ROOT_URL+ "/sms-utility-config/"+$scope.organizationId +"/"+$scope.unitId;
				var data = { };
				GenericService.serviceAction("GET", URI, data).then(function(response) {
							if (response.data.status == "success") {
								$rootScope.stopLoader();
								
								$scope.pagination.data=[];
								$scope.mailConfigurationScheduledList=[]
								if(response.data.listObject!=null && response.data.listObject.length>0){
									angular.forEach(response.data.listObject, function (value, key) {
										if(value.scheduleTypeId == 1 && value.isEmail){
											$scope.mailConfigurationList.push(value);
										}
										if(value.scheduleTypeId == 2 && value.isEmail){
											$scope.mailConfigurationScheduledList.push(value);
										}
						            });
								}
								
								 if($scope.mailConfigurationList.length > 0){            
			                         PaginationService.searchData($scope.mailConfigurationList , $scope.pagination);
			                       }
							}
						});
			}
			$scope.getMailConfigurationList();
			
			$scope.addMailNumber = function() {
				
				if($scope.mailDto.mailId == undefined || $scope.mailDto.mailId == null || $scope.mailDto.mailId == ""){
					growl.warning('Please Enter User Defined data',{title : 'warning!'});
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
				$scope.saveBtnFlag = false;
				$scope.updateScope = true;
				$scope.updateBtnFlag = true;
				$scope.disableText=false;
				$scope.common.triggerName=data.triggerName;
				$scope.common.categoryName=data.categoryName;
				$scope.common.templateDescription=data.templateDescription;
				$scope.common.smsUtilityId=data.smsUtilityId;
				$scope.common.triggerId=data.triggerId;
				$scope.common.categoryId=data.categoryId;
				$scope.common.isWhatsappSend = data.isWhatsappSend;
				$scope.common.criticalReport = data.criticalReport;
				$scope.common.type = data.type;
				if(data.isEmail){
					$scope.common.mailSubject = data.mailSubject;
				}	
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
							}
						});
			}
			
			$scope.clearFields = function() {
				$scope.userDefinedString = "";
				$scope.selectedDate = "";
				$scope.common.templateDescription = "";
			}
			
			$scope.appendString=function(selectedDate){
				if($scope.selectedDate==1){
					if($scope.userDefinedString == undefined || $scope.userDefinedString == null || $scope.userDefinedString == ""){
						growl.warning('Please Enter User Defined data',{title : 'warning!'});
						return false;
					}
					$scope.common.templateDescription += " "+$scope.userDefinedString;
				}else {
					$scope.common.templateDescription += " "+$scope.selectedDate;
				}
				$scope.userDefinedString = "";
				$scope.selectedDate = "";
			}
			
//			==========================================================
			
			$scope.updateStatus = function(id, type) 
			{
				type = (type == "A") ? "A" : "I";

				var data = {
					smsUtilityId : id,
					status : type,
					updatedBy : $scope.userId,
				}
				var URI = BASE_URL + ROOT_URL + "/update/status";
				GenericService.serviceAction("POST", URI, data)
						.then(function(response) {
									if (response.data.status == "success") {
										growl.success('Updated Successfully...!!',{title : 'Success!'});
									} else {
										growl.error('Something went wrong...!!',{title : 'Error!'});
									}
								});
			}
			
			$scope.isSetFirstTabLevel = function(tabNum) {
				return $scope.firstTabLevel === tabNum;
				
			};

			$scope.setFirstTabLevel = function(newTab) {
				$scope.firstTabLevel = newTab;
				if (newTab == '1') {
					$scope.isSetFirstTabLevel(newTab)
				}
				
			};

            $scope.whatsAppTemplateList =[];
            $scope.getWhatsAppTemplateList = function(){
                if($scope.whatsAppOption !== undefined && $scope.whatsAppOption === 4){
                    var URI = BASE_URL + ROOT_URL+ "/getWhatsAppTemplateList/"+$scope.organizationId+"/"+$scope.unitId;
                    var data = { };
                    GenericService.serviceAction("GET", URI, data).then(function(response) {
                        if (response.data.status == "success") {
                            $rootScope.stopLoader();
                            $scope.whatsAppTemplateList = response.data.listObject;
                            console.log("Category list",$scope.whatsAppTemplateList);
                        }
                        else{
                            $rootScope.stopLoader();
                        }
                    });
                }
            }
            $scope.getWhatsAppTemplateList();
		
			} ]);