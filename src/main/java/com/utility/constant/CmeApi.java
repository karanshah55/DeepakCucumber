/*
 *
 */
package com.utility.constant;

/**
 * The Class CmeApi.
 */
public class CmeApi {

	/**
	 * Instantiates a new CME API Constant page.
	 * @author GS-1629
	 */
	private CmeApi()
	{
		// Do Nothing
	}

	// ========================================== Api Content type / Accept type ==============================================================
	/** The Constant CONTENT_TYPE_JSON. */
	public static final String CONTENT_TYPE_JSON = "application/json";

	/** The Constant CONTENT_TYPE_XML. */
	public static final String CONTENT_TYPE_XML = "application/xml";

	/** The Constant ACCEPT_TYPE_XML. */
	public static final String ACCEPT_TYPE_XML = CONTENT_TYPE_XML;

	/** The Constant CONTENT_TYPE_TEXT_XML. */
	public static final String CONTENT_TYPE_TEXT_XML = "text/xml";

	/** The Constant ACCEPT_TYPE_TEXT_PLAIN. */
	public static final String ACCEPT_TYPE_TEXT_PLAIN = "text/plain";

	/** The Constant ACCEPT_TYPE_TEXT_XML. */
	public static final String ACCEPT_TYPE_TEXT_XML = CONTENT_TYPE_TEXT_XML;

	// ============================================== common debug / warn Messages ==========================================================================
	/** The Constant PRE_CHECK_PASSED. */
	public static final String PRE_CHECK_PASSED = "-> PreCheck Passed...";

	/** The Constant POST_CHECK_PASSED. */
	public static final String POST_CHECK_PASSED = "-> PostCheck Passed...";

	//============================================== timing log Messages ====================================================================================
	/** The Constant ADD_TIME_DELAY. */
	public static final long ADD_TIME_DELAY = 3000L;

	/** The Constant GET_TIME_STAMP_QUERY. */
	public static final String GET_TIME_STAMP_QUERY = "SELECT GETUTCDATE()";

	//================================================ file and its types ===================================================================================
	/** The Constant FILE_TYPE_PDF. */
	public static final String FILE_TYPE_PDF = "pdf";

	/** The Constant FILE_TYPE_DOCX. */
	public static final String FILE_TYPE_DOCX = "docx";

	/** The Constant FILE_TYPE_DOC. */
	public static final String FILE_TYPE_DOC = "doc";

	/** The Constant FILE_TYPE_XML. */
	public static final String FILE_TYPE_XML = "xml";

	/** The Constant FILE_TYPE_XLS. */
	public static final String FILE_TYPE_XLS = "xls";

	/** The Constant DOWNLOAD_FILE_GET_FILEID_PATTERN. */
	public static final String DOWNLOAD_FILE_GET_FILEID_PATTERN = "/files/(.*?)/content";
}
