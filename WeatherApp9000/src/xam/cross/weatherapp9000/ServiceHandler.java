package xam.cross.weatherapp9000;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class ServiceHandler {

	static String response = "";
	public final static int GET = 0;
	public final static int POST = 1;
	
	/**
	 * Overloaded makeServiceCall which calls makeServiceCall with null parameter in place of name value pairs
	 * @param url - url to be used in http call
	 * @param method - either GET or POST method
	 * @return response from server as String
	 */
	public String makeServiceCall (String url, int method){
		return this.makeServiceCall(url, method, null);
	}
	
	/**
	 * Makes a http call for a given url, method and parameters
	 * @param url - url to be used in http call
	 * @param method - either GET or POST method
	 * @param parameters - parameters to be added to the http call
	 * @return response from server as String
	 */
	public String makeServiceCall (String url, int method, List<NameValuePair> parameters){
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			
            if (parameters != null) {
	            String paramString = URLEncodedUtils
	                    .format(parameters, "utf-8");
	            url += "?" + paramString;
            }
            HttpGet httpGet = new HttpGet(url);
 
            httpResponse = httpClient.execute(httpGet);

            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
 
        }
		catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
		catch (ClientProtocolException e) {
            e.printStackTrace();
        }
		catch (IOException e) {
            e.printStackTrace();
        }
         
        return response;
 
    }
	
	
}
