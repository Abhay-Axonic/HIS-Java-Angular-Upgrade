/**
 * 
 */

angular.module('myApp')
		.controller("userAuthenticationMasterController",
		['$scope','$rootScope','$state','$cookies','GenericService','PagerService','promiseFactory','$sessionStorage','growl','BillingGenericService','PaginationService',
		function($scope,$rootScope,$state, $cookies, GenericService,PagerService,promiseFactory,$sessionStorage,growl,BillingGenericService,PaginationService){
			$rootScope.loginpage = true;
			$scope.saveBtnFlag = true;
			$scope.updateBtnFlag = false;
			$scope.updateScope = false;
			$scope.popUpFlag = true;
	
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
			$scope.isSuperUser=cookieObject.isSuperUser;
			$scope.unitId = cookieObject.unitId;
			$scope.organisationId = cookieObject.organizationId;
			$scope.userId = cookieObject.userId;
			$scope.createdBy =cookieObject.userId;
			$scope.updatedBy =cookieObject.userId;
			$scope.dateOptions = {
				formatYear : 'yyyy',
				showWeeks : false,
				minDate : new Date()
			};

			$scope.open = function() {
				$scope.popup.opened = true;
			};
			
			$scope.popup =
			{
				opened : false
			};
			
			$scope.selectedRoleList=[];	
			$scope.initMaster = function(){
				$scope.saveBtnFlag = true;
				$scope.updateBtnFlag = false;
				$scope.common = {
						roleId:"",
						userId:"",
						empCode:"",
						empName:"",
						roleName:"",
			
				};
				console.log("Common-init",$scope.common);
			}
			$scope.initMaster();
			
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
				
			
			$scope.getListByUserName = function(name){
				
				var URI = BASE_URL + ROOT_URL + "/api/global/searchEmployeeByName";
				var data = {
						 organizationId:$scope.organisationId,
					      unitId:$scope.unitId,
					      firstName:name
					}
				return GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == "success")
							console.log("getListByUserName", response.data.listObject);
						return response.data.listObject;
					});
						
				}
				
			
			$scope.selectUserId = function(info, model,label) {
				console.log(info);
				$scope.common.userId = info.userId;
				$scope.common.empName = info.empName;
			}
			$scope.getPassword = function(password)
			{
                $scope.viewPassword=password;
			}
			//search by role name
			$scope.getListByRoleName = function(name){
				
				var URI = BASE_URL + ROOT_URL + "/api/global/searchRoleByName";
				var data = {
						  organisationId:$scope.organisationId,
					      unitId:$scope.unitId,
					      roleName:name
					}
				return GenericService.serviceAction("POST",URI,data).then(function(response){
						if (response.data.status == "success")
							console.log("getListByRoleName", response.data.listObject);
						return response.data.listObject;
					});
						
				}
				
			
			$scope.selectRoleId = function(info, model,label) {
				console.log(info);
				$scope.common.roleId = info.roleId;
				$scope.common.roleName = info.roleName;
			}
			
			$scope.getUserAuthenticatedList=function(){
					var data1 = {
							organisationId:$scope.organisationId,
							unitId:$scope.unitId,
							roleName:$scope.common.roleName,
							empName:$scope.common.empName
						};
		
		
					var URI1 = BASE_URL + ROOT_URL +"/api/global/getUserAuthenticationList";			
		
					GenericService.serviceAction("POST",URI1,data1).then(function(response){
						if(response.data.status == "success"){
							$rootScope.stopLoader();	
							$scope.pagination.data = [];
							$scope.commonList = response.data.listObject;
							
							if($scope.commonList.length > 0){            
		                         PaginationService.searchData($scope.commonList, $scope.pagination);
		                       }
							
							console.log("commonList",$scope.commonList);
							}
						});
			}
			$scope.getUserAuthenticatedList();
			
			
			/**Crud Operations Start**/
		
			/**List And Pagination Start**//*
			$scope.setNoOfRecords = function() {
				$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			};
			
			$scope.initMasterList = function(organisationId,offset, noOfRecordsPerPage,unitId)
			{
				$rootScope.startLoader();
				
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;				
				var data1 = {
								organisationId:organisationId,
								offset:offset,
								noOfRecordsPerPage:noOfRecordsPerPage,
								unitId:unitId,
								roleName:"",
								empName:""
							};
				
				var data2 = {
						      organisationId:organisationId,
						      unitId:unitId
							};
				
				var URI1 = BASE_URL + ROOT_URL +"/api/global/getUserAuthenticationList"; 
				//temp use the mappercount api
				var URI2 = BASE_URL + ROOT_URL + "/api/global/getUserRoleMapperCount";
					
				
				GenericService.serviceAction("POST",URI1,data1).then(function(response){
					if(response.data.status == "success"){
					$rootScope.stopLoader();					
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
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
			
			$scope.masterList = function(organisationId,offset, noOfRecordsPerPage,unitId) {
				$rootScope.startLoader();
				console.log("offset", offset);
				offset = offset != null ? offset : 0;
				noOfRecordsPerPage = noOfRecordsPerPage != null ? noOfRecordsPerPage : 10;
				
				var data = {
						organisationId:organisationId,
						offset:offset,
						noOfRecordsPerPage:noOfRecordsPerPage,
						unitId:unitId
						};
				
				var URI = BASE_URL + ROOT_URL + "/api/global/getUserRoleMapperList";
				console.log("URI", URI);
				GenericService.serviceAction("POST", URI, data).then(function(response) {
					$rootScope.stopLoader();
					$scope.commonList = response.data.listObject;
					console.log("commonList",$scope.commonList);
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
					$scope.masterList($scope.organisationId ,$scope.pager.startIndex, $scope.pager.pageSize,$scope.unitId);
				}
			}

			$scope.initMasterList($scope.organisationId,$scope.offset, $scope.noOfRecordsPerPage,$scope.unitId);
			
			*//**List And Pagination End**/
			
			// function for popup ok button call
			$scope.PopupOkBtn = function() {
				$scope.popUpFlag = false;
				$scope.save();
			}
			//send mail to user 
			$scope.sendMail=function(info){
				var data={				
						emailId:info.emailId,
						empCode:info.empCode,
						password:info.password,
						firstName:info.firstName												
				}
				var URI = BASE_URL + ROOT_URL + "/api/global/sendPasswordToUser";
				console.log("sendMail-URI-",URI);
				console.log(" sendMail OBJ",data);// return false;  
				$rootScope.startLoader();
				GenericService.serviceAction("POST",URI,data).then(function(response){
					
					if (response.data.status == 'success')
					{
						$rootScope.stopLoader();
						growl.success(response.data.message,
								{
									title : 'Success!'
								});
						
					} else if(response.data.status == 'error')
					{
						$rootScope.stopLoader();
						growl.error(response.data.message,
								{
									title : 'Error!'
								});
					}
					$rootScope.stopLoader();

				});
			}
			
			$rootScope.resendPassword = function (userObj) {
				$rootScope.startLoader();
				var data ={
						
						"password":userObj.password,
						"userName":	userObj.userName,
						"emailId":	userObj.emailId,
				}
				var URI = BASE_URL + ROOT_URL + "/adt/resendUserNameAndPassword";
				
				GenericService.serviceAction("POST", URI, data).then(function (response) {
					if (response.data.status == "success") 
					{
						growl.success("UserName and Password Resend Successfully.",{
							title : SUCCESS_MSG
						});
						$rootScope.stopLoader();
					}
					else if (response.data.status == "error") {
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$rootScope.stopLoader();
					}

				});
			}
			
			$rootScope.resendUserName = function (userObj) {
				$rootScope.startLoader();
				var data ={
						
						"userName":	userObj.userName,
						"emailId":	userObj.emailId,
				}
				var URI = BASE_URL + ROOT_URL + "/adt/resendUserName";
				
				GenericService.serviceAction("POST", URI, data).then(function (response) {
					if (response.data.status == "success") 
					{
						growl.success("UserName Resend Successfully.",{
							title : SUCCESS_MSG
						});
						$rootScope.stopLoader();
					}
					else if (response.data.status == "error") {
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$rootScope.stopLoader();
					}

				});
			}
			
			  $scope.validatePassword = function(password) {
			      var minLength = 8;
			      var maxLength = 16;
			      var hasUppercase = /[A-Z]/.test(password);
			      var hasLowercase = /[a-z]/.test(password);
			      var hasDigit = /[0-9]/.test(password);
			      var hasSpecialChar = /[!@#$%^&*(),.?":{}|<>]/.test(password);
			      var isLengthValid = password.length >= minLength && password.length <= maxLength;;

			      return hasUppercase && hasLowercase && hasDigit && hasSpecialChar && isLengthValid;
			    };
			
			
			
			$scope.passwordCheck = 'N';
			$scope.pwdExpiryCheck = 'N';
			$scope.password = "";
			$scope.pwdExpiry = "";
			$scope.userIdForChanges = 0;
			$scope.updateUserPassword = function () {
				
				var testMsg = "";
				var testVariable = false;
				$scope.isPasswordValid = true;
				var data ={}
				if($scope.passwordCheck == 'Y'){
					data.password = $scope.password;
					testMsg = "Password Changed Successfully";
					testVariable = true;
					
					$scope.isPasswordValid = $scope.validatePassword(this.password);
	
				}
				
				 if(!$scope.isPasswordValid){
				    	
				    	growl.warning("Password must be 8 to 16 characters long and include uppercase letters, lowercase letters, digits, and special characters.",{
							title : ERROR_MSG
						});
				    	
				    	return false;
				    	
				    }
				 $rootScope.startLoader();
				
				if($scope.pwdExpiryCheck == 'Y'){
					data.pwdExpiry = moment($scope.pwdExpiry).format('DD-MM-YYYY');
					if(testVariable){
						testMsg = "Password and Expiry Date Changed Successfully";
					}else{
						testMsg = "Expiry Date Changed Successfully";
					}
				}
				
				if($scope.passwordCheck == 'N' && $scope.pwdExpiryCheck == 'N' ){
					growl.warning("Please Select Atleast One option",
							{
								title : 'Warning!'
							});
					$rootScope.stopLoader();
					return false;
				}
				
				
				if($scope.passwordCheck == 'Y'){
					if($scope.password == ''){
						
					growl.warning("Please Enter New Password",
							{
								title : 'Warning!'
							});
					$rootScope.stopLoader();
					return false;
					}
				}
				
				if( $scope.pwdExpiryCheck == 'Y' ){
					if($scope.pwdExpiry == ''){
						growl.warning("Please Enter Expiry Date",
								{
									title : 'Warning!'
								});
						$rootScope.stopLoader();
						return false;
					}
				}

				data.userId=$scope.userIdForChanges;
                data.unitId=$scope.unitId;
                data.organizationId=$scope.organisationId;
				
				var URI = BASE_URL + ROOT_URL + "/adt/updatePassword";
				
				GenericService.serviceAction("POST", URI, data).then(function (response) {
					if (response.data.status == "success") 
					{
						growl.success(testMsg,{
							title : SUCCESS_MSG
						});
						$rootScope.stopLoader();
						$("#changePasswordExpiryModal").modal("hide");
						$scope.passwordCheck = 'N';
						$scope.pwdExpiryCheck = 'N';
						$scope.password = "";
						$scope.pwdExpiry = "";
						$scope.getUserAuthenticatedList();
					}
					else if (response.data.status == "error") {
						growl.warning(response.data.message,{
							title : ERROR_MSG
						});
						$rootScope.stopLoader();
					}

				});
			}
			
			$scope.getUserId = function(info){
				$scope.userIdForChanges =  info.userId;
			}
			
			$scope.cancelUserPassword = function(){
				$scope.passwordCheck = 'N';
				$scope.pwdExpiryCheck = 'N';
				$scope.password = "";
				$scope.pwdExpiry = "";

			}
			
			/**Crud Operations End**/
}]);