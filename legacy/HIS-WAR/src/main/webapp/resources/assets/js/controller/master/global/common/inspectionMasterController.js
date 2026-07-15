/**
 * 
 */

angular.module('myApp')
		.controller("inspectionMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl', '$filter',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl, $filter){
	
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
	
			/**Variables for pagination */
			$scope.offset = 0;
			$scope.noOfRecordsPerPage = 10;
			$scope.commonList = [];
			$scope.totalNoOfRecords;
			$scope.searchKeyword;
			$scope.commonListCount;
			var cookieObject = $cookies.getObject('cookieObject');
			if(cookieObject == undefined){
			$state.go('login');
			return;
			}

			$scope.orgUnitId = cookieObject.unitId;
			$scope.orgId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			
			$scope.selectedSpecialityType = [];
			
			$scope.initInspectionMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
					instructionId:"",
					instructionCode:"",
					instructionDesc :"",
					isMedication :"N"
					
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initInspectionMaster();			
			
			/**Crud Operations Start**/
			
			/**List And Pagination Start**/
			$scope.setNoOfRecords = function() {
				$scope.initInspectionMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
			};
			
			$scope.initInspectionMasterList = function(orgId,offset, noOfRecordsPerPage)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
								organizationId:orgId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage
							};
				
				var data2 = {
								organizationId:orgId
							};
				
				var URI1 = BASE_URL + ROOT_URL +"/api/common/getInspectionMasterList"; 
				var URI2 = BASE_URL + ROOT_URL + "/api/common/getInspectionMasterCount";
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("LISt ",$scope.commonList);
					}
				});
				
				GenericService.serviceAction("POST",URI2,data2).then(function(response){
					if(response.data.status == "success"){					
						$rootScope.stopLoader();
						$scope.commonListCount = response.data.object;
						$scope.setPage(1,false);
						console.log("commonListCount",response.data.object);
					}
				});
				
			}		
			
			$scope.getInspectionList = function(orgId,offset, noOfRecordsPerPage) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organizationId:orgId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/common/getInspectionMasterList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
				});
			}

			$scope.LanguObj={
					languageId: 1,
					desc:"English"
				};
			$scope.assignToLang = function(){
				$scope.Obj.convertedText = $scope.common.instructionDesc;
			}
	/*		$scope.onclickAjax = function() {
				$rootScope.startLoader();
				var url="http://www.apertium.org/apy/translate?q=hello%20world&langpair=en|es";
		          $http.get(url, data, config)
		           .then(function (response)  
		        	{
		        	   $scope.responseData = [];
		        	   $scope.responseData = responseData;
		        	});
				var URI="https://www.apertium.org/apy/translate?q=hello%20world&langpair=en|es";
//		          $https.get(url, data, config)
				
//				var URI = BASE_URL + ROOT_URL + "/LIS/global/getTotalLanguageMasterRecordActive"+"/"+ $scope.orgId+"/"+ $scope.orgUnitId;
				GenericService.serviceAction("GET", URI).then(function(responseData) {
					$scope.responseData = responseData;
//					$scope.languageList = response.data.listObject;
				});
			}*/
			
			
			$scope.getLanguageList = function() {
				$rootScope.startLoader();
				
				var data = {
						organizationId:$scope.orgId,
						unitId:	$scope.orgUnitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/LIS/global/getTotalLanguageMasterRecordActive"+"/"+ $scope.orgId+"/"+ $scope.orgUnitId;
				console.log("URI", URI);
				GenericService.serviceAction("GET", URI).then(function(response) {
					$rootScope.stopLoader();
					$scope.languageList = [];
//					$scope.languageList = response.data.listObject;
					$scope.languageList = [
						{
							desc:'English',
							languageId:'1',
							lg:'en'
								}, 
						{   desc:'Marathi',
							languageId:'2',
							lg:'mr'
								},
						{   desc:'Hindi',
						    languageId:'3',
							lg:'hi'
								},
					
						{   desc:'Arabic',
							languageId:'4',
							lg:'ar'
								},
						{   desc:'Russian',
							languageId:'5',
							lg:'ru'
								},
						{   desc:'Spanish',
							languageId:'5',
							lg:'sp'
								},
						]
					console.log("language list" , $scope.languageList);
				});
			}
//			$scope.getLanguageList();
			
			$scope.addedLanguageList = [];
			$scope.Obj={};
//			$scope.Obj={
//					languageId: "1_English",
//					convertedText:""
//				};

			$scope.tempList = [];
			$scope.addLanguageDetails = function(obj) {
			    console.log("obj==========,",obj)
				var newArr = {};
				var addedLangList = [];
				$scope.tempList = [];
//				var Data= $scope.Obj.languageId.split('_');
//				newArr.desc = Data[1];
//				newArr.languageId =  Data[0];
//				newArr.convertedText = $scope.Obj.convertedText;
//				$scope.tempList.push(newArr);
//              angular.copy($scope.tempList, addedLangList);
				var jsonObj = JSON.parse(obj);
                newArr.desc = jsonObj.desc;
                newArr.languageId =  jsonObj.id;
                newArr.convertedText = $scope.Obj.convertedText;

                if(jsonObj.id==null || jsonObj.id==undefined || jsonObj.id==""){
                    growl.error("Please select language", {
                        title: 'Error!'
                    });
                    $scope.LanguObj = "";
                    $rootScope.stopLoader();
                    return;
                }

                if($scope.Obj.convertedText==null || $scope.Obj.convertedText==undefined || $scope.Obj.convertedText==""){
                    growl.error("Please fill-up text", {
                        title: 'Error!'
                    });
                    $rootScope.stopLoader();
                    return;
                }

                $scope.tempList.push(newArr);
                angular.copy($scope.tempList, addedLangList);

//               addedLangList.forEach(function (item) {
//                 var boolean = $filter('filter')($scope.addedLanguageList, {
//                 languageId: parseInt(item.languageId)
//                 }, true);
//
//                 if (boolean.length === 0) {
//                    $scope.addedLanguageList.push(item);
//                 }
//                 else{
//                    growl.error("Already Exist..");
//                 }
//               });

                if($scope.addedLanguageList !== undefined && $scope.addedLanguageList !== null && $scope.addedLanguageList.length>0){
                     var boolean = $filter('filter')($scope.addedLanguageList, {
                        languageId: parseInt(newArr.languageId)
                     }, true);

                     if (boolean.length === 0) {
                        $scope.addedLanguageList.push(newArr);
                     }
                      else{
                         growl.error("Already Exist..");
                      }
                }else{
                    $scope.addedLanguageList.push(newArr);
                }

				$scope.LanguObj={
						languageId: "",
						desc:""
					};
				$scope.convertedText = "";
			}
			
			
			$scope.removeLanguage = function(index) {
				$scope.addedLanguageList.splice(index,1);
			}
			
			
			$scope.convertLan = function(obj) {
				
				var langs = obj;
				$translate.use(lang);
				$('instruction').translate('langs');
			}
			
            $scope.defaultSet = function(obj) {
				
            	var newArr = {};
				newArr.desc = "English";
				newArr.languageId = "1";
				newArr.convertedText = $scope.common.instructionDesc;
				$scope.addedLanguageList.push(newArr);
			}
			$scope.pager = {};
			$scope.page;

			$scope.setPage = function(page, flag) {
				if (page < 1 || page > $scope.pager.totalPages)
				{
					return;
				}
				$scope.pager = PagerService.GetPager($scope.commonListCount, page, $scope.noOfRecordsPerPage);
				if (flag)
				{
					$scope.getInspectionList($scope.orgId ,$scope.pager.startIndex, $scope.pager.pageSize);
				}
			}

			$scope.initInspectionMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
			
			/**List And Pagination End**/
			$scope.deptSpecilityId=[];
			$scope.save = function(){
				 console.log($scope.selectedSpecialityType)
				
				
				try{
					
					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('YYYY-MM-DD HH:mm:ss');
					if($scope.common.instructionCode == undefined || $scope.common.instructionCode == "" || $scope.common.instructionCode == null){
						$rootScope.stopLoader();
						growl.warning("Please Enter Instruction Code",
								{
									title : 'Warning!'
								});
						return false;
					}
					
					if($scope.common.instructionDesc == undefined || $scope.common.instructionDesc == "" || $scope.common.instructionDesc == null){
						$rootScope.stopLoader();
						growl.warning("Please Enter Instruction Description",
								{
									title : 'Warning!'
								});
						return false;
					}
					var inspectionObj = {
							instructionCode : $scope.common.instructionCode,
							instructionDesc : $scope.common.instructionDesc,
							isMedication 	:  $scope.common.isMedication,
							createdBy:$scope.createdBy,
							updatedBy:$scope.updatedBy,
							createdDate:$scope.dt,
							unitId:$scope.orgUnitId,
							status:'A',
							organizationId:$scope.orgId,
							updatedDate:$scope.dt,
							deptSpecilityId:[],
							addedLanguageList : [],
					}
					 inspectionObj.deptSpecilityId = $scope.selectedSpecialityType;
					 inspectionObj.addedLanguageList = $scope.addedLanguageList ;
					console.log(inspectionObj)
					
					var URI = BASE_URL + ROOT_URL + "/api/common/saveInspectionMaster";
					console.log("Save-URI-",URI);
					console.log("OBJ",inspectionObj);
					
					GenericService.serviceAction("POST",URI,inspectionObj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.selectedSpecialityType=[];
							$scope.addedLanguageList=[];
							$scope.initInspectionMaster();
							$scope.initInspectionMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			$scope.showUpdateBtn = function(id){
				try{
					//$rootScope.startLoader();
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					
					var URI = BASE_URL + ROOT_URL +"/api/common/getInspectionMasterListById/"+id+"/"+$scope.orgId;
					console.log("EDIT",URI);
					GenericService.serviceAction("GET",URI).then(function(response){
						console.log("Edit",response);
						
						if(response.data.status == "success"){
							$rootScope.stopLoader();
							$scope.common =  response.data.object;
							$scope.selectedSpecialityType=response.data.listObject;
							$scope.addedLanguageList=response.data.resObject;
							console.log($scope.common)
							
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.update = function(){
				try{
					
					$rootScope.startLoader();
					var date = new Date();
					$scope.dt = moment(date).format('YYYY-MM-DD HH:mm:ss');
					
					if($scope.common.instructionCode == undefined || $scope.common.instructionCode == "" || $scope.common.instructionCode == null){
						$rootScope.stopLoader();
						growl.warning("Please Enter Instruction Code",
								{
									title : 'Warning!'
								});
						return false;
					}
					
					if($scope.common.instructionDesc == undefined || $scope.common.instructionDesc == "" || $scope.common.instructionDesc == null){
						$rootScope.stopLoader();
						growl.warning("Please Enter Instruction Description",
								{
									title : 'Warning!'
								});
						return false;
					}
					
					var instructionObj = {
							instructionCode :  $scope.common.instructionCode,
							instructionDesc :  $scope.common.instructionDesc,
							isMedication 	:  $scope.common.isMedication,
							instructionId 	:  $scope.common.instructionId,
							createdBy       :  $scope.createdBy,
							updatedBy		:  $scope.updatedBy,
							organizationId  :  $scope.orgId,
							unitId			:  $scope.orgUnitId,
							updatedDate		:  $scope.dt,
							createdDate:$scope.dt,
							deptSpecilityId :[],
							addedLanguageList : [],
					}
					instructionObj.deptSpecilityId = $scope.selectedSpecialityType;	
					instructionObj.addedLanguageList = $scope.addedLanguageList ;
					
					var URI = BASE_URL + ROOT_URL + "/api/common/updateInspectionMaster";
					console.log("Update-URI-",URI);
					console.log("OBJ",instructionObj);
					
					GenericService.serviceAction("POST",URI,instructionObj).then(function(response){
						
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
							$scope.selectedSpecialityType=[];
							$scope.addedLanguageList=[];
							$scope.initInspectionMaster();
							$scope.initInspectionMasterList($scope.orgId,$scope.offset, $scope.noOfRecordsPerPage);
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
			
			
			$scope.getSpecialityMasterList = function(){
				try{
					var URI = BASE_URL + ROOT_URL + GETSPECIALITYLIST;
					var obj = {
								organizationId : $scope.orgId,
								unitId         :$scope.orgUnitId
						    }
					GenericService.
					serviceAction("POST",URI, obj)
					.then(function(response){
						console.log(response);
						if(response.data.status == "success")
							$scope.specialityMasterList=[];
						console.log($scope.specialityMasterList)
						$scope.getIcpcDesciptionList = [];
						$scope.dumyList= [];
					angular.forEach(response.data.listObject,
										function(value,index) {
											var data = {};
											data.id = value.specialityId;
											data.label = value.specialityName;
											console.log("data",JSON.stringify(data));
												   $scope.specialityMasterList.push(data);
												  
												
										});
						
						
					});
				}catch (e) {
					console.log("Exception",e.message);
				}
			}
			$scope.getSpecialityMasterList();
			
			$scope.showFieldList = function(id){
				try{
					$scope.fieldMasterList=[];
					$scope.saveBtnFlag = false;
					$scope.updateScope = true;
					$scope.updateBtnFlag = true;
					var data=
					     {
							organizationId : $scope.orgId,
							unitId         :$scope.orgUnitId,
							instructionId :id
					    }
					var URI = BASE_URL + ROOT_URL + "/api/common/getDeptSpecilityById";
					
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if(response.data.status == "success"){
 							$scope.fieldMasterList= response.data.listObject;
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
		
			$scope.showLanguageList = function(id){
				try{
					$scope.languageMasterList=[];
					var data=
					     {
							organizationId : $scope.orgId,
							unitId         :$scope.orgUnitId,
							instructionId :id
					    }
					var URI = BASE_URL + ROOT_URL + "/api/common/getConvertedLanguageListById";
					
					GenericService.serviceAction("POST",URI,data).then(function(response){
						
						if(response.data.status == "success"){
 							$scope.languageMasterList= response.data.listObject;
						}else if(response.data.status == 'error'){
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
					
				}catch(e){
					console.log("Exception-",e);
				}
			}
		
			
			$scope.updateStatus = function(id,status){
				try{
					var date = new Date();
					$scope.dt = moment(date).format('DD-MM-YYYY HH:mm:ss');
					
					var visitObj = {
							instructionId 	: 	id,
							status			:	status,
							updatedDate		:	$scope.dt,
							updatedBy		:	$scope.updatedBy
					}
					var URI = BASE_URL + ROOT_URL + "/api/common/updateInspectionMasterStatus";
					
					GenericService.serviceAction("POST",URI,visitObj).then(function(response){
						if (response.data.status == 'success')
						{
							$rootScope.stopLoader();
							growl.success(response.data.message,
									{
										title : 'Success!'
									});
							$scope.$broadcast('show-errors-reset');
						} else if(response.data.status == 'error')
						{
							$rootScope.stopLoader();
							growl.error(response.data.message,
									{
										title : 'Error!'
									});
						}
					});
				}catch(e){
					console.log("Exception",e);
				}
			}

            $scope.languageList = [];
			$scope.getLanguageMasterList = function() {
                var data = "";
                $rootScope.startLoader();
                var URI = BASE_URL + ROOT_URL + LIS_GLOBAL + "/listLanguageMaster/" + $scope.orgId + "/" + $scope.orgUnitId;
                console.log("URI", URI);
                $scope.languageList = [];
                GenericService.serviceAction("GET", URI, data).then(function(response) {
                    $rootScope.stopLoader();
                    $scope.languageList = response.data.listObject;
                });
            }
			$scope.getLanguageMasterList();
			/**Crud Operations End**/
			
			// Set the original/default language
			
}]);