/*
 *
 */
package com.utility.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessRead;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.testng.Reporter;

import com.utility.constant.SeleniumConst;
import com.utility.selenium.BasePageObject;
import com.utility.selenium.BaseTestScript;
import com.utility.selenium.BasePageObject;

/**
 * The Class Downloaded_Verifier.
 */
public class Downloaded_Verifier extends BaseTestScript
{

	/** The logger. */
	Logger logger = Logger.getLogger(Downloaded_Verifier.class);

	/**
	 * Verify document downloaded.
	 *
	 * @author GS-1629
	 * @param docName the doc name
	 * @return true, if successful
	 * @throws InterruptedException the interrupted exception
	 */
	public boolean verifyDocumentDownloaded(String docName) throws InterruptedException
	{
		return verifyDocumentDownloaded(docName, 0);
	}

	/**
	 * Verify document downloaded.
	 *
	 * @author GS-1629
	 * @param docName the doc name
	 * @param timeInMintutes the time in mintutes
	 * @return true, if successful
	 * @throws InterruptedException the interrupted exception
	 */
	public boolean verifyDocumentDownloaded(String docName, int timeInMintutes) throws InterruptedException

	{
		int time;
		if (timeInMintutes == 0)
		{
			time = BasePageObject.Seconds;
		}
		else
		{
			time = timeInMintutes * 60;
		}
		int count = 1;
		timeout(2);
		logger.info("In Verify Documnent Download :-" + docName);
		String dwnPath = SeleniumConst.FILE_PATH_DOWNLOADED + docName;
		File file = new File(dwnPath);
		while (count <= time)
		{
			if (file.exists() && !file.isDirectory())
			{
				return true;
			}
			timeout(1);
			count++;
		}
		return false;
	}

	/**
	 * This method will create ZIP of generated report
	 * All failuer screenshots and report.html page will wrapr into zip and
	 * finally it will ready to send as attachment in mail.
	 * @author GS-1629
	 */
	public void ZipCreation()
	{
		ArrayList<String> fileList = new ArrayList<>();
		fileList = generateFileList(new File(BaseTestScript.REPORTLOCATION), fileList);
		zipIt(BaseTestScript.REPORTLOCATION+"\\CME-EDB-Automation-Report.zip", fileList);
	}

	/**
	 * Helper method for creating Zip based on its two @Param.
	 * @author GS-1629
	 * @param zipFile output ZIP file location
	 * @param fileList the file list
	 */
	private void zipIt(String zipFile, ArrayList<String> fileList){

		byte[] buffer = new byte[1024];
		try{
			FileOutputStream fos = new FileOutputStream(zipFile);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for(String file : fileList){
				ZipEntry ze= new ZipEntry(file);
				zos.putNextEntry(ze);
				FileInputStream in = new FileInputStream(BaseTestScript.REPORTLOCATION + File.separator + file);
				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				in.close();
			}
			zos.closeEntry();
			zos.close();
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}

	/**
	 * Traverse a directory and get all files,
	 * and add the file into fileList.
	 * @author GS-1629
	 * @param node file or directory
	 * @param fileList the file list
	 * @return the array list
	 */
	private ArrayList<String> generateFileList(File node, ArrayList<String> fileList){
		//add file only
		if(node.isFile()){
			fileList.add(generateZipEntry(node.getAbsoluteFile().toString()));
		}
		if(node.isDirectory()){
			String[] subNote = node.list();
			for(String filename : subNote){
				generateFileList(new File(node, filename), fileList);
			}
		}
		return fileList;
	}

	/**
	 * Format the file path for zip.
	 * @author GS-1629
	 * @param file file path
	 * @return Formatted file path
	 */
	private String generateZipEntry(String file){
		return file.substring(BaseTestScript.REPORTLOCATION.length()+1, file.length());
	}

	/**
	 * Verify document from zip folder downloaded.
	 *
	 * @author GS-1629
	 * @param zipFolderName the zip folder name
	 * @param name the name
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public boolean verifyDocumentFromZipFolderDownloaded(String zipFolderName, String name) throws IOException, InterruptedException
	{
		String dwnPath = SeleniumConst.FILE_PATH_DOWNLOADED + zipFolderName;
		timeout(4);
		ZipFile zipFile = new ZipFile(dwnPath);
		try
		{
			Enumeration<?> entries = zipFile.entries();
			while (entries.hasMoreElements())
			{
				ZipEntry ze = (ZipEntry) entries.nextElement();
				if (ze.getName().trim().equalsIgnoreCase(name.trim()))
				{
					return true;
				}
			}
		}
		catch (Exception e) {
			logger.error("something wrong with zip file : "+e.getMessage());
		}
		finally
		{
			zipFile.close();
		}
		return false;

	}

	/**
	 * Verify downloaded excel data.
	 * @author GS-1629
	 * @param verifyXslxDatalist the verify xslx datalist
	 * @param exportedDatalist the exported datalist
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean verifyDownloadedExcelData(List<String> verifyXslxDatalist, List<String> exportedDatalist) throws IOException
	{
		for (String value : verifyXslxDatalist)
		{
			int flag = 0;
			for (String valueFromList : exportedDatalist)
			{
				if (value.equalsIgnoreCase(valueFromList))
				{
					logger.info("Column value is available in Exported Data :" + value);
					break;
				}
				flag++;
			}
			if (flag == exportedDatalist.size())
			{
				logger.info("Column value is unavailable in Exported Data :" + value);

				return false;
			}
		}
		return true;
	}

	/**
	 * Delete downloaded file OR folder.
	 *
	 * @author GS-1629
	 * @param docPath the doc path
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public void deleteDownloadedFileORFolder(String docPath) throws IOException, InterruptedException
	{
		logger.info("In Delete Downloaded Document");

		// For File Delete Must have to Pass File name With its Extension i.e Test.txt
		timeout(2);
		File directory = new File(SeleniumConst.FILE_PATH_DOWNLOADED + docPath);

		if (directory.isDirectory())
		{
			String[] list = directory.list();
			for (int i = 0; i < list.length; i++)
			{
				File entry = new File(directory, list[i]);
				entry.delete();
			}
			directory.delete();
		}
		else if (directory.isFile())
		{
			directory.delete();
		}
		else
		{
			logger.info("File OR Folder Not Found.");
		}
	}

	/**
	 * Delete all files from directory.
	 * @author GS-1629
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void deleteAllFilesFromDirectory(String filePath) throws IOException
	{
		logger.info("In Delete All Files From Directory.");
		try {
			File directory = new File(filePath);
			File[] files = directory.listFiles();

			for (File file : files)
			{
				if (!file.delete())
				{
					logger.info("Failed to delete " + file);
				}
			}
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	}

	/**
	 * Move all files from directory.
	 * @author GS-1629
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void moveAllFilesFromDirectory() throws IOException
	{
		String dwnPath = SeleniumConst.FILE_PATH_DOWNLOADED;

		String downloadedFilePathnew = SeleniumConst.SYSTEM_USER_HOME + "\\Downloads_backup";

		File source = new File(dwnPath);
		File destination = new File(downloadedFilePathnew);

		Path dir = Paths.get(downloadedFilePathnew);

		if (!Files.exists(dir))
		{
			Files.createDirectories(dir);
		}
		else
		{
			logger.info("Directory is Already Exists...!!!");
		}

		try
		{
			File[] onlyFiles = source.listFiles((FileFilter) FileFileFilter.FILE);
			for (File file : onlyFiles)
			{
				InputStream in = new FileInputStream(file);
				OutputStream out = new FileOutputStream(destination + "/" + file.getName());
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0)
				{
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		}
		catch (Exception e)
		{
			logger.info("Exception:-", e);
		}
		deleteAllFilesFromDirectory(dwnPath);
	}

	public void moveFileFromSourceToDestination(String filename, String source, String destination)
	{
		File destinationFolder = new File(destination);
		File sourceFolder = new File(source);

		if (!destinationFolder.exists())
		{
			destinationFolder.mkdirs();
		}

		// Check weather source exists and it is folder.
		if (sourceFolder.exists() && sourceFolder.isDirectory())
		{
			// Get list of the files and iterate over them
			File[] listOfFiles = sourceFolder.listFiles();

			if (listOfFiles != null)
			{
				for (File child : listOfFiles )
				{
					// Move files to destination folder
					if(child.getName().equalsIgnoreCase(filename)) {
						child.renameTo(new File(destinationFolder + "\\" + child.getName()));
					}
				}
			}
		}
		else
		{
			logger.info(sourceFolder + "  Folder does not exists");
		}
	}
	/**
	 * Extract zip folder.
	 *
	 * @author GS-1629
	 * @param zipFolderName the zip folder name
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public void extractZipFolder(String zipFolderName) throws IOException, InterruptedException
	{
		timeout(3);
		byte[] buffer = new byte[1024];
		String sourcePath = SeleniumConst.FILE_PATH_DOWNLOADED + zipFolderName;
		File file = new File(sourcePath);
		String destName = getFileWithoutExtension(file);
		String destination = SeleniumConst.FILE_PATH_DOWNLOADED + destName;

		ZipInputStream zis = new ZipInputStream(new FileInputStream(sourcePath));
		ZipEntry ze = zis.getNextEntry();

		while (ze != null)
		{
			String fileName = ze.getName();
			File newFile = new File(destination + File.separator + fileName);
			new File(newFile.getParent()).mkdirs();
			FileOutputStream fos = new FileOutputStream(newFile);
			int len;
			while ((len = zis.read(buffer)) > 0)
			{
				fos.write(buffer, 0, len);
			}
			fos.close();
			ze = zis.getNextEntry();
		}

		zis.closeEntry();
		zis.close();
		timeout(3);
	}

	/**
	 * Delete specific file.
	 * @author GS-1629
	 * @param fileName the file name
	 */
	public void deleteSpecificFile(String fileName, String filepath)
	{
		try
		{
			File folder = new File(filepath);
			File[] myfile = folder.listFiles();
			for (int i = 0; i < myfile.length; i++)
			{
				if (myfile[i].getName().contains(fileName))
				{
					myfile[i].delete();
				}
			}
		}
		catch (Exception e)
		{
			logger.info(e.getMessage());
		}
	}

	/**
	 * Verify file content.
	 * @author GS-1629
	 * @param docName the doc name
	 * @param fileContent the file content
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean verifyFileContent(String docName, String fileContent) throws IOException
	{
		return verifyFileContent(docName, fileContent, 0);
	}

	/**
	 * Verify file content.
	 * @author GS-1629
	 * @param docName the doc name
	 * @param fileContent the file content
	 * @param sheetNo the sheet no
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean verifyFileContent(String docName, String fileContent, int sheetNo) throws IOException
	{
		logger.info("============ In Verify File Content Method =============");
		String fileExt = FilenameUtils.getExtension(docName);
		if ("xlsx".equalsIgnoreCase(fileExt) || "xls".equalsIgnoreCase(fileExt))
		{
			File myFile = new File(docName);
			FileInputStream fis = new FileInputStream(myFile);

			// Finds the workbook instance for XLSX file
			XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);

			// Return first sheet from the XLSX workbook
			XSSFSheet mySheet = myWorkBook.getSheetAt(sheetNo);

			// Get iterator to all the rows in current sheet
			Iterator<Row> rowIterator = mySheet.iterator();

			// Traversing over each row of XLSX file

			while (rowIterator.hasNext())
			{
				Row row = rowIterator.next();
				// For each row, iterate through each columns
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext())
				{
					Cell cell = cellIterator.next();
					switch (cell.getCellType())
					{
					case Cell.CELL_TYPE_STRING:
						String cellValue = cell.getStringCellValue().trim();
						logger.info("String : " + cellValue);
						if (cellValue.equalsIgnoreCase(fileContent))
						{
							return true;
						}
					}
				}
			}
			return false;
		}
		return false;
	}

	/**
	 * Verify downloaded docx file content.
	 *
	 * @author GS-1629
	 * @param docName the doc name
	 * @param content the content
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public boolean verifyDownloadedDocxFileContent(String docName, String content) throws IOException, InterruptedException
	{
		logger.info("============ In Verify Docx File Content Method =============");

		List<String> l = new ArrayList<>();
		String downloadedFilePath = new File(SeleniumConst.FILE_PATH_DOWNLOADED + docName).getCanonicalPath();
		timeout(2);
		FileInputStream fis = new FileInputStream(new File(downloadedFilePath));
		XWPFDocument document = new XWPFDocument(fis);
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		for (XWPFParagraph para : paragraphs)
		{
			l.add(para.getText());
		}
		if (l.get(0).equals(content))
		{
			return true;
		}
		fis.close();
		return false;
	}

	/**
	 * Update excel file content.
	 * @author GS-1629
	 * @param docName the doc name
	 * @param fileContent the file content
	 * @param rowNumber the row number
	 * @param colNumber the col number
	 * @param addEditrecored the add editrecored
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void updateExcelFileContent(String docName, List<?> fileContent, int rowNumber, int colNumber, boolean addEditrecored) throws IOException
	{
		logger.info("======== In Update Excel File Data ========");

		String downloadedFilePath = new File(SeleniumConst.FILE_PATH_DOWNLOADED + docName).getCanonicalPath();
		logger.info("downloadedFile_path====" + downloadedFilePath);
		FileInputStream fis = new FileInputStream(new File(downloadedFilePath));
		XSSFWorkbook myWorkBook = new XSSFWorkbook(fis);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);

		Row dataRow;
		if (addEditrecored)
		{
			dataRow = mySheet.getRow(rowNumber);
		}
		else
		{
			dataRow = mySheet.createRow(rowNumber);
		}
		int listSize = fileContent.size();
		for (int j = 0; j < listSize; j++)
		{

			if (fileContent.get(j).getClass().getName().equals("java.lang.Double"))
			{
				dataRow.createCell(colNumber).setCellValue((Double) fileContent.get(j));
			}
			else if (fileContent.get(j).getClass().getName().equals("java.lang.Integer"))
			{
				dataRow.createCell(colNumber).setCellValue((Integer) fileContent.get(j));
			}
			else
			{
				dataRow.createCell(colNumber).setCellValue(String.valueOf(fileContent.get(j)));
			}

			colNumber++;
		}
		fis.close();
		FileOutputStream out = new FileOutputStream(downloadedFilePath);
		myWorkBook.write(out);
	}

	/**
	 * Creates the file.
	 * @author GS-1629
	 * @param fileName the file name
	 * @param fileExtension the file extension
	 * @param contentText the content text
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void createFile(String fileName, String fileExtension, String contentText) throws IOException
	{

		FileWriter fw = null;
		BufferedWriter bw = null;

		try
		{
			File file = new File(SeleniumConst.FILE_PATH_DOWNLOADED + fileName + "." + fileExtension);

			if (!file.exists())
			{
				file.createNewFile();
			}
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);

			bw.write(contentText);

		}
		catch (IOException e)
		{
			logger.info("Exception:-", e);
		}
		finally
		{
			if (bw != null)
			{
				bw.close();
			}
			if (fw != null)
			{
				fw.close();
			}
		}
	}

	/**
	 * Read excel file content.
	 * @author GS-1629
	 * @param docName the doc name
	 * @return the list
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<String> readExcelFileContent(String docName) throws IOException
	{
		logger.info("======== In Read Excel File Data ========");

		String filePath = new File("../src/resource/testDocuments/" + docName + "").getCanonicalPath().trim();

		Cell cell;
		Row row;
		ArrayList<String> list = new ArrayList<>();
		list.clear();
		FileInputStream inputStream = new FileInputStream(new File(filePath));
		XSSFWorkbook myWorkBook = new XSSFWorkbook(inputStream);
		XSSFSheet mySheet = myWorkBook.getSheetAt(0);

		Iterator<Row> rowIterator = mySheet.iterator();

		while (rowIterator.hasNext())
		{
			row = rowIterator.next();
			if (row.getRowNum() == 0)
			{
				continue;
			}
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext())
			{
				cell = cellIterator.next();

				if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
				{
					list.add(cell.getNumericCellValue() + "");
				}
				else if (cell.getCellType() == Cell.CELL_TYPE_STRING)
				{
					list.add(cell.getRichStringCellValue().toString());
				}
				else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN)
				{
					list.add(cell.getStringCellValue().trim());
				}
			}
		}
		logger.info("LIST " + list);
		return list;
	}

	/**
	 * Gets the csv file row.
	 * @author GS-1629
	 * @param fileName the file name
	 * @return the csv file row
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public int getCsvFileRow(String fileName) throws IOException
	{
		String fname = SeleniumConst.FILE_PATH_DOWNLOADED + fileName;
		BufferedReader bufferedReader = new BufferedReader(new FileReader(fname));
		int count = 0;
		while (bufferedReader.readLine() != null)
		{
			count++;
		}
		bufferedReader.close();
		logger.info("Count : " + count);
		return count;
	}

	/**
	 * Rename file.
	 * @author GS-1629
	 * @param oldName the old name
	 * @param newName the new name
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean RenameFile(String oldName, String newName) throws IOException
	{
		logger.info("======== In RenameFile ========");
		logger.info("oldFile_path " + oldName);
		logger.info("NewFile_path: " + newName);
		File file = new File(oldName);
		File file2 = new File(newName);
		boolean success = file.renameTo(file2);
		if (success)
		{
			return true;
		}
		return false;
	}

	/**
	 * Read text file.
	 * @author GS-1629
	 * @param path the path
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("resource")
	public String ReadTextFile(String path) throws IOException
	{
		logger.info("======== In Read File Data ========");
		logger.info("File Path ::=> " + path);
		Scanner scanner=null;
		String contents;
		try {
			scanner = new Scanner(new File(path)).useDelimiter("\\Z");
			contents = scanner.next();
			logger.info(contents);
		} finally {
			scanner.close();
		}
		return contents;
	}

	/**
	 * Gets the xlsx file row data.
	 * @author GS-1629
	 * @param fileName the file name
	 * @param recordNo the record no
	 * @param sheetNo
	 * @return the xlsx file row data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<String> getXlsxFileRowData(String fileName, int recordNo, int sheetNo) throws IOException
	{
		String fname = SeleniumConst.FILE_PATH_DOWNLOADED + fileName;
		Reporter.log(fname);

		// ----------------------------- START EXCEL FILE READING ---------------------------
		FileInputStream fis = new FileInputStream(fname);
		ArrayList<String> excelData = new ArrayList<>();

		// Create Workbook instance for xlsx/xls file input stream
		Workbook workbook = null;
		if (fileName.toLowerCase().endsWith("xlsx"))
		{
			workbook = new XSSFWorkbook(fis);
		}
		else if (fileName.toLowerCase().endsWith("xls"))
		{
			workbook = new HSSFWorkbook(fis);
		}

		// Get the nth sheet from the workbook
		Sheet sheet = workbook.getSheetAt(sheetNo);

		// sheet has rows, iterate over them
		Iterator<Row> rowIterator = sheet.iterator();
		int record = 1;
		while (rowIterator.hasNext())
		{
			excelData.clear();
			// Get the row object
			Row row = rowIterator.next();
			// Every row has columns, get the column iterator and iterate over them
			Iterator<Cell> cellIterator = row.cellIterator();
			while (cellIterator.hasNext())
			{
				// Get the Cell object
				Cell cell = cellIterator.next();
				// check the cell type and process accordingly
				switch (cell.getCellType())
				{
				case Cell.CELL_TYPE_STRING:
					excelData.add(cell.getStringCellValue().trim());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					if (DateUtil.isCellDateFormatted(cell))
					{
						DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
						excelData.add(dateFormat.format(cell.getDateCellValue()) + "");
					}
					else
					{
						String temp = cell.getNumericCellValue() + "";
						excelData.add(temp);
					}
					break;
				case Cell.CELL_TYPE_BLANK:
					excelData.add(cell.getStringCellValue().trim());
					break;
				}
			} // end of cell iterator

			if (record == recordNo)
			{

				logger.info(" Row number [ " + record + " ] Proceed For Execution ");
				return excelData;
			}
			record++;
		}

		fis.close();
		// ------------------------------ END OF EXCEL FILE READING -------------------------
		return null;
	}

	/**
	 * Verify xls file cell data.
	 * @author GS-1629
	 * @param fileName the file name
	 * @param map the map
	 * @param headerRowNum the header row num
	 * @param rowNum the row num
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean verifyXlsFileCellData(String fileName, Map<String, String> map, int headerRowNum, int rowNum) throws IOException
	{
		String fname = new File(SeleniumConst.FILE_PATH_DOWNLOADED + fileName).getCanonicalPath();

		// ----------------------------- START EXCEL FILE READING ---------------------------
		FileInputStream fis = new FileInputStream(fname);
		Map<String, Integer> get = new HashMap<>();

		// Create Workbook instance for xlsx/xls file input stream
		Workbook workbook = null;

		if (fileName.toLowerCase().endsWith("xlsx"))
		{
			workbook = new XSSFWorkbook(fis);
		}
		else if (fileName.toLowerCase().endsWith("xls"))
		{
			workbook = new HSSFWorkbook(fis);
		}
		// Map Sheet Headers to get column Index
		Sheet sheet = workbook.getSheetAt(0);
		int rowcount = sheet.getLastRowNum() - sheet.getFirstRowNum();

		Row row = sheet.getRow(headerRowNum);

		int colNum = row.getLastCellNum();

		for (int i = 0; i <= rowcount; i++)
		{
			for (int j = 0; j < colNum; j++)
			{
				Cell cell = row.getCell(j);

				if ((i == 0))
				{
					cell = row.getCell(j);

					get.put(String.valueOf(cell), j);
				}
			}
		}
		// Verify Cell Value According to Index

		for (int j = rowNum; j <= rowcount; j++)
		{
			row = sheet.getRow(j);
			logger.info("row " + j + row);

			for (Map.Entry<String, Integer> entry : get.entrySet())
			{

				for (Map.Entry<String, String> entry1 : map.entrySet())
				{
					String actualHeader = entry.getKey();
					logger.info("actual header" + actualHeader);

					String expectHeader = entry1.getKey();
					logger.info("expect header" + expectHeader);

					String actualValue = "";
					if (actualHeader.equalsIgnoreCase(expectHeader))
					{
						logger.info(entry.getKey() + "===" + entry1.getKey());
						Cell cell = row.getCell(entry.getValue());
						switch (cell.getCellType())
						{
						case Cell.CELL_TYPE_STRING:
							actualValue = cell.getStringCellValue().trim();
							logger.info("actual value switch" + actualValue);
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (DateUtil.isCellDateFormatted(cell))
							{
								DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
								actualValue = dateFormat.format(cell.getDateCellValue());
							}
							else
							{
								actualValue = cell.getNumericCellValue() + "";
							}
							break;
						case Cell.CELL_TYPE_BLANK:
							actualValue = cell.getStringCellValue().trim();

							break;
						}
						if (actualValue.equalsIgnoreCase(entry1.getValue()))
						{
							logger.info("Cell Value Match");
							logger.info(cell.getStringCellValue() + "===" + entry1.getValue());
						}
						else
						{
							logger.info("In else");
							return false;
						}
					}
				}

			}
		}
		fis.close();
		// ------------------------------ END OF EXCEL FILE READING -------------------------
		return true;
	}

	/**
	 * Gets the xlsx file row count from dynamic file.
	 * @author GS-1629
	 * @param filename the filename
	 * @param rowNum the row num
	 * @return the xlsx file row count from dynamic file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public int getXlsxFileRowCountFromDynamicFile(String filename, int rowNum) throws IOException
	{
		File directory = new File(SeleniumConst.FILE_PATH_DOWNLOADED);
		File[] files = directory.listFiles();
		for (File f : files)
		{
			if (f.getName().startsWith(filename))
			{
				return getXlsxFileRowCount(f.getName(), rowNum);
			}
		}
		return 0;
	}

	/**
	 * Gets the xlsx file row count.
	 * @author GS-1629
	 * @param fileName the file name
	 * @param rowNum the row num
	 * @return the xlsx file row count
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public int getXlsxFileRowCount(String fileName, int rowNum) throws IOException
	{
		String fname = new File(SeleniumConst.FILE_PATH_DOWNLOADED + fileName).getCanonicalPath();
		// ----------------------------- START EXCEL FILE READING ---------------------------
		FileInputStream fis = new FileInputStream(fname);
		// Create Workbook instance for xlsx/xls file input stream
		Workbook workbook = null;
		if (fileName.toLowerCase().endsWith("xlsx"))
		{
			workbook = new XSSFWorkbook(fis);
		}
		else if (fileName.toLowerCase().endsWith("xls"))
		{
			workbook = new HSSFWorkbook(fis);
		}
		// Get the nth sheet from the workbook
		Sheet sheet = workbook.getSheetAt(0);
		int noOfRows = 0;
		// Traversing over each row of XLSX file
		int rowcount = sheet.getPhysicalNumberOfRows();
		for (int i = 0; i < rowcount; i++)
		{
			Row row = sheet.getRow(i);
			int colNum = row.getLastCellNum();
			if (colNum > 1)
			{
				noOfRows++;
			}
		}
		fis.close();
		logger.info("TOTAL ROWS====" + (noOfRows - rowNum));
		// ------------------------------ END OF EXCEL FILE READING -------------------------
		return (noOfRows - rowNum);
	}

	/**
	 * Gets the csv file row data.
	 * @author GS-1629
	 * @param fileName the file name
	 * @param recordNo the record no
	 * @return the csv file row data
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<String> getCsvFileRowData(String fileName, int recordNo) throws IOException
	{

		String fname = SeleniumConst.FILE_PATH_DOWNLOADED + fileName;

		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = "\",\"";
		ArrayList<String> csvData = new ArrayList<>();
		int row = 1;
		try
		{
			br = new BufferedReader(new FileReader(fname));
			while ((line = br.readLine()) != null)
			{
				if (row == recordNo)
				{
					// use comma as separator
					String[] rowData = line.split(cvsSplitBy);
					for (String column : rowData)
					{
						if (column.startsWith("\""))
						{
							column = column.replace("\"", "").trim();
						}
						else if (column.endsWith("\","))
						{
							column = column.replace("\",", "").trim();
						}
						if (column.endsWith(","))
						{
							column = column.replace(",", "").trim();
						}
						csvData.add(column);
					}
					br.close();
					return csvData;
				}
				row++;
			}
			br.close();
		}
		catch (FileNotFoundException e)
		{
			logger.error(null, e);
		}
		catch (IOException e)
		{
			logger.error(null, e);
		}
		finally
		{
			if (br != null)
			{
				br.close();
			}
		}
		return null;
	}

	/**
	 * Gets the file without extension.
	 * @author GS-1629
	 * @param file the file
	 * @return the file without extension
	 */
	private String getFileWithoutExtension(File file)
	{
		String name = file.getName();
		int lastIndexOf = name.lastIndexOf('.');
		if (lastIndexOf == -1)
		{
			return name;
		}
		return name.substring(0, lastIndexOf);
	}

	/**
	 * Verify file extension xls.
	 * @author GS-1629
	 * @param file the file
	 * @return true, if successful
	 */
	public boolean verifyFileExtensionXls(String file)
	{
		if (FilenameUtils.getExtension(file) == "xls")
		{
			return true;
		}

		return false;
	}

	/**
	 * Gets the p df text from dynamic file.
	 * @author GS-1629
	 * @param filename the filename
	 * @return the p df text from dynamic file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String getPDfTextFromDynamicFile(String filename) throws IOException
	{
		File directory = new File(SeleniumConst.FILE_PATH_DOWNLOADED); // fl is the directory in which look for files
		File[] files = directory.listFiles();
		for (File f : files)
		{
			if (f.getName().startsWith(filename))
			{
				return getPdfText(f.getName());
			}
		}
		return null;
	}

	/**
	 * Gets the pdf text.
	 * @author GS-1629
	 * @param fileName the file name
	 * @return the pdf text
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String getPdfText(String fileName) throws IOException
	{
		String Text;
		String fname = SeleniumConst.FILE_PATH_DOWNLOADED + fileName;
		File file = new File(fname);
		RandomAccessFile accessFile = new RandomAccessFile(file, "r");
		PDFParser parser = new PDFParser((RandomAccessRead) accessFile);// update for PDFBox V 2.0
		parser.parse();
		COSDocument cosDoc = parser.getDocument();
		PDFTextStripper pdfStripper = new PDFTextStripper();
		PDDocument pdDoc = new PDDocument(cosDoc);
		pdDoc.getNumberOfPages();

		pdfStripper.setEndPage(pdDoc.getNumberOfPages());

		Text = pdfStripper.getText(pdDoc);
		logger.info(Text);

		try
		{
			if (cosDoc != null)
			{
				cosDoc.close();
			}

			if (pdDoc != null)
			{
				pdDoc.close();
			}
			if (!((RandomAccessRead) accessFile).isClosed())
			{
				accessFile.close();
			}
		}
		catch (Exception e1)
		{
			logger.error(null, e1);
		}
		finally
		{
			pdDoc.close();
			if (cosDoc != null)
			{
				cosDoc.close();
			}
		}

		return Text;
	}

	/**
	 * Verify xls column numner.
	 * @author GS-1629
	 * @param fileName the file name
	 * @param headerName the header name
	 * @param headerRownumer the header rownumer
	 * @param columnnumber the columnnumber
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean verifyXlsColumnNumner(String fileName, String headerName, int headerRownumer, int columnnumber) throws IOException
	{

		String fname = SeleniumConst.FILE_PATH_DOWNLOADED + fileName;
		FileInputStream fis = new FileInputStream(fname);
		Workbook workbook = null;
		if (fileName.toLowerCase().endsWith("xlsx"))
		{
			workbook = new XSSFWorkbook(fis);
		}
		else if (fileName.toLowerCase().endsWith("xls"))
		{
			workbook = new HSSFWorkbook(fis);
		}
		Sheet sheet = workbook.getSheetAt(0);
		Row row = sheet.getRow(headerRownumer);
		Cell cell = row.getCell(columnnumber);
		int colNum = row.getLastCellNum();
		int tempCol = colNum;
		if (columnnumber == 1)
		{
			if (columnnumber <= tempCol)
			{
				for (int i = columnnumber; i < tempCol; i++)
				{
					cell = row.getCell(columnnumber);
					String actualHeader = String.valueOf(cell);
					logger.info("Actual header" + actualHeader);
					if (actualHeader.equalsIgnoreCase(headerName))
					{

						logger.info("Header match");
						return true;
					}
					columnnumber++;
				}
				return false;
			}
		}

		String actualHeader = String.valueOf(cell);
		logger.info("Actual header" + actualHeader);
		if (actualHeader.equalsIgnoreCase(headerName))
		{
			logger.info("Header match");
			return true;
		}
		else
		{
			logger.info("Header is not define possition");

		}
		return false;
	}

	/**
	 * Verify xls file perticular cell data from dynamic file name.
	 * @author GS-1629
	 * @param fileName the file name
	 * @param headerName the header name
	 * @param value the value
	 * @param headerRowNum the header row num
	 * @param startrowNum the startrow num
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean verifyXlsFilePerticularCellDataFromDynamicFileName(String fileName, String headerName, String value, int headerRowNum, int startrowNum) throws Exception
	{
		File directory = new File(SeleniumConst.FILE_PATH_DOWNLOADED);
		File[] files = directory.listFiles();
		for (File f : files)
		{
			if (f.getName().startsWith(fileName))
			{
				return verifyXlsFileCellPerticularData(f.getName(), headerName, value, headerRowNum, startrowNum);
			}
		}
		return false;
	}

	/**
	 * Verify xls file cell perticular data.
	 * @author GS-1629
	 * @param fileName the file name
	 * @param headerName the header name
	 * @param value the value
	 * @param headerRowNum the header row num
	 * @param startrowNum the startrow num
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean verifyXlsFileCellPerticularData(String fileName, String headerName, String value, int headerRowNum, int startrowNum) throws Exception
	{
		timeout(3);
		String fname = new File(SeleniumConst.FILE_PATH_DOWNLOADED + fileName).getCanonicalPath();
		int actualColnum = -1;
		FileInputStream fis = new FileInputStream(new File(fname));
		Workbook workbook = null;
		if (fileName.toLowerCase().endsWith("xlsx"))
		{
			workbook = new XSSFWorkbook(fis);
		}
		else if (fileName.toLowerCase().endsWith("xls"))
		{
			workbook = new HSSFWorkbook(fis);
		}

		Sheet sheet = workbook.getSheetAt(0);
		int rowcount = sheet.getLastRowNum();
		Row row = sheet.getRow(headerRowNum);
		int colNum = row.getLastCellNum();
		for (int i = 0; i < colNum; i++)
		{
			Cell cell = row.getCell(i);
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
			{
				String actualHeader = cell.getStringCellValue();
				logger.info("ACTUAL HEADER " + actualHeader);
				logger.info("EXPEC. HEADER " + headerName);
				if (headerName.equalsIgnoreCase(actualHeader))
				{
					actualColnum = i;
					logger.info("Heder Match");
					logger.info("actual header no " + actualColnum);
					break;
				}
			}
		}
		if (actualColnum == -1)
		{
			throw new Exception("None of the cells in the header Match expected value");
		}

		for (int r = headerRowNum + 1; r <= rowcount; r++)
		{

			logger.info("Value " + value);
			row = sheet.getRow(r);

			Cell cell = row.getCell(actualColnum);
			switch (cell.getCellType())
			{
			case Cell.CELL_TYPE_STRING:
				logger.info("ROW DATA " + row.getCell(actualColnum).getStringCellValue());
				if (value.equalsIgnoreCase(row.getCell(actualColnum).getStringCellValue()))
				{
					return true;
				}
				break;
			case Cell.CELL_TYPE_NUMERIC:
				logger.info("EACH ROW " + row.getCell(actualColnum).getNumericCellValue());
				double d = row.getCell(actualColnum).getNumericCellValue();
				String s = String.valueOf(d);
				if (value.equalsIgnoreCase(s))
				{
					return true;
				}
			}

		}
		return false;
	}

	/**
	 * Verify xls file cell perticular dynamic file name.
	 * @author GS-1629
	 * @param filename the filename
	 * @param headerName the header name
	 * @param headerRowNum the header row num
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<String> verifyXlsFileCellPerticularDynamicFileName(String filename, String headerName, int headerRowNum) throws Exception
	{
		File directory = new File(SeleniumConst.FILE_PATH_DOWNLOADED);
		File[] files = directory.listFiles();
		for (File f : files)
		{
			if (f.getName().startsWith(filename))
			{
				return verifyXlsFileCellPerticularData(f.getName(), headerName, headerRowNum);
			}
		}
		return null;
	}

	/**
	 * Verify xls file header exist.
	 * @author GS-1629
	 * @param fileName the file name
	 * @param headerName the header name
	 * @param headerRowNum the header row num
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean verifyXlsFileHeaderExist(String fileName, String headerName, int headerRowNum) throws IOException
	{
		File directory = new File(SeleniumConst.FILE_PATH_DOWNLOADED);
		File[] files = directory.listFiles();
		for (File f : files)
		{
			if (f.getName().startsWith(fileName))
			{
				String fi = SeleniumConst.FILE_PATH_DOWNLOADED + f.getName();
				String fname = new File(fi).getCanonicalPath();
				int actualColnum = -1;
				FileInputStream fis = new FileInputStream(new File(fname));
				Workbook workbook = null;
				if (f.getName().toLowerCase().endsWith("xlsx"))
				{
					workbook = new XSSFWorkbook(fis);
				}
				else if (f.getName().toLowerCase().endsWith("xls"))
				{
					workbook = new HSSFWorkbook(fis);
				}
				Sheet sheet = workbook.getSheetAt(0);
				Row row = sheet.getRow(headerRowNum);
				int colNum = row.getLastCellNum();
				for (int i = 0; i < colNum; i++)
				{
					Cell cell = row.getCell(i);
					if (cell.getCellType() == Cell.CELL_TYPE_STRING)
					{
						String actualHeader = cell.getStringCellValue();
						if (headerName.trim().equalsIgnoreCase(actualHeader))
						{
							actualColnum = i;
							logger.info("actual header no " + actualColnum);
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Verify xls file cell perticular data.
	 * @author GS-1629
	 * @param fileName the file name
	 * @param headerName the header name
	 * @param headerRowNum the header row num
	 * @return the list
	 * @throws Exception the exception
	 */
	public List<String> verifyXlsFileCellPerticularData(String fileName, String headerName, int headerRowNum) throws Exception
	{
		String fi = SeleniumConst.FILE_PATH_DOWNLOADED + fileName;
		List<String> list = new ArrayList<>();
		String fname = new File(fi).getCanonicalPath();
		int actualColnum = -1;
		FileInputStream fis = new FileInputStream(new File(fname));
		Workbook workbook = null;
		if (fileName.toLowerCase().endsWith("xlsx"))
		{
			workbook = new XSSFWorkbook(fis);
		}
		else if (fileName.toLowerCase().endsWith("xls"))
		{
			workbook = new HSSFWorkbook(fis);
		}

		Sheet sheet = workbook.getSheetAt(0);
		int rowcount = sheet.getLastRowNum();
		Row row = sheet.getRow(headerRowNum);
		int colNum = row.getLastCellNum();
		for (int i = 0; i < colNum; i++)
		{
			Cell cell = row.getCell(i);
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
			{
				String actualHeader = cell.getStringCellValue();
				if (headerName.trim().equalsIgnoreCase(actualHeader))
				{
					actualColnum = i;
					logger.info("Heder Match");
					logger.info("actual header no " + actualColnum);
					break;
				}
			}
		}
		if (actualColnum == -1)
		{
			throw new Exception("None of the cells in the header Match expected value");
		}

		for (int r = headerRowNum + 1; r <= rowcount; r++)
		{
			row = sheet.getRow(r);
			Cell cell = row.getCell(actualColnum);
			switch (cell.getCellType())
			{
			case Cell.CELL_TYPE_STRING:
				list.add(row.getCell(actualColnum).getStringCellValue());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				double d = row.getCell(actualColnum).getNumericCellValue();
				String s = String.valueOf(d);
				list.add(s);
				break;
			}
		}
		logger.info("List is ==> " + list);
		return list;
	}

	/**
	 * Verify xls one column all value.
	 * @author GS-1629
	 * @param fileName the file name
	 * @param headerName the header name
	 * @param value the value
	 * @param headerRowNum the header row num
	 * @param rowNum the row num
	 * @return true, if successful
	 * @throws Exception the exception
	 */
	public boolean verifyXlsOneColumnAllValue(String fileName, String headerName, String value, int headerRowNum, int rowNum) throws Exception
	{
		String fname = new File(SeleniumConst.FILE_PATH_DOWNLOADED + fileName).getCanonicalPath();
		int actualColnum = -1;
		FileInputStream fis = new FileInputStream(fname);
		Workbook workbook = null;
		if (fileName.toLowerCase().endsWith("xlsx"))
		{
			workbook = new XSSFWorkbook(fis);
		}
		else if (fileName.toLowerCase().endsWith("xls"))
		{
			workbook = new HSSFWorkbook(fis);
		}

		Sheet sheet = workbook.getSheetAt(0);
		int rowcount = sheet.getLastRowNum();
		Row row = sheet.getRow(headerRowNum);
		int colNum = row.getLastCellNum();
		for (int i = 0; i < colNum; i++)
		{
			Cell cell = row.getCell(i);
			if (cell.getCellType() == Cell.CELL_TYPE_STRING)
			{
				String actualHeader = cell.getStringCellValue();
				if (headerName.equalsIgnoreCase(actualHeader))
				{
					actualColnum = i;
					logger.info("actual header no " + actualColnum);
					break;
				}
			}
		}
		if (actualColnum == -1)
		{
			throw new Exception("None of the cells in the header Match expected value");
		}

		for (int r = headerRowNum + 1; r <= rowcount; r++)
		{
			logger.info("ROW DATA " + row.getCell(actualColnum).getStringCellValue());
			logger.info("Value " + value);
			row = sheet.getRow(r);
			logger.info("EACH ROW " + row.getCell(actualColnum).getStringCellValue());
			if (!value.equalsIgnoreCase(row.getCell(actualColnum).getStringCellValue()))
			{
				return false;
			}
		}
		return true;

	}

	/**
	 * Waitforfiledownload.
	 * @author GS-1629
	 * @param filename the filename
	 * @return It will wait for file download.
	 */
	public boolean waitforfiledownload(String filename)
	{
		String fname = SeleniumConst.FILE_PATH_DOWNLOADED + filename;
		File file = new File(fname);
		int second = 0;
		do
		{
			if (file.exists())
			{
				logger.info("In Verify Documnent Download :-" + fname);
				return true;
			}
			second++;
		}
		while (second <= 25);

		return false;
	}

	/**
	 * Used to add new Row in excel sheet.
	 * @author GS-1629
	 * @param fileName the file name
	 * @param dataToWrite : data pass in form of array
	 * @param rowNumber : pass row number at which you want to add row
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void addRecordInExcelFile(String fileName, List<String> dataToWrite, int rowNumber) throws IOException
	{
		String downloadedFilePath = new File(SeleniumConst.FILE_PATH_DOWNLOADED + fileName).getCanonicalPath();
		FileInputStream fis = new FileInputStream(new File(downloadedFilePath));
		Workbook myWorkBook = null;
		if (fileName.toLowerCase().endsWith("xlsx"))
		{
			myWorkBook = new XSSFWorkbook(fis);
		}
		else if (fileName.toLowerCase().endsWith("xls"))
		{
			myWorkBook = new HSSFWorkbook(fis);
		}
		Sheet sheet = myWorkBook.getSheetAt(0);
		Row newRow = sheet.createRow(rowNumber);
		logger.info(dataToWrite.size());
		for (int j = 0; j < dataToWrite.size(); j++)
		{

			Cell cell = newRow.createCell(j);
			cell.setCellValue(dataToWrite.get(j));
		}
		fis.close();
		FileOutputStream out = new FileOutputStream(downloadedFilePath);
		myWorkBook.write(out);
	}

	/**
	 * Verify file start name.
	 * @author GS-1629
	 * @param filename the filename
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean verifyFileStartName(String filename) throws IOException
	{
		String downloadPath = SeleniumConst.FILE_PATH_DOWNLOADED;
		File file = new File(downloadPath);
		File[] files = file.listFiles();

		for (File f : files)
		{
			if (f.getName().startsWith(filename))
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Verify read downloaded docx.
	 * @author GS-1629
	 * @param docName the doc name
	 * @param content the content
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public boolean verifyReadDownloadedDocx(String docName, List<String> content) throws IOException
	{
		logger.info("============ In Verify Docx File Content Method =============");

		boolean flag = false;

		List<String> l = new ArrayList<>();
		String downloadedFilePath = new File(SeleniumConst.FILE_PATH_DOWNLOADED + docName.split("\\s+at")[0]).getCanonicalPath();
		FileInputStream fis = new FileInputStream(new File(downloadedFilePath));
		XWPFDocument document = new XWPFDocument(fis);
		List<XWPFParagraph> paragraphs = document.getParagraphs();
		for (XWPFParagraph para : paragraphs)
		{
			l.add(para.getText());
		}
		for (String contentValue : content)
		{
			for (String lValue : l)
			{
				if (lValue.trim().equals(contentValue))
				{
					flag = true;
					break;
				}
			}
		}
		fis.close();
		return flag;
	}
}
