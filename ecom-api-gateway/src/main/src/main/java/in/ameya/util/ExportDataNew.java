package in.ameya.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import in.ameya.model.master.GenericReportVO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

@Component
@PropertySource(value = "classpath:filePath.properties")
public class ExportDataNew {

	@Autowired
	private Environment env;
	
	@Autowired
	private LoggerUtil loggerUtil;
	
	private static final Logger logger = LogManager.getLogger(ExportDataNew.class);

	/**
	 * @param response
	 * @param downloadString
	 * @param userKey
	 * @return
	 * @throws Exception
	 */
	public String downloadFileDetailsDeleteCSV(HttpServletResponse response, String downloadString,
			Integer loggedInKey) {
		File fileToUpload = null;
		fileToUpload = new File(downloadString);
		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileToUpload.getName());
		FileInputStream fis = null;
		ServletOutputStream servletOutputStream = null;
		try {
			fis = new FileInputStream(fileToUpload);
			servletOutputStream = response.getOutputStream();
			byte[] buffer = new byte[1024 * 4];
			while (true) {
				int bytes = fis.read(buffer);
				if (bytes < 0)
					break;
				servletOutputStream.write(buffer, 0, bytes);
			}
		} catch (Exception e) {
			downloadString = null;
			logger.error("downloadFileDetailsDeleteCSV: " + loggedInKey, e);
		} finally {
			try {
				if (servletOutputStream != null) {
					servletOutputStream.flush();
					servletOutputStream.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (fileToUpload != null) {
					fileToUpload.delete();
				}
			} catch (Exception e) {
				downloadString = null;
				logger.error("downloadFileDetailsDeleteCSV: " + loggedInKey, e);
			}
		}
		return downloadString;
	}

	
	
	/**
	 * @param response
	 * @param downloadString
	 * @param userKey
	 * @return
	 * @throws Exception
	 */
	/*
	 * public void generateCSVFromData(HttpServletResponse response, String
	 * filePath, Integer loggedInKey) { File file = new File(filePath);
	 * 
	 * // Ensure the file exists before proceeding if (!file.exists()) { throw new
	 * RuntimeException("File not found at the specified path: " + filePath); }
	 * 
	 * try (BufferedReader reader = new BufferedReader(new FileReader(file));
	 * OutputStream outputStream = response.getOutputStream()) {
	 * 
	 * // Set response headers for inline content (not downloading as a file)
	 * response.setContentType("text/csv");
	 * response.setHeader("Content-Disposition", "inline; filename=UserMaster.csv");
	 * 
	 * // Read and process the file content String header = reader.readLine();
	 * 
	 * // Check if the header is valid if (header == null || header.isEmpty()) {
	 * throw new RuntimeException("File does not contain a valid header."); }
	 * 
	 * // Modify header: remove single quotes and make it lowercase String[] columns
	 * = header.split("\\|"); StringBuilder modifiedHeader = new StringBuilder();
	 * 
	 * for (String column : columns) { String formattedHeader = column.replace("'",
	 * "").toLowerCase(); modifiedHeader.append(formattedHeader).append("|"); }
	 * 
	 * // Remove the last trailing pipe character
	 * modifiedHeader.deleteCharAt(modifiedHeader.length() - 1);
	 * 
	 * // Write the modified header to the response
	 * outputStream.write(modifiedHeader.toString().getBytes());
	 * 
	 * // Write the remaining file content to the response String line; while ((line
	 * = reader.readLine()) != null) { outputStream.write(line.getBytes());
	 * outputStream.write("\n".getBytes()); // Ensuring line breaks between rows }
	 * 
	 * outputStream.flush();
	 * 
	 * // Log success logger.info("CSV content successfully sent for LoggedInKey: "
	 * + loggedInKey);
	 * 
	 * } catch (IOException e) {
	 * logger.error("Error occurred while sending CSV content for file: " +
	 * filePath, e); throw new
	 * RuntimeException("Error occurred while sending CSV content.", e); } finally {
	 * // Optionally delete the temporary file if (file.exists()) { boolean
	 * isDeleted = file.delete(); if (isDeleted) {
	 * logger.info("Temporary file deleted successfully: " + filePath); } else {
	 * logger.warn("Failed to delete temporary file: " + filePath); } } } }
	 */




	/**
	 * @param response
	 * @param downloadString
	 * @param userKey
	 * @return
	 * @throws Exception
	 */
	public String downloadFileDetailsSaveTXT(HttpServletResponse response, String downloadString, Integer loggedInKey)
			throws Exception {
		File fileToUpload = null;
		fileToUpload = new File(downloadString);
		response.setContentType("text/plain");
		response.setHeader("Content-Disposition", "attachment;filename=" + fileToUpload.getName());
		FileInputStream fis = new FileInputStream(fileToUpload);
		ServletOutputStream servletOutputStream = response.getOutputStream();

		try {
			byte[] buffer = new byte[1024 * 4];
			while (true) {
				int bytes = fis.read(buffer);
				if (bytes < 0)
					break;
				servletOutputStream.write(buffer, 0, bytes);
			}
			servletOutputStream.flush();

		} catch (Exception e) {
			downloadString = null;
			logger.error("downloadFileDetailsSaveTXT: " + loggedInKey, e);
		} finally {
			servletOutputStream.close();
			fis.close();
		}
		return downloadString;
	}

	/**
	 * @param response
	 * @param sheetName
	 * @param rs
	 * @param userId
	 * @throws Exception
	 */
	public void downloadFileDetailsExcel(HttpServletResponse response, String sheetName, ResultSet rs,
			Integer loggedInKey) {

		try {
			HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = null;
			sheet = hwb.createSheet(sheetName);
			HSSFCell cell = null;
			HSSFCellStyle headerStyle = hwb.createCellStyle();

			SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_1);
			Date date = new Date();
			String currentDate = formatter.format(date);

			HSSFFont font = hwb.createFont();
			font.setBold(true);
			headerStyle.setFont(font);
			int index = 0;
			int rowCounter = 0;
			int sheetCounter = 0;
			int pageSize = 30000;
			String header = "";
			boolean isHeaderFound = false;
			if (rs != null) {
				sheetCounter++;
				while (rs.next()) {
					if (rowCounter > pageSize) {
						sheet = null;
						rowCounter = 0;
						index = 0;
						sheet = hwb.createSheet(sheetName + sheetCounter);
						sheetCounter++;
					}
					HSSFRow row = sheet.createRow((short) index);

					// to write header
					if (rowCounter == 0 && !("").equalsIgnoreCase(header)) {
						String headerArr[] = header.split("#");
						int headerSize = headerArr.length;

						for (int h = 0; h < headerSize; h++) {
							cell = row.createCell((int) (h));
							HSSFRichTextString richString = new HSSFRichTextString(headerArr[h]);
							cell.setCellValue(richString);
							cell.setCellStyle(headerStyle);
						}

						index++;
						rowCounter++;
						row = sheet.createRow((short) index);
					}

					if (index == 0) {
						cell = row.createCell((int) (0));
						HSSFRichTextString richString1 = new HSSFRichTextString("Report Date");
						cell.setCellValue(richString1);
						cell.setCellStyle(headerStyle);

						cell = row.createCell((int) (1));
						HSSFRichTextString richString2 = new HSSFRichTextString(currentDate);
						cell.setCellValue(richString2);
						cell.setCellStyle(headerStyle);

						index++;
						row = sheet.createRow((short) index);

						index++;
						row = sheet.createRow((short) index);
					}
					ResultSetMetaData rsmd = rs.getMetaData();
					int numCols = rsmd.getColumnCount();
					for (int i = 0; i < numCols; i++) {
						if (!isHeaderFound) {
							header = header + rs.getString(i + 1) + "#";

							cell = row.createCell((int) (i));
							HSSFRichTextString richString = new HSSFRichTextString(rs.getString(i + 1));
							cell.setCellValue(richString);
							cell.setCellStyle(headerStyle);
						} else {
							cell = row.createCell((int) (i));
							HSSFRichTextString richString = new HSSFRichTextString(rs.getString(i + 1));
							cell.setCellValue(richString);
						}
					}
					index++;
					rowCounter++;
					isHeaderFound = true;
				}
			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=" + sheetName + ".xls");
			OutputStream fileOut = response.getOutputStream();
			hwb.write(fileOut);
			fileOut.close();
			hwb.close();

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("downloadFileDetailsExcel: " + loggedInKey, e);
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {;}
				rs = null;
			}
		}
	}

	/**
	 * @param response
	 * @param downloadString
	 * @param userKey
	 * @return
	 * @throws Exception
	 */
	public String downloadFileDetailsDeleteFile(HttpServletResponse response, String downloadString, String contentType,
			Integer loggedInKey) {
		File fileToUpload = null;
		fileToUpload = new File(downloadString);
		response.setContentType(contentType);
		response.setHeader("Content-Disposition", "attachment;filename=" + fileToUpload.getName());
		FileInputStream fis = null;
		ServletOutputStream servletOutputStream = null;
		try {
			fis = new FileInputStream(fileToUpload);
			servletOutputStream = response.getOutputStream();
			byte[] buffer = new byte[1024 * 4];
			while (true) {
				int bytes = fis.read(buffer);
				if (bytes < 0)
					break;
				servletOutputStream.write(buffer, 0, bytes);
			}
		} catch (Exception e) {
			downloadString = null;
			logger.error("downloadFileDetailsDeleteFile: " + loggedInKey, e);
		} finally {
			try {
				if (servletOutputStream != null) {
					servletOutputStream.flush();
					servletOutputStream.close();
				}
				if (fis != null) {
					fis.close();
				}
				if (fileToUpload != null) {
					fileToUpload.delete();
				}
			} catch (Exception e) {
				downloadString = null;
				logger.error("downloadFileDetailsDeleteFile: " + loggedInKey, e);
			}
		}
		return downloadString;
	}

	public String getSpringURLforContentType(String contentType, String imageURL, String key) {
		String springURL = null;
		try {
			if (contentType != null) {
				if (contentType.contains("image")) {
					springURL = imageURL + "?selRec=" + key;
				} else if (contentType.equalsIgnoreCase("application/pdf")) {
					springURL = "/resources/img/pdf-icon.png";
				} else if (contentType.equalsIgnoreCase("application/vnd.ms-excel")
						|| contentType
								.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
						|| contentType.equalsIgnoreCase("text/csv")) {
					springURL = "/resources/img/excel-xls-icon.png";
				} else if (contentType.equalsIgnoreCase("application/msword")
						|| contentType.equalsIgnoreCase("application/vnd.oasis.opendocument.text")) {
					springURL = "/resources/img/doc-icon.png";
				} else if (contentType.equalsIgnoreCase("application/zip")
						|| contentType.equalsIgnoreCase("application/x-rar-compressed")
						|| contentType.equalsIgnoreCase("application/x-7z-compressed")) {
					springURL = "/resources/img/zip-icon.png";
				} else if (contentType.equalsIgnoreCase("application/rtf")
						|| contentType.equalsIgnoreCase("application/vnd.oasis.opendocument.text")
						|| contentType.equalsIgnoreCase("text/plain")) {
					springURL = "/resources/img/txt-icon.png";
				} else {
					springURL = "/resources/img/Document-Blank-icon.png";
				}

			} else {
				springURL = "/resources/img/Document-Blank-icon.png";
			}
		} catch (Exception e) {
			springURL = null;
			logger.error("getSpringURLforContentType: " + key, e);
		}
		return springURL;
	}

	/**
	 * @param response
	 * @param sheetName
	 * @param rs
	 * @param userId
	 * @param searchparamVals
	 * @param userName
	 * @param solId
	 * @throws Exception
	 */
	public void downloadFileDetailsExcelWithParams(HttpServletResponse response, String sheetName, ResultSet rs,
			Integer loggedInKey, String searchParamVals, String userName, String solId) throws Exception {
		try (HSSFWorkbook workbook = new HSSFWorkbook()) {
			HSSFSheet sheet = workbook.createSheet(sheetName);

			HSSFCellStyle headerStyle = workbook.createCellStyle();
			HSSFCellStyle amountStyle = workbook.createCellStyle();
			HSSFCellStyle rightStyle = workbook.createCellStyle();

			// Styles
			DataFormat format = workbook.createDataFormat();
			amountStyle.setDataFormat(format.getFormat("#,##0.00"));
			rightStyle.setAlignment(HorizontalAlignment.RIGHT);

			HSSFFont font = workbook.createFont();
			font.setBold(true);
			headerStyle.setFont(font);

			int rowIndex = 0;

			// Create Report Header
			createHeaderRow(sheet, "Report Name", sheetName, rowIndex++, headerStyle);
			createHeaderRow(sheet, "Report Date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()), rowIndex++,
					headerStyle);
			createHeaderRow(sheet, "User ID", userName, rowIndex++, headerStyle);
			createHeaderRow(sheet, "SOL ID", solId, rowIndex++, headerStyle);

			rowIndex++; // Add blank line

			// Add search parameters if available
			if (searchParamVals != null && !searchParamVals.isEmpty()) {
				for (String param : searchParamVals.split("#")) {
					HSSFRow row = sheet.createRow(rowIndex++);
					String[] values = param.split("\\|");
					for (int colIndex = 0; colIndex < values.length; colIndex++) {
						HSSFCell cell = row.createCell(colIndex);
						cell.setCellValue(values[colIndex].equals("null") ? "" : values[colIndex]);
						cell.setCellStyle(headerStyle);
					}
				}
			}

			// Add data rows from ResultSet
			if (rs != null) {
				HSSFRow headerRow = sheet.createRow(rowIndex++);
				ResultSetMetaData rsMeta = rs.getMetaData();
				int columnCount = rsMeta.getColumnCount();

				// Write column headers
				for (int i = 1; i <= columnCount; i++) {
					HSSFCell cell = headerRow.createCell(i - 1);
					cell.setCellValue(rsMeta.getColumnName(i));
					cell.setCellStyle(headerStyle);
				}

				// Write data rows
				while (rs.next()) {
					HSSFRow row = sheet.createRow(rowIndex++);
					for (int i = 1; i <= columnCount; i++) {
						HSSFCell cell = row.createCell(i - 1);
						String value = rs.getString(i);
						if (value != null && value.matches("-?\\d+(\\.\\d+)?")) {
							cell.setCellValue(Double.parseDouble(value));
							cell.setCellStyle(amountStyle);
						} else {
							cell.setCellValue(value != null ? value : "");
						}
					}
				}
			}

			// Write to response
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename=\"" + sheetName + ".xls\"");

			try (OutputStream out = response.getOutputStream()) {
				workbook.write(out);
				out.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error generating Excel file", e);
		}
	}

	private void createHeaderRow(HSSFSheet sheet, String label, String value, int rowIndex, HSSFCellStyle style) {
		HSSFRow row = sheet.createRow(rowIndex);
		HSSFCell labelCell = row.createCell(0);
		labelCell.setCellValue(label);
		labelCell.setCellStyle(style);

		HSSFCell valueCell = row.createCell(1);
		valueCell.setCellValue(value);
		valueCell.setCellStyle(style);
	}

	/**
	 * @param response
	 * @param sheetName
	 * @param rs
	 * @param userId
	 * @param searchparamVals
	 * @param userName
	 * @param solId
	 * @throws Exception
	 */
	/*
	 * public String createExcelInZipForEmailReports(String fileNameMain,
	 * ArrayList<ExportDataResultSet> resultSetArray) throws Exception{
	 * 
	 * String returnFileName = "";
	 * 
	 * try { String filePath = env.getProperty("download.file.path");
	 * 
	 * String format = "yyyy-MM-dd"; DateFormat dateFormatter = new
	 * SimpleDateFormat(format); Date date = new Date(); String strDate =
	 * dateFormatter.format(date); filePath = filePath + strDate; File theDir = new
	 * File(filePath); if (!theDir.exists()){ theDir.mkdirs(); }
	 * 
	 * filePath = filePath +"\\"; Date dateFileName = new
	 * Date(System.currentTimeMillis()); SimpleDateFormat formatFileName = new
	 * SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
	 * 
	 * String fileName =
	 * filePath+fileNameMain+"-"+formatFileName.format(dateFileName);
	 * 
	 * HSSFWorkbook hwb = new HSSFWorkbook();
	 * 
	 * Iterator<ExportDataResultSet> it = resultSetArray.iterator();
	 * while(it.hasNext()) { ExportDataResultSet exportDataResultSet = it.next();
	 * ResultSet rs = exportDataResultSet.getResultSet();
	 * 
	 * HSSFSheet sheet = null; sheet =
	 * hwb.createSheet(exportDataResultSet.getSheetName()); HSSFCell cell = null;
	 * HSSFCellStyle headerStyle = hwb.createCellStyle();
	 * 
	 * SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_1);
	 * Date date1 = new Date(); String currentDate = formatter.format(date1);
	 * 
	 * HSSFFont font = hwb.createFont();
	 * //font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); font.setBold(true);
	 * headerStyle.setFont(font); int index = 0; HSSFRow row =
	 * sheet.createRow((short)index); cell = row.createCell((int) (0));
	 * HSSFRichTextString richString1 = new HSSFRichTextString("Report Name");
	 * cell.setCellValue(richString1); cell.setCellStyle(headerStyle);
	 * 
	 * cell = row.createCell((int) (1)); HSSFRichTextString richString2 = new
	 * HSSFRichTextString(exportDataResultSet.getSheetName().replaceAll("_", " "));
	 * cell.setCellValue(richString2); cell.setCellStyle(headerStyle);
	 * 
	 * index++;
	 * 
	 * row = sheet.createRow((short)index); cell = row.createCell((int) (0));
	 * richString1 = new HSSFRichTextString("Report Date");
	 * cell.setCellValue(richString1); cell.setCellStyle(headerStyle);
	 * 
	 * cell = row.createCell((int) (1)); richString2 = new
	 * HSSFRichTextString(currentDate); cell.setCellValue(richString2);
	 * cell.setCellStyle(headerStyle);
	 * 
	 * index++;
	 * 
	 * row = sheet.createRow((short)index);
	 * 
	 * index++;
	 * 
	 * row = sheet.createRow((short)index);
	 * 
	 * index++;
	 * 
	 * int rowCounter = 0; int sheetCounter = 0; int pageSize = 30000; String header
	 * = ""; boolean isHeaderFound = false; if (rs != null) { sheetCounter++;
	 * while(rs.next()) {
	 * 
	 * if(rowCounter > pageSize) { sheet = null; rowCounter = 0; index=0; sheet =
	 * hwb.createSheet(exportDataResultSet.getSheetName()+sheetCounter);
	 * sheetCounter++; } row = sheet.createRow((short)index);
	 * 
	 * // to write header if(rowCounter == 0 && !("").equalsIgnoreCase(header)) {
	 * String headerArr[] = header.split("#"); int headerSize = headerArr.length;
	 * 
	 * for(int h = 0; h < headerSize; h++) { cell = row.createCell((int) (h));
	 * HSSFRichTextString richString = new HSSFRichTextString(headerArr[h]);
	 * cell.setCellValue(richString); cell.setCellStyle(headerStyle); }
	 * 
	 * index++; rowCounter++; row = sheet.createRow((short)index); }
	 * 
	 * ResultSetMetaData rsmd = rs.getMetaData(); int numCols =
	 * rsmd.getColumnCount(); for(int i = 0; i < numCols; i++) { if(!isHeaderFound)
	 * { header = header+rs.getString(i+1) +"#";
	 * 
	 * cell = row.createCell((int) (i)); HSSFRichTextString richString = new
	 * HSSFRichTextString(rs.getString(i+1)); cell.setCellValue(richString);
	 * cell.setCellStyle(headerStyle); } else { cell = row.createCell((int) (i));
	 * HSSFRichTextString richString = new HSSFRichTextString(rs.getString(i+1));
	 * cell.setCellValue(richString); } }
	 * 
	 * index++; rowCounter++; isHeaderFound = true; } }
	 * 
	 * if (rs != null) { rs.close(); rs = null; } } //Write file FileOutputStream
	 * fileOut = new FileOutputStream(fileName+".xls"); hwb.write(fileOut);
	 * fileOut.close(); hwb.close();
	 * 
	 * Path source = Paths.get(fileName+".xls"); returnFileName = fileName+".zip";
	 * 
	 * zipSingleFile(source, returnFileName);
	 * 
	 * File f = new File(fileName+".xls"); if(f.exists()) { f.delete(); }
	 * 
	 * }catch(Exception e){ e.printStackTrace();
	 * logger.error("createExcelInZipForEmailReports:", e); } finally{
	 * 
	 * }
	 * 
	 * return returnFileName; }
	 */

	public static void zipSingleFile(Path source, String zipFileName) throws IOException {
		if (!Files.isRegularFile(source)) {
			System.err.println("Please provide a file.");
			return;
		}

		try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFileName));
				FileInputStream fis = new FileInputStream(source.toFile());) {

			ZipEntry zipEntry = new ZipEntry(source.getFileName().toString());
			zos.putNextEntry(zipEntry);

			byte[] buffer = new byte[1024];
			int len;
			while ((len = fis.read(buffer)) > 0) {
				zos.write(buffer, 0, len);
			}
			zos.closeEntry();
		}

	}
	
	public String generateExcelBase64(ArrayList<JSONObject> data, List<String> columnOrder) throws IOException {
	    if (data.isEmpty()) {
	        System.out.println("Skipping File Generation due to No Data!");
	        return null;
	    }

	    XSSFWorkbook workbook = new XSSFWorkbook();
	    XSSFSheet sheet = workbook.createSheet("Sheet");

	    // Write data rows only (no header row)
	    int rowNum = 0;
	    for (JSONObject obj : data) {
	        Row row = sheet.createRow(rowNum++);
	        int colNum = 0;

	        for (String header : columnOrder) {  // Use the correct column order
	            Cell cell = row.createCell(colNum++);
	            cell.setCellValue(obj.optString(header, ""));
	        }
	    }

	    // Auto-size columns
	    int colNum = 0;
	    for (String header : columnOrder) {
	        sheet.autoSizeColumn(colNum++);
	    }

	    // Convert the Excel file to a Base64 encoded string
	    try (ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream()) {
	        workbook.write(byteArrayOut);
	        workbook.close();

	        // Print raw byte data (optional)
	        System.out.println("Raw Byte Data: " + Arrays.toString(byteArrayOut.toByteArray()));

	        return Base64.getEncoder().encodeToString(byteArrayOut.toByteArray());
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new IOException("Error writing Excel file", e);
	    }
	}




	@SuppressWarnings("unused")
	private String formatHeader(String header) {
	    String[] words = header.split("_");
	    StringBuilder formattedHeader = new StringBuilder();
	    for (String word : words) {
	        if (word.isEmpty()) continue;
	        formattedHeader.append(Character.toUpperCase(word.charAt(0)))
	                       .append(word.substring(1).toLowerCase())
	                       .append(" ");
	    }
	    return formattedHeader.toString().trim();
	}

	public ArrayList<JSONObject> convertResultSetToJSON(ResultSet rs) throws Exception {
	    ArrayList<JSONObject> jsonList = new ArrayList<>();
	    ResultSetMetaData rsmd = rs.getMetaData();
	    int columnCount = rsmd.getColumnCount();

	    while (rs.next()) {
	        JSONObject obj = new JSONObject();
	        for (int i = 1; i <= columnCount; i++) {
	            obj.put(rsmd.getColumnName(i), rs.getObject(i));
	        }
	        jsonList.add(obj);
	    }

	    return jsonList;
	}
	
	public String generateCSV(ArrayList<Map<String, Object>> resultSet) {
	    StringBuilder csvBuilder = new StringBuilder();

	    @SuppressWarnings("unused")
		Map<String, Object> firstRow = resultSet.get(0);
	    for (Map<String, Object> row : resultSet) {
	        for (Object value : row.values()) {
	            csvBuilder.append(escapeCSVValue(value != null ? value.toString() : "")).append(",");
	        }
	        csvBuilder.deleteCharAt(csvBuilder.length() - 1).append("\n");
	    }
	    return csvBuilder.toString();
	}
	
	private String escapeCSVValue(String value) {
	    if (value == null) {
	        return "";
	    }
	    
	    // If value contains comma, quote, or newline, wrap in quotes and escape internal quotes
	    if (value.contains(",") || value.contains("\"") || value.contains("\n") || value.contains("\r")) {
	        return "\"" + value.replace("\"", "\"\"") + "\"";
	    }
	    
	    return value;
	}

	public String encodeBase64(String csvContent) {
	    byte[] csvBytes = csvContent.getBytes(StandardCharsets.UTF_8);
	    return Base64.getEncoder().encodeToString(csvBytes);
	}
	
	public String createPDFFromDatabase(ResultSet rs) throws Exception {
        // Create PDF
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, baos);
        document.open();
        
        // Add title
        document.add(new Paragraph("SWIFT Messages"));
        document.add(new Paragraph(" ")); // Empty line
        
        // Add text from database - each record on new page
        boolean firstRecord = true;
        while (rs.next()) {
            String text = rs.getString(1).replace("¦", "│").replace("+---", "└───"); // First column
            if (text != null) {
                // Add new page for each record (except first one)
                if (!firstRecord) {
                    document.newPage();
                }
                document.add(new Paragraph(text));
                firstRecord = false;
            }
        }
        
        document.close();
        
        // Close connections
        rs.close();
        
        // Convert to base64
        byte[] pdfBytes = baos.toByteArray();
        return Base64.getEncoder().encodeToString(pdfBytes);
    }
	
	private static String reportFile = "";
	JasperReport jasperReport1= null;
	
	JRResultSetDataSource objJRDS  = null;
	JasperPrint jasperPrint1 = null;
	public void downloadFileDetailsPDF( HttpServletResponse response, GenericReportVO objRptVO, ResultSet rs, String userKey ,Connection conn) 
	{
		String reportFolderPath = "src/main/resources/static/jasper/xmlReport/";
		String reportLogoPath = "src/main/resources/static/jasper/xmlReport/logo/";
		String destinationFolderPath = env.getProperty("download.file.path");
		try {	
 				reportFile = objRptVO.getXmlReportFileName();				
 				
 				DateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
 			    Date date = new Date();
 			    String currentDate = formatter.format(date);
 			    
 			    DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssmmm");
 			    String timeStamp= dateFormat.format(date);
 			    String outFileName = objRptVO.getReportOutFileName();
				
			    String outFile = "";
			    //List list = new ArrayList();
			    Map<String, Object> parameters = new HashMap<String, Object>();
			    
			    parameters.put("param_rpt_title",objRptVO.getReportTitle());
			    parameters.put("param_rpt_date",currentDate);
			    parameters.put("param_from_date",objRptVO.getRptSearchFromDate());
			    parameters.put("param_to_date",objRptVO.getRptSearchToDate());
			    
			    parameters.put("param_rpt_img_path",reportLogoPath);
			    parameters.put("param_rpt_branch_name",objRptVO.getRptBranchName());
			    parameters.put("param_rpt_user",objRptVO.getRptUserName());
			    parameters.put("param_rpt_name",objRptVO.getReportName());
			    parameters.put("param_input_1",objRptVO.getStringInParameter1());
			    parameters.put("param_input_2",objRptVO.getStringInParameter2());
			    parameters.put("param_search_branch",objRptVO.getSearchBranch());
			    
			    //user master
			    parameters.put("param_search_criteria",objRptVO.getSearchCriteriaDesc());
			    parameters.put("param_search_status",objRptVO.getSearchStatusDesc());
			    parameters.put("param_search_name",objRptVO.getSearchName());
			    parameters.put("param_search_loginid",objRptVO.getLoginID());
			    parameters.put("param_search_role_profile",objRptVO.getSearchRoleProfileId());
			    
			   //user role Master
			    parameters.put("param_search_roleprofile",objRptVO.getSearchRoleProfile());
			    parameters.put("param_search_roletype",objRptVO.getSearchRoleType());
			    
			    //solid id
			    parameters.put("param_search_type",objRptVO.getSearchTYPE());
			    parameters.put("param_search_bic",objRptVO.getbICCODE());
			    parameters.put("param_search_solid",objRptVO.getsOLID());
			    parameters.put("param_search_code",objRptVO.getSearchCode());
			    
			    //curency
			    parameters.put("param_search_FCY_name",objRptVO.getfCY());
			    parameters.put("param_search_FCY_code",objRptVO.getfCYDesc());
			    
			  //nostroAcc
			    parameters.put("param_search_nostro_name",objRptVO.getNostroName());
			    parameters.put("param_search_nostro_no",objRptVO.getNostroNo());
			    
			  //fraud
			    parameters.put("param_search_account_name",objRptVO.getAccountName());
			    parameters.put("param_search_account_no",objRptVO.getAccountNo());
			    
			  //BRS
			    parameters.put("prm_report_date",objRptVO.getBrsRptDate());
			    parameters.put("prm_msg_as_on",objRptVO.getBrsMsgAsOnDate());
			    parameters.put("prm_report_name",objRptVO.getReportName());
			    
			  //Mirror
			    parameters.put("prm_rpt_name",objRptVO.getReportName());
			    parameters.put("prm_rpr_date",objRptVO.getRptDate());
			    parameters.put("prm_user_id",objRptVO.getRptUserName());
			    parameters.put("prm_sol_id",objRptVO.getsOLID());
			    parameters.put("prm_date_from",objRptVO.getRptSearchFromDate());
			    parameters.put("prm_date_to",objRptVO.getRptSearchToDate());
			    
			    
			   
				try{
					if (rs != null) {
						objJRDS = new JRResultSetDataSource(rs);
					}
					String xmlPath1 = reportFolderPath+reportFile;
					System.out.println("xmlPath1 "+xmlPath1);
					jasperReport1 = JasperCompileManager.compileReport(new FileInputStream(xmlPath1));
					//objJRDS = fetchReportData(con, inParam1, inParam2);
					parameters.put("param_rpt_dataSource",objJRDS);
					jasperPrint1 = JasperFillManager.fillReport(jasperReport1, parameters, conn);
					//list.add(jasperPrint1);
				}catch(Exception e){
					e.printStackTrace();
					loggerUtil.logInfo("jasperReport1..."+e.getMessage());					
				}
				
				try {
					//exportToPDF(response, jasperPrint, title);
					JRPdfExporter pdfExporter = new JRPdfExporter();
					//centralizeCrosstabOnPage(jasperPrint1);
					//pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, list);
					//pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint1);
					//pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint2);
					/*pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,MessageUtil.getExternalBundleFilePath("dqi.destination.folder.path")+curMonHeaderId+"_DQIReport_"+timeStamp+".pdf");*/
					outFile = destinationFolderPath+""+outFileName+"_"+timeStamp+".pdf";
					//pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, outFile);
					
					pdfExporter.setExporterInput(new SimpleExporterInput(jasperPrint1));
					pdfExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outFile));
					
					SimplePdfReportConfiguration reportConfig = new SimplePdfReportConfiguration();
					reportConfig.setSizePageToContent(true);
					reportConfig.setForceLineBreakPolicy(false);

					SimplePdfExporterConfiguration exportConfig = new SimplePdfExporterConfiguration();
					exportConfig.setMetadataAuthor("baeldung");
					exportConfig.setEncrypted(true);
					exportConfig.setAllowedPermissionsHint("PRINTING");

					pdfExporter.setConfiguration(reportConfig);
					pdfExporter.setConfiguration(exportConfig);
					
					
					//System.out.println("Exporting report...");
					loggerUtil.logInfo("Exporting report...");
					pdfExporter.exportReport();
					//System.out.println("Done!");
					loggerUtil.logInfo("Done.....!");
				} catch (Exception e) {
					e.printStackTrace();
					loggerUtil.logErrorInsert(e.getMessage(), e, "ExportData-downloadFileDetailsPDF", userKey);
				}			    
				
				
				File fileToDownLoad = null;
				fileToDownLoad = new File(outFile);
				response.setHeader("Content-Disposition","attachment;filename="+fileToDownLoad.getName());

				FileInputStream fis = new FileInputStream(fileToDownLoad);	 	 
				ServletOutputStream  servletOutputStream = response.getOutputStream();

				try {
					byte[] buffer = new byte[1024 * 4];
					while (true) {
						int bytes = fis.read(buffer);
						if (bytes < 0)
							break;
						servletOutputStream.write(buffer, 0, bytes);
					}
					servletOutputStream.flush();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					servletOutputStream.close();
					fis.close();
				}
				
				if(fileToDownLoad.exists())
				{
					fileToDownLoad.delete();
				}
		   	
		} catch(Exception e) {		
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "ExportData-downloadFileDetailsPDF", userKey);
		} finally {
			if (objJRDS != null){
				objJRDS = null;
			}
		}
	}


}