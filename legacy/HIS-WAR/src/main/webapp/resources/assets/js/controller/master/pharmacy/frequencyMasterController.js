(function() {
  "use strict";
  function frequencyMasterController(
    $scope,
    $rootScope,
    $http,
    $filter,
    CONSTANTS,
    $cookies,
    GenericService,
    growl,
    PaginationService
  ) {
    var self = this;

    //Default Constructor
    var cookieObject = $cookies.getObject('cookieObject'); if(cookieObject == undefined){ $state.go('login'); return;}$scope.unitId = cookieObject.unitId;$scope.organizationId = cookieObject.organizationId;$scope.userId = cookieObject.userId;$scope.storeId = cookieObject.storeId; 
    function initializeController() {
      $rootScope.loginpage = true;
      self.model = {
        isfreqValid: [],
        isAdminTime: [],
        isPreAdminTime: [],
        isPostAdminTime: [],
        pagination: {
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
      };
      self.model.PopulateFrequencyMaster = populateFrequencyMaster;
      self.model.activateDeactivateFrequencyMaster = activateDeactivateFrequencyMaster;
      self.model.SaveFrequencyMaster = SaveFrequencyMaster;
      self.model.ClearFields = clearFields;
      self.model.ViewTimeDetails = ViewTimeDetails;
      self.model.GetTime = GetTime;
      self.model.getMilliseconds = getMilliseconds;
      self.model.createFrequencyMaster = createFrequencyMaster;
      self.model.updateFrequencyMaster = updateFrequencyMaster;
      self.model.ViewTimes = ViewTimes;
      self.model.clearSearch = clearSearch;
      self.model.Add = Add;
      self.model.Less = Less;
      self.model.isOk = false;
      getFrequencyMasterData();
      ViewTimeDetails();
      // getAdministrationTimesData();
    }

    //Method to get the list of FrequencyMaster
    function getFrequencyMasterData() {
      var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.FREQUENCY_API +$scope.organizationId;
      var data = {};
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, data).then(function(response) {
    	  self.model.pagination.data=[];
        self.model.frequencyMasterDataList = response.data;
        
        if(!!response && !!response.data && response.data.length > 0){            
            PaginationService.searchData(self.model.frequencyMasterDataList, self.model.pagination);
          }
        $rootScope.stopLoader();
      });
    }

    var ViewTimes = function(item) {
      angular.forEach(self.model.frequencyMasterDataList, function(x) {
        if (item.id == x.id) {
          self.model.timeDetails = item.items;
        }
      });
    };

    var ViewTimeDetails = function() {
      self.model.clockList = [];
      self.model.clockList.push({
        adminTime: moment(36000000)
          .utc()
          .format("HH:mm"),
        preAdminTime: moment(36000000)
          .utc()
          .format("HH:mm"),
        postAdminTime: moment(36000000)
          .utc()
          .format("HH:mm")
      });

      console.log(self.model.clockList);

      setTimeout(function() {
        $(".clock-input").clockpicker({
          autoclose: true
        }).on('changeTime.clockpicker', function(e) {  // ✅ Corrected event name
          var inputElement = $(this).find('input');
          var scope = angular.element(inputElement).scope();
          var ngModel = inputElement.attr('ng-model');

          scope.$apply(function() {
            scope.$eval(ngModel + ' = "' + e.time.value + '"');
          });
        });
      }, 1000);
    };


    var Add = function(item, idx) {
      self.model.isOk = false;
      self.model.clockList.push({
        adminTime: moment(36000000)
          .utc()
          .format("HH:mm"),
        preAdminTime: moment(36000000)
          .utc()
          .format("HH:mm"),
        postAdminTime: moment(36000000)
          .utc()
          .format("HH:mm")
      });
      setTimeout(function() {
        $(".clock-input").clockpicker({
          autoclose: true
        }).on('changeTime.clockpicker', function(e) {

          var inputElement = $(this).find('input');
          var scope = angular.element(inputElement).scope();
          var ngModel = inputElement.attr('ng-model');

          scope.$apply(function() {
            scope.$eval(ngModel + ' = "' + e.time.value + '"');
          });
        });
      }, 1000);
    };

    var Less = function(item, idx) {
      self.model.isOk = false;
      var index = self.model.clockList.indexOf(item);
      self.model.clockList.splice(index, 1);
    };

    //Method to get the list of administration times
    function getAdministrationTimesData() {
      var URI = constants.apiurl + "/frequencies";
      var data = {};
      GenericService.serviceAction("GET", URI, data).then(function(response) {
        self.model.administrationTimesDataList = response.data;
      });
    }

    var GetTime = function() {
      self.model.isOk = true;
      var items = [];
      //   if(!self.model.id){
      angular.forEach(self.model.clockList, function(x) {
        items.push({
          id: x.id,
          administrationTime: x.adminTime,
          preBufferAdministrationTime: x.preAdminTime,
          postBufferAdministrationTime: x.postAdminTime
          /*administrationTime: getMilliseconds(x.adminTime),
          preBufferAdministrationTime: getMilliseconds(x.preAdminTime),
          postBufferAdministrationTime: getMilliseconds(x.postAdminTime)*/
        });
      });
     setTimeout(function() {
        $(".clockpicker").clockpicker();
      }, 1000);

      self.model.timeList = items;

      if (ValidateTime(self.model.clockList)) {
        var popup = angular.element("#addfrequency");
        popup.modal("hide");
      }
    };

    var ValidateTime = function(frequency) {
      var isValid = true;

      // if (!frequency || frequency.length <= 0) {
      //   isValid = false;
      //   growl.error("Add some items in the Sub Store Master");
      // } else {

      frequency.forEach(function(frequencyitem) {
         if($scope.validateTimeFormat(frequencyitem)){
            var adminTime = parseFloat(frequencyitem.adminTime);
            var preTime = parseFloat(frequencyitem.preAdminTime);
            var postTime = parseFloat(frequencyitem.postAdminTime);

            if( adminTime == 0){
              adminTime = 24;
            }

            if( postTime == 0){
              postTime = 24;
            }

            if (preTime > adminTime) {
              frequencyitem.isPreAdminTime = true;
              frequencyitem.isAdminTime = true;
              frequencyitem.isPostAdminTime = false;
              isValid = false;
              growl.error("Pre Administration Time should be less than Administration Time");
            }

            if (postTime < adminTime) {
              frequencyitem.isAdminTime = true;
              frequencyitem.isPostAdminTime = true;
              frequencyitem.isPreAdminTime = false;
              isValid = false;
              growl.error("Post Administration Time should be greater than Administration Time");
            }

            if (preTime > postTime) {
              frequencyitem.isPreAdminTime = true;
              frequencyitem.isPostAdminTime = true;
              frequencyitem.isAdminTime = false;
              isValid = false;
              growl.error("Pre Administration Time should be less than Post Administration Time");
            }
        }
        else{
            isValid = false;
        }
      });
        if(!$scope.isValidTimeFormat || !isValid){
          return false;
        }
        else{
            return true;
        }
    };

    var getMilliseconds = function(time) {
      return (
        (Number(time.split(":")[0]) * 60 + Number(time.split(":")[1])) *
        60 *
        1000
      );
    };

    //Method to populate formulation type for editing
    function findInRecordInArray(array, key, value) {
      for (var i = 0; i < array.length; i++) {
        if (array[i][key] == value) {
          return array[i];
        }
      }
      return null;
    }
    function getFrequencyMasterById(frequencyId) {
      return findInRecordInArray(
        self.model.frequencyMasterDataList,
        "frequencyId",
        frequencyId
      );
    }
    function populateFrequencyMaster(frequencyMasterData) {
      var URI =
        CONSTANTS.MASTER.GLOBAL.CLINICAL.FREQUENCY_API +$scope.organizationId + "/"+ frequencyMasterData.id;
      var data = {};
      $rootScope.startLoader();
      GenericService.serviceAction("GET", URI, data).then(function(response) {
        self.model.id = response.data.id;
        self.model.frequencyCode = response.data.code;
        self.model.frequencyDescription = response.data.description;
        self.model.adminstrationTimes = response.data.noOfAdministrationTime.toString();
        self.model.status = response.data.status;
        self.model.isEdit = true;
        self.model.activeFrequencyId = response.data.id;
        self.model.clockList = [];
        $scope.addedLanguageList = response.data.addedLanguageList;
        //self.model.ViewTimeDetails(self.model.adminstrationTimes);

        angular.forEach(response.data.items, function(data) {
          self.model.clockList.push({
            id: data.id,
            adminTime: data.administrationTimeString,
            preAdminTime: data.preBufferAdministrationTimeString,
            postAdminTime:data.postBufferAdministrationTimeString,
            
            /*adminTime: moment(data.administrationTime)
              .utc()
              .format("HH:mm"),
            preAdminTime: moment(data.preBufferAdministrationTime)
              .utc()
              .format("HH:mm"),
            postAdminTime: moment(data.postBufferAdministrationTime)
              .utc()
              .format("HH:mm")*/
          });
        });
        // var popup = angular.element('#addfrequency');
        // popup.modal('show');
     setTimeout(function() {
        $(".clockpicker").clockpicker();
      }, 1000);

        $rootScope.stopLoader();
      });
    }

    //Method to activate or deactivate formulation type
    function activateDeactivateFrequencyMaster(frequencyData) {
      var URI =
        CONSTANTS.MASTER.GLOBAL.CLINICAL.FREQUENCY_API +$scope.organizationId + "/" + frequencyData.id;
      var data = {
        status: frequencyData.status
      };
      if (frequencyData.status == false) {
        clearFields();
      }
      $rootScope.startLoader();
      GenericService.serviceAction("PATCH", URI, data).then(
        function(response) {
          $rootScope.stopLoader();
          growl.success("Status changed successfully");
        },
        function errorCallback(response) {
          $rootScope.stopLoader();
          growl.error("Something went wrong");
        }
      );
    }

    //Method to add or update the Frequency master
    var SaveFrequencyMaster = function() {
      if (self.model.id) {
        updateFrequencyMaster();
      } else {
        createFrequencyMaster();
      }
    };

    var ValidateCode = function() {
      var isValid = true;

      self.model.frequencyMasterDataList.forEach(function(element) {
        if (element.code == self.model.frequencyCode) {
          growl.error("Duplicate Frequency code");
          isValid = false;
        }
      });
      if (!/^[a-z0-9]*[-]?[a-z0-9]*$/i.test(self.model.frequencyCode)) {
        //-------------------------^^^^^^^^^^
        growl.error("Name can only be alpha numeric with hypen.");
        return;
      }
      if (self.model.isOk == false) {
        growl.error("Save Items in Frequency Details");
        return;
      }
      return isValid;
    };

    var ValidateCodeForUpdate = function() {
      var isValid = true;

      self.model.frequencyMasterDataList.forEach(function(element) {
        if (element.id != self.model.activeFrequencyId) {
          if (element.code == self.model.frequencyCode) {
            growl.error("Duplicate Frequency code");
            isValid = false;
          }
        }
      });
      if (self.model.isOk == false) {
        growl.error("Add Items in Frequency Details");
        return;
      }
      return isValid;
    };

    $scope.addedLanguageList=[];
    //Method to add the formulation type
    function createFrequencyMaster() {
      if (ValidateCode()) {
        var URI = CONSTANTS.MASTER.GLOBAL.CLINICAL.FREQUENCY_SAVE;
        var data = {
          code: self.model.frequencyCode,
          description: self.model.frequencyDescription,
          noOfAdministrationTime: self.model.timeList.length,
          items: self.model.timeList,
          unitId:$scope.unitId,
          organizationId:$scope.organizationId,
          userId:$scope.userId,
          addedLanguageList : $scope.addedLanguageList
        };
        $rootScope.startLoader();
        GenericService.serviceAction("POST", URI, data).then(
          function successCallback(response) {
            getFrequencyMasterData();
            $rootScope.stopLoader();
            growl.success(response.data.message);
            $scope.addedLanguageList=[];
            clearFields();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error("Something went wrong");
          }
        );
      }
    }

    //Method to update the formulation type
    function updateFrequencyMaster() {
      if (ValidateCodeForUpdate()) {
        var URI =
          CONSTANTS.MASTER.GLOBAL.CLINICAL.FREQUENCY_API +$scope.organizationId + "/" + self.model.id;
        var data = {
          code: self.model.frequencyCode,
          description: self.model.frequencyDescription,
          noOfAdministrationTime: self.model.timeList.length,
          items: self.model.timeList,
          unitId:$scope.unitId,
          organizationId:$scope.organizationId,
          addedLanguageList : $scope.addedLanguageList
        };
        $rootScope.startLoader();
        GenericService.serviceAction("PATCH", URI, data).then(
          function(response) {
            var frequencyData = self.model.frequencyMasterDataList.find(
              function(type) {
                return type.id == self.model.id;
              }
            );
            getFrequencyMasterData();
            $scope.addedLanguageList=[];
            $rootScope.stopLoader();
            growl.success(response.data.message);
            clearFields();
          },
          function errorCallback(response) {
            $rootScope.stopLoader();
            growl.error("Something went wrong");
          }
        );
      }
    }

    //Method to clear the fileds
    function clearFields() {
      self.model.frequencyCode = "";
      self.model.frequencyDescription = "";
      self.model.adminstrationTimes = "";
      self.model.id = undefined;
      self.model.activeFrequencyId = undefined;
      self.model.isOk = false;
      ViewTimeDetails();
      $scope.addedLanguageList=[];
      $scope.Obj.convertedText='';
    }
    var clearSearch = function() {
      self.model.search = "";
    };

    $scope.LanguObj={
        languageId: 1,
        desc:"English"
    };

    $scope.assignToLang = function(){
        if(self.model.frequencyCode !== undefined && self.model.frequencyCode !== undefined){

        }
        $scope.Obj.convertedText = self.model.frequencyDescription;
    }

    $scope.getLanguageList = function() {
        $rootScope.startLoader();

        var data = {
                organizationId:$scope.organizationId,
                unitId:	$scope.unitId
                };

        var URI = BASE_URL + ROOT_URL + "/LIS/global/getTotalLanguageMasterRecordActive"+"/"+ $scope.organizationId+"/"+ $scope.unitId;
        console.log("URI", URI);
        GenericService.serviceAction("GET", URI).then(function(response) {
            $rootScope.stopLoader();
            $scope.languageList = [];
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
//    $scope.getLanguageList();

    $scope.addedLanguageList = [];

    $scope.Obj={
        languageId: "1_English",
        convertedText:""
    };

    $scope.tempList = [];
    $scope.addedLanguageList = [];

    $scope.addLanguageDetails = function(obj) {
        console.log("obj==========,",obj)
        var newArr = {};
        var addedLangList = [];

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


    $scope.showLanguageList = function(id){
        try{
            $scope.languageMasterList=[];
            var data=
                 {
                    organizationId : $scope.orgId,
                    unitId         :$scope.orgUnitId,
                    frequencyId :id
                }
            var URI = BASE_URL + ROOT_URL + "/api/globals/frequency/getConvertedLanguageListById";

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

    $scope.languageList = [];
    $scope.getLanguageMasterList = function() {
        var data = "";
        $rootScope.startLoader();
        var URI = BASE_URL + ROOT_URL + LIS_GLOBAL + "/listLanguageMaster/" + $scope.organizationId + "/" + $scope.unitId;
        console.log("URI", URI);
        $scope.languageList = [];
        GenericService.serviceAction("GET", URI, data).then(function(response) {
            $rootScope.stopLoader();
            $scope.languageList = response.data.listObject;
        });
    }
    $scope.getLanguageMasterList();

        $scope.validateTimeFormat = function(item) {
            var timeList = [];
            var isValidTime = true;

            timeList.push(item.adminTime);
            timeList.push(item.preAdminTime);
            timeList.push(item.postAdminTime);

            $scope.isValidTimeFormat = true;
            angular.forEach(timeList, function(time) {
                if(time !== undefined && time !== null && time !== ''){
                    if (!/^[0-9:]*$/.test(time)) {
                        $scope.isValidTimeFormat = false;
                        growl.error("Please enter valid time!");
                        return;
                    }

                    var parts = time.split(":");
                    if (parts.length !== 2 || parts[0].length > 2 || parts[1].length > 2) {
                        $scope.isValidTimeFormat = false;
                        growl.error("Please enter valid time!");
                        return;
                    }

                    var hours = parseInt(parts[0], 10);
                    var minutes = parseInt(parts[1], 10);

                    if (isNaN(hours) || isNaN(minutes) || hours < 0 || hours > 24 || minutes < 0 || minutes > 59) {
                        $scope.isValidTimeFormat = false;
                        growl.error("Please enter valid time!");
                        return;
                    }
                    if(!isValidTime){
                        $scope.isValidTimeFormat = false;
                        growl.error("Please enter valid time!");
                        return;
                    }
                }
                else{
                     $scope.isValidTimeFormat = false;
                    growl.error("Please enter valid time!");
                    return;
                }
            });
            return $scope.isValidTimeFormat;
        };

    initializeController();
  }
  function config($stateProvider) {
    $stateProvider.state("frequencyMaster", {
      url: "/frequencyMaster",
      templateUrl:
        "views/master/GlobalMaster/AssetManagenent/Master/frequency-master.html",
      controller: "FrequencyMaster.Controller",
      title: 'Frequency Master',
  	resolve : {

			data: function($q, $state, $timeout) {
		        var deferred = $q.defer();
		
		        $timeout(function() {
		        	var arr = JSON.parse(localStorage.getItem('permissions'))
				 	.modules.filter(function(item){
	            	   return (item.id == 478);
				 	});
					if(arr.length <= 0){
						$state.go("accessDenied")
						deferred.reject();
					 } else {
						deferred.resolve();
					 }
		        });
		
		        return deferred.promise;
		      }
		 },
      controllerAs: "vm"
    });
  }
  angular
    .module("myApp")
    .config(config)
    .controller("FrequencyMaster.Controller", frequencyMasterController);
})();
