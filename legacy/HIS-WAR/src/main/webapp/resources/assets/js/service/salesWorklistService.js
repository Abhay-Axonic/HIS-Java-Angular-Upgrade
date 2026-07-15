angular.module('myApp').service('salesWorklistService', ['$rootScope', function ($rootScope) {
    var salesWorklistId = 0;
    var passSalesWorklistId = function(passSalesWorklistId){
      return passSalesWorklistId;
    }
  
    var setWorklistId = function(id) {
        salesWorklistId = id;
    };
  
    var getWorklistId = function(){
        return salesWorklistId;
    };
  
    var setEncounterId = function(encounterId) {
    	
    	encounterId = encounterId;
    };
    var getEncounterId = function(){
        return encounterId;
    };
    
    return {
      passSalesWorklistId: passSalesWorklistId,
      setWorklistId: setWorklistId,
      getWorklistId: getWorklistId,
      setEncounterId: setEncounterId,
      getEncounterId: getEncounterId
    };
  }]);