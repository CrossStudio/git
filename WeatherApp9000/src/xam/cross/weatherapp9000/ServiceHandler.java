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
	
	public ServiceHandler(){
		
	}
	
	public String makeServiceCall (String url, int method){
		return this.makeServiceCall(url, method, null);
	}
	
	public String makeServiceCall (String url, int method, List<NameValuePair> parameters){
		try {
			//http Client
			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpEntity httpEntity = null;
			HttpResponse httpResponse = null;
			
			//Checking http request method type
			if (method == POST){
				HttpPost post = new HttpPost(url);
				//adding post parameters
				if (parameters != null){
					post.setEntity(new UrlEncodedFormEntity(parameters));
				}
				
				httpResponse = httpClient.execute(post);
			}
			else if (method == GET) {
                // appending params to url
                if (parameters != null) {
                    String paramString = URLEncodedUtils
                            .format(parameters, "utf-8");
                    url += "?" + paramString;
                }
                HttpGet httpGet = new HttpGet(url);
 
                httpResponse = httpClient.execute(httpGet);
 
            }
            httpEntity = httpResponse.getEntity();
            response = EntityUtils.toString(httpEntity);
 
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
         
        return response;
 
    }
	
	
}
