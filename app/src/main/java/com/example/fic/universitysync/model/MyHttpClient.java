package com.example.fic.universitysync.model;

import android.graphics.drawable.Drawable;
import android.os.Build;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by fic on 10.9.2014.
 */
public class MyHttpClient {
    public static String SERVER_URL = "http://www.universitysync.sk/android/";

    private static MyHttpClient mInstance;
    private static DefaultHttpClient mHttpClient;

    private MyHttpClient() {
        mHttpClient = new DefaultHttpClient();
    }

    public static MyHttpClient getInstance() {
        if (mInstance == null) {
            mInstance = new MyHttpClient();
        }
        return mInstance;
    }

    public JSONObject makeJSONObject(Map<String, Object> attributes) {
        try {
            JSONObject object = new JSONObject(attributes);
            JSONObject header = new JSONObject();
            header.put("deviceModel", Build.MODEL);
            header.put("deviceVersion", Build.VERSION.RELEASE);
            header.put("language", Locale.getDefault().getISO3Language());
            //object.put("header", header);
            return object;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Makes GET request to web server and waits for the response
     * @param list
     * @param scriptName
     * @return
     */
    public JSONObject get(List<NameValuePair> list, String scriptName) {
        String url = MyHttpClient.SERVER_URL + scriptName;
        try {
            if (list != null) {
                for (int i = 0; i < list.size(); i ++) {
                    NameValuePair attr = list.get(i);
                    url += (i == 0 ? "?" : "&") + attr.getName() + "=" + URLEncoder.encode(attr.getValue(), "UTF-8");
                }
            }
            return new JSONObject(getResultString(url));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject get(String path) {
        try {
            JSONObject jsonObj = XML.toJSONObject(getResultString(SERVER_URL + path));
            return jsonObj;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject post(List<NameValuePair> list, String scriptName) {
        HttpPost post = new HttpPost(MyHttpClient.SERVER_URL + scriptName);
        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list);
            entity.setContentEncoding(HTTP.UTF_8);
            post.setEntity(entity);
            HttpResponse response = mHttpClient.execute(post);
            InputStream inputStream = response.getEntity().getContent();
            String resultString = convertStreamToString(inputStream);
            inputStream.close();
            return new JSONObject(resultString);
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getResultString(String url) {
        try {
            InputStream inputStream = getInputStream(url);
            if (inputStream != null) {
                String resultString = convertStreamToString(inputStream);
                inputStream.close();
                return resultString;
            }
            else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private InputStream getInputStream(String url) {
        try {
            HttpGet get = new HttpGet(url);
            HttpResponse response = mHttpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                return response.getEntity().getContent();
            }
            else
                return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String convertStreamToString(InputStream inputStream) {
        String line;
        StringBuilder str = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
        try {
            while ((line = rd.readLine()) != null) {
                str.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str.toString();
    }

    public Drawable getImage(String path) {
        InputStream inputStream = getInputStream(SERVER_URL + path);
        if (inputStream != null) {
            Drawable image = Drawable.createFromStream(inputStream, path);
            try {
                inputStream.close();
                return image;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
