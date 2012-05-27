package net.enala.phonegap.plugins.opentaps.services;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OpentapsServices {

	private String username = null;
	private String password = null;

	private String serviceUrl = null;
	private String systemId = "9000";
	private String productStoreId = "9000";

	public OpentapsServices(String serviceUrl, String systemId,
			String productStoreId, String username, String password) {

		this.username = username;
		this.password = password;

		this.serviceUrl = serviceUrl;
		if (systemId != null && !systemId.equals("")) {
			this.systemId = systemId;
		}
		if (productStoreId != null && !productStoreId.equals("")) {
			this.productStoreId = productStoreId;
		}
	}

	public OpentapsServices(String serviceUrl, String systemId,
			String productStoreId) {

		this.serviceUrl = serviceUrl;
		if (systemId != null && !systemId.equals("")) {
			this.systemId = systemId;
		}
		if (productStoreId != null && !productStoreId.equals("")) {
			this.productStoreId = productStoreId;
		}
	}

	private Map invokeService(String serviceName, Map parameters) throws MalformedURLException, XmlRpcException {

		XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(serviceUrl));
		config.setEnabledForExtensions(true);
		config.setEnabledForExceptions(true);
		XmlRpcClient client = new XmlRpcClient();
		client.setConfig(config);

		Map result = (Map) client.execute(serviceName,
				new Object[] { parameters });

		return result;

	}
	

	public JSONArray getAvailablePromotions() throws MalformedURLException, XmlRpcException, JSONException {
		JSONArray promotions = new JSONArray();
		
		Map mpParameters = new TreeMap();

		mpParameters.put("systemId", systemId);
		mpParameters.put("productStoreId", productStoreId);
		
		Map mpResult = invokeService("catalogexport.getAvailablePromotions",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			Object[] promos = (Object[]) mpResult.get("AvailablePromotions");
			if (promos != null && promos.length > 0) {
				for (int i = 0; i < promos.length; i++) {
					
					JSONObject promotion = new JSONObject();
					promotion.put("text", (String) promos[i]);
					
					promotions.put(promotion);
				}
			}
		}

		return promotions;

	}

	public JSONObject getRootCategoryId(String catalogId) throws MalformedURLException, XmlRpcException, JSONException {
		JSONObject result = null;

		Map mpParameters = new TreeMap();

		mpParameters.put("systemId", systemId);
		mpParameters.put("productStoreId", productStoreId);
		mpParameters.put("catalogId", catalogId);

		Map mpResult = invokeService("catalogexport.getBrowseRootCategoryId",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = new JSONObject();
			result.put("rootCategoryId", mpResult.get("CategoryId"));
		}

		return result;

	}

	public JSONObject getCategoryHierarchy(String categoryId, String catalogId, Locale locale) throws MalformedURLException, XmlRpcException, JSONException {
		JSONObject result = null;

		Map mpParameters = new TreeMap();

		mpParameters.put("systemId", systemId);
		mpParameters.put("productStoreId", productStoreId);
		mpParameters.put("categoryId", categoryId);
		mpParameters.put("locale", locale);
		mpParameters.put("catalogId", catalogId);

		Map mpResult = invokeService("catalogexport.getCategoryHierarchy",
				mpParameters);
		
		if (mpResult != null || !mpResult.isEmpty()) {
			result = initCategory(mpResult);

			Object[] children = (Object[]) mpResult.get("ChildData");

			result.put("childData", getCategoryChildren(result, children));

		}

		return result;

	}
	
	private JSONObject initCategory(Map data) throws JSONException {
		JSONObject obj = null;
		if ((data != null) && !data.isEmpty()) {
			obj = new JSONObject();
			
			obj.put("categoryId", data.get("CategoryId"));
			obj.put("name", data.get("Name"));
			obj.put("description", data.get("Description"));
			obj.put("longDescription", data.get("LongDescription"));
			obj.put("categoryImageURL", data.get("CategoryImageURL"));
			obj.put("categoryImageAltText", data.get("CategoryImageAltText"));
			obj.put("productCount", data.get("ProductCount"));
		}

		return obj;
	}
	
	private JSONArray getCategoryChildren(JSONObject category, Object[] children) throws JSONException {
		JSONArray result = null;
		if (children != null && children.length > 0) {
			result = new JSONArray();
			for (int i = 0; i < children.length; i++) {
				Map mpChild = (Map) children[i];

				JSONObject childCategory = initCategory(mpChild);
				Object[] childChildren = (Object[]) mpChild.get("ChildData");

				childCategory.put("childData", getCategoryChildren(childCategory, childChildren));
				
				result.put(childCategory);
			}
		}
		return result;
	}

	public JSONObject getCategory(String categoryId, String catalogId, Locale locale) throws MalformedURLException, XmlRpcException, JSONException {
		JSONObject result = null;

		Map mpParameters = new TreeMap();

		mpParameters.put("systemId", systemId);
		mpParameters.put("productStoreId", productStoreId);
		mpParameters.put("categoryId", categoryId);
		mpParameters.put("locale", locale);
		mpParameters.put("catalogId", catalogId);

		Map mpResult = invokeService("catalogexport.getCategory", mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = initCategory(mpResult);
			
			Object[] children = (Object[]) mpResult.get("ChildData");

			result.put("childData", getCategoryChildren(result, (Object[]) mpResult.get("ChildData")));
			
			result.put("products", getProductChildren(result, (Object[]) mpResult.get("ProductData")));
		}

		return result;

	}
	
	private JSONArray getProductChildren(JSONObject category, Object[] products) throws JSONException {
		JSONArray result = null;
		if (products != null && products.length > 0) {
			result = new JSONArray();
			for (int i = 0; i < products.length; i++) {
				Map mpChild = (Map) products[i];
				result.put(initProduct(mpChild));
			}
		}
		return result;
	}
	


	private JSONObject initProduct(Map data) throws JSONException {
		JSONObject obj = null;
		if ((data != null) && !data.isEmpty()) {
			obj = new JSONObject();
			
			obj.put("price", data.get("Price"));
			obj.put("description", data.get("Description"));
			obj.put("longDescription", data.get("LongDescription"));
			obj.put("smallImageURL", data.get("SmallImageURL"));
			obj.put("mediumImageURL", data.get("MediumImageURL"));
			obj.put("largeImageURL", data.get("LargeImageURL"));
			obj.put("detailImageURL", data.get("DetailImageURL"));
			obj.put("smallImageAltText", data.get("SmallImageAltText"));
			obj.put("mediumImageAltText", data.get("MediumImageAltText"));
			obj.put("largeImageAltText", data.get("LargeImageAltText"));
			obj.put("detailmageAltText", data.get("DetailmageAltText"));
			obj.put("listPrice", data.get("ListPrice"));
			obj.put("productId", data.get("ProductId"));
			obj.put("name", data.get("Name"));
			obj.put("priceCurrencyCode", data.get("PriceCurrencyCode"));
			obj.put("requireInventory", data.get("RequireInventory"));
			obj.put("productType", data.get("ProductType"));
			obj.put("quantityInStock", data.get("QuantityInStock"));
			obj.put("daysToShip", data.get("DaysToShip"));
			obj.put("productCategoryId", data.get("productCategoryId"));
			obj.put("isVirtual", data.get("isVirtual"));
			obj.put("isVariant", data.get("isVariant"));

			
			
			if (data.get("Variants") != null) {
				Hashtable temp = new Hashtable((Map) data.get("Variants"));
				if (temp.size() > 0) {
					Enumeration en = temp.keys();
					while (en.hasMoreElements()) {
						String key = (String) en.nextElement();
						List<Object> temp_p = Arrays.asList((Object[]) temp
								.get(key));
						JSONArray prod_var = new JSONArray();
						for (int i = 0; i < temp_p.size(); i++) {
							Map map = (Map) temp_p.get(i);
							prod_var.put(initProduct(map));
						}
						obj.put("variants", prod_var);
					}
				}
			}
		}

		return obj;
	}

	public JSONObject getProduct(String productId, String catalogId, Locale locale) throws MalformedURLException, XmlRpcException, JSONException {
		JSONObject result = null;

		Map<String, Object> mpParameters = new TreeMap<String, Object>();

		mpParameters.put("systemId", systemId);
		mpParameters.put("productStoreId", productStoreId);
		mpParameters.put("productId", productId);
		mpParameters.put("locale", locale);
		mpParameters.put("catalogId", catalogId);

		Map mpResult = invokeService("catalogexport.getProduct", mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = initProduct(mpResult);

		}

		return result;

	}

	public String performExternalLogin(String userName, String password) throws MalformedURLException, XmlRpcException {
		String result = null;

		Map mpParameters = new TreeMap();

		mpParameters.put("systemId", systemId);
		mpParameters.put("userLoginId", userName);
		mpParameters.put("password", password);

		Map mpResult = invokeService(
				"catalogexport.security.performExternalLogin", mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = (String) mpResult.get("ExternalLoginKey");
		}

		return result;

	}

	public JSONArray getAssociatedProducts(String productId,
			String catalogId, Locale locale) throws MalformedURLException, XmlRpcException, JSONException {
		JSONArray result = null;
		Map<String, Object> mpParameters = new TreeMap<String, Object>();

		mpParameters.put("systemId", systemId);
		mpParameters.put("productStoreId", productStoreId);
		mpParameters.put("productId", productId);
		mpParameters.put("catalogId", catalogId);
		mpParameters.put("locale", locale);

		Map mpResult = invokeService("catalogexport.getAssociatedProducts",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			Hashtable associaProdu = new Hashtable((Map) mpResult
					.get("AssociatedProducts"));
			Enumeration en = associaProdu.keys();

			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				JSONObject obj = new JSONObject();
				obj.put("key", key);
				List lsProducts = Arrays.asList((Object[]) associaProdu.get(key));
				JSONArray temp = new JSONArray();
				for (int i = 0; i < lsProducts.size(); i++) {
					Map proAss = (Map) lsProducts.get(i);
					temp.put(initProduct(proAss));
				}
				obj.put("products", temp);
				result.put(obj);
			}

		}

		return result;

	}

	public JSONObject getCategoryContent(String categoryId, Locale locale) throws MalformedURLException, XmlRpcException, JSONException {
		JSONObject result = null;
		Map<String, Object> mpParameters = new TreeMap<String, Object>();

		mpParameters.put("systemId", systemId);
		mpParameters.put("categoryId", categoryId);
		mpParameters.put("locale", locale);

		Map mpResult = invokeService("catalogexport.getCategoryContent",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = new JSONObject();
			
			result.put("name", mpResult.get("Name"));
			result.put("description", mpResult.get("Description"));
			result.put("longDescription", mpResult.get("LongDescription"));
			result.put("smallImageURL", mpResult.get("CategoryImageURL"));
			result.put("smallImageAltText", mpResult.get("CategoryImageAltText"));
		}

		return result;
	}

	public JSONObject getCategoryDescription(String categoryId,
			Locale locale) throws MalformedURLException, XmlRpcException, JSONException {
		JSONObject result = null;
		Map<String, Object> mpParameters = new TreeMap<String, Object>();

		mpParameters.put("systemId", systemId);
		mpParameters.put("categoryId", categoryId);
		mpParameters.put("locale", locale);

		Map mpResult = invokeService("catalogexport.getCategoryDescription",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = new JSONObject();
			result.put("description", mpResult.get("Description"));
			result.put("longDescription", mpResult.get("LongDescription"));
		}

		return result;
	}


	public String getCategoryName(String categoryId, Locale locale) throws MalformedURLException, XmlRpcException {
		String result = null;
		Map<String, Object> mpParameters = new TreeMap<String, Object>();

		mpParameters.put("systemId", systemId);
		mpParameters.put("categoryId", categoryId);
		mpParameters.put("locale", locale);

		Map mpResult = invokeService("catalogexport.getCategoryName",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = (String) mpResult.get("Name");
		}

		return result;
	}


	public JSONArray getProductCatalogIds() throws MalformedURLException, XmlRpcException {

		Map<String, Object> mpParameters = new TreeMap<String, Object>();
		JSONArray result = null;

		mpParameters.put("systemId", systemId);
		mpParameters.put("productStoreId", productStoreId);

		Map mpResult = invokeService("catalogexport.getProductCatalogIds",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = new JSONArray();
			List<Object> temp = Arrays.asList((Object[]) mpResult
					.get("CatalogIds"));
			for (int i = 0; i < temp.size(); i++) {
				result.put(temp.get(i));
			}
		}

		return result;
	}

	public JSONObject getProductContent(String productId, Locale locale) throws MalformedURLException, XmlRpcException, JSONException {

		Map<String, Object> mpParameters = new TreeMap<String, Object>();
		JSONObject result = null;

		mpParameters.put("systemId", systemId);
		mpParameters.put("productId", productId);
		mpParameters.put("locale", locale);

		Map mpResult = invokeService("catalogexport.getProductContent",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = new JSONObject();
			
			result.put("name", mpResult.get("Name"));
			result.put("description", mpResult.get("Description"));
			result.put("longDescription", mpResult.get("LongDescription"));
			result.put("smallImageURL", mpResult.get("SmallImageURL"));
			result.put("mediumImageURL", mpResult.get("MediumImageURL"));
			result.put("largeImageURL", mpResult.get("LargeImageURL"));
			result.put("detailImageURL", mpResult.get("DetailImageURL"));
			result.put("smallImageAltText", mpResult.get("SmallImageAltText"));
			result.put("mediumImageAltText", mpResult.get("MediumImageAltText"));
			result.put("largeImageAltText", mpResult.get("LargeImageAltText"));
			result.put("detailImageAltText", mpResult.get("DetailImageAltText"));
		}

		return result;
	}

	public JSONObject getProductDescription(String productId, Locale locale) throws MalformedURLException, XmlRpcException, JSONException {

		Map<String, Object> mpParameters = new TreeMap<String, Object>();
		JSONObject result = null;

		mpParameters.put("systemId", systemId);
		mpParameters.put("productId", productId);
		mpParameters.put("locale", locale);

		Map mpResult = invokeService("catalogexport.getProductDescription",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = new JSONObject();
			result.put("description", mpResult.get("Description"));
			result.put("longDescription", mpResult.get("LongDescription"));
		}

		return result;
	}

	public String getProductName(String productId, Locale locale) throws MalformedURLException, XmlRpcException {

		Map<String, Object> mpParameters = new TreeMap<String, Object>();
		String result = null;

		mpParameters.put("systemId", systemId);
		mpParameters.put("productId", productId);
		mpParameters.put("locale", locale);

		Map mpResult = invokeService("catalogexport.getProductName",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = (String) mpResult.get("Name");
		}

		return result;
	}

	public JSONObject getProductPrices(String productId, String catalogId,
			Locale locale) throws MalformedURLException, XmlRpcException, JSONException {

		Map<String, Object> mpParameters = new TreeMap<String, Object>();
		JSONObject result = new JSONObject();

		mpParameters.put("systemId", systemId);
		mpParameters.put("productId", productId);
		mpParameters.put("productStoreId", productStoreId);
		mpParameters.put("catalogId", catalogId);
		mpParameters.put("locale", locale);

		Map mpResult = invokeService("catalogexport.getProductPrices",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result.put("priceCurrencyCode", mpResult.get("PriceCurrencyCode"));
			result.put("price", mpResult.get("Price"));
			result.put("isSale", mpResult.get("IsSale"));
			result.put("listPrice", mpResult.get("ListPrice"));
		}

		return result;
	}

	public JSONArray getProductKeywords(String productId) throws JSONException, MalformedURLException, XmlRpcException {

		Map<String, Object> mpParameters = new TreeMap<String, Object>();
		JSONArray result = null;

		mpParameters.put("systemId", systemId);
		mpParameters.put("productId", productId);

		Map mpResult = invokeService("catalogexport.getProductKeywords",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = new JSONArray();
			Hashtable keywords = new Hashtable((Map) mpResult.get("Keywords"));
			Enumeration en = keywords.keys();

			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				Double value = (Double) keywords.get(key);

				JSONObject obj = new JSONObject();
				obj.put("key", key);
				obj.put("value", value);

				result.put(obj);
			}
		}

		return result;
	}

	public JSONArray getProductTypes(Locale locale) throws MalformedURLException, XmlRpcException, JSONException {

		Map<String, Object> mpParameters = new TreeMap<String, Object>();
		JSONArray result = null;

		mpParameters.put("systemId", systemId);
		mpParameters.put("locale", locale);

		Map mpResult = invokeService("catalogexport.getProductTypes",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			Hashtable prodTypes = new Hashtable((Map) mpResult
					.get("ProductTypes"));
			Enumeration en = prodTypes.keys();
			result = new JSONArray();

			while (en.hasMoreElements()) {
				Map prodType = (TreeMap) prodTypes.get(en.nextElement());
				JSONObject obj = new JSONObject();
				
				obj.put("productTypeId", prodType.get("ProductTypeId"));
				obj.put("description", prodType.get("Description"));
				obj.put("isPhysical", prodType.get("IsPhysical"));
				obj.put("isDigital", prodType.get("IsDigital"));

				result.put(obj);
			}
		}

		return result;
	}

	public JSONArray getSpecialCategoryIds(String catalogId) throws JSONException, MalformedURLException, XmlRpcException {

		Map<String, Object> mpParameters = new TreeMap<String, Object>();
		JSONArray result = null;

		mpParameters.put("systemId", systemId);
		mpParameters.put("catalogId", catalogId);

		Map mpResult = invokeService("catalogexport.getSpecialCategoryIds",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = new JSONArray();
			Hashtable temp = new Hashtable((HashMap) mpResult
					.get("CategoryIds"));
			Enumeration en = temp.keys();

			while (en.hasMoreElements()) {
				String categoryTypeId = (String) en.nextElement();
				String categoryId = (String) temp.get(categoryTypeId);

				JSONObject obj = new JSONObject();
				obj.put("categoryId", categoryId);
				obj.put("categoryTypeId", categoryTypeId);

				result.put(obj);
			}
		}

		return result;
	}

	public JSONArray findProductsByKeyword(String catalogId, String keywordString,
			Locale locale) throws MalformedURLException, XmlRpcException, JSONException {

		Map<String, Object> mpParameters = new TreeMap<String, Object>();
		JSONArray result = null;

		mpParameters.put("systemId", systemId);
		mpParameters.put("catalogId", catalogId);
		mpParameters.put("productStoreId", productStoreId);
		mpParameters.put("userLocale", locale);
		mpParameters.put("keywordString", keywordString);

		Map mpResult = invokeService(
				"catalogexport.search.findProductsByKeyword", mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = new JSONArray();
			List<Object> temp = Arrays.asList((Object[]) mpResult
					.get("Products"));
			for (int i = 0; i < temp.size(); i++) {
				Map map = (Map) temp.get(i);
				result.put(initProduct(map));
			}


		}

		return result;
	}


	public JSONArray findRecentProducts(String catalogId,
			Integer limit, Date fromDate, Date toDate, Locale locale) throws MalformedURLException, XmlRpcException, JSONException {

		Map<String, Object> mpParameters = new TreeMap<String, Object>();
		JSONArray result = null;

		mpParameters.put("systemId", systemId);
		mpParameters.put("catalogId", catalogId);
		mpParameters.put("productStoreId", productStoreId);
		mpParameters.put("locale", locale);
		mpParameters.put("limit", limit);
		mpParameters.put("fromDate", fromDate);
		mpParameters.put("toDate", toDate);

		Map mpResult = invokeService("catalogexport.search.findRecentProducts",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			result = new JSONArray();
			List<Object> temp = Arrays.asList((Object[]) mpResult
					.get("Products"));
			for (int i = 0; i < temp.size(); i++) {
				Map map = (Map) temp.get(i);
				JSONObject prod = initProduct(map);
				result.put(prod);
			}

		}

		return result;
	}

	public JSONArray getAvailableProductFeatures(Locale locale) throws JSONException, MalformedURLException, XmlRpcException {

		JSONArray result = null;
		
		Map<String, Object> mpParameters = new TreeMap<String, Object>();

		mpParameters.put("systemId", systemId);
		mpParameters.put("locale", locale);
		Map mpResult = invokeService(
				"catalogexport.search.getAvailableProductFeatures",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			Hashtable temp = new Hashtable((HashMap) mpResult
					.get("AvailableFeatures"));
			Enumeration en = temp.keys();

			while (en.hasMoreElements()) {
				String key = (String) en.nextElement();
				Hashtable temp2 = new Hashtable((Map) temp.get(key));

				if ((temp2 != null) && (temp2.size() > 0)) {
					String featureTypeId = (String) temp2.get("FeatureTypeID");
					Hashtable temp3 = new Hashtable((Map) temp2.get("Features"));

					JSONObject obj = new JSONObject();
					obj.put("featureTypeId", featureTypeId);
					obj.put("key", key);

					Enumeration en3 = temp3.keys();

					JSONArray features = new JSONArray();
					while (en3.hasMoreElements()) {
						String key3 = (String) en3.nextElement();
						String value = (String) ((Map) temp3.get(key3))
								.get("FeatureID");
						JSONObject objf = new JSONObject();
						objf.put("key", key3);
						objf.put("value", value);
						features.put(objf);
					}
					obj.put("features", features);
					result.put(obj);
				}
			}
		}

		return result;
	}

	public JSONArray getSortOrders(Locale locale) throws MalformedURLException, XmlRpcException, JSONException {

		JSONArray result = null;
		
		Map<String, Object> mpParameters = new TreeMap<String, Object>();

		mpParameters.put("systemId", systemId);
		mpParameters.put("userLocale", locale);
		Map mpResult = invokeService("catalogexport.search.getSortOrders",
				mpParameters);

		if (mpResult != null || !mpResult.isEmpty()) {
			Hashtable temp = new Hashtable((HashMap) mpResult.get("SortOrders"));
			Enumeration en = temp.keys();

			while (en.hasMoreElements()) {
				JSONObject obj = new JSONObject();
				String key = (String) en.nextElement();
				String value = (String) temp.get(key);
				obj.put(key, value);
				result.put(obj);
			}
		}

		return result;
	}






}
