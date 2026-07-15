angular
    .module("myApp")
    .filter('sumByColumn', function () {
        return function (collection, column) {
            var total = 0;

            if(collection)
            collection.forEach(function (item) {
                total += isNaN(parseFloat(item[column])) ? 0 :  parseFloat(item[column]);
            });

            return total;
        };
    });

    angular
    .module("myApp")
    .filter('sumByColumnSubColumn', function () {
        
        return function (collection, column) {
            var total = 0;
            if(collection)
            collection.forEach(function (item) {
                total += parseFloat(item[column]);

                if(item.selectedItems)
                item.selectedItems.forEach(function (subitem) {
                    total += isNaN(parseFloat(subitem[column])) ? 0 :  parseFloat(subitem[column]);
                });
            });

            return total;
        };
    });

    angular
    .module("myApp")
    .filter('sumByColumnSub', function () {
        
        return function (collection, column) {
        	
            var total = 0;
            if(collection)
            // angular.forEach()
            
                collection.forEach(function (item) {
                    if((item.itemDisabled != undefined && item.disabled != undefined) && (!item.itemDisabled && !item.disabled)){
                        if(item[column] == undefined){
                            item[column] = 0;
                        }
                        total += parseFloat(item[column]);
                    }

                    if(item.selectedItems)
                    item.selectedItems.forEach(function (subitem) {
                        if(!subitem.itemDisabled)
                        total += isNaN(parseFloat(subitem[column])) ? 0 :  parseFloat(subitem[column]);
                    });
                });
            
            return total;
        };
    });