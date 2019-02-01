/*
 *
 */
package com.utility.api;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.Reporter;

import com.api.beans.Address;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.utility.constant.CmeApi;
import com.utility.db.DbManager;
import com.utility.selenium.ApplicationProperties;
import com.utility.selenium.BaseTestScript;

/**
 * The Class BaseApiClass.
 * This class contains all core API calls, and implementing all base level methods of API Automation.
 * This class is based class for all API Page Objects
 */
public class BaseApiClass
{

	/** The base api class. */
	private static BaseApiClass baseApiClass = null;

	/** The db manager. */
	protected static DbManager dbManager = new DbManager();
	/** The logger. */
	static Logger logger = Logger.getLogger(BaseApiClass.class);
	/**
	 * Instantiates a new base api class.
	 * @author GS-1629
	 */
	protected BaseApiClass()
	{
	}

	/**
	 * Gets the single instance of BaseApiClass.
	 * @author GS-1629
	 * @return single instance of BaseApiClass
	 */
	public static synchronized BaseApiClass getInstance()
	{
		if (baseApiClass == null)
		{
			baseApiClass = new BaseApiClass();
		}
		return baseApiClass;
	}

	/**
	 * Gets the token for authentication of users based on user role.
	 * @author GS-1629
	 * @param role the role
	 * @return the token
	 */
	// ============================== get EmailId , Token , UserId from Property ================================================================
	public String getToken(String role)
	{
		String token = "";

		if (role==null)
		{
			token = "";
			return token;
		}

		try
		{
			Properties prop = new Properties();
			prop.load(this.getClass().getClassLoader().getResourceAsStream("ApiConfigProperties/" + ApplicationProperties.getInstance().getProperty("api.token.propertyfile").trim()));
			token = prop.getProperty(role);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.info(e);
		}
		return token;
	}

	// ==============================================================================================================================

	/**
	 * Its takes Object(POJO) and convert it into JSON/XML, based on parameter and return it.
	 * @author GS-1629
	 * @param <T> the generic type
	 * @param object the object
	 * @param type the type
	 * @return the xml json
	 */
	// =============================== XMl Parsing - get fill Response object and send request XMl ===================================
	public <T> String getXmlJson(T object, String type)
	{
		try
		{
			if (type.equals(CmeApi.CONTENT_TYPE_XML) || type.equals(CmeApi.CONTENT_TYPE_TEXT_XML))
			{
				StringWriter stringWriter = new StringWriter();
				JAXBContext jc = JAXBContext.newInstance(object.getClass());
				Marshaller m = jc.createMarshaller();
				m.marshal(object, stringWriter);
				return stringWriter.toString();
			}
			else
			{
				Gson gson = new Gson();
				return gson.toJson(object);
			}
		}
		catch (Exception e)
		{
			logger.info(e);
		}
		return null;
	}

	/**
	 * It takes XML/JSON data as argument and convert it into POJO and return its object.
	 * @author GS-1629
	 * @param <T> the generic type
	 * @param responseFromServer the response from server
	 * @param type the type
	 * @param clasz the clasz
	 * @return the object
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> getObject(String responseFromServer, String type, Class<T> clasz)
	{
		try
		{
			if (type.equals(CmeApi.CONTENT_TYPE_XML) || type.equals(CmeApi.CONTENT_TYPE_TEXT_XML))
			{
				JAXBContext jc = JAXBContext.newInstance(clasz);
				Unmarshaller u = jc.createUnmarshaller();
				return (List<T>) u.unmarshal(new StreamSource(new StringReader(responseFromServer)), clasz).getValue();
			}
			else
			{
				Gson gson = new Gson();
				StringReader reader = new StringReader(responseFromServer);
				Type collectionType = com.google.gson.internal.$Gson$Types.newParameterizedTypeWithOwner(null, ArrayList.class, clasz);
				List<T> list = gson.fromJson(reader, collectionType);
				return list;
			}
		}
		catch (Exception e)
		{
			System.out.println(e);
			logger.info(e);
		}
		return null;
	}

	public Type getClass(String className)
	{
		if(className.equals("Example"))
		{
			return new TypeToken<Collection<Address>>(){}.getType();
		}
		else
		{
			return null;
		}
	}

	// ==============================================================================================================================

	/**
	 * Sets the connection @param method will execute API call takes data as arguments and execute call.
	 * @author GS-1629
	 * @param <T> the generic type
	 * @param resourceUrl the resource URL
	 * @param method the method
	 * @param acceptedHttpCode the accepted HTTP code
	 * @return the t
	 * @throws Exception the exception
	 */
	// =================================================== API call Main method ======================================================
	public synchronized <T> T setConnectionParam(String resourceUrl, String method, int acceptedHttpCode) throws Exception
	{
		return setConnectionParam(resourceUrl, method, acceptedHttpCode,null,null,null,false,null,null,null);
	}

	/**
	 * Sets the connection @param method will execute API call takes data as arguments and execute call
	 * @author GS-1629
	 * @param <T> the generic type
	 * @param resourceUrl the resource URL
	 * @param method the method
	 * @param acceptedHttpCode the accepted HTTP code
	 * @param userType the user type
	 * @return the t
	 * @throws Exception the exception
	 */
	public synchronized <T> T setConnectionParam(String resourceUrl, String method, int acceptedHttpCode, String userType) throws Exception
	{
		return setConnectionParam(resourceUrl, method, acceptedHttpCode,userType,null,null,false,null,null,null);
	}

	/**
	 * Sets the connection @param method will execute API call takes data as arguments and execute call
	 * @author GS-1629
	 * @param <T> the generic type
	 * @param resourceUrl the resource URL
	 * @param method the method
	 * @param acceptedHttpCode the accepted HTTP code
	 * @param userType the user type
	 * @param requestObj the request OBJ
	 * @return the t
	 * @throws Exception the exception
	 */
	public synchronized <T> T setConnectionParam(String resourceUrl, String method, int acceptedHttpCode, String userType, Object requestObj) throws Exception
	{
		return setConnectionParam(resourceUrl, method, acceptedHttpCode,userType,requestObj,null,false,null,null,null);
	}

	/**
	 * Sets the connection @param method will execute API call takes data as arguments and execute call
	 * @author GS-1629
	 * @param <T> the generic type
	 * @param resourceUrl the resource URL
	 * @param method the method
	 * @param acceptedHttpCode the accepted HTTP code
	 * @param userType the user type
	 * @param requestObj the request OBJ
	 * @param responseObj the response OBJ
	 * @return the t
	 * @throws Exception the exception
	 */
	public synchronized <T> T setConnectionParam(String resourceUrl, String method, int acceptedHttpCode, String userType, Object requestObj,Object responseObj) throws Exception
	{
		return setConnectionParam(resourceUrl, method, acceptedHttpCode,userType,requestObj,responseObj,false,null,null,null);
	}

	/**
	 *Sets the connection @param method will execute API call takes data as arguments and execute call
	 * @author GS-1629
	 * @param <T> the generic type
	 * @param resourceUrl the resource URL
	 * @param method the method
	 * @param acceptedHttpCode the accepted HTTP code
	 * @param userType the user type
	 * @param requestObj the request OBJ
	 * @param responseObj the response OBJ
	 * @param isDownloadFile the is download file
	 * @return the t
	 * @throws Exception the exception
	 */
	public synchronized <T> T setConnectionParam(String resourceUrl, String method, int acceptedHttpCode, String userType, Object requestObj,Object responseObj,boolean isDownloadFile) throws Exception
	{
		return setConnectionParam(resourceUrl, method, acceptedHttpCode,userType,requestObj,responseObj,isDownloadFile,null,null,null);
	}

	/**
	 * Sets the connection @param method will execute API call takes data as arguments and execute call
	 * @author GS-1629
	 * @param <T> the generic type
	 * @param resourceUrl the resource URL
	 * @param method the method
	 * @param acceptedHttpCode the accepted HTTP code
	 * @param userType the user type
	 * @param requestObj the request OBJ
	 * @param responseObj the response OBJ
	 * @param isDownloadFile the is download file
	 * @param contentType the content type
	 * @return the t
	 * @throws Exception the exception
	 */
	public synchronized <T> T setConnectionParam(String resourceUrl, String method, int acceptedHttpCode, String userType, Object requestObj,Object responseObj,boolean isDownloadFile,String contentType) throws Exception
	{
		return setConnectionParam(resourceUrl, method, acceptedHttpCode,userType,requestObj,responseObj,isDownloadFile,contentType,null,null);
	}

	/**
	 * Sets the connection @param method will execute API call takes data as arguments and execute call
	 * @author GS-1629
	 * @param <T> the generic type
	 * @param resourceUrl the resource URL
	 * @param method the method
	 * @param acceptedHttpCode the accepted HTTP code
	 * @param userType the user type
	 * @param requestObj the request OBJ
	 * @param responseObj the response OBJ
	 * @param isDownloadFile the is download file
	 * @param contentType the content type
	 * @param acceptType the accept type
	 * @return the t
	 * @throws Exception the exception
	 */
	public synchronized <T> T setConnectionParam(String resourceUrl, String method, int acceptedHttpCode, String userType, Object requestObj,Object responseObj,boolean isDownloadFile,String contentType, String acceptType) throws Exception
	{
		return setConnectionParam(resourceUrl, method, acceptedHttpCode,userType,requestObj,responseObj,isDownloadFile,contentType,acceptType,null);
	}

	/**
	 * Sets the connection @param method will execute API call takes data as arguments and execute call
	 * @author GS-1629
	 * @param <T> the generic type
	 * @param resourceUrl the resource URL as Base API URL (Mandatory)
	 * @param method the method Post/Get/Delete/Put (Mandatory)
	 * @param acceptedHttpCode the accepted HTTP code (Mandatory)
	 * @param userType the user type Admin/Non-Admin user
	 * @param requestObj the request OBJ as body part
	 * @param responseObj the response OBJ., Response will set into POJO
	 * @param isDownloadFile the is download file
	 * @param contentType the content type, XML/JSON
	 * @param acceptType the accept type, XML/JSON
	 * @param x_Forwarded_IP the x forwarded IP
	 * @return the t generic as return type.
	 * @throws Exception the exception
	 */
	@SuppressWarnings("unchecked")
	public synchronized <T> T setConnectionParam(String resourceUrl, String method, int acceptedHttpCode, String userType, Object requestObj, Object responseObj, boolean isDownloadFile, String contentType, String acceptType, String x_Forwarded_IP) throws Exception
	{

		HttpURLConnection conn;
		String response = null;
		SSLUtilities.trustAllHostnames();
		SSLUtilities.trustAllHttpsCertificates();
		//		URL url = new URL(BaseTestScript.APPLICATIONURL + resourceUrl);
		URL url = new URL(resourceUrl);
		conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod(method);

		if (contentType == null)
		{
			contentType = BaseTestScript.APICONTENTTYPE;
		}

		if (acceptType == null)
		{
			acceptType = BaseTestScript.APIACCEPTTYPE;
		}

		conn.setRequestProperty("Content-type", contentType + "; charset=utf-8");

		if (x_Forwarded_IP != null)
		{
			conn.addRequestProperty("x-forwarded-for", x_Forwarded_IP);
		}
		conn.setRequestProperty("Authorization", "Bearer " + BaseApiClass.getInstance().getToken(userType));

		if (!resourceUrl.endsWith("/batchid"))
		{
			conn.setRequestProperty("Accept", acceptType);
		}

		if (requestObj != null)
		{
			String requestBody = BaseApiClass.getInstance().getXmlJson(requestObj, contentType);
			logger.info("Request Body :: " + requestBody);
			OutputStream os = conn.getOutputStream();
			os.write(requestBody.getBytes());
			os.flush();
		}

		Scanner s = null;
		logger.info("Http Response Code From Server :: " + conn.getResponseCode());
		if (conn.getResponseCode() == 201 || conn.getResponseCode() == 200 || conn.getResponseCode() == 302)
		{
			if (isDownloadFile)
			{
				downloadFile(conn.getInputStream(), conn, resourceUrl);
			}
			else
			{
				s = new Scanner(conn.getInputStream());
				s.useDelimiter("\\Z");
				if (s.hasNext())
				{
					response = s.next();
				}
				logger.info("Response From Server :: " + response);
			}
		}
		else
		{
			if (conn.getErrorStream() != null)
			{
				s = new Scanner(conn.getErrorStream());
				s.useDelimiter("\\Z");
				if (s.hasNext())
				{
					response = s.next();
				}
			}
			logger.info("Response From Server :: " + response);
		}
		if (acceptedHttpCode == conn.getResponseCode())
		{
			logger.info("-> Test Case Passed ...");
		}
		else
		{
			logger.info("-> Test Case Failed");
			logger.info("Response From Server :: " + response);
			Assert.assertEquals(conn.getResponseCode(), acceptedHttpCode);
		}
		conn.disconnect();
		if (response == null || responseObj == null)
		{
			if (resourceUrl.endsWith("/batchid"))
			{
				return (T) response;
			}
			else
			{
				return null;
			}
		}

		if (isDownloadFile)
		{
			return (T) conn.getHeaderField("Content-Disposition");
		}
		else
		{
			return (T) BaseApiClass.getInstance().getObject(response, acceptType, responseObj.getClass());
		}
	}

	// for Third Party Services

	/**
	 * Sets the connection @param via multiple part for file upload API call.
	 * @author GS-1629
	 * @param resourceUrl the resource url
	 * @param requestParameterMap the request parameter map
	 * @param headerListMap the header list map
	 * @param fileParameter the file parameter
	 * @param file the file
	 * @param acceptedHttpCode the accepted http code
	 * @return the map
	 * @throws Exception the exception
	 */
	public synchronized Map<String, Object> setConnectionParamviaMultiplePart(String resourceUrl, Map<String, Object> requestParameterMap, Map<String, Object> headerListMap, String fileParameter, File[] file, int acceptedHttpCode) throws Exception
	{
		CloseableHttpClient client = HttpClientBuilder.create().build();
		// String uri = ApplicationProperties.getInstance().getProperty("test.url").trim() + resourceUrl;
		HttpPost httpPost = new HttpPost(resourceUrl);
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		String fileName = "";
		StringBuilder resultFromServer = null;
		SSLUtilities.trustAllHostnames();
		SSLUtilities.trustAllHttpsCertificates();

		for (Map.Entry<String, Object> entry : requestParameterMap.entrySet())
		{
			builder.addTextBody(entry.getKey(), String.valueOf(entry.getValue()));
		}

		if (file != null)
		{
			for (File f : file)
			{
				byte[] b = loadFileToByteArray(f);
				builder.addBinaryBody(fileParameter, b, ContentType.APPLICATION_OCTET_STREAM, fileName);
			}
		}

		Map<String, Object> resultMap = new HashMap<>();
		HttpEntity multipart = builder.build();
		httpPost.setEntity(multipart);
		CloseableHttpResponse response = client.execute(httpPost);
		String statusCode = String.valueOf(response.getStatusLine().getStatusCode());

		int statusCodeIninteger = Integer.parseInt(statusCode);

		logger.info("status code from server : " + statusCodeIninteger);

		HttpEntity entity = response.getEntity();
		InputStream is = entity.getContent();
		InputStreamReader isr = new InputStreamReader(is, "UTF-8");
		BufferedReader brRdr = new BufferedReader(isr);
		try
		{
			resultFromServer = new StringBuilder();
			String line = "";
			while ((line = brRdr.readLine()) != null)
			{
				resultFromServer.append(line);
			}
			resultMap.put(statusCode, resultFromServer.toString());
		}
		catch (Exception e)
		{
			// handle exception
		}
		finally
		{
			client.close();
			brRdr.close();
			isr.close();
			response.close();
		}

		if (statusCodeIninteger == 201 || statusCodeIninteger == 200 || statusCodeIninteger == 302)
		{
			logger.info("Positive Response From Server :: " + resultFromServer);
		}
		else
		{
			logger.info("Negative Response From Server :: " + resultFromServer);
		}

		if (acceptedHttpCode == statusCodeIninteger)
		{
			logger.info("-> Test Case Passed ...");
		}
		else
		{
			logger.info("-> Test Case Failed");
			Assert.assertEquals(statusCodeIninteger, acceptedHttpCode);
		}
		return resultMap;

	}

	/**
	 * HTTP URL encoded post method.
	 * @author GS-1629
	 * @param HeaderParmeter the header parameter
	 * @param thirdPartyBodyParmeter the third party body parameter
	 * @param url the URL
	 * @return the HTTP response
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClientProtocolException the client protocol exception
	 */
	public HttpResponse httpUrlEncodedPostMethod(HashMap<String, Object> HeaderParmeter, HashMap<String, Object> thirdPartyBodyParmeter, String url) throws IOException, ClientProtocolException
	{
		HttpClient client = HttpClients.createDefault();
		HttpPost post = new HttpPost(url + "?");
		post = iterateThirdPartyHeaderData(post, HeaderParmeter);
		post = iterateThirdPartyBodyData(post, thirdPartyBodyParmeter);
		HttpResponse response = client.execute(post);
		return response;
	}

	/**
	 * Iterate third party header data.
	 * @author GS-1629
	 * @param post the post
	 * @param thirdPartyServiceData the third party service data
	 * @return the HTTP post
	 */
	HttpPost iterateThirdPartyHeaderData(HttpPost post, HashMap<String, Object> thirdPartyServiceData)
	{
		if (thirdPartyServiceData != null
				&& thirdPartyServiceData.size() > 0)
		{
			for (Map.Entry<String, Object> entry : thirdPartyServiceData.entrySet())
			{
				post.setHeader(entry.getKey().trim(), entry.getValue().toString().trim());
			}
		}

		return post;
	}

	/**
	 * it iterate body data for post method.
	 * @author GS-1629
	 * @param post the post
	 * @param thirdPartyServiceData the third party service data
	 * @return the HTTP post
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	HttpPost iterateThirdPartyBodyData(HttpPost post, HashMap<String, Object> thirdPartyServiceData) throws IOException
	{

		if (thirdPartyServiceData != null && thirdPartyServiceData.size() > 0)
		{
			List<BasicNameValuePair> urlParameters = new ArrayList<BasicNameValuePair>();
			for (Map.Entry<String, Object> entry : thirdPartyServiceData.entrySet())
			{
				urlParameters.add(new BasicNameValuePair(entry.getKey().toString().trim(), entry.getValue().toString().trim()));
			}
			post.setEntity(new UrlEncodedFormEntity(urlParameters));
		}
		return post;
	}

	/**
	 * Load file to byte array.
	 * @author GS-1629
	 * @param file the file
	 * @return the byte[]
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	byte[] loadFileToByteArray(File file) throws IOException
	{
		InputStream is = null;
		try
		{
			is = new FileInputStream(file);
			long length = file.length();
			byte[] bytes = new byte[(int) length];
			int x = is.read(bytes);
			while (x != -1)
			{
				x = is.read(bytes);
			}
			return bytes;
		}
		finally
		{
			is.close();
		}
	}
	///////////////////////////////////////////

	/**
	 * Download file.
	 * @author GS-1629
	 * @param in the in
	 * @param httpCon the http con
	 * @param url the url
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void downloadFile(InputStream in, HttpURLConnection httpCon, String url) throws IOException
	{
		String fileName = "", extension = "";
		String disposition = httpCon.getHeaderField("Content-Disposition");
		FileOutputStream outputStream = null;
		InputStream input = null;
		File tempFile = null;
		int index = disposition.indexOf("filename=");
		if (index > 0)
		{
			fileName = disposition.substring(index + 10, disposition.length() - 1);
			extension = fileName.substring(fileName.indexOf(".") + 1);
		}
		boolean isValidFileName = checkFileName(url, fileName);
		if (isValidFileName)
		{
			fileName = fileName.substring(0, fileName.lastIndexOf("."));
			tempFile = File.createTempFile(fileName, ".tmp");
			outputStream = new FileOutputStream(tempFile);
			logger.info(tempFile.getAbsolutePath());
			byte[] image = IOUtils.toByteArray(in);
			input = new ByteArrayInputStream(image);
			int bytesRead = -1;
			byte[] buffer = new byte[image.length];
			while ((bytesRead = input.read(buffer)) != -1)
			{
				outputStream.write(buffer, 0, bytesRead);
			}
			if (!extension.contains(CmeApi.FILE_TYPE_PDF))
			{

				if (checkFileSize(tempFile.length(), url))
				{
					logger.info("File size matched.");
				}
				else
				{
					logger.error("File size not matched.");
					Assert.assertFalse(true);
				}
			}
		}
		else
		{
			logger.error("File name doesnot match.");
			Assert.assertFalse(true);
		}
		if (tempFile != null) {
			tempFile.deleteOnExit();
		}
		if (outputStream != null) {
			outputStream.close();
		}
		if (input != null) {
			input.close();
		}

		logger.info("File downloaded");
		httpCon.disconnect();
	}

	/**
	 * Check file name.
	 * @author GS-1629
	 * @param url the url
	 * @param fileName the file name
	 * @return true, if successful
	 */
	private static boolean checkFileName(String url, String fileName)
	{
		String fileID = "";
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		Pattern pattern = Pattern.compile(CmeApi.DOWNLOAD_FILE_GET_FILEID_PATTERN);
		Matcher matcher = pattern.matcher(url);
		if (matcher.find()) {
			fileID = matcher.group(1);
		}

		try
		{
			String sql = "select docName from gfn_document where id=" + fileID;
			Object fileNameDatabase = dbManager.getResult(sql);

			if (fileNameDatabase.toString().equals(fileName))
			{
				logger.info("File name matched.");
				return true;
			}
			else
			{
				return false;
			}
		}
		catch (Exception e)
		{
			Assert.assertFalse(true);
		}
		return false;
	}

	/**
	 * Check file size.
	 * @author GS-1629
	 * @param length the length
	 * @param url the url
	 * @return true, if successful
	 */
	private static boolean checkFileSize(Long length, String url)
	{
		String fileID = "";
		Long fileSizeDatabase = 0L;
		Pattern pattern = Pattern.compile(CmeApi.DOWNLOAD_FILE_GET_FILEID_PATTERN);
		Matcher matcher = pattern.matcher(url);

		if (matcher.find()) {
			fileID = matcher.group(1);
		}

		try
		{
			String sql = "select fileSize from gfn_document inner join gfn_documentVersion "
					+ "on gfn_document.id=gfn_documentVersion.docID where gfn_document.id=" + fileID;

			Object fileSize = dbManager.getResult(sql);
			fileSizeDatabase = (Long) fileSize;

			if (fileSizeDatabase.equals(length)) {
				return true;
			} else {
				return false;
			}
		}
		catch (Exception e)
		{
			Assert.assertFalse(true);
		}

		return false;
	}

	// ==============================================================================================================================
	// ===================================Methods to get value From DB Ends================================

	/**
	 * Gets the string from stack trace.
	 * @author GS-1629
	 * @param e the e
	 * @return the string from stack trace
	 */
	public static String getStringFromStackTrace(Exception e)
	{
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		String stackTrace = sw.toString();
		return stackTrace;
	}

	/**
	 * Gets the start date time stamp.
	 * @author GS-1629
	 * @return the start date time stamp
	 * @throws InterruptedException the interrupted exception
	 */
	public static Timestamp getStartDateTimeStamp() throws InterruptedException
	{
		Timestamp timeStamp = null;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

		try
		{
			Object value = dbManager.getResult(CmeApi.GET_TIME_STAMP_QUERY);

			if (value != null)
			{
				timeStamp = new java.sql.Timestamp(sdf.parse(value.toString()).getTime());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return timeStamp;
	}

	/**
	 * Gets the end date time stamp.
	 * @author GS-1629
	 * @return the end date time stamp
	 * @throws Exception the exception
	 */
	public static Timestamp getEndDateTimeStamp() throws Exception
	{
		return new Timestamp(getStartDateTimeStamp().getTime() + CmeApi.ADD_TIME_DELAY);
	}

	/**
	 * Show and log message.
	 * @author GS-1629
	 * @param msg the msg
	 */
	public static void showAndLogMessage(String msg)
	{
		logger.info(msg);
		Reporter.log(msg);
	}

	/**
	 * Pre check passed report log.
	 * @author GS-1629
	 */
	public static void preCheckPassed_ReportLog()
	{
		BaseApiClass.showAndLogMessage(CmeApi.PRE_CHECK_PASSED);
	}

	/**
	 * Date and system time.
	 * @author GS-1629
	 * @param dateAndTimeFormat the date and time format
	 * @return the string
	 */
	public static String dateAndSystemTime(String dateAndTimeFormat)
	{
		// get Current UTC date and Time
		return dateAndSystemTime(dateAndTimeFormat, 0);
	}

	/**
	 * Date and system time.
	 * @author GS-1629
	 * @param dateAndTimeFormat the date and time format
	 * @param noOffDay the no off day
	 * @return the string
	 */
	public static String dateAndSystemTime(String dateAndTimeFormat, int noOffDay)
	{
		// get UCT of time After or before specified days

		SimpleDateFormat formattedDate = new SimpleDateFormat(dateAndTimeFormat);
		formattedDate.setTimeZone(TimeZone.getTimeZone("UTC"));
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DATE, noOffDay);
		Date date = c.getTime();
		return formattedDate.format(date);
	}

}
