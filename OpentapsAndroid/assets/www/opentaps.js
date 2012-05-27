var Opentaps = function() {};

Opentaps.prototype.getAvailablePromotions = function(successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getAvailablePromotions', []);
};

Opentaps.prototype.getBrowseRootCategoryId = function(catalogId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getBrowseRootCategoryId', [catalogId]);
};

Opentaps.prototype.getCategoryHierarchy = function(categoryId, catalogId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getCategoryHierarchy', [categoryId, catalogId]);
};

Opentaps.prototype.getCategory = function(categoryId, catalogId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getCategory', [categoryId, catalogId]);
};

Opentaps.prototype.getProduct = function(productId, catalogId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getProduct', [productId, catalogId]);
};

Opentaps.prototype.performExternalLogin = function(username, password, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'performExternalLogin', [username, password]);
};

Opentaps.prototype.getAssociatedProducts = function(productId, catalogId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getAssociatedProducts', [productId, catalogId]);
};

Opentaps.prototype.getCategoryContent = function(categoryId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getCategoryContent', [categoryId]);
};

Opentaps.prototype.getCategoryDescription = function(categoryId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getCategoryDescription', [categoryId]);
};

Opentaps.prototype.getCategoryName = function(categoryId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getCategoryName', [categoryId]);
};

Opentaps.prototype.getProductCatalogIds = function(successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getProductCatalogIds', []);
};

Opentaps.prototype.getProductContent = function(productId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getProductContent', [productId]);
};

Opentaps.prototype.getProductDescription = function(productId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getProductDescription', [productId]);
};

Opentaps.prototype.getProductName = function(productId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getProductName', [productId]);
};

Opentaps.prototype.getProductPrices = function(productId, catalogId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getProductPrices', [productId, catalogId]);
};

Opentaps.prototype.getProductKeywords = function(productId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getProductKeywords', [productId]);
};

Opentaps.prototype.getProductTypes = function(successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getProductTypes', []);
};

Opentaps.prototype.getSpecialCategoryIds = function(catalogId, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getSpecialCategoryIds', [catalogId]);
};

Opentaps.prototype.findProductsByKeyword = function(catalogId, keywordString, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'findProductsByKeyword', [catalogId, keywordString]);
};

Opentaps.prototype.findRecentProducts = function(catalogId, limit, fromDate, toDate, successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'findRecentProducts', [catalogId, limit, fromDate, toDate]);
};

Opentaps.prototype.getAvailableProductFeatures = function(successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getAvailableProductFeatures', []);
};

Opentaps.prototype.getSortOrders = function(successCallback, failureCallback) {
	return PhoneGap.exec( successCallback, failureCallback,  'OpentapsPlugin', 'getSortOrders', []);
};

PhoneGap.addConstructor(function() {
	PhoneGap.addPlugin("opentaps", new Opentaps());
});