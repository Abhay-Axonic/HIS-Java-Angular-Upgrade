angular.module('myApp').service('detailService', ['$rootScope', function ($rootScope) {
    var id = 0;
    var selectedTab=1;

    var setId = function(itemId) {
        id = itemId;
    };
  
    var getId = function(){
        return id;
    };
     
    var setActiveTab = function(tab) {
        selectedTab = tab;
    }

    var getActiveTab = function() {
        return selectedTab;
    }
  
    return {
      setId: setId,
      getId: getId,
      setActiveTab: setActiveTab,
      getActiveTab: getActiveTab
    };
  }]);

 