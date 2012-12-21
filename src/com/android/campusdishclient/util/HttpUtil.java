package com.android.campusdishclient.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.FormatFlagsConversionMismatchException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.campusdishclient.R;
import com.android.campusdishclient.data.Dish;

public class HttpUtil {

	public static  String BASE_URL = "http://10.0.2.2:80/";
//	public static final String BASE_URL = "http://192.168.1.155:80/";
//	public static final String BASE_URL = "http://192.168.42.252:80/";
	public static final char SEPARATECHAR = ']';

	private static HttpClient mHttpClient;

	/**
	 * 获得静态的 {@link org.apache.http.client.HttpClient HttpClient} 对象
	 * 
	 * @return HttpClient 对象
	 */
	public static HttpClient getHttpClient() {
		if (mHttpClient == null) {
			mHttpClient = new DefaultHttpClient();
		}
		return mHttpClient;
	}

	/**
	 * 通过给定的地址获得相应的Html文件
	 * 
	 * @param uri
	 *            地址
	 * @return 获得的Html文本文件字符串
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getRequest(String uri) throws ClientProtocolException,
			IOException {
		TotalUtils.Log("获得地址:" + uri);
		HttpGet get = new HttpGet(uri);
		HttpResponse httpResponse;
		try{
			httpResponse = getHttpClient().execute(get);
		}catch(Exception e){
	
			return null;
		}
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
		}
		return null;
	}

	/**
	 * 通过dishID获得Dish对象
	 * 
	 * @param pDishId
	 *            dish的ID
	 * @return 获得的Dish
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static Dish getDishById(int pDishId) throws ClientProtocolException,
			IOException {

		Dish dish = new Dish(pDishId);
		TotalUtils.Log(pDishId);
		String[] string = null;
		if (pDishId == -1) {
			dish.setDishName("内部错误");
			dish.setDishDetail("内部错误");
		} else {
			try{
				string = HttpUtil.getResponseArray(HttpUtil
						.getRequest(HttpUtil.BASE_URL + "getDish.aspx?dishId="
								+ String.valueOf(pDishId)));
			}catch(NullPointerException e){
				e.printStackTrace();
				return null;
			}
			if(string==null){
				return null;
			}
			try
			{
				dish.setPrice(Float.valueOf(string[HTTP_PRICE].trim().equals("")?"0":string[HTTP_PRICE].trim()));
				
			}catch(NumberFormatException e)
			{
				dish.setPrice(0.0f);
			}
			try
			{
				dish.setDishGrade(Double.valueOf(string[HTTP_GRADE].trim().equals("")?"0":string[HTTP_GRADE].trim()));
			}catch(NumberFormatException e)
			{
				dish.setDishGrade(0.0);
			}
			dish.setDishName(string[HTTP_DISHNAME]);
			dish.setDishImagePath(string[HTTP_IMAGEPATH]);
			dish.setDishDetail(string[HTTP_DETAIL]);
			
			// TotalUtils.Log("获得价格："+String.valueOf(Float.valueOf(string[4])));
		}
		return dish;
	}

	/**
	 * 将获得的文本分开
	 * 
	 * @param pResponse
	 *            获得的文本
	 * @return 分离过后形成的数组
	 */
	public static String[] getResponseArray(String pResponse) {
		// int index=0,i=0;
		// ArrayList<String> string=new ArrayList<String>();
		// while((index=pResponse.indexOf(SEPARATECHAR))!=-1){
		// // string.add(pResponse.split(regularExpression));
		// }
		if(pResponse==null){
			return null;
		}
		String[] str=pResponse.split(String.valueOf(SEPARATECHAR));
		if(str==null){
			throw new NullPointerException("服务器响应异常！");
		}
		return pResponse.split(String.valueOf(SEPARATECHAR));
	}

	/**
	 * 通过类别ID获取类别下产品
	 * 
	 * @param pClassId
	 *            类别ID
	 * @return 该类别下产品
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static ArrayList<Integer> getDishIdByClass(int pClassId)
			throws ClientProtocolException, IOException {
		String[] string = null;
		string = HttpUtil.getResponseArray(HttpUtil
				.getRequest(HttpUtil.BASE_URL + "getDishsByClass.aspx?classId="
						+ String.valueOf(pClassId)));
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for (int i = 0; i < string.length; i++) {
			try{
				arr.add(Integer.valueOf(string[i]));
			}catch(Exception e){
				
			}	
		}
		return arr;
	}

	/**
	 * 获得图片,网络错误则返回null
	 * 
	 * @param pImageName
	 *            图片名称
	 * @return 图片Bitmap;
	 * @throws IOException
	 * 
	 */
	public static Bitmap getImageBitmap(String pImageName) throws IOException {
		URL url;
		
		url = new URL(BASE_URL + "picture/" + java.net.URLEncoder.encode(pImageName));
		
//		TotalUtils.Log("获取图片" + BASE_URL + "picture/" + pImageName);
		TotalUtils.Log(url.toString());
		InputStream in = url.openStream();
		Bitmap bit = BitmapFactory.decodeStream(in);
		in.close();
		return bit;

	}

	public static String postRequest(String url, Map<String, String> pParams)
			throws ClientProtocolException, IOException {
		HttpPost post = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : pParams.keySet()) {
			params.add(new BasicNameValuePair(key, java.net.URLEncoder.encode(pParams.get(key))));
		}
		post.setEntity(new UrlEncodedFormEntity(params, "gbk"));
		HttpResponse httpResponse = mHttpClient.execute(post);
		if (httpResponse.getStatusLine().getStatusCode() == 200) {
			String result = EntityUtils.toString(httpResponse.getEntity());
			return result;
		}
		return null;
	}

	public static ArrayList<Integer> getClassId(int index) {
		return null;
	}
	public static final int HTTP_DISHNAME=1;
	public static final int HTTP_IMAGEPATH=4;
	public static final int HTTP_DETAIL=3;
	public static final int HTTP_PRICE=2;
	public static final int HTTP_GRADE=5;
}
