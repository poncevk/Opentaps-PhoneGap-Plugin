/**
 * 
 */
package net.enala.phonegap.plugins.opentaps;

import java.net.MalformedURLException;
import java.util.Date;
import java.util.Locale;

import net.enala.phonegap.plugins.opentaps.services.OpentapsServices;

import org.apache.cordova.api.PluginResult;
import org.apache.cordova.api.PluginResult.Status;
import org.apache.xmlrpc.XmlRpcException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.phonegap.api.Plugin;

/**
 * @author Jesus Ponce
 *
 */
public class OpentapsPlugin extends Plugin {

	/* (non-Javadoc)
	 * @see org.apache.cordova.api.Plugin#execute(java.lang.String, org.json.JSONArray, java.lang.String)
	 */
	@Override
	public PluginResult execute(String action, JSONArray data, String callback) {
		
		Log.d("OpentapsPlugin", "Plugin called");
		
		PluginResult result = null;
		
		OpentapsServices services = new OpentapsServices(
				ctx.getContext().getString(R.string.opentaps_url), 
				ctx.getContext().getString(R.string.opentaps_system_id), 
				ctx.getContext().getString(R.string.opentaps_product_store_id));
		
		try {
			if (action.equals("getAvailablePromotions")) {
				result = new PluginResult(Status.OK, services.getAvailablePromotions());
			} else if (action.equals("getBrowseRootCategoryId")) {
				result = new PluginResult(Status.OK, services.getRootCategoryId(data.getString(0)));
			} else if (action.equals("getCategoryHierarchy")) {
				result = new PluginResult(Status.OK, services.getCategoryHierarchy(data.getString(0), data.getString(1), Locale.getDefault()));
			} else if (action.equals("getCategory")) {
				result = new PluginResult(Status.OK, services.getCategory(data.getString(0), data.getString(1), Locale.getDefault()));
			} else if (action.equals("getProduct")) {
				result = new PluginResult(Status.OK, services.getProduct(data.getString(0), data.getString(1), Locale.getDefault()));
			} else if (action.equals("performExternalLogin")) {
				result = new PluginResult(Status.OK, services.performExternalLogin(data.getString(0), data.getString(1)));
			} else if (action.equals("getAssociatedProducts")) {
				result = new PluginResult(Status.OK, services.getAssociatedProducts(data.getString(0), data.getString(1), Locale.getDefault()));
			} else if (action.equals("getCategoryContent")) {
				result = new PluginResult(Status.OK, services.getCategoryContent(data.getString(0), Locale.getDefault()));
			} else if (action.equals("getCategoryDescription")) {
				result = new PluginResult(Status.OK, services.getCategoryDescription(data.getString(0), Locale.getDefault()));
			} else if (action.equals("getCategoryName")) {
				result = new PluginResult(Status.OK, services.getCategoryName(data.getString(0), Locale.getDefault()));
			} else if (action.equals("getProductCatalogIds")) {
				result = new PluginResult(Status.OK, services.getProductCatalogIds());
			} else if (action.equals("getProductContent")) {
				result = new PluginResult(Status.OK, services.getProductContent(data.getString(0), Locale.getDefault()));
			} else if (action.equals("getProductDescription")) {
				result = new PluginResult(Status.OK, services.getProductDescription(data.getString(0), Locale.getDefault()));
			} else if (action.equals("getProductName")) {
				result = new PluginResult(Status.OK, services.getProductName(data.getString(0), Locale.getDefault()));
			} else if (action.equals("getProductPrices")) {
				result = new PluginResult(Status.OK, services.getProductPrices(data.getString(0), data.getString(1), Locale.getDefault()));
			} else if (action.equals("getProductKeywords")) {
				result = new PluginResult(Status.OK, services.getProductKeywords(data.getString(0)));
			} else if (action.equals("getProductTypes")) {
				result = new PluginResult(Status.OK, services.getProductTypes(Locale.getDefault()));
			} else if (action.equals("getSpecialCategoryIds")) {
				result = new PluginResult(Status.OK, services.getSpecialCategoryIds(data.getString(0)));
			} else if (action.equals("getProductPrices")) {
				result = new PluginResult(Status.OK, services.getProductPrices(data.getString(0), data.getString(1), Locale.getDefault()));
			} else if (action.equals("findProductsByKeyword")) {
				result = new PluginResult(Status.OK, services.findProductsByKeyword(data.getString(0), data.getString(1), Locale.getDefault()));
			} else if (action.equals("findRecentProducts")) {
				result = new PluginResult(Status.OK, services.findRecentProducts(data.getString(0), data.getInt(1), (Date)data.get(2), (Date)data.get(3), Locale.getDefault()));
			} else if (action.equals("getAvailableProductFeatures")) {
				result = new PluginResult(Status.OK, services.getAvailableProductFeatures(Locale.getDefault()));
			} else if (action.equals("getSortOrders")) {
				result = new PluginResult(Status.OK, services.getSortOrders(Locale.getDefault()));
			} else {
				result = new PluginResult(Status.INVALID_ACTION);
				Log.d("OpentapsPlugin", "Invalid action : "+action+" passed");
			}
		} catch (MalformedURLException e) {
			Log.d("OpentapsPlugin", "Got Exception "+ e.getMessage());
			result = new PluginResult(Status.ERROR);
		} catch (XmlRpcException e) {
			Log.d("OpentapsPlugin", "Got Exception "+ e.getMessage());
			result = new PluginResult(Status.ERROR);
		} catch (JSONException e) {
			Log.d("OpentapsPlugin", "Got Exception "+ e.getMessage());
			result = new PluginResult(Status.ERROR);
		}
		
		return result;
	}

}
