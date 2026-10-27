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
import java.nio.file.Paths;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import in.ameya.model.master.GenericReportVO;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.fill.JRTemplatePrintFrame;
import net.sf.jasperreports.engine.fill.JRTemplatePrintRectangle;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimplePdfReportConfiguration;

@Component
public class ExportData {

	@Autowired
	private LoggerUtil loggerUtil;
	
	@Autowired
	private Environment env;
	
	/**
	 * @param response
	 * @param downloadString
	 * @param userKey
	 * @return
	 * @throws Exception
	 */
	public String downloadFileDetailsDeleteCSV(HttpServletResponse response, String downloadString, String userKey)
	{
		File fileToUpload = null;
		fileToUpload = new File(downloadString);
		response.setContentType("text/csv");
        response.setHeader("Content-Disposition","attachment;filename="+fileToUpload.getName());
        FileInputStream fis = null;
        ServletOutputStream  servletOutputStream = null;
        try
        {
        	fis = new FileInputStream(fileToUpload);	 	 
            servletOutputStream = response.getOutputStream();
        	byte[] buffer = new byte[1024*4];
			while (true) {
				int bytes = fis.read(buffer);
				if (bytes < 0)
					break;
				servletOutputStream.write(buffer, 0, bytes);
			}	
		}
	    catch(Exception e)
	    {
	    	downloadString = null;
	    	loggerUtil.logErrorInsert(e.getMessage(), e, "ExportData-downloadFileDetailsDeleteCSV", "");
	    }
        finally 
		{
        	try
        	{
	        	if(servletOutputStream != null)
	        	{
		        	servletOutputStream.flush();
					servletOutputStream.close();
	        	}
	        	if(fis != null)
	        	{
	        		fis.close();
	        	}
	        	if(fileToUpload != null)
	        	{
	        		fileToUpload.delete();
	        	}
        	}
        	catch(Exception e)
    	    {
    	    	downloadString = null;
    	    	loggerUtil.logErrorInsert(e.getMessage(), e, "ExportData-getUserMasterList", "");
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
	public String downloadFileDetailsSaveCSV(HttpServletResponse response, String downloadString, String userKey) throws Exception
	{
		File fileToUpload = null;
		fileToUpload = new File(downloadString);
		response.setContentType("text/csv");
        response.setHeader("Content-Disposition","attachment;filename="+fileToUpload.getName());
        FileInputStream fis = new FileInputStream(fileToUpload);	 	 
        ServletOutputStream  servletOutputStream = response.getOutputStream();

        try
        {
        	byte[] buffer = new byte[1024*4];
			while (true) {
				int bytes = fis.read(buffer);
				if (bytes < 0)
					break;
				servletOutputStream.write(buffer, 0, bytes);
			}			
		}
	    catch(Exception e)
	    {
	    	downloadString = null;
	    	loggerUtil.logErrorInsert(e.getMessage(), e, "ExportData-downloadFileDetailsSaveCSV", "");
	    }
        finally 
		{
        	if(servletOutputStream != null)
        	{
	        	servletOutputStream.flush();
				servletOutputStream.close();
        	}
        	if(fis != null)
        	{
        		fis.close();
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
	public String downloadFileDetailsSaveTXT(HttpServletResponse response, String downloadString, String userKey) throws Exception
	{
		File fileToUpload = null;
		fileToUpload = new File(downloadString);
		response.setContentType("text/plain");
        response.setHeader("Content-Disposition","attachment;filename="+fileToUpload.getName());
        FileInputStream fis = new FileInputStream(fileToUpload);
        ServletOutputStream  servletOutputStream = response.getOutputStream();

        try
        {
        	byte[] buffer = new byte[1024*4];
			while (true) {
				int bytes = fis.read(buffer);
				if (bytes < 0)
					break;
				servletOutputStream.write(buffer, 0, bytes);
			}
			//fileToUpload.delete();
			servletOutputStream.flush();
			
		}
	    catch(Exception e)
	    {
	    	downloadString = null;
	    	loggerUtil.logErrorInsert(e.getMessage(), e, "ExportData-downloadFileDetailsSaveTXT", "");
	    }
        finally 
		{
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
	public void downloadFileDetailsExcel( HttpServletResponse response,
			String sheetName, ResultSet rs, String userKey)
 	{
 	
 	try
		{	
	 		HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = null;
			sheet = hwb.createSheet(sheetName); 
			HSSFCell cell = null; 
			HSSFCellStyle headerStyle = hwb.createCellStyle();
			/*HSSFCellStyle dateStyle = hwb.createCellStyle();
			dateStyle.setDataFormat(hwb.createDataFormat().getFormat("dd/MM/yyyy H:mm:ss"));
			HSSFCellStyle amountStyle = hwb.createCellStyle();
			amountStyle.setDataFormat(hwb.createDataFormat().getFormat("00.00"));*/
			
			SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_1);  
		    Date date = new Date();
		    String currentDate = formatter.format(date);
			
			HSSFFont font = hwb.createFont();
			//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setBold(true);
			headerStyle.setFont(font);
			int index = 0;
			int rowCounter = 0;
			int sheetCounter = 0;
			int pageSize = 30000;
			String header = "";
			boolean isHeaderFound = false;
			if (rs != null) 
			{
				sheetCounter++;
				while(rs.next())
				{
					if(rowCounter > pageSize) {
						sheet = null;
						rowCounter = 0;
						index=0;
						sheet = hwb.createSheet(sheetName+sheetCounter);
						sheetCounter++;	
					}
					HSSFRow row = sheet.createRow((short)index);
					
					// to write header 
					if(rowCounter == 0 && !("").equalsIgnoreCase(header))
					{
						String headerArr[] = header.split("#");
						int headerSize = headerArr.length;
						
						for(int h = 0; h < headerSize; h++)
						{
							cell = row.createCell((int) (h));
							HSSFRichTextString richString = new HSSFRichTextString(headerArr[h]);
							cell.setCellValue(richString);
							cell.setCellStyle(headerStyle);
						}
						
						index++;
						rowCounter++;						
						row = sheet.createRow((short)index);							
					}
					
					
					if(index == 0)
					{
						cell = row.createCell((int) (0));
						HSSFRichTextString richString1 = new HSSFRichTextString("Report Date");
						cell.setCellValue(richString1);		
						cell.setCellStyle(headerStyle);
						
						cell = row.createCell((int) (1));
						HSSFRichTextString richString2 = new HSSFRichTextString(currentDate);
						cell.setCellValue(richString2);
						cell.setCellStyle(headerStyle);
	
						index++;
						row = sheet.createRow((short)index);
						
						index++;
						row = sheet.createRow((short)index);
					}
					ResultSetMetaData rsmd = rs.getMetaData();
				    int numCols = rsmd.getColumnCount();
					for(int i = 0; i < numCols; i++)
					{	
						if(!isHeaderFound)
						{
							header =  header+rs.getString(i+1) +"#";
							
							cell = row.createCell((int) (i));
							HSSFRichTextString richString = new HSSFRichTextString(rs.getString(i+1));
							cell.setCellValue(richString);
							cell.setCellStyle(headerStyle);
						}	
						else
						{
							cell = row.createCell((int) (i));
							HSSFRichTextString richString = new HSSFRichTextString(rs.getString(i+1));
							cell.setCellValue(richString);
						}
					}
					index++;
					rowCounter++;
					isHeaderFound = true;
				}
			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="+sheetName+".xls");
			OutputStream fileOut = response.getOutputStream();
			hwb.write(fileOut);
			fileOut.close();
			hwb.close();
		   	
		}catch(Exception e){		
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "ExportData-downloadFileDetailsExcel", "");
		}
		finally{
			if (rs != null) 
			{
		    	try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	rs = null;
			}
		}
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



	@SuppressWarnings("unused")
	private void centralizeCrosstabOnPage(JasperPrint jp)
	{
        if (jp != null && jp.getPages() != null) {
            for (JRPrintPage page : jp.getPages()) {
                JRTemplatePrintFrame frame = null;
                JRTemplatePrintRectangle rectangle = null;
                int pageWidth = jp.getPageWidth();
                int pageHeight = jp.getPageHeight();
                if (page.getElements() != null) {
 
                    for (JRPrintElement element : page.getElements()) {
                        if (element instanceof JRTemplatePrintFrame) {
                            frame = (JRTemplatePrintFrame) element;
                        } else if (element instanceof JRTemplatePrintRectangle) {
                            rectangle = (JRTemplatePrintRectangle) element;
                        }
                    }
                }
                int frameWidth = frame.getWidth();
                int frameHeight = frame.getHeight();
                int xPosition = (pageWidth - frameWidth) / 2;
                int yPosition = (pageHeight - frameHeight) / 2;
                frame.setX(xPosition);
                frame.setY(yPosition);
                
                if( rectangle != null ){
                	rectangle.setX(xPosition);
                	rectangle.setY(yPosition);
                }
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
	public String downloadFileDetailsDeleteFile(HttpServletResponse response, String downloadString, String contentType, String userKey)
	{
		File fileToUpload = null;
		fileToUpload = new File(downloadString);
		response.setContentType(contentType);
        response.setHeader("Content-Disposition","attachment;filename="+fileToUpload.getName());
        FileInputStream fis = null;
        ServletOutputStream  servletOutputStream = null;
        try
        {
        	fis = new FileInputStream(fileToUpload);	 	 
            servletOutputStream = response.getOutputStream();
        	byte[] buffer = new byte[1024*4];
			while (true) {
				int bytes = fis.read(buffer);
				if (bytes < 0)
					break;
				servletOutputStream.write(buffer, 0, bytes);
			}	
		}
	    catch(Exception e)
	    {
	    	downloadString = null;
	    	loggerUtil.logErrorInsert(e.getMessage(), e, "ExportData-getUserMasterList", "");
	    }
        finally 
		{
        	try
        	{
	        	if(servletOutputStream != null)
	        	{
		        	servletOutputStream.flush();
					servletOutputStream.close();
	        	}
	        	if(fis != null)
	        	{
	        		fis.close();
	        	}
	        	if(fileToUpload != null)
	        	{
	        		fileToUpload.delete();
	        	}
        	}
        	catch(Exception e)
    	    {
    	    	downloadString = null;
    	    	loggerUtil.logErrorInsert(e.getMessage(), e, "ExportData-downloadFileDetailsDeleteFile", "");
    	    }	
		}
        return downloadString;
	}
	
	public String getSpringURLforContentType(String contentType, String imageURL, String key)
	{
		String springURL = null;
		try
		{
			if(contentType != null)
			{
				if(contentType.contains("image"))
				{
					springURL = imageURL+"?selRec="+key;
				}
				else if(contentType.equalsIgnoreCase("application/pdf"))
				{
					springURL = "/resources/img/pdf-icon.png";
				}
				else if(contentType.equalsIgnoreCase("application/vnd.ms-excel") ||
						contentType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
						contentType.equalsIgnoreCase("text/csv"))
				{
					springURL = "/resources/img/excel-xls-icon.png";
				}
				else if(contentType.equalsIgnoreCase("application/msword") ||
						contentType.equalsIgnoreCase("application/vnd.oasis.opendocument.text"))
				{
					springURL = "/resources/img/doc-icon.png";
				}
				else if(contentType.equalsIgnoreCase("application/zip") ||
						contentType.equalsIgnoreCase("application/x-rar-compressed") ||
						contentType.equalsIgnoreCase("application/x-7z-compressed"))
				{
					springURL = "/resources/img/zip-icon.png";
				}
				else if(contentType.equalsIgnoreCase("application/rtf") ||
						contentType.equalsIgnoreCase("application/vnd.oasis.opendocument.text") ||
						contentType.equalsIgnoreCase("text/plain"))
				{
					springURL = "/resources/img/txt-icon.png";
				}
				else
				{
					springURL = "/resources/img/Document-Blank-icon.png";
				}
				
			}
			else{
				springURL = "/resources/img/Document-Blank-icon.png";
			}
		}
		catch(Exception e)
	    {
			springURL = null;
	    	loggerUtil.logErrorInsert(e.getMessage(), e, "ExportData-getSpringURLforContentType", "");
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
	public void downloadFileDetailsExcelWithParams( HttpServletResponse response,
			String sheetName, ResultSet rs, String userId, String searchparamVals, String userName, String solId)
 	throws Exception{
 	
 	try
		{	
	 		HSSFWorkbook hwb = new HSSFWorkbook();
			HSSFSheet sheet = null;
			sheet = hwb.createSheet(sheetName); 
			HSSFCell cell = null; 
			HSSFCellStyle headerStyle = hwb.createCellStyle();
			DataFormat format = hwb.createDataFormat();
			HSSFCellStyle amountStyle = hwb.createCellStyle();
			HSSFCellStyle rightStyle = hwb.createCellStyle();
			HSSFCellStyle leftStyle = hwb.createCellStyle();
			amountStyle.setDataFormat(format.getFormat("#,##00.00"));
			rightStyle.setAlignment(HorizontalAlignment.RIGHT);
			leftStyle.setAlignment(HorizontalAlignment.LEFT);
				
			SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_1);  
		    Date date = new Date();
		    String currentDate = formatter.format(date);
			
			HSSFFont font = hwb.createFont();
			//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setBold(true);
			headerStyle.setFont(font);
			int index = 0;
			HSSFRow row = sheet.createRow((short)index);
			cell = row.createCell((int) (0));
			HSSFRichTextString richString1 = new HSSFRichTextString("Report Name");
			cell.setCellValue(richString1);		
			cell.setCellStyle(headerStyle);
			
			cell = row.createCell((int) (1));
			HSSFRichTextString richString2 = new HSSFRichTextString(sheetName.replaceAll("_", " "));
			cell.setCellValue(richString2);
			cell.setCellStyle(headerStyle);
			
			index++;
			
			row = sheet.createRow((short)index);
			cell = row.createCell((int) (0));
			richString1 = new HSSFRichTextString("Report Date");
			cell.setCellValue(richString1);		
			cell.setCellStyle(headerStyle);
			
			cell = row.createCell((int) (1));
			richString2 = new HSSFRichTextString(currentDate);
			cell.setCellValue(richString2);
			cell.setCellStyle(headerStyle);
			
			index++;
			
			row = sheet.createRow((short)index);
			cell = row.createCell((int) (0));
			richString1 = new HSSFRichTextString("User ID");
			cell.setCellValue(richString1);		
			cell.setCellStyle(headerStyle);
			
			cell = row.createCell((int) (1));
			richString2 = new HSSFRichTextString(userName);
			cell.setCellValue(richString2);
			cell.setCellStyle(headerStyle);
			
			index++;
			
			row = sheet.createRow((short)index);
			cell = row.createCell((int) (0));
			richString1 = new HSSFRichTextString("SOL ID");
			cell.setCellValue(richString1);		
			cell.setCellStyle(headerStyle);
			
			cell = row.createCell((int) (1));
			richString2 = new HSSFRichTextString(solId);
			cell.setCellValue(richString2);
			cell.setCellStyle(headerStyle);
			
	
			index++;
			row = sheet.createRow((short)index);
			
			index++;
			//row = sheet.createRow((short)index);
			if(searchparamVals != null && searchparamVals.length() > 0){
				String[] searchparamValsSplit = searchparamVals.split("\\#");
				for(int x=0; x<searchparamValsSplit.length; x++){
					row = sheet.createRow((short)index);
					String[] paramSpl = searchparamValsSplit[x].split("\\|");
					
					for(int y=0; y<paramSpl.length; y++){
						cell = row.createCell((int) (y));
						richString1 = new HSSFRichTextString(("null").equalsIgnoreCase(paramSpl[y])?"":paramSpl[y]);
						cell.setCellValue(richString1);		
						cell.setCellStyle(headerStyle);
						
					}
					
					index ++;
				}
				index++;
			}
			
			row = sheet.createRow((short)index);
			
			int rowCounter = 0;
			int sheetCounter = 0;
			int pageSize = 30000;
			String header = "";
			boolean isHeaderFound = false;
			if (rs != null) 
			{
				sheetCounter++;
				while(rs.next())
				{
					if(rowCounter > pageSize) {
						sheet = null;
						rowCounter = 0;
						index=0;
						sheet = hwb.createSheet(sheetName+sheetCounter);
						sheetCounter++;	
					}
					row = sheet.createRow((short)index);
					
					// to write header 
					if(rowCounter == 0 && !("").equalsIgnoreCase(header))
					{
						String headerArr[] = header.split("#");
						int headerSize = headerArr.length;
						
						for(int h = 0; h < headerSize; h++)
						{
							cell = row.createCell((int) (h));
							HSSFRichTextString richString = new HSSFRichTextString(headerArr[h]);
							cell.setCellValue(richString);
							cell.setCellStyle(headerStyle);
						}
						
						index++;
						rowCounter++;						
						row = sheet.createRow((short)index);							
					}
											
					ResultSetMetaData rsmd = rs.getMetaData();
				    int numCols = rsmd.getColumnCount();
					for(int i = 0; i < numCols; i++)
					{	
						if(!isHeaderFound)
						{
								header =  header+rs.getString(i+1) +"#";
								
								cell = row.createCell((int) (i));
								HSSFRichTextString richString = new HSSFRichTextString(rs.getString(i+1));
								cell.setCellValue(richString);
								cell.setCellStyle(headerStyle);
						}	

						String str1 = rs.getString(i+1);
						cell = row.createCell((int) (i));
						HSSFRichTextString richString = new HSSFRichTextString(str1);
						boolean flag = false; 
						flag = Constants.isNumeric(str1);
						if(flag)
						{
							str1 = str1.replaceAll(",", "");
							if(("0.00").equalsIgnoreCase(str1) || str1.contains("."))
							{
								Double d = Double.parseDouble(str1);
								cell.setCellStyle(amountStyle);
								cell.setCellValue(d);
							}
							else
							{
								Double d = Double.parseDouble(str1);
								str1 = String.format ("%.0f", d);
								cell.setCellStyle(rightStyle);
								cell.setCellValue(str1);
							}
						}else
						{
							cell.setCellValue(richString);								
						}	
					
						/*else
						{
							cell = row.createCell((int) (i));
							HSSFRichTextString richString = new HSSFRichTextString(rs.getString(i+1));
							cell.setCellValue(richString);
						}*/
					}				
					
					index++;
					rowCounter++;
					isHeaderFound = true;
				}
			}
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-Disposition", "attachment; filename="+sheetName+".xls");
			OutputStream fileOut = response.getOutputStream();
			hwb.write(fileOut);
			fileOut.close();
			hwb.close();
		   	
		}catch(Exception e){		
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaExportData-downloadFileDetailsExcel", userId);
		}
		finally{
			if (rs != null) 
			{
				rs.close();
				rs = null;
			}
		}
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
	public String createExcelInZipForEmailReports(String fileNameMain, ArrayList<ExportDataResultSet> resultSetArray)
 	throws Exception{
		
 	String returnFileName = "";
 	
 	try
		{	
 		    String filePath = env.getProperty("download.file.path");
 		    
 		    String format = "yyyy-MM-dd";
 	        DateFormat dateFormatter = new SimpleDateFormat(format);
 	        Date date = new Date();
 			String strDate = dateFormatter.format(date);
 			filePath = filePath + strDate;
 			File theDir = new File(filePath);
 			if (!theDir.exists()){
 			    theDir.mkdirs();
 			}
 			
 			filePath = filePath +"\\";
 			Date dateFileName = new Date(System.currentTimeMillis());
 		    SimpleDateFormat formatFileName = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
 		   			
 			String fileName = filePath+fileNameMain+"-"+formatFileName.format(dateFileName);
 		    
	 		HSSFWorkbook hwb = new HSSFWorkbook();
						
			Iterator<ExportDataResultSet> it = resultSetArray.iterator();
			while(it.hasNext())
			{		
				ExportDataResultSet exportDataResultSet = it.next();
				ResultSet rs = exportDataResultSet.getResultSet();
				
				HSSFSheet sheet = null;
				sheet = hwb.createSheet(exportDataResultSet.getSheetName()); 
				HSSFCell cell = null; 
				HSSFCellStyle headerStyle = hwb.createCellStyle();
					
				SimpleDateFormat formatter = new SimpleDateFormat(Constants.DATE_FORMAT_1);  
			    Date date1 = new Date();
			    String currentDate = formatter.format(date1);
				
				HSSFFont font = hwb.createFont();
				//font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				font.setBold(true);
				headerStyle.setFont(font);
				int index = 0;
				HSSFRow row = sheet.createRow((short)index);
				cell = row.createCell((int) (0));
				HSSFRichTextString richString1 = new HSSFRichTextString("Report Name");
				cell.setCellValue(richString1);		
				cell.setCellStyle(headerStyle);
				
				cell = row.createCell((int) (1));
				HSSFRichTextString richString2 = new HSSFRichTextString(exportDataResultSet.getSheetName().replaceAll("_", " "));
				cell.setCellValue(richString2);
				cell.setCellStyle(headerStyle);
				
				index++;
				
				row = sheet.createRow((short)index);
				cell = row.createCell((int) (0));
				richString1 = new HSSFRichTextString("Report Date");
				cell.setCellValue(richString1);		
				cell.setCellStyle(headerStyle);
				
				cell = row.createCell((int) (1));
				richString2 = new HSSFRichTextString(currentDate);
				cell.setCellValue(richString2);
				cell.setCellStyle(headerStyle);
					
				index++;
				
				row = sheet.createRow((short)index);
				
				index++;
							
				row = sheet.createRow((short)index);
				
				index++;
				
				int rowCounter = 0;
				int sheetCounter = 0;
				int pageSize = 30000;
				String header = "";
				boolean isHeaderFound = false;
				if (rs != null) 
				{
					sheetCounter++;					
					while(rs.next())
					{
						
						if(rowCounter > pageSize) {
							sheet = null;
							rowCounter = 0;
							index=0;
							sheet = hwb.createSheet(exportDataResultSet.getSheetName()+sheetCounter);
							sheetCounter++;	
						}
						row = sheet.createRow((short)index);
						
						// to write header 
						if(rowCounter == 0 && !("").equalsIgnoreCase(header))
						{
							String headerArr[] = header.split("#");
							int headerSize = headerArr.length;
							
							for(int h = 0; h < headerSize; h++)
							{
								cell = row.createCell((int) (h));
								HSSFRichTextString richString = new HSSFRichTextString(headerArr[h]);
								cell.setCellValue(richString);
								cell.setCellStyle(headerStyle);
							}
							
							index++;
							rowCounter++;						
							row = sheet.createRow((short)index);							
						}
												
						ResultSetMetaData rsmd = rs.getMetaData();
					    int numCols = rsmd.getColumnCount();
						for(int i = 0; i < numCols; i++)
						{	
							if(!isHeaderFound)
							{
									header =  header+rs.getString(i+1) +"#";
									
									cell = row.createCell((int) (i));
									HSSFRichTextString richString = new HSSFRichTextString(rs.getString(i+1));
									cell.setCellValue(richString);
									cell.setCellStyle(headerStyle);
							}	
							else
							{
								cell = row.createCell((int) (i));
								HSSFRichTextString richString = new HSSFRichTextString(rs.getString(i+1));
								cell.setCellValue(richString);
							}
						}				
						
						index++;
						rowCounter++;
						isHeaderFound = true;
					}
				}
				
				if (rs != null) 
				{
					rs.close();
					rs = null;
				}
			}
			//Write file
			FileOutputStream fileOut = new FileOutputStream(fileName+".xls");
			hwb.write(fileOut);
	        fileOut.close();
	        hwb.close();
	        
	        Path source = Paths.get(fileName+".xls");
	        returnFileName = fileName+".zip";
	        
	        zipSingleFile(source, returnFileName);
	        
	        File f = new File(fileName+".xls");
	        if(f.exists())
	        {
	        	f.delete();
	        }		        
		   	
		}catch(Exception e){		
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "AmeyaExportData-createExcelInZipForEmailReports", "");
		}
		finally{
			
		}
 	
 		return returnFileName;
	}	
	
	public static void zipSingleFile(Path source, String zipFileName)
	        throws IOException 
	{
        if (!Files.isRegularFile(source)) {
            System.err.println("Please provide a file.");
            return;
        }

        try (
            ZipOutputStream zos = new ZipOutputStream(
                new FileOutputStream(zipFileName));
            FileInputStream fis = new FileInputStream(source.toFile());
        ) {

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
}
