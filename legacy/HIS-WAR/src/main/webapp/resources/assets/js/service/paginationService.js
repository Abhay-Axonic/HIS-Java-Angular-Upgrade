angular.module('myApp').service('PaginationService', function ($filter) {

    var searchData = function (data, pagination) {
        if (!!pagination.search) {
            pagination.filteredData = $filter('filter')(data, {
                $: pagination.search
            });
        } else {
            pagination.filteredData = data;
        }

        setTotalPages(pagination);
    }
    var searchDataByField = function (data, pagination, search) {
        if (!!search) {
            pagination.filteredData = $filter('filter')(data, search);
        } else {
            pagination.filteredData = data;
        }
        setTotalPages(pagination);
    }

    var getPagesList = function (pagination) {
        var rangeSize = 5;
        var pages = [];
        var start = pagination.currentPage;

        if (pagination.totalPages < rangeSize) {
            rangeSize = pagination.totalPages;
        }

        if (start > pagination.totalPages - rangeSize) {
            start = pagination.totalPages - rangeSize + 1;
        }

        for (var i = start; i < start + rangeSize; i++) {
            pages.push(i);
        }

        return pages;
    };

    var setTotalPages = function (pagination) {
        pagination.currentPage = 1;
        pagination.totalPages = Math.ceil(pagination.filteredData.length / parseInt(pagination.pageSize, 10));
        setPagedData(pagination);
    }

    var setPagedData = function (pagination) {
        var limit = parseInt(pagination.pageSize, 10);
        var offset = (pagination.currentPage - 1) * limit;
        pagination.data = pagination.filteredData.slice(offset, offset + limit);
    }

    var setPage = function (currentPage, pagination) {
        pagination.currentPage = currentPage;
        setPagedData(pagination);
    }

    var setFirstPage = function (pagination) {
        setPage(1, pagination);
    }

    var setPreviousPage = function (pagination) {
        setPage(pagination.currentPage - 1, pagination);
    }

    var setNextPage = function (pagination) {
        setPage(pagination.currentPage + 1, pagination);
    }

    var setLastPage = function (pagination) {
        setPage(pagination.totalPages, pagination);
    }

    var sortBy = function (pagination, property, reverse) {
        pagination.filteredData = $filter('orderBy')(pagination.filteredData, property, reverse);
        setTotalPages(pagination);
    }

    return {
        searchDataByField:searchDataByField,
        searchData: searchData,
        setPagedData: setPagedData,
        setTotalPages: setTotalPages,
        getPagesList: getPagesList,
        setPage: setPage,
        setFirstPage: setFirstPage,
        setPreviousPage: setPreviousPage,
        setNextPage: setNextPage,
        setLastPage: setLastPage,
        sortBy: sortBy
    };
});