package in.ameya.schedulers.schedulersUtil.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import in.ameya.util.LoggerUtil;



@Component
public class SchedulerUtil {
	
	public static final int  SOURCE_SWIFT = 0;
	
	@Autowired
	private LoggerUtil loggerUtil;
	
	public String getMURNumberFileDate(File file, int sourceSystem)
	{
		String murNumber = "";
		String fileDate = "";
		String returnValue = "";
		
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			if(sourceSystem == SOURCE_SWIFT)
			{
				fileDate = sdf.format(file.lastModified());
				returnValue = ""+"#"+fileDate;
				
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				String strData = "";
				String tagMur = "{108:SC";
				String tagDate = "{177:";
				while((strData = br.readLine()) != null)
				{
					if(strData.contains(tagMur))
					{
						String a = strData.substring(strData.indexOf("{108:"));
						murNumber = a.substring(5, a.indexOf("}"));							
					}
					if(strData.contains(tagDate))
					{
						String a = strData.substring(strData.indexOf(tagDate));
						fileDate = a.substring(5, a.indexOf("}"));	
						fileDate = fileDate.substring(2, 4) + "/"+ 
								fileDate.substring(4, 6) +"/20"+
								fileDate.substring(0, 2)+" "+
								fileDate.substring(6, 8)+":"+
								fileDate.substring(8, 10)+":"+"00";
					    fileDate = sdf.format(sdf.parse(fileDate));
					}	
					if(!("").equalsIgnoreCase(murNumber) || !("").equalsIgnoreCase(fileDate))
					{
						returnValue = murNumber +"#"+fileDate;
					}
					break;
				}
				
				if(("").equalsIgnoreCase(returnValue))
				{
					fileDate = sdf.format(file.lastModified());
					returnValue = ""+"#"+fileDate;
				}
				
				if(br != null)
				{
					br.close();
				}
				if(fr != null)
				{
					fr.close();
				}
			}
			
		}
		catch(Exception e)
		{
			murNumber = "";
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-getMURNumberFileDate", "");
		}
		
		return returnValue;
	}
	
	
	public void copyFileToRespectedDir(String fileNameWithPath, String destinationPath)
	{
		
		try
		{
				File fDest = new File(destinationPath);		
				if(!fDest.exists())
				{
					Files.move 
					        (Paths.get(fileNameWithPath),  
					        Paths.get(destinationPath)); 	
				}else
				{
					File fSource = new File(fileNameWithPath);	
					if(fSource.exists())
					{
						fSource.delete();
					}
				}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-copyFileToRespectedDir", "");
		}
	}
	
	
	public String GetSwiftFileType(String FilePath) throws IOException
	{
		FileReader fr = new FileReader(FilePath);
		BufferedReader br = new BufferedReader(fr);
		String strData = null;
		
		try{
			strData = br.readLine();
			fr.close();			if(strData == null)
				return "";
			strData = strData.substring(strData.indexOf("{2:") + 4).substring(0, 3);
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-GetSwiftFileType", "");
		}
		return strData;
	}
	
	public String GetMessageTypeCode(String InputString)
	{
		String messageTypeCode = "1";
		String Tag = "{2:";
		
		try{
			messageTypeCode = InputString.substring(InputString.indexOf(Tag) + 4).substring(0, 3);			
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-GetMessageTypeCode", "");
		}
		return messageTypeCode;
	}
	
	public boolean IsNumeric(String strData)
	{
		try
		{
			Double.parseDouble(strData);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	
	public String GetInwardOutwardType(String InputString)
	{
		String inwardOutwardType = "2";
		String Tag = "{2:";
		
		try{
			inwardOutwardType = InputString.substring(InputString.indexOf(Tag) + 3).substring(0, 1);
			if(("I").equalsIgnoreCase(inwardOutwardType))
			{
				inwardOutwardType = "2"; // Outward
			}
			else
			{
				inwardOutwardType = "1"; // inward
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-GetInwardOutwardType", "");
		}
		return inwardOutwardType;
	}
	
	public String GetAckNackedFlag(String InputString)
	{
		String ackNackedFlag = "0";
		String Tag = "{451:";
		
		try{
			if(InputString != null)
			{
				if(InputString.contains(Tag))
				ackNackedFlag = InputString.substring(InputString.indexOf(Tag) + 5).substring(0, 1);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-GetInwardOutwardType", "");
		}
		return ackNackedFlag;
	}
	
	public ReturnValue fnFindByTagsDesc(Hashtable<String, Integer> hshTags, String TagDesc)
	{
		ReturnValue objRetValue = new ReturnValue();
		
		try{
			Integer intIndex = -1;
			for(Enumeration<String> e = hshTags.keys(); e.hasMoreElements();)
			{
				// strKey is tag code 
				String strKey = e.nextElement();
				// if TagDesc (actual key) is found in tag list ie matched with message list
				// ex : it will check :50
				if(strKey.indexOf(TagDesc) > -1)
				{
					// find key of tag in Hash Map 
					intIndex = hshTags.get(strKey);
					TagDesc = strKey;
					break;
				}
			}
			objRetValue.intRetVal = intIndex;
			objRetValue.strRetVal = TagDesc;
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnFindByTagsDesc", "");
		}
		return objRetValue;
	}
	
	// used in 300 series
	public ReturnValue fnFindByTagsDescFromTagToTag(Hashtable<String, Integer> hshTags, String TagDesc, String fromTag, String toTag)
	{
		ReturnValue objRetValue = new ReturnValue();
		
		try{
			Integer intIndex = -1;
			boolean start = false;
			boolean end = true;
			
			ArrayList<Map.Entry<String, Integer>> list = new ArrayList(hshTags.entrySet());
		       Collections.sort(list, new Comparator<Map.Entry<?, Integer>>(){

		         public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
		            return o1.getValue().compareTo(o2.getValue());
		        }});
			
		   		       
			//for(Enumeration<String> e = hshTags.keys(); e.hasMoreElements();)
		    Iterator it = list.iterator();  
		    while (it.hasNext())   
			{	
		    	Map.Entry me = (Map.Entry)it.next();
		    	
		    	//String mainStrKey = it.next().toString().split("=")[0];
		    	//String strKey = mainStrKey;
		    	String mainStrKey = (String) me.getKey();
		    	String strKey = mainStrKey;
		    	
		    	if(strKey.contains("|"))
		    	{
		    		strKey = strKey.split("\\|")[0];
		    	}
		    	//System.out.println("strKey-->"+strKey);
				if(fromTag.indexOf(strKey) > -1)
				{
					start = true;
				}
				if(toTag.indexOf(strKey.substring(0, toTag.length())) > -1 && !fromTag.equalsIgnoreCase(strKey) && start)
				{
					end = false;
				}
				if(strKey.indexOf(TagDesc) > -1 && start && end)
				{
					// find key of tag in Hash Map 
					intIndex = hshTags.get(mainStrKey);
					TagDesc = strKey;
					break;
				}
			}
			objRetValue.intRetVal = intIndex;
			objRetValue.strRetVal = TagDesc;
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnFindByTagsDescFromTagToTag", "");
		}
		return objRetValue;
	}
	
	// used in 300 series
	public ReturnValue fnFindByTagsDescFromTagToTagOfOccourance(Hashtable<String, Integer> hshTags, String TagDesc, String fromTag, String toTag, int occourance)
	{
		ReturnValue objRetValue = new ReturnValue();
		
		try{
			Integer intIndex = -1;
			boolean start = false;
			boolean end = true;
			
			ArrayList<Map.Entry<String, Integer>> list = new ArrayList(hshTags.entrySet());
		       Collections.sort(list, new Comparator<Map.Entry<?, Integer>>(){

		         public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
		            return o1.getValue().compareTo(o2.getValue());
		        }});
			
		   		       
			//for(Enumeration<String> e = hshTags.keys(); e.hasMoreElements();)
		    Iterator it = list.iterator();  
		    int i = 0;
		    while (it.hasNext())   
			{	
		    	Map.Entry me = (Map.Entry)it.next();
		    	
		    	//String mainStrKey = it.next().toString().split("=")[0];
		    	//String strKey = mainStrKey;
		    	String mainStrKey = (String) me.getKey();
		    	String strKey = mainStrKey;
		    	
		    	if(strKey.contains("|"))
		    	{
		    		strKey = strKey.split("\\|")[0];
		    	}
		    	//System.out.println("strKey-->"+strKey);
				if(fromTag.indexOf(strKey) > -1)
				{
					start = true;
				}
				if(toTag.indexOf(strKey.substring(0, toTag.length())) > -1 && !fromTag.equalsIgnoreCase(strKey) && start)
				{
					end = false;
				}
				if(strKey.indexOf(TagDesc) > -1 && start && end)
				{
					i = i + 1;
					// find key of tag in Hash Map 
					intIndex = hshTags.get(mainStrKey);
					TagDesc = strKey;
					if(occourance == i) 
						break;
				}
				
			}
			objRetValue.intRetVal = intIndex;
			objRetValue.strRetVal = TagDesc;
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnFindByTagsDescFromTagToTag", "");
		}
		return objRetValue;
	}
	
	public ReturnValue fnFindByTagsDescList(Hashtable<String, ArrayList<Integer>> hshTags, String TagDesc)
	{
		ReturnValue objRetValue = new ReturnValue();
		
		try{
			ArrayList<Integer> intIndex = new ArrayList<Integer>();
		
			for(Enumeration<String> e = hshTags.keys(); e.hasMoreElements();)
			{
				// strKey is tag code 
				String strKey = e.nextElement();
				// if TagDesc (actual key) is found in tag list ie matched with message list
				// ex : it will check :50
				if(strKey.indexOf(TagDesc) > -1)
				{
					// find key of tag in Hash Map 
					intIndex = hshTags.get(strKey);
					TagDesc = strKey;
					break;
				}
			}
			
			objRetValue.lstIntRetVal = intIndex;
			objRetValue.strRetVal = TagDesc;
		
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnFindByTagsDesc", "");
		}
		return objRetValue;
	}	
	
	public ReturnValue fnGetCustomData(ArrayList<String> lstRecord, Integer intPosition, Integer MaxRows)
	{
		String strData = "";
		Integer i = 0;
		ReturnValue objRetValue = new ReturnValue();
		
		try{
			for(intPosition = intPosition + 1; intPosition < lstRecord.size(); intPosition++)
			{
				String strTemp = lstRecord.get(intPosition);
				if(strTemp.indexOf(":") != 0 && strTemp.indexOf("-}") != 0 && i < MaxRows)
				{
					strData = strData + strTemp;
					i += 1;
				}
				else
					break;
			}
				objRetValue.intRetVal = intPosition;
				objRetValue.strRetVal = strData;
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnGetCustomData", "");
		}
		return objRetValue;
	}
	
	/**
	 * @param lstRecord
	 * @param intPosition
	 * @param MaxRows
	 * added by ketan for line wise separator
	 * @return
	 */
	public ReturnValue fnGetCustomDataLineSeperator(ArrayList<String> lstRecord, Integer intPosition, Integer MaxRows)
	{
		String strData = "";
		Integer i = 0;
		ReturnValue objRetValue = new ReturnValue();
		
		try{
			for(intPosition = intPosition + 1; intPosition < lstRecord.size(); intPosition++)
			{
				String strTemp = lstRecord.get(intPosition);
				if(strTemp.indexOf(":") != 0 && strTemp.indexOf("-}") != 0 && i < MaxRows)
				{
					if(i == 0)
					{
						strData = "#_*";
					}
					strData = strData + strTemp + "#_*";
					i += 1;
				}
				else
					break;
			}
			
			if(!("").equalsIgnoreCase(strData))
			{
				if(strData.contains("#_*"))
				{
					strData = strData.substring(0, strData.length()-3);
				}
			}
			objRetValue.intRetVal = intPosition;
			objRetValue.strRetVal = strData;
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnGetCustomDataLineSeperator", "");
		}
		return objRetValue;
	}
	
	public ReturnValue fnGetCustomDataWithSpace(ArrayList<String> lstRecord, Integer intPosition, Integer MaxRows)
	{
		String strData = "";
		Integer i = 0;
		ReturnValue objRetValue = new ReturnValue();
		
		try{
			for(intPosition = intPosition + 1; intPosition < lstRecord.size(); intPosition++)
			{
				String strTemp = lstRecord.get(intPosition);
				if(strTemp.indexOf(":") != 0 && strTemp.indexOf("-}") != 0 && i < MaxRows)
				{
					strData = strData + strTemp + " ";
					i += 1;
				}
				else
					break;
			}
			objRetValue.intRetVal = intPosition;
			objRetValue.strRetVal = strData;
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnGetCustomDataWithSpace", "");
		}
		return objRetValue;
	}
	
	// this method is to get custom data if data is more than 1 line 
	public ReturnValue fnGetCustomData(ArrayList<String> lstRecord, Integer intPosition, Integer MaxRows,
			Integer DelimitAfter)
	{
		String strData = "";
		Integer i = 0;
		ReturnValue objRetValue = new ReturnValue();
		
		try{
			for(intPosition = intPosition + 1; intPosition < lstRecord.size(); intPosition++)
			{
				// read line 
				String strTemp = lstRecord.get(intPosition);
				// string should not contain : , -} and check max rows
				if(strTemp.indexOf(":") != 0 && strTemp.indexOf("-}") != 0 && i < MaxRows)
				{
					strData = strData +" "+ strTemp;
					// first line is name then address separated by "#_*" (DelimitAfter means put "#_*" after first vale ie ex. separator for name and address
					if(i == DelimitAfter - 1) strData += "#_*";
					i += 1;
				}
				else
					break;
			}
			objRetValue.intRetVal = intPosition;
			objRetValue.strRetVal = strData;
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnGetCustomData", "");
		}
		return objRetValue;
	}
	
	// this method is to get delimited custom data if data is more than 1 line 
	public ReturnValue fnGetCustomData(ArrayList<String> lstRecord, Integer intPosition, Integer MaxRows,
			String strDelimiter)
	{
		String strData = "";
		Integer i = 0;
		ReturnValue objRetValue = new ReturnValue();
		
		try{
			for(intPosition = intPosition + 1; intPosition < lstRecord.size(); intPosition++)
			{
				// read line 
				String strTemp = lstRecord.get(intPosition);
				// string should not contain : , -} and check max rows
				if(strTemp.indexOf(":") != 0 && strTemp.indexOf("-}") != 0 && i < MaxRows)
					strData += strTemp + strDelimiter;
				else
					break;
				i++;
			}
			objRetValue.intRetVal = intPosition;
			objRetValue.strRetVal = strData;
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnGetCustomData", "");
		}
		return objRetValue;
	}
	
	public String ReceiverBIC(String InputString)
	{
		String strReceiverBIC = "";
		String Tag = "{1:";
		try{
			// starting position of tag is =>InputString.indexOf(Tag) and +6 
			Integer intPosition = InputString.indexOf(Tag) + 6;
			if(intPosition > 0 && InputString.length() > intPosition + 12)
			{
				// if msg start index is 0 then total 0 to 11 characters
				strReceiverBIC = InputString.substring(intPosition, intPosition + 12);
			}
			/*if(intPosition > 0 && InputString.length() > intPosition + 8)
			{
				// if msg start index is 0 then total 0 to 8 characters
				strReceiverBIC = InputString.substring(intPosition, intPosition + 8);
			}*/
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-ReceiverBIC", "");
		}
		return strReceiverBIC;
	}
	
	
	public String UUID(String InputString)
	{
		String strUUIDC = "";
		String Tag = "{121:";
		try{
			if(InputString.contains("{121:"))
			{
				Integer intPosition = InputString.indexOf(Tag) + 5;
				if(intPosition > 0 && InputString.length() > intPosition + 36)
				{
					strUUIDC = InputString.substring(intPosition, intPosition + 36);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-UUID", "");
		}
		return strUUIDC;
	}
	
	public String SenderCode(String InputString)
	{
		String strSenderCode = "";
		String Tag = "{2:";
		
		try{
			// staring position of tag + 4
			Integer intPosition = InputString.indexOf(Tag) + 4;
			if(intPosition < Tag.length()) return strSenderCode;
			
			Integer intStartIndex = -1;
			char[] chrArray = InputString.toCharArray();
			// form intPosition+4 to all string characters 
			for(int i = intPosition, j = 0; i < chrArray.length; i++, j++)
			{
				if(!IsNumeric(String.valueOf(chrArray[i])))
				{
					intStartIndex = intPosition + j;
					break;
				}
			}
			// if staring from 4 then total 4 to 12 ie 8 characters.
			if(intStartIndex > -1 && intStartIndex + 12 <= InputString.length())
				strSenderCode = InputString.substring(intStartIndex, intStartIndex + 12);	
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-SenderCode", "");
		}
		return strSenderCode;
	}
	
	public String TRFNo(String InputString)
	{
		String strTRFNo = "";
		String Tag = ":20:";
		
		try{
			// starting point of tag
			Integer intPosition = InputString.indexOf(Tag) + Tag.length();
			if(intPosition < Tag.length()) return strTRFNo;
			// total tag value
			strTRFNo = InputString.substring(intPosition);		
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-TRFNo", "");
		}
		return strTRFNo;
	}
	
	public String ValDate(String InputString, String Tag)
	{
		// InputString is total line of tag :32A:
		String strValDate = "";
		try{
			// staring position of tag + tag length (intPosition = 5)
			Integer intPosition = InputString.indexOf(Tag) + Tag.length();
			if(intPosition < Tag.length()) return strValDate;
			// total first 6 characters of data 
			strValDate = InputString.substring(intPosition, intPosition + 6);	
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-ValDate", "");
		}
		return strValDate;
	}
	
	public String findTagValueCurrency(String tagDesc, String line)
	{
		String currencyCode = "";
		try{
				
			if(line != null)
			{
				if(line.contains(tagDesc))
				{
					currencyCode = line.substring(11, 14); 
				}
			}
		}
		catch (Exception e) {
			currencyCode = "";
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-findTagValueCurrency", "");
		}
		return currencyCode;
	}
	
	public String CurrCode(String InputString, String Tag)
	{
		String strCurrCode = "";
		
		try{
			// staring position of tag + tag length (intPosition = 5)
			Integer intPosition = InputString.indexOf(Tag) + Tag.length();
			if(intPosition < Tag.length()) return strCurrCode;
			
			//After Tag, First 6 chars are of value date
			intPosition = intPosition + 6;
			
			if(InputString.length() <= intPosition + 1) return strCurrCode;
			// first 6 is value date next 3 is Currency Code (index from 6 to 8)
			strCurrCode = InputString.substring(intPosition, intPosition + 3);		
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-CurrCode", "");
		}
		return strCurrCode;
	}
		
	public String Amount(String InputString, String Tag)
	{
		String strAmount = "";
		
		try{
			// staring position of tag + tag length (intPosition = 5)
			Integer intPosition = InputString.indexOf(Tag) + Tag.length();
			if(intPosition < Tag.length()) return strAmount;
			
			//After Tag, First 6 chars are of value date, then 3 chars are of CurrCode
			intPosition = intPosition + 6 + 3; 
			
			if(InputString.length() <= intPosition + 1) return strAmount;
			// first 6 is value date next 3 is Currency Code then amt (intPosition = 14)
			// replace "," with "." and remove space
			strAmount = InputString.substring(intPosition).replace(",", ".").replace(" ", "");
			if(strAmount.indexOf(".") + 1 == strAmount.length())
				strAmount += "00";
			else if(!IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1))) // check value after "." is numeric or not
				strAmount  = strAmount.substring(0, strAmount.indexOf(".")) + ".00"; // if not found then add .00 after amount
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-Amount", "");
		}
		return strAmount;
	}
	
	public String CustomTags(String InputString, String Tag)
	{
		//CurrInst, ExchangeRate
		String strCustomData = "";
		
		try{
			// starting index of tag + total length of tag ex if tag is :23B: then intPosition = 5
			Integer intPosition = InputString.indexOf(Tag) + Tag.length();
			if(intPosition < Tag.length()) return strCustomData;
			// take only value of message tag code ie data
			strCustomData = InputString.substring(intPosition);
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-CustomTags", "");
		}
		return strCustomData;
	}
	
	// MT101 start
	public String CurrCode1stPos(String InputString, String Tag)
	{
		String strCurrCode = "";
		
		try{
			Integer intPosition = InputString.indexOf(Tag) + Tag.length();
			if(intPosition < Tag.length()) return strCurrCode;
			// total first 3 characters of data 
			strCurrCode = InputString.substring(intPosition, intPosition + 3);		
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-CurrCode1stPos", "");
		}
		return strCurrCode;
	}
	
	public String Amount2ndPos(String InputString, String Tag)
	{
		String strAmount = "";
		
		try{
			// staring position of tag + tag length (intPosition = 5)
			Integer intPosition = InputString.indexOf(Tag) + Tag.length();
			if(intPosition < Tag.length()) return strAmount;
			//After Tag, First 3 chars are of CurrCode
			intPosition = intPosition + 3; 
			
			if(InputString.length() <= intPosition + 1) return strAmount;
			// first 3 is Currency Code then amt (intPosition = 8)
			// replace "," with "." and remove space
			strAmount = InputString.substring(intPosition).replace(",", ".").replace(" ", "");
			if(strAmount.indexOf(".") + 1 == strAmount.length())
				strAmount += "00";
			else if(!IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1))) // check value after "." is numeric or not
				strAmount  = strAmount.substring(0, strAmount.indexOf(".")) + ".00"; // if not found then add .00 after amount
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-Amount2ndPos", "");
		}
		return strAmount;
	}
	
	public String ExchangeRateMT101(String InputString, String Tag)
	{
		String strAmount = "";
		
		try{
			// staring position of tag + tag length (intPosition = 5)
			Integer intPosition = InputString.indexOf(Tag) + Tag.length();
			if(intPosition < Tag.length()) return strAmount;
			if(InputString.length() <= intPosition + 1) return strAmount;
			// replace "," with "." and remove space
			strAmount = InputString.substring(intPosition).replace(",", ".").replace(" ", "");
			if(strAmount.indexOf(".") + 1 == strAmount.length())
				strAmount += "00";
			else if(!IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1))) // check value after "." is numeric or not
				strAmount  = strAmount.substring(0, strAmount.indexOf(".")) + ".00"; // if not found then add .00 after amount
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-ExchangeRateMT101", "");
		}
		return strAmount;
	}
	
	public String FormatAmount(String InputString)
	{
		try
		{
			String strAmount = InputString.replace(",", ".").replace(" ", "");
			if(strAmount.indexOf(".") + 1 == strAmount.length())
				strAmount += "00";
			else if(!IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1)))
				strAmount  = strAmount.substring(0, strAmount.indexOf(".")) + ".00";
			return strAmount;
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-FormatAmount", "");
		}
		return "";
	}
	// end MT101
	
	
	
	public ArrayList<ArrayList<String>> fnReadFileIntoList(String strFilePath, 
			ArrayList<Hashtable<String, Integer>> lstTags) 
			throws IOException
	{
		ArrayList<ArrayList<String>> lstRecords = new ArrayList<ArrayList<String>>();
		Integer Current = -1;
		FileReader fr = new FileReader(strFilePath);
		BufferedReader br = new BufferedReader(fr);
		String strData = "";
		//int i = 0;
		boolean blnBeginningOfFile = true;
		
		try{
			while((strData = br.readLine()) != null)
			{
				// for first line
				if(blnBeginningOfFile)
				{
					// create new array object
					// lstRecords to save line and lstTags to save tags like :20:
					lstRecords.add(new ArrayList<String>());
					lstTags.add(new Hashtable<String, Integer>());
					Current++;
					blnBeginningOfFile = false;
				}
				
				// chk for new message in line if found then new array object
				if(strData.indexOf("$") > 0)
				{
					String strTempData = strData.substring(0, strData.indexOf("$"));
					lstRecords.get(Current).add(strTempData);
					
					strData = strData.substring(strData.indexOf("$"));
					lstRecords.add(new ArrayList<String>());
					lstTags.add(new Hashtable<String, Integer>());
					Current++;
					lstRecords.get(Current).add(strData);
				}
				else // for same message add tag and data in list
				{
					lstRecords.get(Current).add(strData);
					if(strData.indexOf(":") == 0 &&
							strData.length() > 1)
					{
						String strTag = strData.substring(0, strData.indexOf(":", 1) + 1);
						if(lstTags.get(Current).containsKey(strTag)) // if same tag repeat then append tag 
						{
							lstTags.get(Current).put(strTag + "|" + String.valueOf(lstRecords.get(Current).size()),
									lstRecords.get(Current).size() - 1);
						}
						else
						{
							lstTags.get(Current).put(strTag, lstRecords.get(Current).size() - 1);
						}
					}
				}
				//i++;
			}
			fr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnReadFileIntoList", "");
		}
		return lstRecords;
	}
	
	public ArrayList<ArrayList<String>> fnReadFileIntoList(String strFilePath,
			ArrayList<Hashtable<String, Integer>> lstTags, 
			ArrayList<Hashtable<Integer, Hashtable<String, ArrayList<Integer>>>> lstRpTags) 
			throws IOException
	{
		
		ArrayList<ArrayList<String>> lstRecords = new ArrayList<ArrayList<String>>();
		
		Integer Current = -1;
		Integer InnerCounter = -1;
		FileReader fr = new FileReader(strFilePath);
		BufferedReader br = new BufferedReader(fr);
		String strData = "";

		boolean blnBeginningOfFile = true;
		boolean blnHeaderStarted = true;

		try{
			while((strData = br.readLine()) != null)
			{
				if(strData.indexOf(":21:") == 0)
				{
					InnerCounter++;
					blnHeaderStarted = false;
				}
				
				// for first line
				if(blnBeginningOfFile)
				{
					// create new array object
					// lstRecords to save line and lstTags to save tags like :20:
					lstRecords.add(new ArrayList<String>());
					lstTags.add(new Hashtable<String, Integer>());
					lstRpTags.add(new Hashtable<Integer, Hashtable<String,ArrayList<Integer>>>());
					
					Current++;
					blnBeginningOfFile = false;
					blnHeaderStarted = true;
				}
				
				// check for new message in line if found then new array object
				if(strData.indexOf("$") > 0)
				{
					String strTempData = strData.substring(0, strData.indexOf("$"));
					lstRecords.get(Current).add(strTempData);
					strData = strData.substring(strData.indexOf("$"));
					
					lstRecords.add(new ArrayList<String>());
					lstTags.add(new Hashtable<String, Integer>());
					lstRpTags.add(new Hashtable<Integer, Hashtable<String,ArrayList<Integer>>>());
					
					Current++;
					lstRecords.get(Current).add(strData);
					blnHeaderStarted = true;
				}
				else // for same message add tag and data in list
				{
					lstRecords.get(Current).add(strData);
					if(blnHeaderStarted)
					{
						if(strData.indexOf(":") == 0 &&
								strData.length() > 1)
						{
							String strTag = strData.substring(0, strData.indexOf(":", 1) + 1);
							if(!lstTags.get(Current).containsKey(strTag)) // if same tag repeat then append tag 
							{
								lstTags.get(Current).put(strTag, lstRecords.get(Current).size() - 1);
							}
							else
							{
								//Logic Not defined
							}
						}
					}
					else
					{
						if(!lstRpTags.get(Current).containsKey(InnerCounter) 
								|| strData.indexOf(":21:") == 0)
						{
							Hashtable<String,ArrayList<Integer>> hshInnerTable
								= new Hashtable<String,ArrayList<Integer>>();
							lstRpTags.get(Current).put(InnerCounter, hshInnerTable);
						}
						if(strData.indexOf(":") == 0 && strData.length() > 1)
						{
							String strTag = strData.substring(0, strData.indexOf(":", 1) + 1);
							if(!lstRpTags.get(Current).get(InnerCounter).containsKey(strTag)) //for :23 (repeating)
							{
								lstRpTags.get(Current).get(InnerCounter).put(strTag, 
										new ArrayList<Integer>());
								lstRpTags.get(Current).get(InnerCounter).get(strTag).add(
								lstRecords.get(Current).size() - 1);
							}
							else
							{
								lstRpTags.get(Current).get(InnerCounter).get(strTag).add(
										lstRecords.get(Current).size() - 1);
							}
						}
					}
				}
			}
			fr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnReadFileIntoList", "");
		}
		return lstRecords;
	}
	
	
	public ArrayList<ArrayList<String>> fnReadFileIntoListCompleteMsgList(String strFilePath,
			ArrayList<Hashtable<String, Integer>> lstTags, ArrayList<String> completeMsgList)
			throws IOException
	{
		ArrayList<ArrayList<String>> lstRecords = new ArrayList<ArrayList<String>>();
		Integer Current = -1;
		FileReader fr = new FileReader(strFilePath);
		BufferedReader br = new BufferedReader(fr);
		String strData = "";
		//int i = 0;
		boolean blnBeginningOfFile = true;
		String completeMsg = "";

		try{
			while((strData = br.readLine()) != null)
			{
				// for first line
				if(blnBeginningOfFile)
				{
					// create new array object
					// lstRecords to save line and lstTags to save tags like :20:
					lstRecords.add(new ArrayList<String>());
					lstTags.add(new Hashtable<String, Integer>());
					Current++;
					blnBeginningOfFile = false;
				}

				// chk for new message in line if found then new array object
				if(strData.indexOf("$") > 0)
				{
					String strTempData = strData.substring(0, strData.indexOf("$"));
					lstRecords.get(Current).add(strTempData);

					// added for complete msg
					if(!completeMsg.contains("-}"))
					{
						completeMsg += "\r\n-}";
					}
					if(completeMsg.startsWith("-}$"))
					{
						completeMsg = completeMsg.substring(3, completeMsg.length());
					}
					completeMsgList.add(completeMsg);
					completeMsg = "";
					completeMsg = strData;
					//end complete msg add

					strData = strData.substring(strData.indexOf("$"));
					lstRecords.add(new ArrayList<String>());
					lstTags.add(new Hashtable<String, Integer>());
					Current++;
					lstRecords.get(Current).add(strData);
				}
				else // for same message add tag and data in list
				{
					completeMsg += "\r\n"+strData;
					lstRecords.get(Current).add(strData);
					if(strData.indexOf(":") == 0 &&
							strData.length() > 1)
					{
						String strTag = strData.substring(0, strData.indexOf(":", 1) + 1);
						if(lstTags.get(Current).containsKey(strTag)) // if same tag repeat then append tag
						{
							lstTags.get(Current).put(strTag + "|" + String.valueOf(lstRecords.get(Current).size()),
									lstRecords.get(Current).size() - 1);
						}
						else
						{
							lstTags.get(Current).put(strTag, lstRecords.get(Current).size() - 1);
						}
					}
				}
				//i++;
			}
			// added for complete msg
			if(!completeMsg.contains("-}"))
			{
				completeMsg += "\r\n-}";
			}
			if(completeMsg.startsWith("-}$"))
			{
				completeMsg = completeMsg.substring(3, completeMsg.length());
			}
			completeMsgList.add(completeMsg);
			//end complete msg add
			fr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnReadFileIntoListCompleteMsgList", "");
		}
		return lstRecords;
	}
	
	public ArrayList<Integer> fnFindMultipleTagsByDesc(Hashtable<String, Integer> hshTags, String TagDesc,
			ArrayList<String> lstTagDesc)
	{
		ArrayList<Integer> lstIndex = new ArrayList<Integer>();

		try{
			for(Enumeration<String> e = hshTags.keys(); e.hasMoreElements();)
			{
				String strKey = e.nextElement();
				if(strKey.indexOf(TagDesc) > -1)
				{
					lstIndex.add(hshTags.get(strKey));
					lstTagDesc.add(strKey);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnFindMultipleTagsByDesc", "");
		}
		return lstIndex;
	}
	
	public ReturnValue fnFindByTagsDescMultiple(Hashtable<String, Integer> hshTags, String TagDesc)
	{
		ReturnValue objRetValue = new ReturnValue();
		Map<Integer,String> unsortedMap = new HashMap<Integer,String>();
		try{
			Integer intIndex = -1;
			for(Enumeration<String> e = hshTags.keys(); e.hasMoreElements();)
			{
				// strKey is tag code
				String strKey = e.nextElement();
				String strKeySplit = strKey.split("\\|")[0];

				// if TagDesc (actual key) is found in tag list ie matched with message list
				// ex : it will check :50
				if(strKeySplit.indexOf(TagDesc) > -1)
				{
					// find key of tag in Hash Map
					if(strKey.length() == 4 || strKey.length() == 5){
						intIndex = hshTags.get(strKey);
					}else{
						intIndex = Integer.parseInt(strKey.split("\\|")[1]) - 1;
					}
					TagDesc = strKeySplit;
					unsortedMap.put(intIndex, TagDesc);
				}
			}
			objRetValue.intRetValArr = new Integer[unsortedMap.size()];
			objRetValue.strRetValArr = new String[unsortedMap.size()];

			Map<Integer,String> treeMap = new TreeMap<Integer,String>(unsortedMap);
    		Set set = treeMap.entrySet();
		    Iterator iterator = set.iterator();
		    int k = 0;
		    while(iterator.hasNext()) {
		      Map.Entry me = (Map.Entry)iterator.next();
		      objRetValue.intRetValArr[k] = (Integer) me.getKey();
			  objRetValue.strRetValArr[k] = (String) me.getValue();
			  k++;
		    }
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnFindByTagsDescMultiple", "");
		}
		return objRetValue;
	}

	public ReturnValue fnFindByTagsDescFromTagToTagMultiple(Hashtable<String, Integer> hshTags, String TagDesc, String fromTag, String toTag)
	{
		ReturnValue objRetValue = new ReturnValue();
		Map<Integer,String> unsortedMap = new HashMap<Integer,String>();
		try{
			Integer intIndex = -1;
			
			boolean start = false;
			boolean end = true;
			
			ArrayList<Map.Entry<String, Integer>> list = new ArrayList(hshTags.entrySet());
		       Collections.sort(list, new Comparator<Map.Entry<?, Integer>>(){

		         public int compare(Map.Entry<?, Integer> o1, Map.Entry<?, Integer> o2) {
		            return o1.getValue().compareTo(o2.getValue());
		        }});
			
		    Iterator it = list.iterator();  
		    while (it.hasNext())   
			{	
				// strKey is tag code
		    	Map.Entry me = (Map.Entry)it.next();
		    	String mainStrKey = (String) me.getKey();
				String strKey = mainStrKey;
				//System.out.println("strKey-->"+strKey);
				String strKeySplit = strKey.split("\\|")[0];

				// if TagDesc (actual key) is found in tag list ie matched with message list
				// ex : it will check :50
				
				if(strKey.contains("|"))
		    	{
		    		strKey = strKey.split("\\|")[0];
		    	}
		    	//System.out.println("strKey-->"+strKey);
				if(fromTag.indexOf(strKey) > -1)
				{
					start = true;
				}
				if(toTag.indexOf(strKey.substring(0, toTag.length())) > -1 && !fromTag.equalsIgnoreCase(strKey) && start)
				{
					end = false;
				}
				if(strKeySplit.indexOf(TagDesc) > -1 && start && end)
				{
					// find key of tag in Hash Map
					if(strKey.length() == 4 || strKey.length() == 5){
						intIndex = hshTags.get(mainStrKey);
					}else{
						intIndex = Integer.parseInt(mainStrKey.split("\\|")[1]) - 1;
					}
					TagDesc = strKeySplit;
					unsortedMap.put(intIndex, TagDesc);
				}
			}
			objRetValue.intRetValArr = new Integer[unsortedMap.size()];
			objRetValue.strRetValArr = new String[unsortedMap.size()];

			Map<Integer,String> treeMap = new TreeMap<Integer,String>(unsortedMap);
    		Set set = treeMap.entrySet();
		    Iterator iterator = set.iterator();
		    int k = 0;
		    while(iterator.hasNext()) {
		      Map.Entry me = (Map.Entry)iterator.next();
		      objRetValue.intRetValArr[k] = (Integer) me.getKey();
			  objRetValue.strRetValArr[k] = (String) me.getValue();
			  k++;
		    }
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnFindByTagsDescFromTagToTagMultiple", "");
		}
		return objRetValue;	
	}

	
	public boolean getTagOfCorrectSequence(String allTags, String mandatoryFirstTag, String checkTag, 
			int noOfLoop, String endTag)
	{
		boolean flag = false;

		try
		{
			if(allTags != null && !("").equalsIgnoreCase(allTags))
			{
				String allTagsArr[] = allTags.split("\\|");
				int checkCount = 0;
				for(int i = 0; i < allTagsArr.length; i++)
				{
					if(allTagsArr[i].equalsIgnoreCase(mandatoryFirstTag))
					{
						checkCount = 1;
					}
					if(allTagsArr[i].equalsIgnoreCase(endTag))
					{
						break;
					}
					if(checkCount > 0 && checkCount <= noOfLoop)
					{
						if(allTagsArr[i].equalsIgnoreCase(checkTag))
						{
							flag = true;
							break;
						}
						checkCount = checkCount + 1;
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-getTagOfCorrectSequence", "");
		}

		return flag;
	}
	
	public String fnReadFileAllTagsTag(String strFilePath)
			throws IOException
	{
		String strTag = "";
		FileReader fr = new FileReader(strFilePath);
		BufferedReader br = new BufferedReader(fr);
		String strData = "";

		try{
			while((strData = br.readLine()) != null)
			{
				if(strData.indexOf(":") == 0 &&
						strData.length() > 1)
				{
					if(("").equalsIgnoreCase(strTag))
					{
						strTag = strData.substring(0, strData.indexOf(":", 1) + 1);
					}
					else
					{
						strTag = strTag + "|" +strData.substring(0, strData.indexOf(":", 1) + 1);
					}
				}
			}
			fr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnReadFileAllTagsTag", "");
		}
		return strTag;
	}
	
	/*public String fnReadFileAllTagsTagStartTagEndTag(String strFilePath, String startTag, String endTag, int noOfOccourance)
			throws IOException
	{
		String strTag = "";
		FileReader fr = new FileReader(strFilePath);
		BufferedReader br = new BufferedReader(fr);
		String strData = "";
		int tagCount = 0;
		
		try{
			while((strData = br.readLine()) != null)
			{
				if(strData.indexOf(":") == 0 &&
						strData.length() > 1)
				{
					if(("").equalsIgnoreCase(strTag))
					{
					    String tempTag = strData.substring(0, strData.indexOf(":", 1) + 1);
						if(startTag.equalsIgnoreCase(tempTag))
						{
							if(tagCount == noOfOccourance)
							{
								strTag = tempTag;
							}
							tagCount = tagCount + 1;
						}
					}
					else
					{
						String tempTag = strData.substring(0, strData.indexOf(":", 1) + 1);
						
						if(endTag.equalsIgnoreCase(tempTag))
						{
							strTag = strTag + "|" + tempTag;
							break;
						}
						else
						{
							strTag = strTag + "|" + tempTag;
						}
					}
				}
			}
			fr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnReadFileAllTagsTagStartTagEndTag", "");
		}
		return strTag;
	}*/
	
	public String CurrCodeFirst(String InputString, String Tag)
	{
		String strCurrCode = "";

		try{
			// staring position of tag + tag length (intPosition = 5)
			Integer intPosition = InputString.indexOf(Tag) + Tag.length();
			if(intPosition < Tag.length()) return strCurrCode;

			if(InputString.length() <= intPosition + 1) return strCurrCode;
			strCurrCode = InputString.substring(intPosition, intPosition + 3);
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-CurrCodeFirst", "");
		}
		return strCurrCode;
	}

	public String AmountSecond(String InputString, String Tag)
	{
		String strAmount = "";

		try{
			// staring position of tag + tag length (intPosition = 5)
			Integer intPosition = InputString.indexOf(Tag) + Tag.length();
			if(intPosition < Tag.length()) return strAmount;

			//After Tag, First 6 chars are of value date, then 3 chars are of CurrCode
			intPosition = intPosition + 3;

			if(InputString.length() <= intPosition + 1) return strAmount;
			// first 6 is value date next 3 is Curr Code then amt (intPosition = 14)
			// replace "," with "." and remove space
			strAmount = InputString.substring(intPosition).replace(",", ".").replace(" ", "");
			if(strAmount.indexOf(".") + 1 == strAmount.length())
				strAmount += "00";
			else if(!IsNumeric(strAmount.substring(strAmount.indexOf(".") + 1))) // check value after "." is numeric or not
				strAmount  = strAmount.substring(0, strAmount.indexOf(".")) + ".00"; // if not found then add .00 after amount
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-AmountSecond", "");
		}
		return strAmount;
	}
	
	public ArrayList<ArrayList<String>> fnReadFileIntoListByTag(String strFilePath,
			ArrayList<Hashtable<String, Integer>> lstTags, 
			ArrayList<Hashtable<Integer, Hashtable<String, ArrayList<Integer>>>> lstRpTags, String tag) 
			throws IOException
	{
		
		ArrayList<ArrayList<String>> lstRecords = new ArrayList<ArrayList<String>>();
		
		Integer Current = -1;
		@SuppressWarnings("unused")
		Integer InnerCounter = -1;
		FileReader fr = new FileReader(strFilePath);
		BufferedReader br = new BufferedReader(fr);
		String strData = "";

		boolean blnBeginningOfFile = true;
		boolean blnHeaderStarted = true;

		try{
			while((strData = br.readLine()) != null)
			{
				if(strData.indexOf(tag) == 0)
				{
					InnerCounter++;
					blnHeaderStarted = false;
				}
				
				// for first line
				if(blnBeginningOfFile)
				{
					// create new array object
					// lstRecords to save line and lstTags to save tags like :20:
					lstRecords.add(new ArrayList<String>());
					lstTags.add(new Hashtable<String, Integer>());
					lstRpTags.add(new Hashtable<Integer, Hashtable<String,ArrayList<Integer>>>());
					
					Current++;
					blnBeginningOfFile = false;
					blnHeaderStarted = true;
				}
				
				// check for new message in line if found then new array object
				if(strData.indexOf("$") > 0)
				{
					String strTempData = strData.substring(0, strData.indexOf("$"));
					lstRecords.get(Current).add(strTempData);
					strData = strData.substring(strData.indexOf("$"));
					
					lstRecords.add(new ArrayList<String>());
					lstTags.add(new Hashtable<String, Integer>());
					lstRpTags.add(new Hashtable<Integer, Hashtable<String,ArrayList<Integer>>>());
					
					Current++;
					lstRecords.get(Current).add(strData);
					blnHeaderStarted = true;
				}
				else // for same message add tag and data in list
				{
					lstRecords.get(Current).add(strData);
					if(blnHeaderStarted)
					{
						if(strData.indexOf(":") == 0 &&
								strData.length() > 1)
						{
							String strTag = strData.substring(0, strData.indexOf(":", 1) + 1);
							if(!lstTags.get(Current).containsKey(strTag)) // if same tag repeat then append tag 
							{
								lstTags.get(Current).put(strTag, lstRecords.get(Current).size() - 1);
							}
							else
							{
								//Logic Not defined
							}
						}
					}
					else
					{
						if(!lstRpTags.get(Current).containsKey(InnerCounter) 
								|| strData.indexOf(tag) == 0)
						{
							Hashtable<String,ArrayList<Integer>> hshInnerTable
								= new Hashtable<String,ArrayList<Integer>>();
							lstRpTags.get(Current).put(InnerCounter, hshInnerTable);
						}
						if(strData.indexOf(":") == 0 && strData.length() > 1)
						{
							String strTag = strData.substring(0, strData.indexOf(":", 1) + 1);
							if(!lstRpTags.get(Current).get(InnerCounter).containsKey(strTag)) //for :23 (repeating)
							{
								lstRpTags.get(Current).get(InnerCounter).put(strTag, 
										new ArrayList<Integer>());
								lstRpTags.get(Current).get(InnerCounter).get(strTag).add(
								lstRecords.get(Current).size() - 1);
							}
							else
							{
								lstRpTags.get(Current).get(InnerCounter).get(strTag).add(
										lstRecords.get(Current).size() - 1);
							}
						}
					}
				}
			}
			fr.close();
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnReadFileIntoListByTag", "");
		}
		return lstRecords;
	}
	
	
	public ArrayList<String> getTagSubSeqData(String allTags, String mandatoryFirstTag, String mandatorySecondTag)
	{
		String tempTagData = "";
		ArrayList<String> tagList = new ArrayList<String>();
		try
		{
			if(allTags != null && !("").equalsIgnoreCase(allTags))
			{
				String allTagsArr[] = allTags.split("\\|");
				int firstIdx = -1;
				int secondIdx = -1;
				int checkIdx = 0;
				
				for(int i = 0; i < allTagsArr.length; i++)
				{
					String strTagwithData = allTagsArr[i];
					String tag = "";
					if(strTagwithData.contains(":"))
					{
						tag =  strTagwithData.substring(strTagwithData.indexOf(":", strTagwithData.indexOf(":")), strTagwithData.indexOf(":", strTagwithData.indexOf(":") + 1)+1);
						//System.out.println("tag-->"+tag);
					}
					if(tag.equalsIgnoreCase(mandatoryFirstTag) && firstIdx ==-1)
					{
						firstIdx = i;
					}
					if(tag.equalsIgnoreCase(mandatorySecondTag))
					{
						secondIdx = i;
					}	
					
					if((":NA:").equalsIgnoreCase(mandatorySecondTag) && secondIdx == -1)
					{
						secondIdx = allTagsArr.length - 1;
					}
				}
				if(firstIdx != -1 && secondIdx != -1)
				{
					for(int j = firstIdx; j <= secondIdx; j++)
					{
						tempTagData = allTagsArr[j];
						tagList.add(tempTagData);
						checkIdx =1;
					}
				}
				if(checkIdx == 1)
				return tagList;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-getTagSubSeqData", "");
		}
		return tagList;
	}

	
	public ArrayList<String> getTagSubSeqDataWithOccourance(String allTags, String mandatoryFirstTag, String mandatorySecondTag, int noOfOccourance)
	{
		String tempTagData = "";
		ArrayList<String> tagList = new ArrayList<String>();
		int countOccourance = 0;
		
		try
		{
			if(allTags != null && !("").equalsIgnoreCase(allTags))
			{
				String allTagsArr[] = allTags.split("\\|");
				int firstIdx = -1;
				int secondIdx = -1;
				int checkIdx = 0;
				
				for(int i = 0; i < allTagsArr.length; i++)
				{
					String strTagwithData = allTagsArr[i];
					String tag = "";
					if(strTagwithData.contains(":"))
					{
						tag =  strTagwithData.substring(strTagwithData.indexOf(":", strTagwithData.indexOf(":")), strTagwithData.indexOf(":", strTagwithData.indexOf(":") + 1)+1);
					}
							
					if(tag.equalsIgnoreCase(mandatoryFirstTag))
					{
						if(noOfOccourance == countOccourance)
						{
							firstIdx = i;
							countOccourance = countOccourance + 1;
						}
						else
						{
							//firstIdx = -1;
							countOccourance = countOccourance + 1;
						}
					}
					if(tag.equalsIgnoreCase(mandatorySecondTag))
					{
						secondIdx = i;
					}	
					
					if((":NA:").equalsIgnoreCase(mandatorySecondTag) && secondIdx == -1)
					{
						secondIdx = allTagsArr.length - 1;
					}
				}
				if(firstIdx != -1 && secondIdx != -1)
				{
					int secondTagFound = 0;
					for(int j = firstIdx; j <= secondIdx; j++)
					{
						tempTagData = allTagsArr[j];
						String tag = tempTagData.substring(0, tempTagData.indexOf(":", 1) + 1);
						if(tag.equalsIgnoreCase(mandatoryFirstTag)) // this logic is to end loop if same starting tag found again, this will not add second repeat tag
						{
							secondTagFound = secondTagFound + 1;
						}
						if(secondTagFound == 1)
						{
							tagList.add(tempTagData);
						}
						checkIdx =1;
					}
				}
				if(checkIdx == 1)
				return tagList;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-getTagSubSeqDataWithOccourance", "");
		}
		return tagList;
	}
	
	//Get data of of multiple lines and separated by #_# s
	public String fnReadFileAllTagsWithNextLineData(String strFilePath, int current) 
			throws IOException
	{
		String strTag = "";
		FileReader fr = new FileReader(strFilePath);
		BufferedReader br = new BufferedReader(fr);
		String strData = "";
		boolean blnBeginningOfFile = true;
		String strTagRet = "";
		
		try{
			while((strData = br.readLine()) != null)
			{
				if(strData.indexOf(":") == 0 &&
						strData.length() > 1)
				{
					if(("").equalsIgnoreCase(strTag))
					{
						strTag = strData;
					}
					else
					{
						strTag = strTag + "|" +strData;
					}					
				}	
				if(strData.indexOf("$") > 0)
				{					
					strData = strData.substring(strData.indexOf("$"));
					if(("").equalsIgnoreCase(strTag))
					{
						strTag = strData;
					}
					else
					{
						strTag = strTag + "$";
					}
				}
				else if(strData.indexOf(":") != 0 && blnBeginningOfFile == false  && strData.indexOf("-}") != 0 && strData.length() > 1)
				{
					if(("").equalsIgnoreCase(strTag))
					{
						strTag = strData;
					}
					else
					{
						strTag = strTag + "#_*" +strData;
					}
				}
				blnBeginningOfFile = false;
			}
			fr.close();
			
			// logic to psss string of message string for given reference number, added because file may contain multiple records
			if(!("").equalsIgnoreCase(strTag))
			{
				String arrStr[] = strTag.split("\\$");
				strTagRet = arrStr[current];
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-fnReadFileAllTagsWithNextLineData", "");
		}
		return strTagRet;
	}
	
	
	public ArrayList<String> getMessageString(String strFilePath)
	{
		ArrayList<String> lstRecords = new ArrayList<String>();
		
		try
		{
			Path path = Paths.get(strFilePath);
			byte[] fileBytes = Files.readAllBytes(path);
			String fileContent = new String(fileBytes);
			String fileStrArry[] = fileContent.split("\\$");
			if(fileStrArry != null)
			{
				for(int i = 0; i < fileStrArry.length; i++)
				{
					String msgStr = fileStrArry[i];
					if(msgStr != null)
					{
						msgStr = msgStr.replaceAll("\r\n", "#_*");
					}
					lstRecords.add(msgStr);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			loggerUtil.logErrorInsert(e.getMessage(), e, "(Schedular)SchedulerUtil-getMessageString", "");
		}
		
		return lstRecords;
	}

	/**
	 * Determines IN_OUT_FLAG based on message content
	 * Returns 1 if message contains 'O' after {2:, returns 2 if contains 'I', otherwise returns 0
	 */
	public int getInOutFlag(File file) {
	    int inOutFlag = 0;
	    BufferedReader reader = null;
	    
	    try {
	        reader = new BufferedReader(new FileReader(file));
	        String line;
	        
	        // Pattern to match {2:O... or {2:I...
	        Pattern pattern = Pattern.compile("\\{2:([OI])");
	        
	        while ((line = reader.readLine()) != null) {
	            Matcher matcher = pattern.matcher(line);
	            if (matcher.find()) {
	                String direction = matcher.group(1);
	                if ("O".equals(direction)) {
	                    inOutFlag = 1;
	                    break;
	                } else if ("I".equals(direction)) {
	                    inOutFlag = 2;
	                    break;
	                }
	            }
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	        loggerUtil.logErrorInsert(e.getMessage(), e, "RunParser9: getInOutFlag", "");
	    } finally {
	        if (reader != null) {
	            try {
	                reader.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }
	    
	    return inOutFlag;
	}
	
	public String getFileTypeFromFileName(File file) {

	    if (file == null || file.getName() == null) {
	        return "OTHER_TXNS_INCL_BILL";
	    }

	    String fileName = file.getName().toUpperCase();

	    if (fileName.contains("MIRROR")) {
	        return "CREDENCE_MIRROR";
	    } 
	    else if (fileName.contains("FITT")) {
	        return "FITT_INWARD";
	    } 
	    else if (fileName.contains("FOTT")) {
	        return "FOTT_OUTWARD";
	    } 
	    else {
	        return "OTHER_TXNS_INCL_BILL";
	    }
	}

}
